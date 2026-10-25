package net.anubis.client.module.impl.render;

import imgui.flag.ImGuiKey;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4608;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/SlashTrail.class */
@Environment(EnvType.CLIENT)
final class SlashTrail {
    private static final int CAPACITY = 96;
    private static final long GAP_NANOS = 100000000;
    private static final float SLOW = 0.6f;
    private static final float FAST = 2.8f;
    private static final float ALPHA = 0.6f;
    private static final float BASE_ALPHA = 0.12f;
    private final float[] points = new float[ImGuiKey.F5];
    private final long[] times = new long[CAPACITY];
    private int newest = -1;
    private int size;

    SlashTrail() {
    }

    void add(long now, float[] sample, int from) {
        this.newest = (this.newest + 1) % CAPACITY;
        System.arraycopy(sample, from, this.points, this.newest * 6, 6);
        this.times[this.newest] = now;
        this.size = Math.min(this.size + 1, CAPACITY);
    }

    void clear() {
        this.size = 0;
        this.newest = -1;
    }

    boolean isEmpty(long now, long lifetime) {
        return this.size == 0 || now - this.times[this.newest] > lifetime;
    }

    void emit(class_4587.class_4665 pose, class_4588 out, long now, long lifetime, int rgb, float strength) {
        int r = (rgb >> 16) & 255;
        int g = (rgb >> 8) & 255;
        int b = rgb & 255;
        for (int k = 0; k + 1 < this.size; k++) {
            int newer = Math.floorMod(this.newest - k, CAPACITY);
            int older = Math.floorMod((this.newest - k) - 1, CAPACITY);
            long ageNewer = now - this.times[newer];
            long ageOlder = now - this.times[older];
            if (ageOlder <= lifetime) {
                long dt = this.times[newer] - this.times[older];
                if (dt > 0 && dt <= GAP_NANOS) {
                    int n = newer * 6;
                    int o = older * 6;
                    float dx = this.points[n + 3] - this.points[o + 3];
                    float dy = this.points[n + 4] - this.points[o + 4];
                    float dz = this.points[n + 5] - this.points[o + 5];
                    float speed = class_3532.method_15355(((dx * dx) + (dy * dy)) + (dz * dz)) / (dt / 1.0E9f);
                    float fast = class_3532.method_15363((speed - 0.6f) / 2.1999998f, 0.0f, 1.0f);
                    if (fast > 0.0f) {
                        float aNewer = fade(ageNewer, lifetime) * fast * strength;
                        float aOlder = fade(ageOlder, lifetime) * fast * strength;
                        float[] quad = {this.points[o], this.points[o + 1], this.points[o + 2], aOlder * BASE_ALPHA, this.points[o + 3], this.points[o + 4], this.points[o + 5], aOlder, this.points[n + 3], this.points[n + 4], this.points[n + 5], aNewer, this.points[n], this.points[n + 1], this.points[n + 2], aNewer * BASE_ALPHA};
                        for (int v = 0; v < 4; v++) {
                            vertex(pose, out, quad, v, r, g, b);
                        }
                        for (int v2 = 3; v2 >= 0; v2--) {
                            vertex(pose, out, quad, v2, r, g, b);
                        }
                    }
                }
            } else {
                return;
            }
        }
    }

    private static float fade(long age, long lifetime) {
        float left = 1.0f - class_3532.method_15363(age / lifetime, 0.0f, 1.0f);
        return left * left * 0.6f;
    }

    private static void vertex(class_4587.class_4665 pose, class_4588 out, float[] quad, int v, int r, int g, int b) {
        int i = v * 4;
        out.method_56824(pose, quad[i], quad[i + 1], quad[i + 2]).method_1336(r, g, b, Math.round(class_3532.method_15363(quad[i + 3], 0.0f, 1.0f) * 255.0f)).method_22913(0.5f, 0.5f).method_22922(class_4608.field_21444).method_60803(15728880).method_60831(pose, 0.0f, 0.0f, 1.0f);
    }

    static void sparkle(class_4587.class_4665 pose, class_4588 out, float x, float y, float z, float size, float turn, int rgb, float alpha) {
        int r = (rgb >> 16) & 255;
        int g = (rgb >> 8) & 255;
        int b = rgb & 255;
        int core = Math.round(class_3532.method_15363(alpha, 0.0f, 1.0f) * 255.0f);
        for (int arm = 0; arm < 4; arm++) {
            float angle = turn + (arm * 1.5707964f);
            float cos = class_3532.method_15362(angle);
            float sin = class_3532.method_15374(angle);
            float length = size * (arm % 2 == 0 ? 1.0f : 0.7f);
            float width = size * 0.16f;
            float tx = x + (cos * length);
            float ty = y + (sin * length);
            float lx = x - (sin * width);
            float ly = y + (cos * width);
            float rx = x + (sin * width);
            float ry = y - (cos * width);
            star(pose, out, x, y, z, r, g, b, core);
            star(pose, out, lx, ly, z, r, g, b, core / 3);
            star(pose, out, tx, ty, z, r, g, b, 0);
            star(pose, out, rx, ry, z, r, g, b, core / 3);
            star(pose, out, rx, ry, z, r, g, b, core / 3);
            star(pose, out, tx, ty, z, r, g, b, 0);
            star(pose, out, lx, ly, z, r, g, b, core / 3);
            star(pose, out, x, y, z, r, g, b, core);
        }
    }

    private static void star(class_4587.class_4665 pose, class_4588 out, float x, float y, float z, int r, int g, int b, int a) {
        out.method_56824(pose, x, y, z).method_1336(r, g, b, a).method_22913(0.5f, 0.5f).method_22922(class_4608.field_21444).method_60803(15728880).method_60831(pose, 0.0f, 0.0f, 1.0f);
    }
}
