package net.anubis.client.util.antivanish;

import imgui.extension.imguizmo.flag.Operation;
import imgui.flag.ImGuiKey;
import java.text.Normalizer;
import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/antivanish/AntiVanishText.class */
@Environment(EnvType.CLIENT)
public final class AntiVanishText {
    private AntiVanishText() {
    }

    public static String normalize(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }
        String decomposed = Normalizer.normalize(stripLegacyCodes(text), Normalizer.Form.NFKD).toLowerCase(Locale.ROOT);
        StringBuilder out = new StringBuilder(decomposed.length());
        boolean spaced = true;
        int offset = 0;
        while (offset < decomposed.length()) {
            int codePoint = decomposed.codePointAt(offset);
            offset += Character.charCount(codePoint);
            if (!isMark(codePoint) && !isFormatCode(codePoint)) {
                int folded = foldConfusable(codePoint);
                if ((folded >= 97 && folded <= 122) || (folded >= 48 && folded <= 57)) {
                    out.appendCodePoint(folded);
                    spaced = false;
                } else if (!spaced) {
                    out.append(' ');
                    spaced = true;
                }
            }
        }
        int length = out.length();
        if (length > 0 && out.charAt(length - 1) == ' ') {
            out.setLength(length - 1);
        }
        return out.toString();
    }

    public static boolean containsPlayerName(String displayedName, String playerName) {
        int match;
        String rawDisplay = stripLegacyCodes(displayedName == null ? "" : displayedName).toLowerCase(Locale.ROOT);
        String rawPlayer = stripLegacyCodes(playerName == null ? "" : playerName).trim().toLowerCase(Locale.ROOT);
        if (rawPlayer.matches("[a-z0-9_]{1,16}")) {
            int i = 0;
            while (true) {
                int from = i;
                if (from > rawDisplay.length() - rawPlayer.length() || (match = rawDisplay.indexOf(rawPlayer, from)) < 0) {
                    return false;
                }
                int end = match + rawPlayer.length();
                boolean leftBoundary = match == 0 || !isUsernameCharacter(rawDisplay.charAt(match - 1));
                boolean rightBoundary = end == rawDisplay.length() || !isUsernameCharacter(rawDisplay.charAt(end));
                if (leftBoundary && rightBoundary) {
                    return true;
                }
                i = match + 1;
            }
        } else {
            String displayed = normalize(displayedName);
            String player = normalize(playerName);
            if (displayed.isBlank() || player.isBlank()) {
                return false;
            }
            return displayed.equals(player) || (" " + displayed + " ").contains(" " + player + " ");
        }
    }

    public static boolean looksLikeLeaveMessage(String message, String playerName) {
        if (!containsPlayerName(message, playerName)) {
            return false;
        }
        String text = " " + normalize(message) + " ";
        String player = normalize(playerName);
        if (player.isBlank()) {
            return false;
        }
        String prefix = " " + player + " ";
        return text.contains(prefix + "left the game ") || text.contains(prefix + "left ") || text.contains(prefix + "has left ") || text.contains(prefix + "quit ") || text.contains(prefix + "has quit ") || text.contains(prefix + "disconnected ") || text.contains(prefix + "logged out ");
    }

    public static boolean isPlausiblePlayerName(String name) {
        String trimmed;
        int len;
        return name != null && (len = (trimmed = name.trim()).length()) >= 3 && len <= 16 && trimmed.indexOf(32) < 0;
    }

    public static boolean isUsername(String name) {
        int len;
        if (name == null || (len = name.length()) < 2 || len > 32) {
            return false;
        }
        boolean hasAlnum = false;
        for (int i = 0; i < len; i++) {
            char c = name.charAt(i);
            boolean alnum = (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9');
            if (alnum) {
                hasAlnum = true;
            } else if (c != '_' && c != '.' && c != ' ') {
                return false;
            }
        }
        return hasAlnum;
    }

    private static boolean isUsernameCharacter(char character) {
        return (character >= 'a' && character <= 'z') || (character >= '0' && character <= '9') || character == '_';
    }

    private static boolean isMark(int codePoint) {
        int type = Character.getType(codePoint);
        return type == 6 || type == 8 || type == 7;
    }

    private static boolean isFormatCode(int codePoint) {
        return codePoint == 167 || codePoint == 8203 || codePoint == 8204 || codePoint == 8205 || codePoint == 65279;
    }

    /* JADX WARN: Code duplicated, block: B:18:0x0057  */
    private static String stripLegacyCodes(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        StringBuilder out = new StringBuilder(text.length());
        int i = 0;
        while (i < text.length()) {
            char c = text.charAt(i);
            if (c == 167 && i + 1 < text.length()) {
                char code = Character.toLowerCase(text.charAt(i + 1));
                if ("0123456789abcdefklmnorx".indexOf(code) >= 0) {
                    i++;
                } else {
                    out.append(c);
                }
            } else {
                out.append(c);
            }
            i++;
        }
        return out.toString();
    }

    private static int foldConfusable(int codePoint) {
        switch (codePoint) {
            case ImGuiKey.F22 /* 593 */:
            case 945:
            case 1072:
            case 7424:
                return 97;
            case ImGuiKey.NumLock /* 609 */:
            case ImGuiKey.PrintScreen /* 610 */:
                return 103;
            case ImGuiKey.Keypad6 /* 618 */:
            case 953:
            case 1110:
                return 105;
            case ImGuiKey.KeypadEqual /* 628 */:
            case 1400:
                return 110;
            case 640:
                return 114;
            case ImGuiKey.MouseLeft /* 655 */:
            case 1091:
                return 121;
            case ImGuiKey.ReservedForModSuper /* 665 */:
            case 946:
            case 1074:
                return 98;
            case 668:
            case 1085:
                return 104;
            case 671:
                return 108;
            case 949:
            case 1077:
            case 7431:
                return 101;
            case 954:
            case 1082:
            case 7435:
                return 107;
            case 957:
            case 7456:
                return 118;
            case 959:
            case 1086:
            case 7439:
                return 111;
            case 961:
            case 1088:
            case 7448:
                return 112;
            case 964:
            case 1090:
            case 7451:
                return 116;
            case 965:
            case 7452:
                return 117;
            case 1010:
            case 1089:
            case 7428:
                return 99;
            case 1084:
            case 7437:
                return 109;
            case 1093:
                return Operation.ROTATE;
            case 1109:
            case 42801:
                return 115;
            case 1112:
            case 7434:
                return 106;
            case 1121:
            case 7457:
                return 119;
            case 1281:
            case 7429:
                return 100;
            case 1307:
                return 113;
            case 7458:
            case 7459:
                return 122;
            case 42800:
                return 102;
            default:
                return codePoint;
        }
    }
}
