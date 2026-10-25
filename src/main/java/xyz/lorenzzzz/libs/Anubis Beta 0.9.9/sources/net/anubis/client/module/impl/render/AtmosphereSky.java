package net.anubis.client.module.impl.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_287;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_5819;
import net.minecraft.class_9848;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/AtmosphereSky.class */
@Environment(EnvType.CLIENT)
final class AtmosphereSky {
    private static final float RADIUS = 100.0f;
    private static final int SEGMENTS = 48;
    private static final int HALO_RINGS = 4;
    private static final int HALO_SEGMENTS = 32;
    private static final int METEOR_SEGMENTS = 10;
    private static final int GOLDEN_ZENITH = 5210048;
    private static final int GOLDEN_HORIZON = 16032864;
    private static final int GOLDEN_GLOW = 16747069;
    private static final int DAY_ZENITH = 3108822;
    private static final int DAY_HORIZON = 11850996;
    private static final int DAY_GLOW = 16774362;
    private static final int BLUE_HOUR_ZENITH = 1319497;
    private final SkyMesh dome = new SkyMesh("Anubis sky dome", SkyMesh.PAINT, (RINGS.length - 1) * 48);
    private final SkyMesh endDome = new SkyMesh("Anubis end sky", SkyMesh.PAINT, (RINGS.length - 1) * 48);
    private final SkyMesh halo = new SkyMesh("Anubis sun and moon glow", SkyMesh.GLOW, 256);
    private final SkyMesh meteorMesh = new SkyMesh("Anubis shooting stars", SkyMesh.GLOW, 12);
    private final class_5819 random = class_5819.method_43047();
    private int zenith;
    private int horizon;
    private int glow;
    private float glowStrength;
    private float sunX;
    private float sunY;
    private float sunAngle;
    private float moonAngle;
    private boolean ready;
    private long meteorStart;
    private long meteorEnd;
    private long nextMeteor;
    private float mx;
    private float my;
    private float mz;
    private float tx;
    private float ty;
    private float tz;
    private static final float[] RINGS = {-30.0f, -12.0f, -4.0f, 0.0f, 4.0f, 9.0f, 16.0f, 26.0f, 40.0f, 58.0f, 76.0f, 90.0f};
    private static final float[] KEYS = {-0.35f, -0.08f, 0.06f, 0.3f};

    AtmosphereSky() {
    }

    void update(float sunAngle, float moonAngle, float rain, float thunder, float flash, int nightZenith, int nightHorizon, int twilight) {
        this.sunAngle = sunAngle;
        this.moonAngle = moonAngle;
        this.sunX = -class_3532.method_15374(sunAngle);
        this.sunY = class_3532.method_15362(sunAngle);
        int blueHourZenith = class_9848.method_61319(0.3f, BLUE_HOUR_ZENITH, nightZenith);
        int[] zeniths = {nightZenith, blueHourZenith, GOLDEN_ZENITH, DAY_ZENITH};
        int[] horizons = {nightHorizon, scale(twilight, 0.75f), GOLDEN_HORIZON, DAY_HORIZON};
        int[] glows = {nightHorizon, twilight, GOLDEN_GLOW, DAY_GLOW};
        float[] strengths = {0.0f, 0.55f, 1.0f, 0.25f};
        int key = 0;
        while (key < KEYS.length - 2 && this.sunY > KEYS[key + 1]) {
            key++;
        }
        float t = AtmosphereAurora.smoothstep((this.sunY - KEYS[key]) / (KEYS[key + 1] - KEYS[key]));
        this.zenith = class_9848.method_61319(t, zeniths[key], zeniths[key + 1]);
        this.horizon = class_9848.method_61319(t, horizons[key], horizons[key + 1]);
        this.glow = class_9848.method_61319(t, glows[key], glows[key + 1]);
        this.glowStrength = class_3532.method_16439(t, strengths[key], strengths[key + 1]) * (1.0f - rain);
        this.zenith = weather(this.zenith, rain, thunder, flash);
        this.horizon = weather(this.horizon, rain, thunder, flash);
        this.ready = true;
    }

    private static int weather(int rgb, float rain, float thunder, float flash) {
        int grey = scale(luma(rgb), 0.85f);
        int rgb2 = scale(class_9848.method_61319(rain * 0.65f, rgb, grey), (1.0f - (0.35f * rain)) - (0.25f * thunder));
        return flash > 0.0f ? class_9848.method_61319(flash * 0.45f, rgb2, 15265023) : rgb2;
    }

    boolean ready() {
        return this.ready;
    }

    int horizonToward(float x, float z) {
        float length = class_3532.method_15355((x * x) + (z * z));
        return length < 1.0E-4f ? this.horizon : colourAt(x / length, 0.02f, z / length);
    }

    float golden() {
        return 1.0f - Math.min(1.0f, Math.abs(this.sunY - 0.06f) / 0.22f);
    }

    float nightness() {
        return AtmosphereAurora.smoothstep((0.05f - this.sunY) / 0.4f);
    }

    int twilightGlow() {
        return this.glow;
    }

    void renderDome(class_4587 pose) {
        class_287 builder = this.dome.begin();
        domeQuads(builder, false);
        this.dome.upload(builder);
        this.dome.draw(pose, 1.0f);
    }

    void renderEndDome(class_4587 pose) {
        if (!this.endDome.isBuilt()) {
            class_287 builder = this.endDome.begin();
            domeQuads(builder, true);
            this.endDome.upload(builder);
        }
        this.endDome.draw(pose, 1.0f);
    }

    private void domeQuads(class_287 builder, boolean end) {
        for (int ring = 0; ring < RINGS.length - 1; ring++) {
            for (int s = 0; s < 48; s++) {
                corner(builder, RINGS[ring], s, end);
                corner(builder, RINGS[ring], s + 1, end);
                corner(builder, RINGS[ring + 1], s + 1, end);
                corner(builder, RINGS[ring + 1], s, end);
            }
        }
    }

    private void corner(class_287 builder, float elevationDeg, int segment, boolean end) {
        float el = elevationDeg * 0.017453292f;
        float az = (segment / 48.0f) * 6.2831855f;
        float x = class_3532.method_15362(el) * class_3532.method_15362(az);
        float y = class_3532.method_15374(el);
        float z = class_3532.method_15362(el) * class_3532.method_15374(az);
        int rgb = end ? endColourAt(y) : colourAt(x, y, z);
        SkyMesh.vertex(builder, x * RADIUS, y * RADIUS, z * RADIUS, (-16777216) | rgb);
    }

    private int colourAt(float x, float y, float z) {
        int rgb = class_9848.method_61319((float) Math.sqrt(Math.max(y, 0.0f)), this.horizon, this.zenith);
        if (y < 0.0f) {
            rgb = scale(rgb, 1.0f - (0.35f * AtmosphereAurora.smoothstep((-y) / 0.4f)));
        }
        if (this.glowStrength > 0.001f) {
            float toSun = Math.max(0.0f, (x * this.sunX) + (y * this.sunY));
            float horizontal = Math.max(0.0f, this.sunX < 0.0f ? -x : x);
            float near = (float) ((Math.pow(toSun, 8.0d) * 0.9d) + (Math.pow(toSun, 2.0d) * 0.25d));
            float band = (float) (Math.exp((-Math.abs(y)) * 9.0f) * Math.pow(horizontal, 1.5d));
            float amount = class_3532.method_15363(this.glowStrength * (near + (band * 0.6f)), 0.0f, 1.0f);
            rgb = class_9848.method_61319(amount, rgb, this.glow);
        }
        return rgb & 16777215;
    }

    private static int endColourAt(float y) {
        return class_9848.method_61319((float) Math.sqrt(Math.max(y, 0.0f)), 1839152, 459789) & 16777215;
    }

    void renderHalos(class_4587 pose, float rainBrightness, float night) {
        class_287 builder = this.halo.begin();
        float low = 1.0f - AtmosphereAurora.smoothstep(this.sunY / 0.35f);
        float sunAlpha = AtmosphereAurora.smoothstep((this.sunY + 0.12f) / 0.12f) * rainBrightness * (0.45f + (0.35f * low));
        int sunColour = class_9848.method_61319(low, 16773584, 16742963);
        haloQuads(builder, this.sunAngle, 28.0f * (1.0f + (0.6f * low)), sunColour, sunAlpha);
        haloQuads(builder, this.moonAngle, 34.0f, 11520255, 0.32f * night * rainBrightness);
        this.halo.upload(builder);
        this.halo.draw(pose, 1.0f);
    }

    private static void haloQuads(class_287 builder, float angle, float radius, int rgb, float alpha) {
        if (alpha <= 0.002f) {
            return;
        }
        float[] radii = {0.0f, radius * 0.16f, radius * 0.38f, radius * 0.66f, radius};
        float[] alphas = {1.0f, 0.55f, 0.22f, 0.07f, 0.0f};
        float cosA = class_3532.method_15362(angle);
        float sinA = class_3532.method_15374(angle);
        for (int ring = 0; ring < 4; ring++) {
            for (int s = 0; s < 32; s++) {
                haloVertex(builder, radii[ring], s, cosA, sinA, rgb, alphas[ring] * alpha);
                haloVertex(builder, radii[ring], s + 1, cosA, sinA, rgb, alphas[ring] * alpha);
                haloVertex(builder, radii[ring + 1], s + 1, cosA, sinA, rgb, alphas[ring + 1] * alpha);
                haloVertex(builder, radii[ring + 1], s, cosA, sinA, rgb, alphas[ring + 1] * alpha);
            }
        }
    }

    private static void haloVertex(class_287 builder, float r, int segment, float cosA, float sinA, int rgb, float alpha) {
        float around = (segment / 32.0f) * 6.2831855f;
        float lx = class_3532.method_15362(around) * r;
        float lz = class_3532.method_15374(around) * r;
        float y = (RADIUS * cosA) - (lz * sinA);
        float z = (RADIUS * sinA) + (lz * cosA);
        SkyMesh.vertex(builder, -z, y, lx, (Math.round(class_3532.method_15363(alpha, 0.0f, 1.0f) * 255.0f) << 24) | (rgb & 16777215));
    }

    void renderMeteors(class_4587 pose, float night, float rainBrightness) {
        long now = System.nanoTime();
        if (now >= this.meteorEnd) {
            if (this.nextMeteor == 0) {
                this.nextMeteor = now + seconds(4.0f + (10.0f * this.random.method_43057()));
            }
            if (now < this.nextMeteor || night < 0.6f || rainBrightness < 0.8f) {
                return;
            } else {
                launch(now);
            }
        }
        float life = (now - this.meteorStart) / (this.meteorEnd - this.meteorStart);
        float fade = class_3532.method_15374(3.1415927f * class_3532.method_15363(life, 0.0f, 1.0f)) * night * rainBrightness;
        class_287 builder = this.meteorMesh.begin();
        float travel = life * 0.55f;
        float[] prev = null;
        for (int i = 0; i <= 10; i++) {
            float along = travel - ((0.16f * i) / 10.0f);
            float[] p = {(this.mx + (this.tx * along)) * RADIUS, (this.my + (this.ty * along)) * RADIUS, (this.mz + (this.tz * along)) * RADIUS};
            if (prev != null) {
                streak(builder, prev, p, fade * (1.0f - ((i - 1.0f) / 10.0f)), fade * (1.0f - (i / 10.0f)));
            }
            prev = p;
        }
        this.meteorMesh.upload(builder);
        this.meteorMesh.draw(pose, 1.0f);
    }

    private void launch(long now) {
        float el = (30.0f + (40.0f * this.random.method_43057())) * 0.017453292f;
        float az = this.random.method_43057() * 6.2831855f;
        this.mx = class_3532.method_15362(el) * class_3532.method_15362(az);
        this.my = class_3532.method_15374(el);
        this.mz = class_3532.method_15362(el) * class_3532.method_15374(az);
        float dx = -class_3532.method_15374(az);
        float dz = class_3532.method_15362(az);
        float dy = (-0.6f) - (0.4f * this.random.method_43057());
        float dot = (dx * this.mx) + (dy * this.my) + (dz * this.mz);
        this.tx = dx - (dot * this.mx);
        this.ty = dy - (dot * this.my);
        this.tz = dz - (dot * this.mz);
        float length = class_3532.method_15355((this.tx * this.tx) + (this.ty * this.ty) + (this.tz * this.tz));
        this.tx /= length;
        this.ty /= length;
        this.tz /= length;
        this.meteorStart = now;
        this.meteorEnd = now + seconds(0.7f + (0.5f * this.random.method_43057()));
        this.nextMeteor = this.meteorEnd + seconds(6.0f + (18.0f * this.random.method_43057()));
    }

    private static void streak(class_287 builder, float[] a, float[] b, float alphaA, float alphaB) {
        float lx = b[0] - a[0];
        float ly = b[1] - a[1];
        float lz = b[2] - a[2];
        float wx = (a[1] * lz) - (a[2] * ly);
        float wy = (a[2] * lx) - (a[0] * lz);
        float wz = (a[0] * ly) - (a[1] * lx);
        float wl = class_3532.method_15355((wx * wx) + (wy * wy) + (wz * wz));
        if (wl < 1.0E-5f) {
            return;
        }
        float half = 0.22f / wl;
        float wx2 = wx * half;
        float wy2 = wy * half;
        float wz2 = wz * half;
        int ca = (Math.round(class_3532.method_15363(alphaA, 0.0f, 1.0f) * 255.0f) << 24) | 15397631;
        int cb = (Math.round(class_3532.method_15363(alphaB, 0.0f, 1.0f) * 255.0f) << 24) | 15397631;
        SkyMesh.vertex(builder, a[0] - wx2, a[1] - wy2, a[2] - wz2, ca);
        SkyMesh.vertex(builder, a[0] + wx2, a[1] + wy2, a[2] + wz2, ca);
        SkyMesh.vertex(builder, b[0] + wx2, b[1] + wy2, b[2] + wz2, cb);
        SkyMesh.vertex(builder, b[0] - wx2, b[1] - wy2, b[2] - wz2, cb);
    }

    private static long seconds(float s) {
        return (long) (s * 1.0E9f);
    }

    static int scale(int rgb, float k) {
        int r = class_3532.method_15340(Math.round(((rgb >> 16) & 255) * k), 0, 255);
        int g = class_3532.method_15340(Math.round(((rgb >> 8) & 255) * k), 0, 255);
        int b = class_3532.method_15340(Math.round((rgb & 255) * k), 0, 255);
        return (rgb & (-16777216)) | (r << 16) | (g << 8) | b;
    }

    static int luma(int rgb) {
        int l = Math.round((0.2126f * ((rgb >> 16) & 255)) + (0.7152f * ((rgb >> 8) & 255)) + (0.0722f * (rgb & 255)));
        return (rgb & (-16777216)) | (l << 16) | (l << 8) | l;
    }

    void close() {
        this.dome.close();
        this.endDome.close();
        this.halo.close();
        this.meteorMesh.close();
        this.ready = false;
    }
}
