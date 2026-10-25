package net.anubis.client.module.impl.render.character.kit;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/character/kit/Detail.class */
@Environment(EnvType.CLIENT)
public enum Detail {
    LOW("Low", 7500, 0.42f),
    MEDIUM("Medium", 15000, 0.3f),
    HIGH("High", 30000, 0.21f);

    public final String label;
    public final int budget;
    public final float step;

    Detail(String label, int budget, float step) {
        this.label = label;
        this.budget = budget;
        this.step = step;
    }

    public static Detail byLabel(String label) {
        for (Detail d : values()) {
            if (d.label.equalsIgnoreCase(label) || d.name().equalsIgnoreCase(label)) {
                return d;
            }
        }
        return MEDIUM;
    }
}
