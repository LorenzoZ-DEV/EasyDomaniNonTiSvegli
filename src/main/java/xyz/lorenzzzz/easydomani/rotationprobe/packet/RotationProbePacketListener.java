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
import java.util.concurrent.Executor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.lorenzzzz.easydomani.message.service.MessageService;
import xyz.lorenzzzz.easydomani.rotationprobe.service.AntiVanishProbeDetector;
import xyz.lorenzzzz.easydomani.rotationprobe.service.RotationProbeService;

public final class RotationProbePacketListener extends PacketListenerAbstract {
    private final RotationProbeService rotation;
    private final AntiVanishProbeDetector antiVanish;
    private final MessageService messages;
    private final Executor main;

    public RotationProbePacketListener(RotationProbeService rotation, AntiVanishProbeDetector antiVanish, MessageService messages, Executor main) {
        super(PacketListenerPriority.LOWEST);
        this.rotation = rotation;
        this.antiVanish = antiVanish;
        this.messages = messages;
        this.main = main;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        PacketTypeCommon t = event.getPacketType();
        if (t == PacketType.Play.Client.TAB_COMPLETE) {
            WrapperPlayClientTabComplete w = new WrapperPlayClientTabComplete(event);
            UUID id = event.getUser() != null ? event.getUser().getUUID() : null;
            if (id != null && antiVanish.inspect(id, w.getTransactionId().orElse(-1), w.getText())) main.execute(() -> alertAntiVanish(id));
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

    private void alertAntiVanish(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        if (p != null) messages.notifyStaff(messages.msg("anti-vanish-suspect", "player", p.getName()));
    }
}
