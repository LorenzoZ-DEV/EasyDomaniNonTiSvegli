package net.fabricmc.fabric.mixin.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_310;
import net.minecraft.class_437;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/mixin/screen/MinecraftMixin.class */
@Mixin({class_310.class})
@Environment(EnvType.CLIENT)
abstract class MinecraftMixin {

    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("fabric-screen-api-v1");

    @Unique
    private static final boolean DEBUG_SCREEN;

    @Shadow
    public class_437 field_1755;

    @Shadow
    private Thread field_1696;

    @Unique
    private class_437 tickingScreen;

    MinecraftMixin() {
    }

    static {
        DEBUG_SCREEN = FabricLoader.getInstance().isDevelopmentEnvironment() || Boolean.getBoolean("fabric.debugScreen");
    }

    @Inject(method = {"method_1507(Lnet/minecraft/class_437;)V"}, at = {@At("HEAD")})
    private void checkThreadOnDev(class_437 screen, CallbackInfo ci) {
        Thread currentThread = Thread.currentThread();
        if (DEBUG_SCREEN && currentThread != this.field_1696) {
            LOGGER.error("Attempted to set screen to \"{}\" outside the render thread (\"{}\"). This will likely follow a crash! Make sure to call setScreen on the render thread.", screen, currentThread.getName());
        }
    }

    @Inject(method = {"method_1507(Lnet/minecraft/class_437;)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_437;method_25432()V", shift = At.Shift.AFTER)})
    private void onScreenRemove(class_437 screen, CallbackInfo ci) {
        ScreenEvents.remove(this.field_1755).invoker().onRemove(this.field_1755);
    }

    @Inject(method = {"method_1490()V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_437;method_25432()V", shift = At.Shift.AFTER)})
    private void onScreenRemoveBecauseStopping(CallbackInfo ci) {
        ScreenEvents.remove(this.field_1755).invoker().onRemove(this.field_1755);
    }

    @Inject(method = {"method_1574()V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_437;method_25393()V")})
    private void beforeScreenTick(CallbackInfo ci) {
        this.tickingScreen = this.field_1755;
        ScreenEvents.beforeTick(this.tickingScreen).invoker().beforeTick(this.tickingScreen);
    }

    @Inject(method = {"method_1574()V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_437;method_25393()V", shift = At.Shift.AFTER)})
    private void afterScreenTick(CallbackInfo ci) {
        ScreenEvents.afterTick(this.tickingScreen).invoker().afterTick(this.tickingScreen);
        this.tickingScreen = null;
    }

    @Inject(method = {"method_29610(Lnet/minecraft/class_32$class_5143;Lnet/minecraft/class_3283;Lnet/minecraft/class_6904;Z)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_3928;method_25393()V")})
    private void beforeLoadingScreenTick(CallbackInfo ci) {
        this.tickingScreen = this.field_1755;
        ScreenEvents.beforeTick(this.tickingScreen).invoker().beforeTick(this.tickingScreen);
    }

    @Inject(method = {"method_29610(Lnet/minecraft/class_32$class_5143;Lnet/minecraft/class_3283;Lnet/minecraft/class_6904;Z)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_310;method_1523(Z)V")})
    private void afterLoadingScreenTick(CallbackInfo ci) {
        ScreenEvents.afterTick(this.tickingScreen).invoker().afterTick(this.tickingScreen);
        this.tickingScreen = null;
    }
}
