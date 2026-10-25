package net.anubis.client.module.impl.render.character;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/CharacterHolder.class */
@Environment(EnvType.CLIENT)
public interface CharacterHolder {
    WornCharacter anubis$character();

    void anubis$setCharacter(WornCharacter wornCharacter);

    float anubis$seat();

    void anubis$setSeat(float f);
}
