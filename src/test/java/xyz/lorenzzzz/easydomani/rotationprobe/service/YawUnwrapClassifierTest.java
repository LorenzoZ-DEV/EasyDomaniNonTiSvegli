package xyz.lorenzzzz.easydomani.rotationprobe.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class YawUnwrapClassifierTest {

    @Test
    void simulationValues() {
        assertEquals(true, YawUnwrapClassifier.unwrapped(3600f, 3600f));
        assertEquals(false, YawUnwrapClassifier.unwrapped(3600f, 0f));
        assertEquals(true, YawUnwrapClassifier.unwrapped(3637.5f, 3637.5f));
        assertEquals(false, YawUnwrapClassifier.unwrapped(3637.5f, 37.5f));
        assertEquals(true, YawUnwrapClassifier.unwrapped(-3770f, -3770f));
        assertEquals(false, YawUnwrapClassifier.unwrapped(-3770f, -170f));
        assertEquals(true, YawUnwrapClassifier.unwrapped(3779.9f, 3779.9f));
        assertEquals(false, YawUnwrapClassifier.unwrapped(3779.9f, 179.9f));
        assertEquals(true, YawUnwrapClassifier.unwrapped(-4325.3f, -4325.3f));
        assertEquals(false, YawUnwrapClassifier.unwrapped(-4325.3f, -5.3f));
        assertEquals(true, YawUnwrapClassifier.unwrapped(15945.6f, 15945.6f));
        assertEquals(false, YawUnwrapClassifier.unwrapped(15945.6f, 105.6f));
    }

    @Test
    void mouseMovementBetweenTicks() {

        assertEquals(true, YawUnwrapClassifier.unwrapped(3637.5f, 3637.5f + 25f));
        assertEquals(false, YawUnwrapClassifier.unwrapped(3637.5f, 37.5f - 25f));
        assertEquals(false, YawUnwrapClassifier.unwrapped(3637.5f, 179.9f + 300f));
    }

    @Test
    void uncertain() {
        assertNull(YawUnwrapClassifier.unwrapped(3600f, 1800f));
        assertNull(YawUnwrapClassifier.unwrapped(3600f, -3600f));
        assertNull(YawUnwrapClassifier.unwrapped(3600f, Float.NaN));
    }

    @Test
    void verdictOnAllSamples() {
        float big = YawUnwrapClassifier.wrap(-89.133f) + YawUnwrapClassifier.PUSH_DEGREES;
        assertEquals(true, YawUnwrapClassifier.verdict(big, List.of(3510.867f, 3512f)));
        assertEquals(false, YawUnwrapClassifier.verdict(big, List.of(-89.133f, -80f)));

        assertNull(YawUnwrapClassifier.verdict(3472.168f, List.of(7072.242f)));
        assertNull(YawUnwrapClassifier.verdict(big, List.of(3510.867f, -89.133f)));
        assertNull(YawUnwrapClassifier.verdict(big, List.of()));
    }

    @Test
    void wrapLikeVanilla() {
        assertEquals(-89.133f, YawUnwrapClassifier.wrap(3510.867f), 1e-3f);
        assertEquals(-5.3f, YawUnwrapClassifier.wrap(-725.3f), 1e-3f);
        assertEquals(-180f, YawUnwrapClassifier.wrap(180f), 0f);
        assertEquals(179.9f, YawUnwrapClassifier.wrap(179.9f), 1e-4f);

        assertTrue(Math.abs(YawUnwrapClassifier.wrap(7072.242f) + YawUnwrapClassifier.PUSH_DEGREES) < 3780f);
    }
}
