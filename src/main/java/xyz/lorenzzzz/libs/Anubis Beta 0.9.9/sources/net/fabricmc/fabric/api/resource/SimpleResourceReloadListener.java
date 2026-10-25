package net.fabricmc.fabric.api.resource;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.Function;
import net.minecraft.class_3300;
import net.minecraft.class_3302;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v0-3.3.4+4fc5413f3e.jar:net/fabricmc/fabric/api/resource/SimpleResourceReloadListener.class */
@Deprecated
public interface SimpleResourceReloadListener<T> extends IdentifiableResourceReloadListener {
    CompletableFuture<T> load(class_3300 class_3300Var, Executor executor);

    CompletableFuture<Void> apply(T t, class_3300 class_3300Var, Executor executor);

    default CompletableFuture<Void> method_25931(class_3302.class_11558 store, Executor loadExecutor, class_3302.class_4045 helper, Executor applyExecutor) {
        CompletableFuture<T> completableFutureLoad = load(store.method_72361(), loadExecutor);
        Objects.requireNonNull(helper);
        return completableFutureLoad.thenCompose((Function) helper::method_18352).thenCompose((Function<? super U, ? extends CompletionStage<U>>) o -> {
            return apply(o, store.method_72361(), applyExecutor);
        });
    }
}
