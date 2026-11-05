package xyz.lorenzzzz.easydomani.honeypot.packet;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerMultiBlockChange;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUnloadChunk;
import java.util.UUID;
import xyz.lorenzzzz.easydomani.honeypot.service.HoneypotBaitService;

public final class HoneypotBlockUpdatePacketListener extends PacketListenerAbstract {
    private final HoneypotBaitService honeypot;

    public HoneypotBlockUpdatePacketListener(HoneypotBaitService honeypot) {
        super(PacketListenerPriority.MONITOR);
        this.honeypot = honeypot;
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {

        if (!honeypot.hasAnyBait() || !(event.getPacketType() instanceof PacketType.Play.Server type)) return;
        switch (type) {
            case BLOCK_CHANGE, MULTI_BLOCK_CHANGE, CHUNK_DATA, UNLOAD_CHUNK, RESPAWN -> { }
            default -> { return; }
        }
        UUID uuid = event.getUser() != null ? event.getUser().getUUID() : null;
        if (uuid == null || !honeypot.hasBaits(uuid)) return;
        switch (type) {
            case BLOCK_CHANGE -> {
                WrapperPlayServerBlockChange w = new WrapperPlayServerBlockChange(event);
                Vector3i p = w.getBlockPosition();
                honeypot.onBlockChange(uuid, p.x, p.y, p.z, w.getBlockId());
            }
            case MULTI_BLOCK_CHANGE -> {
                for (WrapperPlayServerMultiBlockChange.EncodedBlock e : new WrapperPlayServerMultiBlockChange(event).getBlocks()) {
                    honeypot.forgetBait(uuid, e.getX(), e.getY(), e.getZ());
                }
            }
            case CHUNK_DATA -> {

                Object buf = event.getByteBuf();
                int start = ByteBufHelper.readerIndex(buf);
                int cx = ByteBufHelper.readInt(buf);
                int cz = ByteBufHelper.readInt(buf);
                ByteBufHelper.readerIndex(buf, start);
                honeypot.forgetChunk(uuid, cx, cz);
            }
            case UNLOAD_CHUNK -> {
                WrapperPlayServerUnloadChunk w = new WrapperPlayServerUnloadChunk(event);
                honeypot.forgetChunk(uuid, w.getChunkX(), w.getChunkZ());
            }
            default -> honeypot.forgetViewerBaits(uuid);
        }
    }
}
