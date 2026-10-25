package net.anubis.client.module.impl.render;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/VoxelModel.class */
@Environment(EnvType.CLIENT)
final class VoxelModel {
    static final int X_NEG = 0;
    static final int X_POS = 1;
    static final int TOP = 2;
    static final int BOTTOM = 3;
    static final int FRONT = 4;
    static final int BACK = 5;
    private static final int SPAN = 40;
    private static final int OFFSET = 20;
    private static final float EDGE_SHADOW = 0.11f;
    private static final float CORNER_SHADOW = 0.05f;
    private static final int[] STEP_X = {-1, 1, 0, 0, 0, 0};
    private static final int[] STEP_Y = {0, 0, -1, 1, 0, 0};
    private static final int[] STEP_Z = {0, 0, 0, 0, -1, 1};
    private final byte[] cells = new byte[64000];
    private final byte[] paint = new byte[384000];

    VoxelModel() {
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/VoxelModel$Ink.class */
    @Environment(EnvType.CLIENT)
    static final class Ink extends Record {
        private final int material;
        private final float tone;
        private final float grain;

        Ink(int material, float tone, float grain) {
            this.material = material;
            this.tone = tone;
            this.grain = grain;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Ink.class), Ink.class, "material;tone;grain", "FIELD:Lnet/anubis/client/module/impl/render/VoxelModel$Ink;->material:I", "FIELD:Lnet/anubis/client/module/impl/render/VoxelModel$Ink;->tone:F", "FIELD:Lnet/anubis/client/module/impl/render/VoxelModel$Ink;->grain:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Ink.class), Ink.class, "material;tone;grain", "FIELD:Lnet/anubis/client/module/impl/render/VoxelModel$Ink;->material:I", "FIELD:Lnet/anubis/client/module/impl/render/VoxelModel$Ink;->tone:F", "FIELD:Lnet/anubis/client/module/impl/render/VoxelModel$Ink;->grain:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Ink.class, Object.class), Ink.class, "material;tone;grain", "FIELD:Lnet/anubis/client/module/impl/render/VoxelModel$Ink;->material:I", "FIELD:Lnet/anubis/client/module/impl/render/VoxelModel$Ink;->tone:F", "FIELD:Lnet/anubis/client/module/impl/render/VoxelModel$Ink;->grain:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int material() {
            return this.material;
        }

        public float tone() {
            return this.tone;
        }

        public float grain() {
            return this.grain;
        }
    }

    void fill(int x0, int y0, int z0, int x1, int y1, int z1, int ink) {
        for (int x = Math.min(x0, x1); x <= Math.max(x0, x1); x++) {
            for (int y = Math.min(y0, y1); y <= Math.max(y0, y1); y++) {
                for (int z = Math.min(z0, z1); z <= Math.max(z0, z1); z++) {
                    this.cells[index(x, y, z)] = (byte) (ink + 1);
                }
            }
        }
    }

    void paint(int x0, int y0, int z0, int x1, int y1, int z1, int face, int ink) {
        for (int x = Math.min(x0, x1); x <= Math.max(x0, x1); x++) {
            for (int y = Math.min(y0, y1); y <= Math.max(y0, y1); y++) {
                for (int z = Math.min(z0, z1); z <= Math.max(z0, z1); z++) {
                    this.paint[(index(x, y, z) * 6) + face] = (byte) (ink + 1);
                }
            }
        }
    }

    void paint(int x, int y, int z, int face, int ink) {
        this.paint[(index(x, y, z) * 6) + face] = (byte) (ink + 1);
    }

    boolean solid(int x, int y, int z) {
        return x >= -20 && y >= -20 && z >= -20 && x < 20 && y < 20 && z < 20 && this.cells[index(x, y, z)] != 0;
    }

    PharaohMesh mesh(Ink[] inks, float ox, float oy, float oz, float sx, float sy, float sz) {
        PharaohMesh.Builder b = new PharaohMesh.Builder();
        emit(b, inks, ox, oy, oz, sx, sy, sz);
        return new PharaohMesh(b);
    }

    void emit(PharaohMesh.Builder b, Ink[] inks, float ox, float oy, float oz, float sx, float sy, float sz) {
        float[] lo = new float[3];
        float[] hi = new float[3];
        for (int x = -20; x < 20; x++) {
            for (int y = -20; y < 20; y++) {
                for (int z = -20; z < 20; z++) {
                    int cell = index(x, y, z);
                    if (this.cells[cell] != 0) {
                        lo[0] = ox + (x * sx);
                        lo[1] = oy + (y * sy);
                        lo[2] = oz + (z * sz);
                        hi[0] = lo[0] + sx;
                        hi[1] = lo[1] + sy;
                        hi[2] = lo[2] + sz;
                        float grain = grain(x, y, z);
                        for (int face = 0; face < 6; face++) {
                            if (!solid(x + STEP_X[face], y + STEP_Y[face], z + STEP_Z[face])) {
                                byte b2 = this.paint[(cell * 6) + face];
                                Ink ink = inks[(b2 != 0 ? b2 : this.cells[cell]) - 1];
                                b.shade(ink.tone() * (1.0f - (ink.grain() * grain)) * occlusion(x, y, z, face));
                                face(b, face, lo, hi, ink.material());
                            }
                        }
                    }
                }
            }
        }
        b.shade(1.0f);
    }

    private float occlusion(int x, int y, int z, int face) {
        boolean zSolid;
        int fx = x + STEP_X[face];
        int fy = y + STEP_Y[face];
        int fz = z + STEP_Z[face];
        int edges = 0;
        int corners = 0;
        for (int du = -1; du <= 1; du++) {
            for (int dv = -1; dv <= 1; dv++) {
                if (du != 0 || dv != 0) {
                    switch (face >> 1) {
                        case 0:
                            zSolid = solid(fx, fy + du, fz + dv);
                            break;
                        case 1:
                            zSolid = solid(fx + du, fy, fz + dv);
                            break;
                        default:
                            zSolid = solid(fx + du, fy + dv, fz);
                            break;
                    }
                    boolean hit = zSolid;
                    if (hit) {
                        if (du == 0 || dv == 0) {
                            edges++;
                        } else {
                            corners++;
                        }
                    }
                }
            }
        }
        return Math.max(0.55f, (1.0f - (EDGE_SHADOW * edges)) - (CORNER_SHADOW * corners));
    }

    private static void face(PharaohMesh.Builder b, int face, float[] lo, float[] hi, int material) {
        float cx = (lo[0] + hi[0]) * 0.5f;
        float cy = (lo[1] + hi[1]) * 0.5f;
        float cz = (lo[2] + hi[2]) * 0.5f;
        float x0 = lo[0];
        float y0 = lo[1];
        float z0 = lo[2];
        float x1 = hi[0];
        float y1 = hi[1];
        float z1 = hi[2];
        switch (face) {
            case 0:
                b.quad(x0, y0, z0, x0, y0, z1, x0, y1, z1, x0, y1, z0, cx, cy, cz, material);
                break;
            case 1:
                b.quad(x1, y0, z0, x1, y1, z0, x1, y1, z1, x1, y0, z1, cx, cy, cz, material);
                break;
            case 2:
                b.quad(x0, y0, z0, x1, y0, z0, x1, y0, z1, x0, y0, z1, cx, cy, cz, material);
                break;
            case 3:
                b.quad(x0, y1, z0, x0, y1, z1, x1, y1, z1, x1, y1, z0, cx, cy, cz, material);
                break;
            case 4:
                b.quad(x0, y0, z0, x0, y1, z0, x1, y1, z0, x1, y0, z0, cx, cy, cz, material);
                break;
            default:
                b.quad(x0, y0, z1, x1, y0, z1, x1, y1, z1, x0, y1, z1, cx, cy, cz, material);
                break;
        }
    }

    private static float grain(int x, int y, int z) {
        int h = ((x * 522133279) ^ (y * 1540483477)) ^ (z * 668265263);
        int h2 = (h ^ (h >>> 13)) * (-2048144789);
        return ((h2 ^ (h2 >>> 16)) & 255) / 255.0f;
    }

    private static int index(int x, int y, int z) {
        return ((((x + 20) * 40) + y + 20) * 40) + z + 20;
    }
}
