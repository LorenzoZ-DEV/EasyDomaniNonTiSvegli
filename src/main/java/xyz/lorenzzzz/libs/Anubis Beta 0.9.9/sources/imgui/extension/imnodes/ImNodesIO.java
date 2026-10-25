package imgui.extension.imnodes;

import imgui.binding.ImGuiStruct;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/extension/imnodes/ImNodesIO.class */
public final class ImNodesIO extends ImGuiStruct {
    private native int nGetAltMouseButton();

    private native void nSetAltMouseButton(int i);

    private native float nGetAutoPanningSpeed();

    private native void nSetAutoPanningSpeed(float f);

    public ImNodesIO(long ptr) {
        super(ptr);
    }

    public int getAltMouseButton() {
        return nGetAltMouseButton();
    }

    public void setAltMouseButton(int value) {
        nSetAltMouseButton(value);
    }

    public float getAutoPanningSpeed() {
        return nGetAutoPanningSpeed();
    }

    public void setAutoPanningSpeed(float value) {
        nSetAutoPanningSpeed(value);
    }
}
