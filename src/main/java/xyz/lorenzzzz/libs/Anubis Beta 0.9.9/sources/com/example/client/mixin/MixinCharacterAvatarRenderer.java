package com.example.client.mixin;

import net.anubis.client.module.impl.render.character.CharacterLayer;
import net.anubis.client.module.impl.render.character.CharacterModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10017;
import net.minecraft.class_10042;
import net.minecraft.class_10055;
import net.minecraft.class_1007;
import net.minecraft.class_11659;
import net.minecraft.class_11890;
import net.minecraft.class_11901;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import net.minecraft.class_5617;
import net.minecraft.class_591;
import net.minecraft.class_630;
import net.minecraft.class_922;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinCharacterAvatarRenderer.class */
@Mixin({class_1007.class})
@Environment(EnvType.CLIENT)
public abstract class MixinCharacterAvatarRenderer<T extends class_11890 & class_11901> extends class_922<T, class_10055, class_591> {
    protected /* bridge */ /* synthetic */ float method_55831(class_10017 class_10017Var) {
        return super.method_55832((class_10042) class_10017Var);
    }

    protected MixinCharacterAvatarRenderer(class_5617.class_5618 context, class_591 model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = {"<init>"}, at = {@At("TAIL")})
    private void anubis$addCharacterLayer(class_5617.class_5618 context, boolean slim, CallbackInfo ci) {
        method_4046(new CharacterLayer(this));
    }

    @Inject(method = {"method_62604"}, at = {@At("TAIL")})
    private void anubis$extractCharacter(class_11890 entity, class_10055 state, float partialTick, CallbackInfo ci) {
        CharacterModule.extract(entity, state);
    }

    @Inject(method = {"method_23205"}, at = {@At("HEAD")}, cancellable = true)
    private void anubis$characterFirstPersonHand(class_4587 poseStack, class_11659 collector, int light, class_2960 skin, class_630 arm, boolean sleeve, CallbackInfo ci) {
        if (CharacterLayer.firstPersonHand(method_4038(), poseStack, collector, light, arm)) {
            ci.cancel();
        }
    }
}
