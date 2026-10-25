package net.fabricmc.fabric.impl.client.rendering.fluid;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.class_1058;
import net.minecraft.class_1059;
import net.minecraft.class_10725;
import net.minecraft.class_1163;
import net.minecraft.class_1920;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2373;
import net.minecraft.class_2397;
import net.minecraft.class_310;
import net.minecraft.class_3610;
import net.minecraft.class_3611;
import net.minecraft.class_3612;
import net.minecraft.class_775;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-rendering-fluids-v1-3.1.43+4ebb5c083e.jar:net/fabricmc/fabric/impl/client/rendering/fluid/FluidRenderHandlerRegistryImpl.class */
@Environment(EnvType.CLIENT)
public class FluidRenderHandlerRegistryImpl implements FluidRenderHandlerRegistry {
    private final Map<class_3611, FluidRenderHandler> handlers = new IdentityHashMap();
    private final Map<class_3611, FluidRenderHandler> modHandlers = new IdentityHashMap();
    private final Object2BooleanMap<class_2248> transparencyForOverlay = new Object2BooleanOpenHashMap();

    public FluidRenderHandlerRegistryImpl() {
        this.handlers.put(class_3612.field_15910, WaterRenderHandler.INSTANCE);
        this.handlers.put(class_3612.field_15909, WaterRenderHandler.INSTANCE);
        this.handlers.put(class_3612.field_15908, LavaRenderHandler.INSTANCE);
        this.handlers.put(class_3612.field_15907, LavaRenderHandler.INSTANCE);
    }

    @Override // net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
    public FluidRenderHandler get(class_3611 fluid) {
        return this.handlers.get(fluid);
    }

    @Override // net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
    public FluidRenderHandler getOverride(class_3611 fluid) {
        return this.modHandlers.get(fluid);
    }

    @Override // net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
    public void register(class_3611 fluid, FluidRenderHandler renderer) {
        this.handlers.put(fluid, renderer);
        this.modHandlers.put(fluid, renderer);
    }

    @Override // net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
    public void setBlockTransparency(class_2248 block, boolean transparent) {
        this.transparencyForOverlay.put(block, transparent);
    }

    @Override // net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
    public boolean isBlockTransparent(class_2248 block) {
        return this.transparencyForOverlay.getOrDefault(block, (block instanceof class_2373) || (block instanceof class_2397));
    }

    public void onFluidRendererReload(class_775 renderer, class_1058[] waterSprites, class_1058[] lavaSprites, class_1058 waterOverlay) {
        FluidRenderingImpl.setVanillaRenderer(renderer);
        WaterRenderHandler.INSTANCE.updateSprites(waterSprites, waterOverlay);
        LavaRenderHandler.INSTANCE.updateSprites(lavaSprites);
        class_1059 texture = class_310.method_1551().method_72703().method_73025(class_10725.field_56382);
        for (FluidRenderHandler handler : this.handlers.values()) {
            handler.reloadTextures(texture);
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-rendering-fluids-v1-3.1.43+4ebb5c083e.jar:net/fabricmc/fabric/impl/client/rendering/fluid/FluidRenderHandlerRegistryImpl$WaterRenderHandler.class */
    @Environment(EnvType.CLIENT)
    private static class WaterRenderHandler implements FluidRenderHandler {
        public static final WaterRenderHandler INSTANCE = new WaterRenderHandler();
        private static final int DEFAULT_WATER_COLOR = 4159204;
        private final class_1058[] sprites = new class_1058[3];

        private WaterRenderHandler() {
        }

        @Override // net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler
        public class_1058[] getFluidSprites(class_1920 view, class_2338 pos, class_3610 state) {
            return this.sprites;
        }

        @Override // net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler
        public int getFluidColor(class_1920 view, class_2338 pos, class_3610 state) {
            if (view != null && pos != null) {
                return class_1163.method_4961(view, pos);
            }
            return DEFAULT_WATER_COLOR;
        }

        public void updateSprites(class_1058[] waterSprites, class_1058 waterOverlay) {
            this.sprites[0] = waterSprites[0];
            this.sprites[1] = waterSprites[1];
            this.sprites[2] = waterOverlay;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-rendering-fluids-v1-3.1.43+4ebb5c083e.jar:net/fabricmc/fabric/impl/client/rendering/fluid/FluidRenderHandlerRegistryImpl$LavaRenderHandler.class */
    @Environment(EnvType.CLIENT)
    private static class LavaRenderHandler implements FluidRenderHandler {
        public static final LavaRenderHandler INSTANCE = new LavaRenderHandler();
        private class_1058[] sprites;

        private LavaRenderHandler() {
        }

        @Override // net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler
        public class_1058[] getFluidSprites(class_1920 view, class_2338 pos, class_3610 state) {
            return this.sprites;
        }

        public void updateSprites(class_1058[] lavaSprites) {
            this.sprites = lavaSprites;
        }
    }
}
