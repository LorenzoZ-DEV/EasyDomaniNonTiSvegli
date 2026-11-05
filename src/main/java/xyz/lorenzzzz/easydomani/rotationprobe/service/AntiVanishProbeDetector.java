package xyz.lorenzzzz.easydomani.rotationprobe.service;

import java.util.logging.Logger;

public final class AntiVanishProbeDetector {
    static final String ANTIVANISH_TEXT = "minecraft:msg ";
    static final int ANTIVANISH_MIN_ID = 30000;

    private final Logger logger;

    public AntiVanishProbeDetector(Logger logger) {
        this.logger = logger;
    }

    public void inspect(String name, int id, String text) {
        if (text == null || !isAntiVanishProbe(id, text)) return;
        String shown = text.length() > 100 ? text.substring(0, 100) : text;
        logger.info("[AntiVanish] " + name + " id=" + id + " text='" + shown.replaceAll("[\\p{Cntrl}§]", "") + "'");
    }

    static boolean isAntiVanishProbe(int id, String text) {
        return ANTIVANISH_TEXT.equals(text) || id >= ANTIVANISH_MIN_ID;
    }
}
