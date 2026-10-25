package net.fabricmc.fabric.mixin.resource;

import net.fabricmc.fabric.impl.resource.pack.ModPackResourcesUtil;
import net.minecraft.class_3806;
import net.minecraft.class_7712;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/mixin/resource/DedicatedServerPropertiesMixin.class */
@Mixin({class_3806.class})
public class DedicatedServerPropertiesMixin {
    @Redirect(method = {"<init>(Ljava/util/Properties;)V"}, at = @At(value = "FIELD", target = "Lnet/minecraft/class_7712;field_40260:Lnet/minecraft/class_7712;"))
    private class_7712 replaceDefaultDataConfiguration() {
        return ModPackResourcesUtil.createDefaultDataConfiguration();
    }
}
