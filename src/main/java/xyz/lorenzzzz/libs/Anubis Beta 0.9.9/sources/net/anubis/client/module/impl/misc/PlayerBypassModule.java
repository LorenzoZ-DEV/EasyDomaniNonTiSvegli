package net.anubis.client.module.impl.misc;

import it.unimi.dsi.fastutil.longs.Long2FloatOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Objects;
import net.anubis.client.chat.ModuleChat;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.gui.ChallengeToast;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.util.ChunkAreas;
import net.anubis.client.util.FoglessLines;
import net.anubis.client.util.playerbypass.ChunkLightSnapshot;
import net.anubis.client.util.playerbypass.PlayerBypassLightTracker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1923;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_634;
import net.minecraft.class_9848;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/PlayerBypassModule.class */
@Environment(EnvType.CLIENT)
public final class PlayerBypassModule extends ClientModule {
    private static final int RENDER_DISTANCE_CHUNKS = 16;
    private static final int ZONE_RADIUS = 5;
    private static final double MARKER_Y = 64.0d;
    private static final float LINE_WIDTH = 1.5f;
    private static final float CENTER_FILL = 0.45f;
    private static final float RIM_FILL = 0.25f;
    private static final float EDGE_SHADE = 0.8f;
    private static final int BASE_COLOR = -2516480;
    private static final int SUSPECT_COLOR = -5766;
    private final NumberSetting opacity;
    private final BooleanSetting tracers;
    private final ChunkAreas chunkAreas;
    private final ChunkAreas suspectAreas;
    private final Long2FloatOpenHashMap baseWeight;
    private final Long2FloatOpenHashMap suspectWeight;
    private final LongOpenHashSet zone;
    private final BooleanSetting sound;
    private final BooleanSetting showCoordinates;
    private final BooleanSetting disconnect;

    public PlayerBypassModule() {
        super("player-bypass", "Player Bypass", "Flags chunks with signs of underground player bases from light data.", ModuleCategory.BASE_RAID);
        this.opacity = (NumberSetting) addSetting("opacity", new NumberSetting("Opacity", 140.0f, 10.0f, 255.0f).step(1.0f));
        this.tracers = (BooleanSetting) addSetting("tracers", new BooleanSetting("Tracers", false));
        this.chunkAreas = new ChunkAreas();
        this.suspectAreas = new ChunkAreas();
        this.baseWeight = new Long2FloatOpenHashMap();
        this.suspectWeight = new Long2FloatOpenHashMap();
        this.zone = new LongOpenHashSet();
        this.sound = (BooleanSetting) addSetting("sound", new BooleanSetting("Sound", true));
        this.showCoordinates = (BooleanSetting) addSetting("show-coordinates", new BooleanSetting("Show Coordinates", true));
        this.disconnect = (BooleanSetting) addSetting("disconnect-on-spotted", new BooleanSetting("Auto Disconnect", false));
        BooleanSetting notifications = usesNotifications();
        BooleanSetting booleanSetting = this.sound;
        Objects.requireNonNull(notifications);
        booleanSetting.visibleWhen(notifications::get);
        this.showCoordinates.visibleWhen(() -> {
            return notifications.get() || this.disconnect.get();
        });
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        PlayerBypassLightTracker.setSpottedListener(this::onChunkSpotted);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        PlayerBypassLightTracker.setSpottedListener(null);
    }

    private void onChunkSpotted(int chunkX, int chunkZ) {
        class_634 connection;
        class_310 mc = class_310.method_1551();
        int x = (chunkX << 4) + 8;
        int z = (chunkZ << 4) + 8;
        boolean showPos = this.showCoordinates.get();
        if (notificationsOn()) {
            ChallengeToast.addOrUpdate(new class_1799(class_1802.field_8575), "Base Detected", showPos ? class_2561.method_43470(x + ", " + z) : class_2561.method_43473(), this.sound.get());
        }
        if (this.disconnect.get() && (connection = mc.method_1562()) != null) {
            ModuleChat.send(this, ModuleChat.body().text("Auto Disconnect ").warn("triggered"));
            connection.method_48296().method_10747(class_2561.method_43470("[Player Bypass] Base detected" + (showPos ? " at " + x + ", " + z : "")));
        }
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        class_310 mc = class_310.method_1551();
        if (mc.field_1687 == null || mc.field_1724 == null) {
            return;
        }
        Long2ObjectMap<ChunkLightSnapshot> snapshots = PlayerBypassLightTracker.snapshots();
        if (snapshots.isEmpty()) {
            return;
        }
        int centerX = mc.field_1724.method_24515().method_10263() >> 4;
        int centerZ = mc.field_1724.method_24515().method_10260() >> 4;
        LongOpenHashSet bases = new LongOpenHashSet();
        LongOpenHashSet suspects = new LongOpenHashSet();
        ObjectIterator it = snapshots.long2ObjectEntrySet().iterator();
        while (it.hasNext()) {
            Long2ObjectMap.Entry<ChunkLightSnapshot> entry = (Long2ObjectMap.Entry) it.next();
            long key = entry.getLongKey();
            if (Math.abs(class_1923.method_8325(key) - centerX) <= 16 && Math.abs(class_1923.method_8332(key) - centerZ) <= 16 && PlayerBypassLightTracker.isFlagged(key, (ChunkLightSnapshot) entry.getValue())) {
                (PlayerBypassLightTracker.isSuspect(key) ? suspects : bases).add(key);
            }
        }
        if (bases.isEmpty() && suspects.isEmpty()) {
            return;
        }
        spread(bases, this.baseWeight);
        spread(suspects, this.suspectWeight);
        FoglessLines lines = FoglessLines.forFrame(event.partialTick);
        drawZones(lines, BASE_COLOR, SUSPECT_COLOR);
        drawSourceEdges(lines, bases, suspects, BASE_COLOR, SUSPECT_COLOR);
        if (this.tracers.get()) {
            this.chunkAreas.drawTracers(bases, MARKER_Y, class_9848.method_61334(BASE_COLOR));
            this.suspectAreas.drawTracers(suspects, MARKER_Y, class_9848.method_61334(SUSPECT_COLOR));
        }
    }

    private static void spread(LongOpenHashSet sources, Long2FloatOpenHashMap into) {
        into.clear();
        LongIterator it = sources.iterator();
        while (it.hasNext()) {
            long key = ((Long) it.next()).longValue();
            int cx = class_1923.method_8325(key);
            int cz = class_1923.method_8332(key);
            for (int dx = -5; dx <= 5; dx++) {
                for (int dz = -5; dz <= 5; dz++) {
                    float weight = falloff(Math.max(Math.abs(dx), Math.abs(dz)));
                    long area = class_1923.method_8331(cx + dx, cz + dz);
                    if (weight > into.get(area)) {
                        into.put(area, weight);
                    }
                }
            }
        }
    }

    private static float falloff(int ring) {
        float t = ring / 6.0f;
        return 1.0f - ((t * t) * (3.0f - (2.0f * t)));
    }

    private void drawZones(FoglessLines lines, int base, int suspect) {
        this.zone.clear();
        this.zone.addAll(this.baseWeight.keySet());
        this.zone.addAll(this.suspectWeight.keySet());
        float maxAlpha = Math.max(10, Math.min(255, this.opacity.getInt()));
        LongIterator it = this.zone.iterator();
        while (it.hasNext()) {
            long key = ((Long) it.next()).longValue();
            float b = this.baseWeight.get(key);
            float s = this.suspectWeight.get(key);
            float mix = (b * b) / ((b * b) + (s * s));
            int color = class_9848.method_61319(mix, suspect, base);
            float strength = RIM_FILL + (0.75f * Math.max(b, s));
            int fill = class_9848.method_61330(Math.round(maxAlpha * CENTER_FILL * strength * class_9848.method_65100(color)), color);
            int cx = class_1923.method_8325(key);
            int cz = class_1923.method_8332(key);
            double x0 = cx << 4;
            double z0 = cz << 4;
            double x1 = x0 + 16.0d;
            double z1 = z0 + 16.0d;
            class_12180.method_75541(new class_238(x0, MARKER_Y, z0, x1, MARKER_Y, z1), class_12179.method_75539(fill)).method_75533();
            int edge = class_9848.method_61330(Math.round(maxAlpha * class_9848.method_65100(color)), class_9848.method_61321(color, EDGE_SHADE));
            if (!this.zone.contains(class_1923.method_8331(cx, cz - 1))) {
                edge(lines, x0, z0, x1, z0, edge);
            }
            if (!this.zone.contains(class_1923.method_8331(cx, cz + 1))) {
                edge(lines, x0, z1, x1, z1, edge);
            }
            if (!this.zone.contains(class_1923.method_8331(cx - 1, cz))) {
                edge(lines, x0, z0, x0, z1, edge);
            }
            if (!this.zone.contains(class_1923.method_8331(cx + 1, cz))) {
                edge(lines, x1, z0, x1, z1, edge);
            }
        }
    }

    private void drawSourceEdges(FoglessLines lines, LongOpenHashSet bases, LongOpenHashSet suspects, int base, int suspect) {
        int alpha = Math.max(20, Math.min(255, this.opacity.getInt()));
        drawSourceEdges(lines, bases, bases, suspects, class_9848.method_61330(Math.round(alpha * class_9848.method_65100(base)), base));
        drawSourceEdges(lines, suspects, bases, suspects, class_9848.method_61330(Math.round(alpha * class_9848.method_65100(suspect)), suspect));
    }

    private static void drawSourceEdges(FoglessLines lines, LongOpenHashSet chunks, LongOpenHashSet bases, LongOpenHashSet suspects, int color) {
        LongIterator it = chunks.iterator();
        while (it.hasNext()) {
            long key = ((Long) it.next()).longValue();
            int cx = class_1923.method_8325(key);
            int cz = class_1923.method_8332(key);
            double x0 = cx << 4;
            double z0 = cz << 4;
            double x1 = x0 + 16.0d;
            double z1 = z0 + 16.0d;
            if (!isSource(bases, suspects, cx, cz - 1)) {
                edge(lines, x0, z0, x1, z0, color);
            }
            if (!isSource(bases, suspects, cx, cz + 1)) {
                edge(lines, x0, z1, x1, z1, color);
            }
            if (!isSource(bases, suspects, cx - 1, cz)) {
                edge(lines, x0, z0, x0, z1, color);
            }
            if (!isSource(bases, suspects, cx + 1, cz)) {
                edge(lines, x1, z0, x1, z1, color);
            }
        }
    }

    private static boolean isSource(LongOpenHashSet bases, LongOpenHashSet suspects, int cx, int cz) {
        long key = class_1923.method_8331(cx, cz);
        return bases.contains(key) || suspects.contains(key);
    }

    private static void edge(FoglessLines lines, double ax, double az, double bx, double bz, int color) {
        lines.line(new class_243(ax, MARKER_Y, az), new class_243(bx, MARKER_Y, bz), color, LINE_WIDTH);
    }
}
