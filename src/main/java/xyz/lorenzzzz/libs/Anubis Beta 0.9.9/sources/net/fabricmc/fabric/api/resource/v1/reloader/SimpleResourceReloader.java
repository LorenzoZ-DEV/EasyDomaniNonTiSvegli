package net.fabricmc.fabric.api.resource.v1.reloader;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import net.minecraft.class_3302;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/api/resource/v1/reloader/SimpleResourceReloader.class */
public abstract class SimpleResourceReloader<T> implements class_3302 {
    protected abstract T prepare(class_3302.class_11558 class_11558Var);

    protected abstract void apply(T t, class_3302.class_11558 class_11558Var);

    public final CompletableFuture<Void> method_25931(class_3302.class_11558 store, Executor prepareExecutor, class_3302.class_4045 reloadSynchronizer, Executor applyExecutor) {
        CompletableFuture<T> prepareStep = CompletableFuture.supplyAsync(() -> {
            return prepare(store);
        }, prepareExecutor);
        Objects.requireNonNull(reloadSynchronizer);
        Objects.requireNonNull(reloadSynchronizer);
        return prepareStep.thenCompose(reloadSynchronizer::method_18352).thenAcceptAsync((Consumer) prepared -> {
            apply(prepared, store);
        }, applyExecutor);
    }
}
