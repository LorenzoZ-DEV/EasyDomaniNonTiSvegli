package com.example.client.mixin;

import net.anubis.client.module.impl.misc.AirStuckModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinAirStuckLocalPlayer.class */
@Mixin({class_746.class})
@Environment(EnvType.CLIENT)
public abstract class MixinAirStuckLocalPlayer {
    @Inject(method = {"method_6007"}, at = {@At("HEAD")}, cancellable = true)
    private void anubis$airStuck(CallbackInfo ci) {
        if (AirStuckModule.active != null) {
            ci.cancel();
        }
    }
}
