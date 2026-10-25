package com.example.client.mixin;

import net.anubis.client.module.impl.render.NoRenderModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11973;
import net.minecraft.class_1297;
import net.minecraft.class_898;
import net.minecraft.class_8984;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinTrialSpawnerRenderer.class */
@Mixin({class_8984.class})
@Environment(EnvType.CLIENT)
public abstract class MixinTrialSpawnerRenderer {
    @Inject(method = {"method_74396"}, at = {@At("HEAD")}, cancellable = true)
    private static void anubis$cancelSpawnerMobExtract(class_11973 state, float partialTick, class_1297 displayEntity, class_898 dispatcher, double x, double y, CallbackInfo ci) {
        if (NoRenderModule.hideSpawnerMob()) {
            ci.cancel();
        }
    }
}
