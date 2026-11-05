package xyz.lorenzzzz.easydomani.honeypot.model;

public final class BlockPositionKey {

    private BlockPositionKey() {
    }

    public static long pack(int x, int y, int z) {
        return ((long) (x & 0x3FFFFFF) << 38) | ((long) (z & 0x3FFFFFF) << 12) | (y & 0xFFFL);
    }
}
