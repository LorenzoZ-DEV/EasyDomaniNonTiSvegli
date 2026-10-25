package net.anubis.client.module.impl.render.character.kit;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/CharacterDesign.class */
@Environment(EnvType.CLIENT)
public abstract class CharacterDesign {
    private final String id;
    private final String name;

    protected abstract void define(Rig rig);

    protected CharacterDesign(String id, String name) {
        if (id == null || !id.matches("[a-z0-9_]+")) {
            throw new IllegalArgumentException("character id must match [a-z0-9_]+: " + id);
        }
        this.id = id;
        this.name = name;
    }

    public final String id() {
        return this.id;
    }

    public final String name() {
        return this.name;
    }

    public final Rig rig() {
        Rig rig = new Rig();
        define(rig);
        return rig;
    }
}
