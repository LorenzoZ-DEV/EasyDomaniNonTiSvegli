package net.anubis.client.module.impl.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_287;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_5819;
import net.minecraft.class_9848;
import org.joml.Vector3f;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/AtmosphereStars.class */
@Environment(EnvType.CLIENT)
final class AtmosphereStars {
    private static final float RADIUS = 100.0f;
    private static final int STARS = 2600;
    private static final float GLOW_ABOVE = 0.8f;
    private static final long TWINKLE_NANOS = 50000000;
    private static final int BAND_COLUMNS = 180;
    private static final int BAND_ROWS = 12;
    private static final float BAND_HALF_WIDTH = 17.0f;
    private static final int NEBULA_COLUMNS = 72;
    private static final int NEBULA_ROWS = 36;
    private static final Vector3f BAND_NORMAL = new Vector3f(0.42f, 0.5f, 0.76f).normalize();
    private boolean placed;
    private long lastTwinkle;
    private int bandTint;
    private final SkyMesh starMesh = new SkyMesh("Anubis stars", SkyMesh.GLOW, 5200);
    private final SkyMesh bandMesh = new SkyMesh("Anubis milky way", SkyMesh.GLOW, 2160);
    private final SkyMesh nebulaMesh = new SkyMesh("Anubis end nebulae", SkyMesh.GLOW, 2592);
    private final float[] dir = new float[7800];
    private final float[] size = new float[STARS];
    private final float[] light = new float[STARS];
    private final float[] roll = new float[STARS];
    private final float[] phase = new float[STARS];
    private final float[] speed = new float[STARS];
    private final int[] rgb = new int[STARS];
    private float builtBrightness = -1.0f;

    AtmosphereStars() {
    }

    void renderStars(class_4587 pose, float alpha, float brightness) {
        long now = System.nanoTime();
        if (now - this.lastTwinkle > TWINKLE_NANOS || brightness != this.builtBrightness) {
            this.lastTwinkle = now;
            this.builtBrightness = brightness;
            buildStars((now % 3600000000000L) / 1.0E9f, brightness);
        }
        this.starMesh.draw(pose, alpha);
    }

    void renderBand(class_4587 pose, float alpha, int tint) {
        if (!this.bandMesh.isBuilt() || tint != this.bandTint) {
            this.bandTint = tint;
            buildBand(tint);
        }
        this.bandMesh.draw(pose, alpha);
    }

    void renderNebulae(class_4587 pose, float alpha) {
        if (!this.nebulaMesh.isBuilt()) {
            buildNebulae();
        }
        this.nebulaMesh.draw(pose, alpha);
    }

    private void place() {
        float y;
        float x;
        float fMethod_15374;
        class_5819 random = class_5819.method_43049(10842L);
        Vector3f a = new Vector3f();
        Vector3f b = new Vector3f();
        bandAxes(a, b);
        for (int i = 0; i < STARS; i++) {
            if (random.method_43057() < 0.3f) {
                float along = random.method_43057() * 6.2831855f;
                float across = ((float) random.method_43059()) * 9.0f * 0.017453292f;
                float cosAcross = class_3532.method_15362(across);
                float sinAcross = class_3532.method_15374(across);
                x = (cosAcross * ((class_3532.method_15362(along) * a.x) + (class_3532.method_15374(along) * b.x))) + (sinAcross * BAND_NORMAL.x);
                y = (cosAcross * ((class_3532.method_15362(along) * a.y) + (class_3532.method_15374(along) * b.y))) + (sinAcross * BAND_NORMAL.y);
                fMethod_15374 = (cosAcross * ((class_3532.method_15362(along) * a.z) + (class_3532.method_15374(along) * b.z))) + (sinAcross * BAND_NORMAL.z);
            } else {
                y = (random.method_43057() * 2.0f) - 1.0f;
                float around = random.method_43057() * 6.2831855f;
                float flat = class_3532.method_15355(1.0f - (y * y));
                x = flat * class_3532.method_15362(around);
                fMethod_15374 = flat * class_3532.method_15374(around);
            }
            float z = fMethod_15374;
            this.dir[i * 3] = x;
            this.dir[(i * 3) + 1] = y;
            this.dir[(i * 3) + 2] = z;
            float big = (float) Math.pow(random.method_43057(), 6.0d);
            this.size[i] = 0.1f + (0.5f * big);
            this.light[i] = class_3532.method_15363(0.3f + (0.7f * big) + (0.25f * random.method_43057()), 0.0f, 1.0f);
            this.roll[i] = random.method_43057() * 6.2831855f;
            this.phase[i] = random.method_43057() * 6.2831855f;
            this.speed[i] = 1.5f + (3.5f * random.method_43057());
            this.rgb[i] = starColour(random.method_43057());
        }
        this.placed = true;
    }

    private static int starColour(float t) {
        if (t < 0.18f) {
            return class_9848.method_61319(t / 0.18f, 16757870, 16774116);
        }
        if (t < 0.75f) {
            return 16775408;
        }
        return class_9848.method_61319((t - 0.75f) / 0.25f, 16774134, 11125503);
    }

    private void buildStars(float time, float brightness) {
        if (!this.placed) {
            place();
        }
        class_287 builder = this.starMesh.begin();
        for (int i = 0; i < STARS; i++) {
            float twinkle = 0.62f + (0.38f * class_3532.method_15374((time * this.speed[i]) + this.phase[i]));
            float alpha = class_3532.method_15363(this.light[i] * twinkle * brightness, 0.0f, 1.0f);
            star(builder, i, this.size[i], alpha);
            if (this.light[i] > GLOW_ABOVE) {
                star(builder, i, this.size[i] * 4.0f, alpha * 0.12f);
            }
        }
        this.starMesh.upload(builder);
    }

    private void star(class_287 builder, int i, float half, float alpha) {
        float ux;
        float uy;
        float uz;
        float x = this.dir[i * 3];
        float y = this.dir[(i * 3) + 1];
        float z = this.dir[(i * 3) + 2];
        if (Math.abs(y) < 0.9f) {
            ux = z;
            uy = 0.0f;
            uz = -x;
        } else {
            ux = 0.0f;
            uy = -z;
            uz = y;
        }
        float ul = class_3532.method_15355((ux * ux) + (uy * uy) + (uz * uz));
        float ux2 = ux / ul;
        float uy2 = uy / ul;
        float uz2 = uz / ul;
        float vx = (y * uz2) - (z * uy2);
        float vy = (z * ux2) - (x * uz2);
        float vz = (x * uy2) - (y * ux2);
        float c = class_3532.method_15362(this.roll[i]) * half;
        float s = class_3532.method_15374(this.roll[i]) * half;
        float ax = (ux2 * c) + (vx * s);
        float ay = (uy2 * c) + (vy * s);
        float az = (uz2 * c) + (vz * s);
        float bx = (vx * c) - (ux2 * s);
        float by = (vy * c) - (uy2 * s);
        float bz = (vz * c) - (uz2 * s);
        float cx = x * RADIUS;
        float cy = y * RADIUS;
        float cz = z * RADIUS;
        int argb = (Math.round(alpha * 255.0f) << 24) | this.rgb[i];
        SkyMesh.vertex(builder, (cx - ax) - bx, (cy - ay) - by, (cz - az) - bz, argb);
        SkyMesh.vertex(builder, (cx + ax) - bx, (cy + ay) - by, (cz + az) - bz, argb);
        SkyMesh.vertex(builder, cx + ax + bx, cy + ay + by, cz + az + bz, argb);
        SkyMesh.vertex(builder, (cx - ax) + bx, (cy - ay) + by, (cz - az) + bz, argb);
    }

    private static void bandAxes(Vector3f a, Vector3f b) {
        new Vector3f(0.0f, 1.0f, 0.0f).cross(BAND_NORMAL, a).normalize();
        new Vector3f(BAND_NORMAL).cross(a, b).normalize();
    }

    private void buildBand(int tint) {
        Vector3f a = new Vector3f();
        Vector3f b = new Vector3f();
        bandAxes(a, b);
        float[][] px = new float[181][13];
        float[][] py = new float[181][13];
        float[][] pz = new float[181][13];
        int[][] colour = new int[181][13];
        for (int c = 0; c <= BAND_COLUMNS; c++) {
            float along = (c / 180.0f) * 6.2831855f;
            float ax = (class_3532.method_15362(along) * a.x) + (class_3532.method_15374(along) * b.x);
            float ay = (class_3532.method_15362(along) * a.y) + (class_3532.method_15374(along) * b.y);
            float az = (class_3532.method_15362(along) * a.z) + (class_3532.method_15374(along) * b.z);
            float width = 0.55f + (0.45f * fbm(ax * 1.7f, ay * 1.7f, az * 1.7f));
            float rift = 3.0f * (fbm((ax * 2.3f) + 5.0f, ay * 2.3f, az * 2.3f) - 0.5f);
            float riftDepth = class_3532.method_15363((fbm(ax * 1.4f, (ay * 1.4f) + 9.0f, az * 1.4f) * 1.6f) - 0.35f, 0.0f, 1.0f);
            for (int r = 0; r <= 12; r++) {
                float acrossDeg = (((r / 12.0f) * 2.0f) - 1.0f) * 17.0f;
                float across = acrossDeg * 0.017453292f;
                float cosAcross = class_3532.method_15362(across);
                float sinAcross = class_3532.method_15374(across);
                float x = (cosAcross * ax) + (sinAcross * BAND_NORMAL.x);
                float y = (cosAcross * ay) + (sinAcross * BAND_NORMAL.y);
                float z = (cosAcross * az) + (sinAcross * BAND_NORMAL.z);
                px[c][r] = x * RADIUS;
                py[c][r] = y * RADIUS;
                pz[c][r] = z * RADIUS;
                float edge = acrossDeg / (17.0f * width);
                float core = (float) Math.exp((-edge) * edge * 2.2f);
                float dust = 0.35f + (0.65f * fbm(x * 6.0f, y * 6.0f, z * 6.0f));
                float lane = 1.0f - ((0.75f * riftDepth) * ((float) Math.exp(-sq((acrossDeg - rift) / 2.6f))));
                float glow = class_3532.method_15363(core * dust * lane, 0.0f, 1.0f);
                int rgb = class_9848.method_61319(core, 7306440, tint) & 16777215;
                colour[c][r] = (Math.round((glow * 0.42f) * 255.0f) << 24) | rgb;
            }
        }
        class_287 builder = this.bandMesh.begin();
        for (int c2 = 0; c2 < BAND_COLUMNS; c2++) {
            for (int r2 = 0; r2 < 12; r2++) {
                SkyMesh.vertex(builder, px[c2][r2], py[c2][r2], pz[c2][r2], colour[c2][r2]);
                SkyMesh.vertex(builder, px[c2 + 1][r2], py[c2 + 1][r2], pz[c2 + 1][r2], colour[c2 + 1][r2]);
                SkyMesh.vertex(builder, px[c2 + 1][r2 + 1], py[c2 + 1][r2 + 1], pz[c2 + 1][r2 + 1], colour[c2 + 1][r2 + 1]);
                SkyMesh.vertex(builder, px[c2][r2 + 1], py[c2][r2 + 1], pz[c2][r2 + 1], colour[c2][r2 + 1]);
            }
        }
        this.bandMesh.upload(builder);
    }

    private void buildNebulae() {
        float[][] px = new float[73][37];
        float[][] py = new float[73][37];
        float[][] pz = new float[73][37];
        int[][] colour = new int[73][37];
        for (int c = 0; c <= NEBULA_COLUMNS; c++) {
            float around = (c / 72.0f) * 6.2831855f;
            for (int r = 0; r <= 36; r++) {
                float el = ((r / 36.0f) - 0.5f) * 3.1415927f;
                float x = class_3532.method_15362(el) * class_3532.method_15362(around);
                float y = class_3532.method_15374(el);
                float z = class_3532.method_15362(el) * class_3532.method_15374(around);
                px[c][r] = x * RADIUS;
                py[c][r] = y * RADIUS;
                pz[c][r] = z * RADIUS;
                float density = AtmosphereAurora.smoothstep((fbm(x * 2.2f, y * 2.2f, z * 2.2f) - 0.42f) / 0.35f);
                float hue = fbm((x * 1.3f) + 11.0f, y * 1.3f, z * 1.3f);
                int rgb = hue < 0.5f ? class_9848.method_61319(hue * 2.0f, 8011007, 16732104) : class_9848.method_61319((hue - 0.5f) * 2.0f, 16732104, 3989712);
                colour[c][r] = (Math.round((density * 0.55f) * 255.0f) << 24) | (rgb & 16777215);
            }
        }
        class_287 builder = this.nebulaMesh.begin();
        for (int c2 = 0; c2 < NEBULA_COLUMNS; c2++) {
            for (int r2 = 0; r2 < 36; r2++) {
                SkyMesh.vertex(builder, px[c2][r2], py[c2][r2], pz[c2][r2], colour[c2][r2]);
                SkyMesh.vertex(builder, px[c2 + 1][r2], py[c2 + 1][r2], pz[c2 + 1][r2], colour[c2 + 1][r2]);
                SkyMesh.vertex(builder, px[c2 + 1][r2 + 1], py[c2 + 1][r2 + 1], pz[c2 + 1][r2 + 1], colour[c2 + 1][r2 + 1]);
                SkyMesh.vertex(builder, px[c2][r2 + 1], py[c2][r2 + 1], pz[c2][r2 + 1], colour[c2][r2 + 1]);
            }
        }
        this.nebulaMesh.upload(builder);
    }

    static float fbm(float x, float y, float z) {
        float sum = 0.0f;
        float amplitude = 0.5f;
        float total = 0.0f;
        for (int octave = 0; octave < 4; octave++) {
            sum += amplitude * noise(x, y, z);
            total += amplitude;
            x *= 2.03f;
            y *= 2.03f;
            z *= 2.03f;
            amplitude *= 0.5f;
        }
        return sum / total;
    }

    private static float noise(float x, float y, float z) {
        int ix = class_3532.method_15375(x);
        int iy = class_3532.method_15375(y);
        int iz = class_3532.method_15375(z);
        float fx = x - ix;
        float fy = y - iy;
        float fz = z - iz;
        float fx2 = fx * fx * (3.0f - (2.0f * fx));
        float fy2 = fy * fy * (3.0f - (2.0f * fy));
        float fz2 = fz * fz * (3.0f - (2.0f * fz));
        float x00 = class_3532.method_16439(fx2, hash(ix, iy, iz), hash(ix + 1, iy, iz));
        float x10 = class_3532.method_16439(fx2, hash(ix, iy + 1, iz), hash(ix + 1, iy + 1, iz));
        float x01 = class_3532.method_16439(fx2, hash(ix, iy, iz + 1), hash(ix + 1, iy, iz + 1));
        float x11 = class_3532.method_16439(fx2, hash(ix, iy + 1, iz + 1), hash(ix + 1, iy + 1, iz + 1));
        return class_3532.method_16439(fz2, class_3532.method_16439(fy2, x00, x10), class_3532.method_16439(fy2, x01, x11));
    }

    private static float hash(int x, int y, int z) {
        int h = (x * 374761393) + (y * 668265263) + (z * 1274126177);
        int h2 = (h ^ (h >>> 13)) * 1274126177;
        return ((h2 ^ (h2 >>> 16)) & 16777215) / 1.6777215E7f;
    }

    private static float sq(float v) {
        return v * v;
    }

    void close() {
        this.starMesh.close();
        this.bandMesh.close();
        this.nebulaMesh.close();
        this.builtBrightness = -1.0f;
    }
}
