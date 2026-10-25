package net.fabricmc.fabric.impl.resource.pack;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import net.fabricmc.fabric.api.resource.v1.pack.ModPackResources;
import net.minecraft.class_3262;
import net.minecraft.class_3288;
import net.minecraft.class_8614;
import net.minecraft.class_9224;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/impl/resource/pack/ModPackResourcesFactory.class */
public final class ModPackResourcesFactory extends Record implements class_3288.class_7680 {
    private final ModPackResources pack;

    public ModPackResourcesFactory(ModPackResources pack) {
        this.pack = pack;
    }

    @Override // java.lang.Record
    public final String toString() {
        return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, ModPackResourcesFactory.class), ModPackResourcesFactory.class, "pack", "FIELD:Lnet/fabricmc/fabric/impl/resource/pack/ModPackResourcesFactory;->pack:Lnet/fabricmc/fabric/api/resource/v1/pack/ModPackResources;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, ModPackResourcesFactory.class), ModPackResourcesFactory.class, "pack", "FIELD:Lnet/fabricmc/fabric/impl/resource/pack/ModPackResourcesFactory;->pack:Lnet/fabricmc/fabric/api/resource/v1/pack/ModPackResources;").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final boolean equals(Object o) {
        return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, ModPackResourcesFactory.class, Object.class), ModPackResourcesFactory.class, "pack", "FIELD:Lnet/fabricmc/fabric/impl/resource/pack/ModPackResourcesFactory;->pack:Lnet/fabricmc/fabric/api/resource/v1/pack/ModPackResources;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
    }

    public ModPackResources pack() {
        return this.pack;
    }

    public class_3262 method_52424(class_9224 location) {
        return this.pack;
    }

    public class_3262 method_52425(class_9224 location, class_3288.class_7679 metadata) {
        if (metadata.comp_1584().isEmpty()) {
            return this.pack;
        }
        ArrayList<class_3262> overlays = new ArrayList<>(metadata.comp_1584().size());
        for (String overlay : metadata.comp_1584()) {
            overlays.add(this.pack.createOverlay(overlay));
        }
        return new class_8614(this.pack, overlays);
    }
}
