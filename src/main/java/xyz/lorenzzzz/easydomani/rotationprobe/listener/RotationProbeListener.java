package xyz.lorenzzzz.easydomani.rotationprobe.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.lorenzzzz.easydomani.rotationprobe.service.RotationProbeService;

public final class RotationProbeListener implements Listener {
    private final RotationProbeService rotation;

    public RotationProbeListener(RotationProbeService rotation) {
        this.rotation = rotation;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        rotation.scheduleJoinProbe(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        rotation.forget(event.getPlayer().getUniqueId());
    }
}
