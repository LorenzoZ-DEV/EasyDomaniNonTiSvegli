package net.anubis.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import java.util.List;
import net.anubis.client.addon.AddonManager;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.module.setting.Setting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_310;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/config/ThemeStore.class */
@Environment(EnvType.CLIENT)
public final class ThemeStore {
    private static final int CONFIG_VERSION = 2;
    private static final String THEME_KEY = "anubisTheme";
    public final ColorSetting accent = new ColorSetting("Accent", -13338378);
    public final NumberSetting rounding = new NumberSetting("Corner radius", 10.0f, 0.0f, 16.0f).step(1.0f);
    public final NumberSetting backgroundDim = new NumberSetting("Background dim", 0.25f, 0.0f, 0.8f).step(0.05f);
    public final BooleanSetting menuBlur = new BooleanSetting("Menu blur", true);
    public final NumberSetting uiScale = new NumberSetting("UI scale", 1.0f, 0.75f, 1.5f).step(0.05f);
    public final NumberSetting animationSpeed = new NumberSetting("Animation speed", 1.0f, 0.0f, 2.0f).step(0.1f);
    public final BooleanSetting animateLines = new BooleanSetting("Animate patterns", true);
    public final BooleanSetting customMainMenu = new BooleanSetting("Custom menus", true);
    public final BooleanSetting communityPopup = new BooleanSetting("Community popup", true);
    public final Surface panels = new Surface("panels", "Panels", false, -14736856, 0.92f, 0.45f);
    public final Surface cards = new Surface("cards", "Cards", true, -15263458, 0.97f, 0.85f);
    public final Surface tabBar = new Surface("tabbar", "Tab bar", false, -15263458, 0.97f, 0.5f);
    private final List<Surface> surfaces = List.of(this.panels, this.cards, this.tabBar);
    private final List<Setting> shared;
    private final List<Setting> settings;
    private final JsonObject defaults;
    private boolean loaded;
    private boolean readOnly;
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private static final ThemeStore INSTANCE = new ThemeStore();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final List<Preset> PRESETS = List.of(new Preset("Default", -13338378, -14736856, -15263458), new Preset("Midnight", -7643914, -15001050, -15461603), new Preset("Rose", -770210, -14410720, -15002344), new Preset("Mint", -15681151, -15064288, -15459560), new Preset("Amber", -680437, -14409445, -15001324), new Preset("Mono", -1710101, -14671840, -15263977));

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/config/ThemeStore$Preset.class */
    @Environment(EnvType.CLIENT)
    public static final class Preset extends Record {
        private final String name;
        private final int accent;
        private final int panel;
        private final int card;

        public Preset(String name, int accent, int panel, int card) {
            this.name = name;
            this.accent = accent;
            this.panel = panel;
            this.card = card;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Preset.class), Preset.class, "name;accent;panel;card", "FIELD:Lnet/anubis/client/config/ThemeStore$Preset;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/config/ThemeStore$Preset;->accent:I", "FIELD:Lnet/anubis/client/config/ThemeStore$Preset;->panel:I", "FIELD:Lnet/anubis/client/config/ThemeStore$Preset;->card:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Preset.class), Preset.class, "name;accent;panel;card", "FIELD:Lnet/anubis/client/config/ThemeStore$Preset;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/config/ThemeStore$Preset;->accent:I", "FIELD:Lnet/anubis/client/config/ThemeStore$Preset;->panel:I", "FIELD:Lnet/anubis/client/config/ThemeStore$Preset;->card:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Preset.class, Object.class), Preset.class, "name;accent;panel;card", "FIELD:Lnet/anubis/client/config/ThemeStore$Preset;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/config/ThemeStore$Preset;->accent:I", "FIELD:Lnet/anubis/client/config/ThemeStore$Preset;->panel:I", "FIELD:Lnet/anubis/client/config/ThemeStore$Preset;->card:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public String name() {
            return this.name;
        }

        public int accent() {
            return this.accent;
        }

        public int panel() {
            return this.panel;
        }

        public int card() {
            return this.card;
        }
    }

    private ThemeStore() {
        List<Setting> all = new ArrayList<>(List.of(this.accent, this.rounding, this.backgroundDim, this.menuBlur, this.uiScale, this.animationSpeed, this.animateLines));
        for (Surface surface : this.surfaces) {
            all.addAll(surface.settings());
        }
        this.shared = List.copyOf(all);
        all.add(this.customMainMenu);
        all.add(this.communityPopup);
        this.settings = List.copyOf(all);
        this.defaults = snapshot(this.settings, true);
        this.customMainMenu.onChange(() -> {
            class_310 mc = class_310.method_1551();
            if (mc == null || mc.method_22683() == null) {
                return;
            }
            mc.method_24288();
        });
    }

    public static ThemeStore get() {
        INSTANCE.ensureLoaded();
        return INSTANCE;
    }

    public List<Surface> surfaces() {
        return this.surfaces;
    }

    public void applyPreset(Preset preset) {
        this.accent.setArgb(preset.accent());
        this.panels.color.setArgb(preset.panel());
        this.cards.color.setArgb(preset.card());
        this.tabBar.color.setArgb(preset.card());
    }

    public boolean matchesPreset(Preset preset) {
        return this.accent.rgb() == (preset.accent() & 16777215) && this.panels.color.rgb() == (preset.panel() & 16777215) && this.cards.color.rgb() == (preset.card() & 16777215);
    }

    public void resetToDefaults() {
        apply(this.shared, this.defaults, true);
    }

    public static Path backgroundsDir() {
        return FabricLoader.getInstance().getConfigDir().resolve(AddonManager.ENTRYPOINT).resolve("backgrounds");
    }

    public String exportString() {
        JsonObject root = new JsonObject();
        root.add(THEME_KEY, snapshot(this.shared, false));
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
            apply(this.shared, values.getAsJsonObject(), false);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public JsonObject profileSnapshot() {
        return snapshot(this.shared, true);
    }

    public void applyProfile(JsonObject values) {
        for (Setting setting : this.shared) {
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
        for (Setting setting : this.shared) {
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
                    if (version > 2) {
                        this.readOnly = true;
                        LOGGER.warn("[Anubis] Theme version {} is newer than supported {}; skipping load", Integer.valueOf(version), 2);
                        if (reader != null) {
                            reader.close();
                            return;
                        }
                        return;
                    }
                    if (root.has("values") && root.get("values").isJsonObject()) {
                        apply(this.settings, root.getAsJsonObject("values"), true);
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
                LOGGER.warn("[Anubis] Failed to load theme; preserving the file", e);
                this.readOnly = true;
            }
        }
    }

    public synchronized void save() {
        if (this.readOnly || !this.loaded) {
            return;
        }
        JsonObject root = new JsonObject();
        root.addProperty("version", 2);
        root.add("values", snapshot(this.settings, true));
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
            LOGGER.warn("[Anubis] Failed to save theme", e2);
        }
    }

    private static JsonObject snapshot(List<Setting> from, boolean withPrivate) {
        JsonObject values = new JsonObject();
        for (Setting setting : from) {
            if (withPrivate || !setting.isPrivate()) {
                values.add(setting.id(), setting.serialize());
            }
        }
        return values;
    }

    private static void apply(List<Setting> from, JsonObject values, boolean withPrivate) {
        for (Setting setting : from) {
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
        return FabricLoader.getInstance().getConfigDir().resolve(AddonManager.ENTRYPOINT).resolve("theme.json");
    }
}
