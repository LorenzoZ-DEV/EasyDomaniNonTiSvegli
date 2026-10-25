package net.anubis.client.util.netherite;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2540;
import net.minecraft.class_6603;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/netherite/HiddenDebrisScanner.class */
@Environment(EnvType.CLIENT)
public final class HiddenDebrisScanner {
    private static final int BLOCK_VALUES = 4096;
    private static final int BIOME_VALUES = 64;
    private static final int BLOCK_PALETTE_BITS = 8;
    private static final int BIOME_PALETTE_BITS = 3;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/netherite/HiddenDebrisScanner$PaletteRead.class */
    @Environment(EnvType.CLIENT)
    private static final class PaletteRead extends Record {
        private final boolean paletteHasTarget;
        private final boolean targetUnused;

        private PaletteRead(boolean paletteHasTarget, boolean targetUnused) {
            this.paletteHasTarget = paletteHasTarget;
            this.targetUnused = targetUnused;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, PaletteRead.class), PaletteRead.class, "paletteHasTarget;targetUnused", "FIELD:Lnet/anubis/client/util/netherite/HiddenDebrisScanner$PaletteRead;->paletteHasTarget:Z", "FIELD:Lnet/anubis/client/util/netherite/HiddenDebrisScanner$PaletteRead;->targetUnused:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, PaletteRead.class), PaletteRead.class, "paletteHasTarget;targetUnused", "FIELD:Lnet/anubis/client/util/netherite/HiddenDebrisScanner$PaletteRead;->paletteHasTarget:Z", "FIELD:Lnet/anubis/client/util/netherite/HiddenDebrisScanner$PaletteRead;->targetUnused:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, PaletteRead.class, Object.class), PaletteRead.class, "paletteHasTarget;targetUnused", "FIELD:Lnet/anubis/client/util/netherite/HiddenDebrisScanner$PaletteRead;->paletteHasTarget:Z", "FIELD:Lnet/anubis/client/util/netherite/HiddenDebrisScanner$PaletteRead;->targetUnused:Z").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public boolean paletteHasTarget() {
            return this.paletteHasTarget;
        }

        public boolean targetUnused() {
            return this.targetUnused;
        }

        boolean hiddenTarget() {
            return this.paletteHasTarget && this.targetUnused;
        }
    }

    private HiddenDebrisScanner() {
    }

    public static List<Integer> findHiddenSections(class_6603 data, int sectionCount, int minSectionY, int targetStateId) {
        Objects.requireNonNull(data, "data");
        if (targetStateId < 0) {
            throw new IllegalArgumentException("targetStateId must be non-negative");
        }
        class_2540 buf = data.method_38586();
        try {
            try {
                List<Integer> sections = scanSections(buf, sectionCount, minSectionY, targetStateId);
                return buf.readableBytes() == 0 ? sections : List.of();
            } catch (RuntimeException e) {
                return List.of();
            }
        } finally {
            buf.release();
        }
    }

    static List<Integer> scanSections(class_2540 buf, int sectionCount, int minSectionY, int targetStateId) {
        Objects.requireNonNull(buf, "buf");
        if (sectionCount < 0 || targetStateId < 0) {
            throw new IllegalArgumentException("invalid section scan arguments");
        }
        List<Integer> hidden = new ArrayList<>();
        for (int i = 0; i < sectionCount; i++) {
            int nonEmptyBlocks = buf.readUnsignedShort();
            PaletteRead blocks = readPalette(buf, 4096, 8, 4, targetStateId);
            readPalette(buf, 64, 3, 1, -1);
            if (nonEmptyBlocks > 0 && blocks.hiddenTarget()) {
                hidden.add(Integer.valueOf(minSectionY + i));
            }
        }
        return List.copyOf(hidden);
    }

    private static PaletteRead readPalette(class_2540 buf, int valueCount, int maxPaletteBits, int minBits, int target) {
        int storageBits;
        int[] palette;
        int bits = buf.readUnsignedByte();
        if (bits == 0) {
            storageBits = 0;
            palette = new int[]{buf.method_10816()};
        } else if (bits <= maxPaletteBits) {
            storageBits = Math.max(minBits, bits);
            int size = buf.method_10816();
            if (size <= 0 || size > (1 << storageBits)) {
                throw new IllegalArgumentException("invalid local palette size");
            }
            palette = new int[size];
            for (int i = 0; i < size; i++) {
                palette[i] = buf.method_10816();
            }
        } else {
            storageBits = bits;
            if (storageBits > 30) {
                throw new IllegalArgumentException("direct palette is too wide");
            }
            palette = null;
        }
        int valuesPerLong = storageBits == 0 ? 0 : 64 / storageBits;
        if (storageBits > 0 && valuesPerLong == 0) {
            throw new IllegalArgumentException("invalid palette storage width");
        }
        int longs = storageBits == 0 ? 0 : ((valueCount + valuesPerLong) - 1) / valuesPerLong;
        long bytes = ((long) longs) * 8;
        if (bytes > buf.readableBytes()) {
            throw new IndexOutOfBoundsException("truncated palette data");
        }
        boolean paletteHasTarget = palette != null && contains(palette, target);
        if (!paletteHasTarget) {
            buf.method_52994((int) bytes);
            return new PaletteRead(false, true);
        }
        int used = 0;
        if (storageBits == 0) {
            if (palette[0] == target) {
                used = valueCount;
            }
        } else {
            int start = buf.readerIndex();
            long mask = (1 << storageBits) - 1;
            for (int i2 = 0; i2 < valueCount; i2++) {
                int longIndex = i2 / valuesPerLong;
                int bitIndex = i2 - (longIndex * valuesPerLong);
                int raw = (int) ((buf.getLong(start + (longIndex * 8)) >>> (bitIndex * storageBits)) & mask);
                int value = palette == null ? raw : paletteValue(palette, raw);
                if (value == target) {
                    used++;
                }
            }
        }
        buf.method_52994((int) bytes);
        return new PaletteRead(paletteHasTarget, used == 0);
    }

    private static int paletteValue(int[] palette, int index) {
        if (index < 0 || index >= palette.length) {
            throw new IllegalArgumentException("packed palette index is invalid");
        }
        return palette[index];
    }

    private static boolean contains(int[] values, int target) {
        for (int value : values) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }
}
