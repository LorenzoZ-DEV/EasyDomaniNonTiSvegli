package net.fabricmc.fabric.api.client.render.fluid.v1;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.rendering.fluid.FluidRenderingImpl;
import net.minecraft.class_1058;
import net.minecraft.class_1059;
import net.minecraft.class_1920;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_3610;
import net.minecraft.class_4588;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-rendering-fluids-v1-3.1.43+4ebb5c083e.jar:net/fabricmc/fabric/api/client/render/fluid/v1/FluidRenderHandler.class */
@Environment(EnvType.CLIENT)
public interface FluidRenderHandler {
    class_1058[] getFluidSprites(class_1920 class_1920Var, class_2338 class_2338Var, class_3610 class_3610Var);

    default int getFluidColor(class_1920 view, class_2338 pos, class_3610 state) {
        return -1;
    }

    default void renderFluid(class_2338 pos, class_1920 world, class_4588 vertexConsumer, class_2680 blockState, class_3610 fluidState) {
        FluidRenderingImpl.renderDefault(this, world, pos, vertexConsumer, blockState, fluidState);
    }

    default void reloadTextures(class_1059 textureAtlas) {
    }
}
