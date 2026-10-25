package net.fabricmc.fabric.mixin.event.lifecycle.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.minecraft.class_5455;
import net.minecraft.class_5912;
import net.minecraft.class_8674;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/mixin/event/lifecycle/client/ClientConfigurationPacketListenerImplMixin.class */
@Mixin({class_8674.class})
@Environment(EnvType.CLIENT)
public class ClientConfigurationPacketListenerImplMixin {
    @Inject(method = {"method_57043(Lnet/minecraft/class_5912;)Lnet/minecraft/class_5455$class_6890;"}, at = {@At("RETURN")})
    private void invokeTagsLoaded(class_5912 factory, CallbackInfoReturnable<class_5455.class_6890> cir) {
        CommonLifecycleEvents.TAGS_LOADED.invoker().onTagsLoaded((class_5455) cir.getReturnValue(), true);
    }
}
