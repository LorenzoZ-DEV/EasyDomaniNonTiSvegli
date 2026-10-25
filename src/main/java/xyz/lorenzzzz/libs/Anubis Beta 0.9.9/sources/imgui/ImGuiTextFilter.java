package imgui;

import imgui.binding.ImGuiStructDestroyable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/ImGuiTextFilter.class */
public final class ImGuiTextFilter extends ImGuiStructDestroyable {
    private native long nCreate(String str);

    private native boolean nDraw();

    private native boolean nDraw(String str);

    private native boolean nDraw(String str, float f);

    private native boolean nDraw(float f);

    private native boolean nPassFilter(String str);

    private native void nBuild();

    private native void nClear();

    private native boolean nIsActive();

    public native String getInputBuffer();

    public native void setInputBuffer(String str);

    private native int nGetCountGrep();

    private native void nSetCountGrep(int i);

    public ImGuiTextFilter() {
        this("");
    }

    public ImGuiTextFilter(String defaultFilter) {
        this.ptr = nCreate(defaultFilter);
    }

    public ImGuiTextFilter(long ptr) {
        super(ptr);
    }

    @Override // imgui.binding.ImGuiStructDestroyable
    protected long create() {
        return nCreate("");
    }

    public boolean draw() {
        return nDraw();
    }

    public boolean draw(String label) {
        return nDraw(label);
    }

    public boolean draw(String label, float width) {
        return nDraw(label, width);
    }

    public boolean draw(float width) {
        return nDraw(width);
    }

    public boolean passFilter(String text) {
        return nPassFilter(text);
    }

    public void build() {
        nBuild();
    }

    public void clear() {
        nClear();
    }

    public boolean isActive() {
        return nIsActive();
    }

    public int getCountGrep() {
        return nGetCountGrep();
    }

    public void setCountGrep(int value) {
        nSetCountGrep(value);
    }
}
