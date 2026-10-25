package net.fabricmc.fabric.api.event;

import net.minecraft.class_2960;
import org.jetbrains.annotations.ApiStatus;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-api-base-1.0.5+4ebb5c083e.jar:net/fabricmc/fabric/api/event/Event.class */
@ApiStatus.NonExtendable
public abstract class Event<T> {
    protected volatile T invoker;
    public static final class_2960 DEFAULT_PHASE = class_2960.method_60655("fabric", "default");

    public abstract void register(T t);

    public final T invoker() {
        return this.invoker;
    }

    public void register(class_2960 phase, T listener) {
        register(listener);
    }

    public void addPhaseOrdering(class_2960 firstPhase, class_2960 secondPhase) {
    }
}
