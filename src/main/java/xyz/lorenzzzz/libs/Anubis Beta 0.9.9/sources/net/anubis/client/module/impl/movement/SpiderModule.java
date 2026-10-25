package net.anubis.client.module.impl.movement;

import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/movement/SpiderModule.class */
@Environment(EnvType.CLIENT)
public final class SpiderModule extends ClientModule {
    private static final double CLIMB_VELOCITY_THRESHOLD = 0.2d;
    private final NumberSetting climbSpeed;

    public SpiderModule() {
        super("spider", "Spider", "Allows you to climb walls like a spider.", ModuleCategory.MOVEMENT);
        this.climbSpeed = (NumberSetting) addSetting("climb-speed", new NumberSetting("Climb Speed", 0.2f, 0.0f, 10.0f).step(0.1f));
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        on(TickEvent.TYPE, Stage.POST, this::onPostTick);
    }

    private void onPostTick(TickEvent event) {
        class_746 player = class_310.method_1551().field_1724;
        if (player == null || !player.field_5976) {
            return;
        }
        class_243 velocity = player.method_18798();
        if (velocity.field_1351 >= CLIMB_VELOCITY_THRESHOLD) {
            return;
        }
        player.method_18800(velocity.field_1352, this.climbSpeed.get(), velocity.field_1350);
    }
}
