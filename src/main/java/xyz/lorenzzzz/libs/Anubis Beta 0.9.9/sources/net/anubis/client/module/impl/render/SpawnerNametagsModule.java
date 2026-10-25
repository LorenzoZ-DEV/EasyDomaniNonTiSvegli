package net.anubis.client.module.impl.render;

import imgui.ImDrawList;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.LongPredicate;
import net.anubis.client.esp.ChunkScanner;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.gui.GuiPalette;
import net.anubis.client.gui.WorldLabel;
import net.anubis.client.gui.WorldText;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.HudOverlayProvider;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_12180;
import net.minecraft.class_1299;
import net.minecraft.class_1923;
import net.minecraft.class_1952;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_2636;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_638;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/SpawnerNametagsModule.class */
@Environment(EnvType.CLIENT)
public final class SpawnerNametagsModule extends ClientModule implements HudOverlayProvider, ChunkScanner.Consumer {
    private static final int MAX_LABELS = 48;
    private static final int RING_SEGMENTS = 64;
    private static final float RING_WIDTH = 1.5f;
    private static final float RING_ALPHA_INSIDE = 0.8f;
    private static final float RING_ALPHA_OUTSIDE = 0.4f;
    private static final double LABEL_HEIGHT = 1.2d;
    private static final double FADE_SHARE = 0.125d;
    private static final String SEPARATOR = "  ·  ";
    private final NumberSetting sRange;
    private final BooleanSetting sRadius;
    private final LongOpenHashSet spawners;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/SpawnerNametagsModule$Shown.class */
    @Environment(EnvType.CLIENT)
    private static final class Shown extends Record {
        private final class_2338 pos;
        private final double distance;
        private final String mob;
        private final int range;
        private final boolean inside;

        private Shown(class_2338 pos, double distance, String mob, int range, boolean inside) {
            this.pos = pos;
            this.distance = distance;
            this.mob = mob;
            this.range = range;
            this.inside = inside;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Shown.class), Shown.class, "pos;distance;mob;range;inside", "FIELD:Lnet/anubis/client/module/impl/render/SpawnerNametagsModule$Shown;->pos:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/render/SpawnerNametagsModule$Shown;->distance:D", "FIELD:Lnet/anubis/client/module/impl/render/SpawnerNametagsModule$Shown;->mob:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/render/SpawnerNametagsModule$Shown;->range:I", "FIELD:Lnet/anubis/client/module/impl/render/SpawnerNametagsModule$Shown;->inside:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Shown.class), Shown.class, "pos;distance;mob;range;inside", "FIELD:Lnet/anubis/client/module/impl/render/SpawnerNametagsModule$Shown;->pos:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/render/SpawnerNametagsModule$Shown;->distance:D", "FIELD:Lnet/anubis/client/module/impl/render/SpawnerNametagsModule$Shown;->mob:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/render/SpawnerNametagsModule$Shown;->range:I", "FIELD:Lnet/anubis/client/module/impl/render/SpawnerNametagsModule$Shown;->inside:Z").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Shown.class, Object.class), Shown.class, "pos;distance;mob;range;inside", "FIELD:Lnet/anubis/client/module/impl/render/SpawnerNametagsModule$Shown;->pos:Lnet/minecraft/class_2338;", "FIELD:Lnet/anubis/client/module/impl/render/SpawnerNametagsModule$Shown;->distance:D", "FIELD:Lnet/anubis/client/module/impl/render/SpawnerNametagsModule$Shown;->mob:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/render/SpawnerNametagsModule$Shown;->range:I", "FIELD:Lnet/anubis/client/module/impl/render/SpawnerNametagsModule$Shown;->inside:Z").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_2338 pos() {
            return this.pos;
        }

        public double distance() {
            return this.distance;
        }

        public String mob() {
            return this.mob;
        }

        public int range() {
            return this.range;
        }

        public boolean inside() {
            return this.inside;
        }
    }

    public SpawnerNametagsModule() {
        super("spawner-nametags", "Spawner Nametags", "Shows each spawner's mob and activation range over the block.", ModuleCategory.RENDER);
        this.sRange = (NumberSetting) addSetting("range", new NumberSetting("Range", 64.0f, 8.0f, 256.0f).step(1.0f));
        this.sRadius = (BooleanSetting) addSetting("activation-radius", new BooleanSetting("Activation Radius", true));
        this.spawners = new LongOpenHashSet();
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        ChunkScanner.register(this);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        ChunkScanner.unregister(this);
        this.spawners.clear();
    }

    public static String mobName(class_2636 spawner) {
        class_1952 data = spawner.method_11390().anubis$spawnerNametagsNextSpawnData();
        if (data == null) {
            return null;
        }
        String id = data.comp_64().method_68564("id", "");
        if (id.isEmpty()) {
            return null;
        }
        return (String) class_1299.method_5898(id).map(type -> {
            return type.method_5897().getString();
        }).orElse(null);
    }

    public static int activationRange(class_2636 spawner) {
        return spawner.method_11390().anubis$spawnerNametagsRequiredPlayerRange();
    }

    @Override // net.anubis.client.esp.ChunkScanner.Consumer
    public boolean wants(class_2680 state) {
        return state.method_27852(class_2246.field_10260);
    }

    @Override // net.anubis.client.esp.ChunkScanner.Consumer
    public void found(long pos, class_2680 state) {
        this.spawners.add(pos);
    }

    @Override // net.anubis.client.esp.ChunkScanner.Consumer
    public void removed(long pos) {
        this.spawners.remove(pos);
    }

    @Override // net.anubis.client.esp.ChunkScanner.Consumer
    public void reset() {
        this.spawners.clear();
    }

    @Override // net.anubis.client.esp.ChunkScanner.Consumer
    public void chunkScanned(long chunkKey, LongPredicate stillThere) {
        LongIterator it = this.spawners.iterator();
        while (it.hasNext()) {
            long pos = it.nextLong();
            if (class_1923.method_8331(class_2338.method_10061(pos) >> 4, class_2338.method_10083(pos) >> 4) == chunkKey && !stillThere.test(pos)) {
                it.remove();
            }
        }
    }

    private List<Shown> inRange(class_310 mc) {
        List<Shown> shown = new ArrayList<>();
        if (this.spawners.isEmpty() || mc.field_1687 == null || mc.field_1724 == null) {
            return shown;
        }
        class_638 class_638Var = mc.field_1687;
        class_243 player = mc.field_1724.method_73189();
        double range = this.sRange.get();
        LongIterator it = this.spawners.iterator();
        while (it.hasNext()) {
            class_2338 pos = class_2338.method_10092(it.nextLong());
            double distanceSq = player.method_1028(((double) pos.method_10263()) + 0.5d, ((double) pos.method_10264()) + 0.5d, ((double) pos.method_10260()) + 0.5d);
            if (distanceSq <= range * range) {
                class_2636 class_2636VarMethod_8321 = class_638Var.method_8321(pos);
                if (class_2636VarMethod_8321 instanceof class_2636) {
                    class_2636 spawner = class_2636VarMethod_8321;
                    int activation = activationRange(spawner);
                    boolean inside = activation < 0 || distanceSq < ((double) activation) * ((double) activation);
                    shown.add(new Shown(pos, Math.sqrt(distanceSq), mobName(spawner), activation, inside));
                }
            }
        }
        shown.sort(Comparator.comparingDouble((v0) -> {
            return v0.distance();
        }));
        return shown.size() > 48 ? shown.subList(0, 48) : shown;
    }

    private float fade(double distance) {
        double range = this.sRange.get();
        return (float) Math.max(0.0d, Math.min(1.0d, (range - distance) / (range * FADE_SHARE)));
    }

    @Override // net.anubis.client.module.HudOverlayProvider
    public boolean isHudOverlayEnabled() {
        return isEnabled() && !this.spawners.isEmpty();
    }

    @Override // net.anubis.client.module.HudOverlayProvider
    public void renderHudOverlay(ImDrawList dl, float screenW, float screenH) {
        String[] runs;
        int[] colors;
        class_310 mc = class_310.method_1551();
        if (mc.field_1690.field_1842) {
            return;
        }
        List<Shown> shown = inRange(mc);
        int accent = WorldText.hudAccent();
        for (int i = shown.size() - 1; i >= 0; i--) {
            Shown spawner = shown.get(i);
            float[] screen = labelAnchor(mc, spawner.pos(), screenW, screenH);
            if (screen != null) {
                String name = spawner.mob() == null ? "Spawner" : spawner.mob() + " Spawner";
                String distance = Math.round(spawner.distance()) + "m";
                if (this.sRadius.get() && spawner.range() > 0) {
                    runs = new String[]{distance, SEPARATOR, spawner.range() + "m range"};
                    int[] iArr = new int[3];
                    iArr[0] = WorldText.LABEL;
                    iArr[1] = WorldText.LABEL;
                    iArr[2] = spawner.inside() ? accent : WorldText.LABEL;
                    colors = iArr;
                } else {
                    runs = new String[]{distance};
                    colors = new int[]{WorldText.LABEL};
                }
                WorldText.draw(dl, screen[0], screen[1], name, runs, colors, fade(spawner.distance()));
            }
        }
    }

    public static float[] labelAnchor(class_310 mc, class_2338 pos, float screenW, float screenH) {
        float[] anchor = WorldLabel.project(mc, new class_243(((double) pos.method_10263()) + 0.5d, ((double) pos.method_10264()) + LABEL_HEIGHT, ((double) pos.method_10260()) + 0.5d), screenW, screenH);
        if (anchor == null) {
            return null;
        }
        for (int corner = 0; corner < 4; corner++) {
            float[] top = WorldLabel.project(mc, new class_243(pos.method_10263() + (corner & 1), ((double) pos.method_10264()) + 1.0d, pos.method_10260() + (corner >> 1)), screenW, screenH);
            if (top != null) {
                anchor[1] = Math.min(anchor[1], top[1]);
            }
        }
        return anchor;
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        if (this.sRadius.get()) {
            class_310 mc = class_310.method_1551();
            List<Shown> shown = inRange(mc);
            if (shown.isEmpty()) {
                return;
            }
            int accent = GuiPalette.abgr(WorldText.hudAccent()) & 16777215;
            for (Shown spawner : shown) {
                if (spawner.range() > 0) {
                    float alpha = (spawner.inside() ? RING_ALPHA_INSIDE : RING_ALPHA_OUTSIDE) * fade(spawner.distance());
                    int color = (Math.round(alpha * 255.0f) << 24) | accent;
                    if ((color >>> 24) >= 4) {
                        drawRing(class_243.method_24953(spawner.pos()), spawner.range(), color);
                    }
                }
            }
        }
    }

    private static void drawRing(class_243 centre, double radius, int color) {
        class_243 previous = null;
        for (int i = 0; i <= 64; i++) {
            double angle = (6.283185307179586d * ((double) i)) / 64.0d;
            class_243 point = centre.method_1031(Math.cos(angle) * radius, 0.0d, Math.sin(angle) * radius);
            if (previous != null) {
                class_12180.method_75546(previous, point, color, RING_WIDTH).method_75533();
            }
            previous = point;
        }
    }
}
