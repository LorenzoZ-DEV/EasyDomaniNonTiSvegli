package net.anubis.client.module.impl.render.character.kit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Rig.class */
@Environment(EnvType.CLIENT)
public final class Rig {
    float flat;
    float scale = 1.0f;
    int outlineColor = -14017512;
    float outline = 0.2f;
    float occlusion = 0.55f;
    boolean firstPersonArms = true;
    final Part[] bones = new Part[Bone.values().length];
    final List<Part> extras = new ArrayList();

    public Rig scale(float scale) {
        if (scale <= 0.05f || scale >= 8.0f) {
            throw new IllegalArgumentException("scale must be between 0.05 and 8");
        }
        this.scale = scale;
        return this;
    }

    public Rig outline(int argb, float thickness) {
        this.outlineColor = argb;
        this.outline = Math.max(0.0f, thickness);
        return this;
    }

    public Rig occlusion(float strength) {
        this.occlusion = Math.max(0.0f, Math.min(1.0f, strength));
        return this;
    }

    public Rig flatLight(float amount) {
        this.flat = Math.max(0.0f, Math.min(1.0f, amount));
        return this;
    }

    public Rig firstPersonArms(boolean show) {
        this.firstPersonArms = show;
        return this;
    }

    public Part part(Bone bone) {
        Part p = this.bones[bone.ordinal()];
        if (p == null) {
            p = new Part(bone.name(), bone, null, false);
            this.bones[bone.ordinal()] = p;
        }
        return p;
    }

    public Part extra(String name, Bone bone) {
        return extra(name, part(bone));
    }

    public Part extra(String name, Part parent) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("extra needs a name");
        }
        Iterator<Part> it = this.extras.iterator();
        while (it.hasNext()) {
            if (it.next().name.equals(name)) {
                throw new IllegalArgumentException("duplicate extra name: " + name);
            }
        }
        for (Bone b : Bone.values()) {
            if (b.name().equals(name)) {
                throw new IllegalArgumentException("extra name clashes with a bone: " + name);
            }
        }
        Part p = new Part(name, parent.bone, parent, true);
        this.extras.add(p);
        return p;
    }

    public float scale() {
        return this.scale;
    }
}
