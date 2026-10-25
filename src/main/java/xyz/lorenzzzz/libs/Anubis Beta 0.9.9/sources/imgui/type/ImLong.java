package imgui.type;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/type/ImLong.class */
public final class ImLong extends Number implements Cloneable, Comparable<ImLong> {
    private final long[] data = {0};

    public ImLong() {
    }

    public ImLong(ImLong imLong) {
        this.data[0] = imLong.data[0];
    }

    public ImLong(long value) {
        set(value);
    }

    public long get() {
        return this.data[0];
    }

    public long[] getData() {
        return this.data;
    }

    public void set(long value) {
        this.data[0] = value;
    }

    public void set(ImLong value) {
        set(value.get());
    }

    public String toString() {
        return String.valueOf(get());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImLong imLong = (ImLong) o;
        return this.data[0] == imLong.data[0];
    }

    public int hashCode() {
        return Long.hashCode(this.data[0]);
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ImLong m24clone() {
        return new ImLong(this);
    }

    @Override // java.lang.Comparable
    public int compareTo(ImLong o) {
        return Long.compare(get(), o.get());
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) get();
    }

    @Override // java.lang.Number
    public long longValue() {
        return get();
    }

    @Override // java.lang.Number
    public float floatValue() {
        return get();
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return get();
    }
}
