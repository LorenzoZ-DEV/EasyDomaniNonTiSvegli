package net.anubis.client.util.seedscan;

import imgui.flag.ImGuiWindowFlags;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalLong;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.LockSupport;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1923;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_2818;
import net.minecraft.class_2826;
import net.minecraft.class_2841;
import net.minecraft.class_310;
import net.minecraft.class_4543;
import net.minecraft.class_638;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/AdvancedScanner.class */
@Environment(EnvType.CLIENT)
public final class AdvancedScanner implements AutoCloseable {
    public static final int MAX_RADIUS = 8;
    public static final int WORLD_LIMIT = 250000;
    private static final int SETTLE_TICKS = 20;
    private static final int SEED_CHECK_AFTER = 40;
    private static final double SEED_CHECK_FAIL = 0.5d;
    private static final int HIDDEN_AFTER = 10;
    private static final int GROUND_CHECK_AFTER = 20;
    private final long seed;
    private volatile boolean closed;
    private volatile boolean stopped;
    private boolean areaStale;
    private long ticks;
    private int trusted;
    private int mispredicted;
    private int hidden;
    private boolean hashChecked;
    private boolean seedWarned;
    private boolean hiddenWarned;
    private boolean distrusted;
    private boolean wrongHeight;
    private boolean released;
    private final ConcurrentLinkedQueue<Object> inbox = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Object> outbox = new ConcurrentLinkedQueue<>();
    private final Long2ObjectOpenHashMap<List<BlobJudge.Cluster>> clusters = new Long2ObjectOpenHashMap<>();
    private final Long2ObjectOpenHashMap<ChunkResult> results = new Long2ObjectOpenHashMap<>();
    private final LongOpenHashSet shownSent = new LongOpenHashSet();
    private final LongArrayFIFOQueue toJudge = new LongArrayFIFOQueue();
    private final LongOpenHashSet judgeQueued = new LongOpenHashSet();
    private final Long2LongOpenHashMap settling = new Long2LongOpenHashMap();
    private final LongOpenHashSet counted = new LongOpenHashSet();
    private final List<String> notices = new ArrayList();
    private LongOpenHashSet area = new LongOpenHashSet();
    private int areaX = ImGuiWindowFlags.NavFlattened;
    private int areaZ = ImGuiWindowFlags.NavFlattened;
    private int areaRadius = -1;
    private final Thread worker = new Thread(this::work, "Anubis Advanced Scanner");

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/AdvancedScanner$ChunkResult.class */
    @Environment(EnvType.CLIENT)
    public static final class ChunkResult extends Record {
        private final long chunk;
        private final int blocks;
        private final long[] anchors;
        private final List<BlobJudge.Verdict> verdicts;

        public ChunkResult(long chunk, int blocks, long[] anchors, List<BlobJudge.Verdict> verdicts) {
            this.chunk = chunk;
            this.blocks = blocks;
            this.anchors = anchors;
            this.verdicts = verdicts;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, ChunkResult.class), ChunkResult.class, "chunk;blocks;anchors;verdicts", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$ChunkResult;->chunk:J", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$ChunkResult;->blocks:I", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$ChunkResult;->anchors:[J", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$ChunkResult;->verdicts:Ljava/util/List;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, ChunkResult.class), ChunkResult.class, "chunk;blocks;anchors;verdicts", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$ChunkResult;->chunk:J", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$ChunkResult;->blocks:I", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$ChunkResult;->anchors:[J", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$ChunkResult;->verdicts:Ljava/util/List;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, ChunkResult.class, Object.class), ChunkResult.class, "chunk;blocks;anchors;verdicts", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$ChunkResult;->chunk:J", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$ChunkResult;->blocks:I", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$ChunkResult;->anchors:[J", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$ChunkResult;->verdicts:Ljava/util/List;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public long chunk() {
            return this.chunk;
        }

        public int blocks() {
            return this.blocks;
        }

        public long[] anchors() {
            return this.anchors;
        }

        public List<BlobJudge.Verdict> verdicts() {
            return this.verdicts;
        }

        public boolean flagged() {
            return this.anchors.length > 0;
        }
    }

    private AdvancedScanner(long seed) {
        this.seed = seed;
        this.worker.setDaemon(true);
        this.worker.setPriority(2);
        this.worker.start();
    }

    public static AdvancedScanner start(long seed) {
        return new AdvancedScanner(seed);
    }

    public long seed() {
        return this.seed;
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.closed = true;
        LockSupport.unpark(this.worker);
    }

    public static boolean matchesHashedSeed(long seed, long hashedSeed) {
        return class_4543.method_27984(seed) == hashedSeed;
    }

    static OptionalLong hashedSeed(class_638 level) {
        try {
            Field found = null;
            for (Field field : class_4543.class.getDeclaredFields()) {
                if (field.getType() == Long.TYPE && !Modifier.isStatic(field.getModifiers())) {
                    if (found != null) {
                        return OptionalLong.empty();
                    }
                    found = field;
                }
            }
            if (found == null) {
                return OptionalLong.empty();
            }
            found.setAccessible(true);
            return OptionalLong.of(found.getLong(level.method_22385()));
        } catch (ReflectiveOperationException | RuntimeException e) {
            return OptionalLong.empty();
        }
    }

    public List<String> drainNotices() {
        List<String> out = new ArrayList<>(this.notices);
        this.notices.clear();
        return out;
    }

    public List<ChunkResult> tick(class_638 level, long deadline) {
        ChunkResult result;
        this.ticks++;
        while (true) {
            Object message = this.outbox.poll();
            if (message == null) {
                break;
            }
            if (message instanceof PredictionArea.ChunkClusters) {
                PredictionArea.ChunkClusters found = (PredictionArea.ChunkClusters) message;
                if (found.clusters() == null) {
                    this.clusters.remove(found.chunk());
                } else {
                    this.clusters.put(found.chunk(), found.clusters());
                    queueJudge(found.chunk());
                }
            } else if (message instanceof String) {
                String notice = (String) message;
                this.notices.add(notice);
            }
        }
        if (this.stopped) {
            release();
            return List.of();
        }
        if (level.method_31607() != -64 || level.method_31605() != 384) {
            if (!this.wrongHeight) {
                this.wrongHeight = true;
                this.notices.add("Advanced Scanner only works with vanilla world height.");
            }
            return List.of();
        }
        if (!this.hashChecked) {
            this.hashChecked = true;
            OptionalLong hashed = hashedSeed(level);
            if (hashed.isPresent() && !matchesHashedSeed(this.seed, hashed.getAsLong())) {
                this.notices.add("Seed " + this.seed + " does not match this server. Check the seed.");
            }
        }
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 != null) {
            updateArea(level, mc.field_1724.method_31476(), Math.min(((Integer) mc.field_1690.method_42503().method_41753()).intValue(), 8));
        }
        copyArrived(level, deadline);
        List<ChunkResult> judged = new ArrayList<>();
        BlobJudge.Client client = clientView(level);
        int rounds = this.toJudge.size();
        while (true) {
            int i = rounds;
            rounds--;
            if (i <= 0 || this.toJudge.isEmpty() || System.nanoTime() >= deadline) {
                break;
            }
            long key = this.toJudge.dequeueLong();
            this.judgeQueued.remove(key);
            if (this.settling.containsKey(key)) {
                if (this.settling.get(key) > this.ticks) {
                    queueJudge(key);
                } else {
                    this.settling.remove(key);
                }
            }
            List<BlobJudge.Cluster> found2 = (List) this.clusters.get(key);
            if (found2 != null && this.area.contains(key) && (result = judge(key, found2, client)) != null) {
                this.results.put(key, result);
                judged.add(result);
            }
        }
        return judged;
    }

    private ChunkResult judge(long key, List<BlobJudge.Cluster> found, BlobJudge.Client client) {
        List<BlobJudge.Verdict> verdicts = new ArrayList<>(found.size());
        for (BlobJudge.Cluster cluster : found) {
            BlobJudge.Verdict verdict = BlobJudge.judge(cluster, client, 1);
            verdicts.add(verdict);
            count(verdict);
        }
        return this.distrusted ? new ChunkResult(key, 0, new long[0], List.of()) : decide(key, (ChunkResult) this.results.get(key), verdicts);
    }

    static ChunkResult decide(long chunk, ChunkResult before, List<BlobJudge.Verdict> now) {
        Map<Long, BlobJudge.Verdict> last = new HashMap<>();
        if (before != null) {
            for (BlobJudge.Verdict verdict : before.verdicts()) {
                if (verdict.status() != BlobJudge.Status.INCOMPLETE) {
                    last.put(Long.valueOf(clusterKey(verdict.owner(), verdict.kind())), verdict);
                }
            }
        }
        boolean undecided = false;
        List<BlobJudge.Verdict> verdicts = new ArrayList<>(now.size());
        LongArrayList blocks = new LongArrayList();
        LongArrayList anchors = new LongArrayList();
        for (BlobJudge.Verdict verdict2 : now) {
            if (verdict2.status() == BlobJudge.Status.INCOMPLETE) {
                BlobJudge.Verdict earlier = last.get(Long.valueOf(clusterKey(verdict2.owner(), verdict2.kind())));
                if (earlier != null) {
                    verdict2 = earlier;
                } else {
                    undecided = true;
                }
            }
            verdicts.add(verdict2);
            if (verdict2.alert()) {
                anchors.add(verdict2.changes()[0]);
                blocks.addElements(blocks.size(), verdict2.changes());
            }
        }
        if (undecided && anchors.isEmpty()) {
            return null;
        }
        long[] sorted = blocks.toLongArray();
        Arrays.sort(sorted);
        int n = 0;
        for (long pos : sorted) {
            if (n == 0 || sorted[n - 1] != pos) {
                int i = n;
                n++;
                sorted[i] = pos;
            }
        }
        return new ChunkResult(chunk, n, anchors.toLongArray(), List.copyOf(verdicts));
    }

    static void dropOutside(Long2ObjectOpenHashMap<ChunkResult> results, LongOpenHashSet area) {
        ObjectIterator<Long2ObjectMap.Entry<ChunkResult>> it = results.long2ObjectEntrySet().fastIterator();
        while (it.hasNext()) {
            Long2ObjectMap.Entry<ChunkResult> entry = (Long2ObjectMap.Entry) it.next();
            if (!area.contains(entry.getLongKey()) && !((ChunkResult) entry.getValue()).flagged()) {
                it.remove();
            }
        }
    }

    private static long clusterKey(long owner, byte kind) {
        return (owner * 31) + ((long) kind);
    }

    public void blockChanged(class_2338 pos) {
        if (!this.stopped && pos.method_10264() >= -17 && pos.method_10264() <= 64) {
            int cx = pos.method_10263() >> 4;
            int cz = pos.method_10260() >> 4;
            for (int dx = -2; dx <= 2; dx++) {
                for (int dz = -2; dz <= 2; dz++) {
                    long key = class_1923.method_8331(cx + dx, cz + dz);
                    if (this.clusters.containsKey(key)) {
                        this.settling.put(key, this.ticks + 20);
                        queueJudge(key);
                    }
                }
            }
        }
    }

    public void chunkArrived(int chunkX, int chunkZ) {
        if (this.stopped) {
            return;
        }
        this.areaStale = true;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                queueJudge(class_1923.method_8331(chunkX + dx, chunkZ + dz));
            }
        }
    }

    static boolean insideWorldLimit(int chunkX, int chunkZ) {
        return chunkX >= (-15625) && chunkX < 15625 && chunkZ >= (-15625) && chunkZ < 15625;
    }

    private void queueJudge(long key) {
        if (this.judgeQueued.add(key)) {
            this.toJudge.enqueue(key);
        }
    }

    private void updateArea(class_638 level, class_1923 center, int radius) {
        boolean moved = (center.field_9181 == this.areaX && center.field_9180 == this.areaZ && radius == this.areaRadius) ? false : true;
        if (moved || (this.areaStale && this.ticks % 20 == 0)) {
            this.areaStale = false;
            this.areaX = center.field_9181;
            this.areaZ = center.field_9180;
            this.areaRadius = radius;
            LongOpenHashSet chunks = new LongOpenHashSet();
            for (int x = center.field_9181 - radius; x <= center.field_9181 + radius; x++) {
                for (int z = center.field_9180 - radius; z <= center.field_9180 + radius; z++) {
                    if (insideWorldLimit(x, z) && level.method_2935().method_21730(x, z) != null) {
                        chunks.add(class_1923.method_8331(x, z));
                    }
                }
            }
            this.area = chunks;
            this.clusters.keySet().retainAll(chunks);
            dropOutside(this.results, chunks);
            int reach = radius + 1 + 1 + 1;
            this.shownSent.removeIf(key -> {
                return Math.max(Math.abs(class_1923.method_8325(key) - center.field_9181), Math.abs(class_1923.method_8332(key) - center.field_9180)) > reach;
            });
            send(new Want(chunks, center.field_9181, center.field_9180));
        }
    }

    private void copyArrived(class_638 level, long deadline) {
        class_2818 chunk;
        if (this.areaRadius < 0) {
            return;
        }
        int reach = this.areaRadius + 1 + 1 + 1;
        for (int ring = 0; ring <= reach && System.nanoTime() < deadline; ring++) {
            for (int x = this.areaX - ring; x <= this.areaX + ring; x++) {
                for (int z = this.areaZ - ring; z <= this.areaZ + ring; z++) {
                    if (Math.max(Math.abs(x - this.areaX), Math.abs(z - this.areaZ)) == ring) {
                        long key = class_1923.method_8331(x, z);
                        if (!this.shownSent.contains(key) && (chunk = level.method_2935().method_21730(x, z)) != null) {
                            class_2826[] sections = chunk.method_12006();
                            class_2841<class_2680>[] copies = new class_2841[sections.length];
                            for (int i = 0; i < sections.length; i++) {
                                if (sections[i] != null && !sections[i].method_38292()) {
                                    copies[i] = sections[i].method_12265().method_39957();
                                }
                            }
                            this.shownSent.add(key);
                            send(new Copied(x, z, copies));
                            if (System.nanoTime() >= deadline) {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private static BlobJudge.Client clientView(final class_638 level) {
        return new BlobJudge.Client() { // from class: net.anubis.client.util.seedscan.AdvancedScanner.1
            private int lastX = ImGuiWindowFlags.NavFlattened;
            private int lastZ = ImGuiWindowFlags.NavFlattened;
            private class_2818 last;

            @Override // net.anubis.client.util.seedscan.BlobJudge.Client
            public class_2680 state(int x, int y, int z) {
                int cx = x >> 4;
                int cz = z >> 4;
                if (cx != this.lastX || cz != this.lastZ) {
                    this.lastX = cx;
                    this.lastZ = cz;
                    this.last = level.method_2935().method_21730(cx, cz);
                }
                if (this.last == null) {
                    return null;
                }
                int index = this.last.method_31602(y);
                return (index < 0 || index >= this.last.method_32890()) ? class_2246.field_10243.method_9564() : this.last.method_38259(index).method_12254(x & 15, y & 15, z & 15);
            }
        };
    }

    private void count(BlobJudge.Verdict verdict) {
        switch (verdict.status()) {
            case CLEAN:
            case ALERT:
            case MISPREDICTED:
            case HIDDEN:
                if (this.counted.add(clusterKey(verdict.owner(), verdict.kind()))) {
                    switch (verdict.status()) {
                        case CLEAN:
                        case ALERT:
                            this.trusted++;
                            break;
                        case MISPREDICTED:
                            this.mispredicted++;
                            break;
                        default:
                            this.hidden++;
                            break;
                    }
                    int seen = this.trusted + this.mispredicted;
                    if (!this.seedWarned && seen >= 40 && this.mispredicted > SEED_CHECK_FAIL * ((double) seen)) {
                        this.seedWarned = true;
                        this.distrusted = true;
                        this.notices.add("Ores do not match seed " + this.seed + ". Advanced alerts are off until you change the seed.");
                    }
                    if (!this.hiddenWarned && this.hidden >= 10 && this.hidden > seen) {
                        this.hiddenWarned = true;
                        this.notices.add("This server hides ores with anti-xray. Advanced Scanner can't check them.");
                        break;
                    }
                }
                break;
        }
    }

    private void send(Object message) {
        if (this.stopped || this.closed) {
            return;
        }
        this.inbox.add(message);
        LockSupport.unpark(this.worker);
    }

    private void release() {
        this.inbox.clear();
        if (this.released) {
            return;
        }
        this.released = true;
        this.clusters.clear();
        this.toJudge.clear();
        this.judgeQueued.clear();
        this.settling.clear();
        this.shownSent.clear();
        this.area = new LongOpenHashSet();
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/AdvancedScanner$Want.class */
    @Environment(EnvType.CLIENT)
    private static final class Want extends Record {
        private final LongOpenHashSet chunks;
        private final int x;
        private final int z;

        private Want(LongOpenHashSet chunks, int x, int z) {
            this.chunks = chunks;
            this.x = x;
            this.z = z;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Want.class), Want.class, "chunks;x;z", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$Want;->chunks:Lit/unimi/dsi/fastutil/longs/LongOpenHashSet;", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$Want;->x:I", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$Want;->z:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Want.class), Want.class, "chunks;x;z", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$Want;->chunks:Lit/unimi/dsi/fastutil/longs/LongOpenHashSet;", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$Want;->x:I", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$Want;->z:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Want.class, Object.class), Want.class, "chunks;x;z", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$Want;->chunks:Lit/unimi/dsi/fastutil/longs/LongOpenHashSet;", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$Want;->x:I", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$Want;->z:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public LongOpenHashSet chunks() {
            return this.chunks;
        }

        public int x() {
            return this.x;
        }

        public int z() {
            return this.z;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/AdvancedScanner$Copied.class */
    @Environment(EnvType.CLIENT)
    private static final class Copied extends Record {
        private final int x;
        private final int z;
        private final class_2841<class_2680>[] sections;

        private Copied(int x, int z, class_2841<class_2680>[] sections) {
            this.x = x;
            this.z = z;
            this.sections = sections;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Copied.class), Copied.class, "x;z;sections", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$Copied;->x:I", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$Copied;->z:I", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$Copied;->sections:[Lnet/minecraft/class_2841;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Copied.class), Copied.class, "x;z;sections", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$Copied;->x:I", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$Copied;->z:I", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$Copied;->sections:[Lnet/minecraft/class_2841;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Copied.class, Object.class), Copied.class, "x;z;sections", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$Copied;->x:I", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$Copied;->z:I", "FIELD:Lnet/anubis/client/util/seedscan/AdvancedScanner$Copied;->sections:[Lnet/minecraft/class_2841;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int x() {
            return this.x;
        }

        public int z() {
            return this.z;
        }

        public class_2841<class_2680>[] sections() {
            return this.sections;
        }
    }

    private void work() {
        SeedWorldgen world = null;
        Throwable failure = null;
        try {
            world = SeedWorldgen.create(this.seed);
            predict(world);
        } catch (Throwable e) {
            failure = e;
        } finally {
            this.stopped = true;
            this.inbox.clear();
        }
        if (failure == null) {
            return;
        }
        try {
            this.outbox.add((world == null ? "Advanced Scanner can't work out this world: " : "Advanced Scanner stopped: ") + String.valueOf(failure));
        } catch (Throwable th) {
        }
    }

    private void predict(SeedWorldgen world) {
        PredictionArea prediction = new PredictionArea(world);
        boolean groundChecked = false;
        while (!this.closed) {
            while (true) {
                Object message = this.inbox.poll();
                if (message == null) {
                    break;
                }
                if (message instanceof Want) {
                    Want want = (Want) message;
                    prediction.want(want.chunks(), want.x(), want.z());
                } else if (message instanceof Copied) {
                    Copied copied = (Copied) message;
                    prediction.offer(ShownChunk.of(copied.x(), copied.z(), copied.sections()));
                }
            }
            if (prediction.idle()) {
                LockSupport.parkNanos(50000000L);
            } else {
                PredictionArea.ChunkClusters found = prediction.step();
                if (found != null) {
                    this.outbox.add(found);
                }
                if (!groundChecked && prediction.compared() >= 20) {
                    groundChecked = true;
                    if (prediction.mismatched() * 2 > prediction.compared()) {
                        this.outbox.add("The terrain here does not match seed " + this.seed + ". Check the seed.");
                    }
                }
            }
        }
    }
}
