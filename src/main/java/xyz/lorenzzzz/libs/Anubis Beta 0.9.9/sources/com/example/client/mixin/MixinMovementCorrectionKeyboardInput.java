package com.example.client.mixin;

import net.anubis.client.util.pvp.InputControl;
import net.anubis.client.util.pvp.ServerRotation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10185;
import net.minecraft.class_241;
import net.minecraft.class_310;
import net.minecraft.class_743;
import net.minecraft.class_744;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinMovementCorrectionKeyboardInput.class */
@Mixin({class_743.class})
@Environment(EnvType.CLIENT)
public abstract class MixinMovementCorrectionKeyboardInput {
    @Inject(method = {"method_3129"}, at = {@At("TAIL")})
    private void anubis$shapeInput(CallbackInfo ci) {
        AccessorMovementClientInput accessorMovementClientInput = (class_744) this;
        class_746 player = class_310.method_1551().field_1724;
        if (player == null || player.field_3913 != accessorMovementClientInput) {
            return;
        }
        class_10185 keys = ((class_744) accessorMovementClientInput).field_54155;
        class_10185 corrected = InputControl.shape(player, ServerRotation.correctInput(keys));
        if (corrected == keys) {
            return;
        }
        ((class_744) accessorMovementClientInput).field_54155 = corrected;
        accessorMovementClientInput.anubis$setMoveVector(new class_241(impulse(corrected.comp_3161(), corrected.comp_3162()), impulse(corrected.comp_3159(), corrected.comp_3160())).method_35581());
    }

    private static float impulse(boolean positive, boolean negative) {
        if (positive == negative) {
            return 0.0f;
        }
        return positive ? 1.0f : -1.0f;
    }
}
