package net.anubis.client.module.impl.movement;

import java.util.Objects;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.PacketReceiveNettyEvent;
import net.anubis.client.event.impl.PacketSendEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.impl.movement.entitycontrol.EntityControlVehicleHook;
import net.anubis.client.module.impl.movement.entitycontrol.VehicleAntiKick;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1316;
import net.minecraft.class_2692;
import net.minecraft.class_2833;
import net.minecraft.class_310;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/movement/BoatFlyModule.class */
@Environment(EnvType.CLIENT)
public final class BoatFlyModule extends ClientModule {
    public static volatile BoatFlyModule active;
    private final NumberSetting speed;
    private final NumberSetting verticalSpeed;
    private final NumberSetting fallSpeed;
    private final BooleanSetting antiKick;
    private final NumberSetting delay;
    private final BooleanSetting cancelServerPackets;
    private final VehicleAntiKick antiKicker;

    public BoatFlyModule() {
        super("boat-fly", "Boat Fly", "Transforms your boat (or any ridden vehicle) into a plane.", ModuleCategory.MOVEMENT);
        this.speed = (NumberSetting) addSetting("speed", new NumberSetting("Speed", 10.0f, 0.0f, 50.0f).step(0.5f));
        this.verticalSpeed = (NumberSetting) addSetting("vertical-speed", new NumberSetting("Vertical Speed", 6.0f, 0.0f, 20.0f).step(0.5f));
        this.fallSpeed = (NumberSetting) addSetting("fall-speed", new NumberSetting("Fall Speed", 0.0f, 0.0f, 10.0f).step(0.1f));
        this.antiKick = (BooleanSetting) addSetting("anti-fly-kick", new BooleanSetting("Anti Fly Kick", true));
        this.delay = (NumberSetting) addSetting("delay", new NumberSetting("Delay", 40.0f, 1.0f, 80.0f).step(1.0f));
        this.cancelServerPackets = (BooleanSetting) addSetting("cancel-server-packets", new BooleanSetting("Cancel Server Packets", false));
        this.antiKicker = new VehicleAntiKick();
        NumberSetting numberSetting = this.delay;
        BooleanSetting booleanSetting = this.antiKick;
        Objects.requireNonNull(booleanSetting);
        numberSetting.visibleWhen(booleanSetting::get);
        BooleanSetting booleanSetting2 = this.antiKick;
        VehicleAntiKick vehicleAntiKick = this.antiKicker;
        Objects.requireNonNull(vehicleAntiKick);
        booleanSetting2.onChange(vehicleAntiKick::forget);
    }

    @Override // net.anubis.client.module.ClientModule
    public boolean persistEnabled() {
        return false;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        BoatNoclipModule noclip = (BoatNoclipModule) ModuleRegistry.get(BoatNoclipModule.class);
        if (noclip != null) {
            noclip.setEnabled(false);
        }
        this.antiKicker.start(this.delay.getInt());
        active = this;
        EntityControlVehicleHook.refresh();
        on(TickEvent.TYPE, Stage.PRE, event -> {
            this.antiKicker.tick();
        });
        on(PacketSendEvent.TYPE, this::onPacketSend);
        on(PacketReceiveNettyEvent.TYPE, this::onPacketReceive);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        active = null;
        EntityControlVehicleHook.refresh();
        this.antiKicker.stop();
    }

    private void onPacketSend(PacketSendEvent event) {
        if (this.antiKick.get()) {
            class_2833 class_2833Var = event.packet;
            if (class_2833Var instanceof class_2833) {
                class_2833 packet = class_2833Var;
                this.antiKicker.onPacketSend(packet, this.delay.getInt());
            }
        }
    }

    private void onPacketReceive(PacketReceiveNettyEvent event) {
        if (this.cancelServerPackets.get() && (event.packet instanceof class_2692)) {
            event.setCancelled(true);
        }
    }

    public void onVehicleSetback() {
        this.antiKicker.forget();
    }

    public double horizontalSpeed() {
        return this.speed.get();
    }

    public double verticalVelocity() {
        class_310 mc = class_310.method_1551();
        double velY = 0.0d;
        if (EntityControlVehicleHook.isPhysicallyDown(mc.field_1690.field_1903)) {
            velY = 0.0d + (((double) this.verticalSpeed.get()) / 20.0d);
        }
        return EntityControlVehicleHook.isPhysicallyDown(mc.field_1690.field_1867) ? velY - (((double) this.verticalSpeed.get()) / 20.0d) : velY - (((double) this.fallSpeed.get()) / 20.0d);
    }

    public static boolean cancelsMountJump(class_746 player) {
        return active != null && (player.method_5854() instanceof class_1316);
    }
}
