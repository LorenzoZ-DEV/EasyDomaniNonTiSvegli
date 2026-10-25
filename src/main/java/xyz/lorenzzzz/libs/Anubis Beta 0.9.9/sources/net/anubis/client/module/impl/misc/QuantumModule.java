package net.anubis.client.module.impl.misc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.anubis.client.chat.ModuleChat;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.BlockUpdateEvent;
import net.anubis.client.event.impl.PacketAppliedEvent;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.util.LevelUtil;
import net.anubis.client.util.quantum.ChunkSnapshotStore;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_1923;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2672;
import net.minecraft.class_2680;
import net.minecraft.class_2818;
import net.minecraft.class_2826;
import net.minecraft.class_2841;
import net.minecraft.class_310;
import net.minecraft.class_638;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/QuantumModule.class */
@Environment(EnvType.CLIENT)
public final class QuantumModule extends ClientModule {
    private static final int SCAN_RADIUS = 8;
    private static final int FLAG_RADIUS = 9;
    private static final double MARKER_Y = 63.0d;
    private static final float LINE_WIDTH = 1.5f;
    private static final Set<class_2248> IGNORED_NEW_BLOCKS = Set.of((Object[]) new class_2248[]{class_2246.field_10036, class_2246.field_10110, class_2246.field_10418, class_2246.field_29219, class_2246.field_10212, class_2246.field_29027, class_2246.field_27120, class_2246.field_29221, class_2246.field_10571, class_2246.field_29026, class_2246.field_10080, class_2246.field_29030, class_2246.field_10013, class_2246.field_29220, class_2246.field_10090, class_2246.field_29028, class_2246.field_10442, class_2246.field_29029, class_2246.field_10503, class_2246.field_10098, class_2246.field_10539, class_2246.field_10335, class_2246.field_10035, class_2246.field_54714, class_2246.field_42731, class_2246.field_28673, class_2246.field_28674, class_2246.field_9989, class_2246.field_33509, class_2246.field_10340, class_2246.field_10282, class_2246.field_10450, class_2246.field_10377, class_2246.field_10523, class_2246.field_10524, class_2246.field_10091, class_2246.field_10228});
    private static final Set<class_2248> IGNORED_OLD_BLOCKS = Set.of(class_2246.field_27159, class_2246.field_10340, class_2246.field_10110);
    private final BooleanSetting advancedDebug;
    private final BooleanSetting render;
    private final ColorSetting fillColor;
    private final ColorSetting lineColor;
    private final ChunkSnapshotStore snapshots;
    private final Set<class_1923> justSent;
    private final List<class_2338> changedBlocks;
    private Set<class_1923> previousRange;
    private class_1923 lastPos;
    private class_638 sessionLevel;

    public QuantumModule() {
        super("quantum", "Quantum", "Finds player activity in resent chunks.", ModuleCategory.BASE_RAID);
        this.advancedDebug = (BooleanSetting) addSetting("advanced-debug", new BooleanSetting("Advanced Debug", false));
        this.render = (BooleanSetting) addSetting("render", new BooleanSetting("Render", true));
        this.fillColor = (ColorSetting) addSetting("side-color", new ColorSetting("Fill Color", 687810600));
        this.lineColor = (ColorSetting) addSetting("line-color", new ColorSetting("Line Color", -55256));
        this.snapshots = new ChunkSnapshotStore(class_2246.field_10124.method_9564());
        this.justSent = new HashSet();
        this.changedBlocks = new ArrayList();
        this.previousRange = Set.of();
        usesNotifications().set(false);
        this.fillColor.visibleWhen(() -> {
            return this.render.get() && this.advancedDebug.get();
        });
        this.lineColor.visibleWhen(() -> {
            return this.render.get() && this.advancedDebug.get();
        });
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        this.sessionLevel = null;
        resetSession();
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        this.snapshots.clear();
    }

    private void resetSession() {
        this.snapshots.clear();
        this.justSent.clear();
        this.changedBlocks.clear();
        this.previousRange = Set.of();
        this.lastPos = null;
    }

    private void ensureSession(class_638 level) {
        if (level == this.sessionLevel) {
            return;
        }
        this.sessionLevel = level;
        resetSession();
    }

    @EventHandler(stage = Stage.POST)
    public void onTick(TickEvent event) {
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return;
        }
        ensureSession(mc.field_1687);
        if (LevelUtil.isOverworld(mc.field_1687)) {
            class_1923 current = mc.field_1724.method_31476();
            if (this.lastPos != null && !this.lastPos.equals(current)) {
                Set<class_1923> range = chunksInRange(current);
                for (class_1923 pos : range) {
                    if (!this.previousRange.contains(pos)) {
                        this.snapshots.store(pos.field_9181, pos.field_9180, extractSections(mc.field_1687.method_8497(pos.field_9181, pos.field_9180)));
                    }
                }
                this.justSent.removeIf(pos2 -> {
                    return !range.contains(pos2);
                });
                Set<Long> keys = new HashSet<>();
                for (class_1923 pos3 : range) {
                    keys.add(Long.valueOf(ChunkSnapshotStore.chunkKey(pos3.field_9181, pos3.field_9180)));
                }
                ChunkSnapshotStore chunkSnapshotStore = this.snapshots;
                Objects.requireNonNull(keys);
                chunkSnapshotStore.retain((v1) -> {
                    return r1.contains(v1);
                });
                this.previousRange = range;
            }
            this.lastPos = current;
        }
    }

    @EventHandler
    public void onPacketApplied(PacketAppliedEvent event) {
        class_2672 class_2672Var = event.packet;
        if (class_2672Var instanceof class_2672) {
            class_2672 packet = class_2672Var;
            class_310 mc = class_310.method_1551();
            class_638 level = mc.field_1687;
            if (mc.field_1724 == null || level == null) {
                return;
            }
            ensureSession(level);
            if (LevelUtil.isOverworld(level)) {
                class_1923 pos = new class_1923(packet.method_11523(), packet.method_11524());
                if (chunksInRange(mc.field_1724.method_31476()).contains(pos) && LevelUtil.isLoaded(level, pos.field_9181, pos.field_9180)) {
                    class_2841<class_2680>[] current = extractSections(level.method_8497(pos.field_9181, pos.field_9180));
                    List<ChunkSnapshotStore.BlockChange> changes = this.snapshots.compare(pos.field_9181, pos.field_9180, current, level.method_32891());
                    for (ChunkSnapshotStore.BlockChange change : changes) {
                        class_2248 newBlock = change.newState().method_26204();
                        class_2248 oldBlock = change.oldState().method_26204();
                        if (!IGNORED_NEW_BLOCKS.contains(newBlock) && !IGNORED_OLD_BLOCKS.contains(oldBlock)) {
                            onActivity(pos, change, newBlock, oldBlock);
                            break;
                        }
                    }
                    this.justSent.add(pos);
                    this.snapshots.store(pos.field_9181, pos.field_9180, current);
                }
            }
        }
    }

    @EventHandler
    public void onBlockUpdate(BlockUpdateEvent event) {
        class_638 level = class_310.method_1551().field_1687;
        if (level == null || !LevelUtil.isOverworld(level)) {
            return;
        }
        class_2338 pos = event.pos;
        int sectionIndex = (pos.method_10264() >> 4) - level.method_32891();
        this.snapshots.update(pos.method_10263() >> 4, pos.method_10260() >> 4, sectionIndex, pos.method_10263() & 15, pos.method_10264() & 15, pos.method_10260() & 15, event.newState);
    }

    private void onActivity(class_1923 pos, ChunkSnapshotStore.BlockChange change, class_2248 newBlock, class_2248 oldBlock) {
        ModuleChat.Body notice = ModuleChat.body().text("Activity ").good("detected").text(" in chunk ").value(pos.field_9181 + ", " + pos.field_9180);
        if (this.advancedDebug.get()) {
            notice.text(": ").name(oldBlock.method_9518().getString()).text(" changed to ").name(newBlock.method_9518().getString());
        }
        ModuleChat.send(this, notice);
        this.changedBlocks.add(new class_2338(change.worldX(), change.worldY(), change.worldZ()));
        RenderUtilModule renderUtil = (RenderUtilModule) ModuleRegistry.get(RenderUtilModule.class);
        if (renderUtil == null) {
            return;
        }
        renderUtil.addSourceChunk(pos);
        List<class_1923> area = new ArrayList<>();
        for (int dx = -9; dx <= 9; dx++) {
            for (int dz = -9; dz <= 9; dz++) {
                area.add(new class_1923(pos.field_9181 + dx, pos.field_9180 + dz));
            }
        }
        if (!renderUtil.hasFlaggedChunks()) {
            renderUtil.addFlaggedChunks(area);
            return;
        }
        List<class_1923> overlap = new ArrayList<>();
        for (class_1923 candidate : area) {
            if (renderUtil.isFlagged(candidate)) {
                overlap.add(candidate);
            }
        }
        renderUtil.clearFlaggedChunks();
        renderUtil.addFlaggedChunks(overlap.isEmpty() ? area : overlap);
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        class_310 mc = class_310.method_1551();
        if (this.render.get() && this.advancedDebug.get() && mc.field_1724 != null && mc.field_1687 != null && LevelUtil.isOverworld(mc.field_1687)) {
            class_12179 blockStyle = class_12179.method_75537(this.lineColor.argb(), LINE_WIDTH, this.fillColor.argb());
            Iterator<class_2338> it = this.changedBlocks.iterator();
            while (it.hasNext()) {
                class_12180.method_75541(new class_238(it.next()), blockStyle).method_75533();
            }
            class_12179 sentStyle = class_12179.method_75537(-16711936, LINE_WIDTH, 671153920);
            for (class_1923 pos : chunksInRange(mc.field_1724.method_31476())) {
                if (this.justSent.contains(pos)) {
                    double x = ((double) pos.field_9181) * 16.0d;
                    double z = ((double) pos.field_9180) * 16.0d;
                    class_12180.method_75541(new class_238(x, 63.0d, z, x + 16.0d, 63.0d, z + 16.0d), sentStyle).method_75533();
                }
            }
        }
    }

    static Set<class_1923> chunksInRange(class_1923 center) {
        Set<class_1923> range = new LinkedHashSet<>();
        for (int dz = -8; dz <= 8; dz++) {
            int cut = Math.max(0, 3 - (8 - Math.abs(dz)));
            int halfWidth = 8 - cut;
            for (int dx = -halfWidth; dx <= halfWidth; dx++) {
                range.add(new class_1923(center.field_9181 + dx, center.field_9180 + dz));
            }
        }
        return range;
    }

    private static class_2841<class_2680>[] extractSections(class_2818 chunk) {
        class_2826[] sections = chunk.method_12006();
        class_2841<class_2680>[] out = new class_2841[sections.length];
        for (int index = 6; index <= 23 && index < sections.length; index++) {
            class_2826 section = sections[index];
            if (section != null && !section.method_38292()) {
                out[index] = section.method_12265().method_39957();
            }
        }
        return out;
    }
}
