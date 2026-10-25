package net.anubis.client.module.impl.render;

import java.util.Objects;
import java.util.function.Consumer;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.PacketReceiveEvent;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.util.combat.TotemPops;
import net.anubis.client.util.glyph.GlyphCanvas;
import net.anubis.client.util.glyph.GlyphSwarm;
import net.anubis.client.util.glyph.GlyphTint;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1665;
import net.minecraft.class_1684;
import net.minecraft.class_1685;
import net.minecraft.class_238;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_5819;
import net.minecraft.class_638;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/RelicParticlesModule.class */
@Environment(EnvType.CLIENT)
public final class RelicParticlesModule extends ClientModule {
    private static final String GLYPH_ANKH = "Ankh";
    private static final String GLYPH_SCARAB = "Scarab";
    private static final String GLYPH_SAND = "Sand";
    private static final String GLYPH_SPARK = "Spark";
    private static final String GLYPH_MIXED = "Mixed";
    private static final float BASE_SIZE = 0.085f;
    private static final double PROJECTILE_RANGE_SQR = 6400.0d;
    private static final double RESTING_SQR = 9.0E-4d;
    private static final double STEP_MIN_SPEED = 0.07d;
    private static final int PEARL = 0;
    private static final int ARROW = 1;
    private static final int TRIDENT = 2;
    private static final int NONE = -1;
    private final BooleanSetting pops;
    private final ModeSetting popGlyph;
    private final NumberSetting popBurst;
    private final BooleanSetting ownPops;
    private final BooleanSetting projectiles;
    private final BooleanSetting pearls;
    private final BooleanSetting arrows;
    private final BooleanSetting tridents;
    private final ModeSetting trailGlyph;
    private final NumberSetting trailDensity;
    private final BooleanSetting footsteps;
    private final ModeSetting stepGlyph;
    private final NumberSetting stepDensity;
    private final NumberSetting size;
    private final NumberSetting lifetime;
    private final NumberSetting limit;
    private final BooleanSetting halo;
    private final ModeSetting colorMode;
    private final ColorSetting mainColor;
    private final ColorSetting secondColor;
    private final GlyphSwarm swarm;
    private final GlyphCanvas canvas;
    private final GlyphTint palette;
    private final class_5819 random;
    private final Consumer<class_1309> popSink;
    private class_638 boundLevel;
    private long lastFrame;
    private static final int CAPACITY = ((GlyphCanvas.MAX_QUADS / GlyphSwarm.WORST_QUADS) / 10) * 10;
    private static final byte[] MIXED_ORDER = {0, 3, 1, 3, 2, 3};

    public RelicParticlesModule() {
        super("particles", "Particles", "Egyptian particles for totem pops, projectiles and footsteps.", ModuleCategory.VISUALS);
        this.pops = (BooleanSetting) addSetting("pops", new BooleanSetting("Totem Pops", true));
        this.popGlyph = (ModeSetting) addSetting("pop-glyph", new ModeSetting("Pop Glyph", GLYPH_MIXED, GLYPH_MIXED, GLYPH_ANKH, GLYPH_SCARAB, GLYPH_SAND, GLYPH_SPARK));
        this.popBurst = (NumberSetting) addSetting("pop-burst", new NumberSetting("Pop Burst", 26.0f, 4.0f, 80.0f).step(1.0f));
        this.ownPops = (BooleanSetting) addSetting("own-pops", new BooleanSetting("Own Pops", true));
        this.projectiles = (BooleanSetting) addSetting("projectiles", new BooleanSetting("Projectiles", true));
        this.pearls = (BooleanSetting) addSetting("pearls", new BooleanSetting("Pearls", true));
        this.arrows = (BooleanSetting) addSetting("arrows", new BooleanSetting("Arrows", true));
        this.tridents = (BooleanSetting) addSetting("tridents", new BooleanSetting("Tridents", true));
        this.trailGlyph = (ModeSetting) addSetting("trail-glyph", new ModeSetting("Trail Glyph", GLYPH_SPARK, GLYPH_SPARK, GLYPH_SAND, GLYPH_ANKH, GLYPH_SCARAB, GLYPH_MIXED));
        this.trailDensity = (NumberSetting) addSetting("trail-density", new NumberSetting("Trail Density", 2.0f, 1.0f, 6.0f).step(1.0f));
        this.footsteps = (BooleanSetting) addSetting("footsteps", new BooleanSetting("Footsteps", false));
        this.stepGlyph = (ModeSetting) addSetting("step-glyph", new ModeSetting("Step Glyph", GLYPH_SAND, GLYPH_SAND, GLYPH_SPARK, GLYPH_ANKH, GLYPH_SCARAB, GLYPH_MIXED));
        this.stepDensity = (NumberSetting) addSetting("step-density", new NumberSetting("Step Density", 2.0f, 1.0f, 6.0f).step(1.0f));
        this.size = (NumberSetting) addSetting("size", new NumberSetting("Size", 1.0f, 0.3f, 2.5f).step(0.05f));
        this.lifetime = (NumberSetting) addSetting("lifetime", new NumberSetting("Lifetime", 1.4f, 0.3f, 4.0f).step(0.1f));
        this.limit = (NumberSetting) addSetting("limit", new NumberSetting("Limit", 240.0f, 40.0f, CAPACITY).step(10.0f));
        this.halo = (BooleanSetting) addSetting("halo", new BooleanSetting("Halo", true));
        this.colorMode = (ModeSetting) addSetting("color-mode", new ModeSetting("Color", GlyphTint.THEME, GlyphTint.THEME, GlyphTint.MANUAL));
        this.mainColor = (ColorSetting) addSetting("main-color", new ColorSetting("Main Color", -12548362));
        this.secondColor = (ColorSetting) addSetting("second-color", new ColorSetting("Second Color", -14992226));
        this.swarm = new GlyphSwarm(CAPACITY);
        this.canvas = new GlyphCanvas();
        this.palette = new GlyphTint();
        this.random = class_5819.method_43047();
        this.popSink = this::burst;
        ModeSetting modeSetting = this.popGlyph;
        BooleanSetting booleanSetting = this.pops;
        Objects.requireNonNull(booleanSetting);
        modeSetting.visibleWhen(booleanSetting::get);
        NumberSetting numberSetting = this.popBurst;
        BooleanSetting booleanSetting2 = this.pops;
        Objects.requireNonNull(booleanSetting2);
        numberSetting.visibleWhen(booleanSetting2::get);
        BooleanSetting booleanSetting3 = this.ownPops;
        BooleanSetting booleanSetting4 = this.pops;
        Objects.requireNonNull(booleanSetting4);
        booleanSetting3.visibleWhen(booleanSetting4::get);
        BooleanSetting booleanSetting5 = this.pearls;
        BooleanSetting booleanSetting6 = this.projectiles;
        Objects.requireNonNull(booleanSetting6);
        booleanSetting5.visibleWhen(booleanSetting6::get);
        BooleanSetting booleanSetting7 = this.arrows;
        BooleanSetting booleanSetting8 = this.projectiles;
        Objects.requireNonNull(booleanSetting8);
        booleanSetting7.visibleWhen(booleanSetting8::get);
        BooleanSetting booleanSetting9 = this.tridents;
        BooleanSetting booleanSetting10 = this.projectiles;
        Objects.requireNonNull(booleanSetting10);
        booleanSetting9.visibleWhen(booleanSetting10::get);
        ModeSetting modeSetting2 = this.trailGlyph;
        BooleanSetting booleanSetting11 = this.projectiles;
        Objects.requireNonNull(booleanSetting11);
        modeSetting2.visibleWhen(booleanSetting11::get);
        NumberSetting numberSetting2 = this.trailDensity;
        BooleanSetting booleanSetting12 = this.projectiles;
        Objects.requireNonNull(booleanSetting12);
        numberSetting2.visibleWhen(booleanSetting12::get);
        ModeSetting modeSetting3 = this.stepGlyph;
        BooleanSetting booleanSetting13 = this.footsteps;
        Objects.requireNonNull(booleanSetting13);
        modeSetting3.visibleWhen(booleanSetting13::get);
        NumberSetting numberSetting3 = this.stepDensity;
        BooleanSetting booleanSetting14 = this.footsteps;
        Objects.requireNonNull(booleanSetting14);
        numberSetting3.visibleWhen(booleanSetting14::get);
        this.mainColor.visibleWhen(() -> {
            return this.colorMode.is(GlyphTint.MANUAL);
        });
        this.secondColor.visibleWhen(() -> {
            return this.colorMode.is(GlyphTint.MANUAL);
        });
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        reset();
        on(PacketReceiveEvent.TYPE, this::onPacket);
        on(TickEvent.TYPE, Stage.POST, this::onTick);
        on(Render3DEvent.TYPE, this::onRender);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        reset();
    }

    private void reset() {
        this.swarm.clear();
        this.boundLevel = null;
        this.lastFrame = 0L;
    }

    private boolean bind(class_638 level) {
        if (level == null) {
            if (this.boundLevel != null) {
                reset();
                return false;
            }
            return false;
        }
        if (level != this.boundLevel) {
            this.swarm.clear();
            this.boundLevel = level;
            return true;
        }
        return true;
    }

    private void onPacket(PacketReceiveEvent event) {
        if (this.pops.get()) {
            TotemPops.scan(event.packet, this.popSink);
        }
    }

    private void onTick(TickEvent event) {
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (!bind(mc.field_1687) || player == null) {
            return;
        }
        refreshPalette();
        if (this.projectiles.get()) {
            trails(mc.field_1687, player);
        }
        if (this.footsteps.get()) {
            steps(player);
        }
    }

    private void onRender(Render3DEvent event) {
        class_310 mc = class_310.method_1551();
        if (bind(mc.field_1687)) {
            long now = System.nanoTime();
            float dt = this.lastFrame == 0 ? 0.0f : Math.min(0.1f, (now - this.lastFrame) / 1.0E9f);
            this.lastFrame = now;
            if (mc.method_1493()) {
                dt = 0.0f;
            }
            this.swarm.trim(this.limit.getInt());
            this.swarm.step(dt);
            if (this.swarm.count() == 0) {
                return;
            }
            this.canvas.begin(mc.field_1773.method_19418());
            this.swarm.draw(this.canvas, 1.0f, this.halo.get());
            this.canvas.submit(false);
        }
    }

    private void burst(class_1309 entity) {
        class_310 mc = class_310.method_1551();
        if (bind(mc.field_1687) && entity.method_73183() == mc.field_1687) {
            if (entity != mc.field_1724 || this.ownPops.get()) {
                refreshPalette();
                class_238 box = entity.method_5829();
                int total = this.popBurst.getInt();
                String mode = this.popGlyph.get();
                for (int i = 0; i < total; i++) {
                    byte kind = pick(mode, i);
                    double px = box.field_1323 + (box.method_17939() * this.random.method_43058());
                    double py = box.field_1322 + (box.method_17940() * (0.15d + (0.8d * this.random.method_43058())));
                    double pz = box.field_1321 + (box.method_17941() * this.random.method_43058());
                    float heading = this.random.method_43057() * 6.2831855f;
                    float out = outward(kind) * (0.45f + this.random.method_43057());
                    float up = upward(kind) * (0.55f + (0.8f * this.random.method_43057()));
                    emit(kind, px, py, pz, class_3532.method_15362(heading) * out, up, class_3532.method_15374(heading) * out, 1.0f, 1.0f, colorFor(kind, -1));
                }
            }
        }
    }

    private void trails(class_638 level, class_746 player) {
        int density = this.trailDensity.getInt();
        String mode = this.trailGlyph.get();
        for (class_1297 entity : level.method_18112()) {
            int role = role(entity);
            if (role != -1) {
                double dx = entity.method_23317() - entity.field_6014;
                double dy = entity.method_23318() - entity.field_6036;
                double dz = entity.method_23321() - entity.field_5969;
                if ((dx * dx) + (dy * dy) + (dz * dz) >= RESTING_SQR && entity.method_5858(player) <= PROJECTILE_RANGE_SQR) {
                    double mid = ((double) entity.method_17682()) * 0.5d;
                    for (int i = 0; i < density; i++) {
                        float f = (i + this.random.method_43057()) / density;
                        byte kind = pick(mode, (entity.field_6012 * density) + i);
                        emit(kind, entity.field_6014 + (dx * ((double) f)) + ((double) jitter(0.06f)), entity.field_6036 + (dy * ((double) f)) + mid + ((double) jitter(0.06f)), entity.field_5969 + (dz * ((double) f)) + ((double) jitter(0.06f)), jitter(0.3f), jitter(0.3f) + 0.15f, jitter(0.3f), 0.55f, 0.8f, colorFor(kind, role));
                    }
                }
            }
        }
    }

    private void steps(class_746 player) {
        if (!player.method_24828() || player.method_7325() || player.method_5765() || player.method_52535()) {
            return;
        }
        double dx = player.method_23317() - player.field_6014;
        double dz = player.method_23321() - player.field_5969;
        double speed = Math.sqrt((dx * dx) + (dz * dz));
        if (speed < STEP_MIN_SPEED) {
            return;
        }
        int count = this.stepDensity.getInt() + (player.method_5624() ? 1 : 0);
        String mode = this.stepGlyph.get();
        for (int i = 0; i < count; i++) {
            byte kind = pick(mode, (player.field_6012 * count) + i);
            double back = 0.2d + (0.3d * this.random.method_43058());
            emit(kind, (player.method_23317() - ((dx / speed) * back)) + ((double) jitter(0.22f)), player.method_23318() + 0.04d, (player.method_23321() - ((dz / speed) * back)) + ((double) jitter(0.22f)), ((float) ((-dx) * 7.0d)) + jitter(0.5f), 0.8f + (this.random.method_43057() * 1.1f), ((float) ((-dz) * 7.0d)) + jitter(0.5f), 0.7f, 0.75f, colorFor(kind, -1));
        }
    }

    private void emit(byte kind, double x, double y, double z, float vx, float vy, float vz, float lifeScale, float sizeScale, int rgb) {
        float life = this.lifetime.get() * lifeScale * (0.75f + (0.5f * this.random.method_43057()));
        float scale = BASE_SIZE * this.size.get() * sizeScale * sizeOf(kind) * (0.8f + (0.4f * this.random.method_43057()));
        float turn = spinOf(kind) * (this.random.method_43056() ? 1.0f : -1.0f) * (0.5f + this.random.method_43057());
        float angle = kind == 0 ? jitter(0.35f) : this.random.method_43057() * 6.2831855f;
        this.swarm.spawn(kind, x, y, z, vx, vy, vz, life, scale, angle, turn, this.random.method_43057(), rgb, this.limit.getInt());
    }

    private void refreshPalette() {
        this.palette.resolve(this.colorMode.get(), CosmeticAccent.argb(), this.mainColor.argb(), this.secondColor.argb());
    }

    private int role(class_1297 entity) {
        if (entity.method_31481()) {
            return -1;
        }
        if (entity instanceof class_1684) {
            return this.pearls.get() ? 0 : -1;
        }
        if (entity instanceof class_1685) {
            return this.tridents.get() ? 2 : -1;
        }
        return ((entity instanceof class_1665) && this.arrows.get()) ? 1 : -1;
    }

    private static byte pick(String mode, int index) {
        switch (mode) {
            case "Ankh":
                return (byte) 0;
            case "Scarab":
                return (byte) 1;
            case "Sand":
                return (byte) 2;
            case "Spark":
                return (byte) 3;
            default:
                return MIXED_ORDER[Math.floorMod(index, MIXED_ORDER.length)];
        }
    }

    private int colorFor(byte kind, int role) {
        int iMix;
        float r = this.random.method_43057();
        switch (kind) {
            case 0:
                iMix = r >= 0.6f ? this.palette.highlight() : this.palette.primary();
                break;
            case 1:
                iMix = r >= 0.55f ? this.palette.companion() : this.palette.secondary();
                break;
            case 2:
                iMix = dust(r);
                break;
            default:
                iMix = GlyphTint.mix(this.palette.highlight(), -1, r * 0.35f);
                break;
        }
        int base = iMix;
        switch (role) {
            case 0:
                return GlyphTint.mix(base, this.palette.companion(), 0.55f);
            case 2:
                return GlyphTint.mix(base, this.palette.secondary(), 0.45f);
            default:
                return base;
        }
    }

    private int dust(float r) {
        int dust = this.palette.dust();
        if (r < 0.4f) {
            return dust;
        }
        return r < 0.75f ? GlyphTint.lift(dust, 0.4f) : GlyphTint.mix(dust, this.palette.secondary(), 0.3f);
    }

    private static float sizeOf(byte kind) {
        switch (kind) {
            case 0:
                return 1.35f;
            case 1:
                return 1.15f;
            case 2:
                return 0.5f;
            default:
                return 0.9f;
        }
    }

    private static float spinOf(byte kind) {
        switch (kind) {
            case 0:
                return 0.5f;
            case 1:
                return 1.1f;
            case 2:
                return 5.0f;
            default:
                return 2.8f;
        }
    }

    private static float outward(byte kind) {
        switch (kind) {
            case 0:
                return 0.8f;
            case 1:
                return 2.1f;
            case 2:
                return 1.5f;
            default:
                return 3.4f;
        }
    }

    private static float upward(byte kind) {
        switch (kind) {
            case 0:
                return 1.1f;
            case 1:
                return 1.7f;
            case 2:
                return 2.6f;
            default:
                return 2.4f;
        }
    }

    private float jitter(float amount) {
        return ((this.random.method_43057() * 2.0f) - 1.0f) * amount;
    }
}
