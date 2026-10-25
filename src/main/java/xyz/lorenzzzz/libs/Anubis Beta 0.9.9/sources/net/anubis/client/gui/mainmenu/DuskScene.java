package net.anubis.client.gui.mainmenu;

import imgui.ImDrawList;
import imgui.ImGui;
import imgui.flag.ImGuiTableFlags;
import java.util.Random;
import net.anubis.client.gui.EgyptDraw;
import net.anubis.client.gui.ThemeImages;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/DuskScene.class */
@Environment(EnvType.CLIENT)
public final class DuskScene {
    public static final float HORIZON = 0.72f;
    private static final String SKY_IMAGE = "/assets/anubis/textures/gui/menu/bg_sky.jpg";
    private static final String SUN_IMAGE = "/assets/anubis/textures/gui/menu/bg_sun.png";
    private static final String MID_IMAGE = "/assets/anubis/textures/gui/menu/bg_mid.png";
    private static final String NEAR_IMAGE = "/assets/anubis/textures/gui/menu/bg_near.png";
    private static final float CANVAS_W = 2880.0f;
    private static final float CANVAS_H = 1620.0f;
    private static final float SAFE_W = 2560.0f;
    private static final float SAFE_H = 1440.0f;
    private static final float CANVAS_HORIZON = 1127.0f;
    private static final float SUN_DISK = 0.3115f;
    private static final float SKY_DRIFT = 4.0f;
    private static final float MID_DRIFT = 14.0f;
    private static final float NEAR_DRIFT = 32.0f;
    private static final int SKY_NIGHT = -16251124;
    private static final int SKY_DUSK = -15069431;
    private static final int SKY_AMBER = -11194353;
    private static final float SKY_TURN = 0.45f;
    private static final int HORIZON_GLOW = -2062038;
    private static final float HORIZON_GLOW_PEAK = 0.2f;
    private static final float HORIZON_GLOW_REACH = 0.55f;
    private static final int GLOW_RINGS = 12;
    private static final float SUN_SPIN = 0.03f;
    private static final float SUN_BREATH = 0.8f;
    private static final int PYRAMID_LIT = -12966896;
    private static final int PYRAMID_SHADE = -15397625;
    private static final int FAR_DUNE = -14412277;
    private static final int NEAR_DUNE = -15594746;
    private static final int DUNE_COLUMNS = 160;
    private static final float VIGNETTE = 0.55f;
    private static final int GLOW_SEGMENTS = 72;
    private static final float[] GLOW_COS = new float[GLOW_SEGMENTS];
    private static final float[] GLOW_SIN = new float[GLOW_SEGMENTS];
    private static final int GRAIN_COUNT = 70;
    private static final float[][] GRAINS = new float[GRAIN_COUNT][];

    static {
        Random random = new Random(686869L);
        for (int i = 0; i < GRAIN_COUNT; i++) {
            GRAINS[i] = new float[]{random.nextFloat(), random.nextFloat(), 0.04f + (random.nextFloat() * 0.09f), SKY_DRIFT + (random.nextFloat() * 14.0f), 0.15f + (random.nextFloat() * 0.35f)};
        }
        for (int i2 = 0; i2 < GLOW_SEGMENTS; i2++) {
            double angle = ((((double) i2) * 3.141592653589793d) * 2.0d) / 72.0d;
            GLOW_COS[i2] = (float) Math.cos(angle);
            GLOW_SIN[i2] = (float) Math.sin(angle);
        }
    }

    private DuskScene() {
    }

    public static float horizon(float height) {
        return height * 0.72f;
    }

    public static void draw(ImDrawList dl, float width, float height, float scale, float time, float horizon, float sunX, float sunY, float sunRadius, float lookX, float lookY) {
        float s = Math.max(width / SAFE_W, height / SAFE_H);
        float x0 = (width - (CANVAS_W * s)) * 0.5f;
        float y0 = horizon - (CANVAS_HORIZON * s);
        if (!layer(dl, SKY_IMAGE, x0, y0, s, SKY_DRIFT, lookX, lookY)) {
            sky(dl, width, height, horizon);
        }
        sun(dl, sunX, sunY, sunRadius, scale, time);
        if (!layer(dl, MID_IMAGE, x0, y0, s, 14.0f, lookX, lookY)) {
            pyramids(dl, width, height, horizon, scale);
        }
        if (!layer(dl, NEAR_IMAGE, x0, y0, s, 32.0f, lookX, lookY)) {
            dunes(dl, width, height, horizon, scale, time);
        }
        sand(dl, width, height, horizon, scale, time);
        vignette(dl, width, height);
    }

    private static boolean layer(ImDrawList dl, String image, float x0, float y0, float s, float drift, float lookX, float lookY) {
        ThemeImages.Texture texture = MenuTextures.original(image);
        if (texture == null) {
            return false;
        }
        float x = x0 - ((lookX * drift) * s);
        float y = y0 - (((lookY * drift) * 0.5f) * s);
        dl.addImage(texture.id(), x, y, x + (CANVAS_W * s), y + (CANVAS_H * s));
        return true;
    }

    private static void sky(ImDrawList dl, float width, float height, float horizon) {
        int night = EgyptDraw.col(SKY_NIGHT, 1.0f);
        int dusk = EgyptDraw.col(SKY_DUSK, 1.0f);
        int amber = EgyptDraw.col(SKY_AMBER, 1.0f);
        float turn = Math.min(height * SKY_TURN, horizon);
        dl.addRectFilledMultiColor(0.0f, 0.0f, width, turn, night, night, dusk, dusk);
        dl.addRectFilledMultiColor(0.0f, turn, width, horizon, dusk, dusk, amber, amber);
        dl.addRectFilled(0.0f, horizon, width, height, EgyptDraw.col(-16777216, 1.0f));
        horizonGlow(dl, width * 0.5f, horizon, width * 0.55f);
    }

    private static void sun(ImDrawList dl, float cx, float cy, float radius, float scale, float time) {
        ThemeImages.Texture texture = MenuTextures.original(SUN_IMAGE);
        if (texture == null) {
            EgyptDraw.sunDisk(dl, cx, cy, radius, time * SUN_SPIN, scale, 1.0f);
            return;
        }
        float half = (radius / SUN_DISK) * 0.5f;
        float glow = 0.965f + (0.035f * ((float) Math.sin(time * SUN_BREATH)));
        dl.addImage(texture.id(), cx - half, cy - half, cx + half, cy + half, 0.0f, 0.0f, 1.0f, 1.0f, EgyptDraw.col(-1, glow));
    }

    private static void pyramids(ImDrawList dl, float width, float height, float horizon, float scale) {
        pyramid(dl, width * 0.17f, horizon, width * 0.34f, height * 0.25f, true, scale);
        pyramid(dl, width * 0.35f, horizon, width * 0.13f, height * 0.09f, true, scale);
        pyramid(dl, width * 0.83f, horizon, width * 0.26f, height * 0.19f, false, scale);
        pyramid(dl, width * 0.96f, horizon, width * 0.14f, height * 0.1f, false, scale);
    }

    private static void dunes(ImDrawList dl, float width, float height, float horizon, float scale, float time) {
        dune(dl, width, height, horizon + (height * 0.015f), 9.0f * scale, 0.0021f / scale, 0.7f, time * 0.02f, FAR_DUNE);
        dune(dl, width, height, horizon + (height * 0.09f), 15.0f * scale, 0.0014f / scale, 2.1f, time * SUN_SPIN, NEAR_DUNE);
    }

    private static void sand(ImDrawList dl, float width, float height, float horizon, float scale, float time) {
        float band = (height - horizon) + (height * 0.08f);
        for (float[] grain : GRAINS) {
            float x = ((width * (1.0f - ((float) (((double) (grain[1] + (time * grain[2]))) % 1.0d)))) * 1.1f) - (width * 0.05f);
            float y = (height - (band * grain[0])) + (((float) Math.sin((time * 1.3f) + (grain[1] * 20.0f))) * 3.0f * scale);
            float len = grain[3] * scale;
            dl.addLine(x, y, x + len, y - (len * 0.08f), EgyptDraw.col(EgyptDraw.SAND, grain[4]), Math.max(1.0f, scale));
        }
    }

    private static void vignette(ImDrawList dl, float width, float height) {
        float edge = Math.min(width, height) * 0.3f;
        int dark = EgyptDraw.col(-16777216, 0.55f);
        int clear = EgyptDraw.col(-16777216, 0.0f);
        dl.addRectFilledMultiColor(0.0f, 0.0f, edge, height, dark, clear, clear, dark);
        dl.addRectFilledMultiColor(width - edge, 0.0f, width, height, clear, dark, dark, clear);
    }

    public static void veil(ImDrawList dl, float width, float height, float amount) {
        dl.addRectFilled(0.0f, 0.0f, width, height, EgyptDraw.col(EgyptDraw.LACQUER, amount));
    }

    private static void pyramid(ImDrawList dl, float cx, float base, float w, float h, boolean litRight, float s) {
        float left = cx - (w * 0.5f);
        float right = cx + (w * 0.5f);
        float apexX = cx + (w * 0.06f * (litRight ? 1 : -1));
        float apexY = base - h;
        int lit = EgyptDraw.col(PYRAMID_LIT, 1.0f);
        int shade = EgyptDraw.col(PYRAMID_SHADE, 1.0f);
        dl.addTriangleFilled(left, base, apexX, apexY, cx, base, litRight ? shade : lit);
        dl.addTriangleFilled(cx, base, apexX, apexY, right, base, litRight ? lit : shade);
        float edgeX = litRight ? right : left;
        dl.addLine(apexX, apexY, edgeX, base, EgyptDraw.col(EgyptDraw.GOLD, 0.35f), Math.max(1.0f, 1.2f * s));
        dl.addLine(apexX, apexY, cx, base, EgyptDraw.col(EgyptDraw.GOLD, 0.12f), Math.max(1.0f, s));
    }

    private static void horizonGlow(ImDrawList dl, float cx, float cy, float radius) {
        float u = ImGui.getFontTexUvWhitePixelX();
        float v = ImGui.getFontTexUvWhitePixelY();
        dl.primReserve((GLOW_SEGMENTS * 3) + (11 * GLOW_SEGMENTS * 6), 1 + (12 * GLOW_SEGMENTS));
        int centre = dl.getVtxCurrentIdx();
        dl.primWriteVtx(cx, cy, u, v, EgyptDraw.col(HORIZON_GLOW, HORIZON_GLOW_PEAK));
        for (int ring = 1; ring <= 12; ring++) {
            float f = ring / 12.0f;
            float left = 1.0f - f;
            int color = EgyptDraw.col(HORIZON_GLOW, HORIZON_GLOW_PEAK * left * left * (3.0f - (2.0f * left)));
            for (int i = 0; i < GLOW_SEGMENTS; i++) {
                dl.primWriteVtx(cx + (GLOW_COS[i] * radius * f), cy + (GLOW_SIN[i] * radius * f), u, v, color);
            }
        }
        for (int i2 = 0; i2 < GLOW_SEGMENTS; i2++) {
            int j = (i2 + 1) % GLOW_SEGMENTS;
            dl.primWriteIdx(centre);
            dl.primWriteIdx(centre + 1 + i2);
            dl.primWriteIdx(centre + 1 + j);
            for (int ring2 = 1; ring2 < 12; ring2++) {
                int inner = centre + 1 + ((ring2 - 1) * GLOW_SEGMENTS);
                int outer = inner + GLOW_SEGMENTS;
                quad(dl, inner + i2, outer + i2, outer + j, inner + j);
            }
        }
    }

    private static void dune(ImDrawList dl, float width, float height, float y, float amp, float freq, float phase, float drift, int argb) {
        float u = ImGui.getFontTexUvWhitePixelX();
        float v = ImGui.getFontTexUvWhitePixelY();
        int body = EgyptDraw.col(argb, 1.0f);
        int clear = EgyptDraw.col(argb, 0.0f);
        dl.primReserve(ImGuiTableFlags.Borders, 483);
        int first = dl.getVtxCurrentIdx();
        for (int i = 0; i <= 160; i++) {
            float x = (width * i) / 160.0f;
            double w = (x * freq) + phase + drift;
            float ridge = y - ((float) ((Math.sin(w) * ((double) amp)) + ((Math.sin((w * 2.3d) + 1.7d) * ((double) amp)) * 0.45d)));
            dl.primWriteVtx(x, ridge - 0.5f, u, v, clear);
            dl.primWriteVtx(x, ridge + 0.5f, u, v, body);
            dl.primWriteVtx(x, height, u, v, body);
        }
        for (int i2 = 0; i2 < 160; i2++) {
            int a = first + (i2 * 3);
            int b = a + 3;
            quad(dl, a, b, b + 1, a + 1);
            quad(dl, a + 1, b + 1, b + 2, a + 2);
        }
    }

    private static void quad(ImDrawList dl, int a, int b, int c, int d) {
        dl.primWriteIdx(a);
        dl.primWriteIdx(b);
        dl.primWriteIdx(c);
        dl.primWriteIdx(a);
        dl.primWriteIdx(c);
        dl.primWriteIdx(d);
    }
}
