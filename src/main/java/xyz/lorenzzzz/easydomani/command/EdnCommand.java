package xyz.lorenzzzz.easydomani.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.annotation.Values;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.lorenzzzz.easydomani.detection.service.ClientIdentityReader;
import xyz.lorenzzzz.easydomani.detection.service.ModDetectionService;
import xyz.lorenzzzz.easydomani.honeypot.service.HoneypotBaitService;
import xyz.lorenzzzz.easydomani.message.service.MessageService;
import xyz.lorenzzzz.easydomani.punishment.service.PunishmentDurations;
import xyz.lorenzzzz.easydomani.punishment.service.PunishmentService;
import xyz.lorenzzzz.easydomani.rotationprobe.service.RotationProbeService;
import xyz.lorenzzzz.easydomani.signprobe.service.SignProbeService;

@CommandAlias("edn|easydomani|easydomaninontisvegli")
@CommandPermission("edn.admin")
public final class EdnCommand extends BaseCommand {
    private static final int MAX_SHOWN_CHANNELS = 20;

    private final Runnable reload;
    private final Logger logger;
    private final Executor mainExecutor;
    private final MessageService messages;
    private final PunishmentService punishments;
    private final ModDetectionService modDetection;
    private final SignProbeService signProbe;
    private final RotationProbeService rotationProbe;
    private final HoneypotBaitService honeypot;

    public EdnCommand(Runnable reload, Logger logger, Executor mainExecutor, MessageService messages,
                      PunishmentService punishments, ModDetectionService modDetection, SignProbeService signProbe,
                      RotationProbeService rotationProbe, HoneypotBaitService honeypot) {
        this.reload = reload;
        this.logger = logger;
        this.mainExecutor = mainExecutor;
        this.messages = messages;
        this.punishments = punishments;
        this.modDetection = modDetection;
        this.signProbe = signProbe;
        this.rotationProbe = rotationProbe;
        this.honeypot = honeypot;
    }

    @Default
    @HelpCommand
    @Syntax("[pagina]")
    @Description("Mostra la lista dei comandi")
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("reload")
    @CommandPermission("edn.admin")
    @Description("Ricarica config.yml e mods.yml")
    public void onReload(CommandSender sender) {
        reload.run();
        sender.sendMessage(messages.msg("reloaded"));
    }

    @Subcommand("info")
    @CommandPermission("edn.admin")
    @CommandCompletion("@players")
    @Syntax("<giocatore>")
    @Description("Mostra brand, canali, mod, honeypot e sanzioni di un giocatore")
    public void onInfo(CommandSender sender, OnlinePlayer target) {
        Player p = target.getPlayer();
        UUID uuid = p.getUniqueId();
        List<String> channels = ClientIdentityReader.channels(p);
        List<String> mods = modDetection.detectedMods(uuid);
        String shown = channels.isEmpty() ? "-"
                : String.join(", ", channels.subList(0, Math.min(MAX_SHOWN_CHANNELS, channels.size())))
                + (channels.size() > MAX_SHOWN_CHANNELS ? ", ..." : "");
        String brand = ClientIdentityReader.brand(p), name = p.getName();
        String probe = signProbe.status(uuid), hits = String.valueOf(honeypot.hits(uuid));
        PunishmentService pm = punishments;

        pm.lookup(uuid).thenAcceptAsync(entry -> {
            String[] kv = {"player", name, "brand", brand,
                    "count", String.valueOf(channels.size()), "channels", shown,
                    "mods", mods.isEmpty() ? "-" : String.join(", ", mods), "probe", probe, "hits", hits,
                    "punishments", pm.effectiveCount(entry) + " (ultima: " + entry.lastMod() + ")"};
            messages.lines("info", kv).forEach(sender::sendMessage);
        }, mainExecutor).exceptionally(err -> fail(sender, "info", err));
    }

    @Subcommand("probe")
    @CommandPermission("edn.admin")
    @CommandCompletion("@players")
    @Syntax("<giocatore>")
    @Description("Forza un sign probe su un giocatore")
    public void onProbe(CommandSender sender, OnlinePlayer target) {
        Player p = target.getPlayer();
        sender.sendMessage(messages.msg(signProbe.force(p) ? "probe-sent" : "probe-failed", "player", p.getName()));
    }

    @Subcommand("rotprobe")
    @CommandPermission("edn.admin")
    @CommandCompletion("@players")
    @Syntax("<giocatore>")
    @Description("Forza un rotation probe (solo log) su un giocatore")
    public void onRotProbe(CommandSender sender, OnlinePlayer target) {
        Player p = target.getPlayer();
        String why = rotationProbe.force(p);
        sender.sendMessage(why == null ? messages.msg("rotprobe-sent", "player", p.getName())
                : messages.msg("rotprobe-failed", "player", p.getName(), "reason", why));
    }

    @Subcommand("punish")
    @CommandPermission("edn.admin")
    @CommandCompletion("@players reset")
    @Syntax("<giocatore> [reset]")
    @Description("Mostra o azzera le sanzioni di un giocatore")
    public void onPunish(CommandSender sender, String name, @Optional @Values("reset") String reset) {
        Player online = Bukkit.getPlayerExact(name);
        OfflinePlayer target = online != null ? online : Bukkit.getOfflinePlayerIfCached(name);
        if (target == null) {
            sender.sendMessage(messages.msg("player-not-found", "player", name));
            return;
        }
        PunishmentService pm = punishments;
        UUID uuid = target.getUniqueId();
        String shownName = target.getName() != null ? target.getName() : name;
        if (reset != null) {
            pm.reset(uuid)
                    .thenRunAsync(() -> sender.sendMessage(messages.msg("punish-reset", "player", shownName)), mainExecutor)
                    .exceptionally(err -> fail(sender, "punish reset", err));
            return;
        }
        pm.lookup(uuid).thenAcceptAsync(entry -> {
            int count = pm.effectiveCount(entry);
            sender.sendMessage(messages.msg("punish-info", "player", shownName, "count", String.valueOf(count),
                    "mod", entry.lastMod(), "next", PunishmentDurations.formatDuration(pm.durationFor(count + 1))));
        }, mainExecutor).exceptionally(err -> fail(sender, "punish", err));
    }

    private Void fail(CommandSender sender, String what, Throwable err) {
        logger.log(Level.WARNING, "/edn " + what + " fallito", err);
        mainExecutor.execute(() -> sender.sendMessage(messages.msg("db-error")));
        return null;
    }
}
