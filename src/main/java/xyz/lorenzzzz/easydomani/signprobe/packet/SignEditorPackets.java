package xyz.lorenzzzz.easydomani.signprobe.packet;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.blockentity.BlockEntityTypes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockEntityData;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCloseWindow;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenSignEditor;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import xyz.lorenzzzz.easydomani.signprobe.model.ActiveSignProbe;
import xyz.lorenzzzz.easydomani.signprobe.service.SignProbeNbtWriter;

public final class SignEditorPackets {
    private final int signStateId;
    private final boolean modernSignFormat;

    private SignEditorPackets(int signStateId, boolean modernSignFormat) {
        this.signStateId = signStateId;
        this.modernSignFormat = modernSignFormat;
    }

    public static SignEditorPackets create() {
        return new SignEditorPackets(
                SpigotConversionUtil.fromBukkitBlockData(Material.OAK_SIGN.createBlockData()).getGlobalId(),
                PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5));
    }

    public User user(Player player) {
        return PacketEvents.getAPI().getPlayerManager().getUser(player);
    }

    public int stateId(Block block) {
        return SpigotConversionUtil.fromBukkitBlockData(block.getBlockData()).getGlobalId();
    }

    public void open(User user, ActiveSignProbe a) {
        user.sendPacket(new WrapperPlayServerBlockChange(a.pos(), signStateId));
        user.sendPacket(new WrapperPlayServerBlockEntityData(a.pos(), BlockEntityTypes.SIGN,
                SignProbeNbtWriter.buildSignNbt(a.defs(), a.sentinel(), modernSignFormat)));
        user.sendPacket(new WrapperPlayServerOpenSignEditor(a.pos(), true));
    }

    public void close(User user) {
        user.sendPacket(new WrapperPlayServerCloseWindow(0));
    }

    public void restore(Player player, ActiveSignProbe a) {
        if (!player.isOnline()) return;
        User user = user(player);
        if (user != null) user.sendPacket(new WrapperPlayServerBlockChange(a.pos(), a.originalState()));
    }
}
