package xyz.lorenzzzz.easydomani.detection.service;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Proxy;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

class ClientIdentityReaderTest {
    @Test
    void sanitizeBrand() {
        assertEquals("cfabric", ClientIdentityReader.sanitize("§cfab\u0000ric", 64));
        assertEquals(64, ClientIdentityReader.sanitize("x".repeat(500), 64).length());
        assertEquals("", ClientIdentityReader.sanitize(null, 64));
    }

    @Test
    void bedrockSoloDaUuidFloodgate() {
        UUID floodgateUuid = new UUID(0L, 0x1234L);
        UUID javaUuid = UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5");
        assertTrue(ClientIdentityReader.isLikelyBedrock(playerWith(floodgateUuid, "Geyser")));
        assertTrue(ClientIdentityReader.isLikelyBedrock(playerWith(floodgateUuid, "vanilla")));
        assertFalse(ClientIdentityReader.isLikelyBedrock(playerWith(javaUuid, "Geyser")));
        assertFalse(ClientIdentityReader.isLikelyBedrock(playerWith(javaUuid, "geyser")));
        assertFalse(ClientIdentityReader.isLikelyBedrock(playerWith(javaUuid, "fabric")));
    }

    private static Player playerWith(UUID uuid, String brand) {
        return (Player) Proxy.newProxyInstance(Player.class.getClassLoader(), new Class<?>[]{Player.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "getUniqueId" -> uuid;
                    case "getClientBrandName" -> brand;
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }
}
