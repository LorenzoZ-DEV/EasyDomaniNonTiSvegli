package xyz.lorenzzzz.easydomani.packspoof.packet;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientResourcePackStatus;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientResourcePackStatus;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.lorenzzzz.easydomani.detection.model.ModProfile;
import xyz.lorenzzzz.easydomani.detection.service.ClientIdentityReader;
import xyz.lorenzzzz.easydomani.detection.service.ModDetectionService;
import xyz.lorenzzzz.easydomani.message.service.MessageService;
import xyz.lorenzzzz.easydomani.packspoof.service.PackSpoofService;

public final class PackSpoofPacketListener extends PacketListenerAbstract implements Listener {
    private static final ModProfile BAN = profile(true);
    private static final ModProfile ALERT = profile(false);

    private final PackSpoofService spoof;
    private final ModDetectionService detection;
    private final MessageService messages;
    private final Executor main;

    public PackSpoofPacketListener(PackSpoofService spoof, ModDetectionService detection, MessageService messages, Executor main) {
        super(PacketListenerPriority.LOWEST);
        this.spoof = spoof;
        this.detection = detection;
        this.messages = messages;
        this.main = main;
    }

    private static ModProfile profile(boolean ban) {
        return new ModProfile("pack-spoof", "Meteor (pack spoof)", ban, List.of(), List.of(), List.of(), null, List.of(), false, false);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        boolean play = event.getPacketType() == PacketType.Play.Client.RESOURCE_PACK_STATUS;
        if (!play && event.getPacketType() != PacketType.Configuration.Client.RESOURCE_PACK_STATUS) return;
        User u = event.getUser();
        UUID uuid = u != null ? u.getUUID() : null;
        if (!spoof.settings().enabled() || uuid == null) return;
        UUID pack;
        String result;
        if (play) {
            WrapperPlayClientResourcePackStatus w = new WrapperPlayClientResourcePackStatus(event);
            pack = w.getPackId();
            result = w.getResult().name();
        } else {
            WrapperConfigClientResourcePackStatus w = new WrapperConfigClientResourcePackStatus(event);
            pack = w.getPackId();
            result = w.getResult().name();
        }
        if (result.equals("ACCEPTED")) spoof.accepted(uuid, pack);
        else if (result.equals("SUCCESSFULLY_LOADED") && spoof.loaded(uuid, pack)) main.execute(() -> flag(uuid));
    }

    @Override
    public void onUserDisconnect(UserDisconnectEvent event) {
        User u = event.getUser();
        if (u != null && u.getUUID() != null) spoof.forget(u.getUUID());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        flag(event.getPlayer().getUniqueId());
    }

    private void flag(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        if (p == null) return;
        long ms = spoof.takePending(uuid);
        if (ms < 0 || ClientIdentityReader.isLikelyBedrock(p)) return;
        messages.notifyStaff(messages.msg("pack-spoof", "player", p.getName(), "ms", String.valueOf(ms)));
        detection.report(p, spoof.settings().experimentalBan() ? BAN : ALERT, "pack-spoof", false);
    }
}
