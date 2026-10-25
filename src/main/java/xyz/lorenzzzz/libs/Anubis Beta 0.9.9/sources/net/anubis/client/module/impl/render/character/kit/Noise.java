package net.anubis.client.module.impl.render.character.kit;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Noise.class */
@Environment(EnvType.CLIENT)
public final class Noise {
    private static final int[] PERM = new int[512];

    static {
        int[] p = new int[256];
        for (int i = 0; i < 256; i++) {
            p[i] = i;
        }
        long seed = 25214903917L;
        for (int i2 = 255; i2 > 0; i2--) {
            seed = (seed * 6364136223846793005L) + 1442695040888963407L;
            int j = (int) ((seed >>> 33) % ((long) (i2 + 1)));
            int t = p[i2];
            p[i2] = p[j];
            p[j] = t;
        }
        for (int i3 = 0; i3 < 512; i3++) {
            PERM[i3] = p[i3 & 255];
        }
    }

    private Noise() {
    }

    public static float noise(float x, float y, float z) {
        int xi = floor(x);
        int yi = floor(y);
        int zi = floor(z);
        float xf = x - xi;
        float yf = y - yi;
        float zf = z - zi;
        int X = xi & 255;
        int Y = yi & 255;
        int Z = zi & 255;
        float u = fade(xf);
        float v = fade(yf);
        float w = fade(zf);
        int a = PERM[X] + Y;
        int aa = PERM[a] + Z;
        int ab = PERM[a + 1] + Z;
        int b = PERM[X + 1] + Y;
        int ba = PERM[b] + Z;
        int bb = PERM[b + 1] + Z;
        float x1 = lerp(grad(PERM[aa], xf, yf, zf), grad(PERM[ba], xf - 1.0f, yf, zf), u);
        float x2 = lerp(grad(PERM[ab], xf, yf - 1.0f, zf), grad(PERM[bb], xf - 1.0f, yf - 1.0f, zf), u);
        float y1 = lerp(x1, x2, v);
        float x3 = lerp(grad(PERM[aa + 1], xf, yf, zf - 1.0f), grad(PERM[ba + 1], xf - 1.0f, yf, zf - 1.0f), u);
        float x4 = lerp(grad(PERM[ab + 1], xf, yf - 1.0f, zf - 1.0f), grad(PERM[bb + 1], xf - 1.0f, yf - 1.0f, zf - 1.0f), u);
        return lerp(y1, lerp(x3, x4, v), w);
    }

    public static float noise(float x, float y) {
        return noise(x, y, 0.37f);
    }

    public static float fbm(float x, float y, float z, int octaves) {
        float sum = 0.0f;
        float amp = 0.5f;
        float norm = 0.0f;
        for (int i = 0; i < octaves; i++) {
            sum += amp * noise(x, y, z);
            norm += amp;
            x = (x * 2.03f) + 17.1f;
            y = (y * 2.03f) - 4.7f;
            z = (z * 2.03f) + 9.3f;
            amp *= 0.5f;
        }
        return sum / norm;
    }

    public static float fbm(float x, float y, int octaves) {
        return fbm(x, y, 0.37f, octaves);
    }

    public static float hash(int x, int y, int z) {
        int h = ((x * 522133279) ^ (y * 1540483477)) ^ (z * 668265263);
        int h2 = (h ^ (h >>> 13)) * (-2048144789);
        return ((h2 ^ (h2 >>> 16)) & 65535) / 65535.0f;
    }

    private static int floor(float v) {
        int i = (int) v;
        return v < ((float) i) ? i - 1 : i;
    }

    private static float fade(float t) {
        return t * t * t * ((t * ((t * 6.0f) - 15.0f)) + 10.0f);
    }

    private static float lerp(float a, float b, float t) {
        return a + ((b - a) * t);
    }

    private static float grad(int hash, float x, float y, float z) {
        float f;
        int h = hash & 15;
        float u = h < 8 ? x : y;
        if (h < 4) {
            f = y;
        } else {
            f = (h == 12 || h == 14) ? x : z;
        }
        float v = f;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }
}
