package net.anubis.client.util.seedscan;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/seedscan/WgRandom.class */
@Environment(EnvType.CLIENT)
final class WgRandom {
    private long lo;
    private long hi;

    WgRandom() {
    }

    private WgRandom(long lo, long hi) {
        this.lo = lo;
        this.hi = hi;
    }

    WgRandom copy() {
        return new WgRandom(this.lo, this.hi);
    }

    boolean sameState(WgRandom other) {
        return this.lo == other.lo && this.hi == other.hi;
    }

    long stateHash() {
        return (this.lo * 31) + this.hi;
    }

    private static long mix(long l) {
        long l2 = (l ^ (l >>> 30)) * (-4658895280553007687L);
        long l3 = (l2 ^ (l2 >>> 27)) * (-7723592293110705685L);
        return l3 ^ (l3 >>> 31);
    }

    void setSeed(long seed) {
        long m = seed ^ 7640891576956012809L;
        long n = m - 7046029254386353131L;
        this.lo = mix(m);
        this.hi = mix(n);
        if ((this.lo | this.hi) == 0) {
            this.lo = -7046029254386353131L;
            this.hi = 7640891576956012809L;
        }
    }

    private long step() {
        long l = this.lo;
        long m = this.hi;
        long n = Long.rotateLeft(l + m, 17) + l;
        long m2 = m ^ l;
        this.lo = (Long.rotateLeft(l, 49) ^ m2) ^ (m2 << 21);
        this.hi = Long.rotateLeft(m2, 28);
        return n;
    }

    void skip(int steps) {
        for (int i = 0; i < steps; i++) {
            step();
        }
    }

    int next(int bits) {
        return (int) (step() >>> (64 - bits));
    }

    int nextInt(int bound) {
        int j;
        int k;
        if ((bound & (bound - 1)) == 0) {
            return (int) ((((long) bound) * ((long) next(31))) >> 31);
        }
        do {
            j = next(31);
            k = j % bound;
        } while ((j - k) + (bound - 1) < 0);
        return k;
    }

    int between(int min, int max) {
        return nextInt((max - min) + 1) + min;
    }

    long nextLong() {
        int i = next(32);
        int j = next(32);
        return (((long) i) << 32) + ((long) j);
    }

    float nextFloat() {
        return next(24) * 5.9604645E-8f;
    }

    double nextDouble() {
        int i = next(26);
        int j = next(27);
        return ((((long) i) << 27) + ((long) j)) * 1.1102230246251565E-16d;
    }

    long setDecorationSeed(long worldSeed, int blockX, int blockZ) {
        setSeed(worldSeed);
        long m = nextLong() | 1;
        long n = nextLong() | 1;
        long seed = ((((long) blockX) * m) + (((long) blockZ) * n)) ^ worldSeed;
        setSeed(seed);
        return seed;
    }

    void setFeatureSeed(long decorationSeed, int index, int step) {
        setSeed(decorationSeed + ((long) index) + (10000 * ((long) step)));
    }
}
