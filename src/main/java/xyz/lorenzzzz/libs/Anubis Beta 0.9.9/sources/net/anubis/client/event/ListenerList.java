package net.anubis.client.event;

import java.util.Arrays;
import java.util.function.Consumer;
import net.anubis.client.event.Event;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/event/ListenerList.class */
@Environment(EnvType.CLIENT)
public final class ListenerList<E extends Event> {
    private static final Consumer[] NONE = new Consumer[0];
    private volatile Consumer<E>[] pre = none();
    private volatile Consumer<E>[] post = none();
    private final Handles<E> preHandles = new Handles<>();
    private final Handles<E> postHandles = new Handles<>();

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/event/ListenerList$Handles.class */
    @Environment(EnvType.CLIENT)
    private static final class Handles<E extends Event> {
        private ListenerHandle<E>[] handles = new ListenerHandle[4];

        private Handles() {
        }

        Consumer<E>[] insert(Consumer<E>[] callbacks, ListenerHandle<E> handle) {
            int size = callbacks.length;
            int priority = handle.priority().value;
            int low = 0;
            int high = size;
            while (low < high) {
                int mid = (low + high) >>> 1;
                if (this.handles[mid].priority().value >= priority) {
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }
            if (size == this.handles.length) {
                this.handles = (ListenerHandle[]) Arrays.copyOf(this.handles, size * 2);
            }
            System.arraycopy(this.handles, low, this.handles, low + 1, size - low);
            this.handles[low] = handle;
            Consumer<E>[] next = new Consumer[size + 1];
            System.arraycopy(callbacks, 0, next, 0, low);
            next[low] = handle.callback();
            System.arraycopy(callbacks, low, next, low + 1, size - low);
            return next;
        }

        Consumer<E>[] remove(Consumer<E>[] callbacks, ListenerHandle<E> handle) {
            int size = callbacks.length;
            int index = indexOf(callbacks, handle);
            if (index < 0) {
                return callbacks;
            }
            System.arraycopy(this.handles, index + 1, this.handles, index, (size - index) - 1);
            this.handles[size - 1] = null;
            if (size == 1) {
                return ListenerList.none();
            }
            Consumer<E>[] next = new Consumer[size - 1];
            System.arraycopy(callbacks, 0, next, 0, index);
            System.arraycopy(callbacks, index + 1, next, index, (size - index) - 1);
            return next;
        }

        private int indexOf(Consumer<E>[] callbacks, ListenerHandle<E> handle) {
            Consumer<E> callback = handle.callback();
            for (int i = 0; i < callbacks.length; i++) {
                if (callbacks[i] == callback && this.handles[i] == handle) {
                    return i;
                }
            }
            return -1;
        }

        ListenerHandle<E> of(Consumer<E>[] callbacks, Consumer<E> callback) {
            for (int i = 0; i < callbacks.length; i++) {
                if (callbacks[i] == callback) {
                    return this.handles[i];
                }
            }
            return null;
        }
    }

    public synchronized void register(ListenerHandle<E> handle) {
        if (handle.stage() == Stage.PRE) {
            this.pre = this.preHandles.insert(this.pre, handle);
        } else {
            this.post = this.postHandles.insert(this.post, handle);
        }
    }

    public synchronized void unregister(ListenerHandle<E> handle) {
        if (handle.stage() == Stage.PRE) {
            this.pre = this.preHandles.remove(this.pre, handle);
        } else {
            this.post = this.postHandles.remove(this.post, handle);
        }
    }

    public void dispatchPre(E event) {
        Consumer<E>[] cbs = this.pre;
        for (int i = 0; i < cbs.length; i++) {
            try {
                cbs[i].accept(event);
            } catch (Exception ex) {
                ListenerErrorReporter.report(handleOf(Stage.PRE, cbs[i]), event, ex);
            }
        }
    }

    public void dispatchPost(E event) {
        Consumer<E>[] cbs = this.post;
        for (int i = 0; i < cbs.length; i++) {
            try {
                cbs[i].accept(event);
            } catch (Exception ex) {
                ListenerErrorReporter.report(handleOf(Stage.POST, cbs[i]), event, ex);
            }
        }
    }

    Consumer<E>[] preCallbacks() {
        return this.pre;
    }

    Consumer<E>[] postCallbacks() {
        return this.post;
    }

    synchronized ListenerHandle<E> handleOf(Stage stage, Consumer<E> callback) {
        ListenerHandle<E> listenerHandleOf;
        if (stage == Stage.PRE) {
            listenerHandleOf = this.preHandles.of(this.pre, callback);
        } else {
            listenerHandleOf = this.postHandles.of(this.post, callback);
        }
        ListenerHandle<E> handle = listenerHandleOf;
        return handle != null ? handle : new ListenerHandle<>(null, stage, EventPriority.NORMAL, null, callback);
    }

    private static <E extends Event> Consumer<E>[] none() {
        return NONE;
    }
}
