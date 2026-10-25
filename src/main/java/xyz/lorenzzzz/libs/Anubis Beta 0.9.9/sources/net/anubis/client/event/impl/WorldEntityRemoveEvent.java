package net.anubis.client.event.impl;

import net.anubis.client.event.Event;
import net.anubis.client.event.EventType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/event/impl/WorldEntityRemoveEvent.class */
@Environment(EnvType.CLIENT)
public final class WorldEntityRemoveEvent extends Event {
    public class_1297 entity;
    public static final EventType<WorldEntityRemoveEvent> TYPE = new EventType<>("WorldEntityRemoveEvent");
    private static final WorldEntityRemoveEvent INSTANCE = new WorldEntityRemoveEvent();

    private WorldEntityRemoveEvent() {
    }

    public static void dispatch(class_1297 entity) {
        if (TYPE.hasPreListeners()) {
            INSTANCE.entity = entity;
            TYPE.dispatch(INSTANCE);
            INSTANCE.entity = null;
        }
    }
}
