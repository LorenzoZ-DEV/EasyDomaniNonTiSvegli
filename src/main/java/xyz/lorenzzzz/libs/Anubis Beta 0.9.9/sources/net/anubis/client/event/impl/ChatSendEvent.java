package net.anubis.client.event.impl;

import net.anubis.client.event.CancellableEvent;
import net.anubis.client.event.CancellableEventType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/event/impl/ChatSendEvent.class */
@Environment(EnvType.CLIENT)
public final class ChatSendEvent extends CancellableEvent {
    public String message;
    public static final CancellableEventType<ChatSendEvent> TYPE = new CancellableEventType<>("ChatSendEvent");
    private static final ChatSendEvent INSTANCE = new ChatSendEvent();

    private ChatSendEvent() {
    }

    public static boolean dispatch(String message) {
        if (!TYPE.hasPreListeners()) {
            return false;
        }
        INSTANCE.message = message;
        INSTANCE.resetCancellation();
        TYPE.dispatch(INSTANCE);
        boolean cancelled = INSTANCE.isCancelled();
        INSTANCE.message = null;
        return cancelled;
    }
}
