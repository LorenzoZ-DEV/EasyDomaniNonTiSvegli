package net.anubis.client.module.impl.render.character;

import net.anubis.client.module.impl.render.character.kit.Bone;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10055;
import net.minecraft.class_12122;
import net.minecraft.class_1306;
import net.minecraft.class_3532;
import net.minecraft.class_572;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/CharacterJoints.class */
@Environment(EnvType.CLIENT)
final class CharacterJoints {
    static final float CROUCH_HIP = 0.5f;
    static final float SADDLE_KNEE = 1.35f;
    static final float SEAT_KNEE = 0.3f;
    private static final float CROUCH_KNEE = 0.62f;
    private static final float CROUCH_ELBOW = 0.2f;
    private static final float MAX_KNEE = 1.7f;
    private static final float MAX_ELBOW = 1.6f;

    private CharacterJoints() {
    }

    static float angle(class_10055 state, Bone bone, float swing) {
        float fade = (state.field_53411 || state.field_53459) ? 0.0f : 1.0f - (state.field_53403 * 0.8f);
        if (fade <= 0.0f) {
            return 0.0f;
        }
        if (bone.leg()) {
            return knee(state, bone) * fade;
        }
        if (bone.arm()) {
            return (-elbow(state, bone, swing)) * fade;
        }
        return 0.0f;
    }

    private static float knee(class_10055 state, Bone bone) {
        if (state.field_53413) {
            return CharacterModule.seat(state);
        }
        float speed = Math.min(1.0f, state.field_53451 / state.field_53405);
        float phase = (state.field_53450 * 0.6662f) + (bone == Bone.LEFT_LEG ? 3.1415927f : 0.0f);
        float flex = speed * (0.1f + (0.85f * Math.max(0.0f, class_3532.method_15374(phase))));
        if (state.field_53410) {
            flex += CROUCH_KNEE;
        }
        return Math.min(MAX_KNEE, flex);
    }

    private static float elbow(class_10055 state, Bone bone, float swing) {
        float fMax;
        boolean right = bone == Bone.RIGHT_ARM;
        class_572.class_573 pose = right ? state.field_55304 : state.field_55306;
        class_572.class_573 other = right ? state.field_55306 : state.field_55304;
        if (other.method_30156()) {
            return 0.0f;
        }
        float speed = Math.min(1.0f, state.field_53451 / state.field_53405);
        switch (AnonymousClass1.$SwitchMap$net$minecraft$client$model$HumanoidModel$ArmPose[pose.ordinal()]) {
            case 1:
                fMax = 0.16f + (0.45f * Math.max(0.0f, -swing)) + (SEAT_KNEE * speed);
                break;
            case 2:
                fMax = SEAT_KNEE + (CROUCH_ELBOW * speed);
                break;
            case 3:
                fMax = 0.55f;
                break;
            case 4:
                fMax = 0.4f;
                break;
            default:
                fMax = 0.0f;
                break;
        }
        float flex = fMax;
        if (flex <= 0.0f) {
            return 0.0f;
        }
        if (state.field_53410) {
            flex += CROUCH_ELBOW;
        }
        class_1306 arm = right ? class_1306.field_6183 : class_1306.field_6182;
        if (state.field_63604 > 0.0f && state.field_53408 == arm && state.field_63603 == class_12122.field_63399) {
            flex += 0.6f * class_3532.method_15374(state.field_63604 * 3.1415927f);
        }
        return Math.min(MAX_ELBOW, flex);
    }

    /* JADX INFO: renamed from: net.anubis.client.module.impl.render.character.CharacterJoints$1, reason: invalid class name */
    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/CharacterJoints$1.class */
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
        }
    }
}
