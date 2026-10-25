package net.anubis.client.module.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.math.BigDecimal;
import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/setting/NumberSetting.class */
@Environment(EnvType.CLIENT)
public final class NumberSetting extends Setting {
    private static final int MAX_DECIMALS = 4;
    private static final double[] POW10 = {1.0d, 10.0d, 100.0d, 1000.0d, 10000.0d};
    private static final String[] FORMATS = {"%.0f", "%.1f", "%.2f", "%.3f", "%.4f"};
    private final float[] value;
    private final float min;
    private final float max;
    private float step;
    private int stepDecimals;
    private double gridMin;
    private double gridStep;

    public NumberSetting(String name, float value, float min, float max) {
        super(name);
        this.value = new float[]{value};
        this.min = min;
        this.max = max;
    }

    public NumberSetting step(float step) {
        if (step < 0.0f || Float.isInfinite(step)) {
            throw new IllegalArgumentException("Invalid step " + step + " for setting '" + name() + "'");
        }
        this.step = step;
        this.stepDecimals = step > 0.0f ? Math.max(decimalsOf(step), decimalsOf(this.min)) : 0;
        this.gridMin = toDecimals(this.min);
        double written = toDecimals(step);
        this.gridStep = written > 0.0d ? written : step;
        set(this.value[0]);
        return this;
    }

    public float step() {
        return this.step;
    }

    public float get() {
        return this.value[0];
    }

    public int getInt() {
        return Math.round(this.value[0]);
    }

    public void set(float newValue) {
        float snapped = snap(newValue);
        if (this.value[0] == snapped) {
            return;
        }
        this.value[0] = snapped;
        notifyChanged();
    }

    public float snap(float v) {
        float clamped = clamp(v);
        if (this.step <= 0.0f) {
            return clamped;
        }
        long k = Math.round(((((double) clamped) - this.gridMin) / this.gridStep) + 1.0E-9d);
        float snapped = onGrid(k);
        if (snapped > this.max) {
            snapped = onGrid(k - 1);
        }
        return Math.max(this.min, Math.min(this.max, snapped));
    }

    public int decimals() {
        if (this.step > 0.0f) {
            return this.stepDecimals;
        }
        float range = this.max - this.min;
        if (this.min == Math.rint(this.min) && this.max == Math.rint(this.max) && range >= 20.0f) {
            return 0;
        }
        return range <= 2.0f ? 2 : 1;
    }

    public String formatted() {
        int decimals = decimals();
        return decimals == 0 ? Integer.toString(Math.round(this.value[0])) : String.format(Locale.ROOT, FORMATS[decimals], Float.valueOf(this.value[0]));
    }

    public float[] value() {
        return this.value;
    }

    public float min() {
        return this.min;
    }

    public float max() {
        return this.max;
    }

    private float clamp(float v) {
        if (Float.isNaN(v)) {
            return this.value[0];
        }
        if (v < this.min) {
            return this.min;
        }
        return v > this.max ? this.max : v;
    }

    private float onGrid(long k) {
        return (float) toDecimals(this.gridMin + (k * this.gridStep));
    }

    private double toDecimals(double v) {
        double scale = POW10[this.stepDecimals];
        return Math.round(v * scale) / scale;
    }

    private static int decimalsOf(float v) {
        if (!Float.isFinite(v)) {
            return 0;
        }
        int scale = new BigDecimal(Float.toString(v)).stripTrailingZeros().scale();
        return Math.max(0, Math.min(4, scale));
    }

    @Override // net.anubis.client.module.setting.Setting
    public JsonElement serialize() {
        return new JsonPrimitive(Float.valueOf(this.value[0]));
    }

    @Override // net.anubis.client.module.setting.Setting
    protected void deserializeValue(JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            return;
        }
        try {
            this.value[0] = snap(element.getAsFloat());
        } catch (RuntimeException e) {
        }
    }
}
