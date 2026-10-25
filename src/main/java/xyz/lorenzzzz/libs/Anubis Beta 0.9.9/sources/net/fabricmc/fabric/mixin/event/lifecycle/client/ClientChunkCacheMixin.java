package net.fabricmc.fabric.mixin.event.lifecycle.client;

import com.llamalad7.mixinextras.sugar.Local;
import java.util.Map;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.minecraft.class_1923;
import net.minecraft.class_2540;
import net.minecraft.class_2818;
import net.minecraft.class_2902;
import net.minecraft.class_631;
import net.minecraft.class_638;
import net.minecraft.class_6603;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/mixin/event/lifecycle/client/ClientChunkCacheMixin.class */
@Mixin({class_631.class})
@Environment(EnvType.CLIENT)
public abstract class ClientChunkCacheMixin {

    @Shadow
    @Final
    class_638 field_16525;

    @Inject(method = {"method_16020(IILnet/minecraft/class_2540;Ljava/util/Map;Ljava/util/function/Consumer;)Lnet/minecraft/class_2818;"}, at = {@At("TAIL")})
    private void onChunkLoad(int x, int z, class_2540 packetByteBuf, Map<class_2902.class_2903, long[]> highmap, Consumer<class_6603.class_6605> consumer, CallbackInfoReturnable<class_2818> info) {
        ClientChunkEvents.CHUNK_LOAD.invoker().onChunkLoad(this.field_16525, (class_2818) info.getReturnValue());
    }

    @Inject(method = {"method_16020(IILnet/minecraft/class_2540;Ljava/util/Map;Ljava/util/function/Consumer;)Lnet/minecraft/class_2818;"}, at = {@At(value = "NEW", target = "Lnet/minecraft/class_2818;", shift = At.Shift.BEFORE)})
    private void onChunkUnload(int x, int z, class_2540 buf, Map<class_2902.class_2903, long[]> highmap, Consumer<class_6603.class_6605> consumer, CallbackInfoReturnable<class_2818> info, @Local class_2818 worldChunk) {
        if (worldChunk != null) {
            ClientChunkEvents.CHUNK_UNLOAD.invoker().onChunkUnload(this.field_16525, worldChunk);
        }
    }

    @Inject(method = {"method_2859(Lnet/minecraft/class_1923;)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_631$class_3681;method_62893(ILnet/minecraft/class_2818;)V")})
    private void onChunkUnload(class_1923 pos, CallbackInfo ci, @Local class_2818 chunk) {
        ClientChunkEvents.CHUNK_UNLOAD.invoker().onChunkUnload(this.field_16525, chunk);
    }

    @Inject(method = {"method_20180(I)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_631$class_3681;method_16034(II)Z")})
    private void onUpdateLoadDistance(int loadDistance, CallbackInfo ci, @Local class_631.class_3681 clientChunkMap, @Local class_2818 oldChunk, @Local class_1923 chunkPos) {
        if (!clientChunkMap.method_16034(chunkPos.field_9181, chunkPos.field_9180)) {
            ClientChunkEvents.CHUNK_UNLOAD.invoker().onChunkUnload(this.field_16525, oldChunk);
        }
    }
}
