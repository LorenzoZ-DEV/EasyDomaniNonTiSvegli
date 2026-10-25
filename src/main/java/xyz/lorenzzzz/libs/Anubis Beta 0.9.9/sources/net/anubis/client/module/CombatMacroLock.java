package net.anubis.client.module;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/CombatMacroLock.class */
@Environment(EnvType.CLIENT)
public final class CombatMacroLock {
    private static Object holder;

    private CombatMacroLock() {
    }

    public static boolean tryAcquire(Object owner) {
        Object obj = holder;
        if (obj instanceof ClientModule) {
            ClientModule module = (ClientModule) obj;
            if (!module.isEnabled()) {
                holder = null;
            }
        }
        if (holder == null || holder == owner) {
            holder = owner;
            return true;
        }
        return false;
    }

    public static void release(Object owner) {
        if (holder == owner) {
            holder = null;
        }
    }

    public static boolean heldByOther(Object owner) {
        Object obj = holder;
        if (obj instanceof ClientModule) {
            ClientModule module = (ClientModule) obj;
            if (!module.isEnabled()) {
                holder = null;
            }
        }
        return (holder == null || holder == owner) ? false : true;
    }
}
