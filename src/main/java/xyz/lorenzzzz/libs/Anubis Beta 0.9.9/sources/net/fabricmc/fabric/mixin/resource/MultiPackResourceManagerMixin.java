package net.fabricmc.fabric.mixin.resource;

import java.util.List;
import net.fabricmc.fabric.impl.resource.FabricLifecycledResourceManager;
import net.minecraft.class_3262;
import net.minecraft.class_3264;
import net.minecraft.class_6861;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/mixin/resource/MultiPackResourceManagerMixin.class */
@Mixin({class_6861.class})
public class MultiPackResourceManagerMixin implements FabricLifecycledResourceManager {

    @Unique
    private class_3264 resourceType;

    @Inject(method = {"<init>(Lnet/minecraft/class_3264;Ljava/util/List;)V"}, at = {@At("TAIL")})
    private void init(class_3264 resourceType, List<class_3262> list, CallbackInfo ci) {
        this.resourceType = resourceType;
    }

    @Override // net.fabricmc.fabric.impl.resource.FabricLifecycledResourceManager
    public class_3264 fabric$getResourceType() {
        return this.resourceType;
    }
}
