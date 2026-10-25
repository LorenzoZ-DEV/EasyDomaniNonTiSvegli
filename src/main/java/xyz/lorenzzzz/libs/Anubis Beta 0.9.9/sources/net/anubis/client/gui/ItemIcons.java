package net.anubis.client.gui;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.GpuFence;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import imgui.ImDrawList;
import imgui.flag.ImGuiWindowFlags;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.anubis.client.imgui.ImGuiManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1011;
import net.minecraft.class_10366;
import net.minecraft.class_10444;
import net.minecraft.class_10725;
import net.minecraft.class_10868;
import net.minecraft.class_11278;
import net.minecraft.class_11515;
import net.minecraft.class_11661;
import net.minecraft.class_11684;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_308;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_4608;
import net.minecraft.class_4696;
import net.minecraft.class_758;
import net.minecraft.class_9799;
import org.joml.Matrix4fStack;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/ItemIcons.class */
@Environment(EnvType.CLIENT)
public final class ItemIcons {
    private static final int SHEET_SIDE = 512;
    private static final int MIN_PX = 8;
    private static final int MAX_PX = 96;
    private static final int MAX_SHEETS = 8;
    private static final int BUSY_FRAMES = 2;
    private static final int IDLE_FRAMES = 600;
    private static final int PAINTS_PER_FRAME = 6;
    private static GpuTexture depth;
    private static GpuTextureView depthView;
    private static class_11278 projection;
    private static class_11661 submits;
    private static class_11684 features;
    private static class_4597.class_4598 buffers;
    private static GpuBuffer readback;
    private static ByteBuffer straightened;
    private static boolean straightenFailed;
    private static GpuTexture itemAtlas;
    private static int retiredFrame;
    private static int paintsLeft;
    private static boolean disabled;
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private static final Cell BLANK = new Cell();
    private static final Int2ObjectOpenHashMap<Sheet> SHEETS = new Int2ObjectOpenHashMap<>();
    private static final List<Sheet> RETIRED = new ArrayList();
    private static final Reference2ObjectOpenHashMap<class_1792, class_1799> PLAIN = new Reference2ObjectOpenHashMap<>();
    private static final ReferenceOpenHashSet<class_1792> BROKEN = new ReferenceOpenHashSet<>();
    private static final ItemLook MODEL = new ItemLook();
    private static final int[] VIEWPORT = new int[4];
    private static int frame = ImGuiWindowFlags.NavFlattened;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/ItemIcons$Cell.class */
    @Environment(EnvType.CLIENT)
    private static final class Cell {
        int texture;
        float u0;
        float v0;
        float u1;
        float v1;

        private Cell() {
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/ItemIcons$Sheet.class */
    @Environment(EnvType.CLIENT)
    private static final class Sheet {
        final int px;
        final Object2ObjectOpenHashMap<Object, Cell> cells = new Object2ObjectOpenHashMap<>();
        GpuTexture color;
        GpuTextureView colorView;
        int filled;
        int lastUsed;

        Sheet(int px) {
            this.px = px;
        }

        boolean full() {
            int perRow = 512 / this.px;
            return this.filled >= perRow * perRow;
        }

        void release() {
            if (this.colorView != null) {
                this.colorView.close();
            }
            if (this.color != null) {
                this.color.close();
            }
            this.colorView = null;
            this.color = null;
            this.cells.clear();
        }
    }

    private ItemIcons() {
    }

    static float pixels(float size) {
        float snapped = size < 16.0f ? Math.round(size) : 16.0f * ((float) Math.floor((size / 16.0f) + 0.25f));
        return Math.max(8.0f, Math.min(96.0f, snapped));
    }

    static void draw(ImDrawList dl, class_1792 item, float x, float y, float size, float alpha) {
        if (item == null || item == class_1802.field_8162) {
            return;
        }
        class_1799 plain = (class_1799) PLAIN.get(item);
        if (plain == null) {
            plain = item.method_7854();
            PLAIN.put(item, plain);
        }
        draw(dl, plain, x, y, size, alpha);
    }

    public static void draw(ImDrawList dl, class_1799 stack, float x, float y, float size, float alpha) {
        if (stack == null || stack.method_7960() || alpha <= 0.004f) {
            return;
        }
        startFrame();
        int px = (int) pixels(size);
        float x0 = Math.round(x);
        float y0 = Math.round(y);
        try {
            Cell cell = cell(stack, px);
            MODEL.method_65605();
            if (cell == null) {
                return;
            }
            if (cell.texture == 0) {
                float r = Math.max(1.0f, px * 0.18f);
                dl.addRectFilled(x0 + 1.0f, y0 + 1.0f, (x0 + px) - 1.0f, (y0 + px) - 1.0f, HudChrome.scaleAlpha(452984831, alpha), r);
            } else {
                dl.addImage(cell.texture, x0, y0, x0 + px, y0 + px, cell.u0, cell.v0, cell.u1, cell.v1, HudChrome.scaleAlpha(-1, alpha));
            }
        } catch (Throwable th) {
            MODEL.method_65605();
            throw th;
        }
    }

    private static void startFrame() {
        int now = ImGuiManager.getInstance().frameCount();
        if (now == frame) {
            return;
        }
        frame = now;
        paintsLeft = 6;
        if (!RETIRED.isEmpty() && now != retiredFrame) {
            Iterator<Sheet> it = RETIRED.iterator();
            while (it.hasNext()) {
                it.next().release();
            }
            RETIRED.clear();
        }
        GpuTexture atlas = currentAtlas();
        if (atlas != itemAtlas) {
            itemAtlas = atlas;
            ObjectIterator it2 = SHEETS.values().iterator();
            while (it2.hasNext()) {
                ((Sheet) it2.next()).release();
            }
            SHEETS.clear();
            BROKEN.clear();
            return;
        }
        ObjectIterator it3 = SHEETS.values().iterator();
        while (it3.hasNext()) {
            Sheet sheet = (Sheet) it3.next();
            if (now - sheet.lastUsed > 600) {
                sheet.release();
                it3.remove();
            }
        }
    }

    private static GpuTexture currentAtlas() {
        try {
            return class_310.method_1551().method_72703().method_73025(class_10725.field_64477).method_68004();
        } catch (RuntimeException e) {
            return null;
        }
    }

    private static Cell cell(class_1799 stack, int px) {
        Sheet sheet = sheet(px);
        if (sheet == null) {
            return null;
        }
        sheet.lastUsed = frame;
        if (!resolve(stack)) {
            return BLANK;
        }
        Cell cell = (Cell) sheet.cells.get(MODEL.identity);
        if (cell != null) {
            return cell;
        }
        if (disabled) {
            return BLANK;
        }
        if (paintsLeft <= 0) {
            return null;
        }
        paintsLeft--;
        if (sheet.full()) {
            retire(sheet);
            sheet = new Sheet(px);
            sheet.lastUsed = frame;
            SHEETS.put(px, sheet);
        }
        Cell cell2 = paint(sheet, seeThrough(stack.method_7909()));
        sheet.cells.put(Arrays.asList(MODEL.identity.toArray()), cell2);
        return cell2;
    }

    private static Sheet sheet(int px) {
        Sheet sheet = (Sheet) SHEETS.get(px);
        if (sheet != null) {
            return sheet;
        }
        if (SHEETS.size() >= 8) {
            Sheet oldest = stalest();
            if (oldest == null || frame - oldest.lastUsed < 2) {
                return null;
            }
            retire(oldest);
        }
        Sheet sheet2 = new Sheet(px);
        SHEETS.put(px, sheet2);
        return sheet2;
    }

    private static Sheet stalest() {
        Sheet oldest = null;
        ObjectIterator it = SHEETS.values().iterator();
        while (it.hasNext()) {
            Sheet sheet = (Sheet) it.next();
            if (oldest == null || sheet.lastUsed < oldest.lastUsed) {
                oldest = sheet;
            }
        }
        return oldest;
    }

    private static void retire(Sheet sheet) {
        SHEETS.remove(sheet.px);
        RETIRED.add(sheet);
        retiredFrame = frame;
    }

    private static boolean resolve(class_1799 stack) {
        if (BROKEN.contains(stack.method_7909())) {
            return false;
        }
        if (MODEL.resolve(stack)) {
            return true;
        }
        BROKEN.add(stack.method_7909());
        return false;
    }

    private static boolean seeThrough(class_1792 item) {
        if (!(item instanceof class_1747)) {
            return false;
        }
        class_1747 block = (class_1747) item;
        return class_4696.method_23679(block.method_7711().method_9564()) == class_11515.field_60926;
    }

    private static Cell paint(Sheet sheet, boolean seeThrough) {
        if (MODEL.method_65606()) {
            return BLANK;
        }
        try {
            if (sheet.color == null) {
                allocate(sheet);
            }
            int px = sheet.px;
            int perRow = 512 / px;
            int index = sheet.filled;
            sheet.filled = index + 1;
            int left = (index % perRow) * px;
            int top = (index / perRow) * px;
            int glTop = (512 - top) - px;
            render(MODEL, sheet.color, sheet.colorView, depth, depthView, left, top, px);
            if (seeThrough) {
                straighten(sheet.color, left, glTop, px);
            }
            Cell cell = new Cell();
            cell.texture = sheet.color.method_68427();
            cell.u0 = left / 512.0f;
            cell.u1 = (left + px) / 512.0f;
            cell.v0 = (512 - top) / 512.0f;
            cell.v1 = glTop / 512.0f;
            return cell;
        } catch (RuntimeException failure) {
            disabled = true;
            LOGGER.warn("HUD item icons could not be drawn; showing plain slots instead", failure);
            return BLANK;
        }
    }

    private static void allocate(Sheet sheet) {
        GpuDevice device = RenderSystem.getDevice();
        if (depth == null) {
            depth = device.createTexture("Anubis HUD icon depth", 8, TextureFormat.DEPTH32, 512, 512, 1, 1);
            depthView = device.createTextureView(depth);
        }
        sheet.color = device.createTexture("Anubis HUD icons " + sheet.px, 15, TextureFormat.RGBA8, 512, 512, 1, 1);
        sheet.colorView = device.createTextureView(sheet.color);
        device.createCommandEncoder().clearColorAndDepthTextures(sheet.color, 0, depth, 1.0d);
        class_10868 class_10868Var = sheet.color;
        if (class_10868Var instanceof class_10868) {
            class_10868 gl = class_10868Var;
            nearest(gl);
        }
    }

    public static void render(class_10444 model, GpuTexture target, GpuTextureView targetView, GpuTexture targetDepth, GpuTextureView targetDepthView, int x, int y, int cell) {
        class_310 mc = class_310.method_1551();
        int side = target.getWidth(0);
        int glY = (side - y) - cell;
        RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(target, 0, targetDepth, 1.0d, x, glY, cell, cell);
        if (features == null) {
            buffers = class_4597.method_22991(new class_9799(262144));
            submits = new class_11661();
            features = new class_11684(submits, mc.method_1541(), buffers, mc.method_72703(), mc.method_22940().method_23003(), mc.method_22940().method_23001(), mc.field_1772);
        }
        GpuTextureView colorBefore = RenderSystem.outputColorTextureOverride;
        GpuTextureView depthBefore = RenderSystem.outputDepthTextureOverride;
        GpuBufferSlice lightsBefore = RenderSystem.getShaderLights();
        GpuBufferSlice fogBefore = RenderSystem.getShaderFog();
        GpuBufferSlice projectionBefore = RenderSystem.getProjectionMatrixBuffer();
        class_10366 projectionTypeBefore = RenderSystem.getProjectionType();
        GL11.glGetIntegerv(2978, VIEWPORT);
        GlStateManager._viewport(VIEWPORT[0], VIEWPORT[1], VIEWPORT[2], VIEWPORT[3]);
        Matrix4fStack modelView = RenderSystem.getModelViewStack();
        modelView.pushMatrix();
        modelView.identity();
        try {
            RenderSystem.outputColorTextureOverride = targetView;
            RenderSystem.outputDepthTextureOverride = targetDepthView;
            if (projection == null) {
                projection = new class_11278("Anubis icons", -1000.0f, 1000.0f, true);
            }
            RenderSystem.setProjectionMatrix(projection.method_71092(side, side), class_10366.field_54954);
            class_308.class_11274 light = model.method_65608() ? class_308.class_11274.field_60027 : class_308.class_11274.field_60026;
            mc.field_1773.method_71114().method_71034(light);
            RenderSystem.setShaderFog(mc.field_1773.anubis$getFogRenderer().method_71109(class_758.class_4596.field_60101));
            RenderSystem.enableScissorForRenderTypeDraws(x, glY, cell, cell);
            class_4587 pose = new class_4587();
            pose.method_46416(x + (cell * 0.5f), y + (cell * 0.5f), 0.0f);
            pose.method_22905(cell, -cell, cell);
            try {
                model.method_65604(pose, submits, 15728880, class_4608.field_21444, 0);
                features.method_73002();
                submits.method_72953();
                buffers.method_22993();
                RenderSystem.disableScissorForRenderTypeDraws();
                RenderSystem.outputColorTextureOverride = colorBefore;
                RenderSystem.outputDepthTextureOverride = depthBefore;
                RenderSystem.setShaderLights(lightsBefore);
                RenderSystem.setShaderFog(fogBefore);
                RenderSystem.setProjectionMatrix(projectionBefore, projectionTypeBefore);
                modelView.popMatrix();
                GlStateManager._viewport(VIEWPORT[0], VIEWPORT[1], VIEWPORT[2], VIEWPORT[3]);
            } catch (Throwable th) {
                submits.method_72953();
                buffers.method_22993();
                throw th;
            }
        } catch (Throwable th2) {
            RenderSystem.disableScissorForRenderTypeDraws();
            RenderSystem.outputColorTextureOverride = colorBefore;
            RenderSystem.outputDepthTextureOverride = depthBefore;
            RenderSystem.setShaderLights(lightsBefore);
            RenderSystem.setShaderFog(fogBefore);
            RenderSystem.setProjectionMatrix(projectionBefore, projectionTypeBefore);
            modelView.popMatrix();
            GlStateManager._viewport(VIEWPORT[0], VIEWPORT[1], VIEWPORT[2], VIEWPORT[3]);
            throw th2;
        }
    }

    static void straighten(GpuTexture page, int x, int y, int cell) {
        if (straightenFailed) {
            return;
        }
        int bytes = cell * cell * 4;
        long whole = ((long) page.getWidth(0)) * ((long) page.getHeight(0)) * 4;
        try {
            GpuDevice device = RenderSystem.getDevice();
            if (readback == null || readback.size() < whole) {
                if (readback != null) {
                    readback.close();
                }
                readback = device.createBuffer(() -> {
                    return "Anubis icon readback";
                }, 9, whole);
            }
            if (straightened == null || straightened.capacity() < bytes) {
                straightened = BufferUtils.createByteBuffer(bytes);
            }
            CommandEncoder encoder = device.createCommandEncoder();
            encoder.copyTextureToBuffer(page, readback, 0L, () -> {
            }, 0, x, y, cell, cell);
            GpuFence copied = encoder.createFence();
            try {
                GL11.glFlush();
                if (!copied.awaitCompletion(1000000000L)) {
                    throw new IllegalStateException("icon read-back timed out");
                }
                if (copied != null) {
                    copied.close();
                }
                straightened.clear().limit(bytes);
                GpuBuffer.MappedView view = encoder.mapBuffer(readback.slice(0L, bytes), true, false);
                try {
                    ByteBuffer read = view.data();
                    for (int i = 0; i < bytes; i += 4) {
                        int a = read.get(i + 3) & 255;
                        for (int ch = 0; ch < 3; ch++) {
                            int v = a == 0 ? 0 : Math.min(255, (((read.get(i + ch) & 255) * 255) + (a / 2)) / a);
                            straightened.put(i + ch, (byte) v);
                        }
                        straightened.put(i + 3, (byte) a);
                    }
                    if (view != null) {
                        view.close();
                    }
                    encoder.writeToTexture(page, straightened, class_1011.class_1012.field_4997, 0, 0, x, y, cell, cell);
                    return;
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
        } catch (RuntimeException e) {
            straightenFailed = true;
            LOGGER.warn("Couldn't read icons back; see-through ones stay darker", e);
        }
        straightenFailed = true;
        LOGGER.warn("Couldn't read icons back; see-through ones stay darker", e);
    }

    static void nearest(class_10868 gl) {
        int bound = GL11.glGetInteger(32873);
        GL11.glBindTexture(3553, gl.method_68427());
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10240, 9728);
        GL11.glBindTexture(3553, bound);
    }
}
