package imgui;

import imgui.binding.ImGuiStruct;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/ImGuiViewport.class */
public final class ImGuiViewport extends ImGuiStruct {
    private native int nGetID();

    private native void nSetID(int i);

    private native int nGetFlags();

    private native void nSetFlags(int i);

    private native void nGetPos(ImVec2 imVec2);

    private native float nGetPosX();

    private native float nGetPosY();

    private native void nSetPos(float f, float f2);

    private native void nGetSize(ImVec2 imVec2);

    private native float nGetSizeX();

    private native float nGetSizeY();

    private native void nSetSize(float f, float f2);

    private native void nGetWorkPos(ImVec2 imVec2);

    private native float nGetWorkPosX();

    private native float nGetWorkPosY();

    private native void nSetWorkPos(float f, float f2);

    private native void nGetWorkSize(ImVec2 imVec2);

    private native float nGetWorkSizeX();

    private native float nGetWorkSizeY();

    private native void nSetWorkSize(float f, float f2);

    private native float nGetDpiScale();

    private native void nSetDpiScale(float f);

    private native int nGetParentViewportId();

    private native void nSetParentViewportId(int i);

    private native long nGetDrawData();

    private native void nSetDrawData(long j);

    public native void setRendererUserData(Object obj);

    public native Object getRendererUserData();

    public native void setPlatformUserData(Object obj);

    public native Object getPlatformUserData();

    public native void setPlatformHandle(long j);

    public native long getPlatformHandle();

    public native void setPlatformHandleRaw(long j);

    public native long getPlatformHandleRaw();

    private native boolean nGetPlatformWindowCreated();

    private native void nSetPlatformWindowCreated(boolean z);

    private native boolean nGetPlatformRequestMove();

    private native void nSetPlatformRequestMove(boolean z);

    private native boolean nGetPlatformRequestResize();

    private native void nSetPlatformRequestResize(boolean z);

    private native boolean nGetPlatformRequestClose();

    private native void nSetPlatformRequestClose(boolean z);

    private native void nGetCenter(ImVec2 imVec2);

    private native float nGetCenterX();

    private native float nGetCenterY();

    private native void nGetWorkCenter(ImVec2 imVec2);

    private native float nGetWorkCenterX();

    private native float nGetWorkCenterY();

    public ImGuiViewport(long ptr) {
        super(ptr);
    }

    public int getID() {
        return nGetID();
    }

    public void setID(int value) {
        nSetID(value);
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

    public ImVec2 getPos() {
        ImVec2 dst = new ImVec2();
        nGetPos(dst);
        return dst;
    }

    public float getPosX() {
        return nGetPosX();
    }

    public float getPosY() {
        return nGetPosY();
    }

    public void getPos(ImVec2 dst) {
        nGetPos(dst);
    }

    public void setPos(ImVec2 value) {
        nSetPos(value.x, value.y);
    }

    public void setPos(float valueX, float valueY) {
        nSetPos(valueX, valueY);
    }

    public ImVec2 getSize() {
        ImVec2 dst = new ImVec2();
        nGetSize(dst);
        return dst;
    }

    public float getSizeX() {
        return nGetSizeX();
    }

    public float getSizeY() {
        return nGetSizeY();
    }

    public void getSize(ImVec2 dst) {
        nGetSize(dst);
    }

    public void setSize(ImVec2 value) {
        nSetSize(value.x, value.y);
    }

    public void setSize(float valueX, float valueY) {
        nSetSize(valueX, valueY);
    }

    public ImVec2 getWorkPos() {
        ImVec2 dst = new ImVec2();
        nGetWorkPos(dst);
        return dst;
    }

    public float getWorkPosX() {
        return nGetWorkPosX();
    }

    public float getWorkPosY() {
        return nGetWorkPosY();
    }

    public void getWorkPos(ImVec2 dst) {
        nGetWorkPos(dst);
    }

    public void setWorkPos(ImVec2 value) {
        nSetWorkPos(value.x, value.y);
    }

    public void setWorkPos(float valueX, float valueY) {
        nSetWorkPos(valueX, valueY);
    }

    public ImVec2 getWorkSize() {
        ImVec2 dst = new ImVec2();
        nGetWorkSize(dst);
        return dst;
    }

    public float getWorkSizeX() {
        return nGetWorkSizeX();
    }

    public float getWorkSizeY() {
        return nGetWorkSizeY();
    }

    public void getWorkSize(ImVec2 dst) {
        nGetWorkSize(dst);
    }

    public void setWorkSize(ImVec2 value) {
        nSetWorkSize(value.x, value.y);
    }

    public void setWorkSize(float valueX, float valueY) {
        nSetWorkSize(valueX, valueY);
    }

    public float getDpiScale() {
        return nGetDpiScale();
    }

    public void setDpiScale(float value) {
        nSetDpiScale(value);
    }

    public int getParentViewportId() {
        return nGetParentViewportId();
    }

    public void setParentViewportId(int value) {
        nSetParentViewportId(value);
    }

    public ImDrawData getDrawData() {
        return new ImDrawData(nGetDrawData());
    }

    public void setDrawData(ImDrawData value) {
        nSetDrawData(value.ptr);
    }

    public boolean getPlatformWindowCreated() {
        return nGetPlatformWindowCreated();
    }

    public void setPlatformWindowCreated(boolean value) {
        nSetPlatformWindowCreated(value);
    }

    public boolean getPlatformRequestMove() {
        return nGetPlatformRequestMove();
    }

    public void setPlatformRequestMove(boolean value) {
        nSetPlatformRequestMove(value);
    }

    public boolean getPlatformRequestResize() {
        return nGetPlatformRequestResize();
    }

    public void setPlatformRequestResize(boolean value) {
        nSetPlatformRequestResize(value);
    }

    public boolean getPlatformRequestClose() {
        return nGetPlatformRequestClose();
    }

    public void setPlatformRequestClose(boolean value) {
        nSetPlatformRequestClose(value);
    }

    public ImVec2 getCenter() {
        ImVec2 dst = new ImVec2();
        nGetCenter(dst);
        return dst;
    }

    public float getCenterX() {
        return nGetCenterX();
    }

    public float getCenterY() {
        return nGetCenterY();
    }

    public void getCenter(ImVec2 dst) {
        nGetCenter(dst);
    }

    public ImVec2 getWorkCenter() {
        ImVec2 dst = new ImVec2();
        nGetWorkCenter(dst);
        return dst;
    }

    public float getWorkCenterX() {
        return nGetWorkCenterX();
    }

    public float getWorkCenterY() {
        return nGetWorkCenterY();
    }

    public void getWorkCenter(ImVec2 dst) {
        nGetWorkCenter(dst);
    }
}
