package net.fabricmc.fabric.mixin.event.lifecycle.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.minecraft.class_1297;
import net.minecraft.class_638;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/mixin/event/lifecycle/client/ClientWorldClientEntityHandlerMixin.class */
@Mixin(targets = {"net/minecraft/class_638$class_5612"})
@Environment(EnvType.CLIENT)
abstract class ClientWorldClientEntityHandlerMixin {

    @Shadow
    @Final
    class_638 field_27735;

    ClientWorldClientEntityHandlerMixin() {
    }

    @Inject(method = {"method_32129(Lnet/minecraft/class_1297;)V"}, at = {@At("TAIL")})
    private void invokeLoadEntity(class_1297 entity, CallbackInfo ci) {
        ClientEntityEvents.ENTITY_LOAD.invoker().onLoad(entity, this.field_27735);
    }

    @Inject(method = {"method_32130(Lnet/minecraft/class_1297;)V"}, at = {@At("HEAD")})
    private void invokeUnloadEntity(class_1297 entity, CallbackInfo ci) {
        ClientEntityEvents.ENTITY_UNLOAD.invoker().onUnload(entity, this.field_27735);
    }
}
