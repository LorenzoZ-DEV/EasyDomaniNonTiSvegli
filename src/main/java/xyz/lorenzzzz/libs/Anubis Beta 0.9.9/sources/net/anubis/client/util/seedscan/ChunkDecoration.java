package net.anubis.client.util.seedscan;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2338;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/ChunkDecoration.class */
@Environment(EnvType.CLIENT)
public final class ChunkDecoration {
    public final int chunkX;
    public final int chunkZ;
    private final long[] keys;
    private final int[] first;
    private final short[] writes;
    private final Fog[] fogs;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/ChunkDecoration$Fog.class */
    @Environment(EnvType.CLIENT)
    public static final class Fog extends Record {
        private final int order;
        private final byte kind;
        private final int minX;
        private final int minY;
        private final int minZ;
        private final int maxX;
        private final int maxY;
        private final int maxZ;

        public Fog(int order, byte kind, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
            this.order = order;
            this.kind = kind;
            this.minX = minX;
            this.minY = minY;
            this.minZ = minZ;
            this.maxX = maxX;
            this.maxY = maxY;
            this.maxZ = maxZ;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Fog.class), Fog.class, "order;kind;minX;minY;minZ;maxX;maxY;maxZ", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->order:I", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->kind:B", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->minX:I", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->minY:I", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->minZ:I", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->maxX:I", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->maxY:I", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->maxZ:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Fog.class), Fog.class, "order;kind;minX;minY;minZ;maxX;maxY;maxZ", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->order:I", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->kind:B", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->minX:I", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->minY:I", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->minZ:I", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->maxX:I", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->maxY:I", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->maxZ:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Fog.class, Object.class), Fog.class, "order;kind;minX;minY;minZ;maxX;maxY;maxZ", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->order:I", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->kind:B", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->minX:I", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->minY:I", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->minZ:I", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->maxX:I", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->maxY:I", "FIELD:Lnet/anubis/client/util/seedscan/ChunkDecoration$Fog;->maxZ:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int order() {
            return this.order;
        }

        public byte kind() {
            return this.kind;
        }

        public int minX() {
            return this.minX;
        }

        public int minY() {
            return this.minY;
        }

        public int minZ() {
            return this.minZ;
        }

        public int maxX() {
            return this.maxX;
        }

        public int maxY() {
            return this.maxY;
        }

        public int maxZ() {
            return this.maxZ;
        }

        boolean contains(int x, int y, int z) {
            return x >= this.minX && x <= this.maxX && y >= this.minY && y <= this.maxY && z >= this.minZ && z <= this.maxZ;
        }
    }

    private ChunkDecoration(int chunkX, int chunkZ, long[] keys, int[] first, short[] writes, Fog[] fogs) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.keys = keys;
        this.first = first;
        this.writes = writes;
        this.fogs = fogs;
    }

    static short pack(int order, byte kind, boolean possible) {
        return (short) ((order << 5) | (kind << 1) | (possible ? 1 : 0));
    }

    public static byte kind(short write) {
        return (byte) ((write >> 1) & 15);
    }

    public static boolean possible(short write) {
        return (write & 1) != 0;
    }

    public int find(long pos) {
        return Arrays.binarySearch(this.keys, pos);
    }

    public int writeCount(int index) {
        return this.first[index + 1] - this.first[index];
    }

    public short write(int index, int n) {
        return this.writes[this.first[index] + n];
    }

    public List<Fog> fogs() {
        return Arrays.asList(this.fogs);
    }

    public int size() {
        return this.keys.length;
    }

    public long key(int index) {
        return this.keys[index];
    }

    public ChunkDecoration trimmed(int minY, int maxY, boolean all) {
        Builder builder = new Builder(this.chunkX, this.chunkZ);
        for (int i = 0; i < this.keys.length; i++) {
            int y = class_2338.method_10071(this.keys[i]);
            boolean inBand = y >= minY && y <= maxY;
            for (int n = this.first[i]; n < this.first[i + 1]; n++) {
                if (inBand || (!all && !VBlock.isBase(kind(this.writes[n])))) {
                    builder.add(this.keys[i], this.writes[n]);
                }
            }
        }
        for (Fog fog : this.fogs) {
            builder.fog(fog);
        }
        return builder.build();
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/ChunkDecoration$Builder.class */
    @Environment(EnvType.CLIENT)
    static final class Builder {
        final int chunkX;
        final int chunkZ;
        private final Long2ObjectOpenHashMap<ShortArrayList> byPos = new Long2ObjectOpenHashMap<>();
        private final ArrayList<Fog> fogs = new ArrayList<>();

        Builder(int chunkX, int chunkZ) {
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
        }

        void add(long pos, short write) {
            ((ShortArrayList) this.byPos.computeIfAbsent(pos, k -> {
                return new ShortArrayList(2);
            })).add(write);
        }

        ShortArrayList at(long pos) {
            return (ShortArrayList) this.byPos.get(pos);
        }

        void fog(Fog fog) {
            this.fogs.add(fog);
        }

        List<Fog> fogList() {
            return this.fogs;
        }

        ChunkDecoration build() {
            long[] keys = this.byPos.keySet().toLongArray();
            Arrays.sort(keys);
            int[] first = new int[keys.length + 1];
            int total = 0;
            ObjectIterator it = this.byPos.long2ObjectEntrySet().iterator();
            while (it.hasNext()) {
                Long2ObjectMap.Entry<ShortArrayList> entry = (Long2ObjectMap.Entry) it.next();
                total += ((ShortArrayList) entry.getValue()).size();
            }
            short[] writes = new short[total];
            int at = 0;
            for (int i = 0; i < keys.length; i++) {
                first[i] = at;
                ShortArrayList list = (ShortArrayList) this.byPos.get(keys[i]);
                for (int n = 0; n < list.size(); n++) {
                    int i2 = at;
                    at++;
                    writes[i2] = list.getShort(n);
                }
            }
            first[keys.length] = at;
            return new ChunkDecoration(this.chunkX, this.chunkZ, keys, first, writes, (Fog[]) this.fogs.toArray(new Fog[0]));
        }
    }
}
