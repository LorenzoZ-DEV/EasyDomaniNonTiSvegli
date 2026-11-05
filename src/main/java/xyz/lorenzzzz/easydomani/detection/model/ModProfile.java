package xyz.lorenzzzz.easydomani.detection.model;

import java.util.List;
import java.util.Locale;

public record ModProfile(String id, String name, boolean ban,
                         List<String> brandExact, List<String> brandContains, List<String> channelPrefixes,
                         String probeKey, List<String> probeExpected, boolean probeBypass, boolean probeKeybind) {

    public boolean hasProbe() {
        return probeKey != null && (probeKeybind || !probeExpected.isEmpty());
    }

    public boolean matchesBrand(String brand) {
        if (brand == null || brand.isBlank()) return false;
        String b = brand.trim();
        for (String e : brandExact) if (b.equalsIgnoreCase(e)) return true;
        if (brandContains.isEmpty()) return false;
        String low = b.toLowerCase(Locale.ROOT);
        for (String c : brandContains) if (low.contains(c)) return true;
        return false;
    }

    public boolean matchesChannel(String channel) {
        if (channel == null || channel.isEmpty()) return false;
        for (String p : channelPrefixes) if (channel.regionMatches(true, 0, p, 0, p.length())) return true;
        return false;
    }
}
