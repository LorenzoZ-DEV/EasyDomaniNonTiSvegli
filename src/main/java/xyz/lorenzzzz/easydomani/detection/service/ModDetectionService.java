package xyz.lorenzzzz.easydomani.detection.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import xyz.lorenzzzz.easydomani.detection.config.ModDetectionSettings;
import xyz.lorenzzzz.easydomani.detection.model.ModProfile;
import xyz.lorenzzzz.easydomani.message.service.MessageService;
import xyz.lorenzzzz.easydomani.punishment.service.PunishmentService;

public final class ModDetectionService {
    private final Logger logger;
    private final MessageService messages;
    private final BooleanSupplier debug;
    private final PunishmentService punishments;
    private final Consumer<Player> onJoinProbe;
    private final Map<UUID, Set<String>> detected = new ConcurrentHashMap<>();
    private final Set<UUID> punished = ConcurrentHashMap.newKeySet();

    private volatile List<ModProfile> profiles = List.of();
    private volatile ModDetectionSettings settings = ModDetectionSettings.disabled();

    public ModDetectionService(Logger logger, MessageService messages, BooleanSupplier debug,
                               PunishmentService punishments, Consumer<Player> onJoinProbe) {
        this.logger = logger;
        this.messages = messages;
        this.debug = debug;
        this.punishments = punishments;
        this.onJoinProbe = onJoinProbe;
    }

    public void reload(ModDetectionSettings settings, List<ModProfile> profiles) {
        this.profiles = profiles;
        this.settings = settings;
    }

    public boolean isEnabled() {
        return settings.enabled();
    }

    public int joinDelayTicks() {
        return settings.joinDelayTicks();
    }

    public void checkOnJoin(Player player) {
        if (!player.isOnline()) return;
        boolean enabled = settings.enabled();
        String brand = ClientIdentityReader.brand(player);
        List<String> channels = ClientIdentityReader.channels(player);
        if (enabled) {
            for (ModProfile p : profiles) {
                if (p.matchesBrand(brand)) report(player, p, "brand");
            }
            for (String ch : channels) checkChannel(player, ch);
        }

        if (debug.getAsBoolean()) logger.info(player.getName() + " brand=" + brand + " canali=" + channels.size() + " " + channels);
        sendJoinInfo(player, brand, channels.size());
        if (enabled && !punished.contains(player.getUniqueId())) onJoinProbe.accept(player);
    }

    public void checkChannel(Player player, String channel) {
        for (ModProfile p : profiles) {
            if (p.matchesChannel(channel)) {
                report(player, p, "canale " + ClientIdentityReader.sanitize(channel, ClientIdentityReader.MAX_TEXT));
            }
        }
    }

    public void forget(UUID uuid) {
        detected.remove(uuid);
        punished.remove(uuid);
    }

    public boolean report(Player player, ModProfile profile, String source) {
        UUID uuid = player.getUniqueId();
        if (!player.isOnline()) return profile.ban();
        boolean first = detected.computeIfAbsent(uuid, k -> Collections.synchronizedSet(new LinkedHashSet<>())).add(profile.name());
        if (first) {
            messages.notifyStaff(messages.msg("mod-detected", "mod", profile.name(), "player", player.getName(), "source", source));
            logger.info("Rilevato " + profile.name() + " su " + player.getName() + " (" + source + ")");
        }
        if (profile.ban() && punishments.isEnabled() && !player.hasPermission("edn.bypass")) {
            punishOnce(uuid, player.getName(), profile.name());
        }
        return profile.ban();
    }

    boolean punishOnce(UUID uuid, String name, String mod) {
        if (!punished.add(uuid)) return false;
        punishments.punish(uuid, name, mod).whenComplete((ignored, error) -> {
            if (error != null) punished.remove(uuid);
        });
        return true;
    }

    private void sendJoinInfo(Player player, String brand, int channelCount) {
        ModDetectionSettings s = settings;
        if (!s.notifyOnJoin() || !player.isOnline()) return;
        if (!s.notifyVanilla() && brand.equalsIgnoreCase("vanilla") && !detected.containsKey(player.getUniqueId())) return;
        messages.notifyStaff(messages.msg("join-info", "player", player.getName(), "brand", brand,
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
}
