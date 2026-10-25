package imgui.internal;

import imgui.ImGuiWindowClass;
import imgui.ImVec2;
import imgui.binding.ImGuiStruct;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/internal/ImGuiDockNode.class */
public final class ImGuiDockNode extends ImGuiStruct {
    private native int nGetID();

    private native void nSetID(int i);

    private native int nGetSharedFlags();

    private native void nSetSharedFlags(int i);

    private native int nGetLocalFlags();

    private native void nSetLocalFlags(int i);

    private native int nGetLocalFlagsInWindows();

    private native void nSetLocalFlagsInWindows(int i);

    private native int nGetMergedFlags();

    private native long nGetParentNode();

    private native void nSetParentNode(long j);

    private native long nGetChildNodeFirst();

    private native void nSetChildNodeFirst(long j);

    private native long nGetChildNodeSecond();

    private native void nSetChildNodeSecond(long j);

    private native void nGetPos(ImVec2 imVec2);

    private native float nGetPosX();

    private native float nGetPosY();

    private native void nSetPos(float f, float f2);

    private native void nGetSize(ImVec2 imVec2);

    private native float nGetSizeX();

    private native float nGetSizeY();

    private native void nSetSize(float f, float f2);

    private native void nGetSizeRef(ImVec2 imVec2);

    private native float nGetSizeRefX();

    private native float nGetSizeRefY();

    private native void nSetSizeRef(float f, float f2);

    private native int nGetSplitAxis();

    private native long nGetWindowClass();

    private native int nGetLastBgColor();

    private native void nSetLastBgColor(int i);

    private native long nGetHostWindow();

    private native void nSetHostWindow(long j);

    private native long nGetVisibleWindow();

    private native void nSetVisibleWindow(long j);

    private native long nGetCentralNode();

    private native void nSetCentralNode(long j);

    private native long nGetOnlyNodeWithWindows();

    private native void nSetOnlyNodeWithWindows(long j);

    private native int nGetLastFrameAlive();

    private native void nSetLastFrameAlive(int i);

    private native int nGetLastFrameActive();

    private native void nSetLastFrameActive(int i);

    private native int nGetLastFrameFocused();

    private native void nSetLastFrameFocused(int i);

    private native int nGetLastFocusedNodeId();

    private native void nSetLastFocusedNodeId(int i);

    private native int nGetSelectedTabId();

    private native void nSetSelectedTabId(int i);

    private native int nGetWantCloseTabId();

    private native void nSetWantCloseTabId(int i);

    private native int nGetAuthorityForPos();

    private native void nSetAuthorityForPos(int i);

    private native int nGetAuthorityForSize();

    private native void nSetAuthorityForSize(int i);

    private native int nGetAuthorityForViewport();

    private native void nSetAuthorityForViewport(int i);

    private native boolean nGetIsVisible();

    private native void nSetIsVisible(boolean z);

    private native boolean nGetIsFocused();

    private native void nSetIsFocused(boolean z);

    private native boolean nGetHasCloseButton();

    private native void nSetHasCloseButton(boolean z);

    private native boolean nGetHasWindowMenuButton();

    private native void nSetHasWindowMenuButton(boolean z);

    private native boolean nGetHasCentralNodeChild();

    private native void nSetHasCentralNodeChild(boolean z);

    private native boolean nGetWantCloseAll();

    private native void nSetWantCloseAll(boolean z);

    private native boolean nGetWantLockSizeOnce();

    private native void nSetWantLockSizeOnce(boolean z);

    private native boolean nGetWantMouseMove();

    private native void nSetWantMouseMove(boolean z);

    private native boolean nGetWantHiddenTabBarUpdate();

    private native void nSetWantHiddenTabBarUpdate(boolean z);

    private native boolean nGetWantHiddenTabBarToggle();

    private native void nSetWantHiddenTabBarToggle(boolean z);

    private native boolean nIsRootNode();

    private native boolean nIsDockSpace();

    private native boolean nIsFloatingNode();

    private native boolean nIsCentralNode();

    private native boolean nIsHiddenTabBar();

    private native boolean nIsNoTabBar();

    private native boolean nIsSplitNode();

    private native boolean nIsLeafNode();

    private native boolean nIsEmpty();

    private native void nRect(ImRect imRect);

    private native void nUpdateMergedFlags();

    public ImGuiDockNode(long ptr) {
        super(ptr);
        ImGui.init();
    }

    public int getID() {
        return nGetID();
    }

    public void setID(int value) {
        nSetID(value);
    }

    public int getSharedFlags() {
        return nGetSharedFlags();
    }

    public void setSharedFlags(int value) {
        nSetSharedFlags(value);
    }

    public void addSharedFlags(int flags) {
        setSharedFlags(getSharedFlags() | flags);
    }

    public void removeSharedFlags(int flags) {
        setSharedFlags(getSharedFlags() & (flags ^ (-1)));
    }

    public boolean hasSharedFlags(int flags) {
        return (getSharedFlags() & flags) != 0;
    }

    public int getLocalFlags() {
        return nGetLocalFlags();
    }

    public void setLocalFlags(int value) {
        nSetLocalFlags(value);
    }

    public void addLocalFlags(int flags) {
        setLocalFlags(getLocalFlags() | flags);
    }

    public void removeLocalFlags(int flags) {
        setLocalFlags(getLocalFlags() & (flags ^ (-1)));
    }

    public boolean hasLocalFlags(int flags) {
        return (getLocalFlags() & flags) != 0;
    }

    public int getLocalFlagsInWindows() {
        return nGetLocalFlagsInWindows();
    }

    public void setLocalFlagsInWindows(int value) {
        nSetLocalFlagsInWindows(value);
    }

    public void addLocalFlagsInWindows(int flags) {
        setLocalFlagsInWindows(getLocalFlagsInWindows() | flags);
    }

    public void removeLocalFlagsInWindows(int flags) {
        setLocalFlagsInWindows(getLocalFlagsInWindows() & (flags ^ (-1)));
    }

    public boolean hasLocalFlagsInWindows(int flags) {
        return (getLocalFlagsInWindows() & flags) != 0;
    }

    public int getMergedFlags() {
        return nGetMergedFlags();
    }

    public boolean hasMergedFlags(int flags) {
        return (getMergedFlags() & flags) != 0;
    }

    public ImGuiDockNode getParentNode() {
        return new ImGuiDockNode(nGetParentNode());
    }

    public void setParentNode(ImGuiDockNode value) {
        nSetParentNode(value.ptr);
    }

    public ImGuiDockNode getChildNodeFirst() {
        return new ImGuiDockNode(nGetChildNodeFirst());
    }

    public void setChildNodeFirst(ImGuiDockNode imGuiDockNode) {
        nSetChildNodeFirst(imGuiDockNode.ptr);
    }

    public ImGuiDockNode getChildNodeSecond() {
        return new ImGuiDockNode(nGetChildNodeSecond());
    }

    public void setChildNodeSecond(ImGuiDockNode imGuiDockNode) {
        nSetChildNodeSecond(imGuiDockNode.ptr);
    }

    public ImVec2 getPos() {
        ImVec2 dst = new ImVec2();
        nGetPos(dst);
        return dst;
    }

    public float getPosX() {
        return nGetPosX();
    }

    public float getPosY() {
        return nGetPosY();
    }

    public void getPos(ImVec2 dst) {
        nGetPos(dst);
    }

    public void setPos(ImVec2 value) {
        nSetPos(value.x, value.y);
    }

    public void setPos(float valueX, float valueY) {
        nSetPos(valueX, valueY);
    }

    public ImVec2 getSize() {
        ImVec2 dst = new ImVec2();
        nGetSize(dst);
        return dst;
    }

    public float getSizeX() {
        return nGetSizeX();
    }

    public float getSizeY() {
        return nGetSizeY();
    }

    public void getSize(ImVec2 dst) {
        nGetSize(dst);
    }

    public void setSize(ImVec2 value) {
        nSetSize(value.x, value.y);
    }

    public void setSize(float valueX, float valueY) {
        nSetSize(valueX, valueY);
    }

    public ImVec2 getSizeRef() {
        ImVec2 dst = new ImVec2();
        nGetSizeRef(dst);
        return dst;
    }

    public float getSizeRefX() {
        return nGetSizeRefX();
    }

    public float getSizeRefY() {
        return nGetSizeRefY();
    }

    public void getSizeRef(ImVec2 dst) {
        nGetSizeRef(dst);
    }

    public void setSizeRef(ImVec2 value) {
        nSetSizeRef(value.x, value.y);
    }

    public void setSizeRef(float valueX, float valueY) {
        nSetSizeRef(valueX, valueY);
    }

    public int getSplitAxis() {
        return nGetSplitAxis();
    }

    public ImGuiWindowClass getWindowClass() {
        return new ImGuiWindowClass(nGetWindowClass());
    }

    public int getLastBgColor() {
        return nGetLastBgColor();
    }

    public void setLastBgColor(int value) {
        nSetLastBgColor(value);
    }

    public ImGuiWindow getHostWindow() {
        return new ImGuiWindow(nGetHostWindow());
    }

    public void setHostWindow(ImGuiWindow value) {
        nSetHostWindow(value.ptr);
    }

    public ImGuiWindow getVisibleWindow() {
        return new ImGuiWindow(nGetVisibleWindow());
    }

    public void setVisibleWindow(ImGuiWindow value) {
        nSetVisibleWindow(value.ptr);
    }

    public ImGuiDockNode getCentralNode() {
        return new ImGuiDockNode(nGetCentralNode());
    }

    public void setCentralNode(ImGuiDockNode value) {
        nSetCentralNode(value.ptr);
    }

    public ImGuiDockNode getOnlyNodeWithWindows() {
        return new ImGuiDockNode(nGetOnlyNodeWithWindows());
    }

    public void setOnlyNodeWithWindows(ImGuiDockNode value) {
        nSetOnlyNodeWithWindows(value.ptr);
    }

    public int getLastFrameAlive() {
        return nGetLastFrameAlive();
    }

    public void setLastFrameAlive(int value) {
        nSetLastFrameAlive(value);
    }

    public int getLastFrameActive() {
        return nGetLastFrameActive();
    }

    public void setLastFrameActive(int value) {
        nSetLastFrameActive(value);
    }

    public int getLastFrameFocused() {
        return nGetLastFrameFocused();
    }

    public void setLastFrameFocused(int value) {
        nSetLastFrameFocused(value);
    }

    public int getLastFocusedNodeId() {
        return nGetLastFocusedNodeId();
    }

    public void setLastFocusedNodeId(int value) {
        nSetLastFocusedNodeId(value);
    }

    public int getSelectedTabId() {
        return nGetSelectedTabId();
    }

    public void setSelectedTabId(int value) {
        nSetSelectedTabId(value);
    }

    public int getWantCloseTabId() {
        return nGetWantCloseTabId();
    }

    public void setWantCloseTabId(int value) {
        nSetWantCloseTabId(value);
    }

    public int getAuthorityForPos() {
        return nGetAuthorityForPos();
    }

    public void setAuthorityForPos(int value) {
        nSetAuthorityForPos(value);
    }

    public int getAuthorityForSize() {
        return nGetAuthorityForSize();
    }

    public void setAuthorityForSize(int value) {
        nSetAuthorityForSize(value);
    }

    public int getAuthorityForViewport() {
        return nGetAuthorityForViewport();
    }

    public void setAuthorityForViewport(int value) {
        nSetAuthorityForViewport(value);
    }

    public boolean getIsVisible() {
        return nGetIsVisible();
    }

    public void setIsVisible(boolean value) {
        nSetIsVisible(value);
    }

    public boolean getIsFocused() {
        return nGetIsFocused();
    }

    public void setIsFocused(boolean value) {
        nSetIsFocused(value);
    }

    public boolean getHasCloseButton() {
        return nGetHasCloseButton();
    }

    public void setHasCloseButton(boolean value) {
        nSetHasCloseButton(value);
    }

    public boolean getHasWindowMenuButton() {
        return nGetHasWindowMenuButton();
    }

    public void setHasWindowMenuButton(boolean value) {
        nSetHasWindowMenuButton(value);
    }

    public boolean getHasCentralNodeChild() {
        return nGetHasCentralNodeChild();
    }

    public void setHasCentralNodeChild(boolean value) {
        nSetHasCentralNodeChild(value);
    }

    public boolean getWantCloseAll() {
        return nGetWantCloseAll();
    }

    public void setWantCloseAll(boolean value) {
        nSetWantCloseAll(value);
    }

    public boolean getWantLockSizeOnce() {
        return nGetWantLockSizeOnce();
    }

    public void setWantLockSizeOnce(boolean value) {
        nSetWantLockSizeOnce(value);
    }

    public boolean getWantMouseMove() {
        return nGetWantMouseMove();
    }

    public void setWantMouseMove(boolean value) {
        nSetWantMouseMove(value);
    }

    public boolean getWantHiddenTabBarUpdate() {
        return nGetWantHiddenTabBarUpdate();
    }

    public void setWantHiddenTabBarUpdate(boolean value) {
        nSetWantHiddenTabBarUpdate(value);
    }

    public boolean getWantHiddenTabBarToggle() {
        return nGetWantHiddenTabBarToggle();
    }

    public void setWantHiddenTabBarToggle(boolean value) {
        nSetWantHiddenTabBarToggle(value);
    }

    public boolean isRootNode() {
        return nIsRootNode();
    }

    public boolean isDockSpace() {
        return nIsDockSpace();
    }

    public boolean isFloatingNode() {
        return nIsFloatingNode();
    }

    public boolean isCentralNode() {
        return nIsCentralNode();
    }

    public boolean isHiddenTabBar() {
        return nIsHiddenTabBar();
    }

    public boolean isNoTabBar() {
        return nIsNoTabBar();
    }

    public boolean isSplitNode() {
        return nIsSplitNode();
    }

    public boolean isLeafNode() {
        return nIsLeafNode();
    }

    public boolean isEmpty() {
        return nIsEmpty();
    }

    public ImRect rect() {
        ImRect dst = new ImRect();
        nRect(dst);
        return dst;
    }

    public void rect(ImRect dst) {
        nRect(dst);
    }

    public void updateMergedFlags() {
        nUpdateMergedFlags();
    }
}
