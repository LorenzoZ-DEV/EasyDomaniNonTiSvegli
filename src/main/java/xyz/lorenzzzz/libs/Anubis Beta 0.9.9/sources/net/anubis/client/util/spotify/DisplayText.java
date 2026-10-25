package net.anubis.client.util.spotify;

import imgui.flag.ImDrawFlags;
import java.text.Normalizer;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;
import net.anubis.client.util.seedscan.SeedWorldgen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/spotify/DisplayText.class */
@Environment(EnvType.CLIENT)
public final class DisplayText {
    private DisplayText() {
    }

    public static String fold(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        StringBuilder out = null;
        boolean space = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            String replacement = replacement(c);
            if (replacement == null && (c != ' ' || !space)) {
                if (out != null) {
                    out.append(c);
                }
                space = c == ' ';
            } else {
                if (out == null) {
                    out = new StringBuilder(text.length()).append((CharSequence) text, 0, i);
                }
                if (replacement != null) {
                    for (int k = 0; k < replacement.length(); k++) {
                        char r = replacement.charAt(k);
                        if (r != ' ' || (!space && !out.isEmpty())) {
                            out.append(r);
                            space = r == ' ';
                        }
                    }
                }
            }
        }
        if (out == null) {
            String stripped = text.strip();
            return stripped.length() == text.length() ? text : stripped;
        }
        return out.toString().strip();
    }

    public static String fold(String text, IntPredicate drawable) {
        String folded = fold(text);
        StringBuilder out = null;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= folded.length()) {
                break;
            }
            int cp = folded.codePointAt(i2);
            int next = i2 + Character.charCount(cp);
            String base = (cp < 128 || !Character.isLetter(cp) || drawable.test(cp)) ? null : baseLetters(cp);
            if (base != null && !base.codePoints().allMatch(b -> {
                return b < 128 || drawable.test(b);
            })) {
                base = null;
            }
            if (base != null && out == null) {
                out = new StringBuilder(folded.length()).append((CharSequence) folded, 0, i2);
            }
            if (out != null) {
                if (base != null) {
                    out.append(base);
                } else {
                    out.append((CharSequence) folded, i2, next);
                }
            }
            i = next;
        }
        return out == null ? folded : out.toString();
    }

    static String baseLetters(int cp) {
        String str;
        switch (cp) {
            case 272:
                str = "D";
                break;
            case 273:
                str = "d";
                break;
            case 294:
                str = "H";
                break;
            case 295:
                str = "h";
                break;
            case 306:
                str = "IJ";
                break;
            case 307:
                str = "ij";
                break;
            case SeedWorldgen.MAX_Y /* 319 */:
                str = "L";
                break;
            case 320:
                str = "l";
                break;
            case 321:
                str = "L";
                break;
            case 322:
                str = "l";
                break;
            case 338:
                str = "OE";
                break;
            case 339:
                str = "oe";
                break;
            case 358:
                str = "T";
                break;
            case 359:
                str = "t";
                break;
            case 383:
                str = "s";
                break;
            default:
                str = null;
                break;
        }
        String special = str;
        if (special != null) {
            return special;
        }
        String decomposed = Normalizer.normalize(new String(Character.toChars(cp)), Normalizer.Form.NFD);
        StringBuilder base = new StringBuilder(decomposed.length());
        IntStream intStreamFilter = decomposed.codePoints().filter(c -> {
            return Character.getType(c) != 6;
        });
        Objects.requireNonNull(base);
        intStreamFilter.forEach(base::appendCodePoint);
        if (base.isEmpty() || base.length() == decomposed.length()) {
            return null;
        }
        return base.toString();
    }

    private static String replacement(char c) {
        switch (c) {
            case '\t':
            case ImDrawFlags.RoundCornersRight /* 160 */:
            case 8192:
            case 8193:
            case 8194:
            case 8195:
            case 8196:
            case 8197:
            case 8198:
            case 8199:
            case 8200:
            case 8201:
            case 8202:
            case 8239:
            case 8287:
            case 12288:
                return " ";
            case 8203:
            case 8204:
            case 8205:
            case 8288:
            case 9833:
            case 9834:
            case 9835:
            case 9836:
            case 65279:
                return "";
            case 8208:
            case 8209:
            case 8210:
            case 8211:
            case 8212:
            case 8213:
            case 8722:
                return "-";
            case 8216:
            case 8217:
            case 8218:
            case 8219:
            case 8242:
                return "'";
            case 8220:
            case 8221:
            case 8222:
            case 8223:
            case 8243:
                return "\"";
            case 8226:
            case 8231:
            case 8729:
                return "·";
            case 8230:
                return "...";
            default:
                return null;
        }
    }
}
