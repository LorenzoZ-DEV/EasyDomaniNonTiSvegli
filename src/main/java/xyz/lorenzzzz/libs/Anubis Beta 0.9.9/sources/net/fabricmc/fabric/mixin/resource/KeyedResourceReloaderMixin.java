package net.fabricmc.fabric.mixin.resource;

import java.util.Locale;
import net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys;
import net.fabricmc.fabric.impl.resource.FabricResourceReloader;
import net.minecraft.class_1863;
import net.minecraft.class_2960;
import net.minecraft.class_2989;
import net.minecraft.class_5349;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/mixin/resource/KeyedResourceReloaderMixin.class */
@Mixin({class_1863.class, class_2989.class, class_5349.class})
public abstract class KeyedResourceReloaderMixin implements FabricResourceReloader {

    @Unique
    private class_2960 id;

    @Override // net.fabricmc.fabric.impl.resource.FabricResourceReloader
    public class_2960 fabric$getId() {
        if (this.id == null) {
            if (this instanceof class_1863) {
                this.id = ResourceReloaderKeys.Server.RECIPES;
            } else if (this instanceof class_2989) {
                this.id = ResourceReloaderKeys.Server.ADVANCEMENTS;
            } else if (this instanceof class_5349) {
                this.id = ResourceReloaderKeys.Server.FUNCTIONS;
            } else {
                this.id = class_2960.method_60656("private/" + getClass().getSimpleName().toLowerCase(Locale.ROOT));
            }
        }
        return this.id;
    }
}
