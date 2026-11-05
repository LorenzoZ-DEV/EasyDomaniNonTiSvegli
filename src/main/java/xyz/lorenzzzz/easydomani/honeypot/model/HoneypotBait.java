package xyz.lorenzzzz.easydomani.honeypot.model;

import org.bukkit.Material;

public record HoneypotBait(int x, int y, int z, long expiresAt, Material material, int stateId) {
}
