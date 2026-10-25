package net.anubis.client.module.impl.movement.elytrafly;

import net.anubis.client.chat.ModuleChat;
import net.anubis.client.event.impl.PlayerMoveEvent;
import net.anubis.client.module.impl.movement.ElytraFlyModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/movement/elytrafly/Pitch40Mode.class */
@Environment(EnvType.CLIENT)
public final class Pitch40Mode extends ElytraFlightMode {
    private static final int MIN_START_ABOVE_LOWER = 40;
    private boolean pitchingDown;
    private float pitch;

    public Pitch40Mode(ElytraFlyModule elytraFly) {
        super(elytraFly);
        this.pitchingDown = true;
    }

    @Override // net.anubis.client.module.impl.movement.elytrafly.ElytraFlightMode
    public void onActivate() {
        class_746 player = this.mc.field_1724;
        if (player.method_23318() < this.elytraFly.pitch40upperBounds.get()) {
            ModuleChat.send(this.elytraFly, ModuleChat.body().text("Pitch40 ").danger("can't start").text(", you must be above the upper bound (Y ").value(Integer.valueOf(this.elytraFly.pitch40upperBounds.getInt())).text(")"));
            this.elytraFly.disableFromMode();
        } else if (player.method_23318() - 40.0d < this.elytraFly.pitch40lowerBounds.get()) {
            ModuleChat.send(this.elytraFly, ModuleChat.body().text("Pitch40 ").danger("can't start").text(", you must be at least ").value(40).text(" blocks above the lower bound (Y ").value(Integer.valueOf(this.elytraFly.pitch40lowerBounds.getInt())).text(")"));
            this.elytraFly.disableFromMode();
        }
        this.pitch = 37.72f;
    }

    private float randPitch(float pitch, float bound) {
        return (float) (((double) pitch) + (((double) bound) * (Math.random() - 0.5d)));
    }

    @Override // net.anubis.client.module.impl.movement.elytrafly.ElytraFlightMode
    public void onTick() {
        super.onTick();
        class_746 player = this.mc.field_1724;
        if (player == null) {
            return;
        }
        if (this.pitchingDown && player.method_23318() <= this.elytraFly.pitch40lowerBounds.get()) {
            this.pitchingDown = false;
        } else if (!this.pitchingDown && player.method_23318() >= this.elytraFly.pitch40upperBounds.get()) {
            this.pitchingDown = true;
        }
        if (!this.pitchingDown) {
            this.pitch -= randPitch(this.elytraFly.pitch40rotationSpeedUp.get(), 1.0f);
            if (this.pitch < -54.77f) {
                this.pitch = -54.77f;
                this.pitchingDown = true;
            }
        } else if (this.pitch < 37.72f) {
            this.pitch += randPitch(this.elytraFly.pitch40rotationSpeedDown.get(), 0.5f);
        }
        player.method_36457(this.pitch);
    }

    @Override // net.anubis.client.module.impl.movement.elytrafly.ElytraFlightMode
    public void autoTakeoff() {
    }

    @Override // net.anubis.client.module.impl.movement.elytrafly.ElytraFlightMode
    public void handleHorizontalSpeed(PlayerMoveEvent event) {
        this.velX = event.x;
        this.velZ = event.z;
    }

    @Override // net.anubis.client.module.impl.movement.elytrafly.ElytraFlightMode
    public void handleVerticalSpeed(PlayerMoveEvent event) {
    }

    @Override // net.anubis.client.module.impl.movement.elytrafly.ElytraFlightMode
    public void handleFallMultiplier() {
    }

    @Override // net.anubis.client.module.impl.movement.elytrafly.ElytraFlightMode
    public void handleAutopilot() {
    }
}
