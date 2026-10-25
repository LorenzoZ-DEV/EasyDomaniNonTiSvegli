package net.anubis.client.esp;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.LongPredicate;
import net.anubis.client.event.impl.BlockUpdateEvent;
import net.anubis.client.event.impl.PacketReceiveNettyEvent;
import net.anubis.client.module.impl.client.PerformanceModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.class_1923;
import net.minecraft.class_2338;
import net.minecraft.class_2678;
import net.minecraft.class_2680;
import net.minecraft.class_2818;
import net.minecraft.class_2826;
import net.minecraft.class_310;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/esp/ChunkScanner.class */
@Environment(EnvType.CLIENT)
public final class ChunkScanner {
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private static final Object OWNER = new Object();
    private static final Consumer[] NONE = new Consumer[0];
    private static final List<Consumer> consumers = new ArrayList();
    private static Consumer[] snapshot = NONE;
    private static final ArrayDeque<Long> queue = new ArrayDeque<>();
    private static final Set<Long> queued = new HashSet();
    private static volatile boolean resetPending;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/esp/ChunkScanner$Consumer.class */
    @Environment(EnvType.CLIENT)
    public interface Consumer {
        boolean wants(class_2680 class_2680Var);

        void found(long j, class_2680 class_2680Var);

        void removed(long j);

        void reset();

        default void chunkScanned(long chunkKey, LongPredicate stillThere) {
        }
    }

    private ChunkScanner() {
    }

    public static void init() {
        ClientChunkEvents.CHUNK_LOAD.register((level, chunk) -> {
            enqueue(chunk.method_12004().method_8324());
        });
        ClientTickEvents.END_CLIENT_TICK.register(ChunkScanner::tick);
        BlockUpdateEvent.TYPE.listen(OWNER, ChunkScanner::onBlockUpdate);
        PacketReceiveNettyEvent.TYPE.listen(OWNER, event -> {
            if (event.packet instanceof class_2678) {
                resetPending = true;
            }
        });
    }

    public static void register(Consumer consumer) {
        if (consumers.contains(consumer)) {
            return;
        }
        consumers.add(consumer);
        snapshot = (Consumer[]) consumers.toArray(NONE);
        queueLoadedChunks();
    }

    public static void unregister(Consumer consumer) {
        consumers.remove(consumer);
        snapshot = (Consumer[]) consumers.toArray(NONE);
        if (consumers.isEmpty()) {
            queue.clear();
            queued.clear();
        }
    }

    public static int pending() {
        return queue.size();
    }

    public static void rescan() {
        queueLoadedChunks();
    }

    private static void queueLoadedChunks() {
        class_310 mc = class_310.method_1551();
        if (mc.field_1687 == null || mc.field_1724 == null) {
            return;
        }
        int cx = mc.field_1724.method_31477() >> 4;
        int cz = mc.field_1724.method_31479() >> 4;
        int radius = ((Integer) mc.field_1690.method_42503().method_41753()).intValue() + 1;
        List<long[]> loaded = new ArrayList<>();
        for (int x = cx - radius; x <= cx + radius; x++) {
            for (int z = cz - radius; z <= cz + radius; z++) {
                if (mc.field_1687.method_2935().method_12123(x, z)) {
                    int dx = x - cx;
                    int dz = z - cz;
                    loaded.add(new long[]{class_1923.method_8331(x, z), (dx * dx) + (dz * dz)});
                }
            }
        }
        loaded.sort(Comparator.comparingLong(a -> {
            return a[1];
        }));
        for (long[] entry : loaded) {
            enqueue(entry[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void enqueue(long chunkKey) {
        if (consumers.isEmpty() || !queued.add(Long.valueOf(chunkKey))) {
            return;
        }
        queue.add(Long.valueOf(chunkKey));
    }

    private static void tick(class_310 mc) {
        if (resetPending) {
            resetPending = false;
            queue.clear();
            queued.clear();
            for (Consumer consumer : snapshot) {
                call(consumer, (v0) -> {
                    v0.reset();
                });
            }
            queueLoadedChunks();
        }
        if (mc.field_1687 == null || consumers.isEmpty()) {
            return;
        }
        long deadline = System.nanoTime() + PerformanceModule.scanBudgetNanos();
        do {
            Long key = queue.poll();
            if (key != null) {
                queued.remove(key);
                class_2818 chunk = mc.field_1687.method_2935().method_21730(class_1923.method_8325(key.longValue()), class_1923.method_8332(key.longValue()));
                if (chunk != null) {
                    scan(mc, chunk);
                }
            } else {
                return;
            }
        } while (System.nanoTime() < deadline);
    }

    private static void scan(class_310 mc, class_2818 chunk) {
        int minX = chunk.method_12004().method_8326();
        int minZ = chunk.method_12004().method_8328();
        int minSection = mc.field_1687.method_8597().comp_651() >> 4;
        class_2826[] sections = chunk.method_12006();
        List<Consumer> interested = new ArrayList<>(consumers.size());
        for (int si = 0; si < sections.length; si++) {
            class_2826 section = sections[si];
            if (section != null && !section.method_38292()) {
                interested.clear();
                for (Consumer consumer : consumers) {
                    Objects.requireNonNull(consumer);
                    if (section.method_19523(consumer::wants)) {
                        interested.add(consumer);
                    }
                }
                if (!interested.isEmpty()) {
                    int baseY = (minSection + si) << 4;
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            for (int y = 0; y < 16; y++) {
                                class_2680 state = section.method_12254(x, y, z);
                                if (!state.method_26215()) {
                                    long pos = class_2338.method_10064(minX + x, baseY + y, minZ + z);
                                    for (Consumer consumer2 : interested) {
                                        if (consumer2.wants(state)) {
                                            found(consumer2, pos, state);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        long chunkKey = chunk.method_12004().method_8324();
        class_2338.class_2339 probe = new class_2338.class_2339();
        for (Consumer consumer3 : snapshot) {
            call(consumer3, c -> {
                c.chunkScanned(chunkKey, pos2 -> {
                    class_2680 state2 = chunk.method_8320(probe.method_16363(pos2));
                    return !state2.method_26215() && c.wants(state2);
                });
            });
        }
    }

    private static void onBlockUpdate(BlockUpdateEvent event) {
        Consumer[] current = snapshot;
        if (current.length == 0) {
            return;
        }
        long pos = event.pos.method_10063();
        class_2680 state = event.newState;
        for (Consumer consumer : current) {
            if (state.method_26215() || !consumer.wants(state)) {
                removed(consumer, pos);
            } else {
                found(consumer, pos, state);
            }
        }
    }

    private static void found(Consumer consumer, long pos, class_2680 state) {
        try {
            consumer.found(pos, state);
        } catch (RuntimeException e) {
            drop(consumer, e);
        }
    }

    private static void removed(Consumer consumer, long pos) {
        try {
            consumer.removed(pos);
        } catch (RuntimeException e) {
            drop(consumer, e);
        }
    }

    private static void call(Consumer consumer, java.util.function.Consumer<Consumer> action) {
        try {
            action.accept(consumer);
        } catch (RuntimeException e) {
            drop(consumer, e);
        }
    }

    private static void drop(Consumer consumer, RuntimeException e) {
        LOGGER.error("[Anubis] Chunk scan consumer {} failed; disabling it", consumer.getClass().getSimpleName(), e);
        consumers.remove(consumer);
        snapshot = (Consumer[]) consumers.toArray(NONE);
    }
}
