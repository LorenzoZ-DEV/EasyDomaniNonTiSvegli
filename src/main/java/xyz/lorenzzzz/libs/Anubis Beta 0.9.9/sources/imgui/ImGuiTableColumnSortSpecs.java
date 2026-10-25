package imgui;

import imgui.binding.ImGuiStruct;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/ImGuiTableColumnSortSpecs.class */
public final class ImGuiTableColumnSortSpecs extends ImGuiStruct {
    private native int nGetColumnUserID();

    private native int nGetColumnIndex();

    private native int nGetSortOrder();

    private native int nGetSortDirection();

    public ImGuiTableColumnSortSpecs(long ptr) {
        super(ptr);
    }

    public int getColumnUserID() {
        return nGetColumnUserID();
    }

    public int getColumnIndex() {
        return nGetColumnIndex();
    }

    public int getSortOrder() {
        return nGetSortOrder();
    }

    public int getSortDirection() {
        return nGetSortDirection();
    }
}
