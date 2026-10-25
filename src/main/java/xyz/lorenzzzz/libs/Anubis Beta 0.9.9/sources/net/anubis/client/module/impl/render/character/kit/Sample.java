package net.anubis.client.module.impl.render.character.kit;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Sample.class */
@Environment(EnvType.CLIENT)
public final class Sample {
    public float r;
    public float g;
    public float b;
    public float glow;
    public float flat;
    public float flatWeight;
    public float occlusion;
    public float occlusionWeight;

    public void set(int argb) {
        this.r = ((argb >> 16) & 255) / 255.0f;
        this.g = ((argb >> 8) & 255) / 255.0f;
        this.b = (argb & 255) / 255.0f;
        this.glow = 0.0f;
        this.flat = 0.0f;
        this.flatWeight = 0.0f;
        this.occlusion = 0.0f;
        this.occlusionWeight = 0.0f;
    }

    public void set(Sample o) {
        this.r = o.r;
        this.g = o.g;
        this.b = o.b;
        this.glow = o.glow;
        this.flat = o.flat;
        this.flatWeight = o.flatWeight;
        this.occlusion = o.occlusion;
        this.occlusionWeight = o.occlusionWeight;
    }

    public void mix(Sample o, float t) {
        float s = 1.0f - t;
        this.r = (this.r * s) + (o.r * t);
        this.g = (this.g * s) + (o.g * t);
        this.b = (this.b * s) + (o.b * t);
        this.glow = (this.glow * s) + (o.glow * t);
        this.flat = (this.flat * s) + (o.flat * t);
        this.flatWeight = (this.flatWeight * s) + (o.flatWeight * t);
        this.occlusion = (this.occlusion * s) + (o.occlusion * t);
        this.occlusionWeight = (this.occlusionWeight * s) + (o.occlusionWeight * t);
    }

    public void toward(int argb, float t) {
        float s = 1.0f - t;
        this.r = (this.r * s) + ((((argb >> 16) & 255) / 255.0f) * t);
        this.g = (this.g * s) + ((((argb >> 8) & 255) / 255.0f) * t);
        this.b = (this.b * s) + (((argb & 255) / 255.0f) * t);
    }

    public int argb() {
        return (-16777216) | (channel(this.r) << 16) | (channel(this.g) << 8) | channel(this.b);
    }

    float flat(float fallback) {
        return blend(fallback, this.flat, this.flatWeight);
    }

    float occlusion(float fallback) {
        return blend(fallback, this.occlusion, this.occlusionWeight);
    }

    private static float blend(float fallback, float weighted, float weight) {
        if (weight <= 1.0E-4f) {
            return fallback;
        }
        float w = Math.min(1.0f, weight);
        return Math.max(0.0f, Math.min(1.0f, (fallback * (1.0f - w)) + (weighted * (w / weight))));
    }

    static int channel(float v) {
        int c = Math.round(v * 255.0f);
        if (c < 0) {
            return 0;
        }
        return Math.min(c, 255);
    }
}
