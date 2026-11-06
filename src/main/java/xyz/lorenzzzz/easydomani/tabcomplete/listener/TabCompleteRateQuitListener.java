package xyz.lorenzzzz.easydomani.tabcomplete.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.lorenzzzz.easydomani.tabcomplete.service.TabCompleteRateService;

public final class TabCompleteRateQuitListener implements Listener {
    private final TabCompleteRateService rate;

    public TabCompleteRateQuitListener(TabCompleteRateService rate) {
        this.rate = rate;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        rate.forget(event.getPlayer().getUniqueId());
    }
}
