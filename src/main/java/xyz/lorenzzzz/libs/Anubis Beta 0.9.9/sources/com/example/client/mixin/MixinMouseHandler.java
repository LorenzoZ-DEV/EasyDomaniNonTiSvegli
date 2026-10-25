package com.example.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.anubis.client.event.impl.MouseInputEvent;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.impl.combat.AimAssistModule;
import net.anubis.client.module.impl.misc.FreecamModule;
import net.anubis.client.module.impl.render.FreelookModule;
import net.anubis.client.module.impl.render.ZoomModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11910;
import net.minecraft.class_310;
import net.minecraft.class_312;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinMouseHandler.class */
@Mixin({class_312.class})
@Environment(EnvType.CLIENT)
public abstract class MixinMouseHandler {
    @Inject(method = {"method_1601"}, at = {@At("HEAD")}, cancellable = true)
    private void anubis$onMouseButton(long window, class_11910 buttonInfo, int action, CallbackInfo ci) {
        if (MouseInputEvent.dispatch(window, buttonInfo.comp_4801(), action, buttonInfo.comp_4797())) {
            ci.cancel();
        }
    }

    @Inject(method = {"method_1598"}, at = {@At("HEAD")}, cancellable = true)
    private void anubis$wheel(long window, double horizontal, double vertical, CallbackInfo ci) {
        class_310 mc = class_310.method_1551();
        if (vertical != 0.0d && window == mc.method_22683().method_4490() && mc.field_1755 == null && mc.method_18506() == null) {
            if (ZoomModule.scroll(vertical) || FreecamModule.scroll(vertical)) {
                ci.cancel();
            }
        }
    }

    @WrapOperation(method = {"method_1606"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_746;method_5872(DD)V")})
    private void anubis$cameraTurn(class_746 player, double yRot, double xRot, Operation<Void> original, @Local(argsOnly = true) double movementTime) {
        double scale = ZoomModule.turnScale();
        double yRot2 = yRot * scale;
        double xRot2 = xRot * scale;
        FreecamModule freecam = FreecamModule.active;
        if (freecam != null) {
            freecam.turn(yRot2, xRot2);
            return;
        }
        FreelookModule freelook = (FreelookModule) ModuleRegistry.get(FreelookModule.class);
        if (freelook != null && freelook.cameraMode()) {
            freelook.turnCamera(yRot2, xRot2);
            return;
        }
        AimAssistModule assist = (AimAssistModule) ModuleRegistry.get(AimAssistModule.class);
        if (assist != null && assist.isEnabled()) {
            double[] turn = assist.nudge(player, yRot2, xRot2, movementTime);
            yRot2 += turn[0];
            xRot2 += turn[1];
        }
        original.call(new Object[]{player, Double.valueOf(yRot2), Double.valueOf(xRot2)});
    }
}
