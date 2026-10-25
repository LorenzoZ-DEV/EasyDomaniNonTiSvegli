package com.example.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.anubis.client.event.impl.PlayerAttackEntityEvent;
import net.anubis.client.module.impl.movement.FlyModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_310;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinPlayer.class */
@Mixin({class_1657.class})
@Environment(EnvType.CLIENT)
public abstract class MixinPlayer {
    @Inject(method = {"method_7324"}, at = {@At("HEAD")})
    private void anubis$onAttack(class_1297 target, CallbackInfo ci) {
        if (this == class_310.method_1551().field_1724) {
            PlayerAttackEntityEvent.dispatch(target);
        }
    }

    @ModifyReturnValue(method = {"method_49484"}, at = {@At("RETURN")})
    private float anubis$flySpeed(float original) {
        FlyModule fly = FlyModule.active;
        if (fly == null) {
            return original;
        }
        class_746 player = class_310.method_1551().field_1724;
        if (player == null || this != player) {
            return original;
        }
        float override = fly.getOffGroundSpeed(player);
        return override != -1.0f ? override : original;
    }
}
