package net.fabricmc.fabric.mixin.screen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.class_11908;
import net.minecraft.class_309;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/mixin/screen/KeyboardHandlerMixin.class */
@Mixin({class_309.class})
@Environment(EnvType.CLIENT)
abstract class KeyboardHandlerMixin {
    KeyboardHandlerMixin() {
    }

    @WrapOperation(method = {"method_1466(JILnet/minecraft/class_11908;)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_437;method_25404(Lnet/minecraft/class_11908;)Z")})
    private boolean invokeKeyPressedEvents(class_437 screen, class_11908 ctx, Operation<Boolean> operation) {
        if (screen != null) {
            if (!ScreenKeyboardEvents.allowKeyPress(screen).invoker().allowKeyPress(screen, ctx)) {
                return true;
            }
            ScreenKeyboardEvents.beforeKeyPress(screen).invoker().beforeKeyPress(screen, ctx);
        }
        boolean result = ((Boolean) operation.call(new Object[]{screen, ctx})).booleanValue();
        if (screen != null) {
            ScreenKeyboardEvents.afterKeyPress(screen).invoker().afterKeyPress(screen, ctx);
        }
        return result;
    }

    @WrapOperation(method = {"method_1466(JILnet/minecraft/class_11908;)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_437;method_16803(Lnet/minecraft/class_11908;)Z")})
    private boolean invokeKeyReleasedEvents(class_437 screen, class_11908 ctx, Operation<Boolean> operation) {
        if (screen != null) {
            if (!ScreenKeyboardEvents.allowKeyRelease(screen).invoker().allowKeyRelease(screen, ctx)) {
                return true;
            }
            ScreenKeyboardEvents.beforeKeyRelease(screen).invoker().beforeKeyRelease(screen, ctx);
        }
        boolean result = ((Boolean) operation.call(new Object[]{screen, ctx})).booleanValue();
        if (screen != null) {
            ScreenKeyboardEvents.afterKeyRelease(screen).invoker().afterKeyRelease(screen, ctx);
        }
        return result;
    }
}
