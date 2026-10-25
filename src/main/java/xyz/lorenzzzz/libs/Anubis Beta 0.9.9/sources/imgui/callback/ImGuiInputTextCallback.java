package imgui.callback;

import imgui.ImGuiInputTextCallbackData;
import java.util.function.Consumer;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/callback/ImGuiInputTextCallback.class */
public abstract class ImGuiInputTextCallback implements Consumer<ImGuiInputTextCallbackData> {
    public final void accept(long ptr) {
        accept(new ImGuiInputTextCallbackData(ptr));
    }
}
