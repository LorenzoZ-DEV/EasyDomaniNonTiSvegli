package xyz.lorenzzzz.easydomani.rotationprobe.service;

import java.util.List;

final class YawUnwrapClassifier {
    static final float PUSH_DEGREES = 3600f;

    private YawUnwrapClassifier() {
    }

    static Boolean unwrapped(float big, float yaw) {
        if (Math.abs(yaw - big) < 180f) return true;
        if (Math.abs(yaw) < 540f) return false;
        return null;
    }

    static Boolean verdict(float big, List<Float> samples) {
        Boolean result = null;
        for (float yaw : samples) {
            Boolean u = unwrapped(big, yaw);
            if (u == null || (result != null && !result.equals(u))) return null;
            result = u;
        }
        return result;
    }

    static float wrap(float degrees) {
        float f = degrees % 360f;
        if (f >= 180f) f -= 360f;
        if (f < -180f) f += 360f;
        return f;
    }
}
