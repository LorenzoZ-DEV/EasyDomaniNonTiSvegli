package net.fabricmc.fabric.mixin.resource.client;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.resource.pack.FabricPack;
import net.minecraft.class_3283;
import net.minecraft.class_3288;
import net.minecraft.class_5369;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/mixin/resource/client/PackSelectionModelMixin.class */
@Mixin({class_5369.class})
@Environment(EnvType.CLIENT)
public class PackSelectionModelMixin {

    @Shadow
    @Final
    List<class_3288> field_25455;

    @Shadow
    @Final
    List<class_3288> field_25456;

    @Inject(method = {"<init>(Ljava/util/function/Consumer;Ljava/util/function/Function;Lnet/minecraft/class_3283;Ljava/util/function/Consumer;)V"}, at = {@At("TAIL")})
    private void removeHiddenPacksInit(Consumer<class_5369.class_5372> updateCallback, Function iconIdSupplier, class_3283 resourcePackManager, Consumer applier, CallbackInfo ci) {
        this.field_25455.removeIf(profile -> {
            return ((FabricPack) profile).fabric$isHidden();
        });
        this.field_25456.removeIf(profile2 -> {
            return ((FabricPack) profile2).fabric$isHidden();
        });
    }

    @Inject(method = {"method_29981()V"}, at = {@At("TAIL")})
    private void removeHiddenPacksRefresh(CallbackInfo ci) {
        this.field_25455.removeIf(profile -> {
            return ((FabricPack) profile).fabric$isHidden();
        });
        this.field_25456.removeIf(profile2 -> {
            return ((FabricPack) profile2).fabric$isHidden();
        });
    }
}
