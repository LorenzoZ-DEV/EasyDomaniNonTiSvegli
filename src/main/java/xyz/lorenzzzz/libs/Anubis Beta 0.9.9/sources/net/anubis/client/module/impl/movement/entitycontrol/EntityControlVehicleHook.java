package net.anubis.client.module.impl.movement.entitycontrol;

import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.impl.misc.FreecamModule;
import net.anubis.client.module.impl.movement.BoatFlyModule;
import net.anubis.client.module.impl.movement.BoatNoclipModule;
import net.anubis.client.module.impl.movement.EntityControlModule;
import net.anubis.client.module.impl.movement.EntitySpeedModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.class_10185;
import net.minecraft.class_10255;
import net.minecraft.class_1297;
import net.minecraft.class_1313;
import net.minecraft.class_243;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3675;
import net.minecraft.class_746;
import org.lwjgl.glfw.GLFW;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/movement/entitycontrol/EntityControlVehicleHook.class */
@Environment(EnvType.CLIENT)
public final class EntityControlVehicleHook {
    private static final double DIAGONAL = 1.0d / Math.sqrt(2.0d);
    public static volatile boolean anyActive;

    private EntityControlVehicleHook() {
    }

    public static void refresh() {
        anyActive = (EntityControlModule.active == null && EntitySpeedModule.active == null && BoatFlyModule.active == null && BoatNoclipModule.active == null) ? false : true;
    }

    public static class_243 onEntityMove(class_1297 entity, class_1313 type, class_243 movement) {
        if (type != class_1313.field_6308 || !entity.method_73183().method_8608()) {
            return movement;
        }
        class_746 player = class_310.method_1551().field_1724;
        if (player == null || player.method_5854() != entity || entity.method_5642() != player) {
            return movement;
        }
        class_243 velocity = entity.method_18798();
        BoatNoclipModule noclip = BoatNoclipModule.active;
        if (noclip != null && (entity instanceof class_10255)) {
            class_10255 boat = (class_10255) entity;
            class_243 result = noclip.boatMovement(boat, player);
            entity.method_36456(player.method_36454());
            if (movement == velocity) {
                entity.method_18799(result);
            }
            return result;
        }
        EntityControlModule control = EntityControlModule.active;
        EntitySpeedModule speed = EntitySpeedModule.active;
        BoatFlyModule fly = BoatFlyModule.active;
        double velX = velocity.field_1352;
        double velY = velocity.field_1351;
        double velZ = velocity.field_1350;
        boolean overridden = false;
        if (speed != null) {
            if (speed.appliesTo(entity)) {
                class_243 horizontal = horizontalVelocity(player, speed.horizontalSpeed());
                velX = horizontal.field_1352;
                velZ = horizontal.field_1350;
                overridden = true;
            }
        } else if (fly != null) {
            class_243 horizontal2 = horizontalVelocity(player, fly.horizontalSpeed());
            velX = horizontal2.field_1352;
            velZ = horizontal2.field_1350;
            overridden = true;
        }
        if (fly != null) {
            velY = fly.verticalVelocity();
            overridden = true;
        }
        if (fly != null || (control != null && control.locksYaw(entity))) {
            entity.method_36456(player.method_36454());
        }
        if (!overridden) {
            return movement;
        }
        class_243 result2 = new class_243(velX, velY, velZ);
        if (movement == velocity) {
            entity.method_18799(result2);
        }
        return result2;
    }

    public static boolean locksBoatTurning(class_10255 boat) {
        if (!anyActive || !boat.method_73183().method_8608()) {
            return false;
        }
        if (BoatFlyModule.active != null || BoatNoclipModule.active != null) {
            return true;
        }
        EntityControlModule control = EntityControlModule.active;
        return control != null && control.locksYaw(boat);
    }

    public static class_243 horizontalVelocity(class_746 player, double bps) {
        float yaw = player.method_36454();
        class_243 forward = class_243.method_1030(0.0f, yaw);
        class_243 right = class_243.method_1030(0.0f, yaw + 90.0f);
        class_10185 input = player.field_3913.field_54155;
        double velX = 0.0d;
        double velZ = 0.0d;
        boolean longitudinal = false;
        if (input.comp_3159()) {
            velX = 0.0d + ((forward.field_1352 / 20.0d) * bps);
            velZ = 0.0d + ((forward.field_1350 / 20.0d) * bps);
            longitudinal = true;
        }
        if (input.comp_3160()) {
            velX -= (forward.field_1352 / 20.0d) * bps;
            velZ -= (forward.field_1350 / 20.0d) * bps;
            longitudinal = true;
        }
        boolean lateral = false;
        if (input.comp_3162()) {
            velX += (right.field_1352 / 20.0d) * bps;
            velZ += (right.field_1350 / 20.0d) * bps;
            lateral = true;
        }
        if (input.comp_3161()) {
            velX -= (right.field_1352 / 20.0d) * bps;
            velZ -= (right.field_1350 / 20.0d) * bps;
            lateral = true;
        }
        if (longitudinal && lateral) {
            velX *= DIAGONAL;
            velZ *= DIAGONAL;
        }
        return new class_243(velX, 0.0d, velZ);
    }

    public static boolean isPhysicallyDown(class_304 mapping) {
        class_310 mc = class_310.method_1551();
        if (mc.field_1755 != null || mapping.method_1415() || isFreecamActive()) {
            return false;
        }
        class_3675.class_306 key = KeyBindingHelper.getBoundKeyOf(mapping);
        class_3675.class_307 type = key.method_1442();
        if (type == class_3675.class_307.field_1668) {
            return class_3675.method_15987(mc.method_22683(), key.method_1444());
        }
        if (type == class_3675.class_307.field_1672) {
            return GLFW.glfwGetMouseButton(mc.method_22683().method_4490(), key.method_1444()) == 1;
        }
        return mapping.method_1434();
    }

    public static boolean isOnAir(class_1297 entity) {
        return entity.method_73183().method_29546(entity.method_5829().method_1014(0.0625d).method_1012(0.0d, -0.55d, 0.0d)).allMatch((v0) -> {
            return v0.method_26215();
        });
    }

    private static boolean isFreecamActive() {
        FreecamModule freecam = (FreecamModule) ModuleRegistry.get(FreecamModule.class);
        return freecam != null && freecam.isEnabled();
    }
}
