package net.anubis.client.module.impl.movement;

import java.util.concurrent.ThreadLocalRandom;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/movement/NoJumpDelayModule.class */
@Environment(EnvType.CLIENT)
public final class NoJumpDelayModule extends ClientModule {
    public static volatile NoJumpDelayModule active;
    private final NumberSetting cooldown;
    private final NumberSetting variance;

    public NoJumpDelayModule() {
        super("no-jump-delay", "No Jump Delay", "Removes the delay between jumps.", ModuleCategory.MOVEMENT);
        this.cooldown = (NumberSetting) addSetting("cooldown", new NumberSetting("Cooldown", 0.0f, 0.0f, 9.0f).step(1.0f));
        this.variance = (NumberSetting) addSetting("variance", new NumberSetting("Variance", 0.0f, 0.0f, 4.0f).step(1.0f));
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        active = this;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        active = null;
    }

    public int rearm(int vanillaTicks) {
        int ticks = this.cooldown.getInt();
        int spread = this.variance.getInt();
        if (spread > 0) {
            ticks += ThreadLocalRandom.current().nextInt(spread + 1);
        }
        return Math.min(vanillaTicks, ticks);
    }
}
