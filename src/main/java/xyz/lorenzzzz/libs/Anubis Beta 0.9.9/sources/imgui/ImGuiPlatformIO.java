package imgui;

import imgui.binding.ImGuiStruct;
import imgui.callback.ImPlatformFuncViewport;
import imgui.callback.ImPlatformFuncViewportFloat;
import imgui.callback.ImPlatformFuncViewportImVec2;
import imgui.callback.ImPlatformFuncViewportString;
import imgui.callback.ImPlatformFuncViewportSuppBoolean;
import imgui.callback.ImPlatformFuncViewportSuppFloat;
import imgui.callback.ImPlatformFuncViewportSuppImVec2;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/ImGuiPlatformIO.class */
public final class ImGuiPlatformIO extends ImGuiStruct {
    private static final ImGuiViewport TMP_VIEWPORT = new ImGuiViewport(0);
    private static final ImGuiPlatformMonitor TMP_MONITOR = new ImGuiPlatformMonitor(0);
    private static final ImVec2 TMP_IM_VEC2 = new ImVec2();

    private static native void nInit(ImGuiViewport imGuiViewport, ImVec2 imVec2);

    public native void setPlatformCreateWindow(ImPlatformFuncViewport imPlatformFuncViewport);

    public native void setPlatformDestroyWindow(ImPlatformFuncViewport imPlatformFuncViewport);

    public native void setPlatformShowWindow(ImPlatformFuncViewport imPlatformFuncViewport);

    public native void setPlatformSetWindowPos(ImPlatformFuncViewportImVec2 imPlatformFuncViewportImVec2);

    public native void setPlatformGetWindowPos(ImPlatformFuncViewportSuppImVec2 imPlatformFuncViewportSuppImVec2);

    public native void setPlatformSetWindowSize(ImPlatformFuncViewportImVec2 imPlatformFuncViewportImVec2);

    public native void setPlatformGetWindowSize(ImPlatformFuncViewportSuppImVec2 imPlatformFuncViewportSuppImVec2);

    public native void setPlatformSetWindowFocus(ImPlatformFuncViewport imPlatformFuncViewport);

    public native void setPlatformGetWindowFocus(ImPlatformFuncViewportSuppBoolean imPlatformFuncViewportSuppBoolean);

    public native void setPlatformGetWindowMinimized(ImPlatformFuncViewportSuppBoolean imPlatformFuncViewportSuppBoolean);

    public native void setPlatformSetWindowTitle(ImPlatformFuncViewportString imPlatformFuncViewportString);

    public native void setPlatformSetWindowAlpha(ImPlatformFuncViewportFloat imPlatformFuncViewportFloat);

    public native void setPlatformUpdateWindow(ImPlatformFuncViewport imPlatformFuncViewport);

    public native void setPlatformRenderWindow(ImPlatformFuncViewport imPlatformFuncViewport);

    public native void setPlatformSwapBuffers(ImPlatformFuncViewport imPlatformFuncViewport);

    public native void setPlatformGetWindowDpiScale(ImPlatformFuncViewportSuppFloat imPlatformFuncViewportSuppFloat);

    public native void setPlatformOnChangedViewport(ImPlatformFuncViewport imPlatformFuncViewport);

    public native void setRendererCreateWindow(ImPlatformFuncViewport imPlatformFuncViewport);

    public native void setRendererDestroyWindow(ImPlatformFuncViewport imPlatformFuncViewport);

    public native void setRendererSetWindowPos(ImPlatformFuncViewportImVec2 imPlatformFuncViewportImVec2);

    public native void setRendererRenderWindow(ImPlatformFuncViewport imPlatformFuncViewport);

    public native void setRendererSwapBuffers(ImPlatformFuncViewport imPlatformFuncViewport);

    public native void resizeMonitors(int i);

    public native int getMonitorsSize();

    public native void pushMonitors(long j, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9);

    private native long nGetMonitors(int i);

    public native int getViewportsSize();

    private native long nGetViewports(int i);

    public ImGuiPlatformIO(long ptr) {
        super(ptr);
    }

    static void init() {
        nInit(TMP_VIEWPORT, TMP_IM_VEC2);
    }

    public ImGuiPlatformMonitor getMonitors(int idx) {
        TMP_MONITOR.ptr = nGetMonitors(idx);
        return TMP_MONITOR;
    }

    public ImGuiViewport getViewports(int idx) {
        TMP_VIEWPORT.ptr = nGetViewports(idx);
        return TMP_VIEWPORT;
    }
}
