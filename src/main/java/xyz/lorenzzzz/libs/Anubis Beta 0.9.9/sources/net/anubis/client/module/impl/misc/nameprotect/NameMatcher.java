package net.anubis.client.module.impl.misc.nameprotect;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/nameprotect/NameMatcher.class */
@Environment(EnvType.CLIENT)
public final class NameMatcher {
    public static final NameMatcher EMPTY = new NameMatcher(Map.of());
    public static final int MAX_NAME_LENGTH = 32;
    private final Map<String, String> mapping;
    private final String[] names;
    private final String[] aliases;
    private final int mask;
    private final long lengths;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/nameprotect/NameMatcher$Match.class */
    @Environment(EnvType.CLIENT)
    public static final class Match extends Record {
        private final int start;
        private final int end;
        private final String alias;

        public Match(int start, int end, String alias) {
            this.start = start;
            this.end = end;
            this.alias = alias;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Match.class), Match.class, "start;end;alias", "FIELD:Lnet/anubis/client/module/impl/misc/nameprotect/NameMatcher$Match;->start:I", "FIELD:Lnet/anubis/client/module/impl/misc/nameprotect/NameMatcher$Match;->end:I", "FIELD:Lnet/anubis/client/module/impl/misc/nameprotect/NameMatcher$Match;->alias:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Match.class), Match.class, "start;end;alias", "FIELD:Lnet/anubis/client/module/impl/misc/nameprotect/NameMatcher$Match;->start:I", "FIELD:Lnet/anubis/client/module/impl/misc/nameprotect/NameMatcher$Match;->end:I", "FIELD:Lnet/anubis/client/module/impl/misc/nameprotect/NameMatcher$Match;->alias:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Match.class, Object.class), Match.class, "start;end;alias", "FIELD:Lnet/anubis/client/module/impl/misc/nameprotect/NameMatcher$Match;->start:I", "FIELD:Lnet/anubis/client/module/impl/misc/nameprotect/NameMatcher$Match;->end:I", "FIELD:Lnet/anubis/client/module/impl/misc/nameprotect/NameMatcher$Match;->alias:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int start() {
            return this.start;
        }

        public int end() {
            return this.end;
        }

        public String alias() {
            return this.alias;
        }
    }

    public NameMatcher(Map<String, String> nameToAlias) {
        int slot;
        Map<String, String> unique = new HashMap<>();
        List<String> keptNames = new ArrayList<>(nameToAlias.size());
        List<String> keptAliases = new ArrayList<>(nameToAlias.size());
        for (Map.Entry<String, String> entry : nameToAlias.entrySet()) {
            String name = entry.getKey();
            String alias = entry.getValue();
            if (name != null && !name.isEmpty() && name.length() <= 32 && alias != null && unique.putIfAbsent(name.toLowerCase(Locale.ROOT), alias) == null) {
                keptNames.add(name);
                keptAliases.add(alias);
            }
        }
        int capacity = Integer.highestOneBit(Math.max(1, keptNames.size()) * 2) * 2;
        String[] tableNames = new String[capacity];
        String[] tableAliases = new String[capacity];
        long lengthBits = 0;
        for (int i = 0; i < keptNames.size(); i++) {
            String name2 = keptNames.get(i);
            int iHash = hash(name2, 0, name2.length());
            while (true) {
                slot = iHash & (capacity - 1);
                iHash = tableNames[slot] != null ? slot + 1 : iHash;
            }
            tableNames[slot] = name2;
            tableAliases[slot] = keptAliases.get(i);
            lengthBits |= 1 << name2.length();
        }
        this.mapping = Map.copyOf(unique);
        this.names = tableNames;
        this.aliases = tableAliases;
        this.mask = capacity - 1;
        this.lengths = lengthBits;
    }

    public boolean isEmpty() {
        return this.mapping.isEmpty();
    }

    public boolean sameMapping(NameMatcher other) {
        return other != null && this.mapping.equals(other.mapping);
    }

    public static boolean isNameChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || ((c >= '0' && c <= '9') || c == '_');
    }

    public static String nameCore(String raw) {
        if (raw == null) {
            return null;
        }
        int start = 0;
        int end = raw.length();
        while (start < end && !isNameChar(raw.charAt(start))) {
            start++;
        }
        while (end > start && !isNameChar(raw.charAt(end - 1))) {
            end--;
        }
        if (start == end || end - start > 32) {
            return null;
        }
        for (int i = start; i < end; i++) {
            if (!isNameChar(raw.charAt(i))) {
                return null;
            }
        }
        return raw.substring(start, end);
    }

    public String aliasAt(String text, int start, int end) {
        int length = end - start;
        if (length <= 0 || length > 32 || (this.lengths & (1 << length)) == 0) {
            return null;
        }
        int iHash = hash(text, start, end);
        int i = this.mask;
        while (true) {
            int slot = iHash & i;
            String name = this.names[slot];
            if (name == null) {
                return null;
            }
            if (name.length() == length && name.regionMatches(true, 0, text, start, length)) {
                return this.aliases[slot];
            }
            iHash = slot + 1;
            i = this.mask;
        }
    }

    public String replace(String text) {
        if (text == null || text.isEmpty() || isEmpty()) {
            return text;
        }
        List<Match> matches = scan(text, new int[]{0, text.length()}, 1);
        if (matches.isEmpty()) {
            return text;
        }
        StringBuilder out = new StringBuilder(text.length() + 16);
        int position = 0;
        for (Match match : matches) {
            out.append((CharSequence) text, position, match.start()).append(match.alias());
            position = match.end();
        }
        return out.append((CharSequence) text, position, text.length()).toString();
    }

    public boolean mentionsAny(String text) {
        return (text == null || text.isEmpty() || isEmpty() || scan(text, new int[]{0, text.length()}, 1).isEmpty()) ? false : true;
    }

    public NameMatcher newNamesSince(NameMatcher older) {
        Map<String, String> fresh = null;
        for (Map.Entry<String, String> entry : this.mapping.entrySet()) {
            if (older == null || !entry.getValue().equals(older.mapping.get(entry.getKey()))) {
                if (fresh == null) {
                    fresh = new HashMap<>();
                }
                fresh.put(entry.getKey(), entry.getValue());
            }
        }
        return fresh != null ? new NameMatcher(fresh) : EMPTY;
    }

    public List<Match> scan(String text, int[] segmentStarts, int segmentCount) {
        if (isEmpty() || text.isEmpty()) {
            return List.of();
        }
        List<Match> found = null;
        int length = text.length();
        int segment = 0;
        int i = 0;
        while (i < length) {
            while (segment < segmentCount - 1 && segmentStarts[segment + 1] <= i) {
                segment++;
            }
            int segmentEnd = segmentStarts[segment + 1];
            char c = text.charAt(i);
            if (c == 167) {
                i += i + 1 < segmentEnd ? 2 : 1;
            } else if (!isNameChar(c)) {
                i++;
            } else {
                int wordEnd = i + 1;
                while (wordEnd < length && isNameChar(text.charAt(wordEnd))) {
                    wordEnd++;
                }
                String alias = aliasAt(text, i, wordEnd);
                if (alias != null) {
                    found = add(found, i, wordEnd, alias);
                } else if (segmentEnd < wordEnd) {
                    for (int part = segment + 1; part < segmentCount && segmentStarts[part] < wordEnd; part++) {
                        int from = segmentStarts[part];
                        String endingAlias = aliasAt(text, from, wordEnd);
                        if (endingAlias != null) {
                            found = add(found, from, wordEnd, endingAlias);
                            break;
                        }
                    }
                }
                i = wordEnd;
            }
        }
        return found != null ? found : List.of();
    }

    private static List<Match> add(List<Match> found, int start, int end, String alias) {
        List<Match> list = found != null ? found : new ArrayList<>(4);
        list.add(new Match(start, end, alias));
        return list;
    }

    private static int hash(String text, int start, int end) {
        int h = 0;
        for (int i = start; i < end; i++) {
            char c = text.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                c = (char) (c + ' ');
            }
            h = (31 * h) + c;
        }
        return h ^ (h >>> 16);
    }
}
