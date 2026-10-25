package net.anubis.client.module.impl.render;

import imgui.flag.ImGuiHoveredFlags;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.Arrays;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4608;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/PharaohMesh.class */
@Environment(EnvType.CLIENT)
final class PharaohMesh {
    static final int GOLD = 0;
    static final int INLAY = 1;
    static final int GEM = 2;
    static final int BRONZE = 3;
    static final int FUR = 4;
    static final int MATERIALS = 5;
    static final int STYLE_NEMES = 0;
    static final int STYLE_KHEPRESH = 1;
    static final int STYLE_SUN_DISC = 2;
    private static final float PX = 0.0625f;
    private static final float TWIST = 0.12f;
    private static final float[] CORNER_U = {0.0f, 1.0f, 1.0f, 0.0f};
    private static final float[] CORNER_V = {0.0f, 0.0f, 1.0f, 1.0f};
    private static final float BAND_TOP = -6.6f;
    private static final float BAND_BOTTOM = -5.3f;
    private final float[] pos;
    private final float[] normal;
    private final float[] sweep;
    private final byte[] material;
    private final float[] tone;
    private final int quads;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/PharaohMesh$Look.class */
    @Environment(EnvType.CLIENT)
    static final class Look extends Record {
        private final int[] palette;
        private final boolean glow;
        private final float sweepCos;
        private final float sweepSin;
        private final float sweepGain;
        private final float gemPulse;

        Look(int[] palette, boolean glow, float sweepCos, float sweepSin, float sweepGain, float gemPulse) {
            this.palette = palette;
            this.glow = glow;
            this.sweepCos = sweepCos;
            this.sweepSin = sweepSin;
            this.sweepGain = sweepGain;
            this.gemPulse = gemPulse;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Look.class), Look.class, "palette;glow;sweepCos;sweepSin;sweepGain;gemPulse", "FIELD:Lnet/anubis/client/module/impl/render/PharaohMesh$Look;->palette:[I", "FIELD:Lnet/anubis/client/module/impl/render/PharaohMesh$Look;->glow:Z", "FIELD:Lnet/anubis/client/module/impl/render/PharaohMesh$Look;->sweepCos:F", "FIELD:Lnet/anubis/client/module/impl/render/PharaohMesh$Look;->sweepSin:F", "FIELD:Lnet/anubis/client/module/impl/render/PharaohMesh$Look;->sweepGain:F", "FIELD:Lnet/anubis/client/module/impl/render/PharaohMesh$Look;->gemPulse:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Look.class), Look.class, "palette;glow;sweepCos;sweepSin;sweepGain;gemPulse", "FIELD:Lnet/anubis/client/module/impl/render/PharaohMesh$Look;->palette:[I", "FIELD:Lnet/anubis/client/module/impl/render/PharaohMesh$Look;->glow:Z", "FIELD:Lnet/anubis/client/module/impl/render/PharaohMesh$Look;->sweepCos:F", "FIELD:Lnet/anubis/client/module/impl/render/PharaohMesh$Look;->sweepSin:F", "FIELD:Lnet/anubis/client/module/impl/render/PharaohMesh$Look;->sweepGain:F", "FIELD:Lnet/anubis/client/module/impl/render/PharaohMesh$Look;->gemPulse:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Look.class, Object.class), Look.class, "palette;glow;sweepCos;sweepSin;sweepGain;gemPulse", "FIELD:Lnet/anubis/client/module/impl/render/PharaohMesh$Look;->palette:[I", "FIELD:Lnet/anubis/client/module/impl/render/PharaohMesh$Look;->glow:Z", "FIELD:Lnet/anubis/client/module/impl/render/PharaohMesh$Look;->sweepCos:F", "FIELD:Lnet/anubis/client/module/impl/render/PharaohMesh$Look;->sweepSin:F", "FIELD:Lnet/anubis/client/module/impl/render/PharaohMesh$Look;->sweepGain:F", "FIELD:Lnet/anubis/client/module/impl/render/PharaohMesh$Look;->gemPulse:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int[] palette() {
            return this.palette;
        }

        public boolean glow() {
            return this.glow;
        }

        public float sweepCos() {
            return this.sweepCos;
        }

        public float sweepSin() {
            return this.sweepSin;
        }

        public float sweepGain() {
            return this.sweepGain;
        }

        public float gemPulse() {
            return this.gemPulse;
        }
    }

    PharaohMesh(Builder b) {
        this.quads = b.quads;
        this.pos = Arrays.copyOf(b.pos, this.quads * 12);
        this.normal = Arrays.copyOf(b.normal, this.quads * 3);
        this.sweep = Arrays.copyOf(b.sweep, this.quads * 8);
        this.material = Arrays.copyOf(b.material, this.quads);
        this.tone = Arrays.copyOf(b.tone, this.quads);
    }

    boolean isEmpty() {
        return this.quads == 0;
    }

    void emit(class_4587.class_4665 pose, class_4588 out, int light, Look look) {
        int[] palette = look.palette();
        float gain = look.sweepGain();
        boolean glint = gain > 0.004f;
        float sc = look.sweepCos();
        float ss = look.sweepSin();
        for (int q = 0; q < this.quads; q++) {
            byte b = this.material[q];
            int argb = palette[b];
            float base = b == 2 ? this.tone[q] * look.gemPulse() : this.tone[q];
            boolean metal = glint && (b == 0 || b == 3);
            int lit = (b == 2 && look.glow()) ? 15728880 : light;
            int n = q * 3;
            float nx = this.normal[n];
            float ny = this.normal[n + 1];
            float nz = this.normal[n + 2];
            for (int v = 0; v < 4; v++) {
                int p = (q * 12) + (v * 3);
                float bright = base;
                if (metal) {
                    int s = (q * 8) + (v * 2);
                    float c = (this.sweep[s] * sc) + (this.sweep[s + 1] * ss);
                    if (c > 0.0f) {
                        float c2 = c * c;
                        float c3 = c2 * c2;
                        bright += gain * c3 * c3;
                    }
                }
                out.method_56824(pose, this.pos[p], this.pos[p + 1], this.pos[p + 2]).method_39415(tint(argb, bright)).method_22913(CORNER_U[v], CORNER_V[v]).method_22922(class_4608.field_21444).method_60803(lit).method_60831(pose, nx, ny, nz);
            }
        }
    }

    private static int tint(int argb, float k) {
        int r;
        int g;
        int b;
        int r2 = (argb >> 16) & 255;
        int g2 = (argb >> 8) & 255;
        int b2 = argb & 255;
        if (k <= 1.0f) {
            r = (int) (r2 * k);
            g = (int) (g2 * k);
            b = (int) (b2 * k);
        } else {
            float w = Math.min(1.0f, k - 1.0f);
            r = r2 + ((int) ((255 - r2) * w));
            g = g2 + ((int) ((255 - g2) * w));
            b = b2 + ((int) ((255 - b2) * w));
        }
        return (-16777216) | (r << 16) | (g << 8) | b;
    }

    static PharaohMesh head(int style, int stripes, boolean helmet) {
        Builder b = new Builder();
        float e = helmet ? 5.35f : 4.8f;
        float top = (-4.0f) - e;
        brow(b, e);
        switch (style) {
            case 1:
                khepresh(b, e, top);
                break;
            case 2:
                sunDisc(b, e, top);
                break;
            default:
                nemes(b, e, top, stripes);
                break;
        }
        uraeus(b, -(e + 0.45f));
        return new PharaohMesh(b);
    }

    static PharaohMesh broadCollar(boolean chest, boolean collar) {
        Builder b = new Builder();
        if (collar) {
            collar(b, ((-2.0f) - lift(chest)) - TWIST);
        }
        return new PharaohMesh(b);
    }

    static PharaohMesh body(int style, int stripes, boolean chest, boolean collar) {
        Builder b = new Builder();
        float lift = lift(chest);
        float front = (-2.0f) - lift;
        float back = 2.0f + lift;
        if (collar) {
            collar(b, front - TWIST);
        }
        if (style == 0) {
            for (int s = -1; s <= 1; s += 2) {
                float z0 = front - 0.9f;
                float z1 = front - 0.35f;
                b.stripedSlab(-0.3f, s * 2.0f, s * 6.2f, z0, z1, 5.0f, s * 1.6f, s * 4.4f, z0, z1, stripes + 1, 1, 0);
                b.slab(5.0f, s * 1.75f, s * 4.25f, z0 + 0.05f, z1 - 0.05f, 5.7f, s * 2.1f, s * 3.9f, z0 + 0.1f, z1 - 0.1f, 3);
            }
            b.stripedSlab(-0.4f, -3.4f, 3.4f, back, back + 0.6f, 4.4f, -1.3f, 1.3f, back, back + 0.6f, stripes, 1, 0);
            b.box(-1.6f, 4.4f, back - 0.1f, 1.6f, 5.2f, back + 0.8f, 0);
            b.slab(5.2f, -1.1f, 1.1f, back, back + 0.55f, 6.4f, -0.4f, 0.4f, back + 0.1f, back + 0.45f, 3);
        }
        return new PharaohMesh(b);
    }

    private static float lift(boolean chest) {
        return chest ? 1.15f : 0.2f;
    }

    private static void brow(Builder b, float e) {
        float o = e + 0.45f;
        float i = e - 0.3f;
        b.box(-o, BAND_TOP, -o, o, BAND_BOTTOM, -i, 0);
        b.box(-o, BAND_TOP, i, o, BAND_BOTTOM, o, 0);
        b.box(-o, BAND_TOP, -i, -i, BAND_BOTTOM, i, 0);
        b.box(i, BAND_TOP, -i, o, BAND_BOTTOM, i, 0);
        for (int s = -1; s <= 1; s += 2) {
            for (int k = 0; k < 2; k++) {
                float x = s * (1.7f + (1.6f * k));
                b.box(x - 0.45f, -6.2999997f, (-o) - TWIST, x + 0.45f, -5.6000004f, (-o) + 0.05f, 1);
            }
        }
    }

    private static void uraeus(Builder b, float zf) {
        b.slab(BAND_BOTTOM, -0.5f, 0.5f, zf - 0.5f, zf, -7.8f, -0.4f, 0.4f, zf - 0.95f, zf - 0.35f, 0);
        b.slab(-7.6f, -0.8f, 0.8f, zf - 1.15f, zf - 0.55f, -9.6f, -1.15f, 1.15f, zf - 1.2f, zf - 0.6f, 0);
        b.slab(-9.6f, -1.15f, 1.15f, zf - 1.2f, zf - 0.6f, -10.1f, -0.5f, 0.5f, zf - 1.2f, zf - 0.65f, 0);
        b.box(-0.42f, -10.7f, zf - 1.75f, 0.42f, -10.0f, zf - 0.75f, 0);
        b.box(-0.4f, -9.1f, zf - 1.32f, 0.4f, -8.2f, zf - 1.1f, 2);
    }

    private static void nemes(Builder b, float e, float top, int stripes) {
        int hoodBands = Math.max(2, (stripes + 1) / 2);
        float hoodTop = top - 1.2f;
        for (int k = 0; k < hoodBands; k++) {
            float t0 = k / hoodBands;
            float t1 = (k + 1) / hoodBands;
            float y0 = lerp(BAND_TOP, hoodTop, t0);
            float y1 = lerp(BAND_TOP, hoodTop, t1);
            float x0 = hood(e, t0);
            float x1 = hood(e, t1);
            b.slab(y0, -x0, x0, -x0, x0, y1, -x1, x1, -x1, x1, (k & 1) == 0 ? 1 : 0);
        }
        float crown = hood(e, 1.0f);
        b.slab(hoodTop, -crown, crown, -crown, crown, hoodTop - 0.55f, -(crown - 0.9f), crown - 0.9f, -(crown - 0.9f), crown - 0.9f, 0);
        b.stripedSlab(BAND_BOTTOM, -e, e, e - 0.5f, e + 0.3f, 0.8f, -(e + 1.1f), e + 1.1f, e - 0.5f, e + 0.55f, stripes, 1, 0);
        float[] upper = new float[12];
        float[] lower = new float[12];
        for (int s = -1; s <= 1; s += 2) {
            ring(upper, BAND_BOTTOM, s * (e - 0.2f), s * (e + 0.45f), -2.6f, e + 0.2f);
            ring(lower, 0.4f, s * (e + 1.6f), s * (e + 2.3f), -3.4f, e + 0.9f);
            b.striped(upper, lower, stripes, 1, 0);
        }
    }

    private static float hood(float e, float t) {
        return (e + (0.35f * ((float) Math.sin((3.141592653589793d * ((double) t)) * 0.8d)))) - (((0.9f * t) * t) * t);
    }

    private static void khepresh(Builder b, float e, float top) {
        float crownTop = top - 5.2f;
        int j = 0;
        while (j < 6) {
            float t0 = j / 6;
            float t1 = (j + 1) / 6;
            float y0 = lerp(BAND_TOP, crownTop, t0);
            float y1 = lerp(BAND_TOP, crownTop, t1);
            float x0 = crownWidth(e, t0);
            float x1 = crownWidth(e, t1);
            float f0 = crownFront(e, t0);
            float f1 = crownFront(e, t1);
            float k0 = crownBack(e, t0);
            float k1 = crownBack(e, t1);
            b.slab(y0, -x0, x0, f0, k0, y1, -x1, x1, f1, k1, 1);
            boolean wide = (j & 1) == 0;
            b.studs(-x0, y0, f0, x0, y0, f0, x1, y1, f1, -x1, y1, f1, wide, j < 2, 0.0f, (y0 + y1) * 0.5f, 0.0f);
            b.studs(x0, y0, k0, -x0, y0, k0, -x1, y1, k1, x1, y1, k1, wide, false, 0.0f, (y0 + y1) * 0.5f, 0.0f);
            b.studs(-x0, y0, k0, -x0, y0, f0, -x1, y1, f1, -x1, y1, k1, wide, false, 0.0f, (y0 + y1) * 0.5f, 0.0f);
            b.studs(x0, y0, f0, x0, y0, k0, x1, y1, k1, x1, y1, f1, wide, false, 0.0f, (y0 + y1) * 0.5f, 0.0f);
            j++;
        }
        float xt = crownWidth(e, 1.0f);
        float ft = crownFront(e, 1.0f);
        float kt = crownBack(e, 1.0f);
        b.slab(crownTop, -xt, xt, ft, kt, crownTop - 0.6f, -(xt - 0.9f), xt - 0.9f, ft + 0.9f, kt - 0.9f, 1);
        for (int s = -1; s <= 1; s += 2) {
            b.slab(-5.7000003f, s * 0.5f, s * 1.5f, e + 0.35f, e + 0.6f, 3.2f, s * 1.0f, s * 2.1f, e + 0.9f, e + 1.15f, 0);
            b.slab(3.2f, s * 1.05f, s * 2.05f, e + 0.9f, e + 1.15f, 3.8f, s * 1.2f, s * 1.9f, e + 0.95f, e + 1.1f, 3);
        }
    }

    private static float crownWidth(float e, float t) {
        return ((e + 0.3f) + (0.9f * ((float) Math.sin(3.141592653589793d * ((double) t))))) - ((((1.3f * t) * t) * t) * t);
    }

    private static float crownFront(float e, float t) {
        return ((-(e + 0.3f)) - (0.35f * ((float) Math.sin(3.141592653589793d * ((double) t))))) + (1.8f * t * t * t);
    }

    private static float crownBack(float e, float t) {
        return e + 0.3f + (0.6f * ((float) Math.sin(3.141592653589793d * ((double) t)))) + (2.4f * t * t);
    }

    private static void sunDisc(Builder b, float e, float top) {
        float m = e - 1.0f;
        b.slab(top + 0.3f, -m, m, -m, m, top - 1.8f, -(m + 0.4f), m + 0.4f, -(m + 0.4f), m + 0.4f, 0);
        float band = m + 0.35f;
        b.box(-band, top - 1.25f, -band, band, top - 0.7f, band, 1);
        float rim = m + 0.75f;
        b.box(-rim, top - 2.3f, -rim, rim, top - 1.8f, rim, 3);
        float[] path = new float[11 * 3];
        float[] half = new float[11];
        for (int s = -1; s <= 1; s += 2) {
            for (int i = 0; i < 11; i++) {
                float t = i / (11 - 1);
                path[i * 3] = s * (1.6f + (5.0f * ((float) Math.sin(2.9845130209103035d * ((double) t)))));
                path[(i * 3) + 1] = (top - 2.3f) - (11.2f * t);
                path[(i * 3) + 2] = 0.6f + (0.5f * ((float) Math.sin(3.141592653589793d * ((double) t))));
                half[i] = 0.85f - (0.65f * t);
            }
            b.tube(path, half, 11, 0);
        }
        b.box(-0.55f, top - 4.2f, 0.45f, 0.55f, top - 2.3f, 1.35f, 3);
        float cy = top - 8.3f;
        b.disc(0.0f, cy, 0.9f, 4.4f, 0.45f, 24, 2, 0);
        b.annulus(0.0f, cy, 0.9f - 0.52f, 3.4f, 3.95f, 24, 0, 0.9f);
    }

    private static void collar(Builder b, float z) {
        for (int row = 0; row < 4; row++) {
            float r0 = 1.9f + (row * 0.68f);
            float r1 = r0 + 0.68f;
            double from = Math.asin(Math.min(1.0d, 1.0d / ((double) r0)));
            double span = 3.141592653589793d - (2.0d * from);
            int mat = (row & 1) == 0 ? 0 : 1;
            for (int i = 0; i < 12; i++) {
                double a0 = from + ((span * ((double) i)) / ((double) 12));
                double a1 = from + ((span * ((double) (i + 1))) / ((double) 12));
                float c0 = (float) Math.cos(a0);
                float s0 = (float) Math.sin(a0);
                float c1 = (float) Math.cos(a1);
                float s1 = (float) Math.sin(a1);
                b.quad(c0 * r0, (-1.2f) + (s0 * r0), z, c0 * r1, (-1.2f) + (s0 * r1), z, c1 * r1, (-1.2f) + (s1 * r1), z, c1 * r0, (-1.2f) + (s1 * r0), z, 0.0f, -1.2f, z + 1.0f, mat);
            }
        }
        float edge = 1.9f + (4 * 0.68f) + 0.42f;
        double from2 = Math.asin(Math.min(1.0d, 1.0d / ((double) edge)));
        for (int i2 = 0; i2 < 9; i2++) {
            double a = from2 + (((3.141592653589793d - (2.0d * from2)) * (((double) i2) + 0.5d)) / ((double) 9));
            float c = (float) Math.cos(a);
            float s = (float) Math.sin(a);
            float x = c * edge;
            float y = (-1.2f) + (s * edge);
            float tx = (-s) * 0.3f;
            float ty = c * 0.3f;
            float rx = c * 0.42f;
            float ry = s * 0.42f;
            b.quad(x - rx, y - ry, z - 0.02f, x + tx, y + ty, z - 0.02f, x + rx, y + ry, z - 0.02f, x - tx, y - ty, z - 0.02f, x, y, z + 1.0f, (i2 & 1) == 0 ? 2 : 0);
        }
    }

    private static void ring(float[] out, float y, float xa, float xb, float za, float zb) {
        set(out, 0, xa, y, za);
        set(out, 1, xb, y, za);
        set(out, 2, xb, y, zb);
        set(out, 3, xa, y, zb);
    }

    private static void set(float[] out, int corner, float x, float y, float z) {
        out[corner * 3] = x;
        out[(corner * 3) + 1] = y;
        out[(corner * 3) + 2] = z;
    }

    private static float lerp(float a, float b, float t) {
        return a + ((b - a) * t);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/PharaohMesh$Builder.class */
    @Environment(EnvType.CLIENT)
    static final class Builder {
        private int quads;
        private float[] pos = new float[3072];
        private float[] normal = new float[ImGuiHoveredFlags.AllowWhenOverlapped];
        private float[] sweep = new float[2048];
        private byte[] material = new byte[256];
        private float[] tone = new float[256];
        private float shade = 1.0f;
        private final float[] corners = new float[24];

        Builder() {
        }

        void shade(float value) {
            this.shade = value;
        }

        private void ensure() {
            if (this.quads < this.material.length) {
                return;
            }
            int capacity = this.material.length * 2;
            this.pos = Arrays.copyOf(this.pos, capacity * 12);
            this.normal = Arrays.copyOf(this.normal, capacity * 3);
            this.sweep = Arrays.copyOf(this.sweep, capacity * 8);
            this.material = Arrays.copyOf(this.material, capacity);
            this.tone = Arrays.copyOf(this.tone, capacity);
        }

        void quad(float ax, float ay, float az, float bx, float by, float bz, float cx, float cy, float cz, float dx, float dy, float dz, float ox, float oy, float oz, int mat) {
            float ux = cx - ax;
            float uy = cy - ay;
            float uz = cz - az;
            float vx = dx - bx;
            float vy = dy - by;
            float vz = dz - bz;
            float nx = (uy * vz) - (uz * vy);
            float ny = (uz * vx) - (ux * vz);
            float nz = (ux * vy) - (uy * vx);
            float length = (float) Math.sqrt((nx * nx) + (ny * ny) + (nz * nz));
            if (length < 1.0E-6f) {
                return;
            }
            float nx2 = nx / length;
            float ny2 = ny / length;
            float nz2 = nz / length;
            float mx = ((((ax + bx) + cx) + dx) * 0.25f) - ox;
            float my = ((((ay + by) + cy) + dy) * 0.25f) - oy;
            float mz = ((((az + bz) + cz) + dz) * 0.25f) - oz;
            if ((nx2 * mx) + (ny2 * my) + (nz2 * mz) < 0.0f) {
                nx2 = -nx2;
                ny2 = -ny2;
                nz2 = -nz2;
            }
            ensure();
            int p = this.quads * 12;
            vertex(p, ax, ay, az);
            vertex(p + 3, bx, by, bz);
            vertex(p + 6, cx, cy, cz);
            vertex(p + 9, dx, dy, dz);
            this.normal[this.quads * 3] = nx2;
            this.normal[(this.quads * 3) + 1] = ny2;
            this.normal[(this.quads * 3) + 2] = nz2;
            this.material[this.quads] = (byte) mat;
            int hash = this.quads * (-1640531535);
            this.tone[this.quads] = this.shade * (0.955f + (0.065f * (((hash ^ (hash >>> 15)) & 255) / 255.0f)));
            this.quads++;
        }

        private void vertex(int p, float x, float y, float z) {
            this.pos[p] = x * PharaohMesh.PX;
            this.pos[p + 1] = y * PharaohMesh.PX;
            this.pos[p + 2] = z * PharaohMesh.PX;
            double angle = Math.atan2(x, -z) + ((double) (y * PharaohMesh.TWIST));
            int s = (this.quads * 8) + (((p - (this.quads * 12)) / 3) * 2);
            this.sweep[s] = (float) Math.cos(angle);
            this.sweep[s + 1] = (float) Math.sin(angle);
        }

        void prism(float[] c, int mat) {
            float ox = 0.0f;
            float oy = 0.0f;
            float oz = 0.0f;
            for (int i = 0; i < 8; i++) {
                ox += c[i * 3];
                oy += c[(i * 3) + 1];
                oz += c[(i * 3) + 2];
            }
            float ox2 = ox * 0.125f;
            float oy2 = oy * 0.125f;
            float oz2 = oz * 0.125f;
            face(c, 0, 1, 2, 3, ox2, oy2, oz2, mat);
            face(c, 4, 5, 6, 7, ox2, oy2, oz2, mat);
            face(c, 0, 1, 5, 4, ox2, oy2, oz2, mat);
            face(c, 1, 2, 6, 5, ox2, oy2, oz2, mat);
            face(c, 2, 3, 7, 6, ox2, oy2, oz2, mat);
            face(c, 3, 0, 4, 7, ox2, oy2, oz2, mat);
        }

        private void face(float[] c, int a, int b, int d, int e, float ox, float oy, float oz, int mat) {
            quad(c[a * 3], c[(a * 3) + 1], c[(a * 3) + 2], c[b * 3], c[(b * 3) + 1], c[(b * 3) + 2], c[d * 3], c[(d * 3) + 1], c[(d * 3) + 2], c[e * 3], c[(e * 3) + 1], c[(e * 3) + 2], ox, oy, oz, mat);
        }

        void slab(float y0, float xa0, float xb0, float za0, float zb0, float y1, float xa1, float xb1, float za1, float zb1, int mat) {
            float[] c = this.corners;
            PharaohMesh.set(c, 0, xa0, y0, za0);
            PharaohMesh.set(c, 1, xb0, y0, za0);
            PharaohMesh.set(c, 2, xb0, y0, zb0);
            PharaohMesh.set(c, 3, xa0, y0, zb0);
            PharaohMesh.set(c, 4, xa1, y1, za1);
            PharaohMesh.set(c, 5, xb1, y1, za1);
            PharaohMesh.set(c, 6, xb1, y1, zb1);
            PharaohMesh.set(c, 7, xa1, y1, zb1);
            prism(c, mat);
        }

        void box(float x0, float y0, float z0, float x1, float y1, float z1, int mat) {
            slab(y0, x0, x1, z0, z1, y1, x0, x1, z0, z1, mat);
        }

        void stripedSlab(float y0, float xa0, float xb0, float za0, float zb0, float y1, float xa1, float xb1, float za1, float zb1, int bands, int first, int second) {
            float[] from = new float[12];
            float[] to = new float[12];
            PharaohMesh.ring(from, y0, xa0, xb0, za0, zb0);
            PharaohMesh.ring(to, y1, xa1, xb1, za1, zb1);
            striped(from, to, bands, first, second);
        }

        void striped(float[] from, float[] to, int bands, int first, int second) {
            float[] c = this.corners;
            for (int k = 0; k < bands; k++) {
                float t0 = k / bands;
                float t1 = (k + 1) / bands;
                for (int i = 0; i < 12; i++) {
                    c[i] = PharaohMesh.lerp(from[i], to[i], t0);
                    c[12 + i] = PharaohMesh.lerp(from[i], to[i], t1);
                }
                prism(c, (k & 1) == 0 ? first : second);
            }
        }

        void studs(float ax, float ay, float az, float bx, float by, float bz, float cx, float cy, float cz, float dx, float dy, float dz, boolean wide, boolean clearCentre, float ox, float oy, float oz) {
            int count = wide ? 3 : 2;
            for (int i = 0; i < count; i++) {
                float u = wide ? 0.2f + (0.3f * i) : 0.35f + (0.3f * i);
                if (!clearCentre || !wide || i != 1) {
                    stud(ax, ay, az, bx, by, bz, cx, cy, cz, dx, dy, dz, u, 0.5f, ox, oy, oz);
                }
            }
        }

        private void stud(float ax, float ay, float az, float bx, float by, float bz, float cx, float cy, float cz, float dx, float dy, float dz, float u, float v, float ox, float oy, float oz) {
            float px = PharaohMesh.lerp(PharaohMesh.lerp(ax, bx, u), PharaohMesh.lerp(dx, cx, u), v);
            float py = PharaohMesh.lerp(PharaohMesh.lerp(ay, by, u), PharaohMesh.lerp(dy, cy, u), v);
            float pz = PharaohMesh.lerp(PharaohMesh.lerp(az, bz, u), PharaohMesh.lerp(dz, cz, u), v);
            float ux = bx - ax;
            float uy = by - ay;
            float uz = bz - az;
            float ul = (float) Math.sqrt((ux * ux) + (uy * uy) + (uz * uz));
            float vx = dx - ax;
            float vy = dy - ay;
            float vz = dz - az;
            float vl = (float) Math.sqrt((vx * vx) + (vy * vy) + (vz * vz));
            if (ul < 1.0E-5f || vl < 1.0E-5f) {
                return;
            }
            float ux2 = ux / ul;
            float uy2 = uy / ul;
            float uz2 = uz / ul;
            float vx2 = vx / vl;
            float vy2 = vy / vl;
            float vz2 = vz / vl;
            float nx = (uy2 * vz2) - (uz2 * vy2);
            float ny = (uz2 * vx2) - (ux2 * vz2);
            float nz = (ux2 * vy2) - (uy2 * vx2);
            float nl = (float) Math.sqrt((nx * nx) + (ny * ny) + (nz * nz));
            if (nl < 1.0E-5f) {
                return;
            }
            float nx2 = nx / nl;
            float ny2 = ny / nl;
            float nz2 = nz / nl;
            if ((nx2 * (px - ox)) + (ny2 * (py - oy)) + (nz2 * (pz - oz)) < 0.0f) {
                nx2 = -nx2;
                ny2 = -ny2;
                nz2 = -nz2;
            }
            float px2 = px + (nx2 * 0.07f);
            float py2 = py + (ny2 * 0.07f);
            float pz2 = pz + (nz2 * 0.07f);
            quad((px2 - (ux2 * 0.32f)) - (vx2 * 0.32f), (py2 - (uy2 * 0.32f)) - (vy2 * 0.32f), (pz2 - (uz2 * 0.32f)) - (vz2 * 0.32f), (px2 + (ux2 * 0.32f)) - (vx2 * 0.32f), (py2 + (uy2 * 0.32f)) - (vy2 * 0.32f), (pz2 + (uz2 * 0.32f)) - (vz2 * 0.32f), px2 + (ux2 * 0.32f) + (vx2 * 0.32f), py2 + (uy2 * 0.32f) + (vy2 * 0.32f), pz2 + (uz2 * 0.32f) + (vz2 * 0.32f), (px2 - (ux2 * 0.32f)) + (vx2 * 0.32f), (py2 - (uy2 * 0.32f)) + (vy2 * 0.32f), (pz2 - (uz2 * 0.32f)) + (vz2 * 0.32f), px2 - nx2, py2 - ny2, pz2 - nz2, 0);
        }

        void tube(float[] path, float[] half, int samples, int mat) {
            float[] c = this.corners;
            for (int i = 0; i + 1 < samples; i++) {
                section(c, 0, path, half, samples, i);
                section(c, 4, path, half, samples, i + 1);
                prism(c, i + 2 == samples ? 3 : mat);
            }
        }

        private static void section(float[] out, int corner, float[] path, float[] half, int samples, int i) {
            int prev = Math.max(0, i - 1);
            int next = Math.min(samples - 1, i + 1);
            float tx = path[next * 3] - path[prev * 3];
            float ty = path[(next * 3) + 1] - path[(prev * 3) + 1];
            float length = (float) Math.sqrt((tx * tx) + (ty * ty));
            float nx = length > 1.0E-5f ? (-ty) / length : 1.0f;
            float ny = length > 1.0E-5f ? tx / length : 0.0f;
            float h = half[i];
            float x = path[i * 3];
            float y = path[(i * 3) + 1];
            float z = path[(i * 3) + 2];
            PharaohMesh.set(out, corner, x + (nx * h), y + (ny * h), z - h);
            PharaohMesh.set(out, corner + 1, x + (nx * h), y + (ny * h), z + h);
            PharaohMesh.set(out, corner + 2, x - (nx * h), y - (ny * h), z + h);
            PharaohMesh.set(out, corner + 3, x - (nx * h), y - (ny * h), z - h);
        }

        void disc(float cx, float cy, float cz, float radius, float depth, int segments, int faceMat, int rimMat) {
            float zf = cz - depth;
            float zb = cz + depth;
            for (int i = 0; i < segments; i++) {
                double a0 = (6.283185307179586d * ((double) i)) / ((double) segments);
                double a1 = (6.283185307179586d * ((double) (i + 1))) / ((double) segments);
                float x0 = cx + (radius * ((float) Math.cos(a0)));
                float y0 = cy + (radius * ((float) Math.sin(a0)));
                float x1 = cx + (radius * ((float) Math.cos(a1)));
                float y1 = cy + (radius * ((float) Math.sin(a1)));
                quad(cx, cy, zf, x0, y0, zf, x1, y1, zf, x1, y1, zf, cx, cy, cz, faceMat);
                quad(cx, cy, zb, x0, y0, zb, x1, y1, zb, x1, y1, zb, cx, cy, cz, faceMat);
                quad(x0, y0, zf, x1, y1, zf, x1, y1, zb, x0, y0, zb, cx, cy, cz, rimMat);
            }
        }

        void annulus(float cx, float cy, float z, float inner, float outer, int segments, int mat, float behind) {
            for (int i = 0; i < segments; i++) {
                double a0 = (6.283185307179586d * ((double) i)) / ((double) segments);
                double a1 = (6.283185307179586d * ((double) (i + 1))) / ((double) segments);
                float c0 = (float) Math.cos(a0);
                float s0 = (float) Math.sin(a0);
                float c1 = (float) Math.cos(a1);
                float s1 = (float) Math.sin(a1);
                quad(cx + (c0 * inner), cy + (s0 * inner), z, cx + (c0 * outer), cy + (s0 * outer), z, cx + (c1 * outer), cy + (s1 * outer), z, cx + (c1 * inner), cy + (s1 * inner), z, cx, cy, behind, mat);
            }
        }
    }
}
