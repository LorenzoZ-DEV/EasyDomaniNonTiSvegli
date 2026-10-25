package net.anubis.client.esp;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.LongPredicate;
import java.util.function.Supplier;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.module.impl.client.PerformanceModule;
import net.anubis.client.util.BlockBoxHelper;
import net.anubis.client.util.BlockClusterGeometry;
import net.anubis.client.util.TracerEngine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_4604;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/esp/BlockLayer.class */
@Environment(EnvType.CLIENT)
public final class BlockLayer {
    private static final long REVEAL_NANOS = 380000000;
    private static final double FADE_START = 64.0d;
    private static final double FADE_END = 256.0d;
    private static final float FADE_FLOOR = 0.45f;
    private final Palette palette;
    private final Supplier<Object> dimension;
    private final Map<Object, World> worlds = new HashMap();

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/esp/BlockLayer$Palette.class */
    @Environment(EnvType.CLIENT)
    public interface Palette {
        int color(Object obj);

        int tracerColor(Object obj);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/esp/BlockLayer$Vein.class */
    @Environment(EnvType.CLIENT)
    public static final class Vein extends Record {
        private final Object kind;
        private final class_243 center;

        public Vein(Object kind, class_243 center) {
            this.kind = kind;
            this.center = center;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Vein.class), Vein.class, "kind;center", "FIELD:Lnet/anubis/client/esp/BlockLayer$Vein;->kind:Ljava/lang/Object;", "FIELD:Lnet/anubis/client/esp/BlockLayer$Vein;->center:Lnet/minecraft/class_243;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Vein.class), Vein.class, "kind;center", "FIELD:Lnet/anubis/client/esp/BlockLayer$Vein;->kind:Ljava/lang/Object;", "FIELD:Lnet/anubis/client/esp/BlockLayer$Vein;->center:Lnet/minecraft/class_243;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Vein.class, Object.class), Vein.class, "kind;center", "FIELD:Lnet/anubis/client/esp/BlockLayer$Vein;->kind:Ljava/lang/Object;", "FIELD:Lnet/anubis/client/esp/BlockLayer$Vein;->center:Lnet/minecraft/class_243;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public Object kind() {
            return this.kind;
        }

        public class_243 center() {
            return this.center;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/esp/BlockLayer$Found.class */
    @Environment(EnvType.CLIENT)
    private static final class Found extends Record {
        private final Object kind;
        private final long bornAt;

        private Found(Object kind, long bornAt) {
            this.kind = kind;
            this.bornAt = bornAt;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Found.class), Found.class, "kind;bornAt", "FIELD:Lnet/anubis/client/esp/BlockLayer$Found;->kind:Ljava/lang/Object;", "FIELD:Lnet/anubis/client/esp/BlockLayer$Found;->bornAt:J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Found.class), Found.class, "kind;bornAt", "FIELD:Lnet/anubis/client/esp/BlockLayer$Found;->kind:Ljava/lang/Object;", "FIELD:Lnet/anubis/client/esp/BlockLayer$Found;->bornAt:J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Found.class, Object.class), Found.class, "kind;bornAt", "FIELD:Lnet/anubis/client/esp/BlockLayer$Found;->kind:Ljava/lang/Object;", "FIELD:Lnet/anubis/client/esp/BlockLayer$Found;->bornAt:J").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public Object kind() {
            return this.kind;
        }

        public long bornAt() {
            return this.bornAt;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/esp/BlockLayer$Cluster.class */
    @Environment(EnvType.CLIENT)
    private static final class Cluster extends Record {
        private final Object kind;
        private final long[] positions;
        private final class_243 center;
        private final long bornAt;
        private final BlockClusterGeometry.Mesh mesh;
        private final long[] links;

        private Cluster(Object kind, long[] positions, class_243 center, long bornAt, BlockClusterGeometry.Mesh mesh, long[] links) {
            this.kind = kind;
            this.positions = positions;
            this.center = center;
            this.bornAt = bornAt;
            this.mesh = mesh;
            this.links = links;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Cluster.class), Cluster.class, "kind;positions;center;bornAt;mesh;links", "FIELD:Lnet/anubis/client/esp/BlockLayer$Cluster;->kind:Ljava/lang/Object;", "FIELD:Lnet/anubis/client/esp/BlockLayer$Cluster;->positions:[J", "FIELD:Lnet/anubis/client/esp/BlockLayer$Cluster;->center:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/esp/BlockLayer$Cluster;->bornAt:J", "FIELD:Lnet/anubis/client/esp/BlockLayer$Cluster;->mesh:Lnet/anubis/client/util/BlockClusterGeometry$Mesh;", "FIELD:Lnet/anubis/client/esp/BlockLayer$Cluster;->links:[J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Cluster.class), Cluster.class, "kind;positions;center;bornAt;mesh;links", "FIELD:Lnet/anubis/client/esp/BlockLayer$Cluster;->kind:Ljava/lang/Object;", "FIELD:Lnet/anubis/client/esp/BlockLayer$Cluster;->positions:[J", "FIELD:Lnet/anubis/client/esp/BlockLayer$Cluster;->center:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/esp/BlockLayer$Cluster;->bornAt:J", "FIELD:Lnet/anubis/client/esp/BlockLayer$Cluster;->mesh:Lnet/anubis/client/util/BlockClusterGeometry$Mesh;", "FIELD:Lnet/anubis/client/esp/BlockLayer$Cluster;->links:[J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Cluster.class, Object.class), Cluster.class, "kind;positions;center;bornAt;mesh;links", "FIELD:Lnet/anubis/client/esp/BlockLayer$Cluster;->kind:Ljava/lang/Object;", "FIELD:Lnet/anubis/client/esp/BlockLayer$Cluster;->positions:[J", "FIELD:Lnet/anubis/client/esp/BlockLayer$Cluster;->center:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/esp/BlockLayer$Cluster;->bornAt:J", "FIELD:Lnet/anubis/client/esp/BlockLayer$Cluster;->mesh:Lnet/anubis/client/util/BlockClusterGeometry$Mesh;", "FIELD:Lnet/anubis/client/esp/BlockLayer$Cluster;->links:[J").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public Object kind() {
            return this.kind;
        }

        public long[] positions() {
            return this.positions;
        }

        public class_243 center() {
            return this.center;
        }

        public long bornAt() {
            return this.bornAt;
        }

        public BlockClusterGeometry.Mesh mesh() {
            return this.mesh;
        }

        public long[] links() {
            return this.links;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/esp/BlockLayer$Shapes.class */
    @Environment(EnvType.CLIENT)
    private static final class Shapes extends Record {
        private final List<Cluster> clusters;
        private final class_238 bounds;

        private Shapes(List<Cluster> clusters, class_238 bounds) {
            this.clusters = clusters;
            this.bounds = bounds;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Shapes.class), Shapes.class, "clusters;bounds", "FIELD:Lnet/anubis/client/esp/BlockLayer$Shapes;->clusters:Ljava/util/List;", "FIELD:Lnet/anubis/client/esp/BlockLayer$Shapes;->bounds:Lnet/minecraft/class_238;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Shapes.class), Shapes.class, "clusters;bounds", "FIELD:Lnet/anubis/client/esp/BlockLayer$Shapes;->clusters:Ljava/util/List;", "FIELD:Lnet/anubis/client/esp/BlockLayer$Shapes;->bounds:Lnet/minecraft/class_238;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Shapes.class, Object.class), Shapes.class, "clusters;bounds", "FIELD:Lnet/anubis/client/esp/BlockLayer$Shapes;->clusters:Ljava/util/List;", "FIELD:Lnet/anubis/client/esp/BlockLayer$Shapes;->bounds:Lnet/minecraft/class_238;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public List<Cluster> clusters() {
            return this.clusters;
        }

        public class_238 bounds() {
            return this.bounds;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/esp/BlockLayer$World.class */
    @Environment(EnvType.CLIENT)
    private static final class World {
        final Long2ObjectOpenHashMap<Found> positions = new Long2ObjectOpenHashMap<>();
        final Long2ObjectOpenHashMap<LongOpenHashSet> byChunk = new Long2ObjectOpenHashMap<>();
        final Long2ObjectOpenHashMap<Shapes> shapes = new Long2ObjectOpenHashMap<>();
        final LongOpenHashSet dirty = new LongOpenHashSet();
        List<Vein> veins = List.of();
        boolean veinsDirty;

        private World() {
        }
    }

    public BlockLayer(Palette palette, Supplier<Object> dimension) {
        this.palette = palette;
        this.dimension = dimension;
    }

    public void put(long pos, Object kind) {
        World world = world(true);
        if (world == null) {
            return;
        }
        Found existing = (Found) world.positions.get(pos);
        if (existing == null || !Objects.equals(existing.kind(), kind)) {
            world.positions.put(pos, new Found(kind, System.nanoTime()));
            ((LongOpenHashSet) world.byChunk.computeIfAbsent(chunkOf(pos), k -> {
                return new LongOpenHashSet();
            })).add(pos);
            markDirty(world, pos);
        }
    }

    public void remove(long pos) {
        World world = world(false);
        if (world == null || world.positions.remove(pos) == null) {
            return;
        }
        long chunk = chunkOf(pos);
        LongOpenHashSet set = (LongOpenHashSet) world.byChunk.get(chunk);
        if (set != null && set.remove(pos) && set.isEmpty()) {
            world.byChunk.remove(chunk);
        }
        markDirty(world, pos);
    }

    public void clear() {
        this.worlds.clear();
    }

    public void retainChunk(long chunkKey, LongPredicate keep) {
        LongOpenHashSet set;
        World world = world(false);
        if (world == null || (set = (LongOpenHashSet) world.byChunk.get(chunkKey)) == null) {
            return;
        }
        for (long pos : set.toLongArray()) {
            if (!keep.test(pos)) {
                remove(pos);
            }
        }
    }

    public List<Vein> veins() {
        World world = world(false);
        if (world == null) {
            return List.of();
        }
        rebuildDirty(world);
        if (world.veinsDirty) {
            world.veins = joinVeins(world);
            world.veinsDirty = false;
        }
        return world.veins;
    }

    private World world(boolean create) {
        Object key = this.dimension.get();
        if (key == null) {
            return null;
        }
        return create ? this.worlds.computeIfAbsent(key, k -> {
            return new World();
        }) : this.worlds.get(key);
    }

    private static long chunkKey(int cx, int cz) {
        return (((long) cx) & 4294967295L) | (((long) cz) << 32);
    }

    private static long chunkOf(long pos) {
        return chunkKey(class_2338.method_10061(pos) >> 4, class_2338.method_10083(pos) >> 4);
    }

    private static void markDirty(World world, long pos) {
        int x = class_2338.method_10061(pos);
        int z = class_2338.method_10083(pos);
        int cx = x >> 4;
        int cz = z >> 4;
        world.dirty.add(chunkKey(cx, cz));
        if ((x & 15) == 0) {
            world.dirty.add(chunkKey(cx - 1, cz));
        }
        if ((x & 15) == 15) {
            world.dirty.add(chunkKey(cx + 1, cz));
        }
        if ((z & 15) == 0) {
            world.dirty.add(chunkKey(cx, cz - 1));
        }
        if ((z & 15) == 15) {
            world.dirty.add(chunkKey(cx, cz + 1));
        }
    }

    private void rebuildDirty(World world) {
        if (world.dirty.isEmpty()) {
            return;
        }
        LongIterator it = world.dirty.iterator();
        while (it.hasNext()) {
            long chunk = ((Long) it.next()).longValue();
            rebuildChunk(world, chunk);
        }
        world.dirty.clear();
        world.veinsDirty = true;
    }

    private void rebuildChunk(World world, long chunkKey) {
        LongOpenHashSet set = (LongOpenHashSet) world.byChunk.get(chunkKey);
        if (set == null) {
            world.shapes.remove(chunkKey);
            return;
        }
        Map<Object, List<class_2338>> byKind = new LinkedHashMap<>();
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;
        LongIterator it = set.iterator();
        while (it.hasNext()) {
            long pos = ((Long) it.next()).longValue();
            class_2338 block = class_2338.method_10092(pos);
            byKind.computeIfAbsent(((Found) world.positions.get(pos)).kind(), k -> {
                return new ArrayList();
            }).add(block);
            minX = Math.min(minX, block.method_10263());
            maxX = Math.max(maxX, block.method_10263());
            minY = Math.min(minY, block.method_10264());
            maxY = Math.max(maxY, block.method_10264());
            minZ = Math.min(minZ, block.method_10260());
            maxZ = Math.max(maxZ, block.method_10260());
        }
        int cx = (int) chunkKey;
        int cz = (int) (chunkKey >> 32);
        List<Cluster> clusters = new ArrayList<>();
        for (Map.Entry<Object, List<class_2338>> entry : byKind.entrySet()) {
            Object kind = entry.getKey();
            BlockClusterGeometry.Membership same = (x, y, z) -> {
                Found found = (Found) world.positions.get(class_2338.method_10064(x, y, z));
                return found != null && Objects.equals(found.kind(), kind);
            };
            for (List<class_2338> component : BlockClusterGeometry.connectedComponents(entry.getValue())) {
                clusters.add(buildCluster(world, kind, component, same, cx, cz));
            }
        }
        world.shapes.put(chunkKey, new Shapes(clusters, new class_238(minX - 1, minY - 1, minZ - 1, maxX + 2, maxY + 2, maxZ + 2)));
    }

    private static Cluster buildCluster(World world, Object kind, List<class_2338> component, BlockClusterGeometry.Membership same, int cx, int cz) {
        long[] positions = new long[component.size()];
        List<Long> links = new ArrayList<>();
        double sx = 0.0d;
        double sy = 0.0d;
        double sz = 0.0d;
        long bornAt = Long.MAX_VALUE;
        for (int i = 0; i < positions.length; i++) {
            class_2338 pos = component.get(i);
            positions[i] = pos.method_10063();
            sx += ((double) pos.method_10263()) + 0.5d;
            sy += ((double) pos.method_10264()) + 0.5d;
            sz += ((double) pos.method_10260()) + 0.5d;
            bornAt = Math.min(bornAt, ((Found) world.positions.get(positions[i])).bornAt());
            for (class_2338 next : new class_2338[]{pos.method_10078(), pos.method_10067(), pos.method_10072(), pos.method_10095()}) {
                if (((next.method_10263() >> 4) != cx || (next.method_10260() >> 4) != cz) && same.contains(next.method_10263(), next.method_10264(), next.method_10260())) {
                    links.add(Long.valueOf(next.method_10063()));
                }
            }
        }
        int n = positions.length;
        return new Cluster(kind, positions, new class_243(sx / ((double) n), sy / ((double) n), sz / ((double) n)), bornAt, BlockClusterGeometry.build(component, same), links.stream().mapToLong((v0) -> {
            return v0.longValue();
        }).toArray());
    }

    private static List<Vein> joinVeins(World world) {
        List<Cluster> all = new ArrayList<>();
        Long2ObjectOpenHashMap<Cluster> owner = new Long2ObjectOpenHashMap<>();
        ObjectIterator it = world.shapes.values().iterator();
        while (it.hasNext()) {
            Shapes shapes = (Shapes) it.next();
            for (Cluster cluster : shapes.clusters()) {
                all.add(cluster);
                if (cluster.links().length != 0) {
                    for (long pos : cluster.positions()) {
                        int lx = class_2338.method_10061(pos) & 15;
                        int lz = class_2338.method_10083(pos) & 15;
                        if (lx == 0 || lx == 15 || lz == 0 || lz == 15) {
                            owner.put(pos, cluster);
                        }
                    }
                }
            }
        }
        Map<Cluster, Cluster> parent = new IdentityHashMap<>();
        for (Cluster cluster2 : all) {
            parent.put(cluster2, cluster2);
        }
        for (Cluster cluster3 : all) {
            for (long link : cluster3.links()) {
                Cluster other = (Cluster) owner.get(link);
                if (other != null) {
                    parent.put(find(parent, cluster3), find(parent, other));
                }
            }
        }
        Map<Cluster, double[]> sums = new IdentityHashMap<>();
        for (Cluster cluster4 : all) {
            double[] sum = sums.computeIfAbsent(find(parent, cluster4), r -> {
                return new double[4];
            });
            int n = cluster4.positions().length;
            sum[0] = sum[0] + (cluster4.center().field_1352 * ((double) n));
            sum[1] = sum[1] + (cluster4.center().field_1351 * ((double) n));
            sum[2] = sum[2] + (cluster4.center().field_1350 * ((double) n));
            sum[3] = sum[3] + ((double) n);
        }
        List<Vein> veins = new ArrayList<>(sums.size());
        for (Map.Entry<Cluster, double[]> entry : sums.entrySet()) {
            double[] s = entry.getValue();
            veins.add(new Vein(entry.getKey().kind(), new class_243(s[0] / s[3], s[1] / s[3], s[2] / s[3])));
        }
        return veins;
    }

    private static Cluster find(Map<Cluster, Cluster> parent, Cluster cluster) {
        Cluster root;
        Cluster cluster2 = cluster;
        while (true) {
            root = cluster2;
            if (parent.get(root) == root) {
                break;
            }
            cluster2 = parent.get(root);
        }
        while (parent.get(cluster) != root) {
            Cluster next = parent.get(cluster);
            parent.put(cluster, root);
            cluster = next;
        }
        return root;
    }

    public void render(Render3DEvent event, EspStyle style) {
        class_310 mc = class_310.method_1551();
        World world = world(false);
        if (mc.field_1724 == null || world == null) {
            return;
        }
        rebuildDirty(world);
        class_4604 frustum = PerformanceModule.frustumCulling() ? event.frustum : null;
        long now = System.nanoTime();
        class_243 eye = mc.field_1724.method_5836(event.partialTick);
        boolean outline = style.outline();
        boolean glow = style.glow.get();
        boolean fade = style.fade.get();
        boolean reveal = style.reveal.get();
        float baseWidth = style.lineWidth.get();
        float fillAlpha = style.fillAlpha.get();
        float boxSize = style.boxSize.get();
        double range = style.range.get();
        ObjectIterator it = world.shapes.values().iterator();
        while (it.hasNext()) {
            Shapes shapes = (Shapes) it.next();
            if (frustum == null || frustum.method_23093(shapes.bounds())) {
                for (Cluster cluster : shapes.clusters()) {
                    double dist = eye.method_1022(cluster.center());
                    if (dist <= range) {
                        float alpha = fade ? distanceFade(dist) : 1.0f;
                        float t = reveal ? Math.min(1.0f, (now - cluster.bornAt()) / 3.8E8f) : 1.0f;
                        float alpha2 = alpha * easeOutCubic(t);
                        if (alpha2 >= 0.02f) {
                            float scale = t < 1.0f ? 0.55f + (FADE_FLOOR * easeOutBack(t)) : 1.0f;
                            float width = fade ? baseWidth * (1.0f - (0.3f * ((float) Math.min(1.0d, dist / FADE_END)))) : baseWidth;
                            int color = this.palette.color(cluster.kind());
                            int stroke = scaleAlpha(color, alpha2);
                            int fill = fillAlpha > 0.0f ? withAlpha(color, fillAlpha * alpha2) : 0;
                            if (outline) {
                                drawOutline(cluster, stroke, fill, width, glow, scale);
                            } else {
                                drawBoxes(cluster, stroke, fill, width, boxSize * scale);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void drawOutline(Cluster cluster, int stroke, int fill, float width, boolean glow, float scale) {
        class_243 c = cluster.center();
        BlockClusterGeometry.Mesh mesh = cluster.mesh();
        if ((fill >>> 24) > 0) {
            class_12179 style = class_12179.method_75539(fill);
            for (BlockClusterGeometry.Face face : mesh.faces()) {
                class_12180.method_75547(grow(face.a(), c, scale), grow(face.b(), c, scale), grow(face.c(), c, scale), grow(face.d(), c, scale), style).method_75533();
            }
        }
        int halo = glow ? scaleAlpha(stroke, 0.18f) : 0;
        for (BlockClusterGeometry.Segment edge : mesh.edges()) {
            class_243 a = grow(edge.a(), c, scale);
            class_243 b = grow(edge.b(), c, scale);
            if (glow) {
                class_12180.method_75546(a, b, halo, width * 3.0f).method_75533();
            }
            class_12180.method_75546(a, b, stroke, width).method_75533();
        }
    }

    private static void drawBoxes(Cluster cluster, int stroke, int fill, float width, float size) {
        class_12179 class_12179VarMethod_75536;
        if ((fill >>> 24) > 0) {
            class_12179VarMethod_75536 = class_12179.method_75537(stroke, width, fill);
        } else {
            class_12179VarMethod_75536 = class_12179.method_75536(stroke, width);
        }
        class_12179 style = class_12179VarMethod_75536;
        for (long pos : cluster.positions()) {
            class_12180.method_75541(BlockBoxHelper.scaledBlockBox(class_2338.method_10092(pos), size), style).method_75533();
        }
    }

    private static class_243 grow(class_243 p, class_243 c, float scale) {
        return scale == 1.0f ? p : c.method_1019(p.method_1020(c).method_1021(scale));
    }

    private static float distanceFade(double dist) {
        if (dist <= FADE_START) {
            return 1.0f;
        }
        float t = (float) Math.min(1.0d, (dist - FADE_START) / 192.0d);
        return 1.0f - ((0.55f * t) * t);
    }

    public void renderTracers(EspStyle style) {
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null) {
            return;
        }
        class_243 eye = mc.field_1724.method_5836(1.0f);
        double rangeSq = ((double) style.range.get()) * ((double) style.range.get());
        for (Vein vein : veins()) {
            if (eye.method_1025(vein.center()) <= rangeSq) {
                int color = this.palette.tracerColor(vein.kind());
                if ((color >>> 24) != 0) {
                    TracerEngine.draw(vein.center(), color);
                }
            }
        }
    }

    private static int withAlpha(int argb, float alpha) {
        return (Math.round(255.0f * Math.max(0.0f, Math.min(1.0f, alpha))) << 24) | (argb & 16777215);
    }

    private static int scaleAlpha(int argb, float mul) {
        int alpha = Math.round((argb >>> 24) * Math.max(0.0f, Math.min(1.0f, mul)));
        return (alpha << 24) | (argb & 16777215);
    }

    private static float easeOutCubic(float t) {
        float inv = 1.0f - t;
        return 1.0f - ((inv * inv) * inv);
    }

    private static float easeOutBack(float t) {
        float c3 = 1.70158f + 1.0f;
        float u = t - 1.0f;
        return 1.0f + (c3 * u * u * u) + (1.70158f * u * u);
    }
}
