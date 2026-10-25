package net.anubis.client.module.impl.combat;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.Objects;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.PlayerAttackEntityEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.CombatMacroLock;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.impl.combat.crystal.CrystalDamage;
import net.anubis.client.module.impl.combat.crystal.Crystals;
import net.anubis.client.module.impl.combat.melee.MeleeAttacks;
import net.anubis.client.module.impl.misc.AutoEatModule;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.util.pvp.ServerRotation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1661;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_239;
import net.minecraft.class_2680;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3489;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import net.minecraft.class_638;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/CrystalMacroModule.class */
@Environment(EnvType.CLIENT)
public final class CrystalMacroModule extends ClientModule {
    private static final int COUNTER_CAP = 1000;
    private static final int WEAPON_SETTLE_TICKS = 2;
    private final NumberSetting breakDelay;
    private final NumberSetting placeDelay;
    private final BooleanSetting autoObsidian;
    private final NumberSetting obsidianDelay;
    private final BooleanSetting switchBack;
    private boolean engaged;
    private int savedSlot;
    private int ownSlot;
    private int ticksSinceAction;
    private class_1799 lastMain;
    private int mainAge;
    private int pendingBreakId;
    private int pendingBreakAge;
    private class_2338 pendingCrystalBase;
    private int pendingCrystalAge;
    private class_2338 pendingObsidian;
    private int pendingObsidianAge;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/CrystalMacroModule$Step.class */
    @Environment(EnvType.CLIENT)
    private enum Step {
        BREAK,
        CRYSTAL,
        OBSIDIAN
    }

    public CrystalMacroModule() {
        super("crystal-macro", "Crystal Macro", "Breaks and places crystals while you hold right-click.", ModuleCategory.COMBAT);
        this.breakDelay = (NumberSetting) addSetting("break-delay", new NumberSetting("Break Delay", 0.0f, 0.0f, 10.0f).step(1.0f));
        this.placeDelay = (NumberSetting) addSetting("place-delay", new NumberSetting("Place Delay", 0.0f, 0.0f, 10.0f).step(1.0f));
        this.autoObsidian = (BooleanSetting) addSetting("auto-obsidian", new BooleanSetting("Auto Obsidian", true));
        this.obsidianDelay = (NumberSetting) addSetting("obsidian-delay", new NumberSetting("Obsidian Delay", 2.0f, 0.0f, 10.0f).step(1.0f));
        this.switchBack = (BooleanSetting) addSetting("switch-back", new BooleanSetting("Switch Back", true));
        this.savedSlot = -1;
        this.ownSlot = -1;
        this.ticksSinceAction = 1000;
        this.mainAge = 1000;
        this.pendingBreakId = -1;
        this.pendingBreakAge = 1000;
        this.pendingCrystalAge = 1000;
        this.pendingObsidianAge = 1000;
        NumberSetting numberSetting = this.obsidianDelay;
        BooleanSetting booleanSetting = this.autoObsidian;
        Objects.requireNonNull(booleanSetting);
        numberSetting.visibleWhen(booleanSetting::get);
    }

    @Override // net.anubis.client.module.ClientModule
    public int settingsVersion() {
        return 1;
    }

    @Override // net.anubis.client.module.ClientModule
    public void migrateSettings(int savedVersion) {
        if (savedVersion < 1) {
            this.breakDelay.set(this.breakDelay.get() - 1.0f);
            this.placeDelay.set(this.placeDelay.get() - 1.0f);
            this.obsidianDelay.set(this.obsidianDelay.get() - 1.0f);
        }
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        resetState();
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        class_310 mc = class_310.method_1551();
        endHold(mc);
        class_746 player = mc.field_1724;
        if (player != null && !hotbarTakenByOther()) {
            restoreSlot(player);
        }
        resetState();
    }

    @EventHandler
    public void onTick(TickEvent event) {
        class_310 mc = class_310.method_1551();
        ageCounters();
        class_746 player = mc.field_1724;
        if (player == null || mc.field_1687 == null || mc.field_1761 == null || player.method_29504()) {
            endHold(mc);
            forgetSlots();
            return;
        }
        class_1799 main = player.method_6047();
        if (main != this.lastMain) {
            this.lastMain = main;
            this.mainAge = 0;
        }
        if (!holdActive(mc, player)) {
            endHold(mc);
            if (mc.field_1755 != null || hotbarTakenByOther() || MeleeAttacks.actedThisTick(player)) {
                return;
            }
            restoreSlot(player);
        }
        if (AutoInvTotemModule.wantsHotbar() || CombatMacroLock.heldByOther(this)) {
            CombatMacroLock.release(this);
            if (this.engaged) {
                suppressVanillaUse(mc);
                return;
            }
            return;
        }
        if (this.engaged) {
            if (!keepEngaged(player)) {
                endHold(mc);
                return;
            }
        } else {
            if (player.method_6115()) {
                return;
            }
            if (!ownsSelection(player) && !activationAllows(player)) {
                return;
            }
        }
        Plan plan = plan(mc, player);
        if (this.engaged || plan != null || ownsSelection(player)) {
            this.engaged = true;
            CombatMacroLock.tryAcquire(this);
            suppressVanillaUse(mc);
            if (plan == null || !MeleeAttacks.handsFree(mc, player) || MeleeAttacks.actedThisTick(player) || ServerRotation.pending()) {
                return;
            }
            switch (plan.step()) {
                case BREAK:
                    breakCrystal(mc, player, plan);
                    break;
                case CRYSTAL:
                    if (useOnCrosshair(mc, player, this.placeDelay, plan)) {
                        this.pendingCrystalBase = plan.target();
                        this.pendingCrystalAge = 0;
                    }
                    break;
                case OBSIDIAN:
                    if (useOnCrosshair(mc, player, this.obsidianDelay, plan)) {
                        this.pendingObsidian = plan.target();
                        this.pendingObsidianAge = 0;
                    }
                    break;
            }
        }
    }

    @EventHandler
    public void onAttack(PlayerAttackEntityEvent event) {
        class_1297 class_1297Var = event.target;
        if (class_1297Var instanceof class_1511) {
            class_1511 crystal = (class_1511) class_1297Var;
            if (crystal.method_73183().method_8608()) {
                markBreak(crystal);
            }
        }
    }

    private boolean holdActive(class_310 mc, class_746 player) {
        return MeleeAttacks.canAct(mc) && mc.method_1569() && !player.method_7325() && Crystals.isPhysicallyDown(mc, mc.field_1690.field_1904);
    }

    private static boolean hotbarTakenByOther() {
        return AutoEatModule.isEating() || AutoInvTotemModule.wantsHotbar();
    }

    private boolean keepEngaged(class_746 player) {
        if (ownsSelection(player)) {
            return true;
        }
        forgetSlots();
        return activationAllows(player);
    }

    private boolean ownsSelection(class_746 player) {
        return this.ownSlot != -1 && player.method_31548().method_67532() == this.ownSlot;
    }

    private static boolean activationAllows(class_746 player) {
        class_1799 main = player.method_6047();
        class_1799 offhand = player.method_6079();
        if (main.method_31574(class_1802.field_8301)) {
            return true;
        }
        if (offhand.method_31574(class_1802.field_8301) && Crystals.isCombatItem(main)) {
            return true;
        }
        return (main.method_31573(class_3489.field_42611) || main.method_31573(class_3489.field_42612) || main.method_31574(class_1802.field_49814)) && (offhand.method_7960() || offhand.method_31574(class_1802.field_8288)) && Crystals.hotbarSlotOf(player.method_31548(), class_1802.field_8301) != -1;
    }

    private void endHold(class_310 mc) {
        CombatMacroLock.release(this);
        if (this.engaged) {
            this.engaged = false;
            mc.field_1690.field_1904.method_23481(mc.field_1755 == null && Crystals.isPhysicallyDown(mc, mc.field_1690.field_1904));
        }
    }

    private void restoreSlot(class_746 player) {
        if (this.ownSlot == -1) {
            return;
        }
        class_1661 inventory = player.method_31548();
        if (this.switchBack.get() && class_1661.method_7380(this.savedSlot) && this.savedSlot != this.ownSlot && inventory.method_67532() == this.ownSlot) {
            inventory.method_61496(this.savedSlot);
        }
        forgetSlots();
    }

    private void forgetSlots() {
        this.savedSlot = -1;
        this.ownSlot = -1;
    }

    private void resetState() {
        this.engaged = false;
        forgetSlots();
        this.ticksSinceAction = 1000;
        this.lastMain = null;
        this.mainAge = 1000;
        this.pendingBreakId = -1;
        this.pendingBreakAge = 1000;
        this.pendingCrystalBase = null;
        this.pendingCrystalAge = 1000;
        this.pendingObsidian = null;
        this.pendingObsidianAge = 1000;
        CombatMacroLock.release(this);
    }

    private static void suppressVanillaUse(class_310 mc) {
        class_304 use = mc.field_1690.field_1904;
        use.method_23481(false);
        while (use.method_1436()) {
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/CrystalMacroModule$Plan.class */
    @Environment(EnvType.CLIENT)
    private static final class Plan extends Record {
        private final Step step;
        private final class_1511 crystal;
        private final class_3965 blockHit;
        private final class_2338 target;
        private final int route;

        private Plan(Step step, class_1511 crystal, class_3965 blockHit, class_2338 target, int route) {
            this.step = step;
            this.crystal = crystal;
            this.blockHit = blockHit;
            this.target = target;
            this.route = route;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Plan.class), Plan.class, "step;crystal;blockHit;target;route", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalMacroModule$Plan;->step:Lnet/anubis/client/module/impl/combat/CrystalMacroModule$Step;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalMacroModule$Plan;->crystal:Lnet/minecraft/class_1511;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalMacroModule$Plan;->blockHit:Lnet/minecraft/class_3965;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalMacroModule$Plan;->target:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalMacroModule$Plan;->route:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Plan.class), Plan.class, "step;crystal;blockHit;target;route", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalMacroModule$Plan;->step:Lnet/anubis/client/module/impl/combat/CrystalMacroModule$Step;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalMacroModule$Plan;->crystal:Lnet/minecraft/class_1511;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalMacroModule$Plan;->blockHit:Lnet/minecraft/class_3965;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalMacroModule$Plan;->target:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalMacroModule$Plan;->route:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Plan.class, Object.class), Plan.class, "step;crystal;blockHit;target;route", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalMacroModule$Plan;->step:Lnet/anubis/client/module/impl/combat/CrystalMacroModule$Step;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalMacroModule$Plan;->crystal:Lnet/minecraft/class_1511;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalMacroModule$Plan;->blockHit:Lnet/minecraft/class_3965;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalMacroModule$Plan;->target:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalMacroModule$Plan;->route:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public Step step() {
            return this.step;
        }

        public class_1511 crystal() {
            return this.crystal;
        }

        public class_3965 blockHit() {
            return this.blockHit;
        }

        public class_2338 target() {
            return this.target;
        }

        public int route() {
            return this.route;
        }

        static Plan forBreak(class_1511 crystal, int weapon) {
            return new Plan(Step.BREAK, crystal, null, null, weapon);
        }

        static Plan forPlace(Step step, class_3965 blockHit, class_2338 target, int route) {
            return new Plan(step, null, blockHit, target, route);
        }
    }

    private Plan plan(class_310 mc, class_746 player) {
        int route;
        class_2338 target;
        int route2;
        int weapon;
        class_3966 class_3966VarMethod_76762 = player.method_76762(1.0f, player);
        int window = Crystals.confirmWindow(mc, player);
        if (this.pendingCrystalBase != null && Crystals.hasCrystal(mc.field_1687, this.pendingCrystalBase, null)) {
            this.pendingCrystalBase = null;
        }
        if (class_3966VarMethod_76762 instanceof class_3966) {
            class_3966 entityHit = class_3966VarMethod_76762;
            class_1511 class_1511VarMethod_17782 = entityHit.method_17782();
            if (!(class_1511VarMethod_17782 instanceof class_1511)) {
                return null;
            }
            class_1511 crystal = class_1511VarMethod_17782;
            if (!crystal.method_5805()) {
                return null;
            }
            if ((crystal.method_5628() == this.pendingBreakId && this.pendingBreakAge < window) || CrystalDamage.endangers(player, CrystalDamage.estimate(crystal.method_73189(), player)) || (weapon = Crystals.weaponRoute(player)) == -1) {
                return null;
            }
            return Plan.forBreak(crystal, weapon);
        }
        if (!(class_3966VarMethod_76762 instanceof class_3965)) {
            return null;
        }
        class_3965 blockHit = (class_3965) class_3966VarMethod_76762;
        if (blockHit.method_17783() != class_239.class_240.field_1332) {
            return null;
        }
        class_638 level = mc.field_1687;
        class_2338 pos = blockHit.method_17777();
        if (!level.method_8621().method_11952(pos)) {
            return null;
        }
        class_2680 state = level.method_8320(pos);
        if (Crystals.isBase(state)) {
            if ((!pos.equals(this.pendingCrystalBase) || this.pendingCrystalAge >= window) && Crystals.canHost(level, pos, null) && (route2 = Crystals.route(player, class_1802.field_8301, state, this.savedSlot)) != -1) {
                return Plan.forPlace(Step.CRYSTAL, blockHit, pos, route2);
            }
            return null;
        }
        if (!this.autoObsidian.get() || Crystals.route(player, class_1802.field_8301, class_2246.field_10540.method_9564(), this.savedSlot) == -1 || (route = Crystals.route(player, class_1802.field_8281, state, this.savedSlot)) == -1 || (target = Crystals.obsidianTarget(level, player, blockHit, state)) == null) {
            return null;
        }
        if (!target.equals(this.pendingObsidian) || this.pendingObsidianAge >= window) {
            return Plan.forPlace(Step.OBSIDIAN, blockHit, target, route);
        }
        return null;
    }

    private void breakCrystal(class_310 mc, class_746 player, Plan plan) {
        if (this.ticksSinceAction <= this.breakDelay.getInt()) {
            return;
        }
        if (plan.route() != -2) {
            select(player, plan.route());
        }
        if ((this.mainAge >= 2 || Crystals.enchantsAddDamage(player.method_6047()) || Crystals.dealsDamage(player, class_1799.field_8037)) && Crystals.strike(mc, player, plan.crystal())) {
            MeleeAttacks.claimTick(mc);
            markBreak(plan.crystal());
            this.ticksSinceAction = 0;
        }
    }

    private boolean useOnCrosshair(class_310 mc, class_746 player, NumberSetting delay, Plan plan) {
        if (this.ticksSinceAction <= delay.getInt()) {
            return false;
        }
        if (plan.route() != -2) {
            select(player, plan.route());
        }
        if (!Crystals.rightClick(mc, player, plan.blockHit())) {
            return false;
        }
        MeleeAttacks.claimTick(mc);
        this.ticksSinceAction = 0;
        return true;
    }

    private void select(class_746 player, int slot) {
        class_1661 inventory = player.method_31548();
        if (this.savedSlot == -1) {
            this.savedSlot = inventory.method_67532();
        }
        inventory.method_61496(slot);
        this.ownSlot = slot;
        this.lastMain = player.method_6047();
        this.mainAge = 0;
    }

    private void markBreak(class_1511 crystal) {
        this.pendingBreakId = crystal.method_5628();
        this.pendingBreakAge = 0;
    }

    private void ageCounters() {
        this.ticksSinceAction = Math.min(this.ticksSinceAction + 1, 1000);
        this.mainAge = Math.min(this.mainAge + 1, 1000);
        this.pendingBreakAge = Math.min(this.pendingBreakAge + 1, 1000);
        this.pendingCrystalAge = Math.min(this.pendingCrystalAge + 1, 1000);
        this.pendingObsidianAge = Math.min(this.pendingObsidianAge + 1, 1000);
    }
}
