package com.example.client.mixin;

import net.anubis.client.gui.WindowIcon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1041;
import net.minecraft.class_3262;
import net.minecraft.class_8518;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinWindow.class */
@Mixin({class_1041.class})
@Environment(EnvType.CLIENT)
public abstract class MixinWindow {
    @Shadow
    public abstract long method_4490();

    @Inject(method = {"method_4491"}, at = {@At("TAIL")})
    private void anubis$setIcon(class_3262 resources, class_8518 iconSet, CallbackInfo ci) {
        WindowIcon.apply(method_4490());
    }
}
