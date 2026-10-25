package net.fabricmc.fabric.mixin.event.lifecycle;

import com.llamalad7.mixinextras.sugar.Local;
import java.util.Map;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-lifecycle-events-v1-2.6.15+4ebb5c083e.jar:net/fabricmc/fabric/mixin/event/lifecycle/LivingEntityMixin.class */
@Mixin({class_1309.class})
public abstract class LivingEntityMixin {
    @Inject(method = {"method_30129()Ljava/util/Map;"}, at = {@At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;")})
    private void getEquipmentChanges(CallbackInfoReturnable<Map<class_1304, class_1799>> cir, @Local class_1304 equipmentSlot, @Local(ordinal = 0) class_1799 previousStack, @Local(ordinal = 1) class_1799 currentStack) {
        ServerEntityEvents.EQUIPMENT_CHANGE.invoker().onChange((class_1309) this, equipmentSlot, previousStack, currentStack);
    }
}
