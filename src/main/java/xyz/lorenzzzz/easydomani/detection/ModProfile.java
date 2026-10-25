package xyz.lorenzzzz.easydomani.detection;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import org.bukkit.configuration.ConfigurationSection;

/** Profilo di una mod letto da mods.yml + matching puro (testabile senza server). */
public record ModProfile(String id, String name, boolean ban,
                         List<String> brandExact, List<String> brandContains, List<String> channelPrefixes,
                         String probeKey, List<String> probeExpected, boolean probeBypass, boolean probeKeybind) {

    public boolean hasProbe() {
        return probeKey != null && (probeKeybind || !probeExpected.isEmpty());
    }

    public boolean matchesBrand(String brand) {
        if (brand == null || brand.isBlank()) return false;
        String b = brand.trim();
        for (String e : brandExact) if (b.equalsIgnoreCase(e)) return true;
        if (brandContains.isEmpty()) return false; // caso comune: niente lowercase
        String low = b.toLowerCase(Locale.ROOT);
        for (String c : brandContains) if (low.contains(c)) return true;
        return false;
    }

    /** Prefisso case-insensitive senza creare stringhe (i prefissi sono gia' lowercase). */
    public boolean matchesChannel(String channel) {
        if (channel == null || channel.isEmpty()) return false;
        for (String p : channelPrefixes) if (channel.regionMatches(true, 0, p, 0, p.length())) return true;
        return false;
    }

    /** Legge la sezione "profiles"; i profili non validi vengono saltati con un warning. */
    public static List<ModProfile> load(ConfigurationSection profiles, Logger log) {
        List<ModProfile> out = new ArrayList<>();
        if (profiles == null) return out;
        for (String id : profiles.getKeys(false)) {
            ConfigurationSection p = profiles.getConfigurationSection(id);
            if (p == null || !p.getBoolean("enabled", true)) continue;
            String policy = p.getString("policy", "ALERT").trim().toUpperCase(Locale.ROOT);
            if (!policy.equals("BAN") && !policy.equals("ALERT")) {
                log.warning("mods.yml: policy '" + policy + "' non valida per " + id + ", uso ALERT");
            }
            String key = null;
            List<String> expected = List.of();
            boolean bypass = false, keybind = false;
            ConfigurationSection probe = p.getConfigurationSection("probe");
            if (probe != null && probe.getBoolean("enabled", true)) {
                key = probe.getString("key");
                expected = lower(probe.getStringList("expected"));
                bypass = probe.getBoolean("bypass", false);
                keybind = probe.getBoolean("keybind", false);
                if (key == null || key.isBlank() || (!keybind && expected.isEmpty())) {
                    log.warning("mods.yml: probe di " + id + " senza key/expected, ignorato");
                    key = null;
                    expected = List.of();
                    keybind = false;
                }
            }
            out.add(new ModProfile(id, p.getString("name", id), policy.equals("BAN"),
                    p.getStringList("brands.exact").stream().map(String::trim).filter(s -> !s.isEmpty()).toList(),
                    lower(p.getStringList("brands.contains")),
                    lower(p.getStringList("channels")),
                    key, expected, bypass, keybind));
        }
        return List.copyOf(out);
    }

    private static List<String> lower(List<String> in) {
        return in.stream().filter(s -> s != null && !s.isBlank()).map(s -> s.trim().toLowerCase(Locale.ROOT)).toList();
    }
}
