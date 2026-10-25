package net.fabricmc.fabric.mixin.event.lifecycle;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.class_1297;
import net.minecraft.class_3218;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/mixin/event/lifecycle/ServerWorldServerEntityHandlerMixin.class */
@Mixin(targets = {"net/minecraft/class_3218$class_5526"})
abstract class ServerWorldServerEntityHandlerMixin {

    @Shadow
    @Final
    class_3218 field_26936;

    ServerWorldServerEntityHandlerMixin() {
    }

    @Inject(method = {"method_31436(Lnet/minecraft/class_1297;)V"}, at = {@At("TAIL")})
    private void invokeEntityLoadEvent(class_1297 entity, CallbackInfo ci) {
        ServerEntityEvents.ENTITY_LOAD.invoker().onLoad(entity, this.field_26936);
    }

    @Inject(method = {"method_31437(Lnet/minecraft/class_1297;)V"}, at = {@At("HEAD")})
    private void invokeEntityUnloadEvent(class_1297 entity, CallbackInfo info) {
        ServerEntityEvents.ENTITY_UNLOAD.invoker().onUnload(entity, this.field_26936);
    }
}
