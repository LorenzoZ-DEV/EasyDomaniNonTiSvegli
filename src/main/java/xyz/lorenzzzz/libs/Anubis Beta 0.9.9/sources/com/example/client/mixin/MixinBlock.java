package com.example.client.mixin;

import net.anubis.client.module.impl.render.XrayModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2248;
import net.minecraft.class_2350;
import net.minecraft.class_2680;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinBlock.class */
@Mixin({class_2248.class})
@Environment(EnvType.CLIENT)
public abstract class MixinBlock {
    @Inject(method = {"method_9607"}, at = {@At("HEAD")}, cancellable = true)
    private static void anubis$xrayForceFace(class_2680 state, class_2680 neighborState, class_2350 direction, CallbackInfoReturnable<Boolean> cir) {
        XrayModule.Snapshot snapshot = XrayModule.active;
        if (snapshot != null && snapshot.isWhitelisted(state.method_26204()) && XrayModule.CURRENT_ALPHA.get()[0] >= 1.0f) {
            cir.setReturnValue(true);
        }
    }
}
