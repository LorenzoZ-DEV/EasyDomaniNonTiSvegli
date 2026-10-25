package net.anubis.client.event.impl;

import net.anubis.client.event.CancellableEvent;
import net.anubis.client.event.CancellableEventType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/event/impl/MouseInputEvent.class */
@Environment(EnvType.CLIENT)
public final class MouseInputEvent extends CancellableEvent {
    public long window;
    public int button;
    public int action;
    public int mods;
    public static final CancellableEventType<MouseInputEvent> TYPE = new CancellableEventType<>("MouseInputEvent");
    private static final MouseInputEvent INSTANCE = new MouseInputEvent();

    private MouseInputEvent() {
    }

    public static boolean dispatch(long window, int button, int action, int mods) {
        if (!TYPE.hasPreListeners()) {
            return false;
        }
        INSTANCE.window = window;
        INSTANCE.button = button;
        INSTANCE.action = action;
        INSTANCE.mods = mods;
        INSTANCE.resetCancellation();
        TYPE.dispatch(INSTANCE);
        return INSTANCE.isCancelled();
    }
}
