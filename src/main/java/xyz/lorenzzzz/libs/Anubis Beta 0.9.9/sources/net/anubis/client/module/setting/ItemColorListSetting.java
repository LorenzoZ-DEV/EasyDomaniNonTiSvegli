package net.anubis.client.module.setting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import imgui.type.ImBoolean;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1792;
import net.minecraft.class_2960;
import net.minecraft.class_7923;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/setting/ItemColorListSetting.class */
@Environment(EnvType.CLIENT)
public final class ItemColorListSetting extends Setting {
    private List<ItemEntry> entries;
    private Supplier<List<ItemEntry>> entriesSupplier;
    private final float[] defaultColor;
    private Map<String, Integer> enabledLookup;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/setting/ItemColorListSetting$ItemEntry.class */
    @Environment(EnvType.CLIENT)
    public static final class ItemEntry extends Record {
        private final String id;
        private final String displayName;
        private final ImBoolean enabled;
        private final float[] color;

        public ItemEntry(String id, String displayName, ImBoolean enabled, float[] color) {
            this.id = id;
            this.displayName = displayName;
            this.enabled = enabled;
            this.color = color;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, ItemEntry.class), ItemEntry.class, "id;displayName;enabled;color", "FIELD:Lnet/anubis/client/module/setting/ItemColorListSetting$ItemEntry;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/setting/ItemColorListSetting$ItemEntry;->displayName:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/setting/ItemColorListSetting$ItemEntry;->enabled:Limgui/type/ImBoolean;", "FIELD:Lnet/anubis/client/module/setting/ItemColorListSetting$ItemEntry;->color:[F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, ItemEntry.class), ItemEntry.class, "id;displayName;enabled;color", "FIELD:Lnet/anubis/client/module/setting/ItemColorListSetting$ItemEntry;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/setting/ItemColorListSetting$ItemEntry;->displayName:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/setting/ItemColorListSetting$ItemEntry;->enabled:Limgui/type/ImBoolean;", "FIELD:Lnet/anubis/client/module/setting/ItemColorListSetting$ItemEntry;->color:[F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, ItemEntry.class, Object.class), ItemEntry.class, "id;displayName;enabled;color", "FIELD:Lnet/anubis/client/module/setting/ItemColorListSetting$ItemEntry;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/setting/ItemColorListSetting$ItemEntry;->displayName:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/setting/ItemColorListSetting$ItemEntry;->enabled:Limgui/type/ImBoolean;", "FIELD:Lnet/anubis/client/module/setting/ItemColorListSetting$ItemEntry;->color:[F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public String id() {
            return this.id;
        }

        public String displayName() {
            return this.displayName;
        }

        public ImBoolean enabled() {
            return this.enabled;
        }

        public float[] color() {
            return this.color;
        }
    }

    public ItemColorListSetting(String name, List<ItemEntry> entries, float[] defaultColor) {
        super(name);
        this.entries = new ArrayList(entries);
        this.defaultColor = defaultColor;
        onChange(this::invalidateLookup);
    }

    public ItemColorListSetting(String name, Supplier<List<ItemEntry>> entriesSupplier, float[] defaultColor) {
        super(name);
        this.entriesSupplier = entriesSupplier;
        this.defaultColor = defaultColor;
        onChange(this::invalidateLookup);
    }

    private List<ItemEntry> entriesInternal() {
        if (this.entries == null) {
            this.entries = new ArrayList(this.entriesSupplier.get());
            this.entriesSupplier = null;
        }
        return this.entries;
    }

    public List<ItemEntry> entries() {
        return Collections.unmodifiableList(entriesInternal());
    }

    public float[] defaultColor() {
        return this.defaultColor;
    }

    public void invalidateLookup() {
        this.enabledLookup = null;
    }

    private Map<String, Integer> lookup() {
        if (this.enabledLookup == null) {
            rebuildLookup();
        }
        return this.enabledLookup;
    }

    private void rebuildLookup() {
        Map<String, Integer> map = new HashMap<>();
        for (ItemEntry entry : entriesInternal()) {
            if (entry.enabled().get()) {
                map.put(entry.id(), Integer.valueOf(rgbaToArgb(entry.color())));
            }
        }
        this.enabledLookup = map;
    }

    public boolean isEnabled(class_1792 item) {
        class_2960 key = class_7923.field_41178.method_10221(item);
        if (key == null) {
            return false;
        }
        return lookup().containsKey(key.toString());
    }

    public int colorArgb(class_1792 item) {
        Integer color;
        class_2960 key = class_7923.field_41178.method_10221(item);
        if (key != null && (color = lookup().get(key.toString())) != null) {
            return color.intValue();
        }
        return rgbaToArgb(this.defaultColor);
    }

    public boolean hasAnyEnabled() {
        return !lookup().isEmpty();
    }

    @Override // net.anubis.client.module.setting.Setting
    public JsonElement serialize() {
        JsonObject root = new JsonObject();
        JsonArray defCol = new JsonArray();
        for (float v : this.defaultColor) {
            defCol.add(Float.valueOf(clamp01(v)));
        }
        root.add("defaultColor", defCol);
        JsonArray arr = new JsonArray();
        for (ItemEntry e : entriesInternal()) {
            JsonObject obj = new JsonObject();
            obj.addProperty("id", e.id());
            obj.addProperty("enabled", Boolean.valueOf(e.enabled().get()));
            JsonArray col = new JsonArray();
            for (float v2 : e.color()) {
                col.add(Float.valueOf(clamp01(v2)));
            }
            obj.add("color", col);
            arr.add(obj);
        }
        root.add("items", arr);
        return root;
    }

    @Override // net.anubis.client.module.setting.Setting
    public JsonElement compact(JsonElement value) {
        JsonElement defaults = defaultJson();
        if (value == null || !value.isJsonObject() || defaults == null || !defaults.isJsonObject()) {
            return value;
        }
        JsonObject root = value.getAsJsonObject();
        JsonObject shipped = defaults.getAsJsonObject();
        if (root.has("changes") || !root.has("items") || !shipped.has("items")) {
            return value;
        }
        try {
            Map<String, String> before = new HashMap<>();
            Iterator it = shipped.getAsJsonArray("items").iterator();
            while (it.hasNext()) {
                JsonObject obj = ((JsonElement) it.next()).getAsJsonObject();
                before.put(obj.get("id").getAsString(), packItem(obj));
            }
            JsonObject changes = new JsonObject();
            for (JsonElement el : root.getAsJsonArray("items")) {
                if (el.isJsonObject() && el.getAsJsonObject().has("id")) {
                    JsonObject obj2 = el.getAsJsonObject();
                    String id = obj2.get("id").getAsString();
                    String now = packItem(obj2);
                    String was = before.get(id);
                    if (!now.equals(was)) {
                        boolean sameColor = was != null && now.substring(2).equals(was.substring(2));
                        changes.addProperty(BlockColorListSetting.shortId(id), sameColor ? now.substring(0, 1) : now);
                    }
                }
            }
            JsonObject out = new JsonObject();
            String color = BlockColorListSetting.hexColor(root.get("defaultColor"));
            if (color != null && !color.equals(BlockColorListSetting.hexColor(shipped.get("defaultColor")))) {
                out.addProperty("defaultColor", color);
            }
            out.add("changes", changes);
            return out;
        } catch (RuntimeException e) {
            return value;
        }
    }

    private static String packItem(JsonObject obj) {
        boolean on = obj.has("enabled") && obj.get("enabled").getAsBoolean();
        return (on ? "1" : "0") + " " + BlockColorListSetting.hexColor(obj.get("color"));
    }

    @Override // net.anubis.client.module.setting.Setting
    protected void deserializeValue(JsonElement element) {
        if (element == null || !element.isJsonObject()) {
            return;
        }
        try {
            JsonObject root = element.getAsJsonObject();
            if (root.has("changes") && root.get("changes").isJsonObject()) {
                applyChanges(root);
            } else {
                applyFull(root);
            }
        } catch (Exception e) {
        }
    }

    private void applyFull(JsonObject root) {
        ItemEntry entry;
        if (root.has("defaultColor")) {
            JsonArray defCol = root.getAsJsonArray("defaultColor");
            for (int i = 0; i < Math.min(4, defCol.size()); i++) {
                this.defaultColor[i] = clamp01(defCol.get(i).getAsFloat());
            }
        }
        if (root.has("items")) {
            Map<String, ItemEntry> byId = byId();
            for (JsonElement el : root.getAsJsonArray("items")) {
                if (el.isJsonObject()) {
                    JsonObject obj = el.getAsJsonObject();
                    if (obj.has("id") && (entry = byId.get(obj.get("id").getAsString())) != null) {
                        if (obj.has("enabled")) {
                            entry.enabled().set(obj.get("enabled").getAsBoolean());
                        }
                        if (obj.has("color")) {
                            JsonArray col = obj.getAsJsonArray("color");
                            float[] c = entry.color();
                            for (int i2 = 0; i2 < Math.min(4, col.size()); i2++) {
                                c[i2] = clamp01(col.get(i2).getAsFloat());
                            }
                        }
                    }
                }
            }
        }
    }

    private void applyChanges(JsonObject root) {
        JsonElement defaults = defaultJson();
        if (defaults != null && defaults.isJsonObject()) {
            applyFull(defaults.getAsJsonObject());
        }
        if (root.has("defaultColor")) {
            BlockColorListSetting.readHex(root.get("defaultColor").getAsString(), this.defaultColor);
        }
        Map<String, ItemEntry> byId = byId();
        for (Map.Entry<String, JsonElement> change : root.getAsJsonObject("changes").entrySet()) {
            ItemEntry entry = byId.get(BlockColorListSetting.fullId(change.getKey()));
            if (entry != null && change.getValue().isJsonPrimitive()) {
                String[] parts = change.getValue().getAsString().split(" ");
                try {
                    float[] color = parts.length >= 2 ? BlockColorListSetting.parseHex(parts[1]) : null;
                    entry.enabled().set("1".equals(parts[0]));
                    if (color != null) {
                        System.arraycopy(color, 0, entry.color(), 0, 4);
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    private Map<String, ItemEntry> byId() {
        Map<String, ItemEntry> map = new HashMap<>();
        for (ItemEntry entry : entriesInternal()) {
            map.putIfAbsent(entry.id(), entry);
        }
        return map;
    }

    public static ItemColorListSetting createDefault() {
        return new ItemColorListSetting("Item Colors", (Supplier<List<ItemEntry>>) ItemColorListSetting::buildDefaultEntries, new float[]{1.0f, 1.0f, 1.0f, 0.8f});
    }

    private static List<ItemEntry> buildDefaultEntries() {
        List<ItemEntry> e = new ArrayList<>();
        for (class_1792 item : class_7923.field_41178) {
            class_2960 key = class_7923.field_41178.method_10221(item);
            if (key != null) {
                String id = key.toString();
                String path = key.method_12832();
                String displayName = prettify(path);
                float[] color = defaultColorFor(path);
                boolean enabled = isPreEnabled(path);
                e.add(entry(id, displayName, enabled, color));
            }
        }
        e.sort((a, b) -> {
            return a.displayName().compareToIgnoreCase(b.displayName());
        });
        return e;
    }

    private static String prettify(String path) {
        String[] words = path.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (!words[i].isEmpty()) {
                sb.append(Character.toUpperCase(words[i].charAt(0)));
                sb.append(words[i].substring(1));
                if (i < words.length - 1) {
                    sb.append(" ");
                }
            }
        }
        return sb.toString();
    }

    private static boolean isPreEnabled(String path) {
        switch (path) {
            case "diamond":
            case "emerald":
            case "netherite_ingot":
            case "ancient_debris":
            case "elytra":
            case "totem_of_undying":
            case "trident":
            case "enchanted_golden_apple":
            case "golden_apple":
            case "nether_star":
            case "heart_of_the_sea":
            case "end_crystal":
            case "shulker_box":
                return true;
            case "gold_ingot":
            case "gold_block":
            case "gold_nugget":
                return true;
            case "iron_ingot":
                return true;
            default:
                return false;
        }
    }

    private static float[] defaultColorFor(String path) {
        switch (path) {
            case "diamond":
            case "diamond_sword":
            case "diamond_pickaxe":
            case "diamond_axe":
            case "diamond_shovel":
            case "diamond_hoe":
            case "diamond_helmet":
            case "diamond_chestplate":
            case "diamond_leggings":
            case "diamond_boots":
                return rgba(0, 255, 255);
            case "emerald":
                return rgba(0, 255, 68);
            case "netherite_ingot":
            case "ancient_debris":
            case "netherite_sword":
            case "netherite_pickaxe":
            case "netherite_axe":
            case "netherite_shovel":
            case "netherite_hoe":
            case "netherite_helmet":
            case "netherite_chestplate":
            case "netherite_leggings":
            case "netherite_boots":
                return rgba(128, 96, 144);
            case "gold_ingot":
            case "gold_block":
            case "gold_nugget":
            case "golden_sword":
            case "golden_pickaxe":
            case "golden_axe":
            case "golden_shovel":
            case "golden_hoe":
            case "golden_helmet":
            case "golden_chestplate":
            case "golden_leggings":
            case "golden_boots":
            case "golden_apple":
            case "enchanted_golden_apple":
                return rgba(255, 215, 0);
            case "iron_ingot":
                return rgba(204, 204, 204);
            case "elytra":
                return rgba(85, 85, 255);
            case "totem_of_undying":
                return rgba(255, 170, 0);
            case "trident":
                return rgba(0, 136, 255);
            case "nether_star":
                return rgba(255, 255, 0);
            case "heart_of_the_sea":
                return rgba(0, 255, 221);
            case "end_crystal":
                return rgba(255, 0, 255);
            case "shulker_box":
                return rgba(204, 68, 204);
            default:
                return rgba(255, 255, 255);
        }
    }

    private static float[] rgba(int r, int g, int b) {
        return new float[]{r / 255.0f, g / 255.0f, b / 255.0f, 1.0f};
    }

    private static ItemEntry entry(String id, String display, boolean enabled, float[] color) {
        return new ItemEntry(id, display, new ImBoolean(enabled), color);
    }

    public static int rgbaToArgb(float[] rgba) {
        int r = Math.round(clamp01(rgba[0]) * 255.0f);
        int g = Math.round(clamp01(rgba[1]) * 255.0f);
        int b = Math.round(clamp01(rgba[2]) * 255.0f);
        int a = Math.round(clamp01(rgba[3]) * 255.0f);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static float clamp01(float v) {
        if (Float.isNaN(v) || Float.isInfinite(v)) {
            return 1.0f;
        }
        return Math.max(0.0f, Math.min(1.0f, v));
    }
}
