package imgui.extension.imguiknobs;

import imgui.type.ImFloat;
import imgui.type.ImInt;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/extension/imguiknobs/ImGuiKnobs.class */
public final class ImGuiKnobs {
    private static native boolean nKnob(String str, float[] fArr, float f, float f2);

    private static native boolean nKnob(String str, float[] fArr, float f, float f2, float f3);

    private static native boolean nKnob(String str, float[] fArr, float f, float f2, float f3, String str2);

    private static native boolean nKnob(String str, float[] fArr, float f, float f2, float f3, String str2, int i);

    private static native boolean nKnob(String str, float[] fArr, float f, float f2, float f3, String str2, int i, float f4);

    private static native boolean nKnob(String str, float[] fArr, float f, float f2, float f3, String str2, int i, float f4, int i2);

    private static native boolean nKnob(String str, float[] fArr, float f, float f2, float f3, String str2, int i, float f4, int i2, int i3);

    private static native boolean nKnob(String str, float[] fArr, float f, float f2, String str2, int i, float f3, int i2, int i3);

    private static native boolean nKnob(String str, float[] fArr, float f, float f2, int i, float f3, int i2, int i3);

    private static native boolean nKnob(String str, float[] fArr, float f, float f2, float f3, int i, float f4, int i2, int i3);

    private static native boolean nKnob(String str, float[] fArr, float f, float f2, float f3, float f4, int i, int i2);

    private static native boolean nKnob(String str, float[] fArr, float f, float f2, float f3, int i, int i2);

    private static native boolean nKnob(String str, float[] fArr, float f, float f2, float f3, int i);

    private static native boolean nKnob(String str, float[] fArr, float f, float f2, float f3, String str2, float f4, int i, int i2);

    private static native boolean nKnob(String str, float[] fArr, float f, float f2, float f3, String str2, int i, int i2, int i3);

    private static native boolean nKnob(String str, float[] fArr, float f, float f2, float f3, String str2, int i, int i2);

    private static native boolean nKnob(String str, int[] iArr, int i, int i2);

    private static native boolean nKnob(String str, int[] iArr, int i, int i2, float f);

    private static native boolean nKnob(String str, int[] iArr, int i, int i2, float f, String str2);

    private static native boolean nKnob(String str, int[] iArr, int i, int i2, float f, String str2, int i3);

    private static native boolean nKnob(String str, int[] iArr, int i, int i2, float f, String str2, int i3, float f2);

    private static native boolean nKnob(String str, int[] iArr, int i, int i2, float f, String str2, int i3, float f2, int i4);

    private static native boolean nKnob(String str, int[] iArr, int i, int i2, float f, String str2, int i3, float f2, int i4, int i5);

    private static native boolean nKnob(String str, int[] iArr, int i, int i2, String str2, int i3, float f, int i4, int i5);

    private static native boolean nKnob(String str, int[] iArr, int i, int i2, int i3, float f, int i4, int i5);

    private static native boolean nKnob(String str, int[] iArr, int i, int i2, float f, int i3, float f2, int i4, int i5);

    private static native boolean nKnob(String str, int[] iArr, int i, int i2, float f, float f2, int i3, int i4);

    private static native boolean nKnob(String str, int[] iArr, int i, int i2, float f, int i3, int i4);

    private static native boolean nKnob(String str, int[] iArr, int i, int i2, float f, int i3);

    private static native boolean nKnob(String str, int[] iArr, int i, int i2, float f, String str2, float f2, int i3, int i4);

    private static native boolean nKnob(String str, int[] iArr, int i, int i2, float f, String str2, int i3, int i4, int i5);

    private static native boolean nKnob(String str, int[] iArr, int i, int i2, float f, String str2, int i3, int i4);

    private ImGuiKnobs() {
    }

    public static boolean knob(String label, ImFloat pValue, float vMin, float vMax) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax);
    }

    public static boolean knob(String label, ImFloat pValue, float vMin, float vMax, float speed) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed);
    }

    public static boolean knob(String label, ImFloat pValue, float vMin, float vMax, float speed, String format) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, format);
    }

    public static boolean knob(String label, ImFloat pValue, float vMin, float vMax, float speed, String format, int variant) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, format, variant);
    }

    public static boolean knob(String label, ImFloat pValue, float vMin, float vMax, float speed, String format, int variant, float size) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, format, variant, size);
    }

    public static boolean knob(String label, ImFloat pValue, float vMin, float vMax, float speed, String format, int variant, float size, int flags) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, format, variant, size, flags);
    }

    public static boolean knob(String label, ImFloat pValue, float vMin, float vMax, float speed, String format, int variant, float size, int flags, int steps) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, format, variant, size, flags, steps);
    }

    public static boolean knob(String label, ImFloat pValue, float vMin, float vMax, String format, int variant, float size, int flags, int steps) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, format, variant, size, flags, steps);
    }

    public static boolean knob(String label, ImFloat pValue, float vMin, float vMax, int variant, float size, int flags, int steps) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, variant, size, flags, steps);
    }

    public static boolean knob(String label, ImFloat pValue, float vMin, float vMax, float speed, int variant, float size, int flags, int steps) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, variant, size, flags, steps);
    }

    public static boolean knob(String label, ImFloat pValue, float vMin, float vMax, float speed, float size, int flags, int steps) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, size, flags, steps);
    }

    public static boolean knob(String label, ImFloat pValue, float vMin, float vMax, float speed, int flags, int steps) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, flags, steps);
    }

    public static boolean knob(String label, ImFloat pValue, float vMin, float vMax, float speed, int steps) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, steps);
    }

    public static boolean knob(String label, ImFloat pValue, float vMin, float vMax, float speed, String format, float size, int flags, int steps) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, format, size, flags, steps);
    }

    public static boolean knob(String label, ImFloat pValue, float vMin, float vMax, float speed, String format, int variant, int flags, int steps) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, format, variant, flags, steps);
    }

    public static boolean knob(String label, ImFloat pValue, float vMin, float vMax, float speed, String format, int variant, int steps) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, format, variant, steps);
    }

    public static boolean knob(String label, ImInt pValue, int vMin, int vMax) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax);
    }

    public static boolean knob(String label, ImInt pValue, int vMin, int vMax, float speed) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed);
    }

    public static boolean knob(String label, ImInt pValue, int vMin, int vMax, float speed, String format) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, format);
    }

    public static boolean knob(String label, ImInt pValue, int vMin, int vMax, float speed, String format, int variant) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, format, variant);
    }

    public static boolean knob(String label, ImInt pValue, int vMin, int vMax, float speed, String format, int variant, float size) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, format, variant, size);
    }

    public static boolean knob(String label, ImInt pValue, int vMin, int vMax, float speed, String format, int variant, float size, int flags) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, format, variant, size, flags);
    }

    public static boolean knob(String label, ImInt pValue, int vMin, int vMax, float speed, String format, int variant, float size, int flags, int steps) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, format, variant, size, flags, steps);
    }

    public static boolean knob(String label, ImInt pValue, int vMin, int vMax, String format, int variant, float size, int flags, int steps) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, format, variant, size, flags, steps);
    }

    public static boolean knob(String label, ImInt pValue, int vMin, int vMax, int variant, float size, int flags, int steps) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, variant, size, flags, steps);
    }

    public static boolean knob(String label, ImInt pValue, int vMin, int vMax, float speed, int variant, float size, int flags, int steps) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, variant, size, flags, steps);
    }

    public static boolean knob(String label, ImInt pValue, int vMin, int vMax, float speed, float size, int flags, int steps) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, size, flags, steps);
    }

    public static boolean knob(String label, ImInt pValue, int vMin, int vMax, float speed, int flags, int steps) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, flags, steps);
    }

    public static boolean knob(String label, ImInt pValue, int vMin, int vMax, float speed, int steps) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, steps);
    }

    public static boolean knob(String label, ImInt pValue, int vMin, int vMax, float speed, String format, float size, int flags, int steps) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, format, size, flags, steps);
    }

    public static boolean knob(String label, ImInt pValue, int vMin, int vMax, float speed, String format, int variant, int flags, int steps) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, format, variant, flags, steps);
    }

    public static boolean knob(String label, ImInt pValue, int vMin, int vMax, float speed, String format, int variant, int steps) {
        return nKnob(label, pValue != null ? pValue.getData() : null, vMin, vMax, speed, format, variant, steps);
    }
}
