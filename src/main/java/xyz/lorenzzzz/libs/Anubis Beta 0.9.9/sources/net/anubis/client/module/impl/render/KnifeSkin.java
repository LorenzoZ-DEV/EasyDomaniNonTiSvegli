package net.anubis.client.module.impl.render;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_7833;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/KnifeSkin.class */
@Environment(EnvType.CLIENT)
final class KnifeSkin {
    static final String NONE = "None";
    static final String BUTTERFLY = "Butterfly";
    static final String KARAMBIT = "Karambit";
    static final String KATANA = "Katana";
    static final int MAX_STREAKS = 2;
    private static final float WIPE_TAIL = 16.0f;
    private static final float SCABBARD_TURN = 0.6981317f;
    private static KnifeSkin butterfly;
    private static KnifeSkin karambit;
    private static KnifeSkin katana;
    final String name;
    final InspectAnimation.Clip inspect;
    final InspectAnimation.Clip deploy;
    private final Part[] parts;
    private final Streak[] streaks;
    private final float voxel;
    private final float gripX;
    private final float gripY;
    private final Vector3f fist;
    private final Vector3f elbow = new Vector3f();
    private final Quaternionf rest;
    private KnifeMesh scabbard;
    private float saddleX;
    private float saddleY;
    private float[] letGo;
    private Centre centre;
    private float bladeFrom;
    private float bladeTo;
    private static final Vector3f OFF_FOREARM = new Vector3f(0.4f, -0.85f, 0.35f).normalize();
    private static final Vector3f OFF_AWAY = new Vector3f(-0.3f, -0.75f, 0.1f);
    private static final Vector3f SCABBARD_FALL = new Vector3f(-0.3f, -0.9f, 0.25f);
    private static final float WIPE_LIFT = 3.0f;
    private static final float[][] BUTTERFLY_INSPECT = {new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}, new float[]{0.08f, -0.07f, 0.09f, 0.05f, -8.0f, 8.0f, 10.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 8.0f}, new float[]{0.14f, -0.09f, 0.12f, 0.06f, -12.0f, 8.0f, 14.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 12.0f}, new float[]{0.19f, -0.09f, 0.12f, 0.06f, -10.0f, 10.0f, 2.0f, -18.0f, 0.0f, 90.0f, 180.0f, 0.0f, 12.0f}, new float[]{0.24f, -0.09f, 0.13f, 0.06f, -12.0f, 8.0f, 10.0f, 0.0f, 0.0f, 180.0f, 360.0f, 0.0f, 12.0f}, new float[]{0.29f, -0.09f, 0.12f, 0.06f, -10.0f, 10.0f, 2.0f, 18.0f, 0.0f, 270.0f, 540.0f, 0.0f, 12.0f}, new float[]{0.34f, -0.09f, 0.12f, 0.06f, -12.0f, 8.0f, 14.0f, 0.0f, 0.0f, 360.0f, 720.0f, 0.0f, 12.0f}, new float[]{0.39f, -0.09f, 0.12f, 0.06f, -10.0f, 10.0f, 2.0f, -18.0f, 0.0f, 450.0f, 900.0f, 0.0f, 12.0f}, new float[]{0.44f, -0.09f, 0.13f, 0.06f, -12.0f, 8.0f, 10.0f, 0.0f, 0.0f, 540.0f, 1080.0f, 0.0f, 12.0f}, new float[]{0.49f, -0.09f, 0.12f, 0.06f, -10.0f, 10.0f, 2.0f, 18.0f, 0.0f, 630.0f, 1260.0f, 0.0f, 12.0f}, new float[]{0.54f, -0.09f, 0.12f, 0.06f, -12.0f, 8.0f, 14.0f, 0.0f, 0.0f, 720.0f, 1440.0f, 0.0f, 12.0f}, new float[]{0.62f, -0.1f, 0.12f, 0.07f, -35.0f, 6.0f, 20.0f, 0.0f, 0.0f, 720.0f, 1440.0f, 0.0f, 0.0f}, new float[]{0.76f, -0.1f, 0.13f, 0.07f, 25.0f, 6.0f, 16.0f, 0.0f, 0.0f, 720.0f, 1440.0f, 1.0f, 0.0f}, new float[]{0.88f, -0.05f, 0.06f, 0.03f, 8.0f, WIPE_LIFT, 6.0f, 0.0f, 0.0f, 720.0f, 1440.0f, 1.0f, 0.0f}, new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 720.0f, 1440.0f, 1.0f, 0.0f}};
    private static final float[][] BUTTERFLY_DEPLOY = {new float[]{0.0f, 0.02f, -0.14f, 0.02f, 0.0f, -30.0f, -20.0f, 0.0f, 0.0f, 180.0f, 0.0f, 0.0f, 10.0f}, new float[]{0.3f, 0.01f, -0.04f, 0.01f, 0.0f, -10.0f, -10.0f, 0.0f, 0.0f, 220.0f, 100.0f, 0.0f, 10.0f}, new float[]{0.62f, 0.0f, 0.01f, 0.0f, 0.0f, 4.0f, 6.0f, 0.0f, 0.0f, 350.0f, 340.0f, 0.0f, WIPE_LIFT}, new float[]{0.8f, 0.0f, 0.0f, 0.0f, 0.0f, -2.0f, -2.0f, 0.0f, 0.0f, 365.0f, 368.0f, 0.0f, 0.0f}, new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 360.0f, 360.0f, 0.0f, 0.0f}};
    private static final float[][] KARAMBIT_INSPECT = {new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}, new float[]{0.1f, -0.12f, 0.14f, 0.06f, -10.0f, 14.0f, 10.0f, 0.0f, 0.0f, -25.0f, 0.0f, 0.0f}, new float[]{0.14f, -0.14f, 0.16f, 0.06f, -10.0f, 14.0f, 10.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}, new float[]{0.205f, -0.14f, 0.17f, 0.06f, -10.0f, 14.0f, 10.0f, 0.0f, 0.0f, 180.0f, 0.0f, 0.0f}, new float[]{0.27f, -0.14f, 0.16f, 0.06f, -10.0f, 14.0f, 10.0f, 0.0f, 0.0f, 360.0f, 0.0f, 0.0f}, new float[]{0.335f, -0.14f, 0.17f, 0.06f, -10.0f, 14.0f, 10.0f, 0.0f, 0.0f, 540.0f, 0.0f, 0.0f}, new float[]{0.4f, -0.14f, 0.16f, 0.06f, -10.0f, 14.0f, 10.0f, 0.0f, 0.0f, 720.0f, 0.0f, 0.0f}, new float[]{0.48f, -0.13f, 0.16f, 0.06f, -10.0f, 12.0f, 10.0f, 0.0f, 0.0f, 750.0f, 0.0f, 0.0f}, new float[]{0.56f, -0.1f, 0.12f, 0.06f, 20.0f, 10.0f, -70.0f, 0.0f, 0.0f, 720.0f, 0.0f, 0.0f}, new float[]{0.72f, -0.1f, 0.13f, 0.06f, -10.0f, 10.0f, -60.0f, 0.0f, 0.0f, 720.0f, 0.0f, 1.0f}, new float[]{0.86f, -0.06f, 0.08f, 0.05f, -6.0f, 4.0f, 8.0f, 0.0f, 0.0f, 360.0f, 0.0f, 1.0f}, new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 360.0f, 0.0f, 1.0f}};
    private static final float[][] KARAMBIT_DEPLOY = {new float[]{0.0f, 0.02f, -0.14f, 0.02f, 0.0f, -30.0f, -20.0f, 0.0f, 0.0f, -540.0f, 0.0f, 0.0f}, new float[]{0.55f, 0.0f, 0.0f, 0.0f, 0.0f, 4.0f, 4.0f, 0.0f, 0.0f, -60.0f, 0.0f, 0.0f}, new float[]{0.78f, 0.0f, 0.0f, 0.0f, 0.0f, -2.0f, -2.0f, 0.0f, 0.0f, 12.0f, 0.0f, 0.0f}, new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}};
    private static final float[][] KATANA_INSPECT = {new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 4.0f}, new float[]{0.1f, -0.06f, 0.12f, 0.06f, 0.0f, 0.0f, 60.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.6f, 4.0f}, new float[]{0.15f, -0.07f, 0.14f, 0.08f, -2.0f, 0.0f, 60.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 4.0f}, new float[]{0.2f, -0.07f, 0.14f, 0.08f, -2.0f, 0.0f, 60.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 12.0f}, new float[]{0.36f, -0.07f, 0.14f, 0.08f, 0.0f, 0.0f, 58.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 44.0f}, new float[]{0.4f, -0.07f, 0.14f, 0.08f, 0.0f, 0.0f, 58.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 53.0f}, new float[]{0.44f, -0.06f, 0.13f, 0.06f, 2.0f, 1.0f, 54.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.6f, 58.0f}, new float[]{0.48f, -0.05f, 0.11f, 0.03f, 4.0f, 2.0f, 42.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 62.0f}, new float[]{0.52f, -0.04f, 0.1f, 0.02f, 0.0f, 4.0f, 20.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 62.0f}, new float[]{0.6f, -0.04f, 0.12f, 0.02f, 0.0f, 4.0f, -160.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 62.0f}, new float[]{0.68f, -0.04f, 0.1f, 0.02f, 0.0f, 4.0f, -340.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 62.0f}, new float[]{0.78f, 0.04f, -0.03f, 0.0f, 0.0f, -6.0f, -400.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 62.0f}, new float[]{0.88f, 0.0f, 0.02f, 0.0f, 0.0f, 2.0f, -365.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 62.0f}, new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -360.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 62.0f}};
    private static final float[][] KATANA_DEPLOY = {new float[]{0.0f, 0.06f, 0.0f, 0.02f, -10.0f, 0.0f, 45.0f, 180.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f}, new float[]{0.05f, 0.05f, 0.01f, 0.01f, -12.0f, 0.0f, 47.0f, 180.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 1.0f, 0.0f}, new float[]{0.18f, 0.15f, 0.0f, 0.05f, -10.0f, 0.0f, 45.0f, 180.0f, 0.0f, 0.0f, 0.0f, 0.3f, 0.0f, 16.0f, 1.0f, 0.0f}, new float[]{0.3f, 0.3f, -0.02f, 0.1f, -8.0f, 0.0f, 43.0f, 180.0f, 0.0f, 0.0f, 0.0f, 0.9f, 0.0f, 50.0f, 1.0f, 0.0f}, new float[]{0.33f, 0.3f, 0.0f, 0.1f, -4.0f, 4.0f, 36.0f, 180.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 52.0f, 1.0f, 0.0f}, new float[]{0.41f, 0.08f, 0.12f, 0.04f, 0.0f, 10.0f, -40.0f, 180.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 52.0f, 0.25f, 0.0f}, new float[]{0.47f, -0.02f, 0.1f, 0.02f, 5.0f, 8.0f, -100.0f, 180.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 52.0f, 0.0f, 0.0f}, new float[]{0.62f, -0.02f, 0.08f, 0.02f, 0.0f, 6.0f, -460.0f, 180.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 52.0f, 0.0f, 0.0f}, new float[]{0.71f, 0.04f, -0.04f, 0.0f, 0.0f, -6.0f, -520.0f, 180.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 52.0f, 0.0f, 0.0f}, new float[]{0.82f, 0.0f, 0.02f, 0.0f, 0.0f, 2.0f, -370.0f, 300.0f, 0.0f, 0.0f, 0.0f, 2.0f, 0.0f, 52.0f, 0.0f, 0.0f}, new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -360.0f, 360.0f, 0.0f, 0.0f, 0.0f, WIPE_LIFT, 0.0f, 52.0f, 0.0f, 0.0f}};

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/KnifeSkin$Centre.class */
    @Environment(EnvType.CLIENT)
    private interface Centre {
        float y(float f);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/KnifeSkin$Sink.class */
    @Environment(EnvType.CLIENT)
    interface Sink {
        void part(class_4587 class_4587Var, KnifeMesh knifeMesh, KnifeMesh.Shine shine);

        void glow(class_4587 class_4587Var, KnifeMesh knifeMesh, KnifeMesh.Shine shine);

        void arm(class_4587 class_4587Var, boolean z);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/KnifeSkin$Part.class */
    @Environment(EnvType.CLIENT)
    private static final class Part extends Record {
        private final KnifeMesh mesh;
        private final int channel;
        private final float pivotX;
        private final float pivotY;

        private Part(KnifeMesh mesh, int channel, float pivotX, float pivotY) {
            this.mesh = mesh;
            this.channel = channel;
            this.pivotX = pivotX;
            this.pivotY = pivotY;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Part.class), Part.class, "mesh;channel;pivotX;pivotY", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Part;->mesh:Lnet/anubis/client/module/impl/render/KnifeMesh;", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Part;->channel:I", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Part;->pivotX:F", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Part;->pivotY:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Part.class), Part.class, "mesh;channel;pivotX;pivotY", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Part;->mesh:Lnet/anubis/client/module/impl/render/KnifeMesh;", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Part;->channel:I", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Part;->pivotX:F", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Part;->pivotY:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Part.class, Object.class), Part.class, "mesh;channel;pivotX;pivotY", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Part;->mesh:Lnet/anubis/client/module/impl/render/KnifeMesh;", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Part;->channel:I", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Part;->pivotX:F", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Part;->pivotY:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public KnifeMesh mesh() {
            return this.mesh;
        }

        public int channel() {
            return this.channel;
        }

        public float pivotX() {
            return this.pivotX;
        }

        public float pivotY() {
            return this.pivotY;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/KnifeSkin$Streak.class */
    @Environment(EnvType.CLIENT)
    static final class Streak extends Record {
        private final int part;
        private final float baseX;
        private final float baseY;
        private final float tipX;
        private final float tipY;
        private final float strength;

        Streak(int part, float baseX, float baseY, float tipX, float tipY, float strength) {
            this.part = part;
            this.baseX = baseX;
            this.baseY = baseY;
            this.tipX = tipX;
            this.tipY = tipY;
            this.strength = strength;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Streak.class), Streak.class, "part;baseX;baseY;tipX;tipY;strength", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Streak;->part:I", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Streak;->baseX:F", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Streak;->baseY:F", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Streak;->tipX:F", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Streak;->tipY:F", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Streak;->strength:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Streak.class), Streak.class, "part;baseX;baseY;tipX;tipY;strength", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Streak;->part:I", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Streak;->baseX:F", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Streak;->baseY:F", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Streak;->tipX:F", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Streak;->tipY:F", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Streak;->strength:F").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Streak.class, Object.class), Streak.class, "part;baseX;baseY;tipX;tipY;strength", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Streak;->part:I", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Streak;->baseX:F", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Streak;->baseY:F", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Streak;->tipX:F", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Streak;->tipY:F", "FIELD:Lnet/anubis/client/module/impl/render/KnifeSkin$Streak;->strength:F").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public int part() {
            return this.part;
        }

        public float baseX() {
            return this.baseX;
        }

        public float baseY() {
            return this.baseY;
        }

        public float tipX() {
            return this.tipX;
        }

        public float tipY() {
            return this.tipY;
        }

        public float strength() {
            return this.strength;
        }
    }

    private KnifeSkin(String name, Part[] parts, Streak[] streaks, float voxel, float gripX, float gripY, Vector3f fist, Vector3f blade, Vector3f spine, InspectAnimation.Clip inspect, InspectAnimation.Clip deploy) {
        this.name = name;
        this.parts = parts;
        this.streaks = streaks;
        this.voxel = voxel;
        this.gripX = gripX;
        this.gripY = gripY;
        this.fist = fist;
        this.inspect = inspect;
        this.deploy = deploy;
        Vector3f x = new Vector3f(blade).normalize();
        Vector3f y = new Vector3f(spine).fma(-spine.dot(x), x).normalize();
        this.rest = new Quaternionf().setFromNormalized(new Matrix3f(x, y, new Vector3f(x).cross(y)));
        fist.fma(0.6f, FirstPersonArm.FOREARM, this.elbow);
    }

    static KnifeSkin of(String name) {
        switch (name) {
            case "Butterfly":
                if (butterfly != null) {
                    return butterfly;
                }
                KnifeSkin knifeSkinButterfly = butterfly();
                butterfly = knifeSkinButterfly;
                return knifeSkinButterfly;
            case "Karambit":
                if (karambit != null) {
                    return karambit;
                }
                KnifeSkin knifeSkinKarambit = karambit();
                karambit = knifeSkinKarambit;
                return knifeSkinKarambit;
            case "Katana":
                if (katana != null) {
                    return katana;
                }
                KnifeSkin knifeSkinKatana = katana();
                katana = knifeSkinKatana;
                return knifeSkinKatana;
            default:
                return null;
        }
    }

    private static KnifeSkin butterfly() {
        Part[] parts = {new Part(KnifeModels.butterflyHandle(1), -1, -1.5f, 0.5f), new Part(KnifeModels.butterflyBlade(), 8, -1.5f, 0.5f), new Part(KnifeModels.butterflyHandle(-1), 9, -1.5f, 0.5f)};
        Streak[] streaks = {new Streak(1, 2.5f, 0.5f, 21.5f, -0.5f, 1.0f), new Streak(2, -4.0f, 0.5f, -27.5f, 0.5f, 0.8f)};
        return new KnifeSkin(BUTTERFLY, parts, streaks, 0.021f, -10.0f, 0.5f, new Vector3f(-0.06f, 0.06f, 0.02f), new Vector3f(-0.15f, 1.0f, -0.1f), new Vector3f(1.0f, 0.15f, 0.3f), new InspectAnimation.Clip(BUTTERFLY_INSPECT, 3.8f, 0.14f, 0.54f), new InspectAnimation.Clip(BUTTERFLY_DEPLOY, 0.9f));
    }

    private static KnifeSkin karambit() {
        Part[] parts = {new Part(KnifeModels.karambit(), 8, -16.0f, -1.0f)};
        float[] tip = KnifeModels.KARAMBIT_TIP;
        Streak[] streaks = {new Streak(0, 6.0f * 2, 0.5f * 2, tip[0], tip[1], 1.0f)};
        return new KnifeSkin(KARAMBIT, parts, streaks, 0.022f / 2, (-16.0f) + 2.0f, -1.0f, new Vector3f(-0.06f, 0.1f, 0.02f), new Vector3f(-0.5f, -0.8f, -0.3f), new Vector3f(0.8f, -0.5f, 0.35f), new InspectAnimation.Clip(KARAMBIT_INSPECT, 3.2f, 0.14f, 0.4f), new InspectAnimation.Clip(KARAMBIT_DEPLOY, 0.8f));
    }

    private static KnifeSkin katana() {
        Part[] parts = {new Part(KnifeModels.katana(), -1, 0.0f, 0.0f)};
        Streak[] streaks = {new Streak(0, 6.0f, KnifeModels.katanaBow(6) + 0.5f, 48.5f, KnifeModels.katanaBow(48) + 1.5f, 1.0f)};
        KnifeSkin skin = new KnifeSkin(KATANA, parts, streaks, 0.028f, -5.0f, 0.5f, new Vector3f(-0.06f, 0.02f, 0.02f), new Vector3f(-0.6f, 0.8f, -0.2f), new Vector3f(0.6f, 0.45f, 0.1f), new InspectAnimation.Clip(KATANA_INSPECT, 4.4f, 0.52f, 0.68f), new InspectAnimation.Clip(KATANA_DEPLOY, 2.0f));
        skin.scabbard = KnifeModels.katanaScabbard();
        skin.saddleX = 20.0f;
        skin.saddleY = 0.0f;
        skin.centre = x -> {
            if (x < WIPE_LIFT) {
                return 0.5f;
            }
            return KnifeModels.katanaCentre(x);
        };
        skin.bladeFrom = WIPE_LIFT;
        skin.bladeTo = 49.0f;
        skin.letGo = skin.findLetGo();
        return skin;
    }

    private float[] findLetGo() {
        float[] at = new float[16];
        float[] held = new float[16];
        for (int i = 0; i <= 2000; i++) {
            this.deploy.sample(i / 2000.0f, at);
            if (at[13] < 1.0f) {
                break;
            }
            System.arraycopy(at, 0, held, 0, 16);
        }
        return held;
    }

    int streakCount() {
        return this.streaks.length;
    }

    float streakStrength(int streak) {
        return this.streaks[streak].strength;
    }

    void draw(class_4587 pose, float[] at, float weight, boolean slim, float size, Sink sink, float[] trail) {
        Matrix4f knife = knifeMatrix(at, weight, size, new Matrix4f());
        Vector3f hand = new Vector3f(this.fist).add(at[0] * weight, at[1] * weight, at[2] * weight);
        FirstPersonArm.place(pose, hand, this.elbow, slim, posed -> {
            sink.arm(posed, false);
        });
        Matrix4f sheath = (this.scabbard == null || weight < 0.999f || at[13] <= 0.01f) ? null : scabbardMatrix(at, size);
        Vector3f other = null;
        if (sheath != null) {
            other = sheath.transformPosition(this.saddleX, this.saddleY, 0.5f, new Vector3f());
        } else if (this.centre != null && at[14] * weight > 0.01f) {
            float t = class_3532.method_15363(at[14] * weight, 0.0f, 1.0f);
            float x = at[15];
            float y = this.centre.y(x) + (x >= this.bladeFrom ? WIPE_LIFT : 0.0f);
            other = knife.transformPosition(x, y, 0.5f, new Vector3f()).fma(1.0f - ((t * t) * (WIPE_LIFT - (2.0f * t))), OFF_AWAY);
        }
        if (other != null) {
            pose.method_22903();
            pose.method_22905(-1.0f, 1.0f, 1.0f);
            Vector3f mirrored = new Vector3f(-other.x, other.y, other.z);
            FirstPersonArm.place(pose, mirrored, new Vector3f(mirrored).fma(0.6f, OFF_FOREARM), slim, posed2 -> {
                sink.arm(posed2, true);
            });
            pose.method_22909();
        }
        KnifeMesh.Shine wipe = wipe(at, weight);
        KnifeMesh.Shine shine = wipe;
        if (shine == null) {
            float sweep = shinePosition(at[10]);
            shine = new KnifeMesh.Shine(sweep, sweep > 0.0f ? class_3532.method_15374(3.1415927f * sweep) * weight : 0.0f, 0.0f);
        }
        pose.method_22903();
        pose.method_34425(knife);
        for (int i = 0; i < this.parts.length; i++) {
            Part part = this.parts[i];
            pose.method_22903();
            if (part.channel >= 0) {
                float angle = class_3532.method_15393(at[part.channel]) * weight;
                if (angle != 0.0f) {
                    pose.method_49278(class_7833.field_40718.rotationDegrees(angle), part.pivotX, part.pivotY, 0.5f);
                }
            }
            sink.part(pose, part.mesh, shine);
            if (wipe != null) {
                sink.glow(pose, part.mesh, wipe);
            }
            if (trail != null) {
                streaks(i, pose.method_23760().method_23761(), trail);
            }
            pose.method_22909();
        }
        pose.method_22909();
        if (sheath != null) {
            pose.method_22903();
            pose.method_34425(sheath);
            sink.part(pose, this.scabbard, KnifeMesh.Shine.NONE);
            pose.method_22909();
        }
    }

    private void streaks(int part, Matrix4f m, float[] trail) {
        Vector3f p = new Vector3f();
        for (int s = 0; s < this.streaks.length; s++) {
            Streak streak = this.streaks[s];
            if (streak.part == part) {
                m.transformPosition(streak.baseX, streak.baseY, 0.5f, p);
                trail[s * 6] = p.x;
                trail[(s * 6) + 1] = p.y;
                trail[(s * 6) + 2] = p.z;
                m.transformPosition(streak.tipX, streak.tipY, 0.5f, p);
                trail[(s * 6) + 3] = p.x;
                trail[(s * 6) + 4] = p.y;
                trail[(s * 6) + 5] = p.z;
            }
        }
    }

    private Matrix4f knifeMatrix(float[] at, float weight, float size, Matrix4f dest) {
        float turn = 0.017453292f * weight;
        float scale = this.voxel * size;
        dest.translation(this.fist.x + (at[0] * weight), this.fist.y + (at[1] * weight), this.fist.z + (at[2] * weight)).rotate(new Quaternionf().rotateY(class_3532.method_15393(at[3]) * turn).rotateX(class_3532.method_15393(at[4]) * turn).rotateZ(class_3532.method_15393(at[5]) * turn)).rotate(this.rest);
        float flip = class_3532.method_15393(at[6]) * turn;
        if (flip != 0.0f) {
            dest.rotateX(flip);
        }
        return dest.scale(scale).translate((at[11] * weight) - this.gripX, -this.gripY, -0.5f);
    }

    private Matrix4f scabbardMatrix(float[] at, float size) {
        boolean held = at[13] >= 1.0f;
        float[] state = held ? at : this.letGo;
        Matrix4f m = knifeMatrix(state, 1.0f, size, new Matrix4f()).rotateAround(new Quaternionf().rotationZ(state[12] / 312.5f), WIPE_LIFT, 312.5f, 0.5f).translate(0.05f, 0.0f, 0.0f);
        if (!held) {
            float fall = 1.0f - Math.max(at[13], 0.0f);
            float fall2 = fall * fall;
            Vector3f hold = m.transformPosition(this.saddleX, this.saddleY, 0.5f, new Vector3f());
            m.translateLocal(-hold.x, -hold.y, -hold.z).rotateLocalZ(SCABBARD_TURN * fall2).translateLocal(hold.x + (SCABBARD_FALL.x * fall2), hold.y + (SCABBARD_FALL.y * fall2), hold.z + (SCABBARD_FALL.z * fall2));
        }
        return m;
    }

    private KnifeMesh.Shine wipe(float[] at, float weight) {
        float touch = this.centre != null ? class_3532.method_15363(((at[14] * weight) - 0.7f) / 0.3f, 0.0f, 1.0f) : 0.0f;
        if (touch <= 0.01f || at[15] < this.bladeFrom) {
            return null;
        }
        float along = (at[15] - this.bladeFrom) / (this.bladeTo - this.bladeFrom);
        return new KnifeMesh.Shine(along, touch * touch * (WIPE_LIFT - (2.0f * touch)), 16.0f);
    }

    float sparkle(float[] at, float weight) {
        float flash = sparkle(at[10]);
        if (this.centre != null && at[15] >= this.bladeFrom) {
            float along = (at[15] - this.bladeFrom) / (this.bladeTo - this.bladeFrom);
            float leaving = class_3532.method_15363((along - 0.96f) / 0.06f, 0.0f, 1.0f) * class_3532.method_15363((1.3f - along) / 0.15f, 0.0f, 1.0f);
            flash = Math.max(flash, leaving * class_3532.method_15363(at[14] * 1.6f, 0.0f, 1.0f));
        }
        return flash * weight;
    }

    static float shinePosition(float channel) {
        if (channel <= 0.0f) {
            return 0.0f;
        }
        float f = channel - (2.0f * class_3532.method_15375(channel / 2.0f));
        if (f < 1.0f) {
            return f;
        }
        return 0.0f;
    }

    static float sparkle(float channel) {
        float f = shinePosition(channel);
        return class_3532.method_15363((f - 0.72f) / 0.16f, 0.0f, 1.0f) * class_3532.method_15363((1.0f - f) / 0.07f, 0.0f, 1.0f);
    }
}
