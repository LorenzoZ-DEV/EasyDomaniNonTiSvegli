package net.anubis.client.module.impl.client;

import net.anubis.client.gui.HudLayoutStore;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1657;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/client/RadarModule.class */
@Environment(EnvType.CLIENT)
public final class RadarModule extends ClientModule {
    private final NumberSetting range;
    private final NumberSetting size;
    private final BooleanSetting rotate;
    private final ColorSetting dotColor;

    public RadarModule() {
        super(HudLayoutStore.RADAR_ID, "Radar", "A round radar of the players around you.", ModuleCategory.CLIENT);
        this.range = (NumberSetting) addSetting("range", new NumberSetting("Range", 48.0f, 8.0f, 256.0f).step(1.0f));
        this.size = (NumberSetting) addSetting("size", new NumberSetting("Size", 150.0f, 80.0f, 320.0f).step(1.0f));
        this.rotate = (BooleanSetting) addSetting("rotate", new BooleanSetting("Rotate", true));
        this.dotColor = (ColorSetting) addSetting("dot-color", new ColorSetting("Player Color", -47814));
    }

    public static RadarModule get() {
        return (RadarModule) ModuleRegistry.get(RadarModule.class);
    }

    public static boolean shouldShowHud() {
        RadarModule module = get();
        return module != null && module.isEnabled();
    }

    public float range() {
        return this.range.get();
    }

    public float size() {
        return this.size.get();
    }

    public boolean rotate() {
        return this.rotate.get();
    }

    public int dotColor() {
        return this.dotColor.argb();
    }

    public boolean shows(class_1657 player) {
        return ((player instanceof class_746) || player.method_31481() || player.method_29504() || player.method_7325()) ? false : true;
    }
}
