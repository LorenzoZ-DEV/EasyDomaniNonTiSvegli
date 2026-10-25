package net.anubis.client.module.impl.render;

import java.util.Objects;
import net.anubis.client.addon.AddonManager;
import net.anubis.client.config.Surface;
import net.anubis.client.event.EventPriority;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.KeyInputEvent;
import net.anubis.client.event.impl.MouseInputEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.input.ModuleKeybinds;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.KeySetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11659;
import net.minecraft.class_11785;
import net.minecraft.class_12249;
import net.minecraft.class_1309;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1921;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3489;
import net.minecraft.class_3675;
import net.minecraft.class_4587;
import net.minecraft.class_746;
import net.minecraft.class_7923;
import net.minecraft.class_811;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/WeaponInspectModule.class */
@Environment(EnvType.CLIENT)
public final class WeaponInspectModule extends ClientModule {
    public static volatile WeaponInspectModule active;
    private static final String SWORDS = "Swords";
    private static final String WEAPONS = "Weapons";
    private static final String ANY_ITEM = "Any Item";
    private static final float EASE_OUT_SECONDS = 0.15f;
    private static final int IDLE = 0;
    private static final int INSPECT = 1;
    private static final int DEPLOY = 2;
    private static final float SPARKLE_SIZE = 0.07f;
    private final KeySetting key;
    private final ModeSetting skin;
    private final ModeSetting style;
    private final ModeSetting items;
    private final NumberSetting speed;
    private final NumberSetting knifeSize;
    private final BooleanSetting showArm;
    private final BooleanSetting deploy;
    private final BooleanSetting slashTrail;
    private final NumberSetting trailLength;
    private final BooleanSetting sparkles;
    private final BooleanSetting sneakSkips;
    private int mode;
    private InspectAnimation.Clip clip;
    private KnifeSkin clipKnife;
    private long startedAt;
    private float seconds;
    private boolean easingOut;
    private long easeStartedAt;
    private float easeFrom;
    private int slot;
    private class_1792 item;
    private int heldSlot;
    private class_1792 heldItem;
    private final float[] state;
    private final float[] trailSample;
    private final SlashTrail[] trails;
    private final Matrix4f base;
    private final Matrix4f baseInverse;
    private final Matrix3f baseNormal;
    private boolean baseKnown;
    private static final class_2960 KNIFE_TEXTURE = class_2960.method_60655(AddonManager.ENTRYPOINT, "textures/misc/knife_base.png");
    private static final float[] REST = new float[16];

    public WeaponInspectModule() {
        super("weapon-inspect", "Weapon Inspect", "Knife skins for your sword and CS-style inspects with a key of your choice (F by default); hold the key to keep the knife spinning.", ModuleCategory.VISUALS);
        this.key = (KeySetting) addSetting("key", new KeySetting("Inspect Key", 70));
        this.skin = (ModeSetting) addSetting("skin", new ModeSetting("Knife Skin", "Butterfly", Surface.BG_NONE, "Butterfly", "Karambit", "Katana"));
        this.style = (ModeSetting) addSetting("style", new ModeSetting("Style", "Classic", "Classic", "Twirl"));
        this.items = (ModeSetting) addSetting("items", new ModeSetting("Items", SWORDS, SWORDS, WEAPONS, ANY_ITEM));
        this.speed = (NumberSetting) addSetting("speed", new NumberSetting("Speed", 1.0f, 0.5f, 2.0f).step(0.05f));
        this.knifeSize = (NumberSetting) addSetting("knife-size", new NumberSetting("Knife Size", 1.0f, 0.5f, 2.0f).step(0.05f));
        this.showArm = (BooleanSetting) addSetting("show-arm", new BooleanSetting("Show Arm", true));
        this.deploy = (BooleanSetting) addSetting("deploy", new BooleanSetting("Deploy Animation", true));
        this.slashTrail = (BooleanSetting) addSetting("slash-trail", new BooleanSetting("Slash Trail", true));
        this.trailLength = (NumberSetting) addSetting("trail-length", new NumberSetting("Trail Length", 0.14f, 0.05f, 0.35f).step(0.01f));
        this.sparkles = (BooleanSetting) addSetting("sparkles", new BooleanSetting("Blade Sparkle", true));
        this.sneakSkips = (BooleanSetting) addSetting("sneak-skips", new BooleanSetting("Sneak Skips Inspect", true));
        this.mode = 0;
        this.heldSlot = -1;
        this.state = new float[16];
        this.trailSample = new float[12];
        this.trails = new SlashTrail[2];
        this.base = new Matrix4f();
        this.baseInverse = new Matrix4f();
        this.baseNormal = new Matrix3f();
        for (int i = 0; i < this.trails.length; i++) {
            this.trails[i] = new SlashTrail();
        }
        this.knifeSize.visibleWhen(() -> {
            return !this.skin.is(Surface.BG_NONE);
        });
        this.deploy.visibleWhen(() -> {
            return !this.skin.is(Surface.BG_NONE);
        });
        this.sparkles.visibleWhen(() -> {
            return !this.skin.is(Surface.BG_NONE);
        });
        NumberSetting numberSetting = this.trailLength;
        BooleanSetting booleanSetting = this.slashTrail;
        Objects.requireNonNull(booleanSetting);
        numberSetting.visibleWhen(booleanSetting::get);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        this.mode = 0;
        this.heldItem = null;
        clearTrails();
        active = this;
        on(KeyInputEvent.TYPE, Stage.PRE, EventPriority.HIGH, this::onKey);
        on(MouseInputEvent.TYPE, Stage.PRE, EventPriority.HIGH, this::onMouse);
        on(TickEvent.TYPE, Stage.POST, this::onTick);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        active = null;
        this.mode = 0;
        clearTrails();
    }

    public static void handsBegin(class_4587 pose) {
        WeaponInspectModule module = active;
        if (module == null) {
            return;
        }
        module.base.set(pose.method_23760().method_23761());
        module.base.invert(module.baseInverse);
        module.baseNormal.set(pose.method_23760().method_23762());
        module.baseKnown = true;
    }

    public static boolean renderItem(class_4587 pose, class_1309 holder, class_1799 stack, class_811 context, class_11659 collector, int light, boolean mainHand) {
        WeaponInspectModule module = active;
        return module != null && module.render(pose, holder, stack, context, collector, light, mainHand);
    }

    private void onKey(KeyInputEvent event) {
        if (event.action != 0) {
            if (pressed(event.key, event.action == 2)) {
                event.setCancelled(true);
            }
        }
    }

    private void onMouse(MouseInputEvent event) {
        if (event.action == 1 && pressed(ModuleKeybinds.MOUSE_OFFSET + event.button, false)) {
            event.setCancelled(true);
        }
    }

    private boolean pressed(int pressed, boolean repeat) {
        int bound = this.key.value();
        if (!ModuleKeybinds.isBindable(bound) || pressed != bound) {
            return false;
        }
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (mc.field_1755 != null || player == null || player.method_7325()) {
            return false;
        }
        if (this.sneakSkips.get() && player.method_5715()) {
            return false;
        }
        class_1799 held = player.method_6047();
        if (!inspectable(held)) {
            return false;
        }
        if (repeat || player.method_6115()) {
            return true;
        }
        start(1, player.method_31548().method_67532(), held);
        return true;
    }

    private boolean held() {
        int bound = this.key.value();
        class_310 mc = class_310.method_1551();
        if (!ModuleKeybinds.isBindable(bound) || mc.field_1755 != null) {
            return false;
        }
        if (bound >= 1000) {
            return GLFW.glfwGetMouseButton(mc.method_22683().method_4490(), bound - ModuleKeybinds.MOUSE_OFFSET) == 1;
        }
        return class_3675.method_15987(mc.method_22683(), bound);
    }

    private boolean inspectable(class_1799 stack) {
        if (stack.method_7960()) {
            return false;
        }
        if (this.items.is(ANY_ITEM) || isSword(stack)) {
            return true;
        }
        return this.items.is(WEAPONS) && (stack.method_31573(class_3489.field_42612) || stack.method_31573(class_3489.field_63257) || stack.method_31574(class_1802.field_49814) || stack.method_31574(class_1802.field_8547));
    }

    private static boolean isSword(class_1799 stack) {
        return !stack.method_7960() && (stack.method_31573(class_3489.field_42611) || class_7923.field_41178.method_10221(stack.method_7909()).method_12832().endsWith("_sword"));
    }

    private static boolean handheld(class_1799 stack) {
        return isSword(stack) || stack.method_31573(class_3489.field_42612) || stack.method_31573(class_3489.field_42614) || stack.method_31573(class_3489.field_42615) || stack.method_31573(class_3489.field_42613);
    }

    private void start(int next, int selected, class_1799 held) {
        InspectAnimation.Clip clip;
        KnifeSkin knife = isSword(held) ? KnifeSkin.of(this.skin.get()) : null;
        if (next == 2) {
            clip = knife.deploy;
        } else {
            clip = knife != null ? knife.inspect : InspectAnimation.clip(this.style.get());
        }
        this.clip = clip;
        this.clipKnife = knife;
        this.mode = next;
        this.seconds = this.clip.seconds;
        this.easingOut = false;
        this.startedAt = System.nanoTime();
        this.slot = selected;
        this.item = held.method_7909();
    }

    private void easeOut() {
        long now = System.nanoTime();
        this.easeFrom = Math.min(progress(now), 1.0f);
        this.easeStartedAt = now;
        this.easingOut = true;
    }

    private void onTick(TickEvent event) {
        class_746 player = class_310.method_1551().field_1724;
        if (player == null) {
            this.mode = 0;
            this.heldItem = null;
            return;
        }
        class_1799 held = player.method_6047();
        int selected = player.method_31548().method_67532();
        if (selected != this.heldSlot || !held.method_31574(this.heldItem)) {
            this.heldSlot = selected;
            this.heldItem = held.method_7909();
            if (!this.deploy.get() || !isSword(held) || KnifeSkin.of(this.skin.get()) == null) {
                if (this.mode == 0 || this.easingOut) {
                    return;
                }
                easeOut();
                return;
            }
            start(2, selected, held);
            return;
        }
        if (this.mode == 0 || this.easingOut) {
            return;
        }
        if (!player.field_6252) {
            if (player.method_6115()) {
                easeOut();
                return;
            } else {
                if (loopedProgress(System.nanoTime()) >= 1.0f) {
                    this.mode = 0;
                    return;
                }
                return;
            }
        }
        this.mode = 0;
    }

    private float progress(long now) {
        return ((now - this.startedAt) / 1.0E9f) / (this.seconds / this.speed.get());
    }

    private float loopedProgress(long now) {
        float progress = progress(now);
        if (this.mode != 1 || !this.clip.loops() || progress < this.clip.loopExit || !held()) {
            return progress;
        }
        float duration = this.seconds / this.speed.get();
        long cycle = (long) ((this.clip.loopExit - this.clip.loopEnter) * duration * 1.0E9f);
        if (cycle <= 0) {
            return progress;
        }
        long over = (long) ((progress - this.clip.loopExit) * duration * 1.0E9f);
        this.startedAt += ((over / cycle) + 1) * cycle;
        return progress(now);
    }

    private float animate(class_1799 stack, KnifeSkin knife, long now) {
        float progress;
        if (this.mode == 0 || !stack.method_31574(this.item)) {
            return 0.0f;
        }
        if (knife != this.clipKnife) {
            this.mode = 0;
            return 0.0f;
        }
        float weight = 1.0f;
        if (this.easingOut) {
            float t = ((now - this.easeStartedAt) / 1.0E9f) / EASE_OUT_SECONDS;
            if (t >= 1.0f) {
                this.mode = 0;
                return 0.0f;
            }
            progress = this.easeFrom;
            weight = 1.0f - ((t * t) * (3.0f - (2.0f * t)));
        } else {
            progress = loopedProgress(now);
            if (progress >= 1.0f) {
                this.mode = 0;
                return 0.0f;
            }
        }
        this.clip.sample(progress, this.state);
        return weight;
    }

    private boolean render(class_4587 pose, class_1309 holder, class_1799 stack, class_811 context, class_11659 collector, int light, boolean mainHand) {
        FirstPersonArm.Arm arm;
        int side = context == class_811.field_4321 ? -1 : 1;
        boolean sword = isSword(stack);
        KnifeSkin knife = sword ? KnifeSkin.of(this.skin.get()) : null;
        long now = System.nanoTime();
        float weight = mainHand ? animate(stack, knife, now) : 0.0f;
        float[] at = weight > 0.0f ? this.state : REST;
        boolean trailing = mainHand && sword && this.slashTrail.get() && this.baseKnown;
        if (mainHand && !trailing) {
            clearTrails();
        }
        if (knife == null) {
            if (((this.showArm.get() && handheld(stack)) || HandModelModule.covers(stack, mainHand)) && (arm = FirstPersonArm.arm()) != null) {
                FirstPersonArm.hold(pose, arm, stack, context, holder, collector, light, at[0] * weight, at[1] * weight, at[2] * weight);
            }
            if (weight > 0.0f) {
                InspectAnimation.place(pose, at, weight, side, SPARKLE_SIZE, 0.2f, SPARKLE_SIZE);
            }
            if (trailing) {
                swordTrail(pose.method_23760().method_23761(), side);
                record(now, 1);
                submitGlow(collector, now, KnifeMaterial.of(stack).glow, 0.0f, null);
                return false;
            }
            return false;
        }
        KnifeMaterial material = KnifeMaterial.of(stack);
        FirstPersonArm.Arm arm2 = (this.showArm.get() || HandModelModule.wants(mainHand)) ? FirstPersonArm.arm() : null;
        boolean slim = arm2 != null && arm2.slim();
        pose.method_22903();
        if (side < 0) {
            pose.method_22905(-1.0f, 1.0f, 1.0f);
        }
        knife.draw(pose, at, weight, slim, this.knifeSize.get(), sink(collector, light, side, arm2, mainHand && offHandFree(), material, stack.method_7958()), trailing ? this.trailSample : null);
        pose.method_22909();
        if (trailing) {
            record(now, knife.streakCount());
            float flash = (!this.sparkles.get() || weight <= 0.0f) ? 0.0f : knife.sparkle(at, weight);
            submitGlow(collector, now, material.glow, flash, knife);
            return true;
        }
        return true;
    }

    private static KnifeSkin.Sink sink(final class_11659 collector, final int light, final int side, final FirstPersonArm.Arm arm, final boolean otherFree, final KnifeMaterial material, final boolean foil) {
        final class_1921 solid = class_12249.method_75994(KNIFE_TEXTURE);
        final int[] palette = material.palette;
        return new KnifeSkin.Sink() { // from class: net.anubis.client.module.impl.render.WeaponInspectModule.1
            @Override // net.anubis.client.module.impl.render.KnifeSkin.Sink
            public void part(class_4587 part, KnifeMesh mesh, KnifeMesh.Shine shine) {
                class_11659 class_11659Var = collector;
                class_1921 class_1921Var = solid;
                int[] iArr = palette;
                int i = light;
                class_11659Var.method_73483(part, class_1921Var, (p, out) -> {
                    mesh.emit(p, out, iArr, i, shine);
                });
                if (foil) {
                    class_11785 class_11785VarMethod_73529 = collector.method_73529(1);
                    class_1921 class_1921VarMethod_75997 = class_12249.method_75997();
                    int[] iArr2 = palette;
                    int i2 = light;
                    class_11785VarMethod_73529.method_73483(part, class_1921VarMethod_75997, (p2, out2) -> {
                        mesh.emit(p2, out2, iArr2, i2, KnifeMesh.Shine.NONE);
                    });
                }
            }

            @Override // net.anubis.client.module.impl.render.KnifeSkin.Sink
            public void glow(class_4587 part, KnifeMesh mesh, KnifeMesh.Shine shine) {
                class_11785 class_11785VarMethod_73529 = collector.method_73529(1);
                class_1921 class_1921VarMethod_76014 = class_12249.method_76014(WeaponInspectModule.KNIFE_TEXTURE);
                KnifeMaterial knifeMaterial = material;
                class_11785VarMethod_73529.method_73483(part, class_1921VarMethod_76014, (p, out) -> {
                    mesh.emitGlow(p, out, knifeMaterial.glow, shine);
                });
            }

            @Override // net.anubis.client.module.impl.render.KnifeSkin.Sink
            public void arm(class_4587 hand, boolean other) {
                if (arm != null) {
                    if (!other || otherFree) {
                        arm.draw(hand, (side < 0) != other, collector, light);
                    }
                }
            }
        };
    }

    private static boolean offHandFree() {
        class_746 player = class_310.method_1551().field_1724;
        return player != null && player.method_6079().method_7960();
    }

    private void swordTrail(Matrix4f pose, int side) {
        Matrix4f m = new Matrix4f(pose).translate((side * 1.13f) / 16.0f, 0.2f, 0.070625f).rotate(new Quaternionf().rotationXYZ(0.0f, -1.5707964f, 0.43633232f)).scale(0.68f).translate(-0.5f, -0.5f, -0.5f);
        Vector3f p = m.transformPosition(0.3125f, 0.3125f, 0.5f, new Vector3f());
        this.trailSample[0] = p.x;
        this.trailSample[1] = p.y;
        this.trailSample[2] = p.z;
        m.transformPosition(0.96875f, 0.96875f, 0.5f, p);
        this.trailSample[3] = p.x;
        this.trailSample[4] = p.y;
        this.trailSample[5] = p.z;
    }

    private void record(long now, int streaks) {
        Vector3f p = new Vector3f();
        for (int s = 0; s < this.trails.length; s++) {
            if (s >= streaks) {
                this.trails[s].clear();
            } else {
                for (int i = s * 6; i < (s * 6) + 6; i += 3) {
                    this.baseInverse.transformPosition(this.trailSample[i], this.trailSample[i + 1], this.trailSample[i + 2], p);
                    this.trailSample[i] = p.x;
                    this.trailSample[i + 1] = p.y;
                    this.trailSample[i + 2] = p.z;
                }
                this.trails[s].add(now, this.trailSample, s * 6);
            }
        }
    }

    private void clearTrails() {
        for (SlashTrail trail : this.trails) {
            trail.clear();
        }
    }

    private void submitGlow(class_11659 collector, long now, int glow, float flash, KnifeSkin knife) {
        long lifetime = (long) (this.trailLength.get() * 1.0E9f);
        int streaks = knife != null ? knife.streakCount() : 1;
        boolean showing = flash > 0.0f;
        for (int s = 0; s < streaks && !showing; s++) {
            showing = !this.trails[s].isEmpty(now, lifetime);
        }
        if (showing) {
            float tipX = this.trailSample[3];
            float tipY = this.trailSample[4];
            float tipZ = this.trailSample[5];
            float turn = ((now % 6283185307L) / 1.0E9f) * 1.5f;
            class_4587 hand = new class_4587();
            hand.method_23760().method_23761().set(this.base);
            hand.method_23760().method_23762().set(this.baseNormal);
            collector.method_73529(1).method_73483(hand, class_12249.method_76014(KNIFE_TEXTURE), (p, out) -> {
                for (int s2 = 0; s2 < streaks; s2++) {
                    this.trails[s2].emit(p, out, now, lifetime, glow, knife != null ? knife.streakStrength(s2) : 1.0f);
                }
                if (flash > 0.0f) {
                    SlashTrail.sparkle(p, out, tipX, tipY, tipZ, SPARKLE_SIZE * flash, turn, glow, flash);
                }
            });
        }
    }
}
