package net.anubis.client.module.impl.combat;

import net.anubis.client.chat.ModuleChat;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.impl.combat.melee.MeleeAttacks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_310;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/FriendGuardModule.class */
@Environment(EnvType.CLIENT)
public final class FriendGuardModule extends ClientModule {
    private static final long HINT_COOLDOWN_MS = 1500;
    private static volatile FriendGuardModule active;
    private long lastHintAt;

    public FriendGuardModule() {
        super("friend-guard", "Friend Guard", "Stops you from hitting players on your friends list.", ModuleCategory.COMBAT);
        usesNotifications();
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        this.lastHintAt = 0L;
        active = this;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        active = null;
        this.lastHintAt = 0L;
    }

    public static boolean intercepts(class_1657 attacker, class_1297 target) {
        class_1657 friend;
        FriendGuardModule guard = active;
        if (guard == null || !(target instanceof class_1657) || (friend = (class_1657) target) == attacker || attacker != class_310.method_1551().field_1724 || !MeleeAttacks.isFriend(friend)) {
            return false;
        }
        guard.hint(friend);
        return true;
    }

    public static boolean interceptsStab(class_746 player) {
        class_1297 friend;
        FriendGuardModule guard = active;
        if (guard == null || player == null || (friend = MeleeAttacks.stabTarget(player, entity -> {
            if (entity instanceof class_1657) {
                class_1657 other = (class_1657) entity;
                if (MeleeAttacks.isFriend(other)) {
                    return true;
                }
            }
            return false;
        })) == null) {
            return false;
        }
        guard.hint((class_1657) friend);
        return true;
    }

    private void hint(class_1657 friend) {
        if (notificationsOn()) {
            long now = System.currentTimeMillis();
            if (now - this.lastHintAt < HINT_COOLDOWN_MS) {
                return;
            }
            this.lastHintAt = now;
            ModuleChat.actionBar(this, ModuleChat.body().text("Held back your hit on ").name(friend.method_7334().name()));
        }
    }
}
