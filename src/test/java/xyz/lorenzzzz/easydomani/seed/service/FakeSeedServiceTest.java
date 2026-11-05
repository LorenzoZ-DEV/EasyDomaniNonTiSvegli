package xyz.lorenzzzz.easydomani.seed.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;

class FakeSeedServiceTest {

    @Test
    void seedStabilePerMondo() {
        FakeSeedService s = new FakeSeedService(Logger.getAnonymousLogger(), () -> false, new Random(42));
        long world = s.fakeSeed("world");
        assertEquals(world, s.fakeSeed("world"));
        assertNotEquals(world, s.fakeSeed("world_nether"));
        assertEquals(s.fakeSeed(""), s.fakeSeed(null));
        assertFalse(s.isEnabled());
        s.reload(true);
        assertTrue(s.isEnabled());
    }
}
