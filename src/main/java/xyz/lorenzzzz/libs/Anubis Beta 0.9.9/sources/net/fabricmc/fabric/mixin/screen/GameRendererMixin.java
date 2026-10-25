package net.fabricmc.fabric.mixin.screen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_757;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/mixin/screen/GameRendererMixin.class */
@Mixin({class_757.class})
@Environment(EnvType.CLIENT)
abstract class GameRendererMixin {
    GameRendererMixin() {
    }

    @WrapOperation(method = {"method_3192(Lnet/minecraft/class_9779;Z)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_437;method_47413(Lnet/minecraft/class_332;IIF)V")})
    private void onRenderScreen(class_437 currentScreen, class_332 drawContext, int mouseX, int mouseY, float tickDelta, Operation<Void> operation) {
        ScreenEvents.beforeRender(currentScreen).invoker().beforeRender(currentScreen, drawContext, mouseX, mouseY, tickDelta);
        operation.call(new Object[]{currentScreen, drawContext, Integer.valueOf(mouseX), Integer.valueOf(mouseY), Float.valueOf(tickDelta)});
        ScreenEvents.afterRender(currentScreen).invoker().afterRender(currentScreen, drawContext, mouseX, mouseY, tickDelta);
    }
}
