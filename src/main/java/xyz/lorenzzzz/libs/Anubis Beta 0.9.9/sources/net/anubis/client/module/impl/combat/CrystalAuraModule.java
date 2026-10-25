package net.anubis.client.module.impl.combat;

import imgui.ImDrawList;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.PacketReceiveEvent;
import net.anubis.client.event.impl.PlayerAttackEntityEvent;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.CombatMacroLock;
import net.anubis.client.module.HudOverlayProvider;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.impl.combat.crystal.Combo;
import net.anubis.client.module.impl.combat.crystal.CrystalDamage;
import net.anubis.client.module.impl.combat.crystal.Crystals;
import net.anubis.client.module.impl.combat.crystal.HurtClock;
import net.anubis.client.module.impl.combat.crystal.ShotView;
import net.anubis.client.module.impl.combat.melee.MeleeAttacks;
import net.anubis.client.module.impl.misc.AutoEatModule;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.KeySetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.util.pvp.Rotation;
import net.anubis.client.util.pvp.RotationUtils;
import net.anubis.client.util.pvp.ServerRotation;
import net.anubis.client.util.pvp.TargetingUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1511;
import net.minecraft.class_1661;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2604;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import net.minecraft.class_638;
import net.minecraft.class_746;
import net.minecraft.class_8042;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/CrystalAuraModule.class */
@Environment(EnvType.CLIENT)
public final class CrystalAuraModule extends ClientModule implements HudOverlayProvider {
    private static final int COUNTER_CAP = 1000;
    private static final int IDLE_TICKS = 10;
    private static final float FACE_PLACE_HEALTH = 10.0f;
    private static final float FACE_PLACE_DAMAGE = 2.0f;
    private static final float ARMOR_LOW = 0.1f;
    private static final float BREAK_FACTOR = 0.6f;
    private static final double AIM_INSET = 0.1d;
    private static final int MAX_BREAK_ATTEMPTS = 3;
    private static final double CHAIN_RANGE = 12.0d;
    private static final int WEAPON_SETTLE_TICKS = 2;
    private static final float LOOK_BONUS = 2.0f;
    private static final int MAX_LEAD_TICKS = 5;
    private static final float LOOK_MATCH_DEGREES = 2.0f;
    private static final double PLACE_BREAK_MARGIN = 0.1d;
    private static final float ONE_LOOK_BONUS = 2.0f;
    private static final float OBSIDIAN_UPGRADE_BAR = 8.0f;
    private static final float OBSIDIAN_MIN_GAIN = 5.0f;
    private static final float OBSIDIAN_MIN_RATIO = 2.0f;
    private final BooleanSetting requireHold;
    private final KeySetting holdKey;
    private final NumberSetting placeDelay;
    private final NumberSetting breakDelay;
    private final NumberSetting minDamage;
    private final NumberSetting maxSelfDamage;
    private final BooleanSetting facePlace;
    private final KeySetting forceFacePlace;
    private final BooleanSetting autoObsidian;
    private final BooleanSetting switchBack;
    private final BooleanSetting render;
    private final ColorSetting renderColor;
    private final ShotView view;
    private boolean engaged;
    private int idleTicks;
    private int savedSlot;
    private int ownSlot;
    private int ticksSinceBreak;
    private int ticksSincePlace;
    private class_1799 lastMain;
    private int mainAge;
    private int pendingBreakId;
    private int pendingBreakAge;
    private class_2338 pendingObsidian;
    private int pendingObsidianAge;
    private final Int2IntOpenHashMap breakAttempts;
    private final Int2IntOpenHashMap doomed;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/CrystalAuraModule$Look.class */
    @Environment(EnvType.CLIENT)
    private enum Look {
        SILENT,
        CROSSHAIR
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/CrystalAuraModule$Step.class */
    @Environment(EnvType.CLIENT)
    private enum Step {
        BREAK,
        CRYSTAL,
        OBSIDIAN
    }

    public CrystalAuraModule() {
        super("crystal-aura", "Crystal Aura", "Places and breaks end crystals for the most damage.", ModuleCategory.COMBAT);
        this.requireHold = (BooleanSetting) addSetting("require-hold", new BooleanSetting("Require Hold", false));
        this.holdKey = (KeySetting) addSetting("hold-key", new KeySetting("Hold Key", 1003));
        this.placeDelay = (NumberSetting) addSetting("place-delay", new NumberSetting("Place Delay", 0.0f, 0.0f, FACE_PLACE_HEALTH).step(1.0f));
        this.breakDelay = (NumberSetting) addSetting("break-delay", new NumberSetting("Break Delay", 0.0f, 0.0f, FACE_PLACE_HEALTH).step(1.0f));
        this.minDamage = (NumberSetting) addSetting("min-damage", new NumberSetting("Min Damage", 4.0f, 0.0f, 20.0f).step(0.5f));
        this.maxSelfDamage = (NumberSetting) addSetting("max-self-damage", new NumberSetting("Max Self Damage", OBSIDIAN_UPGRADE_BAR, 0.0f, 20.0f).step(0.5f));
        this.facePlace = (BooleanSetting) addSetting("face-place", new BooleanSetting("Face Place", true));
        this.forceFacePlace = (KeySetting) addSetting("force-face-place", new KeySetting("Force Face Place", -1));
        this.autoObsidian = (BooleanSetting) addSetting("auto-obsidian", new BooleanSetting("Auto Obsidian", true));
        this.switchBack = (BooleanSetting) addSetting("switch-back", new BooleanSetting("Switch Back", true));
        this.render = (BooleanSetting) addSetting("render", new BooleanSetting("Render", true));
        this.renderColor = (ColorSetting) addSetting("render-color", new ColorSetting("Render Color", -2728705));
        this.view = new ShotView();
        this.savedSlot = -1;
        this.ownSlot = -1;
        this.ticksSinceBreak = 1000;
        this.ticksSincePlace = 1000;
        this.mainAge = 1000;
        this.pendingBreakId = -1;
        this.pendingBreakAge = 1000;
        this.pendingObsidianAge = 1000;
        this.breakAttempts = new Int2IntOpenHashMap();
        this.doomed = new Int2IntOpenHashMap();
        KeySetting keySetting = this.holdKey;
        BooleanSetting booleanSetting = this.requireHold;
        Objects.requireNonNull(booleanSetting);
        keySetting.visibleWhen(booleanSetting::get);
        ColorSetting colorSetting = this.renderColor;
        BooleanSetting booleanSetting2 = this.render;
        Objects.requireNonNull(booleanSetting2);
        colorSetting.visibleWhen(booleanSetting2::get);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        resetState();
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        class_746 player = class_310.method_1551().field_1724;
        if (player != null && !AutoEatModule.isEating() && !AutoInvTotemModule.wantsHotbar()) {
            restoreSlot(player);
        }
        resetState();
        this.view.clear();
    }

    @EventHandler
    public void onTick(TickEvent event) {
        class_310 mc = class_310.method_1551();
        ageCounters();
        class_746 player = mc.field_1724;
        if (player == null || mc.field_1687 == null || mc.field_1761 == null || player.method_29504()) {
            disengage(mc, null);
            Crystals.forgetRtt();
            return;
        }
        HurtClock.tick(player);
        forgetGone(mc.field_1687);
        class_1799 main = player.method_6047();
        if (main != this.lastMain) {
            this.lastMain = main;
            this.mainAge = 0;
        }
        Look look = ServerRotation.canRotate(player) ? Look.SILENT : Look.CROSSHAIR;
        boolean crystals = !this.requireHold.get() || Crystals.isKeyDown(mc, this.holdKey.value());
        AnchorAuraModule aura = (AnchorAuraModule) ModuleRegistry.get(AnchorAuraModule.class);
        boolean anchors = aura != null && aura.isEnabled() && look == Look.SILENT;
        if (!MeleeAttacks.canAct(mc) || player.method_7325() || (!crystals && !anchors)) {
            disengage(mc, player);
            return;
        }
        if (AutoInvTotemModule.wantsHotbar() || AutoCityModule.mining() || CombatMacroLock.heldByOther(this)) {
            CombatMacroLock.release(this);
            return;
        }
        if (this.ownSlot != -1 && !ownsSelection(player)) {
            forgetSlots();
        }
        class_1799 offhand = player.method_6079();
        boolean offhandUse = (Crystals.isCombatItem(offhand) || offhand.method_31574(class_1802.field_8301) || offhand.method_31574(class_1802.field_8281)) ? false : true;
        if (MeleeAttacks.rightClickComing(mc) && (offhandUse || main.method_31574(class_1802.field_23141) || main.method_31574(class_1802.field_8801))) {
            disengage(mc, player);
            return;
        }
        if (MeleeAttacks.actedThisTick(player) || !MeleeAttacks.handsFree(mc, player) || (!ownsSelection(player) && !mayTakeHand(player, anchors))) {
            idle(mc, player);
            return;
        }
        List<class_1309> enemies = Combo.enemies(mc, player);
        if (enemies.isEmpty()) {
            disengage(mc, player);
            return;
        }
        Rotation fixedLook = null;
        if (look == Look.CROSSHAIR) {
            fixedLook = RotationUtils.entityRotation(player);
            if (ServerRotation.pending() || !sameLook(ServerRotation.sent(), fixedLook)) {
                idle(mc, player);
                return;
            }
        }
        int window = Crystals.confirmWindow(mc, player);
        class_1511 lastHit = lastHit(mc.field_1687, window);
        Fight fight = new Fight(this, player, mc.field_1687, enemies, Combo.friends(mc, player), window, Crystals.rttTicks(mc, player), fixedLook, look == Look.SILENT ? 2.0f : 0.0f, Crystals.isKeyDown(mc, this.forceFacePlace.value()));
        Combo.Shot shot = choose(mc, fight, lastHit, crystals, anchors ? aura : null);
        if (shot == null) {
            idle(mc, player);
            return;
        }
        engage();
        Object objPlan = shot.plan();
        if (objPlan instanceof Plan) {
            Plan plan = (Plan) objPlan;
            if (this.render.get()) {
                show(fight, plan);
            }
            if (perform(mc, player, fight, plan, look) && look == Look.SILENT) {
                Plan following = planCrystal(fight, lastHit(mc.field_1687, window), 1, stagedBreak(fight));
                Rotation sent = ServerRotation.sent();
                ServerRotation.request((following == null || fight.holds(following)) ? sent : fight.lookAt(following.aim()));
                return;
            }
            return;
        }
        aura.show(shot);
        if (fire(mc, player, fight, aura, shot)) {
            ServerRotation.request(ServerRotation.sent());
        }
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        if (this.render.get()) {
            this.view.draw();
        }
    }

    @Override // net.anubis.client.module.HudOverlayProvider
    public boolean isHudOverlayEnabled() {
        return isEnabled() && this.render.get() && this.view.showing();
    }

    @Override // net.anubis.client.module.HudOverlayProvider
    public void renderHudOverlay(ImDrawList dl, float screenW, float screenH) {
        this.view.label(dl, screenW, screenH);
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

    @EventHandler
    public void onPacketReceive(PacketReceiveEvent event) {
        class_746 player = class_310.method_1551().field_1724;
        if (player == null) {
            return;
        }
        class_8042 class_8042Var = event.packet;
        if (class_8042Var instanceof class_8042) {
            class_8042 bundle = class_8042Var;
            for (class_2596<?> packet : bundle.method_48324()) {
                note(player, packet);
            }
        } else {
            note(player, event.packet);
        }
        HurtClock.note(player, event.packet);
    }

    private void note(class_746 player, class_2596<?> packet) {
        if (packet instanceof class_2604) {
            class_2604 spawn = (class_2604) packet;
            if (spawn.method_11169() == class_1299.field_6110) {
                Crystals.noteSpawn(player, class_2338.method_49637(spawn.method_11175(), spawn.method_11174() - 1.0d, spawn.method_11176()));
            }
        }
    }

    private static boolean mayTakeHand(class_746 player, boolean anchors) {
        class_1799 main = player.method_6047();
        return main.method_31574(class_1802.field_8301) || main.method_31574(class_1802.field_8281) || Crystals.isCombatItem(main) || (anchors && (main.method_31574(class_1802.field_23141) || main.method_31574(class_1802.field_8801)));
    }

    private boolean ownsSelection(class_746 player) {
        return this.ownSlot != -1 && player.method_31548().method_67532() == this.ownSlot;
    }

    private void engage() {
        this.engaged = true;
        this.idleTicks = 0;
        CombatMacroLock.tryAcquire(this);
    }

    private void idle(class_310 mc, class_746 player) {
        if (this.engaged) {
            int i = this.idleTicks + 1;
            this.idleTicks = i;
            if (i < 10) {
                return;
            }
        }
        disengage(mc, player);
    }

    private void disengage(class_310 mc, class_746 player) {
        this.engaged = false;
        this.idleTicks = 0;
        CombatMacroLock.release(this);
        if (player == null) {
            forgetSlots();
        } else if (mc.field_1755 == null && !AutoEatModule.isEating() && !AutoInvTotemModule.wantsHotbar() && !MeleeAttacks.actedThisTick(player)) {
            restoreSlot(player);
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
        this.idleTicks = 0;
        forgetSlots();
        this.ticksSinceBreak = 1000;
        this.ticksSincePlace = 1000;
        this.lastMain = null;
        this.mainAge = 1000;
        this.pendingBreakId = -1;
        this.pendingBreakAge = 1000;
        this.pendingObsidian = null;
        this.pendingObsidianAge = 1000;
        this.breakAttempts.clear();
        this.doomed.clear();
        Crystals.forgetRtt();
        CombatMacroLock.release(this);
    }

    private void forgetGone(class_638 level) {
        forgetGone(level, this.breakAttempts.keySet());
        forgetGone(level, this.doomed.keySet());
    }

    private static void forgetGone(class_638 level, IntSet ids) {
        IntIterator iterator = ids.iterator();
        while (iterator.hasNext()) {
            if (level.method_8469(iterator.nextInt()) == null) {
                iterator.remove();
            }
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/CrystalAuraModule$Plan.class */
    @Environment(EnvType.CLIENT)
    private static final class Plan extends Record {
        private final Step step;
        private final class_1511 crystal;
        private final class_2338 clicked;
        private final class_2338 target;
        private final class_243 aim;
        private final int route;

        private Plan(Step step, class_1511 crystal, class_2338 clicked, class_2338 target, class_243 aim, int route) {
            this.step = step;
            this.crystal = crystal;
            this.clicked = clicked;
            this.target = target;
            this.aim = aim;
            this.route = route;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Plan.class), Plan.class, "step;crystal;clicked;target;aim;route", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Plan;->step:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Step;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Plan;->crystal:Lnet/minecraft/class_1511;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Plan;->clicked:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Plan;->target:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Plan;->aim:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Plan;->route:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Plan.class), Plan.class, "step;crystal;clicked;target;aim;route", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Plan;->step:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Step;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Plan;->crystal:Lnet/minecraft/class_1511;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Plan;->clicked:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Plan;->target:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Plan;->aim:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Plan;->route:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Plan.class, Object.class), Plan.class, "step;crystal;clicked;target;aim;route", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Plan;->step:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Step;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Plan;->crystal:Lnet/minecraft/class_1511;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Plan;->clicked:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Plan;->target:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Plan;->aim:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Plan;->route:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public Step step() {
            return this.step;
        }

        public class_1511 crystal() {
            return this.crystal;
        }

        public class_2338 clicked() {
            return this.clicked;
        }

        public class_2338 target() {
            return this.target;
        }

        public class_243 aim() {
            return this.aim;
        }

        public int route() {
            return this.route;
        }

        Plan withRoute(int newRoute) {
            return new Plan(this.step, this.crystal, this.clicked, this.target, this.aim, newRoute);
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/CrystalAuraModule$Candidate.class */
    @Environment(EnvType.CLIENT)
    private static final class Candidate extends Record {
        private final Plan plan;
        private final float bound;

        private Candidate(Plan plan, float bound) {
            this.plan = plan;
            this.bound = bound;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Candidate.class), Candidate.class, "plan;bound", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Candidate;->plan:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Plan;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Candidate;->bound:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Candidate.class), Candidate.class, "plan;bound", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Candidate;->plan:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Plan;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Candidate;->bound:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Candidate.class, Object.class), Candidate.class, "plan;bound", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Candidate;->plan:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Plan;", "FIELD:Lnet/anubis/client/module/impl/combat/CrystalAuraModule$Candidate;->bound:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public Plan plan() {
            return this.plan;
        }

        public float bound() {
            return this.bound;
        }
    }

    private static boolean oneLook(class_243 eye, class_238 crystal, class_243 placeAim, double reach) {
        class_243 direction = placeAim.method_1020(eye).method_1029();
        return crystal.method_992(eye, eye.method_1019(direction.method_1021(reach))).isPresent();
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/CrystalAuraModule$Side.class */
    @Environment(EnvType.CLIENT)
    private static final class Side {
        final List<CrystalDamage.Victim> enemies = new ArrayList();
        final List<CrystalDamage.Victim> friends = new ArrayList();
        final CrystalDamage.Victim self;

        Side(class_746 player, List<class_1309> targets, List<class_1309> friends, int lead, int selfLead) {
            for (class_1309 target : targets) {
                this.enemies.add(new CrystalDamage.Victim(target, Combo.lead(target, lead)));
            }
            for (class_1309 friend : friends) {
                this.friends.add(new CrystalDamage.Victim(friend, Combo.lead(friend, lead)));
            }
            this.self = new CrystalDamage.Victim(player, Combo.lead(player, selfLead));
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/CrystalAuraModule$Fight.class */
    @Environment(EnvType.CLIENT)
    private final class Fight {
        final class_746 player;
        final class_638 level;
        final class_243 eye;
        final class_243 aimEye;
        final List<class_1309> enemies;
        final List<class_1309> friends;
        final int window;
        final int rtt;
        final Rotation fixedLook;
        final Rotation serverLook;
        final float lookBonus;
        final int arriveLead;
        private final int placeLead;
        private final int selfPlaceLead;
        final boolean forced;
        private Side breakSide;
        private Side placeSide;

        Fight(CrystalAuraModule crystalAuraModule, class_746 player, class_638 level, List<class_1309> enemies, List<class_1309> friends, int window, int rtt, Rotation fixedLook, float lookBonus, boolean forced) {
            this.player = player;
            this.level = level;
            this.eye = player.method_33571();
            this.aimEye = fixedLook != null ? this.eye : this.eye.method_1019(player.method_18798());
            this.enemies = enemies;
            this.friends = friends;
            this.window = window;
            this.rtt = rtt;
            this.fixedLook = fixedLook;
            this.serverLook = ServerRotation.sent();
            this.lookBonus = lookBonus;
            this.arriveLead = Math.min(rtt, 5);
            this.placeLead = Math.min(2 * rtt, 5);
            this.selfPlaceLead = Math.min(rtt, 5);
            this.forced = forced;
        }

        Side side(Step step) {
            if (step == Step.BREAK) {
                if (this.breakSide == null) {
                    this.breakSide = new Side(this.player, this.enemies, this.friends, this.arriveLead, 0);
                }
                return this.breakSide;
            }
            if (this.placeSide == null) {
                this.placeSide = new Side(this.player, this.enemies, this.friends, this.placeLead, this.selfPlaceLead);
            }
            return this.placeSide;
        }

        Rotation lookAt(class_243 aim) {
            return this.fixedLook != null ? this.fixedLook : RotationUtils.fromTo(this.aimEye, aim);
        }

        boolean holds(Plan plan) {
            return CrystalAuraModule.hitAlong(this.player, plan, this.eye, this.serverLook) != null;
        }

        float lookCredit(Plan plan) {
            if (this.lookBonus <= 0.0f || !holds(plan)) {
                return 0.0f;
            }
            return this.lookBonus;
        }
    }

    private boolean canHostSoon(Fight fight, class_2338 base, class_1297 breaking) {
        class_2338 above = base.method_10084();
        if (!fight.level.method_22347(above)) {
            return false;
        }
        class_238 space = new class_238(above.method_10263(), above.method_10264(), above.method_10260(), ((double) above.method_10263()) + 1.0d, ((double) above.method_10264()) + 2.0d, ((double) above.method_10260()) + 1.0d);
        for (class_1309 class_1309Var : fight.level.method_8335(breaking, space)) {
            if (class_1309Var.method_5805()) {
                if (!(class_1309Var instanceof class_1309)) {
                    return false;
                }
                class_1309 living = class_1309Var;
                if (!fight.enemies.contains(living)) {
                    return false;
                }
                class_238 then = living.method_5829().method_997(Combo.lead(living, fight.arriveLead));
                boolean over = then.field_1322 >= space.field_1325;
                boolean aside = then.field_1320 <= space.field_1323 || then.field_1323 >= space.field_1320 || then.field_1324 <= space.field_1321 || then.field_1321 >= space.field_1324;
                if (!over && !aside) {
                    return false;
                }
            }
        }
        return true;
    }

    private class_1511 lastHit(class_638 level, int window) {
        if (this.pendingBreakId < 0 || this.pendingBreakAge >= window) {
            return null;
        }
        class_1511 class_1511VarMethod_8469 = level.method_8469(this.pendingBreakId);
        if (class_1511VarMethod_8469 instanceof class_1511) {
            class_1511 crystal = class_1511VarMethod_8469;
            if (crystal.method_5805()) {
                return crystal;
            }
        }
        return null;
    }

    private Combo.Shot choose(class_310 mc, Fight fight, class_1511 lastHit, boolean crystals, AnchorAuraModule aura) {
        List<Combo.Shot> shots = new ArrayList<>();
        Combo.Shot aimed = null;
        float rival = 0.0f;
        if (crystals) {
            Plan opening = stagedBreak(fight);
            Plan plan = planCrystal(fight, lastHit, 0, opening);
            if (plan != null) {
                aimed = shot(fight, plan, false);
                if (due(plan, 0)) {
                    shots.add(aimed);
                }
            }
            if (aura != null && opening != null) {
                rival = shot(fight, opening, true).value();
            }
        }
        if (aura != null) {
            shots.addAll(aura.shots(mc, fight.player, fight.enemies, fight.friends, fight.rtt, rival));
        }
        Combo.Shot picked = Combo.pick(shots);
        return picked != null ? picked : aimed;
    }

    private void show(Fight fight, Plan plan) {
        Side side = fight.side(plan.step());
        boolean baseToCome = plan.step() == Step.OBSIDIAN;
        class_243 center = plan.step() == Step.BREAK ? plan.crystal().method_73189() : Crystals.explosionAt(plan.target());
        float most = 0.0f;
        for (CrystalDamage.Victim enemy : side.enemies) {
            most = Math.max(most, enemy.damage(center, baseToCome));
        }
        class_2338 base = plan.step() == Step.BREAK ? plan.crystal().method_24515().method_10074() : plan.target();
        this.view.show(new class_238(base), this.renderColor.argb(), most, side.self.damage(center, baseToCome));
    }

    private Plan planCrystal(Fight fight, class_1511 lastHit, int ahead, Plan opening) {
        Plan planPlanPlacement;
        Plan breakPlan = planBreak(fight, false);
        if (breakPlan != null && due(breakPlan, ahead)) {
            return breakPlan;
        }
        boolean staged = (breakPlan == null && opening == null && !Crystals.anyPlacing(fight.player, fight.window)) ? false : true;
        if (staged) {
            planPlanPlacement = null;
        } else {
            planPlanPlacement = planPlacement(fight, lastHit, lastHit == null);
        }
        Plan placePlan = planPlanPlacement;
        return placePlan != null ? placePlan : breakPlan;
    }

    private Plan stagedBreak(Fight fight) {
        return planBreak(fight, true);
    }

    private Combo.Shot shot(Fight fight, Plan plan, boolean opening) {
        int i;
        Step step = plan.step();
        class_243 center = step == Step.BREAK ? plan.crystal().method_73189() : Crystals.explosionAt(plan.target());
        if (step == Step.BREAK) {
            i = 0;
        } else {
            i = step == Step.CRYSTAL ? fight.rtt : fight.rtt + 1;
        }
        int readyIn = i;
        return new Combo.Shot(Combo.Kind.valueOf(step.name()), plan.aim(), plan.route(), score(fight, step, center, Float.NEGATIVE_INFINITY, opening), kills(fight, step, center, opening), readyIn, plan);
    }

    private Plan planBreak(Fight fight, boolean opening) {
        int weapon;
        Plan plan;
        double reach = fight.player.method_55755();
        Plan best = null;
        float bestScore = Float.NEGATIVE_INFINITY;
        class_238 around = fight.player.method_5829().method_1014(reach + 2.0d);
        for (class_1511 crystal : fight.level.method_18467(class_1511.class, around)) {
            if (crystal.method_5805() && this.breakAttempts.get(crystal.method_5628()) < 3 && (crystal.method_5628() != this.pendingBreakId || this.pendingBreakAge >= fight.window)) {
                if (!this.doomed.containsKey(crystal.method_5628()) || fight.player.field_6012 - this.doomed.get(crystal.method_5628()) >= fight.window) {
                    class_243 eye = fight.eye;
                    if (eye.method_1025(TargetingUtils.closestPoint(eye, crystal.method_5829())) <= reach * reach) {
                        class_243 center = crystal.method_73189();
                        if (bound(fight, Step.BREAK, center) + fight.lookBonus > bestScore && (plan = breakLook(fight, crystal)) != null) {
                            float score = score(fight, Step.BREAK, center, bestScore - fight.lookBonus, opening);
                            if (score != Float.NEGATIVE_INFINITY) {
                                float score2 = score + fight.lookCredit(plan);
                                if (score2 > bestScore) {
                                    best = plan;
                                    bestScore = score2;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (best == null || (weapon = Crystals.weaponRoute(fight.player)) == -1) {
            return null;
        }
        return best.withRoute(weapon);
    }

    private Plan breakLook(Fight fight, class_1511 crystal) {
        if (fight.fixedLook != null) {
            Plan plan = new Plan(Step.BREAK, crystal, null, null, crystal.method_5829().method_1005(), -2);
            if (hitAlong(fight.player, plan, fight.eye, fight.fixedLook) != null) {
                return plan;
            }
            return null;
        }
        class_2338 base = crystal.method_24515().method_10074();
        if (Crystals.isBase(fight.level.method_8320(base))) {
            Plan throughBase = new Plan(Step.BREAK, crystal, base, base, aimOnTop(fight.level, base), -2);
            if (hitAlong(fight.player, throughBase, fight.aimEye, fight.lookAt(throughBase.aim())) != null) {
                return throughBase;
            }
        }
        class_243 nearest = TargetingUtils.closestPoint(fight.aimEye, crystal.method_5829().method_1011(0.1d));
        Plan plan2 = new Plan(Step.BREAK, crystal, null, null, nearest, -2);
        if (hitAlong(fight.player, plan2, fight.aimEye, fight.lookAt(nearest)) != null) {
            return plan2;
        }
        return null;
    }

    private Plan planPlacement(Fight fight, class_1511 breaking, boolean mayLayBase) {
        Plan crystal = planBase(fight, Step.CRYSTAL, breaking);
        if (!this.autoObsidian.get() || !mayLayBase) {
            return crystal;
        }
        float crystalDamage = crystal == null ? 0.0f : enemyDamage(fight, crystal);
        if (crystal != null && crystalDamage >= OBSIDIAN_UPGRADE_BAR) {
            return crystal;
        }
        Plan obsidian = planBase(fight, Step.OBSIDIAN, null);
        if (obsidian == null || crystal == null) {
            return obsidian == null ? crystal : obsidian;
        }
        float obsidianDamage = enemyDamage(fight, obsidian);
        boolean upgrade = obsidianDamage >= crystalDamage + 5.0f && obsidianDamage >= crystalDamage * 2.0f;
        return upgrade ? obsidian : crystal;
    }

    private float enemyDamage(Fight fight, Plan plan) {
        class_243 center = Crystals.explosionAt(plan.target());
        float most = 0.0f;
        for (CrystalDamage.Victim enemy : fight.side(plan.step()).enemies) {
            most = Math.max(most, enemy.damage(center, plan.step() == Step.OBSIDIAN));
        }
        return most;
    }

    /* JADX WARN: Code duplicated, block: B:118:0x01ef A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:120:0x01ef A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:121:0x01ef A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:122:0x01ef A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:39:0x0104  */
    /* JADX WARN: Code duplicated, block: B:44:0x012d  */
    /* JADX WARN: Code duplicated, block: B:46:0x0134  */
    /* JADX WARN: Code duplicated, block: B:49:0x0145  */
    /* JADX WARN: Code duplicated, block: B:51:0x0151  */
    /* JADX WARN: Code duplicated, block: B:54:0x015f  */
    /* JADX WARN: Code duplicated, block: B:56:0x016d  */
    /* JADX WARN: Code duplicated, block: B:57:0x0172  */
    /* JADX WARN: Code duplicated, block: B:61:0x0195  */
    /* JADX WARN: Code duplicated, block: B:64:0x01b4  */
    /* JADX WARN: Code duplicated, block: B:67:0x01cd  */
    private Plan planBase(Fight fight, Step step, class_1511 breaking) {
        int route;
        Plan plan;
        class_2338 base;
        class_2338 class_2338VarMethod_10062;
        class_2338 clicked;
        class_243 aim;
        class_238 crystal;
        float bound;
        class_746 player = fight.player;
        class_638 level = fight.level;
        class_1792 item = step == Step.CRYSTAL ? class_1802.field_8301 : class_1802.field_8281;
        class_2680 anyBase = class_2246.field_10540.method_9564();
        if (Crystals.route(player, item, anyBase, this.savedSlot) == -1) {
            return null;
        }
        if (step == Step.OBSIDIAN && Crystals.route(player, class_1802.field_8301, anyBase, this.savedSlot) == -1) {
            return null;
        }
        double placeReach = player.method_55754();
        double breakable = player.method_55755() - 0.1d;
        class_243 eye = fight.eye;
        class_2338 origin = class_2338.method_49638(eye);
        int radius = class_3532.method_15384(placeReach);
        float cycle = (step != Step.CRYSTAL || fight.lookBonus <= 0.0f) ? 0.0f : 2.0f;
        List<Candidate> candidates = new ArrayList<>();
        class_2338.class_2339 cell = new class_2338.class_2339();
        class_2338.class_2339 below = new class_2338.class_2339();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    cell.method_25504(origin, dx, dy, dz);
                    class_2680 state = level.method_8320(cell);
                    if (step == Step.CRYSTAL) {
                        if (Crystals.isBase(state)) {
                            below.method_25505(cell, class_2350.field_11033);
                            if (step == Step.OBSIDIAN || level.method_8320(below).method_26206(level, below, class_2350.field_11036)) {
                                if (step == Step.CRYSTAL) {
                                    if (!Crystals.placing(player, cell, fight.window)) {
                                        base = cell.method_10062();
                                        if (step == Step.CRYSTAL) {
                                            class_2338VarMethod_10062 = base;
                                        } else {
                                            class_2338VarMethod_10062 = below.method_10062();
                                        }
                                        clicked = class_2338VarMethod_10062;
                                        aim = aimOnTop(level, clicked);
                                        if (eye.method_1025(aim) > placeReach * placeReach) {
                                            crystal = Crystals.boxOn(base);
                                            if (eye.method_1025(TargetingUtils.closestPoint(eye, crystal)) > breakable * breakable) {
                                                bound = bound(fight, step, Crystals.explosionAt(base));
                                                if (bound == Float.NEGATIVE_INFINITY) {
                                                    candidates.add(new Candidate(new Plan(step, null, clicked, base, aim, -2), bound));
                                                }
                                            }
                                        }
                                    }
                                } else if (cell.equals(this.pendingObsidian) || this.pendingObsidianAge >= fight.window) {
                                    base = cell.method_10062();
                                    if (step == Step.CRYSTAL) {
                                        class_2338VarMethod_10062 = base;
                                    } else {
                                        class_2338VarMethod_10062 = below.method_10062();
                                    }
                                    clicked = class_2338VarMethod_10062;
                                    aim = aimOnTop(level, clicked);
                                    if (eye.method_1025(aim) > placeReach * placeReach) {
                                        crystal = Crystals.boxOn(base);
                                        if (eye.method_1025(TargetingUtils.closestPoint(eye, crystal)) > breakable * breakable) {
                                            bound = bound(fight, step, Crystals.explosionAt(base));
                                            if (bound == Float.NEGATIVE_INFINITY) {
                                                candidates.add(new Candidate(new Plan(step, null, clicked, base, aim, -2), bound));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (state.method_45474()) {
                        below.method_25505(cell, class_2350.field_11033);
                        if (step == Step.OBSIDIAN) {
                            if (step == Step.CRYSTAL) {
                                if (!Crystals.placing(player, cell, fight.window)) {
                                    base = cell.method_10062();
                                    if (step == Step.CRYSTAL) {
                                        class_2338VarMethod_10062 = base;
                                    } else {
                                        class_2338VarMethod_10062 = below.method_10062();
                                    }
                                    clicked = class_2338VarMethod_10062;
                                    aim = aimOnTop(level, clicked);
                                    if (eye.method_1025(aim) > placeReach * placeReach) {
                                        crystal = Crystals.boxOn(base);
                                        if (eye.method_1025(TargetingUtils.closestPoint(eye, crystal)) > breakable * breakable) {
                                            bound = bound(fight, step, Crystals.explosionAt(base));
                                            if (bound == Float.NEGATIVE_INFINITY) {
                                                candidates.add(new Candidate(new Plan(step, null, clicked, base, aim, -2), bound));
                                            }
                                        }
                                    }
                                }
                            } else if (cell.equals(this.pendingObsidian)) {
                                base = cell.method_10062();
                                if (step == Step.CRYSTAL) {
                                    class_2338VarMethod_10062 = base;
                                } else {
                                    class_2338VarMethod_10062 = below.method_10062();
                                }
                                clicked = class_2338VarMethod_10062;
                                aim = aimOnTop(level, clicked);
                                if (eye.method_1025(aim) > placeReach * placeReach) {
                                    crystal = Crystals.boxOn(base);
                                    if (eye.method_1025(TargetingUtils.closestPoint(eye, crystal)) > breakable * breakable) {
                                        bound = bound(fight, step, Crystals.explosionAt(base));
                                        if (bound == Float.NEGATIVE_INFINITY) {
                                            candidates.add(new Candidate(new Plan(step, null, clicked, base, aim, -2), bound));
                                        }
                                    }
                                }
                            } else {
                                base = cell.method_10062();
                                if (step == Step.CRYSTAL) {
                                    class_2338VarMethod_10062 = base;
                                } else {
                                    class_2338VarMethod_10062 = below.method_10062();
                                }
                                clicked = class_2338VarMethod_10062;
                                aim = aimOnTop(level, clicked);
                                if (eye.method_1025(aim) > placeReach * placeReach) {
                                    crystal = Crystals.boxOn(base);
                                    if (eye.method_1025(TargetingUtils.closestPoint(eye, crystal)) > breakable * breakable) {
                                        bound = bound(fight, step, Crystals.explosionAt(base));
                                        if (bound == Float.NEGATIVE_INFINITY) {
                                            candidates.add(new Candidate(new Plan(step, null, clicked, base, aim, -2), bound));
                                        }
                                    }
                                }
                            }
                        } else if (step == Step.CRYSTAL) {
                            if (!Crystals.placing(player, cell, fight.window)) {
                                base = cell.method_10062();
                                if (step == Step.CRYSTAL) {
                                    class_2338VarMethod_10062 = base;
                                } else {
                                    class_2338VarMethod_10062 = below.method_10062();
                                }
                                clicked = class_2338VarMethod_10062;
                                aim = aimOnTop(level, clicked);
                                if (eye.method_1025(aim) > placeReach * placeReach) {
                                    crystal = Crystals.boxOn(base);
                                    if (eye.method_1025(TargetingUtils.closestPoint(eye, crystal)) > breakable * breakable) {
                                        bound = bound(fight, step, Crystals.explosionAt(base));
                                        if (bound == Float.NEGATIVE_INFINITY) {
                                            candidates.add(new Candidate(new Plan(step, null, clicked, base, aim, -2), bound));
                                        }
                                    }
                                }
                            }
                        } else if (cell.equals(this.pendingObsidian)) {
                            base = cell.method_10062();
                            if (step == Step.CRYSTAL) {
                                class_2338VarMethod_10062 = base;
                            } else {
                                class_2338VarMethod_10062 = below.method_10062();
                            }
                            clicked = class_2338VarMethod_10062;
                            aim = aimOnTop(level, clicked);
                            if (eye.method_1025(aim) > placeReach * placeReach) {
                                crystal = Crystals.boxOn(base);
                                if (eye.method_1025(TargetingUtils.closestPoint(eye, crystal)) > breakable * breakable) {
                                    bound = bound(fight, step, Crystals.explosionAt(base));
                                    if (bound == Float.NEGATIVE_INFINITY) {
                                        candidates.add(new Candidate(new Plan(step, null, clicked, base, aim, -2), bound));
                                    }
                                }
                            }
                        } else {
                            base = cell.method_10062();
                            if (step == Step.CRYSTAL) {
                                class_2338VarMethod_10062 = base;
                            } else {
                                class_2338VarMethod_10062 = below.method_10062();
                            }
                            clicked = class_2338VarMethod_10062;
                            aim = aimOnTop(level, clicked);
                            if (eye.method_1025(aim) > placeReach * placeReach) {
                                crystal = Crystals.boxOn(base);
                                if (eye.method_1025(TargetingUtils.closestPoint(eye, crystal)) > breakable * breakable) {
                                    bound = bound(fight, step, Crystals.explosionAt(base));
                                    if (bound == Float.NEGATIVE_INFINITY) {
                                        candidates.add(new Candidate(new Plan(step, null, clicked, base, aim, -2), bound));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        candidates.sort(Comparator.comparingDouble((v0) -> {
            return v0.bound();
        }).reversed());
        Plan best = null;
        float bestScore = Float.NEGATIVE_INFINITY;
        for (Candidate candidate : candidates) {
            if (candidate.bound() + fight.lookBonus + cycle <= bestScore) {
                break;
            }
            Plan found = candidate.plan();
            float score = score(fight, step, Crystals.explosionAt(found.target()), (bestScore - fight.lookBonus) - cycle, false);
            if (score != Float.NEGATIVE_INFINITY && level.method_8621().method_11952(found.clicked())) {
                if (canHostSoon(fight, found.target(), step == Step.CRYSTAL ? breaking : null) && (plan = reachable(fight, found)) != null) {
                    float score2 = score + fight.lookCredit(plan);
                    if (cycle > 0.0f && oneLook(fight.aimEye, Crystals.boxOn(plan.target()), plan.aim(), breakable)) {
                        score2 += cycle;
                    }
                    if (score2 > bestScore) {
                        best = plan;
                        bestScore = score2;
                    }
                }
            }
        }
        if (best == null || (route = Crystals.route(player, item, level.method_8320(best.clicked()), this.savedSlot)) == -1) {
            return null;
        }
        return best.withRoute(route);
    }

    private Plan reachable(Fight fight, Plan plan) {
        if (hitAlong(fight.player, plan, fight.aimEye, fight.lookAt(plan.aim())) != null) {
            return plan;
        }
        if (plan.step() != Step.CRYSTAL || fight.fixedLook != null) {
            return null;
        }
        class_243 middle = class_243.method_24953(plan.clicked());
        for (class_2350 side : class_2350.class_2353.field_11062) {
            class_243 face = middle.method_1031(((double) side.method_10148()) * 0.4d, 0.0d, ((double) side.method_10165()) * 0.4d);
            if (((fight.aimEye.field_1352 - face.field_1352) * ((double) side.method_10148())) + ((fight.aimEye.field_1350 - face.field_1350) * ((double) side.method_10165())) > 0.0d) {
                Plan aimed = new Plan(plan.step(), null, plan.clicked(), plan.target(), face, plan.route());
                if (hitAlong(fight.player, aimed, fight.aimEye, fight.lookAt(face)) != null) {
                    return aimed;
                }
            }
        }
        return null;
    }

    private static class_243 aimOnTop(class_638 level, class_2338 pos) {
        double top = level.method_8320(pos).method_26218(level, pos).method_1105(class_2350.class_2351.field_11052);
        return new class_243(((double) pos.method_10263()) + 0.5d, (((double) pos.method_10264()) + top) - 0.1d, ((double) pos.method_10260()) + 0.5d);
    }

    private float need(class_1309 target, boolean breaking, boolean forced) {
        float damage = (forced || (this.facePlace.get() && facePlaced(target))) ? 2.0f : this.minDamage.get();
        return breaking ? damage * BREAK_FACTOR : damage;
    }

    private static boolean facePlaced(class_1309 target) {
        return CrystalDamage.health(target) <= FACE_PLACE_HEALTH || Combo.inHole(target) || armorLow(target);
    }

    private static boolean armorLow(class_1309 target) {
        for (class_1304 slot : class_1304.field_54086) {
            if (slot.method_46643()) {
                class_1799 stack = target.method_6118(slot);
                if (stack.method_7963() && stack.method_7936() - stack.method_7919() <= stack.method_7936() * ARMOR_LOW) {
                    return true;
                }
            }
        }
        return false;
    }

    private float bound(Fight fight, Step step, class_243 center) {
        float best = Float.NEGATIVE_INFINITY;
        for (CrystalDamage.Victim enemy : fight.side(step).enemies) {
            class_1309 entity = enemy.entity();
            float upper = enemy.upperBound(center);
            if (!Combo.kills(upper, entity)) {
                if (upper < need(entity, step == Step.BREAK, fight.forced)) {
                }
            }
            best = Math.max(best, Combo.value(upper, entity));
        }
        return best;
    }

    private float score(Fight fight, Step step, class_243 center, float toBeat, boolean opening) {
        Side side = fight.side(step);
        boolean baseToCome = step == Step.OBSIDIAN;
        float selfDamage = side.self.damage(center, baseToCome);
        if (selfDamage > this.maxSelfDamage.get() || CrystalDamage.endangers(fight.player, selfDamage)) {
            return Float.NEGATIVE_INFINITY;
        }
        for (CrystalDamage.Victim friend : side.friends) {
            float damage = friend.damage(center, baseToCome);
            if (damage > this.maxSelfDamage.get() || CrystalDamage.endangers(friend.entity(), damage)) {
                return Float.NEGATIVE_INFINITY;
            }
        }
        float best = Float.NEGATIVE_INFINITY;
        for (CrystalDamage.Victim enemy : side.enemies) {
            class_1309 entity = enemy.entity();
            float effective = effective(fight, enemy, step, center, opening);
            if (!Combo.kills(effective, entity)) {
                if (effective < need(entity, step == Step.BREAK, fight.forced)) {
                }
            }
            best = Math.max(best, Combo.value(effective, entity) - (0.5f * selfDamage));
        }
        if (best > toBeat) {
            return best;
        }
        return Float.NEGATIVE_INFINITY;
    }

    private static boolean kills(Fight fight, Step step, class_243 center, boolean opening) {
        for (CrystalDamage.Victim enemy : fight.side(step).enemies) {
            if (Combo.kills(effective(fight, enemy, step, center, opening), enemy.entity())) {
                return true;
            }
        }
        return false;
    }

    private static float effective(Fight fight, CrystalDamage.Victim enemy, Step step, class_243 center, boolean opening) {
        int iOpensIn;
        if (step == Step.BREAK) {
            iOpensIn = opening ? HurtClock.opensIn(enemy.entity(), fight.rtt) : 0;
        } else {
            iOpensIn = fight.rtt;
        }
        int ahead = iOpensIn;
        return HurtClock.effective(enemy, enemy.raw(center, step == Step.OBSIDIAN), ahead, fight.rtt);
    }

    private static class_239 hitAlong(class_746 player, Plan plan, class_243 eye, Rotation look) {
        if (look == null) {
            return null;
        }
        class_243 direction = class_243.method_1030(look.pitch(), look.yaw());
        if (plan.step() == Step.BREAK) {
            class_238 box = plan.crystal().method_5829();
            if (box.method_1006(eye)) {
                return new class_3966(plan.crystal(), eye);
            }
            class_243 at = (class_243) box.method_992(eye, eye.method_1019(direction.method_1021(player.method_55755()))).orElse(null);
            if (at == null || !MeleeAttacks.lineIsClear(player, eye, at)) {
                return null;
            }
            return new class_3966(plan.crystal(), at);
        }
        class_638 level = player.method_73183();
        class_2680 clicked = level.method_8320(plan.clicked());
        class_3965 hit = clicked.method_26218(level, plan.clicked()).method_1092(eye, eye.method_1019(direction.method_1021(player.method_55754())), plan.clicked());
        if (hit == null || !MeleeAttacks.lineIsClear(player, eye, hit.method_17784())) {
            return null;
        }
        if (plan.step() == Step.OBSIDIAN && !plan.target().equals(Crystals.placedObsidian(level, player, hit, clicked))) {
            return null;
        }
        return hit;
    }

    private boolean perform(class_310 mc, class_746 player, Fight fight, Plan plan, Look look) {
        class_243 eye = player.method_33571();
        Rotation sent = ServerRotation.sent();
        Rotation next = look == Look.SILENT ? sent : RotationUtils.entityRotation(player);
        class_239 hit = sent == null ? null : hitAlong(player, plan, eye, sent);
        if (hit != null && next != sent && hitAlong(player, plan, eye, next) == null) {
            hit = null;
        }
        if (plan.route() != -2) {
            select(player, plan.route());
        }
        boolean acted = hit != null && due(plan, 0) && (plan.step() != Step.BREAK ? place(mc, player, plan, (class_3965) hit) : strike(mc, player, fight, plan.crystal()));
        if (acted) {
            MeleeAttacks.claimTick(mc);
        } else if (look == Look.SILENT) {
            ServerRotation.request(hit != null ? sent : fight.lookAt(plan.aim()));
        }
        return acted;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private boolean fire(class_310 mc, class_746 player, Fight fight, AnchorAuraModule aura, Combo.Shot shot) throws MatchException {
        if (shot.route() != -2) {
            select(player, shot.route());
        }
        if (!aura.ready(mc, player, shot)) {
            ServerRotation.request(fight.lookAt(shot.aim()));
            return false;
        }
        aura.fire(mc, player, shot);
        return true;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private boolean strike(class_310 mc, class_746 player, Fight fight, class_1511 crystal) throws MatchException {
        if ((this.mainAge < 2 && !Crystals.enchantsAddDamage(player.method_6047()) && !Crystals.dealsDamage(player, class_1799.field_8037)) || !Crystals.strike(mc, player, crystal)) {
            return false;
        }
        markBreak(crystal);
        this.breakAttempts.addTo(crystal.method_5628(), 1);
        this.ticksSinceBreak = 0;
        for (CrystalDamage.Victim enemy : fight.side(Step.BREAK).enemies) {
            float raw = enemy.raw(crystal.method_73189(), false);
            if (raw > 0.0f) {
                HurtClock.shot(player, enemy.entity(), raw, Integer.valueOf(crystal.method_5628()), fight.rtt);
            }
        }
        class_238 blast = crystal.method_5829().method_1014(CHAIN_RANGE);
        for (class_1511 other : fight.level.method_18467(class_1511.class, blast)) {
            if (other != crystal && other.method_5858(crystal) <= 144.0d) {
                this.doomed.put(other.method_5628(), player.field_6012);
            }
        }
        return true;
    }

    private boolean place(class_310 mc, class_746 player, Plan plan, class_3965 hit) {
        if (!Crystals.rightClick(mc, player, hit)) {
            return false;
        }
        if (plan.step() == Step.CRYSTAL) {
            Crystals.notePlace(player, plan.target());
        } else {
            this.pendingObsidian = plan.target();
            this.pendingObsidianAge = 0;
        }
        this.ticksSincePlace = 0;
        return true;
    }

    private static boolean sameLook(Rotation a, Rotation b) {
        return a != null && Math.abs(RotationUtils.yawDelta(a.yaw(), b.yaw())) <= 2.0f && Math.abs(a.pitch() - b.pitch()) <= 2.0f;
    }

    private boolean due(Plan plan, int ahead) {
        if (plan.step() == Step.BREAK) {
            return this.ticksSinceBreak + ahead > this.breakDelay.getInt();
        }
        return this.ticksSincePlace + ahead > this.placeDelay.getInt();
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
        this.ticksSinceBreak = Math.min(this.ticksSinceBreak + 1, 1000);
        this.ticksSincePlace = Math.min(this.ticksSincePlace + 1, 1000);
        this.mainAge = Math.min(this.mainAge + 1, 1000);
        this.pendingBreakAge = Math.min(this.pendingBreakAge + 1, 1000);
        this.pendingObsidianAge = Math.min(this.pendingObsidianAge + 1, 1000);
    }
}
