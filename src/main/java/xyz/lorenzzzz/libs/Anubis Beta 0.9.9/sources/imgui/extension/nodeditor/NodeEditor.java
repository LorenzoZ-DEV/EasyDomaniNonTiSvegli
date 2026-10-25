package imgui.extension.nodeditor;

import imgui.ImDrawList;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.type.ImLong;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/extension/nodeditor/NodeEditor.class */
public final class NodeEditor {
    private static final NodeEditorStyle _GETSTYLE_1 = new NodeEditorStyle(0);
    private static final ImDrawList _GETHINTFOREGROUNDDRAWLIST_1 = new ImDrawList(0);
    private static final ImDrawList _GETHINTBACKGROUNDDRAWLIST_1 = new ImDrawList(0);
    private static final ImDrawList _GETNODEBACKGROUNDDRAWLIST_1040171299 = new ImDrawList(0);

    private static native void nSetCurrentEditor(long j);

    private static native long nGetCurrentEditor();

    private static native long nCreateEditor();

    private static native long nCreateEditor(long j);

    private static native void nDestroyEditor(long j);

    private static native long nGetStyle();

    private static native String nGetStyleColorName(int i);

    private static native void nPushStyleColor(int i, float f, float f2, float f3, float f4);

    private static native void nPopStyleColor();

    private static native void nPopStyleColor(int i);

    private static native void nPushStyleVar(int i, float f);

    private static native void nPushStyleVar(int i, float f, float f2);

    private static native void nPushStyleVar(int i, float f, float f2, float f3, float f4);

    private static native void nPopStyleVar();

    private static native void nPopStyleVar(int i);

    private static native void nBegin(String str);

    private static native void nBegin(String str, float f, float f2);

    private static native void nEnd();

    private static native void nBeginNode(long j);

    private static native void nBeginPin(long j, int i);

    private static native void nPinRect(float f, float f2, float f3, float f4);

    private static native void nPinPivotRect(float f, float f2, float f3, float f4);

    private static native void nPinPivotSize(float f, float f2);

    private static native void nPinPivotScale(float f, float f2);

    private static native void nPinPivotAlignment(float f, float f2);

    private static native void nEndPin();

    private static native void nGroup(float f, float f2);

    private static native void nEndNode();

    private static native boolean nBeginGroupHint(long j);

    private static native void nGetGroupMin(ImVec2 imVec2);

    private static native float nGetGroupMinX();

    private static native float nGetGroupMinY();

    private static native void nGetGroupMax(ImVec2 imVec2);

    private static native float nGetGroupMaxX();

    private static native float nGetGroupMaxY();

    private static native long nGetHintForegroundDrawList();

    private static native long nGetHintBackgroundDrawList();

    private static native void nEndGroupHint();

    private static native long nGetNodeBackgroundDrawList(long j);

    private static native boolean nLink(long j, long j2, long j3);

    private static native boolean nLink(long j, long j2, long j3, float f, float f2, float f3, float f4);

    private static native boolean nLink(long j, long j2, long j3, float f, float f2, float f3, float f4, float f5);

    private static native boolean nLink(long j, long j2, long j3, float f);

    private static native void nFlow(long j);

    private static native void nFlow(long j, int i);

    private static native boolean nBeginCreate();

    private static native boolean nBeginCreate(float f, float f2, float f3, float f4);

    private static native boolean nBeginCreate(float f, float f2, float f3, float f4, float f5);

    private static native boolean nBeginCreate(float f);

    private static native boolean nQueryNewLink(long[] jArr, long[] jArr2);

    private static native boolean nQueryNewLink(long[] jArr, long[] jArr2, float f, float f2, float f3, float f4);

    private static native boolean nQueryNewLink(long[] jArr, long[] jArr2, float f, float f2, float f3, float f4, float f5);

    private static native boolean nQueryNewNode(long[] jArr);

    private static native boolean nQueryNewNode(long[] jArr, float f, float f2, float f3, float f4);

    private static native boolean nQueryNewNode(long[] jArr, float f, float f2, float f3, float f4, float f5);

    private static native boolean nAcceptNewItem();

    private static native boolean nAcceptNewItem(float f, float f2, float f3, float f4);

    private static native boolean nAcceptNewItem(float f, float f2, float f3, float f4, float f5);

    private static native void nRejectNewItem();

    private static native void nRejectNewItem(float f, float f2, float f3, float f4);

    private static native void nRejectNewItem(float f, float f2, float f3, float f4, float f5);

    private static native void nEndCreate();

    private static native boolean nBeginDelete();

    private static native boolean nQueryDeletedLink(long[] jArr);

    private static native boolean nQueryDeletedLink(long[] jArr, long[] jArr2);

    private static native boolean nQueryDeletedLink(long[] jArr, long[] jArr2, long[] jArr3);

    private static native boolean nQueryDeletedNode(long[] jArr);

    private static native boolean nAcceptDeletedItem();

    private static native boolean nAcceptDeletedItem(boolean z);

    private static native void nRejectDeletedItem();

    private static native void nEndDelete();

    private static native void nSetNodePosition(long j, float f, float f2);

    private static native void nSetGroupSize(long j, float f, float f2);

    private static native void nGetNodePosition(ImVec2 imVec2, long j);

    private static native float nGetNodePositionX(long j);

    private static native float nGetNodePositionY(long j);

    private static native void nGetNodeSize(ImVec2 imVec2, long j);

    private static native float nGetNodeSizeX(long j);

    private static native float nGetNodeSizeY(long j);

    private static native void nCenterNodeOnScreen(long j);

    private static native void nSetNodeZPosition(long j, float f);

    private static native float nGetNodeZPosition(long j);

    private static native void nRestoreNodeState(long j);

    private static native void nSuspend();

    private static native void nResume();

    private static native boolean nIsSuspended();

    private static native boolean nIsActive();

    private static native boolean nHasSelectionChanged();

    private static native int nGetSelectedObjectCount();

    private static native int nGetSelectedNodes(long[] jArr, int i);

    private static native int nGetSelectedLinks(long[] jArr, int i);

    private static native boolean nIsNodeSelected(long j);

    private static native boolean nIsLinkSelected(long j);

    private static native void nClearSelection();

    private static native void nSelectNode(long j);

    private static native void nSelectNode(long j, boolean z);

    private static native void nSelectLink(long j);

    private static native void nSelectLink(long j, boolean z);

    private static native void nDeselectNode(long j);

    private static native void nDeselectLink(long j);

    private static native boolean nDeleteNode(long j);

    private static native boolean nDeleteLink(long j);

    private static native boolean nHasAnyLinksNode(long j);

    private static native boolean nHasAnyLinksPin(long j);

    private static native int nBreakLinksNode(long j);

    private static native int nBreakLinksPin(long j);

    private static native void nNavigateToContent();

    private static native void nNavigateToContent(float f);

    private static native void nNavigateToSelection();

    private static native void nNavigateToSelection(boolean z);

    private static native void nNavigateToSelection(boolean z, float f);

    private static native void nNavigateToSelection(float f);

    private static native boolean nShowNodeContextMenu(long[] jArr);

    private static native boolean nShowPinContextMenu(long[] jArr);

    private static native boolean nShowLinkContextMenu(long[] jArr);

    public static native long getNodeWithContextMenu();

    public static native long getPinWithContextMenu();

    public static native long getLinkWithContextMenu();

    private static native boolean nShowBackgroundContextMenu();

    private static native void nEnableShortcuts(boolean z);

    private static native boolean nAreShortcutsEnabled();

    private static native boolean nBeginShortcut();

    private static native boolean nAcceptCut();

    private static native boolean nAcceptCopy();

    private static native boolean nAcceptPaste();

    private static native boolean nAcceptDuplicate();

    private static native boolean nAcceptCreateNode();

    private static native int nGetActionContextSize();

    private static native int nGetActionContextNodes(long[] jArr, int i);

    private static native int nGetActionContextLinks(long[] jArr, int i);

    private static native void nEndShortcut();

    private static native float nGetCurrentZoom();

    private static native long nGetHoveredNode();

    private static native long nGetHoveredPin();

    private static native long nGetHoveredLink();

    private static native long nGetDoubleClickedNode();

    private static native long nGetDoubleClickedPin();

    private static native long nGetDoubleClickedLink();

    private static native boolean nIsBackgroundClicked();

    private static native boolean nIsBackgroundDoubleClicked();

    private static native boolean nGetLinkPins(long j, long[] jArr, long[] jArr2);

    private static native boolean nPinHadAnyLinks(long j);

    private static native void nGetScreenSize(ImVec2 imVec2);

    private static native float nGetScreenSizeX();

    private static native float nGetScreenSizeY();

    private static native void nScreenToCanvas(ImVec2 imVec2, float f, float f2);

    private static native float nScreenToCanvasX(float f, float f2);

    private static native float nScreenToCanvasY(float f, float f2);

    private static native void nCanvasToScreen(ImVec2 imVec2, float f, float f2);

    private static native float nCanvasToScreenX(float f, float f2);

    private static native float nCanvasToScreenY(float f, float f2);

    private static native int nGetNodeCount();

    private static native int nGetOrderedNodeIds(long[] jArr, int i);

    private NodeEditor() {
    }

    public static void setCurrentEditor(NodeEditorContext ctx) {
        nSetCurrentEditor(ctx.ptr);
    }

    public static NodeEditorContext getCurrentEditor() {
        return new NodeEditorContext(nGetCurrentEditor());
    }

    public static NodeEditorContext createEditor() {
        return new NodeEditorContext(nCreateEditor());
    }

    public static NodeEditorContext createEditor(NodeEditorConfig config) {
        return new NodeEditorContext(nCreateEditor(config.ptr));
    }

    public static void destroyEditor(NodeEditorContext ctx) {
        nDestroyEditor(ctx.ptr);
    }

    public static NodeEditorStyle getStyle() {
        _GETSTYLE_1.ptr = nGetStyle();
        return _GETSTYLE_1;
    }

    public static String getStyleColorName(int colorIndex) {
        return nGetStyleColorName(colorIndex);
    }

    public static void pushStyleColor(int colorIndex, ImVec4 color) {
        nPushStyleColor(colorIndex, color.x, color.y, color.z, color.w);
    }

    public static void pushStyleColor(int colorIndex, float colorX, float colorY, float colorZ, float colorW) {
        nPushStyleColor(colorIndex, colorX, colorY, colorZ, colorW);
    }

    public static void popStyleColor() {
        nPopStyleColor();
    }

    public static void popStyleColor(int count) {
        nPopStyleColor(count);
    }

    public static void pushStyleVar(int varIndex, float value) {
        nPushStyleVar(varIndex, value);
    }

    public static void pushStyleVar(int varIndex, ImVec2 value) {
        nPushStyleVar(varIndex, value.x, value.y);
    }

    public static void pushStyleVar(int varIndex, float valueX, float valueY) {
        nPushStyleVar(varIndex, valueX, valueY);
    }

    public static void pushStyleVar(int varIndex, ImVec4 value) {
        nPushStyleVar(varIndex, value.x, value.y, value.z, value.w);
    }

    public static void pushStyleVar(int varIndex, float valueX, float valueY, float valueZ, float valueW) {
        nPushStyleVar(varIndex, valueX, valueY, valueZ, valueW);
    }

    public static void popStyleVar() {
        nPopStyleVar();
    }

    public static void popStyleVar(int count) {
        nPopStyleVar(count);
    }

    public static void begin(String id) {
        nBegin(id);
    }

    public static void begin(String id, ImVec2 size) {
        nBegin(id, size.x, size.y);
    }

    public static void begin(String id, float sizeX, float sizeY) {
        nBegin(id, sizeX, sizeY);
    }

    public static void end() {
        nEnd();
    }

    public static void beginNode(long id) {
        nBeginNode(id);
    }

    public static void beginPin(long id, int kind) {
        nBeginPin(id, kind);
    }

    public static void pinRect(ImVec2 a, ImVec2 b) {
        nPinRect(a.x, a.y, b.x, b.y);
    }

    public static void pinRect(float aX, float aY, float bX, float bY) {
        nPinRect(aX, aY, bX, bY);
    }

    public static void pinPivotRect(ImVec2 a, ImVec2 b) {
        nPinPivotRect(a.x, a.y, b.x, b.y);
    }

    public static void pinPivotRect(float aX, float aY, float bX, float bY) {
        nPinPivotRect(aX, aY, bX, bY);
    }

    public static void pinPivotSize(ImVec2 size) {
        nPinPivotSize(size.x, size.y);
    }

    public static void pinPivotSize(float sizeX, float sizeY) {
        nPinPivotSize(sizeX, sizeY);
    }

    public static void pinPivotScale(ImVec2 scale) {
        nPinPivotScale(scale.x, scale.y);
    }

    public static void pinPivotScale(float scaleX, float scaleY) {
        nPinPivotScale(scaleX, scaleY);
    }

    public static void pinPivotAlignment(ImVec2 alignment) {
        nPinPivotAlignment(alignment.x, alignment.y);
    }

    public static void pinPivotAlignment(float alignmentX, float alignmentY) {
        nPinPivotAlignment(alignmentX, alignmentY);
    }

    public static void endPin() {
        nEndPin();
    }

    public static void group(ImVec2 size) {
        nGroup(size.x, size.y);
    }

    public static void group(float sizeX, float sizeY) {
        nGroup(sizeX, sizeY);
    }

    public static void endNode() {
        nEndNode();
    }

    public static boolean beginGroupHint(long nodeId) {
        return nBeginGroupHint(nodeId);
    }

    public static ImVec2 getGroupMin() {
        ImVec2 dst = new ImVec2();
        nGetGroupMin(dst);
        return dst;
    }

    public static float getGroupMinX() {
        return nGetGroupMinX();
    }

    public static float getGroupMinY() {
        return nGetGroupMinY();
    }

    public static void getGroupMin(ImVec2 dst) {
        nGetGroupMin(dst);
    }

    public static ImVec2 getGroupMax() {
        ImVec2 dst = new ImVec2();
        nGetGroupMax(dst);
        return dst;
    }

    public static float getGroupMaxX() {
        return nGetGroupMaxX();
    }

    public static float getGroupMaxY() {
        return nGetGroupMaxY();
    }

    public static void getGroupMax(ImVec2 dst) {
        nGetGroupMax(dst);
    }

    public static ImDrawList getHintForegroundDrawList() {
        _GETHINTFOREGROUNDDRAWLIST_1.ptr = nGetHintForegroundDrawList();
        return _GETHINTFOREGROUNDDRAWLIST_1;
    }

    public static ImDrawList getHintBackgroundDrawList() {
        _GETHINTBACKGROUNDDRAWLIST_1.ptr = nGetHintBackgroundDrawList();
        return _GETHINTBACKGROUNDDRAWLIST_1;
    }

    public static void endGroupHint() {
        nEndGroupHint();
    }

    public static ImDrawList getNodeBackgroundDrawList(long nodeId) {
        _GETNODEBACKGROUNDDRAWLIST_1040171299.ptr = nGetNodeBackgroundDrawList(nodeId);
        return _GETNODEBACKGROUNDDRAWLIST_1040171299;
    }

    public static boolean link(long id, long startPinId, long endPinId) {
        return nLink(id, startPinId, endPinId);
    }

    public static boolean link(long id, long startPinId, long endPinId, ImVec4 color) {
        return nLink(id, startPinId, endPinId, color.x, color.y, color.z, color.w);
    }

    public static boolean link(long id, long startPinId, long endPinId, float colorX, float colorY, float colorZ, float colorW) {
        return nLink(id, startPinId, endPinId, colorX, colorY, colorZ, colorW);
    }

    public static boolean link(long id, long startPinId, long endPinId, ImVec4 color, float thickness) {
        return nLink(id, startPinId, endPinId, color.x, color.y, color.z, color.w, thickness);
    }

    public static boolean link(long id, long startPinId, long endPinId, float colorX, float colorY, float colorZ, float colorW, float thickness) {
        return nLink(id, startPinId, endPinId, colorX, colorY, colorZ, colorW, thickness);
    }

    public static boolean link(long id, long startPinId, long endPinId, float thickness) {
        return nLink(id, startPinId, endPinId, thickness);
    }

    public static void flow(long linkId) {
        nFlow(linkId);
    }

    public static void flow(long linkId, int direction) {
        nFlow(linkId, direction);
    }

    public static boolean beginCreate() {
        return nBeginCreate();
    }

    public static boolean beginCreate(ImVec4 color) {
        return nBeginCreate(color.x, color.y, color.z, color.w);
    }

    public static boolean beginCreate(float colorX, float colorY, float colorZ, float colorW) {
        return nBeginCreate(colorX, colorY, colorZ, colorW);
    }

    public static boolean beginCreate(ImVec4 color, float thickness) {
        return nBeginCreate(color.x, color.y, color.z, color.w, thickness);
    }

    public static boolean beginCreate(float colorX, float colorY, float colorZ, float colorW, float thickness) {
        return nBeginCreate(colorX, colorY, colorZ, colorW, thickness);
    }

    public static boolean beginCreate(float thickness) {
        return nBeginCreate(thickness);
    }

    public static boolean queryNewLink(ImLong startId, ImLong endId) {
        return nQueryNewLink(startId != null ? startId.getData() : null, endId != null ? endId.getData() : null);
    }

    public static boolean queryNewLink(ImLong startId, ImLong endId, ImVec4 color) {
        return nQueryNewLink(startId != null ? startId.getData() : null, endId != null ? endId.getData() : null, color.x, color.y, color.z, color.w);
    }

    public static boolean queryNewLink(ImLong startId, ImLong endId, float colorX, float colorY, float colorZ, float colorW) {
        return nQueryNewLink(startId != null ? startId.getData() : null, endId != null ? endId.getData() : null, colorX, colorY, colorZ, colorW);
    }

    public static boolean queryNewLink(ImLong startId, ImLong endId, ImVec4 color, float thickness) {
        return nQueryNewLink(startId != null ? startId.getData() : null, endId != null ? endId.getData() : null, color.x, color.y, color.z, color.w, thickness);
    }

    public static boolean queryNewLink(ImLong startId, ImLong endId, float colorX, float colorY, float colorZ, float colorW, float thickness) {
        return nQueryNewLink(startId != null ? startId.getData() : null, endId != null ? endId.getData() : null, colorX, colorY, colorZ, colorW, thickness);
    }

    public static boolean queryNewNode(ImLong pinId) {
        return nQueryNewNode(pinId != null ? pinId.getData() : null);
    }

    public static boolean queryNewNode(ImLong pinId, ImVec4 color) {
        return nQueryNewNode(pinId != null ? pinId.getData() : null, color.x, color.y, color.z, color.w);
    }

    public static boolean queryNewNode(ImLong pinId, float colorX, float colorY, float colorZ, float colorW) {
        return nQueryNewNode(pinId != null ? pinId.getData() : null, colorX, colorY, colorZ, colorW);
    }

    public static boolean queryNewNode(ImLong pinId, ImVec4 color, float thickness) {
        return nQueryNewNode(pinId != null ? pinId.getData() : null, color.x, color.y, color.z, color.w, thickness);
    }

    public static boolean queryNewNode(ImLong pinId, float colorX, float colorY, float colorZ, float colorW, float thickness) {
        return nQueryNewNode(pinId != null ? pinId.getData() : null, colorX, colorY, colorZ, colorW, thickness);
    }

    public static boolean acceptNewItem() {
        return nAcceptNewItem();
    }

    public static boolean acceptNewItem(ImVec4 color) {
        return nAcceptNewItem(color.x, color.y, color.z, color.w);
    }

    public static boolean acceptNewItem(float colorX, float colorY, float colorZ, float colorW) {
        return nAcceptNewItem(colorX, colorY, colorZ, colorW);
    }

    public static boolean acceptNewItem(ImVec4 color, float thickness) {
        return nAcceptNewItem(color.x, color.y, color.z, color.w, thickness);
    }

    public static boolean acceptNewItem(float colorX, float colorY, float colorZ, float colorW, float thickness) {
        return nAcceptNewItem(colorX, colorY, colorZ, colorW, thickness);
    }

    public static void rejectNewItem() {
        nRejectNewItem();
    }

    public static void rejectNewItem(ImVec4 color) {
        nRejectNewItem(color.x, color.y, color.z, color.w);
    }

    public static void rejectNewItem(float colorX, float colorY, float colorZ, float colorW) {
        nRejectNewItem(colorX, colorY, colorZ, colorW);
    }

    public static void rejectNewItem(ImVec4 color, float thickness) {
        nRejectNewItem(color.x, color.y, color.z, color.w, thickness);
    }

    public static void rejectNewItem(float colorX, float colorY, float colorZ, float colorW, float thickness) {
        nRejectNewItem(colorX, colorY, colorZ, colorW, thickness);
    }

    public static void endCreate() {
        nEndCreate();
    }

    public static boolean beginDelete() {
        return nBeginDelete();
    }

    public static boolean queryDeletedLink(ImLong linkId) {
        return nQueryDeletedLink(linkId != null ? linkId.getData() : null);
    }

    public static boolean queryDeletedLink(ImLong linkId, ImLong startId) {
        return nQueryDeletedLink(linkId != null ? linkId.getData() : null, startId != null ? startId.getData() : null);
    }

    public static boolean queryDeletedLink(ImLong linkId, ImLong startId, ImLong endId) {
        return nQueryDeletedLink(linkId != null ? linkId.getData() : null, startId != null ? startId.getData() : null, endId != null ? endId.getData() : null);
    }

    public static boolean queryDeletedNode(ImLong nodeId) {
        return nQueryDeletedNode(nodeId != null ? nodeId.getData() : null);
    }

    public static boolean acceptDeletedItem() {
        return nAcceptDeletedItem();
    }

    public static boolean acceptDeletedItem(boolean deleteDependencies) {
        return nAcceptDeletedItem(deleteDependencies);
    }

    public static void rejectDeletedItem() {
        nRejectDeletedItem();
    }

    public static void endDelete() {
        nEndDelete();
    }

    public static void setNodePosition(long nodeId, ImVec2 editorPosition) {
        nSetNodePosition(nodeId, editorPosition.x, editorPosition.y);
    }

    public static void setNodePosition(long nodeId, float editorPositionX, float editorPositionY) {
        nSetNodePosition(nodeId, editorPositionX, editorPositionY);
    }

    public static void setGroupSize(long nodeId, ImVec2 size) {
        nSetGroupSize(nodeId, size.x, size.y);
    }

    public static void setGroupSize(long nodeId, float sizeX, float sizeY) {
        nSetGroupSize(nodeId, sizeX, sizeY);
    }

    public static ImVec2 getNodePosition(long nodeId) {
        ImVec2 dst = new ImVec2();
        nGetNodePosition(dst, nodeId);
        return dst;
    }

    public static float getNodePositionX(long nodeId) {
        return nGetNodePositionX(nodeId);
    }

    public static float getNodePositionY(long nodeId) {
        return nGetNodePositionY(nodeId);
    }

    public static void getNodePosition(ImVec2 dst, long nodeId) {
        nGetNodePosition(dst, nodeId);
    }

    public static ImVec2 getNodeSize(long nodeId) {
        ImVec2 dst = new ImVec2();
        nGetNodeSize(dst, nodeId);
        return dst;
    }

    public static float getNodeSizeX(long nodeId) {
        return nGetNodeSizeX(nodeId);
    }

    public static float getNodeSizeY(long nodeId) {
        return nGetNodeSizeY(nodeId);
    }

    public static void getNodeSize(ImVec2 dst, long nodeId) {
        nGetNodeSize(dst, nodeId);
    }

    public static void centerNodeOnScreen(long nodeId) {
        nCenterNodeOnScreen(nodeId);
    }

    public static void setNodeZPosition(long nodeId, float z) {
        nSetNodeZPosition(nodeId, z);
    }

    public static float getNodeZPosition(long nodeId) {
        return nGetNodeZPosition(nodeId);
    }

    public static void restoreNodeState(long nodeId) {
        nRestoreNodeState(nodeId);
    }

    public static void suspend() {
        nSuspend();
    }

    public static void resume() {
        nResume();
    }

    public static boolean isSuspended() {
        return nIsSuspended();
    }

    public static boolean isActive() {
        return nIsActive();
    }

    public static boolean hasSelectionChanged() {
        return nHasSelectionChanged();
    }

    public static int getSelectedObjectCount() {
        return nGetSelectedObjectCount();
    }

    public static int getSelectedNodes(long[] nodes, int size) {
        return nGetSelectedNodes(nodes, size);
    }

    public static int getSelectedLinks(long[] links, int size) {
        return nGetSelectedLinks(links, size);
    }

    public static boolean isNodeSelected(long nodeId) {
        return nIsNodeSelected(nodeId);
    }

    public static boolean isLinkSelected(long linkId) {
        return nIsLinkSelected(linkId);
    }

    public static void clearSelection() {
        nClearSelection();
    }

    public static void selectNode(long nodeId) {
        nSelectNode(nodeId);
    }

    public static void selectNode(long nodeId, boolean append) {
        nSelectNode(nodeId, append);
    }

    public static void selectLink(long linkId) {
        nSelectLink(linkId);
    }

    public static void selectLink(long linkId, boolean append) {
        nSelectLink(linkId, append);
    }

    public static void deselectNode(long nodeId) {
        nDeselectNode(nodeId);
    }

    public static void deselectLink(long linkId) {
        nDeselectLink(linkId);
    }

    public static boolean deleteNode(long nodeId) {
        return nDeleteNode(nodeId);
    }

    public static boolean deleteLink(long linkId) {
        return nDeleteLink(linkId);
    }

    public static boolean hasAnyLinksNode(long nodeId) {
        return nHasAnyLinksNode(nodeId);
    }

    public static boolean hasAnyLinksPin(long pinId) {
        return nHasAnyLinksPin(pinId);
    }

    public static int breakLinksNode(long nodeId) {
        return nBreakLinksNode(nodeId);
    }

    public static int breakLinksPin(long pinId) {
        return nBreakLinksPin(pinId);
    }

    public static void navigateToContent() {
        nNavigateToContent();
    }

    public static void navigateToContent(float duration) {
        nNavigateToContent(duration);
    }

    public static void navigateToSelection() {
        nNavigateToSelection();
    }

    public static void navigateToSelection(boolean zoomIn) {
        nNavigateToSelection(zoomIn);
    }

    public static void navigateToSelection(boolean zoomIn, float duration) {
        nNavigateToSelection(zoomIn, duration);
    }

    public static void navigateToSelection(float duration) {
        nNavigateToSelection(duration);
    }

    public static boolean showNodeContextMenu(ImLong nodeId) {
        return nShowNodeContextMenu(nodeId != null ? nodeId.getData() : null);
    }

    public static boolean showPinContextMenu(ImLong pinId) {
        return nShowPinContextMenu(pinId != null ? pinId.getData() : null);
    }

    public static boolean showLinkContextMenu(ImLong linkId) {
        return nShowLinkContextMenu(linkId != null ? linkId.getData() : null);
    }

    public static boolean showBackgroundContextMenu() {
        return nShowBackgroundContextMenu();
    }

    public static void enableShortcuts(boolean enable) {
        nEnableShortcuts(enable);
    }

    public static boolean areShortcutsEnabled() {
        return nAreShortcutsEnabled();
    }

    public static boolean beginShortcut() {
        return nBeginShortcut();
    }

    public static boolean acceptCut() {
        return nAcceptCut();
    }

    public static boolean acceptCopy() {
        return nAcceptCopy();
    }

    public static boolean acceptPaste() {
        return nAcceptPaste();
    }

    public static boolean acceptDuplicate() {
        return nAcceptDuplicate();
    }

    public static boolean acceptCreateNode() {
        return nAcceptCreateNode();
    }

    public static int getActionContextSize() {
        return nGetActionContextSize();
    }

    public static int getActionContextNodes(long[] nodes, int size) {
        return nGetActionContextNodes(nodes, size);
    }

    public static int getActionContextLinks(long[] links, int size) {
        return nGetActionContextLinks(links, size);
    }

    public static void endShortcut() {
        nEndShortcut();
    }

    public static float getCurrentZoom() {
        return nGetCurrentZoom();
    }

    public static long getHoveredNode() {
        return nGetHoveredNode();
    }

    public static long getHoveredPin() {
        return nGetHoveredPin();
    }

    public static long getHoveredLink() {
        return nGetHoveredLink();
    }

    public static long getDoubleClickedNode() {
        return nGetDoubleClickedNode();
    }

    public static long getDoubleClickedPin() {
        return nGetDoubleClickedPin();
    }

    public static long getDoubleClickedLink() {
        return nGetDoubleClickedLink();
    }

    public static boolean isBackgroundClicked() {
        return nIsBackgroundClicked();
    }

    public static boolean isBackgroundDoubleClicked() {
        return nIsBackgroundDoubleClicked();
    }

    public static boolean getLinkPins(long linkId, ImLong startPinId, ImLong endPinId) {
        return nGetLinkPins(linkId, startPinId != null ? startPinId.getData() : null, endPinId != null ? endPinId.getData() : null);
    }

    public static boolean pinHadAnyLinks(long pinId) {
        return nPinHadAnyLinks(pinId);
    }

    public static ImVec2 getScreenSize() {
        ImVec2 dst = new ImVec2();
        nGetScreenSize(dst);
        return dst;
    }

    public static float getScreenSizeX() {
        return nGetScreenSizeX();
    }

    public static float getScreenSizeY() {
        return nGetScreenSizeY();
    }

    public static void getScreenSize(ImVec2 dst) {
        nGetScreenSize(dst);
    }

    public static ImVec2 screenToCanvas(ImVec2 pos) {
        ImVec2 dst = new ImVec2();
        nScreenToCanvas(dst, pos.x, pos.y);
        return dst;
    }

    public static ImVec2 screenToCanvas(float posX, float posY) {
        ImVec2 dst = new ImVec2();
        nScreenToCanvas(dst, posX, posY);
        return dst;
    }

    public static float screenToCanvasX(ImVec2 pos) {
        return nScreenToCanvasX(pos.x, pos.y);
    }

    public static float screenToCanvasY(ImVec2 pos) {
        return nScreenToCanvasY(pos.x, pos.y);
    }

    public static void screenToCanvas(ImVec2 dst, ImVec2 pos) {
        nScreenToCanvas(dst, pos.x, pos.y);
    }

    public static void screenToCanvas(ImVec2 dst, float posX, float posY) {
        nScreenToCanvas(dst, posX, posY);
    }

    public static ImVec2 canvasToScreen(ImVec2 pos) {
        ImVec2 dst = new ImVec2();
        nCanvasToScreen(dst, pos.x, pos.y);
        return dst;
    }

    public static ImVec2 canvasToScreen(float posX, float posY) {
        ImVec2 dst = new ImVec2();
        nCanvasToScreen(dst, posX, posY);
        return dst;
    }

    public static float canvasToScreenX(ImVec2 pos) {
        return nCanvasToScreenX(pos.x, pos.y);
    }

    public static float canvasToScreenY(ImVec2 pos) {
        return nCanvasToScreenY(pos.x, pos.y);
    }

    public static void canvasToScreen(ImVec2 dst, ImVec2 pos) {
        nCanvasToScreen(dst, pos.x, pos.y);
    }

    public static void canvasToScreen(ImVec2 dst, float posX, float posY) {
        nCanvasToScreen(dst, posX, posY);
    }

    public static int getNodeCount() {
        return nGetNodeCount();
    }

    public static int getOrderedNodeIds(long[] nodes, int size) {
        return nGetOrderedNodeIds(nodes, size);
    }
}
