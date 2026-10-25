package net.anubis.client.module.impl.combat;

import net.anubis.client.chat.ModuleChat;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.CombatMacroLock;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.impl.combat.crystal.Combo;
import net.anubis.client.module.impl.combat.crystal.CrystalDamage;
import net.anubis.client.module.impl.combat.crystal.Crystals;
import net.anubis.client.module.impl.combat.melee.MeleeAttacks;
import net.anubis.client.module.impl.combat.melee.SlotReturn;
import net.anubis.client.util.pvp.Rotation;
import net.anubis.client.util.pvp.RotationUtils;
import net.anubis.client.util.pvp.ServerRotation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1268;
import net.minecraft.class_1309;
import net.minecraft.class_1661;
import net.minecraft.class_1799;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_638;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/AutoCityModule.class */
@Environment(EnvType.CLIENT)
public final class AutoCityModule extends ClientModule {
    private static final float CRYSTAL_POWER = 6.0f;
    private static final double FACE_INSET = 0.05d;
    private static final int GIVE_UP_TICKS = 200;
    private static volatile AutoCityModule active;
    private final SlotReturn slots;
    private class_1309 target;
    private class_2338 feet;
    private class_2338 block;
    private int ticks;
    private boolean broken;

    public AutoCityModule() {
        super("auto-city", "Auto City", "Mines a surround block of the best enemy in a hole, then turns off so Crystal Aura can blow through it.", ModuleCategory.COMBAT);
        this.slots = new SlotReturn(this);
        usesNotifications();
    }

    @Override // net.anubis.client.module.ClientModule
    public boolean persistEnabled() {
        return false;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        this.target = null;
        this.feet = null;
        this.block = null;
        this.ticks = 0;
        this.broken = false;
        active = this;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        active = null;
        class_310 mc = class_310.method_1551();
        if (!this.broken && this.block != null && mc.field_1761 != null && mc.field_1761.method_2923()) {
            mc.field_1761.method_2925();
        }
        this.slots.drop(mc, true);
        CombatMacroLock.release(this);
        this.target = null;
        this.block = null;
    }

    public static boolean mining() {
        AutoCityModule module = active;
        return (module == null || module.block == null || module.broken) ? false : true;
    }

    @EventHandler
    public void onTick(TickEvent event) {
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (player == null || mc.field_1687 == null || mc.field_1761 == null || player.method_29504() || this.broken || userClicks(mc)) {
            requestDisable();
            return;
        }
        if (this.block == null && !pick(mc, player)) {
            stop("No enemy in a hole within reach");
            return;
        }
        class_638 level = mc.field_1687;
        class_2680 state = level.method_8320(this.block);
        if (state.method_45474()) {
            opened();
            return;
        }
        this.ticks++;
        String problem = problem(player, state);
        if (problem != null) {
            stop(problem);
            return;
        }
        if (!MeleeAttacks.canAct(mc) || AutoInvTotemModule.wantsHotbar() || player.method_6115() || player.method_3144()) {
            if (mc.field_1761.method_2923()) {
                mc.field_1761.method_2925();
            }
            CombatMacroLock.release(this);
            return;
        }
        if (CombatMacroLock.tryAcquire(this) && this.slots.take(player, toolSlot(player.method_31548(), state))) {
            class_3965 aim = aim(level, player, this.block);
            if (aim == null) {
                stop("Lost sight of the block");
                return;
            }
            ServerRotation.request(RotationUtils.fromTo(player.method_33571(), aim.method_17784()));
            class_3965 held = held(level, player, this.block);
            if (held == null || MeleeAttacks.actedThisTick(player)) {
                return;
            }
            if (mc.field_1761.method_2902(this.block, held.method_17780())) {
                player.method_6104(class_1268.field_5808);
            }
            MeleeAttacks.noteAction(player);
            if (level.method_8320(this.block).method_45474()) {
                opened();
                note(ModuleChat.body().text("Opened ").name(this.target.method_5477().getString()).text("'s surround"));
            }
        }
    }

    private void opened() {
        this.broken = true;
        CombatMacroLock.release(this);
    }

    private void stop(String reason) {
        note(ModuleChat.body().text(reason));
        requestDisable();
    }

    private static boolean userClicks(class_310 mc) {
        return mc.field_1755 == null && (Crystals.isPhysicallyDown(mc, mc.field_1690.field_1886) || MeleeAttacks.clickPending(mc) || Crystals.isPhysicallyDown(mc, mc.field_1690.field_1904));
    }

    private boolean pick(class_310 mc, class_746 player) {
        class_638 level = mc.field_1687;
        float best = 0.0f;
        for (class_1309 enemy : Combo.enemies(mc, player)) {
            if (Combo.inHole(enemy)) {
                class_2338 at = enemy.method_24515();
                for (class_2350 side : class_2350.class_2353.field_11062) {
                    class_2338 pos = at.method_10093(side);
                    class_2680 state = level.method_8320(pos);
                    if (state.method_26214(level, pos) >= 0.0f && Crystals.isBase(level.method_8320(pos.method_10074())) && player.method_56093(pos, 0.0d) && toolSlot(player.method_31548(), state) != -1 && aim(level, player, pos) != null) {
                        class_243 blast = Crystals.explosionAt(pos.method_10074());
                        float own = CrystalDamage.estimate(blast, player, CRYSTAL_POWER, pos);
                        if (!CrystalDamage.endangers(player, own)) {
                            float value = CrystalDamage.estimate(blast, enemy, CRYSTAL_POWER, pos) - (0.5f * own);
                            if (value > best) {
                                best = value;
                                this.target = enemy;
                                this.feet = at;
                                this.block = pos;
                            }
                        }
                    }
                }
            }
        }
        return this.block != null;
    }

    private String problem(class_746 player, class_2680 state) {
        class_2338 at = this.target.method_24515();
        if (!this.target.method_5805() || at.method_10263() != this.feet.method_10263() || at.method_10260() != this.feet.method_10260() || Math.abs(at.method_10264() - this.feet.method_10264()) > 1) {
            return "Target left the hole";
        }
        if (!player.method_56093(this.block, 0.0d)) {
            return "Block went out of reach";
        }
        if (toolSlot(player.method_31548(), state) == -1) {
            return "No pickaxe that can break it";
        }
        if (!ServerRotation.canRotate(player)) {
            return "Can't aim while flying, swimming or riding";
        }
        if (this.ticks > GIVE_UP_TICKS) {
            return "Gave up, the block took too long";
        }
        return null;
    }

    private void note(ModuleChat.Body body) {
        if (notificationsOn()) {
            ModuleChat.actionBar(this, body);
        }
    }

    private static int toolSlot(class_1661 inventory, class_2680 state) {
        int best = -1;
        float fastest = 0.0f;
        for (int slot = 0; slot < class_1661.method_7368(); slot++) {
            class_1799 stack = inventory.method_5438(slot);
            if (stack.method_7951(state) && stack.method_7924(state) > fastest) {
                best = slot;
                fastest = stack.method_7924(state);
            }
        }
        return best;
    }

    private static class_3965 aim(class_638 level, class_746 player, class_2338 pos) {
        class_243 eye = player.method_33571();
        double reach = player.method_55754();
        class_3965 best = null;
        double nearest = reach * reach;
        for (class_2350 face : class_2350.values()) {
            class_243 point = class_243.method_24953(pos).method_43206(face, 0.5d);
            double distance = eye.method_1025(point);
            if (distance <= nearest) {
                class_3965 hit = level.method_17742(new class_3959(eye, point.method_43206(face, -0.05d), class_3959.class_3960.field_17559, class_3959.class_242.field_1348, player));
                if (hit.method_17783() == class_239.class_240.field_1332 && hit.method_17777().equals(pos) && hit.method_17780() == face) {
                    best = hit;
                    nearest = distance;
                }
            }
        }
        return best;
    }

    private static class_3965 held(class_638 level, class_746 player, class_2338 pos) {
        Rotation sent = ServerRotation.sent();
        if (sent == null) {
            return null;
        }
        class_243 eye = player.method_33571();
        class_243 end = eye.method_1019(class_243.method_1030(sent.pitch(), sent.yaw()).method_1021(player.method_55754()));
        class_3965 hit = level.method_17742(new class_3959(eye, end, class_3959.class_3960.field_17559, class_3959.class_242.field_1348, player));
        if (hit.method_17783() == class_239.class_240.field_1332 && hit.method_17777().equals(pos)) {
            return hit;
        }
        return null;
    }
}
