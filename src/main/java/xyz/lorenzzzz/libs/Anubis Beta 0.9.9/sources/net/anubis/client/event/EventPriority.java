package net.anubis.client.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/event/EventPriority.class */
@Environment(EnvType.CLIENT)
public enum EventPriority {
    LOWEST(-100),
    LOW(-50),
    NORMAL(0),
    HIGH(50),
    HIGHEST(100);

    public final int value;

    EventPriority(int value) {
        this.value = value;
    }

    public static EventPriority fromInt(int v) {
        if (v >= 75) {
            return HIGHEST;
        }
        if (v >= 25) {
            return HIGH;
        }
        if (v >= -25) {
            return NORMAL;
        }
        return v >= -75 ? LOW : LOWEST;
    }
}
