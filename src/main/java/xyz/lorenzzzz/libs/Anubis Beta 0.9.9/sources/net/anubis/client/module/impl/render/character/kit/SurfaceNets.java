package net.anubis.client.module.impl.render.character.kit;

import imgui.flag.ImGuiTableFlags;
import java.util.Arrays;
import java.util.stream.IntStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/SurfaceNets.class */
@Environment(EnvType.CLIENT)
final class SurfaceNets {
    private static final int BLOCK = 4;
    private static final int MAX_POINTS = 6000000;
    private static final int[] EDGE_A = {0, 2, 4, 6, 0, 1, 4, 5, 0, 1, 2, 3};
    private static final int[] EDGE_B = {1, 3, 5, 7, 2, 3, 6, 7, 4, 5, 6, 7};
    private final Shape shape;
    private final float ox;
    private final float oy;
    private final float oz;
    private final float h;
    private final int nx;
    private final int ny;
    private final int nz;
    private final int bx;
    private final int by;
    private final int bz;
    private final float[] field;
    private final boolean[] active;

    private SurfaceNets(Shape shape, float x0, float y0, float z0, int nx, int ny, int nz, float h) {
        this.shape = shape;
        this.ox = x0;
        this.oy = y0;
        this.oz = z0;
        this.nx = nx;
        this.ny = ny;
        this.nz = nz;
        this.h = h;
        this.bx = ((nx + 4) - 1) / 4;
        this.by = ((ny + 4) - 1) / 4;
        this.bz = ((nz + 4) - 1) / 4;
        this.field = new float[nx * ny * nz];
        Arrays.fill(this.field, Float.NaN);
        this.active = new boolean[this.bx * this.by * this.bz];
    }

    static float fitStep(float sx, float sy, float sz, float step) {
        float f = step;
        while (true) {
            float s = f;
            if (((long) ((sx / s) + 4.0f)) * ((long) ((sy / s) + 4.0f)) * ((long) ((sz / s) + 4.0f)) <= 6000000) {
                return s;
            }
            f = s * 1.12f;
        }
    }

    static Mesh mesh(Shape shape, float x0, float y0, float z0, float x1, float y1, float z1, float step) {
        float pad = step * 3.0f;
        float x2 = x0 - pad;
        float y2 = y0 - pad;
        float z2 = z0 - pad;
        float x3 = x1 + pad;
        float y3 = y1 + pad;
        float z3 = z1 + pad;
        float h = fitStep(x3 - x2, y3 - y2, z3 - z2, step);
        int nx = ((int) Math.ceil((x3 - x2) / h)) + 1;
        int ny = ((int) Math.ceil((y3 - y2) / h)) + 1;
        int nz = ((int) Math.ceil((z3 - z2) / h)) + 1;
        SurfaceNets nets = new SurfaceNets(shape, x2, y2, z2, nx, ny, nz, h);
        nets.evaluate();
        return nets.extract();
    }

    private void evaluate() {
        float reach = (4.0f * this.h * 0.8660254f * 1.6f) + (this.h * 1.5f);
        float pad = this.h * 2.5f;
        IntStream.range(0, this.bz).parallel().forEach(bk -> {
            for (int bj = 0; bj < this.by; bj++) {
                for (int bi = 0; bi < this.bx; bi++) {
                    float cx = this.ox + (((bi * 4) + 2.0f) * this.h);
                    float cy = this.oy + (((bj * 4) + 2.0f) * this.h);
                    float cz = this.oz + (((bk * 4) + 2.0f) * this.h);
                    float d = this.shape.distance(cx, cy, cz);
                    if (d >= (-reach) && d <= reach) {
                        this.active[(((bk * this.by) + bj) * this.bx) + bi] = true;
                        Shape local = this.shape.prune((this.ox + ((bi * 4) * this.h)) - pad, (this.oy + ((bj * 4) * this.h)) - pad, (this.oz + ((bk * 4) * this.h)) - pad, this.ox + ((bi + 1) * 4 * this.h) + pad, this.oy + ((bj + 1) * 4 * this.h) + pad, this.oz + ((bk + 1) * 4 * this.h) + pad);
                        fill(local, bi * 4, bj * 4, bk * 4);
                    }
                }
            }
        });
    }

    private void fill(Shape local, int i0, int j0, int k0) {
        int i1 = Math.min(i0 + 4, this.nx);
        int j1 = Math.min(j0 + 4, this.ny);
        int k1 = Math.min(k0 + 4, this.nz);
        for (int k = k0; k < k1; k++) {
            for (int j = j0; j < j1; j++) {
                for (int i = i0; i < i1; i++) {
                    float v = local.distance(this.ox + (i * this.h), this.oy + (j * this.h), this.oz + (k * this.h));
                    this.field[(((k * this.ny) + j) * this.nx) + i] = v != v ? 1.0f : v;
                }
            }
        }
    }

    private float value(int i, int j, int k) {
        int idx = (((k * this.ny) + j) * this.nx) + i;
        float v = this.field[idx];
        if (v != v) {
            v = this.shape.distance(this.ox + (i * this.h), this.oy + (j * this.h), this.oz + (k * this.h));
            if (v != v) {
                v = 1.0f;
            }
            this.field[idx] = v;
        }
        return v;
    }

    private Mesh extract() {
        Extraction e = new Extraction();
        for (int bk = 0; bk < this.bz; bk++) {
            for (int bj = 0; bj < this.by; bj++) {
                for (int bi = 0; bi < this.bx; bi++) {
                    if (this.active[(((bk * this.by) + bj) * this.bx) + bi]) {
                        e.block(bi * 4, bj * 4, bk * 4);
                    }
                }
            }
        }
        return new Mesh(Arrays.copyOf(e.pos, e.verts * 3), e.verts, Arrays.copyOf(e.tris, e.triCount * 3), e.triCount);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/SurfaceNets$Extraction.class */
    @Environment(EnvType.CLIENT)
    private final class Extraction {
        private final int[] cells;
        private int verts;
        private int triCount;
        private final float[] corner = new float[8];
        private float[] pos = new float[12288];
        private int[] tris = new int[ImGuiTableFlags.SizingStretchProp];

        Extraction() {
            this.cells = new int[SurfaceNets.this.nx * SurfaceNets.this.ny * SurfaceNets.this.nz];
            Arrays.fill(this.cells, -1);
        }

        void block(int i0, int j0, int k0) {
            int i1 = Math.min(i0 + 4, SurfaceNets.this.nx);
            int j1 = Math.min(j0 + 4, SurfaceNets.this.ny);
            int k1 = Math.min(k0 + 4, SurfaceNets.this.nz);
            for (int k = k0; k < k1; k++) {
                for (int j = j0; j < j1; j++) {
                    for (int i = i0; i < i1; i++) {
                        boolean in = SurfaceNets.this.value(i, j, k) < 0.0f;
                        if (i + 1 < SurfaceNets.this.nx && j >= 1 && k >= 1 && j < SurfaceNets.this.ny - 1 && k < SurfaceNets.this.nz - 1) {
                            if ((SurfaceNets.this.value(i + 1, j, k) < 0.0f) != in) {
                                quad(cell(i, j - 1, k - 1), cell(i, j, k - 1), cell(i, j, k), cell(i, j - 1, k), in);
                            }
                        }
                        if (j + 1 < SurfaceNets.this.ny && i >= 1 && k >= 1 && i < SurfaceNets.this.nx - 1 && k < SurfaceNets.this.nz - 1) {
                            if ((SurfaceNets.this.value(i, j + 1, k) < 0.0f) != in) {
                                quad(cell(i - 1, j, k - 1), cell(i - 1, j, k), cell(i, j, k), cell(i, j, k - 1), in);
                            }
                        }
                        if (k + 1 < SurfaceNets.this.nz && i >= 1 && j >= 1 && i < SurfaceNets.this.nx - 1 && j < SurfaceNets.this.ny - 1) {
                            if ((SurfaceNets.this.value(i, j, k + 1) < 0.0f) != in) {
                                quad(cell(i - 1, j - 1, k), cell(i, j - 1, k), cell(i, j, k), cell(i - 1, j, k), in);
                            }
                        }
                    }
                }
            }
        }

        private int cell(int i, int j, int k) {
            int idx = (((k * SurfaceNets.this.ny) + j) * SurfaceNets.this.nx) + i;
            int v = this.cells[idx];
            if (v >= 0) {
                return v;
            }
            float[] c = this.corner;
            for (int n = 0; n < 8; n++) {
                c[n] = SurfaceNets.this.value(i + (n & 1), j + ((n >> 1) & 1), k + ((n >> 2) & 1));
            }
            float sx = 0.0f;
            float sy = 0.0f;
            float sz = 0.0f;
            int hits = 0;
            for (int e = 0; e < 12; e++) {
                int a = SurfaceNets.EDGE_A[e];
                int b = SurfaceNets.EDGE_B[e];
                float va = c[a];
                float vb = c[b];
                if ((va < 0.0f) != (vb < 0.0f)) {
                    float t = va / (va - vb);
                    float ux = a & 1;
                    float uy = (a >> 1) & 1;
                    float uz = (a >> 2) & 1;
                    float wx = b & 1;
                    float wy = (b >> 1) & 1;
                    float wz = (b >> 2) & 1;
                    sx += ux + ((wx - ux) * t);
                    sy += uy + ((wy - uy) * t);
                    sz += uz + ((wz - uz) * t);
                    hits++;
                }
            }
            float fx = hits > 0 ? sx / hits : 0.5f;
            float fy = hits > 0 ? sy / hits : 0.5f;
            float fz = hits > 0 ? sz / hits : 0.5f;
            if ((this.verts * 3) + 3 > this.pos.length) {
                this.pos = Arrays.copyOf(this.pos, this.pos.length * 2);
            }
            this.pos[this.verts * 3] = SurfaceNets.this.ox + ((i + fx) * SurfaceNets.this.h);
            this.pos[(this.verts * 3) + 1] = SurfaceNets.this.oy + ((j + fy) * SurfaceNets.this.h);
            this.pos[(this.verts * 3) + 2] = SurfaceNets.this.oz + ((k + fz) * SurfaceNets.this.h);
            this.cells[idx] = this.verts;
            int i2 = this.verts;
            this.verts = i2 + 1;
            return i2;
        }

        private void quad(int a, int b, int c, int d, boolean inside) {
            if (!inside) {
                b = d;
                d = b;
            }
            float d02 = dist2(a, c);
            float d13 = dist2(b, d);
            if (d02 <= d13) {
                tri(a, b, c);
                tri(a, c, d);
            } else {
                tri(a, b, d);
                tri(b, c, d);
            }
        }

        private float dist2(int a, int b) {
            float dx = this.pos[a * 3] - this.pos[b * 3];
            float dy = this.pos[(a * 3) + 1] - this.pos[(b * 3) + 1];
            float dz = this.pos[(a * 3) + 2] - this.pos[(b * 3) + 2];
            return (dx * dx) + (dy * dy) + (dz * dz);
        }

        private void tri(int a, int b, int c) {
            if (a == b || b == c || a == c) {
                return;
            }
            if ((this.triCount * 3) + 3 > this.tris.length) {
                this.tris = Arrays.copyOf(this.tris, this.tris.length * 2);
            }
            this.tris[this.triCount * 3] = a;
            this.tris[(this.triCount * 3) + 1] = b;
            this.tris[(this.triCount * 3) + 2] = c;
            this.triCount++;
        }
    }
}
