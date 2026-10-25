package net.anubis.client.util.spotify;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/spotify/NowPlaying.class */
@Environment(EnvType.CLIENT)
public final class NowPlaying extends Record {
    private final Status status;
    private final String title;
    private final String artist;
    private final String album;
    private final Source source;
    private final long durationMs;
    private final long positionMs;
    private final long sampledAtMs;
    private final double rate;
    private final AlbumArt art;
    private static final String SEPARATOR = " - ";
    static final long LYRICS_MIN_LENGTH_MS = 30000;
    static final long LYRICS_MAX_LENGTH_MS = 3600000;
    public static final NowPlaying UNSUPPORTED = idle(Status.UNSUPPORTED);
    public static final NowPlaying WAITING = idle(Status.WAITING);
    public static final NowPlaying NOT_RUNNING = idle(Status.NOT_RUNNING);

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/spotify/NowPlaying$Source.class */
    @Environment(EnvType.CLIENT)
    public enum Source {
        NONE,
        WINDOW,
        SESSION
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/spotify/NowPlaying$Status.class */
    @Environment(EnvType.CLIENT)
    public enum Status {
        UNSUPPORTED,
        WAITING,
        NOT_RUNNING,
        PAUSED,
        PLAYING
    }

    @Override // java.lang.Record
    public final String toString() {
        return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, NowPlaying.class), NowPlaying.class, "status;title;artist;album;source;durationMs;positionMs;sampledAtMs;rate;art", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->status:Lnet/anubis/client/util/spotify/NowPlaying$Status;", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->title:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->artist:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->album:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->source:Lnet/anubis/client/util/spotify/NowPlaying$Source;", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->durationMs:J", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->positionMs:J", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->sampledAtMs:J", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->rate:D", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->art:Lnet/anubis/client/util/spotify/AlbumArt;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, NowPlaying.class), NowPlaying.class, "status;title;artist;album;source;durationMs;positionMs;sampledAtMs;rate;art", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->status:Lnet/anubis/client/util/spotify/NowPlaying$Status;", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->title:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->artist:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->album:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->source:Lnet/anubis/client/util/spotify/NowPlaying$Source;", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->durationMs:J", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->positionMs:J", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->sampledAtMs:J", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->rate:D", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->art:Lnet/anubis/client/util/spotify/AlbumArt;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final boolean equals(Object o) {
        return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, NowPlaying.class, Object.class), NowPlaying.class, "status;title;artist;album;source;durationMs;positionMs;sampledAtMs;rate;art", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->status:Lnet/anubis/client/util/spotify/NowPlaying$Status;", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->title:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->artist:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->album:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->source:Lnet/anubis/client/util/spotify/NowPlaying$Source;", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->durationMs:J", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->positionMs:J", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->sampledAtMs:J", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->rate:D", "FIELD:Lnet/anubis/client/util/spotify/NowPlaying;->art:Lnet/anubis/client/util/spotify/AlbumArt;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
    }

    public Status status() {
        return this.status;
    }

    public String title() {
        return this.title;
    }

    public String artist() {
        return this.artist;
    }

    public String album() {
        return this.album;
    }

    public Source source() {
        return this.source;
    }

    public long durationMs() {
        return this.durationMs;
    }

    public long positionMs() {
        return this.positionMs;
    }

    public long sampledAtMs() {
        return this.sampledAtMs;
    }

    public double rate() {
        return this.rate;
    }

    public AlbumArt art() {
        return this.art;
    }

    public NowPlaying(Status status, String title, String artist, String album, Source source, long durationMs, long positionMs, long sampledAtMs, double rate, AlbumArt art) {
        status = status == null ? Status.NOT_RUNNING : status;
        source = source == null ? Source.NONE : source;
        String title2 = title == null ? "" : title;
        String artist2 = artist == null ? "" : artist;
        String album2 = album == null ? "" : album;
        long durationMs2 = Math.max(0L, durationMs);
        long positionMs2 = durationMs2 > 0 ? Math.max(0L, Math.min(positionMs, durationMs2)) : Math.max(0L, positionMs);
        rate = (rate <= 0.0d || rate > 16.0d) ? 1.0d : rate;
        this.status = status;
        this.title = title2;
        this.artist = artist2;
        this.album = album2;
        this.source = source;
        this.durationMs = durationMs2;
        this.positionMs = positionMs2;
        this.sampledAtMs = sampledAtMs;
        this.rate = rate;
        this.art = art;
    }

    public NowPlaying(Status status, String title, String artist) {
        this(status, title, artist, "", Source.WINDOW, 0L, 0L, 0L, 1.0d, null);
    }

    private static NowPlaying idle(Status status) {
        return new NowPlaying(status, "", "", "", Source.NONE, 0L, 0L, 0L, 1.0d, null);
    }

    public boolean playing() {
        return this.status == Status.PLAYING;
    }

    public boolean hasTrack() {
        return !this.title.isEmpty();
    }

    public boolean active() {
        return this.status == Status.PLAYING || this.status == Status.PAUSED;
    }

    public boolean hasTimeline() {
        return this.source == Source.SESSION && this.durationMs > 0;
    }

    public boolean lyricsEligible() {
        return hasTimeline() && hasTrack() && !this.artist.isBlank() && this.durationMs >= LYRICS_MIN_LENGTH_MS && this.durationMs <= LYRICS_MAX_LENGTH_MS && !this.artist.equalsIgnoreCase("Spotify") && !this.title.equalsIgnoreCase("Advertisement");
    }

    public boolean sameTrack(NowPlaying other) {
        return other != null && this.title.equals(other.title) && this.artist.equals(other.artist) && this.album.equals(other.album);
    }

    public long positionAt(long nowMs) {
        if (!hasTimeline()) {
            return 0L;
        }
        if (!playing()) {
            return this.positionMs;
        }
        long advanced = this.positionMs + ((long) (Math.max(0L, nowMs - this.sampledAtMs) * this.rate));
        return Math.min(advanced, this.durationMs);
    }

    public NowPlaying toggled() {
        long now = System.currentTimeMillis();
        switch (this.status.ordinal()) {
            case 3:
                if (hasTrack()) {
                    return new NowPlaying(Status.PLAYING, this.title, this.artist, this.album, this.source, this.durationMs, this.positionMs, now, this.rate, this.art);
                }
                return this;
            case 4:
                return new NowPlaying(Status.PAUSED, this.title, this.artist, this.album, this.source, this.durationMs, positionAt(now), now, this.rate, this.art);
            default:
                return this;
        }
    }

    public static NowPlaying fromSession(MediaSession.Snapshot snapshot, AlbumArt art, NowPlaying previous) {
        Status status;
        switch (snapshot.state()) {
            case PLAYING:
                status = Status.PLAYING;
                break;
            case CLOSED:
                status = Status.NOT_RUNNING;
                break;
            case CHANGING:
                status = (previous == null || previous.source != Source.SESSION || !previous.playing()) ? Status.PAUSED : Status.PLAYING;
                break;
            default:
                status = Status.PAUSED;
                break;
        }
        Status status2 = status;
        return status2 == Status.NOT_RUNNING ? NOT_RUNNING : new NowPlaying(status2, snapshot.title(), snapshot.artist(), snapshot.album(), Source.SESSION, snapshot.durationMs(), snapshot.positionMs(), snapshot.sampledAtMs(), snapshot.rate(), art);
    }

    public static NowPlaying fromWindowTitle(String windowTitle, NowPlaying previous) {
        if (windowTitle == null) {
            return NOT_RUNNING;
        }
        String title = windowTitle.strip();
        if (title.isEmpty()) {
            return NOT_RUNNING;
        }
        int split = title.indexOf(SEPARATOR);
        if (split < 0 && (title.equals("Spotify") || title.startsWith("Spotify "))) {
            if (previous != null && previous.hasTrack()) {
                return new NowPlaying(Status.PAUSED, previous.title, previous.artist);
            }
            return new NowPlaying(Status.PAUSED, "", "");
        }
        if (split > 0) {
            return new NowPlaying(Status.PLAYING, title.substring(split + SEPARATOR.length()).strip(), title.substring(0, split).strip());
        }
        return new NowPlaying(Status.PLAYING, title, "");
    }
}
