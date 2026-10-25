package net.anubis.client.util;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2591;
import net.minecraft.class_2596;
import net.minecraft.class_2622;
import net.minecraft.class_2672;
import net.minecraft.class_2678;
import net.minecraft.class_2724;
import net.minecraft.class_2960;
import net.minecraft.class_6603;
import net.minecraft.class_7923;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/BlockEntityLeakTracker.class */
@Environment(EnvType.CLIENT)
public final class BlockEntityLeakTracker {
    private static final Map<class_2338, Entry> FOUND = new ConcurrentHashMap();
    private static final ConcurrentLinkedQueue<class_2338> ADDED = new ConcurrentLinkedQueue<>();
    private static final AtomicInteger GENERATION = new AtomicInteger();
    private static volatile boolean recordAdditions;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/BlockEntityLeakTracker$Entry.class */
    @Environment(EnvType.CLIENT)
    public static final class Entry extends Record {
        private final class_2248 block;

        public Entry(class_2248 block) {
            this.block = block;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Entry.class), Entry.class, "block", "FIELD:Lnet/anubis/client/util/BlockEntityLeakTracker$Entry;->block:Lnet/minecraft/class_2248;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Entry.class), Entry.class, "block", "FIELD:Lnet/anubis/client/util/BlockEntityLeakTracker$Entry;->block:Lnet/minecraft/class_2248;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Entry.class, Object.class), Entry.class, "block", "FIELD:Lnet/anubis/client/util/BlockEntityLeakTracker$Entry;->block:Lnet/minecraft/class_2248;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_2248 block() {
            return this.block;
        }
    }

    private BlockEntityLeakTracker() {
    }

    public static Map<class_2338, Entry> view() {
        return FOUND;
    }

    public static boolean contains(class_2338 pos) {
        return FOUND.containsKey(pos);
    }

    public static void clear() {
        FOUND.clear();
        ADDED.clear();
        GENERATION.incrementAndGet();
    }

    public static int generation() {
        return GENERATION.get();
    }

    public static void recordAdditions(boolean on) {
        recordAdditions = on;
        if (!on) {
            ADDED.clear();
        }
    }

    public static class_2338 pollAdded() {
        return ADDED.poll();
    }

    public static void ingest(class_2596<?> packet) {
        if (packet instanceof class_2672) {
            class_2672 chunkPacket = (class_2672) packet;
            class_6603 data = chunkPacket.method_38598();
            try {
                data.method_38587(chunkPacket.method_11523(), chunkPacket.method_11524()).accept((pos, type, tag) -> {
                    if (type != null) {
                        put(pos, type);
                    }
                });
                return;
            } catch (Throwable th) {
                return;
            }
        }
        if (packet instanceof class_2622) {
            class_2622 bePacket = (class_2622) packet;
            put(bePacket.method_11293(), bePacket.method_11291());
        } else if ((packet instanceof class_2678) || (packet instanceof class_2724)) {
            clear();
        }
    }

    private static void put(class_2338 pos, class_2591<?> type) {
        class_2338 key = pos.method_10062();
        if (FOUND.putIfAbsent(key, new Entry(blockFromType(type))) != null || !recordAdditions) {
            return;
        }
        ADDED.add(key);
    }

    public static class_2248 blockFromType(class_2591<?> type) {
        class_2248 class_2248Var;
        class_2960 id = class_7923.field_41181.method_10221(type);
        if (id == null) {
            return class_2246.field_10124;
        }
        String path = id.method_12832();
        switch (path) {
            case "mob_spawner":
                class_2248Var = class_2246.field_10260;
                break;
            case "piston":
                class_2248Var = class_2246.field_10008;
                break;
            case "sign":
                class_2248Var = class_2246.field_10121;
                break;
            case "hanging_sign":
                class_2248Var = class_2246.field_40262;
                break;
            case "skull":
                class_2248Var = class_2246.field_10481;
                break;
            case "banner":
                class_2248Var = class_2246.field_10154;
                break;
            case "bed":
                class_2248Var = class_2246.field_10069;
                break;
            case "brushable_block":
                class_2248Var = class_2246.field_42728;
                break;
            default:
                class_2248Var = null;
                break;
        }
        class_2248 mapped = class_2248Var;
        if (mapped != null) {
            return mapped;
        }
        return (class_2248) class_7923.field_41175.method_10223(id).map((v0) -> {
            return v0.comp_349();
        }).orElse(class_2246.field_10124);
    }
}
