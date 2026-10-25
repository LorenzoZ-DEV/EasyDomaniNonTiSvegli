package net.anubis.client.module.impl.render.character.gltf;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/gltf/MeshReducer.class */
@Environment(EnvType.CLIENT)
final class MeshReducer {
    private final float[] pos;
    private final int[] tri;
    private final boolean[] dead;
    private final int[] corner;
    private final List<List<Integer>> around = new ArrayList();
    private final boolean[] locked;
    private final double[] quad;
    private final int[] version;
    private int live;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/gltf/MeshReducer$Move.class */
    @Environment(EnvType.CLIENT)
    private static final class Move extends Record {
        private final double cost;
        private final int from;
        private final int to;
        private final int vf;
        private final int vt;

        private Move(double cost, int from, int to, int vf, int vt) {
            this.cost = cost;
            this.from = from;
            this.to = to;
            this.vf = vf;
            this.vt = vt;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Move.class), Move.class, "cost;from;to;vf;vt", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/MeshReducer$Move;->cost:D", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/MeshReducer$Move;->from:I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/MeshReducer$Move;->to:I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/MeshReducer$Move;->vf:I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/MeshReducer$Move;->vt:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Move.class), Move.class, "cost;from;to;vf;vt", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/MeshReducer$Move;->cost:D", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/MeshReducer$Move;->from:I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/MeshReducer$Move;->to:I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/MeshReducer$Move;->vf:I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/MeshReducer$Move;->vt:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Move.class, Object.class), Move.class, "cost;from;to;vf;vt", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/MeshReducer$Move;->cost:D", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/MeshReducer$Move;->from:I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/MeshReducer$Move;->to:I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/MeshReducer$Move;->vf:I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/MeshReducer$Move;->vt:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public double cost() {
            return this.cost;
        }

        public int from() {
            return this.from;
        }

        public int to() {
            return this.to;
        }

        public int vf() {
            return this.vf;
        }

        public int vt() {
            return this.vt;
        }
    }

    MeshReducer(PosedMesh s, List<Integer> source, boolean weld) {
        Map<Long, Integer> index = new HashMap<>();
        Map<String, Integer> welded = new HashMap<>();
        List<Integer> order = new ArrayList<>();
        this.tri = new int[source.size() * 3];
        for (int i = 0; i < source.size(); i++) {
            int t = source.get(i).intValue();
            boolean back = t < 0;
            int real = back ? t ^ (-1) : t;
            for (int c = 0; c < 3; c++) {
                int v = s.tris[(real * 3) + (back ? 2 - c : c)];
                int key = back ? v ^ (-1) : v;
                Integer id = index.get(Long.valueOf(key));
                if (id == null && weld) {
                    String spot = Float.floatToIntBits(s.pos[v * 3]) + "," + Float.floatToIntBits(s.pos[(v * 3) + 1]) + "," + Float.floatToIntBits(s.pos[(v * 3) + 2]) + "," + Float.floatToIntBits(s.uv[v * 2]) + "," + Float.floatToIntBits(s.uv[(v * 2) + 1]) + "," + s.material[v] + (back ? "b" : "");
                    id = welded.get(spot);
                    if (id == null) {
                        welded.put(spot, Integer.valueOf(order.size()));
                    }
                    if (id != null) {
                        index.put(Long.valueOf(key), id);
                    }
                }
                if (id == null) {
                    id = Integer.valueOf(order.size());
                    index.put(Long.valueOf(key), id);
                    order.add(Integer.valueOf(key));
                }
                this.tri[(i * 3) + c] = id.intValue();
            }
        }
        int n = order.size();
        this.corner = new int[n];
        this.pos = new float[n * 3];
        for (int i2 = 0; i2 < n; i2++) {
            this.corner[i2] = order.get(i2).intValue();
            int v2 = this.corner[i2] < 0 ? this.corner[i2] ^ (-1) : this.corner[i2];
            System.arraycopy(s.pos, v2 * 3, this.pos, i2 * 3, 3);
        }
        this.dead = new boolean[source.size()];
        this.live = source.size();
        for (int i3 = 0; i3 < n; i3++) {
            this.around.add(new ArrayList());
        }
        for (int t2 = 0; t2 < source.size(); t2++) {
            int a = this.tri[t2 * 3];
            int b = this.tri[(t2 * 3) + 1];
            int c2 = this.tri[(t2 * 3) + 2];
            if (a == b || b == c2 || a == c2) {
                this.dead[t2] = true;
                this.live--;
            } else {
                for (int k = 0; k < 3; k++) {
                    this.around.get(this.tri[(t2 * 3) + k]).add(Integer.valueOf(t2));
                }
            }
        }
        this.locked = new boolean[n];
        Map<Long, Integer> edges = new HashMap<>();
        for (int t3 = 0; t3 < source.size(); t3++) {
            if (!this.dead[t3]) {
                for (int c3 = 0; c3 < 3; c3++) {
                    edges.merge(Long.valueOf(key(this.tri[(t3 * 3) + c3], this.tri[(t3 * 3) + ((c3 + 1) % 3)])), 1, (v0, v1) -> {
                        return Integer.sum(v0, v1);
                    });
                }
            }
        }
        for (Map.Entry<Long, Integer> e : edges.entrySet()) {
            if (e.getValue().intValue() != 2) {
                this.locked[(int) (e.getKey().longValue() >> 32)] = true;
                this.locked[(int) (e.getKey().longValue() & 4294967295L)] = true;
            }
        }
        this.quad = new double[n * 10];
        for (int t4 = 0; t4 < source.size(); t4++) {
            if (!this.dead[t4]) {
                int a2 = this.tri[t4 * 3];
                int b2 = this.tri[(t4 * 3) + 1];
                int c4 = this.tri[(t4 * 3) + 2];
                float[] f = Mat4.faceNormal(this.pos, a2, b2, c4);
                double len = Math.sqrt((f[0] * f[0]) + (f[1] * f[1]) + (f[2] * f[2]));
                if (len >= 1.0E-12d) {
                    double nx = ((double) f[0]) / len;
                    double ny = ((double) f[1]) / len;
                    double nz = ((double) f[2]) / len;
                    double d = -((nx * ((double) this.pos[a2 * 3])) + (ny * ((double) this.pos[(a2 * 3) + 1])) + (nz * ((double) this.pos[(a2 * 3) + 2])));
                    for (int v3 : new int[]{a2, b2, c4}) {
                        addPlane(v3, nx, ny, nz, d, len * 0.5d);
                    }
                }
            }
        }
        this.version = new int[n];
    }

    private static long key(int a, int b) {
        return (((long) Math.min(a, b)) << 32) | ((long) Math.max(a, b));
    }

    private void addPlane(int v, double a, double b, double c, double d, double w) {
        int o = v * 10;
        double[] dArr = this.quad;
        dArr[o] = dArr[o] + (w * a * a);
        double[] dArr2 = this.quad;
        int i = o + 1;
        dArr2[i] = dArr2[i] + (w * a * b);
        double[] dArr3 = this.quad;
        int i2 = o + 2;
        dArr3[i2] = dArr3[i2] + (w * a * c);
        double[] dArr4 = this.quad;
        int i3 = o + 3;
        dArr4[i3] = dArr4[i3] + (w * a * d);
        double[] dArr5 = this.quad;
        int i4 = o + 4;
        dArr5[i4] = dArr5[i4] + (w * b * b);
        double[] dArr6 = this.quad;
        int i5 = o + 5;
        dArr6[i5] = dArr6[i5] + (w * b * c);
        double[] dArr7 = this.quad;
        int i6 = o + 6;
        dArr7[i6] = dArr7[i6] + (w * b * d);
        double[] dArr8 = this.quad;
        int i7 = o + 7;
        dArr8[i7] = dArr8[i7] + (w * c * c);
        double[] dArr9 = this.quad;
        int i8 = o + 8;
        dArr9[i8] = dArr9[i8] + (w * c * d);
        double[] dArr10 = this.quad;
        int i9 = o + 9;
        dArr10[i9] = dArr10[i9] + (w * d * d);
    }

    private double error(int v, double x, double y, double z) {
        double[] q = this.quad;
        int o = v * 10;
        return (q[o] * x * x) + (2.0d * q[o + 1] * x * y) + (2.0d * q[o + 2] * x * z) + (2.0d * q[o + 3] * x) + (q[o + 4] * y * y) + (2.0d * q[o + 5] * y * z) + (2.0d * q[o + 6] * y) + (q[o + 7] * z * z) + (2.0d * q[o + 8] * z) + q[o + 9];
    }

    int[][] reduce(int[] targets) {
        PriorityQueue<Move> heap = new PriorityQueue<>((Comparator<? super Move>) (p, q) -> {
            return Double.compare(p.cost, q.cost);
        });
        for (int v = 0; v < this.locked.length; v++) {
            push(heap, v);
        }
        int[][] out = new int[targets.length][];
        Integer[] order = new Integer[targets.length];
        for (int i = 0; i < order.length; i++) {
            order[i] = Integer.valueOf(i);
        }
        Arrays.sort(order, (a, b) -> {
            return Integer.compare(targets[b.intValue()], targets[a.intValue()]);
        });
        for (Integer num : order) {
            int i2 = num.intValue();
            while (this.live > targets[i2] && !heap.isEmpty()) {
                Move m = heap.poll();
                if (this.version[m.from] == m.vf && this.version[m.to] == m.vt && !this.locked[m.from] && collapse(m.from, m.to)) {
                    Set<Integer> touched = new HashSet<>();
                    touched.add(Integer.valueOf(m.to));
                    Iterator<Integer> it = this.around.get(m.to).iterator();
                    while (it.hasNext()) {
                        int t = it.next().intValue();
                        if (!this.dead[t]) {
                            for (int c = 0; c < 3; c++) {
                                touched.add(Integer.valueOf(this.tri[(t * 3) + c]));
                            }
                        }
                    }
                    Iterator<Integer> it2 = touched.iterator();
                    while (it2.hasNext()) {
                        int v2 = it2.next().intValue();
                        push(heap, v2);
                    }
                }
            }
            out[i2] = snapshot();
        }
        return out;
    }

    private void push(PriorityQueue<Move> heap, int v) {
        if (this.locked[v]) {
            return;
        }
        Iterator<Integer> it = neighbours(v).iterator();
        while (it.hasNext()) {
            int n = it.next().intValue();
            double dx = this.pos[n * 3] - this.pos[v * 3];
            double dy = this.pos[(n * 3) + 1] - this.pos[(v * 3) + 1];
            double dz = this.pos[(n * 3) + 2] - this.pos[(v * 3) + 2];
            double cost = error(v, this.pos[n * 3], this.pos[(n * 3) + 1], this.pos[(n * 3) + 2]) + (1.0E-4d * ((dx * dx) + (dy * dy) + (dz * dz)));
            heap.add(new Move(cost, v, n, this.version[v], this.version[n]));
        }
    }

    private List<Integer> neighbours(int v) {
        List<Integer> out = new ArrayList<>();
        Iterator<Integer> it = this.around.get(v).iterator();
        while (it.hasNext()) {
            int t = it.next().intValue();
            if (!this.dead[t]) {
                for (int c = 0; c < 3; c++) {
                    int w = this.tri[(t * 3) + c];
                    if (w != v && !out.contains(Integer.valueOf(w))) {
                        out.add(Integer.valueOf(w));
                    }
                }
            }
        }
        return out;
    }

    private boolean collapse(int u, int v) {
        List<Integer> nu = neighbours(u);
        List<Integer> nv = neighbours(v);
        if (!nu.contains(Integer.valueOf(v))) {
            return false;
        }
        int shared = 0;
        int common = 0;
        Iterator<Integer> it = this.around.get(u).iterator();
        while (it.hasNext()) {
            int t = it.next().intValue();
            if (!this.dead[t] && has(t, v)) {
                shared++;
            }
        }
        Iterator<Integer> it2 = nu.iterator();
        while (it2.hasNext()) {
            int w = it2.next().intValue();
            if (nv.contains(Integer.valueOf(w))) {
                common++;
            }
        }
        if (common != shared) {
            return false;
        }
        Iterator<Integer> it3 = this.around.get(u).iterator();
        while (it3.hasNext()) {
            int t2 = it3.next().intValue();
            if (!this.dead[t2] && !has(t2, v)) {
                float[] before = Mat4.faceNormal(this.pos, this.tri[t2 * 3], this.tri[(t2 * 3) + 1], this.tri[(t2 * 3) + 2]);
                int[] moved = new int[3];
                moved[0] = this.tri[t2 * 3];
                moved[1] = this.tri[(t2 * 3) + 1];
                moved[2] = this.tri[(t2 * 3) + 2];
                for (int c = 0; c < 3; c++) {
                    if (moved[c] == u) {
                        moved[c] = v;
                    }
                }
                float[] after = Mat4.faceNormal(this.pos, moved[0], moved[1], moved[2]);
                double lb = Math.sqrt((before[0] * before[0]) + (before[1] * before[1]) + (before[2] * before[2]));
                double la = Math.sqrt((after[0] * after[0]) + (after[1] * after[1]) + (after[2] * after[2]));
                if (la < 1.0E-12d || lb < 1.0E-12d || ((double) (((before[0] * after[0]) + (before[1] * after[1])) + (before[2] * after[2]))) / (la * lb) < 0.5d) {
                    return false;
                }
            }
        }
        Iterator<Integer> it4 = this.around.get(u).iterator();
        while (it4.hasNext()) {
            int t3 = it4.next().intValue();
            if (!this.dead[t3]) {
                if (has(t3, v)) {
                    this.dead[t3] = true;
                    this.live--;
                } else {
                    for (int c2 = 0; c2 < 3; c2++) {
                        if (this.tri[(t3 * 3) + c2] == u) {
                            this.tri[(t3 * 3) + c2] = v;
                        }
                    }
                    this.around.get(v).add(Integer.valueOf(t3));
                }
            }
        }
        this.around.get(u).clear();
        for (int k = 0; k < 10; k++) {
            double[] dArr = this.quad;
            int i = (v * 10) + k;
            dArr[i] = dArr[i] + this.quad[(u * 10) + k];
        }
        int[] iArr = this.version;
        iArr[u] = iArr[u] + 1;
        int[] iArr2 = this.version;
        iArr2[v] = iArr2[v] + 1;
        this.locked[u] = true;
        return true;
    }

    private boolean has(int t, int v) {
        return this.tri[t * 3] == v || this.tri[(t * 3) + 1] == v || this.tri[(t * 3) + 2] == v;
    }

    private int[] snapshot() {
        int[] out = new int[this.live * 3];
        int k = 0;
        for (int t = 0; t < this.dead.length; t++) {
            if (!this.dead[t]) {
                for (int c = 0; c < 3; c++) {
                    int i = k;
                    k++;
                    out[i] = this.corner[this.tri[(t * 3) + c]];
                }
            }
        }
        return Arrays.copyOf(out, k);
    }
}
