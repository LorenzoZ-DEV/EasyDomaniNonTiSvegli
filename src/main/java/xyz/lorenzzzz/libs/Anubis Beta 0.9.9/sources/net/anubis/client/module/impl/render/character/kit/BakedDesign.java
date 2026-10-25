package net.anubis.client.module.impl.render.character.kit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/BakedDesign.class */
@Environment(EnvType.CLIENT)
public final class BakedDesign extends CharacterDesign {
    private final Source source;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/BakedDesign$Source.class */
    @Environment(EnvType.CLIENT)
    public interface Source {
        InputStream open() throws IOException;
    }

    public BakedDesign(String id, String name, Source source) {
        super(id, name);
        this.source = source;
    }

    public static BakedDesign builtIn(String id, String name, String resource) {
        return new BakedDesign(id, name, () -> {
            InputStream in = BakedDesign.class.getResourceAsStream(resource);
            if (in == null) {
                throw new FileNotFoundException(resource);
            }
            return in;
        });
    }

    InputStream open() throws IOException {
        return this.source.open();
    }

    @Override // net.anubis.client.module.impl.render.character.kit.CharacterDesign
    protected void define(Rig rig) {
        throw new UnsupportedOperationException(id() + " is baked and has no rig");
    }
}
