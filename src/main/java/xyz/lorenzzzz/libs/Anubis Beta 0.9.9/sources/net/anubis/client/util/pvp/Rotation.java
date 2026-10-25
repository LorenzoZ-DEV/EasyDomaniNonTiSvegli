package net.anubis.client.util.pvp;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/pvp/Rotation.class */
@Environment(EnvType.CLIENT)
public final class Rotation extends Record {
    private final float yaw;
    private final float pitch;

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override // java.lang.Record
    public final String toString() {
        return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Rotation.class), Rotation.class, "yaw;pitch", "FIELD:Lnet/anubis/client/util/pvp/Rotation;->yaw:F", "FIELD:Lnet/anubis/client/util/pvp/Rotation;->pitch:F").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Rotation.class), Rotation.class, "yaw;pitch", "FIELD:Lnet/anubis/client/util/pvp/Rotation;->yaw:F", "FIELD:Lnet/anubis/client/util/pvp/Rotation;->pitch:F").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final boolean equals(Object o) {
        return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Rotation.class, Object.class), Rotation.class, "yaw;pitch", "FIELD:Lnet/anubis/client/util/pvp/Rotation;->yaw:F", "FIELD:Lnet/anubis/client/util/pvp/Rotation;->pitch:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
    }

    public float yaw() {
        return this.yaw;
    }

    public float pitch() {
        return this.pitch;
    }
}
