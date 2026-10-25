package imgui;

import imgui.assertion.ImAssertCallback;
import imgui.callback.ImGuiInputTextCallback;
import imgui.internal.ImGuiContext;
import imgui.type.ImBoolean;
import imgui.type.ImDouble;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImLong;
import imgui.type.ImShort;
import imgui.type.ImString;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.ref.WeakReference;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.Optional;
import java.util.Properties;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/ImGui.class */
public class ImGui {
    private static final String LIB_PATH_PROP = "imgui.library.path";
    private static final String LIB_NAME_PROP = "imgui.library.name";
    private static final String LIB_NAME_DEFAULT = "imgui-java64";
    private static final String LIB_TMP_DIR_PREFIX = "imgui-java-natives";
    private static final ImGuiIO _GETIO_1;
    private static final ImGuiStyle _GETSTYLE_1;
    private static final ImDrawData _GETDRAWDATA_1;
    private static final ImFont _GETFONT_1;
    private static WeakReference<Object> payloadRef;
    private static final byte[] PAYLOAD_PLACEHOLDER_DATA;
    private static final ImGuiViewport _GETMAINVIEWPORT_1;
    private static final ImGuiPlatformIO _GETPLATFORMIO_1;

    private static native void nInitJni();

    public static native void setAssertCallback(ImAssertCallback imAssertCallback);

    private static native long nCreateContext();

    private static native long nCreateContext(long j);

    private static native void nDestroyContext();

    private static native void nDestroyContext(long j);

    private static native long nGetCurrentContext();

    private static native void nSetCurrentContext(long j);

    private static native long nGetIO();

    private static native long nGetStyle();

    private static native void nNewFrame();

    private static native void nEndFrame();

    private static native void nRender();

    private static native long nGetDrawData();

    private static native void nShowDemoWindow();

    private static native void nShowDemoWindow(boolean[] zArr);

    private static native void nShowMetricsWindow();

    private static native void nShowMetricsWindow(boolean[] zArr);

    private static native void nShowDebugLogWindow();

    private static native void nShowDebugLogWindow(boolean[] zArr);

    private static native void nShowIDStackToolWindow();

    private static native void nShowIDStackToolWindow(boolean[] zArr);

    private static native void nShowAboutWindow();

    private static native void nShowAboutWindow(boolean[] zArr);

    private static native void nShowStyleEditor();

    private static native void nShowStyleEditor(long j);

    private static native boolean nShowStyleSelector(String str);

    private static native void nShowFontSelector(String str);

    private static native void nShowUserGuide();

    private static native String nGetVersion();

    private static native void nStyleColorsDark();

    private static native void nStyleColorsDark(long j);

    private static native void nStyleColorsLight();

    private static native void nStyleColorsLight(long j);

    private static native void nStyleColorsClassic();

    private static native void nStyleColorsClassic(long j);

    private static native boolean nBegin(String str);

    private static native boolean nBegin(String str, boolean[] zArr);

    private static native boolean nBegin(String str, boolean[] zArr, int i);

    private static native boolean nBegin(String str, int i);

    private static native void nEnd();

    private static native boolean nBeginChild(String str);

    private static native boolean nBeginChild(String str, float f, float f2);

    private static native boolean nBeginChild(String str, float f, float f2, int i);

    private static native boolean nBeginChild(String str, float f, float f2, int i, int i2);

    private static native boolean nBeginChild(String str, int i, int i2);

    private static native boolean nBeginChild(String str, int i);

    private static native boolean nBeginChild(int i);

    private static native boolean nBeginChild(int i, float f, float f2);

    private static native boolean nBeginChild(int i, float f, float f2, int i2);

    private static native boolean nBeginChild(int i, float f, float f2, int i2, int i3);

    private static native boolean nBeginChild(int i, int i2, int i3);

    private static native boolean nBeginChild(int i, int i2);

    private static native boolean nBeginChild(String str, float f, float f2, boolean z);

    private static native boolean nBeginChild(String str, float f, float f2, boolean z, int i);

    private static native boolean nBeginChild(int i, float f, float f2, boolean z);

    private static native boolean nBeginChild(int i, float f, float f2, boolean z, int i2);

    private static native void nEndChild();

    private static native boolean nIsWindowAppearing();

    private static native boolean nIsWindowCollapsed();

    private static native boolean nIsWindowFocused();

    private static native boolean nIsWindowFocused(int i);

    private static native boolean nIsWindowHovered();

    private static native boolean nIsWindowHovered(int i);

    private static native long nGetWindowDrawList();

    private static native float nGetWindowDpiScale();

    private static native void nGetWindowPos(ImVec2 imVec2);

    private static native float nGetWindowPosX();

    private static native float nGetWindowPosY();

    private static native void nGetWindowSize(ImVec2 imVec2);

    private static native float nGetWindowSizeX();

    private static native float nGetWindowSizeY();

    private static native float nGetWindowWidth();

    private static native float nGetWindowHeight();

    private static native long nGetWindowViewport();

    private static native void nSetNextWindowPos(float f, float f2);

    private static native void nSetNextWindowPos(float f, float f2, int i);

    private static native void nSetNextWindowPos(float f, float f2, int i, float f3, float f4);

    private static native void nSetNextWindowPos(float f, float f2, float f3, float f4);

    private static native void nSetNextWindowSize(float f, float f2);

    private static native void nSetNextWindowSize(float f, float f2, int i);

    private static native void nSetNextWindowSizeConstraints(float f, float f2, float f3, float f4);

    private static native void nSetNextWindowContentSize(float f, float f2);

    private static native void nSetNextWindowCollapsed(boolean z);

    private static native void nSetNextWindowCollapsed(boolean z, int i);

    private static native void nSetNextWindowFocus();

    private static native void nSetNextWindowScroll(float f, float f2);

    private static native void nSetNextWindowBgAlpha(float f);

    private static native void nSetNextWindowViewport(int i);

    private static native void nSetWindowPos(float f, float f2);

    private static native void nSetWindowPos(float f, float f2, int i);

    private static native void nSetWindowSize(float f, float f2);

    private static native void nSetWindowSize(float f, float f2, int i);

    private static native void nSetWindowCollapsed(boolean z);

    private static native void nSetWindowCollapsed(boolean z, int i);

    private static native void nSetWindowFocus();

    private static native void nSetWindowFontScale(float f);

    private static native void nSetWindowPos(String str, float f, float f2);

    private static native void nSetWindowPos(String str, float f, float f2, int i);

    private static native void nSetWindowSize(String str, float f, float f2);

    private static native void nSetWindowSize(String str, float f, float f2, int i);

    private static native void nSetWindowCollapsed(String str, boolean z);

    private static native void nSetWindowCollapsed(String str, boolean z, int i);

    private static native void nSetWindowFocus(String str);

    private static native void nGetContentRegionAvail(ImVec2 imVec2);

    private static native float nGetContentRegionAvailX();

    private static native float nGetContentRegionAvailY();

    private static native void nGetContentRegionMax(ImVec2 imVec2);

    private static native float nGetContentRegionMaxX();

    private static native float nGetContentRegionMaxY();

    private static native void nGetWindowContentRegionMin(ImVec2 imVec2);

    private static native float nGetWindowContentRegionMinX();

    private static native float nGetWindowContentRegionMinY();

    private static native void nGetWindowContentRegionMax(ImVec2 imVec2);

    private static native float nGetWindowContentRegionMaxX();

    private static native float nGetWindowContentRegionMaxY();

    private static native float nGetScrollX();

    private static native float nGetScrollY();

    private static native void nSetScrollX(float f);

    private static native void nSetScrollY(float f);

    private static native float nGetScrollMaxX();

    private static native float nGetScrollMaxY();

    private static native void nSetScrollHereX();

    private static native void nSetScrollHereX(float f);

    private static native void nSetScrollHereY();

    private static native void nSetScrollHereY(float f);

    private static native void nSetScrollFromPosX(float f);

    private static native void nSetScrollFromPosX(float f, float f2);

    private static native void nSetScrollFromPosY(float f);

    private static native void nSetScrollFromPosY(float f, float f2);

    private static native void nPushFont(long j);

    private static native void nPopFont();

    public static native void pushStyleColor(int i, int i2, int i3, int i4, int i5);

    private static native void nPushStyleColor(int i, float f, float f2, float f3, float f4);

    private static native void nPushStyleColor(int i, int i2);

    private static native void nPopStyleColor();

    private static native void nPopStyleColor(int i);

    private static native void nPushStyleVar(int i, float f);

    private static native void nPushStyleVar(int i, float f, float f2);

    private static native void nPopStyleVar();

    private static native void nPopStyleVar(int i);

    private static native void nPushTabStop(boolean z);

    private static native void nPopTabStop();

    private static native void nPushButtonRepeat(boolean z);

    private static native void nPopButtonRepeat();

    private static native void nPushItemWidth(float f);

    private static native void nPopItemWidth();

    private static native void nSetNextItemWidth(float f);

    private static native float nCalcItemWidth();

    private static native void nPushTextWrapPos();

    private static native void nPushTextWrapPos(float f);

    private static native void nPopTextWrapPos();

    private static native long nGetFont();

    private static native int nGetFontSize();

    private static native void nGetFontTexUvWhitePixel(ImVec2 imVec2);

    private static native float nGetFontTexUvWhitePixelX();

    private static native float nGetFontTexUvWhitePixelY();

    private static native int nGetColorU32(int i);

    private static native int nGetColorU32(int i, float f);

    private static native int nGetColorU32(float f, float f2, float f3, float f4);

    private static native int nGetColorU32i(int i);

    private static native int nGetColorU32i(int i, float f);

    private static native void nGetStyleColorVec4(ImVec4 imVec4, int i);

    private static native float nGetStyleColorVec4X(int i);

    private static native float nGetStyleColorVec4Y(int i);

    private static native float nGetStyleColorVec4Z(int i);

    private static native float nGetStyleColorVec4W(int i);

    private static native void nGetCursorScreenPos(ImVec2 imVec2);

    private static native float nGetCursorScreenPosX();

    private static native float nGetCursorScreenPosY();

    private static native void nSetCursorScreenPos(float f, float f2);

    private static native void nGetCursorPos(ImVec2 imVec2);

    private static native float nGetCursorPosX();

    private static native float nGetCursorPosY();

    private static native void nSetCursorPos(float f, float f2);

    private static native void nSetCursorPosX(float f);

    private static native void nSetCursorPosY(float f);

    private static native void nGetCursorStartPos(ImVec2 imVec2);

    private static native float nGetCursorStartPosX();

    private static native float nGetCursorStartPosY();

    private static native void nSeparator();

    private static native void nSameLine();

    private static native void nSameLine(float f);

    private static native void nSameLine(float f, float f2);

    private static native void nNewLine();

    private static native void nSpacing();

    private static native void nDummy(float f, float f2);

    private static native void nIndent();

    private static native void nIndent(float f);

    private static native void nUnindent();

    private static native void nUnindent(float f);

    private static native void nBeginGroup();

    private static native void nEndGroup();

    private static native void nAlignTextToFramePadding();

    private static native float nGetTextLineHeight();

    private static native float nGetTextLineHeightWithSpacing();

    private static native float nGetFrameHeight();

    private static native float nGetFrameHeightWithSpacing();

    private static native void nPushID(String str);

    private static native void nPushID(String str, String str2);

    private static native void nPushID(long j);

    private static native void nPushID(int i);

    private static native void nPopID();

    private static native int nGetID(String str);

    private static native int nGetID(String str, String str2);

    private static native int nGetID(long j);

    private static native void nTextUnformatted(String str);

    private static native void nTextUnformatted(String str, String str2);

    private static native void nText(String str);

    private static native void nTextColored(float f, float f2, float f3, float f4, String str);

    public static native void textColored(int i, int i2, int i3, int i4, String str);

    public static native void textColored(int i, String str);

    private static native void nTextDisabled(String str);

    private static native void nTextWrapped(String str);

    private static native void nLabelText(String str, String str2);

    private static native void nBulletText(String str);

    private static native void nSeparatorText(String str);

    private static native boolean nButton(String str);

    private static native boolean nButton(String str, float f, float f2);

    private static native boolean nSmallButton(String str);

    private static native boolean nInvisibleButton(String str, float f, float f2);

    private static native boolean nInvisibleButton(String str, float f, float f2, int i);

    private static native boolean nArrowButton(String str, int i);

    private static native boolean nCheckbox(String str, boolean z);

    private static native boolean nCheckbox(String str, boolean[] zArr);

    private static native boolean nCheckboxFlags(String str, int[] iArr, int i);

    private static native boolean nRadioButton(String str, boolean z);

    private static native boolean nRadioButton(String str, int[] iArr, int i);

    private static native void nProgressBar(float f);

    private static native void nProgressBar(float f, float f2, float f3);

    private static native void nProgressBar(float f, float f2, float f3, String str);

    private static native void nProgressBar(float f, String str);

    private static native void nBullet();

    private static native void nImage(long j, float f, float f2);

    private static native void nImage(long j, float f, float f2, float f3, float f4);

    private static native void nImage(long j, float f, float f2, float f3, float f4, float f5, float f6);

    private static native void nImage(long j, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10);

    private static native void nImage(long j, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14);

    private static native boolean nImageButton(String str, long j, float f, float f2);

    private static native boolean nImageButton(String str, long j, float f, float f2, float f3, float f4);

    private static native boolean nImageButton(String str, long j, float f, float f2, float f3, float f4, float f5, float f6);

    private static native boolean nImageButton(String str, long j, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10);

    private static native boolean nImageButton(String str, long j, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14);

    private static native boolean nBeginCombo(String str, String str2);

    private static native boolean nBeginCombo(String str, String str2, int i);

    private static native void nEndCombo();

    private static native boolean nCombo(String str, int[] iArr, String[] strArr, int i);

    private static native boolean nCombo(String str, int[] iArr, String[] strArr, int i, int i2);

    private static native boolean nCombo(String str, int[] iArr, String str2);

    private static native boolean nCombo(String str, int[] iArr, String str2, int i);

    private static native boolean nDragFloat(String str, float[] fArr);

    private static native boolean nDragFloat(String str, float[] fArr, float f);

    private static native boolean nDragFloat(String str, float[] fArr, float f, float f2);

    private static native boolean nDragFloat(String str, float[] fArr, float f, float f2, float f3);

    private static native boolean nDragFloat(String str, float[] fArr, float f, float f2, float f3, String str2);

    private static native boolean nDragFloat(String str, float[] fArr, float f, float f2, float f3, String str2, int i);

    private static native boolean nDragFloat(String str, float[] fArr, float f, float f2, float f3, int i);

    private static native boolean nDragFloat2(String str, float[] fArr);

    private static native boolean nDragFloat2(String str, float[] fArr, float f);

    private static native boolean nDragFloat2(String str, float[] fArr, float f, float f2);

    private static native boolean nDragFloat2(String str, float[] fArr, float f, float f2, float f3);

    private static native boolean nDragFloat2(String str, float[] fArr, float f, float f2, float f3, String str2);

    private static native boolean nDragFloat2(String str, float[] fArr, float f, float f2, float f3, String str2, int i);

    private static native boolean nDragFloat2(String str, float[] fArr, float f, float f2, float f3, int i);

    private static native boolean nDragFloat3(String str, float[] fArr);

    private static native boolean nDragFloat3(String str, float[] fArr, float f);

    private static native boolean nDragFloat3(String str, float[] fArr, float f, float f2);

    private static native boolean nDragFloat3(String str, float[] fArr, float f, float f2, float f3);

    private static native boolean nDragFloat3(String str, float[] fArr, float f, float f2, float f3, String str2);

    private static native boolean nDragFloat3(String str, float[] fArr, float f, float f2, float f3, String str2, int i);

    private static native boolean nDragFloat3(String str, float[] fArr, float f, float f2, float f3, int i);

    private static native boolean nDragFloat4(String str, float[] fArr);

    private static native boolean nDragFloat4(String str, float[] fArr, float f);

    private static native boolean nDragFloat4(String str, float[] fArr, float f, float f2);

    private static native boolean nDragFloat4(String str, float[] fArr, float f, float f2, float f3);

    private static native boolean nDragFloat4(String str, float[] fArr, float f, float f2, float f3, String str2);

    private static native boolean nDragFloat4(String str, float[] fArr, float f, float f2, float f3, String str2, int i);

    private static native boolean nDragFloat4(String str, float[] fArr, float f, float f2, float f3, int i);

    private static native boolean nDragFloatRange2(String str, float[] fArr, float[] fArr2, float f);

    private static native boolean nDragFloatRange2(String str, float[] fArr, float[] fArr2, float f, float f2);

    private static native boolean nDragFloatRange2(String str, float[] fArr, float[] fArr2, float f, float f2, float f3);

    private static native boolean nDragFloatRange2(String str, float[] fArr, float[] fArr2, float f, float f2, float f3, String str2);

    private static native boolean nDragFloatRange2(String str, float[] fArr, float[] fArr2, float f, float f2, float f3, String str2, String str3);

    private static native boolean nDragFloatRange2(String str, float[] fArr, float[] fArr2, float f, float f2, float f3, String str2, String str3, int i);

    private static native boolean nDragInt(String str, int[] iArr);

    private static native boolean nDragInt(String str, int[] iArr, float f);

    private static native boolean nDragInt(String str, int[] iArr, float f, int i);

    private static native boolean nDragInt(String str, int[] iArr, float f, int i, int i2);

    private static native boolean nDragInt(String str, int[] iArr, float f, int i, int i2, String str2);

    private static native boolean nDragInt(String str, int[] iArr, float f, int i, int i2, String str2, int i3);

    private static native boolean nDragInt(String str, int[] iArr, float f, int i, int i2, int i3);

    private static native boolean nDragInt2(String str, int[] iArr);

    private static native boolean nDragInt2(String str, int[] iArr, float f);

    private static native boolean nDragInt2(String str, int[] iArr, float f, int i);

    private static native boolean nDragInt2(String str, int[] iArr, float f, int i, int i2);

    private static native boolean nDragInt2(String str, int[] iArr, float f, int i, int i2, String str2);

    private static native boolean nDragInt2(String str, int[] iArr, float f, int i, int i2, String str2, int i3);

    private static native boolean nDragInt2(String str, int[] iArr, float f, int i, int i2, int i3);

    private static native boolean nDragInt3(String str, int[] iArr);

    private static native boolean nDragInt3(String str, int[] iArr, float f);

    private static native boolean nDragInt3(String str, int[] iArr, float f, int i);

    private static native boolean nDragInt3(String str, int[] iArr, float f, int i, int i2);

    private static native boolean nDragInt3(String str, int[] iArr, float f, int i, int i2, String str2);

    private static native boolean nDragInt3(String str, int[] iArr, float f, int i, int i2, String str2, int i3);

    private static native boolean nDragInt3(String str, int[] iArr, float f, int i, int i2, int i3);

    private static native boolean nDragInt4(String str, int[] iArr);

    private static native boolean nDragInt4(String str, int[] iArr, float f);

    private static native boolean nDragInt4(String str, int[] iArr, float f, int i);

    private static native boolean nDragInt4(String str, int[] iArr, float f, int i, int i2);

    private static native boolean nDragInt4(String str, int[] iArr, float f, int i, int i2, String str2);

    private static native boolean nDragInt4(String str, int[] iArr, float f, int i, int i2, String str2, int i3);

    private static native boolean nDragInt4(String str, int[] iArr, float f, int i, int i2, int i3);

    private static native boolean nDragIntRange2(String str, int[] iArr, int[] iArr2);

    private static native boolean nDragIntRange2(String str, int[] iArr, int[] iArr2, float f);

    private static native boolean nDragIntRange2(String str, int[] iArr, int[] iArr2, float f, int i);

    private static native boolean nDragIntRange2(String str, int[] iArr, int[] iArr2, float f, int i, int i2);

    private static native boolean nDragIntRange2(String str, int[] iArr, int[] iArr2, float f, int i, int i2, String str2);

    private static native boolean nDragIntRange2(String str, int[] iArr, int[] iArr2, float f, int i, int i2, String str2, String str3);

    private static native boolean nDragIntRange2(String str, int[] iArr, int[] iArr2, float f, int i, int i2, String str2, String str3, int i3);

    private static native boolean nDragScalar(String str, short[] sArr);

    private static native boolean nDragScalar(String str, short[] sArr, float f);

    private static native boolean nDragScalar(String str, short[] sArr, float f, short s);

    private static native boolean nDragScalar(String str, short[] sArr, float f, short s, short s2);

    private static native boolean nDragScalar(String str, short[] sArr, float f, short s, short s2, String str2);

    private static native boolean nDragScalar(String str, short[] sArr, float f, short s, short s2, String str2, int i);

    private static native boolean nDragScalar(String str, int[] iArr);

    private static native boolean nDragScalar(String str, int[] iArr, float f);

    private static native boolean nDragScalar(String str, int[] iArr, float f, int i);

    private static native boolean nDragScalar(String str, int[] iArr, float f, int i, int i2);

    private static native boolean nDragScalar(String str, int[] iArr, float f, int i, int i2, String str2);

    private static native boolean nDragScalar(String str, int[] iArr, float f, int i, int i2, String str2, int i3);

    private static native boolean nDragScalar(String str, long[] jArr);

    private static native boolean nDragScalar(String str, long[] jArr, float f);

    private static native boolean nDragScalar(String str, long[] jArr, float f, long j);

    private static native boolean nDragScalar(String str, long[] jArr, float f, long j, long j2);

    private static native boolean nDragScalar(String str, long[] jArr, float f, long j, long j2, String str2);

    private static native boolean nDragScalar(String str, long[] jArr, float f, long j, long j2, String str2, int i);

    private static native boolean nDragScalar(String str, float[] fArr);

    private static native boolean nDragScalar(String str, float[] fArr, float f);

    private static native boolean nDragScalar(String str, float[] fArr, float f, float f2);

    private static native boolean nDragScalar(String str, float[] fArr, float f, float f2, float f3);

    private static native boolean nDragScalar(String str, float[] fArr, float f, float f2, float f3, String str2);

    private static native boolean nDragScalar(String str, float[] fArr, float f, float f2, float f3, String str2, int i);

    private static native boolean nDragScalar(String str, double[] dArr);

    private static native boolean nDragScalar(String str, double[] dArr, float f);

    private static native boolean nDragScalar(String str, double[] dArr, float f, double d);

    private static native boolean nDragScalar(String str, double[] dArr, float f, double d, double d2);

    private static native boolean nDragScalar(String str, double[] dArr, float f, double d, double d2, String str2);

    private static native boolean nDragScalar(String str, double[] dArr, float f, double d, double d2, String str2, int i);

    private static native boolean nDragScalarN(String str, short[] sArr, int i);

    private static native boolean nDragScalarN(String str, short[] sArr, int i, float f);

    private static native boolean nDragScalarN(String str, short[] sArr, int i, float f, short s);

    private static native boolean nDragScalarN(String str, short[] sArr, int i, float f, short s, short s2);

    private static native boolean nDragScalarN(String str, short[] sArr, int i, float f, short s, short s2, String str2);

    private static native boolean nDragScalarN(String str, short[] sArr, int i, float f, short s, short s2, String str2, int i2);

    private static native boolean nDragScalarN(String str, int[] iArr, int i);

    private static native boolean nDragScalarN(String str, int[] iArr, int i, float f);

    private static native boolean nDragScalarN(String str, int[] iArr, int i, float f, int i2);

    private static native boolean nDragScalarN(String str, int[] iArr, int i, float f, int i2, int i3);

    private static native boolean nDragScalarN(String str, int[] iArr, int i, float f, int i2, int i3, String str2);

    private static native boolean nDragScalarN(String str, int[] iArr, int i, float f, int i2, int i3, String str2, int i4);

    private static native boolean nDragScalarN(String str, long[] jArr, int i);

    private static native boolean nDragScalarN(String str, long[] jArr, int i, float f);

    private static native boolean nDragScalarN(String str, long[] jArr, int i, float f, long j);

    private static native boolean nDragScalarN(String str, long[] jArr, int i, float f, long j, long j2);

    private static native boolean nDragScalarN(String str, long[] jArr, int i, float f, long j, long j2, String str2);

    private static native boolean nDragScalarN(String str, long[] jArr, int i, float f, long j, long j2, String str2, int i2);

    private static native boolean nDragScalarN(String str, float[] fArr, int i);

    private static native boolean nDragScalarN(String str, float[] fArr, int i, float f);

    private static native boolean nDragScalarN(String str, float[] fArr, int i, float f, float f2);

    private static native boolean nDragScalarN(String str, float[] fArr, int i, float f, float f2, float f3);

    private static native boolean nDragScalarN(String str, float[] fArr, int i, float f, float f2, float f3, String str2);

    private static native boolean nDragScalarN(String str, float[] fArr, int i, float f, float f2, float f3, String str2, int i2);

    private static native boolean nDragScalarN(String str, double[] dArr, int i);

    private static native boolean nDragScalarN(String str, double[] dArr, int i, float f);

    private static native boolean nDragScalarN(String str, double[] dArr, int i, float f, double d);

    private static native boolean nDragScalarN(String str, double[] dArr, int i, float f, double d, double d2);

    private static native boolean nDragScalarN(String str, double[] dArr, int i, float f, double d, double d2, String str2);

    private static native boolean nDragScalarN(String str, double[] dArr, int i, float f, double d, double d2, String str2, int i2);

    private static native boolean nSliderFloat(String str, float[] fArr, float f, float f2);

    private static native boolean nSliderFloat(String str, float[] fArr, float f, float f2, String str2);

    private static native boolean nSliderFloat(String str, float[] fArr, float f, float f2, String str2, int i);

    private static native boolean nSliderFloat(String str, float[] fArr, float f, float f2, int i);

    private static native boolean nSliderFloat2(String str, float[] fArr, float f, float f2);

    private static native boolean nSliderFloat2(String str, float[] fArr, float f, float f2, String str2);

    private static native boolean nSliderFloat2(String str, float[] fArr, float f, float f2, String str2, int i);

    private static native boolean nSliderFloat2(String str, float[] fArr, float f, float f2, int i);

    private static native boolean nSliderFloat3(String str, float[] fArr, float f, float f2);

    private static native boolean nSliderFloat3(String str, float[] fArr, float f, float f2, String str2);

    private static native boolean nSliderFloat3(String str, float[] fArr, float f, float f2, String str2, int i);

    private static native boolean nSliderFloat3(String str, float[] fArr, float f, float f2, int i);

    private static native boolean nSliderFloat4(String str, float[] fArr, float f, float f2);

    private static native boolean nSliderFloat4(String str, float[] fArr, float f, float f2, String str2);

    private static native boolean nSliderFloat4(String str, float[] fArr, float f, float f2, String str2, int i);

    private static native boolean nSliderFloat4(String str, float[] fArr, float f, float f2, int i);

    private static native boolean nSliderAngle(String str, float[] fArr);

    private static native boolean nSliderAngle(String str, float[] fArr, float f);

    private static native boolean nSliderAngle(String str, float[] fArr, float f, float f2);

    private static native boolean nSliderAngle(String str, float[] fArr, float f, float f2, String str2);

    private static native boolean nSliderAngle(String str, float[] fArr, float f, float f2, String str2, int i);

    private static native boolean nSliderAngle(String str, float[] fArr, float f, float f2, int i);

    private static native boolean nSliderInt(String str, int[] iArr, int i, int i2);

    private static native boolean nSliderInt(String str, int[] iArr, int i, int i2, String str2);

    private static native boolean nSliderInt(String str, int[] iArr, int i, int i2, String str2, int i3);

    private static native boolean nSliderInt(String str, int[] iArr, int i, int i2, int i3);

    private static native boolean nSliderInt2(String str, int[] iArr, int i, int i2);

    private static native boolean nSliderInt2(String str, int[] iArr, int i, int i2, String str2);

    private static native boolean nSliderInt2(String str, int[] iArr, int i, int i2, String str2, int i3);

    private static native boolean nSliderInt2(String str, int[] iArr, int i, int i2, int i3);

    private static native boolean nSliderInt3(String str, int[] iArr, int i, int i2);

    private static native boolean nSliderInt3(String str, int[] iArr, int i, int i2, String str2);

    private static native boolean nSliderInt3(String str, int[] iArr, int i, int i2, String str2, int i3);

    private static native boolean nSliderInt3(String str, int[] iArr, int i, int i2, int i3);

    private static native boolean nSliderInt4(String str, int[] iArr, int i, int i2);

    private static native boolean nSliderInt4(String str, int[] iArr, int i, int i2, String str2);

    private static native boolean nSliderInt4(String str, int[] iArr, int i, int i2, String str2, int i3);

    private static native boolean nSliderInt4(String str, int[] iArr, int i, int i2, int i3);

    private static native boolean nSliderScalar(String str, short[] sArr, short s, short s2);

    private static native boolean nSliderScalar(String str, short[] sArr, short s, short s2, String str2);

    private static native boolean nSliderScalar(String str, short[] sArr, short s, short s2, String str2, int i);

    private static native boolean nSliderScalar(String str, int[] iArr, int i, int i2);

    private static native boolean nSliderScalar(String str, int[] iArr, int i, int i2, String str2);

    private static native boolean nSliderScalar(String str, int[] iArr, int i, int i2, String str2, int i3);

    private static native boolean nSliderScalar(String str, long[] jArr, long j, long j2);

    private static native boolean nSliderScalar(String str, long[] jArr, long j, long j2, String str2);

    private static native boolean nSliderScalar(String str, long[] jArr, long j, long j2, String str2, int i);

    private static native boolean nSliderScalar(String str, float[] fArr, float f, float f2);

    private static native boolean nSliderScalar(String str, float[] fArr, float f, float f2, String str2);

    private static native boolean nSliderScalar(String str, float[] fArr, float f, float f2, String str2, int i);

    private static native boolean nSliderScalar(String str, double[] dArr, double d, double d2);

    private static native boolean nSliderScalar(String str, double[] dArr, double d, double d2, String str2);

    private static native boolean nSliderScalar(String str, double[] dArr, double d, double d2, String str2, int i);

    private static native boolean nSliderScalarN(String str, short[] sArr, int i, short s, short s2);

    private static native boolean nSliderScalarN(String str, short[] sArr, int i, short s, short s2, String str2);

    private static native boolean nSliderScalarN(String str, short[] sArr, int i, short s, short s2, String str2, int i2);

    private static native boolean nSliderScalarN(String str, int[] iArr, int i, int i2, int i3);

    private static native boolean nSliderScalarN(String str, int[] iArr, int i, int i2, int i3, String str2);

    private static native boolean nSliderScalarN(String str, int[] iArr, int i, int i2, int i3, String str2, int i4);

    private static native boolean nSliderScalarN(String str, long[] jArr, int i, long j, long j2);

    private static native boolean nSliderScalarN(String str, long[] jArr, int i, long j, long j2, String str2);

    private static native boolean nSliderScalarN(String str, long[] jArr, int i, long j, long j2, String str2, int i2);

    private static native boolean nSliderScalarN(String str, float[] fArr, int i, float f, float f2);

    private static native boolean nSliderScalarN(String str, float[] fArr, int i, float f, float f2, String str2);

    private static native boolean nSliderScalarN(String str, float[] fArr, int i, float f, float f2, String str2, int i2);

    private static native boolean nSliderScalarN(String str, double[] dArr, int i, double d, double d2);

    private static native boolean nSliderScalarN(String str, double[] dArr, int i, double d, double d2, String str2);

    private static native boolean nSliderScalarN(String str, double[] dArr, int i, double d, double d2, String str2, int i2);

    private static native boolean nVSliderFloat(String str, float f, float f2, float[] fArr, float f3, float f4);

    private static native boolean nVSliderFloat(String str, float f, float f2, float[] fArr, float f3, float f4, String str2);

    private static native boolean nVSliderFloat(String str, float f, float f2, float[] fArr, float f3, float f4, String str2, int i);

    private static native boolean nVSliderFloat(String str, float f, float f2, float[] fArr, float f3, float f4, int i);

    private static native boolean nVSliderInt(String str, float f, float f2, int[] iArr, int i, int i2);

    private static native boolean nVSliderInt(String str, float f, float f2, int[] iArr, int i, int i2, String str2);

    private static native boolean nVSliderInt(String str, float f, float f2, int[] iArr, int i, int i2, String str2, int i3);

    private static native boolean nVSliderInt(String str, float f, float f2, int[] iArr, int i, int i2, int i3);

    private static native boolean nVSliderScalar(String str, float f, float f2, short[] sArr, short s, short s2);

    private static native boolean nVSliderScalar(String str, float f, float f2, short[] sArr, short s, short s2, String str2);

    private static native boolean nVSliderScalar(String str, float f, float f2, short[] sArr, short s, short s2, String str2, int i);

    private static native boolean nVSliderScalar(String str, float f, float f2, int[] iArr, int i, int i2);

    private static native boolean nVSliderScalar(String str, float f, float f2, int[] iArr, int i, int i2, String str2);

    private static native boolean nVSliderScalar(String str, float f, float f2, int[] iArr, int i, int i2, String str2, int i3);

    private static native boolean nVSliderScalar(String str, float f, float f2, long[] jArr, long j, long j2);

    private static native boolean nVSliderScalar(String str, float f, float f2, long[] jArr, long j, long j2, String str2);

    private static native boolean nVSliderScalar(String str, float f, float f2, long[] jArr, long j, long j2, String str2, int i);

    private static native boolean nVSliderScalar(String str, float f, float f2, float[] fArr, float f3, float f4);

    private static native boolean nVSliderScalar(String str, float f, float f2, float[] fArr, float f3, float f4, String str2);

    private static native boolean nVSliderScalar(String str, float f, float f2, float[] fArr, float f3, float f4, String str2, int i);

    private static native boolean nVSliderScalar(String str, float f, float f2, double[] dArr, double d, double d2);

    private static native boolean nVSliderScalar(String str, float f, float f2, double[] dArr, double d, double d2, String str2);

    private static native boolean nVSliderScalar(String str, float f, float f2, double[] dArr, double d, double d2, String str2, int i);

    private static native void nInitInputTextData();

    private static native boolean nInputText(boolean z, boolean z2, String str, String str2, ImString imString, byte[] bArr, int i, float f, float f2, int i2, ImString.InputData inputData, String str3, boolean z3, ImGuiInputTextCallback imGuiInputTextCallback);

    private static native boolean nInputFloat(String str, float[] fArr);

    private static native boolean nInputFloat(String str, float[] fArr, float f);

    private static native boolean nInputFloat(String str, float[] fArr, float f, float f2);

    private static native boolean nInputFloat(String str, float[] fArr, float f, float f2, String str2);

    private static native boolean nInputFloat(String str, float[] fArr, float f, float f2, String str2, int i);

    private static native boolean nInputFloat(String str, float[] fArr, float f, float f2, int i);

    private static native boolean nInputFloat2(String str, float[] fArr);

    private static native boolean nInputFloat2(String str, float[] fArr, String str2);

    private static native boolean nInputFloat2(String str, float[] fArr, String str2, int i);

    private static native boolean nInputFloat2(String str, float[] fArr, int i);

    private static native boolean nInputFloat3(String str, float[] fArr);

    private static native boolean nInputFloat3(String str, float[] fArr, String str2);

    private static native boolean nInputFloat3(String str, float[] fArr, String str2, int i);

    private static native boolean nInputFloat3(String str, float[] fArr, int i);

    private static native boolean nInputFloat4(String str, float[] fArr);

    private static native boolean nInputFloat4(String str, float[] fArr, String str2);

    private static native boolean nInputFloat4(String str, float[] fArr, String str2, int i);

    private static native boolean nInputFloat4(String str, float[] fArr, int i);

    private static native boolean nInputInt(String str, int[] iArr);

    private static native boolean nInputInt(String str, int[] iArr, int i);

    private static native boolean nInputInt(String str, int[] iArr, int i, int i2);

    private static native boolean nInputInt(String str, int[] iArr, int i, int i2, int i3);

    private static native boolean nInputInt2(String str, int[] iArr);

    private static native boolean nInputInt2(String str, int[] iArr, int i);

    private static native boolean nInputInt3(String str, int[] iArr);

    private static native boolean nInputInt3(String str, int[] iArr, int i);

    private static native boolean nInputInt4(String str, int[] iArr);

    private static native boolean nInputInt4(String str, int[] iArr, int i);

    private static native boolean nInputDouble(String str, double[] dArr);

    private static native boolean nInputDouble(String str, double[] dArr, double d);

    private static native boolean nInputDouble(String str, double[] dArr, double d, double d2);

    private static native boolean nInputDouble(String str, double[] dArr, double d, double d2, String str2);

    private static native boolean nInputDouble(String str, double[] dArr, double d, double d2, String str2, int i);

    private static native boolean nInputDouble(String str, double[] dArr, double d, double d2, int i);

    private static native boolean nInputScalar(String str, short[] sArr);

    private static native boolean nInputScalar(String str, short[] sArr, short s);

    private static native boolean nInputScalar(String str, short[] sArr, short s, short s2);

    private static native boolean nInputScalar(String str, short[] sArr, short s, short s2, String str2);

    private static native boolean nInputScalar(String str, short[] sArr, short s, short s2, String str2, int i);

    private static native boolean nInputScalar(String str, int[] iArr);

    private static native boolean nInputScalar(String str, int[] iArr, int i);

    private static native boolean nInputScalar(String str, int[] iArr, int i, int i2);

    private static native boolean nInputScalar(String str, int[] iArr, int i, int i2, String str2);

    private static native boolean nInputScalar(String str, int[] iArr, int i, int i2, String str2, int i3);

    private static native boolean nInputScalar(String str, long[] jArr);

    private static native boolean nInputScalar(String str, long[] jArr, long j);

    private static native boolean nInputScalar(String str, long[] jArr, long j, long j2);

    private static native boolean nInputScalar(String str, long[] jArr, long j, long j2, String str2);

    private static native boolean nInputScalar(String str, long[] jArr, long j, long j2, String str2, int i);

    private static native boolean nInputScalar(String str, float[] fArr);

    private static native boolean nInputScalar(String str, float[] fArr, float f);

    private static native boolean nInputScalar(String str, float[] fArr, float f, float f2);

    private static native boolean nInputScalar(String str, float[] fArr, float f, float f2, String str2);

    private static native boolean nInputScalar(String str, float[] fArr, float f, float f2, String str2, int i);

    private static native boolean nInputScalar(String str, double[] dArr);

    private static native boolean nInputScalar(String str, double[] dArr, double d);

    private static native boolean nInputScalar(String str, double[] dArr, double d, double d2);

    private static native boolean nInputScalar(String str, double[] dArr, double d, double d2, String str2);

    private static native boolean nInputScalar(String str, double[] dArr, double d, double d2, String str2, int i);

    private static native boolean nInputScalar(String str, int i, short[] sArr);

    private static native boolean nInputScalar(String str, int i, short[] sArr, short s);

    private static native boolean nInputScalar(String str, int i, short[] sArr, short s, short s2);

    private static native boolean nInputScalar(String str, int i, short[] sArr, short s, short s2, String str2);

    private static native boolean nInputScalar(String str, int i, short[] sArr, short s, short s2, String str2, int i2);

    private static native boolean nInputScalar(String str, int i, int[] iArr);

    private static native boolean nInputScalar(String str, int i, int[] iArr, int i2);

    private static native boolean nInputScalar(String str, int i, int[] iArr, int i2, int i3);

    private static native boolean nInputScalar(String str, int i, int[] iArr, int i2, int i3, String str2);

    private static native boolean nInputScalar(String str, int i, int[] iArr, int i2, int i3, String str2, int i4);

    private static native boolean nInputScalar(String str, int i, long[] jArr);

    private static native boolean nInputScalar(String str, int i, long[] jArr, long j);

    private static native boolean nInputScalar(String str, int i, long[] jArr, long j, long j2);

    private static native boolean nInputScalar(String str, int i, long[] jArr, long j, long j2, String str2);

    private static native boolean nInputScalar(String str, int i, long[] jArr, long j, long j2, String str2, int i2);

    private static native boolean nInputScalar(String str, int i, float[] fArr);

    private static native boolean nInputScalar(String str, int i, float[] fArr, float f);

    private static native boolean nInputScalar(String str, int i, float[] fArr, float f, float f2);

    private static native boolean nInputScalar(String str, int i, float[] fArr, float f, float f2, String str2);

    private static native boolean nInputScalar(String str, int i, float[] fArr, float f, float f2, String str2, int i2);

    private static native boolean nInputScalar(String str, int i, double[] dArr);

    private static native boolean nInputScalar(String str, int i, double[] dArr, double d);

    private static native boolean nInputScalar(String str, int i, double[] dArr, double d, double d2);

    private static native boolean nInputScalar(String str, int i, double[] dArr, double d, double d2, String str2);

    private static native boolean nInputScalar(String str, int i, double[] dArr, double d, double d2, String str2, int i2);

    private static native boolean nInputScalarN(String str, short[] sArr, int i);

    private static native boolean nInputScalarN(String str, short[] sArr, int i, short s);

    private static native boolean nInputScalarN(String str, short[] sArr, int i, short s, short s2);

    private static native boolean nInputScalarN(String str, short[] sArr, int i, short s, short s2, String str2);

    private static native boolean nInputScalarN(String str, short[] sArr, int i, short s, short s2, String str2, int i2);

    private static native boolean nInputScalarN(String str, int[] iArr, int i);

    private static native boolean nInputScalarN(String str, int[] iArr, int i, int i2);

    private static native boolean nInputScalarN(String str, int[] iArr, int i, int i2, int i3);

    private static native boolean nInputScalarN(String str, int[] iArr, int i, int i2, int i3, String str2);

    private static native boolean nInputScalarN(String str, int[] iArr, int i, int i2, int i3, String str2, int i4);

    private static native boolean nInputScalarN(String str, long[] jArr, int i);

    private static native boolean nInputScalarN(String str, long[] jArr, int i, long j);

    private static native boolean nInputScalarN(String str, long[] jArr, int i, long j, long j2);

    private static native boolean nInputScalarN(String str, long[] jArr, int i, long j, long j2, String str2);

    private static native boolean nInputScalarN(String str, long[] jArr, int i, long j, long j2, String str2, int i2);

    private static native boolean nInputScalarN(String str, float[] fArr, int i);

    private static native boolean nInputScalarN(String str, float[] fArr, int i, float f);

    private static native boolean nInputScalarN(String str, float[] fArr, int i, float f, float f2);

    private static native boolean nInputScalarN(String str, float[] fArr, int i, float f, float f2, String str2);

    private static native boolean nInputScalarN(String str, float[] fArr, int i, float f, float f2, String str2, int i2);

    private static native boolean nInputScalarN(String str, double[] dArr, int i);

    private static native boolean nInputScalarN(String str, double[] dArr, int i, double d);

    private static native boolean nInputScalarN(String str, double[] dArr, int i, double d, double d2);

    private static native boolean nInputScalarN(String str, double[] dArr, int i, double d, double d2, String str2);

    private static native boolean nInputScalarN(String str, double[] dArr, int i, double d, double d2, String str2, int i2);

    private static native boolean nInputScalarN(String str, int i, short[] sArr, int i2);

    private static native boolean nInputScalarN(String str, int i, short[] sArr, int i2, short s);

    private static native boolean nInputScalarN(String str, int i, short[] sArr, int i2, short s, short s2);

    private static native boolean nInputScalarN(String str, int i, short[] sArr, int i2, short s, short s2, String str2);

    private static native boolean nInputScalarN(String str, int i, short[] sArr, int i2, short s, short s2, String str2, int i3);

    private static native boolean nInputScalarN(String str, int i, int[] iArr, int i2);

    private static native boolean nInputScalarN(String str, int i, int[] iArr, int i2, int i3);

    private static native boolean nInputScalarN(String str, int i, int[] iArr, int i2, int i3, int i4);

    private static native boolean nInputScalarN(String str, int i, int[] iArr, int i2, int i3, int i4, String str2);

    private static native boolean nInputScalarN(String str, int i, int[] iArr, int i2, int i3, int i4, String str2, int i5);

    private static native boolean nInputScalarN(String str, int i, long[] jArr, int i2);

    private static native boolean nInputScalarN(String str, int i, long[] jArr, int i2, long j);

    private static native boolean nInputScalarN(String str, int i, long[] jArr, int i2, long j, long j2);

    private static native boolean nInputScalarN(String str, int i, long[] jArr, int i2, long j, long j2, String str2);

    private static native boolean nInputScalarN(String str, int i, long[] jArr, int i2, long j, long j2, String str2, int i3);

    private static native boolean nInputScalarN(String str, int i, float[] fArr, int i2);

    private static native boolean nInputScalarN(String str, int i, float[] fArr, int i2, float f);

    private static native boolean nInputScalarN(String str, int i, float[] fArr, int i2, float f, float f2);

    private static native boolean nInputScalarN(String str, int i, float[] fArr, int i2, float f, float f2, String str2);

    private static native boolean nInputScalarN(String str, int i, float[] fArr, int i2, float f, float f2, String str2, int i3);

    private static native boolean nInputScalarN(String str, int i, double[] dArr, int i2);

    private static native boolean nInputScalarN(String str, int i, double[] dArr, int i2, double d);

    private static native boolean nInputScalarN(String str, int i, double[] dArr, int i2, double d, double d2);

    private static native boolean nInputScalarN(String str, int i, double[] dArr, int i2, double d, double d2, String str2);

    private static native boolean nInputScalarN(String str, int i, double[] dArr, int i2, double d, double d2, String str2, int i3);

    private static native boolean nColorEdit3(String str, float[] fArr);

    private static native boolean nColorEdit3(String str, float[] fArr, int i);

    private static native boolean nColorEdit4(String str, float[] fArr);

    private static native boolean nColorEdit4(String str, float[] fArr, int i);

    private static native boolean nColorPicker3(String str, float[] fArr);

    private static native boolean nColorPicker3(String str, float[] fArr, int i);

    private static native boolean nColorPicker4(String str, float[] fArr);

    private static native boolean nColorPicker4(String str, float[] fArr, int i);

    private static native boolean nColorPicker4(String str, float[] fArr, int i, float[] fArr2);

    private static native boolean nColorPicker4(String str, float[] fArr, float[] fArr2);

    private static native boolean nColorButton(String str, float f, float f2, float f3, float f4);

    private static native boolean nColorButton(String str, float f, float f2, float f3, float f4, int i);

    private static native boolean nColorButton(String str, float f, float f2, float f3, float f4, int i, float f5, float f6);

    private static native boolean nColorButton(String str, float f, float f2, float f3, float f4, float f5, float f6);

    private static native boolean nColorButton(String str, float[] fArr);

    private static native boolean nColorButton(String str, float[] fArr, int i);

    private static native boolean nColorButton(String str, float[] fArr, int i, float f, float f2);

    private static native boolean nColorButton(String str, float[] fArr, float f, float f2);

    private static native void nSetColorEditOptions(int i);

    private static native boolean nTreeNode(String str);

    private static native boolean nTreeNode(String str, String str2);

    private static native boolean nTreeNode(long j, String str);

    private static native boolean nTreeNodeEx(String str);

    private static native boolean nTreeNodeEx(String str, int i);

    private static native boolean nTreeNodeEx(String str, int i, String str2);

    private static native boolean nTreeNodeEx(long j, int i, String str);

    private static native void nTreePush(String str);

    private static native void nTreePush(long j);

    private static native void nTreePop();

    private static native float nGetTreeNodeToLabelSpacing();

    private static native boolean nCollapsingHeader(String str);

    private static native boolean nCollapsingHeader(String str, int i);

    private static native boolean nCollapsingHeader(String str, boolean[] zArr);

    private static native boolean nCollapsingHeader(String str, boolean[] zArr, int i);

    private static native void nSetNextItemOpen(boolean z);

    private static native void nSetNextItemOpen(boolean z, int i);

    private static native boolean nSelectable(String str);

    private static native boolean nSelectable(String str, boolean z);

    private static native boolean nSelectable(String str, boolean z, int i);

    private static native boolean nSelectable(String str, boolean z, int i, float f, float f2);

    private static native boolean nSelectable(String str, int i, float f, float f2);

    private static native boolean nSelectable(String str, float f, float f2);

    private static native boolean nSelectable(String str, boolean z, float f, float f2);

    private static native boolean nSelectable(String str, boolean[] zArr);

    private static native boolean nSelectable(String str, boolean[] zArr, int i);

    private static native boolean nSelectable(String str, boolean[] zArr, int i, float f, float f2);

    private static native boolean nSelectable(String str, boolean[] zArr, float f, float f2);

    private static native boolean nBeginListBox(String str);

    private static native boolean nBeginListBox(String str, float f, float f2);

    private static native void nEndListBox();

    private static native void nListBox(String str, int[] iArr, String[] strArr, int i);

    private static native void nListBox(String str, int[] iArr, String[] strArr, int i, int i2);

    private static native void nPlotLines(String str, float[] fArr, int i);

    private static native void nPlotLines(String str, float[] fArr, int i, int i2);

    private static native void nPlotLines(String str, float[] fArr, int i, int i2, String str2);

    private static native void nPlotLines(String str, float[] fArr, int i, int i2, String str2, float f);

    private static native void nPlotLines(String str, float[] fArr, int i, int i2, String str2, float f, float f2);

    private static native void nPlotLines(String str, float[] fArr, int i, int i2, String str2, float f, float f2, float f3, float f4);

    private static native void nPlotLines(String str, float[] fArr, int i, int i2, String str2, float f, float f2, float f3, float f4, int i3);

    private static native void nPlotLines(String str, float[] fArr, int i, String str2, float f, float f2, float f3, float f4, int i2);

    private static native void nPlotLines(String str, float[] fArr, int i, float f, float f2, float f3, float f4, int i2);

    private static native void nPlotLines(String str, float[] fArr, int i, int i2, float f, float f2, float f3, float f4, int i3);

    private static native void nPlotLines(String str, float[] fArr, int i, int i2, String str2, float f, float f2, int i3);

    private static native void nPlotHistogram(String str, float[] fArr, int i);

    private static native void nPlotHistogram(String str, float[] fArr, int i, int i2);

    private static native void nPlotHistogram(String str, float[] fArr, int i, int i2, String str2);

    private static native void nPlotHistogram(String str, float[] fArr, int i, int i2, String str2, float f);

    private static native void nPlotHistogram(String str, float[] fArr, int i, int i2, String str2, float f, float f2);

    private static native void nPlotHistogram(String str, float[] fArr, int i, int i2, String str2, float f, float f2, float f3, float f4);

    private static native void nPlotHistogram(String str, float[] fArr, int i, int i2, String str2, float f, float f2, float f3, float f4, int i3);

    private static native void nPlotHistogram(String str, float[] fArr, int i, String str2, float f, float f2, float f3, float f4, int i2);

    private static native void nPlotHistogram(String str, float[] fArr, int i, float f, float f2, float f3, float f4, int i2);

    private static native void nPlotHistogram(String str, float[] fArr, int i, int i2, float f, float f2, float f3, float f4, int i3);

    private static native void nPlotHistogram(String str, float[] fArr, int i, int i2, String str2, float f, float f2, int i3);

    private static native void nValue(String str, String str2);

    private static native boolean nBeginMenuBar();

    private static native void nEndMenuBar();

    private static native boolean nBeginMainMenuBar();

    private static native void nEndMainMenuBar();

    private static native boolean nBeginMenu(String str);

    private static native boolean nBeginMenu(String str, boolean z);

    private static native void nEndMenu();

    private static native boolean nMenuItem(String str);

    private static native boolean nMenuItem(String str, boolean z);

    private static native boolean nMenuItem(String str, boolean z, boolean z2);

    private static native boolean nMenuItem(String str, String str2);

    private static native boolean nMenuItem(String str, String str2, boolean z);

    private static native boolean nMenuItem(String str, String str2, boolean z, boolean z2);

    private static native boolean nMenuItem(String str, String str2, boolean[] zArr);

    private static native boolean nMenuItem(String str, String str2, boolean[] zArr, boolean z);

    private static native void nBeginTooltip();

    private static native void nEndTooltip();

    private static native void nSetTooltip(String str);

    private static native boolean nBeginItemTooltip();

    private static native void nSetItemTooltip(String str);

    private static native boolean nBeginPopup(String str);

    private static native boolean nBeginPopup(String str, int i);

    private static native boolean nBeginPopupModal(String str);

    private static native boolean nBeginPopupModal(String str, boolean[] zArr);

    private static native boolean nBeginPopupModal(String str, boolean[] zArr, int i);

    private static native boolean nBeginPopupModal(String str, int i);

    private static native void nEndPopup();

    private static native void nOpenPopup(String str);

    private static native void nOpenPopup(String str, int i);

    private static native void nOpenPopup(int i);

    private static native void nOpenPopup(int i, int i2);

    private static native void nOpenPopupOnItemClick();

    private static native void nOpenPopupOnItemClick(String str);

    private static native void nOpenPopupOnItemClick(String str, int i);

    private static native void nOpenPopupOnItemClick(int i);

    private static native void nCloseCurrentPopup();

    private static native boolean nBeginPopupContextItem();

    private static native boolean nBeginPopupContextItem(String str);

    private static native boolean nBeginPopupContextItem(String str, int i);

    private static native boolean nBeginPopupContextItem(int i);

    private static native boolean nBeginPopupContextWindow();

    private static native boolean nBeginPopupContextWindow(String str);

    private static native boolean nBeginPopupContextWindow(String str, int i);

    private static native boolean nBeginPopupContextWindow(int i);

    private static native boolean nBeginPopupContextVoid();

    private static native boolean nBeginPopupContextVoid(String str);

    private static native boolean nBeginPopupContextVoid(String str, int i);

    private static native boolean nBeginPopupContextVoid(int i);

    private static native boolean nIsPopupOpen(String str);

    private static native boolean nIsPopupOpen(String str, int i);

    private static native boolean nBeginTable(String str, int i);

    private static native boolean nBeginTable(String str, int i, int i2);

    private static native boolean nBeginTable(String str, int i, int i2, float f, float f2);

    private static native boolean nBeginTable(String str, int i, int i2, float f, float f2, float f3);

    private static native boolean nBeginTable(String str, int i, float f, float f2, float f3);

    private static native boolean nBeginTable(String str, int i, float f);

    private static native boolean nBeginTable(String str, int i, int i2, float f);

    private static native void nEndTable();

    private static native void nTableNextRow();

    private static native void nTableNextRow(int i);

    private static native void nTableNextRow(int i, float f);

    private static native void nTableNextRow(float f);

    private static native boolean nTableNextColumn();

    private static native boolean nTableSetColumnIndex(int i);

    private static native void nTableSetupColumn(String str);

    private static native void nTableSetupColumn(String str, int i);

    private static native void nTableSetupColumn(String str, int i, float f);

    private static native void nTableSetupColumn(String str, int i, float f, int i2);

    private static native void nTableSetupColumn(String str, float f, int i);

    private static native void nTableSetupColumn(String str, int i, int i2);

    private static native void nTableSetupScrollFreeze(int i, int i2);

    private static native void nTableHeader(String str);

    private static native void nTableHeadersRow();

    private static native void nTableAngledHeadersRow();

    private static native long nTableGetSortSpecs();

    private static native int nTableGetColumnCount();

    private static native int nTableGetColumnIndex();

    private static native int nTableGetRowIndex();

    private static native String nTableGetColumnName();

    private static native String nTableGetColumnName(int i);

    private static native int nTableGetColumnFlags();

    private static native int nTableGetColumnFlags(int i);

    private static native void nTableSetColumnEnabled(int i, boolean z);

    private static native int nTableGetHoveredColumn();

    private static native void nTableSetBgColor(int i, int i2);

    private static native void nTableSetBgColor(int i, int i2, int i3);

    private static native void nColumns();

    private static native void nColumns(int i);

    private static native void nColumns(int i, String str);

    private static native void nColumns(int i, String str, boolean z);

    private static native void nColumns(String str, boolean z);

    private static native void nColumns(boolean z);

    private static native void nColumns(int i, boolean z);

    private static native void nNextColumn();

    private static native int nGetColumnIndex();

    private static native float nGetColumnWidth();

    private static native float nGetColumnWidth(int i);

    private static native void nSetColumnWidth(int i, float f);

    private static native float nGetColumnOffset();

    private static native float nGetColumnOffset(int i);

    private static native void nSetColumnOffset(int i, float f);

    private static native int nGetColumnsCount();

    private static native boolean nBeginTabBar(String str);

    private static native boolean nBeginTabBar(String str, int i);

    private static native void nEndTabBar();

    private static native boolean nBeginTabItem(String str);

    private static native boolean nBeginTabItem(String str, boolean[] zArr);

    private static native boolean nBeginTabItem(String str, boolean[] zArr, int i);

    private static native boolean nBeginTabItem(String str, int i);

    private static native void nEndTabItem();

    private static native boolean nTabItemButton(String str);

    private static native boolean nTabItemButton(String str, int i);

    private static native void nSetTabItemClosed(String str);

    private static native int nDockSpace(int i);

    private static native int nDockSpace(int i, float f, float f2);

    private static native int nDockSpace(int i, float f, float f2, int i2);

    private static native int nDockSpace(int i, float f, float f2, int i2, long j);

    private static native int nDockSpace(int i, int i2, long j);

    private static native int nDockSpace(int i, long j);

    private static native int nDockSpace(int i, float f, float f2, long j);

    private static native int nDockSpaceOverViewport();

    private static native int nDockSpaceOverViewport(int i);

    private static native int nDockSpaceOverViewport(int i, long j);

    private static native int nDockSpaceOverViewport(int i, long j, int i2);

    private static native int nDockSpaceOverViewport(int i, long j, int i2, long j2);

    private static native int nDockSpaceOverViewport(long j, int i, long j2);

    private static native int nDockSpaceOverViewport(int i, long j, long j2);

    private static native void nSetNextWindowDockID(int i);

    private static native void nSetNextWindowDockID(int i, int i2);

    private static native void nSetNextWindowClass(long j);

    private static native int nGetWindowDockID();

    private static native boolean nIsWindowDocked();

    private static native void nLogToTTY();

    private static native void nLogToTTY(int i);

    private static native void nLogToFile();

    private static native void nLogToFile(int i);

    private static native void nLogToFile(int i, String str);

    private static native void nLogToFile(String str);

    private static native void nLogToClipboard();

    private static native void nLogToClipboard(int i);

    private static native void nLogFinish();

    private static native void nLogButtons();

    private static native void nLogText(String str);

    private static native boolean nBeginDragDropSource();

    private static native boolean nBeginDragDropSource(int i);

    private static native boolean nSetDragDropPayload(String str, byte[] bArr, int i, int i2);

    private static native void nEndDragDropSource();

    private static native boolean nBeginDragDropTarget();

    private static native boolean nAcceptDragDropPayload(String str, int i);

    private static native void nEndDragDropTarget();

    private static native boolean nHasDragDropPayload();

    private static native boolean nHasDragDropPayload(String str);

    private static native void nBeginDisabled();

    private static native void nBeginDisabled(boolean z);

    private static native void nEndDisabled();

    private static native void nPushClipRect(float f, float f2, float f3, float f4, boolean z);

    private static native void nPopClipRect();

    private static native void nSetItemDefaultFocus();

    private static native void nSetKeyboardFocusHere();

    private static native void nSetKeyboardFocusHere(int i);

    private static native void nSetNextItemAllowOverlap();

    private static native boolean nIsItemHovered();

    private static native boolean nIsItemHovered(int i);

    private static native boolean nIsItemActive();

    private static native boolean nIsItemFocused();

    private static native boolean nIsItemClicked();

    private static native boolean nIsItemClicked(int i);

    private static native boolean nIsItemVisible();

    private static native boolean nIsItemEdited();

    private static native boolean nIsItemActivated();

    private static native boolean nIsItemDeactivated();

    private static native boolean nIsItemDeactivatedAfterEdit();

    private static native boolean nIsItemToggledOpen();

    private static native boolean nIsAnyItemHovered();

    private static native boolean nIsAnyItemActive();

    private static native boolean nIsAnyItemFocused();

    private static native int nGetItemID();

    private static native void nGetItemRectMin(ImVec2 imVec2);

    private static native float nGetItemRectMinX();

    private static native float nGetItemRectMinY();

    private static native void nGetItemRectMax(ImVec2 imVec2);

    private static native float nGetItemRectMaxX();

    private static native float nGetItemRectMaxY();

    private static native void nGetItemRectSize(ImVec2 imVec2);

    private static native float nGetItemRectSizeX();

    private static native float nGetItemRectSizeY();

    private static native long nGetMainViewport();

    private static native long nGetBackgroundDrawList();

    private static native long nGetBackgroundDrawList(long j);

    private static native long nGetForegroundDrawList();

    private static native long nGetForegroundDrawList(long j);

    private static native boolean nIsRectVisible(float f, float f2);

    private static native boolean nIsRectVisible(float f, float f2, float f3, float f4);

    private static native double nGetTime();

    private static native int nGetFrameCount();

    private static native String nGetStyleColorName(int i);

    private static native void nSetStateStorage(long j);

    private static native long nGetStateStorage();

    private static native void nCalcTextSize(ImVec2 imVec2, String str);

    private static native float nCalcTextSizeX(String str);

    private static native float nCalcTextSizeY(String str);

    private static native void nCalcTextSize(ImVec2 imVec2, String str, boolean z);

    private static native float nCalcTextSizeX(String str, boolean z);

    private static native float nCalcTextSizeY(String str, boolean z);

    private static native void nCalcTextSize(ImVec2 imVec2, String str, boolean z, float f);

    private static native float nCalcTextSizeX(String str, boolean z, float f);

    private static native float nCalcTextSizeY(String str, boolean z, float f);

    private static native void nCalcTextSize(ImVec2 imVec2, String str, float f);

    private static native float nCalcTextSizeX(String str, float f);

    private static native float nCalcTextSizeY(String str, float f);

    private static native void nColorConvertU32ToFloat4(ImVec4 imVec4, int i);

    private static native float nColorConvertU32ToFloat4X(int i);

    private static native float nColorConvertU32ToFloat4Y(int i);

    private static native float nColorConvertU32ToFloat4Z(int i);

    private static native float nColorConvertU32ToFloat4W(int i);

    private static native int nColorConvertFloat4ToU32(float f, float f2, float f3, float f4);

    public static native void nColorConvertRGBtoHSV(float[] fArr, float[] fArr2);

    public static native void nColorConvertHSVtoRGB(float[] fArr, float[] fArr2);

    private static native boolean nIsKeyDown(int i);

    private static native boolean nIsKeyPressed(int i);

    private static native boolean nIsKeyPressed(int i, boolean z);

    private static native boolean nIsKeyReleased(int i);

    private static native boolean nIsKeyChordPressed(int i);

    private static native boolean nGetKeyPressedAmount(int i, float f, float f2);

    private static native String nGetKeyName(int i);

    private static native void nSetNextFrameWantCaptureKeyboard(boolean z);

    private static native boolean nShortcut(int i);

    private static native boolean nShortcut(int i, int i2);

    private static native void nSetNextItemShortcut(int i);

    private static native void nSetNextItemShortcut(int i, int i2);

    private static native boolean nIsMouseDown(int i);

    private static native boolean nIsMouseClicked(int i);

    private static native boolean nIsMouseClicked(int i, boolean z);

    private static native boolean nIsMouseReleased(int i);

    private static native boolean nIsMouseDoubleClicked(int i);

    private static native int nGetMouseClickedCount(int i);

    private static native boolean nIsMouseHoveringRect(float f, float f2, float f3, float f4);

    private static native boolean nIsMouseHoveringRect(float f, float f2, float f3, float f4, boolean z);

    private static native boolean nIsMousePosValid();

    private static native boolean nIsMousePosValid(float f, float f2);

    private static native boolean nIsAnyMouseDown();

    private static native void nGetMousePos(ImVec2 imVec2);

    private static native float nGetMousePosX();

    private static native float nGetMousePosY();

    private static native void nGetMousePosOnOpeningCurrentPopup(ImVec2 imVec2);

    private static native float nGetMousePosOnOpeningCurrentPopupX();

    private static native float nGetMousePosOnOpeningCurrentPopupY();

    private static native boolean nIsMouseDragging(int i);

    private static native boolean nIsMouseDragging(int i, float f);

    private static native void nGetMouseDragDelta(ImVec2 imVec2);

    private static native float nGetMouseDragDeltaX();

    private static native float nGetMouseDragDeltaY();

    private static native void nGetMouseDragDelta(ImVec2 imVec2, int i);

    private static native float nGetMouseDragDeltaX(int i);

    private static native float nGetMouseDragDeltaY(int i);

    private static native void nGetMouseDragDelta(ImVec2 imVec2, int i, float f);

    private static native float nGetMouseDragDeltaX(int i, float f);

    private static native float nGetMouseDragDeltaY(int i, float f);

    private static native void nResetMouseDragDelta();

    private static native void nResetMouseDragDelta(int i);

    private static native int nGetMouseCursor();

    private static native void nSetMouseCursor(int i);

    private static native void nSetNextFrameWantCaptureMouse(boolean z);

    private static native String nGetClipboardText();

    private static native void nSetClipboardText(String str);

    private static native void nLoadIniSettingsFromDisk(String str);

    private static native void nLoadIniSettingsFromMemory(String str);

    private static native void nLoadIniSettingsFromMemory(String str, int i);

    private static native void nSaveIniSettingsToDisk(String str);

    private static native String nSaveIniSettingsToMemory();

    private static native String nSaveIniSettingsToMemory(long j);

    private static native void nDebugTextEncoding(String str);

    private static native void nDebugFlashStyleColor(int i);

    private static native void nDebugStartItemPicker();

    private static native boolean nDebugCheckVersionAndDataLayout(String str, int i, int i2, int i3, int i4, int i5, int i6);

    private static native long nGetPlatformIO();

    private static native void nUpdatePlatformWindows();

    private static native void nRenderPlatformWindowsDefault();

    private static native void nDestroyPlatformWindows();

    private static native long nFindViewportByID(int i);

    private static native long nFindViewportByPlatformHandle(long j);

    static {
        String libPath = System.getProperty(LIB_PATH_PROP);
        String libName = System.getProperty(LIB_NAME_PROP, LIB_NAME_DEFAULT);
        String fullLibName = resolveFullLibName();
        if (libPath != null) {
            System.load(Paths.get(libPath, new String[0]).resolve(fullLibName).toAbsolutePath().toString());
        } else {
            try {
                System.loadLibrary(libName);
            } catch (Error | Exception e) {
                String extractedLibAbsPath = tryLoadFromClasspath(fullLibName);
                if (extractedLibAbsPath != null) {
                    System.load(extractedLibAbsPath);
                } else {
                    throw e;
                }
            }
        }
        nInitJni();
        ImFontAtlas.nInit();
        ImGuiPlatformIO.init();
        nInitInputTextData();
        setAssertCallback(new ImAssertCallback() { // from class: imgui.ImGui.1
            @Override // imgui.assertion.ImAssertCallback
            public void imAssertCallback(String assertion, int line, String file) {
                System.err.println("Dear ImGui Assertion Failed: " + assertion);
                System.err.println("Assertion Located At: " + file + ":" + line);
                Thread.dumpStack();
            }
        });
        _GETIO_1 = new ImGuiIO(0L);
        _GETSTYLE_1 = new ImGuiStyle(0L);
        _GETDRAWDATA_1 = new ImDrawData(0L);
        _GETFONT_1 = new ImFont(0L);
        payloadRef = null;
        PAYLOAD_PLACEHOLDER_DATA = new byte[1];
        _GETMAINVIEWPORT_1 = new ImGuiViewport(0L);
        _GETPLATFORMIO_1 = new ImGuiPlatformIO(0L);
    }

    private static String resolveFullLibName() {
        String libPrefix;
        String libSuffix;
        boolean isWin = System.getProperty("os.name").toLowerCase().contains("win");
        boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");
        if (isWin) {
            libPrefix = "";
            libSuffix = ".dll";
        } else if (isMac) {
            libPrefix = "lib";
            libSuffix = ".dylib";
        } else {
            libPrefix = "lib";
            libSuffix = ".so";
        }
        return System.getProperty(LIB_NAME_PROP, libPrefix + LIB_NAME_DEFAULT + libSuffix);
    }

    /* JADX WARN: Not initialized variable reg: 8, insn: 0x00e6: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r8 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('is' java.io.InputStream)]) A[TRY_LEAVE], block:B:39:0x00e6 */
    /* JADX WARN: Not initialized variable reg: 9, insn: 0x00ea: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r9 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:41:0x00ea */
    private static String tryLoadFromClasspath(String fullLibName) {
        InputStream is;
        Throwable th;
        try {
            try {
                InputStream is2 = ImGui.class.getClassLoader().getResourceAsStream("io/imgui/java/native-bin/" + fullLibName);
                Throwable th2 = null;
                if (is2 != null) {
                    String version = getVersionString().orElse("undefined");
                    Path tmpDir = Paths.get(System.getProperty("java.io.tmpdir"), new String[0]).resolve(LIB_TMP_DIR_PREFIX).resolve(version);
                    if (!Files.exists(tmpDir, new LinkOption[0])) {
                        Files.createDirectories(tmpDir, new FileAttribute[0]);
                    }
                    Path libBin = tmpDir.resolve(fullLibName);
                    try {
                        Files.copy(is2, libBin, StandardCopyOption.REPLACE_EXISTING);
                    } catch (AccessDeniedException e) {
                        if (!Files.exists(libBin, new LinkOption[0])) {
                            throw e;
                        }
                    }
                    String string = libBin.toAbsolutePath().toString();
                    if (is2 != null) {
                        if (0 != 0) {
                            try {
                                is2.close();
                            } catch (Throwable th3) {
                                th2.addSuppressed(th3);
                            }
                        } else {
                            is2.close();
                        }
                    }
                    return string;
                }
                if (is2 != null) {
                    if (0 != 0) {
                        try {
                            is2.close();
                        } catch (Throwable th4) {
                            th2.addSuppressed(th4);
                        }
                    } else {
                        is2.close();
                    }
                }
                return null;
            } catch (Throwable th5) {
                if (is != null) {
                    if (th != null) {
                        try {
                            is.close();
                        } catch (Throwable th6) {
                            th.addSuppressed(th6);
                        }
                    } else {
                        is.close();
                    }
                }
                throw th5;
            }
        } catch (IOException e2) {
            throw new UncheckedIOException(e2);
        }
        throw new UncheckedIOException(e2);
    }

    /* JADX WARN: Not initialized variable reg: 5, insn: 0x006e: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r5 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('is' java.io.InputStream)]) A[TRY_LEAVE], block:B:28:0x006e */
    /* JADX WARN: Not initialized variable reg: 6, insn: 0x0072: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r6 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:30:0x0072 */
    private static Optional<String> getVersionString() {
        InputStream is;
        Throwable th;
        Properties properties = new Properties();
        try {
            try {
                InputStream is2 = ImGui.class.getResourceAsStream("/imgui/imgui-java.properties");
                Throwable th2 = null;
                if (is2 == null) {
                    if (is2 != null) {
                        if (0 != 0) {
                            try {
                                is2.close();
                            } catch (Throwable th3) {
                                th2.addSuppressed(th3);
                            }
                        } else {
                            is2.close();
                        }
                    }
                    return Optional.empty();
                }
                properties.load(is2);
                Optional<String> optionalOf = Optional.of(properties.get("imgui.java.version").toString());
                if (is2 != null) {
                    if (0 != 0) {
                        try {
                            is2.close();
                        } catch (Throwable th4) {
                            th2.addSuppressed(th4);
                        }
                    } else {
                        is2.close();
                    }
                }
                return optionalOf;
            } catch (Throwable th5) {
                if (is != null) {
                    if (th != null) {
                        try {
                            is.close();
                        } catch (Throwable th6) {
                            th.addSuppressed(th6);
                        }
                    } else {
                        is.close();
                    }
                }
                throw th5;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        throw new UncheckedIOException(e);
    }

    public static void init() {
    }

    public static ImGuiContext createContext() {
        return new ImGuiContext(nCreateContext());
    }

    public static ImGuiContext createContext(ImFontAtlas sharedFontAtlas) {
        return new ImGuiContext(nCreateContext(sharedFontAtlas.ptr));
    }

    public static void destroyContext() {
        nDestroyContext();
    }

    public static void destroyContext(ImGuiContext ctx) {
        nDestroyContext(ctx.ptr);
    }

    public static ImGuiContext getCurrentContext() {
        return new ImGuiContext(nGetCurrentContext());
    }

    public static void setCurrentContext(ImGuiContext ctx) {
        nSetCurrentContext(ctx.ptr);
    }

    public static ImGuiIO getIO() {
        _GETIO_1.ptr = nGetIO();
        return _GETIO_1;
    }

    public static ImGuiStyle getStyle() {
        _GETSTYLE_1.ptr = nGetStyle();
        return _GETSTYLE_1;
    }

    public static void newFrame() {
        nNewFrame();
    }

    public static void endFrame() {
        nEndFrame();
    }

    public static void render() {
        nRender();
    }

    public static ImDrawData getDrawData() {
        _GETDRAWDATA_1.ptr = nGetDrawData();
        return _GETDRAWDATA_1;
    }

    public static void showDemoWindow() {
        nShowDemoWindow();
    }

    public static void showDemoWindow(ImBoolean pOpen) {
        nShowDemoWindow(pOpen != null ? pOpen.getData() : null);
    }

    public static void showMetricsWindow() {
        nShowMetricsWindow();
    }

    public static void showMetricsWindow(ImBoolean pOpen) {
        nShowMetricsWindow(pOpen != null ? pOpen.getData() : null);
    }

    public static void showDebugLogWindow() {
        nShowDebugLogWindow();
    }

    public static void showDebugLogWindow(ImBoolean pOpen) {
        nShowDebugLogWindow(pOpen != null ? pOpen.getData() : null);
    }

    public static void showIDStackToolWindow() {
        nShowIDStackToolWindow();
    }

    public static void showIDStackToolWindow(ImBoolean pOpen) {
        nShowIDStackToolWindow(pOpen != null ? pOpen.getData() : null);
    }

    public static void showAboutWindow() {
        nShowAboutWindow();
    }

    public static void showAboutWindow(ImBoolean pOpen) {
        nShowAboutWindow(pOpen != null ? pOpen.getData() : null);
    }

    public static void showStyleEditor() {
        nShowStyleEditor();
    }

    public static void showStyleEditor(ImGuiStyle ref) {
        nShowStyleEditor(ref.ptr);
    }

    public static boolean showStyleSelector(String label) {
        return nShowStyleSelector(label);
    }

    public static void showFontSelector(String label) {
        nShowFontSelector(label);
    }

    public static void showUserGuide() {
        nShowUserGuide();
    }

    public static String getVersion() {
        return nGetVersion();
    }

    public static void styleColorsDark() {
        nStyleColorsDark();
    }

    public static void styleColorsDark(ImGuiStyle style) {
        nStyleColorsDark(style.ptr);
    }

    public static void styleColorsLight() {
        nStyleColorsLight();
    }

    public static void styleColorsLight(ImGuiStyle style) {
        nStyleColorsLight(style.ptr);
    }

    public static void styleColorsClassic() {
        nStyleColorsClassic();
    }

    public static void styleColorsClassic(ImGuiStyle style) {
        nStyleColorsClassic(style.ptr);
    }

    public static boolean begin(String title) {
        return nBegin(title);
    }

    public static boolean begin(String title, ImBoolean pOpen) {
        return nBegin(title, pOpen != null ? pOpen.getData() : null);
    }

    public static boolean begin(String title, ImBoolean pOpen, int imGuiWindowFlags) {
        return nBegin(title, pOpen != null ? pOpen.getData() : null, imGuiWindowFlags);
    }

    public static boolean begin(String title, int imGuiWindowFlags) {
        return nBegin(title, imGuiWindowFlags);
    }

    public static void end() {
        nEnd();
    }

    public static boolean beginChild(String strId) {
        return nBeginChild(strId);
    }

    public static boolean beginChild(String strId, ImVec2 size) {
        return nBeginChild(strId, size.x, size.y);
    }

    public static boolean beginChild(String strId, float sizeX, float sizeY) {
        return nBeginChild(strId, sizeX, sizeY);
    }

    public static boolean beginChild(String strId, ImVec2 size, int childFlags) {
        return nBeginChild(strId, size.x, size.y, childFlags);
    }

    public static boolean beginChild(String strId, float sizeX, float sizeY, int childFlags) {
        return nBeginChild(strId, sizeX, sizeY, childFlags);
    }

    public static boolean beginChild(String strId, ImVec2 size, int childFlags, int windowFlags) {
        return nBeginChild(strId, size.x, size.y, childFlags, windowFlags);
    }

    public static boolean beginChild(String strId, float sizeX, float sizeY, int childFlags, int windowFlags) {
        return nBeginChild(strId, sizeX, sizeY, childFlags, windowFlags);
    }

    public static boolean beginChild(String strId, int childFlags, int windowFlags) {
        return nBeginChild(strId, childFlags, windowFlags);
    }

    public static boolean beginChild(String strId, int windowFlags) {
        return nBeginChild(strId, windowFlags);
    }

    public static boolean beginChild(int id) {
        return nBeginChild(id);
    }

    public static boolean beginChild(int id, ImVec2 size) {
        return nBeginChild(id, size.x, size.y);
    }

    public static boolean beginChild(int id, float sizeX, float sizeY) {
        return nBeginChild(id, sizeX, sizeY);
    }

    public static boolean beginChild(int id, ImVec2 size, int childFlags) {
        return nBeginChild(id, size.x, size.y, childFlags);
    }

    public static boolean beginChild(int id, float sizeX, float sizeY, int childFlags) {
        return nBeginChild(id, sizeX, sizeY, childFlags);
    }

    public static boolean beginChild(int id, ImVec2 size, int childFlags, int windowFlags) {
        return nBeginChild(id, size.x, size.y, childFlags, windowFlags);
    }

    public static boolean beginChild(int id, float sizeX, float sizeY, int childFlags, int windowFlags) {
        return nBeginChild(id, sizeX, sizeY, childFlags, windowFlags);
    }

    public static boolean beginChild(int id, int childFlags, int windowFlags) {
        return nBeginChild(id, childFlags, windowFlags);
    }

    public static boolean beginChild(int id, int windowFlags) {
        return nBeginChild(id, windowFlags);
    }

    public static boolean beginChild(String strId, ImVec2 size, boolean border) {
        return nBeginChild(strId, size.x, size.y, border);
    }

    public static boolean beginChild(String strId, float sizeX, float sizeY, boolean border) {
        return nBeginChild(strId, sizeX, sizeY, border);
    }

    public static boolean beginChild(String strId, ImVec2 size, boolean border, int windowFlags) {
        return nBeginChild(strId, size.x, size.y, border, windowFlags);
    }

    public static boolean beginChild(String strId, float sizeX, float sizeY, boolean border, int windowFlags) {
        return nBeginChild(strId, sizeX, sizeY, border, windowFlags);
    }

    @Deprecated
    public static boolean beginChild(int id, ImVec2 size, boolean border) {
        return nBeginChild(id, size.x, size.y, border);
    }

    @Deprecated
    public static boolean beginChild(int id, float sizeX, float sizeY, boolean border) {
        return nBeginChild(id, sizeX, sizeY, border);
    }

    @Deprecated
    public static boolean beginChild(int id, ImVec2 size, boolean border, int windowFlags) {
        return nBeginChild(id, size.x, size.y, border, windowFlags);
    }

    @Deprecated
    public static boolean beginChild(int id, float sizeX, float sizeY, boolean border, int windowFlags) {
        return nBeginChild(id, sizeX, sizeY, border, windowFlags);
    }

    public static void endChild() {
        nEndChild();
    }

    public static boolean isWindowAppearing() {
        return nIsWindowAppearing();
    }

    public static boolean isWindowCollapsed() {
        return nIsWindowCollapsed();
    }

    public static boolean isWindowFocused() {
        return nIsWindowFocused();
    }

    public static boolean isWindowFocused(int imGuiFocusedFlags) {
        return nIsWindowFocused(imGuiFocusedFlags);
    }

    public static boolean isWindowHovered() {
        return nIsWindowHovered();
    }

    public static boolean isWindowHovered(int imGuiHoveredFlags) {
        return nIsWindowHovered(imGuiHoveredFlags);
    }

    public static ImDrawList getWindowDrawList() {
        return new ImDrawList(nGetWindowDrawList());
    }

    public static float getWindowDpiScale() {
        return nGetWindowDpiScale();
    }

    public static ImVec2 getWindowPos() {
        ImVec2 dst = new ImVec2();
        nGetWindowPos(dst);
        return dst;
    }

    public static float getWindowPosX() {
        return nGetWindowPosX();
    }

    public static float getWindowPosY() {
        return nGetWindowPosY();
    }

    public static void getWindowPos(ImVec2 dst) {
        nGetWindowPos(dst);
    }

    public static ImVec2 getWindowSize() {
        ImVec2 dst = new ImVec2();
        nGetWindowSize(dst);
        return dst;
    }

    public static float getWindowSizeX() {
        return nGetWindowSizeX();
    }

    public static float getWindowSizeY() {
        return nGetWindowSizeY();
    }

    public static void getWindowSize(ImVec2 dst) {
        nGetWindowSize(dst);
    }

    public static float getWindowWidth() {
        return nGetWindowWidth();
    }

    public static float getWindowHeight() {
        return nGetWindowHeight();
    }

    public static ImGuiViewport getWindowViewport() {
        return new ImGuiViewport(nGetWindowViewport());
    }

    public static void setNextWindowPos(ImVec2 pos) {
        nSetNextWindowPos(pos.x, pos.y);
    }

    public static void setNextWindowPos(float posX, float posY) {
        nSetNextWindowPos(posX, posY);
    }

    public static void setNextWindowPos(ImVec2 pos, int cond) {
        nSetNextWindowPos(pos.x, pos.y, cond);
    }

    public static void setNextWindowPos(float posX, float posY, int cond) {
        nSetNextWindowPos(posX, posY, cond);
    }

    public static void setNextWindowPos(ImVec2 pos, int cond, ImVec2 pivot) {
        nSetNextWindowPos(pos.x, pos.y, cond, pivot.x, pivot.y);
    }

    public static void setNextWindowPos(float posX, float posY, int cond, float pivotX, float pivotY) {
        nSetNextWindowPos(posX, posY, cond, pivotX, pivotY);
    }

    public static void setNextWindowPos(ImVec2 pos, ImVec2 pivot) {
        nSetNextWindowPos(pos.x, pos.y, pivot.x, pivot.y);
    }

    public static void setNextWindowPos(float posX, float posY, float pivotX, float pivotY) {
        nSetNextWindowPos(posX, posY, pivotX, pivotY);
    }

    public static void setNextWindowSize(ImVec2 size) {
        nSetNextWindowSize(size.x, size.y);
    }

    public static void setNextWindowSize(float sizeX, float sizeY) {
        nSetNextWindowSize(sizeX, sizeY);
    }

    public static void setNextWindowSize(ImVec2 size, int cond) {
        nSetNextWindowSize(size.x, size.y, cond);
    }

    public static void setNextWindowSize(float sizeX, float sizeY, int cond) {
        nSetNextWindowSize(sizeX, sizeY, cond);
    }

    public static void setNextWindowSizeConstraints(ImVec2 sizeMin, ImVec2 sizeMax) {
        nSetNextWindowSizeConstraints(sizeMin.x, sizeMin.y, sizeMax.x, sizeMax.y);
    }

    public static void setNextWindowSizeConstraints(float sizeMinX, float sizeMinY, float sizeMaxX, float sizeMaxY) {
        nSetNextWindowSizeConstraints(sizeMinX, sizeMinY, sizeMaxX, sizeMaxY);
    }

    public static void setNextWindowContentSize(ImVec2 size) {
        nSetNextWindowContentSize(size.x, size.y);
    }

    public static void setNextWindowContentSize(float sizeX, float sizeY) {
        nSetNextWindowContentSize(sizeX, sizeY);
    }

    public static void setNextWindowCollapsed(boolean collapsed) {
        nSetNextWindowCollapsed(collapsed);
    }

    public static void setNextWindowCollapsed(boolean collapsed, int cond) {
        nSetNextWindowCollapsed(collapsed, cond);
    }

    public static void setNextWindowFocus() {
        nSetNextWindowFocus();
    }

    public static void setNextWindowScroll(ImVec2 scroll) {
        nSetNextWindowScroll(scroll.x, scroll.y);
    }

    public static void setNextWindowScroll(float scrollX, float scrollY) {
        nSetNextWindowScroll(scrollX, scrollY);
    }

    public static void setNextWindowBgAlpha(float alpha) {
        nSetNextWindowBgAlpha(alpha);
    }

    public static void setNextWindowViewport(int viewportId) {
        nSetNextWindowViewport(viewportId);
    }

    public static void setWindowPos(ImVec2 pos) {
        nSetWindowPos(pos.x, pos.y);
    }

    public static void setWindowPos(float posX, float posY) {
        nSetWindowPos(posX, posY);
    }

    public static void setWindowPos(ImVec2 pos, int cond) {
        nSetWindowPos(pos.x, pos.y, cond);
    }

    public static void setWindowPos(float posX, float posY, int cond) {
        nSetWindowPos(posX, posY, cond);
    }

    public static void setWindowSize(ImVec2 size) {
        nSetWindowSize(size.x, size.y);
    }

    public static void setWindowSize(float sizeX, float sizeY) {
        nSetWindowSize(sizeX, sizeY);
    }

    public static void setWindowSize(ImVec2 size, int cond) {
        nSetWindowSize(size.x, size.y, cond);
    }

    public static void setWindowSize(float sizeX, float sizeY, int cond) {
        nSetWindowSize(sizeX, sizeY, cond);
    }

    public static void setWindowCollapsed(boolean collapsed) {
        nSetWindowCollapsed(collapsed);
    }

    public static void setWindowCollapsed(boolean collapsed, int cond) {
        nSetWindowCollapsed(collapsed, cond);
    }

    public static void setWindowFocus() {
        nSetWindowFocus();
    }

    public static void setWindowFontScale(float scale) {
        nSetWindowFontScale(scale);
    }

    public static void setWindowPos(String name, ImVec2 pos) {
        nSetWindowPos(name, pos.x, pos.y);
    }

    public static void setWindowPos(String name, float posX, float posY) {
        nSetWindowPos(name, posX, posY);
    }

    public static void setWindowPos(String name, ImVec2 pos, int cond) {
        nSetWindowPos(name, pos.x, pos.y, cond);
    }

    public static void setWindowPos(String name, float posX, float posY, int cond) {
        nSetWindowPos(name, posX, posY, cond);
    }

    public static void setWindowSize(String name, ImVec2 size) {
        nSetWindowSize(name, size.x, size.y);
    }

    public static void setWindowSize(String name, float sizeX, float sizeY) {
        nSetWindowSize(name, sizeX, sizeY);
    }

    public static void setWindowSize(String name, ImVec2 size, int cond) {
        nSetWindowSize(name, size.x, size.y, cond);
    }

    public static void setWindowSize(String name, float sizeX, float sizeY, int cond) {
        nSetWindowSize(name, sizeX, sizeY, cond);
    }

    public static void setWindowCollapsed(String name, boolean collapsed) {
        nSetWindowCollapsed(name, collapsed);
    }

    public static void setWindowCollapsed(String name, boolean collapsed, int cond) {
        nSetWindowCollapsed(name, collapsed, cond);
    }

    public static void setWindowFocus(String name) {
        nSetWindowFocus(name);
    }

    public static ImVec2 getContentRegionAvail() {
        ImVec2 dst = new ImVec2();
        nGetContentRegionAvail(dst);
        return dst;
    }

    public static float getContentRegionAvailX() {
        return nGetContentRegionAvailX();
    }

    public static float getContentRegionAvailY() {
        return nGetContentRegionAvailY();
    }

    public static void getContentRegionAvail(ImVec2 dst) {
        nGetContentRegionAvail(dst);
    }

    public static ImVec2 getContentRegionMax() {
        ImVec2 dst = new ImVec2();
        nGetContentRegionMax(dst);
        return dst;
    }

    public static float getContentRegionMaxX() {
        return nGetContentRegionMaxX();
    }

    public static float getContentRegionMaxY() {
        return nGetContentRegionMaxY();
    }

    public static void getContentRegionMax(ImVec2 dst) {
        nGetContentRegionMax(dst);
    }

    public static ImVec2 getWindowContentRegionMin() {
        ImVec2 dst = new ImVec2();
        nGetWindowContentRegionMin(dst);
        return dst;
    }

    public static float getWindowContentRegionMinX() {
        return nGetWindowContentRegionMinX();
    }

    public static float getWindowContentRegionMinY() {
        return nGetWindowContentRegionMinY();
    }

    public static void getWindowContentRegionMin(ImVec2 dst) {
        nGetWindowContentRegionMin(dst);
    }

    public static ImVec2 getWindowContentRegionMax() {
        ImVec2 dst = new ImVec2();
        nGetWindowContentRegionMax(dst);
        return dst;
    }

    public static float getWindowContentRegionMaxX() {
        return nGetWindowContentRegionMaxX();
    }

    public static float getWindowContentRegionMaxY() {
        return nGetWindowContentRegionMaxY();
    }

    public static void getWindowContentRegionMax(ImVec2 dst) {
        nGetWindowContentRegionMax(dst);
    }

    public static float getScrollX() {
        return nGetScrollX();
    }

    public static float getScrollY() {
        return nGetScrollY();
    }

    public static void setScrollX(float scrollX) {
        nSetScrollX(scrollX);
    }

    public static void setScrollY(float scrollY) {
        nSetScrollY(scrollY);
    }

    public static float getScrollMaxX() {
        return nGetScrollMaxX();
    }

    public static float getScrollMaxY() {
        return nGetScrollMaxY();
    }

    public static void setScrollHereX() {
        nSetScrollHereX();
    }

    public static void setScrollHereX(float centerXRatio) {
        nSetScrollHereX(centerXRatio);
    }

    public static void setScrollHereY() {
        nSetScrollHereY();
    }

    public static void setScrollHereY(float centerYRatio) {
        nSetScrollHereY(centerYRatio);
    }

    public static void setScrollFromPosX(float localX) {
        nSetScrollFromPosX(localX);
    }

    public static void setScrollFromPosX(float localX, float centerXRatio) {
        nSetScrollFromPosX(localX, centerXRatio);
    }

    public static void setScrollFromPosY(float localY) {
        nSetScrollFromPosY(localY);
    }

    public static void setScrollFromPosY(float localY, float centerYRatio) {
        nSetScrollFromPosY(localY, centerYRatio);
    }

    public static void pushFont(ImFont font) {
        nPushFont(font.ptr);
    }

    public static void popFont() {
        nPopFont();
    }

    public static void pushStyleColor(int imGuiCol, ImVec4 col) {
        nPushStyleColor(imGuiCol, col.x, col.y, col.z, col.w);
    }

    public static void pushStyleColor(int imGuiCol, float colX, float colY, float colZ, float colW) {
        nPushStyleColor(imGuiCol, colX, colY, colZ, colW);
    }

    public static void pushStyleColor(int imGuiCol, int col) {
        nPushStyleColor(imGuiCol, col);
    }

    public static void popStyleColor() {
        nPopStyleColor();
    }

    public static void popStyleColor(int count) {
        nPopStyleColor(count);
    }

    public static void pushStyleVar(int imGuiStyleVar, float val) {
        nPushStyleVar(imGuiStyleVar, val);
    }

    public static void pushStyleVar(int imGuiStyleVar, ImVec2 val) {
        nPushStyleVar(imGuiStyleVar, val.x, val.y);
    }

    public static void pushStyleVar(int imGuiStyleVar, float valX, float valY) {
        nPushStyleVar(imGuiStyleVar, valX, valY);
    }

    public static void popStyleVar() {
        nPopStyleVar();
    }

    public static void popStyleVar(int count) {
        nPopStyleVar(count);
    }

    public static void pushTabStop(boolean tabStop) {
        nPushTabStop(tabStop);
    }

    public static void popTabStop() {
        nPopTabStop();
    }

    public static void pushButtonRepeat(boolean repeat) {
        nPushButtonRepeat(repeat);
    }

    public static void popButtonRepeat() {
        nPopButtonRepeat();
    }

    public static void pushItemWidth(float itemWidth) {
        nPushItemWidth(itemWidth);
    }

    public static void popItemWidth() {
        nPopItemWidth();
    }

    public static void setNextItemWidth(float itemWidth) {
        nSetNextItemWidth(itemWidth);
    }

    public static float calcItemWidth() {
        return nCalcItemWidth();
    }

    public static void pushTextWrapPos() {
        nPushTextWrapPos();
    }

    public static void pushTextWrapPos(float wrapLocalPosX) {
        nPushTextWrapPos(wrapLocalPosX);
    }

    public static void popTextWrapPos() {
        nPopTextWrapPos();
    }

    public static ImFont getFont() {
        _GETFONT_1.ptr = nGetFont();
        return _GETFONT_1;
    }

    public static int getFontSize() {
        return nGetFontSize();
    }

    public static ImVec2 getFontTexUvWhitePixel() {
        ImVec2 dst = new ImVec2();
        nGetFontTexUvWhitePixel(dst);
        return dst;
    }

    public static float getFontTexUvWhitePixelX() {
        return nGetFontTexUvWhitePixelX();
    }

    public static float getFontTexUvWhitePixelY() {
        return nGetFontTexUvWhitePixelY();
    }

    public static void getFontTexUvWhitePixel(ImVec2 dst) {
        nGetFontTexUvWhitePixel(dst);
    }

    public static int getColorU32(int idx) {
        return nGetColorU32(idx);
    }

    public static int getColorU32(int idx, float alphaMul) {
        return nGetColorU32(idx, alphaMul);
    }

    public static int getColorU32(ImVec4 col) {
        return nGetColorU32(col.x, col.y, col.z, col.w);
    }

    public static int getColorU32(float colX, float colY, float colZ, float colW) {
        return nGetColorU32(colX, colY, colZ, colW);
    }

    public static int getColorU32i(int col) {
        return nGetColorU32i(col);
    }

    public static int getColorU32i(int col, float alphaMul) {
        return nGetColorU32i(col, alphaMul);
    }

    public static ImVec4 getStyleColorVec4(int imGuiColIdx) {
        ImVec4 dst = new ImVec4();
        nGetStyleColorVec4(dst, imGuiColIdx);
        return dst;
    }

    public static float getStyleColorVec4X(int imGuiColIdx) {
        return nGetStyleColorVec4X(imGuiColIdx);
    }

    public static float getStyleColorVec4Y(int imGuiColIdx) {
        return nGetStyleColorVec4Y(imGuiColIdx);
    }

    public static float getStyleColorVec4Z(int imGuiColIdx) {
        return nGetStyleColorVec4Z(imGuiColIdx);
    }

    public static float getStyleColorVec4W(int imGuiColIdx) {
        return nGetStyleColorVec4W(imGuiColIdx);
    }

    public static void getStyleColorVec4(ImVec4 dst, int imGuiColIdx) {
        nGetStyleColorVec4(dst, imGuiColIdx);
    }

    public static ImVec2 getCursorScreenPos() {
        ImVec2 dst = new ImVec2();
        nGetCursorScreenPos(dst);
        return dst;
    }

    public static float getCursorScreenPosX() {
        return nGetCursorScreenPosX();
    }

    public static float getCursorScreenPosY() {
        return nGetCursorScreenPosY();
    }

    public static void getCursorScreenPos(ImVec2 dst) {
        nGetCursorScreenPos(dst);
    }

    public static void setCursorScreenPos(ImVec2 pos) {
        nSetCursorScreenPos(pos.x, pos.y);
    }

    public static void setCursorScreenPos(float posX, float posY) {
        nSetCursorScreenPos(posX, posY);
    }

    public static ImVec2 getCursorPos() {
        ImVec2 dst = new ImVec2();
        nGetCursorPos(dst);
        return dst;
    }

    public static float getCursorPosX() {
        return nGetCursorPosX();
    }

    public static float getCursorPosY() {
        return nGetCursorPosY();
    }

    public static void getCursorPos(ImVec2 dst) {
        nGetCursorPos(dst);
    }

    public static void setCursorPos(ImVec2 localPos) {
        nSetCursorPos(localPos.x, localPos.y);
    }

    public static void setCursorPos(float localPosX, float localPosY) {
        nSetCursorPos(localPosX, localPosY);
    }

    public static void setCursorPosX(float localX) {
        nSetCursorPosX(localX);
    }

    public static void setCursorPosY(float localY) {
        nSetCursorPosY(localY);
    }

    public static ImVec2 getCursorStartPos() {
        ImVec2 dst = new ImVec2();
        nGetCursorStartPos(dst);
        return dst;
    }

    public static float getCursorStartPosX() {
        return nGetCursorStartPosX();
    }

    public static float getCursorStartPosY() {
        return nGetCursorStartPosY();
    }

    public static void getCursorStartPos(ImVec2 dst) {
        nGetCursorStartPos(dst);
    }

    public static void separator() {
        nSeparator();
    }

    public static void sameLine() {
        nSameLine();
    }

    public static void sameLine(float offsetFromStartX) {
        nSameLine(offsetFromStartX);
    }

    public static void sameLine(float offsetFromStartX, float spacing) {
        nSameLine(offsetFromStartX, spacing);
    }

    public static void newLine() {
        nNewLine();
    }

    public static void spacing() {
        nSpacing();
    }

    public static void dummy(ImVec2 size) {
        nDummy(size.x, size.y);
    }

    public static void dummy(float sizeX, float sizeY) {
        nDummy(sizeX, sizeY);
    }

    public static void indent() {
        nIndent();
    }

    public static void indent(float indentW) {
        nIndent(indentW);
    }

    public static void unindent() {
        nUnindent();
    }

    public static void unindent(float indentW) {
        nUnindent(indentW);
    }

    public static void beginGroup() {
        nBeginGroup();
    }

    public static void endGroup() {
        nEndGroup();
    }

    public static void alignTextToFramePadding() {
        nAlignTextToFramePadding();
    }

    public static float getTextLineHeight() {
        return nGetTextLineHeight();
    }

    public static float getTextLineHeightWithSpacing() {
        return nGetTextLineHeightWithSpacing();
    }

    public static float getFrameHeight() {
        return nGetFrameHeight();
    }

    public static float getFrameHeightWithSpacing() {
        return nGetFrameHeightWithSpacing();
    }

    public static void pushID(String strId) {
        nPushID(strId);
    }

    public static void pushID(String strIdBegin, String strIdEnd) {
        nPushID(strIdBegin, strIdEnd);
    }

    public static void pushID(long ptrId) {
        nPushID(ptrId);
    }

    public static void pushID(int intId) {
        nPushID(intId);
    }

    public static void popID() {
        nPopID();
    }

    public static int getID(String strId) {
        return nGetID(strId);
    }

    public static int getID(String strIdBegin, String strIdEnd) {
        return nGetID(strIdBegin, strIdEnd);
    }

    public static int getID(long ptrId) {
        return nGetID(ptrId);
    }

    public static void textUnformatted(String text) {
        nTextUnformatted(text);
    }

    public static void textUnformatted(String text, String textEnd) {
        nTextUnformatted(text, textEnd);
    }

    public static void text(String text) {
        nText(text);
    }

    public static void textColored(ImVec4 col, String text) {
        nTextColored(col.x, col.y, col.z, col.w, text);
    }

    public static void textColored(float colX, float colY, float colZ, float colW, String text) {
        nTextColored(colX, colY, colZ, colW, text);
    }

    public static void textDisabled(String text) {
        nTextDisabled(text);
    }

    public static void textWrapped(String text) {
        nTextWrapped(text);
    }

    public static void labelText(String label, String text) {
        nLabelText(label, text);
    }

    public static void bulletText(String text) {
        nBulletText(text);
    }

    public static void separatorText(String label) {
        nSeparatorText(label);
    }

    public static boolean button(String label) {
        return nButton(label);
    }

    public static boolean button(String label, ImVec2 size) {
        return nButton(label, size.x, size.y);
    }

    public static boolean button(String label, float sizeX, float sizeY) {
        return nButton(label, sizeX, sizeY);
    }

    public static boolean smallButton(String label) {
        return nSmallButton(label);
    }

    public static boolean invisibleButton(String strId, ImVec2 size) {
        return nInvisibleButton(strId, size.x, size.y);
    }

    public static boolean invisibleButton(String strId, float sizeX, float sizeY) {
        return nInvisibleButton(strId, sizeX, sizeY);
    }

    public static boolean invisibleButton(String strId, ImVec2 size, int imGuiButtonFlags) {
        return nInvisibleButton(strId, size.x, size.y, imGuiButtonFlags);
    }

    public static boolean invisibleButton(String strId, float sizeX, float sizeY, int imGuiButtonFlags) {
        return nInvisibleButton(strId, sizeX, sizeY, imGuiButtonFlags);
    }

    public static boolean arrowButton(String strId, int dir) {
        return nArrowButton(strId, dir);
    }

    public static boolean checkbox(String label, boolean active) {
        return nCheckbox(label, active);
    }

    public static boolean checkbox(String label, ImBoolean data) {
        return nCheckbox(label, data != null ? data.getData() : null);
    }

    public static boolean checkboxFlags(String label, ImInt flags, int flagsValue) {
        return nCheckboxFlags(label, flags != null ? flags.getData() : null, flagsValue);
    }

    public static boolean radioButton(String label, boolean active) {
        return nRadioButton(label, active);
    }

    public static boolean radioButton(String label, ImInt v, int vButton) {
        return nRadioButton(label, v != null ? v.getData() : null, vButton);
    }

    public static void progressBar(float fraction) {
        nProgressBar(fraction);
    }

    public static void progressBar(float fraction, ImVec2 size) {
        nProgressBar(fraction, size.x, size.y);
    }

    public static void progressBar(float fraction, float sizeX, float sizeY) {
        nProgressBar(fraction, sizeX, sizeY);
    }

    public static void progressBar(float fraction, ImVec2 size, String overlay) {
        nProgressBar(fraction, size.x, size.y, overlay);
    }

    public static void progressBar(float fraction, float sizeX, float sizeY, String overlay) {
        nProgressBar(fraction, sizeX, sizeY, overlay);
    }

    public static void progressBar(float fraction, String overlay) {
        nProgressBar(fraction, overlay);
    }

    public static void bullet() {
        nBullet();
    }

    public static void image(long userTextureId, ImVec2 imageSize) {
        nImage(userTextureId, imageSize.x, imageSize.y);
    }

    public static void image(long userTextureId, float imageSizeX, float imageSizeY) {
        nImage(userTextureId, imageSizeX, imageSizeY);
    }

    public static void image(long userTextureId, ImVec2 imageSize, ImVec2 uv0) {
        nImage(userTextureId, imageSize.x, imageSize.y, uv0.x, uv0.y);
    }

    public static void image(long userTextureId, float imageSizeX, float imageSizeY, float uv0X, float uv0Y) {
        nImage(userTextureId, imageSizeX, imageSizeY, uv0X, uv0Y);
    }

    public static void image(long userTextureId, ImVec2 imageSize, ImVec2 uv0, ImVec2 uv1) {
        nImage(userTextureId, imageSize.x, imageSize.y, uv0.x, uv0.y, uv1.x, uv1.y);
    }

    public static void image(long userTextureId, float imageSizeX, float imageSizeY, float uv0X, float uv0Y, float uv1X, float uv1Y) {
        nImage(userTextureId, imageSizeX, imageSizeY, uv0X, uv0Y, uv1X, uv1Y);
    }

    public static void image(long userTextureId, ImVec2 imageSize, ImVec2 uv0, ImVec2 uv1, ImVec4 tintCol) {
        nImage(userTextureId, imageSize.x, imageSize.y, uv0.x, uv0.y, uv1.x, uv1.y, tintCol.x, tintCol.y, tintCol.z, tintCol.w);
    }

    public static void image(long userTextureId, float imageSizeX, float imageSizeY, float uv0X, float uv0Y, float uv1X, float uv1Y, float tintColX, float tintColY, float tintColZ, float tintColW) {
        nImage(userTextureId, imageSizeX, imageSizeY, uv0X, uv0Y, uv1X, uv1Y, tintColX, tintColY, tintColZ, tintColW);
    }

    public static void image(long userTextureId, ImVec2 imageSize, ImVec2 uv0, ImVec2 uv1, ImVec4 tintCol, ImVec4 borderCol) {
        nImage(userTextureId, imageSize.x, imageSize.y, uv0.x, uv0.y, uv1.x, uv1.y, tintCol.x, tintCol.y, tintCol.z, tintCol.w, borderCol.x, borderCol.y, borderCol.z, borderCol.w);
    }

    public static void image(long userTextureId, float imageSizeX, float imageSizeY, float uv0X, float uv0Y, float uv1X, float uv1Y, float tintColX, float tintColY, float tintColZ, float tintColW, float borderColX, float borderColY, float borderColZ, float borderColW) {
        nImage(userTextureId, imageSizeX, imageSizeY, uv0X, uv0Y, uv1X, uv1Y, tintColX, tintColY, tintColZ, tintColW, borderColX, borderColY, borderColZ, borderColW);
    }

    public static boolean imageButton(String strId, long userTextureId, ImVec2 imageSize) {
        return nImageButton(strId, userTextureId, imageSize.x, imageSize.y);
    }

    public static boolean imageButton(String strId, long userTextureId, float imageSizeX, float imageSizeY) {
        return nImageButton(strId, userTextureId, imageSizeX, imageSizeY);
    }

    public static boolean imageButton(String strId, long userTextureId, ImVec2 imageSize, ImVec2 uv0) {
        return nImageButton(strId, userTextureId, imageSize.x, imageSize.y, uv0.x, uv0.y);
    }

    public static boolean imageButton(String strId, long userTextureId, float imageSizeX, float imageSizeY, float uv0X, float uv0Y) {
        return nImageButton(strId, userTextureId, imageSizeX, imageSizeY, uv0X, uv0Y);
    }

    public static boolean imageButton(String strId, long userTextureId, ImVec2 imageSize, ImVec2 uv0, ImVec2 uv1) {
        return nImageButton(strId, userTextureId, imageSize.x, imageSize.y, uv0.x, uv0.y, uv1.x, uv1.y);
    }

    public static boolean imageButton(String strId, long userTextureId, float imageSizeX, float imageSizeY, float uv0X, float uv0Y, float uv1X, float uv1Y) {
        return nImageButton(strId, userTextureId, imageSizeX, imageSizeY, uv0X, uv0Y, uv1X, uv1Y);
    }

    public static boolean imageButton(String strId, long userTextureId, ImVec2 imageSize, ImVec2 uv0, ImVec2 uv1, ImVec4 bgCol) {
        return nImageButton(strId, userTextureId, imageSize.x, imageSize.y, uv0.x, uv0.y, uv1.x, uv1.y, bgCol.x, bgCol.y, bgCol.z, bgCol.w);
    }

    public static boolean imageButton(String strId, long userTextureId, float imageSizeX, float imageSizeY, float uv0X, float uv0Y, float uv1X, float uv1Y, float bgColX, float bgColY, float bgColZ, float bgColW) {
        return nImageButton(strId, userTextureId, imageSizeX, imageSizeY, uv0X, uv0Y, uv1X, uv1Y, bgColX, bgColY, bgColZ, bgColW);
    }

    public static boolean imageButton(String strId, long userTextureId, ImVec2 imageSize, ImVec2 uv0, ImVec2 uv1, ImVec4 bgCol, ImVec4 tintCol) {
        return nImageButton(strId, userTextureId, imageSize.x, imageSize.y, uv0.x, uv0.y, uv1.x, uv1.y, bgCol.x, bgCol.y, bgCol.z, bgCol.w, tintCol.x, tintCol.y, tintCol.z, tintCol.w);
    }

    public static boolean imageButton(String strId, long userTextureId, float imageSizeX, float imageSizeY, float uv0X, float uv0Y, float uv1X, float uv1Y, float bgColX, float bgColY, float bgColZ, float bgColW, float tintColX, float tintColY, float tintColZ, float tintColW) {
        return nImageButton(strId, userTextureId, imageSizeX, imageSizeY, uv0X, uv0Y, uv1X, uv1Y, bgColX, bgColY, bgColZ, bgColW, tintColX, tintColY, tintColZ, tintColW);
    }

    public static boolean beginCombo(String label, String previewValue) {
        return nBeginCombo(label, previewValue);
    }

    public static boolean beginCombo(String label, String previewValue, int imGuiComboFlags) {
        return nBeginCombo(label, previewValue, imGuiComboFlags);
    }

    public static void endCombo() {
        nEndCombo();
    }

    public static boolean combo(String label, ImInt currentItem, String[] items) {
        return nCombo(label, currentItem != null ? currentItem.getData() : null, items, items.length);
    }

    public static boolean combo(String label, ImInt currentItem, String[] items, int popupMaxHeightInItems) {
        return nCombo(label, currentItem != null ? currentItem.getData() : null, items, items.length, popupMaxHeightInItems);
    }

    public static boolean combo(String label, ImInt currentItem, String itemsSeparatedByZeros) {
        return nCombo(label, currentItem != null ? currentItem.getData() : null, itemsSeparatedByZeros);
    }

    public static boolean combo(String label, ImInt currentItem, String itemsSeparatedByZeros, int popupMaxHeightInItems) {
        return nCombo(label, currentItem != null ? currentItem.getData() : null, itemsSeparatedByZeros, popupMaxHeightInItems);
    }

    public static boolean dragFloat(String label, float[] v) {
        return nDragFloat(label, v);
    }

    public static boolean dragFloat(String label, float[] v, float vSpeed) {
        return nDragFloat(label, v, vSpeed);
    }

    public static boolean dragFloat(String label, float[] v, float vSpeed, float vMin) {
        return nDragFloat(label, v, vSpeed, vMin);
    }

    public static boolean dragFloat(String label, float[] v, float vSpeed, float vMin, float vMax) {
        return nDragFloat(label, v, vSpeed, vMin, vMax);
    }

    public static boolean dragFloat(String label, float[] v, float vSpeed, float vMin, float vMax, String format) {
        return nDragFloat(label, v, vSpeed, vMin, vMax, format);
    }

    public static boolean dragFloat(String label, float[] v, float vSpeed, float vMin, float vMax, String format, int imGuiSliderFlags) {
        return nDragFloat(label, v, vSpeed, vMin, vMax, format, imGuiSliderFlags);
    }

    public static boolean dragFloat(String label, float[] v, float vSpeed, float vMin, float vMax, int imGuiSliderFlags) {
        return nDragFloat(label, v, vSpeed, vMin, vMax, imGuiSliderFlags);
    }

    public static boolean dragFloat2(String label, float[] v) {
        return nDragFloat2(label, v);
    }

    public static boolean dragFloat2(String label, float[] v, float vSpeed) {
        return nDragFloat2(label, v, vSpeed);
    }

    public static boolean dragFloat2(String label, float[] v, float vSpeed, float vMin) {
        return nDragFloat2(label, v, vSpeed, vMin);
    }

    public static boolean dragFloat2(String label, float[] v, float vSpeed, float vMin, float vMax) {
        return nDragFloat2(label, v, vSpeed, vMin, vMax);
    }

    public static boolean dragFloat2(String label, float[] v, float vSpeed, float vMin, float vMax, String format) {
        return nDragFloat2(label, v, vSpeed, vMin, vMax, format);
    }

    public static boolean dragFloat2(String label, float[] v, float vSpeed, float vMin, float vMax, String format, int imGuiSliderFlags) {
        return nDragFloat2(label, v, vSpeed, vMin, vMax, format, imGuiSliderFlags);
    }

    public static boolean dragFloat2(String label, float[] v, float vSpeed, float vMin, float vMax, int imGuiSliderFlags) {
        return nDragFloat2(label, v, vSpeed, vMin, vMax, imGuiSliderFlags);
    }

    public static boolean dragFloat3(String label, float[] v) {
        return nDragFloat3(label, v);
    }

    public static boolean dragFloat3(String label, float[] v, float vSpeed) {
        return nDragFloat3(label, v, vSpeed);
    }

    public static boolean dragFloat3(String label, float[] v, float vSpeed, float vMin) {
        return nDragFloat3(label, v, vSpeed, vMin);
    }

    public static boolean dragFloat3(String label, float[] v, float vSpeed, float vMin, float vMax) {
        return nDragFloat3(label, v, vSpeed, vMin, vMax);
    }

    public static boolean dragFloat3(String label, float[] v, float vSpeed, float vMin, float vMax, String format) {
        return nDragFloat3(label, v, vSpeed, vMin, vMax, format);
    }

    public static boolean dragFloat3(String label, float[] v, float vSpeed, float vMin, float vMax, String format, int imGuiSliderFlags) {
        return nDragFloat3(label, v, vSpeed, vMin, vMax, format, imGuiSliderFlags);
    }

    public static boolean dragFloat3(String label, float[] v, float vSpeed, float vMin, float vMax, int imGuiSliderFlags) {
        return nDragFloat3(label, v, vSpeed, vMin, vMax, imGuiSliderFlags);
    }

    public static boolean dragFloat4(String label, float[] v) {
        return nDragFloat4(label, v);
    }

    public static boolean dragFloat4(String label, float[] v, float vSpeed) {
        return nDragFloat4(label, v, vSpeed);
    }

    public static boolean dragFloat4(String label, float[] v, float vSpeed, float vMin) {
        return nDragFloat4(label, v, vSpeed, vMin);
    }

    public static boolean dragFloat4(String label, float[] v, float vSpeed, float vMin, float vMax) {
        return nDragFloat4(label, v, vSpeed, vMin, vMax);
    }

    public static boolean dragFloat4(String label, float[] v, float vSpeed, float vMin, float vMax, String format) {
        return nDragFloat4(label, v, vSpeed, vMin, vMax, format);
    }

    public static boolean dragFloat4(String label, float[] v, float vSpeed, float vMin, float vMax, String format, int imGuiSliderFlags) {
        return nDragFloat4(label, v, vSpeed, vMin, vMax, format, imGuiSliderFlags);
    }

    public static boolean dragFloat4(String label, float[] v, float vSpeed, float vMin, float vMax, int imGuiSliderFlags) {
        return nDragFloat4(label, v, vSpeed, vMin, vMax, imGuiSliderFlags);
    }

    public static boolean dragFloatRange2(String label, float[] vCurrentMin, float[] vCurrentMax, float vSpeed) {
        return nDragFloatRange2(label, vCurrentMin, vCurrentMax, vSpeed);
    }

    public static boolean dragFloatRange2(String label, float[] vCurrentMin, float[] vCurrentMax, float vSpeed, float vMin) {
        return nDragFloatRange2(label, vCurrentMin, vCurrentMax, vSpeed, vMin);
    }

    public static boolean dragFloatRange2(String label, float[] vCurrentMin, float[] vCurrentMax, float vSpeed, float vMin, float vMax) {
        return nDragFloatRange2(label, vCurrentMin, vCurrentMax, vSpeed, vMin, vMax);
    }

    public static boolean dragFloatRange2(String label, float[] vCurrentMin, float[] vCurrentMax, float vSpeed, float vMin, float vMax, String format) {
        return nDragFloatRange2(label, vCurrentMin, vCurrentMax, vSpeed, vMin, vMax, format);
    }

    public static boolean dragFloatRange2(String label, float[] vCurrentMin, float[] vCurrentMax, float vSpeed, float vMin, float vMax, String format, String formatMax) {
        return nDragFloatRange2(label, vCurrentMin, vCurrentMax, vSpeed, vMin, vMax, format, formatMax);
    }

    public static boolean dragFloatRange2(String label, float[] vCurrentMin, float[] vCurrentMax, float vSpeed, float vMin, float vMax, String format, String formatMax, int imGuiSliderFlags) {
        return nDragFloatRange2(label, vCurrentMin, vCurrentMax, vSpeed, vMin, vMax, format, formatMax, imGuiSliderFlags);
    }

    public static boolean dragInt(String label, int[] v) {
        return nDragInt(label, v);
    }

    public static boolean dragInt(String label, int[] v, float vSpeed) {
        return nDragInt(label, v, vSpeed);
    }

    public static boolean dragInt(String label, int[] v, float vSpeed, int vMin) {
        return nDragInt(label, v, vSpeed, vMin);
    }

    public static boolean dragInt(String label, int[] v, float vSpeed, int vMin, int vMax) {
        return nDragInt(label, v, vSpeed, vMin, vMax);
    }

    public static boolean dragInt(String label, int[] v, float vSpeed, int vMin, int vMax, String format) {
        return nDragInt(label, v, vSpeed, vMin, vMax, format);
    }

    public static boolean dragInt(String label, int[] v, float vSpeed, int vMin, int vMax, String format, int imGuiSliderFlags) {
        return nDragInt(label, v, vSpeed, vMin, vMax, format, imGuiSliderFlags);
    }

    public static boolean dragInt(String label, int[] v, float vSpeed, int vMin, int vMax, int imGuiSliderFlags) {
        return nDragInt(label, v, vSpeed, vMin, vMax, imGuiSliderFlags);
    }

    public static boolean dragInt2(String label, int[] v) {
        return nDragInt2(label, v);
    }

    public static boolean dragInt2(String label, int[] v, float vSpeed) {
        return nDragInt2(label, v, vSpeed);
    }

    public static boolean dragInt2(String label, int[] v, float vSpeed, int vMin) {
        return nDragInt2(label, v, vSpeed, vMin);
    }

    public static boolean dragInt2(String label, int[] v, float vSpeed, int vMin, int vMax) {
        return nDragInt2(label, v, vSpeed, vMin, vMax);
    }

    public static boolean dragInt2(String label, int[] v, float vSpeed, int vMin, int vMax, String format) {
        return nDragInt2(label, v, vSpeed, vMin, vMax, format);
    }

    public static boolean dragInt2(String label, int[] v, float vSpeed, int vMin, int vMax, String format, int imGuiSliderFlags) {
        return nDragInt2(label, v, vSpeed, vMin, vMax, format, imGuiSliderFlags);
    }

    public static boolean dragInt2(String label, int[] v, float vSpeed, int vMin, int vMax, int imGuiSliderFlags) {
        return nDragInt2(label, v, vSpeed, vMin, vMax, imGuiSliderFlags);
    }

    public static boolean dragInt3(String label, int[] v) {
        return nDragInt3(label, v);
    }

    public static boolean dragInt3(String label, int[] v, float vSpeed) {
        return nDragInt3(label, v, vSpeed);
    }

    public static boolean dragInt3(String label, int[] v, float vSpeed, int vMin) {
        return nDragInt3(label, v, vSpeed, vMin);
    }

    public static boolean dragInt3(String label, int[] v, float vSpeed, int vMin, int vMax) {
        return nDragInt3(label, v, vSpeed, vMin, vMax);
    }

    public static boolean dragInt3(String label, int[] v, float vSpeed, int vMin, int vMax, String format) {
        return nDragInt3(label, v, vSpeed, vMin, vMax, format);
    }

    public static boolean dragInt3(String label, int[] v, float vSpeed, int vMin, int vMax, String format, int imGuiSliderFlags) {
        return nDragInt3(label, v, vSpeed, vMin, vMax, format, imGuiSliderFlags);
    }

    public static boolean dragInt3(String label, int[] v, float vSpeed, int vMin, int vMax, int imGuiSliderFlags) {
        return nDragInt3(label, v, vSpeed, vMin, vMax, imGuiSliderFlags);
    }

    public static boolean dragInt4(String label, int[] v) {
        return nDragInt4(label, v);
    }

    public static boolean dragInt4(String label, int[] v, float vSpeed) {
        return nDragInt4(label, v, vSpeed);
    }

    public static boolean dragInt4(String label, int[] v, float vSpeed, int vMin) {
        return nDragInt4(label, v, vSpeed, vMin);
    }

    public static boolean dragInt4(String label, int[] v, float vSpeed, int vMin, int vMax) {
        return nDragInt4(label, v, vSpeed, vMin, vMax);
    }

    public static boolean dragInt4(String label, int[] v, float vSpeed, int vMin, int vMax, String format) {
        return nDragInt4(label, v, vSpeed, vMin, vMax, format);
    }

    public static boolean dragInt4(String label, int[] v, float vSpeed, int vMin, int vMax, String format, int imGuiSliderFlags) {
        return nDragInt4(label, v, vSpeed, vMin, vMax, format, imGuiSliderFlags);
    }

    public static boolean dragInt4(String label, int[] v, float vSpeed, int vMin, int vMax, int imGuiSliderFlags) {
        return nDragInt4(label, v, vSpeed, vMin, vMax, imGuiSliderFlags);
    }

    public static boolean dragIntRange2(String label, int[] vCurrentMin, int[] vCurrentMax) {
        return nDragIntRange2(label, vCurrentMin, vCurrentMax);
    }

    public static boolean dragIntRange2(String label, int[] vCurrentMin, int[] vCurrentMax, float vSpeed) {
        return nDragIntRange2(label, vCurrentMin, vCurrentMax, vSpeed);
    }

    public static boolean dragIntRange2(String label, int[] vCurrentMin, int[] vCurrentMax, float vSpeed, int vMin) {
        return nDragIntRange2(label, vCurrentMin, vCurrentMax, vSpeed, vMin);
    }

    public static boolean dragIntRange2(String label, int[] vCurrentMin, int[] vCurrentMax, float vSpeed, int vMin, int vMax) {
        return nDragIntRange2(label, vCurrentMin, vCurrentMax, vSpeed, vMin, vMax);
    }

    public static boolean dragIntRange2(String label, int[] vCurrentMin, int[] vCurrentMax, float vSpeed, int vMin, int vMax, String format) {
        return nDragIntRange2(label, vCurrentMin, vCurrentMax, vSpeed, vMin, vMax, format);
    }

    public static boolean dragIntRange2(String label, int[] vCurrentMin, int[] vCurrentMax, float vSpeed, int vMin, int vMax, String format, String formatMax) {
        return nDragIntRange2(label, vCurrentMin, vCurrentMax, vSpeed, vMin, vMax, format, formatMax);
    }

    public static boolean dragIntRange2(String label, int[] vCurrentMin, int[] vCurrentMax, float vSpeed, int vMin, int vMax, String format, String formatMax, int imGuiSliderFlags) {
        return nDragIntRange2(label, vCurrentMin, vCurrentMax, vSpeed, vMin, vMax, format, formatMax, imGuiSliderFlags);
    }

    public static boolean dragScalar(String label, short[] pData) {
        return nDragScalar(label, pData);
    }

    public static boolean dragScalar(String label, short[] pData, float vSpeed) {
        return nDragScalar(label, pData, vSpeed);
    }

    public static boolean dragScalar(String label, short[] pData, float vSpeed, short pMin) {
        return nDragScalar(label, pData, vSpeed, pMin);
    }

    public static boolean dragScalar(String label, short[] pData, float vSpeed, short pMin, short pMax) {
        return nDragScalar(label, pData, vSpeed, pMin, pMax);
    }

    public static boolean dragScalar(String label, short[] pData, float vSpeed, short pMin, short pMax, String format) {
        return nDragScalar(label, pData, vSpeed, pMin, pMax, format);
    }

    public static boolean dragScalar(String label, short[] pData, float vSpeed, short pMin, short pMax, String format, int imGuiSliderFlags) {
        return nDragScalar(label, pData, vSpeed, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean dragScalar(String label, int[] pData) {
        return nDragScalar(label, pData);
    }

    public static boolean dragScalar(String label, int[] pData, float vSpeed) {
        return nDragScalar(label, pData, vSpeed);
    }

    public static boolean dragScalar(String label, int[] pData, float vSpeed, int pMin) {
        return nDragScalar(label, pData, vSpeed, pMin);
    }

    public static boolean dragScalar(String label, int[] pData, float vSpeed, int pMin, int pMax) {
        return nDragScalar(label, pData, vSpeed, pMin, pMax);
    }

    public static boolean dragScalar(String label, int[] pData, float vSpeed, int pMin, int pMax, String format) {
        return nDragScalar(label, pData, vSpeed, pMin, pMax, format);
    }

    public static boolean dragScalar(String label, int[] pData, float vSpeed, int pMin, int pMax, String format, int imGuiSliderFlags) {
        return nDragScalar(label, pData, vSpeed, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean dragScalar(String label, long[] pData) {
        return nDragScalar(label, pData);
    }

    public static boolean dragScalar(String label, long[] pData, float vSpeed) {
        return nDragScalar(label, pData, vSpeed);
    }

    public static boolean dragScalar(String label, long[] pData, float vSpeed, long pMin) {
        return nDragScalar(label, pData, vSpeed, pMin);
    }

    public static boolean dragScalar(String label, long[] pData, float vSpeed, long pMin, long pMax) {
        return nDragScalar(label, pData, vSpeed, pMin, pMax);
    }

    public static boolean dragScalar(String label, long[] pData, float vSpeed, long pMin, long pMax, String format) {
        return nDragScalar(label, pData, vSpeed, pMin, pMax, format);
    }

    public static boolean dragScalar(String label, long[] pData, float vSpeed, long pMin, long pMax, String format, int imGuiSliderFlags) {
        return nDragScalar(label, pData, vSpeed, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean dragScalar(String label, float[] pData) {
        return nDragScalar(label, pData);
    }

    public static boolean dragScalar(String label, float[] pData, float vSpeed) {
        return nDragScalar(label, pData, vSpeed);
    }

    public static boolean dragScalar(String label, float[] pData, float vSpeed, float pMin) {
        return nDragScalar(label, pData, vSpeed, pMin);
    }

    public static boolean dragScalar(String label, float[] pData, float vSpeed, float pMin, float pMax) {
        return nDragScalar(label, pData, vSpeed, pMin, pMax);
    }

    public static boolean dragScalar(String label, float[] pData, float vSpeed, float pMin, float pMax, String format) {
        return nDragScalar(label, pData, vSpeed, pMin, pMax, format);
    }

    public static boolean dragScalar(String label, float[] pData, float vSpeed, float pMin, float pMax, String format, int imGuiSliderFlags) {
        return nDragScalar(label, pData, vSpeed, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean dragScalar(String label, double[] pData) {
        return nDragScalar(label, pData);
    }

    public static boolean dragScalar(String label, double[] pData, float vSpeed) {
        return nDragScalar(label, pData, vSpeed);
    }

    public static boolean dragScalar(String label, double[] pData, float vSpeed, double pMin) {
        return nDragScalar(label, pData, vSpeed, pMin);
    }

    public static boolean dragScalar(String label, double[] pData, float vSpeed, double pMin, double pMax) {
        return nDragScalar(label, pData, vSpeed, pMin, pMax);
    }

    public static boolean dragScalar(String label, double[] pData, float vSpeed, double pMin, double pMax, String format) {
        return nDragScalar(label, pData, vSpeed, pMin, pMax, format);
    }

    public static boolean dragScalar(String label, double[] pData, float vSpeed, double pMin, double pMax, String format, int imGuiSliderFlags) {
        return nDragScalar(label, pData, vSpeed, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean dragScalarN(String label, short[] pData, int components) {
        return nDragScalarN(label, pData, components);
    }

    public static boolean dragScalarN(String label, short[] pData, int components, float vSpeed) {
        return nDragScalarN(label, pData, components, vSpeed);
    }

    public static boolean dragScalarN(String label, short[] pData, int components, float vSpeed, short pMin) {
        return nDragScalarN(label, pData, components, vSpeed, pMin);
    }

    public static boolean dragScalarN(String label, short[] pData, int components, float vSpeed, short pMin, short pMax) {
        return nDragScalarN(label, pData, components, vSpeed, pMin, pMax);
    }

    public static boolean dragScalarN(String label, short[] pData, int components, float vSpeed, short pMin, short pMax, String format) {
        return nDragScalarN(label, pData, components, vSpeed, pMin, pMax, format);
    }

    public static boolean dragScalarN(String label, short[] pData, int components, float vSpeed, short pMin, short pMax, String format, int imGuiSliderFlags) {
        return nDragScalarN(label, pData, components, vSpeed, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean dragScalarN(String label, int[] pData, int components) {
        return nDragScalarN(label, pData, components);
    }

    public static boolean dragScalarN(String label, int[] pData, int components, float vSpeed) {
        return nDragScalarN(label, pData, components, vSpeed);
    }

    public static boolean dragScalarN(String label, int[] pData, int components, float vSpeed, int pMin) {
        return nDragScalarN(label, pData, components, vSpeed, pMin);
    }

    public static boolean dragScalarN(String label, int[] pData, int components, float vSpeed, int pMin, int pMax) {
        return nDragScalarN(label, pData, components, vSpeed, pMin, pMax);
    }

    public static boolean dragScalarN(String label, int[] pData, int components, float vSpeed, int pMin, int pMax, String format) {
        return nDragScalarN(label, pData, components, vSpeed, pMin, pMax, format);
    }

    public static boolean dragScalarN(String label, int[] pData, int components, float vSpeed, int pMin, int pMax, String format, int imGuiSliderFlags) {
        return nDragScalarN(label, pData, components, vSpeed, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean dragScalarN(String label, long[] pData, int components) {
        return nDragScalarN(label, pData, components);
    }

    public static boolean dragScalarN(String label, long[] pData, int components, float vSpeed) {
        return nDragScalarN(label, pData, components, vSpeed);
    }

    public static boolean dragScalarN(String label, long[] pData, int components, float vSpeed, long pMin) {
        return nDragScalarN(label, pData, components, vSpeed, pMin);
    }

    public static boolean dragScalarN(String label, long[] pData, int components, float vSpeed, long pMin, long pMax) {
        return nDragScalarN(label, pData, components, vSpeed, pMin, pMax);
    }

    public static boolean dragScalarN(String label, long[] pData, int components, float vSpeed, long pMin, long pMax, String format) {
        return nDragScalarN(label, pData, components, vSpeed, pMin, pMax, format);
    }

    public static boolean dragScalarN(String label, long[] pData, int components, float vSpeed, long pMin, long pMax, String format, int imGuiSliderFlags) {
        return nDragScalarN(label, pData, components, vSpeed, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean dragScalarN(String label, float[] pData, int components) {
        return nDragScalarN(label, pData, components);
    }

    public static boolean dragScalarN(String label, float[] pData, int components, float vSpeed) {
        return nDragScalarN(label, pData, components, vSpeed);
    }

    public static boolean dragScalarN(String label, float[] pData, int components, float vSpeed, float pMin) {
        return nDragScalarN(label, pData, components, vSpeed, pMin);
    }

    public static boolean dragScalarN(String label, float[] pData, int components, float vSpeed, float pMin, float pMax) {
        return nDragScalarN(label, pData, components, vSpeed, pMin, pMax);
    }

    public static boolean dragScalarN(String label, float[] pData, int components, float vSpeed, float pMin, float pMax, String format) {
        return nDragScalarN(label, pData, components, vSpeed, pMin, pMax, format);
    }

    public static boolean dragScalarN(String label, float[] pData, int components, float vSpeed, float pMin, float pMax, String format, int imGuiSliderFlags) {
        return nDragScalarN(label, pData, components, vSpeed, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean dragScalarN(String label, double[] pData, int components) {
        return nDragScalarN(label, pData, components);
    }

    public static boolean dragScalarN(String label, double[] pData, int components, float vSpeed) {
        return nDragScalarN(label, pData, components, vSpeed);
    }

    public static boolean dragScalarN(String label, double[] pData, int components, float vSpeed, double pMin) {
        return nDragScalarN(label, pData, components, vSpeed, pMin);
    }

    public static boolean dragScalarN(String label, double[] pData, int components, float vSpeed, double pMin, double pMax) {
        return nDragScalarN(label, pData, components, vSpeed, pMin, pMax);
    }

    public static boolean dragScalarN(String label, double[] pData, int components, float vSpeed, double pMin, double pMax, String format) {
        return nDragScalarN(label, pData, components, vSpeed, pMin, pMax, format);
    }

    public static boolean dragScalarN(String label, double[] pData, int components, float vSpeed, double pMin, double pMax, String format, int imGuiSliderFlags) {
        return nDragScalarN(label, pData, components, vSpeed, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean sliderFloat(String label, float[] v, float vMin, float vMax) {
        return nSliderFloat(label, v, vMin, vMax);
    }

    public static boolean sliderFloat(String label, float[] v, float vMin, float vMax, String format) {
        return nSliderFloat(label, v, vMin, vMax, format);
    }

    public static boolean sliderFloat(String label, float[] v, float vMin, float vMax, String format, int imGuiSliderFlags) {
        return nSliderFloat(label, v, vMin, vMax, format, imGuiSliderFlags);
    }

    public static boolean sliderFloat(String label, float[] v, float vMin, float vMax, int imGuiSliderFlags) {
        return nSliderFloat(label, v, vMin, vMax, imGuiSliderFlags);
    }

    public static boolean sliderFloat2(String label, float[] v, float vMin, float vMax) {
        return nSliderFloat2(label, v, vMin, vMax);
    }

    public static boolean sliderFloat2(String label, float[] v, float vMin, float vMax, String format) {
        return nSliderFloat2(label, v, vMin, vMax, format);
    }

    public static boolean sliderFloat2(String label, float[] v, float vMin, float vMax, String format, int imGuiSliderFlags) {
        return nSliderFloat2(label, v, vMin, vMax, format, imGuiSliderFlags);
    }

    public static boolean sliderFloat2(String label, float[] v, float vMin, float vMax, int imGuiSliderFlags) {
        return nSliderFloat2(label, v, vMin, vMax, imGuiSliderFlags);
    }

    public static boolean sliderFloat3(String label, float[] v, float vMin, float vMax) {
        return nSliderFloat3(label, v, vMin, vMax);
    }

    public static boolean sliderFloat3(String label, float[] v, float vMin, float vMax, String format) {
        return nSliderFloat3(label, v, vMin, vMax, format);
    }

    public static boolean sliderFloat3(String label, float[] v, float vMin, float vMax, String format, int imGuiSliderFlags) {
        return nSliderFloat3(label, v, vMin, vMax, format, imGuiSliderFlags);
    }

    public static boolean sliderFloat3(String label, float[] v, float vMin, float vMax, int imGuiSliderFlags) {
        return nSliderFloat3(label, v, vMin, vMax, imGuiSliderFlags);
    }

    public static boolean sliderFloat4(String label, float[] v, float vMin, float vMax) {
        return nSliderFloat4(label, v, vMin, vMax);
    }

    public static boolean sliderFloat4(String label, float[] v, float vMin, float vMax, String format) {
        return nSliderFloat4(label, v, vMin, vMax, format);
    }

    public static boolean sliderFloat4(String label, float[] v, float vMin, float vMax, String format, int imGuiSliderFlags) {
        return nSliderFloat4(label, v, vMin, vMax, format, imGuiSliderFlags);
    }

    public static boolean sliderFloat4(String label, float[] v, float vMin, float vMax, int imGuiSliderFlags) {
        return nSliderFloat4(label, v, vMin, vMax, imGuiSliderFlags);
    }

    public static boolean sliderAngle(String label, float[] vRad) {
        return nSliderAngle(label, vRad);
    }

    public static boolean sliderAngle(String label, float[] vRad, float vDegreesMin) {
        return nSliderAngle(label, vRad, vDegreesMin);
    }

    public static boolean sliderAngle(String label, float[] vRad, float vDegreesMin, float vDegreesMax) {
        return nSliderAngle(label, vRad, vDegreesMin, vDegreesMax);
    }

    public static boolean sliderAngle(String label, float[] vRad, float vDegreesMin, float vDegreesMax, String format) {
        return nSliderAngle(label, vRad, vDegreesMin, vDegreesMax, format);
    }

    public static boolean sliderAngle(String label, float[] vRad, float vDegreesMin, float vDegreesMax, String format, int imGuiSliderFlags) {
        return nSliderAngle(label, vRad, vDegreesMin, vDegreesMax, format, imGuiSliderFlags);
    }

    public static boolean sliderAngle(String label, float[] vRad, float vDegreesMin, float vDegreesMax, int imGuiSliderFlags) {
        return nSliderAngle(label, vRad, vDegreesMin, vDegreesMax, imGuiSliderFlags);
    }

    public static boolean sliderInt(String label, int[] v, int vMin, int vMax) {
        return nSliderInt(label, v, vMin, vMax);
    }

    public static boolean sliderInt(String label, int[] v, int vMin, int vMax, String format) {
        return nSliderInt(label, v, vMin, vMax, format);
    }

    public static boolean sliderInt(String label, int[] v, int vMin, int vMax, String format, int imGuiSliderFlags) {
        return nSliderInt(label, v, vMin, vMax, format, imGuiSliderFlags);
    }

    public static boolean sliderInt(String label, int[] v, int vMin, int vMax, int imGuiSliderFlags) {
        return nSliderInt(label, v, vMin, vMax, imGuiSliderFlags);
    }

    public static boolean sliderInt2(String label, int[] v, int vMin, int vMax) {
        return nSliderInt2(label, v, vMin, vMax);
    }

    public static boolean sliderInt2(String label, int[] v, int vMin, int vMax, String format) {
        return nSliderInt2(label, v, vMin, vMax, format);
    }

    public static boolean sliderInt2(String label, int[] v, int vMin, int vMax, String format, int imGuiSliderFlags) {
        return nSliderInt2(label, v, vMin, vMax, format, imGuiSliderFlags);
    }

    public static boolean sliderInt2(String label, int[] v, int vMin, int vMax, int imGuiSliderFlags) {
        return nSliderInt2(label, v, vMin, vMax, imGuiSliderFlags);
    }

    public static boolean sliderInt3(String label, int[] v, int vMin, int vMax) {
        return nSliderInt3(label, v, vMin, vMax);
    }

    public static boolean sliderInt3(String label, int[] v, int vMin, int vMax, String format) {
        return nSliderInt3(label, v, vMin, vMax, format);
    }

    public static boolean sliderInt3(String label, int[] v, int vMin, int vMax, String format, int imGuiSliderFlags) {
        return nSliderInt3(label, v, vMin, vMax, format, imGuiSliderFlags);
    }

    public static boolean sliderInt3(String label, int[] v, int vMin, int vMax, int imGuiSliderFlags) {
        return nSliderInt3(label, v, vMin, vMax, imGuiSliderFlags);
    }

    public static boolean sliderInt4(String label, int[] v, int vMin, int vMax) {
        return nSliderInt4(label, v, vMin, vMax);
    }

    public static boolean sliderInt4(String label, int[] v, int vMin, int vMax, String format) {
        return nSliderInt4(label, v, vMin, vMax, format);
    }

    public static boolean sliderInt4(String label, int[] v, int vMin, int vMax, String format, int imGuiSliderFlags) {
        return nSliderInt4(label, v, vMin, vMax, format, imGuiSliderFlags);
    }

    public static boolean sliderInt4(String label, int[] v, int vMin, int vMax, int imGuiSliderFlags) {
        return nSliderInt4(label, v, vMin, vMax, imGuiSliderFlags);
    }

    public static boolean sliderScalar(String label, short[] pData, short pMin, short pMax) {
        return nSliderScalar(label, pData, pMin, pMax);
    }

    public static boolean sliderScalar(String label, short[] pData, short pMin, short pMax, String format) {
        return nSliderScalar(label, pData, pMin, pMax, format);
    }

    public static boolean sliderScalar(String label, short[] pData, short pMin, short pMax, String format, int imGuiSliderFlags) {
        return nSliderScalar(label, pData, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean sliderScalar(String label, int[] pData, int pMin, int pMax) {
        return nSliderScalar(label, pData, pMin, pMax);
    }

    public static boolean sliderScalar(String label, int[] pData, int pMin, int pMax, String format) {
        return nSliderScalar(label, pData, pMin, pMax, format);
    }

    public static boolean sliderScalar(String label, int[] pData, int pMin, int pMax, String format, int imGuiSliderFlags) {
        return nSliderScalar(label, pData, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean sliderScalar(String label, long[] pData, long pMin, long pMax) {
        return nSliderScalar(label, pData, pMin, pMax);
    }

    public static boolean sliderScalar(String label, long[] pData, long pMin, long pMax, String format) {
        return nSliderScalar(label, pData, pMin, pMax, format);
    }

    public static boolean sliderScalar(String label, long[] pData, long pMin, long pMax, String format, int imGuiSliderFlags) {
        return nSliderScalar(label, pData, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean sliderScalar(String label, float[] pData, float pMin, float pMax) {
        return nSliderScalar(label, pData, pMin, pMax);
    }

    public static boolean sliderScalar(String label, float[] pData, float pMin, float pMax, String format) {
        return nSliderScalar(label, pData, pMin, pMax, format);
    }

    public static boolean sliderScalar(String label, float[] pData, float pMin, float pMax, String format, int imGuiSliderFlags) {
        return nSliderScalar(label, pData, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean sliderScalar(String label, double[] pData, double pMin, double pMax) {
        return nSliderScalar(label, pData, pMin, pMax);
    }

    public static boolean sliderScalar(String label, double[] pData, double pMin, double pMax, String format) {
        return nSliderScalar(label, pData, pMin, pMax, format);
    }

    public static boolean sliderScalar(String label, double[] pData, double pMin, double pMax, String format, int imGuiSliderFlags) {
        return nSliderScalar(label, pData, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean sliderScalarN(String label, short[] pData, int components, short pMin, short pMax) {
        return nSliderScalarN(label, pData, components, pMin, pMax);
    }

    public static boolean sliderScalarN(String label, short[] pData, int components, short pMin, short pMax, String format) {
        return nSliderScalarN(label, pData, components, pMin, pMax, format);
    }

    public static boolean sliderScalarN(String label, short[] pData, int components, short pMin, short pMax, String format, int imGuiSliderFlags) {
        return nSliderScalarN(label, pData, components, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean sliderScalarN(String label, int[] pData, int components, int pMin, int pMax) {
        return nSliderScalarN(label, pData, components, pMin, pMax);
    }

    public static boolean sliderScalarN(String label, int[] pData, int components, int pMin, int pMax, String format) {
        return nSliderScalarN(label, pData, components, pMin, pMax, format);
    }

    public static boolean sliderScalarN(String label, int[] pData, int components, int pMin, int pMax, String format, int imGuiSliderFlags) {
        return nSliderScalarN(label, pData, components, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean sliderScalarN(String label, long[] pData, int components, long pMin, long pMax) {
        return nSliderScalarN(label, pData, components, pMin, pMax);
    }

    public static boolean sliderScalarN(String label, long[] pData, int components, long pMin, long pMax, String format) {
        return nSliderScalarN(label, pData, components, pMin, pMax, format);
    }

    public static boolean sliderScalarN(String label, long[] pData, int components, long pMin, long pMax, String format, int imGuiSliderFlags) {
        return nSliderScalarN(label, pData, components, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean sliderScalarN(String label, float[] pData, int components, float pMin, float pMax) {
        return nSliderScalarN(label, pData, components, pMin, pMax);
    }

    public static boolean sliderScalarN(String label, float[] pData, int components, float pMin, float pMax, String format) {
        return nSliderScalarN(label, pData, components, pMin, pMax, format);
    }

    public static boolean sliderScalarN(String label, float[] pData, int components, float pMin, float pMax, String format, int imGuiSliderFlags) {
        return nSliderScalarN(label, pData, components, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean sliderScalarN(String label, double[] pData, int components, double pMin, double pMax) {
        return nSliderScalarN(label, pData, components, pMin, pMax);
    }

    public static boolean sliderScalarN(String label, double[] pData, int components, double pMin, double pMax, String format) {
        return nSliderScalarN(label, pData, components, pMin, pMax, format);
    }

    public static boolean sliderScalarN(String label, double[] pData, int components, double pMin, double pMax, String format, int imGuiSliderFlags) {
        return nSliderScalarN(label, pData, components, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean vSliderFloat(String label, ImVec2 size, float[] v, float vMin, float vMax) {
        return nVSliderFloat(label, size.x, size.y, v, vMin, vMax);
    }

    public static boolean vSliderFloat(String label, float sizeX, float sizeY, float[] v, float vMin, float vMax) {
        return nVSliderFloat(label, sizeX, sizeY, v, vMin, vMax);
    }

    public static boolean vSliderFloat(String label, ImVec2 size, float[] v, float vMin, float vMax, String format) {
        return nVSliderFloat(label, size.x, size.y, v, vMin, vMax, format);
    }

    public static boolean vSliderFloat(String label, float sizeX, float sizeY, float[] v, float vMin, float vMax, String format) {
        return nVSliderFloat(label, sizeX, sizeY, v, vMin, vMax, format);
    }

    public static boolean vSliderFloat(String label, ImVec2 size, float[] v, float vMin, float vMax, String format, int imGuiSliderFlags) {
        return nVSliderFloat(label, size.x, size.y, v, vMin, vMax, format, imGuiSliderFlags);
    }

    public static boolean vSliderFloat(String label, float sizeX, float sizeY, float[] v, float vMin, float vMax, String format, int imGuiSliderFlags) {
        return nVSliderFloat(label, sizeX, sizeY, v, vMin, vMax, format, imGuiSliderFlags);
    }

    public static boolean vSliderFloat(String label, ImVec2 size, float[] v, float vMin, float vMax, int imGuiSliderFlags) {
        return nVSliderFloat(label, size.x, size.y, v, vMin, vMax, imGuiSliderFlags);
    }

    public static boolean vSliderFloat(String label, float sizeX, float sizeY, float[] v, float vMin, float vMax, int imGuiSliderFlags) {
        return nVSliderFloat(label, sizeX, sizeY, v, vMin, vMax, imGuiSliderFlags);
    }

    public static boolean vSliderInt(String label, ImVec2 size, int[] v, int vMin, int vMax) {
        return nVSliderInt(label, size.x, size.y, v, vMin, vMax);
    }

    public static boolean vSliderInt(String label, float sizeX, float sizeY, int[] v, int vMin, int vMax) {
        return nVSliderInt(label, sizeX, sizeY, v, vMin, vMax);
    }

    public static boolean vSliderInt(String label, ImVec2 size, int[] v, int vMin, int vMax, String format) {
        return nVSliderInt(label, size.x, size.y, v, vMin, vMax, format);
    }

    public static boolean vSliderInt(String label, float sizeX, float sizeY, int[] v, int vMin, int vMax, String format) {
        return nVSliderInt(label, sizeX, sizeY, v, vMin, vMax, format);
    }

    public static boolean vSliderInt(String label, ImVec2 size, int[] v, int vMin, int vMax, String format, int imGuiSliderFlags) {
        return nVSliderInt(label, size.x, size.y, v, vMin, vMax, format, imGuiSliderFlags);
    }

    public static boolean vSliderInt(String label, float sizeX, float sizeY, int[] v, int vMin, int vMax, String format, int imGuiSliderFlags) {
        return nVSliderInt(label, sizeX, sizeY, v, vMin, vMax, format, imGuiSliderFlags);
    }

    public static boolean vSliderInt(String label, ImVec2 size, int[] v, int vMin, int vMax, int imGuiSliderFlags) {
        return nVSliderInt(label, size.x, size.y, v, vMin, vMax, imGuiSliderFlags);
    }

    public static boolean vSliderInt(String label, float sizeX, float sizeY, int[] v, int vMin, int vMax, int imGuiSliderFlags) {
        return nVSliderInt(label, sizeX, sizeY, v, vMin, vMax, imGuiSliderFlags);
    }

    public static boolean vSliderScalar(String label, ImVec2 size, short[] pData, short pMin, short pMax) {
        return nVSliderScalar(label, size.x, size.y, pData, pMin, pMax);
    }

    public static boolean vSliderScalar(String label, float sizeX, float sizeY, short[] pData, short pMin, short pMax) {
        return nVSliderScalar(label, sizeX, sizeY, pData, pMin, pMax);
    }

    public static boolean vSliderScalar(String label, ImVec2 size, short[] pData, short pMin, short pMax, String format) {
        return nVSliderScalar(label, size.x, size.y, pData, pMin, pMax, format);
    }

    public static boolean vSliderScalar(String label, float sizeX, float sizeY, short[] pData, short pMin, short pMax, String format) {
        return nVSliderScalar(label, sizeX, sizeY, pData, pMin, pMax, format);
    }

    public static boolean vSliderScalar(String label, ImVec2 size, short[] pData, short pMin, short pMax, String format, int imGuiSliderFlags) {
        return nVSliderScalar(label, size.x, size.y, pData, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean vSliderScalar(String label, float sizeX, float sizeY, short[] pData, short pMin, short pMax, String format, int imGuiSliderFlags) {
        return nVSliderScalar(label, sizeX, sizeY, pData, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean vSliderScalar(String label, ImVec2 size, int[] pData, int pMin, int pMax) {
        return nVSliderScalar(label, size.x, size.y, pData, pMin, pMax);
    }

    public static boolean vSliderScalar(String label, float sizeX, float sizeY, int[] pData, int pMin, int pMax) {
        return nVSliderScalar(label, sizeX, sizeY, pData, pMin, pMax);
    }

    public static boolean vSliderScalar(String label, ImVec2 size, int[] pData, int pMin, int pMax, String format) {
        return nVSliderScalar(label, size.x, size.y, pData, pMin, pMax, format);
    }

    public static boolean vSliderScalar(String label, float sizeX, float sizeY, int[] pData, int pMin, int pMax, String format) {
        return nVSliderScalar(label, sizeX, sizeY, pData, pMin, pMax, format);
    }

    public static boolean vSliderScalar(String label, ImVec2 size, int[] pData, int pMin, int pMax, String format, int imGuiSliderFlags) {
        return nVSliderScalar(label, size.x, size.y, pData, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean vSliderScalar(String label, float sizeX, float sizeY, int[] pData, int pMin, int pMax, String format, int imGuiSliderFlags) {
        return nVSliderScalar(label, sizeX, sizeY, pData, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean vSliderScalar(String label, ImVec2 size, long[] pData, long pMin, long pMax) {
        return nVSliderScalar(label, size.x, size.y, pData, pMin, pMax);
    }

    public static boolean vSliderScalar(String label, float sizeX, float sizeY, long[] pData, long pMin, long pMax) {
        return nVSliderScalar(label, sizeX, sizeY, pData, pMin, pMax);
    }

    public static boolean vSliderScalar(String label, ImVec2 size, long[] pData, long pMin, long pMax, String format) {
        return nVSliderScalar(label, size.x, size.y, pData, pMin, pMax, format);
    }

    public static boolean vSliderScalar(String label, float sizeX, float sizeY, long[] pData, long pMin, long pMax, String format) {
        return nVSliderScalar(label, sizeX, sizeY, pData, pMin, pMax, format);
    }

    public static boolean vSliderScalar(String label, ImVec2 size, long[] pData, long pMin, long pMax, String format, int imGuiSliderFlags) {
        return nVSliderScalar(label, size.x, size.y, pData, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean vSliderScalar(String label, float sizeX, float sizeY, long[] pData, long pMin, long pMax, String format, int imGuiSliderFlags) {
        return nVSliderScalar(label, sizeX, sizeY, pData, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean vSliderScalar(String label, ImVec2 size, float[] pData, float pMin, float pMax) {
        return nVSliderScalar(label, size.x, size.y, pData, pMin, pMax);
    }

    public static boolean vSliderScalar(String label, float sizeX, float sizeY, float[] pData, float pMin, float pMax) {
        return nVSliderScalar(label, sizeX, sizeY, pData, pMin, pMax);
    }

    public static boolean vSliderScalar(String label, ImVec2 size, float[] pData, float pMin, float pMax, String format) {
        return nVSliderScalar(label, size.x, size.y, pData, pMin, pMax, format);
    }

    public static boolean vSliderScalar(String label, float sizeX, float sizeY, float[] pData, float pMin, float pMax, String format) {
        return nVSliderScalar(label, sizeX, sizeY, pData, pMin, pMax, format);
    }

    public static boolean vSliderScalar(String label, ImVec2 size, float[] pData, float pMin, float pMax, String format, int imGuiSliderFlags) {
        return nVSliderScalar(label, size.x, size.y, pData, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean vSliderScalar(String label, float sizeX, float sizeY, float[] pData, float pMin, float pMax, String format, int imGuiSliderFlags) {
        return nVSliderScalar(label, sizeX, sizeY, pData, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean vSliderScalar(String label, ImVec2 size, double[] pData, double pMin, double pMax) {
        return nVSliderScalar(label, size.x, size.y, pData, pMin, pMax);
    }

    public static boolean vSliderScalar(String label, float sizeX, float sizeY, double[] pData, double pMin, double pMax) {
        return nVSliderScalar(label, sizeX, sizeY, pData, pMin, pMax);
    }

    public static boolean vSliderScalar(String label, ImVec2 size, double[] pData, double pMin, double pMax, String format) {
        return nVSliderScalar(label, size.x, size.y, pData, pMin, pMax, format);
    }

    public static boolean vSliderScalar(String label, float sizeX, float sizeY, double[] pData, double pMin, double pMax, String format) {
        return nVSliderScalar(label, sizeX, sizeY, pData, pMin, pMax, format);
    }

    public static boolean vSliderScalar(String label, ImVec2 size, double[] pData, double pMin, double pMax, String format, int imGuiSliderFlags) {
        return nVSliderScalar(label, size.x, size.y, pData, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean vSliderScalar(String label, float sizeX, float sizeY, double[] pData, double pMin, double pMax, String format, int imGuiSliderFlags) {
        return nVSliderScalar(label, sizeX, sizeY, pData, pMin, pMax, format, imGuiSliderFlags);
    }

    public static boolean inputText(String label, ImString text) {
        return preInputText(false, label, null, text);
    }

    public static boolean inputText(String label, ImString text, int imGuiInputTextFlags) {
        return preInputText(false, label, null, text, 0.0f, 0.0f, imGuiInputTextFlags);
    }

    public static boolean inputText(String label, ImString text, int imGuiInputTextFlags, ImGuiInputTextCallback callback) {
        return preInputText(false, label, null, text, 0.0f, 0.0f, imGuiInputTextFlags, callback);
    }

    public static boolean inputTextMultiline(String label, ImString text) {
        return preInputText(true, label, null, text);
    }

    public static boolean inputTextMultiline(String label, ImString text, float width, float height) {
        return preInputText(true, label, null, text, width, height);
    }

    public static boolean inputTextMultiline(String label, ImString text, int imGuiInputTextFlags) {
        return preInputText(true, label, null, text, 0.0f, 0.0f, imGuiInputTextFlags);
    }

    public static boolean inputTextMultiline(String label, ImString text, int imGuiInputTextFlags, ImGuiInputTextCallback callback) {
        return preInputText(true, label, null, text, 0.0f, 0.0f, imGuiInputTextFlags, callback);
    }

    public static boolean inputTextMultiline(String label, ImString text, float width, float height, int imGuiInputTextFlags) {
        return preInputText(true, label, null, text, width, height, imGuiInputTextFlags);
    }

    public static boolean inputTextMultiline(String label, ImString text, float width, float height, int imGuiInputTextFlags, ImGuiInputTextCallback callback) {
        return preInputText(true, label, null, text, width, height, imGuiInputTextFlags, callback);
    }

    public static boolean inputTextWithHint(String label, String hint, ImString text) {
        return preInputText(false, label, hint, text);
    }

    public static boolean inputTextWithHint(String label, String hint, ImString text, int imGuiInputTextFlags) {
        return preInputText(false, label, hint, text, 0.0f, 0.0f, imGuiInputTextFlags);
    }

    public static boolean inputTextWithHint(String label, String hint, ImString text, int imGuiInputTextFlags, ImGuiInputTextCallback callback) {
        return preInputText(false, label, hint, text, 0.0f, 0.0f, imGuiInputTextFlags, callback);
    }

    private static boolean preInputText(boolean multiline, String label, String hint, ImString text) {
        return preInputText(multiline, label, hint, text, 0.0f, 0.0f);
    }

    private static boolean preInputText(boolean multiline, String label, String hint, ImString text, float width, float height) {
        return preInputText(multiline, label, hint, text, width, height, 0);
    }

    private static boolean preInputText(boolean multiline, String label, String hint, ImString text, float width, float height, int flags) {
        return preInputText(multiline, label, hint, text, width, height, flags, null);
    }

    private static boolean preInputText(boolean multiline, String label, String hint, ImString text, float width, float height, int flags, ImGuiInputTextCallback callback) {
        ImString.InputData inputData = text.inputData;
        String hintLabel = hint;
        if (hintLabel == null) {
            hintLabel = "";
        }
        return nInputText(multiline, hint != null, label, hintLabel, text, text.getData(), text.getData().length, width, height, flags, inputData, inputData.allowedChars, inputData.isResizable, callback);
    }

    public static boolean inputFloat(String label, ImFloat v) {
        return nInputFloat(label, v != null ? v.getData() : null);
    }

    public static boolean inputFloat(String label, ImFloat v, float step) {
        return nInputFloat(label, v != null ? v.getData() : null, step);
    }

    public static boolean inputFloat(String label, ImFloat v, float step, float stepFast) {
        return nInputFloat(label, v != null ? v.getData() : null, step, stepFast);
    }

    public static boolean inputFloat(String label, ImFloat v, float step, float stepFast, String format) {
        return nInputFloat(label, v != null ? v.getData() : null, step, stepFast, format);
    }

    public static boolean inputFloat(String label, ImFloat v, float step, float stepFast, String format, int imGuiInputTextFlags) {
        return nInputFloat(label, v != null ? v.getData() : null, step, stepFast, format, imGuiInputTextFlags);
    }

    public static boolean inputFloat(String label, ImFloat v, float step, float stepFast, int imGuiInputTextFlags) {
        return nInputFloat(label, v != null ? v.getData() : null, step, stepFast, imGuiInputTextFlags);
    }

    public static boolean inputFloat2(String label, float[] v) {
        return nInputFloat2(label, v);
    }

    public static boolean inputFloat2(String label, float[] v, String format) {
        return nInputFloat2(label, v, format);
    }

    public static boolean inputFloat2(String label, float[] v, String format, int imGuiInputTextFlags) {
        return nInputFloat2(label, v, format, imGuiInputTextFlags);
    }

    public static boolean inputFloat2(String label, float[] v, int imGuiInputTextFlags) {
        return nInputFloat2(label, v, imGuiInputTextFlags);
    }

    public static boolean inputFloat3(String label, float[] v) {
        return nInputFloat3(label, v);
    }

    public static boolean inputFloat3(String label, float[] v, String format) {
        return nInputFloat3(label, v, format);
    }

    public static boolean inputFloat3(String label, float[] v, String format, int imGuiInputTextFlags) {
        return nInputFloat3(label, v, format, imGuiInputTextFlags);
    }

    public static boolean inputFloat3(String label, float[] v, int imGuiInputTextFlags) {
        return nInputFloat3(label, v, imGuiInputTextFlags);
    }

    public static boolean inputFloat4(String label, float[] v) {
        return nInputFloat4(label, v);
    }

    public static boolean inputFloat4(String label, float[] v, String format) {
        return nInputFloat4(label, v, format);
    }

    public static boolean inputFloat4(String label, float[] v, String format, int imGuiInputTextFlags) {
        return nInputFloat4(label, v, format, imGuiInputTextFlags);
    }

    public static boolean inputFloat4(String label, float[] v, int imGuiInputTextFlags) {
        return nInputFloat4(label, v, imGuiInputTextFlags);
    }

    public static boolean inputInt(String label, ImInt v) {
        return nInputInt(label, v != null ? v.getData() : null);
    }

    public static boolean inputInt(String label, ImInt v, int step) {
        return nInputInt(label, v != null ? v.getData() : null, step);
    }

    public static boolean inputInt(String label, ImInt v, int step, int stepFast) {
        return nInputInt(label, v != null ? v.getData() : null, step, stepFast);
    }

    public static boolean inputInt(String label, ImInt v, int step, int stepFast, int imGuiInputTextFlags) {
        return nInputInt(label, v != null ? v.getData() : null, step, stepFast, imGuiInputTextFlags);
    }

    public static boolean inputInt2(String label, int[] v) {
        return nInputInt2(label, v);
    }

    public static boolean inputInt2(String label, int[] v, int imGuiInputTextFlags) {
        return nInputInt2(label, v, imGuiInputTextFlags);
    }

    public static boolean inputInt3(String label, int[] v) {
        return nInputInt3(label, v);
    }

    public static boolean inputInt3(String label, int[] v, int imGuiInputTextFlags) {
        return nInputInt3(label, v, imGuiInputTextFlags);
    }

    public static boolean inputInt4(String label, int[] v) {
        return nInputInt4(label, v);
    }

    public static boolean inputInt4(String label, int[] v, int imGuiInputTextFlags) {
        return nInputInt4(label, v, imGuiInputTextFlags);
    }

    public static boolean inputDouble(String label, ImDouble v) {
        return nInputDouble(label, v != null ? v.getData() : null);
    }

    public static boolean inputDouble(String label, ImDouble v, double step) {
        return nInputDouble(label, v != null ? v.getData() : null, step);
    }

    public static boolean inputDouble(String label, ImDouble v, double step, double stepFast) {
        return nInputDouble(label, v != null ? v.getData() : null, step, stepFast);
    }

    public static boolean inputDouble(String label, ImDouble v, double step, double stepFast, String format) {
        return nInputDouble(label, v != null ? v.getData() : null, step, stepFast, format);
    }

    public static boolean inputDouble(String label, ImDouble v, double step, double stepFast, String format, int imGuiInputTextFlags) {
        return nInputDouble(label, v != null ? v.getData() : null, step, stepFast, format, imGuiInputTextFlags);
    }

    public static boolean inputDouble(String label, ImDouble v, double step, double stepFast, int imGuiInputTextFlags) {
        return nInputDouble(label, v != null ? v.getData() : null, step, stepFast, imGuiInputTextFlags);
    }

    public static boolean inputScalar(String label, ImShort pData) {
        return nInputScalar(label, pData != null ? pData.getData() : null);
    }

    public static boolean inputScalar(String label, ImShort pData, short pStep) {
        return nInputScalar(label, pData != null ? pData.getData() : null, pStep);
    }

    public static boolean inputScalar(String label, ImShort pData, short pStep, short pStepFast) {
        return nInputScalar(label, pData != null ? pData.getData() : null, pStep, pStepFast);
    }

    public static boolean inputScalar(String label, ImShort pData, short pStep, short pStepFast, String format) {
        return nInputScalar(label, pData != null ? pData.getData() : null, pStep, pStepFast, format);
    }

    public static boolean inputScalar(String label, ImShort pData, short pStep, short pStepFast, String format, int imGuiSliderFlags) {
        return nInputScalar(label, pData != null ? pData.getData() : null, pStep, pStepFast, format, imGuiSliderFlags);
    }

    public static boolean inputScalar(String label, ImInt pData) {
        return nInputScalar(label, pData != null ? pData.getData() : null);
    }

    public static boolean inputScalar(String label, ImInt pData, int pStep) {
        return nInputScalar(label, pData != null ? pData.getData() : null, pStep);
    }

    public static boolean inputScalar(String label, ImInt pData, int pStep, int pStepFast) {
        return nInputScalar(label, pData != null ? pData.getData() : null, pStep, pStepFast);
    }

    public static boolean inputScalar(String label, ImInt pData, int pStep, int pStepFast, String format) {
        return nInputScalar(label, pData != null ? pData.getData() : null, pStep, pStepFast, format);
    }

    public static boolean inputScalar(String label, ImInt pData, int pStep, int pStepFast, String format, int imGuiSliderFlags) {
        return nInputScalar(label, pData != null ? pData.getData() : null, pStep, pStepFast, format, imGuiSliderFlags);
    }

    public static boolean inputScalar(String label, ImLong pData) {
        return nInputScalar(label, pData != null ? pData.getData() : null);
    }

    public static boolean inputScalar(String label, ImLong pData, long pStep) {
        return nInputScalar(label, pData != null ? pData.getData() : null, pStep);
    }

    public static boolean inputScalar(String label, ImLong pData, long pStep, long pStepFast) {
        return nInputScalar(label, pData != null ? pData.getData() : null, pStep, pStepFast);
    }

    public static boolean inputScalar(String label, ImLong pData, long pStep, long pStepFast, String format) {
        return nInputScalar(label, pData != null ? pData.getData() : null, pStep, pStepFast, format);
    }

    public static boolean inputScalar(String label, ImLong pData, long pStep, long pStepFast, String format, int imGuiSliderFlags) {
        return nInputScalar(label, pData != null ? pData.getData() : null, pStep, pStepFast, format, imGuiSliderFlags);
    }

    public static boolean inputScalar(String label, ImFloat pData) {
        return nInputScalar(label, pData != null ? pData.getData() : null);
    }

    public static boolean inputScalar(String label, ImFloat pData, float pStep) {
        return nInputScalar(label, pData != null ? pData.getData() : null, pStep);
    }

    public static boolean inputScalar(String label, ImFloat pData, float pStep, float pStepFast) {
        return nInputScalar(label, pData != null ? pData.getData() : null, pStep, pStepFast);
    }

    public static boolean inputScalar(String label, ImFloat pData, float pStep, float pStepFast, String format) {
        return nInputScalar(label, pData != null ? pData.getData() : null, pStep, pStepFast, format);
    }

    public static boolean inputScalar(String label, ImFloat pData, float pStep, float pStepFast, String format, int imGuiSliderFlags) {
        return nInputScalar(label, pData != null ? pData.getData() : null, pStep, pStepFast, format, imGuiSliderFlags);
    }

    public static boolean inputScalar(String label, ImDouble pData) {
        return nInputScalar(label, pData != null ? pData.getData() : null);
    }

    public static boolean inputScalar(String label, ImDouble pData, double pStep) {
        return nInputScalar(label, pData != null ? pData.getData() : null, pStep);
    }

    public static boolean inputScalar(String label, ImDouble pData, double pStep, double pStepFast) {
        return nInputScalar(label, pData != null ? pData.getData() : null, pStep, pStepFast);
    }

    public static boolean inputScalar(String label, ImDouble pData, double pStep, double pStepFast, String format) {
        return nInputScalar(label, pData != null ? pData.getData() : null, pStep, pStepFast, format);
    }

    public static boolean inputScalar(String label, ImDouble pData, double pStep, double pStepFast, String format, int imGuiSliderFlags) {
        return nInputScalar(label, pData != null ? pData.getData() : null, pStep, pStepFast, format, imGuiSliderFlags);
    }

    public static boolean inputScalar(String label, int dataType, ImShort pData) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null);
    }

    public static boolean inputScalar(String label, int dataType, ImShort pData, short pStep) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null, pStep);
    }

    public static boolean inputScalar(String label, int dataType, ImShort pData, short pStep, short pStepFast) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null, pStep, pStepFast);
    }

    public static boolean inputScalar(String label, int dataType, ImShort pData, short pStep, short pStepFast, String format) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null, pStep, pStepFast, format);
    }

    public static boolean inputScalar(String label, int dataType, ImShort pData, short pStep, short pStepFast, String format, int imGuiSliderFlags) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null, pStep, pStepFast, format, imGuiSliderFlags);
    }

    public static boolean inputScalar(String label, int dataType, ImInt pData) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null);
    }

    public static boolean inputScalar(String label, int dataType, ImInt pData, int pStep) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null, pStep);
    }

    public static boolean inputScalar(String label, int dataType, ImInt pData, int pStep, int pStepFast) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null, pStep, pStepFast);
    }

    public static boolean inputScalar(String label, int dataType, ImInt pData, int pStep, int pStepFast, String format) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null, pStep, pStepFast, format);
    }

    public static boolean inputScalar(String label, int dataType, ImInt pData, int pStep, int pStepFast, String format, int imGuiSliderFlags) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null, pStep, pStepFast, format, imGuiSliderFlags);
    }

    public static boolean inputScalar(String label, int dataType, ImLong pData) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null);
    }

    public static boolean inputScalar(String label, int dataType, ImLong pData, long pStep) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null, pStep);
    }

    public static boolean inputScalar(String label, int dataType, ImLong pData, long pStep, long pStepFast) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null, pStep, pStepFast);
    }

    public static boolean inputScalar(String label, int dataType, ImLong pData, long pStep, long pStepFast, String format) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null, pStep, pStepFast, format);
    }

    public static boolean inputScalar(String label, int dataType, ImLong pData, long pStep, long pStepFast, String format, int imGuiSliderFlags) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null, pStep, pStepFast, format, imGuiSliderFlags);
    }

    public static boolean inputScalar(String label, int dataType, ImFloat pData) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null);
    }

    public static boolean inputScalar(String label, int dataType, ImFloat pData, float pStep) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null, pStep);
    }

    public static boolean inputScalar(String label, int dataType, ImFloat pData, float pStep, float pStepFast) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null, pStep, pStepFast);
    }

    public static boolean inputScalar(String label, int dataType, ImFloat pData, float pStep, float pStepFast, String format) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null, pStep, pStepFast, format);
    }

    public static boolean inputScalar(String label, int dataType, ImFloat pData, float pStep, float pStepFast, String format, int imGuiSliderFlags) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null, pStep, pStepFast, format, imGuiSliderFlags);
    }

    public static boolean inputScalar(String label, int dataType, ImDouble pData) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null);
    }

    public static boolean inputScalar(String label, int dataType, ImDouble pData, double pStep) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null, pStep);
    }

    public static boolean inputScalar(String label, int dataType, ImDouble pData, double pStep, double pStepFast) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null, pStep, pStepFast);
    }

    public static boolean inputScalar(String label, int dataType, ImDouble pData, double pStep, double pStepFast, String format) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null, pStep, pStepFast, format);
    }

    public static boolean inputScalar(String label, int dataType, ImDouble pData, double pStep, double pStepFast, String format, int imGuiSliderFlags) {
        return nInputScalar(label, dataType, pData != null ? pData.getData() : null, pStep, pStepFast, format, imGuiSliderFlags);
    }

    public static boolean inputScalarN(String label, short[] pData, int components) {
        return nInputScalarN(label, pData, components);
    }

    public static boolean inputScalarN(String label, short[] pData, int components, short pStep) {
        return nInputScalarN(label, pData, components, pStep);
    }

    public static boolean inputScalarN(String label, short[] pData, int components, short pStep, short pStepFast) {
        return nInputScalarN(label, pData, components, pStep, pStepFast);
    }

    public static boolean inputScalarN(String label, short[] pData, int components, short pStep, short pStepFast, String format) {
        return nInputScalarN(label, pData, components, pStep, pStepFast, format);
    }

    public static boolean inputScalarN(String label, short[] pData, int components, short pStep, short pStepFast, String format, int imGuiSliderFlags) {
        return nInputScalarN(label, pData, components, pStep, pStepFast, format, imGuiSliderFlags);
    }

    public static boolean inputScalarN(String label, int[] pData, int components) {
        return nInputScalarN(label, pData, components);
    }

    public static boolean inputScalarN(String label, int[] pData, int components, int pStep) {
        return nInputScalarN(label, pData, components, pStep);
    }

    public static boolean inputScalarN(String label, int[] pData, int components, int pStep, int pStepFast) {
        return nInputScalarN(label, pData, components, pStep, pStepFast);
    }

    public static boolean inputScalarN(String label, int[] pData, int components, int pStep, int pStepFast, String format) {
        return nInputScalarN(label, pData, components, pStep, pStepFast, format);
    }

    public static boolean inputScalarN(String label, int[] pData, int components, int pStep, int pStepFast, String format, int imGuiSliderFlags) {
        return nInputScalarN(label, pData, components, pStep, pStepFast, format, imGuiSliderFlags);
    }

    public static boolean inputScalarN(String label, long[] pData, int components) {
        return nInputScalarN(label, pData, components);
    }

    public static boolean inputScalarN(String label, long[] pData, int components, long pStep) {
        return nInputScalarN(label, pData, components, pStep);
    }

    public static boolean inputScalarN(String label, long[] pData, int components, long pStep, long pStepFast) {
        return nInputScalarN(label, pData, components, pStep, pStepFast);
    }

    public static boolean inputScalarN(String label, long[] pData, int components, long pStep, long pStepFast, String format) {
        return nInputScalarN(label, pData, components, pStep, pStepFast, format);
    }

    public static boolean inputScalarN(String label, long[] pData, int components, long pStep, long pStepFast, String format, int imGuiSliderFlags) {
        return nInputScalarN(label, pData, components, pStep, pStepFast, format, imGuiSliderFlags);
    }

    public static boolean inputScalarN(String label, float[] pData, int components) {
        return nInputScalarN(label, pData, components);
    }

    public static boolean inputScalarN(String label, float[] pData, int components, float pStep) {
        return nInputScalarN(label, pData, components, pStep);
    }

    public static boolean inputScalarN(String label, float[] pData, int components, float pStep, float pStepFast) {
        return nInputScalarN(label, pData, components, pStep, pStepFast);
    }

    public static boolean inputScalarN(String label, float[] pData, int components, float pStep, float pStepFast, String format) {
        return nInputScalarN(label, pData, components, pStep, pStepFast, format);
    }

    public static boolean inputScalarN(String label, float[] pData, int components, float pStep, float pStepFast, String format, int imGuiSliderFlags) {
        return nInputScalarN(label, pData, components, pStep, pStepFast, format, imGuiSliderFlags);
    }

    public static boolean inputScalarN(String label, double[] pData, int components) {
        return nInputScalarN(label, pData, components);
    }

    public static boolean inputScalarN(String label, double[] pData, int components, double pStep) {
        return nInputScalarN(label, pData, components, pStep);
    }

    public static boolean inputScalarN(String label, double[] pData, int components, double pStep, double pStepFast) {
        return nInputScalarN(label, pData, components, pStep, pStepFast);
    }

    public static boolean inputScalarN(String label, double[] pData, int components, double pStep, double pStepFast, String format) {
        return nInputScalarN(label, pData, components, pStep, pStepFast, format);
    }

    public static boolean inputScalarN(String label, double[] pData, int components, double pStep, double pStepFast, String format, int imGuiSliderFlags) {
        return nInputScalarN(label, pData, components, pStep, pStepFast, format, imGuiSliderFlags);
    }

    public static boolean inputScalarN(String label, int dataType, short[] pData, int components) {
        return nInputScalarN(label, dataType, pData, components);
    }

    public static boolean inputScalarN(String label, int dataType, short[] pData, int components, short pStep) {
        return nInputScalarN(label, dataType, pData, components, pStep);
    }

    public static boolean inputScalarN(String label, int dataType, short[] pData, int components, short pStep, short pStepFast) {
        return nInputScalarN(label, dataType, pData, components, pStep, pStepFast);
    }

    public static boolean inputScalarN(String label, int dataType, short[] pData, int components, short pStep, short pStepFast, String format) {
        return nInputScalarN(label, dataType, pData, components, pStep, pStepFast, format);
    }

    public static boolean inputScalarN(String label, int dataType, short[] pData, int components, short pStep, short pStepFast, String format, int imGuiSliderFlags) {
        return nInputScalarN(label, dataType, pData, components, pStep, pStepFast, format, imGuiSliderFlags);
    }

    public static boolean inputScalarN(String label, int dataType, int[] pData, int components) {
        return nInputScalarN(label, dataType, pData, components);
    }

    public static boolean inputScalarN(String label, int dataType, int[] pData, int components, int pStep) {
        return nInputScalarN(label, dataType, pData, components, pStep);
    }

    public static boolean inputScalarN(String label, int dataType, int[] pData, int components, int pStep, int pStepFast) {
        return nInputScalarN(label, dataType, pData, components, pStep, pStepFast);
    }

    public static boolean inputScalarN(String label, int dataType, int[] pData, int components, int pStep, int pStepFast, String format) {
        return nInputScalarN(label, dataType, pData, components, pStep, pStepFast, format);
    }

    public static boolean inputScalarN(String label, int dataType, int[] pData, int components, int pStep, int pStepFast, String format, int imGuiSliderFlags) {
        return nInputScalarN(label, dataType, pData, components, pStep, pStepFast, format, imGuiSliderFlags);
    }

    public static boolean inputScalarN(String label, int dataType, long[] pData, int components) {
        return nInputScalarN(label, dataType, pData, components);
    }

    public static boolean inputScalarN(String label, int dataType, long[] pData, int components, long pStep) {
        return nInputScalarN(label, dataType, pData, components, pStep);
    }

    public static boolean inputScalarN(String label, int dataType, long[] pData, int components, long pStep, long pStepFast) {
        return nInputScalarN(label, dataType, pData, components, pStep, pStepFast);
    }

    public static boolean inputScalarN(String label, int dataType, long[] pData, int components, long pStep, long pStepFast, String format) {
        return nInputScalarN(label, dataType, pData, components, pStep, pStepFast, format);
    }

    public static boolean inputScalarN(String label, int dataType, long[] pData, int components, long pStep, long pStepFast, String format, int imGuiSliderFlags) {
        return nInputScalarN(label, dataType, pData, components, pStep, pStepFast, format, imGuiSliderFlags);
    }

    public static boolean inputScalarN(String label, int dataType, float[] pData, int components) {
        return nInputScalarN(label, dataType, pData, components);
    }

    public static boolean inputScalarN(String label, int dataType, float[] pData, int components, float pStep) {
        return nInputScalarN(label, dataType, pData, components, pStep);
    }

    public static boolean inputScalarN(String label, int dataType, float[] pData, int components, float pStep, float pStepFast) {
        return nInputScalarN(label, dataType, pData, components, pStep, pStepFast);
    }

    public static boolean inputScalarN(String label, int dataType, float[] pData, int components, float pStep, float pStepFast, String format) {
        return nInputScalarN(label, dataType, pData, components, pStep, pStepFast, format);
    }

    public static boolean inputScalarN(String label, int dataType, float[] pData, int components, float pStep, float pStepFast, String format, int imGuiSliderFlags) {
        return nInputScalarN(label, dataType, pData, components, pStep, pStepFast, format, imGuiSliderFlags);
    }

    public static boolean inputScalarN(String label, int dataType, double[] pData, int components) {
        return nInputScalarN(label, dataType, pData, components);
    }

    public static boolean inputScalarN(String label, int dataType, double[] pData, int components, double pStep) {
        return nInputScalarN(label, dataType, pData, components, pStep);
    }

    public static boolean inputScalarN(String label, int dataType, double[] pData, int components, double pStep, double pStepFast) {
        return nInputScalarN(label, dataType, pData, components, pStep, pStepFast);
    }

    public static boolean inputScalarN(String label, int dataType, double[] pData, int components, double pStep, double pStepFast, String format) {
        return nInputScalarN(label, dataType, pData, components, pStep, pStepFast, format);
    }

    public static boolean inputScalarN(String label, int dataType, double[] pData, int components, double pStep, double pStepFast, String format, int imGuiSliderFlags) {
        return nInputScalarN(label, dataType, pData, components, pStep, pStepFast, format, imGuiSliderFlags);
    }

    public static boolean colorEdit3(String label, float[] col) {
        return nColorEdit3(label, col);
    }

    public static boolean colorEdit3(String label, float[] col, int imGuiColorEditFlags) {
        return nColorEdit3(label, col, imGuiColorEditFlags);
    }

    public static boolean colorEdit4(String label, float[] col) {
        return nColorEdit4(label, col);
    }

    public static boolean colorEdit4(String label, float[] col, int imGuiColorEditFlags) {
        return nColorEdit4(label, col, imGuiColorEditFlags);
    }

    public static boolean colorPicker3(String label, float[] col) {
        return nColorPicker3(label, col);
    }

    public static boolean colorPicker3(String label, float[] col, int imGuiColorEditFlags) {
        return nColorPicker3(label, col, imGuiColorEditFlags);
    }

    public static boolean colorPicker4(String label, float[] col) {
        return nColorPicker4(label, col);
    }

    public static boolean colorPicker4(String label, float[] col, int imGuiColorEditFlags) {
        return nColorPicker4(label, col, imGuiColorEditFlags);
    }

    public static boolean colorPicker4(String label, float[] col, int imGuiColorEditFlags, float[] refCol) {
        return nColorPicker4(label, col, imGuiColorEditFlags, refCol);
    }

    public static boolean colorPicker4(String label, float[] col, float[] refCol) {
        return nColorPicker4(label, col, refCol);
    }

    public static boolean colorButton(String descId, ImVec4 col) {
        return nColorButton(descId, col.x, col.y, col.z, col.w);
    }

    public static boolean colorButton(String descId, float colX, float colY, float colZ, float colW) {
        return nColorButton(descId, colX, colY, colZ, colW);
    }

    public static boolean colorButton(String descId, ImVec4 col, int imGuiColorEditFlags) {
        return nColorButton(descId, col.x, col.y, col.z, col.w, imGuiColorEditFlags);
    }

    public static boolean colorButton(String descId, float colX, float colY, float colZ, float colW, int imGuiColorEditFlags) {
        return nColorButton(descId, colX, colY, colZ, colW, imGuiColorEditFlags);
    }

    public static boolean colorButton(String descId, ImVec4 col, int imGuiColorEditFlags, ImVec2 size) {
        return nColorButton(descId, col.x, col.y, col.z, col.w, imGuiColorEditFlags, size.x, size.y);
    }

    public static boolean colorButton(String descId, float colX, float colY, float colZ, float colW, int imGuiColorEditFlags, float sizeX, float sizeY) {
        return nColorButton(descId, colX, colY, colZ, colW, imGuiColorEditFlags, sizeX, sizeY);
    }

    public static boolean colorButton(String descId, ImVec4 col, ImVec2 size) {
        return nColorButton(descId, col.x, col.y, col.z, col.w, size.x, size.y);
    }

    public static boolean colorButton(String descId, float colX, float colY, float colZ, float colW, float sizeX, float sizeY) {
        return nColorButton(descId, colX, colY, colZ, colW, sizeX, sizeY);
    }

    @Deprecated
    public static boolean colorButton(String descId, float[] col) {
        return nColorButton(descId, col);
    }

    @Deprecated
    public static boolean colorButton(String descId, float[] col, int imGuiColorEditFlags) {
        return nColorButton(descId, col, imGuiColorEditFlags);
    }

    @Deprecated
    public static boolean colorButton(String descId, float[] col, int imGuiColorEditFlags, ImVec2 size) {
        return nColorButton(descId, col, imGuiColorEditFlags, size.x, size.y);
    }

    @Deprecated
    public static boolean colorButton(String descId, float[] col, int imGuiColorEditFlags, float sizeX, float sizeY) {
        return nColorButton(descId, col, imGuiColorEditFlags, sizeX, sizeY);
    }

    @Deprecated
    public static boolean colorButton(String descId, float[] col, ImVec2 size) {
        return nColorButton(descId, col, size.x, size.y);
    }

    @Deprecated
    public static boolean colorButton(String descId, float[] col, float sizeX, float sizeY) {
        return nColorButton(descId, col, sizeX, sizeY);
    }

    public static void setColorEditOptions(int imGuiColorEditFlags) {
        nSetColorEditOptions(imGuiColorEditFlags);
    }

    public static boolean treeNode(String label) {
        return nTreeNode(label);
    }

    public static boolean treeNode(String strId, String label) {
        return nTreeNode(strId, label);
    }

    public static boolean treeNode(long ptrId, String label) {
        return nTreeNode(ptrId, label);
    }

    public static boolean treeNodeEx(String label) {
        return nTreeNodeEx(label);
    }

    public static boolean treeNodeEx(String label, int flags) {
        return nTreeNodeEx(label, flags);
    }

    public static boolean treeNodeEx(String strId, int flags, String label) {
        return nTreeNodeEx(strId, flags, label);
    }

    public static boolean treeNodeEx(long ptrId, int flags, String label) {
        return nTreeNodeEx(ptrId, flags, label);
    }

    public static void treePush(String strId) {
        nTreePush(strId);
    }

    public static void treePush(long ptrId) {
        nTreePush(ptrId);
    }

    public static void treePop() {
        nTreePop();
    }

    public static float getTreeNodeToLabelSpacing() {
        return nGetTreeNodeToLabelSpacing();
    }

    public static boolean collapsingHeader(String label) {
        return nCollapsingHeader(label);
    }

    public static boolean collapsingHeader(String label, int imGuiTreeNodeFlags) {
        return nCollapsingHeader(label, imGuiTreeNodeFlags);
    }

    public static boolean collapsingHeader(String label, ImBoolean pVisible) {
        return nCollapsingHeader(label, pVisible != null ? pVisible.getData() : null);
    }

    public static boolean collapsingHeader(String label, ImBoolean pVisible, int imGuiTreeNodeFlags) {
        return nCollapsingHeader(label, pVisible != null ? pVisible.getData() : null, imGuiTreeNodeFlags);
    }

    public static void setNextItemOpen(boolean isOpen) {
        nSetNextItemOpen(isOpen);
    }

    public static void setNextItemOpen(boolean isOpen, int cond) {
        nSetNextItemOpen(isOpen, cond);
    }

    public static boolean selectable(String label) {
        return nSelectable(label);
    }

    public static boolean selectable(String label, boolean selected) {
        return nSelectable(label, selected);
    }

    public static boolean selectable(String label, boolean selected, int imGuiSelectableFlags) {
        return nSelectable(label, selected, imGuiSelectableFlags);
    }

    public static boolean selectable(String label, boolean selected, int imGuiSelectableFlags, ImVec2 size) {
        return nSelectable(label, selected, imGuiSelectableFlags, size.x, size.y);
    }

    public static boolean selectable(String label, boolean selected, int imGuiSelectableFlags, float sizeX, float sizeY) {
        return nSelectable(label, selected, imGuiSelectableFlags, sizeX, sizeY);
    }

    public static boolean selectable(String label, int imGuiSelectableFlags, ImVec2 size) {
        return nSelectable(label, imGuiSelectableFlags, size.x, size.y);
    }

    public static boolean selectable(String label, int imGuiSelectableFlags, float sizeX, float sizeY) {
        return nSelectable(label, imGuiSelectableFlags, sizeX, sizeY);
    }

    public static boolean selectable(String label, ImVec2 size) {
        return nSelectable(label, size.x, size.y);
    }

    public static boolean selectable(String label, float sizeX, float sizeY) {
        return nSelectable(label, sizeX, sizeY);
    }

    public static boolean selectable(String label, boolean selected, ImVec2 size) {
        return nSelectable(label, selected, size.x, size.y);
    }

    public static boolean selectable(String label, boolean selected, float sizeX, float sizeY) {
        return nSelectable(label, selected, sizeX, sizeY);
    }

    public static boolean selectable(String label, ImBoolean pSelected) {
        return nSelectable(label, pSelected != null ? pSelected.getData() : null);
    }

    public static boolean selectable(String label, ImBoolean pSelected, int imGuiSelectableFlags) {
        return nSelectable(label, pSelected != null ? pSelected.getData() : null, imGuiSelectableFlags);
    }

    public static boolean selectable(String label, ImBoolean pSelected, int imGuiSelectableFlags, ImVec2 size) {
        return nSelectable(label, pSelected != null ? pSelected.getData() : null, imGuiSelectableFlags, size.x, size.y);
    }

    public static boolean selectable(String label, ImBoolean pSelected, int imGuiSelectableFlags, float sizeX, float sizeY) {
        return nSelectable(label, pSelected != null ? pSelected.getData() : null, imGuiSelectableFlags, sizeX, sizeY);
    }

    public static boolean selectable(String label, ImBoolean pSelected, ImVec2 size) {
        return nSelectable(label, pSelected != null ? pSelected.getData() : null, size.x, size.y);
    }

    public static boolean selectable(String label, ImBoolean pSelected, float sizeX, float sizeY) {
        return nSelectable(label, pSelected != null ? pSelected.getData() : null, sizeX, sizeY);
    }

    public static boolean beginListBox(String label) {
        return nBeginListBox(label);
    }

    public static boolean beginListBox(String label, ImVec2 size) {
        return nBeginListBox(label, size.x, size.y);
    }

    public static boolean beginListBox(String label, float sizeX, float sizeY) {
        return nBeginListBox(label, sizeX, sizeY);
    }

    public static void endListBox() {
        nEndListBox();
    }

    public static void listBox(String label, ImInt currentItem, String[] items) {
        nListBox(label, currentItem != null ? currentItem.getData() : null, items, items.length);
    }

    public static void listBox(String label, ImInt currentItem, String[] items, int heightInItems) {
        nListBox(label, currentItem != null ? currentItem.getData() : null, items, items.length, heightInItems);
    }

    public static void plotLines(String label, float[] values, int valuesCount) {
        nPlotLines(label, values, valuesCount);
    }

    public static void plotLines(String label, float[] values, int valuesCount, int valuesOffset) {
        nPlotLines(label, values, valuesCount, valuesOffset);
    }

    public static void plotLines(String label, float[] values, int valuesCount, int valuesOffset, String overlayText) {
        nPlotLines(label, values, valuesCount, valuesOffset, overlayText);
    }

    public static void plotLines(String label, float[] values, int valuesCount, int valuesOffset, String overlayText, float scaleMin) {
        nPlotLines(label, values, valuesCount, valuesOffset, overlayText, scaleMin);
    }

    public static void plotLines(String label, float[] values, int valuesCount, int valuesOffset, String overlayText, float scaleMin, float scaleMax) {
        nPlotLines(label, values, valuesCount, valuesOffset, overlayText, scaleMin, scaleMax);
    }

    public static void plotLines(String label, float[] values, int valuesCount, int valuesOffset, String overlayText, float scaleMin, float scaleMax, ImVec2 graphSize) {
        nPlotLines(label, values, valuesCount, valuesOffset, overlayText, scaleMin, scaleMax, graphSize.x, graphSize.y);
    }

    public static void plotLines(String label, float[] values, int valuesCount, int valuesOffset, String overlayText, float scaleMin, float scaleMax, float graphSizeX, float graphSizeY) {
        nPlotLines(label, values, valuesCount, valuesOffset, overlayText, scaleMin, scaleMax, graphSizeX, graphSizeY);
    }

    public static void plotLines(String label, float[] values, int valuesCount, int valuesOffset, String overlayText, float scaleMin, float scaleMax, ImVec2 graphSize, int stride) {
        nPlotLines(label, values, valuesCount, valuesOffset, overlayText, scaleMin, scaleMax, graphSize.x, graphSize.y, stride);
    }

    public static void plotLines(String label, float[] values, int valuesCount, int valuesOffset, String overlayText, float scaleMin, float scaleMax, float graphSizeX, float graphSizeY, int stride) {
        nPlotLines(label, values, valuesCount, valuesOffset, overlayText, scaleMin, scaleMax, graphSizeX, graphSizeY, stride);
    }

    public static void plotLines(String label, float[] values, int valuesCount, String overlayText, float scaleMin, float scaleMax, ImVec2 graphSize, int stride) {
        nPlotLines(label, values, valuesCount, overlayText, scaleMin, scaleMax, graphSize.x, graphSize.y, stride);
    }

    public static void plotLines(String label, float[] values, int valuesCount, String overlayText, float scaleMin, float scaleMax, float graphSizeX, float graphSizeY, int stride) {
        nPlotLines(label, values, valuesCount, overlayText, scaleMin, scaleMax, graphSizeX, graphSizeY, stride);
    }

    public static void plotLines(String label, float[] values, int valuesCount, float scaleMin, float scaleMax, ImVec2 graphSize, int stride) {
        nPlotLines(label, values, valuesCount, scaleMin, scaleMax, graphSize.x, graphSize.y, stride);
    }

    public static void plotLines(String label, float[] values, int valuesCount, float scaleMin, float scaleMax, float graphSizeX, float graphSizeY, int stride) {
        nPlotLines(label, values, valuesCount, scaleMin, scaleMax, graphSizeX, graphSizeY, stride);
    }

    public static void plotLines(String label, float[] values, int valuesCount, int valuesOffset, float scaleMin, float scaleMax, ImVec2 graphSize, int stride) {
        nPlotLines(label, values, valuesCount, valuesOffset, scaleMin, scaleMax, graphSize.x, graphSize.y, stride);
    }

    public static void plotLines(String label, float[] values, int valuesCount, int valuesOffset, float scaleMin, float scaleMax, float graphSizeX, float graphSizeY, int stride) {
        nPlotLines(label, values, valuesCount, valuesOffset, scaleMin, scaleMax, graphSizeX, graphSizeY, stride);
    }

    public static void plotLines(String label, float[] values, int valuesCount, int valuesOffset, String overlayText, float scaleMin, float scaleMax, int stride) {
        nPlotLines(label, values, valuesCount, valuesOffset, overlayText, scaleMin, scaleMax, stride);
    }

    public static void plotHistogram(String label, float[] values, int valuesCount) {
        nPlotHistogram(label, values, valuesCount);
    }

    public static void plotHistogram(String label, float[] values, int valuesCount, int valuesOffset) {
        nPlotHistogram(label, values, valuesCount, valuesOffset);
    }

    public static void plotHistogram(String label, float[] values, int valuesCount, int valuesOffset, String overlayText) {
        nPlotHistogram(label, values, valuesCount, valuesOffset, overlayText);
    }

    public static void plotHistogram(String label, float[] values, int valuesCount, int valuesOffset, String overlayText, float scaleMin) {
        nPlotHistogram(label, values, valuesCount, valuesOffset, overlayText, scaleMin);
    }

    public static void plotHistogram(String label, float[] values, int valuesCount, int valuesOffset, String overlayText, float scaleMin, float scaleMax) {
        nPlotHistogram(label, values, valuesCount, valuesOffset, overlayText, scaleMin, scaleMax);
    }

    public static void plotHistogram(String label, float[] values, int valuesCount, int valuesOffset, String overlayText, float scaleMin, float scaleMax, ImVec2 graphSize) {
        nPlotHistogram(label, values, valuesCount, valuesOffset, overlayText, scaleMin, scaleMax, graphSize.x, graphSize.y);
    }

    public static void plotHistogram(String label, float[] values, int valuesCount, int valuesOffset, String overlayText, float scaleMin, float scaleMax, float graphSizeX, float graphSizeY) {
        nPlotHistogram(label, values, valuesCount, valuesOffset, overlayText, scaleMin, scaleMax, graphSizeX, graphSizeY);
    }

    public static void plotHistogram(String label, float[] values, int valuesCount, int valuesOffset, String overlayText, float scaleMin, float scaleMax, ImVec2 graphSize, int stride) {
        nPlotHistogram(label, values, valuesCount, valuesOffset, overlayText, scaleMin, scaleMax, graphSize.x, graphSize.y, stride);
    }

    public static void plotHistogram(String label, float[] values, int valuesCount, int valuesOffset, String overlayText, float scaleMin, float scaleMax, float graphSizeX, float graphSizeY, int stride) {
        nPlotHistogram(label, values, valuesCount, valuesOffset, overlayText, scaleMin, scaleMax, graphSizeX, graphSizeY, stride);
    }

    public static void plotHistogram(String label, float[] values, int valuesCount, String overlayText, float scaleMin, float scaleMax, ImVec2 graphSize, int stride) {
        nPlotHistogram(label, values, valuesCount, overlayText, scaleMin, scaleMax, graphSize.x, graphSize.y, stride);
    }

    public static void plotHistogram(String label, float[] values, int valuesCount, String overlayText, float scaleMin, float scaleMax, float graphSizeX, float graphSizeY, int stride) {
        nPlotHistogram(label, values, valuesCount, overlayText, scaleMin, scaleMax, graphSizeX, graphSizeY, stride);
    }

    public static void plotHistogram(String label, float[] values, int valuesCount, float scaleMin, float scaleMax, ImVec2 graphSize, int stride) {
        nPlotHistogram(label, values, valuesCount, scaleMin, scaleMax, graphSize.x, graphSize.y, stride);
    }

    public static void plotHistogram(String label, float[] values, int valuesCount, float scaleMin, float scaleMax, float graphSizeX, float graphSizeY, int stride) {
        nPlotHistogram(label, values, valuesCount, scaleMin, scaleMax, graphSizeX, graphSizeY, stride);
    }

    public static void plotHistogram(String label, float[] values, int valuesCount, int valuesOffset, float scaleMin, float scaleMax, ImVec2 graphSize, int stride) {
        nPlotHistogram(label, values, valuesCount, valuesOffset, scaleMin, scaleMax, graphSize.x, graphSize.y, stride);
    }

    public static void plotHistogram(String label, float[] values, int valuesCount, int valuesOffset, float scaleMin, float scaleMax, float graphSizeX, float graphSizeY, int stride) {
        nPlotHistogram(label, values, valuesCount, valuesOffset, scaleMin, scaleMax, graphSizeX, graphSizeY, stride);
    }

    public static void plotHistogram(String label, float[] values, int valuesCount, int valuesOffset, String overlayText, float scaleMin, float scaleMax, int stride) {
        nPlotHistogram(label, values, valuesCount, valuesOffset, overlayText, scaleMin, scaleMax, stride);
    }

    public static void value(String prefix, Number value) {
        nValue(prefix, value.toString());
    }

    public static void value(String prefix, float value, String floatFormat) {
        nValue(prefix, String.format(floatFormat, Float.valueOf(value)));
    }

    public static boolean beginMenuBar() {
        return nBeginMenuBar();
    }

    public static void endMenuBar() {
        nEndMenuBar();
    }

    public static boolean beginMainMenuBar() {
        return nBeginMainMenuBar();
    }

    public static void endMainMenuBar() {
        nEndMainMenuBar();
    }

    public static boolean beginMenu(String label) {
        return nBeginMenu(label);
    }

    public static boolean beginMenu(String label, boolean enabled) {
        return nBeginMenu(label, enabled);
    }

    public static void endMenu() {
        nEndMenu();
    }

    public static boolean menuItem(String label) {
        return nMenuItem(label);
    }

    public static boolean menuItem(String label, boolean selected) {
        return nMenuItem(label, selected);
    }

    public static boolean menuItem(String label, boolean selected, boolean enabled) {
        return nMenuItem(label, selected, enabled);
    }

    public static boolean menuItem(String label, String shortcut) {
        return nMenuItem(label, shortcut);
    }

    public static boolean menuItem(String label, String shortcut, boolean selected) {
        return nMenuItem(label, shortcut, selected);
    }

    public static boolean menuItem(String label, String shortcut, boolean selected, boolean enabled) {
        return nMenuItem(label, shortcut, selected, enabled);
    }

    public static boolean menuItem(String label, String shortcut, ImBoolean pSelected) {
        return nMenuItem(label, shortcut, pSelected != null ? pSelected.getData() : null);
    }

    public static boolean menuItem(String label, String shortcut, ImBoolean pSelected, boolean enabled) {
        return nMenuItem(label, shortcut, pSelected != null ? pSelected.getData() : null, enabled);
    }

    public static void beginTooltip() {
        nBeginTooltip();
    }

    public static void endTooltip() {
        nEndTooltip();
    }

    public static void setTooltip(String text) {
        nSetTooltip(text);
    }

    public static boolean beginItemTooltip() {
        return nBeginItemTooltip();
    }

    public static void setItemTooltip(String text) {
        nSetItemTooltip(text);
    }

    public static boolean beginPopup(String strId) {
        return nBeginPopup(strId);
    }

    public static boolean beginPopup(String strId, int imGuiWindowFlags) {
        return nBeginPopup(strId, imGuiWindowFlags);
    }

    public static boolean beginPopupModal(String name) {
        return nBeginPopupModal(name);
    }

    public static boolean beginPopupModal(String name, ImBoolean pOpen) {
        return nBeginPopupModal(name, pOpen != null ? pOpen.getData() : null);
    }

    public static boolean beginPopupModal(String name, ImBoolean pOpen, int imGuiWindowFlags) {
        return nBeginPopupModal(name, pOpen != null ? pOpen.getData() : null, imGuiWindowFlags);
    }

    public static boolean beginPopupModal(String name, int imGuiWindowFlags) {
        return nBeginPopupModal(name, imGuiWindowFlags);
    }

    public static void endPopup() {
        nEndPopup();
    }

    public static void openPopup(String strId) {
        nOpenPopup(strId);
    }

    public static void openPopup(String strId, int imGuiPopupFlags) {
        nOpenPopup(strId, imGuiPopupFlags);
    }

    public static void openPopup(int id) {
        nOpenPopup(id);
    }

    public static void openPopup(int id, int imGuiPopupFlags) {
        nOpenPopup(id, imGuiPopupFlags);
    }

    public static void openPopupOnItemClick() {
        nOpenPopupOnItemClick();
    }

    public static void openPopupOnItemClick(String strId) {
        nOpenPopupOnItemClick(strId);
    }

    public static void openPopupOnItemClick(String strId, int imGuiPopupFlags) {
        nOpenPopupOnItemClick(strId, imGuiPopupFlags);
    }

    public static void openPopupOnItemClick(int imGuiPopupFlags) {
        nOpenPopupOnItemClick(imGuiPopupFlags);
    }

    public static void closeCurrentPopup() {
        nCloseCurrentPopup();
    }

    public static boolean beginPopupContextItem() {
        return nBeginPopupContextItem();
    }

    public static boolean beginPopupContextItem(String strId) {
        return nBeginPopupContextItem(strId);
    }

    public static boolean beginPopupContextItem(String strId, int imGuiPopupFlags) {
        return nBeginPopupContextItem(strId, imGuiPopupFlags);
    }

    public static boolean beginPopupContextItem(int imGuiPopupFlags) {
        return nBeginPopupContextItem(imGuiPopupFlags);
    }

    public static boolean beginPopupContextWindow() {
        return nBeginPopupContextWindow();
    }

    public static boolean beginPopupContextWindow(String strId) {
        return nBeginPopupContextWindow(strId);
    }

    public static boolean beginPopupContextWindow(String strId, int imGuiPopupFlags) {
        return nBeginPopupContextWindow(strId, imGuiPopupFlags);
    }

    public static boolean beginPopupContextWindow(int imGuiPopupFlags) {
        return nBeginPopupContextWindow(imGuiPopupFlags);
    }

    public static boolean beginPopupContextVoid() {
        return nBeginPopupContextVoid();
    }

    public static boolean beginPopupContextVoid(String strId) {
        return nBeginPopupContextVoid(strId);
    }

    public static boolean beginPopupContextVoid(String strId, int imGuiPopupFlags) {
        return nBeginPopupContextVoid(strId, imGuiPopupFlags);
    }

    public static boolean beginPopupContextVoid(int imGuiPopupFlags) {
        return nBeginPopupContextVoid(imGuiPopupFlags);
    }

    public static boolean isPopupOpen(String strId) {
        return nIsPopupOpen(strId);
    }

    public static boolean isPopupOpen(String strId, int imGuiPopupFlags) {
        return nIsPopupOpen(strId, imGuiPopupFlags);
    }

    public static boolean beginTable(String id, int columns) {
        return nBeginTable(id, columns);
    }

    public static boolean beginTable(String id, int columns, int imGuiTableFlags) {
        return nBeginTable(id, columns, imGuiTableFlags);
    }

    public static boolean beginTable(String id, int columns, int imGuiTableFlags, ImVec2 outerSize) {
        return nBeginTable(id, columns, imGuiTableFlags, outerSize.x, outerSize.y);
    }

    public static boolean beginTable(String id, int columns, int imGuiTableFlags, float outerSizeX, float outerSizeY) {
        return nBeginTable(id, columns, imGuiTableFlags, outerSizeX, outerSizeY);
    }

    public static boolean beginTable(String id, int columns, int imGuiTableFlags, ImVec2 outerSize, float innerWidth) {
        return nBeginTable(id, columns, imGuiTableFlags, outerSize.x, outerSize.y, innerWidth);
    }

    public static boolean beginTable(String id, int columns, int imGuiTableFlags, float outerSizeX, float outerSizeY, float innerWidth) {
        return nBeginTable(id, columns, imGuiTableFlags, outerSizeX, outerSizeY, innerWidth);
    }

    public static boolean beginTable(String id, int columns, ImVec2 outerSize, float innerWidth) {
        return nBeginTable(id, columns, outerSize.x, outerSize.y, innerWidth);
    }

    public static boolean beginTable(String id, int columns, float outerSizeX, float outerSizeY, float innerWidth) {
        return nBeginTable(id, columns, outerSizeX, outerSizeY, innerWidth);
    }

    public static boolean beginTable(String id, int columns, float innerWidth) {
        return nBeginTable(id, columns, innerWidth);
    }

    public static boolean beginTable(String id, int columns, int imGuiTableFlags, float innerWidth) {
        return nBeginTable(id, columns, imGuiTableFlags, innerWidth);
    }

    public static void endTable() {
        nEndTable();
    }

    public static void tableNextRow() {
        nTableNextRow();
    }

    public static void tableNextRow(int imGuiTableRowFlags) {
        nTableNextRow(imGuiTableRowFlags);
    }

    public static void tableNextRow(int imGuiTableRowFlags, float minRowHeight) {
        nTableNextRow(imGuiTableRowFlags, minRowHeight);
    }

    public static void tableNextRow(float minRowHeight) {
        nTableNextRow(minRowHeight);
    }

    public static boolean tableNextColumn() {
        return nTableNextColumn();
    }

    public static boolean tableSetColumnIndex(int columnN) {
        return nTableSetColumnIndex(columnN);
    }

    public static void tableSetupColumn(String label) {
        nTableSetupColumn(label);
    }

    public static void tableSetupColumn(String label, int imGuiTableColumnFlags) {
        nTableSetupColumn(label, imGuiTableColumnFlags);
    }

    public static void tableSetupColumn(String label, int imGuiTableColumnFlags, float initWidthOrWeight) {
        nTableSetupColumn(label, imGuiTableColumnFlags, initWidthOrWeight);
    }

    public static void tableSetupColumn(String label, int imGuiTableColumnFlags, float initWidthOrWeight, int userId) {
        nTableSetupColumn(label, imGuiTableColumnFlags, initWidthOrWeight, userId);
    }

    public static void tableSetupColumn(String label, float initWidthOrWeight, int userId) {
        nTableSetupColumn(label, initWidthOrWeight, userId);
    }

    public static void tableSetupColumn(String label, int imGuiTableColumnFlags, int userId) {
        nTableSetupColumn(label, imGuiTableColumnFlags, userId);
    }

    public static void tableSetupScrollFreeze(int cols, int rows) {
        nTableSetupScrollFreeze(cols, rows);
    }

    public static void tableHeader(String label) {
        nTableHeader(label);
    }

    public static void tableHeadersRow() {
        nTableHeadersRow();
    }

    public static void tableAngledHeadersRow() {
        nTableAngledHeadersRow();
    }

    public static ImGuiTableSortSpecs tableGetSortSpecs() {
        return new ImGuiTableSortSpecs(nTableGetSortSpecs());
    }

    public static int tableGetColumnCount() {
        return nTableGetColumnCount();
    }

    public static int tableGetColumnIndex() {
        return nTableGetColumnIndex();
    }

    public static int tableGetRowIndex() {
        return nTableGetRowIndex();
    }

    public static String tableGetColumnName() {
        return nTableGetColumnName();
    }

    public static String tableGetColumnName(int columnN) {
        return nTableGetColumnName(columnN);
    }

    public static int tableGetColumnFlags() {
        return nTableGetColumnFlags();
    }

    public static int tableGetColumnFlags(int columnN) {
        return nTableGetColumnFlags(columnN);
    }

    public static void tableSetColumnEnabled(int columnN, boolean value) {
        nTableSetColumnEnabled(columnN, value);
    }

    public static int tableGetHoveredColumn() {
        return nTableGetHoveredColumn();
    }

    public static void tableSetBgColor(int imGuiTableBgTarget, int color) {
        nTableSetBgColor(imGuiTableBgTarget, color);
    }

    public static void tableSetBgColor(int imGuiTableBgTarget, int color, int columnN) {
        nTableSetBgColor(imGuiTableBgTarget, color, columnN);
    }

    public static void columns() {
        nColumns();
    }

    public static void columns(int count) {
        nColumns(count);
    }

    public static void columns(int count, String id) {
        nColumns(count, id);
    }

    public static void columns(int count, String id, boolean border) {
        nColumns(count, id, border);
    }

    public static void columns(String id, boolean border) {
        nColumns(id, border);
    }

    public static void columns(boolean border) {
        nColumns(border);
    }

    public static void columns(int count, boolean border) {
        nColumns(count, border);
    }

    public static void nextColumn() {
        nNextColumn();
    }

    public static int getColumnIndex() {
        return nGetColumnIndex();
    }

    public static float getColumnWidth() {
        return nGetColumnWidth();
    }

    public static float getColumnWidth(int columnIndex) {
        return nGetColumnWidth(columnIndex);
    }

    public static void setColumnWidth(int columnIndex, float width) {
        nSetColumnWidth(columnIndex, width);
    }

    public static float getColumnOffset() {
        return nGetColumnOffset();
    }

    public static float getColumnOffset(int columnIndex) {
        return nGetColumnOffset(columnIndex);
    }

    public static void setColumnOffset(int columnIndex, float offsetX) {
        nSetColumnOffset(columnIndex, offsetX);
    }

    public static int getColumnsCount() {
        return nGetColumnsCount();
    }

    public static boolean beginTabBar(String strId) {
        return nBeginTabBar(strId);
    }

    public static boolean beginTabBar(String strId, int imGuiTabBarFlags) {
        return nBeginTabBar(strId, imGuiTabBarFlags);
    }

    public static void endTabBar() {
        nEndTabBar();
    }

    public static boolean beginTabItem(String label) {
        return nBeginTabItem(label);
    }

    public static boolean beginTabItem(String label, ImBoolean pOpen) {
        return nBeginTabItem(label, pOpen != null ? pOpen.getData() : null);
    }

    public static boolean beginTabItem(String label, ImBoolean pOpen, int imGuiTabItemFlags) {
        return nBeginTabItem(label, pOpen != null ? pOpen.getData() : null, imGuiTabItemFlags);
    }

    public static boolean beginTabItem(String label, int imGuiTabItemFlags) {
        return nBeginTabItem(label, imGuiTabItemFlags);
    }

    public static void endTabItem() {
        nEndTabItem();
    }

    public static boolean tabItemButton(String label) {
        return nTabItemButton(label);
    }

    public static boolean tabItemButton(String label, int imGuiTabItemFlags) {
        return nTabItemButton(label, imGuiTabItemFlags);
    }

    public static void setTabItemClosed(String tabOrDockedWindowLabel) {
        nSetTabItemClosed(tabOrDockedWindowLabel);
    }

    public static int dockSpace(int dockspaceId) {
        return nDockSpace(dockspaceId);
    }

    public static int dockSpace(int dockspaceId, ImVec2 size) {
        return nDockSpace(dockspaceId, size.x, size.y);
    }

    public static int dockSpace(int dockspaceId, float sizeX, float sizeY) {
        return nDockSpace(dockspaceId, sizeX, sizeY);
    }

    public static int dockSpace(int dockspaceId, ImVec2 size, int imGuiDockNodeFlags) {
        return nDockSpace(dockspaceId, size.x, size.y, imGuiDockNodeFlags);
    }

    public static int dockSpace(int dockspaceId, float sizeX, float sizeY, int imGuiDockNodeFlags) {
        return nDockSpace(dockspaceId, sizeX, sizeY, imGuiDockNodeFlags);
    }

    public static int dockSpace(int dockspaceId, ImVec2 size, int imGuiDockNodeFlags, ImGuiWindowClass windowClass) {
        return nDockSpace(dockspaceId, size.x, size.y, imGuiDockNodeFlags, windowClass.ptr);
    }

    public static int dockSpace(int dockspaceId, float sizeX, float sizeY, int imGuiDockNodeFlags, ImGuiWindowClass windowClass) {
        return nDockSpace(dockspaceId, sizeX, sizeY, imGuiDockNodeFlags, windowClass.ptr);
    }

    public static int dockSpace(int dockspaceId, int imGuiDockNodeFlags, ImGuiWindowClass windowClass) {
        return nDockSpace(dockspaceId, imGuiDockNodeFlags, windowClass.ptr);
    }

    public static int dockSpace(int dockspaceId, ImGuiWindowClass windowClass) {
        return nDockSpace(dockspaceId, windowClass.ptr);
    }

    public static int dockSpace(int dockspaceId, ImVec2 size, ImGuiWindowClass windowClass) {
        return nDockSpace(dockspaceId, size.x, size.y, windowClass.ptr);
    }

    public static int dockSpace(int dockspaceId, float sizeX, float sizeY, ImGuiWindowClass windowClass) {
        return nDockSpace(dockspaceId, sizeX, sizeY, windowClass.ptr);
    }

    public static int dockSpaceOverViewport() {
        return nDockSpaceOverViewport();
    }

    public static int dockSpaceOverViewport(int dockspaceId) {
        return nDockSpaceOverViewport(dockspaceId);
    }

    public static int dockSpaceOverViewport(int dockspaceId, ImGuiViewport viewport) {
        return nDockSpaceOverViewport(dockspaceId, viewport.ptr);
    }

    public static int dockSpaceOverViewport(int dockspaceId, ImGuiViewport viewport, int imGuiDockNodeFlags) {
        return nDockSpaceOverViewport(dockspaceId, viewport.ptr, imGuiDockNodeFlags);
    }

    public static int dockSpaceOverViewport(int dockspaceId, ImGuiViewport viewport, int imGuiDockNodeFlags, ImGuiWindowClass windowClass) {
        return nDockSpaceOverViewport(dockspaceId, viewport.ptr, imGuiDockNodeFlags, windowClass.ptr);
    }

    public static int dockSpaceOverViewport(ImGuiViewport viewport, int imGuiDockNodeFlags, ImGuiWindowClass windowClass) {
        return nDockSpaceOverViewport(viewport.ptr, imGuiDockNodeFlags, windowClass.ptr);
    }

    public static int dockSpaceOverViewport(int dockspaceId, ImGuiViewport viewport, ImGuiWindowClass windowClass) {
        return nDockSpaceOverViewport(dockspaceId, viewport.ptr, windowClass.ptr);
    }

    public static void setNextWindowDockID(int dockId) {
        nSetNextWindowDockID(dockId);
    }

    public static void setNextWindowDockID(int dockId, int imGuiCond) {
        nSetNextWindowDockID(dockId, imGuiCond);
    }

    public static void setNextWindowClass(ImGuiWindowClass windowClass) {
        nSetNextWindowClass(windowClass.ptr);
    }

    public static int getWindowDockID() {
        return nGetWindowDockID();
    }

    public static boolean isWindowDocked() {
        return nIsWindowDocked();
    }

    public static void logToTTY() {
        nLogToTTY();
    }

    public static void logToTTY(int autoOpenDepth) {
        nLogToTTY(autoOpenDepth);
    }

    public static void logToFile() {
        nLogToFile();
    }

    public static void logToFile(int autoOpenDepth) {
        nLogToFile(autoOpenDepth);
    }

    public static void logToFile(int autoOpenDepth, String filename) {
        nLogToFile(autoOpenDepth, filename);
    }

    public static void logToFile(String filename) {
        nLogToFile(filename);
    }

    public static void logToClipboard() {
        nLogToClipboard();
    }

    public static void logToClipboard(int autoOpenDepth) {
        nLogToClipboard(autoOpenDepth);
    }

    public static void logFinish() {
        nLogFinish();
    }

    public static void logButtons() {
        nLogButtons();
    }

    public static void logText(String text) {
        nLogText(text);
    }

    public static boolean beginDragDropSource() {
        return nBeginDragDropSource();
    }

    public static boolean beginDragDropSource(int imGuiDragDropFlags) {
        return nBeginDragDropSource(imGuiDragDropFlags);
    }

    public static boolean setDragDropPayload(String dataType, Object payload) {
        return setDragDropPayload(dataType, payload, 0);
    }

    public static boolean setDragDropPayload(String dataType, Object payload, int imGuiCond) {
        if (payloadRef == null || payloadRef.get() != payload) {
            payloadRef = new WeakReference<>(payload);
        }
        return nSetDragDropPayload(dataType, PAYLOAD_PLACEHOLDER_DATA, 1, imGuiCond);
    }

    public static boolean setDragDropPayload(Object payload) {
        return setDragDropPayload(payload, 0);
    }

    public static boolean setDragDropPayload(Object payload, int imGuiCond) {
        return setDragDropPayload(String.valueOf(payload.getClass().hashCode()), payload, imGuiCond);
    }

    public static void endDragDropSource() {
        nEndDragDropSource();
    }

    public static boolean beginDragDropTarget() {
        return nBeginDragDropTarget();
    }

    public static <T> T acceptDragDropPayload(String str) {
        return (T) acceptDragDropPayload(str, 0);
    }

    public static <T> T acceptDragDropPayload(String str, Class<T> cls) {
        return (T) acceptDragDropPayload(str, 0, cls);
    }

    public static <T> T acceptDragDropPayload(String str, int i) {
        return (T) acceptDragDropPayload(str, i, null);
    }

    public static <T> T acceptDragDropPayload(String str, int i, Class<T> cls) {
        T t;
        if (payloadRef != null && nAcceptDragDropPayload(str, i) && (t = (T) payloadRef.get()) != null) {
            if (cls == null || t.getClass().isAssignableFrom(cls)) {
                return t;
            }
            return null;
        }
        return null;
    }

    public static <T> T acceptDragDropPayload(Class<T> cls) {
        return (T) acceptDragDropPayload(String.valueOf(cls.hashCode()), 0, cls);
    }

    public static <T> T acceptDragDropPayload(Class<T> cls, int i) {
        return (T) acceptDragDropPayload(String.valueOf(cls.hashCode()), i, cls);
    }

    public static void endDragDropTarget() {
        nEndDragDropTarget();
    }

    public static <T> T getDragDropPayload() {
        T t;
        if (payloadRef != null && nHasDragDropPayload() && (t = (T) payloadRef.get()) != null) {
            return t;
        }
        return null;
    }

    public static <T> T getDragDropPayload(String str) {
        T t;
        if (payloadRef != null && nHasDragDropPayload(str) && (t = (T) payloadRef.get()) != null) {
            return t;
        }
        return null;
    }

    public static <T> T getDragDropPayload(Class<T> cls) {
        return (T) getDragDropPayload(String.valueOf(cls.hashCode()));
    }

    public static void beginDisabled() {
        nBeginDisabled();
    }

    public static void beginDisabled(boolean disabled) {
        nBeginDisabled(disabled);
    }

    public static void endDisabled() {
        nEndDisabled();
    }

    public static void pushClipRect(ImVec2 clipRectMin, ImVec2 clipRectMax, boolean intersectWithCurrentClipRect) {
        nPushClipRect(clipRectMin.x, clipRectMin.y, clipRectMax.x, clipRectMax.y, intersectWithCurrentClipRect);
    }

    public static void pushClipRect(float clipRectMinX, float clipRectMinY, float clipRectMaxX, float clipRectMaxY, boolean intersectWithCurrentClipRect) {
        nPushClipRect(clipRectMinX, clipRectMinY, clipRectMaxX, clipRectMaxY, intersectWithCurrentClipRect);
    }

    public static void popClipRect() {
        nPopClipRect();
    }

    public static void setItemDefaultFocus() {
        nSetItemDefaultFocus();
    }

    public static void setKeyboardFocusHere() {
        nSetKeyboardFocusHere();
    }

    public static void setKeyboardFocusHere(int offset) {
        nSetKeyboardFocusHere(offset);
    }

    public static void setNextItemAllowOverlap() {
        nSetNextItemAllowOverlap();
    }

    public static boolean isItemHovered() {
        return nIsItemHovered();
    }

    public static boolean isItemHovered(int imGuiHoveredFlags) {
        return nIsItemHovered(imGuiHoveredFlags);
    }

    public static boolean isItemActive() {
        return nIsItemActive();
    }

    public static boolean isItemFocused() {
        return nIsItemFocused();
    }

    public static boolean isItemClicked() {
        return nIsItemClicked();
    }

    public static boolean isItemClicked(int mouseButton) {
        return nIsItemClicked(mouseButton);
    }

    public static boolean isItemVisible() {
        return nIsItemVisible();
    }

    public static boolean isItemEdited() {
        return nIsItemEdited();
    }

    public static boolean isItemActivated() {
        return nIsItemActivated();
    }

    public static boolean isItemDeactivated() {
        return nIsItemDeactivated();
    }

    public static boolean isItemDeactivatedAfterEdit() {
        return nIsItemDeactivatedAfterEdit();
    }

    public static boolean isItemToggledOpen() {
        return nIsItemToggledOpen();
    }

    public static boolean isAnyItemHovered() {
        return nIsAnyItemHovered();
    }

    public static boolean isAnyItemActive() {
        return nIsAnyItemActive();
    }

    public static boolean isAnyItemFocused() {
        return nIsAnyItemFocused();
    }

    public static int getItemID() {
        return nGetItemID();
    }

    public static ImVec2 getItemRectMin() {
        ImVec2 dst = new ImVec2();
        nGetItemRectMin(dst);
        return dst;
    }

    public static float getItemRectMinX() {
        return nGetItemRectMinX();
    }

    public static float getItemRectMinY() {
        return nGetItemRectMinY();
    }

    public static void getItemRectMin(ImVec2 dst) {
        nGetItemRectMin(dst);
    }

    public static ImVec2 getItemRectMax() {
        ImVec2 dst = new ImVec2();
        nGetItemRectMax(dst);
        return dst;
    }

    public static float getItemRectMaxX() {
        return nGetItemRectMaxX();
    }

    public static float getItemRectMaxY() {
        return nGetItemRectMaxY();
    }

    public static void getItemRectMax(ImVec2 dst) {
        nGetItemRectMax(dst);
    }

    public static ImVec2 getItemRectSize() {
        ImVec2 dst = new ImVec2();
        nGetItemRectSize(dst);
        return dst;
    }

    public static float getItemRectSizeX() {
        return nGetItemRectSizeX();
    }

    public static float getItemRectSizeY() {
        return nGetItemRectSizeY();
    }

    public static void getItemRectSize(ImVec2 dst) {
        nGetItemRectSize(dst);
    }

    public static ImGuiViewport getMainViewport() {
        _GETMAINVIEWPORT_1.ptr = nGetMainViewport();
        return _GETMAINVIEWPORT_1;
    }

    public static ImDrawList getBackgroundDrawList() {
        return new ImDrawList(nGetBackgroundDrawList());
    }

    public static ImDrawList getBackgroundDrawList(ImGuiViewport viewport) {
        return new ImDrawList(nGetBackgroundDrawList(viewport.ptr));
    }

    public static ImDrawList getForegroundDrawList() {
        return new ImDrawList(nGetForegroundDrawList());
    }

    public static ImDrawList getForegroundDrawList(ImGuiViewport viewport) {
        return new ImDrawList(nGetForegroundDrawList(viewport.ptr));
    }

    public static boolean isRectVisible(ImVec2 size) {
        return nIsRectVisible(size.x, size.y);
    }

    public static boolean isRectVisible(float sizeX, float sizeY) {
        return nIsRectVisible(sizeX, sizeY);
    }

    public static boolean isRectVisible(ImVec2 rectMin, ImVec2 rectMax) {
        return nIsRectVisible(rectMin.x, rectMin.y, rectMax.x, rectMax.y);
    }

    public static boolean isRectVisible(float rectMinX, float rectMinY, float rectMaxX, float rectMaxY) {
        return nIsRectVisible(rectMinX, rectMinY, rectMaxX, rectMaxY);
    }

    public static double getTime() {
        return nGetTime();
    }

    public static int getFrameCount() {
        return nGetFrameCount();
    }

    public static String getStyleColorName(int imGuiColIdx) {
        return nGetStyleColorName(imGuiColIdx);
    }

    public static void setStateStorage(ImGuiStorage storage) {
        nSetStateStorage(storage.ptr);
    }

    public static ImGuiStorage getStateStorage() {
        return new ImGuiStorage(nGetStateStorage());
    }

    public static ImVec2 calcTextSize(String text) {
        ImVec2 dst = new ImVec2();
        nCalcTextSize(dst, text);
        return dst;
    }

    public static float calcTextSizeX(String text) {
        return nCalcTextSizeX(text);
    }

    public static float calcTextSizeY(String text) {
        return nCalcTextSizeY(text);
    }

    public static void calcTextSize(ImVec2 dst, String text) {
        nCalcTextSize(dst, text);
    }

    public static ImVec2 calcTextSize(String text, boolean hideTextAfterDoubleHash) {
        ImVec2 dst = new ImVec2();
        nCalcTextSize(dst, text, hideTextAfterDoubleHash);
        return dst;
    }

    public static float calcTextSizeX(String text, boolean hideTextAfterDoubleHash) {
        return nCalcTextSizeX(text, hideTextAfterDoubleHash);
    }

    public static float calcTextSizeY(String text, boolean hideTextAfterDoubleHash) {
        return nCalcTextSizeY(text, hideTextAfterDoubleHash);
    }

    public static void calcTextSize(ImVec2 dst, String text, boolean hideTextAfterDoubleHash) {
        nCalcTextSize(dst, text, hideTextAfterDoubleHash);
    }

    public static ImVec2 calcTextSize(String text, boolean hideTextAfterDoubleHash, float wrapWidth) {
        ImVec2 dst = new ImVec2();
        nCalcTextSize(dst, text, hideTextAfterDoubleHash, wrapWidth);
        return dst;
    }

    public static float calcTextSizeX(String text, boolean hideTextAfterDoubleHash, float wrapWidth) {
        return nCalcTextSizeX(text, hideTextAfterDoubleHash, wrapWidth);
    }

    public static float calcTextSizeY(String text, boolean hideTextAfterDoubleHash, float wrapWidth) {
        return nCalcTextSizeY(text, hideTextAfterDoubleHash, wrapWidth);
    }

    public static void calcTextSize(ImVec2 dst, String text, boolean hideTextAfterDoubleHash, float wrapWidth) {
        nCalcTextSize(dst, text, hideTextAfterDoubleHash, wrapWidth);
    }

    public static ImVec2 calcTextSize(String text, float wrapWidth) {
        ImVec2 dst = new ImVec2();
        nCalcTextSize(dst, text, wrapWidth);
        return dst;
    }

    public static float calcTextSizeX(String text, float wrapWidth) {
        return nCalcTextSizeX(text, wrapWidth);
    }

    public static float calcTextSizeY(String text, float wrapWidth) {
        return nCalcTextSizeY(text, wrapWidth);
    }

    public static void calcTextSize(ImVec2 dst, String text, float wrapWidth) {
        nCalcTextSize(dst, text, wrapWidth);
    }

    public static ImVec4 colorConvertU32ToFloat4(int in) {
        ImVec4 dst = new ImVec4();
        nColorConvertU32ToFloat4(dst, in);
        return dst;
    }

    public static float colorConvertU32ToFloat4X(int in) {
        return nColorConvertU32ToFloat4X(in);
    }

    public static float colorConvertU32ToFloat4Y(int in) {
        return nColorConvertU32ToFloat4Y(in);
    }

    public static float colorConvertU32ToFloat4Z(int in) {
        return nColorConvertU32ToFloat4Z(in);
    }

    public static float colorConvertU32ToFloat4W(int in) {
        return nColorConvertU32ToFloat4W(in);
    }

    public static void colorConvertU32ToFloat4(ImVec4 dst, int in) {
        nColorConvertU32ToFloat4(dst, in);
    }

    public static int colorConvertFloat4ToU32(ImVec4 in) {
        return nColorConvertFloat4ToU32(in.x, in.y, in.z, in.w);
    }

    public static int colorConvertFloat4ToU32(float inX, float inY, float inZ, float inW) {
        return nColorConvertFloat4ToU32(inX, inY, inZ, inW);
    }

    public static void colorConvertRGBtoHSV(float[] rgb, float[] hsv) {
        nColorConvertRGBtoHSV(rgb, hsv);
    }

    public static void colorConvertHSVtoRGB(float[] hsv, float[] rgb) {
        nColorConvertHSVtoRGB(hsv, rgb);
    }

    public static boolean isKeyDown(int key) {
        return nIsKeyDown(key);
    }

    public static boolean isKeyPressed(int key) {
        return nIsKeyPressed(key);
    }

    public static boolean isKeyPressed(int key, boolean repeat) {
        return nIsKeyPressed(key, repeat);
    }

    public static boolean isKeyReleased(int key) {
        return nIsKeyReleased(key);
    }

    public static boolean isKeyChordPressed(int keyChord) {
        return nIsKeyChordPressed(keyChord);
    }

    public static boolean getKeyPressedAmount(int key, float repeatDelay, float rate) {
        return nGetKeyPressedAmount(key, repeatDelay, rate);
    }

    public static String getKeyName(int key) {
        return nGetKeyName(key);
    }

    public static void setNextFrameWantCaptureKeyboard(boolean wantCaptureKeyboard) {
        nSetNextFrameWantCaptureKeyboard(wantCaptureKeyboard);
    }

    public static boolean shortcut(int keyChord) {
        return nShortcut(keyChord);
    }

    public static boolean shortcut(int keyChord, int flags) {
        return nShortcut(keyChord, flags);
    }

    public static void setNextItemShortcut(int keyChord) {
        nSetNextItemShortcut(keyChord);
    }

    public static void setNextItemShortcut(int keyChord, int flags) {
        nSetNextItemShortcut(keyChord, flags);
    }

    public static boolean isMouseDown(int button) {
        return nIsMouseDown(button);
    }

    public static boolean isMouseClicked(int button) {
        return nIsMouseClicked(button);
    }

    public static boolean isMouseClicked(int button, boolean repeat) {
        return nIsMouseClicked(button, repeat);
    }

    public static boolean isMouseReleased(int button) {
        return nIsMouseReleased(button);
    }

    public static boolean isMouseDoubleClicked(int button) {
        return nIsMouseDoubleClicked(button);
    }

    public static int getMouseClickedCount(int button) {
        return nGetMouseClickedCount(button);
    }

    public static boolean isMouseHoveringRect(ImVec2 rMin, ImVec2 rMax) {
        return nIsMouseHoveringRect(rMin.x, rMin.y, rMax.x, rMax.y);
    }

    public static boolean isMouseHoveringRect(float rMinX, float rMinY, float rMaxX, float rMaxY) {
        return nIsMouseHoveringRect(rMinX, rMinY, rMaxX, rMaxY);
    }

    public static boolean isMouseHoveringRect(ImVec2 rMin, ImVec2 rMax, boolean clip) {
        return nIsMouseHoveringRect(rMin.x, rMin.y, rMax.x, rMax.y, clip);
    }

    public static boolean isMouseHoveringRect(float rMinX, float rMinY, float rMaxX, float rMaxY, boolean clip) {
        return nIsMouseHoveringRect(rMinX, rMinY, rMaxX, rMaxY, clip);
    }

    public static boolean isMousePosValid() {
        return nIsMousePosValid();
    }

    public static boolean isMousePosValid(ImVec2 mousePos) {
        return nIsMousePosValid(mousePos.x, mousePos.y);
    }

    public static boolean isMousePosValid(float mousePosX, float mousePosY) {
        return nIsMousePosValid(mousePosX, mousePosY);
    }

    public static boolean isAnyMouseDown() {
        return nIsAnyMouseDown();
    }

    public static ImVec2 getMousePos() {
        ImVec2 dst = new ImVec2();
        nGetMousePos(dst);
        return dst;
    }

    public static float getMousePosX() {
        return nGetMousePosX();
    }

    public static float getMousePosY() {
        return nGetMousePosY();
    }

    public static void getMousePos(ImVec2 dst) {
        nGetMousePos(dst);
    }

    public static ImVec2 getMousePosOnOpeningCurrentPopup() {
        ImVec2 dst = new ImVec2();
        nGetMousePosOnOpeningCurrentPopup(dst);
        return dst;
    }

    public static float getMousePosOnOpeningCurrentPopupX() {
        return nGetMousePosOnOpeningCurrentPopupX();
    }

    public static float getMousePosOnOpeningCurrentPopupY() {
        return nGetMousePosOnOpeningCurrentPopupY();
    }

    public static void getMousePosOnOpeningCurrentPopup(ImVec2 dst) {
        nGetMousePosOnOpeningCurrentPopup(dst);
    }

    public static boolean isMouseDragging(int button) {
        return nIsMouseDragging(button);
    }

    public static boolean isMouseDragging(int button, float lockThreshold) {
        return nIsMouseDragging(button, lockThreshold);
    }

    public static ImVec2 getMouseDragDelta() {
        ImVec2 dst = new ImVec2();
        nGetMouseDragDelta(dst);
        return dst;
    }

    public static float getMouseDragDeltaX() {
        return nGetMouseDragDeltaX();
    }

    public static float getMouseDragDeltaY() {
        return nGetMouseDragDeltaY();
    }

    public static void getMouseDragDelta(ImVec2 dst) {
        nGetMouseDragDelta(dst);
    }

    public static ImVec2 getMouseDragDelta(int button) {
        ImVec2 dst = new ImVec2();
        nGetMouseDragDelta(dst, button);
        return dst;
    }

    public static float getMouseDragDeltaX(int button) {
        return nGetMouseDragDeltaX(button);
    }

    public static float getMouseDragDeltaY(int button) {
        return nGetMouseDragDeltaY(button);
    }

    public static void getMouseDragDelta(ImVec2 dst, int button) {
        nGetMouseDragDelta(dst, button);
    }

    public static ImVec2 getMouseDragDelta(int button, float lockThreshold) {
        ImVec2 dst = new ImVec2();
        nGetMouseDragDelta(dst, button, lockThreshold);
        return dst;
    }

    public static float getMouseDragDeltaX(int button, float lockThreshold) {
        return nGetMouseDragDeltaX(button, lockThreshold);
    }

    public static float getMouseDragDeltaY(int button, float lockThreshold) {
        return nGetMouseDragDeltaY(button, lockThreshold);
    }

    public static void getMouseDragDelta(ImVec2 dst, int button, float lockThreshold) {
        nGetMouseDragDelta(dst, button, lockThreshold);
    }

    public static void resetMouseDragDelta() {
        nResetMouseDragDelta();
    }

    public static void resetMouseDragDelta(int button) {
        nResetMouseDragDelta(button);
    }

    public static int getMouseCursor() {
        return nGetMouseCursor();
    }

    public static void setMouseCursor(int type) {
        nSetMouseCursor(type);
    }

    public static void setNextFrameWantCaptureMouse(boolean wantCaptureMouse) {
        nSetNextFrameWantCaptureMouse(wantCaptureMouse);
    }

    public static String getClipboardText() {
        return nGetClipboardText();
    }

    public static void setClipboardText(String text) {
        nSetClipboardText(text);
    }

    public static void loadIniSettingsFromDisk(String iniFilename) {
        nLoadIniSettingsFromDisk(iniFilename);
    }

    public static void loadIniSettingsFromMemory(String iniData) {
        nLoadIniSettingsFromMemory(iniData);
    }

    public static void loadIniSettingsFromMemory(String iniData, int iniSize) {
        nLoadIniSettingsFromMemory(iniData, iniSize);
    }

    public static void saveIniSettingsToDisk(String iniFilename) {
        nSaveIniSettingsToDisk(iniFilename);
    }

    public static String saveIniSettingsToMemory() {
        return nSaveIniSettingsToMemory();
    }

    public static String saveIniSettingsToMemory(long outIniSize) {
        return nSaveIniSettingsToMemory(outIniSize);
    }

    public static void debugTextEncoding(String text) {
        nDebugTextEncoding(text);
    }

    public static void debugFlashStyleColor(int idx) {
        nDebugFlashStyleColor(idx);
    }

    public static void debugStartItemPicker() {
        nDebugStartItemPicker();
    }

    public static boolean debugCheckVersionAndDataLayout(String versionStr, int szIo, int szStyle, int szVec2, int szVec4, int szDrawVert, int szDrawIdx) {
        return nDebugCheckVersionAndDataLayout(versionStr, szIo, szStyle, szVec2, szVec4, szDrawVert, szDrawIdx);
    }

    public static ImGuiPlatformIO getPlatformIO() {
        _GETPLATFORMIO_1.ptr = nGetPlatformIO();
        return _GETPLATFORMIO_1;
    }

    public static void updatePlatformWindows() {
        nUpdatePlatformWindows();
    }

    public static void renderPlatformWindowsDefault() {
        nRenderPlatformWindowsDefault();
    }

    public static void destroyPlatformWindows() {
        nDestroyPlatformWindows();
    }

    public static ImGuiViewport findViewportByID(int imGuiID) {
        return new ImGuiViewport(nFindViewportByID(imGuiID));
    }

    public static ImGuiViewport findViewportByPlatformHandle(long platformHandle) {
        return new ImGuiViewport(nFindViewportByPlatformHandle(platformHandle));
    }
}
