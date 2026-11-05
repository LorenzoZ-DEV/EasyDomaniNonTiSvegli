package xyz.lorenzzzz.easydomani.honeypot.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.lorenzzzz.easydomani.honeypot.service.HoneypotBaitService;

public final class HoneypotBlockListener implements Listener {
    private final HoneypotBaitService honeypot;

    public HoneypotBlockListener(HoneypotBaitService honeypot) {
        this.honeypot = honeypot;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(BlockDamageEvent event) {
        honeypot.checkHit(event.getPlayer(), event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent event) {
        honeypot.checkHit(event.getPlayer(), event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        honeypot.abandonBaits(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        honeypot.forget(event.getPlayer().getUniqueId());
    }
}
