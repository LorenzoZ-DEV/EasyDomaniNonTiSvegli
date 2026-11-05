package xyz.lorenzzzz.easydomani.detection.listener;

import java.util.concurrent.Executor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.plugin.Plugin;
import xyz.lorenzzzz.easydomani.detection.service.ModDetectionService;

public final class ModDetectionListener implements Listener {
    private final Plugin plugin;
    private final Executor mainExecutor;
    private final ModDetectionService detection;

    public ModDetectionListener(Plugin plugin, Executor mainExecutor, ModDetectionService detection) {
        this.plugin = plugin;
        this.mainExecutor = mainExecutor;
        this.detection = detection;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(plugin, () -> detection.checkOnJoin(player), detection.joinDelayTicks());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRegisterChannel(PlayerRegisterChannelEvent event) {
        if (!detection.isEnabled()) return;
        Player player = event.getPlayer();
        String channel = event.getChannel();

        if (Bukkit.isPrimaryThread()) detection.checkChannel(player, channel);
        else mainExecutor.execute(() -> {
            if (player.isOnline()) detection.checkChannel(player, channel);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        detection.forget(event.getPlayer().getUniqueId());
    }
}
