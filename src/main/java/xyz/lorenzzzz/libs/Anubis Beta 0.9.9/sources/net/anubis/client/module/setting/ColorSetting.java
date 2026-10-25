package net.anubis.client.module.setting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/setting/ColorSetting.class */
@Environment(EnvType.CLIENT)
public final class ColorSetting extends Setting {
    private final float[] value;
    private int packed;
    private int packedR;
    private int packedG;
    private int packedB;
    private int packedA;

    public ColorSetting(String name, int argb) {
        super(name);
        this.value = new float[]{((argb >> 16) & 255) / 255.0f, ((argb >> 8) & 255) / 255.0f, (argb & 255) / 255.0f, ((argb >>> 24) & 255) / 255.0f};
    }

    public float[] value() {
        return this.value;
    }

    public void setArgb(int argb) {
        if (argb() == argb) {
            return;
        }
        setFromArgb(argb);
        notifyChanged();
    }

    public int argb() {
        int r = Float.floatToRawIntBits(this.value[0]);
        int g = Float.floatToRawIntBits(this.value[1]);
        int b = Float.floatToRawIntBits(this.value[2]);
        int a = Float.floatToRawIntBits(this.value[3]);
        if (r != this.packedR || g != this.packedG || b != this.packedB || a != this.packedA) {
            this.packed = pack();
            this.packedR = r;
            this.packedG = g;
            this.packedB = b;
            this.packedA = a;
        }
        return this.packed;
    }

    public int rgb() {
        return argb() & 16777215;
    }

    public int argbWithAlpha(int alpha) {
        return (clamp255(alpha) << 24) | (argb() & 16777215);
    }

    private int pack() {
        return pack(this.value[0], this.value[1], this.value[2], this.value[3]);
    }

    private static int pack(float red, float green, float blue, float alpha) {
        int a = Math.round(clamp01(alpha) * 255.0f);
        int r = Math.round(clamp01(red) * 255.0f);
        int g = Math.round(clamp01(green) * 255.0f);
        int b = Math.round(clamp01(blue) * 255.0f);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /* JADX WARN: Code duplicated, block: B:16:0x005d A[Catch: RuntimeException -> 0x0094, TryCatch #0 {RuntimeException -> 0x0094, blocks: (B:4:0x0004, B:6:0x000b, B:10:0x003e, B:12:0x0045, B:14:0x0050, B:16:0x005d, B:18:0x0069), top: B:24:0x0004 }] */
    /* JADX WARN: Code duplicated, block: B:17:0x0068  */
    @Override // net.anubis.client.module.setting.Setting
    public JsonElement compact(JsonElement element) {
        JsonArray array;
        float asFloat;
        if (element != null) {
            try {
                if (element.isJsonObject()) {
                    JsonObject object = element.getAsJsonObject();
                    return new JsonPrimitive(hex(pack(readFloat(object, "r", 0.0f), readFloat(object, "g", 0.0f), readFloat(object, "b", 0.0f), readFloat(object, "a", 1.0f))));
                }
                if (element != null && element.isJsonArray() && element.getAsJsonArray().size() >= 3) {
                    array = element.getAsJsonArray();
                    if (array.size() > 3) {
                        asFloat = array.get(3).getAsFloat();
                    } else {
                        asFloat = 1.0f;
                    }
                    float alpha = asFloat;
                    return new JsonPrimitive(hex(pack(array.get(0).getAsFloat(), array.get(1).getAsFloat(), array.get(2).getAsFloat(), alpha)));
                }
            } catch (RuntimeException e) {
            }
        } else if (element != null) {
            array = element.getAsJsonArray();
            if (array.size() > 3) {
                asFloat = array.get(3).getAsFloat();
            } else {
                asFloat = 1.0f;
            }
            float alpha2 = asFloat;
            return new JsonPrimitive(hex(pack(array.get(0).getAsFloat(), array.get(1).getAsFloat(), array.get(2).getAsFloat(), alpha2)));
        }
        return element;
    }

    public static String hex(int argb) {
        return String.format("%08X", Integer.valueOf(argb));
    }

    @Override // net.anubis.client.module.setting.Setting
    public JsonElement serialize() {
        JsonObject object = new JsonObject();
        object.addProperty("r", Float.valueOf(clamp01(this.value[0])));
        object.addProperty("g", Float.valueOf(clamp01(this.value[1])));
        object.addProperty("b", Float.valueOf(clamp01(this.value[2])));
        object.addProperty("a", Float.valueOf(clamp01(this.value[3])));
        return object;
    }

    @Override // net.anubis.client.module.setting.Setting
    protected void deserializeValue(JsonElement element) {
        if (element == null) {
            return;
        }
        try {
            if (element.isJsonObject()) {
                JsonObject object = element.getAsJsonObject();
                this.value[0] = readFloat(object, "r", this.value[0]);
                this.value[1] = readFloat(object, "g", this.value[1]);
                this.value[2] = readFloat(object, "b", this.value[2]);
                this.value[3] = readFloat(object, "a", this.value[3]);
                clampValues();
                return;
            }
            if (element.isJsonArray()) {
                JsonArray array = element.getAsJsonArray();
                for (int i = 0; i < Math.min(4, array.size()); i++) {
                    this.value[i] = clamp01(array.get(i).getAsFloat());
                }
                return;
            }
            if (element.isJsonPrimitive()) {
                JsonPrimitive primitive = element.getAsJsonPrimitive();
                if (primitive.isString()) {
                    setFromHex(primitive.getAsString());
                } else if (primitive.isNumber()) {
                    setFromArgb(primitive.getAsInt());
                }
            }
        } catch (RuntimeException e) {
        }
    }

    private void setFromHex(String raw) {
        String hex = raw.trim();
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        if (hex.length() == 6) {
            hex = "FF" + hex;
        }
        if (hex.length() != 8) {
            return;
        }
        setFromArgb((int) Long.parseLong(hex, 16));
    }

    private void setFromArgb(int argb) {
        this.value[0] = ((argb >> 16) & 255) / 255.0f;
        this.value[1] = ((argb >> 8) & 255) / 255.0f;
        this.value[2] = (argb & 255) / 255.0f;
        this.value[3] = ((argb >>> 24) & 255) / 255.0f;
    }

    private void clampValues() {
        for (int i = 0; i < this.value.length; i++) {
            this.value[i] = clamp01(this.value[i]);
        }
    }

    private static float readFloat(JsonObject object, String name, float fallback) {
        if (!object.has(name)) {
            return fallback;
        }
        return clamp01(object.get(name).getAsFloat());
    }

    private static float clamp01(float v) {
        if (Float.isNaN(v) || Float.isInfinite(v)) {
            return 1.0f;
        }
        return Math.max(0.0f, Math.min(1.0f, v));
    }

    private static int clamp255(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
