package net.anubis.client.module.impl.render;

import java.util.Set;
import java.util.function.LongPredicate;
import net.anubis.client.esp.BlockLayer;
import net.anubis.client.esp.ChunkScanner;
import net.anubis.client.esp.EspStyle;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BlockColorListSetting;
import net.anubis.client.module.setting.BooleanSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2248;
import net.minecraft.class_2680;
import net.minecraft.class_310;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/BlockEspModule.class */
@Environment(EnvType.CLIENT)
public final class BlockEspModule extends ClientModule implements ChunkScanner.Consumer {
    private final EspStyle style;
    private final BooleanSetting sTracers;
    private final BlockColorListSetting sBlockColors;
    private final BlockLayer layer;
    private Set<String> scannedIds;

    public BlockEspModule() {
        super("block-esp", "Block ESP", "Highlights chosen blocks; touching blocks merge into one shape.", ModuleCategory.RENDER);
        this.style = new EspStyle((x$0, x$1) -> {
            return this.addSetting(x$0, x$1);
        }, EspStyle.OUTLINE, 0.2f, 2048.0f);
        this.sTracers = (BooleanSetting) addSetting("tracers", new BooleanSetting("Tracers", false));
        this.sBlockColors = (BlockColorListSetting) addSetting(BlockColorListSetting.createDefault());
        this.layer = new BlockLayer(new BlockLayer.Palette() { // from class: net.anubis.client.module.impl.render.BlockEspModule.1
            @Override // net.anubis.client.esp.BlockLayer.Palette
            public int color(Object kind) {
                return BlockEspModule.this.sBlockColors.colorArgb((class_2248) kind);
            }

            @Override // net.anubis.client.esp.BlockLayer.Palette
            public int tracerColor(Object kind) {
                return BlockEspModule.this.sBlockColors.tracerColorArgb((class_2248) kind);
            }
        }, () -> {
            if (class_310.method_1551().field_1687 == null) {
                return null;
            }
            return class_310.method_1551().field_1687.method_27983();
        });
        this.scannedIds = Set.of();
        this.sBlockColors.onChange(this::rescan);
    }

    private void rescan() {
        if (isEnabled()) {
            Set<String> ids = this.sBlockColors.enabledIds();
            if (ids.equals(this.scannedIds)) {
                return;
            }
            boolean removed = !ids.containsAll(this.scannedIds);
            this.scannedIds = ids;
            if (removed) {
                this.layer.clear();
            }
            ChunkScanner.rescan();
        }
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        this.scannedIds = this.sBlockColors.enabledIds();
        ChunkScanner.register(this);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        ChunkScanner.unregister(this);
        this.layer.clear();
    }

    @Override // net.anubis.client.esp.ChunkScanner.Consumer
    public boolean wants(class_2680 state) {
        return this.sBlockColors.isEnabled(state.method_26204());
    }

    @Override // net.anubis.client.esp.ChunkScanner.Consumer
    public void found(long pos, class_2680 state) {
        this.layer.put(pos, state.method_26204());
    }

    @Override // net.anubis.client.esp.ChunkScanner.Consumer
    public void removed(long pos) {
        this.layer.remove(pos);
    }

    @Override // net.anubis.client.esp.ChunkScanner.Consumer
    public void reset() {
        this.layer.clear();
    }

    @Override // net.anubis.client.esp.ChunkScanner.Consumer
    public void chunkScanned(long chunkKey, LongPredicate stillThere) {
        this.layer.retainChunk(chunkKey, stillThere);
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        this.layer.render(event, this.style);
        if (!this.sTracers.get() || !this.sBlockColors.hasAnyTracerEnabled()) {
            return;
        }
        this.layer.renderTracers(this.style);
    }
}
