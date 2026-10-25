package net.anubis.client.module.impl.misc;

import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1268;
import net.minecraft.class_310;
import net.minecraft.class_746;
import org.lwjgl.glfw.GLFW;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/RiptideModule.class */
@Environment(EnvType.CLIENT)
public final class RiptideModule extends ClientModule {
    private int releasedTick;

    public RiptideModule() {
        super("riptide", "Riptide", "Uses a riptide trident while you hold right-click.", ModuleCategory.DONUT);
        this.releasedTick = -100;
    }

    @EventHandler
    public void onTick(TickEvent event) {
        class_1268 hand;
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (player == null || mc.field_1761 == null || mc.field_1755 != null || GLFW.glfwGetMouseButton(mc.method_22683().method_4490(), 1) != 1 || !GrimRiptide.clientAccepts() || (hand = hand(player)) == null) {
            return;
        }
        if (player.method_6115()) {
            if (GrimRiptide.isRiptide(player.method_6030()) && player.method_6048() >= chargeTicks() && player.field_6012 - this.releasedTick >= 9) {
                mc.field_1761.method_2897(player);
                this.releasedTick = player.field_6012;
                return;
            }
            return;
        }
        if (player.field_6012 - this.releasedTick < 9) {
            return;
        }
        mc.field_1761.method_2919(player, hand);
    }

    private static int chargeTicks() {
        return Math.max(9, TridentUtilModule.minChargeTicks(10));
    }

    private static class_1268 hand(class_746 player) {
        if (GrimRiptide.isRiptide(player.method_6047())) {
            return class_1268.field_5808;
        }
        if (GrimRiptide.isRiptide(player.method_6079())) {
            return class_1268.field_5810;
        }
        return null;
    }
}
