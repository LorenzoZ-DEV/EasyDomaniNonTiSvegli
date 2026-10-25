package net.anubis.client.module.impl.render.character.kit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Atlas.class */
@Environment(EnvType.CLIENT)
final class Atlas {
    static final int WHITE = 16;
    private static final int PAD = 3;
    private static final int MAX = 2048;
    private static final int LEVELS = 5;
    final int width;
    final int height;
    final int[][] mips;
    private final Map<Canvas, int[]> rects;

    private Atlas(int width, int height, int[][] mips, Map<Canvas, int[]> rects) {
        this.width = width;
        this.height = height;
        this.mips = mips;
        this.rects = rects;
    }

    float whiteU() {
        return 8.0f / this.width;
    }

    float whiteV() {
        return 8.0f / this.height;
    }

    float u(Canvas c, float u) {
        int[] r = this.rects.get(c);
        return (r[0] + (u * r[2])) / this.width;
    }

    float v(Canvas c, float v) {
        int[] r = this.rects.get(c);
        return (r[1] + (v * r[3])) / this.height;
    }

    static Atlas pack(List<Canvas> input) {
        int width;
        Map<Canvas, int[]> rects;
        int height;
        List<Canvas> canvases = new ArrayList<>();
        for (Canvas c : input) {
            if (!canvases.contains(c)) {
                canvases.add(c);
            }
        }
        canvases.sort((a, b) -> {
            return Integer.compare(b.height(), a.height());
        });
        long area = 484;
        int widest = 22;
        for (Canvas c2 : canvases) {
            area += ((long) (c2.width() + 6)) * ((long) (c2.height() + 6));
            widest = Math.max(widest, c2.width() + 6);
        }
        int iPow2 = pow2(Math.max(widest, (int) Math.ceil(Math.sqrt(area))));
        while (true) {
            width = iPow2;
            rects = new IdentityHashMap<>();
            int x = 22;
            int y = 0;
            int shelf = 22;
            for (Canvas c3 : canvases) {
                int w = c3.width() + 6;
                int h = c3.height() + 6;
                if (x + w > width) {
                    x = 0;
                    y += shelf;
                    shelf = 0;
                }
                rects.put(c3, new int[]{x + 3, y + 3, c3.width(), c3.height()});
                x += w;
                shelf = Math.max(shelf, h);
            }
            height = pow2(Math.max(22, y + shelf));
            if (height <= 2048 || width >= 2048) {
                break;
            }
            iPow2 = width * 2;
        }
        if (height > 2048) {
            throw new IllegalStateException("decals do not fit into a 2048x2048 atlas");
        }
        return render(width, height, canvases, rects);
    }

    private static Atlas render(int width, int height, List<Canvas> canvases, Map<Canvas, int[]> rects) {
        int[] px = new int[width * height];
        for (int y = 0; y < 19; y++) {
            for (int x = 0; x < 19; x++) {
                px[(y * width) + x] = -1;
            }
        }
        for (Canvas c : canvases) {
            int[] r = rects.get(c);
            int[] src = dilate(c);
            for (int y2 = -3; y2 < c.height() + 3; y2++) {
                int sy = Math.max(0, Math.min(c.height() - 1, y2));
                for (int x2 = -3; x2 < c.width() + 3; x2++) {
                    int sx = Math.max(0, Math.min(c.width() - 1, x2));
                    px[((r[1] + y2) * width) + r[0] + x2] = src[(sy * c.width()) + sx];
                }
            }
        }
        return new Atlas(width, height, mips(px, width, height), rects);
    }

    static int[][] mips(int[] px, int width, int height) {
        int levels = Math.min(5, 1 + (31 - Integer.numberOfLeadingZeros(Math.min(width, height))));
        int[][] mips = new int[levels][];
        mips[0] = px;
        int w = width;
        int h = height;
        for (int l = 1; l < levels; l++) {
            mips[l] = down(mips[l - 1], w, h);
            w = Math.max(1, w / 2);
            h = Math.max(1, h / 2);
        }
        return mips;
    }

    private static int[] dilate(Canvas c) {
        int w = c.width();
        int h = c.height();
        int[] px = Arrays.copyOf(c.pixels, c.pixels.length);
        boolean[] known = new boolean[px.length];
        for (int i = 0; i < px.length; i++) {
            known[i] = (px[i] >>> 24) != 0;
        }
        for (int pass = 0; pass < 4; pass++) {
            boolean[] next = (boolean[]) known.clone();
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int i2 = (y * w) + x;
                    if (!known[i2]) {
                        int r = 0;
                        int g = 0;
                        int b = 0;
                        int n = 0;
                        for (int dy = -1; dy <= 1; dy++) {
                            for (int dx = -1; dx <= 1; dx++) {
                                int nx = x + dx;
                                int ny = y + dy;
                                if (nx >= 0 && ny >= 0 && nx < w && ny < h) {
                                    int j = (ny * w) + nx;
                                    if (known[j]) {
                                        r += (px[j] >> 16) & 255;
                                        g += (px[j] >> 8) & 255;
                                        b += px[j] & 255;
                                        n++;
                                    }
                                }
                            }
                        }
                        if (n != 0) {
                            px[i2] = ((r / n) << 16) | ((g / n) << 8) | (b / n);
                            next[i2] = true;
                        }
                    }
                }
            }
            known = next;
        }
        return px;
    }

    private static int[] down(int[] src, int w, int h) {
        int or;
        int og;
        int iRound;
        int nw = Math.max(1, w / 2);
        int nh = Math.max(1, h / 2);
        int[] out = new int[nw * nh];
        for (int y = 0; y < nh; y++) {
            for (int x = 0; x < nw; x++) {
                float a = 0.0f;
                float r = 0.0f;
                float g = 0.0f;
                float b = 0.0f;
                float rr = 0.0f;
                float gg = 0.0f;
                float bb = 0.0f;
                for (int k = 0; k < 4; k++) {
                    int sx = Math.min(w - 1, (x * 2) + (k & 1));
                    int sy = Math.min(h - 1, (y * 2) + (k >> 1));
                    int p = src[(sy * w) + sx];
                    float pa = (p >>> 24) / 255.0f;
                    a += pa;
                    r += ((p >> 16) & 255) * pa;
                    g += ((p >> 8) & 255) * pa;
                    b += (p & 255) * pa;
                    rr += (p >> 16) & 255;
                    gg += (p >> 8) & 255;
                    bb += p & 255;
                }
                int oa = Math.round((a / 4.0f) * 255.0f);
                if (a > 1.0E-4f) {
                    or = Math.round(r / a);
                    og = Math.round(g / a);
                    iRound = Math.round(b / a);
                } else {
                    or = Math.round(rr / 4.0f);
                    og = Math.round(gg / 4.0f);
                    iRound = Math.round(bb / 4.0f);
                }
                int ob = iRound;
                out[(y * nw) + x] = (Colors.clamp(oa) << 24) | (Colors.clamp(or) << 16) | (Colors.clamp(og) << 8) | Colors.clamp(ob);
            }
        }
        return out;
    }

    private static int pow2(int v) {
        int i = 16;
        while (true) {
            int p = i;
            if (p >= v) {
                return p;
            }
            i = p << 1;
        }
    }
}
