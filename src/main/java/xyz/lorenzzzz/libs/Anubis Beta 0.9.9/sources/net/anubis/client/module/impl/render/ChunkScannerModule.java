package net.anubis.client.module.impl.render;

import imgui.flag.ImGuiWindowFlags;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntMaps;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.ref.WeakReference;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.OptionalLong;
import net.anubis.client.chat.ModuleChat;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.BlockUpdateEvent;
import net.anubis.client.event.impl.PacketAppliedEvent;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.gui.ChallengeToast;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.impl.client.PerformanceModule;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.module.setting.StringSetting;
import net.anubis.client.util.ChunkAreas;
import net.anubis.client.util.ChunkOverlay;
import net.anubis.client.util.LevelUtil;
import net.anubis.client.util.seedscan.AdvancedScanner;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1923;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2561;
import net.minecraft.class_2672;
import net.minecraft.class_2680;
import net.minecraft.class_2818;
import net.minecraft.class_2826;
import net.minecraft.class_310;
import net.minecraft.class_5285;
import net.minecraft.class_634;
import net.minecraft.class_638;
import net.minecraft.class_9848;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/ChunkScannerModule.class */
@Environment(EnvType.CLIENT)
public final class ChunkScannerModule extends ClientModule {
    private static final int PROBE_CHUNKS = 48;
    private static final byte OTHER = 0;
    private static final byte STONE = 1;
    private static final byte BLOB = 2;
    private static final byte AIR = 3;
    private static final byte MISSING = 4;
    private static final int MIN_Y = 0;
    private static final int MAX_Y = 32;
    private static final int CHUNK_COLOR = 1627389951;
    private static final int OLD_CHUNK_COLOR = 1614011733;
    private static final int SEED_SETTLE_TICKS = 20;
    private final BooleanSetting advancedScanner;
    private final StringSetting seed;
    private final NumberSetting stackHeight;
    private final NumberSetting spreadRadius;
    private final ColorSetting chunkColor;
    private final BooleanSetting tracers;
    private final BooleanSetting sound;
    private final ChunkAreas chunkAreas;
    private final ChunkOverlay chunkOverlay;
    private final Long2ObjectOpenHashMap<Find> flagged;
    private final LongArrayFIFOQueue queue;
    private final LongOpenHashSet queued;
    private final List<Find> fresh;
    private final Long2IntOpenHashMap heat;
    private int appliedRadius;
    private LongOpenHashSet shown;
    private class_638 lastLevel;
    private boolean scanning;
    private AdvancedScanner advanced;
    private boolean appliedAdvanced;
    private String seedText;
    private int seedStill;
    private boolean seedAsked;
    private ScanConfig appliedConfig;
    private WeakReference<class_634> probeServer;
    private boolean probing;
    private final LongOpenHashSet probed;
    private final class_2818[] around;
    private final int[] groupX;
    private final int[] groupZ;
    private int groupSize;
    private boolean guessed;
    private class_2248 wall;
    private class_2248 lastBlock;
    private final int[] stackTop;
    private final int[] stackShape;
    private final int[] stackAreas;
    private final boolean[] stackGuessed;
    private final class_2248[] stackWall;
    private boolean sawBlob;
    private int scanX;
    private int scanZ;
    private int keepHeight;
    private long[] keptStacks;
    private int keptStackCount;
    private int tallest;
    private long bottom;
    private boolean unsure;
    private static final int[] SIDE_X = {1, -1, 0, 0};
    private static final int[] SIDE_Z = {0, 0, 1, -1};
    private static final int[] CORNER_X = {1, 1, -1, -1};
    private static final int[] CORNER_Z = {1, -1, 1, -1};
    private static final Find UNDECIDED = new Find(0, 0, new long[0]);

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/ChunkScannerModule$Find.class */
    @Environment(EnvType.CLIENT)
    private static final class Find extends Record {
        private final long pos;
        private final int height;
        private final long[] stacks;

        private Find(long pos, int height, long[] stacks) {
            this.pos = pos;
            this.height = height;
            this.stacks = stacks;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Find.class), Find.class, "pos;height;stacks", "FIELD:Lnet/anubis/client/module/impl/render/ChunkScannerModule$Find;->pos:J", "FIELD:Lnet/anubis/client/module/impl/render/ChunkScannerModule$Find;->height:I", "FIELD:Lnet/anubis/client/module/impl/render/ChunkScannerModule$Find;->stacks:[J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Find.class), Find.class, "pos;height;stacks", "FIELD:Lnet/anubis/client/module/impl/render/ChunkScannerModule$Find;->pos:J", "FIELD:Lnet/anubis/client/module/impl/render/ChunkScannerModule$Find;->height:I", "FIELD:Lnet/anubis/client/module/impl/render/ChunkScannerModule$Find;->stacks:[J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Find.class, Object.class), Find.class, "pos;height;stacks", "FIELD:Lnet/anubis/client/module/impl/render/ChunkScannerModule$Find;->pos:J", "FIELD:Lnet/anubis/client/module/impl/render/ChunkScannerModule$Find;->height:I", "FIELD:Lnet/anubis/client/module/impl/render/ChunkScannerModule$Find;->stacks:[J").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public long pos() {
            return this.pos;
        }

        public int height() {
            return this.height;
        }

        public long[] stacks() {
            return this.stacks;
        }

        boolean sameAs(Find other) {
            return this.pos == other.pos && this.height == other.height && Arrays.equals(this.stacks, other.stacks);
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/ChunkScannerModule$ScanConfig.class */
    @Environment(EnvType.CLIENT)
    private static final class ScanConfig extends Record {
        private final int minY;
        private final int maxY;
        private final int stackHeight;

        private ScanConfig(int minY, int maxY, int stackHeight) {
            this.minY = minY;
            this.maxY = maxY;
            this.stackHeight = stackHeight;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, ScanConfig.class), ScanConfig.class, "minY;maxY;stackHeight", "FIELD:Lnet/anubis/client/module/impl/render/ChunkScannerModule$ScanConfig;->minY:I", "FIELD:Lnet/anubis/client/module/impl/render/ChunkScannerModule$ScanConfig;->maxY:I", "FIELD:Lnet/anubis/client/module/impl/render/ChunkScannerModule$ScanConfig;->stackHeight:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, ScanConfig.class), ScanConfig.class, "minY;maxY;stackHeight", "FIELD:Lnet/anubis/client/module/impl/render/ChunkScannerModule$ScanConfig;->minY:I", "FIELD:Lnet/anubis/client/module/impl/render/ChunkScannerModule$ScanConfig;->maxY:I", "FIELD:Lnet/anubis/client/module/impl/render/ChunkScannerModule$ScanConfig;->stackHeight:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, ScanConfig.class, Object.class), ScanConfig.class, "minY;maxY;stackHeight", "FIELD:Lnet/anubis/client/module/impl/render/ChunkScannerModule$ScanConfig;->minY:I", "FIELD:Lnet/anubis/client/module/impl/render/ChunkScannerModule$ScanConfig;->maxY:I", "FIELD:Lnet/anubis/client/module/impl/render/ChunkScannerModule$ScanConfig;->stackHeight:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int minY() {
            return this.minY;
        }

        public int maxY() {
            return this.maxY;
        }

        public int stackHeight() {
            return this.stackHeight;
        }
    }

    public ChunkScannerModule() {
        super("chunk-scanner", "Chunk Scanner", "Marks suspicious chunks with signs of player activity.", ModuleCategory.BASE_RAID);
        this.advancedScanner = (BooleanSetting) addSetting("advanced-scanner", new BooleanSetting("Advanced Scanner", false));
        this.seed = (StringSetting) addSetting("seed", new StringSetting("Seed", ""));
        this.stackHeight = (NumberSetting) addSetting("stack-height", new NumberSetting("Sensitivity", 2.0f, 2.0f, 5.0f).step(1.0f));
        this.spreadRadius = (NumberSetting) addSetting("spread-radius", new NumberSetting("Spread Radius", 1.0f, 0.0f, 16.0f).step(1.0f));
        this.chunkColor = (ColorSetting) addSetting("chunk-color", new ColorSetting("Chunk Color", CHUNK_COLOR));
        this.tracers = (BooleanSetting) addSetting("tracers", new BooleanSetting("Tracers", true));
        this.sound = (BooleanSetting) addSetting("sound", new BooleanSetting("Sound", true));
        this.chunkAreas = new ChunkAreas();
        this.chunkOverlay = new ChunkOverlay();
        this.flagged = new Long2ObjectOpenHashMap<>();
        this.queue = new LongArrayFIFOQueue();
        this.queued = new LongOpenHashSet();
        this.fresh = new ArrayList();
        this.heat = new Long2IntOpenHashMap();
        this.shown = new LongOpenHashSet();
        this.probeServer = new WeakReference<>(null);
        this.probed = new LongOpenHashSet();
        this.around = new class_2818[9];
        this.groupX = new int[4];
        this.groupZ = new int[4];
        this.stackTop = new int[256];
        this.stackShape = new int[256];
        this.stackAreas = new int[256];
        this.stackGuessed = new boolean[256];
        this.stackWall = new class_2248[256];
        this.keptStacks = new long[16];
        BooleanSetting notifications = usesNotifications();
        BooleanSetting booleanSetting = this.sound;
        Objects.requireNonNull(notifications);
        booleanSetting.visibleWhen(notifications::get);
        this.stackHeight.visibleWhen(() -> {
            return !this.advancedScanner.get();
        });
        StringSetting stringSetting = this.seed;
        BooleanSetting booleanSetting2 = this.advancedScanner;
        Objects.requireNonNull(booleanSetting2);
        stringSetting.visibleWhen(booleanSetting2::get);
        this.seed.privateValue();
    }

    @Override // net.anubis.client.module.ClientModule
    public int settingsVersion() {
        return 1;
    }

    @Override // net.anubis.client.module.ClientModule
    public void migrateSettings(int savedVersion) {
        if (savedVersion >= 1 || this.chunkColor.argb() != OLD_CHUNK_COLOR) {
            return;
        }
        this.chunkColor.setArgb(CHUNK_COLOR);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        startLevel(class_310.method_1551().field_1687);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        startLevel(null);
    }

    private void startLevel(class_638 level) {
        closeAdvanced();
        this.appliedAdvanced = this.advancedScanner.get();
        this.flagged.clear();
        this.heat.clear();
        this.appliedRadius = this.spreadRadius.getInt();
        this.queue.clear();
        this.queued.clear();
        this.fresh.clear();
        this.shown = new LongOpenHashSet();
        this.lastLevel = level;
        this.scanning = (level == null || level.method_27983() == class_1937.field_25180 || level.method_27983() == class_1937.field_25181) ? false : true;
        if (this.scanning) {
            class_634 connection = class_310.method_1551().method_1562();
            if (connection != this.probeServer.get()) {
                this.probeServer = new WeakReference<>(connection);
                this.probing = true;
                this.probed.clear();
            }
            this.appliedConfig = currentConfig(level);
            queueLoadedChunks(level);
        }
    }

    private ScanConfig currentConfig(class_638 level) {
        return new ScanConfig(Math.max(0, level.method_31607()), Math.min(32, level.method_31600()), this.stackHeight.getInt());
    }

    private void queueLoadedChunks(class_638 level) {
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null) {
            return;
        }
        int radius = ((Integer) mc.field_1690.method_42503().method_41753()).intValue() + 1;
        class_1923 center = mc.field_1724.method_31476();
        for (int x = center.field_9181 - radius; x <= center.field_9181 + radius; x++) {
            for (int z = center.field_9180 - radius; z <= center.field_9180 + radius; z++) {
                if (LevelUtil.isLoaded(level, x, z)) {
                    enqueue(class_1923.method_8331(x, z));
                }
            }
        }
    }

    private void enqueue(long key) {
        if (!this.scanning || this.advancedScanner.get() || !this.queued.add(key)) {
            return;
        }
        this.queue.enqueue(key);
    }

    @EventHandler
    public void onPacketApplied(PacketAppliedEvent event) {
        if (this.advanced != null) {
            class_2672 class_2672Var = event.packet;
            if (class_2672Var instanceof class_2672) {
                class_2672 arrived = class_2672Var;
                this.advanced.chunkArrived(arrived.method_11523(), arrived.method_11524());
            }
        }
        class_2672 class_2672Var2 = event.packet;
        if (class_2672Var2 instanceof class_2672) {
            class_2672 chunk = class_2672Var2;
            enqueue(class_1923.method_8331(chunk.method_11523(), chunk.method_11524()));
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx != 0 || dz != 0) {
                        enqueue(class_1923.method_8331(chunk.method_11523() + dx, chunk.method_11524() + dz));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockUpdate(BlockUpdateEvent event) {
        if (!this.scanning || event.pos == null) {
            return;
        }
        if (this.advanced != null) {
            this.advanced.blockChanged(event.pos);
        }
        int y = event.pos.method_10264();
        if (y < this.appliedConfig.minY() - 1 || y > this.appliedConfig.maxY() + 1) {
            return;
        }
        if (matters(event.oldState) || matters(event.newState)) {
            int x = event.pos.method_10263();
            int z = event.pos.method_10260();
            for (int cx = (x - 2) >> 4; cx <= ((x + 1) >> 4); cx++) {
                for (int cz = (z - 2) >> 4; cz <= ((z + 2) >> 4); cz++) {
                    enqueue(class_1923.method_8331(cx, cz));
                }
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:59:0x0162 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:67:0x00c0 A[SYNTHETIC] */
    @EventHandler
    public void onTick(TickEvent event) {
        class_638 level = class_310.method_1551().field_1687;
        if (level != this.lastLevel) {
            startLevel(level);
        }
        if (this.scanning) {
            if (this.advancedScanner.get() != this.appliedAdvanced) {
                startLevel(level);
            }
            ScanConfig config = currentConfig(level);
            if (!config.equals(this.appliedConfig)) {
                this.appliedConfig = config;
                queueLoadedChunks(level);
            }
            int radius = this.spreadRadius.getInt();
            if (radius != this.appliedRadius) {
                this.appliedRadius = radius;
                this.heat.clear();
                ObjectIterator<Long2ObjectMap.Entry<Find>> it = this.flagged.long2ObjectEntrySet().fastIterator();
                while (it.hasNext()) {
                    Long2ObjectMap.Entry<Find> entry = (Long2ObjectMap.Entry) it.next();
                    spread(entry.getLongKey(), (Find) entry.getValue(), 1);
                }
            }
            long deadline = System.nanoTime() + PerformanceModule.scanBudgetNanos();
            if (this.advancedScanner.get()) {
                tickAdvanced(level, deadline);
            }
            while (!this.queue.isEmpty() && System.nanoTime() < deadline) {
                long key = this.queue.dequeueLong();
                this.queued.remove(key);
                int cx = class_1923.method_8325(key);
                int cz = class_1923.method_8332(key);
                if (LevelUtil.isLoaded(level, cx, cz)) {
                    Find find = scan(level, cx, cz, config);
                    if (this.sawBlob) {
                        probe(key);
                    }
                    if (find != UNDECIDED) {
                        Find was = (Find) this.flagged.get(key);
                        if (find == null) {
                            if (was != null) {
                                refile(key, was, find);
                                if (was == null) {
                                    this.fresh.add(find);
                                }
                            }
                        } else if (was == null || !was.sameAs(find)) {
                            refile(key, was, find);
                            if (was == null) {
                                this.fresh.add(find);
                            }
                        }
                    }
                }
            }
            announce(this.fresh);
            this.fresh.clear();
            this.shown = computeShown(level);
        }
    }

    private void refile(long key, Find was, Find find) {
        if (was != null) {
            spread(key, was, -1);
        }
        if (find == null) {
            this.flagged.remove(key);
        } else {
            this.flagged.put(key, find);
            spread(key, find, 1);
        }
    }

    private void spread(long key, Find find, int sign) {
        int amount = sign * find.stacks().length;
        int r = this.appliedRadius;
        int cx = class_1923.method_8325(key);
        int cz = class_1923.method_8332(key);
        for (int x = cx - r; x <= cx + r; x++) {
            for (int z = cz - r; z <= cz + r; z++) {
                long area = class_1923.method_8331(x, z);
                if (this.heat.addTo(area, amount) + amount == 0) {
                    this.heat.remove(area);
                }
            }
        }
    }

    private LongOpenHashSet computeShown(class_638 level) {
        Long2IntOpenHashMap candidates = new Long2IntOpenHashMap();
        ObjectIterator it = Long2IntMaps.fastIterable(this.heat).iterator();
        while (it.hasNext()) {
            Long2IntMap.Entry entry = (Long2IntMap.Entry) it.next();
            long key = entry.getLongKey();
            if (LevelUtil.isLoaded(level, class_1923.method_8325(key), class_1923.method_8332(key))) {
                candidates.put(key, entry.getIntValue());
            }
        }
        return this.appliedRadius == 0 ? new LongOpenHashSet(candidates.keySet()) : hottestPerPatch(candidates);
    }

    private static LongOpenHashSet hottestPerPatch(Long2IntOpenHashMap candidates) {
        LongOpenHashSet kept = new LongOpenHashSet();
        LongOpenHashSet seen = new LongOpenHashSet();
        LongArrayList patch = new LongArrayList();
        LongArrayFIFOQueue open = new LongArrayFIFOQueue();
        LongIterator it = candidates.keySet().iterator();
        while (it.hasNext()) {
            long start = ((Long) it.next()).longValue();
            if (seen.add(start)) {
                patch.clear();
                open.enqueue(start);
                int max = 0;
                while (!open.isEmpty()) {
                    long key = open.dequeueLong();
                    patch.add(key);
                    max = Math.max(max, candidates.get(key));
                    int cx = class_1923.method_8325(key);
                    int cz = class_1923.method_8332(key);
                    for (int side = 0; side < 4; side++) {
                        long next = class_1923.method_8331(cx + SIDE_X[side], cz + SIDE_Z[side]);
                        if (candidates.containsKey(next) && seen.add(next)) {
                            open.enqueue(next);
                        }
                    }
                }
                for (int i = 0; i < patch.size(); i++) {
                    if (candidates.get(patch.getLong(i)) == max) {
                        kept.add(patch.getLong(i));
                    }
                }
            }
        }
        return kept;
    }

    private Find scan(class_638 level, int chunkX, int chunkZ, ScanConfig config) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                this.around[((dx + 1) * 3) + dz + 1] = level.method_2935().method_21730(chunkX + dx, chunkZ + dz);
            }
        }
        this.sawBlob = false;
        this.scanX = chunkX << 4;
        this.scanZ = chunkZ << 4;
        this.keepHeight = config.stackHeight();
        this.keptStackCount = 0;
        this.tallest = 0;
        this.bottom = 0L;
        this.unsure = false;
        try {
            class_2818 chunk = this.around[4];
            class_2826[] sections = chunk.method_12006();
            int minSectionY = chunk.method_32891();
            Arrays.fill(this.stackTop, ImGuiWindowFlags.NavFlattened);
            boolean blank = true;
            for (int i = 0; i < sections.length; i++) {
                class_2826 section = sections[i];
                if (section != null && !section.method_38292()) {
                    int baseY = (minSectionY + i) << 4;
                    if (baseY + 15 >= config.minY() && baseY <= config.maxY()) {
                        int fromY = Math.max(config.minY() - baseY, 0);
                        int toY = Math.min(config.maxY() - baseY, 15);
                        if (blank) {
                            blank = !hasBlocks(section, fromY, toY);
                        }
                        if (section.method_19523(state -> {
                            return state.method_27852(class_2246.field_10340);
                        }) && section.method_19523(state2 -> {
                            return isBlob(state2.method_26204());
                        })) {
                            for (int y = fromY; y <= toY; y++) {
                                for (int z = 0; z < 16; z++) {
                                    boolean afterStone = false;
                                    for (int x = 0; x < 16; x++) {
                                        class_2248 block = section.method_12254(x, y, z).method_26204();
                                        boolean stone = block == class_2246.field_10340;
                                        if (stone) {
                                            if (!afterStone && isArea(x, baseY + y, z)) {
                                                stack(x, baseY + y, z);
                                            }
                                        } else if (this.probing && isBlob(block)) {
                                            this.sawBlob = true;
                                            if (enclosed(x, baseY + y, z)) {
                                                this.probing = false;
                                                this.probed.clear();
                                            }
                                        }
                                        afterStone = stone;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for (int i2 = 0; i2 < 256; i2++) {
                keep(i2);
            }
            if (this.tallest >= config.stackHeight()) {
                return new Find(this.bottom, this.tallest, Arrays.copyOf(this.keptStacks, this.keptStackCount));
            }
            return (this.unsure || blank) ? UNDECIDED : null;
        } finally {
            Arrays.fill(this.around, (Object) null);
        }
    }

    private void stack(int x, int y, int z) {
        int i = (z << 4) | x;
        int shape = shape(x, z);
        boolean onTop = this.stackTop[i] == y - 1 && this.stackShape[i] == shape && this.stackWall[i] == this.wall;
        if (!onTop) {
            keep(i);
        }
        this.stackTop[i] = y;
        this.stackShape[i] = shape;
        this.stackWall[i] = this.wall;
        this.stackAreas[i] = onTop ? this.stackAreas[i] + 1 : 1;
        this.stackGuessed[i] = this.guessed || (onTop && this.stackGuessed[i]);
    }

    private void keep(int i) {
        int height = this.stackAreas[i];
        int top = this.stackTop[i];
        if (top == Integer.MIN_VALUE || height < this.keepHeight) {
            return;
        }
        int shape = this.stackShape[i];
        if (goesOn(i & 15, top + 1, i >> 4, shape) && goesOn(i & 15, top - height, i >> 4, shape)) {
            if (this.stackGuessed[i]) {
                this.unsure = true;
                return;
            }
            int x = this.scanX + (i & 15);
            int z = this.scanZ + (i >> 4);
            long lowest = class_2338.method_10064(x, (top - height) + 1, z);
            if (height > this.tallest || (height == this.tallest && before(lowest, this.bottom))) {
                this.tallest = height;
                this.bottom = lowest;
            }
            if (this.keptStackCount == this.keptStacks.length) {
                this.keptStacks = Arrays.copyOf(this.keptStacks, this.keptStacks.length * 2);
            }
            long[] jArr = this.keptStacks;
            int i2 = this.keptStackCount;
            this.keptStackCount = i2 + 1;
            jArr[i2] = lowest;
        }
    }

    private boolean goesOn(int x0, int y, int z0, int shape) {
        for (int bit = 0; bit < 6; bit++) {
            if (((shape >> bit) & 1) != 0) {
                kind(x0 + (bit / 3), y, (z0 + (bit % 3)) - 1);
                if (!carriesStone(this.lastBlock)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean before(long a, long b) {
        if (class_2338.method_10071(a) != class_2338.method_10071(b)) {
            return class_2338.method_10071(a) < class_2338.method_10071(b);
        }
        if (class_2338.method_10083(a) != class_2338.method_10083(b)) {
            return class_2338.method_10083(a) < class_2338.method_10083(b);
        }
        return class_2338.method_10061(a) < class_2338.method_10061(b);
    }

    private int shape(int x0, int z0) {
        int shape = 0;
        for (int i = 0; i < this.groupSize; i++) {
            shape |= 1 << (((((this.groupX[i] - x0) * 3) + this.groupZ[i]) - z0) + 1);
        }
        return shape;
    }

    private boolean isArea(int x0, int y, int z0) {
        this.guessed = false;
        this.wall = null;
        if (seen(x0 - 1, y, z0) != 2 || !sameWall() || seen(x0, y, z0 - 1) != 2 || !sameWall()) {
            return false;
        }
        this.groupX[0] = x0;
        this.groupZ[0] = z0;
        int size = 1;
        int maxX = x0;
        int minZ = z0;
        int maxZ = z0;
        for (int i = 0; i < size; i++) {
            for (int side = 0; side < 4; side++) {
                int x = this.groupX[i] + SIDE_X[side];
                int z = this.groupZ[i] + SIDE_Z[side];
                if (!inGroup(x, z, size)) {
                    byte kind = seen(x, y, z);
                    if (kind == 2) {
                        if (!sameWall()) {
                            return false;
                        }
                    } else {
                        if (kind != 1 || x < x0) {
                            return false;
                        }
                        if (x == x0 && z < z0) {
                            return false;
                        }
                        maxX = Math.max(maxX, x);
                        minZ = Math.min(minZ, z);
                        maxZ = Math.max(maxZ, z);
                        if (maxX - x0 > 1 || maxZ - minZ > 1) {
                            return false;
                        }
                        this.groupX[size] = x;
                        this.groupZ[size] = z;
                        size++;
                    }
                }
            }
        }
        for (int i2 = 0; i2 < size; i2++) {
            for (int corner = 0; corner < 4; corner++) {
                int x2 = this.groupX[i2] + CORNER_X[corner];
                int z2 = this.groupZ[i2] + CORNER_Z[corner];
                if (!inGroup(x2, z2, size) && (seen(x2, y, z2) != 2 || !sameWall())) {
                    return false;
                }
            }
        }
        for (int i3 = 0; i3 < size; i3++) {
            if (kind(this.groupX[i3], y + 1, this.groupZ[i3]) == 3 || kind(this.groupX[i3], y - 1, this.groupZ[i3]) == 3) {
                return false;
            }
        }
        this.groupSize = size;
        return true;
    }

    private boolean inGroup(int x, int z, int size) {
        for (int i = 0; i < size; i++) {
            if (this.groupX[i] == x && this.groupZ[i] == z) {
                return true;
            }
        }
        return false;
    }

    private byte seen(int x, int y, int z) {
        byte kind = kind(x, y, z);
        if (kind != 4) {
            return kind;
        }
        this.guessed = true;
        return (byte) 2;
    }

    private boolean sameWall() {
        if (this.lastBlock == null) {
            return true;
        }
        if (this.wall == null) {
            this.wall = this.lastBlock;
        }
        return this.lastBlock == this.wall;
    }

    private byte kind(int x, int y, int z) {
        class_2818 chunk = this.around[(((x >> 4) + 1) * 3) + (z >> 4) + 1];
        this.lastBlock = null;
        if (chunk == null) {
            return (byte) 4;
        }
        int index = chunk.method_31602(y);
        if (index < 0 || index >= chunk.method_32890()) {
            return (byte) 0;
        }
        class_2680 state = chunk.method_38259(index).method_12254(x & 15, y & 15, z & 15);
        this.lastBlock = state.method_26204();
        return kindOf(state);
    }

    private boolean enclosed(int x, int y, int z) {
        return full(x + 1, y, z) && full(x - 1, y, z) && full(x, y, z + 1) && full(x, y, z - 1) && full(x, y + 1, z) && full(x, y - 1, z);
    }

    private boolean full(int x, int y, int z) {
        int index;
        class_2818 chunk = this.around[(((x >> 4) + 1) * 3) + (z >> 4) + 1];
        return chunk != null && (index = chunk.method_31602(y)) >= 0 && index < chunk.method_32890() && chunk.method_38259(index).method_12254(x & 15, y & 15, z & 15).method_26216();
    }

    private static boolean hasBlocks(class_2826 section, int fromY, int toY) {
        if (!section.method_19523(state -> {
            return !state.method_26215();
        })) {
            return false;
        }
        for (int y = fromY; y <= toY; y++) {
            for (int z = 0; z < 16; z++) {
                for (int x = 0; x < 16; x++) {
                    if (!section.method_12254(x, y, z).method_26215()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static byte kindOf(class_2680 state) {
        class_2248 block = state.method_26204();
        if (block == class_2246.field_10340) {
            return (byte) 1;
        }
        if (isBlob(block)) {
            return (byte) 2;
        }
        return state.method_26215() ? (byte) 3 : (byte) 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isBlob(class_2248 block) {
        return block == class_2246.field_10508 || block == class_2246.field_10115 || block == class_2246.field_10474 || block == class_2246.field_10255;
    }

    private static boolean carriesStone(class_2248 block) {
        return block == class_2246.field_10340 || block == class_2246.field_28888;
    }

    private static boolean matters(class_2680 state) {
        return state != null && (kindOf(state) != 0 || carriesStone(state.method_26204()));
    }

    private void probe(long key) {
        if (this.probing && this.probed.add(key) && this.probed.size() >= 48) {
            this.probing = false;
            this.probed.clear();
            ModuleChat.send(this, ModuleChat.body().text("This server ").warn("hides").text(" ores with anti-xray, so patched shafts can't be seen"));
        }
    }

    private void announce(List<Find> finds) {
        if (notificationsOn() && !finds.isEmpty()) {
            class_1799 icon = new class_1799(this.advancedScanner.get() ? class_1802.field_20401 : class_1802.field_27070);
            ChallengeToast.addOrUpdate(icon, name(), class_2561.method_43470("Suspect Chunk Scanned"), this.sound.get());
        }
    }

    private void tickAdvanced(class_638 level, long deadline) {
        String text = this.seed.get();
        if (!text.equals(this.seedText)) {
            this.seedStill = this.seedText == null ? 20 : 0;
            this.seedText = text;
        } else if (this.seedStill < 20) {
            this.seedStill++;
        }
        if (this.seedStill >= 20) {
            OptionalLong wanted = class_5285.method_46720(text);
            if (wanted.isEmpty()) {
                if (this.advanced != null) {
                    startLevel(level);
                }
                if (!this.seedAsked) {
                    this.seedAsked = true;
                    ModuleChat.send(this, ModuleChat.body().text("Advanced Scanner needs the world seed: ").warn("set Seed"));
                    return;
                }
                return;
            }
            this.seedAsked = false;
            if (this.advanced == null || this.advanced.seed() != wanted.getAsLong()) {
                if (this.advanced != null) {
                    startLevel(level);
                }
                this.advanced = AdvancedScanner.start(wanted.getAsLong());
            }
        }
        if (this.advanced == null) {
            return;
        }
        for (AdvancedScanner.ChunkResult result : this.advanced.tick(level, deadline)) {
            Find find = result.flagged() ? new Find(result.anchors()[0], result.blocks(), result.anchors()) : null;
            Find was = (Find) this.flagged.get(result.chunk());
            if (find == null) {
                if (was != null) {
                    refile(result.chunk(), was, find);
                    if (was != null && find != null) {
                        this.fresh.add(find);
                    }
                }
            } else if (was == null || !was.sameAs(find)) {
                refile(result.chunk(), was, find);
                if (was != null) {
                }
            }
        }
        for (String notice : this.advanced.drainNotices()) {
            ModuleChat.send(this, notice);
        }
    }

    private void closeAdvanced() {
        if (this.advanced != null) {
            this.advanced.close();
        }
        this.advanced = null;
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        if (class_310.method_1551().field_1687 == null) {
            return;
        }
        this.chunkOverlay.draw(this.shown, this.chunkColor.argb(), event.partialTick);
        if (this.tracers.get()) {
            this.chunkAreas.drawTracers(this.shown, 63.0d, class_9848.method_61334(this.chunkColor.argb()));
        }
    }
}
