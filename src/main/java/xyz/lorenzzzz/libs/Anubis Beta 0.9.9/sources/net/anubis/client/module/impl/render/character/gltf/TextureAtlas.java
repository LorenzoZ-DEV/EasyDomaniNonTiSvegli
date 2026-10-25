package net.anubis.client.module.impl.render.character.gltf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/gltf/TextureAtlas.class */
@Environment(EnvType.CLIENT)
final class TextureAtlas {
    private static final int SIZE = 2048;
    private static final int PAD = 4;
    private static final int MAX_SIDE = 8192;
    final int width;
    final int height;
    private final int[] pixels;
    private final Map<Integer, float[]> regions = new HashMap();
    private final Map<Integer, int[]> tiles = new HashMap();

    private TextureAtlas(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];
    }

    static TextureAtlas pack(GltfFile file, Map<Integer, float[]> used, ImageDecoder decoder) throws IOException {
        Map<Integer, ImageDecoder.Picture> crops = new LinkedHashMap<>();
        Map<Integer, float[]> cropUv = new HashMap<>();
        Map<Integer, int[]> tiles = new HashMap<>();
        for (Map.Entry<Integer, float[]> e : used.entrySet()) {
            float[] b = e.getValue();
            int tu = (int) Math.floor(b[0] + 0.001f);
            int tv = (int) Math.floor(b[1] + 0.001f);
            int nu = Math.min(8, Math.max(1, ((int) Math.ceil(b[2] - 0.001f)) - tu));
            int nv = Math.min(8, Math.max(1, ((int) Math.ceil(b[3] - 0.001f)) - tv));
            ImageDecoder.Picture src = tile(baseColor(file, e.getKey().intValue(), decoder), nu, nv);
            tiles.put(e.getKey(), new int[]{tu, tv, nu, nv});
            float u0 = (b[0] - tu) / nu;
            float v0 = (b[1] - tv) / nv;
            float u1 = (b[2] - tu) / nu;
            float v1 = (b[3] - tv) / nv;
            int x0 = clamp(((int) Math.floor(u0 * src.width())) - 2, 0, src.width() - 1);
            int y0 = clamp(((int) Math.floor(v0 * src.height())) - 2, 0, src.height() - 1);
            int x1 = clamp(((int) Math.ceil(u1 * src.width())) + 2, x0 + 1, src.width());
            int y1 = clamp(((int) Math.ceil(v1 * src.height())) + 2, y0 + 1, src.height());
            crops.put(e.getKey(), crop(src, x0, y0, x1, y1));
            cropUv.put(e.getKey(), new float[]{x0 / src.width(), y0 / src.height(), x1 / src.width(), y1 / src.height()});
        }
        double area = 0.0d;
        for (ImageDecoder.Picture c : crops.values()) {
            area += ((double) (c.width() + 8)) * ((double) (c.height() + 8));
        }
        double dMin = Math.min(1.0d, Math.sqrt(3858759.68d / Math.max(1.0d, area)));
        while (true) {
            double k = dMin;
            TextureAtlas a = tryPack(crops, cropUv, k);
            if (a != null) {
                a.tiles.putAll(tiles);
                return a;
            }
            dMin = k * 0.93d;
        }
    }

    private static ImageDecoder.Picture baseColor(GltfFile file, int material, ImageDecoder decoder) throws IOException {
        byte[] encoded = file.baseColorImage(material);
        ImageDecoder.Picture src = encoded == null ? new ImageDecoder.Picture(4, 4, filled(16, -1)) : decoder.decode(encoded);
        float[] f = file.baseColorFactor(material);
        if (f[0] == 1.0f && f[1] == 1.0f && f[2] == 1.0f && f[3] == 1.0f) {
            return src;
        }
        for (int c = 0; c < 3; c++) {
            f[c] = (float) Math.pow(f[c], 0.45454545454545453d);
        }
        int[] out = new int[src.argb().length];
        for (int i = 0; i < out.length; i++) {
            int p = src.argb()[i];
            int a = Math.round((p >>> 24) * f[3]);
            int r = Math.round(((p >> 16) & 255) * f[0]);
            int g = Math.round(((p >> 8) & 255) * f[1]);
            int b = Math.round((p & 255) * f[2]);
            out[i] = (a << 24) | (r << 16) | (g << 8) | b;
        }
        return new ImageDecoder.Picture(src.width(), src.height(), out);
    }

    private static int[] filled(int n, int argb) {
        int[] out = new int[n];
        Arrays.fill(out, argb);
        return out;
    }

    private static ImageDecoder.Picture tile(ImageDecoder.Picture src, int nu, int nv) {
        if (nu == 1 && nv == 1) {
            return src;
        }
        if (src.width() * nu > 8192 || src.height() * nv > 8192) {
            src = resize(src, Math.max(1, Math.min(src.width(), 8192 / nu)), Math.max(1, Math.min(src.height(), 8192 / nv)));
        }
        int w = src.width() * nu;
        int h = src.height() * nv;
        int[] out = new int[w * h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                out[(y * w) + x] = src.argb()[((y % src.height()) * src.width()) + (x % src.width())];
            }
        }
        return new ImageDecoder.Picture(w, h, out);
    }

    private static ImageDecoder.Picture crop(ImageDecoder.Picture src, int x0, int y0, int x1, int y1) {
        int w = x1 - x0;
        int h = y1 - y0;
        int[] out = new int[w * h];
        for (int y = 0; y < h; y++) {
            System.arraycopy(src.argb(), ((y0 + y) * src.width()) + x0, out, y * w, w);
        }
        return new ImageDecoder.Picture(w, h, out);
    }

    private static TextureAtlas tryPack(Map<Integer, ImageDecoder.Picture> crops, Map<Integer, float[]> cropUv, double k) {
        List<Integer> order = new ArrayList<>(crops.keySet());
        order.sort((a, b) -> {
            return Integer.compare(((ImageDecoder.Picture) crops.get(b)).height(), ((ImageDecoder.Picture) crops.get(a)).height());
        });
        int x = 0;
        int y = 0;
        int shelf = 0;
        int right = 0;
        Map<Integer, int[]> place = new HashMap<>();
        Iterator<Integer> it = order.iterator();
        while (it.hasNext()) {
            int m = it.next().intValue();
            ImageDecoder.Picture c = crops.get(Integer.valueOf(m));
            int w = Math.max(1, (int) Math.round(((double) c.width()) * k));
            int h = Math.max(1, (int) Math.round(((double) c.height()) * k));
            if (x + w + 8 > 2048) {
                x = 0;
                y += shelf;
                shelf = 0;
            }
            if (w + 8 > 2048 || y + h + 8 > 2048) {
                return null;
            }
            place.put(Integer.valueOf(m), new int[]{x + 4, y + 4, w, h});
            x += w + 8;
            shelf = Math.max(shelf, h + 8);
            right = Math.max(right, x);
        }
        TextureAtlas a2 = new TextureAtlas(pow2(right), pow2(y + shelf));
        Iterator<Integer> it2 = order.iterator();
        while (it2.hasNext()) {
            int m2 = it2.next().intValue();
            int[] r = place.get(Integer.valueOf(m2));
            ImageDecoder.Picture scaled = resize(crops.get(Integer.valueOf(m2)), r[2], r[3]);
            for (int py = -4; py < r[3] + 4; py++) {
                for (int px = -4; px < r[2] + 4; px++) {
                    int sx = clamp(px, 0, r[2] - 1);
                    int sy = clamp(py, 0, r[3] - 1);
                    a2.pixels[((r[1] + py) * a2.width) + r[0] + px] = scaled.argb()[(sy * r[2]) + sx];
                }
            }
            float[] c2 = cropUv.get(Integer.valueOf(m2));
            a2.regions.put(Integer.valueOf(m2), new float[]{c2[0], c2[1], c2[2], c2[3], r[0], r[1], r[2], r[3]});
        }
        return a2;
    }

    float[] map(int material, float u, float v) {
        float[] r = this.regions.get(Integer.valueOf(material));
        int[] t = this.tiles.get(Integer.valueOf(material));
        float tu = (u - t[0]) / t[2];
        float tv = (v - t[1]) / t[3];
        float cu = (tu - r[0]) / (r[2] - r[0]);
        float cv = (tv - r[1]) / (r[3] - r[1]);
        return new float[]{(r[4] + (cu * r[6])) / this.width, (r[5] + (cv * r[7])) / this.height};
    }

    byte[] packed() throws IOException {
        byte[] raw = new byte[this.pixels.length * 4];
        for (int i = 0; i < this.pixels.length; i++) {
            int left = i % this.width == 0 ? 0 : this.pixels[i - 1];
            for (int c = 0; c < 4; c++) {
                raw[(i * 4) + c] = (byte) ((this.pixels[i] >>> (24 - (c * 8))) - (left >>> (24 - (c * 8))));
            }
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DeflaterOutputStream z = new DeflaterOutputStream(out, new Deflater(9));
        try {
            z.write(raw);
            z.close();
            return out.toByteArray();
        } catch (Throwable th) {
            try {
                z.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private static ImageDecoder.Picture resize(ImageDecoder.Picture src, int w, int h) {
        ImageDecoder.Picture cur;
        ImageDecoder.Picture pictureHalf = src;
        while (true) {
            cur = pictureHalf;
            if (cur.width() / 2 < w || cur.height() / 2 < h) {
                break;
            }
            pictureHalf = half(cur);
        }
        return (cur.width() == w && cur.height() == h) ? cur : bilinear(cur, w, h);
    }

    private static ImageDecoder.Picture half(ImageDecoder.Picture src) {
        int w = src.width() / 2;
        int h = src.height() / 2;
        int[] out = new int[w * h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int[] s = src.argb();
                int i = (y * 2 * src.width()) + (x * 2);
                out[(y * w) + x] = average(s[i], s[i + 1], s[i + src.width()], s[i + src.width() + 1]);
            }
        }
        return new ImageDecoder.Picture(w, h, out);
    }

    private static int average(int a, int b, int c, int d) {
        int out = 0;
        for (int shift = 0; shift < 32; shift += 8) {
            int sum = ((a >>> shift) & 255) + ((b >>> shift) & 255) + ((c >>> shift) & 255) + ((d >>> shift) & 255);
            out |= ((sum + 2) / 4) << shift;
        }
        return out;
    }

    private static ImageDecoder.Picture bilinear(ImageDecoder.Picture src, int w, int h) {
        int[] out = new int[w * h];
        int sw = src.width();
        int sh = src.height();
        for (int y = 0; y < h; y++) {
            float fy = Math.max(0.0f, (((y + 0.5f) * sh) / h) - 0.5f);
            int y0 = Math.min(sh - 1, (int) fy);
            int y1 = Math.min(sh - 1, y0 + 1);
            float ty = fy - y0;
            for (int x = 0; x < w; x++) {
                float fx = Math.max(0.0f, (((x + 0.5f) * sw) / w) - 0.5f);
                int x0 = Math.min(sw - 1, (int) fx);
                int x1 = Math.min(sw - 1, x0 + 1);
                float tx = fx - x0;
                int[] s = src.argb();
                int p = 0;
                for (int shift = 0; shift < 32; shift += 8) {
                    float top = (((s[(y0 * sw) + x0] >>> shift) & 255) * (1.0f - tx)) + (((s[(y0 * sw) + x1] >>> shift) & 255) * tx);
                    float bottom = (((s[(y1 * sw) + x0] >>> shift) & 255) * (1.0f - tx)) + (((s[(y1 * sw) + x1] >>> shift) & 255) * tx);
                    p |= Math.round((top * (1.0f - ty)) + (bottom * ty)) << shift;
                }
                out[(y * w) + x] = p;
            }
        }
        return new ImageDecoder.Picture(w, h, out);
    }

    private static int clamp(int v, int lo, int hi) {
        return Math.max(lo, Math.min(hi, v));
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
