package net.anubis.client.module.impl.misc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import net.anubis.client.chat.ModuleChat;
import net.anubis.client.event.impl.BlockUpdateEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.impl.misc.spawnerprotect.DiscordWebhook;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.module.setting.StringSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2480;
import net.minecraft.class_2561;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3965;
import net.minecraft.class_419;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/SpawnerProtectModule.class */
@Environment(EnvType.CLIENT)
public final class SpawnerProtectModule extends ClientModule {
    private static final int SHOP_END_CATEGORY_SLOT = 11;
    private static final int SHOP_ENDER_CHEST_SLOT = 9;
    private static final int SHOP_CONFIRM_SLOT = 25;
    private final NumberSetting targetStackCount;
    private final NumberSetting scanRange;
    private final NumberSetting rotationSpeed;
    private final BooleanSetting detectBlockUpdates;
    private final StringSetting whitelist;
    private final NumberSetting breakRange;
    private final NumberSetting blockUpdateThreshold;
    private final NumberSetting blockUpdateWindow;
    private final StringSetting webhookUrl;
    private State currentState;
    private class_2338 targetBlock;
    private class_2338 targetChest;
    private int chestTick;
    private int breakCooldown;
    private int lagWaitTicks;
    private boolean strangerDetected;
    private final Set<UUID> nearbyPlayers;
    private int blockBreakCounter;
    private long lastBreakTimestamp;
    private int shopSequence;
    private int shopTick;
    private int miningTicks;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/SpawnerProtectModule$State.class */
    @Environment(EnvType.CLIENT)
    private enum State {
        WAITING_FOR_STRANGER,
        WORKING,
        GOING_TO_CHEST,
        OPENING_CHEST,
        DEPOSITING_ITEMS,
        FINAL_EXIT,
        BUYING_ECHEST,
        PLACING_ECHEST
    }

    public SpawnerProtectModule() {
        super("spawner-protect", "Spawner Protect", "Protects spawners on block updates; player arrivals only send webhook notifications.", ModuleCategory.DONUT);
        this.targetStackCount = (NumberSetting) addSetting("stack-to-deposit", new NumberSetting("Stack To Deposit", 35.0f, 1.0f, 35.0f).step(1.0f));
        this.scanRange = (NumberSetting) addSetting("trigger-range", new NumberSetting("Trigger Range", 64.0f, 16.0f, 128.0f).step(0.1f));
        this.rotationSpeed = (NumberSetting) addSetting("rotation-speed", new NumberSetting("Rotation Speed", 15.0f, 1.0f, 30.0f).step(0.1f));
        this.detectBlockUpdates = (BooleanSetting) addSetting("detect-block-updates", new BooleanSetting("Detect Block Updates", true));
        this.whitelist = (StringSetting) addSetting("whitelist", new StringSetting("Whitelist", ""));
        this.breakRange = (NumberSetting) addSetting("break-range", new NumberSetting("Break Range", 5.5f, 1.0f, 8.0f).step(0.1f));
        this.blockUpdateThreshold = (NumberSetting) addSetting("block-update-threshold", new NumberSetting("Block Update Threshold", 5.0f, 1.0f, 20.0f).step(1.0f));
        this.blockUpdateWindow = (NumberSetting) addSetting("block-update-window", new NumberSetting("Block Update Window", 60.0f, 1.0f, 600.0f).step(1.0f));
        this.webhookUrl = (StringSetting) addSetting("webhook-url", new StringSetting("Webhook URL", ""));
        this.currentState = State.WAITING_FOR_STRANGER;
        this.nearbyPlayers = new HashSet();
        this.whitelist.privateValue();
        this.webhookUrl.privateValue();
    }

    @Override // net.anubis.client.module.ClientModule
    public boolean persistEnabled() {
        return false;
    }

    @Override // net.anubis.client.module.ClientModule
    public int settingsVersion() {
        return 1;
    }

    @Override // net.anubis.client.module.ClientModule
    public void migrateSettings(int savedVersion) {
        if (savedVersion >= 1 || this.targetStackCount.get() != 3.0f) {
            return;
        }
        this.targetStackCount.resetToDefault();
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        on(TickEvent.TYPE, this::onTick);
        on(BlockUpdateEvent.TYPE, this::onBlockUpdate);
        resetModule();
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        stopMovement();
        updateSneak(false);
    }

    private void resetModule() {
        this.currentState = State.WAITING_FOR_STRANGER;
        this.strangerDetected = false;
        this.nearbyPlayers.clear();
        this.lagWaitTicks = 0;
        this.blockBreakCounter = 0;
        this.lastBreakTimestamp = 0L;
        stopMovement();
        updateSneak(false);
        this.shopSequence = 0;
        this.shopTick = 0;
        resetState();
    }

    private void resetState() {
        this.targetChest = null;
        this.targetBlock = null;
        this.chestTick = 0;
        this.breakCooldown = 0;
        this.miningTicks = 0;
    }

    private void onBlockUpdate(BlockUpdateEvent event) {
        class_310 mc = class_310.method_1551();
        if (this.strangerDetected || mc.field_1724 == null || mc.field_1687 == null || isNearSpawn() || !this.detectBlockUpdates.get() || event.oldState.method_26215() || (event.oldState.method_26204() instanceof class_2480)) {
            return;
        }
        checkBlockUpdate(event.pos, event.newState);
    }

    private void checkBlockUpdate(class_2338 pos, class_2680 newState) {
        class_310 mc = class_310.method_1551();
        if (this.strangerDetected || mc.field_1687 == null) {
            return;
        }
        for (class_1657 player : mc.field_1687.method_18456()) {
            if (player.method_33571().method_1022(class_243.method_24953(pos)) < 8.0d && isWhitelisted(player.method_5477().getString())) {
                return;
            }
        }
        if (pos.method_10264() <= -64 || !newState.method_26215() || mc.field_1724.method_33571().method_1022(class_243.method_24953(pos)) < 6.0d) {
            return;
        }
        double dx = mc.field_1724.method_23317() - ((double) pos.method_10263());
        double dz = mc.field_1724.method_23321() - ((double) pos.method_10260());
        double horizontalDist = Math.sqrt((dx * dx) + (dz * dz));
        if (horizontalDist > this.scanRange.get()) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - this.lastBreakTimestamp > ((long) this.blockUpdateWindow.getInt()) * 1000) {
            this.blockBreakCounter = 0;
        }
        this.blockBreakCounter++;
        this.lastBreakTimestamp = now;
        if (this.blockBreakCounter < this.blockUpdateThreshold.getInt()) {
            return;
        }
        this.strangerDetected = true;
        this.currentState = State.WORKING;
        ModuleChat.send(this, "🚨 UZAK TESPİT: Yakında bir blok kırıldı! (Yatay Mesafe: " + ((int) horizontalDist) + "m, Y: " + pos.method_10264() + ")");
        sendWebhook("🚨 Protection activated", "The block-break threshold was reached. Spawner collection has started.", 15548997, DiscordWebhook.field("📡 Trigger", "Block breaking", true), DiscordWebhook.field("🧱 Block counter", this.blockBreakCounter + " / " + this.blockUpdateThreshold.getInt(), true), DiscordWebhook.field("📏 Horizontal distance", ((int) horizontalDist) + " blocks", true), DiscordWebhook.field("📍 Block location", coordinates(pos), false));
    }

    private void onTick(TickEvent event) {
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null || mc.field_1687 == null || mc.field_1761 == null || (mc.field_1755 instanceof class_419)) {
            resetModule();
            return;
        }
        if (isNearSpawn()) {
            if (this.strangerDetected) {
                resetModule();
            }
            return;
        }
        notifyNearbyPlayers();
        if (this.currentState == State.WAITING_FOR_STRANGER) {
            return;
        }
        if (this.currentState != State.OPENING_CHEST && this.currentState != State.DEPOSITING_ITEMS && this.currentState != State.BUYING_ECHEST) {
            updateSneak(true);
        }
        handleRotation();
        switch (this.currentState) {
            case WORKING:
                handleWorking();
                break;
            case GOING_TO_CHEST:
                handleGoingToChest();
                break;
            case OPENING_CHEST:
                handleOpeningChest();
                break;
            case DEPOSITING_ITEMS:
                handleDepositing();
                break;
            case FINAL_EXIT:
                handleFinalExit();
                break;
            case BUYING_ECHEST:
                handleBuyingEChest();
                break;
            case PLACING_ECHEST:
                handlePlacingEChest();
                break;
        }
    }

    private void notifyNearbyPlayers() {
        class_310 mc = class_310.method_1551();
        Set<UUID> present = new HashSet<>();
        for (class_746 class_746Var : mc.field_1687.method_18456()) {
            if (class_746Var != mc.field_1724 && !class_746Var.method_7325() && !isWhitelisted(class_746Var.method_5477().getString()) && mc.field_1724.method_5739(class_746Var) <= this.scanRange.get()) {
                present.add(class_746Var.method_5667());
                if (!this.nearbyPlayers.contains(class_746Var.method_5667())) {
                    Map<String, Object>[] mapArr = new Map[4];
                    mapArr[0] = DiscordWebhook.field("Player", "`" + class_746Var.method_5477().getString() + "`", true);
                    mapArr[1] = DiscordWebhook.field("📏 Distance", String.format(Locale.ROOT, "%.1f blocks", Float.valueOf(mc.field_1724.method_5739(class_746Var))), true);
                    mapArr[2] = DiscordWebhook.field("🛡️ Protection", this.strangerDetected ? "In progress" : "Standby", true);
                    mapArr[3] = DiscordWebhook.field("📍 Player location", coordinates(class_746Var.method_24515()), false);
                    sendWebhook("👤 A player entered your range", "A new player is nearby. This event only sends a notification.", 5793266, mapArr);
                }
            }
        }
        this.nearbyPlayers.clear();
        this.nearbyPlayers.addAll(present);
    }

    private void handleRotation() {
        class_310 mc = class_310.method_1551();
        class_243 targetPos = null;
        if (this.currentState == State.WORKING) {
            class_1542 dropped = findDroppedSpawner();
            if (dropped != null) {
                targetPos = dropped.method_73189();
            } else {
                retargetSpawner(mc);
                if (this.targetBlock != null) {
                    targetPos = class_243.method_24953(this.targetBlock);
                }
            }
        } else if ((this.currentState == State.GOING_TO_CHEST || this.currentState == State.OPENING_CHEST) && this.targetChest != null) {
            targetPos = class_243.method_24953(this.targetChest);
        }
        if (targetPos != null) {
            smoothLook(mc.field_1724, targetPos);
        }
    }

    private void retargetSpawner(class_310 mc) {
        if (this.targetBlock == null || mc.field_1687.method_8320(this.targetBlock).method_26204() != class_2246.field_10260 || mc.field_1724.method_24515().method_10262(this.targetBlock) > 256.0d) {
            this.targetBlock = findRandomBlock(class_2246.field_10260, 16);
            this.miningTicks = 0;
        }
    }

    private void smoothLook(class_746 player, class_243 target) {
        class_243 eyes = player.method_33571();
        double dx = target.field_1352 - eyes.field_1352;
        double dy = target.field_1351 - eyes.field_1351;
        double dz = target.field_1350 - eyes.field_1350;
        double dist = Math.sqrt((dx * dx) + (dz * dz));
        float targetYaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
        float targetPitch = (float) (-Math.toDegrees(Math.atan2(dy, dist)));
        float step = this.rotationSpeed.get();
        player.method_36456(player.method_36454() + class_3532.method_15363(class_3532.method_15393(targetYaw - player.method_36454()), -step, step));
        player.method_36457(player.method_36455() + class_3532.method_15363(class_3532.method_15393(targetPitch - player.method_36455()), -step, step));
    }

    private void handleWorking() {
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (player.field_7512 != player.field_7498) {
            player.method_7346();
        }
        if (findDroppedSpawner() != null) {
            this.lagWaitTicks = 0;
            stopBreaking();
            mc.field_1690.field_1894.method_23481(true);
            return;
        }
        int spawners = getSpawnerCount();
        if (spawners >= this.targetStackCount.getInt() * 64 || (spawners > 0 && player.method_31548().method_7376() == -1)) {
            goToChest();
            return;
        }
        retargetSpawner(mc);
        if (this.targetBlock == null) {
            stopMovement();
            if (this.lagWaitTicks < 40) {
                this.lagWaitTicks++;
                return;
            } else if (getSpawnerCount() > 0) {
                goToChest();
                return;
            } else {
                this.currentState = State.FINAL_EXIT;
                this.lagWaitTicks = 0;
                return;
            }
        }
        this.lagWaitTicks = 0;
        if (player.method_33571().method_1022(class_243.method_24953(this.targetBlock)) > this.breakRange.get()) {
            stopBreaking();
            mc.field_1690.field_1894.method_23481(true);
            return;
        }
        mc.field_1690.field_1894.method_23481(false);
        this.miningTicks++;
        if (this.miningTicks > 60) {
            mc.field_1761.method_2896(player, class_1268.field_5808, new class_3965(class_243.method_24953(this.targetBlock), class_2350.field_11036, this.targetBlock, false));
            player.method_6104(class_1268.field_5808);
            this.miningTicks = 0;
        }
        if (this.breakCooldown <= 0) {
            mc.field_1761.method_2902(this.targetBlock, class_2350.field_11036);
            player.method_6104(class_1268.field_5808);
            mc.field_1690.field_1886.method_23481(true);
            this.breakCooldown = 6;
            return;
        }
        this.breakCooldown--;
    }

    private void goToChest() {
        stopMovement();
        this.targetChest = findNearestBlock(class_2246.field_10443, 4);
        if (this.targetChest != null) {
            this.currentState = State.GOING_TO_CHEST;
            return;
        }
        if (getEnderChestCount() > 0) {
            this.currentState = State.PLACING_ECHEST;
            this.shopTick = 0;
        } else {
            this.currentState = State.BUYING_ECHEST;
            this.shopSequence = 0;
            this.shopTick = 0;
        }
    }

    private void handleGoingToChest() {
        class_310 mc = class_310.method_1551();
        if (this.targetChest == null) {
            this.targetChest = findNearestBlock(class_2246.field_10443, 4);
        }
        if (this.targetChest == null) {
            this.currentState = State.WORKING;
            return;
        }
        mc.field_1690.field_1894.method_23481(true);
        if (mc.field_1724.method_24515().method_19771(this.targetChest, 4.0d)) {
            stopMovement();
            this.currentState = State.OPENING_CHEST;
        }
    }

    private void handleOpeningChest() {
        class_310 mc = class_310.method_1551();
        updateSneak(false);
        if (this.chestTick > 200) {
            ModuleChat.send(this, "Ender chest açılamadı; eşyalar envanterde tutuluyor.");
            this.currentState = State.FINAL_EXIT;
            this.lagWaitTicks = 0;
            return;
        }
        if (this.chestTick % 12 == 0 && this.targetChest != null) {
            mc.field_1761.method_2896(mc.field_1724, class_1268.field_5808, new class_3965(class_243.method_24953(this.targetChest), class_2350.field_11036, this.targetChest, false));
        }
        this.chestTick++;
        if (mc.field_1724.field_7512 instanceof class_1707) {
            this.chestTick = 0;
            this.currentState = State.DEPOSITING_ITEMS;
            this.lagWaitTicks = 0;
        }
    }

    private void handleBuyingEChest() {
        class_310 mc = class_310.method_1551();
        stopMovement();
        updateSneak(false);
        this.shopTick++;
        if (this.shopTick > 200) {
            ModuleChat.send(this, "Market zaman aşımı; eşyalar envanterde tutuluyor.");
            this.currentState = State.FINAL_EXIT;
            this.lagWaitTicks = 0;
        }
        if (this.shopSequence > 0 && mc.field_1755 == null && this.shopTick > 60) {
            ModuleChat.send(this, "Market ekranı kapandı, baştan deneniyor...");
            this.shopSequence = 0;
            this.shopTick = 0;
            return;
        }
        if (this.shopTick < 30) {
            return;
        }
        switch (this.shopSequence) {
            case 0:
                ModuleChat.send(this, "Market açılıyor: /shop");
                mc.method_1562().method_45730("shop");
                this.shopSequence = 1;
                this.shopTick = 0;
                break;
            case 1:
                shopStep(mc, "SHOP", "Market: End kategorisi seçiliyor...", 11);
                break;
            case 2:
                shopStep(mc, "END", "Market: Ender Chest seçiliyor...", 9);
                break;
            case 3:
                shopStep(mc, "ENDER CHEST", "Market: Satın alma onaylanıyor...", 25);
                break;
            case 4:
                if (getEnderChestCount() > 0) {
                    ModuleChat.send(this, "Ender Chest satın alındı.");
                    mc.field_1724.method_7346();
                    this.currentState = State.PLACING_ECHEST;
                    this.shopTick = 0;
                } else if (this.shopTick > 100) {
                    ModuleChat.send(this, "Satın alma başarısız oldu (Zaman aşımı).");
                    mc.field_1724.method_7346();
                    this.shopSequence = 0;
                    this.shopTick = 0;
                }
                break;
        }
    }

    private void shopStep(class_310 mc, String title, String message, int slot) {
        if (mc.field_1755 == null || !mc.field_1755.method_25440().getString().toUpperCase(Locale.ROOT).contains(title)) {
            return;
        }
        ModuleChat.send(this, message);
        mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot, 0, class_1713.field_7790, mc.field_1724);
        this.shopSequence++;
        this.shopTick = 0;
    }

    private void handlePlacingEChest() {
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        this.shopTick++;
        if (this.shopTick < 10) {
            return;
        }
        int slot = -1;
        for (int i = 0; i < 9; i++) {
            if (player.method_31548().method_5438(i).method_7909() == class_2246.field_10443.method_8389()) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            for (int i2 = 9; i2 < 36; i2++) {
                if (player.method_31548().method_5438(i2).method_7909() == class_2246.field_10443.method_8389()) {
                    player.method_7346();
                    mc.field_1761.method_2906(player.field_7498.field_7763, i2, 0, class_1713.field_7791, player);
                    this.shopTick = 0;
                    return;
                }
            }
            this.currentState = State.BUYING_ECHEST;
            this.shopSequence = 0;
            return;
        }
        player.method_31548().method_61496(slot);
        class_2338 feet = player.method_24515();
        class_2338 placePos = null;
        for (class_2350 direction : class_2350.values()) {
            if (direction != class_2350.field_11036 && direction != class_2350.field_11033) {
                class_2338 candidate = feet.method_10093(direction);
                if (mc.field_1687.method_8320(candidate).method_45474() && !mc.field_1687.method_8320(candidate.method_10074()).method_26215()) {
                    placePos = candidate;
                    break;
                }
            }
        }
        if (placePos == null) {
            ModuleChat.send(this, "E-Chest koyacak yer bulunamadı!");
            this.currentState = State.FINAL_EXIT;
            this.lagWaitTicks = 0;
        } else {
            class_2338 support = placePos.method_10074();
            mc.field_1761.method_2896(player, class_1268.field_5808, new class_3965(class_243.method_24953(support).method_1031(0.0d, 0.5d, 0.0d), class_2350.field_11036, support, false));
            player.method_6104(class_1268.field_5808);
            this.targetChest = placePos;
            this.currentState = State.GOING_TO_CHEST;
        }
    }

    private int getEnderChestCount() {
        return countInInventory(class_2246.field_10443);
    }

    private void handleDepositing() {
        class_310 mc = class_310.method_1551();
        if (this.lagWaitTicks < 15) {
            this.lagWaitTicks++;
            return;
        }
        class_1707 class_1707Var = mc.field_1724.field_7512;
        if (!(class_1707Var instanceof class_1707)) {
            this.currentState = State.OPENING_CHEST;
            this.chestTick = 0;
            return;
        }
        class_1707 chest = class_1707Var;
        int chestSize = chest.field_7761.size() - 36;
        boolean hasSpace = false;
        for (int i = 0; i < chestSize; i++) {
            class_1799 stack = chest.method_7611(i).method_7677();
            if (stack.method_7960() || (stack.method_7909() == class_2246.field_10260.method_8389() && stack.method_7947() < stack.method_7914())) {
                hasSpace = true;
                break;
            }
        }
        boolean depositedSomething = false;
        for (int i2 = 0; i2 < 36; i2++) {
            int slotId = chestSize + i2;
            if (chest.method_7611(slotId).method_7677().method_7909() == class_2246.field_10260.method_8389()) {
                if (!hasSpace) {
                    this.currentState = State.FINAL_EXIT;
                    this.lagWaitTicks = 0;
                    return;
                } else {
                    mc.field_1761.method_2906(chest.field_7763, slotId, 0, class_1713.field_7794, mc.field_1724);
                    depositedSomething = true;
                    break;
                }
            }
        }
        if (!depositedSomething) {
            mc.field_1724.method_7346();
            if (findNearestBlock(class_2246.field_10260, 16) != null) {
                this.currentState = State.WORKING;
            } else {
                this.currentState = State.FINAL_EXIT;
                this.lagWaitTicks = 0;
            }
        }
    }

    private void handleFinalExit() {
        class_310 mc = class_310.method_1551();
        if (this.lagWaitTicks == 0) {
            int remaining = getSpawnerCount();
            boolean leftOver = remaining > 0;
            sendWebhook(leftOver ? "⚠️ Spawners remain in inventory" : "✅ Task completed", leftOver ? "Some spawners are still in your inventory. Disconnecting in two seconds." : "The collection sequence has ended. Disconnecting in two seconds.", leftOver ? 16705372 : 5763719, DiscordWebhook.field("🎒 Remaining in inventory", remaining + " spawners", true), DiscordWebhook.field("🚪 Next step", "Disconnect from server", true));
            ModuleChat.send(this, "Görev bitti, eşyaların sunucuya kaydedilmesi için 2 saniye beklenip çıkılacak...");
        }
        stopMovement();
        updateSneak(false);
        this.lagWaitTicks++;
        if (this.lagWaitTicks > 40) {
            if (mc.method_1562() != null) {
                mc.method_1562().method_48296().method_10747(class_2561.method_43470("[SpawnerProtect] Görev tamamlandı, güvenli çıkış yapıldı."));
            }
            resetModule();
            requestDisable();
        }
    }

    @SafeVarargs
    private void sendWebhook(String title, String description, int color, Map<String, Object>... eventFields) {
        class_310 mc = class_310.method_1551();
        if (this.webhookUrl.get().isBlank() || mc.field_1724 == null || mc.field_1687 == null) {
            return;
        }
        List<Map<String, Object>> fields = new ArrayList<>(Arrays.asList(eventFields));
        fields.add(DiscordWebhook.field("👤 Account", "`" + mc.field_1724.method_5477().getString() + "`", true));
        fields.add(DiscordWebhook.field("🌍 Dimension", "`" + String.valueOf(mc.field_1687.method_27983().method_29177()) + "`", true));
        fields.add(DiscordWebhook.field("📍 Your location", coordinates(mc.field_1724.method_24515()), false));
        DiscordWebhook.send(this.webhookUrl.get(), DiscordWebhook.payload(title, description, color, fields, Instant.now()));
    }

    private static String coordinates(class_2338 pos) {
        return "`X: " + pos.method_10263() + "  Y: " + pos.method_10264() + "  Z: " + pos.method_10260() + "`";
    }

    private void updateSneak(boolean sneak) {
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 != null) {
            mc.field_1724.method_5660(sneak);
        }
        mc.field_1690.field_1832.method_23481(sneak);
    }

    private boolean isNearSpawn() {
        class_746 player = class_310.method_1551().field_1724;
        return player != null && Math.abs(player.method_23317()) < 100.0d && Math.abs(player.method_23321()) < 100.0d;
    }

    private boolean isWhitelisted(String name) {
        return Arrays.stream(this.whitelist.get().split("[,;\\s]+")).anyMatch(entry -> {
            return entry.equalsIgnoreCase(name);
        });
    }

    private class_2338 findNearestBlock(class_2248 block, int radius) {
        class_310 mc = class_310.method_1551();
        class_2338 origin = mc.field_1724.method_24515();
        class_2338 nearest = null;
        double minDist = Double.MAX_VALUE;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    class_2338 pos = origin.method_10069(x, y, z);
                    if (mc.field_1687.method_8320(pos).method_26204() == block) {
                        double dist = origin.method_10262(pos);
                        if (dist < minDist) {
                            minDist = dist;
                            nearest = pos;
                        }
                    }
                }
            }
        }
        return nearest;
    }

    private class_2338 findRandomBlock(class_2248 block, int radius) {
        class_310 mc = class_310.method_1551();
        class_2338 origin = mc.field_1724.method_24515();
        List<class_2338> found = new ArrayList<>();
        double maxDistSq = radius * radius;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    class_2338 pos = origin.method_10069(x, y, z);
                    if (origin.method_10262(pos) <= maxDistSq && mc.field_1687.method_8320(pos).method_26204() == block) {
                        found.add(pos);
                    }
                }
            }
        }
        if (found.isEmpty()) {
            return null;
        }
        return found.get(new Random().nextInt(found.size()));
    }

    private int getSpawnerCount() {
        return countInInventory(class_2246.field_10260);
    }

    private static int countInInventory(class_2248 block) {
        class_746 player = class_310.method_1551().field_1724;
        int count = 0;
        for (int i = 0; i < 36; i++) {
            class_1799 stack = player.method_31548().method_5438(i);
            if (stack.method_7909() == block.method_8389()) {
                count += stack.method_7947();
            }
        }
        return count;
    }

    private class_1542 findDroppedSpawner() {
        class_310 mc = class_310.method_1551();
        class_1542 nearest = null;
        float nearestDist = Float.MAX_VALUE;
        for (class_1297 entity : mc.field_1687.method_18112()) {
            if (entity instanceof class_1542) {
                class_1542 item = (class_1542) entity;
                if (item.method_6983().method_7909() == class_2246.field_10260.method_8389()) {
                    float dist = mc.field_1724.method_5739(item);
                    if (dist < 16.0f && !item.method_6977() && dist < nearestDist) {
                        nearestDist = dist;
                        nearest = item;
                    }
                }
            }
        }
        return nearest;
    }

    private void stopBreaking() {
        class_310 mc = class_310.method_1551();
        mc.field_1690.field_1886.method_23481(false);
        if (mc.field_1761 != null) {
            mc.field_1761.method_2925();
        }
        this.breakCooldown = 0;
    }

    private void stopMovement() {
        stopBreaking();
        class_310.method_1551().field_1690.field_1894.method_23481(false);
    }
}
