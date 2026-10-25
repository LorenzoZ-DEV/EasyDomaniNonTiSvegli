package net.anubis.client.module.impl.movement;

import net.anubis.client.event.EventPriority;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.KeyInputEvent;
import net.anubis.client.event.impl.MouseInputEvent;
import net.anubis.client.event.impl.PlayerMoveEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.impl.misc.FreecamModule;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_11910;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_315;
import net.minecraft.class_3675;
import net.minecraft.class_4076;
import net.minecraft.class_638;
import net.minecraft.class_746;
import org.lwjgl.glfw.GLFW;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/movement/AutoWalkModule.class */
@Environment(EnvType.CLIENT)
public final class AutoWalkModule extends ClientModule {
    private final ModeSetting direction;
    private final BooleanSetting disableOnInput;
    private final BooleanSetting disableOnY;
    private final BooleanSetting waitForChunks;

    public AutoWalkModule() {
        super("auto-walk", "Auto Walk", "Automatically walks forward.", ModuleCategory.MOVEMENT);
        this.direction = (ModeSetting) addSetting("simple-direction", new ModeSetting("Simple Direction", "Forwards", "Forwards", "Backwards", "Left", "Right"));
        this.disableOnInput = (BooleanSetting) addSetting("disable-on-input", new BooleanSetting("Disable On Input", false));
        this.disableOnY = (BooleanSetting) addSetting("disable-on-y-change", new BooleanSetting("Disable On Y Change", false));
        this.waitForChunks = (BooleanSetting) addSetting("no-unloaded-chunks", new BooleanSetting("No Unloaded Chunks", true));
        this.direction.onChange(() -> {
            if (isEnabled()) {
                releaseKeys();
            }
        });
    }

    @Override // net.anubis.client.module.ClientModule
    public boolean persistEnabled() {
        return false;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        on(TickEvent.TYPE, Stage.PRE, EventPriority.HIGH, this::onPreTick);
        on(KeyInputEvent.TYPE, this::onKey);
        on(MouseInputEvent.TYPE, this::onMouseButton);
        on(PlayerMoveEvent.TYPE, this::onPlayerMove);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        releaseKeys();
    }

    private void onPreTick(TickEvent event) {
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (player == null || mc.field_1687 == null) {
            return;
        }
        if (this.disableOnY.get() && player.field_6036 != player.method_23318()) {
            requestDisable();
        } else {
            keyFor(mc.field_1690).method_23481(true);
        }
    }

    private void onKey(KeyInputEvent event) {
        if (event.action == 1 && this.disableOnInput.get() && event.key != -1) {
            class_315 options = class_310.method_1551().field_1690;
            class_11908 key = new class_11908(event.key, event.scancode, event.mods);
            if (options.field_1894.method_1417(key) || options.field_1881.method_1417(key) || options.field_1913.method_1417(key) || options.field_1849.method_1417(key) || options.field_1832.method_1417(key) || options.field_1903.method_1417(key)) {
                onMovementInput();
            }
        }
    }

    private void onMouseButton(MouseInputEvent event) {
        if (event.action == 1 && this.disableOnInput.get()) {
            class_315 options = class_310.method_1551().field_1690;
            class_11909 click = new class_11909(0.0d, 0.0d, new class_11910(event.button, event.mods));
            if (options.field_1894.method_1433(click) || options.field_1881.method_1433(click) || options.field_1913.method_1433(click) || options.field_1849.method_1433(click) || options.field_1832.method_1433(click) || options.field_1903.method_1433(click)) {
                onMovementInput();
            }
        }
    }

    private void onMovementInput() {
        if (class_310.method_1551().field_1755 != null) {
            return;
        }
        requestDisable();
    }

    private void onPlayerMove(PlayerMoveEvent event) {
        if (this.waitForChunks.get()) {
            class_310 mc = class_310.method_1551();
            class_746 player = mc.field_1724;
            class_638 level = mc.field_1687;
            if (player == null || level == null) {
                return;
            }
            int chunkX = class_4076.method_42615(player.method_23317() + (event.x * 2.0d));
            int chunkZ = class_4076.method_42615(player.method_23321() + (event.z * 2.0d));
            if (!level.method_2935().method_12123(chunkX, chunkZ)) {
                event.x = 0.0d;
                event.z = 0.0d;
            }
        }
    }

    private class_304 keyFor(class_315 options) {
        switch (this.direction.get()) {
            case "Backwards":
                return options.field_1881;
            case "Left":
                return options.field_1913;
            case "Right":
                return options.field_1849;
            default:
                return options.field_1894;
        }
    }

    private static void releaseKeys() {
        class_310 mc = class_310.method_1551();
        class_315 options = mc.field_1690;
        if (options == null) {
            return;
        }
        boolean inGame = mc.field_1755 == null && !isFreecamActive();
        restore(mc, options.field_1894, inGame);
        restore(mc, options.field_1881, inGame);
        restore(mc, options.field_1913, inGame);
        restore(mc, options.field_1849, inGame);
    }

    private static boolean isFreecamActive() {
        FreecamModule freecam = (FreecamModule) ModuleRegistry.get(FreecamModule.class);
        return freecam != null && freecam.isEnabled();
    }

    private static void restore(class_310 mc, class_304 mapping, boolean inGame) {
        mapping.method_23481(inGame && isPhysicallyHeld(mc, mapping));
    }

    private static boolean isPhysicallyHeld(class_310 mc, class_304 mapping) {
        if (mapping.method_1415()) {
            return false;
        }
        class_3675.class_306 key = class_3675.method_15981(mapping.method_1428());
        class_3675.class_307 type = key.method_1442();
        if (type == class_3675.class_307.field_1668) {
            return class_3675.method_15987(mc.method_22683(), key.method_1444());
        }
        return type == class_3675.class_307.field_1672 && GLFW.glfwGetMouseButton(mc.method_22683().method_4490(), key.method_1444()) == 1;
    }
}
