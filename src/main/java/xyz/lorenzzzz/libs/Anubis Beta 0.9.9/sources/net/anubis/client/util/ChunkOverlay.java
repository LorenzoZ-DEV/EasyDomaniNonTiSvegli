package net.anubis.client.util;

import it.unimi.dsi.fastutil.longs.Long2FloatMap;
import it.unimi.dsi.fastutil.longs.Long2FloatOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_12175;
import net.minecraft.class_12177;
import net.minecraft.class_12180;
import net.minecraft.class_1923;
import net.minecraft.class_243;
import net.minecraft.class_9848;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/ChunkOverlay.class */
@Environment(EnvType.CLIENT)
public final class ChunkOverlay {
    public static final double MARKER_Y = 63.0d;
    private static final float EDGE_SHADE = 0.77f;
    private static final float LINE_WIDTH = 1.5f;
    private static final double CORNER_RADIUS = 2.0d;
    private static final int ARC_SEGMENTS = 4;
    private static final float GROW_SECONDS = 0.3f;
    private static final float SHRINK_SECONDS = 0.25f;
    private static final float SETTLE_SECONDS = 0.25f;
    private static final float MAX_WAIT_SECONDS = 1.0f;
    private static final float RESET_GAP_SECONDS = 0.5f;
    private final Long2FloatOpenHashMap shown = new Long2FloatOpenHashMap();
    private final LongOpenHashSet pending = new LongOpenHashSet();
    private final LongOpenHashSet done = new LongOpenHashSet();
    private final LongArrayFIFOQueue queue = new LongArrayFIFOQueue();
    private final LongArrayList group = new LongArrayList();
    private long lastNanos;
    private long pendingSince;
    private long lastAdded;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/ChunkOverlay$Plane.class */
    @Environment(EnvType.CLIENT)
    private static final class Plane extends Record implements class_12175 {
        private final class_243[] outline;
        private final int color;

        private Plane(class_243[] outline, int color) {
            this.outline = outline;
            this.color = color;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Plane.class), Plane.class, "outline;color", "FIELD:Lnet/anubis/client/util/ChunkOverlay$Plane;->outline:[Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/util/ChunkOverlay$Plane;->color:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Plane.class), Plane.class, "outline;color", "FIELD:Lnet/anubis/client/util/ChunkOverlay$Plane;->outline:[Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/util/ChunkOverlay$Plane;->color:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Plane.class, Object.class), Plane.class, "outline;color", "FIELD:Lnet/anubis/client/util/ChunkOverlay$Plane;->outline:[Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/util/ChunkOverlay$Plane;->color:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_243[] outline() {
            return this.outline;
        }

        public int color() {
            return this.color;
        }

        public void method_75531(class_12177 primitives, float alpha) {
            primitives.method_75478(this.outline, ChunkOverlay.fade(this.color, alpha));
        }
    }

    public void draw(LongSet chunks, int fill, float partialTick) {
        long now = System.nanoTime();
        float dt = this.lastNanos == 0 ? 0.0f : (now - this.lastNanos) / 1.0E9f;
        this.lastNanos = now;
        if (dt > 0.5f) {
            this.shown.clear();
            this.pending.clear();
        }
        collect(chunks, now);
        step(chunks, dt);
        if (this.shown.isEmpty()) {
            return;
        }
        int edge = class_9848.method_61330(Math.min(255, (class_9848.method_61320(fill) * 3) / 2), class_9848.method_61321(fill, EDGE_SHADE));
        FoglessLines lines = FoglessLines.forFrame(partialTick);
        this.done.clear();
        LongIterator keys = this.shown.keySet().iterator();
        while (keys.hasNext()) {
            long start = keys.nextLong();
            if (this.done.add(start)) {
                drawGroup(lines, chunks, start, fill, edge);
            }
        }
    }

    private void collect(LongSet chunks, long now) {
        LongIterator keys = chunks.iterator();
        while (keys.hasNext()) {
            long key = keys.nextLong();
            if (!this.shown.containsKey(key) && !this.pending.contains(key)) {
                if (this.pending.isEmpty()) {
                    this.pendingSince = now;
                }
                this.pending.add(key);
                this.lastAdded = now;
            }
        }
        LongIterator keys2 = this.pending.iterator();
        while (keys2.hasNext()) {
            if (!chunks.contains(keys2.nextLong())) {
                keys2.remove();
            }
        }
        if (this.pending.isEmpty()) {
            return;
        }
        if ((now - this.lastAdded) / 1.0E9f >= 0.25f || (now - this.pendingSince) / 1.0E9f >= 1.0f) {
            LongIterator keys3 = this.pending.iterator();
            while (keys3.hasNext()) {
                this.shown.put(keys3.nextLong(), 0.0f);
            }
            this.pending.clear();
        }
    }

    private void step(LongSet chunks, float dt) {
        ObjectIterator<Long2FloatMap.Entry> entries = this.shown.long2FloatEntrySet().fastIterator();
        while (entries.hasNext()) {
            Long2FloatMap.Entry entry = (Long2FloatMap.Entry) entries.next();
            if (chunks.contains(entry.getLongKey())) {
                entry.setValue(Math.min(1.0f, entry.getFloatValue() + (dt / GROW_SECONDS)));
            } else {
                float left = entry.getFloatValue() - (dt / 0.25f);
                if (left <= 0.0f) {
                    entries.remove();
                } else {
                    entry.setValue(left);
                }
            }
        }
    }

    private void drawGroup(FoglessLines lines, LongSet chunks, long start, int fill, int edge) {
        float progress = this.shown.get(start);
        boolean present = chunks.contains(start);
        this.group.clear();
        this.queue.enqueue(start);
        double sumX = 0.0d;
        double sumZ = 0.0d;
        while (!this.queue.isEmpty()) {
            long key = this.queue.dequeueLong();
            this.group.add(key);
            int cx = class_1923.method_8325(key);
            int cz = class_1923.method_8332(key);
            sumX += ((double) (cx << 4)) + 8.0d;
            sumZ += ((double) (cz << 4)) + 8.0d;
            visit(chunks, class_1923.method_8331(cx, cz - 1), progress, present);
            visit(chunks, class_1923.method_8331(cx, cz + 1), progress, present);
            visit(chunks, class_1923.method_8331(cx - 1, cz), progress, present);
            visit(chunks, class_1923.method_8331(cx + 1, cz), progress, present);
        }
        float size = easeOut(progress);
        if (size <= 0.001f) {
            return;
        }
        double mx = sumX / ((double) this.group.size());
        double mz = sumZ / ((double) this.group.size());
        int groupFill = size >= 1.0f ? fill : fade(fill, size);
        int groupEdge = size >= 1.0f ? edge : fade(edge, size);
        for (int i = 0; i < this.group.size(); i++) {
            long key2 = this.group.getLong(i);
            int cx2 = class_1923.method_8325(key2);
            int cz2 = class_1923.method_8332(key2);
            double x0 = mx + ((((double) (cx2 << 4)) - mx) * ((double) size));
            double z0 = mz + ((((double) (cz2 << 4)) - mz) * ((double) size));
            double x1 = x0 + (16.0d * ((double) size));
            double z1 = z0 + (16.0d * ((double) size));
            tile(lines, x0, z0, x1, z1, CORNER_RADIUS * ((double) size), !joined(chunks, class_1923.method_8331(cx2, cz2 - 1), progress, present), !joined(chunks, class_1923.method_8331(cx2, cz2 + 1), progress, present), !joined(chunks, class_1923.method_8331(cx2 - 1, cz2), progress, present), !joined(chunks, class_1923.method_8331(cx2 + 1, cz2), progress, present), groupFill, groupEdge);
        }
    }

    private void visit(LongSet chunks, long key, float progress, boolean present) {
        if (!joined(chunks, key, progress, present) || !this.done.add(key)) {
            return;
        }
        this.queue.enqueue(key);
    }

    private boolean joined(LongSet chunks, long key, float progress, boolean present) {
        return this.shown.containsKey(key) && this.shown.get(key) == progress && chunks.contains(key) == present;
    }

    private static void tile(FoglessLines lines, double x0, double z0, double x1, double z1, double radius, boolean north, boolean south, boolean west, boolean east, int fill, int edge) {
        double nw = (north && west) ? radius : 0.0d;
        double ne = (north && east) ? radius : 0.0d;
        double se = (south && east) ? radius : 0.0d;
        double sw = (south && west) ? radius : 0.0d;
        List<class_243> outline = new ArrayList<>(20);
        corner(outline, lines, x0 + nw, z0 + nw, nw, 180.0d, edge);
        corner(outline, lines, x1 - ne, z0 + ne, ne, 270.0d, edge);
        corner(outline, lines, x1 - se, z1 - se, se, 0.0d, edge);
        corner(outline, lines, x0 + sw, z1 - sw, sw, 90.0d, edge);
        class_12180.method_75553(new Plane((class_243[]) outline.toArray(new class_243[0]), fill)).method_75533();
        if (north) {
            edge(lines, x0 + nw, z0, x1 - ne, z0, edge);
        }
        if (south) {
            edge(lines, x0 + sw, z1, x1 - se, z1, edge);
        }
        if (west) {
            edge(lines, x0, z0 + nw, x0, z1 - sw, edge);
        }
        if (east) {
            edge(lines, x1, z0 + ne, x1, z1 - se, edge);
        }
    }

    private static void corner(List<class_243> outline, FoglessLines lines, double cx, double cz, double radius, double startDegrees, int edge) {
        if (radius <= 0.0d) {
            outline.add(new class_243(cx, 63.0d, cz));
            return;
        }
        class_243 previous = null;
        for (int i = 0; i <= 4; i++) {
            double angle = Math.toRadians(startDegrees + ((90.0d * ((double) i)) / 4.0d));
            class_243 point = new class_243(cx + (Math.cos(angle) * radius), 63.0d, cz + (Math.sin(angle) * radius));
            outline.add(point);
            if (previous != null) {
                lines.line(previous, point, edge, LINE_WIDTH);
            }
            previous = point;
        }
    }

    private static float easeOut(float t) {
        float inv = 1.0f - t;
        return 1.0f - ((inv * inv) * inv);
    }

    private static int fade(int color, float amount) {
        return class_9848.method_61330(Math.round(class_9848.method_61320(color) * amount), color);
    }

    private static void edge(FoglessLines lines, double ax, double az, double bx, double bz, int color) {
        lines.line(new class_243(ax, 63.0d, az), new class_243(bx, 63.0d, bz), color, LINE_WIDTH);
    }
}
