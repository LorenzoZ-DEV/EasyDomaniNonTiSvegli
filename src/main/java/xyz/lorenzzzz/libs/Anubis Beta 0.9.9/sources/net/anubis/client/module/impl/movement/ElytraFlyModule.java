package net.anubis.client.module.impl.movement;

import java.util.Objects;
import net.anubis.client.config.Surface;
import net.anubis.client.event.EventPriority;
import net.anubis.client.event.ListenerHandle;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.PacketReceiveNettyEvent;
import net.anubis.client.event.impl.PacketSendEvent;
import net.anubis.client.event.impl.PlayerMoveEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.impl.misc.FreecamModule;
import net.anubis.client.module.impl.movement.elytrafly.BounceMode;
import net.anubis.client.module.impl.movement.elytrafly.ElytraFlightMode;
import net.anubis.client.module.impl.movement.elytrafly.ElytraFlightModes;
import net.anubis.client.module.impl.movement.elytrafly.ElytraFlyInventory;
import net.anubis.client.module.impl.movement.elytrafly.PacketMode;
import net.anubis.client.module.impl.movement.elytrafly.Pitch40Mode;
import net.anubis.client.module.impl.movement.elytrafly.VanillaMode;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1304;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_2708;
import net.minecraft.class_2828;
import net.minecraft.class_2848;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_746;
import net.minecraft.class_9334;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/movement/ElytraFlyModule.class */
@Environment(EnvType.CLIENT)
public final class ElytraFlyModule extends ClientModule {
    public static volatile ElytraFlyModule active;
    private static final float ELYTRA_MAX_REPLACE_DURABILITY = 431.0f;
    public final ModeSetting flightMode;
    public final BooleanSetting autoTakeOff;
    public final NumberSetting fallMultiplier;
    public final NumberSetting horizontalSpeed;
    public final NumberSetting verticalSpeed;
    public final BooleanSetting acceleration;
    public final NumberSetting accelerationStep;
    public final NumberSetting accelerationMin;
    public final BooleanSetting stopInWater;
    public final BooleanSetting dontGoIntoUnloadedChunks;
    public final BooleanSetting autoHover;
    public final BooleanSetting noCrash;
    public final NumberSetting crashLookAhead;
    private final BooleanSetting instaDrop;
    public final NumberSetting pitch40lowerBounds;
    public final NumberSetting pitch40upperBounds;
    public final NumberSetting pitch40rotationSpeedUp;
    public final NumberSetting pitch40rotationSpeedDown;
    public final BooleanSetting autoJump;
    public final ModeSetting yawLockMode;
    public final NumberSetting yaw;
    public final BooleanSetting lockPitch;
    public final NumberSetting pitch;
    public final BooleanSetting restart;
    public final NumberSetting restartDelay;
    public final BooleanSetting sprint;
    public final BooleanSetting manualTakeoff;
    public final BooleanSetting replace;
    public final NumberSetting replaceDurability;
    public final ModeSetting chestSwap;
    public final BooleanSetting autoReplenish;
    public final NumberSetting replenishSlot;
    public final BooleanSetting autoPilot;
    public final BooleanSetting useFireworks;
    public final NumberSetting autoPilotFireworkDelay;
    public final NumberSetting autoPilotMinimumHeight;
    private ElytraFlightModes modeType;
    private ElytraFlightMode currentMode;
    private boolean modeRequestedDisable;
    private volatile boolean positionPacketReceived;
    private boolean moveIsVelocity;
    private class_746 recastPlayer;
    private boolean previousGliding;
    private ListenerHandle<PlayerMoveEvent> groundListener;
    private ListenerHandle<TickEvent> instaDropListener;

    public ElytraFlyModule() {
        super("elytra-fly", "Elytra Fly", "Gives you more control over your elytra.", ModuleCategory.MOVEMENT);
        this.flightMode = (ModeSetting) addSetting("mode", new ModeSetting("Mode", "Vanilla", "Vanilla", "Packet", "Pitch40", "Bounce"));
        this.autoTakeOff = (BooleanSetting) addSetting("auto-take-off", new BooleanSetting("Auto Take Off", false));
        this.fallMultiplier = (NumberSetting) addSetting("fall-multiplier", new NumberSetting("Fall Multiplier", 0.01f, 0.0f, 2.0f).step(0.01f));
        this.horizontalSpeed = (NumberSetting) addSetting("horizontal-speed", new NumberSetting("Horizontal Speed", 1.0f, 0.0f, 10.0f).step(0.1f));
        this.verticalSpeed = (NumberSetting) addSetting("vertical-speed", new NumberSetting("Vertical Speed", 1.0f, 0.0f, 10.0f).step(0.1f));
        this.acceleration = (BooleanSetting) addSetting("acceleration", new BooleanSetting("Acceleration", false));
        this.accelerationStep = (NumberSetting) addSetting("acceleration-step", new NumberSetting("Acceleration Step", 1.0f, 0.1f, 5.0f).step(0.1f));
        this.accelerationMin = (NumberSetting) addSetting("acceleration-start", new NumberSetting("Acceleration Start", 0.0f, 0.0f, 10.0f).step(0.1f));
        this.stopInWater = (BooleanSetting) addSetting("stop-in-water", new BooleanSetting("Stop In Water", true));
        this.dontGoIntoUnloadedChunks = (BooleanSetting) addSetting("no-unloaded-chunks", new BooleanSetting("No Unloaded Chunks", true));
        this.autoHover = (BooleanSetting) addSetting("auto-hover", new BooleanSetting("Auto Hover", false));
        this.noCrash = (BooleanSetting) addSetting("no-crash", new BooleanSetting("No Crash", false));
        this.crashLookAhead = (NumberSetting) addSetting("crash-look-ahead", new NumberSetting("Crash Look Ahead", 5.0f, 1.0f, 15.0f).step(1.0f));
        this.instaDrop = (BooleanSetting) addSetting("insta-drop", new BooleanSetting("Insta Drop", false));
        this.pitch40lowerBounds = (NumberSetting) addSetting("pitch40-lower-bounds", new NumberSetting("Pitch40 Lower Bounds", 180.0f, -128.0f, 360.0f).step(1.0f));
        this.pitch40upperBounds = (NumberSetting) addSetting("pitch40-upper-bounds", new NumberSetting("Pitch40 Upper Bounds", 220.0f, -128.0f, 360.0f).step(1.0f));
        this.pitch40rotationSpeedUp = (NumberSetting) addSetting("pitch40-rotate-speed-up", new NumberSetting("Pitch40 Rotate Speed Up", 5.45f, 1.0f, 20.0f).step(0.05f));
        this.pitch40rotationSpeedDown = (NumberSetting) addSetting("pitch40-rotate-speed-down", new NumberSetting("Pitch40 Rotate Speed Down", 0.9f, 0.5f, 2.0f).step(0.05f));
        this.autoJump = (BooleanSetting) addSetting("auto-jump", new BooleanSetting("Auto Jump", true));
        this.yawLockMode = (ModeSetting) addSetting("yaw-lock", new ModeSetting("Yaw Lock", "Smart", "Smart", "Simple", Surface.BG_NONE));
        this.yaw = (NumberSetting) addSetting("yaw", new NumberSetting("Yaw", 0.0f, 0.0f, 360.0f).step(1.0f));
        this.lockPitch = (BooleanSetting) addSetting("pitch-lock", new BooleanSetting("Pitch Lock", true));
        this.pitch = (NumberSetting) addSetting("pitch", new NumberSetting("Pitch", 85.0f, 0.0f, 90.0f).step(1.0f));
        this.restart = (BooleanSetting) addSetting("restart", new BooleanSetting("Restart", true));
        this.restartDelay = (NumberSetting) addSetting("restart-delay", new NumberSetting("Restart Delay", 7.0f, 0.0f, 20.0f).step(1.0f));
        this.sprint = (BooleanSetting) addSetting("sprint-constantly", new BooleanSetting("Sprint Constantly", true));
        this.manualTakeoff = (BooleanSetting) addSetting("manual-takeoff", new BooleanSetting("Manual Takeoff", false));
        this.replace = (BooleanSetting) addSetting("elytra-replace", new BooleanSetting("Elytra Replace", false));
        this.replaceDurability = (NumberSetting) addSetting("replace-durability", new NumberSetting("Replace Durability", 2.0f, 1.0f, ELYTRA_MAX_REPLACE_DURABILITY).step(1.0f));
        this.chestSwap = (ModeSetting) addSetting("chest-swap", new ModeSetting("Chest Swap", "Never", "Always", "Never", "WaitForGround"));
        this.autoReplenish = (BooleanSetting) addSetting("replenish-fireworks", new BooleanSetting("Replenish Fireworks", false));
        this.replenishSlot = (NumberSetting) addSetting("replenish-slot", new NumberSetting("Replenish Slot", 9.0f, 1.0f, 9.0f).step(1.0f));
        this.autoPilot = (BooleanSetting) addSetting("auto-pilot", new BooleanSetting("Auto Pilot", false));
        this.useFireworks = (BooleanSetting) addSetting("use-fireworks", new BooleanSetting("Use Fireworks", false));
        this.autoPilotFireworkDelay = (NumberSetting) addSetting("firework-delay", new NumberSetting("Firework Delay", 8.0f, 1.0f, 20.0f).step(0.5f));
        this.autoPilotMinimumHeight = (NumberSetting) addSetting("minimum-height", new NumberSetting("Minimum Height", 120.0f, -128.0f, 260.0f).step(1.0f));
        this.modeType = ElytraFlightModes.Vanilla;
        usesNotifications();
        this.flightMode.onChange(this::onModeChanged);
        this.autoTakeOff.visibleWhen(this::steerableMode);
        this.fallMultiplier.visibleWhen(this::steerableMode);
        this.horizontalSpeed.visibleWhen(this::steerableMode);
        this.verticalSpeed.visibleWhen(this::steerableMode);
        this.acceleration.visibleWhen(this::steerableMode);
        this.accelerationStep.visibleWhen(() -> {
            return steerableMode() && this.acceleration.get();
        });
        this.accelerationMin.visibleWhen(() -> {
            return steerableMode() && this.acceleration.get();
        });
        this.stopInWater.visibleWhen(() -> {
            return !this.flightMode.is("Bounce");
        });
        this.autoHover.visibleWhen(() -> {
            return !this.flightMode.is("Bounce");
        });
        this.noCrash.visibleWhen(() -> {
            return !this.flightMode.is("Bounce");
        });
        this.crashLookAhead.visibleWhen(() -> {
            return this.noCrash.get() && !this.flightMode.is("Bounce");
        });
        this.instaDrop.visibleWhen(() -> {
            return !this.flightMode.is("Bounce");
        });
        this.pitch40lowerBounds.visibleWhen(() -> {
            return this.flightMode.is("Pitch40");
        });
        this.pitch40upperBounds.visibleWhen(() -> {
            return this.flightMode.is("Pitch40");
        });
        this.pitch40rotationSpeedUp.visibleWhen(() -> {
            return this.flightMode.is("Pitch40");
        });
        this.pitch40rotationSpeedDown.visibleWhen(() -> {
            return this.flightMode.is("Pitch40");
        });
        this.autoJump.visibleWhen(() -> {
            return this.flightMode.is("Bounce");
        });
        this.yawLockMode.visibleWhen(() -> {
            return this.flightMode.is("Bounce");
        });
        this.yaw.visibleWhen(() -> {
            return this.flightMode.is("Bounce") && this.yawLockMode.is("Simple");
        });
        this.lockPitch.visibleWhen(() -> {
            return this.flightMode.is("Bounce");
        });
        this.pitch.visibleWhen(() -> {
            return this.flightMode.is("Bounce") && this.lockPitch.get();
        });
        this.restart.visibleWhen(() -> {
            return this.flightMode.is("Bounce");
        });
        this.restartDelay.visibleWhen(() -> {
            return this.flightMode.is("Bounce") && this.restart.get();
        });
        this.sprint.visibleWhen(() -> {
            return this.flightMode.is("Bounce");
        });
        this.manualTakeoff.visibleWhen(() -> {
            return this.flightMode.is("Bounce");
        });
        NumberSetting numberSetting = this.replaceDurability;
        BooleanSetting booleanSetting = this.replace;
        Objects.requireNonNull(booleanSetting);
        numberSetting.visibleWhen(booleanSetting::get);
        NumberSetting numberSetting2 = this.replenishSlot;
        BooleanSetting booleanSetting2 = this.autoReplenish;
        Objects.requireNonNull(booleanSetting2);
        numberSetting2.visibleWhen(booleanSetting2::get);
        this.autoPilot.visibleWhen(this::steerableMode);
        this.useFireworks.visibleWhen(() -> {
            return this.autoPilot.get() && steerableMode();
        });
        this.autoPilotFireworkDelay.visibleWhen(() -> {
            return this.useFireworks.get() && steerableMode();
        });
        this.autoPilotMinimumHeight.visibleWhen(() -> {
            return this.autoPilot.get() && steerableMode();
        });
    }

    @Override // net.anubis.client.module.ClientModule
    public boolean persistEnabled() {
        return false;
    }

    private boolean steerableMode() {
        return (this.flightMode.is("Pitch40") || this.flightMode.is("Bounce")) ? false : true;
    }

    public ElytraFlightModes flightModeType() {
        return this.modeType;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (player == null) {
            requestDisable();
            return;
        }
        cancelGroundListener();
        cancelInstaDropListener();
        this.modeRequestedDisable = false;
        this.positionPacketReceived = false;
        this.moveIsVelocity = false;
        this.recastPlayer = player;
        this.previousGliding = player.method_6128();
        this.modeType = ElytraFlightModes.valueOf(this.flightMode.get());
        this.currentMode = createMode(this.modeType);
        this.currentMode.onActivate();
        if (this.modeRequestedDisable) {
            return;
        }
        if ((this.chestSwap.is("Always") || this.chestSwap.is("WaitForGround")) && player.method_6118(class_1304.field_6174).method_7909() != class_1802.field_8833) {
            ElytraFlyInventory.chestSwap();
        }
        active = this;
        on(PlayerMoveEvent.TYPE, Stage.PRE, EventPriority.HIGHEST, this::captureMoveAliasing);
        on(PlayerMoveEvent.TYPE, this::onPlayerMove);
        on(TickEvent.TYPE, Stage.PRE, this::onPreTick);
        on(TickEvent.TYPE, Stage.POST, this::onPostTick);
        on(PacketSendEvent.TYPE, this::onPacketSend);
        on(PacketReceiveNettyEvent.TYPE, this::onPacketReceive);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        ElytraFlightMode mode = this.currentMode;
        this.currentMode = null;
        if (player != null && mode != null) {
            try {
                if (this.autoPilot.get()) {
                    mc.field_1690.field_1894.method_23481(false);
                }
                if (this.chestSwap.is("Always") && player.method_6118(class_1304.field_6174).method_7909() == class_1802.field_8833) {
                    ElytraFlyInventory.chestSwap();
                } else if (this.chestSwap.is("WaitForGround")) {
                    enableGroundListener();
                }
                if (player.method_6128() && this.instaDrop.get()) {
                    enableInstaDropListener();
                }
            } finally {
                active = null;
                this.recastPlayer = null;
            }
        }
        if (mode != null) {
            mode.onDeactivate();
        }
    }

    private void onModeChanged() {
        this.modeType = ElytraFlightModes.valueOf(this.flightMode.get());
        if (isEnabled() && this.currentMode != null) {
            this.currentMode = createMode(this.modeType);
        }
        if (this.modeType == ElytraFlightModes.Pitch40) {
            this.autoPilot.set(false);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private ElytraFlightMode createMode(ElytraFlightModes type) throws MatchException {
        switch (type) {
            case Vanilla:
                return new VanillaMode(this);
            case Packet:
                return new PacketMode(this);
            case Pitch40:
                return new Pitch40Mode(this);
            case Bounce:
                return new BounceMode(this);
            default:
                throw new MatchException((String) null, (Throwable) null);
        }
    }

    public void disableFromMode() {
        this.modeRequestedDisable = true;
        requestDisable();
    }

    private void onPreTick(TickEvent event) {
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null || mc.field_1687 == null) {
            requestDisable();
            return;
        }
        ElytraFlightMode mode = this.currentMode;
        if (mode == null) {
            return;
        }
        if (this.positionPacketReceived) {
            this.positionPacketReceived = false;
            mode.zeroAcceleration();
            mode.onPositionPacket();
        }
        mode.onPreTick();
    }

    private void onPostTick(TickEvent event) {
        ElytraFlightMode mode = this.currentMode;
        if (class_310.method_1551().field_1724 == null || mode == null) {
            return;
        }
        mode.onTick();
    }

    private void onPacketSend(PacketSendEvent event) {
        class_310 mc = class_310.method_1551();
        ElytraFlightMode mode = this.currentMode;
        if (!mc.method_18854() || mc.field_1724 == null || mode == null) {
            return;
        }
        mode.onPacketSend(event.packet);
    }

    private void onPacketReceive(PacketReceiveNettyEvent event) {
        if (event.packet instanceof class_2708) {
            this.positionPacketReceived = true;
        }
    }

    private void captureMoveAliasing(PlayerMoveEvent event) {
        class_746 player = class_310.method_1551().field_1724;
        if (player == null) {
            this.moveIsVelocity = false;
        } else {
            class_243 velocity = player.method_18798();
            this.moveIsVelocity = velocity.field_1352 == event.x && velocity.field_1351 == event.y && velocity.field_1350 == event.z;
        }
    }

    private void onPlayerMove(PlayerMoveEvent event) {
        boolean aliased = this.moveIsVelocity;
        this.moveIsVelocity = false;
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        ElytraFlightMode mode = this.currentMode;
        if (player == null || mc.field_1687 == null || mode == null || !player.method_6118(class_1304.field_6174).method_57826(class_9334.field_54197)) {
            return;
        }
        mode.autoTakeoff();
        boolean bounce = this.modeType == ElytraFlightModes.Bounce;
        if (player.method_6128()) {
            if (!bounce) {
                mode.velX = 0.0d;
                mode.velY = event.y;
                mode.velZ = 0.0d;
                mode.forward = class_243.method_1030(0.0f, player.method_36454()).method_1021(0.1d);
                mode.right = class_243.method_1030(0.0f, player.method_36454() + 90.0f).method_1021(0.1d);
                if (player.method_5799() && this.stopInWater.get()) {
                    player.field_3944.method_52787(new class_2848(player, class_2848.class_2849.field_12982));
                    return;
                }
                mode.handleFallMultiplier();
                mode.handleAutopilot();
                mode.handleAcceleration();
                mode.handleHorizontalSpeed(event);
                mode.handleVerticalSpeed(event);
            }
            int chunkX = (int) ((player.method_23317() + mode.velX) / 16.0d);
            int chunkZ = (int) ((player.method_23321() + mode.velZ) / 16.0d);
            if (this.dontGoIntoUnloadedChunks.get()) {
                if (mc.field_1687.method_2935().method_12123(chunkX, chunkZ)) {
                    if (!bounce) {
                        setMovement(player, aliased, event, mode.velX, mode.velY, mode.velZ);
                    }
                } else {
                    mode.zeroAcceleration();
                    setMovement(player, aliased, event, 0.0d, mode.velY, 0.0d);
                }
            } else if (!bounce) {
                setMovement(player, aliased, event, mode.velX, mode.velY, mode.velZ);
            }
            if (!bounce) {
                mode.onPlayerMove();
            }
        } else if (mode.lastForwardPressed && !bounce) {
            mc.field_1690.field_1894.method_23481(false);
            mode.lastForwardPressed = false;
        }
        if (this.noCrash.get() && player.method_6128() && !bounce) {
            class_243 position = player.method_73189();
            class_243 lookAheadPos = position.method_1019(player.method_18798().method_1029().method_1021(this.crashLookAhead.getInt()));
            class_3959 context = new class_3959(position, new class_243(lookAheadPos.field_1352, player.method_23318(), lookAheadPos.field_1350), class_3959.class_3960.field_17558, class_3959.class_242.field_1348, player);
            class_3965 hitResult = mc.field_1687.method_17742(context);
            if (hitResult != null && hitResult.method_17783() == class_239.class_240.field_1332) {
                setMovement(player, aliased, event, 0.0d, mode.velY, 0.0d);
            }
        }
        if (this.autoHover.get() && player.field_3913.field_54155.comp_3164() && !isFreecamActive() && player.method_6128() && !bounce) {
            class_2338 below = player.method_24515().method_10074();
            class_2680 underState = mc.field_1687.method_8320(below);
            class_2680 under2State = mc.field_1687.method_8320(below.method_10074());
            boolean underCollidable = underState.method_26204().anubis$elytraFlyHasCollision() || !underState.method_26227().method_15769();
            boolean under2Collidable = under2State.method_26204().anubis$elytraFlyHasCollision() || !under2State.method_26227().method_15769();
            if (!underCollidable && under2Collidable) {
                setMovement(player, aliased, event, event.x, -0.10000000149011612d, event.z);
                player.method_36457(class_3532.method_15363(player.method_61414(0.0f), -50.0f, 20.0f));
            }
            if (underCollidable) {
                setMovement(player, aliased, event, event.x, -0.029999999329447746d, event.z);
                player.method_36457(class_3532.method_15363(player.method_61414(0.0f), -50.0f, 20.0f));
                if (player.method_73189().field_1351 <= below.method_10264() + 1.34f) {
                    setMovement(player, aliased, event, event.x, 0.0d, event.z);
                    player.method_5660(false);
                }
            }
        }
    }

    private static void setMovement(class_746 player, boolean aliased, PlayerMoveEvent event, double x, double y, double z) {
        event.x = x;
        event.y = y;
        event.z = z;
        if (aliased) {
            player.method_18800(x, y, z);
        }
    }

    public boolean canPacketEfly(class_746 player) {
        return this.modeType == ElytraFlightModes.Packet && player.method_6118(class_1304.field_6174).method_57826(class_9334.field_54197) && !player.method_24828();
    }

    public boolean modifyFallFlying(class_746 player, boolean original) {
        boolean gliding = original || canPacketEfly(player);
        if (player != this.recastPlayer) {
            this.recastPlayer = player;
            this.previousGliding = false;
        }
        boolean wasGliding = this.previousGliding;
        this.previousGliding = gliding;
        if (wasGliding && !gliding && this.modeType == ElytraFlightModes.Bounce) {
            return BounceMode.recastElytra(player);
        }
        return gliding;
    }

    private void enableGroundListener() {
        if (this.groundListener != null) {
            return;
        }
        this.groundListener = PlayerMoveEvent.TYPE.listen(this, Stage.PRE, EventPriority.NORMAL, event -> {
            class_746 player = class_310.method_1551().field_1724;
            if (player != null && player.method_24828() && player.method_6118(class_1304.field_6174).method_7909() == class_1802.field_8833) {
                ElytraFlyInventory.chestSwap();
                cancelGroundListener();
            }
        });
    }

    private void cancelGroundListener() {
        if (this.groundListener == null) {
            return;
        }
        this.groundListener.unregister();
        this.groundListener = null;
    }

    private void enableInstaDropListener() {
        if (this.instaDropListener != null) {
            return;
        }
        this.instaDropListener = TickEvent.TYPE.listen(this, Stage.POST, EventPriority.NORMAL, event -> {
            class_746 player = class_310.method_1551().field_1724;
            if (player != null && player.method_6128()) {
                player.method_18800(0.0d, 0.0d, 0.0d);
                player.field_3944.method_52787(new class_2828.class_5911(true, player.field_5976));
            } else {
                cancelInstaDropListener();
            }
        });
    }

    private void cancelInstaDropListener() {
        if (this.instaDropListener == null) {
            return;
        }
        this.instaDropListener.unregister();
        this.instaDropListener = null;
    }

    private static boolean isFreecamActive() {
        FreecamModule freecam = (FreecamModule) ModuleRegistry.get(FreecamModule.class);
        return freecam != null && freecam.isEnabled();
    }
}
