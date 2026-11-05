package xyz.lorenzzzz.easydomani.seed.packet;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerJoinGame;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRespawn;
import xyz.lorenzzzz.easydomani.seed.service.FakeSeedService;

public final class HashedSeedPacketListener extends PacketListenerAbstract {
    private final FakeSeedService seeds;

    public HashedSeedPacketListener(FakeSeedService seeds) {
        super(PacketListenerPriority.HIGH);
        this.seeds = seeds;
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (!seeds.isEnabled()) return;
        if (event.getPacketType() == PacketType.Play.Server.JOIN_GAME) {
            WrapperPlayServerJoinGame w = new WrapperPlayServerJoinGame(event);
            w.setHashedSeed(seeds.fakeSeed(w.getWorldName()));
            event.markForReEncode(true);
        } else if (event.getPacketType() == PacketType.Play.Server.RESPAWN) {
            WrapperPlayServerRespawn w = new WrapperPlayServerRespawn(event);
            w.setHashedSeed(seeds.fakeSeed(w.getWorldName().orElse("")));
            event.markForReEncode(true);
        }
    }
}
