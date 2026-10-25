package net.anubis.client.module.impl.render;

import it.unimi.dsi.fastutil.longs.Long2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.function.LongConsumer;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.PacketAppliedEvent;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.util.ChunkAreas;
import net.anubis.client.util.ChunkOverlay;
import net.anubis.client.util.GeodeGlowScan;
import net.anubis.client.util.LevelUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1923;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2672;
import net.minecraft.class_2676;
import net.minecraft.class_2680;
import net.minecraft.class_2741;
import net.minecraft.class_2818;
import net.minecraft.class_2826;
import net.minecraft.class_310;
import net.minecraft.class_634;
import net.minecraft.class_638;
import net.minecraft.class_9848;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/SusChunkFinderModule.class */
@Environment(EnvType.CLIENT)
public final class SusChunkFinderModule extends ClientModule {
    private static final long SCAN_BUDGET_NANOS = 3000000;
    private static final int MIN_SCANNED_NEIGHBOURS = 3;
    private static final int NO_PACKET_SENSITIVITY = 1;
    private static final int VISIBLE_REFRESH_TICKS = 20;
    private static final int PACKET_MIN_Y = -16;
    private static final int PACKET_MAX_Y = 30;
    private static final String METHOD_CLASSIC = "Classic";
    private static final String METHOD_ANBS = "ANBS+ Scan";
    private static final int GLOW_CELLS_PER_POINT = 12;
    private final ModeSetting method;
    private final NumberSetting spreadRadius;
    private final NumberSetting sensitivity;
    private final ColorSetting chunkColor;
    private final BooleanSetting tracers;
    private final ChunkAreas chunkAreas;
    private final ChunkOverlay chunkOverlay;
    private final GeodeGlowScan glow;
    private final Long2IntOpenHashMap heat;
    private final Long2IntOpenHashMap vetoes;
    private final Long2IntOpenHashMap chunkScore;
    private final Long2BooleanOpenHashMap chunkUngrown;
    private final Long2BooleanOpenHashMap chunkHasPacket;
    private final LongOpenHashSet scannedChunks;
    private final LongArrayFIFOQueue queue;
    private final LongOpenHashSet queued;
    private Long2IntOpenHashMap visible;
    private boolean visibleDirty;
    private int visibleAge;
    private class_638 lastLevel;
    private int appliedRadius;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/SusChunkFinderModule$ChunkScore.class */
    @Environment(EnvType.CLIENT)
    private static final class ChunkScore extends Record {
        private final int selfHeat;
        private final boolean hasUngrown;

        private ChunkScore(int selfHeat, boolean hasUngrown) {
            this.selfHeat = selfHeat;
            this.hasUngrown = hasUngrown;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, ChunkScore.class), ChunkScore.class, "selfHeat;hasUngrown", "FIELD:Lnet/anubis/client/module/impl/render/SusChunkFinderModule$ChunkScore;->selfHeat:I", "FIELD:Lnet/anubis/client/module/impl/render/SusChunkFinderModule$ChunkScore;->hasUngrown:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, ChunkScore.class), ChunkScore.class, "selfHeat;hasUngrown", "FIELD:Lnet/anubis/client/module/impl/render/SusChunkFinderModule$ChunkScore;->selfHeat:I", "FIELD:Lnet/anubis/client/module/impl/render/SusChunkFinderModule$ChunkScore;->hasUngrown:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, ChunkScore.class, Object.class), ChunkScore.class, "selfHeat;hasUngrown", "FIELD:Lnet/anubis/client/module/impl/render/SusChunkFinderModule$ChunkScore;->selfHeat:I", "FIELD:Lnet/anubis/client/module/impl/render/SusChunkFinderModule$ChunkScore;->hasUngrown:Z").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int selfHeat() {
            return this.selfHeat;
        }

        public boolean hasUngrown() {
            return this.hasUngrown;
        }
    }

    public SusChunkFinderModule() {
        super("sus-chunk-finder", "Sus Chunk Finder", "Finds chunks with signs of player activity from fully grown amethyst.", ModuleCategory.BASE_RAID);
        this.method = (ModeSetting) addSetting("method", new ModeSetting("Method", METHOD_CLASSIC, METHOD_CLASSIC, METHOD_ANBS));
        this.spreadRadius = (NumberSetting) addSetting("spread-radius", new NumberSetting("Simulation Distance", 5.0f, 2.0f, 16.0f).step(1.0f));
        this.sensitivity = (NumberSetting) addSetting("packet-sensitivity", new NumberSetting("Sensitivity", 6.0f, 1.0f, 20.0f).step(1.0f));
        this.chunkColor = (ColorSetting) addSetting("chunk-color", new ColorSetting("Chunk Color", 1627325444));
        this.tracers = (BooleanSetting) addSetting("tracers", new BooleanSetting("Tracers", true));
        this.chunkAreas = new ChunkAreas();
        this.chunkOverlay = new ChunkOverlay();
        this.glow = new GeodeGlowScan();
        this.heat = new Long2IntOpenHashMap();
        this.vetoes = new Long2IntOpenHashMap();
        this.chunkScore = new Long2IntOpenHashMap();
        this.chunkUngrown = new Long2BooleanOpenHashMap();
        this.chunkHasPacket = new Long2BooleanOpenHashMap();
        this.scannedChunks = new LongOpenHashSet();
        this.queue = new LongArrayFIFOQueue();
        this.queued = new LongOpenHashSet();
        this.visible = new Long2IntOpenHashMap();
        this.visibleDirty = true;
        this.method.onChange(this::rescan);
        this.sensitivity.onChange(() -> {
            this.visibleDirty = true;
        });
    }

    private void rescan() {
        clearAll();
        queueLoadedChunks();
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        clearAll();
        this.lastLevel = class_310.method_1551().field_1687;
        queueLoadedChunks();
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        clearAll();
        this.lastLevel = null;
    }

    private void clearAll() {
        this.heat.clear();
        this.vetoes.clear();
        this.chunkScore.clear();
        this.chunkUngrown.clear();
        this.chunkHasPacket.clear();
        this.scannedChunks.clear();
        this.queue.clear();
        this.queued.clear();
        this.visible = new Long2IntOpenHashMap();
        this.visibleDirty = true;
        this.appliedRadius = this.spreadRadius.getInt();
    }

    private void queueLoadedChunks() {
        class_310 mc = class_310.method_1551();
        if (mc.field_1687 == null || mc.field_1724 == null) {
            return;
        }
        int radius = ((Integer) mc.field_1690.method_42503().method_41753()).intValue() + 1;
        class_1923 center = mc.field_1724.method_31476();
        for (int x = center.field_9181 - radius; x <= center.field_9181 + radius; x++) {
            for (int z = center.field_9180 - radius; z <= center.field_9180 + radius; z++) {
                if (LevelUtil.isLoaded(mc.field_1687, x, z)) {
                    enqueue(class_1923.method_8331(x, z));
                }
            }
        }
    }

    private void enqueue(long key) {
        if (this.queued.add(key)) {
            this.queue.enqueue(key);
        }
    }

    @EventHandler
    public void onPacketApplied(PacketAppliedEvent event) {
        class_2672 class_2672Var = event.packet;
        if (class_2672Var instanceof class_2672) {
            class_2672 chunk = class_2672Var;
            enqueue(class_1923.method_8331(chunk.method_11523(), chunk.method_11524()));
            return;
        }
        class_2676 class_2676Var = event.packet;
        if (class_2676Var instanceof class_2676) {
            class_2676 light = class_2676Var;
            if (this.method.is(METHOD_ANBS)) {
                enqueue(class_1923.method_8331(light.method_11558(), light.method_11554()));
            }
        }
    }

    @EventHandler
    public void onTick(TickEvent event) {
        class_310 mc = class_310.method_1551();
        class_638 level = mc.field_1687;
        if (level != this.lastLevel) {
            clearAll();
            this.lastLevel = level;
            queueLoadedChunks();
        }
        if (level == null) {
            return;
        }
        if (this.spreadRadius.getInt() != this.appliedRadius) {
            clearAll();
            queueLoadedChunks();
        }
        long deadline = System.nanoTime() + SCAN_BUDGET_NANOS;
        while (!this.queue.isEmpty() && System.nanoTime() < deadline) {
            long key = this.queue.dequeueLong();
            this.queued.remove(key);
            int cx = class_1923.method_8325(key);
            int cz = class_1923.method_8332(key);
            if (LevelUtil.isLoaded(level, cx, cz)) {
                updateChunk(key, level.method_8497(cx, cz), level);
                this.scannedChunks.add(key);
                this.visibleDirty = true;
            }
        }
        if (!this.visibleDirty) {
            int i = this.visibleAge + 1;
            this.visibleAge = i;
            if (i < 20) {
                return;
            }
        }
        this.visible = computeVisible(level);
        this.visibleDirty = false;
        this.visibleAge = 0;
    }

    private void updateChunk(long key, class_2818 chunk, class_638 level) {
        int r = this.spreadRadius.getInt();
        int cx = class_1923.method_8325(key);
        int cz = class_1923.method_8332(key);
        int oldHeat = this.chunkScore.getOrDefault(key, 0);
        boolean oldUngrown = this.chunkUngrown.getOrDefault(key, false);
        if (oldHeat > 0) {
            forArea(cx, cz, r, area -> {
                subtract(this.heat, area, oldHeat);
            });
        }
        if (oldUngrown) {
            forArea(cx, cz, r, area2 -> {
                subtract(this.vetoes, area2, 1);
            });
        }
        this.chunkHasPacket.put(key, hasBlocksInRange(chunk, -16, 30));
        ChunkScore score = scoreChunk(chunk, level);
        this.chunkScore.put(key, score.selfHeat());
        this.chunkUngrown.put(key, score.hasUngrown());
        if (score.hasUngrown()) {
            forArea(cx, cz, r, area3 -> {
                this.vetoes.addTo(area3, 1);
            });
        }
        if (score.selfHeat() > 0) {
            forArea(cx, cz, r, area4 -> {
                this.heat.addTo(area4, score.selfHeat());
            });
        }
    }

    private static void forArea(int cx, int cz, int r, LongConsumer action) {
        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
                action.accept(class_1923.method_8331(cx + dx, cz + dz));
            }
        }
    }

    private static void subtract(Long2IntOpenHashMap map, long key, int amount) {
        int left = map.get(key) - amount;
        if (left <= 0) {
            map.remove(key);
        } else {
            map.put(key, left);
        }
    }

    private static boolean hasBlocksInRange(class_2818 chunk, int minY, int maxY) {
        class_2826[] sections = chunk.method_12006();
        int minSectionY = chunk.method_32891();
        for (int i = 0; i < sections.length; i++) {
            class_2826 section = sections[i];
            if (section != null && !section.method_38292()) {
                int baseY = (minSectionY + i) << 4;
                if (baseY + 15 >= minY && baseY <= maxY) {
                    for (int y = 0; y < 16; y++) {
                        int worldY = baseY + y;
                        if (worldY >= minY && worldY <= maxY) {
                            for (int x = 0; x < 16; x++) {
                                for (int z = 0; z < 16; z++) {
                                    if (!section.method_12254(x, y, z).method_26215()) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private ChunkScore scoreChunk(class_2818 chunk, class_638 level) {
        if (this.method.is(METHOD_ANBS)) {
            return new ChunkScore(this.glow.scan(level, chunk, null) / 12, false);
        }
        class_2826[] sections = chunk.method_12006();
        int minSectionY = chunk.method_32891();
        class_1923 pos = chunk.method_12004();
        boolean paletteOnly = isDonutFolia();
        int grown = 0;
        int ungrown = 0;
        boolean sawBud = false;
        boolean sawCluster = false;
        class_2338.class_2339 cursor = new class_2338.class_2339();
        for (int i = 0; i < sections.length; i++) {
            class_2826 section = sections[i];
            if (section != null && !section.method_38292()) {
                if (paletteOnly) {
                    if (!sawBud && section.method_19523(SusChunkFinderModule::isBud)) {
                        sawBud = true;
                    }
                    if (!sawCluster && section.method_19523(state -> {
                        return state.method_27852(class_2246.field_27161);
                    })) {
                        sawCluster = true;
                    }
                } else if (section.method_19523(SusChunkFinderModule::isAmethystGrowth)) {
                    int baseY = (minSectionY + i) << 4;
                    for (int x = 0; x < 16; x++) {
                        for (int y = 0; y < 16; y++) {
                            for (int z = 0; z < 16; z++) {
                                class_2680 state2 = section.method_12254(x, y, z);
                                if (state2.method_27852(class_2246.field_27161)) {
                                    grown++;
                                } else if (isBud(state2) && state2.method_28498(class_2741.field_12525)) {
                                    class_2350 facing = state2.method_11654(class_2741.field_12525);
                                    cursor.method_10103(pos.method_8326() + x, baseY + y, pos.method_8328() + z).method_10098(facing.method_10153());
                                    if (level.method_8320(cursor).method_27852(class_2246.field_27160)) {
                                        ungrown++;
                                    } else {
                                        grown++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (paletteOnly) {
            if (sawBud) {
                ungrown++;
            } else if (sawCluster) {
                grown++;
            }
        }
        return new ChunkScore(grown, ungrown > 0);
    }

    private static boolean isBud(class_2680 state) {
        class_2248 block = state.method_26204();
        return block == class_2246.field_27164 || block == class_2246.field_27163 || block == class_2246.field_27162;
    }

    private static boolean isAmethystGrowth(class_2680 state) {
        return state.method_27852(class_2246.field_27161) || isBud(state);
    }

    private static boolean isDonutFolia() {
        String brand;
        class_634 connection = class_310.method_1551().method_1562();
        return (connection == null || (brand = connection.method_52790()) == null || !brand.contains("DonutFolia")) ? false : true;
    }

    private Long2IntOpenHashMap computeVisible(class_638 level) {
        Long2IntOpenHashMap eligible = new Long2IntOpenHashMap();
        ObjectIterator it = this.heat.long2IntEntrySet().iterator();
        while (it.hasNext()) {
            Long2IntMap.Entry entry = (Long2IntMap.Entry) it.next();
            long key = entry.getLongKey();
            if (!this.vetoes.containsKey(key)) {
                int cx = class_1923.method_8325(key);
                int cz = class_1923.method_8332(key);
                if (LevelUtil.isLoaded(level, cx, cz) && scannedNeighbours(cx, cz) >= 3) {
                    eligible.put(key, entry.getIntValue());
                }
            }
        }
        int withPacket = this.sensitivity.getInt();
        int reach = this.appliedRadius;
        Long2IntOpenHashMap kept = new Long2IntOpenHashMap();
        ObjectIterator it2 = eligible.long2IntEntrySet().iterator();
        while (it2.hasNext()) {
            Long2IntMap.Entry entry2 = (Long2IntMap.Entry) it2.next();
            long key2 = entry2.getLongKey();
            int value = entry2.getIntValue();
            int threshold = this.chunkHasPacket.getOrDefault(key2, true) ? withPacket : 1;
            if (value >= threshold && isPeak(eligible, key2, value, reach)) {
                kept.put(key2, value);
            }
        }
        return kept;
    }

    private static boolean isPeak(Long2IntOpenHashMap field, long key, int value, int reach) {
        int cx = class_1923.method_8325(key);
        int cz = class_1923.method_8332(key);
        for (int dx = -reach; dx <= reach; dx++) {
            for (int dz = -reach; dz <= reach; dz++) {
                if (!(dx == 0 && dz == 0) && field.get(class_1923.method_8331(cx + dx, cz + dz)) > value) {
                    return false;
                }
            }
        }
        return true;
    }

    private int scannedNeighbours(int cx, int cz) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if ((dx != 0 || dz != 0) && this.scannedChunks.contains(class_1923.method_8331(cx + dx, cz + dz))) {
                    count++;
                }
            }
        }
        return count;
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        if (class_310.method_1551().field_1687 == null) {
            return;
        }
        this.chunkOverlay.draw(this.visible.keySet(), this.chunkColor.argb(), event.partialTick);
        if (this.tracers.get()) {
            this.chunkAreas.drawTracers(this.visible.keySet(), 63.0d, class_9848.method_61334(this.chunkColor.argb()));
        }
    }
}
