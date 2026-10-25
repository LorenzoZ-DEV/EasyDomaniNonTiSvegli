package net.anubis.client.module.impl.render.character.kit;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Paint2D.class */
@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface Paint2D {
    int argb(float f, float f2);

    static Paint2D solid(int argb) {
        return (x, y) -> {
            return argb;
        };
    }

    static Paint2D linear(float x0, float y0, float x1, float y1, int... stops) {
        if (stops.length == 0) {
            throw new IllegalArgumentException("gradient needs at least one colour");
        }
        float dx = x1 - x0;
        float dy = y1 - y0;
        float l2 = (dx * dx) + (dy * dy);
        float inv = l2 < 1.0E-8f ? 0.0f : 1.0f / l2;
        return (x, y) -> {
            return stop(stops, (((x - x0) * dx) + ((y - y0) * dy)) * inv);
        };
    }

    static Paint2D radial(float cx, float cy, float radius, int... stops) {
        if (stops.length == 0) {
            throw new IllegalArgumentException("gradient needs at least one colour");
        }
        float inv = 1.0f / Math.max(1.0E-4f, radius);
        return (x, y) -> {
            float dx = x - cx;
            float dy = y - cy;
            return stop(stops, ((float) Math.sqrt((dx * dx) + (dy * dy))) * inv);
        };
    }

    static Paint2D elliptic(float cx, float cy, float rx, float ry, int... stops) {
        if (stops.length == 0) {
            throw new IllegalArgumentException("gradient needs at least one colour");
        }
        float ix = 1.0f / Math.max(1.0E-4f, rx);
        float iy = 1.0f / Math.max(1.0E-4f, ry);
        return (x, y) -> {
            float dx = (x - cx) * ix;
            float dy = (y - cy) * iy;
            return stop(stops, (float) Math.sqrt((dx * dx) + (dy * dy)));
        };
    }

    static Paint2D noise(int a, int b, float size, float contrast) {
        float f = 1.0f / Math.max(0.001f, size);
        return (x, y) -> {
            float n = (Noise.fbm(x * f, y * f, 4) * contrast) + 0.5f;
            return Colors.mix(a, b, n < 0.0f ? 0.0f : Math.min(n, 1.0f));
        };
    }

    static Paint2D grain(int light, int dark, float angleDeg, float spacing, float wobble) {
        double a = Math.toRadians(angleDeg);
        float ux = (float) Math.cos(a);
        float uy = (float) Math.sin(a);
        float inv = 1.0f / Math.max(0.1f, spacing);
        return (x, y) -> {
            float along = (x * ux) + (y * uy);
            float across = ((-x) * uy) + (y * ux);
            float w = Noise.fbm(along * 0.02f, across * 0.05f, 3) * wobble;
            float v = (across + w) * inv;
            float f = v - ((float) Math.floor(v));
            float line = Paint.smooth(0.6f, 0.8f, f) * (1.0f - Paint.smooth(0.85f, 1.0f, f));
            float streak = (Noise.noise(along * 0.015f, across * inv * 1.7f) * 0.5f) + 0.5f;
            float k = (line * 0.8f) + (streak * 0.3f);
            return Colors.mix(light, dark, k < 0.0f ? 0.0f : Math.min(k, 1.0f));
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    static int stop(int[] stops, float t) {
        if (stops.length == 1) {
            return stops[0];
        }
        float f = (t < 0.0f ? 0.0f : Math.min(t, 1.0f)) * (stops.length - 1);
        int i = Math.min((int) f, stops.length - 2);
        return Colors.mix(stops[i], stops[i + 1], f - i);
    }
}
