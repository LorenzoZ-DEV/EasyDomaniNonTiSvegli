package net.anubis.client.util.seedscan;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1959;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2794;
import net.minecraft.class_2893;
import net.minecraft.class_2975;
import net.minecraft.class_3031;
import net.minecraft.class_3124;
import net.minecraft.class_3481;
import net.minecraft.class_3532;
import net.minecraft.class_3825;
import net.minecraft.class_6796;
import net.minecraft.class_6797;
import net.minecraft.class_6862;
import net.minecraft.class_6880;
import net.minecraft.class_7510;
import net.minecraft.class_7923;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/OreSpec.class */
@Environment(EnvType.CLIENT)
public final class OreSpec extends Record {
    private final String id;
    private final int step;
    private final int index;
    private final int order;
    private final int countMin;
    private final int countMax;
    private final int rarity;
    private final boolean trapezoid;
    private final int yMin;
    private final int yMax;
    private final int plateau;
    private final int size;
    private final float discard;
    private final byte kind;
    private final Set<class_6880<class_1959>> biomes;
    private static final int[] STEPS = {class_2893.class_2895.field_13176.ordinal(), class_2893.class_2895.field_13177.ordinal()};

    public OreSpec(String id, int step, int index, int order, int countMin, int countMax, int rarity, boolean trapezoid, int yMin, int yMax, int plateau, int size, float discard, byte kind, Set<class_6880<class_1959>> biomes) {
        this.id = id;
        this.step = step;
        this.index = index;
        this.order = order;
        this.countMin = countMin;
        this.countMax = countMax;
        this.rarity = rarity;
        this.trapezoid = trapezoid;
        this.yMin = yMin;
        this.yMax = yMax;
        this.plateau = plateau;
        this.size = size;
        this.discard = discard;
        this.kind = kind;
        this.biomes = biomes;
    }

    @Override // java.lang.Record
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, OreSpec.class), OreSpec.class, "id;step;index;order;countMin;countMax;rarity;trapezoid;yMin;yMax;plateau;size;discard;kind;biomes", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->step:I", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->index:I", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->order:I", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->countMin:I", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->countMax:I", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->rarity:I", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->trapezoid:Z", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->yMin:I", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->yMax:I", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->plateau:I", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->size:I", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->discard:F", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->kind:B", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->biomes:Ljava/util/Set;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final boolean equals(Object o) {
        return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, OreSpec.class, Object.class), OreSpec.class, "id;step;index;order;countMin;countMax;rarity;trapezoid;yMin;yMax;plateau;size;discard;kind;biomes", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->step:I", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->index:I", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->order:I", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->countMin:I", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->countMax:I", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->rarity:I", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->trapezoid:Z", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->yMin:I", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->yMax:I", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->plateau:I", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->size:I", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->discard:F", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->kind:B", "FIELD:Lnet/anubis/client/util/seedscan/OreSpec;->biomes:Ljava/util/Set;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
    }

    public String id() {
        return this.id;
    }

    public int step() {
        return this.step;
    }

    public int index() {
        return this.index;
    }

    public int order() {
        return this.order;
    }

    public int countMin() {
        return this.countMin;
    }

    public int countMax() {
        return this.countMax;
    }

    public int rarity() {
        return this.rarity;
    }

    public boolean trapezoid() {
        return this.trapezoid;
    }

    public int yMin() {
        return this.yMin;
    }

    public int yMax() {
        return this.yMax;
    }

    public int plateau() {
        return this.plateau;
    }

    public int size() {
        return this.size;
    }

    public float discard() {
        return this.discard;
    }

    public byte kind() {
        return this.kind;
    }

    public Set<class_6880<class_1959>> biomes() {
        return this.biomes;
    }

    public int reach() {
        return class_3532.method_15386((((this.size / 16.0f) * 2.0f) + 1.0f) / 2.0f);
    }

    public int halfWidth() {
        return class_3532.method_15386(this.size / 8.0f) + reach();
    }

    public boolean drawsOnTerrain() {
        return this.discard > 0.0f && this.discard < 1.0f;
    }

    int sampleCount(WgRandom random) {
        if (this.rarity > 0) {
            return random.nextFloat() < 1.0f / ((float) this.rarity) ? 1 : 0;
        }
        return this.countMin == this.countMax ? this.countMin : random.between(this.countMin, this.countMax);
    }

    int sampleY(WgRandom random) {
        int k;
        if (this.yMin > this.yMax) {
            return this.yMin;
        }
        if (this.trapezoid && this.plateau < (k = this.yMax - this.yMin)) {
            int l = (k - this.plateau) / 2;
            int m = k - l;
            return this.yMin + random.between(0, m) + random.between(0, l);
        }
        return random.between(this.yMin, this.yMax);
    }

    boolean inBiome(class_6880<class_1959> biome) {
        return this.biomes == null || this.biomes.contains(biome);
    }

    @Override // java.lang.Record
    public String toString() {
        return this.id + "(" + this.step + ":" + this.index + ")";
    }

    static List<OreSpec> derive(List<class_7510.class_6827> steps, List<class_6880<class_1959>> biomes, class_2794 generator, Function<class_6796, String> names, int minY, int maxY) {
        checkTags();
        List<OreSpec> specs = new ArrayList<>();
        for (int step : STEPS) {
            if (step < steps.size()) {
                List<class_6796> features = steps.get(step).comp_303();
                for (int index = 0; index < features.size(); index++) {
                    class_6796 placed = features.get(index);
                    class_2975<?, ?> configured = (class_2975) placed.comp_334().comp_349();
                    if (configured.comp_332() == class_3031.field_13517) {
                        class_3124 config = configured.comp_333();
                        String id = names.apply(placed);
                        Set<class_6880<class_1959>> carriers = Collections.newSetFromMap(new IdentityHashMap());
                        for (class_6880<class_1959> biome : biomes) {
                            if (generator.method_44216(biome).method_38105(placed)) {
                                carriers.add(biome);
                            }
                        }
                        specs.add(read(id, step, index, specs.size(), placed, config, carriers.size() == biomes.size() ? null : Set.copyOf(carriers), minY, maxY));
                    }
                }
            }
        }
        return List.copyOf(specs);
    }

    private static OreSpec read(String id, int step, int index, int order, class_6796 placed, class_3124 config, Set<class_6880<class_1959>> biomes, int minY, int maxY) {
        int countMax;
        int countMin;
        boolean z;
        List<class_6797> mods = placed.comp_335();
        if (mods.size() != 4) {
            throw unsupported(id, "placement " + mods.size() + " modifiers");
        }
        JsonObject first = json(id, mods.get(0));
        int rarity = 0;
        switch (type(first)) {
            case "minecraft:count":
                JsonElement count = first.get("count");
                if (count.isJsonPrimitive()) {
                    int asInt = count.getAsInt();
                    countMax = asInt;
                    countMin = asInt;
                    break;
                } else {
                    JsonObject provider = count.getAsJsonObject();
                    if (!type(provider).equals("minecraft:uniform")) {
                        throw unsupported(id, "count " + String.valueOf(provider));
                    }
                    countMin = provider.get("min_inclusive").getAsInt();
                    countMax = provider.get("max_inclusive").getAsInt();
                    break;
                }
                break;
            case "minecraft:rarity_filter":
                countMax = 1;
                countMin = 1;
                rarity = first.get("chance").getAsInt();
                break;
            default:
                throw unsupported(id, "first modifier " + String.valueOf(first));
        }
        if (!type(json(id, mods.get(1))).equals("minecraft:in_square")) {
            throw unsupported(id, "second modifier");
        }
        JsonObject range = json(id, mods.get(2));
        if (!type(range).equals("minecraft:height_range")) {
            throw unsupported(id, "third modifier");
        }
        if (!type(json(id, mods.get(3))).equals("minecraft:biome")) {
            throw unsupported(id, "fourth modifier");
        }
        JsonObject height = range.getAsJsonObject("height");
        switch (type(height)) {
            case "minecraft:uniform":
                z = false;
                break;
            case "minecraft:trapezoid":
                z = true;
                break;
            default:
                throw unsupported(id, "height " + String.valueOf(height));
        }
        boolean trapezoid = z;
        int plateau = height.has("plateau") ? height.get("plateau").getAsInt() : 0;
        int yMin = anchor(id, height.getAsJsonObject("min_inclusive"), minY, maxY);
        int yMax = anchor(id, height.getAsJsonObject("max_inclusive"), minY, maxY);
        return new OreSpec(id, step, index, order, countMin, countMax, rarity, trapezoid, yMin, yMax, plateau, config.field_13723, config.field_29064, kindOf(id, config), biomes);
    }

    private static int anchor(String id, JsonObject anchor, int minY, int maxY) {
        if (anchor.has("absolute")) {
            return anchor.get("absolute").getAsInt();
        }
        if (anchor.has("above_bottom")) {
            return minY + anchor.get("above_bottom").getAsInt();
        }
        if (anchor.has("below_top")) {
            return maxY - anchor.get("below_top").getAsInt();
        }
        throw unsupported(id, "anchor " + String.valueOf(anchor));
    }

    private static byte kindOf(String id, class_3124 config) {
        Set<String> tags = new HashSet<>();
        byte kind = -1;
        for (class_3124.class_5876 target : config.field_29063) {
            JsonObject test = ((JsonElement) class_3825.field_25012.encodeStart(JsonOps.INSTANCE, target.field_29068).getOrThrow()).getAsJsonObject();
            if (!test.get("predicate_type").getAsString().equals("minecraft:tag_match")) {
                throw unsupported(id, "target " + String.valueOf(test));
            }
            tags.add(test.get("tag").getAsString());
            byte written = VBlock.of(target.field_29069);
            if (written == 12 || written == 0 || (kind != -1 && kind != written)) {
                throw unsupported(id, "writes " + String.valueOf(target.field_29069));
            }
            kind = written;
        }
        boolean covered = tags.equals(Set.of("minecraft:base_stone_overworld")) || tags.equals(Set.of("minecraft:stone_ore_replaceables", "minecraft:deepslate_ore_replaceables"));
        if (!covered || kind == -1) {
            throw unsupported(id, "targets " + String.valueOf(tags));
        }
        return kind;
    }

    static void checkTags() {
        expect(class_3481.field_25806, class_2246.field_10340, class_2246.field_10474, class_2246.field_10508, class_2246.field_10115, class_2246.field_27165, class_2246.field_28888);
        expect(class_3481.field_28992, class_2246.field_10340, class_2246.field_10474, class_2246.field_10508, class_2246.field_10115);
        expect(class_3481.field_28993, class_2246.field_28888, class_2246.field_27165);
    }

    private static void expect(class_6862<class_2248> tag, class_2248... blocks) {
        Set<class_2248> want = Collections.newSetFromMap(new IdentityHashMap());
        Collections.addAll(want, blocks);
        Set<class_2248> have = Collections.newSetFromMap(new IdentityHashMap());
        for (class_6880<class_2248> holder : class_7923.field_41175.method_40286(tag)) {
            have.add((class_2248) holder.comp_349());
        }
        if (!have.equals(want)) {
            throw new IllegalStateException("block tag " + String.valueOf(tag) + " is not vanilla's: " + String.valueOf(have));
        }
    }

    private static JsonObject json(String id, class_6797 modifier) {
        try {
            return ((JsonElement) class_6797.field_35736.encodeStart(JsonOps.INSTANCE, modifier).getOrThrow()).getAsJsonObject();
        } catch (RuntimeException e) {
            throw unsupported(id, "modifier " + String.valueOf(modifier) + ": " + e.getMessage());
        }
    }

    private static String type(JsonObject object) {
        JsonElement type = object.get("type");
        return type == null ? "" : type.getAsString();
    }

    private static IllegalStateException unsupported(String id, String what) {
        return new IllegalStateException("feature " + id + " is not placed like a vanilla ore: " + what);
    }
}
