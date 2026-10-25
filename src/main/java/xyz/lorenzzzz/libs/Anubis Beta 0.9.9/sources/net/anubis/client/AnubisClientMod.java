package net.anubis.client;

import java.util.Objects;
import net.anubis.client.addon.AddonManager;
import net.anubis.client.config.HudTheme;
import net.anubis.client.config.ModuleConfigStore;
import net.anubis.client.config.ProfileAutoLoad;
import net.anubis.client.config.ThemeStore;
import net.anubis.client.esp.ChunkScanner;
import net.anubis.client.event.impl.PacketAppliedEvent;
import net.anubis.client.event.impl.PacketReceiveNettyEvent;
import net.anubis.client.event.impl.Render2DEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.gui.BorderlessFullscreen;
import net.anubis.client.gui.HudLayoutStore;
import net.anubis.client.gui.HudRenderer;
import net.anubis.client.gui.ImGuiScreen;
import net.anubis.client.gui.LoadingScreens;
import net.anubis.client.gui.StaffGhost;
import net.anubis.client.gui.ToggleToasts;
import net.anubis.client.imgui.ImGuiManager;
import net.anubis.client.input.ModuleKeybinds;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.impl.misc.noinvleak.StreamOverlay;
import net.anubis.client.util.BlockEntityLeakTracker;
import net.anubis.client.util.combat.CombatTarget;
import net.anubis.client.util.netherite.NetheriteChunkTracker;
import net.anubis.client.util.playerbypass.PlayerBypassLightTracker;
import net.anubis.client.util.playerbypass.ProtectedChunkStore;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.class_2960;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3414;
import net.minecraft.class_3675;
import net.minecraft.class_437;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/AnubisClientMod.class */
@Environment(EnvType.CLIENT)
public final class AnubisClientMod implements ClientModInitializer {
    private static class_304 openGuiKey;
    private final ModuleKeybinds keybinds = new ModuleKeybinds();
    public static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    public static final class_2960 SPAWNER_NOTIFY_SOUND_ID = class_2960.method_60655(AddonManager.ENTRYPOINT, "spawner_notify");
    public static final class_3414 SPAWNER_NOTIFY_SOUND = class_3414.method_47908(SPAWNER_NOTIFY_SOUND_ID);
    public static final class_304.class_11900 KEY_CATEGORY = class_304.class_11900.method_74698(class_2960.method_60655(AddonManager.ENTRYPOINT, "controls"));
    private static final Object LEAK_TRACKER_OWNER = new Object();
    private static final Object PLAYER_BYPASS_TRACKER_OWNER = new Object();
    private static final Object NETHERITE_TRACKER_OWNER = new Object();

    public void onInitializeClient() {
        LOGGER.info("[Anubis] Initialising");
        AddonManager.init();
        ImGuiManager imGuiManager = ImGuiManager.getInstance();
        HudRenderer hudRenderer = HudRenderer.getInstance();
        Objects.requireNonNull(hudRenderer);
        Runnable runnable = hudRenderer::render;
        HudRenderer hudRenderer2 = HudRenderer.getInstance();
        Objects.requireNonNull(hudRenderer2);
        imGuiManager.setHudRenderer(runnable, hudRenderer2::hasWork);
        ImGuiManager.getInstance().setStreamRenderer(StreamOverlay::drawFakes, StreamOverlay::hasFakes);
        ImGuiManager.getInstance().setToastRenderer(ToggleToasts::render, ToggleToasts::hasWork);
        ImGuiManager.getInstance().setOverlayRenderer(LoadingScreens::render, LoadingScreens::hasWork);
        ImGuiManager.getInstance().setAlertRenderer(StaffGhost::render, StaffGhost::hasWork);
        Render2DEvent.TYPE.listen(StaffGhost.class, StaffGhost::renderNote);
        ChunkScanner.init();
        PacketReceiveNettyEvent.TYPE.listen(LEAK_TRACKER_OWNER, event -> {
            BlockEntityLeakTracker.ingest(event.packet);
        });
        PacketAppliedEvent.TYPE.listen(PLAYER_BYPASS_TRACKER_OWNER, event2 -> {
            PlayerBypassLightTracker.onPacketApplied(event2.packet);
        });
        TickEvent.TYPE.listen(PLAYER_BYPASS_TRACKER_OWNER, event3 -> {
            PlayerBypassLightTracker.onTick();
        });
        TickEvent.TYPE.listen(BorderlessFullscreen.class, event4 -> {
            BorderlessFullscreen.tick();
        });
        CombatTarget.install();
        PacketAppliedEvent.TYPE.listen(NETHERITE_TRACKER_OWNER, event5 -> {
            NetheriteChunkTracker.onPacketApplied(event5.packet);
        });
        ToggleToasts.install();
        ProfileAutoLoad.install();
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            HudLayoutStore.getInstance().save();
            ModuleConfigStore.getInstance().save();
            ThemeStore.get().save();
            HudTheme.get().save();
            ProtectedChunkStore.get().flushAndWait();
            StreamOverlay.stop();
        });
        openGuiKey = KeyBindingHelper.registerKeyBinding(new class_304("key.anubis.open_gui", class_3675.class_307.field_1668, 344, KEY_CATEGORY));
        ModuleConfigStore.getInstance().load();
        AddonManager.configLoaded();
        ClientTickEvents.END_CLIENT_TICK.register(client2 -> {
            ModuleConfigStore.getInstance().applyPendingEnabled();
            while (openGuiKey.method_1436()) {
                if (client2.field_1755 instanceof ImGuiScreen) {
                    client2.method_1507((class_437) null);
                } else {
                    client2.method_1507(new ImGuiScreen());
                }
            }
            this.keybinds.update(ModuleRegistry.all(), AnubisClientMod::isBoundKeyDown, client2.field_1755 == null && client2.method_1569() && client2.field_1724 != null);
            ModuleConfigStore.getInstance().tickAutosave();
        });
    }

    private static boolean isBoundKeyDown(int key) {
        class_310 client = class_310.method_1551();
        if (key >= 1000) {
            return GLFW.glfwGetMouseButton(client.method_22683().method_4490(), key - ModuleKeybinds.MOUSE_OFFSET) == 1;
        }
        return class_3675.method_15987(client.method_22683(), key);
    }
}
