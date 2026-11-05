package xyz.lorenzzzz.easydomani.rotationprobe.packet;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPositionAndRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPong;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientTabComplete;
import java.util.UUID;
import xyz.lorenzzzz.easydomani.rotationprobe.service.AntiVanishProbeDetector;
import xyz.lorenzzzz.easydomani.rotationprobe.service.RotationProbeService;

public final class RotationProbePacketListener extends PacketListenerAbstract {
    private final RotationProbeService rotation;
    private final AntiVanishProbeDetector antiVanish;

    public RotationProbePacketListener(RotationProbeService rotation, AntiVanishProbeDetector antiVanish) {
        super(PacketListenerPriority.LOWEST);
        this.rotation = rotation;
        this.antiVanish = antiVanish;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        PacketTypeCommon t = event.getPacketType();
        if (t == PacketType.Play.Client.TAB_COMPLETE) {
            WrapperPlayClientTabComplete w = new WrapperPlayClientTabComplete(event);
            antiVanish.inspect(event.getUser() != null ? event.getUser().getName() : "?", w.getTransactionId().orElse(-1), w.getText());
            return;
        }
        boolean rot = t == PacketType.Play.Client.PLAYER_ROTATION, posRot = t == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION;
        if (!rot && !posRot && t != PacketType.Play.Client.PONG && t != PacketType.Play.Client.CLIENT_TICK_END) return;
        User u = event.getUser();
        UUID uuid = u != null ? u.getUUID() : null;
        if (uuid == null) return;
        if (rot) {
            WrapperPlayClientPlayerRotation w = new WrapperPlayClientPlayerRotation(event);
            rotation.onRotation(uuid, w.getYaw(), w.getPitch());
            return;
        }
        if (posRot) {
            WrapperPlayClientPlayerPositionAndRotation w = new WrapperPlayClientPlayerPositionAndRotation(event);
            rotation.onRotation(uuid, w.getYaw(), w.getPitch());
            return;
        }
        if (!rotation.hasSession(uuid)) return;
        if (t == PacketType.Play.Client.PONG) {
            if (rotation.onPong(uuid, new WrapperPlayClientPong(event).getId())) event.setCancelled(true);
        } else {
            rotation.onTickEnd(uuid);
        }
    }
}
