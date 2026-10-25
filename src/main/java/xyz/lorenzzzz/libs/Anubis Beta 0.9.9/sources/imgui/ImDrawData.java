package imgui;

import imgui.binding.ImGuiStruct;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/ImDrawData.class */
public final class ImDrawData extends ImGuiStruct {
    private static final int RESIZE_FACTOR = 5000;
    private static ByteBuffer dataBuffer = ByteBuffer.allocateDirect(25000).order(ByteOrder.nativeOrder());

    public native int getCmdListCmdBufferSize(int i);

    public native int getCmdListCmdBufferElemCount(int i, int i2);

    public native void getCmdListCmdBufferClipRect(ImVec4 imVec4, int i, int i2);

    public native long getCmdListCmdBufferTextureId(int i, int i2);

    public native int getCmdListCmdBufferVtxOffset(int i, int i2);

    public native int getCmdListCmdBufferIdxOffset(int i, int i2);

    public native int getCmdListIdxBufferSize(int i);

    private native void nGetCmdListIdxBufferData(int i, ByteBuffer byteBuffer, int i2);

    public native int getCmdListVtxBufferSize(int i);

    private native void nGetCmdListVtxBufferData(int i, ByteBuffer byteBuffer, int i2);

    public static native int sizeOfImDrawVert();

    public static native int sizeOfImDrawIdx();

    private native boolean nGetValid();

    private native int nGetCmdListsCount();

    private native int nGetTotalIdxCount();

    private native int nGetTotalVtxCount();

    private native void nGetDisplayPos(ImVec2 imVec2);

    private native float nGetDisplayPosX();

    private native float nGetDisplayPosY();

    private native void nGetDisplaySize(ImVec2 imVec2);

    private native float nGetDisplaySizeX();

    private native float nGetDisplaySizeY();

    private native void nGetFramebufferScale(ImVec2 imVec2);

    private native float nGetFramebufferScaleX();

    private native float nGetFramebufferScaleY();

    private native long nGetOwnerViewport();

    private native void nClear();

    private native void nAddDrawList(long j);

    private native void nDeIndexAllBuffers();

    private native void nScaleClipRects(float f, float f2);

    public ImDrawData(long ptr) {
        super(ptr);
    }

    public ImVec4 getCmdListCmdBufferClipRect(int cmdListIdx, int cmdBufferIdx) {
        ImVec4 dst = new ImVec4();
        getCmdListCmdBufferClipRect(dst, cmdListIdx, cmdBufferIdx);
        return dst;
    }

    public ByteBuffer getCmdListIdxBufferData(int cmdListIdx) {
        int idxBufferCapacity = getCmdListIdxBufferSize(cmdListIdx) * sizeOfImDrawIdx();
        if (dataBuffer.capacity() < idxBufferCapacity) {
            dataBuffer.clear();
            dataBuffer = ByteBuffer.allocateDirect(idxBufferCapacity + RESIZE_FACTOR).order(ByteOrder.nativeOrder());
        }
        nGetCmdListIdxBufferData(cmdListIdx, dataBuffer, idxBufferCapacity);
        dataBuffer.position(0);
        dataBuffer.limit(idxBufferCapacity);
        return dataBuffer;
    }

    public ByteBuffer getCmdListVtxBufferData(int cmdListIdx) {
        int vtxBufferCapacity = getCmdListVtxBufferSize(cmdListIdx) * sizeOfImDrawVert();
        if (dataBuffer.capacity() < vtxBufferCapacity) {
            dataBuffer.clear();
            dataBuffer = ByteBuffer.allocateDirect(vtxBufferCapacity + RESIZE_FACTOR).order(ByteOrder.nativeOrder());
        }
        nGetCmdListVtxBufferData(cmdListIdx, dataBuffer, vtxBufferCapacity);
        dataBuffer.position(0);
        dataBuffer.limit(vtxBufferCapacity);
        return dataBuffer;
    }

    public boolean getValid() {
        return nGetValid();
    }

    public int getCmdListsCount() {
        return nGetCmdListsCount();
    }

    public int getTotalIdxCount() {
        return nGetTotalIdxCount();
    }

    public int getTotalVtxCount() {
        return nGetTotalVtxCount();
    }

    public ImVec2 getDisplayPos() {
        ImVec2 dst = new ImVec2();
        nGetDisplayPos(dst);
        return dst;
    }

    public float getDisplayPosX() {
        return nGetDisplayPosX();
    }

    public float getDisplayPosY() {
        return nGetDisplayPosY();
    }

    public void getDisplayPos(ImVec2 dst) {
        nGetDisplayPos(dst);
    }

    public ImVec2 getDisplaySize() {
        ImVec2 dst = new ImVec2();
        nGetDisplaySize(dst);
        return dst;
    }

    public float getDisplaySizeX() {
        return nGetDisplaySizeX();
    }

    public float getDisplaySizeY() {
        return nGetDisplaySizeY();
    }

    public void getDisplaySize(ImVec2 dst) {
        nGetDisplaySize(dst);
    }

    public ImVec2 getFramebufferScale() {
        ImVec2 dst = new ImVec2();
        nGetFramebufferScale(dst);
        return dst;
    }

    public float getFramebufferScaleX() {
        return nGetFramebufferScaleX();
    }

    public float getFramebufferScaleY() {
        return nGetFramebufferScaleY();
    }

    public void getFramebufferScale(ImVec2 dst) {
        nGetFramebufferScale(dst);
    }

    public ImGuiViewport getOwnerViewport() {
        return new ImGuiViewport(nGetOwnerViewport());
    }

    public void clear() {
        nClear();
    }

    public void addDrawList(ImDrawList drawList) {
        nAddDrawList(drawList.ptr);
    }

    public void deIndexAllBuffers() {
        nDeIndexAllBuffers();
    }

    public void scaleClipRects(ImVec2 fbScale) {
        nScaleClipRects(fbScale.x, fbScale.y);
    }

    public void scaleClipRects(float fbScaleX, float fbScaleY) {
        nScaleClipRects(fbScaleX, fbScaleY);
    }
}
