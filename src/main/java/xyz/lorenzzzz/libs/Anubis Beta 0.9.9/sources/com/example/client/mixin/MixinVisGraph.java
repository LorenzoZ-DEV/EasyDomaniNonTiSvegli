package com.example.client.mixin;

import net.anubis.client.module.impl.misc.FreecamModule;
import net.anubis.client.module.impl.render.XrayModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2338;
import net.minecraft.class_852;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinVisGraph.class */
@Mixin({class_852.class})
@Environment(EnvType.CLIENT)
public abstract class MixinVisGraph {
    @Inject(method = {"method_3682"}, at = {@At("HEAD")}, cancellable = true)
    private void anubis$noOcclusion(class_2338 pos, CallbackInfo ci) {
        if (XrayModule.active != null || FreecamModule.active != null) {
            ci.cancel();
        }
    }
}
