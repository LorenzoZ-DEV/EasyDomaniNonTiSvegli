package imgui;

import java.awt.Color;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/ImColor.class */
public final class ImColor {
    private ImColor() {
    }

    public static int rgba(int r, int g, int b, int a) {
        return intToColor(r, g, b, a);
    }

    public static int rgb(int r, int g, int b) {
        return intToColor(r, g, b);
    }

    public static int rgba(float r, float g, float b, float a) {
        return floatToColor(r, g, b, a);
    }

    public static int rgb(float r, float g, float b) {
        return floatToColor(r, g, b);
    }

    public static int rgba(String hex) {
        return rgbaToColor(hex);
    }

    public static int rgb(String hex) {
        return rgbToColor(hex);
    }

    public static int rgba(ImVec4 color) {
        return rgba(color.x, color.y, color.z, color.w);
    }

    public static int rgb(ImVec4 color) {
        return rgb(color.x, color.y, color.z);
    }

    public static int rgba(Color color) {
        return rgba(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static int rgb(Color color) {
        return rgb(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static int hsla(float h, float s, float l, float a) {
        return hslToColor(h, s, l, a);
    }

    public static int hsl(float h, float s, float l) {
        return hslToColor(h, s, l);
    }

    public static int hsla(int h, int s, int l, int a) {
        return hslToColor(h, s, l, a);
    }

    public static int hsl(int h, int s, int l) {
        return hslToColor(h, s, l);
    }

    @Deprecated
    public static int intToColor(int r, int g, int b, int a) {
        return (a << 24) | (b << 16) | (g << 8) | r;
    }

    @Deprecated
    public static int intToColor(int r, int g, int b) {
        return intToColor(r, g, b, 255);
    }

    @Deprecated
    public static int floatToColor(float r, float g, float b, float a) {
        return intToColor((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), (int) (a * 255.0f));
    }

    @Deprecated
    public static int floatToColor(float r, float g, float b) {
        return floatToColor(r, g, b, 1.0f);
    }

    @Deprecated
    public static int rgbToColor(String hex) {
        return intToColor(Integer.parseInt(hex.substring(1, 3), 16), Integer.parseInt(hex.substring(3, 5), 16), Integer.parseInt(hex.substring(5, 7), 16));
    }

    @Deprecated
    public static int rgbaToColor(String hex) {
        return intToColor(Integer.parseInt(hex.substring(1, 3), 16), Integer.parseInt(hex.substring(3, 5), 16), Integer.parseInt(hex.substring(5, 7), 16), Integer.parseInt(hex.substring(7, 9), 16));
    }

    @Deprecated
    public static int hslToColor(int h, int s, int l) {
        return hslToColor(h, s, l, 1.0f);
    }

    @Deprecated
    public static int hslToColor(int h, int s, int l, float a) {
        return hslToColor(h / 360.0f, s / 100.0f, l / 100.0f, a);
    }

    @Deprecated
    public static int hslToColor(float h, float s, float l) {
        return hslToColor(h, s, l, 1.0f);
    }

    @Deprecated
    public static int hslToColor(float h, float s, float l, float a) {
        float r;
        float g;
        float b;
        if (s == 0.0f) {
            r = l;
            g = l;
            b = l;
        } else {
            float q = ((double) l) < 0.5d ? l * (1.0f + s) : (l + s) - (l * s);
            float p = (2.0f * l) - q;
            r = hue2rgb(p, q, h + 0.33333334f);
            g = hue2rgb(p, q, h);
            b = hue2rgb(p, q, h - 0.33333334f);
        }
        return floatToColor(r, g, b, a);
    }

    private static float hue2rgb(float p, float q, float hue) {
        float h = hue;
        if (h < 0.0f) {
            h += 1.0f;
        }
        if (h > 1.0f) {
            h -= 1.0f;
        }
        if (6.0f * h < 1.0f) {
            return p + ((q - p) * 6.0f * h);
        }
        if (2.0f * h < 1.0f) {
            return q;
        }
        if (3.0f * h < 2.0f) {
            return p + ((q - p) * 6.0f * (0.6666667f - h));
        }
        return p;
    }
}
