package net.fabricmc.fabric.mixin.resource;

import java.util.List;
import net.fabricmc.fabric.impl.resource.pack.ModPackResourcesUtil;
import net.minecraft.class_5359;
import net.minecraft.class_6306;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/mixin/resource/GameTestServerMixin.class */
@Mixin({class_6306.class})
public class GameTestServerMixin {
    @Redirect(method = {"method_40378(Ljava/lang/Thread;Lnet/minecraft/class_32$class_5143;Lnet/minecraft/class_3283;Ljava/util/Optional;Z)Lnet/minecraft/class_6306;"}, at = @At(value = "NEW", target = "(Ljava/util/List;Ljava/util/List;)Lnet/minecraft/class_5359;"))
    private static class_5359 replaceDefaultDataPackSettings(List<String> enabled, List<String> disabled) {
        return ModPackResourcesUtil.createTestServerSettings(enabled, disabled);
    }
}
