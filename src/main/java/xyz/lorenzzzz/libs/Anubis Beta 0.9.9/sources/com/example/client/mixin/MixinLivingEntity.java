package com.example.client.mixin;

import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.impl.misc.SlowSwingModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1309;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinLivingEntity.class */
@Mixin({class_1309.class})
@Environment(EnvType.CLIENT)
public abstract class MixinLivingEntity {
    @Inject(method = {"method_6028"}, at = {@At("RETURN")}, cancellable = true)
    private void anubis$modifySwingDuration(CallbackInfoReturnable<Integer> cir) {
        SlowSwingModule module = (SlowSwingModule) ModuleRegistry.get(SlowSwingModule.class);
        if (module != null && module.isEnabled() && this == class_310.method_1551().field_1724) {
            cir.setReturnValue(Integer.valueOf(module.getModifiedSwingDuration(((Integer) cir.getReturnValue()).intValue())));
        }
    }
}
