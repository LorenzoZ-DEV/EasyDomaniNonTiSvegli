package net.anubis.client.util.seedscan;

import it.unimi.dsi.fastutil.longs.Long2ByteMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2338;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/OrePredictor.class */
@Environment(EnvType.CLIENT)
public final class OrePredictor {
    private static final ThreadLocal<Answers> ANSWERS = ThreadLocal.withInitial(Answers::new);

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/OrePredictor$Inputs.class */
    @Environment(EnvType.CLIENT)
    public interface Inputs {
        TerrainChunk terrain(int i, int i2);

        GateHeights gateHeights(int i, int i2);

        boolean othersMayWrite(int i, int i2, int i3);

        byte shown(int i, int i2, int i3);

        boolean predictedOre(int i, int i2, int i3);
    }

    private OrePredictor() {
    }

    static boolean baseish(byte kind) {
        return VBlock.isBase(kind) || kind == 11;
    }

    static boolean disagreesOnBase(Inputs inputs, byte model, int x, int y, int z) {
        byte shown = inputs.shown(x, y, z);
        if (shown == 15) {
            return true;
        }
        boolean modelBase = baseish(model);
        if (shown == 11) {
            return (modelBase && (model == 11 || inputs.predictedOre(x, y, z))) ? false : true;
        }
        return modelBase != VBlock.isBase(shown);
    }

    static boolean disagreesOnAir(Inputs inputs, byte model, int x, int y, int z) {
        byte shown = inputs.shown(x, y, z);
        if (shown == 15) {
            return true;
        }
        return (model == 0) != (shown == 0) || disagreesOnBase(inputs, model, x, y, z);
    }

    public static ChunkDecoration predict(SeedWorldgen world, int chunkX, int chunkZ, Inputs inputs) {
        View view = new View(world, chunkX, chunkZ, inputs);
        for (OreSpec spec : world.specs()) {
            view.spec = spec;
            OreSim.Chain chain = OreSim.run(spec, chunkX, chunkZ, view);
            ObjectIterator<Long2ByteMap.Entry> it = chain.writes.long2ByteEntrySet().fastIterator();
            while (it.hasNext()) {
                Long2ByteMap.Entry entry = (Long2ByteMap.Entry) it.next();
                view.builder.add(entry.getLongKey(), ChunkDecoration.pack(spec.order(), spec.kind(), entry.getByteValue() == 2));
            }
            if (chain.fogged()) {
                int[] box = chain.fogBox();
                view.builder.fog(new ChunkDecoration.Fog(spec.order(), spec.kind(), box[0], box[1], box[2], box[3], box[4], box[5]));
            }
        }
        return view.builder.build();
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/OrePredictor$Answers.class */
    @Environment(EnvType.CLIENT)
    private static final class Answers {
        static final int SIZE = 884736;
        final int[] stamp = new int[SIZE];
        final byte[] answer = new byte[SIZE];
        int generation;

        private Answers() {
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/OrePredictor$View.class */
    @Environment(EnvType.CLIENT)
    private static final class View implements OreSim.World {
        final SeedWorldgen world;
        final int chunkX;
        final int chunkZ;
        final Inputs inputs;
        final ChunkDecoration.Builder builder;
        final int generation;
        final int minX;
        final int minZ;
        OreSpec spec;
        final TerrainChunk[] terrains = new TerrainChunk[9];
        final GateHeights[] heights = new GateHeights[9];
        final Answers answers = OrePredictor.ANSWERS.get();

        View(SeedWorldgen world, int chunkX, int chunkZ, Inputs inputs) {
            this.world = world;
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
            this.inputs = inputs;
            this.builder = new ChunkDecoration.Builder(chunkX, chunkZ);
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    this.terrains[((dx + 1) * 3) + dz + 1] = inputs.terrain(chunkX + dx, chunkZ + dz);
                    this.heights[((dx + 1) * 3) + dz + 1] = inputs.gateHeights(chunkX + dx, chunkZ + dz);
                }
            }
            Answers answers = this.answers;
            int i = answers.generation + 1;
            answers.generation = i;
            this.generation = i;
            this.minX = (chunkX - 1) << 4;
            this.minZ = (chunkZ - 1) << 4;
        }

        private int around(int x, int z) {
            int dx = (x >> 4) - this.chunkX;
            int dz = (z >> 4) - this.chunkZ;
            if (dx < -1 || dx > 1 || dz < -1 || dz > 1) {
                throw new IllegalStateException("read outside the 3x3 chunks at " + x + ", " + z);
            }
            return ((dx + 1) * 3) + dz + 1;
        }

        private TerrainChunk terrain(int x, int z) {
            return this.terrains[around(x, z)];
        }

        private byte answers(int x, int y, int z) {
            byte b;
            int lx = x - this.minX;
            int lz = z - this.minZ;
            int index = ((((y - (-64)) * 48) + lz) * 48) + lx;
            if (this.answers.stamp[index] == this.generation) {
                return this.answers.answer[index];
            }
            byte model = terrain(x, z).kind(x, y, z);
            byte base = VBlock.isBase(model) ? (byte) 1 : (byte) 0;
            boolean disagrees = OrePredictor.disagreesOnBase(this.inputs, model, x, y, z);
            byte forAir = disagrees ? (byte) 2 : base;
            byte forDraws = forAir;
            if (forDraws == 1 && this.inputs.othersMayWrite(x, y, z)) {
                forDraws = 2;
            }
            if (OrePredictor.disagreesOnAir(this.inputs, model, x, y, z)) {
                b = 2;
            } else {
                b = model == 0 ? (byte) 1 : (byte) 0;
            }
            byte air = b;
            byte packed = (byte) (forDraws | (forAir << 2) | (air << 4));
            this.answers.stamp[index] = this.generation;
            this.answers.answer[index] = packed;
            return packed;
        }

        @Override // net.anubis.client.util.seedscan.OreSim.World
        public long seed() {
            return this.world.seed;
        }

        @Override // net.anubis.client.util.seedscan.OreSim.World
        public int gateLow(int x, int z) {
            return this.heights[around(x, z)].low(x, z);
        }

        @Override // net.anubis.client.util.seedscan.OreSim.World
        public int gateHigh(int x, int z) {
            return this.heights[around(x, z)].high(x, z);
        }

        @Override // net.anubis.client.util.seedscan.OreSim.World
        public byte base(int x, int y, int z) {
            byte base;
            if (this.spec.drawsOnTerrain()) {
                base = (byte) (answers(x, y, z) & 3);
                if (base == 1 && ownFogCovers(x, y, z)) {
                    base = 2;
                }
            } else if (this.spec.discard() > 0.0f) {
                base = (byte) ((answers(x, y, z) >> 2) & 3);
            } else {
                base = VBlock.isBase(terrain(x, z).kind(x, y, z)) ? (byte) 1 : (byte) 0;
            }
            ShortArrayList own = this.builder.at(class_2338.method_10064(x, y, z));
            if (own != null) {
                for (int n = 0; n < own.size(); n++) {
                    short write = own.getShort(n);
                    if (!VBlock.isBase(ChunkDecoration.kind(write))) {
                        base = (!ChunkDecoration.possible(write) || base == 0) ? (byte) 0 : (byte) 2;
                    }
                }
            }
            return base;
        }

        private boolean ownFogCovers(int x, int y, int z) {
            for (ChunkDecoration.Fog fog : this.builder.fogList()) {
                if (!VBlock.isBase(fog.kind()) && fog.contains(x, y, z)) {
                    return true;
                }
            }
            return false;
        }

        @Override // net.anubis.client.util.seedscan.OreSim.World
        public byte air(int x, int y, int z) {
            if (y < -64 || y > 319) {
                return (byte) 1;
            }
            if (this.spec.discard() > 0.0f) {
                return (byte) ((answers(x, y, z) >> 4) & 3);
            }
            return terrain(x, z).kind(x, y, z) == 0 ? (byte) 1 : (byte) 0;
        }

        @Override // net.anubis.client.util.seedscan.OreSim.World
        public boolean inBiome(OreSpec spec, int x, int y, int z) {
            return spec.biomes() == null || spec.inBiome(this.world.biomeAt(x, y, z));
        }
    }
}
