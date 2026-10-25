package imgui;

import imgui.binding.ImGuiStructDestroyable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/ImFont.class */
public final class ImFont extends ImGuiStructDestroyable {
    private static final ImFontGlyph _GETFALLBACKGLYPH_1 = new ImFontGlyph(0);

    private native long nCreate();

    private native float nGetFallbackAdvanceX();

    private native void nSetFallbackAdvanceX(float f);

    private native float nGetFontSize();

    private native void nSetFontSize(float f);

    private native long nGetFallbackGlyph();

    private native void nSetFallbackGlyph(long j);

    private native short nGetConfigDataCount();

    private native void nSetConfigDataCount(short s);

    private native short nGetEllipsisChar();

    private native void nSetEllipsisChar(short s);

    private native short nGetEllipsisCharCount();

    private native void nSetEllipsisCharCount(short s);

    private native float nGetEllipsisWidth();

    private native void nSetEllipsisWidth(float f);

    private native float nGetEllipsisCharStep();

    private native void nSetEllipsisCharStep(float f);

    private native boolean nGetDirtyLookupTables();

    private native void nSetDirtyLookupTables(boolean z);

    private native float nGetScale();

    private native void nSetScale(float f);

    private native float nGetAscent();

    private native void nSetAscent(float f);

    private native float nGetDescent();

    private native void nSetDescent(float f);

    private native int nGetMetricsTotalSurface();

    private native void nSetMetricsTotalSurface(int i);

    private native long nFindGlyph(int i);

    private native long nFindGlyphNoFallback(int i);

    private native float nGetCharAdvance(int i);

    private native boolean nIsLoaded();

    private native String nGetDebugName();

    private native void nCalcTextSizeA(ImVec2 imVec2, float f, float f2, float f3, String str);

    private native float nCalcTextSizeAX(float f, float f2, float f3, String str);

    private native float nCalcTextSizeAY(float f, float f2, float f3, String str);

    private native void nCalcTextSizeA(ImVec2 imVec2, float f, float f2, float f3, String str, String str2);

    private native float nCalcTextSizeAX(float f, float f2, float f3, String str, String str2);

    private native float nCalcTextSizeAY(float f, float f2, float f3, String str, String str2);

    private native String nCalcWordWrapPositionA(float f, String str, String str2, float f2);

    private native void nRenderChar(long j, float f, float f2, float f3, int i, int i2);

    private native void nRenderText(long j, float f, float f2, float f3, int i, float f4, float f5, float f6, float f7, String str, String str2);

    private native void nRenderText(long j, float f, float f2, float f3, int i, float f4, float f5, float f6, float f7, String str, String str2, float f8);

    private native void nRenderText(long j, float f, float f2, float f3, int i, float f4, float f5, float f6, float f7, String str, String str2, float f8, boolean z);

    private native void nRenderText(long j, float f, float f2, float f3, int i, float f4, float f5, float f6, float f7, String str, String str2, boolean z);

    public ImFont() {
    }

    public ImFont(long ptr) {
        super(ptr);
    }

    @Override // imgui.binding.ImGuiStructDestroyable
    protected long create() {
        return nCreate();
    }

    public float getFallbackAdvanceX() {
        return nGetFallbackAdvanceX();
    }

    public void setFallbackAdvanceX(float value) {
        nSetFallbackAdvanceX(value);
    }

    public float getFontSize() {
        return nGetFontSize();
    }

    public void setFontSize(float value) {
        nSetFontSize(value);
    }

    public ImFontGlyph getFallbackGlyph() {
        _GETFALLBACKGLYPH_1.ptr = nGetFallbackGlyph();
        return _GETFALLBACKGLYPH_1;
    }

    public void setFallbackGlyph(ImFontGlyph value) {
        nSetFallbackGlyph(value.ptr);
    }

    public short getConfigDataCount() {
        return nGetConfigDataCount();
    }

    public void setConfigDataCount(short value) {
        nSetConfigDataCount(value);
    }

    public short getEllipsisChar() {
        return nGetEllipsisChar();
    }

    public void setEllipsisChar(short value) {
        nSetEllipsisChar(value);
    }

    public short getEllipsisCharCount() {
        return nGetEllipsisCharCount();
    }

    public void setEllipsisCharCount(short value) {
        nSetEllipsisCharCount(value);
    }

    public float getEllipsisWidth() {
        return nGetEllipsisWidth();
    }

    public void setEllipsisWidth(float value) {
        nSetEllipsisWidth(value);
    }

    public float getEllipsisCharStep() {
        return nGetEllipsisCharStep();
    }

    public void setEllipsisCharStep(float value) {
        nSetEllipsisCharStep(value);
    }

    public boolean getDirtyLookupTables() {
        return nGetDirtyLookupTables();
    }

    public void setDirtyLookupTables(boolean value) {
        nSetDirtyLookupTables(value);
    }

    public float getScale() {
        return nGetScale();
    }

    public void setScale(float value) {
        nSetScale(value);
    }

    public float getAscent() {
        return nGetAscent();
    }

    public void setAscent(float value) {
        nSetAscent(value);
    }

    public float getDescent() {
        return nGetDescent();
    }

    public void setDescent(float value) {
        nSetDescent(value);
    }

    public int getMetricsTotalSurface() {
        return nGetMetricsTotalSurface();
    }

    public void setMetricsTotalSurface(int value) {
        nSetMetricsTotalSurface(value);
    }

    public ImFontGlyph findGlyph(int c) {
        return new ImFontGlyph(nFindGlyph(c));
    }

    public ImFontGlyph findGlyphNoFallback(int c) {
        return new ImFontGlyph(nFindGlyphNoFallback(c));
    }

    public float getCharAdvance(int c) {
        return nGetCharAdvance(c);
    }

    public boolean isLoaded() {
        return nIsLoaded();
    }

    public String getDebugName() {
        return nGetDebugName();
    }

    public ImVec2 calcTextSizeA(float size, float maxWidth, float wrapWidth, String textBegin) {
        ImVec2 dst = new ImVec2();
        nCalcTextSizeA(dst, size, maxWidth, wrapWidth, textBegin);
        return dst;
    }

    public float calcTextSizeAX(float size, float maxWidth, float wrapWidth, String textBegin) {
        return nCalcTextSizeAX(size, maxWidth, wrapWidth, textBegin);
    }

    public float calcTextSizeAY(float size, float maxWidth, float wrapWidth, String textBegin) {
        return nCalcTextSizeAY(size, maxWidth, wrapWidth, textBegin);
    }

    public void calcTextSizeA(ImVec2 dst, float size, float maxWidth, float wrapWidth, String textBegin) {
        nCalcTextSizeA(dst, size, maxWidth, wrapWidth, textBegin);
    }

    public ImVec2 calcTextSizeA(float size, float maxWidth, float wrapWidth, String textBegin, String textEnd) {
        ImVec2 dst = new ImVec2();
        nCalcTextSizeA(dst, size, maxWidth, wrapWidth, textBegin, textEnd);
        return dst;
    }

    public float calcTextSizeAX(float size, float maxWidth, float wrapWidth, String textBegin, String textEnd) {
        return nCalcTextSizeAX(size, maxWidth, wrapWidth, textBegin, textEnd);
    }

    public float calcTextSizeAY(float size, float maxWidth, float wrapWidth, String textBegin, String textEnd) {
        return nCalcTextSizeAY(size, maxWidth, wrapWidth, textBegin, textEnd);
    }

    public void calcTextSizeA(ImVec2 dst, float size, float maxWidth, float wrapWidth, String textBegin, String textEnd) {
        nCalcTextSizeA(dst, size, maxWidth, wrapWidth, textBegin, textEnd);
    }

    public String calcWordWrapPositionA(float scale, String text, String textEnd, float wrapWidth) {
        return nCalcWordWrapPositionA(scale, text, textEnd, wrapWidth);
    }

    public void renderChar(ImDrawList drawList, float size, ImVec2 pos, int col, int c) {
        nRenderChar(drawList.ptr, size, pos.x, pos.y, col, c);
    }

    public void renderChar(ImDrawList drawList, float size, float posX, float posY, int col, int c) {
        nRenderChar(drawList.ptr, size, posX, posY, col, c);
    }

    public void renderText(ImDrawList drawList, float size, ImVec2 pos, int col, ImVec4 clipRect, String textBegin, String textEnd) {
        nRenderText(drawList.ptr, size, pos.x, pos.y, col, clipRect.x, clipRect.y, clipRect.z, clipRect.w, textBegin, textEnd);
    }

    public void renderText(ImDrawList drawList, float size, float posX, float posY, int col, float clipRectX, float clipRectY, float clipRectZ, float clipRectW, String textBegin, String textEnd) {
        nRenderText(drawList.ptr, size, posX, posY, col, clipRectX, clipRectY, clipRectZ, clipRectW, textBegin, textEnd);
    }

    public void renderText(ImDrawList drawList, float size, ImVec2 pos, int col, ImVec4 clipRect, String textBegin, String textEnd, float wrapWidth) {
        nRenderText(drawList.ptr, size, pos.x, pos.y, col, clipRect.x, clipRect.y, clipRect.z, clipRect.w, textBegin, textEnd, wrapWidth);
    }

    public void renderText(ImDrawList drawList, float size, float posX, float posY, int col, float clipRectX, float clipRectY, float clipRectZ, float clipRectW, String textBegin, String textEnd, float wrapWidth) {
        nRenderText(drawList.ptr, size, posX, posY, col, clipRectX, clipRectY, clipRectZ, clipRectW, textBegin, textEnd, wrapWidth);
    }

    public void renderText(ImDrawList drawList, float size, ImVec2 pos, int col, ImVec4 clipRect, String textBegin, String textEnd, float wrapWidth, boolean cpuFineClip) {
        nRenderText(drawList.ptr, size, pos.x, pos.y, col, clipRect.x, clipRect.y, clipRect.z, clipRect.w, textBegin, textEnd, wrapWidth, cpuFineClip);
    }

    public void renderText(ImDrawList drawList, float size, float posX, float posY, int col, float clipRectX, float clipRectY, float clipRectZ, float clipRectW, String textBegin, String textEnd, float wrapWidth, boolean cpuFineClip) {
        nRenderText(drawList.ptr, size, posX, posY, col, clipRectX, clipRectY, clipRectZ, clipRectW, textBegin, textEnd, wrapWidth, cpuFineClip);
    }

    public void renderText(ImDrawList drawList, float size, ImVec2 pos, int col, ImVec4 clipRect, String textBegin, String textEnd, boolean cpuFineClip) {
        nRenderText(drawList.ptr, size, pos.x, pos.y, col, clipRect.x, clipRect.y, clipRect.z, clipRect.w, textBegin, textEnd, cpuFineClip);
    }

    public void renderText(ImDrawList drawList, float size, float posX, float posY, int col, float clipRectX, float clipRectY, float clipRectZ, float clipRectW, String textBegin, String textEnd, boolean cpuFineClip) {
        nRenderText(drawList.ptr, size, posX, posY, col, clipRectX, clipRectY, clipRectZ, clipRectW, textBegin, textEnd, cpuFineClip);
    }
}
