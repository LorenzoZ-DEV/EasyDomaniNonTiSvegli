package net.anubis.client.module.impl.movement;

import com.example.client.mixin.AccessorLocalPlayer;
import com.example.client.mixin.AccessorServerboundMovePlayerPacket;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.PacketReceiveNettyEvent;
import net.anubis.client.event.impl.PacketSendEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.impl.misc.FreecamModule;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1656;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2696;
import net.minecraft.class_2793;
import net.minecraft.class_2828;
import net.minecraft.class_310;
import net.minecraft.class_634;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/movement/FlyModule.class */
@Environment(EnvType.CLIENT)
public final class FlyModule extends ClientModule {
    public static volatile FlyModule active;
    private static final String ABILITIES = "Abilities";
    private static final String VELOCITY = "Velocity";
    private static final String KICK_NORMAL = "Normal";
    private static final String KICK_PACKET = "Packet";
    private static final String KICK_NONE = "None";
    private static final double ANTI_KICK_DROP = 0.0313d;
    private static final float VANILLA_FLYING_SPEED = 0.05f;
    private static final String DEFAULT_MODE = "Abilities";
    private static final float DEFAULT_SPEED = 0.1f;
    private final ModeSetting mode;
    private final NumberSetting speed;
    private final BooleanSetting verticalSpeedMatch;
    private final BooleanSetting noSneak;
    private final ModeSetting antiKickMode;
    private final NumberSetting delay;
    private final NumberSetting offTime;
    private int delayLeft;
    private int offLeft;
    private boolean flip;
    private float lastYaw;
    private double lastPacketY;
    private class_746 trackedPlayer;

    public FlyModule() {
        super("fly", "Fly", "Lets you fly.", ModuleCategory.MOVEMENT);
        this.mode = (ModeSetting) addSetting("fly-mode", new ModeSetting("Mode", "Abilities", "Abilities", VELOCITY));
        this.speed = (NumberSetting) addSetting("fly-speed", new NumberSetting("Speed", DEFAULT_SPEED, 0.0f, 10.0f).step(VANILLA_FLYING_SPEED));
        this.verticalSpeedMatch = (BooleanSetting) addSetting("vertical-speed-match", new BooleanSetting("Vertical Speed Match", false));
        this.noSneak = (BooleanSetting) addSetting("no-sneak", new BooleanSetting("No Sneak", false));
        this.antiKickMode = (ModeSetting) addSetting("anti-kick-mode", new ModeSetting("Anti Kick Mode", KICK_PACKET, KICK_NORMAL, KICK_PACKET, "None"));
        this.delay = (NumberSetting) addSetting("delay", new NumberSetting("Anti Kick Delay", 20.0f, 1.0f, 200.0f).step(1.0f));
        this.offTime = (NumberSetting) addSetting("off-time", new NumberSetting("Anti Kick Off Time", 1.0f, 1.0f, 20.0f).step(1.0f));
        this.delayLeft = 20;
        this.offLeft = 1;
        this.lastPacketY = Double.MAX_VALUE;
        this.noSneak.visibleWhen(() -> {
            return this.mode.is(VELOCITY);
        });
        this.mode.onChange(() -> {
            class_746 player;
            if (isEnabled() && (player = class_310.method_1551().field_1724) != null) {
                abilitiesOff(player);
            }
        });
    }

    @Override // net.anubis.client.module.ClientModule
    public boolean persistEnabled() {
        return false;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        class_746 player = class_310.method_1551().field_1724;
        if (player == null) {
            requestDisable();
            return;
        }
        this.delayLeft = this.delay.getInt();
        this.offLeft = this.offTime.getInt();
        resetPlayerState(player);
        active = this;
        if (this.mode.is("Abilities") && !player.method_7325()) {
            class_1656 abilities = player.method_31549();
            abilities.field_7479 = true;
            if (!abilities.field_7477) {
                abilities.field_7478 = true;
            }
        }
        on(TickEvent.TYPE, Stage.PRE, this::onPreTick);
        on(TickEvent.TYPE, Stage.POST, this::onPostTick);
        on(PacketSendEvent.TYPE, this::onPacketSend);
        on(PacketReceiveNettyEvent.TYPE, this::onPacketReceiveNetty);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        active = null;
        class_746 player = class_310.method_1551().field_1724;
        if (player != null && this.mode.is("Abilities")) {
            abilitiesOff(player);
        }
        this.trackedPlayer = null;
    }

    private void resetPlayerState(class_746 player) {
        this.trackedPlayer = player;
        this.lastYaw = player.method_36454();
        this.lastPacketY = Double.MAX_VALUE;
    }

    private void onPreTick(TickEvent event) {
        class_746 player = class_310.method_1551().field_1724;
        if (player == null) {
            requestDisable();
            return;
        }
        if (player != this.trackedPlayer) {
            resetPlayerState(player);
        }
        float currentYaw = player.method_36454();
        if (player.field_6017 >= 3.0d && currentYaw == this.lastYaw && player.method_18798().method_1033() < 0.003d) {
            player.method_36456(currentYaw + (this.flip ? 1 : -1));
            this.flip = !this.flip;
        }
        this.lastYaw = currentYaw;
    }

    private void onPostTick(TickEvent event) {
        class_746 player = class_310.method_1551().field_1724;
        if (player == null) {
            return;
        }
        if (player != this.trackedPlayer) {
            resetPlayerState(player);
        }
        if (this.delayLeft > 0) {
            this.delayLeft--;
        }
        if (this.offLeft <= 0 && this.delayLeft <= 0) {
            this.delayLeft = this.delay.getInt();
            this.offLeft = this.offTime.getInt();
            if (this.antiKickMode.is(KICK_PACKET)) {
                forcePositionPacket(player);
            }
        } else if (this.delayLeft <= 0) {
            boolean shouldReturn = false;
            if (this.antiKickMode.is(KICK_NORMAL)) {
                if (this.mode.is("Abilities")) {
                    abilitiesOff(player);
                    shouldReturn = true;
                }
            } else if (this.antiKickMode.is(KICK_PACKET) && this.offLeft == this.offTime.getInt()) {
                forcePositionPacket(player);
            }
            this.offLeft--;
            if (shouldReturn) {
                return;
            }
        }
        if (player.method_36454() != this.lastYaw) {
            player.method_36456(this.lastYaw);
        }
        if (this.mode.is(VELOCITY)) {
            if (!player.method_7325()) {
                player.method_31549().field_7479 = false;
            }
            player.method_18800(0.0d, 0.0d, 0.0d);
            class_243 velocity = player.method_18798();
            if (!isFreecamActive()) {
                class_310 mc = class_310.method_1551();
                double vertical = ((double) this.speed.get()) * ((double) (this.verticalSpeedMatch.get() ? 10.0f : 5.0f));
                if (mc.field_1690.field_1903.method_1434()) {
                    velocity = velocity.method_1031(0.0d, vertical, 0.0d);
                }
                if (mc.field_1690.field_1832.method_1434()) {
                    velocity = velocity.method_1023(0.0d, vertical, 0.0d);
                }
            }
            player.method_18799(velocity);
            if (this.noSneak.get()) {
                player.method_24830(false);
                return;
            }
            return;
        }
        if (player.method_7325()) {
            return;
        }
        class_1656 abilities = player.method_31549();
        abilities.method_7248(this.speed.get());
        abilities.field_7479 = true;
        if (abilities.field_7477) {
            return;
        }
        abilities.field_7478 = true;
    }

    private void onPacketSend(PacketSendEvent event) {
        class_2828.class_2830 class_2829Var;
        class_310 mc = class_310.method_1551();
        if (mc.method_18854()) {
            if (event.packet instanceof class_2793) {
                this.lastPacketY = Double.MAX_VALUE;
                return;
            }
            class_2596<?> class_2596Var = event.packet;
            if (class_2596Var instanceof class_2828) {
                class_2828 packet = (class_2828) class_2596Var;
                if (this.antiKickMode.is(KICK_PACKET)) {
                    class_746 player = mc.field_1724;
                    class_634 connection = mc.method_1562();
                    if (player == null || connection == null) {
                        return;
                    }
                    double currentY = packet.method_12268(Double.MAX_VALUE);
                    if (currentY != Double.MAX_VALUE) {
                        antiKickPacket(player, packet, currentY);
                        return;
                    }
                    if (packet.method_36172()) {
                        class_2829Var = new class_2828.class_2830(player.method_23317(), player.method_23318(), player.method_23321(), packet.method_12271(0.0f), packet.method_12270(0.0f), packet.method_12273(), player.field_5976);
                    } else {
                        class_2829Var = new class_2828.class_2829(player.method_23317(), player.method_23318(), player.method_23321(), packet.method_12273(), player.field_5976);
                    }
                    event.setCancelled(true);
                    antiKickPacket(player, class_2829Var, player.method_23318());
                    connection.method_52787(class_2829Var);
                }
            }
        }
    }

    private void antiKickPacket(class_746 player, class_2828 packet, double currentY) {
        if (this.delayLeft <= 0 && this.lastPacketY != Double.MAX_VALUE && shouldFlyDown(currentY, this.lastPacketY) && isOnAir(player)) {
            ((AccessorServerboundMovePlayerPacket) packet).anubis$setY(this.lastPacketY - ANTI_KICK_DROP);
        } else {
            this.lastPacketY = currentY;
        }
    }

    private void onPacketReceiveNetty(PacketReceiveNettyEvent event) {
        class_2696 class_2696Var = event.packet;
        if (class_2696Var instanceof class_2696) {
            class_2696 packet = class_2696Var;
            if (this.mode.is("Abilities")) {
                event.setCancelled(true);
                class_310 mc = class_310.method_1551();
                mc.execute(() -> {
                    class_746 player = mc.field_1724;
                    if (player == null) {
                        return;
                    }
                    class_1656 abilities = player.method_31549();
                    abilities.field_7480 = packet.method_11695();
                    abilities.field_7477 = packet.method_11696();
                    abilities.method_7250(packet.method_11691());
                });
            }
        }
    }

    private static boolean shouldFlyDown(double currentY, double lastY) {
        return currentY >= lastY || lastY - currentY < ANTI_KICK_DROP;
    }

    private static void forcePositionPacket(class_746 player) {
        ((AccessorLocalPlayer) player).anubis$setPositionReminder(20);
    }

    private static boolean isOnAir(class_746 player) {
        class_238 box = player.method_5829().method_1014(0.0625d).method_1012(0.0d, -0.55d, 0.0d);
        return player.method_73183().method_29546(box).allMatch((v0) -> {
            return v0.method_26215();
        });
    }

    private static boolean isFreecamActive() {
        FreecamModule freecam = (FreecamModule) ModuleRegistry.get(FreecamModule.class);
        return freecam != null && freecam.isEnabled();
    }

    private static void abilitiesOff(class_746 player) {
        if (player.method_7325()) {
            return;
        }
        class_1656 abilities = player.method_31549();
        abilities.field_7479 = false;
        abilities.method_7248(VANILLA_FLYING_SPEED);
        if (abilities.field_7477) {
            return;
        }
        abilities.field_7478 = false;
    }

    public float getOffGroundSpeed(class_746 player) {
        if (isEnabled() && this.mode.is(VELOCITY)) {
            return this.speed.get() * (player.method_5624() ? 15.0f : 10.0f);
        }
        return -1.0f;
    }

    public boolean noSneak() {
        return isEnabled() && this.mode.is(VELOCITY) && this.noSneak.get();
    }

    public boolean suppressFlightVerticalInput() {
        return isEnabled() && isFreecamActive();
    }
}
