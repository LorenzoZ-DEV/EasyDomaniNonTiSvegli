package net.fabricmc.fabric.api.client.event.lifecycle.v1;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_1297;
import net.minecraft.class_638;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/client/event/lifecycle/v1/ClientEntityEvents.class */
@Environment(EnvType.CLIENT)
public final class ClientEntityEvents {
    public static final Event<Load> ENTITY_LOAD = EventFactory.createArrayBacked(Load.class, callbacks -> {
        return (entity, world) -> {
            for (Load callback : callbacks) {
                callback.onLoad(entity, world);
            }
        };
    });
    public static final Event<Unload> ENTITY_UNLOAD = EventFactory.createArrayBacked(Unload.class, callbacks -> {
        return (entity, world) -> {
            for (Unload callback : callbacks) {
                callback.onUnload(entity, world);
            }
        };
    });

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/client/event/lifecycle/v1/ClientEntityEvents$Load.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface Load {
        void onLoad(class_1297 class_1297Var, class_638 class_638Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/client/event/lifecycle/v1/ClientEntityEvents$Unload.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface Unload {
        void onUnload(class_1297 class_1297Var, class_638 class_638Var);
    }

    private ClientEntityEvents() {
    }
}
