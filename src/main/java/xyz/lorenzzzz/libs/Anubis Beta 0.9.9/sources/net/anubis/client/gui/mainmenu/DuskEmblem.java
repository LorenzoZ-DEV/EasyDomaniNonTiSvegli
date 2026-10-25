package net.anubis.client.gui.mainmenu;

import imgui.ImDrawList;
import imgui.ImFont;
import net.anubis.client.gui.EgyptDraw;
import net.anubis.client.gui.ThemeImages;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/DuskEmblem.class */
@Environment(EnvType.CLIENT)
final class DuskEmblem {
    private static final String NAME = "ANUBIS CLIENT";
    private static final float NAME_TRACK = 6.0f;
    private static final float[] ANKH = {0.46f, 0.32285714f, 0.5371429f, 0.4457143f};
    private static final float[] LEFT_EYE = {0.40857142f, 0.43714285f, 0.4485714f, 0.47714287f};
    private static final float[] RIGHT_EYE = {0.55142856f, 0.4457143f, 0.5885714f, 0.48f};
    private static final int HALO_BLUR = 5;
    private static final int GLOW_BLUR = 3;
    private static final float CYCLE = 8.0f;
    private static final float FIRST_PASS = 1.1f;
    private static final float HEAD_PASS = 0.9f;
    private static final float NAME_START = 0.55f;
    private static final float NAME_PASS = 0.8f;
    private static final float BUTTON_START = 1.2f;
    private static final float BUTTON_STAGGER = 0.25f;
    private static final float BUTTON_PASS = 0.8f;
    private static final float EYES_AT = 4.2f;
    private static final float EYES_FOR = 0.7f;
    private static final float ANKH_BREATH = 3.5f;
    static final int LIGHT = -3633;

    private DuskEmblem() {
    }

    static void draw(MenuContext c, float cx, float cy, float size, float nameY) {
        float t = c.time();
        float cycle = cycle(t);
        head(c, cx, cy, size, t, cycle);
        name(c, cx, nameY, cycle);
    }

    private static float cycle(float t) {
        if (t < FIRST_PASS) {
            return -1.0f;
        }
        return (t - FIRST_PASS) % CYCLE;
    }

    static float buttonPass(MenuContext c, int n) {
        float cycle = cycle(c.time());
        if (cycle < 0.0f) {
            return -1.0f;
        }
        float p = ((cycle - BUTTON_START) - (n * BUTTON_STAGGER)) / 0.8f;
        if (p < 0.0f || p > 1.0f) {
            return -1.0f;
        }
        return p;
    }

    static float breath(float t) {
        return 0.5f - (0.5f * ((float) Math.cos(((((double) t) * 3.141592653589793d) * 2.0d) / 3.5d)));
    }

    private static void head(MenuContext c, float cx, float cy, float size, float t, float cycle) {
        ThemeImages.Texture head = c.head();
        if (head == null) {
            return;
        }
        ImDrawList dl = c.bg();
        float in = c.intro(0.0f);
        float size2 = size * (0.96f + (0.04f * in));
        float x0 = cx - (size2 * 0.5f);
        float y0 = cy - (size2 * 0.5f);
        float px = size2 / head.width();
        ThemeImages.Texture halo = c.headBlur(5);
        float pad = 10.0f * px;
        c.image(dl, halo, x0 - pad, y0 - pad, x0 + size2 + pad, y0 + size2 + pad, -16777216, EYES_FOR * in);
        c.image(dl, head, x0, y0, x0 + size2, y0 + size2, -1, in);
        lightUp(c, ANKH, x0, y0, size2, px, in * (0.2f + (0.5f * breath(t))));
        float eyes = cycle < 0.0f ? 0.0f : flash((cycle - EYES_AT) / EYES_FOR);
        lightUp(c, LEFT_EYE, x0, y0, size2, px, in * eyes);
        lightUp(c, RIGHT_EYE, x0, y0, size2, px, in * eyes);
        if (cycle < 0.0f || cycle >= 0.9f) {
            return;
        }
        sweep(c, x0, y0, size2, smooth(cycle / 0.9f), in);
    }

    private static void lightUp(MenuContext c, float[] box, float x0, float y0, float size, float px, float amount) {
        if (amount <= 0.01f) {
            return;
        }
        ImDrawList dl = c.bg();
        float bx0 = x0 + (box[0] * size);
        float by0 = y0 + (box[1] * size);
        float bx1 = x0 + (box[2] * size);
        float by1 = y0 + (box[3] * size);
        ThemeImages.Texture glow = c.headBlur(3);
        float pad = NAME_TRACK * px;
        dl.pushClipRect(bx0 - pad, by0 - pad, bx1 + pad, by1 + pad, true);
        c.image(dl, glow, x0 - pad, y0 - pad, x0 + size + pad, y0 + size + pad, EgyptDraw.GOLD_LIGHT, 0.8f * amount);
        dl.popClipRect();
        dl.pushClipRect(bx0, by0, bx1, by1, true);
        c.image(dl, c.headMask(), x0, y0, x0 + size, y0 + size, LIGHT, 0.6f * amount);
        dl.popClipRect();
    }

    private static void sweep(MenuContext c, float x0, float y0, float size, float progress, float alpha) {
        ThemeImages.Texture mask = c.headMask();
        if (mask == null) {
            return;
        }
        ImDrawList dl = c.bg();
        float half = size * 0.14f;
        float lean = size * 0.35f;
        float top = (x0 - half) + (progress * (size + (half * 2.0f) + lean));
        float y1 = y0 + size;
        float[] xs = {top - half, top, top + half, (top - half) - lean, top - lean, (top + half) - lean};
        int edge = EgyptDraw.col(LIGHT, 0.0f);
        int centre = EgyptDraw.col(LIGHT, 0.9f * alpha);
        dl.pushClipRect(x0, y0, x0 + size, y1, true);
        dl.pushTextureID(mask.id());
        dl.primReserve(12, 6);
        int first = dl.getVtxCurrentIdx();
        int i = 0;
        while (i < 6) {
            float y = i < 3 ? y0 : y1;
            dl.primWriteVtx(xs[i], y, (xs[i] - x0) / size, (y - y0) / size, i % 3 == 1 ? centre : edge);
            i++;
        }
        writeQuad(dl, first, first + 1, first + 4, first + 3);
        writeQuad(dl, first + 1, first + 2, first + 5, first + 4);
        dl.popTextureID();
        dl.popClipRect();
    }

    private static void name(MenuContext c, float cx, float y, float cycle) {
        ImDrawList dl = c.bg();
        ImFont font = c.nameFont();
        float s = c.scale();
        float track = NAME_TRACK * s;
        float width = c.spacedWidth(font, NAME, track);
        float left = cx - (width * 0.5f);
        float light = cycle < 0.0f ? -1.0f : (cycle - NAME_START) / 0.8f;
        float x = left;
        for (int i = 0; i < NAME.length(); i++) {
            String glyph = String.valueOf(NAME.charAt(i));
            float w = c.textWidth(font, glyph);
            float a = c.intro(0.3f + (i * 0.035f));
            float ly = y + ((1.0f - a) * NAME_TRACK * s);
            float at = ((x + (w * 0.5f)) - left) / width;
            float k = (light < 0.0f || light > 1.0f) ? 0.0f : smooth(Math.max(0.0f, 1.0f - (Math.abs(((light * 1.4f) - 0.2f) - at) / 0.16f)));
            c.text(dl, font, x + s, ly + (1.5f * s), -16777216, EYES_FOR * a, glyph);
            c.text(dl, font, x, ly, EgyptDraw.mix(EgyptDraw.GOLD, LIGHT, k), a, glyph);
            x += w + track;
        }
    }

    private static float flash(float x) {
        if (x <= 0.0f || x >= 1.0f) {
            return 0.0f;
        }
        return x < 0.15f ? smooth(x / 0.15f) : smooth(1.0f - ((x - 0.15f) / 0.85f));
    }

    static float smooth(float x) {
        float x2 = Math.max(0.0f, Math.min(1.0f, x));
        return x2 * x2 * (3.0f - (2.0f * x2));
    }

    private static void writeQuad(ImDrawList dl, int a, int b, int c, int d) {
        dl.primWriteIdx(a);
        dl.primWriteIdx(b);
        dl.primWriteIdx(c);
        dl.primWriteIdx(a);
        dl.primWriteIdx(c);
        dl.primWriteIdx(d);
    }
}
