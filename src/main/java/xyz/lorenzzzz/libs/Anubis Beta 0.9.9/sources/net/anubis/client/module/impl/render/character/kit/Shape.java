package net.anubis.client.module.impl.render.character.kit;

import java.util.function.UnaryOperator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shape.class */
@Environment(EnvType.CLIENT)
public abstract class Shape {
    static final float FAR = 100000.0f;
    float minX = -100000.0f;
    float minY = -100000.0f;
    float minZ = -100000.0f;
    float maxX = FAR;
    float maxY = FAR;
    float maxZ = FAR;
    Paint paint;

    public abstract float distance(float f, float f2, float f3);

    protected Shape() {
    }

    float distance(float x, float y, float z, float limit) {
        return distance(x, y, z);
    }

    float shade(float x, float y, float z, Sample out) {
        float d = distance(x, y, z);
        Paint.DEFAULT.apply(x, y, z, out);
        return d;
    }

    public final float sample(float x, float y, float z, Sample out) {
        if (this.paint != null) {
            float d = distance(x, y, z);
            this.paint.apply(x, y, z, out);
            return d;
        }
        return shade(x, y, z, out);
    }

    protected final void bounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.minZ = Math.min(minZ, maxZ);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        this.maxZ = Math.max(minZ, maxZ);
    }

    final void boundsOf(Shape o, float pad) {
        if (!o.bounded()) {
            this.minZ = -100000.0f;
            this.minY = -100000.0f;
            this.minX = -100000.0f;
            this.maxZ = FAR;
            this.maxY = FAR;
            this.maxX = FAR;
            return;
        }
        bounds(o.minX - pad, o.minY - pad, o.minZ - pad, o.maxX + pad, o.maxY + pad, o.maxZ + pad);
    }

    public final boolean bounded() {
        return this.maxX - this.minX < FAR && this.maxY - this.minY < FAR && this.maxZ - this.minZ < FAR;
    }

    public final float minX() {
        return this.minX;
    }

    public final float minY() {
        return this.minY;
    }

    public final float minZ() {
        return this.minZ;
    }

    public final float maxX() {
        return this.maxX;
    }

    public final float maxY() {
        return this.maxY;
    }

    public final float maxZ() {
        return this.maxZ;
    }

    Shape prune(float x0, float y0, float z0, float x1, float y1, float z1) {
        return this;
    }

    Shape map(UnaryOperator<Shape> f) {
        return (Shape) f.apply(this);
    }

    final Shape inherit(Shape from) {
        if (from.paint != null && this.paint == null) {
            this.paint = from.paint;
        }
        return this;
    }

    final float boxGap(float x0, float y0, float z0, float x1, float y1, float z1) {
        float dx = Math.max(0.0f, Math.max(this.minX - x1, x0 - this.maxX));
        float dy = Math.max(0.0f, Math.max(this.minY - y1, y0 - this.maxY));
        float dz = Math.max(0.0f, Math.max(this.minZ - z1, z0 - this.maxZ));
        return (float) Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    final float boundDistance(float x, float y, float z) {
        float f;
        float f2;
        float f3;
        if (x < this.minX) {
            f = this.minX - x;
        } else {
            f = x > this.maxX ? x - this.maxX : 0.0f;
        }
        float dx = f;
        if (y < this.minY) {
            f2 = this.minY - y;
        } else {
            f2 = y > this.maxY ? y - this.maxY : 0.0f;
        }
        float dy = f2;
        if (z < this.minZ) {
            f3 = this.minZ - z;
        } else {
            f3 = z > this.maxZ ? z - this.maxZ : 0.0f;
        }
        float dz = f3;
        return (float) Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    public Shape color(int argb) {
        this.paint = Paint.of(argb);
        return this;
    }

    public Shape paint(Paint paint) {
        this.paint = paint;
        return this;
    }

    public Shape move(float dx, float dy, float dz) {
        return new Shapes.Translate(this, dx, dy, dz);
    }

    public Shape rotate(float degX, float degY, float degZ) {
        return rotate(degX, degY, degZ, 0.0f, 0.0f, 0.0f);
    }

    public Shape rotate(float degX, float degY, float degZ, float cx, float cy, float cz) {
        return new Shapes.Rotate(this, Mat3.euler(degX, degY, degZ), cx, cy, cz);
    }

    public Shape rotateAxis(float ax, float ay, float az, float degrees, float cx, float cy, float cz) {
        return new Shapes.Rotate(this, Mat3.about(ax, ay, az, degrees), cx, cy, cz);
    }

    public Shape scale(float k) {
        return scale(k, 0.0f, 0.0f, 0.0f);
    }

    public Shape scale(float k, float cx, float cy, float cz) {
        return new Shapes.Scale(this, k, cx, cy, cz);
    }

    public Shape mirrorX() {
        return new Shapes.Mirror(this, true);
    }

    public Shape mirrored() {
        return new Shapes.Mirror(this, false);
    }

    public Shape inflate(float radius) {
        return new Shapes.Round(this, radius);
    }

    public Shape shell(float thickness) {
        return new Shapes.Shell(this, thickness);
    }

    public Shape roughen(float amplitude, float size) {
        return new Shapes.Roughen(this, amplitude, size);
    }

    public Shape twist(float degreesPerUnit, float cx, float cz) {
        return new Shapes.Twist(this, degreesPerUnit, cx, cz);
    }

    public Shape union(Shape... others) {
        return Shapes.union(prepend(others));
    }

    public Shape blend(float k, Shape... others) {
        return Shapes.blend(k, prepend(others));
    }

    public Shape subtract(Shape cutter) {
        return Shapes.subtract(this, cutter);
    }

    public Shape subtract(float k, Shape cutter) {
        return Shapes.subtract(k, this, cutter);
    }

    public Shape intersect(Shape other) {
        return Shapes.intersect(this, other);
    }

    public Shape intersect(float k, Shape other) {
        return Shapes.intersect(k, this, other);
    }

    private Shape[] prepend(Shape[] others) {
        Shape[] all = new Shape[others.length + 1];
        all[0] = this;
        System.arraycopy(others, 0, all, 1, others.length);
        return all;
    }
}
