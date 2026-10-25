package net.fabricmc.fabric.mixin.event.lifecycle.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.minecraft.class_310;
import net.minecraft.class_638;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/mixin/event/lifecycle/client/MinecraftMixin.class */
@Mixin({class_310.class})
@Environment(EnvType.CLIENT)
public abstract class MinecraftMixin {
    @Inject(at = {@At("HEAD")}, method = {"method_1574()V"})
    private void onStartTick(CallbackInfo info) {
        ClientTickEvents.START_CLIENT_TICK.invoker().onStartTick((class_310) this);
    }

    @Inject(at = {@At("RETURN")}, method = {"method_1574()V"})
    private void onEndTick(CallbackInfo info) {
        ClientTickEvents.END_CLIENT_TICK.invoker().onEndTick((class_310) this);
    }

    @Inject(at = {@At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;)V", shift = At.Shift.AFTER)}, method = {"method_1490()V"})
    private void onStopping(CallbackInfo ci) {
        ClientLifecycleEvents.CLIENT_STOPPING.invoker().onClientStopping((class_310) this);
    }

    @Inject(at = {@At(value = "FIELD", target = "Lnet/minecraft/class_310;field_1696:Ljava/lang/Thread;", shift = At.Shift.AFTER, ordinal = 0)}, method = {"method_1514()V"})
    private void onStart(CallbackInfo ci) {
        ClientLifecycleEvents.CLIENT_STARTED.invoker().onClientStarted((class_310) this);
    }

    @Inject(method = {"method_76796(Lnet/minecraft/class_638;)V"}, at = {@At("TAIL")})
    private void afterClientWorldChange(class_638 world, CallbackInfo ci) {
        if (world != null) {
            class_310 client = (class_310) this;
            ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.invoker().afterWorldChange(client, world);
        }
    }
}
