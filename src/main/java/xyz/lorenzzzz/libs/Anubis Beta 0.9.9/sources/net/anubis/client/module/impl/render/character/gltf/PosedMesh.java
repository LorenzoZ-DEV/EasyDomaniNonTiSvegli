package net.anubis.client.module.impl.render.character.gltf;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/gltf/PosedMesh.class */
@Environment(EnvType.CLIENT)
final class PosedMesh {
    private static final int MAX_TRIANGLES = 4000000;
    final GltfFile file;
    final float[][] globals;
    final int[] parent;
    boolean colored;
    int vertices;
    int triangles;
    private float shiftX;
    private float shiftY;
    private float shiftZ;
    private boolean flipped;
    final Set<Integer> joints = new HashSet();
    float[] pos = new float[0];
    float[] nrm = new float[0];
    float[] uv = new float[0];
    int[] material = new int[0];
    int[] color = new int[0];
    int[] influence = new int[0];
    float[] weight = new float[0];
    int[] tris = new int[0];
    private float scale = 1.0f;

    private PosedMesh(GltfFile file) {
        this.file = file;
        this.globals = new float[file.nodes()][];
        this.parent = new int[file.nodes()];
        Arrays.fill(this.parent, -1);
    }

    static PosedMesh pose(GltfFile file, Map<Integer, Float> turns) throws IOException {
        PosedMesh m = new PosedMesh(file);
        for (int n = 0; n < file.nodes(); n++) {
            for (JsonElement c : GltfFile.array(file.node(n), "children")) {
                m.parent[c.getAsInt()] = n;
            }
        }
        JsonArray scenes = GltfFile.array(file.json, "scenes");
        JsonArray<JsonElement> roots = new JsonArray();
        if (scenes.isEmpty()) {
            for (int n2 = 0; n2 < file.nodes(); n2++) {
                if (m.parent[n2] < 0) {
                    roots.add(Integer.valueOf(n2));
                }
            }
        } else {
            roots = GltfFile.array(scenes.get(GltfFile.integer(file.json, "scene", 0)).getAsJsonObject(), "nodes");
        }
        for (JsonElement r : roots) {
            m.walk(r.getAsInt(), Mat4.identity(), turns);
        }
        for (JsonElement skin : GltfFile.array(file.json, "skins")) {
            for (JsonElement j : GltfFile.array(skin.getAsJsonObject(), "joints")) {
                m.joints.add(Integer.valueOf(j.getAsInt()));
            }
        }
        for (JsonElement r2 : roots) {
            m.collect(r2.getAsInt());
        }
        if (m.triangles == 0) {
            throw new IOException("it has no triangles");
        }
        return m;
    }

    private void walk(int node, float[] parentGlobal, Map<Integer, Float> turns) {
        float[] global = Mat4.mul(parentGlobal, Mat4.local(this.file.node(node)));
        Float turn = turns.get(Integer.valueOf(node));
        if (turn != null) {
            global = Mat4.mul(Mat4.rollAbout(turn.floatValue(), Mat4.point(global, 0.0f, 0.0f, 0.0f)), global);
        }
        this.globals[node] = global;
        for (JsonElement c : GltfFile.array(this.file.node(node), "children")) {
            walk(c.getAsInt(), global, turns);
        }
    }

    private void collect(int node) throws IOException {
        JsonObject n = this.file.node(node);
        if (n.has("mesh")) {
            JsonObject mesh = GltfFile.array(this.file.json, "meshes").get(n.get("mesh").getAsInt()).getAsJsonObject();
            int skin = GltfFile.integer(n, "skin", -1);
            for (JsonElement p : GltfFile.array(mesh, "primitives")) {
                primitive(p.getAsJsonObject(), node, skin);
            }
        }
        for (JsonElement c : GltfFile.array(n, "children")) {
            collect(c.getAsInt());
        }
    }

    private void primitive(JsonObject p, int node, int skin) throws IOException {
        float[] outP;
        float[] outN;
        if (GltfFile.integer(p, "mode", 4) != 4) {
            return;
        }
        JsonObject attrs = GltfFile.object(p, "attributes");
        int mat = GltfFile.integer(p, "material", -1);
        float[] ps = this.file.floats(attrs.get("POSITION").getAsInt());
        int n = ps.length / 3;
        float[] ns = attrs.has("NORMAL") ? this.file.floats(attrs.get("NORMAL").getAsInt()) : null;
        String uvKey = "TEXCOORD_" + this.file.texCoord(mat);
        float[] ts = attrs.has(uvKey) ? this.file.floats(attrs.get(uvKey).getAsInt()) : new float[n * 2];
        float[] cs = attrs.has("COLOR_0") ? this.file.floats(attrs.get("COLOR_0").getAsInt()) : null;
        int cn = cs == null ? 0 : cs.length / n;
        this.colored |= cs != null;
        int[] idx = p.has("indices") ? this.file.ints(p.get("indices").getAsInt()) : null;
        if (idx == null) {
            idx = new int[n];
            for (int i = 0; i < n; i++) {
                idx[i] = i;
            }
        }
        for (int i2 : idx) {
            if (i2 < 0 || i2 >= n) {
                throw new IOException("a mesh has broken indices");
            }
        }
        if (this.triangles + (idx.length / 3) > MAX_TRIANGLES) {
            throw new IOException("it has more than four million triangles");
        }
        float[][] skinMats = null;
        int[] js = null;
        int[] jointNodes = null;
        float[] ws = null;
        if (skin >= 0 && attrs.has("JOINTS_0") && attrs.has("WEIGHTS_0")) {
            JsonObject sk = GltfFile.array(this.file.json, "skins").get(skin).getAsJsonObject();
            JsonArray list = GltfFile.array(sk, "joints");
            jointNodes = new int[list.size()];
            float[] ibm = sk.has("inverseBindMatrices") ? this.file.floats(sk.get("inverseBindMatrices").getAsInt()) : null;
            skinMats = new float[list.size()][];
            for (int j = 0; j < list.size(); j++) {
                jointNodes[j] = list.get(j).getAsInt();
                float[] inv = ibm == null ? Mat4.identity() : Arrays.copyOfRange(ibm, j * 16, (j * 16) + 16);
                float[] g = this.globals[jointNodes[j]];
                skinMats[j] = Mat4.mul(g == null ? Mat4.identity() : g, inv);
            }
            js = this.file.ints(attrs.get("JOINTS_0").getAsInt());
            ws = this.file.floats(attrs.get("WEIGHTS_0").getAsInt());
        }
        float[] g2 = this.globals[node];
        int base = this.vertices;
        grow(n, idx.length / 3);
        for (int i3 = 0; i3 < n; i3++) {
            float x = ps[i3 * 3];
            float y = ps[(i3 * 3) + 1];
            float z = ps[(i3 * 3) + 2];
            float nx = ns == null ? 0.0f : ns[i3 * 3];
            float ny = ns == null ? 1.0f : ns[(i3 * 3) + 1];
            float nz = ns == null ? 0.0f : ns[(i3 * 3) + 2];
            int v = base + i3;
            Arrays.fill(this.influence, v * 4, (v * 4) + 4, -1);
            if (skinMats != null) {
                outP = new float[3];
                outN = new float[3];
                for (int k = 0; k < 4; k++) {
                    float w = ws[(i3 * 4) + k];
                    int j2 = js[(i3 * 4) + k];
                    if (w > 0.0f && j2 >= 0 && j2 < skinMats.length) {
                        float[] q = Mat4.point(skinMats[j2], x, y, z);
                        float[] d = Mat4.dir(skinMats[j2], nx, ny, nz);
                        for (int c = 0; c < 3; c++) {
                            int i4 = c;
                            outP[i4] = outP[i4] + (q[c] * w);
                            int i5 = c;
                            outN[i5] = outN[i5] + (d[c] * w);
                        }
                        this.influence[(v * 4) + k] = jointNodes[j2];
                        this.weight[(v * 4) + k] = w;
                    }
                }
            } else {
                outP = Mat4.point(g2, x, y, z);
                outN = Mat4.dir(g2, nx, ny, nz);
                this.influence[v * 4] = node;
                this.weight[v * 4] = 1.0f;
            }
            System.arraycopy(outP, 0, this.pos, v * 3, 3);
            Mat4.normalise(outN);
            System.arraycopy(outN, 0, this.nrm, v * 3, 3);
            this.uv[v * 2] = ts[i3 * 2];
            this.uv[(v * 2) + 1] = ts[(i3 * 2) + 1];
            this.material[v] = mat;
            this.color[v] = cs == null ? -1 : (-16777216) | (srgb(cs[i3 * cn]) << 16) | (srgb(cs[(i3 * cn) + 1]) << 8) | srgb(cs[(i3 * cn) + 2]);
        }
        for (int t = 0; t < idx.length / 3; t++) {
            for (int c2 = 0; c2 < 3; c2++) {
                this.tris[((this.triangles + t) * 3) + c2] = base + idx[(t * 3) + c2];
            }
        }
        int first = this.triangles;
        this.vertices += n;
        this.triangles += idx.length / 3;
        if (ns == null) {
            smoothNormals(base, n, first);
        }
    }

    private void smoothNormals(int base, int n, int firstTriangle) {
        Arrays.fill(this.nrm, base * 3, (base + n) * 3, 0.0f);
        for (int t = firstTriangle; t < this.triangles; t++) {
            float[] f = Mat4.faceNormal(this.pos, this.tris[t * 3], this.tris[(t * 3) + 1], this.tris[(t * 3) + 2]);
            for (int c = 0; c < 3; c++) {
                int v = this.tris[(t * 3) + c];
                for (int k = 0; k < 3; k++) {
                    float[] fArr = this.nrm;
                    int i = (v * 3) + k;
                    fArr[i] = fArr[i] + f[k];
                }
            }
        }
        for (int v2 = base; v2 < base + n; v2++) {
            float[] q = {this.nrm[v2 * 3], this.nrm[(v2 * 3) + 1], this.nrm[(v2 * 3) + 2]};
            Mat4.normalise(q);
            System.arraycopy(q, 0, this.nrm, v2 * 3, 3);
        }
    }

    private void grow(int moreVerts, int moreTris) {
        int nv = this.vertices + moreVerts;
        int nt = this.triangles + moreTris;
        this.pos = Arrays.copyOf(this.pos, nv * 3);
        this.nrm = Arrays.copyOf(this.nrm, nv * 3);
        this.uv = Arrays.copyOf(this.uv, nv * 2);
        this.influence = Arrays.copyOf(this.influence, nv * 4);
        this.weight = Arrays.copyOf(this.weight, nv * 4);
        this.material = Arrays.copyOf(this.material, nv);
        this.color = Arrays.copyOf(this.color, nv);
        this.tris = Arrays.copyOf(this.tris, nt * 3);
    }

    private static int srgb(float linear) {
        return Math.round(((float) Math.pow(Math.max(0.0f, Math.min(1.0f, linear)), 0.45454545454545453d)) * 255.0f);
    }

    void cluster(int target) {
        if (this.triangles <= target) {
            return;
        }
        float[] box = bounds();
        int[] map = new int[this.vertices];
        for (float cell = Math.max(box[3] - box[0], Math.max(box[4] - box[1], box[5] - box[2])) / 600.0f; weld(cell, map) > target; cell *= 1.25f) {
        }
        int kept = 0;
        for (int m : map) {
            kept = Math.max(kept, m + 1);
        }
        float[] p = new float[kept * 3];
        float[] n = new float[kept * 3];
        float[] t = new float[kept * 2];
        int[] count = new int[kept];
        int[] mat = new int[kept];
        int[] inf = new int[kept * 4];
        float[] w = new float[kept * 4];
        long[] rgb = new long[kept * 3];
        for (int v = 0; v < this.vertices; v++) {
            int m2 = map[v];
            int i = count[m2];
            count[m2] = i + 1;
            if (i == 0) {
                mat[m2] = this.material[v];
                System.arraycopy(this.influence, v * 4, inf, m2 * 4, 4);
                System.arraycopy(this.weight, v * 4, w, m2 * 4, 4);
            }
            for (int k = 0; k < 3; k++) {
                int i2 = (m2 * 3) + k;
                p[i2] = p[i2] + this.pos[(v * 3) + k];
                int i3 = (m2 * 3) + k;
                n[i3] = n[i3] + this.nrm[(v * 3) + k];
                int i4 = (m2 * 3) + k;
                rgb[i4] = rgb[i4] + ((long) ((this.color[v] >> (16 - (k * 8))) & 255));
            }
            int i5 = m2 * 2;
            t[i5] = t[i5] + this.uv[v * 2];
            int i6 = (m2 * 2) + 1;
            t[i6] = t[i6] + this.uv[(v * 2) + 1];
        }
        int[] c = new int[kept];
        for (int m3 = 0; m3 < kept; m3++) {
            for (int k2 = 0; k2 < 3; k2++) {
                int i7 = (m3 * 3) + k2;
                p[i7] = p[i7] / count[m3];
            }
            int i8 = m3 * 2;
            t[i8] = t[i8] / count[m3];
            int i9 = (m3 * 2) + 1;
            t[i9] = t[i9] / count[m3];
            float[] q = {n[m3 * 3], n[(m3 * 3) + 1], n[(m3 * 3) + 2]};
            Mat4.normalise(q);
            System.arraycopy(q, 0, n, m3 * 3, 3);
            c[m3] = (-16777216) | (((int) (rgb[m3 * 3] / ((long) count[m3]))) << 16) | (((int) (rgb[(m3 * 3) + 1] / ((long) count[m3]))) << 8) | ((int) (rgb[(m3 * 3) + 2] / ((long) count[m3])));
        }
        Set<Long> seen = new HashSet<>();
        int[] out = new int[this.triangles * 3];
        int corners = 0;
        for (int i10 = 0; i10 < this.triangles; i10++) {
            int a = map[this.tris[i10 * 3]];
            int b = map[this.tris[(i10 * 3) + 1]];
            int d = map[this.tris[(i10 * 3) + 2]];
            if (a != b && b != d && a != d && seen.add(Long.valueOf(key(a, b, d)))) {
                int i11 = corners;
                int corners2 = corners + 1;
                out[i11] = a;
                int corners3 = corners2 + 1;
                out[corners2] = b;
                corners = corners3 + 1;
                out[corners3] = d;
            }
        }
        this.pos = p;
        this.nrm = n;
        this.uv = t;
        this.material = mat;
        this.influence = inf;
        this.weight = w;
        this.color = c;
        this.tris = Arrays.copyOf(out, corners);
        this.vertices = kept;
        this.triangles = corners / 3;
    }

    private int weld(float cell, int[] map) {
        Map<String, Integer> cells = new HashMap<>();
        for (int v = 0; v < this.vertices; v++) {
            String key = ((int) Math.floor(this.pos[v * 3] / cell)) + "," + ((int) Math.floor(this.pos[(v * 3) + 1] / cell)) + "," + ((int) Math.floor(this.pos[(v * 3) + 2] / cell)) + "," + this.material[v] + "," + Math.round(this.uv[v * 2] * 64.0f) + "," + Math.round(this.uv[(v * 2) + 1] * 64.0f);
            Integer id = cells.putIfAbsent(key, Integer.valueOf(cells.size()));
            map[v] = id == null ? cells.size() - 1 : id.intValue();
        }
        Set<Long> seen = new HashSet<>();
        for (int i = 0; i < this.triangles; i++) {
            int a = map[this.tris[i * 3]];
            int b = map[this.tris[(i * 3) + 1]];
            int c = map[this.tris[(i * 3) + 2]];
            if (a != b && b != c && a != c) {
                seen.add(Long.valueOf(key(a, b, c)));
            }
        }
        return seen.size();
    }

    private static long key(int a, int b, int c) {
        int lo = Math.min(a, Math.min(b, c));
        int hi = Math.max(a, Math.max(b, c));
        int mid = (((a + b) + c) - lo) - hi;
        return (((((long) lo) * 2654435761L) + ((long) mid)) * 2246822519L) + ((long) hi);
    }

    int[] separate(int[] part) {
        Map<Long, Integer> copies = new HashMap<>();
        List<Integer> from = new ArrayList<>();
        List<Integer> owners = new ArrayList<>();
        for (int t = 0; t < this.triangles; t++) {
            int owner = CharacterBake.owner(part, this.tris[t * 3], this.tris[(t * 3) + 1], this.tris[(t * 3) + 2]);
            for (int c = 0; c < 3; c++) {
                int v = this.tris[(t * 3) + c];
                if (part[v] != owner) {
                    Integer copy = copies.get(Long.valueOf((((long) v) * 8) + ((long) owner)));
                    if (copy == null) {
                        copy = Integer.valueOf(this.vertices + from.size());
                        copies.put(Long.valueOf((((long) v) * 8) + ((long) owner)), copy);
                        from.add(Integer.valueOf(v));
                        owners.add(Integer.valueOf(owner));
                    }
                    this.tris[(t * 3) + c] = copy.intValue();
                }
            }
        }
        int first = this.vertices;
        grow(from.size(), 0);
        int[] grown = Arrays.copyOf(part, first + from.size());
        for (int i = 0; i < from.size(); i++) {
            int v2 = from.get(i).intValue();
            int copy2 = first + i;
            System.arraycopy(this.pos, v2 * 3, this.pos, copy2 * 3, 3);
            System.arraycopy(this.nrm, v2 * 3, this.nrm, copy2 * 3, 3);
            System.arraycopy(this.uv, v2 * 2, this.uv, copy2 * 2, 2);
            System.arraycopy(this.influence, v2 * 4, this.influence, copy2 * 4, 4);
            System.arraycopy(this.weight, v2 * 4, this.weight, copy2 * 4, 4);
            this.material[copy2] = this.material[v2];
            this.color[copy2] = this.color[v2];
            grown[copy2] = owners.get(i).intValue();
        }
        this.vertices += from.size();
        return grown;
    }

    void normalise(float height, boolean flip) {
        this.flipped = flip;
        if (flip) {
            for (int v = 0; v < this.vertices; v++) {
                this.pos[v * 3] = -this.pos[v * 3];
                this.pos[(v * 3) + 2] = -this.pos[(v * 3) + 2];
                this.nrm[v * 3] = -this.nrm[v * 3];
                this.nrm[(v * 3) + 2] = -this.nrm[(v * 3) + 2];
            }
        }
        float[] box = bounds();
        this.scale = height / Math.max(1.0E-6f, box[4] - box[1]);
        this.shiftX = (-(box[0] + box[3])) * 0.5f;
        this.shiftY = -box[1];
        this.shiftZ = (-(box[2] + box[5])) * 0.5f;
        for (int v2 = 0; v2 < this.vertices; v2++) {
            this.pos[v2 * 3] = (this.pos[v2 * 3] + this.shiftX) * this.scale;
            this.pos[(v2 * 3) + 1] = (this.pos[(v2 * 3) + 1] + this.shiftY) * this.scale;
            this.pos[(v2 * 3) + 2] = (this.pos[(v2 * 3) + 2] + this.shiftZ) * this.scale;
        }
    }

    float[] bounds() {
        float[] b = new float[6];
        b[0] = Float.MAX_VALUE;
        b[1] = Float.MAX_VALUE;
        b[2] = Float.MAX_VALUE;
        b[3] = -3.4028235E38f;
        b[4] = -3.4028235E38f;
        b[5] = -3.4028235E38f;
        for (int v = 0; v < this.vertices; v++) {
            for (int k = 0; k < 3; k++) {
                b[k] = Math.min(b[k], this.pos[(v * 3) + k]);
                b[k + 3] = Math.max(b[k + 3], this.pos[(v * 3) + k]);
            }
        }
        return b;
    }

    float[] world(int node) {
        float[] g = this.globals[node];
        return g == null ? new float[3] : Mat4.point(g, 0.0f, 0.0f, 0.0f);
    }

    float[] joint(int node) {
        float[] p = world(node);
        float x = this.flipped ? -p[0] : p[0];
        float z = this.flipped ? -p[2] : p[2];
        return new float[]{(x + this.shiftX) * this.scale, (p[1] + this.shiftY) * this.scale, (z + this.shiftZ) * this.scale};
    }

    boolean under(int node, int ancestor) {
        int n = node;
        for (int steps = 0; n >= 0 && steps <= this.parent.length; steps++) {
            if (n == ancestor) {
                return true;
            }
            n = this.parent[n];
        }
        return false;
    }

    float[] below(int joint) {
        float[] out = new float[this.vertices];
        for (int v = 0; v < this.vertices; v++) {
            for (int k = 0; k < 4; k++) {
                int n = this.influence[(v * 4) + k];
                if (n >= 0 && under(n, joint)) {
                    int i = v;
                    out[i] = out[i] + this.weight[(v * 4) + k];
                }
            }
        }
        return out;
    }
}
