package com.example.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.anubis.client.module.impl.render.CustomFovModule;
import net.anubis.client.module.impl.render.ZoomModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_757;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinCustomFovGameRenderer.class */
@Mixin({class_757.class})
@Environment(EnvType.CLIENT)
public abstract class MixinCustomFovGameRenderer {
    @Shadow
    public abstract boolean method_35765();

    @ModifyReturnValue(method = {"method_3196"}, at = {@At("RETURN")})
    private float anubis$customFov(float original, @Local(argsOnly = true) boolean useFovSetting) {
        CustomFovModule module = CustomFovModule.active;
        float fov = (module == null || method_35765()) ? original : module.fov();
        if (!useFovSetting) {
            CustomFovModule.hudFovApplied(original, fov);
        }
        return (!useFovSetting || method_35765()) ? fov : ZoomModule.fov(fov);
    }
}
