package net.fabricmc.fabric.api.client.render.fluid.v1;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.rendering.fluid.FluidRenderingImpl;
import net.minecraft.class_1920;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_3610;
import net.minecraft.class_4588;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-rendering-fluids-v1-3.1.43+4ebb5c083e.jar:net/fabricmc/fabric/api/client/render/fluid/v1/FluidRendering.class */
@Environment(EnvType.CLIENT)
public final class FluidRendering {
    private FluidRendering() {
    }

    public static void render(FluidRenderHandler handler, class_1920 world, class_2338 pos, class_4588 vertexConsumer, class_2680 blockState, class_3610 fluidState, DefaultRenderer defaultRenderer) {
        FluidRenderingImpl.render(handler, world, pos, vertexConsumer, blockState, fluidState, defaultRenderer);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-rendering-fluids-v1-3.1.43+4ebb5c083e.jar:net/fabricmc/fabric/api/client/render/fluid/v1/FluidRendering$DefaultRenderer.class */
    @Environment(EnvType.CLIENT)
    public interface DefaultRenderer {
        default void render(FluidRenderHandler handler, class_1920 world, class_2338 pos, class_4588 vertexConsumer, class_2680 blockState, class_3610 fluidState) {
            FluidRenderingImpl.renderVanillaDefault(handler, world, pos, vertexConsumer, blockState, fluidState);
        }
    }
}
