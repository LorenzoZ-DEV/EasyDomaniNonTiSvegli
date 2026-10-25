package net.anubis.client.util.glyph;

import java.util.Arrays;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_12175;
import net.minecraft.class_12177;
import net.minecraft.class_12178;
import net.minecraft.class_12180;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_4184;
import net.minecraft.class_9848;
import org.joml.Vector3fc;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/glyph/GlyphCanvas.class */
@Environment(EnvType.CLIENT)
public final class GlyphCanvas implements class_12175 {
    public static final int MAX_QUADS = 12000;
    private static final int MAX_LINES = 4000;
    private static final int MIN_ALPHA = 3;
    private static final int VERTEX_BITS = 9;
    private static final double RIBBON_NEAR = 0.05d;
    private int quads;
    private int lines;
    private double camX;
    private double camY;
    private double camZ;
    private double rightX;
    private double rightY;
    private double rightZ;
    private double upX;
    private double upY;
    private double upZ;
    private double lookX;
    private double lookY;
    private double lookZ;
    private final class_243[] vertices = new class_243[512];
    private double[] quadPos = new double[3072];
    private int[] quadColor = new int[256];
    private double[] linePos = new double[384];
    private int[] lineColor = new int[64];
    private float[] lineWidth = new float[64];

    public void begin(class_4184 camera) {
        this.quads = 0;
        this.lines = 0;
        class_243 eye = camera.method_71156();
        this.camX = eye.field_1352;
        this.camY = eye.field_1351;
        this.camZ = eye.field_1350;
        Vector3fc left = camera.method_35689();
        Vector3fc up = camera.method_19336();
        Vector3fc forward = camera.method_19335();
        this.rightX = -left.x();
        this.rightY = -left.y();
        this.rightZ = -left.z();
        this.upX = up.x();
        this.upY = up.y();
        this.upZ = up.z();
        this.lookX = forward.x();
        this.lookY = forward.y();
        this.lookZ = forward.z();
    }

    public void submit(boolean onTop) {
        if (this.quads == 0 && this.lines == 0) {
            return;
        }
        class_12178 properties = class_12180.method_75553(this);
        if (onTop) {
            properties.method_75533();
        }
    }

    public double camX() {
        return this.camX;
    }

    public double camY() {
        return this.camY;
    }

    public double camZ() {
        return this.camZ;
    }

    public double depth(double x, double y, double z) {
        return ((x - this.camX) * this.lookX) + ((y - this.camY) * this.lookY) + ((z - this.camZ) * this.lookZ);
    }

    public double distanceSqr(double x, double y, double z) {
        double dx = x - this.camX;
        double dy = y - this.camY;
        double dz = z - this.camZ;
        return (dx * dx) + (dy * dy) + (dz * dz);
    }

    public void quad(double ax, double ay, double az, double bx, double by, double bz, double cx, double cy, double cz, double dx, double dy, double dz, int argb) {
        if (class_9848.method_61320(argb) < 3 || !reserveQuad()) {
            return;
        }
        int p = this.quads * 12;
        double[] out = this.quadPos;
        out[p] = ax;
        out[p + 1] = ay;
        out[p + 2] = az;
        out[p + 3] = bx;
        out[p + 4] = by;
        out[p + 5] = bz;
        out[p + 6] = cx;
        out[p + 7] = cy;
        out[p + 8] = cz;
        out[p + 9] = dx;
        out[p + 10] = dy;
        out[p + 11] = dz;
        int[] iArr = this.quadColor;
        int i = this.quads;
        this.quads = i + 1;
        iArr[i] = argb;
    }

    public void outwardTriangle(double ax, double ay, double az, double bx, double by, double bz, double cx, double cy, double cz, double ox, double oy, double oz, int argb) {
        double ux = bx - ax;
        double uy = by - ay;
        double uz = bz - az;
        double vx = cx - ax;
        double vy = cy - ay;
        double vz = cz - az;
        double nx = (uy * vz) - (uz * vy);
        double ny = (uz * vx) - (ux * vz);
        double nz = (ux * vy) - (uy * vx);
        double mx = (((ax + bx) + cx) / 3.0d) - ox;
        double my = (((ay + by) + cy) / 3.0d) - oy;
        double mz = (((az + bz) + cz) / 3.0d) - oz;
        if ((nx * mx) + (ny * my) + (nz * mz) >= 0.0d) {
            quad(ax, ay, az, bx, by, bz, cx, cy, cz, cx, cy, cz, argb);
        } else {
            quad(ax, ay, az, cx, cy, cz, bx, by, bz, bx, by, bz, argb);
        }
    }

    public void line(double ax, double ay, double az, double bx, double by, double bz, int argb, float width) {
        if (class_9848.method_61320(argb) < 3 || !reserveLine()) {
            return;
        }
        int p = this.lines * 6;
        double[] out = this.linePos;
        out[p] = ax;
        out[p + 1] = ay;
        out[p + 2] = az;
        out[p + 3] = bx;
        out[p + 4] = by;
        out[p + 5] = bz;
        this.lineColor[this.lines] = argb;
        float[] fArr = this.lineWidth;
        int i = this.lines;
        this.lines = i + 1;
        fArr[i] = width;
    }

    public void ribbon(double ax, double ay, double az, double bx, double by, double bz, int argb, float width, double pixel) {
        double depthA = depth(ax, ay, az);
        double depthB = depth(bx, by, bz);
        if (depthA >= RIBBON_NEAR || depthB >= RIBBON_NEAR) {
            if (depthA < RIBBON_NEAR) {
                double t = (RIBBON_NEAR - depthA) / (depthB - depthA);
                ax += (bx - ax) * t;
                ay += (by - ay) * t;
                az += (bz - az) * t;
                depthA = 0.05d;
            } else if (depthB < RIBBON_NEAR) {
                double t2 = (RIBBON_NEAR - depthB) / (depthA - depthB);
                bx += (ax - bx) * t2;
                by += (ay - by) * t2;
                bz += (az - bz) * t2;
                depthB = 0.05d;
            }
            double fx = ax - this.camX;
            double fy = ay - this.camY;
            double fz = az - this.camZ;
            double ex = bx - ax;
            double ey = by - ay;
            double ez = bz - az;
            double sx = (fy * ez) - (fz * ey);
            double sy = (fz * ex) - (fx * ez);
            double sz = (fx * ey) - (fy * ex);
            double length = Math.sqrt((sx * sx) + (sy * sy) + (sz * sz));
            if (length < 1.0E-9d) {
                return;
            }
            double scale = ((((double) width) * 0.5d) * pixel) / length;
            double ka = scale * depthA;
            double kb = scale * depthB;
            quad(ax + (sx * ka), ay + (sy * ka), az + (sz * ka), bx + (sx * kb), by + (sy * kb), bz + (sz * kb), bx - (sx * kb), by - (sy * kb), bz - (sz * kb), ax - (sx * ka), ay - (sy * ka), az - (sz * ka), argb);
        }
    }

    public void shape(float[] shape, double x, double y, double z, double ux, double uy, double uz, double vx, double vy, double vz, int argb) {
        if (class_9848.method_61320(argb) < 3) {
            return;
        }
        for (int i = 0; i + 7 < shape.length; i += 8) {
            float u0 = shape[i];
            float v0 = shape[i + 1];
            float u1 = shape[i + 2];
            float v1 = shape[i + 3];
            float u2 = shape[i + 4];
            float v2 = shape[i + 5];
            float u3 = shape[i + 6];
            float v3 = shape[i + 7];
            quad(x + (ux * ((double) u0)) + (vx * ((double) v0)), y + (uy * ((double) u0)) + (vy * ((double) v0)), z + (uz * ((double) u0)) + (vz * ((double) v0)), x + (ux * ((double) u1)) + (vx * ((double) v1)), y + (uy * ((double) u1)) + (vy * ((double) v1)), z + (uz * ((double) u1)) + (vz * ((double) v1)), x + (ux * ((double) u2)) + (vx * ((double) v2)), y + (uy * ((double) u2)) + (vy * ((double) v2)), z + (uz * ((double) u2)) + (vz * ((double) v2)), x + (ux * ((double) u3)) + (vx * ((double) v3)), y + (uy * ((double) u3)) + (vy * ((double) v3)), z + (uz * ((double) u3)) + (vz * ((double) v3)), argb);
        }
    }

    public void sprite(float[] shape, double x, double y, double z, float size, float angle, int argb) {
        sprite(shape, x, y, z, size, size, angle, argb);
    }

    public void sprite(float[] shape, double x, double y, double z, float width, float height, float angle, int argb) {
        sprite(shape, x, y, z, width, height, angle, 0.0f, 0.0f, argb);
    }

    public void sprite(float[] shape, double x, double y, double z, float width, float height, float angle, float shiftU, float shiftV, int argb) {
        if (class_9848.method_61320(argb) < 3) {
            return;
        }
        float cos = class_3532.method_15362(angle);
        float sin = class_3532.method_15374(angle);
        double ux = ((this.rightX * ((double) cos)) + (this.upX * ((double) sin))) * ((double) width);
        double uy = ((this.rightY * ((double) cos)) + (this.upY * ((double) sin))) * ((double) width);
        double uz = ((this.rightZ * ((double) cos)) + (this.upZ * ((double) sin))) * ((double) width);
        double vx = ((this.upX * ((double) cos)) - (this.rightX * ((double) sin))) * ((double) height);
        double vy = ((this.upY * ((double) cos)) - (this.rightY * ((double) sin))) * ((double) height);
        double vz = ((this.upZ * ((double) cos)) - (this.rightZ * ((double) sin))) * ((double) height);
        shape(shape, x + (ux * ((double) shiftU)) + (vx * ((double) shiftV)), y + (uy * ((double) shiftU)) + (vy * ((double) shiftV)), z + (uz * ((double) shiftU)) + (vz * ((double) shiftV)), ux, uy, uz, vx, vy, vz, argb);
    }

    public void flat(float[] shape, double x, double y, double z, float size, float angle, int argb) {
        float cos = class_3532.method_15362(angle) * size;
        float sin = class_3532.method_15374(angle) * size;
        double flip = this.camY >= y ? -1.0d : 1.0d;
        shape(shape, x, y, z, cos, 0.0d, flip * ((double) sin), -sin, 0.0d, flip * ((double) cos), argb);
    }

    public void band(double x, double y, double z, float inner, float outer, float from, float to, int segments, int argb) {
        if (outer <= inner || segments <= 0 || class_9848.method_61320(argb) < 3) {
            return;
        }
        double flip = this.camY >= y ? -1.0d : 1.0d;
        float step = (to - from) / segments;
        float cosA = class_3532.method_15362(from);
        float sinA = class_3532.method_15374(from);
        for (int i = 1; i <= segments; i++) {
            float angle = from + (step * i);
            float cosB = class_3532.method_15362(angle);
            float sinB = class_3532.method_15374(angle);
            quad(x + ((double) (cosA * inner)), y, z + (flip * ((double) sinA) * ((double) inner)), x + ((double) (cosA * outer)), y, z + (flip * ((double) sinA) * ((double) outer)), x + ((double) (cosB * outer)), y, z + (flip * ((double) sinB) * ((double) outer)), x + ((double) (cosB * inner)), y, z + (flip * ((double) sinB) * ((double) inner)), argb);
            cosA = cosB;
            sinA = sinB;
        }
    }

    public void ring(double x, double y, double z, float inner, float outer, int segments, int argb) {
        band(x, y, z, inner, outer, 0.0f, 6.2831855f, segments, argb);
    }

    public void method_75531(class_12177 out, float alpha) {
        boolean faded = alpha < 1.0f;
        double[] q = this.quadPos;
        int i = 0;
        int p = 0;
        while (i < this.quads) {
            int color = faded ? class_9848.method_75601(this.quadColor[i], alpha) : this.quadColor[i];
            out.method_75474(vertex(q[p], q[p + 1], q[p + 2]), vertex(q[p + 3], q[p + 4], q[p + 5]), vertex(q[p + 6], q[p + 7], q[p + 8]), vertex(q[p + 9], q[p + 10], q[p + 11]), color);
            i++;
            p += 12;
        }
        double[] l = this.linePos;
        int i2 = 0;
        int p2 = 0;
        while (i2 < this.lines) {
            int color2 = faded ? class_9848.method_75601(this.lineColor[i2], alpha) : this.lineColor[i2];
            out.method_75473(vertex(l[p2], l[p2 + 1], l[p2 + 2]), vertex(l[p2 + 3], l[p2 + 4], l[p2 + 5]), color2, this.lineWidth[i2]);
            i2++;
            p2 += 6;
        }
    }

    private class_243 vertex(double x, double y, double z) {
        long hash = ((Double.doubleToRawLongBits(x) * (-7046029254386353131L)) ^ (Double.doubleToRawLongBits(y) * (-4417276706812531889L))) ^ (Double.doubleToRawLongBits(z) * 1609587929392839161L);
        int slot = (int) (hash >>> 55);
        class_243 cached = this.vertices[slot];
        if (cached != null && cached.field_1352 == x && cached.field_1351 == y && cached.field_1350 == z) {
            return cached;
        }
        class_243 made = new class_243(x, y, z);
        this.vertices[slot] = made;
        return made;
    }

    private boolean reserveQuad() {
        if (this.quads < this.quadColor.length) {
            return true;
        }
        if (this.quads >= 12000) {
            return false;
        }
        int grown = Math.min(MAX_QUADS, this.quadColor.length * 2);
        this.quadColor = Arrays.copyOf(this.quadColor, grown);
        this.quadPos = Arrays.copyOf(this.quadPos, grown * 12);
        return true;
    }

    private boolean reserveLine() {
        if (this.lines < this.lineColor.length) {
            return true;
        }
        if (this.lines >= MAX_LINES) {
            return false;
        }
        int grown = Math.min(MAX_LINES, this.lineColor.length * 2);
        this.lineColor = Arrays.copyOf(this.lineColor, grown);
        this.lineWidth = Arrays.copyOf(this.lineWidth, grown);
        this.linePos = Arrays.copyOf(this.linePos, grown * 6);
        return true;
    }
}
