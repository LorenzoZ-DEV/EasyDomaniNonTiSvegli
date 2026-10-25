package imgui;

import imgui.binding.ImGuiStruct;
import imgui.internal.ImGuiContext;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/ImGuiInputTextCallbackData.class */
public class ImGuiInputTextCallbackData extends ImGuiStruct {
    private static final ImGuiContext _GETCTX_1 = new ImGuiContext(0);

    private native long nGetCtx();

    private native void nSetCtx(long j);

    private native int nGetEventFlag();

    private native int nGetFlags();

    private native int nGetEventChar();

    private native void nSetEventChar(int i);

    private native int nGetEventKey();

    private native String nGetBuf();

    private native void nSetBuf(String str);

    private native int nGetBufTextLen();

    private native void nSetBufTextLen(int i);

    private native boolean nGetBufDirty();

    private native void nSetBufDirty(boolean z);

    private native int nGetCursorPos();

    private native void nSetCursorPos(int i);

    private native int nGetSelectionStart();

    private native void nSetSelectionStart(int i);

    private native int nGetSelectionEnd();

    private native void nSetSelectionEnd(int i);

    private native void nDeleteChars(int i, int i2);

    private native void nInsertChars(int i, String str);

    private native void nSelectAll();

    private native void nClearSelection();

    private native boolean nHasSelection();

    public ImGuiInputTextCallbackData(long ptr) {
        super(ptr);
    }

    public ImGuiContext getCtx() {
        _GETCTX_1.ptr = nGetCtx();
        return _GETCTX_1;
    }

    public void setCtx(ImGuiContext value) {
        nSetCtx(value.ptr);
    }

    public int getEventFlag() {
        return nGetEventFlag();
    }

    public boolean hasEventFlag(int flags) {
        return (getEventFlag() & flags) != 0;
    }

    public int getFlags() {
        return nGetFlags();
    }

    public boolean hasFlags(int flags) {
        return (getFlags() & flags) != 0;
    }

    public int getEventChar() {
        return nGetEventChar();
    }

    public void setEventChar(int value) {
        nSetEventChar(value);
    }

    public int getEventKey() {
        return nGetEventKey();
    }

    public String getBuf() {
        return nGetBuf();
    }

    public void setBuf(String value) {
        nSetBuf(value);
    }

    public int getBufTextLen() {
        return nGetBufTextLen();
    }

    public void setBufTextLen(int value) {
        nSetBufTextLen(value);
    }

    public boolean getBufDirty() {
        return nGetBufDirty();
    }

    public void setBufDirty(boolean value) {
        nSetBufDirty(value);
    }

    public int getCursorPos() {
        return nGetCursorPos();
    }

    public void setCursorPos(int value) {
        nSetCursorPos(value);
    }

    public int getSelectionStart() {
        return nGetSelectionStart();
    }

    public void setSelectionStart(int value) {
        nSetSelectionStart(value);
    }

    public int getSelectionEnd() {
        return nGetSelectionEnd();
    }

    public void setSelectionEnd(int value) {
        nSetSelectionEnd(value);
    }

    public void deleteChars(int pos, int bytesCount) {
        nDeleteChars(pos, bytesCount);
    }

    public void insertChars(int pos, String str) {
        nInsertChars(pos, str);
    }

    public void selectAll() {
        nSelectAll();
    }

    public void clearSelection() {
        nClearSelection();
    }

    public boolean hasSelection() {
        return nHasSelection();
    }
}
