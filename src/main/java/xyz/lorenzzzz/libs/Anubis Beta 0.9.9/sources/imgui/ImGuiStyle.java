package imgui;

import imgui.binding.ImGuiStructDestroyable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/ImGuiStyle.class */
public final class ImGuiStyle extends ImGuiStructDestroyable {
    private native long nCreate();

    private native float nGetAlpha();

    private native void nSetAlpha(float f);

    private native float nGetDisabledAlpha();

    private native void nSetDisabledAlpha(float f);

    private native void nGetWindowPadding(ImVec2 imVec2);

    private native float nGetWindowPaddingX();

    private native float nGetWindowPaddingY();

    private native void nSetWindowPadding(float f, float f2);

    private native float nGetWindowRounding();

    private native void nSetWindowRounding(float f);

    private native float nGetWindowBorderSize();

    private native void nSetWindowBorderSize(float f);

    private native void nGetWindowMinSize(ImVec2 imVec2);

    private native float nGetWindowMinSizeX();

    private native float nGetWindowMinSizeY();

    private native void nSetWindowMinSize(float f, float f2);

    private native void nGetWindowTitleAlign(ImVec2 imVec2);

    private native float nGetWindowTitleAlignX();

    private native float nGetWindowTitleAlignY();

    private native void nSetWindowTitleAlign(float f, float f2);

    private native int nGetWindowMenuButtonPosition();

    private native void nSetWindowMenuButtonPosition(int i);

    private native float nGetChildRounding();

    private native void nSetChildRounding(float f);

    private native float nGetChildBorderSize();

    private native void nSetChildBorderSize(float f);

    private native float nGetPopupRounding();

    private native void nSetPopupRounding(float f);

    private native float nGetPopupBorderSize();

    private native void nSetPopupBorderSize(float f);

    private native void nGetFramePadding(ImVec2 imVec2);

    private native float nGetFramePaddingX();

    private native float nGetFramePaddingY();

    private native void nSetFramePadding(float f, float f2);

    private native float nGetFrameRounding();

    private native void nSetFrameRounding(float f);

    private native float nGetFrameBorderSize();

    private native void nSetFrameBorderSize(float f);

    private native void nGetItemSpacing(ImVec2 imVec2);

    private native float nGetItemSpacingX();

    private native float nGetItemSpacingY();

    private native void nSetItemSpacing(float f, float f2);

    private native void nGetItemInnerSpacing(ImVec2 imVec2);

    private native float nGetItemInnerSpacingX();

    private native float nGetItemInnerSpacingY();

    private native void nSetItemInnerSpacing(float f, float f2);

    private native void nGetCellPadding(ImVec2 imVec2);

    private native float nGetCellPaddingX();

    private native float nGetCellPaddingY();

    private native void nSetCellPadding(float f, float f2);

    private native void nGetTouchExtraPadding(ImVec2 imVec2);

    private native float nGetTouchExtraPaddingX();

    private native float nGetTouchExtraPaddingY();

    private native void nSetTouchExtraPadding(float f, float f2);

    private native float nGetIndentSpacing();

    private native void nSetIndentSpacing(float f);

    private native float nGetColumnsMinSpacing();

    private native void nSetColumnsMinSpacing(float f);

    private native float nGetScrollbarSize();

    private native void nSetScrollbarSize(float f);

    private native float nGetScrollbarRounding();

    private native void nSetScrollbarRounding(float f);

    private native float nGetGrabMinSize();

    private native void nSetGrabMinSize(float f);

    private native float nGetGrabRounding();

    private native void nSetGrabRounding(float f);

    private native float nGetLogSliderDeadzone();

    private native void nSetLogSliderDeadzone(float f);

    private native float nGetTabRounding();

    private native void nSetTabRounding(float f);

    private native float nGetTabBorderSize();

    private native void nSetTabBorderSize(float f);

    private native float nGetTabMinWidthForCloseButton();

    private native void nSetTabMinWidthForCloseButton(float f);

    private native int nGetColorButtonPosition();

    private native void nSetColorButtonPosition(int i);

    private native void nGetButtonTextAlign(ImVec2 imVec2);

    private native float nGetButtonTextAlignX();

    private native float nGetButtonTextAlignY();

    private native void nSetButtonTextAlign(float f, float f2);

    private native void nGetSelectableTextAlign(ImVec2 imVec2);

    private native float nGetSelectableTextAlignX();

    private native float nGetSelectableTextAlignY();

    private native void nSetSelectableTextAlign(float f, float f2);

    private native float nGetSeparatorTextBorderSize();

    private native void nSetSeparatorTextBorderSize(float f);

    private native void nGetSeparatorTextAlign(ImVec2 imVec2);

    private native float nGetSeparatorTextAlignX();

    private native float nGetSeparatorTextAlignY();

    private native void nSetSeparatorTextAlign(float f, float f2);

    private native void nGetSeparatorTextPadding(ImVec2 imVec2);

    private native float nGetSeparatorTextPaddingX();

    private native float nGetSeparatorTextPaddingY();

    private native void nSetSeparatorTextPadding(float f, float f2);

    private native void nGetDisplayWindowPadding(ImVec2 imVec2);

    private native float nGetDisplayWindowPaddingX();

    private native float nGetDisplayWindowPaddingY();

    private native void nSetDisplayWindowPadding(float f, float f2);

    private native void nGetDisplaySafeAreaPadding(ImVec2 imVec2);

    private native float nGetDisplaySafeAreaPaddingX();

    private native float nGetDisplaySafeAreaPaddingY();

    private native void nSetDisplaySafeAreaPadding(float f, float f2);

    private native float nGetDockingSeparatorSize();

    private native void nSetDockingSeparatorSize(float f);

    private native float nGetMouseCursorScale();

    private native void nSetMouseCursorScale(float f);

    private native boolean nGetAntiAliasedLines();

    private native void nSetAntiAliasedLines(boolean z);

    private native boolean nGetAntiAliasedLinesUseTex();

    private native void nSetAntiAliasedLinesUseTex(boolean z);

    private native boolean nGetAntiAliasedFill();

    private native void nSetAntiAliasedFill(boolean z);

    private native float nGetCurveTessellationTol();

    private native void nSetCurveTessellationTol(float f);

    private native float nGetCircleTessellationMaxError();

    private native void nSetCircleTessellationMaxError(float f);

    private native ImVec4[] nGetColors();

    private native void nSetColors(ImVec4[] imVec4Arr);

    public native void getColor(int i, ImVec4 imVec4);

    public native void setColor(int i, float f, float f2, float f3, float f4);

    public native void setColor(int i, int i2, int i3, int i4, int i5);

    public native void setColor(int i, int i2);

    private native float nGetHoverStationaryDelay();

    private native void nSetHoverStationaryDelay(float f);

    private native float nGetHoverDelayShort();

    private native void nSetHoverDelayShort(float f);

    private native float nGetHoverDelayNormal();

    private native void nSetHoverDelayNormal(float f);

    private native int nGetHoverFlagsForTooltipMouse();

    private native void nSetHoverFlagsForTooltipMouse(int i);

    private native int nGetHoverFlagsForTooltipNav();

    private native void nSetHoverFlagsForTooltipNav(int i);

    private native void nScaleAllSizes(float f);

    public ImGuiStyle() {
    }

    public ImGuiStyle(long ptr) {
        super(ptr);
    }

    @Override // imgui.binding.ImGuiStructDestroyable
    protected long create() {
        return nCreate();
    }

    public float getAlpha() {
        return nGetAlpha();
    }

    public void setAlpha(float value) {
        nSetAlpha(value);
    }

    public float getDisabledAlpha() {
        return nGetDisabledAlpha();
    }

    public void setDisabledAlpha(float value) {
        nSetDisabledAlpha(value);
    }

    public ImVec2 getWindowPadding() {
        ImVec2 dst = new ImVec2();
        nGetWindowPadding(dst);
        return dst;
    }

    public float getWindowPaddingX() {
        return nGetWindowPaddingX();
    }

    public float getWindowPaddingY() {
        return nGetWindowPaddingY();
    }

    public void getWindowPadding(ImVec2 dst) {
        nGetWindowPadding(dst);
    }

    public void setWindowPadding(ImVec2 value) {
        nSetWindowPadding(value.x, value.y);
    }

    public void setWindowPadding(float valueX, float valueY) {
        nSetWindowPadding(valueX, valueY);
    }

    public float getWindowRounding() {
        return nGetWindowRounding();
    }

    public void setWindowRounding(float value) {
        nSetWindowRounding(value);
    }

    public float getWindowBorderSize() {
        return nGetWindowBorderSize();
    }

    public void setWindowBorderSize(float value) {
        nSetWindowBorderSize(value);
    }

    public ImVec2 getWindowMinSize() {
        ImVec2 dst = new ImVec2();
        nGetWindowMinSize(dst);
        return dst;
    }

    public float getWindowMinSizeX() {
        return nGetWindowMinSizeX();
    }

    public float getWindowMinSizeY() {
        return nGetWindowMinSizeY();
    }

    public void getWindowMinSize(ImVec2 dst) {
        nGetWindowMinSize(dst);
    }

    public void setWindowMinSize(ImVec2 value) {
        nSetWindowMinSize(value.x, value.y);
    }

    public void setWindowMinSize(float valueX, float valueY) {
        nSetWindowMinSize(valueX, valueY);
    }

    public ImVec2 getWindowTitleAlign() {
        ImVec2 dst = new ImVec2();
        nGetWindowTitleAlign(dst);
        return dst;
    }

    public float getWindowTitleAlignX() {
        return nGetWindowTitleAlignX();
    }

    public float getWindowTitleAlignY() {
        return nGetWindowTitleAlignY();
    }

    public void getWindowTitleAlign(ImVec2 dst) {
        nGetWindowTitleAlign(dst);
    }

    public void setWindowTitleAlign(ImVec2 value) {
        nSetWindowTitleAlign(value.x, value.y);
    }

    public void setWindowTitleAlign(float valueX, float valueY) {
        nSetWindowTitleAlign(valueX, valueY);
    }

    public int getWindowMenuButtonPosition() {
        return nGetWindowMenuButtonPosition();
    }

    public void setWindowMenuButtonPosition(int value) {
        nSetWindowMenuButtonPosition(value);
    }

    public float getChildRounding() {
        return nGetChildRounding();
    }

    public void setChildRounding(float value) {
        nSetChildRounding(value);
    }

    public float getChildBorderSize() {
        return nGetChildBorderSize();
    }

    public void setChildBorderSize(float value) {
        nSetChildBorderSize(value);
    }

    public float getPopupRounding() {
        return nGetPopupRounding();
    }

    public void setPopupRounding(float value) {
        nSetPopupRounding(value);
    }

    public float getPopupBorderSize() {
        return nGetPopupBorderSize();
    }

    public void setPopupBorderSize(float value) {
        nSetPopupBorderSize(value);
    }

    public ImVec2 getFramePadding() {
        ImVec2 dst = new ImVec2();
        nGetFramePadding(dst);
        return dst;
    }

    public float getFramePaddingX() {
        return nGetFramePaddingX();
    }

    public float getFramePaddingY() {
        return nGetFramePaddingY();
    }

    public void getFramePadding(ImVec2 dst) {
        nGetFramePadding(dst);
    }

    public void setFramePadding(ImVec2 value) {
        nSetFramePadding(value.x, value.y);
    }

    public void setFramePadding(float valueX, float valueY) {
        nSetFramePadding(valueX, valueY);
    }

    public float getFrameRounding() {
        return nGetFrameRounding();
    }

    public void setFrameRounding(float value) {
        nSetFrameRounding(value);
    }

    public float getFrameBorderSize() {
        return nGetFrameBorderSize();
    }

    public void setFrameBorderSize(float value) {
        nSetFrameBorderSize(value);
    }

    public ImVec2 getItemSpacing() {
        ImVec2 dst = new ImVec2();
        nGetItemSpacing(dst);
        return dst;
    }

    public float getItemSpacingX() {
        return nGetItemSpacingX();
    }

    public float getItemSpacingY() {
        return nGetItemSpacingY();
    }

    public void getItemSpacing(ImVec2 dst) {
        nGetItemSpacing(dst);
    }

    public void setItemSpacing(ImVec2 value) {
        nSetItemSpacing(value.x, value.y);
    }

    public void setItemSpacing(float valueX, float valueY) {
        nSetItemSpacing(valueX, valueY);
    }

    public ImVec2 getItemInnerSpacing() {
        ImVec2 dst = new ImVec2();
        nGetItemInnerSpacing(dst);
        return dst;
    }

    public float getItemInnerSpacingX() {
        return nGetItemInnerSpacingX();
    }

    public float getItemInnerSpacingY() {
        return nGetItemInnerSpacingY();
    }

    public void getItemInnerSpacing(ImVec2 dst) {
        nGetItemInnerSpacing(dst);
    }

    public void setItemInnerSpacing(ImVec2 value) {
        nSetItemInnerSpacing(value.x, value.y);
    }

    public void setItemInnerSpacing(float valueX, float valueY) {
        nSetItemInnerSpacing(valueX, valueY);
    }

    public ImVec2 getCellPadding() {
        ImVec2 dst = new ImVec2();
        nGetCellPadding(dst);
        return dst;
    }

    public float getCellPaddingX() {
        return nGetCellPaddingX();
    }

    public float getCellPaddingY() {
        return nGetCellPaddingY();
    }

    public void getCellPadding(ImVec2 dst) {
        nGetCellPadding(dst);
    }

    public void setCellPadding(ImVec2 value) {
        nSetCellPadding(value.x, value.y);
    }

    public void setCellPadding(float valueX, float valueY) {
        nSetCellPadding(valueX, valueY);
    }

    public ImVec2 getTouchExtraPadding() {
        ImVec2 dst = new ImVec2();
        nGetTouchExtraPadding(dst);
        return dst;
    }

    public float getTouchExtraPaddingX() {
        return nGetTouchExtraPaddingX();
    }

    public float getTouchExtraPaddingY() {
        return nGetTouchExtraPaddingY();
    }

    public void getTouchExtraPadding(ImVec2 dst) {
        nGetTouchExtraPadding(dst);
    }

    public void setTouchExtraPadding(ImVec2 value) {
        nSetTouchExtraPadding(value.x, value.y);
    }

    public void setTouchExtraPadding(float valueX, float valueY) {
        nSetTouchExtraPadding(valueX, valueY);
    }

    public float getIndentSpacing() {
        return nGetIndentSpacing();
    }

    public void setIndentSpacing(float value) {
        nSetIndentSpacing(value);
    }

    public float getColumnsMinSpacing() {
        return nGetColumnsMinSpacing();
    }

    public void setColumnsMinSpacing(float value) {
        nSetColumnsMinSpacing(value);
    }

    public float getScrollbarSize() {
        return nGetScrollbarSize();
    }

    public void setScrollbarSize(float value) {
        nSetScrollbarSize(value);
    }

    public float getScrollbarRounding() {
        return nGetScrollbarRounding();
    }

    public void setScrollbarRounding(float value) {
        nSetScrollbarRounding(value);
    }

    public float getGrabMinSize() {
        return nGetGrabMinSize();
    }

    public void setGrabMinSize(float value) {
        nSetGrabMinSize(value);
    }

    public float getGrabRounding() {
        return nGetGrabRounding();
    }

    public void setGrabRounding(float value) {
        nSetGrabRounding(value);
    }

    public float getLogSliderDeadzone() {
        return nGetLogSliderDeadzone();
    }

    public void setLogSliderDeadzone(float value) {
        nSetLogSliderDeadzone(value);
    }

    public float getTabRounding() {
        return nGetTabRounding();
    }

    public void setTabRounding(float value) {
        nSetTabRounding(value);
    }

    public float getTabBorderSize() {
        return nGetTabBorderSize();
    }

    public void setTabBorderSize(float value) {
        nSetTabBorderSize(value);
    }

    public float getTabMinWidthForCloseButton() {
        return nGetTabMinWidthForCloseButton();
    }

    public void setTabMinWidthForCloseButton(float value) {
        nSetTabMinWidthForCloseButton(value);
    }

    public int getColorButtonPosition() {
        return nGetColorButtonPosition();
    }

    public void setColorButtonPosition(int value) {
        nSetColorButtonPosition(value);
    }

    public ImVec2 getButtonTextAlign() {
        ImVec2 dst = new ImVec2();
        nGetButtonTextAlign(dst);
        return dst;
    }

    public float getButtonTextAlignX() {
        return nGetButtonTextAlignX();
    }

    public float getButtonTextAlignY() {
        return nGetButtonTextAlignY();
    }

    public void getButtonTextAlign(ImVec2 dst) {
        nGetButtonTextAlign(dst);
    }

    public void setButtonTextAlign(ImVec2 value) {
        nSetButtonTextAlign(value.x, value.y);
    }

    public void setButtonTextAlign(float valueX, float valueY) {
        nSetButtonTextAlign(valueX, valueY);
    }

    public ImVec2 getSelectableTextAlign() {
        ImVec2 dst = new ImVec2();
        nGetSelectableTextAlign(dst);
        return dst;
    }

    public float getSelectableTextAlignX() {
        return nGetSelectableTextAlignX();
    }

    public float getSelectableTextAlignY() {
        return nGetSelectableTextAlignY();
    }

    public void getSelectableTextAlign(ImVec2 dst) {
        nGetSelectableTextAlign(dst);
    }

    public void setSelectableTextAlign(ImVec2 value) {
        nSetSelectableTextAlign(value.x, value.y);
    }

    public void setSelectableTextAlign(float valueX, float valueY) {
        nSetSelectableTextAlign(valueX, valueY);
    }

    public float getSeparatorTextBorderSize() {
        return nGetSeparatorTextBorderSize();
    }

    public void setSeparatorTextBorderSize(float value) {
        nSetSeparatorTextBorderSize(value);
    }

    public ImVec2 getSeparatorTextAlign() {
        ImVec2 dst = new ImVec2();
        nGetSeparatorTextAlign(dst);
        return dst;
    }

    public float getSeparatorTextAlignX() {
        return nGetSeparatorTextAlignX();
    }

    public float getSeparatorTextAlignY() {
        return nGetSeparatorTextAlignY();
    }

    public void getSeparatorTextAlign(ImVec2 dst) {
        nGetSeparatorTextAlign(dst);
    }

    public void setSeparatorTextAlign(ImVec2 value) {
        nSetSeparatorTextAlign(value.x, value.y);
    }

    public void setSeparatorTextAlign(float valueX, float valueY) {
        nSetSeparatorTextAlign(valueX, valueY);
    }

    public ImVec2 getSeparatorTextPadding() {
        ImVec2 dst = new ImVec2();
        nGetSeparatorTextPadding(dst);
        return dst;
    }

    public float getSeparatorTextPaddingX() {
        return nGetSeparatorTextPaddingX();
    }

    public float getSeparatorTextPaddingY() {
        return nGetSeparatorTextPaddingY();
    }

    public void getSeparatorTextPadding(ImVec2 dst) {
        nGetSeparatorTextPadding(dst);
    }

    public void setSeparatorTextPadding(ImVec2 value) {
        nSetSeparatorTextPadding(value.x, value.y);
    }

    public void setSeparatorTextPadding(float valueX, float valueY) {
        nSetSeparatorTextPadding(valueX, valueY);
    }

    public ImVec2 getDisplayWindowPadding() {
        ImVec2 dst = new ImVec2();
        nGetDisplayWindowPadding(dst);
        return dst;
    }

    public float getDisplayWindowPaddingX() {
        return nGetDisplayWindowPaddingX();
    }

    public float getDisplayWindowPaddingY() {
        return nGetDisplayWindowPaddingY();
    }

    public void getDisplayWindowPadding(ImVec2 dst) {
        nGetDisplayWindowPadding(dst);
    }

    public void setDisplayWindowPadding(ImVec2 value) {
        nSetDisplayWindowPadding(value.x, value.y);
    }

    public void setDisplayWindowPadding(float valueX, float valueY) {
        nSetDisplayWindowPadding(valueX, valueY);
    }

    public ImVec2 getDisplaySafeAreaPadding() {
        ImVec2 dst = new ImVec2();
        nGetDisplaySafeAreaPadding(dst);
        return dst;
    }

    public float getDisplaySafeAreaPaddingX() {
        return nGetDisplaySafeAreaPaddingX();
    }

    public float getDisplaySafeAreaPaddingY() {
        return nGetDisplaySafeAreaPaddingY();
    }

    public void getDisplaySafeAreaPadding(ImVec2 dst) {
        nGetDisplaySafeAreaPadding(dst);
    }

    public void setDisplaySafeAreaPadding(ImVec2 value) {
        nSetDisplaySafeAreaPadding(value.x, value.y);
    }

    public void setDisplaySafeAreaPadding(float valueX, float valueY) {
        nSetDisplaySafeAreaPadding(valueX, valueY);
    }

    public float getDockingSeparatorSize() {
        return nGetDockingSeparatorSize();
    }

    public void setDockingSeparatorSize(float value) {
        nSetDockingSeparatorSize(value);
    }

    public float getMouseCursorScale() {
        return nGetMouseCursorScale();
    }

    public void setMouseCursorScale(float value) {
        nSetMouseCursorScale(value);
    }

    public boolean getAntiAliasedLines() {
        return nGetAntiAliasedLines();
    }

    public void setAntiAliasedLines(boolean value) {
        nSetAntiAliasedLines(value);
    }

    public boolean getAntiAliasedLinesUseTex() {
        return nGetAntiAliasedLinesUseTex();
    }

    public void setAntiAliasedLinesUseTex(boolean value) {
        nSetAntiAliasedLinesUseTex(value);
    }

    public boolean getAntiAliasedFill() {
        return nGetAntiAliasedFill();
    }

    public void setAntiAliasedFill(boolean value) {
        nSetAntiAliasedFill(value);
    }

    public float getCurveTessellationTol() {
        return nGetCurveTessellationTol();
    }

    public void setCurveTessellationTol(float value) {
        nSetCurveTessellationTol(value);
    }

    public float getCircleTessellationMaxError() {
        return nGetCircleTessellationMaxError();
    }

    public void setCircleTessellationMaxError(float value) {
        nSetCircleTessellationMaxError(value);
    }

    public ImVec4[] getColors() {
        return nGetColors();
    }

    public void setColors(ImVec4[] value) {
        nSetColors(value);
    }

    public ImVec4 getColor(int col) {
        ImVec4 dst = new ImVec4();
        getColor(col, dst);
        return dst;
    }

    public float getHoverStationaryDelay() {
        return nGetHoverStationaryDelay();
    }

    public void setHoverStationaryDelay(float value) {
        nSetHoverStationaryDelay(value);
    }

    public float getHoverDelayShort() {
        return nGetHoverDelayShort();
    }

    public void setHoverDelayShort(float value) {
        nSetHoverDelayShort(value);
    }

    public float getHoverDelayNormal() {
        return nGetHoverDelayNormal();
    }

    public void setHoverDelayNormal(float value) {
        nSetHoverDelayNormal(value);
    }

    public int getHoverFlagsForTooltipMouse() {
        return nGetHoverFlagsForTooltipMouse();
    }

    public void setHoverFlagsForTooltipMouse(int value) {
        nSetHoverFlagsForTooltipMouse(value);
    }

    public int getHoverFlagsForTooltipNav() {
        return nGetHoverFlagsForTooltipNav();
    }

    public void setHoverFlagsForTooltipNav(int value) {
        nSetHoverFlagsForTooltipNav(value);
    }

    public void scaleAllSizes(float scaleFactor) {
        nScaleAllSizes(scaleFactor);
    }
}
