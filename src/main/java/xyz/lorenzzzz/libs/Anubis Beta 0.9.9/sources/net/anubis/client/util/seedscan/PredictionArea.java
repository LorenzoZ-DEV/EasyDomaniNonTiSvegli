package net.anubis.client.util.seedscan;

import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1923;
import net.minecraft.class_2338;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/PredictionArea.class */
@Environment(EnvType.CLIENT)
public final class PredictionArea {
    public static final int MARGIN = 1;
    public static final int DOUBT_MISMATCH = 8;
    public static final int MISMATCH_COLUMNS = 26;
    private final SeedWorldgen world;
    private final Long2ObjectOpenHashMap<TerrainChunk> terrains = new Long2ObjectOpenHashMap<>();
    private final Long2ObjectOpenHashMap<ShownChunk> shown = new Long2ObjectOpenHashMap<>();
    private final Long2ObjectOpenHashMap<ChunkDecoration> decorations = new Long2ObjectOpenHashMap<>();
    private final Long2ObjectOpenHashMap<MayWrite> may = new Long2ObjectOpenHashMap<>();
    private final Long2ObjectOpenHashMap<GateHeights> gateHeights = new Long2ObjectOpenHashMap<>();
    private final DecorationOrder order = new DecorationOrder();
    private final Long2IntOpenHashMap doubt = new Long2IntOpenHashMap();
    private LongOpenHashSet bandChunks = new LongOpenHashSet();
    private LongOpenHashSet decorationChunks = new LongOpenHashSet();
    private final LongOpenHashSet dirty = new LongOpenHashSet();
    private final LongOpenHashSet built = new LongOpenHashSet();
    private final Long2ObjectOpenHashMap<VanillaBand> bands = new Long2ObjectOpenHashMap<>();
    private final LongOpenHashSet clustersDue = new LongOpenHashSet();
    private final LongOpenHashSet clustersSent = new LongOpenHashSet();
    private final ArrayDeque<ChunkClusters> out = new ArrayDeque<>();
    private final LongOpenHashSet collected = new LongOpenHashSet();
    private final Long2LongOpenHashMap signatures = new Long2LongOpenHashMap();
    private int focusX;
    private int focusZ;
    private int compared;
    private int mismatched;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/PredictionArea$ChunkClusters.class */
    @Environment(EnvType.CLIENT)
    public static final class ChunkClusters extends Record {
        private final long chunk;
        private final List<BlobJudge.Cluster> clusters;

        public ChunkClusters(long chunk, List<BlobJudge.Cluster> clusters) {
            this.chunk = chunk;
            this.clusters = clusters;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, ChunkClusters.class), ChunkClusters.class, "chunk;clusters", "FIELD:Lnet/anubis/client/util/seedscan/PredictionArea$ChunkClusters;->chunk:J", "FIELD:Lnet/anubis/client/util/seedscan/PredictionArea$ChunkClusters;->clusters:Ljava/util/List;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, ChunkClusters.class), ChunkClusters.class, "chunk;clusters", "FIELD:Lnet/anubis/client/util/seedscan/PredictionArea$ChunkClusters;->chunk:J", "FIELD:Lnet/anubis/client/util/seedscan/PredictionArea$ChunkClusters;->clusters:Ljava/util/List;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, ChunkClusters.class, Object.class), ChunkClusters.class, "chunk;clusters", "FIELD:Lnet/anubis/client/util/seedscan/PredictionArea$ChunkClusters;->chunk:J", "FIELD:Lnet/anubis/client/util/seedscan/PredictionArea$ChunkClusters;->clusters:Ljava/util/List;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public long chunk() {
            return this.chunk;
        }

        public List<BlobJudge.Cluster> clusters() {
            return this.clusters;
        }
    }

    public PredictionArea(SeedWorldgen world) {
        this.world = world;
        this.doubt.defaultReturnValue(-1);
    }

    public void want(LongCollection judged, int focusChunkX, int focusChunkZ) {
        this.focusX = focusChunkX;
        this.focusZ = focusChunkZ;
        LongOpenHashSet wanted = new LongOpenHashSet(judged);
        LongOpenHashSet decos = new LongOpenHashSet();
        LongIterator it = wanted.iterator();
        while (it.hasNext()) {
            long key = it.nextLong();
            int x = class_1923.method_8325(key);
            int z = class_1923.method_8332(key);
            for (int dx = -2; dx <= 2; dx++) {
                for (int dz = -2; dz <= 2; dz++) {
                    decos.add(class_1923.method_8331(x + dx, z + dz));
                }
            }
        }
        LongIterator it2 = decos.iterator();
        while (it2.hasNext()) {
            long key2 = it2.nextLong();
            if (!this.decorations.containsKey(key2)) {
                this.dirty.add(key2);
            }
        }
        this.bandChunks = wanted;
        this.decorationChunks = decos;
        this.built.retainAll(wanted);
        this.bands.keySet().retainAll(wanted);
        this.clustersDue.retainAll(wanted);
        this.clustersSent.retainAll(wanted);
        this.dirty.retainAll(decos);
        forgetOutside(decos);
    }

    private void forgetOutside(LongOpenHashSet decos) {
        LongOpenHashSet near = new LongOpenHashSet();
        LongIterator it = decos.iterator();
        while (it.hasNext()) {
            long key = it.nextLong();
            int x = class_1923.method_8325(key);
            int z = class_1923.method_8332(key);
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    near.add(class_1923.method_8331(x + dx, z + dz));
                }
            }
        }
        this.terrains.keySet().retainAll(near);
        this.shown.keySet().retainAll(near);
        this.gateHeights.keySet().retainAll(near);
        this.doubt.keySet().retainAll(near);
        this.decorations.keySet().retainAll(decos);
        this.may.keySet().retainAll(decos);
        this.collected.retainAll(decos);
        this.signatures.keySet().retainAll(decos);
    }

    public void offer(ShownChunk chunk) {
        long key = class_1923.method_8331(chunk.chunkX, chunk.chunkZ);
        this.shown.put(key, chunk);
        this.doubt.remove(key);
        this.gateHeights.remove(key);
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                this.collected.remove(class_1923.method_8331(chunk.chunkX + dx, chunk.chunkZ + dz));
            }
        }
        for (int dx2 = -1; dx2 <= 1; dx2++) {
            for (int dz2 = -1; dz2 <= 1; dz2++) {
                long near = class_1923.method_8331(chunk.chunkX + dx2, chunk.chunkZ + dz2);
                if (this.decorationChunks.contains(near)) {
                    markDirty(near);
                }
            }
        }
    }

    public boolean idle() {
        if (!this.dirty.isEmpty() || !this.out.isEmpty() || !this.clustersDue.isEmpty()) {
            return false;
        }
        LongIterator it = this.bandChunks.iterator();
        while (it.hasNext()) {
            if (!this.built.contains(it.nextLong())) {
                return false;
            }
        }
        return true;
    }

    public int compared() {
        return this.compared;
    }

    public int mismatched() {
        return this.mismatched;
    }

    public ChunkClusters step() {
        if (!this.out.isEmpty()) {
            return this.out.poll();
        }
        LongOpenHashSet firstTime = new LongOpenHashSet();
        LongIterator it = this.dirty.iterator();
        while (it.hasNext()) {
            long key = it.nextLong();
            if (!this.decorations.containsKey(key)) {
                firstTime.add(key);
            }
        }
        long next = nearest(firstTime.isEmpty() ? this.dirty : firstTime);
        if (next != Long.MIN_VALUE) {
            int x = class_1923.method_8325(next);
            int z = class_1923.method_8332(next);
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    long key2 = class_1923.method_8331(x + dx, z + dz);
                    if (!this.terrains.containsKey(key2)) {
                        this.terrains.put(key2, this.world.terrain(x + dx, z + dz));
                        return null;
                    }
                }
            }
            predict(next);
            return null;
        }
        LongOpenHashSet waiting = new LongOpenHashSet();
        LongIterator it2 = this.bandChunks.iterator();
        while (it2.hasNext()) {
            long key3 = it2.nextLong();
            if (!this.built.contains(key3)) {
                waiting.add(key3);
            }
        }
        while (!waiting.isEmpty()) {
            long key4 = nearest(waiting);
            waiting.remove(key4);
            if (ready(key4)) {
                int x2 = class_1923.method_8325(key4);
                int z2 = class_1923.method_8332(key4);
                ChunkDecoration[] around = new ChunkDecoration[9];
                for (int dx2 = -1; dx2 <= 1; dx2++) {
                    for (int dz2 = -1; dz2 <= 1; dz2++) {
                        around[((dx2 + 1) * 3) + dz2 + 1] = (ChunkDecoration) this.decorations.get(class_1923.method_8331(x2 + dx2, z2 + dz2));
                    }
                }
                VanillaBand band = VanillaBand.of((TerrainChunk) this.terrains.get(key4), around, this.order);
                this.built.add(key4);
                this.bands.put(key4, band);
                for (int dx3 = -2; dx3 <= 2; dx3++) {
                    for (int dz3 = -2; dz3 <= 2; dz3++) {
                        long near = class_1923.method_8331(x2 + dx3, z2 + dz3);
                        if (this.bandChunks.contains(near)) {
                            this.clustersDue.add(near);
                        }
                    }
                }
                return null;
            }
        }
        LongOpenHashSet due = new LongOpenHashSet(this.clustersDue);
        while (!due.isEmpty()) {
            long key5 = nearest(due);
            due.remove(key5);
            if (clustersReady(key5)) {
                this.clustersDue.remove(key5);
                this.clustersSent.add(key5);
                return new ChunkClusters(key5, List.copyOf(BlobJudge.clusters(class_1923.method_8325(key5), class_1923.method_8332(key5), (cx, cz) -> {
                    return (VanillaBand) this.bands.get(class_1923.method_8331(cx, cz));
                })));
            }
        }
        return null;
    }

    private boolean clustersReady(long key) {
        if (!this.bands.containsKey(key)) {
            return false;
        }
        int x = class_1923.method_8325(key);
        int z = class_1923.method_8332(key);
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                long near = class_1923.method_8331(x + dx, z + dz);
                if (this.bandChunks.contains(near) && !this.bands.containsKey(near)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void unbuild(long key) {
        if (this.built.remove(key)) {
            this.bands.remove(key);
            int x = class_1923.method_8325(key);
            int z = class_1923.method_8332(key);
            for (int dx = -2; dx <= 2; dx++) {
                for (int dz = -2; dz <= 2; dz++) {
                    long near = class_1923.method_8331(x + dx, z + dz);
                    if (this.clustersSent.remove(near)) {
                        this.out.add(new ChunkClusters(near, null));
                    }
                    if (this.bandChunks.contains(near)) {
                        this.clustersDue.add(near);
                    }
                }
            }
        }
    }

    private boolean ready(long key) {
        int x = class_1923.method_8325(key);
        int z = class_1923.method_8332(key);
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (!this.decorations.containsKey(class_1923.method_8331(x + dx, z + dz))) {
                    return false;
                }
            }
        }
        if (!this.terrains.containsKey(key)) {
            return false;
        }
        for (int dx2 = -3; dx2 <= 3; dx2++) {
            for (int dz2 = -3; dz2 <= 3; dz2++) {
                if (this.dirty.contains(class_1923.method_8331(x + dx2, z + dz2))) {
                    return false;
                }
            }
        }
        return true;
    }

    private void markDirty(long key) {
        if (this.decorationChunks.contains(key)) {
            this.dirty.add(key);
            int x = class_1923.method_8325(key);
            int z = class_1923.method_8332(key);
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    unbuild(class_1923.method_8331(x + dx, z + dz));
                }
            }
        }
    }

    private void predict(long key) {
        int x = class_1923.method_8325(key);
        int z = class_1923.method_8332(key);
        ChunkDecoration full = OrePredictor.predict(this.world, x, z, inputs(x, z));
        this.dirty.remove(key);
        ChunkDecoration decoration = full.trimmed(-16, 63, true);
        ChunkDecoration before = (ChunkDecoration) this.decorations.put(key, decoration);
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                unbuild(class_1923.method_8331(x + dx, z + dz));
            }
        }
        MayWrite own = (MayWrite) this.may.computeIfAbsent(key, k -> {
            return new MayWrite(x, z);
        });
        if (own.absorb(full)) {
            for (int dx2 = -2; dx2 <= 2; dx2++) {
                for (int dz2 = -2; dz2 <= 2; dz2++) {
                    if (dx2 != 0 || dz2 != 0) {
                        markDirty(class_1923.method_8331(x + dx2, z + dz2));
                    }
                }
            }
        }
        long signature = DecorationOrder.evidenceSignature(decoration);
        boolean evidenceChanged = (before != null && this.signatures.get(key) == signature && this.collected.contains(key)) ? false : true;
        this.signatures.put(key, signature);
        List<long[]> changed = new ArrayList<>();
        for (int dx3 = -2; dx3 <= 2; dx3++) {
            for (int dz3 = -2; dz3 <= 2; dz3++) {
                long near = class_1923.method_8331(x + dx3, z + dz3);
                ChunkDecoration d = (ChunkDecoration) this.decorations.get(near);
                if (d != null && neighbourhoodGuessed(near) && (evidenceChanged || !this.collected.contains(near))) {
                    changed.addAll(this.order.collect(d, this.decorations, this::terrainOrNull, this::shownKind));
                    this.collected.add(near);
                }
            }
        }
        for (long[] pair : changed) {
            for (long end : pair) {
                int ex = class_1923.method_8325(end);
                int ez = class_1923.method_8332(end);
                for (int dx4 = -2; dx4 <= 2; dx4++) {
                    for (int dz4 = -2; dz4 <= 2; dz4++) {
                        markDirty(class_1923.method_8331(ex + dx4, ez + dz4));
                    }
                }
            }
        }
    }

    private boolean neighbourhoodGuessed(long key) {
        int x = class_1923.method_8325(key);
        int z = class_1923.method_8332(key);
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                long near = class_1923.method_8331(x + dx, z + dz);
                if (this.decorationChunks.contains(near) && !this.decorations.containsKey(near)) {
                    return false;
                }
            }
        }
        return true;
    }

    private TerrainChunk terrainOrNull(int chunkX, int chunkZ) {
        return (TerrainChunk) this.terrains.get(class_1923.method_8331(chunkX, chunkZ));
    }

    private byte shownKind(int x, int y, int z) {
        ShownChunk chunk = (ShownChunk) this.shown.get(class_1923.method_8331(x >> 4, z >> 4));
        if (chunk == null) {
            return (byte) 15;
        }
        return chunk.kind(x, y, z);
    }

    private int heightDoubt(long key, TerrainChunk model, ShownChunk seen) {
        int known = this.doubt.get(key);
        if (known != -1) {
            return known;
        }
        int off = 0;
        int columns = 0;
        int chunkX = class_1923.method_8325(key);
        int chunkZ = class_1923.method_8332(key);
        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                int wx = (chunkX << 4) + x;
                int wz = (chunkZ << 4) + z;
                int top = seen.terrainTop(wx, wz);
                if (top != Integer.MIN_VALUE) {
                    columns++;
                    if (Math.abs(top - model.oceanFloor(wx, wz)) > 2) {
                        off++;
                    }
                }
            }
        }
        int result = (columns < 64 || off * 256 <= 26 * columns) ? 0 : 8;
        this.doubt.put(key, result);
        if (columns >= 64) {
            this.compared++;
            if (result == 8) {
                this.mismatched++;
            }
        }
        return result;
    }

    private GateHeights gateHeights(int chunkX, int chunkZ) {
        long key = class_1923.method_8331(chunkX, chunkZ);
        GateHeights heights = (GateHeights) this.gateHeights.get(key);
        if (heights != null) {
            return heights;
        }
        TerrainChunk model = (TerrainChunk) this.terrains.get(key);
        ShownChunk seen = (ShownChunk) this.shown.get(key);
        GateHeights heights2 = GateHeights.of(model, seen, seen == null ? 0 : heightDoubt(key, model, seen), this.world.treeAllowance(chunkX, chunkZ));
        this.gateHeights.put(key, heights2);
        return heights2;
    }

    private OrePredictor.Inputs inputs(int chunkX, int chunkZ) {
        final long self = class_1923.method_8331(chunkX, chunkZ);
        return new OrePredictor.Inputs() { // from class: net.anubis.client.util.seedscan.PredictionArea.1
            @Override // net.anubis.client.util.seedscan.OrePredictor.Inputs
            public TerrainChunk terrain(int x, int z) {
                return (TerrainChunk) PredictionArea.this.terrains.get(class_1923.method_8331(x, z));
            }

            @Override // net.anubis.client.util.seedscan.OrePredictor.Inputs
            public GateHeights gateHeights(int x, int z) {
                return PredictionArea.this.gateHeights(x, z);
            }

            @Override // net.anubis.client.util.seedscan.OrePredictor.Inputs
            public boolean othersMayWrite(int x, int y, int z) {
                int px = x >> 4;
                int pz = z >> 4;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        long writer = class_1923.method_8331(px + dx, pz + dz);
                        if (writer != self && !PredictionArea.this.order.after(writer, self)) {
                            if (!PredictionArea.this.decorationChunks.contains(writer)) {
                                return true;
                            }
                            MayWrite w = (MayWrite) PredictionArea.this.may.get(writer);
                            if (w != null && w.nonBase(x, y, z)) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }

            @Override // net.anubis.client.util.seedscan.OrePredictor.Inputs
            public byte shown(int x, int y, int z) {
                return PredictionArea.this.shownKind(x, y, z);
            }

            @Override // net.anubis.client.util.seedscan.OrePredictor.Inputs
            public boolean predictedOre(int x, int y, int z) {
                int px = x >> 4;
                int pz = z >> 4;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        MayWrite w = (MayWrite) PredictionArea.this.may.get(class_1923.method_8331(px + dx, pz + dz));
                        if (w != null && w.ore(x, y, z)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
    }

    private long nearest(LongOpenHashSet keys) {
        long best = Long.MIN_VALUE;
        int bestDistance = Integer.MAX_VALUE;
        LongIterator it = keys.iterator();
        while (it.hasNext()) {
            long key = it.nextLong();
            int distance = Math.max(Math.abs(class_1923.method_8325(key) - this.focusX), Math.abs(class_1923.method_8332(key) - this.focusZ));
            if (distance < bestDistance) {
                bestDistance = distance;
                best = key;
            }
        }
        return best;
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/PredictionArea$MayWrite.class */
    @Environment(EnvType.CLIENT)
    static final class MayWrite {
        static final int TOP = 200;
        private final int originX;
        private final int originZ;
        private int[] nonBase = new int[0];
        private int[] ores = new int[0];
        private final List<ChunkDecoration.Fog> fogs = new ArrayList();

        MayWrite(int chunkX, int chunkZ) {
            this.originX = (chunkX - 1) << 4;
            this.originZ = (chunkZ - 1) << 4;
        }

        private int index(int x, int y, int z) {
            int lx = x - this.originX;
            int lz = z - this.originZ;
            if (lx < 0 || lx >= 48 || lz < 0 || lz >= 48 || y < -64 || y > TOP) {
                return -1;
            }
            return ((((y - (-64)) * 48) + lz) * 48) + lx;
        }

        boolean absorb(ChunkDecoration d) {
            IntOpenHashSet addNonBase = new IntOpenHashSet();
            IntOpenHashSet addOres = new IntOpenHashSet();
            for (int i = 0; i < d.size(); i++) {
                long pos = d.key(i);
                int index = index(class_2338.method_10061(pos), class_2338.method_10071(pos), class_2338.method_10083(pos));
                if (index >= 0) {
                    for (int n = 0; n < d.writeCount(i); n++) {
                        byte kind = ChunkDecoration.kind(d.write(i, n));
                        if (!VBlock.isBase(kind)) {
                            if (Arrays.binarySearch(this.nonBase, index) < 0) {
                                addNonBase.add(index);
                            }
                            if (kind == 11 && Arrays.binarySearch(this.ores, index) < 0) {
                                addOres.add(index);
                            }
                        }
                    }
                }
            }
            boolean grew = (addNonBase.isEmpty() && addOres.isEmpty()) ? false : true;
            this.nonBase = merged(this.nonBase, addNonBase);
            this.ores = merged(this.ores, addOres);
            for (ChunkDecoration.Fog fog : d.fogs()) {
                if (!VBlock.isBase(fog.kind()) && !this.fogs.contains(fog)) {
                    this.fogs.add(fog);
                    grew = true;
                }
            }
            return grew;
        }

        private static int[] merged(int[] sorted, IntOpenHashSet more) {
            if (more.isEmpty()) {
                return sorted;
            }
            int[] out = Arrays.copyOf(sorted, sorted.length + more.size());
            int at = sorted.length;
            IntIterator it = more.iterator();
            while (it.hasNext()) {
                int i = at;
                at++;
                out[i] = it.nextInt();
            }
            Arrays.sort(out);
            return out;
        }

        boolean nonBase(int x, int y, int z) {
            int index = index(x, y, z);
            if (index >= 0 && Arrays.binarySearch(this.nonBase, index) >= 0) {
                return true;
            }
            for (ChunkDecoration.Fog fog : this.fogs) {
                if (fog.contains(x, y, z)) {
                    return true;
                }
            }
            return false;
        }

        boolean ore(int x, int y, int z) {
            int index = index(x, y, z);
            if (index >= 0 && Arrays.binarySearch(this.ores, index) >= 0) {
                return true;
            }
            for (ChunkDecoration.Fog fog : this.fogs) {
                if (fog.kind() == 11 && fog.contains(x, y, z)) {
                    return true;
                }
            }
            return false;
        }
    }
}
