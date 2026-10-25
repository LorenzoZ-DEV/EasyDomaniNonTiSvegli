package xyz.lorenzzzz.easydomani.detection.probe;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPositionAndRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPong;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientTabComplete;
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
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;
import xyz.lorenzzzz.easydomani.EasyDomaniNonTiSvegli;

/**
 * Rotation probe, SOLO diagnostica (log, nessuna sanzione).
 * Anubis (ServerRotation.startTick) riporta lo yaw entro 180 gradi dall'ultimo yaw inviato; il vanilla dopo un
 * LookAt ha lo yaw in [-180,180) (Entity.lookAt -> wrapDegrees). Ogni fase chiude con una barriera ping/pong:
 * <ol>
 * <li>PUSH: rotazione ASSOLUTA a big = wrap(yaw) + 3600 + ping. L'eco di handleRotatePlayer vale esattamente big:
 *     riconosciuta per valore, non per posizione (i moduli/il mouse mandano rotazioni proprie in mezzo).</li>
 * <li>LOOK: LookAt verso dove il player guarda + ping.</li>
 * <li>TICK: primo ClientTickEnd dopo il pong (il tick e' iniziato dopo il LookAt), poi rotazione relativa (0,0) + ping.</li>
 * <li>ECHO: tutte le rotazioni fino al pong decidono: tutte vicino a big = UNWRAPPED, tutte nel range = WRAPPED,
 *     altrimenti UNCERTAIN.</li>
 * </ol>
 * A fine probe, se lo yaw e' rimasto fuori range, una rotazione assoluta lo riporta in [-180,180).
 * La sessione resta finche' tutti i ping hanno avuto il pong: niente retry o nuovo probe con pacchetti in volo.
 * Nello stesso listener: log passivo delle richieste tab-complete del modulo AntiVanish di Anubis.
 */
public final class RotationProbe implements Listener {
    enum Stage { PUSH, LOOK, TICK, ECHO, DONE }

    static final float PUSH_DEGREES = 3600f;
    static final String ANTIVANISH_TEXT = "minecraft:msg ";
    static final int ANTIVANISH_MIN_ID = 30000;
    private static final float ECHO_EPSILON = 0.01f;
    private static final long TIMEOUT_TICKS = 60L; // 3 s: verdetto UNCERTAIN
    private static final long DRAIN_TICKS = 200L;  // 10 s: sessione chiusa anche senza tutti i pong
    private static final long RETRY_DELAY_TICKS = 20L;
    private static final int BUSY_WAITS = 30;      // attese da 1 s se sign/pack probe e' attivo
    private static final double LOOK_DISTANCE = 64.0;
    /** Ping id riservati al probe: negativi, lontani dai contatori positivi di altri plugin. */
    private static final int PING_BASE = -0x45440000;

    private final EasyDomaniNonTiSvegli plugin;
    private final Map<UUID, Session> sessions = new ConcurrentHashMap<>();
    /** Ultima rotazione grezza ricevuta da ogni player (yaw, pitch): serve lo yaw prima del push. */
    private final Map<UUID, float[]> lastRotation = new ConcurrentHashMap<>();
    private final AtomicInteger pings = new AtomicInteger();
    private PacketListenerAbstract listener;
    private volatile boolean enabled;
    private volatile long delayTicks;

    /** Stato per player: tutto sotto synchronized(this), da Netty o dal main. */
    private static final class Session {
        final UUID uuid;
        final String name;
        final User user;
        final int attempt;
        final float before, big;
        final Set<Integer> pending = new HashSet<>();
        final List<Float> samples = new ArrayList<>();
        Stage stage = Stage.PUSH;
        float lastYaw = Float.NaN, lastPitch;
        int pushRotations;
        boolean echoSeen, retry;

        Session(UUID uuid, String name, User user, int attempt, float before, float pitch) {
            this.uuid = uuid;
            this.name = name;
            this.user = user;
            this.attempt = attempt;
            this.before = before;
            this.big = wrap(before) + PUSH_DEGREES;
            this.lastPitch = pitch;
        }
    }

    public RotationProbe(EasyDomaniNonTiSvegli plugin) {
        this.plugin = plugin;
    }

    public void reload(ConfigurationSection c) {
        enabled = c.getBoolean("enabled", false);
        delayTicks = Math.max(1, c.getLong("delayTicks", 200));
    }

    /**
     * Funzione pura. true = yaw rimasto vicino a big (Anubis-like), false = wrap vanilla, null = incerto.
     * Con |big| >= 1800 i due intervalli sono disgiunti.
     */
    static Boolean unwrapped(float big, float yaw) {
        if (Math.abs(yaw - big) < 180f) return true;
        if (Math.abs(yaw) < 540f) return false;
        return null;
    }

    /** Funzione pura: verdetto su tutti i campioni; un solo campione incerto o discordante = null. */
    static Boolean verdict(float big, List<Float> samples) {
        Boolean result = null;
        for (float yaw : samples) {
            Boolean u = unwrapped(big, yaw);
            if (u == null || (result != null && !result.equals(u))) return null;
            result = u;
        }
        return result;
    }

    /** Mth.wrapDegrees vanilla: [-180,180). */
    static float wrap(float degrees) {
        float f = degrees % 360f;
        if (f >= 180f) f -= 360f;
        if (f < -180f) f += 360f;
        return f;
    }

    /** Funzione pura: richiesta tab-complete del modulo AntiVanish (id 30000-40000, "minecraft:msg " senza "/"). */
    static boolean isAntiVanishProbe(int id, String text) {
        return ANTIVANISH_TEXT.equals(text) || id >= ANTIVANISH_MIN_ID;
    }

    public void start() {
        listener = new PacketListenerAbstract(PacketListenerPriority.LOWEST) {
            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                handle(event);
            }
        };
        PacketEvents.getAPI().getEventManager().registerListener(listener);
    }

    public void stop() {
        if (listener != null) PacketEvents.getAPI().getEventManager().unregisterListener(listener);
        listener = null;
        sessions.clear();
        lastRotation.clear();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        if (!enabled) return;
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(plugin, () -> startWhenFree(player, BUSY_WAITS), delayTicks);
    }

    /** Main thread. Se sign/pack probe e' attivo riprova ogni secondo, al massimo BUSY_WAITS volte. */
    private void startWhenFree(Player player, int waits) {
        if (!player.isOnline()) return;
        if (otherProbeBusy(player) && waits > 0) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> startWhenFree(player, waits - 1), 20L);
            return;
        }
        String why = begin(player, false, 0);
        if (why != null && plugin.debug()) plugin.getLogger().info("[Rot] " + player.getName() + " non avviato: " + why);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        sessions.remove(uuid);
        lastRotation.remove(uuid);
    }

    /** Main thread, da /edn rotprobe: ignora enabled e edn.bypass. Ritorna null se avviato, altrimenti il motivo. */
    public String force(Player player) {
        return begin(player, true, 0);
    }

    private boolean otherProbeBusy(Player p) {
        return plugin.signProbe().busy(p.getUniqueId()) || plugin.packProbe().isPending(p.getUniqueId());
    }

    /** Main thread. Controlli Bukkit, poi rotazione assoluta a big e ping. */
    private String begin(Player p, boolean forced, int attempt) {
        if (!forced && !enabled) return "disattivato";
        if (!forced && p.hasPermission("edn.bypass")) return "edn.bypass";
        Session old = sessions.get(p.getUniqueId());
        if (old != null) {
            synchronized (old) {
                return old.stage == Stage.DONE ? "probe precedente con " + old.pending.size() + " ping senza pong" : "gia' attivo";
            }
        }
        if (otherProbeBusy(p)) return "sign/pack probe in corso";
        if (SignProbe.isLikelyBedrock(p)) return "Bedrock/Floodgate";
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
            debug(s, String.format(Locale.ROOT, "avvio (tentativo %d/2, prima=%.3f) -> %s", attempt + 1, s.before, Stage.PUSH));
            // pitch relativo 0: cambia solo lo yaw, nella stessa direzione di prima (+10 giri)
            s.user.sendPacket(new WrapperPlayServerPlayerRotation(s.big, false, 0f, true));
            ping(s);
        }
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Stage at;
            synchronized (s) {
                at = s.stage;
            }
            if (at != Stage.DONE) finish(s, null, "timeout in " + at);
        }, TIMEOUT_TICKS);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            synchronized (s) {
                if (sessions.get(s.uuid) != s) return;
                plugin.getLogger().info("[Rot] " + s.name + " sessione chiusa con " + s.pending.size() + " ping senza pong, nessun retry");
                s.retry = false;
                sessions.remove(s.uuid, s);
            }
        }, DRAIN_TICKS);
        return null;
    }

    /** Situazioni in cui lo yaw puo' cambiare per motivi vanilla (veicolo, elytra) o il player non ticka. */
    private static String excluded(Player p) {
        if (p.isDead()) return "morto";
        if (p.getGameMode() == GameMode.SPECTATOR) return "spettatore";
        if (p.isInsideVehicle()) return "in un veicolo";
        if (p.isGliding()) return "elytra";
        if (p.isSleeping()) return "a letto";
        return null;
    }

    /** Netty thread. */
    private void handle(PacketReceiveEvent event) {
        PacketTypeCommon t = event.getPacketType();
        if (t == PacketType.Play.Client.TAB_COMPLETE) {
            logAntiVanish(event);
            return;
        }
        boolean rot = t == PacketType.Play.Client.PLAYER_ROTATION, posRot = t == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION;
        if (!rot && !posRot && t != PacketType.Play.Client.PONG && t != PacketType.Play.Client.CLIENT_TICK_END) return;
        User u = event.getUser();
        UUID uuid = u != null ? u.getUUID() : null;
        if (uuid == null) return;
        if (rot || posRot) {
            float yaw, pitch;
            if (rot) {
                WrapperPlayClientPlayerRotation w = new WrapperPlayClientPlayerRotation(event);
                yaw = w.getYaw();
                pitch = w.getPitch();
            } else {
                WrapperPlayClientPlayerPositionAndRotation w = new WrapperPlayClientPlayerPositionAndRotation(event);
                yaw = w.getYaw();
                pitch = w.getPitch();
            }
            lastRotation.put(uuid, new float[]{yaw, pitch});
            Session s = sessions.get(uuid);
            if (s != null) onRotation(s, yaw, pitch);
            return;
        }
        Session s = sessions.get(uuid);
        if (s == null) return;
        if (t == PacketType.Play.Client.PONG) {
            if (onPong(s, new WrapperPlayClientPong(event).getId())) event.setCancelled(true); // ping nostro: non lo vedono altri plugin
        } else {
            onTickEnd(s);
        }
    }

    private void onRotation(Session s, float yaw, float pitch) {
        synchronized (s) {
            s.lastYaw = yaw;
            s.lastPitch = pitch;
            if (s.stage == Stage.PUSH) {
                s.pushRotations++;
                if (Math.abs(yaw - s.big) < ECHO_EPSILON) s.echoSeen = true;
            } else if (s.stage == Stage.ECHO) {
                s.samples.add(yaw);
            }
        }
    }

    private boolean onPong(Session s, int id) {
        synchronized (s) {
            if (!s.pending.remove(id)) return false;
            switch (s.stage) {
                case PUSH -> {
                    if (!s.echoSeen) {
                        finish(s, null, "eco del push assente");
                    } else {
                        move(s, Stage.LOOK);
                        plugin.mainExecutor().execute(() -> sendLook(s)); // posizione Bukkit solo dal main
                    }
                }
                case LOOK -> move(s, Stage.TICK);
                case ECHO -> {
                    Boolean v = verdict(s.big, s.samples);
                    finish(s, v, v == null ? "campioni misti o fuori range" : v
                            ? "yaw rimasto vicino a big (ServerRotation.startTick)" : "LookAt applicato (wrapDegrees)");
                }
                default -> { }
            }
            if (s.stage == Stage.DONE && s.pending.isEmpty()) drained(s);
            return true;
        }
    }

    /** Main thread: LookAt verso il punto che il player sta gia' guardando (con lo yaw big), poi ping. */
    private void sendLook(Session s) {
        Player p = Bukkit.getPlayer(s.uuid);
        String excluded = p == null || !p.isOnline() ? "offline" : excluded(p);
        if (excluded != null) {
            finish(s, null, excluded);
            return;
        }
        Location eye = p.getEyeLocation();
        synchronized (s) {
            if (s.stage != Stage.LOOK) return;
            eye.setYaw(s.big);
            eye.setPitch(s.lastPitch);
            Vector target = eye.toVector().add(eye.getDirection().multiply(LOOK_DISTANCE));
            s.user.sendPacket(new WrapperPlayServerFacePlayer(WrapperPlayServerFacePlayer.EntitySection.EYES,
                    new Vector3d(target.getX(), target.getY(), target.getZ()), null));
            ping(s);
        }
    }

    private void onTickEnd(Session s) {
        synchronized (s) {
            if (s.stage != Stage.TICK) return;
            // il tick appena finito e' iniziato dopo il LookAt: startTick di Anubis ha gia' agito
            move(s, Stage.ECHO);
            s.user.sendPacket(new WrapperPlayServerPlayerRotation(0f, true, 0f, true)); // eco forzato anche a mouse fermo
            ping(s);
        }
    }

    /** Verdetto e log, poi pulizia dello yaw; il retry parte solo a ping tutti rientrati. Qualsiasi thread. */
    private void finish(Session s, Boolean unwrapped, String reason) {
        synchronized (s) {
            if (s.stage == Stage.DONE) return;
            s.stage = Stage.DONE;
            String verdict = unwrapped == null ? "UNCERTAIN" : unwrapped ? "UNWRAPPED" : "WRAPPED";
            plugin.getLogger().info(String.format(Locale.ROOT,
                    "[Rot] %s big=%.3f yaw=%.3f prima=%.3f rotPush=%d campioni=%s -> %s (%s, tentativo %d/2)",
                    s.name, s.big, s.lastYaw, s.before, s.pushRotations, s.samples, verdict, reason, s.attempt + 1));
            s.retry = unwrapped == null && s.attempt == 0;
            if (!Float.isNaN(s.lastYaw) && Math.abs(s.lastYaw) > 180f) {
                // stato pulito: yaw di nuovo in [-180,180), stessa direzione; l'eco riallinea anche ServerRotation
                float clean = wrap(s.lastYaw);
                debug(s, String.format(Locale.ROOT, "pulizia yaw %.3f -> %.3f", s.lastYaw, clean));
                s.user.sendPacket(new WrapperPlayServerPlayerRotation(clean, false, 0f, true));
                ping(s);
            }
            if (s.pending.isEmpty()) drained(s);
        }
    }

    /** Tutti i ping hanno avuto il pong: sessione chiusa, eventuale retry. */
    private void drained(Session s) {
        if (!sessions.remove(s.uuid, s)) return;
        debug(s, "ping rientrati, sessione chiusa");
        if (!s.retry) return;
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Player p = Bukkit.getPlayer(s.uuid);
            if (p == null || !p.isOnline()) return;
            String why = begin(p, true, 1); // forced: il primo tentativo era gia' passato dai controlli
            if (why != null) plugin.getLogger().info("[Rot] " + s.name + " retry non avviato: " + why);
        }, RETRY_DELAY_TICKS);
    }

    /** Sotto synchronized(s). */
    private void ping(Session s) {
        int id = PING_BASE - (pings.getAndIncrement() & 0xFFFF);
        s.pending.add(id);
        s.user.sendPacket(new WrapperPlayServerPing(id));
    }

    private void move(Session s, Stage to) {
        debug(s, String.format(Locale.ROOT, "%s -> %s (big=%.3f rotPush=%d)", s.stage, to, s.big, s.pushRotations));
        s.stage = to;
    }

    private void debug(Session s, String text) {
        if (plugin.debug()) plugin.getLogger().info("[Rot] " + s.name + " " + text);
    }

    /** Netty thread: solo log, il pacchetto prosegue normalmente. */
    private void logAntiVanish(PacketReceiveEvent event) {
        WrapperPlayClientTabComplete w = new WrapperPlayClientTabComplete(event);
        int id = w.getTransactionId().orElse(-1);
        String text = w.getText();
        if (text == null || !isAntiVanishProbe(id, text)) return;
        String name = event.getUser() != null ? event.getUser().getName() : "?";
        String shown = text.length() > 100 ? text.substring(0, 100) : text;
        plugin.getLogger().info("[AntiVanish] " + name + " id=" + id + " text='" + shown.replaceAll("[\\p{Cntrl}§]", "") + "'");
    }
}
