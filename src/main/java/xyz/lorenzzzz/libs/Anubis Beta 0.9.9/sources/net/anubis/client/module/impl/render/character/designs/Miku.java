package net.anubis.client.module.impl.render.character.designs;

import java.util.ArrayList;
import java.util.List;
import net.anubis.client.module.impl.render.character.kit.Bone;
import net.anubis.client.module.impl.render.character.kit.Canvas;
import net.anubis.client.module.impl.render.character.kit.CharacterDesign;
import net.anubis.client.module.impl.render.character.kit.Colors;
import net.anubis.client.module.impl.render.character.kit.Decal;
import net.anubis.client.module.impl.render.character.kit.Motion;
import net.anubis.client.module.impl.render.character.kit.Paint;
import net.anubis.client.module.impl.render.character.kit.Paint2D;
import net.anubis.client.module.impl.render.character.kit.Part;
import net.anubis.client.module.impl.render.character.kit.Rig;
import net.anubis.client.module.impl.render.character.kit.Shape;
import net.anubis.client.module.impl.render.character.kit.Shape2D;
import net.anubis.client.module.impl.render.character.kit.Shapes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/designs/Miku.class */
@Environment(EnvType.CLIENT)
public final class Miku extends CharacterDesign {
    private static final int SKIN = -8241;
    private static final int SKIN_SHADE = -1198942;
    private static final int SKIN_LINE = -5740452;
    private static final int HAIR_ROOT = -14705514;
    private static final int HAIR = -12793152;
    private static final int HAIR_TIP = -7936291;
    private static final int HAIR_SHADE = -15368325;
    private static final int HAIR_LINE = -15903662;
    private static final int SHINE = -857866245;
    private static final int SHIRT = -2499360;
    private static final int SHIRT_SHADE = -5525063;
    private static final int SHIRT_LINE = -10788500;
    private static final int BLACK = -13618373;
    private static final int BLACK_SHADE = -14934492;
    private static final int BLACK_IN = -15592681;
    private static final int BLACK_LINE = -16119026;
    private static final int TEAL = -13645638;
    private static final int TEAL_DARK = -14971002;
    private static final int MAGENTA = -1882484;
    private static final int TATTOO = -2608065;
    private static final float SKIRT_TOP = 17.9f;
    private static final float SKIRT_HEM = 13.3f;
    private static final int PLEATS = 18;
    private static final float HS = 1.12f;
    private static final float DROP = 0.4f;
    private static final float HX = 0.0f;
    private static final float HY = 28.9f;
    private static final float HZ = -0.3f;
    private static final float PPU = 56.88889f;
    private static final float FACE_TOP = 29.95f;
    private static final float[][] LOCKS = {new float[]{-0.1f, 28.3f, -2.0f, 0.95f, 0.12f, 0.34f}, new float[]{-1.0f, 28.75f, -25.0f, 0.95f, 0.28f, 0.3f}, new float[]{-1.92f, 28.4f, -47.0f, 0.92f, 0.26f, 0.3f}, new float[]{0.9f, 28.85f, 24.0f, 0.95f, 0.28f, 0.3f}, new float[]{1.9f, 28.5f, 46.0f, 0.92f, 0.25f, 0.3f}, new float[]{-0.55f, 28.5f, -13.0f, 0.45f, 0.16f, 0.44f}, new float[]{0.46f, 28.62f, 11.0f, 0.45f, 0.18f, 0.44f}, new float[]{-1.46f, 28.65f, -36.0f, 0.42f, 0.2f, 0.42f}, new float[]{1.42f, 28.7f, 35.0f, 0.42f, 0.2f, 0.42f}};

    public Miku() {
        super("miku", "Miku");
    }

    @Override // net.anubis.client.module.impl.render.character.kit.CharacterDesign
    protected void define(Rig rig) {
        rig.scale(1.0f).outline(-14011336, 0.12f).occlusion(0.3f).flatLight(0.55f);
        float[][] locks = lockPaths();
        Paint hair = hairPaint();
        head(rig, locks);
        hair(rig, hair);
        fringe(rig, locks);
        twinTails(rig);
        headset(rig);
        body(rig);
        skirt(rig);
        arms(rig);
        legs(rig);
    }

    private static Shape up(Shape s) {
        return s.scale(HS, HX, 24.6f, -0.2f).move(HX, -0.4f, HX);
    }

    private static float tx(float x) {
        return x * HS;
    }

    private static float ty(float y) {
        return (24.6f + ((y - 24.6f) * HS)) - DROP;
    }

    private static float tz(float z) {
        return (-0.2f) + ((z + 0.2f) * HS);
    }

    private static Shape headBase() {
        return Shapes.blend(0.85f, Shapes.ellipsoid(HX, 28.72f, -0.15f, 2.28f, 2.4f, 2.35f), Shapes.ellipsoid(HX, 27.75f, 0.55f, 2.05f, 1.3f, 1.75f), Shapes.cone(HX, 27.3f, 0.62f, 1.35f, HX, 26.05f, 1.2f, 0.3f));
    }

    private static void head(Rig rig, float[][] locks) {
        Shape neck = Shapes.capsule(HX, 23.3f, -0.45f, HX, 25.9f, HZ, 0.58f);
        float chinY = ty(25.82f);
        float chinZ = tz(1.2f);
        Paint skin = Paint.field((x, y, z) -> {
            float jaw = chinY + (Math.max(HX, chinZ - z) * 0.62f);
            float t = smooth(jaw + 0.05f, jaw - 0.2f, y) * (0.6f + (DROP * smooth(23.4f, 24.8f, y)));
            return Colors.mix(SKIN, SKIN_SHADE, t);
        });
        Shape skull = up(headBase().intersect(Shapes.below(30.2f)));
        rig.part(Bone.HEAD).pivot(HX, 24.6f, -0.2f).shape(Shapes.blend(0.45f, skull, neck).paint(skin)).detail(2.0f).flatLight(1.0f).occlusion(0.22f).outline(SKIN_LINE, 0.1f).decal(face(locks));
    }

    private static float[][] lockPaths() {
        Shape head = headBase();
        float[][] out = new float[LOCKS.length][];
        for (int i = 0; i < LOCKS.length; i++) {
            float[] l = LOCKS[i];
            float[] a = onHead(36.0f, l[2], -0.15f);
            float[] b = onHead(58.0f, l[2], 0.05f);
            float x2 = b[0] + ((l[0] - b[0]) * 0.75f);
            float y2 = l[1] + 0.68f;
            float x3 = l[0] - (Math.signum(l[0]) * l[4]);
            out[i] = new float[]{a[0], a[1], a[2], b[0], b[1], b[2], x2, y2, frontZ(head, x2, y2) + l[5], x3, l[1], frontZ(head, x3, l[1]) + 0.22f, l[3]};
        }
        return out;
    }

    private static float frontZ(Shape s, float x, float y) {
        float z;
        float f = 3.5f;
        while (true) {
            z = f;
            if (z <= -1.0f || s.distance(x, y, z) <= HX) {
                break;
            }
            f = z - 0.02f;
        }
        return z;
    }

    private static void fringe(Rig rig, float[][] locks) {
        Shape[] parts = new Shape[locks.length];
        Shape[] blades = new Shape[locks.length];
        for (int i = 0; i < locks.length; i++) {
            float[] p = locks[i];
            float w = p[12];
            Shape s = Shapes.tube(p[0], p[1], p[2], 0.9f * w, p[3], p[4], p[5], 1.15f * w, p[6], p[7], p[8], 0.95f * w, p[9], p[10], p[11], 0.12f).flattenFrom(3.0f, HX, 28.7f, -0.15f);
            blades[i] = Shapes.extrude(Shape2D.polygon(blade(p)), 5.0f, HX);
            parts[i] = Shapes.intersect(s, blades[i]);
        }
        Shape bangs = Shapes.intersect(Shapes.blend(0.15f, parts), Shapes.union(blades));
        Shape skin = headBase();
        Paint paint = Paint.field((x, y, z) -> {
            int c = Colors.mix(Colors.mix(HAIR_ROOT, HAIR, 0.45f), HAIR, smooth(30.9f, 29.7f, y));
            return Colors.mix(Colors.mix(c, HAIR_TIP, 0.45f * smooth(29.5f, 28.5f, y)), HAIR_SHADE, 0.6f * (1.0f - smooth(0.12f, 0.42f, skin.distance(x, y, z))));
        });
        rig.extra("fringe", Bone.HEAD).shape(up(bangs.paint(paint))).resolution(0.45f).detail(1.05f).flatLight(0.7f).occlusion(0.1f).outline(HAIR_LINE, 0.09f).decal(shine());
    }

    private static float[] blade(float[] p) {
        float w = p[12];
        boolean right = p[9] < HX;
        float x0 = p[3];
        float cx = p[6];
        float cy = p[7];
        float x1 = p[9];
        float y1 = p[10];
        float[] xy = new float[((2 * 12) + 1) * 2];
        for (int i = 0; i <= 12; i++) {
            float t = i / 12;
            float u = 1.0f - t;
            float bx = (u * u * x0) + (2.0f * u * t * cx) + (t * t * x1);
            float by = (u * u * 31.6f) + (2.0f * u * t * cy) + (t * t * y1);
            float tx = (u * (cx - x0)) + (t * (x1 - cx));
            float ty = (u * (cy - 31.6f)) + (t * (y1 - cy));
            float tl = (float) Math.sqrt((tx * tx) + (ty * ty));
            float nx = (-ty) / tl;
            float ny = tx / tl;
            if ((nx > HX) != right) {
                nx = -nx;
                ny = -ny;
            }
            float wi = w * (1.0f - smooth(0.6f, 1.0f, t));
            float wo = w * (1.0f - ((float) Math.pow(smooth(0.3f, 1.0f, t), 0.75d)));
            xy[i * 2] = bx + (nx * wi);
            xy[(i * 2) + 1] = by + (ny * wi);
            if (i < 12) {
                xy[((2 * 12) - i) * 2] = bx - (nx * wo);
                xy[(((2 * 12) - i) * 2) + 1] = by - (ny * wo);
            }
        }
        return xy;
    }

    private static Decal shine() {
        Canvas c = new Canvas(280, 22);
        float[][] dashes = {new float[]{-68.0f, -54.0f}, new float[]{-45.0f, -25.0f}, new float[]{-13.0f, 9.0f}, new float[]{20.0f, 38.0f}, new float[]{48.0f, 64.0f}};
        for (float[] d : dashes) {
            float a0 = d[0];
            float a1 = d[1];
            float am = (a0 + a1) * 0.5f;
            float w = 6.5f - (Math.abs(am) * 0.025f);
            c.fill(Shape2D.curve(140.0f + (a0 * 2.0f), ringY(a0), 140.0f + a0 + am, ringY((a0 + am) * 0.5f), 140.0f + (am * 2.0f), ringY(am), 1.0f, w), SHINE);
            c.fill(Shape2D.curve(140.0f + (am * 2.0f), ringY(am), 140.0f + am + a1, ringY((am + a1) * 0.5f), 140.0f + (a1 * 2.0f), ringY(a1), w, 1.0f), SHINE);
        }
        return Decal.wrap(c, HX, ty(30.16f), tz(HZ), HX, 1.0f, HX, HX, HX, 1.0f, 140.0f, 0.6272f).radius(1.2f, 3.8f).offset(0.03f);
    }

    private static float ringY(float deg) {
        float y = 30.4f - (0.36f * (1.0f - ((float) Math.cos(Math.toRadians(deg)))));
        return (30.44f - y) * 40.0f;
    }

    private static void hair(Rig rig, Paint paint) {
        List<Shape> cap = new ArrayList<>();
        cap.add(Shapes.ellipsoid(HX, HY, HZ, 2.58f, 2.62f, 2.7f));
        for (float f : new float[]{64.0f, 88.0f, 272.0f, 296.0f}) {
            cap.add(capLock(f, 5.0f, 98.0f, 0.8f, 0.62f));
        }
        float f2 = 130.0f;
        while (true) {
            float phi = f2;
            if (phi > 230.0f) {
                Shape faceCut = Shapes.box(HX, 26.3f, 2.3f, 2.95f, 3.3f, 2.8f, 0.9f);
                Shape sideFill = Shapes.blend(0.3f, Shapes.strand(0.62f, 0.44f, -2.3f, 29.6f, -0.05f, -2.48f, 28.1f, -0.12f, -2.45f, 26.5f, -0.22f).flatten(1.6f, 1.0f, HX, HX), Shapes.strand(0.68f, 0.44f, -2.25f, 29.5f, -0.95f, -2.46f, 27.9f, -1.05f, -2.45f, 26.3f, -1.12f).flatten(1.6f, 1.0f, HX, HX)).mirrorX();
                Shape nape = Shapes.strand(0.9f, 0.3f, HX, 28.0f, -2.35f, HX, 26.6f, -2.2f, HX, 25.6f, -1.95f).flatten(2.2f, HX, HX, 1.0f);
                Shape capShape = Shapes.blend(0.25f, Shapes.subtract(Shapes.blend(0.3f, (Shape[]) cap.toArray(new Shape[0])), faceCut).paint(paint), sideFill.paint(paint.occlusion(0.35f)), nape.paint(paint));
                rig.extra("hair", Bone.HEAD).shape(up(capShape)).detail(1.1f).flatLight(0.55f).occlusion(0.22f).outline(HAIR_LINE, 0.09f).decal(shine());
                Shape front = Shapes.tube(-1.95f, 30.4f, 0.92f, 0.55f, -2.3f, 29.3f, HS, 0.6f, -2.42f, 27.8f, 1.0f, 0.55f, -2.4f, 25.9f, 0.8f, 0.46f, -2.3f, 24.0f, 0.78f, 0.36f, -2.15f, 22.6f, 0.82f, 0.05f).flatten(1.8f, 1.0f, HX, 0.3f);
                Shape back = Shapes.tube(-2.2f, 30.2f, 0.1f, 0.58f, -2.52f, HY, 0.24f, 0.62f, -2.58f, 27.3f, 0.32f, 0.55f, -2.45f, 25.6f, 0.5f, 0.42f, -2.36f, 23.8f, 0.52f, 0.25f, -2.3f, 23.0f, 0.55f, 0.04f).flatten(1.8f, 1.0f, HX, 0.15f);
                Motion sway = new Motion().idle(1.5f, HX, 0.6f, 0.35f).walk(3.0f, HX, 1.0f, 2.0f).drag(8.0f).hang(0.6f).spring(60.0f, 8.0f).bend(1.4f);
                Part lockR = rig.extra("sidelock_r", Bone.HEAD).pivot(tx(-2.3f), ty(30.0f), tz(0.6f)).shape(up(Shapes.blend(0.25f, front, back).paint(paint))).motion(sway).resolution(0.7f).detail(0.6f).flatLight(0.5f).occlusion(0.35f).outline(HAIR_LINE, 0.09f);
                rig.extra("sidelock_l", Bone.HEAD).mirrorOf(lockR);
                return;
            }
            cap.add(capLock(phi, 20.0f, 118.0f, 0.85f, 0.5f));
            f2 = phi + 25.0f;
        }
    }

    private static void twinTails(Rig rig) {
        float ax = tx(-2.62f);
        float ay = ty(30.2f);
        float az = tz(-1.35f);
        float[][] paths = {new float[]{HX, HX, 1.0f, -0.9f, -0.5f, 1.35f, -1.5f, -2.6f, 1.5f, -1.75f, -6.5f, 1.45f, -1.75f, -11.5f, 1.3f, -1.6f, -16.5f, 1.05f, -1.45f, -20.5f, 0.7f, -1.35f, -23.4f, 0.06f}, new float[]{HX, 0.1f, 0.8f, -1.0f, -0.4f, 1.0f, -1.9f, -2.8f, 1.1f, -2.2f, -7.0f, 1.0f, -2.25f, -12.0f, 0.9f, -2.05f, -17.0f, 0.7f, -1.85f, -20.8f, 0.45f, -1.95f, -22.8f, 0.04f}, new float[]{HX, 0.1f, 0.8f, -0.7f, -0.6f, 1.0f, -1.0f, -2.9f, 1.05f, -1.15f, -7.2f, 1.0f, -1.1f, -12.2f, 0.9f, -0.95f, -16.8f, 0.7f, -0.9f, -20.2f, 0.45f, -0.75f, -22.2f, 0.04f}};
        float[] depth = {HX, -0.25f, 0.2f};
        Shape[] strands = new Shape[paths.length];
        for (int i = 0; i < paths.length; i++) {
            float[] p = paths[i];
            float[] xyzr = new float[(p.length / 3) * 4];
            for (int k = 0; k < p.length / 3; k++) {
                float t = k / ((p.length / 3.0f) - 1.0f);
                xyzr[k * 4] = ax + p[k * 3];
                xyzr[(k * 4) + 1] = ay + (p[(k * 3) + 1] * HS);
                xyzr[(k * 4) + 2] = (az - (1.15f * ((float) Math.sin((((double) Math.min(1.0f, t * 3.0f)) * 3.141592653589793d) * 0.5d)))) + (depth[i] * t);
                xyzr[(k * 4) + 3] = p[(k * 3) + 2];
            }
            strands[i] = Shapes.tube(xyzr).flatten(1.7f, 0.25f, HX, 1.0f);
        }
        float endY = ay - 26.208f;
        Paint paint = Paint.field((x, y, z) -> {
            float t2 = clamp01((ay - y) / (ay - endY));
            int c = Colors.mix(HAIR_ROOT, HAIR, smooth(HX, 0.18f, t2));
            int c2 = Colors.mix(c, HAIR_TIP, 0.6f * smooth(0.55f, 1.0f, t2));
            float inner = smooth(DROP, -0.6f, (x - ax) + 1.6f) * 0.35f;
            return Colors.mix(c2, HAIR_SHADE, inner);
        });
        Shape tie = Shapes.union(Shapes.box(ax - 0.3f, ay, az, 0.34f, 1.0f, 0.75f, 0.14f).color(BLACK), Shapes.box(ax - 0.62f, ay, az + 0.38f, 0.06f, 0.86f, 0.09f, 0.03f).color(MAGENTA), Shapes.box(ax - 0.62f, ay, az - 0.38f, 0.06f, 0.86f, 0.09f, 0.03f).color(MAGENTA)).rotate(HX, HX, -12.0f, ax, ay, az);
        Motion motion = Motion.hair().hang(0.25f).follow(Bone.BODY, 0.8f).drag(12.0f).turn(6.0f, HX).spring(40.0f, 6.0f).bend(1.6f).limit(35.0f);
        Part right = rig.extra("tail_r", Bone.HEAD).pivot(ax, ay, az).shape(Shapes.union(Shapes.blend(0.35f, strands).paint(paint), tie)).motion(motion).resolution(1.0f).detail(0.9f).flatLight(0.5f).occlusion(0.3f).outline(HAIR_LINE, 0.1f);
        rig.extra("tail_l", Bone.HEAD).mirrorOf(right);
    }

    private static void headset(Rig rig) {
        Shape cupR = Shapes.union(Shapes.cylinder(-2.45f, 28.0f, 0.05f, -2.85f, 28.0f, 0.05f, 0.62f, 0.12f).color(BLACK), Shapes.cylinder(-2.8f, 28.0f, 0.05f, -2.95f, 28.0f, 0.05f, 0.38f, 0.06f).color(TEAL));
        Shape mic = Shapes.union(Shapes.tube(-2.7f, 27.7f, 0.45f, 0.08f, -2.45f, 26.8f, 1.3f, 0.08f, -1.7f, 26.25f, 2.05f, 0.08f, -0.95f, 26.15f, 2.25f, 0.08f).color(BLACK), Shapes.ellipsoid(-0.85f, 26.15f, 2.28f, 0.22f, 0.16f, 0.16f).color(BLACK_SHADE));
        rig.extra("headset", Bone.HEAD).shape(up(Shapes.union(cupR, cupR.mirrored(), mic))).resolution(DROP).detail(0.6f).flatLight(DROP).outline(BLACK_LINE, 0.08f);
    }

    private static Shape capLock(float phi, float from, float to, float r0, float r1) {
        float[] xyz = new float[15];
        for (int i = 0; i < 5; i++) {
            float[] p = onHead(from + (((to - from) * i) / 4.0f), phi, 0.02f);
            System.arraycopy(p, 0, xyz, i * 3, 3);
        }
        return Shapes.strand(r0, r1, xyz).flattenFrom(2.8f, HX, HY, HZ);
    }

    private static float[] onHead(float alpha, float phi, float lift) {
        double a = Math.toRadians(alpha);
        double p = Math.toRadians(phi);
        float ca = (float) Math.cos(a);
        float sa = (float) Math.sin(a);
        float cp = (float) Math.cos(p);
        float sp = (float) Math.sin(p);
        float dx = sp * sa;
        float dy = (0.97f * ca) + (0.24f * cp * sa);
        float dz = ((-0.24f) * ca) + (0.97f * cp * sa);
        float t = (1.0f / ((float) Math.sqrt((((dx * dx) / 7.3984003f) + ((dy * dy) / 7.3984003f)) + ((dz * dz) / 8.065599f)))) + lift;
        return new float[]{HX + (dx * t), HY + (dy * t), HZ + (dz * t)};
    }

    private static Paint hairPaint() {
        return Paint.field((x, y, z) -> {
            float dy = y - 31.2f;
            float dz = z + 0.7f;
            float d = (float) Math.sqrt((x * x) + (dy * dy) + (dz * dz));
            int c = Colors.mix(HAIR_ROOT, HAIR, smooth(0.8f, 2.9f, d));
            return Colors.mix(c, HAIR_TIP, smooth(5.5f, 17.5f, d) * 0.9f);
        });
    }

    private static void body(Rig rig) {
        Shape shoulders = Shapes.union(Shapes.capsule(HX, 23.95f, -0.15f, -2.45f, 23.3f, -0.15f, 0.9f), Shapes.capsule(HX, 23.95f, -0.15f, 2.45f, 23.3f, -0.15f, 0.9f));
        Shape chest = Shapes.ellipsoid(HX, 21.7f, 0.05f, 2.45f, 2.1f, 1.65f);
        Shape bust = Shapes.ellipsoid(0.84f, 21.6f, 0.6f, 0.9f, 0.8f, 0.95f).mirrorX();
        Shape waist = Shapes.ellipsoid(HX, 19.6f, HX, 2.0f, 1.7f, 1.45f);
        Shape hips = Shapes.ellipsoid(HX, 17.8f, HX, 2.15f, 1.6f, 1.5f);
        Shape torso = Shapes.blend(1.0f, shoulders, Shapes.blend(0.8f, chest, bust), waist, hips).intersect(Shapes.above(17.2f));
        Paint shirt = Paint.field((x, y, z) -> {
            float under = smooth(0.5f, 1.3f, z) * smooth(19.9f, 20.5f, y) * (1.0f - smooth(20.6f, 21.1f, y));
            float side = smooth(1.6f, 2.3f, Math.abs(x)) * 0.35f;
            return Colors.mix(SHIRT, SHIRT_SHADE, Math.max(under * 0.55f, side));
        });
        rig.part(Bone.BODY).shape(torso.paint(shirt)).outline(SHIRT_LINE, 0.1f).detail(0.85f).occlusion(0.2f);
        Shape2D flap = Shape2D.polygon(0.05f, 24.5f, 1.15f, 24.55f, 1.35f, 23.7f, 0.6f, 23.15f, 0.08f, 23.55f);
        Shape collarR = Shapes.extrude(flap, 0.14f, 0.07f).rotate(-25.0f, -20.0f, HX, 0.5f, 23.9f, HX).move(HX, HX, frontZ(torso, 0.6f, 23.8f) + 0.02f);
        Shape collar = Shapes.union(collarR, collarR.mirrored()).color(SHIRT);
        rig.extra("collar", Bone.BODY).shape(collar).resolution(0.35f).detail(0.8f).flatLight(0.3f).outline(TEAL, 0.1f);
        float[] ys = {23.35f, 22.4f, 21.3f, 20.2f, 19.3f};
        float[] rs = {0.26f, 0.32f, 0.38f, 0.42f, 0.44f};
        float[] p = new float[ys.length * 4];
        for (int i = 0; i < ys.length; i++) {
            p[(i * 4) + 1] = ys[i];
            p[(i * 4) + 2] = frontZ(torso, HX, ys[i]) + 0.1f;
            p[(i * 4) + 3] = rs[i];
        }
        Shape2D tip = Shape2D.polygon(-1.0f, 24.0f, 1.0f, 24.0f, 1.0f, 19.7f, HX, 19.1f, -1.0f, 19.7f);
        Shape blade = Shapes.intersect(Shapes.tube(p).flatten(3.5f, HX, HX, 1.0f), Shapes.extrude(tip, 4.0f, HX));
        Shape knot = Shapes.ellipsoid(HX, 23.45f, frontZ(torso, HX, 23.45f) + 0.12f, 0.36f, 0.3f, 0.22f);
        rig.extra("tie", Bone.BODY).pivot(HX, 23.45f, 1.6f).shape(Shapes.union(blade, knot).paint(Paint.vertical(TEAL_DARK, 19.3f, TEAL, 22.5f))).motion(Motion.none().walk(3.0f, HX, 2.0f, 2.0f).drag(4.0f).spring(80.0f, 8.0f)).resolution(DROP).detail(0.5f).flatLight(DROP).outline(-15835560, 0.08f);
    }

    private static void skirt(Rig rig) {
        Shape skirt = Shapes.field(Miku::skirtDistance, -3.4f, 13.1f, -3.0f, 3.4f, 18.1f, 3.0f).paint(Paint.field(Miku::skirtColor));
        rig.extra("skirt", Bone.BODY).pivot(HX, SKIRT_TOP, HX).shape(skirt).resolution(0.9f).detail(0.8f).occlusion(0.35f).outline(BLACK_LINE, 0.12f).decal(stripe(13.650001f, 0.26f)).motion(Motion.none().walk(HX, 8.0f, HX, 1.0f).walkPhase(-90.0f).drag(2.0f).spring(80.0f, 10.0f));
    }

    private static float flare(float y) {
        float t = clamp01((17.6f - y) / 4.299999f);
        return (t * (2.0f - t) * 0.8f) + (t * 0.2f);
    }

    private static float pleat(float x, float z, float t) {
        float rx = 2.25f + (0.95f * t);
        float rz = 1.62f + (0.95f * t);
        double f = (Math.atan2(x / rx, z / rz) * 18.0d) / 6.283185307179586d;
        return (float) Math.abs((2.0d * (f - Math.floor(f))) - 1.0d);
    }

    private static float skirtDistance(float x, float y, float z) {
        float t = flare(y);
        float rx = 2.25f + (0.95f * t);
        float rz = 1.62f + (0.95f * t);
        float a = x / rx;
        float b = z / rz;
        float k = (float) Math.sqrt((a * a) + (b * b));
        float side = (((k - 1.0f) * Math.min(rx, rz)) * 0.72f) - ((0.2f * t) * (pleat(x, z, t) - 0.5f));
        return Math.max(side, Math.max(y - SKIRT_TOP, SKIRT_HEM - y));
    }

    private static int skirtColor(float x, float y, float z) {
        float t = flare(y);
        return Colors.mix(BLACK_SHADE, BLACK, 0.35f + (0.65f * ((1.0f - t) + (t * pleat(x, z, t)))));
    }

    private static Decal stripe(float y, float width) {
        Canvas c = new Canvas(512, 24);
        c.fill(Shape2D.box(256.0f, 12.0f, 256.0f, 8.0f, HX), TEAL);
        return Decal.wrap(c, HX, y, HX, HX, 1.0f, HX, HX, HX, 1.0f, 360.0f, width).radius(1.8f, 3.4f);
    }

    private static void arms(Rig rig) {
        Shape arm = Shapes.blend(0.35f, Shapes.sphere((-3.5f) + 0.1f, 23.1f, -0.12f, 0.66f), Shapes.cone(-3.5f, 23.1f, -0.12f, 0.62f, -3.95f, 19.5f, HZ, 0.52f), Shapes.cone(-3.95f, 19.5f, HZ, 0.52f, -4.2f, 16.0f, DROP, 0.44f)).paint(Paint.field((x, y, z) -> {
            float t = smooth(23.4f, 21.8f, y);
            return Colors.mix(SKIN_SHADE, SKIN, DROP + (0.6f * t));
        }));
        Part armR = rig.part(Bone.RIGHT_ARM).pivot(-3.5f, 23.25f, -0.1f).grip(-4.32f, 15.05f, 0.72f).shape(arm).resolution(0.7f).detail(0.8f).flatLight(0.8f).outline(SKIN_LINE, 0.1f).swing(0.55f).joint(-3.95f, 19.5f, HZ, 0.55f);
        rig.part(Bone.LEFT_ARM).mirrorOf(armR).decal(tattoo(-(-3.5f), 23.1f, -0.12f, -(-3.95f), 19.5f, HZ));
        float fl = (float) Math.sqrt((((-4.2f) - (-3.95f)) * ((-4.2f) - (-3.95f))) + ((16.0f - 19.5f) * (16.0f - 19.5f)) + ((DROP - HZ) * (DROP - HZ)));
        float dx = ((-4.2f) - (-3.95f)) / fl;
        float dy = (16.0f - 19.5f) / fl;
        float dz = (DROP - HZ) / fl;
        float ox = (-3.95f) + (0.35f * ((-4.2f) - (-3.95f)));
        float oy = 19.5f + (0.35f * (16.0f - 19.5f)) + 0.9f;
        float oz = HZ + (0.35f * (DROP - HZ));
        float mx = (-4.2f) + (1.1f * dx);
        float my = 16.0f + (1.1f * dy);
        float mz = DROP + (1.1f * dz);
        Shape outer = Shapes.cone(ox, oy + 0.3f, oz, 0.62f, mx, my, mz, 1.15f);
        Shape cavity = Shapes.sphere(mx + (0.25f * dx), my + (0.25f * dy), mz + (0.25f * dz), 0.95f);
        float ay0 = oy + 0.3f;
        float al = (float) Math.sqrt(((mx - ox) * (mx - ox)) + ((my - ay0) * (my - ay0)) + ((mz - oz) * (mz - oz)));
        Shape sleeve = Shapes.subtract(0.1f, outer, cavity).paint(Paint.field((x2, y2, z2) -> {
            float s = ((((x2 - ox) * (mx - ox)) + ((y2 - ay0) * (my - ay0))) + ((z2 - oz) * (mz - oz))) / (al * al);
            return Colors.mix(BLACK, BLACK_SHADE, smooth(0.2f, 1.0f, s) * 0.5f);
        }));
        Canvas band = new Canvas(256, 16);
        band.fill(Shape2D.box(128.0f, 8.0f, 128.0f, 6.0f, HX), TEAL);
        float bx = mx - ox;
        float by = my - ay0;
        float bz = mz - oz;
        Decal cuff = Decal.wrap(band, ox + (bx * 0.93f), ay0 + (by * 0.93f), oz + (bz * 0.93f), bx, by, bz, HX, HX, 1.0f, 360.0f, al * 0.1f).radius(0.7f, 1.4f);
        Decal hem = Decal.wrap(band, ox + (bx * 0.05f), ay0 + (by * 0.05f), oz + (bz * 0.05f), bx, by, bz, HX, HX, 1.0f, 360.0f, al * 0.08f).radius(DROP, 1.0f);
        Part sleeveR = rig.extra("sleeve_r", Bone.RIGHT_ARM).shape(sleeve).resolution(1.1f).detail(0.8f).flatLight(0.3f).occlusion(DROP).outline(BLACK_LINE, 0.1f).decal(cuff).decal(hem);
        rig.extra("sleeve_l", Bone.LEFT_ARM).mirrorOf(sleeveR);
        hands(rig, -4.2f, 16.0f, DROP);
    }

    private static Decal tattoo(float sx, float sy, float sz, float ex, float ey, float ez) {
        Canvas c = new Canvas(64, 40);
        c.fill(Shape2D.ellipse(44.0f, 20.0f, 9.0f, 14.0f).outline(4.5f), TATTOO);
        c.fill(Shape2D.line(20.0f, 7.0f, 20.0f, 34.0f, 4.5f), TATTOO);
        c.fill(Shape2D.line(20.0f, 7.0f, 14.0f, 13.0f, 4.0f), TATTOO);
        float x = sx + ((ex - sx) * 0.22f);
        float y = sy + ((ey - sy) * 0.22f);
        float z = sz + ((ez - sz) * 0.22f);
        return Decal.left(c, x + 0.55f, y, z + 0.05f, 1.0f, 0.62f).depth(0.6f, 0.6f).maxAngle(70.0f).flatLight(0.8f);
    }

    private static void hands(Rig rig, float wx, float wy, float wz) {
        List<Shape> hand = new ArrayList<>();
        hand.add(Shapes.ellipsoid(4.3f, 15.25f, 0.6f, 0.33f, 0.6f, 0.52f));
        hand.add(Shapes.capsule(4.3f - 0.02f, 15.25f + 0.45f, 0.6f - 0.12f, -wx, wy + 0.1f, wz, 0.32f));
        hand.add(Shapes.strand(0.15f, 0.13f, 4.3f + 0.02f, 15.25f - DROP, 0.6f + 0.36f, 4.3f - 0.02f, 15.25f - 0.86f, 0.6f + 0.44f, 4.3f - 0.2f, 15.25f - 1.1f, 0.6f + DROP));
        hand.add(Shapes.strand(0.15f, 0.13f, 4.3f + 0.03f, 15.25f - 0.44f, 0.6f + 0.12f, 4.3f, 15.25f - 0.98f, 0.6f + 0.14f, 4.3f - 0.22f, 15.25f - 1.26f, 0.6f + 0.11f));
        hand.add(Shapes.strand(0.14f, 0.12f, 4.3f + 0.03f, 15.25f - 0.42f, 0.6f - 0.12f, 4.3f, 15.25f - 0.9f, 0.6f - 0.13f, 4.3f - 0.2f, 15.25f - 1.16f, 0.6f - 0.14f));
        hand.add(Shapes.strand(0.13f, 0.11f, 4.3f + 0.02f, 15.25f - 0.36f, 0.6f - 0.34f, 4.3f, 15.25f - 0.74f, 0.6f - 0.36f, 4.3f - 0.16f, 15.25f - 0.95f, 0.6f - 0.35f));
        hand.add(Shapes.strand(0.17f, 0.14f, 4.3f - 0.18f, 15.25f + 0.1f, 0.6f + 0.42f, 4.3f - 0.3f, 15.25f - 0.3f, 0.6f + 0.58f, 4.3f - 0.32f, 15.25f - 0.54f, 0.6f + 0.5f));
        Part left = rig.extra("hand_l", Bone.LEFT_ARM).shape(Shapes.blend(0.12f, (Shape[]) hand.toArray(new Shape[0])).color(SKIN)).resolution(0.45f).detail(0.35f).flatLight(1.0f).occlusion(0.25f).outline(SKIN_LINE, 0.1f);
        rig.extra("hand_r", Bone.RIGHT_ARM).mirrorOf(left);
    }

    private static void legs(Rig rig) {
        Shape hip = Shapes.sphere(-1.1f, 14.6f, -0.05f, 1.02f);
        Shape thigh = Shapes.cone((-1.1f) - 0.05f, 14.3f, HX, 1.02f, -1.1f, 8.4f, 0.08f, 0.76f);
        Shape knee = Shapes.sphere(-1.1f, 7.9f, 0.1f, 0.72f);
        Shape calf = Shapes.tube(-1.1f, 7.6f, 0.08f, 0.72f, (-1.1f) - 0.02f, 5.8f, -0.12f, 0.8f, (-1.1f) - 0.04f, 3.6f, -0.02f, 0.56f, (-1.1f) - 0.05f, 2.0f, HX, 0.48f);
        Shape leg = Shapes.blend(0.45f, hip, thigh, knee, calf);
        Shape skin = leg.intersect(Shapes.above(10.6f)).paint(Paint.vertical(SKIN_SHADE, 10.8f, SKIN, 12.5f));
        Shape cuff = Shapes.cylinder(-1.1f, 11.05f, 0.03f, -1.1f, 10.35f, 0.05f, 0.9f, 0.12f).color(TEAL);
        Shape boot = Shapes.blend(0.3f, leg.intersect(Shapes.below(10.4f)).inflate(0.09f), shoe(-1.1f)).paint(Paint.field((x, y, z) -> {
            return Colors.mix(BLACK, BLACK_SHADE, smooth(-0.2f, -0.8f, z) * 0.6f);
        }));
        Part legR = rig.part(Bone.RIGHT_LEG).pivot(-1.1f, 14.2f, HX).shape(skin).detail(0.6f).resolution(1.0f).flatLight(0.8f).outline(SKIN_LINE, 0.1f).occlusion(0.3f).swing(0.55f, 30.0f).joint(-1.1f, 7.9f, 0.1f, 0.8f);
        rig.part(Bone.LEFT_LEG).mirrorOf(legR);
        Part bootR = rig.extra("boot_r", Bone.RIGHT_LEG).shape(boot).detail(0.8f).resolution(1.0f).outline(BLACK_LINE, 0.11f).occlusion(0.3f);
        rig.extra("boot_l", Bone.LEFT_LEG).mirrorOf(bootR);
        Part trimR = rig.extra("boot_trim_r", Bone.RIGHT_LEG).shape(Shapes.union(cuff, sole(-1.1f))).detail(DROP).resolution(0.6f).flatLight(0.3f).outline(-15835560, 0.09f);
        rig.extra("boot_trim_l", Bone.LEFT_LEG).mirrorOf(trimR);
    }

    private static Shape shoe(float lx) {
        return Shapes.blend(DROP, Shapes.ellipsoid(lx, 1.1f, 0.95f, 0.62f, 0.6f, 1.15f), Shapes.ellipsoid(lx, 1.4f, -0.35f, 0.62f, 0.9f, 0.72f), Shapes.cone(lx, 2.3f, -0.05f, 0.5f, lx, 1.2f, HX, 0.6f));
    }

    private static Shape sole(float lx) {
        Shape plate = Shapes.box(lx, 0.62f, 0.85f, 0.64f, 0.14f, 1.3f, 0.12f);
        Shape heel = Shapes.box(lx, 0.3f, -0.55f, 0.46f, 0.3f, DROP, 0.1f);
        return Shapes.blend(0.12f, plate, heel).intersect(Shapes.above(HX)).paint(Paint.vertical(TEAL_DARK, HX, TEAL, 0.7f));
    }

    private static float px(float x) {
        return 128.0f + (x * PPU);
    }

    private static float py(float y) {
        return (FACE_TOP - y) * PPU;
    }

    private static Decal face(float[][] locks) {
        Canvas eye = new Canvas(88, 136);
        Shape2D white = Shape2D.ellipse(40.0f, 70.0f, 27.5f, 33.0f);
        eye.fill(white, Paint2D.linear(40.0f, 70.0f - 33.0f, 40.0f, 70.0f + 10.0f, -1904912, -1));
        eye.fill(Shape2D.ellipse(40.0f, 70.0f - 34.0f, 34.0f, 22.0f).intersect(white), -1715153190, 2.0f);
        Shape2D irisShape = Shape2D.ellipse(40.0f, 70.0f + 3.5f, 20.5f, 27.5f);
        Shape2D iris = irisShape.intersect(white);
        eye.fill(iris, Paint2D.linear(40.0f, 70.0f - 24.0f, 40.0f, 70.0f + 30.0f, -16106432, -14504024, -6491416));
        eye.fill(irisShape.outline(2.6f).intersect(white), -1341506496);
        eye.fill(Shape2D.ellipse(40.0f, 70.0f + 1.5f, 9.0f, 12.5f), -16439768);
        eye.fill(Shape2D.ellipse(40.0f, 70.0f + 17.0f, 12.0f, 6.5f).intersect(iris), -2133655560, 3.0f);
        eye.fill(Shape2D.curve(40.0f + 26.5f, 70.0f - 13.0f, 40.0f + 2.0f, 70.0f - 46.0f, 40.0f - 30.0f, 70.0f - 6.0f, 4.2f, 8.4f), -14800336);
        eye.fill(Shape2D.taper(40.0f - 25.0f, 70.0f - 9.0f, 6.0f, 40.0f - 35.0f, 70.0f - 6.0f, 1.4f), -14800336);
        eye.fill(Shape2D.curve(40.0f - 5.0f, 70.0f + 32.0f, 40.0f - 20.0f, 70.0f + 31.0f, 40.0f - 26.5f, 70.0f + 16.0f, 1.6f, 2.8f), -868591010);
        eye.fill(Shape2D.curve(40.0f + 15.0f, 70.0f - 38.0f, 40.0f, 70.0f - 48.0f, 40.0f - 23.0f, 70.0f - 33.0f, 1.5f, 1.5f), 2005965984);
        eye.fill(Shape2D.curve(40.0f + 20.0f, 70.0f - 48.0f, 40.0f + 2.0f, 70.0f - 57.0f, 40.0f - 22.0f, 70.0f - 51.0f, 4.0f, 2.6f), -13989243);
        eye.fill(Shape2D.ellipse(40.0f - 4.0f, 70.0f + 46.0f, 21.0f, 9.0f), 1442810524, 10.0f);
        Canvas c = new Canvas(256, 220);
        int oy = Math.round(py(28.0f)) - 70;
        List<Shape2D> shade = new ArrayList<>();
        for (float[] p : locks) {
            float[] xy = blade(p);
            for (int i = 0; i < xy.length; i += 2) {
                xy[i] = px(xy[i]);
                xy[i + 1] = py(xy[i + 1]) + 14.0f;
            }
            shade.add(Shape2D.polygon(xy));
        }
        shade.add(Shape2D.box(128.0f, py(29.75f), 130.0f, 14.222222f, HX));
        c.fill(Shape2D.union((Shape2D[]) shade.toArray(new Shape2D[0])), -1931434602, 3.0f);
        c.draw(eye, 28, oy);
        c.draw(eye.mirrored(), 140, oy);
        for (int side = -1; side <= 1; side += 2) {
            float cx = 128.0f + (side * 60.0f);
            c.fill(Shape2D.ellipse(cx - 7.5f, oy + 60.0f, 7.0f, 8.8f), -1);
            c.fill(Shape2D.circle(cx + 8.0f, oy + 83.0f, 3.0f), -570425345);
            c.fill(Shape2D.circle(cx - 12.0f, oy + 76.0f, 1.6f), -1426063361);
        }
        c.fill(Shape2D.line(130.0f, oy + 119.0f, 127.0f, oy + 124.5f, 2.4f), -1713795968);
        c.fill(Shape2D.path(3.0f, 3.0f, 118.0f, oy + 156.0f, 123.0f, oy + 161.0f, 128.0f, oy + 157.5f, 133.0f, oy + 161.0f, 138.0f, oy + 156.0f), -6668728);
        return Decal.front(c, HX, ty(28.016407f), tz(2.1f), 5.04f, 4.33125f).depth(2.0f, 1.8f).maxAngle(72.0f).flatLight(1.0f).occlusion(0.3f);
    }

    private static float smooth(float e0, float e1, float x) {
        float t = clamp01((x - e0) / (e1 - e0));
        return t * t * (3.0f - (2.0f * t));
    }

    private static float clamp01(float v) {
        return v < HX ? HX : Math.min(v, 1.0f);
    }
}
