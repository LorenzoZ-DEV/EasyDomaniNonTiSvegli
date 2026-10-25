package net.anubis.client.module.impl.render.character.designs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import net.anubis.client.module.impl.render.character.kit.Strand;
import net.anubis.client.module.impl.render.character.kit.Vec3;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/designs/Mika.class */
@Environment(EnvType.CLIENT)
public final class Mika extends CharacterDesign {
    private static final int SKIN = -9532;
    private static final int SKIN_SHADE = -1331558;
    private static final int SKIN_LINE = -5740966;
    private static final int HAIR = -681403;
    private static final int HAIR_DARK = -3647457;
    private static final int HAIR_TIP = -15750;
    private static final int HAIR_LINE = -6142180;
    private static final int EAR_TIP = -4697060;
    private static final int EAR_PINK = -22088;
    private static final int TUFT = -5162;
    private static final int HOODIE = -4200487;
    private static final int HOODIE_SHADE = -7092297;
    private static final int RIB = -5710645;
    private static final int CUFF_IN = -9461870;
    private static final int HOODIE_LINE = -10774650;
    private static final int LINING = -744532;
    private static final int SKIRT = -12892555;
    private static final int SKIRT_DARK = -14275250;
    private static final int SKIRT_LINE = -14999232;
    private static final int TIGHTS = -12965330;
    private static final int TIGHTS_SHEEN = -9808288;
    private static final int TIGHTS_LINE = -15068908;
    private static final int SHOE = -542005;
    private static final int SOLE = -264207;
    private static final int MINT = -5839921;
    private static final int FISH = -8398652;
    private static final int FISH_LINE = -12811148;
    private static final int SHOE_LINE = -6659470;
    private static final int GOLD = -867761;
    private static final int TAIL_TIP = -3618;
    private static final int LASH = -13887470;
    private static final int BROW = -4691398;
    private static final float HY = 27.95f;
    private static final float HZ = -0.15f;
    private static final float RX = 2.3f;
    private static final float RY = 2.3f;
    private static final float RZ = 2.35f;
    private static final float HS = 1.12f;
    private static final float NY = 24.9f;
    private static final float PPU = 55.652176f;
    private static final float TOP = 29.0f;
    private static final int PLEATS = 16;
    private static final float HX = 0.0f;
    private static final float DROP = 0.4f;
    private static final float NZ = -0.3f;
    private static final float[][] FRINGE = {new float[]{1.0f, -1.0f, 30.15f, -1.5f, 29.6f, -2.0f, 28.3f, -1.8f, 27.0f, 0.3f}, new float[]{0.95f, -0.42f, 30.25f, -0.75f, 29.75f, -1.05f, 28.6f, -0.85f, 27.9f, -0.35f}, new float[]{1.05f, -0.28f, 30.3f, HX, 29.8f, 0.5f, 28.4f, 0.42f, 27.2f, DROP}, new float[]{0.95f, 0.55f, 30.25f, 0.9f, 29.75f, 1.25f, 28.6f, 1.02f, 27.65f, NZ}, new float[]{1.0f, 1.1f, 30.15f, 1.6f, 29.6f, 2.05f, 28.3f, 1.85f, 26.75f, 0.35f}, new float[]{0.65f, -0.75f, 30.2f, -1.2f, 29.6f, -1.5f, 28.3f, -1.3f, 27.45f, 0.3f}, new float[]{0.5f, 0.28f, 30.25f, 0.6f, 29.7f, 0.88f, 28.5f, 0.75f, 27.5f, NZ}, new float[]{0.5f, -0.45f, 30.25f, -0.4f, 29.7f, NZ, 28.3f, -0.18f, 27.6f, 0.3f}};
    private static final float[][] CROWN = {new float[]{-0.413f, 0.766f, 0.492f}, new float[]{-0.992f, 0.087f, -0.087f}, new float[]{-0.539f, -0.342f, -0.77f}, new float[]{0.464f, -0.375f, -0.803f}, new float[]{0.975f, 0.139f, -0.172f}, new float[]{0.353f, 0.788f, 0.504f}};

    public Mika() {
        super("mika", "Mika");
    }

    @Override // net.anubis.client.module.impl.render.character.kit.CharacterDesign
    protected void define(Rig rig) {
        rig.scale(1.0f).outline(-12967400, 0.12f).occlusion(0.3f);
        body(rig);
        head(rig);
        hair(rig);
        ears(rig);
        arms(rig);
        legs(rig);
        tail(rig);
    }

    private static Shape toHead(Shape s) {
        return s.move(HX, -0.4f, HX).scale(HS, HX, NY, NZ);
    }

    private static float wx(float x) {
        return x * HS;
    }

    private static float wy(float y) {
        return NY + (((y - DROP) - NY) * HS);
    }

    private static float wz(float z) {
        return NZ + ((z - NZ) * HS);
    }

    private static float u(float x) {
        return 128.0f + (x * PPU);
    }

    private static float v(float y) {
        return (TOP - y) * PPU;
    }

    private static float capZ(float x, float y, float off) {
        float ax = x / (2.72f + off);
        float ay = (y - 28.25f) / (2.8f + off);
        return (-0.28f) + ((2.85f + off) * ((float) Math.sqrt(Math.max(0.02f, (1.0f - (ax * ax)) - (ay * ay)))));
    }

    private static float[] onCap(float dx, float dy, float dz, float off) {
        float l = (float) Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
        float dx2 = dx / l;
        float dy2 = dy / l;
        float dz2 = dz / l;
        float ax = dx2 / (2.72f + off);
        float ay = dy2 / (2.8f + off);
        float az = dz2 / (2.85f + off);
        float k = 1.0f / ((float) Math.sqrt(((ax * ax) + (ay * ay)) + (az * az)));
        return new float[]{dx2 * k, 28.25f + (dy2 * k), (-0.28f) + (dz2 * k)};
    }

    private static Shape crownClump(float[] t, float w) {
        float[] p = new float[16];
        float[] s = {HX, 0.38f, 0.72f, 1.0f};
        float[] r = {0.2f, 0.85f * w, 0.8f * w, 0.3f};
        float[] off = {-0.12f, -0.07f, -0.07f, -0.14f};
        for (int i = 0; i < 4; i++) {
            float[] q = onCap(0.15f + ((t[0] - 0.15f) * s[i]), 0.93f + ((t[1] - 0.93f) * s[i]), (-0.35f) + ((t[2] - (-0.35f)) * s[i]), off[i]);
            p[i * 4] = q[0];
            p[(i * 4) + 1] = q[1];
            p[(i * 4) + 2] = q[2];
            p[(i * 4) + 3] = r[i];
        }
        return Shapes.tube(p).flattenFrom(3.2f, HX, 28.25f, -0.28f);
    }

    private static float surf(float x, float y, float off) {
        float ax = x / (2.3f + off);
        float ay = (y - HY) / (2.3f + off);
        return HZ + ((RZ + off) * ((float) Math.sqrt(Math.max(0.02f, (1.0f - (ax * ax)) - (ay * ay)))));
    }

    private static void head(Rig rig) {
        Shape cranium = Shapes.ellipsoid(HX, HY, HZ, 2.3f, 2.3f, RZ);
        Shape cheeks = Shapes.ellipsoid(HX, 26.72f, 0.35f, 1.93f, 1.3f, 1.7f);
        Shape jaw = Shapes.cone(HX, 26.2f, 0.45f, 1.2f, HX, 25.25f, 0.98f, 0.3f);
        Shape hidden = Shapes.union(Shapes.box(HX, TOP, -1.8f, 3.0f, 2.3f, 1.5f), Shapes.box(HX, 30.3f, 0.5f, 3.0f, 1.3f, 3.0f));
        Shape skull = Shapes.subtract(Shapes.blend(0.7f, cranium, cheeks, jaw), hidden).color(SKIN);
        rig.part(Bone.HEAD).pivot(HX, NY, NZ).shape(toHead(skull)).detail(1.5f).flatLight(1.0f).occlusion(0.2f).outline(SKIN_LINE, 0.1f).decal(face());
        Paint neck = Paint.vertical(SKIN, 23.7f, SKIN_SHADE, 24.3f).flatLight(1.0f).occlusion(0.25f);
        rig.extra("neck", Bone.BODY).shape(Shapes.capsule(HX, 23.1f, -0.28f, HX, 25.2f, -0.2f, 0.62f).paint(neck)).outline(SKIN_LINE, 0.08f);
    }

    private static void hair(Rig rig) {
        Paint paint = Paint.field(Mika::hairColor);
        Shape cap = Shapes.ellipsoid(HX, 28.25f, -0.28f, 2.72f, 2.8f, 2.85f).intersect(Shapes.plane(HX, 27.4f, 1.2f, HX, -0.6f, 1.0f)).intersect(Shapes.above(26.4f));
        Shape[] clumps = new Shape[CROWN.length];
        for (int i = 0; i < CROWN.length; i++) {
            clumps[i] = crownClump(CROWN[i], i % 2 == 0 ? 1.0f : 0.9f);
        }
        Shape crown = Shapes.blend(0.3f, cap, Shapes.union(clumps));
        Part top = rig.extra("hair", Bone.HEAD).shape(toHead(Shapes.union(crown, backLock(180.0f, 24.95f, 1.05f, 0.25f)).paint(paint))).detail(0.9f).resolution(0.95f).flatLight(0.5f).occlusion(0.3f).outline(HAIR_LINE, 0.09f);
        float[][] back = {new float[]{-54.0f, 25.95f, 0.9f, 0.3f}, new float[]{-75.0f, 25.45f, 1.0f, NZ}, new float[]{-97.0f, 25.8f, 1.05f, 0.25f}, new float[]{-119.0f, 25.3f, 1.05f, -0.2f}, new float[]{-140.0f, 25.5f, 1.05f, 0.3f}, new float[]{-161.0f, 25.1f, 1.0f, NZ}};
        List<Shape> half = new ArrayList<>();
        for (float[] b : back) {
            half.add(backLock(b[0], b[1], b[2], b[3]));
        }
        half.add(topLock(-84.0f, 26.25f));
        half.add(topLock(-128.0f, 26.5f));
        Part right = rig.extra("hair_r", Bone.HEAD).pivot(HX, wy(29.8f), wz(-0.4f)).shape(toHead(Shapes.union((Shape[]) half.toArray(new Shape[0])).paint(paint))).detail(1.0f).resolution(0.85f).flatLight(0.5f).occlusion(0.35f).outline(HAIR_LINE, 0.09f).motion(Motion.hair().idle(1.2f, HX, 1.0f, 0.35f).walk(2.5f, HX, 2.0f, 2.0f).drag(6.0f).hang(0.2f).turn(5.0f, HX).spring(60.0f, 8.0f).bend(1.6f));
        rig.extra("hair_l", Bone.HEAD).mirrorOf(right);
        Shape[] locks = new Shape[FRINGE.length];
        for (int i2 = 0; i2 < FRINGE.length; i2++) {
            locks[i2] = fringeLock(FRINGE[i2]);
        }
        Shape inner = Shapes.ellipsoid(HX, 28.25f, -0.28f, 2.56f, 2.64f, 2.69f).intersect(Shapes.above(29.3f));
        Shape bangs = Shapes.subtract(Shapes.union(locks), inner);
        Part fringe = rig.extra("fringe", Bone.HEAD).shape(toHead(bangs.paint(Paint.vertical(HAIR_TIP, 26.9f, HAIR, 29.2f)))).detail(1.3f).resolution(0.5f).flatLight(0.5f).occlusion(0.25f).outline(HAIR_LINE, 0.08f);
        shine(top, fringe);
        clip(rig);
        Strand side = Shapes.tube(-1.95f, 29.5f, 0.85f, 0.42f, -2.36f, 28.4f, 1.05f, 0.56f, -2.42f, 27.1f, 1.08f, 0.6f, -2.3f, 25.9f, 1.02f, 0.5f, -2.26f, 25.0f, 0.98f, 0.34f, -2.5f, 24.45f, 0.95f, 0.05f).flatten(2.3f, -0.55f, HX, 0.83f);
        Part lock = rig.extra("lock_r", Bone.HEAD).pivot(wx(-1.95f), wy(29.4f), wz(0.85f)).shape(toHead(pointed(side, -2.5f, 24.45f, 0.95f, DROP, 1.0f, 0.3f).paint(paint))).detail(0.8f).resolution(0.55f).flatLight(0.5f).occlusion(0.35f).outline(HAIR_LINE, 0.09f).motion(Motion.hair().drag(10.0f).hang(0.35f));
        rig.extra("lock_l", Bone.HEAD).mirrorOf(lock);
        Strand ahoge = Shapes.tube(0.05f, 30.8f, 0.1f, 0.24f, 0.03f, 31.45f, 0.2f, 0.26f, 0.3f, 32.05f, 0.32f, 0.22f, 0.8f, 32.38f, 0.42f, 0.17f, 1.34f, 32.35f, 0.5f, 0.11f, 1.72f, 32.02f, 0.55f, 0.05f).flatten(1.6f, HX, HX, 1.0f);
        rig.extra("ahoge", Bone.HEAD).pivot(wx(0.05f), wy(30.85f), wz(0.1f)).shape(toHead(ahoge.paint(Paint.vertical(HAIR, 31.2f, HAIR_TIP, 32.1f)))).resolution(0.5f).detail(0.9f).flatLight(0.5f).occlusion(0.3f).outline(HAIR_LINE, 0.08f).motion(Motion.hair().idle(4.0f, HX, 6.0f, 0.5f).walk(8.0f, HX, 8.0f, 2.0f).drag(6.0f).hang(0.3f).spring(70.0f, 5.0f).bend(1.2f));
    }

    private static int hairColor(float x, float y, float z) {
        int base = Colors.mix(HAIR_TIP, HAIR, smooth(25.0f, 27.6f, y));
        float rho = (float) Math.sqrt((x * x) + ((z + 0.25f) * (z + 0.25f)));
        float inner = (1.0f - smooth(1.9f, 2.55f, rho)) * (1.0f - smooth(26.4f, 27.6f, y));
        return Colors.mix(base, HAIR_DARK, inner * 0.75f);
    }

    private static Shape fringeLock(float[] f) {
        float f2;
        if (f[0] < 0.7f) {
            f2 = 0.52f;
        } else {
            f2 = f[9] > HX ? DROP : 0.28f;
        }
        float lift = f2;
        Strand lock = Shapes.strand(f[0], 0.05f, f[1], f[2], capZ(f[1], f[2], NZ), f[3], f[4], capZ(f[3], f[4], 0.1f), f[5], f[6], surf(f[5], f[6], lift), f[7], f[8], surf(f[7], f[8], 0.12f)).flattenFrom(2.8f, HX, HY, HZ);
        float dx = f[5] - f[7];
        float dy = f[6] - f[8];
        float l = (float) Math.sqrt((dx * dx) + (dy * dy));
        float dx2 = dx / l;
        float dy2 = dy / l;
        float ra = 0.9f * (1.0f + f[9]);
        float rb = 0.9f * (1.0f - f[9]);
        Shape2D v = Shape2D.polygon(f[7], f[8], (f[7] + (dx2 * ra)) - (dy2 * 0.34f), f[8] + (dy2 * ra) + (dx2 * 0.34f), (f[7] + (dx2 * 8.0f)) - (dy2 * 5.0f), f[8] + (dy2 * 8.0f) + (dx2 * 5.0f), f[7] + (dx2 * 8.0f) + (dy2 * 5.0f), (f[8] + (dy2 * 8.0f)) - (dx2 * 5.0f), f[7] + (dx2 * rb) + (dy2 * 0.34f), (f[8] + (dy2 * rb)) - (dx2 * 0.34f));
        return lock.intersect(Shapes.extrude(v, 6.0f, HX));
    }

    private static void clip(Rig rig) {
        Shape2D fish = Shape2D.blend(0.08f, Shape2D.ellipse(-0.1f, HX, 0.5f, 0.32f), Shape2D.polygon(0.3f, HX, 0.86f, 0.36f, 0.66f, HX, 0.86f, -0.36f));
        Shape body = Shapes.extrude(fish, 0.15f, 0.11f).scale(1.3f).paint(Paint.gradient(-3869978, HX, 0.35f, HX, FISH, HX, -0.35f, HX));
        float z = surf(1.62f, 28.7f, 0.75f);
        float ax = 2.3f + 0.75f;
        float ay = 2.3f + 0.75f;
        float az = RZ + 0.75f;
        float nx = 1.62f / (ax * ax);
        float ny = (28.7f - HY) / (ay * ay);
        float nz = (z - HZ) / (az * az);
        float l = (float) Math.sqrt((nx * nx) + (ny * ny) + (nz * nz));
        float yaw = (float) Math.toDegrees(Math.atan2(nx, nz));
        float pitch = (float) (-Math.toDegrees(Math.asin(ny / l)));
        Shape placed = body.rotate(HX, HX, 35.0f).rotate(pitch, yaw, HX).move(1.62f, 28.7f, z);
        Vec3 eye = new Vec3(-0.5f, 0.08f, 0.2f).rotate(HX, HX, 35.0f).rotate(pitch, yaw, HX).add(1.62f, 28.7f, z);
        Vec3 n = new Vec3(HX, HX, 1.0f).rotate(HX, HX, 35.0f).rotate(pitch, yaw, HX);
        Canvas c = new Canvas(32, 32);
        c.fill(Shape2D.circle(16.0f, 16.0f, 7.0f), -14796236);
        c.fill(Shape2D.circle(13.5f, 13.5f, 2.2f), -855638017);
        Decal dot = Decal.project(c, wx(eye.x()), wy(eye.y()), wz(eye.z()), n.x(), n.y(), n.z(), HX, 1.0f, HX, 0.33600003f, 0.33600003f).depth(0.25f, 0.25f);
        rig.extra("clip", Bone.HEAD).shape(toHead(placed)).resolution(DROP).detail(0.5f).flatLight(DROP).outline(FISH_LINE, 0.1f).decal(dot);
    }

    private static void shine(Part top, Part fringe) {
        Canvas c = new Canvas(96, 24);
        c.fill(Shape2D.union(Shape2D.taper(3.0f, 12.0f, 1.0f, 48.0f, 12.0f, 17.0f), Shape2D.taper(48.0f, 12.0f, 17.0f, 93.0f, 12.0f, 1.0f)), -419442022, 1.5f);
        float[][] dash = {new float[]{-52.0f, 0.72f, 0.12f, 6.0f}, new float[]{-24.0f, 0.9f, -0.06f, -4.0f}, new float[]{4.0f, 0.66f, 0.08f, 5.0f}, new float[]{27.0f, 0.82f, -0.08f, -6.0f}, new float[]{57.0f, 0.7f, 0.1f, 3.0f}};
        for (float[] d : dash) {
            double a = Math.toRadians(d[0]);
            float sx = (float) Math.sin(a);
            float cz = (float) Math.cos(a);
            float[] p = onCap(sx, 0.36f + (0.3f * (1.0f - cz)) + d[2], cz, 0.28f);
            float nx = p[0];
            float ny = p[1] - 28.25f;
            float nz = p[2] + 0.28f;
            Vec3 up = new Vec3(HX, 1.0f, HX).rotate(HX, HX, d[3]);
            Decal glint = Decal.project(c, wx(p[0]), wy(p[1]), wz(p[2]), nx, ny * 0.6f, nz, up.x(), up.y(), up.z(), d[1] * HS, 0.2464f).depth(0.896f, 0.896f).maxAngle(80.0f).flatLight(0.8f).occlusion(HX);
            top.decal(glint);
            fringe.decal(glint);
        }
    }

    private static Shape pointed(Strand lock, float tx, float ty, float tz, float spread, float rise, float skew) {
        float deg = (float) Math.toDegrees(Math.atan2(tx, tz - (-0.25f)));
        float ra = rise * (1.0f + skew);
        float rb = rise * (1.0f - skew);
        Shape2D v = Shape2D.polygon(-5.0f, ty + 8.0f, 5.0f, ty + 8.0f, spread, ty + ra, HX, ty, -spread, ty + rb);
        return lock.intersect(Shapes.extrude(v, 5.0f, HX).rotate(HX, deg, HX, HX, HX, -0.25f));
    }

    private static Shape backLock(float deg, float endY, float width, float skew) {
        double a = Math.toRadians(deg);
        float sx = (float) Math.sin(a);
        float cz = (float) Math.cos(a);
        float back = Math.max(HX, Math.min(1.0f, (Math.abs(deg) - 100.0f) / 70.0f));
        float[] ys = {30.1f, 29.1f, 27.9f, 26.9f, ((26.9f + endY) * 0.5f) - 0.1f, endY};
        float[] rx = {2.04f, 2.59f, 2.7f, 2.52f, 2.42f, 2.32f};
        float[] rz = {2.14f, 2.72f, 2.83f, 2.62f, 2.48f, RZ};
        float[] out = {-0.34f, -0.06f, 0.04f, 0.04f, 0.02f, (-0.06f) - (0.24f * back)};
        float[] rs = {0.7f, 1.05f, 1.05f, 0.95f, 0.64f, 0.06f};
        float[] p = new float[24];
        int i = 0;
        while (i < 6) {
            p[i * 4] = sx * (rx[i] + out[i]);
            p[(i * 4) + 1] = ys[i];
            p[(i * 4) + 2] = (-0.28f) + (cz * (rz[i] + out[i]));
            p[(i * 4) + 3] = rs[i] * (i == 5 ? 1.0f : width);
            i++;
        }
        return pointed(Shapes.tube(p).flattenFrom(2.1f, HX, HY, HZ), p[20], endY, p[22], 0.62f, 1.25f, skew);
    }

    private static Shape topLock(float deg, float endY) {
        double a = Math.toRadians(deg);
        float sx = (float) Math.sin(a);
        float cz = (float) Math.cos(a);
        float[] ring = {2.2f, 2.8f, 2.98f, 3.0f};
        float[] ys = {29.4f, 28.2f, 27.2f, endY};
        float[] rs = {0.55f, 0.75f, 0.55f, 0.08f};
        float[] p = new float[16];
        for (int i = 0; i < 4; i++) {
            p[i * 4] = sx * ring[i];
            p[(i * 4) + 1] = ys[i];
            p[(i * 4) + 2] = (-0.25f) + (cz * ring[i] * 1.05f);
            p[(i * 4) + 3] = rs[i];
        }
        return pointed(Shapes.tube(p).flattenFrom(2.2f, HX, HY, HZ), p[12], endY, p[14], 0.6f, 1.15f, 0.2f);
    }

    private static void ears(Rig rig) {
        Shape front = Shapes.extrude(Shape2D.polygon(-1.1f, HX, 1.1f, HX, 0.08f, 2.55f), 0.64f, 0.32f);
        Shape profile = Shapes.extrude(Shape2D.polygon(0.6f, NZ, -0.62f, NZ, HZ, 2.7f, 0.2f, 2.7f), 1.3f, 0.2f).rotate(HX, 90.0f, HX);
        Shape outer = Shapes.intersect(front, profile).paint(Paint.vertical(HAIR, 1.45f, EAR_TIP, 2.25f));
        Shape inner = Shapes.extrude(Shape2D.polygon(-0.66f, 0.3f, 0.68f, 0.3f, 0.07f, 1.95f), 0.5f, 0.14f).move(HX, HX, 0.62f).paint(Paint.of(EAR_PINK).flatLight(0.7f));
        Shape tuft = Shapes.union(Shapes.strand(0.14f, 0.05f, -0.34f, 0.28f, 0.25f, -0.46f, 0.6f, 0.44f, -0.38f, 0.9f, 0.5f), Shapes.strand(0.14f, 0.05f, 0.3f, 0.28f, 0.25f, 0.46f, 0.58f, 0.44f, 0.56f, 0.86f, 0.48f)).paint(Paint.of(TUFT).flatLight(0.7f));
        Shape ear = Shapes.union(Shapes.subtract(0.1f, outer, inner), tuft).scale(1.18f).rotate(-6.0f, -18.0f, 24.0f).move(-1.6f, 29.75f, HZ);
        Part right = rig.extra("ear_r", Bone.HEAD).pivot(wx(-1.6f), wy(29.9f), wz(HZ)).shape(toHead(ear)).resolution(0.58f).detail(0.9f).flatLight(DROP).outline(HAIR_LINE, 0.09f).motion(earMotion(HX, -1.0f));
        rig.extra("ear_l", Bone.HEAD).mirrorOf(right).motion(earMotion(140.0f, 1.0f));
    }

    private static Motion earMotion(float phase, float side) {
        return Motion.none().idle(2.5f, HX, 5.0f * side, 0.55f).idlePhase(phase).walk(3.0f, HX, 3.0f * side, 2.0f).spring(90.0f, 7.0f);
    }

    private static void body(Rig rig) {
        Shape yoke = Shapes.union(Shapes.cone(HX, 22.6f, -0.2f, 1.02f, -1.95f, 22.2f, -0.2f, 0.9f), Shapes.cone(HX, 22.6f, -0.2f, 1.02f, 1.95f, 22.2f, -0.2f, 0.9f));
        Shape chest = Shapes.ellipsoid(HX, 20.5f, -0.1f, 2.3f, 2.4f, 1.72f);
        Shape belly = Shapes.ellipsoid(HX, 17.8f, -0.02f, 2.25f, 2.9f, 1.7f);
        Shape blouse = Shapes.ellipsoid(HX, 15.25f, -0.05f, 2.45f, 1.25f, 1.86f);
        Shape torso = Shapes.blend(1.0f, yoke, chest, belly, blouse).paint(Paint.field(Mika::hoodie));
        Shape pocket = Shapes.intersect(Shapes.ellipsoid(HX, 16.3f, -0.02f, 2.6f, 2.9f, 2.02f), Shapes.extrude(Shape2D.polygon(-1.6f, 17.3f, 1.6f, 17.3f, 2.15f, 14.75f, -2.15f, 14.75f), 2.0f, HX).move(HX, HX, 2.0f)).color(HOODIE);
        Shape hem = Shapes.extrude(Shape2D.ellipse(HX, HX, 2.42f, 1.8f), DROP, 0.3f).rotate(90.0f, HX, HX).move(HX, 14.2f, -0.05f).color(RIB);
        rig.part(Bone.BODY).pivot(HX, 23.4f, -0.1f).shape(Shapes.union(Shapes.blend(0.25f, torso, pocket), hem)).resolution(1.15f).detail(1.0f).outline(HOODIE_LINE, 0.12f).occlusion(0.3f).flatLight(0.2f).decal(pocketDecal()).decal(pawDecal()).decal(hemRib());
        rig.extra("collar", Bone.BODY).shape(rim().paint(Paint.field(Mika::rimColor))).resolution(0.7f).detail(0.8f).occlusion(0.3f).flatLight(0.2f).outline(HOODIE_LINE, 0.1f);
        Shape pouch = Shapes.blend(0.8f, Shapes.ellipsoid(HX, 21.7f, -1.85f, 2.2f, 2.2f, 0.85f), Shapes.cone(HX, 20.6f, -2.3f, 0.8f, HX, 19.1f, -2.2f, 0.28f));
        Shape dip = Shapes.ellipsoid(HX, HX, HX, 1.55f, 0.35f, 0.85f).rotate(-26.6f, HX, HX).move(HX, 22.95f, -1.95f);
        Shape hood = Shapes.subtract(0.15f, pouch.intersect(Shapes.plane(HX, 22.7f, -2.2f, HX, 1.0f, -0.5f)), dip).paint(Paint.field(Mika::hoodColor));
        rig.extra("hood", Bone.BODY).shape(hood).resolution(0.85f).detail(0.6f).occlusion(0.3f).flatLight(0.2f).outline(HOODIE_LINE, 0.12f);
        rig.extra("skirt", Bone.BODY).pivot(HX, 14.6f, HX).shape(skirt()).resolution(0.9f).detail(0.8f).occlusion(0.35f).outline(SKIRT_LINE, 0.12f).decal(skirtStripe()).motion(Motion.none().walk(HX, 8.0f, HX, 1.0f).walkPhase(-90.0f).drag(2.0f).spring(80.0f, 10.0f));
        Shape cord = Shapes.tube(-0.32f, 23.5f, 1.45f, 0.13f, -0.38f, 22.6f, 1.75f, 0.13f, -0.44f, 21.3f, 1.95f, 0.13f, -0.48f, 20.1f, 2.0f, 0.12f).color(-264984);
        Shape bell = Shapes.sphere(-0.48f, 19.6f, 2.05f, 0.45f).paint(Paint.radial(-3920, -0.62f, 19.8f, RZ, 0.6f, GOLD));
        Part cordR = rig.extra("cord_r", Bone.BODY).pivot(-0.32f, 23.5f, 1.45f).shape(Shapes.union(cord, bell)).resolution(0.5f).detail(0.5f).outline(HOODIE_LINE, 0.08f).decal(bellDecal(-0.48f)).motion(Motion.none().rest(-3.0f, HX, HX).idle(1.5f, HX, 2.0f, 0.5f).walk(5.0f, HX, 6.0f, 2.0f).drag(-4.0f).hang(0.9f).spring(60.0f, 6.0f).bend(0.8f));
        rig.extra("cord_l", Bone.BODY).mirrorOf(cordR);
    }

    private static Shape rim() {
        return Shapes.tube(0.02f, 23.64f, 1.44f, 0.36f, -0.5f, 23.84f, 1.3f, 0.41f, -1.12f, 24.08f, 0.8f, 0.43f, -1.42f, 24.2f, -0.18f, 0.43f, -1.1f, 24.3f, -1.14f, 0.43f, 0.1f, 24.34f, -1.68f, 0.43f).flattenFrom(1.25f, HX, 24.1f, -0.25f).mirrorX();
    }

    private static int hoodie(float x, float y, float z) {
        float rho = (float) Math.sqrt((x * x) + ((z + 0.2f) * (z + 0.2f)));
        float collar = smooth(22.4f, 23.3f, y) * (1.0f - smooth(1.7f, 2.3f, rho)) * 0.6f;
        float fold = (1.0f - smooth(14.6f, 15.3f, y)) * 0.55f;
        return Colors.mix(HOODIE, HOODIE_SHADE, Math.max(collar, fold));
    }

    private static int rimColor(float x, float y, float z) {
        float rho = (float) Math.sqrt((x * x) + ((z + 0.2f) * (z + 0.2f)));
        float top = smooth(0.28f, 0.36f, (y - 24.0f) + (0.22f * z));
        return Colors.mix(HOODIE, LINING, Math.max(top, 1.0f - smooth(HS, 1.38f, rho)));
    }

    private static int hoodColor(float x, float y, float z) {
        float ex = x / 2.2f;
        float ey = (y - 21.7f) / 2.2f;
        float ez = (z + 1.85f) / 0.85f;
        float cut = ((y - 22.7f) - (0.5f * (z + 2.2f))) * 0.894f;
        return (cut <= -0.5f || ((ex * ex) + (ey * ey)) + (ez * ez) >= 0.8f) ? HOODIE : LINING;
    }

    private static Shape skirt() {
        return Shapes.field(Mika::skirtDistance, -3.3f, 11.8f, -2.8f, 3.3f, 14.4f, 2.8f).paint(Paint.field(Mika::skirtColor));
    }

    private static float flare(float y) {
        float t = Math.max(HX, Math.min(1.0f, (14.0f - y) / 2.1f));
        return t * (2.0f - t);
    }

    private static float pleat(float x, float z, float t) {
        float rx = 2.2f + (0.75f * t);
        float rz = 1.66f + (0.84f * t);
        double f = (Math.atan2(x / rx, z / rz) * 16.0d) / 6.283185307179586d;
        return (float) Math.abs((2.0d * (f - Math.floor(f))) - 1.0d);
    }

    private static float skirtDistance(float x, float y, float z) {
        float t = flare(y);
        float rx = 2.2f + (0.75f * t);
        float rz = 1.66f + (0.84f * t);
        float a = x / rx;
        float b = z / rz;
        float k = (float) Math.sqrt((a * a) + (b * b));
        float side = (((k - 1.0f) * Math.min(rx, rz)) * 0.72f) - ((0.2f * t) * (pleat(x, z, t) - 0.5f));
        return Math.max(side, Math.max(y - 14.3f, 11.9f - y));
    }

    private static int skirtColor(float x, float y, float z) {
        float t = flare(y);
        return Colors.mix(SKIRT_DARK, SKIRT, 0.45f + (0.55f * ((1.0f - t) + (t * pleat(x, z, t)))));
    }

    private static Decal skirtStripe() {
        Canvas c = new Canvas(512, 24);
        c.fill(Shape2D.box(256.0f, 12.0f, 256.0f, 7.0f, HX), MINT);
        return Decal.wrap(c, HX, 12.35f, HX, HX, 1.0f, HX, HX, HX, 1.0f, 360.0f, 0.16f).radius(2.2f, 3.2f);
    }

    private static void arms(Rig rig) {
        Shape shoulder = Shapes.sphere(-2.72f, 21.95f, HZ, 0.95f);
        Shape upper = Shapes.cone(-2.85f, 21.6f, HZ, 0.9f, -3.35f, 18.4f, -0.2f, 0.88f);
        Shape elbow = Shapes.sphere(-3.38f, 18.3f, -0.18f, 0.9f);
        Shape fore = Shapes.cone(-3.38f, 18.2f, -0.18f, 0.9f, -3.72f, 15.0f, 0.45f, HS);
        Shape sleeve = Shapes.blend(DROP, shoulder, upper, elbow, fore).paint(Paint.field(Mika::sleeve));
        Shape cuff = Shapes.cylinder(-3.74f, 15.05f, 0.47f, -3.8f, 13.4f, 0.69f, 0.9f, 0.28f).paint(Paint.field(Mika::cuff));
        Shape arm = Shapes.blend(0.15f, sleeve, cuff);
        Part right = rig.part(Bone.RIGHT_ARM).pivot(-2.72f, 21.95f, HZ).grip(-3.8f, 12.9f, 0.9f).shape(arm).detail(0.9f).resolution(1.05f).outline(HOODIE_LINE, 0.12f).occlusion(0.3f).flatLight(0.2f).swing(0.65f, 40.0f).joint(-3.38f, 18.3f, -0.18f, 0.7f).decal(cuffRib());
        rig.part(Bone.LEFT_ARM).mirrorOf(right);
        Part hand = rig.extra("hand_r", Bone.RIGHT_ARM).shape(hand()).resolution(DROP).detail(2.6f).flatLight(1.0f).occlusion(0.25f).outline(SKIN_LINE, 0.08f);
        rig.extra("hand_l", Bone.LEFT_ARM).mirrorOf(hand);
    }

    private static int sleeve(float x, float y, float z) {
        float t = Math.max(HX, Math.min(1.0f, (21.9f - y) / 6.9f));
        float inner = smooth(0.25f, 0.85f, x + 2.8f + (0.94f * t)) * 0.5f;
        float crease = (1.0f - smooth(0.2f, 0.55f, Math.abs(y - 18.25f))) * smooth(0.2f, 0.7f, z) * 0.45f;
        return Colors.mix(HOODIE, HOODIE_SHADE, Math.max(inner, crease));
    }

    private static int cuff(float x, float y, float z) {
        float l = (float) Math.sqrt(((-0.06f) * (-0.06f)) + ((-1.65f) * (-1.65f)) + (0.22f * 0.22f));
        float px = x + 3.8f;
        float py = y - 13.4f;
        float pz = z - 0.69f;
        float along = (-(((px * (-0.06f)) + (py * (-1.65f))) + (pz * 0.22f))) / l;
        float rx = px + ((along * (-0.06f)) / l);
        float ry = py + ((along * (-1.65f)) / l);
        float rz = pz + ((along * 0.22f) / l);
        float radial = (float) Math.sqrt((rx * rx) + (ry * ry) + (rz * rz));
        return Colors.mix(RIB, CUFF_IN, (1.0f - smooth(0.04f, 0.12f, along)) * (1.0f - smooth(0.55f, 0.68f, radial)));
    }

    private static Shape hand() {
        float[][] f = {new float[]{-3.47f, 0.86f, 13.06f}, new float[]{-3.79f, 0.95f, 12.97f}, new float[]{-4.1f, 0.86f, 13.04f}};
        Shape[] tips = new Shape[f.length];
        for (int i = 0; i < f.length; i++) {
            tips[i] = Shapes.capsule(f[i][0], 13.5f, f[i][1] - 0.12f, f[i][0], f[i][2], f[i][1], 0.16f);
        }
        Shape thumb = Shapes.capsule(-3.36f, 13.48f, 0.64f, -3.3f, 13.2f, 0.76f, 0.15f);
        return Shapes.union(Shapes.union(tips), thumb).paint(Paint.of(SKIN));
    }

    private static void legs(Rig rig) {
        Shape hip = Shapes.sphere(-1.12f, 13.2f, -0.05f, 1.0f);
        Shape thigh = Shapes.cone(-1.18f, 12.8f, HX, 1.13f, -1.1f, 7.9f, 0.1f, 0.88f);
        Shape knee = Shapes.sphere(-1.08f, 7.45f, 0.12f, 0.82f);
        Shape kneecap = Shapes.sphere(-1.07f, 7.5f, 0.48f, 0.5f);
        Shape calf = Shapes.tube(-1.08f, 7.2f, 0.1f, 0.84f, -1.1f, 5.6f, HZ, 0.93f, -1.13f, 3.6f, -0.02f, 0.64f, -1.15f, 2.2f, HX, 0.52f);
        Shape leg = Shapes.blend(0.45f, hip, thigh, knee, kneecap, calf).paint(Paint.field(Mika::tights));
        Part right = rig.part(Bone.RIGHT_LEG).pivot(-1.2f, 13.7f, HX).shape(leg).detail(0.85f).resolution(1.1f).outline(TIGHTS_LINE, 0.12f).occlusion(0.35f).swing(0.55f, 30.0f).joint(-1.08f, 7.45f, 0.12f, 0.8f);
        rig.part(Bone.LEFT_LEG).mirrorOf(right);
        Part shoe = rig.extra("shoe_r", Bone.RIGHT_LEG).shape(sneaker().rotate(HX, -7.0f, HX, -1.2f, HX, 0.2f)).resolution(0.75f).detail(1.0f).outline(SHOE_LINE, 0.12f).decal(laces());
        rig.extra("shoe_l", Bone.LEFT_LEG).mirrorOf(shoe);
    }

    private static int tights(float x, float y, float z) {
        double a = Math.atan2(-(x + 1.1f), z - 0.02f) - 0.35d;
        float sheen = smooth(0.6f, 0.95f, (float) Math.cos(a)) * smooth(2.4f, 4.5f, y) * 0.5f;
        float dk = (float) Math.sqrt(((y - 7.5f) * (y - 7.5f)) + ((x + 1.07f) * (x + 1.07f) * 1.4f));
        float knee = (1.0f - smooth(0.3f, 0.8f, dk)) * smooth(0.2f, 0.7f, z) * 0.55f;
        return Colors.mix(TIGHTS, TIGHTS_SHEEN, Math.max(sheen, knee));
    }

    private static Shape sneaker() {
        Shape sole = Shapes.blend(0.25f, Shapes.box(-1.2f, 0.3f, 0.5f, 0.9f, 0.3f, 1.84f, 0.26f), Shapes.ellipsoid(-1.2f, 0.36f, 2.05f, 0.82f, 0.34f, 0.5f)).intersect(Shapes.above(HX)).paint(Paint.of(SOLE).band(-1011800, HX, 0.46f, HX, HX, 1.0f, HX, 0.07f, 0.05f));
        Shape toe = Shapes.ellipsoid(-1.2f, 0.9f, 1.15f, 0.74f, 0.55f, HS);
        Shape heel = Shapes.ellipsoid(-1.2f, 1.28f, -0.48f, 0.75f, 0.95f, 0.84f);
        Shape upper = Shapes.blend(0.45f, toe, heel).color(SHOE);
        Shape cap = Shapes.ellipsoid(-1.2f, 0.72f, 1.86f, 0.68f, 0.36f, 0.5f).color(SOLE);
        Shape collar = Shapes.torus(-1.2f, 2.1f, -0.4f, 0.6f, 0.26f).color(MINT);
        Shape tongue = Shapes.box(-1.2f, 1.75f, 0.35f, 0.42f, 0.42f, 0.3f, 0.18f).rotate(-25.0f, HX, HX, -1.2f, 1.75f, 0.35f).color(-207907);
        Shape tab = Shapes.capsule(-1.2f, 1.8f, -1.2f, -1.2f, 2.45f, -1.02f, 0.17f).color(MINT);
        return Shapes.union(sole, Shapes.blend(0.12f, upper, cap), collar, tongue, tab);
    }

    private static void tail(Rig rig) {
        float[] pts = {HX, 13.95f, -1.45f, 0.58f, 0.15f, 13.35f, -2.7f, 0.76f, 0.6f, 13.2f, -4.1f, 0.92f, 1.6f, 14.0f, -5.15f, 1.06f, 2.6f, 15.9f, -5.5f, 1.15f, 3.2f, 18.1f, -5.15f, 1.15f, 3.35f, 19.9f, -4.35f, 1.04f, 3.0f, 21.0f, -3.45f, 0.72f, 2.55f, 21.45f, -2.85f, 0.26f};
        Curve curve = new Curve(pts);
        Shape tufts = Shapes.union(Shapes.strand(0.24f, 0.04f, 2.68f, 21.32f, -3.02f, 2.36f, 21.62f, -2.68f, 1.96f, 21.62f, -2.62f), Shapes.strand(0.24f, 0.04f, 2.68f, 21.32f, -3.02f, 2.46f, 21.6f, -2.62f, 2.42f, 21.62f, -2.2f), Shapes.strand(0.24f, 0.04f, 2.68f, 21.32f, -3.02f, 2.44f, 21.7f, -2.72f, 2.4f, 22.12f, -2.62f));
        Part partPivot = rig.extra("tail", Bone.BODY).pivot(HX, 13.95f, -1.45f);
        Shape shapeUnion = Shapes.union(Shapes.tube(pts), tufts);
        Objects.requireNonNull(curve);
        Part tail = partPivot.shape(shapeUnion.paint(Paint.field(curve::tailColor))).detail(1.0f).resolution(1.1f).flatLight(0.3f).outline(HAIR_LINE, 0.1f).motion(Motion.tail().idle(3.0f, 14.0f, 4.0f, DROP).walk(4.0f, 12.0f, 3.0f, 1.0f).spring(30.0f, 4.0f).bend(1.4f));
        Canvas chevron = new Canvas(128, 96);
        chevron.fill(Shape2D.union(Shape2D.line(10.0f, 80.0f, 64.0f, 26.0f, 24.0f), Shape2D.line(64.0f, 26.0f, 118.0f, 80.0f, 24.0f)), -1999082968, 6.0f);
        for (float t : new float[]{0.3f, 0.43f, 0.56f}) {
            tail.decal(curve.chevron(chevron, t));
        }
    }

    private static float smooth(float e0, float e1, float x) {
        float t = Math.max(HX, Math.min(1.0f, (x - e0) / (e1 - e0)));
        return t * t * (3.0f - (2.0f * t));
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/designs/Mika$Curve.class */
    @Environment(EnvType.CLIENT)
    private static final class Curve {
        private final float[] xs;
        private final float[] ys;
        private final float[] zs;
        private final float[] len;
        private final int n;

        Curve(float[] p) {
            int count = (p.length / 4) - 2;
            this.n = (count * 12) + 1;
            this.xs = new float[this.n];
            this.ys = new float[this.n];
            this.zs = new float[this.n];
            this.len = new float[this.n];
            int k = 0;
            int i = 1;
            while (i <= count) {
                int prev = (i - 1) * 4;
                int cur = i * 4;
                int next = (i + 1) * 4;
                float ax = i == 1 ? p[0] : (p[prev] + p[cur]) * 0.5f;
                float ay = i == 1 ? p[1] : (p[prev + 1] + p[cur + 1]) * 0.5f;
                float az = i == 1 ? p[2] : (p[prev + 2] + p[cur + 2]) * 0.5f;
                float cx = i == count ? p[next] : (p[cur] + p[next]) * 0.5f;
                float cy = i == count ? p[next + 1] : (p[cur + 1] + p[next + 1]) * 0.5f;
                float cz = i == count ? p[next + 2] : (p[cur + 2] + p[next + 2]) * 0.5f;
                int j = 0;
                while (true) {
                    if (j >= 12 && (i != count || j != 12)) {
                        break;
                    }
                    float t = j / 12;
                    float u = 1.0f - t;
                    this.xs[k] = (u * u * ax) + (2.0f * u * t * p[cur]) + (t * t * cx);
                    this.ys[k] = (u * u * ay) + (2.0f * u * t * p[cur + 1]) + (t * t * cy);
                    this.zs[k] = (u * u * az) + (2.0f * u * t * p[cur + 2]) + (t * t * cz);
                    if (k > 0) {
                        float dx = this.xs[k] - this.xs[k - 1];
                        float dy = this.ys[k] - this.ys[k - 1];
                        float dz = this.zs[k] - this.zs[k - 1];
                        this.len[k] = this.len[k - 1] + ((float) Math.sqrt((dx * dx) + (dy * dy) + (dz * dz)));
                    }
                    k++;
                    j++;
                }
                i++;
            }
        }

        float param(float x, float y, float z) {
            int best = 0;
            float bd = Float.MAX_VALUE;
            for (int i = 0; i < this.n; i++) {
                float dx = x - this.xs[i];
                float dy = y - this.ys[i];
                float dz = z - this.zs[i];
                float d = (dx * dx) + (dy * dy) + (dz * dz);
                if (d < bd) {
                    bd = d;
                    best = i;
                }
            }
            return this.len[best] / this.len[this.n - 1];
        }

        int tailColor(float x, float y, float z) {
            float t = param(x, y, z);
            return Colors.mix(Colors.mix(Mika.HAIR_DARK, Mika.HAIR, Mika.smooth(0.04f, 0.22f, t)), Mika.TAIL_TIP, Mika.smooth(0.72f, 0.8f, t));
        }

        Decal chevron(Canvas canvas, float t) {
            float target = t * this.len[this.n - 1];
            int i = 1;
            while (i < this.n - 2 && this.len[i] < target) {
                i++;
            }
            float tx = this.xs[i + 1] - this.xs[i - 1];
            float ty = this.ys[i + 1] - this.ys[i - 1];
            float tz = this.zs[i + 1] - this.zs[i - 1];
            return Decal.wrap(canvas, this.xs[i], this.ys[i], this.zs[i], tx, ty, tz, this.xs[i] - 1.8f, this.ys[i] - 17.6f, this.zs[i] + 3.3f, 140.0f, 0.9f).radius(0.3f, 1.6f);
        }
    }

    private static Decal face() {
        Canvas c = new Canvas(256, 200);
        Shape2D[] shadow = new Shape2D[FRINGE.length];
        for (int i = 0; i < FRINGE.length; i++) {
            float[] f = FRINGE[i];
            float ex = f[7] + ((f[5] - f[7]) * 0.12f);
            float ey = f[8] + ((f[6] - f[8]) * 0.12f);
            shadow[i] = Shape2D.path(f[0] * 1.7f * PPU, 9.0f, u(f[3]), v(f[4]) + 13.356522f, u(f[5]), v(f[6]) + 13.356522f, u(ex), v(ey) + 13.356522f);
        }
        c.fill(Shape2D.union(shadow), -1713793398, 3.0f);
        for (int s = -1; s <= 1; s += 2) {
            eye(c, 128.0f + (s * 53.0f), 114.0f, s);
        }
        c.fill(Shape2D.curve(125.5f, 146.5f, 128.5f, 150.5f, 131.5f, 149.0f, 2.2f, 1.4f), -1713795968);
        c.fill(Shape2D.path(3.0f, 3.0f, 117.0f, 177.0f, 122.5f, 182.5f, 128.0f, 178.5f, 133.5f, 182.5f, 139.0f, 177.0f), -7455954);
        return Decal.front(c, HX, wy(27.203125f), wz(2.1f), 5.152f, 4.0249996f).depth(1.4f, 1.4f).maxAngle(72.0f).flatLight(1.0f).occlusion(0.3f);
    }

    private static void eye(Canvas c, float ex, float ey, int s) {
        Shape2D lid = Shape2D.ellipse(ex, ey + 10.0f, 31.0f, 40.0f).rotate(s * 12.0f, ex, ey + 10.0f);
        Shape2D white = Shape2D.ellipse(ex, ey + 2.0f, 26.0f, 31.0f).intersect(lid);
        c.fill(white, Paint2D.linear(ex, ey - 28.0f, ex, ey + 4.0f, -2571046, -776));
        Shape2D ring = Shape2D.ellipse(ex + s, ey + 5.0f, 21.0f, 27.0f);
        c.fill(ring.intersect(white), Paint2D.linear(ex, ey - 22.0f, ex, ey + 32.0f, -11920890, -5748206, -1535446, -11414));
        c.fill(Shape2D.ellipse(ex + s, ey + 6.0f, 9.0f, 21.0f).intersect(white), -1711287446, 4.0f);
        c.fill(ring.outline(2.5f).intersect(white), -1338371580);
        float px = ex + s;
        float py = ey + 6.0f;
        c.fill(Shape2D.circle(px - 42.2f, py, 46.7f).intersect(Shape2D.circle(px + 42.2f, py, 46.7f)), -14414846);
        c.fill(Shape2D.ellipse(ex - 8.0f, ey - 8.0f, 7.5f, 9.0f).rotate(-20.0f, ex - 8.0f, ey - 8.0f), -1);
        c.fill(Shape2D.circle(ex + 9.0f, ey + 17.0f, 3.5f), -419430401);
        c.fill(Shape2D.curve(ex - (s * 28.0f), ey - 8.0f, ex - (s * 2.0f), ey - 42.0f, ex + (s * 30.0f), ey - 24.0f, 4.0f, 9.5f), LASH);
        c.fill(Shape2D.taper(ex + (s * 24.0f), ey - 24.0f, 7.0f, ex + (s * 40.0f), ey - 36.0f, 1.5f), LASH);
        c.fill(Shape2D.curve(ex + (s * 8.0f), ey + 34.0f, ex + (s * 21.0f), ey + 31.0f, ex + (s * 26.0f), ey + 17.0f, 1.2f, 3.0f), -866506210);
        c.fill(Shape2D.curve(ex - (s * 17.0f), ey - 38.0f, ex + (s * 2.0f), ey - 48.0f, ex + (s * 24.0f), ey - 38.0f, 1.5f, 2.2f), 1721786442);
        c.fill(Shape2D.curve(ex - (s * 16.0f), ey - 50.0f, ex + (s * 2.0f), ey - 58.0f, ex + (s * 22.0f), ey - 53.0f, 3.0f, 2.0f), BROW);
        float bx = ex + (s * 10.0f);
        float by = ey + 42.0f;
        c.fill(Shape2D.ellipse(bx, by, 20.0f, 9.0f), 1627357068, 9.0f);
        for (int i = 0; i < 3; i++) {
            c.fill(Shape2D.line((bx - 8.0f) + (i * 7.0f), by - 4.0f, (bx - 12.0f) + (i * 7.0f), by + 4.0f, 1.6f), 1894277230);
        }
    }

    private static Decal pocketDecal() {
        Canvas c = new Canvas(240, 144);
        for (int i = 0; i < 18; i++) {
            c.fill(Shape2D.box(43.5f + (i * 9.0f), 16.5f, 2.6f, 0.75f, 0.7f), -1722777742);
        }
        for (int i2 = 0; i2 < 23; i2++) {
            c.fill(Shape2D.box(21.0f + (i2 * 9.0f), 129.0f, 2.6f, 0.75f, 0.7f), -1722777742);
        }
        for (int s = -1; s <= 1; s += 2) {
            float tx = 120.0f + (s * 78.0f);
            float bx = 120.0f + (s * 102.75f);
            c.fill(Shape2D.taper(tx, 13.5f, 6.0f, bx, 132.0f, 3.75f), -1872719998);
            c.fill(Shape2D.taper(tx + (s * 2.25f), 13.5f, 2.25f, bx + (s * 2.25f), 132.0f, 1.5f), -1605733274);
            c.fill(Shape2D.taper(tx - (s * 4.5f), 16.5f, 1.9f, bx - (s * 4.5f), 129.0f, 1.5f), 1627389951);
        }
        return Decal.front(c, HX, 16.03f, 2.3f, 5.0f, 3.0f).depth(1.2f, 1.2f).maxAngle(75.0f);
    }

    private static Decal pawDecal() {
        Canvas c = new Canvas(128, 128);
        Shape2D pad = Shape2D.blend(8.0f, Shape2D.ellipse(64.0f, 90.0f, 27.0f, 19.0f), Shape2D.circle(48.0f, 78.0f, 15.0f), Shape2D.circle(80.0f, 78.0f, 15.0f));
        c.fill(pad, -877910);
        c.fill(Shape2D.ellipse(28.0f, 52.0f, 11.0f, 14.0f).rotate(-25.0f, 28.0f, 52.0f), -877910);
        c.fill(Shape2D.ellipse(51.0f, 33.0f, 11.0f, 14.5f).rotate(-8.0f, 51.0f, 33.0f), -877910);
        c.fill(Shape2D.ellipse(77.0f, 33.0f, 11.0f, 14.5f).rotate(8.0f, 77.0f, 33.0f), -877910);
        c.fill(Shape2D.ellipse(100.0f, 52.0f, 11.0f, 14.0f).rotate(25.0f, 100.0f, 52.0f), -877910);
        return Decal.front(c, 1.2f, 20.6f, 1.6f, 1.5f, 1.5f).depth(1.5f, 1.5f).maxAngle(70.0f);
    }

    private static Canvas ribs(int count, int width) {
        Canvas c = new Canvas(width, 16);
        float step = width / count;
        for (int i = 0; i < count; i++) {
            float x = (i * step) + (step * 0.5f);
            c.fill(Shape2D.box(x, 8.0f, step * 0.18f, 8.0f, HX), 1078491756);
        }
        return c;
    }

    private static Decal hemRib() {
        return Decal.wrap(ribs(64, 506), HX, 14.2f, -0.05f, HX, 1.0f, HX, HX, HX, 1.0f, 360.0f, 0.62f).radius(1.2f, 3.3f);
    }

    private static Decal cuffRib() {
        return Decal.wrap(ribs(24, 256), -3.77f, 14.3f, 0.57f, 0.06f, 1.65f, -0.22f, HX, HX, 1.0f, 360.0f, 1.2f).radius(0.6f, 1.4f);
    }

    private static Decal bellDecal(float x) {
        Canvas c = new Canvas(64, 64);
        c.fill(Shape2D.box(32.0f, 30.0f, 30.0f, 2.0f, 1.0f), -5737444);
        c.fill(Shape2D.circle(32.0f, 40.0f, 4.5f), -11915254);
        c.fill(Shape2D.line(32.0f, 40.0f, 32.0f, 60.0f, 3.5f), -11915254);
        c.fill(Shape2D.ellipse(24.0f, 20.0f, 5.0f, 3.5f), -1426063361);
        return Decal.front(c, x, 19.6f, 2.5f, 1.0f, 1.0f).depth(0.6f, 0.6f).maxAngle(70.0f);
    }

    private static Decal laces() {
        Canvas c = new Canvas(96, 128);
        for (int i = 0; i < 4; i++) {
            float y = 30.0f + (i * 22.0f);
            c.fill(Shape2D.line(24.0f, y, 72.0f, y + 14.0f, 6.0f), -1);
            c.fill(Shape2D.line(72.0f, y, 24.0f, y + 14.0f, 6.0f), -1);
            c.fill(Shape2D.circle(20.0f, y, 3.5f), -7710102);
            c.fill(Shape2D.circle(76.0f, y, 3.5f), -7710102);
        }
        c.fill(Shape2D.ellipse(36.0f, 14.0f, 11.0f, 6.0f).outline(4.0f), -1);
        c.fill(Shape2D.ellipse(60.0f, 14.0f, 11.0f, 6.0f).outline(4.0f), -1);
        c.fill(Shape2D.circle(48.0f, 16.0f, 4.0f), -1);
        Vec3 pivot = new Vec3(-1.2f, HX, 0.2f);
        Vec3 at = new Vec3(-1.2f, 1.62f, 0.72f).rotate(HX, -7.0f, HX, pivot);
        Vec3 dir = new Vec3(HX, 0.8f, 0.6f).rotate(HX, -7.0f, HX);
        Vec3 up = new Vec3(HX, 0.6f, -0.8f).rotate(HX, -7.0f, HX);
        return Decal.project(c, at.x(), at.y(), at.z(), dir.x(), dir.y(), dir.z(), up.x(), up.y(), up.z(), 0.95f, 1.25f).depth(0.6f, 0.6f).maxAngle(65.0f);
    }
}
