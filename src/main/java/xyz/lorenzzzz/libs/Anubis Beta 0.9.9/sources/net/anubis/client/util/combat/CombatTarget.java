package net.anubis.client.util.combat;

import net.anubis.client.event.EventPriority;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.PlayerAttackEntityEvent;
import net.anubis.client.event.impl.TickEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1309;
import net.minecraft.class_156;
import net.minecraft.class_310;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/combat/CombatTarget.class */
@Environment(EnvType.CLIENT)
public final class CombatTarget {
    private static final Object OWNER = new Object();
    private static final long MEMORY_CAP_MS = 30000;
    private static class_1309 lastHit;
    private static long lastHitAt;

    private CombatTarget() {
    }

    public static void install() {
        PlayerAttackEntityEvent.TYPE.listen(OWNER, Stage.PRE, EventPriority.LOWEST, event -> {
            if (event.isCancelled()) {
                return;
            }
            class_1309 class_1309Var = event.target;
            if (class_1309Var instanceof class_1309) {
                class_1309 living = class_1309Var;
                class_310 mc = class_310.method_1551();
                if (mc.method_18854() && living.method_73183() == mc.field_1687) {
                    lastHit = living;
                    lastHitAt = class_156.method_658();
                }
            }
        });
        TickEvent.TYPE.listen(OWNER, event2 -> {
            forgetStale();
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void forgetStale() {
        class_1309 entity = lastHit;
        if (entity == null) {
            return;
        }
        if (!entity.method_5805() || entity.method_73183() != class_310.method_1551().field_1687 || class_156.method_658() - lastHitAt > MEMORY_CAP_MS) {
            lastHit = null;
        }
    }

    public static class_1309 lastHit(long timeoutMs) {
        class_1309 entity = lastHit;
        if (entity == null || !entity.method_5805() || entity.method_73183() != class_310.method_1551().field_1687 || class_156.method_658() - lastHitAt > timeoutMs) {
            return null;
        }
        return entity;
    }

    public static class_1309 current(long timeoutMs) {
        class_1309 hit = lastHit(timeoutMs);
        if (hit != null) {
            return hit;
        }
        class_1309 class_1309Var = class_310.method_1551().field_1692;
        if (class_1309Var instanceof class_1309) {
            class_1309 living = class_1309Var;
            if (living.method_5805()) {
                return living;
            }
        }
        return null;
    }
}
