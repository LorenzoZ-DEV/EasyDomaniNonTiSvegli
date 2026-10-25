package imgui;

import imgui.binding.ImGuiStructDestroyable;
import imgui.callback.ImListClipperCallback;
import imgui.internal.ImGuiContext;
import java.util.function.Consumer;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/ImGuiListClipper.class */
public final class ImGuiListClipper extends ImGuiStructDestroyable {
    private static final ImGuiContext _GETCTX_1 = new ImGuiContext(0);

    private native long nCreate();

    private native long nGetCtx();

    private native void nSetCtx(long j);

    private native int nGetDisplayStart();

    private native void nSetDisplayStart(int i);

    private native int nGetDisplayEnd();

    private native void nSetDisplayEnd(int i);

    private native int nGetItemsCount();

    private native float nGetItemsHeight();

    private native float nGetStartPosY();

    private native void nBegin(int i);

    private native void nBegin(int i, float f);

    private native void nEnd();

    private native boolean nStep();

    private native void nIncludeItemByIndex(int i);

    private native void nIncludeItemsByIndex(int i, int i2);

    private static native void nForEach(int i, int i2, ImListClipperCallback imListClipperCallback);

    public ImGuiListClipper() {
    }

    public ImGuiListClipper(long ptr) {
        super(ptr);
    }

    @Override // imgui.binding.ImGuiStructDestroyable
    protected long create() {
        return nCreate();
    }

    public ImGuiContext getCtx() {
        _GETCTX_1.ptr = nGetCtx();
        return _GETCTX_1;
    }

    public void setCtx(ImGuiContext value) {
        nSetCtx(value.ptr);
    }

    public int getDisplayStart() {
        return nGetDisplayStart();
    }

    public void setDisplayStart(int value) {
        nSetDisplayStart(value);
    }

    public int getDisplayEnd() {
        return nGetDisplayEnd();
    }

    public void setDisplayEnd(int value) {
        nSetDisplayEnd(value);
    }

    public int getItemsCount() {
        return nGetItemsCount();
    }

    public float getItemsHeight() {
        return nGetItemsHeight();
    }

    public float getStartPosY() {
        return nGetStartPosY();
    }

    public void begin(int itemsCount) {
        nBegin(itemsCount);
    }

    public void begin(int itemsCount, float itemsHeight) {
        nBegin(itemsCount, itemsHeight);
    }

    public void end() {
        nEnd();
    }

    public boolean step() {
        return nStep();
    }

    public void includeItemByIndex(int itemIndex) {
        nIncludeItemByIndex(itemIndex);
    }

    public void includeItemsByIndex(int itemBegin, int itemEnd) {
        nIncludeItemsByIndex(itemBegin, itemEnd);
    }

    public static void forEach(Consumer<ImGuiListClipper> clipAction) {
        ImGuiListClipper clipper = new ImGuiListClipper();
        clipAction.accept(clipper);
        clipper.destroy();
    }

    public static void forEach(int itemsCount, ImListClipperCallback callback) {
        nForEach(itemsCount, -1, callback);
    }

    public static void forEach(int itemsCount, int itemsHeight, ImListClipperCallback callback) {
        nForEach(itemsCount, itemsHeight, callback);
    }
}
