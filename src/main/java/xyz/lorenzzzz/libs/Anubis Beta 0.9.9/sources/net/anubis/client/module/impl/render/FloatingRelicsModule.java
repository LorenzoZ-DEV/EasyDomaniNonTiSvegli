package net.anubis.client.module.impl.render;

import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.impl.client.PerformanceModule;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.util.glyph.GlyphCanvas;
import net.anubis.client.util.glyph.GlyphShapes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_4604;
import net.minecraft.class_5819;
import net.minecraft.class_638;
import net.minecraft.class_746;
import net.minecraft.class_9848;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/FloatingRelicsModule.class */
@Environment(EnvType.CLIENT)
public final class FloatingRelicsModule extends ClientModule {
    private static final String SYMBOL_ANUBIS = "Anubis";
    private static final String SYMBOL_LARP = "Larp";
    private static final String COLOR_THEME = "Theme";
    private static final String COLOR_MANUAL = "Manual";
    private static final int MAX_RELICS = 32;
    private static final float GRAVITY = 0.016f;
    private static final float BUOYANCY = 0.045f;
    private static final float AIR_DRAG = 0.982f;
    private static final float GROUND_GRIP = 0.86f;
    private static final float ORBIT_PULL = 0.012f;
    private static final float SWIRL = 0.0025f;
    private static final float FOLLOW = 0.035f;
    private static final float WANDER = 0.003f;
    private static final float MAX_SPEED = 0.9f;
    private static final float SPIN_SETTLE = 0.03f;
    private static final float ANGLE_WRAP = 512.0f;
    private static final float TILT_SPRING = 0.09f;
    private static final float TILT_DAMP = 0.9f;
    private static final float MAX_TILT = 0.55f;
    private static final int APPEAR_TICKS = 12;
    private static final int STAGGER_TICKS = 3;
    private static final float GLYPH_SCALE = 1.25f;
    private static final float THICKNESS = 0.07f;
    private static final float SWAY = 0.12f;
    private static final float BOB = 0.2f;
    private static final float BACK_ALPHA = 0.9f;
    private static final float GLINT = 0.45f;
    private static final float HALO_PUSH = 0.6f;
    private static final double DETAIL_RANGE_SQR = 576.0d;
    private static final float LIGHT_X;
    private static final float LIGHT_Y;
    private static final float LIGHT_Z;
    private final ModeSetting symbols;
    private final NumberSetting count;
    private final NumberSetting size;
    private final NumberSetting spread;
    private final NumberSetting bounce;
    private final NumberSetting drift;
    private final NumberSetting spinSpeed;
    private final ModeSetting colorMode;
    private final ColorSetting mainColor;
    private final ColorSetting secondColor;
    private final BooleanSetting halo;
    private final BooleanSetting blocks;
    private final Relic[] relics;
    private final GlyphCanvas canvas;
    private final class_5819 random;
    private final class_2338.class_2339 cursor;
    private int live;
    private int age;
    private class_638 boundLevel;
    private int tintMain;
    private int tintShade;
    private int tintShine;
    private static final float[][] ANUBIS_GLYPHS = {RelicGlyphs.ANKH, RelicGlyphs.EYE, RelicGlyphs.SCARAB, RelicGlyphs.DJED, RelicGlyphs.FEATHER, RelicGlyphs.WAS};
    private static final float FACE_ALPHA = 0.94f;
    private static final float[] ANUBIS_FIT = {1.0f, 0.84f, FACE_ALPHA, 1.0f, 1.0f, 1.0f};
    private static final float[][] LARP_GLYPHS = {RelicGlyphs.DOLLAR, RelicGlyphs.EURO, RelicGlyphs.BITCOIN};
    private static final float[] LARP_FIT = {1.0f, 0.92f, 0.96f};
    private static final float[] HALO_SCALES = {1.35f, 2.0f};
    private static final float[] HALO_WEIGHTS = {1.0f, 0.5f};
    private static final long EPOCH = System.nanoTime();

    static {
        float length = (float) Math.sqrt((0.32f * 0.32f) + (0.87f * 0.87f) + (0.37f * 0.37f));
        LIGHT_X = 0.32f / length;
        LIGHT_Y = 0.87f / length;
        LIGHT_Z = 0.37f / length;
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/FloatingRelicsModule$Relic.class */
    @Environment(EnvType.CLIENT)
    private static final class Relic {
        double x;
        double y;
        double z;
        double lastX;
        double lastY;
        double lastZ;
        float vx;
        float vy;
        float vz;
        float yaw;
        float lastYaw;
        float turn;
        float restTurn;
        float tiltX;
        float tiltZ;
        float lastTiltX;
        float lastTiltZ;
        float swayX;
        float swayZ;
        float orbit;
        float lift;
        float grain;
        float phase;
        boolean placed;
        int wakeAt;
        int placedAt;

        private Relic() {
        }
    }

    public FloatingRelicsModule() {
        super("floating-relics", "Floating Relics", "Egyptian symbols float and spin around you.", ModuleCategory.VISUALS);
        this.symbols = (ModeSetting) addSetting("symbols", new ModeSetting("Symbols", "Anubis", "Anubis", SYMBOL_LARP));
        this.count = (NumberSetting) addSetting("count", new NumberSetting("Count", 9.0f, 1.0f, 32.0f).step(1.0f));
        this.size = (NumberSetting) addSetting("size", new NumberSetting("Size", 0.3f, 0.15f, 0.75f).step(0.05f));
        this.spread = (NumberSetting) addSetting("spread", new NumberSetting("Spread", 2.6f, 1.2f, 6.0f).step(0.1f));
        this.bounce = (NumberSetting) addSetting("bounce", new NumberSetting("Bounce", HALO_PUSH, 0.1f, 0.95f).step(0.05f));
        this.drift = (NumberSetting) addSetting("drift", new NumberSetting("Drift", 0.5f, 0.0f, 2.5f).step(0.1f));
        this.spinSpeed = (NumberSetting) addSetting("spin", new NumberSetting("Spin", 1.0f, BOB, 3.0f).step(0.1f));
        this.colorMode = (ModeSetting) addSetting("color", new ModeSetting("Color", "Theme", "Theme", "Manual"));
        this.mainColor = (ColorSetting) addSetting("main-color", new ColorSetting("Main Color", -12614409));
        this.secondColor = (ColorSetting) addSetting("second-color", new ColorSetting("Second Color", -14923108));
        this.halo = (BooleanSetting) addSetting("halo", new BooleanSetting("Halo", true));
        this.blocks = (BooleanSetting) addSetting("block-bounce", new BooleanSetting("Block Bounce", true));
        this.relics = new Relic[32];
        this.canvas = new GlyphCanvas();
        this.random = class_5819.method_43047();
        this.cursor = new class_2338.class_2339();
        this.mainColor.visibleWhen(() -> {
            return this.colorMode.is("Manual");
        });
        this.secondColor.visibleWhen(() -> {
            return this.colorMode.is("Manual");
        });
        for (int i = 0; i < 32; i++) {
            this.relics[i] = new Relic();
        }
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        reset();
        on(TickEvent.TYPE, Stage.POST, this::onTick);
        on(Render3DEvent.TYPE, this::onRender3D);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        reset();
    }

    private void reset() {
        this.live = 0;
        this.age = 0;
        this.boundLevel = null;
    }

    private void syncLevel(class_638 level) {
        if (level == this.boundLevel) {
            return;
        }
        this.boundLevel = level;
        this.live = 0;
    }

    private void onTick(TickEvent event) {
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        class_638 level = mc.field_1687;
        if (player == null || level == null) {
            reset();
            return;
        }
        syncLevel(level);
        this.age++;
        int want = Math.min(32, this.count.getInt());
        for (int i = this.live; i < want; i++) {
            wake(this.relics[i], (i - this.live) * 3);
        }
        this.live = want;
        double px = player.method_23317();
        double py = player.method_23318();
        double pz = player.method_23321();
        float height = player.method_17682();
        float moveX = (float) (px - player.field_6014);
        float moveZ = (float) (pz - player.field_5969);
        float restitution = this.bounce.get();
        for (int i2 = 0; i2 < this.live; i2++) {
            step(this.relics[i2], level, px, py, pz, height, moveX, moveZ, restitution);
        }
        for (int i3 = 0; i3 < this.live; i3++) {
            for (int j = i3 + 1; j < this.live; j++) {
                knock(this.relics[i3], this.relics[j], restitution);
            }
        }
    }

    private void wake(Relic r, int delay) {
        r.placed = false;
        r.wakeAt = this.age + delay;
        r.orbit = MAX_TILT + (GLINT * this.random.method_43057());
        r.lift = this.random.method_43057();
        r.grain = 0.82f + (0.3f * this.random.method_43057());
        r.phase = this.random.method_43057() * 6.2831855f;
        r.restTurn = signed(THICKNESS, SWAY);
    }

    private float signed(float min, float max) {
        float value = min + ((max - min) * this.random.method_43057());
        return this.random.method_43056() ? value : -value;
    }

    private void place(Relic r, double px, double py, double pz) {
        float angle = this.random.method_43057() * 6.2831855f;
        double radius = this.spread.get() * r.orbit;
        r.x = px + (((double) class_3532.method_15362(angle)) * radius);
        r.y = py + 0.15d;
        r.z = pz + (((double) class_3532.method_15374(angle)) * radius);
        r.lastX = r.x;
        r.lastY = r.y;
        r.lastZ = r.z;
        r.vx = (-class_3532.method_15374(angle)) * SPIN_SETTLE;
        r.vy = 0.14f + (0.08f * this.random.method_43057());
        r.vz = class_3532.method_15362(angle) * SPIN_SETTLE;
        r.yaw = this.random.method_43057() * 6.2831855f;
        r.lastYaw = r.yaw;
        r.turn = r.restTurn * 3.0f;
        r.tiltX = 0.0f;
        r.tiltZ = 0.0f;
        r.lastTiltX = 0.0f;
        r.lastTiltZ = 0.0f;
        r.swayX = signed(0.02f, 0.05f);
        r.swayZ = signed(0.02f, 0.05f);
        r.placed = true;
        r.placedAt = this.age;
    }

    private void step(Relic r, class_638 level, double px, double py, double pz, float height, float moveX, float moveZ, float restitution) {
        if (!r.placed) {
            if (this.age < r.wakeAt) {
                return;
            } else {
                place(r, px, py, pz);
            }
        }
        r.lastX = r.x;
        r.lastY = r.y;
        r.lastZ = r.z;
        double dx = r.x - px;
        double dz = r.z - pz;
        double flat = Math.sqrt((dx * dx) + (dz * dz));
        double hover = py + 0.35d + (((double) r.lift) * (((double) height) + 0.2d)) + (0.16d * ((double) class_3532.method_15374((this.age + (r.phase * 16.0f)) * BUOYANCY)));
        double leash = (((double) this.spread.get()) * 3.0d) + 8.0d;
        if (flat > leash || Math.abs(r.y - hover) > leash) {
            place(r, px, py, pz);
            return;
        }
        float nx = flat > 1.0E-4d ? (float) (dx / flat) : 1.0f;
        float nz = flat > 1.0E-4d ? (float) (dz / flat) : 0.0f;
        float pull = ((float) (((double) (this.spread.get() * r.orbit)) - flat)) * ORBIT_PULL;
        float swirl = this.drift.get() * SWIRL;
        r.vx += ((nx * pull) - (nz * swirl)) + (moveX * FOLLOW);
        r.vz += (nz * pull) + (nx * swirl) + (moveZ * FOLLOW);
        float wander = (this.age + (r.phase * 20.0f)) * 0.06f;
        r.vx += class_3532.method_15374(wander) * WANDER;
        r.vz += class_3532.method_15362(wander * 1.31f) * WANDER;
        r.vy -= GRAVITY;
        double sink = hover - r.y;
        if (sink > 0.0d) {
            r.vy += ((float) Math.min(sink, 1.5d)) * BUOYANCY;
        }
        r.vx *= AIR_DRAG;
        r.vy *= AIR_DRAG;
        r.vz *= AIR_DRAG;
        float speed = (r.vx * r.vx) + (r.vy * r.vy) + (r.vz * r.vz);
        if (speed > 0.80999994f) {
            float k = 0.9f / ((float) Math.sqrt(speed));
            r.vx *= k;
            r.vy *= k;
            r.vz *= k;
        }
        boolean collide = this.blocks.get();
        float half = this.size.get() * r.grain * 0.5f;
        double next = r.x + ((double) r.vx);
        if (collide && solid(level, next + ((double) Math.copySign(half, r.vx)), r.y, r.z)) {
            r.vx = (-r.vx) * restitution;
            tumble(r, HALO_PUSH);
        } else {
            r.x = next;
        }
        double next2 = r.y + ((double) r.vy);
        if (collide && solid(level, r.x, next2 + ((double) Math.copySign(half, r.vy)), r.z)) {
            if (r.vy < 0.0f) {
                r.vx *= GROUND_GRIP;
                r.vz *= GROUND_GRIP;
            }
            r.vy = (-r.vy) * restitution;
            tumble(r, 0.8f);
        } else {
            r.y = next2;
        }
        double next3 = r.z + ((double) r.vz);
        if (collide && solid(level, r.x, r.y, next3 + ((double) Math.copySign(half, r.vz)))) {
            r.vz = (-r.vz) * restitution;
            tumble(r, HALO_PUSH);
        } else {
            r.z = next3;
        }
        if (collide && solid(level, r.x, r.y, r.z)) {
            r.y += 0.2d;
            r.lastY = r.y;
            r.vy = Math.max(r.vy, 0.08f);
        }
        if (Math.abs(r.yaw) > ANGLE_WRAP) {
            r.yaw %= 6.2831855f;
        }
        r.lastYaw = r.yaw;
        r.lastTiltX = r.tiltX;
        r.lastTiltZ = r.tiltZ;
        r.turn += ((r.restTurn * this.spinSpeed.get()) - r.turn) * SPIN_SETTLE;
        r.yaw += r.turn;
        r.swayX = (r.swayX - (r.tiltX * TILT_SPRING)) * 0.9f;
        r.swayZ = (r.swayZ - (r.tiltZ * TILT_SPRING)) * 0.9f;
        r.tiltX = class_3532.method_15363(r.tiltX + r.swayX, -0.55f, MAX_TILT);
        r.tiltZ = class_3532.method_15363(r.tiltZ + r.swayZ, -0.55f, MAX_TILT);
    }

    private void knock(Relic a, Relic b, float restitution) {
        if (a.placed && b.placed) {
            double dx = b.x - a.x;
            double dy = b.y - a.y;
            double dz = b.z - a.z;
            double reach = ((double) (this.size.get() * (a.grain + b.grain))) * 0.5d * 0.95d;
            double d2 = (dx * dx) + (dy * dy) + (dz * dz);
            if (d2 >= reach * reach || d2 < 1.0E-8d) {
                return;
            }
            double d = Math.sqrt(d2);
            float nx = (float) (dx / d);
            float ny = (float) (dy / d);
            float nz = (float) (dz / d);
            double push = (reach - d) * 0.5d;
            a.x -= ((double) nx) * push;
            a.y -= ((double) ny) * push;
            a.z -= ((double) nz) * push;
            b.x += ((double) nx) * push;
            b.y += ((double) ny) * push;
            b.z += ((double) nz) * push;
            float closing = ((a.vx - b.vx) * nx) + ((a.vy - b.vy) * ny) + ((a.vz - b.vz) * nz);
            if (closing <= 0.0f) {
                return;
            }
            float impulse = closing * (1.0f + restitution) * 0.5f;
            a.vx -= impulse * nx;
            a.vy -= impulse * ny;
            a.vz -= impulse * nz;
            b.vx += impulse * nx;
            b.vy += impulse * ny;
            b.vz += impulse * nz;
            tumble(a, 0.5f);
            tumble(b, 0.5f);
        }
    }

    private void tumble(Relic r, float strength) {
        r.turn += (this.random.method_43057() - 0.5f) * 0.3f * strength;
        r.swayX += (this.random.method_43057() - 0.5f) * SWAY * strength;
        r.swayZ += (this.random.method_43057() - 0.5f) * SWAY * strength;
    }

    private boolean solid(class_638 level, double x, double y, double z) {
        this.cursor.method_10103(class_3532.method_15357(x), class_3532.method_15357(y), class_3532.method_15357(z));
        class_2680 state = level.method_8320(this.cursor);
        if (state.method_26215()) {
            return false;
        }
        class_265 shape = state.method_26220(level, this.cursor);
        if (shape.method_1110()) {
            return false;
        }
        double lx = x - ((double) this.cursor.method_10263());
        double ly = y - ((double) this.cursor.method_10264());
        double lz = z - ((double) this.cursor.method_10260());
        return lx >= shape.method_1091(class_2350.class_2351.field_11048) && lx <= shape.method_1105(class_2350.class_2351.field_11048) && ly >= shape.method_1091(class_2350.class_2351.field_11052) && ly <= shape.method_1105(class_2350.class_2351.field_11052) && lz >= shape.method_1091(class_2350.class_2351.field_11051) && lz <= shape.method_1105(class_2350.class_2351.field_11051);
    }

    private void onRender3D(Render3DEvent event) {
        if (this.live == 0) {
            return;
        }
        class_310 mc = class_310.method_1551();
        if (mc.field_1687 == null || mc.field_1724 == null) {
            return;
        }
        this.canvas.begin(mc.field_1773.method_19418());
        float pt = event.partialTick;
        float time = (System.nanoTime() - EPOCH) / 1.0E9f;
        class_4604 frustum = PerformanceModule.frustumCulling() ? event.frustum : null;
        float base = this.size.get() * 0.5f * GLYPH_SCALE;
        boolean larp = this.symbols.is(SYMBOL_LARP);
        float[][] glyphs = larp ? LARP_GLYPHS : ANUBIS_GLYPHS;
        float[] fits = larp ? LARP_FIT : ANUBIS_FIT;
        boolean glow = this.halo.get();
        tint();
        for (int i = 0; i < this.live; i++) {
            Relic r = this.relics[i];
            if (r.placed) {
                int kind = i % glyphs.length;
                float since = (this.age - r.placedAt) + pt;
                float extent = base * r.grain * fits[kind] * appear(since / 12.0f);
                if (extent >= 0.004f) {
                    double x = class_3532.method_16436(pt, r.lastX, r.x);
                    double y = class_3532.method_16436(pt, r.lastY, r.y) + ((double) (extent * BOB * class_3532.method_15374((time * 2.4f) + (r.phase * 3.0f))));
                    double z = class_3532.method_16436(pt, r.lastZ, r.z);
                    double reach = ((double) extent) * 2.0d;
                    if (frustum == null || frustum.method_23093(new class_238(x - reach, y - reach, z - reach, x + reach, y + reach, z + reach))) {
                        float fade = Math.min(1.0f, since / 7.2000003f);
                        float yaw = class_3532.method_16439(pt, r.lastYaw, r.yaw);
                        float tiltX = class_3532.method_16439(pt, r.lastTiltX, r.tiltX) + (SWAY * class_3532.method_15374((time * 1.3f) + r.phase));
                        float tiltZ = class_3532.method_16439(pt, r.lastTiltZ, r.tiltZ) + (SWAY * class_3532.method_15374((time * 0.9f) + (r.phase * 2.3f)));
                        float shimmer = glow ? (0.1f + (THICKNESS * class_3532.method_15374((time * 2.1f) + r.phase))) * fade : 0.0f;
                        drawRelic(glyphs[kind], x, y, z, extent, yaw, tiltX, tiltZ, fade, shimmer);
                    }
                }
            }
        }
        this.canvas.submit(false);
    }

    private void tint() {
        if (this.colorMode.is("Manual")) {
            this.tintMain = class_9848.method_61334(this.mainColor.argb());
            this.tintShade = class_9848.method_61334(this.secondColor.argb());
        } else {
            this.tintMain = CosmeticAccent.argb();
            this.tintShade = class_9848.method_61321(this.tintMain, GLINT);
        }
        this.tintShine = class_9848.method_61319(HALO_PUSH, this.tintMain, -1);
    }

    private void drawRelic(float[] glyph, double x, double y, double z, float extent, float yaw, float tiltX, float tiltZ, float fade, float shimmer) {
        float cy = class_3532.method_15362(yaw);
        float sy = class_3532.method_15374(yaw);
        float cx = class_3532.method_15362(tiltX);
        float sx = class_3532.method_15374(tiltX);
        float cz = class_3532.method_15362(tiltZ);
        float sz = class_3532.method_15374(tiltZ);
        float ux = (cy * cz) + (sy * sx * sz);
        float uy = cx * sz;
        float uz = ((-sy) * cz) + (cy * sx * sz);
        float vx = ((-cy) * sz) + (sy * sx * cz);
        float vy = cx * cz;
        float vz = (sy * sz) + (cy * sx * cz);
        float nx = sy * cx;
        float ny = -sx;
        float nz = cy * cx;
        double tx = this.canvas.camX() - x;
        double ty = this.canvas.camY() - y;
        double tz = this.canvas.camZ() - z;
        double distSqr = (tx * tx) + (ty * ty) + (tz * tz);
        if (distSqr < 1.0E-8d) {
            return;
        }
        double dist = Math.sqrt(distSqr);
        float facing = (float) ((((((double) nx) * tx) + (((double) ny) * ty)) + (((double) nz) * tz)) / dist);
        if (facing < 0.0f) {
            ux = -ux;
            uy = -uy;
            uz = -uz;
            nx = -nx;
            ny = -ny;
            nz = -nz;
            facing = -facing;
        }
        if (shimmer > 0.0f) {
            double push = ((double) (extent * HALO_PUSH)) / dist;
            double hx = x - (tx * push);
            double hy = y - (ty * push);
            double hz = z - (tz * push);
            for (int layer = 0; layer < HALO_SCALES.length; layer++) {
                this.canvas.sprite(GlyphShapes.DISK, hx, hy, hz, extent * HALO_SCALES[layer], 0.0f, withAlpha(this.tintMain, shimmer * HALO_WEIGHTS[layer]));
            }
        }
        double au = ux * extent;
        double bu = uy * extent;
        double cu = uz * extent;
        double av = vx * extent;
        double bv = vy * extent;
        double cv = vz * extent;
        double depth = extent * THICKNESS;
        if (distSqr < DETAIL_RANGE_SQR && facing < 0.995f) {
            this.canvas.shape(glyph, x - (((double) nx) * depth), y - (((double) ny) * depth), z - (((double) nz) * depth), au, bu, cu, av, bv, cv, withAlpha(this.tintShade, 0.9f * fade));
        }
        float light = Math.max(0.0f, (nx * LIGHT_X) + (ny * LIGHT_Y) + (nz * LIGHT_Z));
        float glint = facing * facing;
        float glint2 = glint * glint;
        int face = class_9848.method_61319(glint2 * glint2 * GLINT, class_9848.method_61321(this.tintMain, HALO_PUSH + (0.22f * light) + (0.18f * facing)), this.tintShine);
        this.canvas.shape(glyph, x + (((double) nx) * depth), y + (((double) ny) * depth), z + (((double) nz) * depth), au, bu, cu, av, bv, cv, withAlpha(face, FACE_ALPHA * fade));
    }

    private static float appear(float t) {
        if (t >= 1.0f) {
            return 1.0f;
        }
        if (t <= 0.0f) {
            return 0.0f;
        }
        float inv = 1.0f - t;
        return (1.0f - ((inv * inv) * inv)) + (0.22f * class_3532.method_15374(t * 3.1415927f) * inv);
    }

    private static int withAlpha(int rgb, float alpha) {
        int a = (int) ((Math.max(0.0f, Math.min(1.0f, alpha)) * 255.0f) + 0.5f);
        return (a << 24) | (rgb & 16777215);
    }
}
