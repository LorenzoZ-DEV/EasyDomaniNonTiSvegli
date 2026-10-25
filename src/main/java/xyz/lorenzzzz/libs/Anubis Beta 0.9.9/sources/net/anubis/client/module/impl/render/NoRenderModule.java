package net.anubis.client.module.impl.render;

import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/NoRenderModule.class */
@Environment(EnvType.CLIENT)
public final class NoRenderModule extends ClientModule {
    private static NoRenderModule instance;
    private final BooleanSetting sFireOverlay;
    private final BooleanSetting sTotemAnimation;
    private final BooleanSetting sPumpkinPowderSnow;
    private final BooleanSetting sNauseaPortal;
    private final BooleanSetting sBlindnessDarkness;
    private final BooleanSetting sSpawnerMob;
    private final BooleanSetting sFallingBlocks;
    private final BooleanSetting sWeather;
    private final BooleanSetting sLavaFog;
    private final BooleanSetting sEntityFire;

    public NoRenderModule() {
        super("no-render", "No Render", "Hide selected overlays, screen effects and entity renders.", ModuleCategory.RENDER);
        this.sFireOverlay = (BooleanSetting) addSetting("fire-overlay", new BooleanSetting("Fire Overlay", true));
        this.sTotemAnimation = (BooleanSetting) addSetting("totem-animation", new BooleanSetting("Totem Animation", true));
        this.sPumpkinPowderSnow = (BooleanSetting) addSetting("pumpkin-powder-snow", new BooleanSetting("Pumpkin & Powder Snow", true));
        this.sNauseaPortal = (BooleanSetting) addSetting("nausea-portal", new BooleanSetting("Nausea & Portal", true));
        this.sBlindnessDarkness = (BooleanSetting) addSetting("blindness-darkness", new BooleanSetting("Blindness & Darkness", true));
        this.sSpawnerMob = (BooleanSetting) addSetting("spawner-mob", new BooleanSetting("Spawner Mob", true));
        this.sFallingBlocks = (BooleanSetting) addSetting("falling-blocks", new BooleanSetting("Falling Blocks", false));
        this.sWeather = (BooleanSetting) addSetting("weather", new BooleanSetting("Weather", true));
        this.sLavaFog = (BooleanSetting) addSetting("lava-fog", new BooleanSetting("Lava Fog", true));
        this.sEntityFire = (BooleanSetting) addSetting("entity-fire", new BooleanSetting("Entity Fire", false));
        instance = this;
    }

    public static boolean hideFireOverlay() {
        NoRenderModule module = instance;
        return module != null && module.isEnabled() && module.sFireOverlay.get();
    }

    public static boolean hideTotemAnimation() {
        NoRenderModule module = instance;
        return module != null && module.isEnabled() && module.sTotemAnimation.get();
    }

    public static boolean hidePumpkinPowderSnow() {
        NoRenderModule module = instance;
        return module != null && module.isEnabled() && module.sPumpkinPowderSnow.get();
    }

    public static boolean hideNauseaPortal() {
        NoRenderModule module = instance;
        return module != null && module.isEnabled() && module.sNauseaPortal.get();
    }

    public static boolean hideBlindnessDarkness() {
        NoRenderModule module = instance;
        return module != null && module.isEnabled() && module.sBlindnessDarkness.get();
    }

    public static boolean hideSpawnerMob() {
        NoRenderModule module = instance;
        return module != null && module.isEnabled() && module.sSpawnerMob.get();
    }

    public static boolean hideFallingBlocks() {
        NoRenderModule module = instance;
        return module != null && module.isEnabled() && module.sFallingBlocks.get();
    }

    public static boolean hideWeather() {
        NoRenderModule module = instance;
        return module != null && module.isEnabled() && module.sWeather.get();
    }

    public static boolean hideLavaFog() {
        NoRenderModule module = instance;
        return module != null && module.isEnabled() && module.sLavaFog.get();
    }

    public static boolean hideEntityFire() {
        NoRenderModule module = instance;
        return module != null && module.isEnabled() && module.sEntityFire.get();
    }
}
