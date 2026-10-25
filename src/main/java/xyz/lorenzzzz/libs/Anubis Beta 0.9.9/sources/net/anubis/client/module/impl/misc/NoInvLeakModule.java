package net.anubis.client.module.impl.misc;

import com.sun.jna.Platform;
import net.anubis.client.config.ModuleConfigStore;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.impl.misc.noinvleak.FakeLoadout;
import net.anubis.client.module.impl.misc.noinvleak.StreamOverlay;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1304;
import net.minecraft.class_1661;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_310;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/NoInvLeakModule.class */
@Environment(EnvType.CLIENT)
public final class NoInvLeakModule extends ClientModule {
    private static final String STYLE_TOTEM = "Totem";
    private static final String STYLE_RANDOM = "Random";
    private static final String STYLE_EMPTY = "Empty";
    private static final int OFFHAND_SEED = 10;
    private static volatile NoInvLeakModule active;
    private static class_1799 totem;
    private final ModeSetting style;
    private final BooleanSetting fillEmpty;
    private final BooleanSetting streamOnly;
    private final BooleanSetting borderless;
    private final BooleanSetting tooltips;
    private final BooleanSetting itemName;
    private final BooleanSetting selectedSlot;
    private final BooleanSetting hud;
    private final FakeLoadout loadout;

    public NoInvLeakModule() {
        super("no-inv-leak", "No Inv Leak", "Hides your items on screen for screen shares. Stream Only keeps them visible to you alone.", ModuleCategory.MISC);
        this.style = (ModeSetting) addSetting("style", new ModeSetting("Style", STYLE_TOTEM, STYLE_TOTEM, STYLE_RANDOM, STYLE_EMPTY));
        this.fillEmpty = (BooleanSetting) addSetting("fill-empty", new BooleanSetting("Fill Empty Slots", true));
        this.streamOnly = (BooleanSetting) addSetting("stream-only", new BooleanSetting("Stream Only", false));
        this.borderless = (BooleanSetting) addSetting("borderless-fullscreen", new BooleanSetting("Borderless Fullscreen", true));
        this.tooltips = (BooleanSetting) addSetting("hide-tooltips", new BooleanSetting("Hide Tooltips", true));
        this.itemName = (BooleanSetting) addSetting("hide-item-name", new BooleanSetting("Hide Item Name", true));
        this.selectedSlot = (BooleanSetting) addSetting("hide-selected-slot", new BooleanSetting("Hide Selected Slot", true));
        this.hud = (BooleanSetting) addSetting("anubis-hud", new BooleanSetting("Anubis HUD", true));
        this.loadout = new FakeLoadout();
        this.fillEmpty.visibleWhen(() -> {
            return !this.style.is(STYLE_EMPTY);
        });
        this.streamOnly.visibleWhen(Platform::isWindows);
        this.streamOnly.onChange(() -> {
            if (!this.streamOnly.get()) {
                StreamOverlay.stop();
            } else {
                StreamOverlay.retry();
            }
        });
        this.borderless.visibleWhen(() -> {
            return Platform.isWindows() && this.streamOnly.get();
        });
    }

    public static boolean wantsBorderless() {
        NoInvLeakModule module = (NoInvLeakModule) ModuleRegistry.get(NoInvLeakModule.class);
        if (module != null && module.streamOnly.get() && module.borderless.get()) {
            return module.isEnabled() || ModuleConfigStore.getInstance().enablesOnLoad(module);
        }
        return false;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        this.loadout.reseed();
        active = this;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        active = null;
        StreamOverlay.stop();
    }

    public static class_1799 hotbar(class_1799 stack, int x, int y, int seed) {
        int i;
        NoInvLeakModule module = active;
        if (module == null || stack == null) {
            return stack;
        }
        if (seed == 10) {
            i = 40;
        } else {
            i = (seed < 1 || seed > 9) ? 41 : seed - 1;
        }
        int slot = i;
        if (module.streamOverlaying()) {
            StreamOverlay.hotbar(slot, x, y, stack);
            return class_1799.field_8037;
        }
        return module.shown(stack, slot);
    }

    public static class_1799 container(class_1799 stack, class_1735 slot, int x, int y, boolean hovered) {
        NoInvLeakModule module = active;
        if (module == null || stack == null) {
            return stack;
        }
        if (module.streamOverlaying()) {
            StreamOverlay.container(slot.method_34266(), x, y, stack, hovered);
            return class_1799.field_8037;
        }
        return module.shown(stack, slot.method_34266());
    }

    public static class_1799 carried(class_1799 stack, int x, int y) {
        NoInvLeakModule module = active;
        if (module == null || stack == null || stack.method_7960()) {
            return stack;
        }
        if (module.streamOverlaying()) {
            StreamOverlay.carried(stack, x, y);
            return class_1799.field_8037;
        }
        return module.fake(stack, 41);
    }

    public static class_1799 shownInHud(class_1799 stack, class_1304 slot) {
        NoInvLeakModule module = active;
        return (module == null || !module.hud.get() || stack == null || stack.method_7960()) ? stack : module.fake(stack, inventorySlot(slot));
    }

    public static class_1799 fakeFor(int slot) {
        NoInvLeakModule module = active;
        return module == null ? class_1799.field_8037 : module.fake(class_1799.field_8037, slot);
    }

    public static boolean fillsEmpty() {
        NoInvLeakModule module = active;
        return (module == null || !module.fillEmpty.get() || module.style.is(STYLE_EMPTY)) ? false : true;
    }

    public static boolean overlaying() {
        NoInvLeakModule module = active;
        return module != null && module.streamOnly.get() && StreamOverlay.usable();
    }

    private boolean streamOverlaying() {
        return this.streamOnly.get() && StreamOverlay.usable();
    }

    public static boolean ownSlot(class_1735 slot) {
        return (active == null || slot == null || !(slot.field_7871 instanceof class_1661)) ? false : true;
    }

    public static boolean hidesTooltips() {
        NoInvLeakModule module = active;
        return module != null && module.tooltips.get();
    }

    public static boolean hidesItemName() {
        NoInvLeakModule module = active;
        return module != null && module.itemName.get();
    }

    public static boolean hidesSelectedSlot() {
        NoInvLeakModule module = active;
        return module != null && module.selectedSlot.get();
    }

    public static boolean overlayWanted() {
        NoInvLeakModule module = active;
        return module != null && module.streamOnly.get();
    }

    public static boolean opaqueCells() {
        NoInvLeakModule module = active;
        return (module == null || module.style.is(STYLE_EMPTY)) ? false : true;
    }

    private class_1799 shown(class_1799 stack, int slot) {
        return (!stack.method_7960() || this.fillEmpty.get()) ? fake(stack, slot) : stack;
    }

    private class_1799 fake(class_1799 stack, int slot) {
        if (this.style.is(STYLE_EMPTY)) {
            return class_1799.field_8037;
        }
        if (this.style.is(STYLE_RANDOM)) {
            return this.loadout.get(slot);
        }
        class_1799 stand = totem;
        if (stand == null) {
            class_1799 class_1799Var = new class_1799(class_1802.field_8288);
            stand = class_1799Var;
            totem = class_1799Var;
        }
        return stand;
    }

    /* JADX INFO: renamed from: net.anubis.client.module.impl.misc.NoInvLeakModule$1, reason: invalid class name */
    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/NoInvLeakModule$1.class */
    @Environment(EnvType.CLIENT)
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$net$minecraft$world$entity$EquipmentSlot = new int[class_1304.values().length];

        static {
            try {
                $SwitchMap$net$minecraft$world$entity$EquipmentSlot[class_1304.field_6169.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$net$minecraft$world$entity$EquipmentSlot[class_1304.field_6174.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$net$minecraft$world$entity$EquipmentSlot[class_1304.field_6172.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$net$minecraft$world$entity$EquipmentSlot[class_1304.field_6166.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$net$minecraft$world$entity$EquipmentSlot[class_1304.field_6171.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$net$minecraft$world$entity$EquipmentSlot[class_1304.field_6173.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    private static int inventorySlot(class_1304 slot) {
        switch (AnonymousClass1.$SwitchMap$net$minecraft$world$entity$EquipmentSlot[slot.ordinal()]) {
            case 1:
                return 39;
            case 2:
                return 38;
            case 3:
                return 37;
            case 4:
                return 36;
            case 5:
                return 40;
            case 6:
                class_746 player = class_310.method_1551().field_1724;
                if (player != null) {
                    return player.method_31548().method_67532();
                }
                return 41;
            default:
                return 41;
        }
    }
}
