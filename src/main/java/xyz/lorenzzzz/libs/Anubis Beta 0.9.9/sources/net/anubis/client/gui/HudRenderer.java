package net.anubis.client.gui;

import com.example.client.mixin.AccessorHudToastInstance;
import com.mojang.blaze3d.textures.GpuTexture;
import imgui.ImDrawList;
import imgui.ImFont;
import imgui.ImFontGlyph;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.nio.ByteBuffer;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;
import net.anubis.client.config.HudTheme;
import net.anubis.client.config.Surface;
import net.anubis.client.imgui.ImGuiManager;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.HudOverlayProvider;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.ModuleRevision;
import net.anubis.client.module.impl.client.HotkeysModule;
import net.anubis.client.module.impl.client.HudModule;
import net.anubis.client.module.impl.client.RadarModule;
import net.anubis.client.module.impl.client.RegionMapModule;
import net.anubis.client.module.impl.client.SpotifyHudModule;
import net.anubis.client.module.impl.misc.AntiVanishModule;
import net.anubis.client.module.impl.misc.NameProtectModule;
import net.anubis.client.module.impl.misc.StaffListModule;
import net.anubis.client.util.spotify.AlbumArt;
import net.anubis.client.util.spotify.DisplayText;
import net.anubis.client.util.spotify.Lyrics;
import net.anubis.client.util.spotify.NowPlaying;
import net.anubis.client.util.spotify.PlaybackClock;
import net.anubis.client.util.spotify.SpotifyWindow;
import net.anubis.client.util.spotify.SyncedLyrics;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1041;
import net.minecraft.class_1068;
import net.minecraft.class_1074;
import net.minecraft.class_10868;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_368;
import net.minecraft.class_408;
import net.minecraft.class_634;
import net.minecraft.class_640;
import net.minecraft.class_642;
import net.minecraft.class_6880;
import net.minecraft.class_742;
import net.minecraft.class_746;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer.class */
@Environment(EnvType.CLIENT)
public final class HudRenderer {
    private static final float CAP_H = 28.0f;
    private static final float PAD_X = 12.0f;
    private static final float GAP_UNIT = 4.0f;
    private static final float GAP_DIV = 10.0f;
    private static final float DIV_H = 12.0f;
    private static final float EMBLEM_H = 24.0f;
    static final float ANIM_SPEED = 12.0f;
    private static final float WIDTH_SPEED = 16.0f;
    private static final float INTRO_DURATION = 0.36f;
    private static final float INTRO_STAGGER = 0.04f;
    private static final float INTRO_SLIDE = 6.0f;
    private static final String MINUS = "−";
    private static final String INFINITY = "∞";
    private int slotCount;
    private float moduleListW;
    private float toastShift;
    private boolean layoutDirty;
    private long lastWheelNanos;
    private float introTime;
    private boolean hudVisibleLastFrame;
    private String draggingId;
    private float dragOffsetX;
    private boolean spotifyButtonsShown;
    private boolean spotifyButtonsDrawn;
    private boolean chatInput;
    private float dragOffsetY;
    private Float snapGuideX;
    private Float snapGuideY;
    private static final float HIDDEN_ALPHA = 0.35f;
    private static final float ROWS_RADIUS = 6.0f;
    private static final int ROWS_SHADOW_LAYERS = 3;
    private static final int ROWS_TEXT = -1;
    private static final float COORD_ROW_H = 20.0f;
    private static final float COORD_PAD = 7.0f;
    private static final float COORD_ICON = 16.0f;
    private static final float COORD_GAP_ICON = 8.0f;
    private static final float COORD_GAP = 12.0f;
    private static final float COORD_ELSEWHERE_ALPHA = 0.62f;
    private static final float LIST_ROW_H = 18.0f;
    private static final float LIST_PAD = 6.0f;
    private static final String NO_MODULES = "No active modules";
    private static final float TOAST_GAP = 10.0f;
    private static final float POTION_ROW_H = 22.0f;
    private static final float POTION_PAD = 6.0f;
    private static final float ICON = 18.0f;
    private static final float GAP_ICON = 6.0f;
    private static final float GAP_DURATION = 14.0f;
    private static final String NO_EFFECTS = "No active effects";
    private static final float VANISH_ROW_H = 22.0f;
    private static final float VANISH_PAD = 8.0f;
    private static final float GAP_TAG = 8.0f;
    private static final String NO_VANISH = "No vanish alerts";
    private static final float STAFF_ROW_H = 22.0f;
    private static final float STAFF_HEADER_H = 20.0f;
    private static final float STAFF_PAD = 8.0f;
    private static final float STAFF_FACE = 16.0f;
    private static final float GAP_FACE = 6.0f;
    private static final float GAP_COUNT = 5.0f;
    private static final float GAP_STATE = 14.0f;
    private static final String NO_STAFF = "No staff online";
    private static final String STAFF_HEADER = "STAFF";
    private static final String WATCHING_YOU = "WATCHING YOU";
    private static final float SKIN_FACE_U = 0.125f;
    private static final float SKIN_HAT_U = 0.625f;
    private static final float SKIN_TOP_V = 0.125f;
    private static final float SKIN_PART = 0.125f;
    private static final float RADAR_DEFAULT_SIZE = 150.0f;
    private static final float RADAR_LETTER_INSET = 10.0f;
    private static final float RADAR_LETTER_CLEAR = 4.0f;
    private static final float RADAR_EDGE = 5.0f;
    private static final float RADAR_SELF = 3.0f;
    private static final float RADAR_MARKER_INSET = 1.5f;
    private static final float RADAR_MARKER_H = 5.0f;
    private static final float RADAR_MARKER_W = 3.5f;
    private static final float RADAR_DOT = 3.2f;
    private static final int RADAR_DOT_EDGE = -1946157056;
    private float radarLetterMid;
    private float radarLetterHalf;
    private ImFont radarLetterFont;
    private static final float HOTKEY_ROW_H = 22.0f;
    private static final float HOTKEY_HEADER_H = 20.0f;
    private static final float HOTKEY_PAD = 8.0f;
    private static final float GAP_HOTKEY = 16.0f;
    private static final float KEYCAP_H = 16.0f;
    private static final float KEYCAP_PAD = 5.0f;
    private static final float KEYCAP_RADIUS = 4.0f;
    private static final String HOTKEYS_HEADER = "HOTKEYS";
    private static final String NO_HOTKEYS = "No keys bound";
    private ImFont hotkeyFont;
    private float hotkeyNameColumn;
    private float hotkeyKeyColumn;
    private float hotkeyHeaderW;
    private float hotkeyCapMid;
    private static final float SPOTIFY_PAD_Y = 8.0f;
    private static final float SPOTIFY_LINE_GAP = 2.0f;
    private static final float SPOTIFY_GLYPH = 10.0f;
    private static final float SPOTIFY_GLYPH_GAP = 10.0f;
    private static final float SPOTIFY_PAUSED = 0.62f;
    private static final float MARQUEE_SPEED = 30.0f;
    private static final float MARQUEE_HOLD = 2.4f;
    private static final float MARQUEE_GAP = 40.0f;
    private static final String ELLIPSIS = "...";
    private static final float CARD_PAD = 10.0f;
    private static final float CARD_MIN_WIDTH = 200.0f;
    private static final float CARD_DEFAULT_WIDTH = 320.0f;
    private static final float ART_GAP = 10.0f;
    private static final float ART_MIN_COLUMN = 128.0f;
    private static final float ART_RADIUS = 4.0f;
    private static final int ART_PLACEHOLDER = 318767103;
    private static final float LABEL_GAP = 3.0f;
    private static final float ARTIST_GAP = 1.0f;
    private static final float TRANSPORT_GAP = 7.0f;
    private static final float PROGRESS_GAP = 6.0f;
    private static final float PROGRESS_H = 2.0f;
    private static final float SKIP_GLYPH = 8.0f;
    private static final float PLAY_GLYPH = 10.0f;
    private static final float GLYPH_SPACING = 14.0f;
    private static final float GLYPH_MARGIN = 8.0f;
    private static final float LYRICS_GAP = 12.0f;
    private static final float LYRIC_ROW_GAP = 6.0f;
    private static final float LYRIC_LINE_GAP = 6.0f;
    private static final float LYRIC_SPRING = 13.0f;
    private static final float LYRIC_SETTLE_TIME = 1.1f;
    private static final float LYRIC_STAGGER = 0.045f;
    private static final int LYRIC_MAX_STAGGERED = 6;
    private static final int LYRIC_MAX_SHIFTS = 8;
    private static final float LYRIC_GLOW_TIME = 0.35f;
    private static final float LYRIC_ARRIVE_TIME = 0.3f;
    private static final float LYRIC_FADE_TIME = 0.32f;
    private static final float LYRIC_RISE = 8.0f;
    private static final int LYRIC_MAX_ROWS = 4;
    private static final float LYRIC_GROW_SPEED = 14.0f;
    private static final float LYRIC_SHRINK_SPEED = 9.0f;
    private static final float LYRIC_FAR_ALPHA = 0.6f;
    private static final float LYRIC_PAUSED_ALPHA = 0.7f;
    private static final float LYRIC_PAUSE_SPEED = 8.0f;
    private static final float LYRIC_BREATH_PERIOD = 1.6f;
    private static final long LYRIC_LEAD_MS = 200;
    private static final long LYRIC_SWEEP_MS = 180;
    private static final float LYRIC_SLOW_PACE = 2.0f;
    private static final float LYRIC_CHAR_MS = 90.0f;
    private static final float LYRIC_LIT_ALL = Float.POSITIVE_INFINITY;
    private static final long LYRIC_QUIET_MS = 700;
    private static final float LYRIC_MIN_COVERAGE = 0.75f;
    private float marqueeTime;
    private NowPlaying spotifyFolded;
    private AlbumArt spotifyArt;
    private ThemeImages.Texture spotifyArtTexture;
    private Lyrics lyricPrepared;
    private int lyricRowsPx;
    private int lyricRowsWidth;
    private int lyricViewLines;
    private float lyricViewScale;
    private SyncedLyrics lyricSource;
    private int lyricStart;
    private float lyricClock;
    private List<LyricItem> lyricFadeItems;
    private List<LyricItem> lyricLeaveItems;
    private float lyricLeave;
    private float lyricBreath;
    private int lyricBrightLo;
    private float lyricBlockH;
    private long lyricLoadingSince;
    private ImFont glyphFont;
    private static final float SNAP_THRESHOLD = 6.0f;
    private static final HudRenderer INSTANCE = new HudRenderer();
    private static final DateTimeFormatter CLOCK_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private static final String[] SECONDS = (String[]) IntStream.range(0, 60).mapToObj(i -> {
        return String.format(":%02d", Integer.valueOf(i));
    }).toArray(x$0 -> {
        return new String[x$0];
    });
    private static final SpotifyWindow.MediaKey[] SPOTIFY_BUTTONS = {SpotifyWindow.MediaKey.PREVIOUS, SpotifyWindow.MediaKey.PLAY_PAUSE, SpotifyWindow.MediaKey.NEXT};
    private static int studioFrame = -2;
    private static final String[] COORD_SLOTS = {"coords.0", "coords.1", "coords.2"};
    private static final float[] RADAR_RINGS = {0.33333334f, 0.6666667f};
    private static final String[] RADAR_LETTERS = {"N", "E", "S", "W"};
    private static final int[] RADAR_LETTER_DX = {0, 1, 0, -1};
    private static final int[] RADAR_LETTER_DZ = {-1, 0, 1, 0};
    private static final String[] NO_ROWS = new String[0];
    private static final NowPlaying SAMPLE_TRACK = new NowPlaying(NowPlaying.Status.PLAYING, "Golden Hour Drive", "Sample Artist", "", NowPlaying.Source.SESSION, 214000, 84000, 0, 1.0d, null);
    private static final Lyrics SAMPLE_LYRICS = new Lyrics(Lyrics.Status.SYNCED, SyncedLyrics.parse("[01:21.00]Every light in the city knows my name\n[01:27.40]We keep on driving till the morning comes\n[01:33.80]Nothing but the radio and you\n"), List.of(), 0);
    private final HudLayoutStore layout = HudLayoutStore.getInstance();
    private final ModuleListRows moduleRows = new ModuleListRows();
    private final TargetCard targetCard = new TargetCard();
    private final ArmorStatus armorStatus = new ArmorStatus();
    private final CooldownTray cooldownTray = new CooldownTray();
    private final Map<String, Int2ObjectOpenHashMap<StableWidth>> slots = new HashMap();
    private final float[] spotifyButtons = new float[12];
    private long infoSecond = Long.MIN_VALUE;
    private int infoFps = -1;
    private int infoPing = -1;
    private List<InfoItem> infoItems = List.of();
    private final Map<String, Float> coordAnim = new HashMap();
    private final Map<UUID, class_2960> staffSkins = new HashMap();
    private final Set<GpuTexture> nearestSkins = Collections.newSetFromMap(new WeakHashMap());
    private final float[] radarLetterW = new float[4];
    private int radarLetterPx = -1;
    private List<HotkeyLine> hotkeyLines = List.of();
    private long hotkeyRevision = Long.MIN_VALUE;
    private int hotkeyTextPx = -1;
    private int hotkeyKeyPx = -1;
    private String marqueeTitle = "";
    private String marqueeArtist = "";
    private final PlaybackClock spotifyClock = new PlaybackClock();
    private String spotifyTitle = "";
    private String spotifyArtist = "";
    private final Map<Long, Float> capTops = new HashMap();
    private String[] lyricTexts = NO_ROWS;
    private String[][] lyricRows = new String[0][];
    private LyricWords[] lyricWords = new LyricWords[0];
    private float lyricCharMs = LYRIC_CHAR_MS;
    private boolean lyricReadable = true;
    private float lyricViewH = -1.0f;
    private final List<LyricShift> lyricShifts = new ArrayList(8);
    private float lyricFade = 1.0f;
    private List<LyricItem> lyricLastItems = List.of();
    private float[] lyricShown = new float[0];
    private float lyricPlaying = 1.0f;
    private int lyricCurrentFirst = -1;
    private int lyricCurrentLast = -1;
    private float[] lyricBright = new float[0];
    private int lyricBrightHi = -1;
    private final Map<Integer, Boolean> glyphs = new HashMap();
    private final HudModule hudModule = (HudModule) ModuleRegistry.get(HudModule.class);
    private final RegionMapModule regionMapModule = (RegionMapModule) ModuleRegistry.get(RegionMapModule.class);
    private final RadarModule radarModule = (RadarModule) ModuleRegistry.get(RadarModule.class);

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$SpotifyGlyph.class */
    @Environment(EnvType.CLIENT)
    private enum SpotifyGlyph {
        NONE,
        PLAYING,
        PAUSED
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$SpotifyLook.class */
    @Environment(EnvType.CLIENT)
    private enum SpotifyLook {
        HIDDEN,
        IDLE,
        COMPACT,
        CARD
    }

    private HudRenderer() {
    }

    public static HudRenderer getInstance() {
        return INSTANCE;
    }

    static void markStudioPreview() {
        studioFrame = ImGuiManager.getInstance().frameCount();
    }

    static boolean studioPreview() {
        int age = ImGuiManager.getInstance().frameCount() - studioFrame;
        return age == 0 || age == 1;
    }

    boolean isDragging() {
        return this.draggingId != null;
    }

    void studioInput(float screenW, float screenH, boolean blocked) {
        markStudioPreview();
        if (blocked && this.draggingId == null) {
            return;
        }
        updateEditorInput(screenW, screenH);
    }

    public void render() {
        class_310 client = class_310.method_1551();
        if (client.field_1687 == null) {
            releaseSpotifyArt();
            resetCombatElements();
            return;
        }
        boolean editorScreen = client.field_1755 instanceof HudEditorScreen;
        boolean preview = studioPreview();
        boolean editMode = editorScreen || preview;
        if ((client.field_1755 instanceof ImGuiScreen) && !preview) {
            if (this.draggingId != null || this.layoutDirty) {
                saveLayout();
            }
            this.draggingId = null;
            this.snapGuideX = null;
            this.snapGuideY = null;
            this.hudVisibleLastFrame = false;
            return;
        }
        boolean chatEditMode = client.field_1755 instanceof class_408;
        boolean acceptInput = editorScreen || chatEditMode;
        this.chatInput = chatEditMode;
        this.spotifyButtonsShown = this.spotifyButtonsDrawn;
        this.spotifyButtonsDrawn = false;
        boolean showHudOverlays = shouldRenderHudOverlays(client);
        float dt = Math.max(ImGui.getIO().getDeltaTime(), 0.001f);
        float screenW = ImGui.getIO().getDisplaySizeX();
        float screenH = ImGui.getIO().getDisplaySizeY();
        flushLayoutIfIdle();
        boolean hasHudOverlays = computeHudOverlays();
        boolean regionMapEnabled = isRegionMapEnabled();
        HudLayoutStore.HudElementState regionMap = this.layout.element(HudLayoutStore.REGION_MAP_ID);
        boolean regionMapShown = regionMapEnabled && regionMap.visible;
        boolean hudVisible = this.hudModule != null && (editMode || this.hudModule.isEnabled());
        if (!hudVisible) {
            this.hudVisibleLastFrame = false;
            this.introTime = 0.0f;
            clearSlots();
            this.moduleListW = 0.0f;
            this.toastShift = 0.0f;
            releaseSpotifyArt();
            resetCombatElements();
            if ((hasHudOverlays || regionMapShown) && showHudOverlays) {
                drawOverlayOnly(screenW, screenH, chatEditMode, regionMapShown);
                return;
            }
            return;
        }
        boolean showPing = editMode || this.hudModule.ping();
        boolean showFps = editMode || this.hudModule.fps();
        boolean showClock = editMode || this.hudModule.clock();
        boolean shadow = this.hudModule.shadow();
        updateIntro(editMode, dt);
        this.moduleRows.update(ModuleRegistry.enabled(), editMode, dt);
        if (this.slotCount > 256) {
            clearSlots();
        }
        HudLayoutStore.HudElementState infoBar = this.layout.element(HudLayoutStore.INFO_BAR_ID);
        HudLayoutStore.HudElementState coordinates = this.layout.element(HudLayoutStore.COORDINATES_ID);
        HudLayoutStore.HudElementState moduleList = this.layout.element(HudLayoutStore.MODULE_LIST_ID);
        HudLayoutStore.HudElementState potionTracker = this.layout.element(HudLayoutStore.POTION_TRACKER_ID);
        HudLayoutStore.HudElementState antiVanish = this.layout.element(HudLayoutStore.ANTI_VANISH_ID);
        HudLayoutStore.HudElementState staffList = this.layout.element(HudLayoutStore.STAFF_LIST_ID);
        HudLayoutStore.HudElementState spotify = this.layout.element(HudLayoutStore.SPOTIFY_ID);
        HudLayoutStore.HudElementState radar = this.layout.element(HudLayoutStore.RADAR_ID);
        HudLayoutStore.HudElementState hotkeys = this.layout.element(HudLayoutStore.HOTKEYS_ID);
        HudLayoutStore.HudElementState target = this.layout.element(HudLayoutStore.TARGET_CARD_ID);
        HudLayoutStore.HudElementState armor = this.layout.element(HudLayoutStore.ARMOR_STATUS_ID);
        HudLayoutStore.HudElementState cooldowns = this.layout.element(HudLayoutStore.ITEM_COOLDOWNS_ID);
        InfoPlan infoPlan = planInfoBar(infoBar, buildInfoItems(showFps, showClock, showPing, client), dt);
        this.layout.updateBounds(infoBar.id, infoPlan.width(), infoPlan.height(), screenW, screenH);
        CoordPlan coordsPlan = planCoordinates(coordinates, coordinateRows(client, editMode, dt), editMode, dt);
        this.layout.updateBounds(coordinates.id, coordsPlan.width(), coordsPlan.height(), screenW, screenH);
        ListPlan listPlan = planModuleList(moduleList, editMode, dt);
        this.layout.updateBounds(moduleList.id, listPlan.width(), listPlan.height(), screenW, screenH);
        PotionPlan potionPlan = planPotions(potionTracker, buildPotionRows(client), editMode, dt);
        this.layout.updateBounds(potionTracker.id, potionPlan.width(), potionPlan.height(), screenW, screenH);
        VanishPlan vanishPlan = planVanish(antiVanish, editMode, dt);
        this.layout.updateBounds(antiVanish.id, vanishPlan.width(), vanishPlan.height(), screenW, screenH);
        StaffPlan staffPlan = planStaff(staffList, editMode, dt);
        this.layout.updateBounds(staffList.id, staffPlan.width(), staffPlan.height(), screenW, screenH);
        SpotifyPlan spotifyPlan = planSpotify(spotify, editMode, dt);
        this.layout.updateBounds(spotify.id, spotifyPlan.width(), spotifyPlan.height(), screenW, screenH);
        float radarSize = planRadar(radar, editMode);
        this.layout.updateBounds(radar.id, radarSize, radarSize, screenW, screenH);
        HotkeyPlan hotkeyPlan = planHotkeys(hotkeys, editMode, dt);
        this.layout.updateBounds(hotkeys.id, hotkeyPlan.width(), hotkeyPlan.height(), screenW, screenH);
        boolean combatHud = this.hudModule.isEnabled() && !client.field_1690.field_1842;
        boolean targetWanted = combatHud && target.visible && this.hudModule.targetCard();
        boolean armorWanted = combatHud && armor.visible && this.hudModule.armorStatus();
        boolean cooldownWanted = combatHud && cooldowns.visible && this.hudModule.itemCooldowns();
        float targetScale = scaleOf(target);
        float armorScale = scaleOf(armor);
        float cooldownScale = scaleOf(cooldowns);
        this.targetCard.update(client, this.hudModule, targetWanted, editMode, dt);
        this.layout.updateBounds(target.id, this.targetCard.planWidth(targetScale), this.targetCard.planHeight(targetScale), screenW, screenH);
        this.armorStatus.update(client, this.hudModule, armorWanted, editMode, dt);
        this.layout.updateBounds(armor.id, this.armorStatus.planWidth(armorScale), this.armorStatus.planHeight(armorScale), screenW, screenH);
        this.cooldownTray.update(client, this.hudModule, cooldownWanted, editMode, dt);
        this.layout.updateBounds(cooldowns.id, this.cooldownTray.planWidth(cooldownScale), this.cooldownTray.planHeight(cooldownScale), screenW, screenH);
        if (regionMapEnabled || editMode) {
            updateRegionMapBounds(screenW, screenH);
        }
        if (acceptInput) {
            updateEditorInput(screenW, screenH);
        } else if (!preview) {
            if (this.draggingId != null || this.layoutDirty) {
                saveLayout();
            }
            this.draggingId = null;
            this.snapGuideX = null;
            this.snapGuideY = null;
        }
        float listShift = updateToastShift(client, moduleList, editMode, dt);
        ImGui.setNextWindowPos(0.0f, 0.0f, 1);
        ImGui.setNextWindowSize(screenW, screenH);
        int flags = 205215;
        if (!acceptInput) {
            flags = 205215 | ImGuiWindowFlags.NoInputs;
        }
        ImGui.pushStyleVar(4, 0.0f);
        if (!ImGui.begin("##hud_overlay", flags)) {
            ImGui.end();
            ImGui.popStyleVar();
            return;
        }
        ImGui.popStyleVar();
        ImDrawList dl = ImGui.getWindowDrawList();
        if (showHudOverlays) {
            drawHudOverlays(dl, screenW, screenH);
        }
        boolean hudOn = this.hudModule.isEnabled();
        boolean infoBarShown = hudOn && infoBar.visible && (this.hudModule.fps() || this.hudModule.clock() || this.hudModule.ping());
        boolean coordsShown = hudOn && coordinates.visible;
        boolean listShown = hudOn && moduleList.visible && this.hudModule.moduleList();
        boolean potionsShown = hudOn && potionTracker.visible && this.hudModule.potions();
        boolean vanishShown = hudOn && antiVanish.visible && AntiVanishModule.shouldShowHud();
        boolean staffShown = hudOn && staffList.visible && StaffListModule.shouldShowHud();
        boolean spotifyShown = hudOn && spotify.visible && SpotifyHudModule.shouldShowHud();
        boolean gameHidden = gameHidesHud(client);
        boolean radarShown = hudOn && radar.visible && RadarModule.shouldShowHud() && !gameHidden;
        boolean hotkeysShown = hudOn && hotkeys.visible && HotkeysModule.shouldShowHud() && !gameHidden;
        if ((infoBarShown && !infoPlan.segments().isEmpty()) || editMode) {
            drawInfoBar(dl, infoBar, infoPlan, shadow, transform(infoBar, editMode, infoBarShown));
        }
        if ((coordsShown && !coordsPlan.rows().isEmpty()) || editMode) {
            drawCoordinates(dl, coordinates, coordsPlan, shadow, transform(coordinates, editMode, coordsShown), screenW);
        }
        if ((listShown && !listPlan.rows().isEmpty()) || editMode) {
            drawModuleList(dl, moduleList, listPlan, shadow, transform(moduleList, editMode, listShown).down(listShift), screenW);
        }
        if ((potionsShown && !potionPlan.lines().isEmpty()) || editMode) {
            drawPotions(dl, potionTracker, potionPlan, shadow, transform(potionTracker, editMode, potionsShown));
        }
        if ((vanishShown && !vanishPlan.lines().isEmpty()) || editMode) {
            drawVanish(dl, antiVanish, vanishPlan, shadow, transform(antiVanish, editMode, vanishShown));
        }
        if (staffShown || editMode) {
            drawStaff(dl, staffList, staffPlan, shadow, transform(staffList, editMode, staffShown));
        }
        if (spotifyShown || editMode) {
            drawSpotify(dl, spotify, spotifyPlan, shadow, transform(spotify, editMode, spotifyShown));
        }
        if (radarShown || editMode) {
            drawRadar(dl, radar, radarSize, client, shadow, transform(radar, editMode, radarShown));
        }
        if ((hotkeysShown && !hotkeyPlan.lines().isEmpty()) || editMode) {
            drawHotkeys(dl, hotkeys, hotkeyPlan, shadow, transform(hotkeys, editMode, hotkeysShown));
        }
        if (this.targetCard.visible()) {
            DrawTransform tr = transform(target, editMode, targetWanted);
            this.targetCard.draw(dl, target, Math.round(target.x + tr.xOffset()), Math.round(target.y + tr.yOffset()), tr.alpha(), shadow, this.hudModule);
        }
        if (this.armorStatus.visible()) {
            DrawTransform tr2 = transform(armor, editMode, armorWanted);
            this.armorStatus.draw(dl, armor, Math.round(armor.x + tr2.xOffset()), Math.round(armor.y + tr2.yOffset()), tr2.alpha(), shadow, client);
        }
        if (this.cooldownTray.visible()) {
            DrawTransform tr3 = transform(cooldowns, editMode, cooldownWanted);
            this.cooldownTray.draw(dl, cooldowns, Math.round(cooldowns.x + tr3.xOffset()), Math.round(cooldowns.y + tr3.yOffset()), tr3.alpha(), shadow, hudAccent());
        }
        if (regionMapShown || editMode) {
            RegionMapRenderer.render(dl, this.regionMapModule, regionMap, (!editMode || regionMapShown) ? 1.0f : 0.35f);
        }
        if (editorScreen) {
            drawEditorChrome(dl, screenW, screenH);
        } else if (preview) {
            drawSnapGuides(dl, screenW, screenH);
        }
        ImGui.end();
    }

    private DrawTransform transform(HudLayoutStore.HudElementState state, boolean editMode, boolean shownInGame) {
        DrawTransform tr = introTransform(state, editMode);
        return (!editMode || shownInGame) ? tr : new DrawTransform(tr.xOffset(), tr.yOffset(), tr.alpha() * 0.35f);
    }

    private void resetCombatElements() {
        this.targetCard.reset();
        this.armorStatus.reset();
        this.cooldownTray.reset();
    }

    private void saveLayout() {
        this.layout.save();
        this.layoutDirty = false;
    }

    private void flushLayoutIfIdle() {
        if (!this.layoutDirty || System.nanoTime() - this.lastWheelNanos <= 400000000) {
            return;
        }
        saveLayout();
    }

    private void drawOverlayOnly(float screenW, float screenH, boolean acceptInput, boolean drawRegionMap) {
        if (drawRegionMap) {
            updateRegionMapBounds(screenW, screenH);
        }
        ImGui.setNextWindowPos(0.0f, 0.0f, 1);
        ImGui.setNextWindowSize(screenW, screenH);
        int flags = 205215;
        if (!acceptInput) {
            flags = 205215 | ImGuiWindowFlags.NoInputs;
        }
        ImGui.pushStyleVar(4, 0.0f);
        if (!ImGui.begin("##anubis_overlay_only", flags)) {
            ImGui.end();
            ImGui.popStyleVar();
            return;
        }
        ImGui.popStyleVar();
        if (acceptInput) {
            updateEditorInput(screenW, screenH);
        } else {
            if (this.draggingId != null || this.layoutDirty) {
                saveLayout();
            }
            this.draggingId = null;
        }
        ImDrawList dl = ImGui.getWindowDrawList();
        drawHudOverlays(dl, screenW, screenH);
        if (drawRegionMap && this.regionMapModule != null) {
            RegionMapRenderer.render(dl, this.regionMapModule, this.layout.element(HudLayoutStore.REGION_MAP_ID));
        }
        ImGui.end();
    }

    private boolean isRegionMapEnabled() {
        return this.regionMapModule != null && this.regionMapModule.isEnabled();
    }

    private void updateRegionMapBounds(float screenW, float screenH) {
        if (this.regionMapModule == null) {
            return;
        }
        HudLayoutStore.HudElementState state = this.layout.element(HudLayoutStore.REGION_MAP_ID);
        RegionMapRenderer.Size size = RegionMapRenderer.measureSize(this.regionMapModule, scaleOf(state));
        this.layout.updateBounds(HudLayoutStore.REGION_MAP_ID, size.width(), size.height(), screenW, screenH);
    }

    public boolean hasWork() {
        class_310 client = class_310.method_1551();
        if (client.field_1687 != null) {
            return (client.field_1755 instanceof HudEditorScreen) || studioPreview() || (this.hudModule != null && (this.hudModule.isEnabled() || this.hudVisibleLastFrame)) || isRegionMapEnabled() || computeHudOverlays();
        }
        resetCombatElements();
        return false;
    }

    private static boolean shouldRenderHudOverlays(class_310 client) {
        return client.field_1755 == null || (client.field_1755 instanceof class_408);
    }

    private static boolean gameHidesHud(class_310 client) {
        return client.field_1690.field_1842 || client.field_61504.method_72776();
    }

    private static boolean computeHudOverlays() {
        for (HudOverlayProvider provider : ModuleRegistry.instancesOf(HudOverlayProvider.class)) {
            if (provider.isHudOverlayEnabled()) {
                return true;
            }
        }
        return false;
    }

    private static void drawHudOverlays(ImDrawList dl, float screenW, float screenH) {
        for (HudOverlayProvider provider : ModuleRegistry.instancesOf(HudOverlayProvider.class)) {
            if (provider.isHudOverlayEnabled()) {
                provider.renderHudOverlay(dl, screenW, screenH);
            }
        }
    }

    private void updateIntro(boolean editMode, float dt) {
        if (editMode) {
            this.introTime = 0.56f;
            this.hudVisibleLastFrame = true;
        } else if (!this.hudVisibleLastFrame) {
            this.introTime = 0.0f;
            this.hudVisibleLastFrame = true;
        } else {
            this.introTime = Math.min(this.introTime + dt, 0.56f);
        }
    }

    private DrawTransform introTransform(HudLayoutStore.HudElementState state, boolean editMode) {
        if (editMode) {
            return DrawTransform.identity();
        }
        float t = clamp01((this.introTime - introDelay(state.id)) / INTRO_DURATION);
        float eased = easeOutCubic(t);
        float slide = 6.0f * scaleOf(state) * (1.0f - eased);
        switch (state.anchorX) {
            case 1:
                return new DrawTransform(0.0f, -slide, eased);
            case 2:
                return new DrawTransform(slide, 0.0f, eased);
            default:
                return new DrawTransform(-slide, 0.0f, eased);
        }
    }

    private static float introDelay(String id) {
        switch (id) {
            case "info_bar":
                return INTRO_STAGGER;
            case "coordinates":
                return 0.08f;
            case "anti_vanish":
                return 0.12f;
            case "spotify":
                return 0.12f;
            case "module_list":
                return 0.16f;
            case "staff_list":
                return 0.16f;
            case "hotkeys":
                return 0.16f;
            case "radar":
                return 0.19999999f;
            case "potion_tracker":
                return 0.19999999f;
            case "armor_status":
                return 0.16f;
            case "item_cooldowns":
                return 0.12f;
            case "target_card":
                return 0.08f;
            default:
                return 0.0f;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$Type.class */
    @Environment(EnvType.CLIENT)
    private static final class Type extends Record {
        private final ImFont value;
        private final int valuePx;
        private final ImFont text;
        private final int textPx;
        private final ImFont label;
        private final int labelPx;

        private Type(ImFont value, int valuePx, ImFont text, int textPx, ImFont label, int labelPx) {
            this.value = value;
            this.valuePx = valuePx;
            this.text = text;
            this.textPx = textPx;
            this.label = label;
            this.labelPx = labelPx;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Type.class), Type.class, "value;valuePx;text;textPx;label;labelPx", "FIELD:Lnet/anubis/client/gui/HudRenderer$Type;->value:Limgui/ImFont;", "FIELD:Lnet/anubis/client/gui/HudRenderer$Type;->valuePx:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$Type;->text:Limgui/ImFont;", "FIELD:Lnet/anubis/client/gui/HudRenderer$Type;->textPx:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$Type;->label:Limgui/ImFont;", "FIELD:Lnet/anubis/client/gui/HudRenderer$Type;->labelPx:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Type.class), Type.class, "value;valuePx;text;textPx;label;labelPx", "FIELD:Lnet/anubis/client/gui/HudRenderer$Type;->value:Limgui/ImFont;", "FIELD:Lnet/anubis/client/gui/HudRenderer$Type;->valuePx:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$Type;->text:Limgui/ImFont;", "FIELD:Lnet/anubis/client/gui/HudRenderer$Type;->textPx:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$Type;->label:Limgui/ImFont;", "FIELD:Lnet/anubis/client/gui/HudRenderer$Type;->labelPx:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Type.class, Object.class), Type.class, "value;valuePx;text;textPx;label;labelPx", "FIELD:Lnet/anubis/client/gui/HudRenderer$Type;->value:Limgui/ImFont;", "FIELD:Lnet/anubis/client/gui/HudRenderer$Type;->valuePx:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$Type;->text:Limgui/ImFont;", "FIELD:Lnet/anubis/client/gui/HudRenderer$Type;->textPx:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$Type;->label:Limgui/ImFont;", "FIELD:Lnet/anubis/client/gui/HudRenderer$Type;->labelPx:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public ImFont value() {
            return this.value;
        }

        public int valuePx() {
            return this.valuePx;
        }

        public ImFont text() {
            return this.text;
        }

        public int textPx() {
            return this.textPx;
        }

        public ImFont label() {
            return this.label;
        }

        public int labelPx() {
            return this.labelPx;
        }

        static Type of(float scale) {
            ImGuiManager m = ImGuiManager.getInstance();
            return new Type(m.hudValueFont(), HudChrome.px(18.0f, scale), m.hudTextFont(), HudChrome.px(17.0f, scale), m.hudLabelFont(), HudChrome.px(14.0f, scale));
        }
    }

    private float slot(String key, int px, float target, float dt) {
        Int2ObjectOpenHashMap<StableWidth> bySize = this.slots.computeIfAbsent(key, k -> {
            return new Int2ObjectOpenHashMap();
        });
        StableWidth width = (StableWidth) bySize.get(px);
        if (width == null) {
            width = new StableWidth();
            bySize.put(px, width);
            this.slotCount++;
        }
        return width.update(target, dt);
    }

    private void removeSlot(String key, int px) {
        Int2ObjectOpenHashMap<StableWidth> bySize = this.slots.get(key);
        if (bySize == null || bySize.remove(px) == null) {
            return;
        }
        this.slotCount--;
    }

    private void clearSlots() {
        this.slots.clear();
        this.slotCount = 0;
    }

    private static String number(long value) {
        return value < 0 ? "−" + (-value) : Long.toString(value);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$InfoItem.class */
    @Environment(EnvType.CLIENT)
    private static final class InfoItem extends Record {
        private final String slot;
        private final String value;
        private final String dim;
        private final String unit;

        private InfoItem(String slot, String value, String dim, String unit) {
            this.slot = slot;
            this.value = value;
            this.dim = dim;
            this.unit = unit;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, InfoItem.class), InfoItem.class, "slot;value;dim;unit", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoItem;->slot:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoItem;->value:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoItem;->dim:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoItem;->unit:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, InfoItem.class), InfoItem.class, "slot;value;dim;unit", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoItem;->slot:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoItem;->value:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoItem;->dim:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoItem;->unit:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, InfoItem.class, Object.class), InfoItem.class, "slot;value;dim;unit", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoItem;->slot:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoItem;->value:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoItem;->dim:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoItem;->unit:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public String slot() {
            return this.slot;
        }

        public String value() {
            return this.value;
        }

        public String dim() {
            return this.dim;
        }

        public String unit() {
            return this.unit;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$Segment.class */
    @Environment(EnvType.CLIENT)
    private static final class Segment extends Record {
        private final InfoItem item;
        private final float valueW;
        private final float dimW;
        private final float contentW;
        private final float slotW;

        private Segment(InfoItem item, float valueW, float dimW, float contentW, float slotW) {
            this.item = item;
            this.valueW = valueW;
            this.dimW = dimW;
            this.contentW = contentW;
            this.slotW = slotW;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Segment.class), Segment.class, "item;valueW;dimW;contentW;slotW", "FIELD:Lnet/anubis/client/gui/HudRenderer$Segment;->item:Lnet/anubis/client/gui/HudRenderer$InfoItem;", "FIELD:Lnet/anubis/client/gui/HudRenderer$Segment;->valueW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Segment;->dimW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Segment;->contentW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Segment;->slotW:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Segment.class), Segment.class, "item;valueW;dimW;contentW;slotW", "FIELD:Lnet/anubis/client/gui/HudRenderer$Segment;->item:Lnet/anubis/client/gui/HudRenderer$InfoItem;", "FIELD:Lnet/anubis/client/gui/HudRenderer$Segment;->valueW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Segment;->dimW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Segment;->contentW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Segment;->slotW:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Segment.class, Object.class), Segment.class, "item;valueW;dimW;contentW;slotW", "FIELD:Lnet/anubis/client/gui/HudRenderer$Segment;->item:Lnet/anubis/client/gui/HudRenderer$InfoItem;", "FIELD:Lnet/anubis/client/gui/HudRenderer$Segment;->valueW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Segment;->dimW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Segment;->contentW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Segment;->slotW:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public InfoItem item() {
            return this.item;
        }

        public float valueW() {
            return this.valueW;
        }

        public float dimW() {
            return this.dimW;
        }

        public float contentW() {
            return this.contentW;
        }

        public float slotW() {
            return this.slotW;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$InfoPlan.class */
    @Environment(EnvType.CLIENT)
    private static final class InfoPlan extends Record {
        private final List<Segment> segments;
        private final Type type;
        private final float width;
        private final float height;
        private final float emblemW;

        private InfoPlan(List<Segment> segments, Type type, float width, float height, float emblemW) {
            this.segments = segments;
            this.type = type;
            this.width = width;
            this.height = height;
            this.emblemW = emblemW;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, InfoPlan.class), InfoPlan.class, "segments;type;width;height;emblemW", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoPlan;->segments:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoPlan;->height:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoPlan;->emblemW:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, InfoPlan.class), InfoPlan.class, "segments;type;width;height;emblemW", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoPlan;->segments:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoPlan;->height:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoPlan;->emblemW:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, InfoPlan.class, Object.class), InfoPlan.class, "segments;type;width;height;emblemW", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoPlan;->segments:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoPlan;->height:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$InfoPlan;->emblemW:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public List<Segment> segments() {
            return this.segments;
        }

        public Type type() {
            return this.type;
        }

        public float width() {
            return this.width;
        }

        public float height() {
            return this.height;
        }

        public float emblemW() {
            return this.emblemW;
        }
    }

    private List<InfoItem> buildInfoItems(boolean showFps, boolean showClock, boolean showPing, class_310 client) {
        long second = showClock ? System.currentTimeMillis() / 1000 : -1L;
        int fps = showFps ? Math.max(0, client.method_47599()) : -1;
        int ping = -1;
        if (showPing) {
            class_634 conn = client.method_1562();
            int latency = 0;
            if (conn != null && client.field_1724 != null) {
                latency = readTabLatency(client, conn);
                if (latency <= 0) {
                    latency = readServerPing(conn);
                }
            }
            ping = Math.max(0, latency);
        }
        if (second == this.infoSecond && fps == this.infoFps && ping == this.infoPing) {
            return this.infoItems;
        }
        this.infoSecond = second;
        this.infoFps = fps;
        this.infoPing = ping;
        List<InfoItem> items = new ArrayList<>(3);
        if (showClock) {
            LocalTime now = LocalTime.now();
            items.add(new InfoItem("info.time", now.format(CLOCK_FORMAT), SECONDS[now.getSecond()], null));
        }
        if (showFps) {
            items.add(new InfoItem("info.fps", String.valueOf(fps), null, "fps"));
        }
        if (showPing) {
            items.add(new InfoItem("info.ping", String.valueOf(ping), null, "ms"));
        }
        this.infoItems = List.copyOf(items);
        return this.infoItems;
    }

    private InfoPlan planInfoBar(HudLayoutStore.HudElementState state, List<InfoItem> items, float dt) {
        float s = scaleOf(state);
        Type t = Type.of(s);
        List<Segment> segments = new ArrayList<>(items.size());
        float total = 12.0f * s * 2.0f;
        for (int i = 0; i < items.size(); i++) {
            InfoItem item = items.get(i);
            float valueW = HudChrome.tabularWidth(t.value(), t.valuePx(), item.value());
            float dimW = item.dim() != null ? HudChrome.tabularWidth(t.value(), t.valuePx(), item.dim()) : 0.0f;
            float content = valueW + dimW;
            if (item.unit() != null) {
                content += (4.0f * s) + HudChrome.width(t.label(), t.labelPx(), item.unit());
            }
            float slotW = slot(item.slot(), t.valuePx(), (float) Math.ceil(content), dt);
            segments.add(new Segment(item, valueW, dimW, content, slotW));
            total += slotW;
        }
        if (segments.size() > 1) {
            total += (segments.size() - 1) * ((10.0f * s * 2.0f) + 1.0f);
        }
        float emblemW = 0.0f;
        ThemeImages.Texture emblem = (this.hudModule == null || !this.hudModule.logo() || segments.isEmpty()) ? null : HudEmblem.forHeight(Math.round(24.0f * s));
        if (emblem != null) {
            emblemW = emblem.width();
            total += emblemW + (10.0f * s * 2.0f) + 1.0f;
        }
        return new InfoPlan(segments, t, Math.round(total), Math.round(CAP_H * s), emblemW);
    }

    private void drawInfoBar(ImDrawList dl, HudLayoutStore.HudElementState state, InfoPlan plan, boolean shadow, DrawTransform tr) {
        float a = tr.alpha();
        if (a <= 0.001f) {
            return;
        }
        float s = scaleOf(state);
        float x = Math.round(state.x + tr.xOffset());
        float y = Math.round(state.y + tr.yOffset());
        float w = state.width;
        float h = state.height;
        Surface surface = hudSurface(state);
        boolean guard = HudChrome.needsTextGuard(surface);
        HudChrome.card(dl, surface, x, y, w, h, hudRounding() * s, a, shadow, s);
        Type t = plan.type();
        float valueTop = y + ((float) Math.floor((h - t.valuePx()) * 0.5f));
        float labelTop = valueTop + HudChrome.baselineOffset(t.value(), t.valuePx(), t.label(), t.labelPx());
        float cx = x + (12.0f * s);
        ThemeImages.Texture emblem = plan.emblemW() > 0.0f ? HudEmblem.forHeight(Math.round(24.0f * s)) : null;
        if (emblem != null) {
            float ex = Math.round(cx);
            float ey = Math.round(y + ((h - emblem.height()) * 0.5f));
            dl.addImage(emblem.id(), ex, ey, ex + emblem.width(), ey + emblem.height(), 0.0f, 0.0f, 1.0f, 1.0f, HudChrome.scaleAlpha(hudAccent(), a));
            float cx2 = cx + plan.emblemW();
            HudChrome.divider(dl, cx2 + (10.0f * s), y + (h * 0.5f), 12.0f * s, a);
            cx = cx2 + (10.0f * s * 2.0f) + 1.0f;
        }
        List<Segment> segments = plan.segments();
        for (int i = 0; i < segments.size(); i++) {
            Segment seg = segments.get(i);
            InfoItem item = seg.item();
            float vx = item.unit() != null ? (cx + seg.slotW()) - seg.contentW() : cx;
            HudChrome.tabular(dl, t.value(), t.valuePx(), vx, valueTop, HudChrome.scaleAlpha(HudChrome.TEXT_VALUE, a), item.value(), guard);
            float vx2 = vx + seg.valueW();
            if (item.dim() != null) {
                HudChrome.tabular(dl, t.value(), t.valuePx(), vx2, valueTop, HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, a), item.dim(), guard);
                vx2 += seg.dimW();
            }
            if (item.unit() != null) {
                HudChrome.text(dl, t.label(), t.labelPx(), vx2 + (4.0f * s), labelTop, HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, a), item.unit(), guard);
            }
            cx += seg.slotW();
            if (i < segments.size() - 1) {
                HudChrome.divider(dl, cx + (10.0f * s), y + (h * 0.5f), 12.0f * s, a);
                cx += (10.0f * s * 2.0f) + 1.0f;
            }
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$Step.class */
    @Environment(EnvType.CLIENT)
    private static final class Step extends Record {
        private final int item;
        private final float l;
        private final float t;
        private final float r;
        private final float b;
        private final float alpha;

        private Step(int item, float l, float t, float r, float b, float alpha) {
            this.item = item;
            this.l = l;
            this.t = t;
            this.r = r;
            this.b = b;
            this.alpha = alpha;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Step.class), Step.class, "item;l;t;r;b;alpha", "FIELD:Lnet/anubis/client/gui/HudRenderer$Step;->item:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$Step;->l:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Step;->t:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Step;->r:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Step;->b:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Step;->alpha:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Step.class), Step.class, "item;l;t;r;b;alpha", "FIELD:Lnet/anubis/client/gui/HudRenderer$Step;->item:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$Step;->l:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Step;->t:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Step;->r:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Step;->b:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Step;->alpha:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Step.class, Object.class), Step.class, "item;l;t;r;b;alpha", "FIELD:Lnet/anubis/client/gui/HudRenderer$Step;->item:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$Step;->l:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Step;->t:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Step;->r:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Step;->b:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Step;->alpha:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int item() {
            return this.item;
        }

        public float l() {
            return this.l;
        }

        public float t() {
            return this.t;
        }

        public float r() {
            return this.r;
        }

        public float b() {
            return this.b;
        }

        public float alpha() {
            return this.alpha;
        }
    }

    private static float rowsRadius(float s) {
        return Math.min(hudRounding(), 6.0f) * s;
    }

    private void drawRows(ImDrawList dl, List<Step> steps, Surface surface, float x, float y, float w, float h, float radius, float s, boolean shadow) {
        if (shadow) {
            drawRowsShadow(dl, steps, radius, s);
        }
        int fill = GuiPalette.fill(surface);
        int i = 0;
        while (i < steps.size()) {
            Step q = steps.get(i);
            Step up = i > 0 ? steps.get(i - 1) : null;
            Step dn = i < steps.size() - 1 ? steps.get(i + 1) : null;
            int corners = convexCorners(q, up, dn);
            int color = HudChrome.scaleAlpha(fill, q.alpha());
            if (corners == 256) {
                dl.addRectFilled(q.l(), q.t(), q.r(), q.b(), color);
            } else {
                dl.addRectFilled(q.l(), q.t(), q.r(), q.b(), color, radius, corners);
            }
            SurfaceBackground.drawPart(dl, surface, x, y, w, h, q.l(), q.t(), q.r() - q.l(), q.b() - q.t(), q.alpha(), radius, corners);
            float stepH = q.b() - q.t();
            if (up != null && up.l() < q.l() - 0.5f) {
                fillet(dl, q.l(), q.t(), -1, 1, Math.min(radius, Math.min(q.l() - up.l(), stepH)), color);
            }
            if (up != null && up.r() > q.r() + 0.5f) {
                fillet(dl, q.r(), q.t(), 1, 1, Math.min(radius, Math.min(up.r() - q.r(), stepH)), color);
            }
            if (dn != null && dn.l() < q.l() - 0.5f) {
                fillet(dl, q.l(), q.b(), -1, -1, Math.min(radius, Math.min(q.l() - dn.l(), stepH)), color);
            }
            if (dn != null && dn.r() > q.r() + 0.5f) {
                fillet(dl, q.r(), q.b(), 1, -1, Math.min(radius, Math.min(dn.r() - q.r(), stepH)), color);
            }
            i++;
        }
    }

    private static int convexCorners(Step q, Step up, Step dn) {
        int flags = 0;
        if (up == null || up.l() > q.l() + 0.5f) {
            flags = 0 | 16;
        }
        if (up == null || up.r() < q.r() - 0.5f) {
            flags |= 32;
        }
        if (dn == null || dn.l() > q.l() + 0.5f) {
            flags |= 64;
        }
        if (dn == null || dn.r() < q.r() - 0.5f) {
            flags |= 128;
        }
        if (flags == 0) {
            return 256;
        }
        return flags;
    }

    private static void fillet(ImDrawList dl, float px, float py, int dirX, int dirY, float r, int color) {
        float f;
        if (r < 1.0f) {
            return;
        }
        float cx = px + (dirX * r);
        float cy = py + (dirY * r);
        float a0 = dirX < 0 ? 0.0f : 3.1415927f;
        if (dirY > 0) {
            f = dirX < 0 ? -1.5707964f : 4.712389f;
        } else {
            f = 1.5707964f;
        }
        float a1 = f;
        dl.pathClear();
        dl.pathLineTo(px, py);
        dl.pathArcTo(cx, cy, r, a0, a1, 8);
        dl.pathFillConcave(color);
    }

    private static void drawRowsShadow(ImDrawList dl, List<Step> steps, float radius, float s) {
        for (int k = 3; k >= 1; k--) {
            float spread = k * Math.max(1.0f, s);
            int i = 0;
            while (i < steps.size()) {
                Step q = steps.get(i);
                boolean first = i == 0;
                boolean last = i == steps.size() - 1;
                int corners = convexCorners(q, first ? null : steps.get(i - 1), last ? null : steps.get(i + 1));
                float top = q.t() - (first ? spread : 0.0f);
                float bottom = q.b() + (last ? spread + Math.max(1.0f, s) : 0.0f);
                int color = HudChrome.scaleAlpha(218103808, q.alpha());
                if (corners == 256) {
                    dl.addRectFilled(q.l() - spread, top, q.r() + spread, bottom, color);
                } else {
                    dl.addRectFilled(q.l() - spread, top, q.r() + spread, bottom, color, radius + spread, corners);
                }
                i++;
            }
        }
    }

    private static boolean alignsRight(HudLayoutStore.HudElementState state, float screenW) {
        switch (state.anchorX) {
            case 0:
                return false;
            case 2:
                return true;
            default:
                return state.x + (state.width * 0.5f) >= screenW * 0.5f;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$CoordRow.class */
    @Environment(EnvType.CLIENT)
    private static final class CoordRow extends Record {
        private final String block;
        private final boolean here;
        private final boolean enabled;
        private final String[] values;
        private final float anim;

        private CoordRow(String block, boolean here, boolean enabled, String[] values, float anim) {
            this.block = block;
            this.here = here;
            this.enabled = enabled;
            this.values = values;
            this.anim = anim;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, CoordRow.class), CoordRow.class, "block;here;enabled;values;anim", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordRow;->block:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordRow;->here:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordRow;->enabled:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordRow;->values:[Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordRow;->anim:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, CoordRow.class), CoordRow.class, "block;here;enabled;values;anim", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordRow;->block:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordRow;->here:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordRow;->enabled:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordRow;->values:[Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordRow;->anim:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, CoordRow.class, Object.class), CoordRow.class, "block;here;enabled;values;anim", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordRow;->block:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordRow;->here:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordRow;->enabled:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordRow;->values:[Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordRow;->anim:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public String block() {
            return this.block;
        }

        public boolean here() {
            return this.here;
        }

        public boolean enabled() {
            return this.enabled;
        }

        public String[] values() {
            return this.values;
        }

        public float anim() {
            return this.anim;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$CoordPlan.class */
    @Environment(EnvType.CLIENT)
    private static final class CoordPlan extends Record {
        private final List<CoordRow> rows;
        private final Type type;
        private final float[] slotW;
        private final float width;
        private final float height;

        private CoordPlan(List<CoordRow> rows, Type type, float[] slotW, float width, float height) {
            this.rows = rows;
            this.type = type;
            this.slotW = slotW;
            this.width = width;
            this.height = height;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, CoordPlan.class), CoordPlan.class, "rows;type;slotW;width;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordPlan;->rows:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordPlan;->slotW:[F", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordPlan;->height:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, CoordPlan.class), CoordPlan.class, "rows;type;slotW;width;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordPlan;->rows:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordPlan;->slotW:[F", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordPlan;->height:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, CoordPlan.class, Object.class), CoordPlan.class, "rows;type;slotW;width;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordPlan;->rows:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordPlan;->slotW:[F", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$CoordPlan;->height:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public List<CoordRow> rows() {
            return this.rows;
        }

        public Type type() {
            return this.type;
        }

        public float[] slotW() {
            return this.slotW;
        }

        public float width() {
            return this.width;
        }

        public float height() {
            return this.height;
        }
    }

    private List<CoordRow> coordinateRows(class_310 client, boolean editMode, float dt) {
        long x = 0;
        long y = 0;
        long z = 0;
        if (client.field_1724 != null) {
            x = (long) Math.floor(client.field_1724.method_23317());
            y = (long) Math.floor(client.field_1724.method_23318());
            z = (long) Math.floor(client.field_1724.method_23321());
        }
        boolean inNether = client.field_1687 != null && client.field_1687.method_27983() == class_1937.field_25180;
        boolean inEnd = client.field_1687 != null && client.field_1687.method_27983() == class_1937.field_25181;
        boolean overworldOn = this.hudModule.coordinates();
        boolean netherOn = this.hudModule.netherCoordinates();
        float k = 1.0f - ((float) Math.exp((-12.0f) * dt));
        List<CoordRow> rows = new ArrayList<>(2);
        if (inEnd) {
            addCoordRow(rows, "end", "end_stone", true, overworldOn || netherOn, editMode, new long[]{x, y, z}, k);
        } else {
            long[] overworld = inNether ? new long[]{x * 8, y, z * 8} : new long[]{x, y, z};
            long[] nether = inNether ? new long[]{x, y, z} : new long[]{Math.floorDiv(x, 8), y, Math.floorDiv(z, 8)};
            addCoordRow(rows, "overworld", "grass_block_side", !inNether, overworldOn, editMode, overworld, k);
            addCoordRow(rows, "nether", "netherrack", inNether, netherOn, editMode, nether, k);
        }
        return rows;
    }

    private void addCoordRow(List<CoordRow> rows, String key, String block, boolean here, boolean enabled, boolean editMode, long[] xyz, float k) {
        float target = (enabled || editMode) ? 1.0f : 0.0f;
        float anim = this.coordAnim.getOrDefault(key, Float.valueOf(target)).floatValue();
        float anim2 = anim + ((target - anim) * k);
        if (Math.abs(anim2 - target) < 0.005f) {
            anim2 = target;
        }
        this.coordAnim.put(key, Float.valueOf(anim2));
        if (anim2 <= 0.005f) {
            return;
        }
        rows.add(new CoordRow(block, here, enabled, new String[]{number(xyz[0]), number(xyz[1]), number(xyz[2])}, anim2));
    }

    private static float coordIconSize(float s) {
        return s >= 1.0f ? 16.0f * Math.max(1.0f, (float) Math.floor(s + 0.25f)) : Math.round(16.0f * s);
    }

    private CoordPlan planCoordinates(HudLayoutStore.HudElementState state, List<CoordRow> rows, boolean editMode, float dt) {
        float s = scaleOf(state);
        Type t = Type.of(s);
        float rowFull = 20.0f * s;
        float[] valueW = new float[3];
        float height = 0.0f;
        for (CoordRow row : rows) {
            for (int i = 0; i < 3; i++) {
                valueW[i] = Math.max(valueW[i], HudChrome.tabularWidth(t.text(), t.textPx(), row.values()[i]));
            }
            height += rowFull * smooth(row.anim());
        }
        float[] slotW = new float[3];
        float total = (7.0f * s * 2.0f) + Math.min(coordIconSize(s), rowFull - 2.0f) + (8.0f * s) + (12.0f * s * 2.0f);
        for (int i2 = 0; i2 < 3; i2++) {
            slotW[i2] = slot(COORD_SLOTS[i2], t.textPx(), (float) Math.ceil(valueW[i2]), dt);
            total += slotW[i2];
        }
        if (editMode) {
            height = Math.max(height, rowFull);
        }
        return new CoordPlan(rows, t, slotW, Math.round(total), Math.max(1, Math.round(height)));
    }

    private void drawCoordinates(ImDrawList dl, HudLayoutStore.HudElementState state, CoordPlan plan, boolean shadow, DrawTransform tr, float screenW) {
        float a = tr.alpha();
        if (a <= 0.001f || plan.rows().isEmpty()) {
            return;
        }
        float s = scaleOf(state);
        float x = Math.round(state.x + tr.xOffset());
        float y = Math.round(state.y + tr.yOffset());
        float w = state.width;
        float h = state.height;
        Type t = plan.type();
        boolean right = alignsRight(state, screenW);
        float rowFull = 20.0f * s;
        List<Step> steps = new ArrayList<>(plan.rows().size());
        float cy = y;
        for (int i = 0; i < plan.rows().size(); i++) {
            CoordRow row = plan.rows().get(i);
            float open = smooth(row.anim());
            float rowH = rowFull * open;
            float top = Math.round(cy);
            float bottom = Math.round(cy + rowH);
            cy += rowH;
            if (bottom > top) {
                float slide = Math.round(w * (1.0f - open));
                float l = right ? x + slide : x - slide;
                float rowAlpha = a * (row.enabled() ? 1.0f : 0.35f) * Math.min(1.0f, row.anim() * RADAR_MARKER_INSET);
                steps.add(new Step(i, l, top, l + w, bottom, rowAlpha));
            }
        }
        drawRows(dl, steps, hudSurface(state), x, y, w, h, rowsRadius(s), s, shadow);
        float icon = Math.min(coordIconSize(s), rowFull - 2.0f);
        float iconR = 2.5f * s;
        float textInset = (float) Math.floor((rowFull - t.textPx()) * 0.5f);
        for (Step q : steps) {
            CoordRow row2 = plan.rows().get(q.item());
            float stepH = q.b() - q.t();
            float shift = (rowFull - stepH) * 0.5f;
            float contentAlpha = q.alpha() * row2.anim();
            boolean clip = stepH < rowFull - 0.5f;
            if (clip) {
                dl.pushClipRect(q.l(), q.t(), q.r(), q.b(), true);
            }
            float ix = Math.round(q.l() + (7.0f * s));
            float iy = Math.round((q.t() + ((rowFull - icon) * 0.5f)) - shift);
            ThemeImages.Texture texture = GameIcons.block(row2.block());
            if (texture != null) {
                dl.addImageRounded(texture.id(), ix, iy, ix + icon, iy + icon, 0.0f, 0.0f, 1.0f, 1.0f, HudChrome.scaleAlpha(-1, contentAlpha), iconR, 240);
            }
            float cx = ix + icon + (8.0f * s);
            float valueTop = Math.round((q.t() + textInset) - shift);
            int color = HudChrome.scaleAlpha(-1, contentAlpha * (row2.here() ? 1.0f : 0.62f));
            for (int i2 = 0; i2 < 3; i2++) {
                HudChrome.tabular(dl, t.text(), t.textPx(), cx, valueTop, color, row2.values()[i2], true);
                cx += plan.slotW()[i2] + (12.0f * s);
            }
            if (clip) {
                dl.popClipRect();
            }
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$ListPlan.class */
    @Environment(EnvType.CLIENT)
    private static final class ListPlan extends Record {
        private final List<ModuleListRows.Row> rows;
        private final Type type;
        private final float width;
        private final float height;

        private ListPlan(List<ModuleListRows.Row> rows, Type type, float width, float height) {
            this.rows = rows;
            this.type = type;
            this.width = width;
            this.height = height;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, ListPlan.class), ListPlan.class, "rows;type;width;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$ListPlan;->rows:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$ListPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$ListPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$ListPlan;->height:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, ListPlan.class), ListPlan.class, "rows;type;width;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$ListPlan;->rows:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$ListPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$ListPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$ListPlan;->height:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, ListPlan.class, Object.class), ListPlan.class, "rows;type;width;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$ListPlan;->rows:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$ListPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$ListPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$ListPlan;->height:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public List<ModuleListRows.Row> rows() {
            return this.rows;
        }

        public Type type() {
            return this.type;
        }

        public float width() {
            return this.width;
        }

        public float height() {
            return this.height;
        }
    }

    private static float rowWidth(float nameW, float s) {
        return Math.round((12.0f * s) + nameW);
    }

    private ListPlan planModuleList(HudLayoutStore.HudElementState state, boolean editMode, float dt) {
        float s = scaleOf(state);
        Type t = Type.of(s);
        float rowFull = 18.0f * s;
        List<ModuleListRows.Row> rows = this.moduleRows.ordered(t.text(), t.textPx(), HudChrome::width, state.anchorBottom);
        float widest = 0.0f;
        float height = 0.0f;
        for (int i = 0; i < rows.size(); i++) {
            ModuleListRows.Row row = rows.get(i);
            widest = Math.max(widest, row.nameW());
            height += rowFull * smooth(row.anim());
        }
        if (rows.isEmpty()) {
            this.moduleListW = editMode ? rowWidth(HudChrome.width(t.label(), t.labelPx(), NO_MODULES), s) : 0.0f;
            return new ListPlan(rows, t, Math.max(1, Math.round(this.moduleListW)), Math.round(rowFull));
        }
        if (editMode) {
            height = Math.max(height, rowFull);
        }
        float target = rowWidth(widest, s);
        if (target >= this.moduleListW || Math.abs(target - this.moduleListW) < 0.5f) {
            this.moduleListW = target;
        } else {
            this.moduleListW += (target - this.moduleListW) * (1.0f - ((float) Math.exp((-16.0f) * dt)));
        }
        return new ListPlan(rows, t, Math.round(this.moduleListW), Math.max(1, Math.round(height)));
    }

    private void drawModuleList(ImDrawList dl, HudLayoutStore.HudElementState state, ListPlan plan, boolean shadow, DrawTransform tr, float screenW) {
        float a = tr.alpha();
        if (a <= 0.001f) {
            return;
        }
        float s = scaleOf(state);
        float x = Math.round(state.x + tr.xOffset());
        float y = Math.round(state.y + tr.yOffset());
        float w = state.width;
        float h = state.height;
        Type t = plan.type();
        boolean right = alignsRight(state, screenW);
        float rowFull = 18.0f * s;
        List<Step> steps = new ArrayList<>(Math.max(1, plan.rows().size()));
        if (plan.rows().isEmpty()) {
            float rowW = rowWidth(HudChrome.width(t.label(), t.labelPx(), NO_MODULES), s);
            float l = right ? (x + w) - rowW : x;
            steps.add(new Step(-1, l, y, l + rowW, Math.round(y + rowFull), a));
        } else {
            float cy = y;
            for (int i = 0; i < plan.rows().size(); i++) {
                ModuleListRows.Row row = plan.rows().get(i);
                float open = smooth(row.anim());
                float rowH = rowFull * open;
                float top = Math.round(cy);
                float bottom = Math.round(cy + rowH);
                cy += rowH;
                if (bottom > top) {
                    float rowW2 = rowWidth(row.nameW(), s);
                    float slide = Math.round(rowW2 * (1.0f - open));
                    float l2 = right ? ((x + w) - rowW2) + slide : x - slide;
                    steps.add(new Step(i, l2, top, l2 + rowW2, bottom, a * Math.min(1.0f, row.anim() * RADAR_MARKER_INSET)));
                }
            }
        }
        drawRows(dl, steps, hudSurface(state), x, y, w, h, rowsRadius(s), s, shadow);
        float textInset = (float) Math.floor((rowFull - t.textPx()) * 0.5f);
        float pad = 6.0f * s;
        for (Step q : steps) {
            if (q.item() < 0) {
                float labelW = HudChrome.width(t.label(), t.labelPx(), NO_MODULES);
                float tx = right ? (q.r() - pad) - labelW : q.l() + pad;
                HudChrome.text(dl, t.label(), t.labelPx(), tx, q.t() + ((float) Math.floor((rowFull - t.labelPx()) * 0.5f)), HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, a), NO_MODULES, true);
            } else {
                ModuleListRows.Row row2 = plan.rows().get(q.item());
                float stepH = q.b() - q.t();
                float tx2 = right ? (q.r() - pad) - row2.nameW() : q.l() + pad;
                float ty = (q.t() + textInset) - ((rowFull - stepH) * 0.5f);
                boolean clip = stepH < rowFull - 0.5f;
                if (clip) {
                    dl.pushClipRect(q.l(), q.t(), q.r(), q.b(), true);
                }
                HudChrome.text(dl, t.text(), t.textPx(), Math.round(tx2), Math.round(ty), HudChrome.scaleAlpha(-1, a * row2.anim() * row2.anim()), row2.module().name(), true);
                if (clip) {
                    dl.popClipRect();
                }
            }
        }
    }

    private float updateToastShift(class_310 client, HudLayoutStore.HudElementState state, boolean editMode, float dt) {
        float target = (editMode || this.draggingId != null) ? 0.0f : toastClearance(client, state);
        if (editMode || Math.abs(target - this.toastShift) < 0.5f) {
            this.toastShift = target;
        } else {
            this.toastShift += (target - this.toastShift) * (1.0f - ((float) Math.exp((-12.0f) * dt)));
        }
        return this.toastShift;
    }

    private static float toastClearance(class_310 client, HudLayoutStore.HudElementState state) {
        if (state.anchorX != 2 || state.anchorBottom || client.field_1690.field_1842) {
            return 0.0f;
        }
        class_1041 window = client.method_22683();
        int gui = window.method_4495();
        int guiW = window.method_4486();
        float bottom = 0.0f;
        for (Object entry : client.method_1566().anubis$hudVisibleToasts()) {
            AccessorHudToastInstance instance = (AccessorHudToastInstance) entry;
            class_368 toast = instance.anubis$hudToast();
            float shown = instance.anubis$hudVisibility() == class_368.class_369.field_2209 ? instance.anubis$hudVisiblePortion() : 1.0f;
            float left = toast.method_71809(guiW, shown) * gui;
            if (left < state.x + state.width && left + (toast.method_29049() * gui) > state.x) {
                bottom = Math.max(bottom, (toast.method_71808(instance.anubis$hudFirstSlot()) + toast.method_29050()) * gui);
            }
        }
        if (bottom <= 0.0f) {
            return 0.0f;
        }
        return class_3532.method_15363((bottom + (10.0f * ImGuiManager.getInstance().uiScale())) - state.y, 0.0f, bottom);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$PotionRow.class */
    @Environment(EnvType.CLIENT)
    private static final class PotionRow extends Record {
        private final class_6880<class_1291> effect;
        private final String name;
        private final String amplifier;
        private final String duration;
        private final boolean infinite;
        private final boolean expiringSoon;
        private final int ticks;

        private PotionRow(class_6880<class_1291> effect, String name, String amplifier, String duration, boolean infinite, boolean expiringSoon, int ticks) {
            this.effect = effect;
            this.name = name;
            this.amplifier = amplifier;
            this.duration = duration;
            this.infinite = infinite;
            this.expiringSoon = expiringSoon;
            this.ticks = ticks;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, PotionRow.class), PotionRow.class, "effect;name;amplifier;duration;infinite;expiringSoon;ticks", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionRow;->effect:Lnet/minecraft/class_6880;", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionRow;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionRow;->amplifier:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionRow;->duration:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionRow;->infinite:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionRow;->expiringSoon:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionRow;->ticks:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, PotionRow.class), PotionRow.class, "effect;name;amplifier;duration;infinite;expiringSoon;ticks", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionRow;->effect:Lnet/minecraft/class_6880;", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionRow;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionRow;->amplifier:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionRow;->duration:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionRow;->infinite:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionRow;->expiringSoon:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionRow;->ticks:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, PotionRow.class, Object.class), PotionRow.class, "effect;name;amplifier;duration;infinite;expiringSoon;ticks", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionRow;->effect:Lnet/minecraft/class_6880;", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionRow;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionRow;->amplifier:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionRow;->duration:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionRow;->infinite:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionRow;->expiringSoon:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionRow;->ticks:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_6880<class_1291> effect() {
            return this.effect;
        }

        public String name() {
            return this.name;
        }

        public String amplifier() {
            return this.amplifier;
        }

        public String duration() {
            return this.duration;
        }

        public boolean infinite() {
            return this.infinite;
        }

        public boolean expiringSoon() {
            return this.expiringSoon;
        }

        public int ticks() {
            return this.ticks;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$PotionLine.class */
    @Environment(EnvType.CLIENT)
    private static final class PotionLine extends Record {
        private final PotionRow row;
        private final float nameW;
        private final float ampW;
        private final float durationW;

        private PotionLine(PotionRow row, float nameW, float ampW, float durationW) {
            this.row = row;
            this.nameW = nameW;
            this.ampW = ampW;
            this.durationW = durationW;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, PotionLine.class), PotionLine.class, "row;nameW;ampW;durationW", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionLine;->row:Lnet/anubis/client/gui/HudRenderer$PotionRow;", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionLine;->nameW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionLine;->ampW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionLine;->durationW:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, PotionLine.class), PotionLine.class, "row;nameW;ampW;durationW", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionLine;->row:Lnet/anubis/client/gui/HudRenderer$PotionRow;", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionLine;->nameW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionLine;->ampW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionLine;->durationW:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, PotionLine.class, Object.class), PotionLine.class, "row;nameW;ampW;durationW", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionLine;->row:Lnet/anubis/client/gui/HudRenderer$PotionRow;", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionLine;->nameW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionLine;->ampW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionLine;->durationW:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public PotionRow row() {
            return this.row;
        }

        public float nameW() {
            return this.nameW;
        }

        public float ampW() {
            return this.ampW;
        }

        public float durationW() {
            return this.durationW;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$PotionPlan.class */
    @Environment(EnvType.CLIENT)
    private static final class PotionPlan extends Record {
        private final List<PotionLine> lines;
        private final Type type;
        private final float width;
        private final float height;

        private PotionPlan(List<PotionLine> lines, Type type, float width, float height) {
            this.lines = lines;
            this.type = type;
            this.width = width;
            this.height = height;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, PotionPlan.class), PotionPlan.class, "lines;type;width;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionPlan;->lines:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionPlan;->height:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, PotionPlan.class), PotionPlan.class, "lines;type;width;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionPlan;->lines:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionPlan;->height:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, PotionPlan.class, Object.class), PotionPlan.class, "lines;type;width;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionPlan;->lines:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$PotionPlan;->height:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public List<PotionLine> lines() {
            return this.lines;
        }

        public Type type() {
            return this.type;
        }

        public float width() {
            return this.width;
        }

        public float height() {
            return this.height;
        }
    }

    private static float iconSize(float s) {
        return s >= 1.0f ? 18.0f * Math.max(1.0f, (float) Math.floor(s + 0.25f)) : Math.round(18.0f * s);
    }

    private PotionPlan planPotions(HudLayoutStore.HudElementState state, List<PotionRow> rows, boolean editMode, float dt) {
        float fTabularWidth;
        float s = scaleOf(state);
        Type t = Type.of(s);
        float rowFull = 22.0f * s;
        List<PotionRow> sorted = new ArrayList<>(rows);
        sorted.sort(Comparator.comparingInt((v0) -> {
            return v0.ticks();
        }).thenComparing((v0) -> {
            return v0.name();
        }));
        if (state.anchorBottom) {
            Collections.reverse(sorted);
        }
        List<PotionLine> lines = new ArrayList<>(sorted.size());
        float target = 0.0f;
        float fixed = (6.0f * s * 2.0f) + Math.min(iconSize(s), rowFull - 2.0f) + (6.0f * s) + (14.0f * s);
        for (PotionRow row : sorted) {
            float nameW = HudChrome.width(t.text(), t.textPx(), row.name());
            float ampW = row.amplifier().isEmpty() ? 0.0f : HudChrome.width(t.text(), t.textPx(), row.amplifier());
            if (row.infinite()) {
                fTabularWidth = HudChrome.width(t.text(), t.textPx(), INFINITY);
            } else {
                fTabularWidth = HudChrome.tabularWidth(t.text(), t.textPx(), row.duration());
            }
            float durationW = fTabularWidth;
            lines.add(new PotionLine(row, nameW, ampW, durationW));
            target = Math.max(target, fixed + nameW + (ampW > 0.0f ? (4.0f * s) + ampW : 0.0f) + durationW);
        }
        if (lines.isEmpty()) {
            removeSlot("potions", t.textPx());
            float w = editMode ? (6.0f * s * 2.0f) + HudChrome.width(t.label(), t.labelPx(), NO_EFFECTS) : 1.0f;
            return new PotionPlan(lines, t, Math.round(w), Math.round(rowFull));
        }
        float w2 = slot("potions", t.textPx(), (float) Math.ceil(target), dt);
        return new PotionPlan(lines, t, Math.round(w2), Math.round(rowFull * lines.size()));
    }

    private void drawPotions(ImDrawList dl, HudLayoutStore.HudElementState state, PotionPlan plan, boolean shadow, DrawTransform tr) {
        float a = tr.alpha();
        if (a <= 0.001f) {
            return;
        }
        float s = scaleOf(state);
        float x = Math.round(state.x + tr.xOffset());
        float y = Math.round(state.y + tr.yOffset());
        float w = state.width;
        float h = state.height;
        Type t = plan.type();
        float rowFull = 22.0f * s;
        float pad = 6.0f * s;
        int count = Math.max(1, plan.lines().size());
        List<Step> steps = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            steps.add(new Step(plan.lines().isEmpty() ? -1 : i, x, Math.round(y + (rowFull * i)), x + w, Math.round(y + (rowFull * (i + 1))), a));
        }
        drawRows(dl, steps, hudSurface(state), x, y, w, h, rowsRadius(s), s, shadow);
        if (plan.lines().isEmpty()) {
            HudChrome.text(dl, t.label(), t.labelPx(), x + pad, y + ((float) Math.floor((rowFull - t.labelPx()) * 0.5f)), HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, a), NO_EFFECTS, true);
            return;
        }
        float icon = Math.min(iconSize(s), rowFull - 2.0f);
        float textInset = (float) Math.floor((rowFull - t.textPx()) * 0.5f);
        for (Step q : steps) {
            PotionLine line = plan.lines().get(q.item());
            PotionRow row = line.row();
            float ix = Math.round(q.l() + pad);
            float iy = Math.round(q.t() + ((rowFull - icon) * 0.5f));
            float iconAlpha = a;
            if (!row.infinite() && row.ticks() <= 200) {
                iconAlpha *= 0.55f + (0.45f * Math.abs((float) Math.cos((((double) row.ticks()) * 3.141592653589793d) / 10.0d)));
            }
            ThemeImages.Texture texture = GameIcons.effect(row.effect());
            if (texture != null) {
                dl.addImage(texture.id(), ix, iy, ix + icon, iy + icon, 0.0f, 0.0f, 1.0f, 1.0f, HudChrome.scaleAlpha(-1, iconAlpha));
            } else {
                int color = GuiPalette.abgr((-16777216) | ((class_1291) row.effect().comp_349()).method_5556());
                dl.addCircleFilled(ix + (icon * 0.5f), iy + (icon * 0.5f), icon * LYRIC_ARRIVE_TIME, HudChrome.scaleAlpha(color, iconAlpha), 16);
            }
            float ty = q.t() + textInset;
            float tx = ix + icon + (6.0f * s);
            HudChrome.text(dl, t.text(), t.textPx(), tx, ty, HudChrome.scaleAlpha(-1, a), row.name(), true);
            if (line.ampW() > 0.0f) {
                HudChrome.text(dl, t.text(), t.textPx(), tx + line.nameW() + (4.0f * s), ty, HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, a), row.amplifier(), true);
            }
            float dx = (q.r() - pad) - line.durationW();
            if (row.infinite()) {
                HudChrome.text(dl, t.text(), t.textPx(), dx, ty, HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, a), INFINITY, true);
            } else {
                int color2 = row.expiringSoon() ? HudChrome.WARN : HudChrome.TEXT_LABEL;
                HudChrome.tabular(dl, t.text(), t.textPx(), dx, ty, HudChrome.scaleAlpha(color2, a), row.duration(), true);
            }
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$VanishLine.class */
    @Environment(EnvType.CLIENT)
    private static final class VanishLine extends Record {
        private final String tag;
        private final String value;
        private final boolean urgent;
        private final float tagW;
        private final float valueW;

        private VanishLine(String tag, String value, boolean urgent, float tagW, float valueW) {
            this.tag = tag;
            this.value = value;
            this.urgent = urgent;
            this.tagW = tagW;
            this.valueW = valueW;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, VanishLine.class), VanishLine.class, "tag;value;urgent;tagW;valueW", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishLine;->tag:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishLine;->value:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishLine;->urgent:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishLine;->tagW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishLine;->valueW:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, VanishLine.class), VanishLine.class, "tag;value;urgent;tagW;valueW", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishLine;->tag:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishLine;->value:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishLine;->urgent:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishLine;->tagW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishLine;->valueW:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, VanishLine.class, Object.class), VanishLine.class, "tag;value;urgent;tagW;valueW", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishLine;->tag:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishLine;->value:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishLine;->urgent:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishLine;->tagW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishLine;->valueW:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public String tag() {
            return this.tag;
        }

        public String value() {
            return this.value;
        }

        public boolean urgent() {
            return this.urgent;
        }

        public float tagW() {
            return this.tagW;
        }

        public float valueW() {
            return this.valueW;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$VanishPlan.class */
    @Environment(EnvType.CLIENT)
    private static final class VanishPlan extends Record {
        private final List<VanishLine> lines;
        private final Type type;
        private final float tagColumn;
        private final float width;
        private final float height;

        private VanishPlan(List<VanishLine> lines, Type type, float tagColumn, float width, float height) {
            this.lines = lines;
            this.type = type;
            this.tagColumn = tagColumn;
            this.width = width;
            this.height = height;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, VanishPlan.class), VanishPlan.class, "lines;type;tagColumn;width;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishPlan;->lines:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishPlan;->tagColumn:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishPlan;->height:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, VanishPlan.class), VanishPlan.class, "lines;type;tagColumn;width;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishPlan;->lines:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishPlan;->tagColumn:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishPlan;->height:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, VanishPlan.class, Object.class), VanishPlan.class, "lines;type;tagColumn;width;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishPlan;->lines:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishPlan;->tagColumn:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$VanishPlan;->height:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public List<VanishLine> lines() {
            return this.lines;
        }

        public Type type() {
            return this.type;
        }

        public float tagColumn() {
            return this.tagColumn;
        }

        public float width() {
            return this.width;
        }

        public float height() {
            return this.height;
        }
    }

    private VanishPlan planVanish(HudLayoutStore.HudElementState state, boolean editMode, float dt) {
        float s = scaleOf(state);
        Type t = Type.of(s);
        float rowFull = 22.0f * s;
        List<VanishLine> lines = new ArrayList<>();
        for (AntiVanishModule.HudEntry entry : AntiVanishModule.hudEntries()) {
            String tag = AntiVanishModule.hudTag(entry);
            String value = AntiVanishModule.hudValue(entry);
            boolean urgent = "ALERT".equals(tag) || "VANISH".equals(tag);
            lines.add(new VanishLine(tag, value, urgent, HudChrome.width(t.label(), t.labelPx(), tag), HudChrome.width(t.text(), t.textPx(), value)));
        }
        if (state.anchorBottom) {
            Collections.reverse(lines);
        }
        if (lines.isEmpty()) {
            removeSlot("vanish", t.textPx());
            float w = editMode ? (8.0f * s * 2.0f) + HudChrome.width(t.label(), t.labelPx(), NO_VANISH) : 1.0f;
            return new VanishPlan(lines, t, 0.0f, Math.round(w), Math.round(rowFull));
        }
        float tagColumn = 0.0f;
        float valueW = 0.0f;
        for (VanishLine line : lines) {
            tagColumn = Math.max(tagColumn, line.tagW());
            valueW = Math.max(valueW, line.valueW());
        }
        float target = (8.0f * s * 2.0f) + tagColumn + (8.0f * s) + valueW;
        float w2 = slot("vanish", t.textPx(), (float) Math.ceil(target), dt);
        return new VanishPlan(lines, t, tagColumn, Math.round(w2), Math.round(rowFull * lines.size()));
    }

    private void drawVanish(ImDrawList dl, HudLayoutStore.HudElementState state, VanishPlan plan, boolean shadow, DrawTransform tr) {
        float a = tr.alpha();
        if (a <= 0.001f) {
            return;
        }
        float s = scaleOf(state);
        float x = Math.round(state.x + tr.xOffset());
        float y = Math.round(state.y + tr.yOffset());
        float w = state.width;
        float h = state.height;
        Type t = plan.type();
        float rowFull = 22.0f * s;
        float pad = 8.0f * s;
        int count = Math.max(1, plan.lines().size());
        List<Step> steps = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            steps.add(new Step(plan.lines().isEmpty() ? -1 : i, x, Math.round(y + (rowFull * i)), x + w, Math.round(y + (rowFull * (i + 1))), a));
        }
        drawRows(dl, steps, hudSurface(state), x, y, w, h, rowsRadius(s), s, shadow);
        if (plan.lines().isEmpty()) {
            HudChrome.text(dl, t.label(), t.labelPx(), x + pad, y + ((float) Math.floor((rowFull - t.labelPx()) * 0.5f)), HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, a), NO_VANISH, true);
            return;
        }
        float textInset = (float) Math.floor((rowFull - t.textPx()) * 0.5f);
        for (Step q : steps) {
            VanishLine line = plan.lines().get(q.item());
            float ty = q.t() + textInset;
            float labelTop = ty + HudChrome.baselineOffset(t.text(), t.textPx(), t.label(), t.labelPx());
            float tx = q.l() + pad;
            HudChrome.text(dl, t.label(), t.labelPx(), tx, labelTop, HudChrome.scaleAlpha(line.urgent() ? HudChrome.WARN : HudChrome.TEXT_LABEL, a), line.tag(), true);
            HudChrome.text(dl, t.text(), t.textPx(), tx + plan.tagColumn() + (8.0f * s), ty, HudChrome.scaleAlpha(-1, a), line.value(), true);
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$StaffLine.class */
    @Environment(EnvType.CLIENT)
    private static final class StaffLine extends Record {
        private final String name;
        private final int face;
        private final String state;
        private final boolean more;
        private final float nameW;
        private final float stateW;
        private final boolean server;
        private final boolean nearby;
        private final float alert;
        private final String note;
        private final float noteW;
        private final String value;
        private final float valueW;
        private final float serverTagW;

        private StaffLine(String name, int face, String state, boolean more, float nameW, float stateW, boolean server, boolean nearby, float alert, String note, float noteW, String value, float valueW, float serverTagW) {
            this.name = name;
            this.face = face;
            this.state = state;
            this.more = more;
            this.nameW = nameW;
            this.stateW = stateW;
            this.server = server;
            this.nearby = nearby;
            this.alert = alert;
            this.note = note;
            this.noteW = noteW;
            this.value = value;
            this.valueW = valueW;
            this.serverTagW = serverTagW;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, StaffLine.class), StaffLine.class, "name;face;state;more;nameW;stateW;server;nearby;alert;note;noteW;value;valueW;serverTagW", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->face:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->state:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->more:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->nameW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->stateW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->server:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->nearby:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->alert:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->note:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->noteW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->value:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->valueW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->serverTagW:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, StaffLine.class), StaffLine.class, "name;face;state;more;nameW;stateW;server;nearby;alert;note;noteW;value;valueW;serverTagW", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->face:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->state:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->more:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->nameW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->stateW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->server:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->nearby:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->alert:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->note:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->noteW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->value:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->valueW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->serverTagW:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, StaffLine.class, Object.class), StaffLine.class, "name;face;state;more;nameW;stateW;server;nearby;alert;note;noteW;value;valueW;serverTagW", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->face:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->state:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->more:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->nameW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->stateW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->server:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->nearby:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->alert:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->note:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->noteW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->value:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->valueW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffLine;->serverTagW:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public String name() {
            return this.name;
        }

        public int face() {
            return this.face;
        }

        public String state() {
            return this.state;
        }

        public boolean more() {
            return this.more;
        }

        public float nameW() {
            return this.nameW;
        }

        public float stateW() {
            return this.stateW;
        }

        public boolean server() {
            return this.server;
        }

        public boolean nearby() {
            return this.nearby;
        }

        public float alert() {
            return this.alert;
        }

        public String note() {
            return this.note;
        }

        public float noteW() {
            return this.noteW;
        }

        public String value() {
            return this.value;
        }

        public float valueW() {
            return this.valueW;
        }

        public float serverTagW() {
            return this.serverTagW;
        }

        static StaffLine plain(String name, int face, String state, boolean more, float nameW, float stateW) {
            return new StaffLine(name, face, state, more, nameW, stateW, false, false, 0.0f, "", 0.0f, "", 0.0f, 0.0f);
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$StaffPlan.class */
    @Environment(EnvType.CLIENT)
    private static final class StaffPlan extends Record {
        private final List<StaffLine> lines;
        private final Type type;
        private final String count;
        private final float width;
        private final float height;

        private StaffPlan(List<StaffLine> lines, Type type, String count, float width, float height) {
            this.lines = lines;
            this.type = type;
            this.count = count;
            this.width = width;
            this.height = height;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, StaffPlan.class), StaffPlan.class, "lines;type;count;width;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffPlan;->lines:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffPlan;->count:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffPlan;->height:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, StaffPlan.class), StaffPlan.class, "lines;type;count;width;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffPlan;->lines:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffPlan;->count:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffPlan;->height:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, StaffPlan.class, Object.class), StaffPlan.class, "lines;type;count;width;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffPlan;->lines:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffPlan;->count:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$StaffPlan;->height:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public List<StaffLine> lines() {
            return this.lines;
        }

        public Type type() {
            return this.type;
        }

        public String count() {
            return this.count;
        }

        public float width() {
            return this.width;
        }

        public float height() {
            return this.height;
        }
    }

    private StaffPlan planStaff(HudLayoutStore.HudElementState state, boolean editMode, float dt) {
        String str;
        float s = scaleOf(state);
        Type t = Type.of(s);
        float rowFull = 22.0f * s;
        StaffListModule.HudList list = StaffListModule.hudRows();
        List<StaffLine> lines = new ArrayList<>(list.rows().size() + 1);
        for (StaffListModule.HudRow row : list.rows()) {
            int face = staffFace(row.id(), row.nameHidden());
            if (row.presence() == StaffListModule.Presence.VANISHED) {
                str = "VANISHED";
            } else {
                str = row.presence() == StaffListModule.Presence.SPECTATOR ? "SPECTATOR" : "";
            }
            String status = str;
            float statusW = status.isEmpty() ? 0.0f : HudChrome.width(t.label(), t.labelPx(), status);
            if (row.watching()) {
                String value = row.nearby() ? row.distanceLabel() : row.watchingFor();
                float valueW = HudChrome.tabularWidth(t.text(), t.textPx(), value);
                float serverTagW = HudChrome.width(t.label(), t.labelPx(), WATCHING_YOU);
                float nameW = HudChrome.width(t.text(), t.textPx(), row.name()) + (status.isEmpty() ? 0.0f : (8.0f * s) + statusW);
                lines.add(new StaffLine(row.name(), face, WATCHING_YOU, false, nameW, serverTagW + (4.0f * s) + valueW, true, row.nearby(), row.alertLevel(), status, statusW, value, valueW, serverTagW));
            } else {
                lines.add(StaffLine.plain(row.name(), face, status, false, HudChrome.width(t.text(), t.textPx(), row.name()), statusW));
            }
        }
        if (list.more() > 0) {
            String more = "+" + list.more() + " more";
            lines.add(StaffLine.plain(more, 0, "", true, HudChrome.width(t.label(), t.labelPx(), more), 0.0f));
        }
        if (state.anchorBottom) {
            Collections.reverse(lines);
        }
        if (lines.isEmpty()) {
            removeSlot("staff", t.textPx());
            boolean placeholder = editMode || StaffListModule.shouldShowHud();
            float w = placeholder ? (8.0f * s * 2.0f) + HudChrome.width(t.label(), t.labelPx(), NO_STAFF) : 1.0f;
            return new StaffPlan(lines, t, "", Math.round(w), Math.round(rowFull));
        }
        String count = Integer.toString(list.rows().size() + list.more());
        float headerW = HudChrome.width(t.label(), t.labelPx(), STAFF_HEADER) + (5.0f * s) + HudChrome.tabularWidth(t.text(), t.textPx(), count);
        float nameColumn = 0.0f;
        float stateColumn = 0.0f;
        for (StaffLine line : lines) {
            nameColumn = Math.max(nameColumn, line.nameW());
            stateColumn = Math.max(stateColumn, line.stateW());
        }
        float rowW = faceSize(s) + (6.0f * s) + nameColumn + (stateColumn > 0.0f ? (14.0f * s) + stateColumn : 0.0f);
        float target = (8.0f * s * 2.0f) + Math.max(headerW, rowW);
        float w2 = slot("staff", t.textPx(), (float) Math.ceil(target), dt);
        return new StaffPlan(lines, t, count, Math.round(w2), Math.round((20.0f * s) + (rowFull * lines.size())));
    }

    private void drawStaff(ImDrawList dl, HudLayoutStore.HudElementState state, StaffPlan plan, boolean shadow, DrawTransform tr) {
        float a = tr.alpha();
        if (a <= 0.001f) {
            return;
        }
        float s = scaleOf(state);
        float x = Math.round(state.x + tr.xOffset());
        float y = Math.round(state.y + tr.yOffset());
        float w = state.width;
        float h = state.height;
        Type t = plan.type();
        float rowFull = 22.0f * s;
        float pad = 8.0f * s;
        float labelInset = (float) Math.floor((rowFull - t.labelPx()) * 0.5f);
        if (plan.lines().isEmpty()) {
            List<Step> empty = List.of(new Step(-1, x, y, x + w, Math.round(y + rowFull), a));
            drawRows(dl, empty, hudSurface(state), x, y, w, h, rowsRadius(s), s, shadow);
            HudChrome.text(dl, t.label(), t.labelPx(), x + pad, y + labelInset, HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, a), NO_STAFF, true);
            return;
        }
        float headerH = 20.0f * s;
        List<Step> steps = new ArrayList<>(plan.lines().size() + 1);
        steps.add(new Step(-1, x, y, x + w, Math.round(y + headerH), a));
        for (int i = 0; i < plan.lines().size(); i++) {
            steps.add(new Step(i, x, Math.round(y + headerH + (rowFull * i)), x + w, Math.round(y + headerH + (rowFull * (i + 1))), a));
        }
        drawRows(dl, steps, hudSurface(state), x, y, w, h, rowsRadius(s), s, shadow);
        int i2 = 1;
        while (i2 < steps.size()) {
            Step q = steps.get(i2);
            StaffLine line = plan.lines().get(q.item());
            if (line.server()) {
                int corners = convexCorners(q, steps.get(i2 - 1), i2 < steps.size() - 1 ? steps.get(i2 + 1) : null);
                float glow = 0.14f;
                if (line.alert() > 0.0f) {
                    float elapsed = (1.0f - line.alert()) * 4000.0f;
                    float wave = 0.5f + (0.5f * ((float) Math.cos(((((double) elapsed) * 2.0d) * 3.141592653589793d) / 500.0d)));
                    glow = 0.14f + (0.24f * line.alert() * wave);
                }
                int tint = HudChrome.scaleAlpha(HudChrome.WARN, a * glow);
                if (corners == 256) {
                    dl.addRectFilled(q.l(), q.t(), q.r(), q.b(), tint);
                } else {
                    dl.addRectFilled(q.l(), q.t(), q.r(), q.b(), tint, rowsRadius(s), corners);
                }
                dl.addRectFilled(q.l() + (3.0f * s), q.t() + (4.0f * s), q.l() + (5.0f * s), q.b() - (4.0f * s), HudChrome.scaleAlpha(HudChrome.WARN, a), 1.0f * s);
            }
            i2++;
        }
        Step head = steps.get(0);
        float countTop = head.t() + ((float) Math.floor(((head.b() - head.t()) - t.textPx()) * 0.5f));
        HudChrome.text(dl, t.label(), t.labelPx(), x + pad, countTop + HudChrome.baselineOffset(t.text(), t.textPx(), t.label(), t.labelPx()), HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, a), STAFF_HEADER, true);
        HudChrome.tabular(dl, t.text(), t.textPx(), x + pad + HudChrome.width(t.label(), t.labelPx(), STAFF_HEADER) + (5.0f * s), countTop, HudChrome.scaleAlpha(-1, a), plan.count(), true);
        float face = faceSize(s);
        float textInset = (float) Math.floor((rowFull - t.textPx()) * 0.5f);
        float nameX = pad + face + (6.0f * s);
        for (int i3 = 1; i3 < steps.size(); i3++) {
            Step q2 = steps.get(i3);
            StaffLine line2 = plan.lines().get(q2.item());
            if (line2.more()) {
                HudChrome.text(dl, t.label(), t.labelPx(), q2.l() + nameX, q2.t() + labelInset, HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, a), line2.name(), true);
            } else {
                if (line2.face() != 0) {
                    drawFace(dl, line2.face(), Math.round(q2.l() + pad), Math.round(q2.t() + (((q2.b() - q2.t()) - face) * 0.5f)), face, s, a);
                }
                float ty = q2.t() + textInset;
                float labelTop = ty + HudChrome.baselineOffset(t.text(), t.textPx(), t.label(), t.labelPx());
                HudChrome.text(dl, t.text(), t.textPx(), q2.l() + nameX, ty, HudChrome.scaleAlpha(-1, a), line2.name(), true);
                if (line2.server()) {
                    if (!line2.note().isEmpty()) {
                        HudChrome.text(dl, t.label(), t.labelPx(), ((q2.l() + nameX) + line2.nameW()) - line2.noteW(), labelTop, HudChrome.scaleAlpha(HudChrome.WARN, a), line2.note(), true);
                    }
                    float sx = (q2.r() - pad) - line2.stateW();
                    HudChrome.text(dl, t.label(), t.labelPx(), sx, labelTop, HudChrome.scaleAlpha(HudChrome.WARN, a), line2.state(), true);
                    HudChrome.tabular(dl, t.text(), t.textPx(), sx + line2.serverTagW() + (4.0f * s), ty, HudChrome.scaleAlpha(line2.nearby() ? -1 : HudChrome.TEXT_LABEL, a), line2.value(), true);
                } else if (!line2.state().isEmpty()) {
                    HudChrome.text(dl, t.label(), t.labelPx(), (q2.r() - pad) - line2.stateW(), labelTop, HudChrome.scaleAlpha(HudChrome.WARN, a), line2.state(), true);
                }
            }
        }
    }

    private static float faceSize(float s) {
        int size = Math.round(Math.round(16.0f * s) / 8.0f) * 8;
        if (size > (22.0f * s) - (4.0f * s)) {
            size -= 8;
        }
        return Math.max(8, size);
    }

    private static void drawFace(ImDrawList dl, int texture, float x, float y, float size, float s, float a) {
        float r = Math.max(1.0f, Math.round(2.0f * s));
        int tint = HudChrome.scaleAlpha(-1, a);
        dl.addImageRounded(texture, x, y, x + size, y + size, 0.125f, 0.125f, 0.25f, 0.25f, tint, r, 240);
        dl.addImageRounded(texture, x, y, x + size, y + size, SKIN_HAT_U, 0.125f, LYRIC_MIN_COVERAGE, 0.25f, tint, r, 240);
        dl.addRect(x, y, x + size, y + size, HudChrome.scaleAlpha(402653183, a), r, 0, 1.0f);
    }

    int staffFace(UUID id, boolean hidden) {
        class_2960 skin;
        class_310 mc = class_310.method_1551();
        class_634 conn = mc.method_1562();
        class_640 info = conn == null ? null : conn.method_2871(id);
        if (info != null) {
            skin = info.method_52810().comp_1626().comp_3627();
            this.staffSkins.put(id, skin);
            if (hidden) {
                skin = NameProtectModule.hiddenHead(id, info.method_52810()).comp_1626().comp_3627();
            }
        } else if (hidden) {
            skin = NameProtectModule.hiddenHead(id, class_1068.method_4648(id)).comp_1626().comp_3627();
        } else {
            skin = this.staffSkins.get(id);
            if (skin == null) {
                skin = class_1068.method_4648(id).comp_1626().comp_3627();
            }
        }
        try {
            GpuTexture texture = mc.method_1531().method_4619(skin).method_68004();
            if (!(texture instanceof class_10868)) {
                return 0;
            }
            class_10868 gl = (class_10868) texture;
            if (gl.isClosed()) {
                return 0;
            }
            if (this.nearestSkins.add(texture)) {
                int bound = GL11.glGetInteger(32873);
                GL11.glBindTexture(3553, gl.method_68427());
                GL11.glTexParameteri(3553, 10241, 9728);
                GL11.glTexParameteri(3553, 10240, 9728);
                GL11.glBindTexture(3553, bound);
            }
            return gl.method_68427();
        } catch (IllegalStateException e) {
            return 0;
        }
    }

    private float planRadar(HudLayoutStore.HudElementState state, boolean editMode) {
        if (!editMode && !RadarModule.shouldShowHud()) {
            return 1.0f;
        }
        float size = this.radarModule != null ? this.radarModule.size() : RADAR_DEFAULT_SIZE;
        return Math.max(2.0f, Math.round(size * scaleOf(state)));
    }

    private void drawRadar(ImDrawList dl, HudLayoutStore.HudElementState state, float size, class_310 client, boolean shadow, DrawTransform tr) {
        float a = tr.alpha();
        if (a <= 0.001f || size <= 1.0f) {
            return;
        }
        float s = scaleOf(state);
        Type t = Type.of(s);
        float x = Math.round(state.x + tr.xOffset());
        float y = Math.round(state.y + tr.yOffset());
        float r = size * 0.5f;
        float cx = x + r;
        float cy = y + r;
        int accent = hudAccent();
        Surface surface = hudSurface(state);
        if (shadow) {
            drawDiscShadow(dl, cx, cy, r, s, a);
        }
        dl.addCircleFilled(cx, cy, r, HudChrome.scaleAlpha(GuiPalette.fill(surface), a), 0);
        SurfaceBackground.drawDisc(dl, surface, cx, cy, r, a);
        dl.addCircle(cx, cy, r - 0.5f, HudChrome.scaleAlpha(402653183, a), 0, 1.0f);
        measureRadarLetters(t.label(), t.labelPx());
        float letterR = (r - (10.0f * s)) - this.radarLetterHalf;
        float reach = (letterR - this.radarLetterHalf) - (4.0f * s);
        float rangeR = r - (5.0f * s);
        int hairline = HudChrome.scaleAlpha(536870911, a);
        for (float ring : RADAR_RINGS) {
            if (rangeR * ring < reach) {
                dl.addCircle(cx, cy, rangeR * ring, hairline, 0, 1.0f);
            }
        }
        float hx = ((float) Math.floor(cx)) + 0.5f;
        float hy = ((float) Math.floor(cy)) + 0.5f;
        dl.addLine(hx, cy - reach, hx, cy + reach, hairline, 1.0f);
        dl.addLine(cx - reach, hy, cx + reach, hy, hairline, 1.0f);
        class_1657 class_1657Var = client.field_1724;
        boolean rotate = this.radarModule == null || this.radarModule.rotate();
        float partial = client.method_61966().method_60637(false);
        double yaw = class_1657Var != null ? Math.toRadians(class_1657Var.method_5705(partial)) : 0.0d;
        float sin = (float) Math.sin(yaw);
        float cos = (float) Math.cos(yaw);
        int i = 0;
        while (i < RADAR_LETTERS.length) {
            int wx = RADAR_LETTER_DX[i];
            int wz = RADAR_LETTER_DZ[i];
            float ox = rotate ? ((-wx) * cos) - (wz * sin) : wx;
            float oy = rotate ? (wx * sin) - (wz * cos) : wz;
            int color = i == 0 ? accent : HudChrome.TEXT_LABEL;
            HudChrome.text(dl, t.label(), t.labelPx(), Math.round((cx + (ox * letterR)) - (this.radarLetterW[i] * 0.5f)), Math.round((cy + (oy * letterR)) - this.radarLetterMid), HudChrome.scaleAlpha(color, a), RADAR_LETTERS[i], true);
            i++;
        }
        if (this.radarModule != null && class_1657Var != null && client.field_1687 != null) {
            float range = Math.max(1.0f, this.radarModule.range());
            double rangeSq = ((double) range) * ((double) range);
            float perBlock = rangeR / range;
            double px0 = class_3532.method_16436(partial, ((class_746) class_1657Var).field_6014, class_1657Var.method_23317());
            double pz0 = class_3532.method_16436(partial, ((class_746) class_1657Var).field_5969, class_1657Var.method_23321());
            float dot = RADAR_DOT * s;
            float edge = Math.max(1.0f, LYRIC_MIN_COVERAGE * s);
            int dotEdge = HudChrome.scaleAlpha(RADAR_DOT_EDGE, a);
            int dotColor = HudChrome.scaleAlpha(GuiPalette.abgr(this.radarModule.dotColor()), a);
            for (class_1657 class_1657Var2 : client.field_1687.method_18456()) {
                if (class_1657Var2 != class_1657Var && this.radarModule.shows(class_1657Var2)) {
                    double dx = class_3532.method_16436(partial, ((class_742) class_1657Var2).field_6014, class_1657Var2.method_23317()) - px0;
                    double dz = class_3532.method_16436(partial, ((class_742) class_1657Var2).field_5969, class_1657Var2.method_23321()) - pz0;
                    if ((dx * dx) + (dz * dz) <= rangeSq) {
                        float ox2 = rotate ? (float) (((-dx) * ((double) cos)) - (dz * ((double) sin))) : (float) dx;
                        float oy2 = (float) (rotate ? (dx * ((double) sin)) - (dz * ((double) cos)) : dz);
                        float bx = cx + (ox2 * perBlock);
                        float by = cy + (oy2 * perBlock);
                        dl.addCircleFilled(bx, by, dot + edge, dotEdge, 12);
                        dl.addCircleFilled(bx, by, dot, dotColor, 12);
                    }
                }
            }
        }
        dl.addCircleFilled(cx, cy, 6.0f * s, HudChrome.scaleAlpha(accent, a * 0.25f), 0);
        dl.addCircleFilled(cx, cy, 3.0f * s, HudChrome.scaleAlpha(accent, a), 0);
        float ux = rotate ? 0.0f : -sin;
        float uy = rotate ? -1.0f : cos;
        float tip = (r - 1.0f) - (RADAR_MARKER_INSET * s);
        float base = tip - (5.0f * s);
        float half = RADAR_MARKER_W * s;
        dl.addTriangleFilled(cx + (ux * tip), cy + (uy * tip), (cx + (ux * base)) - (uy * half), cy + (uy * base) + (ux * half), cx + (ux * base) + (uy * half), (cy + (uy * base)) - (ux * half), HudChrome.scaleAlpha(accent, a));
    }

    private static void drawDiscShadow(ImDrawList dl, float cx, float cy, float r, float s, float a) {
        float sp = Math.max(1.0f, s);
        int color = HudChrome.scaleAlpha(218103808, a);
        for (int k = 3; k >= 1; k--) {
            dl.addCircleFilled(cx, cy + (sp * 0.5f), r + ((k + 0.5f) * sp), color, 0);
        }
    }

    private void measureRadarLetters(ImFont font, int px) {
        if (font == this.radarLetterFont && px == this.radarLetterPx) {
            return;
        }
        this.radarLetterFont = font;
        this.radarLetterPx = px;
        for (int i = 0; i < RADAR_LETTERS.length; i++) {
            this.radarLetterW[i] = HudChrome.width(font, px, RADAR_LETTERS[i]);
        }
        float capTop = capTop(font, px);
        float ascent = HudChrome.ascent(font, px);
        this.radarLetterMid = (capTop + ascent) * 0.5f;
        this.radarLetterHalf = (ascent - capTop) * 0.5f;
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$HotkeyLine.class */
    @Environment(EnvType.CLIENT)
    private static final class HotkeyLine extends Record {
        private final String name;
        private final String key;
        private final boolean on;
        private final float nameW;
        private final float keyW;

        private HotkeyLine(String name, String key, boolean on, float nameW, float keyW) {
            this.name = name;
            this.key = key;
            this.on = on;
            this.nameW = nameW;
            this.keyW = keyW;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, HotkeyLine.class), HotkeyLine.class, "name;key;on;nameW;keyW", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyLine;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyLine;->key:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyLine;->on:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyLine;->nameW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyLine;->keyW:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, HotkeyLine.class), HotkeyLine.class, "name;key;on;nameW;keyW", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyLine;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyLine;->key:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyLine;->on:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyLine;->nameW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyLine;->keyW:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, HotkeyLine.class, Object.class), HotkeyLine.class, "name;key;on;nameW;keyW", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyLine;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyLine;->key:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyLine;->on:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyLine;->nameW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyLine;->keyW:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public String name() {
            return this.name;
        }

        public String key() {
            return this.key;
        }

        public boolean on() {
            return this.on;
        }

        public float nameW() {
            return this.nameW;
        }

        public float keyW() {
            return this.keyW;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$HotkeyPlan.class */
    @Environment(EnvType.CLIENT)
    private static final class HotkeyPlan extends Record {
        private final List<HotkeyLine> lines;
        private final Type type;
        private final float width;
        private final float height;

        private HotkeyPlan(List<HotkeyLine> lines, Type type, float width, float height) {
            this.lines = lines;
            this.type = type;
            this.width = width;
            this.height = height;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, HotkeyPlan.class), HotkeyPlan.class, "lines;type;width;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyPlan;->lines:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyPlan;->height:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, HotkeyPlan.class), HotkeyPlan.class, "lines;type;width;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyPlan;->lines:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyPlan;->height:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, HotkeyPlan.class, Object.class), HotkeyPlan.class, "lines;type;width;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyPlan;->lines:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$HotkeyPlan;->height:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public List<HotkeyLine> lines() {
            return this.lines;
        }

        public Type type() {
            return this.type;
        }

        public float width() {
            return this.width;
        }

        public float height() {
            return this.height;
        }
    }

    private HotkeyPlan planHotkeys(HudLayoutStore.HudElementState state, boolean editMode, float dt) {
        float s = scaleOf(state);
        Type t = Type.of(s);
        float rowFull = 22.0f * s;
        List<HotkeyLine> lines = (editMode || HotkeysModule.shouldShowHud()) ? hotkeyLinesFor(t) : List.of();
        if (lines.isEmpty()) {
            removeSlot(HudLayoutStore.HOTKEYS_ID, t.textPx());
            float w = editMode ? (8.0f * s * 2.0f) + HudChrome.width(t.label(), t.labelPx(), NO_HOTKEYS) : 1.0f;
            return new HotkeyPlan(lines, t, Math.round(w), Math.round(rowFull));
        }
        float rows = this.hotkeyNameColumn + (16.0f * s) + keycapWidth(this.hotkeyKeyColumn, s);
        float target = (8.0f * s * 2.0f) + Math.max(this.hotkeyHeaderW, rows);
        float w2 = slot(HudLayoutStore.HOTKEYS_ID, t.textPx(), (float) Math.ceil(target), dt);
        return new HotkeyPlan(lines, t, Math.round(w2), Math.round((20.0f * s) + (rowFull * lines.size())));
    }

    private List<HotkeyLine> hotkeyLinesFor(Type t) {
        long revision = ModuleRevision.current();
        if (revision == this.hotkeyRevision && t.text() == this.hotkeyFont && t.textPx() == this.hotkeyTextPx && t.labelPx() == this.hotkeyKeyPx) {
            return this.hotkeyLines;
        }
        this.hotkeyRevision = revision;
        this.hotkeyFont = t.text();
        this.hotkeyTextPx = t.textPx();
        this.hotkeyKeyPx = t.labelPx();
        List<HotkeyLine> lines = new ArrayList<>();
        float nameColumn = 0.0f;
        float keyColumn = 0.0f;
        for (ClientModule module : ModuleRegistry.all()) {
            int key = module.keybind();
            if (key != -1) {
                String label = ClickGuiRenderer.keyName(key).toUpperCase(Locale.ROOT);
                float nameW = HudChrome.width(t.text(), t.textPx(), module.name());
                float keyW = HudChrome.width(t.text(), t.labelPx(), label);
                lines.add(new HotkeyLine(module.name(), label, module.isEnabled(), nameW, keyW));
                nameColumn = Math.max(nameColumn, nameW);
                keyColumn = Math.max(keyColumn, keyW);
            }
        }
        this.hotkeyNameColumn = nameColumn;
        this.hotkeyKeyColumn = keyColumn;
        this.hotkeyHeaderW = HudChrome.width(t.label(), t.labelPx(), HOTKEYS_HEADER);
        this.hotkeyCapMid = (capTop(t.text(), t.labelPx()) + HudChrome.ascent(t.text(), t.labelPx())) * 0.5f;
        this.hotkeyLines = List.copyOf(lines);
        return this.hotkeyLines;
    }

    private void drawHotkeys(ImDrawList dl, HudLayoutStore.HudElementState state, HotkeyPlan plan, boolean shadow, DrawTransform tr) {
        float a = tr.alpha();
        if (a <= 0.001f) {
            return;
        }
        float s = scaleOf(state);
        float x = Math.round(state.x + tr.xOffset());
        float y = Math.round(state.y + tr.yOffset());
        float w = state.width;
        float h = state.height;
        Type t = plan.type();
        float rowFull = 22.0f * s;
        float pad = 8.0f * s;
        if (plan.lines().isEmpty()) {
            List<Step> empty = List.of(new Step(-1, x, y, x + w, Math.round(y + rowFull), a));
            drawRows(dl, empty, hudSurface(state), x, y, w, h, rowsRadius(s), s, shadow);
            HudChrome.text(dl, t.label(), t.labelPx(), x + pad, y + ((float) Math.floor((rowFull - t.labelPx()) * 0.5f)), HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, a), NO_HOTKEYS, true);
            return;
        }
        float headerH = 20.0f * s;
        List<Step> steps = new ArrayList<>(plan.lines().size() + 1);
        steps.add(new Step(-1, x, y, x + w, Math.round(y + headerH), a));
        for (int i = 0; i < plan.lines().size(); i++) {
            steps.add(new Step(i, x, Math.round(y + headerH + (rowFull * i)), x + w, Math.round(y + headerH + (rowFull * (i + 1))), a));
        }
        drawRows(dl, steps, hudSurface(state), x, y, w, h, rowsRadius(s), s, shadow);
        Step head = steps.get(0);
        float countTop = head.t() + ((float) Math.floor(((head.b() - head.t()) - t.textPx()) * 0.5f));
        float labelTop = countTop + HudChrome.baselineOffset(t.text(), t.textPx(), t.label(), t.labelPx());
        HudChrome.text(dl, t.label(), t.labelPx(), x + pad, labelTop, HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, a), HOTKEYS_HEADER, true);
        float capW = keycapWidth(this.hotkeyKeyColumn, s);
        float textInset = (float) Math.floor((rowFull - t.textPx()) * 0.5f);
        for (int i2 = 1; i2 < steps.size(); i2++) {
            Step q = steps.get(i2);
            HotkeyLine line = plan.lines().get(q.item());
            HudChrome.text(dl, t.text(), t.textPx(), q.l() + pad, q.t() + textInset, HudChrome.scaleAlpha(line.on() ? -1 : HudChrome.TEXT_LABEL, a), line.name(), true);
            drawKeycap(dl, t, line, q.r() - pad, (q.t() + q.b()) * 0.5f, capW, s, a);
        }
    }

    private void drawKeycap(ImDrawList dl, Type t, HotkeyLine line, float right, float centerY, float capW, float s, float a) {
        float capH = keycapHeight(s);
        float l = Math.round(right - capW);
        float top = Math.round(centerY - (capH * 0.5f));
        float r = Math.min(hudRounding(), 4.0f) * s;
        dl.addRectFilled(l, top, l + capW, top + capH, HudChrome.scaleAlpha(452984831, a), r);
        dl.addRect(l, top, l + capW, top + capH, HudChrome.scaleAlpha(402653183, a), r, 0, 1.0f);
        HudChrome.text(dl, t.text(), t.labelPx(), Math.round(l + ((capW - line.keyW()) * 0.5f)), Math.round((top + (capH * 0.5f)) - this.hotkeyCapMid), HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, a), line.key(), true);
    }

    private static float keycapHeight(float s) {
        return Math.round(16.0f * s);
    }

    private static float keycapWidth(float keyW, float s) {
        return Math.max(keycapHeight(s), (float) Math.ceil(keyW + (5.0f * s * 2.0f)));
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$SpotifyPlan.class */
    @Environment(EnvType.CLIENT)
    private static final class SpotifyPlan extends Record {
        private final Type type;
        private final SpotifyLook look;
        private final SpotifyGlyph glyph;
        private final String title;
        private final String artist;
        private final float titleW;
        private final float artistW;
        private final float titleOffset;
        private final float artistOffset;
        private final float loopGap;
        private final boolean scroll;
        private final SpotifyCard card;
        private final SpotifyColors colors;
        private final float width;
        private final float height;

        private SpotifyPlan(Type type, SpotifyLook look, SpotifyGlyph glyph, String title, String artist, float titleW, float artistW, float titleOffset, float artistOffset, float loopGap, boolean scroll, SpotifyCard card, SpotifyColors colors, float width, float height) {
            this.type = type;
            this.look = look;
            this.glyph = glyph;
            this.title = title;
            this.artist = artist;
            this.titleW = titleW;
            this.artistW = artistW;
            this.titleOffset = titleOffset;
            this.artistOffset = artistOffset;
            this.loopGap = loopGap;
            this.scroll = scroll;
            this.card = card;
            this.colors = colors;
            this.width = width;
            this.height = height;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, SpotifyPlan.class), SpotifyPlan.class, "type;look;glyph;title;artist;titleW;artistW;titleOffset;artistOffset;loopGap;scroll;card;colors;width;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->look:Lnet/anubis/client/gui/HudRenderer$SpotifyLook;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->glyph:Lnet/anubis/client/gui/HudRenderer$SpotifyGlyph;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->title:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->artist:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->titleW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->artistW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->titleOffset:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->artistOffset:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->loopGap:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->scroll:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->card:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->colors:Lnet/anubis/client/gui/HudRenderer$SpotifyColors;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->height:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, SpotifyPlan.class), SpotifyPlan.class, "type;look;glyph;title;artist;titleW;artistW;titleOffset;artistOffset;loopGap;scroll;card;colors;width;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->look:Lnet/anubis/client/gui/HudRenderer$SpotifyLook;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->glyph:Lnet/anubis/client/gui/HudRenderer$SpotifyGlyph;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->title:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->artist:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->titleW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->artistW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->titleOffset:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->artistOffset:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->loopGap:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->scroll:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->card:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->colors:Lnet/anubis/client/gui/HudRenderer$SpotifyColors;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->height:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, SpotifyPlan.class, Object.class), SpotifyPlan.class, "type;look;glyph;title;artist;titleW;artistW;titleOffset;artistOffset;loopGap;scroll;card;colors;width;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->type:Lnet/anubis/client/gui/HudRenderer$Type;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->look:Lnet/anubis/client/gui/HudRenderer$SpotifyLook;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->glyph:Lnet/anubis/client/gui/HudRenderer$SpotifyGlyph;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->title:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->artist:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->titleW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->artistW:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->titleOffset:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->artistOffset:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->loopGap:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->scroll:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->card:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->colors:Lnet/anubis/client/gui/HudRenderer$SpotifyColors;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->width:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyPlan;->height:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public Type type() {
            return this.type;
        }

        public SpotifyLook look() {
            return this.look;
        }

        public SpotifyGlyph glyph() {
            return this.glyph;
        }

        public String title() {
            return this.title;
        }

        public String artist() {
            return this.artist;
        }

        public float titleW() {
            return this.titleW;
        }

        public float artistW() {
            return this.artistW;
        }

        public float titleOffset() {
            return this.titleOffset;
        }

        public float artistOffset() {
            return this.artistOffset;
        }

        public float loopGap() {
            return this.loopGap;
        }

        public boolean scroll() {
            return this.scroll;
        }

        public SpotifyCard card() {
            return this.card;
        }

        public SpotifyColors colors() {
            return this.colors;
        }

        public float width() {
            return this.width;
        }

        public float height() {
            return this.height;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$SpotifyColors.class */
    @Environment(EnvType.CLIENT)
    private static final class SpotifyColors extends Record {
        private final int title;
        private final int artist;
        private final int lyric;
        private final int upcoming;
        private final int lit;
        static final SpotifyColors DEFAULT = new SpotifyColors(HudChrome.TEXT_NAME, HudChrome.TEXT_LABEL, HudChrome.TEXT_VALUE, HudChrome.TEXT_LABEL, HudChrome.TEXT_VALUE);

        private SpotifyColors(int title, int artist, int lyric, int upcoming, int lit) {
            this.title = title;
            this.artist = artist;
            this.lyric = lyric;
            this.upcoming = upcoming;
            this.lit = lit;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, SpotifyColors.class), SpotifyColors.class, "title;artist;lyric;upcoming;lit", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyColors;->title:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyColors;->artist:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyColors;->lyric:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyColors;->upcoming:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyColors;->lit:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, SpotifyColors.class), SpotifyColors.class, "title;artist;lyric;upcoming;lit", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyColors;->title:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyColors;->artist:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyColors;->lyric:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyColors;->upcoming:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyColors;->lit:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, SpotifyColors.class, Object.class), SpotifyColors.class, "title;artist;lyric;upcoming;lit", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyColors;->title:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyColors;->artist:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyColors;->lyric:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyColors;->upcoming:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyColors;->lit:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int title() {
            return this.title;
        }

        public int artist() {
            return this.artist;
        }

        public int lyric() {
            return this.lyric;
        }

        public int upcoming() {
            return this.upcoming;
        }

        public int lit() {
            return this.lit;
        }

        static SpotifyColors of(SpotifyHudModule module) {
            if (module == null) {
                return DEFAULT;
            }
            SpotifyHudModule.LyricHighlight mode = module.lyricHighlight();
            int lyric = GuiPalette.abgr(module.lyricsColor());
            int highlight = GuiPalette.abgr(module.highlightColor());
            return new SpotifyColors(GuiPalette.abgr(module.titleColor()), GuiPalette.abgr(module.artistColor()), mode == SpotifyHudModule.LyricHighlight.LINE ? highlight : lyric, GuiPalette.abgr(module.upcomingLyricsColor()), mode == SpotifyHudModule.LyricHighlight.OFF ? lyric : highlight);
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$SpotifyCard.class */
    @Environment(EnvType.CLIENT)
    private static final class SpotifyCard extends Record {
        private final NowPlaying track;
        private final boolean sample;
        private final boolean art;
        private final float header;
        private final float textTop;
        private final float column;
        private final boolean timeline;
        private final String elapsed;
        private final String remaining;
        private final float progress;
        private final boolean progressBar;
        private final LyricView lyrics;
        private final float lyricAlpha;

        private SpotifyCard(NowPlaying track, boolean sample, boolean art, float header, float textTop, float column, boolean timeline, String elapsed, String remaining, float progress, boolean progressBar, LyricView lyrics, float lyricAlpha) {
            this.track = track;
            this.sample = sample;
            this.art = art;
            this.header = header;
            this.textTop = textTop;
            this.column = column;
            this.timeline = timeline;
            this.elapsed = elapsed;
            this.remaining = remaining;
            this.progress = progress;
            this.progressBar = progressBar;
            this.lyrics = lyrics;
            this.lyricAlpha = lyricAlpha;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, SpotifyCard.class), SpotifyCard.class, "track;sample;art;header;textTop;column;timeline;elapsed;remaining;progress;progressBar;lyrics;lyricAlpha", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->track:Lnet/anubis/client/util/spotify/NowPlaying;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->sample:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->art:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->header:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->textTop:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->column:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->timeline:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->elapsed:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->remaining:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->progress:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->progressBar:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->lyrics:Lnet/anubis/client/gui/HudRenderer$LyricView;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->lyricAlpha:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, SpotifyCard.class), SpotifyCard.class, "track;sample;art;header;textTop;column;timeline;elapsed;remaining;progress;progressBar;lyrics;lyricAlpha", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->track:Lnet/anubis/client/util/spotify/NowPlaying;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->sample:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->art:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->header:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->textTop:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->column:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->timeline:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->elapsed:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->remaining:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->progress:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->progressBar:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->lyrics:Lnet/anubis/client/gui/HudRenderer$LyricView;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->lyricAlpha:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, SpotifyCard.class, Object.class), SpotifyCard.class, "track;sample;art;header;textTop;column;timeline;elapsed;remaining;progress;progressBar;lyrics;lyricAlpha", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->track:Lnet/anubis/client/util/spotify/NowPlaying;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->sample:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->art:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->header:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->textTop:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->column:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->timeline:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->elapsed:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->remaining:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->progress:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->progressBar:Z", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->lyrics:Lnet/anubis/client/gui/HudRenderer$LyricView;", "FIELD:Lnet/anubis/client/gui/HudRenderer$SpotifyCard;->lyricAlpha:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public NowPlaying track() {
            return this.track;
        }

        public boolean sample() {
            return this.sample;
        }

        public boolean art() {
            return this.art;
        }

        public float header() {
            return this.header;
        }

        public float textTop() {
            return this.textTop;
        }

        public float column() {
            return this.column;
        }

        public boolean timeline() {
            return this.timeline;
        }

        public String elapsed() {
            return this.elapsed;
        }

        public String remaining() {
            return this.remaining;
        }

        public float progress() {
            return this.progress;
        }

        public boolean progressBar() {
            return this.progressBar;
        }

        public LyricView lyrics() {
            return this.lyrics;
        }

        public float lyricAlpha() {
            return this.lyricAlpha;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$LyricItem.class */
    @Environment(EnvType.CLIENT)
    private static final class LyricItem extends Record {
        private final String text;
        private final float top;
        private final float highlight;
        private final float alpha;
        private final float progress;
        private final float fill;

        private LyricItem(String text, float top, float highlight, float alpha, float progress, float fill) {
            this.text = text;
            this.top = top;
            this.highlight = highlight;
            this.alpha = alpha;
            this.progress = progress;
            this.fill = fill;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, LyricItem.class), LyricItem.class, "text;top;highlight;alpha;progress;fill", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricItem;->text:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricItem;->top:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricItem;->highlight:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricItem;->alpha:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricItem;->progress:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricItem;->fill:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, LyricItem.class), LyricItem.class, "text;top;highlight;alpha;progress;fill", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricItem;->text:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricItem;->top:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricItem;->highlight:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricItem;->alpha:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricItem;->progress:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricItem;->fill:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, LyricItem.class, Object.class), LyricItem.class, "text;top;highlight;alpha;progress;fill", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricItem;->text:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricItem;->top:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricItem;->highlight:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricItem;->alpha:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricItem;->progress:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricItem;->fill:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public String text() {
            return this.text;
        }

        public float top() {
            return this.top;
        }

        public float highlight() {
            return this.highlight;
        }

        public float alpha() {
            return this.alpha;
        }

        public float progress() {
            return this.progress;
        }

        public float fill() {
            return this.fill;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$LyricWords.class */
    @Environment(EnvType.CLIENT)
    private static final class LyricWords extends Record {
        private final int[] from;
        private final int[] to;
        private final long[] start;
        private final long[] sweep;

        private LyricWords(int[] from, int[] to, long[] start, long[] sweep) {
            this.from = from;
            this.to = to;
            this.start = start;
            this.sweep = sweep;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, LyricWords.class), LyricWords.class, "from;to;start;sweep", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricWords;->from:[I", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricWords;->to:[I", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricWords;->start:[J", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricWords;->sweep:[J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, LyricWords.class), LyricWords.class, "from;to;start;sweep", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricWords;->from:[I", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricWords;->to:[I", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricWords;->start:[J", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricWords;->sweep:[J").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, LyricWords.class, Object.class), LyricWords.class, "from;to;start;sweep", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricWords;->from:[I", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricWords;->to:[I", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricWords;->start:[J", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricWords;->sweep:[J").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int[] from() {
            return this.from;
        }

        public int[] to() {
            return this.to;
        }

        public long[] start() {
            return this.start;
        }

        public long[] sweep() {
            return this.sweep;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$LyricShift.class */
    @Environment(EnvType.CLIENT)
    private static final class LyricShift extends Record {
        private final float distance;
        private final float at;
        private final int start;
        private final float stagger;

        private LyricShift(float distance, float at, int start, float stagger) {
            this.distance = distance;
            this.at = at;
            this.start = start;
            this.stagger = stagger;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, LyricShift.class), LyricShift.class, "distance;at;start;stagger", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricShift;->distance:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricShift;->at:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricShift;->start:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricShift;->stagger:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, LyricShift.class), LyricShift.class, "distance;at;start;stagger", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricShift;->distance:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricShift;->at:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricShift;->start:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricShift;->stagger:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, LyricShift.class, Object.class), LyricShift.class, "distance;at;start;stagger", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricShift;->distance:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricShift;->at:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricShift;->start:I", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricShift;->stagger:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public float distance() {
            return this.distance;
        }

        public float at() {
            return this.at;
        }

        public int start() {
            return this.start;
        }

        public float stagger() {
            return this.stagger;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$LyricView.class */
    @Environment(EnvType.CLIENT)
    private static final class LyricView extends Record {
        private final List<LyricItem> items;
        private final String status;
        private final float height;

        private LyricView(List<LyricItem> items, String status, float height) {
            this.items = items;
            this.status = status;
            this.height = height;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, LyricView.class), LyricView.class, "items;status;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricView;->items:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricView;->status:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricView;->height:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, LyricView.class), LyricView.class, "items;status;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricView;->items:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricView;->status:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricView;->height:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, LyricView.class, Object.class), LyricView.class, "items;status;height", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricView;->items:Ljava/util/List;", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricView;->status:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/HudRenderer$LyricView;->height:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public List<LyricItem> items() {
            return this.items;
        }

        public String status() {
            return this.status;
        }

        public float height() {
            return this.height;
        }
    }

    private SpotifyPlan planSpotify(HudLayoutStore.HudElementState state, boolean editMode, float dt) {
        float s = scaleOf(state);
        Type t = Type.of(s);
        if (!editMode && !SpotifyHudModule.shouldShowHud()) {
            removeSlot(HudLayoutStore.SPOTIFY_ID, t.textPx());
            releaseSpotifyArt();
            this.lyricBlockH = 0.0f;
            return new SpotifyPlan(t, SpotifyLook.HIDDEN, SpotifyGlyph.NONE, "", "", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, false, null, SpotifyColors.DEFAULT, 1.0f, Math.round(CAP_H * s));
        }
        SpotifyHudModule module = SpotifyHudModule.get();
        SpotifyHudModule active = SpotifyHudModule.active();
        NowPlaying now = active != null ? active.nowPlaying() : NowPlaying.WAITING;
        boolean track = now.active() && now.hasTrack();
        boolean lyricsSeen = editMode || state.visible;
        if (track && now.source() == NowPlaying.Source.SESSION) {
            return planSpotifyCard(module, now, false, lyricsSeen, t, s, editMode, dt);
        }
        if (editMode && !track) {
            return planSpotifyCard(module, SAMPLE_TRACK, true, true, t, s, true, dt);
        }
        releaseSpotifyArt();
        this.lyricBlockH = 0.0f;
        return planSpotifyCompact(module, now, t, s, editMode, dt);
    }

    private SpotifyPlan planSpotifyCompact(SpotifyHudModule module, NowPlaying now, Type t, float s, boolean editMode, float dt) {
        String title;
        float fTextPx;
        boolean withArtist = module == null || module.showArtist();
        String artist = "";
        SpotifyGlyph glyph = SpotifyGlyph.NONE;
        boolean idle = false;
        switch (now.status()) {
            case PLAYING:
            case PAUSED:
                glyph = now.playing() ? SpotifyGlyph.PLAYING : SpotifyGlyph.PAUSED;
                foldTrack(now, t.text());
                title = now.hasTrack() ? this.spotifyTitle : "Not playing";
                if (withArtist) {
                    artist = this.spotifyArtist;
                }
                break;
            case NOT_RUNNING:
                title = "Spotify is not open";
                idle = true;
                break;
            case UNSUPPORTED:
                title = "Spotify · Windows only";
                idle = true;
                break;
            default:
                title = "Nothing playing";
                idle = true;
                break;
        }
        ImFont titleFont = idle ? t.label() : t.text();
        int titlePx = idle ? t.labelPx() : t.textPx();
        float titleW = HudChrome.width(titleFont, titlePx, title);
        float artistW = artist.isEmpty() ? 0.0f : HudChrome.width(t.label(), t.labelPx(), artist);
        float glyphW = glyph == SpotifyGlyph.NONE ? 0.0f : 20.0f * s;
        float fixed = (12.0f * s * 2.0f) + glyphW;
        float maxText = Math.max(40.0f * s, ((module != null ? module.cardWidth() : CARD_DEFAULT_WIDTH) * s) - fixed);
        float textW = Math.min(Math.max(titleW, artistW), maxText);
        float w = slot(HudLayoutStore.SPOTIFY_ID, t.textPx(), (float) Math.ceil(fixed + textW), dt);
        if (artist.isEmpty()) {
            fTextPx = CAP_H * s;
        } else {
            fTextPx = (8.0f * s * 2.0f) + t.textPx() + (2.0f * s) + t.labelPx();
        }
        float height = fTextPx;
        float avail = Math.max(1.0f, w - fixed);
        boolean scroll = module == null || module.scrollTitles();
        float loopGap = 40.0f * s;
        float[] offsets = marquee(title, artist, titleW, artistW, avail, scroll, now.playing() || editMode, loopGap, s, dt);
        return new SpotifyPlan(t, idle ? SpotifyLook.IDLE : SpotifyLook.COMPACT, glyph, title, artist, titleW, artistW, offsets[0], offsets[1], loopGap, scroll, null, SpotifyColors.of(module), Math.round(w), Math.round(height));
    }

    private void drawSpotify(ImDrawList dl, HudLayoutStore.HudElementState state, SpotifyPlan plan, boolean shadow, DrawTransform tr) {
        float a = tr.alpha();
        if (a <= 0.001f || plan.look() == SpotifyLook.HIDDEN) {
            return;
        }
        float s = scaleOf(state);
        float x = Math.round(state.x + tr.xOffset());
        float y = Math.round(state.y + tr.yOffset());
        float w = state.width;
        float h = state.height;
        Surface surface = hudSurface(state);
        boolean guard = HudChrome.needsTextGuard(surface);
        HudChrome.card(dl, surface, x, y, w, h, hudRounding() * s, a, shadow, s);
        if (plan.look() != SpotifyLook.CARD) {
            drawSpotifyCompact(dl, plan, x, y, w, h, s, a, guard);
        } else {
            drawSpotifyCard(dl, plan, x, y, w, h, s, a, guard);
        }
    }

    private void drawSpotifyCompact(ImDrawList dl, SpotifyPlan plan, float x, float y, float w, float h, float s, float a, boolean guard) {
        Type t = plan.type();
        float cx = x + (12.0f * s);
        if (plan.glyph() != SpotifyGlyph.NONE) {
            drawPlayState(dl, cx, y + (h * 0.5f), 10.0f * s, plan.glyph() == SpotifyGlyph.PLAYING, a);
            cx += 20.0f * s;
        }
        float avail = Math.max(1.0f, ((x + w) - (12.0f * s)) - cx);
        if (plan.look() == SpotifyLook.IDLE) {
            float top = y + ((float) Math.floor((h - t.labelPx()) * 0.5f));
            marqueeLine(dl, t.label(), t.labelPx(), cx, top, avail, plan.titleW(), 0.0f, plan.loopGap(), false, plan.title(), HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, a), guard);
            return;
        }
        float dim = plan.glyph() == SpotifyGlyph.PAUSED ? 0.62f : 1.0f;
        boolean twoLines = !plan.artist().isEmpty();
        float contentH = twoLines ? t.textPx() + (2.0f * s) + t.labelPx() : t.textPx();
        float titleTop = y + ((float) Math.floor((h - contentH) * 0.5f));
        marqueeLine(dl, t.text(), t.textPx(), cx, titleTop, avail, plan.titleW(), plan.titleOffset(), plan.loopGap(), plan.scroll(), plan.title(), HudChrome.scaleAlpha(plan.colors().title(), a * dim), guard);
        if (twoLines) {
            float artistTop = titleTop + t.textPx() + (2.0f * s);
            marqueeLine(dl, t.label(), t.labelPx(), cx, artistTop, avail, plan.artistW(), plan.artistOffset(), plan.loopGap(), plan.scroll(), plan.artist(), HudChrome.scaleAlpha(plan.colors().artist(), a), guard);
        }
    }

    private void drawPlayState(ImDrawList dl, float left, float centerY, float size, boolean playing, float a) {
        float top = Math.round(centerY - (size * 0.5f));
        float bottom = top + Math.round(size);
        if (playing) {
            dl.addTriangleFilled(left, top, left + (size * 0.9f), (top + bottom) * 0.5f, left, bottom, HudChrome.scaleAlpha(hudAccent(), a));
            return;
        }
        float bar = Math.max(2.0f, Math.round(size * LYRIC_ARRIVE_TIME));
        float gap = Math.max(2.0f, Math.round(size * 0.25f));
        int color = HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, a);
        float l = Math.round(left + (((size - (bar * 2.0f)) - gap) * 0.5f));
        dl.addRectFilled(l, top, l + bar, bottom, color, 1.0f);
        dl.addRectFilled(l + bar + gap, top, l + (bar * 2.0f) + gap, bottom, color, 1.0f);
    }

    private SpotifyPlan planSpotifyCard(SpotifyHudModule module, NowPlaying track, boolean sample, boolean lyricsSeen, Type t, float s, boolean editMode, float dt) {
        float fAscent;
        boolean z;
        Lyrics lyricsLyricsFor;
        boolean withArtist = module == null || module.showArtist();
        boolean withArt = module == null || module.showAlbumArt();
        boolean withProgress = module == null || module.showProgress();
        boolean withLyrics = module == null || module.showLyrics();
        boolean scroll = module == null || module.scrollTitles();
        long nowMs = System.currentTimeMillis();
        foldTrack(track, t.text());
        String title = this.spotifyTitle;
        String artist = withArtist ? this.spotifyArtist : "";
        boolean timeline = track.hasTimeline();
        long position = track.positionMs();
        if (!sample) {
            this.spotifyClock.update(track, nowMs);
            position = this.spotifyClock.positionAt(nowMs);
        }
        float capTop = capTop(t.label(), t.labelPx());
        float transportTop = t.labelPx() + (3.0f * s) + t.textPx() + (artist.isEmpty() ? 0.0f : (1.0f * s) + t.labelPx()) + (7.0f * s);
        boolean progressBar = withProgress && timeline;
        if (progressBar) {
            fAscent = transportTop + t.labelPx() + (6.0f * s) + progressHeight(s);
        } else {
            fAscent = transportTop + HudChrome.ascent(t.label(), t.labelPx());
        }
        float columnBottom = fAscent;
        float header = Math.round(columnBottom - capTop);
        float pad = 10.0f * s;
        float w = Math.round(Math.max(CARD_MIN_WIDTH, module != null ? module.cardWidth() : CARD_DEFAULT_WIDTH) * s);
        boolean art = withArt && ((w - (pad * 2.0f)) - header) - (10.0f * s) >= ART_MIN_COLUMN * s;
        float column = (w - (pad * 2.0f)) - (art ? header + (10.0f * s) : 0.0f);
        String elapsed = "";
        String remaining = "";
        float progress = 0.0f;
        if (timeline) {
            long elapsedSeconds = position / 1000;
            long totalSeconds = (track.durationMs() + 500) / 1000;
            elapsed = formatSeconds(elapsedSeconds);
            remaining = "−" + formatSeconds(Math.max(0L, totalSeconds - elapsedSeconds));
            progress = clamp01(position / track.durationMs());
        }
        float titleW = HudChrome.width(t.text(), t.textPx(), title);
        float artistW = artist.isEmpty() ? 0.0f : HudChrome.width(t.label(), t.labelPx(), artist);
        float loopGap = 40.0f * s;
        float[] offsets = marquee(title, artist, titleW, artistW, column, scroll, track.playing() || editMode, loopGap, s, dt);
        if (sample) {
            z = withLyrics && timeline;
        } else {
            z = lyricsSeen && module != null && module.wantsLyrics(track);
        }
        boolean lyricsWanted = z;
        if (lyricsWanted) {
            lyricsLyricsFor = sample ? SAMPLE_LYRICS : module.lyricsFor(track);
        } else {
            lyricsLyricsFor = null;
        }
        Lyrics lyrics = lyricsLyricsFor;
        boolean online = module == null || module.lyricsOnline();
        int lines = module == null ? 2 : module.lyricLines();
        SpotifyHudModule.LyricMotion motion = module == null ? SpotifyHudModule.LyricMotion.WAVE : module.lyricMotion();
        SpotifyHudModule.LyricHighlight highlight = module == null ? SpotifyHudModule.LyricHighlight.OFF : module.lyricHighlight();
        LyricView view = planLyrics(lyrics, lyricsWanted, online, position, lines, motion, highlight, t, w - (pad * 2.0f), s, dt, nowMs);
        float playing = track.playing() ? 1.0f : 0.0f;
        this.lyricPlaying += (playing - this.lyricPlaying) * (1.0f - ((float) Math.exp((-8.0f) * dt)));
        float lyricAlpha = LYRIC_PAUSED_ALPHA + (LYRIC_ARRIVE_TIME * this.lyricPlaying);
        SpotifyCard card = new SpotifyCard(track, sample, art, header, pad - capTop, column, timeline, elapsed, remaining, progress, progressBar, view, lyricAlpha);
        float height = (pad * 2.0f) + header + view.height();
        return new SpotifyPlan(t, SpotifyLook.CARD, SpotifyGlyph.NONE, title, artist, titleW, artistW, offsets[0], offsets[1], loopGap, scroll, card, SpotifyColors.of(module), w, Math.round(height));
    }

    private void drawSpotifyCard(ImDrawList dl, SpotifyPlan plan, float x, float y, float w, float h, float s, float a, boolean guard) {
        SpotifyCard card = plan.card();
        Type t = plan.type();
        float pad = 10.0f * s;
        boolean playing = card.track().playing();
        float left = x + pad;
        if (card.art()) {
            drawAlbumArt(dl, card, left, y + pad, card.header(), s, a);
            left += card.header() + (10.0f * s);
        } else {
            releaseSpotifyArt();
        }
        float column = card.column();
        float top = y + card.textTop();
        HudChrome.text(dl, t.label(), t.labelPx(), left, Math.round(top), HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, a), playing ? "NOW PLAYING" : "PAUSED", guard);
        float top2 = top + t.labelPx() + (3.0f * s);
        marqueeLine(dl, t.text(), t.textPx(), left, Math.round(top2), column, plan.titleW(), plan.titleOffset(), plan.loopGap(), plan.scroll(), plan.title(), HudChrome.scaleAlpha(plan.colors().title(), a), guard);
        float top3 = top2 + t.textPx();
        if (!plan.artist().isEmpty()) {
            float top4 = top3 + (1.0f * s);
            marqueeLine(dl, t.label(), t.labelPx(), left, Math.round(top4), column, plan.artistW(), plan.artistOffset(), plan.loopGap(), plan.scroll(), plan.artist(), HudChrome.scaleAlpha(plan.colors().artist(), a), guard);
            top3 = top4 + t.labelPx();
        }
        float top5 = Math.round(top3 + (7.0f * s));
        drawTransport(dl, t, card, left, top5, column, s, a, guard);
        if (card.progressBar()) {
            float barTop = Math.round(top5 + t.labelPx() + (6.0f * s));
            drawProgress(dl, left, barTop, column, progressHeight(s), card.progress(), playing, a);
        }
        LyricView lyrics = card.lyrics();
        if (lyrics.height() > 0.5f) {
            float blockTop = Math.round(y + pad + card.header() + (12.0f * s));
            drawLyrics(dl, t, lyrics, plan.colors(), x + pad, blockTop, w - (pad * 2.0f), (y + h) - Math.max(1.0f, s), s, a * card.lyricAlpha(), guard);
        }
    }

    private void drawAlbumArt(ImDrawList dl, SpotifyCard card, float x, float y, float size, float s, float a) {
        float r = Math.min(hudRounding() * 0.5f, 4.0f) * s;
        if (card.sample()) {
            releaseSpotifyArt();
        }
        ThemeImages.Texture texture = card.sample() ? null : spotifyArtTexture(card.track().art());
        if (texture != null) {
            dl.addImageRounded(texture.id(), x, y, x + size, y + size, 0.0f, 0.0f, 1.0f, 1.0f, HudChrome.scaleAlpha(-1, a), r, 240);
        } else {
            dl.addRectFilled(x, y, x + size, y + size, HudChrome.scaleAlpha(ART_PLACEHOLDER, a), r);
            float bar = Math.max(2.0f, Math.round(size * 0.07f));
            float gap = Math.max(2.0f, Math.round(bar * 0.8f));
            float base = Math.round(y + (size * 0.62f));
            float l = Math.round(x + (((size - (bar * 3.0f)) - (gap * 2.0f)) * 0.5f));
            int color = HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, a * 0.45f);
            float[] heights = {0.2f, 0.34f, 0.26f};
            for (int i = 0; i < heights.length; i++) {
                float bx = l + (i * (bar + gap));
                dl.addRectFilled(bx, base - Math.round(size * heights[i]), bx + bar, base, color, 1.0f);
            }
        }
        dl.addRect(x, y, x + size, y + size, HudChrome.scaleAlpha(402653183, a), r, 0, 1.0f);
    }

    private void drawTransport(ImDrawList dl, Type t, SpotifyCard card, float left, float top, float column, float s, float a, boolean guard) {
        float elapsedW = 0.0f;
        float remainingW = 0.0f;
        if (card.timeline()) {
            elapsedW = HudChrome.tabularWidth(t.label(), t.labelPx(), card.elapsed());
            remainingW = HudChrome.tabularWidth(t.label(), t.labelPx(), card.remaining());
            int color = HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, a);
            HudChrome.tabular(dl, t.label(), t.labelPx(), left, top, color, card.elapsed(), guard);
            HudChrome.tabular(dl, t.label(), t.labelPx(), Math.round((left + column) - remainingW), top, color, card.remaining(), guard);
        }
        float skip = Math.max(4.0f, Math.round(8.0f * s));
        float play = Math.max(5.0f, Math.round(10.0f * s));
        float spacing = Math.round(14.0f * s);
        float cluster = (skip * 2.0f) + play + (spacing * 2.0f);
        float centre = left + (column * 0.5f);
        float margin = 8.0f * s;
        if (centre - (cluster * 0.5f) < left + elapsedW + margin || centre + (cluster * 0.5f) > ((left + column) - remainingW) - margin) {
            return;
        }
        float centerY = top + ((capTop(t.label(), t.labelPx()) + HudChrome.ascent(t.label(), t.labelPx())) * 0.5f);
        float playLeft = Math.round(centre - (play * 0.5f));
        float[] lefts = {(playLeft - spacing) - skip, playLeft, playLeft + play + spacing};
        float[] sizes = {skip, play, skip};
        float reach = Math.round(3.0f * s);
        float mouseX = ImGui.getMousePosX();
        float mouseY = ImGui.getMousePosY();
        int i = 0;
        while (i < 3) {
            float l = lefts[i] - reach;
            float r = lefts[i] + sizes[i] + reach;
            float tp = Math.round(centerY - (sizes[i] * 0.5f)) - reach;
            float b = Math.round(centerY + (sizes[i] * 0.5f)) + reach;
            this.spotifyButtons[i * 4] = l;
            this.spotifyButtons[(i * 4) + 1] = tp;
            this.spotifyButtons[(i * 4) + 2] = r;
            this.spotifyButtons[(i * 4) + 3] = b;
            boolean hovered = this.chatInput && mouseX >= l && mouseX <= r && mouseY >= tp && mouseY <= b;
            if (hovered) {
                dl.addRectFilled(l, tp, r, b, HudChrome.scaleAlpha(654311423, a), (r - l) * 0.5f);
            }
            int color2 = HudChrome.scaleAlpha((hovered || i == 1) ? HudChrome.TEXT_NAME : HudChrome.TEXT_LABEL, a);
            if (i == 1) {
                drawPlayPauseGlyph(dl, lefts[i], centerY, play, card.track().playing(), color2);
            } else {
                drawSkipGlyph(dl, lefts[i], centerY, skip, i == 2, color2);
            }
            i++;
        }
        this.spotifyButtonsDrawn = true;
    }

    private int spotifyButtonAt(float x, float y) {
        for (int i = 0; i < SPOTIFY_BUTTONS.length; i++) {
            if (x >= this.spotifyButtons[i * 4] && x <= this.spotifyButtons[(i * 4) + 2] && y >= this.spotifyButtons[(i * 4) + 1] && y <= this.spotifyButtons[(i * 4) + 3]) {
                return i;
            }
        }
        return -1;
    }

    private static void drawSkipGlyph(ImDrawList dl, float left, float centerY, float size, boolean next, int color) {
        float top = Math.round(centerY - (size * 0.5f));
        float bottom = top + size;
        float mid = (top + bottom) * 0.5f;
        float bar = Math.max(1.0f, Math.round(size * 0.2f));
        if (next) {
            dl.addTriangleFilled(left, top, (left + size) - bar, mid, left, bottom, color);
            dl.addRectFilled((left + size) - bar, top, left + size, bottom, color);
        } else {
            dl.addRectFilled(left, top, left + bar, bottom, color);
            dl.addTriangleFilled(left + size, top, left + size, bottom, left + bar, mid, color);
        }
    }

    private static void drawPlayPauseGlyph(ImDrawList dl, float left, float centerY, float size, boolean playing, int color) {
        float top = Math.round(centerY - (size * 0.5f));
        float bottom = top + size;
        if (playing) {
            float bar = Math.max(2.0f, Math.round(size * LYRIC_ARRIVE_TIME));
            float gap = Math.max(2.0f, Math.round(size * 0.24f));
            float l = Math.round(left + (((size - (bar * 2.0f)) - gap) * 0.5f));
            dl.addRectFilled(l, top, l + bar, bottom, color, 1.0f);
            dl.addRectFilled(l + bar + gap, top, l + (bar * 2.0f) + gap, bottom, color, 1.0f);
            return;
        }
        float inset = Math.round(size * 0.08f);
        dl.addTriangleFilled(left + inset, top, left + inset + (size * 0.86f), (top + bottom) * 0.5f, left + inset, bottom, color);
    }

    private void drawProgress(ImDrawList dl, float left, float top, float width, float height, float progress, boolean playing, float a) {
        float r = height * 0.5f;
        dl.addRectFilled(left, top, left + width, top + height, HudChrome.scaleAlpha(452984831, a), r);
        float fill = Math.round(width * clamp01(progress));
        if (fill < 1.0f) {
            return;
        }
        dl.addRectFilled(left, top, left + fill, top + height, HudChrome.scaleAlpha(playing ? hudAccent() : HudChrome.TEXT_LABEL, a), r);
    }

    private static float progressHeight(float s) {
        return Math.max(1.0f, Math.round(2.0f * s));
    }

    private LyricView planLyrics(Lyrics lyrics, boolean wanted, boolean online, long position, int lines, SpotifyHudModule.LyricMotion motion, SpotifyHudModule.LyricHighlight highlight, Type t, float innerW, float s, float dt, long nowMs) {
        List<LyricItem> items = List.of();
        String status = null;
        float content = -1.0f;
        boolean live = false;
        if (!wanted) {
            this.lyricLoadingSince = 0L;
            content = 0.0f;
        } else {
            if (lyrics == null || lyrics.status() != Lyrics.Status.LOADING) {
                this.lyricLoadingSince = 0L;
            } else if (this.lyricLoadingSince == 0) {
                this.lyricLoadingSince = nowMs;
            }
            if (lyrics != null) {
                switch (lyrics.status()) {
                    case LOADING:
                        status = nowMs - this.lyricLoadingSince < LYRIC_QUIET_MS ? null : "Loading lyrics";
                        break;
                    case SYNCED:
                        prepareLyrics(lyrics, t.text());
                        if (this.lyricReadable) {
                            items = new ArrayList(16);
                            float viewH = lyricViewHeight(lyrics.synced(), lines, t, innerW, s);
                            syncedItems(items, lyrics.synced(), position, viewH, motion, highlight, t, innerW, s, dt);
                            content = viewH - (t.textPx() - HudChrome.ascent(t.text(), t.textPx()));
                            live = true;
                        } else {
                            status = "Lyrics in a script the HUD font lacks";
                        }
                        break;
                    case PLAIN:
                        status = "No synced lyrics";
                        break;
                    case INSTRUMENTAL:
                        status = "Instrumental";
                        break;
                    case NOT_FOUND:
                        status = online ? "No lyrics found" : "No saved lyrics";
                        break;
                    case UNAVAILABLE:
                        status = "Lyrics unavailable";
                        break;
                }
            }
        }
        if (status != null) {
            content = Math.round((t.textPx() - t.labelPx()) * 0.5f) + HudChrome.ascent(t.label(), t.labelPx());
        }
        if (live) {
            this.lyricLastItems = items;
            this.lyricLeaveItems = null;
        } else {
            items = leavingItems(status == null, t, s, dt);
        }
        float target = content < 0.0f ? this.lyricBlockH : content > 0.0f ? (12.0f * s) + Math.round(content) : 0.0f;
        return new LyricView(items, status, easeLyricBlock(target, dt));
    }

    private List<LyricItem> leavingItems(boolean quiet, Type t, float s, float dt) {
        this.lyricSource = null;
        if (!this.lyricLastItems.isEmpty()) {
            this.lyricLeaveItems = this.lyricLastItems;
            this.lyricLeave = 0.0f;
            this.lyricLastItems = List.of();
        }
        if (this.lyricLeaveItems == null) {
            return List.of();
        }
        this.lyricLeave = Math.min(1.0f, this.lyricLeave + (dt / LYRIC_FADE_TIME));
        if (!quiet || this.lyricLeave >= 1.0f) {
            this.lyricLeaveItems = null;
            return List.of();
        }
        List<LyricItem> items = new ArrayList<>(this.lyricLeaveItems.size());
        addFaded(items, this.lyricLeaveItems, smooth(this.lyricLeave), 8.0f * s);
        return items;
    }

    private static void addFaded(List<LyricItem> items, List<LyricItem> frozen, float fade, float rise) {
        for (LyricItem item : frozen) {
            float alpha = item.alpha() * (1.0f - fade);
            if (alpha > 0.004f) {
                items.add(new LyricItem(item.text(), item.top() - (rise * fade), item.highlight(), alpha, item.progress(), item.fill()));
            }
        }
    }

    private void syncedItems(List<LyricItem> items, SyncedLyrics synced, long position, float viewH, SpotifyHudModule.LyricMotion motion, SpotifyHudModule.LyricHighlight highlight, Type t, float innerW, float s, float dt) {
        float lineGap = 6.0f * s;
        int currentLast = synced.indexAt(position + LYRIC_LEAD_MS);
        int currentFirst = synced.firstWithTimeOf(currentLast);
        int start = Math.max(0, currentFirst);
        if (synced != this.lyricSource) {
            this.lyricSource = synced;
            this.lyricStart = start;
            this.lyricShifts.clear();
            this.lyricClock = 0.0f;
            this.lyricFadeItems = null;
            this.lyricFade = 1.0f;
            this.lyricShown = new float[synced.size() * 2];
            this.lyricBright = new float[synced.size()];
            this.lyricBrightLo = 0;
            this.lyricBrightHi = -1;
            for (int i = Math.max(0, currentFirst); i <= currentLast; i++) {
                this.lyricBright[i] = 1.0f;
            }
            if (currentLast >= 0) {
                this.lyricBrightLo = Math.max(0, currentFirst);
                this.lyricBrightHi = currentLast;
            }
        }
        this.lyricClock += dt;
        this.lyricBreath = (this.lyricBreath + dt) % LYRIC_BREATH_PERIOD;
        this.lyricCurrentFirst = currentFirst;
        this.lyricCurrentLast = currentLast;
        float stagger = motion == SpotifyHudModule.LyricMotion.WAVE ? LYRIC_STAGGER : 0.0f;
        if (start != this.lyricStart) {
            float distance = start > this.lyricStart ? restDistance(this.lyricStart, start, t, innerW, s) : -1.0f;
            if (motion != SpotifyHudModule.LyricMotion.FADE && distance > 0.0f && distance <= viewH + lineGap + 0.5f) {
                if (this.lyricShifts.size() == 8) {
                    this.lyricShifts.remove(0);
                }
                this.lyricShifts.add(new LyricShift(distance, this.lyricClock, start, stagger));
            } else {
                this.lyricFadeItems = this.lyricLastItems;
                this.lyricFade = 0.0f;
                this.lyricShifts.clear();
                showView(synced, start, viewH, t, innerW, s);
            }
            this.lyricStart = start;
        }
        updateBrightness(currentFirst, currentLast, dt);
        this.lyricFade = Math.min(1.0f, this.lyricFade + (dt / LYRIC_FADE_TIME));
        if (this.lyricFade >= 1.0f) {
            this.lyricFadeItems = null;
        }
        float settled = 1.37f;
        this.lyricShifts.removeIf(shift -> {
            return this.lyricClock - shift.at() > settled;
        });
        if (this.lyricShifts.isEmpty()) {
            this.lyricClock = 0.0f;
        }
        float rise = 8.0f * s;
        if (this.lyricFadeItems != null) {
            float fade = smooth(this.lyricFade);
            addFaded(items, this.lyricFadeItems, fade, rise);
            addLyricView(items, synced, this.lyricStart, fade, rise * (1.0f - fade), viewH, position, highlight, t, innerW, s, dt);
            return;
        }
        addLyricView(items, synced, this.lyricStart, 1.0f, 0.0f, viewH, position, highlight, t, innerW, s, dt);
    }

    private void addLyricView(List<LyricItem> items, SyncedLyrics synced, int from, float alpha, float rise, float viewH, long position, SpotifyHudModule.LyricHighlight mode, Type t, float innerW, float s, float dt) {
        ImFont font = t.text();
        int px = t.textPx();
        float rowGap = 6.0f * s;
        float lineGap = 6.0f * s;
        float slot = px + lineGap;
        boolean karaoke = mode == SpotifyHudModule.LyricHighlight.KARAOKE;
        float restTop = 0.0f;
        for (int i = from; i < synced.size() && restTop <= viewH + (slot * 2.0f); i++) {
            String[] rows = rowsFor(i, font, px, innerW);
            int count = Math.max(1, rows.length);
            float lineTop = restTop + lyricOffset(i) + rise;
            float depth = 1.0f - (0.39999998f * clamp01((lineTop / slot) - 1.0f));
            float highlight = lyricGlow(i, mode);
            float progress = rows.length == 0 ? breakProgress(synced, i, position) : 1.0f;
            for (int r = 0; r < count; r++) {
                float rowRest = restTop + (r * (px + rowGap));
                float shown = updateShown(i, r, rowRest + ((float) px) <= viewH + 0.5f, dt);
                float top = lineTop + (r * (px + rowGap));
                float rowAlpha = alpha * depth * smooth(shown) * topFade(top, s) * bottomFade((top + px) - viewH, px);
                if (rowAlpha > 0.004f) {
                    float fill = (!karaoke || rows.length <= 0) ? 0.0f : rowFill(synced, i, rows, r, font, px, position);
                    items.add(new LyricItem(rows.length == 0 ? null : rows[r], top, highlight, rowAlpha, progress, fill));
                }
            }
            restTop += lineHeight(rows, px, rowGap) + lineGap;
        }
        float lineRest = 0.0f;
        for (int i2 = from - 1; i2 >= 0; i2--) {
            String[] rows2 = rowsFor(i2, font, px, innerW);
            int count2 = Math.max(1, rows2.length);
            lineRest -= lineHeight(rows2, px, rowGap) + lineGap;
            float lineTop2 = lineRest + lyricOffset(i2) + rise;
            if (topFade(lineTop2 + ((count2 - 1) * (px + rowGap)), s) > 0.0f) {
                float highlight2 = lyricGlow(i2, mode);
                float progress2 = rows2.length == 0 ? breakProgress(synced, i2, position) : 1.0f;
                for (int r2 = 0; r2 < count2; r2++) {
                    float top2 = lineTop2 + (r2 * (px + rowGap));
                    float rowAlpha2 = alpha * smooth(this.lyricShown[(i2 * 2) + r2]) * topFade(top2, s);
                    if (rowAlpha2 > 0.004f) {
                        float fill2 = (!karaoke || rows2.length <= 0) ? 0.0f : rowFill(synced, i2, rows2, r2, font, px, position);
                        items.add(new LyricItem(rows2.length == 0 ? null : rows2[r2], top2, highlight2, rowAlpha2, progress2, fill2));
                    }
                }
            } else {
                return;
            }
        }
    }

    private float lyricOffset(int index) {
        float offset = 0.0f;
        for (int k = 0; k < this.lyricShifts.size(); k++) {
            LyricShift shift = this.lyricShifts.get(k);
            float delay = Math.max(0, Math.min(6, index - shift.start())) * shift.stagger();
            float time = (this.lyricClock - shift.at()) - delay;
            offset += time <= 0.0f ? shift.distance() : shift.distance() * springLeft(time);
        }
        return offset;
    }

    private static float springLeft(float time) {
        float wt = 13.0f * time;
        return (1.0f + wt) * ((float) Math.exp(-wt));
    }

    private static float topFade(float top, float s) {
        if (top >= 0.0f) {
            return 1.0f;
        }
        return smooth(1.0f + (top / ((12.0f * s) * LYRIC_MIN_COVERAGE)));
    }

    private static float bottomFade(float below, int px) {
        if (below <= 0.5f) {
            return 1.0f;
        }
        return smooth(1.0f - (below / (px * LYRIC_FAR_ALPHA)));
    }

    private float updateShown(int index, int row, boolean fits, float dt) {
        int k = (index * 2) + row;
        float step = dt / LYRIC_ARRIVE_TIME;
        float shown = fits ? Math.min(1.0f, this.lyricShown[k] + step) : Math.max(0.0f, this.lyricShown[k] - step);
        this.lyricShown[k] = shown;
        return shown;
    }

    private void showView(SyncedLyrics synced, int from, float viewH, Type t, float innerW, float s) {
        Arrays.fill(this.lyricShown, 0.0f);
        int px = t.textPx();
        float rowGap = 6.0f * s;
        float restTop = 0.0f;
        for (int i = from; i < synced.size() && restTop < viewH; i++) {
            String[] rows = rowsFor(i, t.text(), px, innerW);
            int count = Math.max(1, rows.length);
            for (int r = 0; r < count; r++) {
                if (restTop + (r * (px + rowGap)) + px <= viewH + 0.5f) {
                    this.lyricShown[(i * 2) + r] = 1.0f;
                }
            }
            restTop += lineHeight(rows, px, rowGap) + (6.0f * s);
        }
    }

    private float restDistance(int from, int to, Type t, float innerW, float s) {
        float rowGap = 6.0f * s;
        float distance = 0.0f;
        for (int i = from; i < to; i++) {
            distance += lineHeight(rowsFor(i, t.text(), t.textPx(), innerW), t.textPx(), rowGap) + (6.0f * s);
        }
        return distance;
    }

    private void updateBrightness(int currentFirst, int currentLast, float dt) {
        int lo = this.lyricBrightHi >= 0 ? this.lyricBrightLo : Integer.MAX_VALUE;
        int hi = this.lyricBrightHi;
        if (currentLast >= 0) {
            lo = Math.min(lo, Math.max(0, currentFirst));
            hi = Math.max(hi, currentLast);
        }
        float step = dt / 0.35f;
        int lit = Integer.MAX_VALUE;
        int last = -1;
        int i = Math.max(0, lo);
        while (i <= hi && i < this.lyricBright.length) {
            boolean sung = i >= currentFirst && i <= currentLast;
            float bright = sung ? Math.min(1.0f, this.lyricBright[i] + step) : Math.max(0.0f, this.lyricBright[i] - step);
            this.lyricBright[i] = bright;
            if (bright > 0.0f) {
                lit = Math.min(lit, i);
                last = i;
            }
            i++;
        }
        this.lyricBrightLo = last < 0 ? 0 : lit;
        this.lyricBrightHi = last;
    }

    private float lyricGlow(int index, SpotifyHudModule.LyricHighlight mode) {
        if (mode != SpotifyHudModule.LyricHighlight.OFF && index >= 0 && index < this.lyricCurrentFirst) {
            return 1.0f;
        }
        if (index < 0 || index >= this.lyricBright.length) {
            return 0.0f;
        }
        return smooth(this.lyricBright[index]);
    }

    private float breakProgress(SyncedLyrics synced, int index, long position) {
        int next;
        if (index < this.lyricCurrentFirst || index > this.lyricCurrentLast || (next = synced.lastWithTimeOf(index) + 1) >= synced.size()) {
            return 1.0f;
        }
        long begin = synced.line(index).timeMs();
        long end = synced.line(next).timeMs() - LYRIC_LEAD_MS;
        if (end <= begin) {
            return 1.0f;
        }
        return clamp01((position - begin) / (end - begin));
    }

    private float rowFill(SyncedLyrics synced, int index, String[] rows, int row, ImFont font, int px, long position) {
        if (index < this.lyricCurrentFirst) {
            return LYRIC_LIT_ALL;
        }
        if (index > this.lyricCurrentLast) {
            return 0.0f;
        }
        String text = rows[row];
        int rowStart = 0;
        if (row > 0) {
            String line = this.lyricTexts[index];
            rowStart = rows[0].length();
            while (rowStart < line.length() && line.charAt(rowStart) == ' ') {
                rowStart++;
            }
        }
        float lit = litChars(synced, index, position) - rowStart;
        if (lit <= 0.0f) {
            return 0.0f;
        }
        if (lit >= text.length()) {
            return LYRIC_LIT_ALL;
        }
        int whole = (int) lit;
        if (whole > 0 && Character.isLowSurrogate(text.charAt(whole))) {
            whole--;
        }
        int next = whole + Character.charCount(text.codePointAt(whole));
        float x0 = HudChrome.width(font, px, text.substring(0, whole));
        float x1 = HudChrome.width(font, px, text.substring(0, next));
        return x0 + ((x1 - x0) * clamp01((lit - whole) / (next - whole)));
    }

    private float litChars(SyncedLyrics synced, int index, long position) {
        LyricWords words = this.lyricWords[index];
        if (words == null) {
            timeWords(synced, synced.firstWithTimeOf(index));
            words = this.lyricWords[index];
        }
        float lit = 0.0f;
        long[] start = words.start();
        for (int k = 0; k < start.length && position >= start[k]; k++) {
            float swept = smooth((position - start[k]) / words.sweep()[k]);
            lit = words.from()[k] + ((words.to()[k] - words.from()[k]) * swept);
        }
        return lit;
    }

    private void timeWords(SyncedLyrics synced, int first) {
        long window;
        long jLongValue;
        int last = synced.lastWithTimeOf(first);
        int next = last + 1;
        long begin = synced.line(first).timeMs();
        long handover = next < synced.size() ? synced.line(next).timeMs() - LYRIC_LEAD_MS : Long.MAX_VALUE;
        int letters = 0;
        for (int i = first; i <= last; i++) {
            letters += letterCount(this.lyricTexts[i]);
        }
        if (next < synced.size()) {
            window = Math.min(handover - begin, Math.round(letters * this.lyricCharMs * 2.0f));
        } else {
            window = Math.round(letters * this.lyricCharMs);
        }
        long window2 = Math.max(0L, window);
        int done = 0;
        for (int i2 = first; i2 <= last; i2++) {
            String text = this.lyricTexts[i2];
            int count = text.isEmpty() ? 0 : (text.length() - letterCount(text)) + 1;
            int[] from = new int[count];
            int[] to = new int[count];
            long[] start = new long[count];
            long[] sweep = new long[count];
            List<Long> timed = synced.line(i2).wordTimes();
            boolean own = timed.size() == count;
            int c = 0;
            for (int k = 0; k < count; k++) {
                from[k] = c;
                int c2 = text.indexOf(32, c);
                if (c2 < 0) {
                    c2 = text.length();
                }
                int i3 = c2;
                c = c2 + 1;
                to[k] = i3;
                long at = own ? timed.get(k).longValue() : begin + ((window2 * ((long) done)) / ((long) letters));
                done += to[k] - from[k];
                if (own) {
                    jLongValue = k + 1 < count ? timed.get(k + 1).longValue() : Math.min(handover, at + LYRIC_SWEEP_MS);
                } else {
                    jLongValue = begin + ((window2 * ((long) done)) / ((long) letters));
                }
                long until = jLongValue;
                start[k] = at;
                sweep[k] = Math.max(1L, Math.min(LYRIC_SWEEP_MS, until - at));
            }
            this.lyricWords[i2] = new LyricWords(from, to, start, sweep);
        }
    }

    private float lyricPace(SyncedLyrics synced) {
        float[] paces = new float[synced.size()];
        int n = 0;
        int i = 0;
        while (true) {
            int first = i;
            if (first >= synced.size()) {
                break;
            }
            int last = synced.lastWithTimeOf(first);
            int letters = 0;
            for (int i2 = first; i2 <= last; i2++) {
                letters += letterCount(this.lyricTexts[i2]);
            }
            if (letters > 0 && last + 1 < synced.size()) {
                int i3 = n;
                n++;
                paces[i3] = (synced.line(last + 1).timeMs() - synced.line(first).timeMs()) / letters;
            }
            i = last + 1;
        }
        if (n == 0) {
            return LYRIC_CHAR_MS;
        }
        Arrays.sort(paces, 0, n);
        return paces[n / 2];
    }

    private static int letterCount(String text) {
        int letters = text.length();
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {
                letters--;
            }
        }
        return letters;
    }

    private float lyricViewHeight(SyncedLyrics synced, int lines, Type t, float innerW, float s) {
        int px = t.textPx();
        prepareRows(px, innerW);
        if (this.lyricViewH >= 0.0f && this.lyricViewLines == lines && this.lyricViewScale == s) {
            return this.lyricViewH;
        }
        int slots = lines;
        int iLastWithTimeOf = 0;
        while (true) {
            int start = iLastWithTimeOf;
            if (start < synced.size()) {
                int rows = 0;
                int last = synced.lastWithTimeOf(start);
                for (int i = start; i <= last; i++) {
                    rows += Math.max(1, rowsFor(i, t.text(), px, innerW).length);
                }
                slots = Math.max(slots, Math.min(4, rows));
                iLastWithTimeOf = synced.lastWithTimeOf(start) + 1;
            } else {
                this.lyricViewH = (slots * px) + ((slots - 1) * 6.0f * s);
                this.lyricViewLines = lines;
                this.lyricViewScale = s;
                return this.lyricViewH;
            }
        }
    }

    private static float lineHeight(String[] rows, int px, float rowGap) {
        return rows.length <= 1 ? px : (rows.length * px) + ((rows.length - 1) * rowGap);
    }

    private float easeLyricBlock(float target, float dt) {
        float diff = target - this.lyricBlockH;
        if (Math.abs(diff) < 0.5f) {
            this.lyricBlockH = target;
        } else {
            float speed = diff > 0.0f ? 14.0f : LYRIC_SHRINK_SPEED;
            this.lyricBlockH += diff * (1.0f - ((float) Math.exp((-speed) * dt)));
        }
        return this.lyricBlockH;
    }

    private void prepareLyrics(Lyrics lyrics, ImFont font) {
        if (lyrics == this.lyricPrepared) {
            return;
        }
        this.lyricPrepared = lyrics;
        this.lyricViewH = -1.0f;
        SyncedLyrics synced = lyrics.synced();
        this.lyricTexts = new String[synced.size()];
        this.lyricRows = new String[synced.size()][];
        this.lyricWords = new LyricWords[synced.size()];
        int letters = 0;
        int missing = 0;
        IntPredicate drawable = cp -> {
            return hudHasGlyph(font, cp);
        };
        for (int i = 0; i < synced.size(); i++) {
            String text = DisplayText.fold(synced.line(i).text(), drawable);
            this.lyricTexts[i] = text;
            int k = 0;
            while (k < text.length()) {
                int cp2 = text.codePointAt(k);
                k += Character.charCount(cp2);
                if (Character.isLetter(cp2)) {
                    letters++;
                    if (cp2 >= 128 && !hudHasGlyph(font, cp2)) {
                        missing++;
                    }
                }
            }
        }
        this.lyricReadable = ((float) missing) <= ((float) letters) * 0.25f;
        this.lyricCharMs = lyricPace(synced);
    }

    private boolean hudHasGlyph(ImFont font, int cp) {
        if (cp < 128 || font == null) {
            return true;
        }
        if (cp > 65535) {
            return false;
        }
        if (font != this.glyphFont) {
            this.glyphFont = font;
            this.glyphs.clear();
        }
        Boolean known = this.glyphs.get(Integer.valueOf(cp));
        if (known == null) {
            if (this.glyphs.size() > 1024) {
                this.glyphs.clear();
            }
            known = Boolean.valueOf(font.findGlyphNoFallback(cp).isValidPtr());
            this.glyphs.put(Integer.valueOf(cp), known);
        }
        return known.booleanValue();
    }

    private void prepareRows(int px, float maxW) {
        int widthKey = (int) maxW;
        if (px == this.lyricRowsPx && widthKey == this.lyricRowsWidth) {
            return;
        }
        Arrays.fill(this.lyricRows, (Object) null);
        this.lyricRowsPx = px;
        this.lyricRowsWidth = widthKey;
        this.lyricViewH = -1.0f;
    }

    private String[] rowsFor(int index, ImFont font, int px, float maxW) {
        prepareRows(px, maxW);
        String[] rows = this.lyricRows[index];
        if (rows == null) {
            rows = wrapLyric(font, px, this.lyricTexts[index], this.lyricRowsWidth);
            this.lyricRows[index] = rows;
        }
        return rows;
    }

    private static String[] wrapLyric(ImFont font, int px, String text, float maxW) {
        if (text.isEmpty()) {
            return NO_ROWS;
        }
        if (HudChrome.width(font, px, text) <= maxW) {
            return new String[]{text};
        }
        int best = -1;
        float bestW = Float.MAX_VALUE;
        int iIndexOf = text.indexOf(32);
        while (true) {
            int i = iIndexOf;
            if (i <= 0) {
                break;
            }
            float firstW = HudChrome.width(font, px, text.substring(0, i).stripTrailing());
            if (firstW > maxW) {
                break;
            }
            float secondW = HudChrome.width(font, px, text.substring(i + 1).stripLeading());
            if (secondW <= maxW && Math.max(firstW, secondW) < bestW) {
                bestW = Math.max(firstW, secondW);
                best = i;
            }
            iIndexOf = text.indexOf(32, i + 1);
        }
        if (best > 0) {
            return new String[]{text.substring(0, best).stripTrailing(), text.substring(best + 1).stripLeading()};
        }
        int cut = fitPrefix(font, px, text, maxW);
        String first = text.substring(0, cut).stripTrailing();
        String rest = text.substring(cut).stripLeading();
        if (rest.isEmpty()) {
            return new String[]{first};
        }
        String[] strArr = new String[2];
        strArr[0] = first;
        strArr[1] = HudChrome.width(font, px, rest) <= maxW ? rest : ellipsize(font, px, rest, maxW);
        return strArr;
    }

    private static int fitPrefix(ImFont font, int px, String text, float maxW) {
        int lastBreak = -1;
        int iIndexOf = text.indexOf(32);
        while (true) {
            int i = iIndexOf;
            if (i <= 0 || HudChrome.width(font, px, text.substring(0, i)) > maxW) {
                break;
            }
            lastBreak = i;
            iIndexOf = text.indexOf(32, i + 1);
        }
        if (lastBreak > 0) {
            return lastBreak + 1;
        }
        int lo = 1;
        int hi = text.length();
        while (lo < hi) {
            int mid = ((lo + hi) + 1) >>> 1;
            if (HudChrome.width(font, px, text.substring(0, mid)) <= maxW) {
                lo = mid;
            } else {
                hi = mid - 1;
            }
        }
        if (lo > 1 && lo < text.length() && Character.isHighSurrogate(text.charAt(lo - 1))) {
            lo--;
        }
        return lo;
    }

    private void drawLyrics(ImDrawList dl, Type t, LyricView view, SpotifyColors colors, float left, float top, float width, float bottom, float s, float a, boolean guard) {
        float clipTop = (top - (12.0f * s)) + Math.max(1.0f, 2.0f * s);
        if (bottom <= clipTop || a <= 0.001f) {
            return;
        }
        float clipLeft = (float) Math.floor(left - 2.0f);
        float clipRight = (float) Math.ceil(left + width + 2.0f);
        dl.pushClipRect(clipLeft, clipTop, clipRight, bottom, true);
        if (view.status() != null) {
            float statusTop = Math.round(top + ((t.textPx() - t.labelPx()) * 0.5f));
            HudChrome.text(dl, t.label(), t.labelPx(), left, statusTop, HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, a * 0.85f), view.status(), guard);
        }
        for (LyricItem item : view.items()) {
            float alpha = a * item.alpha();
            if (alpha > 0.004f) {
                float rowTop = top + item.top();
                if (item.text() == null) {
                    float centerY = rowTop + ((capTop(t.text(), t.textPx()) + HudChrome.ascent(t.text(), t.textPx())) * 0.5f);
                    drawLyricBreak(dl, colors, left, centerY, s, item.highlight(), item.progress(), alpha);
                } else {
                    int color = HudChrome.scaleAlpha(AnubisTheme.lerpColor(colors.upcoming(), colors.lyric(), item.highlight()), alpha);
                    int lit = HudChrome.scaleAlpha(colors.lit(), alpha);
                    float edge = Math.min(clipRight, ((float) Math.floor(left)) + Math.round(item.fill()));
                    if (item.fill() <= 0.0f) {
                        HudChrome.text(dl, t.text(), t.textPx(), left, Math.round(rowTop), color, item.text(), guard);
                    } else if (edge >= clipRight) {
                        HudChrome.text(dl, t.text(), t.textPx(), left, Math.round(rowTop), lit, item.text(), guard);
                    } else {
                        dl.pushClipRect(clipLeft, clipTop, edge, bottom, true);
                        HudChrome.text(dl, t.text(), t.textPx(), left, Math.round(rowTop), lit, item.text(), guard);
                        dl.popClipRect();
                        dl.pushClipRect(edge, clipTop, clipRight, bottom, true);
                        HudChrome.text(dl, t.text(), t.textPx(), left, Math.round(rowTop), color, item.text(), guard);
                        dl.popClipRect();
                    }
                }
            }
        }
        dl.popClipRect();
    }

    private void drawLyricBreak(ImDrawList dl, SpotifyColors colors, float left, float centerY, float s, float highlight, float progress, float alpha) {
        float r = Math.max(1.0f, LYRIC_BREATH_PERIOD * s);
        float step = Math.max(4.0f, Math.round(6.0f * s));
        float breath = 0.5f - (0.5f * ((float) Math.cos((((double) (this.lyricBreath / LYRIC_BREATH_PERIOD)) * 2.0d) * 3.141592653589793d)));
        for (int i = 0; i < 3; i++) {
            float lit = smooth((progress * 3.0f) - i) * highlight;
            int color = HudChrome.scaleAlpha(AnubisTheme.lerpColor(colors.upcoming(), colors.lit(), lit), alpha);
            float radius = r * (1.0f + (LYRIC_ARRIVE_TIME * lit) + (0.12f * breath * highlight));
            dl.addCircleFilled(left + r + (i * step), centerY, radius, color, 12);
        }
    }

    private void foldTrack(NowPlaying now, ImFont font) {
        if (now == this.spotifyFolded) {
            return;
        }
        this.spotifyFolded = now;
        IntPredicate drawable = cp -> {
            return hudHasGlyph(font, cp);
        };
        String title = DisplayText.fold(now.title(), drawable);
        this.spotifyTitle = title.isEmpty() ? now.title() : title;
        this.spotifyArtist = DisplayText.fold(now.artist(), drawable);
    }

    private float[] marquee(String title, String artist, float titleW, float artistW, float avail, boolean scroll, boolean advancing, float loopGap, float s, float dt) {
        if (!title.equals(this.marqueeTitle) || !artist.equals(this.marqueeArtist)) {
            this.marqueeTitle = title;
            this.marqueeArtist = artist;
            this.marqueeTime = 0.0f;
        }
        float titleOffset = 0.0f;
        float artistOffset = 0.0f;
        float longest = Math.max(titleW > avail ? titleW : 0.0f, artistW > avail ? artistW : 0.0f);
        if (scroll && longest > 0.0f) {
            float speed = MARQUEE_SPEED * s;
            float period = MARQUEE_HOLD + ((longest + loopGap) / speed);
            this.marqueeTime %= period;
            if (advancing || this.marqueeTime > MARQUEE_HOLD) {
                this.marqueeTime = (this.marqueeTime + dt) % period;
            }
            float travelled = Math.max(0.0f, this.marqueeTime - MARQUEE_HOLD) * speed;
            if (titleW > avail) {
                titleOffset = Math.min(travelled, titleW + loopGap);
            }
            if (artistW > avail) {
                artistOffset = Math.min(travelled, artistW + loopGap);
            }
        } else {
            this.marqueeTime = 0.0f;
        }
        return new float[]{titleOffset, artistOffset};
    }

    private static void marqueeLine(ImDrawList dl, ImFont font, int px, float x, float top, float avail, float textW, float offset, float loopGap, boolean scroll, String text, int color, boolean guard) {
        if (textW <= avail + 0.5f) {
            HudChrome.text(dl, font, px, x, top, color, text, guard);
            return;
        }
        if (!scroll) {
            HudChrome.text(dl, font, px, x, top, color, ellipsize(font, px, text, avail), guard);
            return;
        }
        dl.pushClipRect(x, top - 2.0f, x + avail, top + px + 3.0f, true);
        HudChrome.text(dl, font, px, x - offset, top, color, text, guard);
        if (offset > 0.0f) {
            HudChrome.text(dl, font, px, (x - offset) + textW + loopGap, top, color, text, guard);
        }
        dl.popClipRect();
    }

    private static String ellipsize(ImFont font, int px, String text, float maxW) {
        float room = maxW - HudChrome.width(font, px, ELLIPSIS);
        int lo = 0;
        int hi = text.length();
        while (lo < hi) {
            int mid = ((lo + hi) + 1) >>> 1;
            if (HudChrome.width(font, px, text.substring(0, mid)) <= room) {
                lo = mid;
            } else {
                hi = mid - 1;
            }
        }
        return text.substring(0, lo).stripTrailing() + "...";
    }

    private static String formatSeconds(long totalSeconds) {
        long seconds = Math.max(0L, totalSeconds);
        long hours = seconds / 3600;
        long minutes = (seconds / 60) % 60;
        StringBuilder out = new StringBuilder(8);
        if (hours > 0) {
            out.append(hours).append(':').append(minutes < 10 ? "0" : "");
        }
        out.append(minutes).append(':');
        if (seconds % 60 < 10) {
            out.append('0');
        }
        return out.append(seconds % 60).toString();
    }

    private float capTop(ImFont font, int px) {
        if (font == null || font.getFontSize() <= 0.0f) {
            return Math.round(px * 0.2f);
        }
        long key = (((long) System.identityHashCode(font)) << 16) ^ ((long) px);
        Float cached = this.capTops.get(Long.valueOf(key));
        if (cached != null) {
            return cached.floatValue();
        }
        ImFontGlyph glyph = font.findGlyphNoFallback(72);
        float top = glyph.isValidPtr() ? (glyph.getY0() * px) / font.getFontSize() : px * 0.2f;
        float value = Math.max(0.0f, Math.round(top));
        if (this.capTops.size() > 64) {
            this.capTops.clear();
        }
        this.capTops.put(Long.valueOf(key), Float.valueOf(value));
        return value;
    }

    private ThemeImages.Texture spotifyArtTexture(AlbumArt art) {
        if (art == this.spotifyArt) {
            return this.spotifyArtTexture;
        }
        releaseSpotifyArt();
        if (art == null) {
            return null;
        }
        this.spotifyArt = art;
        ByteBuffer pixels = MemoryUtil.memAlloc(art.byteSize());
        try {
            art.copyTo(pixels);
            pixels.flip();
            this.spotifyArtTexture = ThemeImages.fromPixels(pixels, art.size(), art.size(), false);
        } catch (RuntimeException e) {
            this.spotifyArtTexture = null;
        } finally {
            MemoryUtil.memFree(pixels);
        }
        return this.spotifyArtTexture;
    }

    private void releaseSpotifyArt() {
        if (this.spotifyArtTexture != null) {
            GL11.glDeleteTextures(this.spotifyArtTexture.id());
        }
        this.spotifyArtTexture = null;
        this.spotifyArt = null;
    }

    private List<PotionRow> buildPotionRows(class_310 client) {
        if (client.field_1724 == null) {
            return List.of();
        }
        List<PotionRow> rows = new ArrayList<>();
        for (class_1293 instance : client.field_1724.method_6026()) {
            class_6880<class_1291> effect = instance.method_5579();
            String name = class_1074.method_4662(((class_1291) effect.comp_349()).method_5567(), new Object[0]);
            int amp = instance.method_5578();
            String amplifier = amp > 0 ? toRomanNumeral(amp + 1) : "";
            int ticks = instance.method_5584();
            if (ticks < 0 || ticks == Integer.MAX_VALUE || instance.method_48559()) {
                rows.add(new PotionRow(effect, name, amplifier, "", true, false, Integer.MAX_VALUE));
            } else {
                int seconds = Math.max(0, ticks / 20);
                String formatted = (seconds / 60) + ":" + String.format("%02d", Integer.valueOf(seconds % 60));
                rows.add(new PotionRow(effect, name, amplifier, formatted, false, seconds < 30, ticks));
            }
        }
        return rows;
    }

    private static String toRomanNumeral(int n) {
        switch (n) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            case 7:
                return "VII";
            case 8:
                return "VIII";
            case 9:
                return "IX";
            case 10:
                return "X";
            default:
                return String.valueOf(n);
        }
    }

    private int hudAccent() {
        if (this.hudModule != null && this.hudModule.customAccent()) {
            return GuiPalette.abgr(this.hudModule.accentArgb());
        }
        return GuiPalette.accent();
    }

    static Surface hudSurface(HudLayoutStore.HudElementState state) {
        return HudTheme.get().forElementId(state.id);
    }

    static float hudRounding() {
        return HudTheme.get().rounding.get();
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$Snap.class */
    @Environment(EnvType.CLIENT)
    private static final class Snap extends Record {
        private final float position;
        private final float guideAt;

        private Snap(float position, float guideAt) {
            this.position = position;
            this.guideAt = guideAt;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Snap.class), Snap.class, "position;guideAt", "FIELD:Lnet/anubis/client/gui/HudRenderer$Snap;->position:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Snap;->guideAt:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Snap.class), Snap.class, "position;guideAt", "FIELD:Lnet/anubis/client/gui/HudRenderer$Snap;->position:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Snap;->guideAt:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Snap.class, Object.class), Snap.class, "position;guideAt", "FIELD:Lnet/anubis/client/gui/HudRenderer$Snap;->position:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$Snap;->guideAt:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public float position() {
            return this.position;
        }

        public float guideAt() {
            return this.guideAt;
        }
    }

    private void updateEditorInput(float screenW, float screenH) {
        int button;
        float mouseX = ImGui.getMousePosX();
        float mouseY = ImGui.getMousePosY();
        if (this.draggingId != null) {
            if (ImGui.isMouseDown(0)) {
                HudLayoutStore.HudElementState dragging = this.layout.element(this.draggingId);
                float rawX = mouseX - this.dragOffsetX;
                float rawY = mouseY - this.dragOffsetY;
                Snap xSnap = bestSnap(rawX, dragging.width, snapAnchorsX(this.draggingId, screenW));
                Snap ySnap = bestSnap(rawY, dragging.height, snapAnchorsY(this.draggingId, screenH));
                this.snapGuideX = xSnap != null ? Float.valueOf(xSnap.guideAt()) : null;
                this.snapGuideY = ySnap != null ? Float.valueOf(ySnap.guideAt()) : null;
                this.layout.moveTo(this.draggingId, xSnap != null ? xSnap.position() : rawX, ySnap != null ? ySnap.position() : rawY, screenW, screenH);
                return;
            }
            this.draggingId = null;
            this.snapGuideX = null;
            this.snapGuideY = null;
            saveLayout();
            return;
        }
        if (this.chatInput && this.spotifyButtonsShown && ImGui.isMouseClicked(0) && (button = spotifyButtonAt(mouseX, mouseY)) >= 0) {
            SpotifyHudModule.hudButton(SPOTIFY_BUTTONS[button]);
            return;
        }
        if (ImGui.isAnyItemHovered()) {
            return;
        }
        float wheel = ImGui.getIO().getMouseWheel();
        if (wheel != 0.0f) {
            List<HudLayoutStore.HudElementState> states = this.layout.orderedElements();
            for (int i = states.size() - 1; i >= 0; i--) {
                HudLayoutStore.HudElementState s = states.get(i);
                if (contains(s, mouseX, mouseY)) {
                    float next = Math.max(0.5f, Math.min(3.0f, s.scale + (wheel * 0.05f)));
                    if (next == s.scale) {
                        break;
                    }
                    s.scale = next;
                    this.layoutDirty = true;
                    this.lastWheelNanos = System.nanoTime();
                    break;
                }
            }
        }
        if (ImGui.isMouseClicked(0)) {
            List<HudLayoutStore.HudElementState> states2 = this.layout.orderedElements();
            for (int i2 = states2.size() - 1; i2 >= 0; i2--) {
                HudLayoutStore.HudElementState s2 = states2.get(i2);
                if (contains(s2, mouseX, mouseY)) {
                    this.draggingId = s2.id;
                    this.dragOffsetX = mouseX - s2.x;
                    this.dragOffsetY = mouseY - s2.y;
                    return;
                }
            }
        }
    }

    private List<Float> snapAnchorsX(String excludeId, float screenW) {
        List<Float> anchors = new ArrayList<>(List.of(Float.valueOf(0.0f), Float.valueOf(screenW), Float.valueOf(screenW * 0.5f)));
        for (HudLayoutStore.HudElementState s : this.layout.orderedElements()) {
            if (!s.id.equals(excludeId) && s.positioned) {
                anchors.add(Float.valueOf(s.x));
                anchors.add(Float.valueOf(s.x + s.width));
                anchors.add(Float.valueOf(s.x + (s.width * 0.5f)));
            }
        }
        return anchors;
    }

    private List<Float> snapAnchorsY(String excludeId, float screenH) {
        List<Float> anchors = new ArrayList<>(List.of(Float.valueOf(0.0f), Float.valueOf(screenH), Float.valueOf(screenH * 0.5f)));
        for (HudLayoutStore.HudElementState s : this.layout.orderedElements()) {
            if (!s.id.equals(excludeId) && s.positioned) {
                anchors.add(Float.valueOf(s.y));
                anchors.add(Float.valueOf(s.y + s.height));
                anchors.add(Float.valueOf(s.y + (s.height * 0.5f)));
            }
        }
        return anchors;
    }

    private static Snap bestSnap(float raw, float size, List<Float> anchors) {
        float[] offsets = {0.0f, size * 0.5f, size};
        float bestDelta = 6.0f;
        Snap best = null;
        Iterator<Float> it = anchors.iterator();
        while (it.hasNext()) {
            float anchor = it.next().floatValue();
            for (float offset : offsets) {
                float delta = Math.abs((raw + offset) - anchor);
                if (delta < bestDelta) {
                    bestDelta = delta;
                    best = new Snap(anchor - offset, anchor);
                }
            }
        }
        return best;
    }

    private void drawSnapGuides(ImDrawList dl, float screenW, float screenH) {
        if (this.draggingId == null) {
            return;
        }
        int guideColor = HudChrome.scaleAlpha(hudAccent(), 0.8f);
        if (this.snapGuideX != null) {
            dl.addLine(this.snapGuideX.floatValue(), 0.0f, this.snapGuideX.floatValue(), screenH, guideColor, 1.0f);
        }
        if (this.snapGuideY != null) {
            dl.addLine(0.0f, this.snapGuideY.floatValue(), screenW, this.snapGuideY.floatValue(), guideColor, 1.0f);
        }
    }

    private void drawEditorChrome(ImDrawList dl, float screenW, float screenH) {
        float mouseX = ImGui.getMousePosX();
        float mouseY = ImGui.getMousePosY();
        float u = ImGuiManager.getInstance().uiScale();
        ImFont font = ImGuiManager.getInstance().hudLabelFont();
        int size = HudChrome.px(14.0f, u);
        drawSnapGuides(dl, screenW, screenH);
        float e = Math.round(Math.max(2.0f, 3.0f * u));
        List<HudLayoutStore.HudElementState> states = this.layout.orderedElements();
        List<float[]> placed = new ArrayList<>(states.size());
        for (HudLayoutStore.HudElementState state : states) {
            boolean active = state.id.equals(this.draggingId);
            boolean hovered = contains(state, mouseX, mouseY);
            float emphasis = active ? 1.0f : hovered ? 0.85f : 0.4f;
            float ox = Math.round(state.x);
            float oy = Math.round(state.y);
            dl.addRect(ox - e, oy - e, ox + state.width + e, oy + state.height + e, HudChrome.scaleAlpha(hudAccent(), emphasis), (hudRounding() * scaleOf(state)) + e, 0, active ? RADAR_MARKER_INSET : 1.0f);
            drawEditorLabel(dl, font, size, u, state, active || hovered, states, placed, screenW, screenH);
        }
        drawEditorHint(dl, font, size, u, screenW, screenH);
    }

    private static void drawEditorHint(ImDrawList dl, ImFont font, int size, float u, float screenW, float screenH) {
        float w = Math.round(HudChrome.width(font, size, "Drag to move  ·  Scroll to resize  ·  Click the eye to hide") + (12.0f * u * 2.0f));
        float h = Math.round(CAP_H * u);
        float x = Math.round((screenW - w) * 0.5f);
        float y = Math.round((screenH - h) - (96.0f * u));
        float r = hudRounding() * u;
        dl.addRectFilled(x, y, x + w, y + h, HudChrome.CHIP_FILL, r);
        dl.addRect(x, y, x + w, y + h, 402653183, r, 0, 1.0f);
        HudChrome.text(dl, font, size, x + (12.0f * u), y + ((float) Math.floor((h - size) * 0.5f)), HudChrome.TEXT_LABEL, "Drag to move  ·  Scroll to resize  ·  Click the eye to hide", false);
    }

    private void drawEditorLabel(ImDrawList dl, ImFont font, int size, float u, HudLayoutStore.HudElementState state, boolean highlighted, List<HudLayoutStore.HudElementState> states, List<float[]> placed, float screenW, float screenH) {
        int iScaleAlpha;
        String label = editorLabel(state.id);
        float a = highlighted ? 1.0f : LYRIC_FAR_ALPHA;
        float padX = 8.0f * u;
        float eyeSize = 12.0f * u;
        float chipH = Math.round(20.0f * u);
        float textW = HudChrome.width(font, size, label);
        float chipW = Math.round(padX + textW + (8.0f * u) + eyeSize + padX);
        float[] chip = placeChip(state, chipW, chipH, 7.0f * u, states, placed, screenW, screenH);
        placed.add(chip);
        float chipX = chip[0];
        float chipY = chip[1];
        float r = 6.0f * u;
        dl.addRectFilled(chipX, chipY, chipX + chipW, chipY + chipH, HudChrome.scaleAlpha(HudChrome.CHIP_FILL, a), r);
        dl.addRect(chipX, chipY, chipX + chipW, chipY + chipH, HudChrome.scaleAlpha(402653183, a), r, 0, 1.0f);
        int textColor = HudChrome.scaleAlpha(HudChrome.TEXT_VALUE, a);
        HudChrome.text(dl, font, size, chipX + padX, chipY + ((float) Math.floor((chipH - size) * 0.5f)), textColor, label, false);
        float eyeX = chipX + padX + textW + (8.0f * u);
        float eyeY = chipY + ((chipH - eyeSize) * 0.5f);
        ImGui.setCursorPos(eyeX, eyeY);
        if (ImGui.invisibleButton("##vis_" + state.id, eyeSize, eyeSize)) {
            state.visible = !state.visible;
            saveLayout();
        }
        boolean eyeHovered = ImGui.isItemHovered();
        float ecx = eyeX + (eyeSize * 0.5f);
        float ecy = eyeY + (eyeSize * 0.5f);
        float er = eyeSize * 0.34f;
        if (eyeHovered) {
            iScaleAlpha = HudChrome.TEXT_VALUE;
        } else {
            iScaleAlpha = HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, state.visible ? a : a * LYRIC_FAR_ALPHA);
        }
        int eyeColor = iScaleAlpha;
        AnubisWidgets.eye(dl, ecx, ecy, er, eyeColor);
        if (!state.visible) {
            dl.addLine(ecx - er, ecy - er, ecx + er, ecy + er, eyeColor, Math.max(1.0f, 1.4f * u));
        }
    }

    private static float[] placeChip(HudLayoutStore.HudElementState state, float w, float h, float gap, List<HudLayoutStore.HudElementState> states, List<float[]> placed, float screenW, float screenH) {
        float left = state.x;
        float right = (state.x + state.width) - w;
        float alignX = alignsRight(state, screenW) ? right : left;
        float midY = state.y + ((state.height - h) * 0.5f);
        float[][] candidates = {new float[]{alignX, (state.y - gap) - h}, new float[]{alignX, state.y + state.height + gap}, new float[]{state.x + state.width + gap, Math.min(midY, state.y)}, new float[]{(state.x - gap) - w, Math.min(midY, state.y)}};
        for (float[] c : candidates) {
            float x = Math.round(c[0]);
            float y = Math.round(c[1]);
            if (x >= 4.0f && y >= 4.0f && x + w <= screenW - 4.0f && y + h <= screenH - 4.0f && !overlapsAny(x, y, w, h, state, states, placed, gap)) {
                return new float[]{x, y, w, h};
            }
        }
        return new float[]{Math.round(Math.max(4.0f, Math.min(alignX, (screenW - 4.0f) - w))), Math.round(Math.max(4.0f, Math.min(candidates[0][1] >= 4.0f ? candidates[0][1] : candidates[1][1], (screenH - 4.0f) - h))), w, h};
    }

    private static boolean overlapsAny(float x, float y, float w, float h, HudLayoutStore.HudElementState self, List<HudLayoutStore.HudElementState> states, List<float[]> placed, float margin) {
        for (HudLayoutStore.HudElementState other : states) {
            if (other != self && other.positioned && overlaps(x, y, w, h, other.x - (margin * 0.5f), other.y - (margin * 0.5f), other.width + margin, other.height + margin)) {
                return true;
            }
        }
        for (float[] chip : placed) {
            if (overlaps(x, y, w, h, chip[0], chip[1], chip[2], chip[3])) {
                return true;
            }
        }
        return false;
    }

    private static boolean overlaps(float ax, float ay, float aw, float ah, float bx, float by, float bw, float bh) {
        return ax < bx + bw && bx < ax + aw && ay < by + bh && by < ay + ah;
    }

    private static boolean contains(HudLayoutStore.HudElementState state, float x, float y) {
        return state.positioned && x >= state.x && x <= state.x + state.width && y >= state.y && y <= state.y + state.height;
    }

    static String editorLabel(String id) {
        switch (id) {
            case "info_bar":
                return "Info Bar";
            case "coordinates":
                return "Coordinates";
            case "module_list":
                return "Module List";
            case "potion_tracker":
                return "Potions";
            case "region_map":
                return "Region Map";
            case "anti_vanish":
                return "Anti Vanish";
            case "staff_list":
                return "Staff List";
            case "spotify":
                return "Spotify";
            case "radar":
                return "Radar";
            case "hotkeys":
                return "Hotkeys";
            case "target_card":
                return "Target HUD";
            case "armor_status":
                return "Armor HUD";
            case "item_cooldowns":
                return "Cooldown";
            default:
                return id;
        }
    }

    private static int readTabLatency(class_310 client, class_634 connection) {
        class_640 info = connection.method_2871(connection.method_2879().id());
        if (info == null) {
            info = connection.method_2871(client.field_1724.method_5667());
        }
        if (info == null) {
            info = connection.method_73471(connection.method_2879().name());
        }
        if (info == null) {
            info = connection.method_73471(client.field_1724.method_5477().getString());
        }
        if (info == null) {
            return 0;
        }
        return Math.max(0, info.method_2959());
    }

    private static int readServerPing(class_634 connection) {
        class_642 serverData = connection.method_45734();
        if (serverData == null || serverData.field_3758 <= 0 || serverData.field_3758 > 60000) {
            return 0;
        }
        return (int) serverData.field_3758;
    }

    private static float easeOutCubic(float value) {
        float t = clamp01(value);
        float inv = 1.0f - t;
        return 1.0f - ((inv * inv) * inv);
    }

    private static float smooth(float value) {
        float t = clamp01(value);
        return t * t * (3.0f - (2.0f * t));
    }

    private static float clamp01(float value) {
        return Math.max(0.0f, Math.min(1.0f, value));
    }

    private static float elementScale(HudLayoutStore.HudElementState state) {
        if (Float.isNaN(state.scale) || Float.isInfinite(state.scale)) {
            return 1.0f;
        }
        return Math.max(0.5f, Math.min(3.0f, state.scale));
    }

    static float scaleOf(HudLayoutStore.HudElementState state) {
        return elementScale(state) * ImGuiManager.getInstance().uiScale();
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/HudRenderer$DrawTransform.class */
    @Environment(EnvType.CLIENT)
    private static final class DrawTransform extends Record {
        private final float xOffset;
        private final float yOffset;
        private final float alpha;

        private DrawTransform(float xOffset, float yOffset, float alpha) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.alpha = alpha;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, DrawTransform.class), DrawTransform.class, "xOffset;yOffset;alpha", "FIELD:Lnet/anubis/client/gui/HudRenderer$DrawTransform;->xOffset:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$DrawTransform;->yOffset:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$DrawTransform;->alpha:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, DrawTransform.class), DrawTransform.class, "xOffset;yOffset;alpha", "FIELD:Lnet/anubis/client/gui/HudRenderer$DrawTransform;->xOffset:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$DrawTransform;->yOffset:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$DrawTransform;->alpha:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, DrawTransform.class, Object.class), DrawTransform.class, "xOffset;yOffset;alpha", "FIELD:Lnet/anubis/client/gui/HudRenderer$DrawTransform;->xOffset:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$DrawTransform;->yOffset:F", "FIELD:Lnet/anubis/client/gui/HudRenderer$DrawTransform;->alpha:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public float xOffset() {
            return this.xOffset;
        }

        public float yOffset() {
            return this.yOffset;
        }

        public float alpha() {
            return this.alpha;
        }

        private static DrawTransform identity() {
            return new DrawTransform(0.0f, 0.0f, 1.0f);
        }

        private DrawTransform down(float dy) {
            return dy == 0.0f ? this : new DrawTransform(this.xOffset, this.yOffset + dy, this.alpha);
        }
    }
}
