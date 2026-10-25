package net.fabricmc.fabric.mixin.event.lifecycle.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.minecraft.class_1297;
import net.minecraft.class_2586;
import net.minecraft.class_2678;
import net.minecraft.class_2724;
import net.minecraft.class_2790;
import net.minecraft.class_2818;
import net.minecraft.class_5455;
import net.minecraft.class_634;
import net.minecraft.class_638;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/mixin/event/lifecycle/client/ClientPacketListenerMixin.class */
@Mixin({class_634.class})
@Environment(EnvType.CLIENT)
abstract class ClientPacketListenerMixin {

    @Shadow
    private class_638 field_3699;

    @Shadow
    @Final
    private class_5455.class_6890 field_25063;

    ClientPacketListenerMixin() {
    }

    @Inject(method = {"method_11117(Lnet/minecraft/class_2724;)V"}, at = {@At(value = "NEW", target = "Lnet/minecraft/class_638;")})
    private void onPlayerRespawn(class_2724 packet, CallbackInfo ci) {
        if (this.field_3699 != null) {
            for (class_1297 entity : this.field_3699.method_18112()) {
                ClientEntityEvents.ENTITY_UNLOAD.invoker().onUnload(entity, this.field_3699);
            }
            for (class_2818 chunk : this.field_3699.fabric_getLoadedChunks()) {
                for (class_2586 blockEntity : chunk.method_12214().values()) {
                    ClientBlockEntityEvents.BLOCK_ENTITY_UNLOAD.invoker().onUnload(blockEntity, this.field_3699);
                }
            }
        }
    }

    @Inject(method = {"method_11120(Lnet/minecraft/class_2678;)V"}, at = {@At(value = "NEW", target = "Lnet/minecraft/class_638;")})
    private void onGameJoin(class_2678 packet, CallbackInfo ci) {
        if (this.field_3699 != null) {
            for (class_1297 entity : this.field_3699.method_18112()) {
                ClientEntityEvents.ENTITY_UNLOAD.invoker().onUnload(entity, this.field_3699);
            }
            for (class_2818 chunk : this.field_3699.fabric_getLoadedChunks()) {
                for (class_2586 blockEntity : chunk.method_12214().values()) {
                    ClientBlockEntityEvents.BLOCK_ENTITY_UNLOAD.invoker().onUnload(blockEntity, this.field_3699);
                }
            }
        }
    }

    @Inject(method = {"method_54134()V"}, at = {@At("HEAD")})
    private void onClearWorld(CallbackInfo ci) {
        if (this.field_3699 != null) {
            for (class_1297 entity : this.field_3699.method_18112()) {
                ClientEntityEvents.ENTITY_UNLOAD.invoker().onUnload(entity, this.field_3699);
            }
            for (class_2818 chunk : this.field_3699.fabric_getLoadedChunks()) {
                for (class_2586 blockEntity : chunk.method_12214().values()) {
                    ClientBlockEntityEvents.BLOCK_ENTITY_UNLOAD.invoker().onUnload(blockEntity, this.field_3699);
                }
            }
        }
    }

    @Inject(method = {"method_11126(Lnet/minecraft/class_2790;)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_9895;method_61753(Lnet/minecraft/class_7225$class_7874;Lnet/minecraft/class_7699;)Lnet/minecraft/class_9895;")})
    private void invokeTagsLoaded(class_2790 packet, CallbackInfo ci) {
        CommonLifecycleEvents.TAGS_LOADED.invoker().onTagsLoaded(this.field_25063, true);
    }
}
