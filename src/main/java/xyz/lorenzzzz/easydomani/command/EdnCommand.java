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
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.lorenzzzz.easydomani.EasyDomaniNonTiSvegli;
import xyz.lorenzzzz.easydomani.detection.ModDetection;
import xyz.lorenzzzz.easydomani.punishment.PunishmentManager;

/** /edn (alias easydomani): reload, info, probe, rotprobe, punish. ACF registra il comando da solo. */
@CommandAlias("edn|easydomani|easydomaninontisvegli")
@CommandPermission("edn.admin")
public final class EdnCommand extends BaseCommand {
    private static final int MAX_SHOWN_CHANNELS = 20;

    private final EasyDomaniNonTiSvegli plugin;

    public EdnCommand(EasyDomaniNonTiSvegli plugin) {
        this.plugin = plugin;
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
        plugin.reload();
        sender.sendMessage(plugin.msg("reloaded"));
    }

    @Subcommand("info")
    @CommandPermission("edn.admin")
    @CommandCompletion("@players")
    @Syntax("<giocatore>")
    @Description("Mostra brand, canali, mod, honeypot e sanzioni di un giocatore")
    public void onInfo(CommandSender sender, OnlinePlayer target) {
        Player p = target.getPlayer();
        UUID uuid = p.getUniqueId();
        List<String> channels = ModDetection.channels(p);
        List<String> mods = plugin.modDetection().detectedMods(uuid);
        String shown = channels.isEmpty() ? "-"
                : String.join(", ", channels.subList(0, Math.min(MAX_SHOWN_CHANNELS, channels.size())))
                + (channels.size() > MAX_SHOWN_CHANNELS ? ", ..." : "");
        String brand = ModDetection.brand(p), name = p.getName();
        String probe = plugin.signProbe().status(uuid), hits = String.valueOf(plugin.honeypot().hits(uuid));
        PunishmentManager pm = plugin.punishments();
        // contatore: cache (caricata al pre-login) o DB fuori dal main; la risposta torna sul main
        pm.lookup(uuid).thenAcceptAsync(entry -> {
            String[] kv = {"player", name, "brand", brand,
                    "count", String.valueOf(channels.size()), "channels", shown,
                    "mods", mods.isEmpty() ? "-" : String.join(", ", mods), "probe", probe, "hits", hits,
                    "punishments", pm.effectiveCount(entry) + " (ultima: " + entry.lastMod() + ")"};
            plugin.lines("info", kv).forEach(sender::sendMessage);
        }, plugin.mainExecutor()).exceptionally(err -> fail(sender, "info", err));
    }

    @Subcommand("probe")
    @CommandPermission("edn.admin")
    @CommandCompletion("@players")
    @Syntax("<giocatore>")
    @Description("Forza un sign probe su un giocatore")
    public void onProbe(CommandSender sender, OnlinePlayer target) {
        Player p = target.getPlayer();
        sender.sendMessage(plugin.msg(plugin.signProbe().force(p) ? "probe-sent" : "probe-failed", "player", p.getName()));
    }

    @Subcommand("rotprobe")
    @CommandPermission("edn.admin")
    @CommandCompletion("@players")
    @Syntax("<giocatore>")
    @Description("Forza un rotation probe (solo log) su un giocatore")
    public void onRotProbe(CommandSender sender, OnlinePlayer target) {
        Player p = target.getPlayer();
        String why = plugin.rotationProbe().force(p);
        sender.sendMessage(why == null ? plugin.msg("rotprobe-sent", "player", p.getName())
                : plugin.msg("rotprobe-failed", "player", p.getName(), "reason", why));
    }

    /**
     * Il giocatore puo' essere offline: niente resolver OfflinePlayer di ACF (usa
     * Bukkit.getOfflinePlayer, che puo' interrogare Mojang sul main thread), solo cache locale.
     */
    @Subcommand("punish")
    @CommandPermission("edn.admin")
    @CommandCompletion("@players reset")
    @Syntax("<giocatore> [reset]")
    @Description("Mostra o azzera le sanzioni di un giocatore")
    public void onPunish(CommandSender sender, String name, @Optional @Values("reset") String reset) {
        Player online = Bukkit.getPlayerExact(name);
        OfflinePlayer target = online != null ? online : Bukkit.getOfflinePlayerIfCached(name);
        if (target == null) {
            sender.sendMessage(plugin.msg("player-not-found", "player", name));
            return;
        }
        PunishmentManager pm = plugin.punishments();
        UUID uuid = target.getUniqueId();
        String shownName = target.getName() != null ? target.getName() : name;
        if (reset != null) {
            pm.reset(uuid)
                    .thenRunAsync(() -> sender.sendMessage(plugin.msg("punish-reset", "player", shownName)), plugin.mainExecutor())
                    .exceptionally(err -> fail(sender, "punish reset", err));
            return;
        }
        pm.lookup(uuid).thenAcceptAsync(entry -> {
            int count = pm.effectiveCount(entry);
            sender.sendMessage(plugin.msg("punish-info", "player", shownName, "count", String.valueOf(count),
                    "mod", entry.lastMod(), "next", PunishmentManager.formatDuration(pm.durationFor(count + 1))));
        }, plugin.mainExecutor()).exceptionally(err -> fail(sender, "punish", err));
    }

    /** Errore del DB: log completo in console, messaggio breve a chi ha lanciato il comando. */
    private Void fail(CommandSender sender, String what, Throwable err) {
        plugin.getLogger().log(Level.WARNING, "/edn " + what + " fallito", err);
        plugin.mainExecutor().execute(() -> sender.sendMessage(plugin.msg("db-error")));
        return null;
    }
}
