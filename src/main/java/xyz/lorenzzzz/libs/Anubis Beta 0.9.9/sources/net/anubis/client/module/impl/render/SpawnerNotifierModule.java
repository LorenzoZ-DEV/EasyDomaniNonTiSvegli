package net.anubis.client.module.impl.render;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.function.LongPredicate;
import net.anubis.client.AnubisClientMod;
import net.anubis.client.chat.ModuleChat;
import net.anubis.client.esp.BlockLayer;
import net.anubis.client.esp.ChunkScanner;
import net.anubis.client.esp.EspStyle;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1109;
import net.minecraft.class_1297;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_2636;
import net.minecraft.class_2680;
import net.minecraft.class_310;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/SpawnerNotifierModule.class */
@Environment(EnvType.CLIENT)
public final class SpawnerNotifierModule extends ClientModule implements ChunkScanner.Consumer {
    private static final String SPAWNER = "Spawner";
    private final BooleanSetting sSound;
    private final BooleanSetting sRender;
    private final ColorSetting sColor;
    private final EspStyle style;
    private final BlockLayer layer;
    private final LongOpenHashSet notified;

    public SpawnerNotifierModule() {
        super("spawner-notifier", "Spawner Notifier", "Scans chunks for mob spawners and highlights them.", ModuleCategory.DONUT);
        this.sSound = (BooleanSetting) addSetting("sound", new BooleanSetting("Sound", true));
        this.sRender = (BooleanSetting) addSetting("render", new BooleanSetting("Render", true));
        this.sColor = (ColorSetting) addSetting("color", new ColorSetting("Color", -587246251));
        this.style = new EspStyle((x$0, x$1) -> {
            return this.addSetting(x$0, x$1);
        }, EspStyle.OUTLINE, 0.1f, 256.0f);
        this.layer = new BlockLayer(new BlockLayer.Palette() { // from class: net.anubis.client.module.impl.render.SpawnerNotifierModule.1
            @Override // net.anubis.client.esp.BlockLayer.Palette
            public int color(Object kind) {
                return SpawnerNotifierModule.this.sColor.argb();
            }

            @Override // net.anubis.client.esp.BlockLayer.Palette
            public int tracerColor(Object kind) {
                return 0;
            }
        }, () -> {
            if (class_310.method_1551().field_1687 == null) {
                return null;
            }
            return class_310.method_1551().field_1687.method_27983();
        });
        this.notified = new LongOpenHashSet();
        usesNotifications();
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        ChunkScanner.register(this);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        ChunkScanner.unregister(this);
        this.layer.clear();
        this.notified.clear();
    }

    @Override // net.anubis.client.esp.ChunkScanner.Consumer
    public boolean wants(class_2680 state) {
        return state.method_27852(class_2246.field_10260);
    }

    @Override // net.anubis.client.esp.ChunkScanner.Consumer
    public void found(long pos, class_2680 state) {
        this.layer.put(pos, SPAWNER);
        if (this.notified.add(pos)) {
            notify(class_2338.method_10092(pos));
        }
    }

    @Override // net.anubis.client.esp.ChunkScanner.Consumer
    public void removed(long pos) {
        this.layer.remove(pos);
    }

    @Override // net.anubis.client.esp.ChunkScanner.Consumer
    public void reset() {
        this.layer.clear();
        this.notified.clear();
    }

    @Override // net.anubis.client.esp.ChunkScanner.Consumer
    public void chunkScanned(long chunkKey, LongPredicate stillThere) {
        this.layer.retainChunk(chunkKey, stillThere);
    }

    private void notify(class_2338 pos) {
        class_310 mc = class_310.method_1551();
        if (notificationsOn()) {
            ModuleChat.send(this, foundMessage(mc, pos));
        }
        if (this.sSound.get() && mc.field_1724 != null && mc.field_1687 != null) {
            mc.method_1483().method_4873(class_1109.method_4757(AnubisClientMod.SPAWNER_NOTIFY_SOUND, 1.0f, 1.0f));
        }
    }

    private static ModuleChat.Body foundMessage(class_310 mc, class_2338 pos) {
        String mob = entityName(mc, pos);
        ModuleChat.Body body = mob == null ? ModuleChat.body().warn("Unknown") : ModuleChat.body().name(mob);
        body.text(" spawner ").good("found").text(" at ").coords(pos.method_10263(), pos.method_10264(), pos.method_10260());
        if (mc.field_1724 != null) {
            long distance = Math.round(mc.field_1724.method_73189().method_1022(class_243.method_24953(pos)));
            body.text(" (").value(distance + "m").text(")");
        }
        return body;
    }

    private static String entityName(class_310 mc, class_2338 pos) {
        if (mc.field_1687 == null) {
            return null;
        }
        class_2636 class_2636VarMethod_8321 = mc.field_1687.method_8321(pos);
        if (!(class_2636VarMethod_8321 instanceof class_2636)) {
            return null;
        }
        class_2636 spawner = class_2636VarMethod_8321;
        class_1297 entity = spawner.method_11390().method_8283(mc.field_1687, pos);
        if (entity == null) {
            return null;
        }
        return entity.method_5864().method_5897().getString();
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        if (this.sRender.get()) {
            this.layer.render(event, this.style);
        }
    }
}
