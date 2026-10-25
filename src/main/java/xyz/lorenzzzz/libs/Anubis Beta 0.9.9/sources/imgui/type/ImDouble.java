package imgui.type;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/type/ImDouble.class */
public final class ImDouble extends Number implements Cloneable, Comparable<ImDouble> {
    private final double[] data = {0.0d};

    public ImDouble() {
    }

    public ImDouble(ImDouble imDouble) {
        this.data[0] = imDouble.data[0];
    }

    public ImDouble(double value) {
        set(value);
    }

    public double get() {
        return this.data[0];
    }

    public double[] getData() {
        return this.data;
    }

    public void set(double value) {
        this.data[0] = value;
    }

    public void set(ImDouble value) {
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
        ImDouble imDouble = (ImDouble) o;
        return this.data[0] == imDouble.data[0];
    }

    public int hashCode() {
        return Double.hashCode(this.data[0]);
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ImDouble m21clone() {
        return new ImDouble(this);
    }

    @Override // java.lang.Comparable
    public int compareTo(ImDouble o) {
        return Double.compare(get(), o.get());
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) get();
    }

    @Override // java.lang.Number
    public long longValue() {
        return (long) get();
    }

    @Override // java.lang.Number
    public float floatValue() {
        return (float) get();
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return get();
    }
}
