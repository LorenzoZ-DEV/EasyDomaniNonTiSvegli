package com.example.client.mixin;

import net.anubis.client.module.impl.render.character.CharacterLighting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_308;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinCharacterLighting.class */
@Mixin({class_308.class})
@Environment(EnvType.CLIENT)
public abstract class MixinCharacterLighting {
    @Inject(method = {"method_71034"}, at = {@At("HEAD")})
    private void anubis$trackCharacterLighting(class_308.class_11274 entry, CallbackInfo ci) {
        CharacterLighting.setup(entry);
    }
}
