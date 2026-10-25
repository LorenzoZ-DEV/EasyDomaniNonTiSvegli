package net.anubis.client.gui;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudEmblem.class */
@Environment(EnvType.CLIENT)
final class HudEmblem {
    private static final String SOURCE = "/assets/anubis/textures/gui/hud_emblem.png";
    private static final float INK_GAMMA = 0.65f;
    private static final float INK_GAIN = 1.35f;
    private static float[] ink;
    private static int inkW;
    private static int inkH;
    private static boolean missing;
    private static ThemeImages.Texture sized;
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private static int sizedHeight = -1;

    private HudEmblem() {
    }

    static ThemeImages.Texture forHeight(int height) {
        if (height < 4 || missing) {
            return null;
        }
        if (height == sizedHeight && sized != null) {
            return sized;
        }
        if (ink == null && !load()) {
            missing = true;
            return null;
        }
        int width = Math.max(1, Math.round((inkW * height) / inkH));
        ByteBuffer rgba = MemoryUtil.memAlloc(width * height * 4);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                try {
                    float a = coverage(x, y, width, height);
                    rgba.put((byte) -1).put((byte) -1).put((byte) -1).put((byte) Math.round(Math.min(1.0f, ((float) Math.pow(a, 0.6499999761581421d)) * INK_GAIN) * 255.0f));
                } catch (Throwable th) {
                    MemoryUtil.memFree(rgba);
                    throw th;
                }
            }
        }
        rgba.flip();
        if (sized != null) {
            GL11.glDeleteTextures(sized.id());
        }
        sized = ThemeImages.fromPixels(rgba, width, height, false);
        sizedHeight = height;
        ThemeImages.Texture texture = sized;
        MemoryUtil.memFree(rgba);
        return texture;
    }

    private static float coverage(int x, int y, int width, int height) {
        float x0 = (x * inkW) / width;
        float x1 = ((x + 1) * inkW) / width;
        float y0 = (y * inkH) / height;
        float y1 = ((y + 1) * inkH) / height;
        float sum = 0.0f;
        float area = 0.0f;
        for (int sy = (int) y0; sy < Math.min(inkH, (int) Math.ceil(y1)); sy++) {
            float wy = Math.min(y1, sy + 1) - Math.max(y0, sy);
            for (int sx = (int) x0; sx < Math.min(inkW, (int) Math.ceil(x1)); sx++) {
                float wx = Math.min(x1, sx + 1) - Math.max(x0, sx);
                float weight = wx * wy;
                sum += ink[(sy * inkW) + sx] * weight;
                area += weight;
            }
        }
        if (area > 0.0f) {
            return sum / area;
        }
        return 0.0f;
    }

    private static boolean load() {
        try {
            InputStream in = HudEmblem.class.getResourceAsStream(SOURCE);
            try {
                if (in == null) {
                    LOGGER.warn("[Anubis] Missing texture {}", SOURCE);
                    if (in != null) {
                        in.close();
                    }
                    return false;
                }
                byte[] bytes = in.readAllBytes();
                if (in != null) {
                    in.close();
                }
                ByteBuffer file = MemoryUtil.memAlloc(bytes.length);
                try {
                    MemoryStack stack = MemoryStack.stackPush();
                    try {
                        file.put(bytes).flip();
                        IntBuffer w = stack.mallocInt(1);
                        IntBuffer h = stack.mallocInt(1);
                        IntBuffer channels = stack.mallocInt(1);
                        ByteBuffer pixels = STBImage.stbi_load_from_memory(file, w, h, channels, 4);
                        if (pixels == null) {
                            LOGGER.warn("[Anubis] Can't decode {}: {}", SOURCE, STBImage.stbi_failure_reason());
                            if (stack != null) {
                                stack.close();
                            }
                            if (pixels != null) {
                                STBImage.stbi_image_free(pixels);
                            }
                            MemoryUtil.memFree(file);
                            return false;
                        }
                        inkW = w.get(0);
                        inkH = h.get(0);
                        ink = new float[inkW * inkH];
                        for (int i = 0; i < ink.length; i++) {
                            ink[i] = (pixels.get((i * 4) + 3) & 255) / 255.0f;
                        }
                        if (stack != null) {
                            stack.close();
                        }
                        if (pixels != null) {
                            STBImage.stbi_image_free(pixels);
                        }
                        MemoryUtil.memFree(file);
                        return true;
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
                    MemoryUtil.memFree(file);
                    throw th3;
                }
            } catch (Throwable th4) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Throwable th5) {
                        th4.addSuppressed(th5);
                    }
                }
                throw th4;
            }
        } catch (IOException e) {
            LOGGER.warn("[Anubis] Failed to read {}", SOURCE, e);
            return false;
        }
    }
}
