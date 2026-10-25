package com.example.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.anubis.client.module.impl.misc.NameProtectModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2561;
import net.minecraft.class_337;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinNameProtectBossHealthOverlay.class */
@Mixin({class_337.class})
@Environment(EnvType.CLIENT)
public abstract class MixinNameProtectBossHealthOverlay {
    @ModifyExpressionValue(method = {"method_1796"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_345;method_5414()Lnet/minecraft/class_2561;")})
    private class_2561 anubis$nameProtectBossName(class_2561 original) {
        return NameProtectModule.protect(original);
    }
}
