package net.anubis.client.module.impl.render.character.gltf;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import net.anubis.client.module.impl.render.character.kit.BakedModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/gltf/CharacterBake.class */
@Environment(EnvType.CLIENT)
public final class CharacterBake {
    static final int HEAD = 0;
    static final int BODY = 1;
    static final int RIGHT_ARM = 2;
    static final int LEFT_ARM = 3;
    static final int RIGHT_LEG = 4;
    static final int LEFT_LEG = 5;
    private static final int DENSE = 150000;
    private static final int[] BUDGETS = {7500, 15000, 30000};
    private static final float[] STICK = {0.7f, 0.0f, 0.6f, 0.6f, 0.3f, 0.3f};

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/gltf/CharacterBake$Options.class */
    @Environment(EnvType.CLIENT)
    public static final class Options {
        public float flat;
        public float height = 30.0f;
        public final Map<String, String> bones = new HashMap();
        public final Set<String> headMaterials = new HashSet();
        public float neck = -1.0f;
        public float hip = -1.0f;
        public float floor = -1.0f;
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/gltf/CharacterBake$Result.class */
    @Environment(EnvType.CLIENT)
    public static final class Result extends Record {
        private final byte[] data;
        private final List<String> limbs;

        public Result(byte[] data, List<String> limbs) {
            this.data = data;
            this.limbs = limbs;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Result.class), Result.class, "data;limbs", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/CharacterBake$Result;->data:[B", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/CharacterBake$Result;->limbs:Ljava/util/List;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Result.class), Result.class, "data;limbs", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/CharacterBake$Result;->data:[B", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/CharacterBake$Result;->limbs:Ljava/util/List;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Result.class, Object.class), Result.class, "data;limbs", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/CharacterBake$Result;->data:[B", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/CharacterBake$Result;->limbs:Ljava/util/List;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public byte[] data() {
            return this.data;
        }

        public List<String> limbs() {
            return this.limbs;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/gltf/CharacterBake$Cut.class */
    @Environment(EnvType.CLIENT)
    static final class Cut extends Record {
        private final int[] part;
        private final float[][] pivot;
        private final float[][] grip;
        private final float[][] bendAt;
        private final float[][] bendWeights;

        Cut(int[] part, float[][] pivot, float[][] grip, float[][] bendAt, float[][] bendWeights) {
            this.part = part;
            this.pivot = pivot;
            this.grip = grip;
            this.bendAt = bendAt;
            this.bendWeights = bendWeights;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Cut.class), Cut.class, "part;pivot;grip;bendAt;bendWeights", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/CharacterBake$Cut;->part:[I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/CharacterBake$Cut;->pivot:[[F", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/CharacterBake$Cut;->grip:[[F", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/CharacterBake$Cut;->bendAt:[[F", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/CharacterBake$Cut;->bendWeights:[[F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Cut.class), Cut.class, "part;pivot;grip;bendAt;bendWeights", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/CharacterBake$Cut;->part:[I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/CharacterBake$Cut;->pivot:[[F", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/CharacterBake$Cut;->grip:[[F", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/CharacterBake$Cut;->bendAt:[[F", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/CharacterBake$Cut;->bendWeights:[[F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Cut.class, Object.class), Cut.class, "part;pivot;grip;bendAt;bendWeights", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/CharacterBake$Cut;->part:[I", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/CharacterBake$Cut;->pivot:[[F", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/CharacterBake$Cut;->grip:[[F", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/CharacterBake$Cut;->bendAt:[[F", "FIELD:Lnet/anubis/client/module/impl/render/character/gltf/CharacterBake$Cut;->bendWeights:[[F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int[] part() {
            return this.part;
        }

        public float[][] pivot() {
            return this.pivot;
        }

        public float[][] grip() {
            return this.grip;
        }

        public float[][] bendAt() {
            return this.bendAt;
        }

        public float[][] bendWeights() {
            return this.bendWeights;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/gltf/CharacterBake$Piece.class */
    @Environment(EnvType.CLIENT)
    private static final class Piece {
        final int bone;
        final int layer;
        final List<Integer> source = new ArrayList();
        int[][] levels;

        Piece(int bone, int layer) {
            this.bone = bone;
            this.layer = layer;
        }
    }

    private CharacterBake() {
    }

    public static Result bake(byte[] glb, Options options, ImageDecoder decoder) throws IOException {
        GltfFile file = GltfFile.read(glb);
        PosedMesh mesh = PosedMesh.pose(file, Map.of());
        Skeleton skeleton = Skeleton.find(mesh, options.bones);
        Map<Integer, Float> turns = skeleton.armTurns();
        if (!turns.isEmpty()) {
            mesh = PosedMesh.pose(file, turns);
            skeleton = Skeleton.find(mesh, options.bones);
        }
        mesh.normalise(options.height, skeleton.facesBack);
        mesh.cluster(DENSE);
        Cut cut = skeleton.found() ? skeletonCut(mesh, skeleton, options) : ShapeCut.cut(mesh);
        if (cut == null) {
            cut = still(mesh);
        }
        List<Piece> pieces = split(mesh, cut.part());
        reduce(mesh, pieces);
        TextureAtlas atlas = TextureAtlas.pack(file, uvBounds(mesh, pieces), decoder);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        write(new DataOutputStream(bytes), mesh, cut, pieces, atlas, options);
        List<String> limbs = new ArrayList<>();
        if (has(pieces, 0)) {
            limbs.add("head");
        }
        if (has(pieces, 2) || has(pieces, 3)) {
            limbs.add("arms");
        }
        if (has(pieces, 4) || has(pieces, 5)) {
            limbs.add("legs");
        }
        return new Result(bytes.toByteArray(), limbs);
    }

    public static String describe(byte[] glb, Options options) throws IOException {
        GltfFile file = GltfFile.read(glb);
        PosedMesh mesh = PosedMesh.pose(file, Map.of());
        Skeleton skeleton = Skeleton.find(mesh, options.bones);
        StringBuilder b = new StringBuilder(String.format(Locale.ROOT, "%d vertices, %d triangles, %d bones%n", Integer.valueOf(mesh.vertices), Integer.valueOf(mesh.triangles), Integer.valueOf(mesh.joints.size())));
        b.append(skeleton.found() ? skeleton.describe() : "no usable skeleton, limbs are cut by shape\n");
        Set<Integer> materials = new HashSet<>();
        for (int v = 0; v < mesh.vertices; v++) {
            materials.add(Integer.valueOf(mesh.material[v]));
        }
        Iterator<Integer> it = materials.iterator();
        while (it.hasNext()) {
            int m = it.next().intValue();
            b.append("material ").append(file.materialName(m)).append('\n');
        }
        return b.toString();
    }

    private static Cut still(PosedMesh mesh) {
        float[][] pivot = new float[6][];
        pivot[1] = new float[]{0.0f, 24.0f, 0.0f};
        return new Cut(new int[mesh.vertices], pivot, new float[6][], new float[6][], new float[6][]);
    }

    private static Cut skeletonCut(PosedMesh mesh, Skeleton sk, Options o) {
        int[] group = sk.groups();
        boolean legs = sk.thigh[0] >= 0 && sk.thigh[1] >= 0;
        float floor = o.floor;
        if (floor < 0.0f && sk.shin[0] >= 0 && sk.shin[1] >= 0) {
            floor = Math.min(mesh.joint(sk.shin[0])[1], mesh.joint(sk.shin[1])[1]);
        }
        boolean pinned = !o.headMaterials.isEmpty() && sk.head >= 0;
        int[] part = new int[mesh.vertices];
        float[] sum = new float[6];
        for (int v = 0; v < mesh.vertices; v++) {
            float x = mesh.pos[v * 3];
            float y = mesh.pos[(v * 3) + 1];
            if (pinned && o.headMaterials.contains(mesh.file.materialName(mesh.material[v]))) {
                part[v] = y < o.neck ? 1 : 0;
            } else {
                Arrays.fill(sum, 0.0f);
                for (int k = 0; k < 4; k++) {
                    int node = mesh.influence[(v * 4) + k];
                    if (node >= 0) {
                        int i = group[node];
                        sum[i] = sum[i] + mesh.weight[(v * 4) + k];
                    }
                }
                int best = 1;
                for (int b = 0; b < 6; b++) {
                    if (b != 1 && sum[b] >= STICK[b] && sum[b] > sum[best] && (b != 0 || !pinned)) {
                        best = b;
                    }
                }
                boolean lower = best == 1 || best == 4 || best == 5;
                if (legs && lower && o.hip > 0.0f) {
                    best = y >= o.hip ? 1 : x < 0.0f ? 4 : 5;
                } else if (legs && best == 1 && y < floor) {
                    best = x < 0.0f ? 4 : 5;
                }
                part[v] = best;
            }
        }
        float[][] pivot = new float[6][];
        float[][] grip = new float[6][];
        float[][] bendAt = new float[6][];
        float[][] bendWeights = new float[6][];
        pivot[1] = new float[]{0.0f, 24.0f, 0.0f};
        if (sk.head >= 0) {
            pivot[0] = mesh.joint(sk.head);
        }
        int s = 0;
        while (s < 2) {
            int arm = s == 0 ? 2 : 3;
            int leg = s == 0 ? 4 : 5;
            if (sk.upperArm[s] >= 0) {
                pivot[arm] = mesh.joint(sk.upperArm[s]);
                int end = sk.hand[s] >= 0 ? sk.hand[s] : sk.forearm[s];
                grip[arm] = end >= 0 ? mesh.joint(end) : pivot[arm];
                if (sk.forearm[s] >= 0) {
                    bendAt[arm] = mesh.joint(sk.forearm[s]);
                    bendWeights[arm] = bend(mesh, sk.forearm[s], sk.upperArm[s]);
                }
            }
            if (sk.thigh[s] >= 0) {
                pivot[leg] = mesh.joint(sk.thigh[s]);
                if (sk.shin[s] >= 0) {
                    bendAt[leg] = mesh.joint(sk.shin[s]);
                    bendWeights[leg] = bend(mesh, sk.shin[s], sk.thigh[s]);
                }
            }
            s++;
        }
        return new Cut(part, pivot, grip, bendAt, bendWeights);
    }

    static int owner(int[] part, int a, int b, int c) {
        return part[b] == part[c] ? part[b] : part[a];
    }

    private static List<Piece> split(PosedMesh mesh, int[] part) {
        Piece[][] pieces = new Piece[6][2];
        for (int t = 0; t < mesh.triangles; t++) {
            int a = mesh.tris[t * 3];
            int b = mesh.tris[(t * 3) + 1];
            int c = mesh.tris[(t * 3) + 2];
            if (Mat4.area(mesh.pos, a, b, c) >= 1.0E-9f) {
                int owner = owner(part, a, b, c);
                int layer = mesh.file.translucent(mesh.material[a]) ? 1 : 0;
                Piece p = pieces[owner][layer];
                if (p == null) {
                    Piece[] pieceArr = pieces[owner];
                    Piece piece = new Piece(owner, layer);
                    pieceArr[layer] = piece;
                    p = piece;
                }
                p.source.add(Integer.valueOf(t));
                if (layer == 1 && mesh.file.doubleSided(mesh.material[a])) {
                    p.source.add(Integer.valueOf(t ^ (-1)));
                }
            }
        }
        List<Piece> all = new ArrayList<>();
        for (Piece[] row : pieces) {
            for (Piece p2 : row) {
                if (p2 != null) {
                    all.add(p2);
                }
            }
        }
        return all;
    }

    private static void reduce(PosedMesh mesh, List<Piece> pieces) {
        double weightSum = 0.0d;
        for (Piece p : pieces) {
            weightSum += ((double) p.source.size()) * (p.bone == 0 ? 2.0d : 1.0d);
        }
        for (Piece p2 : pieces) {
            int[] targets = new int[BUDGETS.length];
            for (int l = 0; l < BUDGETS.length; l++) {
                double share = (((double) p2.source.size()) * (p2.bone == 0 ? 2.0d : 1.0d)) / weightSum;
                targets[l] = (int) Math.min(p2.source.size(), Math.max(16L, Math.round(((double) BUDGETS[l]) * share)));
            }
            boolean needed = targets[BUDGETS.length - 1] < p2.source.size();
            p2.levels = new MeshReducer(mesh, p2.source, needed).reduce(targets);
        }
    }

    private static Map<Integer, float[]> uvBounds(PosedMesh mesh, List<Piece> pieces) {
        Map<Integer, float[]> used = new LinkedHashMap<>();
        for (Piece p : pieces) {
            Iterator<Integer> it = p.source.iterator();
            while (it.hasNext()) {
                int t = it.next().intValue();
                int tri = t < 0 ? t ^ (-1) : t;
                for (int c = 0; c < 3; c++) {
                    int v = mesh.tris[(tri * 3) + c];
                    float[] b = used.computeIfAbsent(Integer.valueOf(mesh.material[v]), k -> {
                        return new float[]{1.0E9f, 1.0E9f, -1.0E9f, -1.0E9f};
                    });
                    b[0] = Math.min(b[0], mesh.uv[v * 2]);
                    b[1] = Math.min(b[1], mesh.uv[(v * 2) + 1]);
                    b[2] = Math.max(b[2], mesh.uv[v * 2]);
                    b[3] = Math.max(b[3], mesh.uv[(v * 2) + 1]);
                }
            }
        }
        return used;
    }

    private static boolean has(List<Piece> pieces, int bone) {
        for (Piece p : pieces) {
            if (p.bone == bone) {
                return true;
            }
        }
        return false;
    }

    private static Piece find(List<Piece> pieces, int bone, int layer) {
        for (Piece p : pieces) {
            if (p.bone == bone && p.layer == layer) {
                return p;
            }
        }
        return null;
    }

    private static void write(DataOutputStream out, PosedMesh mesh, Cut cut, List<Piece> pieces, TextureAtlas atlas, Options o) throws IOException {
        float[][] pivot = cut.pivot();
        float[][] grip = cut.grip();
        float[][] bendAt = cut.bendAt();
        float[][] bendWeights = cut.bendWeights();
        float[] box = mesh.bounds();
        float hipY = (pivot[4] == null || pivot[5] == null) ? 12.0f : (pivot[4][1] + pivot[5][1]) * 0.5f;
        out.writeInt(BakedModel.MAGIC);
        out.writeInt(3);
        out.writeFloat(box[1]);
        out.writeFloat(box[4]);
        out.writeFloat(hipY);
        out.writeFloat(o.flat);
        out.writeBoolean(true);
        out.writeBoolean(mesh.colored);
        out.writeInt(BUDGETS.length);
        int bones = 0;
        for (int b = 0; b < 6; b++) {
            if (has(pieces, b)) {
                bones++;
            }
        }
        out.writeInt(bones);
        int b2 = 0;
        while (b2 < 6) {
            if (has(pieces, b2)) {
                out.writeByte(b2);
                for (float f : pivot[b2]) {
                    out.writeFloat(f);
                }
                for (float f2 : grip[b2] != null ? grip[b2] : pivot[b2]) {
                    out.writeFloat(f2);
                }
                float[] bounds = bounds(mesh, pieces, b2);
                for (float f3 : bounds) {
                    out.writeFloat(f3);
                }
                boolean arm = b2 == 2 || b2 == 3;
                boolean leg = b2 == 4 || b2 == 5;
                out.writeFloat(arm ? 0.6f : leg ? 0.55f : 1.0f);
                out.writeFloat(arm ? 40.0f : leg ? 30.0f : 0.0f);
                out.writeBoolean(bendAt[b2] != null);
                if (bendAt[b2] != null) {
                    for (float f4 : bendAt[b2]) {
                        out.writeFloat(f4);
                    }
                }
                for (int l = 0; l < BUDGETS.length; l++) {
                    for (int layer = 0; layer < 2; layer++) {
                        writeLevel(out, mesh, atlas, find(pieces, b2, layer), l, bounds, bendWeights[b2], bendAt[b2] != null);
                    }
                }
            }
            b2++;
        }
        byte[] pixels = atlas.packed();
        out.writeInt(atlas.width);
        out.writeInt(atlas.height);
        out.writeInt(pixels.length);
        out.write(pixels);
        out.flush();
    }

    private static float[] bend(PosedMesh mesh, int joint, int limb) {
        float[] at = mesh.joint(joint);
        float[] w = mesh.below(joint);
        float[] inLimb = mesh.below(limb);
        for (int v = 0; v < mesh.vertices; v++) {
            if (inLimb[v] < 0.5f) {
                float t = Math.max(0.0f, Math.min(1.0f, (at[1] - mesh.pos[(v * 3) + 1]) + 0.5f));
                w[v] = t * t * (3.0f - (2.0f * t));
            }
        }
        return w;
    }

    private static float[] bounds(PosedMesh mesh, List<Piece> pieces, int bone) {
        float[] b = new float[6];
        b[0] = 1.0E9f;
        b[1] = 1.0E9f;
        b[2] = 1.0E9f;
        b[3] = -1.0E9f;
        b[4] = -1.0E9f;
        b[5] = -1.0E9f;
        for (Piece p : pieces) {
            if (p.bone == bone) {
                Iterator<Integer> it = p.source.iterator();
                while (it.hasNext()) {
                    int t = it.next().intValue();
                    int tri = t < 0 ? t ^ (-1) : t;
                    for (int c = 0; c < 3; c++) {
                        int v = mesh.tris[(tri * 3) + c];
                        for (int k = 0; k < 3; k++) {
                            b[k] = Math.min(b[k], mesh.pos[(v * 3) + k]);
                            b[k + 3] = Math.max(b[k + 3], mesh.pos[(v * 3) + k]);
                        }
                    }
                }
            }
        }
        for (int k2 = 0; k2 < 3; k2++) {
            if (b[k2 + 3] - b[k2] < 0.001f) {
                b[k2 + 3] = b[k2] + 0.001f;
            }
        }
        return b;
    }

    private static void writeLevel(DataOutputStream out, PosedMesh mesh, TextureAtlas atlas, Piece p, int level, float[] box, float[] bend, boolean jointed) throws IOException {
        if (p == null) {
            out.writeInt(0);
            out.writeInt(0);
            return;
        }
        int[] corners = p.levels[level];
        Map<Integer, Integer> remap = new HashMap<>();
        List<Integer> keys = new ArrayList<>();
        int[] local = new int[corners.length];
        for (int i = 0; i < corners.length; i++) {
            Integer idx = remap.get(Integer.valueOf(corners[i]));
            if (idx == null) {
                idx = Integer.valueOf(keys.size());
                remap.put(Integer.valueOf(corners[i]), idx);
                keys.add(Integer.valueOf(corners[i]));
            }
            local[i] = idx.intValue();
        }
        out.writeInt(keys.size());
        Iterator<Integer> it = keys.iterator();
        while (it.hasNext()) {
            int k = it.next().intValue();
            int v = k < 0 ? k ^ (-1) : k;
            for (int a = 0; a < 3; a++) {
                out.writeChar(Math.round(Math.max(0.0f, Math.min(1.0f, (mesh.pos[(v * 3) + a] - box[a]) / (box[a + 3] - box[a]))) * 65535.0f));
            }
        }
        Iterator<Integer> it2 = keys.iterator();
        while (it2.hasNext()) {
            int k2 = it2.next().intValue();
            int v2 = k2 < 0 ? k2 ^ (-1) : k2;
            float sign = k2 < 0 ? -1.0f : 1.0f;
            for (int a2 = 0; a2 < 3; a2++) {
                out.writeByte(Math.round(mesh.nrm[(v2 * 3) + a2] * sign * 127.0f));
            }
        }
        Iterator<Integer> it3 = keys.iterator();
        while (it3.hasNext()) {
            int k3 = it3.next().intValue();
            int v3 = k3 < 0 ? k3 ^ (-1) : k3;
            float[] t = atlas.map(mesh.material[v3], mesh.uv[v3 * 2], mesh.uv[(v3 * 2) + 1]);
            out.writeChar(Math.round(Math.max(0.0f, Math.min(1.0f, t[0])) * 65535.0f));
            out.writeChar(Math.round(Math.max(0.0f, Math.min(1.0f, t[1])) * 65535.0f));
        }
        if (mesh.colored) {
            Iterator<Integer> it4 = keys.iterator();
            while (it4.hasNext()) {
                int k4 = it4.next().intValue();
                int c = mesh.color[k4 < 0 ? k4 ^ (-1) : k4];
                out.writeByte(c >> 16);
                out.writeByte(c >> 8);
                out.writeByte(c);
            }
        }
        if (jointed) {
            Iterator<Integer> it5 = keys.iterator();
            while (it5.hasNext()) {
                int k5 = it5.next().intValue();
                out.writeByte(Math.round(Math.max(0.0f, Math.min(1.0f, bend[k5 < 0 ? k5 ^ (-1) : k5])) * 255.0f));
            }
        }
        out.writeInt(local.length / 3);
        boolean wide = keys.size() > 65535;
        for (int i2 : local) {
            if (wide) {
                out.writeInt(i2);
            } else {
                out.writeChar(i2);
            }
        }
    }
}
