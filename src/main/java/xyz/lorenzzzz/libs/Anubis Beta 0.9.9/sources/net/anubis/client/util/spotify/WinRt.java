package net.anubis.client.util.spotify;

import com.sun.jna.Function;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;
import java.util.ArrayDeque;
import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/spotify/WinRt.class */
@Environment(EnvType.CLIENT)
final class WinRt {
    static final int FIRST_SLOT = 6;
    static final int RO_INIT_MULTITHREADED = 1;
    static final int RPC_E_CHANGED_MODE = -2147417850;
    static final int REGDB_E_CLASSNOTREG = -2147221164;
    static final int E_NOINTERFACE = -2147467262;
    static final int E_ABORT = -2147467260;
    static final int E_TIMEOUT = -2147023436;
    private static final int SLOT_QUERY_INTERFACE = 0;
    private static final int SLOT_RELEASE = 2;
    private static final int ASYNC_OPERATION_GET_RESULTS = 8;
    private static final Guid.GUID IID_ASYNC_INFO = iid("00000036-0000-0000-C000-000000000046");
    private static final int ASYNC_INFO_GET_STATUS = 7;
    private static final int ASYNC_INFO_GET_ERROR_CODE = 8;
    private static final int ASYNC_INFO_CANCEL = 9;
    private static final int ASYNC_STARTED = 0;
    private static final int ASYNC_COMPLETED = 1;
    private static final int ASYNC_CANCELED = 2;
    private static final long MAX_WAIT_STEP_MS = 5;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/spotify/WinRt$Combase.class */
    @Environment(EnvType.CLIENT)
    interface Combase extends StdCallLibrary {
        public static final Combase INSTANCE = Native.load("combase", Combase.class);

        int RoInitialize(int i);

        void RoUninitialize();

        int RoGetActivationFactory(Pointer pointer, Pointer pointer2, PointerByReference pointerByReference);

        int WindowsCreateString(WString wString, int i, PointerByReference pointerByReference);

        int WindowsDeleteString(Pointer pointer);

        Pointer WindowsGetStringRawBuffer(Pointer pointer, IntByReference intByReference);
    }

    private WinRt() {
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/spotify/WinRt$WinRtException.class */
    @Environment(EnvType.CLIENT)
    static final class WinRtException extends RuntimeException {
        private static final long serialVersionUID = 1;
        final int hresult;

        WinRtException(String step, int hresult) {
            super(step + " failed (0x" + Integer.toHexString(hresult).toUpperCase(Locale.ROOT) + ")", null, false, false);
            this.hresult = hresult;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/spotify/WinRt$Refs.class */
    @Environment(EnvType.CLIENT)
    static final class Refs implements AutoCloseable {
        private final ArrayDeque<Pointer> held = new ArrayDeque<>();

        Refs() {
        }

        Pointer add(Pointer pointer) {
            if (pointer != null) {
                this.held.push(pointer);
            }
            return pointer;
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            while (!this.held.isEmpty()) {
                WinRt.release(this.held.pop());
            }
        }
    }

    static Guid.GUID iid(String text) {
        Guid.GUID guid = new Guid.GUID(text);
        guid.write();
        return guid;
    }

    static int initializeMultithreaded() {
        return Combase.INSTANCE.RoInitialize(1);
    }

    static void uninitialize() {
        Combase.INSTANCE.RoUninitialize();
    }

    static Pointer activationFactory(String runtimeClass, Guid.GUID iid) {
        Pointer name = createString(runtimeClass);
        try {
            PointerByReference factory = new PointerByReference();
            check(Combase.INSTANCE.RoGetActivationFactory(name, iid.getPointer(), factory), "RoGetActivationFactory(" + runtimeClass + ")");
            Pointer pointerOwned = owned(factory.getValue(), "RoGetActivationFactory(" + runtimeClass + ")");
            return pointerOwned;
        } finally {
            Combase.INSTANCE.WindowsDeleteString(name);
        }
    }

    static int invoke(Pointer self, int slot, Object... args) {
        Pointer vtable = self.getPointer(0L);
        Function method = Function.getFunction(vtable.getPointer(((long) slot) * ((long) Native.POINTER_SIZE)), 63);
        Object[] full = new Object[args.length + 1];
        full[0] = self;
        System.arraycopy(args, 0, full, 1, args.length);
        return method.invokeInt(full);
    }

    static void check(int hresult, String step) {
        if (hresult < 0) {
            throw new WinRtException(step, hresult);
        }
    }

    static void release(Pointer pointer) {
        if (pointer == null) {
            return;
        }
        invoke(pointer, 2, new Object[0]);
    }

    static Pointer queryInterface(Pointer self, Guid.GUID iid, String step) {
        PointerByReference out = new PointerByReference();
        check(invoke(self, 0, iid.getPointer(), out), step);
        return owned(out.getValue(), step);
    }

    static Pointer tryQueryInterface(Pointer self, Guid.GUID iid) {
        PointerByReference out = new PointerByReference();
        if (invoke(self, 0, iid.getPointer(), out) < 0) {
            return null;
        }
        return out.getValue();
    }

    static Pointer callForObject(Pointer self, int slot, String step, Object... inArgs) {
        PointerByReference out = new PointerByReference();
        check(invoke(self, slot, append(inArgs, out)), step);
        return out.getValue();
    }

    static String getString(Pointer self, int slot, String step) {
        PointerByReference out = new PointerByReference();
        check(invoke(self, slot, out), step);
        Pointer hstring = out.getValue();
        if (hstring == null) {
            return "";
        }
        try {
            String string = readString(hstring);
            return string;
        } finally {
            Combase.INSTANCE.WindowsDeleteString(hstring);
        }
    }

    static int getInt32(Pointer self, int slot, String step) {
        IntByReference out = new IntByReference();
        check(invoke(self, slot, out), step);
        return out.getValue();
    }

    static long getInt64(Pointer self, int slot, String step) {
        LongByReference out = new LongByReference();
        check(invoke(self, slot, out), step);
        return out.getValue();
    }

    static double getDouble(Pointer self, int slot, String step) {
        DoubleByReference out = new DoubleByReference();
        check(invoke(self, slot, out), step);
        return out.getValue();
    }

    static Pointer awaitObject(Pointer operation, long timeoutMs, String step) {
        await(operation, timeoutMs, step);
        PointerByReference out = new PointerByReference();
        check(invoke(operation, 8, out), step + " results");
        return out.getValue();
    }

    static boolean awaitBoolean(Pointer operation, long timeoutMs, String step) {
        await(operation, timeoutMs, step);
        ByteByReference out = new ByteByReference();
        check(invoke(operation, 8, out), step + " results");
        return out.getValue() != 0;
    }

    static int awaitUInt32(Pointer operation, long timeoutMs, String step) {
        await(operation, timeoutMs, step);
        IntByReference out = new IntByReference();
        check(invoke(operation, 8, out), step + " results");
        return out.getValue();
    }

    private static void await(Pointer operation, long timeoutMs, String step) {
        if (operation == null) {
            throw new WinRtException(step + " returned no operation", E_ABORT);
        }
        Pointer info = queryInterface(operation, IID_ASYNC_INFO, step + " IAsyncInfo");
        try {
            long deadline = System.nanoTime() + (timeoutMs * 1000000);
            long pause = 1;
            while (true) {
                int status = getInt32(info, 7, step + " status");
                if (status != 1) {
                    if (status == 2) {
                        throw new WinRtException(step + " canceled", E_ABORT);
                    }
                    if (status != 0) {
                        IntByReference code = new IntByReference();
                        int hresult = invoke(info, 8, code);
                        throw new WinRtException(step, hresult < 0 ? hresult : code.getValue());
                    }
                    if (System.nanoTime() - deadline >= 0) {
                        invoke(info, 9, new Object[0]);
                        throw new WinRtException(step + " timed out", E_TIMEOUT);
                    }
                    try {
                        Thread.sleep(pause);
                        pause = Math.min(pause + 1, MAX_WAIT_STEP_MS);
                    } catch (InterruptedException e) {
                        invoke(info, 9, new Object[0]);
                        Thread.currentThread().interrupt();
                        throw new WinRtException(step + " interrupted", E_ABORT);
                    }
                } else {
                    release(info);
                    return;
                }
            }
        } catch (Throwable th) {
            release(info);
            throw th;
        }
    }

    private static Pointer createString(String text) {
        PointerByReference out = new PointerByReference();
        check(Combase.INSTANCE.WindowsCreateString(new WString(text), text.length(), out), "WindowsCreateString");
        return out.getValue();
    }

    private static String readString(Pointer hstring) {
        IntByReference length = new IntByReference();
        Pointer buffer = Combase.INSTANCE.WindowsGetStringRawBuffer(hstring, length);
        int count = length.getValue();
        return (buffer == null || count <= 0) ? "" : new String(buffer.getCharArray(0L, count));
    }

    private static Pointer owned(Pointer pointer, String step) {
        if (pointer == null) {
            throw new WinRtException(step + " returned null", E_NOINTERFACE);
        }
        return pointer;
    }

    private static Object[] append(Object[] args, Object last) {
        Object[] all = new Object[args.length + 1];
        System.arraycopy(args, 0, all, 0, args.length);
        all[args.length] = last;
        return all;
    }
}
