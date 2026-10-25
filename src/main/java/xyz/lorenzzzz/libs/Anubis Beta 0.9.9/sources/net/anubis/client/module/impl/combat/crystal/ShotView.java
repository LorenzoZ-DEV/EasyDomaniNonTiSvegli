package net.anubis.client.module.impl.combat.crystal;

import imgui.ImDrawList;
import java.util.Locale;
import net.anubis.client.gui.WorldLabel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_310;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/crystal/ShotView.class */
@Environment(EnvType.CLIENT)
public final class ShotView {
    private static final long SLIDE_MS = 120;
    private static final long HOLD_MS = 150;
    private static final long FADE_MS = 350;
    private static final float FILL_ALPHA = 0.22f;
    private static final float LINE_WIDTH = 1.5f;
    private static final double LABEL_RISE = 0.25d;
    private class_238 from;
    private class_238 to;
    private long movedAt;
    private long shownAt;
    private int color;
    private float damage;
    private float selfDamage;

    public void show(class_238 box, int color, float damage, float selfDamage) {
        long now = System.currentTimeMillis();
        if (!showing(now)) {
            this.from = box;
            this.movedAt = now;
        } else if (!box.equals(this.to)) {
            this.from = box(now);
            this.movedAt = now;
        }
        this.to = box;
        this.shownAt = now;
        this.color = color;
        this.damage = damage;
        this.selfDamage = selfDamage;
    }

    public void clear() {
        this.to = null;
    }

    public boolean showing() {
        return showing(System.currentTimeMillis());
    }

    public void draw() {
        long now = System.currentTimeMillis();
        if (showing(now)) {
            float alpha = alpha(now);
            class_12180.method_75541(box(now), class_12179.method_75537(fade(this.color, alpha), LINE_WIDTH, fade(this.color, alpha * FILL_ALPHA))).method_75533();
        }
    }

    public void label(ImDrawList dl, float screenW, float screenH) {
        long now = System.currentTimeMillis();
        if (showing(now)) {
            class_238 box = box(now);
            class_243 top = new class_243(box.method_1005().field_1352, box.field_1325 + LABEL_RISE, box.method_1005().field_1350);
            float[] screen = WorldLabel.project(class_310.method_1551(), top, screenW, screenH);
            if (screen == null) {
                return;
            }
            WorldLabel.drawText(dl, screen[0], screen[1], String.format(Locale.ROOT, "%.1f", Float.valueOf(this.damage)), String.format(Locale.ROOT, "self %.1f", Float.valueOf(this.selfDamage)), alpha(now));
        }
    }

    private boolean showing(long now) {
        return this.to != null && alpha(now) > 0.0f;
    }

    private float alpha(long now) {
        long idle = (now - this.shownAt) - HOLD_MS;
        if (idle <= 0) {
            return 1.0f;
        }
        return Math.max(0.0f, 1.0f - (idle / 350.0f));
    }

    private class_238 box(long now) {
        float t = Math.min(1.0f, (now - this.movedAt) / 120.0f);
        float eased = 1.0f - (((1.0f - t) * (1.0f - t)) * (1.0f - t));
        return new class_238(this.from.method_61125().method_35590(this.to.method_61125(), eased), this.from.method_61126().method_35590(this.to.method_61126(), eased));
    }

    private static int fade(int argb, float alpha) {
        return (Math.round((argb >>> 24) * alpha) << 24) | (argb & 16777215);
    }
}
