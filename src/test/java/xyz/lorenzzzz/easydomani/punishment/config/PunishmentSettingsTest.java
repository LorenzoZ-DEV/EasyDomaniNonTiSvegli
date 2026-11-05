package xyz.lorenzzzz.easydomani.punishment.config;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

class PunishmentSettingsTest {

    private static Logger capture(List<String> out) {
        Logger l = Logger.getAnonymousLogger();
        l.setUseParentHandlers(false);
        l.addHandler(new Handler() {
            @Override
            public void publish(LogRecord r) {
                out.add(r.getMessage());
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() {
            }
        });
        return l;
    }

    @Test
    void defaultsSuSezioneVuota() {
        PunishmentSettings s = PunishmentSettings.load(new YamlConfiguration(), Logger.getAnonymousLogger());
        assertTrue(s.enabled());
        assertEquals(PunishmentSettings.DEFAULT_LADDER, s.ladder());
        assertEquals(Duration.ZERO, s.resetAfter());
        assertEquals("", s.banCommand());
        assertEquals("perm", s.banCommandPermanent());
        assertEquals("Cheat client non consentito: {mod}", s.banCommandReason());
        assertEquals("EasyDomaniNonTiSvegli", s.source());
        assertEquals(List.of(), s.postBanCommands());
    }

    @Test
    void ladderNonValidaScartataConAvviso() {
        YamlConfiguration c = new YamlConfiguration();
        c.set("ladder", List.of("30s", "0", "abc", "perm"));
        List<String> log = new ArrayList<>();
        PunishmentSettings s = PunishmentSettings.load(c, capture(log));
        assertEquals(2, s.ladder().size());
        assertEquals(Duration.ofSeconds(30), s.ladder().get(0));
        assertTrue(s.ladder().get(1).isNegative());
        assertEquals(List.of(
                "Durata ladder non valida: '0' (es. 30s 10m 12h 3d 2w perm)",
                "Durata ladder non valida: 'abc' (es. 30s 10m 12h 3d 2w perm)"), log);
    }

    @Test
    void ladderTuttaNonValidaUsaDefault() {
        YamlConfiguration c = new YamlConfiguration();
        c.set("ladder", List.of("x"));
        assertEquals(PunishmentSettings.DEFAULT_LADDER, PunishmentSettings.load(c, Logger.getAnonymousLogger()).ladder());
    }

    @Test
    void resetAfterNegativoONonValidoDiventaZero() {
        YamlConfiguration c = new YamlConfiguration();
        c.set("resetAfter", "perm");
        assertEquals(Duration.ZERO, PunishmentSettings.load(c, Logger.getAnonymousLogger()).resetAfter());
        c.set("resetAfter", "boh");
        assertEquals(Duration.ZERO, PunishmentSettings.load(c, Logger.getAnonymousLogger()).resetAfter());
        c.set("resetAfter", "30d");
        assertEquals(Duration.ofDays(30), PunishmentSettings.load(c, Logger.getAnonymousLogger()).resetAfter());
    }

    @Test
    void postBanCommandsSenzaVuoti() {
        YamlConfiguration c = new YamlConfiguration();
        c.set("postBanCommands", List.of("a", " ", "", "b"));
        assertEquals(List.of("a", "b"), PunishmentSettings.load(c, Logger.getAnonymousLogger()).postBanCommands());
    }
}
