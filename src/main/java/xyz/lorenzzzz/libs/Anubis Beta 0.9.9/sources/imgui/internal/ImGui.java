package imgui.internal;

import imgui.ImDrawList;
import imgui.ImFont;
import imgui.ImGuiPlatformMonitor;
import imgui.ImGuiViewport;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.type.ImBoolean;
import imgui.type.ImFloat;
import imgui.type.ImInt;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/internal/ImGui.class */
public final class ImGui extends imgui.ImGui {
    private static native void nInit();

    private static native long nGetCurrentWindowRead();

    private static native long nGetCurrentWindow();

    private static native long nFindWindowByID(int i);

    private static native long nFindWindowByName(String str);

    private static native void nUpdateWindowParentAndRootLinks(long j, int i, long j2);

    private static native void nCalcWindowNextAutoFitSize(ImVec2 imVec2, long j);

    private static native float nCalcWindowNextAutoFitSizeX(long j);

    private static native float nCalcWindowNextAutoFitSizeY(long j);

    private static native boolean nIsWindowChildOf(long j, long j2, boolean z, boolean z2);

    private static native boolean nIsWindowWithinBeginStackOf(long j, long j2);

    private static native boolean nIsWindowAbove(long j, long j2);

    private static native boolean nIsWindowNavFocusable(long j);

    private static native void nSetWindowPos(long j, float f, float f2);

    private static native void nSetWindowPos(long j, float f, float f2, int i);

    private static native void nSetWindowSize(long j, float f, float f2);

    private static native void nSetWindowSize(long j, float f, float f2, int i);

    private static native void nSetWindowCollapsed(long j, boolean z);

    private static native void nSetWindowCollapsed(long j, boolean z, int i);

    private static native void nSetWindowHitTestHole(long j, float f, float f2, float f3, float f4);

    private static native void nWindowRectAbsToRel(ImRect imRect, long j, float f, float f2, float f3, float f4);

    private static native void nWindowRectRelToAbs(ImRect imRect, long j, float f, float f2, float f3, float f4);

    private static native void nFocusWindow(long j);

    private static native void nFocusWindow(long j, int i);

    private static native void nFocusTopMostWindowUnderOne(long j, long j2, long j3, int i);

    private static native void nBringWindowToFocusFront(long j);

    private static native void nBringWindowToDisplayFront(long j);

    private static native void nBringWindowToDisplayBack(long j);

    private static native void nBringWindowToDisplayBehind(long j, long j2);

    private static native int nFindWindowDisplayIndex(long j);

    private static native long nFindBottomMostVisibleWindowWithinBeginStack(long j);

    private static native void nSetCurrentFont(long j);

    private static native long nGetDefaultFont();

    private static native long nGetForegroundDrawList(long j);

    private static native void nInitialize();

    private static native void nShutdown();

    private static native void nUpdateInputEvents(boolean z);

    private static native void nUpdateHoveredWindowAndCaptureFlags();

    private static native void nStartMouseMovingWindow(long j);

    private static native void nStartMouseMovingWindowOrNode(long j, long j2, boolean z);

    private static native void nUpdateMouseMovingWindowNewFrame();

    private static native void nUpdateMouseMovingWindowEndFrame();

    private static native long nGetViewportPlatformMonitor(long j);

    private static native void nSetNextWindowScroll(float f, float f2);

    private static native void nSetScrollX(long j, float f);

    private static native void nSetScrollY(long j, float f);

    private static native void nSetScrollFromPosX(long j, float f, float f2);

    private static native void nSetScrollFromPosY(long j, float f, float f2);

    private static native void nScrollToItem();

    private static native void nScrollToItem(int i);

    private static native void nScrollToRect(long j, float f, float f2, float f3, float f4);

    private static native void nScrollToRect(long j, float f, float f2, float f3, float f4, int i);

    private static native void nScrollToRectEx(ImVec2 imVec2, long j, float f, float f2, float f3, float f4);

    private static native float nScrollToRectExX(long j, float f, float f2, float f3, float f4);

    private static native float nScrollToRectExY(long j, float f, float f2, float f3, float f4);

    private static native void nScrollToRectEx(ImVec2 imVec2, long j, float f, float f2, float f3, float f4, int i);

    private static native float nScrollToRectExX(long j, float f, float f2, float f3, float f4, int i);

    private static native float nScrollToRectExY(long j, float f, float f2, float f3, float f4, int i);

    private static native int nGetItemStatusFlags();

    private static native int nGetItemFlags();

    private static native int nGetActiveID();

    private static native int nGetFocusID();

    private static native void nSetActiveID(int i, long j);

    private static native void nSetFocusID(int i, long j);

    private static native void nClearActiveID();

    private static native int nGetHoveredID();

    private static native void nSetHoveredID(int i);

    private static native void nKeepAliveID(int i);

    private static native void nMarkItemEdited(int i);

    private static native void nPushOverrideID(int i);

    private static native int nGetIDWithSeed(String str, String str2, int i);

    private static native void nItemSize(float f, float f2);

    private static native void nItemSize(float f, float f2, float f3);

    private static native void nItemSize(float f, float f2, float f3, float f4);

    private static native void nItemSize(float f, float f2, float f3, float f4, float f5);

    private static native boolean nItemHoverable(float f, float f2, float f3, float f4, int i, int i2);

    private static native boolean nIsWindowContentHoverable(long j, int i);

    private static native boolean nIsClippedEx(float f, float f2, float f3, float f4, int i);

    private static native void nSetLastItemData(int i, int i2, int i3, float f, float f2, float f3, float f4);

    private static native void nCalcItemSize(ImVec2 imVec2, float f, float f2, float f3, float f4);

    private static native float nCalcItemSizeX(float f, float f2, float f3, float f4);

    private static native float nCalcItemSizeY(float f, float f2, float f3, float f4);

    private static native float nCalcWrapWidthForPos(float f, float f2, float f3);

    private static native void nPushMultiItemsWidths(int i, float f);

    private static native boolean nIsItemToggledSelection();

    private static native void nGetContentRegionMaxAbs(ImVec2 imVec2);

    private static native float nGetContentRegionMaxAbsX();

    private static native float nGetContentRegionMaxAbsY();

    private static native void nPushItemFlag(int i, boolean z);

    private static native void nPopItemFlag();

    private static native void nDockBuilderDockWindow(String str, int i);

    private static native long nDockBuilderGetNode(int i);

    private static native long nDockBuilderGetCentralNode(int i);

    private static native int nDockBuilderAddNode();

    private static native int nDockBuilderAddNode(int i);

    private static native int nDockBuilderAddNode(int i, int i2);

    private static native void nDockBuilderRemoveNode(int i);

    private static native void nDockBuilderRemoveNodeDockedWindows(int i);

    private static native void nDockBuilderRemoveNodeDockedWindows(int i, boolean z);

    private static native void nDockBuilderRemoveNodeChildNodes(int i);

    private static native void nDockBuilderSetNodePos(int i, float f, float f2);

    private static native void nDockBuilderSetNodeSize(int i, float f, float f2);

    private static native int nDockBuilderSplitNode(int i, int i2, float f, int[] iArr, int[] iArr2);

    private static native void nDockBuilderCopyWindowSettings(String str, String str2);

    private static native void nDockBuilderFinish(int i);

    private static native void nTableOpenContextMenu();

    private static native void nTableOpenContextMenu(int i);

    private static native void nTableSetColumnWidth(int i, float f);

    private static native void nTableSetColumnSortDirection(int i, int i2, boolean z);

    private static native int nTableGetHoveredColumn();

    private static native int nTableGetHoveredRow();

    private static native float nTableGetHeaderRowHeight();

    private static native void nTablePushBackgroundChannel();

    private static native void nTablePopBackgroundChannel();

    private static native void nTextEx(String str);

    private static native void nTextEx(String str, String str2);

    private static native void nTextEx(String str, String str2, int i);

    private static native boolean nButtonEx(String str);

    private static native boolean nButtonEx(String str, float f, float f2);

    private static native boolean nButtonEx(String str, float f, float f2, int i);

    private static native boolean nButtonEx(String str, int i);

    private static native boolean nArrowButtonEx(String str, int i, float f, float f2);

    private static native boolean nArrowButtonEx(String str, int i, float f, float f2, int i2);

    private static native boolean nImageButtonEx(int i, long j, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, int i2);

    private static native void nSeparatorEx(int i);

    private static native void nSeparatorEx(int i, float f);

    private static native void nSeparatorTextEx(int i, String str, String str2, float f);

    private static native boolean nCloseButton(int i, float f, float f2);

    private static native boolean nCollapseButton(int i, float f, float f2, long j);

    private static native void nScrollbar(int i);

    private static native void nGetWindowScrollbarRect(ImRect imRect, long j, int i);

    private static native int nGetWindowScrollbarID(long j, int i);

    private static native int nGetWindowResizeCornerID(long j, int i);

    private static native int nGetWindowResizeBorderID(long j, int i);

    private static native boolean nButtonBehavior(float f, float f2, float f3, float f4, int i, boolean[] zArr, boolean[] zArr2);

    private static native boolean nButtonBehavior(float f, float f2, float f3, float f4, int i, boolean[] zArr, boolean[] zArr2, int i2);

    private static native boolean nSplitterBehavior(float f, float f2, float f3, float f4, int i, int i2, float[] fArr, float[] fArr2, float f5, float f6);

    private static native boolean nSplitterBehavior(float f, float f2, float f3, float f4, int i, int i2, float[] fArr, float[] fArr2, float f5, float f6, float f7);

    private static native boolean nSplitterBehavior(float f, float f2, float f3, float f4, int i, int i2, float[] fArr, float[] fArr2, float f5, float f6, float f7, float f8);

    private static native boolean nSplitterBehavior(float f, float f2, float f3, float f4, int i, int i2, float[] fArr, float[] fArr2, float f5, float f6, float f7, float f8, int i3);

    private static native void nShadeVertsTransformPos(long j, int i, int i2, float f, float f2, float f3, float f4, float f5, float f6);

    static {
        nInit();
    }

    public static void init() {
    }

    public static ImGuiWindow getCurrentWindowRead() {
        return new ImGuiWindow(nGetCurrentWindowRead());
    }

    public static ImGuiWindow getCurrentWindow() {
        return new ImGuiWindow(nGetCurrentWindow());
    }

    public static ImGuiWindow findWindowByID(int id) {
        return new ImGuiWindow(nFindWindowByID(id));
    }

    public static ImGuiWindow findWindowByName(String name) {
        return new ImGuiWindow(nFindWindowByName(name));
    }

    public static void updateWindowParentAndRootLinks(ImGuiWindow window, int flags, ImGuiWindow parentWindow) {
        nUpdateWindowParentAndRootLinks(window.ptr, flags, parentWindow.ptr);
    }

    public static ImVec2 calcWindowNextAutoFitSize(ImGuiWindow window) {
        ImVec2 dst = new ImVec2();
        nCalcWindowNextAutoFitSize(dst, window.ptr);
        return dst;
    }

    public static float calcWindowNextAutoFitSizeX(ImGuiWindow window) {
        return nCalcWindowNextAutoFitSizeX(window.ptr);
    }

    public static float calcWindowNextAutoFitSizeY(ImGuiWindow window) {
        return nCalcWindowNextAutoFitSizeY(window.ptr);
    }

    public static void calcWindowNextAutoFitSize(ImVec2 dst, ImGuiWindow window) {
        nCalcWindowNextAutoFitSize(dst, window.ptr);
    }

    public static boolean isWindowChildOf(ImGuiWindow window, ImGuiWindow potentialParent, boolean popupHierarchy, boolean dockHierarchy) {
        return nIsWindowChildOf(window.ptr, potentialParent.ptr, popupHierarchy, dockHierarchy);
    }

    public static boolean isWindowWithinBeginStackOf(ImGuiWindow window, ImGuiWindow potentialParent) {
        return nIsWindowWithinBeginStackOf(window.ptr, potentialParent.ptr);
    }

    public static boolean isWindowAbove(ImGuiWindow potentialAbove, ImGuiWindow potentialBelow) {
        return nIsWindowAbove(potentialAbove.ptr, potentialBelow.ptr);
    }

    public static boolean isWindowNavFocusable(ImGuiWindow window) {
        return nIsWindowNavFocusable(window.ptr);
    }

    public static void setWindowPos(ImGuiWindow window, ImVec2 pos) {
        nSetWindowPos(window.ptr, pos.x, pos.y);
    }

    public static void setWindowPos(ImGuiWindow window, float posX, float posY) {
        nSetWindowPos(window.ptr, posX, posY);
    }

    public static void setWindowPos(ImGuiWindow window, ImVec2 pos, int cond) {
        nSetWindowPos(window.ptr, pos.x, pos.y, cond);
    }

    public static void setWindowPos(ImGuiWindow window, float posX, float posY, int cond) {
        nSetWindowPos(window.ptr, posX, posY, cond);
    }

    public static void setWindowSize(ImGuiWindow window, ImVec2 size) {
        nSetWindowSize(window.ptr, size.x, size.y);
    }

    public static void setWindowSize(ImGuiWindow window, float sizeX, float sizeY) {
        nSetWindowSize(window.ptr, sizeX, sizeY);
    }

    public static void setWindowSize(ImGuiWindow window, ImVec2 size, int cond) {
        nSetWindowSize(window.ptr, size.x, size.y, cond);
    }

    public static void setWindowSize(ImGuiWindow window, float sizeX, float sizeY, int cond) {
        nSetWindowSize(window.ptr, sizeX, sizeY, cond);
    }

    public static void setWindowCollapsed(ImGuiWindow window, boolean collapsed) {
        nSetWindowCollapsed(window.ptr, collapsed);
    }

    public static void setWindowCollapsed(ImGuiWindow window, boolean collapsed, int cond) {
        nSetWindowCollapsed(window.ptr, collapsed, cond);
    }

    public static void setWindowHitTestHole(ImGuiWindow window, ImVec2 pos, ImVec2 size) {
        nSetWindowHitTestHole(window.ptr, pos.x, pos.y, size.x, size.y);
    }

    public static void setWindowHitTestHole(ImGuiWindow window, float posX, float posY, float sizeX, float sizeY) {
        nSetWindowHitTestHole(window.ptr, posX, posY, sizeX, sizeY);
    }

    public static ImRect windowRectAbsToRel(ImGuiWindow window, ImRect r) {
        ImRect dst = new ImRect();
        nWindowRectAbsToRel(dst, window.ptr, r.min.x, r.min.y, r.max.x, r.max.y);
        return dst;
    }

    public static ImRect windowRectAbsToRel(ImGuiWindow window, float rMinX, float rMinY, float rMaxX, float rMaxY) {
        ImRect dst = new ImRect();
        nWindowRectAbsToRel(dst, window.ptr, rMinX, rMinY, rMaxX, rMaxY);
        return dst;
    }

    public static void windowRectAbsToRel(ImRect dst, ImGuiWindow window, ImRect r) {
        nWindowRectAbsToRel(dst, window.ptr, r.min.x, r.min.y, r.max.x, r.max.y);
    }

    public static void windowRectAbsToRel(ImRect dst, ImGuiWindow window, float rMinX, float rMinY, float rMaxX, float rMaxY) {
        nWindowRectAbsToRel(dst, window.ptr, rMinX, rMinY, rMaxX, rMaxY);
    }

    public static ImRect windowRectRelToAbs(ImGuiWindow window, ImRect r) {
        ImRect dst = new ImRect();
        nWindowRectRelToAbs(dst, window.ptr, r.min.x, r.min.y, r.max.x, r.max.y);
        return dst;
    }

    public static ImRect windowRectRelToAbs(ImGuiWindow window, float rMinX, float rMinY, float rMaxX, float rMaxY) {
        ImRect dst = new ImRect();
        nWindowRectRelToAbs(dst, window.ptr, rMinX, rMinY, rMaxX, rMaxY);
        return dst;
    }

    public static void windowRectRelToAbs(ImRect dst, ImGuiWindow window, ImRect r) {
        nWindowRectRelToAbs(dst, window.ptr, r.min.x, r.min.y, r.max.x, r.max.y);
    }

    public static void windowRectRelToAbs(ImRect dst, ImGuiWindow window, float rMinX, float rMinY, float rMaxX, float rMaxY) {
        nWindowRectRelToAbs(dst, window.ptr, rMinX, rMinY, rMaxX, rMaxY);
    }

    public static void focusWindow(ImGuiWindow window) {
        nFocusWindow(window.ptr);
    }

    public static void focusWindow(ImGuiWindow window, int flags) {
        nFocusWindow(window.ptr, flags);
    }

    public static void focusTopMostWindowUnderOne(ImGuiWindow underThisWindow, ImGuiWindow ignoreWindow, ImGuiViewport filterViewport, int flags) {
        nFocusTopMostWindowUnderOne(underThisWindow.ptr, ignoreWindow.ptr, filterViewport.ptr, flags);
    }

    public static void bringWindowToFocusFront(ImGuiWindow window) {
        nBringWindowToFocusFront(window.ptr);
    }

    public static void bringWindowToDisplayFront(ImGuiWindow window) {
        nBringWindowToDisplayFront(window.ptr);
    }

    public static void bringWindowToDisplayBack(ImGuiWindow window) {
        nBringWindowToDisplayBack(window.ptr);
    }

    public static void bringWindowToDisplayBehind(ImGuiWindow window, ImGuiWindow aboveWindow) {
        nBringWindowToDisplayBehind(window.ptr, aboveWindow.ptr);
    }

    public static int findWindowDisplayIndex(ImGuiWindow window) {
        return nFindWindowDisplayIndex(window.ptr);
    }

    public static ImGuiWindow findBottomMostVisibleWindowWithinBeginStack(ImGuiWindow window) {
        return new ImGuiWindow(nFindBottomMostVisibleWindowWithinBeginStack(window.ptr));
    }

    public static void setCurrentFont(ImFont font) {
        nSetCurrentFont(font.ptr);
    }

    public static ImFont getDefaultFont() {
        return new ImFont(nGetDefaultFont());
    }

    public static ImDrawList getForegroundDrawList(ImGuiWindow window) {
        return new ImDrawList(nGetForegroundDrawList(window.ptr));
    }

    public static void initialize() {
        nInitialize();
    }

    public static void shutdown() {
        nShutdown();
    }

    public static void updateInputEvents(boolean trickleFastInputs) {
        nUpdateInputEvents(trickleFastInputs);
    }

    public static void updateHoveredWindowAndCaptureFlags() {
        nUpdateHoveredWindowAndCaptureFlags();
    }

    public static void startMouseMovingWindow(ImGuiWindow window) {
        nStartMouseMovingWindow(window.ptr);
    }

    public static void startMouseMovingWindowOrNode(ImGuiWindow window, ImGuiDockNode node, boolean undockFloatingNode) {
        nStartMouseMovingWindowOrNode(window.ptr, node.ptr, undockFloatingNode);
    }

    public static void updateMouseMovingWindowNewFrame() {
        nUpdateMouseMovingWindowNewFrame();
    }

    public static void updateMouseMovingWindowEndFrame() {
        nUpdateMouseMovingWindowEndFrame();
    }

    public static ImGuiPlatformMonitor getViewportPlatformMonitor(ImGuiViewport viewport) {
        return new ImGuiPlatformMonitor(nGetViewportPlatformMonitor(viewport.ptr));
    }

    public static void setNextWindowScroll(ImVec2 scroll) {
        nSetNextWindowScroll(scroll.x, scroll.y);
    }

    public static void setNextWindowScroll(float scrollX, float scrollY) {
        nSetNextWindowScroll(scrollX, scrollY);
    }

    public static void setScrollX(ImGuiWindow window, float scrollX) {
        nSetScrollX(window.ptr, scrollX);
    }

    public static void setScrollY(ImGuiWindow window, float scrollY) {
        nSetScrollY(window.ptr, scrollY);
    }

    public static void setScrollFromPosX(ImGuiWindow window, float localX, float centerXRatio) {
        nSetScrollFromPosX(window.ptr, localX, centerXRatio);
    }

    public static void setScrollFromPosY(ImGuiWindow window, float localY, float centerYRatio) {
        nSetScrollFromPosY(window.ptr, localY, centerYRatio);
    }

    public static void scrollToItem() {
        nScrollToItem();
    }

    public static void scrollToItem(int flags) {
        nScrollToItem(flags);
    }

    public static void scrollToRect(ImGuiWindow window, ImRect rect) {
        nScrollToRect(window.ptr, rect.min.x, rect.min.y, rect.max.x, rect.max.y);
    }

    public static void scrollToRect(ImGuiWindow window, float rectMinX, float rectMinY, float rectMaxX, float rectMaxY) {
        nScrollToRect(window.ptr, rectMinX, rectMinY, rectMaxX, rectMaxY);
    }

    public static void scrollToRect(ImGuiWindow window, ImRect rect, int flags) {
        nScrollToRect(window.ptr, rect.min.x, rect.min.y, rect.max.x, rect.max.y, flags);
    }

    public static void scrollToRect(ImGuiWindow window, float rectMinX, float rectMinY, float rectMaxX, float rectMaxY, int flags) {
        nScrollToRect(window.ptr, rectMinX, rectMinY, rectMaxX, rectMaxY, flags);
    }

    public static ImVec2 scrollToRectEx(ImGuiWindow window, ImRect rect) {
        ImVec2 dst = new ImVec2();
        nScrollToRectEx(dst, window.ptr, rect.min.x, rect.min.y, rect.max.x, rect.max.y);
        return dst;
    }

    public static ImVec2 scrollToRectEx(ImGuiWindow window, float rectMinX, float rectMinY, float rectMaxX, float rectMaxY) {
        ImVec2 dst = new ImVec2();
        nScrollToRectEx(dst, window.ptr, rectMinX, rectMinY, rectMaxX, rectMaxY);
        return dst;
    }

    public static float scrollToRectExX(ImGuiWindow window, ImRect rect) {
        return nScrollToRectExX(window.ptr, rect.min.x, rect.min.y, rect.max.x, rect.max.y);
    }

    public static float scrollToRectExY(ImGuiWindow window, ImRect rect) {
        return nScrollToRectExY(window.ptr, rect.min.x, rect.min.y, rect.max.x, rect.max.y);
    }

    public static void scrollToRectEx(ImVec2 dst, ImGuiWindow window, ImRect rect) {
        nScrollToRectEx(dst, window.ptr, rect.min.x, rect.min.y, rect.max.x, rect.max.y);
    }

    public static void scrollToRectEx(ImVec2 dst, ImGuiWindow window, float rectMinX, float rectMinY, float rectMaxX, float rectMaxY) {
        nScrollToRectEx(dst, window.ptr, rectMinX, rectMinY, rectMaxX, rectMaxY);
    }

    public static ImVec2 scrollToRectEx(ImGuiWindow window, ImRect rect, int flags) {
        ImVec2 dst = new ImVec2();
        nScrollToRectEx(dst, window.ptr, rect.min.x, rect.min.y, rect.max.x, rect.max.y, flags);
        return dst;
    }

    public static ImVec2 scrollToRectEx(ImGuiWindow window, float rectMinX, float rectMinY, float rectMaxX, float rectMaxY, int flags) {
        ImVec2 dst = new ImVec2();
        nScrollToRectEx(dst, window.ptr, rectMinX, rectMinY, rectMaxX, rectMaxY, flags);
        return dst;
    }

    public static float scrollToRectExX(ImGuiWindow window, ImRect rect, int flags) {
        return nScrollToRectExX(window.ptr, rect.min.x, rect.min.y, rect.max.x, rect.max.y, flags);
    }

    public static float scrollToRectExY(ImGuiWindow window, ImRect rect, int flags) {
        return nScrollToRectExY(window.ptr, rect.min.x, rect.min.y, rect.max.x, rect.max.y, flags);
    }

    public static void scrollToRectEx(ImVec2 dst, ImGuiWindow window, ImRect rect, int flags) {
        nScrollToRectEx(dst, window.ptr, rect.min.x, rect.min.y, rect.max.x, rect.max.y, flags);
    }

    public static void scrollToRectEx(ImVec2 dst, ImGuiWindow window, float rectMinX, float rectMinY, float rectMaxX, float rectMaxY, int flags) {
        nScrollToRectEx(dst, window.ptr, rectMinX, rectMinY, rectMaxX, rectMaxY, flags);
    }

    public static int getItemStatusFlags() {
        return nGetItemStatusFlags();
    }

    public static int getItemFlags() {
        return nGetItemFlags();
    }

    public static int getActiveID() {
        return nGetActiveID();
    }

    public static int getFocusID() {
        return nGetFocusID();
    }

    public static void setActiveID(int id, ImGuiWindow window) {
        nSetActiveID(id, window.ptr);
    }

    public static void setFocusID(int id, ImGuiWindow window) {
        nSetFocusID(id, window.ptr);
    }

    public static void clearActiveID() {
        nClearActiveID();
    }

    public static int getHoveredID() {
        return nGetHoveredID();
    }

    public static void setHoveredID(int id) {
        nSetHoveredID(id);
    }

    public static void keepAliveID(int id) {
        nKeepAliveID(id);
    }

    public static void markItemEdited(int id) {
        nMarkItemEdited(id);
    }

    public static void pushOverrideID(int id) {
        nPushOverrideID(id);
    }

    public static int getIDWithSeed(String strIdBegin, String strIdEnd, int seed) {
        return nGetIDWithSeed(strIdBegin, strIdEnd, seed);
    }

    public static void itemSize(ImVec2 size) {
        nItemSize(size.x, size.y);
    }

    public static void itemSize(float sizeX, float sizeY) {
        nItemSize(sizeX, sizeY);
    }

    public static void itemSize(ImVec2 size, float textBaselineY) {
        nItemSize(size.x, size.y, textBaselineY);
    }

    public static void itemSize(float sizeX, float sizeY, float textBaselineY) {
        nItemSize(sizeX, sizeY, textBaselineY);
    }

    public static void itemSize(ImRect bb) {
        nItemSize(bb.min.x, bb.min.y, bb.max.x, bb.max.y);
    }

    public static void itemSize(float bbMinX, float bbMinY, float bbMaxX, float bbMaxY) {
        nItemSize(bbMinX, bbMinY, bbMaxX, bbMaxY);
    }

    public static void itemSize(ImRect bb, float textBaselineY) {
        nItemSize(bb.min.x, bb.min.y, bb.max.x, bb.max.y, textBaselineY);
    }

    public static void itemSize(float bbMinX, float bbMinY, float bbMaxX, float bbMaxY, float textBaselineY) {
        nItemSize(bbMinX, bbMinY, bbMaxX, bbMaxY, textBaselineY);
    }

    public static boolean itemHoverable(ImRect bb, int id, int itemFlags) {
        return nItemHoverable(bb.min.x, bb.min.y, bb.max.x, bb.max.y, id, itemFlags);
    }

    public static boolean itemHoverable(float bbMinX, float bbMinY, float bbMaxX, float bbMaxY, int id, int itemFlags) {
        return nItemHoverable(bbMinX, bbMinY, bbMaxX, bbMaxY, id, itemFlags);
    }

    public static boolean isWindowContentHoverable(ImGuiWindow window, int flags) {
        return nIsWindowContentHoverable(window.ptr, flags);
    }

    public static boolean isClippedEx(ImRect bb, int id) {
        return nIsClippedEx(bb.min.x, bb.min.y, bb.max.x, bb.max.y, id);
    }

    public static boolean isClippedEx(float bbMinX, float bbMinY, float bbMaxX, float bbMaxY, int id) {
        return nIsClippedEx(bbMinX, bbMinY, bbMaxX, bbMaxY, id);
    }

    public static void setLastItemData(int itemId, int inFlags, int statusFlags, ImRect itemRect) {
        nSetLastItemData(itemId, inFlags, statusFlags, itemRect.min.x, itemRect.min.y, itemRect.max.x, itemRect.max.y);
    }

    public static void setLastItemData(int itemId, int inFlags, int statusFlags, float itemRectMinX, float itemRectMinY, float itemRectMaxX, float itemRectMaxY) {
        nSetLastItemData(itemId, inFlags, statusFlags, itemRectMinX, itemRectMinY, itemRectMaxX, itemRectMaxY);
    }

    public static ImVec2 calcItemSize(ImVec2 size, float defaultW, float defaultH) {
        ImVec2 dst = new ImVec2();
        nCalcItemSize(dst, size.x, size.y, defaultW, defaultH);
        return dst;
    }

    public static ImVec2 calcItemSize(float sizeX, float sizeY, float defaultW, float defaultH) {
        ImVec2 dst = new ImVec2();
        nCalcItemSize(dst, sizeX, sizeY, defaultW, defaultH);
        return dst;
    }

    public static float calcItemSizeX(ImVec2 size, float defaultW, float defaultH) {
        return nCalcItemSizeX(size.x, size.y, defaultW, defaultH);
    }

    public static float calcItemSizeY(ImVec2 size, float defaultW, float defaultH) {
        return nCalcItemSizeY(size.x, size.y, defaultW, defaultH);
    }

    public static void calcItemSize(ImVec2 dst, ImVec2 size, float defaultW, float defaultH) {
        nCalcItemSize(dst, size.x, size.y, defaultW, defaultH);
    }

    public static void calcItemSize(ImVec2 dst, float sizeX, float sizeY, float defaultW, float defaultH) {
        nCalcItemSize(dst, sizeX, sizeY, defaultW, defaultH);
    }

    public static float calcWrapWidthForPos(ImVec2 pos, float wrapPosX) {
        return nCalcWrapWidthForPos(pos.x, pos.y, wrapPosX);
    }

    public static float calcWrapWidthForPos(float posX, float posY, float wrapPosX) {
        return nCalcWrapWidthForPos(posX, posY, wrapPosX);
    }

    public static void pushMultiItemsWidths(int components, float widthFull) {
        nPushMultiItemsWidths(components, widthFull);
    }

    public static boolean isItemToggledSelection() {
        return nIsItemToggledSelection();
    }

    public static ImVec2 getContentRegionMaxAbs() {
        ImVec2 dst = new ImVec2();
        nGetContentRegionMaxAbs(dst);
        return dst;
    }

    public static float getContentRegionMaxAbsX() {
        return nGetContentRegionMaxAbsX();
    }

    public static float getContentRegionMaxAbsY() {
        return nGetContentRegionMaxAbsY();
    }

    public static void getContentRegionMaxAbs(ImVec2 dst) {
        nGetContentRegionMaxAbs(dst);
    }

    public static void pushItemFlag(int option, boolean enabled) {
        nPushItemFlag(option, enabled);
    }

    public static void popItemFlag() {
        nPopItemFlag();
    }

    public static void dockBuilderDockWindow(String windowName, int nodeId) {
        nDockBuilderDockWindow(windowName, nodeId);
    }

    public static ImGuiDockNode dockBuilderGetNode(int nodeId) {
        return new ImGuiDockNode(nDockBuilderGetNode(nodeId));
    }

    public static ImGuiDockNode dockBuilderGetCentralNode(int nodeId) {
        return new ImGuiDockNode(nDockBuilderGetCentralNode(nodeId));
    }

    public static int dockBuilderAddNode() {
        return nDockBuilderAddNode();
    }

    public static int dockBuilderAddNode(int nodeId) {
        return nDockBuilderAddNode(nodeId);
    }

    public static int dockBuilderAddNode(int nodeId, int flags) {
        return nDockBuilderAddNode(nodeId, flags);
    }

    public static void dockBuilderRemoveNode(int nodeId) {
        nDockBuilderRemoveNode(nodeId);
    }

    public static void dockBuilderRemoveNodeDockedWindows(int nodeId) {
        nDockBuilderRemoveNodeDockedWindows(nodeId);
    }

    public static void dockBuilderRemoveNodeDockedWindows(int nodeId, boolean clearSettingsRefs) {
        nDockBuilderRemoveNodeDockedWindows(nodeId, clearSettingsRefs);
    }

    public static void dockBuilderRemoveNodeChildNodes(int nodeId) {
        nDockBuilderRemoveNodeChildNodes(nodeId);
    }

    public static void dockBuilderSetNodePos(int nodeId, ImVec2 pos) {
        nDockBuilderSetNodePos(nodeId, pos.x, pos.y);
    }

    public static void dockBuilderSetNodePos(int nodeId, float posX, float posY) {
        nDockBuilderSetNodePos(nodeId, posX, posY);
    }

    public static void dockBuilderSetNodeSize(int nodeId, ImVec2 size) {
        nDockBuilderSetNodeSize(nodeId, size.x, size.y);
    }

    public static void dockBuilderSetNodeSize(int nodeId, float sizeX, float sizeY) {
        nDockBuilderSetNodeSize(nodeId, sizeX, sizeY);
    }

    public static int dockBuilderSplitNode(int nodeId, int splitDir, float sizeRatioForNodeAtDir, ImInt outIdAtDir, ImInt outIdAtOppositeDir) {
        return nDockBuilderSplitNode(nodeId, splitDir, sizeRatioForNodeAtDir, outIdAtDir != null ? outIdAtDir.getData() : null, outIdAtOppositeDir != null ? outIdAtOppositeDir.getData() : null);
    }

    public static void dockBuilderCopyWindowSettings(String srcName, String dstName) {
        nDockBuilderCopyWindowSettings(srcName, dstName);
    }

    public static void dockBuilderFinish(int nodeId) {
        nDockBuilderFinish(nodeId);
    }

    public static void tableOpenContextMenu() {
        nTableOpenContextMenu();
    }

    public static void tableOpenContextMenu(int columnN) {
        nTableOpenContextMenu(columnN);
    }

    public static void tableSetColumnWidth(int columnN, float width) {
        nTableSetColumnWidth(columnN, width);
    }

    public static void tableSetColumnSortDirection(int columnN, int sortDirection, boolean appendToSortSpecs) {
        nTableSetColumnSortDirection(columnN, sortDirection, appendToSortSpecs);
    }

    public static int tableGetHoveredColumn() {
        return nTableGetHoveredColumn();
    }

    public static int tableGetHoveredRow() {
        return nTableGetHoveredRow();
    }

    public static float tableGetHeaderRowHeight() {
        return nTableGetHeaderRowHeight();
    }

    public static void tablePushBackgroundChannel() {
        nTablePushBackgroundChannel();
    }

    public static void tablePopBackgroundChannel() {
        nTablePopBackgroundChannel();
    }

    public static void textEx(String beginText) {
        nTextEx(beginText);
    }

    public static void textEx(String beginText, String endText) {
        nTextEx(beginText, endText);
    }

    public static void textEx(String beginText, String endText, int flags) {
        nTextEx(beginText, endText, flags);
    }

    public static boolean buttonEx(String label) {
        return nButtonEx(label);
    }

    public static boolean buttonEx(String label, ImVec2 size) {
        return nButtonEx(label, size.x, size.y);
    }

    public static boolean buttonEx(String label, float sizeX, float sizeY) {
        return nButtonEx(label, sizeX, sizeY);
    }

    public static boolean buttonEx(String label, ImVec2 size, int flags) {
        return nButtonEx(label, size.x, size.y, flags);
    }

    public static boolean buttonEx(String label, float sizeX, float sizeY, int flags) {
        return nButtonEx(label, sizeX, sizeY, flags);
    }

    public static boolean buttonEx(String label, int flags) {
        return nButtonEx(label, flags);
    }

    public static boolean arrowButtonEx(String strId, int dir, ImVec2 size) {
        return nArrowButtonEx(strId, dir, size.x, size.y);
    }

    public static boolean arrowButtonEx(String strId, int dir, float sizeX, float sizeY) {
        return nArrowButtonEx(strId, dir, sizeX, sizeY);
    }

    public static boolean arrowButtonEx(String strId, int dir, ImVec2 size, int flags) {
        return nArrowButtonEx(strId, dir, size.x, size.y, flags);
    }

    public static boolean arrowButtonEx(String strId, int dir, float sizeX, float sizeY, int flags) {
        return nArrowButtonEx(strId, dir, sizeX, sizeY, flags);
    }

    public static boolean imageButtonEx(int id, long textureId, ImVec2 size, ImVec2 uv0, ImVec2 uv1, ImVec4 bgCol, ImVec4 tintCol, int flags) {
        return nImageButtonEx(id, textureId, size.x, size.y, uv0.x, uv0.y, uv1.x, uv1.y, bgCol.x, bgCol.y, bgCol.z, bgCol.w, tintCol.x, tintCol.y, tintCol.z, tintCol.w, flags);
    }

    public static boolean imageButtonEx(int id, long textureId, float sizeX, float sizeY, float uv0X, float uv0Y, float uv1X, float uv1Y, float bgColX, float bgColY, float bgColZ, float bgColW, float tintColX, float tintColY, float tintColZ, float tintColW, int flags) {
        return nImageButtonEx(id, textureId, sizeX, sizeY, uv0X, uv0Y, uv1X, uv1Y, bgColX, bgColY, bgColZ, bgColW, tintColX, tintColY, tintColZ, tintColW, flags);
    }

    public static void separatorEx(int flags) {
        nSeparatorEx(flags);
    }

    public static void separatorEx(int flags, float thickness) {
        nSeparatorEx(flags, thickness);
    }

    public static void separatorTextEx(int id, String label, String labelEnd, float extraWidth) {
        nSeparatorTextEx(id, label, labelEnd, extraWidth);
    }

    public static boolean closeButton(int id, ImVec2 pos) {
        return nCloseButton(id, pos.x, pos.y);
    }

    public static boolean closeButton(int id, float posX, float posY) {
        return nCloseButton(id, posX, posY);
    }

    public static boolean collapseButton(int id, ImVec2 pos, ImGuiDockNode dockNode) {
        return nCollapseButton(id, pos.x, pos.y, dockNode.ptr);
    }

    public static boolean collapseButton(int id, float posX, float posY, ImGuiDockNode dockNode) {
        return nCollapseButton(id, posX, posY, dockNode.ptr);
    }

    public static void scrollbar(int axis) {
        nScrollbar(axis);
    }

    public static ImRect getWindowScrollbarRect(ImGuiWindow imGuiWindow, int axis) {
        ImRect dst = new ImRect();
        nGetWindowScrollbarRect(dst, imGuiWindow.ptr, axis);
        return dst;
    }

    public static void getWindowScrollbarRect(ImRect dst, ImGuiWindow imGuiWindow, int axis) {
        nGetWindowScrollbarRect(dst, imGuiWindow.ptr, axis);
    }

    public static int getWindowScrollbarID(ImGuiWindow window, int axis) {
        return nGetWindowScrollbarID(window.ptr, axis);
    }

    public static int getWindowResizeCornerID(ImGuiWindow window, int n) {
        return nGetWindowResizeCornerID(window.ptr, n);
    }

    public static int getWindowResizeBorderID(ImGuiWindow window, int dir) {
        return nGetWindowResizeBorderID(window.ptr, dir);
    }

    public static boolean buttonBehavior(ImRect bb, int id, ImBoolean outHovered, ImBoolean outHeld) {
        return nButtonBehavior(bb.min.x, bb.min.y, bb.max.x, bb.max.y, id, outHovered != null ? outHovered.getData() : null, outHeld != null ? outHeld.getData() : null);
    }

    public static boolean buttonBehavior(float bbMinX, float bbMinY, float bbMaxX, float bbMaxY, int id, ImBoolean outHovered, ImBoolean outHeld) {
        return nButtonBehavior(bbMinX, bbMinY, bbMaxX, bbMaxY, id, outHovered != null ? outHovered.getData() : null, outHeld != null ? outHeld.getData() : null);
    }

    public static boolean buttonBehavior(ImRect bb, int id, ImBoolean outHovered, ImBoolean outHeld, int imGuiButtonFlags) {
        return nButtonBehavior(bb.min.x, bb.min.y, bb.max.x, bb.max.y, id, outHovered != null ? outHovered.getData() : null, outHeld != null ? outHeld.getData() : null, imGuiButtonFlags);
    }

    public static boolean buttonBehavior(float bbMinX, float bbMinY, float bbMaxX, float bbMaxY, int id, ImBoolean outHovered, ImBoolean outHeld, int imGuiButtonFlags) {
        return nButtonBehavior(bbMinX, bbMinY, bbMaxX, bbMaxY, id, outHovered != null ? outHovered.getData() : null, outHeld != null ? outHeld.getData() : null, imGuiButtonFlags);
    }

    public static boolean splitterBehavior(ImRect bb, int id, int axis, ImFloat size1, ImFloat size2, float minSize1, float minSize2) {
        return nSplitterBehavior(bb.min.x, bb.min.y, bb.max.x, bb.max.y, id, axis, size1 != null ? size1.getData() : null, size2 != null ? size2.getData() : null, minSize1, minSize2);
    }

    public static boolean splitterBehavior(float bbMinX, float bbMinY, float bbMaxX, float bbMaxY, int id, int axis, ImFloat size1, ImFloat size2, float minSize1, float minSize2) {
        return nSplitterBehavior(bbMinX, bbMinY, bbMaxX, bbMaxY, id, axis, size1 != null ? size1.getData() : null, size2 != null ? size2.getData() : null, minSize1, minSize2);
    }

    public static boolean splitterBehavior(ImRect bb, int id, int axis, ImFloat size1, ImFloat size2, float minSize1, float minSize2, float hoverExtend) {
        return nSplitterBehavior(bb.min.x, bb.min.y, bb.max.x, bb.max.y, id, axis, size1 != null ? size1.getData() : null, size2 != null ? size2.getData() : null, minSize1, minSize2, hoverExtend);
    }

    public static boolean splitterBehavior(float bbMinX, float bbMinY, float bbMaxX, float bbMaxY, int id, int axis, ImFloat size1, ImFloat size2, float minSize1, float minSize2, float hoverExtend) {
        return nSplitterBehavior(bbMinX, bbMinY, bbMaxX, bbMaxY, id, axis, size1 != null ? size1.getData() : null, size2 != null ? size2.getData() : null, minSize1, minSize2, hoverExtend);
    }

    public static boolean splitterBehavior(ImRect bb, int id, int axis, ImFloat size1, ImFloat size2, float minSize1, float minSize2, float hoverExtend, float hoverVisibilityDelay) {
        return nSplitterBehavior(bb.min.x, bb.min.y, bb.max.x, bb.max.y, id, axis, size1 != null ? size1.getData() : null, size2 != null ? size2.getData() : null, minSize1, minSize2, hoverExtend, hoverVisibilityDelay);
    }

    public static boolean splitterBehavior(float bbMinX, float bbMinY, float bbMaxX, float bbMaxY, int id, int axis, ImFloat size1, ImFloat size2, float minSize1, float minSize2, float hoverExtend, float hoverVisibilityDelay) {
        return nSplitterBehavior(bbMinX, bbMinY, bbMaxX, bbMaxY, id, axis, size1 != null ? size1.getData() : null, size2 != null ? size2.getData() : null, minSize1, minSize2, hoverExtend, hoverVisibilityDelay);
    }

    public static boolean splitterBehavior(ImRect bb, int id, int axis, ImFloat size1, ImFloat size2, float minSize1, float minSize2, float hoverExtend, float hoverVisibilityDelay, int bgCol) {
        return nSplitterBehavior(bb.min.x, bb.min.y, bb.max.x, bb.max.y, id, axis, size1 != null ? size1.getData() : null, size2 != null ? size2.getData() : null, minSize1, minSize2, hoverExtend, hoverVisibilityDelay, bgCol);
    }

    public static boolean splitterBehavior(float bbMinX, float bbMinY, float bbMaxX, float bbMaxY, int id, int axis, ImFloat size1, ImFloat size2, float minSize1, float minSize2, float hoverExtend, float hoverVisibilityDelay, int bgCol) {
        return nSplitterBehavior(bbMinX, bbMinY, bbMaxX, bbMaxY, id, axis, size1 != null ? size1.getData() : null, size2 != null ? size2.getData() : null, minSize1, minSize2, hoverExtend, hoverVisibilityDelay, bgCol);
    }

    public static void shadeVertsTransformPos(ImDrawList drawList, int vertStartIdx, int vertEndIdx, ImVec2 pivotIn, float cosA, float sinA, ImVec2 pivotOut) {
        nShadeVertsTransformPos(drawList.ptr, vertStartIdx, vertEndIdx, pivotIn.x, pivotIn.y, cosA, sinA, pivotOut.x, pivotOut.y);
    }

    public static void shadeVertsTransformPos(ImDrawList drawList, int vertStartIdx, int vertEndIdx, float pivotInX, float pivotInY, float cosA, float sinA, float pivotOutX, float pivotOutY) {
        nShadeVertsTransformPos(drawList.ptr, vertStartIdx, vertEndIdx, pivotInX, pivotInY, cosA, sinA, pivotOutX, pivotOutY);
    }
}
