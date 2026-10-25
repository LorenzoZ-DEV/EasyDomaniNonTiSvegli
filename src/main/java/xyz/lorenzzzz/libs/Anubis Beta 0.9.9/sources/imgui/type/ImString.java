package imgui.type;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/type/ImString.class */
public final class ImString implements Cloneable, Comparable<ImString> {
    public static final short DEFAULT_LENGTH = 100;
    public static final short CARET_LEN = 1;
    public final InputData inputData;
    private byte[] data;
    private String text;

    public ImString() {
        this(100);
    }

    public ImString(ImString imString) {
        this(imString.text, imString.data.length);
        this.inputData.allowedChars = imString.inputData.allowedChars;
        this.inputData.isResizable = imString.inputData.isResizable;
        this.inputData.resizeFactor = imString.inputData.resizeFactor;
        this.inputData.size = imString.inputData.size;
        this.inputData.isDirty = imString.inputData.isDirty;
        this.inputData.isResized = imString.inputData.isResized;
    }

    public ImString(int length) {
        this.inputData = new InputData();
        this.text = "";
        this.data = new byte[length + 1];
    }

    public ImString(String text) {
        this.inputData = new InputData();
        this.text = "";
        set(text, true, 0);
    }

    public ImString(String text, int length) {
        this(length);
        set(text);
    }

    public String get() {
        if (this.inputData.isDirty) {
            this.inputData.isDirty = false;
            this.text = new String(this.data, 0, this.inputData.size, StandardCharsets.UTF_8);
        }
        return this.text;
    }

    public byte[] getData() {
        return this.data;
    }

    public void set(Object object) {
        set(String.valueOf(object));
    }

    public void set(ImString value) {
        set(value.get(), true);
    }

    public void set(ImString value, boolean resize) {
        set(value.get(), resize);
    }

    public void set(String value) {
        set(value, this.inputData.isResizable, this.inputData.resizeFactor);
    }

    public void set(String value, boolean resize) {
        set(value, resize, this.inputData.resizeFactor);
    }

    public void set(String value, boolean resize, int resizeValue) {
        byte[] valueBuff = (value == null ? "null" : value).getBytes(StandardCharsets.UTF_8);
        int currentLen = this.data == null ? 0 : this.data.length;
        byte[] newBuff = null;
        if (resize && currentLen - 1 < valueBuff.length) {
            newBuff = new byte[valueBuff.length + resizeValue + 1];
            this.inputData.size = valueBuff.length;
        }
        if (newBuff == null) {
            newBuff = new byte[currentLen];
            this.inputData.size = Math.max(0, Math.min(valueBuff.length, currentLen - 1));
        }
        System.arraycopy(valueBuff, 0, newBuff, 0, Math.min(valueBuff.length, newBuff.length - 1));
        this.data = newBuff;
        this.inputData.isDirty = true;
    }

    public void resize(int newSize) {
        if (newSize < this.data.length) {
            throw new IllegalArgumentException("New size should be greater than current size of the buffer");
        }
        int size = newSize + 1;
        byte[] newBuffer = new byte[size];
        System.arraycopy(this.data, 0, newBuffer, 0, this.data.length);
        this.data = newBuffer;
    }

    byte[] resizeInternal(int newSize) {
        resize(newSize + this.inputData.resizeFactor);
        return this.data;
    }

    public int getLength() {
        return get().length();
    }

    public int getBufferSize() {
        return this.data.length;
    }

    public boolean isEmpty() {
        return getLength() == 0;
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public void clear() {
        set("");
    }

    public String toString() {
        return get();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImString imString = (ImString) o;
        return Objects.equals(this.text, imString.text);
    }

    public int hashCode() {
        return this.text.hashCode();
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ImString m26clone() {
        return new ImString(this);
    }

    @Override // java.lang.Comparable
    public int compareTo(ImString o) {
        return get().compareTo(o.get());
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/type/ImString$InputData.class */
    public static final class InputData {
        private static final short DEFAULT_RESIZE_FACTOR = 10;
        public String allowedChars;
        public boolean isResizable;
        public int resizeFactor;
        int size;
        boolean isDirty;
        boolean isResized;

        private InputData() {
            this.allowedChars = "";
            this.resizeFactor = 10;
            this.isResized = false;
        }
    }
}
