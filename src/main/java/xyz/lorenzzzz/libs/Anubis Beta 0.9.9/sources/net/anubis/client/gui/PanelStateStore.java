package net.anubis.client.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
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
import java.util.LinkedHashSet;
import java.util.Set;
import net.anubis.client.addon.AddonManager;
import net.anubis.client.module.ModuleCategory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/PanelStateStore.class */
@Environment(EnvType.CLIENT)
public final class PanelStateStore {
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private static final PanelStateStore INSTANCE = new PanelStateStore();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final int CONFIG_VERSION = 1;
    private final Set<String> collapsed = new LinkedHashSet();
    private boolean loaded;
    private boolean readOnly;

    private PanelStateStore() {
    }

    public static PanelStateStore getInstance() {
        return INSTANCE;
    }

    public synchronized boolean isCollapsed(ModuleCategory category) {
        ensureLoaded();
        return this.collapsed.contains(category.name());
    }

    public synchronized void setCollapsed(ModuleCategory category, boolean fold) {
        ensureLoaded();
        boolean changed = fold ? this.collapsed.add(category.name()) : this.collapsed.remove(category.name());
        if (changed) {
            save();
        }
    }

    public synchronized void load() {
        if (this.loaded) {
            return;
        }
        this.loaded = true;
        this.collapsed.clear();
        Path path = configPath();
        if (Files.isRegularFile(path, new LinkOption[0])) {
            try {
                Reader reader = Files.newBufferedReader(path);
                try {
                    JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                    int version = root.has("version") ? root.get("version").getAsInt() : 0;
                    if (version > 1) {
                        this.readOnly = true;
                        LOGGER.warn("[Anubis] ClickGUI state version {} is newer than supported {}; skipping load", Integer.valueOf(version), 1);
                        if (reader != null) {
                            reader.close();
                            return;
                        }
                        return;
                    }
                    if (!root.has("collapsed") || !root.get("collapsed").isJsonArray()) {
                        if (reader != null) {
                            reader.close();
                            return;
                        }
                        return;
                    }
                    for (JsonElement element : root.getAsJsonArray("collapsed")) {
                        if (element.isJsonPrimitive()) {
                            this.collapsed.add(element.getAsString());
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
                LOGGER.warn("[Anubis] Failed to load the ClickGUI state; preserving the file", e);
                this.collapsed.clear();
                this.readOnly = true;
            }
        }
    }

    public synchronized void save() {
        if (this.readOnly) {
            return;
        }
        JsonObject root = new JsonObject();
        root.addProperty("version", 1);
        JsonArray list = new JsonArray();
        for (String id : this.collapsed) {
            list.add(id);
        }
        root.add("collapsed", list);
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
            LOGGER.warn("[Anubis] Failed to save the ClickGUI state", e2);
        }
    }

    private void ensureLoaded() {
        if (!this.loaded) {
            load();
        }
    }

    private Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(AddonManager.ENTRYPOINT).resolve("clickgui.json");
    }
}
