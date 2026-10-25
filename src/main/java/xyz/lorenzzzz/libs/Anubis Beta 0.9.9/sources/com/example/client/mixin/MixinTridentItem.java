package com.example.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.anubis.client.module.impl.misc.TridentUtilModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1835;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinTridentItem.class */
@Mixin({class_1835.class})
@Environment(EnvType.CLIENT)
public abstract class MixinTridentItem {
    @ModifyExpressionValue(method = {"method_7836", "method_7840"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_1657;method_5721()Z")}, require = 0)
    private boolean anubis$allowOutOfWater(boolean original) {
        return original || TridentUtilModule.allowOutOfWater();
    }

    @ModifyExpressionValue(method = {"method_7840"}, at = {@At(value = "CONSTANT", args = {"intValue=10"})}, require = 0)
    private int anubis$scaleMinCharge(int original) {
        return TridentUtilModule.minChargeTicks(original);
    }
}
