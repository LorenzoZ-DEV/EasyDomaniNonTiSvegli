package imgui.binding;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/binding/ImGuiStructDestroyable.class */
public abstract class ImGuiStructDestroyable extends ImGuiStruct {
    protected abstract long create();

    private native void nDestroy(long j);

    public ImGuiStructDestroyable() {
        this(0L);
        this.ptr = create();
    }

    public ImGuiStructDestroyable(long ptr) {
        super(ptr);
    }

    public void destroy() {
        nDestroy(this.ptr);
    }
}
