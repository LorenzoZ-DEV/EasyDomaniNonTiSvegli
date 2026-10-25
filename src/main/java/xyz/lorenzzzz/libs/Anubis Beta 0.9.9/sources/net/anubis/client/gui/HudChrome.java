package net.anubis.client.gui;

import imgui.ImDrawList;
import imgui.ImFont;
import it.unimi.dsi.fastutil.longs.Long2FloatOpenHashMap;
import net.anubis.client.config.Surface;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudChrome.class */
@Environment(EnvType.CLIENT)
final class HudChrome {
    static final int BORDER = 402653183;
    static final int DIVIDER = 536870911;
    static final int TRACK = 452984831;
    private static final int TEXT_GUARD = -1946157056;
    static final int TEXT_VALUE = GuiPalette.abgr(-657929);
    static final int TEXT_NAME = GuiPalette.abgr(-1447442);
    static final int TEXT_LABEL = GuiPalette.abgr(-6052949);
    static final int WARN = GuiPalette.abgr(-47814);
    static final int CHIP_FILL = GuiPalette.abgr(-350873571);
    private static final Long2FloatOpenHashMap DIGIT_CELLS = new Long2FloatOpenHashMap();
    private static final String[] ASCII = new String[128];

    private HudChrome() {
    }

    static {
        DIGIT_CELLS.defaultReturnValue(Float.NaN);
        char c = 0;
        while (true) {
            char c2 = c;
            if (c2 >= ASCII.length) {
                return;
            }
            ASCII[c2] = String.valueOf(c2);
            c = (char) (c2 + 1);
        }
    }

    static void card(ImDrawList dl, Surface surface, float x, float y, float w, float h, float r, float alpha, boolean shadow, float scale) {
        if (alpha <= 0.001f) {
            return;
        }
        if (shadow) {
            shadow(dl, x, y, w, h, r, alpha, scale);
        }
        dl.addRectFilled(x, y, x + w, y + h, scaleAlpha(GuiPalette.fill(surface), alpha), r);
        SurfaceBackground.draw(dl, surface, x, y, w, h, r, alpha);
        dl.addRect(x, y, x + w, y + h, scaleAlpha(BORDER, alpha), r, 0, 1.0f);
    }

    static void shadow(ImDrawList dl, float x, float y, float w, float h, float r, float alpha, float scale) {
        float s = Math.max(1.0f, scale);
        dl.addRectFilled(x - s, y - (0.5f * s), x + w + s, y + h + (1.5f * s), scaleAlpha(771751936, alpha), r + s);
        dl.addRectFilled(x - (2.0f * s), y + (0.5f * s), x + w + (2.0f * s), y + h + (3.5f * s), scaleAlpha(385875968, alpha), r + (2.0f * s));
        dl.addRectFilled(x - (3.0f * s), y + (1.5f * s), x + w + (3.0f * s), y + h + (6.0f * s), scaleAlpha(201326592, alpha), r + (3.0f * s));
    }

    static void divider(ImDrawList dl, float x, float centerY, float height, float alpha) {
        float px = Math.round(x);
        float top = Math.round(centerY - (height * 0.5f));
        dl.addRectFilled(px, top, px + 1.0f, top + Math.round(height), scaleAlpha(DIVIDER, alpha));
    }

    static boolean needsTextGuard(Surface surface) {
        return surface != null && (surface.opacity.get() < 0.55f || surface.isImage());
    }

    static int px(float base, float scale) {
        return Math.max(6, Math.round(base * scale));
    }

    static float width(ImFont font, int size, String text) {
        return (font == null || text.isEmpty()) ? text.length() * size * 0.55f : font.calcTextSizeA(size, Float.MAX_VALUE, 0.0f, text).x;
    }

    static float ascent(ImFont font, int size) {
        return (font == null || font.getFontSize() <= 0.0f) ? size * 0.77f : (font.getAscent() * size) / font.getFontSize();
    }

    static float baselineOffset(ImFont big, int bigSize, ImFont small, int smallSize) {
        return Math.round(ascent(big, bigSize) - ascent(small, smallSize));
    }

    static void text(ImDrawList dl, ImFont font, int size, float x, float y, int color, String text, boolean guard) {
        if (text.isEmpty() || ((color >>> 24) & 255) == 0) {
            return;
        }
        if (guard) {
            raw(dl, font, size, x + 1.0f, y + 1.0f, scaleAlpha(TEXT_GUARD, ((color >>> 24) & 255) / 255.0f), text);
        }
        raw(dl, font, size, x, y, color, text);
    }

    private static void raw(ImDrawList dl, ImFont font, int size, float x, float y, int color, String text) {
        if (font == null) {
            dl.addText(x, y, color, text);
        } else {
            dl.addText(font, size, x, y, color, text);
        }
    }

    private static float digitCell(ImFont font, int size) {
        if (font == null) {
            return size * 0.6f;
        }
        long key = (((long) System.identityHashCode(font)) << 16) ^ ((long) size);
        float cached = DIGIT_CELLS.get(key);
        if (!Float.isNaN(cached)) {
            return cached;
        }
        float cell = 0.0f;
        char c = '0';
        while (true) {
            char c2 = c;
            if (c2 > '9') {
                break;
            }
            cell = Math.max(cell, advance(font, size, c2));
            c = (char) (c2 + 1);
        }
        if (DIGIT_CELLS.size() > 64) {
            DIGIT_CELLS.clear();
        }
        DIGIT_CELLS.put(key, cell);
        return cell;
    }

    private static float advance(ImFont font, int size, char c) {
        return (font.getCharAdvance(c) * size) / font.getFontSize();
    }

    static float tabularWidth(ImFont font, int size, String text) {
        if (font == null) {
            return width(null, size, text);
        }
        float cell = digitCell(font, size);
        float w = 0.0f;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            w += Character.isDigit(c) ? cell : advance(font, size, c);
        }
        return w;
    }

    static void tabular(ImDrawList dl, ImFont font, int size, float x, float y, int color, String text, boolean guard) {
        float f;
        if (font == null) {
            text(dl, null, size, x, y, color, text, guard);
            return;
        }
        float cell = digitCell(font, size);
        float cx = x;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            float adv = advance(font, size, c);
            if (Character.isDigit(c)) {
                text(dl, font, size, cx + ((cell - adv) * 0.5f), y, color, glyph(c), guard);
                f = cell;
            } else {
                if (c != ' ') {
                    text(dl, font, size, cx, y, color, glyph(c), guard);
                }
                f = adv;
            }
            cx += f;
        }
    }

    private static String glyph(char c) {
        return c < ASCII.length ? ASCII[c] : String.valueOf(c);
    }

    static int scaleAlpha(int color, float alphaScale) {
        int alpha = (color >>> 24) & 255;
        int scaled = Math.round(alpha * Math.max(0.0f, Math.min(1.0f, alphaScale)));
        return (color & 16777215) | (scaled << 24);
    }
}
