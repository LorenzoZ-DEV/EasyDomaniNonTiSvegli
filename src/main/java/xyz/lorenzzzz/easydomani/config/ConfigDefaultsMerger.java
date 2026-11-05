package xyz.lorenzzzz.easydomani.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

public final class ConfigDefaultsMerger {
    private final Logger logger;

    public ConfigDefaultsMerger(Logger logger) {
        this.logger = logger;
    }

    public void addMissingDefaults(FileConfiguration c, File file) {
        Configuration defaults = c.getDefaults();
        if (defaults == null) return;
        List<String> missing = new ArrayList<>();
        for (String k : defaults.getKeys(true)) {
            if (!defaults.isConfigurationSection(k) && !c.contains(k, true)) missing.add(k);
        }
        if (missing.isEmpty()) return;
        c.options().copyDefaults(true);
        try {
            c.save(file);
            logger.info("Aggiunte " + missing.size() + " chiavi mancanti a config.yml: " + String.join(", ", missing));
        } catch (IOException e) {
            logger.warning("Impossibile salvare config.yml con le chiavi mancanti: " + e.getMessage());
        }
    }
}
