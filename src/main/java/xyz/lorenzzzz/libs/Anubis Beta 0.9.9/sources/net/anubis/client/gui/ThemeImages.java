package net.anubis.client.gui;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import net.anubis.client.config.HudTheme;
import net.anubis.client.config.Surface;
import net.anubis.client.config.ThemeStore;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.class_156;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.tinyfd.TinyFileDialogs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/ThemeImages.class */
@Environment(EnvType.CLIENT)
public final class ThemeImages {
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private static final ThemeImages INSTANCE = new ThemeImages();
    private static final int MAX_SIZE = 8192;
    private static final long RESCAN_MS = 2000;
    private static final int SWEEP_TICKS = 20;
    private final Map<String, Texture> textures = new HashMap();
    private final Map<String, GifAnimation> animations = new HashMap();
    private final Map<String, GifAnimation> posters = new HashMap();
    private final Set<String> failed = new HashSet();
    private final List<Runnable> pendingFrees = new ArrayList();
    private List<String> files = List.of();
    private long lastScan;
    private boolean listingChanged;
    private int ticks;
    private volatile Path pendingImport;
    private volatile boolean dialogOpen;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/ThemeImages$Texture.class */
    @Environment(EnvType.CLIENT)
    public static final class Texture extends Record {
        private final int id;
        private final int width;
        private final int height;

        public Texture(int id, int width, int height) {
            this.id = id;
            this.width = width;
            this.height = height;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Texture.class), Texture.class, "id;width;height", "FIELD:Lnet/anubis/client/gui/ThemeImages$Texture;->id:I", "FIELD:Lnet/anubis/client/gui/ThemeImages$Texture;->width:I", "FIELD:Lnet/anubis/client/gui/ThemeImages$Texture;->height:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Texture.class), Texture.class, "id;width;height", "FIELD:Lnet/anubis/client/gui/ThemeImages$Texture;->id:I", "FIELD:Lnet/anubis/client/gui/ThemeImages$Texture;->width:I", "FIELD:Lnet/anubis/client/gui/ThemeImages$Texture;->height:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Texture.class, Object.class), Texture.class, "id;width;height", "FIELD:Lnet/anubis/client/gui/ThemeImages$Texture;->id:I", "FIELD:Lnet/anubis/client/gui/ThemeImages$Texture;->width:I", "FIELD:Lnet/anubis/client/gui/ThemeImages$Texture;->height:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int id() {
            return this.id;
        }

        public int width() {
            return this.width;
        }

        public int height() {
            return this.height;
        }
    }

    private ThemeImages() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!this.pendingFrees.isEmpty()) {
                this.pendingFrees.forEach((v0) -> {
                    v0.run();
                });
                this.pendingFrees.clear();
            }
            int i = this.ticks + 1;
            this.ticks = i;
            if (i % 20 == 0) {
                sweep();
            }
        });
    }

    public static ThemeImages get() {
        return INSTANCE;
    }

    public static boolean isImage(Path path) {
        String name = path.getFileName().toString().toLowerCase(Locale.ROOT);
        return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") || isGif(name);
    }

    public static boolean isGif(String fileName) {
        return fileName.toLowerCase(Locale.ROOT).endsWith(".gif");
    }

    public List<String> files() {
        long now = System.currentTimeMillis();
        if (now - this.lastScan > RESCAN_MS) {
            this.lastScan = now;
            List<String> scanned = scan();
            if (!scanned.equals(this.files)) {
                this.files = scanned;
                this.listingChanged = true;
            }
        }
        return this.files;
    }

    public Texture texture(String fileName) {
        if (fileName == null || fileName.isBlank() || this.failed.contains(fileName)) {
            return null;
        }
        if (isGif(fileName)) {
            return frame(this.animations, fileName, false);
        }
        Texture texture = this.textures.get(fileName);
        if (texture == null) {
            texture = upload(ThemeStore.backgroundsDir().resolve(fileName));
            if (texture == null) {
                this.failed.add(fileName);
            } else {
                this.textures.put(fileName, texture);
            }
        }
        return texture;
    }

    public Texture preview(String fileName) {
        if (fileName == null || this.failed.contains(fileName) || !isGif(fileName)) {
            return texture(fileName);
        }
        GifAnimation playing = this.animations.get(fileName);
        Texture frame = playing != null ? playing.current() : null;
        return frame != null ? frame : frame(this.posters, fileName, true);
    }

    public boolean isBroken(String fileName) {
        return this.failed.contains(fileName);
    }

    public String importFile(Path source) {
        if (source == null || !Files.isRegularFile(source, new LinkOption[0]) || !isImage(source)) {
            return null;
        }
        try {
            Path dir = ThemeStore.backgroundsDir();
            Files.createDirectories(dir, new FileAttribute[0]);
            if (dir.toAbsolutePath().normalize().equals(source.toAbsolutePath().normalize().getParent())) {
                return source.getFileName().toString();
            }
            String name = uniqueName(dir, source.getFileName().toString());
            Files.copy(source, dir.resolve(name), new CopyOption[0]);
            this.lastScan = 0L;
            return name;
        } catch (IOException | RuntimeException e) {
            LOGGER.warn("[Anubis] Failed to import header image {}", source, e);
            return null;
        }
    }

    public boolean delete(String fileName) {
        if (fileName == null || !this.files.contains(fileName)) {
            return false;
        }
        try {
            Files.deleteIfExists(ThemeStore.backgroundsDir().resolve(fileName));
            Texture texture = this.textures.remove(fileName);
            if (texture != null) {
                this.pendingFrees.add(() -> {
                    GL11.glDeleteTextures(texture.id());
                });
            }
            GifAnimation playing = this.animations.remove(fileName);
            if (playing != null) {
                List<Runnable> list = this.pendingFrees;
                Objects.requireNonNull(playing);
                list.add(playing::free);
            }
            GifAnimation poster = this.posters.remove(fileName);
            if (poster != null) {
                List<Runnable> list2 = this.pendingFrees;
                Objects.requireNonNull(poster);
                list2.add(poster::free);
            }
            this.failed.remove(fileName);
            List<Surface> surfaces = new ArrayList<>(ThemeStore.get().surfaces());
            surfaces.addAll(HudTheme.get().surfaces());
            for (Surface surface : surfaces) {
                if (fileName.equals(surface.image.get())) {
                    surface.image.set("");
                    if (surface.isImage()) {
                        surface.background.set(Surface.BG_NONE);
                    }
                }
            }
            this.lastScan = 0L;
            return true;
        } catch (IOException | RuntimeException e) {
            LOGGER.warn("[Anubis] Failed to delete header image {}", fileName, e);
            return false;
        }
    }

    public void browseAsync() {
        if (this.dialogOpen) {
            return;
        }
        this.dialogOpen = true;
        Thread thread = new Thread(() -> {
            try {
                try {
                    MemoryStack stack = MemoryStack.stackPush();
                    try {
                        PointerBuffer filters = stack.mallocPointer(4);
                        filters.put(stack.UTF8("*.png")).put(stack.UTF8("*.jpg")).put(stack.UTF8("*.jpeg")).put(stack.UTF8("*.gif")).flip();
                        String result = TinyFileDialogs.tinyfd_openFileDialog("Choose a header image", (CharSequence) null, filters, "Images (png, jpg, gif)", false);
                        if (result != null) {
                            this.pendingImport = Path.of(result, new String[0]);
                        }
                        if (stack != null) {
                            stack.close();
                        }
                        this.dialogOpen = false;
                    } catch (Throwable th) {
                        if (stack != null) {
                            try {
                                stack.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        }
                        throw th;
                    }
                } catch (Throwable t) {
                    LOGGER.warn("[Anubis] File dialog failed", t);
                    this.dialogOpen = false;
                }
            } catch (Throwable th3) {
                this.dialogOpen = false;
                throw th3;
            }
        }, "Anubis theme file dialog");
        thread.setDaemon(true);
        thread.start();
    }

    public Path takePendingImport() {
        Path path = this.pendingImport;
        this.pendingImport = null;
        return path;
    }

    public void openFolder() {
        try {
            Path dir = ThemeStore.backgroundsDir();
            Files.createDirectories(dir, new FileAttribute[0]);
            class_156.method_668().method_60932(dir);
        } catch (IOException | RuntimeException e) {
            LOGGER.warn("[Anubis] Failed to open backgrounds folder", e);
        }
    }

    private Texture frame(Map<String, GifAnimation> cache, String fileName, boolean poster) {
        GifAnimation animation = cache.computeIfAbsent(fileName, name -> {
            return GifAnimation.load(ThemeStore.backgroundsDir().resolve(name), poster);
        });
        Texture texture = animation.current();
        if (texture == null && animation.failed()) {
            cache.remove(fileName);
            this.failed.add(fileName);
        }
        return texture;
    }

    private void sweep() {
        Set<String> shown = new HashSet<>();
        for (Surface surface : ThemeStore.get().surfaces()) {
            if (surface.isImage()) {
                shown.add(surface.image.get());
            }
        }
        for (Surface surface2 : HudTheme.get().surfaces()) {
            if (surface2.isImage()) {
                shown.add(surface2.image.get());
            }
        }
        freeAllBut(this.animations, shown);
        if (this.listingChanged) {
            this.listingChanged = false;
            Set<String> present = new HashSet<>(this.files);
            freeAllBut(this.animations, present);
            freeAllBut(this.posters, present);
            this.textures.entrySet().removeIf(entry -> {
                if (present.contains(entry.getKey())) {
                    return false;
                }
                GL11.glDeleteTextures(((Texture) entry.getValue()).id());
                return true;
            });
            this.failed.retainAll(present);
        }
    }

    private static void freeAllBut(Map<String, GifAnimation> cache, Set<String> keep) {
        cache.entrySet().removeIf(entry -> {
            if (keep.contains(entry.getKey())) {
                return false;
            }
            ((GifAnimation) entry.getValue()).free();
            return true;
        });
    }

    private static List<String> scan() {
        Path dir = ThemeStore.backgroundsDir();
        if (!Files.isDirectory(dir, new LinkOption[0])) {
            return List.of();
        }
        try {
            Stream<Path> stream = Files.list(dir);
            try {
                List<String> list = stream.filter(x$0 -> {
                    return Files.isRegularFile(x$0, new LinkOption[0]);
                }).filter(ThemeImages::isImage).map(path -> {
                    return path.getFileName().toString();
                }).sorted(String.CASE_INSENSITIVE_ORDER).toList();
                if (stream != null) {
                    stream.close();
                }
                return list;
            } catch (Throwable th) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (IOException e) {
            return List.of();
        }
    }

    private static String uniqueName(Path dir, String original) {
        String name = original.replaceAll("[^A-Za-z0-9._-]", "_");
        int dot = name.lastIndexOf(46);
        String base = dot > 0 ? name.substring(0, dot) : name;
        String ext = dot > 0 ? name.substring(dot) : "";
        String candidate = name;
        int i = 2;
        while (Files.exists(dir.resolve(candidate), new LinkOption[0])) {
            candidate = base + "-" + i + ext;
            i++;
        }
        return candidate;
    }

    private static Texture upload(Path path) {
        if (!Files.isRegularFile(path, new LinkOption[0])) {
            return null;
        }
        try {
            return decode(Files.readAllBytes(path), path.getFileName().toString(), false);
        } catch (IOException | RuntimeException e) {
            LOGGER.warn("[Anubis] Failed to load {}", path.getFileName(), e);
            return null;
        }
    }

    static Texture decode(byte[] bytes, String name, boolean pixelated) {
        try {
            MemoryStack stack = MemoryStack.stackPush();
            try {
                ByteBuffer file = MemoryUtil.memAlloc(bytes.length);
                file.put(bytes).flip();
                IntBuffer w = stack.mallocInt(1);
                IntBuffer h = stack.mallocInt(1);
                IntBuffer channels = stack.mallocInt(1);
                ByteBuffer pixels = STBImage.stbi_load_from_memory(file, w, h, channels, 4);
                if (pixels == null) {
                    LOGGER.warn("[Anubis] Can't decode {}: {}", name, STBImage.stbi_failure_reason());
                    if (stack != null) {
                        stack.close();
                    }
                    if (pixels != null) {
                        STBImage.stbi_image_free(pixels);
                    }
                    if (file != null) {
                        MemoryUtil.memFree(file);
                    }
                    return null;
                }
                int width = w.get(0);
                int height = h.get(0);
                if (width > 8192 || height > 8192) {
                    LOGGER.warn("[Anubis] {} is larger than {}px", name, 8192);
                    if (stack != null) {
                        stack.close();
                    }
                    if (pixels != null) {
                        STBImage.stbi_image_free(pixels);
                    }
                    if (file != null) {
                        MemoryUtil.memFree(file);
                    }
                    return null;
                }
                Texture texture = new Texture(createTexture(pixels, width, height, pixelated), width, height);
                if (stack != null) {
                    stack.close();
                }
                if (pixels != null) {
                    STBImage.stbi_image_free(pixels);
                }
                if (file != null) {
                    MemoryUtil.memFree(file);
                }
                return texture;
            } catch (Throwable th) {
                if (stack != null) {
                    try {
                        stack.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (Throwable th3) {
            if (0 != 0) {
                STBImage.stbi_image_free((ByteBuffer) null);
            }
            if (0 != 0) {
                MemoryUtil.memFree((Buffer) null);
            }
            throw th3;
        }
    }

    public static Texture fromPixels(ByteBuffer rgba, int width, int height, boolean pixelated) {
        return new Texture(createTexture(rgba, width, height, pixelated), width, height);
    }

    private static int createTexture(ByteBuffer pixels, int width, int height, boolean pixelated) {
        int prevTexture = GL11.glGetInteger(32873);
        int prevUnpackBuffer = GL11.glGetInteger(35055);
        int prevAlignment = GL11.glGetInteger(3317);
        int prevRowLength = GL11.glGetInteger(3314);
        int prevSkipPixels = GL11.glGetInteger(3316);
        int prevSkipRows = GL11.glGetInteger(3315);
        try {
            GL15.glBindBuffer(35052, 0);
            GL11.glPixelStorei(3317, 4);
            GL11.glPixelStorei(3314, 0);
            GL11.glPixelStorei(3316, 0);
            GL11.glPixelStorei(3315, 0);
            int id = GL11.glGenTextures();
            GL11.glBindTexture(3553, id);
            GL11.glTexParameteri(3553, 10241, pixelated ? 9728 : 9987);
            GL11.glTexParameteri(3553, 10240, pixelated ? 9728 : 9729);
            GL11.glTexParameteri(3553, 10242, 33071);
            GL11.glTexParameteri(3553, 10243, 33071);
            GL11.glTexImage2D(3553, 0, 32856, width, height, 0, 6408, 5121, pixels);
            if (!pixelated) {
                GL30.glGenerateMipmap(3553);
            }
            return id;
        } finally {
            GL11.glBindTexture(3553, prevTexture);
            GL15.glBindBuffer(35052, prevUnpackBuffer);
            GL11.glPixelStorei(3317, prevAlignment);
            GL11.glPixelStorei(3314, prevRowLength);
            GL11.glPixelStorei(3316, prevSkipPixels);
            GL11.glPixelStorei(3315, prevSkipRows);
        }
    }
}
