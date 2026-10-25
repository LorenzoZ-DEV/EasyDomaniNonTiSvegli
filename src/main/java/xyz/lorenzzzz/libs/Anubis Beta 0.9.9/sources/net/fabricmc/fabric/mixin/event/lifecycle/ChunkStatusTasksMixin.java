package net.fabricmc.fabric.mixin.event.lifecycle;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.impl.event.lifecycle.ChunkLevelTypeEventTracker;
import net.minecraft.class_2791;
import net.minecraft.class_2818;
import net.minecraft.class_2821;
import net.minecraft.class_3194;
import net.minecraft.class_9310;
import net.minecraft.class_9312;
import net.minecraft.class_9761;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/mixin/event/lifecycle/ChunkStatusTasksMixin.class */
@Mixin({class_9310.class})
abstract class ChunkStatusTasksMixin {

    @Unique
    private static final class_3194[] fabric_CHUNK_LEVEL_TYPES = class_3194.values();

    ChunkStatusTasksMixin() {
    }

    @Inject(method = {"method_60553(Lnet/minecraft/class_2791;Lnet/minecraft/class_9312;Lnet/minecraft/class_9761;)Lnet/minecraft/class_2791;"}, at = {@At("TAIL")})
    private static void onChunkLoad(class_2791 chunk, class_9312 chunkGenerationContext, class_9761 chunkHolder, CallbackInfoReturnable<class_2791> callbackInfoReturnable) {
        class_2818 worldChunk = (class_2818) callbackInfoReturnable.getReturnValue();
        ServerChunkEvents.CHUNK_LOAD.invoker().onChunkLoad(chunkGenerationContext.comp_2434(), worldChunk);
        if (!(chunk instanceof class_2821)) {
            ServerChunkEvents.CHUNK_GENERATE.invoker().onChunkGenerate(chunkGenerationContext.comp_2434(), worldChunk);
        }
        ChunkLevelTypeEventTracker levelTypeTracker = (ChunkLevelTypeEventTracker) chunkHolder;
        for (int i = levelTypeTracker.fabric_getCurrentEventLevelType().ordinal(); i < chunkHolder.method_60474().ordinal(); i++) {
            class_3194 oldLevelType = fabric_CHUNK_LEVEL_TYPES[i];
            class_3194 newLevelType = fabric_CHUNK_LEVEL_TYPES[i + 1];
            ServerChunkEvents.CHUNK_LEVEL_TYPE_CHANGE.invoker().onChunkLevelTypeChange(chunkGenerationContext.comp_2434(), worldChunk, oldLevelType, newLevelType);
            levelTypeTracker.fabric_setCurrentEventLevelType(newLevelType);
        }
    }
}
