package com.example.client.mixin;

import net.anubis.client.module.impl.render.character.CharacterHolder;
import net.anubis.client.module.impl.render.character.WornCharacter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10055;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:com/example/client/mixin/MixinCharacterAvatarRenderState.class */
@Mixin({class_10055.class})
@Environment(EnvType.CLIENT)
public abstract class MixinCharacterAvatarRenderState implements CharacterHolder {

    @Unique
    private WornCharacter anubis$character;

    @Unique
    private float anubis$seat;

    @Override // net.anubis.client.module.impl.render.character.CharacterHolder
    public WornCharacter anubis$character() {
        return this.anubis$character;
    }

    @Override // net.anubis.client.module.impl.render.character.CharacterHolder
    public void anubis$setCharacter(WornCharacter character) {
        this.anubis$character = character;
    }

    @Override // net.anubis.client.module.impl.render.character.CharacterHolder
    public float anubis$seat() {
        return this.anubis$seat;
    }

    @Override // net.anubis.client.module.impl.render.character.CharacterHolder
    public void anubis$setSeat(float knee) {
        this.anubis$seat = knee;
    }
}
