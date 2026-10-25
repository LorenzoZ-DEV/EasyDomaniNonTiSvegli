package net.anubis.client.util.seedscan;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1923;
import net.minecraft.class_2338;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/DecorationOrder.class */
@Environment(EnvType.CLIENT)
public final class DecorationOrder {
    public static final int MIN_VOTES = 3;
    private final Map<Pair, int[]> votes = new HashMap();
    private final Long2ObjectOpenHashMap<Map<Pair, int[]>> bySource = new Long2ObjectOpenHashMap<>();

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/DecorationOrder$Shown.class */
    @Environment(EnvType.CLIENT)
    public interface Shown {
        byte kind(int i, int i2, int i3);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/DecorationOrder$TerrainLookup.class */
    @Environment(EnvType.CLIENT)
    public interface TerrainLookup {
        TerrainChunk terrain(int i, int i2);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/DecorationOrder$Pair.class */
    @Environment(EnvType.CLIENT)
    private static final class Pair extends Record {
        private final long first;
        private final long second;

        private Pair(long first, long second) {
            this.first = first;
            this.second = second;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Pair.class), Pair.class, "first;second", "FIELD:Lnet/anubis/client/util/seedscan/DecorationOrder$Pair;->first:J", "FIELD:Lnet/anubis/client/util/seedscan/DecorationOrder$Pair;->second:J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Pair.class), Pair.class, "first;second", "FIELD:Lnet/anubis/client/util/seedscan/DecorationOrder$Pair;->first:J", "FIELD:Lnet/anubis/client/util/seedscan/DecorationOrder$Pair;->second:J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Pair.class, Object.class), Pair.class, "first;second", "FIELD:Lnet/anubis/client/util/seedscan/DecorationOrder$Pair;->first:J", "FIELD:Lnet/anubis/client/util/seedscan/DecorationOrder$Pair;->second:J").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public long first() {
            return this.first;
        }

        public long second() {
            return this.second;
        }

        static Pair of(long a, long b) {
            return a < b ? new Pair(a, b) : new Pair(b, a);
        }
    }

    public int compare(long a, long b) {
        int[] v;
        if (a == b || (v = this.votes.get(Pair.of(a, b))) == null) {
            return 0;
        }
        boolean aIsFirst = a < b;
        if (v[0] >= 3 && v[1] == 0) {
            return aIsFirst ? 1 : -1;
        }
        if (v[1] < 3 || v[0] != 0) {
            return 0;
        }
        return aIsFirst ? -1 : 1;
    }

    public boolean after(long a, long b) {
        return compare(a, b) > 0;
    }

    public Set<long[]> collect(ChunkDecoration own, Long2ObjectMap<ChunkDecoration> decorations, TerrainLookup terrain, Shown shown) {
        TerrainChunk chunk;
        int index;
        long ownKey = class_1923.method_8331(own.chunkX, own.chunkZ);
        Map<Pair, int[]> fresh = new HashMap<>();
        for (int i = 0; i < own.size(); i++) {
            long pos = own.key(i);
            byte ownKind = lastStone(own, i);
            if (ownKind >= 0) {
                int x = class_2338.method_10061(pos);
                int y = class_2338.method_10071(pos);
                int z = class_2338.method_10083(pos);
                byte otherKind = -1;
                long other = 0;
                boolean usable = true;
                boolean found = false;
                for (int dx = -1; dx <= 1 && usable; dx++) {
                    for (int dz = -1; dz <= 1 && usable; dz++) {
                        long key = class_1923.method_8331((x >> 4) + dx, (z >> 4) + dz);
                        ChunkDecoration d = (ChunkDecoration) decorations.get(key);
                        if (d != null) {
                            for (ChunkDecoration.Fog fog : d.fogs()) {
                                if (fog.contains(x, y, z)) {
                                    usable = false;
                                }
                            }
                            if (key != ownKey && (index = d.find(pos)) >= 0) {
                                if (found) {
                                    usable = false;
                                } else {
                                    found = true;
                                    otherKind = lastStone(d, index);
                                    other = key;
                                    if (otherKind < 0) {
                                        usable = false;
                                    }
                                }
                            }
                        }
                    }
                }
                if (usable && found && otherKind != ownKind && other >= ownKey && (chunk = terrain.terrain(x >> 4, z >> 4)) != null && VBlock.isBase(chunk.kind(x, y, z))) {
                    byte kind = shown.kind(x, y, z);
                    int[] v = fresh.computeIfAbsent(Pair.of(ownKey, other), k -> {
                        return new int[2];
                    });
                    if (kind == ownKind) {
                        v[0] = v[0] + 1;
                    } else if (kind == otherKind) {
                        v[1] = v[1] + 1;
                    }
                }
            }
        }
        Set<Pair> touched = new HashSet<>(fresh.keySet());
        Map<Pair, int[]> old = (Map) this.bySource.put(ownKey, fresh);
        if (old != null) {
            touched.addAll(old.keySet());
        }
        Set<long[]> changed = new HashSet<>();
        for (Pair pair : touched) {
            int before = compare(pair.first(), pair.second());
            int[] total = this.votes.computeIfAbsent(pair, k2 -> {
                return new int[2];
            });
            int[] was = old == null ? null : old.get(pair);
            int[] now = fresh.get(pair);
            if (was != null) {
                total[0] = total[0] - was[0];
                total[1] = total[1] - was[1];
            }
            if (now != null) {
                total[0] = total[0] + now[0];
                total[1] = total[1] + now[1];
            }
            if (total[0] == 0 && total[1] == 0) {
                this.votes.remove(pair);
            }
            if (compare(pair.first(), pair.second()) != before) {
                changed.add(new long[]{pair.first(), pair.second()});
            }
        }
        return changed;
    }

    static long evidenceSignature(ChunkDecoration d) {
        long hash = 1125899906842597L;
        for (int i = 0; i < d.size(); i++) {
            byte kind = lastStone(d, i);
            if (kind >= 0) {
                hash = (31 * ((31 * hash) + d.key(i))) + ((long) kind);
            }
        }
        return (31 * hash) + ((long) d.fogs().hashCode());
    }

    private static byte lastStone(ChunkDecoration d, int index) {
        byte last = -1;
        for (int n = 0; n < d.writeCount(index); n++) {
            short write = d.write(index, n);
            byte kind = ChunkDecoration.kind(write);
            if (ChunkDecoration.possible(write) || !VBlock.isBase(kind)) {
                return (byte) -1;
            }
            last = kind;
        }
        return last;
    }
}
