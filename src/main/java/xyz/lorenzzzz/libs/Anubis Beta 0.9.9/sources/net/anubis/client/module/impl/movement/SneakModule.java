package net.anubis.client.module.impl.movement;

import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.ModeSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/movement/SneakModule.class */
@Environment(EnvType.CLIENT)
public final class SneakModule extends ClientModule {
    public static volatile SneakModule active;
    private final ModeSetting mode;

    public SneakModule() {
        super("sneak", "Sneak", "Sneaks for you.", ModuleCategory.MOVEMENT);
        this.mode = (ModeSetting) addSetting("mode", new ModeSetting("Mode", "Vanilla", "Packet", "Vanilla"));
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        active = this;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        active = null;
    }

    public boolean doPacket() {
        return isEnabled() && this.mode.is("Packet") && !isFlying();
    }

    public boolean doVanilla() {
        return isEnabled() && this.mode.is("Vanilla") && !isFlying();
    }

    private static boolean isFlying() {
        class_746 player = class_310.method_1551().field_1724;
        return player == null || player.method_31549().field_7479;
    }
}
