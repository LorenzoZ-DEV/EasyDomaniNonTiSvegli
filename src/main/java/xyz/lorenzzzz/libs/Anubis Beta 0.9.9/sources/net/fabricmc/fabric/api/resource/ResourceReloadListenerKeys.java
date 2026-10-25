package net.fabricmc.fabric.api.resource;

import net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys;
import net.minecraft.class_2960;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:META-INF/jars/fabric-resource-loader-v0-3.3.4+4fc5413f3e.jar:net/fabricmc/fabric/api/resource/ResourceReloadListenerKeys.class */
@Deprecated
public final class ResourceReloadListenerKeys {
    public static final class_2960 SOUNDS = ResourceReloaderKeys.Client.SOUNDS;
    public static final class_2960 FONTS = ResourceReloaderKeys.Client.FONTS;
    public static final class_2960 MODELS = ResourceReloaderKeys.Client.MODELS;
    public static final class_2960 LANGUAGES = ResourceReloaderKeys.Client.LANGUAGES;
    public static final class_2960 TEXTURES = ResourceReloaderKeys.Client.TEXTURES;
    public static final class_2960 RECIPES = ResourceReloaderKeys.Server.RECIPES;
    public static final class_2960 ADVANCEMENTS = ResourceReloaderKeys.Server.ADVANCEMENTS;
    public static final class_2960 FUNCTIONS = ResourceReloaderKeys.Server.FUNCTIONS;

    private ResourceReloadListenerKeys() {
    }
}
