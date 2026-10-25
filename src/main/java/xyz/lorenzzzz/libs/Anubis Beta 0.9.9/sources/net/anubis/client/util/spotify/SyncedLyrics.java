package net.anubis.client.util.spotify;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/spotify/SyncedLyrics.class */
@Environment(EnvType.CLIENT)
public final class SyncedLyrics {
    public static final SyncedLyrics EMPTY = new SyncedLyrics(new Line[0]);
    private static final Pattern TIMESTAMP = Pattern.compile("(\\d{1,3}):(\\d{1,2})(?:[.:](\\d{1,3}))?");
    private static final Pattern WORD_TIMESTAMP = Pattern.compile("<(\\d{1,3}):(\\d{1,2})(?:[.:](\\d{1,3}))?>");
    private static final Pattern METADATA = Pattern.compile("[A-Za-z#][A-Za-z0-9_ ]*:.*", 32);
    private static final Pattern OFFSET = Pattern.compile("offset\\s*:\\s*([+-]?\\d{1,7})\\s*");
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private static final long WORD_EARLY_MS = 1000;
    private final Line[] lines;
    private final long[] times;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/spotify/SyncedLyrics$Line.class */
    @Environment(EnvType.CLIENT)
    public static final class Line extends Record {
        private final long timeMs;
        private final String text;
        private final List<Long> wordTimes;

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Line.class), Line.class, "timeMs;text;wordTimes", "FIELD:Lnet/anubis/client/util/spotify/SyncedLyrics$Line;->timeMs:J", "FIELD:Lnet/anubis/client/util/spotify/SyncedLyrics$Line;->text:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/SyncedLyrics$Line;->wordTimes:Ljava/util/List;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Line.class), Line.class, "timeMs;text;wordTimes", "FIELD:Lnet/anubis/client/util/spotify/SyncedLyrics$Line;->timeMs:J", "FIELD:Lnet/anubis/client/util/spotify/SyncedLyrics$Line;->text:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/SyncedLyrics$Line;->wordTimes:Ljava/util/List;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Line.class, Object.class), Line.class, "timeMs;text;wordTimes", "FIELD:Lnet/anubis/client/util/spotify/SyncedLyrics$Line;->timeMs:J", "FIELD:Lnet/anubis/client/util/spotify/SyncedLyrics$Line;->text:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/SyncedLyrics$Line;->wordTimes:Ljava/util/List;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public long timeMs() {
            return this.timeMs;
        }

        public String text() {
            return this.text;
        }

        public List<Long> wordTimes() {
            return this.wordTimes;
        }

        public Line(long timeMs, String text, List<Long> wordTimes) {
            String text2 = text == null ? "" : text;
            List<Long> wordTimes2 = wordTimes == null ? List.of() : List.copyOf(wordTimes);
            this.timeMs = timeMs;
            this.text = text2;
            this.wordTimes = wordTimes2;
        }

        public boolean isGap() {
            return this.text.isEmpty();
        }
    }

    private SyncedLyrics(Line[] lines) {
        this.lines = lines;
        this.times = new long[lines.length];
        for (int i = 0; i < lines.length; i++) {
            this.times[i] = lines[i].timeMs();
        }
    }

    public static SyncedLyrics parse(String lrc) {
        int close;
        if (lrc == null || lrc.isBlank()) {
            return EMPTY;
        }
        List<Line> parsed = new ArrayList<>();
        List<Long> stamps = new ArrayList<>(4);
        int offset = 0;
        for (String raw : lrc.split("\r\n|\r|\n")) {
            String line = raw.strip();
            if (!line.isEmpty() && line.charAt(0) == 65279) {
                line = line.substring(1).strip();
            }
            stamps.clear();
            int pos = 0;
            while (pos < line.length() && line.charAt(pos) == '[' && (close = line.indexOf(93, pos + 1)) >= 0) {
                String tag = line.substring(pos + 1, close).strip();
                Matcher time = TIMESTAMP.matcher(tag);
                if (time.matches()) {
                    stamps.add(Long.valueOf(toMillis(time)));
                } else {
                    if (!METADATA.matcher(tag).matches()) {
                        break;
                    }
                    Matcher shift = OFFSET.matcher(tag.toLowerCase(Locale.ROOT));
                    if (shift.matches()) {
                        offset = Integer.parseInt(shift.group(1));
                    }
                }
                pos = close + 1;
                while (pos < line.length() && line.charAt(pos) == ' ') {
                    pos++;
                }
            }
            if (!stamps.isEmpty()) {
                String body = line.substring(pos);
                String text = cleanText(body);
                long first = ((Long) Collections.min(stamps)).longValue();
                List<Long> words = wordTimes(body, text, first);
                Iterator<Long> it = stamps.iterator();
                while (it.hasNext()) {
                    long stamp = it.next().longValue();
                    parsed.add(new Line(stamp, text, shift(words, stamp - first)));
                }
            }
        }
        if (parsed.isEmpty()) {
            return EMPTY;
        }
        Line[] sorted = (Line[]) parsed.toArray(new Line[0]);
        if (offset != 0) {
            for (int i = 0; i < sorted.length; i++) {
                Line moved = sorted[i];
                sorted[i] = new Line(Math.max(0L, moved.timeMs() - ((long) offset)), moved.text(), shift(moved.wordTimes(), -offset));
            }
        }
        Arrays.sort(sorted, Comparator.comparingLong((v0) -> {
            return v0.timeMs();
        }));
        return new SyncedLyrics(sorted);
    }

    public int indexAt(long timeMs) {
        int low = 0;
        int high = this.times.length;
        while (low < high) {
            int mid = (low + high) >>> 1;
            if (this.times[mid] <= timeMs) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low - 1;
    }

    public int firstWithTimeOf(int index) {
        if (index < 0 || index >= this.times.length) {
            return index;
        }
        long time = this.times[index];
        while (index > 0 && this.times[index - 1] == time) {
            index--;
        }
        return index;
    }

    public int lastWithTimeOf(int index) {
        if (index < 0 || index >= this.times.length) {
            return index;
        }
        long time = this.times[index];
        while (index + 1 < this.times.length && this.times[index + 1] == time) {
            index++;
        }
        return index;
    }

    public Line line(int index) {
        return this.lines[index];
    }

    public int size() {
        return this.lines.length;
    }

    public boolean isEmpty() {
        return this.lines.length == 0;
    }

    public List<String> texts() {
        List<String> texts = new ArrayList<>(this.lines.length);
        for (Line line : this.lines) {
            if (!line.isGap()) {
                texts.add(line.text());
            }
        }
        return List.copyOf(texts);
    }

    private static long toMillis(Matcher time) {
        long minutes = Long.parseLong(time.group(1));
        long seconds = Long.parseLong(time.group(2));
        String fraction = time.group(3);
        long millis = 0;
        if (fraction != null) {
            millis = Long.parseLong(fraction);
            if (fraction.length() == 1) {
                millis *= 100;
            } else if (fraction.length() == 2) {
                millis *= 10;
            }
        }
        return (minutes * 60000) + (seconds * WORD_EARLY_MS) + millis;
    }

    private static String cleanText(String text) {
        if (text.indexOf(60) >= 0) {
            text = WORD_TIMESTAMP.matcher(text).replaceAll("");
        }
        return WHITESPACE.matcher(text).replaceAll(" ").strip();
    }

    private static List<Long> wordTimes(String body, String text, long lineTime) {
        if (body.indexOf(60) < 0 || text.isEmpty()) {
            return List.of();
        }
        List<Long> starts = new ArrayList<>();
        Matcher tag = WORD_TIMESTAMP.matcher(body);
        long pending = -1;
        boolean inWord = false;
        int i = 0;
        while (i < body.length()) {
            if (body.charAt(i) == '<' && tag.region(i, body.length()).lookingAt()) {
                i = tag.end();
                boolean syllable = inWord && i < body.length() && body.charAt(i) > ' ';
                if (!syllable) {
                    pending = toMillis(tag);
                }
            } else {
                int i2 = i;
                i++;
                char c = body.charAt(i2);
                if (c <= ' ') {
                    inWord = false;
                } else if (inWord) {
                    continue;
                } else {
                    inWord = true;
                    if (pending < 0) {
                        return List.of();
                    }
                    if (starts.isEmpty()) {
                        if (pending < lineTime - WORD_EARLY_MS) {
                            return List.of();
                        }
                        starts.add(Long.valueOf(pending));
                        pending = -1;
                    } else {
                        if (pending < ((Long) starts.getLast()).longValue()) {
                            return List.of();
                        }
                        starts.add(Long.valueOf(pending));
                        pending = -1;
                    }
                }
            }
        }
        int words = 1;
        for (int i3 = 0; i3 < text.length(); i3++) {
            if (text.charAt(i3) == ' ') {
                words++;
            }
        }
        return starts.size() == words ? starts : List.of();
    }

    private static List<Long> shift(List<Long> times, long by) {
        if (by == 0 || times.isEmpty()) {
            return times;
        }
        List<Long> moved = new ArrayList<>(times.size());
        Iterator<Long> it = times.iterator();
        while (it.hasNext()) {
            long time = it.next().longValue();
            moved.add(Long.valueOf(Math.max(0L, time + by)));
        }
        return moved;
    }
}
