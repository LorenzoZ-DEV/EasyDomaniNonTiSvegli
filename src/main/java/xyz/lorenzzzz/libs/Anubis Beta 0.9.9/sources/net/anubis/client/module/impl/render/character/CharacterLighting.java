package net.anubis.client.module.impl.render.character;

import net.anubis.client.module.impl.render.character.kit.Lights;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2874;
import net.minecraft.class_308;
import net.minecraft.class_310;
import net.minecraft.class_638;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/CharacterLighting.class */
@Environment(EnvType.CLIENT)
public final class CharacterLighting {
    private static final Vector3f LIGHT_0 = new Vector3f(0.2f, 1.0f, -0.7f).normalize();
    private static final Vector3f LIGHT_1 = new Vector3f(-0.2f, 1.0f, 0.7f).normalize();
    private static final Lights NETHER = new Lights(0.2f, 1.0f, -0.7f, -0.2f, -1.0f, 0.7f);
    private static final Lights INVENTORY = new Lights(0.2f, -1.0f, 1.0f, -0.2f, -1.0f, 0.0f);
    private static final Lights ITEMS_FLAT = transformed(new Matrix4f().rotationY(-0.3926991f).rotateX(2.3561945f));
    private static final Lights ITEMS_3D = transformed(new Matrix4f().scaling(1.0f, -1.0f, 1.0f).rotateYXZ(1.0821041f, 3.2375858f, 0.0f).rotateYXZ(-0.3926991f, 2.3561945f, 0.0f));
    private static class_308.class_11274 entry = class_308.class_11274.field_60025;

    private CharacterLighting() {
    }

    public static void setup(class_308.class_11274 current) {
        entry = current;
    }

    /* JADX INFO: renamed from: net.anubis.client.module.impl.render.character.CharacterLighting$1, reason: invalid class name */
    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/CharacterLighting$1.class */
    @Environment(EnvType.CLIENT)
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$mojang$blaze3d$platform$Lighting$Entry = new int[class_308.class_11274.values().length];

        static {
            try {
                $SwitchMap$com$mojang$blaze3d$platform$Lighting$Entry[class_308.class_11274.field_60025.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$mojang$blaze3d$platform$Lighting$Entry[class_308.class_11274.field_60026.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$mojang$blaze3d$platform$Lighting$Entry[class_308.class_11274.field_60027.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$mojang$blaze3d$platform$Lighting$Entry[class_308.class_11274.field_60028.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$mojang$blaze3d$platform$Lighting$Entry[class_308.class_11274.field_60029.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    static Lights current() throws MatchException {
        switch (AnonymousClass1.$SwitchMap$com$mojang$blaze3d$platform$Lighting$Entry[entry.ordinal()]) {
            case 1:
                return nether() ? NETHER : Lights.WORLD;
            case 2:
                return ITEMS_FLAT;
            case 3:
                return ITEMS_3D;
            case 4:
            case 5:
                return INVENTORY;
            default:
                throw new MatchException((String) null, (Throwable) null);
        }
    }

    private static boolean nether() {
        class_638 level = class_310.method_1551().field_1687;
        return level != null && level.method_8597().comp_5221() == class_2874.class_12325.field_64381;
    }

    private static Lights transformed(Matrix4f matrix) {
        Vector3f a = matrix.transformDirection(LIGHT_0, new Vector3f());
        Vector3f b = matrix.transformDirection(LIGHT_1, new Vector3f());
        return new Lights(a.x, a.y, a.z, b.x, b.y, b.z);
    }
}
