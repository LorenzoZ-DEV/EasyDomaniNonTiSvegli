package net.anubis.client.module.impl.movement.speed;

import net.anubis.client.module.impl.movement.SpeedModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/movement/speed/StrafeSpeedMode.class */
@Environment(EnvType.CLIENT)
public final class StrafeSpeedMode extends SpeedMode {
    private long timer;

    public StrafeSpeedMode(SpeedModule settings) {
        super(settings);
        this.timer = 0L;
    }

    @Override // net.anubis.client.module.impl.movement.speed.SpeedMode
    public class_243 onMove(class_746 player, class_243 movement) {
        boolean moving = SpeedModule.isMoving(player);
        double y = movement.field_1351;
        switch (this.stage) {
            case 0:
                if (moving) {
                    this.stage++;
                    this.speed = (1.1799999475479126d * getDefaultSpeed(player)) - 0.01d;
                    break;
                }
            case 1:
                if (moving && player.method_24828()) {
                    y = getHop(player, 0.40123128d);
                    this.speed *= this.settings.strafeSpeed();
                    this.stage++;
                }
                break;
            case 2:
                this.speed = this.distance - (0.76d * (this.distance - getDefaultSpeed(player)));
                this.stage++;
                break;
            case 3:
                if (!player.method_73183().method_18026(player.method_5829().method_989(0.0d, player.method_18798().field_1351, 0.0d)) || (player.field_5992 && this.stage > 0)) {
                    this.stage = 0;
                }
                this.speed = this.distance - (this.distance / 159.0d);
                break;
        }
        this.speed = Math.max(this.speed, getDefaultSpeed(player));
        if (this.settings.speedLimit()) {
            if (System.currentTimeMillis() - this.timer > 2500) {
                this.timer = System.currentTimeMillis();
            }
            this.speed = Math.min(this.speed, System.currentTimeMillis() - this.timer > 1250 ? 0.44d : 0.43d);
        }
        double[] change = transformStrafe(player, this.speed);
        return new class_243(change[0], y, change[1]);
    }

    private static double[] transformStrafe(class_746 player, double speed) {
        class_241 input = player.field_3913.method_3128();
        float forward = Math.signum(input.field_1342);
        float side = Math.signum(input.field_1343);
        float yaw = player.method_61415(class_310.method_1551().method_61966().method_60637(true));
        if (forward == 0.0f && side == 0.0f) {
            return new double[2];
        }
        float strafe = 90.0f * side;
        if (forward != 0.0f) {
            strafe *= forward * 0.5f;
        }
        float yaw2 = yaw - strafe;
        if (forward < 0.0f) {
            yaw2 -= 180.0f;
        }
        double yawRadians = Math.toRadians(yaw2);
        return new double[]{(-Math.sin(yawRadians)) * speed, Math.cos(yawRadians) * speed};
    }

    @Override // net.anubis.client.module.impl.movement.speed.SpeedMode
    public void onTick(class_746 player) {
        double dx = player.method_23317() - player.field_6014;
        double dz = player.method_23321() - player.field_5969;
        this.distance = Math.sqrt((dx * dx) + (dz * dz));
    }
}
