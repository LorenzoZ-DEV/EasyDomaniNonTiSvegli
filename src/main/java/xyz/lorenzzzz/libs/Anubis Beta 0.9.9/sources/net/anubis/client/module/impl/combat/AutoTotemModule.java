package net.anubis.client.module.impl.combat;

import com.example.client.mixin.AccessorLocalPlayer;
import java.util.Objects;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.EventPriority;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.impl.combat.totem.DoubleHand;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.util.pvp.InputControl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1268;
import net.minecraft.class_1661;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2846;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_634;
import net.minecraft.class_640;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/AutoTotemModule.class */
@Environment(EnvType.CLIENT)
public final class AutoTotemModule extends ClientModule {
    private static final int MIN_CONFIRM_TICKS = 10;
    private static final int MAX_CONFIRM_TICKS = 40;
    private static final int GIVE_UP_COOLDOWN_TICKS = 10;
    private static AutoTotemModule instance;
    private final BooleanSetting hotbarRefill;
    private final NumberSetting hotbarSlot;
    private final NumberSetting delay;
    private final BooleanSetting doubleHand;
    private final DoubleHand totemHand;
    private int tick;
    private int reactionAt;
    private int idleUntil;
    private int confirmDeadline;
    private int clickedAt;
    private boolean settling;
    private int heldSlot;
    private int previousSlot;

    public AutoTotemModule() {
        super("autototem", "Auto Totem", "Fills an empty offhand with a totem without opening the inventory; Double Hand also keeps one in the main hand while in danger.", ModuleCategory.COMBAT);
        this.hotbarRefill = (BooleanSetting) addSetting("hotbar-refill", new BooleanSetting("Hotbar Refill", false));
        this.hotbarSlot = (NumberSetting) addSetting("hotbar-slot", new NumberSetting("Hotbar Slot", 1.0f, 1.0f, 9.0f).step(1.0f));
        this.delay = (NumberSetting) addSetting("reaction-delay", new NumberSetting("Delay", 0.0f, 0.0f, 10.0f).step(1.0f));
        this.doubleHand = (BooleanSetting) addSetting("double-hand", new BooleanSetting("Double Hand", false));
        this.totemHand = new DoubleHand();
        this.reactionAt = -1;
        this.confirmDeadline = -1;
        this.clickedAt = -1;
        this.heldSlot = -1;
        this.previousSlot = -1;
        instance = this;
        NumberSetting numberSetting = this.hotbarSlot;
        BooleanSetting booleanSetting = this.hotbarRefill;
        Objects.requireNonNull(booleanSetting);
        numberSetting.visibleWhen(booleanSetting::get);
    }

    public static boolean wantsHotbar() {
        AutoTotemModule module = instance;
        return module != null && module.isEnabled() && (module.heldSlot != -1 || module.totemHand.holding());
    }

    public static boolean busy() {
        AutoTotemModule module = instance;
        class_746 player = class_310.method_1551().field_1724;
        if (module == null || !module.isEnabled() || player == null || player.method_7325() || player.method_56992()) {
            return false;
        }
        class_1661 inventory = player.method_31548();
        return wantsHotbar() || module.awaitingServer(player) || module.clickedAt == module.tick || (player.method_6079().method_7960() && !(findStoredTotem(inventory) == -1 && findHotbarTotem(inventory) == -1));
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        AutoInvTotemModule other = (AutoInvTotemModule) ModuleRegistry.get(AutoInvTotemModule.class);
        if (other != null) {
            other.setEnabled(false);
        }
        this.idleUntil = 0;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        class_746 player = class_310.method_1551().field_1724;
        if (player != null) {
            class_1661 inventory = player.method_31548();
            if (this.heldSlot != -1) {
                restoreHeld(inventory);
            }
            this.totemHand.putBack(inventory);
        }
        reset();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTick(TickEvent event) {
        this.tick++;
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        boolean settled = this.settling;
        this.settling = false;
        if (player == null || mc.field_1687 == null || mc.field_1761 == null || mc.method_1562() == null || player.method_29504() || player.method_7325() || player.method_56992()) {
            reset();
            return;
        }
        if (this.heldSlot != -1) {
            restoreHeld(player.method_31548());
        }
        this.totemHand.tick(mc, player, this.doubleHand.get());
        if (!player.method_6079().method_7960()) {
            this.reactionAt = -1;
            this.confirmDeadline = -1;
            if (this.hotbarRefill.get()) {
                refillHotbar(mc, player);
                return;
            }
            return;
        }
        if (!awaitingServer(player)) {
            if (this.reactionAt < 0) {
                this.reactionAt = this.tick + this.delay.getInt();
            }
            if (this.tick >= this.reactionAt) {
                refillOffhand(mc, player, settled);
            }
        }
    }

    private void refillOffhand(class_310 mc, class_746 player, boolean settled) {
        class_1661 inventory = player.method_31548();
        int stored = findStoredTotem(inventory);
        if (stored != -1 && AutoInvTotemModule.clickSafe(player)) {
            click(mc, player, stored, 40);
            this.clickedAt = this.tick;
            return;
        }
        if (mc.field_1755 != null) {
            return;
        }
        int hotbar = findHotbarTotem(inventory);
        if (hotbar != -1) {
            swapOffhand(mc, player, hotbar);
            return;
        }
        if (stored != -1 && !settled && this.tick >= this.idleUntil && !((AccessorLocalPlayer) player).anubis$lastSentInput().comp_3164()) {
            InputControl.idle();
            this.settling = true;
        } else if (settled) {
            this.idleUntil = this.tick + 10;
        }
    }

    private void refillHotbar(class_310 mc, class_746 player) {
        int stored;
        class_1661 inventory = player.method_31548();
        int target = this.hotbarSlot.getInt() - 1;
        if (!inventory.method_5438(target).method_31574(class_1802.field_8288) && AutoInvTotemModule.clickSafe(player) && (stored = findStoredTotem(inventory)) != -1) {
            click(mc, player, stored, target);
            this.clickedAt = this.tick;
        }
    }

    private void swapOffhand(class_310 mc, class_746 player, int slot) {
        class_634 connection = mc.method_1562();
        class_1661 inventory = player.method_31548();
        int previous = inventory.method_67532();
        inventory.method_61496(slot);
        mc.field_1761.anubis$elytraFlySyncSelected();
        connection.method_52787(new class_2846(class_2846.class_2847.field_12969, class_2338.field_10980, class_2350.field_11033));
        this.confirmDeadline = this.tick + confirmWindow(connection, player);
        if (slot == previous) {
            return;
        }
        if (player.method_6115() && player.method_6058() == class_1268.field_5808) {
            this.heldSlot = slot;
            this.previousSlot = previous;
        } else {
            inventory.method_61496(previous);
        }
    }

    private void restoreHeld(class_1661 inventory) {
        if (inventory.method_67532() == this.heldSlot) {
            inventory.method_61496(this.previousSlot);
        }
        this.heldSlot = -1;
        this.previousSlot = -1;
    }

    private static void click(class_310 mc, class_746 player, int inventorySlot, int button) {
        class_1703 menu = player.field_7512;
        if (menu.method_34255().method_7960()) {
            mc.field_1761.anubis$elytraFlySyncSelected();
            menu.method_37418(player.method_31548(), inventorySlot).ifPresent(slot -> {
                mc.field_1761.method_2906(menu.field_7763, slot, button, class_1713.field_7791, player);
            });
        }
    }

    private static int findStoredTotem(class_1661 inventory) {
        for (int i = 9; i < 36; i++) {
            if (inventory.method_5438(i).method_31574(class_1802.field_8288)) {
                return i;
            }
        }
        return -1;
    }

    private static int findHotbarTotem(class_1661 inventory) {
        int selected = inventory.method_67532();
        for (int i = 0; i < 9; i++) {
            if (i != selected && inventory.method_5438(i).method_31574(class_1802.field_8288)) {
                return i;
            }
        }
        if (inventory.method_5438(selected).method_31574(class_1802.field_8288)) {
            return selected;
        }
        return -1;
    }

    private static int confirmWindow(class_634 connection, class_746 player) {
        class_640 info = connection.method_2871(player.method_5667());
        int latencyMs = info != null ? info.method_2959() : 0;
        return class_3532.method_15340((latencyMs / 25) + 4, 10, 40);
    }

    private boolean awaitingServer(class_746 player) {
        return this.confirmDeadline >= 0 && this.tick < this.confirmDeadline && !player.method_6079().method_31574(class_1802.field_8288);
    }

    private void reset() {
        this.reactionAt = -1;
        this.confirmDeadline = -1;
        this.settling = false;
        this.heldSlot = -1;
        this.previousSlot = -1;
        this.totemHand.reset();
    }
}
