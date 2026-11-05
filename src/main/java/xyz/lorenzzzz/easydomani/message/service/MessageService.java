package xyz.lorenzzzz.easydomani.message.service;

import co.aikar.commands.Locales;
import co.aikar.commands.MessageKeys;
import co.aikar.commands.MinecraftMessageKeys;
import co.aikar.commands.PaperCommandManager;
import co.aikar.locales.MessageKeyProvider;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public final class MessageService {
    private final Logger logger;
    private volatile YamlConfiguration messages = new YamlConfiguration();

    public MessageService(Logger logger) {
        this.logger = logger;
    }

    public void reload(File file, InputStream defaults) {
        YamlConfiguration loaded = YamlConfiguration.loadConfiguration(file);
        if (defaults != null) {
            YamlConfiguration d = YamlConfiguration.loadConfiguration(new InputStreamReader(defaults, StandardCharsets.UTF_8));
            for (String k : d.getKeys(true)) {
                if (!d.isConfigurationSection(k) && !loaded.contains(k, true)) loaded.set(k, d.get(k));
            }
        }
        messages = loaded;
    }

    public void applyAcf(PaperCommandManager manager) {
        ConfigurationSection acf = messages.getConfigurationSection("acf");
        if (acf == null) return;
        for (String k : acf.getKeys(false)) {
            MessageKeyProvider key = acfKey(k.toUpperCase(Locale.ROOT).replace('-', '_'));
            if (key == null) {
                logger.warning("messages.yml: chiave ACF sconosciuta '" + k + "'");
                continue;
            }
            manager.getLocales().addMessage(Locales.ITALIAN, key, ChatTextFormatter.legacy(format(acf.getString(k, ""))));
        }
    }

    private static MessageKeyProvider acfKey(String name) {
        try {
            return MessageKeys.valueOf(name);
        } catch (IllegalArgumentException e) {
            try {
                return MinecraftMessageKeys.valueOf(name);
            } catch (IllegalArgumentException e2) {
                return null;
            }
        }
    }

    private Component format(String template, String... kv) {
        return ChatTextFormatter.parseWithPrefix(messages.getString("prefix", ""), template, kv);
    }

    public Component msg(String key, String... kv) {
        YamlConfiguration m = messages;
        String t = m.isList(key) ? String.join("\n", m.getStringList(key)) : m.getString(key, key);
        return format(t, kv);
    }

    public List<Component> lines(String key, String... kv) {
        List<Component> out = new ArrayList<>();
        for (String l : messages.getStringList(key)) out.add(format(l, kv));
        return List.copyOf(out);
    }

    public void notifyStaff(Component message) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("edn.notify")) p.sendMessage(message);
        }
        Bukkit.getConsoleSender().sendMessage(message);
    }
}
