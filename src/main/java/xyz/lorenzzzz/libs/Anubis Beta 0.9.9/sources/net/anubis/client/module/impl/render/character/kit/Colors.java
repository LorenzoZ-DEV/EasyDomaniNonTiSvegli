package net.anubis.client.module.impl.render.character.kit;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Colors.class */
@Environment(EnvType.CLIENT)
public final class Colors {
    public static final int WHITE = -1;
    public static final int BLACK = -16777216;
    public static final int CLEAR = 0;

    private Colors() {
    }

    public static int rgb(int r, int g, int b) {
        return (-16777216) | (clamp(r) << 16) | (clamp(g) << 8) | clamp(b);
    }

    public static int argb(int a, int r, int g, int b) {
        return (clamp(a) << 24) | (clamp(r) << 16) | (clamp(g) << 8) | clamp(b);
    }

    public static int hex(String hex) {
        String h = hex.startsWith("#") ? hex.substring(1) : hex;
        long v = Long.parseLong(h, 16);
        return h.length() <= 6 ? (int) (4278190080L | v) : (int) v;
    }

    public static int hsv(float hueDegrees, float saturation, float value) {
        float r;
        float g;
        float b;
        float h = (((hueDegrees % 360.0f) + 360.0f) % 360.0f) / 60.0f;
        float c = value * saturation;
        float x = c * (1.0f - Math.abs((h % 2.0f) - 1.0f));
        if (h < 1.0f) {
            r = c;
            g = x;
            b = 0.0f;
        } else if (h < 2.0f) {
            r = x;
            g = c;
            b = 0.0f;
        } else if (h < 3.0f) {
            r = 0.0f;
            g = c;
            b = x;
        } else if (h < 4.0f) {
            r = 0.0f;
            g = x;
            b = c;
        } else if (h < 5.0f) {
            r = x;
            g = 0.0f;
            b = c;
        } else {
            r = c;
            g = 0.0f;
            b = x;
        }
        float m = value - c;
        return rgb(Math.round((r + m) * 255.0f), Math.round((g + m) * 255.0f), Math.round((b + m) * 255.0f));
    }

    public static int mix(int a, int b, float t) {
        float s = 1.0f - t;
        return argb(Math.round((alpha(a) * s) + (alpha(b) * t)), Math.round((red(a) * s) + (red(b) * t)), Math.round((green(a) * s) + (green(b) * t)), Math.round((blue(a) * s) + (blue(b) * t)));
    }

    public static int shade(int argb, float k) {
        if (k <= 1.0f) {
            return argb(alpha(argb), Math.round(red(argb) * k), Math.round(green(argb) * k), Math.round(blue(argb) * k));
        }
        float w = Math.min(1.0f, k - 1.0f);
        return argb(alpha(argb), Math.round(red(argb) + ((255 - red(argb)) * w)), Math.round(green(argb) + ((255 - green(argb)) * w)), Math.round(blue(argb) + ((255 - blue(argb)) * w)));
    }

    public static int withAlpha(int argb, float alpha) {
        return (clamp(Math.round(alpha * 255.0f)) << 24) | (argb & 16777215);
    }

    public static int alpha(int argb) {
        return argb >>> 24;
    }

    public static int red(int argb) {
        return (argb >> 16) & 255;
    }

    public static int green(int argb) {
        return (argb >> 8) & 255;
    }

    public static int blue(int argb) {
        return argb & 255;
    }

    static int clamp(int v) {
        if (v < 0) {
            return 0;
        }
        return Math.min(v, 255);
    }
}
