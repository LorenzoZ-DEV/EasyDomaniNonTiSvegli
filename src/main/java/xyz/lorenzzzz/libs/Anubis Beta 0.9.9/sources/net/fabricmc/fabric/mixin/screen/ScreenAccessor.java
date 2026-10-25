package net.fabricmc.fabric.mixin.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-screen-api-v1-3.1.7+4ebb5c083e.jar:net/fabricmc/fabric/mixin/screen/ScreenAccessor.class */
@Mixin({class_437.class})
@Environment(EnvType.CLIENT)
public interface ScreenAccessor {
    @Accessor("field_22787")
    class_310 getClient();
}
