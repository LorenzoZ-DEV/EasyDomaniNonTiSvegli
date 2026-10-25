package net.anubis.client.event;

import java.util.function.Supplier;
import net.anubis.client.event.CancellableEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/event/PooledCancellableEventType.class */
@Environment(EnvType.CLIENT)
public class PooledCancellableEventType<E extends CancellableEvent> extends PooledEventType<E> {
    public PooledCancellableEventType(Supplier<E> factory, int capacity) {
        super(factory, capacity);
    }

    public PooledCancellableEventType(String name, Supplier<E> factory, int capacity) {
        super(name, factory, capacity);
    }

    @Override // net.anubis.client.event.EventType
    public void dispatchPre(E event) {
        CancellableEventType.dispatchPreCancellable(this.list, event);
    }

    @Override // net.anubis.client.event.EventType
    public void dispatchPost(E event) {
        CancellableEventType.dispatchPostGuarded(this.list, event);
    }
}
