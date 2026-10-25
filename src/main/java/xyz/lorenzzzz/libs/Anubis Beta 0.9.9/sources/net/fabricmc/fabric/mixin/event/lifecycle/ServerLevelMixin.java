package net.fabricmc.fabric.mixin.event.lifecycle;

import java.util.function.BooleanSupplier;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.class_3218;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/mixin/event/lifecycle/ServerLevelMixin.class */
@Mixin({class_3218.class})
public abstract class ServerLevelMixin {
    @Inject(method = {"method_18765(Ljava/util/function/BooleanSupplier;)V"}, at = {@At(value = "FIELD", target = "Lnet/minecraft/class_3218;field_13953:Z", opcode = 181, ordinal = 0, shift = At.Shift.AFTER)})
    private void startWorldTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        ServerTickEvents.START_WORLD_TICK.invoker().onStartTick((class_3218) this);
    }

    @Inject(method = {"method_18765(Ljava/util/function/BooleanSupplier;)V"}, at = {@At("TAIL")})
    private void endWorldTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        ServerTickEvents.END_WORLD_TICK.invoker().onEndTick((class_3218) this);
    }
}
