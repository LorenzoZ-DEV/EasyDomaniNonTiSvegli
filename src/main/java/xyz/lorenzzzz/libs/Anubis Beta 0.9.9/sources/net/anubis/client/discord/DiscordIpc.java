package net.anubis.client.discord;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.sun.jna.Platform;
import java.io.Closeable;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.net.SocketTimeoutException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/discord/DiscordIpc.class */
@Environment(EnvType.CLIENT)
public final class DiscordIpc implements Closeable {
    private static final int OP_HANDSHAKE = 0;
    private static final int OP_FRAME = 1;
    private static final int OP_CLOSE = 2;
    private static final int OP_PING = 3;
    private static final int OP_PONG = 4;
    private static final int RPC_VERSION = 1;
    private static final int SOCKET_SLOTS = 10;
    private static final int MAX_FRAME_BYTES = 1048576;
    private final Pipe pipe;
    private final ByteBuffer header = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
    private static final String[] UNIX_TEMP_VARIABLES = {"XDG_RUNTIME_DIR", "TMPDIR", "TMP", "TEMP"};
    private static final boolean WINDOWS = Platform.isWindows();
    private static final long REPLY_TIMEOUT_NANOS = TimeUnit.SECONDS.toNanos(5);
    private static final long READ_POLL_NANOS = TimeUnit.MILLISECONDS.toNanos(20);
    private static final long PID = ProcessHandle.current().pid();
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/discord/DiscordIpc$Pipe.class */
    @Environment(EnvType.CLIENT)
    private interface Pipe extends Closeable {
        int readWaiting(ByteBuffer byteBuffer) throws IOException;

        void write(ByteBuffer byteBuffer, long j) throws IOException;
    }

    private DiscordIpc(Pipe pipe) {
        this.pipe = pipe;
    }

    public static DiscordIpc connect(String applicationId) throws Exception {
        Pipe pipe = WINDOWS ? openWindowsPipe() : openUnixSocket();
        if (pipe == null) {
            return null;
        }
        DiscordIpc ipc = new DiscordIpc(pipe);
        try {
            ipc.handshake(applicationId);
            return ipc;
        } catch (IOException | RuntimeException failure) {
            ipc.close();
            throw failure;
        }
    }

    public void setActivity(JsonObject activity) throws RefusedException, IOException {
        JsonObject reply;
        String nonce = UUID.randomUUID().toString();
        JsonObject args = new JsonObject();
        args.addProperty("pid", Long.valueOf(PID));
        args.add("activity", activity != null ? activity : JsonNull.INSTANCE);
        JsonObject command = new JsonObject();
        command.addProperty("cmd", "SET_ACTIVITY");
        command.add("args", args);
        command.addProperty("nonce", nonce);
        send(1, command);
        long deadline = System.nanoTime() + REPLY_TIMEOUT_NANOS;
        while (true) {
            reply = answer(readFrame(deadline, true));
            if (reply != null && nonce.equals(string(reply, "nonce"))) {
                break;
            }
        }
        if ("ERROR".equals(string(reply, "evt"))) {
            throw new RefusedException(errorMessage(reply));
        }
    }

    public void poll() throws IOException {
        while (true) {
            Frame frame = readFrame(System.nanoTime() + REPLY_TIMEOUT_NANOS, false);
            if (frame != null) {
                answer(frame);
            } else {
                return;
            }
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        try {
            this.pipe.close();
        } catch (IOException e) {
        }
    }

    private void handshake(String applicationId) throws IOException {
        JsonObject hello = new JsonObject();
        hello.addProperty("v", 1);
        hello.addProperty("client_id", applicationId);
        send(0, hello);
        long deadline = System.nanoTime() + REPLY_TIMEOUT_NANOS;
        while (true) {
            JsonObject reply = answer(readFrame(deadline, true));
            if (reply != null && "DISPATCH".equals(string(reply, "cmd"))) {
                String event = string(reply, "evt");
                if ("READY".equals(event)) {
                    return;
                }
                if ("ERROR".equals(event)) {
                    throw new IOException("Discord refused the handshake: " + errorMessage(reply));
                }
            }
        }
    }

    private JsonObject answer(Frame frame) throws IOException {
        switch (frame.opcode()) {
            case 1:
                return frame.json();
            case 2:
                throw new IOException("Discord closed the connection: " + errorMessage(frame.json()));
            case 3:
                send(4, frame.body());
                return null;
            default:
                return null;
        }
    }

    private Frame readFrame(long deadline, boolean wait) throws IOException {
        this.header.clear();
        if (!wait && this.pipe.readWaiting(this.header) == 0) {
            return null;
        }
        readFully(this.header, deadline);
        this.header.flip();
        int opcode = this.header.getInt();
        int length = this.header.getInt();
        if (length < 0 || length > 1048576) {
            throw new IOException("Discord sent a frame of " + length + " bytes");
        }
        ByteBuffer body = ByteBuffer.allocate(length);
        readFully(body, deadline);
        return new Frame(opcode, body.array());
    }

    private void readFully(ByteBuffer buffer, long deadline) throws IOException {
        while (buffer.hasRemaining()) {
            if (this.pipe.readWaiting(buffer) <= 0) {
                if (System.nanoTime() - deadline >= 0) {
                    throw new SocketTimeoutException("Discord did not answer in time");
                }
                LockSupport.parkNanos(READ_POLL_NANOS);
            }
        }
    }

    private void send(int opcode, JsonObject body) throws IOException {
        send(opcode, GSON.toJson(body).getBytes(StandardCharsets.UTF_8));
    }

    private void send(int opcode, byte[] body) throws IOException {
        ByteBuffer frame = ByteBuffer.allocate(8 + body.length).order(ByteOrder.LITTLE_ENDIAN);
        frame.putInt(opcode).putInt(body.length).put(body).flip();
        this.pipe.write(frame, System.nanoTime() + REPLY_TIMEOUT_NANOS);
    }

    private static String errorMessage(JsonObject body) {
        JsonElement data = body.get("data");
        String message = string((data == null || !data.isJsonObject()) ? body : data.getAsJsonObject(), "message");
        return message != null ? message : body.toString();
    }

    private static String string(JsonObject object, String key) {
        JsonElement value = object.get(key);
        if (value == null || !value.isJsonPrimitive()) {
            return null;
        }
        return value.getAsString();
    }

    private static Pipe openWindowsPipe() {
        for (int slot = 0; slot < 10; slot++) {
            try {
                return new WindowsPipe(new RandomAccessFile("\\\\.\\pipe\\discord-ipc-" + slot, "rw"));
            } catch (FileNotFoundException e) {
            }
        }
        return null;
    }

    private static Pipe openUnixSocket() {
        for (Path folder : unixFolders()) {
            for (int slot = 0; slot < 10; slot++) {
                Path socket = folder.resolve("discord-ipc-" + slot);
                if (Files.exists(socket, new LinkOption[0])) {
                    try {
                        SocketChannel channel = SocketChannel.open(StandardProtocolFamily.UNIX);
                        try {
                            channel.connect(UnixDomainSocketAddress.of(socket));
                            channel.configureBlocking(false);
                            return new UnixPipe(channel);
                        } catch (IOException failure) {
                            channel.close();
                            throw failure;
                        }
                    } catch (IOException e) {
                    }
                }
            }
        }
        return null;
    }

    private static Set<Path> unixFolders() {
        Set<Path> roots = new LinkedHashSet<>();
        for (String variable : UNIX_TEMP_VARIABLES) {
            String value = System.getenv(variable);
            if (value != null && !value.isBlank()) {
                try {
                    roots.add(Path.of(value, new String[0]));
                } catch (InvalidPathException e) {
                }
            }
        }
        roots.add(Path.of("/tmp", new String[0]));
        Set<Path> folders = new LinkedHashSet<>();
        for (Path root : roots) {
            folders.add(root);
            addSubfolders(folders, root.resolve("app"), "*");
            addSubfolders(folders, root, "snap.*");
        }
        return folders;
    }

    private static void addSubfolders(Set<Path> folders, Path parent, String glob) {
        if (Files.isDirectory(parent, new LinkOption[0])) {
            try {
                DirectoryStream<Path> children = Files.newDirectoryStream(parent, glob);
                try {
                    for (Path child : children) {
                        if (Files.isDirectory(child, new LinkOption[0])) {
                            folders.add(child);
                        }
                    }
                    if (children != null) {
                        children.close();
                    }
                } catch (Throwable th) {
                    if (children != null) {
                        try {
                            children.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (IOException | SecurityException e) {
            }
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/discord/DiscordIpc$RefusedException.class */
    @Environment(EnvType.CLIENT)
    public static final class RefusedException extends Exception {
        RefusedException(String message) {
            super(message);
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/discord/DiscordIpc$Frame.class */
    @Environment(EnvType.CLIENT)
    private static final class Frame extends Record {
        private final int opcode;
        private final byte[] body;

        private Frame(int opcode, byte[] body) {
            this.opcode = opcode;
            this.body = body;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Frame.class), Frame.class, "opcode;body", "FIELD:Lnet/anubis/client/discord/DiscordIpc$Frame;->opcode:I", "FIELD:Lnet/anubis/client/discord/DiscordIpc$Frame;->body:[B").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Frame.class), Frame.class, "opcode;body", "FIELD:Lnet/anubis/client/discord/DiscordIpc$Frame;->opcode:I", "FIELD:Lnet/anubis/client/discord/DiscordIpc$Frame;->body:[B").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Frame.class, Object.class), Frame.class, "opcode;body", "FIELD:Lnet/anubis/client/discord/DiscordIpc$Frame;->opcode:I", "FIELD:Lnet/anubis/client/discord/DiscordIpc$Frame;->body:[B").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int opcode() {
            return this.opcode;
        }

        public byte[] body() {
            return this.body;
        }

        JsonObject json() throws IOException {
            try {
                JsonElement parsed = JsonParser.parseString(new String(this.body, StandardCharsets.UTF_8));
                if (parsed.isJsonObject()) {
                    return parsed.getAsJsonObject();
                }
            } catch (JsonParseException e) {
            }
            throw new IOException("Discord sent a frame that is not a JSON object");
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/discord/DiscordIpc$WindowsPipe.class */
    @Environment(EnvType.CLIENT)
    private static final class WindowsPipe extends Record implements Pipe {
        private final RandomAccessFile file;

        private WindowsPipe(RandomAccessFile file) {
            this.file = file;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, WindowsPipe.class), WindowsPipe.class, "file", "FIELD:Lnet/anubis/client/discord/DiscordIpc$WindowsPipe;->file:Ljava/io/RandomAccessFile;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, WindowsPipe.class), WindowsPipe.class, "file", "FIELD:Lnet/anubis/client/discord/DiscordIpc$WindowsPipe;->file:Ljava/io/RandomAccessFile;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, WindowsPipe.class, Object.class), WindowsPipe.class, "file", "FIELD:Lnet/anubis/client/discord/DiscordIpc$WindowsPipe;->file:Ljava/io/RandomAccessFile;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public RandomAccessFile file() {
            return this.file;
        }

        @Override // net.anubis.client.discord.DiscordIpc.Pipe
        public int readWaiting(ByteBuffer into) throws IOException {
            long waiting = this.file.length();
            if (waiting <= 0) {
                return 0;
            }
            int read = this.file.read(into.array(), into.arrayOffset() + into.position(), (int) Math.min(waiting, into.remaining()));
            if (read < 0) {
                throw new EOFException("Discord hung up");
            }
            into.position(into.position() + read);
            return read;
        }

        @Override // net.anubis.client.discord.DiscordIpc.Pipe
        public void write(ByteBuffer from, long deadline) throws IOException {
            this.file.write(from.array(), from.arrayOffset() + from.position(), from.remaining());
            from.position(from.limit());
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.file.close();
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/discord/DiscordIpc$UnixPipe.class */
    @Environment(EnvType.CLIENT)
    private static final class UnixPipe extends Record implements Pipe {
        private final SocketChannel channel;

        private UnixPipe(SocketChannel channel) {
            this.channel = channel;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, UnixPipe.class), UnixPipe.class, "channel", "FIELD:Lnet/anubis/client/discord/DiscordIpc$UnixPipe;->channel:Ljava/nio/channels/SocketChannel;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, UnixPipe.class), UnixPipe.class, "channel", "FIELD:Lnet/anubis/client/discord/DiscordIpc$UnixPipe;->channel:Ljava/nio/channels/SocketChannel;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, UnixPipe.class, Object.class), UnixPipe.class, "channel", "FIELD:Lnet/anubis/client/discord/DiscordIpc$UnixPipe;->channel:Ljava/nio/channels/SocketChannel;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public SocketChannel channel() {
            return this.channel;
        }

        @Override // net.anubis.client.discord.DiscordIpc.Pipe
        public int readWaiting(ByteBuffer into) throws IOException {
            int read = this.channel.read(into);
            if (read < 0) {
                throw new EOFException("Discord hung up");
            }
            return read;
        }

        @Override // net.anubis.client.discord.DiscordIpc.Pipe
        public void write(ByteBuffer from, long deadline) throws IOException {
            while (from.hasRemaining()) {
                if (this.channel.write(from) <= 0) {
                    if (System.nanoTime() - deadline >= 0) {
                        throw new SocketTimeoutException("Discord stopped reading");
                    }
                    LockSupport.parkNanos(DiscordIpc.READ_POLL_NANOS);
                }
            }
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.channel.close();
        }
    }
}
