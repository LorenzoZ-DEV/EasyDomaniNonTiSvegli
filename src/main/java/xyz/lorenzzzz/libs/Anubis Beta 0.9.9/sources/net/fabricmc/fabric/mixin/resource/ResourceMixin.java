package net.fabricmc.fabric.mixin.resource;

import net.fabricmc.fabric.api.resource.v1.FabricResource;
import net.fabricmc.fabric.impl.resource.PackSourceTracker;
import net.minecraft.class_3298;
import net.minecraft.class_5352;
import org.spongepowered.asm.mixin.Mixin;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/mixin/resource/ResourceMixin.class */
@Mixin({class_3298.class})
class ResourceMixin implements FabricResource {
    ResourceMixin() {
    }

    @Override // net.fabricmc.fabric.api.resource.v1.FabricResource
    public class_5352 getFabricPackSource() {
        class_3298 self = (class_3298) this;
        return PackSourceTracker.getSource(self.method_45304());
    }
}
