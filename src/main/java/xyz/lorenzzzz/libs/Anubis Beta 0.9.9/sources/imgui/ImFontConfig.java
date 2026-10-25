package imgui;

import imgui.binding.ImGuiStructDestroyable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/ImFontConfig.class */
public final class ImFontConfig extends ImGuiStructDestroyable {
    private short[] glyphRanges;

    private native long nCreate();

    public native byte[] getFontData();

    public native void setFontData(byte[] bArr);

    private native int nGetFontDataSize();

    private native void nSetFontDataSize(int i);

    private native boolean nGetFontDataOwnedByAtlas();

    private native void nSetFontDataOwnedByAtlas(boolean z);

    private native int nGetFontNo();

    private native void nSetFontNo(int i);

    private native float nGetSizePixels();

    private native void nSetSizePixels(float f);

    private native int nGetOversampleH();

    private native void nSetOversampleH(int i);

    private native int nGetOversampleV();

    private native void nSetOversampleV(int i);

    private native boolean nGetPixelSnapH();

    private native void nSetPixelSnapH(boolean z);

    private native void nGetGlyphExtraSpacing(ImVec2 imVec2);

    private native float nGetGlyphExtraSpacingX();

    private native float nGetGlyphExtraSpacingY();

    private native void nSetGlyphExtraSpacing(float f, float f2);

    private native void nGetGlyphOffset(ImVec2 imVec2);

    private native float nGetGlyphOffsetX();

    private native float nGetGlyphOffsetY();

    private native void nSetGlyphOffset(float f, float f2);

    private native void nSetGlyphRanges(short[] sArr);

    private native float nGetGlyphMinAdvanceX();

    private native void nSetGlyphMinAdvanceX(float f);

    private native float nGetGlyphMaxAdvanceX();

    private native void nSetGlyphMaxAdvanceX(float f);

    private native boolean nGetMergeMode();

    private native void nSetMergeMode(boolean z);

    private native int nGetFontBuilderFlags();

    private native void nSetFontBuilderFlags(int i);

    private native float nGetRasterizerMultiply();

    private native void nSetRasterizerMultiply(float f);

    private native float nGetRasterizerDensity();

    private native void nSetRasterizerDensity(float f);

    private native short nGetEllipsisChar();

    private native void nSetEllipsisChar(short s);

    public native void setName(String str);

    private native long nGetDstFont();

    private native void nSetDstFont(long j);

    public ImFontConfig() {
    }

    ImFontConfig(long ptr) {
        super(ptr);
    }

    @Override // imgui.binding.ImGuiStructDestroyable
    protected long create() {
        return nCreate();
    }

    public int getFontDataSize() {
        return nGetFontDataSize();
    }

    public void setFontDataSize(int value) {
        nSetFontDataSize(value);
    }

    public boolean getFontDataOwnedByAtlas() {
        return nGetFontDataOwnedByAtlas();
    }

    public void setFontDataOwnedByAtlas(boolean value) {
        nSetFontDataOwnedByAtlas(value);
    }

    public int getFontNo() {
        return nGetFontNo();
    }

    public void setFontNo(int value) {
        nSetFontNo(value);
    }

    public float getSizePixels() {
        return nGetSizePixels();
    }

    public void setSizePixels(float value) {
        nSetSizePixels(value);
    }

    public int getOversampleH() {
        return nGetOversampleH();
    }

    public void setOversampleH(int value) {
        nSetOversampleH(value);
    }

    public int getOversampleV() {
        return nGetOversampleV();
    }

    public void setOversampleV(int value) {
        nSetOversampleV(value);
    }

    public boolean getPixelSnapH() {
        return nGetPixelSnapH();
    }

    public void setPixelSnapH(boolean value) {
        nSetPixelSnapH(value);
    }

    public ImVec2 getGlyphExtraSpacing() {
        ImVec2 dst = new ImVec2();
        nGetGlyphExtraSpacing(dst);
        return dst;
    }

    public float getGlyphExtraSpacingX() {
        return nGetGlyphExtraSpacingX();
    }

    public float getGlyphExtraSpacingY() {
        return nGetGlyphExtraSpacingY();
    }

    public void getGlyphExtraSpacing(ImVec2 dst) {
        nGetGlyphExtraSpacing(dst);
    }

    public void setGlyphExtraSpacing(ImVec2 value) {
        nSetGlyphExtraSpacing(value.x, value.y);
    }

    public void setGlyphExtraSpacing(float valueX, float valueY) {
        nSetGlyphExtraSpacing(valueX, valueY);
    }

    public ImVec2 getGlyphOffset() {
        ImVec2 dst = new ImVec2();
        nGetGlyphOffset(dst);
        return dst;
    }

    public float getGlyphOffsetX() {
        return nGetGlyphOffsetX();
    }

    public float getGlyphOffsetY() {
        return nGetGlyphOffsetY();
    }

    public void getGlyphOffset(ImVec2 dst) {
        nGetGlyphOffset(dst);
    }

    public void setGlyphOffset(ImVec2 value) {
        nSetGlyphOffset(value.x, value.y);
    }

    public void setGlyphOffset(float valueX, float valueY) {
        nSetGlyphOffset(valueX, valueY);
    }

    public short[] getGlyphRanges() {
        return this.glyphRanges;
    }

    public void setGlyphRanges(short[] glyphRanges) {
        this.glyphRanges = glyphRanges;
        nSetGlyphRanges(glyphRanges);
    }

    public float getGlyphMinAdvanceX() {
        return nGetGlyphMinAdvanceX();
    }

    public void setGlyphMinAdvanceX(float value) {
        nSetGlyphMinAdvanceX(value);
    }

    public float getGlyphMaxAdvanceX() {
        return nGetGlyphMaxAdvanceX();
    }

    public void setGlyphMaxAdvanceX(float value) {
        nSetGlyphMaxAdvanceX(value);
    }

    public boolean getMergeMode() {
        return nGetMergeMode();
    }

    public void setMergeMode(boolean value) {
        nSetMergeMode(value);
    }

    public int getFontBuilderFlags() {
        return nGetFontBuilderFlags();
    }

    public void setFontBuilderFlags(int value) {
        nSetFontBuilderFlags(value);
    }

    public void addFontBuilderFlags(int flags) {
        setFontBuilderFlags(getFontBuilderFlags() | flags);
    }

    public void removeFontBuilderFlags(int flags) {
        setFontBuilderFlags(getFontBuilderFlags() & (flags ^ (-1)));
    }

    public boolean hasFontBuilderFlags(int flags) {
        return (getFontBuilderFlags() & flags) != 0;
    }

    public float getRasterizerMultiply() {
        return nGetRasterizerMultiply();
    }

    public void setRasterizerMultiply(float value) {
        nSetRasterizerMultiply(value);
    }

    public float getRasterizerDensity() {
        return nGetRasterizerDensity();
    }

    public void setRasterizerDensity(float value) {
        nSetRasterizerDensity(value);
    }

    public short getEllipsisChar() {
        return nGetEllipsisChar();
    }

    public void setEllipsisChar(short value) {
        nSetEllipsisChar(value);
    }

    public ImFont getDstFont() {
        return new ImFont(nGetDstFont());
    }

    public void setDstFont(ImFont value) {
        nSetDstFont(value.ptr);
    }
}
