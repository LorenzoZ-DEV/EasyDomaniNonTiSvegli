package net.anubis.client.module.impl.render;

import imgui.ImDrawList;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import net.anubis.client.config.Surface;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.gui.WorldLabel;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.HudOverlayProvider;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.impl.misc.NameProtectModule;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.module.setting.Setting;
import net.anubis.client.util.Ballistics;
import net.anubis.client.util.combat.CombatTarget;
import net.anubis.client.util.glyph.GlyphCanvas;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_12178;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1665;
import net.minecraft.class_1676;
import net.minecraft.class_1680;
import net.minecraft.class_1681;
import net.minecraft.class_1683;
import net.minecraft.class_1684;
import net.minecraft.class_1685;
import net.minecraft.class_1686;
import net.minecraft.class_1753;
import net.minecraft.class_1764;
import net.minecraft.class_1771;
import net.minecraft.class_1776;
import net.minecraft.class_1779;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1811;
import net.minecraft.class_1823;
import net.minecraft.class_1835;
import net.minecraft.class_1890;
import net.minecraft.class_1937;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_4537;
import net.minecraft.class_638;
import net.minecraft.class_746;
import net.minecraft.class_9278;
import net.minecraft.class_9284;
import net.minecraft.class_9334;
import net.minecraft.class_9701;
import net.minecraft.class_9848;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/TrajectoriesModule.class */
@Environment(EnvType.CLIENT)
public final class TrajectoriesModule extends ClientModule implements HudOverlayProvider {
    private static final String PATH_BEADS = "Beads";
    private static final String PATH_DASHES = "Dashes";
    private static final String PATH_SOLID = "Solid";
    private static final String COLOR_THEME = "Theme";
    private static final String COLOR_MANUAL = "Manual";
    private static final String MARKER_ANKH = "Ankh";
    private static final String MARKER_SUN = "Sun Disk";
    private static final String MARKER_NONE = "None";
    private static final int WHITE = -1;
    private static final float HEAD_LIFT = 0.35f;
    private static final float SHEEN_LIFT = 0.8f;
    private static final float PLATE_LIFT = 0.55f;
    private static final float HIT_LIFT = 0.5f;
    private static final int VOLLEY_CAP = 3;
    private static final int TRACK_CAP = 24;
    private static final int TRACK_LIFETIME = 200;
    private static final double RESTING_SPEED_SQR = 1.0E-4d;
    private static final double AIM_DRIFT = 0.001d;
    private static final double AIM_SWAY = 5.0E-4d;
    private static final float BOW_MIN_PULL = 0.1f;
    private static final float BOW_SPEED = 3.0f;
    private static final int TRIDENT_WINDUP = 10;
    private static final float CROSSBOW_ARROW_SPEED = 3.15f;
    private static final float CROSSBOW_ROCKET_SPEED = 1.6f;
    private static final float MULTISHOT_FAN = 10.0f;
    private static final float BOTTLE_SPEED = 0.7f;
    private static final float LOB_TILT = -20.0f;
    private static final double MUZZLE_DROP = 0.1d;
    private static final double ROCKET_DROP = 0.15d;
    private static final int ROCKET_FUSE_SLACK = 6;
    private static final double BEAD_PERIOD = 0.42d;
    private static final double BEAD_ANGLE = 0.011d;
    private static final double BEAD_MIN = 0.035d;
    private static final double BEAD_MAX = 0.2d;
    private static final double DASH_PERIOD = 0.72d;
    private static final double DASH_LENGTH = 0.44d;
    private static final double SOLID_PIECE = 0.6d;
    private static final double FLOW_SPEED = 1.3d;
    private static final double FADE_IN = 1.4d;
    private static final double SHEEN_SPEED = 9.0d;
    private static final double SHEEN_REACH = 1.6d;
    private static final double SHEEN_REST = 14.0d;
    private static final float HALO_ALPHA = 0.22f;
    private static final float HALO_WIDTH = 2.8f;
    private static final double HALO_NEAR = 24.0d;
    private static final double HALO_FAR = 40.0d;
    private static final int AIM_PIECES = 200;
    private static final int TRACK_PIECES = 110;
    private static final float GHOST_ALPHA = 0.38f;
    private static final float STRAY_ALPHA = 0.7f;
    private static final float FLYING_ALPHA = 0.85f;
    private static final float UNFURL_SECONDS = 0.32f;
    private static final double MARKER_UNIT = 0.5d;
    private static final double MARKER_LIFT = 0.025d;
    private static final int RIM_STEPS = 24;
    private static final int ORBIT_ARCS = 10;
    private static final int SUN_RAYS = 12;
    private static final long TARGET_MEMORY_MS = 6000;
    private static final long LABEL_TTL_NANOS = 250000000;
    private static final String PEARL_PREDICT_ID = "predict";
    private static final String PEARL_OTHERS_ID = "others-pearls";
    private final BooleanSetting aimPreview;
    private final BooleanSetting ghostArc;
    private final BooleanSetting inFlight;
    private final BooleanSetting othersShots;
    private final NumberSetting range;
    private final BooleanSetting bows;
    private final BooleanSetting tridents;
    private final BooleanSetting throwables;
    private final BooleanSetting pearls;
    private final ModeSetting pathStyle;
    private final BooleanSetting flow;
    private final ModeSetting colorMode;
    private final ColorSetting pathColor;
    private final NumberSetting thickness;
    private final BooleanSetting halo;
    private final BooleanSetting throughWalls;
    private final ModeSetting marker;
    private final NumberSetting markerScale;
    private final BooleanSetting targetBox;
    private final ColorSetting hitColor;
    private final BooleanSetting flightTime;
    private final Aim[] volley;
    private int volleySize;
    private boolean volleyGhost;
    private class_1792 aimItem;
    private long aimSince;
    private long markerSince;
    private final Int2ObjectOpenHashMap<Track> tracks;
    private final ArrayDeque<Track> spareTracks;
    private int sweep;
    private class_638 level;
    private final GlyphCanvas canvas;
    private final List<Label> labels;
    private long labelsAt;
    private int headTint;
    private int tailTint;
    private int haloTint;
    private int markTint;
    private int ringTint;
    private int sheenTint;
    private int plateTint;
    private int hitTint;
    private boolean onTop;
    private class_243 camera;
    private double strokeTotal;
    private double strokeSheen;
    private float strokeAlpha;
    private float strokeWidth;
    private boolean strokeFade;
    private boolean strokeHalo;
    private double gx;
    private double gy;
    private double gz;
    private double rx;
    private double ry;
    private double rz;
    private double ux;
    private double uy;
    private double uz;
    private static final long EPOCH = System.nanoTime();
    private static final double[] RIM_COS = new double[25];
    private static final double[] RIM_SIN = new double[25];
    private static final double DIAGONAL = 1.0d / Math.sqrt(3.0d);
    private static final double[][] BURST_RAYS = {new double[]{1.0d, 0.0d, 0.0d}, new double[]{-1.0d, 0.0d, 0.0d}, new double[]{0.0d, 1.0d, 0.0d}, new double[]{0.0d, -1.0d, 0.0d}, new double[]{0.0d, 0.0d, 1.0d}, new double[]{0.0d, 0.0d, -1.0d}, new double[]{DIAGONAL, DIAGONAL, DIAGONAL}, new double[]{-DIAGONAL, DIAGONAL, DIAGONAL}, new double[]{DIAGONAL, -DIAGONAL, DIAGONAL}, new double[]{DIAGONAL, DIAGONAL, -DIAGONAL}, new double[]{-DIAGONAL, -DIAGONAL, DIAGONAL}, new double[]{-DIAGONAL, DIAGONAL, -DIAGONAL}, new double[]{DIAGONAL, -DIAGONAL, -DIAGONAL}, new double[]{-DIAGONAL, -DIAGONAL, -DIAGONAL}};

    static {
        for (int i = 0; i <= 24; i++) {
            double angle = (6.283185307179586d * ((double) i)) / HALO_NEAR;
            RIM_COS[i] = Math.cos(angle);
            RIM_SIN[i] = Math.sin(angle);
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/TrajectoriesModule$Label.class */
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
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Label.class), Label.class, "pos;primary;secondary", "FIELD:Lnet/anubis/client/module/impl/render/TrajectoriesModule$Label;->pos:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/module/impl/render/TrajectoriesModule$Label;->primary:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/render/TrajectoriesModule$Label;->secondary:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Label.class), Label.class, "pos;primary;secondary", "FIELD:Lnet/anubis/client/module/impl/render/TrajectoriesModule$Label;->pos:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/module/impl/render/TrajectoriesModule$Label;->primary:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/render/TrajectoriesModule$Label;->secondary:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Label.class, Object.class), Label.class, "pos;primary;secondary", "FIELD:Lnet/anubis/client/module/impl/render/TrajectoriesModule$Label;->pos:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/module/impl/render/TrajectoriesModule$Label;->primary:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/render/TrajectoriesModule$Label;->secondary:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
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

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/TrajectoriesModule$Plot.class */
    @Environment(EnvType.CLIENT)
    private static class Plot {
        final Ballistics.Flight flight = new Ballistics.Flight();
        String seconds;
        String detail;

        private Plot() {
        }

        void caption(class_243 from, boolean wanted) {
            this.seconds = null;
            this.detail = null;
            if (!wanted || this.flight.outcome() == Ballistics.Outcome.OPEN || this.flight.size() < 2) {
                return;
            }
            class_1297 struck = this.flight.struck();
            this.seconds = String.format(Locale.ROOT, "%.2fs", Float.valueOf(this.flight.ticks() / 20.0f));
            this.detail = struck != null ? NameProtectModule.protect(struck.method_5477()).getString() : String.format(Locale.ROOT, "%.0fm", Double.valueOf(from.method_1022(this.flight.impact())));
        }

        void forget() {
            this.flight.clear();
            this.seconds = null;
            this.detail = null;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/TrajectoriesModule$Track.class */
    @Environment(EnvType.CLIENT)
    private static final class Track extends Plot {
        class_1676 shot;
        int seen;
        boolean departed;

        private Track() {
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/TrajectoriesModule$Aim.class */
    @Environment(EnvType.CLIENT)
    private static final class Aim extends Plot {
        Ballistics.Body body;
        double x;
        double y;
        double z;
        double vx;
        double vy;
        double vz;
        int life;
        int stamp;
        boolean held;

        private Aim() {
        }

        boolean matches(Ballistics.Body body, class_243 from, class_243 motion, int life, int stamp) {
            return this.held && this.stamp == stamp && this.body == body && this.life == life && Math.abs(this.x - from.field_1352) < TrajectoriesModule.AIM_DRIFT && Math.abs(this.y - from.field_1351) < TrajectoriesModule.AIM_DRIFT && Math.abs(this.z - from.field_1350) < TrajectoriesModule.AIM_DRIFT && Math.abs(this.vx - motion.field_1352) < TrajectoriesModule.AIM_SWAY && Math.abs(this.vy - motion.field_1351) < TrajectoriesModule.AIM_SWAY && Math.abs(this.vz - motion.field_1350) < TrajectoriesModule.AIM_SWAY;
        }

        void hold(Ballistics.Body body, class_243 from, class_243 motion, int life, int stamp) {
            this.body = body;
            this.x = from.field_1352;
            this.y = from.field_1351;
            this.z = from.field_1350;
            this.vx = motion.field_1352;
            this.vy = motion.field_1351;
            this.vz = motion.field_1350;
            this.life = life;
            this.stamp = stamp;
            this.held = true;
        }

        @Override // net.anubis.client.module.impl.render.TrajectoriesModule.Plot
        void forget() {
            super.forget();
            this.held = false;
        }
    }

    public TrajectoriesModule() {
        super("trajectories", "Trajectories", "Shows where your projectiles will land.", ModuleCategory.RENDER);
        this.aimPreview = (BooleanSetting) addSetting("aim-preview", new BooleanSetting("Aim Preview", true));
        this.ghostArc = (BooleanSetting) addSetting("ghost-arc", new BooleanSetting("Ghost Arc", true));
        this.inFlight = (BooleanSetting) addSetting("in-flight", new BooleanSetting("In Flight", true));
        this.othersShots = (BooleanSetting) addSetting("others-shots", new BooleanSetting("Others' Shots", true));
        this.range = (NumberSetting) addSetting("range", new NumberSetting("Range", 64.0f, 16.0f, 160.0f).step(8.0f));
        this.bows = (BooleanSetting) addSetting("bows", new BooleanSetting("Bows & Crossbows", true));
        this.tridents = (BooleanSetting) addSetting("tridents", new BooleanSetting("Tridents", true));
        this.throwables = (BooleanSetting) addSetting("throwables", new BooleanSetting("Throwables", true));
        this.pearls = (BooleanSetting) addSetting("pearls", new BooleanSetting("Ender Pearls", true));
        this.pathStyle = (ModeSetting) addSetting("path-style", new ModeSetting("Path", PATH_BEADS, PATH_BEADS, PATH_DASHES, PATH_SOLID));
        this.flow = (BooleanSetting) addSetting("flow", new BooleanSetting(Surface.BG_FLOW, true));
        this.colorMode = (ModeSetting) addSetting("color", new ModeSetting("Color", "Theme", "Theme", "Manual"));
        this.pathColor = (ColorSetting) addSetting("path-color", new ColorSetting("Path Color", -13338378));
        this.thickness = (NumberSetting) addSetting("thickness", new NumberSetting("Thickness", 2.25f, 0.5f, 5.0f).step(0.25f));
        this.halo = (BooleanSetting) addSetting("halo", new BooleanSetting("Halo", true));
        this.throughWalls = (BooleanSetting) addSetting("through-walls", new BooleanSetting("Through Walls", true));
        this.marker = (ModeSetting) addSetting("marker", new ModeSetting("Marker", MARKER_ANKH, MARKER_ANKH, MARKER_SUN, "None"));
        this.markerScale = (NumberSetting) addSetting("marker-scale", new NumberSetting("Marker Scale", 1.0f, 0.5f, 2.0f).step(BOW_MIN_PULL));
        this.targetBox = (BooleanSetting) addSetting("target-box", new BooleanSetting("Target Box", true));
        this.hitColor = (ColorSetting) addSetting("hit-color", new ColorSetting("Hit Color", -6635270));
        this.flightTime = (BooleanSetting) addSetting("flight-time", new BooleanSetting("Flight Time", true));
        this.volley = new Aim[3];
        this.aimSince = -1L;
        this.markerSince = -1L;
        this.tracks = new Int2ObjectOpenHashMap<>();
        this.spareTracks = new ArrayDeque<>();
        this.canvas = new GlyphCanvas();
        this.labels = new ArrayList();
        this.camera = class_243.field_1353;
        for (int i = 0; i < 3; i++) {
            this.volley[i] = new Aim();
        }
        BooleanSetting booleanSetting = this.ghostArc;
        BooleanSetting booleanSetting2 = this.aimPreview;
        Objects.requireNonNull(booleanSetting2);
        booleanSetting.visibleWhen(booleanSetting2::get);
        BooleanSetting booleanSetting3 = this.othersShots;
        BooleanSetting booleanSetting4 = this.inFlight;
        Objects.requireNonNull(booleanSetting4);
        booleanSetting3.visibleWhen(booleanSetting4::get);
        NumberSetting numberSetting = this.range;
        BooleanSetting booleanSetting5 = this.inFlight;
        Objects.requireNonNull(booleanSetting5);
        numberSetting.visibleWhen(booleanSetting5::get);
        this.flow.visibleWhen(() -> {
            return !this.pathStyle.is(PATH_SOLID);
        });
        this.pathColor.visibleWhen(() -> {
            return this.colorMode.is("Manual");
        });
        this.markerScale.visibleWhen(() -> {
            return !this.marker.is("None");
        });
        this.hitColor.visibleWhen(() -> {
            return this.targetBox.get() && this.colorMode.is("Manual");
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
        forgetAim();
        dropTracks();
        this.labels.clear();
        this.level = null;
    }

    private void syncLevel(class_638 world) {
        if (world == this.level) {
            return;
        }
        reset();
        this.level = world;
    }

    private void forgetAim() {
        for (Aim aim : this.volley) {
            aim.forget();
        }
        this.volleySize = 0;
        this.volleyGhost = false;
        this.aimItem = null;
        this.aimSince = -1L;
        this.markerSince = -1L;
    }

    private void dropTracks() {
        if (this.tracks.isEmpty()) {
            return;
        }
        ObjectIterator it = this.tracks.values().iterator();
        while (it.hasNext()) {
            Track track = (Track) it.next();
            recycle(track);
        }
        this.tracks.clear();
    }

    private void recycle(Track track) {
        track.shot = null;
        track.departed = false;
        track.forget();
        if (this.spareTracks.size() < 24) {
            this.spareTracks.push(track);
        }
    }

    private void onTick(TickEvent event) {
        this.sweep++;
        class_310 mc = class_310.method_1551();
        class_1937 class_1937Var = mc.field_1687;
        class_1297 class_1297Var = mc.field_1724;
        syncLevel(class_1937Var);
        if (class_1937Var == null || class_1297Var == null || !this.inFlight.get()) {
            dropTracks();
            return;
        }
        double reach = this.range.get() * this.range.get();
        PearlTrajectoryModule pearlModule = activePearlModule();
        boolean otherPearlsTaken = pearlModule != null && pearlToggle(pearlModule, PEARL_OTHERS_ID);
        boolean others = this.othersShots.get();
        boolean captions = this.flightTime.get();
        class_243 eye = class_1297Var.method_33571();
        int budget = 24;
        for (class_1297 entity : class_1937Var.method_18112()) {
            if (entity instanceof class_1676) {
                class_1297 class_1297Var2 = (class_1676) entity;
                if (class_1297Var2.method_31481()) {
                    continue;
                } else {
                    class_1297 owner = class_1297Var2.method_24921();
                    boolean own = owner == class_1297Var;
                    if (own || others) {
                        boolean pearlTaken = pearlModule != null && (owner == null || own || otherPearlsTaken);
                        Ballistics.Body body = bodyOf(class_1297Var2, pearlTaken);
                        if (body != null && class_1297Var2.method_5858(class_1297Var) <= reach) {
                            class_243 motion = class_1297Var2.method_18798();
                            if (motion.method_1027() >= RESTING_SPEED_SQR && !resting(class_1297Var2)) {
                                int i = budget;
                                budget--;
                                if (i == 0) {
                                    break;
                                }
                                Track track = (Track) this.tracks.get(class_1297Var2.method_5628());
                                if (track == null) {
                                    track = this.spareTracks.isEmpty() ? new Track() : this.spareTracks.pop();
                                    this.tracks.put(class_1297Var2.method_5628(), track);
                                }
                                track.shot = class_1297Var2;
                                track.seen = this.sweep;
                                track.flight.launch(body, class_1297Var2.method_73189(), motion).bounds(class_1297Var2.method_17681(), class_1297Var2.method_17682()).gravity(class_1297Var2.method_5740() ? 0.0d : body.gravity()).wet(class_1297Var2.method_5799()).age(((class_1676) class_1297Var2).field_6012).lifetime(200).ignoring(owner, class_1297Var2).leftShooter(track.departed).solve(class_1937Var);
                                track.departed = track.flight.hasLeftShooter();
                                track.caption(eye, captions && (own || track.flight.outcome() == Ballistics.Outcome.ENTITY));
                            }
                        }
                    }
                }
            }
        }
        if (!this.tracks.isEmpty()) {
            this.tracks.values().removeIf(this::stale);
        }
    }

    private boolean stale(Track track) {
        if (track.seen == this.sweep) {
            return false;
        }
        recycle(track);
        return true;
    }

    private static boolean resting(class_1676 shot) {
        double dx = shot.method_23317() - shot.field_6014;
        double dy = shot.method_23318() - shot.field_6036;
        double dz = shot.method_23321() - shot.field_5969;
        return shot.field_6012 > 1 && ((dx * dx) + (dy * dy)) + (dz * dz) < RESTING_SPEED_SQR;
    }

    private Ballistics.Body bodyOf(class_1676 shot, boolean pearlsTaken) {
        if (shot instanceof class_1685) {
            class_1685 trident = (class_1685) shot;
            if (!this.tridents.get() || trident.method_7441()) {
                return null;
            }
            return Ballistics.Body.TRIDENT;
        }
        if (shot instanceof class_1665) {
            class_1665 arrow = (class_1665) shot;
            if (!this.bows.get() || arrow.method_7441()) {
                return null;
            }
            return Ballistics.Body.ARROW;
        }
        if (shot instanceof class_1684) {
            if (!this.pearls.get() || pearlsTaken) {
                return null;
            }
            return Ballistics.Body.ORB;
        }
        if (!this.throwables.get()) {
            return null;
        }
        if (shot instanceof class_1686) {
            return Ballistics.Body.FLASK;
        }
        if (shot instanceof class_1683) {
            return Ballistics.Body.BOTTLE;
        }
        if ((shot instanceof class_1680) || (shot instanceof class_1681)) {
            return Ballistics.Body.ORB;
        }
        return null;
    }

    private static PearlTrajectoryModule activePearlModule() {
        PearlTrajectoryModule pearl = (PearlTrajectoryModule) ModuleRegistry.get(PearlTrajectoryModule.class);
        if (pearl == null || !pearl.isEnabled()) {
            return null;
        }
        return pearl;
    }

    private static boolean pearlToggle(PearlTrajectoryModule pearl, String id) {
        for (Setting setting : pearl.settings()) {
            if (id.equals(setting.id()) && (setting instanceof BooleanSetting)) {
                BooleanSetting toggle = (BooleanSetting) setting;
                return toggle.get();
            }
        }
        return true;
    }

    private void onRender(Render3DEvent event) {
        this.labels.clear();
        class_310 mc = class_310.method_1551();
        class_638 world = mc.field_1687;
        class_746 player = mc.field_1724;
        syncLevel(world);
        if (world == null || player == null) {
            return;
        }
        long now = System.nanoTime();
        this.labelsAt = now;
        float time = (now - EPOCH) / 1.0E9f;
        float partialTick = event.partialTick;
        choosePalette();
        this.onTop = this.throughWalls.get();
        this.canvas.begin(mc.field_1773.method_19418());
        this.camera = mc.field_1773.method_19418().method_71156();
        class_1309 focus = this.targetBox.get() ? CombatTarget.current(TARGET_MEMORY_MS) : null;
        if (this.aimPreview.get()) {
            drawAim(world, player, partialTick, now, time, focus);
        } else if (this.aimItem != null || this.volleySize > 0) {
            forgetAim();
        }
        if (this.inFlight.get()) {
            drawTracks(player, partialTick, time, focus);
        }
        this.canvas.submit(this.onTop);
    }

    private void drawTracks(class_746 player, float partialTick, float time, class_1309 focus) {
        ObjectIterator it = this.tracks.values().iterator();
        while (it.hasNext()) {
            Track track = (Track) it.next();
            class_1676 shot = track.shot;
            if (shot != null && !shot.method_31481()) {
                boolean own = shot.method_24921() == player;
                float alpha = own ? 1.0f : 0.7f;
                drawPath(track.flight, shot.method_30950(partialTick), Double.MAX_VALUE, alpha * FLYING_ALPHA, false, false, own, time);
                drawImpact(track.flight, 1.0f, alpha, time, partialTick, focus);
                addLabel(track);
            }
        }
    }

    private void choosePalette() {
        boolean manual = this.colorMode.is("Manual");
        int base = manual ? this.pathColor.argb() : CosmeticAccent.argb();
        this.headTint = class_9848.method_61319(HEAD_LIFT, base, -1);
        this.tailTint = base;
        this.haloTint = base;
        this.markTint = base;
        this.ringTint = this.headTint;
        this.sheenTint = class_9848.method_61319(SHEEN_LIFT, base, -1);
        this.plateTint = class_9848.method_61319(PLATE_LIFT, base, -1);
        this.hitTint = manual ? this.hitColor.argb() : class_9848.method_61319(0.5f, base, -1);
    }

    private void drawAim(class_638 world, class_746 player, float partialTick, long now, float time, class_1309 focus) {
        class_1799 stack = heldLauncher(player);
        this.volleySize = 0;
        this.volleyGhost = false;
        if (!stack.method_7960()) {
            loadVolley(world, player, stack, partialTick);
        }
        if (this.volleySize == 0) {
            if (this.aimItem != null) {
                forgetAim();
                return;
            }
            return;
        }
        class_1792 item = stack.method_7909();
        if (item != this.aimItem) {
            this.aimItem = item;
            this.aimSince = now;
            this.markerSince = -1L;
        }
        float unfurl = easeOutExpo(((now - this.aimSince) / 1.0E9f) / UNFURL_SECONDS);
        Ballistics.Flight lead = this.volley[0].flight;
        if (unfurl < 1.0f || lead.outcome() == Ballistics.Outcome.OPEN) {
            this.markerSince = -1L;
        } else if (this.markerSince < 0) {
            this.markerSince = now;
        }
        float grow = this.markerSince < 0 ? 0.0f : settle((now - this.markerSince) / 1.0E9f);
        float alpha = this.volleyGhost ? GHOST_ALPHA : 1.0f;
        for (int i = 0; i < this.volleySize; i++) {
            Ballistics.Flight flight = this.volley[i].flight;
            drawPath(flight, null, flight.length() * ((double) unfurl), alpha, true, !this.volleyGhost, true, time);
            if (grow > 0.0f) {
                drawImpact(flight, grow, alpha, time, partialTick, focus);
            }
        }
        if (this.volleyGhost || unfurl < 1.0f) {
            return;
        }
        addLabel(this.volley[0]);
    }

    private static class_1799 heldLauncher(class_746 player) {
        if (player.method_6115()) {
            return player.method_6030();
        }
        class_1799 main = player.method_6047();
        return launches(main.method_7909()) ? main : player.method_6079();
    }

    private static boolean launches(class_1792 item) {
        return (item instanceof class_1811) || (item instanceof class_1835) || (item instanceof class_1823) || (item instanceof class_1771) || (item instanceof class_1776) || (item instanceof class_4537) || (item instanceof class_1779);
    }

    private void loadVolley(class_638 world, class_746 player, class_1799 stack, float partialTick) {
        class_1792 item = stack.method_7909();
        boolean drawing = player.method_6115() && player.method_6030() == stack;
        if (item instanceof class_1753) {
            if (!this.bows.get() || player.method_18808(stack).method_7960()) {
                return;
            }
            if (drawing || this.ghostArc.get()) {
                float pull = drawing ? bowPull(player.method_6048() + partialTick) : 1.0f;
                this.volleyGhost = !drawing || pull < BOW_MIN_PULL;
                aimed(world, player, partialTick, Ballistics.Body.ARROW, Math.max(pull, BOW_MIN_PULL) * BOW_SPEED, 0.0f);
                return;
            }
            return;
        }
        if (item instanceof class_1764) {
            if (this.bows.get()) {
                crossbowVolley(world, player, partialTick, (class_9278) stack.method_58695(class_9334.field_49649, class_9278.field_49298));
                return;
            }
            return;
        }
        if (item instanceof class_1835) {
            if (this.tridents.get()) {
                if ((drawing || this.ghostArc.get()) && !class_1890.method_60142(stack, class_9701.field_51651)) {
                    this.volleyGhost = !drawing || player.method_6048() < 10;
                    aimed(world, player, partialTick, Ballistics.Body.TRIDENT, 2.5f, 0.0f);
                    return;
                }
                return;
            }
            return;
        }
        if (!(item instanceof class_1776)) {
            if (this.throwables.get()) {
                lob(world, player, partialTick, item);
                return;
            }
            return;
        }
        PearlTrajectoryModule pearlModule = activePearlModule();
        if (this.pearls.get()) {
            if (pearlModule == null || !pearlToggle(pearlModule, PEARL_PREDICT_ID)) {
                aimed(world, player, partialTick, Ballistics.Body.ORB, class_1776.field_55033, 0.0f);
            }
        }
    }

    private void lob(class_638 world, class_746 player, float partialTick, class_1792 item) {
        if (item instanceof class_1823) {
            aimed(world, player, partialTick, Ballistics.Body.ORB, class_1823.field_55045, 0.0f);
            return;
        }
        if (item instanceof class_1771) {
            aimed(world, player, partialTick, Ballistics.Body.ORB, 1.5f, 0.0f);
        } else if (item instanceof class_4537) {
            aimed(world, player, partialTick, Ballistics.Body.FLASK, class_4537.field_55046, LOB_TILT);
        } else if (item instanceof class_1779) {
            aimed(world, player, partialTick, Ballistics.Body.BOTTLE, 0.7f, LOB_TILT);
        }
    }

    private static float bowPull(float ticks) {
        float seconds = ticks / 20.0f;
        return Math.min(1.0f, ((seconds * seconds) + (seconds * 2.0f)) / BOW_SPEED);
    }

    private void aimed(class_638 world, class_746 player, float partialTick, Ballistics.Body body, float speed, float tilt) {
        float pitchDeg = player.method_5695(partialTick);
        double pitch = Math.toRadians(pitchDeg);
        double yaw = Math.toRadians(player.method_5705(partialTick));
        double lift = Math.toRadians(pitchDeg + tilt);
        class_243 aim = new class_243((-Math.sin(yaw)) * Math.cos(pitch), -Math.sin(lift), Math.cos(yaw) * Math.cos(pitch)).method_1029().method_1021(speed);
        class_243 carried = player.method_60478();
        class_243 motion = aim.method_1031(carried.field_1352, player.method_24828() ? 0.0d : carried.field_1351, carried.field_1350);
        release(world, player, body, muzzle(player, partialTick, MUZZLE_DROP), motion, 240);
    }

    private void crossbowVolley(class_638 world, class_746 player, float partialTick, class_9278 loaded) {
        if (loaded.method_57442()) {
            return;
        }
        List<class_1799> rounds = loaded.method_57437();
        float speed = loaded.method_57438(class_1802.field_8639) ? CROSSBOW_ROCKET_SPEED : CROSSBOW_ARROW_SPEED;
        int count = rounds.size();
        float gap = count > 1 ? 20.0f / (count - 1) : 0.0f;
        float centre = (((count - 1) % 2) * gap) / 2.0f;
        class_243 look = player.method_5828(partialTick);
        class_243 up = player.method_18864(partialTick);
        for (int i = 0; i < count; i++) {
            class_1799 round = rounds.get(i);
            if (!round.method_7960()) {
                float offset = centre + (((i & 1) == 1 ? -1.0f : 1.0f) * ((i + 1) / 2) * gap);
                class_243 motion = swing(look, up, offset).method_1029().method_1021(speed);
                if (round.method_31574(class_1802.field_8639)) {
                    release(world, player, Ballistics.Body.ROCKET, muzzle(player, partialTick, ROCKET_DROP), motion, rocketFuse(round));
                } else {
                    release(world, player, Ballistics.Body.ARROW, muzzle(player, partialTick, MUZZLE_DROP), motion, 240);
                }
            }
        }
    }

    private static int rocketFuse(class_1799 rocket) {
        class_9284 fireworks = (class_9284) rocket.method_58694(class_9334.field_49616);
        int flight = 1 + (fireworks != null ? fireworks.comp_2391() : 0);
        return (10 * flight) + 6;
    }

    private static class_243 swing(class_243 vector, class_243 axis, double degrees) {
        if (degrees == 0.0d) {
            return vector;
        }
        double angle = Math.toRadians(degrees);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        class_243 k = axis.method_1029();
        return vector.method_1021(cos).method_1019(k.method_1036(vector).method_1021(sin)).method_1019(k.method_1021(k.method_1026(vector) * (1.0d - cos)));
    }

    private static class_243 muzzle(class_746 player, float partialTick, double drop) {
        class_243 feet = player.method_30950(partialTick);
        return new class_243(feet.field_1352, (feet.field_1351 + ((double) player.method_5751())) - drop, feet.field_1350);
    }

    private void release(class_638 world, class_746 player, Ballistics.Body body, class_243 from, class_243 motion, int life) {
        if (this.volleySize == 3) {
            return;
        }
        Aim[] aimArr = this.volley;
        int i = this.volleySize;
        this.volleySize = i + 1;
        Aim aim = aimArr[i];
        if (aim.matches(body, from, motion, life, this.sweep)) {
            return;
        }
        aim.hold(body, from, motion, life, this.sweep);
        aim.flight.launch(body, from, motion).lifetime(life).ignoring(player, null).solve(world);
        aim.caption(from, this.flightTime.get());
    }

    private void drawPath(Ballistics.Flight flight, class_243 head, double reveal, float alpha, boolean aim, boolean shine, boolean glow, float time) {
        double ax;
        double ay;
        double az;
        int next;
        int count = flight.size();
        if (count < 2 || alpha < 0.02f) {
            return;
        }
        double total = flight.length();
        if (head != null) {
            double hx = head.field_1352 - flight.x(0);
            double hy = head.field_1351 - flight.y(0);
            double hz = head.field_1350 - flight.z(0);
            total += Math.sqrt((hx * hx) + (hy * hy) + (hz * hz));
        }
        if (total < RESTING_SPEED_SQR) {
            return;
        }
        double limit = Math.min(reveal, total);
        boolean solid = this.pathStyle.is(PATH_SOLID);
        boolean dashes = this.pathStyle.is(PATH_DASHES);
        double unit = solid ? 0.6d : dashes ? DASH_PERIOD : BEAD_PERIOD;
        double spread = Math.max(1.0d, total / (unit * ((double) (aim ? 200 : TRACK_PIECES))));
        double period = (dashes ? DASH_PERIOD : BEAD_PERIOD) * spread;
        double dash = DASH_LENGTH * spread;
        double pieceLength = 0.6d * spread;
        double phase = (solid || !this.flow.get()) ? 0.0d : (((double) time) * FLOW_SPEED) % period;
        this.strokeTotal = total;
        this.strokeAlpha = alpha;
        this.strokeFade = aim;
        this.strokeSheen = shine ? ((((double) time) * SHEEN_SPEED) % (total + SHEEN_REST)) - SHEEN_REACH : Double.NaN;
        this.strokeWidth = this.thickness.get();
        this.strokeHalo = glow && this.halo.get();
        if (head != null) {
            ax = head.field_1352;
            ay = head.field_1351;
            az = head.field_1350;
            next = 0;
        } else {
            ax = flight.x(0);
            ay = flight.y(0);
            az = flight.z(0);
            next = 1;
        }
        double walked = 0.0d;
        for (int i = next; i < count && walked < limit; i++) {
            double bx = flight.x(i);
            double by = flight.y(i);
            double bz = flight.z(i);
            double dx = bx - ax;
            double dy = by - ay;
            double dz = bz - az;
            double seg = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
            if (seg > 1.0E-6d) {
                double end = Math.min(walked + seg, limit);
                if (solid) {
                    int pieces = Math.max(1, (int) Math.ceil((end - walked) / pieceLength));
                    double piece = (end - walked) / ((double) pieces);
                    for (int p = 0; p < pieces; p++) {
                        double u0 = walked + (((double) p) * piece);
                        stroke(ax, ay, az, dx, dy, dz, (u0 - walked) / seg, ((u0 + piece) - walked) / seg, u0 + (piece * MARKER_UNIT));
                    }
                } else {
                    double dFloor = Math.floor((walked - phase) / period) * period;
                    double d = phase;
                    while (true) {
                        double d2 = dFloor + d;
                        if (d2 < end) {
                            double u1 = Math.max(d2, walked);
                            double length = dashes ? dash : beadLength(ax, ay, az, dx, dy, dz, (u1 - walked) / seg);
                            double u2 = Math.min(d2 + length, end);
                            if (u2 > u1) {
                                stroke(ax, ay, az, dx, dy, dz, (u1 - walked) / seg, (u2 - walked) / seg, (u1 + u2) * MARKER_UNIT);
                            }
                            dFloor = d2;
                            d = period;
                        }
                    }
                }
            }
            walked += seg;
            ax = bx;
            ay = by;
            az = bz;
        }
    }

    private double beadLength(double ax, double ay, double az, double dx, double dy, double dz, double t) {
        double px = (ax + (dx * t)) - this.camera.field_1352;
        double py = (ay + (dy * t)) - this.camera.field_1351;
        double pz = (az + (dz * t)) - this.camera.field_1350;
        return class_3532.method_15350(Math.sqrt((px * px) + (py * py) + (pz * pz)) * BEAD_ANGLE, BEAD_MIN, BEAD_MAX);
    }

    private void stroke(double ax, double ay, double az, double dx, double dy, double dz, double t0, double t1, double along) {
        float alpha = this.strokeAlpha;
        if (this.strokeFade) {
            alpha *= smooth((float) (along / FADE_IN));
        }
        if (alpha < 0.02f) {
            return;
        }
        float progress = (float) (along / this.strokeTotal);
        int core = class_9848.method_61319(progress, this.headTint, this.tailTint);
        if (!Double.isNaN(this.strokeSheen)) {
            double glint = 1.0d - (Math.abs(along - this.strokeSheen) / SHEEN_REACH);
            if (glint > 0.0d) {
                core = class_9848.method_61319((float) (glint * 0.7d), core, this.sheenTint);
            }
        }
        double x0 = ax + (dx * t0);
        double y0 = ay + (dy * t0);
        double z0 = az + (dz * t0);
        double x1 = ax + (dx * t1);
        double y1 = ay + (dy * t1);
        double z1 = az + (dz * t1);
        if (this.strokeHalo) {
            float glow = alpha * HALO_ALPHA * haloFade(x0, y0, z0);
            if (glow > 0.0f) {
                this.canvas.line(x0, y0, z0, x1, y1, z1, class_9848.method_75601(this.haloTint, glow), this.strokeWidth * HALO_WIDTH);
            }
        }
        this.canvas.line(x0, y0, z0, x1, y1, z1, class_9848.method_75601(core, alpha), this.strokeWidth);
    }

    private float haloFade(double x, double y, double z) {
        double px = x - this.camera.field_1352;
        double py = y - this.camera.field_1351;
        double pz = z - this.camera.field_1350;
        double distanceSqr = (px * px) + (py * py) + (pz * pz);
        if (distanceSqr <= 576.0d) {
            return 1.0f;
        }
        if (distanceSqr >= 1600.0d) {
            return 0.0f;
        }
        return 1.0f - smooth((float) ((Math.sqrt(distanceSqr) - HALO_NEAR) / 16.0d));
    }

    private void drawImpact(Ballistics.Flight flight, float grow, float alpha, float time, float partialTick, class_1309 focus) {
        switch (flight.outcome()) {
            case ENTITY:
                if (this.targetBox.get()) {
                    drawTargetBox(flight.struck(), alpha, time, partialTick, focus);
                }
                break;
            case BLOCK:
                drawMarker(flight, grow, alpha, time);
                break;
            case BURST:
                drawBurst(flight.impact(), grow, alpha);
                break;
        }
    }

    private void drawMarker(Ballistics.Flight flight, float grow, float alpha, float time) {
        if (this.marker.is("None") || grow <= 0.01f) {
            return;
        }
        class_2350 face = flight.face();
        class_243 normal = new class_243(face.method_10148(), face.method_10164(), face.method_10165());
        class_243 up = new class_243(0.0d, 1.0d, 0.0d);
        if (face.method_10166() == class_2350.class_2351.field_11052) {
            class_243 heading = flight.heading();
            up = (heading.field_1352 * heading.field_1352) + (heading.field_1350 * heading.field_1350) < 1.0E-8d ? new class_243(0.0d, 0.0d, 1.0d) : new class_243(heading.field_1352, 0.0d, heading.field_1350).method_1029();
        }
        class_243 right = up.method_1036(normal).method_1029();
        class_243 centre = flight.impact().method_1019(normal.method_1021(MARKER_LIFT));
        double scale = MARKER_UNIT * ((double) this.markerScale.get()) * ((double) grow);
        this.gx = centre.field_1352;
        this.gy = centre.field_1351;
        this.gz = centre.field_1350;
        this.rx = right.field_1352 * scale;
        this.ry = right.field_1351 * scale;
        this.rz = right.field_1350 * scale;
        this.ux = up.field_1352 * scale;
        this.uy = up.field_1351 * scale;
        this.uz = up.field_1350 * scale;
        if (this.marker.is(MARKER_SUN)) {
            drawSunDisk(alpha, time);
        } else {
            drawAnkh(alpha, time);
        }
    }

    private void drawAnkh(float alpha, float time) {
        int gold = class_9848.method_75601(this.markTint, alpha);
        disc(0.78d, class_9848.method_75601(this.plateTint, alpha * 0.16f));
        orbit(0.9d, ((double) time) * 0.6d, class_9848.method_75601(this.ringTint, alpha * FLYING_ALPHA), this.thickness.get() * 0.75f);
        facet(at(-0.06d, 0.02d), at(0.06d, 0.02d), at(ROCKET_DROP, -0.62d), at(-0.15d, -0.62d), gold);
        facet(at(-0.06d, 0.09d), at(-0.06d, -0.03d), at(-0.37d, -0.08d), at(-0.37d, 0.14d), gold);
        facet(at(0.06d, 0.09d), at(0.06d, -0.03d), at(0.37d, -0.08d), at(0.37d, 0.14d), gold);
        for (int k = 0; k < 24; k++) {
            double c0 = RIM_COS[k];
            double s0 = RIM_SIN[k];
            double c1 = RIM_COS[k + 1];
            double s1 = RIM_SIN[k + 1];
            facet(at(0.19d * c0, 0.34d + (0.27d * s0)), at(0.19d * c1, 0.34d + (0.27d * s1)), at(MUZZLE_DROP * c1, 0.34d + (0.17d * s1)), at(MUZZLE_DROP * c0, 0.34d + (0.17d * s0)), gold);
        }
    }

    private void drawSunDisk(float alpha, float time) {
        int gold = class_9848.method_75601(this.markTint, alpha);
        float width = this.thickness.get();
        disc(0.95d, class_9848.method_75601(this.plateTint, alpha * 0.12f));
        disc(0.3d, gold);
        int ring = class_9848.method_75601(this.ringTint, alpha);
        class_243 previous = at(BEAD_PERIOD, 0.0d);
        for (int k = 1; k <= 24; k++) {
            class_243 point = at(BEAD_PERIOD * RIM_COS[k], BEAD_PERIOD * RIM_SIN[k]);
            line(previous, point, ring, width);
            previous = point;
        }
        double spin = ((double) time) * 0.35d;
        for (int k2 = 0; k2 < 12; k2++) {
            double angle = spin + ((6.283185307179586d * ((double) k2)) / 12.0d);
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);
            double tip = (k2 & 1) == 0 ? 0.8d : 0.66d;
            line(at(0.52d * cos, 0.52d * sin), at(tip * cos, tip * sin), gold, width);
        }
    }

    private void drawBurst(class_243 centre, float grow, float alpha) {
        int color = class_9848.method_75601(this.markTint, alpha);
        double reach = 0.55d * ((double) this.markerScale.get()) * ((double) grow);
        float width = this.thickness.get();
        for (double[] ray : BURST_RAYS) {
            class_243 dir = new class_243(ray[0], ray[1], ray[2]);
            line(centre.method_1019(dir.method_1021(reach * 0.25d)), centre.method_1019(dir.method_1021(reach)), color, width);
        }
    }

    private void drawTargetBox(class_1297 entity, float alpha, float time, float partialTick, class_1309 focus) {
        if (entity == null || entity.method_31481()) {
            return;
        }
        class_243 shift = entity.method_30950(partialTick).method_1020(entity.method_73189());
        class_238 box = entity.method_5829().method_997(shift).method_1014(0.06d);
        if (box.method_1006(this.camera)) {
            return;
        }
        boolean locked = entity == focus;
        float pulse = locked ? 0.5f + (0.5f * class_3532.method_15374(time * 6.5f)) : 0.0f;
        int edge = class_9848.method_75601(locked ? class_9848.method_61319(HEAD_LIFT * pulse, this.hitTint, -1) : this.hitTint, alpha);
        class_12178 fill = class_12180.method_75541(box, class_12179.method_75539(class_9848.method_75601(edge, locked ? 0.14f + (0.12f * pulse) : BOW_MIN_PULL)));
        if (this.onTop) {
            fill.method_75533();
        }
        brackets(box, locked ? 0.34d + (0.06d * ((double) pulse)) : 0.26d, edge, this.thickness.get() * (locked ? 1.2f : 1.0f));
    }

    private void brackets(class_238 box, double reach, int color, float width) {
        double lx = (box.field_1320 - box.field_1323) * reach;
        double ly = (box.field_1325 - box.field_1322) * reach;
        double lz = (box.field_1324 - box.field_1321) * reach;
        for (int corner = 0; corner < 8; corner++) {
            boolean east = (corner & 1) != 0;
            boolean top = (corner & 2) != 0;
            boolean south = (corner & 4) != 0;
            double x = east ? box.field_1320 : box.field_1323;
            double y = top ? box.field_1325 : box.field_1322;
            double z = south ? box.field_1324 : box.field_1321;
            class_243 tip = new class_243(x, y, z);
            line(tip, new class_243(east ? x - lx : x + lx, y, z), color, width);
            line(tip, new class_243(x, top ? y - ly : y + ly, z), color, width);
            line(tip, new class_243(x, y, south ? z - lz : z + lz), color, width);
        }
    }

    private void disc(double radius, int color) {
        class_243 centre = at(0.0d, 0.0d);
        for (int k = 0; k < 24; k++) {
            facet(centre, centre, at(radius * RIM_COS[k], radius * RIM_SIN[k]), at(radius * RIM_COS[k + 1], radius * RIM_SIN[k + 1]), color);
        }
    }

    private void orbit(double radius, double spin, int color, float width) {
        double span = 0.6283185307179586d * 0.55d;
        for (int k = 0; k < 10; k++) {
            double start = spin + (((double) k) * 0.6283185307179586d);
            class_243 previous = at(radius * Math.cos(start), radius * Math.sin(start));
            for (int s = 1; s <= 3; s++) {
                double angle = start + ((span * ((double) s)) / 3.0d);
                class_243 point = at(radius * Math.cos(angle), radius * Math.sin(angle));
                line(previous, point, color, width);
                previous = point;
            }
        }
    }

    private class_243 at(double x, double y) {
        return new class_243(this.gx + (this.rx * x) + (this.ux * y), this.gy + (this.ry * x) + (this.uy * y), this.gz + (this.rz * x) + (this.uz * y));
    }

    private void facet(class_243 a, class_243 b, class_243 c, class_243 d, int color) {
        double ex = c.field_1352 - a.field_1352;
        double ey = c.field_1351 - a.field_1351;
        double ez = c.field_1350 - a.field_1350;
        double fx = d.field_1352 - b.field_1352;
        double fy = d.field_1351 - b.field_1351;
        double fz = d.field_1350 - b.field_1350;
        double nx = (ey * fz) - (ez * fy);
        double ny = (ez * fx) - (ex * fz);
        double nz = (ex * fy) - (ey * fx);
        double facing = (nx * (this.camera.field_1352 - a.field_1352)) + (ny * (this.camera.field_1351 - a.field_1351)) + (nz * (this.camera.field_1350 - a.field_1350));
        if (facing >= 0.0d) {
            this.canvas.quad(a.field_1352, a.field_1351, a.field_1350, b.field_1352, b.field_1351, b.field_1350, c.field_1352, c.field_1351, c.field_1350, d.field_1352, d.field_1351, d.field_1350, color);
        } else {
            this.canvas.quad(d.field_1352, d.field_1351, d.field_1350, c.field_1352, c.field_1351, c.field_1350, b.field_1352, b.field_1351, b.field_1350, a.field_1352, a.field_1351, a.field_1350, color);
        }
    }

    private void line(class_243 from, class_243 to, int color, float width) {
        this.canvas.line(from.field_1352, from.field_1351, from.field_1350, to.field_1352, to.field_1351, to.field_1350, color, width);
    }

    private void addLabel(Plot plot) {
        class_243 class_243VarMethod_1031;
        if (!this.flightTime.get() || plot.seconds == null) {
            return;
        }
        Ballistics.Flight flight = plot.flight;
        class_243 impact = flight.impact();
        class_1297 struck = flight.struck();
        if (struck != null) {
            class_243VarMethod_1031 = new class_243(impact.field_1352, struck.method_5829().field_1325 + 0.35d, impact.field_1350);
        } else {
            class_243VarMethod_1031 = impact.method_1031(0.0d, 0.55d, 0.0d);
        }
        class_243 anchor = class_243VarMethod_1031;
        this.labels.add(new Label(anchor, plot.seconds, plot.detail));
    }

    @Override // net.anubis.client.module.HudOverlayProvider
    public boolean isHudOverlayEnabled() {
        return isEnabled() && this.flightTime.get();
    }

    @Override // net.anubis.client.module.HudOverlayProvider
    public void renderHudOverlay(ImDrawList dl, float screenW, float screenH) {
        if (this.labels.isEmpty() || System.nanoTime() - this.labelsAt > LABEL_TTL_NANOS) {
            return;
        }
        class_310 mc = class_310.method_1551();
        for (Label label : this.labels) {
            float[] screen = WorldLabel.project(mc, label.pos(), screenW, screenH);
            if (screen != null) {
                WorldLabel.drawText(dl, screen[0], screen[1], label.primary(), label.secondary(), 1.0f);
            }
        }
    }

    private static float smooth(float t) {
        float t2 = class_3532.method_15363(t, 0.0f, 1.0f);
        return t2 * t2 * (BOW_SPEED - (2.0f * t2));
    }

    private static float easeOutExpo(float t) {
        if (t >= 1.0f) {
            return 1.0f;
        }
        return 1.0f - ((float) Math.pow(2.0d, (-10.0d) * ((double) Math.max(0.0f, t))));
    }

    private static float settle(float seconds) {
        return 1.0f - ((float) (Math.exp((-5.5d) * ((double) seconds)) * Math.cos(8.5d * ((double) seconds))));
    }
}
