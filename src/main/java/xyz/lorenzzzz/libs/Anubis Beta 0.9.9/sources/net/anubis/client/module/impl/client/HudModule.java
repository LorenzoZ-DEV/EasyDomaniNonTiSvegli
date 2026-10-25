package net.anubis.client.module.impl.client;

import java.util.List;
import java.util.Objects;
import net.anubis.client.gui.HudLayoutStore;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.Setting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/client/HudModule.class */
@Environment(EnvType.CLIENT)
public final class HudModule extends ClientModule {
    private final BooleanSetting coordinates;
    private final BooleanSetting netherCoordinates;
    private final BooleanSetting ping;
    private final BooleanSetting fps;
    private final BooleanSetting clock;
    private final BooleanSetting logo;
    private final BooleanSetting moduleList;
    private final BooleanSetting potions;
    private final BooleanSetting targetCard;
    private final BooleanSetting targetPlayersOnly;
    private final BooleanSetting targetOnLook;
    private final BooleanSetting targetGear;
    private final BooleanSetting armorStatus;
    private final ModeSetting armorLayout;
    private final ModeSetting armorWear;
    private final BooleanSetting armorHands;
    private final BooleanSetting itemCooldowns;
    private final ModeSetting cooldownDial;
    private final ModeSetting toastStyle;
    private final BooleanSetting shadow;
    private final BooleanSetting customAccent;
    private final ColorSetting accentColor;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/client/HudModule$ArmorWear.class */
    @Environment(EnvType.CLIENT)
    public enum ArmorWear {
        GAUGE,
        PERCENT,
        BOTH
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/client/HudModule$ToastStyle.class */
    @Environment(EnvType.CLIENT)
    public enum ToastStyle {
        BAR,
        CARD,
        CHIP
    }

    public HudModule() {
        super("hud", "HUD", "Module list, coordinates and status overlays.", ModuleCategory.CLIENT);
        this.coordinates = (BooleanSetting) addSetting(HudLayoutStore.COORDINATES_ID, new BooleanSetting("Overworld Coords", true));
        this.netherCoordinates = (BooleanSetting) addSetting("nether-coordinates", new BooleanSetting("Nether Coords", true));
        this.ping = (BooleanSetting) addSetting("ping", new BooleanSetting("Ping", true));
        this.fps = (BooleanSetting) addSetting("fps", new BooleanSetting("FPS", true));
        this.clock = (BooleanSetting) addSetting("clock", new BooleanSetting("Clock", true));
        this.logo = (BooleanSetting) addSetting("logo", new BooleanSetting("Logo", true));
        this.moduleList = (BooleanSetting) addSetting("module-list", new BooleanSetting("Module List", true));
        this.potions = (BooleanSetting) addSetting("potions", new BooleanSetting("Potions", true));
        this.targetCard = (BooleanSetting) addSetting("target-card", new BooleanSetting("Target HUD", true));
        this.targetPlayersOnly = (BooleanSetting) addSetting("target-players-only", new BooleanSetting("Players Only", false));
        this.targetOnLook = (BooleanSetting) addSetting("target-on-look", new BooleanSetting("Show On Look", false));
        this.targetGear = (BooleanSetting) addSetting("target-gear", new BooleanSetting("Show Gear", true));
        this.armorStatus = (BooleanSetting) addSetting("armor-status", new BooleanSetting("Armor HUD", true));
        this.armorLayout = (ModeSetting) addSetting("armor-layout", new ModeSetting("Armor Layout", "Row", "Row", "Column"));
        this.armorWear = (ModeSetting) addSetting("armor-wear", new ModeSetting("Wear As", "Gauge", "Gauge", "Percent", "Gauge + Percent"));
        this.armorHands = (BooleanSetting) addSetting("armor-hands", new BooleanSetting("Include Hands", false));
        this.itemCooldowns = (BooleanSetting) addSetting("item-cooldowns", new BooleanSetting("Cooldown", true));
        this.cooldownDial = (ModeSetting) addSetting("cooldown-dial", new ModeSetting("Cooldown Dial", "Halo", "Halo", "Sandbar"));
        this.toastStyle = (ModeSetting) addSetting("toast-style", new ModeSetting("Toast Style", "Bar", "Bar", "Card", "Chip"));
        this.shadow = (BooleanSetting) addSetting("shadow", new BooleanSetting("Shadow", true));
        this.customAccent = (BooleanSetting) addSetting("custom-accent", new BooleanSetting("Custom Accent", false));
        this.accentColor = (ColorSetting) addSetting("accent-color", new ColorSetting("Accent Color", -13338378));
        ColorSetting colorSetting = this.accentColor;
        BooleanSetting booleanSetting = this.customAccent;
        Objects.requireNonNull(booleanSetting);
        colorSetting.visibleWhen(booleanSetting::get);
        BooleanSetting booleanSetting2 = this.targetPlayersOnly;
        BooleanSetting booleanSetting3 = this.targetCard;
        Objects.requireNonNull(booleanSetting3);
        booleanSetting2.visibleWhen(booleanSetting3::get);
        BooleanSetting booleanSetting4 = this.targetOnLook;
        BooleanSetting booleanSetting5 = this.targetCard;
        Objects.requireNonNull(booleanSetting5);
        booleanSetting4.visibleWhen(booleanSetting5::get);
        BooleanSetting booleanSetting6 = this.targetGear;
        BooleanSetting booleanSetting7 = this.targetCard;
        Objects.requireNonNull(booleanSetting7);
        booleanSetting6.visibleWhen(booleanSetting7::get);
        ModeSetting modeSetting = this.armorLayout;
        BooleanSetting booleanSetting8 = this.armorStatus;
        Objects.requireNonNull(booleanSetting8);
        modeSetting.visibleWhen(booleanSetting8::get);
        ModeSetting modeSetting2 = this.armorWear;
        BooleanSetting booleanSetting9 = this.armorStatus;
        Objects.requireNonNull(booleanSetting9);
        modeSetting2.visibleWhen(booleanSetting9::get);
        BooleanSetting booleanSetting10 = this.armorHands;
        BooleanSetting booleanSetting11 = this.armorStatus;
        Objects.requireNonNull(booleanSetting11);
        booleanSetting10.visibleWhen(booleanSetting11::get);
        ModeSetting modeSetting3 = this.cooldownDial;
        BooleanSetting booleanSetting12 = this.itemCooldowns;
        Objects.requireNonNull(booleanSetting12);
        modeSetting3.visibleWhen(booleanSetting12::get);
    }

    @Override // net.anubis.client.module.ClientModule
    public boolean enabledByDefault() {
        return true;
    }

    public boolean coordinates() {
        return this.coordinates.get();
    }

    public boolean netherCoordinates() {
        return this.netherCoordinates.get();
    }

    public boolean ping() {
        return this.ping.get();
    }

    public boolean fps() {
        return this.fps.get();
    }

    public boolean clock() {
        return this.clock.get();
    }

    public boolean logo() {
        return this.logo.get();
    }

    public boolean moduleList() {
        return this.moduleList.get();
    }

    public boolean potions() {
        return this.potions.get();
    }

    public boolean targetCard() {
        return this.targetCard.get();
    }

    public boolean targetPlayersOnly() {
        return this.targetPlayersOnly.get();
    }

    public boolean targetOnLook() {
        return this.targetOnLook.get();
    }

    public boolean targetGear() {
        return this.targetGear.get();
    }

    public boolean armorStatus() {
        return this.armorStatus.get();
    }

    public boolean armorColumn() {
        return this.armorLayout.is("Column");
    }

    public boolean armorHands() {
        return this.armorHands.get();
    }

    public boolean itemCooldowns() {
        return this.itemCooldowns.get();
    }

    public boolean cooldownSandbar() {
        return this.cooldownDial.is("Sandbar");
    }

    public boolean shadow() {
        return this.shadow.get();
    }

    public boolean customAccent() {
        return this.customAccent.get();
    }

    public int accentArgb() {
        return this.accentColor.argb();
    }

    public ArmorWear armorWear() {
        switch (this.armorWear.get()) {
            case "Percent":
                return ArmorWear.PERCENT;
            case "Gauge + Percent":
                return ArmorWear.BOTH;
            default:
                return ArmorWear.GAUGE;
        }
    }

    public ToastStyle toastStyle() {
        switch (this.toastStyle.get()) {
            case "Card":
                return ToastStyle.CARD;
            case "Chip":
                return ToastStyle.CHIP;
            default:
                return ToastStyle.BAR;
        }
    }

    public List<Setting> elementSettings(String elementId) {
        switch (elementId) {
            case "info_bar":
                return List.of(this.clock, this.fps, this.ping);
            case "coordinates":
                return List.of(this.coordinates, this.netherCoordinates);
            case "module_list":
                return List.of(this.moduleList);
            case "potion_tracker":
                return List.of(this.potions);
            case "target_card":
                return List.of(this.targetCard, this.targetPlayersOnly, this.targetOnLook, this.targetGear);
            case "armor_status":
                return List.of(this.armorStatus, this.armorLayout, this.armorWear, this.armorHands);
            case "item_cooldowns":
                return List.of(this.itemCooldowns, this.cooldownDial);
            case "toggle_toasts":
                return List.of(this.toastStyle);
            default:
                return List.of();
        }
    }
}
