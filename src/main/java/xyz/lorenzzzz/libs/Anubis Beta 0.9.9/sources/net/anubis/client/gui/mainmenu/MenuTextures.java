package net.anubis.client.gui.mainmenu;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;
import net.anubis.client.gui.ThemeImages;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/MenuTextures.class */
@Environment(EnvType.CLIENT)
final class MenuTextures {
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private static final Map<String, ThemeImages.Texture> TEXTURES = new HashMap();

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/MenuTextures$Pixels.class */
    @Environment(EnvType.CLIENT)
    private static final class Pixels extends Record {
        private final int width;
        private final int height;
        private final int[] argb;

        private Pixels(int width, int height, int[] argb) {
            this.width = width;
            this.height = height;
            this.argb = argb;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Pixels.class), Pixels.class, "width;height;argb", "FIELD:Lnet/anubis/client/gui/mainmenu/MenuTextures$Pixels;->width:I", "FIELD:Lnet/anubis/client/gui/mainmenu/MenuTextures$Pixels;->height:I", "FIELD:Lnet/anubis/client/gui/mainmenu/MenuTextures$Pixels;->argb:[I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Pixels.class), Pixels.class, "width;height;argb", "FIELD:Lnet/anubis/client/gui/mainmenu/MenuTextures$Pixels;->width:I", "FIELD:Lnet/anubis/client/gui/mainmenu/MenuTextures$Pixels;->height:I", "FIELD:Lnet/anubis/client/gui/mainmenu/MenuTextures$Pixels;->argb:[I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Pixels.class, Object.class), Pixels.class, "width;height;argb", "FIELD:Lnet/anubis/client/gui/mainmenu/MenuTextures$Pixels;->width:I", "FIELD:Lnet/anubis/client/gui/mainmenu/MenuTextures$Pixels;->height:I", "FIELD:Lnet/anubis/client/gui/mainmenu/MenuTextures$Pixels;->argb:[I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int width() {
            return this.width;
        }

        public int height() {
            return this.height;
        }

        public int[] argb() {
            return this.argb;
        }
    }

    private MenuTextures() {
    }

    static ThemeImages.Texture original(String resource) {
        return cached(resource, resource, UnaryOperator.identity());
    }

    static ThemeImages.Texture mask(String resource) {
        return cached(resource + "#mask", resource, p -> {
            int[] out = new int[p.argb().length];
            for (int i = 0; i < out.length; i++) {
                out[i] = (p.argb()[i] & (-16777216)) | 16777215;
            }
            return new Pixels(p.width(), p.height(), out);
        });
    }

    static ThemeImages.Texture blurred(String resource, int radius) {
        return cached(resource + "#blur" + radius, resource, p -> {
            return blur(p, radius);
        });
    }

    private static ThemeImages.Texture cached(String key, String resource, UnaryOperator<Pixels> variant) {
        if (TEXTURES.containsKey(key)) {
            return TEXTURES.get(key);
        }
        ThemeImages.Texture texture = null;
        try {
            InputStream in = MenuTextures.class.getResourceAsStream(resource);
            try {
                if (in == null) {
                    LOGGER.warn("[Anubis] Missing menu image {}", resource);
                } else {
                    Pixels p = decode(in.readAllBytes(), resource);
                    if (p != null) {
                        Pixels p2 = (Pixels) variant.apply(p);
                        texture = upload(p2.argb(), p2.width(), p2.height());
                    }
                }
                if (in != null) {
                    in.close();
                }
            } catch (Throwable th) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (Exception e) {
            LOGGER.warn("[Anubis] Failed to load menu image {}", key, e);
        }
        TEXTURES.put(key, texture);
        return texture;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Pixels blur(Pixels p, int radius) {
        int pad = radius * 2;
        int w = p.width() + (pad * 2);
        int h = p.height() + (pad * 2);
        float[] alpha = new float[w * h];
        for (int y = 0; y < p.height(); y++) {
            for (int x = 0; x < p.width(); x++) {
                alpha[((y + pad) * w) + x + pad] = (p.argb()[(y * p.width()) + x] >>> 24) / 255.0f;
            }
        }
        for (int pass = 0; pass < 3; pass++) {
            alpha = box(box(alpha, w, h, radius, true), w, h, radius, false);
        }
        int[] out = new int[w * h];
        for (int i = 0; i < out.length; i++) {
            out[i] = (Math.round(Math.min(1.0f, alpha[i]) * 255.0f) << 24) | 16777215;
        }
        return new Pixels(w, h, out);
    }

    private static float[] box(float[] in, int w, int h, int radius, boolean across) {
        float[] out = new float[in.length];
        float norm = 1.0f / ((radius * 2) + 1);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                float sum = 0.0f;
                for (int k = -radius; k <= radius; k++) {
                    int sx = across ? x + k : x;
                    int sy = across ? y : y + k;
                    if (sx >= 0 && sy >= 0 && sx < w && sy < h) {
                        sum += in[(sy * w) + sx];
                    }
                }
                out[(y * w) + x] = sum * norm;
            }
        }
        return out;
    }

    private static Pixels decode(byte[] bytes, String name) {
        try {
            MemoryStack stack = MemoryStack.stackPush();
            try {
                ByteBuffer file = MemoryUtil.memAlloc(bytes.length);
                file.put(bytes).flip();
                IntBuffer w = stack.mallocInt(1);
                IntBuffer h = stack.mallocInt(1);
                IntBuffer channels = stack.mallocInt(1);
                ByteBuffer rgba = STBImage.stbi_load_from_memory(file, w, h, channels, 4);
                if (rgba == null) {
                    LOGGER.warn("[Anubis] Can't decode {}: {}", name, STBImage.stbi_failure_reason());
                    if (stack != null) {
                        stack.close();
                    }
                    if (rgba != null) {
                        STBImage.stbi_image_free(rgba);
                    }
                    if (file != null) {
                        MemoryUtil.memFree(file);
                    }
                    return null;
                }
                int width = w.get(0);
                int height = h.get(0);
                int[] argb = new int[width * height];
                for (int i = 0; i < argb.length; i++) {
                    int r = rgba.get(i * 4) & 255;
                    int g = rgba.get((i * 4) + 1) & 255;
                    int b = rgba.get((i * 4) + 2) & 255;
                    int a = rgba.get((i * 4) + 3) & 255;
                    argb[i] = (a << 24) | (r << 16) | (g << 8) | b;
                }
                Pixels pixels = new Pixels(width, height, argb);
                if (stack != null) {
                    stack.close();
                }
                if (rgba != null) {
                    STBImage.stbi_image_free(rgba);
                }
                if (file != null) {
                    MemoryUtil.memFree(file);
                }
                return pixels;
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

    private static ThemeImages.Texture upload(int[] argb, int width, int height) {
        ByteBuffer buffer = MemoryUtil.memAlloc(argb.length * 4);
        try {
            for (int c : argb) {
                buffer.put((byte) (c >> 16)).put((byte) (c >> 8)).put((byte) c).put((byte) (c >>> 24));
            }
            buffer.flip();
            return ThemeImages.fromPixels(buffer, width, height, false);
        } finally {
            MemoryUtil.memFree(buffer);
        }
    }
}
