package net.anubis.client.gui;

import com.sun.jna.Platform;
import net.anubis.client.module.impl.misc.NoInvLeakModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/BorderlessFullscreen.class */
@Environment(EnvType.CLIENT)
public final class BorderlessFullscreen {

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/BorderlessFullscreen$Switchable.class */
    @Environment(EnvType.CLIENT)
    public interface Switchable {
        boolean anubis$isBorderless();

        void anubis$reapplyMode();
    }

    private BorderlessFullscreen() {
    }

    public static boolean wanted() {
        return Platform.isWindows() && NoInvLeakModule.wantsBorderless();
    }

    public static void tick() {
        Switchable switchableMethod_22683 = class_310.method_1551().method_22683();
        if (switchableMethod_22683.method_4498()) {
            Switchable switchable = switchableMethod_22683;
            if (wanted() != switchable.anubis$isBorderless()) {
                switchable.anubis$reapplyMode();
            }
        }
    }
}
