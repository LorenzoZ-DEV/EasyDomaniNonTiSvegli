package xyz.lorenzzzz.easydomani.bootstrap;

import co.aikar.commands.Locales;
import co.aikar.commands.PaperCommandManager;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerCommon;
import java.io.File;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.lorenzzzz.easydomani.command.EdnCommand;
import xyz.lorenzzzz.easydomani.command.EdnHelpFormatter;
import xyz.lorenzzzz.easydomani.config.ConfigDefaultsMerger;
import xyz.lorenzzzz.easydomani.config.PluginSettings;
import xyz.lorenzzzz.easydomani.detection.config.ModDetectionSettings;
import xyz.lorenzzzz.easydomani.detection.config.ModProfileLoader;
import xyz.lorenzzzz.easydomani.detection.listener.ModDetectionListener;
import xyz.lorenzzzz.easydomani.detection.model.ModProfile;
import xyz.lorenzzzz.easydomani.detection.service.ModDetectionService;
import xyz.lorenzzzz.easydomani.honeypot.config.HoneypotSettings;
import xyz.lorenzzzz.easydomani.honeypot.listener.HoneypotBlockListener;
import xyz.lorenzzzz.easydomani.honeypot.packet.HoneypotBlockUpdatePacketListener;
import xyz.lorenzzzz.easydomani.honeypot.service.HoneypotBaitService;
import xyz.lorenzzzz.easydomani.message.service.MessageService;
import xyz.lorenzzzz.easydomani.packprobe.config.PackProbeSettings;
import xyz.lorenzzzz.easydomani.packprobe.listener.PackProbeStatusListener;
import xyz.lorenzzzz.easydomani.packprobe.service.PackProbeService;
import xyz.lorenzzzz.easydomani.punishment.config.PunishmentSettings;
import xyz.lorenzzzz.easydomani.punishment.listener.PunishmentPreLoginListener;
import xyz.lorenzzzz.easydomani.punishment.service.PunishmentService;
import xyz.lorenzzzz.easydomani.rotationprobe.config.RotationProbeSettings;
import xyz.lorenzzzz.easydomani.rotationprobe.listener.RotationProbeListener;
import xyz.lorenzzzz.easydomani.rotationprobe.packet.RotationProbePacketListener;
import xyz.lorenzzzz.easydomani.rotationprobe.service.AntiVanishProbeDetector;
import xyz.lorenzzzz.easydomani.rotationprobe.service.RotationProbeService;
import xyz.lorenzzzz.easydomani.seed.packet.HashedSeedPacketListener;
import xyz.lorenzzzz.easydomani.seed.service.FakeSeedService;
import xyz.lorenzzzz.easydomani.signprobe.config.SignProbeSettings;
import xyz.lorenzzzz.easydomani.signprobe.listener.SignProbeListener;
import xyz.lorenzzzz.easydomani.signprobe.packet.SignEditorPackets;
import xyz.lorenzzzz.easydomani.signprobe.packet.SignProbePacketListener;
import xyz.lorenzzzz.easydomani.signprobe.service.SignProbeService;

public final class EasyDomaniNonTiSvegli extends JavaPlugin {
    private PunishmentService punishments;
    private ModDetectionService modDetection;
    private SignProbeService signProbe;
    private PackProbeService packProbe;
    private RotationProbeService rotationProbe;
    private HoneypotBaitService honeypot;
    private FakeSeedService fakeSeeds;
    private MessageService messages;
    private ConfigDefaultsMerger defaultsMerger;
    private Executor mainExecutor;
    private volatile boolean debug;
    private PaperCommandManager commands;
    private PacketListenerCommon signPackets, rotationPackets, honeypotPackets, seedPackets;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveIfMissing("mods.yml");
        saveIfMissing("messages.yml");

        Logger logger = getLogger();
        Executor main = Bukkit.getScheduler().getMainThreadExecutor(this);
        mainExecutor = main;
        BooleanSupplier debugOn = () -> debug;
        defaultsMerger = new ConfigDefaultsMerger(logger);
        messages = new MessageService(logger);
        punishments = new PunishmentService(getDataFolder(), logger, main, messages);
        try {
            punishments.open();
        } catch (SQLException | RuntimeException e) {
            logger.log(Level.SEVERE, "Database H2 non disponibile, plugin disattivato", e);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        modDetection = new ModDetectionService(logger, messages, debugOn, punishments, p -> signProbe.startOnJoin(p));
        packProbe = new PackProbeService(getDataFolder(), logger, debugOn,
                (p, expected, key, value) -> signProbe.queuePackControls(p, expected, key, value));
        signProbe = new SignProbeService(this, logger, main, debugOn, modDetection, packProbe, SignEditorPackets.create());
        SignProbeService sign = signProbe;
        PackProbeService pack = packProbe;
        rotationProbe = new RotationProbeService(this, logger, main, debugOn, uuid -> sign.busy(uuid) || pack.isPending(uuid));
        honeypot = new HoneypotBaitService(this, logger, messages, debugOn);
        fakeSeeds = new FakeSeedService(logger, debugOn, new SecureRandom());
        reload();

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PunishmentPreLoginListener(punishments), this);
        pm.registerEvents(new ModDetectionListener(this, main, modDetection), this);
        pm.registerEvents(new SignProbeListener(signProbe), this);
        pm.registerEvents(new PackProbeStatusListener(main, packProbe), this);
        pm.registerEvents(new RotationProbeListener(rotationProbe), this);
        pm.registerEvents(new HoneypotBlockListener(honeypot), this);
        signPackets = registerPackets(new SignProbePacketListener(signProbe));
        rotationPackets = registerPackets(new RotationProbePacketListener(rotationProbe, new AntiVanishProbeDetector(logger)));
        honeypotPackets = registerPackets(new HoneypotBlockUpdatePacketListener(honeypot));
        honeypot.start();
        seedPackets = registerPackets(new HashedSeedPacketListener(fakeSeeds));
        registerCommands();
    }

    private static PacketListenerCommon registerPackets(PacketListenerCommon listener) {
        PacketEvents.getAPI().getEventManager().registerListener(listener);
        return listener;
    }

    private static void unregisterPackets(PacketListenerCommon listener) {
        if (listener != null) PacketEvents.getAPI().getEventManager().unregisterListener(listener);
    }

    @SuppressWarnings("deprecation")
    private void registerCommands() {
        commands = new PaperCommandManager(this);
        commands.enableUnstableAPI("help");
        commands.getLocales().setDefaultLocale(Locales.ITALIAN);
        commands.setHelpFormatter(new EdnHelpFormatter(commands, messages, getPluginMeta().getVersion()));
        messages.applyAcf(commands);
        commands.registerCommand(new EdnCommand(this::reload, getLogger(), mainExecutor,
                messages, punishments, modDetection, signProbe, rotationProbe, honeypot));
    }

    private void saveIfMissing(String name) {
        if (!new File(getDataFolder(), name).exists()) saveResource(name, false);
    }

    @Override
    public void onDisable() {
        unregisterPackets(seedPackets);
        seedPackets = null;
        unregisterPackets(honeypotPackets);
        honeypotPackets = null;
        if (honeypot != null) honeypot.stop();
        unregisterPackets(rotationPackets);
        rotationPackets = null;
        if (rotationProbe != null) rotationProbe.stop();
        if (packProbe != null) packProbe.stop();
        unregisterPackets(signPackets);
        signPackets = null;
        if (signProbe != null) signProbe.stop();
        if (punishments != null) punishments.close();
    }

    public void reload() {
        reloadConfig();
        FileConfiguration c = getConfig();
        defaultsMerger.addMissingDefaults(c, new File(getDataFolder(), "config.yml"));
        messages.reload(new File(getDataFolder(), "messages.yml"), getResource("messages.yml"));
        if (commands != null) messages.applyAcf(commands);
        PluginSettings global = PluginSettings.load(c);
        debug = global.debug();
        Logger logger = getLogger();
        List<ModProfile> profiles = ModProfileLoader.load(
                YamlConfiguration.loadConfiguration(new File(getDataFolder(), "mods.yml")).getConfigurationSection("profiles"),
                logger);
        punishments.reload(PunishmentSettings.load(section("punishment"), logger));
        modDetection.reload(ModDetectionSettings.load(c), profiles);
        signProbe.reload(SignProbeSettings.load(section("modDetection.probe")), profiles);
        packProbe.reload(PackProbeSettings.load(section("modDetection.packProbe"), logger));
        rotationProbe.reload(RotationProbeSettings.load(section("modDetection.rotationProbe")));
        honeypot.reload(HoneypotSettings.load(section("honeypot"), logger));
        fakeSeeds.reload(global.antiSeedCrackerEnabled());
        if (debug) logger.info("Caricati " + profiles.size() + " profili da mods.yml");
    }

    private ConfigurationSection section(String path) {
        ConfigurationSection s = getConfig().getConfigurationSection(path);
        return s != null ? s : getConfig().createSection(path);
    }
}
