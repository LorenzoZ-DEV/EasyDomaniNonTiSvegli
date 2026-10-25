package net.anubis.client.config;

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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import net.anubis.client.addon.AddonManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/config/FriendStore.class */
@Environment(EnvType.CLIENT)
public final class FriendStore {
    private static final int CONFIG_VERSION = 1;
    private final Map<String, String> friends = new LinkedHashMap();
    private boolean loaded;
    private boolean readOnly;
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private static final FriendStore INSTANCE = new FriendStore();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Pattern VALID_NAME = Pattern.compile("[.*]?[A-Za-z0-9_]{1,16}");

    private FriendStore() {
    }

    public static FriendStore getInstance() {
        return INSTANCE;
    }

    public static boolean isValidName(String name) {
        return name != null && VALID_NAME.matcher(name).matches();
    }

    public synchronized boolean isFriend(String name) {
        ensureLoaded();
        return name != null && this.friends.containsKey(name.toLowerCase(Locale.ROOT));
    }

    public synchronized List<String> all() {
        ensureLoaded();
        return List.copyOf(this.friends.values());
    }

    public synchronized boolean add(String name) {
        ensureLoaded();
        String trimmed = name == null ? "" : name.trim();
        if (!isValidName(trimmed) || this.friends.putIfAbsent(trimmed.toLowerCase(Locale.ROOT), trimmed) != null) {
            return false;
        }
        save();
        return true;
    }

    public synchronized boolean remove(String name) {
        ensureLoaded();
        if (name == null || this.friends.remove(name.toLowerCase(Locale.ROOT)) == null) {
            return false;
        }
        save();
        return true;
    }

    public synchronized void replaceAll(Collection<String> names) {
        ensureLoaded();
        this.friends.clear();
        Iterator<String> it = names.iterator();
        while (it.hasNext()) {
            String name = it.next();
            String trimmed = name == null ? "" : name.trim();
            if (isValidName(trimmed)) {
                this.friends.putIfAbsent(trimmed.toLowerCase(Locale.ROOT), trimmed);
            }
        }
        save();
    }

    public synchronized void load() {
        if (this.loaded) {
            return;
        }
        this.loaded = true;
        this.friends.clear();
        Path path = configPath();
        if (Files.isRegularFile(path, new LinkOption[0])) {
            try {
                Reader reader = Files.newBufferedReader(path);
                try {
                    JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                    int version = root.has("version") ? root.get("version").getAsInt() : 0;
                    if (version > 1) {
                        this.readOnly = true;
                        LOGGER.warn("[Anubis] Friend list version {} is newer than supported {}; skipping load", Integer.valueOf(version), 1);
                        if (reader != null) {
                            reader.close();
                            return;
                        }
                        return;
                    }
                    if (!root.has("friends") || !root.get("friends").isJsonArray()) {
                        if (reader != null) {
                            reader.close();
                            return;
                        }
                        return;
                    }
                    for (JsonElement element : root.getAsJsonArray("friends")) {
                        if (element.isJsonPrimitive()) {
                            String name = element.getAsString().trim();
                            if (isValidName(name)) {
                                this.friends.putIfAbsent(name.toLowerCase(Locale.ROOT), name);
                            }
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
                LOGGER.warn("[Anubis] Failed to load friend list; preserving the file", e);
                this.friends.clear();
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
        for (String name : this.friends.values()) {
            list.add(name);
        }
        root.add("friends", list);
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
            LOGGER.warn("[Anubis] Failed to save friend list", e2);
        }
    }

    private void ensureLoaded() {
        if (!this.loaded) {
            load();
        }
    }

    private Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(AddonManager.ENTRYPOINT).resolve("friends.json");
    }
}
