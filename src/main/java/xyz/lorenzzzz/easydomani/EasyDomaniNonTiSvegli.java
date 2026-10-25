package xyz.lorenzzzz.easydomani;

import co.aikar.commands.Locales;
import co.aikar.commands.MessageKeys;
import co.aikar.commands.MinecraftMessageKeys;
import co.aikar.commands.PaperCommandManager;
import co.aikar.locales.MessageKeyProvider;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.lorenzzzz.easydomani.command.EdnCommand;
import xyz.lorenzzzz.easydomani.command.EdnHelpFormatter;
import xyz.lorenzzzz.easydomani.detection.ModDetection;
import xyz.lorenzzzz.easydomani.detection.ModProfile;
import xyz.lorenzzzz.easydomani.detection.probe.PackProbe;
import xyz.lorenzzzz.easydomani.detection.probe.RotationProbe;
import xyz.lorenzzzz.easydomani.detection.probe.SignProbe;
import xyz.lorenzzzz.easydomani.honeypot.Honeypot;
import xyz.lorenzzzz.easydomani.punishment.PunishmentManager;
import xyz.lorenzzzz.easydomani.seed.SeedHider;
import xyz.lorenzzzz.easydomani.util.TextUtil;

/** Entry point: carica config/mods.yml, avvia i moduli, helper per messaggi e alert. */
public final class EasyDomaniNonTiSvegli extends JavaPlugin {
    private PunishmentManager punishments;
    private ModDetection modDetection;
    private SignProbe signProbe;
    private PackProbe packProbe;
    private RotationProbe rotationProbe;
    private Honeypot honeypot;
    private SeedHider seedHider;
    private Executor mainExecutor;
    private volatile boolean debug;
    private volatile YamlConfiguration messages = new YamlConfiguration();
    private PaperCommandManager commands;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveIfMissing("mods.yml");
        saveIfMissing("messages.yml");

        mainExecutor = Bukkit.getScheduler().getMainThreadExecutor(this);
        punishments = new PunishmentManager(this);
        try {
            punishments.open();
        } catch (SQLException | RuntimeException e) {
            // senza DB i ban a scala non sono affidabili: meglio fermarsi in modo esplicito
            getLogger().log(Level.SEVERE, "Database H2 non disponibile, plugin disattivato", e);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        modDetection = new ModDetection(this, punishments);
        signProbe = new SignProbe(this, modDetection);
        packProbe = new PackProbe(this, signProbe);
        rotationProbe = new RotationProbe(this);
        honeypot = new Honeypot(this);
        seedHider = new SeedHider(this);
        reload();

        var pm = Bukkit.getPluginManager();
        pm.registerEvents(punishments, this);
        pm.registerEvents(modDetection, this);
        pm.registerEvents(signProbe, this);
        pm.registerEvents(packProbe, this);
        pm.registerEvents(rotationProbe, this);
        pm.registerEvents(honeypot, this);
        signProbe.start();
        rotationProbe.start();
        honeypot.start();
        seedHider.start();
        registerCommands();
    }

    @SuppressWarnings("deprecation")
    private void registerCommands() {
        commands = new PaperCommandManager(this);
        commands.enableUnstableAPI("help");
        commands.getLocales().setDefaultLocale(Locales.ITALIAN); // errori ACF in italiano
        commands.setHelpFormatter(new EdnHelpFormatter(commands, this));
        applyAcfMessages();
        commands.registerCommand(new EdnCommand(this));
    }

    /** Evita il warning di saveResource quando il file esiste gia'. */
    private void saveIfMissing(String name) {
        if (!new File(getDataFolder(), name).exists()) saveResource(name, false);
    }

    @Override
    public void onDisable() {
        if (seedHider != null) seedHider.stop();
        if (honeypot != null) honeypot.stop();
        if (rotationProbe != null) rotationProbe.stop();
        if (packProbe != null) packProbe.stop();
        if (signProbe != null) signProbe.stop();
        if (punishments != null) punishments.close(); // attende le scritture in coda, poi chiude il pool
    }

    /** Ricarica config.yml, messages.yml e mods.yml e propaga ai moduli. */
    public void reload() {
        reloadConfig();
        addMissingDefaults();
        messages = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "messages.yml"));
        addMissingMessages();
        if (commands != null) applyAcfMessages();
        var c = getConfig();
        debug = c.getBoolean("debug", false);
        List<ModProfile> profiles = ModProfile.load(
                YamlConfiguration.loadConfiguration(new File(getDataFolder(), "mods.yml")).getConfigurationSection("profiles"),
                getLogger());
        punishments.reload(section("punishment"));
        modDetection.reload(c, profiles);
        signProbe.reload(section("modDetection.probe"), profiles);
        packProbe.reload(section("modDetection.packProbe"));
        rotationProbe.reload(section("modDetection.rotationProbe"));
        honeypot.reload(section("honeypot"));
        seedHider.reload(c.getBoolean("antiSeedCracker.enabled", true));
        if (debug) getLogger().info("Caricati " + profiles.size() + " profili da mods.yml");
    }

    /** Aggiunge al config.yml le chiavi presenti nel jar ma mancanti sul server, senza toccare i valori esistenti. */
    private void addMissingDefaults() {
        var c = getConfig();
        var defaults = c.getDefaults();
        if (defaults == null) return;
        List<String> missing = defaults.getKeys(true).stream()
                .filter(k -> !defaults.isConfigurationSection(k) && !c.contains(k, true))
                .toList();
        if (missing.isEmpty()) return;
        c.options().copyDefaults(true);
        try {
            c.save(new File(getDataFolder(), "config.yml"));
            getLogger().info("Aggiunte " + missing.size() + " chiavi mancanti a config.yml: " + String.join(", ", missing));
        } catch (IOException e) {
            getLogger().warning("Impossibile salvare config.yml con le chiavi mancanti: " + e.getMessage());
        }
    }

    /** Chiavi di messages.yml del jar mancanti sul server: aggiunte solo in memoria, il file non viene toccato. */
    private void addMissingMessages() {
        var in = getResource("messages.yml");
        if (in == null) return;
        var defaults = YamlConfiguration.loadConfiguration(new InputStreamReader(in, StandardCharsets.UTF_8));
        for (String k : defaults.getKeys(true)) {
            if (!defaults.isConfigurationSection(k) && !messages.contains(k, true)) messages.set(k, defaults.get(k));
        }
    }

    private ConfigurationSection section(String path) {
        ConfigurationSection s = getConfig().getConfigurationSection(path);
        return s != null ? s : getConfig().createSection(path);
    }

    /** Executor del main thread: per tornare sulle API Bukkit da un CompletableFuture o da Netty. */
    public Executor mainExecutor() {
        return mainExecutor;
    }

    public boolean debug() {
        return debug;
    }

    private void applyAcfMessages() {
        ConfigurationSection acf = messages.getConfigurationSection("acf");
        if (acf == null) return;
        for (String k : acf.getKeys(false)) {
            MessageKeyProvider key = acfKey(k.toUpperCase(Locale.ROOT).replace('-', '_'));
            if (key == null) {
                getLogger().warning("messages.yml: chiave ACF sconosciuta '" + k + "'");
                continue;
            }
            commands.getLocales().addMessage(Locales.ITALIAN, key, TextUtil.legacy(format(acf.getString(k, ""))));
        }
    }

    /** Chiave ACF generica o Minecraft; null se non esiste. */
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

    /** Template di messages.yml con %prefix% fidato; i kv restano non fidati. */
    private Component format(String template, String... kv) {
        return TextUtil.parseWithPrefix(messages.getString("prefix", ""), template, kv);
    }

    /** Messaggio da messages.yml (stringa o lista unita da a capo); %prefix% e' disponibile. */
    public Component msg(String key, String... kv) {
        String t = messages.isList(key) ? String.join("\n", messages.getStringList(key)) : messages.getString(key, key);
        return format(t, kv);
    }

    /** Lista di righe di messages.yml (es. info, help.header). */
    public List<Component> lines(String key, String... kv) {
        return messages.getStringList(key).stream().map(l -> format(l, kv)).toList();
    }

    /** Alert a chi ha edn.notify e alla console. */
    public void notifyStaff(Component message) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("edn.notify")) p.sendMessage(message);
        }
        Bukkit.getConsoleSender().sendMessage(message); // Paper la rende come testo/ANSI
    }

    public PunishmentManager punishments() {
        return punishments;
    }

    public ModDetection modDetection() {
        return modDetection;
    }

    public SignProbe signProbe() {
        return signProbe;
    }

    public PackProbe packProbe() {
        return packProbe;
    }

    public RotationProbe rotationProbe() {
        return rotationProbe;
    }

    public Honeypot honeypot() {
        return honeypot;
    }
}
