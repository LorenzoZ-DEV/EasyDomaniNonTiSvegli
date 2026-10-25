package net.anubis.client.module.impl.render.character.kit;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Vec3.class */
@Environment(EnvType.CLIENT)
public final class Vec3 extends Record {
    private final float x;
    private final float y;
    private final float z;
    public static final Vec3 ZERO = new Vec3(0.0f, 0.0f, 0.0f);
    public static final Vec3 UP = new Vec3(0.0f, 1.0f, 0.0f);
    public static final Vec3 FORWARD = new Vec3(0.0f, 0.0f, 1.0f);
    public static final Vec3 LEFT = new Vec3(1.0f, 0.0f, 0.0f);

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override // java.lang.Record
    public final String toString() {
        return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Vec3.class), Vec3.class, "x;y;z", "FIELD:Lnet/anubis/client/module/impl/render/character/kit/Vec3;->x:F", "FIELD:Lnet/anubis/client/module/impl/render/character/kit/Vec3;->y:F", "FIELD:Lnet/anubis/client/module/impl/render/character/kit/Vec3;->z:F").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final int hashCode() {
        return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Vec3.class), Vec3.class, "x;y;z", "FIELD:Lnet/anubis/client/module/impl/render/character/kit/Vec3;->x:F", "FIELD:Lnet/anubis/client/module/impl/render/character/kit/Vec3;->y:F", "FIELD:Lnet/anubis/client/module/impl/render/character/kit/Vec3;->z:F").dynamicInvoker().invoke(this) /* invoke-custom */;
    }

    @Override // java.lang.Record
    public final boolean equals(Object o) {
        return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Vec3.class, Object.class), Vec3.class, "x;y;z", "FIELD:Lnet/anubis/client/module/impl/render/character/kit/Vec3;->x:F", "FIELD:Lnet/anubis/client/module/impl/render/character/kit/Vec3;->y:F", "FIELD:Lnet/anubis/client/module/impl/render/character/kit/Vec3;->z:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
    }

    public float x() {
        return this.x;
    }

    public float y() {
        return this.y;
    }

    public float z() {
        return this.z;
    }

    public static Vec3 of(float x, float y, float z) {
        return new Vec3(x, y, z);
    }

    public Vec3 add(Vec3 o) {
        return new Vec3(this.x + o.x, this.y + o.y, this.z + o.z);
    }

    public Vec3 add(float dx, float dy, float dz) {
        return new Vec3(this.x + dx, this.y + dy, this.z + dz);
    }

    public Vec3 sub(Vec3 o) {
        return new Vec3(this.x - o.x, this.y - o.y, this.z - o.z);
    }

    public Vec3 mul(float k) {
        return new Vec3(this.x * k, this.y * k, this.z * k);
    }

    public float dot(Vec3 o) {
        return (this.x * o.x) + (this.y * o.y) + (this.z * o.z);
    }

    public Vec3 cross(Vec3 o) {
        return new Vec3((this.y * o.z) - (this.z * o.y), (this.z * o.x) - (this.x * o.z), (this.x * o.y) - (this.y * o.x));
    }

    public float length() {
        return (float) Math.sqrt((this.x * this.x) + (this.y * this.y) + (this.z * this.z));
    }

    public float distance(Vec3 o) {
        return sub(o).length();
    }

    public Vec3 normalize() {
        float l = length();
        return l < 1.0E-8f ? ZERO : mul(1.0f / l);
    }

    public Vec3 lerp(Vec3 o, float t) {
        return new Vec3(this.x + ((o.x - this.x) * t), this.y + ((o.y - this.y) * t), this.z + ((o.z - this.z) * t));
    }

    public Vec3 mirrorX() {
        return new Vec3(-this.x, this.y, this.z);
    }

    public Vec3 rotate(float degX, float degY, float degZ) {
        return Mat3.euler(degX, degY, degZ).apply(this);
    }

    public Vec3 rotate(float degX, float degY, float degZ, Vec3 centre) {
        return Mat3.euler(degX, degY, degZ).apply(sub(centre)).add(centre);
    }
}
