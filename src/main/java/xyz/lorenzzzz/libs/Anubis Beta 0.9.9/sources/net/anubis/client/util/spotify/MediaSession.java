package net.anubis.client.util.spotify;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Guid;
import java.io.ByteArrayOutputStream;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.Arrays;
import java.util.Locale;
import net.anubis.client.gui.HudLayoutStore;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/spotify/MediaSession.class */
@Environment(EnvType.CLIENT)
public final class MediaSession {
    private static final String MANAGER_CLASS = "Windows.Media.Control.GlobalSystemMediaTransportControlsSessionManager";
    private static final int STATICS_REQUEST_ASYNC = 6;
    private static final int MANAGER_GET_CURRENT_SESSION = 6;
    private static final int MANAGER_GET_SESSIONS = 7;
    private static final int VECTOR_VIEW_GET_AT = 6;
    private static final int VECTOR_VIEW_GET_SIZE = 7;
    private static final int SESSION_GET_SOURCE_APP_USER_MODEL_ID = 6;
    private static final int SESSION_TRY_GET_MEDIA_PROPERTIES_ASYNC = 7;
    private static final int SESSION_GET_TIMELINE_PROPERTIES = 8;
    private static final int SESSION_GET_PLAYBACK_INFO = 9;
    private static final int SESSION_TRY_SKIP_NEXT_ASYNC = 16;
    private static final int SESSION_TRY_SKIP_PREVIOUS_ASYNC = 17;
    private static final int SESSION_TRY_TOGGLE_PLAY_PAUSE_ASYNC = 20;
    private static final int PROPERTIES_GET_TITLE = 6;
    private static final int PROPERTIES_GET_ARTIST = 9;
    private static final int PROPERTIES_GET_ALBUM_TITLE = 10;
    private static final int PROPERTIES_GET_THUMBNAIL = 15;
    private static final int TIMELINE_GET_START_TIME = 6;
    private static final int TIMELINE_GET_END_TIME = 7;
    private static final int TIMELINE_GET_POSITION = 10;
    private static final int TIMELINE_GET_LAST_UPDATED_TIME = 11;
    private static final int PLAYBACK_INFO_GET_PLAYBACK_STATUS = 7;
    private static final int PLAYBACK_INFO_GET_PLAYBACK_RATE = 10;
    private static final int REFERENCE_GET_VALUE = 6;
    private static final int STREAM_REFERENCE_OPEN_READ_ASYNC = 6;
    private static final int RANDOM_ACCESS_STREAM_GET_SIZE = 6;
    private static final int CONTENT_TYPE_PROVIDER_GET_CONTENT_TYPE = 6;
    private static final String DATA_READER_CLASS = "Windows.Storage.Streams.DataReader";
    private static final int DATA_READER_FACTORY_CREATE = 6;
    private static final int DATA_READER_PUT_INPUT_STREAM_OPTIONS = 12;
    private static final int DATA_READER_READ_BYTES = 14;
    private static final int DATA_READER_LOAD_ASYNC = 29;
    private static final int INPUT_STREAM_OPTIONS_PARTIAL = 1;
    private static final long REQUEST_TIMEOUT_MS = 3000;
    private static final long PROPERTIES_TIMEOUT_MS = 1000;
    private static final long THUMBNAIL_TIMEOUT_MS = 1500;
    private static final long CONTROL_TIMEOUT_MS = 1000;
    private static final long MANAGER_RETRY_MS = 5000;
    private static final int MAX_SESSIONS = 64;
    private static final int THUMBNAIL_CHECKS = 3;
    private static final long THUMBNAIL_RECHECK_MS = 2000;
    private static final long THUMBNAIL_SETTLE_MS = 1500;
    private static final int THUMBNAIL_CHUNK = 65536;
    private static final int THUMBNAIL_MAX_BYTES = 4194304;
    private static final long UNIX_EPOCH_TICKS = 116444736000000000L;
    private static final long TICKS_PER_MS = 10000;
    private final String appFilter;
    private volatile Thread owner;
    private boolean ownerInitialized;
    private volatile boolean closed;
    private volatile boolean unsupported;
    private volatile String lastError;
    private Pointer manager;
    private Pointer dataReaderFactory;
    private long managerRetryAt;
    private String trackKey;
    private long trackChangedAt;
    private byte[] thumbnail;
    private int thumbnailVersion;
    private int thumbnailChecksLeft;
    private long nextThumbnailCheckAt;
    private byte[] staleThumbnail;
    private static final Guid.GUID IID_MANAGER_STATICS = WinRt.iid("2050C4EE-11A0-57DE-AED7-C97C70338245");
    private static final Guid.GUID IID_RANDOM_ACCESS_STREAM = WinRt.iid("905A0FE1-BC53-11DF-8C49-001E4FC686DA");
    private static final Guid.GUID IID_INPUT_STREAM = WinRt.iid("905A0FE2-BC53-11DF-8C49-001E4FC686DA");
    private static final Guid.GUID IID_CONTENT_TYPE_PROVIDER = WinRt.iid("97D098A5-3B99-4DE9-88A5-E11D2F50C795");
    private static final Guid.GUID IID_DATA_READER_FACTORY = WinRt.iid("D7527847-57DA-4E15-914C-06806699A098");
    private String sessionApp = "";
    private String trackAlbum = "";
    private String thumbnailType = "";

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/spotify/MediaSession$PlaybackState.class */
    @Environment(EnvType.CLIENT)
    public enum PlaybackState {
        CLOSED,
        OPENED,
        CHANGING,
        STOPPED,
        PLAYING,
        PAUSED;

        private static final PlaybackState[] VALUES = values();

        static PlaybackState of(int value) {
            return (value < 0 || value >= VALUES.length) ? CLOSED : VALUES[value];
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/spotify/MediaSession$Snapshot.class */
    @Environment(EnvType.CLIENT)
    public static final class Snapshot extends Record {
        private final String title;
        private final String artist;
        private final String album;
        private final long durationMs;
        private final long positionMs;
        private final long sampledAtMs;
        private final PlaybackState state;
        private final double rate;
        private final byte[] thumbnail;
        private final String thumbnailType;
        private final int thumbnailVersion;

        public Snapshot(String title, String artist, String album, long durationMs, long positionMs, long sampledAtMs, PlaybackState state, double rate, byte[] thumbnail, String thumbnailType, int thumbnailVersion) {
            this.title = title;
            this.artist = artist;
            this.album = album;
            this.durationMs = durationMs;
            this.positionMs = positionMs;
            this.sampledAtMs = sampledAtMs;
            this.state = state;
            this.rate = rate;
            this.thumbnail = thumbnail;
            this.thumbnailType = thumbnailType;
            this.thumbnailVersion = thumbnailVersion;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Snapshot.class), Snapshot.class, "title;artist;album;durationMs;positionMs;sampledAtMs;state;rate;thumbnail;thumbnailType;thumbnailVersion", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->title:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->artist:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->album:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->durationMs:J", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->positionMs:J", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->sampledAtMs:J", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->state:Lnet/anubis/client/util/spotify/MediaSession$PlaybackState;", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->rate:D", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->thumbnail:[B", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->thumbnailType:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->thumbnailVersion:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Snapshot.class), Snapshot.class, "title;artist;album;durationMs;positionMs;sampledAtMs;state;rate;thumbnail;thumbnailType;thumbnailVersion", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->title:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->artist:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->album:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->durationMs:J", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->positionMs:J", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->sampledAtMs:J", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->state:Lnet/anubis/client/util/spotify/MediaSession$PlaybackState;", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->rate:D", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->thumbnail:[B", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->thumbnailType:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->thumbnailVersion:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Snapshot.class, Object.class), Snapshot.class, "title;artist;album;durationMs;positionMs;sampledAtMs;state;rate;thumbnail;thumbnailType;thumbnailVersion", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->title:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->artist:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->album:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->durationMs:J", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->positionMs:J", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->sampledAtMs:J", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->state:Lnet/anubis/client/util/spotify/MediaSession$PlaybackState;", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->rate:D", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->thumbnail:[B", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->thumbnailType:Ljava/lang/String;", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Snapshot;->thumbnailVersion:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
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

        public long durationMs() {
            return this.durationMs;
        }

        public long positionMs() {
            return this.positionMs;
        }

        public long sampledAtMs() {
            return this.sampledAtMs;
        }

        public PlaybackState state() {
            return this.state;
        }

        public double rate() {
            return this.rate;
        }

        public byte[] thumbnail() {
            return this.thumbnail;
        }

        public String thumbnailType() {
            return this.thumbnailType;
        }

        public int thumbnailVersion() {
            return this.thumbnailVersion;
        }

        public boolean hasThumbnail() {
            return this.thumbnail != null;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/spotify/MediaSession$Thumbnail.class */
    @Environment(EnvType.CLIENT)
    private static final class Thumbnail extends Record {
        private final byte[] bytes;
        private final String type;

        private Thumbnail(byte[] bytes, String type) {
            this.bytes = bytes;
            this.type = type;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Thumbnail.class), Thumbnail.class, "bytes;type", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Thumbnail;->bytes:[B", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Thumbnail;->type:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Thumbnail.class), Thumbnail.class, "bytes;type", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Thumbnail;->bytes:[B", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Thumbnail;->type:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Thumbnail.class, Object.class), Thumbnail.class, "bytes;type", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Thumbnail;->bytes:[B", "FIELD:Lnet/anubis/client/util/spotify/MediaSession$Thumbnail;->type:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public byte[] bytes() {
            return this.bytes;
        }

        public String type() {
            return this.type;
        }
    }

    public MediaSession(String appFilter) {
        this.appFilter = appFilter == null ? null : appFilter.toLowerCase(Locale.ROOT);
    }

    public static MediaSession spotify() {
        return new MediaSession(HudLayoutStore.SPOTIFY_ID);
    }

    public Snapshot poll() {
        try {
            if (!prepare()) {
                return null;
            }
            this.lastError = null;
            return read();
        } catch (LinkageError failure) {
            this.unsupported = true;
            return fail("WinRT unavailable: " + String.valueOf(failure));
        } catch (WinRt.WinRtException failure2) {
            return fail(failure2.getMessage());
        } catch (RuntimeException failure3) {
            return fail(failure3.toString());
        }
    }

    public boolean togglePlayPause() {
        return control(20, "TryTogglePlayPauseAsync", new Object[0]);
    }

    public boolean skipNext() {
        return control(16, "TrySkipNextAsync", new Object[0]);
    }

    public boolean skipPrevious() {
        return control(17, "TrySkipPreviousAsync", new Object[0]);
    }

    public String lastError() {
        return this.lastError;
    }

    public boolean isUnsupported() {
        return this.unsupported;
    }

    public void close() {
        this.closed = true;
        if (this.owner == Thread.currentThread()) {
            dispose();
        }
    }

    private boolean prepare() {
        Thread current = Thread.currentThread();
        if (this.owner != current) {
            if (this.owner != null && this.owner.isAlive()) {
                this.lastError = "called from a thread other than the owner";
                return false;
            }
            if (this.closed || this.unsupported) {
                return false;
            }
            int result = WinRt.initializeMultithreaded();
            if (result < 0 && result != -2147417850) {
                throw new WinRt.WinRtException("RoInitialize", result);
            }
            this.owner = current;
            this.ownerInitialized = result >= 0;
        }
        if (this.closed) {
            dispose();
            return false;
        }
        if (this.unsupported) {
            return false;
        }
        if (this.manager == null) {
            long now = System.currentTimeMillis();
            if (now < this.managerRetryAt) {
                return false;
            }
            this.managerRetryAt = now + MANAGER_RETRY_MS;
            this.manager = requestManager();
        }
        return this.manager != null;
    }

    private Pointer requestManager() {
        WinRt.Refs refs = new WinRt.Refs();
        try {
            try {
                Pointer statics = refs.add(WinRt.activationFactory(MANAGER_CLASS, IID_MANAGER_STATICS));
                Pointer operation = refs.add(WinRt.callForObject(statics, 6, "RequestAsync", new Object[0]));
                Pointer pointerAwaitObject = WinRt.awaitObject(operation, REQUEST_TIMEOUT_MS, "RequestAsync");
                refs.close();
                return pointerAwaitObject;
            } catch (WinRt.WinRtException failure) {
                if (failure.hresult == -2147221164 || failure.hresult == -2147467262) {
                    this.unsupported = true;
                }
                throw failure;
            }
        } catch (Throwable th) {
            try {
                refs.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private void dispose() {
        Pointer oldManager = this.manager;
        Pointer oldFactory = this.dataReaderFactory;
        this.manager = null;
        this.dataReaderFactory = null;
        try {
            WinRt.release(oldManager);
            WinRt.release(oldFactory);
        } catch (LinkageError | RuntimeException e) {
        }
        forgetTrack();
        if (this.ownerInitialized) {
            this.ownerInitialized = false;
            try {
                WinRt.uninitialize();
            } catch (LinkageError | RuntimeException e2) {
            }
        }
    }

    private Snapshot fail(String message) {
        this.lastError = message;
        return null;
    }

    private void dropManager() {
        Pointer oldManager = this.manager;
        this.manager = null;
        try {
            WinRt.release(oldManager);
        } catch (LinkageError | RuntimeException e) {
        }
    }

    private Snapshot read() {
        WinRt.Refs refs = new WinRt.Refs();
        try {
            Pointer session = findSession(refs);
            if (session == null) {
                forgetTrack();
                refs.close();
                return null;
            }
            String app = this.sessionApp;
            PlaybackState state = PlaybackState.CLOSED;
            double rate = 1.0d;
            Pointer playback = refs.add(WinRt.callForObject(session, 9, "GetPlaybackInfo", new Object[0]));
            if (playback != null) {
                state = PlaybackState.of(WinRt.getInt32(playback, 7, "PlaybackStatus"));
                Pointer rateValue = refs.add(WinRt.callForObject(playback, 10, "PlaybackRate", new Object[0]));
                if (rateValue != null) {
                    double reported = WinRt.getDouble(rateValue, 6, "PlaybackRate value");
                    if (reported > 0.0d && reported <= 16.0d) {
                        rate = reported;
                    }
                }
            }
            long startTicks = 0;
            long endTicks = 0;
            long positionTicks = 0;
            long updatedTicks = 0;
            Pointer timeline = refs.add(WinRt.callForObject(session, 8, "GetTimelineProperties", new Object[0]));
            if (timeline != null) {
                startTicks = WinRt.getInt64(timeline, 6, "StartTime");
                endTicks = WinRt.getInt64(timeline, 7, "EndTime");
                positionTicks = WinRt.getInt64(timeline, 10, "Position");
                updatedTicks = WinRt.getInt64(timeline, 11, "LastUpdatedTime");
            }
            long timelineReadAt = System.currentTimeMillis();
            String title = "";
            String artist = "";
            String album = "";
            Pointer operation = refs.add(WinRt.callForObject(session, 7, "TryGetMediaPropertiesAsync", new Object[0]));
            Pointer properties = refs.add(WinRt.awaitObject(operation, 1000L, "TryGetMediaPropertiesAsync"));
            if (properties != null) {
                title = WinRt.getString(properties, 6, "Title").strip();
                artist = WinRt.getString(properties, 9, "Artist").strip();
                album = WinRt.getString(properties, 10, "AlbumTitle").strip();
            }
            long now = System.currentTimeMillis();
            String key = app + "\n" + title + "\n" + artist + "\n" + album;
            if (!key.equals(this.trackKey)) {
                boolean sameAlbum = (this.trackKey == null || album.isEmpty() || !album.equals(this.trackAlbum)) ? false : true;
                this.staleThumbnail = sameAlbum ? null : this.thumbnail;
                if (!sameAlbum) {
                    setThumbnail(null, "");
                }
                this.trackKey = key;
                this.trackAlbum = album;
                this.trackChangedAt = now;
                this.thumbnailChecksLeft = 3;
                this.nextThumbnailCheckAt = now;
            }
            if (properties != null && this.thumbnailChecksLeft > 0 && now >= this.nextThumbnailCheckAt) {
                Thumbnail art = readThumbnail(properties);
                now = System.currentTimeMillis();
                boolean stale = (art == null || this.staleThumbnail == null || !Arrays.equals(art.bytes(), this.staleThumbnail)) ? false : true;
                if ((art == null || stale) && now - this.trackChangedAt < 1500) {
                    this.nextThumbnailCheckAt = now;
                } else {
                    if (art != null) {
                        setThumbnail(art.bytes(), art.type());
                    }
                    this.staleThumbnail = null;
                    this.thumbnailChecksLeft--;
                    this.nextThumbnailCheckAt = now + THUMBNAIL_RECHECK_MS;
                }
            }
            long durationMs = Math.max(0L, (endTicks - startTicks) / TICKS_PER_MS);
            long positionMs = Math.max(0L, (positionTicks - startTicks) / TICKS_PER_MS);
            if (state == PlaybackState.PLAYING && durationMs > 0) {
                long fromMs = updatedTicks > 0 ? (updatedTicks - UNIX_EPOCH_TICKS) / TICKS_PER_MS : timelineReadAt;
                long elapsed = now - fromMs;
                if (elapsed > 0) {
                    positionMs += (long) (Math.min(elapsed, durationMs) * rate);
                }
            }
            if (durationMs > 0) {
                positionMs = Math.min(positionMs, durationMs);
            }
            Snapshot snapshot = new Snapshot(title, artist, album, durationMs, positionMs, now, state, rate, this.thumbnail, this.thumbnailType, this.thumbnailVersion);
            refs.close();
            return snapshot;
        } catch (Throwable th) {
            try {
                refs.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private Pointer findSession(WinRt.Refs refs) {
        if (this.appFilter == null) {
            Pointer session = refs.add(managerCall(6, "GetCurrentSession"));
            if (session != null) {
                this.sessionApp = WinRt.getString(session, 6, "SourceAppUserModelId");
            }
            return session;
        }
        Pointer sessions = refs.add(managerCall(7, "GetSessions"));
        if (sessions == null) {
            return null;
        }
        int count = Math.min(WinRt.getInt32(sessions, 7, "Sessions.Size"), 64);
        for (int i = 0; i < count; i++) {
            Pointer session2 = refs.add(WinRt.callForObject(sessions, 6, "Sessions.GetAt", Integer.valueOf(i)));
            if (session2 != null) {
                String app = WinRt.getString(session2, 6, "SourceAppUserModelId");
                if (app.toLowerCase(Locale.ROOT).contains(this.appFilter)) {
                    this.sessionApp = app;
                    return session2;
                }
            }
        }
        return null;
    }

    private Pointer managerCall(int slot, String step) {
        try {
            return WinRt.callForObject(this.manager, slot, step, new Object[0]);
        } catch (WinRt.WinRtException failure) {
            dropManager();
            throw failure;
        }
    }

    private void forgetTrack() {
        this.trackKey = null;
        this.trackAlbum = "";
        this.thumbnailChecksLeft = 0;
        this.staleThumbnail = null;
        setThumbnail(null, "");
    }

    private void setThumbnail(byte[] bytes, String type) {
        if (bytes == null) {
            if (this.thumbnail == null) {
                return;
            }
        } else if (Arrays.equals(bytes, this.thumbnail)) {
            return;
        }
        this.thumbnail = bytes;
        this.thumbnailType = bytes == null ? "" : type;
        this.thumbnailVersion++;
    }

    private Thumbnail readThumbnail(Pointer properties) {
        try {
            WinRt.Refs refs = new WinRt.Refs();
            try {
                Pointer reference = refs.add(WinRt.callForObject(properties, 15, "Thumbnail", new Object[0]));
                if (reference == null) {
                    refs.close();
                    return null;
                }
                Pointer operation = refs.add(WinRt.callForObject(reference, 6, "OpenReadAsync", new Object[0]));
                Pointer stream = refs.add(WinRt.awaitObject(operation, 1500L, "OpenReadAsync"));
                if (stream == null) {
                    refs.close();
                    return null;
                }
                Pointer typed = refs.add(WinRt.tryQueryInterface(stream, IID_CONTENT_TYPE_PROVIDER));
                String type = typed != null ? WinRt.getString(typed, 6, "ContentType") : "";
                Pointer random = refs.add(WinRt.queryInterface(stream, IID_RANDOM_ACCESS_STREAM, "IRandomAccessStream"));
                long size = WinRt.getInt64(random, 6, "Stream.Size");
                if (size < 0 || size > 4194304) {
                    refs.close();
                    return null;
                }
                Pointer input = refs.add(WinRt.queryInterface(stream, IID_INPUT_STREAM, "IInputStream"));
                Pointer reader = refs.add(WinRt.callForObject(dataReaderFactory(), 6, "CreateDataReader", input));
                if (reader == null) {
                    refs.close();
                    return null;
                }
                WinRt.check(WinRt.invoke(reader, 12, 1), "InputStreamOptions");
                ByteArrayOutputStream out = new ByteArrayOutputStream(size > 0 ? (int) size : 65536);
                while (true) {
                    if (size != 0 && out.size() >= size) {
                        break;
                    }
                    int want = size > 0 ? (int) Math.min(size - ((long) out.size()), 65536L) : 65536;
                    Pointer load = WinRt.callForObject(reader, 29, "LoadAsync", Integer.valueOf(want));
                    try {
                        int loaded = WinRt.awaitUInt32(load, 1500L, "LoadAsync");
                        WinRt.release(load);
                        if (loaded <= 0) {
                            break;
                        }
                        if (out.size() + loaded > 4194304) {
                            refs.close();
                            return null;
                        }
                        byte[] chunk = new byte[loaded];
                        WinRt.check(WinRt.invoke(reader, 14, Integer.valueOf(loaded), chunk), "ReadBytes");
                        out.write(chunk, 0, loaded);
                    } catch (Throwable th) {
                        WinRt.release(load);
                        throw th;
                    }
                }
                if (out.size() == 0 || (size > 0 && out.size() != size)) {
                    refs.close();
                    return null;
                }
                Thumbnail thumbnail = new Thumbnail(out.toByteArray(), type);
                refs.close();
                return thumbnail;
            } catch (Throwable th2) {
                try {
                    refs.close();
                } catch (Throwable th3) {
                    th2.addSuppressed(th3);
                }
                throw th2;
            }
        } catch (WinRt.WinRtException failure) {
            this.lastError = "album art: " + failure.getMessage();
            return null;
        }
    }

    private Pointer dataReaderFactory() {
        if (this.dataReaderFactory == null) {
            this.dataReaderFactory = WinRt.activationFactory(DATA_READER_CLASS, IID_DATA_READER_FACTORY);
        }
        return this.dataReaderFactory;
    }

    private boolean control(int slot, String step, Object... args) {
        try {
            if (!prepare()) {
                return false;
            }
            WinRt.Refs refs = new WinRt.Refs();
            try {
                Pointer session = findSession(refs);
                if (session != null) {
                    Pointer operation = refs.add(WinRt.callForObject(session, slot, step, args));
                    boolean accepted = WinRt.awaitBoolean(operation, 1000L, step);
                    this.lastError = null;
                    refs.close();
                    return accepted;
                }
                refs.close();
                return false;
            } catch (Throwable th) {
                try {
                    refs.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (LinkageError failure) {
            this.unsupported = true;
            fail("WinRT unavailable: " + String.valueOf(failure));
            return false;
        } catch (WinRt.WinRtException failure2) {
            fail(failure2.getMessage());
            return false;
        } catch (RuntimeException failure3) {
            fail(failure3.toString());
            return false;
        }
    }
}
