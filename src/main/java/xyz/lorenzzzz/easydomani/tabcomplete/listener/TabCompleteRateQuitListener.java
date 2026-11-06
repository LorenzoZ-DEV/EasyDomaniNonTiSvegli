package xyz.lorenzzzz.easydomani.tabcomplete.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.lorenzzzz.easydomani.rotationprobe.service.AntiVanishProbeDetector;
import xyz.lorenzzzz.easydomani.tabcomplete.service.TabCompleteRateService;

public final class TabCompleteRateQuitListener implements Listener {
    private final TabCompleteRateService rate;
    private final AntiVanishProbeDetector antiVanish;

    public TabCompleteRateQuitListener(TabCompleteRateService rate, AntiVanishProbeDetector antiVanish) {
        this.rate = rate;
        this.antiVanish = antiVanish;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        rate.forget(event.getPlayer().getUniqueId());
        antiVanish.forget(event.getPlayer().getUniqueId());
    }
}
