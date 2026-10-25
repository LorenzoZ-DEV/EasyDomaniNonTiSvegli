package net.anubis.client.util.glyph;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_3532;
import net.minecraft.class_9848;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/glyph/GlyphTint.class */
@Environment(EnvType.CLIENT)
public final class GlyphTint {
    public static final String THEME = "Theme";
    public static final String MANUAL = "Manual";
    private static final float WARM = 0.16666667f;
    private static final float COOL = 0.6666667f;
    private static final float COMPANION_TURN = -0.09f;
    private int primary;
    private int secondary;
    private int highlight;
    private int companion;
    private int dust;
    private int flash;
    private float hue;
    private float saturation;
    private float value;

    public GlyphTint resolve(String mode, int accent, int manualMain, int manualSecond) {
        boolean manual = MANUAL.equals(mode);
        this.primary = class_9848.method_61334(manual ? manualMain : accent);
        split(this.primary);
        if (manual) {
            this.secondary = class_9848.method_61334(manualSecond);
            this.companion = mix(this.primary, this.secondary, 0.5f);
        } else {
            this.secondary = hsv(toward(this.hue, COOL, 0.025f), this.saturation * 1.05f, this.value * 0.66f);
            this.companion = hsv(this.hue + COMPANION_TURN, this.saturation * 0.9f, this.value * 0.94f);
        }
        this.highlight = hsv(toward(this.hue, WARM, 0.03f), this.saturation * 0.5f, this.value + ((1.0f - this.value) * 0.85f));
        this.dust = hsv(toward(this.hue, WARM, 0.02f), this.saturation * 0.34f, 0.5f + (this.value * 0.4f));
        this.flash = mix(this.highlight, -1, 0.6f);
        return this;
    }

    private void split(int rgb) {
        int r = class_9848.method_61327(rgb);
        int g = class_9848.method_61329(rgb);
        int b = class_9848.method_61331(rgb);
        int max = Math.max(r, Math.max(g, b));
        int min = Math.min(r, Math.min(g, b));
        float range = max - min;
        this.value = max / 255.0f;
        this.saturation = max == 0 ? 0.0f : range / max;
        if (range == 0.0f) {
            this.hue = 0.0f;
            return;
        }
        if (max == r) {
            this.hue = class_3532.method_22450(((g - b) / range) / 6.0f);
        } else if (max == g) {
            this.hue = (2.0f + ((b - r) / range)) / 6.0f;
        } else {
            this.hue = (4.0f + ((r - g) / range)) / 6.0f;
        }
    }

    private static float toward(float from, float target, float step) {
        float delta = target - from;
        float delta2 = delta - class_3532.method_15375(delta + 0.5f);
        return Math.abs(delta2) <= step ? target : from + Math.copySign(step, delta2);
    }

    private static int hsv(float h, float s, float v) {
        float wrapped = class_3532.method_22450(h);
        if (wrapped >= 1.0f) {
            wrapped = 0.0f;
        }
        return class_3532.method_60599(wrapped, class_3532.method_15363(s, 0.0f, 1.0f), class_3532.method_15363(v, 0.0f, 1.0f), 255);
    }

    public int primary() {
        return this.primary;
    }

    public int secondary() {
        return this.secondary;
    }

    public int highlight() {
        return this.highlight;
    }

    public int companion() {
        return this.companion;
    }

    public int dust() {
        return this.dust;
    }

    public int flash() {
        return this.flash;
    }

    public static int alpha(int rgb, float alpha) {
        float clamped = alpha <= 0.0f ? 0.0f : Math.min(1.0f, alpha);
        return (((int) ((clamped * 255.0f) + 0.5f)) << 24) | (rgb & 16777215);
    }

    public static int mix(int from, int to, float k) {
        return class_9848.method_61319(Math.max(0.0f, Math.min(1.0f, k)), from, to);
    }

    public static int lift(int rgb, float amount) {
        return mix(class_9848.method_61334(rgb), -1, amount);
    }
}
