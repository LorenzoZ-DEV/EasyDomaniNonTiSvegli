package xyz.lorenzzzz.easydomani.punishment;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.h2.jdbcx.JdbcDataSource;
import xyz.lorenzzzz.easydomani.EasyDomaniNonTiSvegli;
import xyz.lorenzzzz.easydomani.util.TextUtil;

/**
 * Ban a scala con contatore persistente in H2 (pool HikariCP).
 * I/O del DB solo sul thread "EDN-DB"; ban, kick e comandi tornano sul main thread.
 * Cache Caffeine dei contatori: caricata al pre-login (gia' off-main), mai letture DB sul main.
 */
public final class PunishmentManager implements Listener {
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault());
    private static final Duration PERM = Duration.ofSeconds(-1);
    private static final List<Duration> DEFAULT_LADDER = List.of(Duration.ofMinutes(10), Duration.ofHours(12), Duration.ofDays(3));
    private static final long MINUTE = 60, HOUR = 60 * MINUTE, DAY = 24 * HOUR, WEEK = 7 * DAY; // secondi
    private static final int CLOSE_TIMEOUT_SECONDS = 5;

    /** Contatore sanzioni di un giocatore; count 0 = nessuna sanzione registrata. */
    public record Entry(UUID uuid, String name, int count, long lastMs, String lastMod) {
        static Entry empty(UUID uuid, String name) {
            return new Entry(uuid, name, 0, 0L, "-");
        }
    }

    private final EasyDomaniNonTiSvegli plugin;
    private final Cache<UUID, Entry> cache = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(30)) // dopo il quit nessuno la tocca piu': scade da sola
            .maximumSize(10_000)
            .build();
    private ExecutorService io;
    private HikariDataSource dataSource;
    private PunishmentRepository repo;

    // letti anche dal thread del DB
    private volatile boolean enabled;
    private volatile List<Duration> ladder = DEFAULT_LADDER;
    private volatile Duration resetAfter = Duration.ZERO;
    // letti solo dal main thread
    private String banCommand, banCommandPermanent, banCommandReason, source;
    private List<String> postBanCommands = List.of();

    public PunishmentManager(EasyDomaniNonTiSvegli plugin) {
        this.plugin = plugin;
    }

    /** Apre H2 (file nel data folder) e crea la tabella. Bloccante: solo in onEnable. */
    public void open() throws SQLException {
        io = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "EDN-DB");
            t.setDaemon(true);
            return t;
        });
        JdbcDataSource h2 = new JdbcDataSource(); // niente DriverManager: funziona col library loader di Paper
        h2.setURL("jdbc:h2:file:" + new File(plugin.getDataFolder(), "punishments").getAbsolutePath());
        HikariConfig cfg = new HikariConfig();
        cfg.setDataSource(h2);
        cfg.setPoolName("EDN-H2");
        cfg.setMaximumPoolSize(3); // 1 scrittore (EDN-DB) + letture dei pre-login
        cfg.setMinimumIdle(1);
        dataSource = new HikariDataSource(cfg);
        repo = new PunishmentRepository(dataSource);
        repo.createTable();
    }

    /** Attende le scritture in coda (max CLOSE_TIMEOUT_SECONDS) e chiude il pool. */
    public void close() {
        if (io != null) {
            io.shutdown();
            try {
                if (!io.awaitTermination(CLOSE_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                    plugin.getLogger().warning("Scritture DB non completate entro " + CLOSE_TIMEOUT_SECONDS + "s");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (dataSource != null) dataSource.close();
    }

    public void reload(ConfigurationSection c) {
        enabled = c.getBoolean("enabled", true);
        List<Duration> l = new ArrayList<>();
        for (String step : c.getStringList("ladder")) {
            Duration d = parseDuration(step);
            if (d != null && !d.isZero()) l.add(d);
            else plugin.getLogger().warning("Durata ladder non valida: '" + step + "' (es. 30s 10m 12h 3d 2w perm)");
        }
        ladder = l.isEmpty() ? DEFAULT_LADDER : List.copyOf(l);
        Duration r = parseDuration(c.getString("resetAfter", "0"));
        resetAfter = r == null || r.isNegative() ? Duration.ZERO : r;
        banCommand = c.getString("banCommand", "");
        banCommandPermanent = c.getString("banCommandPermanent", "perm");
        banCommandReason = c.getString("banCommandReason", "Cheat client non consentito: {mod}");
        source = c.getString("source", "EasyDomaniNonTiSvegli");
        postBanCommands = c.getStringList("postBanCommands").stream().filter(s -> !s.isBlank()).toList();
    }

    public boolean isEnabled() {
        return enabled;
    }

    /** Thread di login (gia' asincrono): qui la lettura sincrona dal DB va bene. */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;
        try {
            cache.put(event.getUniqueId(), load(event.getUniqueId(), event.getName()));
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Lettura sanzioni fallita per " + event.getName(), e);
        }
    }

    /** Contatore dalla cache o, se manca, dal DB sul thread EDN-DB. */
    public CompletableFuture<Entry> lookup(UUID uuid) {
        Entry cached = cache.getIfPresent(uuid);
        if (cached != null) return CompletableFuture.completedFuture(cached);
        return async(() -> {
            Entry e = load(uuid, null);
            cache.put(uuid, e);
            return e;
        });
    }

    public CompletableFuture<Void> reset(UUID uuid) {
        return async(() -> {
            repo.delete(uuid);
            cache.invalidate(uuid);
            return null;
        });
    }

    /**
     * Main thread (da ModDetection). Passi:
     * EDN-DB: legge e incrementa il contatore -> main: ban/kick/comandi/alert. Errori sempre loggati.
     */
    public void punish(UUID uuid, String name, String mod) {
        async(() -> recordOffense(uuid, name, mod))
                .thenAcceptAsync(e -> applyBan(uuid, name, mod, e.count()), plugin.mainExecutor())
                .whenComplete((v, err) -> {
                    if (err != null) plugin.getLogger().log(Level.SEVERE, "Ban non applicato a " + name + " (" + mod + ")", err);
                });
    }

    /** EDN-DB: unica scrittura del contatore, quindi niente race tra due sanzioni. */
    private Entry recordOffense(UUID uuid, String name, String mod) throws SQLException {
        long now = System.currentTimeMillis();
        Entry e = new Entry(uuid, name, effectiveCount(repo.find(uuid), resetAfter, now) + 1, now, mod);
        repo.upsert(e);
        cache.put(uuid, e);
        return e;
    }

    /** Main thread: ban (comando esterno o ban-list Bukkit), post-comandi e alert. */
    private void applyBan(UUID uuid, String name, String mod, int count) {
        Duration duration = durationFor(count);
        String durationText = formatDuration(duration);
        String expires = duration == null ? "mai" : DATE.format(Instant.now().plus(duration));
        String base = uuid.toString();
        String countText = String.valueOf(count);
        if (banCommand != null && !banCommand.isBlank()) {
            String cmdDuration = duration == null ? (banCommandPermanent.isBlank() ? "perm" : banCommandPermanent.trim()) : durationText;
            String reason = flatten(banCommandReason).replace("{mod}", mod).replace("{count}", countText).replace("{duration}", durationText);
            dispatch(banCommand.replace("%player%", name).replace("%uuid%", base).replace("%reason%", reason)
                    .replace("%duration%", cmdDuration).replace("%count%", countText).replace("%mod%", mod), "ban");
        } else {
            var reason = plugin.msg("ban-screen", "mod", mod, "count", countText,
                    "duration", durationText, "expires", expires);
            Bukkit.getOfflinePlayer(uuid).ban(TextUtil.legacy(reason), duration, source);
            Player online = Bukkit.getPlayer(uuid);
            if (online != null) online.kick(reason);
        }
        for (String raw : postBanCommands) {
            dispatch(raw.replace("%player%", name).replace("%uuid%", base).replace("%duration%", durationText)
                    .replace("%count%", countText).replace("%mod%", mod).replace("%expires%", expires)
                    .replace("\\n", "\n"), "post-ban");
        }
        plugin.notifyStaff(plugin.msg("ban", "player", name, "duration", durationText, "count", countText, "mod", mod));
        plugin.getLogger().warning("Ban " + name + " (" + uuid + ") per " + durationText + " - sanzione #" + count + " - " + mod);
    }

    private void dispatch(String command, String what) {
        String cmd = command.trim();
        if (cmd.startsWith("/")) cmd = cmd.substring(1).trim();
        if (cmd.isEmpty()) {
            plugin.getLogger().warning("Comando " + what + " vuoto dopo i placeholder: saltato");
            return;
        }
        try {
            if (!Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd)) {
                plugin.getLogger().warning("Comando " + what + " RIFIUTATO (sconosciuto?): /" + cmd);
            }
        } catch (Throwable t) {
            plugin.getLogger().log(Level.WARNING, "Comando " + what + " fallito: /" + cmd, t);
        }
    }

    private Entry load(UUID uuid, String name) throws SQLException {
        Entry e = repo.find(uuid);
        return e != null ? e : Entry.empty(uuid, name);
    }

    @FunctionalInterface
    private interface SqlTask<T> {
        T run() throws SQLException;
    }

    /** Esegue sul thread EDN-DB; un rifiuto (plugin in spegnimento) diventa un future fallito, non un'eccezione. */
    private <T> CompletableFuture<T> async(SqlTask<T> task) {
        try {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    return task.run();
                } catch (SQLException e) {
                    throw new CompletionException(e);
                }
            }, io);
        } catch (RejectedExecutionException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    /** Durata della sanzione n-esima con la ladder attuale (null = permanente). */
    public Duration durationFor(int offense) {
        return durationFor(ladder, offense);
    }

    /** Sanzioni ancora valide (tiene conto di resetAfter). */
    public int effectiveCount(Entry e) {
        return effectiveCount(e, resetAfter, System.currentTimeMillis());
    }

    // ---- logica pura (testata) ----

    static int effectiveCount(Entry e, Duration resetAfter, long now) {
        if (e == null || e.count() <= 0) return 0;
        if (!resetAfter.isZero() && e.lastMs() > 0 && now - e.lastMs() > resetAfter.toMillis()) return 0;
        return e.count();
    }

    /** Oltre l'ultimo scalino si riusa l'ultimo; null = permanente. */
    static Duration durationFor(List<Duration> ladder, int offense) {
        Duration d = ladder.get(Math.min(Math.max(offense, 1), ladder.size()) - 1);
        return d.isNegative() ? null : d;
    }

    private static String flatten(String raw) {
        String oneLine = raw.replace('\r', ' ').replace('\n', ' ');
        String plain = PlainTextComponentSerializer.plainText().serialize(TextUtil.parse(oneLine));
        return plain.replaceAll("\\s{2,}", " ").trim();
    }

    /** null = permanente. */
    public static String formatDuration(Duration d) {
        if (d == null) return "permanente";
        long s = d.getSeconds();
        if (s >= WEEK && s % WEEK == 0) return s / WEEK + "w";
        if (s >= DAY && s % DAY == 0) return s / DAY + "d";
        if (s >= HOUR && s % HOUR == 0) return s / HOUR + "h";
        if (s >= MINUTE && s % MINUTE == 0) return s / MINUTE + "m";
        return s + "s";
    }

    /** "10m", "12h", "3d", "2w", "30s", "0"; "perm"/"permanent"/"-1" = durata negativa (permanente). null se non valida. */
    static Duration parseDuration(String raw) {
        if (raw == null) return null;
        String t = raw.trim().toLowerCase(Locale.ROOT);
        if (t.isEmpty()) return null;
        if (t.equals("perm") || t.equals("permanent") || t.equals("-1")) return PERM;
        if (t.equals("0")) return Duration.ZERO;
        try {
            long n = Long.parseLong(t.substring(0, t.length() - 1).trim());
            if (n <= 0 || n > 100_000) return null; // limite sano: evita overflow su date/durate
            return switch (t.charAt(t.length() - 1)) {
                case 's' -> Duration.ofSeconds(n);
                case 'm' -> Duration.ofMinutes(n);
                case 'h' -> Duration.ofHours(n);
                case 'd' -> Duration.ofDays(n);
                case 'w' -> Duration.ofDays(n * 7);
                default -> null;
            };
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
