package net.anubis.client.module.impl.render.character.designs;

import imgui.flag.ImDrawFlags;
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
import net.anubis.client.module.impl.render.character.kit.Vec3;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/designs/Tralalero.class */
@Environment(EnvType.CLIENT)
public final class Tralalero extends CharacterDesign {
    private static final float YJ = 17.9f;
    private static final float PIVOT_Z = -1.5f;
    private static final float HIP = 12.0f;
    private static final float LEG_Z = 3.4f;
    private static final float KNEE = 6.4f;
    private static final float MID_HIP = 14.2f;
    private static final float MID_Z = -7.0f;
    private static final float MOUTH_DEG = 152.0f;
    private static final int MOUTH_W = 284;
    private static final int MOUTH_H = 120;
    private static final float MOUTH_PX = 25.0f;
    private static final float MOUTH_Z = 9.45f;
    private static final float CORNER = 74.0f;
    private static final float LIP_FRONT = 11.0f;
    private static final float LIP_BACK = 7.4f;
    private static final int BACK_TOP = -12621165;
    private static final int BACK = -10515274;
    private static final int BELLY = -920330;
    private static final int FIN_TIP = -13346176;
    private static final int SKIN = -10055494;
    private static final int SKIN_LIGHT = -8674358;
    private static final int SKIN_DARK = -11634525;
    private static final int SHOE = -13933608;
    private static final int SHOE_DARK = -14989656;
    private static final int SHOE_LIGHT = -9593609;
    private static final int SOLE = -657935;
    private static final int SOLE_GREY = -4604218;
    private static final int LINE = -15194566;
    private static final int MOUTH = -12972264;
    private static final int GUM = -10872282;
    private static final int TEETH = -263436;
    private static final float LEG_X = 2.9f;
    private static final float[] BODY_Z = {-12.4f, -10.7f, -8.4f, -5.7f, -2.9f, 0.0f, LEG_X, 5.8f, 8.3f, 10.3f, 11.8f, 12.8f, 13.4f};
    private static final float SHOE_SCALE = 1.15f;
    private static final float SHIFT = 0.8f;
    private static final float[] BODY_Y = {1.4f, SHOE_SCALE, SHIFT, 0.4f, 0.1f, 0.0f, 0.05f, 0.25f, 0.5f, 0.75f, 0.95f, 1.05f, 1.1f};
    private static final float[] BODY_RY = {1.0f, 2.1f, 3.3f, 4.6f, 5.5f, 5.85f, 5.75f, 5.2f, 4.35f, 3.35f, 2.3f, 1.35f, 0.45f};
    private static final float[] BODY_RX = {0.9f, 1.8f, LEG_X, 4.2f, 5.1f, 5.45f, 5.35f, 4.8f, 3.95f, 3.0f, 2.1f, 1.25f, 0.45f};
    private static final float[] LINE_Z = {-14.5f, -11.0f, -8.0f, -5.0f, -2.0f, 1.0f, 3.5f, 5.5f, 7.0f, 9.0f, 11.0f, 14.5f};
    private static final float[] LINE_Y = {19.699999f, 19.1f, 18.1f, 17.1f, 16.4f, 16.3f, 16.9f, 17.65f, 17.4f, 17.6f, 18.1f, 18.4f};

    public Tralalero() {
        super("tralalero", "Tralalero Tralala");
    }

    @Override // net.anubis.client.module.impl.render.character.kit.CharacterDesign
    protected void define(Rig rig) {
        rig.scale(1.0f).outline(LINE, 0.16f).occlusion(0.5f).firstPersonArms(false);
        rig.part(Bone.BODY).pivot(0.0f, 15.9f, 0.0f);
        Shape trunk = Shapes.field(Tralalero::loft, -5.8f, 11.7f, -14.6f, 5.8f, 24.099998f, MID_HIP);
        Shape above = Shapes.field((x, y, z) -> {
            return bellyLine(z) - y;
        }, -6.0f, 10.9f, -14.0f, 6.0f, 24.9f, 15.0f);
        Shape body = Shapes.blend(1.1f, Shapes.intersect(0.5f, trunk, above).paint(Paint.field(Tralalero::skin)), dorsal()).move(0.0f, 0.0f, SHIFT);
        Canvas eyeArt = eyeCanvas();
        Canvas gillArt = gillCanvas();
        Canvas laceArt = laceCanvas();
        Canvas stripeArt = stripeCanvas();
        Part shark = rig.extra("shark", Bone.BODY).pivot(0.0f, YJ, PIVOT_Z).shape(body).resolution(1.2f).detail(0.78f).outline(0.1f).neck(0.0f, YJ, 6.8f, 0.0f, 0.0f, 1.0f, 3.0f).look(0.45f, 0.45f, 28.0f).motion(Motion.none().idle(0.7f, SHIFT, 0.0f, 0.28f).walk(2.0f, 0.0f, 4.0f, 1.0f).follow(Bone.HEAD, 0.25f).spring(70.0f, 10.0f).limit(HIP)).decal(eye(eyeArt, 1.0f)).decal(eye(eyeArt.mirrored(), -1.0f)).decal(gills(gillArt, 1.0f)).decal(gills(gillArt.mirrored(), -1.0f));
        Shape below = Shapes.field((x2, y2, z2) -> {
            return (y2 - bellyLine(z2)) - 0.4f;
        }, -6.0f, 10.9f, -14.0f, 6.0f, 24.9f, 15.0f);
        Shape shell = Shapes.intersect(trunk.inflate(-0.15f), below).paint(Paint.of(BELLY).flatLight(0.4f).occlusion(0.35f)).move(0.0f, 0.0f, SHIFT);
        rig.extra("belly", shark).shape(shell).resolution(1.0f).detail(1.0f).decal(mouth());
        rig.extra("tail", shark).pivot(0.0f, 19.199999f, -9.9f).shape(caudal()).resolution(1.0f).motion(Motion.tail().idle(1.0f, 9.0f, 0.0f, 0.45f).walk(2.0f, 14.0f, 0.0f, 1.0f).drag(5.0f).hang(0.3f).turn(0.0f, 16.0f).spring(40.0f, 5.5f).bend(1.2f));
        Part finR = rig.part(Bone.RIGHT_ARM).pivot(-4.3f, 15.4f, 4.0f).grip(-6.8f, 13.4f, 2.7f).shape(pectoral()).resolution(0.9f).detail(SHIFT).swing(0.35f);
        rig.part(Bone.LEFT_ARM).mirrorOf(finR);
        Part legR = rig.part(Bone.RIGHT_LEG).pivot(-2.9f, HIP, LEG_Z).shape(leg(-2.9f, HIP, LEG_Z, -1.0f)).resolution(1.0f).detail(1.4f).swing(0.5f, 35.0f).joint(-2.8000002f, 6.5f, 3.5500002f, SHIFT);
        rig.part(Bone.LEFT_LEG).mirrorOf(legR);
        Part shoeR = rig.extra("shoe_r", Bone.RIGHT_LEG).shape(shoe(-2.9f, LEG_Z)).resolution(1.0f).detail(1.6f).decal(laces(laceArt, -2.9f, LEG_Z)).decal(stripe(stripeArt, -2.9f, LEG_Z, -1.0f));
        rig.extra("shoe_l", Bone.LEFT_LEG).mirrorOf(shoeR);
        Part mid = rig.extra("leg_mid", Bone.BODY).pivot(0.0f, MID_HIP, MID_Z).shape(leg(0.0f, MID_HIP, MID_Z, 1.0f)).resolution(1.0f).detail(1.4f).motion(Motion.none().follow(Bone.LEFT_LEG, 0.5f).walk(22.0f, 0.0f, 0.0f, 1.0f).hang(0.5f).limit(45.0f));
        rig.extra("shoe_mid", mid).shape(shoe(0.0f, MID_Z)).resolution(1.0f).detail(1.6f).decal(laces(laceArt, 0.0f, MID_Z)).decal(stripe(stripeArt, 0.0f, MID_Z, -1.0f)).decal(stripe(stripeArt.mirrored(), 0.0f, MID_Z, 1.0f));
    }

    private static float smooth(float e0, float e1, float v) {
        float t = (v - e0) / (e1 - e0);
        float t2 = t < 0.0f ? 0.0f : Math.min(t, 1.0f);
        return t2 * t2 * (3.0f - (2.0f * t2));
    }

    private static int segment(float[] zs, float z) {
        int i = 0;
        while (i < zs.length - 2 && z > zs[i + 1]) {
            i++;
        }
        return i;
    }

    private static float spline(float[] zs, float[] v, int i, float z, boolean slope) {
        int n = zs.length;
        float h = zs[i + 1] - zs[i];
        float t = Math.max(0.0f, Math.min(1.0f, (z - zs[i]) / h));
        float p0 = v[Math.max(0, i - 1)];
        float p1 = v[i];
        float p2 = v[i + 1];
        float p3 = v[Math.min(n - 1, i + 2)];
        float a = (((2.0f * p0) - (5.0f * p1)) + (4.0f * p2)) - p3;
        float b = (((3.0f * p1) - p0) - (3.0f * p2)) + p3;
        return slope ? (0.5f * (((p2 - p0) + ((2.0f * a) * t)) + (((3.0f * b) * t) * t))) / h : 0.5f * ((2.0f * p1) + ((p2 - p0) * t) + (a * t * t) + (b * t * t * t));
    }

    private static float bellyLine(float z) {
        return spline(LINE_Z, LINE_Y, segment(LINE_Z, z), z, false);
    }

    private static float loft(float x, float y, float z) {
        int n = BODY_Z.length;
        if (z > BODY_Z[n - 1]) {
            float dy = (y - YJ) - BODY_Y[n - 1];
            float dz = z - BODY_Z[n - 1];
            return ((float) Math.sqrt(((x * x) + (dy * dy)) + (dz * dz))) - BODY_RY[n - 1];
        }
        if (z < BODY_Z[0]) {
            float dy2 = (y - YJ) - BODY_Y[0];
            float dz2 = z - BODY_Z[0];
            return ((float) Math.sqrt(((x * x) + (dy2 * dy2)) + (dz2 * dz2))) - BODY_RX[0];
        }
        int i = segment(BODY_Z, z);
        float cy = YJ + spline(BODY_Z, BODY_Y, i, z, false);
        float ry = spline(BODY_Z, BODY_RY, i, z, false);
        float rx = spline(BODY_Z, BODY_RX, i, z, false);
        float sy = spline(BODY_Z, BODY_RY, i, z, true);
        float sx = spline(BODY_Z, BODY_RX, i, z, true);
        float sc = spline(BODY_Z, BODY_Y, i, z, true);
        float s = Math.max(Math.abs(sx), Math.abs(sy)) + Math.abs(sc);
        float px = x / rx;
        float py = (y - cy) / ry;
        float k0 = (float) Math.sqrt((px * px) + (py * py));
        float k1 = (float) Math.sqrt(((px * px) / (rx * rx)) + ((py * py) / (ry * ry)));
        float d = k1 < 1.0E-6f ? -Math.min(rx, ry) : (k0 * (k0 - 1.0f)) / k1;
        return d / ((float) Math.sqrt(1.0f + (s * s)));
    }

    private static int skin(float x, float y, float z) {
        return Colors.mix(BACK, BACK_TOP, smooth(bellyLine(z) + 0.5f, 23.4f, y));
    }

    private static Shape2D outline(float dy, float... xy) {
        float[] p = (float[]) xy.clone();
        for (int i = 1; i < p.length; i += 2) {
            int i2 = i;
            p[i2] = p[i2] + dy;
        }
        return Shape2D.polygon(p);
    }

    private static Shape blade(Shape2D profile, float cx, float cy, float w0, float s) {
        Shape slab = Shapes.extrude(profile, w0, w0);
        float n = (float) Math.sqrt(1.0f + (s * s));
        return Shapes.field((x, y, z) -> {
            float r = (float) Math.sqrt(((x - cx) * (x - cx)) + ((y - cy) * (y - cy)));
            return Math.max(slab.distance(x, y, z), ((Math.abs(z) - w0) + (s * r)) / n);
        }, profile.minX(), profile.minY(), -w0, profile.maxX(), profile.maxY(), w0);
    }

    private static Shape side(Shape s) {
        return orient(s, 0.0f, 0.0f, 0.0f, new Vec3(0.0f, 0.0f, -1.0f), Vec3.UP);
    }

    private static Shape dorsal() {
        Shape2D first = outline(YJ, -2.6f, 4.2f, -1.9f, 6.2f, -1.0f, 7.9f, 0.1f, 9.4f, 1.4f, 10.6f, 2.6f, 11.5f, 3.7f, 12.1f, 3.2f, 11.2f, 2.75f, 10.0f, 2.45f, 8.7f, 2.4f, LIP_BACK, 2.6f, 6.2f, 3.1f, 5.0f, 3.6f, 4.2f);
        Shape2D second = outline(YJ, 8.3f, 2.2f, 8.9f, 3.6f, 9.7f, 4.7f, 10.6f, 5.5f, 10.2f, 4.5f, 10.1f, LEG_Z, 10.4f, 2.2f);
        Shape fins = Shapes.union(side(blade(first.inflate(0.1f), 0.4f, 22.5f, 1.5f, 0.125f)), side(blade(second, 9.4f, 20.699999f, 0.72f, 0.14f)));
        return fins.paint(Paint.vertical(BACK_TOP, 22.9f, FIN_TIP, 29.4f));
    }

    private static Shape caudal() {
        Shape2D lobes = outline(YJ, HIP, 1.4f, 11.7f, 2.2f, 12.4f, 3.8f, 13.2f, 5.4f, 14.0f, 7.0f, 14.8f, 8.3f, 15.4f, 9.1f, 15.0f, 7.9f, 14.6f, 6.3f, 14.3f, 4.6f, 14.1f, 3.0f, MID_HIP, 1.6f, 14.4f, 0.2f, 14.7f, -1.3f, 14.95f, -2.7f, 15.2f, -3.9f, 14.5f, -3.2f, 13.6f, -2.2f, 12.8f, -1.0f, 11.9f, 0.4f);
        Shape fin = side(blade(lobes.inflate(0.08f), HIP, 19.3f, 0.82f, 0.062f));
        Shape stem = Shapes.cone(0.0f, 19.1f, -10.3f, 1.5f, 0.0f, 19.3f, -12.5f, 1.0f);
        Shape keel = Shapes.ellipsoid(0.0f, 19.3f, -12.2f, 1.3f, 0.55f, 1.4f);
        return Shapes.blend(0.7f, stem, keel, fin).paint(Paint.radial(BACK, 0.0f, 19.3f, -12.2f, 5.5f, FIN_TIP)).move(0.0f, 0.0f, SHIFT);
    }

    private static Shape pectoral() {
        Shape2D profile = Shape2D.polygon(-1.2f, 2.0f, 0.5f, 2.1f, 2.0f, 1.7f, 3.5f, 1.0f, 5.0f, 0.1f, 6.3f, -0.9f, 7.0f, -1.6f, 6.0f, PIVOT_Z, 4.6f, -1.35f, 3.2f, -1.3f, 1.8f, PIVOT_Z, 0.4f, -1.9f, -1.2f, -2.2f);
        Shape fin = orient(blade(profile, 0.0f, 0.0f, 0.62f, 0.05f), -4.3f, 15.4f, 3.2f, new Vec3(-0.8f, -0.5f, -0.33f), new Vec3(0.25f, 0.05f, 1.0f));
        return fin.paint(Paint.radial(BACK, -4.3f, 15.4f, 3.2f, 6.0f, FIN_TIP)).move(0.0f, 0.0f, SHIFT);
    }

    private static Paint legPaint(float z0) {
        return Paint.field((x, y, z) -> {
            float front = smooth(z0 + 0.3f, z0 + 1.2f, z);
            float pit = smooth(1.3f, 0.3f, Math.abs(y - KNEE)) * smooth(z0 - 0.2f, z0 - SHIFT, z);
            return Colors.mix(Colors.mix(SKIN, SKIN_LIGHT, front), SKIN_DARK, pit);
        });
    }

    private static Shape leg(float x, float hy, float z, float lat) {
        float t = hy - KNEE;
        Shape root = Shapes.capsule(x, hy, z, x, hy + 2.8f, z, 1.85f);
        Shape thigh = Shapes.cone(x, hy + 0.3f, z, 2.0f, x - (lat * 0.15f), KNEE + 0.9f, z + 0.15f, 1.12f);
        Shape quad = Shapes.ellipsoid(x + (lat * 0.05f), KNEE + (0.56f * t), z + 0.85f, 1.18f, 0.42f * t, 1.08f);
        Shape outer = Shapes.ellipsoid(x + (lat * 0.7f), KNEE + (0.55f * t), z, 0.85f, 0.45f * t, 1.05f);
        Shape inner = Shapes.ellipsoid(x - (lat * 0.6f), KNEE + 1.35f, z + 0.55f, SHIFT, 1.0f, 0.78f);
        Shape hams = Shapes.ellipsoid(x, KNEE + (0.55f * t), z - 0.7f, 1.2f, 0.45f * t, 0.95f);
        Shape knee = Shapes.sphere(x - (lat * 0.1f), KNEE + 0.1f, z + 0.15f, 0.9f);
        Shape cap = Shapes.ellipsoid(x - (lat * 0.05f), KNEE + 0.25f, z + 0.95f, 0.55f, 0.62f, 0.4f);
        Shape shin = Shapes.cone(x - (lat * 0.1f), KNEE - 0.3f, z + 0.25f, 0.86f, x, 2.4f, z + 0.05f, 0.56f);
        Shape calfIn = Shapes.ellipsoid(x - (lat * 0.36f), KNEE - 1.55f, z - 0.62f, 0.84f, 1.55f, 0.98f);
        Shape calfOut = Shapes.ellipsoid(x + (lat * 0.36f), KNEE - 1.25f, z - 0.55f, 0.76f, 1.35f, 0.88f);
        Shape heel = Shapes.cone(x, KNEE - 2.7f, z - 0.55f, 0.5f, x, 2.2f, z - 0.5f, 0.42f);
        Shape ankle = Shapes.sphere(x, 2.3f, z, 0.58f);
        Shape limb = Shapes.union(root, Shapes.blend(0.3f, thigh, quad, outer, inner, hams, knee, cap, shin, calfIn, calfOut, heel, ankle)).paint(legPaint(z));
        Shape sole = flat(foot(0.0f), x, 0.6f, z, 0.6f, 0.52f).paint(Paint.vertical(SOLE_GREY, 0.2f, SOLE, 0.3f));
        return Shapes.union(limb, sole.scale(SHOE_SCALE, x, 0.0f, z));
    }

    private static Shape orient(Shape s, float ox, float oy, float oz, Vec3 xAxis, Vec3 yHint) {
        Vec3 x = xAxis.normalize();
        Vec3 z = x.cross(yHint).normalize();
        Vec3 y = z.cross(x);
        float angle = (float) Math.acos(Math.max(-1.0f, Math.min(1.0f, (((x.x() + y.y()) + z.z()) - 1.0f) * 0.5f)));
        Shape r = angle < 1.0E-4f ? s : s.rotateAxis(y.z() - z.y(), z.x() - x.z(), x.y() - y.x(), (float) Math.toDegrees(angle), 0.0f, 0.0f, 0.0f);
        return r.move(ox, oy, oz);
    }

    private static Shape flat(Shape2D profile, float x, float y, float z, float half, float round) {
        return orient(Shapes.extrude(profile, half, round), x, y, z, Vec3.LEFT, Vec3.FORWARD);
    }

    private static Shape2D foot(float inset) {
        Shape2D heel = Shape2D.circle(0.0f, -1.05f, 1.0f);
        Shape2D ball = Shape2D.circle(0.0f, 1.4f, 1.22f);
        Shape2D toe = Shape2D.circle(0.0f, 2.35f, 1.05f);
        Shape2D outline = Shape2D.blend(2.2f, heel, ball, toe);
        return inset > 0.0f ? outline.inflate(-inset) : outline;
    }

    private static Shape shoe(float x, float z) {
        Shape round = Shapes.blend(SHIFT, Shapes.cone(x, 1.55f, z - 0.85f, 1.3f, x, 1.25f, z + 1.1f, 1.3f), Shapes.ellipsoid(x, 0.95f, z + 1.95f, 1.22f, 0.92f, 1.5f));
        Paint upperPaint = Paint.field((px, py, pz) -> {
            return Colors.mix(SHOE, SHOE_DARK, smooth(z - 0.6f, z - 1.3f, pz));
        });
        Shape upper = Shapes.intersect(0.15f, round, flat(foot(0.1f), x, 2.0f, z, 1.3f, 0.25f)).paint(upperPaint);
        Shape collar = Shapes.torus(x, 2.95f, z - 0.5f, 0.78f, 0.34f).rotate(-14.0f, 0.0f, 0.0f, x, 2.95f, z - 0.5f).color(SHOE_DARK);
        Shape tongue = Shapes.ellipsoid(x, LEG_X, z + 0.35f, 0.52f, 0.4f, 0.7f).rotate(20.0f, 0.0f, 0.0f, x, LEG_X, z + 0.35f).color(SHOE_LIGHT);
        Shape tab = Shapes.ellipsoid(x, 3.15f, z - 1.25f, 0.26f, 0.45f, 0.2f).color(SHOE_DARK);
        return Shapes.blend(0.2f, upper, collar, tongue, tab).scale(SHOE_SCALE, x, 0.0f, z);
    }

    private static Canvas laceCanvas() {
        Canvas c = new Canvas(60, 76);
        for (int i = 0; i < 4; i++) {
            float y = 6.5f + (i * 20.25f);
            float w = 22.5f - (i * 1.9f);
            c.fill(Shape2D.box(30.0f, y, w, 4.1f, 4.1f), -1);
            c.fill(Shape2D.circle((30.0f - w) - 2.3f, y, 2.4f), -14274500);
            c.fill(Shape2D.circle(30.0f + w + 2.3f, y, 2.4f), -14274500);
        }
        return c;
    }

    private static Canvas stripeCanvas() {
        Canvas c = new Canvas(ImDrawFlags.RoundCornersRight, 30);
        c.fill(Shape2D.line(20.0f, 9.0f, 140.0f, 21.0f, 13.0f), -1);
        return c;
    }

    private static Decal stripe(Canvas c, float x, float z, float side) {
        return Decal.project(c, x + (side * 1.3f * SHOE_SCALE), 1.55f * SHOE_SCALE, z + (0.35f * SHOE_SCALE), side, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 3.6f * SHOE_SCALE, 0.68f * SHOE_SCALE).depth(0.9f, 0.9f).maxAngle(70.0f).flatLight(0.6f).occlusion(0.4f);
    }

    private static Decal laces(Canvas c, float x, float z) {
        return Decal.project(c, x, 2.7f * SHOE_SCALE, z + (1.3f * SHOE_SCALE), 0.0f, 0.79f, 0.61f, 0.0f, 1.0f, 0.0f, 1.25f * SHOE_SCALE, 1.58f * SHOE_SCALE).depth(0.9f, 0.9f).flatLight(0.6f).occlusion(0.4f);
    }

    private static Canvas eyeCanvas() {
        Canvas c = new Canvas(96, 96);
        Shape2D ball = Shape2D.ellipse(48.0f, 50.0f, 21.0f, 20.0f);
        c.fill(ball.inflate(4.0f), 1428832344, 3.0f);
        c.fill(ball, Paint2D.radial(48.0f - 4.5f, 50.0f - 6.0f, 26.0f, -14011322, -16316147));
        c.fill(Shape2D.circle(48.0f - 7.5f, 50.0f - 7.0f, 5.6f), -1);
        c.fill(Shape2D.circle(48.0f + 7.5f, 50.0f + 8.0f, 2.4f), -1325400065);
        c.fill(Shape2D.arc(48.0f, 50.0f + 2.0f, 22.0f, 200.0f, 340.0f, 4.0f), -2144713630, 2.0f);
        return c;
    }

    private static Decal eye(Canvas c, float s) {
        return Decal.project(c, s * 3.3f, 20.1f, 10.2f, s * 0.84f, 0.36f, 0.4f, 0.0f, 1.0f, 0.0f, 2.2f, 2.2f).depth(1.2f, 1.2f).maxAngle(75.0f).flatLight(0.7f);
    }

    private static Canvas gillCanvas() {
        Canvas c = new Canvas(120, 120);
        for (int i = 0; i < 5; i++) {
            float gx = 16.0f + (i * 22.0f);
            float len = 42.0f - (i * 3.0f);
            c.fill(Shape2D.curve(gx, 58.0f - len, gx + 7.0f, 58.0f, gx, 58.0f + len, LEG_Z, LEG_Z), -802799780);
        }
        return c;
    }

    private static Decal gills(Canvas c, float s) {
        return Decal.project(c, s * 5.1f, 18.5f, 6.0f, s, 0.05f, 0.2f, 0.0f, 1.0f, 0.0f, 2.8f, 2.8f).depth(1.5f, 1.5f);
    }

    private static float mouthX(float a) {
        return 142.0f + ((a * 284.0f) / MOUTH_DEG);
    }

    private static float arch(float a) {
        float k = Math.min(1.0f, Math.abs(a) / CORNER);
        float z = 11.0f - (3.6f * ((float) Math.pow(k, 1.8d)));
        return 60.0f - ((z - MOUTH_Z) * MOUTH_PX);
    }

    private static float gape(float a) {
        float k = Math.min(1.0f, Math.abs(a) / CORNER);
        return 15.000001f * (1.0f - (k * k));
    }

    private static Decal mouth() {
        Canvas c = new Canvas(MOUTH_W, 120);
        float[] mid = new float[MOUTH_W];
        float[] half = new float[MOUTH_W];
        float[] norm = new float[MOUTH_W];
        for (int i = 0; i < MOUTH_W; i++) {
            float a = ((i + 0.5f) - 142.0f) * 0.53521127f;
            float slope = (arch(a + 0.5f) - arch(a - 0.5f)) * 0.53521127f;
            mid[i] = arch(a);
            half[i] = Math.abs(a) > 75.0f ? -1.0f : gape(a);
            norm[i] = 1.0f / ((float) Math.sqrt(1.0f + (slope * slope)));
        }
        Shape2D open = Shape2D.field((px, py) -> {
            int i2 = Math.max(0, Math.min(283, (int) px));
            if (half[i2] < 0.0f) {
                return 4.0f;
            }
            return (Math.abs(py - mid[i2]) - half[i2]) * norm[i2];
        }, mouthX(-76.0f), 3.0f, mouthX(76.0f), 117.0f);
        c.fill(open, Paint2D.linear(0.0f, 0.0f, 0.0f, 120.0f, GUM, MOUTH));
        for (int i2 = -4; i2 <= 4; i2++) {
            float a2 = i2 * 15.0f;
            float hh = gape(a2);
            if (hh >= 2.5f) {
                Shape2D top = Shape2D.polygon(mouthX(a2 - 6.0f), (arch(a2 - 6.0f) - gape(a2 - 6.0f)) - 0.75f, mouthX(a2 + 6.0f), (arch(a2 + 6.0f) - gape(a2 + 6.0f)) - 0.75f, mouthX(a2), arch(a2) + (hh * 0.5f));
                c.fill(top.intersect(open), TEETH);
                float b = a2 + 7.5f;
                float bh = gape(b);
                if (bh >= 2.5f && i2 != 4) {
                    Shape2D bottom = Shape2D.polygon(mouthX(b - 5.0f), arch(b - 5.0f) + gape(b - 5.0f) + 0.75f, mouthX(b + 5.0f), arch(b + 5.0f) + gape(b + 5.0f) + 0.75f, mouthX(b), arch(b) - (bh * 0.35f));
                    c.fill(bottom.intersect(open), -1185056);
                }
            }
        }
        c.fill(open.outline(2.5f), LINE);
        return Decal.wrap(c, 0.0f, 18.35f, 10.25f, 0.0f, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f, MOUTH_DEG, 4.8f).maxAngle(80.0f).flatLight(1.0f).occlusion(0.3f);
    }
}
