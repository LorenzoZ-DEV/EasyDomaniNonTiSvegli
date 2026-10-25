package net.anubis.client.module.impl.misc;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.CombatMacroLock;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.impl.combat.melee.MeleeAttacks;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.class_239;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3675;
import net.minecraft.class_3965;
import org.lwjgl.glfw.GLFW;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/AutoClickerModule.class */
@Environment(EnvType.CLIENT)
public final class AutoClickerModule extends ClientModule {
    private static final String CLICK = "Click";
    private static final String HOLD = "Hold";
    private static final String OFF = "Off";
    private static final double TICK_MS = 50.0d;
    private static final double PACE_MIN_MS = 500.0d;
    private static final double PACE_MAX_MS = 1500.0d;
    private static final double PACE_EASE = 0.3d;
    private static final double HOLD_MEAN_MS = 55.0d;
    private static final double HOLD_SPREAD_MS = 18.0d;
    private static final double HOLD_MIN_MS = 20.0d;
    private static final double HOLD_MAX_MS = 110.0d;
    private static final double HOLD_MAX_GAP = 0.7d;
    private static final double GAP_MIN_FACTOR = 0.6d;
    private static final double GAP_MAX_FACTOR = 2.2d;
    private final ModeSetting left;
    private final NumberSetting leftMin;
    private final NumberSetting leftMax;
    private final ModeSetting right;
    private final NumberSetting rightMin;
    private final NumberSetting rightMax;
    private final NumberSetting randomization;
    private final BooleanSetting onlyWhileHeld;
    private final BooleanSetting breakBlocks;
    private final Button leftButton;
    private final Button rightButton;

    public AutoClickerModule() {
        super("auto-clicker", "Auto Clicker", "Clicks the left and/or right mouse button at a randomised, human CPS.", ModuleCategory.MISC);
        this.left = (ModeSetting) addSetting("left", new ModeSetting("Left", CLICK, CLICK, HOLD, "Off"));
        this.leftMin = (NumberSetting) addSetting("left-min-cps", new NumberSetting("Left Min CPS", 9.0f, 1.0f, 20.0f).step(1.0f));
        this.leftMax = (NumberSetting) addSetting("left-max-cps", new NumberSetting("Left Max CPS", 13.0f, 1.0f, 20.0f).step(1.0f));
        this.right = (ModeSetting) addSetting("right", new ModeSetting("Right", "Off", CLICK, HOLD, "Off"));
        this.rightMin = (NumberSetting) addSetting("right-min-cps", new NumberSetting("Right Min CPS", 8.0f, 1.0f, 20.0f).step(1.0f));
        this.rightMax = (NumberSetting) addSetting("right-max-cps", new NumberSetting("Right Max CPS", 12.0f, 1.0f, 20.0f).step(1.0f));
        this.randomization = (NumberSetting) addSetting("randomization", new NumberSetting("Randomization", 15.0f, 0.0f, 40.0f).step(1.0f));
        this.onlyWhileHeld = (BooleanSetting) addSetting("only-while-held", new BooleanSetting("Only While Held", false));
        this.breakBlocks = (BooleanSetting) addSetting("break-blocks", new BooleanSetting("Break Blocks", true));
        this.leftButton = new Button(true, this.left, this.leftMin, this.leftMax, mc -> {
            return mc.field_1690.field_1886;
        });
        this.rightButton = new Button(false, this.right, this.rightMin, this.rightMax, mc2 -> {
            return mc2.field_1690.field_1904;
        });
        this.leftMin.visibleWhen(() -> {
            return this.left.is(CLICK);
        });
        this.leftMax.visibleWhen(() -> {
            return this.left.is(CLICK);
        });
        this.rightMin.visibleWhen(() -> {
            return this.right.is(CLICK);
        });
        this.rightMax.visibleWhen(() -> {
            return this.right.is(CLICK);
        });
        this.randomization.visibleWhen(this::anyClicking);
        this.onlyWhileHeld.visibleWhen(this::anyClicking);
        this.breakBlocks.visibleWhen(() -> {
            return this.left.is(CLICK);
        });
    }

    @Override // net.anubis.client.module.ClientModule
    public int settingsVersion() {
        return 1;
    }

    @Override // net.anubis.client.module.ClientModule
    public void migrateSettings(int savedVersion) {
        if (savedVersion < 1) {
            this.onlyWhileHeld.set(false);
        }
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        class_310 mc = class_310.method_1551();
        this.leftButton.handBack(mc);
        this.rightButton.handBack(mc);
    }

    @EventHandler
    public void onTick(TickEvent event) {
        class_310 mc = class_310.method_1551();
        boolean allowed = MeleeAttacks.canAct(mc) && mc.method_18506() == null && !CombatMacroLock.heldByOther(this) && !MeleeAttacks.actedThisTick(mc.field_1724);
        this.leftButton.tick(mc, allowed);
        this.rightButton.tick(mc, allowed);
    }

    private boolean anyClicking() {
        return this.left.is(CLICK) || this.right.is(CLICK);
    }

    private static boolean aimsAtBlock(class_310 mc) {
        class_3965 class_3965Var = mc.field_1765;
        if (class_3965Var instanceof class_3965) {
            class_3965 hit = class_3965Var;
            if (hit.method_17783() == class_239.class_240.field_1332 && mc.field_1687 != null && !mc.field_1687.method_8320(hit.method_17777()).method_26215()) {
                return true;
            }
        }
        return false;
    }

    private static boolean physicallyDown(class_310 mc, class_304 mapping) {
        class_3675.class_306 key;
        int value;
        if (mapping.method_1415() || (value = (key = KeyBindingHelper.getBoundKeyOf(mapping)).method_1444()) < 0) {
            return false;
        }
        switch (AnonymousClass1.$SwitchMap$com$mojang$blaze3d$platform$InputConstants$Type[key.method_1442().ordinal()]) {
            case 1:
                return GLFW.glfwGetMouseButton(mc.method_22683().method_4490(), value) == 1;
            case 2:
                return class_3675.method_15987(mc.method_22683(), value);
            default:
                return mapping.method_1434();
        }
    }

    /* JADX INFO: renamed from: net.anubis.client.module.impl.misc.AutoClickerModule$1, reason: invalid class name */
    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/AutoClickerModule$1.class */
    @Environment(EnvType.CLIENT)
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$mojang$blaze3d$platform$InputConstants$Type = new int[class_3675.class_307.values().length];

        static {
            try {
                $SwitchMap$com$mojang$blaze3d$platform$InputConstants$Type[class_3675.class_307.field_1672.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$mojang$blaze3d$platform$InputConstants$Type[class_3675.class_307.field_1668.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/AutoClickerModule$Button.class */
    @Environment(EnvType.CLIENT)
    private final class Button {
        private final boolean isLeft;
        private final ModeSetting mode;
        private final NumberSetting minCps;
        private final NumberSetting maxCps;
        private final Function<class_310, class_304> mapping;
        private boolean driving;
        private boolean running;
        private double clock;
        private double nextPress;
        private double releaseAt;
        private double rate;
        private double pace;
        private double repaceAt;

        Button(boolean isLeft, ModeSetting mode, NumberSetting minCps, NumberSetting maxCps, Function<class_310, class_304> mapping) {
            this.isLeft = isLeft;
            this.mode = mode;
            this.minCps = minCps;
            this.maxCps = maxCps;
            this.mapping = mapping;
        }

        void tick(class_310 mc, boolean allowed) {
            if (!allowed || this.mode.is("Off")) {
                handBack(mc);
                return;
            }
            class_304 key = this.mapping.apply(mc);
            if (this.mode.is(AutoClickerModule.HOLD)) {
                this.running = false;
                this.driving = true;
                key.method_23481(true);
                return;
            }
            if (key.method_1415() || ((AutoClickerModule.this.onlyWhileHeld.get() && !AutoClickerModule.physicallyDown(mc, key)) || ((this.isLeft && AutoClickerModule.this.breakBlocks.get() && AutoClickerModule.aimsAtBlock(mc)) || (!this.isLeft && mc.field_1724.method_6115())))) {
                handBack(mc);
                return;
            }
            if (!this.running) {
                start();
            }
            this.clock += AutoClickerModule.TICK_MS;
            if (this.isLeft) {
                mc.field_1771 = 0;
            }
            class_3675.class_306 bound = KeyBindingHelper.getBoundKeyOf(key);
            while (this.nextPress <= this.clock) {
                class_304.method_1420(bound);
                double gap = nextGap();
                this.releaseAt = this.nextPress + holdFor(gap);
                this.nextPress += gap;
            }
            this.driving = true;
            key.method_23481(this.clock < this.releaseAt);
        }

        private void start() {
            double lo = Math.min(this.minCps.get(), this.maxCps.get());
            double hi = Math.max(this.minCps.get(), this.maxCps.get());
            ThreadLocalRandom random = ThreadLocalRandom.current();
            this.running = true;
            this.clock = 0.0d;
            this.releaseAt = 0.0d;
            this.pace = lo + (random.nextDouble() * (hi - lo));
            this.rate = this.pace;
            this.repaceAt = AutoClickerModule.PACE_MIN_MS + (random.nextDouble() * 1000.0d);
            this.nextPress = nextGap();
        }

        private double nextGap() {
            double lo = Math.min(this.minCps.get(), this.maxCps.get());
            double hi = Math.max(this.minCps.get(), this.maxCps.get());
            ThreadLocalRandom random = ThreadLocalRandom.current();
            if (this.clock >= this.repaceAt) {
                this.pace = lo + (random.nextDouble() * (hi - lo));
                this.repaceAt = this.clock + AutoClickerModule.PACE_MIN_MS + (random.nextDouble() * 1000.0d);
            }
            this.rate = AutoClickerModule.clamp(this.rate + ((this.pace - this.rate) * AutoClickerModule.PACE_EASE), lo, hi);
            double gap = 1000.0d / this.rate;
            double spread = ((double) AutoClickerModule.this.randomization.get()) / 100.0d;
            if (spread <= 0.0d) {
                return gap;
            }
            double factor = 1.0d + (random.nextGaussian() * spread);
            if (random.nextDouble() < spread * 0.25d) {
                factor += 0.4d + (random.nextDouble() * 0.6d);
            }
            return gap * AutoClickerModule.clamp(factor, 0.6d, AutoClickerModule.GAP_MAX_FACTOR);
        }

        private double holdFor(double gap) {
            if (AutoClickerModule.this.randomization.get() <= 0.0f) {
                return gap * 0.5d;
            }
            double hold = AutoClickerModule.HOLD_MEAN_MS + (ThreadLocalRandom.current().nextGaussian() * AutoClickerModule.HOLD_SPREAD_MS);
            return AutoClickerModule.clamp(hold, AutoClickerModule.HOLD_MIN_MS, Math.min(AutoClickerModule.HOLD_MAX_MS, gap * AutoClickerModule.HOLD_MAX_GAP));
        }

        void handBack(class_310 mc) {
            this.running = false;
            if (this.driving) {
                this.driving = false;
                class_304 key = this.mapping.apply(mc);
                key.method_23481(mc.field_1755 == null && AutoClickerModule.physicallyDown(mc, key));
            }
        }
    }
}
