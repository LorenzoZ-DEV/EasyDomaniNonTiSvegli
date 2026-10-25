package xyz.lorenzzzz.easydomani.detection.probe;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.nbt.NBTType;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.blockentity.BlockEntityTypes;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateSign;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockEntityData;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCloseWindow;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenSignEditor;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.lorenzzzz.easydomani.EasyDomaniNonTiSvegli;
import xyz.lorenzzzz.easydomani.detection.ModDetection;
import xyz.lorenzzzz.easydomani.detection.ModProfile;

/**
 * Sign probe: cartello finto (solo pacchetti) con translation key delle mod, editor aperto
 * e chiuso subito; il client rimanda le righe tradotte. Una key tradotta = mod installata.
 * Profili keybind: {"keybind":key} e' risolto dal registro KeyMapping del client (non dipende dalle lang,
 * es. Lunar): qualsiasi risposta diversa dalla key = MATCH.
 * Riga 4 = sentinella casuale per capire se l'editor e' stato manomesso.
 */
public final class SignProbe implements Listener {
    enum Result { MATCH, NO_MATCH, UNKNOWN, TAMPERED }

    private static final Pattern COLOR = Pattern.compile("(?i)&[0-9A-FK-OR]|§[0-9A-FK-OR]");
    private static final long LATE_WRITE_GUARD_MS = 5000L;

    /**
     * Chiavi di controllo, solo con debug: loggate e basta (niente classify/report/retry) per confrontare
     * un client vanilla con le mod che filtrano il probe (es. ExploitPreventer). Vanilla atteso:
     * translate key.jump = nome tradotto, keybind key.jump = tasto assegnato, key sconosciuta + fallback = FB_TEST.
     */
    static final String CTRL_FALLBACK_TEXT = "FB_TEST";
    static final ModProfile CTRL_TRANSLATE = control("ctrl-translate", "key.jump", false);
    static final ModProfile CTRL_KEYBIND = control("ctrl-keybind", "key.jump", true);
    static final ModProfile CTRL_FALLBACK = control("ctrl-fallback", "key.anubis.open_gui", false);
    static final List<ModProfile> CONTROLS = List.of(CTRL_TRANSLATE, CTRL_KEYBIND, CTRL_FALLBACK);
    /** Pack probe: key del resource pack del server, probeExpected = valore atteso (solo per il log). */
    static final String CTRL_PACK_ID = "ctrl-pack";
    /** Key di una mod sovrascritta dal pack: translate (con fallback) e keybind. */
    static final String CTRL_OVR_TRANSLATE_ID = "ctrl-ovr-translate", CTRL_OVR_KEYBIND_ID = "ctrl-ovr-keybind";
    private static final Set<String> PACK_CONTROL_IDS = Set.of(CTRL_PACK_ID, CTRL_OVR_TRANSLATE_ID, CTRL_OVR_KEYBIND_ID);

    private final EasyDomaniNonTiSvegli plugin;
    private final ModDetection detection;
    private final Map<UUID, Session> sessions = new ConcurrentHashMap<>();
    private final AtomicLong ids = new AtomicLong();
    private PacketListenerAbstract listener;
    /** Calcolati una volta in start(): non cambiano finche' il server e' acceso. */
    private int signStateId;
    private boolean modernSignFormat;

    private volatile List<ModProfile> table = List.of();
    private volatile boolean enabled;
    private volatile int timeoutTicks, autoCloseTicks, intervalTicks, keysPerSign, maxPerSession, maxDeferrals;

    /** Stato per player. queue/retried/opened solo dal main thread; active/late letti anche da Netty. */
    private static final class Session {
        final Deque<ModProfile> queue = new ArrayDeque<>();
        final Set<String> retried = new HashSet<>();
        int opened;
        boolean packSent;
        volatile Active active;
        volatile Active late;

        Session(List<ModProfile> table) {
            queue.addAll(table);
        }

        /** Chiude il probe se e' ancora quello attivo; le risposte in ritardo restano ignorate per un po'. */
        boolean close(Active a) {
            if (active != a) return false; // gia' scaduto o sostituito
            active = null;
            late = a;
            return true;
        }

        /** Un solo nuovo tentativo per key (timeout o risposta anomala). */
        void retry(ModProfile def) {
            if (retried.add(def.id())) queue.addLast(def);
        }
    }

    private record Active(long id, Vector3i pos, int originalState, String sentinel, List<ModProfile> defs, long lateUntil) {}

    public SignProbe(EasyDomaniNonTiSvegli plugin, ModDetection detection) {
        this.plugin = plugin;
        this.detection = detection;
    }

    public void reload(ConfigurationSection c, List<ModProfile> profiles) {
        table = profiles.stream().filter(ModProfile::hasProbe).toList();
        enabled = c.getBoolean("enabled", true);
        timeoutTicks = Math.max(20, c.getInt("timeoutTicks", 40));
        autoCloseTicks = Math.max(0, c.getInt("autoCloseTicks", 5));
        intervalTicks = Math.max(1, c.getInt("intervalTicks", 10));
        keysPerSign = Math.max(1, Math.min(3, c.getInt("keysPerSign", 3)));
        maxPerSession = Math.max(0, c.getInt("maxPerSession", 3));
        maxDeferrals = Math.max(0, c.getInt("maxDeferrals", 5));
    }

    public void start() {
        signStateId = SpigotConversionUtil.fromBukkitBlockData(Material.OAK_SIGN.createBlockData()).getGlobalId();
        modernSignFormat = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5);
        listener = new PacketListenerAbstract(PacketListenerPriority.LOWEST) {
            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                if (event.getPacketType() != PacketType.Play.Client.UPDATE_SIGN) return;
                UUID uuid = event.getUser() != null ? event.getUser().getUUID() : null;
                Session s = uuid != null ? sessions.get(uuid) : null;
                if (s == null) return;
                Active a = s.active;
                Active late = s.late;
                if (a == null && (late == null || System.currentTimeMillis() >= late.lateUntil)) return; // cartello vero: niente parsing
                WrapperPlayClientUpdateSign w = new WrapperPlayClientUpdateSign(event);
                Vector3i pos = w.getBlockPosition();
                if (a != null && a.pos.equals(pos)) {
                    event.setCancelled(true);
                    String[] lines = w.getTextLines();
                    plugin.mainExecutor().execute(() -> handleResponse(uuid, s, a, lines)); // Netty -> main
                } else if (late != null && late.pos.equals(pos) && System.currentTimeMillis() < late.lateUntil) {
                    event.setCancelled(true); // risposta in ritardo a un probe gia' chiuso
                }
            }
        };
        PacketEvents.getAPI().getEventManager().registerListener(listener);
    }

    public void stop() {
        if (listener != null) PacketEvents.getAPI().getEventManager().unregisterListener(listener);
        listener = null;
        for (Map.Entry<UUID, Session> e : sessions.entrySet()) {
            Active a = e.getValue().active;
            Player p = Bukkit.getPlayer(e.getKey());
            if (a != null && p != null) restore(p, a);
        }
        sessions.clear();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        sessions.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Session s = sessions.get(event.getPlayer().getUniqueId());
        if (s != null) s.active = null; // il cartello era nel mondo precedente
    }

    /** Chiamato dopo joinDelayTicks. */
    public void startOnJoin(Player player) {
        if (!enabled || table.isEmpty() || player.hasPermission("edn.bypass") || isLikelyBedrock(player)) return;
        sessions.put(player.getUniqueId(), new Session(probeList()));
        next(player, 0);
    }

    /**
     * Main thread. Pack del server caricato: in testa alla coda ctrl-pack e, se c'e' una key sovrascritta,
     * ctrl-ovr-translate/keybind (stesso cartello con keysPerSign 3). False se non c'e' una sessione.
     */
    public boolean queuePackControls(Player player, String expected, String overrideKey, String overrideValue) {
        Session s = sessions.get(player.getUniqueId());
        if (s == null) return false;
        List<ModProfile> controls = packControls(expected, overrideKey, overrideValue);
        for (int i = controls.size() - 1; i >= 0; i--) s.queue.addFirst(controls.get(i));
        if (s.active == null) later(intervalTicks, () -> next(player, 0)); // next() ignora i doppioni se gia' attivo
        return true;
    }

    /**
     * Coda vuota. Prima volta: manda il pack, cosi' i probe veri girano tutti SENZA pack (le key sovrascritte
     * cambierebbero le risposte: un keybind non risolto diventerebbe MATCH su un client vanilla).
     * Dopo i controlli del pack: lo toglie.
     */
    private void drained(Player player, Session s) {
        PackProbe pack = plugin.packProbe();
        if (pack == null || !player.isOnline()) return;
        if (!s.packSent) {
            s.packSent = true;
            pack.send(player);
        } else {
            pack.remove(player);
        }
    }

    /** /edn probe: nuova sessione completa (ignora quelle gia' fatte). */
    public boolean force(Player player) {
        Session old = sessions.get(player.getUniqueId());
        if (table.isEmpty() || (old != null && old.active != null)) return false;
        sessions.put(player.getUniqueId(), new Session(probeList()));
        return next(player, 0);
    }

    /** Profili della sessione; con debug in coda le chiavi di controllo. */
    private List<ModProfile> probeList() {
        if (!plugin.debug()) return table;
        List<ModProfile> list = new ArrayList<>(table);
        list.addAll(CONTROLS);
        return list;
    }

    /** Main thread. Editor aperto o key ancora in coda: altri probe non devono sovrapporsi. */
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
        if (maxPerSession > 0 && s.opened >= maxPerSession) {
            s.queue.removeIf(d -> !isControl(d)); // i cartelli di soli controlli non contano
            if (s.queue.isEmpty()) {
                drained(player, s);
                return false;
            }
        }
        Block block = findLocation(player);
        if (block == null) {
            if (deferrals < maxDeferrals) later(20, () -> next(player, deferrals + 1));
            return false;
        }
        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        if (user == null) return false;

        List<ModProfile> group = new ArrayList<>(keysPerSign);
        while (group.size() < keysPerSign && !s.queue.isEmpty()) group.add(s.queue.poll());
        Active a = new Active(ids.incrementAndGet(), new Vector3i(block.getX(), block.getY(), block.getZ()),
                SpigotConversionUtil.fromBukkitBlockData(block.getBlockData()).getGlobalId(),
                "edn-" + Long.toHexString(ThreadLocalRandom.current().nextLong()), List.copyOf(group),
                System.currentTimeMillis() + timeoutTicks * 50L + LATE_WRITE_GUARD_MS);
        s.active = a;
        if (!group.stream().allMatch(SignProbe::isControl)) s.opened++; // l'editor di soli controlli non consuma maxPerSession
        try {
            sendSign(user, a);
        } catch (Throwable t) {
            s.active = null;
            restore(player, a);
            plugin.getLogger().warning("Sign probe non inviato a " + player.getName() + ": " + t);
            return false;
        }
        if (plugin.debug()) plugin.getLogger().info("[Probe] " + player.getName() + " #" + a.id + " key=" + group.stream().map(d -> isControl(d) ? d.id() + ":" + d.probeKey() : d.probeKey()).toList());

        if (autoCloseTicks > 0) later(autoCloseTicks, () -> autoClose(player, user, s, a));
        later(timeoutTicks, () -> timeout(player, s, a));
        return true;
    }

    private static void autoClose(Player player, User user, Session s, Active a) {
        if (s.active != a || !player.isOnline()) return;
        user.sendPacket(new WrapperPlayServerCloseWindow(0)); // il client chiude l'editor e manda UPDATE_SIGN
        restore(player, a);
    }

    private void timeout(Player player, Session s, Active a) {
        if (!s.close(a)) return;
        if (plugin.debug()) plugin.getLogger().info("[Probe] " + player.getName() + " #" + a.id + " TIMEOUT");
        if (!player.isOnline()) return;
        restore(player, a);
        for (ModProfile def : a.defs) s.retry(def);
        scheduleNext(player, s);
    }

    private void handleResponse(UUID uuid, Session s, Active a, String[] lines) {
        if (!s.close(a)) return;
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        restore(player, a);
        boolean intact = isIntact(lines, a.sentinel);
        boolean stop = false;
        for (int i = 0; i < a.defs.size(); i++) {
            ModProfile def = a.defs.get(i);
            String line = lines != null && i < lines.length ? lines[i] : null;
            if (isControl(def)) { // solo diagnostica: nessuna sanzione ne' retry
                String exp = def.probeExpected().isEmpty() ? "" : " expected='" + def.probeExpected().get(0) + "'";
                if (plugin.debug()) plugin.getLogger().info("[Probe] " + player.getName() + " #" + a.id + " " + def.id() + " -> '" + line + "'" + exp + " = DIAG intact=" + intact);
                continue;
            }
            Result r = classify(def, a.sentinel, line, intact);
            if (plugin.debug()) plugin.getLogger().info("[Probe] " + player.getName() + " #" + a.id + " " + def.id() + " -> '" + line + "' = " + r);
            switch (r) {
                case MATCH -> {
                    PackProbe pack = plugin.packProbe();
                    if (pack != null && pack.isPending(uuid)) { // risposte falsate dalle key del pack: niente sanzione
                        plugin.getLogger().info("[Probe] " + player.getName() + " #" + a.id + " " + def.id() + " MATCH ignorato: pack probe attivo");
                    } else {
                        stop |= detection.report(player, def, "sign probe");
                    }
                }
                case UNKNOWN, TAMPERED -> s.retry(def);
                case NO_MATCH -> { }
            }
        }
        if (stop) s.queue.clear(); // profilo BAN: inutile continuare
        else scheduleNext(player, s);
    }

    private void scheduleNext(Player player, Session s) {
        if (!s.queue.isEmpty()) later(intervalTicks, () -> next(player, 0));
        else drained(player, s);
    }

    /** Task necessario: attese in tick tra probe/timeout, il callback usa API Bukkit sul main. */
    private void later(long ticks, Runnable r) {
        Bukkit.getScheduler().runTaskLater(plugin, r, ticks);
    }

    // ---- logica pura (testata) ----

    static Result classify(ModProfile def, String sentinel, String raw, boolean intact) {
        String response = clean(raw);
        if (response.isEmpty()) return Result.TAMPERED;
        String norm = response.toLowerCase(Locale.ROOT);
        String key = def.probeKey().toLowerCase(Locale.ROOT);
        // key rimandata identica = client vanilla (non la conosce)
        if (norm.equals(key) || (norm.startsWith("{") && norm.contains(key))) return Result.NO_MATCH;
        if (sentinel != null && norm.equals(sentinel.toLowerCase(Locale.ROOT))) return Result.NO_MATCH;
        if (def.probeKeybind()) return intact ? Result.MATCH : Result.TAMPERED; // editor manomesso: non fidarsi
        if (def.probeExpected().contains(norm)) return Result.MATCH;
        return intact ? Result.UNKNOWN : Result.TAMPERED;
    }

    static boolean isIntact(String[] lines, String sentinel) {
        return lines != null && lines.length > 3 && sentinel != null && clean(lines[3]).equals(sentinel);
    }

    private static String clean(String line) {
        if (line == null) return "";
        String c = COLOR.matcher(line).replaceAll("").trim();
        if (c.length() >= 2 && c.charAt(0) == '"' && c.charAt(c.length() - 1) == '"') c = c.substring(1, c.length() - 1).trim();
        return c;
    }

    // ---- pacchetti ----

    private void sendSign(User user, Active a) {
        user.sendPacket(new WrapperPlayServerBlockChange(a.pos, signStateId));
        user.sendPacket(new WrapperPlayServerBlockEntityData(a.pos, BlockEntityTypes.SIGN, buildSignNbt(a.defs, a.sentinel, modernSignFormat)));
        user.sendPacket(new WrapperPlayServerOpenSignEditor(a.pos, true));
    }

    /** NBT del cartello: componenti NBT (1.21.5+) o JSON (prima). Text1..4 per i client vecchi. */
    static NBTCompound buildSignNbt(List<ModProfile> defs, String sentinel, boolean modern) {
        NBTCompound root = new NBTCompound();
        NBTCompound front = new NBTCompound();
        if (modern) {
            NBTList<NBTCompound> messages = new NBTList<>(NBTType.COMPOUND);
            for (int i = 0; i < 4; i++) {
                NBTCompound line = i < defs.size() ? keyComponent(defs.get(i)) : textComponent(i == 3 ? sentinel : "");
                messages.addTag(line);
                root.setTag("Text" + (i + 1), line);
            }
            front.setTag("messages", messages);
        } else {
            NBTList<NBTString> messages = new NBTList<>(NBTType.STRING);
            for (int i = 0; i < 4; i++) {
                NBTString line = new NBTString(i < defs.size() ? keyJson(defs.get(i)) : "{\"text\":\"" + (i == 3 ? sentinel : "") + "\"}");
                messages.addTag(line);
                root.setTag("Text" + (i + 1), line);
            }
            front.setTag("messages", messages);
        }
        root.setTag("front_text", front);
        root.setTag("is_waxed", new NBTByte((byte) 0));
        return root;
    }

    /** bypass: {"translate":"%s","with":[{"translate":key}]} supera i filtri sulle key note (Meteor). */
    static String keyJson(ModProfile def) {
        if (def.probeKeybind()) return "{\"keybind\":\"" + def.probeKey() + "\"}";
        if (hasFallback(def)) return "{\"translate\":\"" + def.probeKey() + "\",\"fallback\":\"" + CTRL_FALLBACK_TEXT + "\"}";
        return def.probeBypass()
                ? "{\"translate\":\"%s\",\"with\":[{\"translate\":\"" + def.probeKey() + "\"}]}"
                : "{\"translate\":\"" + def.probeKey() + "\"}";
    }

    private static NBTCompound keyComponent(ModProfile def) {
        NBTCompound inner = new NBTCompound();
        inner.setTag(def.probeKeybind() ? "keybind" : "translate", new NBTString(def.probeKey()));
        if (def.probeKeybind()) return inner;
        if (hasFallback(def)) inner.setTag("fallback", new NBTString(CTRL_FALLBACK_TEXT));
        if (!def.probeBypass()) return inner;
        NBTCompound outer = new NBTCompound();
        outer.setTag("translate", new NBTString("%s"));
        NBTList<NBTCompound> with = new NBTList<>(NBTType.COMPOUND);
        with.addTag(inner);
        outer.setTag("with", with);
        return outer;
    }

    private static ModProfile control(String id, String key, boolean keybind) {
        return new ModProfile(id, id, false, List.of(), List.of(), List.of(), key, List.of(), false, keybind);
    }

    /** ctrl-pack + (se overrideKey != null) ctrl-ovr-translate e ctrl-ovr-keybind; expected = valore del pack. */
    static List<ModProfile> packControls(String expected, String overrideKey, String overrideValue) {
        List<ModProfile> out = new ArrayList<>(3);
        out.add(packControl(CTRL_PACK_ID, PackProbe.KEY, expected, false));
        if (overrideKey != null) {
            out.add(packControl(CTRL_OVR_TRANSLATE_ID, overrideKey, overrideValue, false));
            out.add(packControl(CTRL_OVR_KEYBIND_ID, overrideKey, overrideValue, true));
        }
        return out;
    }

    private static ModProfile packControl(String id, String key, String expected, boolean keybind) {
        return new ModProfile(id, id, false, List.of(), List.of(), List.of(), key, List.of(expected), false, keybind);
    }

    static boolean isControl(ModProfile def) {
        return CONTROLS.contains(def) || PACK_CONTROL_IDS.contains(def.id());
    }

    /** I keybind ignorano il fallback (keyComponent/keyJson tornano prima). */
    private static boolean hasFallback(ModProfile def) {
        return CTRL_FALLBACK.equals(def) || CTRL_PACK_ID.equals(def.id()) || CTRL_OVR_TRANSLATE_ID.equals(def.id());
    }

    private static NBTCompound textComponent(String text) {
        NBTCompound c = new NBTCompound();
        c.setTag("text", new NBTString(text));
        return c;
    }

    private static void restore(Player player, Active a) {
        if (!player.isOnline()) return;
        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        if (user != null) user.sendPacket(new WrapperPlayServerBlockChange(a.pos, a.originalState));
    }

    /** Primo blocco non solido attorno al player (sopra la testa, sotto i piedi, ai lati). */
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

    /** Floodgate usa UUID con i 64 bit alti a zero; Geyser manda brand "geyser". Niente reflection. */
    static boolean isLikelyBedrock(Player player) {
        return player.getUniqueId().getMostSignificantBits() == 0L || "geyser".equalsIgnoreCase(ModDetection.brand(player));
    }
}
