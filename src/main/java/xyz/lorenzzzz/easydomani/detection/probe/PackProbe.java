package xyz.lorenzzzz.easydomani.detection.probe;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import xyz.lorenzzzz.easydomani.EasyDomaniNonTiSvegli;

/**
 * Pack probe (SOLO diagnostica, richiede debug): resource pack del server (multi-pack, required=false) con la lang
 * key {@value #KEY} = valore casuale. A pack caricato il sign probe manda ctrl-pack: un client vanilla risolve la
 * key del pack, una mod che lascia passare solo le key vanilla rimanda la key grezza o il fallback.
 * overrideKeys: key di mod sovrascritte dal pack (valore {@code <value>-a}, {@code -b}, ...); la prima e' testata
 * come ctrl-ovr-translate/keybind. Il pack parte solo a fine coda dei probe veri (vedi SignProbe#drained).
 */
public final class PackProbe implements Listener {
    static final String KEY = "edn.probe";
    /** Max 60: i valori override aggiungono "-a".."-z" e restano nello stesso formato. */
    private static final Pattern VALUE = Pattern.compile("[A-Za-z0-9_.-]{4,60}");
    private static final Pattern LANG_KEY = Pattern.compile("[a-z0-9_.-]{1,128}");
    private static final int MAX_OVERRIDES = 26;
    /** pack_format/supported_formats per i client < 1.21.9, min/max_format per quelli >= 1.21.9 (format 65+). */
    private static final String MCMETA = "{\"pack\":{\"description\":\"edn\",\"pack_format\":46,"
            + "\"supported_formats\":{\"min_inclusive\":22,\"max_inclusive\":1000},\"min_format\":22,\"max_format\":1000}}";

    private final EasyDomaniNonTiSvegli plugin;
    private final SignProbe signProbe;
    /** Stesso id per tutta la vita del plugin: un nuovo invio sostituisce il pack, remove lo trova sempre. */
    private final UUID packId = UUID.randomUUID();
    private HttpServer server;
    private volatile boolean active;
    private volatile String url, value;
    private volatile byte[] sha1;
    private volatile Map<String, String> overrides = Map.of();
    /** Pack inviato e non ancora tolto: in questo intervallo i MATCH del sign probe non sanzionano. */
    private final Set<UUID> pending = ConcurrentHashMap.newKeySet();

    public PackProbe(EasyDomaniNonTiSvegli plugin, SignProbe signProbe) {
        this.plugin = plugin;
        this.signProbe = signProbe;
    }

    /** Main thread. Riavvia il server HTTP; ogni errore disattiva solo questo probe. */
    public void reload(ConfigurationSection c) {
        stop();
        if (!c.getBoolean("enabled", false)) return;
        String external = c.getString("externalUrl", "").trim();
        String v = c.getString("value", "").trim();
        if (v.isEmpty()) {
            if (!external.isEmpty()) {
                warn("con externalUrl serve un 'value' fisso (il pack caricato non cambia a ogni avvio)");
                return;
            }
            v = "edn-" + Long.toHexString(ThreadLocalRandom.current().nextLong());
        }
        if (!VALUE.matcher(v).matches()) {
            warn("'value' non valido (ammessi A-Z a-z 0-9 _ . -, 4-60 caratteri)");
            return;
        }
        Map<String, String> ovr = overrides(v, c.getStringList("overrideKeys"));
        byte[] zip, hash;
        try {
            zip = buildZip(v, ovr);
            hash = sha1(zip);
        } catch (IOException | NoSuchAlgorithmException e) {
            warn("generazione del pack fallita: " + e);
            return;
        }
        String hex = HexFormat.of().formatHex(hash);
        String u;
        if (!external.isEmpty()) {
            if (!isHttpUrl(external)) {
                warn("externalUrl non valido (serve http:// o https://)");
                return;
            }
            u = external;
            try {
                Files.write(plugin.getDataFolder().toPath().resolve("probe-pack.zip"), zip);
            } catch (IOException e) {
                plugin.getLogger().warning("Pack probe: probe-pack.zip non scritto: " + e); // l'URL puo' gia' servirlo
            }
            plugin.getLogger().info("Pack probe: externalUrl deve servire probe-pack.zip con sha1=" + hex);
        } else {
            String base = c.getString("publicUrl", "").trim();
            int port = c.getInt("port", 8164);
            if (!isHttpUrl(base) || port < 1 || port > 65535) {
                warn("publicUrl (http/https) e port (1-65535) obbligatori senza externalUrl");
                return;
            }
            String path = "/" + hex + ".zip";
            try {
                server = startServer(c.getString("bind", "0.0.0.0"), port, path, zip);
            } catch (IOException | RuntimeException e) { // porta occupata, bind non risolvibile...
                warn("server HTTP non avviato su porta " + port + ": " + e);
                return;
            }
            u = (base.endsWith("/") ? base.substring(0, base.length() - 1) : base) + path;
        }
        value = v;
        overrides = ovr;
        sha1 = hash;
        url = u;
        active = true;
        if (plugin.debug()) plugin.getLogger().info("[Pack] attivo url=" + u + " sha1=" + hex + " " + KEY + "='" + v + "' override=" + ovr);
    }

    public void stop() {
        active = false;
        if (server != null) server.stop(0);
        server = null;
    }

    /** A coda del sign probe finita. Aggiunto ai pack esistenti, required=false. */
    public void send(Player player) {
        if (!active || !plugin.debug()) return;
        pending.add(player.getUniqueId());
        player.addResourcePack(packId, url, sha1, null, false);
        plugin.getLogger().info("[Pack] " + player.getName() + " inviato " + url);
    }

    /** Idempotente: toglie il pack solo se era stato inviato. */
    public void remove(Player player) {
        if (!pending.remove(player.getUniqueId()) || !player.isOnline()) return;
        player.removeResourcePack(packId);
        if (plugin.debug()) plugin.getLogger().info("[Pack] " + player.getName() + " pack rimosso");
    }

    public boolean isPending(UUID uuid) {
        return pending.contains(uuid);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        pending.remove(event.getPlayer().getUniqueId()); // il client scarta i pack del server alla disconnessione
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onStatus(PlayerResourcePackStatusEvent event) {
        if (!packId.equals(event.getID())) return;
        Player player = event.getPlayer();
        PlayerResourcePackStatusEvent.Status status = event.getStatus();
        if (Bukkit.isPrimaryThread()) handleStatus(player, status);
        else plugin.mainExecutor().execute(() -> handleStatus(player, status));
    }

    /** Main thread. Solo log: nessuno stato porta a sanzioni. */
    private void handleStatus(Player player, PlayerResourcePackStatusEvent.Status status) {
        plugin.getLogger().info("[Pack] " + player.getName() + " status=" + status);
        switch (status) {
            case ACCEPTED, DOWNLOADED -> { } // intermedi
            case SUCCESSFULLY_LOADED -> {
                String v = value;
                Map.Entry<String, String> first = overrides.entrySet().stream().findFirst().orElse(null);
                if (!active || v == null || !player.isOnline()
                        || !signProbe.queuePackControls(player, v, first == null ? null : first.getKey(), first == null ? null : first.getValue())) {
                    plugin.getLogger().info("[Pack] " + player.getName() + " nessuna sessione sign probe, ctrl-pack saltato");
                    remove(player);
                }
            }
            default -> remove(player); // rifiutato/fallito: nessun probe, nessuna sanzione
        }
    }

    private void warn(String why) {
        plugin.getLogger().warning("Pack probe disattivato: " + why);
    }

    // ---- logica pura (testata) ----

    /** Key override valide (ordine mantenuto, doppioni e key non valide saltati) -> value-a, value-b, ... */
    static Map<String, String> overrides(String value, List<String> keys) {
        Map<String, String> out = new LinkedHashMap<>();
        for (String k : keys) {
            String key = k == null ? "" : k.trim();
            if (out.size() >= MAX_OVERRIDES || !LANG_KEY.matcher(key).matches() || key.equals(KEY)) continue;
            if (!out.containsKey(key)) out.put(key, value + "-" + (char) ('a' + out.size()));
        }
        return out;
    }

    /** Zip deterministico a parita' di input (date fisse): stesso SHA-1, serve per externalUrl. */
    static byte[] buildZip(String value, Map<String, String> overrides) throws IOException {
        // key e valori gia' validati ([a-z0-9_.-] / [A-Za-z0-9_.-]): niente escape JSON necessario
        StringBuilder lang = new StringBuilder("{\"").append(KEY).append("\":\"").append(value).append('"');
        overrides.forEach((k, v) -> lang.append(",\"").append(k).append("\":\"").append(v).append('"'));
        lang.append('}');
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ZipOutputStream zip = new ZipOutputStream(out)) {
            entry(zip, "pack.mcmeta", MCMETA);
            // il client carica sempre en_us e poi la lingua scelta: basta en_us per ogni lingua
            entry(zip, "assets/edn/lang/en_us.json", lang.toString());
        }
        return out.toByteArray();
    }

    private static void entry(ZipOutputStream zip, String name, String content) throws IOException {
        ZipEntry e = new ZipEntry(name);
        e.setTime(0L);
        zip.putNextEntry(e);
        zip.write(content.getBytes(StandardCharsets.UTF_8));
        zip.closeEntry();
    }

    static byte[] sha1(byte[] data) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("SHA-1").digest(data);
    }

    static boolean isHttpUrl(String s) {
        try {
            URI uri = new URI(s);
            String scheme = uri.getScheme();
            return uri.getHost() != null && ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme));
        } catch (Exception e) {
            return false;
        }
    }

    /** Serve solo path esatto con GET/HEAD; tutto il resto 404/405. IOException se la porta e' occupata. */
    static HttpServer startServer(String bind, int port, String path, byte[] zip) throws IOException {
        HttpServer s = HttpServer.create(new InetSocketAddress(bind, port), 0);
        s.createContext(path, ex -> serve(ex, path, zip));
        s.start();
        return s;
    }

    private static void serve(HttpExchange ex, String path, byte[] zip) throws IOException {
        try (ex) {
            String method = ex.getRequestMethod();
            if (!path.equals(ex.getRequestURI().getPath())) {
                ex.sendResponseHeaders(404, -1);
                return;
            }
            boolean head = "HEAD".equals(method);
            if (!head && !"GET".equals(method)) {
                ex.sendResponseHeaders(405, -1);
                return;
            }
            ex.getResponseHeaders().set("Content-Type", "application/zip");
            ex.sendResponseHeaders(200, head ? -1 : zip.length);
            if (!head) ex.getResponseBody().write(zip);
        }
    }
}
