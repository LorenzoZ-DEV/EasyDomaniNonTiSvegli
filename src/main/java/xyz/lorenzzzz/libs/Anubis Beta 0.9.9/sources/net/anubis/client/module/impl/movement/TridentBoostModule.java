package net.anubis.client.module.impl.movement;

import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/movement/TridentBoostModule.class */
@Environment(EnvType.CLIENT)
public final class TridentBoostModule extends ClientModule {
    public static volatile TridentBoostModule active;
    private final NumberSetting boost;
    private final BooleanSetting outOfWater;

    public TridentBoostModule() {
        super("trident-boost", "Trident Boost", "Boosts you when using riptide with a trident.", ModuleCategory.MOVEMENT);
        this.boost = (NumberSetting) addSetting("boost", new NumberSetting("Boost", 2.0f, 0.1f, 10.0f).step(0.1f));
        this.outOfWater = (BooleanSetting) addSetting("out-of-water", new BooleanSetting("Out Of Water", true));
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        active = this;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        active = null;
    }

    public static double riptideMultiplier() {
        TridentBoostModule module = active;
        if (module != null) {
            return module.boost.get();
        }
        return 1.0d;
    }

    public static boolean allowOutOfWater() {
        TridentBoostModule module = active;
        return module != null && module.outOfWater.get();
    }
}
