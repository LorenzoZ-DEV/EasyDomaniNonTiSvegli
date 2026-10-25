package net.fabricmc.fabric.mixin.resource;

import java.util.Set;
import java.util.function.Predicate;
import net.fabricmc.fabric.impl.resource.PackSourceTracker;
import net.fabricmc.fabric.impl.resource.pack.FabricPack;
import net.minecraft.class_3262;
import net.minecraft.class_3288;
import net.minecraft.class_9224;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/mixin/resource/PackMixin.class */
@Mixin({class_3288.class})
abstract class PackMixin implements FabricPack {

    @Unique
    private static final Predicate<Set<String>> DEFAULT_PARENT_PREDICATE = parents -> {
        return true;
    };

    @Unique
    private Predicate<Set<String>> parentsPredicate = DEFAULT_PARENT_PREDICATE;

    @Shadow
    public abstract class_9224 method_56933();

    PackMixin() {
    }

    @Inject(method = {"method_14458()Lnet/minecraft/class_3262;"}, at = {@At("RETURN")})
    private void onCreateResourcePack(CallbackInfoReturnable<class_3262> cir) {
        PackSourceTracker.setSource((class_3262) cir.getReturnValue(), method_56933().comp_2331());
    }

    @Override // net.fabricmc.fabric.impl.resource.pack.FabricPack
    public boolean fabric$isHidden() {
        return this.parentsPredicate != DEFAULT_PARENT_PREDICATE;
    }

    @Override // net.fabricmc.fabric.impl.resource.pack.FabricPack
    public boolean fabric$parentsEnabled(Set<String> enabled) {
        return this.parentsPredicate.test(enabled);
    }

    @Override // net.fabricmc.fabric.impl.resource.pack.FabricPack
    public void fabric$setParentsPredicate(Predicate<Set<String>> predicate) {
        this.parentsPredicate = predicate;
    }
}
