package net.anubis.client.module.impl.render;

import java.util.Objects;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.impl.misc.FreecamModule;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3675;
import net.minecraft.class_5498;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/FreelookModule.class */
@Environment(EnvType.CLIENT)
public final class FreelookModule extends ClientModule {
    private static final String PLAYER = "Player";
    private static final String CAMERA = "Camera";
    private static final float MIN_SENSITIVITY = 0.1f;
    private final ModeSetting mode;
    private final BooleanSetting togglePerspective;
    private final NumberSetting sensitivity;
    private final BooleanSetting arrows;
    private final NumberSetting arrowSpeed;
    private float cameraYaw;
    private float cameraPitch;
    private class_5498 previousView;

    public FreelookModule() {
        super("freelook", "Freelook", "Turns the camera and the player separately.", ModuleCategory.RENDER);
        this.mode = (ModeSetting) addSetting("mode", new ModeSetting("Mode", PLAYER, PLAYER, CAMERA));
        this.togglePerspective = (BooleanSetting) addSetting("toggle-perspective", new BooleanSetting("Toggle Perspective", true));
        this.sensitivity = (NumberSetting) addSetting("camera-sensitivity", new NumberSetting("Camera Sensitivity", 8.0f, 0.0f, 10.0f).step(MIN_SENSITIVITY));
        this.arrows = (BooleanSetting) addSetting("arrows-control-opposite", new BooleanSetting("Arrows Control Opposite", true));
        this.arrowSpeed = (NumberSetting) addSetting("arrow-speed", new NumberSetting("Arrow Speed", 4.0f, 0.0f, 10.0f).step(0.5f));
        this.sensitivity.visibleWhen(() -> {
            return this.mode.is(CAMERA);
        });
        NumberSetting numberSetting = this.arrowSpeed;
        BooleanSetting booleanSetting = this.arrows;
        Objects.requireNonNull(booleanSetting);
        numberSetting.visibleWhen(booleanSetting::get);
    }

    @Override // net.anubis.client.module.ClientModule
    public boolean persistEnabled() {
        return false;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null) {
            requestDisable();
            return;
        }
        this.cameraYaw = mc.field_1724.method_36454();
        this.cameraPitch = mc.field_1724.method_36455();
        this.previousView = mc.field_1690.method_31044();
        if (this.previousView != class_5498.field_26665 && this.togglePerspective.get()) {
            mc.field_1690.method_31043(class_5498.field_26665);
        }
        on(TickEvent.TYPE, Stage.POST, this::onTick);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        class_310 mc = class_310.method_1551();
        if (this.previousView != null && mc.field_1690.method_31044() != this.previousView && this.togglePerspective.get()) {
            mc.field_1690.method_31043(this.previousView);
        }
        this.previousView = null;
    }

    private void onTick(TickEvent e) {
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (player == null) {
            return;
        }
        if (this.arrows.get() && mc.field_1755 == null) {
            float turn = this.arrowSpeed.get();
            float yawTurn = (held(mc, 262) ? turn : 0.0f) - (held(mc, 263) ? turn : 0.0f);
            float pitchTurn = (held(mc, 264) ? turn : 0.0f) - (held(mc, 265) ? turn : 0.0f);
            if (this.mode.is(PLAYER)) {
                this.cameraYaw += yawTurn;
                this.cameraPitch += pitchTurn;
            } else if (yawTurn != 0.0f || pitchTurn != 0.0f) {
                player.method_36456(player.method_36454() + yawTurn);
                player.method_36457(player.method_36455() + pitchTurn);
            }
        }
        this.cameraPitch = class_3532.method_15363(this.cameraPitch, -90.0f, 90.0f);
    }

    private static boolean held(class_310 mc, int key) {
        return class_3675.method_15987(mc.method_22683(), key);
    }

    public boolean cameraMode() {
        return isEnabled() && this.mode.is(CAMERA);
    }

    public static boolean throughBlocks() {
        FreelookModule freelook = (FreelookModule) ModuleRegistry.get(FreelookModule.class);
        return freelook != null && freelook.isEnabled() && FreecamModule.active == null;
    }

    public void turnCamera(double yRot, double xRot) {
        float divisor = Math.max(this.sensitivity.get(), MIN_SENSITIVITY);
        this.cameraYaw += (float) (yRot / ((double) divisor));
        this.cameraPitch += (float) (xRot / ((double) divisor));
        if (Math.abs(this.cameraPitch) > 90.0f) {
            this.cameraPitch = this.cameraPitch > 0.0f ? 90.0f : -90.0f;
        }
    }

    public float cameraYaw() {
        return this.cameraYaw;
    }

    public float cameraPitch() {
        return this.cameraPitch;
    }
}
