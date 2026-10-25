package net.anubis.client.module.impl.misc;

import com.example.client.mixin.AccessorNetheriteClientLevel;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.anubis.client.chat.ModuleChat;
import net.anubis.client.util.netherite.NetheriteChunkTracker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1799;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_2846;
import net.minecraft.class_2868;
import net.minecraft.class_310;
import net.minecraft.class_638;
import net.minecraft.class_7202;
import net.minecraft.class_746;
import net.minecraft.class_9334;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/AdvancedFinder.class */
@Environment(EnvType.CLIENT)
final class AdvancedFinder {
    private static final double REACH = 5.0d;
    private static final double PROBES_PER_TICK = 18.0d;
    private static final double BURST = 600.0d;
    private static final int RECOUNT_INTERVAL_TICKS = 10;
    private static final int MAX_REVEALED = 256;
    private final NetheriteFinderModule module;
    private boolean armed;
    private int ticks;
    private double budget;
    private int recountCooldown;
    private static final int MISSING_REFRESH_TICKS = 10;
    private int handWarning;
    private class_638 level;
    private final Map<NetheriteChunkTracker.Candidate, Scan> scans = new HashMap();
    private final Set<NetheriteChunkTracker.Candidate> done = new HashSet();
    private final Set<class_2338> revealed = new LinkedHashSet();
    private String lastReason = "";

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/AdvancedFinder$MissingVisitor.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    interface MissingVisitor {
        void visit(int i, int i2, int i3);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/AdvancedFinder$ProbeVisitor.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    interface ProbeVisitor {
        void visit(int i, int i2, int i3, int i4);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/AdvancedFinder$Probe.class */
    @Environment(EnvType.CLIENT)
    private static final class Probe extends Record {
        private final Scan scan;
        private final int index;
        private final class_2338 pos;
        private final double distanceSq;

        private Probe(Scan scan, int index, class_2338 pos, double distanceSq) {
            this.scan = scan;
            this.index = index;
            this.pos = pos;
            this.distanceSq = distanceSq;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Probe.class), Probe.class, "scan;index;pos;distanceSq", "FIELD:Lnet/anubis/client/module/impl/misc/AdvancedFinder$Probe;->scan:Lnet/anubis/client/module/impl/misc/AdvancedFinder$Scan;", "FIELD:Lnet/anubis/client/module/impl/misc/AdvancedFinder$Probe;->index:I", "FIELD:Lnet/anubis/client/module/impl/misc/AdvancedFinder$Probe;->pos:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/misc/AdvancedFinder$Probe;->distanceSq:D").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Probe.class), Probe.class, "scan;index;pos;distanceSq", "FIELD:Lnet/anubis/client/module/impl/misc/AdvancedFinder$Probe;->scan:Lnet/anubis/client/module/impl/misc/AdvancedFinder$Scan;", "FIELD:Lnet/anubis/client/module/impl/misc/AdvancedFinder$Probe;->index:I", "FIELD:Lnet/anubis/client/module/impl/misc/AdvancedFinder$Probe;->pos:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/misc/AdvancedFinder$Probe;->distanceSq:D").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Probe.class, Object.class), Probe.class, "scan;index;pos;distanceSq", "FIELD:Lnet/anubis/client/module/impl/misc/AdvancedFinder$Probe;->scan:Lnet/anubis/client/module/impl/misc/AdvancedFinder$Scan;", "FIELD:Lnet/anubis/client/module/impl/misc/AdvancedFinder$Probe;->index:I", "FIELD:Lnet/anubis/client/module/impl/misc/AdvancedFinder$Probe;->pos:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/misc/AdvancedFinder$Probe;->distanceSq:D").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public Scan scan() {
            return this.scan;
        }

        public int index() {
            return this.index;
        }

        public class_2338 pos() {
            return this.pos;
        }

        public double distanceSq() {
            return this.distanceSq;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/AdvancedFinder$Scan.class */
    @Environment(EnvType.CLIENT)
    private static final class Scan {
        final NetheriteChunkTracker.Candidate section;
        final int total;
        int missingCount;
        int sent;
        boolean dirty;
        final BitSet probed = new BitSet(4096);
        final int[] stamp = new int[4096];
        final int[] missing = new int[4096];
        int missingAt = -1000;

        Scan(NetheriteChunkTracker.Candidate section, int total) {
            this.section = section;
            this.total = total;
        }
    }

    AdvancedFinder(NetheriteFinderModule module) {
        this.module = module;
    }

    boolean armed() {
        return this.armed;
    }

    Collection<class_2338> revealed() {
        return this.revealed;
    }

    boolean isDone(NetheriteChunkTracker.Candidate section) {
        return this.done.contains(section);
    }

    float progress(NetheriteChunkTracker.Candidate section) {
        Scan scan = this.armed ? this.scans.get(section) : null;
        if (scan == null) {
            return -1.0f;
        }
        return fraction(scan);
    }

    private static float fraction(Scan scan) {
        if (scan.total == 0) {
            return 1.0f;
        }
        return Math.min(1.0f, scan.sent / scan.total);
    }

    void forEachProbe(class_243 eye, double radius, int limit, float missingFrom, ProbeVisitor visitor) {
        if (this.armed) {
            double radiusSq = radius * radius;
            int emitted = 0;
            for (Scan scan : this.scans.values()) {
                if (fraction(scan) < missingFrom) {
                    int baseX = scan.section.chunkX() << 4;
                    int baseY = scan.section.sectionY() << 4;
                    int baseZ = scan.section.chunkZ() << 4;
                    for (int index = 0; index < 4096; index++) {
                        int at = scan.stamp[index];
                        if (at != 0) {
                            int x = baseX + (index & 15);
                            int y = baseY + (index >> 8);
                            int z = baseZ + ((index >> 4) & 15);
                            double dx = (((double) x) + 0.5d) - eye.field_1352;
                            double dy = (((double) y) + 0.5d) - eye.field_1351;
                            double dz = (((double) z) + 0.5d) - eye.field_1350;
                            if ((dx * dx) + (dy * dy) + (dz * dz) > radiusSq) {
                                continue;
                            } else {
                                visitor.visit(x, y, z, (this.ticks - at) + 1);
                                emitted++;
                                if (emitted >= limit) {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    void forEachMissing(class_243 eye, double radius, int limit, float missingFrom, MissingVisitor visitor) {
        if (this.armed) {
            double radiusSq = radius * radius;
            int emitted = 0;
            for (Scan scan : this.scans.values()) {
                if (fraction(scan) >= missingFrom) {
                    refreshMissing(scan);
                    int baseX = scan.section.chunkX() << 4;
                    int baseY = scan.section.sectionY() << 4;
                    int baseZ = scan.section.chunkZ() << 4;
                    for (int i = 0; i < scan.missingCount; i++) {
                        int index = scan.missing[i];
                        if (!scan.probed.get(index)) {
                            int x = baseX + (index & 15);
                            int y = baseY + (index >> 8);
                            int z = baseZ + ((index >> 4) & 15);
                            double dx = (((double) x) + 0.5d) - eye.field_1352;
                            double dy = (((double) y) + 0.5d) - eye.field_1351;
                            double dz = (((double) z) + 0.5d) - eye.field_1350;
                            if ((dx * dx) + (dy * dy) + (dz * dz) > radiusSq) {
                                continue;
                            } else {
                                visitor.visit(x, y, z);
                                emitted++;
                                if (emitted >= limit) {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void refreshMissing(Scan scan) {
        if (this.level == null || this.ticks - scan.missingAt < 10) {
            return;
        }
        scan.missingAt = this.ticks;
        int baseX = scan.section.chunkX() << 4;
        int baseY = scan.section.sectionY() << 4;
        int baseZ = scan.section.chunkZ() << 4;
        class_2338.class_2339 cursor = new class_2338.class_2339();
        int count = 0;
        int iNextClearBit = scan.probed.nextClearBit(0);
        while (true) {
            int index = iNextClearBit;
            if (index < 4096) {
                cursor.method_10103(baseX + (index & 15), baseY + (index >> 8), baseZ + ((index >> 4) & 15));
                if (worthProbing(this.level, cursor)) {
                    int i = count;
                    count++;
                    scan.missing[i] = index;
                }
                iNextClearBit = scan.probed.nextClearBit(index + 1);
            } else {
                scan.missingCount = count;
                return;
            }
        }
    }

    void arm() {
        this.armed = true;
        this.lastReason = "";
    }

    void disarm(class_310 mc) {
        this.armed = false;
        this.lastReason = "";
    }

    void reset(class_310 mc) {
        disarm(mc);
        this.scans.clear();
        this.done.clear();
        this.revealed.clear();
        this.budget = 0.0d;
        this.level = null;
    }

    boolean onRevealed(class_2338 pos) {
        NetheriteChunkTracker.Candidate section = new NetheriteChunkTracker.Candidate(pos.method_10263() >> 4, pos.method_10260() >> 4, pos.method_10264() >> 4);
        if (!this.revealed.add(pos)) {
            return false;
        }
        if (this.revealed.size() > 256) {
            Iterator<class_2338> oldest = this.revealed.iterator();
            oldest.next();
            oldest.remove();
        }
        if (this.armed && this.scans.remove(section) != null) {
            this.done.add(section);
            ModuleChat.send(this.module, ModuleChat.body().text("Ancient debris ").good("found").text(" at ").coords(pos.method_10263(), pos.method_10264(), pos.method_10260()));
            return true;
        }
        return true;
    }

    void maintain(class_310 mc) {
        class_638 world = mc.field_1687;
        if (world == this.level) {
            if (world != null) {
                this.revealed.removeIf(pos -> {
                    return !world.method_8320(pos).method_27852(class_2246.field_22109);
                });
            }
        } else {
            this.scans.clear();
            this.done.clear();
            this.revealed.clear();
            this.level = world;
        }
    }

    void tick(class_310 mc) {
        if (this.armed) {
            class_746 player = mc.field_1724;
            class_638 world = mc.field_1687;
            if (player == null || world == null || player.field_3944 == null || mc.field_1761 == null) {
                return;
            }
            this.ticks++;
            this.budget = Math.min(BURST, this.budget + PROBES_PER_TICK);
            if (this.handWarning > 0) {
                this.handWarning--;
            }
            if (!NetheriteChunkTracker.isNether(world)) {
                hold("Not in the Nether");
                return;
            }
            this.lastReason = "";
            if (mc.field_1755 != null || player.method_6115() || mc.field_1761.method_2923()) {
                return;
            }
            class_243 eye = player.method_33571();
            refreshScans(world, eye);
            if (this.scans.isEmpty() || this.budget < 1.0d) {
                return;
            }
            List<Probe> probes = collect(world, eye);
            if (probes.isEmpty()) {
                return;
            }
            int selected = player.method_31548().method_67532();
            int slot = probeSlot(player, selected);
            if (slot < 0) {
                if (this.handWarning <= 0) {
                    this.handWarning = 200;
                    ModuleChat.send(this.module, ModuleChat.body().text("Hotbar needs an empty or non-tool slot"));
                    return;
                }
                return;
            }
            if (slot != selected) {
                player.field_3944.method_52787(new class_2868(slot));
            }
            class_2338 last = null;
            for (Probe probe : probes) {
                if (this.budget < 1.0d) {
                    break;
                }
                sendAction(player, world, class_2846.class_2847.field_12968, probe.pos());
                probe.scan().probed.set(probe.index());
                probe.scan().sent++;
                probe.scan().stamp[probe.index()] = this.ticks;
                probe.scan().dirty = true;
                this.budget -= 1.0d;
                last = probe.pos();
            }
            if (last != null) {
                sendAction(player, world, class_2846.class_2847.field_12971, last);
            }
            if (slot != selected) {
                player.field_3944.method_52787(new class_2868(selected));
            }
        }
    }

    private void refreshScans(class_638 world, class_243 eye) {
        Set<NetheriteChunkTracker.Candidate> live = new HashSet<>(NetheriteChunkTracker.snapshot());
        this.scans.keySet().retainAll(live);
        for (NetheriteChunkTracker.Candidate candidate : live) {
            if (!this.done.contains(candidate) && !this.scans.containsKey(candidate) && distanceToSectionSq(eye, candidate) <= 25.0d) {
                this.scans.put(candidate, new Scan(candidate, countProbeable(world, candidate)));
            }
        }
        int i = this.recountCooldown;
        this.recountCooldown = i - 1;
        if (i > 0) {
            return;
        }
        this.recountCooldown = 10;
        Iterator<Scan> iterator = this.scans.values().iterator();
        while (iterator.hasNext()) {
            Scan scan = iterator.next();
            if (scan.dirty) {
                scan.dirty = false;
                if (!hasUnprobed(world, scan)) {
                    iterator.remove();
                    this.done.add(scan.section);
                    NetheriteChunkTracker.Candidate s = scan.section;
                    ModuleChat.send(this.module, ModuleChat.body().text("Section ").good("clean").text(" at ").coords((s.chunkX() * 16) + 8, (s.sectionY() * 16) + 8, (s.chunkZ() * 16) + 8));
                }
            }
        }
    }

    private List<Probe> collect(class_638 world, class_243 eye) {
        List<Probe> coarse = new ArrayList<>();
        List<Probe> fine = new ArrayList<>();
        int span = (int) Math.ceil(REACH);
        int cx = (int) Math.floor(eye.field_1352);
        int cy = (int) Math.floor(eye.field_1351);
        int cz = (int) Math.floor(eye.field_1350);
        class_2338.class_2339 cursor = new class_2338.class_2339();
        for (Scan scan : this.scans.values()) {
            int baseX = scan.section.chunkX() << 4;
            int baseY = scan.section.sectionY() << 4;
            int baseZ = scan.section.chunkZ() << 4;
            int minX = Math.max(baseX, cx - span);
            int maxX = Math.min(baseX + 15, cx + span);
            int minY = Math.max(baseY, cy - span);
            int maxY = Math.min(baseY + 15, cy + span);
            int minZ = Math.max(baseZ, cz - span);
            int maxZ = Math.min(baseZ + 15, cz + span);
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        int lx = x & 15;
                        int ly = y & 15;
                        int lz = z & 15;
                        int index = (ly << 8) | (lz << 4) | lx;
                        if (!scan.probed.get(index)) {
                            cursor.method_10103(x, y, z);
                            double distanceSq = distanceToBlockSq(eye, cursor);
                            if (distanceSq <= 25.0d) {
                                if (!worthProbing(world, cursor)) {
                                    scan.probed.set(index);
                                } else {
                                    Probe probe = new Probe(scan, index, cursor.method_10062(), distanceSq);
                                    if (lx % 3 == 1 && ly % 3 == 1 && lz % 3 == 1) {
                                        coarse.add(probe);
                                    } else {
                                        fine.add(probe);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        coarse.sort(Comparator.comparingDouble((v0) -> {
            return v0.distanceSq();
        }));
        fine.sort(Comparator.comparingDouble((v0) -> {
            return v0.distanceSq();
        }));
        coarse.addAll(fine);
        return coarse;
    }

    private static int countProbeable(class_638 world, NetheriteChunkTracker.Candidate section) {
        int baseX = section.chunkX() << 4;
        int baseY = section.sectionY() << 4;
        int baseZ = section.chunkZ() << 4;
        class_2338.class_2339 cursor = new class_2338.class_2339();
        int count = 0;
        for (int index = 0; index < 4096; index++) {
            cursor.method_10103(baseX + (index & 15), baseY + (index >> 8), baseZ + ((index >> 4) & 15));
            if (worthProbing(world, cursor)) {
                count++;
            }
        }
        return count;
    }

    private static boolean hasUnprobed(class_638 world, Scan scan) {
        int baseX = scan.section.chunkX() << 4;
        int baseY = scan.section.sectionY() << 4;
        int baseZ = scan.section.chunkZ() << 4;
        class_2338.class_2339 cursor = new class_2338.class_2339();
        int iNextClearBit = scan.probed.nextClearBit(0);
        while (true) {
            int index = iNextClearBit;
            if (index < 4096) {
                cursor.method_10103(baseX + (index & 15), baseY + (index >> 8), baseZ + ((index >> 4) & 15));
                if (worthProbing(world, cursor)) {
                    return true;
                }
                iNextClearBit = scan.probed.nextClearBit(index + 1);
            } else {
                return false;
            }
        }
    }

    private static boolean worthProbing(class_638 world, class_2338 pos) {
        class_2680 state = world.method_8320(pos);
        return !state.method_26215() && state.method_26227().method_15769() && !state.method_27852(class_2246.field_22109) && state.method_26216() && state.method_26214(world, pos) > 0.0f;
    }

    private void hold(String reason) {
        if (reason.equals(this.lastReason)) {
            return;
        }
        this.lastReason = reason;
        ModuleChat.send(this.module, ModuleChat.body().text(reason));
    }

    private static void sendAction(class_746 player, class_638 world, class_2846.class_2847 action, class_2338 pos) {
        class_7202 handler = ((AccessorNetheriteClientLevel) world).anubis$blockStatePredictionHandler();
        class_7202 sequence = handler.method_41937();
        try {
            player.field_3944.method_52787(new class_2846(action, pos, class_2350.field_11036, sequence.method_41942()));
            if (sequence != null) {
                sequence.close();
            }
        } catch (Throwable th) {
            if (sequence != null) {
                try {
                    sequence.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private static double distanceToSectionSq(class_243 from, NetheriteChunkTracker.Candidate section) {
        double minX = ((double) section.chunkX()) * 16.0d;
        double minY = ((double) section.sectionY()) * 16.0d;
        double minZ = ((double) section.chunkZ()) * 16.0d;
        double dx = Math.max(0.0d, Math.max(minX - from.field_1352, from.field_1352 - (minX + 16.0d)));
        double dy = Math.max(0.0d, Math.max(minY - from.field_1351, from.field_1351 - (minY + 16.0d)));
        double dz = Math.max(0.0d, Math.max(minZ - from.field_1350, from.field_1350 - (minZ + 16.0d)));
        return (dx * dx) + (dy * dy) + (dz * dz);
    }

    private static double distanceToBlockSq(class_243 from, class_2338 pos) {
        double dx = Math.max(0.0d, Math.max(((double) pos.method_10263()) - from.field_1352, from.field_1352 - (((double) pos.method_10263()) + 1.0d)));
        double dy = Math.max(0.0d, Math.max(((double) pos.method_10264()) - from.field_1351, from.field_1351 - (((double) pos.method_10264()) + 1.0d)));
        double dz = Math.max(0.0d, Math.max(((double) pos.method_10260()) - from.field_1350, from.field_1350 - (((double) pos.method_10260()) + 1.0d)));
        return (dx * dx) + (dy * dy) + (dz * dz);
    }

    private static int probeSlot(class_746 player, int selected) {
        if (!isTool(player.method_31548().method_5438(selected))) {
            return selected;
        }
        for (int slot = 0; slot < 9; slot++) {
            if (player.method_31548().method_5438(slot).method_7960()) {
                return slot;
            }
        }
        for (int slot2 = 0; slot2 < 9; slot2++) {
            if (!isTool(player.method_31548().method_5438(slot2))) {
                return slot2;
            }
        }
        return -1;
    }

    private static boolean isTool(class_1799 stack) {
        return stack.method_57826(class_9334.field_50077);
    }
}
