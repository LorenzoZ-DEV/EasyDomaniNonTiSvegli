package net.anubis.client.gui;

import imgui.ImDrawList;
import imgui.ImFont;
import imgui.ImGui;
import imgui.extension.implot.flag.ImPlotAxisFlags;
import imgui.flag.ImGuiKey;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.anubis.client.config.ConfigProfiles;
import net.anubis.client.config.FriendStore;
import net.anubis.client.config.HudTheme;
import net.anubis.client.config.ModuleCodes;
import net.anubis.client.config.ModuleConfigStore;
import net.anubis.client.config.ProfileException;
import net.anubis.client.config.Surface;
import net.anubis.client.config.ThemeStore;
import net.anubis.client.imgui.ImGuiManager;
import net.anubis.client.input.ModuleKeybinds;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.impl.client.HotkeysModule;
import net.anubis.client.module.impl.client.HudModule;
import net.anubis.client.module.impl.client.RadarModule;
import net.anubis.client.module.impl.client.RegionMapModule;
import net.anubis.client.module.setting.ActionSetting;
import net.anubis.client.module.setting.BlockColorListSetting;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.ItemColorListSetting;
import net.anubis.client.module.setting.KeySetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.module.setting.Setting;
import net.anubis.client.module.setting.StringListSetting;
import net.anubis.client.module.setting.StringSetting;
import net.anubis.client.util.glyph.GlyphTint;
import net.anubis.client.util.seedscan.SeedWorldgen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_156;
import net.minecraft.class_310;
import org.lwjgl.glfw.GLFW;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/ClickGuiRenderer.class */
@Environment(EnvType.CLIENT)
public final class ClickGuiRenderer {
    private static final float PANEL_W = 212.0f;
    private static final float PANEL_GAP = 14.0f;
    private static final float PANEL_TOP = 28.0f;
    private static final float HEADER_H = 42.0f;
    private static final float ROW_H = 30.0f;
    private static final float ROW_GAP = 2.0f;
    private static final float ROW_INSET = 6.0f;
    private static final float CARD_W = 460.0f;
    private static final float CARD_MAX_H = 760.0f;
    private static final float CARD_PAD = 24.0f;
    private static final float SETTING_ROW_H = 46.0f;
    private static final float TAB_BAR_H = 44.0f;
    private static final float TAB_BAR_BOTTOM = 26.0f;
    private static final float INSPECTOR_W = 360.0f;
    private static final float SMALL_TOG_W = 16.0f;
    private static final float SMALL_TOG_H = 16.0f;
    public static final int ERROR_TEXT = -10856974;
    private static final int PANEL_FLAGS = 123;
    private static final int CARD_FLAGS = 8511;
    public static final int CHILD_FLAGS = 24;
    private static final String PANEL_ID_PREFIX = "##panel_";
    private static final String SEARCH_PANEL_ID = "##panel_search";
    private static final int STYLE_VAR_COUNT = 11;
    private static final int DYNAMIC_COLOR_COUNT = 10;
    private float tabIndicatorW;
    private ClientModule settingsModule;
    private boolean cardJustOpened;
    private boolean dividerPending;
    private boolean panelGuard;
    private boolean tabBarGuard;
    private boolean cardGuard;
    private boolean cardTextLift;
    private static float scrollDragFrom;
    private boolean focusSearch;
    private float searchBodyH;
    private boolean focusFriendInput;
    private String friendMessage;
    private long friendMessageAt;
    private boolean focusProfileInput;
    private String configMessage;
    private boolean configMessageError;
    private long configMessageAt;
    private String moduleCodeFor;
    private String moduleCodeMessage;
    private boolean moduleCodeError;
    private long moduleCodeAt;
    private String pendingConfirm;
    private long pendingConfirmAt;
    private String themeMessage;
    private boolean themeMessageError;
    private long themeMessageAt;
    private Surface themeSurface;
    private boolean inspectMode;
    private float[] cardRect;
    private float[] tabBarRect;
    private float[] inspectorRect;
    private boolean focusInspector;
    private long previewChangedAt;
    private String hudElementId;
    private boolean focusHudInspector;
    private String hudMessage;
    private boolean hudMessageError;
    private long hudMessageAt;
    private static final int[] AVATAR_COLORS = {-624588, -10893516, -16080897, -2207057, -10536961, -3690448, -2730408, -16724737};
    private static final int[] THEME_COLORS = {0, AnubisTheme.GUI_TEXT, 1, AnubisTheme.GUI_TEXT_MUTED, 3, 0, 4, -97901279, 5, 352321535, 6, 0, 7, AnubisTheme.GUI_SURFACE, 8, AnubisTheme.GUI_SURFACE_HOVER, 9, AnubisTheme.GUI_SURFACE_HOVER, 21, AnubisTheme.GUI_SURFACE, 22, AnubisTheme.GUI_SURFACE_HOVER, 24, AnubisTheme.GUI_SURFACE, 25, AnubisTheme.GUI_SURFACE_HOVER, 27, AnubisTheme.GUI_DIVIDER, 14, 0, 15, 822083583, 16, 1358954495, 17, 1895825407, 30, 0, 31, 0, 32, 0, 53, 0};
    private static final ImBoolean SCROLLBAR_HOVERED = new ImBoolean();
    private static final ImBoolean SCROLLBAR_HELD = new ImBoolean();
    private Tab tab = Tab.MODULES;
    private final long guiOpenedAt = System.nanoTime();
    private long viewChangedAt = this.guiOpenedAt;
    private float tabIndicatorX = -1.0f;
    private final HashMap<String, float[]> scroll = new HashMap<>();
    private ClientModule listeningModule = null;
    private boolean listeningFirstFrame = false;
    private KeySetting listeningKeySetting = null;
    private boolean listeningKeyFirstFrame = false;
    private final ImBoolean colorPickerOpen = new ImBoolean(false);
    private ClientModule activeColorModule = null;
    private ColorSetting activeColorSetting = null;
    private final ImString globalSearch = new ImString(128);
    private final Map<String, float[]> panelSpots = new HashMap();
    private CategoryPanel[] panelCache = new CategoryPanel[0];
    private int panelCacheRevision = -1;
    private final Map<String, float[]> panelFold = new HashMap();
    private final Map<String, Motion.Rows> rowMotion = new HashMap();
    private final Int2ObjectOpenHashMap<String> keyLabels = new Int2ObjectOpenHashMap<>();
    private int cachedRegistryRevision = -1;
    private String cachedGlobalSearch = "";
    private List<ClientModule> cachedGlobalResults = List.of();
    private final ImString friendInput = new ImString(32);
    private final ImString profileInput = new ImString(64);
    private final ProfileList profileList = new ProfileList((v1, v2) -> {
        setConfigMessage(v1, v2);
    });
    private final List<float[]> panelRects = new ArrayList();
    private final BlockListEditor blockLists = new BlockListEditor();
    private ItemColorListSetting activeItemColorPalette = null;
    private boolean itemColorWindowOpen = false;
    private final ImString itemSearchBuf = new ImString(256);
    private ItemColorListSetting.ItemEntry activeItemColorEntry = null;
    private boolean editingItemDefaultColor = false;
    private final HashMap<ItemColorListSetting.ItemEntry, Float> itemEntryTogAnim = new HashMap<>();
    private final ImBoolean itemColorPickerOpen = new ImBoolean(false);

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/ClickGuiRenderer$Tab.class */
    @Environment(EnvType.CLIENT)
    private enum Tab {
        MODULES("Modules"),
        HUD("HUD"),
        FRIENDS("Friends"),
        THEME(GlyphTint.THEME),
        CONFIG("Config");

        final String label;

        Tab(String label) {
            this.label = label;
        }
    }

    public void render() {
        float dt = animationDelta(Math.max(ImGui.getIO().getDeltaTime(), 0.001f));
        ThemeStore theme = ThemeStore.get();
        this.panelGuard = HudChrome.needsTextGuard(theme.panels);
        this.tabBarGuard = HudChrome.needsTextGuard(theme.tabBar);
        this.cardGuard = HudChrome.needsTextGuard(theme.cards);
        this.cardTextLift = theme.cards.isImage() && !theme.cards.isBanner();
        applyTheme();
        boolean moduleFontPushed = pushFont(ImGuiManager.getInstance().moduleFont());
        try {
            float sw = ImGui.getIO().getDisplaySizeX();
            float sh = ImGui.getIO().getDisplaySizeY();
            float open = easeOut(elapsed(this.guiOpenedAt, 240.0f));
            float view = easeOut(elapsed(this.viewChangedAt, 260.0f));
            float alpha = Math.max(0.01f, open * view);
            ImGui.getBackgroundDrawList().addRectFilled(0.0f, 0.0f, sw, sh, AnubisWidgets.withAlpha(-16777216, theme.backgroundDim.get() * open));
            Path picked = ThemeImages.get().takePendingImport();
            if (picked != null) {
                importHeaderImage(picked);
            }
            handleShortcuts();
            switch (this.tab) {
                case MODULES:
                    if (this.settingsModule != null) {
                        renderModuleCard(this.settingsModule, dt, alpha, view);
                    } else {
                        renderModulePanels(dt, alpha);
                    }
                    break;
                case HUD:
                    renderHudStudio(dt, alpha, view);
                    break;
                case FRIENDS:
                    renderFriendsCard(dt, alpha, view);
                    break;
                case THEME:
                    renderThemeStudio(dt, alpha, view);
                    break;
                case CONFIG:
                    renderConfigCard(dt, alpha, view);
                    break;
            }
            renderTabBar(dt, open);
            renderColorPickerWindow();
            this.blockLists.renderWindows();
            renderItemColorPaletteWindow();
            closeCardOnOutsideClick();
        } finally {
            if (moduleFontPushed) {
                ImGui.popFont();
            }
            removeTheme();
        }
    }

    private void handleShortcuts() {
        if (this.tab == Tab.MODULES && this.settingsModule == null && ImGui.getIO().getKeyCtrl() && ImGui.isKeyPressed(ImGuiKey.F)) {
            this.focusSearch = true;
        }
    }

    private void switchTab(Tab next) {
        if (next == this.tab) {
            return;
        }
        closeSettings();
        this.tab = next;
        this.viewChangedAt = System.nanoTime();
        this.pendingConfirm = null;
        this.profileList.disarm();
        if (next == Tab.CONFIG) {
            ConfigProfiles.getInstance().refresh();
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/ClickGuiRenderer$CategoryPanel.class */
    @Environment(EnvType.CLIENT)
    private static final class CategoryPanel extends Record {
        private final ModuleCategory category;
        private final String id;
        private final String rowsId;

        private CategoryPanel(ModuleCategory category, String id, String rowsId) {
            this.category = category;
            this.id = id;
            this.rowsId = rowsId;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, CategoryPanel.class), CategoryPanel.class, "category;id;rowsId", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$CategoryPanel;->category:Lnet/anubis/client/module/ModuleCategory;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$CategoryPanel;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$CategoryPanel;->rowsId:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, CategoryPanel.class), CategoryPanel.class, "category;id;rowsId", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$CategoryPanel;->category:Lnet/anubis/client/module/ModuleCategory;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$CategoryPanel;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$CategoryPanel;->rowsId:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, CategoryPanel.class, Object.class), CategoryPanel.class, "category;id;rowsId", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$CategoryPanel;->category:Lnet/anubis/client/module/ModuleCategory;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$CategoryPanel;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$CategoryPanel;->rowsId:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public ModuleCategory category() {
            return this.category;
        }

        public String id() {
            return this.id;
        }

        public String rowsId() {
            return this.rowsId;
        }
    }

    private void renderModulePanels(float dt, float alpha) {
        CategoryPanel[] panels = visiblePanels();
        boolean placed = this.panelSpots.containsKey(SEARCH_PANEL_ID);
        for (CategoryPanel panel : panels) {
            placed &= this.panelSpots.containsKey(panel.id());
        }
        if (!placed) {
            placeNewPanels(panels);
        }
        for (CategoryPanel panel2 : panels) {
            renderCategoryPanel(panel2, dt, alpha);
        }
        renderSearchPanel(dt, alpha);
    }

    private CategoryPanel[] visiblePanels() {
        int revision = ModuleRegistry.revision();
        if (revision != this.panelCacheRevision) {
            List<CategoryPanel> panels = new ArrayList<>();
            for (ModuleCategory category : ModuleCategory.values()) {
                if (!modulesFor(category).isEmpty()) {
                    panels.add(new CategoryPanel(category, "##panel_" + category.name(), "##rows_" + category.name()));
                }
            }
            this.panelCache = (CategoryPanel[]) panels.toArray(x$0 -> {
                return new CategoryPanel[x$0];
            });
            this.panelCacheRevision = revision;
        }
        return this.panelCache;
    }

    private void renderCategoryPanel(CategoryPanel panel, float dt, float alpha) {
        List<ClientModule> modules = modulesFor(panel.category());
        float listH = Math.min(rowListHeight(modules.size()), maxListHeight());
        float open = unfolded(panel, dt);
        if (open < 1.0f) {
            ImGui.setNextWindowSize(AnubisWidgets.s(PANEL_W), AnubisWidgets.s(42.0f) + (listH * open), 1);
        }
        if (beginPanel(panel.id(), alpha)) {
            String title = panel.category().displayName();
            drawPanelChrome(title, panel.category());
            recordPanelRect();
            renderFoldControl(panel.category(), title, open);
            if (open > 0.0f) {
                renderRowList(panel.rowsId(), modules, listH, false, dt);
            }
        }
        endPanel();
    }

    private float unfolded(CategoryPanel panel, float dt) {
        float target = PanelStateStore.getInstance().isCollapsed(panel.category()) ? 0.0f : 1.0f;
        float[] open = this.panelFold.get(panel.id());
        if (open == null) {
            float[] fArr = {target};
            open = fArr;
            this.panelFold.put(panel.id(), fArr);
        }
        open[0] = smooth(open[0], target, dt, 14.0f);
        if (Math.abs(open[0] - target) < 0.002f) {
            open[0] = target;
        }
        return open[0];
    }

    private void renderFoldControl(ModuleCategory category, String title, float open) {
        float wx = ImGui.getWindowPosX();
        float wy = ImGui.getWindowPosY();
        float ww = ImGui.getWindowWidth();
        float headerH = AnubisWidgets.s(42.0f);
        float hit = AnubisWidgets.s(22.0f);
        ImFont font = ImGuiManager.getInstance().categoryFont();
        float cx = Math.min(wx + AnubisWidgets.s(14.0f) + AnubisWidgets.textWidth(font, title) + AnubisWidgets.s(12.0f), (wx + ww) - AnubisWidgets.s(40.0f));
        float cy = wy + (headerH * 0.5f);
        boolean overHeader = !this.inspectMode && ImGui.isWindowHovered() && ImGui.isMouseHoveringRect(wx, wy, wx + ww, wy + headerH);
        ImGui.setCursorScreenPos(cx - (hit * 0.5f), cy - (hit * 0.5f));
        boolean clicked = ImGui.invisibleButton("##fold", hit, hit) && !this.inspectMode;
        float hover = AnubisWidgets.animate(ImGui.getItemID(), overHeader, 18.0f);
        ImGui.setCursorPos(0.0f, headerH);
        if (clicked || (overHeader && ImGui.isMouseClicked(1))) {
            PanelStateStore store = PanelStateStore.getInstance();
            store.setCollapsed(category, !store.isCollapsed(category));
        }
        AnubisWidgets.chevronRotated(ImGui.getWindowDrawList(), cx, cy, AnubisWidgets.s(4.0f), AnubisTheme.lerpColor(AnubisTheme.GUI_TEXT_MUTED, AnubisTheme.GUI_TEXT, hover), open * 1.5707964f);
    }

    private void renderSearchPanel(float dt, float alpha) {
        float fS;
        if (beginPanel(SEARCH_PANEL_ID, alpha)) {
            drawPanelChrome("Search", null);
            recordPanelRect();
            float inset = AnubisWidgets.s(ROW_INSET) + AnubisWidgets.s(ROW_GAP);
            ImGui.setCursorPos(inset, AnubisWidgets.s(42.0f) - AnubisWidgets.s(4.0f));
            ImGui.pushStyleVar(11, AnubisWidgets.s(10.0f), AnubisWidgets.s(7.0f));
            ImGui.setNextItemWidth(AnubisWidgets.s(PANEL_W) - (inset * ROW_GAP));
            if (this.focusSearch) {
                ImGui.setKeyboardFocusHere();
                this.focusSearch = false;
            }
            ImGui.inputTextWithHint("##globalsearch", "Search modules...", this.globalSearch);
            ImGui.popStyleVar();
            ImGui.dummy(0.0f, AnubisWidgets.s(8.0f));
            String query = normalizeSearch(this.globalSearch.get());
            List<ClientModule> results = query.isEmpty() ? List.of() : searchModules(query);
            if (query.isEmpty()) {
                fS = 0.0f;
            } else {
                fS = results.isEmpty() ? AnubisWidgets.s(34.0f) : Math.min(rowListHeight(results.size()), maxListHeight());
            }
            float target = fS;
            this.searchBodyH = smooth(this.searchBodyH, target, dt, 14.0f);
            if (Math.abs(this.searchBodyH - target) < 0.5f) {
                this.searchBodyH = target;
            }
            if (this.searchBodyH >= 1.0f) {
                if (results.isEmpty()) {
                    ImGui.beginChild("##search_empty", AnubisWidgets.s(PANEL_W), this.searchBodyH, false, 24);
                    ImFont font = ImGui.getFont();
                    AnubisWidgets.text(ImGui.getWindowDrawList(), font, ImGui.getWindowPosX() + ((AnubisWidgets.s(PANEL_W) - AnubisWidgets.textWidth(font, "No results")) * 0.5f), ImGui.getWindowPosY() + AnubisWidgets.s(4.0f), AnubisTheme.GUI_TEXT_MUTED, "No results", this.panelGuard);
                    ImGui.endChild();
                } else {
                    renderRowList("##search_rows", results, this.searchBodyH, true, dt);
                }
            }
        }
        endPanel();
    }

    private boolean beginPanel(String id, float alpha) {
        float[] spot = this.panelSpots.get(id);
        ImGui.setNextWindowPos(spot[0], spot[1], 4);
        ImGui.setNextWindowSizeConstraints(AnubisWidgets.s(PANEL_W), 0.0f, AnubisWidgets.s(PANEL_W), Float.MAX_VALUE);
        ImGui.pushStyleVar(0, alpha);
        ImGui.pushStyleVar(2, 0.0f, 0.0f);
        ImGui.pushStyleVar(14, 0.0f, 0.0f);
        ImGui.pushStyleVar(3, panelRounding());
        boolean open = ImGui.begin(id, PANEL_FLAGS);
        spot[0] = ImGui.getWindowPosX();
        spot[1] = ImGui.getWindowPosY();
        return open;
    }

    private static void endPanel() {
        ImGui.end();
        ImGui.popStyleVar(4);
    }

    private void drawPanelChrome(String title, ModuleCategory category) {
        float wx = ImGui.getWindowPosX();
        float wy = ImGui.getWindowPosY();
        float ww = ImGui.getWindowWidth();
        float wh = ImGui.getWindowHeight();
        drawShadow(wx, wy, ww, wh, panelRounding(), 4);
        ImDrawList dl = ImGui.getWindowDrawList();
        SurfaceBackground.draw(dl, ThemeStore.get().panels, wx, wy, ww, wh, panelRounding());
        ImFont font = ImGuiManager.getInstance().categoryFont();
        float headerH = AnubisWidgets.s(42.0f);
        AnubisWidgets.text(dl, font, wx + AnubisWidgets.s(14.0f), wy + ((headerH - AnubisWidgets.lineHeight(font)) * 0.5f), AnubisTheme.GUI_TEXT, title, this.panelGuard);
        float cx = (wx + ww) - AnubisWidgets.s(20.0f);
        float cy = wy + (headerH * 0.5f);
        float r = AnubisWidgets.s(7.0f);
        if (category == null) {
            AnubisWidgets.magnifier(dl, cx, cy, r, AnubisTheme.GUI_TEXT_MUTED);
        } else {
            drawCategoryIcon(dl, category, cx, cy, r, AnubisTheme.GUI_TEXT_MUTED);
        }
        ImGui.dummy(ww, headerH);
    }

    private static void drawCategoryIcon(ImDrawList dl, ModuleCategory category, float cx, float cy, float r, int color) {
        switch (category.icon()) {
            case CROSSHAIR:
                AnubisWidgets.crosshair(dl, cx, cy, r, color);
                break;
            case CHEVRON:
                AnubisWidgets.chevron(dl, cx, cy, r, color, true);
                break;
            case GRID:
                AnubisWidgets.grid(dl, cx, cy, r, color);
                break;
            case RING:
                AnubisWidgets.ring(dl, cx, cy, r, color);
                break;
            case EYE:
                AnubisWidgets.eye(dl, cx, cy, r, color);
                break;
            case MONITOR:
                AnubisWidgets.monitor(dl, cx, cy, r, color);
                break;
            case HOUSE:
                AnubisWidgets.house(dl, cx, cy, r, color);
                break;
            case PAPYRUS:
                AnubisWidgets.papyrus(dl, cx, cy, r, color);
                break;
            case ANKH:
                AnubisWidgets.ankh(dl, cx, cy, r, color);
                break;
        }
    }

    public static void drawShadow(float x, float y, float w, float h, float rounding, int layers) {
        ImDrawList bg = ImGui.getBackgroundDrawList();
        for (int i = 1; i <= layers; i++) {
            float e = AnubisWidgets.s(2.5f) * i;
            bg.addRectFilled(x - e, (y - e) + AnubisWidgets.s(4.0f), x + w + e, y + h + e + AnubisWidgets.s(4.0f), AnubisWidgets.c(AnubisTheme.GUI_SHADOW), rounding + e);
        }
    }

    private static float rowListHeight(int rows) {
        return ((rows * AnubisWidgets.s(32.0f)) - AnubisWidgets.s(ROW_GAP)) + AnubisWidgets.s(ROW_INSET);
    }

    private static float maxListHeight() {
        return Math.max(AnubisWidgets.s(120.0f), (ImGui.getIO().getDisplaySizeY() * 0.62f) - AnubisWidgets.s(42.0f));
    }

    private void renderRowList(String id, List<ClientModule> modules, float height, boolean showCategory, float dt) {
        if (ImGui.beginChild(id, AnubisWidgets.s(PANEL_W), Math.max(1.0f, height), false, 24)) {
            Motion.Rows motion = this.rowMotion.computeIfAbsent(id, key -> {
                return new Motion.Rows();
            });
            motion.bind(modules);
            for (int i = 0; i < modules.size(); i++) {
                renderModuleRow(modules.get(i), showCategory, motion, i, dt);
            }
            ImGui.dummy(1.0f, AnubisWidgets.s(ROW_INSET) - AnubisWidgets.s(ROW_GAP));
            applySmoothScroll(id, dt);
        }
        ImGui.endChild();
    }

    private void renderModuleRow(ClientModule module, boolean showCategory, Motion.Rows motion, int row, float dt) {
        String strKeyLabel;
        String str;
        ImGui.pushID(module.id());
        float inset = AnubisWidgets.s(ROW_INSET);
        float rowH = AnubisWidgets.s(ROW_H);
        float localY = ImGui.getCursorPosY();
        ImGui.setCursorPosX(inset);
        float x0 = ImGui.getCursorScreenPosX();
        float y0 = ImGui.getCursorScreenPosY();
        float w = ImGui.getWindowWidth() - (inset * ROW_GAP);
        if (ImGui.invisibleButton("##row", w, rowH) && !this.inspectMode) {
            if (module.isAction()) {
                module.onClick();
            } else {
                module.toggle();
            }
        }
        boolean hovered = ImGui.isItemHovered();
        if (!this.inspectMode && ImGui.isItemClicked(1)) {
            openSettings(module);
        }
        if (!this.inspectMode && ImGui.isItemHovered(65536)) {
            String description = module.description();
            if (description == null || description.isBlank()) {
                str = "Right-click for settings";
            } else {
                str = description + "\nRight-click for settings";
            }
            showTooltip(str);
        }
        float hover = motion.hover(row, hovered, dt, 18.0f);
        float on = motion.on(row, module.isEnabled(), dt, 14.0f);
        ImDrawList dl = ImGui.getWindowDrawList();
        float r = AnubisWidgets.s(ROW_INSET);
        if (hover > 0.01f) {
            dl.addRectFilled(x0, y0, x0 + w, y0 + rowH, AnubisWidgets.c(AnubisWidgets.withAlpha(318767103, hover)), r);
        }
        if (on > 0.01f) {
            dl.addRectFilled(x0, y0, x0 + w, y0 + rowH, AnubisWidgets.c(AnubisWidgets.withAlpha(AnubisTheme.lerpColor(GuiPalette.accent(), GuiPalette.accentHover(), hover * 0.3f), on)), r);
        }
        ImFont font = ImGui.getFont();
        ImFont small = ImGuiManager.getInstance().smallFont();
        if (showCategory) {
            strKeyLabel = module.category().displayName();
        } else {
            strKeyLabel = module.keybind() == -1 ? null : keyLabel(module.keybind());
        }
        String trailing = strKeyLabel;
        float tw = trailing == null ? 0.0f : AnubisWidgets.textWidth(small, trailing);
        int textColor = AnubisTheme.lerpColor(AnubisTheme.lerpColor(AnubisTheme.GUI_TEXT_ROW, AnubisTheme.GUI_TEXT, hover), -1, on);
        String name = showCategory ? AnubisWidgets.ellipsize(font, module.name(), (w - AnubisWidgets.s(PANEL_TOP)) - tw) : module.name();
        AnubisWidgets.text(dl, font, x0 + AnubisWidgets.s(10.0f), y0 + ((rowH - AnubisWidgets.lineHeight(font)) * 0.5f), textColor, name, this.panelGuard);
        if (trailing != null) {
            int trailColor = AnubisTheme.lerpColor(AnubisTheme.GUI_TEXT_MUTED, -419430401, on);
            AnubisWidgets.text(dl, small, ((x0 + w) - AnubisWidgets.s(10.0f)) - tw, y0 + ((rowH - AnubisWidgets.lineHeight(small)) * 0.5f), trailColor, trailing, this.panelGuard);
        } else if (module.isAction()) {
            AnubisWidgets.chevronRight(dl, (x0 + w) - AnubisWidgets.s(14.0f), y0 + (rowH * 0.5f), AnubisWidgets.s(4.0f), AnubisTheme.lerpColor(AnubisTheme.GUI_TEXT_MUTED, AnubisTheme.GUI_TEXT, hover));
        }
        ImGui.setCursorPos(0.0f, localY + rowH + AnubisWidgets.s(ROW_GAP));
        ImGui.popID();
    }

    public static void showTooltip(String text) {
        ImGui.pushStyleVar(2, AnubisWidgets.s(10.0f), AnubisWidgets.s(8.0f));
        ImGui.beginTooltip();
        ImGui.pushTextWrapPos(AnubisWidgets.s(280.0f));
        ImGui.textUnformatted(text);
        ImGui.popTextWrapPos();
        ImGui.endTooltip();
        ImGui.popStyleVar();
    }

    private void applySmoothScroll(String key, float dt) {
        smoothScroll(this.scroll.computeIfAbsent(key, k -> {
            return new float[2];
        }), dt);
    }

    public static void smoothScroll(float[] state, float dt) {
        float max = ImGui.getScrollMaxY();
        float wx = ImGui.getWindowPosX();
        float wy = ImGui.getWindowPosY();
        float ww = ImGui.getWindowWidth();
        float wh = ImGui.getWindowHeight();
        float barH = Math.max(AnubisWidgets.s(24.0f), (wh * wh) / (wh + max));
        float travel = Math.max(1.0f, wh - barH);
        boolean held = false;
        boolean hot = false;
        if (max > 0.5f) {
            int id = ImGui.getID("##scrollbar");
            imgui.internal.ImGui.keepAliveID(id);
            imgui.internal.ImGui.buttonBehavior((wx + ww) - AnubisWidgets.s(8.0f), wy, wx + ww, wy + wh, id, SCROLLBAR_HOVERED, SCROLLBAR_HELD);
            held = SCROLLBAR_HELD.get();
            hot = held || SCROLLBAR_HOVERED.get();
            if (held && ImGui.isMouseClicked(0)) {
                float barY = wy + (travel * (state[0] / max));
                float mouseY = ImGui.getMousePosY();
                state[1] = (mouseY < barY || mouseY > barY + barH) ? (((mouseY - wy) - (barH * 0.5f)) / travel) * max : state[0];
                scrollDragFrom = state[1];
            } else if (held) {
                state[1] = scrollDragFrom + ((ImGui.getMouseDragDeltaY(0, 0.0f) * max) / travel);
            }
        }
        if (!held && ImGui.isWindowHovered()) {
            float wheel = ImGui.getIO().getMouseWheel();
            if (wheel != 0.0f) {
                state[1] = state[1] - (wheel * AnubisWidgets.s(56.0f));
            }
        }
        state[1] = Math.max(0.0f, Math.min(max, state[1]));
        state[0] = held ? state[1] : smooth(state[0], state[1], dt, 16.0f);
        if (Math.abs(state[0] - state[1]) < 0.3f) {
            state[0] = state[1];
        }
        ImGui.setScrollY(state[0]);
        if (max > 0.5f) {
            float barY2 = wy + (travel * (state[0] / max));
            ImGui.getWindowDrawList().addRectFilled((wx + ww) - (hot ? AnubisWidgets.s(ROW_INSET) : AnubisWidgets.s(4.0f)), barY2, (wx + ww) - AnubisWidgets.s(1.5f), barY2 + barH, AnubisWidgets.c(hot ? 1728053247 : 872415231), AnubisWidgets.s(ROW_GAP));
        }
    }

    private void placeNewPanels(CategoryPanel[] panels) {
        List<String> ids = new ArrayList<>();
        List<Float> heights = new ArrayList<>();
        for (CategoryPanel panel : panels) {
            ids.add(panel.id());
            heights.add(Float.valueOf(AnubisWidgets.s(42.0f) + Math.min(rowListHeight(modulesFor(panel.category()).size()), maxListHeight())));
        }
        ids.add(SEARCH_PANEL_ID);
        heights.add(Float.valueOf(AnubisWidgets.s(42.0f) + ImGui.getFontSize() + AnubisWidgets.s(18.0f) + maxListHeight()));
        Map<String, float[]> saved = savedPanelSpots();
        List<float[]> taken = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            float[] spot = this.panelSpots.get(ids.get(i));
            if (spot == null && saved.containsKey(ids.get(i))) {
                spot = saved.get(ids.get(i));
                this.panelSpots.put(ids.get(i), spot);
            }
            if (spot != null) {
                taken.add(new float[]{spot[0], spot[1], AnubisWidgets.s(PANEL_W), heights.get(i).floatValue()});
            }
        }
        boolean firstRun = taken.isEmpty();
        for (int i2 = 0; i2 < ids.size(); i2++) {
            if (!this.panelSpots.containsKey(ids.get(i2))) {
                float h = heights.get(i2).floatValue();
                float[] spot2 = firstRun ? panelPosFor(i2, ids.size()) : null;
                if (spot2 == null || coveredArea(spot2[0], spot2[1], h, taken) > 0.0f) {
                    spot2 = freeSpot(h, taken);
                }
                this.panelSpots.put(ids.get(i2), spot2);
                taken.add(new float[]{spot2[0], spot2[1], AnubisWidgets.s(PANEL_W), h});
            }
        }
    }

    private static float[] panelPosFor(int index, int panelCount) {
        float sw = ImGui.getIO().getDisplaySizeX();
        float sh = ImGui.getIO().getDisplaySizeY();
        float w = AnubisWidgets.s(PANEL_W);
        float gap = AnubisWidgets.s(14.0f);
        int columns = Math.max(1, (int) (((sw - AnubisWidgets.s(40.0f)) + gap) / (w + gap)));
        int inRow = Math.min(columns, panelCount);
        float rowW = (inRow * (w + gap)) - gap;
        float startX = Math.max(AnubisWidgets.s(20.0f), (sw - rowW) * 0.5f);
        return new float[]{startX + ((index % columns) * (w + gap)), AnubisWidgets.s(PANEL_TOP) + ((index / columns) * sh * 0.45f)};
    }

    private static float[] freeSpot(float h, List<float[]> taken) {
        float sw = ImGui.getIO().getDisplaySizeX();
        float sh = ImGui.getIO().getDisplaySizeY();
        float w = AnubisWidgets.s(PANEL_W);
        float gap = AnubisWidgets.s(14.0f);
        float margin = AnubisWidgets.s(20.0f);
        float bottom = (sh - AnubisWidgets.s(70.0f)) - gap;
        float highest = Float.MAX_VALUE;
        Iterator<float[]> it = taken.iterator();
        while (it.hasNext()) {
            highest = Math.min(highest, it.next()[1]);
        }
        float[] rightMost = null;
        for (float[] rect : taken) {
            boolean topRow = rect[1] < highest + AnubisWidgets.s(42.0f);
            if (topRow && (rightMost == null || rect[0] + rect[2] > rightMost[0] + rightMost[2])) {
                rightMost = rect;
            }
        }
        float[] beside = rightMost == null ? new float[]{margin, AnubisWidgets.s(PANEL_TOP)} : new float[]{rightMost[0] + rightMost[2] + gap, rightMost[1]};
        List<float[]> scan = new ArrayList<>();
        scan.add(new float[]{margin, AnubisWidgets.s(PANEL_TOP)});
        for (float[] rect2 : taken) {
            scan.add(new float[]{rect2[0] + rect2[2] + gap, rect2[1]});
            scan.add(new float[]{rect2[0], rect2[1] + rect2[3] + gap});
        }
        scan.sort(Comparator.comparingDouble(spot -> {
            return spot[1];
        }).thenComparingDouble(spot2 -> {
            return spot2[0];
        }));
        scan.addFirst(beside);
        for (float[] spot3 : scan) {
            boolean onScreen = spot3[0] >= 0.0f && spot3[1] >= 0.0f && spot3[0] + w <= sw - margin && spot3[1] + h <= bottom;
            if (onScreen && coveredArea(spot3[0], spot3[1], h, taken) == 0.0f) {
                return spot3;
            }
        }
        float[] best = null;
        float bestArea = Float.MAX_VALUE;
        for (float[] spot4 : scan) {
            float x = Math.max(0.0f, Math.min(spot4[0], (sw - w) - margin));
            float y = Math.max(0.0f, Math.min(spot4[1], (sh - h) - margin));
            float area = coveredArea(x, y, h, taken);
            if (area < bestArea) {
                best = new float[]{x, y};
                bestArea = area;
            }
        }
        return best;
    }

    private static float coveredArea(float x, float y, float h, List<float[]> taken) {
        float w = AnubisWidgets.s(PANEL_W);
        float m = AnubisWidgets.s(14.0f) * 0.5f;
        float area = 0.0f;
        for (float[] r : taken) {
            float overlapW = (Math.min(x + w, r[0] + r[2]) + m) - Math.max(x, r[0]);
            float overlapH = (Math.min(y + h, r[1] + r[3]) + m) - Math.max(y, r[1]);
            if (overlapW > 0.0f && overlapH > 0.0f) {
                area += overlapW * overlapH;
            }
        }
        return area;
    }

    private static Map<String, float[]> savedPanelSpots() {
        Map<String, float[]> spots = new HashMap<>();
        String ini = ImGui.getIO().getIniFilename();
        if (ini == null || !Files.isRegularFile(Path.of(ini, new String[0]), new LinkOption[0])) {
            return spots;
        }
        try {
            String window = null;
            for (String line : Files.readAllLines(Path.of(ini, new String[0]))) {
                if (line.startsWith("[")) {
                    boolean panel = line.startsWith("[Window][" + "##panel_") && line.endsWith("]");
                    window = panel ? line.substring("[Window][".length(), line.length() - 1) : null;
                } else if (window != null && line.startsWith("Pos=")) {
                    String[] xy = line.substring(4).split(",");
                    spots.put(window, new float[]{Float.parseFloat(xy[0]), Float.parseFloat(xy[1])});
                }
            }
        } catch (IOException | RuntimeException e) {
            spots.clear();
        }
        return spots;
    }

    private List<ClientModule> modulesFor(ModuleCategory category) {
        return ModuleRegistry.inCategory(category);
    }

    private List<ClientModule> searchModules(String query) {
        int revision = ModuleRegistry.revision();
        if (revision != this.cachedRegistryRevision) {
            this.cachedRegistryRevision = revision;
            this.cachedGlobalSearch = "";
            this.cachedGlobalResults = List.of();
        }
        if (query.equals(this.cachedGlobalSearch)) {
            return this.cachedGlobalResults;
        }
        List<ClientModule> matches = new ArrayList<>();
        for (ClientModule module : ModuleRegistry.all()) {
            if (module.name().toLowerCase(Locale.ROOT).contains(query)) {
                matches.add(module);
            }
        }
        this.cachedGlobalSearch = query;
        this.cachedGlobalResults = List.copyOf(matches);
        return this.cachedGlobalResults;
    }

    private static String normalizeSearch(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT).trim();
    }

    private void renderTabBar(float dt, float open) {
        Tab[] tabs = Tab.values();
        ImFont font = ImGui.getFont();
        float h = AnubisWidgets.s(TAB_BAR_H);
        float inner = AnubisWidgets.s(4.0f);
        float padX = AnubisWidgets.s(20.0f);
        float[] widths = new float[tabs.length];
        float total = inner * ROW_GAP;
        for (int i = 0; i < tabs.length; i++) {
            widths[i] = AnubisWidgets.textWidth(font, tabs[i].label) + (padX * ROW_GAP);
            total += widths[i];
        }
        float sw = ImGui.getIO().getDisplaySizeX();
        float sh = ImGui.getIO().getDisplaySizeY();
        float x = (sw - total) * 0.5f;
        float y = ((sh - h) - AnubisWidgets.s(TAB_BAR_BOTTOM)) + (AnubisWidgets.s(16.0f) * (1.0f - open));
        ImGui.setNextWindowPos(x, y, 1);
        ImGui.setNextWindowSize(total, h, 1);
        ImGui.pushStyleVar(0, Math.max(0.01f, open));
        ImGui.pushStyleVar(2, 0.0f, 0.0f);
        ImGui.pushStyleVar(3, h * 0.5f);
        ImGui.pushStyleColor(2, GuiPalette.tabBarBg());
        if (ImGui.begin("##tabbar", SeedWorldgen.MAX_Y)) {
            this.tabBarRect = new float[]{x, y, total, h};
            drawShadow(x, y, total, h, h * 0.5f, 4);
            ImDrawList dl = ImGui.getWindowDrawList();
            SurfaceBackground.draw(dl, ThemeStore.get().tabBar, x, y, total, h, h * 0.5f);
            dl.addRect(x, y, x + total, y + h, AnubisWidgets.c(318767103), h * 0.5f, 0, 1.0f);
            float targetX = inner;
            for (int i2 = 0; i2 < this.tab.ordinal(); i2++) {
                targetX += widths[i2];
            }
            float targetW = widths[this.tab.ordinal()];
            if (this.tabIndicatorX < 0.0f) {
                this.tabIndicatorX = targetX;
                this.tabIndicatorW = targetW;
            }
            this.tabIndicatorX = smooth(this.tabIndicatorX, targetX, dt, 18.0f);
            this.tabIndicatorW = smooth(this.tabIndicatorW, targetW, dt, 18.0f);
            float pillH = h - (inner * ROW_GAP);
            dl.addRectFilled(x + this.tabIndicatorX, y + inner, x + this.tabIndicatorX + this.tabIndicatorW, y + inner + pillH, AnubisWidgets.c(GuiPalette.accent()), pillH * 0.5f);
            float tx = inner;
            for (int i3 = 0; i3 < tabs.length; i3++) {
                ImGui.setCursorPos(tx, inner);
                if (ImGui.invisibleButton("##tab_" + tabs[i3].name(), widths[i3], pillH)) {
                    switchTab(tabs[i3]);
                }
                float hover = AnubisWidgets.animate(ImGui.getItemID(), ImGui.isItemHovered(), 18.0f);
                float centre = x + tx + (widths[i3] * 0.5f);
                float cover = Math.max(0.0f, 1.0f - (Math.abs(centre - ((x + this.tabIndicatorX) + (this.tabIndicatorW * 0.5f))) / Math.max(1.0f, widths[i3] * 0.5f)));
                int color = AnubisTheme.lerpColor(AnubisTheme.lerpColor(AnubisTheme.GUI_TEXT_MUTED, AnubisTheme.GUI_TEXT, hover), -1, cover);
                float tw = AnubisWidgets.textWidth(font, tabs[i3].label);
                AnubisWidgets.text(dl, font, x + tx + ((widths[i3] - tw) * 0.5f), y + ((h - AnubisWidgets.lineHeight(font)) * 0.5f), color, tabs[i3].label, this.tabBarGuard);
                tx += widths[i3];
            }
        }
        ImGui.end();
        ImGui.popStyleColor();
        ImGui.popStyleVar(3);
    }

    private float[] beginCard(String id, float alpha, float view) {
        return beginCard(id, alpha, view, ImGui.getIO().getDisplaySizeX() * 0.5f);
    }

    private float[] beginCard(String id, float alpha, float view, float centreX) {
        float sw = ImGui.getIO().getDisplaySizeX();
        float sh = ImGui.getIO().getDisplaySizeY();
        float reserved = AnubisWidgets.s(86.0f);
        float w = Math.min(sw - AnubisWidgets.s(32.0f), AnubisWidgets.s(CARD_W));
        float h = Math.min((sh - reserved) - AnubisWidgets.s(32.0f), AnubisWidgets.s(CARD_MAX_H));
        float x = Math.max(AnubisWidgets.s(16.0f), centreX - (w * 0.5f));
        float y = Math.max(AnubisWidgets.s(16.0f), (((sh - reserved) - h) * 0.5f) + AnubisWidgets.s(8.0f)) + (AnubisWidgets.s(24.0f) * (1.0f - view));
        ImGui.setNextWindowPos(x, y, 1);
        ImGui.setNextWindowSize(w, h, 1);
        ImGui.pushStyleVar(0, alpha);
        ImGui.pushStyleVar(2, 0.0f, 0.0f);
        ImGui.pushStyleVar(14, 0.0f, 0.0f);
        ImGui.pushStyleVar(3, cardRounding());
        ImGui.pushStyleColor(2, GuiPalette.cardBg());
        if (this.cardTextLift) {
            ImGui.pushStyleColor(0, -1);
            ImGui.pushStyleColor(1, AnubisTheme.GUI_TEXT_ROW);
        }
        ImGui.begin(id, CARD_FLAGS);
        drawShadow(x, y, w, h, cardRounding(), 6);
        ImDrawList dl = ImGui.getWindowDrawList();
        Surface cards = ThemeStore.get().cards;
        SurfaceBackground.draw(dl, cards, x, y, w, h, cardRounding());
        dl.addRect(x, y, x + w, y + h, AnubisWidgets.c(318767103), cardRounding(), 0, 1.0f);
        float top = SurfaceBackground.hasImageBanner(cards) ? Math.max(AnubisWidgets.s(22.0f), SurfaceBackground.bannerHeight(cards) - AnubisWidgets.s(TAB_BAR_H)) : AnubisWidgets.s(22.0f);
        return new float[]{w, h, top, x, y};
    }

    private static float panelRounding() {
        return AnubisWidgets.s(GuiPalette.rounding());
    }

    public static float cardRounding() {
        return AnubisWidgets.s(Math.min(PANEL_TOP, GuiPalette.rounding() * 2.2f));
    }

    private void endCard() {
        ImGui.end();
        ImGui.popStyleColor(this.cardTextLift ? 3 : 1);
        ImGui.popStyleVar(4);
    }

    public static void drawCardTitle(String title, float x, float centreY) {
        drawCardTitle(title, x, centreY, false);
    }

    private static void drawCardTitle(String title, float x, float centreY, boolean guard) {
        ImFont font = ImGuiManager.getInstance().titleFont();
        AnubisWidgets.text(ImGui.getWindowDrawList(), font, ImGui.getWindowPosX() + x, (ImGui.getWindowPosY() + centreY) - (AnubisWidgets.lineHeight(font) * 0.5f), AnubisTheme.GUI_TEXT, title, guard);
    }

    public static float mutedParagraph(String text, float x, float y, float wrapX) {
        ImGui.setCursorPos(x, y);
        ImGui.pushTextWrapPos(wrapX);
        ImGui.pushStyleColor(0, ImGui.getStyleColorVec4(1));
        ImGui.textUnformatted(text);
        ImGui.popStyleColor();
        ImGui.popTextWrapPos();
        return ImGui.getCursorPosY();
    }

    public static void cardDivider(float pad, float w, float localY) {
        float wx = ImGui.getWindowPosX();
        float wy = ImGui.getWindowPosY();
        ImGui.getWindowDrawList().addLine(wx + pad, wy + localY, (wx + w) - pad, wy + localY, AnubisWidgets.c(AnubisTheme.GUI_DIVIDER), 1.0f);
    }

    private void renderModuleCard(ClientModule module, float dt, float alpha, float view) {
        renderModuleCard(module, dt, alpha, view, false);
    }

    private void renderModuleCard(ClientModule module, float dt, float alpha, float view, boolean preview) {
        renderModuleCard(module, dt, alpha, view, preview, !preview);
    }

    private void renderModuleCard(ClientModule module, float dt, float alpha, float view, boolean preview, boolean showBack) {
        float[] size;
        float sw = ImGui.getIO().getDisplaySizeX();
        float sh = ImGui.getIO().getDisplaySizeY();
        if (preview) {
            size = beginCard("##preview_card", alpha, view, (sw - AnubisWidgets.s(408.0f)) * 0.5f);
            this.cardRect = new float[]{size[3], size[4], size[0], size[1]};
            ImGui.pushStyleVar(1, 1.0f);
            ImGui.beginDisabled();
        } else {
            ImGui.getBackgroundDrawList().addRectFilled(0.0f, 0.0f, sw, sh, AnubisWidgets.withAlpha(1426063360, view));
            size = beginCard("##module_card", alpha, view);
        }
        float w = size[0];
        float h = size[1];
        float pad = AnubisWidgets.s(24.0f);
        float top = size[2];
        float btn = AnubisWidgets.s(32.0f);
        ImDrawList dl = ImGui.getWindowDrawList();
        float wx = ImGui.getWindowPosX();
        float wy = ImGui.getWindowPosY();
        float titleX = pad;
        if (!preview && showBack) {
            ImGui.setCursorPos(pad - AnubisWidgets.s(4.0f), top);
            if (AnubisWidgets.iconButton("##back", btn)) {
                closeSettings();
            }
            AnubisWidgets.chevronLeft(dl, (((wx + pad) - AnubisWidgets.s(4.0f)) + (btn * 0.5f)) - AnubisWidgets.s(1.0f), wy + top + (btn * 0.5f), AnubisWidgets.s(5.0f), AnubisTheme.GUI_TEXT);
            titleX = pad + btn + AnubisWidgets.s(8.0f);
        }
        drawCardTitle(module.name(), titleX, top + (btn * 0.5f), this.cardGuard);
        if (module.isAction()) {
            float openW = AnubisWidgets.s(72.0f);
            float openH = AnubisWidgets.s(ROW_H);
            ImGui.setCursorPos((w - pad) - openW, top + ((btn - openH) * 0.5f));
            if (AnubisWidgets.chipButton("##open", "Open", openW, openH, true)) {
                module.onClick();
            }
        } else {
            float swW = AnubisWidgets.s(SETTING_ROW_H);
            float swH = AnubisWidgets.s(TAB_BAR_BOTTOM);
            ImGui.setCursorPos((w - pad) - swW, top + ((btn - swH) * 0.5f));
            float swX = ImGui.getCursorScreenPosX();
            float swY = ImGui.getCursorScreenPosY();
            if (ImGui.invisibleButton("##enabled", swW, swH)) {
                module.toggle();
            }
            AnubisWidgets.drawSwitch(dl, swX, swY, swW, swH, AnubisWidgets.animate(ImGui.getItemID(), module.isEnabled(), 14.0f));
        }
        float y = top + btn + AnubisWidgets.s(12.0f);
        String description = module.description();
        if (description != null && !description.isBlank()) {
            y = mutedParagraph(description, pad, y, w - pad) + AnubisWidgets.s(4.0f);
        }
        float y2 = y + AnubisWidgets.s(12.0f);
        cardDivider(pad, w, y2);
        ImGui.setCursorPos(0.0f, y2 + 1.0f);
        if (ImGui.beginChild("##body", w, Math.max(1.0f, ((h - y2) - 1.0f) - AnubisWidgets.s(10.0f)), false, 24)) {
            float contentW = w - (pad * ROW_GAP);
            float labelY = ImGui.getCursorPosY() + AnubisWidgets.s(16.0f);
            sectionLabel("Settings", pad);
            if (!preview && module.hasModifiedSettings()) {
                renderResetAllChip(module, pad, contentW, labelY);
            }
            boolean any = false;
            for (Setting setting : module.settings()) {
                if (setting.isVisible() && (!module.isAction() || setting != module.showInHud())) {
                    any = true;
                    ImGui.pushID(setting.id());
                    renderSetting(module, setting, pad, contentW);
                    ImGui.popID();
                }
            }
            if (!any) {
                mutedParagraph("This module has no settings.", pad, ImGui.getCursorPosY() + AnubisWidgets.s(8.0f), w - pad);
            }
            sectionLabel("Keybind", pad);
            ImGui.pushID("##keybind_row");
            renderKeybindRow(module, pad, contentW);
            ImGui.popID();
            if (isHudModule(module)) {
                renderHudActions(pad, contentW);
            }
            if (module.inConfigs()) {
                sectionLabel("Config", pad);
                renderModuleConfigActions(module, pad, contentW);
            }
            ImGui.dummy(1.0f, AnubisWidgets.s(18.0f));
            applySmoothScroll("card_" + module.id(), dt);
        }
        ImGui.endChild();
        if (preview) {
            ImGui.endDisabled();
            ImGui.popStyleVar();
        }
        endCard();
    }

    private void renderResetAllChip(ClientModule module, float pad, float contentW, float labelY) {
        float afterY = ImGui.getCursorPosY();
        String key = "reset:" + module.id();
        String label = isConfirming(key) ? "Confirm reset" : "Reset all";
        ImFont small = ImGuiManager.getInstance().smallFont();
        boolean pushed = pushFont(small);
        float chipW = AnubisWidgets.textWidth(small, label) + AnubisWidgets.s(18.0f);
        float chipH = AnubisWidgets.lineHeight(small) + AnubisWidgets.s(8.0f);
        ImGui.setCursorPos((pad + contentW) - chipW, labelY + ((ImGui.getTextLineHeight() - chipH) * 0.5f));
        if (AnubisWidgets.chipButton("##reset_all", label, chipW, chipH, isConfirming(key)) && confirmClick(key)) {
            module.resetSettings();
        }
        if (ImGui.isItemHovered(65536)) {
            showTooltip("Restore every setting to its default");
        }
        if (pushed) {
            ImGui.popFont();
        }
        ImGui.setCursorPosY(afterY);
    }

    private boolean confirmClick(String key) {
        if (isConfirming(key)) {
            this.pendingConfirm = null;
            return true;
        }
        this.pendingConfirm = key;
        this.pendingConfirmAt = System.nanoTime();
        return false;
    }

    private boolean isConfirming(String key) {
        return key.equals(this.pendingConfirm) && System.nanoTime() - this.pendingConfirmAt < 3000000000L;
    }

    private void sectionLabel(String text, float pad) {
        ImGui.setCursorPos(pad, ImGui.getCursorPosY() + AnubisWidgets.s(16.0f));
        boolean pushed = pushFont(ImGuiManager.getInstance().smallFont());
        ImGui.pushStyleColor(0, ImGui.getStyleColorVec4(1));
        ImGui.textUnformatted(text);
        ImGui.popStyleColor();
        if (pushed) {
            ImGui.popFont();
        }
        ImGui.setCursorPosY(ImGui.getCursorPosY() + AnubisWidgets.s(4.0f));
        this.dividerPending = false;
    }

    private float rowStart(float pad, float contentW) {
        float localY = ImGui.getCursorPosY();
        if (this.dividerPending) {
            float wx = ImGui.getWindowPosX();
            float sy = ImGui.getCursorScreenPosY();
            ImGui.getWindowDrawList().addLine(wx + pad, sy, wx + pad + contentW, sy, AnubisWidgets.c(AnubisTheme.GUI_DIVIDER), 1.0f);
        }
        this.dividerPending = true;
        return localY;
    }

    public static void rowLabel(String label, float pad, float startY, float rowH) {
        ImGui.setCursorPos(pad, startY + ((rowH - ImGui.getTextLineHeight()) * 0.5f));
        ImGui.textUnformatted(label);
    }

    private void openSettings(ClientModule module) {
        this.settingsModule = module;
        this.viewChangedAt = System.nanoTime();
        this.cardJustOpened = true;
    }

    private void closeSettings() {
        if (this.settingsModule != null) {
            this.viewChangedAt = System.nanoTime();
        }
        this.settingsModule = null;
        this.listeningModule = null;
        this.listeningKeySetting = null;
        this.activeColorModule = null;
        this.activeColorSetting = null;
        this.colorPickerOpen.set(false);
        this.blockLists.close();
        this.itemColorWindowOpen = false;
    }

    private void closeCardOnOutsideClick() {
        if (this.cardJustOpened) {
            this.cardJustOpened = false;
            return;
        }
        if (this.settingsModule != null && this.listeningModule == null && this.listeningKeySetting == null && ImGui.isMouseClicked(0) && !ImGui.isWindowHovered(4) && !ImGui.isAnyItemActive()) {
            closeSettings();
        }
    }

    private void renderFriendsCard(float dt, float alpha, float view) {
        float[] size = beginCard("##friends_card", alpha, view);
        float w = size[0];
        float h = size[1];
        float pad = AnubisWidgets.s(24.0f);
        float top = size[2];
        float btn = AnubisWidgets.s(32.0f);
        FriendStore store = FriendStore.getInstance();
        List<String> friends = store.all();
        drawCardTitle("Friends", pad, top + (btn * 0.5f), this.cardGuard);
        String count = friends.size() == 1 ? "1 player" : friends.size() + " players";
        ImFont small = ImGuiManager.getInstance().smallFont();
        AnubisWidgets.text(ImGui.getWindowDrawList(), small, ((ImGui.getWindowPosX() + w) - pad) - AnubisWidgets.textWidth(small, count), ImGui.getWindowPosY() + top + ((btn - AnubisWidgets.lineHeight(small)) * 0.5f), AnubisTheme.GUI_TEXT_MUTED, count, this.cardGuard);
        float y = mutedParagraph("Add players by their exact username.", pad, top + btn + AnubisWidgets.s(12.0f), w - pad) + AnubisWidgets.s(14.0f);
        float addW = AnubisWidgets.s(84.0f);
        float inputH = ImGui.getTextLineHeight() + AnubisWidgets.s(16.0f);
        ImGui.setCursorPos(pad, y);
        ImGui.pushStyleVar(11, AnubisWidgets.s(12.0f), AnubisWidgets.s(8.0f));
        ImGui.setNextItemWidth(((w - (pad * ROW_GAP)) - addW) - AnubisWidgets.s(8.0f));
        if (this.focusFriendInput) {
            ImGui.setKeyboardFocusHere();
            this.focusFriendInput = false;
        }
        ImGui.inputTextWithHint("##friend_input", "Username", this.friendInput, 16);
        boolean submitted = AnubisWidgets.submittedWithEnter();
        ImGui.popStyleVar();
        ImGui.setCursorPos((w - pad) - addW, y);
        if (AnubisWidgets.chipButton("##friend_add", "Add", addW, inputH, true)) {
            submitted = true;
        }
        if (submitted) {
            tryAddFriend(store);
        }
        float y2 = y + inputH + AnubisWidgets.s(8.0f);
        if (this.friendMessage != null && elapsed(this.friendMessageAt, 2500.0f) < 1.0f) {
            ImGui.setCursorPos(pad, y2);
            boolean pushed = pushFont(small);
            ImGui.pushStyleColor(0, ERROR_TEXT);
            ImGui.textUnformatted(this.friendMessage);
            ImGui.popStyleColor();
            if (pushed) {
                ImGui.popFont();
            }
        }
        float y3 = y2 + AnubisWidgets.lineHeight(small) + AnubisWidgets.s(10.0f);
        cardDivider(pad, w, y3);
        ImGui.setCursorPos(0.0f, y3 + 1.0f);
        if (ImGui.beginChild("##friend_list", w, Math.max(1.0f, ((h - y3) - 1.0f) - AnubisWidgets.s(10.0f)), false, 24)) {
            if (friends.isEmpty()) {
                ImGui.setCursorPos((w - ImGui.calcTextSize("No friends yet").x) * 0.5f, AnubisWidgets.s(40.0f));
                ImGui.textDisabled("No friends yet");
            } else {
                ImGui.dummy(1.0f, AnubisWidgets.s(8.0f));
                for (String name : friends) {
                    renderFriendRow(store, name, pad, w - (pad * ROW_GAP));
                }
            }
            ImGui.dummy(1.0f, AnubisWidgets.s(12.0f));
            applySmoothScroll("friends", dt);
        }
        ImGui.endChild();
        endCard();
    }

    private void renderFriendRow(FriendStore store, String name, float pad, float contentW) {
        ImGui.pushID("friend_" + name);
        float rowH = AnubisWidgets.s(48.0f);
        float localY = ImGui.getCursorPosY();
        ImGui.setCursorPos(pad, localY);
        float x0 = ImGui.getCursorScreenPosX();
        float y0 = ImGui.getCursorScreenPosY();
        ImGui.setNextItemAllowOverlap();
        ImGui.invisibleButton("##row", contentW, rowH);
        boolean rowHovered = ImGui.isMouseHoveringRect(x0, y0, x0 + contentW, y0 + rowH);
        float hover = AnubisWidgets.animate(ImGui.getItemID(), rowHovered, 18.0f);
        ImDrawList dl = ImGui.getWindowDrawList();
        if (hover > 0.01f) {
            dl.addRectFilled(x0 - AnubisWidgets.s(8.0f), y0, x0 + contentW + AnubisWidgets.s(8.0f), y0 + rowH, AnubisWidgets.c(AnubisWidgets.withAlpha(218103807, hover)), AnubisWidgets.s(10.0f));
        }
        float avatarR = AnubisWidgets.s(15.0f);
        float acx = x0 + avatarR;
        float acy = y0 + (rowH * 0.5f);
        int avatarColor = AVATAR_COLORS[Math.floorMod(name.toLowerCase(Locale.ROOT).hashCode(), AVATAR_COLORS.length)];
        dl.addCircleFilled(acx, acy, avatarR, AnubisWidgets.c(avatarColor), 32);
        ImFont category = ImGuiManager.getInstance().categoryFont();
        String initial = name.substring(0, 1).toUpperCase(Locale.ROOT);
        AnubisWidgets.text(dl, category, acx - (AnubisWidgets.textWidth(category, initial) * 0.5f), acy - (AnubisWidgets.lineHeight(category) * 0.5f), -1, initial);
        ImFont font = ImGui.getFont();
        AnubisWidgets.text(dl, font, x0 + (avatarR * ROW_GAP) + AnubisWidgets.s(12.0f), y0 + ((rowH - AnubisWidgets.lineHeight(font)) * 0.5f), AnubisTheme.GUI_TEXT, name);
        float removeSize = AnubisWidgets.s(PANEL_TOP);
        ImGui.setCursorPos((pad + contentW) - removeSize, localY + ((rowH - removeSize) * 0.5f));
        float rx = ImGui.getCursorScreenPosX();
        float ry = ImGui.getCursorScreenPosY();
        if (ImGui.invisibleButton("##remove", removeSize, removeSize)) {
            store.remove(name);
        }
        float removeHover = AnubisWidgets.animate(ImGui.getItemID(), ImGui.isItemHovered(), 18.0f);
        float rcx = rx + (removeSize * 0.5f);
        float rcy = ry + (removeSize * 0.5f);
        if (removeHover > 0.01f) {
            dl.addCircleFilled(rcx, rcy, removeSize * 0.5f, AnubisWidgets.c(AnubisWidgets.withAlpha(ERROR_TEXT, 0.18f * removeHover)), 32);
        }
        AnubisWidgets.cross(dl, rcx, rcy, AnubisWidgets.s(4.5f), AnubisTheme.lerpColor(AnubisWidgets.withAlpha(AnubisTheme.GUI_TEXT_MUTED, 0.25f + (0.75f * hover)), ERROR_TEXT, removeHover));
        ImGui.setCursorPos(0.0f, localY + rowH + AnubisWidgets.s(4.0f));
        ImGui.popID();
    }

    private void tryAddFriend(FriendStore store) {
        String name = this.friendInput.get().trim();
        if (!FriendStore.isValidName(name)) {
            this.friendMessage = "Use 1-16 letters, digits or _ (Bedrock names start with .)";
        } else if (store.isFriend(name)) {
            this.friendMessage = name + " is already on your list";
        } else {
            store.add(name);
            this.friendInput.set("");
            this.friendMessage = null;
        }
        this.friendMessageAt = System.nanoTime();
        this.focusFriendInput = true;
    }

    private void renderConfigCard(float dt, float alpha, float view) {
        float[] size = beginCard("##config_card", alpha, view);
        float w = size[0];
        float h = size[1];
        float pad = AnubisWidgets.s(24.0f);
        float top = size[2];
        float btn = AnubisWidgets.s(32.0f);
        float contentW = w - (pad * ROW_GAP);
        ModuleConfigStore store = ModuleConfigStore.getInstance();
        ConfigProfiles profiles = ConfigProfiles.getInstance();
        ImFont small = ImGuiManager.getInstance().smallFont();
        drawCardTitle("Config", pad, top + (btn * 0.5f), this.cardGuard);
        String status = store.isReadOnly() ? "Read-only" : store.hasUnsavedChanges() ? "Saving..." : "Saved";
        int statusColor = store.isReadOnly() ? ERROR_TEXT : AnubisTheme.GUI_TEXT_MUTED;
        AnubisWidgets.text(ImGui.getWindowDrawList(), small, ((ImGui.getWindowPosX() + w) - pad) - AnubisWidgets.textWidth(small, status), ImGui.getWindowPosY() + top + ((btn - AnubisWidgets.lineHeight(small)) * 0.5f), statusColor, status, this.cardGuard);
        if (store.isReadOnly() && ImGui.isMouseHoveringRect(((ImGui.getWindowPosX() + w) - pad) - AnubisWidgets.s(80.0f), ImGui.getWindowPosY() + top, (ImGui.getWindowPosX() + w) - pad, ImGui.getWindowPosY() + top + btn)) {
            showTooltip("modules.json could not be read. Fix or delete it.");
        }
        float y = mutedParagraph("Changes save automatically. Profiles can be shared with a code.", pad, top + btn + AnubisWidgets.s(12.0f), w - pad) + AnubisWidgets.s(14.0f);
        float gap = AnubisWidgets.s(8.0f);
        float chipH = ImGui.getTextLineHeight() + AnubisWidgets.s(16.0f);
        float chipW = (contentW - (gap * ROW_GAP)) / 3.0f;
        ImGui.setCursorPos(pad, y);
        if (AnubisWidgets.chipButton("##cfg_save", "Save all", chipW, chipH, true)) {
            saveAllConfigs(store);
        }
        if (ImGui.isItemHovered(65536)) {
            showTooltip("Save everything now");
        }
        ImGui.setCursorPos(pad + chipW + gap, y);
        if (AnubisWidgets.chipButton("##cfg_folder", "Open folder", chipW, chipH, false)) {
            openProfilesFolder(profiles);
        }
        ImGui.setCursorPos(pad + ((chipW + gap) * ROW_GAP), y);
        boolean resetArmed = isConfirming("reset-all-modules");
        if (AnubisWidgets.chipButton("##cfg_reset", resetArmed ? "Confirm" : "Reset all", chipW, chipH, resetArmed) && confirmClick("reset-all-modules")) {
            for (ClientModule module : ModuleRegistry.all()) {
                module.resetSettings();
            }
            setConfigMessage("Every module setting is back to default", false);
        }
        if (ImGui.isItemHovered(65536)) {
            showTooltip("Reset all module settings. Keybinds stay.");
        }
        float y2 = y + chipH + AnubisWidgets.s(18.0f);
        cardDivider(pad, w, y2);
        float y3 = y2 + AnubisWidgets.s(14.0f);
        float createW = AnubisWidgets.s(84.0f);
        ImGui.setCursorPos(pad, y3);
        ImGui.pushStyleVar(11, AnubisWidgets.s(12.0f), AnubisWidgets.s(8.0f));
        ImGui.setNextItemWidth((contentW - createW) - gap);
        if (this.focusProfileInput) {
            ImGui.setKeyboardFocusHere();
            this.focusProfileInput = false;
        }
        ImGui.inputTextWithHint("##profile_input", "New profile name", this.profileInput);
        boolean submitted = AnubisWidgets.submittedWithEnter();
        ImGui.popStyleVar();
        ImGui.setCursorPos((w - pad) - createW, y3);
        if (AnubisWidgets.chipButton("##profile_create", "Create", createW, chipH, true)) {
            submitted = true;
        }
        if (ImGui.isItemHovered(65536)) {
            showTooltip("Save your current modules as a new profile");
        }
        if (submitted) {
            tryCreateProfile(profiles);
        }
        float y4 = y3 + chipH + AnubisWidgets.s(8.0f);
        float halfW = (contentW - gap) * 0.5f;
        ImGui.setCursorPos(pad, y4);
        if (AnubisWidgets.chipButton("##profile_import", "Import file", halfW, chipH, false)) {
            importProfileFile();
        }
        if (ImGui.isItemHovered(65536)) {
            showTooltip("Import a profile .json file. You can also drop it here.");
        }
        ImGui.setCursorPos(pad + halfW + gap, y4);
        if (AnubisWidgets.chipButton("##profile_paste", "Paste code", halfW, chipH, false)) {
            pasteProfileCode();
        }
        if (ImGui.isItemHovered(65536)) {
            showTooltip("Load a share code from your clipboard. Your current settings go to Auto backup.");
        }
        float y5 = y4 + chipH + AnubisWidgets.s(8.0f);
        if (this.configMessage != null && elapsed(this.configMessageAt, 2500.0f) < 1.0f) {
            ImGui.setCursorPos(pad, y5);
            boolean pushed = pushFont(small);
            ImGui.pushStyleColor(0, this.configMessageError ? ERROR_TEXT : AnubisTheme.GUI_TEXT_MUTED);
            ImGui.textUnformatted(this.configMessage);
            ImGui.popStyleColor();
            if (pushed) {
                ImGui.popFont();
            }
        }
        float y6 = y5 + AnubisWidgets.lineHeight(small) + AnubisWidgets.s(10.0f);
        cardDivider(pad, w, y6);
        List<ConfigProfiles.Profile> list = profiles.list();
        ImGui.setCursorPos(0.0f, y6 + 1.0f);
        if (ImGui.beginChild("##profile_list", w, Math.max(1.0f, ((h - y6) - 1.0f) - AnubisWidgets.s(10.0f)), false, 24)) {
            if (list.isEmpty()) {
                ImGui.setCursorPos((w - ImGui.calcTextSize("No profiles yet").x) * 0.5f, AnubisWidgets.s(40.0f));
                ImGui.textDisabled("No profiles yet");
            } else {
                ImGui.dummy(1.0f, AnubisWidgets.s(8.0f));
                this.profileList.render(profiles, list, store.activeProfile(), pad, contentW);
            }
            ImGui.dummy(1.0f, AnubisWidgets.s(12.0f));
            float target = this.profileList.takeScrollTarget();
            if (!Float.isNaN(target)) {
                this.scroll.computeIfAbsent("profiles", k -> {
                    return new float[2];
                })[1] = target;
            }
            applySmoothScroll("profiles", dt);
        }
        ImGui.endChild();
        endCard();
    }

    private void tryCreateProfile(ConfigProfiles profiles) {
        try {
            String name = profiles.create(this.profileInput.get());
            this.profileInput.set("");
            this.profileList.open(name);
            setConfigMessage("Created " + name + " from the current modules", false);
        } catch (ProfileException e) {
            setConfigMessage(e.getMessage(), true);
        }
        this.focusProfileInput = true;
    }

    private void importProfileFile() {
        if (FileDialogs.busy()) {
            setConfigMessage("A file window is already open", true);
        } else {
            FileDialogs.openProfile("Import an Anubis profile", this::importProfile);
        }
    }

    private void importProfile(Path path) {
        try {
            String name = ConfigProfiles.getInstance().importFile(path);
            this.profileList.open(name);
            setConfigMessage("Imported " + name + " - press Load to use it", false);
        } catch (ProfileException e) {
            setConfigMessage(e.getMessage(), true);
        }
    }

    private void pasteProfileCode() {
        ConfigProfiles profiles = ConfigProfiles.getInstance();
        try {
            String name = profiles.importCode(class_310.method_1551().field_1774.method_1460());
            ConfigProfiles.Profile imported = profiles.find(name);
            profiles.backup(imported == null ? Set.of(ConfigProfiles.Part.MODULES) : imported.parts());
            profiles.load(name);
            this.profileList.open(name);
            setConfigMessage("Loaded " + name + " - your previous settings are in Auto backup", false);
        } catch (ProfileException e) {
            setConfigMessage(e.getMessage(), true);
        }
    }

    private void saveAllConfigs(ModuleConfigStore store) {
        store.save();
        ThemeStore.get().save();
        HudTheme.get().save();
        HudLayoutStore.getInstance().save();
        FriendStore.getInstance().save();
        if (!store.isReadOnly()) {
            setConfigMessage("Everything saved", false);
        } else {
            setConfigMessage("modules.json is unreadable, so it was left untouched", true);
        }
    }

    private void openProfilesFolder(ConfigProfiles profiles) {
        try {
            Path dir = profiles.directory();
            Files.createDirectories(dir, new FileAttribute[0]);
            class_156.method_668().method_60932(dir);
        } catch (IOException | RuntimeException e) {
            setConfigMessage("Could not open the profiles folder", true);
        }
    }

    private void setConfigMessage(String message, boolean error) {
        this.configMessage = message;
        this.configMessageError = error;
        this.configMessageAt = System.nanoTime();
    }

    private void renderThemeStudio(float dt, float alpha, float view) {
        ThemeStore theme = ThemeStore.get();
        float preview = easeOut(elapsed(this.previewChangedAt, 260.0f));
        this.panelRects.clear();
        this.cardRect = null;
        this.inspectMode = true;
        if (this.themeSurface == theme.cards) {
            ClientModule sample = previewModule();
            if (sample != null) {
                renderModuleCard(sample, dt, alpha * preview, Math.min(view, preview), true);
            }
        } else {
            renderModulePanels(dt, alpha * preview);
        }
        this.inspectMode = false;
        renderInspector(theme, dt, alpha);
        highlightSurfaces(theme);
    }

    private void renderInspector(ThemeStore theme, float dt, float alpha) {
        float ty;
        float sw = ImGui.getIO().getDisplaySizeX();
        float sh = ImGui.getIO().getDisplaySizeY();
        float w = AnubisWidgets.s(INSPECTOR_W);
        float h = Math.min((sh - AnubisWidgets.s(86.0f)) - AnubisWidgets.s(40.0f), AnubisWidgets.s(CARD_MAX_H));
        ImGui.setNextWindowPos((sw - w) - AnubisWidgets.s(24.0f), AnubisWidgets.s(24.0f), 4);
        ImGui.setNextWindowSize(w, h, 1);
        if (this.focusInspector) {
            ImGui.setNextWindowFocus();
            this.focusInspector = false;
        }
        ImGui.pushStyleVar(0, alpha);
        ImGui.pushStyleVar(2, 0.0f, 0.0f);
        ImGui.pushStyleVar(14, 0.0f, 0.0f);
        ImGui.pushStyleVar(3, cardRounding());
        ImGui.pushStyleColor(2, GuiPalette.abgr(theme.cards.color.argbWithAlpha(250)));
        if (ImGui.begin("##theme_inspector", 59)) {
            float x = ImGui.getWindowPosX();
            float y = ImGui.getWindowPosY();
            this.inspectorRect = new float[]{x, y, w, h};
            drawShadow(x, y, w, h, cardRounding(), 6);
            ImGui.getWindowDrawList().addRect(x, y, x + w, y + h, AnubisWidgets.c(318767103), cardRounding(), 0, 1.0f);
            float pad = AnubisWidgets.s(20.0f);
            float top = AnubisWidgets.s(20.0f);
            drawCardTitle(GlyphTint.THEME, pad, top + AnubisWidgets.s(14.0f));
            float ty2 = top + AnubisWidgets.s(34.0f);
            if (this.themeMessage != null && System.nanoTime() - this.themeMessageAt < 3000000000L) {
                ImGui.setCursorPos(pad, ty2);
                ImGui.pushTextWrapPos(w - pad);
                ImGui.pushStyleColor(0, this.themeMessageError ? ERROR_TEXT : AnubisTheme.GUI_TEXT);
                ImGui.textUnformatted(this.themeMessage);
                ImGui.popStyleColor();
                ImGui.popTextWrapPos();
                ty = ImGui.getCursorPosY();
            } else {
                ty = mutedParagraph("Click a panel or card on screen to edit it.", pad, ty2, w - pad);
            }
            float ty3 = renderSurfaceTabs(theme, pad, ty + AnubisWidgets.s(12.0f), w - (pad * ROW_GAP)) + AnubisWidgets.s(14.0f);
            cardDivider(pad, w, ty3);
            ImGui.setCursorPos(0.0f, ty3 + 1.0f);
            if (ImGui.beginChild("##inspector_body", w, Math.max(1.0f, ((h - ty3) - 1.0f) - AnubisWidgets.s(10.0f)), false, 24)) {
                float contentW = w - (pad * ROW_GAP);
                if (this.themeSurface == null) {
                    renderGlobalOptions(theme, pad, contentW);
                } else {
                    renderSurfaceOptions(this.themeSurface, pad, contentW);
                }
                ImGui.dummy(1.0f, AnubisWidgets.s(18.0f));
                applySmoothScroll("inspector", dt);
            }
            ImGui.endChild();
        }
        ImGui.end();
        ImGui.popStyleColor();
        ImGui.popStyleVar(4);
    }

    private float renderSurfaceTabs(ThemeStore theme, float pad, float y, float contentW) {
        String[] labels = {"Global", "Panels", "Cards", "Tab bar"};
        Surface[] targets = {null, theme.panels, theme.cards, theme.tabBar};
        int selected = 0;
        for (int i = 1; i < targets.length; i++) {
            if (targets[i] == this.themeSurface) {
                selected = i;
            }
        }
        float h = AnubisWidgets.s(32.0f);
        ImGui.setCursorPos(pad, y);
        int clicked = AnubisWidgets.segmented("##surface", labels, selected, contentW, h);
        if (clicked >= 0) {
            selectSurface(targets[clicked]);
        }
        return y + h;
    }

    private void renderGlobalOptions(ThemeStore theme, float pad, float contentW) {
        sectionLabel("Presets", pad);
        renderPresetRow(theme, pad, contentW);
        sectionLabel("Colors", pad);
        settingRows(pad, contentW, theme.accent);
        sectionLabel("Layout", pad);
        settingRows(pad, contentW, theme.rounding, theme.backgroundDim, theme.menuBlur);
        sectionLabel("Interface", pad);
        settingRows(pad, contentW, theme.uiScale, theme.animationSpeed, theme.animateLines, theme.customMainMenu, theme.communityPopup);
        sectionLabel("Share", pad);
        renderThemeActions(theme, pad, contentW);
    }

    private void renderSurfaceOptions(Surface surface, float pad, float contentW) {
        sectionLabel("Fill", pad);
        settingRows(pad, contentW, surface.color, surface.opacity);
        sectionLabel("Background", pad);
        settingRows(pad, contentW, surface.background);
        if (surface.isImage()) {
            renderImageGallery(surface, pad, contentW);
        }
        settingRows(pad, contentW, surface.mapping, surface.coverage, surface.headerHeight, surface.imageOpacity, surface.imageScale, surface.imageX, surface.imageY, surface.lineOpacity);
        if (surface.isScreenMapped()) {
            mutedParagraph("Screen mapping pins the background to the screen, so each surface is a window onto it. Drag a panel to see another part.", pad, ImGui.getCursorPosY() + AnubisWidgets.s(12.0f), pad + contentW);
        }
    }

    private void selectSurface(Surface surface) {
        if (surface == this.themeSurface) {
            return;
        }
        ThemeStore theme = ThemeStore.get();
        if ((surface == theme.cards) != (this.themeSurface == theme.cards)) {
            this.previewChangedAt = System.nanoTime();
        }
        this.themeSurface = surface;
        this.focusInspector = true;
    }

    private void highlightSurfaces(ThemeStore theme) {
        boolean zIsMouseClicked;
        float mx = ImGui.getIO().getMousePosX();
        float my = ImGui.getIO().getMousePosY();
        boolean blocked = contains(this.inspectorRect, mx, my) || ImGui.isPopupOpen("", 3072);
        Surface hovered = null;
        if (!blocked) {
            for (float[] rect : this.panelRects) {
                if (contains(rect, mx, my)) {
                    hovered = theme.panels;
                }
            }
            if (contains(this.cardRect, mx, my)) {
                hovered = theme.cards;
            }
            if (contains(this.tabBarRect, mx, my)) {
                hovered = theme.tabBar;
            }
        }
        ImDrawList fg = ImGui.getForegroundDrawList();
        if (this.themeSurface != null) {
            outlineSurface(fg, theme, this.themeSurface, AnubisWidgets.withAlpha(GuiPalette.accent(), 0.8f));
        }
        if (hovered == null) {
            return;
        }
        if (hovered != this.themeSurface) {
            outlineSurface(fg, theme, hovered, -2130706433);
        }
        boolean tabBar = hovered == theme.tabBar;
        String hint = hovered.label + (tabBar ? "  ·  right-click to edit" : "  ·  click to edit");
        ImFont small = ImGuiManager.getInstance().smallFont();
        float chipW = AnubisWidgets.textWidth(small, hint) + AnubisWidgets.s(18.0f);
        float chipH = AnubisWidgets.lineHeight(small) + AnubisWidgets.s(10.0f);
        float cx = Math.min(mx + AnubisWidgets.s(14.0f), (ImGui.getIO().getDisplaySizeX() - chipW) - AnubisWidgets.s(4.0f));
        float cy = Math.min(my + AnubisWidgets.s(18.0f), (ImGui.getIO().getDisplaySizeY() - chipH) - AnubisWidgets.s(4.0f));
        fg.addRectFilled(cx, cy, cx + chipW, cy + chipH, GuiPalette.accent(), chipH * 0.5f);
        AnubisWidgets.text(fg, small, cx + AnubisWidgets.s(9.0f), cy + AnubisWidgets.s(5.0f), -1, hint);
        if (tabBar) {
            zIsMouseClicked = ImGui.isMouseClicked(1);
        } else {
            zIsMouseClicked = ImGui.isMouseClicked(0) || ImGui.isMouseClicked(1);
        }
        boolean clicked = zIsMouseClicked;
        if (clicked) {
            selectSurface(hovered);
        }
    }

    private void outlineSurface(ImDrawList fg, ThemeStore theme, Surface surface, int color) {
        List<float[]> rects;
        float rounding;
        if (surface == theme.panels) {
            rects = this.panelRects;
            rounding = panelRounding();
        } else if (surface == theme.cards) {
            rects = this.cardRect == null ? List.of() : List.of(this.cardRect);
            rounding = cardRounding();
        } else {
            rects = this.tabBarRect == null ? List.of() : List.of(this.tabBarRect);
            rounding = this.tabBarRect == null ? 0.0f : this.tabBarRect[3] * 0.5f;
        }
        float e = AnubisWidgets.s(3.0f);
        for (float[] r : rects) {
            fg.addRect(r[0] - e, r[1] - e, r[0] + r[2] + e, r[1] + r[3] + e, color, rounding + e, 0, AnubisWidgets.s(1.5f));
        }
    }

    private void recordPanelRect() {
        if (this.inspectMode) {
            this.panelRects.add(new float[]{ImGui.getWindowPosX(), ImGui.getWindowPosY(), ImGui.getWindowWidth(), ImGui.getWindowHeight()});
        }
    }

    private static boolean contains(float[] rect, float x, float y) {
        return rect != null && x >= rect[0] && x <= rect[0] + rect[2] && y >= rect[1] && y <= rect[1] + rect[3];
    }

    private static ClientModule previewModule() {
        ClientModule fallback = null;
        for (ClientModule module : ModuleRegistry.all()) {
            if (fallback == null) {
                fallback = module;
            }
            if (module.settings().size() >= 3) {
                return module;
            }
        }
        return fallback;
    }

    private void settingRows(float pad, float contentW, Setting... settings) {
        for (Setting setting : settings) {
            if (setting.isVisible()) {
                ImGui.pushID(setting.id());
                renderSetting(null, setting, pad, contentW);
                ImGui.popID();
            }
        }
    }

    private void renderPresetRow(ThemeStore theme, float pad, float contentW) {
        float rowH = AnubisWidgets.s(SETTING_ROW_H) + AnubisWidgets.s(ROW_INSET);
        float startY = rowStart(pad, contentW);
        float size = AnubisWidgets.s(PANEL_TOP);
        float gap = AnubisWidgets.s(12.0f);
        ImDrawList dl = ImGui.getWindowDrawList();
        for (int i = 0; i < ThemeStore.PRESETS.size(); i++) {
            ThemeStore.Preset preset = ThemeStore.PRESETS.get(i);
            ImGui.setCursorPos(pad + (i * (size + gap)), startY + ((rowH - size) * 0.5f));
            float cx = ImGui.getCursorScreenPosX() + (size * 0.5f);
            float cy = ImGui.getCursorScreenPosY() + (size * 0.5f);
            if (ImGui.invisibleButton("##preset" + i, size, size)) {
                theme.applyPreset(preset);
            }
            boolean hovered = ImGui.isItemHovered();
            float hover = AnubisWidgets.animate(ImGui.getItemID(), hovered, 18.0f);
            boolean selected = theme.matchesPreset(preset);
            dl.addCircleFilled(cx, cy, (size * 0.5f) - AnubisWidgets.s(3.0f), AnubisWidgets.c(GuiPalette.abgr(preset.accent())), 32);
            if (selected) {
                dl.addCircle(cx, cy, size * 0.5f, AnubisWidgets.c(-1), 32, AnubisWidgets.s(1.5f));
            } else if (hover > 0.01f) {
                dl.addCircle(cx, cy, size * 0.5f, AnubisWidgets.c(AnubisWidgets.withAlpha(-1, 0.35f * hover)), 32, AnubisWidgets.s(1.2f));
            }
            if (hovered) {
                showTooltip(preset.name());
            }
        }
        ImGui.setCursorPos(0.0f, startY + rowH);
    }

    private void renderImageGallery(Surface surface, float pad, float contentW) {
        List<String> files = ThemeImages.get().files();
        float gap = AnubisWidgets.s(8.0f);
        float r = AnubisWidgets.s(8.0f);
        float tileW = (contentW - (gap * (3 - 1))) / 3;
        float tileH = tileW * 0.6f;
        float startY = ImGui.getCursorPosY() + AnubisWidgets.s(4.0f);
        ImDrawList dl = ImGui.getWindowDrawList();
        ImFont small = ImGuiManager.getInstance().smallFont();
        String deleted = null;
        int count = files.size() + 1;
        int i = 0;
        while (i < count) {
            ImGui.setCursorPos(pad + ((i % 3) * (tileW + gap)), startY + ((i / 3) * (tileH + gap)));
            float sx = ImGui.getCursorScreenPosX();
            float sy = ImGui.getCursorScreenPosY();
            String name = i == 0 ? null : files.get(i - 1);
            String deleteKey = name == null ? null : "image-delete:" + name;
            boolean armed = deleteKey != null && isConfirming(deleteKey);
            float del = AnubisWidgets.s(20.0f);
            float dx = ((sx + tileW) - del) - AnubisWidgets.s(5.0f);
            float dy = sy + AnubisWidgets.s(5.0f);
            boolean overTile = ImGui.isWindowHovered(128) && ImGui.isMouseHoveringRect(sx, sy, sx + tileW, sy + tileH);
            boolean deleteShown = deleteKey != null && (armed || overTile);
            boolean deleteHovered = false;
            float deleteHover = 0.0f;
            if (deleteShown) {
                ImGui.setCursorScreenPos(dx, dy);
                if (ImGui.invisibleButton("##tiledel" + i, del, del) && confirmClick(deleteKey)) {
                    deleted = name;
                }
                deleteHovered = ImGui.isItemHovered();
                deleteHover = AnubisWidgets.animate(ImGui.getItemID(), deleteHovered, 18.0f);
                armed = isConfirming(deleteKey);
                if (deleteHovered) {
                    showTooltip(armed ? "Click again to delete" : "Delete image");
                }
                ImGui.setCursorScreenPos(sx, sy);
            }
            boolean clicked = ImGui.invisibleButton("##tile" + i, tileW, tileH);
            boolean hovered = ImGui.isItemHovered() || deleteHovered;
            float hover = AnubisWidgets.animate(ImGui.getItemID(), hovered, 18.0f);
            if (i == 0) {
                dl.addRectFilled(sx, sy, sx + tileW, sy + tileH, AnubisWidgets.c(AnubisTheme.lerpColor(AnubisTheme.GUI_SURFACE, AnubisTheme.GUI_SURFACE_HOVER, hover)), r);
                int glyph = AnubisTheme.lerpColor(AnubisTheme.GUI_TEXT_MUTED, AnubisTheme.GUI_TEXT, hover);
                float cx = sx + (tileW * 0.5f);
                float cy = (sy + (tileH * 0.5f)) - AnubisWidgets.s(8.0f);
                dl.addLine(cx - AnubisWidgets.s(7.0f), cy, cx + AnubisWidgets.s(7.0f), cy, AnubisWidgets.c(glyph), AnubisWidgets.s(1.8f));
                dl.addLine(cx, cy - AnubisWidgets.s(7.0f), cx, cy + AnubisWidgets.s(7.0f), AnubisWidgets.c(glyph), AnubisWidgets.s(1.8f));
                AnubisWidgets.text(dl, small, cx - (AnubisWidgets.textWidth(small, "Add image") * 0.5f), cy + AnubisWidgets.s(12.0f), glyph, "Add image");
                if (clicked) {
                    ThemeImages.get().browseAsync();
                }
            } else {
                ThemeImages.Texture texture = ThemeImages.get().preview(name);
                boolean broken = texture == null && ThemeImages.get().isBroken(name);
                dl.addRectFilled(sx, sy, sx + tileW, sy + tileH, AnubisWidgets.c(AnubisTheme.GUI_SURFACE), r);
                if (texture != null) {
                    float[] uv = SurfaceBackground.coverUv(texture.width(), texture.height(), tileW, tileH, 0.5f);
                    dl.addImageRounded(texture.id(), sx, sy, sx + tileW, sy + tileH, uv[0], uv[1], uv[2], uv[3], AnubisWidgets.c(-1), r, 240);
                } else if (broken && !armed) {
                    float cx2 = sx + (tileW * 0.5f);
                    float cy2 = (sy + (tileH * 0.5f)) - AnubisWidgets.s(8.0f);
                    AnubisWidgets.cross(dl, cx2, cy2, AnubisWidgets.s(5.0f), AnubisTheme.GUI_TEXT_MUTED);
                    AnubisWidgets.text(dl, small, cx2 - (AnubisWidgets.textWidth(small, "Can't load") * 0.5f), cy2 + AnubisWidgets.s(10.0f), AnubisTheme.GUI_TEXT_MUTED, "Can't load");
                }
                if (ThemeImages.isGif(name)) {
                    float badgePad = AnubisWidgets.s(4.0f);
                    float badgeH = AnubisWidgets.lineHeight(small) + AnubisWidgets.s(ROW_GAP);
                    float badgeW = AnubisWidgets.textWidth(small, "GIF") + (badgePad * ROW_GAP);
                    float bx = sx + AnubisWidgets.s(5.0f);
                    float by = ((sy + tileH) - badgeH) - AnubisWidgets.s(5.0f);
                    dl.addRectFilled(bx, by, bx + badgeW, by + badgeH, AnubisWidgets.c(AnubisWidgets.withAlpha(-16777216, 0.6f)), AnubisWidgets.s(4.0f));
                    AnubisWidgets.text(dl, small, bx + badgePad, by + AnubisWidgets.s(1.0f), -1, "GIF");
                }
                if (armed) {
                    dl.addRectFilled(sx, sy, sx + tileW, sy + tileH, AnubisWidgets.c(AnubisWidgets.withAlpha(-16777216, 0.6f)), r);
                    AnubisWidgets.text(dl, small, sx + ((tileW - AnubisWidgets.textWidth(small, "Delete?")) * 0.5f), sy + ((tileH - AnubisWidgets.lineHeight(small)) * 0.5f), -1, "Delete?");
                }
                if (name.equals(surface.image.get())) {
                    dl.addRect(sx - AnubisWidgets.s(1.0f), sy - AnubisWidgets.s(1.0f), sx + tileW + AnubisWidgets.s(1.0f), sy + tileH + AnubisWidgets.s(1.0f), AnubisWidgets.c(GuiPalette.accent()), r + AnubisWidgets.s(1.0f), 0, AnubisWidgets.s(ROW_GAP));
                } else if (hover > 0.01f) {
                    dl.addRect(sx, sy, sx + tileW, sy + tileH, AnubisWidgets.c(AnubisWidgets.withAlpha(-1, 0.4f * hover)), r, 0, 1.0f);
                }
                if (deleteShown) {
                    float fade = armed ? 1.0f : hover;
                    float dcx = dx + (del * 0.5f);
                    float dcy = dy + (del * 0.5f);
                    int disc = armed ? ERROR_TEXT : AnubisTheme.lerpColor(AnubisWidgets.withAlpha(-16777216, 0.55f), GuiPalette.accent(), deleteHover);
                    dl.addCircleFilled(dcx, dcy, del * 0.5f, AnubisWidgets.c(AnubisWidgets.withAlpha(disc, fade)), 32);
                    AnubisWidgets.cross(dl, dcx, dcy, AnubisWidgets.s(3.5f), AnubisWidgets.withAlpha(-1, fade));
                }
                if (clicked) {
                    if (armed) {
                        this.pendingConfirm = null;
                    } else {
                        surface.image.set(name);
                    }
                }
                if (!deleteHovered && ImGui.isItemHovered(65536)) {
                    showTooltip(broken ? name + "\nCouldn't load this image" : name);
                }
            }
            i++;
        }
        if (deleted != null) {
            deleteImage(deleted);
        }
        int rows = ((count + 3) - 1) / 3;
        float hintY = ((startY + (rows * (tileH + gap))) - gap) + AnubisWidgets.s(10.0f);
        float hintW = AnubisWidgets.textWidth(small, "Drop an image on the screen or ");
        float linkW = AnubisWidgets.textWidth(small, "open the folder");
        float lineH = AnubisWidgets.lineHeight(small);
        float wx = ImGui.getWindowPosX();
        float wy = ImGui.getWindowPosY() - ImGui.getScrollY();
        AnubisWidgets.text(dl, small, wx + pad, wy + hintY, AnubisTheme.GUI_TEXT_MUTED, "Drop an image on the screen or ");
        ImGui.setCursorPos(pad + hintW, hintY);
        if (ImGui.invisibleButton("##openfolder", linkW, lineH)) {
            ThemeImages.get().openFolder();
        }
        float linkHover = AnubisWidgets.animate(ImGui.getItemID(), ImGui.isItemHovered(), 18.0f);
        AnubisWidgets.text(dl, small, wx + pad + hintW, wy + hintY, AnubisTheme.lerpColor(GuiPalette.accent(), GuiPalette.accentHover(), linkHover), "open the folder");
        ImGui.setCursorPos(0.0f, hintY + lineH + AnubisWidgets.s(8.0f));
    }

    private void renderThemeActions(ThemeStore theme, float pad, float contentW) {
        float gap = AnubisWidgets.s(8.0f);
        float h = AnubisWidgets.s(34.0f);
        float buttonW = (contentW - (gap * ROW_GAP)) / 3.0f;
        float y = ImGui.getCursorPosY() + AnubisWidgets.s(ROW_INSET);
        ImGui.setCursorPos(pad, y);
        if (AnubisWidgets.chipButton("##copytheme", "Copy theme", buttonW, h, false)) {
            class_310.method_1551().field_1774.method_1455(theme.exportString());
            showThemeMessage("Theme copied to the clipboard", false);
        }
        ImGui.setCursorPos(pad + buttonW + gap, y);
        if (AnubisWidgets.chipButton("##pastetheme", "Paste theme", buttonW, h, false)) {
            boolean applied = theme.importString(class_310.method_1551().field_1774.method_1460());
            showThemeMessage(applied ? "Theme applied from the clipboard" : "The clipboard doesn't contain an Anubis theme", !applied);
        }
        ImGui.setCursorPos(pad + ((buttonW + gap) * ROW_GAP), y);
        if (AnubisWidgets.chipButton("##resettheme", "Reset", buttonW, h, false)) {
            theme.resetToDefaults();
            showThemeMessage("Theme reset to the defaults", false);
        }
        ImGui.setCursorPos(0.0f, y + h);
    }

    private void showThemeMessage(String message, boolean error) {
        this.themeMessage = message;
        this.themeMessageError = error;
        this.themeMessageAt = System.nanoTime();
    }

    public void onFilesDropped(List<Path> paths) {
        if (paths.isEmpty()) {
            return;
        }
        if (this.tab == Tab.CONFIG) {
            for (Path path : paths) {
                if (path.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".json")) {
                    importProfile(path);
                    return;
                }
            }
        }
        for (Path path2 : paths) {
            if (ThemeImages.isImage(path2)) {
                if (this.tab == Tab.HUD) {
                    importHudHeaderImage(path2);
                    return;
                } else {
                    switchTab(Tab.THEME);
                    importHeaderImage(path2);
                    return;
                }
            }
        }
        if (this.tab != Tab.HUD) {
            switchTab(Tab.THEME);
            showThemeMessage("Only PNG, JPG and GIF images can be used", true);
        } else {
            showHudMessage("Only PNG, JPG and GIF images can be used", true);
        }
    }

    private void importHudHeaderImage(Path path) {
        String name = ThemeImages.get().importFile(path);
        if (name == null) {
            showHudMessage("Only PNG, JPG and GIF images can be used", true);
            return;
        }
        String targetId = this.hudElementId != null ? this.hudElementId : HudLayoutStore.INFO_BAR_ID;
        Surface target = HudTheme.get().forElementId(targetId);
        if (target == null) {
            return;
        }
        target.image.set(name);
        target.background.set(Surface.BG_IMAGE);
        selectHudElement(targetId);
        showHudMessage(name + " is now the " + target.label.toLowerCase(Locale.ROOT) + " background", false);
    }

    private void importHeaderImage(Path path) {
        String name = ThemeImages.get().importFile(path);
        if (name == null) {
            showThemeMessage("Only PNG, JPG and GIF images can be used", true);
            return;
        }
        ThemeStore theme = ThemeStore.get();
        Surface target = this.themeSurface != null ? this.themeSurface : theme.cards;
        target.image.set(name);
        target.background.set(Surface.BG_IMAGE);
        showThemeMessage(name + " is now the " + target.label.toLowerCase(Locale.ROOT) + " background", false);
    }

    private void deleteImage(String name) {
        boolean deleted = ThemeImages.get().delete(name);
        String message = deleted ? "Deleted " + name : "Couldn't delete " + name;
        if (this.tab != Tab.HUD) {
            showThemeMessage(message, !deleted);
        } else {
            showHudMessage(message, !deleted);
        }
    }

    private static void drawCheck(float x, float y, float progress, int accent) {
        ImDrawList dl = ImGui.getWindowDrawList();
        dl.addRectFilled(x, y, x + 16.0f, y + 16.0f, AnubisTheme.GUI_SURFACE, 4.0f);
        dl.addRect(x + 0.5f, y + 0.5f, (x + 16.0f) - 0.5f, (y + 16.0f) - 0.5f, AnubisTheme.lerpColor(452984831, accent, progress), 4.0f);
        if (progress > 0.01f) {
            dl.addLine(x + 4.0f, y + 8.0f, x + 7.0f, y + 11.0f, accent, ROW_GAP * progress);
            dl.addLine(x + 7.0f, y + 11.0f, x + 12.0f, y + 5.0f, accent, ROW_GAP * progress);
        }
    }

    static float smooth(float from, float to, float dt, float speed) {
        return from + ((to - from) * (1.0f - ((float) Math.exp((-speed) * Math.max(0.0f, dt)))));
    }

    public static float animationDelta(float dt) {
        float speed = GuiPalette.animationSpeed();
        if (speed <= 0.0f) {
            return 1.0f;
        }
        return dt * speed;
    }

    public static float elapsed(long since, float durationMs) {
        float speed = GuiPalette.animationSpeed();
        if (speed <= 0.0f) {
            return 1.0f;
        }
        return Math.min(1.0f, ((System.nanoTime() - since) * speed) / (durationMs * 1000000.0f));
    }

    public static float easeOut(float t) {
        float inv = 1.0f - t;
        return 1.0f - ((inv * inv) * inv);
    }

    private void renderKeybindRow(ClientModule module, float pad, float contentW) {
        String strKeyName;
        float rowH = AnubisWidgets.s(SETTING_ROW_H);
        float startY = rowStart(pad, contentW);
        boolean isListening = this.listeningModule == module;
        if (isListening) {
            if (this.listeningFirstFrame) {
                this.listeningFirstFrame = false;
            } else {
                int pressed = scanPressedKey();
                if (pressed == 256) {
                    module.setKeybind(-1);
                    this.listeningModule = null;
                    isListening = false;
                } else if (pressed != -1) {
                    module.setKeybind(pressed);
                    this.listeningModule = null;
                    isListening = false;
                }
            }
        }
        rowLabel("Key", pad, startY, rowH);
        float chipH = AnubisWidgets.s(ROW_H);
        float keyW = AnubisWidgets.s(96.0f);
        float modeW = AnubisWidgets.s(76.0f);
        float gap = AnubisWidgets.s(8.0f);
        float chipY = startY + ((rowH - chipH) * 0.5f);
        if (!module.isAction()) {
            ImGui.setCursorPos((((pad + contentW) - keyW) - gap) - modeW, chipY);
            if (AnubisWidgets.chipButton("##bindmode", module.isHoldMode() ? "Hold" : "Toggle", modeW, chipH, false)) {
                module.setHoldMode(!module.isHoldMode());
            }
        }
        if (isListening) {
            strKeyName = "Press a key...";
        } else {
            strKeyName = module.keybind() == -1 ? Surface.BG_NONE : keyName(module.keybind());
        }
        String label = strKeyName;
        ImGui.setCursorPos((pad + contentW) - keyW, chipY);
        if (AnubisWidgets.chipButton("##bind", label, keyW, chipH, isListening)) {
            if (isListening) {
                this.listeningModule = null;
            } else {
                this.listeningModule = module;
                this.listeningFirstFrame = true;
                this.listeningKeySetting = null;
                this.listeningKeyFirstFrame = false;
            }
        }
        ImGui.setCursorPos(0.0f, startY + rowH);
    }

    public boolean handleEscape() {
        if (this.listeningKeySetting != null) {
            this.listeningKeySetting.setValue(-1);
            this.listeningKeySetting = null;
            this.listeningKeyFirstFrame = false;
            return true;
        }
        if (this.listeningModule != null) {
            this.listeningModule.setKeybind(-1);
            this.listeningModule = null;
            this.listeningFirstFrame = false;
            return true;
        }
        if (this.blockLists.closePicker()) {
            return true;
        }
        if (this.settingsModule != null) {
            closeSettings();
            return true;
        }
        return false;
    }

    public static int scanPressedKey() {
        long handle = class_310.method_1551().method_22683().method_4490();
        for (int key = 32; key <= 348; key++) {
            if (GLFW.glfwGetKey(handle, key) == 1) {
                return key;
            }
        }
        for (int button = 0; button <= 7; button++) {
            if (GLFW.glfwGetMouseButton(handle, button) == 1) {
                return ModuleKeybinds.MOUSE_OFFSET + button;
            }
        }
        return -1;
    }

    private String keyLabel(int key) {
        String label = (String) this.keyLabels.get(key);
        if (label == null) {
            label = keyName(key);
            this.keyLabels.put(key, label);
        }
        return label;
    }

    public static String keyName(int key) {
        String name;
        if (key >= 1000) {
            int button = key - ModuleKeybinds.MOUSE_OFFSET;
            switch (button) {
                case 0:
                    return "LMB";
                case 1:
                    return "RMB";
                case 2:
                    return "MMB";
                default:
                    return "MB" + (button + 1);
            }
        }
        if (((key >= 39 && key <= 96) || (key >= 161 && key <= 162)) && (name = GLFW.glfwGetKeyName(key, -1)) != null && !name.isBlank()) {
            return name.toUpperCase(Locale.ROOT);
        }
        switch (key) {
            case 32:
                return "SPACE";
            case 256:
                return "ESC";
            case 257:
                return "ENTER";
            case ImPlotAxisFlags.AuxDefault /* 258 */:
                return "TAB";
            case 259:
                return "BKSP";
            case 260:
                return "INS";
            case 261:
                return "DEL";
            case 262:
                return "RIGHT";
            case 263:
                return "LEFT";
            case 264:
                return "DOWN";
            case 265:
                return "UP";
            case 266:
                return "PGUP";
            case 267:
                return "PGDN";
            case 268:
                return "HOME";
            case 269:
                return "END";
            case 290:
                return "F1";
            case 291:
                return "F2";
            case 292:
                return "F3";
            case 293:
                return "F4";
            case 294:
                return "F5";
            case 295:
                return "F6";
            case 296:
                return "F7";
            case 297:
                return "F8";
            case 298:
                return "F9";
            case 299:
                return "F10";
            case 300:
                return "F11";
            case 301:
                return "F12";
            case 335:
                return "NUM ENTER";
            case 340:
                return "L SHIFT";
            case 341:
                return "L CTRL";
            case 342:
                return "L ALT";
            case 344:
                return "R SHIFT";
            case 345:
                return "R CTRL";
            case 346:
                return "R ALT";
            default:
                return "K" + key;
        }
    }

    private void renderModuleConfigActions(ClientModule module, float pad, float contentW) {
        float gap = AnubisWidgets.s(8.0f);
        float h = AnubisWidgets.s(36.0f);
        float buttonW = (contentW - gap) * 0.5f;
        float y = ImGui.getCursorPosY() + AnubisWidgets.s(ROW_INSET);
        ImGui.setCursorPos(pad, y);
        if (AnubisWidgets.chipButton("##copy_config", "Copy Config", buttonW, h, true)) {
            copyModuleConfig(module);
        }
        if (ImGui.isItemHovered(65536)) {
            showTooltip("Copy this module's settings as a short code");
        }
        String key = "paste:" + module.id();
        boolean confirming = isConfirming(key);
        ImGui.setCursorPos(pad + buttonW + gap, y);
        if (AnubisWidgets.chipButton("##paste_config", confirming ? "Confirm paste" : "Paste Config", buttonW, h, confirming) && confirmClick(key)) {
            pasteModuleConfig(module);
        }
        if (ImGui.isItemHovered(65536)) {
            showTooltip("Replace this module's settings with a code from your clipboard");
        }
        float y2 = y + h + AnubisWidgets.s(8.0f);
        ImFont small = ImGuiManager.getInstance().smallFont();
        if (module.id().equals(this.moduleCodeFor) && this.moduleCodeMessage != null && elapsed(this.moduleCodeAt, 2500.0f) < 1.0f) {
            ImGui.setCursorPos(pad, y2);
            boolean pushed = pushFont(small);
            ImGui.pushStyleColor(0, this.moduleCodeError ? ERROR_TEXT : AnubisTheme.GUI_TEXT_MUTED);
            ImGui.textUnformatted(this.moduleCodeMessage);
            ImGui.popStyleColor();
            if (pushed) {
                ImGui.popFont();
            }
        }
        ImGui.setCursorPos(0.0f, y2 + AnubisWidgets.lineHeight(small));
    }

    private void copyModuleConfig(ClientModule module) {
        String code = ModuleCodes.copy(module);
        class_310.method_1551().field_1774.method_1455(code);
        setModuleCodeMessage(module, "Copied, " + code.length() + " characters", false);
    }

    private void pasteModuleConfig(ClientModule module) {
        try {
            ModuleCodes.paste(module, class_310.method_1551().field_1774.method_1460());
            setModuleCodeMessage(module, "Config pasted", false);
        } catch (ProfileException e) {
            setModuleCodeMessage(module, e.getMessage(), true);
        }
    }

    private void setModuleCodeMessage(ClientModule module, String message, boolean error) {
        this.moduleCodeFor = module.id();
        this.moduleCodeMessage = message;
        this.moduleCodeError = error;
        this.moduleCodeAt = System.nanoTime();
    }

    private void renderHudActions(float pad, float contentW) {
        float gap = AnubisWidgets.s(8.0f);
        float h = AnubisWidgets.s(36.0f);
        float buttonW = (contentW - gap) * 0.5f;
        float y = ImGui.getCursorPosY() + AnubisWidgets.s(14.0f);
        ImGui.setCursorPos(pad, y);
        if (AnubisWidgets.chipButton("##editlayout", "Edit Layout", buttonW, h, true)) {
            class_310.method_1551().method_1507(new HudEditorScreen());
        }
        ImGui.setCursorPos(pad + buttonW + gap, y);
        if (AnubisWidgets.chipButton("##resetlayout", "Reset Layout", buttonW, h, false)) {
            HudLayoutStore.getInstance().resetToDefaults(ImGui.getIO().getDisplaySizeX(), ImGui.getIO().getDisplaySizeY());
        }
        ImGui.setCursorPos(0.0f, y + h);
    }

    private static boolean isHudModule(ClientModule module) {
        return module.category() == ModuleCategory.CLIENT && "HUD".equals(module.name());
    }

    private static ClientModule hudModule() {
        for (ClientModule module : ModuleRegistry.all()) {
            if (isHudModule(module)) {
                return module;
            }
        }
        return null;
    }

    private void renderHudStudio(float dt, float alpha, float view) {
        renderHudInspector(dt, alpha);
        highlightHudElements();
        float sw = ImGui.getIO().getDisplaySizeX();
        float sh = ImGui.getIO().getDisplaySizeY();
        HudRenderer.getInstance().studioInput(sw, sh, hudStudioBlocked());
    }

    private static boolean hudStudioBlocked() {
        return ImGui.isWindowHovered(4) || ImGui.isAnyItemActive() || ImGui.isPopupOpen("", 3072);
    }

    private void renderHudInspector(float dt, float alpha) {
        float ty;
        float sw = ImGui.getIO().getDisplaySizeX();
        float sh = ImGui.getIO().getDisplaySizeY();
        float w = AnubisWidgets.s(INSPECTOR_W);
        float h = Math.min((sh - AnubisWidgets.s(86.0f)) - AnubisWidgets.s(40.0f), AnubisWidgets.s(CARD_MAX_H));
        ImGui.setNextWindowPos((sw - w) - AnubisWidgets.s(24.0f), AnubisWidgets.s(24.0f), 4);
        ImGui.setNextWindowSize(w, h, 1);
        if (this.focusHudInspector) {
            ImGui.setNextWindowFocus();
            this.focusHudInspector = false;
        }
        ImGui.pushStyleVar(0, alpha);
        ImGui.pushStyleVar(2, 0.0f, 0.0f);
        ImGui.pushStyleVar(14, 0.0f, 0.0f);
        ImGui.pushStyleVar(3, cardRounding());
        ImGui.pushStyleColor(2, GuiPalette.abgr(ThemeStore.get().cards.color.argbWithAlpha(250)));
        if (ImGui.begin("##hud_inspector", 59)) {
            float x = ImGui.getWindowPosX();
            float y = ImGui.getWindowPosY();
            drawShadow(x, y, w, h, cardRounding(), 6);
            ImGui.getWindowDrawList().addRect(x, y, x + w, y + h, AnubisWidgets.c(318767103), cardRounding(), 0, 1.0f);
            float pad = AnubisWidgets.s(20.0f);
            float top = AnubisWidgets.s(20.0f);
            drawCardTitle("HUD", pad, top + AnubisWidgets.s(14.0f));
            float ty2 = top + AnubisWidgets.s(34.0f);
            if (this.hudMessage != null && System.nanoTime() - this.hudMessageAt < 3000000000L) {
                ImGui.setCursorPos(pad, ty2);
                ImGui.pushTextWrapPos(w - pad);
                ImGui.pushStyleColor(0, this.hudMessageError ? ERROR_TEXT : AnubisTheme.GUI_TEXT);
                ImGui.textUnformatted(this.hudMessage);
                ImGui.popStyleColor();
                ImGui.popTextWrapPos();
                ty = ImGui.getCursorPosY();
            } else {
                ty = mutedParagraph("Click an element on screen to edit it.", pad, ty2, w - pad);
            }
            float ty3 = renderHudElementTabs(pad, ty + AnubisWidgets.s(12.0f), w - (pad * ROW_GAP), dt) + AnubisWidgets.s(14.0f);
            cardDivider(pad, w, ty3);
            ImGui.setCursorPos(0.0f, ty3 + 1.0f);
            if (ImGui.beginChild("##hud_inspector_body", w, Math.max(1.0f, ((h - ty3) - 1.0f) - AnubisWidgets.s(10.0f)), false, 24)) {
                float contentW = w - (pad * ROW_GAP);
                this.dividerPending = false;
                if (this.hudElementId == null) {
                    renderHudGeneralOptions(pad, contentW);
                } else {
                    renderHudElementOptions(this.hudElementId, pad, contentW);
                }
                ImGui.dummy(1.0f, AnubisWidgets.s(18.0f));
                applySmoothScroll("hud_inspector", dt);
            }
            ImGui.endChild();
        }
        ImGui.end();
        ImGui.popStyleColor();
        ImGui.popStyleVar(4);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private float renderHudElementTabs(float pad, float y, float contentW, float dt) {
        List<C1Item> items = List.of((Object[]) new C1Item[]{new Record(null, "General") { // from class: net.anubis.client.gui.ClickGuiRenderer.1Item
            private final String id;
            private final String label;

            {
                this.id = id;
                this.label = label;
            }

            @Override // java.lang.Record
            public final String toString() {
                return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final int hashCode() {
                return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final boolean equals(Object o) {
                return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, C1Item.class, Object.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
            }

            public String id() {
                return this.id;
            }

            public String label() {
                return this.label;
            }
        }, new Record(HudLayoutStore.INFO_BAR_ID, HudTheme.get().infoBar.label) { // from class: net.anubis.client.gui.ClickGuiRenderer.1Item
            private final String id;
            private final String label;

            {
                this.id = id;
                this.label = label;
            }

            @Override // java.lang.Record
            public final String toString() {
                return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final int hashCode() {
                return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final boolean equals(Object o) {
                return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, C1Item.class, Object.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
            }

            public String id() {
                return this.id;
            }

            public String label() {
                return this.label;
            }
        }, new Record(HudLayoutStore.COORDINATES_ID, HudTheme.get().coordinates.label) { // from class: net.anubis.client.gui.ClickGuiRenderer.1Item
            private final String id;
            private final String label;

            {
                this.id = id;
                this.label = label;
            }

            @Override // java.lang.Record
            public final String toString() {
                return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final int hashCode() {
                return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final boolean equals(Object o) {
                return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, C1Item.class, Object.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
            }

            public String id() {
                return this.id;
            }

            public String label() {
                return this.label;
            }
        }, new Record(HudLayoutStore.MODULE_LIST_ID, HudTheme.get().moduleList.label) { // from class: net.anubis.client.gui.ClickGuiRenderer.1Item
            private final String id;
            private final String label;

            {
                this.id = id;
                this.label = label;
            }

            @Override // java.lang.Record
            public final String toString() {
                return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final int hashCode() {
                return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final boolean equals(Object o) {
                return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, C1Item.class, Object.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
            }

            public String id() {
                return this.id;
            }

            public String label() {
                return this.label;
            }
        }, new Record(HudLayoutStore.POTION_TRACKER_ID, HudTheme.get().potions.label) { // from class: net.anubis.client.gui.ClickGuiRenderer.1Item
            private final String id;
            private final String label;

            {
                this.id = id;
                this.label = label;
            }

            @Override // java.lang.Record
            public final String toString() {
                return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final int hashCode() {
                return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final boolean equals(Object o) {
                return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, C1Item.class, Object.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
            }

            public String id() {
                return this.id;
            }

            public String label() {
                return this.label;
            }
        }, new Record(HudLayoutStore.REGION_MAP_ID, HudTheme.get().regionMap.label) { // from class: net.anubis.client.gui.ClickGuiRenderer.1Item
            private final String id;
            private final String label;

            {
                this.id = id;
                this.label = label;
            }

            @Override // java.lang.Record
            public final String toString() {
                return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final int hashCode() {
                return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final boolean equals(Object o) {
                return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, C1Item.class, Object.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
            }

            public String id() {
                return this.id;
            }

            public String label() {
                return this.label;
            }
        }, new Record(HudLayoutStore.ANTI_VANISH_ID, HudTheme.get().antiVanish.label) { // from class: net.anubis.client.gui.ClickGuiRenderer.1Item
            private final String id;
            private final String label;

            {
                this.id = id;
                this.label = label;
            }

            @Override // java.lang.Record
            public final String toString() {
                return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final int hashCode() {
                return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final boolean equals(Object o) {
                return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, C1Item.class, Object.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
            }

            public String id() {
                return this.id;
            }

            public String label() {
                return this.label;
            }
        }, new Record(HudLayoutStore.STAFF_LIST_ID, HudTheme.get().staffList.label) { // from class: net.anubis.client.gui.ClickGuiRenderer.1Item
            private final String id;
            private final String label;

            {
                this.id = id;
                this.label = label;
            }

            @Override // java.lang.Record
            public final String toString() {
                return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final int hashCode() {
                return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final boolean equals(Object o) {
                return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, C1Item.class, Object.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
            }

            public String id() {
                return this.id;
            }

            public String label() {
                return this.label;
            }
        }, new Record(HudLayoutStore.SPOTIFY_ID, HudTheme.get().spotify.label) { // from class: net.anubis.client.gui.ClickGuiRenderer.1Item
            private final String id;
            private final String label;

            {
                this.id = id;
                this.label = label;
            }

            @Override // java.lang.Record
            public final String toString() {
                return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final int hashCode() {
                return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final boolean equals(Object o) {
                return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, C1Item.class, Object.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
            }

            public String id() {
                return this.id;
            }

            public String label() {
                return this.label;
            }
        }, new Record(HudLayoutStore.HOTKEYS_ID, HudTheme.get().hotkeys.label) { // from class: net.anubis.client.gui.ClickGuiRenderer.1Item
            private final String id;
            private final String label;

            {
                this.id = id;
                this.label = label;
            }

            @Override // java.lang.Record
            public final String toString() {
                return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final int hashCode() {
                return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final boolean equals(Object o) {
                return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, C1Item.class, Object.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
            }

            public String id() {
                return this.id;
            }

            public String label() {
                return this.label;
            }
        }, new Record(HudLayoutStore.RADAR_ID, HudRenderer.editorLabel(HudLayoutStore.RADAR_ID)) { // from class: net.anubis.client.gui.ClickGuiRenderer.1Item
            private final String id;
            private final String label;

            {
                this.id = id;
                this.label = label;
            }

            @Override // java.lang.Record
            public final String toString() {
                return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final int hashCode() {
                return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final boolean equals(Object o) {
                return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, C1Item.class, Object.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
            }

            public String id() {
                return this.id;
            }

            public String label() {
                return this.label;
            }
        }, new Record(HudLayoutStore.TARGET_CARD_ID, HudTheme.get().targetCard.label) { // from class: net.anubis.client.gui.ClickGuiRenderer.1Item
            private final String id;
            private final String label;

            {
                this.id = id;
                this.label = label;
            }

            @Override // java.lang.Record
            public final String toString() {
                return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final int hashCode() {
                return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final boolean equals(Object o) {
                return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, C1Item.class, Object.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
            }

            public String id() {
                return this.id;
            }

            public String label() {
                return this.label;
            }
        }, new Record(HudLayoutStore.ARMOR_STATUS_ID, HudTheme.get().armorStatus.label) { // from class: net.anubis.client.gui.ClickGuiRenderer.1Item
            private final String id;
            private final String label;

            {
                this.id = id;
                this.label = label;
            }

            @Override // java.lang.Record
            public final String toString() {
                return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final int hashCode() {
                return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final boolean equals(Object o) {
                return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, C1Item.class, Object.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
            }

            public String id() {
                return this.id;
            }

            public String label() {
                return this.label;
            }
        }, new Record(HudLayoutStore.ITEM_COOLDOWNS_ID, HudTheme.get().itemCooldowns.label) { // from class: net.anubis.client.gui.ClickGuiRenderer.1Item
            private final String id;
            private final String label;

            {
                this.id = id;
                this.label = label;
            }

            @Override // java.lang.Record
            public final String toString() {
                return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final int hashCode() {
                return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final boolean equals(Object o) {
                return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, C1Item.class, Object.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
            }

            public String id() {
                return this.id;
            }

            public String label() {
                return this.label;
            }
        }, new Record(ToggleToasts.ID, HudTheme.get().toasts.label) { // from class: net.anubis.client.gui.ClickGuiRenderer.1Item
            private final String id;
            private final String label;

            {
                this.id = id;
                this.label = label;
            }

            @Override // java.lang.Record
            public final String toString() {
                return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final int hashCode() {
                return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, C1Item.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
            }

            @Override // java.lang.Record
            public final boolean equals(Object o) {
                return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, C1Item.class, Object.class), C1Item.class, "id;label", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->id:Ljava/lang/String;", "FIELD:Lnet/anubis/client/gui/ClickGuiRenderer$1Item;->label:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
            }

            public String id() {
                return this.id;
            }

            public String label() {
                return this.label;
            }
        }});
        ImFont small = ImGuiManager.getInstance().smallFont();
        boolean pushed = pushFont(small);
        float chipH = AnubisWidgets.s(24.0f);
        float gap = AnubisWidgets.s(ROW_INSET);
        float rowGap = AnubisWidgets.s(ROW_INSET);
        float curX = pad;
        float curY = y;
        try {
            for (C1Item item : items) {
                boolean selected = Objects.equals(item.id(), this.hudElementId);
                float chipW = AnubisWidgets.textWidth(small, item.label()) + AnubisWidgets.s(18.0f);
                if (curX + chipW > pad + contentW && curX > pad) {
                    curX = pad;
                    curY += chipH + rowGap;
                }
                ImGui.setCursorPos(curX, curY);
                if (AnubisWidgets.chipButton("##hudel_" + (item.id() == null ? "general" : item.id()), item.label(), chipW, chipH, selected)) {
                    selectHudElement(item.id());
                }
                curX += chipW + gap;
            }
            return curY + chipH;
        } finally {
            if (pushed) {
                ImGui.popFont();
            }
        }
    }

    private void renderHudGeneralOptions(float pad, float contentW) {
        ClientModule hud = hudModule();
        if (hud != null) {
            sectionLabel("Settings", pad);
            settingRows(pad, contentW, (Setting[]) hud.settings().toArray(new Setting[0]));
        }
        sectionLabel("Layout", pad);
        settingRows(pad, contentW, HudTheme.get().rounding);
        float y = ImGui.getCursorPosY() + AnubisWidgets.s(ROW_INSET);
        float h = AnubisWidgets.s(34.0f);
        ImGui.setCursorPos(pad, y);
        if (AnubisWidgets.chipButton("##resethudlayout", "Reset positions & sizes", contentW, h, false)) {
            HudLayoutStore.getInstance().resetToDefaults(ImGui.getIO().getDisplaySizeX(), ImGui.getIO().getDisplaySizeY());
            showHudMessage("HUD layout reset", false);
        }
        ImGui.setCursorPos(0.0f, y + h);
        sectionLabel("Share", pad);
        renderHudThemeActions(pad, contentW);
    }

    private void renderHudElementOptions(String id, float pad, float contentW) {
        ClientModule clientModule;
        ClientModule regionMap;
        ClientModule clientModuleHudModule = hudModule();
        if (clientModuleHudModule instanceof HudModule) {
            HudModule hud = (HudModule) clientModuleHudModule;
            List<Setting> own = hud.elementSettings(id);
            if (!own.isEmpty()) {
                sectionLabel(hudOptionsTitle(id), pad);
                settingRows(pad, contentW, (Setting[]) own.toArray(new Setting[0]));
            }
        }
        if (HudLayoutStore.REGION_MAP_ID.equals(id) && (regionMap = ModuleRegistry.get(RegionMapModule.class)) != null) {
            renderInlineModuleToggle(regionMap, pad, contentW);
            sectionLabel("Settings", pad);
            settingRows(pad, contentW, (Setting[]) regionMap.settings().toArray(new Setting[0]));
        }
        if (HudLayoutStore.RADAR_ID.equals(id)) {
            clientModule = ModuleRegistry.get(RadarModule.class);
        } else {
            clientModule = HudLayoutStore.HOTKEYS_ID.equals(id) ? ModuleRegistry.get(HotkeysModule.class) : null;
        }
        ClientModule owner = clientModule;
        if (owner != null) {
            renderInlineModuleToggle(owner, pad, contentW);
            if (!owner.settings().isEmpty()) {
                sectionLabel("Settings", pad);
                settingRows(pad, contentW, (Setting[]) owner.settings().toArray(new Setting[0]));
            }
        }
        Surface surface = HudTheme.get().forElementId(id);
        if (surface != null) {
            renderSurfaceOptions(surface, pad, contentW);
        }
    }

    private static String hudOptionsTitle(String id) {
        switch (id) {
            case "toggle_toasts":
                return "Style";
            case "target_card":
            case "armor_status":
            case "item_cooldowns":
                return "Options";
            default:
                return "Show";
        }
    }

    private void renderInlineModuleToggle(ClientModule module, float pad, float contentW) {
        float rowH = AnubisWidgets.s(SETTING_ROW_H);
        float startY = rowStart(pad, contentW);
        rowLabel("Active", pad, startY, rowH);
        float swW = AnubisWidgets.s(40.0f);
        float swH = AnubisWidgets.s(22.0f);
        ImGui.setCursorPos((pad + contentW) - swW, startY + ((rowH - swH) * 0.5f));
        float sx = ImGui.getCursorScreenPosX();
        float sy = ImGui.getCursorScreenPosY();
        if (ImGui.invisibleButton("##active", swW, swH)) {
            module.toggle();
        }
        AnubisWidgets.drawSwitch(ImGui.getWindowDrawList(), sx, sy, swW, swH, AnubisWidgets.animate(ImGui.getItemID(), module.isEnabled(), 16.0f));
        ImGui.setCursorPos(0.0f, startY + rowH);
    }

    private void renderHudThemeActions(float pad, float contentW) {
        HudTheme theme = HudTheme.get();
        float gap = AnubisWidgets.s(8.0f);
        float h = AnubisWidgets.s(34.0f);
        float buttonW = (contentW - (gap * ROW_GAP)) / 3.0f;
        float y = ImGui.getCursorPosY() + AnubisWidgets.s(ROW_INSET);
        ImGui.setCursorPos(pad, y);
        if (AnubisWidgets.chipButton("##copyhudtheme", "Copy theme", buttonW, h, false)) {
            class_310.method_1551().field_1774.method_1455(theme.exportString());
            showHudMessage("HUD theme copied to the clipboard", false);
        }
        ImGui.setCursorPos(pad + buttonW + gap, y);
        if (AnubisWidgets.chipButton("##pastehudtheme", "Paste theme", buttonW, h, false)) {
            boolean applied = theme.importString(class_310.method_1551().field_1774.method_1460());
            showHudMessage(applied ? "HUD theme applied from the clipboard" : "The clipboard doesn't contain an Anubis HUD theme", !applied);
        }
        ImGui.setCursorPos(pad + ((buttonW + gap) * ROW_GAP), y);
        if (AnubisWidgets.chipButton("##resethudtheme", "Reset", buttonW, h, false)) {
            theme.resetToDefaults();
            showHudMessage("HUD theme reset to the defaults", false);
        }
        ImGui.setCursorPos(0.0f, y + h);
    }

    private void showHudMessage(String message, boolean error) {
        this.hudMessage = message;
        this.hudMessageError = error;
        this.hudMessageAt = System.nanoTime();
    }

    private void selectHudElement(String id) {
        if (Objects.equals(id, this.hudElementId)) {
            return;
        }
        this.hudElementId = id;
        this.focusHudInspector = true;
    }

    private void highlightHudElements() {
        HudLayoutStore.HudElementState selected;
        float mx = ImGui.getIO().getMousePosX();
        float my = ImGui.getIO().getMousePosY();
        boolean dragging = HudRenderer.getInstance().isDragging();
        HudLayoutStore.HudElementState hovered = null;
        if (!dragging && !hudStudioBlocked()) {
            for (HudLayoutStore.HudElementState state : HudLayoutStore.getInstance().orderedElements()) {
                if (state.positioned && contains(new float[]{state.x, state.y, state.width, state.height}, mx, my)) {
                    hovered = state;
                }
            }
        }
        ImDrawList fg = ImGui.getForegroundDrawList();
        if (this.hudElementId != null && (selected = findHudElementState(this.hudElementId)) != null) {
            outlineHudElement(fg, selected, AnubisWidgets.withAlpha(GuiPalette.accent(), 0.8f));
        }
        if (hovered == null) {
            return;
        }
        if (!hovered.id.equals(this.hudElementId)) {
            outlineHudElement(fg, hovered, -2130706433);
        }
        Surface surface = HudTheme.get().forElementId(hovered.id);
        String label = surface != null ? surface.label : HudRenderer.editorLabel(hovered.id);
        String hint = label + "  ·  click to edit, drag to move, scroll to resize";
        ImFont small = ImGuiManager.getInstance().smallFont();
        float chipW = AnubisWidgets.textWidth(small, hint) + AnubisWidgets.s(18.0f);
        float chipH = AnubisWidgets.lineHeight(small) + AnubisWidgets.s(10.0f);
        float cx = Math.min(mx + AnubisWidgets.s(14.0f), (ImGui.getIO().getDisplaySizeX() - chipW) - AnubisWidgets.s(4.0f));
        float cy = Math.min(my + AnubisWidgets.s(18.0f), (ImGui.getIO().getDisplaySizeY() - chipH) - AnubisWidgets.s(4.0f));
        fg.addRectFilled(cx, cy, cx + chipW, cy + chipH, GuiPalette.accent(), chipH * 0.5f);
        AnubisWidgets.text(fg, small, cx + AnubisWidgets.s(9.0f), cy + AnubisWidgets.s(5.0f), -1, hint);
        if (ImGui.isMouseClicked(0) || ImGui.isMouseClicked(1)) {
            selectHudElement(hovered.id);
        }
    }

    private void outlineHudElement(ImDrawList fg, HudLayoutStore.HudElementState state, int color) {
        float e = AnubisWidgets.s(3.0f);
        float rounding = HudRenderer.hudRounding() * HudRenderer.scaleOf(state);
        fg.addRect(state.x - e, state.y - e, state.x + state.width + e, state.y + state.height + e, color, rounding + e, 0, AnubisWidgets.s(1.5f));
    }

    private static HudLayoutStore.HudElementState findHudElementState(String id) {
        for (HudLayoutStore.HudElementState state : HudLayoutStore.getInstance().orderedElements()) {
            if (state.id.equals(id)) {
                return state;
            }
        }
        return null;
    }

    private void openColorPicker(ClientModule module, ColorSetting setting) {
        this.activeColorModule = module;
        this.activeColorSetting = setting;
        this.colorPickerOpen.set(true);
    }

    private void renderColorPickerWindow() {
        if (this.activeColorSetting == null) {
            return;
        }
        if (!this.colorPickerOpen.get()) {
            this.activeColorSetting = null;
            this.activeColorModule = null;
            return;
        }
        ImGui.setNextWindowSize(270.0f, 330.0f, 4);
        ImGui.setNextWindowPos(260.0f, 90.0f, 4);
        String owner = this.activeColorModule != null ? this.activeColorModule.name() + " / " : "";
        if (ImGui.begin("Color - " + owner + this.activeColorSetting.name() + "##color_picker", this.colorPickerOpen, 352)) {
            if (ImGui.colorPicker4("##picker", this.activeColorSetting.value(), 135593984)) {
                this.activeColorSetting.notifyChanged();
            }
            ImGui.setNextItemWidth(240.0f);
            if (ImGui.colorEdit4("RGBA", this.activeColorSetting.value(), 135593984)) {
                this.activeColorSetting.notifyChanged();
            }
            if (ImGui.button("Close", 80.0f, 22.0f)) {
                this.activeColorSetting.notifyChanged();
                this.colorPickerOpen.set(false);
            }
        }
        ImGui.end();
        if (!this.colorPickerOpen.get()) {
            this.activeColorSetting = null;
            this.activeColorModule = null;
        }
    }

    private void renderItemColorPaletteWindow() {
        if (!this.itemColorWindowOpen || this.activeItemColorPalette == null) {
            return;
        }
        float dt = Math.max(ImGui.getIO().getDeltaTime(), 0.001f);
        ImGui.setNextWindowSize(340.0f, 500.0f, 4);
        ImGui.setNextWindowPos(340.0f, 100.0f, 4);
        boolean open = ImGui.begin("Item Colors##icpalette", 288);
        if (!open) {
            ImGui.end();
            if (this.activeItemColorPalette != null) {
                this.activeItemColorPalette.notifyChanged();
            }
            this.itemColorWindowOpen = false;
            return;
        }
        ImGui.text("Default:");
        ImGui.sameLine();
        if (ImGui.colorButton("##icdefcol", this.activeItemColorPalette.defaultColor(), 131648, 42.0f, 16.0f)) {
            this.editingItemDefaultColor = true;
            this.activeItemColorEntry = null;
            this.itemColorPickerOpen.set(true);
        }
        ImGui.separator();
        ImGui.text("Search:");
        ImGui.sameLine();
        ImGui.setNextItemWidth(200.0f);
        ImGui.inputText("##icsearch", this.itemSearchBuf);
        String search = this.itemSearchBuf.get().toLowerCase().trim();
        ImGui.separator();
        ImGui.beginChild("##iclist", 0.0f, INSPECTOR_W, false, 0);
        for (ItemColorListSetting.ItemEntry entry : this.activeItemColorPalette.entries()) {
            boolean matches = search.isEmpty() || entry.displayName().toLowerCase().contains(search) || entry.id().toLowerCase().contains(search);
            if (matches) {
                float rowY = ImGui.getCursorPosY();
                float availW = ImGui.getContentRegionAvailX();
                ImGui.setCursorPos(4.0f, rowY + ((22.0f - 16.0f) * 0.5f));
                ImGui.pushID(entry.id() + "_ictog");
                renderItemEntryToggle(this.activeItemColorPalette, entry, dt);
                ImGui.popID();
                float textH2 = ImGui.getTextLineHeight();
                ImGui.setCursorPos(24.0f, rowY + ((22.0f - textH2) * 0.5f));
                ImGui.textUnformatted(entry.displayName());
                float swatchX = availW - SETTING_ROW_H;
                if (swatchX < 96.0f) {
                    swatchX = 96.0f;
                }
                ImGui.setCursorPos(swatchX, rowY + ((22.0f - 16.0f) * 0.5f));
                ImGui.pushID(entry.id() + "_iccol");
                if (ImGui.colorButton("##ic", entry.color(), 131648, 42.0f, 16.0f)) {
                    this.activeItemColorEntry = entry;
                    this.editingItemDefaultColor = false;
                    this.itemColorPickerOpen.set(true);
                }
                ImGui.popID();
                ImGui.setCursorPosY(rowY + 22.0f + ROW_GAP);
            }
        }
        ImGui.endChild();
        ImGui.separator();
        if (ImGui.button("Close##icclose", 80.0f, 22.0f)) {
            this.activeItemColorPalette.notifyChanged();
            this.itemColorWindowOpen = false;
            this.activeItemColorPalette = null;
        }
        ImGui.end();
        renderItemColorPicker();
    }

    private void renderItemColorPicker() {
        float[] target;
        String label;
        if (!this.itemColorPickerOpen.get()) {
            this.activeItemColorEntry = null;
            this.editingItemDefaultColor = false;
            return;
        }
        if (this.activeItemColorEntry == null && !this.editingItemDefaultColor) {
            this.itemColorPickerOpen.set(false);
            return;
        }
        if (this.editingItemDefaultColor && this.activeItemColorPalette != null) {
            target = this.activeItemColorPalette.defaultColor();
            label = "Default Color";
        } else {
            target = this.activeItemColorEntry.color();
            label = this.activeItemColorEntry.displayName();
        }
        ImGui.setNextWindowSize(270.0f, 330.0f, 4);
        ImGui.setNextWindowPos(700.0f, 100.0f, 4);
        if (ImGui.begin("Item Color - " + label, this.itemColorPickerOpen, 352)) {
            ImGui.colorPicker4("##icpicker", target, 135593984);
            ImGui.setNextItemWidth(240.0f);
            if (ImGui.colorEdit4("RGBA##icce", target, 135593984) && this.activeItemColorPalette != null) {
                this.activeItemColorPalette.notifyChanged();
            }
            if (ImGui.button("Close##iccp", 80.0f, 22.0f)) {
                if (this.activeItemColorPalette != null) {
                    this.activeItemColorPalette.notifyChanged();
                }
                this.itemColorPickerOpen.set(false);
                this.activeItemColorEntry = null;
                this.editingItemDefaultColor = false;
            }
        }
        ImGui.end();
        if (!this.itemColorPickerOpen.get()) {
            this.activeItemColorEntry = null;
            this.editingItemDefaultColor = false;
        }
    }

    private void renderItemEntryToggle(ItemColorListSetting palette, ItemColorListSetting.ItemEntry entry, float dt) {
        float sx = ImGui.getCursorScreenPosX();
        float sy = ImGui.getCursorScreenPosY();
        if (ImGui.invisibleButton("##ietog", 16.0f, 16.0f)) {
            entry.enabled().set(!entry.enabled().get());
            palette.notifyChanged();
        }
        float target = entry.enabled().get() ? 1.0f : 0.0f;
        float current = smooth(this.itemEntryTogAnim.getOrDefault(entry, Float.valueOf(target)).floatValue(), target, dt, 14.0f);
        this.itemEntryTogAnim.put(entry, Float.valueOf(current));
        drawCheck(sx, sy, current, GuiPalette.accent());
    }

    private void renderSetting(ClientModule module, Setting setting, float pad, float contentW) {
        float rowH = AnubisWidgets.s(SETTING_ROW_H);
        float startY = rowStart(pad, contentW);
        ImDrawList dl = ImGui.getWindowDrawList();
        if (setting instanceof BooleanSetting) {
            BooleanSetting bs = (BooleanSetting) setting;
            ImGui.setCursorPos(pad, startY);
            float sx = ImGui.getCursorScreenPosX();
            float sy = ImGui.getCursorScreenPosY();
            if (ImGui.invisibleButton("##row", contentW, rowH)) {
                bs.set(!bs.get());
            }
            float hover = AnubisWidgets.animate(ImGui.getItemID(), ImGui.isItemHovered(), 18.0f);
            if (hover > 0.01f) {
                dl.addRectFilled(sx - AnubisWidgets.s(10.0f), sy + AnubisWidgets.s(3.0f), sx + contentW + AnubisWidgets.s(10.0f), (sy + rowH) - AnubisWidgets.s(3.0f), AnubisWidgets.c(AnubisWidgets.withAlpha(184549375, hover)), AnubisWidgets.s(10.0f));
            }
            rowLabel(setting.name(), pad, startY, rowH);
            float swW = AnubisWidgets.s(40.0f);
            float swH = AnubisWidgets.s(22.0f);
            AnubisWidgets.drawSwitch(dl, (sx + contentW) - swW, sy + ((rowH - swH) * 0.5f), swW, swH, AnubisWidgets.animate(ImGui.getID("##switch"), bs.get(), 16.0f));
        } else if (setting instanceof NumberSetting) {
            NumberSetting ns = (NumberSetting) setting;
            rowH = AnubisWidgets.s(64.0f);
            ImGui.setCursorPos(pad, startY + AnubisWidgets.s(10.0f));
            ImGui.textUnformatted(setting.name());
            ImFont small = ImGuiManager.getInstance().smallFont();
            String value = ns.formatted();
            float tw = AnubisWidgets.textWidth(small, value);
            float pillW = tw + AnubisWidgets.s(16.0f);
            float pillH = AnubisWidgets.lineHeight(small) + AnubisWidgets.s(8.0f);
            float wx = ImGui.getWindowPosX();
            float wy = ImGui.getWindowPosY() - ImGui.getScrollY();
            float px = ((wx + pad) + contentW) - pillW;
            float py = wy + startY + AnubisWidgets.s(8.0f);
            dl.addRectFilled(px, py, px + pillW, py + pillH, AnubisWidgets.c(AnubisTheme.GUI_SURFACE), pillH * 0.5f);
            AnubisWidgets.text(dl, small, px + AnubisWidgets.s(8.0f), py + AnubisWidgets.s(4.0f), AnubisTheme.GUI_TEXT, value);
            ImGui.setCursorPos(pad - AnubisWidgets.s(7.0f), startY + AnubisWidgets.s(36.0f));
            float[] fArrValue = ns.value();
            float fMin = ns.min();
            float fMax = ns.max();
            float fS = contentW + AnubisWidgets.s(14.0f);
            Objects.requireNonNull(ns);
            if (AnubisWidgets.sliderFloat("##v", fArrValue, fMin, fMax, fS, ns::snap)) {
                ns.notifyChanged();
            }
        } else if (setting instanceof ModeSetting) {
            ModeSetting ms = (ModeSetting) setting;
            rowLabel(setting.name(), pad, startY, rowH);
            float widest = 0.0f;
            for (String mode : ms.modes()) {
                widest = Math.max(widest, ImGui.calcTextSize(mode).x);
            }
            float dropW = Math.min(contentW * 0.6f, Math.max(AnubisWidgets.s(120.0f), widest + AnubisWidgets.s(TAB_BAR_H)));
            float dropH = AnubisWidgets.s(ROW_H);
            ImGui.setCursorPos((pad + contentW) - dropW, startY + ((rowH - dropH) * 0.5f));
            int picked = AnubisWidgets.dropdown("##mode", ms.get(), ms.modes(), dropW, dropH);
            if (picked >= 0) {
                ms.set(ms.modes().get(picked));
            }
        } else if (setting instanceof ColorSetting) {
            ColorSetting cs = (ColorSetting) setting;
            rowLabel(setting.name(), pad, startY, rowH);
            float swW2 = AnubisWidgets.s(TAB_BAR_H);
            float swH2 = AnubisWidgets.s(24.0f);
            ImGui.setCursorPos((pad + contentW) - swW2, startY + ((rowH - swH2) * 0.5f));
            float sx2 = ImGui.getCursorScreenPosX();
            float sy2 = ImGui.getCursorScreenPosY();
            if (ImGui.invisibleButton("##color", swW2, swH2)) {
                openColorPicker(module, cs);
            }
            boolean hovered = ImGui.isItemHovered();
            float[] val = cs.value();
            int r = ((int) (val[0] * 255.0f)) & 255;
            int g = ((int) (val[1] * 255.0f)) & 255;
            int b = ((int) (val[2] * 255.0f)) & 255;
            int a = val.length > 3 ? ((int) (val[3] * 255.0f)) & 255 : 255;
            dl.addRectFilled(sx2, sy2, sx2 + swW2, sy2 + swH2, AnubisWidgets.c(AnubisTheme.GUI_SURFACE), AnubisWidgets.s(7.0f));
            dl.addRectFilled(sx2, sy2, sx2 + swW2, sy2 + swH2, AnubisWidgets.c((a << 24) | (b << 16) | (g << 8) | r), AnubisWidgets.s(7.0f));
            dl.addRect(sx2, sy2, sx2 + swW2, sy2 + swH2, AnubisWidgets.c(hovered ? 1728053247 : 654311423), AnubisWidgets.s(7.0f), 0, 1.0f);
            if (hovered) {
                showTooltip(String.format("#%02X%02X%02X", Integer.valueOf(r), Integer.valueOf(g), Integer.valueOf(b)));
            }
        } else if (setting instanceof KeySetting) {
            KeySetting ks = (KeySetting) setting;
            boolean isListening = this.listeningKeySetting == ks;
            if (isListening) {
                if (this.listeningKeyFirstFrame) {
                    this.listeningKeyFirstFrame = false;
                } else {
                    int pressed = scanPressedKey();
                    if (pressed == 256) {
                        ks.setValue(-1);
                        this.listeningKeySetting = null;
                        isListening = false;
                    } else if (pressed != -1) {
                        ks.setValue(pressed);
                        this.listeningKeySetting = null;
                        isListening = false;
                    }
                }
            }
            rowLabel(setting.name(), pad, startY, rowH);
            float keyW = AnubisWidgets.s(96.0f);
            float chipH = AnubisWidgets.s(ROW_H);
            String keyLabel = isListening ? "Press a key..." : ks.value() == -1 ? Surface.BG_NONE : keyName(ks.value());
            ImGui.setCursorPos((pad + contentW) - keyW, startY + ((rowH - chipH) * 0.5f));
            if (AnubisWidgets.chipButton("##keyval", keyLabel, keyW, chipH, isListening)) {
                if (isListening) {
                    this.listeningKeySetting = null;
                } else {
                    this.listeningKeySetting = ks;
                    this.listeningKeyFirstFrame = true;
                    this.listeningModule = null;
                    this.listeningFirstFrame = false;
                }
            }
        } else if (setting instanceof StringSetting) {
            StringSetting ss = (StringSetting) setting;
            ImGui.setCursorPos(pad, startY + AnubisWidgets.s(10.0f));
            ImGui.textUnformatted(setting.name());
            ImGui.setCursorPos(pad, startY + AnubisWidgets.s(34.0f));
            ImGui.pushStyleVar(11, AnubisWidgets.s(12.0f), AnubisWidgets.s(8.0f));
            ImGui.setNextItemWidth(contentW);
            if (ImGui.inputText("##str", ss.getImString())) {
                ss.notifyChanged();
            }
            ImGui.popStyleVar();
            rowH = AnubisWidgets.s(34.0f) + ImGui.getTextLineHeight() + AnubisWidgets.s(16.0f) + AnubisWidgets.s(12.0f);
        } else if (setting instanceof StringListSetting) {
            StringListSetting sls = (StringListSetting) setting;
            rowH = renderStringList(sls, pad, contentW, startY);
        } else if (setting instanceof ActionSetting) {
            ActionSetting actionSetting = (ActionSetting) setting;
            float h = AnubisWidgets.s(36.0f);
            ImGui.setCursorPos(pad, startY + (((rowH + AnubisWidgets.s(ROW_INSET)) - h) * 0.5f));
            if (AnubisWidgets.chipButton("##action", setting.name(), contentW, h, false)) {
                actionSetting.run();
            }
            rowH += AnubisWidgets.s(ROW_INSET);
        } else if (setting instanceof BlockColorListSetting) {
            BlockColorListSetting bcls = (BlockColorListSetting) setting;
            rowH = this.blockLists.render(module, bcls, pad, contentW, startY);
        } else if (setting instanceof ItemColorListSetting) {
            ItemColorListSetting icls = (ItemColorListSetting) setting;
            long enabled = icls.entries().stream().filter(en -> {
                return en.enabled().get();
            }).count();
            if (listChip(setting.name(), "Edit (" + enabled + ")", pad, contentW, startY, rowH)) {
                this.activeItemColorPalette = icls;
                this.itemColorWindowOpen = true;
                this.activeItemColorEntry = null;
                this.editingItemDefaultColor = false;
            }
        }
        if (!(setting instanceof ActionSetting)) {
            renderResetAffordance(setting, pad, contentW, startY, rowH);
        }
        ImGui.setCursorPos(0.0f, startY + rowH);
    }

    private void renderResetAffordance(Setting setting, float pad, float contentW, float startY, float rowH) {
        if (this.tab == Tab.THEME) {
            return;
        }
        boolean modified = !setting.isDefault();
        float wx = ImGui.getWindowPosX();
        float wy = ImGui.getWindowPosY() - ImGui.getScrollY();
        boolean topLabel = (setting instanceof NumberSetting) || (setting instanceof StringSetting) || (setting instanceof StringListSetting) || (setting instanceof BlockColorListSetting);
        float labelCy = topLabel ? startY + AnubisWidgets.s(10.0f) + (ImGui.getTextLineHeight() * 0.5f) : startY + (rowH * 0.5f);
        if (modified) {
            float dx = (wx + pad) - AnubisWidgets.s(11.0f);
            float dy = wy + labelCy;
            float r = AnubisWidgets.s(3.0f);
            ImGui.getWindowDrawList().addCircleFilled(dx, dy, r, AnubisWidgets.c(GuiPalette.accent()), 12);
            if (ImGui.isMouseHoveringRect(dx - (r * ROW_GAP), dy - (r * ROW_GAP), dx + (r * ROW_GAP), dy + (r * ROW_GAP))) {
                showTooltip("Modified - right-click the row to reset");
            }
        }
        float x0 = (wx + pad) - AnubisWidgets.s(10.0f);
        float y0 = wy + startY;
        if (ImGui.isWindowHovered() && ImGui.isMouseClicked(1) && ImGui.isMouseHoveringRect(x0, y0, x0 + contentW + AnubisWidgets.s(20.0f), y0 + rowH)) {
            ImGui.openPopup("##setting_ctx");
        }
        ImGui.pushStyleVar(2, AnubisWidgets.s(ROW_INSET), AnubisWidgets.s(ROW_INSET));
        if (ImGui.beginPopup("##setting_ctx")) {
            ImGui.textDisabled(setting.name());
            ImGui.separator();
            if (modified) {
                if (ImGui.selectable("Reset to default")) {
                    setting.resetToDefault();
                }
            } else {
                ImGui.textDisabled("Already at default");
            }
            ImGui.endPopup();
        }
        ImGui.popStyleVar();
    }

    private static float renderStringList(StringListSetting list, float pad, float contentW, float startY) {
        ImGui.setCursorPos(pad, startY + AnubisWidgets.s(10.0f));
        ImGui.textUnformatted(list.name());
        float y = startY + AnubisWidgets.s(34.0f);
        float gap = AnubisWidgets.s(ROW_INSET);
        float fieldH = ImGui.getTextLineHeight() + AnubisWidgets.s(16.0f);
        int remove = -1;
        ImGui.pushStyleVar(11, AnubisWidgets.s(12.0f), AnubisWidgets.s(8.0f));
        for (int i = 0; i < list.size(); i++) {
            ImGui.pushID(i);
            ImGui.setCursorPos(pad, y);
            ImGui.setNextItemWidth((contentW - fieldH) - gap);
            if (ImGui.inputText("##entry", list.entry(i))) {
                list.edited();
            }
            ImGui.setCursorPos((pad + contentW) - fieldH, y);
            if (AnubisWidgets.chipButton("##remove", "x", fieldH, fieldH, false)) {
                remove = i;
            }
            ImGui.popID();
            y += fieldH + gap;
        }
        ImGui.popStyleVar();
        if (remove >= 0) {
            list.remove(remove);
        }
        if (list.canAdd()) {
            float addH = AnubisWidgets.s(ROW_H);
            ImGui.setCursorPos(pad, y);
            if (AnubisWidgets.chipButton("##add", "+ Add", contentW, addH, false)) {
                list.add();
            }
            y += addH;
        }
        return (y - startY) + AnubisWidgets.s(12.0f);
    }

    private static boolean listChip(String label, String chip, float pad, float contentW, float startY, float rowH) {
        rowLabel(label, pad, startY, rowH);
        float chipW = Math.max(AnubisWidgets.s(96.0f), ImGui.calcTextSize(chip).x + AnubisWidgets.s(PANEL_TOP));
        float chipH = AnubisWidgets.s(ROW_H);
        ImGui.setCursorPos((pad + contentW) - chipW, startY + ((rowH - chipH) * 0.5f));
        return AnubisWidgets.chipButton("##listedit", chip, chipW, chipH, false);
    }

    public static void applyTheme() {
        ImGui.pushStyleVar(3, AnubisWidgets.s(10.0f));
        ImGui.pushStyleVar(12, AnubisWidgets.s(8.0f));
        ImGui.pushStyleVar(9, AnubisWidgets.s(10.0f));
        ImGui.pushStyleVar(21, AnubisWidgets.s(ROW_INSET));
        ImGui.pushStyleVar(2, AnubisWidgets.s(10.0f), AnubisWidgets.s(10.0f));
        ImGui.pushStyleVar(11, AnubisWidgets.s(10.0f), AnubisWidgets.s(ROW_INSET));
        ImGui.pushStyleVar(14, AnubisWidgets.s(8.0f), AnubisWidgets.s(ROW_INSET));
        ImGui.pushStyleVar(4, 0.0f);
        ImGui.pushStyleVar(10, 1.0f);
        ImGui.pushStyleVar(18, AnubisWidgets.s(ROW_INSET));
        ImGui.pushStyleVar(19, AnubisWidgets.s(3.0f));
        for (int i = 0; i < THEME_COLORS.length; i += 2) {
            ImGui.pushStyleColor(THEME_COLORS[i], THEME_COLORS[i + 1]);
        }
        int accent = GuiPalette.accent();
        int card = GuiPalette.cardBg();
        ImGui.pushStyleColor(2, GuiPalette.panelBg());
        ImGui.pushStyleColor(10, card);
        ImGui.pushStyleColor(11, card);
        ImGui.pushStyleColor(12, card);
        ImGui.pushStyleColor(18, accent);
        ImGui.pushStyleColor(19, accent);
        ImGui.pushStyleColor(20, accent);
        ImGui.pushStyleColor(23, accent);
        ImGui.pushStyleColor(26, accent);
        ImGui.pushStyleColor(51, AnubisWidgets.withAlpha(accent, 0.4f));
    }

    public static void removeTheme() {
        ImGui.popStyleColor((THEME_COLORS.length / 2) + 10);
        ImGui.popStyleVar(11);
    }

    public static boolean pushFont(ImFont font) {
        if (font == null) {
            return false;
        }
        ImGui.pushFont(font);
        return true;
    }
}
