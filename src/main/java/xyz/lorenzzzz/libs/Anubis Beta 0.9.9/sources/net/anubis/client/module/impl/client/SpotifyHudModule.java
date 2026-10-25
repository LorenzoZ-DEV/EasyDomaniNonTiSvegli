package net.anubis.client.module.impl.client;

import com.sun.jna.Platform;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.anubis.client.chat.ModuleChat;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.input.ModuleKeybinds;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.KeySetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.util.spotify.AlbumArt;
import net.anubis.client.util.spotify.Lyrics;
import net.anubis.client.util.spotify.LyricsQuery;
import net.anubis.client.util.spotify.LyricsService;
import net.anubis.client.util.spotify.MediaSession;
import net.anubis.client.util.spotify.NowPlaying;
import net.anubis.client.util.spotify.SpotifyWindow;
import net.anubis.client.util.staff.StaffAlarm;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import net.minecraft.class_3675;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/client/SpotifyHudModule.class */
@Environment(EnvType.CLIENT)
public final class SpotifyHudModule extends ClientModule {
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private static final long POLL_INTERVAL_MS = 500;
    private static final long WINDOW_POLL_MS = 1000;
    private static final long REPOLL_AFTER_KEY_MS = 250;
    private static final long KEY_COOLDOWN_MS = 200;
    private static final int ART_MAX_SIDE = 256;
    private static final int STABLE_POLLS = 2;
    private static final int SESSION_HICCUPS = 2;
    private static final long ERROR_LOG_INTERVAL_MS = 60000;
    private final BooleanSetting showArtist;
    private final BooleanSetting showAlbumArt;
    private final BooleanSetting showProgress;
    private final BooleanSetting scrollTitles;
    private final BooleanSetting showLyrics;
    private final ModeSetting lyricLines;
    private final ModeSetting lyricMotion;
    private final ModeSetting lyricHighlight;
    private final BooleanSetting lyricsOnline;
    private final NumberSetting cardWidth;
    private final ColorSetting titleColor;
    private final ColorSetting artistColor;
    private final ColorSetting lyricsColor;
    private final ColorSetting upcomingLyricsColor;
    private final ColorSetting highlightColor;
    private final BooleanSetting showWhenIdle;
    private final BooleanSetting mediaKeys;
    private final KeySetting playPauseKey;
    private final KeySetting nextKey;
    private final KeySetting previousKey;
    private volatile NowPlaying nowPlaying;
    private volatile TrackLyrics lyrics;
    private volatile Poller poller;
    private final Object publishLock;
    private final KeySetting[] keys;
    private final SpotifyWindow.MediaKey[] actions;
    private final boolean[] wasDown;
    private long lastKeyAt;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/client/SpotifyHudModule$LyricHighlight.class */
    @Environment(EnvType.CLIENT)
    public enum LyricHighlight {
        KARAOKE,
        LINE,
        OFF
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/client/SpotifyHudModule$LyricMotion.class */
    @Environment(EnvType.CLIENT)
    public enum LyricMotion {
        WAVE,
        GLIDE,
        FADE
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/client/SpotifyHudModule$TrackLyrics.class */
    @Environment(EnvType.CLIENT)
    private static final class TrackLyrics extends Record {
        private final String title;
        private final String artist;
        private final LyricsQuery query;

        private TrackLyrics(String title, String artist, LyricsQuery query) {
            this.title = title;
            this.artist = artist;
            this.query = query;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, TrackLyrics.class), TrackLyrics.class, "title;artist;query", "FIELD:Lnet/anubis/client/module/impl/client/SpotifyHudModule$TrackLyrics;->title:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/client/SpotifyHudModule$TrackLyrics;->artist:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/client/SpotifyHudModule$TrackLyrics;->query:Lnet/anubis/client/util/spotify/LyricsQuery;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, TrackLyrics.class), TrackLyrics.class, "title;artist;query", "FIELD:Lnet/anubis/client/module/impl/client/SpotifyHudModule$TrackLyrics;->title:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/client/SpotifyHudModule$TrackLyrics;->artist:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/client/SpotifyHudModule$TrackLyrics;->query:Lnet/anubis/client/util/spotify/LyricsQuery;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, TrackLyrics.class, Object.class), TrackLyrics.class, "title;artist;query", "FIELD:Lnet/anubis/client/module/impl/client/SpotifyHudModule$TrackLyrics;->title:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/client/SpotifyHudModule$TrackLyrics;->artist:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/client/SpotifyHudModule$TrackLyrics;->query:Lnet/anubis/client/util/spotify/LyricsQuery;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public String title() {
            return this.title;
        }

        public String artist() {
            return this.artist;
        }

        public LyricsQuery query() {
            return this.query;
        }
    }

    public SpotifyHudModule() {
        super("spotify-hud", "Spotify HUD", "Shows what Spotify is playing, with synced lyrics and optional media keys.", ModuleCategory.CLIENT);
        this.showArtist = (BooleanSetting) addSetting("show-artist", new BooleanSetting("Show Artist", true));
        this.showAlbumArt = (BooleanSetting) addSetting("show-album-art", new BooleanSetting("Show Album Art", true));
        this.showProgress = (BooleanSetting) addSetting("show-progress", new BooleanSetting("Show Progress", true));
        this.scrollTitles = (BooleanSetting) addSetting("scroll-titles", new BooleanSetting("Scroll Long Titles", true));
        this.showLyrics = (BooleanSetting) addSetting("show-lyrics", new BooleanSetting("Show Lyrics", true));
        this.lyricLines = (ModeSetting) addSetting("lyric-lines", new ModeSetting("Lyric Lines", "2", "1", "2", "3"));
        this.lyricMotion = (ModeSetting) addSetting("lyric-motion", new ModeSetting("Lyric Motion", "Wave", "Wave", "Glide", "Fade"));
        this.lyricHighlight = (ModeSetting) addSetting("lyric-highlight", new ModeSetting("Lyric Highlight", "Karaoke", "Karaoke", "Line", StaffAlarm.OFF));
        this.lyricsOnline = (BooleanSetting) addSetting("lyrics-online", new BooleanSetting("Lyrics Online", true));
        this.cardWidth = (NumberSetting) addSetting("card-width", new NumberSetting("Card Width", 320.0f, 200.0f, 560.0f).step(1.0f));
        this.titleColor = (ColorSetting) addSetting("title-color", new ColorSetting("Title Color", -1447442));
        this.artistColor = (ColorSetting) addSetting("artist-color", new ColorSetting("Artist Color", -6052949));
        this.lyricsColor = (ColorSetting) addSetting("lyrics-color", new ColorSetting("Lyrics Color", -657929));
        this.upcomingLyricsColor = (ColorSetting) addSetting("upcoming-lyrics-color", new ColorSetting("Upcoming Lyrics Color", -6052949));
        this.highlightColor = (ColorSetting) addSetting("highlight-color", new ColorSetting("Highlight Color", -14756000));
        this.showWhenIdle = (BooleanSetting) addSetting("show-when-idle", new BooleanSetting("Show When Idle", false));
        this.mediaKeys = (BooleanSetting) addSetting("media-keys", new BooleanSetting("Media Keys", false));
        this.playPauseKey = (KeySetting) addSetting("play-pause-key", new KeySetting("Play / Pause", -1));
        this.nextKey = (KeySetting) addSetting("next-key", new KeySetting("Next Track", -1));
        this.previousKey = (KeySetting) addSetting("previous-key", new KeySetting("Previous Track", -1));
        this.nowPlaying = NowPlaying.WAITING;
        this.publishLock = new Object();
        this.keys = new KeySetting[]{this.playPauseKey, this.nextKey, this.previousKey};
        this.actions = new SpotifyWindow.MediaKey[]{SpotifyWindow.MediaKey.PLAY_PAUSE, SpotifyWindow.MediaKey.NEXT, SpotifyWindow.MediaKey.PREVIOUS};
        this.wasDown = new boolean[this.keys.length];
        usesNotifications();
        ColorSetting colorSetting = this.artistColor;
        BooleanSetting booleanSetting = this.showArtist;
        Objects.requireNonNull(booleanSetting);
        colorSetting.visibleWhen(booleanSetting::get);
        this.lyricsColor.visibleWhen(() -> {
            return this.showLyrics.get() && !this.lyricHighlight.is("Line");
        });
        ColorSetting colorSetting2 = this.upcomingLyricsColor;
        BooleanSetting booleanSetting2 = this.showLyrics;
        Objects.requireNonNull(booleanSetting2);
        colorSetting2.visibleWhen(booleanSetting2::get);
        this.highlightColor.visibleWhen(() -> {
            return this.showLyrics.get() && !this.lyricHighlight.is(StaffAlarm.OFF);
        });
        ModeSetting modeSetting = this.lyricLines;
        BooleanSetting booleanSetting3 = this.showLyrics;
        Objects.requireNonNull(booleanSetting3);
        modeSetting.visibleWhen(booleanSetting3::get);
        ModeSetting modeSetting2 = this.lyricMotion;
        BooleanSetting booleanSetting4 = this.showLyrics;
        Objects.requireNonNull(booleanSetting4);
        modeSetting2.visibleWhen(booleanSetting4::get);
        ModeSetting modeSetting3 = this.lyricHighlight;
        BooleanSetting booleanSetting5 = this.showLyrics;
        Objects.requireNonNull(booleanSetting5);
        modeSetting3.visibleWhen(booleanSetting5::get);
        BooleanSetting booleanSetting6 = this.lyricsOnline;
        BooleanSetting booleanSetting7 = this.showLyrics;
        Objects.requireNonNull(booleanSetting7);
        booleanSetting6.visibleWhen(booleanSetting7::get);
        this.lyricsOnline.onChange(() -> {
            Poller running = this.poller;
            if (running != null) {
                running.lyricsService.setOnline(this.lyricsOnline.get());
            }
        });
        KeySetting keySetting = this.playPauseKey;
        BooleanSetting booleanSetting8 = this.mediaKeys;
        Objects.requireNonNull(booleanSetting8);
        keySetting.visibleWhen(booleanSetting8::get);
        KeySetting keySetting2 = this.nextKey;
        BooleanSetting booleanSetting9 = this.mediaKeys;
        Objects.requireNonNull(booleanSetting9);
        keySetting2.visibleWhen(booleanSetting9::get);
        KeySetting keySetting3 = this.previousKey;
        BooleanSetting booleanSetting10 = this.mediaKeys;
        Objects.requireNonNull(booleanSetting10);
        keySetting3.visibleWhen(booleanSetting10::get);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        Arrays.fill(this.wasDown, true);
        if (!Platform.isWindows()) {
            synchronized (this.publishLock) {
                this.nowPlaying = NowPlaying.UNSUPPORTED;
                this.lyrics = null;
            }
            ModuleChat.send(this, ModuleChat.body().danger("Not supported").text(", only the Spotify app on Windows works"));
            return;
        }
        Poller started = new Poller();
        synchronized (this.publishLock) {
            this.poller = started;
            this.nowPlaying = NowPlaying.WAITING;
            this.lyrics = null;
        }
        started.start();
        on(TickEvent.TYPE, Stage.POST, this::onTick);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        Poller stopped;
        synchronized (this.publishLock) {
            stopped = this.poller;
            this.poller = null;
            this.nowPlaying = Platform.isWindows() ? NowPlaying.WAITING : NowPlaying.UNSUPPORTED;
            this.lyrics = null;
        }
        if (stopped != null) {
            stopped.stop();
        }
    }

    private void publish(Poller from, NowPlaying state, TrackLyrics trackLyrics) {
        synchronized (this.publishLock) {
            if (from != this.poller) {
                return;
            }
            this.nowPlaying = state;
            this.lyrics = trackLyrics;
        }
    }

    public static SpotifyHudModule get() {
        return (SpotifyHudModule) ModuleRegistry.get(SpotifyHudModule.class);
    }

    public static SpotifyHudModule active() {
        SpotifyHudModule module = get();
        if (module == null || !module.isEnabled()) {
            return null;
        }
        return module;
    }

    public static void hudButton(SpotifyWindow.MediaKey key) {
        SpotifyHudModule module = active();
        if (module != null) {
            module.press(key);
        }
    }

    public static boolean shouldShowHud() {
        SpotifyHudModule module = active();
        if (module == null) {
            return false;
        }
        NowPlaying now = module.nowPlaying;
        if (now.status() == NowPlaying.Status.UNSUPPORTED || now.status() == NowPlaying.Status.WAITING) {
            return false;
        }
        return now.active() || module.showWhenIdle.get();
    }

    public NowPlaying nowPlaying() {
        return this.nowPlaying;
    }

    public Lyrics lyricsFor(NowPlaying now) {
        TrackLyrics current = this.lyrics;
        Poller running = this.poller;
        if (current != null && running != null && wantsLyrics(now) && current.title().equals(now.title()) && current.artist().equals(now.artist())) {
            return running.lyricsService.lookup(current.query());
        }
        return null;
    }

    public boolean wantsLyrics(NowPlaying now) {
        return this.showLyrics.get() && now != null && now.lyricsEligible();
    }

    public boolean showArtist() {
        return this.showArtist.get();
    }

    public boolean showAlbumArt() {
        return this.showAlbumArt.get();
    }

    public boolean showProgress() {
        return this.showProgress.get();
    }

    public boolean scrollTitles() {
        return this.scrollTitles.get();
    }

    public boolean showLyrics() {
        return this.showLyrics.get();
    }

    public int lyricLines() {
        try {
            return Math.max(1, Math.min(3, Integer.parseInt(this.lyricLines.get())));
        } catch (NumberFormatException e) {
            return 2;
        }
    }

    public LyricMotion lyricMotion() {
        switch (this.lyricMotion.get()) {
            case "Glide":
                return LyricMotion.GLIDE;
            case "Fade":
                return LyricMotion.FADE;
            default:
                return LyricMotion.WAVE;
        }
    }

    public LyricHighlight lyricHighlight() {
        switch (this.lyricHighlight.get()) {
            case "Line":
                return LyricHighlight.LINE;
            case "Off":
                return LyricHighlight.OFF;
            default:
                return LyricHighlight.KARAOKE;
        }
    }

    public boolean lyricsOnline() {
        return this.lyricsOnline.get();
    }

    public float cardWidth() {
        return this.cardWidth.get();
    }

    public int titleColor() {
        return this.titleColor.argb();
    }

    public int artistColor() {
        return this.artistColor.argb();
    }

    public int lyricsColor() {
        return this.lyricsColor.argb();
    }

    public int upcomingLyricsColor() {
        return this.upcomingLyricsColor.argb();
    }

    public int highlightColor() {
        return this.highlightColor.argb();
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/client/SpotifyHudModule$Poller.class */
    @Environment(EnvType.CLIENT)
    private final class Poller {
        final ScheduledThreadPoolExecutor executor;
        final LyricsService lyricsService = new LyricsService();
        private MediaSession media;
        private boolean mediaFailed;
        private SpotifyWindow window;
        private boolean windowFailed;
        private long nextWindowReadAt;
        private AlbumArt art;
        private int artVersion;
        private boolean artDecoded;
        private LyricsQuery lyricsCandidate;
        private int lyricsCandidatePolls;
        private LyricsQuery lyricsQuery;
        private TrackLyrics published;
        private int sessionFailures;
        private String lastError;
        private long lastErrorLoggedAt;

        Poller() {
            this.lyricsService.setOnline(SpotifyHudModule.this.lyricsOnline.get());
            this.executor = new ScheduledThreadPoolExecutor(1, runnable -> {
                Thread thread = new Thread(runnable, "Anubis-Spotify");
                thread.setDaemon(true);
                return thread;
            });
            this.executor.setRemoveOnCancelPolicy(true);
            this.executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        }

        void start() {
            this.executor.scheduleWithFixedDelay(this::poll, 0L, SpotifyHudModule.POLL_INTERVAL_MS, TimeUnit.MILLISECONDS);
        }

        void stop() {
            try {
                this.executor.execute(this::closeMedia);
            } catch (RejectedExecutionException e) {
            }
            this.executor.shutdown();
            this.lyricsService.close();
        }

        private void closeMedia() {
            if (this.media != null) {
                this.media.close();
            }
        }

        boolean press(SpotifyWindow.MediaKey key) {
            try {
                this.executor.execute(() -> {
                    control(key);
                });
                this.executor.schedule(this::poll, SpotifyHudModule.REPOLL_AFTER_KEY_MS, TimeUnit.MILLISECONDS);
                return true;
            } catch (RejectedExecutionException e) {
                return false;
            }
        }

        private void poll() {
            if (SpotifyHudModule.this.poller != this) {
                return;
            }
            try {
                NowPlaying next = readSession();
                if (next == null) {
                    if (this.sessionFailures > 0 && this.sessionFailures <= 2 && SpotifyHudModule.this.nowPlaying.source() == NowPlaying.Source.SESSION) {
                        return;
                    }
                    long now = System.currentTimeMillis();
                    if (SpotifyHudModule.this.nowPlaying.source() != NowPlaying.Source.SESSION && now < this.nextWindowReadAt) {
                        return;
                    }
                    this.nextWindowReadAt = now + SpotifyHudModule.WINDOW_POLL_MS;
                    next = readWindow();
                }
                if (next != null) {
                    SpotifyHudModule.this.publish(this, next, lyricsFor(next));
                }
            } catch (LinkageError | RuntimeException failure) {
                SpotifyHudModule.LOGGER.debug("[Anubis] Spotify HUD poll failed", failure);
            }
        }

        private NowPlaying readSession() {
            if (this.mediaFailed) {
                return null;
            }
            if (this.media == null) {
                try {
                    this.media = MediaSession.spotify();
                } catch (LinkageError failure) {
                    SpotifyHudModule.LOGGER.warn("[Anubis] Spotify HUD: media session unavailable ({})", failure.toString());
                    this.mediaFailed = true;
                    return null;
                }
            }
            if (this.media.isUnsupported()) {
                return null;
            }
            MediaSession.Snapshot snapshot = this.media.poll();
            if (snapshot == null) {
                String error = this.media.lastError();
                logError(error);
                this.sessionFailures = (error == null || this.media.isUnsupported()) ? 0 : this.sessionFailures + 1;
                return null;
            }
            this.sessionFailures = 0;
            return NowPlaying.fromSession(snapshot, artFor(snapshot), SpotifyHudModule.this.nowPlaying);
        }

        private AlbumArt artFor(MediaSession.Snapshot snapshot) {
            if (!SpotifyHudModule.this.showAlbumArt.get()) {
                this.art = null;
                this.artDecoded = false;
                return null;
            }
            if (!this.artDecoded || snapshot.thumbnailVersion() != this.artVersion) {
                this.artDecoded = true;
                this.artVersion = snapshot.thumbnailVersion();
                this.art = null;
                if (snapshot.hasThumbnail()) {
                    try {
                        this.art = AlbumArt.decode(snapshot.thumbnail(), 256);
                    } catch (LinkageError | RuntimeException failure) {
                        SpotifyHudModule.LOGGER.debug("[Anubis] Spotify HUD: album art could not be decoded", failure);
                    }
                }
            }
            return this.art;
        }

        private NowPlaying readWindow() {
            if (!this.windowFailed && this.window == null) {
                try {
                    this.window = new SpotifyWindow();
                } catch (LinkageError failure) {
                    windowFailed(failure);
                }
            }
            if (this.window != null) {
                try {
                    return NowPlaying.fromWindowTitle(this.window.readTitle(), SpotifyHudModule.this.nowPlaying);
                } catch (LinkageError failure2) {
                    windowFailed(failure2);
                }
            }
            if (this.media != null && !this.mediaFailed && !this.media.isUnsupported()) {
                return NowPlaying.NOT_RUNNING;
            }
            this.executor.shutdown();
            return NowPlaying.UNSUPPORTED;
        }

        private void windowFailed(LinkageError failure) {
            SpotifyHudModule.LOGGER.warn("[Anubis] Spotify HUD: Win32 access unavailable ({})", failure.toString());
            this.window = null;
            this.windowFailed = true;
        }

        private TrackLyrics lyricsFor(NowPlaying now) {
            if (!SpotifyHudModule.this.wantsLyrics(now)) {
                this.lyricsCandidate = null;
                this.lyricsCandidatePolls = 0;
                this.lyricsQuery = null;
                this.published = null;
                return null;
            }
            LyricsQuery query = new LyricsQuery(now.title(), now.artist(), now.album(), 0).withDurationMillis(now.durationMs());
            if (query.equals(this.lyricsCandidate)) {
                this.lyricsCandidatePolls++;
            } else {
                this.lyricsCandidate = query;
                this.lyricsCandidatePolls = 1;
            }
            if (this.lyricsCandidatePolls >= 2) {
                this.lyricsQuery = this.lyricsCandidate;
            }
            if (this.lyricsQuery == null || !this.lyricsQuery.title().equals(now.title()) || !this.lyricsQuery.artist().equals(now.artist())) {
                return this.published;
            }
            if (this.published == null || !this.published.query().equals(this.lyricsQuery) || !this.published.title().equals(now.title()) || !this.published.artist().equals(now.artist())) {
                this.published = new TrackLyrics(now.title(), now.artist(), this.lyricsQuery);
            }
            return this.published;
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
        private void control(SpotifyWindow.MediaKey key) throws MatchException {
            boolean zSkipPrevious;
            if (SpotifyHudModule.this.poller != this) {
                return;
            }
            if (this.media != null && SpotifyHudModule.this.nowPlaying.source() == NowPlaying.Source.SESSION) {
                switch (key) {
                    case PLAY_PAUSE:
                        zSkipPrevious = this.media.togglePlayPause();
                        break;
                    case NEXT:
                        zSkipPrevious = this.media.skipNext();
                        break;
                    case PREVIOUS:
                        zSkipPrevious = this.media.skipPrevious();
                        break;
                    default:
                        throw new MatchException((String) null, (Throwable) null);
                }
                boolean accepted = zSkipPrevious;
                if (accepted || this.media.lastError() == null) {
                    return;
                } else {
                    SpotifyHudModule.LOGGER.debug("[Anubis] Spotify HUD: media session control failed ({}); sending the media key", this.media.lastError());
                }
            }
            try {
                SpotifyWindow.tap(key);
            } catch (LinkageError | RuntimeException failure) {
                SpotifyHudModule.LOGGER.debug("[Anubis] Spotify HUD media key failed", failure);
            }
        }

        private void logError(String error) {
            if (error == null) {
                return;
            }
            long now = System.currentTimeMillis();
            if (!Objects.equals(error, this.lastError) || now - this.lastErrorLoggedAt >= SpotifyHudModule.ERROR_LOG_INTERVAL_MS) {
                this.lastError = error;
                this.lastErrorLoggedAt = now;
                SpotifyHudModule.LOGGER.debug("[Anubis] Spotify HUD media session: {}", error);
            }
        }
    }

    private void onTick(TickEvent event) {
        class_310 mc = class_310.method_1551();
        boolean accept = this.mediaKeys.get() && mc.field_1755 == null && mc.method_1569() && mc.field_1724 != null;
        for (int i = 0; i < this.keys.length; i++) {
            boolean down = isDown(mc, this.keys[i].value());
            if (down && !this.wasDown[i] && accept) {
                press(this.actions[i]);
            }
            this.wasDown[i] = down;
        }
    }

    private void press(SpotifyWindow.MediaKey key) {
        long now = System.currentTimeMillis();
        Poller running = this.poller;
        if (running == null || now - this.lastKeyAt < KEY_COOLDOWN_MS) {
            return;
        }
        this.lastKeyAt = now;
        if (running.press(key) && key == SpotifyWindow.MediaKey.PLAY_PAUSE) {
            synchronized (this.publishLock) {
                if (this.poller == running) {
                    this.nowPlaying = this.nowPlaying.toggled();
                }
            }
        }
    }

    private static boolean isDown(class_310 mc, int key) {
        if (!ModuleKeybinds.isBindable(key)) {
            return false;
        }
        if (key >= 1000) {
            return GLFW.glfwGetMouseButton(mc.method_22683().method_4490(), key - ModuleKeybinds.MOUSE_OFFSET) == 1;
        }
        return class_3675.method_15987(mc.method_22683(), key);
    }
}
