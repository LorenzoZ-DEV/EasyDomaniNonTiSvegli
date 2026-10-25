package net.anubis.client.proxy;

import com.google.common.net.InetAddresses;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;
import net.anubis.client.addon.AddonManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/proxy/Proxies.class */
@Environment(EnvType.CLIENT)
public final class Proxies {
    private static final int CONFIG_VERSION = 1;
    private final List<Proxy> proxies = new CopyOnWriteArrayList();
    private final List<Proxy> view = Collections.unmodifiableList(this.proxies);
    private volatile boolean loaded;
    private boolean readOnly;
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private static final Proxies INSTANCE = new Proxies();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Pattern IPV4 = Pattern.compile("\\d{1,3}(?:\\.\\d{1,3}){3}");
    private static final Pattern HOST_NAME = Pattern.compile("[A-Za-z0-9_-]+(?:\\.[A-Za-z0-9_-]+)*");

    private Proxies() {
    }

    public static Proxies get() {
        return INSTANCE;
    }

    public List<Proxy> list() {
        ensureLoaded();
        return this.view;
    }

    public Proxy enabled() {
        ensureLoaded();
        for (Proxy proxy : this.proxies) {
            if (proxy.enabled) {
                return proxy;
            }
        }
        return null;
    }

    public synchronized void setEnabled(Proxy proxy, boolean on) {
        ensureLoaded();
        if (this.proxies.contains(proxy)) {
            if (on) {
                if (!proxy.isValid()) {
                    return;
                }
                Iterator<Proxy> it = this.proxies.iterator();
                while (it.hasNext()) {
                    Proxy other = it.next();
                    other.enabled = other == proxy;
                }
            } else {
                proxy.enabled = false;
            }
            save();
        }
    }

    public synchronized void remove(Proxy proxy) {
        ensureLoaded();
        if (this.proxies.remove(proxy)) {
            save();
        }
    }

    public synchronized void removeAll(Collection<Proxy> gone) {
        ensureLoaded();
        if (this.proxies.removeAll(Set.copyOf(gone))) {
            save();
        }
    }

    public synchronized void checked() {
        ensureLoaded();
        save();
    }

    public synchronized int importText(String text) {
        ensureLoaded();
        if (text == null || text.isBlank()) {
            return 0;
        }
        Set<String> known = new HashSet<>();
        Iterator<Proxy> it = this.proxies.iterator();
        while (it.hasNext()) {
            known.add(key(it.next()));
        }
        List<Proxy> found = new ArrayList<>();
        for (String line : text.split("\\R")) {
            Proxy proxy = parseLine(line);
            if (proxy != null && known.add(key(proxy))) {
                found.add(proxy);
            }
        }
        if (found.isEmpty()) {
            return 0;
        }
        this.proxies.addAll(found);
        save();
        return found.size();
    }

    synchronized Proxy enabledCopy() {
        Proxy proxy = enabled();
        if (proxy == null) {
            return null;
        }
        return proxy.copy();
    }

    private void keepOneEnabled() {
        Proxy keep = null;
        for (Proxy proxy : this.proxies) {
            if (keep == null && proxy.enabled && proxy.isValid()) {
                keep = proxy;
            }
        }
        Iterator<Proxy> it = this.proxies.iterator();
        while (it.hasNext()) {
            Proxy proxy2 = it.next();
            proxy2.enabled = proxy2 == keep;
        }
    }

    private static String key(Proxy proxy) {
        return proxy.displayAddress().toLowerCase(Locale.ROOT);
    }

    static Proxy parseLine(String line) {
        String host;
        String rest;
        String rest2 = line == null ? "" : line.strip();
        if (rest2.isEmpty() || rest2.startsWith("#")) {
            return null;
        }
        ProxyType type = ProxyType.SOCKS5;
        int scheme = rest2.indexOf("://");
        if (scheme >= 0) {
            type = ProxyType.parse(rest2.substring(0, scheme));
            if (type == null) {
                return null;
            }
            rest2 = rest2.substring(scheme + 3);
            if (rest2.endsWith("/")) {
                rest2 = rest2.substring(0, rest2.length() - 1);
            }
        }
        String username = "";
        String password = "";
        int at = rest2.lastIndexOf(64);
        boolean uriLogin = at >= 0;
        if (uriLogin) {
            String login = rest2.substring(0, at);
            rest2 = rest2.substring(at + 1);
            int colon = login.indexOf(58);
            username = colon < 0 ? login : login.substring(0, colon);
            password = colon < 0 ? "" : login.substring(colon + 1);
        }
        if (rest2.startsWith("[")) {
            int close = rest2.indexOf(93);
            if (close < 0 || close + 1 >= rest2.length() || rest2.charAt(close + 1) != ':') {
                return null;
            }
            host = rest2.substring(1, close);
            rest = rest2.substring(close + 2);
        } else {
            int colon2 = rest2.indexOf(58);
            if (colon2 < 0) {
                return null;
            }
            host = rest2.substring(0, colon2);
            rest = rest2.substring(colon2 + 1);
        }
        String[] parts = rest.split(":", 3);
        int port = parsePort(parts[0]);
        String host2 = normalizeHost(host);
        if (port < 0 || host2 == null) {
            return null;
        }
        if (parts.length > 1) {
            if (uriLogin || parts[1].isEmpty()) {
                return null;
            }
            username = parts[1];
            password = parts.length > 2 ? parts[2] : "";
        }
        Proxy proxy = new Proxy();
        proxy.type = type;
        proxy.address = host2;
        proxy.port = port;
        proxy.username = username;
        proxy.password = password;
        proxy.name = proxy.displayAddress();
        return proxy;
    }

    private static int parsePort(String text) {
        if (text.isEmpty() || text.length() > 5) {
            return -1;
        }
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) < '0' || text.charAt(i) > '9') {
                return -1;
            }
        }
        int port = Integer.parseInt(text);
        if (port < 1 || port > 65535) {
            return -1;
        }
        return port;
    }

    private static String normalizeHost(String host) {
        if (IPV4.matcher(host).matches()) {
            StringBuilder plain = new StringBuilder();
            for (String octet : host.split("\\.")) {
                int value = Integer.parseInt(octet);
                if (value > 255) {
                    return null;
                }
                if (!plain.isEmpty()) {
                    plain.append('.');
                }
                plain.append(value);
            }
            return plain.toString();
        }
        if (host.indexOf(58) >= 0) {
            if (InetAddresses.isInetAddress(host)) {
                return host;
            }
            return null;
        }
        if (HOST_NAME.matcher(host).matches()) {
            return host;
        }
        return null;
    }

    private void ensureLoaded() {
        if (!this.loaded) {
            load();
        }
    }

    private synchronized void load() {
        if (this.loaded) {
            return;
        }
        Path path = configPath();
        List<Proxy> saved = new ArrayList<>();
        if (Files.isRegularFile(path, new LinkOption[0])) {
            try {
                Reader reader = Files.newBufferedReader(path);
                try {
                    JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                    int version = integer(root, "version");
                    if (version > 1) {
                        this.readOnly = true;
                        LOGGER.warn("[Anubis] Proxy list version {} is newer than supported {}; skipping load", Integer.valueOf(version), 1);
                    } else {
                        JsonArray jsonArray = root.get("proxies");
                        if (jsonArray instanceof JsonArray) {
                            JsonArray<JsonObject> list = jsonArray;
                            for (JsonObject jsonObject : list) {
                                if (jsonObject instanceof JsonObject) {
                                    JsonObject entry = jsonObject;
                                    saved.add(readEntry(entry));
                                }
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
                LOGGER.warn("[Anubis] Failed to load the proxy list; starting empty", e);
                saved.clear();
                setAside(path);
            }
        }
        this.proxies.addAll(saved);
        keepOneEnabled();
        this.loaded = true;
    }

    private void setAside(Path path) {
        try {
            Files.move(path, path.resolveSibling(String.valueOf(path.getFileName()) + ".corrupt"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOGGER.warn("[Anubis] Couldn't move the broken proxy list aside; not saving proxies this session", e);
            this.readOnly = true;
        }
    }

    /* JADX WARN: Code duplicated, block: B:13:0x0083  */
    private static Proxy readEntry(JsonObject json) {
        boolean z;
        Proxy proxy = new Proxy();
        proxy.name = string(json, "name");
        ProxyType type = ProxyType.parse(string(json, "type"));
        proxy.type = type == null ? ProxyType.SOCKS5 : type;
        proxy.address = string(json, "address");
        proxy.port = integer(json, "port");
        proxy.username = string(json, "username");
        proxy.password = string(json, "password");
        JsonPrimitive jsonPrimitive = json.get("enabled");
        if (jsonPrimitive instanceof JsonPrimitive) {
            JsonPrimitive flag = jsonPrimitive;
            if (flag.isBoolean() && flag.getAsBoolean()) {
                z = true;
            } else {
                z = false;
            }
        } else {
            z = false;
        }
        proxy.enabled = z;
        JsonPrimitive jsonPrimitive2 = json.get("ping");
        if (jsonPrimitive2 instanceof JsonPrimitive) {
            JsonPrimitive ping = jsonPrimitive2;
            if (ping.isNumber()) {
                proxy.ping = Math.max(-1, ping.getAsInt());
            }
        }
        return proxy;
    }

    private static String string(JsonObject json, String key) {
        JsonPrimitive jsonPrimitive = json.get(key);
        if (!(jsonPrimitive instanceof JsonPrimitive)) {
            return "";
        }
        JsonPrimitive value = jsonPrimitive;
        return value.getAsString();
    }

    private static int integer(JsonObject json, String key) {
        JsonPrimitive jsonPrimitive = json.get(key);
        if (jsonPrimitive instanceof JsonPrimitive) {
            JsonPrimitive value = jsonPrimitive;
            if (value.isNumber()) {
                return value.getAsInt();
            }
        }
        return 0;
    }

    private void save() {
        if (this.readOnly) {
            return;
        }
        JsonObject root = new JsonObject();
        root.addProperty("version", 1);
        JsonArray list = new JsonArray();
        for (Proxy proxy : this.proxies) {
            JsonObject entry = new JsonObject();
            entry.addProperty("name", proxy.name == null ? "" : proxy.name);
            entry.addProperty("type", (proxy.type == null ? ProxyType.SOCKS5 : proxy.type).name());
            entry.addProperty("address", proxy.address == null ? "" : proxy.address.trim());
            entry.addProperty("port", Integer.valueOf(proxy.port));
            if (proxy.username != null && !proxy.username.isEmpty()) {
                entry.addProperty("username", proxy.username);
            }
            if (proxy.password != null && !proxy.password.isEmpty()) {
                entry.addProperty("password", proxy.password);
            }
            entry.addProperty("enabled", Boolean.valueOf(proxy.enabled));
            if (proxy.ping != -2) {
                entry.addProperty("ping", Integer.valueOf(proxy.ping));
            }
            list.add(entry);
        }
        root.add("proxies", list);
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
            LOGGER.warn("[Anubis] Failed to save the proxy list", e2);
        }
    }

    private static Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(AddonManager.ENTRYPOINT).resolve("proxies.json");
    }
}
