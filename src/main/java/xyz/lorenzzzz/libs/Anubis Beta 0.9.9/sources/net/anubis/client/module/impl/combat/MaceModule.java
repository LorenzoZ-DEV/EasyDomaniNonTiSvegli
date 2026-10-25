package net.anubis.client.module.impl.combat;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Objects;
import java.util.Optional;
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
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.util.pvp.InputControl;
import net.anubis.client.util.pvp.ServerRotation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1887;
import net.minecraft.class_1893;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3966;
import net.minecraft.class_5134;
import net.minecraft.class_5321;
import net.minecraft.class_6880;
import net.minecraft.class_746;
import net.minecraft.class_9362;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/MaceModule.class */
@Environment(EnvType.CLIENT)
public final class MaceModule extends ClientModule {
    private static final String PICK_DAMAGE = "Damage";
    private static final String PICK_WIND_BURST = "Wind Burst";
    private static final String SMASH_DIRECT = "Direct";
    private static final String SMASH_DELAYED = "Delayed";
    private static final double REACH_MARGIN = 0.35d;
    private static final float BREACH_GAIN = 1.15f;
    private final BooleanSetting autoSwap;
    private final ModeSetting pick;
    private final BooleanSetting breachSwap;
    private final BooleanSetting swapBack;
    private final BooleanSetting smashAssist;
    private final ModeSetting smashTiming;
    private final NumberSetting minFall;
    private final BooleanSetting mobs;
    private final SlotReturn slots;
    private boolean smashed;

    public MaceModule() {
        super("mace", "Mace", "Swaps to the strongest mace for smash hits, with an optional smash assist.", ModuleCategory.COMBAT);
        this.autoSwap = (BooleanSetting) addSetting("auto-swap", new BooleanSetting("Auto Swap", true));
        this.pick = (ModeSetting) addSetting("pick", new ModeSetting("Pick", PICK_DAMAGE, PICK_DAMAGE, PICK_WIND_BURST));
        this.breachSwap = (BooleanSetting) addSetting("breach-swap", new BooleanSetting("Breach Swap", false));
        this.swapBack = (BooleanSetting) addSetting("swap-back", new BooleanSetting("Swap Back", true));
        this.smashAssist = (BooleanSetting) addSetting("smash-assist", new BooleanSetting("Smash Assist", false));
        this.smashTiming = (ModeSetting) addSetting("smash-timing", new ModeSetting("Smash Timing", SMASH_DELAYED, SMASH_DIRECT, SMASH_DELAYED));
        this.minFall = (NumberSetting) addSetting("min-fall", new NumberSetting("Min Fall", 3.0f, 1.5f, 30.0f).step(0.5f));
        this.mobs = (BooleanSetting) addSetting("mobs", new BooleanSetting("Mobs", false));
        this.slots = new SlotReturn(this);
        ModeSetting modeSetting = this.pick;
        BooleanSetting booleanSetting = this.autoSwap;
        Objects.requireNonNull(booleanSetting);
        modeSetting.visibleWhen(booleanSetting::get);
        BooleanSetting booleanSetting2 = this.breachSwap;
        BooleanSetting booleanSetting3 = this.autoSwap;
        Objects.requireNonNull(booleanSetting3);
        booleanSetting2.visibleWhen(booleanSetting3::get);
        this.swapBack.visibleWhen(() -> {
            return this.autoSwap.get() || this.smashAssist.get();
        });
        ModeSetting modeSetting2 = this.smashTiming;
        BooleanSetting booleanSetting4 = this.smashAssist;
        Objects.requireNonNull(booleanSetting4);
        modeSetting2.visibleWhen(booleanSetting4::get);
        NumberSetting numberSetting = this.minFall;
        BooleanSetting booleanSetting5 = this.smashAssist;
        Objects.requireNonNull(booleanSetting5);
        numberSetting.visibleWhen(booleanSetting5::get);
        BooleanSetting booleanSetting6 = this.mobs;
        BooleanSetting booleanSetting7 = this.smashAssist;
        Objects.requireNonNull(booleanSetting7);
        booleanSetting6.visibleWhen(booleanSetting7::get);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        this.smashed = false;
        this.slots.drop(class_310.method_1551(), this.swapBack.get());
    }

    public void beforeAttack(class_746 player, class_1297 target) {
        int slot;
        if (this.autoSwap.get() && (target instanceof class_1309)) {
            class_1309 living = (class_1309) target;
            if (!MeleeAttacks.shieldBlocks(living, player) || MeleeAttacks.shieldBroken(living)) {
                boolean smash = class_9362.method_58659(player);
                if ((smash || this.breachSwap.get()) && (slot = bestMace(player, living, smash)) >= 0) {
                    if (smash || expected(player, player.method_31548().method_5438(slot), living) >= expected(player, player.method_6047(), living) * BREACH_GAIN) {
                        this.slots.take(player, slot);
                    }
                }
            }
        }
    }

    private int bestMace(class_746 player, class_1309 target, boolean smash) {
        boolean windBurst = smash && this.pick.is(PICK_WIND_BURST);
        class_1661 inventory = player.method_31548();
        int best = -1;
        boolean bestBurst = false;
        float bestDamage = 0.0f;
        for (int slot = 0; slot < class_1661.method_7368(); slot++) {
            class_1799 stack = inventory.method_5438(slot);
            if (stack.method_31574(class_1802.field_49814) && !stack.method_63692() && (smash || level(stack, class_1893.field_50158) != 0)) {
                boolean burst = windBurst && level(stack, class_1893.field_50159) > 0;
                float damage = expected(player, stack, target);
                if (best < 0 || ((!bestBurst || burst) && (bestBurst != burst || damage > bestDamage))) {
                    best = slot;
                    bestBurst = burst;
                    bestDamage = damage;
                }
            }
        }
        return best;
    }

    private static float expected(class_746 player, class_1799 stack, class_1309 target) {
        float charge = MeleeAttacks.charge(player);
        double fall = player.field_6017;
        float damage = ((float) (1.0d + MeleeAttacks.attackDamage(player.method_6047()))) * (0.2f + (0.8f * charge * charge));
        if (stack.method_31574(class_1802.field_49814) && class_9362.method_58659(player)) {
            damage += (float) (smashBonus(fall) + (0.5d * ((double) level(stack, class_1893.field_50157)) * fall));
        }
        if (MeleeAttacks.wouldCrit(player, target)) {
            damage *= 1.5f;
        }
        int sharpness = level(stack, class_1893.field_9118);
        if (sharpness > 0) {
            damage += charge * ((0.5f * sharpness) + 0.5f);
        }
        return afterArmor(target, damage, level(stack, class_1893.field_50158));
    }

    private static double smashBonus(double fall) {
        if (fall <= 3.0d) {
            return 4.0d * fall;
        }
        return fall <= 8.0d ? 12.0d + (2.0d * (fall - 3.0d)) : (22.0d + fall) - 8.0d;
    }

    private static float afterArmor(class_1309 target, float damage, int breach) {
        float armor = target.method_6096();
        float toughness = (float) target.method_45325(class_5134.field_23725);
        float cut = class_3532.method_15363(armor - (damage / (2.0f + (toughness / 4.0f))), armor * 0.2f, 20.0f) / 25.0f;
        return damage * (1.0f - class_3532.method_15363(cut - (0.15f * breach), 0.0f, 1.0f));
    }

    private static int level(class_1799 stack, class_5321<class_1887> enchantment) {
        for (Object2IntMap.Entry<class_6880<class_1887>> entry : stack.method_58657().method_57539()) {
            if (((class_6880) entry.getKey()).method_40225(enchantment)) {
                return entry.getIntValue();
            }
        }
        return 0;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTick(TickEvent event) {
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 != null && mc.field_1724.method_24828()) {
            this.smashed = false;
        }
        this.slots.tick(mc, this.swapBack.get());
    }

    @EventHandler
    public void onSmashTick(TickEvent event) {
        if (this.smashAssist.get()) {
            trySmash(class_310.method_1551());
        }
    }

    private void trySmash(class_310 mc) {
        class_3966 hit;
        if (!MeleeAttacks.canAct(mc) || CombatMacroLock.heldByOther(this) || AutoInvTotemModule.wantsHotbar()) {
            return;
        }
        class_746 player = mc.field_1724;
        if (MeleeAttacks.actedThisTick(player) || MeleeAttacks.clickPending(mc) || MeleeAttacks.rightClickComing(mc) || ServerRotation.pending() || mc.field_1771 > 0 || !MeleeAttacks.handsFree(mc, player) || player.method_18798().field_1351 >= 0.0d || (hit = MeleeAttacks.crosshairHit(mc)) == null) {
            return;
        }
        class_1297 class_1297VarMethod_17782 = hit.method_17782();
        if (class_1297VarMethod_17782 instanceof class_1309) {
            class_1309 target = (class_1309) class_1297VarMethod_17782;
            if (MeleeAttacks.isEnemy(player, target)) {
                if (((target instanceof class_1657) || this.mobs.get()) && smashReady(player, target)) {
                    if (MeleeAttacks.shieldBlocks(target, player) && !MeleeAttacks.shieldBroken(target)) {
                        int axe = ShieldBreakerModule.bestAxe(player);
                        if (!canHoldOneTick(player, target) || axe < 0) {
                            return;
                        }
                        if ((this.autoSwap.get() || this.swapBack.get()) && this.slots.take(player, axe)) {
                            if (MeleeAttacks.attack(mc, hit)) {
                                MeleeAttacks.noteShieldBroken(target);
                                MeleeAttacks.claimTick(mc);
                                return;
                            } else {
                                this.slots.drop(mc, true);
                                return;
                            }
                        }
                        return;
                    }
                    if (holdsSmash(player, target)) {
                        InputControl.dropSprint();
                        mc.field_1771 = Math.max(mc.field_1771, 1);
                    } else if (MeleeAttacks.attack(mc, hit)) {
                        this.smashed = true;
                        MeleeAttacks.claimTick(mc);
                    }
                }
            }
        }
    }

    public void noteSmash() {
        this.smashed = true;
    }

    public boolean smashReady(class_746 player, class_1309 target) {
        return isEnabled() && !this.smashed && class_9362.method_58659(player) && player.field_6017 >= ((double) this.minFall.get()) && (player.method_6047().method_31574(class_1802.field_49814) || (this.autoSwap.get() && bestMace(player, target, true) >= 0));
    }

    public boolean holdsSmash(class_746 player, class_1309 target) {
        boolean chargeSoon = MeleeAttacks.charge(player) <= 0.9f && player.method_7261(1.5f) > 0.9f;
        return canHoldOneTick(player, target) && (this.smashTiming.is(SMASH_DELAYED) || chargeSoon || target.field_6235 > 0 || player.method_5624());
    }

    private static boolean canHoldOneTick(class_746 player, class_1309 target) {
        class_243 motion = player.method_18798();
        if (!player.method_73183().method_8587(player, player.method_5829().method_997(motion))) {
            return false;
        }
        double reach = player.method_55755() - REACH_MARGIN;
        if (reach <= 0.0d) {
            return false;
        }
        class_243 nextEye = player.method_33571().method_1019(motion);
        class_243 targetStep = target.method_73189().method_1023(target.field_6014, target.field_6036, target.field_5969);
        class_238 nextBox = target.method_5829().method_997(targetStep).method_1014(target.method_5871());
        if (nextBox.method_1006(nextEye)) {
            return true;
        }
        Optional<class_243> aim = nextBox.method_992(nextEye, nextEye.method_1019(player.method_5828(1.0f).method_1021(reach)));
        return aim.isPresent() && MeleeAttacks.lineIsClear(player, nextEye, aim.get());
    }
}
