package imgui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/ImFontGlyphRangesBuilder.class */
public final class ImFontGlyphRangesBuilder {
    private static final int UNICODE_CODEPOINT_MAX = 65535;
    private final long[] usedChars = new long[8192];

    public ImFontGlyphRangesBuilder() {
        clear();
    }

    public void addChar(char c) {
        setBit(c);
    }

    public void addText(String text) {
        for (int i = 0; i < text.length(); i++) {
            addChar(text.charAt(i));
        }
    }

    public void addRanges(short[] ranges) {
        for (int i = 0; i < ranges.length && ranges[i] != 0; i += 2) {
            for (int k = ranges[i]; k <= ranges[i + 1]; k++) {
                addChar((char) k);
            }
        }
    }

    public short[] buildRanges() {
        List<Short> out = new ArrayList<>();
        int n = 0;
        while (n <= UNICODE_CODEPOINT_MAX) {
            if (getBit(n)) {
                out.add(Short.valueOf((short) n));
                while (n < UNICODE_CODEPOINT_MAX && getBit(n + 1)) {
                    n++;
                }
                out.add(Short.valueOf((short) n));
            }
            n++;
        }
        short[] result = new short[out.size() + 1];
        for (int i = 0; i < out.size(); i++) {
            result[i] = out.get(i).shortValue();
        }
        result[result.length - 1] = 0;
        return result;
    }

    public void clear() {
        Arrays.fill(this.usedChars, 0L);
    }

    public void setBit(int n) {
        int off = n >> 5;
        long mask = 1 << ((int) (((long) n) & 31));
        long[] jArr = this.usedChars;
        jArr[off] = jArr[off] | mask;
    }

    public boolean getBit(int n) {
        int off = n >> 5;
        long mask = 1 << ((int) (((long) n) & 31));
        return (this.usedChars[off] & mask) > 0;
    }
}
