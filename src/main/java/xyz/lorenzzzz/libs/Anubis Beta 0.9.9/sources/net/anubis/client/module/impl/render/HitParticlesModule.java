package net.anubis.client.module.impl.render;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import net.anubis.client.event.EventPriority;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.PlayerAttackEntityEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_238;
import net.minecraft.class_2390;
import net.minecraft.class_2394;
import net.minecraft.class_2398;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3966;
import net.minecraft.class_5819;
import net.minecraft.class_638;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/HitParticlesModule.class */
@Environment(EnvType.CLIENT)
public final class HitParticlesModule extends ClientModule {
    private static final String TYPE_CRIT = "Crit";
    private static final String TYPE_MAGIC_CRIT = "Magic Crit";
    private static final String TYPE_SPARK = "Spark";
    private static final String TYPE_FLAME = "Flame";
    private static final String TYPE_SOUL_FLAME = "Soul Flame";
    private static final String TYPE_END_ROD = "End Rod";
    private static final String TYPE_TOTEM = "Totem";
    private static final String TYPE_HEART = "Heart";
    private static final String TYPE_DUST = "Dust";
    private static final String ORIGIN_IMPACT = "Impact";
    private static final String ORIGIN_BODY = "Body";
    private static final String TARGETS_LIVING = "Living";
    private static final String TARGETS_PLAYERS = "Players";
    private static final String TARGETS_ALL = "All";
    private static final String COLOR_CUSTOM = "Custom";
    private static final String COLOR_ACCENT = "Accent";
    private static final double LIFT = 0.25d;
    private final ModeSetting type;
    private final NumberSetting amount;
    private final NumberSetting spread;
    private final ModeSetting origin;
    private final BooleanSetting onlyCrits;
    private final ModeSetting targets;
    private final ModeSetting dustColorMode;
    private final ColorSetting dustColor;
    private final NumberSetting dustSize;
    private final class_5819 random;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/HitParticlesModule$Burst.class */
    @Environment(EnvType.CLIENT)
    private static final class Burst extends Record {
        private final class_2394 particle;
        private final double speed;
        private final double jitter;

        private Burst(class_2394 particle, double speed, double jitter) {
            this.particle = particle;
            this.speed = speed;
            this.jitter = jitter;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Burst.class), Burst.class, "particle;speed;jitter", "FIELD:Lnet/anubis/client/module/impl/render/HitParticlesModule$Burst;->particle:Lnet/minecraft/class_2394;", "FIELD:Lnet/anubis/client/module/impl/render/HitParticlesModule$Burst;->speed:D", "FIELD:Lnet/anubis/client/module/impl/render/HitParticlesModule$Burst;->jitter:D").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Burst.class), Burst.class, "particle;speed;jitter", "FIELD:Lnet/anubis/client/module/impl/render/HitParticlesModule$Burst;->particle:Lnet/minecraft/class_2394;", "FIELD:Lnet/anubis/client/module/impl/render/HitParticlesModule$Burst;->speed:D", "FIELD:Lnet/anubis/client/module/impl/render/HitParticlesModule$Burst;->jitter:D").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Burst.class, Object.class), Burst.class, "particle;speed;jitter", "FIELD:Lnet/anubis/client/module/impl/render/HitParticlesModule$Burst;->particle:Lnet/minecraft/class_2394;", "FIELD:Lnet/anubis/client/module/impl/render/HitParticlesModule$Burst;->speed:D", "FIELD:Lnet/anubis/client/module/impl/render/HitParticlesModule$Burst;->jitter:D").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_2394 particle() {
            return this.particle;
        }

        public double speed() {
            return this.speed;
        }

        public double jitter() {
            return this.jitter;
        }
    }

    public HitParticlesModule() {
        super("hit-particles", "Hit Particles", "Spawns particles where your hits land.", ModuleCategory.VISUALS);
        this.type = (ModeSetting) addSetting("type", new ModeSetting("Type", TYPE_CRIT, TYPE_CRIT, TYPE_MAGIC_CRIT, TYPE_SPARK, TYPE_FLAME, TYPE_SOUL_FLAME, TYPE_END_ROD, TYPE_TOTEM, TYPE_HEART, TYPE_DUST));
        this.amount = (NumberSetting) addSetting("amount", new NumberSetting("Amount", 10.0f, 1.0f, 40.0f).step(1.0f));
        this.spread = (NumberSetting) addSetting("spread", new NumberSetting("Spread", 1.0f, 0.25f, 3.0f).step(0.25f));
        this.origin = (ModeSetting) addSetting("origin", new ModeSetting("Origin", ORIGIN_IMPACT, ORIGIN_IMPACT, ORIGIN_BODY));
        this.onlyCrits = (BooleanSetting) addSetting("only-crits", new BooleanSetting("Only Crits", false));
        this.targets = (ModeSetting) addSetting("targets", new ModeSetting("Targets", TARGETS_LIVING, TARGETS_LIVING, TARGETS_PLAYERS, TARGETS_ALL));
        this.dustColorMode = (ModeSetting) addSetting("dust-color-mode", new ModeSetting("Color", COLOR_ACCENT, COLOR_CUSTOM, COLOR_ACCENT));
        this.dustColor = (ColorSetting) addSetting("dust-color", new ColorSetting("Custom Color", -657929));
        this.dustSize = (NumberSetting) addSetting("dust-size", new NumberSetting("Dust Size", 1.0f, 0.5f, 2.0f).step(0.1f));
        this.random = class_5819.method_43047();
        this.dustColorMode.visibleWhen(() -> {
            return this.type.is(TYPE_DUST);
        });
        this.dustColor.visibleWhen(() -> {
            return this.type.is(TYPE_DUST) && this.dustColorMode.is(COLOR_CUSTOM);
        });
        this.dustSize.visibleWhen(() -> {
            return this.type.is(TYPE_DUST);
        });
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        on(PlayerAttackEntityEvent.TYPE, Stage.PRE, EventPriority.LOWEST, this::onAttack);
    }

    private void onAttack(PlayerAttackEntityEvent event) {
        class_310 mc = class_310.method_1551();
        if (mc.method_18854()) {
            class_1297 class_1297Var = mc.field_1724;
            class_638 level = mc.field_1687;
            class_1297 target = event.target;
            if (class_1297Var == null || level == null || target == null || target == class_1297Var || !target.method_5732() || !accepts(target)) {
                return;
            }
            if (!this.onlyCrits.get() || isCritical(class_1297Var, target)) {
                spawnBurst(mc, level, class_1297Var, target);
            }
        }
    }

    private boolean accepts(class_1297 target) {
        switch (this.targets.get()) {
            case "Players":
                return target instanceof class_1657;
            case "All":
                return true;
            default:
                return target instanceof class_1309;
        }
    }

    private static boolean isCritical(class_746 player, class_1297 target) {
        return (player.method_7261(0.5f) <= 0.9f || player.field_6017 <= 0.0d || player.method_24828() || player.method_6101() || player.method_5799() || player.method_74025() || player.method_5765() || player.method_5624() || !(target instanceof class_1309)) ? false : true;
    }

    private void spawnBurst(class_310 mc, class_638 level, class_746 player, class_1297 target) {
        Burst burst = burst();
        class_238 box = target.method_5829();
        class_243 center = box.method_1005();
        boolean body = this.origin.is(ORIGIN_BODY);
        class_243 impact = body ? null : impactPoint(mc, player, target, box);
        class_243 fallbackOut = player.method_5720().method_22882();
        double spreadScale = this.spread.get();
        double jitter = burst.jitter() * spreadScale;
        int count = this.amount.getInt();
        for (int i = 0; i < count; i++) {
            class_243 from = body ? randomPointIn(box) : impact;
            class_243 out = from.method_1020(center);
            class_243 dir = (out.method_1027() < 1.0E-6d ? fallbackOut : out.method_1029()).method_1021(0.8d).method_1019(randomUnit()).method_1031(0.0d, LIFT, 0.0d).method_1029();
            double speed = burst.speed() * spreadScale * (0.6d + (0.4d * this.random.method_43058()));
            level.method_8466(burst.particle(), true, false, from.field_1352 + (this.random.method_43059() * jitter), from.field_1351 + (this.random.method_43059() * jitter), from.field_1350 + (this.random.method_43059() * jitter), dir.field_1352 * speed, dir.field_1351 * speed, dir.field_1350 * speed);
        }
    }

    private Burst burst() {
        switch (this.type.get()) {
            case "Magic Crit":
                return new Burst(class_2398.field_11208, 0.8d, 0.1d);
            case "Spark":
                return new Burst(class_2398.field_29644, 0.16d, 0.1d);
            case "Flame":
                return new Burst(class_2398.field_11240, 0.035d, 0.12d);
            case "Soul Flame":
                return new Burst(class_2398.field_22246, 0.035d, 0.12d);
            case "End Rod":
                return new Burst(class_2398.field_11207, 0.09d, 0.1d);
            case "Totem":
                return new Burst(class_2398.field_11220, 0.45d, 0.1d);
            case "Heart":
                return new Burst(class_2398.field_11201, 0.0d, 0.35d);
            case "Dust":
                return new Burst(new class_2390(dustRgb(), this.dustSize.get()), 1.0d, 0.3d);
            default:
                return new Burst(class_2398.field_11205, 0.8d, 0.1d);
        }
    }

    private int dustRgb() {
        int argb = this.dustColorMode.is(COLOR_ACCENT) ? CosmeticAccent.argb() : this.dustColor.argb();
        return argb & 16777215;
    }

    private static class_243 impactPoint(class_310 mc, class_746 player, class_1297 target, class_238 box) {
        class_3966 class_3966Var = mc.field_1765;
        if (class_3966Var instanceof class_3966) {
            class_3966 hit = class_3966Var;
            if (hit.method_17782() == target) {
                return hit.method_17784();
            }
        }
        class_243 eye = player.method_33571();
        return new class_243(class_3532.method_15350(eye.field_1352, box.field_1323, box.field_1320), class_3532.method_15350(eye.field_1351, box.field_1322, box.field_1325), class_3532.method_15350(eye.field_1350, box.field_1321, box.field_1324));
    }

    private class_243 randomPointIn(class_238 box) {
        return new class_243(box.field_1323 + (box.method_17939() * (0.1d + (0.8d * this.random.method_43058()))), box.field_1322 + (box.method_17940() * (0.1d + (0.8d * this.random.method_43058()))), box.field_1321 + (box.method_17941() * (0.1d + (0.8d * this.random.method_43058()))));
    }

    private class_243 randomUnit() {
        class_243 v = new class_243(this.random.method_43059(), this.random.method_43059(), this.random.method_43059());
        return v.method_1027() < 1.0E-9d ? new class_243(0.0d, 1.0d, 0.0d) : v.method_1029();
    }
}
