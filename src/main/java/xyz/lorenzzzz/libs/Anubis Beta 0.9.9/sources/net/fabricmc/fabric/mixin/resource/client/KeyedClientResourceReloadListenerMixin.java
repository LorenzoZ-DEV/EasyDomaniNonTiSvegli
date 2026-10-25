package net.fabricmc.fabric.mixin.resource.client;

import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys;
import net.fabricmc.fabric.impl.resource.FabricResourceReloader;
import net.minecraft.class_10151;
import net.minecraft.class_10201;
import net.minecraft.class_1060;
import net.minecraft.class_1069;
import net.minecraft.class_1070;
import net.minecraft.class_1076;
import net.minecraft.class_10831;
import net.minecraft.class_1092;
import net.minecraft.class_11327;
import net.minecraft.class_1144;
import net.minecraft.class_11697;
import net.minecraft.class_11939;
import net.minecraft.class_2960;
import net.minecraft.class_378;
import net.minecraft.class_4008;
import net.minecraft.class_5407;
import net.minecraft.class_6877;
import net.minecraft.class_761;
import net.minecraft.class_776;
import net.minecraft.class_824;
import net.minecraft.class_898;
import net.minecraft.class_9955;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v1-1.0.10+78c8b4663e.jar:net/fabricmc/fabric/mixin/resource/client/KeyedClientResourceReloadListenerMixin.class */
@Mixin({class_11697.class, class_1092.class, class_824.class, class_776.class, class_9955.class, class_10201.class, class_898.class, class_10831.class, class_1070.class, class_378.class, class_1069.class, class_1076.class, class_11939.class, class_10151.class, class_4008.class, class_1144.class, class_1060.class, class_11327.class, class_761.class, class_5407.class, class_6877.class})
@Environment(EnvType.CLIENT)
public abstract class KeyedClientResourceReloadListenerMixin implements FabricResourceReloader {

    @Unique
    private class_2960 fabric$id;

    @Override // net.fabricmc.fabric.impl.resource.FabricResourceReloader
    public class_2960 fabric$getId() {
        if (this.fabric$id == null) {
            if (this instanceof class_11697) {
                this.fabric$id = ResourceReloaderKeys.Client.ATLAS;
            } else if (this instanceof class_1092) {
                this.fabric$id = ResourceReloaderKeys.Client.MODELS;
            } else if (this instanceof class_824) {
                this.fabric$id = ResourceReloaderKeys.Client.BLOCK_ENTITY_RENDERERS;
            } else if (this instanceof class_776) {
                this.fabric$id = ResourceReloaderKeys.Client.BLOCK_RENDER_MANAGER;
            } else if (this instanceof class_9955) {
                this.fabric$id = ResourceReloaderKeys.Client.CLOUD_CELLS;
            } else if (this instanceof class_10831) {
                this.fabric$id = ResourceReloaderKeys.Client.DRY_FOLIAGE_COLORMAP;
            } else if (this instanceof class_10201) {
                this.fabric$id = ResourceReloaderKeys.Client.EQUIPMENT_MODELS;
            } else if (this instanceof class_898) {
                this.fabric$id = ResourceReloaderKeys.Client.ENTITY_RENDERERS;
            } else if (this instanceof class_378) {
                this.fabric$id = ResourceReloaderKeys.Client.FONTS;
            } else if (this instanceof class_1070) {
                this.fabric$id = ResourceReloaderKeys.Client.FOLIAGE_COLORMAP;
            } else if (this instanceof class_1069) {
                this.fabric$id = ResourceReloaderKeys.Client.GRASS_COLORMAP;
            } else if (this instanceof class_1076) {
                this.fabric$id = ResourceReloaderKeys.Client.LANGUAGES;
            } else if (this instanceof class_11939) {
                this.fabric$id = ResourceReloaderKeys.Client.PARTICLES;
            } else if (this instanceof class_10151) {
                this.fabric$id = ResourceReloaderKeys.Client.SHADERS;
            } else if (this instanceof class_4008) {
                this.fabric$id = ResourceReloaderKeys.Client.SPLASH_TEXTS;
            } else if (this instanceof class_1144) {
                this.fabric$id = ResourceReloaderKeys.Client.SOUNDS;
            } else if (this instanceof class_1060) {
                this.fabric$id = ResourceReloaderKeys.Client.TEXTURES;
            } else if (this instanceof class_11327) {
                this.fabric$id = ResourceReloaderKeys.Client.WAYPOINT_STYLE_ASSETS;
            } else {
                this.fabric$id = class_2960.method_60656("private/" + getClass().getSimpleName().toLowerCase(Locale.ROOT));
            }
        }
        return this.fabric$id;
    }
}
