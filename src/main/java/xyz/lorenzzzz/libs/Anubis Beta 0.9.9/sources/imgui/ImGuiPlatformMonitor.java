package imgui;

import imgui.binding.ImGuiStruct;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/ImGuiPlatformMonitor.class */
public final class ImGuiPlatformMonitor extends ImGuiStruct {
    private native void nGetMainPos(ImVec2 imVec2);

    private native float nGetMainPosX();

    private native float nGetMainPosY();

    private native void nSetMainPos(float f, float f2);

    private native void nGetMainSize(ImVec2 imVec2);

    private native float nGetMainSizeX();

    private native float nGetMainSizeY();

    private native void nSetMainSize(float f, float f2);

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

    public native void setPlatformHandle(long j);

    public native long getPlatformHandle();

    public ImGuiPlatformMonitor(long ptr) {
        super(ptr);
    }

    public ImVec2 getMainPos() {
        ImVec2 dst = new ImVec2();
        nGetMainPos(dst);
        return dst;
    }

    public float getMainPosX() {
        return nGetMainPosX();
    }

    public float getMainPosY() {
        return nGetMainPosY();
    }

    public void getMainPos(ImVec2 dst) {
        nGetMainPos(dst);
    }

    public void setMainPos(ImVec2 value) {
        nSetMainPos(value.x, value.y);
    }

    public void setMainPos(float valueX, float valueY) {
        nSetMainPos(valueX, valueY);
    }

    public ImVec2 getMainSize() {
        ImVec2 dst = new ImVec2();
        nGetMainSize(dst);
        return dst;
    }

    public float getMainSizeX() {
        return nGetMainSizeX();
    }

    public float getMainSizeY() {
        return nGetMainSizeY();
    }

    public void getMainSize(ImVec2 dst) {
        nGetMainSize(dst);
    }

    public void setMainSize(ImVec2 value) {
        nSetMainSize(value.x, value.y);
    }

    public void setMainSize(float valueX, float valueY) {
        nSetMainSize(valueX, valueY);
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
}
