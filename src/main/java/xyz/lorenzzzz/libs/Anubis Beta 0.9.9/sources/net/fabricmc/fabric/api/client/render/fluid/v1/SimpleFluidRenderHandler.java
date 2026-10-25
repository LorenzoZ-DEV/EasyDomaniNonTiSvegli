package net.fabricmc.fabric.api.client.render.fluid.v1;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1058;
import net.minecraft.class_1059;
import net.minecraft.class_1920;
import net.minecraft.class_2338;
import net.minecraft.class_2960;
import net.minecraft.class_3610;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-rendering-fluids-v1-3.1.43+4ebb5c083e.jar:net/fabricmc/fabric/api/client/render/fluid/v1/SimpleFluidRenderHandler.class */
@Environment(EnvType.CLIENT)
public class SimpleFluidRenderHandler implements FluidRenderHandler {
    public static final class_2960 WATER_STILL = class_2960.method_60656("block/water_still");
    public static final class_2960 WATER_FLOWING = class_2960.method_60656("block/water_flow");
    public static final class_2960 WATER_OVERLAY = class_2960.method_60656("block/water_overlay");
    public static final class_2960 LAVA_STILL = class_2960.method_60656("block/lava_still");
    public static final class_2960 LAVA_FLOWING = class_2960.method_60656("block/lava_flow");
    protected final class_2960 stillTexture;
    protected final class_2960 flowingTexture;
    protected final class_2960 overlayTexture;
    protected final class_1058[] sprites;
    protected final int tint;

    public SimpleFluidRenderHandler(class_2960 stillTexture, class_2960 flowingTexture, class_2960 overlayTexture, int tint) {
        this.stillTexture = (class_2960) Objects.requireNonNull(stillTexture, "stillTexture");
        this.flowingTexture = (class_2960) Objects.requireNonNull(flowingTexture, "flowingTexture");
        this.overlayTexture = overlayTexture;
        this.sprites = new class_1058[overlayTexture == null ? 2 : 3];
        this.tint = tint;
    }

    public SimpleFluidRenderHandler(class_2960 stillTexture, class_2960 flowingTexture, class_2960 overlayTexture) {
        this(stillTexture, flowingTexture, overlayTexture, -1);
    }

    public SimpleFluidRenderHandler(class_2960 stillTexture, class_2960 flowingTexture, int tint) {
        this(stillTexture, flowingTexture, null, tint);
    }

    public SimpleFluidRenderHandler(class_2960 stillTexture, class_2960 flowingTexture) {
        this(stillTexture, flowingTexture, null, -1);
    }

    public static SimpleFluidRenderHandler coloredWater(int tint) {
        return new SimpleFluidRenderHandler(WATER_STILL, WATER_FLOWING, WATER_OVERLAY, tint);
    }

    @Override // net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler
    public class_1058[] getFluidSprites(class_1920 view, class_2338 pos, class_3610 state) {
        return this.sprites;
    }

    @Override // net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler
    public void reloadTextures(class_1059 textureAtlas) {
        this.sprites[0] = textureAtlas.method_4608(this.stillTexture);
        this.sprites[1] = textureAtlas.method_4608(this.flowingTexture);
        if (this.overlayTexture != null) {
            this.sprites[2] = textureAtlas.method_4608(this.overlayTexture);
        }
    }

    @Override // net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler
    public int getFluidColor(class_1920 view, class_2338 pos, class_3610 state) {
        return this.tint;
    }
}
