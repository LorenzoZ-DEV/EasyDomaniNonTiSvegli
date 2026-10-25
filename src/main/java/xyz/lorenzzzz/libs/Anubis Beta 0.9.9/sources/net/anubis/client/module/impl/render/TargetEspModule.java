package net.anubis.client.module.impl.render;

import imgui.extension.imguizmo.flag.Operation;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.util.combat.CombatTarget;
import net.anubis.client.util.glyph.GlyphCanvas;
import net.anubis.client.util.glyph.GlyphShapes;
import net.anubis.client.util.glyph.GlyphTint;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1309;
import net.minecraft.class_1531;
import net.minecraft.class_1657;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_5819;
import net.minecraft.class_638;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/TargetEspModule.class */
@Environment(EnvType.CLIENT)
public final class TargetEspModule extends ClientModule {
    private static final String STYLE_ANKH = "Ankh Circle";
    private static final String STYLE_HORUS = "Horus Halo";
    private static final String STYLE_VORTEX = "Sand Vortex";
    private static final String STYLE_CAPSTONE = "Capstone";
    private static final float APPEAR_SECONDS = 0.3f;
    private static final float VANISH_SECONDS = 0.38f;
    private static final float HURT_TICKS = 10.0f;
    private static final double MAX_DISTANCE_SQR = 16384.0d;
    private static final int GLYPH_MARKS = 6;
    private static final int CARTOUCHE_DASHES = 12;
    private static final int VORTEX_GRAINS = 40;
    private static final int VORTEX_POSTS = 3;
    private static final float VORTEX_FALL = 0.48f;
    private static final float VORTEX_SPIN = 0.8f;
    private static final float VORTEX_SWIRL = 3.1f;
    private static final float VORTEX_RIPPLE = 0.96f;
    private static final float[] GRAIN_SEEDS = scatter(Operation.ROTATE, 1515036955);
    private static final double BLINK_PERIOD = 4.6d;
    private static final double BLINK_LENGTH = 0.22d;
    private final ModeSetting style;
    private final ModeSetting colorMode;
    private final ColorSetting mainColor;
    private final ColorSetting secondColor;
    private final ColorSetting hitColor;
    private final NumberSetting scale;
    private final NumberSetting tempo;
    private final NumberSetting opacity;
    private final NumberSetting memory;
    private final BooleanSetting crosshair;
    private final BooleanSetting playersOnly;
    private final BooleanSetting hurtFlash;
    private final BooleanSetting throughWalls;
    private final GlyphCanvas canvas;
    private final GlyphTint palette;
    private final double[] capX;
    private final double[] capZ;
    private Mark current;
    private Mark previous;
    private class_638 boundLevel;
    private long lastFrame;
    private double phase;
    private int gold;
    private int lapis;
    private int light;
    private int hit;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/TargetEspModule$Mark.class */
    @Environment(EnvType.CLIENT)
    private static final class Mark {
        class_1309 entity;
        double x;
        double y;
        double z;
        float width = 0.6f;
        float height = 1.8f;
        float presence;
        float hurt;

        private Mark() {
        }

        void assign(class_1309 target) {
            this.entity = target;
            this.presence = 0.0f;
            this.hurt = 0.0f;
        }

        void follow(float partialTick, class_638 level) {
            class_1309 e = this.entity;
            if (e == null) {
                return;
            }
            if (e.method_31481() || e.method_73183() != level) {
                this.hurt = 0.0f;
                return;
            }
            this.x = class_3532.method_16436(partialTick, e.field_6014, e.method_23317());
            this.y = class_3532.method_16436(partialTick, e.field_6036, e.method_23318());
            this.z = class_3532.method_16436(partialTick, e.field_5969, e.method_23321());
            this.width = e.method_17681();
            this.height = e.method_17682();
            this.hurt = e.field_6235 > 0 ? class_3532.method_15363((e.field_6235 - partialTick) / TargetEspModule.HURT_TICKS, 0.0f, 1.0f) : 0.0f;
        }
    }

    public TargetEspModule() {
        super("target-esp", "Target ESP", "Draws an Egyptian mark on your combat target.", ModuleCategory.VISUALS);
        this.style = (ModeSetting) addSetting("style", new ModeSetting("Style", STYLE_ANKH, STYLE_ANKH, STYLE_HORUS, STYLE_VORTEX, STYLE_CAPSTONE));
        this.colorMode = (ModeSetting) addSetting("color-mode", new ModeSetting("Color", GlyphTint.THEME, GlyphTint.THEME, GlyphTint.MANUAL));
        this.mainColor = (ColorSetting) addSetting("main-color", new ColorSetting("Main Color", -12614409));
        this.secondColor = (ColorSetting) addSetting("second-color", new ColorSetting("Second Color", -15189606));
        this.hitColor = (ColorSetting) addSetting("hit-color", new ColorSetting("Hit Color", -2233345));
        this.scale = (NumberSetting) addSetting("scale", new NumberSetting("Scale", 1.0f, 0.6f, 1.8f).step(0.1f));
        this.tempo = (NumberSetting) addSetting("tempo", new NumberSetting("Tempo", 1.0f, APPEAR_SECONDS, 2.4f).step(0.1f));
        this.opacity = (NumberSetting) addSetting("opacity", new NumberSetting("Opacity", 0.9f, 0.2f, 1.0f).step(0.05f));
        this.memory = (NumberSetting) addSetting("memory", new NumberSetting("Memory", 4.0f, 0.5f, 15.0f).step(0.5f));
        this.crosshair = (BooleanSetting) addSetting("crosshair", new BooleanSetting("Crosshair Pick", true));
        this.playersOnly = (BooleanSetting) addSetting("players-only", new BooleanSetting("Players Only", false));
        this.hurtFlash = (BooleanSetting) addSetting("hurt-flash", new BooleanSetting("Hit Flash", true));
        this.throughWalls = (BooleanSetting) addSetting("through-walls", new BooleanSetting("Through Walls", false));
        this.canvas = new GlyphCanvas();
        this.palette = new GlyphTint();
        this.capX = new double[4];
        this.capZ = new double[4];
        this.current = new Mark();
        this.previous = new Mark();
        this.mainColor.visibleWhen(() -> {
            return this.colorMode.is(GlyphTint.MANUAL);
        });
        this.secondColor.visibleWhen(() -> {
            return this.colorMode.is(GlyphTint.MANUAL);
        });
        this.hitColor.visibleWhen(() -> {
            return this.colorMode.is(GlyphTint.MANUAL) && this.hurtFlash.get();
        });
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        reset();
        on(TickEvent.TYPE, Stage.POST, this::onTick);
        on(Render3DEvent.TYPE, this::onRender);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        reset();
    }

    private void reset() {
        this.current.assign(null);
        this.previous.assign(null);
        this.boundLevel = null;
        this.lastFrame = 0L;
    }

    private void onTick(TickEvent event) {
        if (this.boundLevel == null || class_310.method_1551().field_1687 == this.boundLevel) {
            return;
        }
        reset();
    }

    private void onRender(Render3DEvent event) {
        class_310 mc = class_310.method_1551();
        class_638 level = mc.field_1687;
        if (level == null || mc.field_1724 == null) {
            if (this.boundLevel != null) {
                reset();
                return;
            }
            return;
        }
        if (level != this.boundLevel) {
            reset();
            this.boundLevel = level;
        }
        long now = System.nanoTime();
        float dt = this.lastFrame == 0 ? 0.0f : Math.min(0.1f, (now - this.lastFrame) / 1.0E9f);
        this.lastFrame = now;
        this.phase += (double) (dt * this.tempo.get());
        retarget(pick(mc), event.partialTick, level);
        this.current.presence = this.current.entity != null ? Math.min(1.0f, this.current.presence + (dt / APPEAR_SECONDS)) : 0.0f;
        this.previous.presence = Math.max(0.0f, this.previous.presence - (dt / VANISH_SECONDS));
        if (this.previous.presence <= 0.0f) {
            this.previous.entity = null;
        }
        this.current.follow(event.partialTick, level);
        this.previous.follow(event.partialTick, level);
        if (this.current.entity == null && this.previous.entity == null) {
            return;
        }
        this.palette.resolve(this.colorMode.get(), CosmeticAccent.argb(), this.mainColor.argb(), this.secondColor.argb());
        this.hit = this.colorMode.is(GlyphTint.MANUAL) ? this.hitColor.argb() : this.palette.flash();
        this.canvas.begin(mc.field_1773.method_19418());
        if (this.previous.entity != null) {
            draw(this.previous, true);
        }
        if (this.current.entity != null) {
            draw(this.current, false);
        }
        this.canvas.submit(this.throughWalls.get());
    }

    private void retarget(class_1309 wanted, float partialTick, class_638 level) {
        if (wanted == this.current.entity) {
            return;
        }
        if (wanted != null && wanted == this.previous.entity) {
            Mark swap = this.current;
            this.current = this.previous;
            this.previous = swap;
            return;
        }
        if (this.current.entity != null && this.current.presence > 0.02f) {
            Mark swap2 = this.previous;
            this.previous = this.current;
            this.current = swap2;
        }
        this.current.assign(wanted);
        this.current.follow(partialTick, level);
    }

    private class_1309 pick(class_310 mc) {
        long window = (long) (this.memory.get() * 1000.0f);
        class_1309 target = this.crosshair.get() ? CombatTarget.current(window) : CombatTarget.lastHit(window);
        if (target == null || target == mc.field_1724 || !target.method_5805() || target.method_7325() || (target instanceof class_1531)) {
            return null;
        }
        if (!this.playersOnly.get() || (target instanceof class_1657)) {
            return target;
        }
        return null;
    }

    private void draw(Mark mark, boolean leaving) {
        if (this.canvas.distanceSqr(mark.x, mark.y, mark.z) > MAX_DISTANCE_SQR) {
        }
        float p = mark.presence;
        float visible = leaving ? p * p : 1.0f - (((1.0f - p) * (1.0f - p)) * (1.0f - p));
        float grow = leaving ? 1.0f + (APPEAR_SECONDS * (1.0f - p)) : 0.55f + (0.45f * unfurl(p));
        float flash = this.hurtFlash.get() ? mark.hurt : 0.0f;
        float alpha = this.opacity.get() * visible;
        if (alpha < 0.01f) {
            return;
        }
        this.gold = GlyphTint.mix(this.palette.primary(), this.hit, flash * VORTEX_SPIN);
        this.lapis = GlyphTint.mix(this.palette.secondary(), this.hit, flash * 0.45f);
        this.light = GlyphTint.mix(this.palette.highlight(), this.hit, flash * 0.6f);
        float size = this.scale.get() * grow * (1.0f + (0.1f * flash));
        float radius = ((mark.width * 0.5f) + 0.4f) * size;
        switch (this.style.get()) {
            case "Horus Halo":
                horusHalo(mark, size, alpha);
                break;
            case "Sand Vortex":
                sandVortex(mark, size, radius, alpha);
                break;
            case "Capstone":
                capstone(mark, size, alpha);
                break;
            default:
                ankhCircle(mark, size, radius, alpha);
                break;
        }
    }

    private void ankhCircle(Mark m, float size, float r, float a) {
        double gy = m.y + 0.02d;
        this.canvas.ring(m.x, gy, m.z, r * 0.62f, r * 0.86f, 40, GlyphTint.alpha(this.lapis, a * 0.1f));
        this.canvas.ring(m.x, gy, m.z, r * 0.86f, r * 0.965f, 40, GlyphTint.alpha(this.gold, a * 0.22f));
        this.canvas.ring(m.x, gy, m.z, r * 0.965f, r * 1.035f, 48, GlyphTint.alpha(this.light, a * 0.9f));
        this.canvas.ring(m.x, gy, m.z, r * 1.035f, r * 1.14f, 48, GlyphTint.alpha(this.gold, a * 0.2f));
        float drift = (float) (((-this.phase) * 0.7d) % 6.2831854820251465d);
        for (int i = 0; i < 12; i++) {
            float from = drift + (i * 0.5235988f);
            this.canvas.band(m.x, gy, m.z, r * 0.74f, r * VORTEX_SPIN, from, from + (0.5235988f * 0.62f), 3, GlyphTint.alpha(this.lapis, a * 0.85f));
        }
        float orbit = (float) ((this.phase * 0.9d) % 6.2831854820251465d);
        float mark = 0.12f * size;
        for (int i2 = 0; i2 < 6; i2++) {
            float angle = orbit + ((i2 * 6.2831855f) / 6.0f);
            double px = m.x + ((double) (class_3532.method_15362(angle) * r));
            double pz = m.z + ((double) (class_3532.method_15374(angle) * r));
            double py = m.y + (0.2d * ((double) size)) + (0.045d * ((double) size) * ((double) class_3532.method_15374((this.phase * 2.6d) + (((double) i2) * 1.3d))));
            boolean ankh = (i2 & 1) == 0;
            this.canvas.sprite(GlyphShapes.OCTAGON, px, py, pz, mark * 2.1f, 0.0f, GlyphTint.alpha(ankh ? this.gold : this.lapis, a * 0.14f));
            if (ankh) {
                this.canvas.sprite(GlyphShapes.ANKH, px, py, pz, mark, 0.0f, GlyphTint.alpha(this.gold, a));
            } else {
                this.canvas.sprite(GlyphShapes.SCARAB, px, py, pz, mark * 0.9f, 0.0f, GlyphTint.alpha(this.lapis, a));
                this.canvas.sprite(GlyphShapes.DISK, px, py, pz, mark * APPEAR_SECONDS, mark * APPEAR_SECONDS, 0.0f, 0.0f, 4.2f, GlyphTint.alpha(this.light, a));
            }
        }
    }

    private void horusHalo(Mark m, float size, float a) {
        double top = m.y + ((double) m.height);
        float hr = ((m.width * 0.5f) + 0.2f) * size;
        double hy = top + 0.08d;
        this.canvas.ring(m.x, hy, m.z, hr * 0.9f, hr, 32, GlyphTint.alpha(this.gold, a * 0.85f));
        this.canvas.ring(m.x, hy, m.z, hr, hr * 1.18f, 32, GlyphTint.alpha(this.gold, a * 0.18f));
        float spin = (float) ((this.phase * 1.2d) % 6.2831854820251465d);
        for (int i = 0; i < 8; i++) {
            float from = spin + ((i * 6.2831855f) / 8.0f);
            this.canvas.band(m.x, hy, m.z, hr * 1.22f, hr * 1.44f, from, from + 0.13f, 1, GlyphTint.alpha(this.lapis, a * VORTEX_SPIN));
        }
        float eye = 0.26f * size;
        double ey = top + (0.52d * ((double) size)) + (0.05d * ((double) size) * ((double) class_3532.method_15374(this.phase * 1.8d)));
        float lid = blink();
        this.canvas.sprite(GlyphShapes.OCTAGON, m.x, ey, m.z, eye * 1.95f, 0.0f, GlyphTint.alpha(this.lapis, a * 0.16f));
        this.canvas.sprite(GlyphShapes.DISK, m.x, ey, m.z, eye * 1.3f, 0.0f, GlyphTint.alpha(GlyphTint.mix(this.lapis, -16777216, 0.45f), a * 0.6f));
        this.canvas.sprite(GlyphShapes.HALO, m.x, ey, m.z, eye * 1.36f, 0.0f, GlyphTint.alpha(this.gold, a));
        this.canvas.sprite(GlyphShapes.EYE, m.x, ey, m.z, eye, eye * lid, 0.0f, GlyphTint.alpha(this.gold, a));
        this.canvas.sprite(GlyphShapes.EYE_PUPIL, m.x, ey, m.z, eye, eye * lid, 0.0f, GlyphTint.alpha(this.light, a));
    }

    private void sandVortex(Mark m, float size, float r, float a) {
        double base = m.y + 0.03d;
        double top = m.y + ((double) m.height) + (0.3d * ((double) size));
        double span = top - base;
        double waistY = m.y + (((double) m.height) * 0.5d);
        float waist = class_3532.method_15363((float) ((waistY - base) / span), 0.15f, 0.85f);
        float neck = ((m.width * 0.5f) + 0.12f) * size;
        float fall = (float) ((this.phase * 0.47999998927116394d) % 1.0d);
        float turn = (float) ((this.phase * 0.800000011920929d) % 6.2831854820251465d);
        float tumble = (float) ((this.phase * 4.0d) % 6.2831854820251465d);
        this.canvas.ring(m.x, top, m.z, r * 0.9f, r * 0.98f, 40, GlyphTint.alpha(this.gold, a * VORTEX_SPIN));
        this.canvas.ring(m.x, top, m.z, r * 0.98f, r * 1.1f, 40, GlyphTint.alpha(this.gold, a * 0.16f));
        for (int i = 0; i < 6; i++) {
            float from = ((-turn) * 1.5f) + ((i * 6.2831855f) / 6.0f);
            this.canvas.band(m.x, top, m.z, r * 0.76f, r * 0.84f, from, from + 0.5f, 2, GlyphTint.alpha(this.lapis, a * 0.7f));
        }
        this.canvas.ring(m.x, waistY, m.z, neck * 0.9f, neck, 24, GlyphTint.alpha(this.light, a * APPEAR_SECONDS));
        this.canvas.ring(m.x, base, m.z, r * 0.92f, r, 40, GlyphTint.alpha(this.lapis, a * 0.6f));
        this.canvas.ring(m.x, base, m.z, 0.0f, r * 0.92f, 32, GlyphTint.alpha(this.gold, a * 0.08f));
        float wave = (float) ((this.phase * 0.9599999785423279d) % 1.0d);
        float swell = neck + (((r * 1.12f) - neck) * wave);
        this.canvas.ring(m.x, base + 0.005d, m.z, swell * 0.93f, swell, 36, GlyphTint.alpha(this.gold, a * 0.55f * (1.0f - wave)));
        int post = GlyphTint.alpha(this.gold, a * 0.35f);
        for (int i2 = 0; i2 < 3; i2++) {
            float angle = (turn * 0.5f) + ((i2 * 6.2831855f) / 3.0f);
            double px = m.x + ((double) (class_3532.method_15362(angle) * r * 1.04f));
            double pz = m.z + ((double) (class_3532.method_15374(angle) * r * 1.04f));
            this.canvas.line(px, base, pz, px, top, pz, post, 1.4f);
        }
        for (int i3 = 0; i3 < 40; i3++) {
            float lead = GRAIN_SEEDS[i3 * 3];
            float spread = GRAIN_SEEDS[(i3 * 3) + 1];
            float grit = GRAIN_SEEDS[(i3 * 3) + 2];
            float u = class_3532.method_22450(fall + lead);
            float drop = u * (0.4f + (0.6f * u));
            float h = 1.0f - drop;
            float k = h >= waist ? (h - waist) / (1.0f - waist) : (waist - h) / waist;
            float bulge = k * (2.0f - k);
            float reach = neck + ((r - neck) * bulge);
            float angle2 = (spread * 6.2831855f) + turn + (drop * VORTEX_SWIRL);
            double px2 = m.x + ((double) (class_3532.method_15362(angle2) * reach));
            double pz2 = m.z + ((double) (class_3532.method_15374(angle2) * reach));
            double py = top - (((double) drop) * span);
            float grainAlpha = a * Math.min(1.0f, u * HURT_TICKS) * Math.min(1.0f, (1.0f - u) * 8.0f);
            float mote = 0.042f * size * (0.7f + (0.6f * grit));
            int color = GlyphTint.mix(this.light, this.gold, bulge);
            if (i3 % 5 == 0) {
                color = GlyphTint.mix(color, this.lapis, 0.6f);
            }
            if (k < APPEAR_SECONDS) {
                this.canvas.sprite(GlyphShapes.OCTAGON, px2, py, pz2, mote * 2.6f, 0.0f, GlyphTint.alpha(this.light, grainAlpha * 0.6f * (APPEAR_SECONDS - k)));
            }
            this.canvas.sprite(GlyphShapes.GRAIN, px2, py, pz2, mote, tumble + (grit * 6.2831855f), GlyphTint.alpha(color, grainAlpha));
        }
    }

    private void capstone(Mark m, float size, float a) {
        double top = m.y + ((double) m.height);
        double apex = top + (0.28d * ((double) size)) + (0.06d * ((double) size) * ((double) class_3532.method_15374(this.phase * 2.1d)));
        float depth = 0.34f * size;
        float half = 0.17f * size * class_3532.field_15724;
        double rim = apex + ((double) depth);
        double core = apex + (((double) depth) * 0.6d);
        float spin = (float) ((this.phase * 1.3d) % 6.2831854820251465d);
        for (int k = 0; k < 4; k++) {
            float angle = spin + (k * 1.5707964f);
            this.capX[k] = m.x + ((double) (class_3532.method_15362(angle) * half));
            this.capZ[k] = m.z + ((double) (class_3532.method_15374(angle) * half));
        }
        this.canvas.sprite(GlyphShapes.OCTAGON, m.x, core, m.z, depth * 1.7f, 0.0f, GlyphTint.alpha(this.gold, a * 0.12f));
        for (int k2 = 0; k2 < 4; k2++) {
            int n = (k2 + 1) & 3;
            int face = (k2 & 1) == 0 ? this.gold : GlyphTint.mix(this.gold, this.lapis, 0.35f);
            this.canvas.outwardTriangle(this.capX[k2], rim, this.capZ[k2], this.capX[n], rim, this.capZ[n], m.x, apex, m.z, m.x, core, m.z, GlyphTint.alpha(face, a * 0.5f));
        }
        this.canvas.outwardTriangle(this.capX[0], rim, this.capZ[0], this.capX[1], rim, this.capZ[1], this.capX[2], rim, this.capZ[2], m.x, core, m.z, GlyphTint.alpha(this.lapis, a * 0.65f));
        this.canvas.outwardTriangle(this.capX[0], rim, this.capZ[0], this.capX[2], rim, this.capZ[2], this.capX[3], rim, this.capZ[3], m.x, core, m.z, GlyphTint.alpha(this.lapis, a * 0.65f));
        int edge = GlyphTint.alpha(this.light, a * 0.95f);
        for (int k3 = 0; k3 < 4; k3++) {
            int n2 = (k3 + 1) & 3;
            this.canvas.line(this.capX[k3], rim, this.capZ[k3], this.capX[n2], rim, this.capZ[n2], edge, 2.2f);
            this.canvas.line(this.capX[k3], rim, this.capZ[k3], m.x, apex, m.z, edge, 2.2f);
        }
        double drop = apex - top;
        int plumb = GlyphTint.alpha(this.gold, a * 0.45f);
        for (int i = 0; i < 3; i++) {
            double from = apex - (drop * (0.12d + (((double) i) * 0.3d)));
            double to = from - (drop * 0.14d);
            this.canvas.line(m.x, from, m.z, m.x, to, m.z, plumb, 1.6f);
        }
        float orbit = (float) (((-this.phase) * 1.9d) % 6.2831854820251465d);
        float reach = half * 1.7f;
        for (int i2 = 0; i2 < 4; i2++) {
            float angle2 = orbit + (i2 * 1.5707964f) + 0.7853982f;
            float twinkle = 0.6f + (0.4f * class_3532.method_15374((this.phase * 7.0d) + (((double) i2) * 2.1d)));
            this.canvas.sprite(GlyphShapes.SPARK, m.x + ((double) (class_3532.method_15362(angle2) * reach)), rim - (((double) depth) * 0.15d), m.z + ((double) (class_3532.method_15374(angle2) * reach)), 0.06f * size * twinkle, angle2, GlyphTint.alpha(this.light, a * twinkle));
        }
    }

    private float blink() {
        double cycle = this.phase % BLINK_PERIOD;
        if (cycle > BLINK_LENGTH) {
            return 1.0f;
        }
        return 1.0f - (0.88f * class_3532.method_15374((cycle / BLINK_LENGTH) * 3.141592653589793d));
    }

    private static float[] scatter(int count, long salt) {
        class_5819 random = class_5819.method_43049(salt);
        float[] out = new float[count];
        for (int i = 0; i < count; i++) {
            out[i] = random.method_43057();
        }
        return out;
    }

    private static float unfurl(float t) {
        float u = 1.0f - t;
        return 1.0f - (((u * u) * (1.0f + (1.6f * t))) * (1.0f - (0.6f * t)));
    }
}
