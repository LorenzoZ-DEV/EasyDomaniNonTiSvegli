package net.fabricmc.fabric.api.client.event.lifecycle.v1;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_310;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/client/event/lifecycle/v1/ClientLifecycleEvents.class */
@Environment(EnvType.CLIENT)
public final class ClientLifecycleEvents {
    public static final Event<ClientStarted> CLIENT_STARTED = EventFactory.createArrayBacked(ClientStarted.class, callbacks -> {
        return client -> {
            for (ClientStarted callback : callbacks) {
                callback.onClientStarted(client);
            }
        };
    });
    public static final Event<ClientStopping> CLIENT_STOPPING = EventFactory.createArrayBacked(ClientStopping.class, callbacks -> {
        return client -> {
            for (ClientStopping callback : callbacks) {
                callback.onClientStopping(client);
            }
        };
    });

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/client/event/lifecycle/v1/ClientLifecycleEvents$ClientStarted.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface ClientStarted {
        void onClientStarted(class_310 class_310Var);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/client/event/lifecycle/v1/ClientLifecycleEvents$ClientStopping.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface ClientStopping {
        void onClientStopping(class_310 class_310Var);
    }

    private ClientLifecycleEvents() {
    }
}
