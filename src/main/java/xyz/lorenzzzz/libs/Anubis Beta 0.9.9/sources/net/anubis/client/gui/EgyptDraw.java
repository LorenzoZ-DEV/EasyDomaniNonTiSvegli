package net.anubis.client.gui;

import imgui.ImDrawList;
import imgui.ImFont;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/EgyptDraw.class */
@Environment(EnvType.CLIENT)
public final class EgyptDraw {
    public static final int GOLD = -871878;
    public static final int GOLD_LIGHT = -11654;
    public static final int GOLD_TEXT = -1326998;
    public static final int LACQUER = -16120059;
    public static final int SAND = -2513573;
    public static final int SAND_PALE = -1454184;
    public static final int SAND_DARK = -4618945;
    public static final int LAPIS = -13738055;
    public static final int LAPIS_LIGHT = -12750874;
    public static final int LAPIS_DEEP = -14994064;
    public static final int TURQUOISE = -12794702;
    public static final int MALACHITE = -12600956;
    public static final int CARNELIAN = -2537681;
    public static final int TEXT = -923950;
    public static final int MUTED = -7372954;
    public static final int BLACK = -16777216;
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private static final Map<String, ThemeImages.Texture> TEXTURES = new HashMap();

    private EgyptDraw() {
    }

    public static int col(int argb, float alpha) {
        int a = Math.round(((argb >>> 24) & 255) * Math.max(0.0f, Math.min(1.0f, alpha)));
        return GuiPalette.abgr((a << 24) | (argb & 16777215));
    }

    public static int mix(int from, int to, float k) {
        float k2 = Math.max(0.0f, Math.min(1.0f, k));
        int a = Math.round(((from >>> 24) & 255) + ((((to >>> 24) & 255) - ((from >>> 24) & 255)) * k2));
        int r = Math.round(((from >> 16) & 255) + ((((to >> 16) & 255) - ((from >> 16) & 255)) * k2));
        int g = Math.round(((from >> 8) & 255) + ((((to >> 8) & 255) - ((from >> 8) & 255)) * k2));
        int b = Math.round((from & 255) + (((to & 255) - (from & 255)) * k2));
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static void glow(ImDrawList dl, float cx, float cy, float radius, int argb, float alpha) {
        for (int i = 0; i < 18; i++) {
            float f = 1.0f - (i / 18);
            dl.addCircleFilled(cx, cy, radius * f, col(argb, (alpha / 18) * 2.2f), 48);
        }
    }

    public static void sunDisk(ImDrawList dl, float cx, float cy, float r, float spin, float s, float a) {
        glow(dl, cx, cy, r * 1.9f, -1466054, 0.16f * a);
        dl.addCircleFilled(cx, cy, r, col(-14018806, 0.55f * a), 96);
        dl.addCircle(cx, cy, r, col(GOLD, 0.55f * a), 96, Math.max(1.0f, 1.4f * s));
        dl.addCircle(cx, cy, r * 0.93f, col(GOLD, 0.22f * a), 96, Math.max(1.0f, s));
        for (int i = 0; i < 48; i++) {
            float ang = spin + ((float) (((((double) i) * 3.141592653589793d) * 2.0d) / ((double) 48)));
            float inner = r * 1.07f;
            float outer = r * (i % 2 == 0 ? 1.34f : 1.2f);
            float cos = (float) Math.cos(ang);
            float sin = (float) Math.sin(ang);
            dl.addLine(cx + (cos * inner), cy + (sin * inner), cx + (cos * outer), cy + (sin * outer), col(GOLD, (i % 2 == 0 ? 0.28f : 0.16f) * a), Math.max(1.0f, 1.3f * s));
        }
    }

    public static void text(ImDrawList dl, ImFont font, float x, float y, String text, int abgr) {
        if (font == null) {
            dl.addText(x, y, abgr, text);
        } else {
            dl.addText(font, Math.round(font.getFontSize()), x, y, abgr, text);
        }
    }

    public static float textWidth(ImFont font, String text) {
        return font == null ? text.length() * 7.0f : font.calcTextSizeAX(font.getFontSize(), Float.MAX_VALUE, 0.0f, text);
    }

    public static float spacedWidth(ImFont font, String text, float spacing) {
        float w = 0.0f;
        for (int i = 0; i < text.length(); i++) {
            w += textWidth(font, String.valueOf(text.charAt(i)));
        }
        return w + (spacing * Math.max(0, text.length() - 1));
    }

    public static void drawSpaced(ImDrawList dl, ImFont font, float cx, float y, String text, float spacing, int argb, float a, boolean shadow) {
        float x = cx - (spacedWidth(font, text, spacing) * 0.5f);
        for (int i = 0; i < text.length(); i++) {
            String glyph = String.valueOf(text.charAt(i));
            if (shadow) {
                text(dl, font, x + 1.0f, y + 1.0f, glyph, col(-16777216, 0.7f * a));
            }
            text(dl, font, x, y, glyph, col(argb, a));
            x += textWidth(font, glyph) + spacing;
        }
    }

    public static ThemeImages.Texture texture(String resource) {
        if (TEXTURES.containsKey(resource)) {
            return TEXTURES.get(resource);
        }
        ThemeImages.Texture texture = null;
        try {
            InputStream in = EgyptDraw.class.getResourceAsStream(resource);
            try {
                if (in != null) {
                    texture = ThemeImages.decode(in.readAllBytes(), resource, false);
                } else {
                    LOGGER.warn("[Anubis+] Missing texture {}", resource);
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
        } catch (IOException | RuntimeException e) {
            LOGGER.warn("[Anubis+] Failed to load {}", resource, e);
        }
        TEXTURES.put(resource, texture);
        return texture;
    }

    public static float intro(float t, float delay) {
        float x = Math.max(0.0f, Math.min(1.0f, (t - delay) / 0.6f));
        float inv = 1.0f - x;
        return 1.0f - ((inv * inv) * inv);
    }
}
