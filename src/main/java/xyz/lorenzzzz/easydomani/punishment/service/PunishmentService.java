package xyz.lorenzzzz.easydomani.punishment.service;

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
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.h2.jdbcx.JdbcDataSource;
import xyz.lorenzzzz.easydomani.message.service.ChatTextFormatter;
import xyz.lorenzzzz.easydomani.message.service.MessageService;
import xyz.lorenzzzz.easydomani.punishment.config.PunishmentSettings;
import xyz.lorenzzzz.easydomani.punishment.model.PunishmentRecord;
import xyz.lorenzzzz.easydomani.punishment.repository.PunishmentRepository;

public final class PunishmentService {
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault());
    private static final int CLOSE_TIMEOUT_SECONDS = 5;

    private final File dataFolder;
    private final Logger logger;
    private final Executor mainExecutor;
    private final MessageService messages;
    private final Cache<UUID, PunishmentRecord> cache = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(30))
            .maximumSize(10_000)
            .build();
    private ExecutorService io;
    private HikariDataSource dataSource;
    private PunishmentRepository repo;

    private volatile PunishmentSettings settings = PunishmentSettings.disabled();

    public PunishmentService(File dataFolder, Logger logger, Executor mainExecutor, MessageService messages) {
        this.dataFolder = dataFolder;
        this.logger = logger;
        this.mainExecutor = mainExecutor;
        this.messages = messages;
    }

    public void open() throws SQLException {
        io = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "EDN-DB");
            t.setDaemon(true);
            return t;
        });
        JdbcDataSource h2 = new JdbcDataSource();
        h2.setURL("jdbc:h2:file:" + new File(dataFolder, "punishments").getAbsolutePath());
        HikariConfig cfg = new HikariConfig();
        cfg.setDataSource(h2);
        cfg.setPoolName("EDN-H2");
        cfg.setMaximumPoolSize(3);
        cfg.setMinimumIdle(1);
        dataSource = new HikariDataSource(cfg);
        repo = new PunishmentRepository(dataSource);
        repo.createTable();
    }

    public void close() {
        if (io != null) {
            io.shutdown();
            try {
                if (!io.awaitTermination(CLOSE_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                    logger.warning("Scritture DB non completate entro " + CLOSE_TIMEOUT_SECONDS + "s");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (dataSource != null) dataSource.close();
    }

    public void reload(PunishmentSettings settings) {
        this.settings = settings;
    }

    public boolean isEnabled() {
        return settings.enabled();
    }

    public void preload(UUID uuid, String name) {
        try {
            cache.put(uuid, load(uuid, name));
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Lettura sanzioni fallita per " + name, e);
        }
    }

    public CompletableFuture<PunishmentRecord> lookup(UUID uuid) {
        PunishmentRecord cached = cache.getIfPresent(uuid);
        if (cached != null) return CompletableFuture.completedFuture(cached);
        return async(() -> {
            PunishmentRecord e = load(uuid, null);
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

    public CompletableFuture<Void> punish(UUID uuid, String name, String mod) {
        return async(() -> recordOffense(uuid, name, mod))
                .thenAcceptAsync(e -> applyBan(uuid, name, mod, e.count()), mainExecutor)
                .whenComplete((v, err) -> {
                    if (err != null) logger.log(Level.SEVERE, "Ban non applicato a " + name + " (" + mod + ")", err);
                });
    }

    private PunishmentRecord recordOffense(UUID uuid, String name, String mod) throws SQLException {
        long now = System.currentTimeMillis();
        PunishmentRecord e = new PunishmentRecord(uuid, name, PunishmentDurations.effectiveCount(repo.find(uuid), settings.resetAfter(), now) + 1, now, mod);
        repo.upsert(e);
        cache.put(uuid, e);
        return e;
    }

    private void applyBan(UUID uuid, String name, String mod, int count) {
        PunishmentSettings s = settings;
        Duration duration = PunishmentDurations.durationFor(s.ladder(), count);
        String durationText = PunishmentDurations.formatDuration(duration);
        String expires = duration == null ? "mai" : DATE.format(Instant.now().plus(duration));
        String base = uuid.toString();
        String countText = String.valueOf(count);
        String banCommand = s.banCommand();
        if (banCommand != null && !banCommand.isBlank()) {
            String permanent = s.banCommandPermanent();
            String cmdDuration = duration == null ? (permanent.isBlank() ? "perm" : permanent.trim()) : durationText;
            String reason = flatten(s.banCommandReason()).replace("{mod}", mod).replace("{count}", countText).replace("{duration}", durationText);
            dispatch(banCommand.replace("%player%", name).replace("%uuid%", base).replace("%reason%", reason)
                    .replace("%duration%", cmdDuration).replace("%count%", countText).replace("%mod%", mod), "ban");
        } else {
            var reason = messages.msg("ban-screen", "mod", mod, "count", countText,
                    "duration", durationText, "expires", expires);
            Bukkit.getOfflinePlayer(uuid).ban(ChatTextFormatter.legacy(reason), duration, s.source());
            Player online = Bukkit.getPlayer(uuid);
            if (online != null) online.kick(reason);
        }
        for (String raw : s.postBanCommands()) {
            dispatch(raw.replace("%player%", name).replace("%uuid%", base).replace("%duration%", durationText)
                    .replace("%count%", countText).replace("%mod%", mod).replace("%expires%", expires)
                    .replace("\\n", "\n"), "post-ban");
        }
        messages.notifyStaff(messages.msg("ban", "player", name, "duration", durationText, "count", countText, "mod", mod));
        logger.warning("Ban " + name + " (" + uuid + ") per " + durationText + " - sanzione #" + count + " - " + mod);
    }

    private void dispatch(String command, String what) {
        String cmd = command.trim();
        if (cmd.startsWith("/")) cmd = cmd.substring(1).trim();
        if (cmd.isEmpty()) {
            logger.warning("Comando " + what + " vuoto dopo i placeholder: saltato");
            return;
        }
        try {
            if (!Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd)) {
                logger.warning("Comando " + what + " RIFIUTATO (sconosciuto?): /" + cmd);
            }
        } catch (Throwable t) {
            logger.log(Level.WARNING, "Comando " + what + " fallito: /" + cmd, t);
        }
    }

    private PunishmentRecord load(UUID uuid, String name) throws SQLException {
        PunishmentRecord e = repo.find(uuid);
        return e != null ? e : PunishmentRecord.empty(uuid, name);
    }

    @FunctionalInterface
    private interface SqlTask<T> {
        T run() throws SQLException;
    }

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

    public Duration durationFor(int offense) {
        return PunishmentDurations.durationFor(settings.ladder(), offense);
    }

    public int effectiveCount(PunishmentRecord e) {
        return PunishmentDurations.effectiveCount(e, settings.resetAfter(), System.currentTimeMillis());
    }

    private static String flatten(String raw) {
        String oneLine = raw.replace('\r', ' ').replace('\n', ' ');
        String plain = PlainTextComponentSerializer.plainText().serialize(ChatTextFormatter.parse(oneLine));
        return plain.replaceAll("\\s{2,}", " ").trim();
    }
}
