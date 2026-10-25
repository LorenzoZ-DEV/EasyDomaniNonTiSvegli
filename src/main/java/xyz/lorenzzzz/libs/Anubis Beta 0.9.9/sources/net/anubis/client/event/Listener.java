package net.anubis.client.event;

import net.anubis.client.event.Event;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/event/Listener.class */
@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface Listener<E extends Event> {
    void invoke(E e);
}
