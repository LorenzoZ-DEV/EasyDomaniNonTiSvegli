package imgui.extension.nodeditor;

import imgui.binding.ImGuiStructDestroyable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/extension/nodeditor/NodeEditorConfig.class */
public final class NodeEditorConfig extends ImGuiStructDestroyable {
    private native long nCreate();

    private native String nGetSettingsFile();

    private native void nSetSettingsFile(String str);

    public native int nGetCanvasSizeMode();

    public native void nSetCanvasSizeMode(int i);

    private native int nGetDragButtonIndex();

    private native void nSetDragButtonIndex(int i);

    private native int nGetSelectButtonIndex();

    private native void nSetSelectButtonIndex(int i);

    private native int nGetNavigateButtonIndex();

    private native void nSetNavigateButtonIndex(int i);

    private native int nGetContextMenuButtonIndex();

    private native void nSetContextMenuButtonIndex(int i);

    private native boolean nGetEnableSmoothZoom();

    private native void nSetEnableSmoothZoom(boolean z);

    private native float nGetSmoothZoomPower();

    private native void nSetSmoothZoomPower(float f);

    @Override // imgui.binding.ImGuiStructDestroyable
    protected long create() {
        return nCreate();
    }

    public String getSettingsFile() {
        return nGetSettingsFile();
    }

    public void setSettingsFile(String value) {
        nSetSettingsFile(value);
    }

    public int getCanvasSizeMode() {
        return nGetCanvasSizeMode();
    }

    public void setCanvasSizeMode(int value) {
        nSetCanvasSizeMode(value);
    }

    public int getDragButtonIndex() {
        return nGetDragButtonIndex();
    }

    public void setDragButtonIndex(int value) {
        nSetDragButtonIndex(value);
    }

    public int getSelectButtonIndex() {
        return nGetSelectButtonIndex();
    }

    public void setSelectButtonIndex(int value) {
        nSetSelectButtonIndex(value);
    }

    public int getNavigateButtonIndex() {
        return nGetNavigateButtonIndex();
    }

    public void setNavigateButtonIndex(int value) {
        nSetNavigateButtonIndex(value);
    }

    public int getContextMenuButtonIndex() {
        return nGetContextMenuButtonIndex();
    }

    public void setContextMenuButtonIndex(int value) {
        nSetContextMenuButtonIndex(value);
    }

    public boolean getEnableSmoothZoom() {
        return nGetEnableSmoothZoom();
    }

    public void setEnableSmoothZoom(boolean value) {
        nSetEnableSmoothZoom(value);
    }

    public float getSmoothZoomPower() {
        return nGetSmoothZoomPower();
    }

    public void setSmoothZoomPower(float value) {
        nSetSmoothZoomPower(value);
    }
}
