package net.anubis.client.module.impl.combat.crystal;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.HashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10707;
import net.minecraft.class_1267;
import net.minecraft.class_1280;
import net.minecraft.class_1282;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2378;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_3726;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_5134;
import net.minecraft.class_5321;
import net.minecraft.class_7924;
import net.minecraft.class_9334;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/crystal/CrystalDamage.class */
@Environment(EnvType.CLIENT)
public final class CrystalDamage {
    private static final double REACH = 12.0d;
    private static final float SELF_SCALE = 1.15f;
    private static final float SELF_MARGIN = 2.0f;
    private static final class_1304[] ARMOR = {class_1304.field_6169, class_1304.field_6174, class_1304.field_6172, class_1304.field_6166};

    private CrystalDamage() {
    }

    public static float estimate(class_243 center, class_1309 entity) {
        return new Victim(entity, class_243.field_1353).damage(center);
    }

    public static float estimate(class_243 center, class_1309 entity, float power, class_2338 ignored) {
        return new Victim(entity, class_243.field_1353).damage(center, power, ignored);
    }

    public static float health(class_1309 entity) {
        return entity.method_6032() + entity.method_6067();
    }

    public static boolean endangers(class_1657 self, float damage) {
        return !self.method_68878() && (damage * SELF_SCALE) + SELF_MARGIN >= health(self);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/crystal/CrystalDamage$Victim.class */
    @Environment(EnvType.CLIENT)
    public static final class Victim {
        private final class_1309 entity;
        private final class_243 position;
        private final class_238 box;
        private final class_1282 source;
        private final float armor;
        private final float toughness;
        private final float protection;
        private final float resistance;
        private final class_10707 shield;
        private final Map<class_243, Float> exposures = new HashMap();
        private final Map<Blast, Float> blastExposures = new HashMap();

        public Victim(class_1309 entity, class_243 offset) {
            this.entity = entity;
            this.position = entity.method_73189().method_1019(offset);
            this.box = entity.method_5829().method_997(offset);
            this.source = entity.method_48923().method_48819((class_1297) null, (class_1297) null);
            this.armor = entity.method_6096();
            this.toughness = (float) entity.method_45325(class_5134.field_23725);
            this.protection = CrystalDamage.protection(entity);
            class_1293 effect = entity.method_6112(class_1294.field_5907);
            this.resistance = effect == null ? 1.0f : Math.max(25 - ((effect.method_5578() + 1) * 5), 0) / 25.0f;
            class_1799 blocking = entity.method_62821();
            this.shield = blocking == null ? null : (class_10707) blocking.method_58694(class_9334.field_56396);
        }

        public class_1309 entity() {
            return this.entity;
        }

        public float damage(class_243 center) {
            return damage(center, false);
        }

        public float damage(class_243 center, boolean baseToCome) {
            return reduce(raw(center, baseToCome));
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
        public float raw(class_243 center, boolean baseToCome) throws MatchException {
            double distance = Math.sqrt(this.position.method_1025(center)) / CrystalDamage.REACH;
            if (distance > 1.0d) {
                return 0.0f;
            }
            float damage = scaled(CrystalDamage.falloff((1.0d - distance) * ((double) exposure(center, baseToCome))));
            return damage - blocked(center, damage);
        }

        public float reduce(float raw) {
            float damage = class_1280.method_5496(this.entity, raw, this.source, this.armor, this.toughness);
            return class_1280.method_5497(damage * this.resistance, this.protection);
        }

        public float damage(class_243 center, float power, class_2338 ignored) {
            return reduce(raw(center, power, ignored));
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
        public float raw(class_243 center, float power, class_2338 ignored) throws MatchException {
            double diameter = ((double) power) * 2.0d;
            double distance = Math.sqrt(this.position.method_1025(center)) / diameter;
            if (distance > 1.0d) {
                return 0.0f;
            }
            float damage = scaled(CrystalDamage.falloff((1.0d - distance) * ((double) blastExposure(center, ignored)), diameter));
            return damage - blocked(center, damage);
        }

        private float blocked(class_243 center, float damage) {
            if (this.shield == null || damage <= 0.0f) {
                return 0.0f;
            }
            class_243 facing = this.entity.method_5631(0.0f, this.entity.method_5791());
            class_243 toward = center.method_1020(this.position);
            class_243 flat = new class_243(toward.field_1352, 0.0d, toward.field_1350).method_1029();
            double angle = Math.acos(class_3532.method_15350(flat.method_1026(facing), -1.0d, 1.0d));
            return this.shield.method_67202(this.source, damage, angle);
        }

        public float upperBound(class_243 center) {
            double distance = Math.sqrt(this.position.method_1025(center)) / CrystalDamage.REACH;
            if (distance > 1.0d) {
                return 0.0f;
            }
            return reduce(scaled(CrystalDamage.falloff(1.0d - distance)));
        }

        public float upperBound(class_243 center, float power) {
            double diameter = ((double) power) * 2.0d;
            double distance = Math.sqrt(this.position.method_1025(center)) / diameter;
            if (distance > 1.0d) {
                return 0.0f;
            }
            return reduce(scaled(CrystalDamage.falloff(1.0d - distance, diameter)));
        }

        private float exposure(class_243 center, boolean baseToCome) {
            Float known = this.exposures.get(center);
            if (known != null) {
                return known.floatValue();
            }
            class_238 base = baseToCome ? new class_238(class_2338.method_49637(center.field_1352, center.field_1351 - 0.5d, center.field_1350)) : null;
            float seen = (!openAir(center, null) || (base != null && hull(center).method_994(base))) ? seenPercent(center, base, null) : 1.0f;
            this.exposures.put(center, Float.valueOf(seen));
            return seen;
        }

        private float blastExposure(class_243 center, class_2338 ignored) {
            Blast key = new Blast(center, ignored);
            Float known = this.blastExposures.get(key);
            if (known != null) {
                return known.floatValue();
            }
            float seen = openAir(center, ignored) ? 1.0f : seenPercent(center, null, ignored);
            this.blastExposures.put(key, Float.valueOf(seen));
            return seen;
        }

        private class_238 hull(class_243 center) {
            return this.box.method_991(new class_238(center, center));
        }

        private boolean openAir(class_243 center, class_2338 ignored) {
            class_1937 level = this.entity.method_73183();
            class_3726 context = class_3726.method_16195(this.entity);
            int minX = class_3532.method_15357(Math.min(this.box.field_1323, center.field_1352));
            int minY = class_3532.method_15357(Math.min(this.box.field_1322, center.field_1351));
            int minZ = class_3532.method_15357(Math.min(this.box.field_1321, center.field_1350));
            int maxX = class_3532.method_15357(Math.max(this.box.field_1320, center.field_1352));
            int maxY = class_3532.method_15357(Math.max(this.box.field_1325, center.field_1351));
            int maxZ = class_3532.method_15357(Math.max(this.box.field_1324, center.field_1350));
            class_2338.class_2339 pos = new class_2338.class_2339();
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        pos.method_10103(x, y, z);
                        if (!pos.equals(ignored) && !level.method_8320(pos).method_26194(level, pos, context).method_1110()) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        private float seenPercent(class_243 center, class_238 base, class_2338 ignored) {
            double stepX = 1.0d / (((this.box.field_1320 - this.box.field_1323) * 2.0d) + 1.0d);
            double stepY = 1.0d / (((this.box.field_1325 - this.box.field_1322) * 2.0d) + 1.0d);
            double stepZ = 1.0d / (((this.box.field_1324 - this.box.field_1321) * 2.0d) + 1.0d);
            double offsetX = (1.0d - (Math.floor(1.0d / stepX) * stepX)) / 2.0d;
            double offsetZ = (1.0d - (Math.floor(1.0d / stepZ) * stepZ)) / 2.0d;
            if (stepX < 0.0d || stepY < 0.0d || stepZ < 0.0d) {
                return 0.0f;
            }
            class_1937 level = this.entity.method_73183();
            int seen = 0;
            int total = 0;
            double d = 0.0d;
            while (true) {
                double fx = d;
                if (fx <= 1.0d) {
                    double d2 = 0.0d;
                    while (true) {
                        double fy = d2;
                        if (fy <= 1.0d) {
                            double d3 = 0.0d;
                            while (true) {
                                double fz = d3;
                                if (fz <= 1.0d) {
                                    class_243 from = new class_243(class_3532.method_16436(fx, this.box.field_1323, this.box.field_1320) + offsetX, class_3532.method_16436(fy, this.box.field_1322, this.box.field_1325), class_3532.method_16436(fz, this.box.field_1321, this.box.field_1324) + offsetZ);
                                    class_3965 hit = level.method_17742(new class_3959(from, center, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, this.entity));
                                    if ((hit.method_17783() == class_239.class_240.field_1333 || hit.method_17777().equals(ignored)) && (base == null || !throughBlock(base, from, center))) {
                                        seen++;
                                    }
                                    total++;
                                    d3 = fz + stepZ;
                                }
                            }
                            d2 = fy + stepY;
                        }
                    }
                    d = fx + stepX;
                } else {
                    return seen / total;
                }
            }
        }

        private static boolean throughBlock(class_238 block, class_243 from, class_243 to) {
            class_243 end = new class_243(class_3532.method_16436(1.0E-7d, to.field_1352, from.field_1352), class_3532.method_16436(1.0E-7d, to.field_1351, from.field_1351), class_3532.method_16436(1.0E-7d, to.field_1350, from.field_1350));
            return block.method_1006(from) || block.method_992(from, end).isPresent();
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
        private float scaled(float damage) throws MatchException {
            if (!(this.entity instanceof class_1657)) {
                return damage;
            }
            switch (AnonymousClass1.$SwitchMap$net$minecraft$world$Difficulty[this.entity.method_73183().method_8407().ordinal()]) {
                case 1:
                    return 0.0f;
                case 2:
                    return Math.min((damage / CrystalDamage.SELF_MARGIN) + 1.0f, damage);
                case 3:
                    return damage;
                case 4:
                    return damage * 1.5f;
                default:
                    throw new MatchException((String) null, (Throwable) null);
            }
        }
    }

    /* JADX INFO: renamed from: net.anubis.client.module.impl.combat.crystal.CrystalDamage$1, reason: invalid class name */
    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/crystal/CrystalDamage$1.class */
    @Environment(EnvType.CLIENT)
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$net$minecraft$world$Difficulty = new int[class_1267.values().length];

        static {
            try {
                $SwitchMap$net$minecraft$world$Difficulty[class_1267.field_5801.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$net$minecraft$world$Difficulty[class_1267.field_5805.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$net$minecraft$world$Difficulty[class_1267.field_5802.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$net$minecraft$world$Difficulty[class_1267.field_5807.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/crystal/CrystalDamage$Blast.class */
    @Environment(EnvType.CLIENT)
    private static final class Blast extends Record {
        private final class_243 center;
        private final class_2338 ignored;

        private Blast(class_243 center, class_2338 ignored) {
            this.center = center;
            this.ignored = ignored;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Blast.class), Blast.class, "center;ignored", "FIELD:Lnet/anubis/client/module/impl/combat/crystal/CrystalDamage$Blast;->center:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/module/impl/combat/crystal/CrystalDamage$Blast;->ignored:Lnet/minecraft/class_2338;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Blast.class), Blast.class, "center;ignored", "FIELD:Lnet/anubis/client/module/impl/combat/crystal/CrystalDamage$Blast;->center:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/module/impl/combat/crystal/CrystalDamage$Blast;->ignored:Lnet/minecraft/class_2338;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Blast.class, Object.class), Blast.class, "center;ignored", "FIELD:Lnet/anubis/client/module/impl/combat/crystal/CrystalDamage$Blast;->center:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/module/impl/combat/crystal/CrystalDamage$Blast;->ignored:Lnet/minecraft/class_2338;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_243 center() {
            return this.center;
        }

        public class_2338 ignored() {
            return this.ignored;
        }
    }

    private static float falloff(double exposure) {
        return falloff(exposure, REACH);
    }

    private static float falloff(double exposure, double diameter) {
        return (float) (((((exposure * exposure) + exposure) / 2.0d) * 7.0d * diameter) + 1.0d);
    }

    private static float protection(class_1309 entity) {
        class_2378<class_1887> enchantments = entity.method_56673().method_30530(class_7924.field_41265);
        int total = 0;
        for (class_1304 slot : ARMOR) {
            class_1799 stack = entity.method_6118(slot);
            if (!stack.method_7960()) {
                total += level(enchantments, class_1893.field_9111, stack) + (2 * level(enchantments, class_1893.field_9107, stack));
            }
        }
        return total;
    }

    private static int level(class_2378<class_1887> enchantments, class_5321<class_1887> key, class_1799 stack) {
        return ((Integer) enchantments.method_46746(key).map(holder -> {
            return Integer.valueOf(class_1890.method_8225(holder, stack));
        }).orElse(0)).intValue();
    }
}
