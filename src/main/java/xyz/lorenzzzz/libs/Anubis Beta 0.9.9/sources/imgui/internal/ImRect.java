package imgui.internal;

import imgui.ImVec2;
import java.util.Objects;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/internal/ImRect.class */
public final class ImRect implements Cloneable {
    public final ImVec2 min = new ImVec2();
    public final ImVec2 max = new ImVec2();

    public ImRect() {
    }

    public ImRect(float minX, float minY, float maxX, float maxY) {
        set(minX, minY, maxX, maxY);
    }

    public ImRect(ImVec2 min, ImVec2 max) {
        set(min, max);
    }

    public ImRect(ImRect value) {
        set(value);
    }

    public void set(float minX, float minY, float maxX, float maxY) {
        this.min.x = minX;
        this.min.y = minY;
        this.max.x = maxX;
        this.max.y = maxY;
    }

    public void set(ImVec2 min, ImVec2 max) {
        set(min.x, min.y, max.x, max.y);
    }

    public void set(ImRect value) {
        set(value.min, value.max);
    }

    public String toString() {
        return "ImRect{min=" + this.min + ", max=" + this.max + '}';
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImRect imRect = (ImRect) o;
        return Objects.equals(this.min, imRect.min) && Objects.equals(this.max, imRect.max);
    }

    public int hashCode() {
        return Objects.hash(this.min, this.max);
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ImRect m19clone() {
        return new ImRect(this);
    }
}
