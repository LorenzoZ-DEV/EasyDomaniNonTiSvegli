package net.anubis.client.module.impl.render;

import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.ActionSetting;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_4587;
import net.minecraft.class_7833;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/HandsModule.class */
@Environment(EnvType.CLIENT)
public final class HandsModule extends ClientModule {
    public static volatile HandsModule live;
    private static final String MAIN = "Main Hand";
    private static final String OFF = "Offhand";
    private static final float PIVOT_X = 0.56f;
    private static final float PIVOT_Y = -0.52f;
    private static final float PIVOT_Z = -0.72f;
    private static final float[] REST = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
    private static final int X = 0;
    private static final int Y = 1;
    private static final int Z = 2;
    private static final int PITCH = 3;
    private static final int YAW = 4;
    private static final int ROLL = 5;
    private static final int SCALE = 6;
    private static final float SETTLED = 0.001f;
    private static final float MAX_STEP = 0.1f;
    private static final float SLOWEST_RATE = 3.0f;
    private static final float FASTEST_RATE = 30.0f;
    private final BooleanSetting link;
    private final ModeSetting editing;
    private final NumberSetting[] main;
    private final NumberSetting[] off;
    private final NumberSetting glide;
    private final HandState mainState;
    private final HandState offState;

    public HandsModule() {
        super("hand-pose", "Hand Pose", "Moves, rotates and resizes your first-person hands.", ModuleCategory.VISUALS);
        this.link = (BooleanSetting) addSetting("link-hands", new BooleanSetting("Link Hands", false));
        this.editing = (ModeSetting) addSetting("editing", new ModeSetting("Editing", MAIN, MAIN, OFF));
        this.main = axes("main", true);
        this.off = axes("off", false);
        this.glide = (NumberSetting) addSetting("glide", new NumberSetting("Glide", 0.4f, 0.0f, 1.0f).step(0.05f));
        this.mainState = new HandState();
        this.offState = new HandState();
        this.editing.visibleWhen(() -> {
            return !this.link.get();
        });
        addSetting("reset-hand", new ActionSetting("Reset Hand", this::resetEdited));
    }

    private NumberSetting[] axes(String prefix, boolean mainHand) {
        NumberSetting[] axes = {(NumberSetting) addSetting(prefix + "-x", new NumberSetting("Offset X", REST[0], -1.0f, 1.0f).step(0.01f)), (NumberSetting) addSetting(prefix + "-y", new NumberSetting("Offset Y", REST[1], -1.0f, 1.0f).step(0.01f)), (NumberSetting) addSetting(prefix + "-z", new NumberSetting("Offset Z", REST[2], -1.0f, 1.0f).step(0.01f)), (NumberSetting) addSetting(prefix + "-pitch", new NumberSetting("Pitch", REST[3], -180.0f, 180.0f).step(1.0f)), (NumberSetting) addSetting(prefix + "-yaw", new NumberSetting("Yaw", REST[4], -180.0f, 180.0f).step(1.0f)), (NumberSetting) addSetting(prefix + "-roll", new NumberSetting("Roll", REST[5], -180.0f, 180.0f).step(1.0f)), (NumberSetting) addSetting(prefix + "-scale", new NumberSetting("Scale", REST[6], 0.3f, 2.0f).step(0.05f))};
        for (NumberSetting axis : axes) {
            axis.visibleWhen(mainHand ? () -> {
                return this.link.get() || this.editing.is(MAIN);
            } : () -> {
                return !this.link.get() && this.editing.is(OFF);
            });
        }
        return axes;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        this.mainState.stamp = 0L;
        this.offState.stamp = 0L;
        live = this;
    }

    private void resetEdited() {
        NumberSetting[] axes = (this.link.get() || this.editing.is(MAIN)) ? this.main : this.off;
        for (int i = 0; i < axes.length; i++) {
            axes[i].set(REST[i]);
        }
    }

    public static void shape(class_4587 pose, boolean mainHand, int side) {
        HandsModule module = live;
        if (module != null) {
            module.shapeHand(pose, mainHand, side);
        }
    }

    private void shapeHand(class_4587 pose, boolean mainHand, int side) {
        boolean on = isEnabled();
        HandState state = mainHand ? this.mainState : this.offState;
        state.step((mainHand || this.link.get()) ? this.main : this.off, on, follow());
        if (!on && this.mainState.rested && this.offState.rested) {
            live = null;
            return;
        }
        if (state.rested) {
            return;
        }
        float[] v = state.value;
        pose.method_46416(side * (PIVOT_X + v[0]), PIVOT_Y + v[1], PIVOT_Z + v[2]);
        pose.method_22907(class_7833.field_40716.rotationDegrees(side * v[4]));
        pose.method_22907(class_7833.field_40714.rotationDegrees(v[3]));
        pose.method_22907(class_7833.field_40718.rotationDegrees(side * v[5]));
        pose.method_22905(v[6], v[6], v[6]);
        pose.method_46416((-side) * PIVOT_X, 0.52f, 0.72f);
    }

    private float follow() {
        float g = this.glide.get();
        if (g <= 0.0f) {
            return -1.0f;
        }
        return FASTEST_RATE + ((-27.0f) * g);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/HandsModule$HandState.class */
    @Environment(EnvType.CLIENT)
    private static final class HandState {
        long stamp;
        final float[] value = (float[]) HandsModule.REST.clone();
        boolean rested = true;

        private HandState() {
        }

        void step(NumberSetting[] target, boolean on, float rate) {
            long now = System.nanoTime();
            float dt = this.stamp == 0 ? 0.0f : Math.min(HandsModule.MAX_STEP, (now - this.stamp) / 1.0E9f);
            this.stamp = now;
            float k = rate < 0.0f ? 1.0f : 1.0f - ((float) Math.exp((-rate) * dt));
            boolean settled = true;
            for (int i = 0; i < this.value.length; i++) {
                float goal = on ? target[i].get() : HandsModule.REST[i];
                float next = this.value[i] + ((goal - this.value[i]) * k);
                if (Math.abs(goal - next) < HandsModule.SETTLED) {
                    next = goal;
                }
                this.value[i] = next;
                if (next != HandsModule.REST[i]) {
                    settled = false;
                }
            }
            this.rested = settled;
        }
    }
}
