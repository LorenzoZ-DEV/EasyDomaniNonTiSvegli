package net.anubis.client.util.spotify;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/spotify/Lyrics.class */
@Environment(EnvType.CLIENT)
public final class Lyrics extends Record {
    private final Status status;
    private final SyncedLyrics synced;
    private final List<String> plainLines;
    private final long sourceId;
    public static final Lyrics LOADING = new Lyrics(Status.LOADING, SyncedLyrics.EMPTY, List.of(), 0);
    public static final Lyrics NOT_FOUND = new Lyrics(Status.NOT_FOUND, SyncedLyrics.EMPTY, List.of(), 0);
    public static final Lyrics UNAVAILABLE = new Lyrics(Status.UNAVAILABLE, SyncedLyrics.EMPTY, List.of(), 0);

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/spotify/Lyrics$Status.class */
    @Environment(EnvType.CLIENT)
    public enum Status {
        LOADING,
        SYNCED,
        PLAIN,
        INSTRUMENTAL,
        NOT_FOUND,
        UNAVAILABLE
    }

    @Override // java.lang.Record
    public final String toString() {
        return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Lyrics.class), Lyrics.class, "status;synced;plainLines;sourceId", "FIELD:Lnet/anubis/client/util/spotify/Lyrics;->status:Lnet/anubis/client/util/spotify/Lyrics$Status;", "FIELD:Lnet/anubis/client/util/spotify/Lyrics;->synced:Lnet/anubis/client/util/spotify/SyncedLyrics;", "FIELD:Lnet/anubis/client/util/spotify/Lyrics;->plainLines:Ljava/util/List;", "FIELD:Lnet/anubis/client/util/spotify/Lyrics;->sourceId:J").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Lyrics.class), Lyrics.class, "status;synced;plainLines;sourceId", "FIELD:Lnet/anubis/client/util/spotify/Lyrics;->status:Lnet/anubis/client/util/spotify/Lyrics$Status;", "FIELD:Lnet/anubis/client/util/spotify/Lyrics;->synced:Lnet/anubis/client/util/spotify/SyncedLyrics;", "FIELD:Lnet/anubis/client/util/spotify/Lyrics;->plainLines:Ljava/util/List;", "FIELD:Lnet/anubis/client/util/spotify/Lyrics;->sourceId:J").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final boolean equals(Object o) {
        return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Lyrics.class, Object.class), Lyrics.class, "status;synced;plainLines;sourceId", "FIELD:Lnet/anubis/client/util/spotify/Lyrics;->status:Lnet/anubis/client/util/spotify/Lyrics$Status;", "FIELD:Lnet/anubis/client/util/spotify/Lyrics;->synced:Lnet/anubis/client/util/spotify/SyncedLyrics;", "FIELD:Lnet/anubis/client/util/spotify/Lyrics;->plainLines:Ljava/util/List;", "FIELD:Lnet/anubis/client/util/spotify/Lyrics;->sourceId:J").dynamicInvoker().invoke(this, o) /* invoke-custom */;
    }

    public Status status() {
        return this.status;
    }

    public SyncedLyrics synced() {
        return this.synced;
    }

    public List<String> plainLines() {
        return this.plainLines;
    }

    public long sourceId() {
        return this.sourceId;
    }

    public Lyrics(Status status, SyncedLyrics synced, List<String> plainLines, long sourceId) {
        status = status == null ? Status.NOT_FOUND : status;
        synced = synced == null ? SyncedLyrics.EMPTY : synced;
        List<String> plainLines2 = plainLines == null ? List.of() : List.copyOf(plainLines);
        this.status = status;
        this.synced = synced;
        this.plainLines = plainLines2;
        this.sourceId = sourceId;
    }

    public static Lyrics of(LrclibTrack track) {
        if (track == null) {
            return NOT_FOUND;
        }
        SyncedLyrics synced = SyncedLyrics.parse(track.syncedLyrics());
        List<String> plain = plainLines(track.plainLyrics());
        if (plain.isEmpty() && synced.isEmpty()) {
            plain = plainLines(track.syncedLyrics());
        }
        if (!synced.isEmpty() && !synced.texts().isEmpty()) {
            return new Lyrics(Status.SYNCED, synced, plain.isEmpty() ? synced.texts() : plain, track.id());
        }
        if (!plain.isEmpty()) {
            return new Lyrics(Status.PLAIN, SyncedLyrics.EMPTY, plain, track.id());
        }
        if (track.instrumental() || synced.size() > 0) {
            return new Lyrics(Status.INSTRUMENTAL, SyncedLyrics.EMPTY, List.of(), track.id());
        }
        return NOT_FOUND;
    }

    static List<String> plainLines(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        List<String> lines = new ArrayList<>();
        boolean gap = false;
        for (String raw : text.split("\r\n|\r|\n")) {
            String line = raw.strip();
            if (line.isEmpty()) {
                gap = !lines.isEmpty();
            } else {
                if (gap) {
                    lines.add("");
                }
                gap = false;
                lines.add(line);
            }
        }
        return List.copyOf(lines);
    }
}
