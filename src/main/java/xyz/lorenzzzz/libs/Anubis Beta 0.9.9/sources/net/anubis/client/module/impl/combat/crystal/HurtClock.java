package net.anubis.client.module.impl.combat.crystal;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.anubis.client.module.impl.combat.melee.MeleeAttacks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2716;
import net.minecraft.class_6880;
import net.minecraft.class_746;
import net.minecraft.class_8042;
import net.minecraft.class_8110;
import net.minecraft.class_8111;
import net.minecraft.class_8143;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/crystal/HurtClock.class */
@Environment(EnvType.CLIENT)
public final class HurtClock {
    private static final int NEVER = -1000;
    private static final int WINDOW = 10;
    private static final int MAX_WAIT = 20;
    private static final int MAX_MARGIN = 2;
    private static final int RELAX_STREAK = 8;
    private static final int REMOVE_GRACE = 3;
    private static final double ANCHOR_MATCH_SQR = 0.25d;
    private static final Int2ObjectOpenHashMap<Mark> MARKS = new Int2ObjectOpenHashMap<>();
    private static class_746 lastPlayer;
    private static class_1937 lastLevel;
    private static class_2596<?> lastPacket;
    private static int now;
    private static int margin;
    private static int streak;

    private HurtClock() {
    }

    public static void note(class_746 player, class_2596<?> packet) {
        if (packet == lastPacket) {
            return;
        }
        lastPacket = packet;
        if (packet instanceof class_8042) {
            class_8042 bundle = (class_8042) packet;
            for (class_2596<?> inner : bundle.method_48324()) {
                handle(player, inner);
            }
            return;
        }
        handle(player, packet);
    }

    public static void tick(class_746 player) {
        now = player.field_6012;
        if (player != lastPlayer || player.method_73183() != lastLevel) {
            MARKS.clear();
            margin = 0;
            streak = 0;
            lastPlayer = player;
            lastLevel = player.method_73183();
        }
        ObjectIterator<Int2ObjectMap.Entry<Mark>> iterator = MARKS.int2ObjectEntrySet().iterator();
        while (iterator.hasNext()) {
            Int2ObjectMap.Entry<Mark> entry = (Int2ObjectMap.Entry) iterator.next();
            class_1309 class_1309VarMethod_8469 = lastLevel.method_8469(entry.getIntKey());
            if (class_1309VarMethod_8469 instanceof class_1309) {
                class_1309 target = class_1309VarMethod_8469;
                if (target.method_5805()) {
                    Mark mark = (Mark) entry.getValue();
                    if (mark.removedAt != NEVER && !mark.confirmed && now - mark.removedAt > 3) {
                        margin = Math.min(2, margin + 1);
                        streak = 0;
                        mark.shotTick = NEVER;
                        mark.removedAt = NEVER;
                    }
                }
            }
            iterator.remove();
        }
    }

    public static boolean freshAt(class_1309 target, int ahead, int lead) {
        if ((target.field_6008 - ahead) - lead > 10) {
            return false;
        }
        Mark mark = (Mark) MARKS.get(target.method_5628());
        return mark == null || mark.shotTick == NEVER || (now + ahead) - mark.shotTick >= 10 + margin;
    }

    public static int opensIn(class_1309 target, int lead) {
        for (int ahead = 0; ahead < 20; ahead++) {
            if (freshAt(target, ahead, lead)) {
                return ahead;
            }
        }
        return 20;
    }

    public static float effective(CrystalDamage.Victim victim, float raw, int ahead, int lead) {
        class_1309 target = victim.entity();
        if (freshAt(target, ahead, lead)) {
            return victim.reduce(raw);
        }
        Mark mark = (Mark) MARKS.get(target.method_5628());
        float last = mark == null ? Float.MAX_VALUE : mark.lastRaw;
        if (raw > last) {
            return victim.reduce(raw - last);
        }
        return 0.0f;
    }

    public static void shot(class_746 player, class_1309 target, float raw, Object key, int lead) {
        Mark mark = (Mark) MARKS.computeIfAbsent(target.method_5628(), id -> {
            return new Mark();
        });
        if (freshAt(target, 0, lead)) {
            mark.shotTick = player.field_6012;
            mark.shotRaw = raw;
            mark.lastRaw = raw;
            mark.shotKey = key;
            mark.confirmed = false;
            mark.removedAt = NEVER;
            return;
        }
        mark.lastRaw = Math.max(mark.lastRaw, raw);
    }

    private static void handle(class_746 player, class_2596<?> packet) {
        if (packet instanceof class_8143) {
            class_8143 damage = (class_8143) packet;
            damaged(player, damage);
        } else if (packet instanceof class_2716) {
            class_2716 remove = (class_2716) packet;
            removed(remove.method_36548());
        }
    }

    private static void damaged(class_746 player, class_8143 packet) {
        class_1657 class_1657VarMethod_8469 = player.method_73183().method_8469(packet.comp_1267());
        if (class_1657VarMethod_8469 instanceof class_1657) {
            class_1657 target = class_1657VarMethod_8469;
            if (MeleeAttacks.isEnemy(player, target)) {
                Mark mark = (Mark) MARKS.computeIfAbsent(target.method_5628(), id -> {
                    return new Mark();
                });
                if (mark.shotTick != NEVER && !mark.confirmed && ours(player, mark, packet)) {
                    mark.lastRaw = mark.shotRaw;
                    mark.confirmed = true;
                    if (mark.shotKey instanceof Integer) {
                        int i = streak + 1;
                        streak = i;
                        if (i >= 8) {
                            margin = Math.max(0, margin - 1);
                            streak = 0;
                            return;
                        }
                        return;
                    }
                    return;
                }
                class_6880<class_8110> type = packet.comp_1268();
                mark.shotTick = NEVER;
                mark.removedAt = NEVER;
                mark.lastRaw = (type.method_40225(class_8111.field_42320) || type.method_40225(class_8111.field_42360)) ? 0.0f : Float.MAX_VALUE;
            }
        }
    }

    private static boolean ours(class_746 player, Mark mark, class_8143 packet) {
        class_6880<class_8110> type = packet.comp_1268();
        if (type.method_40225(class_8111.field_42332)) {
            if (packet.comp_1269() == player.method_5628()) {
                Object obj = mark.shotKey;
                if (obj instanceof Integer) {
                    Integer crystal = (Integer) obj;
                    if (packet.comp_1270() == crystal.intValue()) {
                        return true;
                    }
                }
            }
            return false;
        }
        if (type.method_40225(class_8111.field_42334)) {
            Object obj2 = mark.shotKey;
            if (obj2 instanceof class_243) {
                class_243 center = (class_243) obj2;
                if (packet.comp_1271().filter(at -> {
                    return at.method_1025(center) < ANCHOR_MATCH_SQR;
                }).isPresent()) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void removed(IntList ids) {
        ObjectIterator it = MARKS.values().iterator();
        while (it.hasNext()) {
            Mark mark = (Mark) it.next();
            if (mark.shotTick != NEVER && !mark.confirmed) {
                Object obj = mark.shotKey;
                if (obj instanceof Integer) {
                    Integer crystal = (Integer) obj;
                    if (ids.contains(crystal.intValue())) {
                        mark.removedAt = now;
                    }
                }
            }
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/combat/crystal/HurtClock$Mark.class */
    @Environment(EnvType.CLIENT)
    private static final class Mark {
        float shotRaw;
        Object shotKey;
        boolean confirmed;
        float lastRaw = Float.MAX_VALUE;
        int shotTick = HurtClock.NEVER;
        int removedAt = HurtClock.NEVER;

        private Mark() {
        }
    }
}
