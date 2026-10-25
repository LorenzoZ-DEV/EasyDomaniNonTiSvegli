package net.anubis.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.anubis.client.addon.AddonManager;
import net.anubis.client.event.impl.ModuleToggleEvent;
import net.anubis.client.input.ModuleKeybinds;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.ModuleRevision;
import net.anubis.client.module.setting.Setting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/config/ModuleConfigStore.class */
@Environment(EnvType.CLIENT)
public final class ModuleConfigStore {
    private static final int CONFIG_VERSION = 3;
    private static final long AUTOSAVE_DELAY_NANOS = 2000000000;
    private boolean loaded;
    private boolean readOnly;
    private String activeProfile;
    private long observedAt;
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private static final ModuleConfigStore INSTANCE = new ModuleConfigStore();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<String, String> RENAMED_MODULES = Map.of("activity-bypass", "player-bypass", "Activity Bypass", "player-bypass");
    private final Map<String, JsonObject> orphanModules = new LinkedHashMap();
    private final Map<String, Boolean> pendingEnabled = new LinkedHashMap();
    private final Map<String, JsonObject> retainedEntries = new LinkedHashMap();
    private long savedRevision = -1;
    private long observedRevision = -1;

    private ModuleConfigStore() {
    }

    public static ModuleConfigStore getInstance() {
        return INSTANCE;
    }

    public synchronized void load() {
        if (this.loaded) {
            return;
        }
        this.loaded = true;
        this.orphanModules.clear();
        this.pendingEnabled.clear();
        readConfig();
        for (ClientModule module : ModuleRegistry.all()) {
            if (module.enabledByDefault() && !this.retainedEntries.containsKey(module.id())) {
                this.pendingEnabled.put(module.id(), true);
            }
        }
    }

    private void readConfig() {
        Path path = configPath();
        if (!Files.isRegularFile(path, new LinkOption[0])) {
            return;
        }
        try {
            Reader reader = Files.newBufferedReader(path);
            try {
                JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                applyConfigRoot(root);
                if (root.has("activeProfile") && root.get("activeProfile").isJsonPrimitive()) {
                    this.activeProfile = root.get("activeProfile").getAsString();
                }
                int version = root.has("version") ? root.get("version").getAsInt() : 1;
                if (!this.readOnly && version < 3) {
                    Path backup = path.resolveSibling(String.valueOf(path.getFileName()) + ".v" + version + ".bak");
                    if (!Files.exists(backup, new LinkOption[0])) {
                        Files.copy(path, backup, new CopyOption[0]);
                    }
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
            LOGGER.warn("[Anubis] Failed to load module config; preserving the file", e);
            this.orphanModules.clear();
            this.pendingEnabled.clear();
            this.readOnly = true;
        }
    }

    public synchronized boolean enablesOnLoad(ClientModule module) {
        return Boolean.TRUE.equals(this.pendingEnabled.get(module.id()));
    }

    public synchronized void applyPendingEnabled() {
        if (this.pendingEnabled.isEmpty()) {
            return;
        }
        ModuleToggleEvent.silently(() -> {
            ClientModule module;
            for (Map.Entry<String, Boolean> entry : this.pendingEnabled.entrySet()) {
                if (Boolean.TRUE.equals(entry.getValue()) && (module = ModuleRegistry.resolve(entry.getKey())) != null && module.persistEnabled()) {
                    module.setEnabled(true);
                }
            }
        });
        this.pendingEnabled.clear();
    }

    public synchronized void save() {
        ensureLoaded();
        if (this.readOnly) {
            return;
        }
        long revision = ModuleRevision.current();
        JsonObject root = buildConfigRoot();
        if (this.activeProfile != null) {
            root.addProperty("activeProfile", this.activeProfile);
        }
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
                this.savedRevision = revision;
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
            LOGGER.warn("[Anubis] Failed to save module config", e2);
        }
    }

    public synchronized void tickAutosave() {
        long now = System.nanoTime();
        if (autosaveDue(ModuleRevision.current(), now)) {
            save();
            this.observedAt = now;
        }
    }

    private boolean autosaveDue(long revision, long now) {
        if (!this.loaded || this.readOnly) {
            return false;
        }
        if (this.savedRevision < 0) {
            this.observedRevision = revision;
            this.savedRevision = revision;
            return false;
        }
        if (revision == this.savedRevision) {
            return false;
        }
        if (revision == this.observedRevision) {
            return now - this.observedAt >= AUTOSAVE_DELAY_NANOS;
        }
        this.observedRevision = revision;
        this.observedAt = now;
        return false;
    }

    public synchronized boolean hasUnsavedChanges() {
        return (this.readOnly || this.savedRevision < 0 || ModuleRevision.current() == this.savedRevision) ? false : true;
    }

    public synchronized boolean isReadOnly() {
        return this.readOnly;
    }

    public synchronized String activeProfile() {
        return this.activeProfile;
    }

    synchronized void setActiveProfile(String name) {
        this.activeProfile = name;
    }

    public synchronized JsonObject snapshot() {
        ensureLoaded();
        JsonObject root = buildConfigRoot();
        JsonObject modules = root.getAsJsonObject("modules");
        for (ClientModule module : ModuleRegistry.all()) {
            if (module.inConfigs()) {
                modules.add(module.id(), withoutPrivate(module, modules.getAsJsonObject(module.id())));
            } else {
                modules.remove(module.id());
            }
        }
        return root;
    }

    public synchronized boolean applyProfile(JsonObject root) {
        ensureLoaded();
        int version = profileVersion(root);
        if (version < 0) {
            return false;
        }
        List<ClientModule> modules = ModuleRegistry.all();
        Map<ClientModule, JsonObject> matched = matchEntries(root, version, modules);
        Map<String, Boolean> enable = new HashMap<>();
        ModuleToggleEvent.silently(() -> {
            Iterator it = modules.iterator();
            while (it.hasNext()) {
                ClientModule module = (ClientModule) it.next();
                if (module.inConfigs()) {
                    JsonObject entry = (JsonObject) matched.get(module);
                    for (Setting setting : module.settings()) {
                        if (!setting.isPrivate()) {
                            setting.resetToDefault();
                        }
                    }
                    module.setKeybind(-1);
                    module.setHoldMode(false);
                    if (entry != null) {
                        JsonObject entry2 = withoutPrivate(module, entry);
                        applyModuleEntry(module, entry2, enable);
                        this.retainedEntries.put(module.id(), entry2);
                    } else {
                        this.retainedEntries.remove(module.id());
                    }
                    if (module.persistEnabled()) {
                        module.setEnabled(Boolean.TRUE.equals(enable.get(module.id())));
                    }
                }
            }
        });
        this.pendingEnabled.clear();
        return true;
    }

    public synchronized JsonObject shareable(JsonObject root) {
        int version = profileVersion(root);
        if (version < 0) {
            return null;
        }
        List<ClientModule> modules = ModuleRegistry.all();
        Map<ClientModule, JsonObject> matched = matchEntries(root, version, modules);
        JsonObject shared = new JsonObject();
        for (ClientModule module : modules) {
            JsonObject entry = module.inConfigs() ? matched.get(module) : null;
            JsonObject kept = entry == null ? null : shareableEntry(module, entry);
            if (kept != null) {
                shared.add(module.id(), kept);
            }
        }
        JsonObject out = new JsonObject();
        out.addProperty("version", 3);
        out.add("modules", shared);
        return out;
    }

    private static int profileVersion(JsonObject root) {
        if (root == null || !root.has("modules") || !root.get("modules").isJsonObject()) {
            return -1;
        }
        try {
            int version = root.has("version") ? root.get("version").getAsInt() : 0;
            if (version > 3) {
                return -1;
            }
            return version;
        } catch (RuntimeException e) {
            return -1;
        }
    }

    private static Map<ClientModule, JsonObject> matchEntries(JsonObject root, int version, List<ClientModule> modules) {
        Map<String, ClientModule> byKey = new HashMap<>();
        for (ClientModule module : modules) {
            byKey.putIfAbsent(module.name(), module);
        }
        for (ClientModule module2 : modules) {
            byKey.put(module2.id(), module2);
        }
        Map<ClientModule, JsonObject> matched = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : renameModules(root.getAsJsonObject("modules"), version).entrySet()) {
            if (entry.getValue().isJsonObject()) {
                JsonObject moduleEntry = entry.getValue().getAsJsonObject();
                ClientModule module3 = byKey.get(entry.getKey());
                if (module3 == null && moduleEntry.has("name") && moduleEntry.get("name").isJsonPrimitive()) {
                    module3 = byKey.get(moduleEntry.get("name").getAsString());
                }
                if (module3 != null) {
                    matched.putIfAbsent(module3, moduleEntry);
                }
            }
        }
        return matched;
    }

    private static JsonObject shareableEntry(ClientModule module, JsonObject entry) {
        JsonObject out = new JsonObject();
        if (module.persistEnabled() && flag(entry, "enabled")) {
            out.addProperty("enabled", true);
        }
        int key = number(entry, "keybind", -1);
        if (key != -1 && ModuleKeybinds.isBindable(key)) {
            out.addProperty("keybind", Integer.valueOf(key));
        }
        if (flag(entry, "holdMode")) {
            out.addProperty("holdMode", true);
        }
        JsonObject values = (entry.has("settings") && entry.get("settings").isJsonObject()) ? entry.getAsJsonObject("settings") : null;
        JsonObject settings = new JsonObject();
        if (values != null) {
            for (Setting setting : module.settings()) {
                if (!setting.isPrivate()) {
                    JsonElement value = values.has(setting.id()) ? values.get(setting.id()) : values.get(setting.name());
                    if (value != null && !value.isJsonNull() && !setting.isDefault(value)) {
                        settings.add(setting.id(), setting.compact(value.deepCopy()));
                    }
                }
            }
        }
        if (settings.size() > 0) {
            out.add("settings", settings);
        }
        if (out.size() == 0) {
            return null;
        }
        int saved = number(entry, "settingsVersion", 0);
        if (saved > 0) {
            out.addProperty("settingsVersion", Integer.valueOf(saved));
        }
        return out;
    }

    private static JsonObject withoutPrivate(ClientModule module, JsonObject entry) {
        JsonObject copy = entry.deepCopy();
        if (copy.has("settings") && copy.get("settings").isJsonObject()) {
            JsonObject settings = copy.getAsJsonObject("settings");
            for (Setting setting : module.settings()) {
                if (setting.isPrivate()) {
                    settings.remove(setting.id());
                    settings.remove(setting.name());
                }
            }
        }
        return copy;
    }

    private static boolean flag(JsonObject entry, String key) {
        JsonElement value = entry.get(key);
        if (value == null || !value.isJsonPrimitive()) {
            return false;
        }
        try {
            return value.getAsBoolean();
        } catch (RuntimeException e) {
            return false;
        }
    }

    private static int number(JsonObject entry, String key, int fallback) {
        JsonElement value = entry.get(key);
        if (value == null || !value.isJsonPrimitive()) {
            return fallback;
        }
        try {
            return value.getAsInt();
        } catch (RuntimeException e) {
            return fallback;
        }
    }

    public synchronized void adopt(ClientModule module) {
        JsonObject entry;
        if (this.loaded && (entry = this.orphanModules.remove(module.id())) != null) {
            this.retainedEntries.put(module.id(), entry.deepCopy());
            applyModuleEntry(module, entry, this.pendingEnabled);
        }
    }

    public synchronized void release(ClientModule module) {
        if (this.loaded) {
            JsonObject entry = serializeModule(module);
            if (Boolean.TRUE.equals(this.pendingEnabled.remove(module.id()))) {
                entry.addProperty("enabled", true);
            }
            this.orphanModules.put(module.id(), entry);
            this.retainedEntries.remove(module.id());
        }
    }

    public static String renamedModuleId(String key) {
        for (Map.Entry<String, String> rename : RENAMED_MODULES.entrySet()) {
            if (rename.getKey().equalsIgnoreCase(key)) {
                return rename.getValue();
            }
        }
        return null;
    }

    private void applyConfigRoot(JsonObject root) {
        if (root == null) {
            return;
        }
        int version = root.has("version") ? root.get("version").getAsInt() : 0;
        if (version > 3) {
            this.readOnly = true;
            LOGGER.warn("[Anubis] Module config version {} is newer than supported {}; skipping load", Integer.valueOf(version), 3);
            return;
        }
        if (!root.has("modules") || !root.get("modules").isJsonObject()) {
            return;
        }
        JsonObject modules = renameModules(root.getAsJsonObject("modules"), version);
        for (Map.Entry<String, JsonElement> entry : modules.entrySet()) {
            String name = entry.getKey();
            if (entry.getValue().isJsonObject()) {
                JsonObject moduleEntry = entry.getValue().getAsJsonObject();
                ClientModule module = ModuleRegistry.resolve(name);
                if (module == null && version >= 2 && moduleEntry.has("name") && moduleEntry.get("name").isJsonPrimitive()) {
                    module = ModuleRegistry.resolve(moduleEntry.get("name").getAsString());
                }
                if (module == null) {
                    this.orphanModules.put(name, moduleEntry.deepCopy());
                } else {
                    this.retainedEntries.put(module.id(), moduleEntry.deepCopy());
                    applyModuleEntry(module, moduleEntry, this.pendingEnabled);
                }
            }
        }
    }

    private JsonObject buildConfigRoot() {
        JsonObject root = new JsonObject();
        root.addProperty("version", 3);
        JsonObject modulesObj = new JsonObject();
        for (ClientModule module : ModuleRegistry.all()) {
            modulesObj.add(module.id(), serializeModule(module));
        }
        for (Map.Entry<String, JsonObject> orphan : this.orphanModules.entrySet()) {
            if (!modulesObj.has(orphan.getKey())) {
                modulesObj.add(orphan.getKey(), orphan.getValue().deepCopy());
            }
        }
        root.add("modules", modulesObj);
        return root;
    }

    private void ensureLoaded() {
        if (!this.loaded) {
            load();
        }
    }

    private Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(AddonManager.ENTRYPOINT).resolve("modules.json");
    }

    private static void applyModuleEntry(ClientModule module, JsonObject entry, Map<String, Boolean> pendingEnabled) {
        if (entry.has("keybind") && entry.get("keybind").isJsonPrimitive()) {
            try {
                int key = entry.get("keybind").getAsInt();
                if (key == -1 || ModuleKeybinds.isBindable(key)) {
                    module.setKeybind(key);
                }
            } catch (RuntimeException e) {
            }
        }
        if (entry.has("holdMode") && entry.get("holdMode").isJsonPrimitive()) {
            try {
                module.setHoldMode(entry.get("holdMode").getAsBoolean());
            } catch (RuntimeException e2) {
            }
        }
        if (entry.has("settings") && entry.get("settings").isJsonObject()) {
            JsonObject settings = entry.getAsJsonObject("settings");
            for (Setting setting : module.settings()) {
                JsonElement value = settings.has(setting.id()) ? settings.get(setting.id()) : settings.get(setting.name());
                if (value != null) {
                    setting.deserialize(value);
                }
            }
        }
        migrateSettings(module, entry);
        if (!entry.has("enabled") || !entry.get("enabled").isJsonPrimitive()) {
            return;
        }
        try {
            boolean enabled = entry.get("enabled").getAsBoolean();
            if (!module.persistEnabled()) {
                return;
            }
            String name = module.id();
            if (enabled) {
                pendingEnabled.put(name, true);
            } else {
                module.setEnabled(false);
            }
        } catch (RuntimeException e3) {
        }
    }

    private static void migrateSettings(ClientModule module, JsonObject entry) {
        int current = module.settingsVersion();
        if (current <= 0) {
            return;
        }
        int saved = 0;
        if (entry.has("settingsVersion") && entry.get("settingsVersion").isJsonPrimitive()) {
            try {
                saved = entry.get("settingsVersion").getAsInt();
            } catch (RuntimeException e) {
            }
        }
        if (saved >= current) {
            return;
        }
        try {
            module.migrateSettings(saved);
        } catch (RuntimeException e2) {
            LOGGER.warn("[Anubis] Failed to migrate settings of '{}' from version {}", new Object[]{module.id(), Integer.valueOf(saved), e2});
        }
    }

    private static JsonObject renameModules(JsonObject modules, int version) {
        if (version >= 3) {
            return modules;
        }
        JsonObject renamed = new JsonObject();
        for (Map.Entry<String, JsonElement> entry : modules.entrySet()) {
            String id = RENAMED_MODULES.get(entry.getKey());
            if (id == null) {
                renamed.add(entry.getKey(), entry.getValue());
            } else if (!modules.has(id) && !renamed.has(id)) {
                renamed.add(id, entry.getValue());
            }
        }
        return renamed;
    }

    private JsonObject serializeModule(ClientModule module) {
        JsonObject previous = this.retainedEntries.get(module.id());
        JsonObject entry = previous == null ? new JsonObject() : previous.deepCopy();
        entry.addProperty("name", module.name());
        entry.addProperty("enabled", Boolean.valueOf(module.isEnabled()));
        entry.addProperty("keybind", Integer.valueOf(module.keybind()));
        entry.addProperty("holdMode", Boolean.valueOf(module.isHoldMode()));
        if (module.settingsVersion() > 0) {
            entry.addProperty("settingsVersion", Integer.valueOf(module.settingsVersion()));
        }
        JsonObject settings = (entry.has("settings") && entry.get("settings").isJsonObject()) ? entry.getAsJsonObject("settings").deepCopy() : new JsonObject();
        for (Setting setting : module.settings()) {
            settings.remove(setting.name());
            settings.add(setting.id(), setting.serialize());
        }
        entry.add("settings", settings);
        return entry;
    }
}
