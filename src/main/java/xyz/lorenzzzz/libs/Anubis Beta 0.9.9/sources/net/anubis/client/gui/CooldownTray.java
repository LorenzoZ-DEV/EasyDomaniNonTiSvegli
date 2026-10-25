package net.anubis.client.gui;

import com.example.client.mixin.AccessorHudCooldownInstance;
import com.example.client.mixin.AccessorHudItemCooldowns;
import imgui.ImDrawList;
import imgui.ImFont;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.anubis.client.config.Surface;
import net.anubis.client.imgui.ImGuiManager;
import net.anubis.client.module.impl.client.HudModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1661;
import net.minecraft.class_1792;
import net.minecraft.class_1796;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_746;
import net.minecraft.class_7923;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/CooldownTray.class */
@Environment(EnvType.CLIENT)
final class CooldownTray {
    private static final float PAD = 6.0f;
    private static final float TILE_W = 32.0f;
    private static final float TILE_GAP = 5.0f;
    private static final float DIAL = 26.0f;
    private static final float DIAL_ICON = 16.0f;
    private static final float DIAL_STROKE = 2.0f;
    private static final float BAR_ICON = 18.0f;
    private static final float BAR_H = 3.0f;
    private static final float BAR_GAP = 3.0f;
    private static final float TEXT_GAP = 2.0f;
    private static final float APPEAR_TIME = 0.18f;
    private static final float READY_TIME = 0.32f;
    private static final float VANISH_TIME = 0.22f;
    private static final float LOOKUP_INTERVAL = 0.5f;
    private static final float TICKS_PER_SECOND = 20.0f;
    private static final float TAU = 6.2831855f;
    private static final float TOP = -1.5707964f;
    private static final String[] TENTHS = new String[100];
    private static final String[] SECONDS = new String[100];
    private static final String[] MINUTES = new String[100];
    private class_746 owner;
    private boolean sampling;
    private boolean sandbar;
    private float clock;
    private final List<Slot> slots = new ArrayList();
    private final List<Slot> pool = new ArrayList();
    private final Slot[] samples = {new Slot(), new Slot()};
    private final List<Slot> sampleView = List.of((Object[]) this.samples);
    private float tickRate = 20.0f;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/CooldownTray$Slot.class */
    @Environment(EnvType.CLIENT)
    private static final class Slot {
        class_2960 group;
        class_1792 item;
        float lookupAt;
        float remaining;
        float total;
        float presence;
        float ready;
        boolean live;
        boolean done;

        private Slot() {
        }
    }

    CooldownTray() {
        this.samples[0].item = class_1802.field_8634;
        this.samples[0].total = 20.0f;
        this.samples[1].item = class_1802.field_49098;
        this.samples[1].total = 10.0f;
        for (Slot sample : this.samples) {
            sample.presence = 1.0f;
        }
    }

    void reset() {
        for (Slot slot : this.slots) {
            slot.group = null;
            slot.item = null;
            this.pool.add(slot);
        }
        this.slots.clear();
        this.owner = null;
    }

    boolean visible() {
        return this.sampling || !this.slots.isEmpty();
    }

    void update(class_310 mc, HudModule hud, boolean active, boolean editMode, float dt) {
        this.clock += dt;
        this.sandbar = hud.cooldownSandbar();
        float rate = mc.field_1687 == null ? 20.0f : mc.field_1687.method_54719().method_54748();
        this.tickRate = rate > 0.0f ? rate : 20.0f;
        class_746 player = mc.field_1724;
        if (player != this.owner) {
            reset();
            this.owner = player;
        }
        Iterator<Slot> it = this.slots.iterator();
        while (it.hasNext()) {
            it.next().live = false;
        }
        if (active && player != null) {
            collect(mc, player);
        }
        for (int i = this.slots.size() - 1; i >= 0; i--) {
            Slot slot = this.slots.get(i);
            if (slot.live) {
                slot.presence = Math.min(1.0f, slot.presence + (dt / APPEAR_TIME));
                if (slot.item == null && this.clock >= slot.lookupAt && player != null) {
                    slot.item = resolve(player, slot.group);
                    slot.lookupAt = this.clock + 0.5f;
                }
            } else {
                if (!slot.done) {
                    slot.done = true;
                    slot.remaining = 0.0f;
                    slot.ready = active ? 1.0f : 0.0f;
                }
                if (slot.ready > 0.0f) {
                    slot.ready = Math.max(0.0f, slot.ready - (dt / READY_TIME));
                } else {
                    slot.presence -= dt / VANISH_TIME;
                    if (slot.presence <= 0.0f) {
                        this.slots.remove(i);
                        slot.group = null;
                        slot.item = null;
                        this.pool.add(slot);
                    }
                }
            }
        }
        this.sampling = editMode && this.slots.isEmpty();
        if (this.sampling) {
            for (int i2 = 0; i2 < this.samples.length; i2++) {
                float phase = ((this.clock * 0.45f) + (i2 * 0.43f)) % 1.0f;
                this.samples[i2].remaining = (1.0f - phase) * this.samples[i2].total;
            }
        }
    }

    private void collect(class_310 mc, class_746 player) {
        AccessorHudItemCooldowns access = player.method_7357();
        int tick = access.anubis$hudTickCount();
        float partial = mc.method_61966().method_60637(true);
        for (Map.Entry<class_2960, ?> entry : access.anubis$hudCooldowns().entrySet()) {
            AccessorHudCooldownInstance span = (AccessorHudCooldownInstance) entry.getValue();
            int start = span.anubis$hudStartTime();
            int end = span.anubis$hudEndTime();
            float left = end - (tick + partial);
            if (left > 0.0f) {
                Slot slot = find(entry.getKey());
                if (slot == null) {
                    slot = open(entry.getKey());
                }
                slot.live = true;
                slot.done = false;
                slot.ready = 0.0f;
                slot.remaining = left;
                slot.total = Math.max(1.0f, end - start);
            }
        }
    }

    private Slot find(class_2960 group) {
        for (Slot slot : this.slots) {
            if (slot.group.equals(group)) {
                return slot;
            }
        }
        return null;
    }

    private Slot open(class_2960 group) {
        Slot slot = this.pool.isEmpty() ? new Slot() : this.pool.remove(this.pool.size() - 1);
        slot.group = group;
        slot.item = null;
        slot.lookupAt = 0.0f;
        slot.presence = 0.0f;
        slot.ready = 0.0f;
        slot.done = false;
        this.slots.add(slot);
        return slot;
    }

    private static class_1792 resolve(class_746 player, class_2960 group) {
        class_1661 inventory = player.method_31548();
        class_1796 cooldowns = player.method_7357();
        for (int i = 0; i < inventory.method_5439(); i++) {
            class_1799 stack = inventory.method_5438(i);
            if (!stack.method_7960() && group.equals(cooldowns.method_62836(stack))) {
                return stack.method_7909();
            }
        }
        if (class_7923.field_41178.method_10250(group)) {
            return (class_1792) class_7923.field_41178.method_63535(group);
        }
        return null;
    }

    float planWidth(float s) {
        if (!visible()) {
            return 1.0f;
        }
        float spread = 0.0f;
        List<Slot> shown = this.sampling ? this.sampleView : this.slots;
        for (Slot slot : shown) {
            spread += HudGlyphs.easeOutQuint(slot.presence) * 37.0f * s;
        }
        return Math.max(1.0f, Math.round((PAD * s * 2.0f) + Math.max(0.0f, spread - (5.0f * s))));
    }

    float planHeight(float s) {
        float body = this.sandbar ? ItemIcons.pixels(18.0f * s) + (3.0f * s) + barHeight(s) : Math.round(DIAL * s);
        return Math.round((PAD * s * 2.0f) + body + (2.0f * s) + HudChrome.px(14.0f, s));
    }

    private static float barHeight(float s) {
        return Math.max(2.0f, Math.round(3.0f * s));
    }

    void draw(ImDrawList dl, HudLayoutStore.HudElementState state, float x, float y, float alpha, boolean shadow, int accent) {
        if (alpha <= 0.004f || !visible()) {
            return;
        }
        List<Slot> shown = this.sampling ? this.sampleView : this.slots;
        float total = 0.0f;
        Iterator<Slot> it = shown.iterator();
        while (it.hasNext()) {
            total += it.next().presence;
        }
        float cardAlpha = alpha * HudGlyphs.easeOutQuint(Math.min(1.0f, total));
        if (cardAlpha <= 0.004f) {
            return;
        }
        float s = HudRenderer.scaleOf(state);
        float w = state.width;
        float h = state.height;
        Surface surface = HudRenderer.hudSurface(state);
        boolean guard = HudChrome.needsTextGuard(surface);
        HudChrome.card(dl, surface, x, y, w, h, HudRenderer.hudRounding() * s, cardAlpha, shadow, s);
        ImFont label = ImGuiManager.getInstance().hudLabelFont();
        int labelPx = HudChrome.px(14.0f, s);
        float pad = Math.round(PAD * s);
        float tile = 32.0f * s;
        float dial = this.sandbar ? ItemIcons.pixels(18.0f * s) + (3.0f * s) + barHeight(s) : Math.round(DIAL * s);
        float textTop = Math.round(y + pad + dial + (2.0f * s));
        float bleed = 10.0f * s;
        dl.pushClipRect(x, y - bleed, x + w, y + h + bleed, true);
        float cx = x + pad;
        for (Slot slot : shown) {
            float open = HudGlyphs.easeOutQuint(slot.presence);
            float a = alpha * open;
            if (a > 0.004f) {
                if (this.sandbar) {
                    drawSandbar(dl, slot, Math.round(cx), y + pad, tile, s, a, accent);
                } else {
                    drawHalo(dl, slot, cx + (tile * 0.5f), y + pad + (Math.round(DIAL * s) * 0.5f), s, a, accent);
                }
                drawCaption(dl, slot, cx + (tile * 0.5f), textTop, label, labelPx, s, a, guard);
            }
            cx += open * (tile + (5.0f * s));
        }
        dl.popClipRect();
    }

    private void drawHalo(ImDrawList dl, Slot slot, float cx, float cy, float s, float a, int accent) {
        float r = (Math.round(DIAL * s) * 0.5f) - ((2.0f * s) * 0.5f);
        float stroke = Math.max(1.5f, 2.0f * s);
        float left = fraction(slot);
        dl.addCircle(cx, cy, r, HudChrome.scaleAlpha(452984831, a), 0, stroke);
        if (left > 0.0f) {
            int tone = EgyptDraw.mix(accent, GuiPalette.abgr(EgyptDraw.GOLD), 1.0f - left);
            HudGlyphs.arc(dl, cx, cy, r, TOP, TAU * left, stroke, HudChrome.scaleAlpha(tone, a));
        }
        int icon = (int) ItemIcons.pixels(16.0f * s);
        drawItem(dl, slot, cx - (icon * 0.5f), cy - (icon * 0.5f), icon, s, a * (0.62f + (0.38f * (1.0f - left))));
        if (slot.ready > 0.0f) {
            float grow = (1.0f - slot.ready) * 7.0f * s;
            dl.addCircle(cx, cy, r + grow, HudGlyphs.ink(EgyptDraw.GOLD, a * slot.ready * 0.9f), 0, stroke);
        }
    }

    private void drawSandbar(ImDrawList dl, Slot slot, float tx, float ty, float tile, float s, float a, int accent) {
        int icon = (int) ItemIcons.pixels(18.0f * s);
        float left = fraction(slot);
        float ix = Math.round(tx + ((tile - icon) * 0.5f));
        drawItem(dl, slot, ix, ty, icon, s, a * (0.62f + (0.38f * (1.0f - left))));
        float by = ty + icon + (3.0f * s);
        float bh = barHeight(s);
        float inset = Math.round(3.0f * s);
        float bl = tx + inset;
        float br = (tx + tile) - inset;
        dl.addRectFilled(bl, by, br, by + bh, HudChrome.scaleAlpha(452984831, a), bh * 0.5f);
        if (left > 0.0f) {
            int tone = EgyptDraw.mix(accent, GuiPalette.abgr(EgyptDraw.GOLD), 1.0f - left);
            float fill = (br - bl) * left;
            dl.addRectFilled(bl, by, bl + fill, by + bh, HudChrome.scaleAlpha(tone, a), Math.min(bh * 0.5f, fill * 0.5f));
        }
        if (slot.ready > 0.0f) {
            float glow = slot.ready;
            dl.addRectFilled(bl, by, br, by + bh, HudGlyphs.ink(EgyptDraw.GOLD_LIGHT, a * glow), bh * 0.5f);
        }
    }

    private static void drawItem(ImDrawList dl, Slot slot, float x, float y, int icon, float s, float a) {
        if (slot.item != null) {
            ItemIcons.draw(dl, slot.item, x, y, icon, a);
        } else {
            HudGlyphs.hourglass(dl, x + (icon * 0.5f), y + (icon * 0.5f), icon * 0.8f, Math.max(1.0f, 1.3f * s), HudGlyphs.ink(EgyptDraw.SAND, a));
        }
    }

    private void drawCaption(ImDrawList dl, Slot slot, float centerX, float top, ImFont label, int labelPx, float s, float a, boolean guard) {
        if (slot.done) {
            float h = labelPx * 0.85f;
            HudGlyphs.ankh(dl, centerX, top + (labelPx * 0.5f), h, Math.max(1.0f, 1.2f * s), HudGlyphs.ink(EgyptDraw.GOLD, a * Math.max(slot.ready, 0.4f)));
        } else {
            String value = seconds(slot.remaining / this.tickRate);
            float vw = HudChrome.tabularWidth(label, labelPx, value);
            HudChrome.tabular(dl, label, labelPx, Math.round(centerX - (vw * 0.5f)), top, HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, a), value, guard);
        }
    }

    private static float fraction(Slot slot) {
        if (slot.total <= 0.0f) {
            return 0.0f;
        }
        return Math.max(0.0f, Math.min(1.0f, slot.remaining / slot.total));
    }

    private static String seconds(float seconds) {
        if (seconds < 9.95f) {
            int tenths = Math.max(1, Math.min(99, (int) Math.ceil(seconds * 10.0f)));
            String cached = TENTHS[tenths];
            if (cached == null) {
                cached = (tenths / 10) + "." + (tenths % 10) + "s";
                TENTHS[tenths] = cached;
            }
            return cached;
        }
        int whole = (int) Math.ceil(seconds);
        if (whole < 100) {
            String cached2 = SECONDS[whole];
            if (cached2 == null) {
                cached2 = whole + "s";
                SECONDS[whole] = cached2;
            }
            return cached2;
        }
        int minutes = Math.min(99, (int) Math.ceil(seconds / 60.0f));
        String cached3 = MINUTES[minutes];
        if (cached3 == null) {
            cached3 = minutes + "m";
            MINUTES[minutes] = cached3;
        }
        return cached3;
    }
}
