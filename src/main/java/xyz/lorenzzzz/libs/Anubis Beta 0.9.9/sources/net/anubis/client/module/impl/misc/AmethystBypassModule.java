package net.anubis.client.module.impl.misc;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.ArrayDeque;
import java.util.Objects;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.PacketAppliedEvent;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.util.GeodeGlowScan;
import net.anubis.client.util.LevelUtil;
import net.anubis.client.util.TracerEngine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_1923;
import net.minecraft.class_1944;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2626;
import net.minecraft.class_2637;
import net.minecraft.class_2672;
import net.minecraft.class_2680;
import net.minecraft.class_2818;
import net.minecraft.class_2826;
import net.minecraft.class_2841;
import net.minecraft.class_310;
import net.minecraft.class_638;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/AmethystBypassModule.class */
@Environment(EnvType.CLIENT)
public final class AmethystBypassModule extends ClientModule {
    private static final int COLOR = -3636481;
    private static final float LINE_WIDTH = 1.5f;
    private static final int RESCAN_TICKS = 20;
    private static final long BUDGET_NANOS = 2000000;
    private static final String ANBS_SCAN = "ANBS+ Scan";
    private static final int MAX_GLOW_MARKS = 200;
    private final ModeSetting method;
    private final NumberSetting minCells;
    private final BooleanSetting tracer;
    private final ArrayDeque<class_1923> queue;
    private final LongOpenHashSet found;
    private final LongOpenHashSet bypassScanned;
    private final Long2ObjectOpenHashMap<class_2338> bypassMarkers;
    private final GeodeGlowScan glow;
    private final LongArrayList glowCells;
    private long[] hits;
    private int tick;

    public AmethystBypassModule() {
        super("amethyst-bypass", "Amethyst Bypass", "Shows amethyst geodes hidden by anti-xray.", ModuleCategory.BASE_RAID);
        this.method = (ModeSetting) addSetting("method", new ModeSetting("Method", "Bypass", "Bypass", "Light", ANBS_SCAN));
        this.minCells = (NumberSetting) addSetting("min-cells", new NumberSetting("Min Cells", 12.0f, 1.0f, 100.0f).step(1.0f));
        this.tracer = (BooleanSetting) addSetting("tracer", new BooleanSetting("Tracer", false));
        this.queue = new ArrayDeque<>();
        this.found = new LongOpenHashSet();
        this.bypassScanned = new LongOpenHashSet();
        this.bypassMarkers = new Long2ObjectOpenHashMap<>();
        this.glow = new GeodeGlowScan();
        this.glowCells = new LongArrayList();
        this.hits = new long[0];
        this.method.onChange(this::restart);
        this.minCells.visibleWhen(() -> {
            return this.method.is(ANBS_SCAN);
        });
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        restart();
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        restart();
    }

    private void restart() {
        this.queue.clear();
        this.found.clear();
        this.bypassScanned.clear();
        this.bypassMarkers.clear();
        this.hits = new long[0];
        this.tick = 0;
    }

    @EventHandler(stage = Stage.POST)
    public void onTick(TickEvent event) {
        class_310 mc = class_310.method_1551();
        class_638 level = mc.field_1687;
        if (level == null || mc.field_1724 == null) {
            return;
        }
        boolean bypass = this.method.is("Bypass");
        if (this.queue.isEmpty()) {
            int i = this.tick + 1;
            this.tick = i;
            if (i < 20) {
                return;
            }
            this.tick = 0;
            class_1923 center = mc.field_1724.method_31476();
            int radius = ((Integer) mc.field_1690.method_42503().method_41753()).intValue();
            if (bypass) {
                this.bypassScanned.removeIf(key -> {
                    return (inRange(key, center, radius) && LevelUtil.isLoaded(level, class_1923.method_8325(key), class_1923.method_8332(key))) ? false : true;
                });
                this.bypassMarkers.keySet().removeIf(key2 -> {
                    return !this.bypassScanned.contains(key2);
                });
                for (int x = center.field_9181 - radius; x <= center.field_9181 + radius; x++) {
                    for (int z = center.field_9180 - radius; z <= center.field_9180 + radius; z++) {
                        long key3 = class_1923.method_8331(x, z);
                        if (LevelUtil.isLoaded(level, x, z) && !this.bypassScanned.contains(key3)) {
                            this.queue.add(new class_1923(x, z));
                        }
                    }
                }
            } else {
                this.found.clear();
                for (int x2 = center.field_9181 - radius; x2 <= center.field_9181 + radius; x2++) {
                    for (int z2 = center.field_9180 - radius; z2 <= center.field_9180 + radius; z2++) {
                        if (LevelUtil.isLoaded(level, x2, z2)) {
                            this.queue.add(new class_1923(x2, z2));
                        }
                    }
                }
            }
        }
        long deadline = System.nanoTime() + BUDGET_NANOS;
        while (!this.queue.isEmpty() && System.nanoTime() < deadline) {
            class_1923 pos = this.queue.poll();
            if (LevelUtil.isLoaded(level, pos.field_9181, pos.field_9180)) {
                class_2818 chunk = level.method_8497(pos.field_9181, pos.field_9180);
                if (bypass) {
                    collectPaletteLeak(chunk, this.bypassMarkers);
                    this.bypassScanned.add(pos.method_8324());
                } else if (this.method.is(ANBS_SCAN)) {
                    collectGlow(level, chunk);
                } else {
                    collectHiddenBuds(level, chunk, this.found);
                }
            }
        }
        if (this.queue.isEmpty()) {
            this.hits = bypass ? markersToLongArray(this.bypassMarkers) : this.found.toLongArray();
        }
    }

    private void collectGlow(class_638 level, class_2818 chunk) {
        this.glowCells.clear();
        GeodeGlowScan geodeGlowScan = this.glow;
        LongArrayList longArrayList = this.glowCells;
        Objects.requireNonNull(longArrayList);
        if (geodeGlowScan.scan(level, chunk, longArrayList::add) < this.minCells.getInt()) {
            return;
        }
        int marks = Math.min(this.glowCells.size(), MAX_GLOW_MARKS);
        for (int i = 0; i < marks; i++) {
            this.found.add(this.glowCells.getLong(i));
        }
    }

    private static boolean inRange(long chunkKey, class_1923 center, int radius) {
        int x = class_1923.method_8325(chunkKey);
        int z = class_1923.method_8332(chunkKey);
        return Math.abs(x - center.field_9181) <= radius && Math.abs(z - center.field_9180) <= radius;
    }

    private static long[] markersToLongArray(Long2ObjectOpenHashMap<class_2338> markers) {
        long[] out = new long[markers.size()];
        int i = 0;
        ObjectIterator it = markers.values().iterator();
        while (it.hasNext()) {
            class_2338 marker = (class_2338) it.next();
            int i2 = i;
            i++;
            out[i2] = marker.method_10063();
        }
        return out;
    }

    @EventHandler
    public void onPacketApplied(PacketAppliedEvent event) {
        if (this.method.is("Bypass")) {
            class_2672 class_2672Var = event.packet;
            if (class_2672Var instanceof class_2672) {
                class_2672 chunk = class_2672Var;
                dirtyBypassChunk(chunk.method_11523(), chunk.method_11524());
                return;
            }
            class_2626 class_2626Var = event.packet;
            if (class_2626Var instanceof class_2626) {
                class_2626 update = class_2626Var;
                dirtyBypassChunk(update.method_11309().method_10263() >> 4, update.method_11309().method_10260() >> 4);
                return;
            }
            class_2637 class_2637Var = event.packet;
            if (class_2637Var instanceof class_2637) {
                class_2637 sectionUpdate = class_2637Var;
                sectionUpdate.method_30621((pos, state) -> {
                    dirtyBypassChunk(pos.method_10263() >> 4, pos.method_10260() >> 4);
                });
            }
        }
    }

    private void dirtyBypassChunk(int chunkX, int chunkZ) {
        long key = class_1923.method_8331(chunkX, chunkZ);
        if (this.bypassScanned.remove(key)) {
            this.bypassMarkers.remove(key);
        }
    }

    private static void collectPaletteLeak(class_2818 chunk, Long2ObjectOpenHashMap<class_2338> out) {
        class_2338 class_2338Var;
        class_2826[] sections = chunk.method_12006();
        int minSectionY = chunk.method_32891();
        class_1923 pos = chunk.method_12004();
        for (int i = 0; i < sections.length; i++) {
            class_2826 section = sections[i];
            if (section != null) {
                class_2841<class_2680> states = section.method_12265();
                if (!states.method_19526(state -> {
                    return false;
                }) && states.method_19526(AmethystBypassModule::isBud)) {
                    int baseY = (minSectionY + i) * 16;
                    class_2338 marker = pickMarker(chunk, section, baseY, pos);
                    long jMethod_8324 = pos.method_8324();
                    if (marker != null) {
                        class_2338Var = marker;
                    } else {
                        class_2338Var = new class_2338(pos.method_8326() + 8, baseY + 8, pos.method_8328() + 8);
                    }
                    out.put(jMethod_8324, class_2338Var);
                    return;
                }
            }
        }
    }

    private static class_2338 pickMarker(class_2818 chunk, class_2826 section, int baseY, class_1923 pos) {
        int score;
        class_2338.class_2339 neighbour = new class_2338.class_2339();
        class_2338 best = null;
        int bestScore = Integer.MAX_VALUE;
        for (int y = 0; y < 16; y++) {
            int worldY = baseY + y;
            for (int z = 0; z < 16; z++) {
                for (int x = 0; x < 16; x++) {
                    if (section.method_12254(x, y, z).method_26215()) {
                        int worldX = pos.method_8326() + x;
                        int worldZ = pos.method_8328() + z;
                        boolean touchesGeode = false;
                        for (class_2350 direction : class_2350.values()) {
                            int nx = worldX + direction.method_10148();
                            int nz = worldZ + direction.method_10165();
                            if ((nx >> 4) == pos.field_9181 && (nz >> 4) == pos.field_9180) {
                                neighbour.method_10103(nx, worldY + direction.method_10164(), nz);
                                class_2248 block = chunk.method_8320(neighbour).method_26204();
                                if (block == class_2246.field_27159 || block == class_2246.field_27160) {
                                    touchesGeode = true;
                                    break;
                                }
                            }
                        }
                        if (touchesGeode && (score = scatter(worldX, worldY, worldZ)) < bestScore) {
                            bestScore = score;
                            best = new class_2338(worldX, worldY, worldZ);
                        }
                    }
                }
            }
        }
        return best;
    }

    static int scatter(int x, int y, int z) {
        long h = ((((long) x) * (-7046029254386353131L)) ^ (((long) y) * (-4417276706812531889L))) ^ (((long) z) * 1609587929392839161L);
        long h2 = (h ^ (h >>> 29)) * (-4658895280553007687L);
        return Math.floorMod(h2 ^ (h2 >>> 32), 100);
    }

    private static void collectHiddenBuds(class_638 level, class_2818 chunk, LongOpenHashSet out) {
        class_2826[] sections = chunk.method_12006();
        int minSectionY = chunk.method_32891();
        class_1923 pos = chunk.method_12004();
        class_2338.class_2339 cursor = new class_2338.class_2339();
        for (int i = 0; i < sections.length; i++) {
            class_2826 section = sections[i];
            if (section != null && !section.method_38292() && section.method_12265().method_19526(AmethystBypassModule::isGeode)) {
                int baseY = (minSectionY + i) * 16;
                for (int y = 0; y < 16; y++) {
                    for (int z = 0; z < 16; z++) {
                        for (int x = 0; x < 16; x++) {
                            if (section.method_12254(x, y, z).method_26213() <= 0) {
                                cursor.method_10103(pos.method_8326() + x, baseY + y, pos.method_8328() + z);
                                int light = level.method_8314(class_1944.field_9282, cursor);
                                if (isBudLight(light) && touchesGeode(level, cursor) && isLocalMaximum(level, cursor, light)) {
                                    out.add(cursor.method_10063());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean isBudLight(int light) {
        return light == 1 || light == 2 || light == 4 || light == 5;
    }

    private static boolean touchesGeode(class_638 level, class_2338 pos) {
        for (class_2350 direction : class_2350.values()) {
            if (isGeode(level.method_8320(pos.method_10093(direction)))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isLocalMaximum(class_638 level, class_2338 pos, int light) {
        for (class_2350 direction : class_2350.values()) {
            if (level.method_8314(class_1944.field_9282, pos.method_10093(direction)) >= light) {
                return false;
            }
        }
        return true;
    }

    private static boolean isBud(class_2680 state) {
        class_2248 block = state.method_26204();
        return block == class_2246.field_27164 || block == class_2246.field_27163 || block == class_2246.field_27162;
    }

    private static boolean isGeode(class_2680 state) {
        return state.method_27852(class_2246.field_27159) || state.method_27852(class_2246.field_27160);
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        class_638 level = class_310.method_1551().field_1687;
        long[] current = this.hits;
        if (level == null || current.length == 0) {
            return;
        }
        class_12179 style = class_12179.method_75537(COLOR, LINE_WIDTH, COLOR);
        boolean drawTracers = this.tracer.get();
        for (long packed : current) {
            class_2338 pos = class_2338.method_10092(packed);
            if (LevelUtil.isLoaded(level, pos.method_10263() >> 4, pos.method_10260() >> 4)) {
                class_12180.method_75541(new class_238(pos), style).method_75533();
                if (drawTracers) {
                    TracerEngine.draw(class_243.method_24953(pos), COLOR);
                }
            }
        }
    }
}
