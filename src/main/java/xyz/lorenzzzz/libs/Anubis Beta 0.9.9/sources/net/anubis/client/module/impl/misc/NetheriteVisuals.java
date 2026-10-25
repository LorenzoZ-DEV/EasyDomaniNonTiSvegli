package net.anubis.client.module.impl.misc;

import imgui.ImDrawList;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import net.anubis.client.gui.WorldLabel;
import net.anubis.client.util.FoglessLines;
import net.anubis.client.util.TracerEngine;
import net.anubis.client.util.glyph.GlyphCanvas;
import net.anubis.client.util.glyph.GlyphShapes;
import net.anubis.client.util.glyph.GlyphSwarm;
import net.anubis.client.util.glyph.GlyphTint;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1109;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3417;
import net.minecraft.class_3532;
import net.minecraft.class_5819;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/NetheriteVisuals.class */
@Environment(EnvType.CLIENT)
final class NetheriteVisuals {
    static final String DOTS = "Dots";
    static final String CUBES = "Cubes";
    static final String PULSES = "Pulses";
    private static final long PULSE_NANOS = 1400000000;
    private static final float WAVE_SECONDS = 0.9f;
    private static final int MAX_WAVES = 12;
    private static final int GLOW_LIMIT = 32;
    private static final int DEBRIS_TRACERS = 3;
    private static final int LABEL_LIMIT = 8;
    private static final double PROBE_RADIUS = 12.0d;
    private static final int DOT_LIMIT = 1800;
    private static final int CUBE_LIMIT = 450;
    private static final double MISSING_RADIUS = 32.0d;
    private static final int MISSING_LIMIT = 1600;
    private static final float PROBE_FRESH_TICKS = 14.0f;
    private static final float[] SHELL_INFLATE = {0.06f, 0.16f, 0.3f};
    private static final float[] SHELL_ALPHA = {0.16f, 0.08f, 0.04f};
    private final GlyphCanvas canvas = new GlyphCanvas();
    private final GlyphSwarm swarm = new GlyphSwarm(128);
    private final class_5819 random = class_5819.method_43047();
    private final List<Wave> waves = new ArrayList();
    private final List<Label> labels = new ArrayList();
    private long lastFrame;
    private double pixel;

    NetheriteVisuals() {
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/NetheriteVisuals$Section.class */
    @Environment(EnvType.CLIENT)
    static final class Section extends Record {
        private final class_238 box;
        private final double distance;
        private final float progress;

        Section(class_238 box, double distance, float progress) {
            this.box = box;
            this.distance = distance;
            this.progress = progress;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Section.class), Section.class, "box;distance;progress", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Section;->box:Lnet/minecraft/class_238;", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Section;->distance:D", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Section;->progress:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Section.class), Section.class, "box;distance;progress", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Section;->box:Lnet/minecraft/class_238;", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Section;->distance:D", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Section;->progress:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Section.class, Object.class), Section.class, "box;distance;progress", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Section;->box:Lnet/minecraft/class_238;", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Section;->distance:D", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Section;->progress:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_238 box() {
            return this.box;
        }

        public double distance() {
            return this.distance;
        }

        public float progress() {
            return this.progress;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/NetheriteVisuals$Options.class */
    @Environment(EnvType.CLIENT)
    static final class Options extends Record {
        private final int near;
        private final int far;
        private final double fade;
        private final double range;
        private final String probeStyle;
        private final float missingFrom;
        private final int tracerCount;
        private final boolean showProbes;

        Options(int near, int far, double fade, double range, String probeStyle, float missingFrom, int tracerCount, boolean showProbes) {
            this.near = near;
            this.far = far;
            this.fade = fade;
            this.range = range;
            this.probeStyle = probeStyle;
            this.missingFrom = missingFrom;
            this.tracerCount = tracerCount;
            this.showProbes = showProbes;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Options.class), Options.class, "near;far;fade;range;probeStyle;missingFrom;tracerCount;showProbes", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->near:I", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->far:I", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->fade:D", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->range:D", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->probeStyle:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->missingFrom:F", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->tracerCount:I", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->showProbes:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Options.class), Options.class, "near;far;fade;range;probeStyle;missingFrom;tracerCount;showProbes", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->near:I", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->far:I", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->fade:D", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->range:D", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->probeStyle:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->missingFrom:F", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->tracerCount:I", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->showProbes:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Options.class, Object.class), Options.class, "near;far;fade;range;probeStyle;missingFrom;tracerCount;showProbes", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->near:I", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->far:I", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->fade:D", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->range:D", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->probeStyle:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->missingFrom:F", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->tracerCount:I", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Options;->showProbes:Z").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int near() {
            return this.near;
        }

        public int far() {
            return this.far;
        }

        public double fade() {
            return this.fade;
        }

        public double range() {
            return this.range;
        }

        public String probeStyle() {
            return this.probeStyle;
        }

        public float missingFrom() {
            return this.missingFrom;
        }

        public int tracerCount() {
            return this.tracerCount;
        }

        public boolean showProbes() {
            return this.showProbes;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/NetheriteVisuals$Wave.class */
    @Environment(EnvType.CLIENT)
    private static final class Wave extends Record {
        private final double x;
        private final double y;
        private final double z;
        private final long born;

        private Wave(double x, double y, double z, long born) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.born = born;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Wave.class), Wave.class, "x;y;z;born", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Wave;->x:D", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Wave;->y:D", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Wave;->z:D", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Wave;->born:J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Wave.class), Wave.class, "x;y;z;born", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Wave;->x:D", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Wave;->y:D", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Wave;->z:D", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Wave;->born:J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Wave.class, Object.class), Wave.class, "x;y;z;born", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Wave;->x:D", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Wave;->y:D", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Wave;->z:D", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Wave;->born:J").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public double x() {
            return this.x;
        }

        public double y() {
            return this.y;
        }

        public double z() {
            return this.z;
        }

        public long born() {
            return this.born;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/NetheriteVisuals$Label.class */
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
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Label.class), Label.class, "pos;primary;secondary", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Label;->pos:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Label;->primary:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Label;->secondary:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Label.class), Label.class, "pos;primary;secondary", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Label;->pos:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Label;->primary:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Label;->secondary:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Label.class, Object.class), Label.class, "pos;primary;secondary", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Label;->pos:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Label;->primary:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/NetheriteVisuals$Label;->secondary:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
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

    void clear() {
        this.swarm.clear();
        this.waves.clear();
        this.labels.clear();
        this.lastFrame = 0L;
    }

    void burst(class_2338 pos, int rgb) {
        double x = ((double) pos.method_10263()) + 0.5d;
        double y = ((double) pos.method_10264()) + 0.5d;
        double z = ((double) pos.method_10260()) + 0.5d;
        if (this.waves.size() >= 12) {
            this.waves.removeFirst();
        }
        this.waves.add(new Wave(x, y, z, System.nanoTime()));
        int light = GlyphTint.lift(rgb, 0.55f);
        for (int i = 0; i < 2; i++) {
            this.swarm.spawn((byte) 0, x + ((double) jitter(0.3f)), y + 0.6d, z + ((double) jitter(0.3f)), jitter(0.2f), 1.1f, jitter(0.2f), 1.8f, 0.22f, jitter(0.3f), jitter(0.5f), this.random.method_43057(), light, 128);
        }
        for (int i2 = 0; i2 < 10; i2++) {
            float angle = this.random.method_43057() * 6.2831855f;
            float speed = 2.2f + (this.random.method_43057() * 1.6f);
            int color = GlyphTint.mix(rgb, -1, this.random.method_43057() * 0.5f);
            this.swarm.spawn((byte) 3, x, y, z, class_3532.method_15362(angle) * speed, 1.2f + (this.random.method_43057() * 1.5f), class_3532.method_15374(angle) * speed, 0.8f, 0.09f, angle, 2.8f, this.random.method_43057(), color, 128);
        }
        class_310.method_1551().method_1483().method_4873(class_1109.method_4757(class_3417.field_26980, 1.3f, 0.9f));
    }

    void render(class_310 mc, float partialTick, class_243 eye, List<Section> sections, List<class_2338> revealed, AdvancedFinder finder, Options options) {
        long now = System.nanoTime();
        float dt = this.lastFrame == 0 ? 0.0f : Math.min(0.1f, (now - this.lastFrame) / 1.0E9f);
        this.lastFrame = now;
        this.swarm.step(dt);
        this.canvas.begin(mc.field_1773.method_19418());
        this.pixel = FoglessLines.pixelScale(partialTick);
        int accent = GlyphTint.alpha(options.near(), 1.0f);
        int light = GlyphTint.lift(accent, 0.55f);
        float pulse = 0.5f + (0.5f * class_3532.method_15374(((now % PULSE_NANOS) / 1.4E9f) * 6.2831855f));
        this.labels.clear();
        drawRevealed(eye, revealed, options, accent, light, pulse);
        drawWaves(now, accent, light);
        if (options.showProbes()) {
            drawProbes(eye, finder, options, accent, light, pulse);
        }
        drawSections(sections, options, light, pulse);
        drawTracers(sections, options);
        this.swarm.draw(this.canvas, 1.0f, true);
        this.canvas.submit(true);
    }

    void overlay(ImDrawList dl, float screenW, float screenH) {
        class_310 mc = class_310.method_1551();
        for (Label label : this.labels) {
            float[] screen = WorldLabel.project(mc, label.pos(), screenW, screenH);
            if (screen != null) {
                WorldLabel.drawText(dl, screen[0], screen[1], label.primary(), label.secondary(), 1.0f);
            }
        }
    }

    boolean hasLabels() {
        return !this.labels.isEmpty();
    }

    private static int tint(Options options, double distance) {
        float t = (float) class_3532.method_15350(distance / options.fade(), 0.0d, 1.0d);
        return GlyphTint.alpha(GlyphTint.mix(options.near(), options.far(), t), 1.0f);
    }

    private void drawSections(List<Section> sections, Options options, int light, float pulse) {
        for (Section section : sections) {
            if (section.progress() >= 0.0f) {
                drawScanning(section, light, pulse);
            }
        }
        for (Section section2 : sections) {
            if (section2.progress() < 0.0f) {
                drawCandidate(section2, options);
            }
        }
    }

    private void drawCandidate(Section section, Options options) {
        double far = class_3532.method_15350(section.distance() / options.fade(), 0.0d, 1.0d);
        int base = tint(options, section.distance());
        box(section.box(), GlyphTint.alpha(base, (float) (0.95d - (0.3d * far))), (float) (2.4d - (0.8d * far)));
        faces(section.box(), GlyphTint.alpha(base, (float) (0.07d - (0.03d * far))));
    }

    private void drawScanning(Section section, int light, float pulse) {
        class_238 box = section.box();
        box(box, GlyphTint.alpha(light, 0.6f + (0.4f * pulse)), 3.2f);
        faces(box, GlyphTint.alpha(light, 0.05f));
        double y = box.field_1322 + ((box.field_1325 - box.field_1322) * ((double) section.progress()));
        int plane = GlyphTint.alpha(light, 0.2f);
        this.canvas.quad(box.field_1323, y, box.field_1321, box.field_1320, y, box.field_1321, box.field_1320, y, box.field_1324, box.field_1323, y, box.field_1324, plane);
        this.canvas.quad(box.field_1323, y, box.field_1321, box.field_1323, y, box.field_1324, box.field_1320, y, box.field_1324, box.field_1320, y, box.field_1321, plane);
        int rim = GlyphTint.alpha(light, 0.95f);
        line(box.field_1323, y, box.field_1321, box.field_1320, y, box.field_1321, rim, 2.4f);
        line(box.field_1320, y, box.field_1321, box.field_1320, y, box.field_1324, rim, 2.4f);
        line(box.field_1320, y, box.field_1324, box.field_1323, y, box.field_1324, rim, 2.4f);
        line(box.field_1323, y, box.field_1324, box.field_1323, y, box.field_1321, rim, 2.4f);
    }

    private void drawTracers(List<Section> sections, Options options) {
        int drawn = 0;
        for (Section section : sections) {
            if (drawn >= options.tracerCount()) {
                return;
            }
            if (section.distance() >= 1.0d) {
                class_243 center = section.box().method_1005();
                TracerEngine.draw(center, GlyphTint.alpha(tint(options, section.distance()), 0.85f));
                String pct = section.progress() >= 0.0f ? Math.round(section.progress() * 100.0f) + "%" : null;
                this.labels.add(new Label(center, String.format(Locale.ROOT, "%.0fm", Double.valueOf(section.distance())), pct));
                drawn++;
            }
        }
    }

    private void drawProbes(class_243 eye, AdvancedFinder finder, Options options, int accent, int light, float pulse) {
        int missing = GlyphTint.mix(light, -1, 0.5f);
        finder.forEachMissing(eye, MISSING_RADIUS, MISSING_LIMIT, options.missingFrom(), (x, y, z) -> {
            this.canvas.sprite(GlyphShapes.DIAMOND, ((double) x) + 0.5d, ((double) y) + 0.5d, ((double) z) + 0.5d, 0.11f + (0.05f * pulse), 0.0f, GlyphTint.alpha(missing, 0.5f + (0.45f * pulse)));
        });
        int fresh = GlyphTint.mix(light, -1, 0.6f);
        String style = options.probeStyle();
        int limit = CUBES.equals(style) ? CUBE_LIMIT : DOT_LIMIT;
        finder.forEachProbe(eye, PROBE_RADIUS, limit, options.missingFrom(), (x2, y2, z2, age) -> {
            float t = Math.min(1.0f, age / 14.0f);
            int color = GlyphTint.mix(fresh, accent, t);
            double cx = ((double) x2) + 0.5d;
            double cy = ((double) y2) + 0.5d;
            double cz = ((double) z2) + 0.5d;
            if (CUBES.equals(style)) {
                double half = 0.2d - (0.1d * ((double) t));
                faces(new class_238(cx - half, cy - half, cz - half, cx + half, cy + half, cz + half), GlyphTint.alpha(color, 0.65f - (0.3f * t)));
                return;
            }
            this.canvas.sprite(GlyphShapes.DIAMOND, cx, cy, cz, 0.13f - (0.06f * t), 0.0f, GlyphTint.alpha(color, 0.95f - (0.5f * t)));
            if (PULSES.equals(style) && t < 1.0f) {
                this.canvas.sprite(GlyphShapes.HALO, cx, cy, cz, 0.15f + (0.55f * t), 0.0f, GlyphTint.alpha(fresh, 0.55f * (1.0f - t) * (1.0f - t)));
            }
        });
    }

    private void drawRevealed(class_243 eye, List<class_2338> revealed, Options options, int accent, int light, float pulse) {
        List<class_2338> sorted = new ArrayList<>(revealed);
        sorted.sort(Comparator.comparingDouble(pos -> {
            return pos.method_19770(eye);
        }));
        for (int i = 0; i < sorted.size(); i++) {
            class_2338 pos2 = sorted.get(i);
            double distance = Math.sqrt(pos2.method_19770(eye));
            if (distance <= options.range()) {
                class_238 box = new class_238(pos2);
                if (i < 32) {
                    for (int s = 0; s < SHELL_INFLATE.length; s++) {
                        faces(box.method_1014(SHELL_INFLATE[s]), GlyphTint.alpha(accent, SHELL_ALPHA[s] * (0.7f + (0.3f * pulse))));
                    }
                }
                faces(box, GlyphTint.alpha(accent, 0.3f));
                box(box, GlyphTint.alpha(light, 0.95f), 2.6f);
                class_243 center = box.method_1005();
                if (i < 3) {
                    TracerEngine.draw(center, GlyphTint.alpha(accent, 0.8f));
                }
                if (i < 8) {
                    this.labels.add(new Label(center.method_1031(0.0d, 0.9d, 0.0d), String.format(Locale.ROOT, "%.0fm", Double.valueOf(distance)), null));
                }
            } else {
                return;
            }
        }
    }

    private void drawWaves(long now, int accent, int light) {
        Iterator<Wave> iterator = this.waves.iterator();
        while (iterator.hasNext()) {
            Wave wave = iterator.next();
            float t = ((now - wave.born()) / 1.0E9f) / 0.9f;
            if (t >= 1.0f) {
                iterator.remove();
            } else {
                float ease = 1.0f - (((1.0f - t) * (1.0f - t)) * (1.0f - t));
                float radius = 0.4f + (2.6f * ease);
                float fade = (1.0f - t) * (1.0f - t);
                this.canvas.ring(wave.x(), wave.y(), wave.z(), radius * 0.86f, radius, 48, GlyphTint.alpha(light, fade * 0.9f));
                this.canvas.ring(wave.x(), wave.y(), wave.z(), radius, radius * 1.25f, 48, GlyphTint.alpha(accent, fade * 0.3f));
            }
        }
    }

    private void line(double ax, double ay, double az, double bx, double by, double bz, int argb, float width) {
        this.canvas.ribbon(ax, ay, az, bx, by, bz, argb, width, this.pixel);
    }

    private void box(class_238 b, int argb, float width) {
        line(b.field_1323, b.field_1322, b.field_1321, b.field_1320, b.field_1322, b.field_1321, argb, width);
        line(b.field_1320, b.field_1322, b.field_1321, b.field_1320, b.field_1322, b.field_1324, argb, width);
        line(b.field_1320, b.field_1322, b.field_1324, b.field_1323, b.field_1322, b.field_1324, argb, width);
        line(b.field_1323, b.field_1322, b.field_1324, b.field_1323, b.field_1322, b.field_1321, argb, width);
        line(b.field_1323, b.field_1325, b.field_1321, b.field_1320, b.field_1325, b.field_1321, argb, width);
        line(b.field_1320, b.field_1325, b.field_1321, b.field_1320, b.field_1325, b.field_1324, argb, width);
        line(b.field_1320, b.field_1325, b.field_1324, b.field_1323, b.field_1325, b.field_1324, argb, width);
        line(b.field_1323, b.field_1325, b.field_1324, b.field_1323, b.field_1325, b.field_1321, argb, width);
        line(b.field_1323, b.field_1322, b.field_1321, b.field_1323, b.field_1325, b.field_1321, argb, width);
        line(b.field_1320, b.field_1322, b.field_1321, b.field_1320, b.field_1325, b.field_1321, argb, width);
        line(b.field_1320, b.field_1322, b.field_1324, b.field_1320, b.field_1325, b.field_1324, argb, width);
        line(b.field_1323, b.field_1322, b.field_1324, b.field_1323, b.field_1325, b.field_1324, argb, width);
    }

    private void faces(class_238 b, int argb) {
        face(b.field_1323, b.field_1322, b.field_1321, b.field_1320, b.field_1322, b.field_1321, b.field_1320, b.field_1322, b.field_1324, b.field_1323, b.field_1322, b.field_1324, argb);
        face(b.field_1323, b.field_1325, b.field_1321, b.field_1320, b.field_1325, b.field_1321, b.field_1320, b.field_1325, b.field_1324, b.field_1323, b.field_1325, b.field_1324, argb);
        face(b.field_1323, b.field_1322, b.field_1321, b.field_1320, b.field_1322, b.field_1321, b.field_1320, b.field_1325, b.field_1321, b.field_1323, b.field_1325, b.field_1321, argb);
        face(b.field_1323, b.field_1322, b.field_1324, b.field_1320, b.field_1322, b.field_1324, b.field_1320, b.field_1325, b.field_1324, b.field_1323, b.field_1325, b.field_1324, argb);
        face(b.field_1323, b.field_1322, b.field_1321, b.field_1323, b.field_1322, b.field_1324, b.field_1323, b.field_1325, b.field_1324, b.field_1323, b.field_1325, b.field_1321, argb);
        face(b.field_1320, b.field_1322, b.field_1321, b.field_1320, b.field_1322, b.field_1324, b.field_1320, b.field_1325, b.field_1324, b.field_1320, b.field_1325, b.field_1321, argb);
    }

    private void face(double ax, double ay, double az, double bx, double by, double bz, double cx, double cy, double cz, double dx, double dy, double dz, int argb) {
        this.canvas.quad(ax, ay, az, bx, by, bz, cx, cy, cz, dx, dy, dz, argb);
        this.canvas.quad(ax, ay, az, dx, dy, dz, cx, cy, cz, bx, by, bz, argb);
    }

    private float jitter(float amount) {
        return ((this.random.method_43057() * 2.0f) - 1.0f) * amount;
    }
}
