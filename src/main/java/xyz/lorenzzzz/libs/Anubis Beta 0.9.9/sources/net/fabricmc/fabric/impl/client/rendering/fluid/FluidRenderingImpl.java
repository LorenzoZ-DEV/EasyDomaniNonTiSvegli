package net.fabricmc.fabric.impl.client.rendering.fluid;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRendering;
import net.minecraft.class_1920;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_3610;
import net.minecraft.class_4588;
import net.minecraft.class_775;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-rendering-fluids-v1-3.1.43+4ebb5c083e.jar:net/fabricmc/fabric/impl/client/rendering/fluid/FluidRenderingImpl.class */
@Environment(EnvType.CLIENT)
public class FluidRenderingImpl {
    private static final ThreadLocal<FluidRendering.DefaultRenderer> CURRENT_DEFAULT_RENDERER = new ThreadLocal<>();
    private static final ThreadLocal<FluidRenderHandlerInfo> CURRENT_INFO = ThreadLocal.withInitial(FluidRenderHandlerInfo::new);
    private static class_775 vanillaRenderer;

    public static void render(FluidRenderHandler handler, class_1920 world, class_2338 pos, class_4588 vertexConsumer, class_2680 blockState, class_3610 fluidState, FluidRendering.DefaultRenderer defaultRenderer) {
        CURRENT_DEFAULT_RENDERER.set(defaultRenderer);
        try {
            handler.renderFluid(pos, world, vertexConsumer, blockState, fluidState);
        } finally {
            CURRENT_DEFAULT_RENDERER.remove();
        }
    }

    public static void renderDefault(FluidRenderHandler handler, class_1920 world, class_2338 pos, class_4588 vertexConsumer, class_2680 blockState, class_3610 fluidState) {
        FluidRendering.DefaultRenderer renderer = CURRENT_DEFAULT_RENDERER.get();
        if (renderer != null) {
            renderer.render(handler, world, pos, vertexConsumer, blockState, fluidState);
        } else {
            renderVanillaDefault(handler, world, pos, vertexConsumer, blockState, fluidState);
        }
    }

    public static void renderVanillaDefault(FluidRenderHandler handler, class_1920 world, class_2338 pos, class_4588 vertexConsumer, class_2680 blockState, class_3610 fluidState) {
        FluidRenderHandlerInfo info = CURRENT_INFO.get();
        info.setup(handler, world, pos, fluidState);
        try {
            vanillaRenderer.method_3347(world, pos, vertexConsumer, blockState, fluidState);
        } finally {
            info.clear();
        }
    }

    public static void setVanillaRenderer(class_775 vanillaRenderer2) {
        vanillaRenderer = vanillaRenderer2;
    }

    public static FluidRenderHandlerInfo getCurrentInfo() {
        return CURRENT_INFO.get();
    }
}
