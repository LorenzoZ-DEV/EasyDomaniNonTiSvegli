package com.example.client.mixin;

import net.anubis.client.module.impl.render.MotionBlurModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_757;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinMotionBlurGameRenderer.class */
@Mixin({class_757.class})
@Environment(EnvType.CLIENT)
public abstract class MixinMotionBlurGameRenderer {
    @Inject(method = {"method_3192"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_758;method_71108()V")})
    private void anubis$motionBlur(class_9779 deltaTracker, boolean renderLevel, CallbackInfo ci) {
        if (MotionBlurModule.active != null) {
            MotionBlurModule.afterWorld(renderLevel);
        }
    }
}
