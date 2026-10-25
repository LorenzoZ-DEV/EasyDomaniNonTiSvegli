package net.anubis.client.event;

import java.util.function.Consumer;
import net.anubis.client.event.CancellableEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/event/CancellableEventType.class */
@Environment(EnvType.CLIENT)
public class CancellableEventType<E extends CancellableEvent> extends EventType<E> {
    public CancellableEventType() {
    }

    public CancellableEventType(String name) {
        super(name);
    }

    @Override // net.anubis.client.event.EventType
    public void dispatchPre(E event) {
        dispatchPreCancellable(this.list, event);
    }

    @Override // net.anubis.client.event.EventType
    public void dispatchPost(E event) {
        dispatchPostGuarded(this.list, event);
    }

    static <E extends CancellableEvent> void dispatchPreCancellable(ListenerList<E> list, E event) {
        Consumer[] consumerArrPreCallbacks = list.preCallbacks();
        for (int i = 0; i < consumerArrPreCallbacks.length; i++) {
            try {
                consumerArrPreCallbacks[i].accept(event);
            } catch (Exception ex) {
                ListenerErrorReporter.report(list.handleOf(Stage.PRE, consumerArrPreCallbacks[i]), event, ex);
            }
            if (event.cancelled) {
                return;
            }
        }
    }

    static <E extends CancellableEvent> void dispatchPostGuarded(ListenerList<E> list, E event) {
        event.postStage = true;
        try {
            list.dispatchPost(event);
        } finally {
            event.postStage = false;
        }
    }
}
