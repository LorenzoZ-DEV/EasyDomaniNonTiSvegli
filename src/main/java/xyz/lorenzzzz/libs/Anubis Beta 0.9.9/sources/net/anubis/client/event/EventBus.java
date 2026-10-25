package net.anubis.client.event;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/event/EventBus.class */
@Environment(EnvType.CLIENT)
public final class EventBus {
    private final Map<Object, List<RegistrationEntry<?>>> subscriptions = new IdentityHashMap();
    private final ArrayBlockingQueue<Runnable> asyncQueue = new ArrayBlockingQueue<>(4096);
    private volatile long failedTasks;
    public static final EventBus INSTANCE = new EventBus();
    private static final MethodType FACTORY_TYPE = MethodType.methodType((Class<?>) Listener.class, (Class<?>) Object.class);
    private static final ClassValue<List<HandlerSpec>> HANDLER_SPECS = new ClassValue<List<HandlerSpec>>() { // from class: net.anubis.client.event.EventBus.1
        @Override // java.lang.ClassValue
        protected /* bridge */ /* synthetic */ List<HandlerSpec> computeValue(Class cls) {
            return computeValue((Class<?>) cls);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ClassValue
        protected List<HandlerSpec> computeValue(Class<?> clazz) {
            return EventBus.scan(clazz);
        }
    };
    private static final ClassValue<EventType<?>> TYPE_CACHE = new ClassValue<EventType<?>>() { // from class: net.anubis.client.event.EventBus.2
        @Override // java.lang.ClassValue
        protected /* bridge */ /* synthetic */ EventType<?> computeValue(Class cls) {
            return computeValue((Class<?>) cls);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ClassValue
        protected EventType<?> computeValue(Class<?> eventClass) {
            try {
                Field f = eventClass.getField("TYPE");
                if (!EventType.class.isAssignableFrom(f.getType())) {
                    throw new IllegalArgumentException(eventClass.getName() + ".TYPE must be an EventType instance.");
                }
                return (EventType) f.get(null);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Cannot access " + eventClass.getName() + ".TYPE", e);
            } catch (NoSuchFieldException e2) {
                throw new IllegalArgumentException("Event class " + eventClass.getName() + " must declare 'public static final EventType<...> TYPE'.", e2);
            }
        }
    };

    public boolean postAsync(Runnable task) {
        return this.asyncQueue.offer((Runnable) Objects.requireNonNull(task));
    }

    public int drainAsync() {
        return drainAsync(512, 2000000L);
    }

    public int drainAsync(int maxTasks, long maxNanos) {
        Runnable task;
        if (maxTasks <= 0 || maxNanos <= 0) {
            throw new IllegalArgumentException("Drain limits must be positive");
        }
        long started = System.nanoTime();
        int processed = 0;
        while (processed < maxTasks && System.nanoTime() - started < maxNanos && (task = this.asyncQueue.poll()) != null) {
            processed++;
            try {
                task.run();
            } catch (RuntimeException failure) {
                long count = this.failedTasks + 1;
                this.failedTasks = count;
                if (count <= 4 || count % 256 == 0) {
                    System.err.println("[AnubisEventBus] Async task failed (#" + count + ")");
                    failure.printStackTrace();
                }
            }
        }
        return processed;
    }

    public <E extends Event> void off(ListenerHandle<E> handle) {
        handle.unregister();
    }

    public int register(Object subscriber) {
        int iRegister;
        List<HandlerSpec> specs = HANDLER_SPECS.get(subscriber.getClass());
        if (specs.isEmpty()) {
            return 0;
        }
        synchronized (this) {
            iRegister = register(subscriber, specs);
        }
        return iRegister;
    }

    private int register(Object subscriber, List<HandlerSpec> specs) {
        if (this.subscriptions.containsKey(subscriber)) {
            throw new IllegalStateException("Subscriber already registered: " + subscriber.getClass().getName());
        }
        List<RegistrationEntry<?>> entries = buildEntries(subscriber, specs);
        try {
            for (RegistrationEntry<?> entry : entries) {
                entry.register();
            }
            this.subscriptions.put(subscriber, entries);
            return entries.size();
        } catch (Error | RuntimeException failure) {
            for (RegistrationEntry<?> entry2 : entries) {
                try {
                    entry2.unregister();
                } catch (Error | RuntimeException cleanupFailure) {
                    if (failure != cleanupFailure) {
                        failure.addSuppressed(cleanupFailure);
                    }
                }
            }
            throw failure;
        }
    }

    public synchronized void unregister(Object subscriber) {
        List<RegistrationEntry<?>> entries = this.subscriptions.remove(subscriber);
        if (entries == null) {
            return;
        }
        for (RegistrationEntry<?> entry : entries) {
            entry.unregister();
        }
    }

    private static List<RegistrationEntry<?>> buildEntries(Object subscriber, List<HandlerSpec> specs) {
        List<RegistrationEntry<?>> entries = new ArrayList<>(specs.size());
        for (HandlerSpec spec : specs) {
            Listener<Event> listener = spec.staticListener();
            if (listener == null) {
                try {
                    listener = castListener((Listener) spec.listenerFactory().invokeExact(subscriber));
                } catch (Throwable t) {
                    throw new RuntimeException("Failed to bind listener for " + subscriber.getClass().getName(), t);
                }
            }
            EventType<Event> eventTypeType = spec.type();
            Stage stage = spec.stage();
            EventPriority eventPriorityPriority = spec.priority();
            Listener<Event> listener2 = listener;
            Objects.requireNonNull(listener2);
            ListenerHandle<Event> handle = new ListenerHandle<>(eventTypeType, stage, eventPriorityPriority, subscriber, listener2::invoke);
            entries.add(new RegistrationEntry<>(spec.type(), handle));
        }
        return entries;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static Listener<Event> castListener(Listener<?> listener) {
        return listener;
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/event/EventBus$HandlerSpec.class */
    @Environment(EnvType.CLIENT)
    private static final class HandlerSpec extends Record {
        private final EventType<Event> type;
        private final Stage stage;
        private final EventPriority priority;
        private final MethodHandle listenerFactory;
        private final Listener<Event> staticListener;

        private HandlerSpec(EventType<Event> type, Stage stage, EventPriority priority, MethodHandle listenerFactory, Listener<Event> staticListener) {
            this.type = type;
            this.stage = stage;
            this.priority = priority;
            this.listenerFactory = listenerFactory;
            this.staticListener = staticListener;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, HandlerSpec.class), HandlerSpec.class, "type;stage;priority;listenerFactory;staticListener", "FIELD:Lnet/anubis/client/event/EventBus$HandlerSpec;->type:Lnet/anubis/client/event/EventType;", "FIELD:Lnet/anubis/client/event/EventBus$HandlerSpec;->stage:Lnet/anubis/client/event/Stage;", "FIELD:Lnet/anubis/client/event/EventBus$HandlerSpec;->priority:Lnet/anubis/client/event/EventPriority;", "FIELD:Lnet/anubis/client/event/EventBus$HandlerSpec;->listenerFactory:Ljava/lang/invoke/MethodHandle;", "FIELD:Lnet/anubis/client/event/EventBus$HandlerSpec;->staticListener:Lnet/anubis/client/event/Listener;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, HandlerSpec.class), HandlerSpec.class, "type;stage;priority;listenerFactory;staticListener", "FIELD:Lnet/anubis/client/event/EventBus$HandlerSpec;->type:Lnet/anubis/client/event/EventType;", "FIELD:Lnet/anubis/client/event/EventBus$HandlerSpec;->stage:Lnet/anubis/client/event/Stage;", "FIELD:Lnet/anubis/client/event/EventBus$HandlerSpec;->priority:Lnet/anubis/client/event/EventPriority;", "FIELD:Lnet/anubis/client/event/EventBus$HandlerSpec;->listenerFactory:Ljava/lang/invoke/MethodHandle;", "FIELD:Lnet/anubis/client/event/EventBus$HandlerSpec;->staticListener:Lnet/anubis/client/event/Listener;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, HandlerSpec.class, Object.class), HandlerSpec.class, "type;stage;priority;listenerFactory;staticListener", "FIELD:Lnet/anubis/client/event/EventBus$HandlerSpec;->type:Lnet/anubis/client/event/EventType;", "FIELD:Lnet/anubis/client/event/EventBus$HandlerSpec;->stage:Lnet/anubis/client/event/Stage;", "FIELD:Lnet/anubis/client/event/EventBus$HandlerSpec;->priority:Lnet/anubis/client/event/EventPriority;", "FIELD:Lnet/anubis/client/event/EventBus$HandlerSpec;->listenerFactory:Ljava/lang/invoke/MethodHandle;", "FIELD:Lnet/anubis/client/event/EventBus$HandlerSpec;->staticListener:Lnet/anubis/client/event/Listener;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public EventType<Event> type() {
            return this.type;
        }

        public Stage stage() {
            return this.stage;
        }

        public EventPriority priority() {
            return this.priority;
        }

        public MethodHandle listenerFactory() {
            return this.listenerFactory;
        }

        public Listener<Event> staticListener() {
            return this.staticListener;
        }
    }

    private static List<HandlerSpec> scan(Class<?> clazz) {
        List<HandlerSpec> specs = new ArrayList<>();
        Class<?> superclass = clazz;
        while (true) {
            Class<?> parent = superclass;
            if (parent != null) {
                for (Method method : parent.getDeclaredMethods()) {
                    if (isHandler(method) && !Modifier.isPublic(method.getModifiers())) {
                        throw new IllegalArgumentException("Event handler must be public: " + String.valueOf(method));
                    }
                }
                superclass = parent.getSuperclass();
            } else {
                for (Method method2 : clazz.getMethods()) {
                    Stage stage = Stage.PRE;
                    EventPriority priority = EventPriority.NORMAL;
                    boolean matched = false;
                    EventHandler newAnnotation = (EventHandler) method2.getAnnotation(EventHandler.class);
                    if (newAnnotation != null) {
                        stage = newAnnotation.stage();
                        priority = newAnnotation.priority();
                        matched = true;
                    } else {
                        EventListener legacyAnnotation = (EventListener) method2.getAnnotation(EventListener.class);
                        if (legacyAnnotation != null) {
                            stage = Stage.PRE;
                            priority = EventPriority.fromInt(legacyAnnotation.priority());
                            matched = true;
                        }
                    }
                    if (matched) {
                        Class<?>[] params = method2.getParameterTypes();
                        if (params.length != 1 || !Event.class.isAssignableFrom(params[0]) || method2.getReturnType() != Void.TYPE) {
                            throw new IllegalArgumentException("Event handler must return void and accept one Event: " + String.valueOf(method2));
                        }
                        EventType<Event> type = resolveTypeFor(params[0]);
                        if (Modifier.isStatic(method2.getModifiers())) {
                            specs.add(new HandlerSpec(type, stage, priority, null, LambdaListenerFactory.createStatic(method2)));
                        } else {
                            specs.add(new HandlerSpec(type, stage, priority, LambdaListenerFactory.createFactory(method2).asType(FACTORY_TYPE), null));
                        }
                    }
                }
                return specs.isEmpty() ? List.of() : List.copyOf(specs);
            }
        }
    }

    private static boolean isHandler(Method method) {
        return method.isAnnotationPresent(EventHandler.class) || method.isAnnotationPresent(EventListener.class);
    }

    static <E extends Event> EventType<E> resolveTypeFor(Class<E> eventClass) {
        return (EventType) TYPE_CACHE.get(eventClass);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/event/EventBus$RegistrationEntry.class */
    @Environment(EnvType.CLIENT)
    private static final class RegistrationEntry<E extends Event> extends Record {
        private final EventType<E> type;
        private final ListenerHandle<E> handle;

        private RegistrationEntry(EventType<E> type, ListenerHandle<E> handle) {
            this.type = type;
            this.handle = handle;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, RegistrationEntry.class), RegistrationEntry.class, "type;handle", "FIELD:Lnet/anubis/client/event/EventBus$RegistrationEntry;->type:Lnet/anubis/client/event/EventType;", "FIELD:Lnet/anubis/client/event/EventBus$RegistrationEntry;->handle:Lnet/anubis/client/event/ListenerHandle;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, RegistrationEntry.class), RegistrationEntry.class, "type;handle", "FIELD:Lnet/anubis/client/event/EventBus$RegistrationEntry;->type:Lnet/anubis/client/event/EventType;", "FIELD:Lnet/anubis/client/event/EventBus$RegistrationEntry;->handle:Lnet/anubis/client/event/ListenerHandle;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, RegistrationEntry.class, Object.class), RegistrationEntry.class, "type;handle", "FIELD:Lnet/anubis/client/event/EventBus$RegistrationEntry;->type:Lnet/anubis/client/event/EventType;", "FIELD:Lnet/anubis/client/event/EventBus$RegistrationEntry;->handle:Lnet/anubis/client/event/ListenerHandle;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public EventType<E> type() {
            return this.type;
        }

        public ListenerHandle<E> handle() {
            return this.handle;
        }

        void register() {
            this.type.register(this.handle);
        }

        void unregister() {
            this.type.unregister(this.handle);
        }
    }
}
