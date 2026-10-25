package net.anubis.client.module.impl.misc;

import java.util.Objects;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2246;
import net.minecraft.class_2680;
import net.minecraft.class_3481;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/SpeedMineModule.class */
@Environment(EnvType.CLIENT)
public final class SpeedMineModule extends ClientModule {
    private static volatile SpeedMineModule active;
    private final NumberSetting breakAt;
    private final BooleanSetting skipDelay;
    private final BooleanSetting keepInstantDelay;

    public SpeedMineModule() {
        super("speed-mine", "Donut Speed Mine", "Finishes breaking blocks before the full break time.", ModuleCategory.DONUT);
        this.breakAt = (NumberSetting) addSetting("break-at", new NumberSetting("Break At", 0.7f, 0.3f, 1.0f).step(0.01f));
        this.skipDelay = (BooleanSetting) addSetting("skip-delay", new BooleanSetting("Skip Break Delay", true));
        this.keepInstantDelay = (BooleanSetting) addSetting("keep-instant-delay", new BooleanSetting("Keep Instant Delay", true));
        BooleanSetting booleanSetting = this.keepInstantDelay;
        BooleanSetting booleanSetting2 = this.skipDelay;
        Objects.requireNonNull(booleanSetting2);
        booleanSetting.visibleWhen(booleanSetting2::get);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        active = this;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        active = null;
    }

    public static float scaleProgress(class_2680 state, float progress) {
        SpeedMineModule module = active;
        return (module == null || !affects(state)) ? progress : progress / module.breakAt.get();
    }

    public static boolean dropsDelay(class_2680 state, float progress) {
        SpeedMineModule module = active;
        if (module != null && module.skipDelay.get() && affects(state)) {
            return !module.keepInstantDelay.get() || progress < 1.0f;
        }
        return false;
    }

    private static boolean affects(class_2680 state) {
        if (state.method_26215() || state.method_27852(class_2246.field_10260)) {
            return false;
        }
        if (state.method_27852(class_2246.field_10255)) {
            return true;
        }
        return (state.method_26164(class_3481.field_33716) || state.method_26164(class_3481.field_33713)) ? false : true;
    }
}
