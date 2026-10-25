package imgui.type;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/type/ImBoolean.class */
public final class ImBoolean implements Cloneable, Comparable<ImBoolean> {
    private final boolean[] data = {false};

    public ImBoolean() {
    }

    public ImBoolean(ImBoolean imBoolean) {
        this.data[0] = imBoolean.data[0];
    }

    public ImBoolean(boolean value) {
        this.data[0] = value;
    }

    public boolean get() {
        return this.data[0];
    }

    public boolean[] getData() {
        return this.data;
    }

    public void set(boolean value) {
        this.data[0] = value;
    }

    public void set(ImBoolean value) {
        set(value.get());
    }

    public String toString() {
        return String.valueOf(this.data[0]);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImBoolean imBoolean = (ImBoolean) o;
        return this.data[0] == imBoolean.data[0];
    }

    public int hashCode() {
        return Boolean.hashCode(this.data[0]);
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ImBoolean m20clone() {
        return new ImBoolean(this);
    }

    @Override // java.lang.Comparable
    public int compareTo(ImBoolean o) {
        return Boolean.compare(get(), o.get());
    }
}
