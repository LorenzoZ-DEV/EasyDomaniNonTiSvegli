package imgui.extension.nodeditor;

import imgui.ImVec2;
import imgui.ImVec4;
import imgui.binding.ImGuiStruct;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/extension/nodeditor/NodeEditorStyle.class */
public final class NodeEditorStyle extends ImGuiStruct {
    private native void nGetNodePadding(ImVec4 imVec4);

    private native float nGetNodePaddingX();

    private native float nGetNodePaddingY();

    private native float nGetNodePaddingZ();

    private native float nGetNodePaddingW();

    private native void nSetNodePadding(float f, float f2, float f3, float f4);

    private native float nGetNodeRounding();

    private native void nSetNodeRounding(float f);

    private native float nGetNodeBorderWidth();

    private native void nSetNodeBorderWidth(float f);

    private native float nGetHoveredNodeBorderWidth();

    private native void nSetHoveredNodeBorderWidth(float f);

    private native float nGetHoverNodeBorderOffset();

    private native void nSetHoverNodeBorderOffset(float f);

    private native float nGetSelectedNodeBorderWidth();

    private native void nSetSelectedNodeBorderWidth(float f);

    private native float nGetSelectedNodeBorderOffset();

    private native void nSetSelectedNodeBorderOffset(float f);

    private native float nGetPinRounding();

    private native void nSetPinRounding(float f);

    private native float nGetPinBorderWidth();

    private native void nSetPinBorderWidth(float f);

    private native float nGetLinkStrength();

    private native void nSetLinkStrength(float f);

    private native void nGetSourceDirection(ImVec2 imVec2);

    private native float nGetSourceDirectionX();

    private native float nGetSourceDirectionY();

    private native void nSetSourceDirection(float f, float f2);

    private native void nGetTargetDirection(ImVec2 imVec2);

    private native float nGetTargetDirectionX();

    private native float nGetTargetDirectionY();

    private native void nSetTargetDirection(float f, float f2);

    private native float nGetScrollDuration();

    private native void nSetScrollDuration(float f);

    private native float nGetFlowMarkerDistance();

    private native void nSetFlowMarkerDistance(float f);

    private native float nGetFlowSpeed();

    private native void nSetFlowSpeed(float f);

    private native float nGetFlowDuration();

    private native void nSetFlowDuration(float f);

    private native void nGetPivotAlignment(ImVec2 imVec2);

    private native float nGetPivotAlignmentX();

    private native float nGetPivotAlignmentY();

    private native void nSetPivotAlignment(float f, float f2);

    private native void nGetPivotSize(ImVec2 imVec2);

    private native float nGetPivotSizeX();

    private native float nGetPivotSizeY();

    private native void nSetPivotSize(float f, float f2);

    private native void nGetPivotScale(ImVec2 imVec2);

    private native float nGetPivotScaleX();

    private native float nGetPivotScaleY();

    private native void nSetPivotScale(float f, float f2);

    private native float nGetPinCorners();

    private native void nSetPinCorners(float f);

    private native float nGetPinRadius();

    private native void nSetPinRadius(float f);

    private native float nGetPinArrowSize();

    private native void nSetPinArrowSize(float f);

    private native float nGetPinArrowWidth();

    private native void nSetPinArrowWidth(float f);

    private native float nGetGroupRounding();

    private native void nSetGroupRounding(float f);

    private native float nGetGroupBorderWidth();

    private native void nSetGroupBorderWidth(float f);

    private native float nGetHighlightConnectedLinks();

    private native void nSetHighlightConnectedLinks(float f);

    private native float nGetSnapLinkToPinDir();

    private native void nSetSnapLinkToPinDir(float f);

    private native ImVec4[] nGetColors();

    private native void nSetColors(ImVec4[] imVec4Arr);

    public NodeEditorStyle(long ptr) {
        super(ptr);
    }

    public ImVec4 getNodePadding() {
        ImVec4 dst = new ImVec4();
        nGetNodePadding(dst);
        return dst;
    }

    public float getNodePaddingX() {
        return nGetNodePaddingX();
    }

    public float getNodePaddingY() {
        return nGetNodePaddingY();
    }

    public float getNodePaddingZ() {
        return nGetNodePaddingZ();
    }

    public float getNodePaddingW() {
        return nGetNodePaddingW();
    }

    public void getNodePadding(ImVec4 dst) {
        nGetNodePadding(dst);
    }

    public void setNodePadding(ImVec4 value) {
        nSetNodePadding(value.x, value.y, value.z, value.w);
    }

    public void setNodePadding(float valueX, float valueY, float valueZ, float valueW) {
        nSetNodePadding(valueX, valueY, valueZ, valueW);
    }

    public float getNodeRounding() {
        return nGetNodeRounding();
    }

    public void setNodeRounding(float value) {
        nSetNodeRounding(value);
    }

    public float getNodeBorderWidth() {
        return nGetNodeBorderWidth();
    }

    public void setNodeBorderWidth(float value) {
        nSetNodeBorderWidth(value);
    }

    public float getHoveredNodeBorderWidth() {
        return nGetHoveredNodeBorderWidth();
    }

    public void setHoveredNodeBorderWidth(float value) {
        nSetHoveredNodeBorderWidth(value);
    }

    public float getHoverNodeBorderOffset() {
        return nGetHoverNodeBorderOffset();
    }

    public void setHoverNodeBorderOffset(float value) {
        nSetHoverNodeBorderOffset(value);
    }

    public float getSelectedNodeBorderWidth() {
        return nGetSelectedNodeBorderWidth();
    }

    public void setSelectedNodeBorderWidth(float value) {
        nSetSelectedNodeBorderWidth(value);
    }

    public float getSelectedNodeBorderOffset() {
        return nGetSelectedNodeBorderOffset();
    }

    public void setSelectedNodeBorderOffset(float value) {
        nSetSelectedNodeBorderOffset(value);
    }

    public float getPinRounding() {
        return nGetPinRounding();
    }

    public void setPinRounding(float value) {
        nSetPinRounding(value);
    }

    public float getPinBorderWidth() {
        return nGetPinBorderWidth();
    }

    public void setPinBorderWidth(float value) {
        nSetPinBorderWidth(value);
    }

    public float getLinkStrength() {
        return nGetLinkStrength();
    }

    public void setLinkStrength(float value) {
        nSetLinkStrength(value);
    }

    public ImVec2 getSourceDirection() {
        ImVec2 dst = new ImVec2();
        nGetSourceDirection(dst);
        return dst;
    }

    public float getSourceDirectionX() {
        return nGetSourceDirectionX();
    }

    public float getSourceDirectionY() {
        return nGetSourceDirectionY();
    }

    public void getSourceDirection(ImVec2 dst) {
        nGetSourceDirection(dst);
    }

    public void setSourceDirection(ImVec2 value) {
        nSetSourceDirection(value.x, value.y);
    }

    public void setSourceDirection(float valueX, float valueY) {
        nSetSourceDirection(valueX, valueY);
    }

    public ImVec2 getTargetDirection() {
        ImVec2 dst = new ImVec2();
        nGetTargetDirection(dst);
        return dst;
    }

    public float getTargetDirectionX() {
        return nGetTargetDirectionX();
    }

    public float getTargetDirectionY() {
        return nGetTargetDirectionY();
    }

    public void getTargetDirection(ImVec2 dst) {
        nGetTargetDirection(dst);
    }

    public void setTargetDirection(ImVec2 value) {
        nSetTargetDirection(value.x, value.y);
    }

    public void setTargetDirection(float valueX, float valueY) {
        nSetTargetDirection(valueX, valueY);
    }

    public float getScrollDuration() {
        return nGetScrollDuration();
    }

    public void setScrollDuration(float value) {
        nSetScrollDuration(value);
    }

    public float getFlowMarkerDistance() {
        return nGetFlowMarkerDistance();
    }

    public void setFlowMarkerDistance(float value) {
        nSetFlowMarkerDistance(value);
    }

    public float getFlowSpeed() {
        return nGetFlowSpeed();
    }

    public void setFlowSpeed(float value) {
        nSetFlowSpeed(value);
    }

    public float getFlowDuration() {
        return nGetFlowDuration();
    }

    public void setFlowDuration(float value) {
        nSetFlowDuration(value);
    }

    public ImVec2 getPivotAlignment() {
        ImVec2 dst = new ImVec2();
        nGetPivotAlignment(dst);
        return dst;
    }

    public float getPivotAlignmentX() {
        return nGetPivotAlignmentX();
    }

    public float getPivotAlignmentY() {
        return nGetPivotAlignmentY();
    }

    public void getPivotAlignment(ImVec2 dst) {
        nGetPivotAlignment(dst);
    }

    public void setPivotAlignment(ImVec2 value) {
        nSetPivotAlignment(value.x, value.y);
    }

    public void setPivotAlignment(float valueX, float valueY) {
        nSetPivotAlignment(valueX, valueY);
    }

    public ImVec2 getPivotSize() {
        ImVec2 dst = new ImVec2();
        nGetPivotSize(dst);
        return dst;
    }

    public float getPivotSizeX() {
        return nGetPivotSizeX();
    }

    public float getPivotSizeY() {
        return nGetPivotSizeY();
    }

    public void getPivotSize(ImVec2 dst) {
        nGetPivotSize(dst);
    }

    public void setPivotSize(ImVec2 value) {
        nSetPivotSize(value.x, value.y);
    }

    public void setPivotSize(float valueX, float valueY) {
        nSetPivotSize(valueX, valueY);
    }

    public ImVec2 getPivotScale() {
        ImVec2 dst = new ImVec2();
        nGetPivotScale(dst);
        return dst;
    }

    public float getPivotScaleX() {
        return nGetPivotScaleX();
    }

    public float getPivotScaleY() {
        return nGetPivotScaleY();
    }

    public void getPivotScale(ImVec2 dst) {
        nGetPivotScale(dst);
    }

    public void setPivotScale(ImVec2 value) {
        nSetPivotScale(value.x, value.y);
    }

    public void setPivotScale(float valueX, float valueY) {
        nSetPivotScale(valueX, valueY);
    }

    public float getPinCorners() {
        return nGetPinCorners();
    }

    public void setPinCorners(float value) {
        nSetPinCorners(value);
    }

    public float getPinRadius() {
        return nGetPinRadius();
    }

    public void setPinRadius(float value) {
        nSetPinRadius(value);
    }

    public float getPinArrowSize() {
        return nGetPinArrowSize();
    }

    public void setPinArrowSize(float value) {
        nSetPinArrowSize(value);
    }

    public float getPinArrowWidth() {
        return nGetPinArrowWidth();
    }

    public void setPinArrowWidth(float value) {
        nSetPinArrowWidth(value);
    }

    public float getGroupRounding() {
        return nGetGroupRounding();
    }

    public void setGroupRounding(float value) {
        nSetGroupRounding(value);
    }

    public float getGroupBorderWidth() {
        return nGetGroupBorderWidth();
    }

    public void setGroupBorderWidth(float value) {
        nSetGroupBorderWidth(value);
    }

    public float getHighlightConnectedLinks() {
        return nGetHighlightConnectedLinks();
    }

    public void setHighlightConnectedLinks(float value) {
        nSetHighlightConnectedLinks(value);
    }

    public float getSnapLinkToPinDir() {
        return nGetSnapLinkToPinDir();
    }

    public void setSnapLinkToPinDir(float value) {
        nSetSnapLinkToPinDir(value);
    }

    public ImVec4[] getColors() {
        return nGetColors();
    }

    public void setColors(ImVec4[] value) {
        nSetColors(value);
    }
}
