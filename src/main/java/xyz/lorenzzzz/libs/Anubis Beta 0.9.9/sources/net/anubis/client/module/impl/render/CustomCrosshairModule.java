package net.anubis.client.module.impl.render;

import imgui.ImDrawList;
import imgui.ImGui;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.Objects;
import net.anubis.client.esp.EspStyle;
import net.anubis.client.gui.AnubisTheme;
import net.anubis.client.gui.AnubisWidgets;
import net.anubis.client.gui.GuiPalette;
import net.anubis.client.imgui.ImGuiManager;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.HudOverlayProvider;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11631;
import net.minecraft.class_12392;
import net.minecraft.class_1309;
import net.minecraft.class_1934;
import net.minecraft.class_2338;
import net.minecraft.class_239;
import net.minecraft.class_310;
import net.minecraft.class_3908;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import net.minecraft.class_4061;
import net.minecraft.class_408;
import net.minecraft.class_746;
import net.minecraft.class_9334;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/CustomCrosshairModule.class */
@Environment(EnvType.CLIENT)
public final class CustomCrosshairModule extends ClientModule implements HudOverlayProvider {
    public static volatile CustomCrosshairModule active;
    private static final String STYLE_CROSS = "Cross";
    private static final String STYLE_T = "T";
    private static final String STYLE_X = "X";
    private static final String STYLE_DOT = "Dot";
    private static final String STYLE_CIRCLE = "Circle";
    private static final String COLOR_CUSTOM = "Custom";
    private static final String COLOR_ACCENT = "Accent";
    private static final int OUTLINE = -1946157056;
    private static final int TRACK_ARGB = -870967267;
    private static final float BAR_HALF_WIDTH = 10.0f;
    private static final float BAR_HEIGHT = 3.0f;
    private static final float DIAGONAL = 0.70710677f;
    private static final double FULL_SPREAD_SPEED = 0.28d;
    private static final float TINT_RATE = 14.0f;
    private static final float SPREAD_RATE = 12.0f;
    private static final float FADE_RATE = 18.0f;
    private final ModeSetting style;
    private final NumberSetting size;
    private final NumberSetting gap;
    private final NumberSetting thickness;
    private final BooleanSetting centerDot;
    private final BooleanSetting outline;
    private final ModeSetting colorMode;
    private final ColorSetting color;
    private final BooleanSetting targetTint;
    private final ColorSetting targetColor;
    private final BooleanSetting dynamicGap;
    private final NumberSetting spread;
    private float tint;
    private float extraGap;
    private float barAlpha;
    private float barFill;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/CustomCrosshairModule$Geometry.class */
    @Environment(EnvType.CLIENT)
    private static final class Geometry extends Record {
        private final float cx;
        private final float cy;
        private final float size;
        private final float open;
        private final float thick;
        private final float below;

        private Geometry(float cx, float cy, float size, float open, float thick, float below) {
            this.cx = cx;
            this.cy = cy;
            this.size = size;
            this.open = open;
            this.thick = thick;
            this.below = below;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Geometry.class), Geometry.class, "cx;cy;size;open;thick;below", "FIELD:Lnet/anubis/client/module/impl/render/CustomCrosshairModule$Geometry;->cx:F", "FIELD:Lnet/anubis/client/module/impl/render/CustomCrosshairModule$Geometry;->cy:F", "FIELD:Lnet/anubis/client/module/impl/render/CustomCrosshairModule$Geometry;->size:F", "FIELD:Lnet/anubis/client/module/impl/render/CustomCrosshairModule$Geometry;->open:F", "FIELD:Lnet/anubis/client/module/impl/render/CustomCrosshairModule$Geometry;->thick:F", "FIELD:Lnet/anubis/client/module/impl/render/CustomCrosshairModule$Geometry;->below:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Geometry.class), Geometry.class, "cx;cy;size;open;thick;below", "FIELD:Lnet/anubis/client/module/impl/render/CustomCrosshairModule$Geometry;->cx:F", "FIELD:Lnet/anubis/client/module/impl/render/CustomCrosshairModule$Geometry;->cy:F", "FIELD:Lnet/anubis/client/module/impl/render/CustomCrosshairModule$Geometry;->size:F", "FIELD:Lnet/anubis/client/module/impl/render/CustomCrosshairModule$Geometry;->open:F", "FIELD:Lnet/anubis/client/module/impl/render/CustomCrosshairModule$Geometry;->thick:F", "FIELD:Lnet/anubis/client/module/impl/render/CustomCrosshairModule$Geometry;->below:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Geometry.class, Object.class), Geometry.class, "cx;cy;size;open;thick;below", "FIELD:Lnet/anubis/client/module/impl/render/CustomCrosshairModule$Geometry;->cx:F", "FIELD:Lnet/anubis/client/module/impl/render/CustomCrosshairModule$Geometry;->cy:F", "FIELD:Lnet/anubis/client/module/impl/render/CustomCrosshairModule$Geometry;->size:F", "FIELD:Lnet/anubis/client/module/impl/render/CustomCrosshairModule$Geometry;->open:F", "FIELD:Lnet/anubis/client/module/impl/render/CustomCrosshairModule$Geometry;->thick:F", "FIELD:Lnet/anubis/client/module/impl/render/CustomCrosshairModule$Geometry;->below:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public float cx() {
            return this.cx;
        }

        public float cy() {
            return this.cy;
        }

        public float size() {
            return this.size;
        }

        public float open() {
            return this.open;
        }

        public float thick() {
            return this.thick;
        }

        public float below() {
            return this.below;
        }
    }

    public CustomCrosshairModule() {
        super("custom-crosshair", "Custom Crosshair", "Draws a configurable crosshair in place of the vanilla one.", ModuleCategory.VISUALS);
        this.style = (ModeSetting) addSetting("style", new ModeSetting("Style", STYLE_CROSS, STYLE_CROSS, STYLE_T, STYLE_X, STYLE_DOT, STYLE_CIRCLE));
        this.size = (NumberSetting) addSetting("size", new NumberSetting("Size", 6.0f, 1.0f, 20.0f).step(1.0f));
        this.gap = (NumberSetting) addSetting("gap", new NumberSetting("Gap", 2.0f, 0.0f, 16.0f).step(1.0f));
        this.thickness = (NumberSetting) addSetting("thickness", new NumberSetting("Thickness", 2.0f, 1.0f, 5.0f).step(1.0f));
        this.centerDot = (BooleanSetting) addSetting("center-dot", new BooleanSetting("Center Dot", false));
        this.outline = (BooleanSetting) addSetting("outline", new BooleanSetting(EspStyle.OUTLINE, true));
        this.colorMode = (ModeSetting) addSetting("color-mode", new ModeSetting("Color", COLOR_CUSTOM, COLOR_CUSTOM, COLOR_ACCENT));
        this.color = (ColorSetting) addSetting("color", new ColorSetting("Custom Color", -657929));
        this.targetTint = (BooleanSetting) addSetting("target-tint", new BooleanSetting("Target Tint", false));
        this.targetColor = (ColorSetting) addSetting("target-color", new ColorSetting("Target Color", -47814));
        this.dynamicGap = (BooleanSetting) addSetting("dynamic-gap", new BooleanSetting("Dynamic Gap", false));
        this.spread = (NumberSetting) addSetting("spread", new NumberSetting("Spread", 4.0f, 1.0f, SPREAD_RATE).step(1.0f));
        this.gap.visibleWhen(this::hasArms);
        this.centerDot.visibleWhen(() -> {
            return !this.style.is(STYLE_DOT);
        });
        this.color.visibleWhen(() -> {
            return this.colorMode.is(COLOR_CUSTOM);
        });
        ColorSetting colorSetting = this.targetColor;
        BooleanSetting booleanSetting = this.targetTint;
        Objects.requireNonNull(booleanSetting);
        colorSetting.visibleWhen(booleanSetting::get);
        this.spread.visibleWhen(() -> {
            return this.dynamicGap.get() && !this.style.is(STYLE_DOT);
        });
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        this.tint = 0.0f;
        this.extraGap = 0.0f;
        this.barAlpha = 0.0f;
        this.barFill = 1.0f;
        active = this;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        active = null;
    }

    @Override // net.anubis.client.module.HudOverlayProvider
    public boolean isHudOverlayEnabled() {
        return isEnabled();
    }

    @Override // net.anubis.client.module.HudOverlayProvider
    public void renderHudOverlay(ImDrawList dl, float screenW, float screenH) {
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (player == null || !isVisible(mc)) {
            return;
        }
        float dt = Math.min(Math.max(ImGui.getIO().getDeltaTime(), 0.0f), 0.1f);
        float scale = ImGuiManager.getInstance().uiScale();
        updateMotion(mc, player, dt);
        Geometry geometry = geometry(screenW * 0.5f, screenH * 0.5f, scale);
        int fill = GuiPalette.abgr(currentColor());
        float fillAlpha = ((fill >>> 24) & 255) / 255.0f;
        float edge = this.outline.get() ? Math.max(1.0f, Math.round(scale)) : 0.0f;
        if (edge > 0.0f) {
            drawShape(dl, geometry, AnubisWidgets.withAlpha(OUTLINE, fillAlpha), edge);
        }
        drawShape(dl, geometry, fill, 0.0f);
        drawCooldown(dl, mc, player, geometry, fill, edge, scale, dt);
    }

    private static boolean isVisible(class_310 mc) {
        if (mc.field_1687 == null || mc.field_1761 == null || mc.field_1690.field_1842) {
            return false;
        }
        if ((mc.field_1755 == null || (mc.field_1755 instanceof class_408)) && mc.field_1690.method_31044().method_31034() && !mc.field_61504.method_72772(class_11631.field_61583)) {
            return mc.field_1761.method_2920() != class_1934.field_9219 || spectatorCanInteract(mc);
        }
        return false;
    }

    private static boolean spectatorCanInteract(class_310 mc) {
        class_3966 class_3966Var = mc.field_1765;
        if (class_3966Var instanceof class_3966) {
            class_3966 entityHit = class_3966Var;
            return entityHit.method_17782() instanceof class_3908;
        }
        if (!(class_3966Var instanceof class_3965)) {
            return false;
        }
        class_3965 blockHit = (class_3965) class_3966Var;
        if (class_3966Var.method_17783() == class_239.class_240.field_1332) {
            class_2338 pos = blockHit.method_17777();
            return mc.field_1687.method_8320(pos).method_26196(mc.field_1687, pos) != null;
        }
        return false;
    }

    /* JADX WARN: Code duplicated, block: B:9:0x002b  */
    private void updateMotion(class_310 mc, class_746 player, float dt) {
        boolean z;
        if (this.targetTint.get()) {
            class_1309 class_1309Var = mc.field_1692;
            if (class_1309Var instanceof class_1309) {
                class_1309 living = class_1309Var;
                if (living.method_5805()) {
                    z = true;
                } else {
                    z = false;
                }
            } else {
                z = false;
            }
        } else {
            z = false;
        }
        boolean onTarget = z;
        this.tint = approach(this.tint, onTarget ? 1.0f : 0.0f, 14.0f, dt);
        float wanted = 0.0f;
        if (this.dynamicGap.get()) {
            double moved = Math.hypot(player.method_23317() - player.field_6014, player.method_23321() - player.field_5969);
            float moving = (float) Math.min(1.0d, moved / FULL_SPREAD_SPEED);
            float swing = 1.0f - player.method_7261(0.0f);
            wanted = this.spread.get() * Math.max(moving, swing);
        }
        this.extraGap = approach(this.extraGap, wanted, SPREAD_RATE, dt);
    }

    private static float approach(float current, float target, float rate, float dt) {
        float next = current + ((target - current) * (1.0f - ((float) Math.exp((-rate) * dt))));
        return Math.abs(target - next) < 0.001f ? target : next;
    }

    private int currentColor() {
        int base = this.colorMode.is(COLOR_ACCENT) ? CosmeticAccent.argb() : this.color.argb();
        return this.tint <= 0.0f ? base : AnubisTheme.lerpColor(base, this.targetColor.argb(), this.tint);
    }

    private boolean hasArms() {
        return this.style.is(STYLE_CROSS) || this.style.is(STYLE_T) || this.style.is(STYLE_X);
    }

    private Geometry geometry(float cx, float cy, float scale) {
        float thick = Math.max(1.0f, Math.round(this.thickness.get() * scale));
        float length = Math.max(1.0f, Math.round(this.size.get() * scale));
        float baseGap = hasArms() ? this.gap.get() : 0.0f;
        float open = Math.round((baseGap + this.extraGap) * scale);
        float maxExtra = (!this.dynamicGap.get() || this.style.is(STYLE_DOT)) ? 0.0f : this.spread.get();
        float below = reachBelow(length, Math.round((baseGap + maxExtra) * scale), thick);
        return new Geometry(cx, cy, length, open, thick, below);
    }

    private float reachBelow(float length, float open, float thick) {
        float f;
        switch (this.style.get()) {
            case "Dot":
                f = length * 0.5f;
                break;
            case "Circle":
                f = length + open + (thick * 0.5f);
                break;
            case "X":
                f = ((open + length) * DIAGONAL) + (thick * 0.5f);
                break;
            default:
                f = open + length + (thick * 0.5f);
                break;
        }
        float below = f;
        return this.centerDot.get() ? Math.max(below, thick * 0.5f) : below;
    }

    private boolean armsJoined(Geometry g) {
        return (this.style.is(STYLE_CROSS) || this.style.is(STYLE_T)) && g.open() < 1.0f;
    }

    private void drawShape(ImDrawList dl, Geometry g, int color, float grow) {
        switch (this.style.get()) {
            case "Dot":
                dl.addCircleFilled(g.cx(), g.cy(), Math.max(1.0f, g.size() * 0.5f) + grow, color, 0);
                break;
            case "Circle":
                dl.addCircle(g.cx(), g.cy(), g.size() + g.open(), color, 0, g.thick() + (grow * 2.0f));
                break;
            case "X":
                drawDiagonals(dl, g, color, grow);
                break;
            default:
                drawArms(dl, g, color, grow, !this.style.is(STYLE_T));
                break;
        }
        if (this.centerDot.get() && !this.style.is(STYLE_DOT) && !armsJoined(g)) {
            float x0 = (float) Math.floor(g.cx() - (g.thick() * 0.5f));
            float y0 = (float) Math.floor(g.cy() - (g.thick() * 0.5f));
            rect(dl, x0, y0, x0 + g.thick(), y0 + g.thick(), color, grow);
        }
    }

    private static void drawArms(ImDrawList dl, Geometry g, int color, float grow, boolean top) {
        float x0 = (float) Math.floor(g.cx() - (g.thick() * 0.5f));
        float x1 = x0 + g.thick();
        float y0 = (float) Math.floor(g.cy() - (g.thick() * 0.5f));
        float y1 = y0 + g.thick();
        float near = g.open();
        float far = g.open() + g.size();
        if (near < 1.0f) {
            rect(dl, x0 - far, y0, x1 + far, y1, color, grow);
            dl.addRectFilled(x0 - grow, y1 + grow, x1 + grow, y1 + far + grow, color);
            if (top) {
                dl.addRectFilled(x0 - grow, (y0 - far) - grow, x1 + grow, y0 - grow, color);
                return;
            }
            return;
        }
        rect(dl, x0 - far, y0, x0 - near, y1, color, grow);
        rect(dl, x1 + near, y0, x1 + far, y1, color, grow);
        rect(dl, x0, y1 + near, x1, y1 + far, color, grow);
        if (top) {
            rect(dl, x0, y0 - far, x1, y0 - near, color, grow);
        }
    }

    private static void drawDiagonals(ImDrawList dl, Geometry g, int color, float grow) {
        float near = Math.max(0.0f, g.open() - grow) * DIAGONAL;
        float far = (g.open() + g.size() + grow) * DIAGONAL;
        float width = g.thick() + (grow * 2.0f);
        for (int sx = -1; sx <= 1; sx += 2) {
            for (int sy = -1; sy <= 1; sy += 2) {
                dl.addLine(g.cx() + (sx * near), g.cy() + (sy * near), g.cx() + (sx * far), g.cy() + (sy * far), color, width);
            }
        }
    }

    private static void rect(ImDrawList dl, float x0, float y0, float x1, float y1, int color, float grow) {
        dl.addRectFilled(x0 - grow, y0 - grow, x1 + grow, y1 + grow, color);
    }

    private void drawCooldown(ImDrawList dl, class_310 mc, class_746 player, Geometry g, int fill, float edge, float scale, float dt) {
        boolean wanted = mc.field_1690.method_42565().method_41753() == class_4061.field_18152;
        float charge = player.method_7261(0.0f);
        boolean ready = wanted && readyOnTarget(mc, player, charge);
        this.barFill = ready ? 1.0f : Math.min(1.0f, charge);
        this.barAlpha = approach(this.barAlpha, (!wanted || (!ready && charge >= 1.0f)) ? 0.0f : 1.0f, 18.0f, dt);
        if (this.barAlpha < 0.01f) {
            return;
        }
        float w = Math.max(SPREAD_RATE, 2.0f * Math.round(BAR_HALF_WIDTH * scale));
        float h = Math.max(2.0f, Math.round(BAR_HEIGHT * scale));
        float x0 = (float) Math.floor(g.cx() - (w * 0.5f));
        float y0 = Math.round(g.cy() + g.below() + edge + (5.0f * scale));
        float alpha = (this.barAlpha * ((fill >>> 24) & 255)) / 255.0f;
        if (edge > 0.0f) {
            dl.addRectFilled(x0 - edge, y0 - edge, x0 + w + edge, y0 + h + edge, AnubisWidgets.withAlpha(OUTLINE, alpha), (h * 0.5f) + edge);
        }
        dl.addRectFilled(x0, y0, x0 + w, y0 + h, AnubisWidgets.withAlpha(GuiPalette.abgr(TRACK_ARGB), alpha), h * 0.5f);
        float filled = Math.round(w * this.barFill);
        if (filled >= 1.0f) {
            dl.addRectFilled(x0, y0, x0 + filled, y0 + h, AnubisWidgets.withAlpha(fill | (-16777216), alpha), h * 0.5f);
        }
    }

    private static boolean readyOnTarget(class_310 mc, class_746 player, float charge) {
        if (charge < 1.0f) {
            return false;
        }
        class_1309 class_1309Var = mc.field_1692;
        if (!(class_1309Var instanceof class_1309)) {
            return false;
        }
        class_1309 target = class_1309Var;
        if (!target.method_5805() || player.method_7279() <= 5.0f) {
            return false;
        }
        class_12392 range = (class_12392) player.method_76694().method_58694(class_9334.field_64680);
        return range == null || mc.field_1765 == null || range.method_76736(player, mc.field_1765.method_17784());
    }
}
