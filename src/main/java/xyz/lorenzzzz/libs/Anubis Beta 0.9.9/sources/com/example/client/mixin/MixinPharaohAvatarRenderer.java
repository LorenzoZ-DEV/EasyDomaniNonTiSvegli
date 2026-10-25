package com.example.client.mixin;

import net.anubis.client.module.impl.render.PharaohHeaddressLayer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10017;
import net.minecraft.class_10042;
import net.minecraft.class_10055;
import net.minecraft.class_1007;
import net.minecraft.class_11890;
import net.minecraft.class_11901;
import net.minecraft.class_5617;
import net.minecraft.class_591;
import net.minecraft.class_922;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinPharaohAvatarRenderer.class */
@Mixin({class_1007.class})
@Environment(EnvType.CLIENT)
public abstract class MixinPharaohAvatarRenderer<T extends class_11890 & class_11901> extends class_922<T, class_10055, class_591> {
    protected /* bridge */ /* synthetic */ float method_55831(class_10017 class_10017Var) {
        return super.method_55832((class_10042) class_10017Var);
    }

    protected MixinPharaohAvatarRenderer(class_5617.class_5618 context, class_591 model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = {"<init>"}, at = {@At("TAIL")})
    private void anubis$addHeadCosmetics(class_5617.class_5618 context, boolean slim, CallbackInfo ci) {
        method_4046(new PharaohHeaddressLayer(this));
    }
}
