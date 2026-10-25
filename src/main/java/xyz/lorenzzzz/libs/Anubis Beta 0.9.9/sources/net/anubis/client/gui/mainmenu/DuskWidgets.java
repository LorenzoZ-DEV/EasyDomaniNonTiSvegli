package net.anubis.client.gui.mainmenu;

import imgui.ImDrawList;
import imgui.ImFont;
import imgui.ImGui;
import imgui.ImGuiInputTextCallbackData;
import imgui.callback.ImGuiInputTextCallback;
import imgui.type.ImString;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BooleanSupplier;
import net.anubis.client.gui.EgyptDraw;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/DuskWidgets.class */
@Environment(EnvType.CLIENT)
public final class DuskWidgets {
    public static final float BUTTON_H = 42.0f;
    public static final float RING = 42.0f;
    public static final float GAP = 11.0f;
    public static final float TAG_H = 24.0f;
    public static final float FIELD_H = 40.0f;
    public static final float PANEL_PAD = 22.0f;
    public static final float DIALOG_W = 560.0f;
    private static final float PANEL_ROUNDING = 8.0f;
    private static final float ROW_ROUNDING = 6.0f;
    private static final float FIELD_ROUNDING = 0.0f;
    private static final float FIELD_PAD = 14.0f;
    private static final float SCROLL_STEP = 64.0f;
    private static final float SCROLLBAR_W = 3.0f;
    private static final float SCROLLBAR_INSET = 5.0f;
    private static final float SCROLL_THUMB_MIN = 28.0f;
    private static final float SCROLLBAR_GRAB = 14.0f;
    private static final float BUTTON_TRACK = 3.0f;
    private static final float TAG_TRACK = 4.0f;
    private static final float DIALOG_FADE = 0.18f;
    private static final float DIALOG_VEIL = 0.62f;
    private static final float LINE_SPACING = 1.35f;
    private static final float EASE = 12.0f;
    private MenuFrame f;
    private int frame;
    private boolean click;
    private boolean doubleClick;
    private boolean mouseDown;
    private float wheel;
    private boolean blocked;
    private final Map<Object, Anim> anims = new HashMap();
    private final Map<Object, Scroll> scrolls = new HashMap();
    private final ArrayDeque<Scroll> openAreas = new ArrayDeque<>();
    private final ArrayDeque<float[]> clips = new ArrayDeque<>();
    private float alpha = 1.0f;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/DuskWidgets$Icon.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface Icon {
        void draw(ImDrawList imDrawList, float f, float f2, float f3, int i, float f4);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/DuskWidgets$Anim.class */
    @Environment(EnvType.CLIENT)
    private static final class Anim {
        float value;
        int frame;

        private Anim() {
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/DuskWidgets$Scroll.class */
    @Environment(EnvType.CLIENT)
    private static final class Scroll {
        float offset;
        float target;
        float grab = Float.NaN;
        float x;
        float y;
        float w;
        float h;
        float content;

        private Scroll() {
        }
    }

    public void begin(MenuFrame current) {
        this.f = current;
        this.frame++;
        this.anims.values().removeIf(anim -> {
            return anim.frame < this.frame - 1;
        });
        this.click = current.clicked();
        this.doubleClick = this.click && ImGui.isMouseDoubleClicked(0);
        this.mouseDown = ImGui.isMouseDown(0);
        this.wheel = ImGui.getIO().getMouseWheel();
        this.blocked = false;
        this.alpha = 1.0f;
        this.openAreas.clear();
        this.clips.clear();
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void end() {
        while (!this.openAreas.isEmpty()) {
            this.openAreas.pop();
            popClip();
        }
    }

    public MenuFrame frame() {
        return this.f;
    }

    public void alpha(float alpha) {
        this.alpha = alpha;
    }

    public float alpha() {
        return this.alpha;
    }

    public boolean hovered(float x, float y, float w, float h) {
        if (this.blocked) {
            return false;
        }
        float mx = this.f.mouseX();
        float my = this.f.mouseY();
        if (mx < x || mx > x + w || my < y || my > y + h) {
            return false;
        }
        float[] clip = this.clips.peek();
        return clip == null || (mx >= clip[0] && mx <= clip[2] && my >= clip[1] && my <= clip[3]);
    }

    public boolean pressed(float x, float y, float w, float h) {
        if (!this.click || !hovered(x, y, w, h)) {
            return false;
        }
        this.click = false;
        return true;
    }

    public float hover(Object key, boolean target) {
        Anim anim = this.anims.computeIfAbsent(key, k -> {
            return new Anim();
        });
        anim.value += ((target ? 1.0f : FIELD_ROUNDING) - anim.value) * Math.min(1.0f, this.f.dt() * EASE);
        anim.frame = this.frame;
        return anim.value;
    }

    public float hoverOf(Object key) {
        Anim anim = this.anims.get(key);
        return anim != null ? anim.value : FIELD_ROUNDING;
    }

    public void tooltip(String text, float centerX, float y) {
        this.f.tooltip(text, centerX, y);
    }

    public boolean button(Object key, String label, float x, float y, float w, float h) {
        return button(key, label, x, y, w, h, true);
    }

    public boolean button(Object key, String label, float x, float y, float w, float h, boolean enabled) {
        boolean over = enabled && hovered(x, y, w, h);
        boolean clicked = over && pressed(x, y, w, h);
        float k = hover(key, over);
        cartoucheButton(this.f.bg(), this.f.menuFont(), x, y, w, h, label.toUpperCase(Locale.ROOT), enabled, k, this.f.scale(), this.alpha);
        return clicked;
    }

    public boolean iconButton(Object key, Icon icon, String tooltip, float x, float y, float size) {
        boolean over = hovered(x, y, size, size);
        boolean clicked = over && pressed(x, y, size, size);
        float k = hover(key, over);
        float s = this.f.scale();
        ringButton(this.f.bg(), icon, x, y, size, k, s, this.alpha);
        if (tooltip != null && k > 0.5f) {
            this.f.tooltip(tooltip, x + (size * 0.5f), y + size + (PANEL_ROUNDING * s));
        }
        return clicked;
    }

    public void panel(float x, float y, float w, float h, String title) {
        float s = this.f.scale();
        panel(this.f.bg(), x, y, w, h, s, this.alpha);
        if (title != null) {
            float plate = 24.0f * s;
            tag(this.f.bg(), this.f.tagFont(), x + (w * 0.5f), y - (plate * 0.5f), plate, title.toUpperCase(Locale.ROOT), s, this.alpha);
        }
    }

    public int row(Object key, float x, float y, float w, float h, boolean selected) {
        boolean over = hovered(x, y, w, h);
        int clicks = 0;
        if (over && pressed(x, y, w, h)) {
            clicks = this.doubleClick ? 2 : 1;
        }
        float k = hover(key, over);
        row(this.f.bg(), x, y, w, h, k, selected, this.f.scale(), this.alpha);
        return clicks;
    }

    public boolean textField(TextField field, float x, float y, float w, float h) {
        String strRepeat;
        ImDrawList dl = this.f.bg();
        float s = this.f.scale();
        boolean over = hovered(x, y, w, h);
        if (over && this.click) {
            this.click = false;
        }
        float k = hover(field, over);
        field.focus += ((field.active ? 1.0f : FIELD_ROUNDING) - field.focus) * Math.min(1.0f, this.f.dt() * EASE);
        field(dl, x, y, w, h, k, field.focus, s, this.alpha);
        ImFont font = this.f.bodyFont();
        float padX = 14.0f * s;
        float padY = Math.max(FIELD_ROUNDING, (h - this.f.textHeight(font)) * 0.5f);
        if (this.blocked) {
            field.active = false;
            String text = field.text();
            boolean empty = text.isEmpty();
            if (empty) {
                strRepeat = field.placeholder;
            } else {
                strRepeat = field.password ? "*".repeat(text.codePointCount(0, text.length())) : text;
            }
            String shown = strRepeat;
            dl.pushClipRect(x + padX, y, (x + w) - padX, y + h, true);
            this.f.text(dl, font, x + padX, y + padY, empty ? EgyptDraw.MUTED : EgyptDraw.TEXT, this.alpha, shown);
            dl.popClipRect();
            return false;
        }
        ImGui.setCursorScreenPos(x, y);
        if (font != null) {
            ImGui.pushFont(font);
        }
        ImGui.pushStyleVar(11, padX, padY);
        ImGui.pushStyleVar(13, FIELD_ROUNDING);
        ImGui.pushStyleVar(12, FIELD_ROUNDING);
        ImGui.pushStyleColor(7, 0);
        ImGui.pushStyleColor(8, 0);
        ImGui.pushStyleColor(9, 0);
        ImGui.pushStyleColor(53, 0);
        ImGui.pushStyleColor(0, EgyptDraw.col(EgyptDraw.TEXT, this.alpha));
        ImGui.pushStyleColor(1, EgyptDraw.col(EgyptDraw.MUTED, this.alpha));
        ImGui.pushStyleColor(51, EgyptDraw.col(EgyptDraw.GOLD, 0.35f * this.alpha));
        try {
            ImGui.setNextItemWidth(w);
            if (field.focusRequested) {
                ImGui.setKeyboardFocusHere();
                field.focusRequested = false;
            }
            boolean submitted = ImGui.inputTextWithHint(field.id, field.placeholder, field.buffer, field.flags(), field.limiter);
            field.active = ImGui.isItemActive();
            return submitted;
        } finally {
            ImGui.popStyleColor(7);
            ImGui.popStyleVar(3);
            if (font != null) {
                ImGui.popFont();
            }
        }
    }

    public float beginScroll(Object key, float x, float y, float w, float h, float contentHeight) {
        Scroll area = this.scrolls.computeIfAbsent(key, k -> {
            return new Scroll();
        });
        float s = this.f.scale();
        area.x = x;
        area.y = y;
        area.w = w;
        area.h = h;
        area.content = contentHeight;
        float max = Math.max(FIELD_ROUNDING, contentHeight - h);
        if (this.wheel != FIELD_ROUNDING && hovered(x, y, w, h)) {
            area.target -= (this.wheel * SCROLL_STEP) * s;
            this.wheel = FIELD_ROUNDING;
        }
        if (max > FIELD_ROUNDING) {
            float[] thumb = thumb(area, s);
            if (Float.isNaN(area.grab)) {
                float grabX = (thumb[0] + (thumb[2] * 0.5f)) - ((14.0f * s) * 0.5f);
                if (pressed(grabX, thumb[1], 14.0f * s, thumb[3])) {
                    area.grab = this.f.mouseY() - thumb[1];
                }
            } else if (!this.mouseDown || this.blocked) {
                area.grab = Float.NaN;
            } else {
                float travel = (h - (10.0f * s)) - thumb[3];
                float along = ((this.f.mouseY() - area.grab) - (y + (5.0f * s))) / Math.max(1.0f, travel);
                float f = along * max;
                area.offset = f;
                area.target = f;
            }
        } else {
            area.grab = Float.NaN;
        }
        area.target = Math.max(FIELD_ROUNDING, Math.min(max, area.target));
        area.offset += (area.target - area.offset) * Math.min(1.0f, this.f.dt() * 16.0f);
        if (Math.abs(area.target - area.offset) < 0.5f) {
            area.offset = area.target;
        }
        area.offset = Math.max(FIELD_ROUNDING, Math.min(max, area.offset));
        this.openAreas.push(area);
        pushClip(x, y, x + w, y + h);
        return Math.round(area.offset);
    }

    public void endScroll() {
        Scroll area = this.openAreas.pop();
        popClip();
        float max = area.content - area.h;
        if (max <= FIELD_ROUNDING) {
            return;
        }
        float s = this.f.scale();
        float[] thumb = thumb(area, s);
        float trackY = area.y + (5.0f * s);
        float trackH = area.h - (10.0f * s);
        ImDrawList dl = this.f.bg();
        boolean over = hovered(thumb[0] - ((14.0f * s) * 0.5f), thumb[1], 14.0f * s, thumb[3]);
        float k = hover(area, over || !Float.isNaN(area.grab));
        dl.addRectFilled(thumb[0], trackY, thumb[0] + thumb[2], trackY + trackH, EgyptDraw.col(EgyptDraw.GOLD, 0.12f * this.alpha), thumb[2]);
        dl.addRectFilled(thumb[0], thumb[1], thumb[0] + thumb[2], thumb[1] + thumb[3], EgyptDraw.col(EgyptDraw.mix(EgyptDraw.GOLD, EgyptDraw.GOLD_LIGHT, k), (0.55f + (0.35f * k)) * this.alpha), thumb[2]);
    }

    public void setScroll(Object key, float offset) {
        Scroll area = this.scrolls.computeIfAbsent(key, k -> {
            return new Scroll();
        });
        area.target = offset;
        area.offset = offset;
    }

    public void pushClip(float x0, float y0, float x1, float y1) {
        float[] outer = this.clips.peek();
        if (outer != null) {
            x0 = Math.max(x0, outer[0]);
            y0 = Math.max(y0, outer[1]);
            x1 = Math.min(x1, outer[2]);
            y1 = Math.min(y1, outer[3]);
        }
        this.clips.push(new float[]{x0, y0, x1, y1});
        ImGui.pushClipRect(x0, y0, x1, y1, true);
    }

    public void popClip() {
        this.clips.pop();
        ImGui.popClipRect();
    }

    public void dialog(Dialog dialog) {
        ImDrawList dl = this.f.bg();
        float s = this.f.scale();
        float sw = this.f.width();
        float sh = this.f.height();
        if (Float.isNaN(dialog.openedAt)) {
            dialog.openedAt = this.f.time();
        }
        float fade = Math.min(1.0f, (this.f.time() - dialog.openedAt) / DIALOG_FADE);
        float a = 1.0f - (((1.0f - fade) * (1.0f - fade)) * (1.0f - fade));
        float saved = this.alpha;
        this.alpha = a;
        try {
            DuskScene.veil(dl, sw, sh, DIALOG_VEIL * a);
            ImFont font = this.f.bodyFont();
            float pad = 22.0f * s;
            float gap = 11.0f * s;
            float bh = 42.0f * s;
            float w = Math.min(560.0f * s, sw - (48.0f * s));
            float inner = w - (pad * 2.0f);
            List<String> lines = dialog.body.isEmpty() ? List.of() : wrap(font, dialog.body, inner);
            float lineH = this.f.textHeight(font) * LINE_SPACING;
            float bodyH = lines.size() * lineH;
            float contentH = dialog.content != null ? dialog.contentHeight * s : FIELD_ROUNDING;
            float top = pad + (24.0f * s * 0.5f);
            float h = top + bodyH + (contentH > FIELD_ROUNDING ? (lines.isEmpty() ? FIELD_ROUNDING : gap) + contentH : FIELD_ROUNDING) + (gap * 2.0f) + bh + pad;
            float x = (sw - w) * 0.5f;
            float y = ((sh - h) * 0.5f) + ((1.0f - a) * PANEL_ROUNDING * s);
            panel(x, y, w, h, dialog.title);
            float cy = y + top;
            for (String line : lines) {
                this.f.text(dl, font, x + ((w - this.f.textWidth(font, line)) * 0.5f), cy, EgyptDraw.TEXT, 0.9f * a, line);
                cy += lineH;
            }
            if (dialog.content != null) {
                if (!lines.isEmpty()) {
                    cy += gap;
                }
                dialog.content.draw(this, dialog, x + pad, cy, inner, contentH);
            }
            List<Dialog.Choice> choices = dialog.choices();
            int n = choices.size();
            float bw = (inner - (gap * (n - 1))) / Math.max(1, n);
            float by = ((y + h) - pad) - bh;
            for (int i = 0; i < n; i++) {
                Dialog.Choice choice = choices.get(i);
                if (button(choice, choice.label, x + pad + (i * (bw + gap)), by, bw, bh)) {
                    dialog.press(choice);
                }
            }
        } finally {
            this.alpha = saved;
        }
    }

    public static void cartouche(ImDrawList dl, float x, float y, float w, float h, float k, float s, float alpha) {
        float r = h * 0.5f;
        float outer = Math.max(1.2f, 1.8f * s);
        float inner = Math.max(1.0f, s);
        float inset = TAG_TRACK * s;
        dl.addRectFilled(x + (2.0f * s), y + (3.0f * s), x + w + (2.0f * s), y + h + (3.0f * s), EgyptDraw.col(-16777216, 0.5f * alpha), r);
        dl.addRectFilled(x, y, x + w, y + h, EgyptDraw.col(EgyptDraw.mix(EgyptDraw.LACQUER, EgyptDraw.GOLD, k), (0.9f + (0.1f * k)) * alpha), r);
        dl.addRect(x, y, x + w, y + h, EgyptDraw.col(EgyptDraw.GOLD, alpha), r, 0, outer);
        dl.addRect(x + inset, y + inset, (x + w) - inset, (y + h) - inset, EgyptDraw.col(EgyptDraw.mix(EgyptDraw.GOLD, -16777216, k), (0.55f + (0.25f * k)) * alpha), r - inset, 0, inner);
    }

    public static void cartoucheButton(ImDrawList dl, ImFont font, float x, float y, float w, float h, String label, boolean enabled, float k, float s, float a) {
        float alpha = a * (enabled ? 1.0f : 0.45f);
        cartouche(dl, x, y, w, h, k, s, alpha);
        float size = font != null ? font.getFontSize() : 15.0f * s;
        int textColor = enabled ? EgyptDraw.mix(EgyptDraw.GOLD_TEXT, -16777216, k) : EgyptDraw.MUTED;
        EgyptDraw.drawSpaced(dl, font, x + (w * 0.5f), y + ((h - size) * 0.5f) + s, label, 3.0f * s, textColor, alpha, k < 0.5f);
    }

    public static void shenRing(ImDrawList dl, float cx, float cy, float r, float k, float s, float a) {
        float outer = Math.max(1.2f, 1.8f * s);
        float inner = Math.max(1.0f, s);
        dl.addCircleFilled(cx + (2.0f * s), cy + (3.0f * s), r, EgyptDraw.col(-16777216, 0.5f * a), 32);
        dl.addCircleFilled(cx, cy, r, EgyptDraw.col(EgyptDraw.mix(EgyptDraw.LACQUER, EgyptDraw.GOLD, k), (0.9f + (0.1f * k)) * a), 32);
        dl.addCircle(cx, cy, r, EgyptDraw.col(EgyptDraw.GOLD, a), 32, outer);
        dl.addCircle(cx, cy, r - (3.5f * s), EgyptDraw.col(EgyptDraw.mix(EgyptDraw.GOLD, -16777216, k), (0.5f + (0.3f * k)) * a), 32, inner);
        dl.addLine(cx - (r * 0.75f), cy + r + (2.5f * s), cx + (r * 0.75f), cy + r + (2.5f * s), EgyptDraw.col(EgyptDraw.GOLD, a), outer);
    }

    public static void ringButton(ImDrawList dl, Icon icon, float x, float y, float size, float k, float s, float a) {
        float cx = x + (size * 0.5f);
        float cy = (y + (size * 0.5f)) - (2.0f * s);
        float r = size * 0.44f;
        shenRing(dl, cx, cy, r, k, s, a);
        icon.draw(dl, cx, cy, r * 0.5f, EgyptDraw.col(EgyptDraw.mix(EgyptDraw.GOLD_TEXT, -16777216, k), a), Math.max(1.2f, 1.6f * s));
    }

    public static float tag(ImDrawList dl, ImFont font, float cx, float y, float h, String text, float s, float a) {
        float spacing = TAG_TRACK * s;
        float w = EgyptDraw.spacedWidth(font, text, spacing) + (SCROLL_THUMB_MIN * s);
        float x = cx - (w * 0.5f);
        dl.addRectFilled(x, y, x + w, y + h, EgyptDraw.col(EgyptDraw.GOLD, a), 2.0f * s);
        dl.addRect(x + (2.5f * s), y + (2.5f * s), (x + w) - (2.5f * s), (y + h) - (2.5f * s), EgyptDraw.col(-16777216, 0.45f * a), s, 0, Math.max(1.0f, s));
        float size = font != null ? font.getFontSize() : 15.0f * s;
        EgyptDraw.drawSpaced(dl, font, cx, y + ((h - size) * 0.5f) + s, text, spacing, -16777216, a, false);
        return w;
    }

    public static void panel(ImDrawList dl, float x, float y, float w, float h, float s, float a) {
        float r = PANEL_ROUNDING * s;
        float inset = TAG_TRACK * s;
        dl.addRectFilled(x + (3.0f * s), y + (TAG_TRACK * s), x + w + (3.0f * s), y + h + (TAG_TRACK * s), EgyptDraw.col(-16777216, 0.45f * a), r);
        dl.addRectFilled(x, y, x + w, y + h, EgyptDraw.col(EgyptDraw.LACQUER, a), r);
        dl.addRect(x, y, x + w, y + h, EgyptDraw.col(EgyptDraw.GOLD, 0.9f * a), r, 0, Math.max(1.0f, 1.4f * s));
        dl.addRect(x + inset, y + inset, (x + w) - inset, (y + h) - inset, EgyptDraw.col(EgyptDraw.GOLD, 0.3f * a), r - inset, 0, Math.max(1.0f, s));
    }

    public static void row(ImDrawList dl, float x, float y, float w, float h, float k, boolean selected, float s, float a) {
        float r = ROW_ROUNDING * s;
        float line = Math.max(1.0f, s);
        if (selected) {
            float inset = 3.0f * s;
            dl.addRectFilled(x, y, x + w, y + h, EgyptDraw.col(EgyptDraw.GOLD, (0.11f + (0.03f * k)) * a), r);
            dl.addRect(x, y, x + w, y + h, EgyptDraw.col(EgyptDraw.GOLD, 0.85f * a), r, 0, Math.max(1.0f, 1.2f * s));
            dl.addRect(x + inset, y + inset, (x + w) - inset, (y + h) - inset, EgyptDraw.col(EgyptDraw.GOLD, 0.25f * a), r - inset, 0, line);
            return;
        }
        dl.addRectFilled(x, y, x + w, y + h, EgyptDraw.col(-16777216, 0.22f * a), r);
        if (k > 0.01f) {
            dl.addRectFilled(x, y, x + w, y + h, EgyptDraw.col(EgyptDraw.GOLD, 0.06f * k * a), r);
            dl.addRect(x, y, x + w, y + h, EgyptDraw.col(EgyptDraw.GOLD, 0.45f * k * a), r, 0, line);
        }
    }

    public static void field(ImDrawList dl, float x, float y, float w, float h, float k, float focus, float s, float a) {
        float r = FIELD_ROUNDING * s;
        dl.addRectFilled(x, y, x + w, y + h, EgyptDraw.col(-16777216, 0.35f * a), r);
        dl.addRect(x, y, x + w, y + h, EgyptDraw.col(EgyptDraw.mix(EgyptDraw.TEXT, EgyptDraw.GOLD, focus), (0.12f + (0.1f * k) + (0.5f * focus)) * a), r, 0, Math.max(1.0f, s));
    }

    public static void drawTooltip(ImDrawList dl, ImFont font, String text, float cx, float y, float screenW, float screenH, float s) {
        float size = font != null ? font.getFontSize() : 15.0f * s;
        float w = EgyptDraw.textWidth(font, text) + (18.0f * s);
        float h = size + (10.0f * s);
        float margin = TAG_TRACK * s;
        float x = Math.max(margin, Math.min((screenW - w) - margin, cx - (w * 0.5f)));
        float y2 = Math.min(y, (screenH - h) - margin);
        dl.addRectFilled(x, y2, x + w, y2 + h, EgyptDraw.col(EgyptDraw.LACQUER, 0.96f), 3.0f * s);
        dl.addRect(x, y2, x + w, y2 + h, EgyptDraw.col(EgyptDraw.GOLD, 0.7f), 3.0f * s, 0, Math.max(1.0f, s));
        EgyptDraw.text(dl, font, x + (9.0f * s), y2 + (5.0f * s), text, EgyptDraw.col(EgyptDraw.GOLD_LIGHT, 1.0f));
    }

    public static List<String> wrap(ImFont font, String text, float width) {
        List<String> lines = new ArrayList<>();
        for (String paragraph : text.split("\n", -1)) {
            String line = "";
            String[] strArrSplit = paragraph.split(" +");
            int length = strArrSplit.length;
            for (int i = 0; i < length; i++) {
                String word = strArrSplit[i];
                if (!word.isEmpty()) {
                    String candidate = line.isEmpty() ? word : line + " " + word;
                    if (EgyptDraw.textWidth(font, candidate) <= width) {
                        line = candidate;
                    } else {
                        if (!line.isEmpty()) {
                            lines.add(line);
                        }
                        while (word.length() > 1 && EgyptDraw.textWidth(font, word) > width) {
                            int cut = fitCount(font, word, width);
                            lines.add(word.substring(0, cut));
                            word = word.substring(cut);
                        }
                        line = word;
                    }
                }
            }
            lines.add(line);
        }
        return lines;
    }

    public static String fit(ImFont font, String text, float width) {
        if (EgyptDraw.textWidth(font, text) <= width) {
            return text;
        }
        int n = fitCount(font, text, width - EgyptDraw.textWidth(font, "..."));
        return text.substring(0, n).stripTrailing() + "...";
    }

    private static int fitCount(ImFont font, String text, float width) {
        int lo = 1;
        int hi = text.length();
        while (lo < hi) {
            int mid = ((lo + hi) + 1) >>> 1;
            if (EgyptDraw.textWidth(font, text.substring(0, mid)) <= width) {
                lo = mid;
            } else {
                hi = mid - 1;
            }
        }
        return Math.min(lo, text.length());
    }

    private float[] thumb(Scroll area, float s) {
        float inset = 5.0f * s;
        float barW = 3.0f * s;
        float trackH = area.h - (2.0f * inset);
        float thumbH = Math.min(trackH, Math.max(SCROLL_THUMB_MIN * s, (trackH * area.h) / area.content));
        float max = Math.max(1.0f, area.content - area.h);
        float y = area.y + inset + ((trackH - thumbH) * Math.max(FIELD_ROUNDING, Math.min(1.0f, area.offset / max)));
        return new float[]{((area.x + area.w) - inset) - barW, y, barW, thumbH};
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/DuskWidgets$TextField.class */
    @Environment(EnvType.CLIENT)
    public static final class TextField {
        private static int nextId;
        private final String id;
        private final int maxLength;
        private final ImString buffer;
        private final ImGuiInputTextCallback limiter;
        private String placeholder;
        private boolean password;
        private boolean focusRequested;
        private boolean active;
        private float focus;

        public TextField(int maxLength) {
            int i = nextId;
            nextId = i + 1;
            this.id = "##dusk_field_" + i;
            this.placeholder = "";
            this.maxLength = maxLength;
            this.buffer = new ImString(maxLength * 3);
            this.limiter = new ImGuiInputTextCallback() { // from class: net.anubis.client.gui.mainmenu.DuskWidgets.TextField.1
                @Override // java.util.function.Consumer
                public void accept(ImGuiInputTextCallbackData data) {
                    TextField.this.limit(data);
                }
            };
        }

        public TextField placeholder(String text) {
            this.placeholder = text;
            return this;
        }

        public TextField password(boolean masked) {
            this.password = masked;
            return this;
        }

        public TextField allowedChars(String chars) {
            this.buffer.inputData.allowedChars = chars;
            return this;
        }

        public String text() {
            return this.buffer.get();
        }

        public void setText(String text) {
            if (text.codePointCount(0, text.length()) > this.maxLength) {
                text = text.substring(0, text.offsetByCodePoints(0, this.maxLength));
            }
            this.buffer.set(text);
        }

        public void focus() {
            this.focusRequested = true;
        }

        public boolean focused() {
            return this.active;
        }

        public int maxLength() {
            return this.maxLength;
        }

        private int flags() {
            return 4194368 | (this.password ? 1024 : 0);
        }

        private void limit(ImGuiInputTextCallbackData data) {
            String text = data.getBuf();
            int over = text.codePointCount(0, text.length()) - this.maxLength;
            if (over <= 0) {
                return;
            }
            byte[] utf8 = text.getBytes(StandardCharsets.UTF_8);
            int caret = Math.min(Math.max(0, data.getCursorPos()), utf8.length);
            String before = new String(utf8, 0, caret, StandardCharsets.UTF_8);
            int cut = Math.min(over, before.codePointCount(0, before.length()));
            if (cut > 0) {
                int start = before.substring(0, before.offsetByCodePoints(before.length(), -cut)).getBytes(StandardCharsets.UTF_8).length;
                data.deleteChars(start, caret - start);
                over -= cut;
            }
            if (over > 0) {
                String rest = data.getBuf();
                int keep = rest.substring(0, rest.offsetByCodePoints(0, this.maxLength)).getBytes(StandardCharsets.UTF_8).length;
                data.deleteChars(keep, data.getBufTextLen() - keep);
            }
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/DuskWidgets$Dialog.class */
    @Environment(EnvType.CLIENT)
    public static final class Dialog {
        private final String title;
        private final String body;
        private Choice cancel;
        private Content content;
        private float contentHeight;
        private boolean closed;
        private final List<Choice> actions = new ArrayList();
        private float openedAt = Float.NaN;

        /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/DuskWidgets$Dialog$Content.class */
        @FunctionalInterface
        @Environment(EnvType.CLIENT)
        public interface Content {
            void draw(DuskWidgets duskWidgets, Dialog dialog, float f, float f2, float f3, float f4);
        }

        /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/DuskWidgets$Dialog$Choice.class */
        @Environment(EnvType.CLIENT)
        private static final class Choice extends Record {
            private final String label;
            private final Runnable run;
            private final BooleanSupplier submit;

            private Choice(String label, Runnable run, BooleanSupplier submit) {
                this.label = label;
                this.run = run;
                this.submit = submit;
            }

            @Override // java.lang.Record
            public final String toString() {
                return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Choice.class), Choice.class, "label;run;submit", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskWidgets$Dialog$Choice;->label:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskWidgets$Dialog$Choice;->run:Ljava/lang/Runnable;", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskWidgets$Dialog$Choice;->submit:Ljava/util/function/BooleanSupplier;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final int hashCode() {
                return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Choice.class), Choice.class, "label;run;submit", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskWidgets$Dialog$Choice;->label:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskWidgets$Dialog$Choice;->run:Ljava/lang/Runnable;", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskWidgets$Dialog$Choice;->submit:Ljava/util/function/BooleanSupplier;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final boolean equals(Object o) {
                return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Choice.class, Object.class), Choice.class, "label;run;submit", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskWidgets$Dialog$Choice;->label:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskWidgets$Dialog$Choice;->run:Ljava/lang/Runnable;", "FIELD:Lnet/anubis/client/gui/mainmenu/DuskWidgets$Dialog$Choice;->submit:Ljava/util/function/BooleanSupplier;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
            }

            public String label() {
                return this.label;
            }

            public Runnable run() {
                return this.run;
            }

            public BooleanSupplier submit() {
                return this.submit;
            }
        }

        public Dialog(String title, String body) {
            this.title = title;
            this.body = body;
        }

        public Dialog action(String label, Runnable run) {
            this.actions.add(new Choice(label, run, null));
            return this;
        }

        public Dialog submit(String label, BooleanSupplier run) {
            this.actions.add(new Choice(label, null, run));
            return this;
        }

        public Dialog cancel(String label) {
            return cancel(label, null);
        }

        public Dialog cancel(String label, Runnable run) {
            this.cancel = new Choice(label, run, null);
            return this;
        }

        public Dialog content(float height, Content content) {
            this.contentHeight = height;
            this.content = content;
            return this;
        }

        public void confirm() {
            if (!this.actions.isEmpty()) {
                press(this.actions.get(0));
            }
        }

        public void dismiss() {
            if (this.cancel == null) {
                close();
            } else {
                press(this.cancel);
            }
        }

        public void close() {
            this.closed = true;
        }

        public boolean isClosed() {
            return this.closed;
        }

        private List<Choice> choices() {
            if (this.cancel == null) {
                return this.actions;
            }
            List<Choice> all = new ArrayList<>(this.actions);
            all.add(this.cancel);
            return all;
        }

        private void press(Choice choice) {
            if (this.closed) {
                return;
            }
            if (choice.submit != null) {
                if (choice.submit.getAsBoolean()) {
                    this.closed = true;
                }
            } else {
                if (choice.run != null) {
                    choice.run.run();
                }
                this.closed = true;
            }
        }
    }
}
