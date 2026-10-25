package net.anubis.client.module.impl.combat.anchor;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.anubis.client.module.impl.combat.crystal.Crystals;
import net.anubis.client.module.impl.combat.melee.MeleeAttacks;
import net.anubis.client.util.pvp.Rotation;
import net.anubis.client.util.pvp.RotationUtils;
import net.anubis.client.util.pvp.ServerRotation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1661;
import net.minecraft.class_1747;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2343;
import net.minecraft.class_2346;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2682;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_638;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/anchor/AnchorGuard.class */
@Environment(EnvType.CLIENT)
public final class AnchorGuard {
    public static final float BLAST_POWER = 5.0f;
    private static final double MIN_SHADE = 0.15d;
    private static final double FACE_INSET = 0.3d;
    private static final double AIM_INSET = 0.1d;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/anchor/AnchorGuard$Spot.class */
    @Environment(EnvType.CLIENT)
    public static final class Spot extends Record {
        private final class_2338 pos;
        private final class_2338 support;
        private final class_2350 face;
        private final class_243 aim;

        public Spot(class_2338 pos, class_2338 support, class_2350 face, class_243 aim) {
            this.pos = pos;
            this.support = support;
            this.face = face;
            this.aim = aim;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Spot.class), Spot.class, "pos;support;face;aim", "FIELD:Lnet/anubis/client/module/impl/combat/anchor/AnchorGuard$Spot;->pos:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/combat/anchor/AnchorGuard$Spot;->support:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/combat/anchor/AnchorGuard$Spot;->face:Lnet/minecraft/class_2350;", "FIELD:Lnet/anubis/client/module/impl/combat/anchor/AnchorGuard$Spot;->aim:Lnet/minecraft/class_243;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Spot.class), Spot.class, "pos;support;face;aim", "FIELD:Lnet/anubis/client/module/impl/combat/anchor/AnchorGuard$Spot;->pos:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/combat/anchor/AnchorGuard$Spot;->support:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/combat/anchor/AnchorGuard$Spot;->face:Lnet/minecraft/class_2350;", "FIELD:Lnet/anubis/client/module/impl/combat/anchor/AnchorGuard$Spot;->aim:Lnet/minecraft/class_243;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Spot.class, Object.class), Spot.class, "pos;support;face;aim", "FIELD:Lnet/anubis/client/module/impl/combat/anchor/AnchorGuard$Spot;->pos:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/combat/anchor/AnchorGuard$Spot;->support:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/combat/anchor/AnchorGuard$Spot;->face:Lnet/minecraft/class_2350;", "FIELD:Lnet/anubis/client/module/impl/combat/anchor/AnchorGuard$Spot;->aim:Lnet/minecraft/class_243;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_2338 pos() {
            return this.pos;
        }

        public class_2338 support() {
            return this.support;
        }

        public class_2350 face() {
            return this.face;
        }

        public class_243 aim() {
            return this.aim;
        }

        public boolean standing() {
            return this.support == null;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/anchor/AnchorGuard$Cell.class */
    @Environment(EnvType.CLIENT)
    private static final class Cell extends Record {
        private final class_2338 pos;
        private final int shade;
        private final double distance;

        private Cell(class_2338 pos, int shade, double distance) {
            this.pos = pos;
            this.shade = shade;
            this.distance = distance;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Cell.class), Cell.class, "pos;shade;distance", "FIELD:Lnet/anubis/client/module/impl/combat/anchor/AnchorGuard$Cell;->pos:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/combat/anchor/AnchorGuard$Cell;->shade:I", "FIELD:Lnet/anubis/client/module/impl/combat/anchor/AnchorGuard$Cell;->distance:D").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Cell.class), Cell.class, "pos;shade;distance", "FIELD:Lnet/anubis/client/module/impl/combat/anchor/AnchorGuard$Cell;->pos:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/combat/anchor/AnchorGuard$Cell;->shade:I", "FIELD:Lnet/anubis/client/module/impl/combat/anchor/AnchorGuard$Cell;->distance:D").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Cell.class, Object.class), Cell.class, "pos;shade;distance", "FIELD:Lnet/anubis/client/module/impl/combat/anchor/AnchorGuard$Cell;->pos:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/combat/anchor/AnchorGuard$Cell;->shade:I", "FIELD:Lnet/anubis/client/module/impl/combat/anchor/AnchorGuard$Cell;->distance:D").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_2338 pos() {
            return this.pos;
        }

        public int shade() {
            return this.shade;
        }

        public double distance() {
            return this.distance;
        }
    }

    private AnchorGuard() {
    }

    public static Spot find(class_638 level, class_746 player, class_2338 anchor, class_243 sightline) {
        Spot spotDirect;
        int shade;
        class_243 blast = class_243.method_24953(anchor);
        class_238 body = player.method_5829();
        List<class_243> points = bodyPoints(body);
        List<class_243> open = new ArrayList<>(points.size());
        for (class_243 point : points) {
            class_3965 hit = level.method_17742(new class_3959(point, blast, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, player));
            if (hit.method_17783() == class_239.class_240.field_1333 || hit.method_17777().equals(anchor)) {
                open.add(point);
            }
        }
        class_2338 feet = player.method_24515();
        if (open.size() < Math.ceil(((double) points.size()) * MIN_SHADE)) {
            return new Spot(feet, null, null, null);
        }
        int needed = (int) Math.ceil(((double) open.size()) * MIN_SHADE);
        class_243 eye = player.method_33571();
        Set<class_2338> around = new LinkedHashSet<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx != 0 || dz != 0) {
                    around.add(feet.method_10069(dx, 0, dz));
                }
            }
        }
        for (class_2350 side : class_2350.class_2353.field_11062) {
            around.add(anchor.method_10093(side));
        }
        List<Cell> cells = new ArrayList<>(around.size());
        for (class_2338 pos : around) {
            class_238 box = new class_238(pos);
            if (!pos.equals(anchor) && !box.method_994(body) && !box.method_992(eye, sightline).isPresent() && (shade = shade(box, blast, open)) >= needed) {
                cells.add(new Cell(pos, shade, box.method_1005().method_1025(blast)));
            }
        }
        cells.sort(Comparator.comparingInt((v0) -> {
            return v0.shade();
        }).reversed().thenComparingDouble((v0) -> {
            return v0.distance();
        }));
        for (Cell cell : cells) {
            class_2680 state = level.method_8320(cell.pos());
            if (state.method_45474()) {
                if (state.method_26218(level, cell.pos()).method_1110()) {
                    spotDirect = supportFor(level, player, eye, cell.pos());
                } else {
                    spotDirect = direct(level, player, eye, cell.pos(), state);
                }
                Spot spot = spotDirect;
                if (spot != null) {
                    return spot;
                }
            }
        }
        return null;
    }

    public static class_3965 hitAlong(class_638 level, class_746 player, Rotation look, Spot spot) {
        class_3965 hit = trace(level, player, look);
        if (hit == null || !hit.method_17777().equals(spot.support())) {
            return null;
        }
        if (spot.face() != null && hit.method_17780() != spot.face()) {
            return null;
        }
        class_2338 placed = Crystals.placedObsidian(level, player, hit, level.method_8320(spot.support()));
        if (spot.pos().equals(placed)) {
            return hit;
        }
        return null;
    }

    public static class_3965 anchorHit(class_638 level, class_746 player, class_2338 anchor) {
        Rotation sent = ServerRotation.sent();
        class_3965 hit = sent == null ? null : trace(level, player, sent);
        if (hit == null || !hit.method_17777().equals(anchor)) {
            return null;
        }
        return hit;
    }

    public static boolean isGuardBlock(class_1799 stack) {
        class_1747 class_1747VarMethod_7909 = stack.method_7909();
        if (!(class_1747VarMethod_7909 instanceof class_1747)) {
            return false;
        }
        class_1747 item = class_1747VarMethod_7909;
        class_2248 block = item.method_7711();
        if ((block instanceof class_2343) || (block instanceof class_2346) || block == class_2246.field_23152 || block == class_2246.field_10375) {
            return false;
        }
        return block.method_9564().method_26234(class_2682.field_12294, class_2338.field_10980);
    }

    public static int detonatorSlot(class_1661 inventory) {
        int totem = Crystals.hotbarSlotOf(inventory, class_1802.field_8288);
        if (totem != -1) {
            return totem;
        }
        for (int slot = 0; slot < class_1661.method_7368(); slot++) {
            if (Crystals.isCombatItem(inventory.method_5438(slot))) {
                return slot;
            }
        }
        return -1;
    }

    public static int detonatorRoute(class_1661 inventory, class_1799 main) {
        if (Crystals.isCombatItem(main)) {
            return -2;
        }
        int slot = detonatorSlot(inventory);
        if (slot == -1) {
            return -1;
        }
        return slot;
    }

    public static class_243 anchorAim(class_638 level, class_746 player, class_2338 anchor) {
        class_243 eye = player.method_33571();
        class_238 box = new class_238(anchor).method_1011(AIM_INSET);
        class_243 point = new class_243(class_3532.method_15350(eye.field_1352, box.field_1323, box.field_1320), class_3532.method_15350(eye.field_1351, box.field_1322, box.field_1325), class_3532.method_15350(eye.field_1350, box.field_1321, box.field_1324));
        class_3965 hit = trace(level, player, ServerRotation.next(player, RotationUtils.fromTo(eye, point)));
        if (hit == null || !hit.method_17777().equals(anchor)) {
            return null;
        }
        return point;
    }

    public static void click(class_310 mc, class_746 player, class_3965 hit) {
        class_1799 held = player.method_6047();
        int countBefore = held.method_7947();
        class_1269.class_9860 class_9860VarMethod_2896 = mc.field_1761.method_2896(player, class_1268.field_5808, hit);
        if (class_9860VarMethod_2896 instanceof class_1269.class_9860) {
            class_1269.class_9860 success = class_9860VarMethod_2896;
            if (success.comp_2909() == class_1269.class_9861.field_52427) {
                player.method_6104(class_1268.field_5808);
                if (!held.method_7960() && (held.method_7947() != countBefore || player.method_56992())) {
                    mc.field_1773.field_4012.method_3215(class_1268.field_5808);
                }
            }
        }
        MeleeAttacks.claimTick(mc);
    }

    private static Spot direct(class_638 level, class_746 player, class_243 eye, class_2338 pos, class_2680 state) {
        Spot spot = new Spot(pos, pos, null, state.method_26218(level, pos).method_1107().method_996(pos).method_1005());
        Rotation look = ServerRotation.next(player, RotationUtils.fromTo(eye, spot.aim()));
        if (hitAlong(level, player, look, spot) != null) {
            return spot;
        }
        return null;
    }

    public static Spot supportFor(class_638 level, class_746 player, class_243 eye, class_2338 cell) {
        Spot best = null;
        double bestDistance = Double.MAX_VALUE;
        class_2350[] class_2350VarArrValues = class_2350.values();
        int length = class_2350VarArrValues.length;
        for (int i = 0; i < length; i++) {
            class_2350 side = class_2350VarArrValues[i];
            class_2338 support = cell.method_10093(side);
            class_2680 state = level.method_8320(support);
            if (!state.method_45474()) {
                class_265 shape = state.method_26218(level, support);
                if (!shape.method_1110()) {
                    class_2350 face = side.method_10153();
                    Spot spot = null;
                    for (class_243 aim : faceAims(shape.method_1107().method_996(support), face)) {
                        Spot candidate = new Spot(cell, support, face, aim);
                        Rotation look = ServerRotation.next(player, RotationUtils.fromTo(eye, aim));
                        if (hitAlong(level, player, look, candidate) != null) {
                            spot = candidate;
                            break;
                        }
                    }
                    if (spot != null) {
                        double distance = side == class_2350.field_11033 ? -1.0d : eye.method_1025(spot.aim());
                        if (distance < bestDistance) {
                            best = spot;
                            bestDistance = distance;
                        }
                    }
                }
            }
        }
        return best;
    }

    private static List<class_243> faceAims(class_238 box, class_2350 face) {
        class_2350.class_2351 axis = face.method_10166();
        double plane = face.method_10171() == class_2350.class_2352.field_11056 ? box.method_990(axis) : box.method_1001(axis);
        class_243 center = box.method_1005().method_38499(axis, plane);
        List<class_243> aims = new ArrayList<>(5);
        aims.add(center);
        for (class_2350 toward : class_2350.values()) {
            class_2350.class_2351 along = toward.method_10166();
            if (along != axis) {
                aims.add(center.method_43206(toward, (box.method_990(along) - box.method_1001(along)) * FACE_INSET));
            }
        }
        return aims;
    }

    public static class_3965 trace(class_638 level, class_746 player, Rotation look) {
        class_243 eye = player.method_33571();
        class_243 reach = class_243.method_1030(look.pitch(), look.yaw()).method_1021(player.method_55754());
        class_3965 hit = level.method_17742(new class_3959(eye, eye.method_1019(reach), class_3959.class_3960.field_17559, class_3959.class_242.field_1348, player));
        if (hit.method_17783() == class_239.class_240.field_1332) {
            return hit;
        }
        return null;
    }

    private static int shade(class_238 box, class_243 blast, List<class_243> points) {
        int blocked = 0;
        for (class_243 point : points) {
            if (box.method_992(point, blast).isPresent()) {
                blocked++;
            }
        }
        return blocked;
    }

    private static List<class_243> bodyPoints(class_238 body) {
        double stepX = 1.0d / (((body.field_1320 - body.field_1323) * 2.0d) + 1.0d);
        double stepY = 1.0d / (((body.field_1325 - body.field_1322) * 2.0d) + 1.0d);
        double stepZ = 1.0d / (((body.field_1324 - body.field_1321) * 2.0d) + 1.0d);
        double offsetX = (1.0d - (Math.floor(1.0d / stepX) * stepX)) / 2.0d;
        double offsetZ = (1.0d - (Math.floor(1.0d / stepZ) * stepZ)) / 2.0d;
        List<class_243> points = new ArrayList<>();
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
                                points.add(new class_243(class_3532.method_16436(fx, body.field_1323, body.field_1320) + offsetX, class_3532.method_16436(fy, body.field_1322, body.field_1325), class_3532.method_16436(fz, body.field_1321, body.field_1324) + offsetZ));
                                d3 = fz + stepZ;
                            }
                        }
                        d2 = fy + stepY;
                    }
                }
                d = fx + stepX;
            } else {
                return points;
            }
        }
    }
}
