package net.anubis.client.gui;

import imgui.ImDrawList;
import imgui.ImFont;
import net.anubis.client.config.Surface;
import net.anubis.client.imgui.ImGuiManager;
import net.anubis.client.module.impl.client.HudModule;
import net.anubis.client.module.impl.misc.NoInvLeakModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1304;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_310;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/ArmorStatus.class */
@Environment(EnvType.CLIENT)
final class ArmorStatus {
    private static final float PAD = 6.0f;
    private static final float ICON = 18.0f;
    private static final float TILE_GAP = 5.0f;
    private static final float HANDS_GAP = 11.0f;
    private static final float GAUGE_H = 3.0f;
    private static final float GAUGE_GAP = 3.0f;
    private static final float TEXT_GAP = 2.0f;
    private static final float ROW_GAP = 4.0f;
    private static final float SIDE_GAP = 7.0f;
    private static final float SIDE_GAUGE_W = 44.0f;
    private static final float CRITICAL = 0.12f;
    private static final float GAUGE_RATE = 10.0f;
    private static final float SHOW_TIME = 0.25f;
    private static final String WIDEST = "100%";
    private final float[] wear = new float[ARMOR_AND_HANDS.length];
    private final class_1792[] seen = new class_1792[ARMOR_AND_HANDS.length];
    private float presence;
    private float clock;
    private boolean column;
    private boolean hands;
    private boolean gauge;
    private boolean percent;
    private static final class_1304[] ARMOR = {class_1304.field_6169, class_1304.field_6174, class_1304.field_6172, class_1304.field_6166};
    private static final class_1304[] ARMOR_AND_HANDS = {class_1304.field_6169, class_1304.field_6174, class_1304.field_6172, class_1304.field_6166, class_1304.field_6173, class_1304.field_6171};
    private static final String[] PERCENT = new String[101];
    private static final String[] COUNT = new String[100];

    ArmorStatus() {
    }

    void reset() {
        this.presence = 0.0f;
        for (int i = 0; i < this.seen.length; i++) {
            this.seen[i] = null;
        }
    }

    boolean visible() {
        return this.presence > 0.0f;
    }

    void update(class_310 mc, HudModule hud, boolean active, boolean editMode, float dt) {
        this.clock += dt;
        this.column = hud.armorColumn();
        this.hands = hud.armorHands();
        HudModule.ArmorWear mode = hud.armorWear();
        this.gauge = mode != HudModule.ArmorWear.PERCENT;
        this.percent = mode != HudModule.ArmorWear.GAUGE;
        class_746 player = mc.field_1724;
        boolean worn = false;
        class_1304[] order = order();
        for (int i = 0; i < order.length; i++) {
            class_1799 stack = player == null ? class_1799.field_8037 : player.method_6118(order[i]);
            if (!stack.method_7960()) {
                worn = true;
            }
            float target = remaining(stack);
            if (stack.method_7909() != this.seen[i]) {
                this.seen[i] = stack.method_7909();
                this.wear[i] = target;
            } else {
                this.wear[i] = target < 0.0f ? target : HudGlyphs.approach(Math.max(0.0f, this.wear[i]), target, GAUGE_RATE, dt);
            }
        }
        boolean wanted = editMode || (active && worn);
        if (editMode) {
            this.presence = 1.0f;
        } else {
            this.presence = Math.max(0.0f, Math.min(1.0f, this.presence + ((wanted ? dt : -dt) / SHOW_TIME)));
        }
    }

    private class_1304[] order() {
        return this.hands ? ARMOR_AND_HANDS : ARMOR;
    }

    private static float remaining(class_1799 stack) {
        if (stack.method_7960() || !stack.method_7963() || stack.method_7936() <= 0) {
            return -1.0f;
        }
        return Math.max(0.0f, 1.0f - (stack.method_7919() / stack.method_7936()));
    }

    float planWidth(float s) {
        if (!visible()) {
            return 1.0f;
        }
        int n = order().length;
        float icon = ItemIcons.pixels(18.0f * s);
        float pctW = this.percent ? (float) Math.ceil(percentWidth(s)) : 0.0f;
        if (this.column) {
            float side = (this.percent ? pctW : 0.0f) + (this.gauge ? SIDE_GAUGE_W * s : 0.0f) + ((this.percent && this.gauge) ? SIDE_GAP * s : 0.0f);
            return Math.round((PAD * s * TEXT_GAP) + icon + (SIDE_GAP * s) + side);
        }
        float tile = Math.max(icon, pctW);
        return Math.round((PAD * s * TEXT_GAP) + (n * tile) + ((n - 1) * 5.0f * s) + (this.hands ? (11.0f * s) - (5.0f * s) : 0.0f));
    }

    float planHeight(float s) {
        int n = order().length;
        float icon = ItemIcons.pixels(18.0f * s);
        if (this.column) {
            return Math.round((PAD * s * TEXT_GAP) + (n * icon) + ((n - 1) * ROW_GAP * s) + (this.hands ? (11.0f * s) - (ROW_GAP * s) : 0.0f));
        }
        float below = (this.gauge ? (3.0f * s) + gaugeHeight(s) : 0.0f) + (this.percent ? (TEXT_GAP * s) + HudChrome.px(14.0f, s) : 0.0f);
        return Math.round((PAD * s * TEXT_GAP) + icon + below);
    }

    private static float percentWidth(float s) {
        return HudChrome.tabularWidth(ImGuiManager.getInstance().hudLabelFont(), HudChrome.px(14.0f, s), WIDEST);
    }

    private static float gaugeHeight(float s) {
        return Math.max(TEXT_GAP, Math.round(3.0f * s));
    }

    void draw(ImDrawList dl, HudLayoutStore.HudElementState state, float x, float y, float alpha, boolean shadow, class_310 mc) {
        float a = alpha * HudGlyphs.easeOutQuint(this.presence);
        if (a <= 0.004f || mc.field_1724 == null) {
            return;
        }
        float s = HudRenderer.scaleOf(state);
        float w = state.width;
        float h = state.height;
        Surface surface = HudRenderer.hudSurface(state);
        boolean guard = HudChrome.needsTextGuard(surface);
        HudChrome.card(dl, surface, x, y, w, h, HudRenderer.hudRounding() * s, a, shadow, s);
        ImFont label = ImGuiManager.getInstance().hudLabelFont();
        int labelPx = HudChrome.px(14.0f, s);
        int icon = (int) ItemIcons.pixels(18.0f * s);
        float pad = Math.round(PAD * s);
        float pctW = this.percent ? (float) Math.ceil(percentWidth(s)) : 0.0f;
        float tile = Math.max(icon, pctW);
        class_1304[] order = order();
        float cx = x + pad;
        float cy = y + pad;
        for (int i = 0; i < order.length; i++) {
            if (i == ARMOR.length) {
                drawHandsRule(dl, x, y, w, h, cx, cy, s, a);
                if (this.column) {
                    cy += SIDE_GAP * s;
                } else {
                    cx += PAD * s;
                }
            }
            class_1799 worn = mc.field_1724.method_6118(order[i]);
            class_1799 stack = NoInvLeakModule.shownInHud(worn, order[i]);
            float left = stack == worn ? this.wear[i] : -1.0f;
            if (this.column) {
                drawSideways(dl, stack, left, cx, Math.round(cy), icon, pctW, label, labelPx, s, a, guard);
                cy += icon + (ROW_GAP * s);
            } else {
                drawStacked(dl, stack, left, Math.round(cx), cy, icon, tile, label, labelPx, s, a, guard);
                cx += tile + (5.0f * s);
            }
        }
    }

    private void drawHandsRule(ImDrawList dl, float x, float y, float w, float h, float cx, float cy, float s, float a) {
        int rule = HudGlyphs.ink(EgyptDraw.GOLD, a * 0.45f);
        float t = Math.max(1.0f, Math.round(s));
        if (this.column) {
            float ry = Math.round((cy + (((11.0f * s) - (ROW_GAP * s)) * 0.5f)) - ((ROW_GAP * s) * 0.5f));
            HudGlyphs.register(dl, x + (w * 0.15f), x + (w * 0.85f), ry, t, rule);
        } else {
            float rx = Math.round((cx + (((11.0f * s) - (5.0f * s)) * 0.5f)) - ((5.0f * s) * 0.5f));
            int clear = rule & 16777215;
            dl.addRectFilledMultiColor(rx, y + (h * 0.18f), rx + t, y + (h * 0.5f), clear, clear, rule, rule);
            dl.addRectFilledMultiColor(rx, y + (h * 0.5f), rx + t, y + (h * 0.82f), rule, rule, clear, clear);
        }
    }

    private void drawStacked(ImDrawList dl, class_1799 stack, float left, float tx, float ty, int icon, float tile, ImFont label, int labelPx, float s, float a, boolean guard) {
        float ix = Math.round(tx + ((tile - icon) * 0.5f));
        drawIcon(dl, stack, left, ix, ty, icon, label, labelPx, s, a);
        float below = ty + icon;
        if (this.gauge) {
            float below2 = below + (3.0f * s);
            drawGauge(dl, stack, left, tx, below2, tile, s, a);
            below = below2 + gaugeHeight(s);
        }
        if (this.percent && left >= 0.0f) {
            String value = percentText(left);
            float vw = HudChrome.tabularWidth(label, labelPx, value);
            HudChrome.tabular(dl, label, labelPx, Math.round(tx + ((tile - vw) * 0.5f)), Math.round(below + (TEXT_GAP * s)), HudGlyphs.ink(HudGlyphs.vitality(left), a * criticalPulse(left)), value, guard);
        }
    }

    private void drawSideways(ImDrawList dl, class_1799 stack, float left, float tx, float ty, int icon, float pctW, ImFont label, int labelPx, float s, float a, boolean guard) {
        drawIcon(dl, stack, left, tx, ty, icon, label, labelPx, s, a);
        float cx = tx + icon + (SIDE_GAP * s);
        float mid = ty + (icon * 0.5f);
        if (this.percent) {
            if (left >= 0.0f) {
                String value = percentText(left);
                float vw = HudChrome.tabularWidth(label, labelPx, value);
                HudChrome.tabular(dl, label, labelPx, Math.round((cx + pctW) - vw), Math.round(mid - (labelPx * 0.5f)), HudGlyphs.ink(HudGlyphs.vitality(left), a * criticalPulse(left)), value, guard);
            }
            cx += pctW + (SIDE_GAP * s);
        }
        if (this.gauge) {
            drawGauge(dl, stack, left, cx, Math.round(mid - (gaugeHeight(s) * 0.5f)), SIDE_GAUGE_W * s, s, a);
        }
    }

    private void drawIcon(ImDrawList dl, class_1799 stack, float left, float ix, float iy, int icon, ImFont label, int labelPx, float s, float a) {
        if (stack.method_7960()) {
            float inset = Math.max(1.0f, Math.round(TEXT_GAP * s));
            dl.addRect(ix + inset, iy + inset, (ix + icon) - inset, (iy + icon) - inset, HudChrome.scaleAlpha(536870911, a), Math.max(1.0f, 3.0f * s), 0, 1.0f);
            dl.addCircleFilled(ix + (icon * 0.5f), iy + (icon * 0.5f), Math.max(1.0f, 1.2f * s), HudChrome.scaleAlpha(536870911, a), 8);
            return;
        }
        if (left >= 0.0f && left < CRITICAL) {
            float glow = 0.5f + (0.5f * ((float) Math.sin(this.clock * 6.5f)));
            dl.addCircleFilled(ix + (icon * 0.5f), iy + (icon * 0.5f), icon * 0.62f, HudGlyphs.ink(EgyptDraw.CARNELIAN, a * (0.18f + (0.22f * glow))), 20);
        }
        ItemIcons.draw(dl, stack, ix, iy, icon, a);
        if (stack.method_7958()) {
            HudGlyphs.sparkle(dl, (ix + icon) - (1.5f * s), iy + (1.5f * s), 2.8f * s, HudGlyphs.ink(EgyptDraw.GOLD_LIGHT, a * 0.9f));
        }
        int count = stack.method_7947();
        if (count > 1) {
            String value = countText(count);
            float vw = HudChrome.width(label, labelPx, value);
            HudChrome.text(dl, label, labelPx, Math.round(((ix + icon) - vw) + (TEXT_GAP * s)), Math.round(((iy + icon) - labelPx) + (3.0f * s)), HudChrome.scaleAlpha(-1, a), value, true);
        }
    }

    private void drawGauge(ImDrawList dl, class_1799 stack, float left, float gx, float gy, float gw, float s, float a) {
        float gh = gaugeHeight(s);
        float r = gh * 0.5f;
        dl.addRectFilled(gx, gy, gx + gw, gy + gh, HudChrome.scaleAlpha(452984831, a), r);
        if (stack.method_7960() || left < 0.0f) {
            return;
        }
        float fill = gw * Math.max(0.0f, Math.min(1.0f, left));
        if (fill < 0.5f) {
            return;
        }
        dl.addRectFilled(gx, gy, gx + fill, gy + gh, HudGlyphs.ink(HudGlyphs.vitality(left), a * criticalPulse(left)), Math.min(r, fill * 0.5f));
    }

    private float criticalPulse(float left) {
        if (left >= CRITICAL) {
            return 1.0f;
        }
        return 0.6f + (0.4f * (0.5f + (0.5f * ((float) Math.sin(this.clock * 6.5f)))));
    }

    private static String percentText(float left) {
        int value = Math.max(0, Math.min(100, Math.round(left * 100.0f)));
        String cached = PERCENT[value];
        if (cached == null) {
            cached = value + "%";
            PERCENT[value] = cached;
        }
        return cached;
    }

    private static String countText(int count) {
        if (count >= COUNT.length) {
            return Integer.toString(count);
        }
        String cached = COUNT[count];
        if (cached == null) {
            cached = Integer.toString(count);
            COUNT[count] = cached;
        }
        return cached;
    }
}
