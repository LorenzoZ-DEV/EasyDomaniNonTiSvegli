package net.anubis.client.util.quantum;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.LongPredicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2248;
import net.minecraft.class_2680;
import net.minecraft.class_2841;
import net.minecraft.class_6563;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/quantum/ChunkSnapshotStore.class */
@Environment(EnvType.CLIENT)
public final class ChunkSnapshotStore {
    public static final int MIN_SECTION_INDEX = 6;
    public static final int MAX_SECTION_INDEX = 23;
    private static final class_6563<class_2680> STRATEGY = class_6563.method_74162(class_2248.field_10651);
    private final Map<Long, class_2841<class_2680>[]> snapshots = new HashMap();
    private final class_2680 air;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/quantum/ChunkSnapshotStore$BlockChange.class */
    @Environment(EnvType.CLIENT)
    public static final class BlockChange extends Record {
        private final int worldX;
        private final int worldY;
        private final int worldZ;
        private final class_2680 newState;
        private final class_2680 oldState;

        public BlockChange(int worldX, int worldY, int worldZ, class_2680 newState, class_2680 oldState) {
            this.worldX = worldX;
            this.worldY = worldY;
            this.worldZ = worldZ;
            this.newState = newState;
            this.oldState = oldState;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, BlockChange.class), BlockChange.class, "worldX;worldY;worldZ;newState;oldState", "FIELD:Lnet/anubis/client/util/quantum/ChunkSnapshotStore$BlockChange;->worldX:I", "FIELD:Lnet/anubis/client/util/quantum/ChunkSnapshotStore$BlockChange;->worldY:I", "FIELD:Lnet/anubis/client/util/quantum/ChunkSnapshotStore$BlockChange;->worldZ:I", "FIELD:Lnet/anubis/client/util/quantum/ChunkSnapshotStore$BlockChange;->newState:Lnet/minecraft/class_2680;", "FIELD:Lnet/anubis/client/util/quantum/ChunkSnapshotStore$BlockChange;->oldState:Lnet/minecraft/class_2680;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, BlockChange.class), BlockChange.class, "worldX;worldY;worldZ;newState;oldState", "FIELD:Lnet/anubis/client/util/quantum/ChunkSnapshotStore$BlockChange;->worldX:I", "FIELD:Lnet/anubis/client/util/quantum/ChunkSnapshotStore$BlockChange;->worldY:I", "FIELD:Lnet/anubis/client/util/quantum/ChunkSnapshotStore$BlockChange;->worldZ:I", "FIELD:Lnet/anubis/client/util/quantum/ChunkSnapshotStore$BlockChange;->newState:Lnet/minecraft/class_2680;", "FIELD:Lnet/anubis/client/util/quantum/ChunkSnapshotStore$BlockChange;->oldState:Lnet/minecraft/class_2680;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, BlockChange.class, Object.class), BlockChange.class, "worldX;worldY;worldZ;newState;oldState", "FIELD:Lnet/anubis/client/util/quantum/ChunkSnapshotStore$BlockChange;->worldX:I", "FIELD:Lnet/anubis/client/util/quantum/ChunkSnapshotStore$BlockChange;->worldY:I", "FIELD:Lnet/anubis/client/util/quantum/ChunkSnapshotStore$BlockChange;->worldZ:I", "FIELD:Lnet/anubis/client/util/quantum/ChunkSnapshotStore$BlockChange;->newState:Lnet/minecraft/class_2680;", "FIELD:Lnet/anubis/client/util/quantum/ChunkSnapshotStore$BlockChange;->oldState:Lnet/minecraft/class_2680;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int worldX() {
            return this.worldX;
        }

        public int worldY() {
            return this.worldY;
        }

        public int worldZ() {
            return this.worldZ;
        }

        public class_2680 newState() {
            return this.newState;
        }

        public class_2680 oldState() {
            return this.oldState;
        }
    }

    public ChunkSnapshotStore(class_2680 air) {
        this.air = air;
    }

    public static long chunkKey(int chunkX, int chunkZ) {
        return (((long) chunkX) << 32) | (((long) chunkZ) & 4294967295L);
    }

    public void store(int chunkX, int chunkZ, class_2841<class_2680>[] sections) {
        this.snapshots.put(Long.valueOf(chunkKey(chunkX, chunkZ)), sections);
    }

    public void retain(LongPredicate keep) {
        this.snapshots.keySet().removeIf(key -> {
            return !keep.test(key.longValue());
        });
    }

    public void clear() {
        this.snapshots.clear();
    }

    public void update(int chunkX, int chunkZ, int sectionIndex, int localX, int localY, int localZ, class_2680 state) {
        class_2841<class_2680>[] sections;
        if (sectionIndex < 6 || sectionIndex > 23 || (sections = this.snapshots.get(Long.valueOf(chunkKey(chunkX, chunkZ)))) == null || sectionIndex >= sections.length) {
            return;
        }
        class_2841<class_2680> section = sections[sectionIndex];
        if (section == null) {
            if (state == this.air) {
                return;
            }
            section = new class_2841<>(this.air, STRATEGY);
            sections[sectionIndex] = section;
        }
        section.method_35321(localX, localY, localZ, state);
    }

    public List<BlockChange> compare(int chunkX, int chunkZ, class_2841<class_2680>[] current, int minSectionY) {
        List<BlockChange> changes = new ArrayList<>();
        class_2841<class_2680>[] old = this.snapshots.get(Long.valueOf(chunkKey(chunkX, chunkZ)));
        if (isAllAir(old)) {
            return changes;
        }
        int index = 6;
        while (index <= 23) {
            class_2841<class_2680> before = index < old.length ? old[index] : null;
            class_2841<class_2680> after = index < current.length ? current[index] : null;
            if (before != null || after != null) {
                int baseY = (index + minSectionY) * 16;
                for (int y = 0; y < 16; y++) {
                    for (int z = 0; z < 16; z++) {
                        for (int x = 0; x < 16; x++) {
                            class_2680 oldState = before != null ? (class_2680) before.method_12321(x, y, z) : this.air;
                            class_2680 newState = after != null ? (class_2680) after.method_12321(x, y, z) : this.air;
                            if (oldState != newState) {
                                changes.add(new BlockChange((chunkX * 16) + x, baseY + y, (chunkZ * 16) + z, newState, oldState));
                            }
                        }
                    }
                }
            }
            index++;
        }
        return changes;
    }

    private boolean isAllAir(class_2841<class_2680>[] sections) {
        if (sections == null) {
            return true;
        }
        for (int index = 6; index <= 23 && index < sections.length; index++) {
            if (sections[index] != null) {
                return false;
            }
        }
        return true;
    }
}
