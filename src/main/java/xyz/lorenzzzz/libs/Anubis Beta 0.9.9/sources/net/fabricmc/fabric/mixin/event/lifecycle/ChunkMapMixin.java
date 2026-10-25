package net.fabricmc.fabric.mixin.event.lifecycle;

import com.llamalad7.mixinextras.sugar.Local;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.minecraft.class_2791;
import net.minecraft.class_2818;
import net.minecraft.class_3193;
import net.minecraft.class_3218;
import net.minecraft.class_3898;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/mixin/event/lifecycle/ChunkMapMixin.class */
@Mixin({class_3898.class})
public abstract class ChunkMapMixin {

    @Shadow
    @Final
    class_3218 field_17214;

    @Inject(method = {"method_60440(Lnet/minecraft/class_3193;Ljava/util/concurrent/CompletableFuture;J)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_3898;method_17228(Lnet/minecraft/class_2791;)Z")})
    private void onChunkUnload(class_3193 chunkHolder, CompletableFuture<?> completableFuture, long l, CallbackInfo ci, @Local class_2791 chunk) {
        if (chunk instanceof class_2818) {
            class_2818 worldChunk = (class_2818) chunk;
            ServerChunkEvents.CHUNK_UNLOAD.invoker().onChunkUnload(this.field_17214, worldChunk);
        }
    }
}
