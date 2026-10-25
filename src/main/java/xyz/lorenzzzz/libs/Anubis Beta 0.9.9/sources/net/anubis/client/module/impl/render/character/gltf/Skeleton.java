package net.anubis.client.module.impl.render.character.gltf;

import com.google.gson.JsonElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.IntPredicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/gltf/Skeleton.class */
@Environment(EnvType.CLIENT)
final class Skeleton {
    static final int RIGHT = 0;
    static final int LEFT = 1;
    private static final Set<String> NOISE = Set.of((Object[]) new String[]{"j", "bip", "bip01", "def", "deform", "mch", "org", "jnt", "jt", "bone", "bn", "b", "c", "x", "stretch", "valve", "biped", "mixamorig"});
    private static final Set<String> LEFT_WORDS = Set.of("left", "l", "lf", "lft");
    private static final Set<String> RIGHT_WORDS = Set.of("right", "r", "rt", "rgt");
    private static final List<String> UPPER_ARM = List.of("upperarm", "uparm", "armupper");
    private static final List<String> FOREARM = List.of("forearm", "lowerarm", "elbow", "armlower", "loarm");
    private static final List<String> HAND = List.of("hand", "wrist");
    private static final List<String> THIGH = List.of("thigh", "upleg", "upperleg", "legupper");
    private static final List<String> SHIN = List.of("calf", "shin", "knee", "lowerleg", "leglower", "loleg");
    int head = -1;
    final int[] upperArm = {-1, -1};
    final int[] forearm = {-1, -1};
    final int[] hand = {-1, -1};
    final int[] thigh = {-1, -1};
    final int[] shin = {-1, -1};
    boolean facesBack;
    private final PosedMesh mesh;
    private final String[] core;
    private final int[] named;

    private Skeleton(PosedMesh mesh) {
        this.mesh = mesh;
        int n = mesh.file.nodes();
        this.core = new String[n];
        this.named = new int[n];
        for (int i = 0; i < n; i++) {
            List<String> words = words(mesh.file.nodeName(i));
            this.named[i] = -1;
            StringBuilder b = new StringBuilder();
            for (String w : words) {
                if (LEFT_WORDS.contains(w)) {
                    this.named[i] = 1;
                } else if (RIGHT_WORDS.contains(w)) {
                    this.named[i] = 0;
                } else if (!NOISE.contains(w)) {
                    b.append(w);
                }
            }
            this.core[i] = b.toString().replaceAll("\\d+$", "");
        }
    }

    static Skeleton find(PosedMesh mesh, Map<String, String> bones) throws IOException {
        Skeleton s = new Skeleton(mesh);
        if (!bones.isEmpty()) {
            s.head = s.named(bones, "head");
            int side = 0;
            while (side < 2) {
                String w = side == 0 ? "right " : "left ";
                s.upperArm[side] = s.named(bones, w + "arm");
                s.forearm[side] = s.named(bones, w + "elbow");
                s.hand[side] = s.named(bones, w + "hand");
                s.thigh[side] = s.named(bones, w + "leg");
                s.shin[side] = s.named(bones, w + "knee");
                side++;
            }
            s.facesBack = s.upperArm[1] >= 0 && s.upperArm[0] >= 0 && mesh.world(s.upperArm[1])[0] < mesh.world(s.upperArm[0])[0];
            return s;
        }
        s.facesBack = s.facingBack();
        s.head = s.shallowest(n -> {
            return s.core[n].startsWith("head");
        });
        for (int side2 = 0; side2 < 2; side2++) {
            int sd = side2;
            int namedElbow = s.shallowest(n2 -> {
                return s.side(n2) == sd && s.is(n2, FOREARM);
            });
            int elbow = namedElbow;
            int arm = s.shallowest(n3 -> {
                return s.side(n3) == sd && (s.is(n3, UPPER_ARM) || s.core[n3].equals("arm")) && (namedElbow < 0 || (mesh.under(namedElbow, n3) && n3 != namedElbow));
            });
            if (arm < 0 && elbow >= 0) {
                arm = mesh.parent[elbow];
            }
            if (elbow < 0 && arm >= 0) {
                elbow = s.mainChild(arm);
            }
            s.upperArm[sd] = arm;
            s.forearm[sd] = elbow;
            if (elbow >= 0) {
                int e = elbow;
                int wrist = s.shallowest(n4 -> {
                    return n4 != e && mesh.under(n4, e) && s.is(n4, HAND);
                });
                s.hand[sd] = wrist >= 0 ? wrist : s.mainChild(e);
            }
            int namedKnee = s.shallowest(n5 -> {
                return s.side(n5) == sd && s.is(n5, SHIN);
            });
            int knee = namedKnee;
            int leg = s.shallowest(n6 -> {
                return s.side(n6) == sd && (s.is(n6, THIGH) || s.core[n6].equals("leg")) && (namedKnee < 0 || (mesh.under(namedKnee, n6) && n6 != namedKnee));
            });
            if (knee < 0 && leg >= 0) {
                knee = s.shallowest(n7 -> {
                    return n7 != leg && mesh.under(n7, leg) && s.core[n7].equals("leg");
                });
                if (knee < 0) {
                    knee = s.mainChild(leg);
                }
            }
            if (leg < 0 && knee >= 0) {
                leg = mesh.parent[knee];
            }
            s.thigh[sd] = leg;
            s.shin[sd] = knee;
        }
        return s;
    }

    private boolean is(int node, List<String> words) {
        for (String w : words) {
            if (this.core[node].contains(w)) {
                return true;
            }
        }
        return false;
    }

    private int named(Map<String, String> bones, String key) throws IOException {
        String name = bones.get(key);
        if (name == null) {
            return -1;
        }
        for (int i = 0; i < this.core.length; i++) {
            if (this.mesh.file.nodeName(i).equalsIgnoreCase(name)) {
                return i;
            }
        }
        String lower = name.toLowerCase(Locale.ROOT);
        for (int i2 = 0; i2 < this.core.length; i2++) {
            if (this.mesh.file.nodeName(i2).toLowerCase(Locale.ROOT).startsWith(lower)) {
                return i2;
            }
        }
        throw new IOException("no bone named " + name);
    }

    private static List<String> words(String name) {
        String n = name.replaceAll("_\\d+$", "").replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase(Locale.ROOT).replace("手首", " wrist ").replace("足首", " ankle ").replace("左", " left ").replace("右", " right ").replace("ひじ", " elbow ").replace("ひざ", " knee ").replace("腕", " arm ").replace("足", " leg ").replace("頭", " head ").replace("首", " neck ");
        List<String> out = new ArrayList<>();
        for (String w : n.split("[^a-z0-9]+")) {
            if (!w.isEmpty()) {
                out.add(w);
            }
        }
        return out;
    }

    private boolean facingBack() {
        int left = shallowest(n -> {
            return this.named[n] == 1 && is(n, HAND);
        });
        int right = shallowest(n2 -> {
            return this.named[n2] == 0 && is(n2, HAND);
        });
        if (left < 0 || right < 0) {
            left = shallowest(n3 -> {
                return this.named[n3] == 1 && is(n3, SHIN);
            });
            right = shallowest(n4 -> {
                return this.named[n4] == 0 && is(n4, SHIN);
            });
        }
        if (left >= 0 && right >= 0) {
            return this.mesh.world(left)[0] < this.mesh.world(right)[0];
        }
        float[] box = this.mesh.bounds();
        float h = box[4] - box[1];
        float feet = 0.0f;
        float shins = 0.0f;
        int nf = 0;
        int ns = 0;
        for (int v = 0; v < this.mesh.vertices; v++) {
            float y = this.mesh.pos[(v * 3) + 1] - box[1];
            float z = this.mesh.pos[(v * 3) + 2];
            if (y < h * 0.04f) {
                feet += z;
                nf++;
            } else if (y > h * 0.1f && y < h * 0.2f) {
                shins += z;
                ns++;
            }
        }
        return nf > 0 && ns > 0 && feet / ((float) nf) < (shins / ((float) ns)) - (h * 0.01f);
    }

    private int side(int node) {
        if (this.named[node] >= 0) {
            return this.named[node];
        }
        return ((this.mesh.world(node)[0] > 0.0f ? 1 : (this.mesh.world(node)[0] == 0.0f ? 0 : -1)) < 0) != this.facesBack ? 0 : 1;
    }

    private int shallowest(IntPredicate test) {
        int best = -1;
        int bestDepth = Integer.MAX_VALUE;
        Iterator<Integer> it = this.mesh.joints.iterator();
        while (it.hasNext()) {
            int n = it.next().intValue();
            if (test.test(n)) {
                int depth = 0;
                int i = this.mesh.parent[n];
                while (true) {
                    int p = i;
                    if (p < 0 || depth > this.core.length) {
                        break;
                    }
                    depth++;
                    i = this.mesh.parent[p];
                }
                if (depth < bestDepth || (depth == bestDepth && n < best)) {
                    best = n;
                    bestDepth = depth;
                }
            }
        }
        return best;
    }

    private int mainChild(int node) {
        int best = -1;
        int bestSize = -1;
        for (JsonElement c : GltfFile.array(this.mesh.file.node(node), "children")) {
            int child = c.getAsInt();
            int size = 0;
            for (int n = 0; n < this.core.length; n++) {
                if (this.mesh.under(n, child)) {
                    size++;
                }
            }
            if (size > bestSize) {
                best = child;
                bestSize = size;
            }
        }
        return best;
    }

    Map<Integer, Float> armTurns() {
        Map<Integer, Float> turns = new HashMap<>();
        for (int s = 0; s < 2; s++) {
            if (this.upperArm[s] >= 0 && this.forearm[s] >= 0) {
                float[] a = this.mesh.world(this.upperArm[s]);
                float[] b = this.mesh.world(this.forearm[s]);
                float dx = b[0] - a[0];
                float dy = b[1] - a[1];
                float angle = (float) Math.toDegrees(Math.atan2(Math.abs(dx), -dy));
                if (angle > 40.0f) {
                    turns.put(Integer.valueOf(this.upperArm[s]), Float.valueOf((dx < 0.0f ? 1.0f : -1.0f) * (angle - 10.0f)));
                }
            }
        }
        return turns;
    }

    int[] groups() {
        int[] out = new int[this.core.length];
        for (int n = 0; n < out.length; n++) {
            out[n] = 1;
            if (this.head >= 0 && this.mesh.under(n, this.head)) {
                out[n] = 0;
            } else if (this.upperArm[0] >= 0 && this.mesh.under(n, this.upperArm[0])) {
                out[n] = 2;
            } else if (this.upperArm[1] >= 0 && this.mesh.under(n, this.upperArm[1])) {
                out[n] = 3;
            } else if (this.thigh[0] >= 0 && this.mesh.under(n, this.thigh[0])) {
                out[n] = 4;
            } else if (this.thigh[1] >= 0 && this.mesh.under(n, this.thigh[1])) {
                out[n] = 5;
            }
        }
        return out;
    }

    boolean found() {
        return this.head >= 0 || this.upperArm[0] >= 0 || this.upperArm[1] >= 0 || this.thigh[0] >= 0 || this.thigh[1] >= 0;
    }

    String describe() {
        StringBuilder b = new StringBuilder();
        b.append("facing ").append(this.facesBack ? "-z (turned around)" : "+z").append('\n');
        line(b, "head", this.head);
        int s = 0;
        while (s < 2) {
            String w = s == 0 ? "right " : "left ";
            line(b, w + "arm", this.upperArm[s]);
            line(b, w + "elbow", this.forearm[s]);
            line(b, w + "hand", this.hand[s]);
            line(b, w + "leg", this.thigh[s]);
            line(b, w + "knee", this.shin[s]);
            s++;
        }
        return b.toString();
    }

    private void line(StringBuilder b, String role, int node) {
        Locale locale = Locale.ROOT;
        Object[] objArr = new Object[2];
        objArr[0] = role;
        objArr[1] = node < 0 ? "-" : this.mesh.file.nodeName(node);
        b.append(String.format(locale, "%-11s %s%n", objArr));
    }
}
