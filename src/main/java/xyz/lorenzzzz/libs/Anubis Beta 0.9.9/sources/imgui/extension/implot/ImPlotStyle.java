package imgui.extension.implot;

import imgui.ImVec2;
import imgui.ImVec4;
import imgui.binding.ImGuiStructDestroyable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/extension/implot/ImPlotStyle.class */
public final class ImPlotStyle extends ImGuiStructDestroyable {
    private native long nCreate();

    private native float nGetLineWeight();

    private native void nSetLineWeight(float f);

    private native int nGetMarker();

    private native void nSetMarker(int i);

    private native float nGetMarkerSize();

    private native void nSetMarkerSize(float f);

    private native float nGetMarkerWeight();

    private native void nSetMarkerWeight(float f);

    private native float nGetFillAlpha();

    private native void nSetFillAlpha(float f);

    private native float nGetErrorBarSize();

    private native void nSetErrorBarSize(float f);

    private native float nGetErrorBarWeight();

    private native void nSetErrorBarWeight(float f);

    private native float nGetDigitalBitHeight();

    private native void nSetDigitalBitHeight(float f);

    private native float nGetDigitalBitGap();

    private native void nSetDigitalBitGap(float f);

    private native float nGetPlotBorderSize();

    private native void nSetPlotBorderSize(float f);

    private native float nGetMinorAlpha();

    private native void nSetMinorAlpha(float f);

    private native void nGetMajorTickLen(ImVec2 imVec2);

    private native float nGetMajorTickLenX();

    private native float nGetMajorTickLenY();

    private native void nSetMajorTickLen(float f, float f2);

    private native void nGetMinorTickLen(ImVec2 imVec2);

    private native float nGetMinorTickLenX();

    private native float nGetMinorTickLenY();

    private native void nSetMinorTickLen(float f, float f2);

    private native void nGetMajorTickSize(ImVec2 imVec2);

    private native float nGetMajorTickSizeX();

    private native float nGetMajorTickSizeY();

    private native void nSetMajorTickSize(float f, float f2);

    private native void nGetMinorTickSize(ImVec2 imVec2);

    private native float nGetMinorTickSizeX();

    private native float nGetMinorTickSizeY();

    private native void nSetMinorTickSize(float f, float f2);

    private native void nGetMajorGridSize(ImVec2 imVec2);

    private native float nGetMajorGridSizeX();

    private native float nGetMajorGridSizeY();

    private native void nSetMajorGridSize(float f, float f2);

    private native void nGetMinorGridSize(ImVec2 imVec2);

    private native float nGetMinorGridSizeX();

    private native float nGetMinorGridSizeY();

    private native void nSetMinorGridSize(float f, float f2);

    private native void nGetPlotPadding(ImVec2 imVec2);

    private native float nGetPlotPaddingX();

    private native float nGetPlotPaddingY();

    private native void nSetPlotPadding(float f, float f2);

    private native void nGetLabelPadding(ImVec2 imVec2);

    private native float nGetLabelPaddingX();

    private native float nGetLabelPaddingY();

    private native void nSetLabelPadding(float f, float f2);

    private native void nGetLegendPadding(ImVec2 imVec2);

    private native float nGetLegendPaddingX();

    private native float nGetLegendPaddingY();

    private native void nSetLegendPadding(float f, float f2);

    private native void nGetLegendInnerPadding(ImVec2 imVec2);

    private native float nGetLegendInnerPaddingX();

    private native float nGetLegendInnerPaddingY();

    private native void nSetLegendInnerPadding(float f, float f2);

    private native void nGetLegendSpacing(ImVec2 imVec2);

    private native float nGetLegendSpacingX();

    private native float nGetLegendSpacingY();

    private native void nSetLegendSpacing(float f, float f2);

    private native void nGetMousePosPadding(ImVec2 imVec2);

    private native float nGetMousePosPaddingX();

    private native float nGetMousePosPaddingY();

    private native void nSetMousePosPadding(float f, float f2);

    private native void nGetAnnotationPadding(ImVec2 imVec2);

    private native float nGetAnnotationPaddingX();

    private native float nGetAnnotationPaddingY();

    private native void nSetAnnotationPadding(float f, float f2);

    private native void nGetFitPadding(ImVec2 imVec2);

    private native float nGetFitPaddingX();

    private native float nGetFitPaddingY();

    private native void nSetFitPadding(float f, float f2);

    private native void nGetPlotDefaultSize(ImVec2 imVec2);

    private native float nGetPlotDefaultSizeX();

    private native float nGetPlotDefaultSizeY();

    private native void nSetPlotDefaultSize(float f, float f2);

    private native void nGetPlotMinSize(ImVec2 imVec2);

    private native float nGetPlotMinSizeX();

    private native float nGetPlotMinSizeY();

    private native void nSetPlotMinSize(float f, float f2);

    private native ImVec4[] nGetColors();

    private native void nSetColors(ImVec4[] imVec4Arr);

    private native int nGetColormap();

    private native void nSetColormap(int i);

    private native boolean nGetUseLocalTime();

    private native void nSetUseLocalTime(boolean z);

    private native boolean nGetUseISO8601();

    private native void nSetUseISO8601(boolean z);

    private native boolean nGetUse24HourClock();

    private native void nSetUse24HourClock(boolean z);

    public ImPlotStyle() {
    }

    public ImPlotStyle(long ptr) {
        super(ptr);
    }

    @Override // imgui.binding.ImGuiStructDestroyable
    protected long create() {
        return nCreate();
    }

    public float getLineWeight() {
        return nGetLineWeight();
    }

    public void setLineWeight(float value) {
        nSetLineWeight(value);
    }

    public int getMarker() {
        return nGetMarker();
    }

    public void setMarker(int value) {
        nSetMarker(value);
    }

    public float getMarkerSize() {
        return nGetMarkerSize();
    }

    public void setMarkerSize(float value) {
        nSetMarkerSize(value);
    }

    public float getMarkerWeight() {
        return nGetMarkerWeight();
    }

    public void setMarkerWeight(float value) {
        nSetMarkerWeight(value);
    }

    public float getFillAlpha() {
        return nGetFillAlpha();
    }

    public void setFillAlpha(float value) {
        nSetFillAlpha(value);
    }

    public float getErrorBarSize() {
        return nGetErrorBarSize();
    }

    public void setErrorBarSize(float value) {
        nSetErrorBarSize(value);
    }

    public float getErrorBarWeight() {
        return nGetErrorBarWeight();
    }

    public void setErrorBarWeight(float value) {
        nSetErrorBarWeight(value);
    }

    public float getDigitalBitHeight() {
        return nGetDigitalBitHeight();
    }

    public void setDigitalBitHeight(float value) {
        nSetDigitalBitHeight(value);
    }

    public float getDigitalBitGap() {
        return nGetDigitalBitGap();
    }

    public void setDigitalBitGap(float value) {
        nSetDigitalBitGap(value);
    }

    public float getPlotBorderSize() {
        return nGetPlotBorderSize();
    }

    public void setPlotBorderSize(float value) {
        nSetPlotBorderSize(value);
    }

    public float getMinorAlpha() {
        return nGetMinorAlpha();
    }

    public void setMinorAlpha(float value) {
        nSetMinorAlpha(value);
    }

    public ImVec2 getMajorTickLen() {
        ImVec2 dst = new ImVec2();
        nGetMajorTickLen(dst);
        return dst;
    }

    public float getMajorTickLenX() {
        return nGetMajorTickLenX();
    }

    public float getMajorTickLenY() {
        return nGetMajorTickLenY();
    }

    public void getMajorTickLen(ImVec2 dst) {
        nGetMajorTickLen(dst);
    }

    public void setMajorTickLen(ImVec2 value) {
        nSetMajorTickLen(value.x, value.y);
    }

    public void setMajorTickLen(float valueX, float valueY) {
        nSetMajorTickLen(valueX, valueY);
    }

    public ImVec2 getMinorTickLen() {
        ImVec2 dst = new ImVec2();
        nGetMinorTickLen(dst);
        return dst;
    }

    public float getMinorTickLenX() {
        return nGetMinorTickLenX();
    }

    public float getMinorTickLenY() {
        return nGetMinorTickLenY();
    }

    public void getMinorTickLen(ImVec2 dst) {
        nGetMinorTickLen(dst);
    }

    public void setMinorTickLen(ImVec2 value) {
        nSetMinorTickLen(value.x, value.y);
    }

    public void setMinorTickLen(float valueX, float valueY) {
        nSetMinorTickLen(valueX, valueY);
    }

    public ImVec2 getMajorTickSize() {
        ImVec2 dst = new ImVec2();
        nGetMajorTickSize(dst);
        return dst;
    }

    public float getMajorTickSizeX() {
        return nGetMajorTickSizeX();
    }

    public float getMajorTickSizeY() {
        return nGetMajorTickSizeY();
    }

    public void getMajorTickSize(ImVec2 dst) {
        nGetMajorTickSize(dst);
    }

    public void setMajorTickSize(ImVec2 value) {
        nSetMajorTickSize(value.x, value.y);
    }

    public void setMajorTickSize(float valueX, float valueY) {
        nSetMajorTickSize(valueX, valueY);
    }

    public ImVec2 getMinorTickSize() {
        ImVec2 dst = new ImVec2();
        nGetMinorTickSize(dst);
        return dst;
    }

    public float getMinorTickSizeX() {
        return nGetMinorTickSizeX();
    }

    public float getMinorTickSizeY() {
        return nGetMinorTickSizeY();
    }

    public void getMinorTickSize(ImVec2 dst) {
        nGetMinorTickSize(dst);
    }

    public void setMinorTickSize(ImVec2 value) {
        nSetMinorTickSize(value.x, value.y);
    }

    public void setMinorTickSize(float valueX, float valueY) {
        nSetMinorTickSize(valueX, valueY);
    }

    public ImVec2 getMajorGridSize() {
        ImVec2 dst = new ImVec2();
        nGetMajorGridSize(dst);
        return dst;
    }

    public float getMajorGridSizeX() {
        return nGetMajorGridSizeX();
    }

    public float getMajorGridSizeY() {
        return nGetMajorGridSizeY();
    }

    public void getMajorGridSize(ImVec2 dst) {
        nGetMajorGridSize(dst);
    }

    public void setMajorGridSize(ImVec2 value) {
        nSetMajorGridSize(value.x, value.y);
    }

    public void setMajorGridSize(float valueX, float valueY) {
        nSetMajorGridSize(valueX, valueY);
    }

    public ImVec2 getMinorGridSize() {
        ImVec2 dst = new ImVec2();
        nGetMinorGridSize(dst);
        return dst;
    }

    public float getMinorGridSizeX() {
        return nGetMinorGridSizeX();
    }

    public float getMinorGridSizeY() {
        return nGetMinorGridSizeY();
    }

    public void getMinorGridSize(ImVec2 dst) {
        nGetMinorGridSize(dst);
    }

    public void setMinorGridSize(ImVec2 value) {
        nSetMinorGridSize(value.x, value.y);
    }

    public void setMinorGridSize(float valueX, float valueY) {
        nSetMinorGridSize(valueX, valueY);
    }

    public ImVec2 getPlotPadding() {
        ImVec2 dst = new ImVec2();
        nGetPlotPadding(dst);
        return dst;
    }

    public float getPlotPaddingX() {
        return nGetPlotPaddingX();
    }

    public float getPlotPaddingY() {
        return nGetPlotPaddingY();
    }

    public void getPlotPadding(ImVec2 dst) {
        nGetPlotPadding(dst);
    }

    public void setPlotPadding(ImVec2 value) {
        nSetPlotPadding(value.x, value.y);
    }

    public void setPlotPadding(float valueX, float valueY) {
        nSetPlotPadding(valueX, valueY);
    }

    public ImVec2 getLabelPadding() {
        ImVec2 dst = new ImVec2();
        nGetLabelPadding(dst);
        return dst;
    }

    public float getLabelPaddingX() {
        return nGetLabelPaddingX();
    }

    public float getLabelPaddingY() {
        return nGetLabelPaddingY();
    }

    public void getLabelPadding(ImVec2 dst) {
        nGetLabelPadding(dst);
    }

    public void setLabelPadding(ImVec2 value) {
        nSetLabelPadding(value.x, value.y);
    }

    public void setLabelPadding(float valueX, float valueY) {
        nSetLabelPadding(valueX, valueY);
    }

    public ImVec2 getLegendPadding() {
        ImVec2 dst = new ImVec2();
        nGetLegendPadding(dst);
        return dst;
    }

    public float getLegendPaddingX() {
        return nGetLegendPaddingX();
    }

    public float getLegendPaddingY() {
        return nGetLegendPaddingY();
    }

    public void getLegendPadding(ImVec2 dst) {
        nGetLegendPadding(dst);
    }

    public void setLegendPadding(ImVec2 value) {
        nSetLegendPadding(value.x, value.y);
    }

    public void setLegendPadding(float valueX, float valueY) {
        nSetLegendPadding(valueX, valueY);
    }

    public ImVec2 getLegendInnerPadding() {
        ImVec2 dst = new ImVec2();
        nGetLegendInnerPadding(dst);
        return dst;
    }

    public float getLegendInnerPaddingX() {
        return nGetLegendInnerPaddingX();
    }

    public float getLegendInnerPaddingY() {
        return nGetLegendInnerPaddingY();
    }

    public void getLegendInnerPadding(ImVec2 dst) {
        nGetLegendInnerPadding(dst);
    }

    public void setLegendInnerPadding(ImVec2 value) {
        nSetLegendInnerPadding(value.x, value.y);
    }

    public void setLegendInnerPadding(float valueX, float valueY) {
        nSetLegendInnerPadding(valueX, valueY);
    }

    public ImVec2 getLegendSpacing() {
        ImVec2 dst = new ImVec2();
        nGetLegendSpacing(dst);
        return dst;
    }

    public float getLegendSpacingX() {
        return nGetLegendSpacingX();
    }

    public float getLegendSpacingY() {
        return nGetLegendSpacingY();
    }

    public void getLegendSpacing(ImVec2 dst) {
        nGetLegendSpacing(dst);
    }

    public void setLegendSpacing(ImVec2 value) {
        nSetLegendSpacing(value.x, value.y);
    }

    public void setLegendSpacing(float valueX, float valueY) {
        nSetLegendSpacing(valueX, valueY);
    }

    public ImVec2 getMousePosPadding() {
        ImVec2 dst = new ImVec2();
        nGetMousePosPadding(dst);
        return dst;
    }

    public float getMousePosPaddingX() {
        return nGetMousePosPaddingX();
    }

    public float getMousePosPaddingY() {
        return nGetMousePosPaddingY();
    }

    public void getMousePosPadding(ImVec2 dst) {
        nGetMousePosPadding(dst);
    }

    public void setMousePosPadding(ImVec2 value) {
        nSetMousePosPadding(value.x, value.y);
    }

    public void setMousePosPadding(float valueX, float valueY) {
        nSetMousePosPadding(valueX, valueY);
    }

    public ImVec2 getAnnotationPadding() {
        ImVec2 dst = new ImVec2();
        nGetAnnotationPadding(dst);
        return dst;
    }

    public float getAnnotationPaddingX() {
        return nGetAnnotationPaddingX();
    }

    public float getAnnotationPaddingY() {
        return nGetAnnotationPaddingY();
    }

    public void getAnnotationPadding(ImVec2 dst) {
        nGetAnnotationPadding(dst);
    }

    public void setAnnotationPadding(ImVec2 value) {
        nSetAnnotationPadding(value.x, value.y);
    }

    public void setAnnotationPadding(float valueX, float valueY) {
        nSetAnnotationPadding(valueX, valueY);
    }

    public ImVec2 getFitPadding() {
        ImVec2 dst = new ImVec2();
        nGetFitPadding(dst);
        return dst;
    }

    public float getFitPaddingX() {
        return nGetFitPaddingX();
    }

    public float getFitPaddingY() {
        return nGetFitPaddingY();
    }

    public void getFitPadding(ImVec2 dst) {
        nGetFitPadding(dst);
    }

    public void setFitPadding(ImVec2 value) {
        nSetFitPadding(value.x, value.y);
    }

    public void setFitPadding(float valueX, float valueY) {
        nSetFitPadding(valueX, valueY);
    }

    public ImVec2 getPlotDefaultSize() {
        ImVec2 dst = new ImVec2();
        nGetPlotDefaultSize(dst);
        return dst;
    }

    public float getPlotDefaultSizeX() {
        return nGetPlotDefaultSizeX();
    }

    public float getPlotDefaultSizeY() {
        return nGetPlotDefaultSizeY();
    }

    public void getPlotDefaultSize(ImVec2 dst) {
        nGetPlotDefaultSize(dst);
    }

    public void setPlotDefaultSize(ImVec2 value) {
        nSetPlotDefaultSize(value.x, value.y);
    }

    public void setPlotDefaultSize(float valueX, float valueY) {
        nSetPlotDefaultSize(valueX, valueY);
    }

    public ImVec2 getPlotMinSize() {
        ImVec2 dst = new ImVec2();
        nGetPlotMinSize(dst);
        return dst;
    }

    public float getPlotMinSizeX() {
        return nGetPlotMinSizeX();
    }

    public float getPlotMinSizeY() {
        return nGetPlotMinSizeY();
    }

    public void getPlotMinSize(ImVec2 dst) {
        nGetPlotMinSize(dst);
    }

    public void setPlotMinSize(ImVec2 value) {
        nSetPlotMinSize(value.x, value.y);
    }

    public void setPlotMinSize(float valueX, float valueY) {
        nSetPlotMinSize(valueX, valueY);
    }

    public ImVec4[] getColors() {
        return nGetColors();
    }

    public void setColors(ImVec4[] value) {
        nSetColors(value);
    }

    public int getColormap() {
        return nGetColormap();
    }

    public void setColormap(int value) {
        nSetColormap(value);
    }

    public boolean getUseLocalTime() {
        return nGetUseLocalTime();
    }

    public void setUseLocalTime(boolean value) {
        nSetUseLocalTime(value);
    }

    public boolean getUseISO8601() {
        return nGetUseISO8601();
    }

    public void setUseISO8601(boolean value) {
        nSetUseISO8601(value);
    }

    public boolean getUse24HourClock() {
        return nGetUse24HourClock();
    }

    public void setUse24HourClock(boolean value) {
        nSetUse24HourClock(value);
    }
}
