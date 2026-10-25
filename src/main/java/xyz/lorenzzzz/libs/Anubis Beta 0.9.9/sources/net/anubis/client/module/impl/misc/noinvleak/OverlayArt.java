package net.anubis.client.module.impl.misc.noinvleak;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1011;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3298;
import net.minecraft.class_5348;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/noinvleak/OverlayArt.class */
@Environment(EnvType.CLIENT)
final class OverlayArt {
    private static final int GLYPH = 8;
    private static final int SHADOW_MASK = 16579836;
    private static Image selection;
    private static Slice tooltipBackground;
    private static Slice tooltipFrame;
    private static boolean[] glyphs;
    private static boolean loaded;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/noinvleak/OverlayArt$Image.class */
    @Environment(EnvType.CLIENT)
    static final class Image extends Record {
        private final int[] pixels;
        private final int width;
        private final int height;

        Image(int[] pixels, int width, int height) {
            this.pixels = pixels;
            this.width = width;
            this.height = height;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Image.class), Image.class, "pixels;width;height", "FIELD:Lnet/anubis/client/module/impl/misc/noinvleak/OverlayArt$Image;->pixels:[I", "FIELD:Lnet/anubis/client/module/impl/misc/noinvleak/OverlayArt$Image;->width:I", "FIELD:Lnet/anubis/client/module/impl/misc/noinvleak/OverlayArt$Image;->height:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Image.class), Image.class, "pixels;width;height", "FIELD:Lnet/anubis/client/module/impl/misc/noinvleak/OverlayArt$Image;->pixels:[I", "FIELD:Lnet/anubis/client/module/impl/misc/noinvleak/OverlayArt$Image;->width:I", "FIELD:Lnet/anubis/client/module/impl/misc/noinvleak/OverlayArt$Image;->height:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Image.class, Object.class), Image.class, "pixels;width;height", "FIELD:Lnet/anubis/client/module/impl/misc/noinvleak/OverlayArt$Image;->pixels:[I", "FIELD:Lnet/anubis/client/module/impl/misc/noinvleak/OverlayArt$Image;->width:I", "FIELD:Lnet/anubis/client/module/impl/misc/noinvleak/OverlayArt$Image;->height:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int[] pixels() {
            return this.pixels;
        }

        public int width() {
            return this.width;
        }

        public int height() {
            return this.height;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/noinvleak/OverlayArt$Slice.class */
    @Environment(EnvType.CLIENT)
    static final class Slice extends Record {
        private final Image image;
        private final int border;
        private final boolean stretch;

        Slice(Image image, int border, boolean stretch) {
            this.image = image;
            this.border = border;
            this.stretch = stretch;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Slice.class), Slice.class, "image;border;stretch", "FIELD:Lnet/anubis/client/module/impl/misc/noinvleak/OverlayArt$Slice;->image:Lnet/anubis/client/module/impl/misc/noinvleak/OverlayArt$Image;", "FIELD:Lnet/anubis/client/module/impl/misc/noinvleak/OverlayArt$Slice;->border:I", "FIELD:Lnet/anubis/client/module/impl/misc/noinvleak/OverlayArt$Slice;->stretch:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Slice.class), Slice.class, "image;border;stretch", "FIELD:Lnet/anubis/client/module/impl/misc/noinvleak/OverlayArt$Slice;->image:Lnet/anubis/client/module/impl/misc/noinvleak/OverlayArt$Image;", "FIELD:Lnet/anubis/client/module/impl/misc/noinvleak/OverlayArt$Slice;->border:I", "FIELD:Lnet/anubis/client/module/impl/misc/noinvleak/OverlayArt$Slice;->stretch:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Slice.class, Object.class), Slice.class, "image;border;stretch", "FIELD:Lnet/anubis/client/module/impl/misc/noinvleak/OverlayArt$Slice;->image:Lnet/anubis/client/module/impl/misc/noinvleak/OverlayArt$Image;", "FIELD:Lnet/anubis/client/module/impl/misc/noinvleak/OverlayArt$Slice;->border:I", "FIELD:Lnet/anubis/client/module/impl/misc/noinvleak/OverlayArt$Slice;->stretch:Z").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public Image image() {
            return this.image;
        }

        public int border() {
            return this.border;
        }

        public boolean stretch() {
            return this.stretch;
        }
    }

    private OverlayArt() {
    }

    static void reset() {
        selection = null;
        tooltipBackground = null;
        tooltipFrame = null;
        glyphs = null;
        loaded = false;
    }

    static Image selection() {
        load();
        return selection;
    }

    static Slice tooltipBackground() {
        load();
        return tooltipBackground;
    }

    static Slice tooltipFrame() {
        load();
        return tooltipFrame;
    }

    private static void load() {
        if (loaded) {
            return;
        }
        loaded = true;
        selection = image("textures/gui/sprites/hud/hotbar_selection.png");
        tooltipBackground = slice("textures/gui/sprites/tooltip/background.png", 9, false);
        tooltipFrame = slice("textures/gui/sprites/tooltip/frame.png", 10, true);
        Image font = image("textures/font/ascii.png");
        if (font != null) {
            glyphs = new boolean[font.width() * font.height()];
            for (int i = 0; i < glyphs.length; i++) {
                glyphs[i] = (font.pixels()[i] >>> 24) != 0;
            }
        }
    }

    private static Image image(String path) {
        class_3298 resource = (class_3298) class_310.method_1551().method_1478().method_14486(class_2960.method_60656(path)).orElse(null);
        if (resource == null) {
            return null;
        }
        try {
            InputStream in = resource.method_14482();
            try {
                class_1011 image = class_1011.method_4309(in);
                try {
                    int[] abgr = image.method_48463();
                    int[] out = new int[abgr.length];
                    for (int i = 0; i < abgr.length; i++) {
                        int a = abgr[i] >>> 24;
                        out[i] = Canvas.premultiply((a << 24) | ((abgr[i] & 255) << 16) | (((abgr[i] >>> 8) & 255) << 8) | ((abgr[i] >>> 16) & 255), a);
                    }
                    Image image2 = new Image(out, image.method_4307(), image.method_4323());
                    if (image != null) {
                        image.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                    return image2;
                } catch (Throwable th) {
                    if (image != null) {
                        try {
                            image.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Throwable th4) {
                        th3.addSuppressed(th4);
                    }
                }
                throw th3;
            }
        } catch (IOException | RuntimeException e) {
            return null;
        }
    }

    private static Slice slice(String path, int border, boolean stretch) {
        Image image = image(path);
        if (image == null) {
            return null;
        }
        class_3298 meta = (class_3298) class_310.method_1551().method_1478().method_14486(class_2960.method_60656(path + ".mcmeta")).orElse(null);
        if (meta != null) {
            try {
                Reader reader = meta.method_43039();
                try {
                    JsonObject scaling = JsonParser.parseReader(reader).getAsJsonObject().getAsJsonObject("gui").getAsJsonObject("scaling");
                    if (scaling.has("border")) {
                        border = scaling.get("border").getAsInt();
                    }
                    stretch = scaling.has("stretch_inner") && scaling.get("stretch_inner").getAsBoolean();
                    if (reader != null) {
                        reader.close();
                    }
                } catch (Throwable th) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (IOException | RuntimeException e) {
            }
        }
        return new Slice(image, border, stretch);
    }

    static void slice(Canvas canvas, Slice slice, int x, int y, int width, int height, int scale) {
        if (slice == null) {
            return;
        }
        Image img = slice.image();
        int b = slice.border();
        int innerW = img.width() - (2 * b);
        int innerH = img.height() - (2 * b);
        if (innerW <= 0 || innerH <= 0) {
            return;
        }
        int[] column = new int[width];
        int[] row = new int[height];
        for (int dx = 0; dx < width; dx++) {
            column[dx] = sourceAt(dx, width, b, innerW, slice.stretch());
        }
        for (int dy = 0; dy < height; dy++) {
            row[dy] = sourceAt(dy, height, b, innerH, slice.stretch());
        }
        for (int dy2 = 0; dy2 < height; dy2++) {
            for (int dx2 = 0; dx2 < width; dx2++) {
                int src = img.pixels()[(row[dy2] * img.width()) + column[dx2]];
                if ((src >>> 24) != 0) {
                    canvas.blendPixel(src, (x + dx2) * scale, (y + dy2) * scale, scale);
                }
            }
        }
    }

    private static int sourceAt(int d, int size, int b, int inner, boolean stretch) {
        if (d < b) {
            return d;
        }
        if (d >= size - b) {
            return b + inner + (d - (size - b));
        }
        int into = d - b;
        int middle = size - (2 * b);
        return b + (stretch ? (into * inner) / Math.max(middle, 1) : into % inner);
    }

    static void text(Canvas canvas, class_2561 text, int x, int y, int scale, int argb, boolean shadow) {
        load();
        if (glyphs == null) {
            return;
        }
        int[] cursor = {x};
        text.method_27658((style, piece) -> {
            int color = style.method_10973() != null ? (argb & (-16777216)) | style.method_10973().method_27716() : argb;
            int i = 0;
            while (i < piece.length()) {
                int cp = piece.codePointAt(i);
                i += Character.charCount(cp);
                int advance = advance(cp, style);
                if (cp < 128 && cp > 32 && !style.method_10987()) {
                    if (shadow) {
                        glyph(canvas, cp, cursor[0] + 1, y + 1, scale, (color & (-16777216)) | ((color & SHADOW_MASK) >> 2), style);
                    }
                    glyph(canvas, cp, cursor[0], y, scale, color, style);
                }
                if (style.method_10986()) {
                    canvas.rect(cursor[0] * scale, (y + 4) * scale, advance * scale, scale, color);
                }
                if (style.method_10965()) {
                    canvas.rect(cursor[0] * scale, (y + 8) * scale, advance * scale, scale, color);
                }
                cursor[0] = cursor[0] + advance;
            }
            return Optional.empty();
        }, class_2583.field_24360);
    }

    private static int advance(int cp, class_2583 style) {
        return class_310.method_1551().field_1772.method_27525(class_5348.method_29431(new String(Character.toChars(cp)), style));
    }

    private static void glyph(Canvas canvas, int cp, int x, int y, int scale, int argb, class_2583 style) {
        int cellX = (cp % 16) * 8;
        int cellY = (cp / 16) * 8;
        int row = 0;
        while (row < 8) {
            int shear = (!style.method_10966() || row >= 4) ? 0 : 1;
            int col = 0;
            while (col < 8) {
                if (!glyphs[((cellY + row) * 128) + cellX + col]) {
                    col++;
                } else {
                    int start = col;
                    while (col < 8 && glyphs[((cellY + row) * 128) + cellX + col]) {
                        col++;
                    }
                    canvas.rect((x + start + shear) * scale, (y + row) * scale, (col - start) * scale, scale, argb);
                    if (style.method_10984()) {
                        canvas.rect((x + start + shear + 1) * scale, (y + row) * scale, (col - start) * scale, scale, argb);
                    }
                }
            }
            row++;
        }
    }
}
