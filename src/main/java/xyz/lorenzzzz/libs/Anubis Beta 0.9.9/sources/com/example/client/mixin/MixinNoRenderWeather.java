package com.example.client.mixin;

import net.anubis.client.module.impl.render.NoRenderModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_12077;
import net.minecraft.class_243;
import net.minecraft.class_4066;
import net.minecraft.class_4184;
import net.minecraft.class_4597;
import net.minecraft.class_638;
import net.minecraft.class_9976;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinNoRenderWeather.class */
@Mixin({class_9976.class})
@Environment(EnvType.CLIENT)
public abstract class MixinNoRenderWeather {
    @Inject(method = {"method_62320"}, at = {@At("HEAD")}, cancellable = true)
    private void anubis$hideWeather(class_4597 bufferSource, class_243 camera, class_12077 state, CallbackInfo ci) {
        if (NoRenderModule.hideWeather()) {
            ci.cancel();
        }
    }

    @Inject(method = {"method_62319"}, at = {@At("HEAD")}, cancellable = true)
    private void anubis$hideRainSplashes(class_638 level, class_4184 camera, int ticks, class_4066 particles, int radius, CallbackInfo ci) {
        if (NoRenderModule.hideWeather()) {
            ci.cancel();
        }
    }
}
