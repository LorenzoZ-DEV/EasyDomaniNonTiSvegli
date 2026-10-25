package net.anubis.client.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/event/Event.class */
@Environment(EnvType.CLIENT)
public abstract class Event {
    boolean pooledValid = true;

    public final void checkValid() {
    }
}
