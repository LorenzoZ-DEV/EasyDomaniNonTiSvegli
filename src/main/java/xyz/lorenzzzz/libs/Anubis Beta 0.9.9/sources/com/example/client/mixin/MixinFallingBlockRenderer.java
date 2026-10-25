package com.example.client.mixin;

import net.anubis.client.module.impl.render.NoRenderModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1540;
import net.minecraft.class_4604;
import net.minecraft.class_901;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinFallingBlockRenderer.class */
@Mixin({class_901.class})
@Environment(EnvType.CLIENT)
public abstract class MixinFallingBlockRenderer {
    @Inject(method = {"method_62436"}, at = {@At("HEAD")}, cancellable = true)
    private void anubis$cancelFallingBlock(class_1540 entity, class_4604 frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (NoRenderModule.hideFallingBlocks()) {
            cir.setReturnValue(false);
        }
    }
}
