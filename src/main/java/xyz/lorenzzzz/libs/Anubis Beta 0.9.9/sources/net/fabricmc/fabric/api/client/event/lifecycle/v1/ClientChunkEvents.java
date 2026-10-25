package net.fabricmc.fabric.api.client.event.lifecycle.v1;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_2818;
import net.minecraft.class_638;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/client/event/lifecycle/v1/ClientChunkEvents.class */
@Environment(EnvType.CLIENT)
public final class ClientChunkEvents {
    public static final Event<Load> CHUNK_LOAD = EventFactory.createArrayBacked(Load.class, callbacks -> {
        return (clientWorld, chunk) -> {
            for (Load callback : callbacks) {
                callback.onChunkLoad(clientWorld, chunk);
            }
        };
    });
    public static final Event<Unload> CHUNK_UNLOAD = EventFactory.createArrayBacked(Unload.class, callbacks -> {
        return (clientWorld, chunk) -> {
            for (Unload callback : callbacks) {
                callback.onChunkUnload(clientWorld, chunk);
            }
        };
    });

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/client/event/lifecycle/v1/ClientChunkEvents$Load.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface Load {
        void onChunkLoad(class_638 class_638Var, class_2818 class_2818Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/client/event/lifecycle/v1/ClientChunkEvents$Unload.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface Unload {
        void onChunkUnload(class_638 class_638Var, class_2818 class_2818Var);
    }

    private ClientChunkEvents() {
    }
}
