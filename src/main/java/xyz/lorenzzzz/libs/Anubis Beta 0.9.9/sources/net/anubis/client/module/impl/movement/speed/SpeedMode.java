package net.anubis.client.module.impl.movement.speed;

import net.anubis.client.module.impl.movement.SpeedModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_243;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/movement/speed/SpeedMode.class */
@Environment(EnvType.CLIENT)
public abstract class SpeedMode {
    private static final double BASE_SPEED = 0.2873d;
    protected final SpeedModule settings;
    protected int stage;
    protected double distance;
    protected double speed;

    protected SpeedMode(SpeedModule settings) {
        this.settings = settings;
        reset();
    }

    public void onTick(class_746 player) {
    }

    public class_243 onMove(class_746 player, class_243 movement) {
        return movement;
    }

    public void onRubberband() {
        reset();
    }

    public void onActivate() {
    }

    public void onDeactivate() {
    }

    protected double getDefaultSpeed(class_746 player) {
        double defaultSpeed = 0.2873d;
        class_1293 speedEffect = player.method_6112(class_1294.field_5904);
        if (speedEffect != null) {
            defaultSpeed = BASE_SPEED * (1.0d + (0.2d * ((double) (speedEffect.method_5578() + 1))));
        }
        class_1293 slowness = player.method_6112(class_1294.field_5909);
        if (slowness != null) {
            defaultSpeed /= 1.0d + (0.2d * ((double) (slowness.method_5578() + 1)));
        }
        return defaultSpeed;
    }

    protected void reset() {
        this.stage = 0;
        this.distance = 0.0d;
        this.speed = BASE_SPEED;
    }

    protected double getHop(class_746 player, double height) {
        class_1293 jumpBoost = player.method_6112(class_1294.field_5913);
        if (jumpBoost != null) {
            height += (double) ((jumpBoost.method_5578() + 1) * 0.1f);
        }
        return height;
    }
}
