package net.fabricmc.fabric.impl.client.rendering.fluid;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.minecraft.class_1058;
import net.minecraft.class_1920;
import net.minecraft.class_2338;
import net.minecraft.class_3610;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-rendering-fluids-v1-3.1.43+4ebb5c083e.jar:net/fabricmc/fabric/impl/client/rendering/fluid/FluidRenderHandlerInfo.class */
@Environment(EnvType.CLIENT)
public class FluidRenderHandlerInfo {
    public final class_1058[] sprites = new class_1058[2];
    public FluidRenderHandler handler;
    public boolean hasOverlay;
    public class_1058 overlaySprite;

    public void setup(FluidRenderHandler handler, class_1920 world, class_2338 pos, class_3610 fluidState) {
        this.handler = handler;
        class_1058[] sprites = handler.getFluidSprites(world, pos, fluidState);
        this.sprites[0] = sprites[0];
        this.sprites[1] = sprites[1];
        if (sprites.length > 2) {
            this.hasOverlay = true;
            this.overlaySprite = sprites[2];
        }
    }

    public void clear() {
        this.sprites[0] = null;
        this.sprites[1] = null;
        this.handler = null;
        this.hasOverlay = false;
        this.overlaySprite = null;
    }
}
