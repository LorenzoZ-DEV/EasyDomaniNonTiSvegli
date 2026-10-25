package com.example.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.anubis.client.module.impl.movement.JesusModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1309;
import net.minecraft.class_310;
import net.minecraft.class_3610;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinJesusLivingEntity.class */
@Mixin({class_1309.class})
@Environment(EnvType.CLIENT)
public abstract class MixinJesusLivingEntity {
    @ModifyReturnValue(method = {"method_26319"}, at = {@At("RETURN")})
    private boolean anubis$jesusCanStandOnFluid(boolean original, class_3610 fluidState) {
        if (original) {
            return true;
        }
        JesusModule jesus = JesusModule.active;
        if (jesus == null || this != class_310.method_1551().field_1724) {
            return false;
        }
        return jesus.canWalkOnFluid(fluidState);
    }
}
