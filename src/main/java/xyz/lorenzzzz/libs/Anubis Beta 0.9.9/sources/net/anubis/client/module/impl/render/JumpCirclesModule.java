package net.anubis.client.module.impl.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.List;
import java.util.Objects;
import net.anubis.client.addon.AddonManager;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1011;
import net.minecraft.class_1049;
import net.minecraft.class_10799;
import net.minecraft.class_12075;
import net.minecraft.class_12245;
import net.minecraft.class_12247;
import net.minecraft.class_12253;
import net.minecraft.class_1309;
import net.minecraft.class_1921;
import net.minecraft.class_243;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_4588;
import net.minecraft.class_4597;
import net.minecraft.class_4725;
import net.minecraft.class_638;
import net.minecraft.class_742;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/JumpCirclesModule.class */
@Environment(EnvType.CLIENT)
public final class JumpCirclesModule extends ClientModule {
    public static volatile JumpCirclesModule active;
    private static final String COLOR_ACCENT = "Accent";
    private static final String COLOR_CUSTOM = "Custom";
    private static final int MAX_RINGS = 24;
    private static final int SEGMENTS = 64;
    private static final double LIFT = 0.015d;
    private static final double MAX_DISTANCE_SQR = 16384.0d;
    private static final double JUMP_RISE = 0.2d;
    private static final int RISE_TICKS = 6;
    private static final int SWEEP_TICKS = 100;
    private static final float CORE_WHITE = 0.45f;
    private static final float CORE_ALPHA = 0.95f;
    private static final float HALO_ALPHA = 0.55f;
    private static final float FADE_IN_NANOS = 8.0E7f;
    private static final float LOGO_FILL = 0.75f;
    private static final float LOGO_SPAN = 0.9277344f;
    private static final float LOGO_DROP = 0.0126953125f;
    private static final float LOGO_GLOW_SCALE = 1.5f;
    private final ModeSetting colorMode;
    private final ColorSetting color;
    private final NumberSetting size;
    private final NumberSetting duration;
    private final BooleanSetting logo;
    private final BooleanSetting others;
    private final double[] ringX;
    private final double[] ringY;
    private final double[] ringZ;
    private final float[] ringFacingX;
    private final float[] ringFacingZ;
    private final long[] ringBorn;
    private int head;
    private int count;
    private float poseX;
    private float poseY;
    private float poseZ;
    private float poseT;
    private float poseRadius;
    private float poseFade;
    private final Int2ObjectOpenHashMap<Lift> lifts;
    private int tick;
    private int levelKey;
    public static final class_2960 LOGO_TEXTURE = class_2960.method_60655(AddonManager.ENTRYPOINT, "textures/misc/jump_circle_logo.png");
    public static final class_2960 LOGO_GLOW_TEXTURE = class_2960.method_60655(AddonManager.ENTRYPOINT, "textures/misc/jump_circle_logo_glow.png");
    private static final float[] COS = new float[65];
    private static final float[] SIN = new float[65];

    static {
        for (int i = 0; i <= 64; i++) {
            double angle = (6.283185307179586d * ((double) i)) / 64.0d;
            COS[i] = (float) Math.cos(angle);
            SIN[i] = (float) Math.sin(angle);
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/JumpCirclesModule$Lift.class */
    @Environment(EnvType.CLIENT)
    private static final class Lift {
        double x;
        double y;
        double z;
        float yaw;
        int airTicks;
        boolean judged;
        int seen;

        private Lift() {
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/JumpCirclesModule$Ring.class */
    @Environment(EnvType.CLIENT)
    private static final class Ring {
        static final class_1921 TYPE = class_1921.method_75940("anubis_jump_circles", class_12247.method_75927(class_10799.field_56865).method_75930(class_12245.field_63976).method_75938());

        private Ring() {
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/JumpCirclesModule$Logo.class */
    @Environment(EnvType.CLIENT)
    private static final class Logo {
        static final RenderPipeline PIPELINE = RenderPipeline.builder(new RenderPipeline.Snippet[]{class_10799.field_60125}).withLocation(class_2960.method_60655(AddonManager.ENTRYPOINT, "pipeline/jump_circle_logo")).withVertexShader("core/position_tex_color").withFragmentShader("core/position_tex_color").withSampler("Sampler0").withBlend(BlendFunction.TRANSLUCENT).withDepthWrite(false).withCull(false).withVertexFormat(class_290.field_1575, VertexFormat.class_5596.field_27382).build();
        static final class_1921 TYPE = class_1921.method_75940("anubis_jump_circle_logo", class_12247.method_75927(PIPELINE).method_75934("Sampler0", JumpCirclesModule.LOGO_TEXTURE).method_75930(class_12245.field_63976).method_75938());
        static final class_1921 GLOW_TYPE = class_1921.method_75940("anubis_jump_circle_logo_glow", class_12247.method_75927(PIPELINE).method_75934("Sampler0", JumpCirclesModule.LOGO_GLOW_TEXTURE).method_75930(class_12245.field_63976).method_75938());

        private Logo() {
        }

        static {
            class_310.method_1551().method_1531().method_65876(JumpCirclesModule.LOGO_TEXTURE, new LogoTexture(JumpCirclesModule.LOGO_TEXTURE));
            class_310.method_1551().method_1531().method_65876(JumpCirclesModule.LOGO_GLOW_TEXTURE, new LogoTexture(JumpCirclesModule.LOGO_GLOW_TEXTURE));
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/JumpCirclesModule$LogoTexture.class */
    @Environment(EnvType.CLIENT)
    private static final class LogoTexture extends class_1049 {
        LogoTexture(class_2960 id) {
            super(id);
        }

        protected void method_65856(class_1011 image) {
            int levels = 32 - Integer.numberOfLeadingZeros(Math.min(image.method_4307(), image.method_4323()));
            class_1011[] mips = class_4725.method_24102(method_65859(), new class_1011[]{image}, levels - 1, class_12253.field_64077, 0.0f);
            try {
                GpuDevice device = RenderSystem.getDevice();
                close();
                class_2960 class_2960VarMethod_65859 = method_65859();
                Objects.requireNonNull(class_2960VarMethod_65859);
                this.field_56974 = device.createTexture(class_2960VarMethod_65859::toString, 5, TextureFormat.RGBA8, image.method_4307(), image.method_4323(), 1, levels);
                this.field_60597 = device.createTextureView(this.field_56974);
                CommandEncoder encoder = device.createCommandEncoder();
                for (int level = 0; level < levels; level++) {
                    class_1011 mip = mips[level];
                    encoder.writeToTexture(this.field_56974, mip, level, 0, 0, 0, mip.method_4307(), mip.method_4323(), 0, 0);
                }
                for (int level2 = 1; level2 < mips.length; level2++) {
                    mips[level2].close();
                }
                this.field_63613 = RenderSystem.getSamplerCache().method_76520(FilterMode.LINEAR, true);
            } catch (Throwable th) {
                for (int level3 = 1; level3 < mips.length; level3++) {
                    mips[level3].close();
                }
                throw th;
            }
        }
    }

    public JumpCirclesModule() {
        super("jump-circles", "Jump Circles", "Draws a fading Anubis ring where you jump.", ModuleCategory.VISUALS);
        this.colorMode = (ModeSetting) addSetting("color-mode", new ModeSetting("Color", COLOR_ACCENT, COLOR_ACCENT, COLOR_CUSTOM));
        this.color = (ColorSetting) addSetting("color", new ColorSetting("Custom Color", -1853860));
        this.size = (NumberSetting) addSetting("size", new NumberSetting("Size", 1.2f, 0.5f, 3.0f).step(0.1f));
        this.duration = (NumberSetting) addSetting("duration", new NumberSetting("Duration", 1.0f, 0.3f, 3.0f).step(0.1f));
        this.logo = (BooleanSetting) addSetting("logo", new BooleanSetting("Logo", true));
        this.others = (BooleanSetting) addSetting("others", new BooleanSetting("Others", false));
        this.ringX = new double[24];
        this.ringY = new double[24];
        this.ringZ = new double[24];
        this.ringFacingX = new float[24];
        this.ringFacingZ = new float[24];
        this.ringBorn = new long[24];
        this.lifts = new Int2ObjectOpenHashMap<>();
        this.color.visibleWhen(() -> {
            return this.colorMode.is(COLOR_CUSTOM);
        });
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        clear();
        active = this;
        on(TickEvent.TYPE, Stage.POST, this::onTick);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        active = null;
        clear();
    }

    private void clear() {
        this.head = 0;
        this.count = 0;
        this.lifts.clear();
        this.levelKey = 0;
    }

    public static void onJump(class_1309 entity) {
        JumpCirclesModule module = active;
        if (module == null) {
            return;
        }
        class_310 mc = class_310.method_1551();
        if (entity != mc.field_1724 || mc.field_1687 == null) {
            return;
        }
        module.syncLevel(mc.field_1687);
        module.spawn(entity.method_23317(), entity.method_23318(), entity.method_23321(), entity.method_36454());
    }

    private void spawn(double x, double y, double z, float yaw) {
        if (this.count == 24) {
            this.head = (this.head + 1) % 24;
            this.count--;
        }
        int slot = (this.head + this.count) % 24;
        this.ringX[slot] = x;
        this.ringY[slot] = y;
        this.ringZ[slot] = z;
        double heading = Math.toRadians(yaw);
        this.ringFacingX[slot] = (float) (-Math.sin(heading));
        this.ringFacingZ[slot] = (float) Math.cos(heading);
        this.ringBorn[slot] = System.nanoTime();
        this.count++;
    }

    private void expire(long now) {
        long life = durationNanos();
        while (this.count > 0 && now - this.ringBorn[this.head] >= life) {
            this.head = (this.head + 1) % 24;
            this.count--;
        }
    }

    private long durationNanos() {
        return (long) (((double) this.duration.get()) * 1.0E9d);
    }

    private void syncLevel(class_638 level) {
        int key = System.identityHashCode(level);
        if (key == this.levelKey) {
            return;
        }
        this.levelKey = key;
        this.head = 0;
        this.count = 0;
        this.lifts.clear();
    }

    private void onTick(TickEvent event) {
        class_310 mc = class_310.method_1551();
        class_638 level = mc.field_1687;
        if (level == null) {
            clear();
            return;
        }
        syncLevel(level);
        if (!this.others.get()) {
            if (!this.lifts.isEmpty()) {
                this.lifts.clear();
                return;
            }
            return;
        }
        this.tick++;
        List<class_742> players = level.method_18456();
        for (int i = 0; i < players.size(); i++) {
            class_742 class_742Var = players.get(i);
            if (class_742Var != mc.field_1724) {
                Lift lift = (Lift) this.lifts.get(class_742Var.method_5628());
                if (lift == null) {
                    lift = new Lift();
                    lift.airTicks = 6;
                    lift.judged = true;
                    this.lifts.put(class_742Var.method_5628(), lift);
                }
                lift.seen = this.tick;
                track(class_742Var, lift);
            }
        }
        if (this.tick % 100 == 0) {
            int now = this.tick;
            this.lifts.values().removeIf(lift2 -> {
                return lift2.seen != now;
            });
        }
    }

    private void track(class_742 player, Lift lift) {
        if (player.method_24828()) {
            lift.x = player.method_23317();
            lift.y = player.method_23318();
            lift.z = player.method_23321();
            lift.yaw = player.method_36454();
            lift.airTicks = -1;
            lift.judged = false;
            return;
        }
        if (lift.judged) {
            return;
        }
        lift.airTicks++;
        if (player.method_23318() - lift.y > JUMP_RISE) {
            lift.judged = true;
            if (canLeaveRing(player)) {
                spawn(lift.x, lift.y, lift.z, lift.yaw);
                return;
            }
            return;
        }
        if (lift.airTicks >= 6) {
            lift.judged = true;
        }
    }

    private static boolean canLeaveRing(class_742 player) {
        return (player.method_5765() || player.method_6128() || player.method_52535() || player.method_6101() || player.field_6235 != 0 || player.method_7325() || player.method_5767()) ? false : true;
    }

    public static void renderLevel(class_12075 camera) {
        JumpCirclesModule module = active;
        if (module == null || module.count <= 0 || !camera.field_63079) {
            return;
        }
        module.render(camera.field_63078);
    }

    private void render(class_243 camera) {
        long now = System.nanoTime();
        expire(now);
        if (this.count == 0) {
            return;
        }
        int argb = this.colorMode.is(COLOR_CUSTOM) ? this.color.argb() : CosmeticAccent.argb();
        int rgb = argb & 16777215;
        float radiusMax = this.size.get();
        float life = durationNanos();
        float core = 0.025f + (0.02f * radiusMax);
        int coreRgb = towardsWhite(rgb, CORE_WHITE);
        class_4597.class_4598 buffers = class_310.method_1551().method_22940().method_23000();
        if (this.logo.get()) {
            float alpha = (argb >>> 24) / 255.0f;
            class_4588 out = buffers.method_73477(Logo.GLOW_TYPE);
            for (int n = 0; n < this.count; n++) {
                int slot = (this.head + n) % 24;
                if (pose(slot, now, life, radiusMax, camera)) {
                    logo(out, slot, this.poseRadius - core, LOGO_GLOW_SCALE, withAlpha(rgb, alpha * HALO_ALPHA * this.poseFade));
                }
            }
            buffers.method_22994(Logo.GLOW_TYPE);
            class_4588 out2 = buffers.method_73477(Logo.TYPE);
            for (int n2 = 0; n2 < this.count; n2++) {
                int slot2 = (this.head + n2) % 24;
                if (pose(slot2, now, life, radiusMax, camera)) {
                    logo(out2, slot2, this.poseRadius - core, 1.0f, withAlpha(coreRgb, alpha * CORE_ALPHA * this.poseFade));
                }
            }
            buffers.method_22994(Logo.TYPE);
        }
        class_4588 out3 = buffers.method_73477(Ring.TYPE);
        for (int n3 = 0; n3 < this.count; n3++) {
            if (pose((this.head + n3) % 24, now, life, radiusMax, camera)) {
                float glow = (0.08f + (0.08f * radiusMax)) * (1.0f - (0.35f * this.poseT));
                int halo = withAlpha(rgb, HALO_ALPHA * this.poseFade);
                int line = withAlpha(coreRgb, CORE_ALPHA * this.poseFade);
                float cx = this.poseX;
                float cy = this.poseY;
                float cz = this.poseZ;
                float radius = this.poseRadius;
                float in = radius - core;
                float outR = radius + core;
                band(out3, cx, cy, cz, Math.max(0.0f, in - (glow * 0.7f)), rgb, in, halo);
                band(out3, cx, cy, cz, in, halo, radius, line);
                band(out3, cx, cy, cz, radius, line, outR, halo);
                band(out3, cx, cy, cz, outR, halo, outR + glow, rgb);
            }
        }
        buffers.method_22994(Ring.TYPE);
    }

    private boolean pose(int slot, long now, float life, float radiusMax, class_243 camera) {
        float cx = (float) (this.ringX[slot] - camera.field_1352);
        float cy = (float) ((this.ringY[slot] + LIFT) - camera.field_1351);
        float cz = (float) (this.ringZ[slot] - camera.field_1350);
        if ((((double) cx) * ((double) cx)) + (((double) cy) * ((double) cy)) + (((double) cz) * ((double) cz)) > MAX_DISTANCE_SQR) {
            return false;
        }
        long age = now - this.ringBorn[slot];
        float t = Math.min(1.0f, age / life);
        float open = 1.0f - (((1.0f - t) * (1.0f - t)) * (1.0f - t));
        float fade = (1.0f - t) * (1.0f - t) * Math.min(1.0f, age / FADE_IN_NANOS);
        if (fade <= 0.004f) {
            return false;
        }
        this.poseX = cx;
        this.poseY = cy;
        this.poseZ = cz;
        this.poseT = t;
        this.poseRadius = radiusMax * (0.18f + (0.82f * open));
        this.poseFade = fade;
        return true;
    }

    private void logo(class_4588 out, int slot, float inner, float scale, int argb) {
        if (inner <= 0.0f) {
            return;
        }
        float half = (LOGO_FILL * inner) / LOGO_SPAN;
        float fx = this.ringFacingX[slot];
        float fz = this.ringFacingZ[slot];
        float lead = 0.025390625f * half;
        float x = this.poseX + (fx * lead);
        float y = this.poseY;
        float z = this.poseZ + (fz * lead);
        float half2 = half * scale;
        float ax = (fx + fz) * half2;
        float az = (fz - fx) * half2;
        float bx = (fx - fz) * half2;
        float bz = (fz + fx) * half2;
        out.method_22912(x + ax, y, z + az).method_22913(0.0f, 0.0f).method_39415(argb);
        out.method_22912(x + bx, y, z + bz).method_22913(1.0f, 0.0f).method_39415(argb);
        out.method_22912(x - ax, y, z - az).method_22913(1.0f, 1.0f).method_39415(argb);
        out.method_22912(x - bx, y, z - bz).method_22913(0.0f, 1.0f).method_39415(argb);
    }

    private static void band(class_4588 out, float x, float y, float z, float r0, int c0, float r1, int c1) {
        for (int i = 0; i < 64; i++) {
            float cosA = COS[i];
            float sinA = SIN[i];
            float cosB = COS[i + 1];
            float sinB = SIN[i + 1];
            out.method_22912(x + (cosA * r0), y, z + (sinA * r0)).method_39415(c0);
            out.method_22912(x + (cosB * r0), y, z + (sinB * r0)).method_39415(c0);
            out.method_22912(x + (cosB * r1), y, z + (sinB * r1)).method_39415(c1);
            out.method_22912(x + (cosA * r1), y, z + (sinA * r1)).method_39415(c1);
        }
    }

    private static int withAlpha(int rgb, float alpha) {
        int a = (int) ((Math.max(0.0f, Math.min(1.0f, alpha)) * 255.0f) + 0.5f);
        return (a << 24) | rgb;
    }

    private static int towardsWhite(int rgb, float amount) {
        int r = (rgb >> 16) & 255;
        int g = (rgb >> 8) & 255;
        int b = rgb & 255;
        return ((r + ((int) ((255 - r) * amount))) << 16) | ((g + ((int) ((255 - g) * amount))) << 8) | (b + ((int) ((255 - b) * amount)));
    }
}
