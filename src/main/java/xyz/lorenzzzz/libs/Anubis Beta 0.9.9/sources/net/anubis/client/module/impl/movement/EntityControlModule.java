package net.anubis.client.module.impl.movement;

import net.anubis.client.event.impl.PacketReceiveNettyEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.impl.movement.entitycontrol.EntityControlVehicleHook;
import net.anubis.client.module.setting.BooleanSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10255;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_2692;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/movement/EntityControlModule.class */
@Environment(EnvType.CLIENT)
public final class EntityControlModule extends ClientModule {
    public static volatile EntityControlModule active;
    private final BooleanSetting boats;
    private final BooleanSetting horses;
    private final BooleanSetting camels;
    private final BooleanSetting pigs;
    private final BooleanSetting striders;
    private final BooleanSetting nautiluses;
    private final BooleanSetting happyGhasts;
    private final BooleanSetting spoofSaddle;
    private final BooleanSetting maxJump;
    private final BooleanSetting lockYaw;
    private final BooleanSetting cancelServerPackets;

    public EntityControlModule() {
        super("entity-control", "Entity Control", "Lets you control rideable entities without a saddle.", ModuleCategory.MOVEMENT);
        this.boats = (BooleanSetting) addSetting("boats", new BooleanSetting("Boats", true));
        this.horses = (BooleanSetting) addSetting("horses", new BooleanSetting("Horses", true));
        this.camels = (BooleanSetting) addSetting("camels", new BooleanSetting("Camels", true));
        this.pigs = (BooleanSetting) addSetting("pigs", new BooleanSetting("Pigs", true));
        this.striders = (BooleanSetting) addSetting("striders", new BooleanSetting("Striders", true));
        this.nautiluses = (BooleanSetting) addSetting("nautiluses", new BooleanSetting("Nautiluses", true));
        this.happyGhasts = (BooleanSetting) addSetting("happy-ghasts", new BooleanSetting("Happy Ghasts", true));
        this.spoofSaddle = (BooleanSetting) addSetting("spoof-saddle", new BooleanSetting("Spoof Saddle", false));
        this.maxJump = (BooleanSetting) addSetting("max-jump", new BooleanSetting("Max Jump", true));
        this.lockYaw = (BooleanSetting) addSetting("lock-yaw", new BooleanSetting("Lock Yaw", true));
        this.cancelServerPackets = (BooleanSetting) addSetting("cancel-server-packets", new BooleanSetting("Cancel Server Packets", false));
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        active = this;
        EntityControlVehicleHook.refresh();
        on(PacketReceiveNettyEvent.TYPE, this::onPacketReceive);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        active = null;
        EntityControlVehicleHook.refresh();
    }

    private void onPacketReceive(PacketReceiveNettyEvent event) {
        if (this.cancelServerPackets.get() && (event.packet instanceof class_2692)) {
            event.setCancelled(true);
        }
    }

    public static boolean spoofSaddle() {
        EntityControlModule module = active;
        return module != null && module.spoofSaddle.get();
    }

    public static boolean maxJump() {
        EntityControlModule module = active;
        return module != null && module.maxJump.get();
    }

    public boolean locksYaw(class_1297 vehicle) {
        return this.lockYaw.get() && isTarget(vehicle);
    }

    private boolean isTarget(class_1297 entity) {
        if (entity instanceof class_10255) {
            return this.boats.get();
        }
        class_1299<?> type = entity.method_5864();
        if (type == class_1299.field_6139 || type == class_1299.field_6067 || type == class_1299.field_6057 || type == class_1299.field_6075 || type == class_1299.field_6048) {
            return this.horses.get();
        }
        if (type == class_1299.field_40116 || type == class_1299.field_64132) {
            return this.camels.get();
        }
        if (type == class_1299.field_6093) {
            return this.pigs.get();
        }
        if (type == class_1299.field_23214) {
            return this.striders.get();
        }
        if (type == class_1299.field_63289 || type == class_1299.field_63290) {
            return this.nautiluses.get();
        }
        if (type == class_1299.field_59668) {
            return this.happyGhasts.get();
        }
        return false;
    }
}
