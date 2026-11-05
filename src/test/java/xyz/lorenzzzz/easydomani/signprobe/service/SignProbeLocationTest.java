package xyz.lorenzzzz.easydomani.signprobe.service;

import static org.junit.jupiter.api.Assertions.*;

import org.bukkit.Material;
import org.junit.jupiter.api.Test;

class SignProbeLocationTest {
    @Test
    void soloAria() {
        assertTrue(SignProbeService.isProbeSpot(Material.AIR));
        assertTrue(SignProbeService.isProbeSpot(Material.CAVE_AIR));
        assertTrue(SignProbeService.isProbeSpot(Material.VOID_AIR));
        assertFalse(SignProbeService.isProbeSpot(Material.WATER));
        assertFalse(SignProbeService.isProbeSpot(Material.LAVA));
        assertFalse(SignProbeService.isProbeSpot(Material.OAK_SIGN));
        assertFalse(SignProbeService.isProbeSpot(Material.TORCH));
        assertFalse(SignProbeService.isProbeSpot(Material.SHORT_GRASS));
        assertFalse(SignProbeService.isProbeSpot(Material.STONE));
    }
}
