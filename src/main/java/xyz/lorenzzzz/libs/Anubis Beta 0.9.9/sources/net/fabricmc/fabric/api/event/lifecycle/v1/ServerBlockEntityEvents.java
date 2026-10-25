package net.fabricmc.fabric.api.event.lifecycle.v1;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_2586;
import net.minecraft.class_3218;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerBlockEntityEvents.class */
public final class ServerBlockEntityEvents {
    public static final Event<Load> BLOCK_ENTITY_LOAD = EventFactory.createArrayBacked(Load.class, callbacks -> {
        return (blockEntity, world) -> {
            for (Load callback : callbacks) {
                callback.onLoad(blockEntity, world);
            }
        };
    });
    public static final Event<Unload> BLOCK_ENTITY_UNLOAD = EventFactory.createArrayBacked(Unload.class, callbacks -> {
        return (blockEntity, world) -> {
            for (Unload callback : callbacks) {
                callback.onUnload(blockEntity, world);
            }
        };
    });

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerBlockEntityEvents$Load.class */
    @FunctionalInterface
    public interface Load {
        void onLoad(class_2586 class_2586Var, class_3218 class_3218Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerBlockEntityEvents$Unload.class */
    @FunctionalInterface
    public interface Unload {
        void onUnload(class_2586 class_2586Var, class_3218 class_3218Var);
    }

    private ServerBlockEntityEvents() {
    }
}
