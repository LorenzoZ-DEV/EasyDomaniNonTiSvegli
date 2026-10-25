package net.anubis.client.module.impl.render;

import java.util.Arrays;
import net.anubis.client.util.glyph.GlyphShapes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/RelicGlyphs.class */
@Environment(EnvType.CLIENT)
final class RelicGlyphs {
    static final float[] ANKH = GlyphShapes.ANKH;
    static final float[] EYE = join(GlyphShapes.EYE, GlyphShapes.EYE_PUPIL);
    static final float[] SCARAB = GlyphShapes.SCARAB;
    static final float[] DJED = djed();
    static final float[] FEATHER = feather();
    static final float[] WAS = was();
    static final float[] DOLLAR = dollar();
    static final float[] EURO = euro();
    static final float[] BITCOIN = bitcoin();

    private RelicGlyphs() {
    }

    private static float[] djed() {
        Sketch s = new Sketch();
        s.quad(-0.36f, -1.0f, 0.36f, -1.0f, 0.32f, -0.88f, -0.32f, -0.88f);
        s.quad(-0.2f, -0.88f, 0.2f, -0.88f, 0.15f, 0.2f, -0.15f, 0.2f);
        int k = 0;
        while (k < 4) {
            float low = 0.2f + (k * 0.2f);
            float high = low + 0.12f;
            float reach = k == 3 ? 0.48f : 0.44f;
            s.quad(-reach, low, reach, low, reach, high, -reach, high);
            if (k < 3) {
                s.quad(-0.15f, high, 0.15f, high, 0.15f, high + 0.08f, -0.15f, high + 0.08f);
            }
            k++;
        }
        s.quad(-0.36f, 0.92f, 0.36f, 0.92f, 0.26f, 1.0f, -0.26f, 1.0f);
        return s.build();
    }

    private static float[] feather() {
        float[] spine = {0.0f, -0.72f, 0.0f, -0.54f, 0.01f, -0.3f, 0.02f, 0.0f, 0.03f, 0.26f, 0.06f, 0.48f, 0.11f, 0.64f, 0.19f, 0.77f, 0.3f, 0.86f, 0.42f, 0.9f, 0.53f, 0.87f, 0.6f, 0.8f};
        float[] edge = {0.05f, 0.21f, 0.28f, 0.3f, 0.29f, 0.26f, 0.22f, 0.17f, 0.13f, 0.09f, 0.05f, 0.02f};
        int n = edge.length;
        float[] rachis = new float[n];
        float[] outer = new float[n];
        float[] inner = new float[n];
        for (int i = 0; i < n; i++) {
            rachis[i] = Math.min(0.026f, edge[i] * 0.45f);
            outer[i] = -edge[i];
            inner[i] = -rachis[i];
        }
        Sketch s = new Sketch();
        s.quad(-0.04f, -1.0f, 0.04f, -1.0f, 0.04f, -0.7f, -0.04f, -0.7f);
        s.band(spine, rachis, edge);
        s.band(spine, outer, inner);
        return s.build();
    }

    private static float[] was() {
        Sketch s = new Sketch();
        s.quad(-0.08f, -0.7f, 0.08f, -0.7f, 0.08f, 0.5f, -0.08f, 0.5f);
        s.quad(-0.3f, -1.0f, -0.16f, -1.0f, 0.03f, -0.7f, -0.08f, -0.7f);
        s.quad(0.16f, -1.0f, 0.3f, -1.0f, 0.08f, -0.7f, -0.03f, -0.7f);
        s.quad(-0.08f, 0.5f, 0.08f, 0.5f, 0.26f, 0.78f, 0.06f, 0.86f);
        s.stroke(0.15f, 0.12f, 0.8f, 0.36f, 0.84f, 0.62f, 0.7f);
        s.stroke(0.1f, 0.08f, 0.82f, -0.02f, 1.0f);
        return s.build();
    }

    private static float[] dollar() {
        float[] upper = arc(0.0f, 0.34f, 0.34f, 28.0f, 270.0f, 16);
        float[] lower = arc(0.0f, -0.34f, 0.34f, 90.0f, -152.0f, 16);
        Sketch s = new Sketch();
        s.stroke(0.17f, join(upper, Arrays.copyOfRange(lower, 2, lower.length)));
        s.quad(-0.055f, -0.98f, 0.055f, -0.98f, 0.055f, 0.98f, -0.055f, 0.98f);
        return s.build();
    }

    private static float[] euro() {
        Sketch s = new Sketch();
        s.stroke(0.17f, arc(0.1f, 0.0f, 0.62f, 48.0f, 312.0f, 26));
        s.quad(-0.82f, 0.06f, 0.16f, 0.06f, 0.16f, 0.2f, -0.82f, 0.2f);
        s.quad(-0.82f, -0.22f, 0.1f, -0.22f, 0.1f, -0.08f, -0.82f, -0.08f);
        return s.build();
    }

    private static float[] bitcoin() {
        Sketch b = new Sketch();
        b.quad(-0.3f, -0.56f, -0.17f, -0.56f, -0.17f, 0.56f, -0.3f, 0.56f);
        b.stroke(0.13f, join(new float[]{-0.24f, 0.5f}, arc(0.06f, 0.27f, 0.23f, 90.0f, -90.0f, 12), new float[]{-0.24f, 0.04f}));
        b.stroke(0.13f, join(new float[]{-0.24f, 0.04f}, arc(0.1f, -0.24f, 0.28f, 90.0f, -90.0f, 12), new float[]{-0.24f, -0.52f}));
        b.quad(-0.21f, 0.5f, -0.13f, 0.5f, -0.13f, 0.74f, -0.21f, 0.74f);
        b.quad(-0.03f, 0.5f, 0.05f, 0.5f, 0.05f, 0.74f, -0.03f, 0.74f);
        b.quad(-0.21f, -0.74f, -0.13f, -0.74f, -0.13f, -0.5f, -0.21f, -0.5f);
        b.quad(-0.03f, -0.74f, 0.05f, -0.74f, 0.05f, -0.5f, -0.03f, -0.5f);
        float[] mark = b.build();
        float c = (float) Math.cos(Math.toRadians(-14.0d));
        float sn = (float) Math.sin(Math.toRadians(-14.0d));
        for (int i = 0; i < mark.length; i += 2) {
            float x = mark[i] + 0.04f;
            float y = mark[i + 1];
            mark[i] = (x * c) - (y * sn);
            mark[i + 1] = (x * sn) + (y * c);
        }
        Sketch rim = new Sketch();
        rim.stroke(0.1f, arc(0.0f, 0.0f, 0.92f, 0.0f, 360.0f, 44));
        return join(rim.build(), mark);
    }

    private static float[] arc(float cx, float cy, float radius, float from, float to, int steps) {
        float[] points = new float[(steps + 1) * 2];
        for (int i = 0; i <= steps; i++) {
            double angle = Math.toRadians(from + (((to - from) * i) / steps));
            points[i * 2] = cx + (radius * ((float) Math.cos(angle)));
            points[(i * 2) + 1] = cy + (radius * ((float) Math.sin(angle)));
        }
        return points;
    }

    private static float[] join(float[]... parts) {
        int length = 0;
        for (float[] fArr : parts) {
            length += fArr.length;
        }
        float[] out = new float[length];
        int at = 0;
        for (float[] part : parts) {
            System.arraycopy(part, 0, out, at, part.length);
            at += part.length;
        }
        return out;
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/RelicGlyphs$Sketch.class */
    @Environment(EnvType.CLIENT)
    private static final class Sketch {
        private float[] data = new float[64];
        private int size;

        private Sketch() {
        }

        void quad(float ax, float ay, float bx, float by, float cx, float cy, float dx, float dy) {
            if (this.size + 8 > this.data.length) {
                this.data = Arrays.copyOf(this.data, this.data.length * 2);
            }
            float[] fArr = this.data;
            int i = this.size;
            this.size = i + 1;
            fArr[i] = ax;
            float[] fArr2 = this.data;
            int i2 = this.size;
            this.size = i2 + 1;
            fArr2[i2] = ay;
            float[] fArr3 = this.data;
            int i3 = this.size;
            this.size = i3 + 1;
            fArr3[i3] = bx;
            float[] fArr4 = this.data;
            int i4 = this.size;
            this.size = i4 + 1;
            fArr4[i4] = by;
            float[] fArr5 = this.data;
            int i5 = this.size;
            this.size = i5 + 1;
            fArr5[i5] = cx;
            float[] fArr6 = this.data;
            int i6 = this.size;
            this.size = i6 + 1;
            fArr6[i6] = cy;
            float[] fArr7 = this.data;
            int i7 = this.size;
            this.size = i7 + 1;
            fArr7[i7] = dx;
            float[] fArr8 = this.data;
            int i8 = this.size;
            this.size = i8 + 1;
            fArr8[i8] = dy;
        }

        void stroke(float width, float... points) {
            float[] low = new float[points.length / 2];
            float[] high = new float[points.length / 2];
            Arrays.fill(low, (-width) * 0.5f);
            Arrays.fill(high, width * 0.5f);
            band(points, low, high);
        }

        void band(float[] points, float[] low, float[] high) {
            int n = points.length / 2;
            float[] nx = new float[n];
            float[] ny = new float[n];
            for (int i = 0; i < n; i++) {
                int a = Math.max(0, i - 1);
                int b = Math.min(n - 1, i + 1);
                float dx = points[b * 2] - points[a * 2];
                float dy = points[(b * 2) + 1] - points[(a * 2) + 1];
                float length = (float) Math.sqrt((dx * dx) + (dy * dy));
                nx[i] = (-dy) / length;
                ny[i] = dx / length;
            }
            int i2 = 0;
            for (int j = 1; j < n; j++) {
                float ax = points[i2 * 2];
                float ay = points[(i2 * 2) + 1];
                float bx = points[j * 2];
                float by = points[(j * 2) + 1];
                quad(ax + (nx[i2] * low[i2]), ay + (ny[i2] * low[i2]), bx + (nx[j] * low[j]), by + (ny[j] * low[j]), bx + (nx[j] * high[j]), by + (ny[j] * high[j]), ax + (nx[i2] * high[i2]), ay + (ny[i2] * high[i2]));
                i2++;
            }
        }

        float[] build() {
            return Arrays.copyOf(this.data, this.size);
        }
    }
}
