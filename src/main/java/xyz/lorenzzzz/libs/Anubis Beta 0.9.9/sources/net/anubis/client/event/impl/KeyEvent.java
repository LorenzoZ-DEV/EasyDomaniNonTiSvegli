package net.anubis.client.event.impl;

import net.anubis.client.event.CancellableEvent;
import net.anubis.client.event.CancellableEventType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/event/impl/KeyEvent.class */
@Environment(EnvType.CLIENT)
public final class KeyEvent extends CancellableEvent {
    public int key;
    public int scancode;
    public int action;
    public int mods;
    public static final CancellableEventType<KeyEvent> TYPE = new CancellableEventType<>("KeyEvent");
    private static final KeyEvent INSTANCE = new KeyEvent();

    private KeyEvent() {
    }

    public static boolean dispatch(int key, int scancode, int action, int mods) {
        if (!TYPE.hasPreListeners()) {
            return false;
        }
        INSTANCE.key = key;
        INSTANCE.scancode = scancode;
        INSTANCE.action = action;
        INSTANCE.mods = mods;
        INSTANCE.resetCancellation();
        TYPE.dispatch(INSTANCE);
        return INSTANCE.isCancelled();
    }
}
