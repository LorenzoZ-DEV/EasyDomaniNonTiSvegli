package imgui.extension.imnodes;

import imgui.ImVec2;
import imgui.internal.ImGuiContext;
import imgui.type.ImBoolean;
import imgui.type.ImInt;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/extension/imnodes/ImNodes.class */
public final class ImNodes {
    private static final ImNodesIO _GETIO_1 = new ImNodesIO(0);
    private static final ImNodesStyle _GETSTYLE_1 = new ImNodesStyle(0);

    private static native void nSetImGuiContext(long j);

    private static native long nCreateContext();

    private static native void nDestroyContext();

    private static native void nDestroyContext(long j);

    private static native long nGetCurrentContext();

    private static native void nSetCurrentContext(long j);

    private static native long nEditorContextCreate();

    private static native void nEditorContextFree(long j);

    private static native void nEditorContextSet(long j);

    private static native void nEditorContextGetPanning(ImVec2 imVec2);

    private static native float nEditorContextGetPanningX();

    private static native float nEditorContextGetPanningY();

    private static native void nEditorContextResetPanning(float f, float f2);

    private static native void nEditorContextMoveToNode(int i);

    private static native long nGetIO();

    private static native long nGetStyle();

    private static native void nStyleColorsDark();

    private static native void nStyleColorsDark(long j);

    private static native void nStyleColorsClassic();

    private static native void nStyleColorsClassic(long j);

    private static native void nStyleColorsLight();

    private static native void nStyleColorsLight(long j);

    private static native void nBeginNodeEditor();

    private static native void nEndNodeEditor();

    private static native void nMiniMap();

    private static native void nMiniMap(float f);

    private static native void nMiniMap(float f, int i);

    private static native void nPushColorStyle(int i, int i2);

    private static native void nPopColorStyle();

    private static native void nPushStyleVar(int i, float f);

    private static native void nPushStyleVar(int i, float f, float f2);

    private static native void nPopStyleVar();

    private static native void nBeginNode(int i);

    private static native void nEndNode();

    private static native void nGetNodeDimensions(ImVec2 imVec2, int i);

    private static native float nGetNodeDimensionsX(int i);

    private static native float nGetNodeDimensionsY(int i);

    private static native void nBeginNodeTitleBar();

    private static native void nEndNodeTitleBar();

    private static native void nBeginInputAttribute(int i);

    private static native void nBeginInputAttribute(int i, int i2);

    private static native void nEndInputAttribute();

    private static native void nBeginOutputAttribute(int i);

    private static native void nBeginOutputAttribute(int i, int i2);

    private static native void nEndOutputAttribute();

    private static native void nBeginStaticAttribute(int i);

    private static native void nEndStaticAttribute();

    private static native void nPushAttributeFlag(int i);

    private static native void nPopAttributeFlag();

    private static native void nLink(int i, int i2, int i3);

    private static native void nSetNodeDraggable(int i, boolean z);

    private static native void nSetNodeScreenSpacePos(int i, float f, float f2);

    private static native void nSetNodeEditorSpacePos(int i, float f, float f2);

    private static native void nSetNodeGridSpacePos(int i, float f, float f2);

    private static native void nGetNodeScreenSpacePos(ImVec2 imVec2, int i);

    private static native float nGetNodeScreenSpacePosX(int i);

    private static native float nGetNodeScreenSpacePosY(int i);

    private static native void nGetNodeEditorSpacePos(ImVec2 imVec2, int i);

    private static native float nGetNodeEditorSpacePosX(int i);

    private static native float nGetNodeEditorSpacePosY(int i);

    private static native void nGetNodeGridSpacePos(ImVec2 imVec2, int i);

    private static native float nGetNodeGridSpacePosX(int i);

    private static native float nGetNodeGridSpacePosY(int i);

    private static native void nSnapNodeToGrid(int i);

    private static native boolean nIsEditorHovered();

    private static native boolean nIsNodeHovered(int[] iArr);

    private static native boolean nIsLinkHovered(int[] iArr);

    private static native boolean nIsPinHovered(int[] iArr);

    public static native int getHoveredNode();

    public static native int getHoveredLink();

    public static native int getHoveredPin();

    private static native int nNumSelectedNodes();

    private static native int nNumSelectedLinks();

    private static native void nGetSelectedNodes(int[] iArr);

    private static native void nGetSelectedLinks(int[] iArr);

    private static native void nClearNodeSelection();

    private static native void nClearLinkSelection();

    private static native void nSelectNode(int i);

    private static native void nClearNodeSelection(int i);

    private static native boolean nIsNodeSelected(int i);

    private static native void nSelectLink(int i);

    private static native void nClearLinkSelection(int i);

    private static native boolean nIsLinkSelected(int i);

    private static native boolean nIsAttributeActive();

    private static native boolean nIsAnyAttributeActive();

    private static native boolean nIsAnyAttributeActive(int[] iArr);

    public static native int getActiveAttribute();

    private static native boolean nIsLinkStarted(int[] iArr);

    private static native boolean nIsLinkDropped();

    private static native boolean nIsLinkDropped(int[] iArr);

    private static native boolean nIsLinkDropped(int[] iArr, boolean z);

    private static native boolean nIsLinkDropped(boolean z);

    private static native boolean nIsLinkCreated(int[] iArr, int[] iArr2);

    private static native boolean nIsLinkCreated(int[] iArr, int[] iArr2, boolean[] zArr);

    private static native boolean nIsLinkCreated(int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4);

    private static native boolean nIsLinkCreated(int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, boolean[] zArr);

    private static native boolean nIsLinkDestroyed(int[] iArr);

    private static native String nSaveCurrentEditorStateToIniString();

    private static native String nSaveEditorStateToIniString(long j);

    private static native void nLoadCurrentEditorStateFromIniString(String str, int i);

    private static native void nLoadEditorStateFromIniString(long j, String str, int i);

    private static native void nSaveCurrentEditorStateToIniFile(String str);

    private static native void nSaveEditorStateToIniFile(long j, String str);

    private static native void nLoadCurrentEditorStateFromIniFile(String str);

    private static native void nLoadEditorStateFromIniFile(long j, String str);

    private ImNodes() {
    }

    public static void setImGuiContext(ImGuiContext ctx) {
        nSetImGuiContext(ctx.ptr);
    }

    public static ImNodesContext createContext() {
        return new ImNodesContext(nCreateContext());
    }

    public static void destroyContext() {
        nDestroyContext();
    }

    public static void destroyContext(ImNodesContext ctx) {
        nDestroyContext(ctx.ptr);
    }

    public static ImNodesContext getCurrentContext() {
        return new ImNodesContext(nGetCurrentContext());
    }

    public static void setCurrentContext(ImNodesContext ctx) {
        nSetCurrentContext(ctx.ptr);
    }

    public static ImNodesEditorContext editorContextCreate() {
        return new ImNodesEditorContext(nEditorContextCreate());
    }

    public static void editorContextFree(ImNodesEditorContext context) {
        nEditorContextFree(context.ptr);
    }

    public static void editorContextSet(ImNodesEditorContext context) {
        nEditorContextSet(context.ptr);
    }

    public static ImVec2 editorContextGetPanning() {
        ImVec2 dst = new ImVec2();
        nEditorContextGetPanning(dst);
        return dst;
    }

    public static float editorContextGetPanningX() {
        return nEditorContextGetPanningX();
    }

    public static float editorContextGetPanningY() {
        return nEditorContextGetPanningY();
    }

    public static void editorContextGetPanning(ImVec2 dst) {
        nEditorContextGetPanning(dst);
    }

    public static void editorContextResetPanning(ImVec2 pos) {
        nEditorContextResetPanning(pos.x, pos.y);
    }

    public static void editorContextResetPanning(float posX, float posY) {
        nEditorContextResetPanning(posX, posY);
    }

    public static void editorContextMoveToNode(int nodeId) {
        nEditorContextMoveToNode(nodeId);
    }

    public static ImNodesIO getIO() {
        _GETIO_1.ptr = nGetIO();
        return _GETIO_1;
    }

    public static ImNodesStyle getStyle() {
        _GETSTYLE_1.ptr = nGetStyle();
        return _GETSTYLE_1;
    }

    public static void styleColorsDark() {
        nStyleColorsDark();
    }

    public static void styleColorsDark(ImNodesStyle style) {
        nStyleColorsDark(style.ptr);
    }

    public static void styleColorsClassic() {
        nStyleColorsClassic();
    }

    public static void styleColorsClassic(ImNodesStyle style) {
        nStyleColorsClassic(style.ptr);
    }

    public static void styleColorsLight() {
        nStyleColorsLight();
    }

    public static void styleColorsLight(ImNodesStyle style) {
        nStyleColorsLight(style.ptr);
    }

    public static void beginNodeEditor() {
        nBeginNodeEditor();
    }

    public static void endNodeEditor() {
        nEndNodeEditor();
    }

    public static void miniMap() {
        nMiniMap();
    }

    public static void miniMap(float miniMapSizeFraction) {
        nMiniMap(miniMapSizeFraction);
    }

    public static void miniMap(float miniMapSizeFraction, int miniMapLocation) {
        nMiniMap(miniMapSizeFraction, miniMapLocation);
    }

    public static void pushColorStyle(int item, int color) {
        nPushColorStyle(item, color);
    }

    public static void popColorStyle() {
        nPopColorStyle();
    }

    public static void pushStyleVar(int styleItem, float value) {
        nPushStyleVar(styleItem, value);
    }

    public static void pushStyleVar(int styleItem, ImVec2 value) {
        nPushStyleVar(styleItem, value.x, value.y);
    }

    public static void pushStyleVar(int styleItem, float valueX, float valueY) {
        nPushStyleVar(styleItem, valueX, valueY);
    }

    public static void popStyleVar() {
        nPopStyleVar();
    }

    public static void beginNode(int node) {
        nBeginNode(node);
    }

    public static void endNode() {
        nEndNode();
    }

    public static ImVec2 getNodeDimensions(int id) {
        ImVec2 dst = new ImVec2();
        nGetNodeDimensions(dst, id);
        return dst;
    }

    public static float getNodeDimensionsX(int id) {
        return nGetNodeDimensionsX(id);
    }

    public static float getNodeDimensionsY(int id) {
        return nGetNodeDimensionsY(id);
    }

    public static void getNodeDimensions(ImVec2 dst, int id) {
        nGetNodeDimensions(dst, id);
    }

    public static void beginNodeTitleBar() {
        nBeginNodeTitleBar();
    }

    public static void endNodeTitleBar() {
        nEndNodeTitleBar();
    }

    public static void beginInputAttribute(int id) {
        nBeginInputAttribute(id);
    }

    public static void beginInputAttribute(int id, int shape) {
        nBeginInputAttribute(id, shape);
    }

    public static void endInputAttribute() {
        nEndInputAttribute();
    }

    public static void beginOutputAttribute(int id) {
        nBeginOutputAttribute(id);
    }

    public static void beginOutputAttribute(int id, int shape) {
        nBeginOutputAttribute(id, shape);
    }

    public static void endOutputAttribute() {
        nEndOutputAttribute();
    }

    public static void beginStaticAttribute(int id) {
        nBeginStaticAttribute(id);
    }

    public static void endStaticAttribute() {
        nEndStaticAttribute();
    }

    public static void pushAttributeFlag(int flag) {
        nPushAttributeFlag(flag);
    }

    public static void popAttributeFlag() {
        nPopAttributeFlag();
    }

    public static void link(int id, int source, int target) {
        nLink(id, source, target);
    }

    public static void setNodeDraggable(int nodeId, boolean draggable) {
        nSetNodeDraggable(nodeId, draggable);
    }

    public static void setNodeScreenSpacePos(int nodeId, ImVec2 screenSpacePos) {
        nSetNodeScreenSpacePos(nodeId, screenSpacePos.x, screenSpacePos.y);
    }

    public static void setNodeScreenSpacePos(int nodeId, float screenSpacePosX, float screenSpacePosY) {
        nSetNodeScreenSpacePos(nodeId, screenSpacePosX, screenSpacePosY);
    }

    public static void setNodeEditorSpacePos(int nodeId, ImVec2 editorSpacePos) {
        nSetNodeEditorSpacePos(nodeId, editorSpacePos.x, editorSpacePos.y);
    }

    public static void setNodeEditorSpacePos(int nodeId, float editorSpacePosX, float editorSpacePosY) {
        nSetNodeEditorSpacePos(nodeId, editorSpacePosX, editorSpacePosY);
    }

    public static void setNodeGridSpacePos(int nodeId, ImVec2 gridPos) {
        nSetNodeGridSpacePos(nodeId, gridPos.x, gridPos.y);
    }

    public static void setNodeGridSpacePos(int nodeId, float gridPosX, float gridPosY) {
        nSetNodeGridSpacePos(nodeId, gridPosX, gridPosY);
    }

    public static ImVec2 getNodeScreenSpacePos(int nodeId) {
        ImVec2 dst = new ImVec2();
        nGetNodeScreenSpacePos(dst, nodeId);
        return dst;
    }

    public static float getNodeScreenSpacePosX(int nodeId) {
        return nGetNodeScreenSpacePosX(nodeId);
    }

    public static float getNodeScreenSpacePosY(int nodeId) {
        return nGetNodeScreenSpacePosY(nodeId);
    }

    public static void getNodeScreenSpacePos(ImVec2 dst, int nodeId) {
        nGetNodeScreenSpacePos(dst, nodeId);
    }

    public static ImVec2 getNodeEditorSpacePos(int nodeId) {
        ImVec2 dst = new ImVec2();
        nGetNodeEditorSpacePos(dst, nodeId);
        return dst;
    }

    public static float getNodeEditorSpacePosX(int nodeId) {
        return nGetNodeEditorSpacePosX(nodeId);
    }

    public static float getNodeEditorSpacePosY(int nodeId) {
        return nGetNodeEditorSpacePosY(nodeId);
    }

    public static void getNodeEditorSpacePos(ImVec2 dst, int nodeId) {
        nGetNodeEditorSpacePos(dst, nodeId);
    }

    public static ImVec2 getNodeGridSpacePos(int nodeId) {
        ImVec2 dst = new ImVec2();
        nGetNodeGridSpacePos(dst, nodeId);
        return dst;
    }

    public static float getNodeGridSpacePosX(int nodeId) {
        return nGetNodeGridSpacePosX(nodeId);
    }

    public static float getNodeGridSpacePosY(int nodeId) {
        return nGetNodeGridSpacePosY(nodeId);
    }

    public static void getNodeGridSpacePos(ImVec2 dst, int nodeId) {
        nGetNodeGridSpacePos(dst, nodeId);
    }

    public static void snapNodeToGrid(int nodeId) {
        nSnapNodeToGrid(nodeId);
    }

    public static boolean isEditorHovered() {
        return nIsEditorHovered();
    }

    public static boolean isNodeHovered(ImInt nodeId) {
        return nIsNodeHovered(nodeId != null ? nodeId.getData() : null);
    }

    public static boolean isLinkHovered(ImInt linkId) {
        return nIsLinkHovered(linkId != null ? linkId.getData() : null);
    }

    public static boolean isPinHovered(ImInt attributeId) {
        return nIsPinHovered(attributeId != null ? attributeId.getData() : null);
    }

    public static int numSelectedNodes() {
        return nNumSelectedNodes();
    }

    public static int numSelectedLinks() {
        return nNumSelectedLinks();
    }

    public static void getSelectedNodes(int[] nodeIds) {
        nGetSelectedNodes(nodeIds);
    }

    public static void getSelectedLinks(int[] linkIds) {
        nGetSelectedLinks(linkIds);
    }

    public static void clearNodeSelection() {
        nClearNodeSelection();
    }

    public static void clearLinkSelection() {
        nClearLinkSelection();
    }

    public static void selectNode(int nodeId) {
        nSelectNode(nodeId);
    }

    public static void clearNodeSelection(int nodeId) {
        nClearNodeSelection(nodeId);
    }

    public static boolean isNodeSelected(int nodeId) {
        return nIsNodeSelected(nodeId);
    }

    public static void selectLink(int linkId) {
        nSelectLink(linkId);
    }

    public static void clearLinkSelection(int linkId) {
        nClearLinkSelection(linkId);
    }

    public static boolean isLinkSelected(int linkId) {
        return nIsLinkSelected(linkId);
    }

    public static boolean isAttributeActive() {
        return nIsAttributeActive();
    }

    public static boolean isAnyAttributeActive() {
        return nIsAnyAttributeActive();
    }

    public static boolean isAnyAttributeActive(ImInt attributeId) {
        return nIsAnyAttributeActive(attributeId != null ? attributeId.getData() : null);
    }

    public static boolean isLinkStarted(ImInt startedAtAttributeId) {
        return nIsLinkStarted(startedAtAttributeId != null ? startedAtAttributeId.getData() : null);
    }

    public static boolean isLinkDropped() {
        return nIsLinkDropped();
    }

    public static boolean isLinkDropped(ImInt startedAtAttributeId) {
        return nIsLinkDropped(startedAtAttributeId != null ? startedAtAttributeId.getData() : null);
    }

    public static boolean isLinkDropped(ImInt startedAtAttributeId, boolean includingDetachedLinks) {
        return nIsLinkDropped(startedAtAttributeId != null ? startedAtAttributeId.getData() : null, includingDetachedLinks);
    }

    public static boolean isLinkDropped(boolean includingDetachedLinks) {
        return nIsLinkDropped(includingDetachedLinks);
    }

    public static boolean isLinkCreated(ImInt startedAtAttributeId, ImInt endedAtAttributeId) {
        return nIsLinkCreated(startedAtAttributeId != null ? startedAtAttributeId.getData() : null, endedAtAttributeId != null ? endedAtAttributeId.getData() : null);
    }

    public static boolean isLinkCreated(ImInt startedAtAttributeId, ImInt endedAtAttributeId, ImBoolean createdFromSnap) {
        return nIsLinkCreated(startedAtAttributeId != null ? startedAtAttributeId.getData() : null, endedAtAttributeId != null ? endedAtAttributeId.getData() : null, createdFromSnap != null ? createdFromSnap.getData() : null);
    }

    public static boolean isLinkCreated(ImInt startedAtNodeId, ImInt startedAtAttributeId, ImInt endedAtNodeId, ImInt endedAtAttributeId) {
        return nIsLinkCreated(startedAtNodeId != null ? startedAtNodeId.getData() : null, startedAtAttributeId != null ? startedAtAttributeId.getData() : null, endedAtNodeId != null ? endedAtNodeId.getData() : null, endedAtAttributeId != null ? endedAtAttributeId.getData() : null);
    }

    public static boolean isLinkCreated(ImInt startedAtNodeId, ImInt startedAtAttributeId, ImInt endedAtNodeId, ImInt endedAtAttributeId, ImBoolean createdFromSnap) {
        return nIsLinkCreated(startedAtNodeId != null ? startedAtNodeId.getData() : null, startedAtAttributeId != null ? startedAtAttributeId.getData() : null, endedAtNodeId != null ? endedAtNodeId.getData() : null, endedAtAttributeId != null ? endedAtAttributeId.getData() : null, createdFromSnap != null ? createdFromSnap.getData() : null);
    }

    public static boolean isLinkDestroyed(ImInt linkId) {
        return nIsLinkDestroyed(linkId != null ? linkId.getData() : null);
    }

    public static String saveCurrentEditorStateToIniString() {
        return nSaveCurrentEditorStateToIniString();
    }

    public static String saveEditorStateToIniString(ImNodesEditorContext editor) {
        return nSaveEditorStateToIniString(editor.ptr);
    }

    public static void loadCurrentEditorStateFromIniString(String data, int dataSize) {
        nLoadCurrentEditorStateFromIniString(data, dataSize);
    }

    public static void loadEditorStateFromIniString(ImNodesEditorContext editor, String data, int dataSize) {
        nLoadEditorStateFromIniString(editor.ptr, data, dataSize);
    }

    public static void saveCurrentEditorStateToIniFile(String fileName) {
        nSaveCurrentEditorStateToIniFile(fileName);
    }

    public static void saveEditorStateToIniFile(ImNodesEditorContext editor, String fileName) {
        nSaveEditorStateToIniFile(editor.ptr, fileName);
    }

    public static void loadCurrentEditorStateFromIniFile(String fileName) {
        nLoadCurrentEditorStateFromIniFile(fileName);
    }

    public static void loadEditorStateFromIniFile(ImNodesEditorContext editor, String fileName) {
        nLoadEditorStateFromIniFile(editor.ptr, fileName);
    }
}
