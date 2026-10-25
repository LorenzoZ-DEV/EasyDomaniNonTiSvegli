package net.anubis.client.module.impl.misc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Base64;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.anubis.client.addon.AddonManager;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.StringSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10538;
import net.minecraft.class_12079;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_7920;
import net.minecraft.class_8685;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/SkinChangeModule.class */
@Environment(EnvType.CLIENT)
public final class SkinChangeModule extends ClientModule {
    private final StringSetting selfPlayerSetting;
    private final StringSetting othersPlayerSetting;
    private volatile CustomSkin selfSkin;
    private volatile CustomSkin othersSkin;
    private String loadedSelfName;
    private String loadedOthersName;
    private boolean selfLoading;
    private boolean othersLoading;
    private final SwapCache selfSwaps;
    private final SwapCache othersSwaps;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/SkinChangeModule$CustomSkin.class */
    @Environment(EnvType.CLIENT)
    private static final class CustomSkin extends Record {
        private final class_12079.class_12081 body;
        private final class_7920 model;

        private CustomSkin(class_12079.class_12081 body, class_7920 model) {
            this.body = body;
            this.model = model;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, CustomSkin.class), CustomSkin.class, "body;model", "FIELD:Lnet/anubis/client/module/impl/misc/SkinChangeModule$CustomSkin;->body:Lnet/minecraft/class_12079$class_12081;", "FIELD:Lnet/anubis/client/module/impl/misc/SkinChangeModule$CustomSkin;->model:Lnet/minecraft/class_7920;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, CustomSkin.class), CustomSkin.class, "body;model", "FIELD:Lnet/anubis/client/module/impl/misc/SkinChangeModule$CustomSkin;->body:Lnet/minecraft/class_12079$class_12081;", "FIELD:Lnet/anubis/client/module/impl/misc/SkinChangeModule$CustomSkin;->model:Lnet/minecraft/class_7920;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, CustomSkin.class, Object.class), CustomSkin.class, "body;model", "FIELD:Lnet/anubis/client/module/impl/misc/SkinChangeModule$CustomSkin;->body:Lnet/minecraft/class_12079$class_12081;", "FIELD:Lnet/anubis/client/module/impl/misc/SkinChangeModule$CustomSkin;->model:Lnet/minecraft/class_7920;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_12079.class_12081 body() {
            return this.body;
        }

        public class_7920 model() {
            return this.model;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/SkinChangeModule$SkinSource.class */
    @Environment(EnvType.CLIENT)
    private static final class SkinSource extends Record {
        private final String url;
        private final class_7920 model;

        private SkinSource(String url, class_7920 model) {
            this.url = url;
            this.model = model;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, SkinSource.class), SkinSource.class, "url;model", "FIELD:Lnet/anubis/client/module/impl/misc/SkinChangeModule$SkinSource;->url:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/SkinChangeModule$SkinSource;->model:Lnet/minecraft/class_7920;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, SkinSource.class), SkinSource.class, "url;model", "FIELD:Lnet/anubis/client/module/impl/misc/SkinChangeModule$SkinSource;->url:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/SkinChangeModule$SkinSource;->model:Lnet/minecraft/class_7920;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, SkinSource.class, Object.class), SkinSource.class, "url;model", "FIELD:Lnet/anubis/client/module/impl/misc/SkinChangeModule$SkinSource;->url:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/misc/SkinChangeModule$SkinSource;->model:Lnet/minecraft/class_7920;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public String url() {
            return this.url;
        }

        public class_7920 model() {
            return this.model;
        }
    }

    public SkinChangeModule() {
        super("skin-change", "Skin Change", "Wear any player's skin client-side.", ModuleCategory.MISC);
        this.selfPlayerSetting = (StringSetting) addSetting("self-player", new StringSetting("Self Player", ""));
        this.othersPlayerSetting = (StringSetting) addSetting("others-player", new StringSetting("Others Player", ""));
        this.loadedSelfName = "";
        this.loadedOthersName = "";
        this.selfSwaps = new SwapCache();
        this.othersSwaps = new SwapCache();
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        reloadIfChanged();
        on(TickEvent.TYPE, e -> {
            reloadIfChanged();
        });
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        this.selfSkin = null;
        this.othersSkin = null;
        this.loadedSelfName = "";
        this.loadedOthersName = "";
        this.selfSwaps.clear();
        this.othersSwaps.clear();
    }

    private void reloadIfChanged() {
        String selfName = this.selfPlayerSetting.get().trim();
        String othersName = this.othersPlayerSetting.get().trim();
        if (!selfName.equals(this.loadedSelfName)) {
            this.selfSkin = null;
            if (selfName.isEmpty()) {
                this.loadedSelfName = "";
            } else if (!this.selfLoading) {
                this.loadedSelfName = selfName;
                this.selfLoading = true;
                loadFromUsername(selfName, true);
            }
        }
        if (!othersName.equals(this.loadedOthersName)) {
            this.othersSkin = null;
            if (othersName.isEmpty()) {
                this.loadedOthersName = "";
            } else if (!this.othersLoading) {
                this.loadedOthersName = othersName;
                this.othersLoading = true;
                loadFromUsername(othersName, false);
            }
        }
    }

    private void loadFromUsername(String username, boolean isSelf) {
        class_310 mc = class_310.method_1551();
        CompletableFuture.runAsync(() -> {
            class_2960 class_2960VarMethod_60655;
            try {
                String uuid = fetchUUID(username);
                if (uuid == null) {
                    mc.execute(() -> {
                        finishLoad(username, isSelf, null);
                    });
                    return;
                }
                SkinSource skin = fetchSkin(uuid);
                if (skin == null) {
                    mc.execute(() -> {
                        finishLoad(username, isSelf, null);
                    });
                    return;
                }
                if (isSelf) {
                    class_2960VarMethod_60655 = class_2960.method_60655(AddonManager.ENTRYPOINT, "skin/self");
                } else {
                    class_2960VarMethod_60655 = class_2960.method_60655(AddonManager.ENTRYPOINT, "skin/others");
                }
                class_2960 id = class_2960VarMethod_60655;
                Path cacheDir = mc.field_1697.toPath().resolve("cache/anubis_skins");
                try {
                    Files.createDirectories(cacheDir, new FileAttribute[0]);
                } catch (IOException e) {
                }
                Path cachePath = cacheDir.resolve((isSelf ? "self_" : "others_") + uuid + ".png");
                registerSkin(id, cachePath, skin.url()).whenCompleteAsync((texture, error) -> {
                    finishLoad(username, isSelf, texture == null ? null : new CustomSkin(texture, skin.model()));
                }, (Executor) mc);
            } catch (Exception e2) {
                mc.execute(() -> {
                    finishLoad(username, isSelf, null);
                });
            }
        });
    }

    private void finishLoad(String username, boolean isSelf, CustomSkin loaded) {
        if (isSelf) {
            this.selfLoading = false;
        } else {
            this.othersLoading = false;
        }
        if (isEnabled()) {
            String current = (isSelf ? this.selfPlayerSetting : this.othersPlayerSetting).get().trim();
            if (loaded != null && username.equals(current)) {
                if (username.equals(isSelf ? this.loadedSelfName : this.loadedOthersName)) {
                    if (isSelf) {
                        this.selfSkin = loaded;
                    } else {
                        this.othersSkin = loaded;
                    }
                }
            }
            reloadIfChanged();
        }
    }

    private static CompletableFuture<class_12079.class_12081> registerSkin(class_2960 id, Path cachePath, String url) {
        class_310 mc = class_310.method_1551();
        class_10538 downloader = new class_10538(mc.method_1487(), mc.method_1531(), mc);
        return downloader.method_65861(id, cachePath, url, true);
    }

    private static String fetchUUID(String username) throws Exception {
        HttpURLConnection conn = openGet("https://api.mojang.com/users/profiles/minecraft/" + username);
        int status = conn.getResponseCode();
        if (status == 204 || status == 404) {
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        try {
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    JsonObject obj = JsonParser.parseString(sb.toString()).getAsJsonObject();
                    String asString = obj.get("id").getAsString();
                    reader.close();
                    return asString;
                }
                sb.append(line);
            }
        } catch (Throwable th) {
            try {
                reader.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private static SkinSource fetchSkin(String uuid) throws Exception {
        HttpURLConnection conn = openGet("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
        if (conn.getResponseCode() != 200) {
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        try {
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
            }
            String json = sb.toString();
            reader.close();
            JsonObject profile = JsonParser.parseString(json).getAsJsonObject();
            for (JsonElement elem : profile.getAsJsonArray("properties")) {
                JsonObject prop = elem.getAsJsonObject();
                if ("textures".equals(prop.get("name").getAsString())) {
                    String decoded = new String(Base64.getDecoder().decode(prop.get("value").getAsString()));
                    JsonObject textures = JsonParser.parseString(decoded).getAsJsonObject().getAsJsonObject("textures");
                    if (!textures.has("SKIN")) {
                        return null;
                    }
                    JsonObject skin = textures.getAsJsonObject("SKIN");
                    JsonObject metadata = skin.has("metadata") ? skin.getAsJsonObject("metadata") : null;
                    String model = (metadata == null || !metadata.has("model")) ? null : metadata.get("model").getAsString();
                    return new SkinSource(skin.get("url").getAsString(), class_7920.method_74671(model));
                }
            }
            return null;
        } catch (Throwable th) {
            try {
                reader.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private static HttpURLConnection openGet(String url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) URI.create(url).toURL().openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.setRequestProperty("User-Agent", "Anubis/1.0");
        return conn;
    }

    public class_8685 skinFor(int entityId, class_8685 original) {
        class_310 mc = class_310.method_1551();
        return skinFor(mc.field_1724 != null && entityId == mc.field_1724.method_5628(), original);
    }

    public class_8685 skinFor(UUID profileId, class_8685 original) {
        class_310 mc = class_310.method_1551();
        return skinFor(mc.field_1724 != null && mc.field_1724.method_5667().equals(profileId), original);
    }

    private class_8685 skinFor(boolean isSelf, class_8685 original) {
        CustomSkin custom = isSelf ? this.selfSkin : this.othersSkin;
        if (custom == null) {
            return original;
        }
        return (isSelf ? this.selfSwaps : this.othersSwaps).swap(original, custom);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/SkinChangeModule$SwapCache.class */
    @Environment(EnvType.CLIENT)
    private static final class SwapCache {
        private static final int MAX = 256;
        private final Map<class_8685, class_8685> swapped = new IdentityHashMap();
        private CustomSkin wearing;

        private SwapCache() {
        }

        class_8685 swap(class_8685 original, CustomSkin custom) {
            if (custom != this.wearing || this.swapped.size() >= 256) {
                this.swapped.clear();
                this.wearing = custom;
            }
            class_8685 skin = this.swapped.get(original);
            if (skin == null) {
                skin = new class_8685(custom.body(), original.comp_1627(), original.comp_1628(), custom.model(), original.comp_1630());
                this.swapped.put(original, skin);
            }
            return skin;
        }

        void clear() {
            this.swapped.clear();
            this.wearing = null;
        }
    }
}
