package imgui.extension.implot;

import java.util.Objects;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/extension/implot/ImPlotRange.class */
public final class ImPlotRange implements Cloneable {
    public double min;
    public double max;

    public ImPlotRange() {
    }

    public ImPlotRange(double min, double max) {
        set(min, max);
    }

    public ImPlotRange(ImPlotRange value) {
        set(value.min, value.max);
    }

    public boolean contains(double value) {
        return value >= this.min && value <= this.max;
    }

    public double size() {
        return this.max - this.min;
    }

    public double clamp(double value) {
        return Math.max(this.min, Math.min(this.max, value));
    }

    public ImPlotRange set(double min, double max) {
        this.min = min;
        this.max = max;
        return this;
    }

    public ImPlotRange set(ImPlotRange value) {
        return set(value.min, value.max);
    }

    public ImPlotRange plus(double min, double max) {
        this.min += min;
        this.max += max;
        return this;
    }

    public ImPlotRange plus(ImPlotRange value) {
        return plus(value.min, value.max);
    }

    public ImPlotRange minus(double min, double max) {
        this.min -= min;
        this.max -= max;
        return this;
    }

    public ImPlotRange minus(ImPlotRange value) {
        return minus(value.min, value.max);
    }

    public ImPlotRange times(double min, double max) {
        this.min *= min;
        this.max *= max;
        return this;
    }

    public ImPlotRange times(ImPlotRange value) {
        return times(value.min, value.max);
    }

    public String toString() {
        return "ImPlotRange{min=" + this.min + ", max=" + this.max + '}';
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImPlotRange ImPlotRange = (ImPlotRange) o;
        return Double.compare(ImPlotRange.min, this.min) == 0 && Double.compare(ImPlotRange.max, this.max) == 0;
    }

    public int hashCode() {
        return Objects.hash(Double.valueOf(this.min), Double.valueOf(this.max));
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ImPlotRange m13clone() {
        return new ImPlotRange(this);
    }
}
