package net.anubis.client.module.impl.render.character.kit;

import java.util.Arrays;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Simplifier.class */
@Environment(EnvType.CLIENT)
final class Simplifier {
    private static final double FLIP_LIMIT = 0.25d;
    private static final int DEBRIS = 10;
    private final int nv;
    private final double[] pos;
    private final int[] tri;
    private final boolean[] triDead;
    private final int[][] around;
    private final int[] aroundCount;
    private final double[] quad;
    private final int[] version;
    private final boolean[] vertDead;
    private final int[] mark;
    private final int[] mark2;
    private int stamp;
    private int live;
    private boolean relaxed;
    private final double lengthWeight;
    private int heapSize;
    private float[] heapCost = new float[1024];
    private int[] heapA = new int[1024];
    private int[] heapB = new int[1024];
    private int[] heapVa = new int[1024];
    private int[] heapVb = new int[1024];
    private final double[] best = new double[3];

    Simplifier(Mesh mesh) {
        this.nv = mesh.vertexCount;
        int nt = mesh.triCount;
        this.pos = new double[this.nv * 3];
        for (int i = 0; i < this.nv * 3; i++) {
            this.pos[i] = mesh.pos[i];
        }
        this.tri = Arrays.copyOf(mesh.tris, nt * 3);
        this.triDead = new boolean[nt];
        this.aroundCount = new int[this.nv];
        for (int i2 = 0; i2 < nt * 3; i2++) {
            int[] iArr = this.aroundCount;
            int i3 = this.tri[i2];
            iArr[i3] = iArr[i3] + 1;
        }
        this.around = new int[this.nv][];
        for (int v = 0; v < this.nv; v++) {
            this.around[v] = new int[Math.max(4, this.aroundCount[v] + 2)];
        }
        Arrays.fill(this.aroundCount, 0);
        for (int t = 0; t < nt; t++) {
            for (int c = 0; c < 3; c++) {
                int v2 = this.tri[(t * 3) + c];
                int[] iArr2 = this.around[v2];
                int[] iArr3 = this.aroundCount;
                int i4 = iArr3[v2];
                iArr3[v2] = i4 + 1;
                iArr2[i4] = t;
            }
        }
        this.quad = new double[this.nv * 10];
        this.version = new int[this.nv];
        this.vertDead = new boolean[this.nv];
        this.mark = new int[this.nv];
        this.mark2 = new int[this.nv];
        this.live = nt;
        double areaSum = 0.0d;
        for (int t2 = 0; t2 < nt; t2++) {
            int a = this.tri[t2 * 3];
            int b = this.tri[(t2 * 3) + 1];
            int c2 = this.tri[(t2 * 3) + 2];
            double ux = this.pos[b * 3] - this.pos[a * 3];
            double uy = this.pos[(b * 3) + 1] - this.pos[(a * 3) + 1];
            double uz = this.pos[(b * 3) + 2] - this.pos[(a * 3) + 2];
            double vx = this.pos[c2 * 3] - this.pos[a * 3];
            double vy = this.pos[(c2 * 3) + 1] - this.pos[(a * 3) + 1];
            double vz = this.pos[(c2 * 3) + 2] - this.pos[(a * 3) + 2];
            double nx = (uy * vz) - (uz * vy);
            double ny = (uz * vx) - (ux * vz);
            double nz = (ux * vy) - (uy * vx);
            double len = Math.sqrt((nx * nx) + (ny * ny) + (nz * nz));
            if (len >= 1.0E-12d) {
                double area = len * 0.5d;
                areaSum += area;
                double nx2 = nx / len;
                double ny2 = ny / len;
                double nz2 = nz / len;
                double d = -((nx2 * this.pos[a * 3]) + (ny2 * this.pos[(a * 3) + 1]) + (nz2 * this.pos[(a * 3) + 2]));
                addPlane(a, nx2, ny2, nz2, d, area);
                addPlane(b, nx2, ny2, nz2, d, area);
                addPlane(c2, nx2, ny2, nz2, d, area);
            }
        }
        this.lengthWeight = nt > 0 ? (0.04d * areaSum) / ((double) nt) : 0.0d;
        dropDebris();
        pushAll();
    }

    private void pushAll() {
        this.heapSize = 0;
        for (int t = 0; t < this.triDead.length; t++) {
            if (!this.triDead[t]) {
                for (int c = 0; c < 3; c++) {
                    int a = this.tri[(t * 3) + c];
                    int b = this.tri[(t * 3) + ((c + 1) % 3)];
                    if (a < b || !hasEdgeTriangleReverse(t, a, b)) {
                        push(a, b);
                    }
                }
            }
        }
    }

    private void dropDebris() {
        int nt = this.triDead.length;
        int[] root = new int[this.nv];
        for (int v = 0; v < this.nv; v++) {
            root[v] = v;
        }
        for (int t = 0; t < nt; t++) {
            int a = find(root, this.tri[t * 3]);
            for (int c = 1; c < 3; c++) {
                int b = find(root, this.tri[(t * 3) + c]);
                if (a != b) {
                    root[b] = a;
                }
            }
        }
        int[] size = new int[this.nv];
        for (int t2 = 0; t2 < nt; t2++) {
            int iFind = find(root, this.tri[t2 * 3]);
            size[iFind] = size[iFind] + 1;
        }
        for (int t3 = 0; t3 < nt; t3++) {
            if (size[find(root, this.tri[t3 * 3])] < 10) {
                this.triDead[t3] = true;
                this.live--;
            }
        }
    }

    private static int find(int[] root, int v) {
        while (root[v] != v) {
            root[v] = root[root[v]];
            v = root[v];
        }
        return v;
    }

    private boolean hasEdgeTriangleReverse(int self, int a, int b) {
        int[] list = this.around[a];
        for (int i = 0; i < this.aroundCount[a]; i++) {
            int t = list[i];
            if (t != self) {
                for (int c = 0; c < 3; c++) {
                    if (this.tri[(t * 3) + c] == b && this.tri[(t * 3) + ((c + 1) % 3)] == a) {
                        return true;
                    }
                }
            }
        }
        return false;
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

    int liveTriangles() {
        return this.live;
    }

    void reduce(int target) {
        drain(target);
        if (this.live > target && !this.relaxed) {
            this.relaxed = true;
            pushAll();
            drain(target);
        }
    }

    private void drain(int target) {
        while (this.live > target && this.heapSize > 0) {
            int a = this.heapA[0];
            int b = this.heapB[0];
            int va = this.heapVa[0];
            int vb = this.heapVb[0];
            pop();
            if (!this.vertDead[a] && !this.vertDead[b] && this.version[a] == va && this.version[b] == vb) {
                cost(a, b);
                double x = this.best[0];
                double y = this.best[1];
                double z = this.best[2];
                if (valid(a, b, x, y, z)) {
                    collapse(a, b, x, y, z);
                }
            }
        }
    }

    Mesh snapshot() {
        int[] remap = new int[this.nv];
        Arrays.fill(remap, -1);
        int count = 0;
        int[] outTris = new int[this.live * 3];
        int tc = 0;
        for (int t = 0; t < this.triDead.length; t++) {
            if (!this.triDead[t]) {
                for (int c = 0; c < 3; c++) {
                    int v = this.tri[(t * 3) + c];
                    if (remap[v] < 0) {
                        int i = count;
                        count++;
                        remap[v] = i;
                    }
                    outTris[(tc * 3) + c] = remap[v];
                }
                tc++;
            }
        }
        float[] outPos = new float[count * 3];
        for (int v2 = 0; v2 < this.nv; v2++) {
            int r = remap[v2];
            if (r >= 0) {
                outPos[r * 3] = (float) this.pos[v2 * 3];
                outPos[(r * 3) + 1] = (float) this.pos[(v2 * 3) + 1];
                outPos[(r * 3) + 2] = (float) this.pos[(v2 * 3) + 2];
            }
        }
        return new Mesh(outPos, count, outTris, tc);
    }

    private float cost(int a, int b) {
        double[] q = this.quad;
        int oa = a * 10;
        int ob = b * 10;
        double q0 = q[oa] + q[ob];
        double q1 = q[oa + 1] + q[ob + 1];
        double q2 = q[oa + 2] + q[ob + 2];
        double q3 = q[oa + 3] + q[ob + 3];
        double q4 = q[oa + 4] + q[ob + 4];
        double q5 = q[oa + 5] + q[ob + 5];
        double q6 = q[oa + 6] + q[ob + 6];
        double q7 = q[oa + 7] + q[ob + 7];
        double q8 = q[oa + 8] + q[ob + 8];
        double q9 = q[oa + 9] + q[ob + 9];
        double ax = this.pos[a * 3];
        double ay = this.pos[(a * 3) + 1];
        double az = this.pos[(a * 3) + 2];
        double bx = this.pos[b * 3];
        double by = this.pos[(b * 3) + 1];
        double bz = this.pos[(b * 3) + 2];
        double ex = bx - ax;
        double ey = by - ay;
        double ez = bz - az;
        double len2 = (ex * ex) + (ey * ey) + (ez * ez);
        double det = ((q0 * ((q4 * q7) - (q5 * q5))) - (q1 * ((q1 * q7) - (q5 * q2)))) + (q2 * ((q1 * q5) - (q4 * q2)));
        double scale = Math.abs(q0 * q4 * q7) + 1.0E-30d;
        double bestCost = Double.MAX_VALUE;
        if (Math.abs(det) > 1.0E-9d * scale) {
            double inv = 1.0d / det;
            double x = (-inv) * (((q3 * ((q4 * q7) - (q5 * q5))) - (q1 * ((q6 * q7) - (q5 * q8)))) + (q2 * ((q6 * q5) - (q4 * q8))));
            double y = (-inv) * (((q0 * ((q6 * q7) - (q8 * q5))) - (q3 * ((q1 * q7) - (q5 * q2)))) + (q2 * ((q1 * q8) - (q6 * q2))));
            double z = (-inv) * (((q0 * ((q4 * q8) - (q5 * q6))) - (q1 * ((q1 * q8) - (q6 * q2)))) + (q3 * ((q1 * q5) - (q4 * q2))));
            double mx = ((ax + bx) * 0.5d) - x;
            double my = ((ay + by) * 0.5d) - y;
            double mz = ((az + bz) * 0.5d) - z;
            if ((mx * mx) + (my * my) + (mz * mz) <= len2) {
                bestCost = error(q0, q1, q2, q3, q4, q5, q6, q7, q8, q9, x, y, z);
                this.best[0] = x;
                this.best[1] = y;
                this.best[2] = z;
            }
        }
        if (bestCost == Double.MAX_VALUE) {
            for (int s = 0; s < 3; s++) {
                double t = ((double) s) * 0.5d;
                double x2 = ax + (ex * t);
                double y2 = ay + (ey * t);
                double z2 = az + (ez * t);
                double e = error(q0, q1, q2, q3, q4, q5, q6, q7, q8, q9, x2, y2, z2);
                if (e < bestCost) {
                    bestCost = e;
                    this.best[0] = x2;
                    this.best[1] = y2;
                    this.best[2] = z2;
                }
            }
        }
        return (float) (Math.max(0.0d, bestCost) + (this.lengthWeight * len2));
    }

    private static double error(double q0, double q1, double q2, double q3, double q4, double q5, double q6, double q7, double q8, double q9, double x, double y, double z) {
        return (q0 * x * x) + (2.0d * q1 * x * y) + (2.0d * q2 * x * z) + (2.0d * q3 * x) + (q4 * y * y) + (2.0d * q5 * y * z) + (2.0d * q6 * y) + (q7 * z * z) + (2.0d * q8 * z) + q9;
    }

    private boolean valid(int a, int b, double x, double y, double z) {
        int s = this.stamp + 1;
        this.stamp = s;
        int shared = 0;
        int[] la = this.around[a];
        for (int i = 0; i < this.aroundCount[a]; i++) {
            int t = la[i];
            if (!this.triDead[t]) {
                boolean hasB = false;
                for (int c = 0; c < 3; c++) {
                    int v = this.tri[(t * 3) + c];
                    if (v == b) {
                        hasB = true;
                    }
                    if (v != a) {
                        this.mark[v] = s;
                    }
                }
                if (hasB) {
                    shared++;
                }
            }
        }
        if (shared == 0 || shared > 2) {
            return false;
        }
        int common = 0;
        int[] lb = this.around[b];
        for (int i2 = 0; i2 < this.aroundCount[b]; i2++) {
            int t2 = lb[i2];
            if (!this.triDead[t2]) {
                for (int c2 = 0; c2 < 3; c2++) {
                    int v2 = this.tri[(t2 * 3) + c2];
                    if (v2 != b && v2 != a && this.mark[v2] == s && this.mark2[v2] != s) {
                        this.mark2[v2] = s;
                        common++;
                    }
                }
            }
        }
        return common == shared && keepsOrientation(a, b, x, y, z) && keepsOrientation(b, a, x, y, z);
    }

    private boolean keepsOrientation(int v, int other, double x, double y, double z) {
        int[] list = this.around[v];
        for (int i = 0; i < this.aroundCount[v]; i++) {
            int t = list[i];
            if (!this.triDead[t]) {
                int i0 = this.tri[t * 3];
                int i1 = this.tri[(t * 3) + 1];
                int i2 = this.tri[(t * 3) + 2];
                if (i0 != other && i1 != other && i2 != other) {
                    double p0x = this.pos[i0 * 3];
                    double p0y = this.pos[(i0 * 3) + 1];
                    double p0z = this.pos[(i0 * 3) + 2];
                    double p1x = this.pos[i1 * 3];
                    double p1y = this.pos[(i1 * 3) + 1];
                    double p1z = this.pos[(i1 * 3) + 2];
                    double p2x = this.pos[i2 * 3];
                    double p2y = this.pos[(i2 * 3) + 1];
                    double p2z = this.pos[(i2 * 3) + 2];
                    double onx = ((p1y - p0y) * (p2z - p0z)) - ((p1z - p0z) * (p2y - p0y));
                    double ony = ((p1z - p0z) * (p2x - p0x)) - ((p1x - p0x) * (p2z - p0z));
                    double onz = ((p1x - p0x) * (p2y - p0y)) - ((p1y - p0y) * (p2x - p0x));
                    if (i0 == v) {
                        p0x = x;
                        p0y = y;
                        p0z = z;
                    } else if (i1 == v) {
                        p1x = x;
                        p1y = y;
                        p1z = z;
                    } else {
                        p2x = x;
                        p2y = y;
                        p2z = z;
                    }
                    double ux = p1x - p0x;
                    double uy = p1y - p0y;
                    double uz = p1z - p0z;
                    double wx = p2x - p0x;
                    double wy = p2y - p0y;
                    double wz = p2z - p0z;
                    double nnx = (uy * wz) - (uz * wy);
                    double nny = (uz * wx) - (ux * wz);
                    double nnz = (ux * wy) - (uy * wx);
                    double ol = Math.sqrt((onx * onx) + (ony * ony) + (onz * onz));
                    double nl = Math.sqrt((nnx * nnx) + (nny * nny) + (nnz * nnz));
                    if (nl < 1.0E-14d) {
                        return false;
                    }
                    double limit = this.relaxed ? 0.0d : FLIP_LIMIT;
                    if (ol > 1.0E-14d && (onx * nnx) + (ony * nny) + (onz * nnz) < limit * ol * nl) {
                        return false;
                    }
                    if (this.relaxed) {
                        continue;
                    } else {
                        double vx = p2x - p1x;
                        double vy = p2y - p1y;
                        double vz = p2z - p1z;
                        double edges = (ux * ux) + (uy * uy) + (uz * uz) + (wx * wx) + (wy * wy) + (wz * wz) + (vx * vx) + (vy * vy) + (vz * vz);
                        if (nl * 3.4641016d < 0.06d * edges) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void collapse(int a, int b, double x, double y, double z) {
        this.pos[b * 3] = x;
        this.pos[(b * 3) + 1] = y;
        this.pos[(b * 3) + 2] = z;
        for (int i = 0; i < 10; i++) {
            double[] dArr = this.quad;
            int i2 = (b * 10) + i;
            dArr[i2] = dArr[i2] + this.quad[(a * 10) + i];
        }
        int[] la = this.around[a];
        for (int i3 = 0; i3 < this.aroundCount[a]; i3++) {
            int t = la[i3];
            if (!this.triDead[t]) {
                int o = t * 3;
                if (this.tri[o] == b || this.tri[o + 1] == b || this.tri[o + 2] == b) {
                    this.triDead[t] = true;
                    this.live--;
                } else {
                    for (int c = 0; c < 3; c++) {
                        if (this.tri[o + c] == a) {
                            this.tri[o + c] = b;
                        }
                    }
                    append(b, t);
                }
            }
        }
        this.vertDead[a] = true;
        this.aroundCount[a] = 0;
        int[] iArr = this.version;
        iArr[a] = iArr[a] + 1;
        int[] iArr2 = this.version;
        iArr2[b] = iArr2[b] + 1;
        compact(b);
        int s = this.stamp + 1;
        this.stamp = s;
        int[] lb = this.around[b];
        for (int i4 = 0; i4 < this.aroundCount[b]; i4++) {
            int t2 = lb[i4];
            for (int c2 = 0; c2 < 3; c2++) {
                int v = this.tri[(t2 * 3) + c2];
                if (v != b && this.mark[v] != s) {
                    this.mark[v] = s;
                    push(b, v);
                }
            }
        }
    }

    private void append(int v, int t) {
        int[] list = this.around[v];
        if (this.aroundCount[v] == list.length) {
            list = Arrays.copyOf(list, list.length * 2);
            this.around[v] = list;
        }
        int[] iArr = this.aroundCount;
        int i = iArr[v];
        iArr[v] = i + 1;
        list[i] = t;
    }

    private void compact(int v) {
        int[] list = this.around[v];
        int n = 0;
        for (int i = 0; i < this.aroundCount[v]; i++) {
            if (!this.triDead[list[i]]) {
                int i2 = n;
                n++;
                list[i2] = list[i];
            }
        }
        this.aroundCount[v] = n;
    }

    private void push(int a, int b) {
        int i;
        float c = cost(a, b);
        if (this.heapSize == this.heapCost.length) {
            int n = this.heapSize * 2;
            this.heapCost = Arrays.copyOf(this.heapCost, n);
            this.heapA = Arrays.copyOf(this.heapA, n);
            this.heapB = Arrays.copyOf(this.heapB, n);
            this.heapVa = Arrays.copyOf(this.heapVa, n);
            this.heapVb = Arrays.copyOf(this.heapVb, n);
        }
        int i2 = this.heapSize;
        int i3 = i2;
        this.heapSize = i2 + 1;
        while (true) {
            i = i3;
            if (i <= 0) {
                break;
            }
            int p = (i - 1) >> 1;
            if (this.heapCost[p] <= c) {
                break;
            }
            move(p, i);
            i3 = p;
        }
        this.heapCost[i] = c;
        this.heapA[i] = a;
        this.heapB[i] = b;
        this.heapVa[i] = this.version[a];
        this.heapVb[i] = this.version[b];
    }

    private void pop() {
        int i;
        int last = this.heapSize - 1;
        this.heapSize = last;
        if (last == 0) {
            return;
        }
        float c = this.heapCost[last];
        int a = this.heapA[last];
        int b = this.heapB[last];
        int va = this.heapVa[last];
        int vb = this.heapVb[last];
        int i2 = 0;
        while (true) {
            i = i2;
            int l = (i * 2) + 1;
            if (l >= last) {
                break;
            }
            int r = l + 1;
            int m = (r >= last || this.heapCost[r] >= this.heapCost[l]) ? l : r;
            if (this.heapCost[m] >= c) {
                break;
            }
            move(m, i);
            i2 = m;
        }
        this.heapCost[i] = c;
        this.heapA[i] = a;
        this.heapB[i] = b;
        this.heapVa[i] = va;
        this.heapVb[i] = vb;
    }

    private void move(int from, int to) {
        this.heapCost[to] = this.heapCost[from];
        this.heapA[to] = this.heapA[from];
        this.heapB[to] = this.heapB[from];
        this.heapVa[to] = this.heapVa[from];
        this.heapVb[to] = this.heapVb[from];
    }
}
