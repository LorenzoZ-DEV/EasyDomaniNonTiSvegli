package net.fabricmc.fabric.mixin.event.lifecycle;

import java.util.List;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.minecraft.class_12096;
import net.minecraft.class_2170;
import net.minecraft.class_5350;
import net.minecraft.class_5455;
import net.minecraft.class_7225;
import net.minecraft.class_7659;
import net.minecraft.class_7699;
import net.minecraft.class_7780;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/mixin/event/lifecycle/ReloadableServerResourcesMixin.class */
@Mixin({class_5350.class})
public class ReloadableServerResourcesMixin {

    @Unique
    private class_5455 dynamicRegistryManager;

    @Inject(method = {"<init>(Lnet/minecraft/class_7780;Lnet/minecraft/class_7225$class_7874;Lnet/minecraft/class_7699;Lnet/minecraft/class_2170$class_5364;Ljava/util/List;Lnet/minecraft/class_12096;)V"}, at = {@At("TAIL")})
    private void init(class_7780<class_7659> combinedDynamicRegistries, class_7225.class_7874 registries, class_7699 enabledFeatures, class_2170.class_5364 environment, List pendingTagLoads, class_12096 permissionPredicate, CallbackInfo ci) {
        this.dynamicRegistryManager = combinedDynamicRegistries.method_45926();
    }

    @Inject(method = {"method_61248()V"}, at = {@At("TAIL")})
    private void hookRefresh(CallbackInfo ci) {
        CommonLifecycleEvents.TAGS_LOADED.invoker().onTagsLoaded(this.dynamicRegistryManager, false);
    }
}
