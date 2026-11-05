package xyz.lorenzzzz.easydomani.packprobe.service;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface SignProbeControlQueue {
    boolean queue(Player player, String expected, String overrideKey, String overrideValue);
}
