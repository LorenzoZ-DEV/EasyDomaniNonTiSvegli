package xyz.lorenzzzz.easydomani.command;

import co.aikar.commands.CommandHelp;
import co.aikar.commands.CommandHelpFormatter;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.HelpEntry;
import co.aikar.commands.PaperCommandManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.command.CommandSender;
import xyz.lorenzzzz.easydomani.message.service.MessageService;

public final class EdnHelpFormatter extends CommandHelpFormatter {

    private static final String PERMISSION = "edn.admin";

    private final MessageService messages;
    private final String version;

    public EdnHelpFormatter(PaperCommandManager manager, MessageService messages, String version) {
        super(manager);
        this.messages = messages;
        this.version = version;
    }

    private static CommandSender sender(CommandIssuer issuer) {
        return issuer.getIssuer();
    }

    private String[] pageKv(CommandHelp help) {
        return new String[]{"version", version,
                "page", String.valueOf(help.getPage()), "pages", String.valueOf(help.getTotalPages())};
    }

    @Override
    public void printHelpHeader(CommandHelp help, CommandIssuer issuer) {
        messages.lines("help.header", pageKv(help)).forEach(sender(issuer)::sendMessage);
    }

    @Override
    public void printHelpCommand(CommandHelp help, CommandIssuer issuer, HelpEntry entry) {
        String command = entry.getCommand();
        String syntax = entry.getParameterSyntax(issuer), desc = entry.getDescription();
        String[] kv = {"command", command, "syntax", syntax == null ? "" : syntax,
                "description", desc == null ? "" : desc, "permission", PERMISSION};
        Component line = messages.msg("help.command", kv);
        if (issuer.isPlayer()) {
            line = line.hoverEvent(HoverEvent.showText(messages.msg("help.hover", kv)))
                    .clickEvent(ClickEvent.suggestCommand("/" + command + " "));
        }
        sender(issuer).sendMessage(line);
    }

    @Override
    public void printHelpFooter(CommandHelp help, CommandIssuer issuer) {
        if (help.getTotalPages() <= 1) return;
        String[] kv = pageKv(help);
        CommandSender sender = sender(issuer);
        sender.sendMessage(messages.msg("help.footer", kv));
        sender.sendMessage(messages.msg("help.footer-more", kv));
    }
}
