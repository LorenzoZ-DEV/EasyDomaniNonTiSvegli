package imgui.extension.memedit;

import imgui.binding.ImGuiStructDestroyable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/extension/memedit/MemoryEditorSizes.class */
public final class MemoryEditorSizes extends ImGuiStructDestroyable {
    private native long nCreate();

    private native int nGetAddrDigitsCount();

    private native void nSetAddrDigitsCount(int i);

    private native float nGetLineHeight();

    private native void nSetLineHeight(float f);

    private native float nGetGlyphWidth();

    private native void nSetGlyphWidth(float f);

    private native float nGetHexCellWidth();

    private native void nSetHexCellWidth(float f);

    private native float nGetSpacingBetweenMidCols();

    private native void nSetSpacingBetweenMidCols(float f);

    private native float nGetPosHexStart();

    private native void nSetPosHexStart(float f);

    private native float nGetPosHexEnd();

    private native void nSetPosHexEnd(float f);

    private native float nGetPosAsciiStart();

    private native void nSetPosAsciiStart(float f);

    private native float nGetPosAsciiEnd();

    private native void nSetPosAsciiEnd(float f);

    private native float nGetWindowWidth();

    private native void nSetWindowWidth(float f);

    public MemoryEditorSizes() {
    }

    public MemoryEditorSizes(long ptr) {
        super(ptr);
    }

    @Override // imgui.binding.ImGuiStructDestroyable
    protected long create() {
        return nCreate();
    }

    public int getAddrDigitsCount() {
        return nGetAddrDigitsCount();
    }

    public void setAddrDigitsCount(int value) {
        nSetAddrDigitsCount(value);
    }

    public float getLineHeight() {
        return nGetLineHeight();
    }

    public void setLineHeight(float value) {
        nSetLineHeight(value);
    }

    public float getGlyphWidth() {
        return nGetGlyphWidth();
    }

    public void setGlyphWidth(float value) {
        nSetGlyphWidth(value);
    }

    public float getHexCellWidth() {
        return nGetHexCellWidth();
    }

    public void setHexCellWidth(float value) {
        nSetHexCellWidth(value);
    }

    public float getSpacingBetweenMidCols() {
        return nGetSpacingBetweenMidCols();
    }

    public void setSpacingBetweenMidCols(float value) {
        nSetSpacingBetweenMidCols(value);
    }

    public float getPosHexStart() {
        return nGetPosHexStart();
    }

    public void setPosHexStart(float value) {
        nSetPosHexStart(value);
    }

    public float getPosHexEnd() {
        return nGetPosHexEnd();
    }

    public void setPosHexEnd(float value) {
        nSetPosHexEnd(value);
    }

    public float getPosAsciiStart() {
        return nGetPosAsciiStart();
    }

    public void setPosAsciiStart(float value) {
        nSetPosAsciiStart(value);
    }

    public float getPosAsciiEnd() {
        return nGetPosAsciiEnd();
    }

    public void setPosAsciiEnd(float value) {
        nSetPosAsciiEnd(value);
    }

    public float getWindowWidth() {
        return nGetWindowWidth();
    }

    public void setWindowWidth(float value) {
        nSetWindowWidth(value);
    }
}
