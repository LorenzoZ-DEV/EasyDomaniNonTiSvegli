package xyz.lorenzzzz.easydomani.signprobe.service;

import java.util.Locale;
import java.util.regex.Pattern;
import xyz.lorenzzzz.easydomani.detection.model.ModProfile;
import xyz.lorenzzzz.easydomani.signprobe.model.SignProbeResult;

final class SignProbeResponseClassifier {
    private static final Pattern COLOR = Pattern.compile("(?i)&[0-9A-FK-OR]|§[0-9A-FK-OR]");

    private SignProbeResponseClassifier() {
    }

    static SignProbeResult classify(ModProfile def, String sentinel, String raw, boolean intact) {
        String response = clean(raw);
        if (response.isEmpty()) return SignProbeResult.TAMPERED;
        String norm = response.toLowerCase(Locale.ROOT);
        String key = def.probeKey().toLowerCase(Locale.ROOT);

        if (norm.equals(key) || (norm.startsWith("{") && norm.contains(key))) return SignProbeResult.NO_MATCH;
        if (sentinel != null && norm.equals(sentinel.toLowerCase(Locale.ROOT))) return SignProbeResult.NO_MATCH;
        if (def.probeKeybind()) return intact ? SignProbeResult.MATCH : SignProbeResult.TAMPERED;
        if (def.probeExpected().contains(norm)) return SignProbeResult.MATCH;
        return intact ? SignProbeResult.UNKNOWN : SignProbeResult.TAMPERED;
    }

    static boolean isIntact(String[] lines, String sentinel) {
        return lines != null && lines.length > 3 && sentinel != null && clean(lines[3]).equals(sentinel);
    }

    private static String clean(String line) {
        if (line == null) return "";
        String c = COLOR.matcher(line).replaceAll("").trim();
        if (c.length() >= 2 && c.charAt(0) == '"' && c.charAt(c.length() - 1) == '"') c = c.substring(1, c.length() - 1).trim();
        return c;
    }
}
