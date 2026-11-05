package xyz.lorenzzzz.easydomani.signprobe.service;

import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.Vector3i;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BooleanSupplier;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import xyz.lorenzzzz.easydomani.detection.model.ModProfile;
import xyz.lorenzzzz.easydomani.detection.service.ClientIdentityReader;
import xyz.lorenzzzz.easydomani.detection.service.ModDetectionService;
import xyz.lorenzzzz.easydomani.packprobe.service.PackProbeService;
import xyz.lorenzzzz.easydomani.signprobe.config.SignProbeSettings;
import xyz.lorenzzzz.easydomani.signprobe.model.ActiveSignProbe;
import xyz.lorenzzzz.easydomani.signprobe.model.SignProbeResult;
import xyz.lorenzzzz.easydomani.signprobe.packet.SignEditorPackets;

public final class SignProbeService {
    private static final long LATE_WRITE_GUARD_MS = 5000L;

    private final Plugin plugin;
    private final Logger logger;
    private final Executor mainExecutor;
    private final BooleanSupplier debug;
    private final ModDetectionService detection;
    private final PackProbeService pack;
    private final SignEditorPackets packets;
    private final Map<UUID, Session> sessions = new ConcurrentHashMap<>();
    private final AtomicLong ids = new AtomicLong();

    private volatile List<ModProfile> table = List.of();
    private volatile SignProbeSettings settings = SignProbeSettings.disabled();

    static final class Session {
        final Deque<ModProfile> queue = new ArrayDeque<>();
        final Set<String> retried = new HashSet<>();
        int opened;
        boolean packSent;
        volatile ActiveSignProbe active;
        volatile ActiveSignProbe late;

        Session(List<ModProfile> table) {
            queue.addAll(table);
        }

        boolean close(ActiveSignProbe a) {
            if (active != a) return false;
            active = null;
            late = a;
            return true;
        }

        void retry(ModProfile def) {
            if (retried.add(def.id())) queue.addLast(def);
        }

        boolean abandonActive() {
            ActiveSignProbe a = active;
            if (a == null || !close(a)) return false;
            for (ModProfile def : a.defs()) retry(def);
            return true;
        }
    }

    public SignProbeService(Plugin plugin, Logger logger, Executor mainExecutor, BooleanSupplier debug,
                            ModDetectionService detection, PackProbeService pack, SignEditorPackets packets) {
        this.plugin = plugin;
        this.logger = logger;
        this.mainExecutor = mainExecutor;
        this.debug = debug;
        this.detection = detection;
        this.pack = pack;
        this.packets = packets;
    }

    public void reload(SignProbeSettings settings, List<ModProfile> profiles) {
        List<ModProfile> withProbe = new ArrayList<>();
        for (ModProfile p : profiles) {
            if (p.hasProbe()) withProbe.add(p);
        }
        table = List.copyOf(withProbe);
        this.settings = settings;
    }

    public void stop() {
        for (Map.Entry<UUID, Session> e : sessions.entrySet()) {
            ActiveSignProbe a = e.getValue().active;
            Player p = Bukkit.getPlayer(e.getKey());
            if (a != null && p != null) packets.restore(p, a);
        }
        sessions.clear();
    }

    public void forget(UUID uuid) {
        sessions.remove(uuid);
    }

    public void abandonActive(Player player) {
        Session s = sessions.get(player.getUniqueId());
        if (s != null && s.abandonActive()) scheduleNext(player, s);
    }

    public boolean awaitsSignUpdate(UUID uuid) {
        Session s = sessions.get(uuid);
        if (s == null) return false;
        ActiveSignProbe late = s.late;
        return s.active != null || (late != null && System.currentTimeMillis() < late.lateUntil());
    }

    public boolean acceptSignUpdate(UUID uuid, Vector3i pos, String[] lines) {
        Session s = sessions.get(uuid);
        if (s == null) return false;
        ActiveSignProbe a = s.active;
        ActiveSignProbe late = s.late;
        if (a != null && a.pos().equals(pos)) {
            mainExecutor.execute(() -> handleResponse(uuid, s, a, lines));
            return true;
        }
        return late != null && late.pos().equals(pos) && System.currentTimeMillis() < late.lateUntil();
    }

    public void startOnJoin(Player player) {
        if (!settings.enabled() || table.isEmpty() || player.hasPermission("edn.bypass") || ClientIdentityReader.isLikelyBedrock(player)) return;
        sessions.put(player.getUniqueId(), new Session(probeList()));
        next(player, 0);
    }

    public boolean queuePackControls(Player player, String expected, String overrideKey, String overrideValue) {
        Session s = sessions.get(player.getUniqueId());
        if (s == null) return false;
        List<ModProfile> controls = SignProbeControlKeys.packControls(expected, overrideKey, overrideValue);
        for (int i = controls.size() - 1; i >= 0; i--) s.queue.addFirst(controls.get(i));
        if (s.active == null) later(settings.intervalTicks(), () -> next(player, 0));
        return true;
    }

    private void drained(Player player, Session s) {
        if (!player.isOnline()) return;
        if (!s.packSent) {
            s.packSent = true;
            pack.send(player);
        } else {
            pack.remove(player);
        }
    }

    public boolean force(Player player) {
        Session old = sessions.get(player.getUniqueId());
        if (table.isEmpty() || (old != null && old.active != null)) return false;
        sessions.put(player.getUniqueId(), new Session(probeList()));
        return next(player, 0);
    }

    private List<ModProfile> probeList() {
        if (!debug.getAsBoolean()) return table;
        List<ModProfile> list = new ArrayList<>(table);
        list.addAll(SignProbeControlKeys.CONTROLS);
        return list;
    }

    public boolean busy(UUID uuid) {
        Session s = sessions.get(uuid);
        return s != null && (s.active != null || !s.queue.isEmpty());
    }

    public String status(UUID uuid) {
        Session s = sessions.get(uuid);
        if (s == null) return "-";
        return s.active != null ? "in corso" : s.queue.isEmpty() ? "completato (" + s.opened + " editor)" : "in coda: " + s.queue.size();
    }

    private boolean next(Player player, int deferrals) {
        UUID uuid = player.getUniqueId();
        Session s = sessions.get(uuid);
        if (s == null || s.active != null || s.queue.isEmpty() || !player.isOnline() || detection.isPunished(uuid)) return false;
        SignProbeSettings cfg = settings;
        if (cfg.maxPerSession() > 0 && s.opened >= cfg.maxPerSession()) {
            s.queue.removeIf(d -> !SignProbeControlKeys.isControl(d));
            if (s.queue.isEmpty()) {
                drained(player, s);
                return false;
            }
        }
        Block block = findLocation(player);
        if (block == null) {
            if (deferrals < cfg.maxDeferrals()) later(20, () -> next(player, deferrals + 1));
            return false;
        }
        User user = packets.user(player);
        if (user == null) return false;

        List<ModProfile> group = new ArrayList<>(cfg.keysPerSign());
        while (group.size() < cfg.keysPerSign() && !s.queue.isEmpty()) group.add(s.queue.poll());
        ActiveSignProbe a = new ActiveSignProbe(ids.incrementAndGet(), new Vector3i(block.getX(), block.getY(), block.getZ()),
                packets.stateId(block),
                "edn-" + Long.toHexString(ThreadLocalRandom.current().nextLong()), List.copyOf(group),
                System.currentTimeMillis() + cfg.timeoutTicks() * 50L + LATE_WRITE_GUARD_MS);
        s.active = a;
        if (!SignProbeControlKeys.allControls(group)) s.opened++;
        try {
            packets.open(user, a);
        } catch (Throwable t) {
            s.active = null;
            packets.restore(player, a);
            logger.warning("Sign probe non inviato a " + player.getName() + ": " + t);
            return false;
        }
        if (debug.getAsBoolean()) logger.info("[Probe] " + player.getName() + " #" + a.id() + " key=" + SignProbeControlKeys.describeKeys(group));

        if (cfg.autoCloseTicks() > 0) later(cfg.autoCloseTicks(), () -> autoClose(player, user, s, a));
        later(cfg.timeoutTicks(), () -> timeout(player, s, a));
        return true;
    }

    private void autoClose(Player player, User user, Session s, ActiveSignProbe a) {
        if (s.active != a || !player.isOnline()) return;
        packets.close(user);
        packets.restore(player, a);
    }

    private void timeout(Player player, Session s, ActiveSignProbe a) {
        if (!s.close(a)) return;
        if (debug.getAsBoolean()) logger.info("[Probe] " + player.getName() + " #" + a.id() + " TIMEOUT");
        if (!player.isOnline()) return;
        packets.restore(player, a);
        for (ModProfile def : a.defs()) s.retry(def);
        scheduleNext(player, s);
    }

    private void handleResponse(UUID uuid, Session s, ActiveSignProbe a, String[] lines) {
        if (!s.close(a)) return;
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        packets.restore(player, a);
        boolean intact = SignProbeResponseClassifier.isIntact(lines, a.sentinel());
        boolean stop = false;
        for (int i = 0; i < a.defs().size(); i++) {
            ModProfile def = a.defs().get(i);
            String line = lines != null && i < lines.length ? lines[i] : null;
            if (SignProbeControlKeys.isControl(def)) {
                String exp = def.probeExpected().isEmpty() ? "" : " expected='" + def.probeExpected().get(0) + "'";
                if (debug.getAsBoolean()) logger.info("[Probe] " + player.getName() + " #" + a.id() + " " + def.id() + " -> '" + line + "'" + exp + " = DIAG intact=" + intact);
                continue;
            }
            SignProbeResult r = SignProbeResponseClassifier.classify(def, a.sentinel(), line, intact);
            if (debug.getAsBoolean()) logger.info("[Probe] " + player.getName() + " #" + a.id() + " " + def.id() + " -> '" + line + "' = " + r);
            switch (r) {
                case MATCH -> {
                    if (pack.isPending(uuid)) {
                        logger.info("[Probe] " + player.getName() + " #" + a.id() + " " + def.id() + " MATCH ignorato: pack probe attivo");
                    } else {
                        stop |= detection.report(player, def, "sign probe");
                    }
                }
                case UNKNOWN, TAMPERED -> s.retry(def);
                case NO_MATCH -> { }
            }
        }
        if (stop) s.queue.clear();
        else scheduleNext(player, s);
    }

    private void scheduleNext(Player player, Session s) {
        if (!s.queue.isEmpty()) later(settings.intervalTicks(), () -> next(player, 0));
        else drained(player, s);
    }

    private void later(long ticks, Runnable r) {
        Bukkit.getScheduler().runTaskLater(plugin, r, ticks);
    }

    private static Block findLocation(Player player) {
        Location loc = player.getLocation();
        World w = loc.getWorld();
        int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();
        int[][] candidates = {{x, y + 2, z}, {x, y - 1, z}, {x + 1, y, z}, {x - 1, y, z}, {x, y, z + 1}, {x, y, z - 1}};
        for (int[] c : candidates) {
            if (c[1] < w.getMinHeight() || c[1] >= w.getMaxHeight()) continue;
            Block b = w.getBlockAt(c[0], c[1], c[2]);
            if (!b.getType().isSolid()) return b;
        }
        return null;
    }
}
