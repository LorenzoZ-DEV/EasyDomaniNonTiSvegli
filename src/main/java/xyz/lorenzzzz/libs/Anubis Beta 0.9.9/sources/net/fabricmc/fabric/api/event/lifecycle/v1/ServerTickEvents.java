package net.fabricmc.fabric.api.event.lifecycle.v1;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_3218;
import net.minecraft.server.MinecraftServer;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerTickEvents.class */
public final class ServerTickEvents {
    public static final Event<StartTick> START_SERVER_TICK = EventFactory.createArrayBacked(StartTick.class, callbacks -> {
        return server -> {
            for (StartTick event : callbacks) {
                event.onStartTick(server);
            }
        };
    });
    public static final Event<EndTick> END_SERVER_TICK = EventFactory.createArrayBacked(EndTick.class, callbacks -> {
        return server -> {
            for (EndTick event : callbacks) {
                event.onEndTick(server);
            }
        };
    });
    public static final Event<StartWorldTick> START_WORLD_TICK = EventFactory.createArrayBacked(StartWorldTick.class, callbacks -> {
        return world -> {
            for (StartWorldTick callback : callbacks) {
                callback.onStartTick(world);
            }
        };
    });
    public static final Event<EndWorldTick> END_WORLD_TICK = EventFactory.createArrayBacked(EndWorldTick.class, callbacks -> {
        return world -> {
            for (EndWorldTick callback : callbacks) {
                callback.onEndTick(world);
            }
        };
    });

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerTickEvents$EndTick.class */
    @FunctionalInterface
    public interface EndTick {
        void onEndTick(MinecraftServer minecraftServer);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerTickEvents$EndWorldTick.class */
    @FunctionalInterface
    public interface EndWorldTick {
        void onEndTick(class_3218 class_3218Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerTickEvents$StartTick.class */
    @FunctionalInterface
    public interface StartTick {
        void onStartTick(MinecraftServer minecraftServer);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/event/lifecycle/v1/ServerTickEvents$StartWorldTick.class */
    @FunctionalInterface
    public interface StartWorldTick {
        void onStartTick(class_3218 class_3218Var);
    }

    private ServerTickEvents() {
    }
}
