package xyz.lorenzzzz.easydomani.rotationprobe.service;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerFacePlayer;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPing;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerRotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import xyz.lorenzzzz.easydomani.detection.service.ClientIdentityReader;
import xyz.lorenzzzz.easydomani.rotationprobe.config.RotationProbeSettings;
import xyz.lorenzzzz.easydomani.rotationprobe.model.RotationProbeStage;

public final class RotationProbeService {
    private static final float ECHO_EPSILON = 0.01f;
    private static final long TIMEOUT_TICKS = 60L;
    private static final long DRAIN_TICKS = 200L;
    private static final long RETRY_DELAY_TICKS = 20L;
    private static final int BUSY_WAITS = 30;
    private static final double LOOK_DISTANCE = 64.0;

    private static final int PING_BASE = -0x45440000;

    private final Plugin plugin;
    private final Logger logger;
    private final Executor mainExecutor;
    private final BooleanSupplier debug;
    private final Predicate<UUID> busy;
    private final Map<UUID, Session> sessions = new ConcurrentHashMap<>();

    private final Map<UUID, float[]> lastRotation = new ConcurrentHashMap<>();
    private final AtomicInteger pings = new AtomicInteger();
    private volatile RotationProbeSettings settings = RotationProbeSettings.disabled();

    private static final class Session {
        final UUID uuid;
        final String name;
        final User user;
        final int attempt;
        final float before, big;
        final Set<Integer> pending = new HashSet<>();
        final List<Float> samples = new ArrayList<>();
        RotationProbeStage stage = RotationProbeStage.PUSH;
        float lastYaw = Float.NaN, lastPitch;
        int pushRotations;
        boolean echoSeen, retry;

        Session(UUID uuid, String name, User user, int attempt, float before, float pitch) {
            this.uuid = uuid;
            this.name = name;
            this.user = user;
            this.attempt = attempt;
            this.before = before;
            this.big = YawUnwrapClassifier.wrap(before) + YawUnwrapClassifier.PUSH_DEGREES;
            this.lastPitch = pitch;
        }
    }

    public RotationProbeService(Plugin plugin, Logger logger, Executor mainExecutor, BooleanSupplier debug, Predicate<UUID> busy) {
        this.plugin = plugin;
        this.logger = logger;
        this.mainExecutor = mainExecutor;
        this.debug = debug;
        this.busy = busy;
    }

    public void reload(RotationProbeSettings settings) {
        this.settings = settings;
    }

    public void stop() {
        sessions.clear();
        lastRotation.clear();
    }

    public void scheduleJoinProbe(Player player) {
        RotationProbeSettings cfg = settings;
        if (!cfg.enabled()) return;
        Bukkit.getScheduler().runTaskLater(plugin, () -> startWhenFree(player, BUSY_WAITS), cfg.delayTicks());
    }

    private void startWhenFree(Player player, int waits) {
        if (!player.isOnline()) return;
        if (otherProbeBusy(player) && waits > 0) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> startWhenFree(player, waits - 1), 20L);
            return;
        }
        String why = begin(player, false, 0);
        if (why != null && debug.getAsBoolean()) logger.info("[Rot] " + player.getName() + " non avviato: " + why);
    }

    public void forget(UUID uuid) {
        sessions.remove(uuid);
        lastRotation.remove(uuid);
    }

    public String force(Player player) {
        return begin(player, true, 0);
    }

    private boolean otherProbeBusy(Player p) {
        return busy.test(p.getUniqueId());
    }

    private String begin(Player p, boolean forced, int attempt) {
        if (!forced && !settings.enabled()) return "disattivato";
        if (!forced && p.hasPermission("edn.bypass")) return "edn.bypass";
        Session old = sessions.get(p.getUniqueId());
        if (old != null) {
            synchronized (old) {
                return old.stage == RotationProbeStage.DONE ? "probe precedente con " + old.pending.size() + " ping senza pong" : "gia' attivo";
            }
        }
        if (otherProbeBusy(p)) return "sign/pack probe in corso";
        if (ClientIdentityReader.isLikelyBedrock(p)) return "Bedrock/Floodgate";
        User user = PacketEvents.getAPI().getPlayerManager().getUser(p);
        if (user == null) return "utente PacketEvents assente";
        ClientVersion cv = user.getClientVersion();
        if (cv != ClientVersion.V_1_21_11) return "client " + (cv == null ? "?" : cv.getReleaseName()) + " (serve 1.21.11 nativo)";
        if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_21_11)) return "server < 1.21.11";
        String excluded = excluded(p);
        if (excluded != null) return excluded;

        float[] rot = lastRotation.get(p.getUniqueId());
        Location loc = p.getLocation();
        Session s = new Session(p.getUniqueId(), p.getName(), user, attempt,
                rot != null ? rot[0] : loc.getYaw(), rot != null ? rot[1] : loc.getPitch());
        synchronized (s) {
            sessions.put(s.uuid, s);
            debug(s, String.format(Locale.ROOT, "avvio (tentativo %d/2, prima=%.3f) -> %s", attempt + 1, s.before, RotationProbeStage.PUSH));

            s.user.sendPacket(new WrapperPlayServerPlayerRotation(s.big, false, 0f, true));
            ping(s);
        }
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            RotationProbeStage at;
            synchronized (s) {
                at = s.stage;
            }
            if (at != RotationProbeStage.DONE) finish(s, null, "timeout in " + at);
        }, TIMEOUT_TICKS);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            synchronized (s) {
                if (sessions.get(s.uuid) != s) return;
                logger.info("[Rot] " + s.name + " sessione chiusa con " + s.pending.size() + " ping senza pong, nessun retry");
                s.retry = false;
                sessions.remove(s.uuid, s);
            }
        }, DRAIN_TICKS);
        return null;
    }

    private static String excluded(Player p) {
        if (p.isDead()) return "morto";
        if (p.getGameMode() == GameMode.SPECTATOR) return "spettatore";
        if (p.isInsideVehicle()) return "in un veicolo";
        if (p.isGliding()) return "elytra";
        if (p.isSleeping()) return "a letto";
        return null;
    }

    public boolean hasSession(UUID uuid) {
        return sessions.containsKey(uuid);
    }

    public void onRotation(UUID uuid, float yaw, float pitch) {
        lastRotation.put(uuid, new float[]{yaw, pitch});
        Session s = sessions.get(uuid);
        if (s == null) return;
        synchronized (s) {
            s.lastYaw = yaw;
            s.lastPitch = pitch;
            if (s.stage == RotationProbeStage.PUSH) {
                s.pushRotations++;
                if (Math.abs(yaw - s.big) < ECHO_EPSILON) s.echoSeen = true;
            } else if (s.stage == RotationProbeStage.ECHO) {
                s.samples.add(yaw);
            }
        }
    }

    public boolean onPong(UUID uuid, int id) {
        Session s = sessions.get(uuid);
        if (s == null) return false;
        synchronized (s) {
            if (!s.pending.remove(id)) return false;
            switch (s.stage) {
                case PUSH -> {
                    if (!s.echoSeen) {
                        finish(s, null, "eco del push assente");
                    } else {
                        move(s, RotationProbeStage.LOOK);
                        mainExecutor.execute(() -> sendLook(s));
                    }
                }
                case LOOK -> move(s, RotationProbeStage.TICK);
                case ECHO -> {
                    Boolean v = YawUnwrapClassifier.verdict(s.big, s.samples);
                    finish(s, v, v == null ? "campioni misti o fuori range" : v
                            ? "yaw rimasto vicino a big (ServerRotation.startTick)" : "LookAt applicato (wrapDegrees)");
                }
                default -> { }
            }
            if (s.stage == RotationProbeStage.DONE && s.pending.isEmpty()) drained(s);
            return true;
        }
    }

    private void sendLook(Session s) {
        Player p = Bukkit.getPlayer(s.uuid);
        String excluded = p == null || !p.isOnline() ? "offline" : excluded(p);
        if (excluded != null) {
            finish(s, null, excluded);
            return;
        }
        Location eye = p.getEyeLocation();
        synchronized (s) {
            if (s.stage != RotationProbeStage.LOOK) return;
            eye.setYaw(s.big);
            eye.setPitch(s.lastPitch);
            Vector target = eye.toVector().add(eye.getDirection().multiply(LOOK_DISTANCE));
            s.user.sendPacket(new WrapperPlayServerFacePlayer(WrapperPlayServerFacePlayer.EntitySection.EYES,
                    new Vector3d(target.getX(), target.getY(), target.getZ()), null));
            ping(s);
        }
    }

    public void onTickEnd(UUID uuid) {
        Session s = sessions.get(uuid);
        if (s == null) return;
        synchronized (s) {
            if (s.stage != RotationProbeStage.TICK) return;

            move(s, RotationProbeStage.ECHO);
            s.user.sendPacket(new WrapperPlayServerPlayerRotation(0f, true, 0f, true));
            ping(s);
        }
    }

    private void finish(Session s, Boolean unwrapped, String reason) {
        synchronized (s) {
            if (s.stage == RotationProbeStage.DONE) return;
            s.stage = RotationProbeStage.DONE;
            String verdict = unwrapped == null ? "UNCERTAIN" : unwrapped ? "UNWRAPPED" : "WRAPPED";
            logger.info(String.format(Locale.ROOT,
                    "[Rot] %s big=%.3f yaw=%.3f prima=%.3f rotPush=%d campioni=%s -> %s (%s, tentativo %d/2)",
                    s.name, s.big, s.lastYaw, s.before, s.pushRotations, s.samples, verdict, reason, s.attempt + 1));
            s.retry = unwrapped == null && s.attempt == 0;
            if (!Float.isNaN(s.lastYaw) && Math.abs(s.lastYaw) > 180f) {

                float clean = YawUnwrapClassifier.wrap(s.lastYaw);
                debug(s, String.format(Locale.ROOT, "pulizia yaw %.3f -> %.3f", s.lastYaw, clean));
                s.user.sendPacket(new WrapperPlayServerPlayerRotation(clean, false, 0f, true));
                ping(s);
            }
            if (s.pending.isEmpty()) drained(s);
        }
    }

    private void drained(Session s) {
        if (!sessions.remove(s.uuid, s)) return;
        debug(s, "ping rientrati, sessione chiusa");
        if (!s.retry) return;
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Player p = Bukkit.getPlayer(s.uuid);
            if (p == null || !p.isOnline()) return;
            String why = begin(p, true, 1);
            if (why != null) logger.info("[Rot] " + s.name + " retry non avviato: " + why);
        }, RETRY_DELAY_TICKS);
    }

    private void ping(Session s) {
        int id = PING_BASE - (pings.getAndIncrement() & 0xFFFF);
        s.pending.add(id);
        s.user.sendPacket(new WrapperPlayServerPing(id));
    }

    private void move(Session s, RotationProbeStage to) {
        debug(s, String.format(Locale.ROOT, "%s -> %s (big=%.3f rotPush=%d)", s.stage, to, s.big, s.pushRotations));
        s.stage = to;
    }

    private void debug(Session s, String text) {
        if (debug.getAsBoolean()) logger.info("[Rot] " + s.name + " " + text);
    }
}
