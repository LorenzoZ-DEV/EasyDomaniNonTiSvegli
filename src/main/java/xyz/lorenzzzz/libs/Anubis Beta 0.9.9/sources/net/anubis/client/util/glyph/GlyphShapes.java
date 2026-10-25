package net.anubis.client.util.glyph;

import java.util.Arrays;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/glyph/GlyphShapes.class */
@Environment(EnvType.CLIENT)
public final class GlyphShapes {
    public static final float[] ANKH = ankh();
    public static final float[] SCARAB = scarab();
    public static final float[] SPARK = spark();
    public static final float[] GRAIN = grain();
    public static final float[] DIAMOND = diamond();
    public static final float[] DISK = new Builder().disk(0.0f, 0.0f, 1.0f, 1.0f, 12).build();
    public static final float[] OCTAGON = new Builder().disk(0.0f, 0.0f, 1.0f, 1.0f, 8).build();
    public static final float[] HALO = new Builder().arc(0.0f, 0.0f, 0.84f, 0.84f, 1.0f, 1.0f, 0.0f, 6.2831855f, 20).build();
    public static final float[] EYE = horusEye();
    public static final float[] EYE_PUPIL = new Builder().offset(-0.14f, 0.04f).disk(-0.08f, 0.07f, 0.19f, 0.19f, 8).build();

    private GlyphShapes() {
    }

    private static float[] ankh() {
        Builder b = new Builder();
        b.quad(-0.17f, -1.0f, 0.17f, -1.0f, 0.09f, 0.06f, -0.09f, 0.06f);
        b.quad(-0.56f, -0.02f, -0.09f, 0.04f, -0.09f, 0.2f, -0.56f, 0.26f);
        b.quad(-0.09f, 0.04f, 0.09f, 0.04f, 0.09f, 0.2f, -0.09f, 0.2f);
        b.quad(0.09f, 0.04f, 0.56f, -0.02f, 0.56f, 0.26f, 0.09f, 0.2f);
        b.arc(0.0f, 0.6f, 0.19f, 0.28f, 0.3f, 0.42f, -1.5707964f, 4.712389f, 10);
        return b.build();
    }

    private static float[] scarab() {
        Builder b = new Builder();
        for (int i = 0; i < 5; i++) {
            double a0 = 1.5707963267948966d + ((3.141592653589793d * ((double) i)) / ((double) 5));
            double a1 = 1.5707963267948966d + ((3.141592653589793d * ((double) (i + 1))) / ((double) 5));
            float y0 = (-0.12f) + (((float) Math.sin(a0)) * 0.78f);
            float y1 = (-0.12f) + (((float) Math.sin(a1)) * 0.78f);
            float x0 = (((float) Math.cos(a0)) * 0.56f) - 0.035f;
            float x1 = (((float) Math.cos(a1)) * 0.56f) - 0.035f;
            b.quad(-0.035f, y0, x0, y0, x1, y1, -0.035f, y1);
            b.quad(0.035f, (-y0) + (2.0f * (-0.12f)), -x0, (-y0) + (2.0f * (-0.12f)), -x1, (-y1) + (2.0f * (-0.12f)), 0.035f, (-y1) + (2.0f * (-0.12f)));
        }
        b.quad(-0.44f, 0.62f, 0.44f, 0.62f, 0.33f, 0.86f, -0.33f, 0.86f);
        b.quad(-0.24f, 0.86f, 0.24f, 0.86f, 0.13f, 1.0f, -0.13f, 1.0f);
        b.stroke(0.055f, -0.34f, 0.7f, -0.62f, 0.8f, -0.74f, 0.98f);
        b.stroke(0.055f, 0.34f, 0.7f, 0.62f, 0.8f, 0.74f, 0.98f);
        b.stroke(0.055f, -0.5f, 0.12f, -0.82f, 0.2f, -0.92f, 0.04f);
        b.stroke(0.055f, 0.5f, 0.12f, 0.82f, 0.2f, 0.92f, 0.04f);
        b.stroke(0.055f, -0.44f, -0.52f, -0.74f, -0.72f, -0.8f, -0.96f);
        b.stroke(0.055f, 0.44f, -0.52f, 0.74f, -0.72f, 0.8f, -0.96f);
        return b.build();
    }

    private static float[] spark() {
        Builder b = new Builder();
        b.quad(0.0f, -1.0f, 0.15f, 0.0f, 0.0f, 1.0f, -0.15f, 0.0f);
        b.quad(-0.62f, 0.0f, 0.0f, -0.13f, 0.62f, 0.0f, 0.0f, 0.13f);
        return b.build();
    }

    private static float[] grain() {
        return new Builder().quad(-0.8f, -0.62f, 0.72f, -0.88f, 0.9f, 0.46f, -0.52f, 0.82f).build();
    }

    private static float[] diamond() {
        return new Builder().quad(0.0f, -1.0f, 1.0f, 0.0f, 0.0f, 1.0f, -1.0f, 0.0f).build();
    }

    private static float[] horusEye() {
        Builder b = new Builder().offset(-0.14f, 0.04f);
        float[] upper = curve(-0.96f, 0.0f, -0.08f, 0.64f, 0.76f, 0.02f, 9);
        b.stroke(0.1f, append(upper, 1.3f, 0.02f));
        b.stroke(0.08f, curve(-0.96f, 0.0f, -0.06f, -0.46f, 0.76f, 0.02f, 8));
        b.stroke(0.12f, curve(-1.0f, 0.46f, 0.02f, 0.98f, 1.28f, 0.52f, 9));
        b.stroke(0.09f, -0.36f, -0.24f, -0.38f, -0.6f, -0.44f, -0.96f);
        b.stroke(0.08f, 0.06f, -0.22f, 0.3f, -0.44f, 0.6f, -0.6f, 0.85f, -0.72f, 0.98f, -0.86f, 0.95f, -0.99f, 0.83f, -1.03f, 0.74f, -0.96f, 0.76f, -0.87f, 0.85f, -0.85f);
        return b.build();
    }

    private static float[] curve(float x0, float y0, float cx, float cy, float x1, float y1, int steps) {
        float[] out = new float[(steps + 1) * 2];
        for (int i = 0; i <= steps; i++) {
            float t = i / steps;
            float s = 1.0f - t;
            out[i * 2] = (s * s * x0) + (2.0f * s * t * cx) + (t * t * x1);
            out[(i * 2) + 1] = (s * s * y0) + (2.0f * s * t * cy) + (t * t * y1);
        }
        return out;
    }

    private static float[] append(float[] points, float x, float y) {
        float[] out = Arrays.copyOf(points, points.length + 2);
        out[points.length] = x;
        out[points.length + 1] = y;
        return out;
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/glyph/GlyphShapes$Builder.class */
    @Environment(EnvType.CLIENT)
    private static final class Builder {
        private float[] data = new float[64];
        private int size;
        private float offsetX;
        private float offsetY;

        private Builder() {
        }

        Builder offset(float x, float y) {
            this.offsetX = x;
            this.offsetY = y;
            return this;
        }

        Builder quad(float ax, float ay, float bx, float by, float cx, float cy, float dx, float dy) {
            if (this.size + 8 > this.data.length) {
                this.data = Arrays.copyOf(this.data, this.data.length * 2);
            }
            float[] fArr = this.data;
            int i = this.size;
            this.size = i + 1;
            fArr[i] = ax + this.offsetX;
            float[] fArr2 = this.data;
            int i2 = this.size;
            this.size = i2 + 1;
            fArr2[i2] = ay + this.offsetY;
            float[] fArr3 = this.data;
            int i3 = this.size;
            this.size = i3 + 1;
            fArr3[i3] = bx + this.offsetX;
            float[] fArr4 = this.data;
            int i4 = this.size;
            this.size = i4 + 1;
            fArr4[i4] = by + this.offsetY;
            float[] fArr5 = this.data;
            int i5 = this.size;
            this.size = i5 + 1;
            fArr5[i5] = cx + this.offsetX;
            float[] fArr6 = this.data;
            int i6 = this.size;
            this.size = i6 + 1;
            fArr6[i6] = cy + this.offsetY;
            float[] fArr7 = this.data;
            int i7 = this.size;
            this.size = i7 + 1;
            fArr7[i7] = dx + this.offsetX;
            float[] fArr8 = this.data;
            int i8 = this.size;
            this.size = i8 + 1;
            fArr8[i8] = dy + this.offsetY;
            return this;
        }

        Builder arc(float cx, float cy, float innerX, float innerY, float outerX, float outerY, float from, float to, int steps) {
            for (int i = 0; i < steps; i++) {
                double a0 = ((double) from) + (((double) ((to - from) * i)) / ((double) steps));
                double a1 = ((double) from) + (((double) ((to - from) * (i + 1))) / ((double) steps));
                float c0 = (float) Math.cos(a0);
                float s0 = (float) Math.sin(a0);
                float c1 = (float) Math.cos(a1);
                float s1 = (float) Math.sin(a1);
                quad(cx + (c0 * innerX), cy + (s0 * innerY), cx + (c0 * outerX), cy + (s0 * outerY), cx + (c1 * outerX), cy + (s1 * outerY), cx + (c1 * innerX), cy + (s1 * innerY));
            }
            return this;
        }

        Builder disk(float cx, float cy, float rx, float ry, int sides) {
            float[] ring = new float[sides * 2];
            for (int i = 0; i < sides; i++) {
                double a = ((6.283185307179586d * ((double) i)) / ((double) sides)) + (3.141592653589793d / ((double) sides));
                ring[i * 2] = cx + (((float) Math.cos(a)) * rx);
                ring[(i * 2) + 1] = cy + (((float) Math.sin(a)) * ry);
            }
            for (int i2 = 1; i2 + 1 < sides; i2 += 2) {
                int j = Math.min(i2 + 2, sides - 1);
                quad(ring[0], ring[1], ring[i2 * 2], ring[(i2 * 2) + 1], ring[(i2 + 1) * 2], ring[((i2 + 1) * 2) + 1], ring[j * 2], ring[(j * 2) + 1]);
            }
            return this;
        }

        Builder stroke(float width, float... points) {
            int n = points.length / 2;
            if (n < 2) {
                return this;
            }
            float half = width * 0.5f;
            float[] nx = new float[n];
            float[] ny = new float[n];
            for (int i = 0; i < n; i++) {
                float sx = 0.0f;
                float sy = 0.0f;
                float px = 0.0f;
                float py = 0.0f;
                if (i > 0) {
                    float dx = points[i * 2] - points[(i * 2) - 2];
                    float dy = points[(i * 2) + 1] - points[(i * 2) - 1];
                    float len = (float) Math.sqrt((dx * dx) + (dy * dy));
                    if (len > 1.0E-6f) {
                        px = (-dy) / len;
                        py = dx / len;
                        sx = 0.0f + px;
                        sy = 0.0f + py;
                    }
                }
                if (i < n - 1) {
                    float dx2 = points[(i * 2) + 2] - points[i * 2];
                    float dy2 = points[(i * 2) + 3] - points[(i * 2) + 1];
                    float len2 = (float) Math.sqrt((dx2 * dx2) + (dy2 * dy2));
                    if (len2 > 1.0E-6f) {
                        sx += (-dy2) / len2;
                        sy += dx2 / len2;
                        if (i == 0) {
                            px = (-dy2) / len2;
                            py = dx2 / len2;
                        }
                    }
                }
                float len3 = (float) Math.sqrt((sx * sx) + (sy * sy));
                if (len3 < 1.0E-6f) {
                    sx = px;
                    sy = py;
                    len3 = 1.0f;
                }
                float sx2 = sx / len3;
                float sy2 = sy / len3;
                float dot = Math.max(0.5f, (sx2 * px) + (sy2 * py));
                nx[i] = (sx2 * half) / dot;
                ny[i] = (sy2 * half) / dot;
            }
            for (int i2 = 0; i2 < n - 1; i2++) {
                float ax = points[i2 * 2];
                float ay = points[(i2 * 2) + 1];
                float bx = points[(i2 * 2) + 2];
                float by = points[(i2 * 2) + 3];
                quad(ax - nx[i2], ay - ny[i2], bx - nx[i2 + 1], by - ny[i2 + 1], bx + nx[i2 + 1], by + ny[i2 + 1], ax + nx[i2], ay + ny[i2]);
            }
            return this;
        }

        float[] build() {
            return Arrays.copyOf(this.data, this.size);
        }
    }
}
