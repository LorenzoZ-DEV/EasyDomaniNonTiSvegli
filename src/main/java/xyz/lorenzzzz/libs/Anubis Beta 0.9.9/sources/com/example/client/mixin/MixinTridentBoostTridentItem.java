package com.example.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.anubis.client.module.impl.movement.TridentBoostModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1657;
import net.minecraft.class_1835;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinTridentBoostTridentItem.class */
@Mixin({class_1835.class})
@Environment(EnvType.CLIENT)
public abstract class MixinTridentBoostTridentItem {
    @WrapOperation(method = {"method_7840"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_1657;method_5762(DDD)V")}, require = 0)
    private void anubis$tridentBoostPush(class_1657 player, double x, double y, double z, Operation<Void> original) {
        double multiplier = TridentBoostModule.riptideMultiplier();
        original.call(new Object[]{player, Double.valueOf(x * multiplier), Double.valueOf(y * multiplier), Double.valueOf(z * multiplier)});
    }

    @ModifyExpressionValue(method = {"method_7836"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_1657;method_5721()Z")}, require = 0)
    private boolean anubis$tridentBoostUseOutOfWater(boolean original) {
        return original || TridentBoostModule.allowOutOfWater();
    }

    @ModifyExpressionValue(method = {"method_7840"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_1657;method_5721()Z")}, require = 0)
    private boolean anubis$tridentBoostReleaseOutOfWater(boolean original) {
        return original || TridentBoostModule.allowOutOfWater();
    }
}
