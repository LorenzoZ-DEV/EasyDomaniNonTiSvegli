package com.example.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.anubis.client.module.impl.movement.BoatFlyModule;
import net.anubis.client.module.impl.movement.EntityControlModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1316;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinEntityControlLocalPlayer.class */
@Mixin({class_746.class})
@Environment(EnvType.CLIENT)
public abstract class MixinEntityControlLocalPlayer {
    @ModifyReturnValue(method = {"method_3151"}, at = {@At("RETURN")})
    private float anubis$entityControlMaxJump(float original) {
        if (EntityControlModule.maxJump()) {
            return 1.0f;
        }
        return original;
    }

    @ModifyReturnValue(method = {"method_45773"}, at = {@At("RETURN")})
    private class_1316 anubis$boatFlyCancelJump(class_1316 original) {
        if (original == null || !BoatFlyModule.cancelsMountJump((class_746) this)) {
            return original;
        }
        return null;
    }
}
