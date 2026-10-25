package net.anubis.client.gui;

import imgui.ImDrawList;
import imgui.ImFont;
import imgui.ImGui;
import imgui.flag.ImGuiKey;
import imgui.type.ImString;
import java.util.List;
import java.util.Locale;
import net.anubis.client.imgui.ImGuiManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/AnubisWidgets.class */
@Environment(EnvType.CLIENT)
public final class AnubisWidgets {
    private static final float POPUP_MAX_H = 280.0f;
    private static final Motion.Store ANIM = new Motion.Store();

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/AnubisWidgets$Snap.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface Snap {
        float apply(float f);
    }

    private AnubisWidgets() {
    }

    public static float s(float v) {
        return v * ImGuiManager.getInstance().uiScale();
    }

    public static float animate(int key, boolean on, float speed) {
        return ease(key, on ? 1.0f : 0.0f, speed);
    }

    private static float ease(int key, float target, float speed) {
        return ANIM.ease(key, target, ClickGuiRenderer.animationDelta(ImGuiManager.getInstance().frameDelta()), speed);
    }

    public static void text(ImDrawList dl, ImFont font, float x, float y, int color, String text) {
        text(dl, font, x, y, color, text, false);
    }

    public static void text(ImDrawList dl, ImFont font, float x, float y, int color, String text, boolean guard) {
        int size = font != null ? Math.round(font.getFontSize()) : 0;
        HudChrome.text(dl, font, size, x, y, c(color), text, guard);
    }

    public static float textWidth(ImFont font, String text) {
        return font != null ? font.calcTextSizeAX(font.getFontSize(), Float.MAX_VALUE, 0.0f, text) : ImGui.calcTextSize(text).x;
    }

    public static float lineHeight(ImFont font) {
        return font != null ? font.getFontSize() : ImGui.getTextLineHeight();
    }

    public static String ellipsize(ImFont font, String text, float maxW) {
        if (textWidth(font, text) <= maxW) {
            return text;
        }
        int end = text.length();
        while (end > 0 && textWidth(font, text.substring(0, end) + "...") > maxW) {
            end--;
        }
        return text.substring(0, end).stripTrailing() + "...";
    }

    public static int c(int color) {
        return ImGui.getColorU32i(color);
    }

    public static int withAlpha(int color, float alpha) {
        int a = Math.round(((color >>> 24) & 255) * Math.max(0.0f, Math.min(1.0f, alpha)));
        return (a << 24) | (color & 16777215);
    }

    public static void drawSwitch(ImDrawList dl, float x, float y, float w, float h, float t) {
        float r = h * 0.5f;
        dl.addRectFilled(x, y, x + w, y + h, c(AnubisTheme.lerpColor(AnubisTheme.GUI_SWITCH_OFF, GuiPalette.accent(), t)), r);
        float knobR = r - s(2.5f);
        float cx = x + r + ((w - (2.0f * r)) * t);
        dl.addCircleFilled(cx, y + r + s(1.0f), knobR, c(855638016), 24);
        dl.addCircleFilled(cx, y + r, knobR, c(AnubisTheme.GUI_KNOB), 24);
    }

    public static boolean sliderFloat(String id, float[] value, float min, float max, float width) {
        return sliderFloat(id, value, min, max, width, null);
    }

    public static boolean sliderFloat(String id, float[] value, float min, float max, float width, Snap snap) {
        float hitH = s(18.0f);
        float sx = ImGui.getCursorScreenPosX();
        float cy = ImGui.getCursorScreenPosY() + (hitH * 0.5f);
        ImGui.invisibleButton(id, width, hitH);
        boolean hovered = ImGui.isItemHovered();
        boolean held = ImGui.isItemActive();
        float focus = animate(ImGui.getItemID(), hovered || held, 16.0f);
        boolean changed = false;
        if (held) {
            float t = Math.max(0.0f, Math.min(1.0f, (ImGui.getIO().getMousePosX() - sx) / Math.max(1.0f, width)));
            float nv = min + (t * (max - min));
            if (snap != null) {
                nv = snap.apply(nv);
            }
            if (nv != value[0]) {
                value[0] = nv;
                changed = true;
            }
        }
        float t2 = frac(value[0], min, max);
        ImDrawList dl = ImGui.getWindowDrawList();
        float trackH = s(4.0f);
        float knobR = s(6.5f) + (s(1.5f) * focus);
        float inset = s(7.0f);
        float x0 = sx + inset;
        float x1 = (sx + width) - inset;
        float fillX = x0 + (t2 * (x1 - x0));
        dl.addRectFilled(x0, cy - (trackH * 0.5f), x1, cy + (trackH * 0.5f), c(AnubisTheme.GUI_SURFACE_HOVER), trackH);
        if (fillX > x0 + 0.5f) {
            dl.addRectFilled(x0, cy - (trackH * 0.5f), fillX, cy + (trackH * 0.5f), c(GuiPalette.accent()), trackH);
        }
        if (focus > 0.01f) {
            dl.addCircleFilled(fillX, cy, knobR + s(4.0f), c(withAlpha(GuiPalette.accent(), 0.25f * focus)), 24);
        }
        dl.addCircleFilled(fillX, cy + s(1.0f), knobR, c(1073741824), 24);
        dl.addCircleFilled(fillX, cy, knobR, c(AnubisTheme.GUI_KNOB), 24);
        return changed;
    }

    public static boolean chipButton(String id, String label, float w, float h, boolean active) {
        int iLerpColor;
        float sx = ImGui.getCursorScreenPosX();
        float sy = ImGui.getCursorScreenPosY();
        boolean clicked = ImGui.invisibleButton(id, w, h);
        boolean hovered = ImGui.isItemHovered();
        boolean held = ImGui.isItemActive();
        float hover = animate(ImGui.getItemID(), hovered, 18.0f);
        ImDrawList dl = ImGui.getWindowDrawList();
        if (active) {
            iLerpColor = AnubisTheme.lerpColor(GuiPalette.accent(), GuiPalette.accentHover(), hover * 0.35f);
        } else {
            iLerpColor = AnubisTheme.lerpColor(AnubisTheme.GUI_SURFACE, AnubisTheme.GUI_SURFACE_HOVER, hover);
        }
        int bg = iLerpColor;
        if (held) {
            bg = AnubisTheme.lerpColor(bg, -16777216, 0.12f);
        }
        dl.addRectFilled(sx, sy, sx + w, sy + h, c(bg), s(7.0f));
        ImFont font = ImGui.getFont();
        float tw = textWidth(font, label);
        text(dl, font, sx + ((w - tw) * 0.5f), sy + ((h - lineHeight(font)) * 0.5f), active ? -1 : AnubisTheme.GUI_TEXT, label);
        return clicked;
    }

    public static boolean iconButton(String id, float size) {
        float sx = ImGui.getCursorScreenPosX();
        float sy = ImGui.getCursorScreenPosY();
        boolean clicked = ImGui.invisibleButton(id, size, size);
        float hover = animate(ImGui.getItemID(), ImGui.isItemHovered(), 18.0f);
        ImGui.getWindowDrawList().addCircleFilled(sx + (size * 0.5f), sy + (size * 0.5f), size * 0.5f, c(AnubisTheme.lerpColor(AnubisTheme.GUI_SURFACE, AnubisTheme.GUI_SURFACE_HOVER, hover)), 32);
        return clicked;
    }

    public static int segmented(String id, String[] labels, int selected, float w, float h) {
        float localX = ImGui.getCursorPosX();
        float localY = ImGui.getCursorPosY();
        float bx = ImGui.getCursorScreenPosX();
        float by = ImGui.getCursorScreenPosY();
        float inner = s(3.0f);
        float r = s(8.0f);
        float segW = (w - (inner * 2.0f)) / labels.length;
        float pillX = ease(ImGui.getID(id), inner + (selected * segW), 18.0f);
        ImDrawList dl = ImGui.getWindowDrawList();
        dl.addRectFilled(bx, by, bx + w, by + h, c(AnubisTheme.GUI_SURFACE), r);
        dl.addRectFilled(bx + pillX, by + inner, bx + pillX + segW, (by + h) - inner, c(GuiPalette.accent()), r - inner);
        ImFont font = ImGui.getFont();
        int clicked = -1;
        for (int i = 0; i < labels.length; i++) {
            ImGui.setCursorPos(localX + inner + (i * segW), localY + inner);
            if (ImGui.invisibleButton(id + i, segW, h - (inner * 2.0f))) {
                clicked = i;
            }
            float hover = animate(ImGui.getItemID(), ImGui.isItemHovered(), 18.0f);
            float centre = bx + inner + (i * segW) + (segW * 0.5f);
            float cover = Math.max(0.0f, 1.0f - (Math.abs(centre - ((bx + pillX) + (segW * 0.5f))) / (segW * 0.5f)));
            int color = AnubisTheme.lerpColor(AnubisTheme.lerpColor(AnubisTheme.GUI_TEXT_MUTED, AnubisTheme.GUI_TEXT, hover), -1, cover);
            float tw = textWidth(font, labels[i]);
            text(dl, font, centre - (tw * 0.5f), by + ((h - lineHeight(font)) * 0.5f), color, labels[i]);
        }
        return clicked;
    }

    public static int dropdown(String id, String current, List<String> options, float w, float h) {
        return dropdown(id, current, current, options, w, h, null);
    }

    public static int dropdown(String id, String current, String label, List<String> options, float w, float h, ImString query) {
        String popupId = id + "_popup";
        float sx = ImGui.getCursorScreenPosX();
        float sy = ImGui.getCursorScreenPosY();
        if (ImGui.invisibleButton(id, w, h)) {
            ImGui.openPopup(popupId);
            if (query != null) {
                query.set("");
            }
        }
        boolean open = ImGui.isPopupOpen(popupId);
        float hover = animate(ImGui.getItemID(), ImGui.isItemHovered() || open, 18.0f);
        ImDrawList dl = ImGui.getWindowDrawList();
        float r = s(7.0f);
        dl.addRectFilled(sx, sy, sx + w, sy + h, c(AnubisTheme.lerpColor(AnubisTheme.GUI_SURFACE, AnubisTheme.GUI_SURFACE_HOVER, hover)), r);
        if (open) {
            dl.addRect(sx, sy, sx + w, sy + h, c(withAlpha(GuiPalette.accent(), 0.8f)), r, 0, s(1.2f));
        }
        ImFont font = ImGui.getFont();
        text(dl, font, sx + s(10.0f), sy + ((h - lineHeight(font)) * 0.5f), current.isEmpty() ? AnubisTheme.GUI_TEXT_MUTED : AnubisTheme.GUI_TEXT, ellipsize(font, label, w - s(34.0f)));
        chevron(dl, (sx + w) - s(14.0f), sy + (h * 0.5f), s(4.0f), AnubisTheme.GUI_TEXT_MUTED, open);
        int picked = -1;
        float popupAnim = animate(ImGui.getID(popupId), open, 22.0f);
        float itemH = s(30.0f);
        float roomBelow = ImGui.getIO().getDisplaySizeY() - (sy + h);
        float slide = s(6.0f) * (1.0f - popupAnim);
        if (roomBelow < s(290.0f) && sy > roomBelow) {
            ImGui.setNextWindowPos(sx, (sy - s(4.0f)) + slide, 1, 0.0f, 1.0f);
        } else {
            ImGui.setNextWindowPos(sx, ((sy + h) + s(4.0f)) - slide);
        }
        ImGui.setNextWindowSizeConstraints(w, 0.0f, w, s(POPUP_MAX_H));
        ImGui.pushStyleVar(0, Math.max(0.05f, popupAnim));
        ImGui.pushStyleVar(2, s(4.0f), s(4.0f));
        ImGui.pushStyleVar(14, 0.0f, s(2.0f));
        if (ImGui.beginPopup(popupId, 8)) {
            float rowW = w - s(8.0f);
            String needle = "";
            if (query != null) {
                if (ImGui.isWindowAppearing()) {
                    ImGui.setKeyboardFocusHere();
                }
                ImGui.pushStyleVar(11, s(8.0f), s(6.0f));
                ImGui.setNextItemWidth(rowW);
                ImGui.inputTextWithHint("##search", "Search...", query);
                boolean enter = submittedWithEnter();
                ImGui.popStyleVar();
                needle = query.get().trim().toLowerCase(Locale.ROOT);
                if (enter) {
                    for (int i = 0; i < options.size() && picked < 0; i++) {
                        if (matches(options.get(i), needle)) {
                            picked = i;
                        }
                    }
                    if (picked >= 0) {
                        ImGui.closeCurrentPopup();
                    }
                }
            }
            ImDrawList pdl = ImGui.getWindowDrawList();
            boolean any = false;
            for (int i2 = 0; i2 < options.size(); i2++) {
                String option = options.get(i2);
                if (matches(option, needle)) {
                    any = true;
                    float rx = ImGui.getCursorScreenPosX();
                    float ry = ImGui.getCursorScreenPosY();
                    if (ImGui.invisibleButton("##opt" + i2, rowW, itemH)) {
                        picked = i2;
                        ImGui.closeCurrentPopup();
                    }
                    boolean selected = option.equals(current);
                    float rowHover = animate(ImGui.getItemID(), ImGui.isItemHovered(), 20.0f);
                    if (rowHover > 0.01f) {
                        pdl.addRectFilled(rx, ry, rx + rowW, ry + itemH, c(withAlpha(AnubisTheme.GUI_SURFACE_HOVER, rowHover)), s(6.0f));
                    }
                    text(pdl, font, rx + s(10.0f), ry + ((itemH - lineHeight(font)) * 0.5f), selected ? GuiPalette.accent() : AnubisTheme.GUI_TEXT, ellipsize(font, option, rowW - s(34.0f)));
                    if (selected) {
                        check(pdl, (rx + rowW) - s(14.0f), ry + (itemH * 0.5f), s(5.0f), GuiPalette.accent());
                    }
                }
            }
            if (!any) {
                float rx2 = ImGui.getCursorScreenPosX();
                float ry2 = ImGui.getCursorScreenPosY();
                ImGui.dummy(rowW, itemH);
                text(pdl, font, rx2 + s(10.0f), ry2 + ((itemH - lineHeight(font)) * 0.5f), AnubisTheme.GUI_TEXT_MUTED, options.isEmpty() ? "Nothing to pick yet" : "No matches");
            }
            ImGui.endPopup();
        }
        ImGui.popStyleVar(3);
        return picked;
    }

    private static boolean matches(String option, String needle) {
        return needle.isEmpty() || option.toLowerCase(Locale.ROOT).contains(needle);
    }

    public static boolean submittedWithEnter() {
        return ImGui.isItemDeactivated() && (ImGui.isKeyPressed(ImGuiKey.Enter, false) || ImGui.isKeyPressed(ImGuiKey.KeypadEnter, false));
    }

    public static void chevron(ImDrawList dl, float cx, float cy, float r, int color, boolean up) {
        float dy = up ? (-r) * 0.5f : r * 0.5f;
        dl.addLine(cx - r, cy - dy, cx, cy + dy, c(color), s(1.6f));
        dl.addLine(cx, cy + dy, cx + r, cy - dy, c(color), s(1.6f));
    }

    public static void chevronLeft(ImDrawList dl, float cx, float cy, float r, int color) {
        dl.addLine(cx + (r * 0.5f), cy - r, cx - (r * 0.5f), cy, c(color), s(2.0f));
        dl.addLine(cx - (r * 0.5f), cy, cx + (r * 0.5f), cy + r, c(color), s(2.0f));
    }

    public static void chevronRight(ImDrawList dl, float cx, float cy, float r, int color) {
        dl.addLine(cx - (r * 0.5f), cy - r, cx + (r * 0.5f), cy, c(color), s(2.0f));
        dl.addLine(cx + (r * 0.5f), cy, cx - (r * 0.5f), cy + r, c(color), s(2.0f));
    }

    public static void chevronRotated(ImDrawList dl, float cx, float cy, float r, int color, float angle) {
        float dx = (float) Math.cos(angle);
        float dy = (float) Math.sin(angle);
        float tipX = cx + (dx * r * 0.5f);
        float tipY = cy + (dy * r * 0.5f);
        float backX = cx - ((dx * r) * 0.5f);
        float backY = cy - ((dy * r) * 0.5f);
        dl.addLine(backX - (dy * r), backY + (dx * r), tipX, tipY, c(color), s(1.6f));
        dl.addLine(tipX, tipY, backX + (dy * r), backY - (dx * r), c(color), s(1.6f));
    }

    public static void check(ImDrawList dl, float cx, float cy, float r, int color) {
        dl.addLine(cx - r, cy, cx - (r * 0.3f), cy + (r * 0.7f), c(color), s(1.8f));
        dl.addLine(cx - (r * 0.3f), cy + (r * 0.7f), cx + r, cy - (r * 0.7f), c(color), s(1.8f));
    }

    public static void cross(ImDrawList dl, float cx, float cy, float r, int color) {
        dl.addLine(cx - r, cy - r, cx + r, cy + r, c(color), s(1.6f));
        dl.addLine(cx - r, cy + r, cx + r, cy - r, c(color), s(1.6f));
    }

    public static void magnifier(ImDrawList dl, float cx, float cy, float r, int color) {
        float cr = r * 0.62f;
        dl.addCircle(cx - (r * 0.15f), cy - (r * 0.15f), cr, c(color), 20, s(1.6f));
        dl.addLine(cx + (r * 0.3f), cy + (r * 0.3f), cx + r, cy + r, c(color), s(1.8f));
    }

    public static void crosshair(ImDrawList dl, float cx, float cy, float r, int color) {
        float t = s(1.5f);
        dl.addCircle(cx, cy, r * 0.62f, c(color), 20, t);
        dl.addLine(cx, cy - r, cx, cy - (r * 0.35f), c(color), t);
        dl.addLine(cx, cy + (r * 0.35f), cx, cy + r, c(color), t);
        dl.addLine(cx - r, cy, cx - (r * 0.35f), cy, c(color), t);
        dl.addLine(cx + (r * 0.35f), cy, cx + r, cy, c(color), t);
    }

    public static void grid(ImDrawList dl, float cx, float cy, float r, int color) {
        float g = r * 0.18f;
        float c = r * 0.8f;
        dl.addRectFilled(cx - c, cy - c, cx - g, cy - g, c(color), s(1.5f));
        dl.addRectFilled(cx + g, cy - c, cx + c, cy - g, c(color), s(1.5f));
        dl.addRectFilled(cx - c, cy + g, cx - g, cy + c, c(color), s(1.5f));
        dl.addRectFilled(cx + g, cy + g, cx + c, cy + c, c(color), s(1.5f));
    }

    public static void ring(ImDrawList dl, float cx, float cy, float r, int color) {
        dl.addCircle(cx, cy, r * 0.62f, c(color), 24, r * 0.42f);
    }

    public static void eye(ImDrawList dl, float cx, float cy, float r, int color) {
        float t = s(1.5f);
        dl.addBezierQuadratic(cx - r, cy, cx, cy - (r * 1.15f), cx + r, cy, c(color), t, 16);
        dl.addBezierQuadratic(cx - r, cy, cx, cy + (r * 1.15f), cx + r, cy, c(color), t, 16);
        dl.addCircleFilled(cx, cy, r * 0.3f, c(color), 16);
    }

    public static void monitor(ImDrawList dl, float cx, float cy, float r, int color) {
        float t = s(1.5f);
        dl.addRect(cx - r, cy - (r * 0.8f), cx + r, cy + (r * 0.4f), c(color), s(2.0f), 0, t);
        dl.addLine(cx, cy + (r * 0.4f), cx, cy + (r * 0.8f), c(color), t);
        dl.addLine(cx - (r * 0.5f), cy + (r * 0.85f), cx + (r * 0.5f), cy + (r * 0.85f), c(color), t);
    }

    public static void papyrus(ImDrawList dl, float cx, float cy, float r, int color) {
        float t = s(1.5f);
        float rollW = r * 0.36f;
        float rollH = r * 0.85f;
        float sheetH = r * 0.62f;
        dl.addRect(cx - r, cy - rollH, (cx - r) + rollW, cy + rollH, c(color), rollW * 0.5f, 0, t);
        dl.addRect((cx + r) - rollW, cy - rollH, cx + r, cy + rollH, c(color), rollW * 0.5f, 0, t);
        dl.addLine((cx - r) + rollW, cy - sheetH, (cx + r) - rollW, cy - sheetH, c(color), t);
        dl.addLine((cx - r) + rollW, cy + sheetH, (cx + r) - rollW, cy + sheetH, c(color), t);
        dl.addLine(cx - (r * 0.3f), cy - (r * 0.15f), cx + (r * 0.3f), cy - (r * 0.15f), c(color), t);
        dl.addLine(cx - (r * 0.3f), cy + (r * 0.22f), cx + (r * 0.12f), cy + (r * 0.22f), c(color), t);
    }

    public static void house(ImDrawList dl, float cx, float cy, float r, int color) {
        float t = s(1.5f);
        dl.addLine(cx - r, cy - (r * 0.05f), cx, cy - (r * 0.95f), c(color), t);
        dl.addLine(cx, cy - (r * 0.95f), cx + r, cy - (r * 0.05f), c(color), t);
        dl.addRect(cx - (r * 0.7f), cy - (r * 0.3f), cx + (r * 0.7f), cy + (r * 0.85f), c(color), 0.0f, 0, t);
        dl.addRectFilled(cx - (r * 0.2f), cy + (r * 0.25f), cx + (r * 0.2f), cy + (r * 0.85f), c(color));
    }

    public static void ankh(ImDrawList dl, float cx, float cy, float r, int color) {
        float t = s(1.5f);
        float barY = cy - (r * 0.15f);
        float top = cy - r;
        dl.addBezierCubic(cx, barY, cx - (r * 0.62f), barY - (r * 0.3f), cx - (r * 0.5f), top, cx, top, c(color), t, 12);
        dl.addBezierCubic(cx, barY, cx + (r * 0.62f), barY - (r * 0.3f), cx + (r * 0.5f), top, cx, top, c(color), t, 12);
        dl.addLine(cx - (r * 0.72f), barY, cx + (r * 0.72f), barY, c(color), t);
        dl.addLine(cx, barY, cx, cy + r, c(color), t);
    }

    private static float frac(float v, float min, float max) {
        float r = max - min;
        if (r < 1.0E-6f) {
            return 0.0f;
        }
        return Math.max(0.0f, Math.min(1.0f, (v - min) / r));
    }
}
