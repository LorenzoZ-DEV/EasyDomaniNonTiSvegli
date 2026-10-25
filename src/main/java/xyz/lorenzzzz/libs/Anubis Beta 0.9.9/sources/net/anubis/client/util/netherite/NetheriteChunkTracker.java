package net.anubis.client.util.netherite;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.ref.WeakReference;
import java.lang.runtime.ObjectMethods;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2596;
import net.minecraft.class_2626;
import net.minecraft.class_2637;
import net.minecraft.class_2672;
import net.minecraft.class_2680;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_5321;
import net.minecraft.class_638;
import net.minecraft.class_7134;
import net.minecraft.class_7924;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/netherite/NetheriteChunkTracker.class */
@Environment(EnvType.CLIENT)
public final class NetheriteChunkTracker {
    static final int MAXIMUM_CANDIDATES = 512;
    private static final int ANCIENT_STATE_ID = class_2248.method_9507(class_2246.field_22109.method_9564());
    private static final class_5321<class_1937> DONUT_NETHER = class_5321.method_29179(class_7924.field_41223, class_2960.method_60655("worlds", "smp_nether"));
    private static final Set<Candidate> CANDIDATES = new LinkedHashSet();
    private static WeakReference<class_638> lastLevel = new WeakReference<>(null);
    private static class_5321<class_1937> lastDimension;
    private static CandidateListener listener;
    private static RevealListener revealListener;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/netherite/NetheriteChunkTracker$CandidateListener.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface CandidateListener {
        void onCandidate(Candidate candidate);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/netherite/NetheriteChunkTracker$RevealListener.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface RevealListener {
        void onRevealed(class_2338 class_2338Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/netherite/NetheriteChunkTracker$Candidate.class */
    @Environment(EnvType.CLIENT)
    public static final class Candidate extends Record {
        private final int chunkX;
        private final int chunkZ;
        private final int sectionY;

        public Candidate(int chunkX, int chunkZ, int sectionY) {
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
            this.sectionY = sectionY;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Candidate.class), Candidate.class, "chunkX;chunkZ;sectionY", "FIELD:Lnet/anubis/client/util/netherite/NetheriteChunkTracker$Candidate;->chunkX:I", "FIELD:Lnet/anubis/client/util/netherite/NetheriteChunkTracker$Candidate;->chunkZ:I", "FIELD:Lnet/anubis/client/util/netherite/NetheriteChunkTracker$Candidate;->sectionY:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Candidate.class), Candidate.class, "chunkX;chunkZ;sectionY", "FIELD:Lnet/anubis/client/util/netherite/NetheriteChunkTracker$Candidate;->chunkX:I", "FIELD:Lnet/anubis/client/util/netherite/NetheriteChunkTracker$Candidate;->chunkZ:I", "FIELD:Lnet/anubis/client/util/netherite/NetheriteChunkTracker$Candidate;->sectionY:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Candidate.class, Object.class), Candidate.class, "chunkX;chunkZ;sectionY", "FIELD:Lnet/anubis/client/util/netherite/NetheriteChunkTracker$Candidate;->chunkX:I", "FIELD:Lnet/anubis/client/util/netherite/NetheriteChunkTracker$Candidate;->chunkZ:I", "FIELD:Lnet/anubis/client/util/netherite/NetheriteChunkTracker$Candidate;->sectionY:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int chunkX() {
            return this.chunkX;
        }

        public int chunkZ() {
            return this.chunkZ;
        }

        public int sectionY() {
            return this.sectionY;
        }
    }

    private NetheriteChunkTracker() {
    }

    public static void onPacketApplied(class_2596<?> packet) {
        class_638 level = class_310.method_1551().field_1687;
        if (level == null || !isNether(level)) {
            return;
        }
        if (packet instanceof class_2672) {
            class_2672 chunk = (class_2672) packet;
            trackLevel(level);
            Iterator<Integer> it = HiddenDebrisScanner.findHiddenSections(chunk.method_38598(), level.method_32890(), level.method_32891(), ANCIENT_STATE_ID).iterator();
            while (it.hasNext()) {
                int sectionY = it.next().intValue();
                add(new Candidate(chunk.method_11523(), chunk.method_11524(), sectionY));
            }
            return;
        }
        if (packet instanceof class_2626) {
            class_2626 update = (class_2626) packet;
            onBlockUpdate(update.method_11309(), update.method_11308());
        } else if (packet instanceof class_2637) {
            class_2637 sectionUpdate = (class_2637) packet;
            sectionUpdate.method_30621(NetheriteChunkTracker::onBlockUpdate);
        }
    }

    private static void onBlockUpdate(class_2338 pos, class_2680 state) {
        if (class_2248.method_9507(state) != ANCIENT_STATE_ID) {
            return;
        }
        if (revealListener != null) {
            revealListener.onRevealed(pos.method_10062());
        }
        CANDIDATES.remove(new Candidate(pos.method_10263() >> 4, pos.method_10260() >> 4, pos.method_10264() >> 4));
    }

    private static void add(Candidate candidate) {
        if (CANDIDATES.contains(candidate)) {
            return;
        }
        if (CANDIDATES.size() >= 512) {
            Iterator<Candidate> oldest = CANDIDATES.iterator();
            oldest.next();
            oldest.remove();
        }
        CANDIDATES.add(candidate);
        if (listener != null) {
            listener.onCandidate(candidate);
        }
    }

    private static void trackLevel(class_638 level) {
        class_5321<class_1937> dimension = level.method_27983();
        if (level == lastLevel.get() && dimension.equals(lastDimension)) {
            return;
        }
        CANDIDATES.clear();
        lastLevel = new WeakReference<>(level);
        lastDimension = dimension;
    }

    public static boolean isNether(class_638 level) {
        class_5321<class_1937> dimension = level.method_27983();
        return dimension == class_1937.field_25180 || DONUT_NETHER.equals(dimension) || level.method_40134().method_40225(class_7134.field_37667);
    }

    public static List<Candidate> snapshot() {
        return class_310.method_1551().field_1687 != lastLevel.get() ? List.of() : List.copyOf(CANDIDATES);
    }

    public static void setListener(CandidateListener candidateListener) {
        listener = candidateListener;
    }

    public static void setRevealListener(RevealListener debrisListener) {
        revealListener = debrisListener;
    }
}
