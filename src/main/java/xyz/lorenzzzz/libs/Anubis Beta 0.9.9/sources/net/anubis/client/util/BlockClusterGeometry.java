package net.anubis.client.util;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2338;
import net.minecraft.class_243;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/BlockClusterGeometry.class */
@Environment(EnvType.CLIENT)
public final class BlockClusterGeometry {
    private static final int[][] AXES = {new int[]{1, 0, 0}, new int[]{0, 1, 0}, new int[]{0, 0, 1}};

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/BlockClusterGeometry$Membership.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface Membership {
        boolean contains(int i, int i2, int i3);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/BlockClusterGeometry$Segment.class */
    @Environment(EnvType.CLIENT)
    public static final class Segment extends Record {
        private final class_243 a;
        private final class_243 b;

        public Segment(class_243 a, class_243 b) {
            this.a = a;
            this.b = b;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Segment.class), Segment.class, "a;b", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Segment;->a:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Segment;->b:Lnet/minecraft/class_243;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Segment.class), Segment.class, "a;b", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Segment;->a:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Segment;->b:Lnet/minecraft/class_243;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Segment.class, Object.class), Segment.class, "a;b", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Segment;->a:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Segment;->b:Lnet/minecraft/class_243;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_243 a() {
            return this.a;
        }

        public class_243 b() {
            return this.b;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/BlockClusterGeometry$Face.class */
    @Environment(EnvType.CLIENT)
    public static final class Face extends Record {
        private final class_243 a;
        private final class_243 b;
        private final class_243 c;
        private final class_243 d;

        public Face(class_243 a, class_243 b, class_243 c, class_243 d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Face.class), Face.class, "a;b;c;d", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Face;->a:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Face;->b:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Face;->c:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Face;->d:Lnet/minecraft/class_243;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Face.class), Face.class, "a;b;c;d", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Face;->a:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Face;->b:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Face;->c:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Face;->d:Lnet/minecraft/class_243;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Face.class, Object.class), Face.class, "a;b;c;d", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Face;->a:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Face;->b:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Face;->c:Lnet/minecraft/class_243;", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Face;->d:Lnet/minecraft/class_243;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_243 a() {
            return this.a;
        }

        public class_243 b() {
            return this.b;
        }

        public class_243 c() {
            return this.c;
        }

        public class_243 d() {
            return this.d;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/BlockClusterGeometry$Mesh.class */
    @Environment(EnvType.CLIENT)
    public static final class Mesh extends Record {
        private final List<Segment> edges;
        private final List<Face> faces;

        public Mesh(List<Segment> edges, List<Face> faces) {
            this.edges = edges;
            this.faces = faces;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Mesh.class), Mesh.class, "edges;faces", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Mesh;->edges:Ljava/util/List;", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Mesh;->faces:Ljava/util/List;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Mesh.class), Mesh.class, "edges;faces", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Mesh;->edges:Ljava/util/List;", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Mesh;->faces:Ljava/util/List;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Mesh.class, Object.class), Mesh.class, "edges;faces", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Mesh;->edges:Ljava/util/List;", "FIELD:Lnet/anubis/client/util/BlockClusterGeometry$Mesh;->faces:Ljava/util/List;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public List<Segment> edges() {
            return this.edges;
        }

        public List<Face> faces() {
            return this.faces;
        }
    }

    private BlockClusterGeometry() {
    }

    public static Mesh build(Collection<class_2338> blocks, Membership same) {
        List<int[]> unitEdges = new ArrayList<>(blocks.size() * 4);
        List<Face> faces = new ArrayList<>(blocks.size() * 3);
        for (class_2338 pos : blocks) {
            int[] p = {pos.method_10263(), pos.method_10264(), pos.method_10260()};
            addFaces(p, same, faces);
            addEdges(p, same, unitEdges);
        }
        return new Mesh(mergeCollinear(unitEdges), faces);
    }

    private static List<Segment> mergeCollinear(List<int[]> unitEdges) {
        Map<List<Integer>, List<Integer>> lines = new HashMap<>();
        for (int[] e : unitEdges) {
            int axis = e[0];
            lines.computeIfAbsent(List.of(Integer.valueOf(axis), Integer.valueOf(e[1 + ((axis + 1) % 3)]), Integer.valueOf(e[1 + ((axis + 2) % 3)])), k -> {
                return new ArrayList();
            }).add(Integer.valueOf(e[1 + axis]));
        }
        List<Segment> merged = new ArrayList<>(unitEdges.size());
        for (Map.Entry<List<Integer>, List<Integer>> line : lines.entrySet()) {
            int axis2 = line.getKey().get(0).intValue();
            int u = (axis2 + 1) % 3;
            int v = (axis2 + 2) % 3;
            List<Integer> starts = line.getValue();
            starts.sort(null);
            int runStart = starts.get(0).intValue();
            int runEnd = runStart + 1;
            for (int i = 1; i <= starts.size(); i++) {
                if (i < starts.size() && starts.get(i).intValue() == runEnd) {
                    runEnd++;
                } else {
                    int[] a = new int[3];
                    int[] b = new int[3];
                    int iIntValue = line.getKey().get(1).intValue();
                    b[u] = iIntValue;
                    a[u] = iIntValue;
                    int iIntValue2 = line.getKey().get(2).intValue();
                    b[v] = iIntValue2;
                    a[v] = iIntValue2;
                    a[axis2] = runStart;
                    b[axis2] = runEnd;
                    merged.add(new Segment(vec(a), vec(b)));
                    if (i < starts.size()) {
                        runStart = starts.get(i).intValue();
                        runEnd = runStart + 1;
                    }
                }
            }
        }
        return merged;
    }

    public static List<List<class_2338>> connectedComponents(Collection<class_2338> blocks) {
        Set<class_2338> remaining = new HashSet<>((Collection<? extends class_2338>) blocks);
        List<List<class_2338>> components = new ArrayList<>();
        ArrayDeque<class_2338> queue = new ArrayDeque<>();
        while (!remaining.isEmpty()) {
            class_2338 seed = remaining.iterator().next();
            remaining.remove(seed);
            List<class_2338> component = new ArrayList<>();
            queue.add(seed);
            while (!queue.isEmpty()) {
                class_2338 current = queue.poll();
                component.add(current);
                for (int[] axis : AXES) {
                    for (int sign = -1; sign <= 1; sign += 2) {
                        class_2338 next = current.method_10069(axis[0] * sign, axis[1] * sign, axis[2] * sign);
                        if (remaining.remove(next)) {
                            queue.add(next);
                        }
                    }
                }
            }
            components.add(component);
        }
        return components;
    }

    private static void addFaces(int[] p, Membership same, List<Face> out) {
        for (int axis = 0; axis < 3; axis++) {
            int u = (axis + 1) % 3;
            int v = (axis + 2) % 3;
            for (int sign = -1; sign <= 1; sign += 2) {
                if (!same.contains(p[0] + (AXES[axis][0] * sign), p[1] + (AXES[axis][1] * sign), p[2] + (AXES[axis][2] * sign))) {
                    int[] base = (int[]) p.clone();
                    if (sign > 0) {
                        int i = axis;
                        base[i] = base[i] + 1;
                    }
                    out.add(new Face(corner(base, u, 0, v, 0), corner(base, u, 1, v, 0), corner(base, u, 1, v, 1), corner(base, u, 0, v, 1)));
                }
            }
        }
    }

    private static void addEdges(int[] p, Membership same, List<int[]> out) {
        for (int axis = 0; axis < 3; axis++) {
            int u = (axis + 1) % 3;
            int v = (axis + 2) % 3;
            for (int su = -1; su <= 1; su += 2) {
                for (int sv = -1; sv <= 1; sv += 2) {
                    boolean nu = contains(same, p, u, su, -1, 0);
                    boolean nv = contains(same, p, v, sv, -1, 0);
                    boolean diagonal = contains(same, p, u, su, v, sv);
                    boolean convex = (nu || nv) ? false : true;
                    boolean concave = nu && nv && !diagonal;
                    if (convex || concave) {
                        int[] start = (int[]) p.clone();
                        if (su > 0) {
                            start[u] = start[u] + 1;
                        }
                        if (sv > 0) {
                            start[v] = start[v] + 1;
                        }
                        out.add(new int[]{axis, start[0], start[1], start[2]});
                    }
                }
            }
        }
    }

    private static boolean contains(Membership same, int[] p, int axisA, int signA, int axisB, int signB) {
        int[] q = (int[]) p.clone();
        q[axisA] = q[axisA] + signA;
        if (axisB >= 0) {
            q[axisB] = q[axisB] + signB;
        }
        return same.contains(q[0], q[1], q[2]);
    }

    private static class_243 corner(int[] base, int u, int du, int v, int dv) {
        int[] q = (int[]) base.clone();
        q[u] = q[u] + du;
        q[v] = q[v] + dv;
        return vec(q);
    }

    private static class_243 vec(int[] p) {
        return new class_243(p[0], p[1], p[2]);
    }
}
