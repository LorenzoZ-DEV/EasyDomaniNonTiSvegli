package xyz.lorenzzzz.easydomani.punishment.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import xyz.lorenzzzz.easydomani.punishment.service.PunishmentService;

public final class PunishmentPreLoginListener implements Listener {
    private final PunishmentService punishments;

    public PunishmentPreLoginListener(PunishmentService punishments) {
        this.punishments = punishments;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;
        punishments.preload(event.getUniqueId(), event.getName());
    }
}
