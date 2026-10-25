package net.anubis.client.imgui;

import com.mojang.blaze3d.opengl.GlStateManager;
import imgui.ImFont;
import imgui.ImFontConfig;
import imgui.ImFontGlyphRangesBuilder;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.function.BooleanSupplier;
import net.anubis.client.addon.AddonManager;
import net.anubis.client.config.ThemeStore;
import net.anubis.client.gui.GuiPalette;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_1041;
import net.minecraft.class_310;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMonitorCallback;
import org.lwjgl.glfw.GLFWMonitorCallbackI;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/imgui/ImGuiManager.class */
@Environment(EnvType.CLIENT)
public final class ImGuiManager {
    private static final ImGuiManager INSTANCE = new ImGuiManager();
    private static final String CATEGORY_FONT_RESOURCE = "/assets/anubis/fonts/geist-semibold.ttf";
    private static final String MODULE_FONT_RESOURCE = "/assets/anubis/fonts/geist-medium.ttf";
    private static final String HUD_VALUE_FONT_RESOURCE = "/assets/anubis/fonts/geist-semibold.ttf";
    private static final String HUD_TEXT_FONT_RESOURCE = "/assets/anubis/fonts/geist-semibold.ttf";
    private static final String HUD_LABEL_FONT_RESOURCE = "/assets/anubis/fonts/geist-medium.ttf";
    private static final String MENU_FONT_RESOURCE = "/assets/anubis/fonts/rajdhani-bold.ttf";
    public static final float MENU_FONT_SIZE = 24.0f;
    public static final float MENU_TAG_FONT_SIZE = 15.0f;
    public static final float MENU_BODY_FONT_SIZE = 18.0f;
    public static final float MENU_NAME_FONT_SIZE = 20.0f;
    public static final float MENU_ITEM_FONT_SIZE = 32.0f;
    public static final float HUD_VALUE_FONT_SIZE = 18.0f;
    public static final float HUD_TEXT_FONT_SIZE = 17.0f;
    public static final float HUD_LABEL_FONT_SIZE = 14.0f;
    private static final String EXTENDED_LATIN_GLYPHS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789ÇĞİÖŞÜçğıöşüÂâÊêÎîÔôÛûÀÁÄÈÉËÌÍÏÒÓÖÙÚÜÝàáäèéëìíïòóöùúüýÿ−∞";
    private static final float REFERENCE_HEIGHT = 1080.0f;
    private GLFWKeyCallback minecraftKeys;
    private GLFWMonitorCallback minecraftMonitors;
    private boolean initialized;
    private boolean idleSkipped;
    private Runnable activeRenderer;
    private Runnable hudRenderer;
    private Runnable toastRenderer;
    private Runnable overlayRenderer;
    private Runnable streamRenderer;
    private Runnable alertRenderer;
    private int frameCount;
    private float frameDelta;
    private ImFont categoryFont;
    private ImFont moduleFont;
    private ImFont titleFont;
    private ImFont smallFont;
    private ImFont hudValueFont;
    private ImFont hudTextFont;
    private ImFont hudLabelFont;
    private ImFont menuFont;
    private ImFont menuTagFont;
    private ImFont menuBodyFont;
    private ImFont menuNameFont;
    private ImFont menuItemFont;
    private short[] glyphRanges;
    private final ImGuiImplGlfw glfw = new ImGuiImplGlfw() { // from class: net.anubis.client.imgui.ImGuiManager.1
        @Override // imgui.glfw.ImGuiImplGlfw
        public void keyCallback(long window, int keycode, int scancode, int action, int mods) {
            boolean zFeedsKeyPress;
            boolean tracked = keycode >= 0 && keycode < ImGuiManager.this.imguiKeysDown.length;
            if (action == 0) {
                zFeedsKeyPress = tracked && ImGuiManager.this.imguiKeysDown[keycode];
            } else {
                zFeedsKeyPress = ImGuiManager.this.feedsKeyPress(keycode, mods);
            }
            boolean feed = zFeedsKeyPress;
            if (!feed) {
                if (ImGuiManager.this.minecraftKeys != null) {
                    ImGuiManager.this.minecraftKeys.invoke(window, keycode, scancode, action, mods);
                }
            } else {
                if (tracked) {
                    ImGuiManager.this.imguiKeysDown[keycode] = action != 0;
                }
                super.keyCallback(window, keycode, scancode, action, mods);
            }
        }

        @Override // imgui.glfw.ImGuiImplGlfw
        public void monitorCallback(long monitor, int event) {
            super.monitorCallback(monitor, event);
            if (ImGuiManager.this.minecraftMonitors != null) {
                ImGuiManager.this.minecraftMonitors.invoke(monitor, event);
            }
        }

        @Override // imgui.glfw.ImGuiImplGlfw
        public void cursorPosCallback(long window, double x, double y) {
            super.cursorPosCallback(window, x, y);
            ImGuiManager.feedScaledCursor(x, y);
        }

        @Override // imgui.glfw.ImGuiImplGlfw
        protected void updateMouseData() {
            super.updateMouseData();
            long handle = class_310.method_1551().method_22683().method_4490();
            if (GLFW.glfwGetWindowAttrib(handle, 131073) == 0) {
                return;
            }
            double[] cx = new double[1];
            double[] cy = new double[1];
            GLFW.glfwGetCursorPos(handle, cx, cy);
            ImGuiManager.feedScaledCursor(cx[0], cy[0]);
        }
    };
    private final ImGuiImplGl3 gl3 = new ImGuiImplGl3();
    private final boolean[] imguiKeysDown = new boolean[349];
    private BooleanSupplier hudHasWork = () -> {
        return true;
    };
    private BooleanSupplier toastHasWork = () -> {
        return false;
    };
    private BooleanSupplier streamHasWork = () -> {
        return false;
    };
    private BooleanSupplier overlayHasWork = () -> {
        return false;
    };
    private BooleanSupplier alertHasWork = () -> {
        return false;
    };
    private float uiScale = 1.0f;
    private float userScale = 1.0f;

    private static void feedScaledCursor(double x, double y) {
        float sx = pixelsPerPointX();
        float sy = pixelsPerPointY();
        if (sx == 1.0f && sy == 1.0f) {
            return;
        }
        ImGui.getIO().addMousePosEvent((float) (x * ((double) sx)), (float) (y * ((double) sy)));
    }

    private static float pixelsPerPointX() {
        class_1041 window = class_310.method_1551().method_22683();
        if (window.method_4480() > 0) {
            return window.method_4489() / window.method_4480();
        }
        return 1.0f;
    }

    private static float pixelsPerPointY() {
        class_1041 window = class_310.method_1551().method_22683();
        if (window.method_4507() > 0) {
            return window.method_4506() / window.method_4507();
        }
        return 1.0f;
    }

    private ImGuiManager() {
    }

    public static ImGuiManager getInstance() {
        return INSTANCE;
    }

    public ImFont categoryFont() {
        return this.categoryFont;
    }

    public ImFont moduleFont() {
        return this.moduleFont;
    }

    public ImFont titleFont() {
        return this.titleFont;
    }

    public ImFont smallFont() {
        return this.smallFont;
    }

    public ImFont hudValueFont() {
        return this.hudValueFont;
    }

    public ImFont hudTextFont() {
        return this.hudTextFont;
    }

    public ImFont hudLabelFont() {
        return this.hudLabelFont;
    }

    public ImFont menuFont() {
        return this.menuFont;
    }

    public ImFont menuTagFont() {
        return this.menuTagFont;
    }

    public ImFont menuBodyFont() {
        return this.menuBodyFont;
    }

    public ImFont menuNameFont() {
        return this.menuNameFont;
    }

    public ImFont menuItemFont() {
        return this.menuItemFont;
    }

    public float uiScale() {
        return this.uiScale;
    }

    public int frameCount() {
        return this.frameCount;
    }

    public float frameDelta() {
        return this.frameDelta;
    }

    public void activate(Runnable renderer) {
        this.activeRenderer = renderer;
    }

    private boolean feedsKeyPress(int keycode, int mods) {
        return this.activeRenderer != null && (keycode != 258 || (mods & 2) == 0);
    }

    public void deactivate() {
        String ini;
        this.activeRenderer = null;
        if (!this.initialized || (ini = ImGui.getIO().getIniFilename()) == null) {
            return;
        }
        ImGui.saveIniSettingsToDisk(ini);
    }

    public void setHudRenderer(Runnable renderer, BooleanSupplier hasWork) {
        this.hudRenderer = renderer;
        this.hudHasWork = hasWork;
    }

    public void setToastRenderer(Runnable renderer, BooleanSupplier hasWork) {
        this.toastRenderer = renderer;
        this.toastHasWork = hasWork;
    }

    public void setStreamRenderer(Runnable renderer, BooleanSupplier hasWork) {
        this.streamRenderer = renderer;
        this.streamHasWork = hasWork;
    }

    public void setOverlayRenderer(Runnable renderer, BooleanSupplier hasWork) {
        this.overlayRenderer = renderer;
        this.overlayHasWork = hasWork;
    }

    public void setAlertRenderer(Runnable renderer, BooleanSupplier hasWork) {
        this.alertRenderer = renderer;
        this.alertHasWork = hasWork;
    }

    public void renderIfActive() {
        boolean hud = this.hudRenderer != null && this.hudHasWork.getAsBoolean();
        boolean toasts = this.toastRenderer != null && this.toastHasWork.getAsBoolean();
        boolean overlay = this.overlayRenderer != null && this.overlayHasWork.getAsBoolean();
        boolean alert = this.alertRenderer != null && this.alertHasWork.getAsBoolean();
        boolean stream = this.streamRenderer != null && this.streamHasWork.getAsBoolean();
        if (this.activeRenderer == null && !hud && !toasts && !overlay && !alert && !stream) {
            if (this.initialized) {
                ImGui.getIO().clearEventsQueue();
                this.idleSkipped = true;
                return;
            }
            return;
        }
        initIfNeeded();
        rebuildFontsIfScaleChanged();
        if (this.idleSkipped) {
            ImGuiIO io2 = ImGui.getIO();
            io2.clearInputKeys();
            io2.clearInputMouse();
            Arrays.fill(this.imguiKeysDown, false);
            this.idleSkipped = false;
        }
        class_310 mc = class_310.method_1551();
        int w = mc.method_22683().method_4489();
        int h = mc.method_22683().method_4506();
        GL30.glBindFramebuffer(36160, 0);
        GL11.glViewport(0, 0, w, h);
        GlStateManager._viewport(0, 0, w, h);
        this.glfw.newFrame();
        this.frameDelta = Math.min(ImGui.getIO().getDeltaTime(), 0.1f);
        ImGui.getIO().setDeltaTime(this.frameDelta);
        updateDisplayMetrics();
        this.gl3.newFrame();
        ImGui.newFrame();
        this.frameCount++;
        GuiPalette.refresh();
        if (this.streamRenderer != null) {
            this.streamRenderer.run();
        }
        if (this.hudRenderer != null) {
            this.hudRenderer.run();
        }
        if (this.activeRenderer != null) {
            this.activeRenderer.run();
        }
        if (this.toastRenderer != null) {
            this.toastRenderer.run();
        }
        if (this.alertRenderer != null) {
            this.alertRenderer.run();
        }
        if (this.overlayRenderer != null) {
            this.overlayRenderer.run();
        }
        ImGui.render();
        this.gl3.renderDrawData(ImGui.getDrawData());
    }

    public void dispose() {
        if (this.initialized) {
            this.gl3.shutdown();
            this.glfw.shutdown();
            ImGui.destroyContext();
            this.categoryFont = null;
            this.moduleFont = null;
            this.titleFont = null;
            this.smallFont = null;
            this.hudValueFont = null;
            this.hudTextFont = null;
            this.hudLabelFont = null;
            this.menuFont = null;
            this.menuTagFont = null;
            this.menuBodyFont = null;
            this.menuNameFont = null;
            this.menuItemFont = null;
            this.initialized = false;
        }
    }

    private void initIfNeeded() {
        if (this.initialized) {
            return;
        }
        long windowHandle = class_310.method_1551().method_22683().method_4490();
        ImGui.createContext();
        ImGuiIO io2 = ImGui.getIO();
        io2.setIniFilename(iniPath());
        io2.addConfigFlags(1);
        ImGui.styleColorsDark();
        this.uiScale = computeScale(this.uiScale);
        loadFonts(io2);
        this.minecraftKeys = GLFW.glfwSetKeyCallback(windowHandle, (GLFWKeyCallbackI) null);
        GLFW.glfwSetKeyCallback(windowHandle, this.minecraftKeys);
        this.minecraftMonitors = GLFW.glfwSetMonitorCallback((GLFWMonitorCallbackI) null);
        GLFW.glfwSetMonitorCallback(this.minecraftMonitors);
        this.glfw.init(windowHandle, true);
        this.gl3.init(null);
        this.initialized = true;
    }

    private void rebuildFontsIfScaleChanged() {
        float scale = computeScale(this.uiScale);
        if (scale == this.uiScale) {
            return;
        }
        this.uiScale = scale;
        ImGuiIO io2 = ImGui.getIO();
        io2.getFonts().clear();
        loadFonts(io2);
        this.gl3.destroyFontsTexture();
        this.gl3.createFontsTexture();
    }

    private float computeScale(float current) {
        int height = class_310.method_1551().method_22683().method_4506();
        if (height < 200) {
            return current;
        }
        if (!this.initialized || !ImGui.getIO().getMouseDown(0)) {
            this.userScale = ThemeStore.get().uiScale.get();
        }
        float raw = Math.max(0.6f, Math.min(3.5f, (height / REFERENCE_HEIGHT) * this.userScale));
        return Math.round(raw * 20.0f) / 20.0f;
    }

    private static String iniPath() {
        try {
            Path dir = FabricLoader.getInstance().getConfigDir().resolve(AddonManager.ENTRYPOINT);
            Files.createDirectories(dir, new FileAttribute[0]);
            return dir.resolve("gui.ini").toString();
        } catch (IOException | RuntimeException e) {
            System.err.println("[Anubis] GUI layout will not be saved: " + e.getMessage());
            return null;
        }
    }

    private void updateDisplayMetrics() {
        class_310 client = class_310.method_1551();
        int w = Math.max(1, client.method_22683().method_4489());
        int h = Math.max(1, client.method_22683().method_4506());
        ImGuiIO io2 = ImGui.getIO();
        io2.setDisplaySize(w, h);
        io2.setDisplayFramebufferScale(1.0f, 1.0f);
    }

    private void loadFonts(ImGuiIO io2) {
        if (this.glyphRanges == null) {
            this.glyphRanges = buildGlyphRanges(io2);
        }
        try {
            io2.getFonts().setFreeTypeRenderer(true);
        } catch (Throwable t) {
            System.err.println("[Anubis] FreeType unavailable, using stb_truetype: " + t.getMessage());
        }
        float s = this.uiScale;
        this.moduleFont = loadFont(io2, "/assets/anubis/fonts/geist-medium.ttf", 16.0f * s, this.glyphRanges);
        this.categoryFont = loadFont(io2, "/assets/anubis/fonts/geist-semibold.ttf", 17.0f * s, this.glyphRanges);
        this.titleFont = loadFont(io2, "/assets/anubis/fonts/geist-semibold.ttf", 24.0f * s, this.glyphRanges);
        this.smallFont = loadFont(io2, "/assets/anubis/fonts/geist-medium.ttf", 13.0f * s, this.glyphRanges);
        this.hudValueFont = loadFont(io2, "/assets/anubis/fonts/geist-semibold.ttf", 18.0f * s, this.glyphRanges);
        this.hudTextFont = loadFont(io2, "/assets/anubis/fonts/geist-semibold.ttf", 17.0f * s, this.glyphRanges);
        this.hudLabelFont = loadFont(io2, "/assets/anubis/fonts/geist-medium.ttf", 14.0f * s, this.glyphRanges);
        this.menuFont = loadFont(io2, MENU_FONT_RESOURCE, 24.0f * s, this.glyphRanges);
        this.menuTagFont = loadFont(io2, MENU_FONT_RESOURCE, 15.0f * s, this.glyphRanges);
        this.menuBodyFont = loadFont(io2, "/assets/anubis/fonts/geist-medium.ttf", 18.0f * s, this.glyphRanges);
        this.menuNameFont = loadFont(io2, MENU_FONT_RESOURCE, 20.0f * s, this.glyphRanges);
        this.menuItemFont = loadFont(io2, MENU_FONT_RESOURCE, 32.0f * s, this.glyphRanges);
        if (this.moduleFont == null && this.categoryFont == null) {
            io2.getFonts().addFontDefault();
        } else if (this.moduleFont != null) {
            io2.setFontDefault(this.moduleFont);
        }
        if (!io2.getFonts().build()) {
            throw new IllegalStateException("ImGui font atlas could not be built");
        }
    }

    private static short[] buildGlyphRanges(ImGuiIO io2) {
        ImFontGlyphRangesBuilder builder = new ImFontGlyphRangesBuilder();
        builder.addRanges(io2.getFonts().getGlyphRangesDefault());
        builder.addRanges(io2.getFonts().getGlyphRangesVietnamese());
        builder.addText(EXTENDED_LATIN_GLYPHS);
        return builder.buildRanges();
    }

    private static ImFont loadFont(ImGuiIO io2, String resourcePath, float sizePixels, short[] ranges) {
        try {
            InputStream in = ImGuiManager.class.getResourceAsStream(resourcePath);
            try {
                if (in == null) {
                    System.err.println("[Anubis] Font resource not found: " + resourcePath);
                    if (in != null) {
                        in.close();
                    }
                    return null;
                }
                byte[] data = in.readAllBytes();
                ImFontConfig config = new ImFontConfig();
                try {
                    config.setOversampleH(3);
                    config.setOversampleV(1);
                    config.setPixelSnapH(true);
                    config.setFontBuilderFlags(8);
                    ImFont imFontAddFontFromMemoryTTF = io2.getFonts().addFontFromMemoryTTF(data, Math.round(sizePixels), config, ranges);
                    config.destroy();
                    if (in != null) {
                        in.close();
                    }
                    return imFontAddFontFromMemoryTTF;
                } catch (Throwable th) {
                    config.destroy();
                    throw th;
                }
            } catch (Throwable th2) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Throwable th3) {
                        th2.addSuppressed(th3);
                    }
                }
                throw th2;
            }
        } catch (IOException | RuntimeException e) {
            System.err.println("[Anubis] Failed to load font " + resourcePath + ": " + e.getMessage());
            return null;
        }
    }
}
