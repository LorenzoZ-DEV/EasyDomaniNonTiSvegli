package net.anubis.client.module.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/MotionBlurModule.class */
@Environment(EnvType.CLIENT)
public final class MotionBlurModule extends ClientModule {
    public static volatile MotionBlurModule active;
    private final NumberSetting strength;

    public MotionBlurModule() {
        super("motion-blur", "Motion Blur", "Adds motion blur when you turn.", ModuleCategory.VISUALS);
        this.strength = (NumberSetting) addSetting("strength", new NumberSetting("Strength", 50.0f, 10.0f, 90.0f).step(5.0f));
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        active = this;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        active = null;
        if (RenderSystem.isOnRenderThread()) {
            MotionBlurPass.release();
        } else {
            class_310.method_1551().execute(MotionBlurModule::releaseIfOff);
        }
    }

    private static void releaseIfOff() {
        if (active == null) {
            MotionBlurPass.release();
        }
    }

    public static void afterWorld(boolean worldRendered) {
        MotionBlurModule module = active;
        if (module != null) {
            MotionBlurPass.apply(worldRendered, module.strength.get() / 100.0f);
        }
    }
}
