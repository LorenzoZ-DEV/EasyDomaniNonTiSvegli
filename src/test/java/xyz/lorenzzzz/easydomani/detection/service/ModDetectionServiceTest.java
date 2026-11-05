package xyz.lorenzzzz.easydomani.detection.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import xyz.lorenzzzz.easydomani.message.service.MessageService;
import xyz.lorenzzzz.easydomani.punishment.config.PunishmentSettings;
import xyz.lorenzzzz.easydomani.punishment.service.PunishmentService;

class ModDetectionServiceTest {
    static final Logger LOGGER = Logger.getLogger("ModDetectionServiceTest");

    @TempDir
    File dataFolder;

    private PunishmentService punishments;

    @AfterEach
    void close() {
        if (punishments != null) punishments.close();
    }

    @Test
    void banFallitoLiberaLUuid() throws Exception {
        punishments = openPunishments(runnable -> { });
        punishments.close();
        ModDetectionService detection = detectionWith(punishments);
        UUID uuid = UUID.randomUUID();

        assertTrue(detection.punishOnce(uuid, "Tizio", "Anubis"));

        assertFalse(detection.isPunished(uuid));
        assertTrue(detection.punishOnce(uuid, "Tizio", "Anubis"));
    }

    @Test
    void banInCorsoTieneLUuid() throws Exception {
        punishments = openPunishments(runnable -> { });
        ModDetectionService detection = detectionWith(punishments);
        UUID uuid = UUID.randomUUID();

        assertTrue(detection.punishOnce(uuid, "Tizio", "Anubis"));

        assertTrue(detection.isPunished(uuid));
        assertFalse(detection.punishOnce(uuid, "Tizio", "Anubis"));
    }

    private PunishmentService openPunishments(Executor mainExecutor) throws Exception {
        PunishmentService service = new PunishmentService(dataFolder, LOGGER, mainExecutor, new MessageService(LOGGER));
        service.open();
        service.reload(new PunishmentSettings(true, PunishmentSettings.DEFAULT_LADDER, Duration.ZERO, "", "perm",
                "motivo", "test", List.of()));
        return service;
    }

    private static ModDetectionService detectionWith(PunishmentService punishments) {
        return new ModDetectionService(LOGGER, new MessageService(LOGGER), () -> false, punishments, player -> { });
    }
}
