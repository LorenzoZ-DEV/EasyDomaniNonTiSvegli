package net.anubis.client.gui;

import imgui.ImDrawList;
import imgui.ImFont;
import imgui.ImGui;
import imgui.flag.ImGuiKey;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import net.anubis.client.config.ThemeStore;
import net.anubis.client.imgui.ImGuiManager;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.setting.BlockColorListSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2248;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_7923;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/BlockListEditor.class */
@Environment(EnvType.CLIENT)
final class BlockListEditor {
    private static final int INLINE_MAX_ROWS = 8;
    private static final float HEADER_H = 40.0f;
    private static final float INLINE_ROW_H = 38.0f;
    private static final float LIST_PAD = 4.0f;
    private static final float EMPTY_H = 44.0f;
    private static final float PICKER_W = 820.0f;
    private static final float PICKER_MAX_H = 760.0f;
    private static final float PICKER_ROW_H = 42.0f;
    private static final float COLUMN_GAP = 28.0f;
    private static final float TAB_BAR_ROOM = 86.0f;
    private static final float ICON_SLOT = 32.0f;
    private static final float COLOR_CONTROLS_W = 88.0f;
    private static final float COLOR_COLUMN_EXTRA = 98.0f;
    private static final float COLOR_WINDOW_GAP = 12.0f;
    private static final float COLOR_WINDOW_W = 290.0f;
    private static final float COLOR_WINDOW_H = 360.0f;
    private static final int ADD_ALL_CONFIRM_OVER = 64;
    private static final long ARM_NANOS = 3000000000L;
    private static final int ARM_RESET = 1;
    private static final int ARM_CLEAR = 2;
    private static final int ARM_ADD = 3;
    private static final int ROW_HOVER = 318767103;
    private static final int SCRIM = 1929379840;
    private static final int TRACER_ACCENT = -12276993;
    private static final String SELECT_LABEL = "Select";
    private static final String EMPTY_TEXT = "No blocks yet";
    private static final String PICKER_TITLE = "Select Blocks";
    private static final String AVAILABLE_TITLE = "All blocks";
    private static final String CHOSEN_TITLE = "Selected";
    private static final String NO_MATCHES = "No blocks match";
    private static final String ALL_CHOSEN = "Every block is selected";
    private static final String NONE_CHOSEN = "Nothing selected yet";
    private static final String SEARCH_HINT = "Search blocks...";
    private static final String RESET_LABEL = "Reset";
    private static final String ADD_SHOWN_LABEL = "Add all shown";
    private static final String CLEAR_LABEL = "Clear all";
    private static final String CONFIRM_LABEL = "Confirm";
    private static final int PICKER_FLAGS = 8639;
    private static final int LEV_INSERT = 1;
    private static final int LEV_SUBSTITUTE = 8;
    private static final int LEV_DELETE = 8;
    private static String language;
    private static int namesGeneration;
    private BlockColorListSetting picker;
    private ListState pickerList;
    private String pickerSubtitle;
    private boolean focusSearch;
    private boolean searchActive;
    private long pickerOpenedAt;
    private boolean pickerJustOpened;
    private int armedAction;
    private BlockColorListSetting armedFor;
    private long armedAt;
    private BlockColorListSetting colorList;
    private BlockColorListSetting.BlockEntry colorEntry;
    private boolean colorTracer;
    private String colorTitle;
    private float colorAtX;
    private float colorAtY;
    private boolean colorFocus;
    private static final int LIST_BG = AnubisWidgets.withAlpha(AnubisTheme.GUI_SURFACE, 0.5f);
    private static final Reference2ObjectOpenHashMap<class_2248, BlockInfo> INFOS = new Reference2ObjectOpenHashMap<>();
    private static final Map<BlockColorListSetting, ListState> LISTS = new WeakHashMap();
    private static int iconsFrame = -2;
    private static int[] levPrev = new int[64];
    private static int[] levCur = new int[64];
    private final Reference2ObjectOpenHashMap<BlockColorListSetting, float[]> inlineScroll = new Reference2ObjectOpenHashMap<>();
    private final ImString search = new ImString(64);
    private final float[] availableScroll = new float[2];
    private final float[] chosenScroll = new float[2];
    private final ImBoolean colorOpen = new ImBoolean(false);

    BlockListEditor() {
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/BlockListEditor$BlockInfo.class */
    @Environment(EnvType.CLIENT)
    private static final class BlockInfo {
        final class_2248 block;
        final int rawId;
        final String id;
        final String idLower;
        final String path;
        final boolean wallBanner;
        final String wallBannerId;
        String name;
        String nameLower;
        String hint;

        BlockInfo(class_2248 block) {
            String str;
            this.block = block;
            this.rawId = class_7923.field_41175.method_10206(block);
            class_2960 key = class_7923.field_41175.method_10221(block);
            this.id = key.toString();
            this.idLower = this.id.toLowerCase(Locale.ROOT);
            this.path = key.method_12832();
            this.wallBanner = this.path.endsWith("_wall_banner");
            if (!this.wallBanner && this.path.endsWith("_banner")) {
                str = key.method_12836() + ":" + this.path.substring(0, this.path.length() - "banner".length()) + "wall_banner";
            } else {
                str = null;
            }
            this.wallBannerId = str;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/BlockListEditor$Fit.class */
    @Environment(EnvType.CLIENT)
    private static final class Fit {
        String text;
        float textW;
        String hint;
        float width = -1.0f;
        int generation = -1;

        private Fit() {
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/BlockListEditor$Row.class */
    @Environment(EnvType.CLIENT)
    private static final class Row {
        final BlockColorListSetting.BlockEntry entry;
        final BlockInfo info;
        Row follower;
        Row leader;
        final Fit inlineFit = new Fit();
        final Fit pickerFit = new Fit();

        Row(BlockColorListSetting.BlockEntry entry, BlockInfo info) {
            this.entry = entry;
            this.info = info;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/BlockListEditor$ListState.class */
    @Environment(EnvType.CLIENT)
    private static final class ListState {
        final Row[] pickable;
        int chosenCount;
        String countPill;
        final int[] results;
        final long[] sortKeys;
        int resultCount;
        String searchKey;
        final Row[] available;
        int availableCount;
        int shownCount;
        String columnsKey;
        String availableLabel;
        String shownLabel;
        final Reference2ObjectOpenHashMap<BlockColorListSetting.BlockEntry, Row> byEntry = new Reference2ObjectOpenHashMap<>();
        Row[] chosen = new Row[0];
        long chosenRevision = -1;
        int labelsFor = -1;
        int searchGeneration = -1;
        Row[] shown = new Row[0];
        long[] shownKeys = new long[0];
        long columnsRevision = -1;
        int columnsGeneration = -1;
        int availableLabelFor = -1;
        int shownLabelFor = -1;

        ListState(BlockColorListSetting setting) {
            List<BlockColorListSetting.BlockEntry> entries = setting.entries();
            ArrayList<Row> rows = new ArrayList<>(entries.size());
            for (BlockColorListSetting.BlockEntry entry : entries) {
                class_2960 key = class_2960.method_12829(entry.id());
                class_2248 block = key == null ? null : (class_2248) class_7923.field_41175.method_17966(key).orElse(null);
                BlockInfo info = block == null ? null : (BlockInfo) BlockListEditor.INFOS.get(block);
                if (info != null) {
                    Row row = new Row(entry, info);
                    rows.add(row);
                    this.byEntry.put(entry, row);
                }
            }
            ArrayList<Row> offered = new ArrayList<>(rows.size());
            for (Row row2 : rows) {
                if (row2.info.wallBannerId != null) {
                    BlockColorListSetting.BlockEntry wall = setting.entry(row2.info.wallBannerId);
                    row2.follower = wall == null ? null : (Row) this.byEntry.get(wall);
                    if (row2.follower != null) {
                        row2.follower.leader = row2;
                    }
                }
                if (!row2.info.wallBanner) {
                    offered.add(row2);
                }
            }
            this.pickable = (Row[]) offered.toArray(new Row[0]);
            this.results = new int[this.pickable.length];
            this.sortKeys = new long[this.pickable.length];
            this.available = new Row[this.pickable.length];
        }
    }

    float render(ClientModule module, BlockColorListSetting setting, float pad, float contentW, float startY) {
        beginFrame();
        ListState list = list(setting);
        refreshChosen(setting, list);
        boolean colors = !setting.colorsHidden();
        ImDrawList dl = ImGui.getWindowDrawList();
        ImFont font = ImGuiManager.getInstance().moduleFont();
        ImFont small = ImGuiManager.getInstance().smallFont();
        float cardX0 = ImGui.getWindowPosX();
        float originY = ImGui.getWindowPosY() - ImGui.getScrollY();
        ImGui.setCursorPos(pad, startY + AnubisWidgets.s(10.0f));
        ImGui.textUnformatted(setting.name());
        float labelCy = originY + startY + AnubisWidgets.s(10.0f) + (ImGui.getTextLineHeight() * 0.5f);
        countPill(dl, small, ImGui.getItemRectMaxX() + AnubisWidgets.s(8.0f), labelCy, list.countPill);
        float chipW = AnubisWidgets.textWidth(font, SELECT_LABEL) + AnubisWidgets.s(COLUMN_GAP);
        float chipH = AnubisWidgets.s(COLUMN_GAP);
        float chipX = ((cardX0 + pad) + contentW) - chipW;
        ImGui.setCursorScreenPos(chipX, labelCy - (chipH * 0.5f));
        if (AnubisWidgets.chipButton("##add_blocks", SELECT_LABEL, chipW, chipH, false)) {
            openPicker(module, setting, list);
        }
        if (colors) {
            float swW = AnubisWidgets.s(30.0f);
            float swH = AnubisWidgets.s(20.0f);
            if (swatch(dl, "##default_color", setting.defaultColor(), (chipX - AnubisWidgets.s(10.0f)) - swW, labelCy - (swH * 0.5f), swW, swH)) {
                openColor(setting, null, false);
            }
            if (ImGui.isItemHovered(32768) && !editing(setting, null, false)) {
                ClickGuiRenderer.showTooltip("Default colour, for blocks not on the list");
            }
        }
        float listY = startY + AnubisWidgets.s(40.0f);
        float step = AnubisWidgets.s(INLINE_ROW_H);
        int n = list.chosenCount;
        float boxH = n == 0 ? AnubisWidgets.s(EMPTY_H) : (Math.min(n, 8) * step) + (AnubisWidgets.s(LIST_PAD) * 2.0f);
        float bx = cardX0 + pad;
        float by = originY + listY;
        float r = AnubisWidgets.s(10.0f);
        dl.addRectFilled(bx, by, bx + contentW, by + boxH, AnubisWidgets.c(LIST_BG), r);
        if (n == 0) {
            AnubisWidgets.text(dl, font, bx + AnubisWidgets.s(14.0f), by + ((boxH - AnubisWidgets.lineHeight(font)) * 0.5f), AnubisTheme.GUI_TEXT_MUTED, EMPTY_TEXT);
        } else if (n <= 8) {
            drawChosenRows(dl, setting, list, pad, listY + AnubisWidgets.s(LIST_PAD), contentW, step, 0, n, colors, font, small);
        } else {
            ImGui.setCursorPos(pad, listY + AnubisWidgets.s(LIST_PAD));
            if (ImGui.beginChild("##chosen", contentW, boxH - (AnubisWidgets.s(LIST_PAD) * 2.0f), false, 24)) {
                ImGui.setCursorPos(0.0f, 0.0f);
                ImGui.dummy(1.0f, n * step);
                float scroll = ImGui.getScrollY();
                int first = Math.max(0, (int) (scroll / step));
                int last = Math.min(n, (int) Math.ceil((scroll + ImGui.getWindowHeight()) / step));
                drawChosenRows(ImGui.getWindowDrawList(), setting, list, 0.0f, 0.0f, contentW, step, first, last, colors, font, small);
                ClickGuiRenderer.smoothScroll((float[]) this.inlineScroll.computeIfAbsent(setting, k -> {
                    return new float[2];
                }), frameDelta());
            }
            ImGui.endChild();
        }
        return AnubisWidgets.s(40.0f) + boxH + AnubisWidgets.s(COLOR_WINDOW_GAP);
    }

    private void drawChosenRows(ImDrawList dl, BlockColorListSetting setting, ListState list, float x, float y, float w, float step, int first, int last, boolean colors, ImFont font, ImFont small) {
        boolean windowHovered = ImGui.isWindowHovered();
        float icon = BlockIcons.size(AnubisWidgets.s(32.0f));
        float btn = AnubisWidgets.s(22.0f);
        float lineH = AnubisWidgets.lineHeight(font);
        float smallH = AnubisWidgets.lineHeight(small);
        Row removed = null;
        for (int i = first; i < last; i++) {
            Row row = list.chosen[i];
            ImGui.setCursorPos(x, y + (i * step));
            float x0 = ImGui.getCursorScreenPosX();
            float y0 = ImGui.getCursorScreenPosY();
            float cy = y0 + (step * 0.5f);
            if (windowHovered && ImGui.isMouseHoveringRect(x0, y0, x0 + w, y0 + step)) {
                dl.addRectFilled(x0 + AnubisWidgets.s(LIST_PAD), y0 + AnubisWidgets.s(1.0f), (x0 + w) - AnubisWidgets.s(LIST_PAD), (y0 + step) - AnubisWidgets.s(1.0f), AnubisWidgets.c(ROW_HOVER), AnubisWidgets.s(7.0f));
            }
            ImGui.pushID(row.info.rawId);
            float right = (x0 + w) - AnubisWidgets.s(10.0f);
            ImGui.setCursorScreenPos(right - btn, cy - (btn * 0.5f));
            if (ImGui.invisibleButton("##remove", btn, btn)) {
                removed = row;
            }
            boolean hovered = ImGui.isItemHovered();
            if (hovered) {
                dl.addCircleFilled(right - (btn * 0.5f), cy, btn * 0.5f, AnubisWidgets.c(AnubisTheme.GUI_SURFACE_HOVER), 20);
            }
            AnubisWidgets.cross(dl, right - (btn * 0.5f), cy, AnubisWidgets.s(3.5f), hovered ? ClickGuiRenderer.ERROR_TEXT : AnubisTheme.GUI_TEXT_MUTED);
            float right2 = right - (btn + AnubisWidgets.s(8.0f));
            if (colors) {
                right2 = colorControls(dl, setting, row, right2, cy);
            }
            ImGui.popID();
            float ix = x0 + AnubisWidgets.s(8.0f);
            BlockIcons.draw(dl, row.info.block, ix, cy - (icon * 0.5f), icon);
            float tx = ix + icon + AnubisWidgets.s(10.0f);
            Fit fit = fit(row.inlineFit, row.info, font, small, right2 - tx);
            AnubisWidgets.text(dl, font, tx, cy - (lineH * 0.5f), AnubisTheme.GUI_TEXT, fit.text);
            if (fit.hint != null) {
                AnubisWidgets.text(dl, small, tx + fit.textW + AnubisWidgets.s(6.0f), cy - (smallH * 0.5f), AnubisTheme.GUI_TEXT_MUTED, fit.hint);
            }
        }
        if (removed != null) {
            deselect(setting, removed);
        }
    }

    private float colorControls(ImDrawList dl, BlockColorListSetting setting, Row row, float right, float cy) {
        float swW = AnubisWidgets.s(30.0f);
        float swH = AnubisWidgets.s(18.0f);
        float box = AnubisWidgets.s(16.0f);
        float gap = AnubisWidgets.s(6.0f);
        float x = right - swW;
        if (swatch(dl, "##tracer_color", row.entry.tracerColor(), x, cy - (swH * 0.5f), swW, swH)) {
            openColor(setting, row, true);
        }
        if (ImGui.isItemHovered(32768) && !editing(setting, row.entry, true)) {
            ClickGuiRenderer.showTooltip("Tracer colour");
        }
        float x2 = x - (gap + box);
        if (checkbox(dl, "##tracer", row.entry.tracerEnabled().get(), x2, cy - (box * 0.5f), box)) {
            row.entry.tracerEnabled().set(!row.entry.tracerEnabled().get());
            setting.notifyChanged();
        }
        if (ImGui.isItemHovered(32768)) {
            ClickGuiRenderer.showTooltip("Tracer");
        }
        float x3 = x2 - (gap + swW);
        if (swatch(dl, "##box_color", row.entry.color(), x3, cy - (swH * 0.5f), swW, swH)) {
            openColor(setting, row, false);
        }
        if (ImGui.isItemHovered(32768) && !editing(setting, row.entry, false)) {
            ClickGuiRenderer.showTooltip("Box colour");
        }
        return x3 - AnubisWidgets.s(10.0f);
    }

    void renderWindows() {
        if (this.picker != null) {
            renderPicker();
        }
        if (this.colorList != null) {
            renderColorWindow();
        }
    }

    void close() {
        closePicker();
        closeColor();
    }

    boolean closePicker() {
        if (this.picker == null) {
            return false;
        }
        this.picker = null;
        this.pickerList = null;
        disarm();
        return true;
    }

    private void openPicker(ClientModule module, BlockColorListSetting setting, ListState list) {
        closeColor();
        this.picker = setting;
        this.pickerList = list;
        this.pickerSubtitle = module.name() + " / " + setting.name();
        this.search.set("");
        this.focusSearch = true;
        this.searchActive = false;
        this.pickerOpenedAt = System.nanoTime();
        this.pickerJustOpened = true;
        float[] fArr = this.availableScroll;
        float[] fArr2 = this.availableScroll;
        float[] fArr3 = this.chosenScroll;
        this.chosenScroll[1] = 0.0f;
        fArr3[0] = 0.0f;
        fArr2[1] = 0.0f;
        fArr[0] = 0.0f;
        disarm();
    }

    private void renderPicker() {
        beginFrame();
        BlockColorListSetting setting = this.picker;
        ListState list = this.pickerList;
        refreshChosen(setting, list);
        boolean colors = !setting.colorsHidden();
        float sw = ImGui.getIO().getDisplaySizeX();
        float sh = ImGui.getIO().getDisplaySizeY();
        float margin = AnubisWidgets.s(16.0f);
        float extra = colors ? AnubisWidgets.s(COLOR_COLUMN_EXTRA) : 0.0f;
        float w = Math.min(sw - (margin * 2.0f), AnubisWidgets.s(PICKER_W) + extra);
        float h = Math.min((sh - AnubisWidgets.s(TAB_BAR_ROOM)) - AnubisWidgets.s(32.0f), AnubisWidgets.s(PICKER_MAX_H));
        float open = ClickGuiRenderer.easeOut(ClickGuiRenderer.elapsed(this.pickerOpenedAt, 200.0f));
        float x = Math.round((sw - w) * 0.5f);
        float y = Math.round(Math.max(margin, (((sh - AnubisWidgets.s(TAB_BAR_ROOM)) - h) * 0.5f) + AnubisWidgets.s(8.0f)) + (AnubisWidgets.s(14.0f) * (1.0f - open)));
        float rounding = ClickGuiRenderer.cardRounding();
        ImGui.setNextWindowPos(0.0f, 0.0f, 1);
        ImGui.setNextWindowSize(sw, sh, 1);
        if (this.pickerJustOpened) {
            ImGui.setNextWindowFocus();
        }
        ImGui.pushStyleVar(0, Math.max(0.01f, open));
        ImGui.pushStyleVar(2, 0.0f, 0.0f);
        ImGui.pushStyleVar(14, 0.0f, 0.0f);
        ImGui.pushStyleVar(4, 0.0f);
        boolean closeClicked = false;
        boolean visible = ImGui.begin("##block_picker", PICKER_FLAGS);
        if (this.pickerJustOpened) {
            imgui.internal.ImGui.bringWindowToDisplayFront(imgui.internal.ImGui.getCurrentWindow());
        }
        if (visible) {
            ImDrawList dl = ImGui.getWindowDrawList();
            dl.addRectFilled(0.0f, 0.0f, sw, sh, AnubisWidgets.c(SCRIM));
            shadow(dl, x, y, w, h, rounding);
            dl.addRectFilled(x, y, x + w, y + h, AnubisWidgets.c(GuiPalette.abgr(ThemeStore.get().cards.color.argbWithAlpha(250))), rounding);
            dl.addRect(x, y, x + w, y + h, AnubisWidgets.c(ROW_HOVER), rounding, 0, 1.0f);
            ImFont font = ImGuiManager.getInstance().moduleFont();
            ImFont small = ImGuiManager.getInstance().smallFont();
            float pad = AnubisWidgets.s(24.0f);
            float top = y + AnubisWidgets.s(22.0f);
            float btn = AnubisWidgets.s(COLUMN_GAP);
            ClickGuiRenderer.drawCardTitle(PICKER_TITLE, x + pad, top + (btn * 0.5f));
            ImGui.setCursorPos(((x + w) - pad) - btn, top);
            if (AnubisWidgets.iconButton("##close", btn)) {
                closeClicked = true;
            }
            AnubisWidgets.cross(dl, ((x + w) - pad) - (btn * 0.5f), top + (btn * 0.5f), AnubisWidgets.s(4.5f), AnubisTheme.GUI_TEXT);
            if (chip("##reset_blocks", RESET_LABEL, armed(1, setting), setting.isDefault(), (((x + w) - pad) - btn) - AnubisWidgets.s(10.0f), top + (btn * 0.5f), small, "Back to the default blocks") && confirm(1, setting)) {
                setting.resetToDefault();
            }
            float subY = top + btn + AnubisWidgets.s(2.0f);
            AnubisWidgets.text(dl, small, x + pad, subY, AnubisTheme.GUI_TEXT_MUTED, this.pickerSubtitle);
            ImGui.setCursorPos(x + pad, subY + AnubisWidgets.lineHeight(small) + AnubisWidgets.s(14.0f));
            ImGui.pushStyleVar(11, AnubisWidgets.s(COLOR_WINDOW_GAP), AnubisWidgets.s(8.0f));
            ImGui.setNextItemWidth(w - (pad * 2.0f));
            if (this.focusSearch) {
                ImGui.setKeyboardFocusHere();
                this.focusSearch = false;
            }
            boolean enter = this.searchActive && (ImGui.isKeyPressed(ImGuiKey.Enter, false) || ImGui.isKeyPressed(ImGuiKey.KeypadEnter, false));
            ImGui.inputTextWithHint("##block_search", SEARCH_HINT, this.search);
            this.searchActive = ImGui.isItemActive();
            ImGui.popStyleVar();
            float headerY = ImGui.getItemRectMaxY() + AnubisWidgets.s(18.0f);
            String ranked = list.searchKey;
            refreshSearch(list, this.search.get());
            if (list.searchKey != ranked) {
                float[] fArr = this.availableScroll;
                float[] fArr2 = this.availableScroll;
                float[] fArr3 = this.chosenScroll;
                this.chosenScroll[1] = 0.0f;
                fArr3[0] = 0.0f;
                fArr2[1] = 0.0f;
                fArr[0] = 0.0f;
                if (this.armedAction == 3) {
                    disarm();
                }
            }
            refreshColumns(setting, list);
            if (enter && !list.searchKey.isBlank()) {
                if (list.availableCount > 0) {
                    select(setting, list.available[0]);
                }
                this.focusSearch = true;
            }
            float colW = (((w - (pad * 2.0f)) - AnubisWidgets.s(COLUMN_GAP)) - extra) * 0.5f;
            float rightW = colW + extra;
            float leftX = x + pad;
            float rightX = leftX + colW + AnubisWidgets.s(COLUMN_GAP);
            float headerH = AnubisWidgets.s(COLUMN_GAP);
            float listTop = headerY + headerH + AnubisWidgets.s(10.0f);
            float listBottom = (y + h) - AnubisWidgets.s(18.0f);
            if (list.availableLabelFor != list.availableCount) {
                list.availableLabelFor = list.availableCount;
                list.availableLabel = Integer.toString(list.availableCount);
            }
            if (list.shownLabelFor != list.shownCount) {
                list.shownLabelFor = list.shownCount;
                list.shownLabel = Integer.toString(list.shownCount);
            }
            columnHeader(dl, font, small, leftX, headerY, headerH, colW, AVAILABLE_TITLE, list.availableLabel);
            columnHeader(dl, font, small, rightX, headerY, headerH, rightW, CHOSEN_TITLE, list.shownLabel);
            float headerCy = headerY + (headerH * 0.5f);
            boolean addShown = chip("##add_shown", ADD_SHOWN_LABEL, armed(3, setting), list.availableCount == 0, (leftX + colW) - AnubisWidgets.s(6.0f), headerCy, small, "Adds every block this column lists");
            boolean clearChosen = chip("##clear_all", CLEAR_LABEL, armed(2, setting), list.chosenCount == 0, (rightX + rightW) - AnubisWidgets.s(6.0f), headerCy, small, "Takes every block off the list");
            float dividerX = Math.round(leftX + colW + (AnubisWidgets.s(COLUMN_GAP) * 0.5f));
            dl.addLine(dividerX, headerY, dividerX, listBottom, AnubisWidgets.c(AnubisTheme.GUI_DIVIDER), 1.0f);
            float listH = Math.max(1.0f, listBottom - listTop);
            String emptyLeft = list.searchKey.isBlank() ? ALL_CHOSEN : NO_MATCHES;
            String emptyRight = list.searchKey.isBlank() ? NONE_CHOSEN : NO_MATCHES;
            Row picked = column("##available", list.available, list.availableCount, true, null, leftX, listTop, colW, listH, this.availableScroll, emptyLeft, font, small);
            Row dropped = column("##chosen", list.shown, list.shownCount, false, colors ? setting : null, rightX, listTop, rightW, listH, this.chosenScroll, emptyRight, font, small);
            if (picked != null) {
                select(setting, picked);
            }
            if (dropped != null) {
                deselect(setting, dropped);
            }
            if (addShown) {
                addAllShown(setting, list);
            }
            if (clearChosen) {
                clearAll(setting);
            }
            if (!this.pickerJustOpened && ImGui.isMouseClicked(0) && ImGui.isWindowHovered() && !ImGui.isMouseHoveringRect(x, y, x + w, y + h)) {
                closeClicked = true;
            }
        }
        ImGui.end();
        ImGui.popStyleVar(4);
        this.pickerJustOpened = false;
        if (closeClicked) {
            closePicker();
        }
    }

    private static boolean chip(String id, String label, boolean armed, boolean disabled, float right, float cy, ImFont small, String tooltip) {
        boolean pushed = ClickGuiRenderer.pushFont(small);
        float chipW = Math.max(AnubisWidgets.textWidth(small, label), AnubisWidgets.textWidth(small, CONFIRM_LABEL)) + AnubisWidgets.s(20.0f);
        float chipH = AnubisWidgets.lineHeight(small) + AnubisWidgets.s(COLOR_WINDOW_GAP);
        ImGui.setCursorPos(right - chipW, cy - (chipH * 0.5f));
        ImGui.beginDisabled(disabled);
        boolean clicked = AnubisWidgets.chipButton(id, armed ? CONFIRM_LABEL : label, chipW, chipH, armed);
        ImGui.endDisabled();
        if (ImGui.isItemHovered(65536)) {
            ClickGuiRenderer.showTooltip(tooltip);
        }
        if (pushed) {
            ImGui.popFont();
        }
        return clicked;
    }

    private boolean confirm(int action, BlockColorListSetting setting) {
        if (armed(action, setting)) {
            disarm();
            return true;
        }
        this.armedAction = action;
        this.armedFor = setting;
        this.armedAt = System.nanoTime();
        return false;
    }

    private boolean armed(int action, BlockColorListSetting setting) {
        return this.armedAction == action && this.armedFor == setting && System.nanoTime() - this.armedAt < ARM_NANOS;
    }

    private void disarm() {
        this.armedAction = 0;
        this.armedFor = null;
    }

    private static void columnHeader(ImDrawList dl, ImFont font, ImFont small, float x, float y, float h, float w, String title, String count) {
        float cy = y + (h * 0.5f);
        AnubisWidgets.text(dl, font, x + AnubisWidgets.s(LIST_PAD), cy - (AnubisWidgets.lineHeight(font) * 0.5f), AnubisTheme.GUI_TEXT, title);
        countPill(dl, small, x + AnubisWidgets.s(LIST_PAD) + AnubisWidgets.textWidth(font, title) + AnubisWidgets.s(8.0f), cy, count);
        dl.addLine(x, y + h + AnubisWidgets.s(LIST_PAD), x + w, y + h + AnubisWidgets.s(LIST_PAD), AnubisWidgets.c(AnubisTheme.GUI_DIVIDER), 1.0f);
    }

    private Row column(String id, Row[] rows, int count, boolean adding, BlockColorListSetting colors, float x, float y, float w, float h, float[] scroll, String emptyText, ImFont font, ImFont small) {
        float textRight;
        boolean hovered;
        Row clicked = null;
        ImGui.setCursorPos(x, y);
        if (ImGui.beginChild(id, w, h, false, 24)) {
            float step = AnubisWidgets.s(42.0f);
            float rowW = w - AnubisWidgets.s(6.0f);
            float rowH = step - AnubisWidgets.s(2.0f);
            ImGui.setCursorPos(0.0f, 0.0f);
            ImGui.dummy(1.0f, (count * step) + AnubisWidgets.s(6.0f));
            ImDrawList dl = ImGui.getWindowDrawList();
            if (count == 0) {
                AnubisWidgets.text(dl, font, ImGui.getWindowPosX() + AnubisWidgets.s(8.0f), ImGui.getWindowPosY() + AnubisWidgets.s(COLOR_WINDOW_GAP), AnubisTheme.GUI_TEXT_MUTED, emptyText);
            } else {
                float lineH = AnubisWidgets.lineHeight(font);
                float smallH = AnubisWidgets.lineHeight(small);
                float icon = BlockIcons.size(AnubisWidgets.s(32.0f));
                float btnR = AnubisWidgets.s(11.0f);
                float scrollY = ImGui.getScrollY();
                int first = Math.max(0, (int) (scrollY / step));
                int last = Math.min(count, (int) Math.ceil((scrollY + ImGui.getWindowHeight()) / step));
                int accent = GuiPalette.accent();
                boolean windowHovered = ImGui.isWindowHovered();
                for (int i = first; i < last; i++) {
                    Row row = rows[i];
                    ImGui.setCursorPos(0.0f, i * step);
                    float x0 = ImGui.getCursorScreenPosX();
                    float y0 = ImGui.getCursorScreenPosY();
                    float cy = y0 + (rowH * 0.5f);
                    float bx = ((x0 + rowW) - AnubisWidgets.s(8.0f)) - btnR;
                    ImGui.pushID(row.info.rawId);
                    if (colors == null) {
                        if (ImGui.invisibleButton("##row", rowW, rowH)) {
                            clicked = row;
                        }
                        hovered = ImGui.isItemHovered();
                        if (hovered) {
                            dl.addRectFilled(x0, y0, x0 + rowW, y0 + rowH, AnubisWidgets.c(ROW_HOVER), AnubisWidgets.s(8.0f));
                        }
                        textRight = (bx - btnR) - AnubisWidgets.s(10.0f);
                    } else {
                        if (windowHovered && ImGui.isMouseHoveringRect(x0, y0, x0 + rowW, y0 + rowH)) {
                            dl.addRectFilled(x0, y0, x0 + rowW, y0 + rowH, AnubisWidgets.c(ROW_HOVER), AnubisWidgets.s(8.0f));
                        }
                        float minusX = (bx - btnR) - AnubisWidgets.s(6.0f);
                        textRight = colorControls(dl, colors, row, minusX - AnubisWidgets.s(LIST_PAD), cy);
                        ImGui.setCursorScreenPos(minusX, y0);
                        if (ImGui.invisibleButton("##minus", (x0 + rowW) - minusX, rowH)) {
                            clicked = row;
                        }
                        boolean hovered2 = ImGui.isItemHovered();
                        ImGui.setCursorScreenPos(x0, y0);
                        if (ImGui.invisibleButton("##row", Math.max(1.0f, (textRight + AnubisWidgets.s(LIST_PAD)) - x0), rowH)) {
                            clicked = row;
                        }
                        hovered = hovered2 | ImGui.isItemHovered();
                    }
                    ImGui.popID();
                    float ix = x0 + AnubisWidgets.s(6.0f);
                    BlockIcons.draw(dl, row.info.block, ix, cy - (icon * 0.5f), icon);
                    float tx = ix + icon + AnubisWidgets.s(COLOR_WINDOW_GAP);
                    Fit fit = fit(row.pickerFit, row.info, font, small, textRight - tx);
                    AnubisWidgets.text(dl, font, tx, cy - (lineH * 0.5f), hovered ? AnubisTheme.GUI_TEXT : AnubisTheme.GUI_TEXT_ROW, fit.text);
                    if (fit.hint != null) {
                        AnubisWidgets.text(dl, small, tx + fit.textW + AnubisWidgets.s(6.0f), cy - (smallH * 0.5f), AnubisTheme.GUI_TEXT_MUTED, fit.hint);
                    }
                    int fill = hovered ? adding ? accent : ClickGuiRenderer.ERROR_TEXT : AnubisTheme.GUI_SURFACE;
                    int glyph = hovered ? -1 : AnubisTheme.GUI_TEXT_MUTED;
                    dl.addCircleFilled(bx, cy, btnR, AnubisWidgets.c(fill), 24);
                    dl.addLine(bx - (btnR * 0.45f), cy, bx + (btnR * 0.45f), cy, AnubisWidgets.c(glyph), AnubisWidgets.s(1.6f));
                    if (adding) {
                        dl.addLine(bx, cy - (btnR * 0.45f), bx, cy + (btnR * 0.45f), AnubisWidgets.c(glyph), AnubisWidgets.s(1.6f));
                    }
                }
            }
            ClickGuiRenderer.smoothScroll(scroll, frameDelta());
        }
        ImGui.endChild();
        return clicked;
    }

    private void openColor(BlockColorListSetting setting, Row row, boolean tracer) {
        String str;
        closeColor();
        this.colorList = setting;
        this.colorEntry = row == null ? null : row.entry;
        this.colorTracer = tracer;
        if (row == null) {
            str = "Default Color";
        } else {
            str = tracer ? row.info.name + " Tracer" : row.info.name;
        }
        String label = str;
        this.colorTitle = "Block Color - " + label + "###block_color";
        this.colorOpen.set(true);
        if (ImGui.isMousePosValid()) {
            this.colorAtX = ImGui.getIO().getMousePosX();
            this.colorAtY = ImGui.getIO().getMousePosY();
        } else {
            this.colorAtX = ImGui.getIO().getDisplaySizeX() * 0.5f;
            this.colorAtY = ImGui.getIO().getDisplaySizeY() * 0.5f;
        }
        this.colorFocus = true;
    }

    private void renderColorWindow() {
        float[] fArrTracerColor;
        if (this.colorEntry == null) {
            fArrTracerColor = this.colorList.defaultColor();
        } else {
            fArrTracerColor = this.colorTracer ? this.colorEntry.tracerColor() : this.colorEntry.color();
        }
        float[] target = fArrTracerColor;
        float sw = ImGui.getIO().getDisplaySizeX();
        float sh = ImGui.getIO().getDisplaySizeY();
        boolean leftOf = (this.colorAtX + COLOR_WINDOW_GAP) + COLOR_WINDOW_W > sw;
        boolean above = (this.colorAtY + COLOR_WINDOW_GAP) + COLOR_WINDOW_H > sh;
        ImGui.setNextWindowPos(this.colorAtX + (leftOf ? -12.0f : COLOR_WINDOW_GAP), this.colorAtY + (above ? -12.0f : COLOR_WINDOW_GAP), 8, leftOf ? 1.0f : 0.0f, above ? 1.0f : 0.0f);
        ImGui.setNextWindowSize(270.0f, 330.0f, 4);
        if (this.colorFocus) {
            ImGui.setNextWindowFocus();
            this.colorFocus = false;
        }
        if (ImGui.begin(this.colorTitle, this.colorOpen, 352)) {
            if (ImGui.colorPicker4("##bcpicker", target, 135593984)) {
                this.colorList.notifyChanged();
            }
            ImGui.setNextItemWidth(240.0f);
            if (ImGui.colorEdit4("RGBA##bcce", target, 135593984)) {
                this.colorList.notifyChanged();
            }
            if (ImGui.button("Close##bccp", 80.0f, 22.0f)) {
                this.colorOpen.set(false);
            }
        }
        ImGui.end();
        if (!this.colorOpen.get()) {
            closeColor();
        }
    }

    private boolean editing(BlockColorListSetting setting, BlockColorListSetting.BlockEntry entry, boolean tracer) {
        return this.colorList == setting && this.colorEntry == entry && this.colorTracer == tracer;
    }

    private void closeColor() {
        if (this.colorList == null) {
            return;
        }
        this.colorList.notifyChanged();
        this.colorList = null;
        this.colorEntry = null;
        this.colorOpen.set(false);
    }

    private static void select(BlockColorListSetting setting, Row row) {
        setting.select(row.entry);
        if (row.follower != null) {
            setting.select(row.follower.entry);
        }
    }

    private static void deselect(BlockColorListSetting setting, Row row) {
        setting.deselect(row.entry);
        if (row.follower != null) {
            setting.deselect(row.follower.entry);
        }
    }

    private void addAllShown(BlockColorListSetting setting, ListState list) {
        int n = list.availableCount;
        if (n != 0) {
            if (n <= 64 || confirm(3, setting)) {
                ArrayList<BlockColorListSetting.BlockEntry> entries = new ArrayList<>(n + 8);
                for (int i = 0; i < n; i++) {
                    Row row = list.available[i];
                    entries.add(row.entry);
                    if (row.follower != null) {
                        entries.add(row.follower.entry);
                    }
                }
                setting.selectAll(entries);
            }
        }
    }

    private void clearAll(BlockColorListSetting setting) {
        if (confirm(2, setting)) {
            setting.deselectAll();
        }
    }

    private static void beginFrame() {
        refreshNames();
        int frame = ImGuiManager.getInstance().frameCount();
        if (frame > iconsFrame + 1) {
            BlockIcons.forget();
        }
        iconsFrame = frame;
        BlockIcons.validate();
    }

    private static void refreshNames() {
        String selected = class_310.method_1551().method_1526().method_4669();
        if (selected.equals(language)) {
            return;
        }
        language = selected;
        if (INFOS.isEmpty()) {
            for (class_2248 block : class_7923.field_41175) {
                INFOS.put(block, new BlockInfo(block));
            }
        }
        Object2IntOpenHashMap<String> uses = new Object2IntOpenHashMap<>();
        ObjectIterator it = INFOS.values().iterator();
        while (it.hasNext()) {
            BlockInfo info = (BlockInfo) it.next();
            info.name = info.block.method_9518().getString();
            info.nameLower = info.name.toLowerCase(Locale.ROOT);
            if (!info.wallBanner) {
                uses.addTo(info.name, 1);
            }
        }
        ObjectIterator it2 = INFOS.values().iterator();
        while (it2.hasNext()) {
            BlockInfo info2 = (BlockInfo) it2.next();
            info2.hint = (info2.wallBanner || uses.getInt(info2.name) > 1) ? info2.path : null;
        }
        namesGeneration++;
    }

    private static ListState list(BlockColorListSetting setting) {
        ListState list = LISTS.get(setting);
        if (list == null) {
            list = new ListState(setting);
            LISTS.put(setting, list);
        }
        return list;
    }

    private static void refreshChosen(BlockColorListSetting setting, ListState list) {
        if (list.chosenRevision == setting.revision()) {
            return;
        }
        list.chosenRevision = setting.revision();
        List<BlockColorListSetting.BlockEntry> selected = setting.selected();
        if (list.chosen.length < selected.size()) {
            list.chosen = new Row[selected.size()];
        }
        int n = 0;
        for (int i = 0; i < selected.size(); i++) {
            Row row = (Row) list.byEntry.get(selected.get(i));
            if (row != null && (row.leader == null || !row.leader.entry.enabled().get())) {
                int i2 = n;
                n++;
                list.chosen[i2] = row;
            }
        }
        list.chosenCount = n;
        if (list.labelsFor != n) {
            list.labelsFor = n;
            list.countPill = Integer.toString(n);
        }
    }

    private static void refreshSearch(ListState list, String raw) {
        if (raw == list.searchKey && list.searchGeneration == namesGeneration) {
            return;
        }
        list.searchKey = raw;
        list.searchGeneration = namesGeneration;
        Row[] rows = list.pickable;
        String filter = raw.trim().toLowerCase(Locale.ROOT);
        if (filter.isEmpty()) {
            for (int i = 0; i < rows.length; i++) {
                list.results[i] = i;
            }
            list.resultCount = rows.length;
            return;
        }
        String[] words = filter.split(" ");
        long[] keys = list.sortKeys;
        int count = 0;
        for (int i2 = 0; i2 < rows.length; i2++) {
            int distance = matchDistance(rows[i2].info, filter, words);
            if (distance >= 0) {
                int i3 = count;
                count++;
                keys[i3] = (((long) distance) << 32) | ((long) i2);
            }
        }
        Arrays.sort(keys, 0, count);
        for (int k = 0; k < count; k++) {
            list.results[k] = (int) keys[k];
        }
        list.resultCount = count;
    }

    private static void refreshColumns(BlockColorListSetting setting, ListState list) {
        if (list.columnsRevision == setting.revision() && list.columnsKey == list.searchKey && list.columnsGeneration == namesGeneration) {
            return;
        }
        list.columnsRevision = setting.revision();
        list.columnsKey = list.searchKey;
        list.columnsGeneration = namesGeneration;
        int n = 0;
        for (int k = 0; k < list.resultCount; k++) {
            Row row = list.pickable[list.results[k]];
            if (!row.entry.enabled().get()) {
                int i = n;
                n++;
                list.available[i] = row;
            }
        }
        list.availableCount = n;
        if (list.shown.length < list.chosen.length) {
            list.shown = new Row[list.chosen.length];
            list.shownKeys = new long[list.chosen.length];
        }
        String filter = list.searchKey.trim().toLowerCase(Locale.ROOT);
        if (filter.isEmpty()) {
            System.arraycopy(list.chosen, 0, list.shown, 0, list.chosenCount);
            list.shownCount = list.chosenCount;
            return;
        }
        String[] words = filter.split(" ");
        long[] keys = list.shownKeys;
        int count = 0;
        for (int i2 = 0; i2 < list.chosenCount; i2++) {
            int distance = matchDistance(list.chosen[i2].info, filter, words);
            if (distance >= 0) {
                int i3 = count;
                count++;
                keys[i3] = (((long) distance) << 32) | ((long) i2);
            }
        }
        Arrays.sort(keys, 0, count);
        for (int k2 = 0; k2 < count; k2++) {
            list.shown[k2] = list.chosen[(int) keys[k2]];
        }
        list.shownCount = count;
    }

    private static int matchDistance(BlockInfo info, String filter, String[] words) {
        int found = Math.max(wordsFound(info.nameLower, words), wordsFound(info.idLower, words));
        int nameDistance = levenshtein(filter, info.nameLower);
        int idDistance = levenshtein(filter, info.idLower);
        float relevancy = Math.max(1.0f - (nameDistance / info.nameLower.length()), 1.0f - (idDistance / info.idLower.length()));
        if (found > 0 || relevancy >= 0.5f) {
            return Math.min(nameDistance, idDistance);
        }
        return -1;
    }

    private static int wordsFound(String text, String[] words) {
        int found = 0;
        for (String word : words) {
            if (!text.contains(word)) {
                return 0;
            }
            if (!word.isEmpty()) {
                int iIndexOf = text.indexOf(word);
                while (true) {
                    int at = iIndexOf;
                    if (at >= 0) {
                        found++;
                        iIndexOf = text.indexOf(word, at + word.length());
                    }
                }
            }
        }
        return found;
    }

    private static int levenshtein(String from, String to) {
        int n = from.length();
        int m = to.length();
        if (n == 0) {
            return m * 1;
        }
        if (m == 0) {
            return n * 8;
        }
        if (levPrev.length <= m) {
            levPrev = new int[m + 1];
            levCur = new int[m + 1];
        }
        int[] prev = levPrev;
        int[] cur = levCur;
        for (int j = 0; j <= m; j++) {
            prev[j] = j * 1;
        }
        for (int i = 1; i <= n; i++) {
            cur[0] = i * 8;
            char typed = from.charAt(i - 1);
            for (int j2 = 1; j2 <= m; j2++) {
                int substitute = prev[j2 - 1] + (typed == to.charAt(j2 - 1) ? 0 : 8);
                cur[j2] = Math.min(Math.min(prev[j2] + 8, cur[j2 - 1] + 1), substitute);
            }
            int[] swap = prev;
            prev = cur;
            cur = swap;
        }
        return prev[m];
    }

    private static Fit fit(Fit fit, BlockInfo info, ImFont font, ImFont small, float avail) {
        if (fit.width == avail && fit.generation == namesGeneration) {
            return fit;
        }
        fit.width = avail;
        fit.generation = namesGeneration;
        fit.text = AnubisWidgets.ellipsize(font, info.name, avail);
        fit.textW = AnubisWidgets.textWidth(font, fit.text);
        float room = (avail - fit.textW) - AnubisWidgets.s(6.0f);
        fit.hint = (info.hint == null || room < AnubisWidgets.s(40.0f)) ? null : AnubisWidgets.ellipsize(small, info.hint, room);
        return fit;
    }

    private static void countPill(ImDrawList dl, ImFont small, float x, float cy, String count) {
        float w = AnubisWidgets.textWidth(small, count) + AnubisWidgets.s(14.0f);
        float h = AnubisWidgets.lineHeight(small) + AnubisWidgets.s(6.0f);
        float y = cy - (h * 0.5f);
        dl.addRectFilled(x, y, x + w, y + h, AnubisWidgets.c(AnubisTheme.GUI_SURFACE), h * 0.5f);
        AnubisWidgets.text(dl, small, x + AnubisWidgets.s(7.0f), y + AnubisWidgets.s(3.0f), AnubisTheme.GUI_TEXT_MUTED, count);
    }

    private static void shadow(ImDrawList dl, float x, float y, float w, float h, float rounding) {
        for (int i = 1; i <= 6; i++) {
            float e = AnubisWidgets.s(2.5f) * i;
            dl.addRectFilled(x - e, (y - e) + AnubisWidgets.s(LIST_PAD), x + w + e, y + h + e + AnubisWidgets.s(LIST_PAD), AnubisWidgets.c(AnubisTheme.GUI_SHADOW), rounding + e);
        }
    }

    private static boolean swatch(ImDrawList dl, String id, float[] rgba, float x, float y, float w, float h) {
        ImGui.setCursorScreenPos(x, y);
        boolean clicked = ImGui.invisibleButton(id, w, h);
        boolean hovered = ImGui.isItemHovered();
        float r = AnubisWidgets.s(5.0f);
        dl.addRectFilled(x, y, x + w, y + h, AnubisWidgets.c(AnubisTheme.GUI_SURFACE), r);
        dl.addRectFilled(x, y, x + w, y + h, AnubisWidgets.c(abgr(rgba)), r);
        dl.addRect(x, y, x + w, y + h, AnubisWidgets.c(hovered ? 1728053247 : 654311423), r, 0, 1.0f);
        return clicked;
    }

    private static boolean checkbox(ImDrawList dl, String id, boolean on, float x, float y, float size) {
        int i;
        ImGui.setCursorScreenPos(x, y);
        boolean clicked = ImGui.invisibleButton(id, size, size);
        if (on) {
            i = TRACER_ACCENT;
        } else {
            i = ImGui.isItemHovered() ? 1090519039 : 452984831;
        }
        int border = i;
        float r = AnubisWidgets.s(LIST_PAD);
        dl.addRectFilled(x, y, x + size, y + size, AnubisWidgets.c(AnubisTheme.GUI_SURFACE), r);
        dl.addRect(x + 0.5f, y + 0.5f, (x + size) - 0.5f, (y + size) - 0.5f, AnubisWidgets.c(border), r, 0, 1.0f);
        if (on) {
            AnubisWidgets.check(dl, x + (size * 0.5f), y + (size * 0.5f), size * 0.28f, TRACER_ACCENT);
        }
        return clicked;
    }

    private static int abgr(float[] rgba) {
        return (unit(rgba[3]) << 24) | (unit(rgba[2]) << 16) | (unit(rgba[1]) << 8) | unit(rgba[0]);
    }

    private static int unit(float v) {
        return Math.round(Math.max(0.0f, Math.min(1.0f, v)) * 255.0f);
    }

    private static float frameDelta() {
        return ClickGuiRenderer.animationDelta(Math.max(ImGui.getIO().getDeltaTime(), 0.001f));
    }
}
