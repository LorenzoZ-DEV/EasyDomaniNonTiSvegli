package com.example.client.mixin;

import net.anubis.client.module.impl.misc.SpamModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2678;
import net.minecraft.class_634;
import net.minecraft.class_638;
import net.minecraft.class_8588;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinSpamClientPacketListener.class */
@Mixin({class_634.class})
@Environment(EnvType.CLIENT)
public abstract class MixinSpamClientPacketListener {

    @Shadow
    private class_638 field_3699;

    @Inject(method = {"method_11120"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_2600;method_11074(Lnet/minecraft/class_2596;Lnet/minecraft/class_2547;Lnet/minecraft/class_11980;)V", shift = At.Shift.AFTER)})
    private void anubis$spamRelogin(class_2678 packet, CallbackInfo ci) {
        if (SpamModule.active == null || this.field_3699 == null) {
            return;
        }
        SpamModule.onGameLeft();
    }

    @Inject(method = {"method_52798"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_2600;method_11074(Lnet/minecraft/class_2596;Lnet/minecraft/class_2547;Lnet/minecraft/class_11980;)V", shift = At.Shift.AFTER)})
    private void anubis$spamReconfiguration(class_8588 packet, CallbackInfo ci) {
        if (SpamModule.active != null) {
            SpamModule.onGameLeft();
        }
    }
}
