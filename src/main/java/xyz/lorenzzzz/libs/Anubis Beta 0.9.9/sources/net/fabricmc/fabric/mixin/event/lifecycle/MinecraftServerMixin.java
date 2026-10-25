package net.fabricmc.fabric.mixin.event.lifecycle;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.class_3218;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/mixin/event/lifecycle/MinecraftServerMixin.class */
@Mixin({MinecraftServer.class})
public abstract class MinecraftServerMixin {

    @Shadow
    private MinecraftServer.class_6897 field_25318;

    @Inject(at = {@At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;method_3823()Z")}, method = {"method_29741()V"})
    private void beforeSetupServer(CallbackInfo info) {
        ServerLifecycleEvents.SERVER_STARTING.invoker().onServerStarting((MinecraftServer) this);
    }

    @Inject(at = {@At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;method_49385()Lnet/minecraft/class_2926;", ordinal = 0)}, method = {"method_29741()V"})
    private void afterSetupServer(CallbackInfo info) {
        ServerLifecycleEvents.SERVER_STARTED.invoker().onServerStarted((MinecraftServer) this);
    }

    @Inject(at = {@At("HEAD")}, method = {"method_3782()V"})
    private void beforeShutdownServer(CallbackInfo info) {
        ServerLifecycleEvents.SERVER_STOPPING.invoker().onServerStopping((MinecraftServer) this);
    }

    @Inject(at = {@At("TAIL")}, method = {"method_3782()V"})
    private void afterShutdownServer(CallbackInfo info) {
        ServerLifecycleEvents.SERVER_STOPPED.invoker().onServerStopped((MinecraftServer) this);
    }

    @Inject(at = {@At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;method_3813(Ljava/util/function/BooleanSupplier;)V")}, method = {"method_3748(Ljava/util/function/BooleanSupplier;)V"})
    private void onStartTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        ServerTickEvents.START_SERVER_TICK.invoker().onStartTick((MinecraftServer) this);
    }

    @Inject(at = {@At("TAIL")}, method = {"method_3748(Ljava/util/function/BooleanSupplier;)V"})
    private void onEndTick(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
        ServerTickEvents.END_SERVER_TICK.invoker().onEndTick((MinecraftServer) this);
    }

    @WrapOperation(method = {"method_3786()V"}, at = {@At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;")})
    private <K, V> V onLoadWorld(Map<K, V> map, K k, V v, Operation<V> operation) {
        V v2 = (V) operation.call(new Object[]{map, k, v});
        ServerWorldEvents.LOAD.invoker().onWorldLoad((MinecraftServer) this, (class_3218) v);
        return v2;
    }

    @Inject(method = {"method_3782()V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_3218;close()V")})
    private void onUnloadWorldAtShutdown(CallbackInfo ci, @Local class_3218 world) {
        ServerWorldEvents.UNLOAD.invoker().onWorldUnload((MinecraftServer) this, world);
    }

    @Inject(method = {"method_29439(Ljava/util/Collection;)Ljava/util/concurrent/CompletableFuture;"}, at = {@At("HEAD")})
    private void startResourceReload(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.invoker().startDataPackReload((MinecraftServer) this, this.field_25318.comp_352());
    }

    @Inject(method = {"method_29439(Ljava/util/Collection;)Ljava/util/concurrent/CompletableFuture;"}, at = {@At("TAIL")})
    private void endResourceReload(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        ((CompletableFuture) cir.getReturnValue()).handleAsync((value, throwable) -> {
            ServerLifecycleEvents.END_DATA_PACK_RELOAD.invoker().endDataPackReload((MinecraftServer) this, this.field_25318.comp_352(), throwable == null);
            return value;
        }, (Executor) this);
    }

    @Inject(method = {"method_3723(ZZZ)Z"}, at = {@At("HEAD")})
    private void startSave(boolean suppressLogs, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir) {
        ServerLifecycleEvents.BEFORE_SAVE.invoker().onBeforeSave((MinecraftServer) this, flush, force);
    }

    @Inject(method = {"method_3723(ZZZ)Z"}, at = {@At("TAIL")})
    private void endSave(boolean suppressLogs, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir) {
        ServerLifecycleEvents.AFTER_SAVE.invoker().onAfterSave((MinecraftServer) this, flush, force);
    }
}
