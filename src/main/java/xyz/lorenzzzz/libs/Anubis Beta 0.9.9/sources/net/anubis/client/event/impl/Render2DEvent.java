package net.anubis.client.event.impl;

import net.anubis.client.event.Event;
import net.anubis.client.event.EventType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_332;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/event/impl/Render2DEvent.class */
@Environment(EnvType.CLIENT)
public final class Render2DEvent extends Event {
    public class_332 graphics;
    public float partialTick;
    public static final EventType<Render2DEvent> TYPE = new EventType<>("Render2DEvent");
    private static final Render2DEvent INSTANCE = new Render2DEvent();

    private Render2DEvent() {
    }

    public static void dispatch(class_332 graphics, float partialTick) {
        if (TYPE.hasPreListeners()) {
            INSTANCE.graphics = graphics;
            INSTANCE.partialTick = partialTick;
            TYPE.dispatch(INSTANCE);
            INSTANCE.graphics = null;
        }
    }
}
