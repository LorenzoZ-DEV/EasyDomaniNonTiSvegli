package imgui.extension.implot;

import imgui.ImDrawList;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.internal.ImGuiContext;
import imgui.type.ImBoolean;
import imgui.type.ImDouble;
import imgui.type.ImFloat;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/extension/implot/ImPlot.class */
public final class ImPlot {
    private static final ImPlotStyle _GETSTYLE_1 = new ImPlotStyle(0);
    private static final ImPlotInputMap _GETINPUTMAP_1 = new ImPlotInputMap(0);

    private static native long nCreateContext();

    private static native void nDestroyContext();

    private static native void nDestroyContext(long j);

    private static native long nGetCurrentContext();

    private static native void nSetCurrentContext(long j);

    private static native void nSetImGuiContext(long j);

    private static native boolean nBeginPlot(String str);

    private static native boolean nBeginPlot(String str, float f, float f2);

    private static native boolean nBeginPlot(String str, float f, float f2, int i);

    private static native boolean nBeginPlot(String str, int i);

    private static native void nEndPlot();

    private static native boolean nBeginSubplots(String str, int i, int i2, float f, float f2);

    private static native boolean nBeginSubplots(String str, int i, int i2, float f, float f2, int i3);

    private static native boolean nBeginSubplots(String str, int i, int i2, float f, float f2, int i3, float[] fArr);

    private static native boolean nBeginSubplots(String str, int i, int i2, float f, float f2, int i3, float[] fArr, float[] fArr2);

    private static native boolean nBeginSubplots(String str, int i, int i2, float f, float f2, float[] fArr, float[] fArr2);

    private static native void nEndSubplots();

    private static native void nSetupAxis(int i);

    private static native void nSetupAxis(int i, String str);

    private static native void nSetupAxis(int i, String str, int i2);

    private static native void nSetupAxis(int i, int i2);

    private static native void nSetupAxisLimits(int i, double d, double d2);

    private static native void nSetupAxisLimits(int i, double d, double d2, int i2);

    private static native void nSetupAxisLinks(int i, double[] dArr, double[] dArr2);

    private static native void nSetupAxisFormat(int i, String str);

    private static native void nSetupAxisTicks(int i, double[] dArr, int i2);

    private static native void nSetupAxisTicks(int i, double[] dArr, int i2, String[] strArr, int i3);

    private static native void nSetupAxisTicks(int i, double[] dArr, int i2, String[] strArr, int i3, boolean z);

    private static native void nSetupAxisTicks(int i, double[] dArr, int i2, boolean z);

    private static native void nSetupAxisTicks(int i, double d, double d2, int i2);

    private static native void nSetupAxisTicks(int i, double d, double d2, int i2, String[] strArr, int i3);

    private static native void nSetupAxisTicks(int i, double d, double d2, int i2, String[] strArr, int i3, boolean z);

    private static native void nSetupAxisTicks(int i, double d, double d2, int i2, boolean z);

    private static native void nSetupAxisScale(int i, int i2);

    private static native void nSetupAxisLimitsConstraints(int i, double d, double d2);

    private static native void nSetupAxisZoomConstraints(int i, double d, double d2);

    private static native void nSetupAxes(String str, String str2);

    private static native void nSetupAxes(String str, String str2, int i);

    private static native void nSetupAxes(String str, String str2, int i, int i2);

    private static native void nSetupAxesLimits(double d, double d2, double d3, double d4);

    private static native void nSetupAxesLimits(double d, double d2, double d3, double d4, int i);

    private static native void nSetupLegend(int i);

    private static native void nSetupLegend(int i, int i2);

    private static native void nSetupMouseText(int i);

    private static native void nSetupMouseText(int i, int i2);

    private static native void nSetupFinish();

    private static native void nSetNextAxisLimits(int i, double d, double d2);

    private static native void nSetNextAxisLimits(int i, double d, double d2, int i2);

    private static native void nSetNextAxisLinks(int i, double[] dArr, double[] dArr2);

    private static native void nSetNextAxisToFit(int i);

    private static native void nSetNextAxesLimits(double d, double d2, double d3, double d4);

    private static native void nSetNextAxesLimits(double d, double d2, double d3, double d4, int i);

    private static native void nSetNextAxesToFit();

    private static native void nPlotLine(String str, short[] sArr);

    private static native void nPlotLine(String str, short[] sArr, double d);

    private static native void nPlotLine(String str, short[] sArr, double d, double d2);

    private static native void nPlotLine(String str, short[] sArr, double d, double d2, int i);

    private static native void nPlotLine(String str, short[] sArr, double d, double d2, int i, int i2);

    private static native void nPlotLine(String str, int[] iArr);

    private static native void nPlotLine(String str, int[] iArr, double d);

    private static native void nPlotLine(String str, int[] iArr, double d, double d2);

    private static native void nPlotLine(String str, int[] iArr, double d, double d2, int i);

    private static native void nPlotLine(String str, int[] iArr, double d, double d2, int i, int i2);

    private static native void nPlotLine(String str, long[] jArr);

    private static native void nPlotLine(String str, long[] jArr, double d);

    private static native void nPlotLine(String str, long[] jArr, double d, double d2);

    private static native void nPlotLine(String str, long[] jArr, double d, double d2, int i);

    private static native void nPlotLine(String str, long[] jArr, double d, double d2, int i, int i2);

    private static native void nPlotLine(String str, float[] fArr);

    private static native void nPlotLine(String str, float[] fArr, double d);

    private static native void nPlotLine(String str, float[] fArr, double d, double d2);

    private static native void nPlotLine(String str, float[] fArr, double d, double d2, int i);

    private static native void nPlotLine(String str, float[] fArr, double d, double d2, int i, int i2);

    private static native void nPlotLine(String str, double[] dArr);

    private static native void nPlotLine(String str, double[] dArr, double d);

    private static native void nPlotLine(String str, double[] dArr, double d, double d2);

    private static native void nPlotLine(String str, double[] dArr, double d, double d2, int i);

    private static native void nPlotLine(String str, double[] dArr, double d, double d2, int i, int i2);

    private static native void nPlotLineV(String str, short[] sArr, int i);

    private static native void nPlotLineV(String str, short[] sArr, int i, double d);

    private static native void nPlotLineV(String str, short[] sArr, int i, double d, double d2);

    private static native void nPlotLineV(String str, short[] sArr, int i, double d, double d2, int i2);

    private static native void nPlotLineV(String str, short[] sArr, int i, double d, double d2, int i2, int i3);

    private static native void nPlotLineV(String str, int[] iArr, int i);

    private static native void nPlotLineV(String str, int[] iArr, int i, double d);

    private static native void nPlotLineV(String str, int[] iArr, int i, double d, double d2);

    private static native void nPlotLineV(String str, int[] iArr, int i, double d, double d2, int i2);

    private static native void nPlotLineV(String str, int[] iArr, int i, double d, double d2, int i2, int i3);

    private static native void nPlotLineV(String str, long[] jArr, int i);

    private static native void nPlotLineV(String str, long[] jArr, int i, double d);

    private static native void nPlotLineV(String str, long[] jArr, int i, double d, double d2);

    private static native void nPlotLineV(String str, long[] jArr, int i, double d, double d2, int i2);

    private static native void nPlotLineV(String str, long[] jArr, int i, double d, double d2, int i2, int i3);

    private static native void nPlotLineV(String str, float[] fArr, int i);

    private static native void nPlotLineV(String str, float[] fArr, int i, double d);

    private static native void nPlotLineV(String str, float[] fArr, int i, double d, double d2);

    private static native void nPlotLineV(String str, float[] fArr, int i, double d, double d2, int i2);

    private static native void nPlotLineV(String str, float[] fArr, int i, double d, double d2, int i2, int i3);

    private static native void nPlotLineV(String str, double[] dArr, int i);

    private static native void nPlotLineV(String str, double[] dArr, int i, double d);

    private static native void nPlotLineV(String str, double[] dArr, int i, double d, double d2);

    private static native void nPlotLineV(String str, double[] dArr, int i, double d, double d2, int i2);

    private static native void nPlotLineV(String str, double[] dArr, int i, double d, double d2, int i2, int i3);

    private static native void nPlotLine(String str, short[] sArr, short[] sArr2);

    private static native void nPlotLine(String str, short[] sArr, short[] sArr2, int i);

    private static native void nPlotLine(String str, int[] iArr, int[] iArr2);

    private static native void nPlotLine(String str, int[] iArr, int[] iArr2, int i);

    private static native void nPlotLine(String str, long[] jArr, long[] jArr2);

    private static native void nPlotLine(String str, long[] jArr, long[] jArr2, int i);

    private static native void nPlotLine(String str, float[] fArr, float[] fArr2);

    private static native void nPlotLine(String str, float[] fArr, float[] fArr2, int i);

    private static native void nPlotLine(String str, double[] dArr, double[] dArr2);

    private static native void nPlotLine(String str, double[] dArr, double[] dArr2, int i);

    private static native void nPlotLineV(String str, short[] sArr, short[] sArr2, int i);

    private static native void nPlotLineV(String str, short[] sArr, short[] sArr2, int i, int i2);

    private static native void nPlotLineV(String str, short[] sArr, short[] sArr2, int i, int i2, int i3);

    private static native void nPlotLineV(String str, int[] iArr, int[] iArr2, int i);

    private static native void nPlotLineV(String str, int[] iArr, int[] iArr2, int i, int i2);

    private static native void nPlotLineV(String str, int[] iArr, int[] iArr2, int i, int i2, int i3);

    private static native void nPlotLineV(String str, long[] jArr, long[] jArr2, int i);

    private static native void nPlotLineV(String str, long[] jArr, long[] jArr2, int i, int i2);

    private static native void nPlotLineV(String str, long[] jArr, long[] jArr2, int i, int i2, int i3);

    private static native void nPlotLineV(String str, float[] fArr, float[] fArr2, int i);

    private static native void nPlotLineV(String str, float[] fArr, float[] fArr2, int i, int i2);

    private static native void nPlotLineV(String str, float[] fArr, float[] fArr2, int i, int i2, int i3);

    private static native void nPlotLineV(String str, double[] dArr, double[] dArr2, int i);

    private static native void nPlotLineV(String str, double[] dArr, double[] dArr2, int i, int i2);

    private static native void nPlotLineV(String str, double[] dArr, double[] dArr2, int i, int i2, int i3);

    private static native void nPlotScatter(String str, short[] sArr);

    private static native void nPlotScatter(String str, short[] sArr, double d);

    private static native void nPlotScatter(String str, short[] sArr, double d, double d2);

    private static native void nPlotScatter(String str, short[] sArr, double d, double d2, int i);

    private static native void nPlotScatter(String str, short[] sArr, double d, double d2, int i, int i2);

    private static native void nPlotScatter(String str, int[] iArr);

    private static native void nPlotScatter(String str, int[] iArr, double d);

    private static native void nPlotScatter(String str, int[] iArr, double d, double d2);

    private static native void nPlotScatter(String str, int[] iArr, double d, double d2, int i);

    private static native void nPlotScatter(String str, int[] iArr, double d, double d2, int i, int i2);

    private static native void nPlotScatter(String str, long[] jArr);

    private static native void nPlotScatter(String str, long[] jArr, double d);

    private static native void nPlotScatter(String str, long[] jArr, double d, double d2);

    private static native void nPlotScatter(String str, long[] jArr, double d, double d2, int i);

    private static native void nPlotScatter(String str, long[] jArr, double d, double d2, int i, int i2);

    private static native void nPlotScatter(String str, float[] fArr);

    private static native void nPlotScatter(String str, float[] fArr, double d);

    private static native void nPlotScatter(String str, float[] fArr, double d, double d2);

    private static native void nPlotScatter(String str, float[] fArr, double d, double d2, int i);

    private static native void nPlotScatter(String str, float[] fArr, double d, double d2, int i, int i2);

    private static native void nPlotScatter(String str, double[] dArr);

    private static native void nPlotScatter(String str, double[] dArr, double d);

    private static native void nPlotScatter(String str, double[] dArr, double d, double d2);

    private static native void nPlotScatter(String str, double[] dArr, double d, double d2, int i);

    private static native void nPlotScatter(String str, double[] dArr, double d, double d2, int i, int i2);

    private static native void nPlotScatterV(String str, short[] sArr, int i);

    private static native void nPlotScatterV(String str, short[] sArr, int i, double d);

    private static native void nPlotScatterV(String str, short[] sArr, int i, double d, double d2);

    private static native void nPlotScatterV(String str, short[] sArr, int i, double d, double d2, int i2);

    private static native void nPlotScatterV(String str, short[] sArr, int i, double d, double d2, int i2, int i3);

    private static native void nPlotScatterV(String str, int[] iArr, int i);

    private static native void nPlotScatterV(String str, int[] iArr, int i, double d);

    private static native void nPlotScatterV(String str, int[] iArr, int i, double d, double d2);

    private static native void nPlotScatterV(String str, int[] iArr, int i, double d, double d2, int i2);

    private static native void nPlotScatterV(String str, int[] iArr, int i, double d, double d2, int i2, int i3);

    private static native void nPlotScatterV(String str, long[] jArr, int i);

    private static native void nPlotScatterV(String str, long[] jArr, int i, double d);

    private static native void nPlotScatterV(String str, long[] jArr, int i, double d, double d2);

    private static native void nPlotScatterV(String str, long[] jArr, int i, double d, double d2, int i2);

    private static native void nPlotScatterV(String str, long[] jArr, int i, double d, double d2, int i2, int i3);

    private static native void nPlotScatterV(String str, float[] fArr, int i);

    private static native void nPlotScatterV(String str, float[] fArr, int i, double d);

    private static native void nPlotScatterV(String str, float[] fArr, int i, double d, double d2);

    private static native void nPlotScatterV(String str, float[] fArr, int i, double d, double d2, int i2);

    private static native void nPlotScatterV(String str, float[] fArr, int i, double d, double d2, int i2, int i3);

    private static native void nPlotScatterV(String str, double[] dArr, int i);

    private static native void nPlotScatterV(String str, double[] dArr, int i, double d);

    private static native void nPlotScatterV(String str, double[] dArr, int i, double d, double d2);

    private static native void nPlotScatterV(String str, double[] dArr, int i, double d, double d2, int i2);

    private static native void nPlotScatterV(String str, double[] dArr, int i, double d, double d2, int i2, int i3);

    private static native void nPlotScatter(String str, short[] sArr, short[] sArr2);

    private static native void nPlotScatter(String str, short[] sArr, short[] sArr2, int i);

    private static native void nPlotScatter(String str, int[] iArr, int[] iArr2);

    private static native void nPlotScatter(String str, int[] iArr, int[] iArr2, int i);

    private static native void nPlotScatter(String str, long[] jArr, long[] jArr2);

    private static native void nPlotScatter(String str, long[] jArr, long[] jArr2, int i);

    private static native void nPlotScatter(String str, float[] fArr, float[] fArr2);

    private static native void nPlotScatter(String str, float[] fArr, float[] fArr2, int i);

    private static native void nPlotScatter(String str, double[] dArr, double[] dArr2);

    private static native void nPlotScatter(String str, double[] dArr, double[] dArr2, int i);

    private static native void nPlotScatterV(String str, short[] sArr, short[] sArr2, int i);

    private static native void nPlotScatterV(String str, short[] sArr, short[] sArr2, int i, int i2);

    private static native void nPlotScatterV(String str, short[] sArr, short[] sArr2, int i, int i2, int i3);

    private static native void nPlotScatterV(String str, int[] iArr, int[] iArr2, int i);

    private static native void nPlotScatterV(String str, int[] iArr, int[] iArr2, int i, int i2);

    private static native void nPlotScatterV(String str, int[] iArr, int[] iArr2, int i, int i2, int i3);

    private static native void nPlotScatterV(String str, long[] jArr, long[] jArr2, int i);

    private static native void nPlotScatterV(String str, long[] jArr, long[] jArr2, int i, int i2);

    private static native void nPlotScatterV(String str, long[] jArr, long[] jArr2, int i, int i2, int i3);

    private static native void nPlotScatterV(String str, float[] fArr, float[] fArr2, int i);

    private static native void nPlotScatterV(String str, float[] fArr, float[] fArr2, int i, int i2);

    private static native void nPlotScatterV(String str, float[] fArr, float[] fArr2, int i, int i2, int i3);

    private static native void nPlotScatterV(String str, double[] dArr, double[] dArr2, int i);

    private static native void nPlotScatterV(String str, double[] dArr, double[] dArr2, int i, int i2);

    private static native void nPlotScatterV(String str, double[] dArr, double[] dArr2, int i, int i2, int i3);

    private static native void nPlotStairs(String str, short[] sArr);

    private static native void nPlotStairs(String str, short[] sArr, double d);

    private static native void nPlotStairs(String str, short[] sArr, double d, double d2);

    private static native void nPlotStairs(String str, short[] sArr, double d, double d2, int i);

    private static native void nPlotStairs(String str, short[] sArr, double d, double d2, int i, int i2);

    private static native void nPlotStairs(String str, int[] iArr);

    private static native void nPlotStairs(String str, int[] iArr, double d);

    private static native void nPlotStairs(String str, int[] iArr, double d, double d2);

    private static native void nPlotStairs(String str, int[] iArr, double d, double d2, int i);

    private static native void nPlotStairs(String str, int[] iArr, double d, double d2, int i, int i2);

    private static native void nPlotStairs(String str, long[] jArr);

    private static native void nPlotStairs(String str, long[] jArr, double d);

    private static native void nPlotStairs(String str, long[] jArr, double d, double d2);

    private static native void nPlotStairs(String str, long[] jArr, double d, double d2, int i);

    private static native void nPlotStairs(String str, long[] jArr, double d, double d2, int i, int i2);

    private static native void nPlotStairs(String str, float[] fArr);

    private static native void nPlotStairs(String str, float[] fArr, double d);

    private static native void nPlotStairs(String str, float[] fArr, double d, double d2);

    private static native void nPlotStairs(String str, float[] fArr, double d, double d2, int i);

    private static native void nPlotStairs(String str, float[] fArr, double d, double d2, int i, int i2);

    private static native void nPlotStairs(String str, double[] dArr);

    private static native void nPlotStairs(String str, double[] dArr, double d);

    private static native void nPlotStairs(String str, double[] dArr, double d, double d2);

    private static native void nPlotStairs(String str, double[] dArr, double d, double d2, int i);

    private static native void nPlotStairs(String str, double[] dArr, double d, double d2, int i, int i2);

    private static native void nPlotStairsV(String str, short[] sArr, int i);

    private static native void nPlotStairsV(String str, short[] sArr, int i, double d);

    private static native void nPlotStairsV(String str, short[] sArr, int i, double d, double d2);

    private static native void nPlotStairsV(String str, short[] sArr, int i, double d, double d2, int i2);

    private static native void nPlotStairsV(String str, short[] sArr, int i, double d, double d2, int i2, int i3);

    private static native void nPlotStairsV(String str, int[] iArr, int i);

    private static native void nPlotStairsV(String str, int[] iArr, int i, double d);

    private static native void nPlotStairsV(String str, int[] iArr, int i, double d, double d2);

    private static native void nPlotStairsV(String str, int[] iArr, int i, double d, double d2, int i2);

    private static native void nPlotStairsV(String str, int[] iArr, int i, double d, double d2, int i2, int i3);

    private static native void nPlotStairsV(String str, long[] jArr, int i);

    private static native void nPlotStairsV(String str, long[] jArr, int i, double d);

    private static native void nPlotStairsV(String str, long[] jArr, int i, double d, double d2);

    private static native void nPlotStairsV(String str, long[] jArr, int i, double d, double d2, int i2);

    private static native void nPlotStairsV(String str, long[] jArr, int i, double d, double d2, int i2, int i3);

    private static native void nPlotStairsV(String str, float[] fArr, int i);

    private static native void nPlotStairsV(String str, float[] fArr, int i, double d);

    private static native void nPlotStairsV(String str, float[] fArr, int i, double d, double d2);

    private static native void nPlotStairsV(String str, float[] fArr, int i, double d, double d2, int i2);

    private static native void nPlotStairsV(String str, float[] fArr, int i, double d, double d2, int i2, int i3);

    private static native void nPlotStairsV(String str, double[] dArr, int i);

    private static native void nPlotStairsV(String str, double[] dArr, int i, double d);

    private static native void nPlotStairsV(String str, double[] dArr, int i, double d, double d2);

    private static native void nPlotStairsV(String str, double[] dArr, int i, double d, double d2, int i2);

    private static native void nPlotStairsV(String str, double[] dArr, int i, double d, double d2, int i2, int i3);

    private static native void nPlotStairs(String str, short[] sArr, short[] sArr2);

    private static native void nPlotStairs(String str, short[] sArr, short[] sArr2, int i);

    private static native void nPlotStairs(String str, int[] iArr, int[] iArr2);

    private static native void nPlotStairs(String str, int[] iArr, int[] iArr2, int i);

    private static native void nPlotStairs(String str, long[] jArr, long[] jArr2);

    private static native void nPlotStairs(String str, long[] jArr, long[] jArr2, int i);

    private static native void nPlotStairs(String str, float[] fArr, float[] fArr2);

    private static native void nPlotStairs(String str, float[] fArr, float[] fArr2, int i);

    private static native void nPlotStairs(String str, double[] dArr, double[] dArr2);

    private static native void nPlotStairs(String str, double[] dArr, double[] dArr2, int i);

    private static native void nPlotStairsV(String str, short[] sArr, short[] sArr2, int i);

    private static native void nPlotStairsV(String str, short[] sArr, short[] sArr2, int i, int i2);

    private static native void nPlotStairsV(String str, short[] sArr, short[] sArr2, int i, int i2, int i3);

    private static native void nPlotStairsV(String str, int[] iArr, int[] iArr2, int i);

    private static native void nPlotStairsV(String str, int[] iArr, int[] iArr2, int i, int i2);

    private static native void nPlotStairsV(String str, int[] iArr, int[] iArr2, int i, int i2, int i3);

    private static native void nPlotStairsV(String str, long[] jArr, long[] jArr2, int i);

    private static native void nPlotStairsV(String str, long[] jArr, long[] jArr2, int i, int i2);

    private static native void nPlotStairsV(String str, long[] jArr, long[] jArr2, int i, int i2, int i3);

    private static native void nPlotStairsV(String str, float[] fArr, float[] fArr2, int i);

    private static native void nPlotStairsV(String str, float[] fArr, float[] fArr2, int i, int i2);

    private static native void nPlotStairsV(String str, float[] fArr, float[] fArr2, int i, int i2, int i3);

    private static native void nPlotStairsV(String str, double[] dArr, double[] dArr2, int i);

    private static native void nPlotStairsV(String str, double[] dArr, double[] dArr2, int i, int i2);

    private static native void nPlotStairsV(String str, double[] dArr, double[] dArr2, int i, int i2, int i3);

    private static native void nPlotShaded(String str, short[] sArr);

    private static native void nPlotShaded(String str, short[] sArr, double d);

    private static native void nPlotShaded(String str, short[] sArr, double d, double d2);

    private static native void nPlotShaded(String str, short[] sArr, double d, double d2, double d3);

    private static native void nPlotShaded(String str, short[] sArr, double d, double d2, double d3, int i);

    private static native void nPlotShaded(String str, short[] sArr, double d, double d2, double d3, int i, int i2);

    private static native void nPlotShaded(String str, int[] iArr);

    private static native void nPlotShaded(String str, int[] iArr, double d);

    private static native void nPlotShaded(String str, int[] iArr, double d, double d2);

    private static native void nPlotShaded(String str, int[] iArr, double d, double d2, double d3);

    private static native void nPlotShaded(String str, int[] iArr, double d, double d2, double d3, int i);

    private static native void nPlotShaded(String str, int[] iArr, double d, double d2, double d3, int i, int i2);

    private static native void nPlotShaded(String str, long[] jArr);

    private static native void nPlotShaded(String str, long[] jArr, double d);

    private static native void nPlotShaded(String str, long[] jArr, double d, double d2);

    private static native void nPlotShaded(String str, long[] jArr, double d, double d2, double d3);

    private static native void nPlotShaded(String str, long[] jArr, double d, double d2, double d3, int i);

    private static native void nPlotShaded(String str, long[] jArr, double d, double d2, double d3, int i, int i2);

    private static native void nPlotShaded(String str, float[] fArr);

    private static native void nPlotShaded(String str, float[] fArr, double d);

    private static native void nPlotShaded(String str, float[] fArr, double d, double d2);

    private static native void nPlotShaded(String str, float[] fArr, double d, double d2, double d3);

    private static native void nPlotShaded(String str, float[] fArr, double d, double d2, double d3, int i);

    private static native void nPlotShaded(String str, float[] fArr, double d, double d2, double d3, int i, int i2);

    private static native void nPlotShaded(String str, double[] dArr);

    private static native void nPlotShaded(String str, double[] dArr, double d);

    private static native void nPlotShaded(String str, double[] dArr, double d, double d2);

    private static native void nPlotShaded(String str, double[] dArr, double d, double d2, double d3);

    private static native void nPlotShaded(String str, double[] dArr, double d, double d2, double d3, int i);

    private static native void nPlotShaded(String str, double[] dArr, double d, double d2, double d3, int i, int i2);

    private static native void nPlotShadedV(String str, short[] sArr, int i);

    private static native void nPlotShadedV(String str, short[] sArr, int i, double d);

    private static native void nPlotShadedV(String str, short[] sArr, int i, double d, double d2);

    private static native void nPlotShadedV(String str, short[] sArr, int i, double d, double d2, double d3);

    private static native void nPlotShadedV(String str, short[] sArr, int i, double d, double d2, double d3, int i2);

    private static native void nPlotShadedV(String str, short[] sArr, int i, double d, double d2, double d3, int i2, int i3);

    private static native void nPlotShadedV(String str, int[] iArr, int i);

    private static native void nPlotShadedV(String str, int[] iArr, int i, double d);

    private static native void nPlotShadedV(String str, int[] iArr, int i, double d, double d2);

    private static native void nPlotShadedV(String str, int[] iArr, int i, double d, double d2, double d3);

    private static native void nPlotShadedV(String str, int[] iArr, int i, double d, double d2, double d3, int i2);

    private static native void nPlotShadedV(String str, int[] iArr, int i, double d, double d2, double d3, int i2, int i3);

    private static native void nPlotShadedV(String str, long[] jArr, int i);

    private static native void nPlotShadedV(String str, long[] jArr, int i, double d);

    private static native void nPlotShadedV(String str, long[] jArr, int i, double d, double d2);

    private static native void nPlotShadedV(String str, long[] jArr, int i, double d, double d2, double d3);

    private static native void nPlotShadedV(String str, long[] jArr, int i, double d, double d2, double d3, int i2);

    private static native void nPlotShadedV(String str, long[] jArr, int i, double d, double d2, double d3, int i2, int i3);

    private static native void nPlotShadedV(String str, float[] fArr, int i);

    private static native void nPlotShadedV(String str, float[] fArr, int i, double d);

    private static native void nPlotShadedV(String str, float[] fArr, int i, double d, double d2);

    private static native void nPlotShadedV(String str, float[] fArr, int i, double d, double d2, double d3);

    private static native void nPlotShadedV(String str, float[] fArr, int i, double d, double d2, double d3, int i2);

    private static native void nPlotShadedV(String str, float[] fArr, int i, double d, double d2, double d3, int i2, int i3);

    private static native void nPlotShadedV(String str, double[] dArr, int i);

    private static native void nPlotShadedV(String str, double[] dArr, int i, double d);

    private static native void nPlotShadedV(String str, double[] dArr, int i, double d, double d2);

    private static native void nPlotShadedV(String str, double[] dArr, int i, double d, double d2, double d3);

    private static native void nPlotShadedV(String str, double[] dArr, int i, double d, double d2, double d3, int i2);

    private static native void nPlotShadedV(String str, double[] dArr, int i, double d, double d2, double d3, int i2, int i3);

    private static native void nPlotShaded(String str, short[] sArr, short[] sArr2);

    private static native void nPlotShaded(String str, short[] sArr, short[] sArr2, double d);

    private static native void nPlotShaded(String str, short[] sArr, short[] sArr2, double d, int i);

    private static native void nPlotShaded(String str, short[] sArr, short[] sArr2, double d, int i, int i2);

    private static native void nPlotShaded(String str, int[] iArr, int[] iArr2);

    private static native void nPlotShaded(String str, int[] iArr, int[] iArr2, double d);

    private static native void nPlotShaded(String str, int[] iArr, int[] iArr2, double d, int i);

    private static native void nPlotShaded(String str, int[] iArr, int[] iArr2, double d, int i, int i2);

    private static native void nPlotShaded(String str, long[] jArr, long[] jArr2);

    private static native void nPlotShaded(String str, long[] jArr, long[] jArr2, double d);

    private static native void nPlotShaded(String str, long[] jArr, long[] jArr2, double d, int i);

    private static native void nPlotShaded(String str, long[] jArr, long[] jArr2, double d, int i, int i2);

    private static native void nPlotShaded(String str, float[] fArr, float[] fArr2);

    private static native void nPlotShaded(String str, float[] fArr, float[] fArr2, double d);

    private static native void nPlotShaded(String str, float[] fArr, float[] fArr2, double d, int i);

    private static native void nPlotShaded(String str, float[] fArr, float[] fArr2, double d, int i, int i2);

    private static native void nPlotShaded(String str, double[] dArr, double[] dArr2);

    private static native void nPlotShaded(String str, double[] dArr, double[] dArr2, double d);

    private static native void nPlotShaded(String str, double[] dArr, double[] dArr2, double d, int i);

    private static native void nPlotShaded(String str, double[] dArr, double[] dArr2, double d, int i, int i2);

    private static native void nPlotShadedV(String str, short[] sArr, short[] sArr2, int i);

    private static native void nPlotShadedV(String str, short[] sArr, short[] sArr2, int i, double d);

    private static native void nPlotShadedV(String str, short[] sArr, short[] sArr2, int i, double d, int i2);

    private static native void nPlotShadedV(String str, short[] sArr, short[] sArr2, int i, double d, int i2, int i3);

    private static native void nPlotShadedV(String str, int[] iArr, int[] iArr2, int i);

    private static native void nPlotShadedV(String str, int[] iArr, int[] iArr2, int i, double d);

    private static native void nPlotShadedV(String str, int[] iArr, int[] iArr2, int i, double d, int i2);

    private static native void nPlotShadedV(String str, int[] iArr, int[] iArr2, int i, double d, int i2, int i3);

    private static native void nPlotShadedV(String str, long[] jArr, long[] jArr2, int i);

    private static native void nPlotShadedV(String str, long[] jArr, long[] jArr2, int i, double d);

    private static native void nPlotShadedV(String str, long[] jArr, long[] jArr2, int i, double d, int i2);

    private static native void nPlotShadedV(String str, long[] jArr, long[] jArr2, int i, double d, int i2, int i3);

    private static native void nPlotShadedV(String str, float[] fArr, float[] fArr2, int i);

    private static native void nPlotShadedV(String str, float[] fArr, float[] fArr2, int i, double d);

    private static native void nPlotShadedV(String str, float[] fArr, float[] fArr2, int i, double d, int i2);

    private static native void nPlotShadedV(String str, float[] fArr, float[] fArr2, int i, double d, int i2, int i3);

    private static native void nPlotShadedV(String str, double[] dArr, double[] dArr2, int i);

    private static native void nPlotShadedV(String str, double[] dArr, double[] dArr2, int i, double d);

    private static native void nPlotShadedV(String str, double[] dArr, double[] dArr2, int i, double d, int i2);

    private static native void nPlotShadedV(String str, double[] dArr, double[] dArr2, int i, double d, int i2, int i3);

    private static native void nPlotShaded(String str, short[] sArr, short[] sArr2, short[] sArr3);

    private static native void nPlotShaded(String str, short[] sArr, short[] sArr2, short[] sArr3, int i);

    private static native void nPlotShaded(String str, int[] iArr, int[] iArr2, int[] iArr3);

    private static native void nPlotShaded(String str, int[] iArr, int[] iArr2, int[] iArr3, int i);

    private static native void nPlotShaded(String str, long[] jArr, long[] jArr2, long[] jArr3);

    private static native void nPlotShaded(String str, long[] jArr, long[] jArr2, long[] jArr3, int i);

    private static native void nPlotShaded(String str, float[] fArr, float[] fArr2, float[] fArr3);

    private static native void nPlotShaded(String str, float[] fArr, float[] fArr2, float[] fArr3, int i);

    private static native void nPlotShaded(String str, double[] dArr, double[] dArr2, double[] dArr3);

    private static native void nPlotShaded(String str, double[] dArr, double[] dArr2, double[] dArr3, int i);

    private static native void nPlotShadedV(String str, short[] sArr, short[] sArr2, short[] sArr3, int i);

    private static native void nPlotShadedV(String str, short[] sArr, short[] sArr2, short[] sArr3, int i, int i2);

    private static native void nPlotShadedV(String str, short[] sArr, short[] sArr2, short[] sArr3, int i, int i2, int i3);

    private static native void nPlotShadedV(String str, int[] iArr, int[] iArr2, int[] iArr3, int i);

    private static native void nPlotShadedV(String str, int[] iArr, int[] iArr2, int[] iArr3, int i, int i2);

    private static native void nPlotShadedV(String str, int[] iArr, int[] iArr2, int[] iArr3, int i, int i2, int i3);

    private static native void nPlotShadedV(String str, long[] jArr, long[] jArr2, long[] jArr3, int i);

    private static native void nPlotShadedV(String str, long[] jArr, long[] jArr2, long[] jArr3, int i, int i2);

    private static native void nPlotShadedV(String str, long[] jArr, long[] jArr2, long[] jArr3, int i, int i2, int i3);

    private static native void nPlotShadedV(String str, float[] fArr, float[] fArr2, float[] fArr3, int i);

    private static native void nPlotShadedV(String str, float[] fArr, float[] fArr2, float[] fArr3, int i, int i2);

    private static native void nPlotShadedV(String str, float[] fArr, float[] fArr2, float[] fArr3, int i, int i2, int i3);

    private static native void nPlotShadedV(String str, double[] dArr, double[] dArr2, double[] dArr3, int i);

    private static native void nPlotShadedV(String str, double[] dArr, double[] dArr2, double[] dArr3, int i, int i2);

    private static native void nPlotShadedV(String str, double[] dArr, double[] dArr2, double[] dArr3, int i, int i2, int i3);

    private static native void nPlotBars(String str, short[] sArr);

    private static native void nPlotBars(String str, short[] sArr, double d);

    private static native void nPlotBars(String str, short[] sArr, double d, double d2);

    private static native void nPlotBars(String str, short[] sArr, double d, double d2, int i);

    private static native void nPlotBars(String str, short[] sArr, double d, double d2, int i, int i2);

    private static native void nPlotBars(String str, int[] iArr);

    private static native void nPlotBars(String str, int[] iArr, double d);

    private static native void nPlotBars(String str, int[] iArr, double d, double d2);

    private static native void nPlotBars(String str, int[] iArr, double d, double d2, int i);

    private static native void nPlotBars(String str, int[] iArr, double d, double d2, int i, int i2);

    private static native void nPlotBars(String str, long[] jArr);

    private static native void nPlotBars(String str, long[] jArr, double d);

    private static native void nPlotBars(String str, long[] jArr, double d, double d2);

    private static native void nPlotBars(String str, long[] jArr, double d, double d2, int i);

    private static native void nPlotBars(String str, long[] jArr, double d, double d2, int i, int i2);

    private static native void nPlotBars(String str, float[] fArr);

    private static native void nPlotBars(String str, float[] fArr, double d);

    private static native void nPlotBars(String str, float[] fArr, double d, double d2);

    private static native void nPlotBars(String str, float[] fArr, double d, double d2, int i);

    private static native void nPlotBars(String str, float[] fArr, double d, double d2, int i, int i2);

    private static native void nPlotBars(String str, double[] dArr);

    private static native void nPlotBars(String str, double[] dArr, double d);

    private static native void nPlotBars(String str, double[] dArr, double d, double d2);

    private static native void nPlotBars(String str, double[] dArr, double d, double d2, int i);

    private static native void nPlotBars(String str, double[] dArr, double d, double d2, int i, int i2);

    private static native void nPlotBarsV(String str, short[] sArr, int i);

    private static native void nPlotBarsV(String str, short[] sArr, int i, double d);

    private static native void nPlotBarsV(String str, short[] sArr, int i, double d, double d2);

    private static native void nPlotBarsV(String str, short[] sArr, int i, double d, double d2, int i2);

    private static native void nPlotBarsV(String str, short[] sArr, int i, double d, double d2, int i2, int i3);

    private static native void nPlotBarsV(String str, int[] iArr, int i);

    private static native void nPlotBarsV(String str, int[] iArr, int i, double d);

    private static native void nPlotBarsV(String str, int[] iArr, int i, double d, double d2);

    private static native void nPlotBarsV(String str, int[] iArr, int i, double d, double d2, int i2);

    private static native void nPlotBarsV(String str, int[] iArr, int i, double d, double d2, int i2, int i3);

    private static native void nPlotBarsV(String str, long[] jArr, int i);

    private static native void nPlotBarsV(String str, long[] jArr, int i, double d);

    private static native void nPlotBarsV(String str, long[] jArr, int i, double d, double d2);

    private static native void nPlotBarsV(String str, long[] jArr, int i, double d, double d2, int i2);

    private static native void nPlotBarsV(String str, long[] jArr, int i, double d, double d2, int i2, int i3);

    private static native void nPlotBarsV(String str, float[] fArr, int i);

    private static native void nPlotBarsV(String str, float[] fArr, int i, double d);

    private static native void nPlotBarsV(String str, float[] fArr, int i, double d, double d2);

    private static native void nPlotBarsV(String str, float[] fArr, int i, double d, double d2, int i2);

    private static native void nPlotBarsV(String str, float[] fArr, int i, double d, double d2, int i2, int i3);

    private static native void nPlotBarsV(String str, double[] dArr, int i);

    private static native void nPlotBarsV(String str, double[] dArr, int i, double d);

    private static native void nPlotBarsV(String str, double[] dArr, int i, double d, double d2);

    private static native void nPlotBarsV(String str, double[] dArr, int i, double d, double d2, int i2);

    private static native void nPlotBarsV(String str, double[] dArr, int i, double d, double d2, int i2, int i3);

    private static native void nPlotBars(String str, short[] sArr, short[] sArr2);

    private static native void nPlotBars(String str, short[] sArr, short[] sArr2, int i);

    private static native void nPlotBars(String str, short[] sArr, short[] sArr2, int i, int i2);

    private static native void nPlotBars(String str, int[] iArr, int[] iArr2);

    private static native void nPlotBars(String str, int[] iArr, int[] iArr2, int i);

    private static native void nPlotBars(String str, int[] iArr, int[] iArr2, int i, int i2);

    private static native void nPlotBars(String str, long[] jArr, long[] jArr2);

    private static native void nPlotBars(String str, long[] jArr, long[] jArr2, int i);

    private static native void nPlotBars(String str, long[] jArr, long[] jArr2, int i, int i2);

    private static native void nPlotBars(String str, float[] fArr, float[] fArr2);

    private static native void nPlotBars(String str, float[] fArr, float[] fArr2, int i);

    private static native void nPlotBars(String str, float[] fArr, float[] fArr2, int i, int i2);

    private static native void nPlotBars(String str, double[] dArr, double[] dArr2);

    private static native void nPlotBars(String str, double[] dArr, double[] dArr2, int i);

    private static native void nPlotBars(String str, double[] dArr, double[] dArr2, int i, int i2);

    private static native void nPlotBarsV(String str, short[] sArr, short[] sArr2, double d);

    private static native void nPlotBarsV(String str, short[] sArr, short[] sArr2, double d, int i);

    private static native void nPlotBarsV(String str, short[] sArr, short[] sArr2, double d, int i, int i2);

    private static native void nPlotBarsV(String str, int[] iArr, int[] iArr2, double d);

    private static native void nPlotBarsV(String str, int[] iArr, int[] iArr2, double d, int i);

    private static native void nPlotBarsV(String str, int[] iArr, int[] iArr2, double d, int i, int i2);

    private static native void nPlotBarsV(String str, long[] jArr, long[] jArr2, double d);

    private static native void nPlotBarsV(String str, long[] jArr, long[] jArr2, double d, int i);

    private static native void nPlotBarsV(String str, long[] jArr, long[] jArr2, double d, int i, int i2);

    private static native void nPlotBarsV(String str, float[] fArr, float[] fArr2, double d);

    private static native void nPlotBarsV(String str, float[] fArr, float[] fArr2, double d, int i);

    private static native void nPlotBarsV(String str, float[] fArr, float[] fArr2, double d, int i, int i2);

    private static native void nPlotBarsV(String str, double[] dArr, double[] dArr2, double d);

    private static native void nPlotBarsV(String str, double[] dArr, double[] dArr2, double d, int i);

    private static native void nPlotBarsV(String str, double[] dArr, double[] dArr2, double d, int i, int i2);

    private static native void nPlotBarsV(String str, short[] sArr, short[] sArr2, int i, double d);

    private static native void nPlotBarsV(String str, short[] sArr, short[] sArr2, int i, double d, int i2);

    private static native void nPlotBarsV(String str, short[] sArr, short[] sArr2, int i, double d, int i2, int i3);

    private static native void nPlotBarsV(String str, int[] iArr, int[] iArr2, int i, double d);

    private static native void nPlotBarsV(String str, int[] iArr, int[] iArr2, int i, double d, int i2);

    private static native void nPlotBarsV(String str, int[] iArr, int[] iArr2, int i, double d, int i2, int i3);

    private static native void nPlotBarsV(String str, long[] jArr, long[] jArr2, int i, double d);

    private static native void nPlotBarsV(String str, long[] jArr, long[] jArr2, int i, double d, int i2);

    private static native void nPlotBarsV(String str, long[] jArr, long[] jArr2, int i, double d, int i2, int i3);

    private static native void nPlotBarsV(String str, float[] fArr, float[] fArr2, int i, double d);

    private static native void nPlotBarsV(String str, float[] fArr, float[] fArr2, int i, double d, int i2);

    private static native void nPlotBarsV(String str, float[] fArr, float[] fArr2, int i, double d, int i2, int i3);

    private static native void nPlotBarsV(String str, double[] dArr, double[] dArr2, int i, double d);

    private static native void nPlotBarsV(String str, double[] dArr, double[] dArr2, int i, double d, int i2);

    private static native void nPlotBarsV(String str, double[] dArr, double[] dArr2, int i, double d, int i2, int i3);

    private static native void nPlotBarGroups(String[] strArr, int i, short[] sArr, int i2);

    private static native void nPlotBarGroups(String[] strArr, int i, short[] sArr, int i2, double d);

    private static native void nPlotBarGroups(String[] strArr, int i, short[] sArr, int i2, double d, double d2);

    private static native void nPlotBarGroups(String[] strArr, int i, short[] sArr, int i2, double d, double d2, int i3);

    private static native void nPlotBarGroups(String[] strArr, int i, int[] iArr, int i2);

    private static native void nPlotBarGroups(String[] strArr, int i, int[] iArr, int i2, double d);

    private static native void nPlotBarGroups(String[] strArr, int i, int[] iArr, int i2, double d, double d2);

    private static native void nPlotBarGroups(String[] strArr, int i, int[] iArr, int i2, double d, double d2, int i3);

    private static native void nPlotBarGroups(String[] strArr, int i, long[] jArr, int i2);

    private static native void nPlotBarGroups(String[] strArr, int i, long[] jArr, int i2, double d);

    private static native void nPlotBarGroups(String[] strArr, int i, long[] jArr, int i2, double d, double d2);

    private static native void nPlotBarGroups(String[] strArr, int i, long[] jArr, int i2, double d, double d2, int i3);

    private static native void nPlotBarGroups(String[] strArr, int i, float[] fArr, int i2);

    private static native void nPlotBarGroups(String[] strArr, int i, float[] fArr, int i2, double d);

    private static native void nPlotBarGroups(String[] strArr, int i, float[] fArr, int i2, double d, double d2);

    private static native void nPlotBarGroups(String[] strArr, int i, float[] fArr, int i2, double d, double d2, int i3);

    private static native void nPlotBarGroups(String[] strArr, int i, double[] dArr, int i2);

    private static native void nPlotBarGroups(String[] strArr, int i, double[] dArr, int i2, double d);

    private static native void nPlotBarGroups(String[] strArr, int i, double[] dArr, int i2, double d, double d2);

    private static native void nPlotBarGroups(String[] strArr, int i, double[] dArr, int i2, double d, double d2, int i3);

    private static native void nPlotBarGroupsV(String[] strArr, int i, short[] sArr, int i2, int i3);

    private static native void nPlotBarGroupsV(String[] strArr, int i, short[] sArr, int i2, int i3, double d);

    private static native void nPlotBarGroupsV(String[] strArr, int i, short[] sArr, int i2, int i3, double d, double d2);

    private static native void nPlotBarGroupsV(String[] strArr, int i, short[] sArr, int i2, int i3, double d, double d2, int i4);

    private static native void nPlotBarGroupsV(String[] strArr, int i, int[] iArr, int i2, int i3);

    private static native void nPlotBarGroupsV(String[] strArr, int i, int[] iArr, int i2, int i3, double d);

    private static native void nPlotBarGroupsV(String[] strArr, int i, int[] iArr, int i2, int i3, double d, double d2);

    private static native void nPlotBarGroupsV(String[] strArr, int i, int[] iArr, int i2, int i3, double d, double d2, int i4);

    private static native void nPlotBarGroupsV(String[] strArr, int i, long[] jArr, int i2, int i3);

    private static native void nPlotBarGroupsV(String[] strArr, int i, long[] jArr, int i2, int i3, double d);

    private static native void nPlotBarGroupsV(String[] strArr, int i, long[] jArr, int i2, int i3, double d, double d2);

    private static native void nPlotBarGroupsV(String[] strArr, int i, long[] jArr, int i2, int i3, double d, double d2, int i4);

    private static native void nPlotBarGroupsV(String[] strArr, int i, float[] fArr, int i2, int i3);

    private static native void nPlotBarGroupsV(String[] strArr, int i, float[] fArr, int i2, int i3, double d);

    private static native void nPlotBarGroupsV(String[] strArr, int i, float[] fArr, int i2, int i3, double d, double d2);

    private static native void nPlotBarGroupsV(String[] strArr, int i, float[] fArr, int i2, int i3, double d, double d2, int i4);

    private static native void nPlotBarGroupsV(String[] strArr, int i, double[] dArr, int i2, int i3);

    private static native void nPlotBarGroupsV(String[] strArr, int i, double[] dArr, int i2, int i3, double d);

    private static native void nPlotBarGroupsV(String[] strArr, int i, double[] dArr, int i2, int i3, double d, double d2);

    private static native void nPlotBarGroupsV(String[] strArr, int i, double[] dArr, int i2, int i3, double d, double d2, int i4);

    private static native void nPlotErrorBars(String str, short[] sArr, short[] sArr2, short[] sArr3);

    private static native void nPlotErrorBars(String str, short[] sArr, short[] sArr2, short[] sArr3, int i);

    private static native void nPlotErrorBars(String str, short[] sArr, short[] sArr2, short[] sArr3, int i, int i2);

    private static native void nPlotErrorBars(String str, int[] iArr, int[] iArr2, int[] iArr3);

    private static native void nPlotErrorBars(String str, int[] iArr, int[] iArr2, int[] iArr3, int i);

    private static native void nPlotErrorBars(String str, int[] iArr, int[] iArr2, int[] iArr3, int i, int i2);

    private static native void nPlotErrorBars(String str, long[] jArr, long[] jArr2, long[] jArr3);

    private static native void nPlotErrorBars(String str, long[] jArr, long[] jArr2, long[] jArr3, int i);

    private static native void nPlotErrorBars(String str, long[] jArr, long[] jArr2, long[] jArr3, int i, int i2);

    private static native void nPlotErrorBars(String str, float[] fArr, float[] fArr2, float[] fArr3);

    private static native void nPlotErrorBars(String str, float[] fArr, float[] fArr2, float[] fArr3, int i);

    private static native void nPlotErrorBars(String str, float[] fArr, float[] fArr2, float[] fArr3, int i, int i2);

    private static native void nPlotErrorBars(String str, double[] dArr, double[] dArr2, double[] dArr3);

    private static native void nPlotErrorBars(String str, double[] dArr, double[] dArr2, double[] dArr3, int i);

    private static native void nPlotErrorBars(String str, double[] dArr, double[] dArr2, double[] dArr3, int i, int i2);

    private static native void nPlotErrorBarsV(String str, short[] sArr, short[] sArr2, short[] sArr3, int i);

    private static native void nPlotErrorBarsV(String str, short[] sArr, short[] sArr2, short[] sArr3, int i, int i2);

    private static native void nPlotErrorBarsV(String str, short[] sArr, short[] sArr2, short[] sArr3, int i, int i2, int i3);

    private static native void nPlotErrorBarsV(String str, int[] iArr, int[] iArr2, int[] iArr3, int i);

    private static native void nPlotErrorBarsV(String str, int[] iArr, int[] iArr2, int[] iArr3, int i, int i2);

    private static native void nPlotErrorBarsV(String str, int[] iArr, int[] iArr2, int[] iArr3, int i, int i2, int i3);

    private static native void nPlotErrorBarsV(String str, long[] jArr, long[] jArr2, long[] jArr3, int i);

    private static native void nPlotErrorBarsV(String str, long[] jArr, long[] jArr2, long[] jArr3, int i, int i2);

    private static native void nPlotErrorBarsV(String str, long[] jArr, long[] jArr2, long[] jArr3, int i, int i2, int i3);

    private static native void nPlotErrorBarsV(String str, float[] fArr, float[] fArr2, float[] fArr3, int i);

    private static native void nPlotErrorBarsV(String str, float[] fArr, float[] fArr2, float[] fArr3, int i, int i2);

    private static native void nPlotErrorBarsV(String str, float[] fArr, float[] fArr2, float[] fArr3, int i, int i2, int i3);

    private static native void nPlotErrorBarsV(String str, double[] dArr, double[] dArr2, double[] dArr3, int i);

    private static native void nPlotErrorBarsV(String str, double[] dArr, double[] dArr2, double[] dArr3, int i, int i2);

    private static native void nPlotErrorBarsV(String str, double[] dArr, double[] dArr2, double[] dArr3, int i, int i2, int i3);

    private static native void nPlotErrorBars(String str, short[] sArr, short[] sArr2, short[] sArr3, short[] sArr4);

    private static native void nPlotErrorBars(String str, short[] sArr, short[] sArr2, short[] sArr3, short[] sArr4, int i);

    private static native void nPlotErrorBars(String str, short[] sArr, short[] sArr2, short[] sArr3, short[] sArr4, int i, int i2);

    private static native void nPlotErrorBars(String str, int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4);

    private static native void nPlotErrorBars(String str, int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int i);

    private static native void nPlotErrorBars(String str, int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int i, int i2);

    private static native void nPlotErrorBars(String str, long[] jArr, long[] jArr2, long[] jArr3, long[] jArr4);

    private static native void nPlotErrorBars(String str, long[] jArr, long[] jArr2, long[] jArr3, long[] jArr4, int i);

    private static native void nPlotErrorBars(String str, long[] jArr, long[] jArr2, long[] jArr3, long[] jArr4, int i, int i2);

    private static native void nPlotErrorBars(String str, float[] fArr, float[] fArr2, float[] fArr3, float[] fArr4);

    private static native void nPlotErrorBars(String str, float[] fArr, float[] fArr2, float[] fArr3, float[] fArr4, int i);

    private static native void nPlotErrorBars(String str, float[] fArr, float[] fArr2, float[] fArr3, float[] fArr4, int i, int i2);

    private static native void nPlotErrorBars(String str, double[] dArr, double[] dArr2, double[] dArr3, double[] dArr4);

    private static native void nPlotErrorBars(String str, double[] dArr, double[] dArr2, double[] dArr3, double[] dArr4, int i);

    private static native void nPlotErrorBars(String str, double[] dArr, double[] dArr2, double[] dArr3, double[] dArr4, int i, int i2);

    private static native void nPlotErrorBarsV(String str, short[] sArr, short[] sArr2, short[] sArr3, short[] sArr4, int i);

    private static native void nPlotErrorBarsV(String str, short[] sArr, short[] sArr2, short[] sArr3, short[] sArr4, int i, int i2);

    private static native void nPlotErrorBarsV(String str, short[] sArr, short[] sArr2, short[] sArr3, short[] sArr4, int i, int i2, int i3);

    private static native void nPlotErrorBarsV(String str, int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int i);

    private static native void nPlotErrorBarsV(String str, int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int i, int i2);

    private static native void nPlotErrorBarsV(String str, int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int i, int i2, int i3);

    private static native void nPlotErrorBarsV(String str, long[] jArr, long[] jArr2, long[] jArr3, long[] jArr4, int i);

    private static native void nPlotErrorBarsV(String str, long[] jArr, long[] jArr2, long[] jArr3, long[] jArr4, int i, int i2);

    private static native void nPlotErrorBarsV(String str, long[] jArr, long[] jArr2, long[] jArr3, long[] jArr4, int i, int i2, int i3);

    private static native void nPlotErrorBarsV(String str, float[] fArr, float[] fArr2, float[] fArr3, float[] fArr4, int i);

    private static native void nPlotErrorBarsV(String str, float[] fArr, float[] fArr2, float[] fArr3, float[] fArr4, int i, int i2);

    private static native void nPlotErrorBarsV(String str, float[] fArr, float[] fArr2, float[] fArr3, float[] fArr4, int i, int i2, int i3);

    private static native void nPlotErrorBarsV(String str, double[] dArr, double[] dArr2, double[] dArr3, double[] dArr4, int i);

    private static native void nPlotErrorBarsV(String str, double[] dArr, double[] dArr2, double[] dArr3, double[] dArr4, int i, int i2);

    private static native void nPlotErrorBarsV(String str, double[] dArr, double[] dArr2, double[] dArr3, double[] dArr4, int i, int i2, int i3);

    private static native void nPlotStems(String str, short[] sArr);

    private static native void nPlotStems(String str, short[] sArr, double d);

    private static native void nPlotStems(String str, short[] sArr, double d, double d2);

    private static native void nPlotStems(String str, short[] sArr, double d, double d2, double d3);

    private static native void nPlotStems(String str, short[] sArr, double d, double d2, double d3, int i);

    private static native void nPlotStems(String str, short[] sArr, double d, double d2, double d3, int i, int i2);

    private static native void nPlotStems(String str, int[] iArr);

    private static native void nPlotStems(String str, int[] iArr, double d);

    private static native void nPlotStems(String str, int[] iArr, double d, double d2);

    private static native void nPlotStems(String str, int[] iArr, double d, double d2, double d3);

    private static native void nPlotStems(String str, int[] iArr, double d, double d2, double d3, int i);

    private static native void nPlotStems(String str, int[] iArr, double d, double d2, double d3, int i, int i2);

    private static native void nPlotStems(String str, long[] jArr);

    private static native void nPlotStems(String str, long[] jArr, double d);

    private static native void nPlotStems(String str, long[] jArr, double d, double d2);

    private static native void nPlotStems(String str, long[] jArr, double d, double d2, double d3);

    private static native void nPlotStems(String str, long[] jArr, double d, double d2, double d3, int i);

    private static native void nPlotStems(String str, long[] jArr, double d, double d2, double d3, int i, int i2);

    private static native void nPlotStems(String str, float[] fArr);

    private static native void nPlotStems(String str, float[] fArr, double d);

    private static native void nPlotStems(String str, float[] fArr, double d, double d2);

    private static native void nPlotStems(String str, float[] fArr, double d, double d2, double d3);

    private static native void nPlotStems(String str, float[] fArr, double d, double d2, double d3, int i);

    private static native void nPlotStems(String str, float[] fArr, double d, double d2, double d3, int i, int i2);

    private static native void nPlotStems(String str, double[] dArr);

    private static native void nPlotStems(String str, double[] dArr, double d);

    private static native void nPlotStems(String str, double[] dArr, double d, double d2);

    private static native void nPlotStems(String str, double[] dArr, double d, double d2, double d3);

    private static native void nPlotStems(String str, double[] dArr, double d, double d2, double d3, int i);

    private static native void nPlotStems(String str, double[] dArr, double d, double d2, double d3, int i, int i2);

    private static native void nPlotStemsV(String str, short[] sArr, int i);

    private static native void nPlotStemsV(String str, short[] sArr, int i, double d);

    private static native void nPlotStemsV(String str, short[] sArr, int i, double d, double d2);

    private static native void nPlotStemsV(String str, short[] sArr, int i, double d, double d2, double d3);

    private static native void nPlotStemsV(String str, short[] sArr, int i, double d, double d2, double d3, int i2);

    private static native void nPlotStemsV(String str, short[] sArr, int i, double d, double d2, double d3, int i2, int i3);

    private static native void nPlotStemsV(String str, int[] iArr, int i);

    private static native void nPlotStemsV(String str, int[] iArr, int i, double d);

    private static native void nPlotStemsV(String str, int[] iArr, int i, double d, double d2);

    private static native void nPlotStemsV(String str, int[] iArr, int i, double d, double d2, double d3);

    private static native void nPlotStemsV(String str, int[] iArr, int i, double d, double d2, double d3, int i2);

    private static native void nPlotStemsV(String str, int[] iArr, int i, double d, double d2, double d3, int i2, int i3);

    private static native void nPlotStemsV(String str, long[] jArr, int i);

    private static native void nPlotStemsV(String str, long[] jArr, int i, double d);

    private static native void nPlotStemsV(String str, long[] jArr, int i, double d, double d2);

    private static native void nPlotStemsV(String str, long[] jArr, int i, double d, double d2, double d3);

    private static native void nPlotStemsV(String str, long[] jArr, int i, double d, double d2, double d3, int i2);

    private static native void nPlotStemsV(String str, long[] jArr, int i, double d, double d2, double d3, int i2, int i3);

    private static native void nPlotStemsV(String str, float[] fArr, int i);

    private static native void nPlotStemsV(String str, float[] fArr, int i, double d);

    private static native void nPlotStemsV(String str, float[] fArr, int i, double d, double d2);

    private static native void nPlotStemsV(String str, float[] fArr, int i, double d, double d2, double d3);

    private static native void nPlotStemsV(String str, float[] fArr, int i, double d, double d2, double d3, int i2);

    private static native void nPlotStemsV(String str, float[] fArr, int i, double d, double d2, double d3, int i2, int i3);

    private static native void nPlotStemsV(String str, double[] dArr, int i);

    private static native void nPlotStemsV(String str, double[] dArr, int i, double d);

    private static native void nPlotStemsV(String str, double[] dArr, int i, double d, double d2);

    private static native void nPlotStemsV(String str, double[] dArr, int i, double d, double d2, double d3);

    private static native void nPlotStemsV(String str, double[] dArr, int i, double d, double d2, double d3, int i2);

    private static native void nPlotStemsV(String str, double[] dArr, int i, double d, double d2, double d3, int i2, int i3);

    private static native void nPlotStems(String str, short[] sArr, short[] sArr2);

    private static native void nPlotStems(String str, short[] sArr, short[] sArr2, double d);

    private static native void nPlotStems(String str, short[] sArr, short[] sArr2, double d, int i);

    private static native void nPlotStems(String str, short[] sArr, short[] sArr2, double d, int i, int i2);

    private static native void nPlotStems(String str, int[] iArr, int[] iArr2);

    private static native void nPlotStems(String str, int[] iArr, int[] iArr2, double d);

    private static native void nPlotStems(String str, int[] iArr, int[] iArr2, double d, int i);

    private static native void nPlotStems(String str, int[] iArr, int[] iArr2, double d, int i, int i2);

    private static native void nPlotStems(String str, long[] jArr, long[] jArr2);

    private static native void nPlotStems(String str, long[] jArr, long[] jArr2, double d);

    private static native void nPlotStems(String str, long[] jArr, long[] jArr2, double d, int i);

    private static native void nPlotStems(String str, long[] jArr, long[] jArr2, double d, int i, int i2);

    private static native void nPlotStems(String str, float[] fArr, float[] fArr2);

    private static native void nPlotStems(String str, float[] fArr, float[] fArr2, double d);

    private static native void nPlotStems(String str, float[] fArr, float[] fArr2, double d, int i);

    private static native void nPlotStems(String str, float[] fArr, float[] fArr2, double d, int i, int i2);

    private static native void nPlotStems(String str, double[] dArr, double[] dArr2);

    private static native void nPlotStems(String str, double[] dArr, double[] dArr2, double d);

    private static native void nPlotStems(String str, double[] dArr, double[] dArr2, double d, int i);

    private static native void nPlotStems(String str, double[] dArr, double[] dArr2, double d, int i, int i2);

    private static native void nPlotStemsV(String str, short[] sArr, short[] sArr2, int i);

    private static native void nPlotStemsV(String str, short[] sArr, short[] sArr2, int i, double d);

    private static native void nPlotStemsV(String str, short[] sArr, short[] sArr2, int i, double d, int i2);

    private static native void nPlotStemsV(String str, short[] sArr, short[] sArr2, int i, double d, int i2, int i3);

    private static native void nPlotStemsV(String str, int[] iArr, int[] iArr2, int i);

    private static native void nPlotStemsV(String str, int[] iArr, int[] iArr2, int i, double d);

    private static native void nPlotStemsV(String str, int[] iArr, int[] iArr2, int i, double d, int i2);

    private static native void nPlotStemsV(String str, int[] iArr, int[] iArr2, int i, double d, int i2, int i3);

    private static native void nPlotStemsV(String str, long[] jArr, long[] jArr2, int i);

    private static native void nPlotStemsV(String str, long[] jArr, long[] jArr2, int i, double d);

    private static native void nPlotStemsV(String str, long[] jArr, long[] jArr2, int i, double d, int i2);

    private static native void nPlotStemsV(String str, long[] jArr, long[] jArr2, int i, double d, int i2, int i3);

    private static native void nPlotStemsV(String str, float[] fArr, float[] fArr2, int i);

    private static native void nPlotStemsV(String str, float[] fArr, float[] fArr2, int i, double d);

    private static native void nPlotStemsV(String str, float[] fArr, float[] fArr2, int i, double d, int i2);

    private static native void nPlotStemsV(String str, float[] fArr, float[] fArr2, int i, double d, int i2, int i3);

    private static native void nPlotStemsV(String str, double[] dArr, double[] dArr2, int i);

    private static native void nPlotStemsV(String str, double[] dArr, double[] dArr2, int i, double d);

    private static native void nPlotStemsV(String str, double[] dArr, double[] dArr2, int i, double d, int i2);

    private static native void nPlotStemsV(String str, double[] dArr, double[] dArr2, int i, double d, int i2, int i3);

    private static native void nPlotInfLines(String str, short[] sArr);

    private static native void nPlotInfLines(String str, short[] sArr, int i);

    private static native void nPlotInfLines(String str, short[] sArr, int i, int i2);

    private static native void nPlotInfLines(String str, int[] iArr);

    private static native void nPlotInfLines(String str, int[] iArr, int i);

    private static native void nPlotInfLines(String str, int[] iArr, int i, int i2);

    private static native void nPlotInfLines(String str, long[] jArr);

    private static native void nPlotInfLines(String str, long[] jArr, int i);

    private static native void nPlotInfLines(String str, long[] jArr, int i, int i2);

    private static native void nPlotInfLines(String str, float[] fArr);

    private static native void nPlotInfLines(String str, float[] fArr, int i);

    private static native void nPlotInfLines(String str, float[] fArr, int i, int i2);

    private static native void nPlotInfLines(String str, double[] dArr);

    private static native void nPlotInfLines(String str, double[] dArr, int i);

    private static native void nPlotInfLines(String str, double[] dArr, int i, int i2);

    private static native void nPlotInfLinesV(String str, short[] sArr, int i);

    private static native void nPlotInfLinesV(String str, short[] sArr, int i, int i2);

    private static native void nPlotInfLinesV(String str, short[] sArr, int i, int i2, int i3);

    private static native void nPlotInfLinesV(String str, int[] iArr, int i);

    private static native void nPlotInfLinesV(String str, int[] iArr, int i, int i2);

    private static native void nPlotInfLinesV(String str, int[] iArr, int i, int i2, int i3);

    private static native void nPlotInfLinesV(String str, long[] jArr, int i);

    private static native void nPlotInfLinesV(String str, long[] jArr, int i, int i2);

    private static native void nPlotInfLinesV(String str, long[] jArr, int i, int i2, int i3);

    private static native void nPlotInfLinesV(String str, float[] fArr, int i);

    private static native void nPlotInfLinesV(String str, float[] fArr, int i, int i2);

    private static native void nPlotInfLinesV(String str, float[] fArr, int i, int i2, int i3);

    private static native void nPlotInfLinesV(String str, double[] dArr, int i);

    private static native void nPlotInfLinesV(String str, double[] dArr, int i, int i2);

    private static native void nPlotInfLinesV(String str, double[] dArr, int i, int i2, int i3);

    private static native void nPlotPieChart(String[] strArr, int i, short[] sArr, double d, double d2, double d3);

    private static native void nPlotPieChart(String[] strArr, int i, short[] sArr, double d, double d2, double d3, String str);

    private static native void nPlotPieChart(String[] strArr, int i, short[] sArr, double d, double d2, double d3, String str, double d4);

    private static native void nPlotPieChart(String[] strArr, int i, short[] sArr, double d, double d2, double d3, String str, double d4, int i2);

    private static native void nPlotPieChart(String[] strArr, int i, short[] sArr, double d, double d2, double d3, double d4, int i2);

    private static native void nPlotPieChart(String[] strArr, int i, int[] iArr, double d, double d2, double d3);

    private static native void nPlotPieChart(String[] strArr, int i, int[] iArr, double d, double d2, double d3, String str);

    private static native void nPlotPieChart(String[] strArr, int i, int[] iArr, double d, double d2, double d3, String str, double d4);

    private static native void nPlotPieChart(String[] strArr, int i, int[] iArr, double d, double d2, double d3, String str, double d4, int i2);

    private static native void nPlotPieChart(String[] strArr, int i, int[] iArr, double d, double d2, double d3, double d4, int i2);

    private static native void nPlotPieChart(String[] strArr, int i, long[] jArr, double d, double d2, double d3);

    private static native void nPlotPieChart(String[] strArr, int i, long[] jArr, double d, double d2, double d3, String str);

    private static native void nPlotPieChart(String[] strArr, int i, long[] jArr, double d, double d2, double d3, String str, double d4);

    private static native void nPlotPieChart(String[] strArr, int i, long[] jArr, double d, double d2, double d3, String str, double d4, int i2);

    private static native void nPlotPieChart(String[] strArr, int i, long[] jArr, double d, double d2, double d3, double d4, int i2);

    private static native void nPlotPieChart(String[] strArr, int i, float[] fArr, double d, double d2, double d3);

    private static native void nPlotPieChart(String[] strArr, int i, float[] fArr, double d, double d2, double d3, String str);

    private static native void nPlotPieChart(String[] strArr, int i, float[] fArr, double d, double d2, double d3, String str, double d4);

    private static native void nPlotPieChart(String[] strArr, int i, float[] fArr, double d, double d2, double d3, String str, double d4, int i2);

    private static native void nPlotPieChart(String[] strArr, int i, float[] fArr, double d, double d2, double d3, double d4, int i2);

    private static native void nPlotPieChart(String[] strArr, int i, double[] dArr, double d, double d2, double d3);

    private static native void nPlotPieChart(String[] strArr, int i, double[] dArr, double d, double d2, double d3, String str);

    private static native void nPlotPieChart(String[] strArr, int i, double[] dArr, double d, double d2, double d3, String str, double d4);

    private static native void nPlotPieChart(String[] strArr, int i, double[] dArr, double d, double d2, double d3, String str, double d4, int i2);

    private static native void nPlotPieChart(String[] strArr, int i, double[] dArr, double d, double d2, double d3, double d4, int i2);

    private static native void nPlotPieChartV(String[] strArr, int i, short[] sArr, int i2, double d, double d2, double d3);

    private static native void nPlotPieChartV(String[] strArr, int i, short[] sArr, int i2, double d, double d2, double d3, String str);

    private static native void nPlotPieChartV(String[] strArr, int i, short[] sArr, int i2, double d, double d2, double d3, String str, double d4);

    private static native void nPlotPieChartV(String[] strArr, int i, short[] sArr, int i2, double d, double d2, double d3, String str, double d4, int i3);

    private static native void nPlotPieChartV(String[] strArr, int i, short[] sArr, int i2, double d, double d2, double d3, double d4, int i3);

    private static native void nPlotPieChartV(String[] strArr, int i, int[] iArr, int i2, double d, double d2, double d3);

    private static native void nPlotPieChartV(String[] strArr, int i, int[] iArr, int i2, double d, double d2, double d3, String str);

    private static native void nPlotPieChartV(String[] strArr, int i, int[] iArr, int i2, double d, double d2, double d3, String str, double d4);

    private static native void nPlotPieChartV(String[] strArr, int i, int[] iArr, int i2, double d, double d2, double d3, String str, double d4, int i3);

    private static native void nPlotPieChartV(String[] strArr, int i, int[] iArr, int i2, double d, double d2, double d3, double d4, int i3);

    private static native void nPlotPieChartV(String[] strArr, int i, long[] jArr, int i2, double d, double d2, double d3);

    private static native void nPlotPieChartV(String[] strArr, int i, long[] jArr, int i2, double d, double d2, double d3, String str);

    private static native void nPlotPieChartV(String[] strArr, int i, long[] jArr, int i2, double d, double d2, double d3, String str, double d4);

    private static native void nPlotPieChartV(String[] strArr, int i, long[] jArr, int i2, double d, double d2, double d3, String str, double d4, int i3);

    private static native void nPlotPieChartV(String[] strArr, int i, long[] jArr, int i2, double d, double d2, double d3, double d4, int i3);

    private static native void nPlotPieChartV(String[] strArr, int i, float[] fArr, int i2, double d, double d2, double d3);

    private static native void nPlotPieChartV(String[] strArr, int i, float[] fArr, int i2, double d, double d2, double d3, String str);

    private static native void nPlotPieChartV(String[] strArr, int i, float[] fArr, int i2, double d, double d2, double d3, String str, double d4);

    private static native void nPlotPieChartV(String[] strArr, int i, float[] fArr, int i2, double d, double d2, double d3, String str, double d4, int i3);

    private static native void nPlotPieChartV(String[] strArr, int i, float[] fArr, int i2, double d, double d2, double d3, double d4, int i3);

    private static native void nPlotPieChartV(String[] strArr, int i, double[] dArr, int i2, double d, double d2, double d3);

    private static native void nPlotPieChartV(String[] strArr, int i, double[] dArr, int i2, double d, double d2, double d3, String str);

    private static native void nPlotPieChartV(String[] strArr, int i, double[] dArr, int i2, double d, double d2, double d3, String str, double d4);

    private static native void nPlotPieChartV(String[] strArr, int i, double[] dArr, int i2, double d, double d2, double d3, String str, double d4, int i3);

    private static native void nPlotPieChartV(String[] strArr, int i, double[] dArr, int i2, double d, double d2, double d3, double d4, int i3);

    private static native void nPlotHeatmap(String str, short[] sArr, int i, int i2);

    private static native void nPlotHeatmap(String str, short[] sArr, int i, int i2, double d);

    private static native void nPlotHeatmap(String str, short[] sArr, int i, int i2, double d, double d2);

    private static native void nPlotHeatmap(String str, short[] sArr, int i, int i2, double d, double d2, String str2);

    private static native void nPlotHeatmap(String str, short[] sArr, int i, int i2, double d, double d2, String str2, double d3, double d4);

    private static native void nPlotHeatmap(String str, short[] sArr, int i, int i2, double d, double d2, String str2, double d3, double d4, double d5, double d6);

    private static native void nPlotHeatmap(String str, short[] sArr, int i, int i2, double d, double d2, String str2, double d3, double d4, double d5, double d6, int i3);

    private static native void nPlotHeatmap(String str, short[] sArr, int i, int i2, double d, double d2, double d3, double d4, double d5, double d6, int i3);

    private static native void nPlotHeatmap(String str, int[] iArr, int i, int i2);

    private static native void nPlotHeatmap(String str, int[] iArr, int i, int i2, double d);

    private static native void nPlotHeatmap(String str, int[] iArr, int i, int i2, double d, double d2);

    private static native void nPlotHeatmap(String str, int[] iArr, int i, int i2, double d, double d2, String str2);

    private static native void nPlotHeatmap(String str, int[] iArr, int i, int i2, double d, double d2, String str2, double d3, double d4);

    private static native void nPlotHeatmap(String str, int[] iArr, int i, int i2, double d, double d2, String str2, double d3, double d4, double d5, double d6);

    private static native void nPlotHeatmap(String str, int[] iArr, int i, int i2, double d, double d2, String str2, double d3, double d4, double d5, double d6, int i3);

    private static native void nPlotHeatmap(String str, int[] iArr, int i, int i2, double d, double d2, double d3, double d4, double d5, double d6, int i3);

    private static native void nPlotHeatmap(String str, long[] jArr, int i, int i2);

    private static native void nPlotHeatmap(String str, long[] jArr, int i, int i2, double d);

    private static native void nPlotHeatmap(String str, long[] jArr, int i, int i2, double d, double d2);

    private static native void nPlotHeatmap(String str, long[] jArr, int i, int i2, double d, double d2, String str2);

    private static native void nPlotHeatmap(String str, long[] jArr, int i, int i2, double d, double d2, String str2, double d3, double d4);

    private static native void nPlotHeatmap(String str, long[] jArr, int i, int i2, double d, double d2, String str2, double d3, double d4, double d5, double d6);

    private static native void nPlotHeatmap(String str, long[] jArr, int i, int i2, double d, double d2, String str2, double d3, double d4, double d5, double d6, int i3);

    private static native void nPlotHeatmap(String str, long[] jArr, int i, int i2, double d, double d2, double d3, double d4, double d5, double d6, int i3);

    private static native void nPlotHeatmap(String str, float[] fArr, int i, int i2);

    private static native void nPlotHeatmap(String str, float[] fArr, int i, int i2, double d);

    private static native void nPlotHeatmap(String str, float[] fArr, int i, int i2, double d, double d2);

    private static native void nPlotHeatmap(String str, float[] fArr, int i, int i2, double d, double d2, String str2);

    private static native void nPlotHeatmap(String str, float[] fArr, int i, int i2, double d, double d2, String str2, double d3, double d4);

    private static native void nPlotHeatmap(String str, float[] fArr, int i, int i2, double d, double d2, String str2, double d3, double d4, double d5, double d6);

    private static native void nPlotHeatmap(String str, float[] fArr, int i, int i2, double d, double d2, String str2, double d3, double d4, double d5, double d6, int i3);

    private static native void nPlotHeatmap(String str, float[] fArr, int i, int i2, double d, double d2, double d3, double d4, double d5, double d6, int i3);

    private static native void nPlotHeatmap(String str, double[] dArr, int i, int i2);

    private static native void nPlotHeatmap(String str, double[] dArr, int i, int i2, double d);

    private static native void nPlotHeatmap(String str, double[] dArr, int i, int i2, double d, double d2);

    private static native void nPlotHeatmap(String str, double[] dArr, int i, int i2, double d, double d2, String str2);

    private static native void nPlotHeatmap(String str, double[] dArr, int i, int i2, double d, double d2, String str2, double d3, double d4);

    private static native void nPlotHeatmap(String str, double[] dArr, int i, int i2, double d, double d2, String str2, double d3, double d4, double d5, double d6);

    private static native void nPlotHeatmap(String str, double[] dArr, int i, int i2, double d, double d2, String str2, double d3, double d4, double d5, double d6, int i3);

    private static native void nPlotHeatmap(String str, double[] dArr, int i, int i2, double d, double d2, double d3, double d4, double d5, double d6, int i3);

    private static native double nPlotHistogram(String str, short[] sArr);

    private static native double nPlotHistogram(String str, short[] sArr, int i);

    private static native double nPlotHistogram(String str, short[] sArr, int i, double d);

    private static native double nPlotHistogram(String str, short[] sArr, int i, double d, double d2, double d3);

    private static native double nPlotHistogram(String str, short[] sArr, int i, double d, double d2, double d3, int i2);

    private static native double nPlotHistogram(String str, short[] sArr, double d, double d2, double d3, int i);

    private static native double nPlotHistogram(String str, short[] sArr, int i, double d, int i2);

    private static native double nPlotHistogram(String str, int[] iArr);

    private static native double nPlotHistogram(String str, int[] iArr, int i);

    private static native double nPlotHistogram(String str, int[] iArr, int i, double d);

    private static native double nPlotHistogram(String str, int[] iArr, int i, double d, double d2, double d3);

    private static native double nPlotHistogram(String str, int[] iArr, int i, double d, double d2, double d3, int i2);

    private static native double nPlotHistogram(String str, int[] iArr, double d, double d2, double d3, int i);

    private static native double nPlotHistogram(String str, int[] iArr, int i, double d, int i2);

    private static native double nPlotHistogram(String str, long[] jArr);

    private static native double nPlotHistogram(String str, long[] jArr, int i);

    private static native double nPlotHistogram(String str, long[] jArr, int i, double d);

    private static native double nPlotHistogram(String str, long[] jArr, int i, double d, double d2, double d3);

    private static native double nPlotHistogram(String str, long[] jArr, int i, double d, double d2, double d3, int i2);

    private static native double nPlotHistogram(String str, long[] jArr, double d, double d2, double d3, int i);

    private static native double nPlotHistogram(String str, long[] jArr, int i, double d, int i2);

    private static native double nPlotHistogram(String str, float[] fArr);

    private static native double nPlotHistogram(String str, float[] fArr, int i);

    private static native double nPlotHistogram(String str, float[] fArr, int i, double d);

    private static native double nPlotHistogram(String str, float[] fArr, int i, double d, double d2, double d3);

    private static native double nPlotHistogram(String str, float[] fArr, int i, double d, double d2, double d3, int i2);

    private static native double nPlotHistogram(String str, float[] fArr, double d, double d2, double d3, int i);

    private static native double nPlotHistogram(String str, float[] fArr, int i, double d, int i2);

    private static native double nPlotHistogram(String str, double[] dArr);

    private static native double nPlotHistogram(String str, double[] dArr, int i);

    private static native double nPlotHistogram(String str, double[] dArr, int i, double d);

    private static native double nPlotHistogram(String str, double[] dArr, int i, double d, double d2, double d3);

    private static native double nPlotHistogram(String str, double[] dArr, int i, double d, double d2, double d3, int i2);

    private static native double nPlotHistogram(String str, double[] dArr, double d, double d2, double d3, int i);

    private static native double nPlotHistogram(String str, double[] dArr, int i, double d, int i2);

    private static native double nPlotHistogramV(String str, short[] sArr, int i);

    private static native double nPlotHistogramV(String str, short[] sArr, int i, int i2);

    private static native double nPlotHistogramV(String str, short[] sArr, int i, int i2, double d);

    private static native double nPlotHistogramV(String str, short[] sArr, int i, int i2, double d, double d2, double d3);

    private static native double nPlotHistogramV(String str, short[] sArr, int i, int i2, double d, double d2, double d3, int i3);

    private static native double nPlotHistogramV(String str, short[] sArr, int i, int i2, double d, int i3);

    private static native double nPlotHistogramV(String str, int[] iArr, int i);

    private static native double nPlotHistogramV(String str, int[] iArr, int i, int i2);

    private static native double nPlotHistogramV(String str, int[] iArr, int i, int i2, double d);

    private static native double nPlotHistogramV(String str, int[] iArr, int i, int i2, double d, double d2, double d3);

    private static native double nPlotHistogramV(String str, int[] iArr, int i, int i2, double d, double d2, double d3, int i3);

    private static native double nPlotHistogramV(String str, int[] iArr, int i, int i2, double d, int i3);

    private static native double nPlotHistogramV(String str, long[] jArr, int i);

    private static native double nPlotHistogramV(String str, long[] jArr, int i, int i2);

    private static native double nPlotHistogramV(String str, long[] jArr, int i, int i2, double d);

    private static native double nPlotHistogramV(String str, long[] jArr, int i, int i2, double d, double d2, double d3);

    private static native double nPlotHistogramV(String str, long[] jArr, int i, int i2, double d, double d2, double d3, int i3);

    private static native double nPlotHistogramV(String str, long[] jArr, int i, int i2, double d, int i3);

    private static native double nPlotHistogramV(String str, float[] fArr, int i);

    private static native double nPlotHistogramV(String str, float[] fArr, int i, int i2);

    private static native double nPlotHistogramV(String str, float[] fArr, int i, int i2, double d);

    private static native double nPlotHistogramV(String str, float[] fArr, int i, int i2, double d, double d2, double d3);

    private static native double nPlotHistogramV(String str, float[] fArr, int i, int i2, double d, double d2, double d3, int i3);

    private static native double nPlotHistogramV(String str, float[] fArr, int i, int i2, double d, int i3);

    private static native double nPlotHistogramV(String str, double[] dArr, int i);

    private static native double nPlotHistogramV(String str, double[] dArr, int i, int i2);

    private static native double nPlotHistogramV(String str, double[] dArr, int i, int i2, double d);

    private static native double nPlotHistogramV(String str, double[] dArr, int i, int i2, double d, double d2, double d3);

    private static native double nPlotHistogramV(String str, double[] dArr, int i, int i2, double d, double d2, double d3, int i3);

    private static native double nPlotHistogramV(String str, double[] dArr, int i, int i2, double d, int i3);

    private static native double nPlotHistogram2D(String str, short[] sArr, short[] sArr2);

    private static native double nPlotHistogram2D(String str, short[] sArr, short[] sArr2, int i);

    private static native double nPlotHistogram2D(String str, short[] sArr, short[] sArr2, int i, int i2);

    private static native double nPlotHistogram2D(String str, short[] sArr, short[] sArr2, int i, int i2, double d, double d2, double d3, double d4);

    private static native double nPlotHistogram2D(String str, short[] sArr, short[] sArr2, int i, int i2, double d, double d2, double d3, double d4, int i3);

    private static native double nPlotHistogram2D(String str, short[] sArr, short[] sArr2, int i, int i2, int i3);

    private static native double nPlotHistogram2D(String str, int[] iArr, int[] iArr2);

    private static native double nPlotHistogram2D(String str, int[] iArr, int[] iArr2, int i);

    private static native double nPlotHistogram2D(String str, int[] iArr, int[] iArr2, int i, int i2);

    private static native double nPlotHistogram2D(String str, int[] iArr, int[] iArr2, int i, int i2, double d, double d2, double d3, double d4);

    private static native double nPlotHistogram2D(String str, int[] iArr, int[] iArr2, int i, int i2, double d, double d2, double d3, double d4, int i3);

    private static native double nPlotHistogram2D(String str, int[] iArr, int[] iArr2, int i, int i2, int i3);

    private static native double nPlotHistogram2D(String str, long[] jArr, long[] jArr2);

    private static native double nPlotHistogram2D(String str, long[] jArr, long[] jArr2, int i);

    private static native double nPlotHistogram2D(String str, long[] jArr, long[] jArr2, int i, int i2);

    private static native double nPlotHistogram2D(String str, long[] jArr, long[] jArr2, int i, int i2, double d, double d2, double d3, double d4);

    private static native double nPlotHistogram2D(String str, long[] jArr, long[] jArr2, int i, int i2, double d, double d2, double d3, double d4, int i3);

    private static native double nPlotHistogram2D(String str, long[] jArr, long[] jArr2, int i, int i2, int i3);

    private static native double nPlotHistogram2D(String str, float[] fArr, float[] fArr2);

    private static native double nPlotHistogram2D(String str, float[] fArr, float[] fArr2, int i);

    private static native double nPlotHistogram2D(String str, float[] fArr, float[] fArr2, int i, int i2);

    private static native double nPlotHistogram2D(String str, float[] fArr, float[] fArr2, int i, int i2, double d, double d2, double d3, double d4);

    private static native double nPlotHistogram2D(String str, float[] fArr, float[] fArr2, int i, int i2, double d, double d2, double d3, double d4, int i3);

    private static native double nPlotHistogram2D(String str, float[] fArr, float[] fArr2, int i, int i2, int i3);

    private static native double nPlotHistogram2D(String str, double[] dArr, double[] dArr2);

    private static native double nPlotHistogram2D(String str, double[] dArr, double[] dArr2, int i);

    private static native double nPlotHistogram2D(String str, double[] dArr, double[] dArr2, int i, int i2);

    private static native double nPlotHistogram2D(String str, double[] dArr, double[] dArr2, int i, int i2, double d, double d2, double d3, double d4);

    private static native double nPlotHistogram2D(String str, double[] dArr, double[] dArr2, int i, int i2, double d, double d2, double d3, double d4, int i3);

    private static native double nPlotHistogram2D(String str, double[] dArr, double[] dArr2, int i, int i2, int i3);

    private static native double nPlotHistogram2DV(String str, short[] sArr, short[] sArr2, int i);

    private static native double nPlotHistogram2DV(String str, short[] sArr, short[] sArr2, int i, int i2);

    private static native double nPlotHistogram2DV(String str, short[] sArr, short[] sArr2, int i, int i2, int i3);

    private static native double nPlotHistogram2DV(String str, short[] sArr, short[] sArr2, int i, int i2, int i3, double d, double d2, double d3, double d4);

    private static native double nPlotHistogram2DV(String str, short[] sArr, short[] sArr2, int i, int i2, int i3, double d, double d2, double d3, double d4, int i4);

    private static native double nPlotHistogram2DV(String str, short[] sArr, short[] sArr2, int i, int i2, int i3, int i4);

    private static native double nPlotHistogram2DV(String str, int[] iArr, int[] iArr2, int i);

    private static native double nPlotHistogram2DV(String str, int[] iArr, int[] iArr2, int i, int i2);

    private static native double nPlotHistogram2DV(String str, int[] iArr, int[] iArr2, int i, int i2, int i3);

    private static native double nPlotHistogram2DV(String str, int[] iArr, int[] iArr2, int i, int i2, int i3, double d, double d2, double d3, double d4);

    private static native double nPlotHistogram2DV(String str, int[] iArr, int[] iArr2, int i, int i2, int i3, double d, double d2, double d3, double d4, int i4);

    private static native double nPlotHistogram2DV(String str, int[] iArr, int[] iArr2, int i, int i2, int i3, int i4);

    private static native double nPlotHistogram2DV(String str, long[] jArr, long[] jArr2, int i);

    private static native double nPlotHistogram2DV(String str, long[] jArr, long[] jArr2, int i, int i2);

    private static native double nPlotHistogram2DV(String str, long[] jArr, long[] jArr2, int i, int i2, int i3);

    private static native double nPlotHistogram2DV(String str, long[] jArr, long[] jArr2, int i, int i2, int i3, double d, double d2, double d3, double d4);

    private static native double nPlotHistogram2DV(String str, long[] jArr, long[] jArr2, int i, int i2, int i3, double d, double d2, double d3, double d4, int i4);

    private static native double nPlotHistogram2DV(String str, long[] jArr, long[] jArr2, int i, int i2, int i3, int i4);

    private static native double nPlotHistogram2DV(String str, float[] fArr, float[] fArr2, int i);

    private static native double nPlotHistogram2DV(String str, float[] fArr, float[] fArr2, int i, int i2);

    private static native double nPlotHistogram2DV(String str, float[] fArr, float[] fArr2, int i, int i2, int i3);

    private static native double nPlotHistogram2DV(String str, float[] fArr, float[] fArr2, int i, int i2, int i3, double d, double d2, double d3, double d4);

    private static native double nPlotHistogram2DV(String str, float[] fArr, float[] fArr2, int i, int i2, int i3, double d, double d2, double d3, double d4, int i4);

    private static native double nPlotHistogram2DV(String str, float[] fArr, float[] fArr2, int i, int i2, int i3, int i4);

    private static native double nPlotHistogram2DV(String str, double[] dArr, double[] dArr2, int i);

    private static native double nPlotHistogram2DV(String str, double[] dArr, double[] dArr2, int i, int i2);

    private static native double nPlotHistogram2DV(String str, double[] dArr, double[] dArr2, int i, int i2, int i3);

    private static native double nPlotHistogram2DV(String str, double[] dArr, double[] dArr2, int i, int i2, int i3, double d, double d2, double d3, double d4);

    private static native double nPlotHistogram2DV(String str, double[] dArr, double[] dArr2, int i, int i2, int i3, double d, double d2, double d3, double d4, int i4);

    private static native double nPlotHistogram2DV(String str, double[] dArr, double[] dArr2, int i, int i2, int i3, int i4);

    private static native void nPlotDigital(String str, short[] sArr, short[] sArr2);

    private static native void nPlotDigital(String str, short[] sArr, short[] sArr2, int i);

    private static native void nPlotDigital(String str, short[] sArr, short[] sArr2, int i, int i2);

    private static native void nPlotDigital(String str, int[] iArr, int[] iArr2);

    private static native void nPlotDigital(String str, int[] iArr, int[] iArr2, int i);

    private static native void nPlotDigital(String str, int[] iArr, int[] iArr2, int i, int i2);

    private static native void nPlotDigital(String str, long[] jArr, long[] jArr2);

    private static native void nPlotDigital(String str, long[] jArr, long[] jArr2, int i);

    private static native void nPlotDigital(String str, long[] jArr, long[] jArr2, int i, int i2);

    private static native void nPlotDigital(String str, float[] fArr, float[] fArr2);

    private static native void nPlotDigital(String str, float[] fArr, float[] fArr2, int i);

    private static native void nPlotDigital(String str, float[] fArr, float[] fArr2, int i, int i2);

    private static native void nPlotDigital(String str, double[] dArr, double[] dArr2);

    private static native void nPlotDigital(String str, double[] dArr, double[] dArr2, int i);

    private static native void nPlotDigital(String str, double[] dArr, double[] dArr2, int i, int i2);

    private static native void nPlotDigitalV(String str, short[] sArr, short[] sArr2, int i);

    private static native void nPlotDigitalV(String str, short[] sArr, short[] sArr2, int i, int i2);

    private static native void nPlotDigitalV(String str, short[] sArr, short[] sArr2, int i, int i2, int i3);

    private static native void nPlotDigitalV(String str, int[] iArr, int[] iArr2, int i);

    private static native void nPlotDigitalV(String str, int[] iArr, int[] iArr2, int i, int i2);

    private static native void nPlotDigitalV(String str, int[] iArr, int[] iArr2, int i, int i2, int i3);

    private static native void nPlotDigitalV(String str, long[] jArr, long[] jArr2, int i);

    private static native void nPlotDigitalV(String str, long[] jArr, long[] jArr2, int i, int i2);

    private static native void nPlotDigitalV(String str, long[] jArr, long[] jArr2, int i, int i2, int i3);

    private static native void nPlotDigitalV(String str, float[] fArr, float[] fArr2, int i);

    private static native void nPlotDigitalV(String str, float[] fArr, float[] fArr2, int i, int i2);

    private static native void nPlotDigitalV(String str, float[] fArr, float[] fArr2, int i, int i2, int i3);

    private static native void nPlotDigitalV(String str, double[] dArr, double[] dArr2, int i);

    private static native void nPlotDigitalV(String str, double[] dArr, double[] dArr2, int i, int i2);

    private static native void nPlotDigitalV(String str, double[] dArr, double[] dArr2, int i, int i2, int i3);

    private static native void nPlotImage(String str, long j, double d, double d2, double d3, double d4);

    private static native void nPlotImage(String str, long j, double d, double d2, double d3, double d4, float f, float f2);

    private static native void nPlotImage(String str, long j, double d, double d2, double d3, double d4, float f, float f2, float f3, float f4);

    private static native void nPlotImage(String str, long j, double d, double d2, double d3, double d4, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8);

    private static native void nPlotImage(String str, long j, double d, double d2, double d3, double d4, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, int i);

    private static native void nPlotText(String str, double d, double d2);

    private static native void nPlotText(String str, double d, double d2, float f, float f2);

    private static native void nPlotText(String str, double d, double d2, float f, float f2, int i);

    private static native void nPlotText(String str, double d, double d2, int i);

    private static native void nPlotDummy(String str);

    private static native void nPlotDummy(String str, int i);

    private static native boolean nDragPoint(int i, double[] dArr, double[] dArr2, float f, float f2, float f3, float f4);

    private static native boolean nDragPoint(int i, double[] dArr, double[] dArr2, float f, float f2, float f3, float f4, float f5);

    private static native boolean nDragPoint(int i, double[] dArr, double[] dArr2, float f, float f2, float f3, float f4, float f5, int i2);

    private static native boolean nDragPoint(int i, double[] dArr, double[] dArr2, float f, float f2, float f3, float f4, int i2);

    private static native boolean nDragLineX(int i, double[] dArr, float f, float f2, float f3, float f4);

    private static native boolean nDragLineX(int i, double[] dArr, float f, float f2, float f3, float f4, float f5);

    private static native boolean nDragLineX(int i, double[] dArr, float f, float f2, float f3, float f4, float f5, int i2);

    private static native boolean nDragLineX(int i, double[] dArr, float f, float f2, float f3, float f4, int i2);

    private static native boolean nDragLineY(int i, double[] dArr, float f, float f2, float f3, float f4);

    private static native boolean nDragLineY(int i, double[] dArr, float f, float f2, float f3, float f4, float f5);

    private static native boolean nDragLineY(int i, double[] dArr, float f, float f2, float f3, float f4, float f5, int i2);

    private static native boolean nDragLineY(int i, double[] dArr, float f, float f2, float f3, float f4, int i2);

    private static native boolean nDragRect(int i, double[] dArr, double[] dArr2, double[] dArr3, double[] dArr4, float f, float f2, float f3, float f4);

    private static native boolean nDragRect(int i, double[] dArr, double[] dArr2, double[] dArr3, double[] dArr4, float f, float f2, float f3, float f4, int i2);

    private static native void nAnnotation(double d, double d2, float f, float f2, float f3, float f4, float f5, float f6, boolean z);

    private static native void nAnnotation(double d, double d2, float f, float f2, float f3, float f4, float f5, float f6, boolean z, boolean z2);

    private static native void nAnnotation(double d, double d2, float f, float f2, float f3, float f4, float f5, float f6, boolean z, String str);

    private static native void nTagX(double d, float f, float f2, float f3, float f4);

    private static native void nTagX(double d, float f, float f2, float f3, float f4, boolean z);

    private static native void nTagX(double d, float f, float f2, float f3, float f4, String str);

    private static native void nTagY(double d, float f, float f2, float f3, float f4);

    private static native void nTagY(double d, float f, float f2, float f3, float f4, boolean z);

    private static native void nTagY(double d, float f, float f2, float f3, float f4, String str);

    private static native void nSetAxis(int i);

    private static native void nSetAxes(int i, int i2);

    private static native void nPixelsToPlot(ImPlotPoint imPlotPoint, float f, float f2);

    private static native void nPixelsToPlot(ImPlotPoint imPlotPoint, float f, float f2, int i);

    private static native void nPixelsToPlot(ImPlotPoint imPlotPoint, float f, float f2, int i, int i2);

    private static native void nPlotToPixels(ImVec2 imVec2, double d, double d2);

    private static native float nPlotToPixelsX(double d, double d2);

    private static native float nPlotToPixelsY(double d, double d2);

    private static native void nPlotToPixels(ImVec2 imVec2, double d, double d2, int i);

    private static native float nPlotToPixelsX(double d, double d2, int i);

    private static native float nPlotToPixelsY(double d, double d2, int i);

    private static native void nPlotToPixels(ImVec2 imVec2, double d, double d2, int i, int i2);

    private static native float nPlotToPixelsX(double d, double d2, int i, int i2);

    private static native float nPlotToPixelsY(double d, double d2, int i, int i2);

    private static native void nGetPlotPos(ImVec2 imVec2);

    private static native float nGetPlotPosX();

    private static native float nGetPlotPosY();

    private static native void nGetPlotSize(ImVec2 imVec2);

    private static native float nGetPlotSizeX();

    private static native float nGetPlotSizeY();

    private static native void nGetPlotMousePos(ImPlotPoint imPlotPoint);

    private static native void nGetPlotMousePos(ImPlotPoint imPlotPoint, int i);

    private static native void nGetPlotMousePos(ImPlotPoint imPlotPoint, int i, int i2);

    private static native void nGetPlotLimits(ImPlotRect imPlotRect);

    private static native void nGetPlotLimits(ImPlotRect imPlotRect, int i);

    private static native void nGetPlotLimits(ImPlotRect imPlotRect, int i, int i2);

    private static native boolean nIsPlotHovered();

    private static native boolean nIsAxisHovered(int i);

    private static native boolean nIsSubplotsHovered();

    private static native boolean nIsPlotSelected();

    private static native void nGetPlotSelection(ImPlotRect imPlotRect);

    private static native void nGetPlotSelection(ImPlotRect imPlotRect, int i);

    private static native void nGetPlotSelection(ImPlotRect imPlotRect, int i, int i2);

    private static native void nCancelPlotSelection();

    private static native void nHideNextItem();

    private static native void nHideNextItem(boolean z);

    private static native void nHideNextItem(boolean z, int i);

    private static native void nHideNextItem(int i);

    private static native boolean nBeginAlignedPlots(String str);

    private static native boolean nBeginAlignedPlots(String str, boolean z);

    private static native void nEndAlignedPlots();

    private static native boolean nBeginLegendPopup(String str);

    private static native boolean nBeginLegendPopup(String str, int i);

    private static native void nEndLegendPopup();

    private static native boolean nIsLegendEntryHovered(String str);

    private static native boolean nBeginDragDropTargetPlot();

    private static native boolean nBeginDragDropTargetAxis(int i);

    private static native boolean nBeginDragDropTargetLegend();

    private static native void nEndDragDropTarget();

    private static native boolean nBeginDragDropSourcePlot();

    private static native boolean nBeginDragDropSourcePlot(int i);

    private static native boolean nBeginDragDropSourceAxis(int i);

    private static native boolean nBeginDragDropSourceAxis(int i, int i2);

    private static native boolean nBeginDragDropSourceItem(String str);

    private static native boolean nBeginDragDropSourceItem(String str, int i);

    private static native void nEndDragDropSource();

    private static native long nGetStyle();

    private static native void nStyleColorsAuto();

    private static native void nStyleColorsAuto(long j);

    private static native void nStyleColorsClassic();

    private static native void nStyleColorsClassic(long j);

    private static native void nStyleColorsDark();

    private static native void nStyleColorsDark(long j);

    private static native void nStyleColorsLight();

    private static native void nStyleColorsLight(long j);

    public static native void pushStyleColor(int i, long j);

    private static native void nPushStyleColor(int i, int i2);

    private static native void nPushStyleColor(int i, float f, float f2, float f3, float f4);

    private static native void nPopStyleColor();

    private static native void nPopStyleColor(int i);

    private static native void nPushStyleVar(int i, float f);

    private static native void nPushStyleVar(int i, int i2);

    private static native void nPushStyleVar(int i, float f, float f2);

    private static native void nPopStyleVar();

    private static native void nPopStyleVar(int i);

    private static native void nSetNextLineStyle();

    private static native void nSetNextLineStyle(float f, float f2, float f3, float f4);

    private static native void nSetNextLineStyle(float f, float f2, float f3, float f4, float f5);

    private static native void nSetNextLineStyle(float f);

    private static native void nSetNextFillStyle();

    private static native void nSetNextFillStyle(float f, float f2, float f3, float f4);

    private static native void nSetNextFillStyle(float f, float f2, float f3, float f4, float f5);

    private static native void nSetNextFillStyle(float f);

    private static native void nSetNextMarkerStyle();

    private static native void nSetNextMarkerStyle(int i);

    private static native void nSetNextMarkerStyle(int i, float f);

    private static native void nSetNextMarkerStyle(int i, float f, float f2, float f3, float f4, float f5);

    private static native void nSetNextMarkerStyle(int i, float f, float f2, float f3, float f4, float f5, float f6);

    private static native void nSetNextMarkerStyle(int i, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10);

    private static native void nSetNextMarkerStyle(int i, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9);

    private static native void nSetNextErrorBarStyle();

    private static native void nSetNextErrorBarStyle(float f, float f2, float f3, float f4);

    private static native void nSetNextErrorBarStyle(float f, float f2, float f3, float f4, float f5);

    private static native void nSetNextErrorBarStyle(float f, float f2, float f3, float f4, float f5, float f6);

    private static native void nSetNextErrorBarStyle(float f, float f2);

    private static native void nGetLastItemColor(ImVec4 imVec4);

    private static native float nGetLastItemColorX();

    private static native float nGetLastItemColorY();

    private static native float nGetLastItemColorZ();

    private static native float nGetLastItemColorW();

    private static native String nGetStyleColorName(int i);

    private static native String nGetMarkerName(int i);

    private static native int nAddColormap(String str, ImVec4[] imVec4Arr);

    private static native int nAddColormap(String str, ImVec4[] imVec4Arr, boolean z);

    private static native int nAddColormap(String str, int[] iArr);

    private static native int nAddColormap(String str, int[] iArr, boolean z);

    private static native int nGetColormapCount();

    private static native String nGetColormapName(int i);

    private static native int nGetColormapIndex(String str);

    private static native void nPushColormap(int i);

    private static native void nPushColormap(String str);

    private static native void nPopColormap();

    private static native void nPopColormap(int i);

    private static native void nNextColormapColor(ImVec4 imVec4);

    private static native float nNextColormapColorX();

    private static native float nNextColormapColorY();

    private static native float nNextColormapColorZ();

    private static native float nNextColormapColorW();

    private static native int nGetColormapSize();

    private static native int nGetColormapSize(int i);

    private static native void nGetColormapColor(ImVec4 imVec4, int i);

    private static native float nGetColormapColorX(int i);

    private static native float nGetColormapColorY(int i);

    private static native float nGetColormapColorZ(int i);

    private static native float nGetColormapColorW(int i);

    private static native void nGetColormapColor(ImVec4 imVec4, int i, int i2);

    private static native float nGetColormapColorX(int i, int i2);

    private static native float nGetColormapColorY(int i, int i2);

    private static native float nGetColormapColorZ(int i, int i2);

    private static native float nGetColormapColorW(int i, int i2);

    private static native void nSampleColormap(ImVec4 imVec4, float f);

    private static native float nSampleColormapX(float f);

    private static native float nSampleColormapY(float f);

    private static native float nSampleColormapZ(float f);

    private static native float nSampleColormapW(float f);

    private static native void nSampleColormap(ImVec4 imVec4, float f, int i);

    private static native float nSampleColormapX(float f, int i);

    private static native float nSampleColormapY(float f, int i);

    private static native float nSampleColormapZ(float f, int i);

    private static native float nSampleColormapW(float f, int i);

    private static native void nColormapScale(String str, double d, double d2);

    private static native void nColormapScale(String str, double d, double d2, float f, float f2);

    private static native void nColormapScale(String str, double d, double d2, float f, float f2, String str2);

    private static native void nColormapScale(String str, double d, double d2, float f, float f2, String str2, int i);

    private static native void nColormapScale(String str, double d, double d2, float f, float f2, String str2, int i, int i2);

    private static native void nColormapScale(String str, double d, double d2, String str2, int i, int i2);

    private static native void nColormapScale(String str, double d, double d2, int i, int i2);

    private static native void nColormapScale(String str, double d, double d2, float f, float f2, int i, int i2);

    private static native boolean nColormapSlider(String str, float[] fArr);

    private static native boolean nColormapSlider(String str, float[] fArr, String str2);

    private static native boolean nColormapSlider(String str, float[] fArr, String str2, int i);

    private static native boolean nColormapSlider(String str, float[] fArr, int i);

    private static native boolean nColormapButton(String str);

    private static native boolean nColormapButton(String str, float f, float f2);

    private static native boolean nColormapButton(String str, float f, float f2, int i);

    private static native boolean nColormapButton(String str, int i);

    private static native void nBustColorCache();

    private static native void nBustColorCache(String str);

    private static native long nGetInputMap();

    private static native void nMapInputDefault();

    private static native void nMapInputDefault(long j);

    private static native void nMapInputReverse();

    private static native void nMapInputReverse(long j);

    private static native void nItemIcon(float f, float f2, float f3, float f4);

    private static native void nItemIcon(int i);

    private static native void nColormapIcon(int i);

    private static native long nGetPlotDrawList();

    private static native void nPushPlotClipRect();

    private static native void nPushPlotClipRect(float f);

    private static native void nPopPlotClipRect();

    private static native boolean nShowStyleSelector(String str);

    private static native boolean nShowColormapSelector(String str);

    private static native boolean nShowInputMapSelector(String str);

    private static native void nShowStyleEditor();

    private static native void nShowStyleEditor(long j);

    private static native void nShowUserGuide();

    private static native void nShowMetricsWindow();

    private static native void nShowMetricsWindow(boolean[] zArr);

    private static native void nShowDemoWindow();

    private static native void nShowDemoWindow(boolean[] zArr);

    private ImPlot() {
    }

    public static ImPlotContext createContext() {
        return new ImPlotContext(nCreateContext());
    }

    public static void destroyContext() {
        nDestroyContext();
    }

    public static void destroyContext(ImPlotContext ctx) {
        nDestroyContext(ctx.ptr);
    }

    public static ImPlotContext getCurrentContext() {
        return new ImPlotContext(nGetCurrentContext());
    }

    public static void setCurrentContext(ImPlotContext ctx) {
        nSetCurrentContext(ctx.ptr);
    }

    public static void setImGuiContext(ImGuiContext ctx) {
        nSetImGuiContext(ctx.ptr);
    }

    public static boolean beginPlot(String titleId) {
        return nBeginPlot(titleId);
    }

    public static boolean beginPlot(String titleId, ImVec2 size) {
        return nBeginPlot(titleId, size.x, size.y);
    }

    public static boolean beginPlot(String titleId, float sizeX, float sizeY) {
        return nBeginPlot(titleId, sizeX, sizeY);
    }

    public static boolean beginPlot(String titleId, ImVec2 size, int flags) {
        return nBeginPlot(titleId, size.x, size.y, flags);
    }

    public static boolean beginPlot(String titleId, float sizeX, float sizeY, int flags) {
        return nBeginPlot(titleId, sizeX, sizeY, flags);
    }

    public static boolean beginPlot(String titleId, int flags) {
        return nBeginPlot(titleId, flags);
    }

    public static void endPlot() {
        nEndPlot();
    }

    public static boolean beginSubplots(String titleID, int rows, int cols, ImVec2 size) {
        return nBeginSubplots(titleID, rows, cols, size.x, size.y);
    }

    public static boolean beginSubplots(String titleID, int rows, int cols, float sizeX, float sizeY) {
        return nBeginSubplots(titleID, rows, cols, sizeX, sizeY);
    }

    public static boolean beginSubplots(String titleID, int rows, int cols, ImVec2 size, int flags) {
        return nBeginSubplots(titleID, rows, cols, size.x, size.y, flags);
    }

    public static boolean beginSubplots(String titleID, int rows, int cols, float sizeX, float sizeY, int flags) {
        return nBeginSubplots(titleID, rows, cols, sizeX, sizeY, flags);
    }

    public static boolean beginSubplots(String titleID, int rows, int cols, ImVec2 size, int flags, float[] rowRatios) {
        return nBeginSubplots(titleID, rows, cols, size.x, size.y, flags, rowRatios);
    }

    public static boolean beginSubplots(String titleID, int rows, int cols, float sizeX, float sizeY, int flags, float[] rowRatios) {
        return nBeginSubplots(titleID, rows, cols, sizeX, sizeY, flags, rowRatios);
    }

    public static boolean beginSubplots(String titleID, int rows, int cols, ImVec2 size, int flags, float[] rowRatios, float[] colRatios) {
        return nBeginSubplots(titleID, rows, cols, size.x, size.y, flags, rowRatios, colRatios);
    }

    public static boolean beginSubplots(String titleID, int rows, int cols, float sizeX, float sizeY, int flags, float[] rowRatios, float[] colRatios) {
        return nBeginSubplots(titleID, rows, cols, sizeX, sizeY, flags, rowRatios, colRatios);
    }

    public static boolean beginSubplots(String titleID, int rows, int cols, ImVec2 size, float[] rowRatios, float[] colRatios) {
        return nBeginSubplots(titleID, rows, cols, size.x, size.y, rowRatios, colRatios);
    }

    public static boolean beginSubplots(String titleID, int rows, int cols, float sizeX, float sizeY, float[] rowRatios, float[] colRatios) {
        return nBeginSubplots(titleID, rows, cols, sizeX, sizeY, rowRatios, colRatios);
    }

    public static void endSubplots() {
        nEndSubplots();
    }

    public static void setupAxis(int axis) {
        nSetupAxis(axis);
    }

    public static void setupAxis(int axis, String label) {
        nSetupAxis(axis, label);
    }

    public static void setupAxis(int axis, String label, int flags) {
        nSetupAxis(axis, label, flags);
    }

    public static void setupAxis(int axis, int flags) {
        nSetupAxis(axis, flags);
    }

    public static void setupAxisLimits(int axis, double vMin, double vMax) {
        nSetupAxisLimits(axis, vMin, vMax);
    }

    public static void setupAxisLimits(int axis, double vMin, double vMax, int cond) {
        nSetupAxisLimits(axis, vMin, vMax, cond);
    }

    public static void setupAxisLinks(int axis, ImDouble linkMin, ImDouble linkMax) {
        nSetupAxisLinks(axis, linkMin != null ? linkMin.getData() : null, linkMax != null ? linkMax.getData() : null);
    }

    public static void setupAxisFormat(int axis, String fmt) {
        nSetupAxisFormat(axis, fmt);
    }

    public static void setupAxisTicks(int axis, double[] values, int nTicks) {
        nSetupAxisTicks(axis, values, nTicks);
    }

    public static void setupAxisTicks(int axis, double[] values, int nTicks, String[] labels) {
        nSetupAxisTicks(axis, values, nTicks, labels, labels.length);
    }

    public static void setupAxisTicks(int axis, double[] values, int nTicks, String[] labels, boolean keepDefault) {
        nSetupAxisTicks(axis, values, nTicks, labels, labels.length, keepDefault);
    }

    public static void setupAxisTicks(int axis, double[] values, int nTicks, boolean keepDefault) {
        nSetupAxisTicks(axis, values, nTicks, keepDefault);
    }

    public static void setupAxisTicks(int axis, double vMin, double vMax, int nTicks) {
        nSetupAxisTicks(axis, vMin, vMax, nTicks);
    }

    public static void setupAxisTicks(int axis, double vMin, double vMax, int nTicks, String[] labels) {
        nSetupAxisTicks(axis, vMin, vMax, nTicks, labels, labels.length);
    }

    public static void setupAxisTicks(int axis, double vMin, double vMax, int nTicks, String[] labels, boolean keepDefault) {
        nSetupAxisTicks(axis, vMin, vMax, nTicks, labels, labels.length, keepDefault);
    }

    public static void setupAxisTicks(int axis, double vMin, double vMax, int nTicks, boolean keepDefault) {
        nSetupAxisTicks(axis, vMin, vMax, nTicks, keepDefault);
    }

    public static void setupAxisScale(int axis, int scale) {
        nSetupAxisScale(axis, scale);
    }

    public static void setupAxisLimitsConstraints(int axis, double vMin, double vMax) {
        nSetupAxisLimitsConstraints(axis, vMin, vMax);
    }

    public static void setupAxisZoomConstraints(int axis, double vMin, double vMax) {
        nSetupAxisZoomConstraints(axis, vMin, vMax);
    }

    public static void setupAxes(String xLabel, String yLabel) {
        nSetupAxes(xLabel, yLabel);
    }

    public static void setupAxes(String xLabel, String yLabel, int xFlags) {
        nSetupAxes(xLabel, yLabel, xFlags);
    }

    public static void setupAxes(String xLabel, String yLabel, int xFlags, int yFlags) {
        nSetupAxes(xLabel, yLabel, xFlags, yFlags);
    }

    public static void setupAxesLimits(double xMin, double xMax, double yMin, double yMax) {
        nSetupAxesLimits(xMin, xMax, yMin, yMax);
    }

    public static void setupAxesLimits(double xMin, double xMax, double yMin, double yMax, int cond) {
        nSetupAxesLimits(xMin, xMax, yMin, yMax, cond);
    }

    public static void setupLegend(int location) {
        nSetupLegend(location);
    }

    public static void setupLegend(int location, int flags) {
        nSetupLegend(location, flags);
    }

    public static void setupMouseText(int location) {
        nSetupMouseText(location);
    }

    public static void setupMouseText(int location, int flags) {
        nSetupMouseText(location, flags);
    }

    public static void setupFinish() {
        nSetupFinish();
    }

    public static void setNextAxisLimits(int axis, double vMin, double vMax) {
        nSetNextAxisLimits(axis, vMin, vMax);
    }

    public static void setNextAxisLimits(int axis, double vMin, double vMax, int cond) {
        nSetNextAxisLimits(axis, vMin, vMax, cond);
    }

    public static void setNextAxisLinks(int axis, ImDouble linkMin, ImDouble linkMax) {
        nSetNextAxisLinks(axis, linkMin != null ? linkMin.getData() : null, linkMax != null ? linkMax.getData() : null);
    }

    public static void setNextAxisToFit(int axis) {
        nSetNextAxisToFit(axis);
    }

    public static void setNextAxesLimits(double xMin, double xMax, double yMin, double yMax) {
        nSetNextAxesLimits(xMin, xMax, yMin, yMax);
    }

    public static void setNextAxesLimits(double xMin, double xMax, double yMin, double yMax, int cond) {
        nSetNextAxesLimits(xMin, xMax, yMin, yMax, cond);
    }

    public static void setNextAxesToFit() {
        nSetNextAxesToFit();
    }

    public static void plotLine(String labelId, short[] values) {
        nPlotLine(labelId, values);
    }

    public static void plotLine(String labelId, short[] values, double xscale) {
        nPlotLine(labelId, values, xscale);
    }

    public static void plotLine(String labelId, short[] values, double xscale, double xstart) {
        nPlotLine(labelId, values, xscale, xstart);
    }

    public static void plotLine(String labelId, short[] values, double xscale, double xstart, int flags) {
        nPlotLine(labelId, values, xscale, xstart, flags);
    }

    public static void plotLine(String labelId, short[] values, double xscale, double xstart, int flags, int offset) {
        nPlotLine(labelId, values, xscale, xstart, flags, offset);
    }

    public static void plotLine(String labelId, int[] values) {
        nPlotLine(labelId, values);
    }

    public static void plotLine(String labelId, int[] values, double xscale) {
        nPlotLine(labelId, values, xscale);
    }

    public static void plotLine(String labelId, int[] values, double xscale, double xstart) {
        nPlotLine(labelId, values, xscale, xstart);
    }

    public static void plotLine(String labelId, int[] values, double xscale, double xstart, int flags) {
        nPlotLine(labelId, values, xscale, xstart, flags);
    }

    public static void plotLine(String labelId, int[] values, double xscale, double xstart, int flags, int offset) {
        nPlotLine(labelId, values, xscale, xstart, flags, offset);
    }

    public static void plotLine(String labelId, long[] values) {
        nPlotLine(labelId, values);
    }

    public static void plotLine(String labelId, long[] values, double xscale) {
        nPlotLine(labelId, values, xscale);
    }

    public static void plotLine(String labelId, long[] values, double xscale, double xstart) {
        nPlotLine(labelId, values, xscale, xstart);
    }

    public static void plotLine(String labelId, long[] values, double xscale, double xstart, int flags) {
        nPlotLine(labelId, values, xscale, xstart, flags);
    }

    public static void plotLine(String labelId, long[] values, double xscale, double xstart, int flags, int offset) {
        nPlotLine(labelId, values, xscale, xstart, flags, offset);
    }

    public static void plotLine(String labelId, float[] values) {
        nPlotLine(labelId, values);
    }

    public static void plotLine(String labelId, float[] values, double xscale) {
        nPlotLine(labelId, values, xscale);
    }

    public static void plotLine(String labelId, float[] values, double xscale, double xstart) {
        nPlotLine(labelId, values, xscale, xstart);
    }

    public static void plotLine(String labelId, float[] values, double xscale, double xstart, int flags) {
        nPlotLine(labelId, values, xscale, xstart, flags);
    }

    public static void plotLine(String labelId, float[] values, double xscale, double xstart, int flags, int offset) {
        nPlotLine(labelId, values, xscale, xstart, flags, offset);
    }

    public static void plotLine(String labelId, double[] values) {
        nPlotLine(labelId, values);
    }

    public static void plotLine(String labelId, double[] values, double xscale) {
        nPlotLine(labelId, values, xscale);
    }

    public static void plotLine(String labelId, double[] values, double xscale, double xstart) {
        nPlotLine(labelId, values, xscale, xstart);
    }

    public static void plotLine(String labelId, double[] values, double xscale, double xstart, int flags) {
        nPlotLine(labelId, values, xscale, xstart, flags);
    }

    public static void plotLine(String labelId, double[] values, double xscale, double xstart, int flags, int offset) {
        nPlotLine(labelId, values, xscale, xstart, flags, offset);
    }

    public static void plotLineV(String labelId, short[] values, int count) {
        nPlotLineV(labelId, values, count);
    }

    public static void plotLineV(String labelId, short[] values, int count, double xscale) {
        nPlotLineV(labelId, values, count, xscale);
    }

    public static void plotLineV(String labelId, short[] values, int count, double xscale, double xstart) {
        nPlotLineV(labelId, values, count, xscale, xstart);
    }

    public static void plotLineV(String labelId, short[] values, int count, double xscale, double xstart, int flags) {
        nPlotLineV(labelId, values, count, xscale, xstart, flags);
    }

    public static void plotLineV(String labelId, short[] values, int count, double xscale, double xstart, int flags, int offset) {
        nPlotLineV(labelId, values, count, xscale, xstart, flags, offset);
    }

    public static void plotLineV(String labelId, int[] values, int count) {
        nPlotLineV(labelId, values, count);
    }

    public static void plotLineV(String labelId, int[] values, int count, double xscale) {
        nPlotLineV(labelId, values, count, xscale);
    }

    public static void plotLineV(String labelId, int[] values, int count, double xscale, double xstart) {
        nPlotLineV(labelId, values, count, xscale, xstart);
    }

    public static void plotLineV(String labelId, int[] values, int count, double xscale, double xstart, int flags) {
        nPlotLineV(labelId, values, count, xscale, xstart, flags);
    }

    public static void plotLineV(String labelId, int[] values, int count, double xscale, double xstart, int flags, int offset) {
        nPlotLineV(labelId, values, count, xscale, xstart, flags, offset);
    }

    public static void plotLineV(String labelId, long[] values, int count) {
        nPlotLineV(labelId, values, count);
    }

    public static void plotLineV(String labelId, long[] values, int count, double xscale) {
        nPlotLineV(labelId, values, count, xscale);
    }

    public static void plotLineV(String labelId, long[] values, int count, double xscale, double xstart) {
        nPlotLineV(labelId, values, count, xscale, xstart);
    }

    public static void plotLineV(String labelId, long[] values, int count, double xscale, double xstart, int flags) {
        nPlotLineV(labelId, values, count, xscale, xstart, flags);
    }

    public static void plotLineV(String labelId, long[] values, int count, double xscale, double xstart, int flags, int offset) {
        nPlotLineV(labelId, values, count, xscale, xstart, flags, offset);
    }

    public static void plotLineV(String labelId, float[] values, int count) {
        nPlotLineV(labelId, values, count);
    }

    public static void plotLineV(String labelId, float[] values, int count, double xscale) {
        nPlotLineV(labelId, values, count, xscale);
    }

    public static void plotLineV(String labelId, float[] values, int count, double xscale, double xstart) {
        nPlotLineV(labelId, values, count, xscale, xstart);
    }

    public static void plotLineV(String labelId, float[] values, int count, double xscale, double xstart, int flags) {
        nPlotLineV(labelId, values, count, xscale, xstart, flags);
    }

    public static void plotLineV(String labelId, float[] values, int count, double xscale, double xstart, int flags, int offset) {
        nPlotLineV(labelId, values, count, xscale, xstart, flags, offset);
    }

    public static void plotLineV(String labelId, double[] values, int count) {
        nPlotLineV(labelId, values, count);
    }

    public static void plotLineV(String labelId, double[] values, int count, double xscale) {
        nPlotLineV(labelId, values, count, xscale);
    }

    public static void plotLineV(String labelId, double[] values, int count, double xscale, double xstart) {
        nPlotLineV(labelId, values, count, xscale, xstart);
    }

    public static void plotLineV(String labelId, double[] values, int count, double xscale, double xstart, int flags) {
        nPlotLineV(labelId, values, count, xscale, xstart, flags);
    }

    public static void plotLineV(String labelId, double[] values, int count, double xscale, double xstart, int flags, int offset) {
        nPlotLineV(labelId, values, count, xscale, xstart, flags, offset);
    }

    public static void plotLine(String labelId, short[] xs, short[] ys) {
        nPlotLine(labelId, xs, ys);
    }

    public static void plotLine(String labelId, short[] xs, short[] ys, int flags) {
        nPlotLine(labelId, xs, ys, flags);
    }

    public static void plotLine(String labelId, int[] xs, int[] ys) {
        nPlotLine(labelId, xs, ys);
    }

    public static void plotLine(String labelId, int[] xs, int[] ys, int flags) {
        nPlotLine(labelId, xs, ys, flags);
    }

    public static void plotLine(String labelId, long[] xs, long[] ys) {
        nPlotLine(labelId, xs, ys);
    }

    public static void plotLine(String labelId, long[] xs, long[] ys, int flags) {
        nPlotLine(labelId, xs, ys, flags);
    }

    public static void plotLine(String labelId, float[] xs, float[] ys) {
        nPlotLine(labelId, xs, ys);
    }

    public static void plotLine(String labelId, float[] xs, float[] ys, int flags) {
        nPlotLine(labelId, xs, ys, flags);
    }

    public static void plotLine(String labelId, double[] xs, double[] ys) {
        nPlotLine(labelId, xs, ys);
    }

    public static void plotLine(String labelId, double[] xs, double[] ys, int flags) {
        nPlotLine(labelId, xs, ys, flags);
    }

    public static void plotLineV(String labelId, short[] xs, short[] ys, int count) {
        nPlotLineV(labelId, xs, ys, count);
    }

    public static void plotLineV(String labelId, short[] xs, short[] ys, int count, int flags) {
        nPlotLineV(labelId, xs, ys, count, flags);
    }

    public static void plotLineV(String labelId, short[] xs, short[] ys, int count, int flags, int offset) {
        nPlotLineV(labelId, xs, ys, count, flags, offset);
    }

    public static void plotLineV(String labelId, int[] xs, int[] ys, int count) {
        nPlotLineV(labelId, xs, ys, count);
    }

    public static void plotLineV(String labelId, int[] xs, int[] ys, int count, int flags) {
        nPlotLineV(labelId, xs, ys, count, flags);
    }

    public static void plotLineV(String labelId, int[] xs, int[] ys, int count, int flags, int offset) {
        nPlotLineV(labelId, xs, ys, count, flags, offset);
    }

    public static void plotLineV(String labelId, long[] xs, long[] ys, int count) {
        nPlotLineV(labelId, xs, ys, count);
    }

    public static void plotLineV(String labelId, long[] xs, long[] ys, int count, int flags) {
        nPlotLineV(labelId, xs, ys, count, flags);
    }

    public static void plotLineV(String labelId, long[] xs, long[] ys, int count, int flags, int offset) {
        nPlotLineV(labelId, xs, ys, count, flags, offset);
    }

    public static void plotLineV(String labelId, float[] xs, float[] ys, int count) {
        nPlotLineV(labelId, xs, ys, count);
    }

    public static void plotLineV(String labelId, float[] xs, float[] ys, int count, int flags) {
        nPlotLineV(labelId, xs, ys, count, flags);
    }

    public static void plotLineV(String labelId, float[] xs, float[] ys, int count, int flags, int offset) {
        nPlotLineV(labelId, xs, ys, count, flags, offset);
    }

    public static void plotLineV(String labelId, double[] xs, double[] ys, int count) {
        nPlotLineV(labelId, xs, ys, count);
    }

    public static void plotLineV(String labelId, double[] xs, double[] ys, int count, int flags) {
        nPlotLineV(labelId, xs, ys, count, flags);
    }

    public static void plotLineV(String labelId, double[] xs, double[] ys, int count, int flags, int offset) {
        nPlotLineV(labelId, xs, ys, count, flags, offset);
    }

    public static void plotScatter(String labelId, short[] values) {
        nPlotScatter(labelId, values);
    }

    public static void plotScatter(String labelId, short[] values, double xscale) {
        nPlotScatter(labelId, values, xscale);
    }

    public static void plotScatter(String labelId, short[] values, double xscale, double xstart) {
        nPlotScatter(labelId, values, xscale, xstart);
    }

    public static void plotScatter(String labelId, short[] values, double xscale, double xstart, int flags) {
        nPlotScatter(labelId, values, xscale, xstart, flags);
    }

    public static void plotScatter(String labelId, short[] values, double xscale, double xstart, int flags, int offset) {
        nPlotScatter(labelId, values, xscale, xstart, flags, offset);
    }

    public static void plotScatter(String labelId, int[] values) {
        nPlotScatter(labelId, values);
    }

    public static void plotScatter(String labelId, int[] values, double xscale) {
        nPlotScatter(labelId, values, xscale);
    }

    public static void plotScatter(String labelId, int[] values, double xscale, double xstart) {
        nPlotScatter(labelId, values, xscale, xstart);
    }

    public static void plotScatter(String labelId, int[] values, double xscale, double xstart, int flags) {
        nPlotScatter(labelId, values, xscale, xstart, flags);
    }

    public static void plotScatter(String labelId, int[] values, double xscale, double xstart, int flags, int offset) {
        nPlotScatter(labelId, values, xscale, xstart, flags, offset);
    }

    public static void plotScatter(String labelId, long[] values) {
        nPlotScatter(labelId, values);
    }

    public static void plotScatter(String labelId, long[] values, double xscale) {
        nPlotScatter(labelId, values, xscale);
    }

    public static void plotScatter(String labelId, long[] values, double xscale, double xstart) {
        nPlotScatter(labelId, values, xscale, xstart);
    }

    public static void plotScatter(String labelId, long[] values, double xscale, double xstart, int flags) {
        nPlotScatter(labelId, values, xscale, xstart, flags);
    }

    public static void plotScatter(String labelId, long[] values, double xscale, double xstart, int flags, int offset) {
        nPlotScatter(labelId, values, xscale, xstart, flags, offset);
    }

    public static void plotScatter(String labelId, float[] values) {
        nPlotScatter(labelId, values);
    }

    public static void plotScatter(String labelId, float[] values, double xscale) {
        nPlotScatter(labelId, values, xscale);
    }

    public static void plotScatter(String labelId, float[] values, double xscale, double xstart) {
        nPlotScatter(labelId, values, xscale, xstart);
    }

    public static void plotScatter(String labelId, float[] values, double xscale, double xstart, int flags) {
        nPlotScatter(labelId, values, xscale, xstart, flags);
    }

    public static void plotScatter(String labelId, float[] values, double xscale, double xstart, int flags, int offset) {
        nPlotScatter(labelId, values, xscale, xstart, flags, offset);
    }

    public static void plotScatter(String labelId, double[] values) {
        nPlotScatter(labelId, values);
    }

    public static void plotScatter(String labelId, double[] values, double xscale) {
        nPlotScatter(labelId, values, xscale);
    }

    public static void plotScatter(String labelId, double[] values, double xscale, double xstart) {
        nPlotScatter(labelId, values, xscale, xstart);
    }

    public static void plotScatter(String labelId, double[] values, double xscale, double xstart, int flags) {
        nPlotScatter(labelId, values, xscale, xstart, flags);
    }

    public static void plotScatter(String labelId, double[] values, double xscale, double xstart, int flags, int offset) {
        nPlotScatter(labelId, values, xscale, xstart, flags, offset);
    }

    public static void plotScatterV(String labelId, short[] values, int count) {
        nPlotScatterV(labelId, values, count);
    }

    public static void plotScatterV(String labelId, short[] values, int count, double xscale) {
        nPlotScatterV(labelId, values, count, xscale);
    }

    public static void plotScatterV(String labelId, short[] values, int count, double xscale, double xstart) {
        nPlotScatterV(labelId, values, count, xscale, xstart);
    }

    public static void plotScatterV(String labelId, short[] values, int count, double xscale, double xstart, int flags) {
        nPlotScatterV(labelId, values, count, xscale, xstart, flags);
    }

    public static void plotScatterV(String labelId, short[] values, int count, double xscale, double xstart, int flags, int offset) {
        nPlotScatterV(labelId, values, count, xscale, xstart, flags, offset);
    }

    public static void plotScatterV(String labelId, int[] values, int count) {
        nPlotScatterV(labelId, values, count);
    }

    public static void plotScatterV(String labelId, int[] values, int count, double xscale) {
        nPlotScatterV(labelId, values, count, xscale);
    }

    public static void plotScatterV(String labelId, int[] values, int count, double xscale, double xstart) {
        nPlotScatterV(labelId, values, count, xscale, xstart);
    }

    public static void plotScatterV(String labelId, int[] values, int count, double xscale, double xstart, int flags) {
        nPlotScatterV(labelId, values, count, xscale, xstart, flags);
    }

    public static void plotScatterV(String labelId, int[] values, int count, double xscale, double xstart, int flags, int offset) {
        nPlotScatterV(labelId, values, count, xscale, xstart, flags, offset);
    }

    public static void plotScatterV(String labelId, long[] values, int count) {
        nPlotScatterV(labelId, values, count);
    }

    public static void plotScatterV(String labelId, long[] values, int count, double xscale) {
        nPlotScatterV(labelId, values, count, xscale);
    }

    public static void plotScatterV(String labelId, long[] values, int count, double xscale, double xstart) {
        nPlotScatterV(labelId, values, count, xscale, xstart);
    }

    public static void plotScatterV(String labelId, long[] values, int count, double xscale, double xstart, int flags) {
        nPlotScatterV(labelId, values, count, xscale, xstart, flags);
    }

    public static void plotScatterV(String labelId, long[] values, int count, double xscale, double xstart, int flags, int offset) {
        nPlotScatterV(labelId, values, count, xscale, xstart, flags, offset);
    }

    public static void plotScatterV(String labelId, float[] values, int count) {
        nPlotScatterV(labelId, values, count);
    }

    public static void plotScatterV(String labelId, float[] values, int count, double xscale) {
        nPlotScatterV(labelId, values, count, xscale);
    }

    public static void plotScatterV(String labelId, float[] values, int count, double xscale, double xstart) {
        nPlotScatterV(labelId, values, count, xscale, xstart);
    }

    public static void plotScatterV(String labelId, float[] values, int count, double xscale, double xstart, int flags) {
        nPlotScatterV(labelId, values, count, xscale, xstart, flags);
    }

    public static void plotScatterV(String labelId, float[] values, int count, double xscale, double xstart, int flags, int offset) {
        nPlotScatterV(labelId, values, count, xscale, xstart, flags, offset);
    }

    public static void plotScatterV(String labelId, double[] values, int count) {
        nPlotScatterV(labelId, values, count);
    }

    public static void plotScatterV(String labelId, double[] values, int count, double xscale) {
        nPlotScatterV(labelId, values, count, xscale);
    }

    public static void plotScatterV(String labelId, double[] values, int count, double xscale, double xstart) {
        nPlotScatterV(labelId, values, count, xscale, xstart);
    }

    public static void plotScatterV(String labelId, double[] values, int count, double xscale, double xstart, int flags) {
        nPlotScatterV(labelId, values, count, xscale, xstart, flags);
    }

    public static void plotScatterV(String labelId, double[] values, int count, double xscale, double xstart, int flags, int offset) {
        nPlotScatterV(labelId, values, count, xscale, xstart, flags, offset);
    }

    public static void plotScatter(String labelId, short[] xs, short[] ys) {
        nPlotScatter(labelId, xs, ys);
    }

    public static void plotScatter(String labelId, short[] xs, short[] ys, int flags) {
        nPlotScatter(labelId, xs, ys, flags);
    }

    public static void plotScatter(String labelId, int[] xs, int[] ys) {
        nPlotScatter(labelId, xs, ys);
    }

    public static void plotScatter(String labelId, int[] xs, int[] ys, int flags) {
        nPlotScatter(labelId, xs, ys, flags);
    }

    public static void plotScatter(String labelId, long[] xs, long[] ys) {
        nPlotScatter(labelId, xs, ys);
    }

    public static void plotScatter(String labelId, long[] xs, long[] ys, int flags) {
        nPlotScatter(labelId, xs, ys, flags);
    }

    public static void plotScatter(String labelId, float[] xs, float[] ys) {
        nPlotScatter(labelId, xs, ys);
    }

    public static void plotScatter(String labelId, float[] xs, float[] ys, int flags) {
        nPlotScatter(labelId, xs, ys, flags);
    }

    public static void plotScatter(String labelId, double[] xs, double[] ys) {
        nPlotScatter(labelId, xs, ys);
    }

    public static void plotScatter(String labelId, double[] xs, double[] ys, int flags) {
        nPlotScatter(labelId, xs, ys, flags);
    }

    public static void plotScatterV(String labelId, short[] xs, short[] ys, int count) {
        nPlotScatterV(labelId, xs, ys, count);
    }

    public static void plotScatterV(String labelId, short[] xs, short[] ys, int count, int flags) {
        nPlotScatterV(labelId, xs, ys, count, flags);
    }

    public static void plotScatterV(String labelId, short[] xs, short[] ys, int count, int flags, int offset) {
        nPlotScatterV(labelId, xs, ys, count, flags, offset);
    }

    public static void plotScatterV(String labelId, int[] xs, int[] ys, int count) {
        nPlotScatterV(labelId, xs, ys, count);
    }

    public static void plotScatterV(String labelId, int[] xs, int[] ys, int count, int flags) {
        nPlotScatterV(labelId, xs, ys, count, flags);
    }

    public static void plotScatterV(String labelId, int[] xs, int[] ys, int count, int flags, int offset) {
        nPlotScatterV(labelId, xs, ys, count, flags, offset);
    }

    public static void plotScatterV(String labelId, long[] xs, long[] ys, int count) {
        nPlotScatterV(labelId, xs, ys, count);
    }

    public static void plotScatterV(String labelId, long[] xs, long[] ys, int count, int flags) {
        nPlotScatterV(labelId, xs, ys, count, flags);
    }

    public static void plotScatterV(String labelId, long[] xs, long[] ys, int count, int flags, int offset) {
        nPlotScatterV(labelId, xs, ys, count, flags, offset);
    }

    public static void plotScatterV(String labelId, float[] xs, float[] ys, int count) {
        nPlotScatterV(labelId, xs, ys, count);
    }

    public static void plotScatterV(String labelId, float[] xs, float[] ys, int count, int flags) {
        nPlotScatterV(labelId, xs, ys, count, flags);
    }

    public static void plotScatterV(String labelId, float[] xs, float[] ys, int count, int flags, int offset) {
        nPlotScatterV(labelId, xs, ys, count, flags, offset);
    }

    public static void plotScatterV(String labelId, double[] xs, double[] ys, int count) {
        nPlotScatterV(labelId, xs, ys, count);
    }

    public static void plotScatterV(String labelId, double[] xs, double[] ys, int count, int flags) {
        nPlotScatterV(labelId, xs, ys, count, flags);
    }

    public static void plotScatterV(String labelId, double[] xs, double[] ys, int count, int flags, int offset) {
        nPlotScatterV(labelId, xs, ys, count, flags, offset);
    }

    public static void plotStairs(String labelId, short[] values) {
        nPlotStairs(labelId, values);
    }

    public static void plotStairs(String labelId, short[] values, double xscale) {
        nPlotStairs(labelId, values, xscale);
    }

    public static void plotStairs(String labelId, short[] values, double xscale, double xstart) {
        nPlotStairs(labelId, values, xscale, xstart);
    }

    public static void plotStairs(String labelId, short[] values, double xscale, double xstart, int flags) {
        nPlotStairs(labelId, values, xscale, xstart, flags);
    }

    public static void plotStairs(String labelId, short[] values, double xscale, double xstart, int flags, int offset) {
        nPlotStairs(labelId, values, xscale, xstart, flags, offset);
    }

    public static void plotStairs(String labelId, int[] values) {
        nPlotStairs(labelId, values);
    }

    public static void plotStairs(String labelId, int[] values, double xscale) {
        nPlotStairs(labelId, values, xscale);
    }

    public static void plotStairs(String labelId, int[] values, double xscale, double xstart) {
        nPlotStairs(labelId, values, xscale, xstart);
    }

    public static void plotStairs(String labelId, int[] values, double xscale, double xstart, int flags) {
        nPlotStairs(labelId, values, xscale, xstart, flags);
    }

    public static void plotStairs(String labelId, int[] values, double xscale, double xstart, int flags, int offset) {
        nPlotStairs(labelId, values, xscale, xstart, flags, offset);
    }

    public static void plotStairs(String labelId, long[] values) {
        nPlotStairs(labelId, values);
    }

    public static void plotStairs(String labelId, long[] values, double xscale) {
        nPlotStairs(labelId, values, xscale);
    }

    public static void plotStairs(String labelId, long[] values, double xscale, double xstart) {
        nPlotStairs(labelId, values, xscale, xstart);
    }

    public static void plotStairs(String labelId, long[] values, double xscale, double xstart, int flags) {
        nPlotStairs(labelId, values, xscale, xstart, flags);
    }

    public static void plotStairs(String labelId, long[] values, double xscale, double xstart, int flags, int offset) {
        nPlotStairs(labelId, values, xscale, xstart, flags, offset);
    }

    public static void plotStairs(String labelId, float[] values) {
        nPlotStairs(labelId, values);
    }

    public static void plotStairs(String labelId, float[] values, double xscale) {
        nPlotStairs(labelId, values, xscale);
    }

    public static void plotStairs(String labelId, float[] values, double xscale, double xstart) {
        nPlotStairs(labelId, values, xscale, xstart);
    }

    public static void plotStairs(String labelId, float[] values, double xscale, double xstart, int flags) {
        nPlotStairs(labelId, values, xscale, xstart, flags);
    }

    public static void plotStairs(String labelId, float[] values, double xscale, double xstart, int flags, int offset) {
        nPlotStairs(labelId, values, xscale, xstart, flags, offset);
    }

    public static void plotStairs(String labelId, double[] values) {
        nPlotStairs(labelId, values);
    }

    public static void plotStairs(String labelId, double[] values, double xscale) {
        nPlotStairs(labelId, values, xscale);
    }

    public static void plotStairs(String labelId, double[] values, double xscale, double xstart) {
        nPlotStairs(labelId, values, xscale, xstart);
    }

    public static void plotStairs(String labelId, double[] values, double xscale, double xstart, int flags) {
        nPlotStairs(labelId, values, xscale, xstart, flags);
    }

    public static void plotStairs(String labelId, double[] values, double xscale, double xstart, int flags, int offset) {
        nPlotStairs(labelId, values, xscale, xstart, flags, offset);
    }

    public static void plotStairsV(String labelId, short[] values, int count) {
        nPlotStairsV(labelId, values, count);
    }

    public static void plotStairsV(String labelId, short[] values, int count, double xscale) {
        nPlotStairsV(labelId, values, count, xscale);
    }

    public static void plotStairsV(String labelId, short[] values, int count, double xscale, double xstart) {
        nPlotStairsV(labelId, values, count, xscale, xstart);
    }

    public static void plotStairsV(String labelId, short[] values, int count, double xscale, double xstart, int flags) {
        nPlotStairsV(labelId, values, count, xscale, xstart, flags);
    }

    public static void plotStairsV(String labelId, short[] values, int count, double xscale, double xstart, int flags, int offset) {
        nPlotStairsV(labelId, values, count, xscale, xstart, flags, offset);
    }

    public static void plotStairsV(String labelId, int[] values, int count) {
        nPlotStairsV(labelId, values, count);
    }

    public static void plotStairsV(String labelId, int[] values, int count, double xscale) {
        nPlotStairsV(labelId, values, count, xscale);
    }

    public static void plotStairsV(String labelId, int[] values, int count, double xscale, double xstart) {
        nPlotStairsV(labelId, values, count, xscale, xstart);
    }

    public static void plotStairsV(String labelId, int[] values, int count, double xscale, double xstart, int flags) {
        nPlotStairsV(labelId, values, count, xscale, xstart, flags);
    }

    public static void plotStairsV(String labelId, int[] values, int count, double xscale, double xstart, int flags, int offset) {
        nPlotStairsV(labelId, values, count, xscale, xstart, flags, offset);
    }

    public static void plotStairsV(String labelId, long[] values, int count) {
        nPlotStairsV(labelId, values, count);
    }

    public static void plotStairsV(String labelId, long[] values, int count, double xscale) {
        nPlotStairsV(labelId, values, count, xscale);
    }

    public static void plotStairsV(String labelId, long[] values, int count, double xscale, double xstart) {
        nPlotStairsV(labelId, values, count, xscale, xstart);
    }

    public static void plotStairsV(String labelId, long[] values, int count, double xscale, double xstart, int flags) {
        nPlotStairsV(labelId, values, count, xscale, xstart, flags);
    }

    public static void plotStairsV(String labelId, long[] values, int count, double xscale, double xstart, int flags, int offset) {
        nPlotStairsV(labelId, values, count, xscale, xstart, flags, offset);
    }

    public static void plotStairsV(String labelId, float[] values, int count) {
        nPlotStairsV(labelId, values, count);
    }

    public static void plotStairsV(String labelId, float[] values, int count, double xscale) {
        nPlotStairsV(labelId, values, count, xscale);
    }

    public static void plotStairsV(String labelId, float[] values, int count, double xscale, double xstart) {
        nPlotStairsV(labelId, values, count, xscale, xstart);
    }

    public static void plotStairsV(String labelId, float[] values, int count, double xscale, double xstart, int flags) {
        nPlotStairsV(labelId, values, count, xscale, xstart, flags);
    }

    public static void plotStairsV(String labelId, float[] values, int count, double xscale, double xstart, int flags, int offset) {
        nPlotStairsV(labelId, values, count, xscale, xstart, flags, offset);
    }

    public static void plotStairsV(String labelId, double[] values, int count) {
        nPlotStairsV(labelId, values, count);
    }

    public static void plotStairsV(String labelId, double[] values, int count, double xscale) {
        nPlotStairsV(labelId, values, count, xscale);
    }

    public static void plotStairsV(String labelId, double[] values, int count, double xscale, double xstart) {
        nPlotStairsV(labelId, values, count, xscale, xstart);
    }

    public static void plotStairsV(String labelId, double[] values, int count, double xscale, double xstart, int flags) {
        nPlotStairsV(labelId, values, count, xscale, xstart, flags);
    }

    public static void plotStairsV(String labelId, double[] values, int count, double xscale, double xstart, int flags, int offset) {
        nPlotStairsV(labelId, values, count, xscale, xstart, flags, offset);
    }

    public static void plotStairs(String labelId, short[] xs, short[] ys) {
        nPlotStairs(labelId, xs, ys);
    }

    public static void plotStairs(String labelId, short[] xs, short[] ys, int flags) {
        nPlotStairs(labelId, xs, ys, flags);
    }

    public static void plotStairs(String labelId, int[] xs, int[] ys) {
        nPlotStairs(labelId, xs, ys);
    }

    public static void plotStairs(String labelId, int[] xs, int[] ys, int flags) {
        nPlotStairs(labelId, xs, ys, flags);
    }

    public static void plotStairs(String labelId, long[] xs, long[] ys) {
        nPlotStairs(labelId, xs, ys);
    }

    public static void plotStairs(String labelId, long[] xs, long[] ys, int flags) {
        nPlotStairs(labelId, xs, ys, flags);
    }

    public static void plotStairs(String labelId, float[] xs, float[] ys) {
        nPlotStairs(labelId, xs, ys);
    }

    public static void plotStairs(String labelId, float[] xs, float[] ys, int flags) {
        nPlotStairs(labelId, xs, ys, flags);
    }

    public static void plotStairs(String labelId, double[] xs, double[] ys) {
        nPlotStairs(labelId, xs, ys);
    }

    public static void plotStairs(String labelId, double[] xs, double[] ys, int flags) {
        nPlotStairs(labelId, xs, ys, flags);
    }

    public static void plotStairsV(String labelId, short[] xs, short[] ys, int count) {
        nPlotStairsV(labelId, xs, ys, count);
    }

    public static void plotStairsV(String labelId, short[] xs, short[] ys, int count, int flags) {
        nPlotStairsV(labelId, xs, ys, count, flags);
    }

    public static void plotStairsV(String labelId, short[] xs, short[] ys, int count, int flags, int offset) {
        nPlotStairsV(labelId, xs, ys, count, flags, offset);
    }

    public static void plotStairsV(String labelId, int[] xs, int[] ys, int count) {
        nPlotStairsV(labelId, xs, ys, count);
    }

    public static void plotStairsV(String labelId, int[] xs, int[] ys, int count, int flags) {
        nPlotStairsV(labelId, xs, ys, count, flags);
    }

    public static void plotStairsV(String labelId, int[] xs, int[] ys, int count, int flags, int offset) {
        nPlotStairsV(labelId, xs, ys, count, flags, offset);
    }

    public static void plotStairsV(String labelId, long[] xs, long[] ys, int count) {
        nPlotStairsV(labelId, xs, ys, count);
    }

    public static void plotStairsV(String labelId, long[] xs, long[] ys, int count, int flags) {
        nPlotStairsV(labelId, xs, ys, count, flags);
    }

    public static void plotStairsV(String labelId, long[] xs, long[] ys, int count, int flags, int offset) {
        nPlotStairsV(labelId, xs, ys, count, flags, offset);
    }

    public static void plotStairsV(String labelId, float[] xs, float[] ys, int count) {
        nPlotStairsV(labelId, xs, ys, count);
    }

    public static void plotStairsV(String labelId, float[] xs, float[] ys, int count, int flags) {
        nPlotStairsV(labelId, xs, ys, count, flags);
    }

    public static void plotStairsV(String labelId, float[] xs, float[] ys, int count, int flags, int offset) {
        nPlotStairsV(labelId, xs, ys, count, flags, offset);
    }

    public static void plotStairsV(String labelId, double[] xs, double[] ys, int count) {
        nPlotStairsV(labelId, xs, ys, count);
    }

    public static void plotStairsV(String labelId, double[] xs, double[] ys, int count, int flags) {
        nPlotStairsV(labelId, xs, ys, count, flags);
    }

    public static void plotStairsV(String labelId, double[] xs, double[] ys, int count, int flags, int offset) {
        nPlotStairsV(labelId, xs, ys, count, flags, offset);
    }

    public static void plotShaded(String labelId, short[] values) {
        nPlotShaded(labelId, values);
    }

    public static void plotShaded(String labelId, short[] values, double yRef) {
        nPlotShaded(labelId, values, yRef);
    }

    public static void plotShaded(String labelId, short[] values, double yRef, double xscale) {
        nPlotShaded(labelId, values, yRef, xscale);
    }

    public static void plotShaded(String labelId, short[] values, double yRef, double xscale, double xstart) {
        nPlotShaded(labelId, values, yRef, xscale, xstart);
    }

    public static void plotShaded(String labelId, short[] values, double yRef, double xscale, double xstart, int flags) {
        nPlotShaded(labelId, values, yRef, xscale, xstart, flags);
    }

    public static void plotShaded(String labelId, short[] values, double yRef, double xscale, double xstart, int flags, int offset) {
        nPlotShaded(labelId, values, yRef, xscale, xstart, flags, offset);
    }

    public static void plotShaded(String labelId, int[] values) {
        nPlotShaded(labelId, values);
    }

    public static void plotShaded(String labelId, int[] values, double yRef) {
        nPlotShaded(labelId, values, yRef);
    }

    public static void plotShaded(String labelId, int[] values, double yRef, double xscale) {
        nPlotShaded(labelId, values, yRef, xscale);
    }

    public static void plotShaded(String labelId, int[] values, double yRef, double xscale, double xstart) {
        nPlotShaded(labelId, values, yRef, xscale, xstart);
    }

    public static void plotShaded(String labelId, int[] values, double yRef, double xscale, double xstart, int flags) {
        nPlotShaded(labelId, values, yRef, xscale, xstart, flags);
    }

    public static void plotShaded(String labelId, int[] values, double yRef, double xscale, double xstart, int flags, int offset) {
        nPlotShaded(labelId, values, yRef, xscale, xstart, flags, offset);
    }

    public static void plotShaded(String labelId, long[] values) {
        nPlotShaded(labelId, values);
    }

    public static void plotShaded(String labelId, long[] values, double yRef) {
        nPlotShaded(labelId, values, yRef);
    }

    public static void plotShaded(String labelId, long[] values, double yRef, double xscale) {
        nPlotShaded(labelId, values, yRef, xscale);
    }

    public static void plotShaded(String labelId, long[] values, double yRef, double xscale, double xstart) {
        nPlotShaded(labelId, values, yRef, xscale, xstart);
    }

    public static void plotShaded(String labelId, long[] values, double yRef, double xscale, double xstart, int flags) {
        nPlotShaded(labelId, values, yRef, xscale, xstart, flags);
    }

    public static void plotShaded(String labelId, long[] values, double yRef, double xscale, double xstart, int flags, int offset) {
        nPlotShaded(labelId, values, yRef, xscale, xstart, flags, offset);
    }

    public static void plotShaded(String labelId, float[] values) {
        nPlotShaded(labelId, values);
    }

    public static void plotShaded(String labelId, float[] values, double yRef) {
        nPlotShaded(labelId, values, yRef);
    }

    public static void plotShaded(String labelId, float[] values, double yRef, double xscale) {
        nPlotShaded(labelId, values, yRef, xscale);
    }

    public static void plotShaded(String labelId, float[] values, double yRef, double xscale, double xstart) {
        nPlotShaded(labelId, values, yRef, xscale, xstart);
    }

    public static void plotShaded(String labelId, float[] values, double yRef, double xscale, double xstart, int flags) {
        nPlotShaded(labelId, values, yRef, xscale, xstart, flags);
    }

    public static void plotShaded(String labelId, float[] values, double yRef, double xscale, double xstart, int flags, int offset) {
        nPlotShaded(labelId, values, yRef, xscale, xstart, flags, offset);
    }

    public static void plotShaded(String labelId, double[] values) {
        nPlotShaded(labelId, values);
    }

    public static void plotShaded(String labelId, double[] values, double yRef) {
        nPlotShaded(labelId, values, yRef);
    }

    public static void plotShaded(String labelId, double[] values, double yRef, double xscale) {
        nPlotShaded(labelId, values, yRef, xscale);
    }

    public static void plotShaded(String labelId, double[] values, double yRef, double xscale, double xstart) {
        nPlotShaded(labelId, values, yRef, xscale, xstart);
    }

    public static void plotShaded(String labelId, double[] values, double yRef, double xscale, double xstart, int flags) {
        nPlotShaded(labelId, values, yRef, xscale, xstart, flags);
    }

    public static void plotShaded(String labelId, double[] values, double yRef, double xscale, double xstart, int flags, int offset) {
        nPlotShaded(labelId, values, yRef, xscale, xstart, flags, offset);
    }

    public static void plotShadedV(String labelId, short[] values, int count) {
        nPlotShadedV(labelId, values, count);
    }

    public static void plotShadedV(String labelId, short[] values, int count, double yRef) {
        nPlotShadedV(labelId, values, count, yRef);
    }

    public static void plotShadedV(String labelId, short[] values, int count, double yRef, double xscale) {
        nPlotShadedV(labelId, values, count, yRef, xscale);
    }

    public static void plotShadedV(String labelId, short[] values, int count, double yRef, double xscale, double xstart) {
        nPlotShadedV(labelId, values, count, yRef, xscale, xstart);
    }

    public static void plotShadedV(String labelId, short[] values, int count, double yRef, double xscale, double xstart, int flags) {
        nPlotShadedV(labelId, values, count, yRef, xscale, xstart, flags);
    }

    public static void plotShadedV(String labelId, short[] values, int count, double yRef, double xscale, double xstart, int flags, int offset) {
        nPlotShadedV(labelId, values, count, yRef, xscale, xstart, flags, offset);
    }

    public static void plotShadedV(String labelId, int[] values, int count) {
        nPlotShadedV(labelId, values, count);
    }

    public static void plotShadedV(String labelId, int[] values, int count, double yRef) {
        nPlotShadedV(labelId, values, count, yRef);
    }

    public static void plotShadedV(String labelId, int[] values, int count, double yRef, double xscale) {
        nPlotShadedV(labelId, values, count, yRef, xscale);
    }

    public static void plotShadedV(String labelId, int[] values, int count, double yRef, double xscale, double xstart) {
        nPlotShadedV(labelId, values, count, yRef, xscale, xstart);
    }

    public static void plotShadedV(String labelId, int[] values, int count, double yRef, double xscale, double xstart, int flags) {
        nPlotShadedV(labelId, values, count, yRef, xscale, xstart, flags);
    }

    public static void plotShadedV(String labelId, int[] values, int count, double yRef, double xscale, double xstart, int flags, int offset) {
        nPlotShadedV(labelId, values, count, yRef, xscale, xstart, flags, offset);
    }

    public static void plotShadedV(String labelId, long[] values, int count) {
        nPlotShadedV(labelId, values, count);
    }

    public static void plotShadedV(String labelId, long[] values, int count, double yRef) {
        nPlotShadedV(labelId, values, count, yRef);
    }

    public static void plotShadedV(String labelId, long[] values, int count, double yRef, double xscale) {
        nPlotShadedV(labelId, values, count, yRef, xscale);
    }

    public static void plotShadedV(String labelId, long[] values, int count, double yRef, double xscale, double xstart) {
        nPlotShadedV(labelId, values, count, yRef, xscale, xstart);
    }

    public static void plotShadedV(String labelId, long[] values, int count, double yRef, double xscale, double xstart, int flags) {
        nPlotShadedV(labelId, values, count, yRef, xscale, xstart, flags);
    }

    public static void plotShadedV(String labelId, long[] values, int count, double yRef, double xscale, double xstart, int flags, int offset) {
        nPlotShadedV(labelId, values, count, yRef, xscale, xstart, flags, offset);
    }

    public static void plotShadedV(String labelId, float[] values, int count) {
        nPlotShadedV(labelId, values, count);
    }

    public static void plotShadedV(String labelId, float[] values, int count, double yRef) {
        nPlotShadedV(labelId, values, count, yRef);
    }

    public static void plotShadedV(String labelId, float[] values, int count, double yRef, double xscale) {
        nPlotShadedV(labelId, values, count, yRef, xscale);
    }

    public static void plotShadedV(String labelId, float[] values, int count, double yRef, double xscale, double xstart) {
        nPlotShadedV(labelId, values, count, yRef, xscale, xstart);
    }

    public static void plotShadedV(String labelId, float[] values, int count, double yRef, double xscale, double xstart, int flags) {
        nPlotShadedV(labelId, values, count, yRef, xscale, xstart, flags);
    }

    public static void plotShadedV(String labelId, float[] values, int count, double yRef, double xscale, double xstart, int flags, int offset) {
        nPlotShadedV(labelId, values, count, yRef, xscale, xstart, flags, offset);
    }

    public static void plotShadedV(String labelId, double[] values, int count) {
        nPlotShadedV(labelId, values, count);
    }

    public static void plotShadedV(String labelId, double[] values, int count, double yRef) {
        nPlotShadedV(labelId, values, count, yRef);
    }

    public static void plotShadedV(String labelId, double[] values, int count, double yRef, double xscale) {
        nPlotShadedV(labelId, values, count, yRef, xscale);
    }

    public static void plotShadedV(String labelId, double[] values, int count, double yRef, double xscale, double xstart) {
        nPlotShadedV(labelId, values, count, yRef, xscale, xstart);
    }

    public static void plotShadedV(String labelId, double[] values, int count, double yRef, double xscale, double xstart, int flags) {
        nPlotShadedV(labelId, values, count, yRef, xscale, xstart, flags);
    }

    public static void plotShadedV(String labelId, double[] values, int count, double yRef, double xscale, double xstart, int flags, int offset) {
        nPlotShadedV(labelId, values, count, yRef, xscale, xstart, flags, offset);
    }

    public static void plotShaded(String labelId, short[] xs, short[] ys) {
        nPlotShaded(labelId, xs, ys);
    }

    public static void plotShaded(String labelId, short[] xs, short[] ys, double yRef) {
        nPlotShaded(labelId, xs, ys, yRef);
    }

    public static void plotShaded(String labelId, short[] xs, short[] ys, double yRef, int flags) {
        nPlotShaded(labelId, xs, ys, yRef, flags);
    }

    public static void plotShaded(String labelId, short[] xs, short[] ys, double yRef, int flags, int offset) {
        nPlotShaded(labelId, xs, ys, yRef, flags, offset);
    }

    public static void plotShaded(String labelId, int[] xs, int[] ys) {
        nPlotShaded(labelId, xs, ys);
    }

    public static void plotShaded(String labelId, int[] xs, int[] ys, double yRef) {
        nPlotShaded(labelId, xs, ys, yRef);
    }

    public static void plotShaded(String labelId, int[] xs, int[] ys, double yRef, int flags) {
        nPlotShaded(labelId, xs, ys, yRef, flags);
    }

    public static void plotShaded(String labelId, int[] xs, int[] ys, double yRef, int flags, int offset) {
        nPlotShaded(labelId, xs, ys, yRef, flags, offset);
    }

    public static void plotShaded(String labelId, long[] xs, long[] ys) {
        nPlotShaded(labelId, xs, ys);
    }

    public static void plotShaded(String labelId, long[] xs, long[] ys, double yRef) {
        nPlotShaded(labelId, xs, ys, yRef);
    }

    public static void plotShaded(String labelId, long[] xs, long[] ys, double yRef, int flags) {
        nPlotShaded(labelId, xs, ys, yRef, flags);
    }

    public static void plotShaded(String labelId, long[] xs, long[] ys, double yRef, int flags, int offset) {
        nPlotShaded(labelId, xs, ys, yRef, flags, offset);
    }

    public static void plotShaded(String labelId, float[] xs, float[] ys) {
        nPlotShaded(labelId, xs, ys);
    }

    public static void plotShaded(String labelId, float[] xs, float[] ys, double yRef) {
        nPlotShaded(labelId, xs, ys, yRef);
    }

    public static void plotShaded(String labelId, float[] xs, float[] ys, double yRef, int flags) {
        nPlotShaded(labelId, xs, ys, yRef, flags);
    }

    public static void plotShaded(String labelId, float[] xs, float[] ys, double yRef, int flags, int offset) {
        nPlotShaded(labelId, xs, ys, yRef, flags, offset);
    }

    public static void plotShaded(String labelId, double[] xs, double[] ys) {
        nPlotShaded(labelId, xs, ys);
    }

    public static void plotShaded(String labelId, double[] xs, double[] ys, double yRef) {
        nPlotShaded(labelId, xs, ys, yRef);
    }

    public static void plotShaded(String labelId, double[] xs, double[] ys, double yRef, int flags) {
        nPlotShaded(labelId, xs, ys, yRef, flags);
    }

    public static void plotShaded(String labelId, double[] xs, double[] ys, double yRef, int flags, int offset) {
        nPlotShaded(labelId, xs, ys, yRef, flags, offset);
    }

    public static void plotShadedV(String labelId, short[] xs, short[] ys, int count) {
        nPlotShadedV(labelId, xs, ys, count);
    }

    public static void plotShadedV(String labelId, short[] xs, short[] ys, int count, double yRef) {
        nPlotShadedV(labelId, xs, ys, count, yRef);
    }

    public static void plotShadedV(String labelId, short[] xs, short[] ys, int count, double yRef, int flags) {
        nPlotShadedV(labelId, xs, ys, count, yRef, flags);
    }

    public static void plotShadedV(String labelId, short[] xs, short[] ys, int count, double yRef, int flags, int offset) {
        nPlotShadedV(labelId, xs, ys, count, yRef, flags, offset);
    }

    public static void plotShadedV(String labelId, int[] xs, int[] ys, int count) {
        nPlotShadedV(labelId, xs, ys, count);
    }

    public static void plotShadedV(String labelId, int[] xs, int[] ys, int count, double yRef) {
        nPlotShadedV(labelId, xs, ys, count, yRef);
    }

    public static void plotShadedV(String labelId, int[] xs, int[] ys, int count, double yRef, int flags) {
        nPlotShadedV(labelId, xs, ys, count, yRef, flags);
    }

    public static void plotShadedV(String labelId, int[] xs, int[] ys, int count, double yRef, int flags, int offset) {
        nPlotShadedV(labelId, xs, ys, count, yRef, flags, offset);
    }

    public static void plotShadedV(String labelId, long[] xs, long[] ys, int count) {
        nPlotShadedV(labelId, xs, ys, count);
    }

    public static void plotShadedV(String labelId, long[] xs, long[] ys, int count, double yRef) {
        nPlotShadedV(labelId, xs, ys, count, yRef);
    }

    public static void plotShadedV(String labelId, long[] xs, long[] ys, int count, double yRef, int flags) {
        nPlotShadedV(labelId, xs, ys, count, yRef, flags);
    }

    public static void plotShadedV(String labelId, long[] xs, long[] ys, int count, double yRef, int flags, int offset) {
        nPlotShadedV(labelId, xs, ys, count, yRef, flags, offset);
    }

    public static void plotShadedV(String labelId, float[] xs, float[] ys, int count) {
        nPlotShadedV(labelId, xs, ys, count);
    }

    public static void plotShadedV(String labelId, float[] xs, float[] ys, int count, double yRef) {
        nPlotShadedV(labelId, xs, ys, count, yRef);
    }

    public static void plotShadedV(String labelId, float[] xs, float[] ys, int count, double yRef, int flags) {
        nPlotShadedV(labelId, xs, ys, count, yRef, flags);
    }

    public static void plotShadedV(String labelId, float[] xs, float[] ys, int count, double yRef, int flags, int offset) {
        nPlotShadedV(labelId, xs, ys, count, yRef, flags, offset);
    }

    public static void plotShadedV(String labelId, double[] xs, double[] ys, int count) {
        nPlotShadedV(labelId, xs, ys, count);
    }

    public static void plotShadedV(String labelId, double[] xs, double[] ys, int count, double yRef) {
        nPlotShadedV(labelId, xs, ys, count, yRef);
    }

    public static void plotShadedV(String labelId, double[] xs, double[] ys, int count, double yRef, int flags) {
        nPlotShadedV(labelId, xs, ys, count, yRef, flags);
    }

    public static void plotShadedV(String labelId, double[] xs, double[] ys, int count, double yRef, int flags, int offset) {
        nPlotShadedV(labelId, xs, ys, count, yRef, flags, offset);
    }

    public static void plotShaded(String labelId, short[] xs, short[] ys1, short[] ys2) {
        nPlotShaded(labelId, xs, ys1, ys2);
    }

    public static void plotShaded(String labelId, short[] xs, short[] ys1, short[] ys2, int flags) {
        nPlotShaded(labelId, xs, ys1, ys2, flags);
    }

    public static void plotShaded(String labelId, int[] xs, int[] ys1, int[] ys2) {
        nPlotShaded(labelId, xs, ys1, ys2);
    }

    public static void plotShaded(String labelId, int[] xs, int[] ys1, int[] ys2, int flags) {
        nPlotShaded(labelId, xs, ys1, ys2, flags);
    }

    public static void plotShaded(String labelId, long[] xs, long[] ys1, long[] ys2) {
        nPlotShaded(labelId, xs, ys1, ys2);
    }

    public static void plotShaded(String labelId, long[] xs, long[] ys1, long[] ys2, int flags) {
        nPlotShaded(labelId, xs, ys1, ys2, flags);
    }

    public static void plotShaded(String labelId, float[] xs, float[] ys1, float[] ys2) {
        nPlotShaded(labelId, xs, ys1, ys2);
    }

    public static void plotShaded(String labelId, float[] xs, float[] ys1, float[] ys2, int flags) {
        nPlotShaded(labelId, xs, ys1, ys2, flags);
    }

    public static void plotShaded(String labelId, double[] xs, double[] ys1, double[] ys2) {
        nPlotShaded(labelId, xs, ys1, ys2);
    }

    public static void plotShaded(String labelId, double[] xs, double[] ys1, double[] ys2, int flags) {
        nPlotShaded(labelId, xs, ys1, ys2, flags);
    }

    public static void plotShadedV(String labelId, short[] xs, short[] ys1, short[] ys2, int count) {
        nPlotShadedV(labelId, xs, ys1, ys2, count);
    }

    public static void plotShadedV(String labelId, short[] xs, short[] ys1, short[] ys2, int count, int flags) {
        nPlotShadedV(labelId, xs, ys1, ys2, count, flags);
    }

    public static void plotShadedV(String labelId, short[] xs, short[] ys1, short[] ys2, int count, int flags, int offset) {
        nPlotShadedV(labelId, xs, ys1, ys2, count, flags, offset);
    }

    public static void plotShadedV(String labelId, int[] xs, int[] ys1, int[] ys2, int count) {
        nPlotShadedV(labelId, xs, ys1, ys2, count);
    }

    public static void plotShadedV(String labelId, int[] xs, int[] ys1, int[] ys2, int count, int flags) {
        nPlotShadedV(labelId, xs, ys1, ys2, count, flags);
    }

    public static void plotShadedV(String labelId, int[] xs, int[] ys1, int[] ys2, int count, int flags, int offset) {
        nPlotShadedV(labelId, xs, ys1, ys2, count, flags, offset);
    }

    public static void plotShadedV(String labelId, long[] xs, long[] ys1, long[] ys2, int count) {
        nPlotShadedV(labelId, xs, ys1, ys2, count);
    }

    public static void plotShadedV(String labelId, long[] xs, long[] ys1, long[] ys2, int count, int flags) {
        nPlotShadedV(labelId, xs, ys1, ys2, count, flags);
    }

    public static void plotShadedV(String labelId, long[] xs, long[] ys1, long[] ys2, int count, int flags, int offset) {
        nPlotShadedV(labelId, xs, ys1, ys2, count, flags, offset);
    }

    public static void plotShadedV(String labelId, float[] xs, float[] ys1, float[] ys2, int count) {
        nPlotShadedV(labelId, xs, ys1, ys2, count);
    }

    public static void plotShadedV(String labelId, float[] xs, float[] ys1, float[] ys2, int count, int flags) {
        nPlotShadedV(labelId, xs, ys1, ys2, count, flags);
    }

    public static void plotShadedV(String labelId, float[] xs, float[] ys1, float[] ys2, int count, int flags, int offset) {
        nPlotShadedV(labelId, xs, ys1, ys2, count, flags, offset);
    }

    public static void plotShadedV(String labelId, double[] xs, double[] ys1, double[] ys2, int count) {
        nPlotShadedV(labelId, xs, ys1, ys2, count);
    }

    public static void plotShadedV(String labelId, double[] xs, double[] ys1, double[] ys2, int count, int flags) {
        nPlotShadedV(labelId, xs, ys1, ys2, count, flags);
    }

    public static void plotShadedV(String labelId, double[] xs, double[] ys1, double[] ys2, int count, int flags, int offset) {
        nPlotShadedV(labelId, xs, ys1, ys2, count, flags, offset);
    }

    public static void plotBars(String labelId, short[] values) {
        nPlotBars(labelId, values);
    }

    public static void plotBars(String labelId, short[] values, double barWidth) {
        nPlotBars(labelId, values, barWidth);
    }

    public static void plotBars(String labelId, short[] values, double barWidth, double xstart) {
        nPlotBars(labelId, values, barWidth, xstart);
    }

    public static void plotBars(String labelId, short[] values, double barWidth, double xstart, int flags) {
        nPlotBars(labelId, values, barWidth, xstart, flags);
    }

    public static void plotBars(String labelId, short[] values, double barWidth, double xstart, int flags, int offset) {
        nPlotBars(labelId, values, barWidth, xstart, flags, offset);
    }

    public static void plotBars(String labelId, int[] values) {
        nPlotBars(labelId, values);
    }

    public static void plotBars(String labelId, int[] values, double barWidth) {
        nPlotBars(labelId, values, barWidth);
    }

    public static void plotBars(String labelId, int[] values, double barWidth, double xstart) {
        nPlotBars(labelId, values, barWidth, xstart);
    }

    public static void plotBars(String labelId, int[] values, double barWidth, double xstart, int flags) {
        nPlotBars(labelId, values, barWidth, xstart, flags);
    }

    public static void plotBars(String labelId, int[] values, double barWidth, double xstart, int flags, int offset) {
        nPlotBars(labelId, values, barWidth, xstart, flags, offset);
    }

    public static void plotBars(String labelId, long[] values) {
        nPlotBars(labelId, values);
    }

    public static void plotBars(String labelId, long[] values, double barWidth) {
        nPlotBars(labelId, values, barWidth);
    }

    public static void plotBars(String labelId, long[] values, double barWidth, double xstart) {
        nPlotBars(labelId, values, barWidth, xstart);
    }

    public static void plotBars(String labelId, long[] values, double barWidth, double xstart, int flags) {
        nPlotBars(labelId, values, barWidth, xstart, flags);
    }

    public static void plotBars(String labelId, long[] values, double barWidth, double xstart, int flags, int offset) {
        nPlotBars(labelId, values, barWidth, xstart, flags, offset);
    }

    public static void plotBars(String labelId, float[] values) {
        nPlotBars(labelId, values);
    }

    public static void plotBars(String labelId, float[] values, double barWidth) {
        nPlotBars(labelId, values, barWidth);
    }

    public static void plotBars(String labelId, float[] values, double barWidth, double xstart) {
        nPlotBars(labelId, values, barWidth, xstart);
    }

    public static void plotBars(String labelId, float[] values, double barWidth, double xstart, int flags) {
        nPlotBars(labelId, values, barWidth, xstart, flags);
    }

    public static void plotBars(String labelId, float[] values, double barWidth, double xstart, int flags, int offset) {
        nPlotBars(labelId, values, barWidth, xstart, flags, offset);
    }

    public static void plotBars(String labelId, double[] values) {
        nPlotBars(labelId, values);
    }

    public static void plotBars(String labelId, double[] values, double barWidth) {
        nPlotBars(labelId, values, barWidth);
    }

    public static void plotBars(String labelId, double[] values, double barWidth, double xstart) {
        nPlotBars(labelId, values, barWidth, xstart);
    }

    public static void plotBars(String labelId, double[] values, double barWidth, double xstart, int flags) {
        nPlotBars(labelId, values, barWidth, xstart, flags);
    }

    public static void plotBars(String labelId, double[] values, double barWidth, double xstart, int flags, int offset) {
        nPlotBars(labelId, values, barWidth, xstart, flags, offset);
    }

    public static void plotBarsV(String labelId, short[] values, int count) {
        nPlotBarsV(labelId, values, count);
    }

    public static void plotBarsV(String labelId, short[] values, int count, double barWidth) {
        nPlotBarsV(labelId, values, count, barWidth);
    }

    public static void plotBarsV(String labelId, short[] values, int count, double barWidth, double xstart) {
        nPlotBarsV(labelId, values, count, barWidth, xstart);
    }

    public static void plotBarsV(String labelId, short[] values, int count, double barWidth, double xstart, int flags) {
        nPlotBarsV(labelId, values, count, barWidth, xstart, flags);
    }

    public static void plotBarsV(String labelId, short[] values, int count, double barWidth, double xstart, int flags, int offset) {
        nPlotBarsV(labelId, values, count, barWidth, xstart, flags, offset);
    }

    public static void plotBarsV(String labelId, int[] values, int count) {
        nPlotBarsV(labelId, values, count);
    }

    public static void plotBarsV(String labelId, int[] values, int count, double barWidth) {
        nPlotBarsV(labelId, values, count, barWidth);
    }

    public static void plotBarsV(String labelId, int[] values, int count, double barWidth, double xstart) {
        nPlotBarsV(labelId, values, count, barWidth, xstart);
    }

    public static void plotBarsV(String labelId, int[] values, int count, double barWidth, double xstart, int flags) {
        nPlotBarsV(labelId, values, count, barWidth, xstart, flags);
    }

    public static void plotBarsV(String labelId, int[] values, int count, double barWidth, double xstart, int flags, int offset) {
        nPlotBarsV(labelId, values, count, barWidth, xstart, flags, offset);
    }

    public static void plotBarsV(String labelId, long[] values, int count) {
        nPlotBarsV(labelId, values, count);
    }

    public static void plotBarsV(String labelId, long[] values, int count, double barWidth) {
        nPlotBarsV(labelId, values, count, barWidth);
    }

    public static void plotBarsV(String labelId, long[] values, int count, double barWidth, double xstart) {
        nPlotBarsV(labelId, values, count, barWidth, xstart);
    }

    public static void plotBarsV(String labelId, long[] values, int count, double barWidth, double xstart, int flags) {
        nPlotBarsV(labelId, values, count, barWidth, xstart, flags);
    }

    public static void plotBarsV(String labelId, long[] values, int count, double barWidth, double xstart, int flags, int offset) {
        nPlotBarsV(labelId, values, count, barWidth, xstart, flags, offset);
    }

    public static void plotBarsV(String labelId, float[] values, int count) {
        nPlotBarsV(labelId, values, count);
    }

    public static void plotBarsV(String labelId, float[] values, int count, double barWidth) {
        nPlotBarsV(labelId, values, count, barWidth);
    }

    public static void plotBarsV(String labelId, float[] values, int count, double barWidth, double xstart) {
        nPlotBarsV(labelId, values, count, barWidth, xstart);
    }

    public static void plotBarsV(String labelId, float[] values, int count, double barWidth, double xstart, int flags) {
        nPlotBarsV(labelId, values, count, barWidth, xstart, flags);
    }

    public static void plotBarsV(String labelId, float[] values, int count, double barWidth, double xstart, int flags, int offset) {
        nPlotBarsV(labelId, values, count, barWidth, xstart, flags, offset);
    }

    public static void plotBarsV(String labelId, double[] values, int count) {
        nPlotBarsV(labelId, values, count);
    }

    public static void plotBarsV(String labelId, double[] values, int count, double barWidth) {
        nPlotBarsV(labelId, values, count, barWidth);
    }

    public static void plotBarsV(String labelId, double[] values, int count, double barWidth, double xstart) {
        nPlotBarsV(labelId, values, count, barWidth, xstart);
    }

    public static void plotBarsV(String labelId, double[] values, int count, double barWidth, double xstart, int flags) {
        nPlotBarsV(labelId, values, count, barWidth, xstart, flags);
    }

    public static void plotBarsV(String labelId, double[] values, int count, double barWidth, double xstart, int flags, int offset) {
        nPlotBarsV(labelId, values, count, barWidth, xstart, flags, offset);
    }

    public static void plotBars(String labelId, short[] xs, short[] ys) {
        nPlotBars(labelId, xs, ys);
    }

    public static void plotBars(String labelId, short[] xs, short[] ys, int flags) {
        nPlotBars(labelId, xs, ys, flags);
    }

    public static void plotBars(String labelId, short[] xs, short[] ys, int flags, int offset) {
        nPlotBars(labelId, xs, ys, flags, offset);
    }

    public static void plotBars(String labelId, int[] xs, int[] ys) {
        nPlotBars(labelId, xs, ys);
    }

    public static void plotBars(String labelId, int[] xs, int[] ys, int flags) {
        nPlotBars(labelId, xs, ys, flags);
    }

    public static void plotBars(String labelId, int[] xs, int[] ys, int flags, int offset) {
        nPlotBars(labelId, xs, ys, flags, offset);
    }

    public static void plotBars(String labelId, long[] xs, long[] ys) {
        nPlotBars(labelId, xs, ys);
    }

    public static void plotBars(String labelId, long[] xs, long[] ys, int flags) {
        nPlotBars(labelId, xs, ys, flags);
    }

    public static void plotBars(String labelId, long[] xs, long[] ys, int flags, int offset) {
        nPlotBars(labelId, xs, ys, flags, offset);
    }

    public static void plotBars(String labelId, float[] xs, float[] ys) {
        nPlotBars(labelId, xs, ys);
    }

    public static void plotBars(String labelId, float[] xs, float[] ys, int flags) {
        nPlotBars(labelId, xs, ys, flags);
    }

    public static void plotBars(String labelId, float[] xs, float[] ys, int flags, int offset) {
        nPlotBars(labelId, xs, ys, flags, offset);
    }

    public static void plotBars(String labelId, double[] xs, double[] ys) {
        nPlotBars(labelId, xs, ys);
    }

    public static void plotBars(String labelId, double[] xs, double[] ys, int flags) {
        nPlotBars(labelId, xs, ys, flags);
    }

    public static void plotBars(String labelId, double[] xs, double[] ys, int flags, int offset) {
        nPlotBars(labelId, xs, ys, flags, offset);
    }

    public static void plotBarsV(String labelId, short[] xs, short[] ys, double barWidth) {
        nPlotBarsV(labelId, xs, ys, barWidth);
    }

    public static void plotBarsV(String labelId, short[] xs, short[] ys, double barWidth, int flags) {
        nPlotBarsV(labelId, xs, ys, barWidth, flags);
    }

    public static void plotBarsV(String labelId, short[] xs, short[] ys, double barWidth, int flags, int offset) {
        nPlotBarsV(labelId, xs, ys, barWidth, flags, offset);
    }

    public static void plotBarsV(String labelId, int[] xs, int[] ys, double barWidth) {
        nPlotBarsV(labelId, xs, ys, barWidth);
    }

    public static void plotBarsV(String labelId, int[] xs, int[] ys, double barWidth, int flags) {
        nPlotBarsV(labelId, xs, ys, barWidth, flags);
    }

    public static void plotBarsV(String labelId, int[] xs, int[] ys, double barWidth, int flags, int offset) {
        nPlotBarsV(labelId, xs, ys, barWidth, flags, offset);
    }

    public static void plotBarsV(String labelId, long[] xs, long[] ys, double barWidth) {
        nPlotBarsV(labelId, xs, ys, barWidth);
    }

    public static void plotBarsV(String labelId, long[] xs, long[] ys, double barWidth, int flags) {
        nPlotBarsV(labelId, xs, ys, barWidth, flags);
    }

    public static void plotBarsV(String labelId, long[] xs, long[] ys, double barWidth, int flags, int offset) {
        nPlotBarsV(labelId, xs, ys, barWidth, flags, offset);
    }

    public static void plotBarsV(String labelId, float[] xs, float[] ys, double barWidth) {
        nPlotBarsV(labelId, xs, ys, barWidth);
    }

    public static void plotBarsV(String labelId, float[] xs, float[] ys, double barWidth, int flags) {
        nPlotBarsV(labelId, xs, ys, barWidth, flags);
    }

    public static void plotBarsV(String labelId, float[] xs, float[] ys, double barWidth, int flags, int offset) {
        nPlotBarsV(labelId, xs, ys, barWidth, flags, offset);
    }

    public static void plotBarsV(String labelId, double[] xs, double[] ys, double barWidth) {
        nPlotBarsV(labelId, xs, ys, barWidth);
    }

    public static void plotBarsV(String labelId, double[] xs, double[] ys, double barWidth, int flags) {
        nPlotBarsV(labelId, xs, ys, barWidth, flags);
    }

    public static void plotBarsV(String labelId, double[] xs, double[] ys, double barWidth, int flags, int offset) {
        nPlotBarsV(labelId, xs, ys, barWidth, flags, offset);
    }

    public static void plotBarsV(String labelId, short[] xs, short[] ys, int count, double barWidth) {
        nPlotBarsV(labelId, xs, ys, count, barWidth);
    }

    public static void plotBarsV(String labelId, short[] xs, short[] ys, int count, double barWidth, int flags) {
        nPlotBarsV(labelId, xs, ys, count, barWidth, flags);
    }

    public static void plotBarsV(String labelId, short[] xs, short[] ys, int count, double barWidth, int flags, int offset) {
        nPlotBarsV(labelId, xs, ys, count, barWidth, flags, offset);
    }

    public static void plotBarsV(String labelId, int[] xs, int[] ys, int count, double barWidth) {
        nPlotBarsV(labelId, xs, ys, count, barWidth);
    }

    public static void plotBarsV(String labelId, int[] xs, int[] ys, int count, double barWidth, int flags) {
        nPlotBarsV(labelId, xs, ys, count, barWidth, flags);
    }

    public static void plotBarsV(String labelId, int[] xs, int[] ys, int count, double barWidth, int flags, int offset) {
        nPlotBarsV(labelId, xs, ys, count, barWidth, flags, offset);
    }

    public static void plotBarsV(String labelId, long[] xs, long[] ys, int count, double barWidth) {
        nPlotBarsV(labelId, xs, ys, count, barWidth);
    }

    public static void plotBarsV(String labelId, long[] xs, long[] ys, int count, double barWidth, int flags) {
        nPlotBarsV(labelId, xs, ys, count, barWidth, flags);
    }

    public static void plotBarsV(String labelId, long[] xs, long[] ys, int count, double barWidth, int flags, int offset) {
        nPlotBarsV(labelId, xs, ys, count, barWidth, flags, offset);
    }

    public static void plotBarsV(String labelId, float[] xs, float[] ys, int count, double barWidth) {
        nPlotBarsV(labelId, xs, ys, count, barWidth);
    }

    public static void plotBarsV(String labelId, float[] xs, float[] ys, int count, double barWidth, int flags) {
        nPlotBarsV(labelId, xs, ys, count, barWidth, flags);
    }

    public static void plotBarsV(String labelId, float[] xs, float[] ys, int count, double barWidth, int flags, int offset) {
        nPlotBarsV(labelId, xs, ys, count, barWidth, flags, offset);
    }

    public static void plotBarsV(String labelId, double[] xs, double[] ys, int count, double barWidth) {
        nPlotBarsV(labelId, xs, ys, count, barWidth);
    }

    public static void plotBarsV(String labelId, double[] xs, double[] ys, int count, double barWidth, int flags) {
        nPlotBarsV(labelId, xs, ys, count, barWidth, flags);
    }

    public static void plotBarsV(String labelId, double[] xs, double[] ys, int count, double barWidth, int flags, int offset) {
        nPlotBarsV(labelId, xs, ys, count, barWidth, flags, offset);
    }

    public static void plotBarGroups(String[] labelIds, short[] values, int groupCount) {
        nPlotBarGroups(labelIds, labelIds.length, values, groupCount);
    }

    public static void plotBarGroups(String[] labelIds, short[] values, int groupCount, double groupSize) {
        nPlotBarGroups(labelIds, labelIds.length, values, groupCount, groupSize);
    }

    public static void plotBarGroups(String[] labelIds, short[] values, int groupCount, double groupSize, double shift) {
        nPlotBarGroups(labelIds, labelIds.length, values, groupCount, groupSize, shift);
    }

    public static void plotBarGroups(String[] labelIds, short[] values, int groupCount, double groupSize, double shift, int flags) {
        nPlotBarGroups(labelIds, labelIds.length, values, groupCount, groupSize, shift, flags);
    }

    public static void plotBarGroups(String[] labelIds, int[] values, int groupCount) {
        nPlotBarGroups(labelIds, labelIds.length, values, groupCount);
    }

    public static void plotBarGroups(String[] labelIds, int[] values, int groupCount, double groupSize) {
        nPlotBarGroups(labelIds, labelIds.length, values, groupCount, groupSize);
    }

    public static void plotBarGroups(String[] labelIds, int[] values, int groupCount, double groupSize, double shift) {
        nPlotBarGroups(labelIds, labelIds.length, values, groupCount, groupSize, shift);
    }

    public static void plotBarGroups(String[] labelIds, int[] values, int groupCount, double groupSize, double shift, int flags) {
        nPlotBarGroups(labelIds, labelIds.length, values, groupCount, groupSize, shift, flags);
    }

    public static void plotBarGroups(String[] labelIds, long[] values, int groupCount) {
        nPlotBarGroups(labelIds, labelIds.length, values, groupCount);
    }

    public static void plotBarGroups(String[] labelIds, long[] values, int groupCount, double groupSize) {
        nPlotBarGroups(labelIds, labelIds.length, values, groupCount, groupSize);
    }

    public static void plotBarGroups(String[] labelIds, long[] values, int groupCount, double groupSize, double shift) {
        nPlotBarGroups(labelIds, labelIds.length, values, groupCount, groupSize, shift);
    }

    public static void plotBarGroups(String[] labelIds, long[] values, int groupCount, double groupSize, double shift, int flags) {
        nPlotBarGroups(labelIds, labelIds.length, values, groupCount, groupSize, shift, flags);
    }

    public static void plotBarGroups(String[] labelIds, float[] values, int groupCount) {
        nPlotBarGroups(labelIds, labelIds.length, values, groupCount);
    }

    public static void plotBarGroups(String[] labelIds, float[] values, int groupCount, double groupSize) {
        nPlotBarGroups(labelIds, labelIds.length, values, groupCount, groupSize);
    }

    public static void plotBarGroups(String[] labelIds, float[] values, int groupCount, double groupSize, double shift) {
        nPlotBarGroups(labelIds, labelIds.length, values, groupCount, groupSize, shift);
    }

    public static void plotBarGroups(String[] labelIds, float[] values, int groupCount, double groupSize, double shift, int flags) {
        nPlotBarGroups(labelIds, labelIds.length, values, groupCount, groupSize, shift, flags);
    }

    public static void plotBarGroups(String[] labelIds, double[] values, int groupCount) {
        nPlotBarGroups(labelIds, labelIds.length, values, groupCount);
    }

    public static void plotBarGroups(String[] labelIds, double[] values, int groupCount, double groupSize) {
        nPlotBarGroups(labelIds, labelIds.length, values, groupCount, groupSize);
    }

    public static void plotBarGroups(String[] labelIds, double[] values, int groupCount, double groupSize, double shift) {
        nPlotBarGroups(labelIds, labelIds.length, values, groupCount, groupSize, shift);
    }

    public static void plotBarGroups(String[] labelIds, double[] values, int groupCount, double groupSize, double shift, int flags) {
        nPlotBarGroups(labelIds, labelIds.length, values, groupCount, groupSize, shift, flags);
    }

    public static void plotBarGroupsV(String[] labelIds, short[] values, int itemCount, int groupCount) {
        nPlotBarGroupsV(labelIds, labelIds.length, values, itemCount, groupCount);
    }

    public static void plotBarGroupsV(String[] labelIds, short[] values, int itemCount, int groupCount, double groupSize) {
        nPlotBarGroupsV(labelIds, labelIds.length, values, itemCount, groupCount, groupSize);
    }

    public static void plotBarGroupsV(String[] labelIds, short[] values, int itemCount, int groupCount, double groupSize, double shift) {
        nPlotBarGroupsV(labelIds, labelIds.length, values, itemCount, groupCount, groupSize, shift);
    }

    public static void plotBarGroupsV(String[] labelIds, short[] values, int itemCount, int groupCount, double groupSize, double shift, int flags) {
        nPlotBarGroupsV(labelIds, labelIds.length, values, itemCount, groupCount, groupSize, shift, flags);
    }

    public static void plotBarGroupsV(String[] labelIds, int[] values, int itemCount, int groupCount) {
        nPlotBarGroupsV(labelIds, labelIds.length, values, itemCount, groupCount);
    }

    public static void plotBarGroupsV(String[] labelIds, int[] values, int itemCount, int groupCount, double groupSize) {
        nPlotBarGroupsV(labelIds, labelIds.length, values, itemCount, groupCount, groupSize);
    }

    public static void plotBarGroupsV(String[] labelIds, int[] values, int itemCount, int groupCount, double groupSize, double shift) {
        nPlotBarGroupsV(labelIds, labelIds.length, values, itemCount, groupCount, groupSize, shift);
    }

    public static void plotBarGroupsV(String[] labelIds, int[] values, int itemCount, int groupCount, double groupSize, double shift, int flags) {
        nPlotBarGroupsV(labelIds, labelIds.length, values, itemCount, groupCount, groupSize, shift, flags);
    }

    public static void plotBarGroupsV(String[] labelIds, long[] values, int itemCount, int groupCount) {
        nPlotBarGroupsV(labelIds, labelIds.length, values, itemCount, groupCount);
    }

    public static void plotBarGroupsV(String[] labelIds, long[] values, int itemCount, int groupCount, double groupSize) {
        nPlotBarGroupsV(labelIds, labelIds.length, values, itemCount, groupCount, groupSize);
    }

    public static void plotBarGroupsV(String[] labelIds, long[] values, int itemCount, int groupCount, double groupSize, double shift) {
        nPlotBarGroupsV(labelIds, labelIds.length, values, itemCount, groupCount, groupSize, shift);
    }

    public static void plotBarGroupsV(String[] labelIds, long[] values, int itemCount, int groupCount, double groupSize, double shift, int flags) {
        nPlotBarGroupsV(labelIds, labelIds.length, values, itemCount, groupCount, groupSize, shift, flags);
    }

    public static void plotBarGroupsV(String[] labelIds, float[] values, int itemCount, int groupCount) {
        nPlotBarGroupsV(labelIds, labelIds.length, values, itemCount, groupCount);
    }

    public static void plotBarGroupsV(String[] labelIds, float[] values, int itemCount, int groupCount, double groupSize) {
        nPlotBarGroupsV(labelIds, labelIds.length, values, itemCount, groupCount, groupSize);
    }

    public static void plotBarGroupsV(String[] labelIds, float[] values, int itemCount, int groupCount, double groupSize, double shift) {
        nPlotBarGroupsV(labelIds, labelIds.length, values, itemCount, groupCount, groupSize, shift);
    }

    public static void plotBarGroupsV(String[] labelIds, float[] values, int itemCount, int groupCount, double groupSize, double shift, int flags) {
        nPlotBarGroupsV(labelIds, labelIds.length, values, itemCount, groupCount, groupSize, shift, flags);
    }

    public static void plotBarGroupsV(String[] labelIds, double[] values, int itemCount, int groupCount) {
        nPlotBarGroupsV(labelIds, labelIds.length, values, itemCount, groupCount);
    }

    public static void plotBarGroupsV(String[] labelIds, double[] values, int itemCount, int groupCount, double groupSize) {
        nPlotBarGroupsV(labelIds, labelIds.length, values, itemCount, groupCount, groupSize);
    }

    public static void plotBarGroupsV(String[] labelIds, double[] values, int itemCount, int groupCount, double groupSize, double shift) {
        nPlotBarGroupsV(labelIds, labelIds.length, values, itemCount, groupCount, groupSize, shift);
    }

    public static void plotBarGroupsV(String[] labelIds, double[] values, int itemCount, int groupCount, double groupSize, double shift, int flags) {
        nPlotBarGroupsV(labelIds, labelIds.length, values, itemCount, groupCount, groupSize, shift, flags);
    }

    public static void plotErrorBars(String labelId, short[] xs, short[] ys, short[] err) {
        nPlotErrorBars(labelId, xs, ys, err);
    }

    public static void plotErrorBars(String labelId, short[] xs, short[] ys, short[] err, int flags) {
        nPlotErrorBars(labelId, xs, ys, err, flags);
    }

    public static void plotErrorBars(String labelId, short[] xs, short[] ys, short[] err, int flags, int offset) {
        nPlotErrorBars(labelId, xs, ys, err, flags, offset);
    }

    public static void plotErrorBars(String labelId, int[] xs, int[] ys, int[] err) {
        nPlotErrorBars(labelId, xs, ys, err);
    }

    public static void plotErrorBars(String labelId, int[] xs, int[] ys, int[] err, int flags) {
        nPlotErrorBars(labelId, xs, ys, err, flags);
    }

    public static void plotErrorBars(String labelId, int[] xs, int[] ys, int[] err, int flags, int offset) {
        nPlotErrorBars(labelId, xs, ys, err, flags, offset);
    }

    public static void plotErrorBars(String labelId, long[] xs, long[] ys, long[] err) {
        nPlotErrorBars(labelId, xs, ys, err);
    }

    public static void plotErrorBars(String labelId, long[] xs, long[] ys, long[] err, int flags) {
        nPlotErrorBars(labelId, xs, ys, err, flags);
    }

    public static void plotErrorBars(String labelId, long[] xs, long[] ys, long[] err, int flags, int offset) {
        nPlotErrorBars(labelId, xs, ys, err, flags, offset);
    }

    public static void plotErrorBars(String labelId, float[] xs, float[] ys, float[] err) {
        nPlotErrorBars(labelId, xs, ys, err);
    }

    public static void plotErrorBars(String labelId, float[] xs, float[] ys, float[] err, int flags) {
        nPlotErrorBars(labelId, xs, ys, err, flags);
    }

    public static void plotErrorBars(String labelId, float[] xs, float[] ys, float[] err, int flags, int offset) {
        nPlotErrorBars(labelId, xs, ys, err, flags, offset);
    }

    public static void plotErrorBars(String labelId, double[] xs, double[] ys, double[] err) {
        nPlotErrorBars(labelId, xs, ys, err);
    }

    public static void plotErrorBars(String labelId, double[] xs, double[] ys, double[] err, int flags) {
        nPlotErrorBars(labelId, xs, ys, err, flags);
    }

    public static void plotErrorBars(String labelId, double[] xs, double[] ys, double[] err, int flags, int offset) {
        nPlotErrorBars(labelId, xs, ys, err, flags, offset);
    }

    public static void plotErrorBarsV(String labelId, short[] xs, short[] ys, short[] err, int count) {
        nPlotErrorBarsV(labelId, xs, ys, err, count);
    }

    public static void plotErrorBarsV(String labelId, short[] xs, short[] ys, short[] err, int count, int flags) {
        nPlotErrorBarsV(labelId, xs, ys, err, count, flags);
    }

    public static void plotErrorBarsV(String labelId, short[] xs, short[] ys, short[] err, int count, int flags, int offset) {
        nPlotErrorBarsV(labelId, xs, ys, err, count, flags, offset);
    }

    public static void plotErrorBarsV(String labelId, int[] xs, int[] ys, int[] err, int count) {
        nPlotErrorBarsV(labelId, xs, ys, err, count);
    }

    public static void plotErrorBarsV(String labelId, int[] xs, int[] ys, int[] err, int count, int flags) {
        nPlotErrorBarsV(labelId, xs, ys, err, count, flags);
    }

    public static void plotErrorBarsV(String labelId, int[] xs, int[] ys, int[] err, int count, int flags, int offset) {
        nPlotErrorBarsV(labelId, xs, ys, err, count, flags, offset);
    }

    public static void plotErrorBarsV(String labelId, long[] xs, long[] ys, long[] err, int count) {
        nPlotErrorBarsV(labelId, xs, ys, err, count);
    }

    public static void plotErrorBarsV(String labelId, long[] xs, long[] ys, long[] err, int count, int flags) {
        nPlotErrorBarsV(labelId, xs, ys, err, count, flags);
    }

    public static void plotErrorBarsV(String labelId, long[] xs, long[] ys, long[] err, int count, int flags, int offset) {
        nPlotErrorBarsV(labelId, xs, ys, err, count, flags, offset);
    }

    public static void plotErrorBarsV(String labelId, float[] xs, float[] ys, float[] err, int count) {
        nPlotErrorBarsV(labelId, xs, ys, err, count);
    }

    public static void plotErrorBarsV(String labelId, float[] xs, float[] ys, float[] err, int count, int flags) {
        nPlotErrorBarsV(labelId, xs, ys, err, count, flags);
    }

    public static void plotErrorBarsV(String labelId, float[] xs, float[] ys, float[] err, int count, int flags, int offset) {
        nPlotErrorBarsV(labelId, xs, ys, err, count, flags, offset);
    }

    public static void plotErrorBarsV(String labelId, double[] xs, double[] ys, double[] err, int count) {
        nPlotErrorBarsV(labelId, xs, ys, err, count);
    }

    public static void plotErrorBarsV(String labelId, double[] xs, double[] ys, double[] err, int count, int flags) {
        nPlotErrorBarsV(labelId, xs, ys, err, count, flags);
    }

    public static void plotErrorBarsV(String labelId, double[] xs, double[] ys, double[] err, int count, int flags, int offset) {
        nPlotErrorBarsV(labelId, xs, ys, err, count, flags, offset);
    }

    public static void plotErrorBars(String labelId, short[] xs, short[] ys, short[] neg, short[] pos) {
        nPlotErrorBars(labelId, xs, ys, neg, pos);
    }

    public static void plotErrorBars(String labelId, short[] xs, short[] ys, short[] neg, short[] pos, int flags) {
        nPlotErrorBars(labelId, xs, ys, neg, pos, flags);
    }

    public static void plotErrorBars(String labelId, short[] xs, short[] ys, short[] neg, short[] pos, int flags, int offset) {
        nPlotErrorBars(labelId, xs, ys, neg, pos, flags, offset);
    }

    public static void plotErrorBars(String labelId, int[] xs, int[] ys, int[] neg, int[] pos) {
        nPlotErrorBars(labelId, xs, ys, neg, pos);
    }

    public static void plotErrorBars(String labelId, int[] xs, int[] ys, int[] neg, int[] pos, int flags) {
        nPlotErrorBars(labelId, xs, ys, neg, pos, flags);
    }

    public static void plotErrorBars(String labelId, int[] xs, int[] ys, int[] neg, int[] pos, int flags, int offset) {
        nPlotErrorBars(labelId, xs, ys, neg, pos, flags, offset);
    }

    public static void plotErrorBars(String labelId, long[] xs, long[] ys, long[] neg, long[] pos) {
        nPlotErrorBars(labelId, xs, ys, neg, pos);
    }

    public static void plotErrorBars(String labelId, long[] xs, long[] ys, long[] neg, long[] pos, int flags) {
        nPlotErrorBars(labelId, xs, ys, neg, pos, flags);
    }

    public static void plotErrorBars(String labelId, long[] xs, long[] ys, long[] neg, long[] pos, int flags, int offset) {
        nPlotErrorBars(labelId, xs, ys, neg, pos, flags, offset);
    }

    public static void plotErrorBars(String labelId, float[] xs, float[] ys, float[] neg, float[] pos) {
        nPlotErrorBars(labelId, xs, ys, neg, pos);
    }

    public static void plotErrorBars(String labelId, float[] xs, float[] ys, float[] neg, float[] pos, int flags) {
        nPlotErrorBars(labelId, xs, ys, neg, pos, flags);
    }

    public static void plotErrorBars(String labelId, float[] xs, float[] ys, float[] neg, float[] pos, int flags, int offset) {
        nPlotErrorBars(labelId, xs, ys, neg, pos, flags, offset);
    }

    public static void plotErrorBars(String labelId, double[] xs, double[] ys, double[] neg, double[] pos) {
        nPlotErrorBars(labelId, xs, ys, neg, pos);
    }

    public static void plotErrorBars(String labelId, double[] xs, double[] ys, double[] neg, double[] pos, int flags) {
        nPlotErrorBars(labelId, xs, ys, neg, pos, flags);
    }

    public static void plotErrorBars(String labelId, double[] xs, double[] ys, double[] neg, double[] pos, int flags, int offset) {
        nPlotErrorBars(labelId, xs, ys, neg, pos, flags, offset);
    }

    public static void plotErrorBarsV(String labelId, short[] xs, short[] ys, short[] neg, short[] pos, int count) {
        nPlotErrorBarsV(labelId, xs, ys, neg, pos, count);
    }

    public static void plotErrorBarsV(String labelId, short[] xs, short[] ys, short[] neg, short[] pos, int count, int flags) {
        nPlotErrorBarsV(labelId, xs, ys, neg, pos, count, flags);
    }

    public static void plotErrorBarsV(String labelId, short[] xs, short[] ys, short[] neg, short[] pos, int count, int flags, int offset) {
        nPlotErrorBarsV(labelId, xs, ys, neg, pos, count, flags, offset);
    }

    public static void plotErrorBarsV(String labelId, int[] xs, int[] ys, int[] neg, int[] pos, int count) {
        nPlotErrorBarsV(labelId, xs, ys, neg, pos, count);
    }

    public static void plotErrorBarsV(String labelId, int[] xs, int[] ys, int[] neg, int[] pos, int count, int flags) {
        nPlotErrorBarsV(labelId, xs, ys, neg, pos, count, flags);
    }

    public static void plotErrorBarsV(String labelId, int[] xs, int[] ys, int[] neg, int[] pos, int count, int flags, int offset) {
        nPlotErrorBarsV(labelId, xs, ys, neg, pos, count, flags, offset);
    }

    public static void plotErrorBarsV(String labelId, long[] xs, long[] ys, long[] neg, long[] pos, int count) {
        nPlotErrorBarsV(labelId, xs, ys, neg, pos, count);
    }

    public static void plotErrorBarsV(String labelId, long[] xs, long[] ys, long[] neg, long[] pos, int count, int flags) {
        nPlotErrorBarsV(labelId, xs, ys, neg, pos, count, flags);
    }

    public static void plotErrorBarsV(String labelId, long[] xs, long[] ys, long[] neg, long[] pos, int count, int flags, int offset) {
        nPlotErrorBarsV(labelId, xs, ys, neg, pos, count, flags, offset);
    }

    public static void plotErrorBarsV(String labelId, float[] xs, float[] ys, float[] neg, float[] pos, int count) {
        nPlotErrorBarsV(labelId, xs, ys, neg, pos, count);
    }

    public static void plotErrorBarsV(String labelId, float[] xs, float[] ys, float[] neg, float[] pos, int count, int flags) {
        nPlotErrorBarsV(labelId, xs, ys, neg, pos, count, flags);
    }

    public static void plotErrorBarsV(String labelId, float[] xs, float[] ys, float[] neg, float[] pos, int count, int flags, int offset) {
        nPlotErrorBarsV(labelId, xs, ys, neg, pos, count, flags, offset);
    }

    public static void plotErrorBarsV(String labelId, double[] xs, double[] ys, double[] neg, double[] pos, int count) {
        nPlotErrorBarsV(labelId, xs, ys, neg, pos, count);
    }

    public static void plotErrorBarsV(String labelId, double[] xs, double[] ys, double[] neg, double[] pos, int count, int flags) {
        nPlotErrorBarsV(labelId, xs, ys, neg, pos, count, flags);
    }

    public static void plotErrorBarsV(String labelId, double[] xs, double[] ys, double[] neg, double[] pos, int count, int flags, int offset) {
        nPlotErrorBarsV(labelId, xs, ys, neg, pos, count, flags, offset);
    }

    public static void plotStems(String labelId, short[] values) {
        nPlotStems(labelId, values);
    }

    public static void plotStems(String labelId, short[] values, double yRef) {
        nPlotStems(labelId, values, yRef);
    }

    public static void plotStems(String labelId, short[] values, double yRef, double xscale) {
        nPlotStems(labelId, values, yRef, xscale);
    }

    public static void plotStems(String labelId, short[] values, double yRef, double xscale, double xstart) {
        nPlotStems(labelId, values, yRef, xscale, xstart);
    }

    public static void plotStems(String labelId, short[] values, double yRef, double xscale, double xstart, int flags) {
        nPlotStems(labelId, values, yRef, xscale, xstart, flags);
    }

    public static void plotStems(String labelId, short[] values, double yRef, double xscale, double xstart, int flags, int offset) {
        nPlotStems(labelId, values, yRef, xscale, xstart, flags, offset);
    }

    public static void plotStems(String labelId, int[] values) {
        nPlotStems(labelId, values);
    }

    public static void plotStems(String labelId, int[] values, double yRef) {
        nPlotStems(labelId, values, yRef);
    }

    public static void plotStems(String labelId, int[] values, double yRef, double xscale) {
        nPlotStems(labelId, values, yRef, xscale);
    }

    public static void plotStems(String labelId, int[] values, double yRef, double xscale, double xstart) {
        nPlotStems(labelId, values, yRef, xscale, xstart);
    }

    public static void plotStems(String labelId, int[] values, double yRef, double xscale, double xstart, int flags) {
        nPlotStems(labelId, values, yRef, xscale, xstart, flags);
    }

    public static void plotStems(String labelId, int[] values, double yRef, double xscale, double xstart, int flags, int offset) {
        nPlotStems(labelId, values, yRef, xscale, xstart, flags, offset);
    }

    public static void plotStems(String labelId, long[] values) {
        nPlotStems(labelId, values);
    }

    public static void plotStems(String labelId, long[] values, double yRef) {
        nPlotStems(labelId, values, yRef);
    }

    public static void plotStems(String labelId, long[] values, double yRef, double xscale) {
        nPlotStems(labelId, values, yRef, xscale);
    }

    public static void plotStems(String labelId, long[] values, double yRef, double xscale, double xstart) {
        nPlotStems(labelId, values, yRef, xscale, xstart);
    }

    public static void plotStems(String labelId, long[] values, double yRef, double xscale, double xstart, int flags) {
        nPlotStems(labelId, values, yRef, xscale, xstart, flags);
    }

    public static void plotStems(String labelId, long[] values, double yRef, double xscale, double xstart, int flags, int offset) {
        nPlotStems(labelId, values, yRef, xscale, xstart, flags, offset);
    }

    public static void plotStems(String labelId, float[] values) {
        nPlotStems(labelId, values);
    }

    public static void plotStems(String labelId, float[] values, double yRef) {
        nPlotStems(labelId, values, yRef);
    }

    public static void plotStems(String labelId, float[] values, double yRef, double xscale) {
        nPlotStems(labelId, values, yRef, xscale);
    }

    public static void plotStems(String labelId, float[] values, double yRef, double xscale, double xstart) {
        nPlotStems(labelId, values, yRef, xscale, xstart);
    }

    public static void plotStems(String labelId, float[] values, double yRef, double xscale, double xstart, int flags) {
        nPlotStems(labelId, values, yRef, xscale, xstart, flags);
    }

    public static void plotStems(String labelId, float[] values, double yRef, double xscale, double xstart, int flags, int offset) {
        nPlotStems(labelId, values, yRef, xscale, xstart, flags, offset);
    }

    public static void plotStems(String labelId, double[] values) {
        nPlotStems(labelId, values);
    }

    public static void plotStems(String labelId, double[] values, double yRef) {
        nPlotStems(labelId, values, yRef);
    }

    public static void plotStems(String labelId, double[] values, double yRef, double xscale) {
        nPlotStems(labelId, values, yRef, xscale);
    }

    public static void plotStems(String labelId, double[] values, double yRef, double xscale, double xstart) {
        nPlotStems(labelId, values, yRef, xscale, xstart);
    }

    public static void plotStems(String labelId, double[] values, double yRef, double xscale, double xstart, int flags) {
        nPlotStems(labelId, values, yRef, xscale, xstart, flags);
    }

    public static void plotStems(String labelId, double[] values, double yRef, double xscale, double xstart, int flags, int offset) {
        nPlotStems(labelId, values, yRef, xscale, xstart, flags, offset);
    }

    public static void plotStemsV(String labelId, short[] values, int count) {
        nPlotStemsV(labelId, values, count);
    }

    public static void plotStemsV(String labelId, short[] values, int count, double yRef) {
        nPlotStemsV(labelId, values, count, yRef);
    }

    public static void plotStemsV(String labelId, short[] values, int count, double yRef, double scale) {
        nPlotStemsV(labelId, values, count, yRef, scale);
    }

    public static void plotStemsV(String labelId, short[] values, int count, double yRef, double scale, double start) {
        nPlotStemsV(labelId, values, count, yRef, scale, start);
    }

    public static void plotStemsV(String labelId, short[] values, int count, double yRef, double scale, double start, int flags) {
        nPlotStemsV(labelId, values, count, yRef, scale, start, flags);
    }

    public static void plotStemsV(String labelId, short[] values, int count, double yRef, double scale, double start, int flags, int offset) {
        nPlotStemsV(labelId, values, count, yRef, scale, start, flags, offset);
    }

    public static void plotStemsV(String labelId, int[] values, int count) {
        nPlotStemsV(labelId, values, count);
    }

    public static void plotStemsV(String labelId, int[] values, int count, double yRef) {
        nPlotStemsV(labelId, values, count, yRef);
    }

    public static void plotStemsV(String labelId, int[] values, int count, double yRef, double scale) {
        nPlotStemsV(labelId, values, count, yRef, scale);
    }

    public static void plotStemsV(String labelId, int[] values, int count, double yRef, double scale, double start) {
        nPlotStemsV(labelId, values, count, yRef, scale, start);
    }

    public static void plotStemsV(String labelId, int[] values, int count, double yRef, double scale, double start, int flags) {
        nPlotStemsV(labelId, values, count, yRef, scale, start, flags);
    }

    public static void plotStemsV(String labelId, int[] values, int count, double yRef, double scale, double start, int flags, int offset) {
        nPlotStemsV(labelId, values, count, yRef, scale, start, flags, offset);
    }

    public static void plotStemsV(String labelId, long[] values, int count) {
        nPlotStemsV(labelId, values, count);
    }

    public static void plotStemsV(String labelId, long[] values, int count, double yRef) {
        nPlotStemsV(labelId, values, count, yRef);
    }

    public static void plotStemsV(String labelId, long[] values, int count, double yRef, double scale) {
        nPlotStemsV(labelId, values, count, yRef, scale);
    }

    public static void plotStemsV(String labelId, long[] values, int count, double yRef, double scale, double start) {
        nPlotStemsV(labelId, values, count, yRef, scale, start);
    }

    public static void plotStemsV(String labelId, long[] values, int count, double yRef, double scale, double start, int flags) {
        nPlotStemsV(labelId, values, count, yRef, scale, start, flags);
    }

    public static void plotStemsV(String labelId, long[] values, int count, double yRef, double scale, double start, int flags, int offset) {
        nPlotStemsV(labelId, values, count, yRef, scale, start, flags, offset);
    }

    public static void plotStemsV(String labelId, float[] values, int count) {
        nPlotStemsV(labelId, values, count);
    }

    public static void plotStemsV(String labelId, float[] values, int count, double yRef) {
        nPlotStemsV(labelId, values, count, yRef);
    }

    public static void plotStemsV(String labelId, float[] values, int count, double yRef, double scale) {
        nPlotStemsV(labelId, values, count, yRef, scale);
    }

    public static void plotStemsV(String labelId, float[] values, int count, double yRef, double scale, double start) {
        nPlotStemsV(labelId, values, count, yRef, scale, start);
    }

    public static void plotStemsV(String labelId, float[] values, int count, double yRef, double scale, double start, int flags) {
        nPlotStemsV(labelId, values, count, yRef, scale, start, flags);
    }

    public static void plotStemsV(String labelId, float[] values, int count, double yRef, double scale, double start, int flags, int offset) {
        nPlotStemsV(labelId, values, count, yRef, scale, start, flags, offset);
    }

    public static void plotStemsV(String labelId, double[] values, int count) {
        nPlotStemsV(labelId, values, count);
    }

    public static void plotStemsV(String labelId, double[] values, int count, double yRef) {
        nPlotStemsV(labelId, values, count, yRef);
    }

    public static void plotStemsV(String labelId, double[] values, int count, double yRef, double scale) {
        nPlotStemsV(labelId, values, count, yRef, scale);
    }

    public static void plotStemsV(String labelId, double[] values, int count, double yRef, double scale, double start) {
        nPlotStemsV(labelId, values, count, yRef, scale, start);
    }

    public static void plotStemsV(String labelId, double[] values, int count, double yRef, double scale, double start, int flags) {
        nPlotStemsV(labelId, values, count, yRef, scale, start, flags);
    }

    public static void plotStemsV(String labelId, double[] values, int count, double yRef, double scale, double start, int flags, int offset) {
        nPlotStemsV(labelId, values, count, yRef, scale, start, flags, offset);
    }

    public static void plotStems(String labelId, short[] xs, short[] ys) {
        nPlotStems(labelId, xs, ys);
    }

    public static void plotStems(String labelId, short[] xs, short[] ys, double ref) {
        nPlotStems(labelId, xs, ys, ref);
    }

    public static void plotStems(String labelId, short[] xs, short[] ys, double ref, int flags) {
        nPlotStems(labelId, xs, ys, ref, flags);
    }

    public static void plotStems(String labelId, short[] xs, short[] ys, double ref, int flags, int offset) {
        nPlotStems(labelId, xs, ys, ref, flags, offset);
    }

    public static void plotStems(String labelId, int[] xs, int[] ys) {
        nPlotStems(labelId, xs, ys);
    }

    public static void plotStems(String labelId, int[] xs, int[] ys, double ref) {
        nPlotStems(labelId, xs, ys, ref);
    }

    public static void plotStems(String labelId, int[] xs, int[] ys, double ref, int flags) {
        nPlotStems(labelId, xs, ys, ref, flags);
    }

    public static void plotStems(String labelId, int[] xs, int[] ys, double ref, int flags, int offset) {
        nPlotStems(labelId, xs, ys, ref, flags, offset);
    }

    public static void plotStems(String labelId, long[] xs, long[] ys) {
        nPlotStems(labelId, xs, ys);
    }

    public static void plotStems(String labelId, long[] xs, long[] ys, double ref) {
        nPlotStems(labelId, xs, ys, ref);
    }

    public static void plotStems(String labelId, long[] xs, long[] ys, double ref, int flags) {
        nPlotStems(labelId, xs, ys, ref, flags);
    }

    public static void plotStems(String labelId, long[] xs, long[] ys, double ref, int flags, int offset) {
        nPlotStems(labelId, xs, ys, ref, flags, offset);
    }

    public static void plotStems(String labelId, float[] xs, float[] ys) {
        nPlotStems(labelId, xs, ys);
    }

    public static void plotStems(String labelId, float[] xs, float[] ys, double ref) {
        nPlotStems(labelId, xs, ys, ref);
    }

    public static void plotStems(String labelId, float[] xs, float[] ys, double ref, int flags) {
        nPlotStems(labelId, xs, ys, ref, flags);
    }

    public static void plotStems(String labelId, float[] xs, float[] ys, double ref, int flags, int offset) {
        nPlotStems(labelId, xs, ys, ref, flags, offset);
    }

    public static void plotStems(String labelId, double[] xs, double[] ys) {
        nPlotStems(labelId, xs, ys);
    }

    public static void plotStems(String labelId, double[] xs, double[] ys, double ref) {
        nPlotStems(labelId, xs, ys, ref);
    }

    public static void plotStems(String labelId, double[] xs, double[] ys, double ref, int flags) {
        nPlotStems(labelId, xs, ys, ref, flags);
    }

    public static void plotStems(String labelId, double[] xs, double[] ys, double ref, int flags, int offset) {
        nPlotStems(labelId, xs, ys, ref, flags, offset);
    }

    public static void plotStemsV(String labelId, short[] xs, short[] ys, int count) {
        nPlotStemsV(labelId, xs, ys, count);
    }

    public static void plotStemsV(String labelId, short[] xs, short[] ys, int count, double yRef) {
        nPlotStemsV(labelId, xs, ys, count, yRef);
    }

    public static void plotStemsV(String labelId, short[] xs, short[] ys, int count, double yRef, int flags) {
        nPlotStemsV(labelId, xs, ys, count, yRef, flags);
    }

    public static void plotStemsV(String labelId, short[] xs, short[] ys, int count, double yRef, int flags, int offset) {
        nPlotStemsV(labelId, xs, ys, count, yRef, flags, offset);
    }

    public static void plotStemsV(String labelId, int[] xs, int[] ys, int count) {
        nPlotStemsV(labelId, xs, ys, count);
    }

    public static void plotStemsV(String labelId, int[] xs, int[] ys, int count, double yRef) {
        nPlotStemsV(labelId, xs, ys, count, yRef);
    }

    public static void plotStemsV(String labelId, int[] xs, int[] ys, int count, double yRef, int flags) {
        nPlotStemsV(labelId, xs, ys, count, yRef, flags);
    }

    public static void plotStemsV(String labelId, int[] xs, int[] ys, int count, double yRef, int flags, int offset) {
        nPlotStemsV(labelId, xs, ys, count, yRef, flags, offset);
    }

    public static void plotStemsV(String labelId, long[] xs, long[] ys, int count) {
        nPlotStemsV(labelId, xs, ys, count);
    }

    public static void plotStemsV(String labelId, long[] xs, long[] ys, int count, double yRef) {
        nPlotStemsV(labelId, xs, ys, count, yRef);
    }

    public static void plotStemsV(String labelId, long[] xs, long[] ys, int count, double yRef, int flags) {
        nPlotStemsV(labelId, xs, ys, count, yRef, flags);
    }

    public static void plotStemsV(String labelId, long[] xs, long[] ys, int count, double yRef, int flags, int offset) {
        nPlotStemsV(labelId, xs, ys, count, yRef, flags, offset);
    }

    public static void plotStemsV(String labelId, float[] xs, float[] ys, int count) {
        nPlotStemsV(labelId, xs, ys, count);
    }

    public static void plotStemsV(String labelId, float[] xs, float[] ys, int count, double yRef) {
        nPlotStemsV(labelId, xs, ys, count, yRef);
    }

    public static void plotStemsV(String labelId, float[] xs, float[] ys, int count, double yRef, int flags) {
        nPlotStemsV(labelId, xs, ys, count, yRef, flags);
    }

    public static void plotStemsV(String labelId, float[] xs, float[] ys, int count, double yRef, int flags, int offset) {
        nPlotStemsV(labelId, xs, ys, count, yRef, flags, offset);
    }

    public static void plotStemsV(String labelId, double[] xs, double[] ys, int count) {
        nPlotStemsV(labelId, xs, ys, count);
    }

    public static void plotStemsV(String labelId, double[] xs, double[] ys, int count, double yRef) {
        nPlotStemsV(labelId, xs, ys, count, yRef);
    }

    public static void plotStemsV(String labelId, double[] xs, double[] ys, int count, double yRef, int flags) {
        nPlotStemsV(labelId, xs, ys, count, yRef, flags);
    }

    public static void plotStemsV(String labelId, double[] xs, double[] ys, int count, double yRef, int flags, int offset) {
        nPlotStemsV(labelId, xs, ys, count, yRef, flags, offset);
    }

    public static void plotInfLines(String labelId, short[] values) {
        nPlotInfLines(labelId, values);
    }

    public static void plotInfLines(String labelId, short[] values, int flags) {
        nPlotInfLines(labelId, values, flags);
    }

    public static void plotInfLines(String labelId, short[] values, int flags, int offset) {
        nPlotInfLines(labelId, values, flags, offset);
    }

    public static void plotInfLines(String labelId, int[] values) {
        nPlotInfLines(labelId, values);
    }

    public static void plotInfLines(String labelId, int[] values, int flags) {
        nPlotInfLines(labelId, values, flags);
    }

    public static void plotInfLines(String labelId, int[] values, int flags, int offset) {
        nPlotInfLines(labelId, values, flags, offset);
    }

    public static void plotInfLines(String labelId, long[] values) {
        nPlotInfLines(labelId, values);
    }

    public static void plotInfLines(String labelId, long[] values, int flags) {
        nPlotInfLines(labelId, values, flags);
    }

    public static void plotInfLines(String labelId, long[] values, int flags, int offset) {
        nPlotInfLines(labelId, values, flags, offset);
    }

    public static void plotInfLines(String labelId, float[] values) {
        nPlotInfLines(labelId, values);
    }

    public static void plotInfLines(String labelId, float[] values, int flags) {
        nPlotInfLines(labelId, values, flags);
    }

    public static void plotInfLines(String labelId, float[] values, int flags, int offset) {
        nPlotInfLines(labelId, values, flags, offset);
    }

    public static void plotInfLines(String labelId, double[] values) {
        nPlotInfLines(labelId, values);
    }

    public static void plotInfLines(String labelId, double[] values, int flags) {
        nPlotInfLines(labelId, values, flags);
    }

    public static void plotInfLines(String labelId, double[] values, int flags, int offset) {
        nPlotInfLines(labelId, values, flags, offset);
    }

    public static void plotInfLinesV(String labelId, short[] values, int count) {
        nPlotInfLinesV(labelId, values, count);
    }

    public static void plotInfLinesV(String labelId, short[] values, int count, int flags) {
        nPlotInfLinesV(labelId, values, count, flags);
    }

    public static void plotInfLinesV(String labelId, short[] values, int count, int flags, int offset) {
        nPlotInfLinesV(labelId, values, count, flags, offset);
    }

    public static void plotInfLinesV(String labelId, int[] values, int count) {
        nPlotInfLinesV(labelId, values, count);
    }

    public static void plotInfLinesV(String labelId, int[] values, int count, int flags) {
        nPlotInfLinesV(labelId, values, count, flags);
    }

    public static void plotInfLinesV(String labelId, int[] values, int count, int flags, int offset) {
        nPlotInfLinesV(labelId, values, count, flags, offset);
    }

    public static void plotInfLinesV(String labelId, long[] values, int count) {
        nPlotInfLinesV(labelId, values, count);
    }

    public static void plotInfLinesV(String labelId, long[] values, int count, int flags) {
        nPlotInfLinesV(labelId, values, count, flags);
    }

    public static void plotInfLinesV(String labelId, long[] values, int count, int flags, int offset) {
        nPlotInfLinesV(labelId, values, count, flags, offset);
    }

    public static void plotInfLinesV(String labelId, float[] values, int count) {
        nPlotInfLinesV(labelId, values, count);
    }

    public static void plotInfLinesV(String labelId, float[] values, int count, int flags) {
        nPlotInfLinesV(labelId, values, count, flags);
    }

    public static void plotInfLinesV(String labelId, float[] values, int count, int flags, int offset) {
        nPlotInfLinesV(labelId, values, count, flags, offset);
    }

    public static void plotInfLinesV(String labelId, double[] values, int count) {
        nPlotInfLinesV(labelId, values, count);
    }

    public static void plotInfLinesV(String labelId, double[] values, int count, int flags) {
        nPlotInfLinesV(labelId, values, count, flags);
    }

    public static void plotInfLinesV(String labelId, double[] values, int count, int flags, int offset) {
        nPlotInfLinesV(labelId, values, count, flags, offset);
    }

    public static void plotPieChart(String[] labelIds, short[] values, double x, double y, double radius) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius);
    }

    public static void plotPieChart(String[] labelIds, short[] values, double x, double y, double radius, String labelFmt) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius, labelFmt);
    }

    public static void plotPieChart(String[] labelIds, short[] values, double x, double y, double radius, String labelFmt, double angle0) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius, labelFmt, angle0);
    }

    public static void plotPieChart(String[] labelIds, short[] values, double x, double y, double radius, String labelFmt, double angle0, int flags) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius, labelFmt, angle0, flags);
    }

    public static void plotPieChart(String[] labelIds, short[] values, double x, double y, double radius, double angle0, int flags) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius, angle0, flags);
    }

    public static void plotPieChart(String[] labelIds, int[] values, double x, double y, double radius) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius);
    }

    public static void plotPieChart(String[] labelIds, int[] values, double x, double y, double radius, String labelFmt) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius, labelFmt);
    }

    public static void plotPieChart(String[] labelIds, int[] values, double x, double y, double radius, String labelFmt, double angle0) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius, labelFmt, angle0);
    }

    public static void plotPieChart(String[] labelIds, int[] values, double x, double y, double radius, String labelFmt, double angle0, int flags) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius, labelFmt, angle0, flags);
    }

    public static void plotPieChart(String[] labelIds, int[] values, double x, double y, double radius, double angle0, int flags) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius, angle0, flags);
    }

    public static void plotPieChart(String[] labelIds, long[] values, double x, double y, double radius) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius);
    }

    public static void plotPieChart(String[] labelIds, long[] values, double x, double y, double radius, String labelFmt) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius, labelFmt);
    }

    public static void plotPieChart(String[] labelIds, long[] values, double x, double y, double radius, String labelFmt, double angle0) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius, labelFmt, angle0);
    }

    public static void plotPieChart(String[] labelIds, long[] values, double x, double y, double radius, String labelFmt, double angle0, int flags) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius, labelFmt, angle0, flags);
    }

    public static void plotPieChart(String[] labelIds, long[] values, double x, double y, double radius, double angle0, int flags) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius, angle0, flags);
    }

    public static void plotPieChart(String[] labelIds, float[] values, double x, double y, double radius) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius);
    }

    public static void plotPieChart(String[] labelIds, float[] values, double x, double y, double radius, String labelFmt) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius, labelFmt);
    }

    public static void plotPieChart(String[] labelIds, float[] values, double x, double y, double radius, String labelFmt, double angle0) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius, labelFmt, angle0);
    }

    public static void plotPieChart(String[] labelIds, float[] values, double x, double y, double radius, String labelFmt, double angle0, int flags) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius, labelFmt, angle0, flags);
    }

    public static void plotPieChart(String[] labelIds, float[] values, double x, double y, double radius, double angle0, int flags) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius, angle0, flags);
    }

    public static void plotPieChart(String[] labelIds, double[] values, double x, double y, double radius) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius);
    }

    public static void plotPieChart(String[] labelIds, double[] values, double x, double y, double radius, String labelFmt) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius, labelFmt);
    }

    public static void plotPieChart(String[] labelIds, double[] values, double x, double y, double radius, String labelFmt, double angle0) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius, labelFmt, angle0);
    }

    public static void plotPieChart(String[] labelIds, double[] values, double x, double y, double radius, String labelFmt, double angle0, int flags) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius, labelFmt, angle0, flags);
    }

    public static void plotPieChart(String[] labelIds, double[] values, double x, double y, double radius, double angle0, int flags) {
        nPlotPieChart(labelIds, labelIds.length, values, x, y, radius, angle0, flags);
    }

    public static void plotPieChartV(String[] labelIds, short[] values, int count, double x, double y, double radius) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius);
    }

    public static void plotPieChartV(String[] labelIds, short[] values, int count, double x, double y, double radius, String labelFmt) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius, labelFmt);
    }

    public static void plotPieChartV(String[] labelIds, short[] values, int count, double x, double y, double radius, String labelFmt, double angle0) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius, labelFmt, angle0);
    }

    public static void plotPieChartV(String[] labelIds, short[] values, int count, double x, double y, double radius, String labelFmt, double angle0, int flags) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius, labelFmt, angle0, flags);
    }

    public static void plotPieChartV(String[] labelIds, short[] values, int count, double x, double y, double radius, double angle0, int flags) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius, angle0, flags);
    }

    public static void plotPieChartV(String[] labelIds, int[] values, int count, double x, double y, double radius) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius);
    }

    public static void plotPieChartV(String[] labelIds, int[] values, int count, double x, double y, double radius, String labelFmt) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius, labelFmt);
    }

    public static void plotPieChartV(String[] labelIds, int[] values, int count, double x, double y, double radius, String labelFmt, double angle0) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius, labelFmt, angle0);
    }

    public static void plotPieChartV(String[] labelIds, int[] values, int count, double x, double y, double radius, String labelFmt, double angle0, int flags) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius, labelFmt, angle0, flags);
    }

    public static void plotPieChartV(String[] labelIds, int[] values, int count, double x, double y, double radius, double angle0, int flags) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius, angle0, flags);
    }

    public static void plotPieChartV(String[] labelIds, long[] values, int count, double x, double y, double radius) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius);
    }

    public static void plotPieChartV(String[] labelIds, long[] values, int count, double x, double y, double radius, String labelFmt) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius, labelFmt);
    }

    public static void plotPieChartV(String[] labelIds, long[] values, int count, double x, double y, double radius, String labelFmt, double angle0) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius, labelFmt, angle0);
    }

    public static void plotPieChartV(String[] labelIds, long[] values, int count, double x, double y, double radius, String labelFmt, double angle0, int flags) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius, labelFmt, angle0, flags);
    }

    public static void plotPieChartV(String[] labelIds, long[] values, int count, double x, double y, double radius, double angle0, int flags) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius, angle0, flags);
    }

    public static void plotPieChartV(String[] labelIds, float[] values, int count, double x, double y, double radius) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius);
    }

    public static void plotPieChartV(String[] labelIds, float[] values, int count, double x, double y, double radius, String labelFmt) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius, labelFmt);
    }

    public static void plotPieChartV(String[] labelIds, float[] values, int count, double x, double y, double radius, String labelFmt, double angle0) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius, labelFmt, angle0);
    }

    public static void plotPieChartV(String[] labelIds, float[] values, int count, double x, double y, double radius, String labelFmt, double angle0, int flags) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius, labelFmt, angle0, flags);
    }

    public static void plotPieChartV(String[] labelIds, float[] values, int count, double x, double y, double radius, double angle0, int flags) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius, angle0, flags);
    }

    public static void plotPieChartV(String[] labelIds, double[] values, int count, double x, double y, double radius) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius);
    }

    public static void plotPieChartV(String[] labelIds, double[] values, int count, double x, double y, double radius, String labelFmt) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius, labelFmt);
    }

    public static void plotPieChartV(String[] labelIds, double[] values, int count, double x, double y, double radius, String labelFmt, double angle0) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius, labelFmt, angle0);
    }

    public static void plotPieChartV(String[] labelIds, double[] values, int count, double x, double y, double radius, String labelFmt, double angle0, int flags) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius, labelFmt, angle0, flags);
    }

    public static void plotPieChartV(String[] labelIds, double[] values, int count, double x, double y, double radius, double angle0, int flags) {
        nPlotPieChartV(labelIds, labelIds.length, values, count, x, y, radius, angle0, flags);
    }

    public static void plotHeatmap(String labelId, short[] values, int rows, int cols) {
        nPlotHeatmap(labelId, values, rows, cols);
    }

    public static void plotHeatmap(String labelId, short[] values, int rows, int cols, double scaleMin) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin);
    }

    public static void plotHeatmap(String labelId, short[] values, int rows, int cols, double scaleMin, double scaleMax) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax);
    }

    public static void plotHeatmap(String labelId, short[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt);
    }

    public static void plotHeatmap(String labelId, short[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, ImPlotPoint boundsMin) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMin.x, boundsMin.y);
    }

    public static void plotHeatmap(String labelId, short[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, double boundsMinX, double boundsMinY) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMinX, boundsMinY);
    }

    public static void plotHeatmap(String labelId, short[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, ImPlotPoint boundsMin, ImPlotPoint boundsMax) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y);
    }

    public static void plotHeatmap(String labelId, short[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, double boundsMinX, double boundsMinY, double boundsMaxX, double boundsMaxY) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMinX, boundsMinY, boundsMaxX, boundsMaxY);
    }

    public static void plotHeatmap(String labelId, short[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, ImPlotPoint boundsMin, ImPlotPoint boundsMax, int flags) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y, flags);
    }

    public static void plotHeatmap(String labelId, short[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, double boundsMinX, double boundsMinY, double boundsMaxX, double boundsMaxY, int flags) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMinX, boundsMinY, boundsMaxX, boundsMaxY, flags);
    }

    public static void plotHeatmap(String labelId, short[] values, int rows, int cols, double scaleMin, double scaleMax, ImPlotPoint boundsMin, ImPlotPoint boundsMax, int flags) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y, flags);
    }

    public static void plotHeatmap(String labelId, short[] values, int rows, int cols, double scaleMin, double scaleMax, double boundsMinX, double boundsMinY, double boundsMaxX, double boundsMaxY, int flags) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, boundsMinX, boundsMinY, boundsMaxX, boundsMaxY, flags);
    }

    public static void plotHeatmap(String labelId, int[] values, int rows, int cols) {
        nPlotHeatmap(labelId, values, rows, cols);
    }

    public static void plotHeatmap(String labelId, int[] values, int rows, int cols, double scaleMin) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin);
    }

    public static void plotHeatmap(String labelId, int[] values, int rows, int cols, double scaleMin, double scaleMax) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax);
    }

    public static void plotHeatmap(String labelId, int[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt);
    }

    public static void plotHeatmap(String labelId, int[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, ImPlotPoint boundsMin) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMin.x, boundsMin.y);
    }

    public static void plotHeatmap(String labelId, int[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, double boundsMinX, double boundsMinY) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMinX, boundsMinY);
    }

    public static void plotHeatmap(String labelId, int[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, ImPlotPoint boundsMin, ImPlotPoint boundsMax) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y);
    }

    public static void plotHeatmap(String labelId, int[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, double boundsMinX, double boundsMinY, double boundsMaxX, double boundsMaxY) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMinX, boundsMinY, boundsMaxX, boundsMaxY);
    }

    public static void plotHeatmap(String labelId, int[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, ImPlotPoint boundsMin, ImPlotPoint boundsMax, int flags) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y, flags);
    }

    public static void plotHeatmap(String labelId, int[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, double boundsMinX, double boundsMinY, double boundsMaxX, double boundsMaxY, int flags) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMinX, boundsMinY, boundsMaxX, boundsMaxY, flags);
    }

    public static void plotHeatmap(String labelId, int[] values, int rows, int cols, double scaleMin, double scaleMax, ImPlotPoint boundsMin, ImPlotPoint boundsMax, int flags) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y, flags);
    }

    public static void plotHeatmap(String labelId, int[] values, int rows, int cols, double scaleMin, double scaleMax, double boundsMinX, double boundsMinY, double boundsMaxX, double boundsMaxY, int flags) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, boundsMinX, boundsMinY, boundsMaxX, boundsMaxY, flags);
    }

    public static void plotHeatmap(String labelId, long[] values, int rows, int cols) {
        nPlotHeatmap(labelId, values, rows, cols);
    }

    public static void plotHeatmap(String labelId, long[] values, int rows, int cols, double scaleMin) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin);
    }

    public static void plotHeatmap(String labelId, long[] values, int rows, int cols, double scaleMin, double scaleMax) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax);
    }

    public static void plotHeatmap(String labelId, long[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt);
    }

    public static void plotHeatmap(String labelId, long[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, ImPlotPoint boundsMin) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMin.x, boundsMin.y);
    }

    public static void plotHeatmap(String labelId, long[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, double boundsMinX, double boundsMinY) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMinX, boundsMinY);
    }

    public static void plotHeatmap(String labelId, long[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, ImPlotPoint boundsMin, ImPlotPoint boundsMax) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y);
    }

    public static void plotHeatmap(String labelId, long[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, double boundsMinX, double boundsMinY, double boundsMaxX, double boundsMaxY) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMinX, boundsMinY, boundsMaxX, boundsMaxY);
    }

    public static void plotHeatmap(String labelId, long[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, ImPlotPoint boundsMin, ImPlotPoint boundsMax, int flags) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y, flags);
    }

    public static void plotHeatmap(String labelId, long[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, double boundsMinX, double boundsMinY, double boundsMaxX, double boundsMaxY, int flags) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMinX, boundsMinY, boundsMaxX, boundsMaxY, flags);
    }

    public static void plotHeatmap(String labelId, long[] values, int rows, int cols, double scaleMin, double scaleMax, ImPlotPoint boundsMin, ImPlotPoint boundsMax, int flags) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y, flags);
    }

    public static void plotHeatmap(String labelId, long[] values, int rows, int cols, double scaleMin, double scaleMax, double boundsMinX, double boundsMinY, double boundsMaxX, double boundsMaxY, int flags) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, boundsMinX, boundsMinY, boundsMaxX, boundsMaxY, flags);
    }

    public static void plotHeatmap(String labelId, float[] values, int rows, int cols) {
        nPlotHeatmap(labelId, values, rows, cols);
    }

    public static void plotHeatmap(String labelId, float[] values, int rows, int cols, double scaleMin) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin);
    }

    public static void plotHeatmap(String labelId, float[] values, int rows, int cols, double scaleMin, double scaleMax) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax);
    }

    public static void plotHeatmap(String labelId, float[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt);
    }

    public static void plotHeatmap(String labelId, float[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, ImPlotPoint boundsMin) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMin.x, boundsMin.y);
    }

    public static void plotHeatmap(String labelId, float[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, double boundsMinX, double boundsMinY) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMinX, boundsMinY);
    }

    public static void plotHeatmap(String labelId, float[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, ImPlotPoint boundsMin, ImPlotPoint boundsMax) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y);
    }

    public static void plotHeatmap(String labelId, float[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, double boundsMinX, double boundsMinY, double boundsMaxX, double boundsMaxY) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMinX, boundsMinY, boundsMaxX, boundsMaxY);
    }

    public static void plotHeatmap(String labelId, float[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, ImPlotPoint boundsMin, ImPlotPoint boundsMax, int flags) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y, flags);
    }

    public static void plotHeatmap(String labelId, float[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, double boundsMinX, double boundsMinY, double boundsMaxX, double boundsMaxY, int flags) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMinX, boundsMinY, boundsMaxX, boundsMaxY, flags);
    }

    public static void plotHeatmap(String labelId, float[] values, int rows, int cols, double scaleMin, double scaleMax, ImPlotPoint boundsMin, ImPlotPoint boundsMax, int flags) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y, flags);
    }

    public static void plotHeatmap(String labelId, float[] values, int rows, int cols, double scaleMin, double scaleMax, double boundsMinX, double boundsMinY, double boundsMaxX, double boundsMaxY, int flags) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, boundsMinX, boundsMinY, boundsMaxX, boundsMaxY, flags);
    }

    public static void plotHeatmap(String labelId, double[] values, int rows, int cols) {
        nPlotHeatmap(labelId, values, rows, cols);
    }

    public static void plotHeatmap(String labelId, double[] values, int rows, int cols, double scaleMin) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin);
    }

    public static void plotHeatmap(String labelId, double[] values, int rows, int cols, double scaleMin, double scaleMax) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax);
    }

    public static void plotHeatmap(String labelId, double[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt);
    }

    public static void plotHeatmap(String labelId, double[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, ImPlotPoint boundsMin) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMin.x, boundsMin.y);
    }

    public static void plotHeatmap(String labelId, double[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, double boundsMinX, double boundsMinY) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMinX, boundsMinY);
    }

    public static void plotHeatmap(String labelId, double[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, ImPlotPoint boundsMin, ImPlotPoint boundsMax) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y);
    }

    public static void plotHeatmap(String labelId, double[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, double boundsMinX, double boundsMinY, double boundsMaxX, double boundsMaxY) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMinX, boundsMinY, boundsMaxX, boundsMaxY);
    }

    public static void plotHeatmap(String labelId, double[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, ImPlotPoint boundsMin, ImPlotPoint boundsMax, int flags) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y, flags);
    }

    public static void plotHeatmap(String labelId, double[] values, int rows, int cols, double scaleMin, double scaleMax, String labelFmt, double boundsMinX, double boundsMinY, double boundsMaxX, double boundsMaxY, int flags) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, labelFmt, boundsMinX, boundsMinY, boundsMaxX, boundsMaxY, flags);
    }

    public static void plotHeatmap(String labelId, double[] values, int rows, int cols, double scaleMin, double scaleMax, ImPlotPoint boundsMin, ImPlotPoint boundsMax, int flags) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y, flags);
    }

    public static void plotHeatmap(String labelId, double[] values, int rows, int cols, double scaleMin, double scaleMax, double boundsMinX, double boundsMinY, double boundsMaxX, double boundsMaxY, int flags) {
        nPlotHeatmap(labelId, values, rows, cols, scaleMin, scaleMax, boundsMinX, boundsMinY, boundsMaxX, boundsMaxY, flags);
    }

    public static double plotHistogram(String labelId, short[] values) {
        return nPlotHistogram(labelId, values);
    }

    public static double plotHistogram(String labelId, short[] values, int bins) {
        return nPlotHistogram(labelId, values, bins);
    }

    public static double plotHistogram(String labelId, short[] values, int bins, double barScale) {
        return nPlotHistogram(labelId, values, bins, barScale);
    }

    public static double plotHistogram(String labelId, short[] values, int bins, double barScale, ImPlotRange range) {
        return nPlotHistogram(labelId, values, bins, barScale, range.min, range.max);
    }

    public static double plotHistogram(String labelId, short[] values, int bins, double barScale, double rangeMin, double rangeMax) {
        return nPlotHistogram(labelId, values, bins, barScale, rangeMin, rangeMax);
    }

    public static double plotHistogram(String labelId, short[] values, int bins, double barScale, ImPlotRange range, int flags) {
        return nPlotHistogram(labelId, values, bins, barScale, range.min, range.max, flags);
    }

    public static double plotHistogram(String labelId, short[] values, int bins, double barScale, double rangeMin, double rangeMax, int flags) {
        return nPlotHistogram(labelId, values, bins, barScale, rangeMin, rangeMax, flags);
    }

    public static double plotHistogram(String labelId, short[] values, double barScale, ImPlotRange range, int flags) {
        return nPlotHistogram(labelId, values, barScale, range.min, range.max, flags);
    }

    public static double plotHistogram(String labelId, short[] values, double barScale, double rangeMin, double rangeMax, int flags) {
        return nPlotHistogram(labelId, values, barScale, rangeMin, rangeMax, flags);
    }

    public static double plotHistogram(String labelId, short[] values, int bins, double barScale, int flags) {
        return nPlotHistogram(labelId, values, bins, barScale, flags);
    }

    public static double plotHistogram(String labelId, int[] values) {
        return nPlotHistogram(labelId, values);
    }

    public static double plotHistogram(String labelId, int[] values, int bins) {
        return nPlotHistogram(labelId, values, bins);
    }

    public static double plotHistogram(String labelId, int[] values, int bins, double barScale) {
        return nPlotHistogram(labelId, values, bins, barScale);
    }

    public static double plotHistogram(String labelId, int[] values, int bins, double barScale, ImPlotRange range) {
        return nPlotHistogram(labelId, values, bins, barScale, range.min, range.max);
    }

    public static double plotHistogram(String labelId, int[] values, int bins, double barScale, double rangeMin, double rangeMax) {
        return nPlotHistogram(labelId, values, bins, barScale, rangeMin, rangeMax);
    }

    public static double plotHistogram(String labelId, int[] values, int bins, double barScale, ImPlotRange range, int flags) {
        return nPlotHistogram(labelId, values, bins, barScale, range.min, range.max, flags);
    }

    public static double plotHistogram(String labelId, int[] values, int bins, double barScale, double rangeMin, double rangeMax, int flags) {
        return nPlotHistogram(labelId, values, bins, barScale, rangeMin, rangeMax, flags);
    }

    public static double plotHistogram(String labelId, int[] values, double barScale, ImPlotRange range, int flags) {
        return nPlotHistogram(labelId, values, barScale, range.min, range.max, flags);
    }

    public static double plotHistogram(String labelId, int[] values, double barScale, double rangeMin, double rangeMax, int flags) {
        return nPlotHistogram(labelId, values, barScale, rangeMin, rangeMax, flags);
    }

    public static double plotHistogram(String labelId, int[] values, int bins, double barScale, int flags) {
        return nPlotHistogram(labelId, values, bins, barScale, flags);
    }

    public static double plotHistogram(String labelId, long[] values) {
        return nPlotHistogram(labelId, values);
    }

    public static double plotHistogram(String labelId, long[] values, int bins) {
        return nPlotHistogram(labelId, values, bins);
    }

    public static double plotHistogram(String labelId, long[] values, int bins, double barScale) {
        return nPlotHistogram(labelId, values, bins, barScale);
    }

    public static double plotHistogram(String labelId, long[] values, int bins, double barScale, ImPlotRange range) {
        return nPlotHistogram(labelId, values, bins, barScale, range.min, range.max);
    }

    public static double plotHistogram(String labelId, long[] values, int bins, double barScale, double rangeMin, double rangeMax) {
        return nPlotHistogram(labelId, values, bins, barScale, rangeMin, rangeMax);
    }

    public static double plotHistogram(String labelId, long[] values, int bins, double barScale, ImPlotRange range, int flags) {
        return nPlotHistogram(labelId, values, bins, barScale, range.min, range.max, flags);
    }

    public static double plotHistogram(String labelId, long[] values, int bins, double barScale, double rangeMin, double rangeMax, int flags) {
        return nPlotHistogram(labelId, values, bins, barScale, rangeMin, rangeMax, flags);
    }

    public static double plotHistogram(String labelId, long[] values, double barScale, ImPlotRange range, int flags) {
        return nPlotHistogram(labelId, values, barScale, range.min, range.max, flags);
    }

    public static double plotHistogram(String labelId, long[] values, double barScale, double rangeMin, double rangeMax, int flags) {
        return nPlotHistogram(labelId, values, barScale, rangeMin, rangeMax, flags);
    }

    public static double plotHistogram(String labelId, long[] values, int bins, double barScale, int flags) {
        return nPlotHistogram(labelId, values, bins, barScale, flags);
    }

    public static double plotHistogram(String labelId, float[] values) {
        return nPlotHistogram(labelId, values);
    }

    public static double plotHistogram(String labelId, float[] values, int bins) {
        return nPlotHistogram(labelId, values, bins);
    }

    public static double plotHistogram(String labelId, float[] values, int bins, double barScale) {
        return nPlotHistogram(labelId, values, bins, barScale);
    }

    public static double plotHistogram(String labelId, float[] values, int bins, double barScale, ImPlotRange range) {
        return nPlotHistogram(labelId, values, bins, barScale, range.min, range.max);
    }

    public static double plotHistogram(String labelId, float[] values, int bins, double barScale, double rangeMin, double rangeMax) {
        return nPlotHistogram(labelId, values, bins, barScale, rangeMin, rangeMax);
    }

    public static double plotHistogram(String labelId, float[] values, int bins, double barScale, ImPlotRange range, int flags) {
        return nPlotHistogram(labelId, values, bins, barScale, range.min, range.max, flags);
    }

    public static double plotHistogram(String labelId, float[] values, int bins, double barScale, double rangeMin, double rangeMax, int flags) {
        return nPlotHistogram(labelId, values, bins, barScale, rangeMin, rangeMax, flags);
    }

    public static double plotHistogram(String labelId, float[] values, double barScale, ImPlotRange range, int flags) {
        return nPlotHistogram(labelId, values, barScale, range.min, range.max, flags);
    }

    public static double plotHistogram(String labelId, float[] values, double barScale, double rangeMin, double rangeMax, int flags) {
        return nPlotHistogram(labelId, values, barScale, rangeMin, rangeMax, flags);
    }

    public static double plotHistogram(String labelId, float[] values, int bins, double barScale, int flags) {
        return nPlotHistogram(labelId, values, bins, barScale, flags);
    }

    public static double plotHistogram(String labelId, double[] values) {
        return nPlotHistogram(labelId, values);
    }

    public static double plotHistogram(String labelId, double[] values, int bins) {
        return nPlotHistogram(labelId, values, bins);
    }

    public static double plotHistogram(String labelId, double[] values, int bins, double barScale) {
        return nPlotHistogram(labelId, values, bins, barScale);
    }

    public static double plotHistogram(String labelId, double[] values, int bins, double barScale, ImPlotRange range) {
        return nPlotHistogram(labelId, values, bins, barScale, range.min, range.max);
    }

    public static double plotHistogram(String labelId, double[] values, int bins, double barScale, double rangeMin, double rangeMax) {
        return nPlotHistogram(labelId, values, bins, barScale, rangeMin, rangeMax);
    }

    public static double plotHistogram(String labelId, double[] values, int bins, double barScale, ImPlotRange range, int flags) {
        return nPlotHistogram(labelId, values, bins, barScale, range.min, range.max, flags);
    }

    public static double plotHistogram(String labelId, double[] values, int bins, double barScale, double rangeMin, double rangeMax, int flags) {
        return nPlotHistogram(labelId, values, bins, barScale, rangeMin, rangeMax, flags);
    }

    public static double plotHistogram(String labelId, double[] values, double barScale, ImPlotRange range, int flags) {
        return nPlotHistogram(labelId, values, barScale, range.min, range.max, flags);
    }

    public static double plotHistogram(String labelId, double[] values, double barScale, double rangeMin, double rangeMax, int flags) {
        return nPlotHistogram(labelId, values, barScale, rangeMin, rangeMax, flags);
    }

    public static double plotHistogram(String labelId, double[] values, int bins, double barScale, int flags) {
        return nPlotHistogram(labelId, values, bins, barScale, flags);
    }

    public static double plotHistogramV(String labelId, short[] values, int count) {
        return nPlotHistogramV(labelId, values, count);
    }

    public static double plotHistogramV(String labelId, short[] values, int count, int bins) {
        return nPlotHistogramV(labelId, values, count, bins);
    }

    public static double plotHistogramV(String labelId, short[] values, int count, int bins, double barScale) {
        return nPlotHistogramV(labelId, values, count, bins, barScale);
    }

    public static double plotHistogramV(String labelId, short[] values, int count, int bins, double barScale, ImPlotRange range) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, range.min, range.max);
    }

    public static double plotHistogramV(String labelId, short[] values, int count, int bins, double barScale, double rangeMin, double rangeMax) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, rangeMin, rangeMax);
    }

    public static double plotHistogramV(String labelId, short[] values, int count, int bins, double barScale, ImPlotRange range, int flags) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, range.min, range.max, flags);
    }

    public static double plotHistogramV(String labelId, short[] values, int count, int bins, double barScale, double rangeMin, double rangeMax, int flags) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, rangeMin, rangeMax, flags);
    }

    public static double plotHistogramV(String labelId, short[] values, int count, int bins, double barScale, int flags) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, flags);
    }

    public static double plotHistogramV(String labelId, int[] values, int count) {
        return nPlotHistogramV(labelId, values, count);
    }

    public static double plotHistogramV(String labelId, int[] values, int count, int bins) {
        return nPlotHistogramV(labelId, values, count, bins);
    }

    public static double plotHistogramV(String labelId, int[] values, int count, int bins, double barScale) {
        return nPlotHistogramV(labelId, values, count, bins, barScale);
    }

    public static double plotHistogramV(String labelId, int[] values, int count, int bins, double barScale, ImPlotRange range) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, range.min, range.max);
    }

    public static double plotHistogramV(String labelId, int[] values, int count, int bins, double barScale, double rangeMin, double rangeMax) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, rangeMin, rangeMax);
    }

    public static double plotHistogramV(String labelId, int[] values, int count, int bins, double barScale, ImPlotRange range, int flags) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, range.min, range.max, flags);
    }

    public static double plotHistogramV(String labelId, int[] values, int count, int bins, double barScale, double rangeMin, double rangeMax, int flags) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, rangeMin, rangeMax, flags);
    }

    public static double plotHistogramV(String labelId, int[] values, int count, int bins, double barScale, int flags) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, flags);
    }

    public static double plotHistogramV(String labelId, long[] values, int count) {
        return nPlotHistogramV(labelId, values, count);
    }

    public static double plotHistogramV(String labelId, long[] values, int count, int bins) {
        return nPlotHistogramV(labelId, values, count, bins);
    }

    public static double plotHistogramV(String labelId, long[] values, int count, int bins, double barScale) {
        return nPlotHistogramV(labelId, values, count, bins, barScale);
    }

    public static double plotHistogramV(String labelId, long[] values, int count, int bins, double barScale, ImPlotRange range) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, range.min, range.max);
    }

    public static double plotHistogramV(String labelId, long[] values, int count, int bins, double barScale, double rangeMin, double rangeMax) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, rangeMin, rangeMax);
    }

    public static double plotHistogramV(String labelId, long[] values, int count, int bins, double barScale, ImPlotRange range, int flags) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, range.min, range.max, flags);
    }

    public static double plotHistogramV(String labelId, long[] values, int count, int bins, double barScale, double rangeMin, double rangeMax, int flags) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, rangeMin, rangeMax, flags);
    }

    public static double plotHistogramV(String labelId, long[] values, int count, int bins, double barScale, int flags) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, flags);
    }

    public static double plotHistogramV(String labelId, float[] values, int count) {
        return nPlotHistogramV(labelId, values, count);
    }

    public static double plotHistogramV(String labelId, float[] values, int count, int bins) {
        return nPlotHistogramV(labelId, values, count, bins);
    }

    public static double plotHistogramV(String labelId, float[] values, int count, int bins, double barScale) {
        return nPlotHistogramV(labelId, values, count, bins, barScale);
    }

    public static double plotHistogramV(String labelId, float[] values, int count, int bins, double barScale, ImPlotRange range) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, range.min, range.max);
    }

    public static double plotHistogramV(String labelId, float[] values, int count, int bins, double barScale, double rangeMin, double rangeMax) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, rangeMin, rangeMax);
    }

    public static double plotHistogramV(String labelId, float[] values, int count, int bins, double barScale, ImPlotRange range, int flags) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, range.min, range.max, flags);
    }

    public static double plotHistogramV(String labelId, float[] values, int count, int bins, double barScale, double rangeMin, double rangeMax, int flags) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, rangeMin, rangeMax, flags);
    }

    public static double plotHistogramV(String labelId, float[] values, int count, int bins, double barScale, int flags) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, flags);
    }

    public static double plotHistogramV(String labelId, double[] values, int count) {
        return nPlotHistogramV(labelId, values, count);
    }

    public static double plotHistogramV(String labelId, double[] values, int count, int bins) {
        return nPlotHistogramV(labelId, values, count, bins);
    }

    public static double plotHistogramV(String labelId, double[] values, int count, int bins, double barScale) {
        return nPlotHistogramV(labelId, values, count, bins, barScale);
    }

    public static double plotHistogramV(String labelId, double[] values, int count, int bins, double barScale, ImPlotRange range) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, range.min, range.max);
    }

    public static double plotHistogramV(String labelId, double[] values, int count, int bins, double barScale, double rangeMin, double rangeMax) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, rangeMin, rangeMax);
    }

    public static double plotHistogramV(String labelId, double[] values, int count, int bins, double barScale, ImPlotRange range, int flags) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, range.min, range.max, flags);
    }

    public static double plotHistogramV(String labelId, double[] values, int count, int bins, double barScale, double rangeMin, double rangeMax, int flags) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, rangeMin, rangeMax, flags);
    }

    public static double plotHistogramV(String labelId, double[] values, int count, int bins, double barScale, int flags) {
        return nPlotHistogramV(labelId, values, count, bins, barScale, flags);
    }

    public static double plotHistogram2D(String labelId, short[] xs, short[] ys) {
        return nPlotHistogram2D(labelId, xs, ys);
    }

    public static double plotHistogram2D(String labelId, short[] xs, short[] ys, int xBins) {
        return nPlotHistogram2D(labelId, xs, ys, xBins);
    }

    public static double plotHistogram2D(String labelId, short[] xs, short[] ys, int xBins, int yBins) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins);
    }

    public static double plotHistogram2D(String labelId, short[] xs, short[] ys, int xBins, int yBins, ImPlotRect range) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, range.x.min, range.y.min, range.x.max, range.y.max);
    }

    public static double plotHistogram2D(String labelId, short[] xs, short[] ys, int xBins, int yBins, double rangeMinX, double rangeMinY, double rangeMaxX, double rangeMaxY) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, rangeMinX, rangeMinY, rangeMaxX, rangeMaxY);
    }

    public static double plotHistogram2D(String labelId, short[] xs, short[] ys, int xBins, int yBins, ImPlotRect range, int flags) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, range.x.min, range.y.min, range.x.max, range.y.max, flags);
    }

    public static double plotHistogram2D(String labelId, short[] xs, short[] ys, int xBins, int yBins, double rangeMinX, double rangeMinY, double rangeMaxX, double rangeMaxY, int flags) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, rangeMinX, rangeMinY, rangeMaxX, rangeMaxY, flags);
    }

    public static double plotHistogram2D(String labelId, short[] xs, short[] ys, int xBins, int yBins, int flags) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, flags);
    }

    public static double plotHistogram2D(String labelId, int[] xs, int[] ys) {
        return nPlotHistogram2D(labelId, xs, ys);
    }

    public static double plotHistogram2D(String labelId, int[] xs, int[] ys, int xBins) {
        return nPlotHistogram2D(labelId, xs, ys, xBins);
    }

    public static double plotHistogram2D(String labelId, int[] xs, int[] ys, int xBins, int yBins) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins);
    }

    public static double plotHistogram2D(String labelId, int[] xs, int[] ys, int xBins, int yBins, ImPlotRect range) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, range.x.min, range.y.min, range.x.max, range.y.max);
    }

    public static double plotHistogram2D(String labelId, int[] xs, int[] ys, int xBins, int yBins, double rangeMinX, double rangeMinY, double rangeMaxX, double rangeMaxY) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, rangeMinX, rangeMinY, rangeMaxX, rangeMaxY);
    }

    public static double plotHistogram2D(String labelId, int[] xs, int[] ys, int xBins, int yBins, ImPlotRect range, int flags) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, range.x.min, range.y.min, range.x.max, range.y.max, flags);
    }

    public static double plotHistogram2D(String labelId, int[] xs, int[] ys, int xBins, int yBins, double rangeMinX, double rangeMinY, double rangeMaxX, double rangeMaxY, int flags) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, rangeMinX, rangeMinY, rangeMaxX, rangeMaxY, flags);
    }

    public static double plotHistogram2D(String labelId, int[] xs, int[] ys, int xBins, int yBins, int flags) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, flags);
    }

    public static double plotHistogram2D(String labelId, long[] xs, long[] ys) {
        return nPlotHistogram2D(labelId, xs, ys);
    }

    public static double plotHistogram2D(String labelId, long[] xs, long[] ys, int xBins) {
        return nPlotHistogram2D(labelId, xs, ys, xBins);
    }

    public static double plotHistogram2D(String labelId, long[] xs, long[] ys, int xBins, int yBins) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins);
    }

    public static double plotHistogram2D(String labelId, long[] xs, long[] ys, int xBins, int yBins, ImPlotRect range) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, range.x.min, range.y.min, range.x.max, range.y.max);
    }

    public static double plotHistogram2D(String labelId, long[] xs, long[] ys, int xBins, int yBins, double rangeMinX, double rangeMinY, double rangeMaxX, double rangeMaxY) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, rangeMinX, rangeMinY, rangeMaxX, rangeMaxY);
    }

    public static double plotHistogram2D(String labelId, long[] xs, long[] ys, int xBins, int yBins, ImPlotRect range, int flags) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, range.x.min, range.y.min, range.x.max, range.y.max, flags);
    }

    public static double plotHistogram2D(String labelId, long[] xs, long[] ys, int xBins, int yBins, double rangeMinX, double rangeMinY, double rangeMaxX, double rangeMaxY, int flags) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, rangeMinX, rangeMinY, rangeMaxX, rangeMaxY, flags);
    }

    public static double plotHistogram2D(String labelId, long[] xs, long[] ys, int xBins, int yBins, int flags) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, flags);
    }

    public static double plotHistogram2D(String labelId, float[] xs, float[] ys) {
        return nPlotHistogram2D(labelId, xs, ys);
    }

    public static double plotHistogram2D(String labelId, float[] xs, float[] ys, int xBins) {
        return nPlotHistogram2D(labelId, xs, ys, xBins);
    }

    public static double plotHistogram2D(String labelId, float[] xs, float[] ys, int xBins, int yBins) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins);
    }

    public static double plotHistogram2D(String labelId, float[] xs, float[] ys, int xBins, int yBins, ImPlotRect range) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, range.x.min, range.y.min, range.x.max, range.y.max);
    }

    public static double plotHistogram2D(String labelId, float[] xs, float[] ys, int xBins, int yBins, double rangeMinX, double rangeMinY, double rangeMaxX, double rangeMaxY) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, rangeMinX, rangeMinY, rangeMaxX, rangeMaxY);
    }

    public static double plotHistogram2D(String labelId, float[] xs, float[] ys, int xBins, int yBins, ImPlotRect range, int flags) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, range.x.min, range.y.min, range.x.max, range.y.max, flags);
    }

    public static double plotHistogram2D(String labelId, float[] xs, float[] ys, int xBins, int yBins, double rangeMinX, double rangeMinY, double rangeMaxX, double rangeMaxY, int flags) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, rangeMinX, rangeMinY, rangeMaxX, rangeMaxY, flags);
    }

    public static double plotHistogram2D(String labelId, float[] xs, float[] ys, int xBins, int yBins, int flags) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, flags);
    }

    public static double plotHistogram2D(String labelId, double[] xs, double[] ys) {
        return nPlotHistogram2D(labelId, xs, ys);
    }

    public static double plotHistogram2D(String labelId, double[] xs, double[] ys, int xBins) {
        return nPlotHistogram2D(labelId, xs, ys, xBins);
    }

    public static double plotHistogram2D(String labelId, double[] xs, double[] ys, int xBins, int yBins) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins);
    }

    public static double plotHistogram2D(String labelId, double[] xs, double[] ys, int xBins, int yBins, ImPlotRect range) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, range.x.min, range.y.min, range.x.max, range.y.max);
    }

    public static double plotHistogram2D(String labelId, double[] xs, double[] ys, int xBins, int yBins, double rangeMinX, double rangeMinY, double rangeMaxX, double rangeMaxY) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, rangeMinX, rangeMinY, rangeMaxX, rangeMaxY);
    }

    public static double plotHistogram2D(String labelId, double[] xs, double[] ys, int xBins, int yBins, ImPlotRect range, int flags) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, range.x.min, range.y.min, range.x.max, range.y.max, flags);
    }

    public static double plotHistogram2D(String labelId, double[] xs, double[] ys, int xBins, int yBins, double rangeMinX, double rangeMinY, double rangeMaxX, double rangeMaxY, int flags) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, rangeMinX, rangeMinY, rangeMaxX, rangeMaxY, flags);
    }

    public static double plotHistogram2D(String labelId, double[] xs, double[] ys, int xBins, int yBins, int flags) {
        return nPlotHistogram2D(labelId, xs, ys, xBins, yBins, flags);
    }

    public static double plotHistogram2DV(String labelId, short[] xs, short[] ys, int count) {
        return nPlotHistogram2DV(labelId, xs, ys, count);
    }

    public static double plotHistogram2DV(String labelId, short[] xs, short[] ys, int count, int xBins) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins);
    }

    public static double plotHistogram2DV(String labelId, short[] xs, short[] ys, int count, int xBins, int yBins) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins);
    }

    public static double plotHistogram2DV(String labelId, short[] xs, short[] ys, int count, int xBins, int yBins, ImPlotRect range) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, range.x.min, range.y.min, range.x.max, range.y.max);
    }

    public static double plotHistogram2DV(String labelId, short[] xs, short[] ys, int count, int xBins, int yBins, double rangeMinX, double rangeMinY, double rangeMaxX, double rangeMaxY) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, rangeMinX, rangeMinY, rangeMaxX, rangeMaxY);
    }

    public static double plotHistogram2DV(String labelId, short[] xs, short[] ys, int count, int xBins, int yBins, ImPlotRect range, int flags) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, range.x.min, range.y.min, range.x.max, range.y.max, flags);
    }

    public static double plotHistogram2DV(String labelId, short[] xs, short[] ys, int count, int xBins, int yBins, double rangeMinX, double rangeMinY, double rangeMaxX, double rangeMaxY, int flags) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, rangeMinX, rangeMinY, rangeMaxX, rangeMaxY, flags);
    }

    public static double plotHistogram2DV(String labelId, short[] xs, short[] ys, int count, int xBins, int yBins, int flags) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, flags);
    }

    public static double plotHistogram2DV(String labelId, int[] xs, int[] ys, int count) {
        return nPlotHistogram2DV(labelId, xs, ys, count);
    }

    public static double plotHistogram2DV(String labelId, int[] xs, int[] ys, int count, int xBins) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins);
    }

    public static double plotHistogram2DV(String labelId, int[] xs, int[] ys, int count, int xBins, int yBins) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins);
    }

    public static double plotHistogram2DV(String labelId, int[] xs, int[] ys, int count, int xBins, int yBins, ImPlotRect range) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, range.x.min, range.y.min, range.x.max, range.y.max);
    }

    public static double plotHistogram2DV(String labelId, int[] xs, int[] ys, int count, int xBins, int yBins, double rangeMinX, double rangeMinY, double rangeMaxX, double rangeMaxY) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, rangeMinX, rangeMinY, rangeMaxX, rangeMaxY);
    }

    public static double plotHistogram2DV(String labelId, int[] xs, int[] ys, int count, int xBins, int yBins, ImPlotRect range, int flags) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, range.x.min, range.y.min, range.x.max, range.y.max, flags);
    }

    public static double plotHistogram2DV(String labelId, int[] xs, int[] ys, int count, int xBins, int yBins, double rangeMinX, double rangeMinY, double rangeMaxX, double rangeMaxY, int flags) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, rangeMinX, rangeMinY, rangeMaxX, rangeMaxY, flags);
    }

    public static double plotHistogram2DV(String labelId, int[] xs, int[] ys, int count, int xBins, int yBins, int flags) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, flags);
    }

    public static double plotHistogram2DV(String labelId, long[] xs, long[] ys, int count) {
        return nPlotHistogram2DV(labelId, xs, ys, count);
    }

    public static double plotHistogram2DV(String labelId, long[] xs, long[] ys, int count, int xBins) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins);
    }

    public static double plotHistogram2DV(String labelId, long[] xs, long[] ys, int count, int xBins, int yBins) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins);
    }

    public static double plotHistogram2DV(String labelId, long[] xs, long[] ys, int count, int xBins, int yBins, ImPlotRect range) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, range.x.min, range.y.min, range.x.max, range.y.max);
    }

    public static double plotHistogram2DV(String labelId, long[] xs, long[] ys, int count, int xBins, int yBins, double rangeMinX, double rangeMinY, double rangeMaxX, double rangeMaxY) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, rangeMinX, rangeMinY, rangeMaxX, rangeMaxY);
    }

    public static double plotHistogram2DV(String labelId, long[] xs, long[] ys, int count, int xBins, int yBins, ImPlotRect range, int flags) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, range.x.min, range.y.min, range.x.max, range.y.max, flags);
    }

    public static double plotHistogram2DV(String labelId, long[] xs, long[] ys, int count, int xBins, int yBins, double rangeMinX, double rangeMinY, double rangeMaxX, double rangeMaxY, int flags) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, rangeMinX, rangeMinY, rangeMaxX, rangeMaxY, flags);
    }

    public static double plotHistogram2DV(String labelId, long[] xs, long[] ys, int count, int xBins, int yBins, int flags) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, flags);
    }

    public static double plotHistogram2DV(String labelId, float[] xs, float[] ys, int count) {
        return nPlotHistogram2DV(labelId, xs, ys, count);
    }

    public static double plotHistogram2DV(String labelId, float[] xs, float[] ys, int count, int xBins) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins);
    }

    public static double plotHistogram2DV(String labelId, float[] xs, float[] ys, int count, int xBins, int yBins) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins);
    }

    public static double plotHistogram2DV(String labelId, float[] xs, float[] ys, int count, int xBins, int yBins, ImPlotRect range) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, range.x.min, range.y.min, range.x.max, range.y.max);
    }

    public static double plotHistogram2DV(String labelId, float[] xs, float[] ys, int count, int xBins, int yBins, double rangeMinX, double rangeMinY, double rangeMaxX, double rangeMaxY) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, rangeMinX, rangeMinY, rangeMaxX, rangeMaxY);
    }

    public static double plotHistogram2DV(String labelId, float[] xs, float[] ys, int count, int xBins, int yBins, ImPlotRect range, int flags) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, range.x.min, range.y.min, range.x.max, range.y.max, flags);
    }

    public static double plotHistogram2DV(String labelId, float[] xs, float[] ys, int count, int xBins, int yBins, double rangeMinX, double rangeMinY, double rangeMaxX, double rangeMaxY, int flags) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, rangeMinX, rangeMinY, rangeMaxX, rangeMaxY, flags);
    }

    public static double plotHistogram2DV(String labelId, float[] xs, float[] ys, int count, int xBins, int yBins, int flags) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, flags);
    }

    public static double plotHistogram2DV(String labelId, double[] xs, double[] ys, int count) {
        return nPlotHistogram2DV(labelId, xs, ys, count);
    }

    public static double plotHistogram2DV(String labelId, double[] xs, double[] ys, int count, int xBins) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins);
    }

    public static double plotHistogram2DV(String labelId, double[] xs, double[] ys, int count, int xBins, int yBins) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins);
    }

    public static double plotHistogram2DV(String labelId, double[] xs, double[] ys, int count, int xBins, int yBins, ImPlotRect range) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, range.x.min, range.y.min, range.x.max, range.y.max);
    }

    public static double plotHistogram2DV(String labelId, double[] xs, double[] ys, int count, int xBins, int yBins, double rangeMinX, double rangeMinY, double rangeMaxX, double rangeMaxY) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, rangeMinX, rangeMinY, rangeMaxX, rangeMaxY);
    }

    public static double plotHistogram2DV(String labelId, double[] xs, double[] ys, int count, int xBins, int yBins, ImPlotRect range, int flags) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, range.x.min, range.y.min, range.x.max, range.y.max, flags);
    }

    public static double plotHistogram2DV(String labelId, double[] xs, double[] ys, int count, int xBins, int yBins, double rangeMinX, double rangeMinY, double rangeMaxX, double rangeMaxY, int flags) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, rangeMinX, rangeMinY, rangeMaxX, rangeMaxY, flags);
    }

    public static double plotHistogram2DV(String labelId, double[] xs, double[] ys, int count, int xBins, int yBins, int flags) {
        return nPlotHistogram2DV(labelId, xs, ys, count, xBins, yBins, flags);
    }

    public static void plotDigital(String labelId, short[] xs, short[] ys) {
        nPlotDigital(labelId, xs, ys);
    }

    public static void plotDigital(String labelId, short[] xs, short[] ys, int flags) {
        nPlotDigital(labelId, xs, ys, flags);
    }

    public static void plotDigital(String labelId, short[] xs, short[] ys, int flags, int offset) {
        nPlotDigital(labelId, xs, ys, flags, offset);
    }

    public static void plotDigital(String labelId, int[] xs, int[] ys) {
        nPlotDigital(labelId, xs, ys);
    }

    public static void plotDigital(String labelId, int[] xs, int[] ys, int flags) {
        nPlotDigital(labelId, xs, ys, flags);
    }

    public static void plotDigital(String labelId, int[] xs, int[] ys, int flags, int offset) {
        nPlotDigital(labelId, xs, ys, flags, offset);
    }

    public static void plotDigital(String labelId, long[] xs, long[] ys) {
        nPlotDigital(labelId, xs, ys);
    }

    public static void plotDigital(String labelId, long[] xs, long[] ys, int flags) {
        nPlotDigital(labelId, xs, ys, flags);
    }

    public static void plotDigital(String labelId, long[] xs, long[] ys, int flags, int offset) {
        nPlotDigital(labelId, xs, ys, flags, offset);
    }

    public static void plotDigital(String labelId, float[] xs, float[] ys) {
        nPlotDigital(labelId, xs, ys);
    }

    public static void plotDigital(String labelId, float[] xs, float[] ys, int flags) {
        nPlotDigital(labelId, xs, ys, flags);
    }

    public static void plotDigital(String labelId, float[] xs, float[] ys, int flags, int offset) {
        nPlotDigital(labelId, xs, ys, flags, offset);
    }

    public static void plotDigital(String labelId, double[] xs, double[] ys) {
        nPlotDigital(labelId, xs, ys);
    }

    public static void plotDigital(String labelId, double[] xs, double[] ys, int flags) {
        nPlotDigital(labelId, xs, ys, flags);
    }

    public static void plotDigital(String labelId, double[] xs, double[] ys, int flags, int offset) {
        nPlotDigital(labelId, xs, ys, flags, offset);
    }

    public static void plotDigitalV(String labelId, short[] xs, short[] ys, int count) {
        nPlotDigitalV(labelId, xs, ys, count);
    }

    public static void plotDigitalV(String labelId, short[] xs, short[] ys, int count, int flags) {
        nPlotDigitalV(labelId, xs, ys, count, flags);
    }

    public static void plotDigitalV(String labelId, short[] xs, short[] ys, int count, int flags, int offset) {
        nPlotDigitalV(labelId, xs, ys, count, flags, offset);
    }

    public static void plotDigitalV(String labelId, int[] xs, int[] ys, int count) {
        nPlotDigitalV(labelId, xs, ys, count);
    }

    public static void plotDigitalV(String labelId, int[] xs, int[] ys, int count, int flags) {
        nPlotDigitalV(labelId, xs, ys, count, flags);
    }

    public static void plotDigitalV(String labelId, int[] xs, int[] ys, int count, int flags, int offset) {
        nPlotDigitalV(labelId, xs, ys, count, flags, offset);
    }

    public static void plotDigitalV(String labelId, long[] xs, long[] ys, int count) {
        nPlotDigitalV(labelId, xs, ys, count);
    }

    public static void plotDigitalV(String labelId, long[] xs, long[] ys, int count, int flags) {
        nPlotDigitalV(labelId, xs, ys, count, flags);
    }

    public static void plotDigitalV(String labelId, long[] xs, long[] ys, int count, int flags, int offset) {
        nPlotDigitalV(labelId, xs, ys, count, flags, offset);
    }

    public static void plotDigitalV(String labelId, float[] xs, float[] ys, int count) {
        nPlotDigitalV(labelId, xs, ys, count);
    }

    public static void plotDigitalV(String labelId, float[] xs, float[] ys, int count, int flags) {
        nPlotDigitalV(labelId, xs, ys, count, flags);
    }

    public static void plotDigitalV(String labelId, float[] xs, float[] ys, int count, int flags, int offset) {
        nPlotDigitalV(labelId, xs, ys, count, flags, offset);
    }

    public static void plotDigitalV(String labelId, double[] xs, double[] ys, int count) {
        nPlotDigitalV(labelId, xs, ys, count);
    }

    public static void plotDigitalV(String labelId, double[] xs, double[] ys, int count, int flags) {
        nPlotDigitalV(labelId, xs, ys, count, flags);
    }

    public static void plotDigitalV(String labelId, double[] xs, double[] ys, int count, int flags, int offset) {
        nPlotDigitalV(labelId, xs, ys, count, flags, offset);
    }

    public static void plotImage(String labelId, long userTextureId, ImPlotPoint boundsMin, ImPlotPoint boundsMax) {
        nPlotImage(labelId, userTextureId, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y);
    }

    public static void plotImage(String labelId, long userTextureId, double boundsMinX, double boundsMinY, double boundsMaxX, double boundsMaxY) {
        nPlotImage(labelId, userTextureId, boundsMinX, boundsMinY, boundsMaxX, boundsMaxY);
    }

    public static void plotImage(String labelId, long userTextureId, ImPlotPoint boundsMin, ImPlotPoint boundsMax, ImVec2 uv0) {
        nPlotImage(labelId, userTextureId, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y, uv0.x, uv0.y);
    }

    public static void plotImage(String labelId, long userTextureId, ImPlotPoint boundsMin, ImPlotPoint boundsMax, float uv0X, float uv0Y) {
        nPlotImage(labelId, userTextureId, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y, uv0X, uv0Y);
    }

    public static void plotImage(String labelId, long userTextureId, double boundsMinX, double boundsMinY, double boundsMaxX, double boundsMaxY, ImVec2 uv0) {
        nPlotImage(labelId, userTextureId, boundsMinX, boundsMinY, boundsMaxX, boundsMaxY, uv0.x, uv0.y);
    }

    public static void plotImage(String labelId, long userTextureId, ImPlotPoint boundsMin, ImPlotPoint boundsMax, ImVec2 uv0, ImVec2 uv1) {
        nPlotImage(labelId, userTextureId, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y, uv0.x, uv0.y, uv1.x, uv1.y);
    }

    public static void plotImage(String labelId, long userTextureId, ImPlotPoint boundsMin, ImPlotPoint boundsMax, float uv0X, float uv0Y, float uv1X, float uv1Y) {
        nPlotImage(labelId, userTextureId, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y, uv0X, uv0Y, uv1X, uv1Y);
    }

    public static void plotImage(String labelId, long userTextureId, double boundsMinX, double boundsMinY, double boundsMaxX, double boundsMaxY, ImVec2 uv0, ImVec2 uv1) {
        nPlotImage(labelId, userTextureId, boundsMinX, boundsMinY, boundsMaxX, boundsMaxY, uv0.x, uv0.y, uv1.x, uv1.y);
    }

    public static void plotImage(String labelId, long userTextureId, ImPlotPoint boundsMin, ImPlotPoint boundsMax, ImVec2 uv0, ImVec2 uv1, ImVec4 tintCol) {
        nPlotImage(labelId, userTextureId, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y, uv0.x, uv0.y, uv1.x, uv1.y, tintCol.x, tintCol.y, tintCol.z, tintCol.w);
    }

    public static void plotImage(String labelId, long userTextureId, ImPlotPoint boundsMin, ImPlotPoint boundsMax, float uv0X, float uv0Y, float uv1X, float uv1Y, float tintColX, float tintColY, float tintColZ, float tintColW) {
        nPlotImage(labelId, userTextureId, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y, uv0X, uv0Y, uv1X, uv1Y, tintColX, tintColY, tintColZ, tintColW);
    }

    public static void plotImage(String labelId, long userTextureId, double boundsMinX, double boundsMinY, double boundsMaxX, double boundsMaxY, ImVec2 uv0, ImVec2 uv1, ImVec4 tintCol) {
        nPlotImage(labelId, userTextureId, boundsMinX, boundsMinY, boundsMaxX, boundsMaxY, uv0.x, uv0.y, uv1.x, uv1.y, tintCol.x, tintCol.y, tintCol.z, tintCol.w);
    }

    public static void plotImage(String labelId, long userTextureId, ImPlotPoint boundsMin, ImPlotPoint boundsMax, ImVec2 uv0, ImVec2 uv1, ImVec4 tintCol, int flags) {
        nPlotImage(labelId, userTextureId, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y, uv0.x, uv0.y, uv1.x, uv1.y, tintCol.x, tintCol.y, tintCol.z, tintCol.w, flags);
    }

    public static void plotImage(String labelId, long userTextureId, ImPlotPoint boundsMin, ImPlotPoint boundsMax, float uv0X, float uv0Y, float uv1X, float uv1Y, float tintColX, float tintColY, float tintColZ, float tintColW, int flags) {
        nPlotImage(labelId, userTextureId, boundsMin.x, boundsMin.y, boundsMax.x, boundsMax.y, uv0X, uv0Y, uv1X, uv1Y, tintColX, tintColY, tintColZ, tintColW, flags);
    }

    public static void plotImage(String labelId, long userTextureId, double boundsMinX, double boundsMinY, double boundsMaxX, double boundsMaxY, ImVec2 uv0, ImVec2 uv1, ImVec4 tintCol, int flags) {
        nPlotImage(labelId, userTextureId, boundsMinX, boundsMinY, boundsMaxX, boundsMaxY, uv0.x, uv0.y, uv1.x, uv1.y, tintCol.x, tintCol.y, tintCol.z, tintCol.w, flags);
    }

    public static void plotText(String text, double x, double y) {
        nPlotText(text, x, y);
    }

    public static void plotText(String text, double x, double y, ImVec2 pixOffset) {
        nPlotText(text, x, y, pixOffset.x, pixOffset.y);
    }

    public static void plotText(String text, double x, double y, float pixOffsetX, float pixOffsetY) {
        nPlotText(text, x, y, pixOffsetX, pixOffsetY);
    }

    public static void plotText(String text, double x, double y, ImVec2 pixOffset, int flags) {
        nPlotText(text, x, y, pixOffset.x, pixOffset.y, flags);
    }

    public static void plotText(String text, double x, double y, float pixOffsetX, float pixOffsetY, int flags) {
        nPlotText(text, x, y, pixOffsetX, pixOffsetY, flags);
    }

    public static void plotText(String text, double x, double y, int flags) {
        nPlotText(text, x, y, flags);
    }

    public static void plotDummy(String labelID) {
        nPlotDummy(labelID);
    }

    public static void plotDummy(String labelID, int flags) {
        nPlotDummy(labelID, flags);
    }

    public static boolean dragPoint(int id, ImDouble x, ImDouble y, ImVec4 col) {
        return nDragPoint(id, x != null ? x.getData() : null, y != null ? y.getData() : null, col.x, col.y, col.z, col.w);
    }

    public static boolean dragPoint(int id, ImDouble x, ImDouble y, float colX, float colY, float colZ, float colW) {
        return nDragPoint(id, x != null ? x.getData() : null, y != null ? y.getData() : null, colX, colY, colZ, colW);
    }

    public static boolean dragPoint(int id, ImDouble x, ImDouble y, ImVec4 col, float size) {
        return nDragPoint(id, x != null ? x.getData() : null, y != null ? y.getData() : null, col.x, col.y, col.z, col.w, size);
    }

    public static boolean dragPoint(int id, ImDouble x, ImDouble y, float colX, float colY, float colZ, float colW, float size) {
        return nDragPoint(id, x != null ? x.getData() : null, y != null ? y.getData() : null, colX, colY, colZ, colW, size);
    }

    public static boolean dragPoint(int id, ImDouble x, ImDouble y, ImVec4 col, float size, int flags) {
        return nDragPoint(id, x != null ? x.getData() : null, y != null ? y.getData() : null, col.x, col.y, col.z, col.w, size, flags);
    }

    public static boolean dragPoint(int id, ImDouble x, ImDouble y, float colX, float colY, float colZ, float colW, float size, int flags) {
        return nDragPoint(id, x != null ? x.getData() : null, y != null ? y.getData() : null, colX, colY, colZ, colW, size, flags);
    }

    public static boolean dragPoint(int id, ImDouble x, ImDouble y, ImVec4 col, int flags) {
        return nDragPoint(id, x != null ? x.getData() : null, y != null ? y.getData() : null, col.x, col.y, col.z, col.w, flags);
    }

    public static boolean dragPoint(int id, ImDouble x, ImDouble y, float colX, float colY, float colZ, float colW, int flags) {
        return nDragPoint(id, x != null ? x.getData() : null, y != null ? y.getData() : null, colX, colY, colZ, colW, flags);
    }

    public static boolean dragLineX(int id, ImDouble x, ImVec4 col) {
        return nDragLineX(id, x != null ? x.getData() : null, col.x, col.y, col.z, col.w);
    }

    public static boolean dragLineX(int id, ImDouble x, float colX, float colY, float colZ, float colW) {
        return nDragLineX(id, x != null ? x.getData() : null, colX, colY, colZ, colW);
    }

    public static boolean dragLineX(int id, ImDouble x, ImVec4 col, float thickness) {
        return nDragLineX(id, x != null ? x.getData() : null, col.x, col.y, col.z, col.w, thickness);
    }

    public static boolean dragLineX(int id, ImDouble x, float colX, float colY, float colZ, float colW, float thickness) {
        return nDragLineX(id, x != null ? x.getData() : null, colX, colY, colZ, colW, thickness);
    }

    public static boolean dragLineX(int id, ImDouble x, ImVec4 col, float thickness, int flags) {
        return nDragLineX(id, x != null ? x.getData() : null, col.x, col.y, col.z, col.w, thickness, flags);
    }

    public static boolean dragLineX(int id, ImDouble x, float colX, float colY, float colZ, float colW, float thickness, int flags) {
        return nDragLineX(id, x != null ? x.getData() : null, colX, colY, colZ, colW, thickness, flags);
    }

    public static boolean dragLineX(int id, ImDouble x, ImVec4 col, int flags) {
        return nDragLineX(id, x != null ? x.getData() : null, col.x, col.y, col.z, col.w, flags);
    }

    public static boolean dragLineX(int id, ImDouble x, float colX, float colY, float colZ, float colW, int flags) {
        return nDragLineX(id, x != null ? x.getData() : null, colX, colY, colZ, colW, flags);
    }

    public static boolean dragLineY(int id, ImDouble y, ImVec4 col) {
        return nDragLineY(id, y != null ? y.getData() : null, col.x, col.y, col.z, col.w);
    }

    public static boolean dragLineY(int id, ImDouble y, float colX, float colY, float colZ, float colW) {
        return nDragLineY(id, y != null ? y.getData() : null, colX, colY, colZ, colW);
    }

    public static boolean dragLineY(int id, ImDouble y, ImVec4 col, float thickness) {
        return nDragLineY(id, y != null ? y.getData() : null, col.x, col.y, col.z, col.w, thickness);
    }

    public static boolean dragLineY(int id, ImDouble y, float colX, float colY, float colZ, float colW, float thickness) {
        return nDragLineY(id, y != null ? y.getData() : null, colX, colY, colZ, colW, thickness);
    }

    public static boolean dragLineY(int id, ImDouble y, ImVec4 col, float thickness, int flags) {
        return nDragLineY(id, y != null ? y.getData() : null, col.x, col.y, col.z, col.w, thickness, flags);
    }

    public static boolean dragLineY(int id, ImDouble y, float colX, float colY, float colZ, float colW, float thickness, int flags) {
        return nDragLineY(id, y != null ? y.getData() : null, colX, colY, colZ, colW, thickness, flags);
    }

    public static boolean dragLineY(int id, ImDouble y, ImVec4 col, int flags) {
        return nDragLineY(id, y != null ? y.getData() : null, col.x, col.y, col.z, col.w, flags);
    }

    public static boolean dragLineY(int id, ImDouble y, float colX, float colY, float colZ, float colW, int flags) {
        return nDragLineY(id, y != null ? y.getData() : null, colX, colY, colZ, colW, flags);
    }

    public static boolean dragRect(int id, ImDouble xMin, ImDouble yMin, ImDouble xMax, ImDouble yMax, ImVec4 col) {
        return nDragRect(id, xMin != null ? xMin.getData() : null, yMin != null ? yMin.getData() : null, xMax != null ? xMax.getData() : null, yMax != null ? yMax.getData() : null, col.x, col.y, col.z, col.w);
    }

    public static boolean dragRect(int id, ImDouble xMin, ImDouble yMin, ImDouble xMax, ImDouble yMax, float colX, float colY, float colZ, float colW) {
        return nDragRect(id, xMin != null ? xMin.getData() : null, yMin != null ? yMin.getData() : null, xMax != null ? xMax.getData() : null, yMax != null ? yMax.getData() : null, colX, colY, colZ, colW);
    }

    public static boolean dragRect(int id, ImDouble xMin, ImDouble yMin, ImDouble xMax, ImDouble yMax, ImVec4 col, int flags) {
        return nDragRect(id, xMin != null ? xMin.getData() : null, yMin != null ? yMin.getData() : null, xMax != null ? xMax.getData() : null, yMax != null ? yMax.getData() : null, col.x, col.y, col.z, col.w, flags);
    }

    public static boolean dragRect(int id, ImDouble xMin, ImDouble yMin, ImDouble xMax, ImDouble yMax, float colX, float colY, float colZ, float colW, int flags) {
        return nDragRect(id, xMin != null ? xMin.getData() : null, yMin != null ? yMin.getData() : null, xMax != null ? xMax.getData() : null, yMax != null ? yMax.getData() : null, colX, colY, colZ, colW, flags);
    }

    public static void annotation(double x, double y, ImVec4 col, ImVec2 pixOffset, boolean clamp) {
        nAnnotation(x, y, col.x, col.y, col.z, col.w, pixOffset.x, pixOffset.y, clamp);
    }

    public static void annotation(double x, double y, float colX, float colY, float colZ, float colW, float pixOffsetX, float pixOffsetY, boolean clamp) {
        nAnnotation(x, y, colX, colY, colZ, colW, pixOffsetX, pixOffsetY, clamp);
    }

    public static void annotation(double x, double y, ImVec4 col, ImVec2 pixOffset, boolean clamp, boolean round) {
        nAnnotation(x, y, col.x, col.y, col.z, col.w, pixOffset.x, pixOffset.y, clamp, round);
    }

    public static void annotation(double x, double y, float colX, float colY, float colZ, float colW, float pixOffsetX, float pixOffsetY, boolean clamp, boolean round) {
        nAnnotation(x, y, colX, colY, colZ, colW, pixOffsetX, pixOffsetY, clamp, round);
    }

    public static void annotation(double x, double y, ImVec4 col, ImVec2 pixOffset, boolean clamp, String fmt) {
        nAnnotation(x, y, col.x, col.y, col.z, col.w, pixOffset.x, pixOffset.y, clamp, fmt);
    }

    public static void annotation(double x, double y, float colX, float colY, float colZ, float colW, float pixOffsetX, float pixOffsetY, boolean clamp, String fmt) {
        nAnnotation(x, y, colX, colY, colZ, colW, pixOffsetX, pixOffsetY, clamp, fmt);
    }

    public static void tagX(double x, ImVec4 col) {
        nTagX(x, col.x, col.y, col.z, col.w);
    }

    public static void tagX(double x, float colX, float colY, float colZ, float colW) {
        nTagX(x, colX, colY, colZ, colW);
    }

    public static void tagX(double x, ImVec4 col, boolean round) {
        nTagX(x, col.x, col.y, col.z, col.w, round);
    }

    public static void tagX(double x, float colX, float colY, float colZ, float colW, boolean round) {
        nTagX(x, colX, colY, colZ, colW, round);
    }

    public static void tagX(double x, ImVec4 col, String fmt) {
        nTagX(x, col.x, col.y, col.z, col.w, fmt);
    }

    public static void tagX(double x, float colX, float colY, float colZ, float colW, String fmt) {
        nTagX(x, colX, colY, colZ, colW, fmt);
    }

    public static void tagY(double y, ImVec4 col) {
        nTagY(y, col.x, col.y, col.z, col.w);
    }

    public static void tagY(double y, float colX, float colY, float colZ, float colW) {
        nTagY(y, colX, colY, colZ, colW);
    }

    public static void tagY(double y, ImVec4 col, boolean round) {
        nTagY(y, col.x, col.y, col.z, col.w, round);
    }

    public static void tagY(double y, float colX, float colY, float colZ, float colW, boolean round) {
        nTagY(y, colX, colY, colZ, colW, round);
    }

    public static void tagY(double y, ImVec4 col, String fmt) {
        nTagY(y, col.x, col.y, col.z, col.w, fmt);
    }

    public static void tagY(double y, float colX, float colY, float colZ, float colW, String fmt) {
        nTagY(y, colX, colY, colZ, colW, fmt);
    }

    public static void setAxis(int axis) {
        nSetAxis(axis);
    }

    public static void setAxes(int xAxis, int yAxis) {
        nSetAxes(xAxis, yAxis);
    }

    public static ImPlotPoint pixelsToPlot(ImVec2 pix) {
        ImPlotPoint dst = new ImPlotPoint();
        nPixelsToPlot(dst, pix.x, pix.y);
        return dst;
    }

    public static ImPlotPoint pixelsToPlot(float pixX, float pixY) {
        ImPlotPoint dst = new ImPlotPoint();
        nPixelsToPlot(dst, pixX, pixY);
        return dst;
    }

    public static void pixelsToPlot(ImPlotPoint dst, ImVec2 pix) {
        nPixelsToPlot(dst, pix.x, pix.y);
    }

    public static void pixelsToPlot(ImPlotPoint dst, float pixX, float pixY) {
        nPixelsToPlot(dst, pixX, pixY);
    }

    public static ImPlotPoint pixelsToPlot(ImVec2 pix, int xAxis) {
        ImPlotPoint dst = new ImPlotPoint();
        nPixelsToPlot(dst, pix.x, pix.y, xAxis);
        return dst;
    }

    public static ImPlotPoint pixelsToPlot(float pixX, float pixY, int xAxis) {
        ImPlotPoint dst = new ImPlotPoint();
        nPixelsToPlot(dst, pixX, pixY, xAxis);
        return dst;
    }

    public static void pixelsToPlot(ImPlotPoint dst, ImVec2 pix, int xAxis) {
        nPixelsToPlot(dst, pix.x, pix.y, xAxis);
    }

    public static void pixelsToPlot(ImPlotPoint dst, float pixX, float pixY, int xAxis) {
        nPixelsToPlot(dst, pixX, pixY, xAxis);
    }

    public static ImPlotPoint pixelsToPlot(ImVec2 pix, int xAxis, int yAxis) {
        ImPlotPoint dst = new ImPlotPoint();
        nPixelsToPlot(dst, pix.x, pix.y, xAxis, yAxis);
        return dst;
    }

    public static ImPlotPoint pixelsToPlot(float pixX, float pixY, int xAxis, int yAxis) {
        ImPlotPoint dst = new ImPlotPoint();
        nPixelsToPlot(dst, pixX, pixY, xAxis, yAxis);
        return dst;
    }

    public static void pixelsToPlot(ImPlotPoint dst, ImVec2 pix, int xAxis, int yAxis) {
        nPixelsToPlot(dst, pix.x, pix.y, xAxis, yAxis);
    }

    public static void pixelsToPlot(ImPlotPoint dst, float pixX, float pixY, int xAxis, int yAxis) {
        nPixelsToPlot(dst, pixX, pixY, xAxis, yAxis);
    }

    public static ImVec2 plotToPixels(ImPlotPoint plt) {
        ImVec2 dst = new ImVec2();
        nPlotToPixels(dst, plt.x, plt.y);
        return dst;
    }

    public static ImVec2 plotToPixels(double pltX, double pltY) {
        ImVec2 dst = new ImVec2();
        nPlotToPixels(dst, pltX, pltY);
        return dst;
    }

    public static float plotToPixelsX(ImPlotPoint plt) {
        return nPlotToPixelsX(plt.x, plt.y);
    }

    public static float plotToPixelsY(ImPlotPoint plt) {
        return nPlotToPixelsY(plt.x, plt.y);
    }

    public static void plotToPixels(ImVec2 dst, ImPlotPoint plt) {
        nPlotToPixels(dst, plt.x, plt.y);
    }

    public static void plotToPixels(ImVec2 dst, double pltX, double pltY) {
        nPlotToPixels(dst, pltX, pltY);
    }

    public static ImVec2 plotToPixels(ImPlotPoint plt, int xAxis) {
        ImVec2 dst = new ImVec2();
        nPlotToPixels(dst, plt.x, plt.y, xAxis);
        return dst;
    }

    public static ImVec2 plotToPixels(double pltX, double pltY, int xAxis) {
        ImVec2 dst = new ImVec2();
        nPlotToPixels(dst, pltX, pltY, xAxis);
        return dst;
    }

    public static float plotToPixelsX(ImPlotPoint plt, int xAxis) {
        return nPlotToPixelsX(plt.x, plt.y, xAxis);
    }

    public static float plotToPixelsY(ImPlotPoint plt, int xAxis) {
        return nPlotToPixelsY(plt.x, plt.y, xAxis);
    }

    public static void plotToPixels(ImVec2 dst, ImPlotPoint plt, int xAxis) {
        nPlotToPixels(dst, plt.x, plt.y, xAxis);
    }

    public static void plotToPixels(ImVec2 dst, double pltX, double pltY, int xAxis) {
        nPlotToPixels(dst, pltX, pltY, xAxis);
    }

    public static ImVec2 plotToPixels(ImPlotPoint plt, int xAxis, int yAxis) {
        ImVec2 dst = new ImVec2();
        nPlotToPixels(dst, plt.x, plt.y, xAxis, yAxis);
        return dst;
    }

    public static ImVec2 plotToPixels(double pltX, double pltY, int xAxis, int yAxis) {
        ImVec2 dst = new ImVec2();
        nPlotToPixels(dst, pltX, pltY, xAxis, yAxis);
        return dst;
    }

    public static float plotToPixelsX(ImPlotPoint plt, int xAxis, int yAxis) {
        return nPlotToPixelsX(plt.x, plt.y, xAxis, yAxis);
    }

    public static float plotToPixelsY(ImPlotPoint plt, int xAxis, int yAxis) {
        return nPlotToPixelsY(plt.x, plt.y, xAxis, yAxis);
    }

    public static void plotToPixels(ImVec2 dst, ImPlotPoint plt, int xAxis, int yAxis) {
        nPlotToPixels(dst, plt.x, plt.y, xAxis, yAxis);
    }

    public static void plotToPixels(ImVec2 dst, double pltX, double pltY, int xAxis, int yAxis) {
        nPlotToPixels(dst, pltX, pltY, xAxis, yAxis);
    }

    public static ImVec2 getPlotPos() {
        ImVec2 dst = new ImVec2();
        nGetPlotPos(dst);
        return dst;
    }

    public static float getPlotPosX() {
        return nGetPlotPosX();
    }

    public static float getPlotPosY() {
        return nGetPlotPosY();
    }

    public static void getPlotPos(ImVec2 dst) {
        nGetPlotPos(dst);
    }

    public static ImVec2 getPlotSize() {
        ImVec2 dst = new ImVec2();
        nGetPlotSize(dst);
        return dst;
    }

    public static float getPlotSizeX() {
        return nGetPlotSizeX();
    }

    public static float getPlotSizeY() {
        return nGetPlotSizeY();
    }

    public static void getPlotSize(ImVec2 dst) {
        nGetPlotSize(dst);
    }

    public static ImPlotPoint getPlotMousePos() {
        ImPlotPoint dst = new ImPlotPoint();
        nGetPlotMousePos(dst);
        return dst;
    }

    public static void getPlotMousePos(ImPlotPoint dst) {
        nGetPlotMousePos(dst);
    }

    public static ImPlotPoint getPlotMousePos(int xAxis) {
        ImPlotPoint dst = new ImPlotPoint();
        nGetPlotMousePos(dst, xAxis);
        return dst;
    }

    public static void getPlotMousePos(ImPlotPoint dst, int xAxis) {
        nGetPlotMousePos(dst, xAxis);
    }

    public static ImPlotPoint getPlotMousePos(int xAxis, int yAxis) {
        ImPlotPoint dst = new ImPlotPoint();
        nGetPlotMousePos(dst, xAxis, yAxis);
        return dst;
    }

    public static void getPlotMousePos(ImPlotPoint dst, int xAxis, int yAxis) {
        nGetPlotMousePos(dst, xAxis, yAxis);
    }

    public static ImPlotRect getPlotLimits() {
        ImPlotRect dst = new ImPlotRect();
        nGetPlotLimits(dst);
        return dst;
    }

    public static void getPlotLimits(ImPlotRect dst) {
        nGetPlotLimits(dst);
    }

    public static ImPlotRect getPlotLimits(int xAxis) {
        ImPlotRect dst = new ImPlotRect();
        nGetPlotLimits(dst, xAxis);
        return dst;
    }

    public static void getPlotLimits(ImPlotRect dst, int xAxis) {
        nGetPlotLimits(dst, xAxis);
    }

    public static ImPlotRect getPlotLimits(int xAxis, int yAxis) {
        ImPlotRect dst = new ImPlotRect();
        nGetPlotLimits(dst, xAxis, yAxis);
        return dst;
    }

    public static void getPlotLimits(ImPlotRect dst, int xAxis, int yAxis) {
        nGetPlotLimits(dst, xAxis, yAxis);
    }

    public static boolean isPlotHovered() {
        return nIsPlotHovered();
    }

    public static boolean isAxisHovered(int axis) {
        return nIsAxisHovered(axis);
    }

    public static boolean isSubplotsHovered() {
        return nIsSubplotsHovered();
    }

    public static boolean isPlotSelected() {
        return nIsPlotSelected();
    }

    public static ImPlotRect getPlotSelection() {
        ImPlotRect dst = new ImPlotRect();
        nGetPlotSelection(dst);
        return dst;
    }

    public static void getPlotSelection(ImPlotRect dst) {
        nGetPlotSelection(dst);
    }

    public static ImPlotRect getPlotSelection(int xAxis) {
        ImPlotRect dst = new ImPlotRect();
        nGetPlotSelection(dst, xAxis);
        return dst;
    }

    public static void getPlotSelection(ImPlotRect dst, int xAxis) {
        nGetPlotSelection(dst, xAxis);
    }

    public static ImPlotRect getPlotSelection(int xAxis, int yAxis) {
        ImPlotRect dst = new ImPlotRect();
        nGetPlotSelection(dst, xAxis, yAxis);
        return dst;
    }

    public static void getPlotSelection(ImPlotRect dst, int xAxis, int yAxis) {
        nGetPlotSelection(dst, xAxis, yAxis);
    }

    public static void cancelPlotSelection() {
        nCancelPlotSelection();
    }

    public static void hideNextItem() {
        nHideNextItem();
    }

    public static void hideNextItem(boolean hidden) {
        nHideNextItem(hidden);
    }

    public static void hideNextItem(boolean hidden, int cond) {
        nHideNextItem(hidden, cond);
    }

    public static void hideNextItem(int cond) {
        nHideNextItem(cond);
    }

    public static boolean beginAlignedPlots(String groupId) {
        return nBeginAlignedPlots(groupId);
    }

    public static boolean beginAlignedPlots(String groupId, boolean vertical) {
        return nBeginAlignedPlots(groupId, vertical);
    }

    public static void endAlignedPlots() {
        nEndAlignedPlots();
    }

    public static boolean beginLegendPopup(String labelId) {
        return nBeginLegendPopup(labelId);
    }

    public static boolean beginLegendPopup(String labelId, int mouseButton) {
        return nBeginLegendPopup(labelId, mouseButton);
    }

    public static void endLegendPopup() {
        nEndLegendPopup();
    }

    public static boolean isLegendEntryHovered(String labelId) {
        return nIsLegendEntryHovered(labelId);
    }

    public static boolean beginDragDropTargetPlot() {
        return nBeginDragDropTargetPlot();
    }

    public static boolean beginDragDropTargetAxis(int axis) {
        return nBeginDragDropTargetAxis(axis);
    }

    public static boolean beginDragDropTargetLegend() {
        return nBeginDragDropTargetLegend();
    }

    public static void endDragDropTarget() {
        nEndDragDropTarget();
    }

    public static boolean beginDragDropSourcePlot() {
        return nBeginDragDropSourcePlot();
    }

    public static boolean beginDragDropSourcePlot(int flags) {
        return nBeginDragDropSourcePlot(flags);
    }

    public static boolean beginDragDropSourceAxis(int axis) {
        return nBeginDragDropSourceAxis(axis);
    }

    public static boolean beginDragDropSourceAxis(int axis, int flags) {
        return nBeginDragDropSourceAxis(axis, flags);
    }

    public static boolean beginDragDropSourceItem(String labelId) {
        return nBeginDragDropSourceItem(labelId);
    }

    public static boolean beginDragDropSourceItem(String labelId, int flags) {
        return nBeginDragDropSourceItem(labelId, flags);
    }

    public static void endDragDropSource() {
        nEndDragDropSource();
    }

    public static ImPlotStyle getStyle() {
        _GETSTYLE_1.ptr = nGetStyle();
        return _GETSTYLE_1;
    }

    public static void styleColorsAuto() {
        nStyleColorsAuto();
    }

    public static void styleColorsAuto(ImPlotStyle dst) {
        nStyleColorsAuto(dst.ptr);
    }

    public static void styleColorsClassic() {
        nStyleColorsClassic();
    }

    public static void styleColorsClassic(ImPlotStyle dst) {
        nStyleColorsClassic(dst.ptr);
    }

    public static void styleColorsDark() {
        nStyleColorsDark();
    }

    public static void styleColorsDark(ImPlotStyle dst) {
        nStyleColorsDark(dst.ptr);
    }

    public static void styleColorsLight() {
        nStyleColorsLight();
    }

    public static void styleColorsLight(ImPlotStyle dst) {
        nStyleColorsLight(dst.ptr);
    }

    public static void pushStyleColor(int idx, int col) {
        nPushStyleColor(idx, col);
    }

    public static void pushStyleColor(int idx, ImVec4 col) {
        nPushStyleColor(idx, col.x, col.y, col.z, col.w);
    }

    public static void pushStyleColor(int idx, float colX, float colY, float colZ, float colW) {
        nPushStyleColor(idx, colX, colY, colZ, colW);
    }

    public static void popStyleColor() {
        nPopStyleColor();
    }

    public static void popStyleColor(int count) {
        nPopStyleColor(count);
    }

    public static void pushStyleVar(int idx, float val) {
        nPushStyleVar(idx, val);
    }

    public static void pushStyleVar(int idx, int val) {
        nPushStyleVar(idx, val);
    }

    public static void pushStyleVar(int idx, ImVec2 val) {
        nPushStyleVar(idx, val.x, val.y);
    }

    public static void pushStyleVar(int idx, float valX, float valY) {
        nPushStyleVar(idx, valX, valY);
    }

    public static void popStyleVar() {
        nPopStyleVar();
    }

    public static void popStyleVar(int count) {
        nPopStyleVar(count);
    }

    public static void setNextLineStyle() {
        nSetNextLineStyle();
    }

    public static void setNextLineStyle(ImVec4 col) {
        nSetNextLineStyle(col.x, col.y, col.z, col.w);
    }

    public static void setNextLineStyle(float colX, float colY, float colZ, float colW) {
        nSetNextLineStyle(colX, colY, colZ, colW);
    }

    public static void setNextLineStyle(ImVec4 col, float weight) {
        nSetNextLineStyle(col.x, col.y, col.z, col.w, weight);
    }

    public static void setNextLineStyle(float colX, float colY, float colZ, float colW, float weight) {
        nSetNextLineStyle(colX, colY, colZ, colW, weight);
    }

    public static void setNextLineStyle(float weight) {
        nSetNextLineStyle(weight);
    }

    public static void setNextFillStyle() {
        nSetNextFillStyle();
    }

    public static void setNextFillStyle(ImVec4 col) {
        nSetNextFillStyle(col.x, col.y, col.z, col.w);
    }

    public static void setNextFillStyle(float colX, float colY, float colZ, float colW) {
        nSetNextFillStyle(colX, colY, colZ, colW);
    }

    public static void setNextFillStyle(ImVec4 col, float alphaMod) {
        nSetNextFillStyle(col.x, col.y, col.z, col.w, alphaMod);
    }

    public static void setNextFillStyle(float colX, float colY, float colZ, float colW, float alphaMod) {
        nSetNextFillStyle(colX, colY, colZ, colW, alphaMod);
    }

    public static void setNextFillStyle(float alphaMod) {
        nSetNextFillStyle(alphaMod);
    }

    public static void setNextMarkerStyle() {
        nSetNextMarkerStyle();
    }

    public static void setNextMarkerStyle(int marker) {
        nSetNextMarkerStyle(marker);
    }

    public static void setNextMarkerStyle(int marker, float size) {
        nSetNextMarkerStyle(marker, size);
    }

    public static void setNextMarkerStyle(int marker, float size, ImVec4 fill) {
        nSetNextMarkerStyle(marker, size, fill.x, fill.y, fill.z, fill.w);
    }

    public static void setNextMarkerStyle(int marker, float size, float fillX, float fillY, float fillZ, float fillW) {
        nSetNextMarkerStyle(marker, size, fillX, fillY, fillZ, fillW);
    }

    public static void setNextMarkerStyle(int marker, float size, ImVec4 fill, float weight) {
        nSetNextMarkerStyle(marker, size, fill.x, fill.y, fill.z, fill.w, weight);
    }

    public static void setNextMarkerStyle(int marker, float size, float fillX, float fillY, float fillZ, float fillW, float weight) {
        nSetNextMarkerStyle(marker, size, fillX, fillY, fillZ, fillW, weight);
    }

    public static void setNextMarkerStyle(int marker, float size, ImVec4 fill, float weight, ImVec4 outline) {
        nSetNextMarkerStyle(marker, size, fill.x, fill.y, fill.z, fill.w, weight, outline.x, outline.y, outline.z, outline.w);
    }

    public static void setNextMarkerStyle(int marker, float size, float fillX, float fillY, float fillZ, float fillW, float weight, float outlineX, float outlineY, float outlineZ, float outlineW) {
        nSetNextMarkerStyle(marker, size, fillX, fillY, fillZ, fillW, weight, outlineX, outlineY, outlineZ, outlineW);
    }

    public static void setNextMarkerStyle(int marker, float size, ImVec4 fill, ImVec4 outline) {
        nSetNextMarkerStyle(marker, size, fill.x, fill.y, fill.z, fill.w, outline.x, outline.y, outline.z, outline.w);
    }

    public static void setNextMarkerStyle(int marker, float size, float fillX, float fillY, float fillZ, float fillW, float outlineX, float outlineY, float outlineZ, float outlineW) {
        nSetNextMarkerStyle(marker, size, fillX, fillY, fillZ, fillW, outlineX, outlineY, outlineZ, outlineW);
    }

    public static void setNextErrorBarStyle() {
        nSetNextErrorBarStyle();
    }

    public static void setNextErrorBarStyle(ImVec4 col) {
        nSetNextErrorBarStyle(col.x, col.y, col.z, col.w);
    }

    public static void setNextErrorBarStyle(float colX, float colY, float colZ, float colW) {
        nSetNextErrorBarStyle(colX, colY, colZ, colW);
    }

    public static void setNextErrorBarStyle(ImVec4 col, float size) {
        nSetNextErrorBarStyle(col.x, col.y, col.z, col.w, size);
    }

    public static void setNextErrorBarStyle(float colX, float colY, float colZ, float colW, float size) {
        nSetNextErrorBarStyle(colX, colY, colZ, colW, size);
    }

    public static void setNextErrorBarStyle(ImVec4 col, float size, float weight) {
        nSetNextErrorBarStyle(col.x, col.y, col.z, col.w, size, weight);
    }

    public static void setNextErrorBarStyle(float colX, float colY, float colZ, float colW, float size, float weight) {
        nSetNextErrorBarStyle(colX, colY, colZ, colW, size, weight);
    }

    public static void setNextErrorBarStyle(float size, float weight) {
        nSetNextErrorBarStyle(size, weight);
    }

    public static ImVec4 getLastItemColor() {
        ImVec4 dst = new ImVec4();
        nGetLastItemColor(dst);
        return dst;
    }

    public static float getLastItemColorX() {
        return nGetLastItemColorX();
    }

    public static float getLastItemColorY() {
        return nGetLastItemColorY();
    }

    public static float getLastItemColorZ() {
        return nGetLastItemColorZ();
    }

    public static float getLastItemColorW() {
        return nGetLastItemColorW();
    }

    public static void getLastItemColor(ImVec4 dst) {
        nGetLastItemColor(dst);
    }

    public static String getStyleColorName(int idx) {
        return nGetStyleColorName(idx);
    }

    public static String getMarkerName(int idx) {
        return nGetMarkerName(idx);
    }

    public static int addColormap(String name, ImVec4[] cols) {
        return nAddColormap(name, cols);
    }

    public static int addColormap(String name, ImVec4[] cols, boolean qual) {
        return nAddColormap(name, cols, qual);
    }

    public static int addColormap(String name, int[] cols) {
        return nAddColormap(name, cols);
    }

    public static int addColormap(String name, int[] cols, boolean qual) {
        return nAddColormap(name, cols, qual);
    }

    public static int getColormapCount() {
        return nGetColormapCount();
    }

    public static String getColormapName(int cmap) {
        return nGetColormapName(cmap);
    }

    public static int getColormapIndex(String name) {
        return nGetColormapIndex(name);
    }

    public static void pushColormap(int cmap) {
        nPushColormap(cmap);
    }

    public static void pushColormap(String name) {
        nPushColormap(name);
    }

    public static void popColormap() {
        nPopColormap();
    }

    public static void popColormap(int count) {
        nPopColormap(count);
    }

    public static ImVec4 nextColormapColor() {
        ImVec4 dst = new ImVec4();
        nNextColormapColor(dst);
        return dst;
    }

    public static float nextColormapColorX() {
        return nNextColormapColorX();
    }

    public static float nextColormapColorY() {
        return nNextColormapColorY();
    }

    public static float nextColormapColorZ() {
        return nNextColormapColorZ();
    }

    public static float nextColormapColorW() {
        return nNextColormapColorW();
    }

    public static void nextColormapColor(ImVec4 dst) {
        nNextColormapColor(dst);
    }

    public static int getColormapSize() {
        return nGetColormapSize();
    }

    public static int getColormapSize(int cmap) {
        return nGetColormapSize(cmap);
    }

    public static ImVec4 getColormapColor(int idx) {
        ImVec4 dst = new ImVec4();
        nGetColormapColor(dst, idx);
        return dst;
    }

    public static float getColormapColorX(int idx) {
        return nGetColormapColorX(idx);
    }

    public static float getColormapColorY(int idx) {
        return nGetColormapColorY(idx);
    }

    public static float getColormapColorZ(int idx) {
        return nGetColormapColorZ(idx);
    }

    public static float getColormapColorW(int idx) {
        return nGetColormapColorW(idx);
    }

    public static void getColormapColor(ImVec4 dst, int idx) {
        nGetColormapColor(dst, idx);
    }

    public static ImVec4 getColormapColor(int idx, int cmap) {
        ImVec4 dst = new ImVec4();
        nGetColormapColor(dst, idx, cmap);
        return dst;
    }

    public static float getColormapColorX(int idx, int cmap) {
        return nGetColormapColorX(idx, cmap);
    }

    public static float getColormapColorY(int idx, int cmap) {
        return nGetColormapColorY(idx, cmap);
    }

    public static float getColormapColorZ(int idx, int cmap) {
        return nGetColormapColorZ(idx, cmap);
    }

    public static float getColormapColorW(int idx, int cmap) {
        return nGetColormapColorW(idx, cmap);
    }

    public static void getColormapColor(ImVec4 dst, int idx, int cmap) {
        nGetColormapColor(dst, idx, cmap);
    }

    public static ImVec4 sampleColormap(float t) {
        ImVec4 dst = new ImVec4();
        nSampleColormap(dst, t);
        return dst;
    }

    public static float sampleColormapX(float t) {
        return nSampleColormapX(t);
    }

    public static float sampleColormapY(float t) {
        return nSampleColormapY(t);
    }

    public static float sampleColormapZ(float t) {
        return nSampleColormapZ(t);
    }

    public static float sampleColormapW(float t) {
        return nSampleColormapW(t);
    }

    public static void sampleColormap(ImVec4 dst, float t) {
        nSampleColormap(dst, t);
    }

    public static ImVec4 sampleColormap(float t, int cmap) {
        ImVec4 dst = new ImVec4();
        nSampleColormap(dst, t, cmap);
        return dst;
    }

    public static float sampleColormapX(float t, int cmap) {
        return nSampleColormapX(t, cmap);
    }

    public static float sampleColormapY(float t, int cmap) {
        return nSampleColormapY(t, cmap);
    }

    public static float sampleColormapZ(float t, int cmap) {
        return nSampleColormapZ(t, cmap);
    }

    public static float sampleColormapW(float t, int cmap) {
        return nSampleColormapW(t, cmap);
    }

    public static void sampleColormap(ImVec4 dst, float t, int cmap) {
        nSampleColormap(dst, t, cmap);
    }

    public static void colormapScale(String label, double scaleMin, double scaleMax) {
        nColormapScale(label, scaleMin, scaleMax);
    }

    public static void colormapScale(String label, double scaleMin, double scaleMax, ImVec2 size) {
        nColormapScale(label, scaleMin, scaleMax, size.x, size.y);
    }

    public static void colormapScale(String label, double scaleMin, double scaleMax, float sizeX, float sizeY) {
        nColormapScale(label, scaleMin, scaleMax, sizeX, sizeY);
    }

    public static void colormapScale(String label, double scaleMin, double scaleMax, ImVec2 size, String format) {
        nColormapScale(label, scaleMin, scaleMax, size.x, size.y, format);
    }

    public static void colormapScale(String label, double scaleMin, double scaleMax, float sizeX, float sizeY, String format) {
        nColormapScale(label, scaleMin, scaleMax, sizeX, sizeY, format);
    }

    public static void colormapScale(String label, double scaleMin, double scaleMax, ImVec2 size, String format, int flags) {
        nColormapScale(label, scaleMin, scaleMax, size.x, size.y, format, flags);
    }

    public static void colormapScale(String label, double scaleMin, double scaleMax, float sizeX, float sizeY, String format, int flags) {
        nColormapScale(label, scaleMin, scaleMax, sizeX, sizeY, format, flags);
    }

    public static void colormapScale(String label, double scaleMin, double scaleMax, ImVec2 size, String format, int flags, int cmap) {
        nColormapScale(label, scaleMin, scaleMax, size.x, size.y, format, flags, cmap);
    }

    public static void colormapScale(String label, double scaleMin, double scaleMax, float sizeX, float sizeY, String format, int flags, int cmap) {
        nColormapScale(label, scaleMin, scaleMax, sizeX, sizeY, format, flags, cmap);
    }

    public static void colormapScale(String label, double scaleMin, double scaleMax, String format, int flags, int cmap) {
        nColormapScale(label, scaleMin, scaleMax, format, flags, cmap);
    }

    public static void colormapScale(String label, double scaleMin, double scaleMax, int flags, int cmap) {
        nColormapScale(label, scaleMin, scaleMax, flags, cmap);
    }

    public static void colormapScale(String label, double scaleMin, double scaleMax, ImVec2 size, int flags, int cmap) {
        nColormapScale(label, scaleMin, scaleMax, size.x, size.y, flags, cmap);
    }

    public static void colormapScale(String label, double scaleMin, double scaleMax, float sizeX, float sizeY, int flags, int cmap) {
        nColormapScale(label, scaleMin, scaleMax, sizeX, sizeY, flags, cmap);
    }

    public static boolean colormapSlider(String label, ImFloat t) {
        return nColormapSlider(label, t != null ? t.getData() : null);
    }

    public static boolean colormapSlider(String label, ImFloat t, String format) {
        return nColormapSlider(label, t != null ? t.getData() : null, format);
    }

    public static boolean colormapSlider(String label, ImFloat t, String format, int cmap) {
        return nColormapSlider(label, t != null ? t.getData() : null, format, cmap);
    }

    public static boolean colormapSlider(String label, ImFloat t, int cmap) {
        return nColormapSlider(label, t != null ? t.getData() : null, cmap);
    }

    public static boolean colormapButton(String label) {
        return nColormapButton(label);
    }

    public static boolean colormapButton(String label, ImVec2 size) {
        return nColormapButton(label, size.x, size.y);
    }

    public static boolean colormapButton(String label, float sizeX, float sizeY) {
        return nColormapButton(label, sizeX, sizeY);
    }

    public static boolean colormapButton(String label, ImVec2 size, int cmap) {
        return nColormapButton(label, size.x, size.y, cmap);
    }

    public static boolean colormapButton(String label, float sizeX, float sizeY, int cmap) {
        return nColormapButton(label, sizeX, sizeY, cmap);
    }

    public static boolean colormapButton(String label, int cmap) {
        return nColormapButton(label, cmap);
    }

    public static void bustColorCache() {
        nBustColorCache();
    }

    public static void bustColorCache(String plotTitleId) {
        nBustColorCache(plotTitleId);
    }

    public static ImPlotInputMap getInputMap() {
        _GETINPUTMAP_1.ptr = nGetInputMap();
        return _GETINPUTMAP_1;
    }

    public static void mapInputDefault() {
        nMapInputDefault();
    }

    public static void mapInputDefault(ImPlotInputMap dst) {
        nMapInputDefault(dst.ptr);
    }

    public static void mapInputReverse() {
        nMapInputReverse();
    }

    public static void mapInputReverse(ImPlotInputMap dst) {
        nMapInputReverse(dst.ptr);
    }

    public static void itemIcon(ImVec4 col) {
        nItemIcon(col.x, col.y, col.z, col.w);
    }

    public static void itemIcon(float colX, float colY, float colZ, float colW) {
        nItemIcon(colX, colY, colZ, colW);
    }

    public static void itemIcon(int col) {
        nItemIcon(col);
    }

    public static void colormapIcon(int cmap) {
        nColormapIcon(cmap);
    }

    public static ImDrawList getPlotDrawList() {
        return new ImDrawList(nGetPlotDrawList());
    }

    public static void pushPlotClipRect() {
        nPushPlotClipRect();
    }

    public static void pushPlotClipRect(float expand) {
        nPushPlotClipRect(expand);
    }

    public static void popPlotClipRect() {
        nPopPlotClipRect();
    }

    public static boolean showStyleSelector(String label) {
        return nShowStyleSelector(label);
    }

    public static boolean showColormapSelector(String label) {
        return nShowColormapSelector(label);
    }

    public static boolean showInputMapSelector(String label) {
        return nShowInputMapSelector(label);
    }

    public static void showStyleEditor() {
        nShowStyleEditor();
    }

    public static void showStyleEditor(ImPlotStyle ref) {
        nShowStyleEditor(ref.ptr);
    }

    public static void showUserGuide() {
        nShowUserGuide();
    }

    public static void showMetricsWindow() {
        nShowMetricsWindow();
    }

    public static void showMetricsWindow(ImBoolean pOpen) {
        nShowMetricsWindow(pOpen != null ? pOpen.getData() : null);
    }

    public static void showDemoWindow() {
        nShowDemoWindow();
    }

    public static void showDemoWindow(ImBoolean pOpen) {
        nShowDemoWindow(pOpen != null ? pOpen.getData() : null);
    }
}
