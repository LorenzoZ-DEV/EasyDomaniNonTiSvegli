package net.fabricmc.fabric.api.client.event.lifecycle.v1;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_2586;
import net.minecraft.class_638;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/client/event/lifecycle/v1/ClientBlockEntityEvents.class */
@Environment(EnvType.CLIENT)
public final class ClientBlockEntityEvents {
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

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/client/event/lifecycle/v1/ClientBlockEntityEvents$Load.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface Load {
        void onLoad(class_2586 class_2586Var, class_638 class_638Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/client/event/lifecycle/v1/ClientBlockEntityEvents$Unload.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface Unload {
        void onUnload(class_2586 class_2586Var, class_638 class_638Var);
    }

    private ClientBlockEntityEvents() {
    }
}
