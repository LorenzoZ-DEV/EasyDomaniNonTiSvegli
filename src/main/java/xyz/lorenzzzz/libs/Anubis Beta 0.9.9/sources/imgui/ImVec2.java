package imgui;

import java.util.Objects;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/ImVec2.class */
public final class ImVec2 implements Cloneable {
    public float x;
    public float y;

    public ImVec2() {
    }

    public ImVec2(float x, float y) {
        set(x, y);
    }

    public ImVec2(ImVec2 value) {
        set(value.x, value.y);
    }

    public ImVec2 set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public ImVec2 set(ImVec2 value) {
        return set(value.x, value.y);
    }

    public ImVec2 plus(float x, float y) {
        return new ImVec2(this.x + x, this.y + y);
    }

    public ImVec2 plus(ImVec2 value) {
        return plus(value.x, value.y);
    }

    public ImVec2 minus(float x, float y) {
        return new ImVec2(this.x - x, this.y - y);
    }

    public ImVec2 minus(ImVec2 value) {
        return minus(value.x, value.y);
    }

    public ImVec2 times(float x, float y) {
        return new ImVec2(this.x * x, this.y * y);
    }

    public ImVec2 times(ImVec2 value) {
        return times(value.x, value.y);
    }

    public ImVec2 div(float x, float y) {
        return new ImVec2(this.x / x, this.y / y);
    }

    public ImVec2 div(ImVec2 value) {
        return div(value.x, value.y);
    }

    public String toString() {
        return "ImVec2{x=" + this.x + ", y=" + this.y + '}';
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImVec2 imVec2 = (ImVec2) o;
        return Float.compare(imVec2.x, this.x) == 0 && Float.compare(imVec2.y, this.y) == 0;
    }

    public int hashCode() {
        return Objects.hash(Float.valueOf(this.x), Float.valueOf(this.y));
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ImVec2 m8clone() {
        return new ImVec2(this);
    }
}
