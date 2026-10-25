package net.anubis.client.module.impl.render;

import com.example.client.mixin.AccessorBlockOverlayMultiPlayerGameMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.anubis.client.config.Surface;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.gui.EgyptDraw;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_12178;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_1297;
import net.minecraft.class_2244;
import net.minecraft.class_2281;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_259;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2741;
import net.minecraft.class_2745;
import net.minecraft.class_2756;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3726;
import net.minecraft.class_3965;
import net.minecraft.class_638;
import net.minecraft.class_9848;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/BlockOverlayModule.class */
@Environment(EnvType.CLIENT)
public final class BlockOverlayModule extends ClientModule {
    private static final String BOTH = "Both";
    private static final String OUTLINE = "Outline";
    private static final String FILL = "Fill";
    private static final String GRADIENT = "Gradient";
    private static final String SOLID = "Solid";
    private static final String RAINBOW = "Rainbow";
    private static final float FADE_IN = 0.12f;
    private static final float FADE_OUT = 0.2f;
    private static final double SNAP_DISTANCE = 6.0d;
    private static final float FLOW_SPEED = 0.35f;
    private static final double SEGMENT = 0.125d;
    private static final double FILL_INFLATE = 0.002d;
    private static final double LINE_LIFT = 0.004d;
    private static final double GLOW_LIFT = 0.002d;
    private final ModeSetting style;
    private final ModeSetting colorMode;
    private final ColorSetting color;
    private final ColorSetting secondColor;
    private final BooleanSetting flow;
    private final NumberSetting fillOpacity;
    private final NumberSetting width;
    private final BooleanSetting glow;
    private final BooleanSetting smooth;
    private final NumberSetting smoothSpeed;
    private final BooleanSetting miningProgress;
    private final BooleanSetting faceOnly;
    private final BooleanSetting seeThrough;
    private class_638 level;
    private class_2338 pos;
    private class_2680 state;
    private class_2338 partner;
    private class_2680 partnerState;
    private final List<double[]> edges;
    private List<class_238> boxes;
    private class_238 localBounds;
    private class_238 goal;
    private class_238 box;
    private class_2350 face;
    private class_238 faceBox;
    private float presence;
    private float progress;
    private long lastFrame;
    private class_243 eye;
    private double time;
    private float fade;
    private double ax;
    private double bx;
    private double ay;
    private double by;
    private double az;
    private double bz;

    public BlockOverlayModule() {
        super("block-overlay", "Block Overlay", "A smooth, animated outline and fill on the block you look at.", ModuleCategory.VISUALS);
        this.style = (ModeSetting) addSetting("style", new ModeSetting("Style", BOTH, BOTH, "Outline", FILL));
        this.colorMode = (ModeSetting) addSetting("color-mode", new ModeSetting("Color Mode", GRADIENT, GRADIENT, SOLID, RAINBOW));
        this.color = (ColorSetting) addSetting("color", new ColorSetting("Color", EgyptDraw.GOLD));
        this.secondColor = (ColorSetting) addSetting("second-color", new ColorSetting("Second Color", -6480));
        this.flow = (BooleanSetting) addSetting("flow", new BooleanSetting(Surface.BG_FLOW, true));
        this.fillOpacity = (NumberSetting) addSetting("fill-opacity", new NumberSetting("Fill Opacity", 20.0f, 2.0f, 60.0f).step(1.0f));
        this.width = (NumberSetting) addSetting("width", new NumberSetting("Width", 2.0f, 1.0f, 5.0f).step(0.5f));
        this.glow = (BooleanSetting) addSetting("glow", new BooleanSetting("Glow", true));
        this.smooth = (BooleanSetting) addSetting("smooth", new BooleanSetting("Smooth", true));
        this.smoothSpeed = (NumberSetting) addSetting("smooth-speed", new NumberSetting("Smooth Speed", 16.0f, 4.0f, 40.0f).step(1.0f));
        this.miningProgress = (BooleanSetting) addSetting("mining-progress", new BooleanSetting("Mining Progress", true));
        this.faceOnly = (BooleanSetting) addSetting("face-only", new BooleanSetting("Face Only", false));
        this.seeThrough = (BooleanSetting) addSetting("see-through", new BooleanSetting("See Through", false));
        this.edges = new ArrayList();
        this.boxes = List.of();
        this.secondColor.visibleWhen(() -> {
            return this.colorMode.is(GRADIENT);
        });
        this.flow.visibleWhen(() -> {
            return !this.colorMode.is(SOLID);
        });
        this.fillOpacity.visibleWhen(() -> {
            return !this.style.is("Outline");
        });
        this.width.visibleWhen(() -> {
            return !this.style.is(FILL);
        });
        this.glow.visibleWhen(() -> {
            return !this.style.is(FILL);
        });
        NumberSetting numberSetting = this.smoothSpeed;
        BooleanSetting booleanSetting = this.smooth;
        Objects.requireNonNull(booleanSetting);
        numberSetting.visibleWhen(booleanSetting::get);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        reset();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    @EventHandler
    public void onRender3D(Render3DEvent event) throws MatchException {
        class_310 mc = class_310.method_1551();
        if (mc.field_1687 == null || mc.field_1724 == null) {
            reset();
            return;
        }
        if (mc.field_1687 != this.level) {
            reset();
            this.level = mc.field_1687;
        }
        long now = System.nanoTime();
        float dt = this.lastFrame == 0 ? 0.0f : Math.min((now - this.lastFrame) / 1.0E9f, 0.1f);
        this.lastFrame = now;
        class_3965 hit = target(mc);
        if (hit != null && adopt(mc, hit)) {
            if (this.box == null || this.presence < 0.02f || !this.smooth.get() || this.box.method_1005().method_1022(this.goal.method_1005()) > SNAP_DISTANCE) {
                this.box = this.goal;
            } else {
                this.box = lerp(this.box, this.goal, 1.0f - ((float) Math.exp((-this.smoothSpeed.get()) * dt)));
            }
            this.presence = Math.min(1.0f, this.presence + (dt / FADE_IN));
        } else {
            hit = null;
            this.presence = Math.max(0.0f, this.presence - (dt / FADE_OUT));
            if (this.presence == 0.0f) {
                reset();
                this.level = mc.field_1687;
                return;
            }
        }
        if (this.box == null) {
            return;
        }
        updateProgress(mc, dt, hit != null);
        this.eye = mc.field_1773.method_19418().method_71156();
        this.time = (now % 3600000000000L) / 1.0E9d;
        draw();
    }

    private static class_3965 target(class_310 mc) {
        class_3965 class_3965Var = mc.field_1765;
        if (!(class_3965Var instanceof class_3965)) {
            return null;
        }
        class_3965 hit = class_3965Var;
        if (hit.method_17783() != class_239.class_240.field_1332 || hit.method_17781() || !mc.field_1773.anubis$shouldRenderBlockOutline()) {
            return null;
        }
        class_2338 at = hit.method_17777();
        if (mc.field_1687.method_8320(at).method_26215() || !mc.field_1687.method_8621().method_11952(at)) {
            return null;
        }
        return hit;
    }

    private boolean adopt(class_310 mc, class_3965 hit) {
        class_2338 class_2338Var;
        class_638 world = mc.field_1687;
        class_2338 at = hit.method_17777();
        class_2338 other = partner(world, at, world.method_8320(at));
        class_2338 ref = (other == null || other.method_10265(at) >= 0) ? at : other;
        if (other == null) {
            class_2338Var = null;
        } else {
            class_2338Var = ref.equals(at) ? other : at;
        }
        class_2338 second = class_2338Var;
        class_2680 refState = world.method_8320(ref);
        class_2680 secondState = second == null ? null : world.method_8320(second);
        if (!ref.equals(this.pos) || refState != this.state || secondState != this.partnerState) {
            class_1297 viewer = mc.method_1560();
            class_3726 context = viewer == null ? class_3726.method_16194() : class_3726.method_16195(viewer);
            class_265 shape = refState.method_26172(world, ref, context);
            if (second != null) {
                shape = class_259.method_1084(shape, secondState.method_26172(world, second, context).method_66507(second.method_10059(ref)));
            }
            class_265 shape2 = shape.method_1097();
            if (shape2.method_1110()) {
                return false;
            }
            if (!ref.equals(this.pos)) {
                this.progress = 0.0f;
            }
            this.pos = ref;
            this.state = refState;
            this.partner = second;
            this.partnerState = secondState;
            this.edges.clear();
            shape2.method_1104((x0, y0, z0, x1, y1, z1) -> {
                this.edges.add(new double[]{x0, y0, z0, x1, y1, z1});
            });
            this.boxes = shape2.method_1090();
            this.localBounds = shape2.method_1107();
            this.goal = this.localBounds.method_996(ref);
        }
        this.face = hit.method_17780();
        this.faceBox = boxOnFace(hit.method_17784().method_1023(this.pos.method_10263(), this.pos.method_10264(), this.pos.method_10260()));
        return true;
    }

    private static class_2338 partner(class_638 world, class_2338 at, class_2680 state) {
        class_2338 other = null;
        if (state.method_28498(class_2741.field_12533)) {
            other = state.method_11654(class_2741.field_12533) == class_2756.field_12607 ? at.method_10084() : at.method_10074();
        } else if (state.method_26204() instanceof class_2244) {
            other = at.method_10093(class_2244.method_24163(state));
        } else if ((state.method_26204() instanceof class_2281) && state.method_11654(class_2281.field_10770) != class_2745.field_12569) {
            other = class_2281.method_72581(at, state);
        }
        if (other == null || !world.method_8320(other).method_27852(state.method_26204())) {
            return null;
        }
        return other;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private class_238 boxOnFace(class_243 local) throws MatchException {
        boolean z;
        for (class_238 b : this.boxes) {
            switch (AnonymousClass1.$SwitchMap$net$minecraft$core$Direction[this.face.ordinal()]) {
                case 1:
                    z = Math.abs(local.field_1352 - b.field_1320) < 0.001d;
                    break;
                case 2:
                    z = Math.abs(local.field_1352 - b.field_1323) < 0.001d;
                    break;
                case 3:
                    z = Math.abs(local.field_1351 - b.field_1325) < 0.001d;
                    break;
                case 4:
                    z = Math.abs(local.field_1351 - b.field_1322) < 0.001d;
                    break;
                case 5:
                    z = Math.abs(local.field_1350 - b.field_1324) < 0.001d;
                    break;
                case 6:
                    z = Math.abs(local.field_1350 - b.field_1321) < 0.001d;
                    break;
                default:
                    throw new MatchException((String) null, (Throwable) null);
            }
            boolean onPlane = z;
            if (onPlane && b.method_1014(0.001d).method_1006(local)) {
                return b;
            }
        }
        return this.localBounds;
    }

    private void updateProgress(class_310 mc, float dt, boolean targeted) {
        AccessorBlockOverlayMultiPlayerGameMode mining;
        class_2338 breaking;
        float want = 0.0f;
        AccessorBlockOverlayMultiPlayerGameMode mining2 = mc.field_1761;
        if (targeted && this.miningProgress.get() && mining2 != null && mining2.method_2923() && (breaking = (mining = mining2).anubis$blockOverlayDestroyPos()) != null && (breaking.equals(this.pos) || breaking.equals(this.partner))) {
            want = class_3532.method_15363(mining.anubis$blockOverlayDestroyProgress(), 0.0f, 1.0f);
        }
        this.progress += (want - this.progress) * (1.0f - ((float) Math.exp((-18.0f) * dt)));
    }

    private void reset() {
        this.level = null;
        this.pos = null;
        this.state = null;
        this.partner = null;
        this.partnerState = null;
        this.edges.clear();
        this.boxes = List.of();
        this.localBounds = null;
        this.goal = null;
        this.box = null;
        this.face = null;
        this.faceBox = null;
        this.presence = 0.0f;
        this.progress = 0.0f;
        this.lastFrame = 0L;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private void draw() throws MatchException {
        this.fade = this.presence * this.presence * (3.0f - (2.0f * this.presence));
        double pop = 0.94d + (0.06d * ((double) this.fade));
        double[] x = axis(this.box.field_1323, this.box.field_1320, this.goal.field_1323, this.goal.field_1320, ((double) this.pos.method_10263()) - this.goal.field_1323, pop);
        double[] y = axis(this.box.field_1322, this.box.field_1325, this.goal.field_1322, this.goal.field_1325, ((double) this.pos.method_10264()) - this.goal.field_1322, pop);
        double[] z = axis(this.box.field_1321, this.box.field_1324, this.goal.field_1321, this.goal.field_1324, ((double) this.pos.method_10260()) - this.goal.field_1321, pop);
        this.ax = x[0];
        this.bx = x[1];
        this.ay = y[0];
        this.by = y[1];
        this.az = z[0];
        this.bz = z[1];
        boolean lines = !this.style.is(FILL);
        boolean fill = !this.style.is("Outline");
        float fillAlpha = this.fillOpacity.get() / 100.0f;
        if (fill) {
            if (this.faceOnly.get()) {
                faceFill(this.faceBox, this.face, fillAlpha, 0.002d);
            } else {
                for (class_238 b : this.boxes) {
                    boxFill(b, fillAlpha, 0.002d);
                }
            }
        }
        if (this.progress > 0.01f) {
            progressFill(Math.min(0.45f, (fillAlpha * 1.4f) + 0.16f));
        }
        if (lines) {
            outline(this.faceOnly.get() ? faceEdges(this.faceBox, this.face) : this.edges);
        }
    }

    private static double[] axis(double boxMin, double boxMax, double goalMin, double goalMax, double offset, double pop) {
        double goalSize = goalMax - goalMin;
        double scale = goalSize > 1.0E-6d ? (boxMax - boxMin) / goalSize : 1.0d;
        double centre = (boxMin + boxMax) * 0.5d;
        double a = (centre * (1.0d - pop)) + ((boxMin + (offset * scale)) * pop);
        return new double[]{a, scale * pop};
    }

    private class_243 world(double lx, double ly, double lz) {
        return new class_243(this.ax + (this.bx * lx), this.ay + (this.by * ly), this.az + (this.bz * lz));
    }

    private void outline(List<double[]> list) {
        boolean varies = !this.colorMode.is(SOLID);
        List<double[]> pieces = new ArrayList<>(list.size() * 4);
        List<Integer> colors = new ArrayList<>(list.size() * 4);
        for (double[] e : list) {
            double length = Math.sqrt(sq(e[3] - e[0]) + sq(e[4] - e[1]) + sq(e[5] - e[2]));
            int n = varies ? class_3532.method_15340((int) Math.ceil(length / SEGMENT), 1, 16) : 1;
            for (int i = 0; i < n; i++) {
                double t0 = ((double) i) / ((double) n);
                double t1 = ((double) (i + 1)) / ((double) n);
                double tm = (t0 + t1) * 0.5d;
                pieces.add(new double[]{class_3532.method_16436(t0, e[0], e[3]), class_3532.method_16436(t0, e[1], e[4]), class_3532.method_16436(t0, e[2], e[5]), class_3532.method_16436(t1, e[0], e[3]), class_3532.method_16436(t1, e[1], e[4]), class_3532.method_16436(t1, e[2], e[5])});
                colors.add(Integer.valueOf(colorAt(class_3532.method_16436(tm, e[0], e[3]), class_3532.method_16436(tm, e[1], e[4]), class_3532.method_16436(tm, e[2], e[5]))));
            }
        }
        float w = this.width.get();
        if (this.glow.get()) {
            for (int i2 = 0; i2 < pieces.size(); i2++) {
                double[] p = pieces.get(i2);
                int c = colors.get(i2).intValue();
                line(p, class_9848.method_75601(c, 0.34f * this.fade), w + 3.0f, 0.002d);
                line(p, class_9848.method_75601(c, 0.13f * this.fade), w + 8.0f, 0.002d);
            }
        }
        for (int i3 = 0; i3 < pieces.size(); i3++) {
            line(pieces.get(i3), class_9848.method_75601(colors.get(i3).intValue(), this.fade), w, LINE_LIFT);
        }
    }

    private void line(double[] p, int argb, float w, double lift) {
        class_12178 drawn = class_12180.method_75546(lift(world(p[0], p[1], p[2]), lift), lift(world(p[3], p[4], p[5]), lift), argb, w);
        if (this.seeThrough.get()) {
            drawn.method_75533();
        }
    }

    private class_243 lift(class_243 point, double by) {
        class_243 toEye = this.eye.method_1020(point);
        double length = toEye.method_1033();
        return length < 1.0E-6d ? point : point.method_1019(toEye.method_1021(by / length));
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private void boxFill(class_238 b, float alpha, double inflate) throws MatchException {
        for (class_2350 d : class_2350.values()) {
            faceFill(b, d, alpha, inflate);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private void faceFill(class_238 b, class_2350 d, float alpha, double inflate) throws MatchException {
        class_243 min = world(b.field_1323, b.field_1322, b.field_1321).method_1023(inflate, inflate, inflate);
        class_243 max = world(b.field_1320, b.field_1325, b.field_1324).method_1031(inflate, inflate, inflate);
        class_243 centre = faceCentre(b, d);
        int c = colorAt(centre.field_1352, centre.field_1351, centre.field_1350);
        int argb = (class_3532.method_15340(Math.round((alpha * this.fade) * 255.0f), 0, 255) << 24) | (c & 16777215);
        class_12178 drawn = class_12180.method_75548(min, max, d, class_12179.method_75539(argb));
        if (this.seeThrough.get()) {
            drawn.method_75533();
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private void progressFill(float alpha) throws MatchException {
        class_238 class_238Var;
        if (this.faceOnly.get()) {
            class_238 b = this.faceBox;
            if (this.face.method_10166() == class_2350.class_2351.field_11052) {
                class_238Var = b.method_35580(b.method_17939() * ((double) (1.0f - this.progress)) * 0.5d, 0.0d, b.method_17941() * ((double) (1.0f - this.progress)) * 0.5d);
            } else {
                class_238Var = new class_238(b.field_1323, b.field_1322, b.field_1321, b.field_1320, b.field_1322 + (b.method_17940() * ((double) this.progress)), b.field_1324);
            }
            class_238 part = class_238Var;
            faceFill(part, this.face, alpha, LINE_LIFT);
            return;
        }
        double top = this.localBounds.field_1322 + (this.localBounds.method_17940() * ((double) this.progress));
        for (class_238 b2 : this.boxes) {
            if (b2.field_1322 < top) {
                boxFill(new class_238(b2.field_1323, b2.field_1322, b2.field_1321, b2.field_1320, Math.min(b2.field_1325, top), b2.field_1324), alpha, LINE_LIFT);
            }
        }
    }

    private int colorAt(double lx, double ly, double lz) {
        int base = this.color.argb();
        if (this.colorMode.is(SOLID)) {
            return base;
        }
        double u = ((norm(lx, this.localBounds.field_1323, this.localBounds.field_1320) + norm(ly, this.localBounds.field_1322, this.localBounds.field_1325)) + norm(lz, this.localBounds.field_1321, this.localBounds.field_1324)) / 3.0d;
        double shift = this.flow.get() ? this.time * 0.3499999940395355d : 0.0d;
        if (this.colorMode.is(RAINBOW)) {
            double hue = (u * 0.5d) - shift;
            return (base & (-16777216)) | (class_3532.method_15369((float) (hue - Math.floor(hue)), 0.65f, 1.0f) & 16777215);
        }
        double k = this.flow.get() ? 0.5d - (0.5d * Math.cos(6.283185307179586d * (u - shift))) : u;
        return class_9848.method_61319((float) k, base, this.secondColor.argb());
    }

    private static double norm(double v, double min, double max) {
        if (max - min < 1.0E-6d) {
            return 0.5d;
        }
        return (v - min) / (max - min);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private static class_243 faceCentre(class_238 b, class_2350 d) throws MatchException {
        class_243 c = b.method_1005();
        switch (AnonymousClass1.$SwitchMap$net$minecraft$core$Direction[d.ordinal()]) {
            case 1:
                return new class_243(b.field_1320, c.field_1351, c.field_1350);
            case 2:
                return new class_243(b.field_1323, c.field_1351, c.field_1350);
            case 3:
                return new class_243(c.field_1352, b.field_1325, c.field_1350);
            case 4:
                return new class_243(c.field_1352, b.field_1322, c.field_1350);
            case 5:
                return new class_243(c.field_1352, c.field_1351, b.field_1324);
            case 6:
                return new class_243(c.field_1352, c.field_1351, b.field_1321);
            default:
                throw new MatchException((String) null, (Throwable) null);
        }
    }

    /* JADX INFO: renamed from: net.anubis.client.module.impl.render.BlockOverlayModule$1, reason: invalid class name */
    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/BlockOverlayModule$1.class */
    @Environment(EnvType.CLIENT)
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$net$minecraft$core$Direction;
        static final /* synthetic */ int[] $SwitchMap$net$minecraft$core$Direction$Axis = new int[class_2350.class_2351.values().length];

        static {
            try {
                $SwitchMap$net$minecraft$core$Direction$Axis[class_2350.class_2351.field_11048.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$net$minecraft$core$Direction$Axis[class_2350.class_2351.field_11052.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$net$minecraft$core$Direction$Axis[class_2350.class_2351.field_11051.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $SwitchMap$net$minecraft$core$Direction = new int[class_2350.values().length];
            try {
                $SwitchMap$net$minecraft$core$Direction[class_2350.field_11034.ordinal()] = 1;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$net$minecraft$core$Direction[class_2350.field_11039.ordinal()] = 2;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$net$minecraft$core$Direction[class_2350.field_11036.ordinal()] = 3;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$net$minecraft$core$Direction[class_2350.field_11033.ordinal()] = 4;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$net$minecraft$core$Direction[class_2350.field_11035.ordinal()] = 5;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$net$minecraft$core$Direction[class_2350.field_11043.ordinal()] = 6;
            } catch (NoSuchFieldError e9) {
            }
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private static List<double[]> faceEdges(class_238 b, class_2350 d) throws MatchException {
        double[][] dArr;
        switch (AnonymousClass1.$SwitchMap$net$minecraft$core$Direction$Axis[d.method_10166().ordinal()]) {
            case 1:
                double x = d == class_2350.field_11034 ? b.field_1320 : b.field_1323;
                dArr = new double[][]{new double[]{x, b.field_1322, b.field_1321}, new double[]{x, b.field_1325, b.field_1321}, new double[]{x, b.field_1325, b.field_1324}, new double[]{x, b.field_1322, b.field_1324}};
                break;
            case 2:
                double y = d == class_2350.field_11036 ? b.field_1325 : b.field_1322;
                dArr = new double[][]{new double[]{b.field_1323, y, b.field_1321}, new double[]{b.field_1320, y, b.field_1321}, new double[]{b.field_1320, y, b.field_1324}, new double[]{b.field_1323, y, b.field_1324}};
                break;
            case 3:
                double z = d == class_2350.field_11035 ? b.field_1324 : b.field_1321;
                dArr = new double[][]{new double[]{b.field_1323, b.field_1322, z}, new double[]{b.field_1320, b.field_1322, z}, new double[]{b.field_1320, b.field_1325, z}, new double[]{b.field_1323, b.field_1325, z}};
                break;
            default:
                throw new MatchException((String) null, (Throwable) null);
        }
        double[][] c = dArr;
        List<double[]> edges = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            double[] p = c[i];
            double[] q = c[(i + 1) % 4];
            edges.add(new double[]{p[0], p[1], p[2], q[0], q[1], q[2]});
        }
        return edges;
    }

    private static class_238 lerp(class_238 from, class_238 to, float k) {
        return new class_238(class_3532.method_16436(k, from.field_1323, to.field_1323), class_3532.method_16436(k, from.field_1322, to.field_1322), class_3532.method_16436(k, from.field_1321, to.field_1321), class_3532.method_16436(k, from.field_1320, to.field_1320), class_3532.method_16436(k, from.field_1325, to.field_1325), class_3532.method_16436(k, from.field_1324, to.field_1324));
    }

    private static double sq(double v) {
        return v * v;
    }
}
