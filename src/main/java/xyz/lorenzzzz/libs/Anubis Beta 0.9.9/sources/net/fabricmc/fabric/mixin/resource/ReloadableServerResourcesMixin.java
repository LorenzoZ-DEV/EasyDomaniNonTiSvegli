package net.fabricmc.fabric.mixin.resource;

import com.llamalad7.mixinextras.sugar.Local;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.fabricmc.fabric.api.resource.v1.DataResourceStore;
import net.fabricmc.fabric.impl.resource.DataResourceStoreImpl;
import net.fabricmc.fabric.impl.resource.FabricDataResourceStoreHolder;
import net.fabricmc.fabric.impl.resource.SetupMarkerResourceReloader;
import net.minecraft.class_3302;
import net.minecraft.class_5350;
import net.minecraft.class_7699;
import net.minecraft.class_9383;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/mixin/resource/ReloadableServerResourcesMixin.class */
@Mixin({class_5350.class})
public class ReloadableServerResourcesMixin implements FabricDataResourceStoreHolder {

    @Unique
    private final DataResourceStore.Mutable dataResourceStore = new DataResourceStoreImpl();

    @ModifyArg(method = {"method_58296(Lnet/minecraft/class_7699;Lnet/minecraft/class_2170$class_5364;Ljava/util/List;Lnet/minecraft/class_12096;Lnet/minecraft/class_3300;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Lnet/minecraft/class_9383$class_9842;)Ljava/util/concurrent/CompletionStage;"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_4014;method_40087(Lnet/minecraft/class_3300;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Z)Lnet/minecraft/class_4011;"))
    private static List<class_3302> onSetupDataReloaders(List<class_3302> reloaders, @Local(argsOnly = true) class_9383.class_9842 loadResult, @Local(argsOnly = true) class_7699 featureSet, @Local class_5350 dataPackContents) {
        ArrayList<class_3302> list = new ArrayList<>(reloaders);
        list.addFirst(new SetupMarkerResourceReloader(dataPackContents, loadResult.comp_2899(), featureSet));
        return Collections.unmodifiableList(list);
    }

    @Override // net.fabricmc.fabric.impl.resource.FabricDataResourceStoreHolder
    public DataResourceStore.Mutable fabric$getDataResourceStore() {
        return this.dataResourceStore;
    }
}
