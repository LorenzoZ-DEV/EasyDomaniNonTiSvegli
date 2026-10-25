package com.example.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.anubis.client.module.impl.movement.FlyModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinFlyLocalPlayer.class */
@Mixin({class_746.class})
@Environment(EnvType.CLIENT)
public abstract class MixinFlyLocalPlayer {
    @ModifyReturnValue(method = {"method_5715"}, at = {@At("RETURN")})
    private boolean anubis$flyNoSneak(boolean original) {
        if (!original) {
            return false;
        }
        FlyModule fly = FlyModule.active;
        return fly == null || !fly.noSneak();
    }

    @ModifyExpressionValue(method = {"method_6007"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_1656;method_7252()F")})
    private float anubis$flyFreecamVertical(float original) {
        FlyModule fly = FlyModule.active;
        if (fly == null || !fly.suppressFlightVerticalInput()) {
            return original;
        }
        return 0.0f;
    }
}
