package net.anubis.client.util.spotify;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/spotify/AlbumArt.class */
@Environment(EnvType.CLIENT)
public final class AlbumArt {
    private static final int MAX_SOURCE_SIDE = 4096;
    private final int size;
    private final byte[] rgba;

    private AlbumArt(int size, byte[] rgba) {
        this.size = size;
        this.rgba = rgba;
    }

    /* JADX WARN: Code duplicated, block: B:67:0x0125  */
    public static AlbumArt decode(byte[] encoded, int maxSide) {
        if (encoded == null || encoded.length == 0 || maxSide <= 0) {
            return null;
        }
        ByteBuffer file = MemoryUtil.memAlloc(encoded.length);
        ByteBuffer pixels = null;
        try {
            MemoryStack stack = MemoryStack.stackPush();
            try {
                file.put(encoded).flip();
                IntBuffer w = stack.mallocInt(1);
                IntBuffer h = stack.mallocInt(1);
                IntBuffer channels = stack.mallocInt(1);
                if (!STBImage.stbi_info_from_memory(file, w, h, channels)) {
                    if (stack != null) {
                        stack.close();
                    }
                    if (0 != 0) {
                        STBImage.stbi_image_free((ByteBuffer) null);
                    }
                    MemoryUtil.memFree(file);
                    return null;
                }
                if (w.get(0) <= 0 || h.get(0) <= 0 || w.get(0) > 4096 || h.get(0) > 4096) {
                    if (stack != null) {
                        stack.close();
                    }
                    if (0 != 0) {
                        STBImage.stbi_image_free((ByteBuffer) null);
                    }
                    MemoryUtil.memFree(file);
                    return null;
                }
                pixels = STBImage.stbi_load_from_memory(file, w, h, channels, 4);
                if (pixels == null) {
                    if (stack != null) {
                        stack.close();
                    }
                    if (pixels != null) {
                        STBImage.stbi_image_free(pixels);
                    }
                    MemoryUtil.memFree(file);
                    return null;
                }
                AlbumArt albumArtCrop = crop(pixels, w.get(0), h.get(0), maxSide);
                if (stack != null) {
                    stack.close();
                }
                if (pixels != null) {
                    STBImage.stbi_image_free(pixels);
                }
                MemoryUtil.memFree(file);
                return albumArtCrop;
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
            if (pixels != null) {
                STBImage.stbi_image_free(pixels);
            }
            MemoryUtil.memFree(file);
            throw th3;
        }
        if (pixels != null) {
            STBImage.stbi_image_free(pixels);
        }
        MemoryUtil.memFree(file);
        throw th3;
    }

    static AlbumArt crop(ByteBuffer pixels, int width, int height, int maxSide) {
        int side = Math.min(width, height);
        int left = (width - side) / 2;
        int top = (height - side) / 2;
        int size = Math.min(side, maxSide);
        byte[] out = new byte[size * size * 4];
        int o = 0;
        for (int oy = 0; oy < size; oy++) {
            int y0 = top + ((int) ((((long) oy) * ((long) side)) / ((long) size)));
            int y1 = Math.max(y0 + 1, top + ((int) ((((long) (oy + 1)) * ((long) side)) / ((long) size))));
            for (int ox = 0; ox < size; ox++) {
                int x0 = left + ((int) ((((long) ox) * ((long) side)) / ((long) size)));
                int x1 = Math.max(x0 + 1, left + ((int) ((((long) (ox + 1)) * ((long) side)) / ((long) size))));
                long r = 0;
                long g = 0;
                long b = 0;
                long a = 0;
                for (int y = y0; y < y1; y++) {
                    int row = ((y * width) + x0) * 4;
                    int x = x0;
                    while (x < x1) {
                        r += (long) (pixels.get(row) & 255);
                        g += (long) (pixels.get(row + 1) & 255);
                        b += (long) (pixels.get(row + 2) & 255);
                        a += (long) (pixels.get(row + 3) & 255);
                        x++;
                        row += 4;
                    }
                }
                long count = ((long) (y1 - y0)) * ((long) (x1 - x0));
                int i = o;
                int o2 = o + 1;
                out[i] = (byte) ((r + (count / 2)) / count);
                int o3 = o2 + 1;
                out[o2] = (byte) ((g + (count / 2)) / count);
                int o4 = o3 + 1;
                out[o3] = (byte) ((b + (count / 2)) / count);
                o = o4 + 1;
                out[o4] = (byte) ((a + (count / 2)) / count);
            }
        }
        return new AlbumArt(size, out);
    }

    public int size() {
        return this.size;
    }

    public int byteSize() {
        return this.rgba.length;
    }

    public void copyTo(ByteBuffer target) {
        target.put(this.rgba);
    }
}
