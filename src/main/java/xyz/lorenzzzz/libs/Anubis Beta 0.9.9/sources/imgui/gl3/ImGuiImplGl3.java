package imgui.gl3;

import imgui.ImDrawData;
import imgui.ImFontAtlas;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImGuiViewport;
import imgui.ImVec4;
import imgui.callback.ImPlatformFuncViewport;
import imgui.type.ImInt;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GLCapabilities;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/gl3/ImGuiImplGl3.class */
public class ImGuiImplGl3 {
    protected static final String OS = System.getProperty("os.name", "generic").toLowerCase();
    protected static final boolean IS_APPLE;
    protected Data data = null;
    private final Properties props = new Properties();

    static {
        IS_APPLE = OS.contains("mac") || OS.contains("darwin");
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/gl3/ImGuiImplGl3$Data.class */
    protected static class Data {
        protected boolean glProfileIsCompat;
        protected int glProfileMask;
        protected boolean hasClipOrigin;
        protected int glVersion = 0;
        protected GLCapabilities glCapabilities = null;
        protected String glslVersion = "";
        protected int fontTexture = 0;
        protected int shaderHandle = 0;
        protected int attribLocationTex = 0;
        protected int attribLocationProjMtx = 0;
        protected int attribLocationVtxPos = 0;
        protected int attribLocationVtxUV = 0;
        protected int attribLocationVtxColor = 0;
        protected int vboHandle = 0;
        protected int elementsHandle = 0;

        protected Data() {
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/gl3/ImGuiImplGl3$Properties.class */
    private static final class Properties {
        private final ImVec4 clipRect;
        private final float[] orthoProjMatrix;
        private final int[] lastActiveTexture;
        private final int[] lastProgram;
        private final int[] lastTexture;
        private final int[] lastSampler;
        private final int[] lastArrayBuffer;
        private final int[] lastVertexArrayObject;
        private final int[] lastPolygonMode;
        private final int[] lastViewport;
        private final int[] lastScissorBox;
        private final int[] lastBlendSrcRgb;
        private final int[] lastBlendDstRgb;
        private final int[] lastBlendSrcAlpha;
        private final int[] lastBlendDstAlpha;
        private final int[] lastBlendEquationRgb;
        private final int[] lastBlendEquationAlpha;
        private boolean lastEnableBlend;
        private boolean lastEnableCullFace;
        private boolean lastEnableDepthTest;
        private boolean lastEnableStencilTest;
        private boolean lastEnableScissorTest;
        private boolean lastEnablePrimitiveRestart;

        private Properties() {
            this.clipRect = new ImVec4();
            this.orthoProjMatrix = new float[16];
            this.lastActiveTexture = new int[1];
            this.lastProgram = new int[1];
            this.lastTexture = new int[1];
            this.lastSampler = new int[1];
            this.lastArrayBuffer = new int[1];
            this.lastVertexArrayObject = new int[1];
            this.lastPolygonMode = new int[2];
            this.lastViewport = new int[4];
            this.lastScissorBox = new int[4];
            this.lastBlendSrcRgb = new int[1];
            this.lastBlendDstRgb = new int[1];
            this.lastBlendSrcAlpha = new int[1];
            this.lastBlendDstAlpha = new int[1];
            this.lastBlendEquationRgb = new int[1];
            this.lastBlendEquationAlpha = new int[1];
            this.lastEnableBlend = false;
            this.lastEnableCullFace = false;
            this.lastEnableDepthTest = false;
            this.lastEnableStencilTest = false;
            this.lastEnableScissorTest = false;
            this.lastEnablePrimitiveRestart = false;
        }
    }

    protected Data newData() {
        return new Data();
    }

    public boolean init() {
        return init(null);
    }

    public boolean init(String glslVersion) {
        this.data = newData();
        ImGuiIO io2 = ImGui.getIO();
        io2.setBackendRendererName("imgui-java_impl_opengl3");
        String glVersion = GL32.glGetString(7938);
        int major = GL32.glGetInteger(33307);
        int minor = GL32.glGetInteger(33308);
        if (major == 0 && minor == 0 && glVersion != null) {
            String[] glVersions = glVersion.split("\\.");
            major = Integer.parseInt(glVersions[0]);
            minor = Integer.parseInt(glVersions[1]);
        }
        this.data.glVersion = (major * 100) + (minor * 10);
        this.data.glProfileMask = GL32.glGetInteger(37158);
        this.data.glProfileIsCompat = (this.data.glProfileMask & 2) != 0;
        if (this.data.glVersion < 330) {
            try {
                this.data.glCapabilities = GL.getCapabilities();
            } catch (IllegalStateException e) {
            }
        }
        if (this.data.glVersion >= 320) {
            io2.addBackendFlags(8);
        }
        io2.addBackendFlags(4096);
        if (glslVersion == null) {
            if (IS_APPLE) {
                this.data.glslVersion = "#version 150";
            } else {
                this.data.glslVersion = "#version 130";
            }
        } else {
            this.data.glslVersion = glslVersion;
        }
        int[] currentTexture = new int[1];
        GL32.glGetIntegerv(32873, currentTexture);
        this.data.hasClipOrigin = this.data.glVersion >= 450;
        if (ImGui.getIO().hasConfigFlags(1024)) {
            initPlatformInterface();
            return true;
        }
        return true;
    }

    public void shutdown() {
        ImGuiIO io2 = ImGui.getIO();
        shutdownPlatformInterface();
        destroyDeviceObjects();
        io2.setBackendRendererName(null);
        io2.removeBackendFlags(4104);
        this.data = null;
    }

    public void newFrame() {
        if (this.data.shaderHandle == 0) {
            createDeviceObjects();
        }
        if (this.data.fontTexture == 0) {
            createFontsTexture();
        }
    }

    protected void setupRenderState(ImDrawData drawData, int fbWidth, int fbHeight, int gVertexArrayObject) {
        GL32.glEnable(3042);
        GL32.glBlendEquation(32774);
        GL32.glBlendFuncSeparate(770, 771, 1, 771);
        GL32.glDisable(2884);
        GL32.glDisable(2929);
        GL32.glDisable(2960);
        GL32.glEnable(3089);
        if (this.data.glVersion >= 310) {
            GL32.glDisable(36765);
        }
        if (this.data.glVersion >= 200) {
            GL32.glPolygonMode(1032, 6914);
        }
        boolean clipOriginLowerLeft = true;
        if (this.data.hasClipOrigin) {
            int[] currentClipOrigin = new int[1];
            GL32.glGetIntegerv(37724, currentClipOrigin);
            if (currentClipOrigin[0] == 36002) {
                clipOriginLowerLeft = false;
            }
        }
        GL32.glViewport(0, 0, fbWidth, fbHeight);
        float L = drawData.getDisplayPosX();
        float R = drawData.getDisplayPosX() + drawData.getDisplaySizeX();
        float T = drawData.getDisplayPosY();
        float B = drawData.getDisplayPosY() + drawData.getDisplaySizeY();
        if (this.data.hasClipOrigin && !clipOriginLowerLeft) {
            T = B;
            B = T;
        }
        this.props.orthoProjMatrix[0] = 2.0f / (R - L);
        this.props.orthoProjMatrix[5] = 2.0f / (T - B);
        this.props.orthoProjMatrix[10] = -1.0f;
        this.props.orthoProjMatrix[12] = (R + L) / (L - R);
        this.props.orthoProjMatrix[13] = (T + B) / (B - T);
        this.props.orthoProjMatrix[15] = 1.0f;
        GL32.glUseProgram(this.data.shaderHandle);
        GL32.glUniform1i(this.data.attribLocationTex, 0);
        GL32.glUniformMatrix4fv(this.data.attribLocationProjMtx, false, this.props.orthoProjMatrix);
        if (this.data.glVersion >= 330 || (this.data.glCapabilities != null && this.data.glCapabilities.GL_ARB_sampler_objects)) {
            GL33.glBindSampler(0, 0);
        }
        GL32.glBindVertexArray(gVertexArrayObject);
        GL32.glBindBuffer(34962, this.data.vboHandle);
        GL32.glBindBuffer(34963, this.data.elementsHandle);
        GL32.glEnableVertexAttribArray(this.data.attribLocationVtxPos);
        GL32.glEnableVertexAttribArray(this.data.attribLocationVtxUV);
        GL32.glEnableVertexAttribArray(this.data.attribLocationVtxColor);
        GL32.glVertexAttribPointer(this.data.attribLocationVtxPos, 2, 5126, false, ImDrawData.sizeOfImDrawVert(), 0L);
        GL32.glVertexAttribPointer(this.data.attribLocationVtxUV, 2, 5126, false, ImDrawData.sizeOfImDrawVert(), 8L);
        GL32.glVertexAttribPointer(this.data.attribLocationVtxColor, 4, 5121, true, ImDrawData.sizeOfImDrawVert(), 16L);
    }

    public void renderDrawData(ImDrawData drawData) {
        int fbWidth = (int) (drawData.getDisplaySizeX() * drawData.getFramebufferScaleX());
        int fbHeight = (int) (drawData.getDisplaySizeY() * drawData.getFramebufferScaleY());
        if (fbWidth <= 0 || fbHeight <= 0 || drawData.getCmdListsCount() <= 0) {
            return;
        }
        GL32.glGetIntegerv(34016, this.props.lastActiveTexture);
        GL32.glActiveTexture(33984);
        GL32.glGetIntegerv(35725, this.props.lastProgram);
        GL32.glGetIntegerv(32873, this.props.lastTexture);
        if (this.data.glVersion >= 330 || (this.data.glCapabilities != null && this.data.glCapabilities.GL_ARB_sampler_objects)) {
            GL32.glGetIntegerv(35097, this.props.lastSampler);
        }
        GL32.glGetIntegerv(34964, this.props.lastArrayBuffer);
        GL32.glGetIntegerv(34229, this.props.lastVertexArrayObject);
        if (this.data.glVersion >= 200) {
            GL32.glGetIntegerv(2880, this.props.lastPolygonMode);
        }
        GL32.glGetIntegerv(2978, this.props.lastViewport);
        GL32.glGetIntegerv(3088, this.props.lastScissorBox);
        GL32.glGetIntegerv(32969, this.props.lastBlendSrcRgb);
        GL32.glGetIntegerv(32968, this.props.lastBlendDstRgb);
        GL32.glGetIntegerv(32971, this.props.lastBlendSrcAlpha);
        GL32.glGetIntegerv(32970, this.props.lastBlendDstAlpha);
        GL32.glGetIntegerv(32777, this.props.lastBlendEquationRgb);
        GL32.glGetIntegerv(34877, this.props.lastBlendEquationAlpha);
        this.props.lastEnableBlend = GL32.glIsEnabled(3042);
        this.props.lastEnableCullFace = GL32.glIsEnabled(2884);
        this.props.lastEnableDepthTest = GL32.glIsEnabled(2929);
        this.props.lastEnableStencilTest = GL32.glIsEnabled(2960);
        this.props.lastEnableScissorTest = GL32.glIsEnabled(3089);
        if (this.data.glVersion >= 310) {
            this.props.lastEnablePrimitiveRestart = GL32.glIsEnabled(36765);
        }
        int vertexArrayObject = GL32.glGenVertexArrays();
        setupRenderState(drawData, fbWidth, fbHeight, vertexArrayObject);
        float clipOffX = drawData.getDisplayPosX();
        float clipOffY = drawData.getDisplayPosY();
        float clipScaleX = drawData.getFramebufferScaleX();
        float clipScaleY = drawData.getFramebufferScaleY();
        for (int n = 0; n < drawData.getCmdListsCount(); n++) {
            GL32.glBufferData(34962, drawData.getCmdListVtxBufferData(n), 35040);
            GL32.glBufferData(34963, drawData.getCmdListIdxBufferData(n), 35040);
            for (int cmdIdx = 0; cmdIdx < drawData.getCmdListCmdBufferSize(n); cmdIdx++) {
                drawData.getCmdListCmdBufferClipRect(this.props.clipRect, n, cmdIdx);
                float clipMinX = (this.props.clipRect.x - clipOffX) * clipScaleX;
                float clipMinY = (this.props.clipRect.y - clipOffY) * clipScaleY;
                float clipMaxX = (this.props.clipRect.z - clipOffX) * clipScaleX;
                float clipMaxY = (this.props.clipRect.w - clipOffY) * clipScaleY;
                if (clipMaxX > clipMinX && clipMaxY > clipMinY) {
                    GL32.glScissor((int) clipMinX, (int) (fbHeight - clipMaxY), (int) (clipMaxX - clipMinX), (int) (clipMaxY - clipMinY));
                    long textureId = drawData.getCmdListCmdBufferTextureId(n, cmdIdx);
                    int elemCount = drawData.getCmdListCmdBufferElemCount(n, cmdIdx);
                    int idxOffset = drawData.getCmdListCmdBufferIdxOffset(n, cmdIdx);
                    int vtxOffset = drawData.getCmdListCmdBufferVtxOffset(n, cmdIdx);
                    long indices = ((long) idxOffset) * ((long) ImDrawData.sizeOfImDrawIdx());
                    int type = ImDrawData.sizeOfImDrawIdx() == 2 ? 5123 : 5125;
                    GL32.glBindTexture(3553, (int) textureId);
                    if (this.data.glVersion >= 320) {
                        GL32.glDrawElementsBaseVertex(4, elemCount, type, indices, vtxOffset);
                    } else {
                        GL32.glDrawElements(4, elemCount, type, indices);
                    }
                }
            }
        }
        GL32.glDeleteVertexArrays(vertexArrayObject);
        if (this.props.lastProgram[0] == 0 || GL32.glIsProgram(this.props.lastProgram[0])) {
            GL32.glUseProgram(this.props.lastProgram[0]);
        }
        GL32.glBindTexture(3553, this.props.lastTexture[0]);
        if (this.data.glVersion >= 330 || (this.data.glCapabilities != null && this.data.glCapabilities.GL_ARB_sampler_objects)) {
            GL33.glBindSampler(0, this.props.lastSampler[0]);
        }
        GL32.glActiveTexture(this.props.lastActiveTexture[0]);
        GL32.glBindVertexArray(this.props.lastVertexArrayObject[0]);
        GL32.glBindBuffer(34962, this.props.lastArrayBuffer[0]);
        GL32.glBlendEquationSeparate(this.props.lastBlendEquationRgb[0], this.props.lastBlendEquationAlpha[0]);
        GL32.glBlendFuncSeparate(this.props.lastBlendSrcRgb[0], this.props.lastBlendDstRgb[0], this.props.lastBlendSrcAlpha[0], this.props.lastBlendDstAlpha[0]);
        if (this.props.lastEnableBlend) {
            GL32.glEnable(3042);
        } else {
            GL32.glDisable(3042);
        }
        if (this.props.lastEnableCullFace) {
            GL32.glEnable(2884);
        } else {
            GL32.glDisable(2884);
        }
        if (this.props.lastEnableDepthTest) {
            GL32.glEnable(2929);
        } else {
            GL32.glDisable(2929);
        }
        if (this.props.lastEnableStencilTest) {
            GL32.glEnable(2960);
        } else {
            GL32.glDisable(2960);
        }
        if (this.props.lastEnableScissorTest) {
            GL32.glEnable(3089);
        } else {
            GL32.glDisable(3089);
        }
        if (this.data.glVersion >= 310) {
            if (this.props.lastEnablePrimitiveRestart) {
                GL32.glEnable(36765);
            } else {
                GL32.glDisable(36765);
            }
        }
        if (this.data.glVersion <= 310 || this.data.glProfileIsCompat) {
            GL32.glPolygonMode(1028, this.props.lastPolygonMode[0]);
            GL32.glPolygonMode(1029, this.props.lastPolygonMode[1]);
        } else {
            GL32.glPolygonMode(1032, this.props.lastPolygonMode[0]);
        }
        GL32.glViewport(this.props.lastViewport[0], this.props.lastViewport[1], this.props.lastViewport[2], this.props.lastViewport[3]);
        GL32.glScissor(this.props.lastScissorBox[0], this.props.lastScissorBox[1], this.props.lastScissorBox[2], this.props.lastScissorBox[3]);
    }

    public boolean createFontsTexture() {
        ImFontAtlas fontAtlas = ImGui.getIO().getFonts();
        ImInt width = new ImInt();
        ImInt height = new ImInt();
        ByteBuffer pixels = fontAtlas.getTexDataAsRGBA32(width, height);
        int[] lastTexture = new int[1];
        GL32.glGetIntegerv(32873, lastTexture);
        this.data.fontTexture = GL32.glGenTextures();
        GL32.glBindTexture(3553, this.data.fontTexture);
        GL32.glTexParameteri(3553, 10241, 9729);
        GL32.glTexParameteri(3553, 10240, 9729);
        GL32.glPixelStorei(3317, 4);
        GL32.glPixelStorei(3316, 0);
        GL32.glPixelStorei(3315, 0);
        GL32.glPixelStorei(3314, 0);
        GL32.glTexImage2D(3553, 0, 6408, width.get(), height.get(), 0, 6408, 5121, pixels);
        fontAtlas.setTexID(this.data.fontTexture);
        GL32.glBindTexture(3553, lastTexture[0]);
        return true;
    }

    public void destroyFontsTexture() {
        ImGuiIO io2 = ImGui.getIO();
        if (this.data.fontTexture != 0) {
            GL32.glDeleteTextures(this.data.fontTexture);
            io2.getFonts().setTexID(0L);
            this.data.fontTexture = 0;
        }
    }

    protected boolean checkShader(int handle, String desc) {
        int[] status = new int[1];
        int[] logLength = new int[1];
        GL32.glGetShaderiv(handle, 35713, status);
        GL32.glGetShaderiv(handle, 35716, logLength);
        if (status[0] == 0) {
            System.err.printf("%s: failed to compile %s! With GLSL: %s\n", this, desc, this.data.glslVersion);
        }
        if (logLength[0] > 1) {
            String log = GL32.glGetShaderInfoLog(handle);
            System.err.println(log);
        }
        return status[0] == 1;
    }

    protected boolean checkProgram(int handle, String desc) {
        int[] status = new int[1];
        int[] logLength = new int[1];
        GL20.glGetProgramiv(handle, 35714, status);
        GL20.glGetProgramiv(handle, 35716, logLength);
        if (status[0] == 0) {
            System.err.printf("%s: failed to link %s! With GLSL: %s\n", this, desc, this.data.glslVersion);
        }
        if (logLength[0] > 1) {
            String log = GL32.glGetProgramInfoLog(handle);
            System.err.println(log);
        }
        return status[0] == 1;
    }

    protected int parseGlslVersionString(String glslVersion) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(glslVersion);
        if (m.find()) {
            return Integer.parseInt(m.group());
        }
        return 130;
    }

    protected boolean createDeviceObjects() {
        CharSequence vertexShader;
        CharSequence fragmentShader;
        int[] lastTexture = new int[1];
        int[] lastArrayBuffer = new int[1];
        int[] lastVertexArray = new int[1];
        GL32.glGetIntegerv(32873, lastTexture);
        GL32.glGetIntegerv(34964, lastArrayBuffer);
        GL32.glGetIntegerv(34229, lastVertexArray);
        int glslVersionValue = parseGlslVersionString(this.data.glslVersion);
        if (glslVersionValue < 130) {
            vertexShader = vertexShaderGlsl120();
            fragmentShader = fragmentShaderGlsl120();
        } else if (glslVersionValue >= 410) {
            vertexShader = vertexShaderGlsl410Core();
            fragmentShader = fragmentShaderGlsl410Core();
        } else if (glslVersionValue == 300) {
            vertexShader = vertexShaderGlsl300es();
            fragmentShader = fragmentShaderGlsl300es();
        } else {
            vertexShader = vertexShaderGlsl130();
            fragmentShader = fragmentShaderGlsl130();
        }
        int vertHandle = GL32.glCreateShader(35633);
        GL32.glShaderSource(vertHandle, vertexShader);
        GL32.glCompileShader(vertHandle);
        checkShader(vertHandle, "vertex shader");
        int fragHandle = GL32.glCreateShader(35632);
        GL32.glShaderSource(fragHandle, fragmentShader);
        GL32.glCompileShader(fragHandle);
        checkShader(fragHandle, "fragment shader");
        this.data.shaderHandle = GL32.glCreateProgram();
        GL32.glAttachShader(this.data.shaderHandle, vertHandle);
        GL32.glAttachShader(this.data.shaderHandle, fragHandle);
        GL32.glLinkProgram(this.data.shaderHandle);
        checkProgram(this.data.shaderHandle, "shader program");
        GL20.glDetachShader(this.data.shaderHandle, vertHandle);
        GL20.glDetachShader(this.data.shaderHandle, fragHandle);
        GL20.glDeleteShader(vertHandle);
        GL20.glDeleteShader(fragHandle);
        this.data.attribLocationTex = GL20.glGetUniformLocation(this.data.shaderHandle, "Texture");
        this.data.attribLocationProjMtx = GL20.glGetUniformLocation(this.data.shaderHandle, "ProjMtx");
        this.data.attribLocationVtxPos = GL20.glGetAttribLocation(this.data.shaderHandle, "Position");
        this.data.attribLocationVtxUV = GL20.glGetAttribLocation(this.data.shaderHandle, "UV");
        this.data.attribLocationVtxColor = GL20.glGetAttribLocation(this.data.shaderHandle, "Color");
        this.data.vboHandle = GL32.glGenBuffers();
        this.data.elementsHandle = GL32.glGenBuffers();
        createFontsTexture();
        GL32.glBindTexture(3553, lastTexture[0]);
        GL32.glBindBuffer(34962, lastArrayBuffer[0]);
        GL32.glBindVertexArray(lastVertexArray[0]);
        return true;
    }

    public void destroyDeviceObjects() {
        if (this.data.vboHandle != 0) {
            GL32.glDeleteBuffers(this.data.vboHandle);
            this.data.vboHandle = 0;
        }
        if (this.data.elementsHandle != 0) {
            GL32.glDeleteBuffers(this.data.elementsHandle);
            this.data.elementsHandle = 0;
        }
        if (this.data.shaderHandle != 0) {
            GL32.glDeleteProgram(this.data.shaderHandle);
            this.data.shaderHandle = 0;
        }
        destroyFontsTexture();
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-lwjgl3-1.90.0.jar:imgui/gl3/ImGuiImplGl3$RendererRenderWindowFunction.class */
    private final class RendererRenderWindowFunction extends ImPlatformFuncViewport {
        private RendererRenderWindowFunction() {
        }

        @Override // imgui.callback.ImPlatformFuncViewport
        public void accept(ImGuiViewport vp) {
            if (!vp.hasFlags(256)) {
                GL32.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                GL32.glClear(16384);
            }
            ImGuiImplGl3.this.renderDrawData(vp.getDrawData());
        }
    }

    protected void initPlatformInterface() {
        ImGui.getPlatformIO().setRendererRenderWindow(new RendererRenderWindowFunction());
    }

    protected void shutdownPlatformInterface() {
        ImGui.destroyPlatformWindows();
    }

    protected String vertexShaderGlsl120() {
        return this.data.glslVersion + "\nuniform mat4 ProjMtx;\nattribute vec2 Position;\nattribute vec2 UV;\nattribute vec4 Color;\nvarying vec2 Frag_UV;\nvarying vec4 Frag_Color;\nvoid main()\n{\n    Frag_UV = UV;\n    Frag_Color = Color;\n    gl_Position = ProjMtx * vec4(Position.xy,0,1);\n}\n";
    }

    protected String vertexShaderGlsl130() {
        return this.data.glslVersion + "\nuniform mat4 ProjMtx;\nin vec2 Position;\nin vec2 UV;\nin vec4 Color;\nout vec2 Frag_UV;\nout vec4 Frag_Color;\nvoid main()\n{\n    Frag_UV = UV;\n    Frag_Color = Color;\n    gl_Position = ProjMtx * vec4(Position.xy,0,1);\n}\n";
    }

    private String vertexShaderGlsl300es() {
        return this.data.glslVersion + "\nprecision highp float;\nlayout (location = 0) in vec2 Position;\nlayout (location = 1) in vec2 UV;\nlayout (location = 2) in vec4 Color;\nuniform mat4 ProjMtx;\nout vec2 Frag_UV;\nout vec4 Frag_Color;\nvoid main()\n{\n    Frag_UV = UV;\n    Frag_Color = Color;\n    gl_Position = ProjMtx * vec4(Position.xy,0,1);\n}\n";
    }

    protected String vertexShaderGlsl410Core() {
        return this.data.glslVersion + "\nlayout (location = 0) in vec2 Position;\nlayout (location = 1) in vec2 UV;\nlayout (location = 2) in vec4 Color;\nuniform mat4 ProjMtx;\nout vec2 Frag_UV;\nout vec4 Frag_Color;\nvoid main()\n{\n    Frag_UV = UV;\n    Frag_Color = Color;\n    gl_Position = ProjMtx * vec4(Position.xy,0,1);\n}\n";
    }

    protected String fragmentShaderGlsl120() {
        return this.data.glslVersion + "\n#ifdef GL_ES\n    precision mediump float;\n#endif\nuniform sampler2D Texture;\nvarying vec2 Frag_UV;\nvarying vec4 Frag_Color;\nvoid main()\n{\n    gl_FragColor = Frag_Color * texture2D(Texture, Frag_UV.st);\n}\n";
    }

    protected String fragmentShaderGlsl130() {
        return this.data.glslVersion + "\nuniform sampler2D Texture;\nin vec2 Frag_UV;\nin vec4 Frag_Color;\nout vec4 Out_Color;\nvoid main()\n{\n    Out_Color = Frag_Color * texture(Texture, Frag_UV.st);\n}\n";
    }

    protected String fragmentShaderGlsl300es() {
        return this.data.glslVersion + "\nprecision mediump float;\nuniform sampler2D Texture;\nin vec2 Frag_UV;\nin vec4 Frag_Color;\nlayout (location = 0) out vec4 Out_Color;\nvoid main()\n{\n    Out_Color = Frag_Color * texture(Texture, Frag_UV.st);\n}\n";
    }

    protected String fragmentShaderGlsl410Core() {
        return this.data.glslVersion + "\nin vec2 Frag_UV;\nin vec4 Frag_Color;\nuniform sampler2D Texture;\nlayout (location = 0) out vec4 Out_Color;\nvoid main()\n{\n    Out_Color = Frag_Color * texture(Texture, Frag_UV.st);\n}\n";
    }
}
