package net.fabricmc.fabric.mixin.event.lifecycle;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.class_2535;
import net.minecraft.class_3222;
import net.minecraft.class_3324;
import net.minecraft.class_8792;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/mixin/event/lifecycle/PlayerListMixin.class */
@Mixin({class_3324.class})
public class PlayerListMixin {
    @Inject(method = {"method_14570(Lnet/minecraft/class_2535;Lnet/minecraft/class_3222;Lnet/minecraft/class_8792;)V"}, at = {@At(value = "NEW", target = "Lnet/minecraft/class_2788;")})
    private void hookOnPlayerConnect(class_2535 connection, class_3222 player, class_8792 arg, CallbackInfo ci) {
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.invoker().onSyncDataPackContents(player, true);
    }

    @Inject(method = {"method_14572()V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_2790;<init>(Ljava/util/Map;)V")})
    private void hookOnDataPacksReloaded(CallbackInfo ci) {
        for (class_3222 player : ((class_3324) this).method_14571()) {
            ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.invoker().onSyncDataPackContents(player, false);
        }
    }
}
