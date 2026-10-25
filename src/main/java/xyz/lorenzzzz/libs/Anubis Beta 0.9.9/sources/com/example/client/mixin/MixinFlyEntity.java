package com.example.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.anubis.client.module.impl.movement.FlyModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinFlyEntity.class */
@Mixin({class_1297.class})
@Environment(EnvType.CLIENT)
public abstract class MixinFlyEntity {
    @ModifyReturnValue(method = {"method_5799"}, at = {@At("RETURN")})
    private boolean anubis$flyIsInWater(boolean original) {
        return original && !anubis$flyAffects();
    }

    @ModifyReturnValue(method = {"method_5771"}, at = {@At("RETURN")})
    private boolean anubis$flyIsInLava(boolean original) {
        return original && !anubis$flyAffects();
    }

    @ModifyExpressionValue(method = {"method_5790"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_1297;method_5869()Z")})
    private boolean anubis$flyIsUnderWater(boolean original) {
        return original && !anubis$flyAffects();
    }

    @Unique
    private boolean anubis$flyAffects() {
        return FlyModule.active != null && this == class_310.method_1551().field_1724;
    }
}
