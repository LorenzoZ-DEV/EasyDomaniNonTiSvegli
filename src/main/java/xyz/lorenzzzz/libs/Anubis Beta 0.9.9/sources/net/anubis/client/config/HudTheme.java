package net.anubis.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import net.anubis.client.addon.AddonManager;
import net.anubis.client.gui.HudLayoutStore;
import net.anubis.client.gui.ToggleToasts;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.module.setting.Setting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/config/HudTheme.class */
@Environment(EnvType.CLIENT)
public final class HudTheme {
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private static final HudTheme INSTANCE = new HudTheme();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final int CONFIG_VERSION = 1;
    private static final String THEME_KEY = "anubisHudTheme";
    private static final int DEFAULT_COLOR = -15329251;
    private static final float DEFAULT_OPACITY = 0.85f;
    private static final float DEFAULT_IMAGE_OPACITY = 0.4f;
    private static final float ROWS_OPACITY = 0.6f;
    public final NumberSetting rounding = new NumberSetting("Corner radius", 8.0f, 0.0f, 16.0f).step(1.0f);
    public final Surface infoBar = surface("hud_info_bar", "Info Bar");
    public final Surface coordinates = surface("hud_coordinates", "Coordinates", ROWS_OPACITY);
    public final Surface moduleList = surface("hud_module_list", "Module List", ROWS_OPACITY);
    public final Surface potions = surface("hud_potions", "Potions", ROWS_OPACITY);
    public final Surface regionMap = surface("hud_region_map", "Region Map");
    public final Surface antiVanish = surface("hud_anti_vanish", "Anti Vanish", ROWS_OPACITY);
    public final Surface staffList = surface("hud_staff_list", "Staff List", ROWS_OPACITY);
    public final Surface spotify = surface("hud_spotify", "Spotify");
    public final Surface hotkeys = surface("hud_hotkeys", "Hotkeys", ROWS_OPACITY);
    public final Surface radar = surface("hud_radar", "Radar", ROWS_OPACITY);
    public final Surface toasts = surface("hud_toasts", "Toggle Toasts");
    public final Surface targetCard = surface("hud_target_card", "Target HUD");
    public final Surface armorStatus = surface("hud_armor_status", "Armor HUD", ROWS_OPACITY);
    public final Surface itemCooldowns = surface("hud_item_cooldowns", "Cooldown", ROWS_OPACITY);
    private final List<Surface> surfaces = List.of((Object[]) new Surface[]{this.infoBar, this.coordinates, this.moduleList, this.potions, this.regionMap, this.antiVanish, this.staffList, this.spotify, this.hotkeys, this.radar, this.toasts, this.targetCard, this.armorStatus, this.itemCooldowns});
    private final List<Setting> settings;
    private final JsonObject defaults;
    private boolean loaded;
    private boolean readOnly;

    private HudTheme() {
        List<Setting> all = new ArrayList<>(List.of(this.rounding));
        for (Surface surface : this.surfaces) {
            all.addAll(surface.settings());
        }
        this.settings = List.copyOf(all);
        this.defaults = snapshot(true);
    }

    private static Surface surface(String key, String label) {
        return surface(key, label, DEFAULT_OPACITY);
    }

    private static Surface surface(String key, String label, float opacity) {
        return new Surface(key, label, false, DEFAULT_COLOR, opacity, DEFAULT_IMAGE_OPACITY);
    }

    public static HudTheme get() {
        INSTANCE.ensureLoaded();
        return INSTANCE;
    }

    public List<Surface> surfaces() {
        return this.surfaces;
    }

    public Surface forElementId(String id) {
        if (HudLayoutStore.INFO_BAR_ID.equals(id)) {
            return this.infoBar;
        }
        if (HudLayoutStore.COORDINATES_ID.equals(id)) {
            return this.coordinates;
        }
        if (HudLayoutStore.MODULE_LIST_ID.equals(id)) {
            return this.moduleList;
        }
        if (HudLayoutStore.POTION_TRACKER_ID.equals(id)) {
            return this.potions;
        }
        if (HudLayoutStore.REGION_MAP_ID.equals(id)) {
            return this.regionMap;
        }
        if (HudLayoutStore.ANTI_VANISH_ID.equals(id)) {
            return this.antiVanish;
        }
        if (HudLayoutStore.STAFF_LIST_ID.equals(id)) {
            return this.staffList;
        }
        if (HudLayoutStore.SPOTIFY_ID.equals(id)) {
            return this.spotify;
        }
        if (HudLayoutStore.HOTKEYS_ID.equals(id)) {
            return this.hotkeys;
        }
        if (HudLayoutStore.RADAR_ID.equals(id)) {
            return this.radar;
        }
        if (ToggleToasts.ID.equals(id)) {
            return this.toasts;
        }
        if (HudLayoutStore.TARGET_CARD_ID.equals(id)) {
            return this.targetCard;
        }
        if (HudLayoutStore.ARMOR_STATUS_ID.equals(id)) {
            return this.armorStatus;
        }
        if (HudLayoutStore.ITEM_COOLDOWNS_ID.equals(id)) {
            return this.itemCooldowns;
        }
        return null;
    }

    public void resetToDefaults() {
        apply(this.defaults, true);
    }

    public String exportString() {
        JsonObject root = new JsonObject();
        root.add(THEME_KEY, snapshot(false));
        return root.toString();
    }

    public boolean importString(String text) {
        if (text == null || text.isBlank()) {
            return false;
        }
        try {
            JsonElement parsed = JsonParser.parseString(text.trim());
            if (!parsed.isJsonObject() || !parsed.getAsJsonObject().has(THEME_KEY)) {
                return false;
            }
            JsonElement values = parsed.getAsJsonObject().get(THEME_KEY);
            if (!values.isJsonObject()) {
                return false;
            }
            apply(values.getAsJsonObject(), false);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public JsonObject profileSnapshot() {
        return snapshot(true);
    }

    public void applyProfile(JsonObject values) {
        for (Setting setting : this.settings) {
            JsonElement value = values.get(setting.id());
            if (value == null && !setting.isPrivate()) {
                value = this.defaults.get(setting.id());
            }
            if (value != null) {
                setting.deserialize(value);
            }
        }
    }

    public JsonObject shareable(JsonObject values) {
        JsonObject kept = new JsonObject();
        for (Setting setting : this.settings) {
            JsonElement value = values.get(setting.id());
            if (value != null && !setting.isPrivate() && !Setting.sameValue(value, this.defaults.get(setting.id()))) {
                kept.add(setting.id(), value.deepCopy());
            }
        }
        return kept;
    }

    public synchronized void load() {
        if (this.loaded) {
            return;
        }
        this.loaded = true;
        Path path = configPath();
        if (Files.isRegularFile(path, new LinkOption[0])) {
            try {
                Reader reader = Files.newBufferedReader(path);
                try {
                    JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                    int version = root.has("version") ? root.get("version").getAsInt() : 0;
                    if (version > 1) {
                        this.readOnly = true;
                        LOGGER.warn("[Anubis] HUD theme version {} is newer than supported {}; skipping load", Integer.valueOf(version), 1);
                        if (reader != null) {
                            reader.close();
                            return;
                        }
                        return;
                    }
                    if (root.has("values") && root.get("values").isJsonObject()) {
                        apply(root.getAsJsonObject("values"), true);
                    }
                    if (reader != null) {
                        reader.close();
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
                LOGGER.warn("[Anubis] Failed to load HUD theme; preserving the file", e);
                this.readOnly = true;
            }
        }
    }

    public synchronized void save() {
        if (this.readOnly || !this.loaded) {
            return;
        }
        JsonObject root = new JsonObject();
        root.addProperty("version", 1);
        root.add("values", snapshot(true));
        Path path = configPath();
        try {
            Files.createDirectories(path.getParent(), new FileAttribute[0]);
            Path tmp = path.resolveSibling(path.getFileName().toString() + ".tmp");
            Writer writer = Files.newBufferedWriter(tmp, new OpenOption[0]);
            try {
                GSON.toJson(root, writer);
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
            LOGGER.warn("[Anubis] Failed to save HUD theme", e2);
        }
    }

    private JsonObject snapshot(boolean withPrivate) {
        JsonObject values = new JsonObject();
        for (Setting setting : this.settings) {
            if (withPrivate || !setting.isPrivate()) {
                values.add(setting.id(), setting.serialize());
            }
        }
        return values;
    }

    private void apply(JsonObject values, boolean withPrivate) {
        for (Setting setting : this.settings) {
            if (withPrivate || !setting.isPrivate()) {
                if (values.has(setting.id())) {
                    setting.deserialize(values.get(setting.id()));
                }
            }
        }
    }

    private void ensureLoaded() {
        if (!this.loaded) {
            load();
        }
    }

    private Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(AddonManager.ENTRYPOINT).resolve("hud_theme.json");
    }
}
