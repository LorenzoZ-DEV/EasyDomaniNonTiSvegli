package net.anubis.client.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.anubis.client.AnubisClientMod;
import net.anubis.client.addon.AddonManager;
import net.anubis.client.imgui.ImGuiManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_310;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudLayoutStore.class */
@Environment(EnvType.CLIENT)
public final class HudLayoutStore {
    private static final int CONFIG_VERSION = 11;
    private static final float DEFAULT_MARGIN = 10.0f;
    private static final float DEFAULT_GAP = 6.0f;
    private static final float CENTER_TOLERANCE = 24.0f;
    static final int ANCHOR_LEFT = 0;
    static final int ANCHOR_CENTER = 1;
    static final int ANCHOR_RIGHT = 2;
    private final Map<String, HudElementState> elements = new LinkedHashMap();
    private boolean loaded;
    private static final HudLayoutStore INSTANCE = new HudLayoutStore();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final String INFO_BAR_ID = "info_bar";
    public static final String COORDINATES_ID = "coordinates";
    public static final String MODULE_LIST_ID = "module_list";
    public static final String POTION_TRACKER_ID = "potion_tracker";
    public static final String REGION_MAP_ID = "region_map";
    public static final String ANTI_VANISH_ID = "anti_vanish";
    public static final String STAFF_LIST_ID = "staff_list";
    public static final String SPOTIFY_ID = "spotify";
    public static final String HOTKEYS_ID = "hotkeys";
    public static final String RADAR_ID = "radar";
    public static final String TARGET_CARD_ID = "target_card";
    public static final String ARMOR_STATUS_ID = "armor_status";
    public static final String ITEM_COOLDOWNS_ID = "item_cooldowns";
    private static final String[] KNOWN_IDS = {INFO_BAR_ID, COORDINATES_ID, MODULE_LIST_ID, POTION_TRACKER_ID, REGION_MAP_ID, ANTI_VANISH_ID, STAFF_LIST_ID, SPOTIFY_ID, HOTKEYS_ID, RADAR_ID, TARGET_CARD_ID, ARMOR_STATUS_ID, ITEM_COOLDOWNS_ID};

    private HudLayoutStore() {
    }

    public static HudLayoutStore getInstance() {
        return INSTANCE;
    }

    synchronized HudElementState element(String id) {
        loadIfNeeded();
        HudElementState state = this.elements.get(id);
        if (state == null) {
            state = defaultElement(id);
            this.elements.put(id, state);
        }
        return state;
    }

    synchronized List<HudElementState> orderedElements() {
        loadIfNeeded();
        return List.copyOf(this.elements.values());
    }

    synchronized void updateBounds(String id, float width, float height, float screenW, float screenH) {
        HudElementState state = element(id);
        float newW = Math.max(1.0f, width);
        float newH = Math.max(1.0f, height);
        if (state.positioned && state.autoPlaced && screenW > 0.0f && screenH > 0.0f && dockMoved(state, screenW, screenH)) {
            state.positioned = false;
        }
        if (!state.positioned || screenW <= 0.0f || screenH <= 0.0f) {
            state.width = newW;
            state.height = newH;
            if (!state.positioned && screenW > 0.0f && screenH > 0.0f && !isCollapsed(state)) {
                placeDefault(state, screenW, screenH);
                return;
            }
            return;
        }
        if (!state.anchored) {
            adoptLoadedPosition(state, screenW, screenH);
        }
        rebaseToScreen(state, screenW, screenH);
        float dw = state.width - newW;
        if (state.anchorX == 2) {
            state.x += dw;
        } else if (state.anchorX == 1) {
            state.x += dw * 0.5f;
        }
        if (state.anchorBottom) {
            state.y += state.height - newH;
        }
        state.width = newW;
        state.height = newH;
        clampToScreen(state, screenW, screenH);
    }

    private void adoptLoadedPosition(HudElementState state, float screenW, float screenH) {
        if (state.refScreenW > 0.0f && state.refScreenH > 0.0f) {
            state.anchored = true;
            return;
        }
        if (INFO_BAR_ID.equals(state.id)) {
            state.x = (screenW - state.width) * 0.5f;
        } else if (state.x > screenW - state.width || state.y > screenH - state.height) {
            placeDefault(state, screenW, screenH);
            return;
        }
        computeAnchor(state, screenW, screenH);
    }

    private static void rebaseToScreen(HudElementState state, float screenW, float screenH) {
        float dx = screenW - state.refScreenW;
        float dy = screenH - state.refScreenH;
        if (dx == 0.0f && dy == 0.0f) {
            return;
        }
        if (state.anchorX == 2) {
            state.x += dx;
        } else if (state.anchorX == 1) {
            state.x += dx * 0.5f;
        }
        if (state.anchorBottom) {
            state.y += dy;
        }
        state.refScreenW = screenW;
        state.refScreenH = screenH;
    }

    synchronized void moveTo(String id, float x, float y, float screenW, float screenH) {
        HudElementState state = element(id);
        state.x = x;
        state.y = y;
        state.positioned = true;
        state.autoPlaced = false;
        clampToScreen(state, screenW, screenH);
        computeAnchor(state, screenW, screenH);
    }

    private static void computeAnchor(HudElementState state, float screenW, float screenH) {
        float cx = state.x + (state.width * 0.5f);
        if (Math.abs(cx - (screenW * 0.5f)) <= 24.0f * uiScale()) {
            state.anchorX = 1;
        } else {
            state.anchorX = cx > screenW * 0.5f ? 2 : 0;
        }
        state.anchorBottom = state.y + (state.height * 0.5f) > screenH * 0.5f;
        state.refScreenW = screenW;
        state.refScreenH = screenH;
        state.anchored = true;
    }

    private static float uiScale() {
        return ImGuiManager.getInstance().uiScale();
    }

    synchronized void resetToDefaults(float screenW, float screenH) {
        Map<String, Dimensions> currentSizes = new LinkedHashMap<>();
        for (String id : KNOWN_IDS) {
            currentSizes.put(id, new Dimensions(existingWidth(id, fallbackWidth(id)), existingHeight(id, 26.0f)));
        }
        this.elements.clear();
        for (String id2 : KNOWN_IDS) {
            HudElementState state = defaultElement(id2);
            Dimensions size = currentSizes.get(id2);
            state.width = size.width();
            state.height = size.height();
            this.elements.put(state.id, state);
        }
        for (HudElementState state2 : this.elements.values()) {
            if (!isCollapsed(state2)) {
                placeDefault(state2, screenW, screenH);
                clampToScreen(state2, screenW, screenH);
            }
        }
        save();
    }

    public synchronized JsonObject profileSnapshot() {
        loadIfNeeded();
        return GSON.toJsonTree(buildConfigFile()).getAsJsonObject();
    }

    public synchronized boolean applyProfile(JsonObject layout) {
        loadIfNeeded();
        ConfigFile config = parse(layout);
        if (config == null) {
            return false;
        }
        applyConfig(config);
        save();
        return true;
    }

    public static JsonObject shareable(JsonObject layout) {
        ConfigFile config = parse(layout);
        if (config == null) {
            return null;
        }
        return GSON.toJsonTree(config).getAsJsonObject();
    }

    private static ConfigFile parse(JsonObject layout) {
        try {
            ConfigFile config = (ConfigFile) GSON.fromJson(layout, ConfigFile.class);
            if (config == null || config.elements == null) {
                return null;
            }
            List<HudElementState> kept = new ArrayList<>();
            for (HudElementState state : config.elements) {
                if (state != null && state.id != null && isKnownId(state.id) && finite(state)) {
                    kept.add(state);
                }
            }
            config.elements = kept;
            return config;
        } catch (RuntimeException e) {
            return null;
        }
    }

    private static boolean finite(HudElementState state) {
        return finite(state.x) && finite(state.y) && finite(state.width) && finite(state.height) && finite(state.scale) && finite(state.refScreenW) && finite(state.refScreenH) && finite(state.placedGuiScale) && finite(state.placedUiScale);
    }

    public synchronized void save() {
        loadIfNeeded();
        ConfigFile config = buildConfigFile();
        Path path = configPath();
        try {
            Files.createDirectories(path.getParent(), new FileAttribute[0]);
            Path tmp = path.resolveSibling(path.getFileName().toString() + ".tmp");
            Writer writer = Files.newBufferedWriter(tmp, new OpenOption[0]);
            try {
                GSON.toJson(config, writer);
                if (writer != null) {
                    writer.close();
                }
                try {
                    Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
                } catch (IOException e) {
                    Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (Throwable th) {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (IOException e2) {
            AnubisClientMod.LOGGER.warn("[Anubis] Failed to save HUD layout config", e2);
        }
    }

    private void loadIfNeeded() {
        if (this.loaded) {
            return;
        }
        this.loaded = true;
        installDefaults();
        Path path = configPath();
        if (!Files.isRegularFile(path, new LinkOption[0])) {
            return;
        }
        try {
            Reader reader = Files.newBufferedReader(path);
            try {
                ConfigFile config = (ConfigFile) GSON.fromJson(reader, ConfigFile.class);
                if (config != null && config.elements != null) {
                    applyConfig(config);
                    if (reader != null) {
                        reader.close();
                    }
                    return;
                } else {
                    if (reader != null) {
                        reader.close();
                        return;
                    }
                    return;
                }
            } catch (Throwable th) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (IOException | RuntimeException e) {
            AnubisClientMod.LOGGER.warn("[Anubis] Failed to load HUD layout config; using defaults", e);
            this.elements.clear();
            installDefaults();
        }
        AnubisClientMod.LOGGER.warn("[Anubis] Failed to load HUD layout config; using defaults", e);
        this.elements.clear();
        installDefaults();
    }

    private ConfigFile buildConfigFile() {
        ConfigFile config = new ConfigFile();
        config.version = 11;
        config.elements = new ArrayList();
        for (HudElementState state : this.elements.values()) {
            if (state.positioned) {
                config.elements.add(state);
            }
        }
        return config;
    }

    private void applyConfig(ConfigFile config) {
        this.elements.clear();
        installDefaults();
        boolean oldLayout = config.version < 11;
        for (HudElementState loadedState : config.elements) {
            if (loadedState != null && loadedState.id != null && !loadedState.id.isBlank() && isKnownId(loadedState.id)) {
                normalize(loadedState);
                this.elements.put(loadedState.id, loadedState);
            }
        }
        ensureKnownElements();
        if (oldLayout) {
            resetPositionsForMigration();
        }
    }

    private void installDefaults() {
        for (String id : KNOWN_IDS) {
            this.elements.put(id, defaultElement(id));
        }
    }

    private void ensureKnownElements() {
        for (String id : KNOWN_IDS) {
            this.elements.computeIfAbsent(id, HudLayoutStore::defaultElement);
        }
    }

    private void resetPositionsForMigration() {
        for (String id : KNOWN_IDS) {
            HudElementState state = this.elements.get(id);
            if (state != null) {
                state.positioned = false;
            }
        }
    }

    private static HudElementState defaultElement(String id) {
        HudElementState state = new HudElementState();
        state.id = id;
        state.x = 0.0f;
        state.y = 0.0f;
        state.width = fallbackWidth(id);
        state.height = 26.0f;
        state.visible = true;
        state.scale = 1.0f;
        state.positioned = false;
        return state;
    }

    private static void normalize(HudElementState state) {
        state.scale = finite(state.scale) ? clamp(state.scale, 0.5f, 3.0f) : 1.0f;
        state.width = (!finite(state.width) || state.width <= 0.0f) ? 1.0f : state.width;
        state.height = (!finite(state.height) || state.height <= 0.0f) ? 1.0f : state.height;
        state.positioned = finite(state.x) && finite(state.y);
        if (state.anchorX < 0 || state.anchorX > 2 || !finite(state.refScreenW) || !finite(state.refScreenH)) {
            state.refScreenW = 0.0f;
            state.refScreenH = 0.0f;
        }
    }

    private void placeDefault(HudElementState state, float screenW, float screenH) {
        float margin = Math.round(DEFAULT_MARGIN * uiScale());
        float gap = Math.round(DEFAULT_GAP * uiScale());
        float coordsBottom = margin + existingHeight(COORDINATES_ID, 28.0f) + gap;
        switch (state.id) {
            case "potion_tracker":
                state.x = Math.max(margin, (screenW - margin) - Math.max(1.0f, state.width));
                state.y = Math.max(margin, (screenH - margin) - Math.max(1.0f, state.height));
                break;
            case "info_bar":
                state.x = Math.max(margin, (screenW - state.width) * 0.5f);
                state.y = margin;
                break;
            case "module_list":
                state.x = Math.max(margin, (screenW - margin) - Math.max(1.0f, state.width));
                state.y = margin;
                break;
            case "region_map":
                state.x = margin;
                state.y = firstFreeY(state, coordsBottom, gap);
                break;
            case "anti_vanish":
                state.x = Math.max(margin, (screenW - state.width) * 0.5f);
                state.y = margin + existingHeight(INFO_BAR_ID, 28.0f) + gap;
                break;
            case "spotify":
                state.x = margin;
                state.y = firstFreeY(state, coordsBottom, gap);
                break;
            case "staff_list":
                state.x = margin;
                state.y = firstFreeY(state, Math.round(screenH * 0.38f), gap);
                break;
            case "hotkeys":
                state.x = Math.max(margin, (screenW - margin) - Math.max(1.0f, state.width));
                state.y = firstFreeY(state, Math.round(screenH * 0.36f), gap);
                break;
            case "radar":
                state.x = Math.max(margin, (screenW - margin) - Math.max(1.0f, state.width));
                state.y = firstFreeY(state, Math.round(screenH * 0.58f), gap);
                break;
            case "target_card":
                state.x = Math.round((screenW * 0.5f) + (44.0f * uiScale()));
                state.y = firstFreeY(state, Math.round((screenH * 0.5f) + (30.0f * uiScale())), gap);
                break;
            case "armor_status":
                state.x = Math.round((screenW * 0.5f) + (119.0f * vanillaGuiScale()));
                state.y = Math.max(margin, (screenH - margin) - Math.max(1.0f, state.height));
                break;
            case "item_cooldowns":
                state.x = Math.max(margin, Math.round((screenW - state.width) * 0.5f));
                state.y = Math.max(margin, (screenH - Math.round(76.0f * vanillaGuiScale())) - Math.max(1.0f, state.height));
                break;
            default:
                state.x = margin;
                state.y = margin;
                break;
        }
        state.positioned = true;
        clampToScreen(state, screenW, screenH);
        computeAnchor(state, screenW, screenH);
        state.autoPlaced = followsVanillaHud(state.id);
        if (state.autoPlaced) {
            state.anchorX = ITEM_COOLDOWNS_ID.equals(state.id) ? 1 : 0;
            state.placedGuiScale = vanillaGuiScale();
            state.placedUiScale = uiScale();
        }
    }

    private static boolean followsVanillaHud(String id) {
        return TARGET_CARD_ID.equals(id) || ARMOR_STATUS_ID.equals(id) || ITEM_COOLDOWNS_ID.equals(id);
    }

    private static boolean dockMoved(HudElementState state, float screenW, float screenH) {
        return (screenW == state.refScreenW && screenH == state.refScreenH && vanillaGuiScale() == state.placedGuiScale && uiScale() == state.placedUiScale) ? false : true;
    }

    private static float fallbackWidth(String id) {
        switch (id) {
            case "module_list":
                return 170.0f;
            case "info_bar":
                return 290.0f;
            case "coordinates":
                return 210.0f;
            case "potion_tracker":
                return 190.0f;
            case "region_map":
                return 240.0f;
            case "anti_vanish":
                return 180.0f;
            case "staff_list":
                return 190.0f;
            case "spotify":
                return 320.0f;
            case "hotkeys":
                return 190.0f;
            case "radar":
                return 150.0f;
            case "target_card":
                return 244.0f;
            case "armor_status":
                return 120.0f;
            case "item_cooldowns":
                return 80.0f;
            default:
                return 110.0f;
        }
    }

    private static float vanillaGuiScale() {
        return Math.max(1, class_310.method_1551().method_22683().method_4495());
    }

    private float firstFreeY(HudElementState state, float startY, float gap) {
        float y = startY;
        float w = Math.max(1.0f, state.width);
        float h = Math.max(1.0f, state.height);
        for (int pass = 0; pass < this.elements.size(); pass++) {
            boolean moved = false;
            for (HudElementState other : this.elements.values()) {
                if (other != state && other.positioned && !isCollapsed(other)) {
                    boolean overlaps = state.x < other.x + other.width && other.x < state.x + w && y < other.y + other.height && other.y < y + h;
                    if (overlaps) {
                        y = other.y + other.height + gap;
                        moved = true;
                    }
                }
            }
            if (!moved) {
                break;
            }
        }
        return y;
    }

    private static boolean isCollapsed(HudElementState state) {
        return state.width <= 1.0f;
    }

    private static void clampToScreen(HudElementState state, float screenW, float screenH) {
        float maxX = Math.max(0.0f, screenW - Math.max(1.0f, state.width));
        float maxY = Math.max(0.0f, screenH - Math.max(1.0f, state.height));
        state.x = clamp(state.x, 0.0f, maxX);
        state.y = clamp(state.y, 0.0f, maxY);
    }

    private float existingWidth(String id, float fallback) {
        HudElementState state = this.elements.get(id);
        return (state == null || !finite(state.width) || state.width <= 0.0f) ? fallback : state.width;
    }

    private float existingHeight(String id, float fallback) {
        HudElementState state = this.elements.get(id);
        return (state == null || !finite(state.height) || state.height <= 0.0f) ? fallback : state.height;
    }

    private static Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(AddonManager.ENTRYPOINT).resolve("hud.json");
    }

    private static boolean isKnownId(String id) {
        for (String k : KNOWN_IDS) {
            if (k.equals(id)) {
                return true;
            }
        }
        return false;
    }

    private static boolean finite(float value) {
        return (Float.isNaN(value) || Float.isInfinite(value)) ? false : true;
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudLayoutStore$HudElementState.class */
    @Environment(EnvType.CLIENT)
    static final class HudElementState {
        String id;
        float x;
        float y;
        float width;
        float height;
        boolean visible;
        float scale;
        transient boolean positioned;
        int anchorX;
        boolean anchorBottom;
        float refScreenW;
        float refScreenH;
        transient boolean anchored;
        boolean autoPlaced;
        float placedGuiScale;
        float placedUiScale;

        HudElementState() {
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudLayoutStore$ConfigFile.class */
    @Environment(EnvType.CLIENT)
    private static final class ConfigFile {
        int version;
        List<HudElementState> elements;

        private ConfigFile() {
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudLayoutStore$Dimensions.class */
    @Environment(EnvType.CLIENT)
    private static final class Dimensions extends Record {
        private final float width;
        private final float height;

        private Dimensions(float width, float height) {
            this.width = width;
            this.height = height;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Dimensions.class), Dimensions.class, "width;height", "FIELD:Lnet/anubis/client/gui/HudLayoutStore$Dimensions;->width:F", "FIELD:Lnet/anubis/client/gui/HudLayoutStore$Dimensions;->height:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Dimensions.class), Dimensions.class, "width;height", "FIELD:Lnet/anubis/client/gui/HudLayoutStore$Dimensions;->width:F", "FIELD:Lnet/anubis/client/gui/HudLayoutStore$Dimensions;->height:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Dimensions.class, Object.class), Dimensions.class, "width;height", "FIELD:Lnet/anubis/client/gui/HudLayoutStore$Dimensions;->width:F", "FIELD:Lnet/anubis/client/gui/HudLayoutStore$Dimensions;->height:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public float width() {
            return this.width;
        }

        public float height() {
            return this.height;
        }
    }
}
