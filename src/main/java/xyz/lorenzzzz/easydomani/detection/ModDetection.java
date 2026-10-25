package xyz.lorenzzzz.easydomani.detection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import xyz.lorenzzzz.easydomani.EasyDomaniNonTiSvegli;
import xyz.lorenzzzz.easydomani.punishment.PunishmentManager;

/** Brand + canali registrati, matching con mods.yml, alert staff e ban (uno per sessione). */
public final class ModDetection implements Listener {
    private static final int MAX_TEXT = 64; // brand e nomi dei canali
    private static final int MAX_CHANNELS = 128;

    private final EasyDomaniNonTiSvegli plugin;
    private final PunishmentManager punishments;
    private final Map<UUID, Set<String>> detected = new ConcurrentHashMap<>();
    private final Set<UUID> punished = ConcurrentHashMap.newKeySet();

    private volatile List<ModProfile> profiles = List.of();
    private volatile boolean enabled, notifyOnJoin, notifyVanilla;
    private volatile int joinDelayTicks;

    public ModDetection(EasyDomaniNonTiSvegli plugin, PunishmentManager punishments) {
        this.plugin = plugin;
        this.punishments = punishments;
    }

    public void reload(FileConfiguration c, List<ModProfile> profiles) {
        this.profiles = profiles;
        enabled = c.getBoolean("modDetection.enabled", true);
        joinDelayTicks = Math.max(1, c.getInt("modDetection.joinDelayTicks", 40));
        notifyOnJoin = c.getBoolean("brand.notifyOnJoin", true);
        notifyVanilla = c.getBoolean("brand.notifyVanilla", false);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // task necessario: ritardo in tick (brand/canali arrivano dopo il join) e API Bukkit sul main
        Bukkit.getScheduler().runTaskLater(plugin, () -> checkOnJoin(player), joinDelayTicks);
    }

    /** Dopo joinDelayTicks: brand e canali sono arrivati, poi parte il sign probe. */
    private void checkOnJoin(Player player) {
        if (!player.isOnline()) return;
        String brand = brand(player);
        List<String> channels = channels(player);
        if (enabled) {
            for (ModProfile p : profiles) {
                if (p.matchesBrand(brand)) report(player, p, "brand");
            }
            for (String ch : channels) checkChannel(player, ch);
        }
        // diagnostica: lista completa (gia' sanitizzata e limitata a MAX_CHANNELS), indipendente da notifyOnJoin
        if (plugin.debug()) plugin.getLogger().info(player.getName() + " brand=" + brand + " canali=" + channels.size() + " " + channels);
        sendJoinInfo(player, brand, channels.size());
        if (enabled && !punished.contains(player.getUniqueId())) plugin.signProbe().startOnJoin(player); // il pack lo manda il sign probe a coda finita
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRegisterChannel(PlayerRegisterChannelEvent event) {
        if (!enabled) return;
        Player player = event.getPlayer();
        String channel = event.getChannel();
        // L'evento puo' arrivare fuori dal main thread: in quel caso si torna sul main.
        if (Bukkit.isPrimaryThread()) checkChannel(player, channel);
        else plugin.mainExecutor().execute(() -> {
            if (player.isOnline()) checkChannel(player, channel);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        detected.remove(uuid);
        punished.remove(uuid);
    }

    private void checkChannel(Player player, String channel) {
        for (ModProfile p : profiles) {
            if (p.matchesChannel(channel)) report(player, p, "canale " + sanitize(channel, MAX_TEXT));
        }
    }

    /** Main thread. Registra la mod, avvisa lo staff (una volta per mod) e banna se BAN. Ritorna true se e' un profilo BAN. */
    public boolean report(Player player, ModProfile profile, String source) {
        UUID uuid = player.getUniqueId();
        if (!player.isOnline()) return profile.ban();
        boolean first = detected.computeIfAbsent(uuid, k -> Collections.synchronizedSet(new LinkedHashSet<>())).add(profile.name());
        if (first) {
            plugin.notifyStaff(plugin.msg("mod-detected", "mod", profile.name(), "player", player.getName(), "source", source));
            plugin.getLogger().info("Rilevato " + profile.name() + " su " + player.getName() + " (" + source + ")");
        }
        if (profile.ban() && punishments.isEnabled() && !player.hasPermission("edn.bypass") && punished.add(uuid)) {
            punishments.punish(uuid, player.getName(), profile.name());
        }
        return profile.ban();
    }

    private void sendJoinInfo(Player player, String brand, int channelCount) {
        if (!notifyOnJoin || !player.isOnline()) return;
        if (!notifyVanilla && brand.equalsIgnoreCase("vanilla") && !detected.containsKey(player.getUniqueId())) return;
        plugin.notifyStaff(plugin.msg("join-info", "player", player.getName(), "brand", brand,
                "channels", String.valueOf(channelCount)));
    }

    public boolean isPunished(UUID uuid) {
        return punished.contains(uuid);
    }

    public List<String> detectedMods(UUID uuid) {
        Set<String> s = detected.get(uuid);
        if (s == null) return List.of();
        synchronized (s) {
            return new ArrayList<>(s);
        }
    }

    /** Brand pulito e limitato (input del client). */
    public static String brand(Player player) {
        String b = sanitize(player.getClientBrandName(), MAX_TEXT);
        return b.isEmpty() ? "vanilla" : b;
    }

    /** Canali registrati (gia' limitati da Paper), al massimo MAX_CHANNELS. */
    public static List<String> channels(Player player) {
        List<String> out = new ArrayList<>();
        for (String ch : player.getListeningPluginChannels()) {
            if (out.size() >= MAX_CHANNELS) break;
            out.add(sanitize(ch, MAX_TEXT));
        }
        return out;
    }

    /** Toglie caratteri di controllo e '§', tronca a max. */
    static String sanitize(String raw, int max) {
        if (raw == null) return "";
        StringBuilder sb = new StringBuilder(Math.min(raw.length(), max));
        for (int i = 0; i < raw.length() && sb.length() < max; i++) {
            char ch = raw.charAt(i);
            if (ch >= 0x20 && ch != 0x7f && ch != '§') sb.append(ch);
        }
        return sb.toString().trim();
    }
}
