package net.anubis.client.module.impl.render.character.kit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/CharacterBuilder.class */
@Environment(EnvType.CLIENT)
public final class CharacterBuilder {
    private static final float DECAL_RESERVE = 0.1f;
    private static final float AO_STEP = 0.38f;
    private static final int MIN_TRIANGLES = 24;
    private static final float MAX_EXTENT = 160.0f;
    private static final float OUTLINE_SHARE = 0.5f;
    private static final float BURIED = 0.02f;
    private static final float SINK = 0.12f;
    private static final int CORNER = 10;

    private CharacterBuilder() {
    }

    public static CharacterModel build(CharacterDesign design, Detail detail) {
        int i;
        if (design instanceof BakedDesign) {
            BakedDesign baked = (BakedDesign) design;
            return BakedModel.load(baked, detail);
        }
        long start = System.nanoTime();
        Rig rig = design.rig();
        List<Part> order = order(rig);
        int n = order.size();
        if (n == 0) {
            throw new IllegalStateException(design.id() + ": the design defines no parts");
        }
        Map<Part, Integer> index = new IdentityHashMap<>();
        for (int i2 = 0; i2 < n; i2++) {
            index.put(order.get(i2), Integer.valueOf(i2));
        }
        List<Shape> solids = new ArrayList<>();
        List<Canvas> canvases = new ArrayList<>();
        Shape[] shapes = new Shape[n];
        float[] steps = new float[n];
        for (int i3 = 0; i3 < n; i3++) {
            Part p = order.get(i3);
            if (p.shape != null) {
                Shape s = p.shape;
                if (!s.bounded()) {
                    throw new IllegalStateException(p.name + ": shape is unbounded; intersect planes with a box");
                }
                if (s.maxX - s.minX > MAX_EXTENT || s.maxY - s.minY > MAX_EXTENT || s.maxZ - s.minZ > MAX_EXTENT) {
                    throw new IllegalStateException(p.name + ": shape is larger than 160.0 units");
                }
                steps[i3] = SurfaceNets.fitStep(s.maxX - s.minX, s.maxY - s.minY, s.maxZ - s.minZ, detail.step * p.resolution);
                float half = steps[i3] * 0.75f;
                Shape s2 = s.map(x -> {
                    if (!(x instanceof Strand)) {
                        return x;
                    }
                    Strand strand = (Strand) x;
                    return strand.thickened(half);
                });
                shapes[i3] = s2;
                solids.add(s2);
            }
            for (Decal d : p.decals) {
                canvases.add(d.canvas);
            }
        }
        Shape world = solids.isEmpty() ? null : Shapes.union((Shape[]) solids.toArray(new Shape[0]));
        Shape[] covers = covers(order, shapes);
        Atlas atlas = Atlas.pack(canvases);
        float[] thickness = new float[n];
        for (int i4 = 0; i4 < n; i4++) {
            Part p2 = order.get(i4);
            thickness[i4] = p2.outline >= 0.0f ? p2.outline : rig.outline;
        }
        Mesh[] fine = new Mesh[n];
        float[] shown = new float[n];
        IntStream.range(0, n).parallel().forEach(i5 -> {
            Shape s3 = shapes[i5];
            if (s3 == null) {
                return;
            }
            fine[i5] = SurfaceNets.mesh(s3, s3.minX, s3.minY, s3.minZ, s3.maxX, s3.maxY, s3.maxZ, steps[i5]);
            shown[i5] = visible(fine[i5], covers[i5]);
        });
        int[] baseTarget = new int[n];
        budget(order, fine, shown, detail, baseTarget, thickness);
        Mesh[] base = new Mesh[n];
        IntStream.range(0, n).parallel().forEach(i6 -> {
            if (fine[i6] == null || fine[i6].triCount == 0) {
                return;
            }
            Simplifier s3 = new Simplifier(fine[i6]);
            s3.reduce(baseTarget[i6]);
            base[i6] = s3.snapshot();
        });
        PartMesh[] parts = new PartMesh[n];
        float[] extent = {Float.MAX_VALUE, -3.4028235E38f};
        int[] slots = new int[n];
        int slotCount = 0;
        for (int i7 = 0; i7 < n; i7++) {
            Part p3 = order.get(i7);
            int i8 = i7;
            if (!p3.extra || p3.motion == null) {
                i = -1;
            } else {
                i = slotCount;
                slotCount++;
            }
            slots[i8] = i;
        }
        IntStream.range(0, n).parallel().forEach(i9 -> {
            Part owner;
            Part p4 = (Part) order.get(i9);
            Surface baseSurface = null;
            Surface hullSurface = null;
            Surface decalSurface = null;
            if (base[i9] != null && base[i9].triCount > 0) {
                float strength = p4.occlusion >= 0.0f ? p4.occlusion : rig.occlusion;
                float flat = p4.flat >= 0.0f ? p4.flat : rig.flat;
                Shaded shaded = shade(shapes[i9], world, base[i9], steps[i9], strength, flat);
                Mesh kept = bury(shaded, base[i9], covers[i9], steps[i9]);
                baseSurface = baseSurface(shaded, kept, p4, atlas);
                if (baseSurface != null && thickness[i9] > 0.0f) {
                    int color = p4.outlineColorSet ? p4.outlineColor : rig.outlineColor;
                    hullSurface = hullSurface(shapes[i9], shaded, baseSurface, p4, thickness[i9], steps[i9], color, atlas);
                }
                decalSurface = p4.decals.isEmpty() ? null : decalSurface(p4, shaded, kept, atlas);
                synchronized (extent) {
                    for (int v = 0; v < shaded.count; v++) {
                        extent[0] = Math.min(extent[0], shaded.pos[(v * 3) + 1]);
                        extent[1] = Math.max(extent[1], shaded.pos[(v * 3) + 1]);
                    }
                }
            }
            int parent = p4.parent == null ? -1 : ((Integer) index.get(p4.parent)).intValue();
            Motion motion = p4.extra ? p4.motion : null;
            if (motion != null && motion.bendPower() > 0.0f) {
                float reach = reach(baseSurface);
                weigh(baseSurface, reach, motion.bendPower());
                if (hullSurface != null && baseSurface != null) {
                    hullSurface.weight = baseSurface.weight;
                }
                weigh(decalSurface, reach, motion.bendPower());
            }
            boolean limb = !p4.extra && (p4.bone.arm() || p4.bone.leg());
            Part part = p4;
            while (true) {
                owner = part;
                if (owner == null || owner.jointSet) {
                    break;
                } else {
                    part = owner.parent;
                }
            }
            boolean jointed = owner != null;
            if (jointed) {
                jointWeights(baseSurface, p4, owner);
                jointWeights(decalSurface, p4, owner);
                if (hullSurface != null && baseSurface != null) {
                    hullSurface.joint = baseSurface.joint;
                }
            }
            parts[i9] = new PartMesh(p4.name, p4.bone, parent, p4.extra, p4.pivotX, p4.pivotY, p4.pivotZ, p4.gripX(), p4.gripY(), p4.gripZ(), motion, baseSurface, hullSurface, decalSurface, slots[i9], limb ? p4.swing : 1.0f, limb ? p4.swingLimit : 0.0f, jointed, jointed ? owner.jointX - p4.pivotX : 0.0f, jointed ? owner.jointY - p4.pivotY : 0.0f, jointed ? owner.jointZ - p4.pivotZ : 0.0f, jointed && owner.neck, jointed ? owner.lookPitch : 0.0f, jointed ? owner.lookYaw : 0.0f, jointed ? owner.lookLimit : 0.0f);
        });
        long finished = System.nanoTime();
        float hip = 12.0f;
        Part rl = rig.bones[Bone.RIGHT_LEG.ordinal()];
        Part ll = rig.bones[Bone.LEFT_LEG.ordinal()];
        if (rl != null && ll != null) {
            hip = (rl.pivotY + ll.pivotY) * 0.5f;
        } else if (rl != null) {
            hip = rl.pivotY;
        } else if (ll != null) {
            hip = ll.pivotY;
        }
        float minY = extent[0] == Float.MAX_VALUE ? 0.0f : extent[0];
        float maxY = extent[1] == -3.4028235E38f ? 32.0f : extent[1];
        long total = (finished - start) / 1000000;
        return new CharacterModel(design.id(), design.name(), detail, rig.scale, parts, slotCount, atlas.width, atlas.height, atlas.mips, atlas.whiteU(), atlas.whiteV(), minY, maxY, hip, rig.firstPersonArms, total);
    }

    private static List<Part> order(Rig rig) {
        List<Part> list = new ArrayList<>();
        for (Part p : rig.bones) {
            if (p != null) {
                list.add(p);
            }
        }
        list.addAll(rig.extras);
        return list;
    }

    private static Shape[] covers(List<Part> order, Shape[] shapes) {
        Part r;
        int n = order.size();
        Part[] root = new Part[n];
        for (int i = 0; i < n; i++) {
            Part part = order.get(i);
            while (true) {
                r = part;
                if (!r.extra || r.motion != null || r.parent == null) {
                    break;
                }
                part = r.parent;
            }
            root[i] = r;
        }
        Shape[] covers = new Shape[n];
        for (int i2 = 0; i2 < n; i2++) {
            if (shapes[i2] != null) {
                List<Shape> near = new ArrayList<>();
                for (int j = 0; j < n; j++) {
                    if (j != i2 && shapes[j] != null && root[j] == root[i2] && overlaps(shapes[i2], shapes[j])) {
                        near.add(shapes[j]);
                    }
                }
                if (!near.isEmpty()) {
                    covers[i2] = Shapes.union((Shape[]) near.toArray(new Shape[0]));
                }
            }
        }
        return covers;
    }

    private static boolean overlaps(Shape a, Shape b) {
        return a.minX <= b.maxX && b.minX <= a.maxX && a.minY <= b.maxY && b.minY <= a.maxY && a.minZ <= b.maxZ && b.minZ <= a.maxZ;
    }

    private static float visible(Mesh mesh, Shape cover) {
        if (cover == null || mesh.triCount == 0) {
            return 1.0f;
        }
        boolean[] buried = new boolean[mesh.vertexCount];
        for (int v = 0; v < mesh.vertexCount; v++) {
            buried[v] = cover.distance(mesh.pos[v * 3], mesh.pos[(v * 3) + 1], mesh.pos[(v * 3) + 2]) < -0.02f;
        }
        int hidden = 0;
        for (int t = 0; t < mesh.triCount; t++) {
            if (buried[mesh.tris[t * 3]] && buried[mesh.tris[(t * 3) + 1]] && buried[mesh.tris[(t * 3) + 2]]) {
                hidden++;
            }
        }
        return Math.max(0.05f, 1.0f - (hidden / mesh.triCount));
    }

    private static void budget(List<Part> order, Mesh[] fine, float[] visible, Detail detail, int[] baseTarget, float[] thickness) {
        int n = order.size();
        float total = 0.0f;
        float outlined = 0.0f;
        float[] weight = new float[n];
        for (int i = 0; i < n; i++) {
            Part p = order.get(i);
            if (fine[i] != null) {
                weight[i] = fine[i].triCount * visible[i] * p.detail;
                total += weight[i];
                if (thickness[i] > 0.0f) {
                    outlined += weight[i];
                }
            }
        }
        if (total <= 0.0f) {
            return;
        }
        float available = detail.budget * 0.9f;
        float share = available / (1.0f + (outlined / total));
        for (int i2 = 0; i2 < n; i2++) {
            if (fine[i2] != null) {
                int target = Math.max(24, Math.round(((share * weight[i2]) / total) / visible[i2]));
                baseTarget[i2] = Math.min(target, fine[i2].triCount);
            }
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/CharacterBuilder$Shaded.class */
    @Environment(EnvType.CLIENT)
    private static final class Shaded {
        final int count;
        final float[] pos;
        final float[] nrm;
        final int[] color;
        final byte[] glow;
        final float[] ao;
        final float[] flat;
        float[] cover;

        Shaded(int count) {
            this.count = count;
            this.pos = new float[count * 3];
            this.nrm = new float[count * 3];
            this.color = new int[count];
            this.glow = new byte[count];
            this.ao = new float[count];
            this.flat = new float[count];
        }
    }

    private static Shaded shade(Shape shape, Shape world, Mesh mesh, float step, float occlusion, float flat) {
        Shaded out = new Shaded(mesh.vertexCount);
        int chunks = (mesh.vertexCount + 255) / 256;
        IntStream.range(0, chunks).parallel().forEach(c -> {
            float[] p = new float[3];
            float[] g = new float[3];
            Sample sample = new Sample();
            int end = Math.min(mesh.vertexCount, (c + 1) * 256);
            for (int v = c * 256; v < end; v++) {
                p[0] = mesh.pos[v * 3];
                p[1] = mesh.pos[(v * 3) + 1];
                p[2] = mesh.pos[(v * 3) + 2];
                project(shape, p, g, step);
                System.arraycopy(p, 0, out.pos, v * 3, 3);
                System.arraycopy(g, 0, out.nrm, v * 3, 3);
                shape.sample(p[0], p[1], p[2], sample);
                float ao = occlusion(world, p[0], p[1], p[2], g[0], g[1], g[2], sample.occlusion(occlusion));
                float ao2 = ao + ((1.0f - ao) * Math.min(1.0f, sample.glow));
                out.ao[v] = ao2;
                out.flat[v] = sample.flat(flat);
                out.glow[v] = (byte) (sample.glow >= 0.5f ? 1 : 0);
                out.color[v] = (-16777216) | (Sample.channel(sample.r * ao2) << 16) | (Sample.channel(sample.g * ao2) << 8) | Sample.channel(sample.b * ao2);
            }
        });
        return out;
    }

    /* JADX WARN: Code duplicated, block: B:18:0x0127  */
    private static Mesh bury(Shaded s, Mesh mesh, Shape cover, float step) {
        if (cover == null) {
            return mesh;
        }
        float[] depth = new float[s.count];
        int chunks = (s.count + 255) / 256;
        IntStream.range(0, chunks).parallel().forEach(c -> {
            float[] p = new float[3];
            float[] g = new float[3];
            int end = Math.min(s.count, (c + 1) * 256);
            for (int v = c * 256; v < end; v++) {
                p[0] = s.pos[v * 3];
                p[1] = s.pos[(v * 3) + 1];
                p[2] = s.pos[(v * 3) + 2];
                float d = cover.distance(p[0], p[1], p[2]);
                depth[v] = d;
                if (d < 0.0f) {
                    gradient(cover, p, Math.max(0.01f, step * 0.3f), g);
                    float sink = Math.min(SINK, (-2.0f) * d);
                    s.pos[v * 3] = p[0] - (g[0] * sink);
                    s.pos[(v * 3) + 1] = p[1] - (g[1] * sink);
                    s.pos[(v * 3) + 2] = p[2] - (g[2] * sink);
                }
            }
        });
        s.cover = depth;
        int[] kept = new int[mesh.triCount * 3];
        int count = 0;
        for (int t = 0; t < mesh.triCount; t++) {
            int a = mesh.tris[t * 3];
            int b = mesh.tris[(t * 3) + 1];
            int c2 = mesh.tris[(t * 3) + 2];
            if (depth[a] < -0.02f && depth[b] < -0.02f && depth[c2] < -0.02f) {
                float cx = ((mesh.pos[a * 3] + mesh.pos[b * 3]) + mesh.pos[c2 * 3]) / 3.0f;
                float cy = ((mesh.pos[(a * 3) + 1] + mesh.pos[(b * 3) + 1]) + mesh.pos[(c2 * 3) + 1]) / 3.0f;
                float cz = ((mesh.pos[(a * 3) + 2] + mesh.pos[(b * 3) + 2]) + mesh.pos[(c2 * 3) + 2]) / 3.0f;
                if (cover.distance(cx, cy, cz) >= -0.02f) {
                    kept[count * 3] = a;
                    kept[(count * 3) + 1] = b;
                    kept[(count * 3) + 2] = c2;
                    count++;
                }
            } else {
                kept[count * 3] = a;
                kept[(count * 3) + 1] = b;
                kept[(count * 3) + 2] = c2;
                count++;
            }
        }
        return count == mesh.triCount ? mesh : new Mesh(mesh.pos, mesh.vertexCount, Arrays.copyOf(kept, count * 3), count);
    }

    static void project(Shape shape, float[] p, float[] n, float step) {
        project(shape, p, n, step, 0.0f);
    }

    static void project(Shape shape, float[] p, float[] n, float step, float iso) {
        float eps = Math.max(0.01f, step * 0.3f);
        float tolerance = step * 0.01f;
        float d = shape.distance(p[0], p[1], p[2]) - iso;
        gradient(shape, p, eps, n);
        for (int it = 0; it < 2 && Math.abs(d) > tolerance; it++) {
            float move = Math.max(-step, Math.min(step, d));
            p[0] = p[0] - (n[0] * move);
            p[1] = p[1] - (n[1] * move);
            p[2] = p[2] - (n[2] * move);
            d = shape.distance(p[0], p[1], p[2]) - iso;
            gradient(shape, p, eps, n);
        }
    }

    static void gradient(Shape shape, float[] p, float eps, float[] out) {
        float x = p[0];
        float y = p[1];
        float z = p[2];
        float gx = shape.distance(x + eps, y, z) - shape.distance(x - eps, y, z);
        float gy = shape.distance(x, y + eps, z) - shape.distance(x, y - eps, z);
        float gz = shape.distance(x, y, z + eps) - shape.distance(x, y, z - eps);
        float l = (float) Math.sqrt((gx * gx) + (gy * gy) + (gz * gz));
        if (l < 1.0E-12f) {
            out[0] = 0.0f;
            out[1] = 1.0f;
            out[2] = 0.0f;
        } else {
            out[0] = gx / l;
            out[1] = gy / l;
            out[2] = gz / l;
        }
    }

    private static float occlusion(Shape world, float x, float y, float z, float nx, float ny, float nz, float strength) {
        if (world == null || strength <= 0.0f) {
            return 1.0f;
        }
        float occ = 0.0f;
        float total = 0.0f;
        float w = 1.0f;
        for (int i = 1; i <= 5; i++) {
            float h = AO_STEP * i;
            float d = world.distance(x + (nx * h), y + (ny * h), z + (nz * h));
            occ += Math.max(0.0f, h - d) * w;
            total += h * w;
            w *= 0.72f;
        }
        float k = Math.min(1.0f, (occ / total) * 1.6f);
        return 1.0f - ((strength * k) * (1.4f - (0.4f * k)));
    }

    private static Surface baseSurface(Shaded s, Mesh mesh, Part p, Atlas atlas) {
        if (mesh.triCount == 0) {
            return null;
        }
        Surface.Builder b = new Surface.Builder();
        float u = atlas.whiteU();
        float v = atlas.whiteV();
        for (int i = 0; i < s.count; i++) {
            b.vertex(s.pos[i * 3] - p.pivotX, s.pos[(i * 3) + 1] - p.pivotY, s.pos[(i * 3) + 2] - p.pivotZ, s.nrm[i * 3], s.nrm[(i * 3) + 1], s.nrm[(i * 3) + 2], u, v, s.color[i], s.glow[i] != 0, s.flat[i]);
        }
        pair(mesh.tris, mesh.triCount, mesh.vertexCount, b);
        return b.build();
    }

    private static Surface hullSurface(Shape shape, Shaded s, Surface base, Part p, float thickness, float step, int color, Atlas atlas) {
        float[] hull = new float[s.count * 3];
        float[] width = new float[s.count];
        float reach = (thickness / 0.5f) * 1.5f;
        int chunks = (s.count + 255) / 256;
        IntStream.range(0, chunks).parallel().forEach(c -> {
            float[] q = new float[3];
            float[] g = new float[3];
            int end = Math.min(s.count, (c + 1) * 256);
            for (int i = c * 256; i < end; i++) {
                float px = s.pos[i * 3];
                float py = s.pos[(i * 3) + 1];
                float pz = s.pos[(i * 3) + 2];
                float nx = s.nrm[i * 3];
                float ny = s.nrm[(i * 3) + 1];
                float nz = s.nrm[(i * 3) + 2];
                float w = (s.cover == null || s.cover[i] >= 0.0f) ? thickness : 0.0f;
                if (w > 0.0f) {
                    float inner = 0.0f;
                    for (int k = 1; k <= 5; k++) {
                        float t = (reach * k) / 5.0f;
                        inner = Math.max(inner, -shape.distance(px - (nx * t), py - (ny * t), pz - (nz * t)));
                    }
                    w = Math.min(w, 0.5f * inner);
                }
                q[0] = px + (nx * w);
                q[1] = py + (ny * w);
                q[2] = pz + (nz * w);
                if (w > 0.0f) {
                    project(shape, q, g, step, w);
                }
                hull[i * 3] = q[0];
                hull[(i * 3) + 1] = q[1];
                hull[(i * 3) + 2] = q[2];
                width[i] = w;
            }
        });
        Surface.Builder b = new Surface.Builder();
        float u = atlas.whiteU();
        float v = atlas.whiteV();
        for (int i = 0; i < s.count; i++) {
            b.vertex(hull[i * 3] - p.pivotX, hull[(i * 3) + 1] - p.pivotY, hull[(i * 3) + 2] - p.pivotZ, 0.0f, 1.0f, 0.0f, u, v, color | (-16777216), false);
        }
        int[] quads = base.quads;
        for (int q = 0; q < quads.length; q += 4) {
            int a = quads[q];
            int bb = quads[q + 1];
            int c2 = quads[q + 2];
            int d = quads[q + 3];
            if (width[a] > 0.0f || width[bb] > 0.0f || width[c2] > 0.0f || width[d] > 0.0f) {
                b.quad(a, d, c2, bb, c2 == d ? 1 : 2);
            }
        }
        return b.build();
    }

    static void pair(int[] tris, int count, int vertices, Surface.Builder out) {
        long[] edges = new long[count * 3];
        for (int t = 0; t < count; t++) {
            for (int e = 0; e < 3; e++) {
                int a = tris[(t * 3) + e];
                int b = tris[(t * 3) + ((e + 1) % 3)];
                long key = (((long) Math.min(a, b)) * ((long) vertices)) + ((long) Math.max(a, b));
                edges[(t * 3) + e] = (key << 21) | ((((long) t) * 3) + ((long) e));
            }
        }
        Arrays.sort(edges);
        int[] neighbour = new int[count * 3];
        Arrays.fill(neighbour, -1);
        int i = 0;
        while (i + 1 < edges.length) {
            if ((edges[i] >>> 21) == (edges[i + 1] >>> 21)) {
                if (i + 2 >= edges.length || (edges[i + 2] >>> 21) != (edges[i] >>> 21)) {
                    int h0 = (int) (edges[i] & 2097151);
                    int h1 = (int) (edges[i + 1] & 2097151);
                    neighbour[h0] = h1;
                    neighbour[h1] = h0;
                    i++;
                } else {
                    while (i + 1 < edges.length && (edges[i + 1] >>> 21) == (edges[i] >>> 21)) {
                        i++;
                    }
                }
            }
            i++;
        }
        boolean[] used = new boolean[count];
        for (int t2 = 0; t2 < count; t2++) {
            if (!used[t2]) {
                int pick = -1;
                for (int e2 = 0; e2 < 3 && pick < 0; e2++) {
                    int h = neighbour[(t2 * 3) + e2];
                    if (h >= 0 && !used[h / 3] && h / 3 != t2) {
                        pick = e2;
                    }
                }
                int a0 = tris[t2 * 3];
                int a1 = tris[(t2 * 3) + 1];
                int a2 = tris[(t2 * 3) + 2];
                if (pick < 0) {
                    used[t2] = true;
                    out.quad(a0, a1, a2, a2, 1);
                } else {
                    int h2 = neighbour[(t2 * 3) + pick];
                    int o = h2 / 3;
                    used[t2] = true;
                    used[o] = true;
                    int a3 = tris[(t2 * 3) + pick];
                    int b2 = tris[(t2 * 3) + ((pick + 1) % 3)];
                    int c = tris[(t2 * 3) + ((pick + 2) % 3)];
                    int d = tris[(o * 3) + (((h2 % 3) + 2) % 3)];
                    out.quad(a3, d, b2, c, 2);
                }
            }
        }
    }

    private static Surface decalSurface(Part p, Shaded s, Mesh mesh, Atlas atlas) {
        float delta;
        Surface.Builder b = new Surface.Builder();
        float[] poly = new float[200];
        float[] tmp = new float[200];
        for (Decal d : p.decals) {
            for (int t = 0; t < mesh.triCount; t++) {
                int i0 = mesh.tris[t * 3];
                int i1 = mesh.tris[(t * 3) + 1];
                int i2 = mesh.tris[(t * 3) + 2];
                if (faces(d, s, i0, i1, i2)) {
                    int[] idx = {i0, i1, i2};
                    float firstAngle = 0.0f;
                    for (int c = 0; c < 3; c++) {
                        int v = idx[c];
                        int o = c * 10;
                        poly[o] = s.pos[v * 3];
                        poly[o + 1] = s.pos[(v * 3) + 1];
                        poly[o + 2] = s.pos[(v * 3) + 2];
                        poly[o + 3] = s.nrm[v * 3];
                        poly[o + 4] = s.nrm[(v * 3) + 1];
                        poly[o + 5] = s.nrm[(v * 3) + 2];
                        poly[o + 8] = s.ao[v];
                        poly[o + 9] = s.flat[v];
                        float rx = poly[o] - d.cx;
                        float ry = poly[o + 1] - d.cy;
                        float rz = poly[o + 2] - d.cz;
                        if (d.wrap) {
                            float along = (rx * d.dx) + (ry * d.dy) + (rz * d.dz);
                            float fx = rx - (along * d.dx);
                            float fy = ry - (along * d.dy);
                            float fz = rz - (along * d.dz);
                            float angle = (float) Math.toDegrees(Math.atan2((fx * d.rx) + (fy * d.ry) + (fz * d.rz), (fx * d.ux) + (fy * d.uy) + (fz * d.uz)));
                            if (c == 0) {
                                firstAngle = angle;
                            } else {
                                float f = angle;
                                float f2 = firstAngle;
                                while (true) {
                                    delta = f - f2;
                                    if (delta <= 180.0f) {
                                        break;
                                    }
                                    f = delta;
                                    f2 = 360.0f;
                                }
                                while (delta < -180.0f) {
                                    delta += 360.0f;
                                }
                                angle = firstAngle + delta;
                            }
                            poly[o + 6] = 0.5f + (angle / d.width);
                            poly[o + 7] = 0.5f - (along / d.height);
                        } else {
                            poly[o + 6] = 0.5f + ((((rx * d.rx) + (ry * d.ry)) + (rz * d.rz)) / d.width);
                            poly[o + 7] = 0.5f - ((((rx * d.ux) + (ry * d.uy)) + (rz * d.uz)) / d.height);
                        }
                    }
                    emitClipped(d, poly, tmp, 0.0f, p, atlas, b);
                    if (d.wrap && d.width >= 359.9f) {
                        emitClipped(d, poly, tmp, 1.0f, p, atlas, b);
                        emitClipped(d, poly, tmp, -1.0f, p, atlas, b);
                    }
                }
            }
        }
        return b.build();
    }

    private static boolean faces(Decal d, Shaded s, int i0, int i1, int i2) {
        float ax = s.pos[i0 * 3];
        float ay = s.pos[(i0 * 3) + 1];
        float az = s.pos[(i0 * 3) + 2];
        float ux = s.pos[i1 * 3] - ax;
        float uy = s.pos[(i1 * 3) + 1] - ay;
        float uz = s.pos[(i1 * 3) + 2] - az;
        float vx = s.pos[i2 * 3] - ax;
        float vy = s.pos[(i2 * 3) + 1] - ay;
        float vz = s.pos[(i2 * 3) + 2] - az;
        float nx = (uy * vz) - (uz * vy);
        float ny = (uz * vx) - (ux * vz);
        float nz = (ux * vy) - (uy * vx);
        float nl = (float) Math.sqrt((nx * nx) + (ny * ny) + (nz * nz));
        if (nl < 1.0E-10f) {
            return false;
        }
        float nx2 = nx / nl;
        float ny2 = ny / nl;
        float nz2 = nz / nl;
        int[] idx = {i0, i1, i2};
        if (!d.wrap) {
            if ((nx2 * d.dx) + (ny2 * d.dy) + (nz2 * d.dz) < d.cosLimit) {
                return false;
            }
            for (int v : idx) {
                float depth = ((s.pos[v * 3] - d.cx) * d.dx) + ((s.pos[(v * 3) + 1] - d.cy) * d.dy) + ((s.pos[(v * 3) + 2] - d.cz) * d.dz);
                if (depth < d.near || depth > d.far) {
                    return false;
                }
            }
            return true;
        }
        float cx = (((s.pos[i0 * 3] + s.pos[i1 * 3]) + s.pos[i2 * 3]) / 3.0f) - d.cx;
        float cy = (((s.pos[(i0 * 3) + 1] + s.pos[(i1 * 3) + 1]) + s.pos[(i2 * 3) + 1]) / 3.0f) - d.cy;
        float cz = (((s.pos[(i0 * 3) + 2] + s.pos[(i1 * 3) + 2]) + s.pos[(i2 * 3) + 2]) / 3.0f) - d.cz;
        float along = (cx * d.dx) + (cy * d.dy) + (cz * d.dz);
        float rx = cx - (along * d.dx);
        float ry = cy - (along * d.dy);
        float rz = cz - (along * d.dz);
        float rl = (float) Math.sqrt((rx * rx) + (ry * ry) + (rz * rz));
        if (rl < 1.0E-5f || (((nx2 * rx) + (ny2 * ry)) + (nz2 * rz)) / rl < d.cosLimit) {
            return false;
        }
        float near = d.near == -3.4028235E38f ? 0.0f : d.near;
        return rl >= near && rl <= d.far;
    }

    private static void emitClipped(Decal d, float[] tri, float[] tmp, float shift, Part p, Atlas atlas, Surface.Builder out) {
        float[] a = new float[200];
        System.arraycopy(tri, 0, a, 0, 30);
        if (shift != 0.0f) {
            for (int c = 0; c < 3; c++) {
                int i = (c * 10) + 6;
                a[i] = a[i] + shift;
            }
        }
        int n = clip(tmp, clip(a, clip(tmp, clip(a, 3, tmp, 6, 0.0f, true), a, 6, 1.0f, false), tmp, 7, 0.0f, true), a, 7, 1.0f, false);
        if (n < 3) {
            return;
        }
        int first = -1;
        for (int c2 = 0; c2 < n; c2++) {
            int o = c2 * 10;
            float nx = a[o + 3];
            float ny = a[o + 4];
            float nz = a[o + 5];
            float l = (float) Math.sqrt((nx * nx) + (ny * ny) + (nz * nz));
            if (l > 1.0E-8f) {
                nx /= l;
                ny /= l;
                nz /= l;
            }
            int shade = Sample.channel(1.0f - (d.occlusion * (1.0f - a[o + 8])));
            int argb = (-16777216) | (shade << 16) | (shade << 8) | shade;
            float flat = d.flat == d.flat ? d.flat : a[o + 9];
            int idx = out.vertex((a[o] + (nx * d.offset)) - p.pivotX, (a[o + 1] + (ny * d.offset)) - p.pivotY, (a[o + 2] + (nz * d.offset)) - p.pivotZ, nx, ny, nz, atlas.u(d.canvas, a[o + 6]), atlas.v(d.canvas, a[o + 7]), argb, d.glow, flat);
            if (first < 0) {
                first = idx;
            }
        }
        int k = 1;
        while (k + 2 < n) {
            out.quad(first, first + k, first + k + 1, first + k + 2, 2);
            k += 2;
        }
        if (k + 1 < n) {
            out.quad(first, first + k, first + k + 1, first + k + 1, 1);
        }
    }

    private static int clip(float[] in, int n, float[] out, int comp, float bound, boolean keepAbove) {
        int m = 0;
        for (int i = 0; i < n; i++) {
            int cur = i * 10;
            int prev = (((i + n) - 1) % n) * 10;
            float cd = keepAbove ? in[cur + comp] - bound : bound - in[cur + comp];
            float pd = keepAbove ? in[prev + comp] - bound : bound - in[prev + comp];
            if (cd >= 0.0f) {
                if (pd < 0.0f) {
                    m = cut(in, prev, cur, pd, cd, out, m);
                }
                System.arraycopy(in, cur, out, m * 10, 10);
                m++;
            } else if (pd >= 0.0f) {
                m = cut(in, prev, cur, pd, cd, out, m);
            }
            if (m >= 19) {
                break;
            }
        }
        return m;
    }

    private static int cut(float[] in, int a, int b, float da, float db, float[] out, int m) {
        float t = da / (da - db);
        for (int k = 0; k < 10; k++) {
            out[(m * 10) + k] = in[a + k] + ((in[b + k] - in[a + k]) * t);
        }
        return m + 1;
    }

    private static float reach(Surface s) {
        if (s == null) {
            return 1.0f;
        }
        float r = 0.0f;
        for (int i = 0; i < s.vertexCount(); i++) {
            float x = s.pos[i * 3];
            float y = s.pos[(i * 3) + 1];
            float z = s.pos[(i * 3) + 2];
            r = Math.max(r, (x * x) + (y * y) + (z * z));
        }
        return Math.max(0.5f, (float) Math.sqrt(r));
    }

    private static void jointWeights(Surface s, Part p, Part owner) {
        if (s == null) {
            return;
        }
        float[] w = new float[s.vertexCount()];
        float ox = owner.jointX - p.pivotX;
        float oy = owner.jointY - p.pivotY;
        float oz = owner.jointZ - p.pivotZ;
        float blend = owner.jointBlend;
        boolean any = false;
        for (int i = 0; i < w.length; i++) {
            float along = ((s.pos[i * 3] - ox) * owner.jointDirX) + ((s.pos[(i * 3) + 1] - oy) * owner.jointDirY) + ((s.pos[(i * 3) + 2] - oz) * owner.jointDirZ);
            float t = Math.max(0.0f, Math.min(1.0f, (along + blend) / (2.0f * blend)));
            w[i] = t * t * (3.0f - (2.0f * t));
            any |= w[i] > 0.0f;
        }
        if (any) {
            s.joint = w;
        }
    }

    private static void weigh(Surface s, float reach, float power) {
        if (s == null) {
            return;
        }
        float[] w = new float[s.vertexCount()];
        for (int i = 0; i < w.length; i++) {
            float x = s.pos[i * 3];
            float y = s.pos[(i * 3) + 1];
            float z = s.pos[(i * 3) + 2];
            float t = Math.min(1.0f, ((float) Math.sqrt(((x * x) + (y * y)) + (z * z))) / reach);
            w[i] = (float) Math.pow(t, power);
        }
        s.weight = w;
    }
}
