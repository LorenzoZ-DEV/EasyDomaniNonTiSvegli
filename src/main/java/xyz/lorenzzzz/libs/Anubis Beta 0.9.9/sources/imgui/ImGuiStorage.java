package imgui;

import imgui.binding.ImGuiStructDestroyable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/ImGuiStorage.class */
public final class ImGuiStorage extends ImGuiStructDestroyable {
    private native long nCreate();

    private native void nClear();

    private native int nGetInt(int i);

    private native int nGetInt(int i, int i2);

    private native void nSetInt(int i, int i2);

    private native boolean nGetBool(int i);

    private native boolean nGetBool(int i, boolean z);

    private native void nSetBool(int i, boolean z);

    private native float nGetFloat(int i);

    private native float nGetFloat(int i, float f);

    private native void nSetFloat(int i, float f);

    private native void nBuildSortByKey();

    private native void nSetAllInt(int i);

    public ImGuiStorage() {
    }

    public ImGuiStorage(long ptr) {
        super(ptr);
    }

    @Override // imgui.binding.ImGuiStructDestroyable
    protected long create() {
        return nCreate();
    }

    public void clear() {
        nClear();
    }

    public int getInt(int key) {
        return nGetInt(key);
    }

    public int getInt(int key, int defaultVal) {
        return nGetInt(key, defaultVal);
    }

    public void setInt(int key, int val) {
        nSetInt(key, val);
    }

    public boolean getBool(int key) {
        return nGetBool(key);
    }

    public boolean getBool(int key, boolean defaultVal) {
        return nGetBool(key, defaultVal);
    }

    public void setBool(int key, boolean val) {
        nSetBool(key, val);
    }

    public float getFloat(int key) {
        return nGetFloat(key);
    }

    public float getFloat(int key, float defaultVal) {
        return nGetFloat(key, defaultVal);
    }

    public void setFloat(int key, float val) {
        nSetFloat(key, val);
    }

    public void buildSortByKey() {
        nBuildSortByKey();
    }

    public void setAllInt(int val) {
        nSetAllInt(val);
    }
}
