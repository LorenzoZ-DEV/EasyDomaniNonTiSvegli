package net.anubis.client.gui;

import imgui.ImDrawList;
import imgui.ImGui;
import net.anubis.client.config.Surface;
import net.anubis.client.config.ThemeStore;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/SurfaceBackground.class */
@Environment(EnvType.CLIENT)
public final class SurfaceBackground {
    private SurfaceBackground() {
    }

    public static float bannerHeight(Surface surface) {
        if (surface.isBanner() && isVisible(surface)) {
            return AnubisWidgets.s(surface.headerHeight.get());
        }
        return 0.0f;
    }

    public static boolean hasImageBanner(Surface surface) {
        return surface.isImage() && bannerHeight(surface) > 0.0f;
    }

    public static void draw(ImDrawList dl, Surface surface, float x, float y, float w, float h, float rounding) {
        draw(dl, surface, x, y, w, h, rounding, 1.0f);
    }

    public static void draw(ImDrawList dl, Surface surface, float x, float y, float w, float h, float rounding, float alpha) {
        if (alpha <= 0.001f || !isVisible(surface)) {
            return;
        }
        boolean banner = surface.isBanner();
        float areaH = banner ? Math.min(h, AnubisWidgets.s(surface.headerHeight.get())) : h;
        if (surface.isImage()) {
            drawImage(dl, surface, x, y, w, areaH, rounding, banner, alpha, surface.imageScale.get());
        } else {
            drawPattern(dl, surface, x, y, w, areaH, rounding, banner, alpha);
        }
    }

    public static void drawDisc(ImDrawList dl, Surface surface, float cx, float cy, float r, float alpha) {
        if (alpha <= 0.001f || !isVisible(surface)) {
            return;
        }
        float x = cx - r;
        float y = cy - r;
        float size = 2.0f * r;
        if (surface.isImage()) {
            drawImage(dl, surface, x, y, size, size, r, false, alpha, Math.max(1.0f, surface.imageScale.get()));
        } else {
            drawPattern(dl, surface, x, y, size, size, r, false, alpha);
        }
    }

    private static boolean isVisible(Surface surface) {
        if (surface.background.is(Surface.BG_NONE)) {
            return false;
        }
        return (surface.isImage() && ThemeImages.get().texture(surface.image.get()) == null) ? false : true;
    }

    private static void drawImage(ImDrawList dl, Surface surface, float x, float y, float w, float h, float rounding, boolean banner, float alpha, float scale) {
        float[] fArrPlace;
        ThemeImages.Texture texture = ThemeImages.get().texture(surface.image.get());
        if (texture == null) {
            return;
        }
        if (surface.isScreenMapped()) {
            fArrPlace = place(texture, surface, 0.0f, 0.0f, ImGui.getIO().getDisplaySizeX(), ImGui.getIO().getDisplaySizeY(), scale);
        } else {
            fArrPlace = place(texture, surface, x, y, w, h, scale);
        }
        float[] image = fArrPlace;
        drawClipped(dl, texture.id(), image, x, y, w, h, AnubisWidgets.c(AnubisWidgets.withAlpha(-1, surface.imageOpacity.get() * alpha)), rounding, banner ? 48 : 240);
        if (banner) {
            int fill = HudChrome.scaleAlpha(GuiPalette.fill(surface), alpha);
            int clear = fill & 16777215;
            dl.addRectFilledMultiColor(x, y + (h * 0.3f), x + w, y + h + 1.0f, AnubisWidgets.c(clear), AnubisWidgets.c(clear), AnubisWidgets.c(fill), AnubisWidgets.c(fill));
        }
    }

    public static float[] coverUv(int imageW, int imageH, float boxW, float boxH, float focus) {
        float imageAspect = imageW / Math.max(1, imageH);
        float boxAspect = boxW / Math.max(1.0f, boxH);
        if (imageAspect > boxAspect) {
            float span = boxAspect / imageAspect;
            float u0 = (1.0f - span) * 0.5f;
            return new float[]{u0, 0.0f, u0 + span, 1.0f};
        }
        float span2 = imageAspect / boxAspect;
        float v0 = (1.0f - span2) * Math.max(0.0f, Math.min(1.0f, focus));
        return new float[]{0.0f, v0, 1.0f, v0 + span2};
    }

    public static float[] place(int imageW, int imageH, float frameX, float frameY, float frameW, float frameH, float scale, float fx, float fy) {
        float k = Math.max(frameW / Math.max(1, imageW), frameH / Math.max(1, imageH)) * scale;
        float w = imageW * k;
        float h = imageH * k;
        float x0 = frameX + ((frameW - w) * fx);
        float y0 = frameY + ((frameH - h) * fy);
        return new float[]{x0, y0, x0 + w, y0 + h};
    }

    private static float[] place(ThemeImages.Texture texture, Surface surface, float x, float y, float w, float h) {
        return place(texture, surface, x, y, w, h, surface.imageScale.get());
    }

    private static float[] place(ThemeImages.Texture texture, Surface surface, float x, float y, float w, float h, float scale) {
        return place(texture.width(), texture.height(), x, y, w, h, scale, surface.imageX.get(), surface.imageY.get());
    }

    private static void drawClipped(ImDrawList dl, int textureId, float[] image, float x, float y, float w, float h, int tint, float rounding, int corners) {
        float x0 = Math.max(x, image[0]);
        float y0 = Math.max(y, image[1]);
        float x1 = Math.min(x + w, image[2]);
        float y1 = Math.min(y + h, image[3]);
        if (x1 <= x0 || y1 <= y0) {
            return;
        }
        float iw = image[2] - image[0];
        float ih = image[3] - image[1];
        float u0 = (x0 - image[0]) / iw;
        float v0 = (y0 - image[1]) / ih;
        float u1 = (x1 - image[0]) / iw;
        float v1 = (y1 - image[1]) / ih;
        boolean left = x0 - x <= rounding;
        boolean top = y0 - y <= rounding;
        boolean right = (x + w) - x1 <= rounding;
        boolean bottom = (y + h) - y1 <= rounding;
        int reached = ((left && top) ? 16 : 0) | ((right && top) ? 32 : 0) | ((left && bottom) ? 64 : 0) | ((right && bottom) ? 128 : 0);
        int rounded = corners & reached;
        if (rounding > 0.0f && rounded != 0) {
            dl.addImageRounded(textureId, x0, y0, x1, y1, u0, v0, u1, v1, tint, rounding, rounded);
        } else {
            dl.addImage(textureId, x0, y0, x1, y1, u0, v0, u1, v1, tint);
        }
    }

    public static void drawPart(ImDrawList dl, Surface surface, float boxX, float boxY, float boxW, float boxH, float x, float y, float w, float h, float alpha, float rounding, int cornerFlags) {
        if (alpha <= 0.001f || w <= 0.0f || h <= 0.0f || boxW <= 0.0f || boxH <= 0.0f || !isVisible(surface)) {
            return;
        }
        float screenW = ImGui.getIO().getDisplaySizeX();
        float screenH = ImGui.getIO().getDisplaySizeY();
        boolean screen = surface.isScreenMapped();
        if (surface.isImage()) {
            ThemeImages.Texture texture = ThemeImages.get().texture(surface.image.get());
            if (texture == null) {
                return;
            }
            float[] image = screen ? place(texture, surface, 0.0f, 0.0f, screenW, screenH) : place(texture, surface, boxX, boxY, boxW, boxH);
            drawClipped(dl, texture.id(), image, x, y, w, h, AnubisWidgets.c(AnubisWidgets.withAlpha(-1, surface.imageOpacity.get() * alpha)), rounding, cornerFlags);
            return;
        }
        Lines lines = new Lines(dl, x, y, w, h, 0.0f, surface.lineOpacity.get() * alpha, false, screen ? x : x - boxX, screen ? y : y - boxY, screen ? screenW : boxW, false);
        dl.pushClipRect(x, y, x + w, y + h, true);
        drawLines(surface, lines);
        dl.popClipRect();
    }

    private static void drawPattern(ImDrawList dl, Surface surface, float x, float y, float w, float h, float rounding, boolean banner, float alpha) {
        boolean screen = surface.isScreenMapped();
        Lines lines = new Lines(dl, x, y, w, h, rounding, surface.lineOpacity.get() * alpha, banner, screen ? x : 0.0f, screen ? y : 0.0f, screen ? ImGui.getIO().getDisplaySizeX() : w, true);
        drawLines(surface, lines);
    }

    private static void drawLines(Surface surface, Lines lines) {
        ThemeStore theme = ThemeStore.get();
        float time = theme.animateLines.get() ? ((float) ImGui.getTime()) * GuiPalette.animationSpeed() : 0.0f;
        switch (surface.background.get()) {
            case "Flow":
                flow(lines, time);
                break;
            case "Topographic":
                topographic(lines, time);
                break;
            case "Rings":
                rings(lines, time);
                break;
            case "Grid":
                grid(lines, time);
                break;
        }
    }

    private static void flow(Lines lines, float time) {
        float step = AnubisWidgets.s(6.0f);
        float gap = AnubisWidgets.s(10.0f);
        float phase = time * 0.25f;
        int first = (int) Math.floor((lines.originY - AnubisWidgets.s(40.0f)) / gap);
        int last = (int) Math.ceil(((lines.originY + lines.h) + AnubisWidgets.s(40.0f)) / gap);
        for (int i = first; i <= last; i++) {
            float prevX = 0.0f;
            float prevY = 0.0f;
            float f = 0.0f;
            while (true) {
                float lx = f;
                if (lx <= lines.w + step) {
                    float u = (lines.originX + lx) / AnubisWidgets.s(1.0f);
                    float worldY = (i * gap) + (((float) Math.sin((u * 0.018f) + (i * 0.35f) + phase)) * AnubisWidgets.s(14.0f)) + (((float) Math.sin(((u * 0.007f) - (i * 0.2f)) - (phase * 0.6f))) * AnubisWidgets.s(10.0f));
                    float ly = worldY - lines.originY;
                    if (lx > 0.0f) {
                        lines.segment(prevX, prevY, lx, ly);
                    }
                    prevX = lx;
                    prevY = ly;
                    f = lx + step;
                }
            }
        }
    }

    private static void rings(Lines lines, float time) {
        float cx = (lines.worldW + AnubisWidgets.s(10.0f)) - lines.originX;
        float cy = (-AnubisWidgets.s(30.0f)) - lines.originY;
        float gap = AnubisWidgets.s(14.0f);
        float offset = (time * AnubisWidgets.s(4.0f)) % gap;
        float nearest = (float) Math.hypot(Math.max(0.0f, Math.max(-cx, cx - lines.w)), Math.max(0.0f, Math.max(-cy, cy - lines.h)));
        float farthest = (float) Math.hypot(Math.max(Math.abs(cx), Math.abs(cx - lines.w)), Math.max(Math.abs(cy), Math.abs(cy - lines.h)));
        float start = AnubisWidgets.s(24.0f) + offset;
        if (nearest > start) {
            start += ((float) Math.floor((nearest - start) / gap)) * gap;
        }
        float f = start;
        while (true) {
            float r = f;
            if (r <= farthest) {
                int segments = Math.max(24, (int) (r * 0.35f));
                float prevX = cx + r;
                float prevY = cy;
                for (int i = 1; i <= segments; i++) {
                    double a = (6.283185307179586d * ((double) i)) / ((double) segments);
                    float px = cx + (((float) Math.cos(a)) * r);
                    float py = cy + (((float) Math.sin(a)) * r);
                    lines.segment(prevX, prevY, px, py);
                    prevX = px;
                    prevY = py;
                }
                f = r + gap;
            } else {
                return;
            }
        }
    }

    private static void grid(Lines lines, float time) {
        float cell = AnubisWidgets.s(16.0f);
        float step = AnubisWidgets.s(8.0f);
        float drift = (time * AnubisWidgets.s(3.0f)) % cell;
        float startX = floorMod(drift - lines.originX, cell);
        float startY = floorMod(drift - lines.originY, cell);
        float f = startX;
        while (true) {
            float gx = f;
            if (gx > lines.w) {
                break;
            }
            float f2 = 0.0f;
            while (true) {
                float gy = f2;
                if (gy < lines.h) {
                    lines.segment(gx, gy, gx, gy + step);
                    f2 = gy + step;
                }
            }
            f = gx + cell;
        }
        float f3 = startY;
        while (true) {
            float gy2 = f3;
            if (gy2 <= lines.h) {
                float f4 = 0.0f;
                while (true) {
                    float gx2 = f4;
                    if (gx2 < lines.w) {
                        lines.segment(gx2, gy2, gx2 + step, gy2);
                        f4 = gx2 + step;
                    }
                }
                f3 = gy2 + cell;
            } else {
                return;
            }
        }
    }

    private static void topographic(Lines lines, float time) {
        float cell = AnubisWidgets.s(6.0f);
        int cols = ((int) Math.ceil(lines.w / cell)) + 1;
        int rows = ((int) Math.ceil(lines.h / cell)) + 1;
        float[] field = new float[cols * rows];
        float drift = time * 0.15f;
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                float u = (lines.originX + (i * cell)) / AnubisWidgets.s(1.0f);
                float v = (lines.originY + (j * cell)) / AnubisWidgets.s(1.0f);
                field[(j * cols) + i] = (float) (Math.sin((((double) u) * 0.021d) + (Math.sin((((double) v) * 0.03d) + ((double) drift)) * 1.6d)) + Math.cos((((double) v) * 0.028d) + (Math.sin((((double) u) * 0.013d) - ((double) drift)) * 2.1d)) + (0.6d * Math.sin((((double) (u + v)) * 0.017d) + ((double) drift))));
            }
        }
        float f = -2.4f;
        while (true) {
            float level = f;
            if (level <= 2.4f) {
                for (int j2 = 0; j2 < rows - 1; j2++) {
                    for (int i2 = 0; i2 < cols - 1; i2++) {
                        contourCell(lines, field, cols, i2, j2, cell, level);
                    }
                }
                f = level + 0.4f;
            } else {
                return;
            }
        }
    }

    private static void contourCell(Lines lines, float[] f, int cols, int i, int j, float cell, float level) {
        float a = f[(j * cols) + i];
        float b = f[(j * cols) + i + 1];
        float d = f[((j + 1) * cols) + i];
        float cc = f[((j + 1) * cols) + i + 1];
        int index = (a > level ? 8 : 0) | (b > level ? 4 : 0) | (cc > level ? 2 : 0) | (d > level ? 1 : 0);
        if (index == 0 || index == 15) {
            return;
        }
        float x = i * cell;
        float y = j * cell;
        float tx = x + (cell * t(a, b, level));
        float rx = x + cell;
        float ry = y + (cell * t(b, cc, level));
        float bx = x + (cell * t(d, cc, level));
        float by = y + cell;
        float ly = y + (cell * t(a, d, level));
        switch (index) {
            case 1:
            case 14:
                lines.segment(x, ly, bx, by);
                break;
            case 2:
            case 13:
                lines.segment(bx, by, rx, ry);
                break;
            case 3:
            case 12:
                lines.segment(x, ly, rx, ry);
                break;
            case 4:
            case 11:
                lines.segment(tx, y, rx, ry);
                break;
            case 5:
                lines.segment(x, ly, tx, y);
                lines.segment(bx, by, rx, ry);
                break;
            case 6:
            case 9:
                lines.segment(tx, y, bx, by);
                break;
            case 7:
            case 8:
                lines.segment(x, ly, tx, y);
                break;
            case 10:
                lines.segment(tx, y, rx, ry);
                lines.segment(x, ly, bx, by);
                break;
        }
    }

    private static float t(float from, float to, float level) {
        float span = to - from;
        if (Math.abs(span) < 1.0E-6f) {
            return 0.5f;
        }
        return (level - from) / span;
    }

    private static float floorMod(float value, float mod) {
        float r = value % mod;
        return r < 0.0f ? r + mod : r;
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/SurfaceBackground$Lines.class */
    @Environment(EnvType.CLIENT)
    private static final class Lines {
        final ImDrawList dl;
        final float x;
        final float y;
        final float w;
        final float h;
        final float edge;
        final float originX;
        final float originY;
        final float worldW;
        final boolean banner;
        final int color;

        Lines(ImDrawList dl, float x, float y, float w, float h, float rounding, float opacity, boolean banner, float originX, float originY, float worldW, boolean softEdges) {
            this.dl = dl;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.banner = banner;
            this.originX = originX;
            this.originY = originY;
            this.worldW = worldW;
            this.edge = softEdges ? Math.max(AnubisWidgets.s(8.0f), rounding) : 0.0f;
            float strength = banner ? 0.24f : 0.16f;
            this.color = AnubisWidgets.withAlpha(AnubisTheme.lerpColor(-1, GuiPalette.accent(), 0.25f), strength * opacity);
        }

        void segment(float ax, float ay, float bx, float by) {
            float mx = (ax + bx) * 0.5f;
            float my = (ay + by) * 0.5f;
            if (my < 0.0f || my > this.h || mx < 0.0f || mx > this.w) {
                return;
            }
            float fade = 1.0f;
            if (this.banner) {
                float fade2 = 1.0f - (my / this.h);
                fade = fade2 * fade2;
            } else if (this.edge > 0.0f) {
                fade = 1.0f * Math.min(1.0f, Math.min(my, this.h - my) / this.edge);
            }
            if (this.edge > 0.0f) {
                fade *= Math.min(1.0f, Math.min(mx, this.w - mx) / this.edge);
            }
            if (fade <= 0.02f) {
                return;
            }
            this.dl.addLine(this.x + ax, this.y + ay, this.x + bx, this.y + by, AnubisWidgets.c(AnubisWidgets.withAlpha(this.color, fade)), 1.0f);
        }
    }
}
