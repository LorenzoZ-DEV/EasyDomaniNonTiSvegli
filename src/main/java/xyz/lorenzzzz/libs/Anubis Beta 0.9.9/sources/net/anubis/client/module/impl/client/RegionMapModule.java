package net.anubis.client.module.impl.client;

import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/client/RegionMapModule.class */
@Environment(EnvType.CLIENT)
public final class RegionMapModule extends ClientModule {
    private final NumberSetting cellSize;
    private final NumberSetting cellGap;
    private final NumberSetting playerScale;
    private final NumberSetting textScale;
    private final NumberSetting opacity;
    private final BooleanSetting shadow;
    private final BooleanSetting showRegionIds;
    private final BooleanSetting showLegend;
    private final BooleanSetting showCoordinates;
    private final ColorSetting playerIndicatorColor;

    public RegionMapModule() {
        super("region-map", "Region Map", "DonutSMP region map with directional indicator.", ModuleCategory.CLIENT);
        this.cellSize = (NumberSetting) addSetting("cell-size", new NumberSetting("Cell Size", 22.0f, 10.0f, 50.0f).step(1.0f));
        this.cellGap = (NumberSetting) addSetting("cell-gap", new NumberSetting("Cell Gap", 2.0f, 0.0f, 10.0f).step(1.0f));
        this.playerScale = (NumberSetting) addSetting("player-scale", new NumberSetting("Player Scale", 1.0f, 0.5f, 3.0f).step(0.1f));
        this.textScale = (NumberSetting) addSetting("text-scale", new NumberSetting("Text Scale", 1.0f, 0.1f, 2.0f).step(0.1f));
        this.opacity = (NumberSetting) addSetting("opacity", new NumberSetting("Opacity", 1.0f, 0.1f, 1.0f).step(0.05f));
        this.shadow = (BooleanSetting) addSetting("shadow", new BooleanSetting("Shadow", true));
        this.showRegionIds = (BooleanSetting) addSetting("show-region-ids", new BooleanSetting("Show Region IDs", true));
        this.showLegend = (BooleanSetting) addSetting("show-legend", new BooleanSetting("Show Legend", true));
        this.showCoordinates = (BooleanSetting) addSetting("show-coordinates", new BooleanSetting("Show Coordinates", true));
        this.playerIndicatorColor = (ColorSetting) addSetting(new ColorSetting("Player Indicator", -52686));
    }

    public float cellSize() {
        return this.cellSize.get();
    }

    public float cellGap() {
        return this.cellGap.get();
    }

    public float playerScale() {
        return this.playerScale.get();
    }

    public float textScale() {
        return this.textScale.get();
    }

    public float opacity() {
        return this.opacity.get();
    }

    public boolean shadow() {
        return this.shadow.get();
    }

    public boolean showRegionIds() {
        return this.showRegionIds.get();
    }

    public boolean showLegend() {
        return this.showLegend.get();
    }

    public boolean showCoordinates() {
        return this.showCoordinates.get();
    }

    public ColorSetting playerIndicatorColor() {
        return this.playerIndicatorColor;
    }
}
