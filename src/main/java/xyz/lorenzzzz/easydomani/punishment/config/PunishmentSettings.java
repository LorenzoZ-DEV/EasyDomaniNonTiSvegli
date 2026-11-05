package xyz.lorenzzzz.easydomani.punishment.config;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.configuration.ConfigurationSection;
import xyz.lorenzzzz.easydomani.punishment.service.PunishmentDurations;

public record PunishmentSettings(
        boolean enabled,
        List<Duration> ladder,
        Duration resetAfter,
        String banCommand,
        String banCommandPermanent,
        String banCommandReason,
        String source,
        List<String> postBanCommands) {

    public static final List<Duration> DEFAULT_LADDER = List.of(Duration.ofMinutes(10), Duration.ofHours(12), Duration.ofDays(3));

    public static PunishmentSettings disabled() {
        return new PunishmentSettings(false, DEFAULT_LADDER, Duration.ZERO, "", "perm",
                "Cheat client non consentito: {mod}", "EasyDomaniNonTiSvegli", List.of());
    }

    public static PunishmentSettings load(ConfigurationSection c, Logger logger) {
        List<Duration> l = new ArrayList<>();
        for (String step : c.getStringList("ladder")) {
            Duration d = PunishmentDurations.parseDuration(step);
            if (d != null && !d.isZero()) l.add(d);
            else logger.warning("Durata ladder non valida: '" + step + "' (es. 30s 10m 12h 3d 2w perm)");
        }
        Duration r = PunishmentDurations.parseDuration(c.getString("resetAfter", "0"));
        List<String> commands = new ArrayList<>();
        for (String s : c.getStringList("postBanCommands")) {
            if (!s.isBlank()) commands.add(s);
        }
        return new PunishmentSettings(
                c.getBoolean("enabled", true),
                l.isEmpty() ? DEFAULT_LADDER : List.copyOf(l),
                r == null || r.isNegative() ? Duration.ZERO : r,
                c.getString("banCommand", ""),
                c.getString("banCommandPermanent", "perm"),
                c.getString("banCommandReason", "Cheat client non consentito: {mod}"),
                c.getString("source", "EasyDomaniNonTiSvegli"),
                List.copyOf(commands));
    }
}
