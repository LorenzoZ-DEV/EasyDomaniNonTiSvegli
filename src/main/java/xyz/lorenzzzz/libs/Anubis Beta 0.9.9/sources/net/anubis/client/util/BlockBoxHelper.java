package net.anubis.client.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2338;
import net.minecraft.class_238;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/BlockBoxHelper.class */
@Environment(EnvType.CLIENT)
public final class BlockBoxHelper {
    private static final double HALF_BLOCK = 0.5d;
    private static final float MIN_SIZE = 0.1f;
    private static final float MAX_SIZE = 2.0f;

    private BlockBoxHelper() {
    }

    public static class_238 scaledBlockBox(class_2338 pos, float size) {
        float clamped = Math.max(MIN_SIZE, Math.min(MAX_SIZE, size));
        double halfSize = ((double) clamped) * HALF_BLOCK;
        double centerX = ((double) pos.method_10263()) + HALF_BLOCK;
        double centerY = ((double) pos.method_10264()) + HALF_BLOCK;
        double centerZ = ((double) pos.method_10260()) + HALF_BLOCK;
        return new class_238(centerX - halfSize, centerY - halfSize, centerZ - halfSize, centerX + halfSize, centerY + halfSize, centerZ + halfSize);
    }
}
