package net.anubis.client.module.impl.render.character.gltf;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/gltf/ShapeCut.class */
@Environment(EnvType.CLIENT)
final class ShapeCut {
    private static final int BANDS = 160;
    private static final int BRIDGE = 5;
    private static final int NO_TRUNK = -2;
    private final PosedMesh mesh;
    private final float height;
    private final float step;
    private final float gap;
    private final List<float[]>[] spans = new List[160];
    private final float[][][] intervals;
    private final int[][] labels;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/gltf/ShapeCut$Arm.class */
    @Environment(EnvType.CLIENT)
    private static final class Arm extends Record {
        private final int from;
        private final int to;
        private final float[] edge;

        private Arm(int from, int to, float[] edge) {
            this.from = from;
            this.to = to;
            this.edge = edge;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Arm.class), Arm.class, "from;to;edge", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/ShapeCut$Arm;->from:I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/ShapeCut$Arm;->to:I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/ShapeCut$Arm;->edge:[F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Arm.class), Arm.class, "from;to;edge", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/ShapeCut$Arm;->from:I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/ShapeCut$Arm;->to:I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/ShapeCut$Arm;->edge:[F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Arm.class, Object.class), Arm.class, "from;to;edge", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/ShapeCut$Arm;->from:I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/ShapeCut$Arm;->to:I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/ShapeCut$Arm;->edge:[F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int from() {
            return this.from;
        }

        public int to() {
            return this.to;
        }

        public float[] edge() {
            return this.edge;
        }
    }

    private ShapeCut(PosedMesh mesh) {
        this.mesh = mesh;
        this.height = mesh.bounds()[4];
        this.step = this.height / 160.0f;
        this.gap = this.height * 0.004f;
        for (int i = 0; i < 160; i++) {
            this.spans[i] = new ArrayList();
        }
        this.intervals = new float[160][][];
        this.labels = new int[160][];
    }

    static CharacterBake.Cut cut(PosedMesh mesh) {
        ShapeCut s = new ShapeCut(mesh);
        if (s.height <= 0.0f) {
            return null;
        }
        s.slice();
        return s.find();
    }

    private void slice() {
        float[] p = this.mesh.pos;
        for (int t = 0; t < this.mesh.triangles; t++) {
            int a = this.mesh.tris[t * 3];
            int b = this.mesh.tris[(t * 3) + 1];
            int c = this.mesh.tris[(t * 3) + 2];
            float ya = p[(a * 3) + 1];
            float yb = p[(b * 3) + 1];
            float yc = p[(c * 3) + 1];
            float lo = Math.min(ya, Math.min(yb, yc));
            float hi = Math.max(ya, Math.max(yb, yc));
            int i0 = Math.max(0, (int) Math.ceil((lo / this.step) - 0.5f));
            int i1 = Math.min(159, (int) Math.floor((hi / this.step) - 0.5f));
            for (int i = i0; i <= i1; i++) {
                float y = (i + 0.5f) * this.step;
                float x0 = Float.MAX_VALUE;
                float x1 = -3.4028235E38f;
                int[][] edges = {new int[]{a, b}, new int[]{b, c}, new int[]{c, a}};
                for (int[] e : edges) {
                    float y0 = p[(e[0] * 3) + 1];
                    float y1 = p[(e[1] * 3) + 1];
                    if (y0 != y1 && (y - y0) * (y - y1) <= 0.0f) {
                        float x = p[e[0] * 3] + (((p[e[1] * 3] - p[e[0] * 3]) * (y - y0)) / (y1 - y0));
                        x0 = Math.min(x0, x);
                        x1 = Math.max(x1, x);
                    }
                }
                if (x0 <= x1) {
                    this.spans[i].add(new float[]{x0, x1});
                }
            }
        }
        for (int i2 = 0; i2 < 160; i2++) {
            List<float[]> list = this.spans[i2];
            list.sort((u, v) -> {
                return Float.compare(u[0], v[0]);
            });
            List<float[]> merged = new ArrayList<>();
            for (float[] s : list) {
                float[] last = merged.isEmpty() ? null : merged.get(merged.size() - 1);
                if (last == null || s[0] > last[1] + this.gap) {
                    merged.add(new float[]{s[0], s[1]});
                } else {
                    last[1] = Math.max(last[1], s[1]);
                }
            }
            this.intervals[i2] = (float[][]) merged.toArray(new float[0][]);
            this.labels[i2] = new int[this.intervals[i2].length];
            this.spans[i2] = null;
        }
    }

    /* JADX WARN: Code duplicated, block: B:105:0x025b  */
    /* JADX WARN: Code duplicated, block: B:107:0x0264  */
    /* JADX WARN: Code duplicated, block: B:108:0x0268  */
    private CharacterBake.Cut find() {
        int i;
        int crotch = crotch();
        int[] inner = new int[320];
        float[] trunk = null;
        int i2 = 0;
        while (i2 < 160) {
            int[] in = innerIntervals(i2, i2 < crotch);
            if (i2 >= crotch && in[0] >= 0) {
                float[] t = this.intervals[i2][in[0]];
                boolean centred = t[0] <= 0.0f && t[1] >= 0.0f;
                boolean continues = trunk != null && t[0] <= trunk[1] && t[1] >= trunk[0];
                if (centred || continues) {
                    trunk = t;
                } else {
                    in = new int[]{-2, -2};
                }
            }
            inner[i2 * 2] = in[0];
            inner[(i2 * 2) + 1] = in[1];
            i2++;
        }
        Arm[] arm = {arm(inner, crotch, 0), arm(inner, crotch, 1)};
        int shoulder = Math.max(arm[0] == null ? -1 : arm[0].to, arm[1] == null ? -1 : arm[1].to);
        int neck = neck(inner, crotch, shoulder < 0 ? 112 : shoulder);
        boolean legs = crotch > 0;
        if (!legs && arm[0] == null && arm[1] == null && neck < 0) {
            return null;
        }
        for (int i3 = 0; i3 < 160; i3++) {
            float[][] iv = this.intervals[i3];
            for (int k = 0; k < iv.length; k++) {
                int label = 1;
                if (i3 < crotch && legs) {
                    if (k == inner[i3 * 2]) {
                        label = 4;
                    } else if (k == inner[(i3 * 2) + 1]) {
                        label = 5;
                    }
                }
                if (neck >= 0 && i3 > neck && label == 1) {
                    label = 0;
                }
                this.labels[i3][k] = label;
            }
        }
        int[] part = new int[this.mesh.vertices];
        for (int v = 0; v < this.mesh.vertices; v++) {
            float x = this.mesh.pos[v * 3];
            float y = this.mesh.pos[(v * 3) + 1];
            int band = Math.max(0, Math.min(159, (int) (y / this.step)));
            part[v] = label(x, y);
            for (int side = 0; side < 2; side++) {
                Arm a = arm[side];
                if (a != null && band >= a.from && band <= a.to) {
                    if (side == 0) {
                        if (x < a.edge[band]) {
                            int i4 = v;
                            if (side == 0) {
                                i = 2;
                            } else {
                                i = 3;
                            }
                            part[i4] = i;
                        }
                    } else if (x > a.edge[band]) {
                        int i5 = v;
                        if (side == 0) {
                            i = 2;
                        } else {
                            i = 3;
                        }
                        part[i5] = i;
                    }
                }
            }
        }
        if (share(part, 2) + share(part, 3) > 0.4f) {
            for (int v2 = 0; v2 < part.length; v2++) {
                if (part[v2] == 2 || part[v2] == 3) {
                    part[v2] = 1;
                }
            }
            arm = new Arm[2];
        }
        if (arm[0] != null || arm[1] != null) {
            part = this.mesh.separate(part);
        }
        float[][] pivot = new float[6][];
        float[][] grip = new float[6][];
        float[][] bendAt = new float[6][];
        float[][] bendWeights = new float[6][];
        pivot[1] = new float[]{0.0f, 24.0f, 0.0f};
        float blend = this.height * 0.02f;
        if (legs) {
            float hip = crotch * this.step;
            for (int bone : new int[]{4, 5}) {
                pivot[bone] = mean(part, bone, hip - (this.height * 0.06f), hip);
                pivot[bone][1] = hip;
                bendAt[bone] = mean(part, bone, hip * 0.45f, hip * 0.55f);
                bendAt[bone][1] = hip * 0.5f;
                bendWeights[bone] = below(bendAt[bone][1], blend);
            }
        }
        int side2 = 0;
        while (side2 < 2) {
            Arm a2 = arm[side2];
            if (a2 != null) {
                int bone2 = side2 == 0 ? 2 : 3;
                float top = (a2.to + 1) * this.step;
                float bottom = a2.from * this.step;
                float[] upper = mean(part, bone2, top - (this.height * 0.05f), top);
                float[] lower = mean(part, bone2, bottom, bottom + (this.height * 0.05f));
                upper[1] = top;
                lower[1] = bottom + (this.height * 0.03f);
                boolean raised = attached(a2, side2, a2.from, -1) && !attached(a2, side2, a2.to, 1);
                pivot[bone2] = raised ? new float[]{lower[0], bottom, lower[2]} : upper;
                grip[bone2] = raised ? new float[]{upper[0], top - (this.height * 0.03f), upper[2]} : lower;
                lower(part, bone2, pivot[bone2], grip[bone2]);
                float[] axis = new float[3];
                bendAt[bone2] = new float[3];
                for (int k2 = 0; k2 < 3; k2++) {
                    bendAt[bone2][k2] = (pivot[bone2][k2] + grip[bone2][k2]) * 0.5f;
                    axis[k2] = grip[bone2][k2] - pivot[bone2][k2];
                }
                bendWeights[bone2] = beyond(bendAt[bone2], axis, blend);
            }
            side2++;
        }
        if (neck >= 0) {
            float y2 = (neck + 1) * this.step;
            pivot[0] = mean(part, 0, y2, y2 + (this.height * 0.04f));
            pivot[0][1] = y2;
        }
        return new CharacterBake.Cut(part, pivot, grip, bendAt, bendWeights);
    }

    private int crotch() {
        int start = -1;
        int i = 0;
        while (i < 160 && this.intervals[i].length == 0) {
            i++;
        }
        int limit = i + 19;
        while (i < 160) {
            boolean split = splitAtCentre(i);
            if (start < 0) {
                if (split) {
                    start = i;
                } else if (i > limit) {
                    return 0;
                }
            } else if (!split) {
                if (i * this.step < this.height * 0.1f || i * this.step > this.height * 0.65f) {
                    return 0;
                }
                return i;
            }
            i++;
        }
        return 0;
    }

    private boolean splitAtCentre(int i) {
        float[][] iv = this.intervals[i];
        if (iv.length < 2) {
            return false;
        }
        for (float[] s : iv) {
            if (s[0] <= 0.0f && s[1] >= 0.0f) {
                return false;
            }
        }
        return iv[0][1] < 0.0f && iv[iv.length - 1][0] > 0.0f;
    }

    private int[] innerIntervals(int i, boolean legs) {
        float[][] iv = this.intervals[i];
        if (iv.length == 0) {
            return new int[]{-1, -1};
        }
        if (legs) {
            int right = -1;
            int left = -1;
            for (int k = 0; k < iv.length; k++) {
                if (iv[k][1] < 0.0f) {
                    right = k;
                } else if (left < 0) {
                    left = k;
                }
            }
            return new int[]{right, left};
        }
        int best = 0;
        float bestDist = Float.MAX_VALUE;
        for (int k2 = 0; k2 < iv.length; k2++) {
            float d = iv[k2][0] > 0.0f ? iv[k2][0] : iv[k2][1] < 0.0f ? -iv[k2][1] : 0.0f;
            if (d < bestDist) {
                bestDist = d;
                best = k2;
            }
        }
        return new int[]{best, best};
    }

    private Arm arm(int[] inner, int crotch, int side) {
        float[] edge = new float[160];
        Arrays.fill(edge, Float.NaN);
        for (int i = 0; i < 160; i++) {
            int k = inner[(i * 2) + side];
            float[][] iv = this.intervals[i];
            if (k == -2 && iv.length > 0 && (side != 0 ? iv[iv.length - 1][1] > 0.0f : iv[0][0] < 0.0f)) {
                edge[i] = 0.0f;
            }
            if (k >= 0) {
                if (side == 0 && k > 0) {
                    edge[i] = (iv[k - 1][1] + iv[k][0]) * 0.5f;
                }
                if (side == 1 && k < iv.length - 1) {
                    edge[i] = (iv[k][1] + iv[k + 1][0]) * 0.5f;
                }
            }
        }
        int bestFrom = -1;
        int bestTo = -1;
        int from = -1;
        int last = -1;
        int i2 = 0;
        while (i2 <= 160) {
            boolean apart = i2 < 160 && !Float.isNaN(edge[i2]);
            if (apart) {
                if (from >= 0 && (i2 - last) - 1 > 5) {
                    if (last - from > bestTo - bestFrom) {
                        bestFrom = from;
                        bestTo = last;
                    }
                    from = -1;
                }
                if (from < 0) {
                    from = i2;
                }
                last = i2;
            }
            i2++;
        }
        if (from >= 0 && last - from > bestTo - bestFrom) {
            bestFrom = from;
            bestTo = last;
        }
        if (bestFrom < 0 || (bestTo - bestFrom) + 1 < 12.799999f || bestTo <= crotch) {
            return null;
        }
        for (int i3 = bestFrom + 1; i3 < bestTo; i3++) {
            if (Float.isNaN(edge[i3])) {
                int lo = i3 - 1;
                int hi = i3 + 1;
                while (Float.isNaN(edge[hi])) {
                    hi++;
                }
                edge[i3] = side == 0 ? Math.min(edge[lo], edge[hi]) : Math.max(edge[lo], edge[hi]);
            }
        }
        return new Arm(bestFrom, bestTo, edge);
    }

    private boolean attached(Arm a, int side, int at, int dir) {
        int i;
        float e = a.edge[at];
        boolean right = side == 0;
        for (int d = 1; d <= 2 && (i = at + (dir * d)) >= 0 && i < 160; d++) {
            for (float[] iv : this.intervals[i]) {
                if (right) {
                    if (iv[0] < e) {
                        return true;
                    }
                } else if (iv[1] > e) {
                    return true;
                }
            }
        }
        return false;
    }

    private void lower(int[] part, int bone, float[] pivot, float[] grip) {
        float dx = grip[0] - pivot[0];
        float dy = grip[1] - pivot[1];
        float dz = grip[2] - pivot[2];
        float len = (float) Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
        if (len < 1.0E-4f) {
            return;
        }
        float angle = (float) Math.acos(Math.max(-1.0f, Math.min(1.0f, (-dy) / len)));
        if (angle < Math.toRadians(40.0d)) {
            return;
        }
        float ax = dz;
        float az = -dx;
        float al = (float) Math.sqrt((ax * ax) + (az * az));
        if (al < 1.0E-5f) {
            ax = 0.0f;
            az = 1.0f;
            al = 1.0f;
        }
        float ax2 = ax / al;
        float az2 = az / al;
        float turn = angle - ((float) Math.toRadians(10.0d));
        float c = (float) Math.cos(turn);
        float s = (float) Math.sin(turn);
        for (int v = 0; v < part.length; v++) {
            if (part[v] == bone) {
                rotate(this.mesh.pos, v * 3, pivot, ax2, az2, c, s);
                rotate(this.mesh.nrm, v * 3, null, ax2, az2, c, s);
            }
        }
        float[] g = (float[]) grip.clone();
        rotate(g, 0, pivot, ax2, az2, c, s);
        System.arraycopy(g, 0, grip, 0, 3);
    }

    private static void rotate(float[] a, int o, float[] about, float ax, float az, float c, float s) {
        float px = a[o] - (about == null ? 0.0f : about[0]);
        float py = a[o + 1] - (about == null ? 0.0f : about[1]);
        float pz = a[o + 2] - (about == null ? 0.0f : about[2]);
        float dot = (ax * px) + (az * pz);
        float cx = (-az) * py;
        float cy = (az * px) - (ax * pz);
        float cz = ax * py;
        float rx = (px * c) + (cx * s) + (ax * dot * (1.0f - c));
        float ry = (py * c) + (cy * s);
        float rz = (pz * c) + (cz * s) + (az * dot * (1.0f - c));
        a[o] = rx + (about == null ? 0.0f : about[0]);
        a[o + 1] = ry + (about == null ? 0.0f : about[1]);
        a[o + 2] = rz + (about == null ? 0.0f : about[2]);
    }

    private int neck(int[] inner, int crotch, int shoulder) {
        int from = Math.max(shoulder, crotch + 1);
        int best = -1;
        float bestWidth = Float.MAX_VALUE;
        for (int i = from; i < 154; i++) {
            float w = torsoWidth(inner, i);
            if (w > 0.0f && w < bestWidth) {
                bestWidth = w;
                best = i;
            }
        }
        if (best < 0) {
            return -1;
        }
        float above = 0.0f;
        float below = torsoWidth(inner, from);
        for (int i2 = best + 1; i2 < 160; i2++) {
            above = Math.max(above, torsoWidth(inner, i2));
        }
        float headHeight = (160 - best) * this.step;
        boolean narrow = bestWidth < above * 0.75f && bestWidth < below * 0.8f;
        if (!narrow || headHeight <= this.height * 0.08f || headHeight >= this.height * 0.45f) {
            return -1;
        }
        return best;
    }

    private float torsoWidth(int[] inner, int i) {
        int k = inner[i * 2];
        if (k < 0) {
            return 0.0f;
        }
        return this.intervals[i][k][1] - this.intervals[i][k][0];
    }

    private int label(float x, float y) {
        int i = Math.max(0, Math.min(159, (int) (y / this.step)));
        for (int d = 0; d < 160; d++) {
            for (int j : new int[]{i - d, i + d}) {
                if (j >= 0 && j < 160 && this.intervals[j].length != 0) {
                    float[][] iv = this.intervals[j];
                    int best = 0;
                    float bestDist = Float.MAX_VALUE;
                    for (int k = 0; k < iv.length; k++) {
                        float dist = x < iv[k][0] ? iv[k][0] - x : x > iv[k][1] ? x - iv[k][1] : 0.0f;
                        if (dist < bestDist) {
                            bestDist = dist;
                            best = k;
                        }
                    }
                    return this.labels[j][best];
                }
            }
        }
        return 1;
    }

    private float share(int[] part, int bone) {
        int n = 0;
        for (int p : part) {
            if (p == bone) {
                n++;
            }
        }
        return n / Math.max(1, part.length);
    }

    private float[] mean(int[] part, int bone, float y0, float y1) {
        double x = 0.0d;
        double y = 0.0d;
        double z = 0.0d;
        int n = 0;
        for (int pass = 0; pass < 2 && n == 0; pass++) {
            for (int v = 0; v < part.length; v++) {
                float vy = this.mesh.pos[(v * 3) + 1];
                if (part[v] == bone && (pass != 0 || (vy >= y0 && vy <= y1))) {
                    x += (double) this.mesh.pos[v * 3];
                    y += (double) vy;
                    z += (double) this.mesh.pos[(v * 3) + 2];
                    n++;
                }
            }
        }
        return n == 0 ? new float[3] : new float[]{(float) (x / ((double) n)), (float) (y / ((double) n)), (float) (z / ((double) n))};
    }

    private float[] beyond(float[] at, float[] axis, float blend) {
        float l = (float) Math.sqrt((axis[0] * axis[0]) + (axis[1] * axis[1]) + (axis[2] * axis[2]));
        float[] w = new float[this.mesh.vertices];
        if (l < 1.0E-5f) {
            return w;
        }
        for (int v = 0; v < w.length; v++) {
            float d = ((((this.mesh.pos[v * 3] - at[0]) * axis[0]) + ((this.mesh.pos[(v * 3) + 1] - at[1]) * axis[1])) + ((this.mesh.pos[(v * 3) + 2] - at[2]) * axis[2])) / l;
            float t = Math.max(0.0f, Math.min(1.0f, (d + blend) / (2.0f * blend)));
            w[v] = t * t * (3.0f - (2.0f * t));
        }
        return w;
    }

    private float[] below(float y, float blend) {
        float[] w = new float[this.mesh.vertices];
        for (int v = 0; v < w.length; v++) {
            float t = Math.max(0.0f, Math.min(1.0f, ((y + blend) - this.mesh.pos[(v * 3) + 1]) / (2.0f * blend)));
            w[v] = t * t * (3.0f - (2.0f * t));
        }
        return w;
    }
}
