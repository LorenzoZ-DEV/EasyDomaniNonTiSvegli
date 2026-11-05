package xyz.lorenzzzz.easydomani.detection.service;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

public final class ClientIdentityReader {
    static final int MAX_TEXT = 64;
    private static final int MAX_CHANNELS = 128;

    private ClientIdentityReader() {
    }

    public static String brand(Player player) {
        String b = sanitize(player.getClientBrandName(), MAX_TEXT);
        return b.isEmpty() ? "vanilla" : b;
    }

    public static List<String> channels(Player player) {
        List<String> out = new ArrayList<>();
        for (String ch : player.getListeningPluginChannels()) {
            if (out.size() >= MAX_CHANNELS) break;
            out.add(sanitize(ch, MAX_TEXT));
        }
        return out;
    }

    public static boolean isLikelyBedrock(Player player) {
        return player.getUniqueId().getMostSignificantBits() == 0L;
    }

    static String sanitize(String raw, int max) {
        if (raw == null) return "";
        StringBuilder sb = new StringBuilder(Math.min(raw.length(), max));
        for (int i = 0; i < raw.length() && sb.length() < max; i++) {
            char ch = raw.charAt(i);
            if (ch >= 0x20 && ch != 0x7f && ch != '§') sb.append(ch);
        }
        return sb.toString().trim();
    }
}
