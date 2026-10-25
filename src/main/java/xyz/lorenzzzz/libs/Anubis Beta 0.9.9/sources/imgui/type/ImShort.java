package imgui.type;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/type/ImShort.class */
public final class ImShort extends Number implements Cloneable, Comparable<ImShort> {
    private final short[] data = {0};

    public ImShort() {
    }

    public ImShort(ImShort imShort) {
        this.data[0] = imShort.data[0];
    }

    public ImShort(short value) {
        set(value);
    }

    public short get() {
        return this.data[0];
    }

    public short[] getData() {
        return this.data;
    }

    public void set(short value) {
        this.data[0] = value;
    }

    public void set(ImShort value) {
        set(value.get());
    }

    public String toString() {
        return String.valueOf((int) get());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImShort imShort = (ImShort) o;
        return this.data[0] == imShort.data[0];
    }

    public int hashCode() {
        return Short.hashCode(this.data[0]);
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ImShort m25clone() {
        return new ImShort(this);
    }

    @Override // java.lang.Comparable
    public int compareTo(ImShort o) {
        return Short.compare(get(), o.get());
    }

    @Override // java.lang.Number
    public int intValue() {
        return get();
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
