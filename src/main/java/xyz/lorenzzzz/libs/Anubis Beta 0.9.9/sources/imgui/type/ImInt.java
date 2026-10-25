package imgui.type;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/type/ImInt.class */
public final class ImInt extends Number implements Cloneable, Comparable<ImInt> {
    private final int[] data = {0};

    public ImInt() {
    }

    public ImInt(ImInt imInt) {
        this.data[0] = imInt.data[0];
    }

    public ImInt(int value) {
        set(value);
    }

    public int get() {
        return this.data[0];
    }

    public int[] getData() {
        return this.data;
    }

    public void set(int value) {
        this.data[0] = value;
    }

    public void set(ImInt value) {
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
        ImInt imInt = (ImInt) o;
        return this.data[0] == imInt.data[0];
    }

    public int hashCode() {
        return Integer.hashCode(this.data[0]);
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ImInt m23clone() {
        return new ImInt(this);
    }

    @Override // java.lang.Comparable
    public int compareTo(ImInt o) {
        return Integer.compare(get(), o.get());
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
