package imgui.extension.imnodes;

import imgui.ImVec2;
import imgui.binding.ImGuiStruct;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/extension/imnodes/ImNodesStyle.class */
public final class ImNodesStyle extends ImGuiStruct {
    private native float nGetGridSpacing();

    private native void nSetGridSpacing(float f);

    private native float nGetNodeCornerRounding();

    private native void nSetNodeCornerRounding(float f);

    private native void nGetNodePadding(ImVec2 imVec2);

    private native float nGetNodePaddingX();

    private native float nGetNodePaddingY();

    private native void nSetNodePadding(float f, float f2);

    private native float nGetNodeBorderThickness();

    private native void nSetNodeBorderThickness(float f);

    private native float nGetLinkThickness();

    private native void nSetLinkThickness(float f);

    private native float nGetLinkLineSegmentsPerLength();

    private native void nSetLinkLineSegmentsPerLength(float f);

    private native float nGetLinkHoverDistance();

    private native void nSetLinkHoverDistance(float f);

    private native float nGetPinCircleRadius();

    private native void nSetPinCircleRadius(float f);

    private native float nGetPinQuadSideLength();

    private native void nSetPinQuadSideLength(float f);

    private native float nGetPinTriangleSideLength();

    private native void nSetPinTriangleSideLength(float f);

    private native float nGetPinLineThickness();

    private native void nSetPinLineThickness(float f);

    private native float nGetPinHoverRadius();

    private native void nSetPinHoverRadius(float f);

    private native float nGetPinOffset();

    private native void nSetPinOffset(float f);

    private native void nGetMiniMapPadding(ImVec2 imVec2);

    private native float nGetMiniMapPaddingX();

    private native float nGetMiniMapPaddingY();

    private native void nSetMiniMapPadding(float f, float f2);

    private native void nGetMiniMapOffset(ImVec2 imVec2);

    private native float nGetMiniMapOffsetX();

    private native float nGetMiniMapOffsetY();

    private native void nSetMiniMapOffset(float f, float f2);

    private native int nGetFlags();

    private native void nSetFlags(int i);

    private native int[] nGetColors();

    private native int nGetColors(int i);

    private native void nSetColors(int[] iArr);

    private native void nSetColors(int i, int i2);

    public ImNodesStyle(long ptr) {
        super(ptr);
    }

    public float getGridSpacing() {
        return nGetGridSpacing();
    }

    public void setGridSpacing(float value) {
        nSetGridSpacing(value);
    }

    public float getNodeCornerRounding() {
        return nGetNodeCornerRounding();
    }

    public void setNodeCornerRounding(float value) {
        nSetNodeCornerRounding(value);
    }

    public ImVec2 getNodePadding() {
        ImVec2 dst = new ImVec2();
        nGetNodePadding(dst);
        return dst;
    }

    public float getNodePaddingX() {
        return nGetNodePaddingX();
    }

    public float getNodePaddingY() {
        return nGetNodePaddingY();
    }

    public void getNodePadding(ImVec2 dst) {
        nGetNodePadding(dst);
    }

    public void setNodePadding(ImVec2 value) {
        nSetNodePadding(value.x, value.y);
    }

    public void setNodePadding(float valueX, float valueY) {
        nSetNodePadding(valueX, valueY);
    }

    public float getNodeBorderThickness() {
        return nGetNodeBorderThickness();
    }

    public void setNodeBorderThickness(float value) {
        nSetNodeBorderThickness(value);
    }

    public float getLinkThickness() {
        return nGetLinkThickness();
    }

    public void setLinkThickness(float value) {
        nSetLinkThickness(value);
    }

    public float getLinkLineSegmentsPerLength() {
        return nGetLinkLineSegmentsPerLength();
    }

    public void setLinkLineSegmentsPerLength(float value) {
        nSetLinkLineSegmentsPerLength(value);
    }

    public float getLinkHoverDistance() {
        return nGetLinkHoverDistance();
    }

    public void setLinkHoverDistance(float value) {
        nSetLinkHoverDistance(value);
    }

    public float getPinCircleRadius() {
        return nGetPinCircleRadius();
    }

    public void setPinCircleRadius(float value) {
        nSetPinCircleRadius(value);
    }

    public float getPinQuadSideLength() {
        return nGetPinQuadSideLength();
    }

    public void setPinQuadSideLength(float value) {
        nSetPinQuadSideLength(value);
    }

    public float getPinTriangleSideLength() {
        return nGetPinTriangleSideLength();
    }

    public void setPinTriangleSideLength(float value) {
        nSetPinTriangleSideLength(value);
    }

    public float getPinLineThickness() {
        return nGetPinLineThickness();
    }

    public void setPinLineThickness(float value) {
        nSetPinLineThickness(value);
    }

    public float getPinHoverRadius() {
        return nGetPinHoverRadius();
    }

    public void setPinHoverRadius(float value) {
        nSetPinHoverRadius(value);
    }

    public float getPinOffset() {
        return nGetPinOffset();
    }

    public void setPinOffset(float value) {
        nSetPinOffset(value);
    }

    public ImVec2 getMiniMapPadding() {
        ImVec2 dst = new ImVec2();
        nGetMiniMapPadding(dst);
        return dst;
    }

    public float getMiniMapPaddingX() {
        return nGetMiniMapPaddingX();
    }

    public float getMiniMapPaddingY() {
        return nGetMiniMapPaddingY();
    }

    public void getMiniMapPadding(ImVec2 dst) {
        nGetMiniMapPadding(dst);
    }

    public void setMiniMapPadding(ImVec2 value) {
        nSetMiniMapPadding(value.x, value.y);
    }

    public void setMiniMapPadding(float valueX, float valueY) {
        nSetMiniMapPadding(valueX, valueY);
    }

    public ImVec2 getMiniMapOffset() {
        ImVec2 dst = new ImVec2();
        nGetMiniMapOffset(dst);
        return dst;
    }

    public float getMiniMapOffsetX() {
        return nGetMiniMapOffsetX();
    }

    public float getMiniMapOffsetY() {
        return nGetMiniMapOffsetY();
    }

    public void getMiniMapOffset(ImVec2 dst) {
        nGetMiniMapOffset(dst);
    }

    public void setMiniMapOffset(ImVec2 value) {
        nSetMiniMapOffset(value.x, value.y);
    }

    public void setMiniMapOffset(float valueX, float valueY) {
        nSetMiniMapOffset(valueX, valueY);
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

    public int[] getColors() {
        return nGetColors();
    }

    public int getColors(int idx) {
        return nGetColors(idx);
    }

    public void setColors(int[] value) {
        nSetColors(value);
    }

    public void setColors(int idx, int value) {
        nSetColors(idx, value);
    }
}
