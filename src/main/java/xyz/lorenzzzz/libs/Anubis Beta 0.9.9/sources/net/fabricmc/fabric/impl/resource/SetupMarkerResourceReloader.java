package net.fabricmc.fabric.impl.resource;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import net.fabricmc.fabric.api.resource.v1.DataResourceLoader;
import net.minecraft.class_3300;
import net.minecraft.class_3302;
import net.minecraft.class_4013;
import net.minecraft.class_5350;
import net.minecraft.class_7225;
import net.minecraft.class_7699;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/impl/resource/SetupMarkerResourceReloader.class */
public final class SetupMarkerResourceReloader extends Record implements class_4013 {
    private final class_5350 dataPackContents;
    private final class_7225.class_7874 registries;
    private final class_7699 featureSet;

    public SetupMarkerResourceReloader(class_5350 dataPackContents, class_7225.class_7874 registries, class_7699 featureSet) {
        this.dataPackContents = dataPackContents;
        this.registries = registries;
        this.featureSet = featureSet;
    }

    @Override // java.lang.Record
    public final String toString() {
        return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, SetupMarkerResourceReloader.class), SetupMarkerResourceReloader.class, "dataPackContents;registries;featureSet", "FIELD:Lnet/fabricmc/fabric/impl/resource/SetupMarkerResourceReloader;->dataPackContents:Lnet/minecraft/class_5350;", "FIELD:Lnet/fabricmc/fabric/impl/resource/SetupMarkerResourceReloader;->registries:Lnet/minecraft/class_7225$class_7874;", "FIELD:Lnet/fabricmc/fabric/impl/resource/SetupMarkerResourceReloader;->featureSet:Lnet/minecraft/class_7699;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, SetupMarkerResourceReloader.class), SetupMarkerResourceReloader.class, "dataPackContents;registries;featureSet", "FIELD:Lnet/fabricmc/fabric/impl/resource/SetupMarkerResourceReloader;->dataPackContents:Lnet/minecraft/class_5350;", "FIELD:Lnet/fabricmc/fabric/impl/resource/SetupMarkerResourceReloader;->registries:Lnet/minecraft/class_7225$class_7874;", "FIELD:Lnet/fabricmc/fabric/impl/resource/SetupMarkerResourceReloader;->featureSet:Lnet/minecraft/class_7699;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final boolean equals(Object o) {
        return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, SetupMarkerResourceReloader.class, Object.class), SetupMarkerResourceReloader.class, "dataPackContents;registries;featureSet", "FIELD:Lnet/fabricmc/fabric/impl/resource/SetupMarkerResourceReloader;->dataPackContents:Lnet/minecraft/class_5350;", "FIELD:Lnet/fabricmc/fabric/impl/resource/SetupMarkerResourceReloader;->registries:Lnet/minecraft/class_7225$class_7874;", "FIELD:Lnet/fabricmc/fabric/impl/resource/SetupMarkerResourceReloader;->featureSet:Lnet/minecraft/class_7699;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
    }

    public class_5350 dataPackContents() {
        return this.dataPackContents;
    }

    public class_7225.class_7874 registries() {
        return this.registries;
    }

    public class_7699 featureSet() {
        return this.featureSet;
    }

    public void prepareSharedState(class_3302.class_11558 store) {
        store.method_72363(DataResourceLoader.RELOADER_REGISTRY_LOOKUP_KEY, this.registries);
        store.method_72363(DataResourceLoader.RELOADER_FEATURE_SET_KEY, this.featureSet);
        store.method_72363(DataResourceLoader.ADVANCEMENT_LOADER_KEY, this.dataPackContents.method_29473());
        store.method_72363(DataResourceLoader.RECIPE_MANAGER_KEY, this.dataPackContents.method_29471());
        store.method_72363(DataResourceLoader.DATA_RESOURCE_STORE_KEY, this.dataPackContents.fabric$getDataResourceStore());
    }

    public void method_14491(class_3300 manager) {
    }
}
