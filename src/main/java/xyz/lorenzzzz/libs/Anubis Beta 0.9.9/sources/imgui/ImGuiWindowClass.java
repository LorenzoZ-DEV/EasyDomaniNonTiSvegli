package imgui;

import imgui.binding.ImGuiStructDestroyable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/ImGuiWindowClass.class */
public final class ImGuiWindowClass extends ImGuiStructDestroyable {
    private native long nCreate();

    private native int nGetClassId();

    private native void nSetClassId(int i);

    private native int nGetParentViewportId();

    private native void nSetParentViewportId(int i);

    private native int nGetFocusRouteParentWindowId();

    private native void nSetFocusRouteParentWindowId(int i);

    private native int nGetViewportFlagsOverrideSet();

    private native void nSetViewportFlagsOverrideSet(int i);

    private native int nGetViewportFlagsOverrideClear();

    private native void nSetViewportFlagsOverrideClear(int i);

    private native int nGetTabItemFlagsOverrideSet();

    private native void nSetTabItemFlagsOverrideSet(int i);

    private native int nGetDockNodeFlagsOverrideSet();

    private native void nSetDockNodeFlagsOverrideSet(int i);

    private native boolean nGetDockingAlwaysTabBar();

    private native void nSetDockingAlwaysTabBar(boolean z);

    private native boolean nGetDockingAllowUnclassed();

    private native void nSetDockingAllowUnclassed(boolean z);

    public ImGuiWindowClass() {
    }

    public ImGuiWindowClass(long ptr) {
        super(ptr);
    }

    @Override // imgui.binding.ImGuiStructDestroyable
    protected long create() {
        return nCreate();
    }

    public int getClassId() {
        return nGetClassId();
    }

    public void setClassId(int value) {
        nSetClassId(value);
    }

    public int getParentViewportId() {
        return nGetParentViewportId();
    }

    public void setParentViewportId(int value) {
        nSetParentViewportId(value);
    }

    public int getFocusRouteParentWindowId() {
        return nGetFocusRouteParentWindowId();
    }

    public void setFocusRouteParentWindowId(int value) {
        nSetFocusRouteParentWindowId(value);
    }

    public int getViewportFlagsOverrideSet() {
        return nGetViewportFlagsOverrideSet();
    }

    public void setViewportFlagsOverrideSet(int value) {
        nSetViewportFlagsOverrideSet(value);
    }

    public void addViewportFlagsOverrideSet(int flags) {
        setViewportFlagsOverrideSet(getViewportFlagsOverrideSet() | flags);
    }

    public void removeViewportFlagsOverrideSet(int flags) {
        setViewportFlagsOverrideSet(getViewportFlagsOverrideSet() & (flags ^ (-1)));
    }

    public boolean hasViewportFlagsOverrideSet(int flags) {
        return (getViewportFlagsOverrideSet() & flags) != 0;
    }

    public int getViewportFlagsOverrideClear() {
        return nGetViewportFlagsOverrideClear();
    }

    public void setViewportFlagsOverrideClear(int value) {
        nSetViewportFlagsOverrideClear(value);
    }

    public void addViewportFlagsOverrideClear(int flags) {
        setViewportFlagsOverrideClear(getViewportFlagsOverrideClear() | flags);
    }

    public void removeViewportFlagsOverrideClear(int flags) {
        setViewportFlagsOverrideClear(getViewportFlagsOverrideClear() & (flags ^ (-1)));
    }

    public boolean hasViewportFlagsOverrideClear(int flags) {
        return (getViewportFlagsOverrideClear() & flags) != 0;
    }

    public int getTabItemFlagsOverrideSet() {
        return nGetTabItemFlagsOverrideSet();
    }

    public void setTabItemFlagsOverrideSet(int value) {
        nSetTabItemFlagsOverrideSet(value);
    }

    public void addTabItemFlagsOverrideSet(int flags) {
        setTabItemFlagsOverrideSet(getTabItemFlagsOverrideSet() | flags);
    }

    public void removeTabItemFlagsOverrideSet(int flags) {
        setTabItemFlagsOverrideSet(getTabItemFlagsOverrideSet() & (flags ^ (-1)));
    }

    public boolean hasTabItemFlagsOverrideSet(int flags) {
        return (getTabItemFlagsOverrideSet() & flags) != 0;
    }

    public int getDockNodeFlagsOverrideSet() {
        return nGetDockNodeFlagsOverrideSet();
    }

    public void setDockNodeFlagsOverrideSet(int value) {
        nSetDockNodeFlagsOverrideSet(value);
    }

    public void addDockNodeFlagsOverrideSet(int flags) {
        setDockNodeFlagsOverrideSet(getDockNodeFlagsOverrideSet() | flags);
    }

    public void removeDockNodeFlagsOverrideSet(int flags) {
        setDockNodeFlagsOverrideSet(getDockNodeFlagsOverrideSet() & (flags ^ (-1)));
    }

    public boolean hasDockNodeFlagsOverrideSet(int flags) {
        return (getDockNodeFlagsOverrideSet() & flags) != 0;
    }

    public boolean getDockingAlwaysTabBar() {
        return nGetDockingAlwaysTabBar();
    }

    public void setDockingAlwaysTabBar(boolean value) {
        nSetDockingAlwaysTabBar(value);
    }

    public boolean getDockingAllowUnclassed() {
        return nGetDockingAllowUnclassed();
    }

    public void setDockingAllowUnclassed(boolean value) {
        nSetDockingAllowUnclassed(value);
    }
}
