package net.anubis.client.service.humanize;

import java.util.concurrent.ThreadLocalRandom;
import net.anubis.client.service.TickJitter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/service/humanize/HumanInventoryTiming.class */
@Environment(EnvType.CLIENT)
public final class HumanInventoryTiming {
    private HumanInventoryTiming() {
    }

    public static int openGuiDelayTicks() {
        return TickJitter.apply(4) + ThreadLocalRandom.current().nextInt(0, 3);
    }

    public static int closeGuiDelayTicks() {
        return TickJitter.apply(2) + ThreadLocalRandom.current().nextInt(0, 2);
    }
}
