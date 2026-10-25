package net.anubis.client.module.impl.movement;

import com.example.client.mixin.AccessorLocalPlayer;
import java.util.Objects;
import net.anubis.client.chat.ModuleChat;
import net.anubis.client.event.EventPriority;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.KeyInputEvent;
import net.anubis.client.event.impl.MouseInputEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.input.ModuleKeybinds;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.CombatMacroLock;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.impl.combat.AutoInvTotemModule;
import net.anubis.client.module.impl.movement.elytrafly.ElytraFlyInventory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.KeySetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.util.pvp.InputControl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10192;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1661;
import net.minecraft.class_1713;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1890;
import net.minecraft.class_2561;
import net.minecraft.class_2815;
import net.minecraft.class_310;
import net.minecraft.class_5134;
import net.minecraft.class_746;
import net.minecraft.class_9285;
import net.minecraft.class_9334;
import net.minecraft.class_9701;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/movement/ElytraSwapModule.class */
@Environment(EnvType.CLIENT)
public final class ElytraSwapModule extends ClientModule {
    private static final int MAX_WAIT_TICKS = 20;
    private static final int CHEST_SLOT_ID = 6;
    private final KeySetting key;
    private final BooleanSetting swapBackOnLanding;
    private final NumberSetting landingDelay;
    private boolean pending;
    private int pendingTicks;
    private boolean armed;
    private boolean glided;
    private int groundTicks;
    private int settleTicks;
    private int storedIndex;
    private class_1792 storedItem;
    private static final int[] SOURCE_INDICES = sourceIndices();
    private static final class_2561 SWAP_FAILED = ModuleChat.body().danger("Swap failed").text(", the item didn't move into the chest slot").build();

    public ElytraSwapModule() {
        super("elytra-swap", "Elytra Swap", "Swaps between your chestplate and elytra with one key.", ModuleCategory.MOVEMENT);
        this.key = (KeySetting) addSetting("key", new KeySetting("Key", -1));
        this.swapBackOnLanding = (BooleanSetting) addSetting("swap-back-on-landing", new BooleanSetting("Swap Back on Landing", true));
        this.landingDelay = (NumberSetting) addSetting("landing-delay", new NumberSetting("Landing Delay", 4.0f, 0.0f, 20.0f).step(1.0f));
        this.storedIndex = -1;
        usesNotifications();
        NumberSetting numberSetting = this.landingDelay;
        BooleanSetting booleanSetting = this.swapBackOnLanding;
        Objects.requireNonNull(booleanSetting);
        numberSetting.visibleWhen(booleanSetting::get);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        reset();
        on(KeyInputEvent.TYPE, Stage.PRE, EventPriority.HIGH, this::onKey);
        on(MouseInputEvent.TYPE, Stage.PRE, EventPriority.HIGH, this::onMouse);
        on(TickEvent.TYPE, Stage.PRE, EventPriority.LOWEST, this::onTick);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        reset();
    }

    private void reset() {
        this.pending = false;
        this.pendingTicks = 0;
        disarm();
    }

    private void disarm() {
        this.armed = false;
        this.glided = false;
        this.groundTicks = 0;
        this.settleTicks = 0;
        this.storedIndex = -1;
        this.storedItem = null;
    }

    private void onKey(KeyInputEvent event) {
        if (event.action == 1) {
            onPress(event.key);
        }
    }

    private void onMouse(MouseInputEvent event) {
        if (event.action == 1) {
            onPress(ModuleKeybinds.MOUSE_OFFSET + event.button);
        }
    }

    private void onPress(int pressed) {
        int bound = this.key.value();
        if (ModuleKeybinds.isBindable(bound) && pressed == bound) {
            class_310 mc = class_310.method_1551();
            if (mc.field_1755 != null || mc.field_1724 == null) {
                return;
            }
            this.pending = true;
            this.pendingTicks = 0;
        }
    }

    private void onTick(TickEvent event) {
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (player == null || mc.field_1687 == null || mc.field_1761 == null) {
            reset();
            return;
        }
        if (this.pending) {
            String busy = busyReason(player);
            if (busy == null) {
                if (mc.field_1755 != null || swap(mc, player)) {
                    this.pending = false;
                    return;
                }
                busy = "you kept moving";
            }
            int i = this.pendingTicks + 1;
            this.pendingTicks = i;
            if (i > 20) {
                this.pending = false;
                ModuleChat.send(this, ModuleChat.body().warn("Swap skipped").text(", " + busy));
                return;
            }
            return;
        }
        if (this.armed) {
            followFlight(mc, player);
        }
    }

    private String busyReason(class_746 player) {
        if (CombatMacroLock.heldByOther(this)) {
            return "another macro kept the inventory busy";
        }
        if (AutoInvTotemModule.busy()) {
            return "the totem refill kept the inventory busy";
        }
        if (player.method_6115()) {
            return "you kept using an item";
        }
        if (((AccessorLocalPlayer) player).anubis$lastSentInput().comp_3164()) {
            return "you kept sneaking";
        }
        return null;
    }

    private void followFlight(class_310 mc, class_746 player) {
        if (!this.swapBackOnLanding.get() || !player.method_6118(class_1304.field_6174).method_57826(class_9334.field_54197)) {
            disarm();
            return;
        }
        if (player.method_6128()) {
            this.glided = true;
            this.groundTicks = 0;
            return;
        }
        if (this.glided) {
            if (!player.method_24828() && !player.method_5799()) {
                this.groundTicks = 0;
                return;
            }
            int i = this.groundTicks;
            this.groundTicks = i + 1;
            if (i >= this.landingDelay.getInt() && mc.field_1755 == null && inventoryReady(player) && busyReason(player) == null) {
                if (!isBound(player, player.method_6118(class_1304.field_6174))) {
                    int chestplate = chestplateToEquip(player);
                    if (chestplate == -1) {
                        ModuleChat.send(this, ModuleChat.body().danger("Can't swap back").text(", no ").name("chestplate").text(" left in your inventory"));
                    } else if (!AutoInvTotemModule.clickSafe(player)) {
                        int i2 = this.settleTicks + 1;
                        this.settleTicks = i2;
                        if (i2 <= 20) {
                            InputControl.idle();
                            return;
                        }
                        ModuleChat.send(this, ModuleChat.body().warn("Swap back skipped").text(", you kept moving"));
                    } else if (!equip(mc, player, chestplate)) {
                        ModuleChat.send(this, SWAP_FAILED);
                    }
                }
                disarm();
            }
        }
    }

    private boolean swap(class_310 mc, class_746 player) {
        if (player.method_7325()) {
            return true;
        }
        if (!inventoryReady(player)) {
            ModuleChat.send(this, ModuleChat.body().warn("Swap skipped").text(", close the open container first"));
            return true;
        }
        class_1799 worn = player.method_6118(class_1304.field_6174);
        if (isBound(player, worn)) {
            ModuleChat.send(this, ModuleChat.body().danger("Can't swap").text(", your chest piece has ").name("Curse of Binding"));
            return true;
        }
        if (worn.method_57826(class_9334.field_54197)) {
            int chestplate = chestplateToEquip(player);
            if (chestplate == -1) {
                ModuleChat.send(this, ModuleChat.body().danger("Can't swap").text(", no ").name("chestplate").text(" in your inventory"));
            } else {
                if (!AutoInvTotemModule.clickSafe(player)) {
                    InputControl.idle();
                    return false;
                }
                if (!equip(mc, player, chestplate)) {
                    ModuleChat.send(this, SWAP_FAILED);
                }
            }
            disarm();
            return true;
        }
        int elytra = findElytra(player);
        if (elytra == -1) {
            ModuleChat.send(this, ModuleChat.body().danger("Can't swap").text(", no usable ").name("elytra").text(" in your inventory"));
            return true;
        }
        if (!AutoInvTotemModule.clickSafe(player)) {
            InputControl.idle();
            return false;
        }
        class_1792 takenOff = isChestplate(worn) ? worn.method_7909() : null;
        if (!equip(mc, player, elytra)) {
            ModuleChat.send(this, SWAP_FAILED);
            return true;
        }
        disarm();
        if (takenOff != null) {
            this.armed = this.swapBackOnLanding.get();
            this.storedIndex = elytra;
            this.storedItem = takenOff;
            return true;
        }
        return true;
    }

    private int chestplateToEquip(class_746 player) {
        if (this.storedIndex >= 0 && this.storedItem != null) {
            class_1799 stored = player.method_31548().method_5438(this.storedIndex);
            if (stored.method_31574(this.storedItem) && !isBound(player, stored)) {
                return this.storedIndex;
            }
        }
        return findBestChestplate(player);
    }

    private static boolean equip(class_310 mc, class_746 player, int index) {
        class_1792 item = player.method_31548().method_5438(index).method_7909();
        mc.field_1761.anubis$elytraFlySyncSelected();
        if (class_1661.method_7380(index) || index == 40) {
            mc.field_1761.method_2906(player.field_7498.field_7763, 6, index, class_1713.field_7791, player);
        } else {
            ElytraFlyInventory.moveToChest(index);
        }
        player.field_3944.method_52787(new class_2815(0));
        return player.method_6118(class_1304.field_6174).method_31574(item);
    }

    private static boolean inventoryReady(class_746 player) {
        return player.field_7512 == player.field_7498 && player.field_7512.method_34255().method_7960();
    }

    private static boolean isBound(class_746 player, class_1799 stack) {
        return !player.method_68878() && class_1890.method_60142(stack, class_9701.field_51656);
    }

    private static int findElytra(class_746 player) {
        int left;
        class_1661 inventory = player.method_31548();
        int best = -1;
        int bestLeft = -1;
        for (int index : SOURCE_INDICES) {
            class_1799 stack = inventory.method_5438(index);
            if (class_1309.method_63624(stack, class_1304.field_6174) && !isBound(player, stack) && (left = durabilityLeft(stack)) > bestLeft) {
                best = index;
                bestLeft = left;
            }
        }
        return best;
    }

    private static int findBestChestplate(class_746 player) {
        class_1661 inventory = player.method_31548();
        int best = -1;
        double bestScore = -1.0d;
        int bestLeft = -1;
        for (int index : SOURCE_INDICES) {
            class_1799 stack = inventory.method_5438(index);
            if (isChestplate(stack) && !isBound(player, stack)) {
                double score = armorScore(stack);
                int left = durabilityLeft(stack);
                if (score > bestScore || (score == bestScore && left > bestLeft)) {
                    best = index;
                    bestScore = score;
                    bestLeft = left;
                }
            }
        }
        return best;
    }

    private static boolean isChestplate(class_1799 stack) {
        class_10192 equippable;
        return (stack.method_7960() || stack.method_57826(class_9334.field_54197) || (equippable = (class_10192) stack.method_58694(class_9334.field_54196)) == null || equippable.comp_3174() != class_1304.field_6174) ? false : true;
    }

    private static double armorScore(class_1799 stack) {
        class_9285 modifiers = (class_9285) stack.method_58695(class_9334.field_49636, class_9285.field_49326);
        return modifiers.method_57481(class_5134.field_23724, 0.0d, class_1304.field_6174) + modifiers.method_57481(class_5134.field_23725, 0.0d, class_1304.field_6174);
    }

    private static int durabilityLeft(class_1799 stack) {
        if (stack.method_7963()) {
            return stack.method_7936() - stack.method_7919();
        }
        return Integer.MAX_VALUE;
    }

    private static int[] sourceIndices() {
        int[] indices = new int[37];
        for (int i = 0; i < 36; i++) {
            indices[i] = i;
        }
        indices[36] = 40;
        return indices;
    }
}
