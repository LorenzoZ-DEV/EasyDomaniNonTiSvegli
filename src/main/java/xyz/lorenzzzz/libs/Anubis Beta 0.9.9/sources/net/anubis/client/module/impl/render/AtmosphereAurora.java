package net.anubis.client.module.impl.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_287;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_9848;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/AtmosphereAurora.class */
@Environment(EnvType.CLIENT)
final class AtmosphereAurora {
    private static final int COLUMNS = 64;
    private static final int ROWS = 6;
    private final SkyMesh mesh = new SkyMesh("Anubis aurora", SkyMesh.GLOW, (CURTAINS.length * 64) * 6);
    private final float[] x = new float[455];
    private final float[] y = new float[this.x.length];
    private final float[] z = new float[this.x.length];
    private final int[] color = new int[this.x.length];
    private static final float RADIUS = 100.0f;
    private static final float[][] CURTAINS = {new float[]{-40.0f, 22.0f, 78.0f, 18.0f}, new float[]{6.0f, 28.0f, RADIUS, 23.0f}, new float[]{54.0f, 20.0f, 70.0f, 16.0f}};

    AtmosphereAurora() {
    }

    void render(class_4587 pose, float intensity, float time, int low, int high) {
        class_287 builder = this.mesh.begin();
        for (int i = 0; i < CURTAINS.length; i++) {
            curtain(builder, CURTAINS[i], i, time, low, high);
        }
        this.mesh.upload(builder);
        this.mesh.draw(pose, intensity);
    }

    private void curtain(class_287 builder, float[] shape, int seed, float time, int low, int high) {
        float azimuth = shape[0];
        float elevation = shape[1];
        float width = shape[2];
        float height = shape[3];
        for (int c = 0; c <= 64; c++) {
            float t = c / 64.0f;
            float az = azimuth + ((t - 0.5f) * width) + (3.0f * wave((t * 2.1f) + (time * 0.05f) + seed));
            float base = elevation + (5.0f * class_3532.method_15374(3.1415927f * t)) + (3.0f * wave(((t * 3.7f) - (time * 0.07f)) + (seed * 2.0f)));
            float ends = smoothstep(t / 0.25f) * smoothstep((1.0f - t) / 0.25f);
            float glow = ends * (0.55f + (0.45f * wave01((t * 5.3f) + (time * 0.11f) + (seed * 1.3f))));
            float rays = 0.72f + (0.28f * wave01((t * 23.0f) + (time * 0.35f) + seed));
            float top = height * (0.8f + (0.35f * wave01((t * 4.1f) + (time * 0.06f) + (seed * 3.0f))));
            float cosAz = class_3532.method_15362(az * 0.017453292f);
            float sinAz = class_3532.method_15374(az * 0.017453292f);
            for (int r = 0; r <= 6; r++) {
                float v = r / 6.0f;
                float el = (base + (v * top)) * 0.017453292f;
                float cosEl = class_3532.method_15362(el);
                int i = (c * 7) + r;
                this.x[i] = RADIUS * cosEl * sinAz;
                this.y[i] = RADIUS * class_3532.method_15374(el);
                this.z[i] = (-100.0f) * cosEl * cosAz;
                float profile = v < 0.12f ? smoothstep(v / 0.12f) : (float) Math.pow(1.0f - ((v - 0.12f) / 0.88f), 1.5d);
                float alpha = class_3532.method_15363(glow * rays * profile * 0.8f, 0.0f, 1.0f);
                int rgb = class_9848.method_61319((float) Math.pow(v, 0.7d), low, high) & 16777215;
                this.color[i] = (Math.round(alpha * 255.0f) << 24) | rgb;
            }
        }
        for (int c2 = 0; c2 < 64; c2++) {
            for (int r2 = 0; r2 < 6; r2++) {
                int a = (c2 * 7) + r2;
                int b = ((c2 + 1) * 7) + r2;
                vertex(builder, a);
                vertex(builder, b);
                vertex(builder, b + 1);
                vertex(builder, a + 1);
            }
        }
    }

    private void vertex(class_287 builder, int i) {
        SkyMesh.vertex(builder, this.x[i], this.y[i], this.z[i], this.color[i]);
    }

    static float wave(float s) {
        return (0.5f * class_3532.method_15374(s)) + (0.3f * class_3532.method_15374((s * 2.13f) + 1.3f)) + (0.2f * class_3532.method_15374((s * 4.37f) + 2.9f));
    }

    static float wave01(float s) {
        return 0.5f + (0.5f * wave(s));
    }

    static float smoothstep(float x) {
        float x2 = class_3532.method_15363(x, 0.0f, 1.0f);
        return x2 * x2 * (3.0f - (2.0f * x2));
    }

    void close() {
        this.mesh.close();
    }
}
