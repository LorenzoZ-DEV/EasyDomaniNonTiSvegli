package net.fabricmc.fabric.mixin.event.lifecycle.server;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.Map;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2586;
import net.minecraft.class_2818;
import net.minecraft.class_3218;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/mixin/event/lifecycle/server/LevelChunkMixin.class */
@Mixin({class_2818.class})
abstract class LevelChunkMixin {
    @Shadow
    public abstract class_1937 method_12200();

    LevelChunkMixin() {
    }

    @ModifyExpressionValue(method = {"method_12007(Lnet/minecraft/class_2586;)V"}, at = {@At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;")})
    private <V> V onLoadBlockEntity(V removedBlockEntity, class_2586 blockEntity) {
        if (blockEntity != null && blockEntity != removedBlockEntity && (method_12200() instanceof class_3218)) {
            ServerBlockEntityEvents.BLOCK_ENTITY_LOAD.invoker().onLoad(blockEntity, (class_3218) method_12200());
        }
        return removedBlockEntity;
    }

    @Inject(method = {"method_12007(Lnet/minecraft/class_2586;)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_2586;method_11012()V", shift = At.Shift.AFTER)})
    private void onRemoveBlockEntity(class_2586 blockEntity, CallbackInfo info, @Local(ordinal = 1) class_2586 removedBlockEntity) {
        if (method_12200() instanceof class_3218) {
            ServerBlockEntityEvents.BLOCK_ENTITY_UNLOAD.invoker().onUnload(removedBlockEntity, (class_3218) method_12200());
        }
    }

    @Redirect(method = {"method_12201(Lnet/minecraft/class_2338;Lnet/minecraft/class_2818$class_2819;)Lnet/minecraft/class_2586;"}, at = @At(value = "INVOKE", target = "Ljava/util/Map;remove(Ljava/lang/Object;)Ljava/lang/Object;"), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/class_2818;method_12208(Lnet/minecraft/class_2338;)Lnet/minecraft/class_2586;")))
    private <K, V> Object onRemoveBlockEntity(Map<K, V> map, K key) {
        V removed = map.remove(key);
        if (removed != null && (method_12200() instanceof class_3218)) {
            ServerBlockEntityEvents.BLOCK_ENTITY_UNLOAD.invoker().onUnload((class_2586) removed, (class_3218) method_12200());
        }
        return removed;
    }

    @Inject(method = {"method_12041(Lnet/minecraft/class_2338;)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_2586;method_11012()V")})
    private void onRemoveBlockEntity(class_2338 pos, CallbackInfo ci, @Local class_2586 removed) {
        if (removed != null && (method_12200() instanceof class_3218)) {
            ServerBlockEntityEvents.BLOCK_ENTITY_UNLOAD.invoker().onUnload(removed, (class_3218) method_12200());
        }
    }
}
