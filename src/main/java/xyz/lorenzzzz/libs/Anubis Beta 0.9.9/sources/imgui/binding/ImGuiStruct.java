package imgui.binding;

import imgui.ImGui;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/binding/ImGuiStruct.class */
public abstract class ImGuiStruct {
    public long ptr;

    public ImGuiStruct(long ptr) {
        ImGui.init();
        this.ptr = ptr;
    }

    public final boolean isValidPtr() {
        return this.ptr != 0;
    }

    public final boolean isNotValidPtr() {
        return !isValidPtr();
    }
}
