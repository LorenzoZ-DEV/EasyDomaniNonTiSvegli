package net.anubis.client.module.impl.combat;

import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.PacketAppliedEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.util.pvp.InputControl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_243;
import net.minecraft.class_2743;
import net.minecraft.class_310;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/JumpResetModule.class */
@Environment(EnvType.CLIENT)
public final class JumpResetModule extends ClientModule {
    private static final double MIN_KNOCKBACK = 0.1d;
    private boolean pending;

    public JumpResetModule() {
        super("jump-reset", "Jump Reset", "Jumps the tick knockback lands to take less of it.", ModuleCategory.COMBAT);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        this.pending = false;
    }

    @EventHandler
    public void onPacketApplied(PacketAppliedEvent event) {
        class_2743 class_2743Var = event.packet;
        if (class_2743Var instanceof class_2743) {
            class_2743 motion = class_2743Var;
            class_746 player = class_310.method_1551().field_1724;
            if (player == null || motion.method_11818() != player.method_5628()) {
                return;
            }
            class_243 knockback = motion.method_73085();
            if (knockback.method_37267() < MIN_KNOCKBACK || knockback.field_1351 <= 0.0d) {
                return;
            }
            this.pending = true;
        }
    }

    @EventHandler
    public void onTick(TickEvent event) {
        if (this.pending) {
            this.pending = false;
            class_310 mc = class_310.method_1551();
            class_746 player = mc.field_1724;
            if (player == null || mc.field_1755 != null || !player.method_24828() || !player.method_5624() || !player.field_3913.field_54155.comp_3159() || player.method_5799() || player.method_5771() || player.method_6101() || player.method_5765() || player.method_6128()) {
                return;
            }
            InputControl.jump();
        }
    }
}
