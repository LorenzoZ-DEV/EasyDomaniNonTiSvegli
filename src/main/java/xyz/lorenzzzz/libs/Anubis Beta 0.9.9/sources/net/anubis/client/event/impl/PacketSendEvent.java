package net.anubis.client.event.impl;

import net.anubis.client.event.CancellableEvent;
import net.anubis.client.event.PooledCancellableEventType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2596;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/event/impl/PacketSendEvent.class */
@Environment(EnvType.CLIENT)
public final class PacketSendEvent extends CancellableEvent {
    public static final PooledCancellableEventType<PacketSendEvent> TYPE = new PooledCancellableEventType<>("PacketSendEvent", PacketSendEvent::new, 1024);
    public class_2596<?> packet;

    private PacketSendEvent() {
    }

    public static boolean dispatchSync(class_2596<?> packet) {
        if (!TYPE.hasPreListeners()) {
            return false;
        }
        PacketSendEvent event = (PacketSendEvent) TYPE.pool.acquire();
        event.packet = packet;
        event.resetCancellation();
        try {
            TYPE.dispatch(event);
            return event.isCancelled();
        } finally {
            event.packet = null;
            TYPE.pool.release(event);
        }
    }
}
