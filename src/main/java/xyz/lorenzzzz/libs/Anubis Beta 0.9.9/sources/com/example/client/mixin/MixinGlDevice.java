package com.example.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10874;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinGlDevice.class */
@Mixin({class_10874.class})
@Environment(EnvType.CLIENT)
public class MixinGlDevice {
    @Redirect(method = {"method_68813"}, at = @At(value = "FIELD", target = "Lnet/minecraft/class_10865;field_58039:Z", opcode = 178))
    private static boolean anubis$blockDsaForAmd() {
        String renderer = GL11.glGetString(7937);
        return renderer == null || !renderer.contains("AMD");
    }
}
