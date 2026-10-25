package imgui.type;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/type/ImFloat.class */
public final class ImFloat extends Number implements Cloneable, Comparable<ImFloat> {
    private final float[] data = {0.0f};

    public ImFloat() {
    }

    public ImFloat(ImFloat imFloat) {
        this.data[0] = imFloat.data[0];
    }

    public ImFloat(float value) {
        set(value);
    }

    public float get() {
        return this.data[0];
    }

    public float[] getData() {
        return this.data;
    }

    public void set(float value) {
        this.data[0] = value;
    }

    public void set(ImFloat value) {
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
        ImFloat imFloat = (ImFloat) o;
        return this.data[0] == imFloat.data[0];
    }

    public int hashCode() {
        return Float.hashCode(this.data[0]);
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ImFloat m22clone() {
        return new ImFloat(this);
    }

    @Override // java.lang.Comparable
    public int compareTo(ImFloat o) {
        return Float.compare(get(), o.get());
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
        return get();
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return get();
    }
}
