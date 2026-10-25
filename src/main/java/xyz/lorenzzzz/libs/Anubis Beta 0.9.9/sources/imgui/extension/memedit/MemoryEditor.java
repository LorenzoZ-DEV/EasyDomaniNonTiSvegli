package imgui.extension.memedit;

import imgui.binding.ImGuiStructDestroyable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/extension/memedit/MemoryEditor.class */
public final class MemoryEditor extends ImGuiStructDestroyable {
    private native long nCreate();

    private native boolean nGetOpen();

    private native void nSetOpen(boolean z);

    private native boolean nGetReadOnly();

    private native void nSetReadOnly(boolean z);

    private native int nGetCols();

    private native void nSetCols(int i);

    private native boolean nGetOptShowOptions();

    private native void nSetOptShowOptions(boolean z);

    private native boolean nGetOptShowDataPreview();

    private native void nSetOptShowDataPreview(boolean z);

    private native boolean nGetOptShowHexII();

    private native void nSetOptShowHexII(boolean z);

    private native boolean nGetOptShowAscii();

    private native void nSetOptShowAscii(boolean z);

    private native boolean nGetOptGreyOutZeroes();

    private native void nSetOptGreyOutZeroes(boolean z);

    private native boolean nGetOptUpperCaseHex();

    private native void nSetOptUpperCaseHex(boolean z);

    private native int nGetOptMidColsCount();

    private native void nSetOptMidColsCount(int i);

    private native int nGetOptAddrDigitsCount();

    private native void nSetOptAddrDigitsCount(int i);

    private native float nGetOptFooterExtraHeight();

    private native void nSetOptFooterExtraHeight(float f);

    private native int nGetHighlightColor();

    private native void nSetHighlightColor(int i);

    private native void nGotoAddrAndHighlight(long j, long j2);

    private native void nCalcSizes(long j, long j2, long j3);

    private native void nDrawWindow(String str, long j, long j2);

    private native void nDrawWindow(String str, long j, long j2, long j3);

    private native void nDrawContents(long j, long j2);

    private native void nDrawContents(long j, long j2, long j3);

    private native void nDrawOptionsLine(long j, long j2, long j3, long j4);

    private native void nDrawPreviewLine(long j, long j2, long j3, long j4);

    public MemoryEditor() {
    }

    public MemoryEditor(long ptr) {
        super(ptr);
    }

    @Override // imgui.binding.ImGuiStructDestroyable
    protected long create() {
        return nCreate();
    }

    public boolean getOpen() {
        return nGetOpen();
    }

    public void setOpen(boolean value) {
        nSetOpen(value);
    }

    public boolean getReadOnly() {
        return nGetReadOnly();
    }

    public void setReadOnly(boolean value) {
        nSetReadOnly(value);
    }

    public int getCols() {
        return nGetCols();
    }

    public void setCols(int value) {
        nSetCols(value);
    }

    public boolean getOptShowOptions() {
        return nGetOptShowOptions();
    }

    public void setOptShowOptions(boolean value) {
        nSetOptShowOptions(value);
    }

    public boolean getOptShowDataPreview() {
        return nGetOptShowDataPreview();
    }

    public void setOptShowDataPreview(boolean value) {
        nSetOptShowDataPreview(value);
    }

    public boolean getOptShowHexII() {
        return nGetOptShowHexII();
    }

    public void setOptShowHexII(boolean value) {
        nSetOptShowHexII(value);
    }

    public boolean getOptShowAscii() {
        return nGetOptShowAscii();
    }

    public void setOptShowAscii(boolean value) {
        nSetOptShowAscii(value);
    }

    public boolean getOptGreyOutZeroes() {
        return nGetOptGreyOutZeroes();
    }

    public void setOptGreyOutZeroes(boolean value) {
        nSetOptGreyOutZeroes(value);
    }

    public boolean getOptUpperCaseHex() {
        return nGetOptUpperCaseHex();
    }

    public void setOptUpperCaseHex(boolean value) {
        nSetOptUpperCaseHex(value);
    }

    public int getOptMidColsCount() {
        return nGetOptMidColsCount();
    }

    public void setOptMidColsCount(int value) {
        nSetOptMidColsCount(value);
    }

    public int getOptAddrDigitsCount() {
        return nGetOptAddrDigitsCount();
    }

    public void setOptAddrDigitsCount(int value) {
        nSetOptAddrDigitsCount(value);
    }

    public float getOptFooterExtraHeight() {
        return nGetOptFooterExtraHeight();
    }

    public void setOptFooterExtraHeight(float value) {
        nSetOptFooterExtraHeight(value);
    }

    public int getHighlightColor() {
        return nGetHighlightColor();
    }

    public void setHighlightColor(int value) {
        nSetHighlightColor(value);
    }

    public void gotoAddrAndHighlight(long addrMin, long addrMax) {
        nGotoAddrAndHighlight(addrMin, addrMax);
    }

    public void calcSizes(MemoryEditorSizes s, long memSize, long baseDisplayAddr) {
        nCalcSizes(s.ptr, memSize, baseDisplayAddr);
    }

    public void drawWindow(String title, long memData, long memSize) {
        nDrawWindow(title, memData, memSize);
    }

    public void drawWindow(String title, long memData, long memSize, long baseDisplayAddr) {
        nDrawWindow(title, memData, memSize, baseDisplayAddr);
    }

    public void drawContents(long memData, long memSize) {
        nDrawContents(memData, memSize);
    }

    public void drawContents(long memData, long memSize, long baseDisplayAddr) {
        nDrawContents(memData, memSize, baseDisplayAddr);
    }

    public void drawOptionsLine(MemoryEditorSizes s, long memData, long memSize, long baseDisplayAddr) {
        nDrawOptionsLine(s.ptr, memData, memSize, baseDisplayAddr);
    }

    public void drawPreviewLine(MemoryEditorSizes s, long memDataVoid, long memSize, long baseDisplayAddr) {
        nDrawPreviewLine(s.ptr, memDataVoid, memSize, baseDisplayAddr);
    }
}
