package net.anubis.client.module.impl.render;

import com.example.client.mixin.AccessorHandItemLayer;
import com.example.client.mixin.AccessorHandItemRenderState;
import com.example.client.mixin.AccessorSkinChangeEntityRenderDispatcher;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.function.Consumer;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.impl.misc.SkinChangeModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1007;
import net.minecraft.class_10444;
import net.minecraft.class_11659;
import net.minecraft.class_1309;
import net.minecraft.class_1664;
import net.minecraft.class_1747;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_742;
import net.minecraft.class_746;
import net.minecraft.class_7833;
import net.minecraft.class_7920;
import net.minecraft.class_804;
import net.minecraft.class_811;
import net.minecraft.class_8685;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/FirstPersonArm.class */
@Environment(EnvType.CLIENT)
final class FirstPersonArm {
    static final float ELBOW = 0.6f;
    private static final float ARM_FIST = 0.5f;
    private static final float ARM_SHOULDER = -0.625f;
    private static final float SHOULDER_STRETCH = 3.0f;
    private static final float HANDLE_INSET = 0.1875f;
    private static final float BLOCK_BELOW = 0.17f;
    static final Vector3f FOREARM = new Vector3f(0.75f, -0.45f, 0.5f).normalize();
    private static final Vector3f ARM_UP = new Vector3f(0.0f, 1.0f, 0.0f);
    private static final Vector3f BLOCK_SHIFT = new Vector3f(0.09f, 0.0f, 0.06f);
    private static final class_10444 MODEL = new class_10444();

    private FirstPersonArm() {
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/FirstPersonArm$Arm.class */
    @Environment(EnvType.CLIENT)
    static final class Arm extends Record {
        private final class_1007<class_742> renderer;
        private final class_2960 texture;
        private final boolean rightSleeve;
        private final boolean leftSleeve;
        private final boolean slim;

        Arm(class_1007<class_742> renderer, class_2960 texture, boolean rightSleeve, boolean leftSleeve, boolean slim) {
            this.renderer = renderer;
            this.texture = texture;
            this.rightSleeve = rightSleeve;
            this.leftSleeve = leftSleeve;
            this.slim = slim;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Arm.class), Arm.class, "renderer;texture;rightSleeve;leftSleeve;slim", "FIELD:Lnet/anubis/client/module/impl/render/FirstPersonArm$Arm;->renderer:Lnet/minecraft/class_1007;", "FIELD:Lnet/anubis/client/module/impl/render/FirstPersonArm$Arm;->texture:Lnet/minecraft/class_2960;", "FIELD:Lnet/anubis/client/module/impl/render/FirstPersonArm$Arm;->rightSleeve:Z", "FIELD:Lnet/anubis/client/module/impl/render/FirstPersonArm$Arm;->leftSleeve:Z", "FIELD:Lnet/anubis/client/module/impl/render/FirstPersonArm$Arm;->slim:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Arm.class), Arm.class, "renderer;texture;rightSleeve;leftSleeve;slim", "FIELD:Lnet/anubis/client/module/impl/render/FirstPersonArm$Arm;->renderer:Lnet/minecraft/class_1007;", "FIELD:Lnet/anubis/client/module/impl/render/FirstPersonArm$Arm;->texture:Lnet/minecraft/class_2960;", "FIELD:Lnet/anubis/client/module/impl/render/FirstPersonArm$Arm;->rightSleeve:Z", "FIELD:Lnet/anubis/client/module/impl/render/FirstPersonArm$Arm;->leftSleeve:Z", "FIELD:Lnet/anubis/client/module/impl/render/FirstPersonArm$Arm;->slim:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Arm.class, Object.class), Arm.class, "renderer;texture;rightSleeve;leftSleeve;slim", "FIELD:Lnet/anubis/client/module/impl/render/FirstPersonArm$Arm;->renderer:Lnet/minecraft/class_1007;", "FIELD:Lnet/anubis/client/module/impl/render/FirstPersonArm$Arm;->texture:Lnet/minecraft/class_2960;", "FIELD:Lnet/anubis/client/module/impl/render/FirstPersonArm$Arm;->rightSleeve:Z", "FIELD:Lnet/anubis/client/module/impl/render/FirstPersonArm$Arm;->leftSleeve:Z", "FIELD:Lnet/anubis/client/module/impl/render/FirstPersonArm$Arm;->slim:Z").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_1007<class_742> renderer() {
            return this.renderer;
        }

        public class_2960 texture() {
            return this.texture;
        }

        public boolean rightSleeve() {
            return this.rightSleeve;
        }

        public boolean leftSleeve() {
            return this.leftSleeve;
        }

        public boolean slim() {
            return this.slim;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void draw(class_4587 pose, boolean left, class_11659 collector, int light) {
            if (left) {
                pose.method_22905(-1.0f, 1.0f, 1.0f);
                this.renderer.method_4221(pose, collector, light, this.texture, this.leftSleeve);
            } else {
                this.renderer.method_4220(pose, collector, light, this.texture, this.rightSleeve);
            }
        }
    }

    static Arm arm() {
        class_8685 swapped;
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (player == null || player.method_5767()) {
            return null;
        }
        AccessorSkinChangeEntityRenderDispatcher accessorSkinChangeEntityRenderDispatcherMethod_1561 = mc.method_1561();
        class_1007<class_742> renderer = accessorSkinChangeEntityRenderDispatcherMethod_1561.method_74405(player);
        class_8685 skin = player.method_52814();
        SkinChangeModule change = (SkinChangeModule) ModuleRegistry.get(SkinChangeModule.class);
        if (change != null && change.isEnabled() && (swapped = change.skinFor(player.method_5628(), skin)) != skin) {
            class_1007<class_742> byModel = accessorSkinChangeEntityRenderDispatcherMethod_1561.anubis$skinChangePlayerRenderers().get(swapped.comp_1629());
            if (byModel != null) {
                renderer = byModel;
            }
            skin = swapped;
        }
        return new Arm(renderer, skin.comp_1626().comp_3627(), player.method_74091(class_1664.field_7570), player.method_74091(class_1664.field_7568), skin.comp_1629() == class_7920.field_41122);
    }

    static void hold(class_4587 pose, Arm arm, class_1799 stack, class_811 context, class_1309 holder, class_11659 collector, int light, float dx, float dy, float dz) {
        Vector3f grip = grip(stack, context, holder);
        if (grip == null) {
            return;
        }
        Vector3f elbow = new Vector3f(grip).fma(ELBOW, FOREARM);
        grip.add(dx, dy, dz);
        boolean left = context.method_67675();
        pose.method_22903();
        if (left) {
            pose.method_22905(-1.0f, 1.0f, 1.0f);
        }
        place(pose, grip, elbow, arm.slim(), posed -> {
            arm.draw(posed, left, collector, light);
        });
        pose.method_22909();
    }

    static Vector3f grip(class_1799 stack, class_811 context, class_1309 holder) {
        if (stack.method_7960() || stack.method_31574(class_1802.field_8102) || stack.method_31574(class_1802.field_8399) || stack.method_31574(class_1802.field_8255) || stack.method_31574(class_1802.field_8547) || stack.method_31574(class_1802.field_27070)) {
            return null;
        }
        class_310.method_1551().method_65386().method_65598(MODEL, stack, context, holder.method_73183(), holder, holder.method_5628() + context.ordinal());
        AccessorHandItemRenderState state = MODEL;
        if (state.anubis$handLayerCount() <= 0) {
            return null;
        }
        AccessorHandItemLayer layer = state.anubis$handLayers()[0];
        class_804 transform = layer.anubis$handTransform();
        class_4587.class_4665 display = new class_4587.class_4665();
        if (transform != null) {
            transform.method_23075(context.method_67675(), display);
        }
        Matrix4f m = display.method_23761();
        if (MODEL.method_65608()) {
            if (!(stack.method_7909() instanceof class_1747)) {
                return null;
            }
            Vector3f center = m.transformPosition(0.0f, 0.0f, 0.0f, new Vector3f());
            if (context.method_67675()) {
                center.x = -center.x;
            }
            return blockGrip(center, transform != null ? transform.comp_3749().y() : 1.0f);
        }
        float[] handle = handle(layer.anubis$handExtents() != null ? layer.anubis$handExtents().get() : null);
        Vector3f grip = m.transformPosition(handle[0] - 0.5f, handle[1] - 0.5f, 0.0f, new Vector3f());
        if (context.method_67675()) {
            grip.x = -grip.x;
        }
        return grip;
    }

    static Vector3f blockGrip(Vector3f center, float scale) {
        return new Vector3f(center).add(BLOCK_SHIFT).sub(0.0f, (0.5f * scale) + BLOCK_BELOW, 0.0f);
    }

    static float[] handle(Vector3fc[] extents) {
        float lowest = Float.MAX_VALUE;
        if (extents != null) {
            for (Vector3fc p : extents) {
                if (!squareCorner(p)) {
                    lowest = Math.min(lowest, p.x() + p.y());
                }
            }
        }
        if (lowest == Float.MAX_VALUE) {
            return new float[]{HANDLE_INSET, HANDLE_INSET};
        }
        float x = 0.0f;
        float y = 0.0f;
        int tied = 0;
        for (Vector3fc p2 : extents) {
            if (!squareCorner(p2) && p2.x() + p2.y() <= lowest + 1.0E-4f) {
                x += p2.x();
                y += p2.y();
                tied++;
            }
        }
        return new float[]{(x / tied) + HANDLE_INSET, (y / tied) + HANDLE_INSET};
    }

    private static boolean squareCorner(Vector3fc p) {
        return (p.x() < 1.0E-4f || p.x() > 0.9999f) && (p.y() < 1.0E-4f || p.y() > 0.9999f);
    }

    static void place(class_4587 pose, Vector3f hand, Vector3f elbow, boolean slim, Consumer<class_4587> draw) {
        Vector3f along = new Vector3f(hand).sub(elbow).normalize();
        Vector3f front = new Vector3f(ARM_UP).fma(-ARM_UP.dot(along), along).normalize();
        Vector3f side = new Vector3f(along).cross(front);
        pose.method_22903();
        pose.method_46416(hand.x, hand.y, hand.z);
        pose.method_22907(new Quaternionf().setFromNormalized(new Matrix3f(side, along, front)));
        drawPart(pose, slim, draw);
        pose.method_46416(0.0f, ARM_SHOULDER, 0.0f);
        pose.method_22905(-1.0f, -3.0f, 1.0f);
        pose.method_46416(0.0f, 0.625f, 0.0f);
        drawPart(pose, slim, draw);
        pose.method_22909();
    }

    private static void drawPart(class_4587 pose, boolean slim, Consumer<class_4587> draw) {
        pose.method_22903();
        pose.method_46416(slim ? 0.03125f : 0.0625f, -0.5f, 0.0f);
        pose.method_22907(class_7833.field_40718.rotation(-0.1f));
        pose.method_46416(0.3125f, -0.125f, 0.0f);
        draw.accept(pose);
        pose.method_22909();
    }

    static void shoulder(class_4587 pose, boolean right, boolean slim) {
        float pivotX = right ? -0.3125f : 0.3125f;
        float tilt = right ? 0.1f : -0.1f;
        float middle = ((right ? -1.0f : 1.0f) * (slim ? 0.5f : 1.0f)) / 16.0f;
        pose.method_46416(pivotX, 0.125f, 0.0f);
        pose.method_22907(class_7833.field_40718.rotation(tilt));
        pose.method_46416(middle, -0.125f, 0.0f);
        pose.method_22905(-1.0f, -3.0f, 1.0f);
        pose.method_46416(-middle, -(-0.125f), 0.0f);
        pose.method_22907(class_7833.field_40718.rotation(-tilt));
        pose.method_46416(-pivotX, -0.125f, 0.0f);
    }
}
