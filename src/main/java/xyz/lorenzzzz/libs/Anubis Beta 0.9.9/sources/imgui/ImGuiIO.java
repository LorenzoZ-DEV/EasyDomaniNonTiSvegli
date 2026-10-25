package imgui;

import imgui.binding.ImGuiStruct;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.internal.ImGuiContext;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/imgui-java-binding-1.90.0.jar:imgui/ImGuiIO.class */
public final class ImGuiIO extends ImGuiStruct {
    private static final ImFontAtlas _GETFONTS_1 = new ImFontAtlas(0);
    private static final ImGuiContext _GETCTX_1 = new ImGuiContext(0);

    private native int nGetConfigFlags();

    private native void nSetConfigFlags(int i);

    private native int nGetBackendFlags();

    private native void nSetBackendFlags(int i);

    private native void nGetDisplaySize(ImVec2 imVec2);

    private native float nGetDisplaySizeX();

    private native float nGetDisplaySizeY();

    private native void nSetDisplaySize(float f, float f2);

    private native float nGetDeltaTime();

    private native void nSetDeltaTime(float f);

    private native float nGetIniSavingRate();

    private native void nSetIniSavingRate(float f);

    private native String nGetIniFilename();

    private native void nSetIniFilename(String str);

    private native String nGetLogFilename();

    private native void nSetLogFilename(String str);

    private native long nGetFonts();

    private native void nSetFonts(long j);

    private native float nGetFontGlobalScale();

    private native void nSetFontGlobalScale(float f);

    private native boolean nGetFontAllowUserScaling();

    private native void nSetFontAllowUserScaling(boolean z);

    private native long nGetFontDefault();

    private native void nSetFontDefault(long j);

    private native void nGetDisplayFramebufferScale(ImVec2 imVec2);

    private native float nGetDisplayFramebufferScaleX();

    private native float nGetDisplayFramebufferScaleY();

    private native void nSetDisplayFramebufferScale(float f, float f2);

    private native boolean nGetConfigDockingNoSplit();

    private native void nSetConfigDockingNoSplit(boolean z);

    private native boolean nGetConfigDockingWithShift();

    private native void nSetConfigDockingWithShift(boolean z);

    private native boolean nGetConfigDockingAlwaysTabBar();

    private native void nSetConfigDockingAlwaysTabBar(boolean z);

    private native boolean nGetConfigDockingTransparentPayload();

    private native void nSetConfigDockingTransparentPayload(boolean z);

    private native boolean nGetConfigViewportsNoAutoMerge();

    private native void nSetConfigViewportsNoAutoMerge(boolean z);

    private native boolean nGetConfigViewportsNoTaskBarIcon();

    private native void nSetConfigViewportsNoTaskBarIcon(boolean z);

    private native boolean nGetConfigViewportsNoDecoration();

    private native void nSetConfigViewportsNoDecoration(boolean z);

    private native boolean nGetConfigViewportsNoDefaultParent();

    private native void nSetConfigViewportsNoDefaultParent(boolean z);

    private native boolean nGetMouseDrawCursor();

    private native void nSetMouseDrawCursor(boolean z);

    private native boolean nGetConfigMacOSXBehaviors();

    private native void nSetConfigMacOSXBehaviors(boolean z);

    private native boolean nGetConfigInputTrickleEventQueue();

    private native void nSetConfigInputTrickleEventQueue(boolean z);

    private native boolean nGetConfigInputTextCursorBlink();

    private native void nSetConfigInputTextCursorBlink(boolean z);

    private native boolean nGetConfigInputTextEnterKeepActive();

    private native void nSetConfigInputTextEnterKeepActive(boolean z);

    private native boolean nGetConfigDragClickToInputText();

    private native void nSetConfigDragClickToInputText(boolean z);

    private native boolean nGetConfigWindowsResizeFromEdges();

    private native void nSetConfigWindowsResizeFromEdges(boolean z);

    private native boolean nGetConfigWindowsMoveFromTitleBarOnly();

    private native void nSetConfigWindowsMoveFromTitleBarOnly(boolean z);

    private native boolean nGetConfigMemoryCompactTimer();

    private native void nSetConfigMemoryCompactTimer(boolean z);

    private native float nGetMouseDoubleClickTime();

    private native void nSetMouseDoubleClickTime(float f);

    private native float nGetMouseDoubleClickMaxDist();

    private native void nSetMouseDoubleClickMaxDist(float f);

    private native float nGetMouseDragThreshold();

    private native void nSetMouseDragThreshold(float f);

    private native float nGetKeyRepeatDelay();

    private native void nSetKeyRepeatDelay(float f);

    private native float nGetKeyRepeatRate();

    private native void nSetKeyRepeatRate(float f);

    private native boolean nGetConfigDebugIsDebuggerPresent();

    private native void nSetConfigDebugIsDebuggerPresent(boolean z);

    private native boolean nGetConfigDebugBeginReturnValueOnce();

    private native void nSetConfigDebugBeginReturnValueOnce(boolean z);

    private native boolean nGetConfigDebugBeginReturnValueLoop();

    private native void nSetConfigDebugBeginReturnValueLoop(boolean z);

    private native boolean nGetConfigDebugIgnoreFocusLoss();

    private native void nSetConfigDebugIgnoreFocusLoss(boolean z);

    private native boolean nGetConfigDebugIniSettings();

    private native void nSetConfigDebugIniSettings(boolean z);

    private native String nGetBackendPlatformName();

    private native void nSetBackendPlatformName(String str);

    private native String nGetBackendRendererName();

    private native void nSetBackendRendererName(String str);

    public native void setSetClipboardTextFn(ImStrConsumer imStrConsumer);

    public native void setGetClipboardTextFn(ImStrSupplier imStrSupplier);

    private native short nGetPlatformLocaleDecimalPoint();

    private native void nSetPlatformLocaleDecimalPoint(short s);

    private native void nAddKeyEvent(int i, boolean z);

    private native void nAddKeyAnalogEvent(int i, boolean z, float f);

    private native void nAddMousePosEvent(float f, float f2);

    private native void nAddMouseButtonEvent(int i, boolean z);

    private native void nAddMouseWheelEvent(float f, float f2);

    private native void nAddMouseSourceEvent(int i);

    private native void nAddMouseViewportEvent(int i);

    private native void nAddFocusEvent(boolean z);

    private native void nAddInputCharacter(int i);

    private native void nAddInputCharacterUTF16(short s);

    private native void nAddInputCharactersUTF8(String str);

    private native void nSetKeyEventNativeData(int i, int i2, int i3);

    private native void nSetKeyEventNativeData(int i, int i2, int i3, int i4);

    private native void nSetAppAcceptingEvents(boolean z);

    private native void nClearEventsQueue();

    private native void nClearInputKeys();

    private native void nClearInputMouse();

    private native boolean nGetWantCaptureMouse();

    private native void nSetWantCaptureMouse(boolean z);

    private native boolean nGetWantCaptureKeyboard();

    private native void nSetWantCaptureKeyboard(boolean z);

    private native boolean nGetWantTextInput();

    private native void nSetWantTextInput(boolean z);

    private native boolean nGetWantSetMousePos();

    private native void nSetWantSetMousePos(boolean z);

    private native boolean nGetWantSaveIniSettings();

    private native void nSetWantSaveIniSettings(boolean z);

    private native boolean nGetNavActive();

    private native void nSetNavActive(boolean z);

    private native boolean nGetNavVisible();

    private native void nSetNavVisible(boolean z);

    private native float nGetFramerate();

    private native void nSetFramerate(float f);

    private native int nGetMetricsRenderVertices();

    private native void nSetMetricsRenderVertices(int i);

    private native int nGetMetricsRenderIndices();

    private native void nSetMetricsRenderIndices(int i);

    private native int nGetMetricsRenderWindows();

    private native void nSetMetricsRenderWindows(int i);

    private native int nGetMetricsActiveWindows();

    private native void nSetMetricsActiveWindows(int i);

    private native void nGetMouseDelta(ImVec2 imVec2);

    private native float nGetMouseDeltaX();

    private native float nGetMouseDeltaY();

    private native void nSetMouseDelta(float f, float f2);

    private native long nGetCtx();

    private native void nSetCtx(long j);

    private native void nGetMousePos(ImVec2 imVec2);

    private native float nGetMousePosX();

    private native float nGetMousePosY();

    private native void nSetMousePos(float f, float f2);

    private native boolean[] nGetMouseDown();

    private native boolean nGetMouseDown(int i);

    private native void nSetMouseDown(boolean[] zArr);

    private native void nSetMouseDown(int i, boolean z);

    private native float nGetMouseWheel();

    private native void nSetMouseWheel(float f);

    private native float nGetMouseWheelH();

    private native void nSetMouseWheelH(float f);

    private native int nGetMouseHoveredViewport();

    private native void nSetMouseHoveredViewport(int i);

    private native boolean nGetKeyCtrl();

    private native void nSetKeyCtrl(boolean z);

    private native boolean nGetKeyShift();

    private native void nSetKeyShift(boolean z);

    private native boolean nGetKeyAlt();

    private native void nSetKeyAlt(boolean z);

    private native boolean nGetKeySuper();

    private native void nSetKeySuper(boolean z);

    private native int nGetKeyMods();

    private native void nSetKeyMods(int i);

    private native ImGuiKeyData[] nGetKeysData();

    private native void nSetKeysData(ImGuiKeyData[] imGuiKeyDataArr);

    private native boolean nGetWantCaptureMouseUnlessPopupClose();

    private native void nSetWantCaptureMouseUnlessPopupClose(boolean z);

    private native void nGetMousePosPrev(ImVec2 imVec2);

    private native float nGetMousePosPrevX();

    private native float nGetMousePosPrevY();

    private native void nSetMousePosPrev(float f, float f2);

    private native ImVec2[] nGetMouseClickedPos();

    private native void nSetMouseClickedPos(ImVec2[] imVec2Arr);

    private native double[] nGetMouseClickedTime();

    private native double nGetMouseClickedTime(int i);

    private native void nSetMouseClickedTime(double[] dArr);

    private native void nSetMouseClickedTime(int i, double d);

    private native boolean[] nGetMouseClicked();

    private native boolean nGetMouseClicked(int i);

    private native void nSetMouseClicked(boolean[] zArr);

    private native void nSetMouseClicked(int i, boolean z);

    private native boolean[] nGetMouseDoubleClicked();

    private native boolean nGetMouseDoubleClicked(int i);

    private native void nSetMouseDoubleClicked(boolean[] zArr);

    private native void nSetMouseDoubleClicked(int i, boolean z);

    private native int[] nGetMouseClickedCount();

    private native int nGetMouseClickedCount(int i);

    private native void nSetMouseClickedCount(int[] iArr);

    private native void nSetMouseClickedCount(int i, int i2);

    private native int[] nGetMouseClickedLastCount();

    private native int nGetMouseClickedLastCount(int i);

    private native void nSetMouseClickedLastCount(int[] iArr);

    private native void nSetMouseClickedLastCount(int i, int i2);

    private native boolean[] nGetMouseReleased();

    private native boolean nGetMouseReleased(int i);

    private native void nSetMouseReleased(boolean[] zArr);

    private native void nSetMouseReleased(int i, boolean z);

    private native boolean[] nGetMouseDownOwned();

    private native boolean nGetMouseDownOwned(int i);

    private native void nSetMouseDownOwned(boolean[] zArr);

    private native void nSetMouseDownOwned(int i, boolean z);

    private native boolean[] nGetMouseDownOwnedUnlessPopupClose();

    private native boolean nGetMouseDownOwnedUnlessPopupClose(int i);

    private native void nSetMouseDownOwnedUnlessPopupClose(boolean[] zArr);

    private native void nSetMouseDownOwnedUnlessPopupClose(int i, boolean z);

    private native boolean nGetMouseWheelRequestAxisSwap();

    private native void nSetMouseWheelRequestAxisSwap(boolean z);

    private native boolean nGetMouseCtrlLeftAsRightClick();

    private native void nSetMouseCtrlLeftAsRightClick(boolean z);

    private native float[] nGetMouseDownDuration();

    private native float nGetMouseDownDuration(int i);

    private native void nSetMouseDownDuration(float[] fArr);

    private native void nSetMouseDownDuration(int i, float f);

    private native float[] nGetMouseDownDurationPrev();

    private native float nGetMouseDownDurationPrev(int i);

    private native void nSetMouseDownDurationPrev(float[] fArr);

    private native void nSetMouseDownDurationPrev(int i, float f);

    private native ImVec2[] nGetMouseDragMaxDistanceAbs();

    private native void nSetMouseDragMaxDistanceAbs(ImVec2[] imVec2Arr);

    private native float[] nGetMouseDragMaxDistanceSqr();

    private native float nGetMouseDragMaxDistanceSqr(int i);

    private native void nSetMouseDragMaxDistanceSqr(float[] fArr);

    private native void nSetMouseDragMaxDistanceSqr(int i, float f);

    private native float nGetPenPressure();

    private native void nSetPenPressure(float f);

    private native boolean nGetAppFocusLost();

    private native boolean nGetAppAcceptingEvents();

    private native short nGetBackendUsingLegacyKeyArrays();

    private native void nSetBackendUsingLegacyKeyArrays(short s);

    private native boolean nGetBackendUsingLegacyNavInputArray();

    private native void nSetBackendUsingLegacyNavInputArray(boolean z);

    private native short nGetInputQueueSurrogate();

    private native void nSetInputQueueSurrogate(short s);

    public ImGuiIO(long ptr) {
        super(ptr);
    }

    public int getConfigFlags() {
        return nGetConfigFlags();
    }

    public void setConfigFlags(int value) {
        nSetConfigFlags(value);
    }

    public void addConfigFlags(int flags) {
        setConfigFlags(getConfigFlags() | flags);
    }

    public void removeConfigFlags(int flags) {
        setConfigFlags(getConfigFlags() & (flags ^ (-1)));
    }

    public boolean hasConfigFlags(int flags) {
        return (getConfigFlags() & flags) != 0;
    }

    public int getBackendFlags() {
        return nGetBackendFlags();
    }

    public void setBackendFlags(int value) {
        nSetBackendFlags(value);
    }

    public void addBackendFlags(int flags) {
        setBackendFlags(getBackendFlags() | flags);
    }

    public void removeBackendFlags(int flags) {
        setBackendFlags(getBackendFlags() & (flags ^ (-1)));
    }

    public boolean hasBackendFlags(int flags) {
        return (getBackendFlags() & flags) != 0;
    }

    public ImVec2 getDisplaySize() {
        ImVec2 dst = new ImVec2();
        nGetDisplaySize(dst);
        return dst;
    }

    public float getDisplaySizeX() {
        return nGetDisplaySizeX();
    }

    public float getDisplaySizeY() {
        return nGetDisplaySizeY();
    }

    public void getDisplaySize(ImVec2 dst) {
        nGetDisplaySize(dst);
    }

    public void setDisplaySize(ImVec2 value) {
        nSetDisplaySize(value.x, value.y);
    }

    public void setDisplaySize(float valueX, float valueY) {
        nSetDisplaySize(valueX, valueY);
    }

    public float getDeltaTime() {
        return nGetDeltaTime();
    }

    public void setDeltaTime(float value) {
        nSetDeltaTime(value);
    }

    public float getIniSavingRate() {
        return nGetIniSavingRate();
    }

    public void setIniSavingRate(float value) {
        nSetIniSavingRate(value);
    }

    public String getIniFilename() {
        return nGetIniFilename();
    }

    public void setIniFilename(String value) {
        nSetIniFilename(value);
    }

    public String getLogFilename() {
        return nGetLogFilename();
    }

    public void setLogFilename(String value) {
        nSetLogFilename(value);
    }

    public ImFontAtlas getFonts() {
        _GETFONTS_1.ptr = nGetFonts();
        return _GETFONTS_1;
    }

    public void setFonts(ImFontAtlas value) {
        nSetFonts(value.ptr);
    }

    public float getFontGlobalScale() {
        return nGetFontGlobalScale();
    }

    public void setFontGlobalScale(float value) {
        nSetFontGlobalScale(value);
    }

    public boolean getFontAllowUserScaling() {
        return nGetFontAllowUserScaling();
    }

    public void setFontAllowUserScaling(boolean value) {
        nSetFontAllowUserScaling(value);
    }

    public ImFont getFontDefault() {
        return new ImFont(nGetFontDefault());
    }

    public void setFontDefault(ImFont value) {
        nSetFontDefault(value.ptr);
    }

    public ImVec2 getDisplayFramebufferScale() {
        ImVec2 dst = new ImVec2();
        nGetDisplayFramebufferScale(dst);
        return dst;
    }

    public float getDisplayFramebufferScaleX() {
        return nGetDisplayFramebufferScaleX();
    }

    public float getDisplayFramebufferScaleY() {
        return nGetDisplayFramebufferScaleY();
    }

    public void getDisplayFramebufferScale(ImVec2 dst) {
        nGetDisplayFramebufferScale(dst);
    }

    public void setDisplayFramebufferScale(ImVec2 value) {
        nSetDisplayFramebufferScale(value.x, value.y);
    }

    public void setDisplayFramebufferScale(float valueX, float valueY) {
        nSetDisplayFramebufferScale(valueX, valueY);
    }

    public boolean getConfigDockingNoSplit() {
        return nGetConfigDockingNoSplit();
    }

    public void setConfigDockingNoSplit(boolean value) {
        nSetConfigDockingNoSplit(value);
    }

    public boolean getConfigDockingWithShift() {
        return nGetConfigDockingWithShift();
    }

    public void setConfigDockingWithShift(boolean value) {
        nSetConfigDockingWithShift(value);
    }

    public boolean getConfigDockingAlwaysTabBar() {
        return nGetConfigDockingAlwaysTabBar();
    }

    public void setConfigDockingAlwaysTabBar(boolean value) {
        nSetConfigDockingAlwaysTabBar(value);
    }

    public boolean getConfigDockingTransparentPayload() {
        return nGetConfigDockingTransparentPayload();
    }

    public void setConfigDockingTransparentPayload(boolean value) {
        nSetConfigDockingTransparentPayload(value);
    }

    public boolean getConfigViewportsNoAutoMerge() {
        return nGetConfigViewportsNoAutoMerge();
    }

    public void setConfigViewportsNoAutoMerge(boolean value) {
        nSetConfigViewportsNoAutoMerge(value);
    }

    public boolean getConfigViewportsNoTaskBarIcon() {
        return nGetConfigViewportsNoTaskBarIcon();
    }

    public void setConfigViewportsNoTaskBarIcon(boolean value) {
        nSetConfigViewportsNoTaskBarIcon(value);
    }

    public boolean getConfigViewportsNoDecoration() {
        return nGetConfigViewportsNoDecoration();
    }

    public void setConfigViewportsNoDecoration(boolean value) {
        nSetConfigViewportsNoDecoration(value);
    }

    public boolean getConfigViewportsNoDefaultParent() {
        return nGetConfigViewportsNoDefaultParent();
    }

    public void setConfigViewportsNoDefaultParent(boolean value) {
        nSetConfigViewportsNoDefaultParent(value);
    }

    public boolean getMouseDrawCursor() {
        return nGetMouseDrawCursor();
    }

    public void setMouseDrawCursor(boolean value) {
        nSetMouseDrawCursor(value);
    }

    public boolean getConfigMacOSXBehaviors() {
        return nGetConfigMacOSXBehaviors();
    }

    public void setConfigMacOSXBehaviors(boolean value) {
        nSetConfigMacOSXBehaviors(value);
    }

    public boolean getConfigInputTrickleEventQueue() {
        return nGetConfigInputTrickleEventQueue();
    }

    public void setConfigInputTrickleEventQueue(boolean value) {
        nSetConfigInputTrickleEventQueue(value);
    }

    public boolean getConfigInputTextCursorBlink() {
        return nGetConfigInputTextCursorBlink();
    }

    public void setConfigInputTextCursorBlink(boolean value) {
        nSetConfigInputTextCursorBlink(value);
    }

    public boolean getConfigInputTextEnterKeepActive() {
        return nGetConfigInputTextEnterKeepActive();
    }

    public void setConfigInputTextEnterKeepActive(boolean value) {
        nSetConfigInputTextEnterKeepActive(value);
    }

    public boolean getConfigDragClickToInputText() {
        return nGetConfigDragClickToInputText();
    }

    public void setConfigDragClickToInputText(boolean value) {
        nSetConfigDragClickToInputText(value);
    }

    public boolean getConfigWindowsResizeFromEdges() {
        return nGetConfigWindowsResizeFromEdges();
    }

    public void setConfigWindowsResizeFromEdges(boolean value) {
        nSetConfigWindowsResizeFromEdges(value);
    }

    public boolean getConfigWindowsMoveFromTitleBarOnly() {
        return nGetConfigWindowsMoveFromTitleBarOnly();
    }

    public void setConfigWindowsMoveFromTitleBarOnly(boolean value) {
        nSetConfigWindowsMoveFromTitleBarOnly(value);
    }

    public boolean getConfigMemoryCompactTimer() {
        return nGetConfigMemoryCompactTimer();
    }

    public void setConfigMemoryCompactTimer(boolean value) {
        nSetConfigMemoryCompactTimer(value);
    }

    public float getMouseDoubleClickTime() {
        return nGetMouseDoubleClickTime();
    }

    public void setMouseDoubleClickTime(float value) {
        nSetMouseDoubleClickTime(value);
    }

    public float getMouseDoubleClickMaxDist() {
        return nGetMouseDoubleClickMaxDist();
    }

    public void setMouseDoubleClickMaxDist(float value) {
        nSetMouseDoubleClickMaxDist(value);
    }

    public float getMouseDragThreshold() {
        return nGetMouseDragThreshold();
    }

    public void setMouseDragThreshold(float value) {
        nSetMouseDragThreshold(value);
    }

    public float getKeyRepeatDelay() {
        return nGetKeyRepeatDelay();
    }

    public void setKeyRepeatDelay(float value) {
        nSetKeyRepeatDelay(value);
    }

    public float getKeyRepeatRate() {
        return nGetKeyRepeatRate();
    }

    public void setKeyRepeatRate(float value) {
        nSetKeyRepeatRate(value);
    }

    public boolean getConfigDebugIsDebuggerPresent() {
        return nGetConfigDebugIsDebuggerPresent();
    }

    public void setConfigDebugIsDebuggerPresent(boolean value) {
        nSetConfigDebugIsDebuggerPresent(value);
    }

    public boolean getConfigDebugBeginReturnValueOnce() {
        return nGetConfigDebugBeginReturnValueOnce();
    }

    public void setConfigDebugBeginReturnValueOnce(boolean value) {
        nSetConfigDebugBeginReturnValueOnce(value);
    }

    public boolean getConfigDebugBeginReturnValueLoop() {
        return nGetConfigDebugBeginReturnValueLoop();
    }

    public void setConfigDebugBeginReturnValueLoop(boolean value) {
        nSetConfigDebugBeginReturnValueLoop(value);
    }

    public boolean getConfigDebugIgnoreFocusLoss() {
        return nGetConfigDebugIgnoreFocusLoss();
    }

    public void setConfigDebugIgnoreFocusLoss(boolean value) {
        nSetConfigDebugIgnoreFocusLoss(value);
    }

    public boolean getConfigDebugIniSettings() {
        return nGetConfigDebugIniSettings();
    }

    public void setConfigDebugIniSettings(boolean value) {
        nSetConfigDebugIniSettings(value);
    }

    public String getBackendPlatformName() {
        return nGetBackendPlatformName();
    }

    public void setBackendPlatformName(String value) {
        nSetBackendPlatformName(value);
    }

    public String getBackendRendererName() {
        return nGetBackendRendererName();
    }

    public void setBackendRendererName(String value) {
        nSetBackendRendererName(value);
    }

    public short getPlatformLocaleDecimalPoint() {
        return nGetPlatformLocaleDecimalPoint();
    }

    public void setPlatformLocaleDecimalPoint(short value) {
        nSetPlatformLocaleDecimalPoint(value);
    }

    public void addKeyEvent(int key, boolean down) {
        nAddKeyEvent(key, down);
    }

    public void addKeyAnalogEvent(int key, boolean down, float v) {
        nAddKeyAnalogEvent(key, down, v);
    }

    public void addMousePosEvent(float x, float y) {
        nAddMousePosEvent(x, y);
    }

    public void addMouseButtonEvent(int button, boolean down) {
        nAddMouseButtonEvent(button, down);
    }

    public void addMouseWheelEvent(float whX, float whY) {
        nAddMouseWheelEvent(whX, whY);
    }

    public void addMouseSourceEvent(int source) {
        nAddMouseSourceEvent(source);
    }

    public void addMouseViewportEvent(int id) {
        nAddMouseViewportEvent(id);
    }

    public void addFocusEvent(boolean focused) {
        nAddFocusEvent(focused);
    }

    public void addInputCharacter(int c) {
        nAddInputCharacter(c);
    }

    public void addInputCharacterUTF16(short c) {
        nAddInputCharacterUTF16(c);
    }

    public void addInputCharactersUTF8(String str) {
        nAddInputCharactersUTF8(str);
    }

    public void setKeyEventNativeData(int key, int nativeKeycode, int nativeScancode) {
        nSetKeyEventNativeData(key, nativeKeycode, nativeScancode);
    }

    public void setKeyEventNativeData(int key, int nativeKeycode, int nativeScancode, int nativeLegacyIndex) {
        nSetKeyEventNativeData(key, nativeKeycode, nativeScancode, nativeLegacyIndex);
    }

    public void setAppAcceptingEvents(boolean acceptingEvents) {
        nSetAppAcceptingEvents(acceptingEvents);
    }

    public void clearEventsQueue() {
        nClearEventsQueue();
    }

    public void clearInputKeys() {
        nClearInputKeys();
    }

    public void clearInputMouse() {
        nClearInputMouse();
    }

    public boolean getWantCaptureMouse() {
        return nGetWantCaptureMouse();
    }

    public void setWantCaptureMouse(boolean value) {
        nSetWantCaptureMouse(value);
    }

    public boolean getWantCaptureKeyboard() {
        return nGetWantCaptureKeyboard();
    }

    public void setWantCaptureKeyboard(boolean value) {
        nSetWantCaptureKeyboard(value);
    }

    public boolean getWantTextInput() {
        return nGetWantTextInput();
    }

    public void setWantTextInput(boolean value) {
        nSetWantTextInput(value);
    }

    public boolean getWantSetMousePos() {
        return nGetWantSetMousePos();
    }

    public void setWantSetMousePos(boolean value) {
        nSetWantSetMousePos(value);
    }

    public boolean getWantSaveIniSettings() {
        return nGetWantSaveIniSettings();
    }

    public void setWantSaveIniSettings(boolean value) {
        nSetWantSaveIniSettings(value);
    }

    public boolean getNavActive() {
        return nGetNavActive();
    }

    public void setNavActive(boolean value) {
        nSetNavActive(value);
    }

    public boolean getNavVisible() {
        return nGetNavVisible();
    }

    public void setNavVisible(boolean value) {
        nSetNavVisible(value);
    }

    public float getFramerate() {
        return nGetFramerate();
    }

    public void setFramerate(float value) {
        nSetFramerate(value);
    }

    public int getMetricsRenderVertices() {
        return nGetMetricsRenderVertices();
    }

    public void setMetricsRenderVertices(int value) {
        nSetMetricsRenderVertices(value);
    }

    public int getMetricsRenderIndices() {
        return nGetMetricsRenderIndices();
    }

    public void setMetricsRenderIndices(int value) {
        nSetMetricsRenderIndices(value);
    }

    public int getMetricsRenderWindows() {
        return nGetMetricsRenderWindows();
    }

    public void setMetricsRenderWindows(int value) {
        nSetMetricsRenderWindows(value);
    }

    public int getMetricsActiveWindows() {
        return nGetMetricsActiveWindows();
    }

    public void setMetricsActiveWindows(int value) {
        nSetMetricsActiveWindows(value);
    }

    public ImVec2 getMouseDelta() {
        ImVec2 dst = new ImVec2();
        nGetMouseDelta(dst);
        return dst;
    }

    public float getMouseDeltaX() {
        return nGetMouseDeltaX();
    }

    public float getMouseDeltaY() {
        return nGetMouseDeltaY();
    }

    public void getMouseDelta(ImVec2 dst) {
        nGetMouseDelta(dst);
    }

    public void setMouseDelta(ImVec2 value) {
        nSetMouseDelta(value.x, value.y);
    }

    public void setMouseDelta(float valueX, float valueY) {
        nSetMouseDelta(valueX, valueY);
    }

    public ImGuiContext getCtx() {
        _GETCTX_1.ptr = nGetCtx();
        return _GETCTX_1;
    }

    public void setCtx(ImGuiContext value) {
        nSetCtx(value.ptr);
    }

    public ImVec2 getMousePos() {
        ImVec2 dst = new ImVec2();
        nGetMousePos(dst);
        return dst;
    }

    public float getMousePosX() {
        return nGetMousePosX();
    }

    public float getMousePosY() {
        return nGetMousePosY();
    }

    public void getMousePos(ImVec2 dst) {
        nGetMousePos(dst);
    }

    public void setMousePos(ImVec2 value) {
        nSetMousePos(value.x, value.y);
    }

    public void setMousePos(float valueX, float valueY) {
        nSetMousePos(valueX, valueY);
    }

    public boolean[] getMouseDown() {
        return nGetMouseDown();
    }

    public boolean getMouseDown(int idx) {
        return nGetMouseDown(idx);
    }

    public void setMouseDown(boolean[] value) {
        nSetMouseDown(value);
    }

    public void setMouseDown(int idx, boolean value) {
        nSetMouseDown(idx, value);
    }

    public float getMouseWheel() {
        return nGetMouseWheel();
    }

    public void setMouseWheel(float value) {
        nSetMouseWheel(value);
    }

    public float getMouseWheelH() {
        return nGetMouseWheelH();
    }

    public void setMouseWheelH(float value) {
        nSetMouseWheelH(value);
    }

    public int getMouseHoveredViewport() {
        return nGetMouseHoveredViewport();
    }

    public void setMouseHoveredViewport(int value) {
        nSetMouseHoveredViewport(value);
    }

    public boolean getKeyCtrl() {
        return nGetKeyCtrl();
    }

    public void setKeyCtrl(boolean value) {
        nSetKeyCtrl(value);
    }

    public boolean getKeyShift() {
        return nGetKeyShift();
    }

    public void setKeyShift(boolean value) {
        nSetKeyShift(value);
    }

    public boolean getKeyAlt() {
        return nGetKeyAlt();
    }

    public void setKeyAlt(boolean value) {
        nSetKeyAlt(value);
    }

    public boolean getKeySuper() {
        return nGetKeySuper();
    }

    public void setKeySuper(boolean value) {
        nSetKeySuper(value);
    }

    public int getKeyMods() {
        return nGetKeyMods();
    }

    public void setKeyMods(int value) {
        nSetKeyMods(value);
    }

    public ImGuiKeyData[] getKeysData() {
        return nGetKeysData();
    }

    public void setKeysData(ImGuiKeyData[] value) {
        nSetKeysData(value);
    }

    public boolean getWantCaptureMouseUnlessPopupClose() {
        return nGetWantCaptureMouseUnlessPopupClose();
    }

    public void setWantCaptureMouseUnlessPopupClose(boolean value) {
        nSetWantCaptureMouseUnlessPopupClose(value);
    }

    public ImVec2 getMousePosPrev() {
        ImVec2 dst = new ImVec2();
        nGetMousePosPrev(dst);
        return dst;
    }

    public float getMousePosPrevX() {
        return nGetMousePosPrevX();
    }

    public float getMousePosPrevY() {
        return nGetMousePosPrevY();
    }

    public void getMousePosPrev(ImVec2 dst) {
        nGetMousePosPrev(dst);
    }

    public void setMousePosPrev(ImVec2 value) {
        nSetMousePosPrev(value.x, value.y);
    }

    public void setMousePosPrev(float valueX, float valueY) {
        nSetMousePosPrev(valueX, valueY);
    }

    public ImVec2[] getMouseClickedPos() {
        return nGetMouseClickedPos();
    }

    public void setMouseClickedPos(ImVec2[] value) {
        nSetMouseClickedPos(value);
    }

    public double[] getMouseClickedTime() {
        return nGetMouseClickedTime();
    }

    public double getMouseClickedTime(int idx) {
        return nGetMouseClickedTime(idx);
    }

    public void setMouseClickedTime(double[] value) {
        nSetMouseClickedTime(value);
    }

    public void setMouseClickedTime(int idx, double value) {
        nSetMouseClickedTime(idx, value);
    }

    public boolean[] getMouseClicked() {
        return nGetMouseClicked();
    }

    public boolean getMouseClicked(int idx) {
        return nGetMouseClicked(idx);
    }

    public void setMouseClicked(boolean[] value) {
        nSetMouseClicked(value);
    }

    public void setMouseClicked(int idx, boolean value) {
        nSetMouseClicked(idx, value);
    }

    public boolean[] getMouseDoubleClicked() {
        return nGetMouseDoubleClicked();
    }

    public boolean getMouseDoubleClicked(int idx) {
        return nGetMouseDoubleClicked(idx);
    }

    public void setMouseDoubleClicked(boolean[] value) {
        nSetMouseDoubleClicked(value);
    }

    public void setMouseDoubleClicked(int idx, boolean value) {
        nSetMouseDoubleClicked(idx, value);
    }

    public int[] getMouseClickedCount() {
        return nGetMouseClickedCount();
    }

    public int getMouseClickedCount(int idx) {
        return nGetMouseClickedCount(idx);
    }

    public void setMouseClickedCount(int[] value) {
        nSetMouseClickedCount(value);
    }

    public void setMouseClickedCount(int idx, int value) {
        nSetMouseClickedCount(idx, value);
    }

    public int[] getMouseClickedLastCount() {
        return nGetMouseClickedLastCount();
    }

    public int getMouseClickedLastCount(int idx) {
        return nGetMouseClickedLastCount(idx);
    }

    public void setMouseClickedLastCount(int[] value) {
        nSetMouseClickedLastCount(value);
    }

    public void setMouseClickedLastCount(int idx, int value) {
        nSetMouseClickedLastCount(idx, value);
    }

    public boolean[] getMouseReleased() {
        return nGetMouseReleased();
    }

    public boolean getMouseReleased(int idx) {
        return nGetMouseReleased(idx);
    }

    public void setMouseReleased(boolean[] value) {
        nSetMouseReleased(value);
    }

    public void setMouseReleased(int idx, boolean value) {
        nSetMouseReleased(idx, value);
    }

    public boolean[] getMouseDownOwned() {
        return nGetMouseDownOwned();
    }

    public boolean getMouseDownOwned(int idx) {
        return nGetMouseDownOwned(idx);
    }

    public void setMouseDownOwned(boolean[] value) {
        nSetMouseDownOwned(value);
    }

    public void setMouseDownOwned(int idx, boolean value) {
        nSetMouseDownOwned(idx, value);
    }

    public boolean[] getMouseDownOwnedUnlessPopupClose() {
        return nGetMouseDownOwnedUnlessPopupClose();
    }

    public boolean getMouseDownOwnedUnlessPopupClose(int idx) {
        return nGetMouseDownOwnedUnlessPopupClose(idx);
    }

    public void setMouseDownOwnedUnlessPopupClose(boolean[] value) {
        nSetMouseDownOwnedUnlessPopupClose(value);
    }

    public void setMouseDownOwnedUnlessPopupClose(int idx, boolean value) {
        nSetMouseDownOwnedUnlessPopupClose(idx, value);
    }

    public boolean getMouseWheelRequestAxisSwap() {
        return nGetMouseWheelRequestAxisSwap();
    }

    public void setMouseWheelRequestAxisSwap(boolean value) {
        nSetMouseWheelRequestAxisSwap(value);
    }

    public boolean getMouseCtrlLeftAsRightClick() {
        return nGetMouseCtrlLeftAsRightClick();
    }

    public void setMouseCtrlLeftAsRightClick(boolean value) {
        nSetMouseCtrlLeftAsRightClick(value);
    }

    public float[] getMouseDownDuration() {
        return nGetMouseDownDuration();
    }

    public float getMouseDownDuration(int idx) {
        return nGetMouseDownDuration(idx);
    }

    public void setMouseDownDuration(float[] value) {
        nSetMouseDownDuration(value);
    }

    public void setMouseDownDuration(int idx, float value) {
        nSetMouseDownDuration(idx, value);
    }

    public float[] getMouseDownDurationPrev() {
        return nGetMouseDownDurationPrev();
    }

    public float getMouseDownDurationPrev(int idx) {
        return nGetMouseDownDurationPrev(idx);
    }

    public void setMouseDownDurationPrev(float[] value) {
        nSetMouseDownDurationPrev(value);
    }

    public void setMouseDownDurationPrev(int idx, float value) {
        nSetMouseDownDurationPrev(idx, value);
    }

    public ImVec2[] getMouseDragMaxDistanceAbs() {
        return nGetMouseDragMaxDistanceAbs();
    }

    public void setMouseDragMaxDistanceAbs(ImVec2[] value) {
        nSetMouseDragMaxDistanceAbs(value);
    }

    public float[] getMouseDragMaxDistanceSqr() {
        return nGetMouseDragMaxDistanceSqr();
    }

    public float getMouseDragMaxDistanceSqr(int idx) {
        return nGetMouseDragMaxDistanceSqr(idx);
    }

    public void setMouseDragMaxDistanceSqr(float[] value) {
        nSetMouseDragMaxDistanceSqr(value);
    }

    public void setMouseDragMaxDistanceSqr(int idx, float value) {
        nSetMouseDragMaxDistanceSqr(idx, value);
    }

    public float getPenPressure() {
        return nGetPenPressure();
    }

    public void setPenPressure(float value) {
        nSetPenPressure(value);
    }

    public boolean getAppFocusLost() {
        return nGetAppFocusLost();
    }

    public boolean getAppAcceptingEvents() {
        return nGetAppAcceptingEvents();
    }

    public short getBackendUsingLegacyKeyArrays() {
        return nGetBackendUsingLegacyKeyArrays();
    }

    public void setBackendUsingLegacyKeyArrays(short value) {
        nSetBackendUsingLegacyKeyArrays(value);
    }

    public boolean getBackendUsingLegacyNavInputArray() {
        return nGetBackendUsingLegacyNavInputArray();
    }

    public void setBackendUsingLegacyNavInputArray(boolean value) {
        nSetBackendUsingLegacyNavInputArray(value);
    }

    public short getInputQueueSurrogate() {
        return nGetInputQueueSurrogate();
    }

    public void setInputQueueSurrogate(short value) {
        nSetInputQueueSurrogate(value);
    }
}
