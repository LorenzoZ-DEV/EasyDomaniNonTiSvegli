package xyz.lorenzzzz.easydomani.signprobe.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.lorenzzzz.easydomani.signprobe.service.SignProbeService;

public final class SignProbeListener implements Listener {
    private final SignProbeService signProbe;

    public SignProbeListener(SignProbeService signProbe) {
        this.signProbe = signProbe;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        signProbe.forget(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        signProbe.abandonActive(event.getPlayer());
    }
}
