package net.fabricmc.fabric.mixin.client.keybinding;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.keybinding.CategoryComparator;
import net.minecraft.class_2960;
import net.minecraft.class_304;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-key-binding-api-v1-1.1.7+4fc5413f3e.jar:net/fabricmc/fabric/mixin/client/keybinding/KeyMappingCategoryMixin.class */
@Mixin({class_304.class_11900.class})
@Environment(EnvType.CLIENT)
abstract class KeyMappingCategoryMixin {

    @Shadow
    @Final
    static List<class_304.class_11900> field_62925;

    KeyMappingCategoryMixin() {
    }

    @Inject(method = {"method_74698(Lnet/minecraft/class_2960;)Lnet/minecraft/class_304$class_11900;"}, at = {@At("RETURN")})
    private static void onReturnRegister(class_2960 id, CallbackInfoReturnable<class_304.class_11900> cir) {
        field_62925.sort(CategoryComparator.INSTANCE);
    }
}
