package net.anubis.client.module.impl.combat;

import net.anubis.client.event.EventHandler;
import net.anubis.client.event.EventPriority;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.CombatMacroLock;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.impl.combat.melee.MeleeAttacks;
import net.anubis.client.module.impl.combat.melee.SlotReturn;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.util.pvp.ServerRotation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10707;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1799;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3966;
import net.minecraft.class_640;
import net.minecraft.class_746;
import net.minecraft.class_9334;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/ShieldBreakerModule.class */
@Environment(EnvType.CLIENT)
public final class ShieldBreakerModule extends ClientModule {
    private static final String MODE_PASSIVE = "Passive";
    private static final String MODE_AUTO = "Auto";
    private static final int REARM_TICKS = 10;
    private static final int MAX_LEAD_TICKS = 4;
    private final ModeSetting mode;
    private final BooleanSetting switchBack;
    private final SlotReturn slots;
    private int ticks;
    private int rearmTick;

    public ShieldBreakerModule() {
        super("shield-breaker", "Shield Breaker", "Swaps to an axe for any hit into a raised shield. Auto also makes the hit.", ModuleCategory.COMBAT);
        this.mode = (ModeSetting) addSetting("mode", new ModeSetting("Mode", MODE_PASSIVE, MODE_PASSIVE, MODE_AUTO));
        this.switchBack = (BooleanSetting) addSetting("switch-back", new BooleanSetting("Switch Back", true));
        this.slots = new SlotReturn(this);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        this.ticks = 0;
        this.rearmTick = 0;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        this.slots.drop(class_310.method_1551(), this.switchBack.get());
    }

    public void beforeAttack(class_746 player, class_1297 target) {
        int slot;
        if (target instanceof class_1657) {
            class_1657 other = (class_1657) target;
            if (!MeleeAttacks.disablesShields(player.method_6047()) && !MeleeAttacks.shieldBroken(other) && blocksSoon(player, other) && (slot = bestAxe(player)) >= 0 && this.slots.take(player, slot)) {
                MeleeAttacks.noteShieldBroken(other);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTick(TickEvent event) {
        this.ticks++;
        this.slots.tick(class_310.method_1551(), this.switchBack.get());
    }

    @EventHandler
    public void onAutoTick(TickEvent event) {
        if (this.mode.is(MODE_AUTO)) {
            tryHit(class_310.method_1551());
        }
    }

    private void tryHit(class_310 mc) {
        class_3966 hit;
        if (this.ticks < this.rearmTick || !MeleeAttacks.canAct(mc) || CombatMacroLock.heldByOther(this) || AutoInvTotemModule.wantsHotbar()) {
            return;
        }
        class_746 player = mc.field_1724;
        if (MeleeAttacks.actedThisTick(player) || ServerRotation.pending() || MeleeAttacks.clickPending(mc) || (hit = MeleeAttacks.crosshairHit(mc)) == null) {
            return;
        }
        class_1657 class_1657VarMethod_17782 = hit.method_17782();
        if (class_1657VarMethod_17782 instanceof class_1657) {
            class_1657 target = class_1657VarMethod_17782;
            if (MeleeAttacks.isEnemy(player, target) && !MeleeAttacks.shieldBroken(target) && blocksSoon(player, target)) {
                if ((MeleeAttacks.disablesShields(player.method_6047()) || bestAxe(player) >= 0) && MeleeAttacks.attack(mc, hit)) {
                    this.rearmTick = this.ticks + 10;
                    MeleeAttacks.claimTick(mc);
                }
            }
        }
    }

    private static boolean blocksSoon(class_746 player, class_1657 target) {
        if (!target.method_6115()) {
            return false;
        }
        class_1799 shield = target.method_6030();
        class_10707 blocks = (class_10707) shield.method_58694(class_9334.field_56396);
        if (blocks == null || target.method_6048() + leadTicks(player) < blocks.method_67197()) {
            return false;
        }
        return MeleeAttacks.shieldBlocks(target, player, shield);
    }

    private static int leadTicks(class_746 player) {
        class_640 info = player.field_3944.method_2871(player.method_5667());
        if (info == null) {
            return 0;
        }
        return class_3532.method_15340(Math.round(info.method_2959() / 50.0f), 0, 4);
    }

    static int bestAxe(class_746 player) {
        class_1661 inventory = player.method_31548();
        int best = -1;
        double bestDamage = Double.NEGATIVE_INFINITY;
        for (int slot = 0; slot < class_1661.method_7368(); slot++) {
            class_1799 stack = inventory.method_5438(slot);
            if (MeleeAttacks.disablesShields(stack) && !stack.method_63692()) {
                double damage = MeleeAttacks.attackDamage(stack);
                if (damage > bestDamage) {
                    best = slot;
                    bestDamage = damage;
                }
            }
        }
        return best;
    }
}
