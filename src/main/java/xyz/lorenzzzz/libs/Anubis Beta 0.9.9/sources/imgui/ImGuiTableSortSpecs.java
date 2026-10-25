package imgui;

import imgui.binding.ImGuiStruct;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/ImGuiTableSortSpecs.class */
public final class ImGuiTableSortSpecs extends ImGuiStruct {
    private native long[] nGetSpecs();

    private native int nGetSpecsCount();

    private native boolean nGetSpecsDirty();

    private native void nSetSpecsDirty(boolean z);

    public ImGuiTableSortSpecs(long ptr) {
        super(ptr);
    }

    public ImGuiTableColumnSortSpecs[] getSpecs() {
        long[] specsPointers = nGetSpecs();
        ImGuiTableColumnSortSpecs[] specs = new ImGuiTableColumnSortSpecs[specsPointers.length];
        for (int i = 0; i < specsPointers.length; i++) {
            specs[i] = new ImGuiTableColumnSortSpecs(specsPointers[i]);
        }
        return specs;
    }

    public int getSpecsCount() {
        return nGetSpecsCount();
    }

    public boolean getSpecsDirty() {
        return nGetSpecsDirty();
    }

    public void setSpecsDirty(boolean value) {
        nSetSpecsDirty(value);
    }
}
