package net.anubis.client.gui;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.Arrays;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/GifDecoder.class */
@Environment(EnvType.CLIENT)
final class GifDecoder {
    private static final int MAX_CANVAS = 4096;
    private static final int IMAGE = 44;
    private static final int EXTENSION = 33;
    private static final int GRAPHIC_CONTROL = 249;
    private static final int DISPOSE_BACKGROUND = 2;
    private static final int DISPOSE_PREVIOUS = 3;
    private static final int MAX_CODES = 4096;
    private final byte[] data;
    private final int[] globalPalette;
    private final int blocksStart;
    private final int frameCount;
    private int pos = 6;
    private final int width = u16();
    private final int height = u16();

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/GifDecoder$Sink.class */
    @Environment(EnvType.CLIENT)
    interface Sink {
        boolean accept(Frame frame);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/GifDecoder$Frame.class */
    @Environment(EnvType.CLIENT)
    static final class Frame extends Record {
        private final int[] argb;
        private final int width;
        private final int height;
        private final int delayMs;

        Frame(int[] argb, int width, int height, int delayMs) {
            this.argb = argb;
            this.width = width;
            this.height = height;
            this.delayMs = delayMs;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Frame.class), Frame.class, "argb;width;height;delayMs", "FIELD:Lnet/anubis/client/gui/GifDecoder$Frame;->argb:[I", "FIELD:Lnet/anubis/client/gui/GifDecoder$Frame;->width:I", "FIELD:Lnet/anubis/client/gui/GifDecoder$Frame;->height:I", "FIELD:Lnet/anubis/client/gui/GifDecoder$Frame;->delayMs:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Frame.class), Frame.class, "argb;width;height;delayMs", "FIELD:Lnet/anubis/client/gui/GifDecoder$Frame;->argb:[I", "FIELD:Lnet/anubis/client/gui/GifDecoder$Frame;->width:I", "FIELD:Lnet/anubis/client/gui/GifDecoder$Frame;->height:I", "FIELD:Lnet/anubis/client/gui/GifDecoder$Frame;->delayMs:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Frame.class, Object.class), Frame.class, "argb;width;height;delayMs", "FIELD:Lnet/anubis/client/gui/GifDecoder$Frame;->argb:[I", "FIELD:Lnet/anubis/client/gui/GifDecoder$Frame;->width:I", "FIELD:Lnet/anubis/client/gui/GifDecoder$Frame;->height:I", "FIELD:Lnet/anubis/client/gui/GifDecoder$Frame;->delayMs:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int[] argb() {
            return this.argb;
        }

        public int width() {
            return this.width;
        }

        public int height() {
            return this.height;
        }

        public int delayMs() {
            return this.delayMs;
        }
    }

    private GifDecoder(byte[] data) throws IOException {
        this.data = data;
        int flags = u8();
        this.pos += 2;
        this.globalPalette = (flags & 128) != 0 ? palette(flags) : null;
        this.blocksStart = this.pos;
        if (this.width == 0 || this.height == 0) {
            throw new IOException("GIF has an empty canvas");
        }
        if (this.width > 4096 || this.height > 4096) {
            throw new IOException("GIF is " + this.width + "x" + this.height + ", larger than 4096 px");
        }
        this.frameCount = countFrames();
        if (this.frameCount == 0) {
            throw new IOException("GIF has no frames");
        }
    }

    static GifDecoder open(byte[] data) throws IOException {
        if (data.length < 13 || data[0] != 71 || data[1] != 73 || data[2] != 70) {
            throw new IOException("Not a GIF file");
        }
        return new GifDecoder(data);
    }

    int width() {
        return this.width;
    }

    int height() {
        return this.height;
    }

    int frameCount() {
        return this.frameCount;
    }

    void decode(int outW, int outH, int maxFrames, Sink sink) {
        int[] canvas = new int[this.width * this.height];
        int[] saved = null;
        int dispose = 0;
        int delay = 0;
        int transparent = -1;
        int lastDispose = 0;
        int lastX = 0;
        int lastY = 0;
        int lastW = 0;
        int lastH = 0;
        int emitted = 0;
        this.pos = this.blocksStart;
        while (emitted < maxFrames && this.pos < this.data.length) {
            int block = u8();
            if (block == 33) {
                if (u8() == GRAPHIC_CONTROL) {
                    int size = u8();
                    int end = this.pos + size;
                    int flags = u8();
                    dispose = (flags >> 2) & 7;
                    delay = u16();
                    int index = u8();
                    transparent = (flags & 1) != 0 ? index : -1;
                    this.pos = end;
                }
                skipSubBlocks();
            } else if (block == 44) {
                int x = u16();
                int y = u16();
                int w = u16();
                int h = u16();
                int flags2 = u8();
                int[] palette = (flags2 & 128) != 0 ? palette(flags2) : this.globalPalette;
                boolean interlaced = (flags2 & 64) != 0;
                int minCodeSize = u8();
                byte[] compressed = readSubBlocks();
                if (((long) w) * ((long) h) > ((long) this.width) * ((long) this.height)) {
                    return;
                }
                if (lastDispose == 2) {
                    clear(canvas, lastX, lastY, lastW, lastH);
                } else if (lastDispose == 3 && saved != null) {
                    System.arraycopy(saved, 0, canvas, 0, canvas.length);
                }
                if (dispose == 3) {
                    if (saved == null) {
                        saved = new int[canvas.length];
                    }
                    System.arraycopy(canvas, 0, saved, 0, canvas.length);
                }
                byte[] indices = new byte[w * h];
                int count = lzw(compressed, minCodeSize, indices);
                if (palette != null) {
                    draw(canvas, indices, count, palette, transparent, x, y, w, h, interlaced);
                }
                int[] pixels = (outW == this.width && outH == this.height) ? (int[]) canvas.clone() : downscale(canvas, this.width, this.height, outW, outH);
                if (!sink.accept(new Frame(pixels, outW, outH, delayMs(delay)))) {
                    return;
                }
                emitted++;
                lastDispose = dispose;
                lastX = x;
                lastY = y;
                lastW = w;
                lastH = h;
                dispose = 0;
                delay = 0;
                transparent = -1;
            } else {
                return;
            }
        }
    }

    private static int delayMs(int centiseconds) {
        if (centiseconds <= 1) {
            return 100;
        }
        return centiseconds * 10;
    }

    private static int lzw(byte[] in, int minCodeSize, byte[] out) {
        if (minCodeSize < 1 || minCodeSize > 8) {
            return 0;
        }
        int clear = 1 << minCodeSize;
        int end = clear + 1;
        short[] prefix = new short[4096];
        byte[] suffix = new byte[4096];
        byte[] stack = new byte[4097];
        for (int i = 0; i < clear; i++) {
            suffix[i] = (byte) i;
        }
        int codeSize = minCodeSize + 1;
        int next = clear + 2;
        int previous = -1;
        byte first = 0;
        int bits = 0;
        int buffer = 0;
        int read = 0;
        int written = 0;
        while (written < out.length) {
            while (bits < codeSize) {
                if (read >= in.length) {
                    return written;
                }
                int i2 = read;
                read++;
                buffer |= (in[i2] & 255) << bits;
                bits += 8;
            }
            int code = buffer & ((1 << codeSize) - 1);
            buffer >>>= codeSize;
            bits -= codeSize;
            if (code == clear) {
                codeSize = minCodeSize + 1;
                next = clear + 2;
                previous = -1;
            } else {
                if (code == end) {
                    break;
                }
                if (previous == -1) {
                    if (code >= clear) {
                        break;
                    }
                    first = suffix[code];
                    int i3 = written;
                    written++;
                    out[i3] = first;
                    previous = code;
                } else {
                    int top = 0;
                    if (code >= next) {
                        if (code > next) {
                            break;
                        }
                        top = 0 + 1;
                        stack[0] = first;
                        code = previous;
                    }
                    while (code >= clear) {
                        int i4 = top;
                        top++;
                        stack[i4] = suffix[code];
                        code = prefix[code];
                    }
                    first = suffix[code];
                    int i5 = top;
                    int top2 = top + 1;
                    stack[i5] = first;
                    if (next < 4096) {
                        prefix[next] = (short) previous;
                        suffix[next] = first;
                        next++;
                        if (next == (1 << codeSize) && codeSize < 12) {
                            codeSize++;
                        }
                    }
                    previous = code;
                    while (top2 > 0 && written < out.length) {
                        int i6 = written;
                        written++;
                        top2--;
                        out[i6] = stack[top2];
                    }
                }
            }
        }
        return written;
    }

    static int[] downscale(int[] src, int srcW, int srcH, int outW, int outH) {
        int[] out = new int[outW * outH];
        for (int oy = 0; oy < outH; oy++) {
            int y0 = (int) ((((long) oy) * ((long) srcH)) / ((long) outH));
            int y1 = Math.max(y0 + 1, (int) ((((long) (oy + 1)) * ((long) srcH)) / ((long) outH)));
            for (int ox = 0; ox < outW; ox++) {
                int x0 = (int) ((((long) ox) * ((long) srcW)) / ((long) outW));
                int x1 = Math.max(x0 + 1, (int) ((((long) (ox + 1)) * ((long) srcW)) / ((long) outW)));
                long a = 0;
                long r = 0;
                long g = 0;
                long b = 0;
                for (int sy = y0; sy < y1; sy++) {
                    int row = sy * srcW;
                    for (int sx = x0; sx < x1; sx++) {
                        int p = src[row + sx];
                        int alpha = p >>> 24;
                        a += (long) alpha;
                        r += ((long) ((p >> 16) & 255)) * ((long) alpha);
                        g += ((long) ((p >> 8) & 255)) * ((long) alpha);
                        b += ((long) (p & 255)) * ((long) alpha);
                    }
                }
                if (a != 0) {
                    int n = (y1 - y0) * (x1 - x0);
                    out[(oy * outW) + ox] = (((int) (a / ((long) n))) << 24) | (((int) (r / a)) << 16) | (((int) (g / a)) << 8) | ((int) (b / a));
                }
            }
        }
        return out;
    }

    private void draw(int[] canvas, byte[] indices, int count, int[] palette, int transparent, int fx, int fy, int fw, int fh, boolean interlaced) {
        int[] rows = rowOrder(fh, interlaced);
        int i = 0;
        for (int r = 0; r < fh && i < count; r++) {
            int end = Math.min(count, i + fw);
            int cy = fy + rows[r];
            if (cy >= this.height) {
                i = end;
            } else {
                int base = cy * this.width;
                int cx = fx;
                while (i < end) {
                    int index = indices[i] & 255;
                    if (index != transparent && index < palette.length && cx < this.width) {
                        canvas[base + cx] = palette[index];
                    }
                    i++;
                    cx++;
                }
            }
        }
    }

    private static int[] rowOrder(int height, boolean interlaced) {
        int[] rows = new int[height];
        if (!interlaced) {
            for (int i = 0; i < height; i++) {
                rows[i] = i;
            }
            return rows;
        }
        int[] starts = {0, 4, 2, 1};
        int[] steps = {8, 8, 4, 2};
        int i2 = 0;
        for (int pass = 0; pass < 4; pass++) {
            int i3 = starts[pass];
            while (true) {
                int y = i3;
                if (y < height) {
                    int i4 = i2;
                    i2++;
                    rows[i4] = y;
                    i3 = y + steps[pass];
                }
            }
        }
        return rows;
    }

    private void clear(int[] canvas, int x, int y, int w, int h) {
        int x1 = Math.min(this.width, x + w);
        int y1 = Math.min(this.height, y + h);
        if (x >= x1) {
            return;
        }
        for (int row = y; row < y1; row++) {
            Arrays.fill(canvas, (row * this.width) + x, (row * this.width) + x1, 0);
        }
    }

    private int countFrames() {
        int frames = 0;
        this.pos = this.blocksStart;
        while (this.pos < this.data.length) {
            int block = u8();
            if (block != 44) {
                if (block != 33) {
                    break;
                }
                this.pos++;
                skipSubBlocks();
            } else {
                frames++;
                this.pos += 8;
                int flags = u8();
                if ((flags & 128) != 0) {
                    this.pos += 3 * (2 << (flags & 7));
                }
                this.pos++;
                skipSubBlocks();
            }
        }
        return frames;
    }

    private int[] palette(int flags) {
        int[] palette = new int[2 << (flags & 7)];
        for (int i = 0; i < palette.length; i++) {
            palette[i] = (-16777216) | (u8() << 16) | (u8() << 8) | u8();
        }
        return palette;
    }

    private void skipSubBlocks() {
        int size;
        while (this.pos < this.data.length && (size = u8()) != 0) {
            this.pos += size;
        }
    }

    private byte[] readSubBlocks() {
        int size;
        int size2;
        int total = 0;
        int i = this.pos;
        while (true) {
            int p = i;
            if (p >= this.data.length || (size2 = this.data[p] & 255) == 0) {
                break;
            }
            total += size2;
            i = p + 1 + size2;
        }
        byte[] out = new byte[total];
        int n = 0;
        while (this.pos < this.data.length && (size = u8()) != 0) {
            int copy = Math.min(size, this.data.length - this.pos);
            System.arraycopy(this.data, this.pos, out, n, copy);
            n += copy;
            this.pos += size;
        }
        return n == total ? out : Arrays.copyOf(out, n);
    }

    private int u8() {
        if (this.pos >= this.data.length) {
            return 0;
        }
        byte[] bArr = this.data;
        int i = this.pos;
        this.pos = i + 1;
        return bArr[i] & 255;
    }

    private int u16() {
        return u8() | (u8() << 8);
    }
}
