package net.fabricmc.fabric.api.client.event.lifecycle.v1;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_310;
import net.minecraft.class_638;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/client/event/lifecycle/v1/ClientTickEvents.class */
@Environment(EnvType.CLIENT)
public final class ClientTickEvents {
    public static final Event<StartTick> START_CLIENT_TICK = EventFactory.createArrayBacked(StartTick.class, callbacks -> {
        return client -> {
            for (StartTick event : callbacks) {
                event.onStartTick(client);
            }
        };
    });
    public static final Event<EndTick> END_CLIENT_TICK = EventFactory.createArrayBacked(EndTick.class, callbacks -> {
        return client -> {
            for (EndTick event : callbacks) {
                event.onEndTick(client);
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

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/client/event/lifecycle/v1/ClientTickEvents$EndTick.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface EndTick {
        void onEndTick(class_310 class_310Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/client/event/lifecycle/v1/ClientTickEvents$EndWorldTick.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface EndWorldTick {
        void onEndTick(class_638 class_638Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/client/event/lifecycle/v1/ClientTickEvents$StartTick.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface StartTick {
        void onStartTick(class_310 class_310Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/client/event/lifecycle/v1/ClientTickEvents$StartWorldTick.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface StartWorldTick {
        void onStartTick(class_638 class_638Var);
    }

    private ClientTickEvents() {
    }
}
