package imgui.extension.implot;

import imgui.binding.ImGuiStructDestroyable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/extension/implot/ImPlotInputMap.class */
public final class ImPlotInputMap extends ImGuiStructDestroyable {
    private native long nCreate();

    private native int nGetPan();

    private native void nSetPan(int i);

    private native int nGetPanMod();

    private native void nSetPanMod(int i);

    private native int nGetFit();

    private native void nSetFit(int i);

    private native int nGetSelect();

    private native void nSetSelect(int i);

    private native int nGetSelectCancel();

    private native void nSetSelectCancel(int i);

    private native int nGetSelectMod();

    private native void nSetSelectMod(int i);

    private native int nGetSelectHorzMod();

    private native void nSetSelectHorzMod(int i);

    private native int nGetSelectVertMod();

    private native void nSetSelectVertMod(int i);

    private native int nGetMenu();

    private native void nSetMenu(int i);

    private native int nGetOverrideMod();

    private native void nSetOverrideMod(int i);

    private native int nGetZoomMod();

    private native void nSetZoomMod(int i);

    private native float nGetZoomRate();

    private native void nSetZoomRate(float f);

    public ImPlotInputMap() {
    }

    public ImPlotInputMap(long ptr) {
        super(ptr);
    }

    @Override // imgui.binding.ImGuiStructDestroyable
    protected long create() {
        return nCreate();
    }

    public int getPan() {
        return nGetPan();
    }

    public void setPan(int value) {
        nSetPan(value);
    }

    public int getPanMod() {
        return nGetPanMod();
    }

    public void setPanMod(int value) {
        nSetPanMod(value);
    }

    public int getFit() {
        return nGetFit();
    }

    public void setFit(int value) {
        nSetFit(value);
    }

    public int getSelect() {
        return nGetSelect();
    }

    public void setSelect(int value) {
        nSetSelect(value);
    }

    public int getSelectCancel() {
        return nGetSelectCancel();
    }

    public void setSelectCancel(int value) {
        nSetSelectCancel(value);
    }

    public int getSelectMod() {
        return nGetSelectMod();
    }

    public void setSelectMod(int value) {
        nSetSelectMod(value);
    }

    public int getSelectHorzMod() {
        return nGetSelectHorzMod();
    }

    public void setSelectHorzMod(int value) {
        nSetSelectHorzMod(value);
    }

    public int getSelectVertMod() {
        return nGetSelectVertMod();
    }

    public void setSelectVertMod(int value) {
        nSetSelectVertMod(value);
    }

    public int getMenu() {
        return nGetMenu();
    }

    public void setMenu(int value) {
        nSetMenu(value);
    }

    public int getOverrideMod() {
        return nGetOverrideMod();
    }

    public void setOverrideMod(int value) {
        nSetOverrideMod(value);
    }

    public int getZoomMod() {
        return nGetZoomMod();
    }

    public void setZoomMod(int value) {
        nSetZoomMod(value);
    }

    public float getZoomRate() {
        return nGetZoomRate();
    }

    public void setZoomRate(float value) {
        nSetZoomRate(value);
    }
}
