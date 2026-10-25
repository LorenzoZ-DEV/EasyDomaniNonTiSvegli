package net.anubis.client.event.impl;

import net.anubis.client.event.Event;
import net.anubis.client.event.EventType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2596;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/event/impl/PacketAppliedEvent.class */
@Environment(EnvType.CLIENT)
public final class PacketAppliedEvent extends Event {
    public class_2596<?> packet;
    public static final EventType<PacketAppliedEvent> TYPE = new EventType<>("PacketAppliedEvent");
    private static final PacketAppliedEvent INSTANCE = new PacketAppliedEvent();

    private PacketAppliedEvent() {
    }

    public static void dispatch(class_2596<?> packet) {
        if (TYPE.hasPreListeners()) {
            INSTANCE.packet = packet;
            TYPE.dispatch(INSTANCE);
            INSTANCE.packet = null;
        }
    }
}
