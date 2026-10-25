package net.fabricmc.fabric.mixin.event.lifecycle.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.class_638;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/mixin/event/lifecycle/client/ClientLevelMixin.class */
@Mixin({class_638.class})
@Environment(EnvType.CLIENT)
public abstract class ClientLevelMixin {
    @Inject(method = {"method_18116()V"}, at = {@At("TAIL")})
    public void tickWorldAfterBlockEntities(CallbackInfo ci) {
        ClientTickEvents.END_WORLD_TICK.invoker().onEndTick((class_638) this);
    }

    @Inject(method = {"method_18116()V"}, at = {@At("HEAD")})
    private void startWorldTick(CallbackInfo ci) {
        ClientTickEvents.START_WORLD_TICK.invoker().onStartTick((class_638) this);
    }
}
