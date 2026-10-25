package net.fabricmc.fabric.api.client.event.lifecycle.v1;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.class_310;
import net.minecraft.class_638;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/client/event/lifecycle/v1/ClientWorldEvents.class */
@Environment(EnvType.CLIENT)
public final class ClientWorldEvents {
    public static final Event<AfterClientWorldChange> AFTER_CLIENT_WORLD_CHANGE = EventFactory.createArrayBacked(AfterClientWorldChange.class, callbacks -> {
        return (client, world) -> {
            for (AfterClientWorldChange callback : callbacks) {
                callback.afterWorldChange(client, world);
            }
        };
    });

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/api/client/event/lifecycle/v1/ClientWorldEvents$AfterClientWorldChange.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface AfterClientWorldChange {
        void afterWorldChange(class_310 class_310Var, class_638 class_638Var);
    }

    private ClientWorldEvents() {
    }
}
