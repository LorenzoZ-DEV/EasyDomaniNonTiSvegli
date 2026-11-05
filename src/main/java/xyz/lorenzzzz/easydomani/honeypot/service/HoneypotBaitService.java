package xyz.lorenzzzz.easydomani.honeypot.service;

import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BooleanSupplier;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import xyz.lorenzzzz.easydomani.honeypot.config.HoneypotSettings;
import xyz.lorenzzzz.easydomani.honeypot.model.BlockPositionKey;
import xyz.lorenzzzz.easydomani.honeypot.model.HoneypotBait;
import xyz.lorenzzzz.easydomani.honeypot.model.HoneypotBaitBlock;
import xyz.lorenzzzz.easydomani.message.service.MessageService;

public final class HoneypotBaitService {
    private static final BlockFace[] FACES = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};

    private final Plugin plugin;
    private final Logger logger;
    private final MessageService messages;
    private final BooleanSupplier debug;

    private final Map<UUID, Map<Long, HoneypotBait>> baits = new ConcurrentHashMap<>();
    private final Map<UUID, Long> lastPlace = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> hits = new ConcurrentHashMap<>();
    private boolean started;
    private BukkitTask task;

    private final Location scratch = new Location(null, 0, 0, 0);

    private volatile HoneypotSettings settings = HoneypotSettings.disabled();
    private HoneypotBaitBlock baitStone, baitDeepslate;

    public HoneypotBaitService(Plugin plugin, Logger logger, MessageService messages, BooleanSupplier debug) {
        this.plugin = plugin;
        this.logger = logger;
        this.messages = messages;
        this.debug = debug;
    }

    public void reload(HoneypotSettings next) {
        int oldInterval = settings.intervalTicks();
        baitStone = baitBlock(next.baitStone());
        baitDeepslate = baitBlock(next.baitDeepslate());
        settings = next;
        if (task != null && next.intervalTicks() != oldInterval) {
            task.cancel();
            task = null;
        }
        if (started && task == null) startTimer();
        if (!next.enabled()) clearAll();
    }

    private static HoneypotBaitBlock baitBlock(Material m) {
        return new HoneypotBaitBlock(m, SpigotConversionUtil.fromBukkitBlockData(m.createBlockData()).getGlobalId());
    }

    private void startTimer() {
        int interval = settings.intervalTicks();
        task = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, interval, interval);
    }

    public void start() {
        started = true;
        startTimer();
    }

    public void stop() {
        started = false;
        if (task != null) task.cancel();
        task = null;
        clearAll();
    }

    public boolean hasAnyBait() {
        return !baits.isEmpty();
    }

    public boolean hasBaits(UUID uuid) {
        Map<Long, HoneypotBait> map = baits.get(uuid);
        return map != null && !map.isEmpty();
    }

    public void onBlockChange(UUID uuid, int x, int y, int z, int blockId) {
        Map<Long, HoneypotBait> map = baits.get(uuid);
        if (map == null) return;
        long key = BlockPositionKey.pack(x, y, z);
        HoneypotBait b = map.get(key);

        if (b != null && blockId != b.stateId()) map.remove(key, b);
    }

    public void forgetBait(UUID uuid, int x, int y, int z) {
        Map<Long, HoneypotBait> map = baits.get(uuid);
        if (map != null) map.remove(BlockPositionKey.pack(x, y, z));
    }

    public void forgetChunk(UUID uuid, int cx, int cz) {
        Map<Long, HoneypotBait> map = baits.get(uuid);
        if (map != null) map.values().removeIf(b -> b.x() >> 4 == cx && b.z() >> 4 == cz);
    }

    public void forgetViewerBaits(UUID uuid) {
        Map<Long, HoneypotBait> map = baits.get(uuid);
        if (map != null) map.clear();
    }

    private void tick() {
        HoneypotSettings cfg = settings;
        if (!cfg.enabled() && baits.isEmpty()) return;
        long now = System.currentTimeMillis();
        for (Player p : Bukkit.getOnlinePlayers()) {
            UUID uuid = p.getUniqueId();
            Map<Long, HoneypotBait> map = baits.get(uuid);
            if (map != null && expire(p, uuid, map, now)) map = null;

            if (!cfg.enabled()) continue;
            Long last = lastPlace.get(uuid);
            if (last != null && now - last < cfg.cooldownMs()) continue;
            if (map != null && map.size() >= cfg.maxPerPlayer()) continue;
            if (!isUnderground(p, scratch, cfg) || p.hasPermission("edn.bypass")) continue;
            placeBait(p, uuid, map, now, cfg);
        }
        scratch.setWorld(null);
    }

    private void placeBait(Player p, UUID uuid, Map<Long, HoneypotBait> map, long now, HoneypotSettings cfg) {
        Block host = findTarget(scratch, map, cfg);
        if (host == null) return;
        HoneypotBaitBlock bait = host.getType() == Material.DEEPSLATE ? baitDeepslate : baitStone;
        if (map == null) map = baits.computeIfAbsent(uuid, k -> new ConcurrentHashMap<>());
        map.put(BlockPositionKey.pack(host.getX(), host.getY(), host.getZ()),
                new HoneypotBait(host.getX(), host.getY(), host.getZ(), now + cfg.ttlMs(), bait.material(), bait.stateId()));
        lastPlace.put(uuid, now);
        p.sendBlockChange(host.getLocation(), bait.material().createBlockData());
        if (debug.getAsBoolean()) logger.info("[Honeypot] bait " + bait.material() + " per " + p.getName() + " a " + host.getX() + " " + host.getY() + " " + host.getZ());
    }

    private boolean expire(Player p, UUID uuid, Map<Long, HoneypotBait> map, long now) {
        for (Iterator<HoneypotBait> it = map.values().iterator(); it.hasNext(); ) {
            HoneypotBait b = it.next();
            if (now < b.expiresAt()) continue;
            it.remove();
            restore(p, b);
        }

        return map.isEmpty() && baits.remove(uuid, map);
    }

    private static boolean isUnderground(Player p, Location loc, HoneypotSettings cfg) {
        if (p.getGameMode() == GameMode.SPECTATOR) return false;
        p.getLocation(loc);
        World w = loc.getWorld();
        if (w.getEnvironment() != World.Environment.NORMAL) return false;
        int eyeY = (int) Math.floor(loc.getY() + p.getEyeHeight());
        return eyeY <= cfg.maxY() && w.getBlockAt(loc.getBlockX(), eyeY, loc.getBlockZ()).getLightFromSky() == 0;
    }

    private static Block findTarget(Location loc, Map<Long, HoneypotBait> existing, HoneypotSettings cfg) {
        World w = loc.getWorld();
        ThreadLocalRandom r = ThreadLocalRandom.current();
        int minRadius = cfg.minRadius(), maxRadius = cfg.maxRadius();
        int min2 = minRadius * minRadius, max2 = maxRadius * maxRadius;
        for (int attempt = 0; attempt < 12; attempt++) {
            int dx = r.nextInt(-maxRadius, maxRadius + 1), dz = r.nextInt(-maxRadius, maxRadius + 1);
            int d2 = dx * dx + dz * dz;
            if (d2 < min2 || d2 > max2) continue;
            int x = loc.getBlockX() + dx, y = loc.getBlockY() + r.nextInt(-6, 7), z = loc.getBlockZ() + dz;
            if (y <= w.getMinHeight() || y >= w.getMaxHeight() - 1 || !w.isChunkLoaded(x >> 4, z >> 4)) continue;
            if (existing != null && existing.containsKey(BlockPositionKey.pack(x, y, z))) continue;
            Block b = w.getBlockAt(x, y, z);
            Material t = b.getType();
            if ((t == Material.STONE || t == Material.DEEPSLATE) && enclosed(b)) return b;
        }
        return null;
    }

    private static boolean enclosed(Block b) {
        for (BlockFace f : FACES) {
            if (!b.getRelative(f).getType().isOccluding()) return false;
        }
        return true;
    }

    public void checkHit(Player player, Block block) {
        Map<Long, HoneypotBait> map = baits.get(player.getUniqueId());
        if (map == null) return;
        HoneypotBait b = map.remove(BlockPositionKey.pack(block.getX(), block.getY(), block.getZ()));
        if (b == null) return;
        int count = hits.merge(player.getUniqueId(), 1, Integer::sum);
        player.sendBlockChange(block.getLocation(), block.getBlockData());
        String world = block.getWorld().getName();
        messages.notifyStaff(messages.msg("honeypot-hit", "player", player.getName(), "block", b.material().name(),
                "x", String.valueOf(b.x()), "y", String.valueOf(b.y()), "z", String.valueOf(b.z()), "count", String.valueOf(count)));
        logger.warning("[Honeypot] " + player.getName() + " ha scavato " + b.material() + " a " + b.x() + " " + b.y() + " " + b.z() + " in " + world + " (#" + count + ")");
    }

    public void abandonBaits(UUID uuid) {
        baits.remove(uuid);
    }

    public void forget(UUID uuid) {
        baits.remove(uuid);
        lastPlace.remove(uuid);
        hits.remove(uuid);
    }

    public int hits(UUID uuid) {
        return hits.getOrDefault(uuid, 0);
    }

    private void clearAll() {
        for (Map.Entry<UUID, Map<Long, HoneypotBait>> e : baits.entrySet()) {
            Player p = Bukkit.getPlayer(e.getKey());
            if (p == null) continue;
            for (HoneypotBait b : e.getValue().values()) restore(p, b);
        }
        baits.clear();
    }

    private static void restore(Player p, HoneypotBait b) {
        World w = p.getWorld();
        if (w.isChunkLoaded(b.x() >> 4, b.z() >> 4)) p.sendBlockChange(new Location(w, b.x(), b.y(), b.z()), w.getBlockAt(b.x(), b.y(), b.z()).getBlockData());
    }
}
