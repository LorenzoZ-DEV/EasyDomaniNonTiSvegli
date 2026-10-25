package net.anubis.client.util.spotify;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/spotify/LyricsQuery.class */
@Environment(EnvType.CLIENT)
public final class LyricsQuery extends Record {
    private final String title;
    private final String artist;
    private final String album;
    private final int durationSeconds;
    static final int MAX_DURATION_SECONDS = 3600;

    @Override // java.lang.Record
    public final String toString() {
        return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, LyricsQuery.class), LyricsQuery.class, "title;artist;album;durationSeconds", "FIELD:Lnet/anubis/client/util/spotify/LyricsQuery;->title:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/LyricsQuery;->artist:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/LyricsQuery;->album:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/LyricsQuery;->durationSeconds:I").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, LyricsQuery.class), LyricsQuery.class, "title;artist;album;durationSeconds", "FIELD:Lnet/anubis/client/util/spotify/LyricsQuery;->title:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/LyricsQuery;->artist:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/LyricsQuery;->album:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/LyricsQuery;->durationSeconds:I").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final boolean equals(Object o) {
        return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, LyricsQuery.class, Object.class), LyricsQuery.class, "title;artist;album;durationSeconds", "FIELD:Lnet/anubis/client/util/spotify/LyricsQuery;->title:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/LyricsQuery;->artist:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/LyricsQuery;->album:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/LyricsQuery;->durationSeconds:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
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

    public int durationSeconds() {
        return this.durationSeconds;
    }

    public LyricsQuery(String title, String artist, String album, int durationSeconds) {
        String title2 = title == null ? "" : title.strip();
        String artist2 = artist == null ? "" : artist.strip();
        String album2 = album == null ? "" : album.strip();
        durationSeconds = (durationSeconds < 0 || durationSeconds > MAX_DURATION_SECONDS) ? 0 : durationSeconds;
        this.title = title2;
        this.artist = artist2;
        this.album = album2;
        this.durationSeconds = durationSeconds;
    }

    public LyricsQuery withDurationMillis(long durationMs) {
        long seconds = Math.max(0L, (durationMs + 500) / 1000);
        return new LyricsQuery(this.title, this.artist, this.album, seconds > 3600 ? 0 : (int) seconds);
    }

    public boolean hasDuration() {
        return this.durationSeconds > 0;
    }

    public boolean searchable() {
        return (this.title.isEmpty() || this.artist.isEmpty()) ? false : true;
    }

    public String cacheKey() {
        String identity = squash(this.artist) + "\n" + squash(this.title) + "\n" + this.durationSeconds;
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(identity.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest, 0, 16);
        } catch (NoSuchAlgorithmException impossible) {
            throw new IllegalStateException("SHA-256 is always available", impossible);
        }
    }

    private static String squash(String value) {
        return value.toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }
}
