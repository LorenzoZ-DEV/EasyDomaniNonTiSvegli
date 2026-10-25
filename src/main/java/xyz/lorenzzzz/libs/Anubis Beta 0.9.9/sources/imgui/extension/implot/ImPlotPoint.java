package imgui.extension.implot;

import imgui.ImVec2;
import java.util.Objects;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/extension/implot/ImPlotPoint.class */
public final class ImPlotPoint implements Cloneable {
    public double x;
    public double y;

    public ImPlotPoint() {
    }

    public ImPlotPoint(double x, double y) {
        set(x, y);
    }

    public ImPlotPoint(ImVec2 value) {
        set(value.x, value.y);
    }

    public ImPlotPoint(ImPlotPoint value) {
        set(value.x, value.y);
    }

    public ImPlotPoint set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public ImPlotPoint set(ImPlotPoint value) {
        return set(value.x, value.y);
    }

    public ImPlotPoint set(ImVec2 value) {
        return set(value.x, value.y);
    }

    public ImPlotPoint plus(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public ImPlotPoint plus(ImPlotPoint value) {
        return plus(value.x, value.y);
    }

    public ImPlotPoint minus(double x, double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public ImPlotPoint minus(ImPlotPoint value) {
        return minus(value.x, value.y);
    }

    public ImPlotPoint times(double x, double y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    public ImPlotPoint times(ImPlotPoint value) {
        return times(value.x, value.y);
    }

    public String toString() {
        return "ImPlotPoint{x=" + this.x + ", y=" + this.y + '}';
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImPlotPoint imPlotPoint = (ImPlotPoint) o;
        return Double.compare(imPlotPoint.x, this.x) == 0 && Double.compare(imPlotPoint.y, this.y) == 0;
    }

    public int hashCode() {
        return Objects.hash(Double.valueOf(this.x), Double.valueOf(this.y));
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ImPlotPoint m12clone() {
        return new ImPlotPoint(this);
    }
}
