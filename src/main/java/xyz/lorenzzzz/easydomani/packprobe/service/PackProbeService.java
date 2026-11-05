package xyz.lorenzzzz.easydomani.packprobe.service;

import com.sun.net.httpserver.HttpServer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BooleanSupplier;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import xyz.lorenzzzz.easydomani.packprobe.config.PackProbeSettings;
import xyz.lorenzzzz.easydomani.packprobe.integration.ProbePackHttpServer;

public final class PackProbeService {
    private final File dataFolder;
    private final Logger logger;
    private final BooleanSupplier debug;
    private final SignProbeControlQueue controls;

    private final UUID packId = UUID.randomUUID();
    private HttpServer server;
    private volatile boolean active;
    private volatile String url, value;
    private volatile byte[] sha1;
    private volatile Map<String, String> overrides = Map.of();

    private final Set<UUID> pending = ConcurrentHashMap.newKeySet();

    public PackProbeService(File dataFolder, Logger logger, BooleanSupplier debug, SignProbeControlQueue controls) {
        this.dataFolder = dataFolder;
        this.logger = logger;
        this.debug = debug;
        this.controls = controls;
    }

    public void reload(PackProbeSettings settings) {
        stop();
        if (!settings.enabled()) return;
        String v = settings.value();
        Map<String, String> ovr = ProbePackArchiveBuilder.overrides(v, settings.overrideKeys());
        byte[] zip, hash;
        try {
            zip = ProbePackArchiveBuilder.buildZip(v, ovr);
            hash = ProbePackArchiveBuilder.sha1(zip);
        } catch (IOException | NoSuchAlgorithmException e) {
            warn("generazione del pack fallita: " + e);
            return;
        }
        String hex = HexFormat.of().formatHex(hash);
        String u;
        String external = settings.externalUrl();
        if (!external.isEmpty()) {
            u = external;
            try {
                Files.write(dataFolder.toPath().resolve("probe-pack.zip"), zip);
            } catch (IOException e) {
                logger.warning("Pack probe: probe-pack.zip non scritto: " + e);
            }
            logger.info("Pack probe: externalUrl deve servire probe-pack.zip con sha1=" + hex);
        } else {
            String base = settings.publicUrl();
            int port = settings.port();
            String path = "/" + hex + ".zip";
            try {
                server = ProbePackHttpServer.start(settings.bind(), port, path, zip);
            } catch (IOException | RuntimeException e) {
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
        if (debug.getAsBoolean()) logger.info("[Pack] attivo url=" + u + " sha1=" + hex + " " + ProbePackArchiveBuilder.KEY + "='" + v + "' override=" + ovr);
    }

    public void stop() {
        active = false;
        if (server != null) server.stop(0);
        server = null;
    }

    public void send(Player player) {
        if (!active || !debug.getAsBoolean()) return;
        pending.add(player.getUniqueId());
        player.addResourcePack(packId, url, sha1, null, false);
        logger.info("[Pack] " + player.getName() + " inviato " + url);
    }

    public void remove(Player player) {
        if (!pending.remove(player.getUniqueId()) || !player.isOnline()) return;
        player.removeResourcePack(packId);
        if (debug.getAsBoolean()) logger.info("[Pack] " + player.getName() + " pack rimosso");
    }

    public boolean isPending(UUID uuid) {
        return pending.contains(uuid);
    }

    public void forget(UUID uuid) {
        pending.remove(uuid);
    }

    public boolean ownsPack(UUID id) {
        return packId.equals(id);
    }

    public void handleStatus(Player player, PlayerResourcePackStatusEvent.Status status) {
        logger.info("[Pack] " + player.getName() + " status=" + status);
        switch (status) {
            case ACCEPTED, DOWNLOADED -> { }
            case SUCCESSFULLY_LOADED -> {
                String v = value;
                Map.Entry<String, String> first = overrides.isEmpty() ? null : overrides.entrySet().iterator().next();
                if (!active || v == null || !player.isOnline()
                        || !controls.queue(player, v, first == null ? null : first.getKey(), first == null ? null : first.getValue())) {
                    logger.info("[Pack] " + player.getName() + " nessuna sessione sign probe, ctrl-pack saltato");
                    remove(player);
                }
            }
            default -> remove(player);
        }
    }

    private void warn(String why) {
        logger.warning("Pack probe disattivato: " + why);
    }
}
