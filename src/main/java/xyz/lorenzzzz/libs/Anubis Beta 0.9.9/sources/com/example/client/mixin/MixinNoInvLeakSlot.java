package com.example.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.anubis.client.module.impl.misc.NoInvLeakModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1661;
import net.minecraft.class_1735;
import net.minecraft.class_2960;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinNoInvLeakSlot.class */
@Mixin({class_1735.class})
@Environment(EnvType.CLIENT)
public abstract class MixinNoInvLeakSlot {
    @ModifyReturnValue(method = {"method_7679"}, at = {@At("RETURN")})
    private class_2960 anubis$noInvLeakSilhouette(class_2960 icon) {
        class_1735 self = (class_1735) this;
        if (icon != null && NoInvLeakModule.overlaying() && (self.field_7871 instanceof class_1661) && self.method_7681()) {
            return null;
        }
        return icon;
    }
}
