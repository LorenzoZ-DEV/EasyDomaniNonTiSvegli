package net.anubis.client.module.impl.render;

import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.anubis.client.esp.BlockLayer;
import net.anubis.client.esp.EspStyle;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.PacketReceiveNettyEvent;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BlockColorListSetting;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.util.BlockEntityLeakTracker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2626;
import net.minecraft.class_2680;
import net.minecraft.class_310;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/BlockEntityDebugModule.class */
@Environment(EnvType.CLIENT)
public final class BlockEntityDebugModule extends ClientModule {
    private static final int SETTINGS_VERSION = 1;
    private static final float FILL_ALPHA = 0.8f;
    private static final Set<String> NEW_IN_TABLE = Set.of("minecraft:jukebox", "minecraft:piston", "minecraft:sticky_piston");
    private final ModeSetting sDetection;
    private final BooleanSetting sBlockFilter;
    private final EspStyle style;
    private final BooleanSetting sTracers;
    private final BooleanSetting sCustomTracerColor;
    private final ColorSetting sTracerColor;
    private final BlockColorListSetting sBlockColors;
    private final BlockLayer layer;
    private final LongOpenHashSet spawners;
    private int seenGeneration;
    private Set<String> scannedIds;

    public BlockEntityDebugModule() {
        super("block-entity-debug", "Block Entity Debug", "Renders every leaked block entity as ESP (always-on Netty capture).", ModuleCategory.BASE_RAID);
        this.sDetection = (ModeSetting) addSetting("detection", new ModeSetting("Detection", "All", "All", "Below Zero", "Below -20", "Below -30"));
        this.sBlockFilter = (BooleanSetting) addSetting("block-filter", new BooleanSetting("Block Filter", false));
        this.style = new EspStyle((x$0, x$1) -> {
            return this.addSetting(x$0, x$1);
        }, EspStyle.BOXES, FILL_ALPHA, 256.0f);
        this.sTracers = (BooleanSetting) addSetting("tracers", new BooleanSetting("Tracers", true));
        this.sCustomTracerColor = (BooleanSetting) addSetting("custom-tracer-color", new BooleanSetting("Custom Tracer Color", false));
        this.sTracerColor = (ColorSetting) addSetting("tracer-color", new ColorSetting("Tracer Color", -1));
        this.sBlockColors = (BlockColorListSetting) addSetting(BlockColorListSetting.createForBlockEntityDebug());
        this.layer = new BlockLayer(new BlockLayer.Palette() { // from class: net.anubis.client.module.impl.render.BlockEntityDebugModule.1
            @Override // net.anubis.client.esp.BlockLayer.Palette
            public int color(Object kind) {
                return BlockEntityDebugModule.this.sBlockColors.colorArgb((class_2248) kind);
            }

            @Override // net.anubis.client.esp.BlockLayer.Palette
            public int tracerColor(Object kind) {
                return BlockEntityDebugModule.this.tracerColorOf((class_2248) kind);
            }
        }, () -> {
            if (class_310.method_1551().field_1687 == null) {
                return null;
            }
            return class_310.method_1551().field_1687.method_27983();
        });
        this.spawners = new LongOpenHashSet();
        this.seenGeneration = -1;
        this.scannedIds = Set.of();
        BooleanSetting booleanSetting = this.sCustomTracerColor;
        BooleanSetting booleanSetting2 = this.sTracers;
        Objects.requireNonNull(booleanSetting2);
        booleanSetting.visibleWhen(booleanSetting2::get);
        this.sTracerColor.visibleWhen(() -> {
            return this.sTracers.get() && this.sCustomTracerColor.get();
        });
        this.sDetection.onChange(this::resync);
        this.sBlockFilter.onChange(this::resync);
        this.sBlockColors.onChange(this::onBlockColorsChanged);
    }

    @Override // net.anubis.client.module.ClientModule
    public int settingsVersion() {
        return 1;
    }

    @Override // net.anubis.client.module.ClientModule
    public void migrateSettings(int savedVersion) {
        this.style.shape.set(EspStyle.BOXES);
        this.style.boxSize.set(1.0f);
        this.style.fillAlpha.set(FILL_ALPHA);
        this.sBlockColors.resetColors();
        int plain = BlockColorListSetting.rgbaToArgb(this.sBlockColors.defaultColor());
        boolean filtered = this.sBlockFilter.get();
        for (BlockColorListSetting.BlockEntry entry : this.sBlockColors.entries()) {
            if (BlockColorListSetting.rgbaToArgb(entry.color()) != plain && (!filtered || NEW_IN_TABLE.contains(entry.id()))) {
                this.sBlockColors.select(entry);
            }
        }
    }

    private int tracerColorOf(class_2248 block) {
        int own = this.sBlockColors.isEnabled(block) ? this.sBlockColors.tracerColorArgb(block) : this.sBlockColors.colorArgb(block);
        if (own == 0) {
            return 0;
        }
        return this.sCustomTracerColor.get() ? this.sTracerColor.argb() : own;
    }

    private void onBlockColorsChanged() {
        if (!isEnabled() || !this.sBlockFilter.get() || this.sBlockColors.enabledIds().equals(this.scannedIds)) {
            return;
        }
        resync();
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        BlockEntityLeakTracker.recordAdditions(true);
        resync();
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        BlockEntityLeakTracker.recordAdditions(false);
        this.layer.clear();
        this.spawners.clear();
    }

    private void resync() {
        if (isEnabled()) {
            this.layer.clear();
            this.spawners.clear();
            this.scannedIds = this.sBlockColors.enabledIds();
            this.seenGeneration = BlockEntityLeakTracker.generation();
            while (BlockEntityLeakTracker.pollAdded() != null) {
            }
            for (Map.Entry<class_2338, BlockEntityLeakTracker.Entry> entry : BlockEntityLeakTracker.view().entrySet()) {
                accept(entry.getKey(), entry.getValue().block());
            }
        }
    }

    @EventHandler
    public void onTick(TickEvent event) {
        if (BlockEntityLeakTracker.generation() != this.seenGeneration) {
            resync();
            return;
        }
        while (true) {
            class_2338 pos = BlockEntityLeakTracker.pollAdded();
            if (pos != null) {
                BlockEntityLeakTracker.Entry entry = BlockEntityLeakTracker.view().get(pos);
                if (entry != null) {
                    accept(pos, entry.block());
                }
            } else {
                return;
            }
        }
    }

    private void accept(class_2338 pos, class_2248 block) {
        if (passesDetection(pos)) {
            if (!this.sBlockFilter.get() || block == class_2246.field_10124 || this.sBlockColors.isEnabled(block)) {
                this.layer.put(pos.method_10063(), block);
                if (block == class_2246.field_10260) {
                    this.spawners.add(pos.method_10063());
                }
            }
        }
    }

    private boolean passesDetection(class_2338 pos) {
        switch (this.sDetection.get()) {
            case "Below Zero":
                return pos.method_10264() < 0;
            case "Below -20":
                return pos.method_10264() < -20;
            case "Below -30":
                return pos.method_10264() < -30;
            default:
                return true;
        }
    }

    @EventHandler
    public void onPacketReceiveNetty(PacketReceiveNettyEvent event) {
        class_2626 class_2626Var = event.packet;
        if (class_2626Var instanceof class_2626) {
            class_2626 packet = class_2626Var;
            if (BlockEntityLeakTracker.contains(packet.method_11309())) {
                class_2680 state = packet.method_11308();
                if (state.method_26215() || state.method_27852(class_2246.field_10340) || state.method_27852(class_2246.field_28888)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        this.layer.render(event, this.style);
        if (this.sTracers.get()) {
            this.layer.renderTracers(this.style);
        }
        if (this.spawners.isEmpty()) {
            return;
        }
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null) {
            return;
        }
        class_243 eye = mc.field_1724.method_5836(event.partialTick);
        double rangeSq = ((double) this.style.range.get()) * ((double) this.style.range.get());
        int color = this.sBlockColors.colorArgb(class_2246.field_10260);
        class_12179 beam = class_12179.method_75536(color, this.style.lineWidth.get());
        LongIterator it = this.spawners.iterator();
        while (it.hasNext()) {
            long packed = ((Long) it.next()).longValue();
            class_2338 pos = class_2338.method_10092(packed);
            if (eye.method_1028(((double) pos.method_10263()) + 0.5d, ((double) pos.method_10264()) + 1.0d, ((double) pos.method_10260()) + 0.5d) <= rangeSq) {
                class_12180.method_75541(new class_238(((double) pos.method_10263()) + 0.3d, ((double) pos.method_10264()) + 1.0d, ((double) pos.method_10260()) + 0.3d, ((double) pos.method_10263()) + 0.7d, ((double) pos.method_10264()) + 257.0d, ((double) pos.method_10260()) + 0.7d), beam).method_75533();
            }
        }
    }
}
