package imgui;

import imgui.binding.ImGuiStruct;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/ImDrawList.class */
public final class ImDrawList extends ImGuiStruct {
    private native int nGetFlags();

    private native void nSetFlags(int i);

    private native int nGetVtxCurrentIdx();

    private native void nPushClipRect(float f, float f2, float f3, float f4);

    private native void nPushClipRect(float f, float f2, float f3, float f4, boolean z);

    private native void nPushClipRectFullScreen();

    private native void nPopClipRect();

    private native void nPushTextureID(long j);

    private native void nPopTextureID();

    private native void nGetClipRectMin(ImVec2 imVec2);

    private native float nGetClipRectMinX();

    private native float nGetClipRectMinY();

    private native void nGetClipRectMax(ImVec2 imVec2);

    private native float nGetClipRectMaxX();

    private native float nGetClipRectMaxY();

    private native void nAddLine(float f, float f2, float f3, float f4, int i);

    private native void nAddLine(float f, float f2, float f3, float f4, int i, float f5);

    private native void nAddRect(float f, float f2, float f3, float f4, int i);

    private native void nAddRect(float f, float f2, float f3, float f4, int i, float f5);

    private native void nAddRect(float f, float f2, float f3, float f4, int i, float f5, int i2);

    private native void nAddRect(float f, float f2, float f3, float f4, int i, float f5, int i2, float f6);

    private native void nAddRect(float f, float f2, float f3, float f4, int i, int i2, float f5);

    private native void nAddRect(float f, float f2, float f3, float f4, int i, float f5, float f6);

    private native void nAddRectFilled(float f, float f2, float f3, float f4, int i);

    private native void nAddRectFilled(float f, float f2, float f3, float f4, int i, float f5);

    private native void nAddRectFilled(float f, float f2, float f3, float f4, int i, float f5, int i2);

    private native void nAddRectFilled(float f, float f2, float f3, float f4, int i, int i2);

    private native void nAddRectFilledMultiColor(float f, float f2, float f3, float f4, int i, int i2, int i3, int i4);

    private native void nAddQuad(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, int i);

    private native void nAddQuad(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, int i, float f9);

    private native void nAddQuadFilled(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, int i);

    private native void nAddTriangle(float f, float f2, float f3, float f4, float f5, float f6, int i);

    private native void nAddTriangle(float f, float f2, float f3, float f4, float f5, float f6, int i, float f7);

    private native void nAddTriangleFilled(float f, float f2, float f3, float f4, float f5, float f6, int i);

    private native void nAddCircle(float f, float f2, float f3, int i);

    private native void nAddCircle(float f, float f2, float f3, int i, int i2);

    private native void nAddCircle(float f, float f2, float f3, int i, int i2, float f4);

    private native void nAddCircle(float f, float f2, float f3, int i, float f4);

    private native void nAddCircleFilled(float f, float f2, float f3, int i);

    private native void nAddCircleFilled(float f, float f2, float f3, int i, int i2);

    private native void nAddNgon(float f, float f2, float f3, int i, int i2);

    private native void nAddNgon(float f, float f2, float f3, int i, int i2, float f4);

    private native void nAddNgonFilled(float f, float f2, float f3, int i, int i2);

    private native void nAddEllipse(float f, float f2, float f3, float f4, int i);

    private native void nAddEllipse(float f, float f2, float f3, float f4, int i, float f5);

    private native void nAddEllipse(float f, float f2, float f3, float f4, int i, float f5, float f6);

    private native void nAddEllipse(float f, float f2, float f3, float f4, int i, float f5, float f6, float f7);

    private native void nAddEllipseFilled(float f, float f2, float f3, float f4, int i);

    private native void nAddEllipseFilled(float f, float f2, float f3, float f4, int i, float f5);

    private native void nAddEllipseFilled(float f, float f2, float f3, float f4, int i, float f5, float f6);

    private native void nAddText(float f, float f2, int i, String str);

    private native void nAddText(float f, float f2, int i, String str, String str2);

    private native void nAddText(long j, int i, float f, float f2, int i2, String str);

    private native void nAddText(long j, int i, float f, float f2, int i2, String str, String str2);

    private native void nAddText(long j, int i, float f, float f2, int i2, String str, String str2, float f3);

    private native void nAddText(long j, int i, float f, float f2, int i2, String str, String str2, float f3, float f4, float f5, float f6, float f7);

    private native void nAddText(long j, int i, float f, float f2, int i2, String str, float f3, float f4, float f5, float f6, float f7);

    private native void nAddText(long j, int i, float f, float f2, int i2, String str, float f3, float f4, float f5, float f6);

    private native void nAddText(long j, int i, float f, float f2, int i2, String str, String str2, float f3, float f4, float f5, float f6);

    private native void nAddBezierCubic(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, int i, float f9);

    private native void nAddBezierCubic(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, int i, float f9, int i2);

    private native void nAddBezierQuadratic(float f, float f2, float f3, float f4, float f5, float f6, int i, float f7);

    private native void nAddBezierQuadratic(float f, float f2, float f3, float f4, float f5, float f6, int i, float f7, int i2);

    private native void nAddPolyline(ImVec2[] imVec2Arr, int i, int i2, int i3, float f);

    private native void nAddConvexPolyFilled(ImVec2[] imVec2Arr, int i, int i2);

    private native void nAddConcavePolyFilled(ImVec2[] imVec2Arr, int i, int i2);

    private native void nAddImage(long j, float f, float f2, float f3, float f4);

    private native void nAddImage(long j, float f, float f2, float f3, float f4, float f5, float f6);

    private native void nAddImage(long j, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8);

    private native void nAddImage(long j, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, int i);

    private native void nAddImageQuad(long j, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8);

    private native void nAddImageQuad(long j, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10);

    private native void nAddImageQuad(long j, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12);

    private native void nAddImageQuad(long j, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14);

    private native void nAddImageQuad(long j, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16);

    private native void nAddImageQuad(long j, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, int i);

    private native void nAddImageRounded(long j, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, int i, float f9);

    private native void nAddImageRounded(long j, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, int i, float f9, int i2);

    private native void nPathClear();

    private native void nPathLineTo(float f, float f2);

    private native void nPathLineToMergeDuplicate(float f, float f2);

    private native void nPathFillConvex(int i);

    private native void nPathFillConcave(int i);

    private native void nPathStroke(int i);

    private native void nPathStroke(int i, int i2);

    private native void nPathStroke(int i, int i2, float f);

    private native void nPathStroke(int i, float f);

    private native void nPathArcTo(float f, float f2, float f3, float f4, float f5);

    private native void nPathArcTo(float f, float f2, float f3, float f4, float f5, int i);

    private native void nPathArcToFast(float f, float f2, float f3, int i, int i2);

    private native void nPathEllipticalArcTo(float f, float f2, float f3, float f4, float f5, float f6, float f7);

    private native void nPathEllipticalArcTo(float f, float f2, float f3, float f4, float f5, float f6, float f7, int i);

    private native void nPathBezierCubicCurveTo(float f, float f2, float f3, float f4, float f5, float f6);

    private native void nPathBezierCubicCurveTo(float f, float f2, float f3, float f4, float f5, float f6, int i);

    private native void nPathBezierQuadraticCurveTo(float f, float f2, float f3, float f4);

    private native void nPathBezierQuadraticCurveTo(float f, float f2, float f3, float f4, int i);

    private native void nPathRect(float f, float f2, float f3, float f4);

    private native void nPathRect(float f, float f2, float f3, float f4, float f5);

    private native void nPathRect(float f, float f2, float f3, float f4, float f5, int i);

    private native void nPathRect(float f, float f2, float f3, float f4, int i);

    private native void nChannelsSplit(int i);

    private native void nChannelsMerge();

    private native void nChannelsSetCurrent(int i);

    private native void nPrimReserve(int i, int i2);

    private native void nPrimUnreserve(int i, int i2);

    private native void nPrimRect(float f, float f2, float f3, float f4, int i);

    private native void nPrimRectUV(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, int i);

    private native void nPrimQuadUV(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, int i);

    private native void nPrimWriteVtx(float f, float f2, float f3, float f4, int i);

    private native void nPrimWriteIdx(int i);

    private native void nPrimVtx(float f, float f2, float f3, float f4, int i);

    public ImDrawList(long ptr) {
        super(ptr);
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

    public int getVtxCurrentIdx() {
        return nGetVtxCurrentIdx();
    }

    public void pushClipRect(ImVec2 clipRectMin, ImVec2 clipRectMax) {
        nPushClipRect(clipRectMin.x, clipRectMin.y, clipRectMax.x, clipRectMax.y);
    }

    public void pushClipRect(float clipRectMinX, float clipRectMinY, float clipRectMaxX, float clipRectMaxY) {
        nPushClipRect(clipRectMinX, clipRectMinY, clipRectMaxX, clipRectMaxY);
    }

    public void pushClipRect(ImVec2 clipRectMin, ImVec2 clipRectMax, boolean intersectWithCurrentClipRect) {
        nPushClipRect(clipRectMin.x, clipRectMin.y, clipRectMax.x, clipRectMax.y, intersectWithCurrentClipRect);
    }

    public void pushClipRect(float clipRectMinX, float clipRectMinY, float clipRectMaxX, float clipRectMaxY, boolean intersectWithCurrentClipRect) {
        nPushClipRect(clipRectMinX, clipRectMinY, clipRectMaxX, clipRectMaxY, intersectWithCurrentClipRect);
    }

    public void pushClipRectFullScreen() {
        nPushClipRectFullScreen();
    }

    public void popClipRect() {
        nPopClipRect();
    }

    public void pushTextureID(long textureId) {
        nPushTextureID(textureId);
    }

    public void popTextureID() {
        nPopTextureID();
    }

    public ImVec2 getClipRectMin() {
        ImVec2 dst = new ImVec2();
        nGetClipRectMin(dst);
        return dst;
    }

    public float getClipRectMinX() {
        return nGetClipRectMinX();
    }

    public float getClipRectMinY() {
        return nGetClipRectMinY();
    }

    public void getClipRectMin(ImVec2 dst) {
        nGetClipRectMin(dst);
    }

    public ImVec2 getClipRectMax() {
        ImVec2 dst = new ImVec2();
        nGetClipRectMax(dst);
        return dst;
    }

    public float getClipRectMaxX() {
        return nGetClipRectMaxX();
    }

    public float getClipRectMaxY() {
        return nGetClipRectMaxY();
    }

    public void getClipRectMax(ImVec2 dst) {
        nGetClipRectMax(dst);
    }

    public void addLine(ImVec2 p1, ImVec2 p2, int col) {
        nAddLine(p1.x, p1.y, p2.x, p2.y, col);
    }

    public void addLine(float p1X, float p1Y, float p2X, float p2Y, int col) {
        nAddLine(p1X, p1Y, p2X, p2Y, col);
    }

    public void addLine(ImVec2 p1, ImVec2 p2, int col, float thickness) {
        nAddLine(p1.x, p1.y, p2.x, p2.y, col, thickness);
    }

    public void addLine(float p1X, float p1Y, float p2X, float p2Y, int col, float thickness) {
        nAddLine(p1X, p1Y, p2X, p2Y, col, thickness);
    }

    public void addRect(ImVec2 pMin, ImVec2 pMax, int col) {
        nAddRect(pMin.x, pMin.y, pMax.x, pMax.y, col);
    }

    public void addRect(float pMinX, float pMinY, float pMaxX, float pMaxY, int col) {
        nAddRect(pMinX, pMinY, pMaxX, pMaxY, col);
    }

    public void addRect(ImVec2 pMin, ImVec2 pMax, int col, float rounding) {
        nAddRect(pMin.x, pMin.y, pMax.x, pMax.y, col, rounding);
    }

    public void addRect(float pMinX, float pMinY, float pMaxX, float pMaxY, int col, float rounding) {
        nAddRect(pMinX, pMinY, pMaxX, pMaxY, col, rounding);
    }

    public void addRect(ImVec2 pMin, ImVec2 pMax, int col, float rounding, int flags) {
        nAddRect(pMin.x, pMin.y, pMax.x, pMax.y, col, rounding, flags);
    }

    public void addRect(float pMinX, float pMinY, float pMaxX, float pMaxY, int col, float rounding, int flags) {
        nAddRect(pMinX, pMinY, pMaxX, pMaxY, col, rounding, flags);
    }

    public void addRect(ImVec2 pMin, ImVec2 pMax, int col, float rounding, int flags, float thickness) {
        nAddRect(pMin.x, pMin.y, pMax.x, pMax.y, col, rounding, flags, thickness);
    }

    public void addRect(float pMinX, float pMinY, float pMaxX, float pMaxY, int col, float rounding, int flags, float thickness) {
        nAddRect(pMinX, pMinY, pMaxX, pMaxY, col, rounding, flags, thickness);
    }

    public void addRect(ImVec2 pMin, ImVec2 pMax, int col, int flags, float thickness) {
        nAddRect(pMin.x, pMin.y, pMax.x, pMax.y, col, flags, thickness);
    }

    public void addRect(float pMinX, float pMinY, float pMaxX, float pMaxY, int col, int flags, float thickness) {
        nAddRect(pMinX, pMinY, pMaxX, pMaxY, col, flags, thickness);
    }

    public void addRect(ImVec2 pMin, ImVec2 pMax, int col, float rounding, float thickness) {
        nAddRect(pMin.x, pMin.y, pMax.x, pMax.y, col, rounding, thickness);
    }

    public void addRect(float pMinX, float pMinY, float pMaxX, float pMaxY, int col, float rounding, float thickness) {
        nAddRect(pMinX, pMinY, pMaxX, pMaxY, col, rounding, thickness);
    }

    public void addRectFilled(ImVec2 pMin, ImVec2 pMax, int col) {
        nAddRectFilled(pMin.x, pMin.y, pMax.x, pMax.y, col);
    }

    public void addRectFilled(float pMinX, float pMinY, float pMaxX, float pMaxY, int col) {
        nAddRectFilled(pMinX, pMinY, pMaxX, pMaxY, col);
    }

    public void addRectFilled(ImVec2 pMin, ImVec2 pMax, int col, float rounding) {
        nAddRectFilled(pMin.x, pMin.y, pMax.x, pMax.y, col, rounding);
    }

    public void addRectFilled(float pMinX, float pMinY, float pMaxX, float pMaxY, int col, float rounding) {
        nAddRectFilled(pMinX, pMinY, pMaxX, pMaxY, col, rounding);
    }

    public void addRectFilled(ImVec2 pMin, ImVec2 pMax, int col, float rounding, int flags) {
        nAddRectFilled(pMin.x, pMin.y, pMax.x, pMax.y, col, rounding, flags);
    }

    public void addRectFilled(float pMinX, float pMinY, float pMaxX, float pMaxY, int col, float rounding, int flags) {
        nAddRectFilled(pMinX, pMinY, pMaxX, pMaxY, col, rounding, flags);
    }

    public void addRectFilled(ImVec2 pMin, ImVec2 pMax, int col, int flags) {
        nAddRectFilled(pMin.x, pMin.y, pMax.x, pMax.y, col, flags);
    }

    public void addRectFilled(float pMinX, float pMinY, float pMaxX, float pMaxY, int col, int flags) {
        nAddRectFilled(pMinX, pMinY, pMaxX, pMaxY, col, flags);
    }

    public void addRectFilledMultiColor(ImVec2 pMin, ImVec2 pMax, int colUprLeft, int colUprRight, int colBotRight, int colBotLeft) {
        nAddRectFilledMultiColor(pMin.x, pMin.y, pMax.x, pMax.y, colUprLeft, colUprRight, colBotRight, colBotLeft);
    }

    public void addRectFilledMultiColor(float pMinX, float pMinY, float pMaxX, float pMaxY, int colUprLeft, int colUprRight, int colBotRight, int colBotLeft) {
        nAddRectFilledMultiColor(pMinX, pMinY, pMaxX, pMaxY, colUprLeft, colUprRight, colBotRight, colBotLeft);
    }

    public void addQuad(ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, int col) {
        nAddQuad(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y, col);
    }

    public void addQuad(float p1X, float p1Y, float p2X, float p2Y, float p3X, float p3Y, float p4X, float p4Y, int col) {
        nAddQuad(p1X, p1Y, p2X, p2Y, p3X, p3Y, p4X, p4Y, col);
    }

    public void addQuad(ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, int col, float thickness) {
        nAddQuad(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y, col, thickness);
    }

    public void addQuad(float p1X, float p1Y, float p2X, float p2Y, float p3X, float p3Y, float p4X, float p4Y, int col, float thickness) {
        nAddQuad(p1X, p1Y, p2X, p2Y, p3X, p3Y, p4X, p4Y, col, thickness);
    }

    public void addQuadFilled(ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, int col) {
        nAddQuadFilled(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y, col);
    }

    public void addQuadFilled(float p1X, float p1Y, float p2X, float p2Y, float p3X, float p3Y, float p4X, float p4Y, int col) {
        nAddQuadFilled(p1X, p1Y, p2X, p2Y, p3X, p3Y, p4X, p4Y, col);
    }

    public void addTriangle(ImVec2 p1, ImVec2 p2, ImVec2 p3, int col) {
        nAddTriangle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, col);
    }

    public void addTriangle(float p1X, float p1Y, float p2X, float p2Y, float p3X, float p3Y, int col) {
        nAddTriangle(p1X, p1Y, p2X, p2Y, p3X, p3Y, col);
    }

    public void addTriangle(ImVec2 p1, ImVec2 p2, ImVec2 p3, int col, float thickness) {
        nAddTriangle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, col, thickness);
    }

    public void addTriangle(float p1X, float p1Y, float p2X, float p2Y, float p3X, float p3Y, int col, float thickness) {
        nAddTriangle(p1X, p1Y, p2X, p2Y, p3X, p3Y, col, thickness);
    }

    public void addTriangleFilled(ImVec2 p1, ImVec2 p2, ImVec2 p3, int col) {
        nAddTriangleFilled(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, col);
    }

    public void addTriangleFilled(float p1X, float p1Y, float p2X, float p2Y, float p3X, float p3Y, int col) {
        nAddTriangleFilled(p1X, p1Y, p2X, p2Y, p3X, p3Y, col);
    }

    public void addCircle(ImVec2 center, float radius, int col) {
        nAddCircle(center.x, center.y, radius, col);
    }

    public void addCircle(float centerX, float centerY, float radius, int col) {
        nAddCircle(centerX, centerY, radius, col);
    }

    public void addCircle(ImVec2 center, float radius, int col, int numSegments) {
        nAddCircle(center.x, center.y, radius, col, numSegments);
    }

    public void addCircle(float centerX, float centerY, float radius, int col, int numSegments) {
        nAddCircle(centerX, centerY, radius, col, numSegments);
    }

    public void addCircle(ImVec2 center, float radius, int col, int numSegments, float thickness) {
        nAddCircle(center.x, center.y, radius, col, numSegments, thickness);
    }

    public void addCircle(float centerX, float centerY, float radius, int col, int numSegments, float thickness) {
        nAddCircle(centerX, centerY, radius, col, numSegments, thickness);
    }

    public void addCircle(ImVec2 center, float radius, int col, float thickness) {
        nAddCircle(center.x, center.y, radius, col, thickness);
    }

    public void addCircle(float centerX, float centerY, float radius, int col, float thickness) {
        nAddCircle(centerX, centerY, radius, col, thickness);
    }

    public void addCircleFilled(ImVec2 center, float radius, int col) {
        nAddCircleFilled(center.x, center.y, radius, col);
    }

    public void addCircleFilled(float centerX, float centerY, float radius, int col) {
        nAddCircleFilled(centerX, centerY, radius, col);
    }

    public void addCircleFilled(ImVec2 center, float radius, int col, int numSegments) {
        nAddCircleFilled(center.x, center.y, radius, col, numSegments);
    }

    public void addCircleFilled(float centerX, float centerY, float radius, int col, int numSegments) {
        nAddCircleFilled(centerX, centerY, radius, col, numSegments);
    }

    public void addNgon(ImVec2 center, float radius, int col, int num_segments) {
        nAddNgon(center.x, center.y, radius, col, num_segments);
    }

    public void addNgon(float centerX, float centerY, float radius, int col, int num_segments) {
        nAddNgon(centerX, centerY, radius, col, num_segments);
    }

    public void addNgon(ImVec2 center, float radius, int col, int num_segments, float thickness) {
        nAddNgon(center.x, center.y, radius, col, num_segments, thickness);
    }

    public void addNgon(float centerX, float centerY, float radius, int col, int num_segments, float thickness) {
        nAddNgon(centerX, centerY, radius, col, num_segments, thickness);
    }

    public void addNgonFilled(ImVec2 center, float radius, int col, int num_segments) {
        nAddNgonFilled(center.x, center.y, radius, col, num_segments);
    }

    public void addNgonFilled(float centerX, float centerY, float radius, int col, int num_segments) {
        nAddNgonFilled(centerX, centerY, radius, col, num_segments);
    }

    public void addEllipse(ImVec2 center, ImVec2 radius, int col) {
        nAddEllipse(center.x, center.y, radius.x, radius.y, col);
    }

    public void addEllipse(float centerX, float centerY, float radiusX, float radiusY, int col) {
        nAddEllipse(centerX, centerY, radiusX, radiusY, col);
    }

    public void addEllipse(ImVec2 center, ImVec2 radius, int col, float rot) {
        nAddEllipse(center.x, center.y, radius.x, radius.y, col, rot);
    }

    public void addEllipse(float centerX, float centerY, float radiusX, float radiusY, int col, float rot) {
        nAddEllipse(centerX, centerY, radiusX, radiusY, col, rot);
    }

    public void addEllipse(ImVec2 center, ImVec2 radius, int col, float rot, float numSegments) {
        nAddEllipse(center.x, center.y, radius.x, radius.y, col, rot, numSegments);
    }

    public void addEllipse(float centerX, float centerY, float radiusX, float radiusY, int col, float rot, float numSegments) {
        nAddEllipse(centerX, centerY, radiusX, radiusY, col, rot, numSegments);
    }

    public void addEllipse(ImVec2 center, ImVec2 radius, int col, float rot, float numSegments, float thickness) {
        nAddEllipse(center.x, center.y, radius.x, radius.y, col, rot, numSegments, thickness);
    }

    public void addEllipse(float centerX, float centerY, float radiusX, float radiusY, int col, float rot, float numSegments, float thickness) {
        nAddEllipse(centerX, centerY, radiusX, radiusY, col, rot, numSegments, thickness);
    }

    public void addEllipseFilled(ImVec2 center, ImVec2 radius, int col) {
        nAddEllipseFilled(center.x, center.y, radius.x, radius.y, col);
    }

    public void addEllipseFilled(float centerX, float centerY, float radiusX, float radiusY, int col) {
        nAddEllipseFilled(centerX, centerY, radiusX, radiusY, col);
    }

    public void addEllipseFilled(ImVec2 center, ImVec2 radius, int col, float rot) {
        nAddEllipseFilled(center.x, center.y, radius.x, radius.y, col, rot);
    }

    public void addEllipseFilled(float centerX, float centerY, float radiusX, float radiusY, int col, float rot) {
        nAddEllipseFilled(centerX, centerY, radiusX, radiusY, col, rot);
    }

    public void addEllipseFilled(ImVec2 center, ImVec2 radius, int col, float rot, float numSegments) {
        nAddEllipseFilled(center.x, center.y, radius.x, radius.y, col, rot, numSegments);
    }

    public void addEllipseFilled(float centerX, float centerY, float radiusX, float radiusY, int col, float rot, float numSegments) {
        nAddEllipseFilled(centerX, centerY, radiusX, radiusY, col, rot, numSegments);
    }

    public void addText(ImVec2 pos, int col, String textBegin) {
        nAddText(pos.x, pos.y, col, textBegin);
    }

    public void addText(float posX, float posY, int col, String textBegin) {
        nAddText(posX, posY, col, textBegin);
    }

    public void addText(ImVec2 pos, int col, String textBegin, String textEnd) {
        nAddText(pos.x, pos.y, col, textBegin, textEnd);
    }

    public void addText(float posX, float posY, int col, String textBegin, String textEnd) {
        nAddText(posX, posY, col, textBegin, textEnd);
    }

    public void addText(ImFont font, int fontSize, ImVec2 pos, int col, String textBegin) {
        nAddText(font.ptr, fontSize, pos.x, pos.y, col, textBegin);
    }

    public void addText(ImFont font, int fontSize, float posX, float posY, int col, String textBegin) {
        nAddText(font.ptr, fontSize, posX, posY, col, textBegin);
    }

    public void addText(ImFont font, int fontSize, ImVec2 pos, int col, String textBegin, String textEnd) {
        nAddText(font.ptr, fontSize, pos.x, pos.y, col, textBegin, textEnd);
    }

    public void addText(ImFont font, int fontSize, float posX, float posY, int col, String textBegin, String textEnd) {
        nAddText(font.ptr, fontSize, posX, posY, col, textBegin, textEnd);
    }

    public void addText(ImFont font, int fontSize, ImVec2 pos, int col, String textBegin, String textEnd, float wrapWidth) {
        nAddText(font.ptr, fontSize, pos.x, pos.y, col, textBegin, textEnd, wrapWidth);
    }

    public void addText(ImFont font, int fontSize, float posX, float posY, int col, String textBegin, String textEnd, float wrapWidth) {
        nAddText(font.ptr, fontSize, posX, posY, col, textBegin, textEnd, wrapWidth);
    }

    public void addText(ImFont font, int fontSize, ImVec2 pos, int col, String textBegin, String textEnd, float wrapWidth, ImVec4 cpuFineClipRect) {
        nAddText(font.ptr, fontSize, pos.x, pos.y, col, textBegin, textEnd, wrapWidth, cpuFineClipRect.x, cpuFineClipRect.y, cpuFineClipRect.z, cpuFineClipRect.w);
    }

    public void addText(ImFont font, int fontSize, float posX, float posY, int col, String textBegin, String textEnd, float wrapWidth, float cpuFineClipRectX, float cpuFineClipRectY, float cpuFineClipRectZ, float cpuFineClipRectW) {
        nAddText(font.ptr, fontSize, posX, posY, col, textBegin, textEnd, wrapWidth, cpuFineClipRectX, cpuFineClipRectY, cpuFineClipRectZ, cpuFineClipRectW);
    }

    public void addText(ImFont font, int fontSize, ImVec2 pos, int col, String textBegin, float wrapWidth, ImVec4 cpuFineClipRect) {
        nAddText(font.ptr, fontSize, pos.x, pos.y, col, textBegin, wrapWidth, cpuFineClipRect.x, cpuFineClipRect.y, cpuFineClipRect.z, cpuFineClipRect.w);
    }

    public void addText(ImFont font, int fontSize, float posX, float posY, int col, String textBegin, float wrapWidth, float cpuFineClipRectX, float cpuFineClipRectY, float cpuFineClipRectZ, float cpuFineClipRectW) {
        nAddText(font.ptr, fontSize, posX, posY, col, textBegin, wrapWidth, cpuFineClipRectX, cpuFineClipRectY, cpuFineClipRectZ, cpuFineClipRectW);
    }

    public void addText(ImFont font, int fontSize, ImVec2 pos, int col, String textBegin, ImVec4 cpuFineClipRect) {
        nAddText(font.ptr, fontSize, pos.x, pos.y, col, textBegin, cpuFineClipRect.x, cpuFineClipRect.y, cpuFineClipRect.z, cpuFineClipRect.w);
    }

    public void addText(ImFont font, int fontSize, float posX, float posY, int col, String textBegin, float cpuFineClipRectX, float cpuFineClipRectY, float cpuFineClipRectZ, float cpuFineClipRectW) {
        nAddText(font.ptr, fontSize, posX, posY, col, textBegin, cpuFineClipRectX, cpuFineClipRectY, cpuFineClipRectZ, cpuFineClipRectW);
    }

    public void addText(ImFont font, int fontSize, ImVec2 pos, int col, String textBegin, String textEnd, ImVec4 cpuFineClipRect) {
        nAddText(font.ptr, fontSize, pos.x, pos.y, col, textBegin, textEnd, cpuFineClipRect.x, cpuFineClipRect.y, cpuFineClipRect.z, cpuFineClipRect.w);
    }

    public void addText(ImFont font, int fontSize, float posX, float posY, int col, String textBegin, String textEnd, float cpuFineClipRectX, float cpuFineClipRectY, float cpuFineClipRectZ, float cpuFineClipRectW) {
        nAddText(font.ptr, fontSize, posX, posY, col, textBegin, textEnd, cpuFineClipRectX, cpuFineClipRectY, cpuFineClipRectZ, cpuFineClipRectW);
    }

    public void addBezierCubic(ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, int col, float thickness) {
        nAddBezierCubic(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y, col, thickness);
    }

    public void addBezierCubic(float p1X, float p1Y, float p2X, float p2Y, float p3X, float p3Y, float p4X, float p4Y, int col, float thickness) {
        nAddBezierCubic(p1X, p1Y, p2X, p2Y, p3X, p3Y, p4X, p4Y, col, thickness);
    }

    public void addBezierCubic(ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, int col, float thickness, int numSegments) {
        nAddBezierCubic(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y, col, thickness, numSegments);
    }

    public void addBezierCubic(float p1X, float p1Y, float p2X, float p2Y, float p3X, float p3Y, float p4X, float p4Y, int col, float thickness, int numSegments) {
        nAddBezierCubic(p1X, p1Y, p2X, p2Y, p3X, p3Y, p4X, p4Y, col, thickness, numSegments);
    }

    public void addBezierQuadratic(ImVec2 p1, ImVec2 p2, ImVec2 p3, int col, float thickness) {
        nAddBezierQuadratic(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, col, thickness);
    }

    public void addBezierQuadratic(float p1X, float p1Y, float p2X, float p2Y, float p3X, float p3Y, int col, float thickness) {
        nAddBezierQuadratic(p1X, p1Y, p2X, p2Y, p3X, p3Y, col, thickness);
    }

    public void addBezierQuadratic(ImVec2 p1, ImVec2 p2, ImVec2 p3, int col, float thickness, int numSegments) {
        nAddBezierQuadratic(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, col, thickness, numSegments);
    }

    public void addBezierQuadratic(float p1X, float p1Y, float p2X, float p2Y, float p3X, float p3Y, int col, float thickness, int numSegments) {
        nAddBezierQuadratic(p1X, p1Y, p2X, p2Y, p3X, p3Y, col, thickness, numSegments);
    }

    public void addPolyline(ImVec2[] points, int numPoints, int col, int imDrawFlags, float thickness) {
        nAddPolyline(points, numPoints, col, imDrawFlags, thickness);
    }

    public void addConvexPolyFilled(ImVec2[] points, int numPoints, int col) {
        nAddConvexPolyFilled(points, numPoints, col);
    }

    public void addConcavePolyFilled(ImVec2[] points, int numPoints, int col) {
        nAddConcavePolyFilled(points, numPoints, col);
    }

    public void addImage(long textureID, ImVec2 pMin, ImVec2 pMax) {
        nAddImage(textureID, pMin.x, pMin.y, pMax.x, pMax.y);
    }

    public void addImage(long textureID, float pMinX, float pMinY, float pMaxX, float pMaxY) {
        nAddImage(textureID, pMinX, pMinY, pMaxX, pMaxY);
    }

    public void addImage(long textureID, ImVec2 pMin, ImVec2 pMax, ImVec2 uvMin) {
        nAddImage(textureID, pMin.x, pMin.y, pMax.x, pMax.y, uvMin.x, uvMin.y);
    }

    public void addImage(long textureID, float pMinX, float pMinY, float pMaxX, float pMaxY, float uvMinX, float uvMinY) {
        nAddImage(textureID, pMinX, pMinY, pMaxX, pMaxY, uvMinX, uvMinY);
    }

    public void addImage(long textureID, ImVec2 pMin, ImVec2 pMax, ImVec2 uvMin, ImVec2 uvMax) {
        nAddImage(textureID, pMin.x, pMin.y, pMax.x, pMax.y, uvMin.x, uvMin.y, uvMax.x, uvMax.y);
    }

    public void addImage(long textureID, float pMinX, float pMinY, float pMaxX, float pMaxY, float uvMinX, float uvMinY, float uvMaxX, float uvMaxY) {
        nAddImage(textureID, pMinX, pMinY, pMaxX, pMaxY, uvMinX, uvMinY, uvMaxX, uvMaxY);
    }

    public void addImage(long textureID, ImVec2 pMin, ImVec2 pMax, ImVec2 uvMin, ImVec2 uvMax, int col) {
        nAddImage(textureID, pMin.x, pMin.y, pMax.x, pMax.y, uvMin.x, uvMin.y, uvMax.x, uvMax.y, col);
    }

    public void addImage(long textureID, float pMinX, float pMinY, float pMaxX, float pMaxY, float uvMinX, float uvMinY, float uvMaxX, float uvMaxY, int col) {
        nAddImage(textureID, pMinX, pMinY, pMaxX, pMaxY, uvMinX, uvMinY, uvMaxX, uvMaxY, col);
    }

    public void addImageQuad(long textureID, ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4) {
        nAddImageQuad(textureID, p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y);
    }

    public void addImageQuad(long textureID, float p1X, float p1Y, float p2X, float p2Y, float p3X, float p3Y, float p4X, float p4Y) {
        nAddImageQuad(textureID, p1X, p1Y, p2X, p2Y, p3X, p3Y, p4X, p4Y);
    }

    public void addImageQuad(long textureID, ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, ImVec2 uv1) {
        nAddImageQuad(textureID, p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y, uv1.x, uv1.y);
    }

    public void addImageQuad(long textureID, float p1X, float p1Y, float p2X, float p2Y, float p3X, float p3Y, float p4X, float p4Y, float uv1X, float uv1Y) {
        nAddImageQuad(textureID, p1X, p1Y, p2X, p2Y, p3X, p3Y, p4X, p4Y, uv1X, uv1Y);
    }

    public void addImageQuad(long textureID, ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, ImVec2 uv1, ImVec2 uv2) {
        nAddImageQuad(textureID, p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y, uv1.x, uv1.y, uv2.x, uv2.y);
    }

    public void addImageQuad(long textureID, float p1X, float p1Y, float p2X, float p2Y, float p3X, float p3Y, float p4X, float p4Y, float uv1X, float uv1Y, float uv2X, float uv2Y) {
        nAddImageQuad(textureID, p1X, p1Y, p2X, p2Y, p3X, p3Y, p4X, p4Y, uv1X, uv1Y, uv2X, uv2Y);
    }

    public void addImageQuad(long textureID, ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, ImVec2 uv1, ImVec2 uv2, ImVec2 uv3) {
        nAddImageQuad(textureID, p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y, uv1.x, uv1.y, uv2.x, uv2.y, uv3.x, uv3.y);
    }

    public void addImageQuad(long textureID, float p1X, float p1Y, float p2X, float p2Y, float p3X, float p3Y, float p4X, float p4Y, float uv1X, float uv1Y, float uv2X, float uv2Y, float uv3X, float uv3Y) {
        nAddImageQuad(textureID, p1X, p1Y, p2X, p2Y, p3X, p3Y, p4X, p4Y, uv1X, uv1Y, uv2X, uv2Y, uv3X, uv3Y);
    }

    public void addImageQuad(long textureID, ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, ImVec2 uv1, ImVec2 uv2, ImVec2 uv3, ImVec2 uv4) {
        nAddImageQuad(textureID, p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y, uv1.x, uv1.y, uv2.x, uv2.y, uv3.x, uv3.y, uv4.x, uv4.y);
    }

    public void addImageQuad(long textureID, float p1X, float p1Y, float p2X, float p2Y, float p3X, float p3Y, float p4X, float p4Y, float uv1X, float uv1Y, float uv2X, float uv2Y, float uv3X, float uv3Y, float uv4X, float uv4Y) {
        nAddImageQuad(textureID, p1X, p1Y, p2X, p2Y, p3X, p3Y, p4X, p4Y, uv1X, uv1Y, uv2X, uv2Y, uv3X, uv3Y, uv4X, uv4Y);
    }

    public void addImageQuad(long textureID, ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, ImVec2 uv1, ImVec2 uv2, ImVec2 uv3, ImVec2 uv4, int col) {
        nAddImageQuad(textureID, p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y, uv1.x, uv1.y, uv2.x, uv2.y, uv3.x, uv3.y, uv4.x, uv4.y, col);
    }

    public void addImageQuad(long textureID, float p1X, float p1Y, float p2X, float p2Y, float p3X, float p3Y, float p4X, float p4Y, float uv1X, float uv1Y, float uv2X, float uv2Y, float uv3X, float uv3Y, float uv4X, float uv4Y, int col) {
        nAddImageQuad(textureID, p1X, p1Y, p2X, p2Y, p3X, p3Y, p4X, p4Y, uv1X, uv1Y, uv2X, uv2Y, uv3X, uv3Y, uv4X, uv4Y, col);
    }

    public void addImageRounded(long textureID, ImVec2 pMin, ImVec2 pMax, ImVec2 uvMin, ImVec2 uvMax, int col, float rounding) {
        nAddImageRounded(textureID, pMin.x, pMin.y, pMax.x, pMax.y, uvMin.x, uvMin.y, uvMax.x, uvMax.y, col, rounding);
    }

    public void addImageRounded(long textureID, float pMinX, float pMinY, float pMaxX, float pMaxY, float uvMinX, float uvMinY, float uvMaxX, float uvMaxY, int col, float rounding) {
        nAddImageRounded(textureID, pMinX, pMinY, pMaxX, pMaxY, uvMinX, uvMinY, uvMaxX, uvMaxY, col, rounding);
    }

    public void addImageRounded(long textureID, ImVec2 pMin, ImVec2 pMax, ImVec2 uvMin, ImVec2 uvMax, int col, float rounding, int imDrawFlags) {
        nAddImageRounded(textureID, pMin.x, pMin.y, pMax.x, pMax.y, uvMin.x, uvMin.y, uvMax.x, uvMax.y, col, rounding, imDrawFlags);
    }

    public void addImageRounded(long textureID, float pMinX, float pMinY, float pMaxX, float pMaxY, float uvMinX, float uvMinY, float uvMaxX, float uvMaxY, int col, float rounding, int imDrawFlags) {
        nAddImageRounded(textureID, pMinX, pMinY, pMaxX, pMaxY, uvMinX, uvMinY, uvMaxX, uvMaxY, col, rounding, imDrawFlags);
    }

    public void pathClear() {
        nPathClear();
    }

    public void pathLineTo(ImVec2 pos) {
        nPathLineTo(pos.x, pos.y);
    }

    public void pathLineTo(float posX, float posY) {
        nPathLineTo(posX, posY);
    }

    public void pathLineToMergeDuplicate(ImVec2 pos) {
        nPathLineToMergeDuplicate(pos.x, pos.y);
    }

    public void pathLineToMergeDuplicate(float posX, float posY) {
        nPathLineToMergeDuplicate(posX, posY);
    }

    public void pathFillConvex(int col) {
        nPathFillConvex(col);
    }

    public void pathFillConcave(int col) {
        nPathFillConcave(col);
    }

    public void pathStroke(int col) {
        nPathStroke(col);
    }

    public void pathStroke(int col, int imDrawFlags) {
        nPathStroke(col, imDrawFlags);
    }

    public void pathStroke(int col, int imDrawFlags, float thickness) {
        nPathStroke(col, imDrawFlags, thickness);
    }

    public void pathStroke(int col, float thickness) {
        nPathStroke(col, thickness);
    }

    public void pathArcTo(ImVec2 center, float radius, float aMin, float aMax) {
        nPathArcTo(center.x, center.y, radius, aMin, aMax);
    }

    public void pathArcTo(float centerX, float centerY, float radius, float aMin, float aMax) {
        nPathArcTo(centerX, centerY, radius, aMin, aMax);
    }

    public void pathArcTo(ImVec2 center, float radius, float aMin, float aMax, int numSegments) {
        nPathArcTo(center.x, center.y, radius, aMin, aMax, numSegments);
    }

    public void pathArcTo(float centerX, float centerY, float radius, float aMin, float aMax, int numSegments) {
        nPathArcTo(centerX, centerY, radius, aMin, aMax, numSegments);
    }

    public void pathArcToFast(ImVec2 center, float radius, int aMinOf12, int aMaxOf12) {
        nPathArcToFast(center.x, center.y, radius, aMinOf12, aMaxOf12);
    }

    public void pathArcToFast(float centerX, float centerY, float radius, int aMinOf12, int aMaxOf12) {
        nPathArcToFast(centerX, centerY, radius, aMinOf12, aMaxOf12);
    }

    public void pathEllipticalArcTo(ImVec2 center, ImVec2 radius, float rot, float aMin, float aMax) {
        nPathEllipticalArcTo(center.x, center.y, radius.x, radius.y, rot, aMin, aMax);
    }

    public void pathEllipticalArcTo(float centerX, float centerY, float radiusX, float radiusY, float rot, float aMin, float aMax) {
        nPathEllipticalArcTo(centerX, centerY, radiusX, radiusY, rot, aMin, aMax);
    }

    public void pathEllipticalArcTo(ImVec2 center, ImVec2 radius, float rot, float aMin, float aMax, int numSegments) {
        nPathEllipticalArcTo(center.x, center.y, radius.x, radius.y, rot, aMin, aMax, numSegments);
    }

    public void pathEllipticalArcTo(float centerX, float centerY, float radiusX, float radiusY, float rot, float aMin, float aMax, int numSegments) {
        nPathEllipticalArcTo(centerX, centerY, radiusX, radiusY, rot, aMin, aMax, numSegments);
    }

    public void pathBezierCubicCurveTo(ImVec2 p2, ImVec2 p3, ImVec2 p4) {
        nPathBezierCubicCurveTo(p2.x, p2.y, p3.x, p3.y, p4.x, p4.y);
    }

    public void pathBezierCubicCurveTo(float p2X, float p2Y, float p3X, float p3Y, float p4X, float p4Y) {
        nPathBezierCubicCurveTo(p2X, p2Y, p3X, p3Y, p4X, p4Y);
    }

    public void pathBezierCubicCurveTo(ImVec2 p2, ImVec2 p3, ImVec2 p4, int numSegments) {
        nPathBezierCubicCurveTo(p2.x, p2.y, p3.x, p3.y, p4.x, p4.y, numSegments);
    }

    public void pathBezierCubicCurveTo(float p2X, float p2Y, float p3X, float p3Y, float p4X, float p4Y, int numSegments) {
        nPathBezierCubicCurveTo(p2X, p2Y, p3X, p3Y, p4X, p4Y, numSegments);
    }

    public void pathBezierQuadraticCurveTo(ImVec2 p2, ImVec2 p3) {
        nPathBezierQuadraticCurveTo(p2.x, p2.y, p3.x, p3.y);
    }

    public void pathBezierQuadraticCurveTo(float p2X, float p2Y, float p3X, float p3Y) {
        nPathBezierQuadraticCurveTo(p2X, p2Y, p3X, p3Y);
    }

    public void pathBezierQuadraticCurveTo(ImVec2 p2, ImVec2 p3, int numSegments) {
        nPathBezierQuadraticCurveTo(p2.x, p2.y, p3.x, p3.y, numSegments);
    }

    public void pathBezierQuadraticCurveTo(float p2X, float p2Y, float p3X, float p3Y, int numSegments) {
        nPathBezierQuadraticCurveTo(p2X, p2Y, p3X, p3Y, numSegments);
    }

    public void pathRect(ImVec2 rectMin, ImVec2 rectMax) {
        nPathRect(rectMin.x, rectMin.y, rectMax.x, rectMax.y);
    }

    public void pathRect(float rectMinX, float rectMinY, float rectMaxX, float rectMaxY) {
        nPathRect(rectMinX, rectMinY, rectMaxX, rectMaxY);
    }

    public void pathRect(ImVec2 rectMin, ImVec2 rectMax, float rounding) {
        nPathRect(rectMin.x, rectMin.y, rectMax.x, rectMax.y, rounding);
    }

    public void pathRect(float rectMinX, float rectMinY, float rectMaxX, float rectMaxY, float rounding) {
        nPathRect(rectMinX, rectMinY, rectMaxX, rectMaxY, rounding);
    }

    public void pathRect(ImVec2 rectMin, ImVec2 rectMax, float rounding, int imDrawFlags) {
        nPathRect(rectMin.x, rectMin.y, rectMax.x, rectMax.y, rounding, imDrawFlags);
    }

    public void pathRect(float rectMinX, float rectMinY, float rectMaxX, float rectMaxY, float rounding, int imDrawFlags) {
        nPathRect(rectMinX, rectMinY, rectMaxX, rectMaxY, rounding, imDrawFlags);
    }

    public void pathRect(ImVec2 rectMin, ImVec2 rectMax, int imDrawFlags) {
        nPathRect(rectMin.x, rectMin.y, rectMax.x, rectMax.y, imDrawFlags);
    }

    public void pathRect(float rectMinX, float rectMinY, float rectMaxX, float rectMaxY, int imDrawFlags) {
        nPathRect(rectMinX, rectMinY, rectMaxX, rectMaxY, imDrawFlags);
    }

    public void channelsSplit(int count) {
        nChannelsSplit(count);
    }

    public void channelsMerge() {
        nChannelsMerge();
    }

    public void channelsSetCurrent(int n) {
        nChannelsSetCurrent(n);
    }

    public void primReserve(int idxCount, int vtxCount) {
        nPrimReserve(idxCount, vtxCount);
    }

    public void primUnreserve(int idxCount, int vtxCount) {
        nPrimUnreserve(idxCount, vtxCount);
    }

    public void primRect(ImVec2 a, ImVec2 b, int col) {
        nPrimRect(a.x, a.y, b.x, b.y, col);
    }

    public void primRect(float aX, float aY, float bX, float bY, int col) {
        nPrimRect(aX, aY, bX, bY, col);
    }

    public void primRectUV(ImVec2 a, ImVec2 b, ImVec2 uvA, ImVec2 uvB, int col) {
        nPrimRectUV(a.x, a.y, b.x, b.y, uvA.x, uvA.y, uvB.x, uvB.y, col);
    }

    public void primRectUV(float aX, float aY, float bX, float bY, float uvAX, float uvAY, float uvBX, float uvBY, int col) {
        nPrimRectUV(aX, aY, bX, bY, uvAX, uvAY, uvBX, uvBY, col);
    }

    public void primQuadUV(ImVec2 a, ImVec2 b, ImVec2 c, ImVec2 d, ImVec2 uvA, ImVec2 uvB, ImVec2 uvC, ImVec2 uvD, int col) {
        nPrimQuadUV(a.x, a.y, b.x, b.y, c.x, c.y, d.x, d.y, uvA.x, uvA.y, uvB.x, uvB.y, uvC.x, uvC.y, uvD.x, uvD.y, col);
    }

    public void primQuadUV(float aX, float aY, float bX, float bY, float cX, float cY, float dX, float dY, float uvAX, float uvAY, float uvBX, float uvBY, float uvCX, float uvCY, float uvDX, float uvDY, int col) {
        nPrimQuadUV(aX, aY, bX, bY, cX, cY, dX, dY, uvAX, uvAY, uvBX, uvBY, uvCX, uvCY, uvDX, uvDY, col);
    }

    public void primWriteVtx(ImVec2 pos, ImVec2 uv, int col) {
        nPrimWriteVtx(pos.x, pos.y, uv.x, uv.y, col);
    }

    public void primWriteVtx(float posX, float posY, float uvX, float uvY, int col) {
        nPrimWriteVtx(posX, posY, uvX, uvY, col);
    }

    public void primWriteIdx(int idx) {
        nPrimWriteIdx(idx);
    }

    public void primVtx(ImVec2 pos, ImVec2 uv, int col) {
        nPrimVtx(pos.x, pos.y, uv.x, uv.y, col);
    }

    public void primVtx(float posX, float posY, float uvX, float uvY, int col) {
        nPrimVtx(posX, posY, uvX, uvY, col);
    }
}
