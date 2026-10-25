package imgui;

import imgui.binding.ImGuiStructDestroyable;
import imgui.type.ImInt;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/ImFontAtlas.class */
public final class ImFontAtlas extends ImGuiStructDestroyable {
    private ByteBuffer alpha8pixels;
    private ByteBuffer rgba32pixels;

    static native void nInit();

    private native long nCreate();

    private native long nAddFont(long j);

    private native long nAddFontDefault();

    private native long nAddFontDefault(long j);

    private native long nAddFontFromFileTTF(String str, float f);

    private native long nAddFontFromFileTTF(String str, float f, long j);

    private native long nAddFontFromFileTTF(String str, float f, long j, short[] sArr);

    private native long nAddFontFromFileTTF(String str, float f, short[] sArr);

    private native long nAddFontFromMemoryTTF(byte[] bArr, float f);

    private native long nAddFontFromMemoryTTF(byte[] bArr, float f, long j);

    private native long nAddFontFromMemoryTTF(byte[] bArr, float f, long j, short[] sArr);

    private native long nAddFontFromMemoryTTF(byte[] bArr, float f, short[] sArr);

    private native long nAddFontFromMemoryTTF(byte[] bArr, int i, float f);

    private native long nAddFontFromMemoryTTF(byte[] bArr, int i, float f, long j);

    private native long nAddFontFromMemoryTTF(byte[] bArr, int i, float f, long j, short[] sArr);

    private native long nAddFontFromMemoryTTF(byte[] bArr, int i, float f, short[] sArr);

    private native long nAddFontFromMemoryCompressedTTF(byte[] bArr, float f);

    private native long nAddFontFromMemoryCompressedTTF(byte[] bArr, float f, long j);

    private native long nAddFontFromMemoryCompressedTTF(byte[] bArr, float f, long j, short[] sArr);

    private native long nAddFontFromMemoryCompressedTTF(byte[] bArr, float f, short[] sArr);

    private native long nAddFontFromMemoryCompressedTTF(byte[] bArr, int i, float f);

    private native long nAddFontFromMemoryCompressedTTF(byte[] bArr, int i, float f, long j);

    private native long nAddFontFromMemoryCompressedTTF(byte[] bArr, int i, float f, long j, short[] sArr);

    private native long nAddFontFromMemoryCompressedTTF(byte[] bArr, int i, float f, short[] sArr);

    private native long nAddFontFromMemoryCompressedBase85TTF(String str, float f, long j);

    private native long nAddFontFromMemoryCompressedBase85TTF(String str, float f, long j, short[] sArr);

    private native void nClearInputData();

    private native void nClearTexData();

    private native void nClearFonts();

    private native void nClear();

    public native void setFreeTypeRenderer(boolean z);

    private native boolean nBuild();

    private native void getTexDataAsAlpha8(int[] iArr, int[] iArr2, int[] iArr3);

    private native void nGetTexDataAsRGBA32(int[] iArr, int[] iArr2, int[] iArr3);

    private native boolean nIsBuilt();

    private native void nSetTexID(long j);

    public native short[] getGlyphRangesDefault();

    public native short[] getGlyphRangesGreek();

    public native short[] getGlyphRangesKorean();

    public native short[] getGlyphRangesJapanese();

    public native short[] getGlyphRangesChineseFull();

    public native short[] getGlyphRangesChineseSimplifiedCommon();

    public native short[] getGlyphRangesCyrillic();

    public native short[] getGlyphRangesThai();

    public native short[] getGlyphRangesVietnamese();

    private native int nAddCustomRectRegular(int i, int i2);

    private native int nAddCustomRectFontGlyph(long j, short s, int i, int i2, float f);

    private native int nAddCustomRectFontGlyph(long j, short s, int i, int i2, float f, float f2, float f3);

    private native int nGetFlags();

    private native void nSetFlags(int i);

    private native int nGetTexDesiredWidth();

    private native void nSetTexDesiredWidth(int i);

    private native int nGetTexGlyphPadding();

    private native void nSetTexGlyphPadding(int i);

    private native boolean nGetLocked();

    private native void nSetLocked(boolean z);

    public ImFontAtlas() {
        this.alpha8pixels = null;
        this.rgba32pixels = null;
    }

    public ImFontAtlas(long ptr) {
        super(ptr);
        this.alpha8pixels = null;
        this.rgba32pixels = null;
    }

    @Override // imgui.binding.ImGuiStructDestroyable
    protected long create() {
        return nCreate();
    }

    public ImFont addFont(ImFontConfig imFontConfig) {
        return new ImFont(nAddFont(imFontConfig.ptr));
    }

    public ImFont addFontDefault() {
        return new ImFont(nAddFontDefault());
    }

    public ImFont addFontDefault(ImFontConfig imFontConfig) {
        return new ImFont(nAddFontDefault(imFontConfig.ptr));
    }

    public ImFont addFontFromFileTTF(String filename, float sizePixels) {
        return new ImFont(nAddFontFromFileTTF(filename, sizePixels));
    }

    public ImFont addFontFromFileTTF(String filename, float sizePixels, ImFontConfig fontConfig) {
        return new ImFont(nAddFontFromFileTTF(filename, sizePixels, fontConfig.ptr));
    }

    public ImFont addFontFromFileTTF(String filename, float sizePixels, ImFontConfig fontConfig, short[] glyphRanges) {
        return new ImFont(nAddFontFromFileTTF(filename, sizePixels, fontConfig.ptr, glyphRanges));
    }

    public ImFont addFontFromFileTTF(String filename, float sizePixels, short[] glyphRanges) {
        return new ImFont(nAddFontFromFileTTF(filename, sizePixels, glyphRanges));
    }

    public ImFont addFontFromMemoryTTF(byte[] fontData, float sizePixels) {
        return new ImFont(nAddFontFromMemoryTTF(fontData, sizePixels));
    }

    public ImFont addFontFromMemoryTTF(byte[] fontData, float sizePixels, ImFontConfig fontConfig) {
        return new ImFont(nAddFontFromMemoryTTF(fontData, sizePixels, fontConfig.ptr));
    }

    public ImFont addFontFromMemoryTTF(byte[] fontData, float sizePixels, ImFontConfig fontConfig, short[] glyphRanges) {
        return new ImFont(nAddFontFromMemoryTTF(fontData, sizePixels, fontConfig.ptr, glyphRanges));
    }

    public ImFont addFontFromMemoryTTF(byte[] fontData, float sizePixels, short[] glyphRanges) {
        return new ImFont(nAddFontFromMemoryTTF(fontData, sizePixels, glyphRanges));
    }

    public ImFont addFontFromMemoryTTF(byte[] fontData, int fontDataSize, float sizePixels) {
        return new ImFont(nAddFontFromMemoryTTF(fontData, fontDataSize, sizePixels));
    }

    public ImFont addFontFromMemoryTTF(byte[] fontData, int fontDataSize, float sizePixels, ImFontConfig fontConfig) {
        return new ImFont(nAddFontFromMemoryTTF(fontData, fontDataSize, sizePixels, fontConfig.ptr));
    }

    public ImFont addFontFromMemoryTTF(byte[] fontData, int fontDataSize, float sizePixels, ImFontConfig fontConfig, short[] glyphRanges) {
        return new ImFont(nAddFontFromMemoryTTF(fontData, fontDataSize, sizePixels, fontConfig.ptr, glyphRanges));
    }

    public ImFont addFontFromMemoryTTF(byte[] fontData, int fontDataSize, float sizePixels, short[] glyphRanges) {
        return new ImFont(nAddFontFromMemoryTTF(fontData, fontDataSize, sizePixels, glyphRanges));
    }

    public ImFont addFontFromMemoryCompressedTTF(byte[] compressedFontData, float sizePixels) {
        return new ImFont(nAddFontFromMemoryCompressedTTF(compressedFontData, sizePixels));
    }

    public ImFont addFontFromMemoryCompressedTTF(byte[] compressedFontData, float sizePixels, ImFontConfig imFontConfig) {
        return new ImFont(nAddFontFromMemoryCompressedTTF(compressedFontData, sizePixels, imFontConfig.ptr));
    }

    public ImFont addFontFromMemoryCompressedTTF(byte[] compressedFontData, float sizePixels, ImFontConfig imFontConfig, short[] glyphRanges) {
        return new ImFont(nAddFontFromMemoryCompressedTTF(compressedFontData, sizePixels, imFontConfig.ptr, glyphRanges));
    }

    public ImFont addFontFromMemoryCompressedTTF(byte[] compressedFontData, float sizePixels, short[] glyphRanges) {
        return new ImFont(nAddFontFromMemoryCompressedTTF(compressedFontData, sizePixels, glyphRanges));
    }

    public ImFont addFontFromMemoryCompressedTTF(byte[] compressedFontData, int compressedFontDataSize, float sizePixels) {
        return new ImFont(nAddFontFromMemoryCompressedTTF(compressedFontData, compressedFontDataSize, sizePixels));
    }

    public ImFont addFontFromMemoryCompressedTTF(byte[] compressedFontData, int compressedFontDataSize, float sizePixels, ImFontConfig imFontConfig) {
        return new ImFont(nAddFontFromMemoryCompressedTTF(compressedFontData, compressedFontDataSize, sizePixels, imFontConfig.ptr));
    }

    public ImFont addFontFromMemoryCompressedTTF(byte[] compressedFontData, int compressedFontDataSize, float sizePixels, ImFontConfig imFontConfig, short[] glyphRanges) {
        return new ImFont(nAddFontFromMemoryCompressedTTF(compressedFontData, compressedFontDataSize, sizePixels, imFontConfig.ptr, glyphRanges));
    }

    public ImFont addFontFromMemoryCompressedTTF(byte[] compressedFontData, int compressedFontDataSize, float sizePixels, short[] glyphRanges) {
        return new ImFont(nAddFontFromMemoryCompressedTTF(compressedFontData, compressedFontDataSize, sizePixels, glyphRanges));
    }

    public ImFont addFontFromMemoryCompressedBase85TTF(String compressedFontDataBase85, float sizePixels, ImFontConfig fontConfig) {
        return new ImFont(nAddFontFromMemoryCompressedBase85TTF(compressedFontDataBase85, sizePixels, fontConfig.ptr));
    }

    public ImFont addFontFromMemoryCompressedBase85TTF(String compressedFontDataBase85, float sizePixels, ImFontConfig fontConfig, short[] glyphRanges) {
        return new ImFont(nAddFontFromMemoryCompressedBase85TTF(compressedFontDataBase85, sizePixels, fontConfig.ptr, glyphRanges));
    }

    public void clearInputData() {
        nClearInputData();
    }

    public void clearTexData() {
        nClearTexData();
    }

    public void clearFonts() {
        nClearFonts();
    }

    public void clear() {
        nClear();
    }

    public boolean build() {
        return nBuild();
    }

    public ByteBuffer getTexDataAsAlpha8(ImInt outWidth, ImInt outHeight) {
        return getTexDataAsAlpha8(outWidth, outHeight, new ImInt());
    }

    public ByteBuffer getTexDataAsAlpha8(ImInt outWidth, ImInt outHeight, ImInt outBytesPerPixel) {
        getTexDataAsAlpha8(outWidth.getData(), outHeight.getData(), outBytesPerPixel.getData());
        return this.alpha8pixels;
    }

    private ByteBuffer createAlpha8Pixels(int size) {
        if (this.alpha8pixels == null || this.alpha8pixels.limit() != size) {
            this.alpha8pixels = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
        } else {
            this.alpha8pixels.clear();
        }
        return this.alpha8pixels;
    }

    public ByteBuffer getTexDataAsRGBA32(ImInt outWidth, ImInt outHeight) {
        return getTexDataAsRGBA32(outWidth, outHeight, new ImInt());
    }

    public ByteBuffer getTexDataAsRGBA32(ImInt outWidth, ImInt outHeight, ImInt outBytesPerPixel) {
        nGetTexDataAsRGBA32(outWidth.getData(), outHeight.getData(), outBytesPerPixel.getData());
        return this.rgba32pixels;
    }

    private ByteBuffer createRgba32Pixels(int size) {
        if (this.rgba32pixels == null || this.rgba32pixels.limit() != size) {
            this.rgba32pixels = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
        } else {
            this.rgba32pixels.clear();
        }
        return this.rgba32pixels;
    }

    public boolean isBuilt() {
        return nIsBuilt();
    }

    public void setTexID(long textureID) {
        nSetTexID(textureID);
    }

    public int addCustomRectRegular(int width, int height) {
        return nAddCustomRectRegular(width, height);
    }

    public int addCustomRectFontGlyph(ImFont imFont, short id, int width, int height, float advanceX) {
        return nAddCustomRectFontGlyph(imFont.ptr, id, width, height, advanceX);
    }

    public int addCustomRectFontGlyph(ImFont imFont, short id, int width, int height, float advanceX, ImVec2 offset) {
        return nAddCustomRectFontGlyph(imFont.ptr, id, width, height, advanceX, offset.x, offset.y);
    }

    public int addCustomRectFontGlyph(ImFont imFont, short id, int width, int height, float advanceX, float offsetX, float offsetY) {
        return nAddCustomRectFontGlyph(imFont.ptr, id, width, height, advanceX, offsetX, offsetY);
    }

    public int getFlags() {
        return nGetFlags();
    }

    public void setFlags(int value) {
        nSetFlags(value);
    }

    public void addFlags(int flags) {
        setFlags(getFlags() | flags);
    }

    public void removeFlags(int flags) {
        setFlags(getFlags() & (flags ^ (-1)));
    }

    public boolean hasFlags(int flags) {
        return (getFlags() & flags) != 0;
    }

    public int getTexDesiredWidth() {
        return nGetTexDesiredWidth();
    }

    public void setTexDesiredWidth(int value) {
        nSetTexDesiredWidth(value);
    }

    public int getTexGlyphPadding() {
        return nGetTexGlyphPadding();
    }

    public void setTexGlyphPadding(int value) {
        nSetTexGlyphPadding(value);
    }

    public boolean getLocked() {
        return nGetLocked();
    }

    public void setLocked(boolean value) {
        nSetLocked(value);
    }
}
