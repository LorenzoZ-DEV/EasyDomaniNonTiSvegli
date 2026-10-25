package com.example.client.mixin;

import net.anubis.client.module.impl.combat.AutoCityModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinAutoCityMinecraft.class */
@Mixin({class_310.class})
@Environment(EnvType.CLIENT)
public abstract class MixinAutoCityMinecraft {
    @Inject(method = {"method_1590"}, at = {@At("HEAD")}, cancellable = true)
    private void anubis$autoCityContinueAttack(boolean down, CallbackInfo ci) {
        if (AutoCityModule.mining()) {
            ci.cancel();
        }
    }
}
