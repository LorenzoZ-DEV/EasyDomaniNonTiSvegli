package net.anubis.client.module.impl.movement;

import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/movement/SprintModule.class */
@Environment(EnvType.CLIENT)
public final class SprintModule extends ClientModule {
    private static volatile SprintModule active;

    public SprintModule() {
        super("sprint", "Sprint", "Keeps you sprinting while you move forward, like vanilla Toggle Sprint.", ModuleCategory.MOVEMENT);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        active = this;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        active = null;
    }

    public static boolean holding() {
        return active != null;
    }
}
