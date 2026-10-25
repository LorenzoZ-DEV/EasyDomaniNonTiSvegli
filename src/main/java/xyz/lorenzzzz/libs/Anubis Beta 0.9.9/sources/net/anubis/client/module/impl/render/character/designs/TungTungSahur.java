package net.anubis.client.module.impl.render.character.designs;

import net.anubis.client.module.impl.render.character.kit.Bone;
import net.anubis.client.module.impl.render.character.kit.Canvas;
import net.anubis.client.module.impl.render.character.kit.CharacterDesign;
import net.anubis.client.module.impl.render.character.kit.Decal;
import net.anubis.client.module.impl.render.character.kit.Motion;
import net.anubis.client.module.impl.render.character.kit.Noise;
import net.anubis.client.module.impl.render.character.kit.Paint;
import net.anubis.client.module.impl.render.character.kit.Paint2D;
import net.anubis.client.module.impl.render.character.kit.Part;
import net.anubis.client.module.impl.render.character.kit.Rig;
import net.anubis.client.module.impl.render.character.kit.Sample;
import net.anubis.client.module.impl.render.character.kit.Shape;
import net.anubis.client.module.impl.render.character.kit.Shape2D;
import net.anubis.client.module.impl.render.character.kit.Shapes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/designs/TungTungSahur.class */
@Environment(EnvType.CLIENT)
public final class TungTungSahur extends CharacterDesign {
    private static final float R = 5.0f;
    private static final float BOTTOM = 12.0f;
    private static final float TOP = 34.0f;
    private static final float RIM = 0.85f;
    private static final float FIST_X = -7.0f;
    private static final float FIST_Y = 11.6f;
    private static final float FIST_Z = 0.72f;
    private static final float[] KNUCKLE_DIR = unit(-0.16f, 0.86f, 0.48f);
    private static final int WOOD = -1063542;
    private static final int WOOD_DARK = -4158378;
    private static final int WOOD_WORN = -5868993;
    private static final int FISSURE = -9551070;
    private static final int KNOT = -11653867;
    private static final int CUT = -1389672;
    private static final int LIMB = -3567528;
    private static final int LIMB_DARK = -7053254;
    private static final int INK = -15003638;
    private static final int LINE = -12967406;

    public TungTungSahur() {
        super("tung_tung_sahur", "Tung Tung Tung Sahur");
    }

    @Override // net.anubis.client.module.impl.render.character.kit.CharacterDesign
    protected void define(Rig rig) {
        rig.scale(1.0f).outline(LINE, 0.16f).occlusion(0.3f).firstPersonArms(false);
        rig.part(Bone.HEAD).pivot(0.0f, 12.5f, 0.0f);
        Part upright = rig.extra("upright", Bone.HEAD).motion(Motion.none().hang(1.0f).limit(100.0f));
        rig.extra("log", upright).shape(log()).detail(1.5f).resolution(1.2f).flatLight(0.35f).neck(0.0f, 22.5f, 0.0f, 0.0f, 1.0f, 0.0f, 4.0f).look(0.6f, 0.0f, 40.0f).motion(Motion.none().walk(0.0f, 4.0f, -6.0f, 1.0f).drag(6.0f)).decal(eyes()).decal(mouth()).decal(rings());
        Part arm = rig.part(Bone.RIGHT_ARM).pivot(-5.3f, 22.8f, 0.2f).grip(FIST_X, FIST_Y, 0.72f).shape(arm()).joint(-6.57f, 17.1f, 0.14f, 0.6f);
        rig.part(Bone.LEFT_ARM).mirrorOf(arm);
        Part hand = rig.extra("hand_r", Bone.RIGHT_ARM).shape(hand()).resolution(0.6f).detail(0.8f);
        rig.extra("hand_l", Bone.LEFT_ARM).mirrorOf(hand);
        Part leg = rig.part(Bone.RIGHT_LEG).pivot(-2.2f, 12.8f, 0.0f).shape(leg()).swing(0.6f, 35.0f).joint(-2.3f, 6.75f, 0.16f, 0.7f);
        rig.part(Bone.LEFT_LEG).mirrorOf(leg);
        Part foot = rig.extra("foot_r", Bone.RIGHT_LEG).shape(foot()).resolution(0.7f).detail(0.8f);
        rig.extra("foot_l", Bone.LEFT_LEG).mirrorOf(foot);
    }

    private static Shape log() {
        Shape trunk = Shapes.field(TungTungSahur::trunk, -5.5f, 11.8f, -5.5f, 5.5f, 34.2f, 5.5f);
        Paint grain = new Bark(Paint.wood(WOOD, WOOD_DARK, 0.3f, 0.0f, -0.2f, 0.0f, 1.0f, 0.0f, 0.8f).noise(WOOD_WORN, 3.5f, 0.08f).grain(0.35f, 0.04f));
        Shape body = Shapes.blend(0.35f, trunk.paint(grain), knot(38.0f, 17.2f, 0.8f), knot(165.0f, 27.5f, 0.95f), knot(-120.0f, 19.5f, 0.7f), knot(-58.0f, 14.2f, 0.55f), stub(128.0f, 25.2f), eye(1.0f, grain), eye(-1.0f, grain));
        Shape mouth = Shapes.ellipsoid(0.0f, 25.35f, radius(0.0f, 25.35f, 1.0f) + 0.12f, 2.7f, 1.05f, 0.55f);
        return Shapes.subtract(0.12f, Shapes.subtract(0.35f, body, mouth), Shapes.union(hole(165.0f, 27.5f, 0.32f), hole(-120.0f, 19.5f, 0.24f)));
    }

    private static Shape stub(float deg, float y) {
        float sx = (float) Math.sin(Math.toRadians(deg));
        float sz = (float) Math.cos(Math.toRadians(deg));
        float c = (float) Math.cos(Math.toRadians(32.0d));
        float s = (float) Math.sin(Math.toRadians(32.0d));
        float dx = sx * c;
        float dz = sz * c;
        float bx = sx * 3.9f;
        float bz = sz * 3.9f;
        float ex = bx + (dx * 3.05f);
        float ey = y + (s * 3.05f);
        float ez = bz + (dz * 3.05f);
        Shape branch = Shapes.cone(bx, y, bz, 0.98f, ex + (dx * 0.4f), ey + (s * 0.4f), ez + (dz * 0.4f), 0.64f).paint(Paint.wood(WOOD, WOOD_DARK, bx, y, bz, dx, s, dz, 0.3f));
        Paint rings = Paint.radial(-6264264, ex, ey, ez, 0.75f, CUT);
        return Shapes.subtract(branch, Shapes.plane(ex, ey, ez, -dx, -s, -dz).paint(rings));
    }

    private static Shape eye(float side, Paint grain) {
        float x = side * 2.14f;
        float ux = x / 5.0f;
        float uz = (float) Math.sqrt(1.0f - (ux * ux));
        float r = radius(ux, 28.72f, uz) - 0.5f;
        float deg = (float) Math.toDegrees(Math.asin(ux));
        return Shapes.ellipsoid(ux * r, 28.72f, uz * r, 1.85f, 1.8f, RIM).rotate(0.0f, deg, 0.0f, ux * r, 28.72f, uz * r).paint(grain);
    }

    private static Shape knot(float deg, float y, float size) {
        float sx = (float) Math.sin(Math.toRadians(deg));
        float sz = (float) Math.cos(Math.toRadians(deg));
        float tip = radius(sx, y, sz) + 0.28f;
        float c = tip - size;
        return Shapes.ellipsoid(sx * c, y, sz * c, size, size * 1.35f, size).paint(Paint.radial(KNOT, sx * tip, y, sz * tip, size * 1.05f, WOOD_DARK));
    }

    private static Shape hole(float deg, float y, float size) {
        float sx = (float) Math.sin(Math.toRadians(deg));
        float sz = (float) Math.cos(Math.toRadians(deg));
        float tip = radius(sx, y, sz) + 0.28f;
        return Shapes.ellipsoid(sx * tip, y, sz * tip, size, size * 1.3f, size).color(KNOT);
    }

    private static float radius(float ux, float y, float uz) {
        float lump = 0.17f * Noise.noise((ux * 1.1f) + 2.3f, y * 0.1f, (uz * 1.1f) - 1.3f);
        return (5.0f + lump) - (0.17f * fissure(ux, y, uz));
    }

    private static float fissure(float ux, float y, float uz) {
        float n = Math.abs(Noise.noise((ux * 3.0f) - 5.1f, y * 0.08f, (uz * 3.0f) + 0.7f));
        float face = smooth(0.3f, 0.62f, uz) * smooth(21.8f, 23.6f, y);
        return Math.max(0.0f, 1.0f - (n / 0.28f)) * (1.0f - (0.88f * face));
    }

    private static float trunk(float x, float y, float z) {
        float rho = (float) Math.sqrt((x * x) + (z * z));
        float ux = rho > 1.0E-4f ? x / rho : 0.0f;
        float uz = rho > 1.0E-4f ? z / rho : 1.0f;
        float qx = (rho - radius(ux, y, uz)) + RIM;
        float qy = (Math.abs(y - 23.0f) - 11.0f) + RIM;
        float ox = Math.max(qx, 0.0f);
        float oy = Math.max(qy, 0.0f);
        return (((float) Math.sqrt((ox * ox) + (oy * oy))) + Math.min(Math.max(qx, qy), 0.0f)) - RIM;
    }

    private static Shape arm() {
        Paint wood = stick(-5.4f, 22.6f, 0.2f, FIST_X, FIST_Y, 0.72f);
        Shape shoulder = Shapes.blend(0.2f, Shapes.sphere(-5.3f, 22.8f, 0.2f, 0.7f), Shapes.capsule(-5.3f, 22.8f, 0.2f, -3.9f, 22.8f, 0.2f, 0.55f));
        Shape upper = Shapes.cone(-5.4f, 22.5f, 0.2f, 0.64f, -6.55f, 17.3f, 0.12f, 0.54f);
        Shape elbow = Shapes.sphere(-6.57f, 17.1f, 0.14f, 0.6f);
        Shape fore = Shapes.cone(-6.6f, 16.85f, 0.14f, 0.55f, -6.93f, 12.6f, 0.5f, 0.5f);
        return Shapes.blend(0.3f, shoulder, upper, elbow, fore).paint(wood);
    }

    private static Shape hand() {
        float dx = KNUCKLE_DIR[0];
        float dy = KNUCKLE_DIR[1];
        float dz = KNUCKLE_DIR[2];
        Shape palm = Shapes.ellipsoid(-6.92f, 11.650001f, 0.70000005f, 0.86f, 1.05f, 0.9f);
        Shape wrist = Shapes.cone(-6.93f, 12.75f, 0.5f, 0.5f, -6.95f, 12.150001f, 0.72f, 0.64f);
        Shape[] knuckles = new Shape[4];
        for (int i = 0; i < 4; i++) {
            float t = (-0.66f) + (i * 0.44f);
            float cx = (FIST_X + (dx * t)) - 0.28f;
            float cy = FIST_Y + (dy * t);
            float cz = 0.72f + (dz * t) + 0.72f;
            knuckles[i] = Shapes.capsule(cx - 0.35f, cy, cz - 0.3f, cx + 0.3f, cy, cz + 0.1f, 0.26f + (0.02f * (3 - i)));
        }
        Shape thumb = Shapes.capsule(-6.38f, 12.1f, 0.92f, -6.82f, 12.320001f, 1.6700001f, 0.27f);
        return Shapes.blend(0.18f, Shapes.blend(0.3f, palm, wrist), Shapes.union(knuckles), thumb).paint(stick(-6.9f, 12.8f, 0.5f, FIST_X, 10.6f, 0.72f));
    }

    private static Shape leg() {
        Paint wood = stick(-2.2f, 12.8f, 0.0f, -2.35f, 1.4f, 0.0f);
        Shape hip = Shapes.sphere(-2.2f, 12.6f, 0.0f, 0.8f);
        Shape thigh = Shapes.cone(-2.2f, 12.4f, 0.0f, 0.74f, -2.3f, 7.0f, 0.12f, 0.62f);
        Shape knee = Shapes.sphere(-2.3f, 6.75f, 0.16f, 0.68f);
        Shape shin = Shapes.cone(-2.3f, 6.5f, 0.12f, 0.62f, -2.36f, 1.9f, 0.0f, 0.55f);
        return Shapes.blend(0.3f, hip, thigh, knee, shin).paint(wood);
    }

    private static Shape foot() {
        Shape sole = Shapes.blend(0.5f, Shapes.ellipsoid(-2.42f, 0.55f, 1.0f, 1.2f, 0.55f, 1.9f), Shapes.ellipsoid(-2.36f, 0.95f, -0.1f, 0.8f, RIM, 1.0f), Shapes.sphere(-2.36f, 1.75f, 0.0f, 0.55f));
        Shape toes = Shapes.union(Shapes.sphere(-1.7f, 0.38f, 2.55f, 0.4f), Shapes.sphere(-2.2f, 0.35f, 2.8f, 0.36f), Shapes.sphere(-2.68f, 0.34f, 2.76f, 0.33f), Shapes.sphere(-3.12f, 0.33f, 2.52f, 0.31f));
        return Shapes.intersect(Shapes.blend(0.22f, sole, toes), Shapes.above(0.0f)).paint(stick(-2.4f, 0.6f, -1.0f, -2.4f, 0.6f, 3.0f));
    }

    private static Paint stick(float ax, float ay, float az, float bx, float by, float bz) {
        return Paint.wood(LIMB, LIMB_DARK, ax, ay, az, bx - ax, by - ay, bz - az, 0.35f).grain(0.3f, 0.05f);
    }

    private static Decal eyes() {
        Canvas c = new Canvas(336, 208);
        for (int side = -1; side <= 1; side += 2) {
            float ex = 168.0f + (side * 85.5f);
            c.fill(Shape2D.circle(ex, 122.0f, 75.0f), 808458248, 13.0f);
            c.fill(Shape2D.circle(ex, 122.0f, 66.0f), INK);
            c.fill(Shape2D.circle(ex, 122.0f, 60.75f), Paint2D.radial(ex - 15.0f, 122.0f - 18.0f, 78.0f, -1, -132105, -1054240));
            float px = ex - (side * 1.5f);
            float py = 122.0f + 1.0f;
            c.fill(Shape2D.circle(px, py, BOTTOM), -15857658);
            c.fill(Shape2D.circle(px - 3.9f, py - 4.4f, 3.6f), -285212673);
        }
        Shape2D brow = Shape2D.curve(15.0f, 48.0f, 74.0f, 18.0f, 147.0f, 71.0f, 21.0f, 34.5f);
        c.fill(brow, INK);
        c.fill(brow.mirrorX(168.0f).rotate(6.0f, 321.0f, 48.0f).move(0.0f, FIST_X), INK);
        return Decal.front(c, 0.0f, 29.175f, 5.0f, 8.4f, 5.2f).depth(3.6f, 1.5f).maxAngle(72.0f).flatLight(1.0f).occlusion(0.0f);
    }

    private static Decal mouth() {
        Canvas c = new Canvas(272, 104);
        Shape2D mouth = Shape2D.ellipse(136.0f, 19.0f, 124.5f, 78.0f).subtract(Shape2D.circle(136.0f, -482.0f, 507.0f));
        c.fill(mouth.inflate(6.0f), INK);
        c.fill(mouth, Paint2D.linear(136.0f, 10.0f, 136.0f, 95.5f, -15071227, -12970996));
        c.fill(Shape2D.ellipse(136.0f, 98.5f, 75.0f, 39.0f).intersect(mouth), -3646898);
        return Decal.front(c, 0.0f, 24.675f, 5.0f, 6.8f, 2.6f).depth(3.6f, 1.5f).maxAngle(72.0f);
    }

    private static Decal rings() {
        Canvas c = new Canvas(168, 168);
        c.fill(Shape2D.circle(84.0f, 84.0f, 72.0f), Paint2D.radial(81.0f, 86.0f, 72.0f, -3565470, -1917818, -1521774, -2709138), 4.0f);
        Shape2D lines = Shape2D.field((x, y) -> {
            float dx = x - 81.0f;
            float dy = y - 86.0f;
            float rr = ((float) Math.sqrt((dx * dx) + (dy * dy))) + 0.001f;
            float w = rr + (2.5f * Noise.noise((dx / rr) * 1.3f, (dy / rr) * 1.3f, rr * 0.028f));
            float k = w / 8.25f;
            float f = k - Math.round(k);
            return Math.max(Math.max((Math.abs(f) * 8.25f) - 0.82f, rr - 67.5f), 6.7f - rr);
        }, BOTTOM, 17.0f, 150.0f, 155.0f);
        c.fill(lines, -1602927328);
        c.fill(Shape2D.circle(81.0f, 86.0f, 3.6f), -9813984);
        c.fill(Shape2D.taper(87.0f, 81.0f, 3.0f, 133.0f, 52.0f, 1.0f), -1069538288);
        c.fill(Shape2D.taper(77.0f, 91.0f, 2.2f, 47.0f, 126.0f, 1.0f), -1337973744);
        return Decal.top(c, 0.0f, TOP, 0.0f, 9.678f, 9.678f).depth(0.6f, 0.6f).maxAngle(35.0f);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/designs/TungTungSahur$Bark.class */
    @Environment(EnvType.CLIENT)
    private static final class Bark extends Paint {
        private final Paint base;

        Bark(Paint base) {
            this.base = base;
        }

        @Override // net.anubis.client.module.impl.render.character.kit.Paint
        public void apply(float x, float y, float z, Sample out) {
            this.base.apply(x, y, z, out);
            float rho = (float) Math.sqrt((x * x) + (z * z));
            if (rho < 0.5f) {
                return;
            }
            float side = TungTungSahur.smooth(3.95f, 4.7f, rho) * TungTungSahur.smooth(TungTungSahur.BOTTOM, 12.6f, y) * (1.0f - TungTungSahur.smooth(33.4f, TungTungSahur.TOP, y));
            float f = TungTungSahur.fissure(x / rho, y, z / rho) * side;
            if (f > 0.0f) {
                out.toward(TungTungSahur.FISSURE, Math.min(1.0f, f * 0.62f));
            }
        }
    }

    private static float smooth(float a, float b, float v) {
        float t = Math.max(0.0f, Math.min(1.0f, (v - a) / (b - a)));
        return t * t * (3.0f - (2.0f * t));
    }

    private static float[] unit(float x, float y, float z) {
        float l = (float) Math.sqrt((x * x) + (y * y) + (z * z));
        return new float[]{x / l, y / l, z / l};
    }
}
