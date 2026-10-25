package net.anubis.client.module.impl.render;

import imgui.ImDrawList;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.gui.WorldLabel;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.HudOverlayProvider;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.impl.misc.NameProtectModule;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.util.PearlTrajectorySimulator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_1297;
import net.minecraft.class_1684;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3965;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/PearlTrajectoryModule.class */
@Environment(EnvType.CLIENT)
public final class PearlTrajectoryModule extends ClientModule implements HudOverlayProvider {
    private static final int MAX_TRAIL_POINTS = 200;
    private static final double MIN_TRAIL_DISTANCE_SQ = 1.0E-4d;
    private static final double PIECE_LENGTH = 0.45d;
    private static final double FADE_IN_DISTANCE = 1.8d;
    private static final int RING_SEGMENTS = 48;
    private static final float POP_IN_SECONDS = 0.22f;
    private static final float WAVE_PERIOD_SECONDS = 1.4f;
    private static final long LABEL_MAX_AGE_NANOS = 250000000;
    private static final long EPOCH = System.nanoTime();
    private static final String LANDING_RING = "Ring";
    private static final String LANDING_BOX = "Box";
    private static final String LANDING_NONE = "None";
    private final Map<Integer, List<class_243>> trails;
    private final List<Label> labels;
    private long labelsAt;
    private long predictionShownAt;
    private final BooleanSetting sPredict;
    private final BooleanSetting sTrail;
    private final BooleanSetting sOthers;
    private final ModeSetting sLanding;
    private final BooleanSetting sInfo;
    private final BooleanSetting sGlow;
    private final BooleanSetting sGradient;
    private final ColorSetting sColor;
    private final ColorSetting sEndColor;
    private final ColorSetting sHitColor;
    private final NumberSetting sLineWidth;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/PearlTrajectoryModule$Label.class */
    @Environment(EnvType.CLIENT)
    private static final class Label extends Record {
        private final class_243 pos;
        private final String primary;
        private final String secondary;

        private Label(class_243 pos, String primary, String secondary) {
            this.pos = pos;
            this.primary = primary;
            this.secondary = secondary;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Label.class), Label.class, "pos;primary;secondary", "FIELD:Lnet/anubis/client/module/impl/render/PearlTrajectoryModule$Label;->pos:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/module/impl/render/PearlTrajectoryModule$Label;->primary:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/render/PearlTrajectoryModule$Label;->secondary:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Label.class), Label.class, "pos;primary;secondary", "FIELD:Lnet/anubis/client/module/impl/render/PearlTrajectoryModule$Label;->pos:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/module/impl/render/PearlTrajectoryModule$Label;->primary:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/render/PearlTrajectoryModule$Label;->secondary:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Label.class, Object.class), Label.class, "pos;primary;secondary", "FIELD:Lnet/anubis/client/module/impl/render/PearlTrajectoryModule$Label;->pos:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/module/impl/render/PearlTrajectoryModule$Label;->primary:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/render/PearlTrajectoryModule$Label;->secondary:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_243 pos() {
            return this.pos;
        }

        public String primary() {
            return this.primary;
        }

        public String secondary() {
            return this.secondary;
        }
    }

    public PearlTrajectoryModule() {
        super("pearl-trajectory", "Pearl Trajectory", "Predicts where a held pearl lands and previews the flight of thrown pearls.", ModuleCategory.RENDER);
        this.trails = new HashMap();
        this.labels = new ArrayList();
        this.predictionShownAt = -1L;
        this.sPredict = (BooleanSetting) addSetting("predict", new BooleanSetting("Predict", true));
        this.sTrail = (BooleanSetting) addSetting("trail", new BooleanSetting("Trail", true));
        this.sOthers = (BooleanSetting) addSetting("others-pearls", new BooleanSetting("Others' Pearls", false));
        this.sLanding = (ModeSetting) addSetting("landing", new ModeSetting("Landing", LANDING_RING, LANDING_RING, LANDING_BOX, "None"));
        this.sInfo = (BooleanSetting) addSetting("landing-info", new BooleanSetting("Landing Info", true));
        this.sGlow = (BooleanSetting) addSetting("glow", new BooleanSetting("Glow", true));
        this.sGradient = (BooleanSetting) addSetting("gradient", new BooleanSetting("Gradient", true));
        this.sColor = (ColorSetting) addSetting("color", new ColorSetting("Color", -9524994));
        this.sEndColor = (ColorSetting) addSetting("end-color", new ColorSetting("End Color", -3697942));
        this.sHitColor = (ColorSetting) addSetting("hit-color", new ColorSetting("Hit Color", -42406));
        this.sLineWidth = (NumberSetting) addSetting("line-width", new NumberSetting("Line Width", 2.0f, 0.5f, 5.0f).step(0.5f));
        ColorSetting colorSetting = this.sEndColor;
        BooleanSetting booleanSetting = this.sGradient;
        Objects.requireNonNull(booleanSetting);
        colorSetting.visibleWhen(booleanSetting::get);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        this.trails.clear();
        this.labels.clear();
        this.predictionShownAt = -1L;
    }

    @EventHandler
    public void onTick(TickEvent event) {
        class_310 mc = class_310.method_1551();
        if (mc.field_1687 == null || mc.field_1724 == null) {
            this.trails.clear();
            return;
        }
        Set<Integer> active = new HashSet<>();
        for (class_1297 entity : mc.field_1687.method_18112()) {
            if (entity instanceof class_1684) {
                class_1684 pearl = (class_1684) entity;
                if (isTracked(pearl, mc)) {
                    active.add(Integer.valueOf(pearl.method_5628()));
                    List<class_243> points = this.trails.computeIfAbsent(Integer.valueOf(pearl.method_5628()), ignored -> {
                        return new ArrayList();
                    });
                    class_243 position = pearl.method_73189();
                    if (points.isEmpty() || ((class_243) points.getLast()).method_1025(position) >= MIN_TRAIL_DISTANCE_SQ) {
                        points.add(position);
                        if (points.size() > MAX_TRAIL_POINTS) {
                            points.removeFirst();
                        }
                    }
                }
            }
        }
        this.trails.keySet().retainAll(active);
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        this.labels.clear();
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return;
        }
        long now = System.nanoTime();
        this.labelsAt = now;
        float time = (now - EPOCH) / 1.0E9f;
        float width = this.sLineWidth.get();
        class_243 eye = mc.field_1724.method_5836(event.partialTick);
        if (this.sPredict.get() && PearlTrajectorySimulator.isHoldingPearl(mc.field_1724)) {
            if (this.predictionShownAt < 0) {
                this.predictionShownAt = now;
            }
            float pop = easeOutBack(Math.min(1.0f, ((now - this.predictionShownAt) / 1.0E9f) / POP_IN_SECONDS));
            PearlTrajectorySimulator.SimulationResult result = PearlTrajectorySimulator.simulate(mc.field_1687, mc.field_1724, event.partialTick);
            drawPath(result.points(), width, true, false, 1.0f);
            drawLanding(result, pop, time, width, 1.0f);
            addLabel(result, eye, null);
        } else {
            this.predictionShownAt = -1L;
        }
        for (class_1297 entity : mc.field_1687.method_18112()) {
            if (entity instanceof class_1684) {
                class_1684 pearl = (class_1684) entity;
                if (isTracked(pearl, mc)) {
                    class_243 head = pearl.method_30950(event.partialTick);
                    if (this.sTrail.get()) {
                        drawTrail(this.trails.get(Integer.valueOf(pearl.method_5628())), head, width);
                    }
                    PearlTrajectorySimulator.SimulationResult rest = PearlTrajectorySimulator.simulate(mc.field_1687, pearl);
                    List<class_243> ahead = new ArrayList<>(rest.points());
                    ahead.set(0, head);
                    drawPath(ahead, width * 0.8f, false, true, 0.7f);
                    drawLanding(rest, 1.0f, time, width, 0.75f);
                    class_746 class_746VarMethod_24921 = pearl.method_24921();
                    boolean own = class_746VarMethod_24921 == null || class_746VarMethod_24921 == mc.field_1724;
                    addLabel(rest, eye, own ? null : NameProtectModule.protect(class_746VarMethod_24921.method_5477()).getString());
                }
            }
        }
    }

    private void drawPath(List<class_243> points, float width, boolean fadeIn, boolean dashed, float alphaMul) {
        if (points.size() < 2) {
            return;
        }
        double total = 0.0d;
        for (int i = 1; i < points.size(); i++) {
            total += points.get(i - 1).method_1022(points.get(i));
        }
        if (total <= 1.0E-6d) {
            return;
        }
        int start = this.sColor.argb();
        int end = this.sGradient.get() ? this.sEndColor.argb() : start;
        boolean glow = this.sGlow.get();
        double travelled = 0.0d;
        int piece = 0;
        for (int i2 = 1; i2 < points.size(); i2++) {
            class_243 a = points.get(i2 - 1);
            class_243 b = points.get(i2);
            double length = a.method_1022(b);
            int pieces = Math.max(1, (int) Math.ceil(length / PIECE_LENGTH));
            int p = 0;
            while (p < pieces) {
                if (!dashed || (piece & 1) != 1) {
                    double t0 = ((double) p) / ((double) pieces);
                    double t1 = ((double) (p + 1)) / ((double) pieces);
                    double mid = travelled + (length * (t0 + t1) * 0.5d);
                    float alpha = alphaMul;
                    if (fadeIn) {
                        alpha *= smoothstep((float) (mid / FADE_IN_DISTANCE));
                    }
                    if (alpha >= 0.02f) {
                        int color = scaleAlpha(lerpArgb(start, end, (float) (mid / total)), alpha);
                        class_243 from = a.method_35590(b, t0);
                        class_243 to = a.method_35590(b, t1);
                        if (glow) {
                            class_12180.method_75546(from, to, scaleAlpha(color, 0.2f), width * 3.2f).method_75533();
                        }
                        class_12180.method_75546(from, to, color, width).method_75533();
                    }
                }
                p++;
                piece++;
            }
            travelled += length;
        }
    }

    private void drawTrail(List<class_243> points, class_243 head, float width) {
        if (points == null || points.isEmpty()) {
            return;
        }
        int color = this.sColor.argb();
        int count = points.size() + 1;
        int i = 1;
        while (i < count) {
            class_243 from = points.get(i - 1);
            class_243 to = i < points.size() ? points.get(i) : head;
            float alpha = 0.15f + ((0.85f * i) / (count - 1.0f));
            class_12180.method_75546(from, to, scaleAlpha(color, alpha), width).method_75533();
            i++;
        }
    }

    private void drawLanding(PearlTrajectorySimulator.SimulationResult result, float scale, float time, float width, float alphaMul) {
        int hitColor = scaleAlpha(this.sHitColor.argb(), alphaMul);
        if (result.entityHit() != null) {
            class_12180.method_75541(result.entityHit().method_5829().method_1014(0.05d), class_12179.method_75537(hitColor, width, scaleAlpha(hitColor, 0.14f))).method_75533();
            return;
        }
        class_3965 hit = result.blockHit();
        String mode = this.sLanding.get();
        if (hit == null || "None".equals(mode)) {
            return;
        }
        int color = scaleAlpha(this.sGradient.get() ? this.sEndColor.argb() : this.sColor.argb(), alphaMul);
        class_2350 face = hit.method_17780();
        class_243 normal = new class_243(face.method_10148(), face.method_10164(), face.method_10165());
        if (LANDING_BOX.equals(mode)) {
            class_238 box = new class_238(hit.method_17777()).method_1014(0.002d);
            class_12180.method_75548(box.method_61125(), box.method_61126(), face, class_12179.method_75537(color, width, scaleAlpha(color, POP_IN_SECONDS))).method_75533();
            return;
        }
        class_243 centre = result.landing().method_1019(normal.method_1021(0.02d));
        float pulse = 1.0f + (0.05f * ((float) Math.sin(time * 5.0f)));
        drawRing(centre, normal, 0.38f * scale * pulse, color, width);
        drawCross(centre, normal, 0.09f * scale, color, width);
        float phase = (time % WAVE_PERIOD_SECONDS) / WAVE_PERIOD_SECONDS;
        float waveRadius = lerp(0.3f, 0.95f, easeOutCubic(phase)) * scale;
        drawRing(centre, normal, waveRadius, scaleAlpha(color, 0.75f * (1.0f - phase)), width * 0.8f);
    }

    private static void drawRing(class_243 centre, class_243 normal, float radius, int color, float width) {
        if (((color >>> 24) & 255) < 4 || radius <= 0.0f) {
            return;
        }
        class_243[] basis = planeBasis(normal);
        class_243 previous = null;
        for (int i = 0; i <= 48; i++) {
            double angle = (6.283185307179586d * ((double) i)) / 48.0d;
            class_243 point = centre.method_1019(basis[0].method_1021(Math.cos(angle) * ((double) radius))).method_1019(basis[1].method_1021(Math.sin(angle) * ((double) radius)));
            if (previous != null) {
                class_12180.method_75546(previous, point, color, width).method_75533();
            }
            previous = point;
        }
    }

    private static void drawCross(class_243 centre, class_243 normal, float half, int color, float width) {
        class_243[] basis = planeBasis(normal);
        class_12180.method_75546(centre.method_1019(basis[0].method_1021(-half)), centre.method_1019(basis[0].method_1021(half)), color, width).method_75533();
        class_12180.method_75546(centre.method_1019(basis[1].method_1021(-half)), centre.method_1019(basis[1].method_1021(half)), color, width).method_75533();
    }

    private static class_243[] planeBasis(class_243 normal) {
        class_243 u = Math.abs(normal.field_1351) > 0.9d ? new class_243(1.0d, 0.0d, 0.0d) : normal.method_1036(new class_243(0.0d, 1.0d, 0.0d)).method_1029();
        return new class_243[]{u, normal.method_1036(u).method_1029()};
    }

    private void addLabel(PearlTrajectorySimulator.SimulationResult result, class_243 eye, String owner) {
        String secondary;
        if (this.sInfo.get() && result.landed()) {
            String time = String.format(Locale.ROOT, "%.1fs", Float.valueOf(result.ticks() / 20.0f));
            String primary = String.format(Locale.ROOT, "%.1fm", Double.valueOf(eye.method_1022(result.landing())));
            if (result.entityHit() != null) {
                secondary = NameProtectModule.protect(result.entityHit().method_5477()).getString() + "  ·  " + time;
            } else {
                secondary = owner != null ? owner + "  ·  " + time : time;
            }
            this.labels.add(new Label(result.landing(), primary, secondary));
        }
    }

    private boolean isTracked(class_1684 pearl, class_310 mc) {
        if (pearl.method_31481()) {
            return false;
        }
        class_746 class_746VarMethod_24921 = pearl.method_24921();
        return class_746VarMethod_24921 == null || class_746VarMethod_24921 == mc.field_1724 || this.sOthers.get();
    }

    @Override // net.anubis.client.module.HudOverlayProvider
    public boolean isHudOverlayEnabled() {
        return isEnabled() && this.sInfo.get();
    }

    @Override // net.anubis.client.module.HudOverlayProvider
    public void renderHudOverlay(ImDrawList dl, float screenW, float screenH) {
        if (this.labels.isEmpty() || System.nanoTime() - this.labelsAt > LABEL_MAX_AGE_NANOS) {
            return;
        }
        class_310 mc = class_310.method_1551();
        for (Label label : this.labels) {
            float[] screen = WorldLabel.project(mc, label.pos().method_1031(0.0d, 0.35d, 0.0d), screenW, screenH);
            if (screen != null) {
                WorldLabel.drawText(dl, screen[0], screen[1], label.primary(), label.secondary(), 1.0f);
            }
        }
    }

    private static int lerpArgb(int a, int b, float t) {
        float t2 = Math.max(0.0f, Math.min(1.0f, t));
        int aa = a >>> 24;
        int ar = (a >> 16) & 255;
        int ag = (a >> 8) & 255;
        int ab = a & 255;
        int ba = b >>> 24;
        int br = (b >> 16) & 255;
        int bg = (b >> 8) & 255;
        int bb = b & 255;
        return (Math.round(aa + ((ba - aa) * t2)) << 24) | (Math.round(ar + ((br - ar) * t2)) << 16) | (Math.round(ag + ((bg - ag) * t2)) << 8) | Math.round(ab + ((bb - ab) * t2));
    }

    private static int scaleAlpha(int argb, float mul) {
        int alpha = Math.round((argb >>> 24) * Math.max(0.0f, Math.min(1.0f, mul)));
        return (alpha << 24) | (argb & 16777215);
    }

    private static float lerp(float a, float b, float t) {
        return a + ((b - a) * t);
    }

    private static float smoothstep(float t) {
        float t2 = Math.max(0.0f, Math.min(1.0f, t));
        return t2 * t2 * (3.0f - (2.0f * t2));
    }

    private static float easeOutCubic(float t) {
        float inv = 1.0f - t;
        return 1.0f - ((inv * inv) * inv);
    }

    private static float easeOutBack(float t) {
        float c3 = 1.70158f + 1.0f;
        float u = t - 1.0f;
        return 1.0f + (c3 * u * u * u) + (1.70158f * u * u);
    }
}
