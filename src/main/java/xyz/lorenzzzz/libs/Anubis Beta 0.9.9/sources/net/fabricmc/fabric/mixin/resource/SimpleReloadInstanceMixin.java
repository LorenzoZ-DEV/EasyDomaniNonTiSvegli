package net.fabricmc.fabric.mixin.resource;

import com.llamalad7.mixinextras.sugar.Local;
import java.util.List;
import net.fabricmc.fabric.impl.resource.FabricLifecycledResourceManager;
import net.fabricmc.fabric.impl.resource.ResourceLoaderImpl;
import net.minecraft.class_3300;
import net.minecraft.class_3302;
import net.minecraft.class_4014;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/mixin/resource/SimpleReloadInstanceMixin.class */
@Mixin({class_4014.class})
public class SimpleReloadInstanceMixin {
    @ModifyArg(method = {"method_40087(Lnet/minecraft/class_3300;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Z)Lnet/minecraft/class_4011;"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_4014;method_18369(Lnet/minecraft/class_3300;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;)Lnet/minecraft/class_4011;"))
    private static List<class_3302> sortSimple(List<class_3302> reloaders, @Local(argsOnly = true) class_3300 resourceManager) {
        if (resourceManager instanceof FabricLifecycledResourceManager) {
            FabricLifecycledResourceManager flrm = (FabricLifecycledResourceManager) resourceManager;
            return ResourceLoaderImpl.sort(flrm.fabric$getResourceType(), reloaders);
        }
        return reloaders;
    }

    @ModifyArg(method = {"method_40087(Lnet/minecraft/class_3300;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Z)Lnet/minecraft/class_4011;"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_4010;method_67569(Lnet/minecraft/class_3300;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;)Lnet/minecraft/class_4011;"))
    private static List<class_3302> sortProfiled(List<class_3302> reloaders, @Local(argsOnly = true) class_3300 resourceManager) {
        if (resourceManager instanceof FabricLifecycledResourceManager) {
            FabricLifecycledResourceManager flrm = (FabricLifecycledResourceManager) resourceManager;
            return ResourceLoaderImpl.sort(flrm.fabric$getResourceType(), reloaders);
        }
        return reloaders;
    }

    @ModifyVariable(method = {"method_40087(Lnet/minecraft/class_3300;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Z)Lnet/minecraft/class_4011;"}, at = @At(value = "LOAD", ordinal = 0), argsOnly = true)
    private static boolean adjustProfiledCheck(boolean profiled) {
        return profiled || ResourceLoaderImpl.DEBUG_PROFILE_RESOURCE_RELOADERS;
    }
}
