package com.example.client.mixin;

import net.anubis.client.module.impl.render.XrayModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_775;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinLiquidBlockRenderer.class */
@Mixin({class_775.class})
@Environment(EnvType.CLIENT)
public abstract class MixinLiquidBlockRenderer {
    @ModifyArg(method = {"method_23072"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_4588;method_22915(FFFF)Lnet/minecraft/class_4588;"), index = 3)
    private float anubis$xrayFluidAlpha(float alpha) {
        return XrayModule.active == null ? alpha : alpha * XrayModule.CURRENT_ALPHA.get()[0];
    }
}
