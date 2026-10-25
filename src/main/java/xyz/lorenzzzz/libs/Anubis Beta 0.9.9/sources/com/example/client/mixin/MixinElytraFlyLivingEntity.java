package com.example.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.anubis.client.module.impl.movement.ElytraFlyModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1309;
import net.minecraft.class_310;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinElytraFlyLivingEntity.class */
@Mixin({class_1309.class})
@Environment(EnvType.CLIENT)
public abstract class MixinElytraFlyLivingEntity {
    @ModifyReturnValue(method = {"method_6128"}, at = {@At("RETURN")})
    private boolean anubis$elytraFlyIsFallFlying(boolean original) {
        ElytraFlyModule module = ElytraFlyModule.active;
        if (module == null) {
            return original;
        }
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        return (this == player && mc.method_18854()) ? module.modifyFallFlying(player, original) : original;
    }
}
