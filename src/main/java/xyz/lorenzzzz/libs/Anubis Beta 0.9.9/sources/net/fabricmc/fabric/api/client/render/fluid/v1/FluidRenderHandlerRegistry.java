package net.fabricmc.fabric.api.client.render.fluid.v1;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.rendering.fluid.FluidRenderHandlerRegistryImpl;
import net.minecraft.class_2248;
import net.minecraft.class_3611;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-rendering-fluids-v1-3.1.43+4ebb5c083e.jar:net/fabricmc/fabric/api/client/render/fluid/v1/FluidRenderHandlerRegistry.class */
@Environment(EnvType.CLIENT)
public interface FluidRenderHandlerRegistry {
    public static final FluidRenderHandlerRegistry INSTANCE = new FluidRenderHandlerRegistryImpl();

    FluidRenderHandler get(class_3611 class_3611Var);

    FluidRenderHandler getOverride(class_3611 class_3611Var);

    void register(class_3611 class_3611Var, FluidRenderHandler fluidRenderHandler);

    void setBlockTransparency(class_2248 class_2248Var, boolean z);

    boolean isBlockTransparent(class_2248 class_2248Var);

    default void register(class_3611 still, class_3611 flow, FluidRenderHandler renderer) {
        register(still, renderer);
        register(flow, renderer);
    }
}
