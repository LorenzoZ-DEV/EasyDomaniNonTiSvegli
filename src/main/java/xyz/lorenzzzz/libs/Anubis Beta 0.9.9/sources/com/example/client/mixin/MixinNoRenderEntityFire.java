package com.example.client.mixin;

import net.anubis.client.module.impl.render.NoRenderModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10017;
import net.minecraft.class_1297;
import net.minecraft.class_761;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinNoRenderEntityFire.class */
@Mixin({class_761.class})
@Environment(EnvType.CLIENT)
public abstract class MixinNoRenderEntityFire {
    @Inject(method = {"method_72914"}, at = {@At("RETURN")})
    private void anubis$hideEntityFire(class_1297 entity, float partialTick, CallbackInfoReturnable<class_10017> cir) {
        class_10017 state = (class_10017) cir.getReturnValue();
        if (state == null || !NoRenderModule.hideEntityFire()) {
            return;
        }
        state.field_53335 = false;
    }
}
