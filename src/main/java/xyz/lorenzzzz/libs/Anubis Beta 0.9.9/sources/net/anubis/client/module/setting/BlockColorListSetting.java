package net.anubis.client.module.setting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import imgui.type.ImBoolean;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2248;
import net.minecraft.class_2960;
import net.minecraft.class_7923;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/setting/BlockColorListSetting.class */
@Environment(EnvType.CLIENT)
public final class BlockColorListSetting extends Setting {
    static final String CHANGES = "changes";
    private static final String VANILLA = "minecraft:";
    private List<BlockEntry> entries;
    private Supplier<List<BlockEntry>> entriesSupplier;
    private final Supplier<List<BlockEntry>> shipped;
    private final float[] defaultColor;
    private final float[] shippedDefaultColor;
    private final boolean colorsHidden;
    private final List<String> defaultOrder;
    private final Map<String, BlockEntry> byId;
    private final ArrayList<BlockEntry> selection;
    private final ReferenceOpenHashSet<BlockEntry> selectionSet;
    private final List<BlockEntry> selectionView;
    private long selectionVersion;
    private Map<String, Integer> enabledLookup;
    private Map<String, Integer> tracerLookup;
    private Reference2IntOpenHashMap<class_2248> enabledByBlock;
    private Reference2IntOpenHashMap<class_2248> tracerByBlock;
    private long lookupVersion;
    private long currentVersion;
    private static final String WALL_BANNER = "_wall_banner";

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/setting/BlockColorListSetting$BlockEntry.class */
    @Environment(EnvType.CLIENT)
    public static final class BlockEntry extends Record {
        private final String id;
        private final String displayName;
        private final String category;
        private final ImBoolean enabled;
        private final float[] color;
        private final ImBoolean tracerEnabled;
        private final float[] tracerColor;

        public BlockEntry(String id, String displayName, String category, ImBoolean enabled, float[] color, ImBoolean tracerEnabled, float[] tracerColor) {
            this.id = id;
            this.displayName = displayName;
            this.category = category;
            this.enabled = enabled;
            this.color = color;
            this.tracerEnabled = tracerEnabled;
            this.tracerColor = tracerColor;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, BlockEntry.class), BlockEntry.class, "id;displayName;category;enabled;color;tracerEnabled;tracerColor", "FIELD:Lnet/anubis/client/module/setting/BlockColorListSetting$BlockEntry;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/setting/BlockColorListSetting$BlockEntry;->displayName:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/setting/BlockColorListSetting$BlockEntry;->category:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/setting/BlockColorListSetting$BlockEntry;->enabled:Limgui/type/ImBoolean;", "FIELD:Lnet/anubis/client/module/setting/BlockColorListSetting$BlockEntry;->color:[F", "FIELD:Lnet/anubis/client/module/setting/BlockColorListSetting$BlockEntry;->tracerEnabled:Limgui/type/ImBoolean;", "FIELD:Lnet/anubis/client/module/setting/BlockColorListSetting$BlockEntry;->tracerColor:[F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, BlockEntry.class), BlockEntry.class, "id;displayName;category;enabled;color;tracerEnabled;tracerColor", "FIELD:Lnet/anubis/client/module/setting/BlockColorListSetting$BlockEntry;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/setting/BlockColorListSetting$BlockEntry;->displayName:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/setting/BlockColorListSetting$BlockEntry;->category:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/setting/BlockColorListSetting$BlockEntry;->enabled:Limgui/type/ImBoolean;", "FIELD:Lnet/anubis/client/module/setting/BlockColorListSetting$BlockEntry;->color:[F", "FIELD:Lnet/anubis/client/module/setting/BlockColorListSetting$BlockEntry;->tracerEnabled:Limgui/type/ImBoolean;", "FIELD:Lnet/anubis/client/module/setting/BlockColorListSetting$BlockEntry;->tracerColor:[F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, BlockEntry.class, Object.class), BlockEntry.class, "id;displayName;category;enabled;color;tracerEnabled;tracerColor", "FIELD:Lnet/anubis/client/module/setting/BlockColorListSetting$BlockEntry;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/setting/BlockColorListSetting$BlockEntry;->displayName:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/setting/BlockColorListSetting$BlockEntry;->category:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/setting/BlockColorListSetting$BlockEntry;->enabled:Limgui/type/ImBoolean;", "FIELD:Lnet/anubis/client/module/setting/BlockColorListSetting$BlockEntry;->color:[F", "FIELD:Lnet/anubis/client/module/setting/BlockColorListSetting$BlockEntry;->tracerEnabled:Limgui/type/ImBoolean;", "FIELD:Lnet/anubis/client/module/setting/BlockColorListSetting$BlockEntry;->tracerColor:[F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public String id() {
            return this.id;
        }

        public String displayName() {
            return this.displayName;
        }

        public String category() {
            return this.category;
        }

        public ImBoolean enabled() {
            return this.enabled;
        }

        public float[] color() {
            return this.color;
        }

        public ImBoolean tracerEnabled() {
            return this.tracerEnabled;
        }

        public float[] tracerColor() {
            return this.tracerColor;
        }
    }

    public BlockColorListSetting(String name, List<BlockEntry> entries, float[] defaultColor) {
        super(name);
        this.byId = new HashMap();
        this.selection = new ArrayList<>();
        this.selectionSet = new ReferenceOpenHashSet<>();
        this.selectionView = Collections.unmodifiableList(this.selection);
        this.selectionVersion = -1L;
        this.lookupVersion = -1L;
        this.currentVersion = 0L;
        this.entries = new ArrayList(entries);
        List<BlockEntry> shippedEntries = entries.stream().map(BlockColorListSetting::copyEntry).toList();
        this.shipped = () -> {
            return shippedEntries;
        };
        this.defaultColor = defaultColor;
        this.shippedDefaultColor = copyColor(defaultColor);
        this.colorsHidden = false;
        this.defaultOrder = List.of();
        index();
        onChange(this::invalidateLookup);
    }

    public BlockColorListSetting(String name, Supplier<List<BlockEntry>> entriesSupplier, float[] defaultColor) {
        this(name, entriesSupplier, defaultColor, false, List.of());
    }

    private BlockColorListSetting(String name, Supplier<List<BlockEntry>> entriesSupplier, float[] defaultColor, boolean colorsHidden, List<String> defaultOrder) {
        super(name);
        this.byId = new HashMap();
        this.selection = new ArrayList<>();
        this.selectionSet = new ReferenceOpenHashSet<>();
        this.selectionView = Collections.unmodifiableList(this.selection);
        this.selectionVersion = -1L;
        this.lookupVersion = -1L;
        this.currentVersion = 0L;
        this.entriesSupplier = entriesSupplier;
        this.shipped = entriesSupplier;
        this.defaultColor = defaultColor;
        this.shippedDefaultColor = copyColor(defaultColor);
        this.colorsHidden = colorsHidden;
        this.defaultOrder = defaultOrder;
        onChange(this::invalidateLookup);
    }

    public boolean colorsHidden() {
        return this.colorsHidden;
    }

    private List<BlockEntry> entriesInternal() {
        if (this.entries == null) {
            this.entries = new ArrayList(this.entriesSupplier.get());
            this.entriesSupplier = null;
            index();
        }
        return this.entries;
    }

    private void index() {
        for (BlockEntry entry : this.entries) {
            this.byId.putIfAbsent(entry.id(), entry);
        }
        for (String id : this.defaultOrder) {
            BlockEntry entry2 = this.byId.get(id);
            if (entry2 != null && entry2.enabled().get()) {
                addToSelection(entry2);
            }
        }
        for (BlockEntry entry3 : this.entries) {
            if (entry3.enabled().get()) {
                addToSelection(entry3);
            }
        }
        this.selectionVersion = this.currentVersion;
    }

    public List<BlockEntry> entries() {
        return Collections.unmodifiableList(entriesInternal());
    }

    public BlockEntry entry(String id) {
        entriesInternal();
        return this.byId.get(id);
    }

    public float[] defaultColor() {
        return this.defaultColor;
    }

    public void resetColors() {
        entriesInternal();
        for (BlockEntry original : this.shipped.get()) {
            BlockEntry entry = this.byId.get(original.id());
            if (entry != null) {
                System.arraycopy(original.color(), 0, entry.color(), 0, 4);
                entry.tracerEnabled().set(original.tracerEnabled().get());
                System.arraycopy(original.tracerColor(), 0, entry.tracerColor(), 0, 4);
            }
        }
        System.arraycopy(this.shippedDefaultColor, 0, this.defaultColor, 0, 4);
        notifyChanged();
    }

    public List<BlockEntry> selected() {
        List<BlockEntry> all = entriesInternal();
        if (this.selectionVersion != this.currentVersion) {
            for (int i = this.selection.size() - 1; i >= 0; i--) {
                BlockEntry entry = this.selection.get(i);
                if (!entry.enabled().get()) {
                    this.selection.remove(i);
                    this.selectionSet.remove(entry);
                }
            }
            for (BlockEntry entry2 : all) {
                if (entry2.enabled().get()) {
                    addToSelection(entry2);
                }
            }
            this.selectionVersion = this.currentVersion;
        }
        return this.selectionView;
    }

    public boolean select(BlockEntry entry) {
        selected();
        if (!addToSelection(entry)) {
            return false;
        }
        entry.enabled().set(true);
        notifyChanged();
        return true;
    }

    public boolean deselect(BlockEntry entry) {
        selected();
        if (!this.selectionSet.remove(entry)) {
            return false;
        }
        for (int i = 0; i < this.selection.size(); i++) {
            if (this.selection.get(i) == entry) {
                this.selection.remove(i);
                break;
            }
        }
        entry.enabled().set(false);
        notifyChanged();
        return true;
    }

    public int selectAll(Iterable<BlockEntry> toAdd) {
        selected();
        int added = 0;
        for (BlockEntry entry : toAdd) {
            if (addToSelection(entry)) {
                entry.enabled().set(true);
                added++;
            }
        }
        if (added > 0) {
            notifyChanged();
        }
        return added;
    }

    public int deselectAll() {
        selected();
        int removed = this.selection.size();
        if (removed == 0) {
            return 0;
        }
        for (BlockEntry entry : this.selection) {
            entry.enabled().set(false);
        }
        this.selection.clear();
        this.selectionSet.clear();
        notifyChanged();
        return removed;
    }

    private boolean addToSelection(BlockEntry entry) {
        if (!this.selectionSet.add(entry)) {
            return false;
        }
        this.selection.add(entry);
        return true;
    }

    public void invalidateLookup() {
        this.currentVersion++;
    }

    private Map<String, Integer> lookup() {
        if (this.enabledLookup == null || this.lookupVersion != this.currentVersion) {
            rebuildLookup();
        }
        return this.enabledLookup;
    }

    private void rebuildLookup() {
        Map<String, Integer> map = new HashMap<>();
        Map<String, Integer> tracers = new HashMap<>();
        Reference2IntOpenHashMap<class_2248> byBlock = new Reference2IntOpenHashMap<>();
        Reference2IntOpenHashMap<class_2248> tracerByBlockMap = new Reference2IntOpenHashMap<>();
        for (BlockEntry entry : entriesInternal()) {
            if (entry.enabled().get()) {
                BlockEntry look = colorSource(entry);
                int argb = rgbaToArgb(look.color());
                map.put(entry.id(), Integer.valueOf(argb));
                class_2248 block = resolveBlock(entry.id());
                if (block != null) {
                    byBlock.put(block, argb);
                }
                if (look.tracerEnabled().get()) {
                    int tracerArgb = rgbaToArgb(look.tracerColor());
                    tracers.put(entry.id(), Integer.valueOf(tracerArgb));
                    if (block != null) {
                        tracerByBlockMap.put(block, tracerArgb);
                    }
                }
            }
        }
        this.enabledLookup = map;
        this.tracerLookup = tracers;
        this.enabledByBlock = byBlock;
        this.tracerByBlock = tracerByBlockMap;
        this.lookupVersion = this.currentVersion;
    }

    private BlockEntry colorSource(BlockEntry entry) {
        String id = entry.id();
        if (!id.endsWith(WALL_BANNER)) {
            return entry;
        }
        BlockEntry banner = this.byId.get(id.substring(0, id.length() - WALL_BANNER.length()) + "_banner");
        return (banner == null || !banner.enabled().get()) ? entry : banner;
    }

    private static class_2248 resolveBlock(String id) {
        class_2960 key = class_2960.method_12829(id);
        if (key == null) {
            return null;
        }
        return (class_2248) class_7923.field_41175.method_17966(key).orElse(null);
    }

    private Map<String, Integer> tracerLookup() {
        if (this.tracerLookup == null || this.lookupVersion != this.currentVersion) {
            rebuildLookup();
        }
        return this.tracerLookup;
    }

    private Reference2IntOpenHashMap<class_2248> blockLookup() {
        if (this.enabledByBlock == null || this.lookupVersion != this.currentVersion) {
            rebuildLookup();
        }
        return this.enabledByBlock;
    }

    private Reference2IntOpenHashMap<class_2248> tracerBlockLookup() {
        if (this.tracerByBlock == null || this.lookupVersion != this.currentVersion) {
            rebuildLookup();
        }
        return this.tracerByBlock;
    }

    public boolean isEnabled(class_2248 block) {
        return blockLookup().containsKey(block);
    }

    public Set<class_2248> enabledBlocks() {
        return Collections.unmodifiableSet(blockLookup().keySet());
    }

    public int colorArgb(class_2248 block) {
        Reference2IntOpenHashMap<class_2248> map = blockLookup();
        return map.containsKey(block) ? map.getInt(block) : rgbaToArgb(this.defaultColor);
    }

    public boolean hasAnyEnabled() {
        return !lookup().isEmpty();
    }

    public Set<String> enabledIds() {
        return lookup().keySet();
    }

    public int tracerColorArgb(class_2248 block) {
        Reference2IntOpenHashMap<class_2248> map = tracerBlockLookup();
        if (map.containsKey(block)) {
            return map.getInt(block);
        }
        return 0;
    }

    public boolean hasAnyTracerEnabled() {
        return !tracerLookup().isEmpty();
    }

    @Override // net.anubis.client.module.setting.Setting
    public JsonElement serialize() {
        JsonObject root = new JsonObject();
        root.add("defaultColor", colorJson(this.defaultColor));
        JsonArray arr = new JsonArray();
        Iterator<BlockEntry> it = selected().iterator();
        while (it.hasNext()) {
            arr.add(entryJson(it.next()));
        }
        for (BlockEntry e : entriesInternal()) {
            if (!this.selectionSet.contains(e)) {
                arr.add(entryJson(e));
            }
        }
        root.add("blocks", arr);
        return root;
    }

    private static JsonObject entryJson(BlockEntry e) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", e.id());
        obj.addProperty("enabled", Boolean.valueOf(e.enabled().get()));
        obj.add("color", colorJson(e.color()));
        obj.addProperty("tracerEnabled", Boolean.valueOf(e.tracerEnabled().get()));
        obj.add("tracerColor", colorJson(e.tracerColor()));
        return obj;
    }

    private static JsonArray colorJson(float[] rgba) {
        JsonArray arr = new JsonArray();
        for (float v : rgba) {
            arr.add(Float.valueOf(clamp01(v)));
        }
        return arr;
    }

    @Override // net.anubis.client.module.setting.Setting
    public JsonElement compact(JsonElement value) {
        JsonElement defaults = defaultJson();
        if (value == null || !value.isJsonObject() || defaults == null || !defaults.isJsonObject()) {
            return value;
        }
        JsonObject root = value.getAsJsonObject();
        JsonObject shipped = defaults.getAsJsonObject();
        if (root.has(CHANGES) || !root.has("blocks") || !shipped.has("blocks")) {
            return value;
        }
        try {
            Map<String, String[]> before = new HashMap<>();
            Iterator it = shipped.getAsJsonArray("blocks").iterator();
            while (it.hasNext()) {
                JsonObject obj = ((JsonElement) it.next()).getAsJsonObject();
                before.put(obj.get("id").getAsString(), fields(obj));
            }
            JsonObject changes = new JsonObject();
            for (JsonElement el : root.getAsJsonArray("blocks")) {
                if (el.isJsonObject() && el.getAsJsonObject().has("id")) {
                    JsonObject obj2 = el.getAsJsonObject();
                    String id = obj2.get("id").getAsString();
                    String[] now = fields(obj2);
                    String[] was = before.get(id);
                    if (was == null || !Arrays.equals(now, was)) {
                        boolean tracer = (was != null && now[2].equals(was[2]) && now[3].equals(was[3])) ? false : true;
                        StringBuilder change = new StringBuilder(now[0]);
                        if (tracer || !now[1].equals(was[1])) {
                            change.append(' ').append(now[1]);
                        }
                        if (tracer) {
                            change.append(' ').append(now[2]).append(' ').append(now[3]);
                        }
                        changes.addProperty(shortId(id), change.toString());
                    }
                }
            }
            JsonObject out = new JsonObject();
            String color = hexColor(root.get("defaultColor"));
            if (color != null && !color.equals(hexColor(shipped.get("defaultColor")))) {
                out.addProperty("defaultColor", color);
            }
            out.add(CHANGES, changes);
            return out;
        } catch (RuntimeException e) {
            return value;
        }
    }

    @Override // net.anubis.client.module.setting.Setting
    protected void deserializeValue(JsonElement element) {
        if (element == null || !element.isJsonObject()) {
            return;
        }
        try {
            JsonObject root = element.getAsJsonObject();
            if (root.has(CHANGES) && root.get(CHANGES).isJsonObject()) {
                applyChanges(root);
            } else {
                applyFull(root);
            }
        } catch (Exception e) {
        }
    }

    private void applyFull(JsonObject root) {
        BlockEntry entry;
        if (root.has("defaultColor")) {
            readColor(root.getAsJsonArray("defaultColor"), this.defaultColor);
        }
        if (root.has("blocks")) {
            JsonArray<JsonElement> arr = root.getAsJsonArray("blocks");
            List<BlockEntry> previous = new ArrayList<>(selected());
            this.selection.clear();
            this.selectionSet.clear();
            for (JsonElement el : arr) {
                if (el.isJsonObject()) {
                    JsonObject obj = el.getAsJsonObject();
                    if (obj.has("id") && (entry = this.byId.get(obj.get("id").getAsString())) != null) {
                        if (obj.has("enabled")) {
                            entry.enabled().set(obj.get("enabled").getAsBoolean());
                        }
                        if (obj.has("color")) {
                            readColor(obj.getAsJsonArray("color"), entry.color());
                        }
                        if (obj.has("tracerEnabled")) {
                            entry.tracerEnabled().set(obj.get("tracerEnabled").getAsBoolean());
                        }
                        if (obj.has("tracerColor")) {
                            readColor(obj.getAsJsonArray("tracerColor"), entry.tracerColor());
                        }
                        if (entry.enabled().get()) {
                            addToSelection(entry);
                        }
                    }
                }
            }
            for (BlockEntry entry2 : previous) {
                if (entry2.enabled().get()) {
                    addToSelection(entry2);
                }
            }
        }
    }

    private void applyChanges(JsonObject root) {
        entriesInternal();
        JsonElement defaults = defaultJson();
        if (defaults != null && defaults.isJsonObject()) {
            applyFull(defaults.getAsJsonObject());
        }
        if (root.has("defaultColor")) {
            readHex(root.get("defaultColor").getAsString(), this.defaultColor);
        }
        for (Map.Entry<String, JsonElement> change : root.getAsJsonObject(CHANGES).entrySet()) {
            BlockEntry entry = this.byId.get(fullId(change.getKey()));
            if (entry != null && change.getValue().isJsonPrimitive()) {
                String[] parts = change.getValue().getAsString().split(" ");
                try {
                    float[] color = parts.length >= 2 ? parseHex(parts[1]) : null;
                    float[] tracerColor = parts.length >= 4 ? parseHex(parts[3]) : null;
                    entry.enabled().set("1".equals(parts[0]));
                    if (color != null) {
                        System.arraycopy(color, 0, entry.color(), 0, 4);
                    }
                    if (tracerColor != null) {
                        entry.tracerEnabled().set("1".equals(parts[2]));
                        System.arraycopy(tracerColor, 0, entry.tracerColor(), 0, 4);
                    }
                    if (entry.enabled().get()) {
                        addToSelection(entry);
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    private static String[] fields(JsonObject obj) {
        return new String[]{flag(obj, "enabled"), String.valueOf(hexColor(obj.get("color"))), flag(obj, "tracerEnabled"), String.valueOf(hexColor(obj.get("tracerColor")))};
    }

    private static String flag(JsonObject obj, String key) {
        return (obj.has(key) && obj.get(key).getAsBoolean()) ? "1" : "0";
    }

    static String hexColor(JsonElement color) {
        if (color == null || !color.isJsonArray()) {
            return null;
        }
        float[] rgba = {1.0f, 1.0f, 1.0f, 1.0f};
        readColor(color.getAsJsonArray(), rgba);
        return ColorSetting.hex(rgbaToArgb(rgba));
    }

    static void readHex(String hex, float[] into) {
        System.arraycopy(parseHex(hex), 0, into, 0, 4);
    }

    static float[] parseHex(String hex) {
        int argb = (int) Long.parseLong(hex, 16);
        return new float[]{((argb >> 16) & 255) / 255.0f, ((argb >> 8) & 255) / 255.0f, (argb & 255) / 255.0f, (argb >>> 24) / 255.0f};
    }

    static String shortId(String id) {
        return id.startsWith(VANILLA) ? id.substring(VANILLA.length()) : id;
    }

    static String fullId(String id) {
        return id.indexOf(58) < 0 ? "minecraft:" + id : id;
    }

    private static void readColor(JsonArray from, float[] into) {
        for (int i = 0; i < Math.min(4, from.size()); i++) {
            into[i] = clamp01(from.get(i).getAsFloat());
        }
    }

    public static BlockColorListSetting createDefault() {
        return new BlockColorListSetting("Block Colors", (Supplier<List<BlockEntry>>) () -> {
            return buildEntries(id -> {
                return false;
            });
        }, white());
    }

    public static BlockColorListSetting blockList(String name, List<String> defaults) {
        Set<String> enabled = Set.copyOf(defaults);
        return new BlockColorListSetting(name, () -> {
            Objects.requireNonNull(enabled);
            return buildEntries((v1) -> {
                return r0.contains(v1);
            });
        }, white(), true, List.copyOf(defaults));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<BlockEntry> buildEntries(Predicate<String> enabledByDefault) {
        class_2960 key;
        List<BlockEntry> e = new ArrayList<>();
        for (class_2248 block : class_7923.field_41175) {
            if (!block.method_9564().method_26215() && (key = class_7923.field_41175.method_10221(block)) != null) {
                String id = key.toString();
                e.add(entry(id, prettify(key.method_12832()), "Blocks", enabledByDefault.test(id), 1.0f, 1.0f, 1.0f, 1.0f));
            }
        }
        return e;
    }

    public static BlockColorListSetting createForBlockEntityDebug() {
        return new BlockColorListSetting("Block Colors", (Supplier<List<BlockEntry>>) BlockColorListSetting::buildBlockEntityDebugEntries, white());
    }

    private static List<BlockEntry> buildBlockEntityDebugEntries() {
        class_2960 key;
        List<BlockEntry> e = new ArrayList<>();
        for (class_2248 block : class_7923.field_41175) {
            if (!block.method_9564().method_26215() && (key = class_7923.field_41175.method_10221(block)) != null) {
                String path = key.method_12832();
                int rgb = blockEntityDebugRgb(path);
                boolean enabled = rgb != -1 || blockEntityDebugPicked(path);
                e.add(entry(key.toString(), prettify(path), "Blocks", enabled, rgb == -1 ? 16777215 : rgb, true));
            }
        }
        return e;
    }

    private static int blockEntityDebugRgb(String path) {
        if (path.endsWith("copper_chest")) {
            return 10246912;
        }
        if (path.endsWith("shulker_box")) {
            return 8781982;
        }
        switch (path) {
            case "chest":
                return 10246912;
            case "trapped_chest":
                return 13775410;
            case "ender_chest":
                return 7667967;
            case "barrel":
                return 6572850;
            case "hopper":
                return 5263440;
            case "dispenser":
            case "dropper":
                return 7887420;
            case "brewing_stand":
                return 7874760;
            case "furnace":
            case "blast_furnace":
            case "smoker":
                return 6579300;
            case "spawner":
                return 9076390;
            case "enchanting_table":
                return 13107200;
            case "crafter":
                return 3978400;
            case "moving_piston":
            case "piston":
            case "sticky_piston":
            case "piston_head":
                return 65280;
            case "lectern":
                return 13806160;
            case "jukebox":
                return 2017380;
            case "decorated_pot":
                return 11822120;
            case "beehive":
            case "bee_nest":
                return 15779840;
            case "beacon":
                return 61660;
            case "conduit":
                return 46320;
            case "comparator":
                return 13120050;
            default:
                return -1;
        }
    }

    private static boolean blockEntityDebugPicked(String path) {
        if (path.contains("sign") || path.endsWith("_bed") || path.endsWith("_banner") || path.endsWith("_skull") || path.endsWith("_head")) {
            return true;
        }
        switch (path) {
            case "bell":
            case "campfire":
            case "soul_campfire":
            case "jigsaw":
            case "structure_block":
            case "command_block":
            case "chain_command_block":
            case "repeating_command_block":
            case "end_portal":
            case "end_gateway":
            case "sculk_sensor":
            case "calibrated_sculk_sensor":
            case "sculk_catalyst":
            case "sculk_shrieker":
            case "suspicious_sand":
            case "suspicious_gravel":
            case "trial_spawner":
            case "vault":
            case "chiseled_bookshelf":
            case "daylight_detector":
                return true;
            default:
                return false;
        }
    }

    private static String prettify(String path) {
        String[] words = path.split("_");
        StringBuilder display = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (!words[i].isEmpty()) {
                display.append(Character.toUpperCase(words[i].charAt(0)));
                display.append(words[i].substring(1));
                if (i < words.length - 1) {
                    display.append(" ");
                }
            }
        }
        return display.toString();
    }

    private static float[] white() {
        return new float[]{1.0f, 1.0f, 1.0f, 1.0f};
    }

    private static BlockEntry entry(String id, String display, String cat, boolean enabled, float r, float g, float b, float a) {
        float[] color = {r, g, b, a};
        return new BlockEntry(id, display, cat, new ImBoolean(enabled), color, new ImBoolean(false), copyColor(color));
    }

    private static BlockEntry entry(String id, String display, String cat, boolean enabled, int rgb, boolean tracer) {
        float[] color = {((rgb >> 16) & 255) / 255.0f, ((rgb >> 8) & 255) / 255.0f, (rgb & 255) / 255.0f, 1.0f};
        return new BlockEntry(id, display, cat, new ImBoolean(enabled), color, new ImBoolean(tracer), copyColor(color));
    }

    private static BlockEntry copyEntry(BlockEntry e) {
        return new BlockEntry(e.id(), e.displayName(), e.category(), new ImBoolean(e.enabled().get()), copyColor(e.color()), new ImBoolean(e.tracerEnabled().get()), copyColor(e.tracerColor()));
    }

    private static float[] copyColor(float[] rgba) {
        return new float[]{rgba[0], rgba[1], rgba[2], rgba[3]};
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
