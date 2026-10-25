package net.anubis.client.module.impl.movement;

import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.impl.movement.entitycontrol.EntityControlVehicleHook;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/movement/EntitySpeedModule.class */
@Environment(EnvType.CLIENT)
public final class EntitySpeedModule extends ClientModule {
    public static volatile EntitySpeedModule active;
    private final NumberSetting horizontalSpeed;
    private final BooleanSetting onlyOnGround;
    private final BooleanSetting inWater;

    public EntitySpeedModule() {
        super("entity-speed", "Entity Speed", "Makes you go faster horizontally when riding entities.", ModuleCategory.MOVEMENT);
        this.horizontalSpeed = (NumberSetting) addSetting("horizontal-speed", new NumberSetting("Horizontal Speed", 10.0f, 0.0f, 50.0f).step(0.5f));
        this.onlyOnGround = (BooleanSetting) addSetting("only-on-ground", new BooleanSetting("Only On Ground", false));
        this.inWater = (BooleanSetting) addSetting("in-water", new BooleanSetting("In Water", true));
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        active = this;
        EntityControlVehicleHook.refresh();
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        active = null;
        EntityControlVehicleHook.refresh();
    }

    public boolean appliesTo(class_1297 vehicle) {
        return (!this.onlyOnGround.get() || vehicle.method_24828() || vehicle.method_70987()) && (this.inWater.get() || !vehicle.method_5799());
    }

    public double horizontalSpeed() {
        return this.horizontalSpeed.get();
    }
}
