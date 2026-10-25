package net.fabricmc.fabric.mixin.event.lifecycle;

import java.util.concurrent.Executor;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.impl.event.lifecycle.ChunkLevelTypeEventTracker;
import net.minecraft.class_1923;
import net.minecraft.class_2806;
import net.minecraft.class_2818;
import net.minecraft.class_3193;
import net.minecraft.class_3194;
import net.minecraft.class_3218;
import net.minecraft.class_3898;
import net.minecraft.class_5539;
import net.minecraft.class_8563;
import net.minecraft.class_9761;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/mixin/event/lifecycle/ChunkHolderMixin.class */
@Mixin({class_3193.class})
public abstract class ChunkHolderMixin extends class_9761 implements ChunkLevelTypeEventTracker {

    @Shadow
    @Final
    private class_5539 field_26929;

    @Shadow
    private int field_16432;

    @Unique
    private static final class_3194[] fabric_CHUNK_LEVEL_TYPES = class_3194.values();

    @Unique
    private class_3194 fabric_currentEventLevelType;

    private ChunkHolderMixin(class_1923 pos) {
        super(pos);
        this.fabric_currentEventLevelType = class_3194.field_19334;
    }

    @Inject(method = {"method_14007(Lnet/minecraft/class_3898;Ljava/util/concurrent/Executor;)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_3193;method_39967(Ljava/util/concurrent/CompletableFuture;)V", shift = At.Shift.AFTER, ordinal = 0)})
    private void updateFutures$inaccessibleToFull(class_3898 chunkLoadingManager, Executor executor, CallbackInfo ci) {
        if ((method_60457(class_2806.field_12803) instanceof class_2818) && this.fabric_currentEventLevelType == class_3194.field_19334) {
            ServerChunkEvents.CHUNK_LEVEL_TYPE_CHANGE.invoker().onChunkLevelTypeChange((class_3218) this.field_26929, (class_2818) method_60457(class_2806.field_12803), class_3194.field_19334, class_3194.field_44855);
            this.fabric_currentEventLevelType = class_3194.field_44855;
        }
    }

    @Inject(method = {"method_14007(Lnet/minecraft/class_3898;Ljava/util/concurrent/Executor;)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_3193;method_39967(Ljava/util/concurrent/CompletableFuture;)V", shift = At.Shift.AFTER, ordinal = 1)})
    private void updateFutures$fullToBlockTicking(class_3898 chunkLoadingManager, Executor executor, CallbackInfo ci) {
        if (this.fabric_currentEventLevelType == class_3194.field_44855) {
            ServerChunkEvents.CHUNK_LEVEL_TYPE_CHANGE.invoker().onChunkLevelTypeChange((class_3218) this.field_26929, (class_2818) method_60457(class_2806.field_12803), class_3194.field_44855, class_3194.field_44856);
            this.fabric_currentEventLevelType = class_3194.field_44856;
        }
    }

    @Inject(method = {"method_14007(Lnet/minecraft/class_3898;Ljava/util/concurrent/Executor;)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_3193;method_39967(Ljava/util/concurrent/CompletableFuture;)V", shift = At.Shift.AFTER, ordinal = 2)})
    private void updateFutures$blockTickingToEntityTicking(class_3898 chunkLoadingManager, Executor executor, CallbackInfo ci) {
        if (this.fabric_currentEventLevelType == class_3194.field_44856) {
            ServerChunkEvents.CHUNK_LEVEL_TYPE_CHANGE.invoker().onChunkLevelTypeChange((class_3218) this.field_26929, (class_2818) method_60457(class_2806.field_12803), class_3194.field_44856, class_3194.field_13877);
            this.fabric_currentEventLevelType = class_3194.field_13877;
        }
    }

    @Inject(method = {"method_31408(Lnet/minecraft/class_3898;Lnet/minecraft/class_3194;)V"}, at = {@At("HEAD")})
    private void decreaseLevel(class_3898 chunkLoadingManager, class_3194 target, CallbackInfo ci) {
        class_3194 previous = class_8563.method_51830(this.field_16432);
        class_3218 serverWorld = this.field_26929;
        for (int i = previous.ordinal(); i > target.ordinal(); i--) {
            class_3194 oldLevelType = fabric_CHUNK_LEVEL_TYPES[i];
            class_3194 newLevelType = fabric_CHUNK_LEVEL_TYPES[i - 1];
            if (this.fabric_currentEventLevelType.method_14014(oldLevelType)) {
                ServerChunkEvents.CHUNK_LEVEL_TYPE_CHANGE.invoker().onChunkLevelTypeChange(serverWorld, (class_2818) method_60457(class_2806.field_12803), oldLevelType, newLevelType);
                this.fabric_currentEventLevelType = newLevelType;
            }
        }
    }

    @Override // net.fabricmc.fabric.impl.event.lifecycle.ChunkLevelTypeEventTracker
    public void fabric_setCurrentEventLevelType(class_3194 levelType) {
        this.fabric_currentEventLevelType = levelType;
    }

    @Override // net.fabricmc.fabric.impl.event.lifecycle.ChunkLevelTypeEventTracker
    public class_3194 fabric_getCurrentEventLevelType() {
        return this.fabric_currentEventLevelType;
    }
}
