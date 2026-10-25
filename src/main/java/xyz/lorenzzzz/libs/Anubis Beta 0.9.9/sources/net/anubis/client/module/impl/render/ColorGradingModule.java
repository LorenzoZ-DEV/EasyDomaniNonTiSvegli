package net.anubis.client.module.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/ColorGradingModule.class */
@Environment(EnvType.CLIENT)
public final class ColorGradingModule extends ClientModule {
    public static volatile ColorGradingModule active;
    private final NumberSetting vibrance;
    private final NumberSetting contrast;
    private final NumberSetting brightness;
    private final NumberSetting warmth;
    private final NumberSetting vignette;

    public ColorGradingModule() {
        super("color-grading", "Color Grading", "Adds color, contrast and a vignette to the world.", ModuleCategory.VISUALS);
        this.vibrance = (NumberSetting) addSetting("vibrance", new NumberSetting("Vibrance", 35.0f, 0.0f, 100.0f).step(1.0f));
        this.contrast = (NumberSetting) addSetting("contrast", new NumberSetting("Contrast", 8.0f, -20.0f, 40.0f).step(1.0f));
        this.brightness = (NumberSetting) addSetting("brightness", new NumberSetting("Brightness", 3.0f, -20.0f, 30.0f).step(1.0f));
        this.warmth = (NumberSetting) addSetting("warmth", new NumberSetting("Warmth", 15.0f, -100.0f, 100.0f).step(1.0f));
        this.vignette = (NumberSetting) addSetting("vignette", new NumberSetting("Vignette", 30.0f, 0.0f, 100.0f).step(1.0f));
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        active = this;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        active = null;
        if (RenderSystem.isOnRenderThread()) {
            ColorGradingPass.release();
        } else {
            class_310.method_1551().execute(ColorGradingModule::releaseIfOff);
        }
    }

    private static void releaseIfOff() {
        if (active == null) {
            ColorGradingPass.release();
        }
    }

    public static void afterWorld(boolean worldRendered) {
        ColorGradingModule module = active;
        if (module == null) {
            return;
        }
        ColorGradingPass.apply(worldRendered, module.vibrance.get() / 100.0f, 1.0f + (module.contrast.get() / 100.0f), 1.0f + (module.brightness.get() / 100.0f), module.warmth.get() / 100.0f, module.vignette.get() / 100.0f);
    }
}
