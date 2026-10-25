package net.anubis.client.module.impl.render;

import imgui.flag.ImGuiWindowFlags;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongListIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.BlockUpdateEvent;
import net.anubis.client.event.impl.PacketReceiveNettyEvent;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.gui.ChallengeToast;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.util.ChunkAreas;
import net.anubis.client.util.ChunkOverlay;
import net.anubis.client.util.LevelUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_156;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1923;
import net.minecraft.class_1944;
import net.minecraft.class_2231;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2269;
import net.minecraft.class_2309;
import net.minecraft.class_2312;
import net.minecraft.class_2315;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2377;
import net.minecraft.class_2401;
import net.minecraft.class_2426;
import net.minecraft.class_2428;
import net.minecraft.class_2436;
import net.minecraft.class_2453;
import net.minecraft.class_2457;
import net.minecraft.class_2459;
import net.minecraft.class_2537;
import net.minecraft.class_2561;
import net.minecraft.class_2665;
import net.minecraft.class_2667;
import net.minecraft.class_2671;
import net.minecraft.class_2672;
import net.minecraft.class_2676;
import net.minecraft.class_2678;
import net.minecraft.class_2680;
import net.minecraft.class_2804;
import net.minecraft.class_2806;
import net.minecraft.class_2818;
import net.minecraft.class_2826;
import net.minecraft.class_310;
import net.minecraft.class_3562;
import net.minecraft.class_368;
import net.minecraft.class_374;
import net.minecraft.class_4076;
import net.minecraft.class_4850;
import net.minecraft.class_5803;
import net.minecraft.class_631;
import net.minecraft.class_638;
import net.minecraft.class_6606;
import net.minecraft.class_8886;
import net.minecraft.class_9848;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/PrimeChunkFinderModule.class */
@Environment(EnvType.CLIENT)
public final class PrimeChunkFinderModule extends ClientModule {
    private static final int SECTION_BYTES = 2048;
    private static final int MAX_FLAGGED_CHUNKS = 256;
    private static final int AMETHYST_MAX_LIGHT = 5;
    private static final int AMETHYST_REACH = 5;
    private static final long REDSTONE_NOTIFY_COOLDOWN_MS = 15000;
    private static final int ORE_LIGHT = 9;
    private static final int LAMP_LIGHT = 14;
    private static final long GONE_LAMP_MS = 5000;
    private static final int MAX_TRACKED_CHUNKS = 5000;
    private static final int SNAPSHOT_RADIUS = 32;
    private static final double MAX_Y = 0.0d;
    private final ColorSetting chunkColor;
    private final BooleanSetting tracers;
    private final BooleanSetting sound;
    private final LinkedHashMap<Long, Map<Integer, byte[]>> chunkLight;
    private final LongOpenHashSet passedChunks;
    private final LongLinkedOpenHashSet flaggedChunks;
    private final LongOpenHashSet shownChunks;
    private final ChunkAreas chunkAreas;
    private final ChunkOverlay chunkOverlay;
    private final LongOpenHashSet checkedSections;
    private final LongOpenHashSet amethystSections;
    private final Long2ObjectOpenHashMap<long[]> oreSections;
    private final Long2ObjectOpenHashMap<int[]> lampSections;
    private final Long2LongOpenHashMap goneLamps;
    private final class_2338.class_2339 cursor;
    private final Long2LongOpenHashMap redstoneNotified;
    private static final long[] NO_ORES = new long[0];
    private static final int[] NO_LAMPS = new int[0];
    private static final byte[] ZERO_SECTION = new byte[2048];

    public PrimeChunkFinderModule() {
        super("prime-chunk-finder", "Prime Chunk Finder", "Flags chunks with light and redstone changes below Y 0.", ModuleCategory.BASE_RAID);
        this.chunkColor = (ColorSetting) addSetting("chunk-color", new ColorSetting("Chunk Color", 1342242650));
        this.tracers = (BooleanSetting) addSetting("tracers", new BooleanSetting("Tracers", true));
        this.sound = (BooleanSetting) addSetting("sound", new BooleanSetting("Sound", true));
        this.chunkLight = new LinkedHashMap<>(256, 0.75f, true);
        this.passedChunks = new LongOpenHashSet();
        this.flaggedChunks = new LongLinkedOpenHashSet();
        this.shownChunks = new LongOpenHashSet();
        this.chunkAreas = new ChunkAreas();
        this.chunkOverlay = new ChunkOverlay();
        this.checkedSections = new LongOpenHashSet();
        this.amethystSections = new LongOpenHashSet();
        this.oreSections = new Long2ObjectOpenHashMap<>();
        this.lampSections = new Long2ObjectOpenHashMap<>();
        this.goneLamps = new Long2LongOpenHashMap();
        this.cursor = new class_2338.class_2339();
        this.redstoneNotified = new Long2LongOpenHashMap();
        BooleanSetting notifications = usesNotifications();
        BooleanSetting booleanSetting = this.sound;
        Objects.requireNonNull(notifications);
        booleanSetting.visibleWhen(notifications::get);
        this.redstoneNotified.defaultReturnValue(-4611686018427387904L);
    }

    @Override // net.anubis.client.module.ClientModule
    public boolean persistEnabled() {
        return false;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1724.method_23318() >= MAX_Y) {
            warnDeepslateOnly(mc);
            requestDisable();
        } else {
            reset();
            snapshotLoadedChunks();
            on(PacketReceiveNettyEvent.TYPE, this::onPacket);
        }
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        reset();
    }

    private static void warnDeepslateOnly(class_310 mc) {
        class_374 toasts = mc.method_1566();
        if (toasts.method_1997(DeepslateToast.class, class_368.field_2208) == null) {
            toasts.method_1999(new DeepslateToast());
        }
    }

    private void onPacket(PacketReceiveNettyEvent event) {
        class_310 mc = class_310.method_1551();
        if (event.packet instanceof class_2678) {
            mc.execute(() -> {
                if (isEnabled()) {
                    reset();
                }
            });
            return;
        }
        class_2672 class_2672Var = event.packet;
        if (class_2672Var instanceof class_2672) {
            class_2672 packet = class_2672Var;
            int chunkX = packet.method_11523();
            int chunkZ = packet.method_11524();
            class_6606 data = packet.method_38599();
            mc.execute(() -> {
                if (!isEnabled() || mc.field_1687 == null) {
                    return;
                }
                baselineChunk(chunkX, chunkZ, data);
            });
            return;
        }
        class_2676 class_2676Var = event.packet;
        if (class_2676Var instanceof class_2676) {
            class_2676 packet2 = class_2676Var;
            int chunkX2 = packet2.method_11558();
            int chunkZ2 = packet2.method_11554();
            class_6606 data2 = packet2.method_38600();
            mc.execute(() -> {
                if (!isEnabled() || mc.field_1687 == null) {
                    return;
                }
                detectChanges(chunkX2, chunkZ2, data2);
            });
        }
    }

    @EventHandler
    public void onBlockUpdate(BlockUpdateEvent event) {
        if (event.oldState == event.newState || class_310.method_1551().field_1687 == null) {
            return;
        }
        rememberGoneLamp(event.pos, event.oldState, event.newState);
        detectRedstone(event.pos, event.oldState, event.newState);
    }

    @EventHandler(stage = Stage.POST)
    public void onTick(TickEvent event) {
        class_310 mc = class_310.method_1551();
        if (mc.field_1687 == null) {
            reset();
            return;
        }
        if (mc.field_1724 != null && mc.field_1724.method_23318() < MAX_Y) {
            this.passedChunks.add(mc.field_1724.method_31476().method_8324());
        }
        forgetOldLamps();
        refreshShown(mc.field_1687);
    }

    private void refreshShown(class_638 level) {
        this.shownChunks.clear();
        LongListIterator it = this.flaggedChunks.iterator();
        while (it.hasNext()) {
            long key = ((Long) it.next()).longValue();
            if (LevelUtil.isLoaded(level, class_1923.method_8325(key), class_1923.method_8332(key))) {
                this.shownChunks.add(key);
            }
        }
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        if (this.shownChunks.isEmpty() || class_310.method_1551().field_1687 == null) {
            return;
        }
        int fill = this.chunkColor.argb();
        this.chunkOverlay.draw(this.shownChunks, fill, event.partialTick);
        if (this.tracers.get()) {
            this.chunkAreas.drawTracers(this.shownChunks, 63.0d, class_9848.method_61334(fill));
        }
    }

    private void flagChunk(long key) {
        this.flaggedChunks.addAndMoveToLast(key);
        this.shownChunks.add(key);
        while (this.flaggedChunks.size() > 256) {
            this.flaggedChunks.removeFirstLong();
        }
    }

    private void snapshotLoadedChunks() {
        class_310 mc = class_310.method_1551();
        if (mc.field_1687 == null || mc.field_1724 == null) {
            return;
        }
        int centerX = mc.field_1724.method_31477() >> 4;
        int centerZ = mc.field_1724.method_31479() >> 4;
        for (int chunkX = centerX - 32; chunkX <= centerX + 32; chunkX++) {
            for (int chunkZ = centerZ - 32; chunkZ <= centerZ + 32; chunkZ++) {
                Map<Integer, byte[]> sections = snapshotChunk(chunkX, chunkZ);
                if (sections != null) {
                    this.chunkLight.put(Long.valueOf(class_1923.method_8331(chunkX, chunkZ)), sections);
                }
            }
        }
        trimChunkCache();
    }

    private Map<Integer, byte[]> snapshotChunk(int chunkX, int chunkZ) {
        class_638 level = class_310.method_1551().field_1687;
        if (level == null) {
            return null;
        }
        class_631 chunkSource = level.method_2935();
        if (!chunkSource.method_12123(chunkX, chunkZ)) {
            return null;
        }
        class_3562 blockLight = chunkSource.method_12130().method_15562(class_1944.field_9282);
        Map<Integer, byte[]> sections = createZeroedChunk();
        for (int sectionY = bottomSection(); sectionY <= topSection(); sectionY++) {
            class_2804 layer = blockLight.method_15544(class_4076.method_18676(chunkX, sectionY, chunkZ));
            if (layer != null && !layer.method_51379(0)) {
                sections.put(Integer.valueOf(sectionY), Arrays.copyOf(layer.method_12137(), 2048));
            }
        }
        return sections;
    }

    private void baselineChunk(int chunkX, int chunkZ, class_6606 data) {
        Map<Integer, byte[]> sections = createZeroedChunk();
        applyInitializedSections(data, sections, chunkX, chunkZ, false, null);
        applyUninitializedSections(data, sections, chunkX, chunkZ, false, null);
        this.chunkLight.put(Long.valueOf(class_1923.method_8331(chunkX, chunkZ)), sections);
        trimChunkCache();
    }

    private void detectChanges(int chunkX, int chunkZ, class_6606 data) {
        long key = class_1923.method_8331(chunkX, chunkZ);
        this.checkedSections.clear();
        this.amethystSections.clear();
        this.oreSections.clear();
        this.lampSections.clear();
        Map<Integer, byte[]> sections = this.chunkLight.get(Long.valueOf(key));
        if (sections == null) {
            sections = snapshotChunk(chunkX, chunkZ);
        }
        boolean compare = (sections == null || isOwnChunk(key)) ? false : true;
        if (sections == null) {
            sections = createZeroedChunk();
        }
        ChangeSummary summary = new ChangeSummary();
        applyInitializedSections(data, sections, chunkX, chunkZ, compare, summary);
        applyUninitializedSections(data, sections, chunkX, chunkZ, compare, summary);
        this.chunkLight.put(Long.valueOf(key), sections);
        trimChunkCache();
        if (summary.count == 0) {
            return;
        }
        flagChunk(key);
        if (notificationsOn()) {
            ChallengeToast.addOrUpdate(new class_1799(class_1802.field_8733), name(), class_2561.method_43470(summary.x + ", " + summary.y + ", " + summary.z), this.sound.get());
        }
    }

    private void rememberGoneLamp(class_2338 pos, class_2680 previous, class_2680 state) {
        if (isLamp(previous)) {
            int light = previous.method_26213();
            if (!isLamp(state) || state.method_26213() < light) {
                this.goneLamps.put(pos.method_10063(), ((class_156.method_658() + GONE_LAMP_MS) << 4) | ((long) light));
            }
        }
    }

    private void forgetOldLamps() {
        if (this.goneLamps.isEmpty()) {
            return;
        }
        long now = class_156.method_658();
        LongIterator it = this.goneLamps.values().iterator();
        while (it.hasNext()) {
            if ((it.nextLong() >>> 4) < now) {
                it.remove();
            }
        }
    }

    private void detectRedstone(class_2338 pos, class_2680 previous, class_2680 state) {
        if (pos.method_10264() < MAX_Y) {
            if (isRedstone(state) || isRedstone(previous)) {
                long key = class_1923.method_8331(pos.method_10263() >> 4, pos.method_10260() >> 4);
                if (isOwnChunk(key)) {
                    return;
                }
                flagChunk(key);
                long now = class_156.method_658();
                if (!notificationsOn() || now - this.redstoneNotified.get(key) < REDSTONE_NOTIFY_COOLDOWN_MS) {
                    return;
                }
                this.redstoneNotified.put(key, now);
                ChallengeToast.addOrUpdate(new class_1799(class_1802.field_8725), name(), class_2561.method_43470("Redstone " + pos.method_10263() + ", " + pos.method_10264() + ", " + pos.method_10260()), this.sound.get());
            }
        }
    }

    private static boolean isRedstone(class_2680 state) {
        class_2248 block = state.method_26204();
        return (block instanceof class_2457) || (block instanceof class_2312) || (block instanceof class_2459) || (block instanceof class_2453) || (block instanceof class_2426) || (block instanceof class_2665) || (block instanceof class_2671) || (block instanceof class_2667) || (block instanceof class_2315) || (block instanceof class_2377) || (block instanceof class_2428) || (block instanceof class_2309) || (block instanceof class_2401) || (block instanceof class_2269) || (block instanceof class_2231) || (block instanceof class_2537) || (block instanceof class_4850) || (block instanceof class_8886) || (block instanceof class_2436);
    }

    private boolean isOwnChunk(long key) {
        if (this.passedChunks.contains(key)) {
            return true;
        }
        class_310 mc = class_310.method_1551();
        return mc.field_1724 != null && mc.field_1724.method_31476().method_8324() == key;
    }

    private void applyInitializedSections(class_6606 data, Map<Integer, byte[]> sections, int chunkX, int chunkZ, boolean compare, ChangeSummary summary) {
        BitSet mask = data.method_38608();
        List<byte[]> updates = data.method_38610();
        int next = 0;
        int iNextSetBit = mask.nextSetBit(0);
        while (true) {
            int bit = iNextSetBit;
            if (bit >= 0 && next < updates.size()) {
                int i = next;
                next++;
                byte[] update = updates.get(i);
                int sectionY = lightSectionY(bit);
                if (isWorldSection(sectionY) && update.length == 2048) {
                    updateSection(sections, chunkX, chunkZ, sectionY, Arrays.copyOf(update, 2048), compare, summary);
                }
                iNextSetBit = mask.nextSetBit(bit + 1);
            } else {
                return;
            }
        }
    }

    private void applyUninitializedSections(class_6606 data, Map<Integer, byte[]> sections, int chunkX, int chunkZ, boolean compare, ChangeSummary summary) {
        BitSet empty = data.method_38609();
        int iNextSetBit = empty.nextSetBit(0);
        while (true) {
            int bit = iNextSetBit;
            if (bit >= 0) {
                int sectionY = lightSectionY(bit);
                if (isWorldSection(sectionY)) {
                    updateSection(sections, chunkX, chunkZ, sectionY, ZERO_SECTION, compare, summary);
                }
                iNextSetBit = empty.nextSetBit(bit + 1);
            } else {
                return;
            }
        }
    }

    private void updateSection(Map<Integer, byte[]> sections, int chunkX, int chunkZ, int sectionY, byte[] light, boolean compare, ChangeSummary summary) {
        byte[] previous = sections.get(Integer.valueOf(sectionY));
        if (compare && previous != null && summary != null) {
            compareSection(previous, light, chunkX, chunkZ, sectionY, summary);
        }
        sections.put(Integer.valueOf(sectionY), light);
    }

    private void compareSection(byte[] previous, byte[] light, int chunkX, int chunkZ, int sectionY, ChangeSummary summary) {
        for (int i = 0; i < 2048; i++) {
            int was = previous[i] & 255;
            int now = light[i] & 255;
            int changed = was ^ now;
            if ((changed & 15) != 0) {
                recordChange(chunkX, chunkZ, sectionY, i << 1, was & 15, now & 15, summary);
            }
            if ((changed & 240) != 0) {
                recordChange(chunkX, chunkZ, sectionY, (i << 1) + 1, (was >>> 4) & 15, (now >>> 4) & 15, summary);
            }
        }
    }

    private void recordChange(int chunkX, int chunkZ, int sectionY, int index, int oldValue, int newValue, ChangeSummary summary) {
        int x = (chunkX << 4) + (index & 15);
        int y = (sectionY << 4) + ((index >>> 8) & 15);
        int z = (chunkZ << 4) + ((index >>> 4) & 15);
        int value = Math.max(oldValue, newValue);
        if (fromAmethyst(x, y, z, oldValue, newValue) || fromRedstoneOre(x, y, z, value) || fromLamp(x, y, z, value)) {
            return;
        }
        summary.add(x, y, z, oldValue, newValue);
    }

    private boolean fromLamp(int x, int y, int z, int value) {
        class_638 level;
        if (value > 14 || (level = class_310.method_1551().field_1687) == null) {
            return false;
        }
        int reach = 14 - value;
        for (int sx = (x - reach) >> 4; sx <= ((x + reach) >> 4); sx++) {
            for (int sy = (y - reach) >> 4; sy <= ((y + reach) >> 4); sy++) {
                for (int sz = (z - reach) >> 4; sz <= ((z + reach) >> 4); sz++) {
                    for (int lamp : lamps(level, sx, sy, sz)) {
                        int index = lamp >>> 4;
                        int lx = (sx << 4) + (index & 15);
                        int ly = (sy << 4) + (index >>> 8);
                        int lz = (sz << 4) + ((index >>> 4) & 15);
                        if (Math.abs(lx - x) + Math.abs(ly - y) + Math.abs(lz - z) <= (lamp & 15) - value) {
                            return true;
                        }
                    }
                }
            }
        }
        long now = class_156.method_658();
        ObjectIterator it = this.goneLamps.long2LongEntrySet().iterator();
        while (it.hasNext()) {
            Long2LongMap.Entry gone = (Long2LongMap.Entry) it.next();
            long packed = gone.getLongValue();
            if ((packed >>> 4) >= now) {
                long pos = gone.getLongKey();
                int distance = Math.abs(class_2338.method_10061(pos) - x) + Math.abs(class_2338.method_10071(pos) - y) + Math.abs(class_2338.method_10083(pos) - z);
                if (distance <= ((int) (packed & 15)) - value) {
                    return true;
                }
            }
        }
        return false;
    }

    private int[] lamps(class_638 level, int sectionX, int sectionY, int sectionZ) {
        long key = class_4076.method_18685(sectionX, sectionY, sectionZ);
        int[] cached = (int[]) this.lampSections.get(key);
        if (cached != null) {
            return cached;
        }
        int[] found = scanLamps(level, sectionX, sectionY, sectionZ);
        this.lampSections.put(key, found);
        return found;
    }

    private static int[] scanLamps(class_638 level, int sectionX, int sectionY, int sectionZ) {
        if (sectionY < level.method_32891() || sectionY > level.method_31597()) {
            return NO_LAMPS;
        }
        class_2818 chunk = level.method_2935().method_2857(sectionX, sectionZ, class_2806.field_12803, false);
        if (chunk == null) {
            return NO_LAMPS;
        }
        class_2826 section = chunk.method_38259(chunk.method_31603(sectionY));
        if (section.method_38292() || !section.method_12265().method_19526(PrimeChunkFinderModule::isLamp)) {
            return NO_LAMPS;
        }
        IntArrayList lamps = new IntArrayList();
        for (int index = 0; index < 4096; index++) {
            class_2680 state = section.method_12254(index & 15, index >>> 8, (index >>> 4) & 15);
            if (isLamp(state)) {
                lamps.add((index << 4) | state.method_26213());
            }
        }
        return lamps.isEmpty() ? NO_LAMPS : lamps.toIntArray();
    }

    private static boolean isLamp(class_2680 state) {
        return state.method_27852(class_2246.field_10336) || state.method_27852(class_2246.field_10099) || state.method_27852(class_2246.field_22092) || state.method_27852(class_2246.field_22093) || state.method_27852(class_2246.field_61902) || state.method_27852(class_2246.field_61903) || class_5803.method_33618(state);
    }

    private boolean fromAmethyst(int x, int y, int z, int oldValue, int newValue) {
        class_638 level;
        if (Math.max(oldValue, newValue) > 5 || (level = class_310.method_1551().field_1687) == null || !amethystSectionNear(level, x, y, z)) {
            return false;
        }
        for (int dx = -5; dx <= 5; dx++) {
            int restX = 5 - Math.abs(dx);
            for (int dy = -restX; dy <= restX; dy++) {
                int restY = restX - Math.abs(dy);
                for (int dz = -restY; dz <= restY; dz++) {
                    if (isAmethyst(level.method_8320(this.cursor.method_10103(x + dx, y + dy, z + dz)))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean amethystSectionNear(class_638 level, int x, int y, int z) {
        for (int sx = (x - 5) >> 4; sx <= ((x + 5) >> 4); sx++) {
            for (int sy = (y - 5) >> 4; sy <= ((y + 5) >> 4); sy++) {
                for (int sz = (z - 5) >> 4; sz <= ((z + 5) >> 4); sz++) {
                    long key = class_4076.method_18685(sx, sy, sz);
                    if (this.checkedSections.add(key) && sectionHasAmethyst(level, sx, sy, sz)) {
                        this.amethystSections.add(key);
                    }
                    if (this.amethystSections.contains(key)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean sectionHasAmethyst(class_638 level, int sectionX, int sectionY, int sectionZ) {
        class_2818 chunk;
        if (sectionY < level.method_32891() || sectionY > level.method_31597() || (chunk = level.method_2935().method_2857(sectionX, sectionZ, class_2806.field_12803, false)) == null) {
            return false;
        }
        class_2826 section = chunk.method_38259(chunk.method_31603(sectionY));
        return !section.method_38292() && section.method_12265().method_19526(PrimeChunkFinderModule::isAmethyst);
    }

    private boolean fromRedstoneOre(int x, int y, int z, int value) {
        class_638 level;
        if (value > 9 || (level = class_310.method_1551().field_1687) == null) {
            return false;
        }
        int reach = 9 - value;
        for (int sx = (x - reach) >> 4; sx <= ((x + reach) >> 4); sx++) {
            for (int sy = (y - reach) >> 4; sy <= ((y + reach) >> 4); sy++) {
                for (int sz = (z - reach) >> 4; sz <= ((z + reach) >> 4); sz++) {
                    for (long ore : exposedOres(level, sx, sy, sz)) {
                        int distance = Math.abs(class_2338.method_10061(ore) - x) + Math.abs(class_2338.method_10071(ore) - y) + Math.abs(class_2338.method_10083(ore) - z);
                        if (distance <= reach) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private long[] exposedOres(class_638 level, int sectionX, int sectionY, int sectionZ) {
        long key = class_4076.method_18685(sectionX, sectionY, sectionZ);
        long[] cached = (long[]) this.oreSections.get(key);
        if (cached != null) {
            return cached;
        }
        long[] found = scanExposedOres(level, sectionX, sectionY, sectionZ);
        this.oreSections.put(key, found);
        return found;
    }

    private long[] scanExposedOres(class_638 level, int sectionX, int sectionY, int sectionZ) {
        if (sectionY < level.method_32891() || sectionY > level.method_31597()) {
            return NO_ORES;
        }
        class_2818 chunk = level.method_2935().method_2857(sectionX, sectionZ, class_2806.field_12803, false);
        if (chunk == null) {
            return NO_ORES;
        }
        class_2826 section = chunk.method_38259(chunk.method_31603(sectionY));
        if (section.method_38292() || !section.method_12265().method_19526(PrimeChunkFinderModule::isRedstoneOre)) {
            return NO_ORES;
        }
        LongArrayList ores = new LongArrayList();
        for (int ly = 0; ly < 16; ly++) {
            for (int lz = 0; lz < 16; lz++) {
                for (int lx = 0; lx < 16; lx++) {
                    if (isRedstoneOre(section.method_12254(lx, ly, lz))) {
                        int wx = (sectionX << 4) + lx;
                        int wy = (sectionY << 4) + ly;
                        int wz = (sectionZ << 4) + lz;
                        if (exposed(level, wx, wy, wz)) {
                            ores.add(class_2338.method_10064(wx, wy, wz));
                        }
                    }
                }
            }
        }
        return ores.isEmpty() ? NO_ORES : ores.toLongArray();
    }

    private boolean exposed(class_638 level, int x, int y, int z) {
        for (class_2350 direction : class_2350.values()) {
            class_2680 neighbour = level.method_8320(this.cursor.method_10103(x + direction.method_10148(), y + direction.method_10164(), z + direction.method_10165()));
            if (neighbour.method_26215() || !neighbour.method_26225()) {
                return true;
            }
        }
        return false;
    }

    private static boolean isRedstoneOre(class_2680 state) {
        return state.method_27852(class_2246.field_10080) || state.method_27852(class_2246.field_29030);
    }

    private static boolean isAmethyst(class_2680 state) {
        return state.method_27852(class_2246.field_27161) || state.method_27852(class_2246.field_27162) || state.method_27852(class_2246.field_27163) || state.method_27852(class_2246.field_27164) || state.method_27852(class_2246.field_27160) || state.method_27852(class_2246.field_27159);
    }

    private Map<Integer, byte[]> createZeroedChunk() {
        Map<Integer, byte[]> sections = new HashMap<>();
        for (int sectionY = bottomSection(); sectionY <= topSection(); sectionY++) {
            sections.put(Integer.valueOf(sectionY), ZERO_SECTION);
        }
        return sections;
    }

    private int lightSectionY(int index) {
        class_638 level = class_310.method_1551().field_1687;
        return level == null ? ImGuiWindowFlags.NavFlattened : level.method_2935().method_12130().method_31929() + index;
    }

    private int bottomSection() {
        return class_310.method_1551().field_1687.method_32891();
    }

    private int topSection() {
        return class_310.method_1551().field_1687.method_31597();
    }

    private boolean isWorldSection(int sectionY) {
        return sectionY >= bottomSection() && sectionY <= topSection();
    }

    private void trimChunkCache() {
        while (this.chunkLight.size() > MAX_TRACKED_CHUNKS) {
            Iterator<Long> keys = this.chunkLight.keySet().iterator();
            keys.next();
            keys.remove();
        }
    }

    private void reset() {
        this.chunkLight.clear();
        this.passedChunks.clear();
        this.flaggedChunks.clear();
        this.shownChunks.clear();
        this.redstoneNotified.clear();
        this.lampSections.clear();
        this.goneLamps.clear();
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/PrimeChunkFinderModule$ChangeSummary.class */
    @Environment(EnvType.CLIENT)
    private static final class ChangeSummary {
        int count;
        int x;
        int y;
        int z;
        int oldValue;
        int newValue;

        private ChangeSummary() {
        }

        void add(int x, int y, int z, int oldValue, int newValue) {
            if (this.count == 0 || Math.abs(newValue - oldValue) > Math.abs(this.newValue - this.oldValue)) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.oldValue = oldValue;
                this.newValue = newValue;
            }
            this.count++;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/PrimeChunkFinderModule$DeepslateToast.class */
    @Environment(EnvType.CLIENT)
    public static final class DeepslateToast extends ChallengeToast {
        private static final class_2561 TITLE = class_2561.method_43470("Deepslate Level Only");
        private static final class_2561 MESSAGE = class_2561.method_43470("Go below Y 0 first.");

        DeepslateToast() {
            super(new class_1799(class_1802.field_28866), TITLE, MESSAGE, true);
        }
    }
}
