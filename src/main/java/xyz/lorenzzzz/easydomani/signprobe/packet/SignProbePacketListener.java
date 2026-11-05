package xyz.lorenzzzz.easydomani.signprobe.packet;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateSign;
import java.util.UUID;
import xyz.lorenzzzz.easydomani.signprobe.service.SignProbeService;

public final class SignProbePacketListener extends PacketListenerAbstract {
    private final SignProbeService signProbe;

    public SignProbePacketListener(SignProbeService signProbe) {
        super(PacketListenerPriority.LOWEST);
        this.signProbe = signProbe;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.UPDATE_SIGN) return;
        UUID uuid = event.getUser() != null ? event.getUser().getUUID() : null;
        if (uuid == null || !signProbe.awaitsSignUpdate(uuid)) return;
        WrapperPlayClientUpdateSign w = new WrapperPlayClientUpdateSign(event);
        if (signProbe.acceptSignUpdate(uuid, w.getBlockPosition(), w.getTextLines())) event.setCancelled(true);
    }
}
