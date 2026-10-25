package net.fabricmc.fabric.api.event.lifecycle.v1;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_2818;
import net.minecraft.class_3194;
import net.minecraft.class_3218;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerChunkEvents.class */
public final class ServerChunkEvents {
    public static final Event<Load> CHUNK_LOAD = EventFactory.createArrayBacked(Load.class, callbacks -> {
        return (serverWorld, chunk) -> {
            for (Load callback : callbacks) {
                callback.onChunkLoad(serverWorld, chunk);
            }
        };
    });
    public static final Event<Generate> CHUNK_GENERATE = EventFactory.createArrayBacked(Generate.class, callbacks -> {
        return (serverWorld, chunk) -> {
            for (Generate callback : callbacks) {
                callback.onChunkGenerate(serverWorld, chunk);
            }
        };
    });
    public static final Event<Unload> CHUNK_UNLOAD = EventFactory.createArrayBacked(Unload.class, callbacks -> {
        return (serverWorld, chunk) -> {
            for (Unload callback : callbacks) {
                callback.onChunkUnload(serverWorld, chunk);
            }
        };
    });
    public static final Event<LevelTypeChange> CHUNK_LEVEL_TYPE_CHANGE = EventFactory.createArrayBacked(LevelTypeChange.class, (world, chunk, oldLevelType, newLevelType) -> {
    }, callbacks -> {
        return (serverWorld, chunk2, oldLevelType2, newLevelType2) -> {
            for (LevelTypeChange callback : callbacks) {
                callback.onChunkLevelTypeChange(serverWorld, chunk2, oldLevelType2, newLevelType2);
            }
        };
    });

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerChunkEvents$Generate.class */
    @FunctionalInterface
    public interface Generate {
        void onChunkGenerate(class_3218 class_3218Var, class_2818 class_2818Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerChunkEvents$LevelTypeChange.class */
    @FunctionalInterface
    public interface LevelTypeChange {
        void onChunkLevelTypeChange(class_3218 class_3218Var, class_2818 class_2818Var, class_3194 class_3194Var, class_3194 class_3194Var2);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerChunkEvents$Load.class */
    @FunctionalInterface
    public interface Load {
        void onChunkLoad(class_3218 class_3218Var, class_2818 class_2818Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerChunkEvents$Unload.class */
    @FunctionalInterface
    public interface Unload {
        void onChunkUnload(class_3218 class_3218Var, class_2818 class_2818Var);
    }

    private ServerChunkEvents() {
    }
}
