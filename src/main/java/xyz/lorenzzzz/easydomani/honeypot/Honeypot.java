package xyz.lorenzzzz.easydomani.honeypot;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerMultiBlockChange;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUnloadChunk;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;
import xyz.lorenzzzz.easydomani.EasyDomaniNonTiSvegli;

/**
 * Honeypot anti x-ray: minerale finto inviato SOLO al player (mai nel mondo reale) dentro
 * stone/deepslate murato. Scavarlo = alert allo staff. Se il server riscrive quel blocco
 * (block update, chunk reinviato/scaricato, respawn) il bait sparisce e il registro viene pulito.
 */
public final class Honeypot implements Listener {
    private static final BlockFace[] FACES = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};

    private record Bait(int x, int y, int z, long expiresAt, Material material, int stateId) {}

    /** Blocco del bait da config con il suo id di stato (per riconoscere il nostro BLOCK_CHANGE). */
    private record BaitBlock(Material material, int stateId) {}

    private final EasyDomaniNonTiSvegli plugin;
    /** uuid -> (posizione impacchettata -> bait). Le mappe interne sono toccate anche da Netty. */
    private final Map<UUID, Map<Long, Bait>> baits = new ConcurrentHashMap<>();
    private final Map<UUID, Long> lastPlace = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> hits = new ConcurrentHashMap<>();
    private PacketListenerAbstract listener;
    private BukkitTask task;
    /** Location riusata dal timer (solo main thread). */
    private final Location scratch = new Location(null, 0, 0, 0);

    private volatile boolean enabled;
    private int maxY, maxPerPlayer, minRadius, maxRadius, intervalTicks;
    private long ttlMs, cooldownMs;
    private BaitBlock baitStone, baitDeepslate;

    public Honeypot(EasyDomaniNonTiSvegli plugin) {
        this.plugin = plugin;
    }

    public void reload(ConfigurationSection c) {
        enabled = c.getBoolean("enabled", true);
        maxY = c.getInt("maxY", 40);
        baitStone = baitBlock(c.getString("baitBlock"), Material.DIAMOND_ORE);
        baitDeepslate = baitBlock(c.getString("baitBlockDeepslate"), Material.DEEPSLATE_DIAMOND_ORE);
        maxPerPlayer = Math.max(1, c.getInt("maxPerPlayer", 2));
        minRadius = Math.min(32, Math.max(2, c.getInt("minRadius", 4)));
        maxRadius = Math.min(32, Math.max(minRadius, c.getInt("maxRadius", 10)));
        ttlMs = Math.max(1, c.getLong("ttlSeconds", 300)) * 1000L;
        cooldownMs = Math.max(1, c.getLong("placeCooldownSeconds", 10)) * 1000L;
        int newInterval = Math.max(5, c.getInt("checkIntervalTicks", 20));
        if (task != null && newInterval != intervalTicks) {
            task.cancel();
            task = null;
        }
        intervalTicks = newInterval;
        if (listener != null && task == null) startTimer();
        if (!enabled) clearAll();
    }

    private BaitBlock baitBlock(String name, Material def) {
        Material m = name == null ? null : Material.matchMaterial(name);
        if (m == null || !m.isBlock()) {
            if (name != null) plugin.getLogger().warning("honeypot: blocco '" + name + "' non valido, uso " + def);
            m = def;
        }
        return new BaitBlock(m, SpigotConversionUtil.fromBukkitBlockData(m.createBlockData()).getGlobalId());
    }

    /** Task necessario: tick periodico sul main, legge mondo e giocatori con le API Bukkit. */
    private void startTimer() {
        task = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, intervalTicks, intervalTicks);
    }

    public void start() {
        listener = new PacketListenerAbstract(PacketListenerPriority.MONITOR) {
            @Override
            public void onPacketSend(PacketSendEvent event) {
                // Uscita immediata: nessun bait attivo sul server (caso normale) o pacchetto irrilevante.
                if (baits.isEmpty() || !(event.getPacketType() instanceof PacketType.Play.Server type)) return;
                switch (type) {
                    case BLOCK_CHANGE, MULTI_BLOCK_CHANGE, CHUNK_DATA, UNLOAD_CHUNK, RESPAWN -> { }
                    default -> { return; }
                }
                UUID uuid = event.getUser() != null ? event.getUser().getUUID() : null;
                Map<Long, Bait> map = uuid != null ? baits.get(uuid) : null;
                if (map != null && !map.isEmpty()) onServerUpdate(event, type, map);
            }
        };
        PacketEvents.getAPI().getEventManager().registerListener(listener);
        startTimer();
    }

    public void stop() {
        if (listener != null) PacketEvents.getAPI().getEventManager().unregisterListener(listener);
        listener = null;
        if (task != null) task.cancel();
        task = null;
        clearAll();
    }

    /** Netty thread: il server ha riscritto (o sta per riscrivere) il blocco del bait. */
    private static void onServerUpdate(PacketSendEvent event, PacketType.Play.Server type, Map<Long, Bait> map) {
        switch (type) {
            case BLOCK_CHANGE -> {
                WrapperPlayServerBlockChange w = new WrapperPlayServerBlockChange(event);
                Vector3i p = w.getBlockPosition();
                long key = pack(p.x, p.y, p.z);
                Bait b = map.get(key);
                // il nostro stesso invio del bait passa di qui: lo riconosco dall'id del blocco
                if (b != null && w.getBlockId() != b.stateId) map.remove(key, b);
            }
            case MULTI_BLOCK_CHANGE -> {
                for (WrapperPlayServerMultiBlockChange.EncodedBlock e : new WrapperPlayServerMultiBlockChange(event).getBlocks()) {
                    map.remove(pack(e.getX(), e.getY(), e.getZ()));
                }
            }
            case CHUNK_DATA -> {
                // Basta X/Z del chunk (primi due int): niente parsing dell'intera colonna.
                Object buf = event.getByteBuf();
                int start = ByteBufHelper.readerIndex(buf);
                int cx = ByteBufHelper.readInt(buf);
                int cz = ByteBufHelper.readInt(buf);
                ByteBufHelper.readerIndex(buf, start);
                removeChunk(map, cx, cz);
            }
            case UNLOAD_CHUNK -> {
                WrapperPlayServerUnloadChunk w = new WrapperPlayServerUnloadChunk(event);
                removeChunk(map, w.getChunkX(), w.getChunkZ());
            }
            default -> map.clear(); // RESPAWN: il client ricarica tutto
        }
    }

    private static void removeChunk(Map<Long, Bait> map, int cx, int cz) {
        map.values().removeIf(b -> b.x >> 4 == cx && b.z >> 4 == cz);
    }

    /** Main thread, ogni checkIntervalTicks: scadenze + eventuale nuovo bait. */
    private void tick() {
        if (!enabled && baits.isEmpty()) return;
        long now = System.currentTimeMillis();
        for (Player p : Bukkit.getOnlinePlayers()) {
            UUID uuid = p.getUniqueId();
            Map<Long, Bait> map = baits.get(uuid);
            if (map != null && expire(p, uuid, map, now)) map = null;
            // controlli dal piu' economico al piu' costoso (lettura blocchi e permessi per ultimi)
            if (!enabled) continue;
            Long last = lastPlace.get(uuid);
            if (last != null && now - last < cooldownMs) continue;
            if (map != null && map.size() >= maxPerPlayer) continue;
            if (!isUnderground(p, scratch) || p.hasPermission("edn.bypass")) continue;
            placeBait(p, uuid, map, now);
        }
        scratch.setWorld(null); // non trattenere il World tra un giro e l'altro
    }

    /** Cerca un blocco attorno a scratch e ci mette il bait (solo pacchetto: il mondo non cambia). */
    private void placeBait(Player p, UUID uuid, Map<Long, Bait> map, long now) {
        Block host = findTarget(scratch, map);
        if (host == null) return;
        BaitBlock bait = host.getType() == Material.DEEPSLATE ? baitDeepslate : baitStone;
        if (map == null) map = baits.computeIfAbsent(uuid, k -> new ConcurrentHashMap<>());
        map.put(pack(host.getX(), host.getY(), host.getZ()), new Bait(host.getX(), host.getY(), host.getZ(), now + ttlMs, bait.material(), bait.stateId()));
        lastPlace.put(uuid, now);
        p.sendBlockChange(host.getLocation(), bait.material().createBlockData());
        if (plugin.debug()) plugin.getLogger().info("[Honeypot] bait " + bait.material() + " per " + p.getName() + " a " + host.getX() + " " + host.getY() + " " + host.getZ());
    }

    /** Rimanda il blocco vero per i bait scaduti. true = registro del player vuoto e rimosso. */
    private boolean expire(Player p, UUID uuid, Map<Long, Bait> map, long now) {
        for (Iterator<Bait> it = map.values().iterator(); it.hasNext(); ) {
            Bait b = it.next();
            if (now < b.expiresAt) continue;
            it.remove();
            restore(p, b);
        }
        // mappa vuota fuori dal registro: il listener dei pacchetti puo' uscire con baits.isEmpty()
        return map.isEmpty() && baits.remove(uuid, map);
    }

    /** Riempie loc con la posizione del player (nessuna allocazione). */
    private boolean isUnderground(Player p, Location loc) {
        if (p.getGameMode() == GameMode.SPECTATOR) return false;
        p.getLocation(loc);
        World w = loc.getWorld();
        if (w.getEnvironment() != World.Environment.NORMAL) return false;
        int eyeY = (int) Math.floor(loc.getY() + p.getEyeHeight());
        return eyeY <= maxY && w.getBlockAt(loc.getBlockX(), eyeY, loc.getBlockZ()).getLightFromSky() == 0;
    }

    /** Stone/deepslate murato su 6 lati, a distanza orizzontale tra minRadius e maxRadius. */
    private Block findTarget(Location loc, Map<Long, Bait> existing) {
        World w = loc.getWorld();
        ThreadLocalRandom r = ThreadLocalRandom.current();
        int min2 = minRadius * minRadius, max2 = maxRadius * maxRadius;
        for (int attempt = 0; attempt < 12; attempt++) {
            int dx = r.nextInt(-maxRadius, maxRadius + 1), dz = r.nextInt(-maxRadius, maxRadius + 1);
            int d2 = dx * dx + dz * dz;
            if (d2 < min2 || d2 > max2) continue;
            int x = loc.getBlockX() + dx, y = loc.getBlockY() + r.nextInt(-6, 7), z = loc.getBlockZ() + dz;
            if (y <= w.getMinHeight() || y >= w.getMaxHeight() - 1 || !w.isChunkLoaded(x >> 4, z >> 4)) continue;
            if (existing != null && existing.containsKey(pack(x, y, z))) continue;
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(BlockDamageEvent event) {
        checkHit(event.getPlayer(), event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent event) {
        checkHit(event.getPlayer(), event.getBlock());
    }

    private void checkHit(Player player, Block block) {
        Map<Long, Bait> map = baits.get(player.getUniqueId());
        if (map == null) return;
        Bait b = map.remove(pack(block.getX(), block.getY(), block.getZ()));
        if (b == null) return;
        int count = hits.merge(player.getUniqueId(), 1, Integer::sum);
        player.sendBlockChange(block.getLocation(), block.getBlockData()); // torna il blocco vero
        String world = block.getWorld().getName();
        plugin.notifyStaff(plugin.msg("honeypot-hit", "player", player.getName(), "block", b.material.name(),
                "x", String.valueOf(b.x), "y", String.valueOf(b.y), "z", String.valueOf(b.z), "count", String.valueOf(count)));
        plugin.getLogger().warning("[Honeypot] " + player.getName() + " ha scavato " + b.material + " a " + b.x + " " + b.y + " " + b.z + " in " + world + " (#" + count + ")");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        baits.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        baits.remove(uuid);
        lastPlace.remove(uuid);
        hits.remove(uuid);
    }

    public int hits(UUID uuid) {
        return hits.getOrDefault(uuid, 0);
    }

    /** Rimanda i blocchi veri a tutti e svuota il registro (disable o honeypot spento). */
    private void clearAll() {
        for (Map.Entry<UUID, Map<Long, Bait>> e : baits.entrySet()) {
            Player p = Bukkit.getPlayer(e.getKey());
            if (p == null) continue;
            for (Bait b : e.getValue().values()) restore(p, b);
        }
        baits.clear();
    }

    /** Rimanda al player il blocco vero al posto del bait (se il chunk e' caricato). */
    private static void restore(Player p, Bait b) {
        World w = p.getWorld();
        if (w.isChunkLoaded(b.x >> 4, b.z >> 4)) p.sendBlockChange(new Location(w, b.x, b.y, b.z), w.getBlockAt(b.x, b.y, b.z).getBlockData());
    }

    /** Stesso formato di BlockPos.asLong. */
    static long pack(int x, int y, int z) {
        return ((long) (x & 0x3FFFFFF) << 38) | ((long) (z & 0x3FFFFFF) << 12) | (y & 0xFFFL);
    }
}
