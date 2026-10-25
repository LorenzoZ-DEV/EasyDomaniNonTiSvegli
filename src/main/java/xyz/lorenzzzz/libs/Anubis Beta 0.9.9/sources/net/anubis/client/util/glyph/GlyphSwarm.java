package net.anubis.client.util.glyph;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_3532;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/glyph/GlyphSwarm.class */
@Environment(EnvType.CLIENT)
public final class GlyphSwarm {
    public static final byte ANKH = 0;
    public static final byte SCARAB = 1;
    public static final byte GRAIN = 2;
    public static final byte SPARK = 3;
    public static final int WORST_QUADS = (Math.max(Math.max(GlyphShapes.ANKH.length, GlyphShapes.SCARAB.length), Math.max(GlyphShapes.SPARK.length, Math.max(GlyphShapes.GRAIN.length, GlyphShapes.DIAMOND.length))) + GlyphShapes.OCTAGON.length) / 8;
    private static final double CULL_DISTANCE_SQR = 9216.0d;
    private static final double DETAIL_DISTANCE_SQR = 484.0d;
    private static final float HALO_SCALE = 2.3f;
    private static final float HALO_ALPHA = 0.17f;
    private final int capacity;
    private final double[] x;
    private final double[] y;
    private final double[] z;
    private final float[] vx;
    private final float[] vy;
    private final float[] vz;
    private final float[] age;
    private final float[] life;
    private final float[] size;
    private final float[] rot;
    private final float[] spin;
    private final float[] seed;
    private final byte[] kind;
    private final int[] color;
    private int count;

    public GlyphSwarm(int capacity) {
        this.capacity = capacity;
        this.x = new double[capacity];
        this.y = new double[capacity];
        this.z = new double[capacity];
        this.vx = new float[capacity];
        this.vy = new float[capacity];
        this.vz = new float[capacity];
        this.age = new float[capacity];
        this.life = new float[capacity];
        this.size = new float[capacity];
        this.rot = new float[capacity];
        this.spin = new float[capacity];
        this.seed = new float[capacity];
        this.kind = new byte[capacity];
        this.color = new int[capacity];
    }

    public int count() {
        return this.count;
    }

    public void clear() {
        this.count = 0;
    }

    public void spawn(byte type, double px, double py, double pz, float velX, float velY, float velZ, float lifetime, float scale, float angle, float turn, float noise, int rgb, int limit) {
        int slot;
        int cap = Math.min(this.capacity, Math.max(1, limit));
        if (this.count < cap) {
            int i = this.count;
            this.count = i + 1;
            slot = i;
        } else {
            slot = oldest();
        }
        this.x[slot] = px;
        this.y[slot] = py;
        this.z[slot] = pz;
        this.vx[slot] = velX;
        this.vy[slot] = velY;
        this.vz[slot] = velZ;
        this.age[slot] = 0.0f;
        this.life[slot] = Math.max(0.05f, lifetime);
        this.size[slot] = scale;
        this.rot[slot] = angle;
        this.spin[slot] = turn;
        this.seed[slot] = noise;
        this.kind[slot] = type;
        this.color[slot] = rgb;
    }

    public void trim(int limit) {
        while (this.count > Math.max(0, limit)) {
            remove(oldest());
        }
    }

    public void step(float dt) {
        float drag;
        float lift;
        if (dt <= 0.0f) {
            return;
        }
        for (int i = this.count - 1; i >= 0; i--) {
            float a = this.age[i] + dt;
            if (a >= this.life[i]) {
                remove(i);
            } else {
                this.age[i] = a;
                switch (this.kind[i]) {
                    case 0:
                        drag = 1.6f;
                        lift = 0.7f;
                        float[] fArr = this.vx;
                        int i2 = i;
                        fArr[i2] = fArr[i2] + (class_3532.method_15374((a * HALO_SCALE) + (this.seed[i] * 6.0f)) * 0.35f * dt);
                        float[] fArr2 = this.vz;
                        int i3 = i;
                        fArr2[i3] = fArr2[i3] + (class_3532.method_15362((a * 1.9f) + (this.seed[i] * 6.0f)) * 0.35f * dt);
                        break;
                    case 1:
                        drag = 0.8f;
                        lift = -1.1f;
                        float turn = (this.seed[i] > 0.5f ? 1.8f : -1.8f) * dt;
                        float cos = class_3532.method_15362(turn);
                        float sin = class_3532.method_15374(turn);
                        float nx = (this.vx[i] * cos) - (this.vz[i] * sin);
                        this.vz[i] = (this.vx[i] * sin) + (this.vz[i] * cos);
                        this.vx[i] = nx;
                        break;
                    case 2:
                        drag = 0.9f;
                        lift = -6.5f;
                        break;
                    default:
                        drag = 3.4f;
                        lift = -1.6f;
                        break;
                }
                float keep = Math.max(0.0f, 1.0f - (drag * dt));
                float[] fArr3 = this.vx;
                int i4 = i;
                fArr3[i4] = fArr3[i4] * keep;
                this.vy[i] = (this.vy[i] * keep) + (lift * dt);
                float[] fArr4 = this.vz;
                int i5 = i;
                fArr4[i5] = fArr4[i5] * keep;
                double[] dArr = this.x;
                int i6 = i;
                dArr[i6] = dArr[i6] + ((double) (this.vx[i] * dt));
                double[] dArr2 = this.y;
                int i7 = i;
                dArr2[i7] = dArr2[i7] + ((double) (this.vy[i] * dt));
                double[] dArr3 = this.z;
                int i8 = i;
                dArr3[i8] = dArr3[i8] + ((double) (this.vz[i] * dt));
                float[] fArr5 = this.rot;
                int i9 = i;
                fArr5[i9] = fArr5[i9] + (this.spin[i] * dt);
            }
        }
    }

    public void draw(GlyphCanvas canvas, float opacity, boolean halo) {
        float[] shape;
        for (int i = 0; i < this.count; i++) {
            double px = this.x[i];
            double py = this.y[i];
            double pz = this.z[i];
            if (canvas.depth(px, py, pz) >= 0.05d) {
                double distSqr = canvas.distanceSqr(px, py, pz);
                if (distSqr <= CULL_DISTANCE_SQR) {
                    float t = this.age[i] / this.life[i];
                    float fade = Math.min(1.0f, t * 9.0f) * (1.0f - (t * t));
                    float scale = this.size[i];
                    float squash = 1.0f;
                    switch (this.kind[i]) {
                        case 0:
                            shape = GlyphShapes.ANKH;
                            scale *= bloom(Math.min(1.0f, t * 5.0f)) * (1.0f - (0.35f * t));
                            break;
                        case 1:
                            shape = GlyphShapes.SCARAB;
                            squash = 0.72f + (0.28f * Math.abs(class_3532.method_15374((this.age[i] * 15.0f) + (this.seed[i] * 9.0f))));
                            break;
                        case 2:
                            shape = GlyphShapes.GRAIN;
                            break;
                        default:
                            shape = GlyphShapes.SPARK;
                            scale *= 1.0f - (0.6f * t);
                            fade *= 0.75f + (0.25f * class_3532.method_15374((this.age[i] * 23.0f) + (this.seed[i] * 12.0f)));
                            break;
                    }
                    float alpha = fade * opacity;
                    if (alpha >= 0.012f) {
                        int rgb = this.color[i];
                        boolean detailed = distSqr < DETAIL_DISTANCE_SQR;
                        if (halo && detailed) {
                            canvas.sprite(GlyphShapes.OCTAGON, px, py, pz, scale * HALO_SCALE, 0.0f, GlyphTint.alpha(rgb, alpha * HALO_ALPHA));
                        }
                        if (!detailed && shape != GlyphShapes.GRAIN) {
                            shape = GlyphShapes.DIAMOND;
                        }
                        canvas.sprite(shape, px, py, pz, scale * squash, scale, this.rot[i], GlyphTint.alpha(rgb, alpha));
                    }
                }
            }
        }
    }

    private static float bloom(float t) {
        float u = t - 1.0f;
        return 1.0f + (2.2f * u * u * u) + (1.2f * u * u);
    }

    private int oldest() {
        int best = 0;
        float worst = -1.0f;
        for (int i = 0; i < this.count; i++) {
            float progress = this.age[i] / this.life[i];
            if (progress > worst) {
                worst = progress;
                best = i;
            }
        }
        return best;
    }

    private void remove(int i) {
        int last = this.count - 1;
        this.count = last;
        if (i == last) {
            return;
        }
        this.x[i] = this.x[last];
        this.y[i] = this.y[last];
        this.z[i] = this.z[last];
        this.vx[i] = this.vx[last];
        this.vy[i] = this.vy[last];
        this.vz[i] = this.vz[last];
        this.age[i] = this.age[last];
        this.life[i] = this.life[last];
        this.size[i] = this.size[last];
        this.rot[i] = this.rot[last];
        this.spin[i] = this.spin[last];
        this.seed[i] = this.seed[last];
        this.kind[i] = this.kind[last];
        this.color[i] = this.color[last];
    }
}
