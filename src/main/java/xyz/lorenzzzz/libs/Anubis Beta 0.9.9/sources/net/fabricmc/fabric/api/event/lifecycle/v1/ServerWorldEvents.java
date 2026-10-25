package net.fabricmc.fabric.api.event.lifecycle.v1;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_3218;
import net.minecraft.server.MinecraftServer;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerWorldEvents.class */
public final class ServerWorldEvents {
    public static final Event<Load> LOAD = EventFactory.createArrayBacked(Load.class, callbacks -> {
        return (server, world) -> {
            for (Load callback : callbacks) {
                callback.onWorldLoad(server, world);
            }
        };
    });
    public static final Event<Unload> UNLOAD = EventFactory.createArrayBacked(Unload.class, callbacks -> {
        return (server, world) -> {
            for (Unload callback : callbacks) {
                callback.onWorldUnload(server, world);
            }
        };
    });

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerWorldEvents$Load.class */
    @FunctionalInterface
    public interface Load {
        void onWorldLoad(MinecraftServer minecraftServer, class_3218 class_3218Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerWorldEvents$Unload.class */
    @FunctionalInterface
    public interface Unload {
        void onWorldUnload(MinecraftServer minecraftServer, class_3218 class_3218Var);
    }

    private ServerWorldEvents() {
    }
}
