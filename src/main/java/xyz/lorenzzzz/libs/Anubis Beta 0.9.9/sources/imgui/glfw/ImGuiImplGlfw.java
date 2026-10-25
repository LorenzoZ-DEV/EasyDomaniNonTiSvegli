package imgui.glfw;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImGuiPlatformIO;
import imgui.ImGuiViewport;
import imgui.ImVec2;
import imgui.callback.ImPlatformFuncViewport;
import imgui.callback.ImPlatformFuncViewportFloat;
import imgui.callback.ImPlatformFuncViewportImVec2;
import imgui.callback.ImPlatformFuncViewportString;
import imgui.callback.ImPlatformFuncViewportSuppBoolean;
import imgui.callback.ImPlatformFuncViewportSuppImVec2;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.extension.imguizmo.flag.Operation;
import imgui.extension.implot.flag.ImPlotAxisFlags;
import imgui.flag.ImDrawFlags;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiKey;
import imgui.lwjgl3.glfw.ImGuiImplGlfwNative;
import imgui.type.ImString;
import java.nio.FloatBuffer;
import net.anubis.client.util.playerbypass.ChunkLightSnapshot;
import net.anubis.client.util.seedscan.SeedWorldgen;
import net.anubis.client.util.seedscan.VanillaBand;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWErrorCallbackI;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMonitorCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWNativeCocoa;
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryUtil;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/glfw/ImGuiImplGlfw.class */
public class ImGuiImplGlfw {
    protected static final String OS = System.getProperty("os.name", "generic").toLowerCase();
    protected static final boolean IS_WINDOWS = OS.contains("win");
    protected static final boolean IS_APPLE;
    protected Data data = null;
    private final Properties props = new Properties();
    protected static final int glfwVersionCombined = 3400;
    protected static final boolean glfwHawWindowTopmost = true;
    protected static final boolean glfwHasWindowHovered = true;
    protected static final boolean glfwHasWindowAlpha = true;
    protected static final boolean glfwHasPerMonitorDpi = true;
    protected static final boolean glfwHasFocusWindow = true;
    protected static final boolean glfwHasFocusOnShow = true;
    protected static final boolean glfwHasMonitorWorkArea = true;
    protected static final boolean glfwHasOsxWindowPosFix = true;
    protected static final boolean glfwHasNewCursors = true;
    protected static final boolean glfwHasMousePassthrough = true;
    protected static final boolean glfwHasGamepadApi = true;
    protected static final boolean glfwHasGetKeyName = true;
    protected static final boolean glfwHasGetError = true;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/glfw/ImGuiImplGlfw$MapAnalog.class */
    @FunctionalInterface
    private interface MapAnalog {
        void run(int i, int i2, int i3, float f, float f2);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/glfw/ImGuiImplGlfw$MapButton.class */
    @FunctionalInterface
    private interface MapButton {
        void run(int i, int i2, int i3);
    }

    static {
        IS_APPLE = OS.contains("mac") || OS.contains("darwin");
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/glfw/ImGuiImplGlfw$Data.class */
    protected static class Data {
        protected long window = -1;
        protected double time = 0.0d;
        protected long mouseWindow = -1;
        protected long[] mouseCursors = new long[9];
        protected ImVec2 lastValidMousePos = new ImVec2();
        protected long[] keyOwnerWindows = new long[348];
        protected boolean installedCallbacks = false;
        protected boolean callbacksChainForAllWindows = false;
        protected boolean wantUpdateMonitors = true;
        protected GLFWWindowFocusCallback prevUserCallbackWindowFocus = null;
        protected GLFWCursorPosCallback prevUserCallbackCursorPos = null;
        protected GLFWCursorEnterCallback prevUserCallbackCursorEnter = null;
        protected GLFWMouseButtonCallback prevUserCallbackMousebutton = null;
        protected GLFWScrollCallback prevUserCallbackScroll = null;
        protected GLFWKeyCallback prevUserCallbackKey = null;
        protected GLFWCharCallback prevUserCallbackChar = null;
        protected GLFWMonitorCallback prevUserCallbackMonitor = null;

        protected Data() {
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/glfw/ImGuiImplGlfw$Properties.class */
    private static final class Properties {
        private final int[] windowW;
        private final int[] windowH;
        private final int[] windowX;
        private final int[] windowY;
        private final int[] displayW;
        private final int[] displayH;
        private final ImVec2 mousePosPrev;
        private final double[] mouseX;
        private final double[] mouseY;
        private final int[] monitorX;
        private final int[] monitorY;
        private final int[] monitorWorkAreaX;
        private final int[] monitorWorkAreaY;
        private final int[] monitorWorkAreaWidth;
        private final int[] monitorWorkAreaHeight;
        private final float[] monitorContentScaleX;
        private final float[] monitorContentScaleY;
        private final String charNames = "`-=[]\\,;'./";
        private final int[] charKeys;

        private Properties() {
            this.windowW = new int[1];
            this.windowH = new int[1];
            this.windowX = new int[1];
            this.windowY = new int[1];
            this.displayW = new int[1];
            this.displayH = new int[1];
            this.mousePosPrev = new ImVec2();
            this.mouseX = new double[1];
            this.mouseY = new double[1];
            this.monitorX = new int[1];
            this.monitorY = new int[1];
            this.monitorWorkAreaX = new int[1];
            this.monitorWorkAreaY = new int[1];
            this.monitorWorkAreaWidth = new int[1];
            this.monitorWorkAreaHeight = new int[1];
            this.monitorContentScaleX = new float[1];
            this.monitorContentScaleY = new float[1];
            this.charNames = "`-=[]\\,;'./";
            this.charKeys = new int[]{96, 45, 61, 91, 93, 92, 44, 59, 39, 46, 47};
        }
    }

    protected ImStrSupplier getClipboardTextFn() {
        return new ImStrSupplier() { // from class: imgui.glfw.ImGuiImplGlfw.1
            @Override // imgui.callback.ImStrSupplier
            public String get() {
                String clipboardString = GLFW.glfwGetClipboardString(ImGuiImplGlfw.this.data.window);
                return clipboardString != null ? clipboardString : "";
            }
        };
    }

    protected ImStrConsumer setClipboardTextFn() {
        return new ImStrConsumer() { // from class: imgui.glfw.ImGuiImplGlfw.2
            @Override // imgui.callback.ImStrConsumer
            public void accept(String text) {
                GLFW.glfwSetClipboardString(ImGuiImplGlfw.this.data.window, text);
            }
        };
    }

    protected int glfwKeyToImGuiKey(int glfwKey) {
        switch (glfwKey) {
            case 32:
                return ImGuiKey.Space;
            case 33:
            case ImGuiCol.Tab /* 34 */:
            case 35:
            case 36:
            case 37:
            case 38:
            case 40:
            case 41:
            case ImGuiCol.PlotLines /* 42 */:
            case 43:
            case 58:
            case 60:
            case ChunkLightSnapshot.BASE_DETECT_CEILING_Y /* 62 */:
            case VanillaBand.MAX_Y /* 63 */:
            case 64:
            case 94:
            case 95:
            case 97:
            case 98:
            case 99:
            case ImString.DEFAULT_LENGTH /* 100 */:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
            case 114:
            case 115:
            case 116:
            case 117:
            case 118:
            case 119:
            case Operation.ROTATE /* 120 */:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 127:
            case 128:
            case 129:
            case 130:
            case 131:
            case 132:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 145:
            case 146:
            case 147:
            case 148:
            case 149:
            case 150:
            case 151:
            case 152:
            case 153:
            case ImGuiKey.NamedKey_COUNT /* 154 */:
            case 155:
            case 156:
            case 157:
            case 158:
            case 159:
            case ImDrawFlags.RoundCornersRight /* 160 */:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 167:
            case 168:
            case 169:
            case 170:
            case 171:
            case 172:
            case 173:
            case 174:
            case 175:
            case 176:
            case 177:
            case 178:
            case 179:
            case 180:
            case 181:
            case 182:
            case 183:
            case 184:
            case 185:
            case 186:
            case 187:
            case 188:
            case 189:
            case 190:
            case 191:
            case ImDrawFlags.RoundCornersBottom /* 192 */:
            case 193:
            case 194:
            case 195:
            case 196:
            case 197:
            case 198:
            case 199:
            case 200:
            case 201:
            case 202:
            case 203:
            case 204:
            case 205:
            case 206:
            case 207:
            case 208:
            case 209:
            case 210:
            case 211:
            case 212:
            case 213:
            case 214:
            case 215:
            case 216:
            case 217:
            case 218:
            case 219:
            case 220:
            case 221:
            case 222:
            case 223:
            case 224:
            case 225:
            case 226:
            case 227:
            case 228:
            case 229:
            case 230:
            case 231:
            case 232:
            case 233:
            case 234:
            case 235:
            case 236:
            case 237:
            case 238:
            case 239:
            case 240:
            case 241:
            case 242:
            case 243:
            case 244:
            case 245:
            case 246:
            case 247:
            case 248:
            case 249:
            case 250:
            case 251:
            case 252:
            case 253:
            case 254:
            case 255:
            case 270:
            case 271:
            case 272:
            case 273:
            case 274:
            case 275:
            case 276:
            case 277:
            case 278:
            case 279:
            case 285:
            case 286:
            case 287:
            case 288:
            case 289:
            case 314:
            case 315:
            case 316:
            case 317:
            case 318:
            case SeedWorldgen.MAX_Y /* 319 */:
            case 337:
            case 338:
            case 339:
            default:
                return 0;
            case ImGuiCol.TabDimmedSelectedOverline /* 39 */:
                return ImGuiKey.Apostrophe;
            case ImGuiCol.PlotHistogram /* 44 */:
                return ImGuiKey.Comma;
            case ImGuiCol.PlotHistogramHovered /* 45 */:
                return ImGuiKey.Minus;
            case ImGuiCol.TableHeaderBg /* 46 */:
                return ImGuiKey.Period;
            case ImGuiCol.TableBorderStrong /* 47 */:
                return ImGuiKey.Slash;
            case 48:
                return ImGuiKey._0;
            case ImGuiCol.TableRowBg /* 49 */:
                return ImGuiKey._1;
            case ImGuiCol.TableRowBgAlt /* 50 */:
                return ImGuiKey._2;
            case ImGuiCol.TextSelectedBg /* 51 */:
                return ImGuiKey._3;
            case ImGuiCol.DragDropTarget /* 52 */:
                return ImGuiKey._4;
            case ImGuiCol.NavHighlight /* 53 */:
                return ImGuiKey._5;
            case ImGuiCol.NavWindowingHighlight /* 54 */:
                return ImGuiKey._6;
            case 55:
                return ImGuiKey._7;
            case ImGuiCol.ModalWindowDimBg /* 56 */:
                return ImGuiKey._8;
            case ImGuiCol.COUNT /* 57 */:
                return ImGuiKey._9;
            case 59:
                return ImGuiKey.Semicolon;
            case 61:
                return ImGuiKey.Equal;
            case 65:
                return ImGuiKey.A;
            case 66:
                return ImGuiKey.B;
            case 67:
                return ImGuiKey.C;
            case 68:
                return ImGuiKey.D;
            case 69:
                return ImGuiKey.E;
            case 70:
                return ImGuiKey.F;
            case 71:
                return ImGuiKey.G;
            case 72:
                return ImGuiKey.H;
            case 73:
                return ImGuiKey.I;
            case 74:
                return ImGuiKey.J;
            case 75:
                return ImGuiKey.K;
            case 76:
                return ImGuiKey.L;
            case 77:
                return ImGuiKey.M;
            case 78:
                return ImGuiKey.N;
            case 79:
                return ImGuiKey.O;
            case 80:
                return ImGuiKey.P;
            case 81:
                return ImGuiKey.Q;
            case 82:
                return ImGuiKey.R;
            case 83:
                return ImGuiKey.S;
            case 84:
                return ImGuiKey.T;
            case 85:
                return ImGuiKey.U;
            case 86:
                return ImGuiKey.V;
            case 87:
                return ImGuiKey.W;
            case 88:
                return ImGuiKey.X;
            case 89:
                return ImGuiKey.Y;
            case 90:
                return ImGuiKey.Z;
            case 91:
                return ImGuiKey.LeftBracket;
            case 92:
                return ImGuiKey.Backslash;
            case 93:
                return ImGuiKey.RightBracket;
            case 96:
                return ImGuiKey.GraveAccent;
            case 256:
                return ImGuiKey.Escape;
            case 257:
                return ImGuiKey.Enter;
            case ImPlotAxisFlags.AuxDefault /* 258 */:
                return 512;
            case 259:
                return ImGuiKey.Backspace;
            case 260:
                return ImGuiKey.Insert;
            case 261:
                return ImGuiKey.Delete;
            case 262:
                return ImGuiKey.RightArrow;
            case 263:
                return ImGuiKey.LeftArrow;
            case 264:
                return ImGuiKey.DownArrow;
            case 265:
                return ImGuiKey.UpArrow;
            case 266:
                return ImGuiKey.PageUp;
            case 267:
                return ImGuiKey.PageDown;
            case 268:
                return ImGuiKey.Home;
            case 269:
                return ImGuiKey.End;
            case 280:
                return ImGuiKey.CapsLock;
            case 281:
                return ImGuiKey.ScrollLock;
            case 282:
                return ImGuiKey.NumLock;
            case 283:
                return ImGuiKey.PrintScreen;
            case 284:
                return ImGuiKey.Pause;
            case 290:
                return ImGuiKey.F1;
            case 291:
                return ImGuiKey.F2;
            case 292:
                return ImGuiKey.F3;
            case 293:
                return ImGuiKey.F4;
            case 294:
                return ImGuiKey.F5;
            case 295:
                return ImGuiKey.F6;
            case 296:
                return ImGuiKey.F7;
            case 297:
                return ImGuiKey.F8;
            case 298:
                return ImGuiKey.F9;
            case 299:
                return ImGuiKey.F10;
            case 300:
                return ImGuiKey.F11;
            case 301:
                return ImGuiKey.F12;
            case 302:
                return ImGuiKey.F13;
            case 303:
                return ImGuiKey.F14;
            case 304:
                return ImGuiKey.F15;
            case 305:
                return ImGuiKey.F16;
            case 306:
                return ImGuiKey.F17;
            case 307:
                return ImGuiKey.F18;
            case 308:
                return ImGuiKey.F19;
            case 309:
                return ImGuiKey.F20;
            case 310:
                return ImGuiKey.F21;
            case 311:
                return ImGuiKey.F22;
            case 312:
                return ImGuiKey.F23;
            case 313:
                return ImGuiKey.F24;
            case 320:
                return ImGuiKey.Keypad0;
            case 321:
                return ImGuiKey.Keypad1;
            case 322:
                return ImGuiKey.Keypad2;
            case 323:
                return ImGuiKey.Keypad3;
            case 324:
                return ImGuiKey.Keypad4;
            case 325:
                return ImGuiKey.Keypad5;
            case 326:
                return ImGuiKey.Keypad6;
            case 327:
                return ImGuiKey.Keypad7;
            case 328:
                return ImGuiKey.Keypad8;
            case 329:
                return ImGuiKey.Keypad9;
            case 330:
                return ImGuiKey.KeypadDecimal;
            case 331:
                return ImGuiKey.KeypadDivide;
            case 332:
                return ImGuiKey.KeypadMultiply;
            case 333:
                return ImGuiKey.KeypadSubtract;
            case 334:
                return ImGuiKey.KeypadAdd;
            case 335:
                return ImGuiKey.KeypadEnter;
            case 336:
                return ImGuiKey.KeypadEqual;
            case 340:
                return ImGuiKey.LeftShift;
            case 341:
                return ImGuiKey.LeftCtrl;
            case 342:
                return ImGuiKey.LeftAlt;
            case 343:
                return ImGuiKey.LeftSuper;
            case 344:
                return ImGuiKey.RightShift;
            case 345:
                return ImGuiKey.RightCtrl;
            case 346:
                return ImGuiKey.RightAlt;
            case 347:
                return ImGuiKey.RightSuper;
            case 348:
                return ImGuiKey.Menu;
        }
    }

    protected void updateKeyModifiers(long window) {
        ImGuiIO io2 = ImGui.getIO();
        io2.addKeyEvent(4096, GLFW.glfwGetKey(window, 341) == 1 || GLFW.glfwGetKey(window, 345) == 1);
        io2.addKeyEvent(8192, GLFW.glfwGetKey(window, 340) == 1 || GLFW.glfwGetKey(window, 344) == 1);
        io2.addKeyEvent(16384, GLFW.glfwGetKey(window, 342) == 1 || GLFW.glfwGetKey(window, 346) == 1);
        io2.addKeyEvent(32768, GLFW.glfwGetKey(window, 343) == 1 || GLFW.glfwGetKey(window, 347) == 1);
    }

    protected boolean shouldChainCallback(long window) {
        return this.data.callbacksChainForAllWindows || window == this.data.window;
    }

    public void mouseButtonCallback(long window, int button, int action, int mods) {
        if (this.data.prevUserCallbackMousebutton != null && shouldChainCallback(window)) {
            this.data.prevUserCallbackMousebutton.invoke(window, button, action, mods);
        }
        updateKeyModifiers(window);
        ImGuiIO io2 = ImGui.getIO();
        if (button >= 0 && button < 5) {
            io2.addMouseButtonEvent(button, action == 1);
        }
    }

    public void scrollCallback(long window, double xOffset, double yOffset) {
        if (this.data.prevUserCallbackScroll != null && shouldChainCallback(window)) {
            this.data.prevUserCallbackScroll.invoke(window, xOffset, yOffset);
        }
        ImGuiIO io2 = ImGui.getIO();
        io2.addMouseWheelEvent((float) xOffset, (float) yOffset);
    }

    protected int translateUntranslatedKey(int key, int scancode) {
        if (key >= 320 && key <= 336) {
            return key;
        }
        int resultKey = key;
        String keyName = GLFW.glfwGetKeyName(key, scancode);
        eatErrors();
        if (keyName != null && keyName.length() > 2 && keyName.charAt(0) != 0 && keyName.charAt(1) == 0) {
            if (keyName.charAt(0) >= '0' && keyName.charAt(0) <= '9') {
                resultKey = 48 + (keyName.charAt(0) - '0');
            } else if (keyName.charAt(0) >= 'A' && keyName.charAt(0) <= 'Z') {
                resultKey = 65 + (keyName.charAt(0) - 'A');
            } else if (keyName.charAt(0) >= 'a' && keyName.charAt(0) <= 'z') {
                resultKey = 65 + (keyName.charAt(0) - 'a');
            } else {
                int index = "`-=[]\\,;'./".indexOf(keyName.charAt(0));
                if (index != -1) {
                    resultKey = this.props.charKeys[index];
                }
            }
        }
        return resultKey;
    }

    protected void eatErrors() {
        PointerBuffer pb = MemoryUtil.memAllocPointer(1);
        GLFW.glfwGetError(pb);
        MemoryUtil.memFree(pb);
    }

    public void keyCallback(long window, int keycode, int scancode, int action, int mods) {
        if (this.data.prevUserCallbackKey != null && shouldChainCallback(window)) {
            this.data.prevUserCallbackKey.invoke(window, keycode, scancode, action, mods);
        }
        if (action != 1 && action != 0) {
            return;
        }
        updateKeyModifiers(window);
        if (keycode >= 0 && keycode < this.data.keyOwnerWindows.length) {
            this.data.keyOwnerWindows[keycode] = action == 1 ? window : -1L;
        }
        int key = translateUntranslatedKey(keycode, scancode);
        ImGuiIO io2 = ImGui.getIO();
        int imguiKey = glfwKeyToImGuiKey(key);
        io2.addKeyEvent(imguiKey, action == 1);
        io2.setKeyEventNativeData(imguiKey, key, scancode);
    }

    public void windowFocusCallback(long window, boolean focused) {
        if (this.data.prevUserCallbackWindowFocus != null && shouldChainCallback(window)) {
            this.data.prevUserCallbackWindowFocus.invoke(window, focused);
        }
        ImGui.getIO().addFocusEvent(focused);
    }

    public void cursorPosCallback(long window, double x, double y) {
        if (this.data.prevUserCallbackCursorPos != null && shouldChainCallback(window)) {
            this.data.prevUserCallbackCursorPos.invoke(window, x, y);
        }
        float posX = (float) x;
        float posY = (float) y;
        ImGuiIO io2 = ImGui.getIO();
        if (io2.hasConfigFlags(1024)) {
            GLFW.glfwGetWindowPos(window, this.props.windowX, this.props.windowY);
            posX += this.props.windowX[0];
            posY += this.props.windowY[0];
        }
        io2.addMousePosEvent(posX, posY);
        this.data.lastValidMousePos.set(posX, posY);
    }

    public void cursorEnterCallback(long window, boolean entered) {
        if (this.data.prevUserCallbackCursorEnter != null && shouldChainCallback(window)) {
            this.data.prevUserCallbackCursorEnter.invoke(window, entered);
        }
        ImGuiIO io2 = ImGui.getIO();
        if (entered) {
            this.data.mouseWindow = window;
            io2.addMousePosEvent(this.data.lastValidMousePos.x, this.data.lastValidMousePos.y);
        } else if (this.data.mouseWindow == window) {
            io2.getMousePos(this.data.lastValidMousePos);
            this.data.mouseWindow = -1L;
            io2.addMousePosEvent(Float.MIN_VALUE, Float.MIN_VALUE);
        }
    }

    public void charCallback(long window, int c) {
        if (this.data.prevUserCallbackChar != null && shouldChainCallback(window)) {
            this.data.prevUserCallbackChar.invoke(window, c);
        }
        ImGui.getIO().addInputCharacter(c);
    }

    public void monitorCallback(long window, int event) {
        this.data.wantUpdateMonitors = true;
    }

    public void installCallbacks(long window) {
        this.data.prevUserCallbackWindowFocus = GLFW.glfwSetWindowFocusCallback(window, this::windowFocusCallback);
        this.data.prevUserCallbackCursorEnter = GLFW.glfwSetCursorEnterCallback(window, this::cursorEnterCallback);
        this.data.prevUserCallbackCursorPos = GLFW.glfwSetCursorPosCallback(window, this::cursorPosCallback);
        this.data.prevUserCallbackMousebutton = GLFW.glfwSetMouseButtonCallback(window, this::mouseButtonCallback);
        this.data.prevUserCallbackScroll = GLFW.glfwSetScrollCallback(window, this::scrollCallback);
        this.data.prevUserCallbackKey = GLFW.glfwSetKeyCallback(window, this::keyCallback);
        this.data.prevUserCallbackChar = GLFW.glfwSetCharCallback(window, this::charCallback);
        this.data.prevUserCallbackMonitor = GLFW.glfwSetMonitorCallback(this::monitorCallback);
        this.data.installedCallbacks = true;
    }

    protected void freeCallback(Callback cb) {
        if (cb != null) {
            cb.free();
        }
    }

    public void restoreCallbacks(long window) {
        freeCallback(GLFW.glfwSetWindowFocusCallback(window, this.data.prevUserCallbackWindowFocus));
        freeCallback(GLFW.glfwSetCursorEnterCallback(window, this.data.prevUserCallbackCursorEnter));
        freeCallback(GLFW.glfwSetCursorPosCallback(window, this.data.prevUserCallbackCursorPos));
        freeCallback(GLFW.glfwSetMouseButtonCallback(window, this.data.prevUserCallbackMousebutton));
        freeCallback(GLFW.glfwSetScrollCallback(window, this.data.prevUserCallbackScroll));
        freeCallback(GLFW.glfwSetKeyCallback(window, this.data.prevUserCallbackKey));
        freeCallback(GLFW.glfwSetCharCallback(window, this.data.prevUserCallbackChar));
        freeCallback(GLFW.glfwSetMonitorCallback(this.data.prevUserCallbackMonitor));
        this.data.installedCallbacks = false;
        this.data.prevUserCallbackWindowFocus = null;
        this.data.prevUserCallbackCursorEnter = null;
        this.data.prevUserCallbackCursorPos = null;
        this.data.prevUserCallbackMousebutton = null;
        this.data.prevUserCallbackScroll = null;
        this.data.prevUserCallbackKey = null;
        this.data.prevUserCallbackChar = null;
        this.data.prevUserCallbackMonitor = null;
    }

    public void setCallbacksChainForAllWindows(boolean chainForAllWindows) {
        this.data.callbacksChainForAllWindows = chainForAllWindows;
    }

    protected Data newData() {
        return new Data();
    }

    public boolean init(long window, boolean installCallbacks) {
        ImGuiIO io2 = ImGui.getIO();
        io2.setBackendPlatformName("imgui-java_impl_glfw");
        io2.addBackendFlags(1030);
        io2.addBackendFlags(2048);
        this.data = newData();
        this.data.window = window;
        this.data.time = 0.0d;
        this.data.wantUpdateMonitors = true;
        io2.setGetClipboardTextFn(getClipboardTextFn());
        io2.setSetClipboardTextFn(setClipboardTextFn());
        GLFWErrorCallback prevErrorCallback = GLFW.glfwSetErrorCallback((GLFWErrorCallbackI) null);
        this.data.mouseCursors[0] = GLFW.glfwCreateStandardCursor(221185);
        this.data.mouseCursors[1] = GLFW.glfwCreateStandardCursor(221186);
        this.data.mouseCursors[3] = GLFW.glfwCreateStandardCursor(221190);
        this.data.mouseCursors[4] = GLFW.glfwCreateStandardCursor(221189);
        this.data.mouseCursors[7] = GLFW.glfwCreateStandardCursor(221188);
        this.data.mouseCursors[2] = GLFW.glfwCreateStandardCursor(221193);
        this.data.mouseCursors[5] = GLFW.glfwCreateStandardCursor(221192);
        this.data.mouseCursors[6] = GLFW.glfwCreateStandardCursor(221191);
        this.data.mouseCursors[8] = GLFW.glfwCreateStandardCursor(221194);
        GLFW.glfwSetErrorCallback(prevErrorCallback);
        eatErrors();
        if (installCallbacks) {
            installCallbacks(window);
        }
        updateMonitors();
        GLFW.glfwSetMonitorCallback(this::monitorCallback);
        ImGuiViewport mainViewport = ImGui.getMainViewport();
        mainViewport.setPlatformHandle(window);
        if (IS_WINDOWS) {
            mainViewport.setPlatformHandleRaw(GLFWNativeWin32.glfwGetWin32Window(window));
        }
        if (IS_APPLE) {
            mainViewport.setPlatformHandleRaw(GLFWNativeCocoa.glfwGetCocoaWindow(window));
        }
        if (io2.hasConfigFlags(1024)) {
            initPlatformInterface();
            return true;
        }
        return true;
    }

    public void shutdown() {
        ImGuiIO io2 = ImGui.getIO();
        shutdownPlatformInterface();
        if (this.data.installedCallbacks) {
            restoreCallbacks(this.data.window);
        }
        for (int cursorN = 0; cursorN < 9; cursorN++) {
            GLFW.glfwDestroyCursor(this.data.mouseCursors[cursorN]);
        }
        io2.setBackendPlatformName(null);
        this.data = null;
        io2.removeBackendFlags(3079);
    }

    protected void updateMouseData() {
        ImGuiIO io2 = ImGui.getIO();
        ImGuiPlatformIO platformIO = ImGui.getPlatformIO();
        int mouseViewportId = 0;
        io2.getMousePos(this.props.mousePosPrev);
        for (int n = 0; n < platformIO.getViewportsSize(); n++) {
            ImGuiViewport viewport = platformIO.getViewports(n);
            long window = viewport.getPlatformHandle();
            boolean isWindowFocused = GLFW.glfwGetWindowAttrib(window, 131073) != 0;
            if (isWindowFocused) {
                if (io2.getWantSetMousePos()) {
                    GLFW.glfwSetCursorPos(window, this.props.mousePosPrev.x - viewport.getPosX(), this.props.mousePosPrev.y - viewport.getPosY());
                }
                if (this.data.mouseWindow == -1) {
                    GLFW.glfwGetCursorPos(window, this.props.mouseX, this.props.mouseY);
                    double mouseX = this.props.mouseX[0];
                    double mouseY = this.props.mouseY[0];
                    if (io2.hasConfigFlags(1024)) {
                        GLFW.glfwGetWindowPos(window, this.props.windowX, this.props.windowY);
                        mouseX += (double) this.props.windowX[0];
                        mouseY += (double) this.props.windowY[0];
                    }
                    this.data.lastValidMousePos.set((float) mouseX, (float) mouseY);
                    io2.addMousePosEvent((float) mouseX, (float) mouseY);
                }
            }
            boolean windowNoInput = viewport.hasFlags(128);
            GLFW.glfwSetWindowAttrib(window, 131085, windowNoInput ? 1 : 0);
            if (GLFW.glfwGetWindowAttrib(window, 131083) == 1 && !windowNoInput) {
                mouseViewportId = viewport.getID();
            }
        }
        if (io2.hasBackendFlags(2048)) {
            io2.addMouseViewportEvent(mouseViewportId);
        }
    }

    protected void updateMouseCursor() {
        ImGuiIO io2 = ImGui.getIO();
        if (io2.hasConfigFlags(32) || GLFW.glfwGetInputMode(this.data.window, 208897) == 212995) {
            return;
        }
        int imguiCursor = ImGui.getMouseCursor();
        ImGuiPlatformIO platformIO = ImGui.getPlatformIO();
        for (int n = 0; n < platformIO.getViewportsSize(); n++) {
            long windowPtr = platformIO.getViewports(n).getPlatformHandle();
            if (imguiCursor == -1 || io2.getMouseDrawCursor()) {
                GLFW.glfwSetInputMode(windowPtr, 208897, 212994);
            } else {
                GLFW.glfwSetCursor(windowPtr, this.data.mouseCursors[imguiCursor] != 0 ? this.data.mouseCursors[imguiCursor] : this.data.mouseCursors[0]);
                GLFW.glfwSetInputMode(windowPtr, 208897, 212993);
            }
        }
    }

    private float saturate(float v) {
        if (v < 0.0f) {
            return 0.0f;
        }
        if (v > 1.0f) {
            return 1.0f;
        }
        return v;
    }

    /* JADX WARN: Bottom block not found for handler: all -> 0x0089 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void updateGamepads() {
        /*
            Method dump skipped, instruction units count: 497
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: imgui.glfw.ImGuiImplGlfw.updateGamepads():void");
    }

    private /* synthetic */ void lambda$updateGamepads$3(FloatBuffer axes, ImGuiIO io2, int keyNo, int axisNo, int _unused, float v0, float v1) {
        float v = ((axes.limit() > axisNo ? axes.get(axisNo) : v0) - v0) / (v1 - v0);
        io2.addKeyAnalogEvent(keyNo, v > 0.1f, saturate(v));
    }

    protected void updateMonitors() {
        ImGuiPlatformIO platformIO = ImGui.getPlatformIO();
        this.data.wantUpdateMonitors = false;
        PointerBuffer monitors = GLFW.glfwGetMonitors();
        if (monitors == null) {
            System.err.println("Unable to get monitors!");
            return;
        }
        if (monitors.limit() == 0) {
            return;
        }
        platformIO.resizeMonitors(0);
        for (int n = 0; n < monitors.limit(); n++) {
            long monitor = monitors.get(n);
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(monitor);
            if (vidMode != null) {
                GLFW.glfwGetMonitorPos(monitor, this.props.monitorX, this.props.monitorY);
                float mainPosX = this.props.monitorX[0];
                float mainPosY = this.props.monitorY[0];
                float mainSizeX = vidMode.width();
                float mainSizeY = vidMode.height();
                float workPosX = 0.0f;
                float workPosY = 0.0f;
                float workSizeX = 0.0f;
                float workSizeY = 0.0f;
                GLFW.glfwGetMonitorWorkarea(monitor, this.props.monitorWorkAreaX, this.props.monitorWorkAreaY, this.props.monitorWorkAreaWidth, this.props.monitorWorkAreaHeight);
                if (this.props.monitorWorkAreaWidth[0] > 0 && this.props.monitorWorkAreaHeight[0] > 0) {
                    workPosX = this.props.monitorWorkAreaX[0];
                    workPosY = this.props.monitorWorkAreaY[0];
                    workSizeX = this.props.monitorWorkAreaWidth[0];
                    workSizeY = this.props.monitorWorkAreaHeight[0];
                }
                GLFW.glfwGetMonitorContentScale(monitor, this.props.monitorContentScaleX, this.props.monitorContentScaleY);
                float dpiScale = this.props.monitorContentScaleX[0];
                platformIO.pushMonitors(monitor, mainPosX, mainPosY, mainSizeX, mainSizeY, workPosX, workPosY, workSizeX, workSizeY, dpiScale);
            }
        }
    }

    public void newFrame() {
        ImGuiIO io2 = ImGui.getIO();
        GLFW.glfwGetWindowSize(this.data.window, this.props.windowW, this.props.windowH);
        GLFW.glfwGetFramebufferSize(this.data.window, this.props.displayW, this.props.displayH);
        io2.setDisplaySize(this.props.windowW[0], this.props.windowH[0]);
        if (this.props.windowW[0] > 0 && this.props.windowH[0] > 0) {
            float scaleX = this.props.displayW[0] / this.props.windowW[0];
            float scaleY = this.props.displayH[0] / this.props.windowH[0];
            io2.setDisplayFramebufferScale(scaleX, scaleY);
        }
        if (this.data.wantUpdateMonitors) {
            updateMonitors();
        }
        double currentTime = GLFW.glfwGetTime();
        if (currentTime <= this.data.time) {
            currentTime = this.data.time + 9.999999747378752E-6d;
        }
        io2.setDeltaTime(this.data.time > 0.0d ? (float) (currentTime - this.data.time) : 0.016666668f);
        this.data.time = currentTime;
        updateMouseData();
        updateMouseCursor();
        updateGamepads();
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/glfw/ImGuiImplGlfw$ViewportData.class */
    private static final class ViewportData {
        long window;
        boolean windowOwned;
        int ignoreWindowPosEventFrame;
        int ignoreWindowSizeEventFrame;

        private ViewportData() {
            this.window = -1L;
            this.windowOwned = false;
            this.ignoreWindowPosEventFrame = -1;
            this.ignoreWindowSizeEventFrame = -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void windowCloseCallback(long windowId) {
        ImGuiViewport vp = ImGui.findViewportByPlatformHandle(windowId);
        if (vp.isValidPtr()) {
            vp.setPlatformRequestClose(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void windowPosCallback(long windowId, int xPos, int yPos) {
        ImGuiViewport vp = ImGui.findViewportByPlatformHandle(windowId);
        if (vp.isNotValidPtr()) {
            return;
        }
        ViewportData vd = (ViewportData) vp.getPlatformUserData();
        if (vd != null) {
            boolean ignoreEvent = ImGui.getFrameCount() <= vd.ignoreWindowPosEventFrame + 1;
            if (ignoreEvent) {
                return;
            }
        }
        vp.setPlatformRequestMove(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void windowSizeCallback(long windowId, int width, int height) {
        ImGuiViewport vp = ImGui.findViewportByPlatformHandle(windowId);
        if (vp.isNotValidPtr()) {
            return;
        }
        ViewportData vd = (ViewportData) vp.getPlatformUserData();
        if (vd != null) {
            boolean ignoreEvent = ImGui.getFrameCount() <= vd.ignoreWindowSizeEventFrame + 1;
            if (ignoreEvent) {
                return;
            }
        }
        vp.setPlatformRequestResize(true);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/glfw/ImGuiImplGlfw$CreateWindowFunction.class */
    private final class CreateWindowFunction extends ImPlatformFuncViewport {
        private CreateWindowFunction() {
        }

        @Override // imgui.callback.ImPlatformFuncViewport
        public void accept(ImGuiViewport vp) {
            ViewportData vd = new ViewportData();
            vp.setPlatformUserData(vd);
            GLFW.glfwWindowHint(131076, 0);
            GLFW.glfwWindowHint(131073, 0);
            GLFW.glfwWindowHint(131084, 0);
            GLFW.glfwWindowHint(131077, vp.hasFlags(8) ? 0 : 1);
            GLFW.glfwWindowHint(131079, vp.hasFlags(1024) ? 1 : 0);
            vd.window = GLFW.glfwCreateWindow((int) vp.getSizeX(), (int) vp.getSizeY(), "No Title Yet", 0L, ImGuiImplGlfw.this.data.window);
            vd.windowOwned = true;
            vp.setPlatformHandle(vd.window);
            if (ImGuiImplGlfw.IS_WINDOWS) {
                vp.setPlatformHandleRaw(GLFWNativeWin32.glfwGetWin32Window(vd.window));
            } else if (ImGuiImplGlfw.IS_APPLE) {
                vp.setPlatformHandleRaw(GLFWNativeCocoa.glfwGetCocoaWindow(vd.window));
            }
            GLFW.glfwSetWindowPos(vd.window, (int) vp.getPosX(), (int) vp.getPosY());
            long j = vd.window;
            ImGuiImplGlfw imGuiImplGlfw = ImGuiImplGlfw.this;
            GLFW.glfwSetWindowFocusCallback(j, imGuiImplGlfw::windowFocusCallback);
            long j2 = vd.window;
            ImGuiImplGlfw imGuiImplGlfw2 = ImGuiImplGlfw.this;
            GLFW.glfwSetCursorEnterCallback(j2, imGuiImplGlfw2::cursorEnterCallback);
            long j3 = vd.window;
            ImGuiImplGlfw imGuiImplGlfw3 = ImGuiImplGlfw.this;
            GLFW.glfwSetCursorPosCallback(j3, imGuiImplGlfw3::cursorPosCallback);
            long j4 = vd.window;
            ImGuiImplGlfw imGuiImplGlfw4 = ImGuiImplGlfw.this;
            GLFW.glfwSetMouseButtonCallback(j4, imGuiImplGlfw4::mouseButtonCallback);
            long j5 = vd.window;
            ImGuiImplGlfw imGuiImplGlfw5 = ImGuiImplGlfw.this;
            GLFW.glfwSetScrollCallback(j5, imGuiImplGlfw5::scrollCallback);
            long j6 = vd.window;
            ImGuiImplGlfw imGuiImplGlfw6 = ImGuiImplGlfw.this;
            GLFW.glfwSetKeyCallback(j6, imGuiImplGlfw6::keyCallback);
            long j7 = vd.window;
            ImGuiImplGlfw imGuiImplGlfw7 = ImGuiImplGlfw.this;
            GLFW.glfwSetCharCallback(j7, imGuiImplGlfw7::charCallback);
            long j8 = vd.window;
            ImGuiImplGlfw imGuiImplGlfw8 = ImGuiImplGlfw.this;
            GLFW.glfwSetWindowCloseCallback(j8, x$0 -> {
                imGuiImplGlfw8.windowCloseCallback(x$0);
            });
            long j9 = vd.window;
            ImGuiImplGlfw imGuiImplGlfw9 = ImGuiImplGlfw.this;
            GLFW.glfwSetWindowPosCallback(j9, (x$1, x$2, x$3) -> {
                imGuiImplGlfw9.windowPosCallback(x$1, x$2, x$3);
            });
            long j10 = vd.window;
            ImGuiImplGlfw imGuiImplGlfw10 = ImGuiImplGlfw.this;
            GLFW.glfwSetWindowSizeCallback(j10, (x$4, x$5, x$6) -> {
                imGuiImplGlfw10.windowSizeCallback(x$4, x$5, x$6);
            });
            GLFW.glfwMakeContextCurrent(vd.window);
            GLFW.glfwSwapInterval(0);
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/glfw/ImGuiImplGlfw$DestroyWindowFunction.class */
    private final class DestroyWindowFunction extends ImPlatformFuncViewport {
        private DestroyWindowFunction() {
        }

        @Override // imgui.callback.ImPlatformFuncViewport
        public void accept(ImGuiViewport vp) {
            ViewportData vd = (ViewportData) vp.getPlatformUserData();
            if (vd != null && vd.windowOwned) {
                for (int i = 0; i < ImGuiImplGlfw.this.data.keyOwnerWindows.length; i++) {
                    if (ImGuiImplGlfw.this.data.keyOwnerWindows[i] == vd.window) {
                        ImGuiImplGlfw.this.keyCallback(vd.window, i, 0, 0, 0);
                    }
                }
                Callbacks.glfwFreeCallbacks(vd.window);
                GLFW.glfwDestroyWindow(vd.window);
                vd.window = -1L;
            }
            vp.setPlatformHandle(-1L);
            vp.setPlatformUserData(null);
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/glfw/ImGuiImplGlfw$ShowWindowFunction.class */
    private static final class ShowWindowFunction extends ImPlatformFuncViewport {
        private ShowWindowFunction() {
        }

        @Override // imgui.callback.ImPlatformFuncViewport
        public void accept(ImGuiViewport vp) {
            ViewportData vd = (ViewportData) vp.getPlatformUserData();
            if (vd == null) {
                return;
            }
            if (ImGuiImplGlfw.IS_WINDOWS && vp.hasFlags(16)) {
                ImGuiImplGlfwNative.win32hideFromTaskBar(vp.getPlatformHandleRaw());
            }
            GLFW.glfwShowWindow(vd.window);
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/glfw/ImGuiImplGlfw$GetWindowPosFunction.class */
    private static final class GetWindowPosFunction extends ImPlatformFuncViewportSuppImVec2 {
        private final int[] posX;
        private final int[] posY;

        private GetWindowPosFunction() {
            this.posX = new int[1];
            this.posY = new int[1];
        }

        @Override // imgui.callback.ImPlatformFuncViewportSuppImVec2
        public void get(ImGuiViewport vp, ImVec2 dst) {
            ViewportData vd = (ViewportData) vp.getPlatformUserData();
            if (vd == null) {
                return;
            }
            this.posX[0] = 0;
            this.posY[0] = 0;
            GLFW.glfwGetWindowPos(vd.window, this.posX, this.posY);
            dst.set(this.posX[0], this.posY[0]);
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/glfw/ImGuiImplGlfw$SetWindowPosFunction.class */
    private static final class SetWindowPosFunction extends ImPlatformFuncViewportImVec2 {
        private SetWindowPosFunction() {
        }

        @Override // imgui.callback.ImPlatformFuncViewportImVec2
        public void accept(ImGuiViewport vp, ImVec2 value) {
            ViewportData vd = (ViewportData) vp.getPlatformUserData();
            if (vd == null) {
                return;
            }
            vd.ignoreWindowPosEventFrame = ImGui.getFrameCount();
            GLFW.glfwSetWindowPos(vd.window, (int) value.x, (int) value.y);
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/glfw/ImGuiImplGlfw$GetWindowSizeFunction.class */
    private static final class GetWindowSizeFunction extends ImPlatformFuncViewportSuppImVec2 {
        private final int[] width;
        private final int[] height;

        private GetWindowSizeFunction() {
            this.width = new int[1];
            this.height = new int[1];
        }

        @Override // imgui.callback.ImPlatformFuncViewportSuppImVec2
        public void get(ImGuiViewport vp, ImVec2 dst) {
            ViewportData vd = (ViewportData) vp.getPlatformUserData();
            if (vd == null) {
                return;
            }
            this.width[0] = 0;
            this.height[0] = 0;
            GLFW.glfwGetWindowSize(vd.window, this.width, this.height);
            dst.x = this.width[0];
            dst.y = this.height[0];
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/glfw/ImGuiImplGlfw$SetWindowSizeFunction.class */
    private final class SetWindowSizeFunction extends ImPlatformFuncViewportImVec2 {
        private final int[] x;
        private final int[] y;
        private final int[] width;
        private final int[] height;

        private SetWindowSizeFunction() {
            this.x = new int[1];
            this.y = new int[1];
            this.width = new int[1];
            this.height = new int[1];
        }

        @Override // imgui.callback.ImPlatformFuncViewportImVec2
        public void accept(ImGuiViewport vp, ImVec2 value) {
            ViewportData vd = (ViewportData) vp.getPlatformUserData();
            if (vd == null) {
                return;
            }
            if (ImGuiImplGlfw.IS_APPLE) {
            }
            vd.ignoreWindowSizeEventFrame = ImGui.getFrameCount();
            GLFW.glfwSetWindowSize(vd.window, (int) value.x, (int) value.y);
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/glfw/ImGuiImplGlfw$SetWindowTitleFunction.class */
    private static final class SetWindowTitleFunction extends ImPlatformFuncViewportString {
        private SetWindowTitleFunction() {
        }

        @Override // imgui.callback.ImPlatformFuncViewportString
        public void accept(ImGuiViewport vp, String value) {
            ViewportData vd = (ViewportData) vp.getPlatformUserData();
            if (vd != null) {
                GLFW.glfwSetWindowTitle(vd.window, value);
            }
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/glfw/ImGuiImplGlfw$SetWindowFocusFunction.class */
    private final class SetWindowFocusFunction extends ImPlatformFuncViewport {
        private SetWindowFocusFunction() {
        }

        @Override // imgui.callback.ImPlatformFuncViewport
        public void accept(ImGuiViewport vp) {
            ViewportData vd = (ViewportData) vp.getPlatformUserData();
            if (vd != null) {
                GLFW.glfwFocusWindow(vd.window);
            }
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/glfw/ImGuiImplGlfw$GetWindowFocusFunction.class */
    private static final class GetWindowFocusFunction extends ImPlatformFuncViewportSuppBoolean {
        private GetWindowFocusFunction() {
        }

        @Override // imgui.callback.ImPlatformFuncViewportSuppBoolean
        public boolean get(ImGuiViewport vp) {
            ViewportData data = (ViewportData) vp.getPlatformUserData();
            return GLFW.glfwGetWindowAttrib(data.window, 131073) != 0;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/glfw/ImGuiImplGlfw$GetWindowMinimizedFunction.class */
    private static final class GetWindowMinimizedFunction extends ImPlatformFuncViewportSuppBoolean {
        private GetWindowMinimizedFunction() {
        }

        @Override // imgui.callback.ImPlatformFuncViewportSuppBoolean
        public boolean get(ImGuiViewport vp) {
            ViewportData vd = (ViewportData) vp.getPlatformUserData();
            return (vd == null || GLFW.glfwGetWindowAttrib(vd.window, 131074) == 0) ? false : true;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/glfw/ImGuiImplGlfw$SetWindowAlphaFunction.class */
    private final class SetWindowAlphaFunction extends ImPlatformFuncViewportFloat {
        private SetWindowAlphaFunction() {
        }

        @Override // imgui.callback.ImPlatformFuncViewportFloat
        public void accept(ImGuiViewport vp, float value) {
            ViewportData vd = (ViewportData) vp.getPlatformUserData();
            if (vd != null) {
                GLFW.glfwSetWindowOpacity(vd.window, value);
            }
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/glfw/ImGuiImplGlfw$RenderWindowFunction.class */
    private static final class RenderWindowFunction extends ImPlatformFuncViewport {
        private RenderWindowFunction() {
        }

        @Override // imgui.callback.ImPlatformFuncViewport
        public void accept(ImGuiViewport vp) {
            ViewportData vd = (ViewportData) vp.getPlatformUserData();
            if (vd != null) {
                GLFW.glfwMakeContextCurrent(vd.window);
            }
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/glfw/ImGuiImplGlfw$SwapBuffersFunction.class */
    private static final class SwapBuffersFunction extends ImPlatformFuncViewport {
        private SwapBuffersFunction() {
        }

        @Override // imgui.callback.ImPlatformFuncViewport
        public void accept(ImGuiViewport vp) {
            ViewportData vd = (ViewportData) vp.getPlatformUserData();
            if (vd != null) {
                GLFW.glfwMakeContextCurrent(vd.window);
                GLFW.glfwSwapBuffers(vd.window);
            }
        }
    }

    protected void initPlatformInterface() {
        ImGuiPlatformIO platformIO = ImGui.getPlatformIO();
        platformIO.setPlatformCreateWindow(new CreateWindowFunction());
        platformIO.setPlatformDestroyWindow(new DestroyWindowFunction());
        platformIO.setPlatformShowWindow(new ShowWindowFunction());
        platformIO.setPlatformGetWindowPos(new GetWindowPosFunction());
        platformIO.setPlatformSetWindowPos(new SetWindowPosFunction());
        platformIO.setPlatformGetWindowSize(new GetWindowSizeFunction());
        platformIO.setPlatformSetWindowSize(new SetWindowSizeFunction());
        platformIO.setPlatformSetWindowTitle(new SetWindowTitleFunction());
        platformIO.setPlatformSetWindowFocus(new SetWindowFocusFunction());
        platformIO.setPlatformGetWindowFocus(new GetWindowFocusFunction());
        platformIO.setPlatformGetWindowMinimized(new GetWindowMinimizedFunction());
        platformIO.setPlatformSetWindowAlpha(new SetWindowAlphaFunction());
        platformIO.setPlatformRenderWindow(new RenderWindowFunction());
        platformIO.setPlatformSwapBuffers(new SwapBuffersFunction());
        ImGuiViewport mainViewport = ImGui.getMainViewport();
        ViewportData vd = new ViewportData();
        vd.window = this.data.window;
        vd.windowOwned = false;
        mainViewport.setPlatformUserData(vd);
        mainViewport.setPlatformHandle(this.data.window);
    }

    protected void shutdownPlatformInterface() {
        ImGui.destroyPlatformWindows();
    }
}
