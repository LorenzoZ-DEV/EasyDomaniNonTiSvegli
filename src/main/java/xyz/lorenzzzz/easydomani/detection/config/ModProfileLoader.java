package xyz.lorenzzzz.easydomani.detection.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import org.bukkit.configuration.ConfigurationSection;
import xyz.lorenzzzz.easydomani.detection.model.ModProfile;

public final class ModProfileLoader {

    private ModProfileLoader() {
    }

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
                    trimmedNonEmpty(p.getStringList("brands.exact")),
                    lower(p.getStringList("brands.contains")),
                    lower(p.getStringList("channels")),
                    key, expected, bypass, keybind));
        }
        return List.copyOf(out);
    }

    private static List<String> lower(List<String> in) {
        List<String> out = new ArrayList<>();
        for (String s : in) {
            if (s != null && !s.isBlank()) out.add(s.trim().toLowerCase(Locale.ROOT));
        }
        return List.copyOf(out);
    }

    private static List<String> trimmedNonEmpty(List<String> in) {
        List<String> out = new ArrayList<>();
        for (String s : in) {
            String t = s.trim();
            if (!t.isEmpty()) out.add(t);
        }
        return List.copyOf(out);
    }
}
