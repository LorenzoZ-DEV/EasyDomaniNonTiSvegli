package imgui.internal;

import imgui.binding.ImGuiStruct;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/internal/ImGuiContext.class */
public class ImGuiContext extends ImGuiStruct {
    public ImGuiContext(long ptr) {
        super(ptr);
        ImGui.init();
    }
}
