package xyz.lorenzzzz.easydomani.packprobe.config;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.bukkit.configuration.ConfigurationSection;

public record PackProbeSettings(boolean enabled, String value, List<String> overrideKeys, String externalUrl,
                                String publicUrl, String bind, int port) {
    private static final Pattern VALUE = Pattern.compile("[A-Za-z0-9_.-]{4,60}");

    public static PackProbeSettings disabled() {
        return new PackProbeSettings(false, "", List.of(), "", "", "0.0.0.0", 8164);
    }

    public static PackProbeSettings load(ConfigurationSection c, Logger logger) {
        if (!c.getBoolean("enabled", false)) return disabled();
        String external = c.getString("externalUrl", "").trim();
        String v = c.getString("value", "").trim();
        if (v.isEmpty()) {
            if (!external.isEmpty()) return disable(logger, "con externalUrl serve un 'value' fisso (il pack caricato non cambia a ogni avvio)");
            v = "edn-" + Long.toHexString(ThreadLocalRandom.current().nextLong());
        }
        if (!VALUE.matcher(v).matches()) return disable(logger, "'value' non valido (ammessi A-Z a-z 0-9 _ . -, 4-60 caratteri)");
        String base = c.getString("publicUrl", "").trim();
        int port = c.getInt("port", 8164);
        if (!external.isEmpty()) {
            if (!isHttpUrl(external)) return disable(logger, "externalUrl non valido (serve http:// o https://)");
        } else if (!isHttpUrl(base) || port < 1 || port > 65535) {
            return disable(logger, "publicUrl (http/https) e port (1-65535) obbligatori senza externalUrl");
        }
        return new PackProbeSettings(true, v, List.copyOf(c.getStringList("overrideKeys")), external, base,
                c.getString("bind", "0.0.0.0"), port);
    }

    public static boolean isHttpUrl(String s) {
        try {
            URI uri = new URI(s);
            String scheme = uri.getScheme();
            return uri.getHost() != null && ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme));
        } catch (Exception e) {
            return false;
        }
    }

    private static PackProbeSettings disable(Logger logger, String why) {
        logger.warning("Pack probe disattivato: " + why);
        return disabled();
    }
}
