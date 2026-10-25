package net.anubis.client.event;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.function.Consumer;
import net.anubis.client.event.Event;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/event/ListenerHandle.class */
@Environment(EnvType.CLIENT)
public final class ListenerHandle<E extends Event> extends Record {
    private final EventType<E> type;
    private final Stage stage;
    private final EventPriority priority;
    private final Object owner;
    private final Consumer<E> callback;

    public ListenerHandle(EventType<E> type, Stage stage, EventPriority priority, Object owner, Consumer<E> callback) {
        this.type = type;
        this.stage = stage;
        this.priority = priority;
        this.owner = owner;
        this.callback = callback;
    }

    @Override // java.lang.Record
    public final String toString() {
        return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, ListenerHandle.class), ListenerHandle.class, "type;stage;priority;owner;callback", "FIELD:Lnet/anubis/client/event/ListenerHandle;->type:Lnet/anubis/client/event/EventType;", "FIELD:Lnet/anubis/client/event/ListenerHandle;->stage:Lnet/anubis/client/event/Stage;", "FIELD:Lnet/anubis/client/event/ListenerHandle;->priority:Lnet/anubis/client/event/EventPriority;", "FIELD:Lnet/anubis/client/event/ListenerHandle;->owner:Ljava/lang/Object;", "FIELD:Lnet/anubis/client/event/ListenerHandle;->callback:Ljava/util/function/Consumer;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, ListenerHandle.class), ListenerHandle.class, "type;stage;priority;owner;callback", "FIELD:Lnet/anubis/client/event/ListenerHandle;->type:Lnet/anubis/client/event/EventType;", "FIELD:Lnet/anubis/client/event/ListenerHandle;->stage:Lnet/anubis/client/event/Stage;", "FIELD:Lnet/anubis/client/event/ListenerHandle;->priority:Lnet/anubis/client/event/EventPriority;", "FIELD:Lnet/anubis/client/event/ListenerHandle;->owner:Ljava/lang/Object;", "FIELD:Lnet/anubis/client/event/ListenerHandle;->callback:Ljava/util/function/Consumer;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final boolean equals(Object o) {
        return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, ListenerHandle.class, Object.class), ListenerHandle.class, "type;stage;priority;owner;callback", "FIELD:Lnet/anubis/client/event/ListenerHandle;->type:Lnet/anubis/client/event/EventType;", "FIELD:Lnet/anubis/client/event/ListenerHandle;->stage:Lnet/anubis/client/event/Stage;", "FIELD:Lnet/anubis/client/event/ListenerHandle;->priority:Lnet/anubis/client/event/EventPriority;", "FIELD:Lnet/anubis/client/event/ListenerHandle;->owner:Ljava/lang/Object;", "FIELD:Lnet/anubis/client/event/ListenerHandle;->callback:Ljava/util/function/Consumer;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
    }

    public EventType<E> type() {
        return this.type;
    }

    public Stage stage() {
        return this.stage;
    }

    public EventPriority priority() {
        return this.priority;
    }

    public Object owner() {
        return this.owner;
    }

    public Consumer<E> callback() {
        return this.callback;
    }

    public void unregister() {
        this.type.unregister(this);
    }
}
