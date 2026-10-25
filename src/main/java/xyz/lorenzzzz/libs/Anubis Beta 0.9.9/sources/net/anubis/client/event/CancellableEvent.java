package net.anubis.client.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/event/CancellableEvent.class */
@Environment(EnvType.CLIENT)
public abstract class CancellableEvent extends Event {
    boolean cancelled;
    boolean postStage;

    public final void setCancelled(boolean cancelled) {
        if (this.postStage) {
            return;
        }
        this.cancelled = cancelled;
    }

    public final boolean isCancelled() {
        return this.cancelled;
    }

    protected void resetCancellation() {
        this.cancelled = false;
        this.postStage = false;
    }
}
