package net.anubis.client.module.impl.combat;

import imgui.ImDrawList;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.PacketReceiveEvent;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.CombatMacroLock;
import net.anubis.client.module.HudOverlayProvider;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.impl.combat.anchor.AnchorGuard;
import net.anubis.client.module.impl.combat.crystal.Combo;
import net.anubis.client.module.impl.combat.crystal.CrystalDamage;
import net.anubis.client.module.impl.combat.crystal.Crystals;
import net.anubis.client.module.impl.combat.crystal.HurtClock;
import net.anubis.client.module.impl.combat.crystal.ShotView;
import net.anubis.client.module.impl.combat.melee.MeleeAttacks;
import net.anubis.client.module.impl.combat.melee.SlotReturn;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.util.pvp.Rotation;
import net.anubis.client.util.pvp.RotationUtils;
import net.anubis.client.util.pvp.ServerRotation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1309;
import net.minecraft.class_1661;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_4969;
import net.minecraft.class_638;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/AnchorAuraModule.class */
@Environment(EnvType.CLIENT)
public final class AnchorAuraModule extends ClientModule implements HudOverlayProvider {
    private static final double REACH_MARGIN = 0.01d;
    private static final int ARM_TICKS = 2;
    private static final int HOLD_TICKS = 10;
    private static final int MAX_LEAD_TICKS = 5;
    private final NumberSetting minDamage;
    private final NumberSetting maxSelfDamage;
    private final BooleanSetting render;
    private final ColorSetting renderColor;
    private final SlotReturn slots;
    private final ShotView view;
    private int tick;
    private int holdUntil;
    private class_2338 spent;
    private int spentUntil;
    private float aimTurn;
    private float placedTurn;
    private List<CrystalDamage.Victim> lastTargets;
    private List<CrystalDamage.Victim> lastSpared;
    private int lastRtt;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/AnchorAuraModule$Blast.class */
    @Environment(EnvType.CLIENT)
    private static final class Blast extends Record {
        private final float value;
        private final boolean kills;
        private final class_1309 source;

        private Blast(float value, boolean kills, class_1309 source) {
            this.value = value;
            this.kills = kills;
            this.source = source;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Blast.class), Blast.class, "value;kills;source", "FIELD:Lnet/anubis/client/module/impl/combat/AnchorAuraModule$Blast;->value:F", "FIELD:Lnet/anubis/client/module/impl/combat/AnchorAuraModule$Blast;->kills:Z", "FIELD:Lnet/anubis/client/module/impl/combat/AnchorAuraModule$Blast;->source:Lnet/minecraft/class_1309;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Blast.class), Blast.class, "value;kills;source", "FIELD:Lnet/anubis/client/module/impl/combat/AnchorAuraModule$Blast;->value:F", "FIELD:Lnet/anubis/client/module/impl/combat/AnchorAuraModule$Blast;->kills:Z", "FIELD:Lnet/anubis/client/module/impl/combat/AnchorAuraModule$Blast;->source:Lnet/minecraft/class_1309;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Blast.class, Object.class), Blast.class, "value;kills;source", "FIELD:Lnet/anubis/client/module/impl/combat/AnchorAuraModule$Blast;->value:F", "FIELD:Lnet/anubis/client/module/impl/combat/AnchorAuraModule$Blast;->kills:Z", "FIELD:Lnet/anubis/client/module/impl/combat/AnchorAuraModule$Blast;->source:Lnet/minecraft/class_1309;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public float value() {
            return this.value;
        }

        public boolean kills() {
            return this.kills;
        }

        public class_1309 source() {
            return this.source;
        }
    }

    public AnchorAuraModule() {
        super("anchor-aura", "Anchor Aura", "Places, charges and detonates respawn anchors on nearby enemies.", ModuleCategory.COMBAT);
        this.minDamage = (NumberSetting) addSetting("min-damage", new NumberSetting("Min Damage", 6.0f, 0.0f, 20.0f).step(0.5f));
        this.maxSelfDamage = (NumberSetting) addSetting("max-self-damage", new NumberSetting("Max Self Damage", 6.0f, 0.0f, 20.0f).step(0.5f));
        this.render = (BooleanSetting) addSetting("render", new BooleanSetting("Render", true));
        this.renderColor = (ColorSetting) addSetting("render-color", new ColorSetting("Render Color", -26052));
        this.slots = new SlotReturn(this);
        this.view = new ShotView();
        ColorSetting colorSetting = this.renderColor;
        BooleanSetting booleanSetting = this.render;
        Objects.requireNonNull(booleanSetting);
        colorSetting.visibleWhen(booleanSetting::get);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        this.slots.drop(class_310.method_1551(), true);
        this.spent = null;
        this.view.clear();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    @EventHandler
    public void onTick(TickEvent event) throws MatchException {
        this.tick++;
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        CrystalAuraModule crystal = (CrystalAuraModule) ModuleRegistry.get(CrystalAuraModule.class);
        if ((crystal != null && crystal.isEnabled()) || !MeleeAttacks.canAct(mc) || player.method_7325() || AutoInvTotemModule.wantsHotbar() || AutoCityModule.mining() || CombatMacroLock.heldByOther(this) || mc.field_1687.method_27983() == class_1937.field_25180) {
            this.slots.tick(mc, true);
            release();
            return;
        }
        HurtClock.tick(player);
        if (this.tick > this.holdUntil) {
            this.slots.tick(mc, true);
        }
        if (MeleeAttacks.actedThisTick(player) || !MeleeAttacks.handsFree(mc, player) || player.method_21823() || MeleeAttacks.rightClickComing(mc)) {
            return;
        }
        Combo.Shot shot = Combo.pick(shots(mc, player, Combo.enemies(mc, player), Combo.friends(mc, player), Crystals.rttTicks(mc, player), 0.0f));
        if (shot == null) {
            if (this.spent != null && this.tick < this.spentUntil && mc.field_1687.method_8320(this.spent).method_27852(class_2246.field_23152)) {
                ServerRotation.request(ServerRotation.sent());
            }
            if (this.tick > this.holdUntil) {
                release();
                return;
            }
            return;
        }
        show(shot);
        if (CombatMacroLock.tryAcquire(this)) {
            if (!ready(mc, player, shot)) {
                ServerRotation.request(RotationUtils.fromTo(player.method_33571(), shot.aim()));
            } else if (shot.route() == -2 || this.slots.take(player, shot.route())) {
                fire(mc, player, shot);
                this.holdUntil = this.tick + 10;
                ServerRotation.request(ServerRotation.sent());
            }
        }
    }

    @EventHandler
    public void onPacketReceive(PacketReceiveEvent event) {
        class_746 player = class_310.method_1551().field_1724;
        if (player != null) {
            HurtClock.note(player, event.packet);
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

    public void show(Combo.Shot shot) {
        class_2338 class_2338VarPos;
        if (this.render.get()) {
            Object objPlan = shot.plan();
            if (objPlan instanceof AnchorGuard.Spot) {
                AnchorGuard.Spot spot = (AnchorGuard.Spot) objPlan;
                class_2338VarPos = spot.pos();
            } else {
                class_2338VarPos = (class_2338) shot.plan();
            }
            class_2338 cell = class_2338VarPos;
            class_243 center = class_243.method_24953(cell);
            float most = 0.0f;
            for (CrystalDamage.Victim victim : this.lastTargets) {
                most = Math.max(most, victim.damage(center, 5.0f, cell));
            }
            float own = this.lastSpared.get(0).damage(center, 5.0f, cell);
            this.view.show(new class_238(cell), this.renderColor.argb(), most, own);
        }
    }

    public List<Combo.Shot> shots(class_310 mc, class_746 player, List<class_1309> enemies, List<class_1309> friends, int rtt, float rival) {
        class_638 level = mc.field_1687;
        class_1661 inventory = player.method_31548();
        class_1799 main = player.method_6047();
        int detonator = AnchorGuard.detonatorRoute(inventory, main);
        if (level.method_27983() == class_1937.field_25180 || !ServerRotation.canRotate(player) || player.method_6079().method_31574(class_1802.field_8801) || player.method_21823() || enemies.isEmpty() || detonator == -1) {
            return List.of();
        }
        List<class_1309> spared = new ArrayList<>();
        spared.add(player);
        spared.addAll(friends);
        this.lastRtt = rtt;
        this.lastTargets = victims(enemies, rtt);
        this.lastSpared = victims(spared, rtt);
        List<Combo.Shot> shots = new ArrayList<>();
        int glowstone = routeOf(inventory, main, class_1802.field_8801);
        boolean existing = false;
        for (class_2338 cell : anchors(level, player, enemies)) {
            class_243 aim = AnchorGuard.anchorAim(level, player, cell);
            if (aim != null && safe(this.lastSpared, cell)) {
                boolean charged = ((Integer) level.method_8320(cell).method_11654(class_4969.field_23153)).intValue() > 0;
                int ahead = charged ? 0 : 1;
                Blast due = blast(cell, ahead, false);
                if (due != null || blast(cell, ahead, true) != null) {
                    existing = true;
                    if (due != null) {
                        if (charged) {
                            shots.add(new Combo.Shot(Combo.Kind.DETONATE, aim, detonator, due.value(), due.kills(), 0, cell));
                        } else if (glowstone != -1 && HurtClock.opensIn(due.source(), rtt) <= 1 && (rival <= 0.0f || due.value() > rival + 1.0f)) {
                            shots.add(new Combo.Shot(Combo.Kind.CHARGE, aim, glowstone, due.value(), due.kills(), 1, cell));
                        }
                    }
                }
            }
        }
        int anchor = routeOf(inventory, main, class_1802.field_23141);
        if (!existing && anchor != -1 && glowstone != -1) {
            List<class_238> reserved = Crystals.reservedBoxes(player, Crystals.confirmWindow(mc, player));
            Combo.Shot placement = place(level, player, anchor, reserved);
            if (placement != null) {
                shots.add(placement);
            }
        }
        return shots;
    }

    public boolean ready(class_310 mc, class_746 player, Combo.Shot shot) {
        class_638 level = mc.field_1687;
        if (shot.kind() == Combo.Kind.ANCHOR) {
            AnchorGuard.Spot spot = (AnchorGuard.Spot) shot.plan();
            Rotation sent = ServerRotation.sent();
            if (sent != null && AnchorGuard.hitAlong(level, player, sent, spot) != null) {
                return true;
            }
            float turn = turnTo(player, spot);
            if (turn > 0.0f) {
                this.aimTurn = turn;
                return false;
            }
            return false;
        }
        class_2338 cell = (class_2338) shot.plan();
        class_2680 state = level.method_8320(cell);
        if (!state.method_27852(class_2246.field_23152) || AnchorGuard.anchorHit(level, player, cell) == null) {
            return false;
        }
        boolean charged = ((Integer) state.method_11654(class_4969.field_23153)).intValue() > 0;
        if (shot.kind() == Combo.Kind.CHARGE) {
            return !charged;
        }
        return charged && safe(this.lastSpared, cell) && blast(cell, 0, false) != null;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    public void fire(class_310 mc, class_746 player, Combo.Shot shot) throws MatchException {
        class_638 level = mc.field_1687;
        switch (shot.kind()) {
            case ANCHOR:
                AnchorGuard.Spot spot = (AnchorGuard.Spot) shot.plan();
                AnchorGuard.click(mc, player, AnchorGuard.hitAlong(level, player, ServerRotation.sent(), spot));
                if (this.aimTurn > 0.0f) {
                    this.placedTurn = this.aimTurn;
                }
                break;
            case CHARGE:
                AnchorGuard.click(mc, player, AnchorGuard.anchorHit(level, player, (class_2338) shot.plan()));
                break;
            case DETONATE:
                class_2338 cell = (class_2338) shot.plan();
                AnchorGuard.click(mc, player, AnchorGuard.anchorHit(level, player, cell));
                this.spent = cell;
                this.spentUntil = this.tick + Crystals.confirmWindow(mc, player);
                class_243 center = class_243.method_24953(cell);
                for (CrystalDamage.Victim victim : this.lastTargets) {
                    float raw = victim.raw(center, 5.0f, cell);
                    if (raw > 0.0f) {
                        HurtClock.shot(player, victim.entity(), raw, center, this.lastRtt);
                    }
                }
                break;
        }
    }

    private Combo.Shot place(class_638 level, class_746 player, int route, List<class_238> reserved) {
        AnchorGuard.Spot candidate;
        Map<class_2338, Float> bounds = new LinkedHashMap<>();
        for (CrystalDamage.Victim victim : this.lastTargets) {
            class_2338 feet = victim.entity().method_24515();
            for (class_2338 pos : class_2338.method_10097(feet.method_10069(-2, -1, -2), feet.method_10069(2, 2, 2))) {
                if (!bounds.containsKey(pos) && level.method_8320(pos).method_26215() && player.method_56093(pos, -0.01d) && !reservedAt(reserved, pos)) {
                    float bound = upperValue(class_243.method_24953(pos));
                    if (bound >= 0.0f) {
                        bounds.put(pos.method_10062(), Float.valueOf(bound));
                    }
                }
            }
        }
        List<class_2338> cells = new ArrayList<>(bounds.keySet());
        cells.sort((a, b) -> {
            return Float.compare(((Float) bounds.get(b)).floatValue(), ((Float) bounds.get(a)).floatValue());
        });
        class_243 eye = player.method_33571();
        Combo.Shot best = null;
        for (class_2338 pos2 : cells) {
            if (best != null && bounds.get(pos2).floatValue() <= best.value()) {
                break;
            }
            Blast blast = blast(pos2, 2, true);
            if (blast != null && (best == null || blast.value() > best.value())) {
                if (safe(this.lastSpared, pos2) && (candidate = AnchorGuard.supportFor(level, player, eye, pos2)) != null) {
                    Rotation look = ServerRotation.next(player, RotationUtils.fromTo(eye, candidate.aim()));
                    class_243 reach = class_243.method_1030(look.pitch(), look.yaw()).method_1021(player.method_55754());
                    if (!new class_238(pos2).method_992(eye, eye.method_1019(reach)).isEmpty()) {
                        float turn = turnTo(player, candidate);
                        if (turn <= 2.0f || Math.abs(turn - this.placedTurn) >= 1.0E-4f) {
                            best = new Combo.Shot(Combo.Kind.ANCHOR, candidate.aim(), route, blast.value(), blast.kills(), 2, candidate);
                        }
                    }
                }
            }
        }
        return best;
    }

    private Set<class_2338> anchors(class_638 level, class_746 player, List<class_1309> enemies) {
        Set<class_2338> anchors = new LinkedHashSet<>();
        for (class_1309 enemy : enemies) {
            class_2338 feet = enemy.method_24515();
            for (class_2338 pos : class_2338.method_10097(feet.method_10069(-4, -3, -4), feet.method_10069(4, 4, 4))) {
                if (level.method_8320(pos).method_27852(class_2246.field_23152) && player.method_56093(pos, -0.01d) && (!pos.equals(this.spent) || this.tick >= this.spentUntil)) {
                    anchors.add(pos.method_10062());
                }
            }
        }
        return anchors;
    }

    private Blast blast(class_2338 cell, int ahead, boolean waiting) {
        class_243 center = class_243.method_24953(cell);
        class_1309 source = null;
        float best = 0.0f;
        boolean kills = false;
        for (CrystalDamage.Victim victim : this.lastTargets) {
            class_1309 enemy = victim.entity();
            int when = waiting ? Math.max(ahead, HurtClock.opensIn(enemy, this.lastRtt)) : ahead;
            float effective = HurtClock.effective(victim, victim.raw(center, 5.0f, cell), when, this.lastRtt);
            if (effective >= needed(enemy)) {
                kills = kills || Combo.kills(effective, enemy);
                float value = Combo.value(effective, enemy);
                if (source == null || value > best) {
                    source = enemy;
                    best = value;
                }
            }
        }
        if (source == null) {
            return null;
        }
        float own = this.lastSpared.get(0).damage(center, 5.0f, cell);
        return new Blast(best - (0.5f * own), kills, source);
    }

    private float upperValue(class_243 center) {
        float bound = -1.0f;
        for (CrystalDamage.Victim victim : this.lastTargets) {
            float upper = victim.upperBound(center, 5.0f);
            if (upper >= needed(victim.entity())) {
                bound = Math.max(bound, Combo.value(upper, victim.entity()));
            }
        }
        return bound;
    }

    private boolean safe(List<CrystalDamage.Victim> spared, class_2338 cell) {
        class_243 center = class_243.method_24953(cell);
        for (CrystalDamage.Victim other : spared) {
            float damage = other.damage(center, 5.0f, cell);
            if (damage > this.maxSelfDamage.get() || CrystalDamage.endangers(other.entity(), damage)) {
                return false;
            }
        }
        return true;
    }

    private float needed(class_1309 enemy) {
        return Math.min(this.minDamage.get(), CrystalDamage.health(enemy));
    }

    private static List<CrystalDamage.Victim> victims(List<class_1309> entities, int rtt) {
        List<CrystalDamage.Victim> victims = new ArrayList<>(entities.size());
        for (class_1309 entity : entities) {
            int lead = entity instanceof class_746 ? 0 : Math.min(rtt, 5);
            victims.add(new CrystalDamage.Victim(entity, Combo.lead(entity, lead)));
        }
        return victims;
    }

    private static boolean reservedAt(List<class_238> reserved, class_2338 pos) {
        for (class_238 box : reserved) {
            if (box.method_71152(pos)) {
                return true;
            }
        }
        return false;
    }

    private static int routeOf(class_1661 inventory, class_1799 main, class_1792 item) {
        if (main.method_31574(item)) {
            return -2;
        }
        int slot = Crystals.hotbarSlotOf(inventory, item);
        if (slot == -1) {
            return -1;
        }
        return slot;
    }

    private static float turnTo(class_746 player, AnchorGuard.Spot spot) {
        Rotation sent = ServerRotation.sent();
        Rotation from = sent != null ? sent : RotationUtils.entityRotation(player);
        Rotation aimed = ServerRotation.next(player, RotationUtils.fromTo(player.method_33571(), spot.aim()));
        return Math.abs(aimed.yaw() - from.yaw());
    }

    private void release() {
        if (!this.slots.holding()) {
            CombatMacroLock.release(this);
        }
    }
}
