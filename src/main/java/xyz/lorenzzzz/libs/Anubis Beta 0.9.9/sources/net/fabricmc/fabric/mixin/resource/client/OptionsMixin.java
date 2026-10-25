package net.fabricmc.fabric.mixin.resource.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.resource.client.DefaultResourcePackStorage;
import net.fabricmc.fabric.impl.resource.pack.FabricPack;
import net.minecraft.class_315;
import net.minecraft.class_3288;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/mixin/resource/client/OptionsMixin.class */
@Mixin({class_315.class})
@Environment(EnvType.CLIENT)
public class OptionsMixin {

    @Shadow
    public List<String> field_1887;

    @Inject(method = {"method_1636()V"}, at = {@At("RETURN")})
    private void onLoad(CallbackInfo ci) {
        this.field_1887 = DefaultResourcePackStorage.process(this.field_1887);
    }

    @WrapOperation(method = {"method_49598(Lnet/minecraft/class_3283;)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/class_3288;method_14465()Z")})
    private boolean excludeInternalResourcePacksFromRefreshCheck(class_3288 instance, Operation<Boolean> original) {
        return ((Boolean) original.call(new Object[]{instance})).booleanValue() || ((FabricPack) instance).fabric$isHidden();
    }
}
