package net.fabricmc.fabric.mixin.resource.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.resource.client.PackTooltipComponent;
import net.minecraft.class_5632;
import net.minecraft.class_5684;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/mixin/resource/client/ClientTooltipComponentMixin.class */
@Mixin({class_5684.class})
@Environment(EnvType.CLIENT)
public interface ClientTooltipComponentMixin {
    @Inject(method = {"method_32663(Lnet/minecraft/class_5632;)Lnet/minecraft/class_5684;"}, at = {@At("HEAD")}, cancellable = true)
    private static void onCreate(class_5632 tooltipComponent, CallbackInfoReturnable<class_5684> cir) {
        if (tooltipComponent instanceof PackTooltipComponent) {
            PackTooltipComponent packTooltipComponent = (PackTooltipComponent) tooltipComponent;
            cir.setReturnValue(packTooltipComponent);
        }
    }
}
