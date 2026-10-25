package net.anubis.client.gui;

import imgui.ImDrawList;
import imgui.ImFont;
import imgui.ImFontGlyph;
import imgui.ImGui;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.anubis.client.config.HudTheme;
import net.anubis.client.config.Surface;
import net.anubis.client.event.impl.ModuleToggleEvent;
import net.anubis.client.imgui.ImGuiManager;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.impl.client.HudModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import net.minecraft.class_3928;
import net.minecraft.class_408;
import net.minecraft.class_424;
import net.minecraft.class_435;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/ToggleToasts.class */
@Environment(EnvType.CLIENT)
public final class ToggleToasts {
    public static final String ID = "toggle_toasts";
    private static final float MARGIN = 10.0f;
    private static final float GAP = 6.0f;
    private static final float CAP_H = 28.0f;
    private static final float PAD_X = 12.0f;
    private static final float PAD_Y = 8.0f;
    private static final float LINE_GAP = 2.0f;
    private static final float GAP_DIV = 10.0f;
    private static final float DIV_H = 12.0f;
    private static final float DOT = 3.0f;
    private static final float GAP_DOT = 8.0f;
    private static final float CHIP_H = 16.0f;
    private static final float CHIP_PAD = 6.0f;
    private static final float CHIP_RADIUS = 4.0f;
    private static final float GAP_CHIP = 10.0f;
    private static final float CHAT_INPUT = 14.0f;
    private static final int MAX_SHOWN = 4;
    private static final float IN_SECONDS = 0.16f;
    private static final float HOLD_SECONDS = 2.0f;
    private static final float OUT_SECONDS = 0.18f;
    private static final float MAX_STEP = 0.033333335f;
    private static final float LIFT_SPEED = 14.0f;
    private static final float OFF_OUTLINE = 0.55f;
    private static final int CHIP_TEXT = -1;
    private static final Object OWNER = new Object();
    private static final Queue<Toggle> PENDING = new ConcurrentLinkedQueue();
    private static final List<Toast> TOASTS = new ArrayList();
    private static float lift = Float.NaN;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/ToggleToasts$Toggle.class */
    @Environment(EnvType.CLIENT)
    private static final class Toggle extends Record {
        private final ClientModule module;
        private final boolean enabled;

        private Toggle(ClientModule module, boolean enabled) {
            this.module = module;
            this.enabled = enabled;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Toggle.class), Toggle.class, "module;enabled", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Toggle;->module:Lnet/anubis/client/module/ClientModule;", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Toggle;->enabled:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Toggle.class), Toggle.class, "module;enabled", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Toggle;->module:Lnet/anubis/client/module/ClientModule;", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Toggle;->enabled:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Toggle.class, Object.class), Toggle.class, "module;enabled", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Toggle;->module:Lnet/anubis/client/module/ClientModule;", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Toggle;->enabled:Z").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public ClientModule module() {
            return this.module;
        }

        public boolean enabled() {
            return this.enabled;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/ToggleToasts$Look.class */
    @Environment(EnvType.CLIENT)
    private static final class Look extends Record {
        private final ImFont name;
        private final int namePx;
        private final ImFont label;
        private final int labelPx;
        private final float s;
        private final float h;
        private final float stateW;
        private final int accent;
        private final boolean guard;

        private Look(ImFont name, int namePx, ImFont label, int labelPx, float s, float h, float stateW, int accent, boolean guard) {
            this.name = name;
            this.namePx = namePx;
            this.label = label;
            this.labelPx = labelPx;
            this.s = s;
            this.h = h;
            this.stateW = stateW;
            this.accent = accent;
            this.guard = guard;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Look.class), Look.class, "name;namePx;label;labelPx;s;h;stateW;accent;guard", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->name:Limgui/ImFont;", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->namePx:I", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->label:Limgui/ImFont;", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->labelPx:I", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->s:F", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->h:F", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->stateW:F", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->accent:I", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->guard:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Look.class), Look.class, "name;namePx;label;labelPx;s;h;stateW;accent;guard", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->name:Limgui/ImFont;", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->namePx:I", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->label:Limgui/ImFont;", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->labelPx:I", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->s:F", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->h:F", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->stateW:F", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->accent:I", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->guard:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Look.class, Object.class), Look.class, "name;namePx;label;labelPx;s;h;stateW;accent;guard", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->name:Limgui/ImFont;", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->namePx:I", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->label:Limgui/ImFont;", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->labelPx:I", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->s:F", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->h:F", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->stateW:F", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->accent:I", "FIELD:Lnet/anubis/client/gui/ToggleToasts$Look;->guard:Z").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public ImFont name() {
            return this.name;
        }

        public int namePx() {
            return this.namePx;
        }

        public ImFont label() {
            return this.label;
        }

        public int labelPx() {
            return this.labelPx;
        }

        public float s() {
            return this.s;
        }

        public float h() {
            return this.h;
        }

        public float stateW() {
            return this.stateW;
        }

        public int accent() {
            return this.accent;
        }

        public boolean guard() {
            return this.guard;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/ToggleToasts$Toast.class */
    @Environment(EnvType.CLIENT)
    private static final class Toast {
        final ClientModule module;
        boolean enabled;
        boolean leaving;
        float shown;
        float hold;
        float open;
        float top;
        float bottom;

        Toast(ClientModule module) {
            this.module = module;
        }
    }

    private ToggleToasts() {
    }

    public static void install() {
        ModuleToggleEvent.TYPE.listen(OWNER, event -> {
            show(event.module, event.enabled);
        });
    }

    public static void show(ClientModule module, boolean enabled) {
        if (module.toggleMessageOn()) {
            class_310 mc = class_310.method_1551();
            if (mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            PENDING.add(new Toggle(module, enabled));
        }
    }

    public static boolean hasWork() {
        return (TOASTS.isEmpty() && PENDING.isEmpty()) ? false : true;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    public static void render() throws MatchException {
        class_310 mc = class_310.method_1551();
        if (mc.field_1687 == null) {
            PENDING.clear();
            TOASTS.clear();
            lift = Float.NaN;
            return;
        }
        float dt = Math.min(ImGuiManager.getInstance().frameDelta(), MAX_STEP);
        step(dt);
        while (true) {
            Toggle toggle = PENDING.poll();
            if (toggle == null) {
                break;
            } else {
                add(toggle);
            }
        }
        if (TOASTS.isEmpty()) {
            lift = Float.NaN;
            return;
        }
        float s = ImGuiManager.getInstance().uiScale();
        lift = Motion.step(lift, liftTarget(mc, s), dt, 14.0f);
        if (!hidden(mc)) {
            draw(s);
        }
    }

    private static void add(Toggle toggle) {
        Toast toast = null;
        for (Toast existing : TOASTS) {
            if (existing.module == toggle.module()) {
                toast = existing;
                break;
            }
        }
        if (toast == null) {
            List<Toast> list = TOASTS;
            Toast toast2 = new Toast(toggle.module());
            toast = toast2;
            list.add(toast2);
        }
        toast.enabled = toggle.enabled();
        toast.leaving = false;
        toast.hold = 2.0f;
        int alive = 1;
        for (int i = TOASTS.size() - 1; i >= 0; i--) {
            Toast other = TOASTS.get(i);
            if (other != toast && !other.leaving) {
                alive++;
                if (alive > 4) {
                    other.leaving = true;
                }
            }
        }
    }

    private static void step(float dt) {
        for (int i = TOASTS.size() - 1; i >= 0; i--) {
            Toast toast = TOASTS.get(i);
            if (toast.leaving) {
                toast.shown -= dt / OUT_SECONDS;
                if (toast.shown <= 0.0f) {
                    TOASTS.remove(i);
                }
            } else if (toast.shown < 1.0f) {
                toast.shown = Math.min(1.0f, toast.shown + (dt / IN_SECONDS));
            } else {
                float f = toast.hold - dt;
                toast.hold = f;
                if (f <= 0.0f) {
                    toast.leaving = true;
                }
            }
        }
    }

    private static float liftTarget(class_310 mc, float s) {
        return mc.field_1755 instanceof class_408 ? (14.0f * mc.method_22683().method_4495()) + (6.0f * s) : 10.0f * s;
    }

    private static boolean hidden(class_310 mc) {
        return (mc.field_1690.field_1842 && mc.field_1755 == null) || mc.method_18506() != null || (mc.field_1755 instanceof class_3928) || (mc.field_1755 instanceof class_424) || (mc.field_1755 instanceof class_435);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private static void draw(float s) throws MatchException {
        HudModule hud = (HudModule) ModuleRegistry.get(HudModule.class);
        HudModule.ToastStyle style = hud != null ? hud.toastStyle() : HudModule.ToastStyle.BAR;
        Surface surface = HudTheme.get().toasts;
        Look k = look(style, s, HudChrome.needsTextGuard(surface));
        float cursor = ImGui.getIO().getDisplaySizeY() - lift;
        for (int i = TOASTS.size() - 1; i >= 0; i--) {
            Toast toast = TOASTS.get(i);
            toast.open = ClickGuiRenderer.easeOut(Math.max(0.0f, Math.min(1.0f, toast.shown)));
            toast.bottom = Math.round(cursor);
            toast.top = Math.round(cursor - (k.h() * toast.open));
            cursor -= (k.h() + (6.0f * s)) * toast.open;
        }
        ImDrawList dl = ImGui.getForegroundDrawList();
        boolean shadow = hud == null || hud.shadow();
        float r = HudTheme.get().rounding.get() * s;
        float left = Math.round(10.0f * s);
        for (Toast toast2 : TOASTS) {
            float cardH = toast2.bottom - toast2.top;
            if (cardH >= 1.0f) {
                String name = toast2.module.name();
                float nameW = HudChrome.width(k.name(), k.namePx(), name);
                float w = measure(style, k, nameW);
                float x = Math.round(left - ((w + left) * (1.0f - toast2.open)));
                float a = toast2.open;
                HudChrome.card(dl, surface, x, toast2.top, w, cardH, r, a, shadow, s);
                boolean clip = cardH < k.h() - 0.5f;
                if (clip) {
                    dl.pushClipRect(x, toast2.top, x + w, toast2.bottom, true);
                }
                float y = Math.round(toast2.top - ((k.h() - cardH) * 0.5f));
                switch (style) {
                    case CARD:
                        drawCard(dl, k, toast2, x, y, a);
                        break;
                    case CHIP:
                        drawChip(dl, k, toast2, x, y, w, a);
                        break;
                    default:
                        drawBar(dl, k, toast2, x, y, nameW, a);
                        break;
                }
                if (clip) {
                    dl.popClipRect();
                }
            }
        }
    }

    private static Look look(HudModule.ToastStyle style, float s, boolean guard) {
        float fRound;
        ImGuiManager gui = ImGuiManager.getInstance();
        ImFont name = gui.hudTextFont();
        ImFont label = gui.hudLabelFont();
        int namePx = HudChrome.px(17.0f, s);
        int labelPx = HudChrome.px(14.0f, s);
        if (style == HudModule.ToastStyle.CARD) {
            fRound = Math.round((8.0f * s * 2.0f) + namePx + (2.0f * s) + labelPx);
        } else {
            fRound = Math.round(CAP_H * s);
        }
        float h = fRound;
        float stateW = Math.max(HudChrome.width(label, labelPx, state(style, true)), HudChrome.width(label, labelPx, state(style, false)));
        if (style == HudModule.ToastStyle.CHIP) {
            stateW = (float) Math.ceil(stateW + (6.0f * s * 2.0f));
        }
        return new Look(name, namePx, label, labelPx, s, h, stateW, WorldText.hudAccent(), guard);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private static String state(HudModule.ToastStyle style, boolean enabled) throws MatchException {
        switch (style) {
            case CARD:
                return enabled ? "Enabled" : "Disabled";
            case CHIP:
                return enabled ? "ON" : "OFF";
            case BAR:
                return enabled ? "on" : "off";
            default:
                throw new MatchException((String) null, (Throwable) null);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private static float measure(HudModule.ToastStyle style, Look k, float nameW) throws MatchException {
        float fStateW;
        float s = k.s();
        switch (style) {
            case CARD:
                fStateW = (12.0f * s * 2.0f) + (14.0f * s) + Math.max(nameW, k.stateW());
                break;
            case CHIP:
                fStateW = (12.0f * s) + nameW + (10.0f * s) + k.stateW() + chipInset(k);
                break;
            case BAR:
                fStateW = (12.0f * s * 2.0f) + nameW + (10.0f * s * 2.0f) + 1.0f + k.stateW();
                break;
            default:
                throw new MatchException((String) null, (Throwable) null);
        }
        float w = fStateW;
        return (float) Math.ceil(w);
    }

    private static float chipInset(Look k) {
        return (float) Math.floor((k.h() - Math.round(16.0f * k.s())) * 0.5f);
    }

    private static void drawBar(ImDrawList dl, Look k, Toast toast, float x, float y, float nameW, float a) {
        float s = k.s();
        float nameTop = y + ((float) Math.floor((k.h() - k.namePx()) * 0.5f));
        float labelTop = nameTop + HudChrome.baselineOffset(k.name(), k.namePx(), k.label(), k.labelPx());
        float cx = Math.round(x + (12.0f * s));
        HudChrome.text(dl, k.name(), k.namePx(), cx, nameTop, HudChrome.scaleAlpha(HudChrome.TEXT_VALUE, a), toast.module.name(), k.guard());
        float cx2 = cx + nameW + (10.0f * s);
        HudChrome.divider(dl, cx2, y + (k.h() * 0.5f), 12.0f * s, a);
        float cx3 = cx2 + 1.0f + (10.0f * s);
        int color = HudChrome.scaleAlpha(toast.enabled ? k.accent() : HudChrome.TEXT_LABEL, a);
        HudChrome.text(dl, k.label(), k.labelPx(), Math.round(cx3), labelTop, color, state(HudModule.ToastStyle.BAR, toast.enabled), k.guard());
    }

    private static void drawCard(ImDrawList dl, Look k, Toast toast, float x, float y, float a) {
        float s = k.s();
        dl.addCircleFilled(x + (15.0f * s), y + (k.h() * 0.5f), DOT * s, HudChrome.scaleAlpha(toast.enabled ? k.accent() : HudChrome.TEXT_LABEL, a), 0);
        float tx = Math.round(x + (26.0f * s));
        float nameTop = Math.round(y + (8.0f * s));
        HudChrome.text(dl, k.name(), k.namePx(), tx, nameTop, HudChrome.scaleAlpha(HudChrome.TEXT_VALUE, a), toast.module.name(), k.guard());
        HudChrome.text(dl, k.label(), k.labelPx(), tx, Math.round(nameTop + k.namePx() + (2.0f * s)), HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, a), state(HudModule.ToastStyle.CARD, toast.enabled), k.guard());
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private static void drawChip(ImDrawList dl, Look k, Toast toast, float x, float y, float w, float a) throws MatchException {
        float s = k.s();
        float nameTop = y + ((float) Math.floor((k.h() - k.namePx()) * 0.5f));
        HudChrome.text(dl, k.name(), k.namePx(), Math.round(x + (12.0f * s)), nameTop, HudChrome.scaleAlpha(HudChrome.TEXT_VALUE, a), toast.module.name(), k.guard());
        float inset = chipInset(k);
        float capH = Math.round(16.0f * s);
        float right = (x + w) - inset;
        float l = right - k.stateW();
        float top = y + inset;
        float r = Math.min(HudTheme.get().rounding.get(), CHIP_RADIUS) * s;
        if (toast.enabled) {
            dl.addRectFilled(l, top, right, top + capH, HudChrome.scaleAlpha(k.accent(), a), r);
        } else {
            dl.addRect(l, top, right, top + capH, HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, a * OFF_OUTLINE), r, 0, 1.0f);
        }
        String word = state(HudModule.ToastStyle.CHIP, toast.enabled);
        float wordX = Math.round(l + ((k.stateW() - HudChrome.width(k.label(), k.labelPx(), word)) * 0.5f));
        float wordY = Math.round((top + (capH * 0.5f)) - capMiddle(k.label(), k.labelPx()));
        HudChrome.text(dl, k.label(), k.labelPx(), wordX, wordY, HudChrome.scaleAlpha(toast.enabled ? -1 : HudChrome.TEXT_LABEL, a), word, !toast.enabled && k.guard());
    }

    private static float capMiddle(ImFont font, int px) {
        float top = px * 0.2f;
        if (font != null && font.getFontSize() > 0.0f) {
            ImFontGlyph glyph = font.findGlyphNoFallback(72);
            if (glyph.isValidPtr()) {
                top = (glyph.getY0() * px) / font.getFontSize();
            }
        }
        return (top + HudChrome.ascent(font, px)) * 0.5f;
    }
}
