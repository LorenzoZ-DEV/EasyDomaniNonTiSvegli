package net.fabricmc.fabric.api.resource.v1;

import java.util.function.Function;
import net.fabricmc.fabric.impl.resource.DataResourceLoaderImpl;
import net.minecraft.class_1863;
import net.minecraft.class_2960;
import net.minecraft.class_2989;
import net.minecraft.class_3302;
import net.minecraft.class_7225;
import org.jetbrains.annotations.ApiStatus;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/api/resource/v1/DataResourceLoader.class */
@ApiStatus.NonExtendable
public interface DataResourceLoader extends ResourceLoader {
    public static final class_3302.class_11559<class_1863> RECIPE_MANAGER_KEY = new class_3302.class_11559<>();
    public static final class_3302.class_11559<class_2989> ADVANCEMENT_LOADER_KEY = new class_3302.class_11559<>();
    public static final class_3302.class_11559<DataResourceStore.Mutable> DATA_RESOURCE_STORE_KEY = new class_3302.class_11559<>();

    void registerReloader(class_2960 class_2960Var, Function<class_7225.class_7874, class_3302> function);

    static DataResourceLoader get() {
        return DataResourceLoaderImpl.INSTANCE;
    }
}
