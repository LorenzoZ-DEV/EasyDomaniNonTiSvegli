package net.anubis.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/StableWidth.class */
@Environment(EnvType.CLIENT)
final class StableWidth {
    static final float SHRINK_DELAY = 1.5f;
    private static final float SHRINK_RATE = 12.0f;
    private static final float SNAP = 0.25f;
    private float current = -1.0f;
    private float narrowerFor;

    StableWidth() {
    }

    float update(float target, float dt) {
        if (this.current < 0.0f || target >= this.current) {
            this.current = target;
            this.narrowerFor = 0.0f;
            return this.current;
        }
        this.narrowerFor += dt;
        if (this.narrowerFor < SHRINK_DELAY) {
            return this.current;
        }
        this.current += (target - this.current) * (1.0f - ((float) Math.exp((-12.0f) * dt)));
        if (Math.abs(this.current - target) < SNAP) {
            this.current = target;
        }
        return this.current;
    }
}
