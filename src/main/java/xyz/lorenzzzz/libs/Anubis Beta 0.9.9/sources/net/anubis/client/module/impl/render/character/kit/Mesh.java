package net.anubis.client.module.impl.render.character.kit;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Mesh.class */
@Environment(EnvType.CLIENT)
final class Mesh {
    final float[] pos;
    final int vertexCount;
    final int[] tris;
    final int triCount;

    Mesh(float[] pos, int vertexCount, int[] tris, int triCount) {
        this.pos = pos;
        this.vertexCount = vertexCount;
        this.tris = tris;
        this.triCount = triCount;
    }
}
