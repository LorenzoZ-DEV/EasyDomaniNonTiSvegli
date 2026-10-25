package net.fabricmc.fabric.mixin.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_437;
import net.minecraft.class_465;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/mixin/screen/AbstractContainerScreenMixin.class */
@Mixin({class_465.class})
@Environment(EnvType.CLIENT)
public abstract class AbstractContainerScreenMixin extends class_437 {
    private AbstractContainerScreenMixin(class_2561 title) {
        super(title);
    }

    @Inject(method = {"method_25406(Lnet/minecraft/class_11909;)Z"}, at = {@At("HEAD")}, cancellable = true)
    private void callSuperMouseReleased(class_11909 ctx, CallbackInfoReturnable<Boolean> cir) {
        if (super.method_25406(ctx)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = {"method_25403(Lnet/minecraft/class_11909;DD)Z"}, at = {@At("HEAD")}, cancellable = true)
    private void callSuperMouseReleased(class_11909 ctx, double deltaX, double deltaY, CallbackInfoReturnable<Boolean> cir) {
        if (super.method_25403(ctx, deltaX, deltaY)) {
            cir.setReturnValue(true);
        }
    }
}
