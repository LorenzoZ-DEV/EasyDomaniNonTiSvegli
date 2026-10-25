package net.anubis.client.util.spotify;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/spotify/LyricsDiskCache.class */
@Environment(EnvType.CLIENT)
final class LyricsDiskCache {
    private static final int FORMAT = 1;
    private static final String SUFFIX = ".json";
    private static final Pattern KEY = Pattern.compile("[0-9a-f]{16,64}");
    private static final long MAX_FILE_BYTES = 524288;
    private final Path dir;
    private final int maxFiles;
    private final long maxBytes;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/spotify/LyricsDiskCache$Entry.class */
    @Environment(EnvType.CLIENT)
    static final class Entry extends Record {
        private final LrclibTrack track;
        private final long fetchedAt;

        Entry(LrclibTrack track, long fetchedAt) {
            this.track = track;
            this.fetchedAt = fetchedAt;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Entry.class), Entry.class, "track;fetchedAt", "FIELD:Lnet/anubis/client/util/spotify/LyricsDiskCache$Entry;->track:Lnet/anubis/client/util/spotify/LrclibTrack;", "FIELD:Lnet/anubis/client/util/spotify/LyricsDiskCache$Entry;->fetchedAt:J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Entry.class), Entry.class, "track;fetchedAt", "FIELD:Lnet/anubis/client/util/spotify/LyricsDiskCache$Entry;->track:Lnet/anubis/client/util/spotify/LrclibTrack;", "FIELD:Lnet/anubis/client/util/spotify/LyricsDiskCache$Entry;->fetchedAt:J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Entry.class, Object.class), Entry.class, "track;fetchedAt", "FIELD:Lnet/anubis/client/util/spotify/LyricsDiskCache$Entry;->track:Lnet/anubis/client/util/spotify/LrclibTrack;", "FIELD:Lnet/anubis/client/util/spotify/LyricsDiskCache$Entry;->fetchedAt:J").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public LrclibTrack track() {
            return this.track;
        }

        public long fetchedAt() {
            return this.fetchedAt;
        }

        boolean found() {
            return this.track != null;
        }
    }

    LyricsDiskCache(Path dir, int maxFiles, long maxBytes) {
        this.dir = dir;
        this.maxFiles = maxFiles;
        this.maxBytes = maxBytes;
    }

    Entry read(String key) {
        Path file = file(key);
        if (file == null) {
            return null;
        }
        try {
            if (Files.size(file) > MAX_FILE_BYTES) {
                return null;
            }
            JsonElement root = JsonParser.parseString(Files.readString(file, StandardCharsets.UTF_8));
            if (!root.isJsonObject()) {
                return null;
            }
            JsonObject object = root.getAsJsonObject();
            if (intValue(object, "format") != 1) {
                return null;
            }
            long fetchedAt = longValue(object, "fetchedAt");
            if (fetchedAt <= 0) {
                return null;
            }
            LrclibTrack track = object.has("track") ? LrclibTrack.fromJson(object.get("track")) : null;
            touch(file);
            return new Entry(track, fetchedAt);
        } catch (IOException | JsonParseException | IllegalStateException | UnsupportedOperationException e) {
            return null;
        }
    }

    void write(String key, LrclibTrack track, long fetchedAt) {
        Path file = file(key);
        if (file == null) {
            return;
        }
        JsonObject object = new JsonObject();
        object.addProperty("format", 1);
        object.addProperty("fetchedAt", Long.valueOf(fetchedAt));
        if (track != null) {
            object.add("track", track.toJson());
        }
        Path tmp = file.resolveSibling(String.valueOf(file.getFileName()) + ".tmp");
        try {
            Files.createDirectories(this.dir, new FileAttribute[0]);
            Files.writeString(tmp, object.toString(), StandardCharsets.UTF_8, new OpenOption[0]);
            try {
                Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException e) {
                Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e2) {
            delete(tmp);
        }
    }

    void prune() {
        if (Files.isDirectory(this.dir, new LinkOption[0])) {
            List<Stored> stored = new ArrayList<>();
            long total = 0;
            try {
                DirectoryStream<Path> files = Files.newDirectoryStream(this.dir);
                try {
                    for (Path file : files) {
                        String name = file.getFileName().toString();
                        if (name.endsWith(".json.tmp")) {
                            delete(file);
                        } else if (name.endsWith(SUFFIX) && KEY.matcher(name.substring(0, name.length() - SUFFIX.length())).matches()) {
                            BasicFileAttributes attributes = Files.readAttributes(file, (Class<BasicFileAttributes>) BasicFileAttributes.class, new LinkOption[0]);
                            if (attributes.isRegularFile()) {
                                stored.add(new Stored(file, attributes.size(), attributes.lastModifiedTime().toMillis()));
                                total += attributes.size();
                            }
                        }
                    }
                    if (files != null) {
                        files.close();
                    }
                    if (stored.size() > this.maxFiles || total > this.maxBytes) {
                        stored.sort(Comparator.comparingLong((v0) -> {
                            return v0.usedAt();
                        }));
                        int count = stored.size();
                        for (Stored entry : stored) {
                            if (count > this.maxFiles || total > this.maxBytes) {
                                if (delete(entry.file())) {
                                    count--;
                                    total -= entry.size();
                                }
                            } else {
                                return;
                            }
                        }
                    }
                } catch (Throwable th) {
                    if (files != null) {
                        try {
                            files.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (IOException e) {
            }
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/spotify/LyricsDiskCache$Stored.class */
    @Environment(EnvType.CLIENT)
    private static final class Stored extends Record {
        private final Path file;
        private final long size;
        private final long usedAt;

        private Stored(Path file, long size, long usedAt) {
            this.file = file;
            this.size = size;
            this.usedAt = usedAt;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Stored.class), Stored.class, "file;size;usedAt", "FIELD:Lnet/anubis/client/util/spotify/LyricsDiskCache$Stored;->file:Ljava/nio/file/Path;", "FIELD:Lnet/anubis/client/util/spotify/LyricsDiskCache$Stored;->size:J", "FIELD:Lnet/anubis/client/util/spotify/LyricsDiskCache$Stored;->usedAt:J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Stored.class), Stored.class, "file;size;usedAt", "FIELD:Lnet/anubis/client/util/spotify/LyricsDiskCache$Stored;->file:Ljava/nio/file/Path;", "FIELD:Lnet/anubis/client/util/spotify/LyricsDiskCache$Stored;->size:J", "FIELD:Lnet/anubis/client/util/spotify/LyricsDiskCache$Stored;->usedAt:J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Stored.class, Object.class), Stored.class, "file;size;usedAt", "FIELD:Lnet/anubis/client/util/spotify/LyricsDiskCache$Stored;->file:Ljava/nio/file/Path;", "FIELD:Lnet/anubis/client/util/spotify/LyricsDiskCache$Stored;->size:J", "FIELD:Lnet/anubis/client/util/spotify/LyricsDiskCache$Stored;->usedAt:J").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public Path file() {
            return this.file;
        }

        public long size() {
            return this.size;
        }

        public long usedAt() {
            return this.usedAt;
        }
    }

    private Path file(String key) {
        if (key == null || !KEY.matcher(key).matches()) {
            return null;
        }
        return this.dir.resolve(key + ".json");
    }

    private static boolean delete(Path file) {
        try {
            Files.deleteIfExists(file);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static void touch(Path file) {
        try {
            Files.setLastModifiedTime(file, FileTime.fromMillis(System.currentTimeMillis()));
        } catch (IOException e) {
        }
    }

    private static int intValue(JsonObject object, String name) {
        JsonPrimitive jsonPrimitive = object.get(name);
        if (jsonPrimitive instanceof JsonPrimitive) {
            JsonPrimitive primitive = jsonPrimitive;
            if (primitive.isNumber()) {
                return primitive.getAsInt();
            }
        }
        return -1;
    }

    private static long longValue(JsonObject object, String name) {
        JsonPrimitive jsonPrimitive = object.get(name);
        if (jsonPrimitive instanceof JsonPrimitive) {
            JsonPrimitive primitive = jsonPrimitive;
            if (primitive.isNumber()) {
                return primitive.getAsLong();
            }
        }
        return -1L;
    }
}
