package imgui;

import imgui.binding.ImGuiStructDestroyable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/ImFontGlyph.class */
public final class ImFontGlyph extends ImGuiStructDestroyable {
    private native long nCreate();

    private native int nGetColored();

    private native void nSetColored(int i);

    private native int nGetVisible();

    private native void nSetVisible(int i);

    private native int nGetCodepoint();

    private native void nSetCodepoint(int i);

    private native float nGetAdvanceX();

    private native void nSetAdvanceX(float f);

    private native float nGetX0();

    private native void nSetX0(float f);

    private native float nGetY0();

    private native void nSetY0(float f);

    private native float nGetX1();

    private native void nSetX1(float f);

    private native float nGetY1();

    private native void nSetY1(float f);

    private native float nGetU0();

    private native void nSetU0(float f);

    private native float nGetV0();

    private native void nSetV0(float f);

    private native float nGetU1();

    private native void nSetU1(float f);

    private native float nGetV1();

    private native void nSetV1(float f);

    public ImFontGlyph() {
    }

    public ImFontGlyph(long ptr) {
        super(ptr);
    }

    @Override // imgui.binding.ImGuiStructDestroyable
    protected long create() {
        return nCreate();
    }

    public int getColored() {
        return nGetColored();
    }

    public void setColored(int value) {
        nSetColored(value);
    }

    public int getVisible() {
        return nGetVisible();
    }

    public void setVisible(int value) {
        nSetVisible(value);
    }

    public int getCodepoint() {
        return nGetCodepoint();
    }

    public void setCodepoint(int value) {
        nSetCodepoint(value);
    }

    public float getAdvanceX() {
        return nGetAdvanceX();
    }

    public void setAdvanceX(float value) {
        nSetAdvanceX(value);
    }

    public float getX0() {
        return nGetX0();
    }

    public void setX0(float value) {
        nSetX0(value);
    }

    public float getY0() {
        return nGetY0();
    }

    public void setY0(float value) {
        nSetY0(value);
    }

    public float getX1() {
        return nGetX1();
    }

    public void setX1(float value) {
        nSetX1(value);
    }

    public float getY1() {
        return nGetY1();
    }

    public void setY1(float value) {
        nSetY1(value);
    }

    public float getU0() {
        return nGetU0();
    }

    public void setU0(float value) {
        nSetU0(value);
    }

    public float getV0() {
        return nGetV0();
    }

    public void setV0(float value) {
        nSetV0(value);
    }

    public float getU1() {
        return nGetU1();
    }

    public void setU1(float value) {
        nSetU1(value);
    }

    public float getV1() {
        return nGetV1();
    }

    public void setV1(float value) {
        nSetV1(value);
    }
}
