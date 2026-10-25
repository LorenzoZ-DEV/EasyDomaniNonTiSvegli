package net.anubis.client.module.impl.misc;

import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/TridentUtilModule.class */
@Environment(EnvType.CLIENT)
public final class TridentUtilModule extends ClientModule {
    private final NumberSetting chargeScale;
    private final BooleanSetting noWater;

    public TridentUtilModule() {
        super("trident-util", "Trident Util", "Use tridents out of water and charge them faster.", ModuleCategory.DONUT);
        this.chargeScale = (NumberSetting) addSetting("scale", new NumberSetting("Charge Scale", 0.0f, 0.0f, 1.0f).step(0.1f));
        this.noWater = (BooleanSetting) addSetting("no-water", new BooleanSetting("No Water", true));
    }

    private static TridentUtilModule active() {
        TridentUtilModule module = (TridentUtilModule) ModuleRegistry.get(TridentUtilModule.class);
        if (module == null || !module.isEnabled()) {
            return null;
        }
        return module;
    }

    public static boolean allowOutOfWater() {
        TridentUtilModule module = active();
        return module != null && module.noWater.get();
    }

    public static int minChargeTicks(int vanilla) {
        TridentUtilModule module = active();
        return module == null ? vanilla : Math.max(1, Math.round(vanilla * module.chargeScale.get()));
    }
}
