package imgui;

import imgui.binding.ImGuiStructDestroyable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/ImGuiKeyData.class */
public final class ImGuiKeyData extends ImGuiStructDestroyable {
    private native long nCreate();

    private native boolean nGetDown();

    private native void nSetDown(boolean z);

    private native float nGetDownDuration();

    private native void nSetDownDuration(float f);

    private native float nGetDownDurationPrev();

    private native void nSetDownDurationPrev(float f);

    private native float nGetAnalogValue();

    private native void nSetAnalogValue(float f);

    public ImGuiKeyData() {
    }

    public ImGuiKeyData(long ptr) {
        super(ptr);
    }

    @Override // imgui.binding.ImGuiStructDestroyable
    protected long create() {
        return nCreate();
    }

    public boolean getDown() {
        return nGetDown();
    }

    public void setDown(boolean value) {
        nSetDown(value);
    }

    public float getDownDuration() {
        return nGetDownDuration();
    }

    public void setDownDuration(float value) {
        nSetDownDuration(value);
    }

    public float getDownDurationPrev() {
        return nGetDownDurationPrev();
    }

    public void setDownDurationPrev(float value) {
        nSetDownDurationPrev(value);
    }

    public float getAnalogValue() {
        return nGetAnalogValue();
    }

    public void setAnalogValue(float value) {
        nSetAnalogValue(value);
    }
}
