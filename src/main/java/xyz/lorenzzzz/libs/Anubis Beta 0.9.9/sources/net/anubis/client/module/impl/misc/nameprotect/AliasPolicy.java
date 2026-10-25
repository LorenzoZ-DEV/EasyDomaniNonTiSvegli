package net.anubis.client.module.impl.misc.nameprotect;

import imgui.flag.ImGuiCol;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/nameprotect/AliasPolicy.class */
@Environment(EnvType.CLIENT)
public final class AliasPolicy {
    public static final int MAX_LENGTH = 16;
    private static final int MIN_GLUED_NAME = 4;
    private static final String[] STAFF_FRAGMENTS = skeletons("admin", "owner", "moderat", "staff", "helper", "founder", "manager", "develop", "console", "support", "operator", "official", "donutsmp");
    private static final Set<String> STAFF_WORDS = Set.of((Object[]) skeletons("mod", "mods", "srmod", "jrmod", "trialmod", "headmod", "dev", "devs", "op", "ops", "server", "system", "builder"));

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/nameprotect/AliasPolicy$Refusal.class */
    @Environment(EnvType.CLIENT)
    public enum Refusal {
        STAFF_TITLE("it reads like a staff or server title"),
        PLAYER_NAME("it is the name of a player seen on this server");

        private final String reason;

        Refusal(String reason) {
            this.reason = reason;
        }

        public String reason() {
            return this.reason;
        }
    }

    private AliasPolicy() {
    }

    public static String sanitize(String raw) {
        if (raw == null) {
            return "";
        }
        StringBuilder clean = new StringBuilder(Math.min(raw.length(), 16));
        int i = 0;
        while (i < raw.length() && clean.length() < 16) {
            char c = raw.charAt(i);
            if (c == 167) {
                i++;
            } else if (NameMatcher.isNameChar(c)) {
                clean.append(c);
            } else if (clean.length() > 0 && clean.charAt(clean.length() - 1) != ' ') {
                clean.append(' ');
            }
            i++;
        }
        return clean.toString().trim();
    }

    public static String skeleton(CharSequence text) {
        StringBuilder out = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char c = Character.toLowerCase(text.charAt(i));
            switch (c) {
                case ' ':
                case '_':
                    break;
                case '0':
                    out.append('o');
                    break;
                case ImGuiCol.TableRowBg /* 49 */:
                case 'i':
                    out.append('l');
                    break;
                case ImGuiCol.TextSelectedBg /* 51 */:
                    out.append('e');
                    break;
                case ImGuiCol.DragDropTarget /* 52 */:
                    out.append('a');
                    break;
                case ImGuiCol.NavHighlight /* 53 */:
                    out.append('s');
                    break;
                case '7':
                    out.append('t');
                    break;
                default:
                    out.append(c);
                    break;
            }
        }
        return out.toString();
    }

    public static Refusal check(String alias, Predicate<String> taken) {
        String compact = skeleton(alias);
        for (String fragment : STAFF_FRAGMENTS) {
            if (compact.contains(fragment)) {
                return Refusal.STAFF_TITLE;
            }
        }
        if (STAFF_WORDS.contains(compact)) {
            return Refusal.STAFF_TITLE;
        }
        if (taken.test(compact)) {
            return Refusal.PLAYER_NAME;
        }
        for (int length = 4; length < compact.length(); length++) {
            if (taken.test(compact.substring(0, length)) || taken.test(compact.substring(compact.length() - length))) {
                return Refusal.PLAYER_NAME;
            }
        }
        for (String word : alias.split("[ _]+")) {
            if (!word.isEmpty()) {
                String wordSkeleton = skeleton(word);
                if (STAFF_WORDS.contains(wordSkeleton)) {
                    return Refusal.STAFF_TITLE;
                }
                if (taken.test(wordSkeleton)) {
                    return Refusal.PLAYER_NAME;
                }
            }
        }
        return null;
    }

    private static String[] skeletons(String... words) {
        String[] out = new String[words.length];
        for (int i = 0; i < words.length; i++) {
            out[i] = skeleton(words[i].toLowerCase(Locale.ROOT));
        }
        return out;
    }
}
