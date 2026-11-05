package xyz.lorenzzzz.easydomani.packprobe.listener;

import java.util.concurrent.Executor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import xyz.lorenzzzz.easydomani.packprobe.service.PackProbeService;

public final class PackProbeStatusListener implements Listener {
    private final Executor mainExecutor;
    private final PackProbeService pack;

    public PackProbeStatusListener(Executor mainExecutor, PackProbeService pack) {
        this.mainExecutor = mainExecutor;
        this.pack = pack;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        pack.forget(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onStatus(PlayerResourcePackStatusEvent event) {
        if (!pack.ownsPack(event.getID())) return;
        Player player = event.getPlayer();
        PlayerResourcePackStatusEvent.Status status = event.getStatus();
        if (Bukkit.isPrimaryThread()) pack.handleStatus(player, status);
        else mainExecutor.execute(() -> pack.handleStatus(player, status));
    }
}
