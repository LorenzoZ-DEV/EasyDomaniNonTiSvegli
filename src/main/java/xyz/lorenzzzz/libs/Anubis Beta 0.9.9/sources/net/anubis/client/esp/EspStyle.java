package net.anubis.client.esp;

import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.module.setting.Setting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/esp/EspStyle.class */
@Environment(EnvType.CLIENT)
public final class EspStyle {
    public static final String OUTLINE = "Outline";
    public static final String BOXES = "Boxes";
    public final ModeSetting shape;
    public final NumberSetting boxSize;
    public final NumberSetting fillAlpha;
    public final NumberSetting lineWidth;
    public final BooleanSetting glow;
    public final BooleanSetting fade;
    public final BooleanSetting reveal;
    public final NumberSetting range;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/esp/EspStyle$Registrar.class */
    @Environment(EnvType.CLIENT)
    public interface Registrar {
        <S extends Setting> S add(String str, S s);
    }

    public EspStyle(Registrar r, String defaultShape, float defaultFillAlpha, float defaultRange) {
        this.shape = (ModeSetting) r.add("shape", new ModeSetting("Shape", defaultShape, OUTLINE, BOXES));
        this.boxSize = (NumberSetting) r.add("box-size", new NumberSetting("Box Size", 1.0f, 0.1f, 2.0f).step(0.1f));
        this.fillAlpha = (NumberSetting) r.add("fill-alpha", new NumberSetting("Fill Alpha", defaultFillAlpha, 0.0f, 1.0f).step(0.05f));
        this.lineWidth = (NumberSetting) r.add("line-width", new NumberSetting("Line Width", 1.5f, 0.5f, 5.0f).step(0.5f));
        this.glow = (BooleanSetting) r.add("glow", new BooleanSetting("Glow", true));
        this.fade = (BooleanSetting) r.add("distance-fade", new BooleanSetting("Distance Fade", true));
        this.reveal = (BooleanSetting) r.add("reveal", new BooleanSetting("Reveal Animation", true));
        this.range = (NumberSetting) r.add("range", new NumberSetting("Range", defaultRange, 16.0f, 2048.0f).step(1.0f));
        this.boxSize.visibleWhen(() -> {
            return this.shape.is(BOXES);
        });
    }

    public boolean outline() {
        return this.shape.is(OUTLINE);
    }
}
