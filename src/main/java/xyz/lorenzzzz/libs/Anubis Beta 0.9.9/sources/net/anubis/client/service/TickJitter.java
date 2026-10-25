package net.anubis.client.service;

import java.util.concurrent.ThreadLocalRandom;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/service/TickJitter.class */
@Environment(EnvType.CLIENT)
public final class TickJitter {
    private static final float VARIANCE = 0.2f;

    private TickJitter() {
    }

    public static int apply(int baseTicks) {
        if (baseTicks <= 0) {
            return baseTicks;
        }
        int span = Math.max(1, Math.round(baseTicks * VARIANCE));
        int delta = ThreadLocalRandom.current().nextInt(-span, span + 1);
        return Math.max(1, baseTicks + delta);
    }
}
