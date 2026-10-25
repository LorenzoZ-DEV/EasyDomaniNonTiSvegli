package net.anubis.client.event.impl;

import net.anubis.client.event.CancellableEvent;
import net.anubis.client.event.CancellableEventType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/event/impl/PlayerAttackEntityEvent.class */
@Environment(EnvType.CLIENT)
public final class PlayerAttackEntityEvent extends CancellableEvent {
    public class_1297 target;
    public static final CancellableEventType<PlayerAttackEntityEvent> TYPE = new CancellableEventType<>("PlayerAttackEntityEvent");
    private static final PlayerAttackEntityEvent INSTANCE = new PlayerAttackEntityEvent();

    private PlayerAttackEntityEvent() {
    }

    public static boolean dispatch(class_1297 target) {
        if (!TYPE.hasPreListeners()) {
            return false;
        }
        INSTANCE.target = target;
        INSTANCE.resetCancellation();
        TYPE.dispatch(INSTANCE);
        boolean cancelled = INSTANCE.isCancelled();
        INSTANCE.target = null;
        return cancelled;
    }
}
