package net.anubis.client.module.impl.render.character.kit;

import java.util.Arrays;
import java.util.function.UnaryOperator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes.class */
@Environment(EnvType.CLIENT)
public final class Shapes {
    private static final ThreadLocal<Scratch> SCRATCH = ThreadLocal.withInitial(Scratch::new);

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes$Field.class */
    @Environment(EnvType.CLIENT)
    public interface Field {
        float distance(float f, float f2, float f3);
    }

    private Shapes() {
    }

    public static Shape sphere(float cx, float cy, float cz, float r) {
        return new Sphere(cx, cy, cz, r);
    }

    public static Shape ellipsoid(float cx, float cy, float cz, float rx, float ry, float rz) {
        return new Ellipsoid(cx, cy, cz, rx, ry, rz);
    }

    public static Shape box(float cx, float cy, float cz, float hx, float hy, float hz) {
        return new Box(cx, cy, cz, hx, hy, hz, 0.0f);
    }

    public static Shape box(float cx, float cy, float cz, float hx, float hy, float hz, float round) {
        return new Box(cx, cy, cz, hx, hy, hz, round);
    }

    public static Shape capsule(float ax, float ay, float az, float bx, float by, float bz, float r) {
        return new RoundCone(ax, ay, az, r, bx, by, bz, r);
    }

    public static Shape cone(float ax, float ay, float az, float ra, float bx, float by, float bz, float rb) {
        return new RoundCone(ax, ay, az, ra, bx, by, bz, rb);
    }

    public static Shape cylinder(float ax, float ay, float az, float bx, float by, float bz, float r, float round) {
        return new Cylinder(ax, ay, az, bx, by, bz, r, round);
    }

    public static Shape torus(float cx, float cy, float cz, float major, float minor) {
        return new Torus(cx, cy, cz, major, minor);
    }

    public static Shape plane(float px, float py, float pz, float nx, float ny, float nz) {
        return new Plane(px, py, pz, nx, ny, nz);
    }

    public static Shape below(float y) {
        return new Plane(0.0f, y, 0.0f, 0.0f, 1.0f, 0.0f);
    }

    public static Shape above(float y) {
        return new Plane(0.0f, y, 0.0f, 0.0f, -1.0f, 0.0f);
    }

    public static Strand bezier(float ax, float ay, float az, float cx, float cy, float cz, float bx, float by, float bz, float r0, float r1) {
        return Strand.of(new float[]{ax, ay, az, r0, cx, cy, cz, (r0 + r1) * 0.5f, bx, by, bz, r1}, true);
    }

    public static Strand strand(float r0, float r1, float... xyz) {
        return Strand.tapered(r0, r1, xyz);
    }

    public static Strand tube(float... xyzr) {
        return Strand.of(xyzr, false);
    }

    public static Shape extrude(Shape2D profile, float halfDepth, float round) {
        return new Extrude(profile, halfDepth, round);
    }

    public static Shape revolve(Shape2D profile) {
        return new Revolve(profile);
    }

    public static Shape field(Field field, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        return new Custom(field, minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static Shape union(Shape... shapes) {
        return shapes.length == 1 ? shapes[0] : new Union(0.0f, shapes);
    }

    public static Shape blend(float k, Shape... shapes) {
        return shapes.length == 1 ? shapes[0] : new Union(k, shapes);
    }

    public static Shape subtract(Shape base, Shape cutter) {
        return new Subtract(0.0f, base, cutter);
    }

    public static Shape subtract(float k, Shape base, Shape cutter) {
        return new Subtract(k, base, cutter);
    }

    public static Shape intersect(Shape a, Shape b) {
        return new Intersect(0.0f, a, b);
    }

    public static Shape intersect(float k, Shape a, Shape b) {
        return new Intersect(k, a, b);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes$Scratch.class */
    @Environment(EnvType.CLIENT)
    private static final class Scratch {
        final float[][] pool = new float[64][];
        int depth;

        private Scratch() {
        }

        float[] take(int size) {
            int d = this.depth;
            this.depth = d + 1;
            if (d >= this.pool.length) {
                return new float[size];
            }
            float[] a = this.pool[d];
            if (a == null || a.length < size) {
                a = new float[Math.max(size, 16)];
                this.pool[d] = a;
            }
            return a;
        }
    }

    static float clamp01(float v) {
        if (v < 0.0f) {
            return 0.0f;
        }
        return Math.min(v, 1.0f);
    }

    static float length(float x, float y, float z) {
        return (float) Math.sqrt((x * x) + (y * y) + (z * z));
    }

    static float boxLowerBound(Shape s, float x, float y, float z) {
        float cx = (s.minX + s.maxX) * 0.5f;
        float cy = (s.minY + s.maxY) * 0.5f;
        float cz = (s.minZ + s.maxZ) * 0.5f;
        float qx = Math.abs(x - cx) - ((s.maxX - s.minX) * 0.5f);
        float qy = Math.abs(y - cy) - ((s.maxY - s.minY) * 0.5f);
        float qz = Math.abs(z - cz) - ((s.maxZ - s.minZ) * 0.5f);
        float ox = Math.max(qx, 0.0f);
        float oy = Math.max(qy, 0.0f);
        float oz = Math.max(qz, 0.0f);
        return ((float) Math.sqrt((ox * ox) + (oy * oy) + (oz * oz))) + Math.min(Math.max(qx, Math.max(qy, qz)), 0.0f);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes$Sphere.class */
    @Environment(EnvType.CLIENT)
    static final class Sphere extends Shape {
        private final float cx;
        private final float cy;
        private final float cz;
        private final float r;

        Sphere(float cx, float cy, float cz, float r) {
            this.cx = cx;
            this.cy = cy;
            this.cz = cz;
            this.r = r;
            bounds(cx - r, cy - r, cz - r, cx + r, cy + r, cz + r);
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        public float distance(float x, float y, float z) {
            return Shapes.length(x - this.cx, y - this.cy, z - this.cz) - this.r;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes$Ellipsoid.class */
    @Environment(EnvType.CLIENT)
    static final class Ellipsoid extends Shape {
        private final float cx;
        private final float cy;
        private final float cz;
        private final float ix;
        private final float iy;
        private final float iz;
        private final float iix;
        private final float iiy;
        private final float iiz;
        private final float rmin;

        Ellipsoid(float cx, float cy, float cz, float rx, float ry, float rz) {
            this.cx = cx;
            this.cy = cy;
            this.cz = cz;
            this.ix = 1.0f / rx;
            this.iy = 1.0f / ry;
            this.iz = 1.0f / rz;
            this.iix = this.ix * this.ix;
            this.iiy = this.iy * this.iy;
            this.iiz = this.iz * this.iz;
            this.rmin = Math.min(rx, Math.min(ry, rz));
            bounds(cx - rx, cy - ry, cz - rz, cx + rx, cy + ry, cz + rz);
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        public float distance(float x, float y, float z) {
            float px = x - this.cx;
            float py = y - this.cy;
            float pz = z - this.cz;
            float k0 = Shapes.length(px * this.ix, py * this.iy, pz * this.iz);
            float k1 = Shapes.length(px * this.iix, py * this.iiy, pz * this.iiz);
            return k1 < 1.0E-9f ? -this.rmin : (k0 * (k0 - 1.0f)) / k1;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes$Box.class */
    @Environment(EnvType.CLIENT)
    static final class Box extends Shape {
        private final float cx;
        private final float cy;
        private final float cz;
        private final float hx;
        private final float hy;
        private final float hz;
        private final float r;

        Box(float cx, float cy, float cz, float hx, float hy, float hz, float round) {
            this.cx = cx;
            this.cy = cy;
            this.cz = cz;
            this.r = Math.max(0.0f, Math.min(round, Math.min(hx, Math.min(hy, hz))));
            this.hx = hx - this.r;
            this.hy = hy - this.r;
            this.hz = hz - this.r;
            bounds(cx - hx, cy - hy, cz - hz, cx + hx, cy + hy, cz + hz);
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        public float distance(float x, float y, float z) {
            float qx = Math.abs(x - this.cx) - this.hx;
            float qy = Math.abs(y - this.cy) - this.hy;
            float qz = Math.abs(z - this.cz) - this.hz;
            float ox = Math.max(qx, 0.0f);
            float oy = Math.max(qy, 0.0f);
            float oz = Math.max(qz, 0.0f);
            return (Shapes.length(ox, oy, oz) + Math.min(Math.max(qx, Math.max(qy, qz)), 0.0f)) - this.r;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes$RoundCone.class */
    @Environment(EnvType.CLIENT)
    static final class RoundCone extends Shape {
        private final float ax;
        private final float ay;
        private final float az;
        private final float bax;
        private final float bay;
        private final float baz;
        private final float ra;
        private final float rb;
        private final float l2;
        private final float rr;
        private final float a2;
        private final float il2;
        private final boolean sphere;
        private final float sx;
        private final float sy;
        private final float sz;
        private final float sr;

        RoundCone(float ax, float ay, float az, float ra, float bx, float by, float bz, float rb) {
            this.ax = ax;
            this.ay = ay;
            this.az = az;
            this.ra = ra;
            this.rb = rb;
            this.bax = bx - ax;
            this.bay = by - ay;
            this.baz = bz - az;
            this.l2 = (this.bax * this.bax) + (this.bay * this.bay) + (this.baz * this.baz);
            this.rr = ra - rb;
            this.a2 = this.l2 - (this.rr * this.rr);
            this.il2 = this.l2 > 1.0E-12f ? 1.0f / this.l2 : 0.0f;
            this.sphere = this.a2 <= 1.0E-6f;
            boolean first = ra >= rb;
            this.sx = first ? ax : bx;
            this.sy = first ? ay : by;
            this.sz = first ? az : bz;
            this.sr = Math.max(ra, rb);
            bounds(Math.min(ax - ra, bx - rb), Math.min(ay - ra, by - rb), Math.min(az - ra, bz - rb), Math.max(ax + ra, bx + rb), Math.max(ay + ra, by + rb), Math.max(az + ra, bz + rb));
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        public float distance(float x, float y, float z) {
            if (this.sphere) {
                return Shapes.length(x - this.sx, y - this.sy, z - this.sz) - this.sr;
            }
            float pax = x - this.ax;
            float pay = y - this.ay;
            float paz = z - this.az;
            float yy = (pax * this.bax) + (pay * this.bay) + (paz * this.baz);
            float zz = yy - this.l2;
            float qx = (pax * this.l2) - (this.bax * yy);
            float qy = (pay * this.l2) - (this.bay * yy);
            float qz = (paz * this.l2) - (this.baz * yy);
            float x2 = (qx * qx) + (qy * qy) + (qz * qz);
            float y2 = yy * yy * this.l2;
            float z2 = zz * zz * this.l2;
            float k = Math.signum(this.rr) * this.rr * this.rr * x2;
            if (Math.signum(zz) * this.a2 * z2 > k) {
                return (((float) Math.sqrt(x2 + z2)) * this.il2) - this.rb;
            }
            return (Math.signum(yy) * this.a2) * y2 < k ? (((float) Math.sqrt(x2 + y2)) * this.il2) - this.ra : ((((float) Math.sqrt((x2 * this.a2) * this.il2)) + (yy * this.rr)) * this.il2) - this.ra;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes$Cylinder.class */
    @Environment(EnvType.CLIENT)
    static final class Cylinder extends Shape {
        private final float ax;
        private final float ay;
        private final float az;
        private final float bax;
        private final float bay;
        private final float baz;
        private final float baba;
        private final float r;
        private final float round;

        Cylinder(float ax, float ay, float az, float bx, float by, float bz, float r, float round) {
            float dx = bx - ax;
            float dy = by - ay;
            float dz = bz - az;
            float len = Shapes.length(dx, dy, dz);
            float rd = Math.max(0.0f, Math.min(round, Math.min(r, len * 0.5f)));
            float ux = len > 1.0E-6f ? dx / len : 0.0f;
            float uy = len > 1.0E-6f ? dy / len : 1.0f;
            float uz = len > 1.0E-6f ? dz / len : 0.0f;
            this.ax = ax + (ux * rd);
            this.ay = ay + (uy * rd);
            this.az = az + (uz * rd);
            this.bax = (bx - (ux * rd)) - this.ax;
            this.bay = (by - (uy * rd)) - this.ay;
            this.baz = (bz - (uz * rd)) - this.az;
            this.baba = Math.max(1.0E-8f, (this.bax * this.bax) + (this.bay * this.bay) + (this.baz * this.baz));
            this.r = r - rd;
            this.round = rd;
            float ex = r * ((float) Math.sqrt(Math.max(0.0f, 1.0f - (ux * ux))));
            float ey = r * ((float) Math.sqrt(Math.max(0.0f, 1.0f - (uy * uy))));
            float ez = r * ((float) Math.sqrt(Math.max(0.0f, 1.0f - (uz * uz))));
            bounds(Math.min(ax, bx) - ex, Math.min(ay, by) - ey, Math.min(az, bz) - ez, Math.max(ax, bx) + ex, Math.max(ay, by) + ey, Math.max(az, bz) + ez);
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        public float distance(float x, float y, float z) {
            float f;
            float pax = x - this.ax;
            float pay = y - this.ay;
            float paz = z - this.az;
            float paba = (pax * this.bax) + (pay * this.bay) + (paz * this.baz);
            float xx = Shapes.length((pax * this.baba) - (this.bax * paba), (pay * this.baba) - (this.bay * paba), (paz * this.baba) - (this.baz * paba)) - (this.r * this.baba);
            float yy = Math.abs(paba - (this.baba * 0.5f)) - (this.baba * 0.5f);
            float x2 = xx * xx;
            float y2 = yy * yy * this.baba;
            if (Math.max(xx, yy) < 0.0f) {
                f = -Math.min(x2, y2);
            } else {
                f = (xx > 0.0f ? x2 : 0.0f) + (yy > 0.0f ? y2 : 0.0f);
            }
            float d = f;
            return ((Math.signum(d) * ((float) Math.sqrt(Math.abs(d)))) / this.baba) - this.round;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes$Torus.class */
    @Environment(EnvType.CLIENT)
    static final class Torus extends Shape {
        private final float cx;
        private final float cy;
        private final float cz;
        private final float major;
        private final float minor;

        Torus(float cx, float cy, float cz, float major, float minor) {
            this.cx = cx;
            this.cy = cy;
            this.cz = cz;
            this.major = major;
            this.minor = minor;
            float e = major + minor;
            bounds(cx - e, cy - minor, cz - e, cx + e, cy + minor, cz + e);
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        public float distance(float x, float y, float z) {
            float px = x - this.cx;
            float pz = z - this.cz;
            float qx = ((float) Math.sqrt((px * px) + (pz * pz))) - this.major;
            float qy = y - this.cy;
            return ((float) Math.sqrt((qx * qx) + (qy * qy))) - this.minor;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes$Plane.class */
    @Environment(EnvType.CLIENT)
    static final class Plane extends Shape {
        private final float px;
        private final float py;
        private final float pz;
        private final float nx;
        private final float ny;
        private final float nz;

        Plane(float px, float py, float pz, float nx, float ny, float nz) {
            float l = Shapes.length(nx, ny, nz);
            this.px = px;
            this.py = py;
            this.pz = pz;
            this.nx = l < 1.0E-8f ? 0.0f : nx / l;
            this.ny = l < 1.0E-8f ? 1.0f : ny / l;
            this.nz = l < 1.0E-8f ? 0.0f : nz / l;
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        public float distance(float x, float y, float z) {
            return ((x - this.px) * this.nx) + ((y - this.py) * this.ny) + ((z - this.pz) * this.nz);
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes$Extrude.class */
    @Environment(EnvType.CLIENT)
    static final class Extrude extends Shape {
        private final Shape2D profile;
        private final float h;
        private final float r;

        Extrude(Shape2D profile, float halfDepth, float round) {
            this.profile = profile;
            this.r = Math.max(0.0f, Math.min(round, halfDepth));
            this.h = halfDepth;
            bounds(profile.minX, profile.minY, -halfDepth, profile.maxX, profile.maxY, halfDepth);
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        public float distance(float x, float y, float z) {
            float d2 = this.profile.distance(x, y) + this.r;
            float wz = (Math.abs(z) - this.h) + this.r;
            float ox = Math.max(d2, 0.0f);
            float oz = Math.max(wz, 0.0f);
            return (Math.min(Math.max(d2, wz), 0.0f) + ((float) Math.sqrt((ox * ox) + (oz * oz)))) - this.r;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes$Revolve.class */
    @Environment(EnvType.CLIENT)
    static final class Revolve extends Shape {
        private final Shape2D profile;

        Revolve(Shape2D profile) {
            this.profile = profile;
            float rad = Math.max(Math.abs(profile.minX), Math.abs(profile.maxX));
            bounds(-rad, profile.minY, -rad, rad, profile.maxY, rad);
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        public float distance(float x, float y, float z) {
            return this.profile.distance((float) Math.sqrt((x * x) + (z * z)), y);
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes$Custom.class */
    @Environment(EnvType.CLIENT)
    static final class Custom extends Shape {
        private final Field field;

        Custom(Field field, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
            this.field = field;
            bounds(minX, minY, minZ, maxX, maxY, maxZ);
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        public float distance(float x, float y, float z) {
            return this.field.distance(x, y, z);
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes$Union.class */
    @Environment(EnvType.CLIENT)
    static final class Union extends Shape {
        private final Shape[] kids;
        private final float k;

        Union(float k, Shape[] kids) {
            if (kids.length == 0) {
                throw new IllegalArgumentException("union needs at least one shape");
            }
            this.k = Math.max(0.0f, k);
            this.kids = (Shape[]) kids.clone();
            boolean open = false;
            float x0 = 100000.0f;
            float y0 = 100000.0f;
            float z0 = 100000.0f;
            float x1 = -100000.0f;
            float y1 = -100000.0f;
            float z1 = -100000.0f;
            for (Shape s : this.kids) {
                if (!s.bounded()) {
                    open = true;
                } else {
                    x0 = Math.min(x0, s.minX);
                    y0 = Math.min(y0, s.minY);
                    z0 = Math.min(z0, s.minZ);
                    x1 = Math.max(x1, s.maxX);
                    y1 = Math.max(y1, s.maxY);
                    z1 = Math.max(z1, s.maxZ);
                }
            }
            if (!open) {
                float pad = this.k * 0.3f;
                bounds(x0 - pad, y0 - pad, z0 - pad, x1 + pad, y1 + pad, z1 + pad);
            }
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        Shape map(UnaryOperator<Shape> f) {
            Shape[] m = new Shape[this.kids.length];
            boolean changed = false;
            for (int i = 0; i < this.kids.length; i++) {
                m[i] = this.kids[i].map(f);
                changed |= m[i] != this.kids[i];
            }
            return (Shape) f.apply(changed ? new Union(this.k, m).inherit(this) : this);
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        Shape prune(float x0, float y0, float z0, float x1, float y1, float z1) {
            float cx = (x0 + x1) * 0.5f;
            float cy = (y0 + y1) * 0.5f;
            float cz = (z0 + z1) * 0.5f;
            float r = Shapes.length(x1 - x0, y1 - y0, z1 - z0) * 0.5f * 1.2f;
            float upper = Float.MAX_VALUE;
            for (Shape s : this.kids) {
                if (s.boxGap(x0, y0, z0, x1, y1, z1) < upper + this.k) {
                    upper = Math.min(upper, s.distance(cx, cy, cz) + r);
                }
            }
            Shape[] kept = new Shape[this.kids.length];
            int n = 0;
            boolean changed = false;
            Shape[] shapeArr = this.kids;
            int length = shapeArr.length;
            for (int i = 0; i < length; i++) {
                Shape s2 = shapeArr[i];
                if (s2.boxGap(x0, y0, z0, x1, y1, z1) >= upper + this.k) {
                    changed = true;
                } else {
                    Shape p = s2.prune(x0, y0, z0, x1, y1, z1);
                    changed |= p != s2;
                    int i2 = n;
                    n++;
                    kept[i2] = p;
                }
            }
            if (!changed || n == 0) {
                return this;
            }
            return n == 1 ? kept[0] : new Union(this.k, (Shape[]) Arrays.copyOf(kept, n));
        }

        private int nearest(float x, float y, float z, float[] bound) {
            int first = 0;
            float min = Float.MAX_VALUE;
            for (int i = 0; i < this.kids.length; i++) {
                float b = Shapes.boxLowerBound(this.kids[i], x, y, z);
                if (bound != null) {
                    bound[i] = b;
                }
                if (b < min) {
                    min = b;
                    first = i;
                }
            }
            return first;
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        public float distance(float x, float y, float z) {
            Shape[] s = this.kids;
            if (s.length <= 4) {
                return distance(x, y, z, (float[]) null);
            }
            Scratch scratch = Shapes.SCRATCH.get();
            float[] bound = scratch.take(s.length);
            try {
                return distance(x, y, z, bound);
            } finally {
                scratch.depth--;
            }
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        float distance(float x, float y, float z, float limit) {
            Shape[] s = this.kids;
            if (s.length <= 4) {
                return evaluate(x, y, z, null, limit);
            }
            Scratch scratch = Shapes.SCRATCH.get();
            float[] bound = scratch.take(s.length);
            try {
                return evaluate(x, y, z, bound, limit);
            } finally {
                scratch.depth--;
            }
        }

        private float distance(float x, float y, float z, float[] bound) {
            return evaluate(x, y, z, bound, Float.MAX_VALUE);
        }

        private float evaluate(float x, float y, float z, float[] bound, float limit) {
            Shape[] s = this.kids;
            int first = nearest(x, y, z, bound);
            float firstBound = bound != null ? bound[first] : Shapes.boxLowerBound(s[first], x, y, z);
            if (firstBound >= limit) {
                return firstBound;
            }
            float kk = this.k;
            float best = s[first].distance(x, y, z, limit == Float.MAX_VALUE ? Float.MAX_VALUE : limit + kk);
            for (int i = 0; i < s.length; i++) {
                if (i != first) {
                    float b = bound != null ? bound[i] : Shapes.boxLowerBound(s[i], x, y, z);
                    if (b < best + kk) {
                        float d = s[i].distance(x, y, z, best + kk);
                        if (kk > 0.0f) {
                            float h = Shapes.clamp01(0.5f + ((0.5f * (d - best)) / kk));
                            best = (d + ((best - d) * h)) - ((kk * h) * (1.0f - h));
                        } else if (d < best) {
                            best = d;
                        }
                    }
                }
            }
            return best;
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        float shade(float x, float y, float z, Sample out) {
            Shape[] s = this.kids;
            int first = nearest(x, y, z, null);
            float best = s[first].sample(x, y, z, out);
            Sample tmp = null;
            float kk = this.k;
            for (int i = 0; i < s.length; i++) {
                if (i != first) {
                    Shape c = s[i];
                    if (Shapes.boxLowerBound(c, x, y, z) < best + kk) {
                        if (tmp == null) {
                            tmp = new Sample();
                        }
                        float d = c.sample(x, y, z, tmp);
                        if (kk > 0.0f) {
                            float h = Shapes.clamp01(0.5f + ((0.5f * (d - best)) / kk));
                            best = (d + ((best - d) * h)) - ((kk * h) * (1.0f - h));
                            out.mix(tmp, 1.0f - h);
                        } else if (d < best) {
                            best = d;
                            out.set(tmp);
                        }
                    }
                }
            }
            return best;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes$Subtract.class */
    @Environment(EnvType.CLIENT)
    static final class Subtract extends Shape {
        private final Shape base;
        private final Shape cutter;
        private final float k;

        Subtract(float k, Shape base, Shape cutter) {
            this.k = Math.max(0.0f, k);
            this.base = base;
            this.cutter = cutter;
            boundsOf(base, this.k * 0.3f);
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        Shape prune(float x0, float y0, float z0, float x1, float y1, float z1) {
            Shape a = this.base.prune(x0, y0, z0, x1, y1, z1);
            Shape b = this.cutter.prune(x0, y0, z0, x1, y1, z1);
            return (a == this.base && b == this.cutter) ? this : new Subtract(this.k, a, b);
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        Shape map(UnaryOperator<Shape> f) {
            Shape a = this.base.map(f);
            Shape b = this.cutter.map(f);
            return (Shape) f.apply((a == this.base && b == this.cutter) ? this : new Subtract(this.k, a, b).inherit(this));
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        public float distance(float x, float y, float z) {
            float a = this.base.distance(x, y, z);
            if (Shapes.boxLowerBound(this.cutter, x, y, z) >= this.k - a) {
                return a;
            }
            float b = this.cutter.distance(x, y, z);
            if (this.k <= 0.0f) {
                return Math.max(a, -b);
            }
            float h = Shapes.clamp01(0.5f - ((0.5f * (a + b)) / this.k));
            return a + (((-b) - a) * h) + (this.k * h * (1.0f - h));
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        float shade(float x, float y, float z, Sample out) {
            float a = this.base.sample(x, y, z, out);
            if (Shapes.boxLowerBound(this.cutter, x, y, z) >= this.k - a) {
                return a;
            }
            if (this.cutter.paint == null) {
                float b = this.cutter.distance(x, y, z);
                if (this.k <= 0.0f) {
                    return Math.max(a, -b);
                }
                float h = Shapes.clamp01(0.5f - ((0.5f * (a + b)) / this.k));
                return a + (((-b) - a) * h) + (this.k * h * (1.0f - h));
            }
            Sample tmp = new Sample();
            float b2 = this.cutter.sample(x, y, z, tmp);
            if (this.k > 0.0f) {
                float h2 = Shapes.clamp01(0.5f - ((0.5f * (a + b2)) / this.k));
                out.mix(tmp, h2);
                return a + (((-b2) - a) * h2) + (this.k * h2 * (1.0f - h2));
            }
            if ((-b2) > a) {
                out.set(tmp);
                return -b2;
            }
            return a;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes$Intersect.class */
    @Environment(EnvType.CLIENT)
    static final class Intersect extends Shape {
        private final Shape a;
        private final Shape b;
        private final float k;

        Intersect(float k, Shape a, Shape b) {
            this.k = Math.max(0.0f, k);
            this.a = a;
            this.b = b;
            if (a.bounded() && b.bounded()) {
                bounds(Math.max(a.minX, b.minX), Math.max(a.minY, b.minY), Math.max(a.minZ, b.minZ), Math.min(a.maxX, b.maxX), Math.min(a.maxY, b.maxY), Math.min(a.maxZ, b.maxZ));
            } else if (a.bounded()) {
                boundsOf(a, 0.0f);
            } else if (b.bounded()) {
                boundsOf(b, 0.0f);
            }
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        Shape prune(float x0, float y0, float z0, float x1, float y1, float z1) {
            Shape pa = this.a.prune(x0, y0, z0, x1, y1, z1);
            Shape pb = this.b.prune(x0, y0, z0, x1, y1, z1);
            return (pa == this.a && pb == this.b) ? this : new Intersect(this.k, pa, pb);
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        Shape map(UnaryOperator<Shape> f) {
            Shape pa = this.a.map(f);
            Shape pb = this.b.map(f);
            return (Shape) f.apply((pa == this.a && pb == this.b) ? this : new Intersect(this.k, pa, pb).inherit(this));
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        public float distance(float x, float y, float z) {
            float da = this.a.distance(x, y, z);
            float db = this.b.distance(x, y, z);
            if (this.k <= 0.0f) {
                return Math.max(da, db);
            }
            float h = Shapes.clamp01(0.5f - ((0.5f * (db - da)) / this.k));
            return db + ((da - db) * h) + (this.k * h * (1.0f - h));
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        float shade(float x, float y, float z, Sample out) {
            float da = this.a.sample(x, y, z, out);
            if (this.b.paint == null) {
                float db = this.b.distance(x, y, z);
                if (this.k <= 0.0f) {
                    return Math.max(da, db);
                }
                float h = Shapes.clamp01(0.5f - ((0.5f * (db - da)) / this.k));
                return db + ((da - db) * h) + (this.k * h * (1.0f - h));
            }
            Sample tmp = new Sample();
            float db2 = this.b.sample(x, y, z, tmp);
            if (this.k > 0.0f) {
                float h2 = Shapes.clamp01(0.5f - ((0.5f * (db2 - da)) / this.k));
                out.mix(tmp, 1.0f - h2);
                return db2 + ((da - db2) * h2) + (this.k * h2 * (1.0f - h2));
            }
            if (db2 > da) {
                out.set(tmp);
                return db2;
            }
            return da;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes$Translate.class */
    @Environment(EnvType.CLIENT)
    static final class Translate extends Shape {
        private final Shape child;
        private final float dx;
        private final float dy;
        private final float dz;

        Translate(Shape child, float dx, float dy, float dz) {
            this.child = child;
            this.dx = dx;
            this.dy = dy;
            this.dz = dz;
            if (child.bounded()) {
                bounds(child.minX + dx, child.minY + dy, child.minZ + dz, child.maxX + dx, child.maxY + dy, child.maxZ + dz);
            }
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        Shape prune(float x0, float y0, float z0, float x1, float y1, float z1) {
            Shape p = this.child.prune(x0 - this.dx, y0 - this.dy, z0 - this.dz, x1 - this.dx, y1 - this.dy, z1 - this.dz);
            return p == this.child ? this : new Translate(p, this.dx, this.dy, this.dz);
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        Shape map(UnaryOperator<Shape> f) {
            Shape p = this.child.map(f);
            return (Shape) f.apply(p == this.child ? this : new Translate(p, this.dx, this.dy, this.dz).inherit(this));
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        public float distance(float x, float y, float z) {
            return this.child.distance(x - this.dx, y - this.dy, z - this.dz);
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        float shade(float x, float y, float z, Sample out) {
            return this.child.sample(x - this.dx, y - this.dy, z - this.dz, out);
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes$Rotate.class */
    @Environment(EnvType.CLIENT)
    static final class Rotate extends Shape {
        private final Shape child;
        private final Mat3 inv;
        private final float cx;
        private final float cy;
        private final float cz;

        Rotate(Shape child, Mat3 m, float cx, float cy, float cz) {
            this.child = child;
            this.inv = m.transpose();
            this.cx = cx;
            this.cy = cy;
            this.cz = cz;
            if (child.bounded()) {
                float x0 = 100000.0f;
                float y0 = 100000.0f;
                float z0 = 100000.0f;
                float x1 = -100000.0f;
                float y1 = -100000.0f;
                float z1 = -100000.0f;
                for (int i = 0; i < 8; i++) {
                    float px = ((i & 1) == 0 ? child.minX : child.maxX) - cx;
                    float py = ((i & 2) == 0 ? child.minY : child.maxY) - cy;
                    float pz = ((i & 4) == 0 ? child.minZ : child.maxZ) - cz;
                    float rx = m.x(px, py, pz) + cx;
                    float ry = m.y(px, py, pz) + cy;
                    float rz = m.z(px, py, pz) + cz;
                    x0 = Math.min(x0, rx);
                    y0 = Math.min(y0, ry);
                    z0 = Math.min(z0, rz);
                    x1 = Math.max(x1, rx);
                    y1 = Math.max(y1, ry);
                    z1 = Math.max(z1, rz);
                }
                bounds(x0, y0, z0, x1, y1, z1);
            }
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        Shape prune(float x0, float y0, float z0, float x1, float y1, float z1) {
            float lx0 = 100000.0f;
            float ly0 = 100000.0f;
            float lz0 = 100000.0f;
            float lx1 = -100000.0f;
            float ly1 = -100000.0f;
            float lz1 = -100000.0f;
            for (int i = 0; i < 8; i++) {
                float px = ((i & 1) == 0 ? x0 : x1) - this.cx;
                float py = ((i & 2) == 0 ? y0 : y1) - this.cy;
                float pz = ((i & 4) == 0 ? z0 : z1) - this.cz;
                float rx = this.inv.x(px, py, pz) + this.cx;
                float ry = this.inv.y(px, py, pz) + this.cy;
                float rz = this.inv.z(px, py, pz) + this.cz;
                lx0 = Math.min(lx0, rx);
                ly0 = Math.min(ly0, ry);
                lz0 = Math.min(lz0, rz);
                lx1 = Math.max(lx1, rx);
                ly1 = Math.max(ly1, ry);
                lz1 = Math.max(lz1, rz);
            }
            Shape p = this.child.prune(lx0, ly0, lz0, lx1, ly1, lz1);
            return p == this.child ? this : new Rotate(p, this.inv.transpose(), this.cx, this.cy, this.cz);
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        Shape map(UnaryOperator<Shape> f) {
            Shape p = this.child.map(f);
            return (Shape) f.apply(p == this.child ? this : new Rotate(p, this.inv.transpose(), this.cx, this.cy, this.cz).inherit(this));
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        public float distance(float x, float y, float z) {
            float px = x - this.cx;
            float py = y - this.cy;
            float pz = z - this.cz;
            return this.child.distance(this.inv.x(px, py, pz) + this.cx, this.inv.y(px, py, pz) + this.cy, this.inv.z(px, py, pz) + this.cz);
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        float shade(float x, float y, float z, Sample out) {
            float px = x - this.cx;
            float py = y - this.cy;
            float pz = z - this.cz;
            return this.child.sample(this.inv.x(px, py, pz) + this.cx, this.inv.y(px, py, pz) + this.cy, this.inv.z(px, py, pz) + this.cz, out);
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes$Scale.class */
    @Environment(EnvType.CLIENT)
    static final class Scale extends Shape {
        private final Shape child;
        private final float k;
        private final float ik;
        private final float cx;
        private final float cy;
        private final float cz;

        Scale(Shape child, float k, float cx, float cy, float cz) {
            this.child = child;
            this.k = k;
            this.ik = 1.0f / k;
            this.cx = cx;
            this.cy = cy;
            this.cz = cz;
            if (child.bounded()) {
                bounds(((child.minX - cx) * k) + cx, ((child.minY - cy) * k) + cy, ((child.minZ - cz) * k) + cz, ((child.maxX - cx) * k) + cx, ((child.maxY - cy) * k) + cy, ((child.maxZ - cz) * k) + cz);
            }
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        Shape prune(float x0, float y0, float z0, float x1, float y1, float z1) {
            float ax = ((x0 - this.cx) * this.ik) + this.cx;
            float bx = ((x1 - this.cx) * this.ik) + this.cx;
            float ay = ((y0 - this.cy) * this.ik) + this.cy;
            float by = ((y1 - this.cy) * this.ik) + this.cy;
            float az = ((z0 - this.cz) * this.ik) + this.cz;
            float bz = ((z1 - this.cz) * this.ik) + this.cz;
            Shape p = this.child.prune(Math.min(ax, bx), Math.min(ay, by), Math.min(az, bz), Math.max(ax, bx), Math.max(ay, by), Math.max(az, bz));
            return p == this.child ? this : new Scale(p, this.k, this.cx, this.cy, this.cz);
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        Shape map(UnaryOperator<Shape> f) {
            Shape p = this.child.map(f);
            return (Shape) f.apply(p == this.child ? this : new Scale(p, this.k, this.cx, this.cy, this.cz).inherit(this));
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        public float distance(float x, float y, float z) {
            return this.child.distance(((x - this.cx) * this.ik) + this.cx, ((y - this.cy) * this.ik) + this.cy, ((z - this.cz) * this.ik) + this.cz) * this.k;
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        float shade(float x, float y, float z, Sample out) {
            return this.child.sample(((x - this.cx) * this.ik) + this.cx, ((y - this.cy) * this.ik) + this.cy, ((z - this.cz) * this.ik) + this.cz, out) * this.k;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes$Mirror.class */
    @Environment(EnvType.CLIENT)
    static final class Mirror extends Shape {
        private final Shape child;
        private final boolean keep;

        Mirror(Shape child, boolean keep) {
            this.child = child;
            this.keep = keep;
            if (child.bounded()) {
                if (keep) {
                    bounds(Math.min(child.minX, -child.maxX), child.minY, child.minZ, Math.max(child.maxX, -child.minX), child.maxY, child.maxZ);
                } else {
                    bounds(-child.maxX, child.minY, child.minZ, -child.minX, child.maxY, child.maxZ);
                }
            }
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        Shape prune(float x0, float y0, float z0, float x1, float y1, float z1) {
            Shape p = this.keep ? this.child.prune(Math.min(x0, -x1), y0, z0, Math.max(x1, -x0), y1, z1) : this.child.prune(-x1, y0, z0, -x0, y1, z1);
            return p == this.child ? this : new Mirror(p, this.keep);
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        Shape map(UnaryOperator<Shape> f) {
            Shape p = this.child.map(f);
            return (Shape) f.apply(p == this.child ? this : new Mirror(p, this.keep).inherit(this));
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        public float distance(float x, float y, float z) {
            if (!this.keep) {
                return this.child.distance(-x, y, z);
            }
            float a = this.child.distance(x, y, z);
            return Shapes.boxLowerBound(this.child, -x, y, z) >= a ? a : Math.min(a, this.child.distance(-x, y, z));
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        float shade(float x, float y, float z, Sample out) {
            if (!this.keep) {
                return this.child.sample(-x, y, z, out);
            }
            float a = this.child.distance(x, y, z);
            float b = Shapes.boxLowerBound(this.child, -x, y, z) >= a ? Float.MAX_VALUE : this.child.distance(-x, y, z);
            return b < a ? this.child.sample(-x, y, z, out) : this.child.sample(x, y, z, out);
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes$Round.class */
    @Environment(EnvType.CLIENT)
    static final class Round extends Shape {
        private final Shape child;
        private final float r;

        Round(Shape child, float r) {
            this.child = child;
            this.r = r;
            boundsOf(child, Math.max(0.0f, r));
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        Shape prune(float x0, float y0, float z0, float x1, float y1, float z1) {
            Shape p = this.child.prune(x0, y0, z0, x1, y1, z1);
            return p == this.child ? this : new Round(p, this.r);
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        Shape map(UnaryOperator<Shape> f) {
            Shape p = this.child.map(f);
            return (Shape) f.apply(p == this.child ? this : new Round(p, this.r).inherit(this));
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        public float distance(float x, float y, float z) {
            return this.child.distance(x, y, z) - this.r;
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        float shade(float x, float y, float z, Sample out) {
            return this.child.sample(x, y, z, out) - this.r;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes$Shell.class */
    @Environment(EnvType.CLIENT)
    static final class Shell extends Shape {
        private final Shape child;
        private final float half;

        Shell(Shape child, float thickness) {
            this.child = child;
            this.half = Math.abs(thickness) * 0.5f;
            boundsOf(child, this.half);
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        Shape prune(float x0, float y0, float z0, float x1, float y1, float z1) {
            Shape p = this.child.prune(x0, y0, z0, x1, y1, z1);
            return p == this.child ? this : new Shell(p, this.half * 2.0f);
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        Shape map(UnaryOperator<Shape> f) {
            Shape p = this.child.map(f);
            return (Shape) f.apply(p == this.child ? this : new Shell(p, this.half * 2.0f).inherit(this));
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        public float distance(float x, float y, float z) {
            return Math.abs(this.child.distance(x, y, z)) - this.half;
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        float shade(float x, float y, float z, Sample out) {
            return Math.abs(this.child.sample(x, y, z, out)) - this.half;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes$Roughen.class */
    @Environment(EnvType.CLIENT)
    static final class Roughen extends Shape {
        private final Shape child;
        private final float amp;
        private final float freq;

        Roughen(Shape child, float amplitude, float size) {
            this.child = child;
            this.amp = amplitude;
            this.freq = 1.0f / Math.max(0.01f, size);
            boundsOf(child, Math.abs(amplitude));
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        Shape prune(float x0, float y0, float z0, float x1, float y1, float z1) {
            Shape p = this.child.prune(x0, y0, z0, x1, y1, z1);
            return p == this.child ? this : new Roughen(p, this.amp, 1.0f / this.freq);
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        Shape map(UnaryOperator<Shape> f) {
            Shape p = this.child.map(f);
            return (Shape) f.apply(p == this.child ? this : new Roughen(p, this.amp, 1.0f / this.freq).inherit(this));
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        public float distance(float x, float y, float z) {
            float d = this.child.distance(x, y, z);
            return d > (this.amp * 1.5f) + 0.5f ? d : d + (this.amp * Noise.noise(x * this.freq, y * this.freq, z * this.freq));
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        float shade(float x, float y, float z, Sample out) {
            return this.child.sample(x, y, z, out) + (this.amp * Noise.noise(x * this.freq, y * this.freq, z * this.freq));
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Shapes$Twist.class */
    @Environment(EnvType.CLIENT)
    static final class Twist extends Shape {
        private final Shape child;
        private final float k;
        private final float cx;
        private final float cz;

        Twist(Shape child, float degreesPerUnit, float cx, float cz) {
            this.child = child;
            this.k = (float) Math.toRadians(degreesPerUnit);
            this.cx = cx;
            this.cz = cz;
            if (child.bounded()) {
                float r = 0.0f;
                for (int i = 0; i < 4; i++) {
                    float px = ((i & 1) == 0 ? child.minX : child.maxX) - cx;
                    float pz = ((i & 2) == 0 ? child.minZ : child.maxZ) - cz;
                    r = Math.max(r, (float) Math.sqrt((px * px) + (pz * pz)));
                }
                bounds(cx - r, child.minY, cz - r, cx + r, child.maxY, cz + r);
            }
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        Shape map(UnaryOperator<Shape> f) {
            Shape p = this.child.map(f);
            return (Shape) f.apply(p == this.child ? this : new Twist(p, (float) Math.toDegrees(this.k), this.cx, this.cz).inherit(this));
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        public float distance(float x, float y, float z) {
            float a = (-this.k) * y;
            float c = (float) Math.cos(a);
            float s = (float) Math.sin(a);
            float px = x - this.cx;
            float pz = z - this.cz;
            return this.child.distance((c * px) + (s * pz) + this.cx, y, ((-s) * px) + (c * pz) + this.cz);
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Shape
        float shade(float x, float y, float z, Sample out) {
            float a = (-this.k) * y;
            float c = (float) Math.cos(a);
            float s = (float) Math.sin(a);
            float px = x - this.cx;
            float pz = z - this.cz;
            return this.child.sample((c * px) + (s * pz) + this.cx, y, ((-s) * px) + (c * pz) + this.cz, out);
        }
    }
}
