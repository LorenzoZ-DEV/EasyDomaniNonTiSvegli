package imgui.internal;

import imgui.binding.ImGuiStruct;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/internal/ImGuiWindow.class */
public class ImGuiWindow extends ImGuiStruct {
    private native boolean nGetScrollbarX();

    private native boolean nGetScrollbarY();

    public ImGuiWindow(long ptr) {
        super(ptr);
        ImGui.init();
    }

    public boolean getScrollbarX() {
        return nGetScrollbarX();
    }

    public boolean getScrollbarY() {
        return nGetScrollbarY();
    }
}
