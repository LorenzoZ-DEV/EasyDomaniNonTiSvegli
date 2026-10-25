package net.anubis.client.module.impl.combat;

import java.util.concurrent.ThreadLocalRandom;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.impl.combat.melee.MeleeAttacks;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.util.combat.CombatTarget;
import net.anubis.client.util.pvp.Rotation;
import net.anubis.client.util.pvp.RotationUtils;
import net.anubis.client.util.pvp.TargetingUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1309;
import net.minecraft.class_156;
import net.minecraft.class_1657;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/AimAssistModule.class */
@Environment(EnvType.CLIENT)
public final class AimAssistModule extends ClientModule {
    private static final long LAST_HIT_MS = 1500;
    private static final double STICK_RANGE = 0.5d;
    private static final double STICK_FOV = 1.3d;
    private static final long MOUSE_IDLE_MS = 150;
    private static final double[] STILL = {0.0d, 0.0d};
    private final NumberSetting range;
    private final NumberSetting fov;
    private final NumberSetting speed;
    private final BooleanSetting weaponsOnly;
    private final BooleanSetting vertical;
    private class_1657 target;
    private double pace;
    private double pendingYaw;
    private double pendingPitch;
    private long movedAt;
    private double userYawSign;
    private double userPitchSign;

    public AimAssistModule() {
        super("aim-assist", "Aim Assist", "Eases your crosshair onto the nearest enemy player while you move the mouse.", ModuleCategory.COMBAT);
        this.range = (NumberSetting) addSetting("range", new NumberSetting("Range", 4.0f, 3.0f, 6.0f).step(0.1f));
        this.fov = (NumberSetting) addSetting("fov", new NumberSetting("FOV", 60.0f, 10.0f, 180.0f).step(5.0f));
        this.speed = (NumberSetting) addSetting("speed", new NumberSetting("Speed", 60.0f, 10.0f, 180.0f).step(5.0f));
        this.weaponsOnly = (BooleanSetting) addSetting("weapons-only", new BooleanSetting("Weapons Only", true));
        this.vertical = (BooleanSetting) addSetting("vertical", new BooleanSetting("Vertical", true));
    }

    @Override // net.anubis.client.module.ClientModule
    public int settingsVersion() {
        return 1;
    }

    @Override // net.anubis.client.module.ClientModule
    public void migrateSettings(int savedVersion) {
        if (savedVersion < 1) {
            this.speed.set(60.0f);
        }
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        follow(null);
    }

    @EventHandler
    public void onTick(TickEvent event) {
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (!MeleeAttacks.canAct(mc) || player.method_6115() || mc.field_1690.field_1904.method_1434() || (this.weaponsOnly.get() && !MeleeAttacks.isMeleeWeapon(player.method_6047()))) {
            follow(null);
        } else {
            follow(choose(mc, player));
        }
    }

    public double[] nudge(class_746 player, double userYaw, double userPitch, double seconds) {
        long now = class_156.method_658();
        if (userYaw != 0.0d || userPitch != 0.0d) {
            this.movedAt = now;
        }
        if (userYaw != 0.0d) {
            this.userYawSign = Math.signum(userYaw);
        }
        if (userPitch != 0.0d) {
            this.userPitchSign = Math.signum(userPitch);
        }
        Rotation want = (this.target == null || now - this.movedAt > MOUSE_IDLE_MS) ? null : aim(player, player.method_33571(), core(this.target));
        if (want == null) {
            this.pendingYaw = 0.0d;
            this.pendingPitch = 0.0d;
            this.userYawSign = 0.0d;
            this.userPitchSign = 0.0d;
            return STILL;
        }
        double dt = Math.min(seconds, 0.05d);
        float yawError = class_3532.method_15393(want.yaw() - player.method_36454());
        float pitchError = this.vertical.get() ? want.pitch() - player.method_36455() : 0.0f;
        if (this.userYawSign * ((double) yawError) < 0.0d) {
            yawError = 0.0f;
        }
        if (this.userPitchSign * ((double) pitchError) < 0.0d) {
            pitchError = 0.0f;
        }
        double cap = ((double) this.speed.get()) * this.pace * dt;
        double ease = 1.0d - Math.exp((-12.0d) * dt);
        double k = RotationUtils.mouseScale(class_310.method_1551(), player);
        double degreesPerCount = ((float) k) * 0.15f;
        this.pendingYaw += class_3532.method_15350(((double) yawError) * ease, -cap, cap) / degreesPerCount;
        this.pendingPitch += class_3532.method_15350((((double) pitchError) * ease) * 0.6d, -cap, cap) / degreesPerCount;
        long yawCounts = (long) this.pendingYaw;
        long pitchCounts = (long) this.pendingPitch;
        this.pendingYaw -= yawCounts;
        this.pendingPitch -= pitchCounts;
        return new double[]{yawCounts * k, pitchCounts * k};
    }

    private void follow(class_1657 next) {
        if (next == this.target) {
            return;
        }
        this.target = next;
        this.pace = ThreadLocalRandom.current().nextDouble(0.85d, 1.15d);
        this.pendingYaw = 0.0d;
        this.pendingPitch = 0.0d;
    }

    private class_1657 choose(class_310 mc, class_746 player) {
        class_243 eye = player.method_33571();
        double halfFov = ((double) this.fov.get()) / 2.0d;
        class_1309 class_1309VarLastHit = CombatTarget.lastHit(LAST_HIT_MS);
        if (class_1309VarLastHit instanceof class_1657) {
            class_1657 hit = (class_1657) class_1309VarLastHit;
            if (offAngle(player, eye, hit, this.range.get()) <= halfFov) {
                return hit;
            }
        }
        if (this.target != null && this.target.method_73183() == player.method_73183() && offAngle(player, eye, this.target, ((double) this.range.get()) + STICK_RANGE) <= halfFov * STICK_FOV) {
            return this.target;
        }
        class_1657 best = null;
        double bestAngle = halfFov;
        for (class_1657 candidate : mc.field_1687.method_18456()) {
            double angle = offAngle(player, eye, candidate, this.range.get());
            if (angle <= bestAngle) {
                best = candidate;
                bestAngle = angle;
            }
        }
        return best;
    }

    private double offAngle(class_746 player, class_243 eye, class_1657 candidate, double reach) {
        if (!MeleeAttacks.isEnemy(player, candidate) || candidate.method_5767() || eye.method_1022(TargetingUtils.closestPoint(eye, candidate.method_5829())) > reach) {
            return Double.MAX_VALUE;
        }
        class_238 core = core(candidate);
        if (!MeleeAttacks.lineIsClear(player, eye, core.method_1005())) {
            return Double.MAX_VALUE;
        }
        Rotation want = aim(player, eye, core);
        if (want == null) {
            return 0.0d;
        }
        return RotationUtils.angleTo(RotationUtils.entityRotation(player), want);
    }

    private Rotation aim(class_746 player, class_243 eye, class_238 core) {
        class_243 view = player.method_5828(1.0f);
        if (core.method_992(eye, eye.method_1019(view.method_1021(((double) this.range.get()) + 1.0d))).isPresent()) {
            return null;
        }
        class_243 near = TargetingUtils.closestPoint(eye.method_1019(view.method_1021(eye.method_1022(core.method_1005()))), core);
        return RotationUtils.fromTo(eye, near);
    }

    private static class_238 core(class_1657 player) {
        class_238 box = player.method_5829();
        return box.method_35580(box.method_17939() * 0.25d, box.method_17940() * 0.1d, box.method_17939() * 0.25d);
    }
}
