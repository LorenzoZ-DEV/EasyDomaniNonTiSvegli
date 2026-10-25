package net.anubis.client.module.impl.misc.noinvleak;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuFence;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import com.sun.jna.Platform;
import imgui.ImDrawList;
import imgui.ImGui;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import net.anubis.client.gui.ItemIcons;
import net.anubis.client.gui.ItemLook;
import net.anubis.client.module.impl.misc.NoInvLeakModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1041;
import net.minecraft.class_10725;
import net.minecraft.class_1799;
import net.minecraft.class_310;
import net.minecraft.class_408;
import net.minecraft.class_437;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/noinvleak/StreamOverlay.class */
@Environment(EnvType.CLIENT)
public final class StreamOverlay {
    private static final int SLOTS = 41;
    private static final int CURSOR = 41;
    private static final int ENTRIES = 42;
    private static final long SLOT_BITS = 2199023255551L;
    private static final int CELLS_PER_SIDE = 7;
    private static final int CACHE_LIMIT = 4194304;
    private static final long READBACK_TIMEOUT = 1000000000;
    private static final long BACKGROUND_INTERVAL = 33000000;
    private static final int FALLBACK_BACKGROUND = -7631989;
    private static long mask;
    private static boolean selectionSeen;
    private static int selectionX;
    private static int selectionY;
    private static long fakeMask;
    private static int fakeScale;
    private static ImDrawList fakeList;
    private static long shownMask;
    private static int shownScale;
    private static boolean shownOpaque;
    private static boolean shownSelection;
    private static int shownSelectionX;
    private static int shownSelectionY;
    private static int shownWindowX;
    private static int shownWindowY;
    private static GpuTexture shownAtlas;
    private static int boxX;
    private static int boxY;
    private static boolean backgroundValid;
    private static class_1799 cursorStack;
    private static int cursorCount;
    private static int cursorBar;
    private static int cursorScale;
    private static int cursorX;
    private static int cursorY;
    private static int cachedBytes;
    private static long backgroundAt;
    private static GpuFence asyncFence;
    private static GpuBuffer asyncBuffer;
    private static long asyncMask;
    private static int asyncScale;
    private static OverlayWindow slotWindow;
    private static OverlayWindow cursorWindow;
    private static GpuTexture scratch;
    private static GpuTextureView scratchView;
    private static GpuTexture scratchDepth;
    private static GpuTextureView scratchDepthView;
    private static GpuBuffer readback;
    private static int iconPx;
    private static boolean failed;
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private static final int[] BLANK = new int[0];
    private static final Runnable NOTHING = () -> {
    };
    private static final class_1799[] stack = new class_1799[42];
    private static final int[] guiX = new int[42];
    private static final int[] guiY = new int[42];
    private static int hovered = -1;
    private static final DigitFont.Sink FAKE_SINK = (x, y, w, h, argb) -> {
        fakeList.addRectFilled(x, y, x + w, y + h, (argb & (-16711936)) | ((argb << 16) & 16711680) | ((argb >>> 16) & 255));
    };
    private static final class_1799[] shownStack = new class_1799[42];
    private static final int[] shownX = new int[42];
    private static final int[] shownY = new int[42];
    private static final int[] shownCount = new int[42];
    private static final int[] shownBar = new int[42];
    private static int shownHover = -1;
    private static final ItemLook look = new ItemLook();
    private static final Object2ObjectOpenHashMap<List<Object>, int[]> icons = new Object2ObjectOpenHashMap<>();
    private static final int[][] slotIcon = new int[42][];
    private static final int[] pendingSlot = new int[49];
    private static final List<Object>[] pendingKey = newKeys(49);
    private static final int[][] background = new int[41][];
    private static final int[][] backgroundNext = new int[41][];
    private static final int[] syncRegion = new int[4];
    private static final int[] asyncX = new int[41];
    private static final int[] asyncY = new int[41];
    private static final int[] asyncRegion = new int[4];
    private static final Canvas canvas = new Canvas();

    private StreamOverlay() {
    }

    public static boolean usable() {
        return Platform.isWindows() && !failed;
    }

    public static void retry() {
        failed = false;
    }

    public static void hotbar(int slot, int x, int y, class_1799 item) {
        if (hotbarVisible()) {
            record(slot, x, y, item);
        }
    }

    public static void container(int slot, int x, int y, class_1799 item, boolean isHovered) {
        record(slot, x, y, item);
        if (isHovered) {
            hovered = slot;
        }
    }

    public static void carried(class_1799 item, int x, int y) {
        record(41, x, y, item);
    }

    public static void selection(int x, int y) {
        if (hotbarVisible()) {
            selectionSeen = true;
            selectionX = x;
            selectionY = y;
        }
    }

    public static void tooltip(class_1799 item, int mouseX, int mouseY) {
        TextPanels.tooltip(item, mouseX, mouseY);
    }

    public static void itemName(class_1799 item, int alpha, int y) {
        TextPanels.itemName(item, alpha, y);
    }

    private static boolean hotbarVisible() {
        class_437 screen = class_310.method_1551().field_1755;
        return screen == null || (screen instanceof class_408);
    }

    private static void record(int entry, int x, int y, class_1799 item) {
        if (entry < 0 || entry >= 42) {
            return;
        }
        if (entry == 41 && item.method_7960()) {
            return;
        }
        stack[entry] = item;
        guiX[entry] = x;
        guiY[entry] = y;
        mask |= 1 << entry;
    }

    public static boolean hasFakes() {
        return fakeMask != 0;
    }

    public static void drawFakes() {
        long bits = fakeMask;
        int scale = fakeScale;
        if (bits == 0 || scale <= 0) {
            return;
        }
        fakeList = ImGui.getBackgroundDrawList();
        for (int slot = 0; slot < 41; slot++) {
            if (((bits >>> slot) & 1) != 0) {
                class_1799 fake = NoInvLeakModule.fakeFor(slot);
                if (!fake.method_7960()) {
                    int x = guiX[slot] * scale;
                    int y = guiY[slot] * scale;
                    ItemIcons.draw(fakeList, fake, x, y, 16.0f * scale, 1.0f);
                    DigitFont.decorations(FAKE_SINK, fake, x, y, scale);
                }
            }
        }
    }

    public static void endFrame() {
        long frame = mask;
        mask = 0L;
        int hover = hovered;
        hovered = -1;
        boolean selection = selectionSeen && NoInvLeakModule.hidesSelectedSlot();
        selectionSeen = false;
        if (!NoInvLeakModule.overlayWanted() || !usable()) {
            fakeMask = 0L;
            if (started()) {
                release();
            }
            TextPanels.frameDone();
            return;
        }
        try {
            class_310 mc = class_310.method_1551();
            class_1041 win = mc.method_22683();
            if (win.method_65966()) {
                fakeMask = 0L;
                hideAll();
            } else {
                int scale = win.method_4495();
                int windowX = win.method_4499();
                int windowY = win.method_4477();
                boolean opaque = NoInvLeakModule.opaqueCells();
                long frame2 = withoutEmpty(frame, opaque && NoInvLeakModule.fillsEmpty());
                GpuTexture atlas = itemAtlas(mc);
                if (atlas != shownAtlas) {
                    OverlayArt.reset();
                    resetIcons(16 * scale);
                    shownAtlas = atlas;
                    shownMask = 0L;
                }
                fakeMask = frame2 & SLOT_BITS;
                fakeScale = scale;
                layers(mc, win, frame2, scale, windowX, windowY, opaque, selection, hover);
                TextPanels.update(win, scale, windowX, windowY);
            }
        } catch (LinkageError | RuntimeException e) {
            failed = true;
            fakeMask = 0L;
            LOGGER.warn("[Anubis] No Inv Leak: stream-only overlay unavailable ({}); items stay hidden", e.toString());
            release();
        }
        TextPanels.frameDone();
    }

    public static void stop() {
        if (!RenderSystem.isOnRenderThread()) {
            class_310.method_1551().execute(StreamOverlay::release);
        } else {
            release();
        }
    }

    private static boolean started() {
        return (slotWindow == null && cursorWindow == null && scratch == null && !TextPanels.started()) ? false : true;
    }

    private static void layers(class_310 mc, class_1041 win, long frame, int scale, int windowX, int windowY, boolean opaque, boolean selection, int hover) {
        long slotBits = frame & SLOT_BITS;
        boolean slotsShown = slotBits != 0 || selection;
        if (!slotsShown && ((frame >>> 41) & 1) == 0) {
            hideSlotsAndCursor();
            return;
        }
        int px = 16 * scale;
        if (px != iconPx || cachedBytes > 4194304) {
            resetIcons(px);
        }
        if (scratch == null) {
            allocate(px);
        }
        if (slotWindow == null && slotsShown) {
            slotWindow = new OverlayWindow(win.method_4490());
        }
        if (cursorWindow == null && ((frame >>> 41) & 1) != 0) {
            cursorWindow = new OverlayWindow(win.method_4490());
        }
        boolean layout = (slotBits == shownMask && scale == shownScale && opaque == shownOpaque && selection == shownSelection && (!selection || (selectionX == shownSelectionX && selectionY == shownSelectionY))) ? false : true;
        boolean content = false;
        for (int i = 0; i < 41; i++) {
            if (((slotBits >>> i) & 1) != 0) {
                if (guiX[i] != shownX[i] || guiY[i] != shownY[i]) {
                    layout = true;
                } else if (stack[i] != shownStack[i] || stack[i].method_7947() != shownCount[i] || bar(stack[i]) != shownBar[i]) {
                    content = true;
                }
            }
        }
        if (layout) {
            backgroundValid = false;
            dropAsync();
        }
        int pending = resolveIcons(frame, px);
        boolean wantBackground = (!opaque || slotBits == 0 || backgroundValid) ? false : true;
        boolean backgroundChanged = false;
        if (pending > 0 || wantBackground) {
            synchronousRead(slotBits, scale, wantBackground, pending);
            if (wantBackground) {
                backgroundChanged = true;
                shownHover = hover;
            }
        } else if (opaque && slotBits != 0) {
            backgroundChanged = takeAsync(slotBits, scale);
        }
        if (!slotsShown) {
            if (slotWindow != null) {
                slotWindow.hide();
            }
            shownMask = 0L;
        } else if (layout || content || backgroundChanged || pending > 0) {
            composeSlots(slotBits, scale, opaque, selection, windowX, windowY);
        } else if (windowX != shownWindowX || windowY != shownWindowY) {
            slotWindow.move(windowX + boxX, windowY + boxY);
            shownWindowX = windowX;
            shownWindowY = windowY;
        }
        if (opaque && slotBits != 0 && asyncFence == null && backgroundValid && (hover != shownHover || (hotbarVisible() && System.nanoTime() - backgroundAt >= BACKGROUND_INTERVAL))) {
            requestAsync(slotBits, scale);
            shownHover = hover;
        }
        cursor(frame, scale, windowX, windowY);
    }

    private static void hideSlotsAndCursor() {
        if (slotWindow != null) {
            slotWindow.hide();
        }
        if (cursorWindow != null) {
            cursorWindow.hide();
        }
        shownMask = 0L;
        cursorStack = null;
    }

    private static long withoutEmpty(long frame, boolean keepEmpty) {
        if (keepEmpty) {
            return frame;
        }
        long bits = frame;
        for (int i = 0; i < 41; i++) {
            if (((bits >>> i) & 1) != 0 && stack[i].method_7960()) {
                bits &= (1 << i) ^ (-1);
            }
        }
        return bits;
    }

    private static void hideAll() {
        hideSlotsAndCursor();
        TextPanels.hide();
    }

    private static int bar(class_1799 item) {
        if (item.method_31578()) {
            return (item.method_31580() << 5) | item.method_31579();
        }
        return -1;
    }

    private static int cellX(int cell, int px) {
        return (cell % 7) * px;
    }

    private static int cellY(int cell, int px) {
        return (cell / 7) * px;
    }

    private static int resolveIcons(long bits, int px) {
        int pending = 0;
        for (int i = 0; i < 42; i++) {
            if (((bits >>> i) & 1) != 0) {
                class_1799 item = stack[i];
                if (item != shownStack[i]) {
                    slotIcon[i] = null;
                }
                if (slotIcon[i] == null) {
                    if (item.method_7960() || !look.resolve(item)) {
                        slotIcon[i] = BLANK;
                    } else {
                        int[] icon = (int[]) icons.get(look.identity);
                        if (icon != null) {
                            slotIcon[i] = icon;
                        } else {
                            int cell = pending;
                            pending++;
                            ItemIcons.render(look, scratch, scratchView, scratchDepth, scratchDepthView, cellX(cell, px), cellY(cell, px), px);
                            pendingSlot[cell] = i;
                            pendingKey[cell] = Arrays.asList(look.identity.toArray());
                        }
                        look.method_65605();
                    }
                }
            }
        }
        return pending;
    }

    private static void resetIcons(int px) {
        icons.clear();
        cachedBytes = 0;
        Arrays.fill(slotIcon, (Object) null);
        dropAsync();
        backgroundValid = false;
        if (px != iconPx || scratch == null) {
            closeTextures();
            Arrays.fill(background, (Object) null);
            Arrays.fill(backgroundNext, (Object) null);
        }
    }

    private static void allocate(int px) {
        int side = 7 * px;
        GpuDevice device = RenderSystem.getDevice();
        scratch = device.createTexture("Anubis overlay icons", 15, TextureFormat.RGBA8, side, side, 1, 1);
        scratchView = device.createTextureView(scratch);
        scratchDepth = device.createTexture("Anubis overlay icon depth", 8, TextureFormat.DEPTH32, side, side, 1, 1);
        scratchDepthView = device.createTextureView(scratchDepth);
        device.createCommandEncoder().clearColorAndDepthTextures(scratch, 0, scratchDepth, 1.0d);
        iconPx = px;
    }

    private static GpuTexture itemAtlas(class_310 mc) {
        try {
            return mc.method_72703().method_73025(class_10725.field_64477).method_68004();
        } catch (RuntimeException e) {
            return null;
        }
    }

    private static void synchronousRead(long slotBits, int scale, boolean wantBackground, int pending) {
        int px = 16 * scale;
        int side = 7 * px;
        long scratchBytes = pending > 0 ? ((long) side) * ((long) side) * 4 : 0L;
        boolean framed = wantBackground && region(slotBits, scale, syncRegion);
        long frameBytes = framed ? ((long) syncRegion[2]) * ((long) syncRegion[3]) * 4 : 0L;
        long needed = framed ? scratchBytes + frameTextureBytes() : scratchBytes;
        long used = scratchBytes + frameBytes;
        if (wantBackground) {
            backgroundValid = true;
        }
        if (needed == 0) {
            if (wantBackground) {
                unpackBackground(null, 0, syncRegion, false, slotBits, scale);
                return;
            }
            return;
        }
        GpuDevice device = RenderSystem.getDevice();
        if (readback == null || readback.size() < needed) {
            if (readback != null) {
                readback.close();
            }
            readback = device.createBuffer(() -> {
                return "Anubis overlay readback";
            }, 9, needed);
        }
        CommandEncoder encoder = device.createCommandEncoder();
        if (pending > 0) {
            encoder.copyTextureToBuffer(scratch, readback, 0L, NOTHING, 0);
        }
        if (framed) {
            copyRegion(encoder, readback, scratchBytes, syncRegion);
        }
        GpuFence copied = encoder.createFence();
        try {
            GL11.glFlush();
            if (!copied.awaitCompletion(READBACK_TIMEOUT)) {
                throw new IllegalStateException("overlay read-back timed out");
            }
            if (copied != null) {
                copied.close();
            }
            GpuBuffer.MappedView view = encoder.mapBuffer(readback.slice(0L, used), true, false);
            try {
                ByteBuffer data = view.data();
                for (int c = 0; c < pending; c++) {
                    int[] icon = unpackIcon(data, cellX(c, px), cellY(c, px), side, px);
                    icons.put(pendingKey[c], icon);
                    pendingKey[c] = null;
                    cachedBytes += px * px * 4;
                    slotIcon[pendingSlot[c]] = icon;
                }
                if (wantBackground) {
                    unpackBackground(data, (int) scratchBytes, syncRegion, framed, slotBits, scale);
                    backgroundAt = System.nanoTime();
                }
                if (view != null) {
                    view.close();
                }
            } catch (Throwable th) {
                if (view != null) {
                    try {
                        view.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (Throwable th3) {
            if (copied != null) {
                try {
                    copied.close();
                } catch (Throwable th4) {
                    th3.addSuppressed(th4);
                }
            }
            throw th3;
        }
    }

    private static void requestAsync(long slotBits, int scale) {
        backgroundAt = System.nanoTime();
        if (region(slotBits, scale, asyncRegion)) {
            long frameBytes = frameTextureBytes();
            GpuDevice device = RenderSystem.getDevice();
            if (asyncBuffer == null || asyncBuffer.size() < frameBytes) {
                if (asyncBuffer != null) {
                    asyncBuffer.close();
                }
                asyncBuffer = device.createBuffer(() -> {
                    return "Anubis overlay background";
                }, 9, frameBytes);
            }
            CommandEncoder encoder = device.createCommandEncoder();
            copyRegion(encoder, asyncBuffer, 0L, asyncRegion);
            asyncFence = encoder.createFence();
            GL11.glFlush();
            asyncMask = slotBits;
            asyncScale = scale;
            for (int i = 0; i < 41; i++) {
                asyncX[i] = guiX[i];
                asyncY[i] = guiY[i];
            }
        }
    }

    private static boolean takeAsync(long slotBits, int scale) {
        if (asyncFence == null || !asyncFence.awaitCompletion(0L)) {
            return false;
        }
        boolean usable = asyncMask == slotBits && asyncScale == scale;
        for (int i = 0; usable && i < 41; i++) {
            if (((slotBits >>> i) & 1) != 0 && (asyncX[i] != guiX[i] || asyncY[i] != guiY[i])) {
                usable = false;
            }
        }
        boolean changed = false;
        if (usable) {
            long bytes = ((long) asyncRegion[2]) * ((long) asyncRegion[3]) * 4;
            CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
            GpuBuffer.MappedView view = encoder.mapBuffer(asyncBuffer.slice(0L, bytes), true, false);
            try {
                changed = unpackBackground(view.data(), 0, asyncRegion, true, slotBits, scale);
                if (view != null) {
                    view.close();
                }
            } catch (Throwable th) {
                if (view != null) {
                    try {
                        view.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        dropAsync();
        return changed;
    }

    private static void dropAsync() {
        if (asyncFence == null) {
            return;
        }
        asyncFence.close();
        asyncFence = null;
    }

    private static long frameTextureBytes() {
        GpuTexture frame = class_310.method_1551().method_1522().method_30277();
        return ((long) frame.getWidth(0)) * ((long) frame.getHeight(0)) * 4;
    }

    private static boolean region(long slotBits, int scale, int[] out) {
        class_1041 win = class_310.method_1551().method_22683();
        int px = 16 * scale;
        int x0 = Integer.MAX_VALUE;
        int y0 = Integer.MAX_VALUE;
        int x1 = Integer.MIN_VALUE;
        int y1 = Integer.MIN_VALUE;
        for (int i = 0; i < 41; i++) {
            if (((slotBits >>> i) & 1) != 0) {
                x0 = Math.min(x0, guiX[i] * scale);
                y0 = Math.min(y0, guiY[i] * scale);
                x1 = Math.max(x1, (guiX[i] * scale) + px);
                y1 = Math.max(y1, (guiY[i] * scale) + px);
            }
        }
        int x2 = Math.max(x0, 0);
        int y2 = Math.max(y0, 0);
        int x3 = Math.min(x1, win.method_4489());
        int y3 = Math.min(y1, win.method_4506());
        if (x3 <= x2 || y3 <= y2) {
            return false;
        }
        out[0] = x2;
        out[1] = y2;
        out[2] = x3 - x2;
        out[3] = y3 - y2;
        return true;
    }

    private static void copyRegion(CommandEncoder encoder, GpuBuffer target, long offset, int[] region) {
        class_310 mc = class_310.method_1551();
        encoder.copyTextureToBuffer(mc.method_1522().method_30277(), target, offset, NOTHING, 0, region[0], (mc.method_22683().method_4506() - region[1]) - region[3], region[2], region[3]);
    }

    private static int[] unpackIcon(ByteBuffer data, int cellX, int cellY, int side, int px) {
        int[] out = new int[px * px];
        for (int y = 0; y < px; y++) {
            int row = ((((side - 1) - (cellY + y)) * side) + cellX) * 4;
            for (int x = 0; x < px; x++) {
                int at = row + (x * 4);
                out[(y * px) + x] = ((data.get(at + 3) & 255) << 24) | ((data.get(at) & 255) << 16) | ((data.get(at + 1) & 255) << 8) | (data.get(at + 2) & 255);
            }
        }
        return out;
    }

    private static boolean unpackBackground(ByteBuffer data, int offset, int[] region, boolean framed, long slotBits, int scale) {
        int px = 16 * scale;
        int rx = region[0];
        int ry = region[1];
        int rw = region[2];
        int rh = region[3];
        boolean changed = false;
        for (int slot = 0; slot < 41; slot++) {
            if (((slotBits >>> slot) & 1) != 0) {
                int cx = (guiX[slot] * scale) - rx;
                int cy = (guiY[slot] * scale) - ry;
                if (!framed || cx < 0 || cy < 0 || cx + px > rw || cy + px > rh) {
                    if (background[slot] != null) {
                        changed = true;
                    }
                    background[slot] = null;
                } else {
                    int[] next = backgroundNext[slot];
                    if (next == null || next.length != px * px) {
                        next = new int[px * px];
                    }
                    for (int y = 0; y < px; y++) {
                        int row = offset + (((((rh - 1) - (cy + y)) * rw) + cx) * 4);
                        for (int x = 0; x < px; x++) {
                            int at = row + (x * 4);
                            next[(y * px) + x] = (-16777216) | ((data.get(at) & 255) << 16) | ((data.get(at + 1) & 255) << 8) | (data.get(at + 2) & 255);
                        }
                    }
                    int[] current = background[slot];
                    if (current == null || !Arrays.equals(current, next)) {
                        changed = true;
                        background[slot] = next;
                        backgroundNext[slot] = current;
                    } else {
                        backgroundNext[slot] = next;
                    }
                }
            }
        }
        return changed;
    }

    private static void composeSlots(long slotBits, int scale, boolean opaque, boolean selection, int windowX, int windowY) {
        int px = 16 * scale;
        OverlayArt.Image frame = selection ? OverlayArt.selection() : null;
        boolean drawSelection = frame != null;
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (int i = 0; i < 41; i++) {
            if (((slotBits >>> i) & 1) != 0) {
                minX = Math.min(minX, guiX[i]);
                minY = Math.min(minY, guiY[i]);
                maxX = Math.max(maxX, guiX[i] + 17);
                maxY = Math.max(maxY, guiY[i] + 17);
            }
        }
        if (drawSelection) {
            minX = Math.min(minX, selectionX);
            minY = Math.min(minY, selectionY);
            maxX = Math.max(maxX, selectionX + frame.width());
            maxY = Math.max(maxY, selectionY + frame.height());
        }
        if (minX > maxX) {
            return;
        }
        int bx = minX * scale;
        int by = minY * scale;
        int bw = (maxX - minX) * scale;
        int bh = (maxY - minY) * scale;
        canvas.wrap(slotWindow.pixels(bw, bh), slotWindow.stride(), bw, bh);
        if (opaque) {
            for (int i2 = 0; i2 < 41; i2++) {
                if (((slotBits >>> i2) & 1) != 0) {
                    int ox = (guiX[i2] * scale) - bx;
                    int oy = (guiY[i2] * scale) - by;
                    int[] behind = background[i2];
                    if (behind != null) {
                        canvas.copy(behind, px, px, ox, oy);
                    } else {
                        canvas.rect(ox, oy, px, px, FALLBACK_BACKGROUND);
                    }
                }
            }
        }
        if (drawSelection) {
            canvas.blend(frame.pixels(), frame.width(), frame.height(), (selectionX * scale) - bx, (selectionY * scale) - by, scale);
        }
        for (int i3 = 0; i3 < 41; i3++) {
            if (((slotBits >>> i3) & 1) != 0) {
                int ox2 = (guiX[i3] * scale) - bx;
                int oy2 = (guiY[i3] * scale) - by;
                int[] icon = slotIcon[i3];
                if (icon != null && icon != BLANK) {
                    canvas.blend(icon, px, px, ox2, oy2, 1);
                }
                if (!stack[i3].method_7960()) {
                    DigitFont.decorations(canvas, stack[i3], ox2, oy2, scale);
                }
            }
        }
        slotWindow.present(windowX + bx, windowY + by, bw, bh);
        for (int i4 = 0; i4 < 41; i4++) {
            if (((slotBits >>> i4) & 1) != 0) {
                shownStack[i4] = stack[i4];
                shownX[i4] = guiX[i4];
                shownY[i4] = guiY[i4];
                shownCount[i4] = stack[i4].method_7947();
                shownBar[i4] = bar(stack[i4]);
            }
        }
        shownMask = slotBits;
        shownScale = scale;
        shownOpaque = opaque;
        shownSelection = selection;
        shownSelectionX = selectionX;
        shownSelectionY = selectionY;
        shownWindowX = windowX;
        shownWindowY = windowY;
        boxX = bx;
        boxY = by;
    }

    private static void cursor(long frame, int scale, int windowX, int windowY) {
        if (((frame >>> 41) & 1) == 0) {
            if (cursorWindow != null) {
                cursorWindow.hide();
            }
            cursorStack = null;
            return;
        }
        class_1799 item = stack[41];
        int px = 16 * scale;
        int screenX = windowX + (guiX[41] * scale);
        int screenY = windowY + (guiY[41] * scale);
        boolean same = item == cursorStack && item.method_7947() == cursorCount && bar(item) == cursorBar && scale == cursorScale;
        if (same) {
            if (screenX != cursorX || screenY != cursorY) {
                cursorWindow.move(screenX, screenY);
            }
        } else {
            int size = px + scale;
            canvas.wrap(cursorWindow.pixels(size, size), cursorWindow.stride(), size, size);
            int[] icon = slotIcon[41];
            if (icon != null && icon != BLANK) {
                canvas.blend(icon, px, px, 0, 0, 1);
            }
            DigitFont.decorations(canvas, item, 0, 0, scale);
            cursorWindow.present(screenX, screenY, size, size);
            cursorStack = item;
            cursorCount = item.method_7947();
            cursorBar = bar(item);
            cursorScale = scale;
        }
        cursorX = screenX;
        cursorY = screenY;
        shownStack[41] = item;
    }

    private static void closeTextures() {
        if (scratchView != null) {
            scratchView.close();
        }
        if (scratch != null) {
            scratch.close();
        }
        if (scratchDepthView != null) {
            scratchDepthView.close();
        }
        if (scratchDepth != null) {
            scratchDepth.close();
        }
        scratchView = null;
        scratch = null;
        scratchDepthView = null;
        scratchDepth = null;
        iconPx = 0;
    }

    private static void release() {
        dropAsync();
        if (slotWindow != null) {
            slotWindow.destroy();
            slotWindow = null;
        }
        if (cursorWindow != null) {
            cursorWindow.destroy();
            cursorWindow = null;
        }
        TextPanels.release();
        closeTextures();
        if (readback != null) {
            readback.close();
            readback = null;
        }
        if (asyncBuffer != null) {
            asyncBuffer.close();
            asyncBuffer = null;
        }
        icons.clear();
        cachedBytes = 0;
        Arrays.fill(slotIcon, (Object) null);
        Arrays.fill(pendingKey, (Object) null);
        Arrays.fill(background, (Object) null);
        Arrays.fill(backgroundNext, (Object) null);
        Arrays.fill(stack, (Object) null);
        Arrays.fill(shownStack, (Object) null);
        mask = 0L;
        fakeMask = 0L;
        shownMask = 0L;
        shownAtlas = null;
        shownHover = -1;
        cursorStack = null;
        backgroundValid = false;
    }

    private static List<Object>[] newKeys(int n) {
        return new List[n];
    }
}
