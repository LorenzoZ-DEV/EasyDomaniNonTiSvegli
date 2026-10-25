package net.anubis.client.module.impl.movement;

import com.example.client.mixin.AccessorJesusServerboundMovePlayerPacket;
import com.example.client.mixin.AccessorServerboundMovePlayerPacket;
import java.util.Objects;
import net.anubis.client.event.EventPriority;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.PacketSendEvent;
import net.anubis.client.event.impl.PlayerMoveEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10255;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1941;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_259;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2741;
import net.minecraft.class_2828;
import net.minecraft.class_310;
import net.minecraft.class_3609;
import net.minecraft.class_3610;
import net.minecraft.class_3612;
import net.minecraft.class_4985;
import net.minecraft.class_5134;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/movement/JesusModule.class */
@Environment(EnvType.CLIENT)
public final class JesusModule extends ClientModule {
    public static volatile JesusModule active;
    private static final String SOLID = "Solid";
    private static final String IGNORE = "Ignore";
    private static final double ASCEND_SPEED = 0.11d;
    private static final double NCP_STRAFE_SPEED = 0.2873d;
    private static final int NCP_BOB_INTERVAL = 15;
    private static final int OVER_WATER = 1;
    private static final int OVER_LAVA = 2;
    private final BooleanSetting powderSnow;
    private final BooleanSetting ncpBypass;
    private final BooleanSetting slowDown;
    private final ModeSetting waterMode;
    private final BooleanSetting dipIfBurning;
    private final BooleanSetting dipOnSneakWater;
    private final BooleanSetting dipOnFallWater;
    private final NumberSetting dipFallHeightWater;
    private final ModeSetting lavaMode;
    private final BooleanSetting dipIfFireResistant;
    private final BooleanSetting dipOnSneakLava;
    private final BooleanSetting dipOnFallLava;
    private final NumberSetting dipFallHeightLava;
    private int ascending;
    private int swimmingTicks;
    private boolean inBubbleColumn;
    private boolean moveIsVelocity;

    public JesusModule() {
        super("jesus", "Jesus", "Walk on liquids and powder snow like Jesus.", ModuleCategory.MOVEMENT);
        this.powderSnow = (BooleanSetting) addSetting("powder-snow", new BooleanSetting("Powder Snow", true));
        this.ncpBypass = (BooleanSetting) addSetting("ncp-bypass", new BooleanSetting("NCP Bypass", false));
        this.slowDown = (BooleanSetting) addSetting("slow-down", new BooleanSetting("Slow Down", false));
        this.waterMode = (ModeSetting) addSetting("water-mode", new ModeSetting("Water Mode", SOLID, SOLID, IGNORE));
        this.dipIfBurning = (BooleanSetting) addSetting("dip-if-burning", new BooleanSetting("Dip If Burning", true));
        this.dipOnSneakWater = (BooleanSetting) addSetting("water-dip-on-sneak", new BooleanSetting("Water Dip On Sneak", true));
        this.dipOnFallWater = (BooleanSetting) addSetting("water-dip-on-fall", new BooleanSetting("Water Dip On Fall", true));
        this.dipFallHeightWater = (NumberSetting) addSetting("water-dip-fall-height", new NumberSetting("Water Dip Fall Height", 4.0f, 1.0f, 255.0f).step(1.0f));
        this.lavaMode = (ModeSetting) addSetting("lava-mode", new ModeSetting("Lava Mode", SOLID, SOLID, IGNORE));
        this.dipIfFireResistant = (BooleanSetting) addSetting("dip-if-resistant", new BooleanSetting("Dip If Resistant", true));
        this.dipOnSneakLava = (BooleanSetting) addSetting("lava-dip-on-sneak", new BooleanSetting("Lava Dip On Sneak", true));
        this.dipOnFallLava = (BooleanSetting) addSetting("lava-dip-on-fall", new BooleanSetting("Lava Dip On Fall", true));
        this.dipFallHeightLava = (NumberSetting) addSetting("lava-dip-fall-height", new NumberSetting("Lava Dip Fall Height", 4.0f, 1.0f, 255.0f).step(1.0f));
        this.ascending = 10;
        BooleanSetting booleanSetting = this.slowDown;
        BooleanSetting booleanSetting2 = this.ncpBypass;
        Objects.requireNonNull(booleanSetting2);
        booleanSetting.visibleWhen(booleanSetting2::get);
        this.dipIfBurning.visibleWhen(() -> {
            return this.waterMode.is(SOLID);
        });
        this.dipOnSneakWater.visibleWhen(() -> {
            return this.waterMode.is(SOLID);
        });
        this.dipOnFallWater.visibleWhen(() -> {
            return this.waterMode.is(SOLID);
        });
        this.dipFallHeightWater.visibleWhen(() -> {
            return this.waterMode.is(SOLID) && this.dipOnFallWater.get();
        });
        this.dipIfFireResistant.visibleWhen(() -> {
            return this.lavaMode.is(SOLID);
        });
        this.dipOnSneakLava.visibleWhen(() -> {
            return this.lavaMode.is(SOLID);
        });
        this.dipOnFallLava.visibleWhen(() -> {
            return this.lavaMode.is(SOLID);
        });
        this.dipFallHeightLava.visibleWhen(() -> {
            return this.lavaMode.is(SOLID) && this.dipOnFallLava.get();
        });
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        this.swimmingTicks = 0;
        this.inBubbleColumn = false;
        this.moveIsVelocity = false;
        active = this;
        on(TickEvent.TYPE, Stage.POST, this::onPostTick);
        on(PlayerMoveEvent.TYPE, Stage.PRE, EventPriority.HIGHEST, this::captureMoveAliasing);
        on(PlayerMoveEvent.TYPE, this::onMove);
        on(PacketSendEvent.TYPE, this::onPacketSend);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        active = null;
        this.inBubbleColumn = false;
        this.moveIsVelocity = false;
        this.swimmingTicks = 0;
    }

    private void onPostTick(TickEvent event) {
        boolean bubbleColumn = this.inBubbleColumn;
        this.inBubbleColumn = false;
        class_310 mc = class_310.method_1551();
        class_1297 class_1297Var = mc.field_1724;
        if (class_1297Var == null || mc.field_1687 == null || class_1297Var.method_20232()) {
            return;
        }
        if (!class_1297Var.method_5799() || waterShouldBeSolid(class_1297Var)) {
            if (!class_1297Var.method_5771() || lavaShouldBeSolid(class_1297Var)) {
                class_1297 moving = class_1297Var.method_5765() ? class_1297Var.method_5854() : class_1297Var;
                if (moving == null) {
                    return;
                }
                if (bubbleColumn) {
                    if (!mc.field_1690.field_1903.method_1434() || moving.method_18798().field_1351 >= ASCEND_SPEED) {
                        return;
                    }
                    setVelocityY(moving, ASCEND_SPEED);
                    return;
                }
                if (moving.method_5799() || moving.method_5771()) {
                    setVelocityY(moving, ASCEND_SPEED);
                    this.ascending = 0;
                    return;
                }
                class_2680 below = mc.field_1687.method_8320(moving.method_24515().method_10074());
                boolean waterlogged = ((Boolean) below.method_61767(class_2741.field_12508, false)).booleanValue();
                if (this.ascending == 0) {
                    setVelocityY(moving, ASCEND_SPEED);
                } else if (this.ascending == 1 && (below.method_27852(class_2246.field_10382) || below.method_27852(class_2246.field_10164) || waterlogged)) {
                    setVelocityY(moving, 0.0d);
                }
                this.ascending++;
            }
        }
    }

    private static void setVelocityY(class_1297 entity, double y) {
        class_243 v = entity.method_18798();
        entity.method_18800(v.field_1352, y, v.field_1350);
    }

    private void captureMoveAliasing(PlayerMoveEvent event) {
        class_746 player;
        this.moveIsVelocity = false;
        if (this.ncpBypass.get() && (player = class_310.method_1551().field_1724) != null) {
            class_243 v = player.method_18798();
            this.moveIsVelocity = v.field_1352 == event.x && v.field_1351 == event.y && v.field_1350 == event.z;
        }
    }

    private void onMove(PlayerMoveEvent event) {
        if (this.ncpBypass.get()) {
            boolean aliased = this.moveIsVelocity;
            this.moveIsVelocity = false;
            class_310 mc = class_310.method_1551();
            class_746 player = mc.field_1724;
            if (player == null || mc.field_1687 == null) {
                return;
            }
            int over = overLiquid(player);
            boolean water = (over & 1) != 0 && waterShouldBeSolid(player);
            boolean lava = (over & 2) != 0 && lavaShouldBeSolid(player);
            if (!water && !lava) {
                this.swimmingTicks = 0;
                return;
            }
            int i = this.swimmingTicks + 1;
            this.swimmingTicks = i;
            if (i < 15) {
                if (player.method_24828()) {
                    applyStrafe(mc, player, event, isMoving(player) ? NCP_STRAFE_SPEED : 0.0d);
                    if (aliased) {
                        setVelocityXZ(player, event.x, event.z);
                        return;
                    }
                    return;
                }
                return;
            }
            this.swimmingTicks = 0;
            if (this.slowDown.get()) {
                event.x = 0.0d;
                event.z = 0.0d;
                if (aliased) {
                    setVelocityXZ(player, 0.0d, 0.0d);
                }
            }
            setVelocityY(player, 0.08d);
            if (aliased) {
                event.y = 0.08d;
            }
        }
    }

    private static void setVelocityXZ(class_1297 entity, double x, double z) {
        entity.method_18800(x, entity.method_18798().field_1351, z);
    }

    private void onPacketSend(PacketSendEvent event) {
        class_746 player;
        AccessorJesusServerboundMovePlayerPacket accessorJesusServerboundMovePlayerPacket = event.packet;
        if (accessorJesusServerboundMovePlayerPacket instanceof class_2828) {
            AccessorJesusServerboundMovePlayerPacket accessorJesusServerboundMovePlayerPacket2 = (class_2828) accessorJesusServerboundMovePlayerPacket;
            if (this.ncpBypass.get()) {
                class_310 mc = class_310.method_1551();
                if (!mc.method_18854() || (player = mc.field_1724) == null || mc.field_1687 == null) {
                    return;
                }
                if (!player.method_5799() || waterShouldBeSolid(player)) {
                    if (!player.method_5771() || lavaShouldBeSolid(player)) {
                        int over = overLiquid(player);
                        boolean shouldWork = ((over & 1) != 0 && waterShouldBeSolid(player)) || ((over & 2) != 0 && lavaShouldBeSolid(player));
                        if (player.method_5799() || player.method_5771() || player.field_6017 > 3.0d || !shouldWork) {
                            return;
                        }
                        accessorJesusServerboundMovePlayerPacket2.anubis$jesusSetOnGround(false);
                        if (player.method_24828() && accessorJesusServerboundMovePlayerPacket2.method_36171()) {
                            ((AccessorServerboundMovePlayerPacket) accessorJesusServerboundMovePlayerPacket2).anubis$setY(accessorJesusServerboundMovePlayerPacket2.method_12268(0.0d) - (0.02d + (1.0E-4d * ((double) this.swimmingTicks))));
                        }
                    }
                }
            }
        }
    }

    private static boolean isMoving(class_746 player) {
        return (player.field_6250 == 0.0f && player.field_6212 == 0.0f) ? false : true;
    }

    private static void applyStrafe(class_310 mc, class_746 player, PlayerMoveEvent event, double speed) {
        class_241 input = player.field_3913.method_3128();
        float forward = Math.signum(input.field_1342);
        float side = Math.signum(input.field_1343);
        float yaw = player.method_61415(mc.method_61966().method_60637(true));
        if (forward == 0.0f && side == 0.0f) {
            event.x = 0.0d;
            event.z = 0.0d;
            return;
        }
        float strafe = 90.0f * side;
        if (forward != 0.0f) {
            strafe *= forward * 0.5f;
        }
        float yaw2 = yaw - strafe;
        if (forward < 0.0f) {
            yaw2 -= 180.0f;
        }
        double yawRadians = Math.toRadians(yaw2);
        event.x = (-Math.sin(yawRadians)) * speed;
        event.z = Math.cos(yawRadians) * speed;
    }

    private static int overLiquid(class_746 player) {
        class_1297 vehicle = player.method_5854();
        class_238 box = vehicle != null ? player.method_5829().method_991(vehicle.method_5829()) : player.method_5829();
        boolean water = false;
        boolean lava = false;
        for (class_2680 state : player.method_73183().method_29546(box.method_989(0.0d, -0.01d, 0.0d))) {
            if (state.method_27852(class_2246.field_10382) || state.method_26227().method_15772() == class_3612.field_15910) {
                water = true;
            } else if (state.method_27852(class_2246.field_10164)) {
                lava = true;
            } else if (!state.method_26215()) {
                return 0;
            }
        }
        return (water ? 1 : 0) | (lava ? 2 : 0);
    }

    private boolean waterShouldBeSolid(class_746 player) {
        if (player.method_7325() || player.method_31549().field_7479 || (player.method_5854() instanceof class_10255) || FlyModule.active != null) {
            return false;
        }
        if (this.dipIfBurning.get() && player.method_5809()) {
            return false;
        }
        if (this.dipOnSneakWater.get() && class_310.method_1551().field_1690.field_1832.method_1434()) {
            return false;
        }
        if (!this.dipOnFallWater.get() || player.field_6017 <= this.dipFallHeightWater.getInt()) {
            return this.waterMode.is(SOLID);
        }
        return false;
    }

    private boolean lavaShouldBeSolid(class_746 player) {
        if (player.method_7325() || player.method_31549().field_7479 || (player.method_5854() instanceof class_4985)) {
            return false;
        }
        if (isLavaDangerous(player) && this.lavaMode.is(SOLID)) {
            return true;
        }
        if (this.dipOnSneakLava.get() && class_310.method_1551().field_1690.field_1832.method_1434()) {
            return false;
        }
        if (!this.dipOnFallLava.get() || player.field_6017 <= this.dipFallHeightLava.getInt()) {
            return this.lavaMode.is(SOLID);
        }
        return false;
    }

    private boolean isLavaDangerous(class_746 player) {
        class_1293 fireResistance;
        return !this.dipIfFireResistant.get() || (fireResistance = player.method_6112(class_1294.field_5918)) == null || ((double) fireResistance.method_5584()) <= 300.0d * player.method_45325(class_5134.field_51579);
    }

    public boolean canWalkOnFluid(class_3610 fluid) {
        class_746 player = class_310.method_1551().field_1724;
        if (player == null || player.method_5681()) {
            return false;
        }
        class_3609 class_3609VarMethod_15772 = fluid.method_15772();
        if ((class_3609VarMethod_15772 == class_3612.field_15910 || class_3609VarMethod_15772 == class_3612.field_15909) && waterShouldBeSolid(player)) {
            return true;
        }
        return (class_3609VarMethod_15772 == class_3612.field_15908 || class_3609VarMethod_15772 == class_3612.field_15907) && lavaShouldBeSolid(player);
    }

    public class_265 collisionShape(class_1941 level, class_2680 state, class_2338 pos, class_265 shape) {
        class_3610 fluid = state.method_26227();
        if (fluid.method_15769()) {
            return shape;
        }
        class_310 mc = class_310.method_1551();
        if (level != mc.field_1687 || !mc.method_18854()) {
            return shape;
        }
        class_746 player = mc.field_1724;
        if (player == null) {
            return shape;
        }
        if ((state.method_27852(class_2246.field_10382) || fluid.method_15772() == class_3612.field_15910) && !player.method_5799() && waterShouldBeSolid(player) && pos.method_10264() <= player.method_23318() - 1.0d) {
            return class_259.method_1077();
        }
        if (state.method_27852(class_2246.field_10164) && !player.method_5771() && lavaShouldBeSolid(player) && (isLavaDangerous(player) || pos.method_10264() <= player.method_23318() - 1.0d)) {
            return class_259.method_1077();
        }
        return shape;
    }

    public boolean canWalkOnPowderSnow() {
        return this.powderSnow.get();
    }

    public void markInBubbleColumn() {
        this.inBubbleColumn = true;
    }
}
