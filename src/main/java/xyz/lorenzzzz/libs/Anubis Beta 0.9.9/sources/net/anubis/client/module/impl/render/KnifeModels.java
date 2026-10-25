package net.anubis.client.module.impl.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/KnifeModels.class */
@Environment(EnvType.CLIENT)
final class KnifeModels {
    static final int KARAMBIT_DETAIL = 2;
    static final float KARAMBIT_RING_X = -16.0f;
    static final float KARAMBIT_RING_Y = -1.0f;
    static final float KARAMBIT_BLADE_LENGTH = 21.0f;
    static final float[] KARAMBIT_TIP = karambitTip();
    private static final float KATANA_BOW = 0.0016f;
    static final float KATANA_BOW_FROM = 3.0f;
    static final float KATANA_BOW_RADIUS = 312.5f;

    private KnifeModels() {
    }

    private static float grain(int x, int y, int z) {
        int h = ((x * 73856093) ^ (y * 19349663)) ^ (z * 83492791);
        int h2 = (h ^ (h >>> 13)) * 1540483477;
        return 0.92f + (0.08f * (((h2 ^ (h2 >>> 15)) & 1023) / 1023.0f));
    }

    static KnifeMesh butterflyBlade() {
        int i;
        KnifeMesh.Voxels v = new KnifeMesh.Voxels();
        int x = -3;
        while (x <= 21) {
            int top = x <= 12 ? 2 : Math.round(2.0f - (((x - 12) * KATANA_BOW_FROM) / 9.0f));
            int bottom = x <= 14 ? -2 : Math.round((-2.0f) + (((float) Math.pow((x - 14) / 7.0f, 2.0d)) * 1.0f));
            for (int y = bottom; y <= top; y++) {
                if (x < 0) {
                    i = 5;
                } else if (x <= 1) {
                    i = 1;
                } else if (y == bottom) {
                    i = 2;
                } else if (y == top) {
                    i = x >= 13 ? 2 : 1;
                } else {
                    i = 0;
                }
                int ink = i;
                v.set(x, y, 0, ink, grain(x, y, 0));
            }
            x++;
        }
        return v.mesh(0.0f, 22.0f);
    }

    static KnifeMesh butterflyHandle(int side) {
        KnifeMesh.Voxels v = new KnifeMesh.Voxels();
        int inner = side > 0 ? 1 : -1;
        int outer = side > 0 ? 2 : -2;
        int cap = side > 0 ? 3 : -3;
        int x = -27;
        while (x <= 0) {
            for (int y = -2; y <= 2; y++) {
                boolean corner = (x == 0 || x == -27) && Math.abs(y) == 2;
                boolean hole = Math.abs(y) <= 1 && ((x <= -6 && x >= -8) || ((x <= -12 && x >= -14) || (x <= -18 && x >= -20)));
                if (!corner && !hole) {
                    v.set(x, y, inner, 4, grain(x, y, inner));
                    int ink = 3;
                    if ((x == -10 || x == -16) && Math.abs(y) <= 1) {
                        ink = 6;
                    }
                    if (y == 0 && (x == -2 || x == -25)) {
                        ink = 5;
                    }
                    v.set(x, y, outer, ink, grain(x, y, outer));
                }
            }
            x++;
        }
        v.set(-2, 0, cap, 5, 1.0f);
        v.set(-25, 0, cap, 5, 1.0f);
        if (side < 0) {
            v.set(-28, 1, inner, 5, 1.0f);
            v.set(-28, 2, inner, 5, 1.0f);
            v.set(-29, 2, inner, 5, 1.0f);
        }
        return v.mesh(-29.0f, 0.0f);
    }

    private static float[] karambitCurve(float s) {
        float x = 5.5f;
        float y = 0.5f;
        float f = 0.0f;
        while (true) {
            float t = f;
            if (t < s) {
                float heading = -((0.04f * t) + (0.0026f * t * t));
                x += ((float) Math.cos(heading)) * 0.05f;
                y += ((float) Math.sin(heading)) * 0.05f;
                f = t + 0.05f;
            } else {
                float heading2 = -((0.04f * s) + (0.0026f * s * s));
                return new float[]{x, y, (float) Math.cos(heading2), (float) Math.sin(heading2)};
            }
        }
    }

    private static float[] karambitTip() {
        float[] c = karambitCurve(KARAMBIT_BLADE_LENGTH);
        return new float[]{c[0] * 2.0f, c[1] * 2.0f};
    }

    static KnifeMesh karambit() {
        int i;
        KnifeMesh.Voxels v = new KnifeMesh.Voxels();
        float ringX = KARAMBIT_RING_X / 2;
        float ringY = KARAMBIT_RING_Y / 2;
        for (int x = Math.round((ringX + 2.4f) * 2); x < 6 * 2; x++) {
            float px = (x + 0.5f) / 2;
            float middle = 0.5f - ((float) Math.pow((5.0f - px) / 11.0f, 2.0d));
            for (int y = (-6) * 2; y <= 4 * 2; y++) {
                float across = ((y + 0.5f) / 2) - middle;
                if (Math.abs(across) <= 2.45f) {
                    v.set(x, y, 0, 5, grain(x, y, 0));
                    boolean rivet = Math.hypot((double) (px + KATANA_BOW_FROM), (double) across) < 0.699999988079071d || Math.hypot((double) (px - 2.5f), (double) across) < 0.699999988079071d;
                    boolean inlay = Math.abs(across) < 0.5f && px > -1.5f && px < 1.5f;
                    boolean rim = Math.abs(across) > 1.95f;
                    for (int z : new int[]{-1, 1}) {
                        if (rivet) {
                            i = 5;
                        } else if (inlay) {
                            i = 6;
                        } else {
                            i = rim ? 4 : 3;
                        }
                        int ink = i;
                        v.set(x, y, z, ink, grain(x, y, z));
                    }
                    if (rivet) {
                        v.set(x, y, -2, 5, 1.0f);
                        v.set(x, y, 2, 5, 1.0f);
                    }
                }
            }
        }
        for (int x2 = Math.round((ringX - 3.4f) * 2); x2 <= Math.round((ringX + 3.4f) * 2); x2++) {
            for (int y2 = Math.round((ringY - 3.4f) * 2); y2 <= Math.round((ringY + 3.4f) * 2); y2++) {
                float dx = ((x2 + 0.5f) / 2) - ringX;
                float dy = ((y2 + 0.5f) / 2) - ringY;
                float d = (float) Math.sqrt((dx * dx) + (dy * dy));
                if (d >= 2.0f && d <= KATANA_BOW_FROM && !v.filled(x2, y2, 0)) {
                    float shade = (d > 2.75f || d < 2.25f) ? 0.86f : 1.0f;
                    for (int z2 = -1; z2 <= 1; z2++) {
                        v.set(x2, y2, z2, 5, shade * grain(x2, y2, z2));
                    }
                }
            }
        }
        float[][] curve = new float[420 + 1][];
        for (int i2 = 0; i2 <= 420; i2++) {
            curve[i2] = karambitCurve((KARAMBIT_BLADE_LENGTH * i2) / 420);
        }
        for (int x3 = 5 * 2; x3 <= 31 * 2; x3++) {
            for (int y3 = (-24) * 2; y3 <= 6 * 2; y3++) {
                float px2 = (x3 + 0.5f) / 2;
                float py = (y3 + 0.5f) / 2;
                int best = 0;
                float bestDistance = Float.MAX_VALUE;
                for (int i3 = 0; i3 <= 420; i3++) {
                    float dx2 = px2 - curve[i3][0];
                    float dy2 = py - curve[i3][1];
                    float d2 = (dx2 * dx2) + (dy2 * dy2);
                    if (d2 < bestDistance) {
                        bestDistance = d2;
                        best = i3;
                    }
                }
                float[] c = curve[best];
                float dx3 = px2 - c[0];
                float dy3 = py - c[1];
                float along = (dx3 * c[2]) + (dy3 * c[3]);
                float across2 = ((-dx3) * c[3]) + (dy3 * c[2]);
                if ((best != 0 || along >= -0.5f) && (best != 420 || along <= 0.3f)) {
                    float left = 1.0f - (best / 420);
                    float spine = (2.9f * ((float) Math.pow(left, 0.6d))) + 0.15f;
                    float edge = (2.9f * ((float) Math.pow(left, 1.1d))) + 0.1f;
                    if (across2 <= spine && across2 >= (-edge) && !v.filled(x3, y3, 0)) {
                        int ink2 = across2 < (-edge) + 0.55f ? 2 : across2 > spine - 0.55f ? 1 : 0;
                        v.set(x3, y3, 0, ink2, grain(x3, y3, 0));
                    }
                }
            }
        }
        return v.mesh(5.0f * 2, 27.0f * 2);
    }

    static KnifeMesh katanaScabbard() {
        int ink;
        KnifeMesh.Voxels v = new KnifeMesh.Voxels();
        int x = 1;
        while (x <= 51) {
            int bow = katanaBow(Math.min(x, 48));
            boolean mouth = x <= 2;
            int low = mouth ? bow - 4 : bow - 3;
            int high = mouth ? bow + 3 : bow + 2;
            int thick = mouth ? 2 : 1;
            int y = low;
            while (y <= high) {
                for (int z = -thick; z <= thick; z++) {
                    boolean outside = y == low || y == high || Math.abs(z) == thick;
                    if (mouth || x >= 50) {
                        ink = 8;
                    } else if (x < 12 || x > 14 || !outside) {
                        ink = 4;
                    } else {
                        ink = ((x + y) + z) % 2 == 0 ? 7 : 6;
                    }
                    v.set(x, y, z, ink, ink == 4 ? 0.9f + (0.1f * grain(x, y, z)) : grain(x, y, z));
                }
                y++;
            }
            x++;
        }
        int knob = katanaBow(16);
        v.set(16, knob + 3, 0, 8, 1.0f);
        v.set(17, knob + 3, 0, 8, 1.0f);
        return v.mesh(1.0f, 51.0f);
    }

    static float katanaCentre(float x) {
        return KATANA_BOW * (x - KATANA_BOW_FROM) * (x - KATANA_BOW_FROM);
    }

    static int katanaBow(int x) {
        return Math.round(KATANA_BOW * (x - KATANA_BOW_FROM) * (x - KATANA_BOW_FROM));
    }

    static KnifeMesh katana() {
        int i;
        int i2;
        int ink;
        KnifeMesh.Voxels v = new KnifeMesh.Voxels();
        for (int x = -17; x <= -1; x++) {
            int y = -1;
            while (y <= 1) {
                int z = -1;
                while (z <= 1) {
                    if (x == -1) {
                        ink = 8;
                    } else if (x == -17) {
                        ink = (y == 0 && z == 0) ? 8 : 5;
                    } else {
                        boolean face = (Math.abs(y) == 1) ^ (Math.abs(z) == 1);
                        boolean window = face && ((x + 20) % 4 == 1 || (x + 20) % 4 == 2);
                        ink = window ? 2 : 7;
                    }
                    v.set(x, y, z, ink, ink == 2 ? 0.82f : grain(x, y, z));
                    z++;
                }
                y++;
            }
        }
        for (int y2 = -4; y2 <= 4; y2++) {
            for (int z2 = -4; z2 <= 4; z2++) {
                float d = (float) Math.sqrt((y2 * y2) + (z2 * z2));
                if (d <= 3.9f) {
                    v.set(0, y2, z2, d >= KATANA_BOW_FROM ? 8 : 5, grain(0, y2, z2));
                }
            }
        }
        for (int y3 = -1; y3 <= 1; y3++) {
            for (int z3 = -1; z3 <= 1; z3++) {
                v.set(1, y3, z3, 8, grain(1, y3, z3));
            }
            v.set(2, y3, 0, 8, grain(2, y3, 0));
        }
        int x2 = 3;
        while (x2 <= 48) {
            int bow = katanaBow(x2);
            if (x2 >= 48) {
                i = bow + 1;
            } else if (x2 >= 46) {
                i = bow;
            } else {
                i = x2 >= 44 ? bow - 1 : bow - 2;
            }
            int lowest = i;
            int highest = x2 >= 48 ? bow + 1 : bow + 1;
            for (int y4 = lowest; y4 <= highest; y4++) {
                if (y4 == lowest) {
                    i2 = 2;
                } else if (y4 == highest) {
                    i2 = 1;
                } else if (y4 == lowest + 1) {
                    i2 = (x2 <= 5 || Math.sin(((double) x2) * 0.85d) + (Math.sin(((double) x2) * 0.37d) * 0.6d) <= 0.35d) ? 0 : 2;
                } else {
                    i2 = 0;
                }
                int ink2 = i2;
                v.set(x2, y4, 0, ink2, grain(x2, y4, 0));
            }
            x2++;
        }
        return v.mesh(KATANA_BOW_FROM, 49.0f);
    }
}
