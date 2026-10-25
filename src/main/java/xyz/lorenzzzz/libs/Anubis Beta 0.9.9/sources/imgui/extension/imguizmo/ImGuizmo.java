package imgui.extension.imguizmo;

import imgui.ImDrawList;
import imgui.ImVec2;
import imgui.internal.ImGuiContext;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/extension/imguizmo/ImGuizmo.class */
public final class ImGuizmo {
    private static native void nSetDrawList();

    private static native void nSetDrawList(long j);

    private static native void nBeginFrame();

    private static native void nSetImGuiContext(long j);

    private static native boolean nIsOver();

    private static native boolean nIsUsing();

    private static native void nEnable(boolean z);

    private static native void nDecomposeMatrixToComponents(float[] fArr, float[] fArr2, float[] fArr3, float[] fArr4);

    private static native void nRecomposeMatrixFromComponents(float[] fArr, float[] fArr2, float[] fArr3, float[] fArr4);

    private static native void nSetRect(float f, float f2, float f3, float f4);

    private static native void nSetOrthographic(boolean z);

    private static native void nDrawCubes(float[] fArr, float[] fArr2, float[] fArr3);

    private static native void nDrawCubes(float[] fArr, float[] fArr2, float[] fArr3, int i);

    private static native void nDrawGrid(float[] fArr, float[] fArr2, float[] fArr3, float f);

    private static native void nManipulate(float[] fArr, float[] fArr2, int i, int i2, float[] fArr3);

    private static native void nManipulate(float[] fArr, float[] fArr2, int i, int i2, float[] fArr3, float[] fArr4);

    private static native void nManipulate(float[] fArr, float[] fArr2, int i, int i2, float[] fArr3, float[] fArr4, float[] fArr5);

    private static native void nManipulate(float[] fArr, float[] fArr2, int i, int i2, float[] fArr3, float[] fArr4, float[] fArr5, float[] fArr6);

    private static native void nManipulate(float[] fArr, float[] fArr2, int i, int i2, float[] fArr3, float[] fArr4, float[] fArr5, float[] fArr6, float[] fArr7);

    private static native void nViewManipulate(float[] fArr, float f, float f2, float f3, float f4, float f5, int i);

    private static native void nSetID(int i);

    private static native boolean nIsOver(int i);

    private static native void nSetGizmoSizeClipSpace(float f);

    private static native void nAllowAxisFlip(boolean z);

    private ImGuizmo() {
    }

    public static void setDrawList() {
        nSetDrawList();
    }

    public static void setDrawList(ImDrawList drawList) {
        nSetDrawList(drawList.ptr);
    }

    public static void beginFrame() {
        nBeginFrame();
    }

    public static void setImGuiContext(ImGuiContext ctx) {
        nSetImGuiContext(ctx.ptr);
    }

    public static boolean isOver() {
        return nIsOver();
    }

    public static boolean isUsing() {
        return nIsUsing();
    }

    public static void enable(boolean enable) {
        nEnable(enable);
    }

    public static void decomposeMatrixToComponents(float[] matrix, float[] translation, float[] rotation, float[] scale) {
        nDecomposeMatrixToComponents(matrix, translation, rotation, scale);
    }

    public static void recomposeMatrixFromComponents(float[] translation, float[] rotation, float[] scale, float[] matrix) {
        nRecomposeMatrixFromComponents(translation, rotation, scale, matrix);
    }

    public static void setRect(float x, float y, float width, float height) {
        nSetRect(x, y, width, height);
    }

    public static void setOrthographic(boolean isOrthographic) {
        nSetOrthographic(isOrthographic);
    }

    public static void drawCubes(float[] view, float[] projection, float[] matrices) {
        nDrawCubes(view, projection, matrices);
    }

    public static void drawCubes(float[] view, float[] projection, float[] matrices, int matrixCount) {
        nDrawCubes(view, projection, matrices, matrixCount);
    }

    public static void drawCubes(float[] view, float[] projection, float[]... matrices) {
        if (matrices.length > 4) {
            throw new IllegalArgumentException("Only up to 4 cubes matrices is supported");
        }
        float[] cubeMatrices = new float[matrices.length * 16];
        int cubeMatricesLength = matrices.length;
        for (int i = 0; i < cubeMatricesLength; i++) {
            float[] cubeMatrix = matrices[i];
            System.arraycopy(cubeMatrix, 0, cubeMatrices, i * cubeMatrix.length, cubeMatrix.length);
        }
        drawCubes(view, projection, cubeMatrices, cubeMatrices.length);
    }

    public static void drawGrid(float[] view, float[] projection, float[] matrix, float gridSize) {
        nDrawGrid(view, projection, matrix, gridSize);
    }

    public static void manipulate(float[] view, float[] projection, int operation, int mode, float[] matrix) {
        nManipulate(view, projection, operation, mode, matrix);
    }

    public static void manipulate(float[] view, float[] projection, int operation, int mode, float[] matrix, float[] deltaMatrix) {
        nManipulate(view, projection, operation, mode, matrix, deltaMatrix);
    }

    public static void manipulate(float[] view, float[] projection, int operation, int mode, float[] matrix, float[] deltaMatrix, float[] snap) {
        nManipulate(view, projection, operation, mode, matrix, deltaMatrix, snap);
    }

    public static void manipulate(float[] view, float[] projection, int operation, int mode, float[] matrix, float[] deltaMatrix, float[] snap, float[] localBounds) {
        nManipulate(view, projection, operation, mode, matrix, deltaMatrix, snap, localBounds);
    }

    public static void manipulate(float[] view, float[] projection, int operation, int mode, float[] matrix, float[] deltaMatrix, float[] snap, float[] localBounds, float[] boundsSnap) {
        nManipulate(view, projection, operation, mode, matrix, deltaMatrix, snap, localBounds, boundsSnap);
    }

    public static void viewManipulate(float[] view, float length, ImVec2 position, ImVec2 size, int backgroundColor) {
        nViewManipulate(view, length, position.x, position.y, size.x, size.y, backgroundColor);
    }

    public static void viewManipulate(float[] view, float length, float positionX, float positionY, float sizeX, float sizeY, int backgroundColor) {
        nViewManipulate(view, length, positionX, positionY, sizeX, sizeY, backgroundColor);
    }

    public static void setID(int id) {
        nSetID(id);
    }

    public static boolean isOver(int operation) {
        return nIsOver(operation);
    }

    public static void setGizmoSizeClipSpace(float value) {
        nSetGizmoSizeClipSpace(value);
    }

    public static void allowAxisFlip(boolean value) {
        nAllowAxisFlip(value);
    }
}
