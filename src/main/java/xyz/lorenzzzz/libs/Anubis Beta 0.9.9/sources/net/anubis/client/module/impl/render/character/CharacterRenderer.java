package net.anubis.client.module.impl.render.character;

import imgui.flag.ImGuiTableFlags;
import net.anubis.client.module.impl.render.character.kit.Bone;
import net.anubis.client.module.impl.render.character.kit.CharacterModel;
import net.anubis.client.module.impl.render.character.kit.Lights;
import net.anubis.client.module.impl.render.character.kit.PartMesh;
import net.anubis.client.module.impl.render.character.kit.Surface;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10055;
import net.minecraft.class_11659;
import net.minecraft.class_1306;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4608;
import net.minecraft.class_5603;
import net.minecraft.class_572;
import net.minecraft.class_591;
import net.minecraft.class_630;
import net.minecraft.class_922;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/CharacterRenderer.class */
@Environment(EnvType.CLIENT)
final class CharacterRenderer {
    private static final int GHOST_ALPHA = 38;
    private static final Bone[] BONES = Bone.values();
    private static final Quaternionf ROTATION = new Quaternionf();
    private static final float[] LEVELS = new float[153];
    private static final float[] PITCH = new float[BONES.length];
    private static final float[] JOINT = new float[BONES.length];
    private static final float[] JOINT_LEVELS = new float[153];
    private static float[] scratch = new float[ImGuiTableFlags.SizingStretchProp];

    private CharacterRenderer() {
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    static void submit(WornCharacter worn, class_591 pm, class_4587 stack, class_11659 collector, int light, class_10055 state) throws MatchException {
        CharacterModel model = worn.model;
        PartMesh[] parts = model.parts;
        boolean ghost = state.field_53333;
        int overlay = class_922.method_23622(state, 0.0f);
        float[] pitch = pitches(state, model, pm);
        float[] joint = joints(state, model);
        float lookPitch = pm.field_3398.field_3654;
        float lookYaw = -pm.field_3398.field_3675;
        float[] angles = CharacterMotion.update(state, model, pm, pitch);
        stack.method_22903();
        pm.method_63512().method_22703(stack);
        lift(stack, state, model);
        for (int i = 0; i < parts.length; i++) {
            PartMesh part = parts[i];
            if (part.base != null || part.outline != null || part.decals != null) {
                boolean bends = part.bends() && angles != null;
                float bendPitch = bends ? angles[part.motionSlot * 3] : 0.0f;
                float yaw = bends ? angles[(part.motionSlot * 3) + 1] : 0.0f;
                float roll = bends ? angles[(part.motionSlot * 3) + 2] : 0.0f;
                float bent = 0.0f;
                float turn = 0.0f;
                if (part.neck) {
                    bent = look(part.lookPitch * lookPitch, part.lookLimit);
                    turn = look(part.lookYaw * lookYaw, part.lookLimit);
                } else if (part.jointed) {
                    bent = joint[part.bone.ordinal()];
                }
                stack.method_22903();
                chain(stack, pm, model, i, angles, !bends, null, pitch);
                submitPart(worn, part, stack, collector, light, overlay, ghost, bends, bendPitch, yaw, roll, bent, turn);
                stack.method_22909();
            }
        }
        stack.method_22909();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    static boolean firstPersonHand(class_591 pm, class_4587 stack, class_11659 collector, int light, class_630 arm) throws MatchException {
        WornCharacter worn = CharacterModule.firstPerson();
        if (worn == null) {
            return false;
        }
        Bone bone = arm == pm.field_3401 ? Bone.RIGHT_ARM : Bone.LEFT_ARM;
        CharacterModel model = worn.model;
        PartMesh main = model.part(bone);
        if (main == null || main.base == null) {
            return false;
        }
        arm.method_41923();
        arm.field_3674 = bone == Bone.RIGHT_ARM ? 0.1f : -0.1f;
        PartMesh[] parts = model.parts;
        for (int i = 0; i < parts.length; i++) {
            PartMesh part = parts[i];
            if (part.bone == bone && (part.base != null || part.outline != null || part.decals != null)) {
                stack.method_22903();
                chain(stack, pm, model, i, null, true, arm, null);
                submitPart(worn, part, stack, collector, light, class_4608.field_21444, false, false, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
                stack.method_22909();
            }
        }
        return true;
    }

    private static void submitPart(WornCharacter worn, PartMesh part, class_4587 stack, class_11659 collector, int light, int overlay, boolean ghost, boolean bends, float pitch, float yaw, float roll, float bent, float turn) {
        if (ghost) {
            if (part.base != null) {
                collector.method_73529(1).method_73483(stack, worn.ghost, (pose, out) -> {
                    emit(pose, out, part.base, light, overlay, 38, bends, pitch, yaw, roll, part, bent, turn);
                });
            }
        } else {
            if (part.base != null || part.outline != null) {
                collector.method_73483(stack, worn.solid, (pose2, out2) -> {
                    if (part.base != null) {
                        emit(pose2, out2, part.base, light, overlay, 255, bends, pitch, yaw, roll, part, bent, turn);
                    }
                    if (part.outline != null) {
                        emit(pose2, out2, part.outline, light, overlay, 255, bends, pitch, yaw, roll, part, bent, turn);
                    }
                });
            }
            if (part.decals != null) {
                collector.method_73529(1).method_73483(stack, worn.decal, (pose3, out3) -> {
                    emit(pose3, out3, part.decals, light, overlay, 255, bends, pitch, yaw, roll, part, bent, turn);
                });
            }
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    static boolean translateToHand(class_591 pm, class_10055 state, class_1306 arm, class_4587 stack) throws MatchException {
        WornCharacter worn = CharacterModule.worn(state);
        if (worn == null) {
            return false;
        }
        CharacterModel model = worn.model;
        Bone bone = arm == class_1306.field_6183 ? Bone.RIGHT_ARM : Bone.LEFT_ARM;
        PartMesh part = model.part(bone);
        if (part == null) {
            return false;
        }
        float s = model.scale;
        class_630 vp = vanilla(pm, bone);
        pm.method_63512().method_22703(stack);
        lift(stack, state, model);
        pivot(stack, vp, part, s, pitch(state, part, vp.field_3654));
        float bent = part.jointed ? CharacterJoints.angle(state, bone, walk(state, bone) * weight(state, bone)) : 0.0f;
        if (bent != 0.0f) {
            float jy = (part.jointY * s) / 16.0f;
            float jz = (part.jointZ * s) / 16.0f;
            stack.method_46416(0.0f, -jy, -jz);
            stack.method_22907(ROTATION.rotationX(bent));
            stack.method_46416(0.0f, jy, jz);
        }
        stack.method_46416(((part.gripX - part.pivotX) * s) / 16.0f, ((-(part.gripY - part.pivotY)) * s) / 16.0f, ((-(part.gripZ - part.pivotZ)) * s) / 16.0f);
        stack.method_22905(s, s, s);
        stack.method_46416((arm == class_1306.field_6183 ? 1.0f : -1.0f) / 16.0f, -0.625f, 0.125f);
        return true;
    }

    private static float[] pitches(class_10055 state, CharacterModel model, class_591 pm) {
        float[] out = PITCH;
        for (Bone bone : BONES) {
            float xRot = vanilla(pm, bone).field_3654;
            PartMesh part = (bone.arm() || bone.leg()) ? model.part(bone) : null;
            out[bone.ordinal()] = part == null ? xRot : pitch(state, part, xRot);
        }
        return out;
    }

    private static float pitch(class_10055 state, PartMesh part, float xRot) {
        float out = part.swingPitch(xRot, walk(state, part.bone), weight(state, part.bone));
        if (part.jointed && part.bone.leg() && state.field_53410 && !state.field_53413) {
            out -= 0.5f * (1.0f - state.field_53403);
        }
        return out;
    }

    private static float[] joints(class_10055 state, CharacterModel model) {
        float[] out = JOINT;
        for (Bone bone : BONES) {
            PartMesh part = (bone.arm() || bone.leg()) ? model.part(bone) : null;
            out[bone.ordinal()] = (part == null || !part.jointed) ? 0.0f : CharacterJoints.angle(state, bone, walk(state, bone) * weight(state, bone));
        }
        return out;
    }

    private static float look(float angle, float limitDegrees) {
        float max = limitDegrees * 0.017453292f;
        return class_3532.method_15363(angle, -max, max);
    }

    private static float walk(class_10055 state, Bone bone) {
        float phase = state.field_53450 * 0.6662f;
        float speed = state.field_53451 / state.field_53405;
        switch (bone) {
            case RIGHT_LEG:
                return class_3532.method_15362(phase) * 1.4f * speed;
            case LEFT_LEG:
                return class_3532.method_15362(phase + 3.1415927f) * 1.4f * speed;
            case RIGHT_ARM:
                return class_3532.method_15362(phase + 3.1415927f) * speed;
            case LEFT_ARM:
                return class_3532.method_15362(phase) * speed;
            default:
                return 0.0f;
        }
    }

    private static float weight(class_10055 state, Bone bone) {
        float f;
        if (bone.leg()) {
            if (state.field_53413) {
                return 0.0f;
            }
            return 1.0f - state.field_53403;
        }
        if (!bone.arm()) {
            return 0.0f;
        }
        class_572.class_573 pose = bone == Bone.RIGHT_ARM ? state.field_55304 : state.field_55306;
        class_572.class_573 other = bone == Bone.RIGHT_ARM ? state.field_55306 : state.field_55304;
        if (other.method_30156()) {
            return 0.0f;
        }
        switch (AnonymousClass1.$SwitchMap$net$minecraft$client$model$HumanoidModel$ArmPose[pose.ordinal()]) {
            case 1:
                f = 1.0f;
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                f = 0.5f;
                break;
            default:
                f = 0.0f;
                break;
        }
        float share = f;
        return state.field_53414 ? share : share * (1.0f - state.field_53403);
    }

    /* JADX INFO: renamed from: net.anubis.client.module.impl.render.character.CharacterRenderer$1, reason: invalid class name */
    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/CharacterRenderer$1.class */
    @Environment(EnvType.CLIENT)
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$net$minecraft$client$model$HumanoidModel$ArmPose = new int[class_572.class_573.values().length];

        static {
            try {
                $SwitchMap$net$minecraft$client$model$HumanoidModel$ArmPose[class_572.class_573.field_3409.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$net$minecraft$client$model$HumanoidModel$ArmPose[class_572.class_573.field_3410.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$net$minecraft$client$model$HumanoidModel$ArmPose[class_572.class_573.field_3406.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$net$minecraft$client$model$HumanoidModel$ArmPose[class_572.class_573.field_42877.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$net$minecraft$client$model$HumanoidModel$ArmPose[class_572.class_573.field_63542.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            $SwitchMap$net$anubis$client$module$impl$render$character$kit$Bone = new int[Bone.values().length];
            try {
                $SwitchMap$net$anubis$client$module$impl$render$character$kit$Bone[Bone.RIGHT_LEG.ordinal()] = 1;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$net$anubis$client$module$impl$render$character$kit$Bone[Bone.LEFT_LEG.ordinal()] = 2;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$net$anubis$client$module$impl$render$character$kit$Bone[Bone.RIGHT_ARM.ordinal()] = 3;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$net$anubis$client$module$impl$render$character$kit$Bone[Bone.LEFT_ARM.ordinal()] = 4;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$net$anubis$client$module$impl$render$character$kit$Bone[Bone.HEAD.ordinal()] = 5;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$net$anubis$client$module$impl$render$character$kit$Bone[Bone.BODY.ordinal()] = 6;
            } catch (NoSuchFieldError e11) {
            }
        }
    }

    private static void lift(class_4587 stack, class_10055 state, CharacterModel model) {
        if (state.field_53413) {
            float rise = 12.0f - (model.scale * model.hipY);
            if (rise != 0.0f) {
                stack.method_46416(0.0f, (-rise) / 16.0f, 0.0f);
            }
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private static void chain(class_4587 stack, class_591 pm, CharacterModel model, int index, float[] angles, boolean rotate, class_630 hand, float[] pitch) throws MatchException {
        PartMesh part = model.parts[index];
        float s = model.scale;
        if (!part.extra) {
            if (hand == null) {
                class_630 vp = vanilla(pm, part.bone);
                pivot(stack, vp, part, s, pitch == null ? vp.field_3654 : pitch[part.bone.ordinal()]);
            } else {
                stack.method_46416(hand.field_3657 / 16.0f, hand.field_3656 / 16.0f, hand.field_3655 / 16.0f);
                stack.method_22907(ROTATION.rotationZYX(hand.field_3674, hand.field_3675, hand.field_3654));
                float side = part.bone == Bone.RIGHT_ARM ? -1.0f : 1.0f;
                stack.method_46416((side - ((part.gripX - part.pivotX) * s)) / 16.0f, (10.0f + ((part.gripY - part.pivotY) * s)) / 16.0f, ((-2.0f) + ((part.gripZ - part.pivotZ) * s)) / 16.0f);
            }
            float k = s / 16.0f;
            stack.method_22905(k, -k, -k);
            return;
        }
        chain(stack, pm, model, part.parent, angles, true, hand, pitch);
        PartMesh parent = model.parts[part.parent];
        stack.method_46416(part.pivotX - parent.pivotX, part.pivotY - parent.pivotY, part.pivotZ - parent.pivotZ);
        if (!rotate || angles == null || part.motionSlot < 0) {
            return;
        }
        int o = part.motionSlot * 3;
        if (angles[o] != 0.0f || angles[o + 1] != 0.0f || angles[o + 2] != 0.0f) {
            stack.method_22907(ROTATION.rotationZYX(angles[o + 2], angles[o + 1], angles[o]));
        }
    }

    private static void pivot(class_4587 stack, class_630 vp, PartMesh part, float s, float xRot) {
        class_5603 rest = vp.method_41921();
        stack.method_46416(((s * part.pivotX) + ((vp.field_3657 - rest.comp_2997()) * s)) / 16.0f, ((24.0f - (s * part.pivotY)) + ((vp.field_3656 - rest.comp_2998()) * s)) / 16.0f, (((-s) * part.pivotZ) + ((vp.field_3655 - rest.comp_2999()) * s)) / 16.0f);
        if (xRot != 0.0f || vp.field_3675 != 0.0f || vp.field_3674 != 0.0f) {
            stack.method_22907(ROTATION.rotationZYX(vp.field_3674, vp.field_3675, xRot));
        }
        if (vp.field_37938 != 1.0f || vp.field_37939 != 1.0f || vp.field_37940 != 1.0f) {
            stack.method_22905(vp.field_37938, vp.field_37939, vp.field_37940);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private static class_630 vanilla(class_591 pm, Bone bone) throws MatchException {
        switch (bone) {
            case RIGHT_LEG:
                return pm.field_3392;
            case LEFT_LEG:
                return pm.field_3397;
            case RIGHT_ARM:
                return pm.field_3401;
            case LEFT_ARM:
                return pm.field_27433;
            case HEAD:
                return pm.field_3398;
            case BODY:
                return pm.field_3391;
            default:
                throw new MatchException((String) null, (Throwable) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void emit(class_4587.class_4665 pose, class_4588 out, Surface s, int light, int overlay, int alpha, boolean bends, float pitch, float yaw, float roll, PartMesh part, float bent, float turn) {
        int k;
        int n = s.vertexCount();
        if (scratch.length < n * 6) {
            scratch = new float[Math.max(n * 6, scratch.length * 2)];
        }
        float[] buf = scratch;
        Matrix4f m = pose.method_23761();
        Matrix3f nm = pose.method_23762();
        float m00 = m.m00();
        float m01 = m.m10();
        float m02 = m.m20();
        float m03 = m.m30();
        float m10 = m.m01();
        float m11 = m.m11();
        float m12 = m.m21();
        float m13 = m.m31();
        float m20 = m.m02();
        float m21 = m.m12();
        float m22 = m.m22();
        float m23 = m.m32();
        float n00 = nm.m00();
        float n01 = nm.m10();
        float n02 = nm.m20();
        float n10 = nm.m01();
        float n11 = nm.m11();
        float n12 = nm.m21();
        float n20 = nm.m02();
        float n21 = nm.m12();
        float n22 = nm.m22();
        float[] pos = s.pos;
        float[] nrm = s.nrm;
        float[] weight = bends ? s.weight : null;
        float[] flat = s.flat;
        Lights lights = flat != null ? CharacterLighting.current() : null;
        if (weight != null) {
            levels(LEVELS, pitch, yaw, roll);
        }
        float[] joint = (bent == 0.0f && turn == 0.0f) ? null : s.joint;
        float jx = part.jointX;
        float jy = part.jointY;
        float jz = part.jointZ;
        if (joint != null) {
            levels(JOINT_LEVELS, bent, turn, 0.0f);
        }
        for (int v = 0; v < n; v++) {
            float x = pos[v * 3];
            float y = pos[(v * 3) + 1];
            float z = pos[(v * 3) + 2];
            float nx = nrm[v * 3];
            float ny = nrm[(v * 3) + 1];
            float nz = nrm[(v * 3) + 2];
            if (joint != null && (k = Math.round(joint[v] * 16.0f)) > 0) {
                float[] r = JOINT_LEVELS;
                int o = k * 9;
                float dx = x - jx;
                float dy = y - jy;
                float dz = z - jz;
                x = jx + (r[o] * dx) + (r[o + 1] * dy) + (r[o + 2] * dz);
                y = jy + (r[o + 3] * dx) + (r[o + 4] * dy) + (r[o + 5] * dz);
                z = jz + (r[o + 6] * dx) + (r[o + 7] * dy) + (r[o + 8] * dz);
                float rx = (r[o] * nx) + (r[o + 1] * ny) + (r[o + 2] * nz);
                float ry = (r[o + 3] * nx) + (r[o + 4] * ny) + (r[o + 5] * nz);
                nz = (r[o + 6] * nx) + (r[o + 7] * ny) + (r[o + 8] * nz);
                nx = rx;
                ny = ry;
            }
            if (weight != null) {
                int k2 = Math.round(weight[v] * 16.0f) * 9;
                float[] r2 = LEVELS;
                float bx = (r2[k2] * x) + (r2[k2 + 1] * y) + (r2[k2 + 2] * z);
                float by = (r2[k2 + 3] * x) + (r2[k2 + 4] * y) + (r2[k2 + 5] * z);
                float bz = (r2[k2 + 6] * x) + (r2[k2 + 7] * y) + (r2[k2 + 8] * z);
                float cx = (r2[k2] * nx) + (r2[k2 + 1] * ny) + (r2[k2 + 2] * nz);
                float cy = (r2[k2 + 3] * nx) + (r2[k2 + 4] * ny) + (r2[k2 + 5] * nz);
                float cz = (r2[k2 + 6] * nx) + (r2[k2 + 7] * ny) + (r2[k2 + 8] * nz);
                x = bx;
                y = by;
                z = bz;
                nx = cx;
                ny = cy;
                nz = cz;
            }
            int o2 = v * 6;
            buf[o2] = (m00 * x) + (m01 * y) + (m02 * z) + m03;
            buf[o2 + 1] = (m10 * x) + (m11 * y) + (m12 * z) + m13;
            buf[o2 + 2] = (m20 * x) + (m21 * y) + (m22 * z) + m23;
            float tx = (n00 * nx) + (n01 * ny) + (n02 * nz);
            float ty = (n10 * nx) + (n11 * ny) + (n12 * nz);
            float tz = (n20 * nx) + (n21 * ny) + (n22 * nz);
            float l = (tx * tx) + (ty * ty) + (tz * tz);
            if (l > 1.0E-12f && Math.abs(l - 1.0f) > 0.001f) {
                float l2 = 1.0f / ((float) Math.sqrt(l));
                tx *= l2;
                ty *= l2;
                tz *= l2;
            }
            buf[o2 + 3] = tx;
            buf[o2 + 4] = ty;
            buf[o2 + 5] = tz;
            if (lights != null && flat[v] > 0.0f) {
                lights.flatten(buf, o2 + 3, flat[v]);
            }
        }
        int[] quads = s.quads;
        int[] color = s.color;
        float[] uv = s.uv;
        byte[] glow = s.glow;
        for (int v2 : quads) {
            int o3 = v2 * 6;
            int c = alpha == 255 ? color[v2] : (color[v2] & 16777215) | (alpha << 24);
            out.method_23919(buf[o3], buf[o3 + 1], buf[o3 + 2], c, uv[v2 * 2], uv[(v2 * 2) + 1], overlay, (glow == null || glow[v2] == 0) ? light : 15728880, buf[o3 + 3], buf[o3 + 4], buf[o3 + 5]);
        }
    }

    private static void levels(float[] r, float pitch, float yaw, float roll) {
        for (int k = 0; k <= 16; k++) {
            float t = k / 16.0f;
            float cx = (float) Math.cos(pitch * t);
            float sx = (float) Math.sin(pitch * t);
            float cy = (float) Math.cos(yaw * t);
            float sy = (float) Math.sin(yaw * t);
            float cz = (float) Math.cos(roll * t);
            float sz = (float) Math.sin(roll * t);
            int o = k * 9;
            r[o] = cz * cy;
            r[o + 1] = ((cz * sy) * sx) - (sz * cx);
            r[o + 2] = (cz * sy * cx) + (sz * sx);
            r[o + 3] = sz * cy;
            r[o + 4] = (sz * sy * sx) + (cz * cx);
            r[o + 5] = ((sz * sy) * cx) - (cz * sx);
            r[o + 6] = -sy;
            r[o + 7] = cy * sx;
            r[o + 8] = cy * cx;
        }
    }
}
