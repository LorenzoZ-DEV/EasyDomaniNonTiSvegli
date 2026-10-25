package net.anubis.client.util.seedscan;

import imgui.flag.ImGuiWindowFlags;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_7923;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/BlobJudge.class */
@Environment(EnvType.CLIENT)
public final class BlobJudge {
    public static final int JUDGED_MIN_Y = 0;
    public static final int JUDGED_MAX_Y = 30;
    public static final double MIN_MATCH = 0.6d;
    public static final int MIN_JUDGED = 8;
    public static final int MAX_FILLED = 3;
    public static final int MAX_CLUSTER = 20000;
    public static final int SHELL_MODEL_CAVE = 16;
    public static final int SHELL_JUDGED = 32;
    public static final int SHELL_STONE = 64;
    private static final Set<class_2248> NATURAL = set(class_2246.field_27114, class_2246.field_29032, class_2246.field_27159, class_2246.field_27160, class_2246.field_28049, class_2246.field_28048, class_2246.field_28681, class_2246.field_28680, class_2246.field_28685, class_2246.field_10460, class_2246.field_37568, class_2246.field_37569, class_2246.field_37570, class_2246.field_28108, class_2246.field_37571, class_2246.field_10092, class_2246.field_10515, class_2246.field_22423);
    private static final Set<class_2248> FAKES = set(class_2246.field_10418, class_2246.field_29219, class_2246.field_10212, class_2246.field_29027, class_2246.field_27120, class_2246.field_29221, class_2246.field_10571, class_2246.field_29026, class_2246.field_10080, class_2246.field_29030, class_2246.field_10090, class_2246.field_29028, class_2246.field_10442, class_2246.field_29029, class_2246.field_10013, class_2246.field_29220, class_2246.field_33508, class_2246.field_33509, class_2246.field_10277, class_2246.field_29224, class_2246.field_9989, class_2246.field_10540, class_2246.field_10034, class_2246.field_10443);
    private static final Map<class_2248, Boolean> STRUCTURE = new ConcurrentHashMap();

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/BlobJudge$Client.class */
    @Environment(EnvType.CLIENT)
    public interface Client {
        class_2680 state(int i, int i2, int i3);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/BlobJudge$Status.class */
    @Environment(EnvType.CLIENT)
    public enum Status {
        ALERT,
        CLEAN,
        OPEN,
        STRUCTURE,
        TERRAIN,
        MISPREDICTED,
        HIDDEN,
        TOO_FEW,
        INCOMPLETE,
        OUT_OF_BAND
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/BlobJudge$Vanilla.class */
    @Environment(EnvType.CLIENT)
    public interface Vanilla {
        VanillaBand band(int i, int i2);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/BlobJudge$Cluster.class */
    @Environment(EnvType.CLIENT)
    public static final class Cluster extends Record {
        private final byte kind;
        private final long owner;
        private final Status unfinished;
        private final long[] cells;
        private final long[] judged;
        private final long[] shell;
        private final byte[] shellFlags;

        public Cluster(byte kind, long owner, Status unfinished, long[] cells, long[] judged, long[] shell, byte[] shellFlags) {
            this.kind = kind;
            this.owner = owner;
            this.unfinished = unfinished;
            this.cells = cells;
            this.judged = judged;
            this.shell = shell;
            this.shellFlags = shellFlags;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Cluster.class), Cluster.class, "kind;owner;unfinished;cells;judged;shell;shellFlags", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Cluster;->kind:B", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Cluster;->owner:J", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Cluster;->unfinished:Lnet/anubis/client/util/seedscan/BlobJudge$Status;", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Cluster;->cells:[J", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Cluster;->judged:[J", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Cluster;->shell:[J", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Cluster;->shellFlags:[B").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Cluster.class), Cluster.class, "kind;owner;unfinished;cells;judged;shell;shellFlags", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Cluster;->kind:B", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Cluster;->owner:J", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Cluster;->unfinished:Lnet/anubis/client/util/seedscan/BlobJudge$Status;", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Cluster;->cells:[J", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Cluster;->judged:[J", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Cluster;->shell:[J", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Cluster;->shellFlags:[B").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Cluster.class, Object.class), Cluster.class, "kind;owner;unfinished;cells;judged;shell;shellFlags", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Cluster;->kind:B", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Cluster;->owner:J", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Cluster;->unfinished:Lnet/anubis/client/util/seedscan/BlobJudge$Status;", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Cluster;->cells:[J", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Cluster;->judged:[J", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Cluster;->shell:[J", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Cluster;->shellFlags:[B").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public byte kind() {
            return this.kind;
        }

        public long owner() {
            return this.owner;
        }

        public Status unfinished() {
            return this.unfinished;
        }

        public long[] cells() {
            return this.cells;
        }

        public long[] judged() {
            return this.judged;
        }

        public long[] shell() {
            return this.shell;
        }

        public byte[] shellFlags() {
            return this.shellFlags;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/BlobJudge$Verdict.class */
    @Environment(EnvType.CLIENT)
    public static final class Verdict extends Record {
        private final Status status;
        private final byte kind;
        private final long owner;
        private final long[] changes;

        public Verdict(Status status, byte kind, long owner, long[] changes) {
            this.status = status;
            this.kind = kind;
            this.owner = owner;
            this.changes = changes;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Verdict.class), Verdict.class, "status;kind;owner;changes", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Verdict;->status:Lnet/anubis/client/util/seedscan/BlobJudge$Status;", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Verdict;->kind:B", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Verdict;->owner:J", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Verdict;->changes:[J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Verdict.class), Verdict.class, "status;kind;owner;changes", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Verdict;->status:Lnet/anubis/client/util/seedscan/BlobJudge$Status;", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Verdict;->kind:B", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Verdict;->owner:J", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Verdict;->changes:[J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Verdict.class, Object.class), Verdict.class, "status;kind;owner;changes", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Verdict;->status:Lnet/anubis/client/util/seedscan/BlobJudge$Status;", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Verdict;->kind:B", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Verdict;->owner:J", "FIELD:Lnet/anubis/client/util/seedscan/BlobJudge$Verdict;->changes:[J").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public Status status() {
            return this.status;
        }

        public byte kind() {
            return this.kind;
        }

        public long owner() {
            return this.owner;
        }

        public long[] changes() {
            return this.changes;
        }

        public boolean alert() {
            return this.status == Status.ALERT;
        }
    }

    private BlobJudge() {
    }

    public static List<Cluster> clusters(int chunkX, int chunkZ, Vanilla vanilla) {
        List<Cluster> clusters = new ArrayList<>();
        VanillaBand band = vanilla.band(chunkX, chunkZ);
        if (band == null) {
            return clusters;
        }
        Bands bands = new Bands(vanilla);
        for (byte kind : new byte[]{4, 5, 6, 8}) {
            int bit = VBlock.familyBit(kind);
            LongOpenHashSet seen = new LongOpenHashSet();
            for (int y = 0; y <= 30; y++) {
                for (int z = chunkZ << 4; z < (chunkZ << 4) + 16; z++) {
                    for (int x = chunkX << 4; x < (chunkX << 4) + 16; x++) {
                        if ((band.families(x, y, z) & bit) != 0) {
                            long start = class_2338.method_10064(x, y, z);
                            if (!seen.contains(start)) {
                                Cluster cluster = cluster(start, kind, bands, seen);
                                if ((class_2338.method_10061(cluster.owner()) >> 4) == chunkX && (class_2338.method_10083(cluster.owner()) >> 4) == chunkZ) {
                                    clusters.add(cluster);
                                }
                            }
                        }
                    }
                }
            }
        }
        return clusters;
    }

    private static Cluster cluster(long start, byte kind, Bands vanilla, LongOpenHashSet seen) {
        int bit = VBlock.familyBit(kind);
        LongArrayList cells = new LongArrayList();
        LongOpenHashSet in = new LongOpenHashSet();
        LongArrayFIFOQueue open = new LongArrayFIFOQueue();
        open.enqueue(start);
        in.add(start);
        boolean incomplete = false;
        boolean outOfBand = false;
        boolean judgedLayers = false;
        long owner = start;
        while (!open.isEmpty()) {
            long pos = open.dequeueLong();
            cells.add(pos);
            int cy = class_2338.method_10071(pos);
            if (cy >= 0 && cy <= 30) {
                if (!judgedLayers || before(pos, owner)) {
                    owner = pos;
                }
                judgedLayers = true;
            }
            if (cells.size() > 20000) {
                outOfBand = true;
                break;
            }
            int px = class_2338.method_10061(pos);
            int py = class_2338.method_10071(pos);
            int pz = class_2338.method_10083(pos);
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx != 0 || dy != 0 || dz != 0) {
                            int x = px + dx;
                            int y = py + dy;
                            int z = pz + dz;
                            long next = class_2338.method_10064(x, y, z);
                            if (!in.contains(next)) {
                                if (!VanillaBand.inBand(y)) {
                                    outOfBand = true;
                                } else {
                                    VanillaBand band = vanilla.at(x, z);
                                    if (band == null) {
                                        incomplete = true;
                                    } else if ((band.families(x, y, z) & bit) != 0) {
                                        in.add(next);
                                        open.enqueue(next);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        seen.addAll(in);
        long[] none = new long[0];
        if (outOfBand) {
            return new Cluster(kind, owner, Status.OUT_OF_BAND, none, none, none, new byte[0]);
        }
        if (incomplete) {
            return new Cluster(kind, owner, Status.INCOMPLETE, none, none, none, new byte[0]);
        }
        LongArrayList judged = new LongArrayList();
        LongOpenHashSet shell = new LongOpenHashSet();
        for (int i = 0; i < cells.size(); i++) {
            long pos2 = cells.getLong(i);
            int px2 = class_2338.method_10061(pos2);
            int py2 = class_2338.method_10071(pos2);
            int pz2 = class_2338.method_10083(pos2);
            if (py2 >= 0 && py2 <= 30 && vanilla.at(px2, pz2).certain(px2, py2, pz2) == kind) {
                judged.add(pos2);
            }
            for (int dx2 = -1; dx2 <= 1; dx2++) {
                for (int dy2 = -1; dy2 <= 1; dy2++) {
                    for (int dz2 = -1; dz2 <= 1; dz2++) {
                        long next2 = class_2338.method_10064(px2 + dx2, py2 + dy2, pz2 + dz2);
                        if (!in.contains(next2)) {
                            shell.add(next2);
                        }
                    }
                }
            }
        }
        long[] shellCells = shell.toLongArray();
        ByteArrayList flags = new ByteArrayList(shellCells.length);
        for (long pos3 : shellCells) {
            int x2 = class_2338.method_10061(pos3);
            int y2 = class_2338.method_10071(pos3);
            int z2 = class_2338.method_10083(pos3);
            int flag = 0;
            VanillaBand band2 = vanilla.at(x2, z2);
            if (band2 != null && VanillaBand.inBand(y2)) {
                flag = band2.families(x2, y2, z2);
                byte certain = band2.certain(x2, y2, z2);
                if (VBlock.isOpen(certain)) {
                    flag |= 16;
                }
                if (certain == 3) {
                    flag |= 64;
                }
                if (y2 >= 0 && y2 <= 30) {
                    flag |= 32;
                }
            }
            flags.add((byte) flag);
        }
        return new Cluster(kind, owner, null, cells.toLongArray(), judged.toLongArray(), shellCells, flags.toByteArray());
    }

    public static Verdict judge(Cluster cluster, Client client, int minChanges) {
        byte kind = cluster.kind();
        long owner = cluster.owner();
        if (cluster.unfinished() != null) {
            return verdict(cluster.unfinished(), kind, owner);
        }
        boolean structure = false;
        for (long pos : cluster.cells()) {
            class_2680 state = client.state(class_2338.method_10061(pos), class_2338.method_10071(pos), class_2338.method_10083(pos));
            if (state == null) {
                return verdict(Status.INCOMPLETE, kind, owner);
            }
            if (open(state)) {
                return verdict(Status.OPEN, kind, owner);
            }
            structure |= isStructure(state.method_26204());
        }
        long[] shell = cluster.shell();
        byte[] flags = cluster.shellFlags();
        int filled = 0;
        for (int i = 0; i < shell.length; i++) {
            class_2680 state2 = client.state(class_2338.method_10061(shell[i]), class_2338.method_10071(shell[i]), class_2338.method_10083(shell[i]));
            if (state2 == null) {
                return verdict(Status.INCOMPLETE, kind, owner);
            }
            if (open(state2)) {
                return verdict(Status.OPEN, kind, owner);
            }
            structure |= isStructure(state2.method_26204());
            if ((flags[i] & 16) != 0) {
                filled++;
            }
        }
        if (structure) {
            return verdict(Status.STRUCTURE, kind, owner);
        }
        if (filled > 3) {
            return verdict(Status.TERRAIN, kind, owner);
        }
        class_2248 block = VBlock.familyBlock(kind);
        int judged = 0;
        int matches = 0;
        int fakes = 0;
        LongArrayList inside = new LongArrayList();
        LongArrayList outside = new LongArrayList();
        for (long pos2 : cluster.judged()) {
            class_2248 shown = client.state(class_2338.method_10061(pos2), class_2338.method_10071(pos2), class_2338.method_10083(pos2)).method_26204();
            if (shown == block) {
                judged++;
                matches++;
            } else if (FAKES.contains(shown)) {
                fakes++;
            } else if (!NATURAL.contains(shown)) {
                judged++;
                inside.add(pos2);
            }
        }
        for (int i2 = 0; i2 < shell.length; i2++) {
            if ((flags[i2] & 32) != 0 && (flags[i2] & 16) == 0) {
                int x = class_2338.method_10061(shell[i2]);
                int y = class_2338.method_10071(shell[i2]);
                int z = class_2338.method_10083(shell[i2]);
                class_2680 state3 = client.state(x, y, z);
                byte shown2 = VBlock.of(state3);
                int shownBit = VBlock.familyBit(shown2);
                if (shownBit != 0) {
                    if ((flags[i2] & shownBit) == 0 && (shown2 != 8 || !nearOpen(client, x, y, z))) {
                        outside.add(shell[i2]);
                    }
                } else if ((flags[i2] & 64) != 0 && shown2 != 3 && !FAKES.contains(state3.method_26204()) && !NATURAL.contains(state3.method_26204()) && !nearOpen(client, x, y, z)) {
                    outside.add(shell[i2]);
                }
            }
        }
        if (judged + fakes > 0 && fakes * 10 > (judged + fakes) * 3) {
            return verdict(Status.HIDDEN, kind, owner);
        }
        if (judged < 8) {
            return verdict(Status.TOO_FEW, kind, owner);
        }
        if (matches < 0.6d * ((double) judged)) {
            return verdict(Status.MISPREDICTED, kind, owner);
        }
        long[] changes = new long[inside.size() + outside.size()];
        for (int i3 = 0; i3 < inside.size(); i3++) {
            changes[i3] = inside.getLong(i3);
        }
        for (int i4 = 0; i4 < outside.size(); i4++) {
            changes[inside.size() + i4] = outside.getLong(i4);
        }
        Status status = changes.length >= Math.max(1, minChanges) ? Status.ALERT : Status.CLEAN;
        return new Verdict(status, kind, owner, changes);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/BlobJudge$Bands.class */
    @Environment(EnvType.CLIENT)
    private static final class Bands implements Vanilla {
        private final Vanilla vanilla;
        private int lastX = ImGuiWindowFlags.NavFlattened;
        private int lastZ = ImGuiWindowFlags.NavFlattened;
        private VanillaBand last;

        Bands(Vanilla vanilla) {
            this.vanilla = vanilla;
        }

        VanillaBand at(int x, int z) {
            return band(x >> 4, z >> 4);
        }

        @Override // net.anubis.client.util.seedscan.BlobJudge.Vanilla
        public VanillaBand band(int chunkX, int chunkZ) {
            if (chunkX != this.lastX || chunkZ != this.lastZ) {
                this.lastX = chunkX;
                this.lastZ = chunkZ;
                this.last = this.vanilla.band(chunkX, chunkZ);
            }
            return this.last;
        }
    }

    private static Verdict verdict(Status status, byte kind, long owner) {
        return new Verdict(status, kind, owner, new long[0]);
    }

    public static boolean open(class_2680 state) {
        return state.method_26215() || !state.method_26227().method_15769();
    }

    private static boolean nearOpen(Client client, int x, int y, int z) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    class_2680 state = client.state(x + dx, y + dy, z + dz);
                    if (state == null || open(state)) {
                        return true;
                    }
                }
            }
        }
        return false;
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

    static boolean isStructure(class_2248 block) {
        Boolean known = STRUCTURE.get(block);
        return (known != null ? known : STRUCTURE.computeIfAbsent(block, BlobJudge::structureBlock)).booleanValue();
    }

    private static boolean structureBlock(class_2248 block) {
        if (block == class_2246.field_10166 || block == class_2246.field_10260 || block == class_2246.field_47336 || block == class_2246.field_48851 || block == class_2246.field_10398 || block == class_2246.field_38420 || block == class_2246.field_10174) {
            return true;
        }
        String path = class_7923.field_41175.method_10221(block).method_12832();
        return path.contains("stone_brick") || (path.contains("tuff") && !path.equals("tuff")) || (!(!path.contains("copper") || path.contains("ore") || path.equals("raw_copper_block")) || path.startsWith("deepslate_brick") || path.startsWith("deepslate_tile") || path.startsWith("cracked_deepslate") || path.startsWith("polished_deepslate") || path.equals("chiseled_deepslate") || path.startsWith("suspicious_") || path.contains("prismarine") || path.endsWith("sponge") || path.contains("terracotta") || path.startsWith("mud_brick"));
    }

    private static Set<class_2248> set(class_2248... blocks) {
        Set<class_2248> set = Collections.newSetFromMap(new IdentityHashMap());
        Collections.addAll(set, blocks);
        return set;
    }
}
