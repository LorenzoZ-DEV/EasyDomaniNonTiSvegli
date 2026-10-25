package net.anubis.client.event;

import java.util.function.Supplier;
import net.anubis.client.event.Event;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/event/PooledEventType.class */
@Environment(EnvType.CLIENT)
public class PooledEventType<E extends Event> extends EventType<E> {
    public final EventPool<E> pool;

    public PooledEventType(Supplier<E> factory, int capacity) {
        this.pool = new EventPool<>(factory, capacity);
    }

    public PooledEventType(String name, Supplier<E> factory, int capacity) {
        super(name);
        this.pool = new EventPool<>(factory, capacity);
    }
}
