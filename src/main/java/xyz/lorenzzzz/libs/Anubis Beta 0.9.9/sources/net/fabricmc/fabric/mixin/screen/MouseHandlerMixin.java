package net.fabricmc.fabric.mixin.screen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.class_11909;
import net.minecraft.class_312;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/mixin/screen/MouseHandlerMixin.class */
@Mixin({class_312.class})
@Environment(EnvType.CLIENT)
abstract class MouseHandlerMixin {
    MouseHandlerMixin() {
    }

    @WrapOperation(method = {"method_1601(JLnet/minecraft/class_11910;I)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_437;method_25402(Lnet/minecraft/class_11909;Z)Z")})
    private boolean invokeMouseClickedEvents(class_437 screen, class_11909 ctx, boolean doubleClick, Operation<Boolean> operation) {
        if (screen != null) {
            if (!ScreenMouseEvents.allowMouseClick(screen).invoker().allowMouseClick(screen, ctx)) {
                return true;
            }
            ScreenMouseEvents.beforeMouseClick(screen).invoker().beforeMouseClick(screen, ctx);
        }
        boolean result = ((Boolean) operation.call(new Object[]{screen, ctx, Boolean.valueOf(doubleClick)})).booleanValue();
        if (screen != null) {
            result |= ScreenMouseEvents.afterMouseClick(screen).invoker().afterMouseClick(screen, ctx, result);
        }
        return result;
    }

    @WrapOperation(method = {"method_1601(JLnet/minecraft/class_11910;I)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_437;method_25406(Lnet/minecraft/class_11909;)Z")})
    private boolean invokeMousePressedEvents(class_437 screen, class_11909 ctx, Operation<Boolean> operation) {
        if (screen != null) {
            if (!ScreenMouseEvents.allowMouseRelease(screen).invoker().allowMouseRelease(screen, ctx)) {
                return true;
            }
            ScreenMouseEvents.beforeMouseRelease(screen).invoker().beforeMouseRelease(screen, ctx);
        }
        boolean result = ((Boolean) operation.call(new Object[]{screen, ctx})).booleanValue();
        if (screen != null) {
            result |= ScreenMouseEvents.afterMouseRelease(screen).invoker().afterMouseRelease(screen, ctx, result);
        }
        return result;
    }

    @WrapOperation(method = {"method_55793()V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_437;method_25403(Lnet/minecraft/class_11909;DD)Z")})
    private boolean invokeMouseDragEvents(class_437 screen, class_11909 ctx, double horizontalAmount, double verticalAmount, Operation<Boolean> operation) {
        if (screen != null) {
            if (!ScreenMouseEvents.allowMouseDrag(screen).invoker().allowMouseDrag(screen, ctx, horizontalAmount, verticalAmount)) {
                return true;
            }
            ScreenMouseEvents.beforeMouseDrag(screen).invoker().beforeMouseDrag(screen, ctx, horizontalAmount, verticalAmount);
        }
        boolean result = ((Boolean) operation.call(new Object[]{screen, ctx, Double.valueOf(horizontalAmount), Double.valueOf(verticalAmount)})).booleanValue();
        if (screen != null) {
            result |= ScreenMouseEvents.afterMouseDrag(screen).invoker().afterMouseDrag(screen, ctx, horizontalAmount, verticalAmount, result);
        }
        return result;
    }

    @WrapOperation(method = {"method_1598(JDD)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_437;method_25401(DDDD)Z")})
    private boolean invokeMouseScrollEvents(class_437 screen, double mouseX, double mouseY, double horizontalAmount, double verticalAmount, Operation<Boolean> operation) {
        if (screen != null) {
            if (!ScreenMouseEvents.allowMouseScroll(screen).invoker().allowMouseScroll(screen, mouseX, mouseY, horizontalAmount, verticalAmount)) {
                return true;
            }
            ScreenMouseEvents.beforeMouseScroll(screen).invoker().beforeMouseScroll(screen, mouseX, mouseY, horizontalAmount, verticalAmount);
        }
        boolean result = ((Boolean) operation.call(new Object[]{screen, Double.valueOf(mouseX), Double.valueOf(mouseY), Double.valueOf(horizontalAmount), Double.valueOf(verticalAmount)})).booleanValue();
        if (screen != null) {
            result |= ScreenMouseEvents.afterMouseScroll(screen).invoker().afterMouseScroll(screen, mouseX, mouseY, horizontalAmount, verticalAmount, result);
        }
        return result;
    }
}
