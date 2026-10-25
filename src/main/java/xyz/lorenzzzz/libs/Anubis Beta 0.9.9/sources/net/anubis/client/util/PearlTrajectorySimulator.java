package net.anubis.client.util;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1301;
import net.minecraft.class_1676;
import net.minecraft.class_1776;
import net.minecraft.class_1802;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_3486;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/PearlTrajectorySimulator.class */
@Environment(EnvType.CLIENT)
public final class PearlTrajectorySimulator {
    private static final double SPAWN_EYE_OFFSET = 0.1d;
    private static final double GRAVITY = 0.03d;
    private static final float AIR_DRAG = 0.99f;
    private static final float WATER_DRAG = 0.8f;
    private static final int MAX_TICKS = 300;
    private static final double ENTITY_MARGIN = 0.3d;

    private PearlTrajectorySimulator() {
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/PearlTrajectorySimulator$SimulationResult.class */
    @Environment(EnvType.CLIENT)
    public static final class SimulationResult extends Record {
        private final List<class_243> points;
        private final class_243 landing;
        private final class_3965 blockHit;
        private final class_1297 entityHit;
        private final int ticks;

        public SimulationResult(List<class_243> points, class_243 landing, class_3965 blockHit, class_1297 entityHit, int ticks) {
            this.points = points;
            this.landing = landing;
            this.blockHit = blockHit;
            this.entityHit = entityHit;
            this.ticks = ticks;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, SimulationResult.class), SimulationResult.class, "points;landing;blockHit;entityHit;ticks", "FIELD:Lnet/anubis/client/util/PearlTrajectorySimulator$SimulationResult;->points:Ljava/util/List;", "FIELD:Lnet/anubis/client/util/PearlTrajectorySimulator$SimulationResult;->landing:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/util/PearlTrajectorySimulator$SimulationResult;->blockHit:Lnet/minecraft/class_3965;", "FIELD:Lnet/anubis/client/util/PearlTrajectorySimulator$SimulationResult;->entityHit:Lnet/minecraft/class_1297;", "FIELD:Lnet/anubis/client/util/PearlTrajectorySimulator$SimulationResult;->ticks:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, SimulationResult.class), SimulationResult.class, "points;landing;blockHit;entityHit;ticks", "FIELD:Lnet/anubis/client/util/PearlTrajectorySimulator$SimulationResult;->points:Ljava/util/List;", "FIELD:Lnet/anubis/client/util/PearlTrajectorySimulator$SimulationResult;->landing:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/util/PearlTrajectorySimulator$SimulationResult;->blockHit:Lnet/minecraft/class_3965;", "FIELD:Lnet/anubis/client/util/PearlTrajectorySimulator$SimulationResult;->entityHit:Lnet/minecraft/class_1297;", "FIELD:Lnet/anubis/client/util/PearlTrajectorySimulator$SimulationResult;->ticks:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, SimulationResult.class, Object.class), SimulationResult.class, "points;landing;blockHit;entityHit;ticks", "FIELD:Lnet/anubis/client/util/PearlTrajectorySimulator$SimulationResult;->points:Ljava/util/List;", "FIELD:Lnet/anubis/client/util/PearlTrajectorySimulator$SimulationResult;->landing:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/util/PearlTrajectorySimulator$SimulationResult;->blockHit:Lnet/minecraft/class_3965;", "FIELD:Lnet/anubis/client/util/PearlTrajectorySimulator$SimulationResult;->entityHit:Lnet/minecraft/class_1297;", "FIELD:Lnet/anubis/client/util/PearlTrajectorySimulator$SimulationResult;->ticks:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public List<class_243> points() {
            return this.points;
        }

        public class_243 landing() {
            return this.landing;
        }

        public class_3965 blockHit() {
            return this.blockHit;
        }

        public class_1297 entityHit() {
            return this.entityHit;
        }

        public int ticks() {
            return this.ticks;
        }

        public boolean landed() {
            return (this.blockHit == null && this.entityHit == null) ? false : true;
        }
    }

    public static SimulationResult simulate(class_1937 level, class_746 player, float partialTick) {
        class_243 eye = player.method_5836(partialTick);
        class_243 pos = new class_243(eye.field_1352, eye.field_1351 - SPAWN_EYE_OFFSET, eye.field_1350);
        class_243 velocity = player.method_5828(partialTick).method_1021(class_1776.field_55033);
        class_243 motion = player.method_60478();
        return simulate(level, pos, velocity.method_1031(motion.field_1352, player.method_24828() ? 0.0d : motion.field_1351, motion.field_1350), player, null);
    }

    public static SimulationResult simulate(class_1937 level, class_1676 pearl) {
        return simulate(level, pearl.method_73189(), pearl.method_18798(), pearl, pearl.method_24921());
    }

    /* JADX WARN: Code duplicated, block: B:21:0x00ed  */
    /* JADX WARN: Code duplicated, block: B:30:0x0112 A[EDGE_INSN: B:30:0x0112->B:25:0x0112 BREAK  A[LOOP:0: B:3:0x0020->B:31:?], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:31:? A[LOOP:0: B:3:0x0020->B:31:?, LOOP_END, SYNTHETIC] */
    public static SimulationResult simulate(class_1937 level, class_243 start, class_243 velocity, class_1297 ignore, class_1297 alsoIgnore) {
        List<class_243> points = new ArrayList<>(64);
        points.add(start);
        class_243 pos = start;
        class_3965 blockHit = null;
        class_1297 entityHit = null;
        int ticks = 0;
        while (ticks < MAX_TICKS) {
            ticks++;
            velocity = velocity.method_1031(0.0d, -0.03d, 0.0d).method_1021(isInWater(level, pos) ? 0.800000011920929d : 0.9900000095367432d);
            class_243 next = pos.method_1019(velocity);
            class_3965 class_3965VarMethod_17742 = level.method_17742(new class_3959(pos, next, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, ignore));
            class_243 end = class_3965VarMethod_17742.method_17783() != class_239.class_240.field_1333 ? class_3965VarMethod_17742.method_17784() : next;
            class_1297 entity = firstEntityOnSegment(level, pos, end, ignore, alsoIgnore);
            if (entity != null) {
                class_243 at = (class_243) entity.method_5829().method_1014(ENTITY_MARGIN).method_992(pos, end).orElse(end);
                points.add(at);
                entityHit = entity;
                break;
            }
            if (class_3965VarMethod_17742 instanceof class_3965) {
                class_3965 hit = class_3965VarMethod_17742;
                if (hit.method_17783() != class_239.class_240.field_1333) {
                    points.add(end);
                    blockHit = hit;
                    break;
                }
                pos = next;
                points.add(pos);
                if (pos.field_1351 < level.method_31607() - 64) {
                    break;
                }
            } else {
                pos = next;
                points.add(pos);
                if (pos.field_1351 < level.method_31607() - 64) {
                    break;
                    break;
                }
            }
        }
        return new SimulationResult(List.copyOf(points), (class_243) points.getLast(), blockHit, entityHit, ticks);
    }

    private static class_1297 firstEntityOnSegment(class_1937 level, class_243 from, class_243 to, class_1297 ignore, class_1297 alsoIgnore) {
        class_238 sweep = new class_238(from, to).method_1014(1.0d);
        class_1297 closest = null;
        double closestSq = Double.MAX_VALUE;
        for (class_1297 entity : level.method_8333(ignore, sweep, PearlTrajectorySimulator::canHit)) {
            if (entity != alsoIgnore) {
                Optional<class_243> hit = entity.method_5829().method_1014(ENTITY_MARGIN).method_992(from, to);
                if (!hit.isEmpty()) {
                    double distanceSq = from.method_1025(hit.get());
                    if (distanceSq < closestSq) {
                        closestSq = distanceSq;
                        closest = entity;
                    }
                }
            }
        }
        return closest;
    }

    private static boolean canHit(class_1297 entity) {
        return class_1301.field_6155.test(entity) && entity.method_5863() && !(entity instanceof class_746);
    }

    private static boolean isInWater(class_1937 level, class_243 pos) {
        return level.method_8316(class_2338.method_49638(pos)).method_15767(class_3486.field_15517);
    }

    public static boolean isHoldingPearl(class_746 player) {
        return player.method_6047().method_31574(class_1802.field_8634) || player.method_6079().method_31574(class_1802.field_8634);
    }
}
