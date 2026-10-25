package net.anubis.client.module.impl.render;

import imgui.ImDrawList;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.anubis.client.config.FriendStore;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.gui.GuiPalette;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.HudOverlayProvider;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.impl.misc.FreecamModule;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.util.TracerEngine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1311;
import net.minecraft.class_156;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_2583;
import net.minecraft.class_310;
import net.minecraft.class_5251;
import net.minecraft.class_746;
import org.joml.Vector4f;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/TracersModule.class */
@Environment(EnvType.CLIENT)
public final class TracersModule extends ClientModule implements HudOverlayProvider {
    private static final double DISTANCE_COLOR_RANGE = 60.0d;
    private static final float ARROW_TIP = 0.73205f;
    private static final int WHITE = 16777215;
    private final BooleanSetting players;
    private final BooleanSetting animals;
    private final BooleanSetting waterAnimals;
    private final BooleanSetting monsters;
    private final BooleanSetting ambient;
    private final BooleanSetting misc;
    private final BooleanSetting ignoreSelf;
    private final BooleanSetting ignoreFriends;
    private final BooleanSetting showInvisible;
    private final ModeSetting style;
    private final ModeSetting target;
    private final BooleanSetting stem;
    private final NumberSetting maxDistance;
    private final NumberSetting distanceOffscreen;
    private final NumberSetting sizeOffscreen;
    private final BooleanSetting blinkOffscreen;
    private final NumberSetting blinkOffscreenSpeed;
    private final BooleanSetting distanceColors;
    private final BooleanSetting friendOverride;
    private final BooleanSetting teamColors;
    private final ColorSetting friendColor;
    private final ColorSetting playersColor;
    private final ColorSetting animalsColor;
    private final ColorSetting waterAnimalsColor;
    private final ColorSetting monstersColor;
    private final ColorSetting ambientColor;
    private final ColorSetting miscColor;

    public TracersModule() {
        super("tracers", "Tracers", "Displays tracer lines to specified entities.", ModuleCategory.RENDER);
        this.players = (BooleanSetting) addSetting("players", new BooleanSetting("Players", true));
        this.animals = (BooleanSetting) addSetting("animals", new BooleanSetting("Animals", false));
        this.waterAnimals = (BooleanSetting) addSetting("water-animals", new BooleanSetting("Water Animals", false));
        this.monsters = (BooleanSetting) addSetting("monsters", new BooleanSetting("Monsters", false));
        this.ambient = (BooleanSetting) addSetting("ambient", new BooleanSetting("Ambient", false));
        this.misc = (BooleanSetting) addSetting("misc", new BooleanSetting("Misc", false));
        this.ignoreSelf = (BooleanSetting) addSetting("ignore-self", new BooleanSetting("Ignore Self", false));
        this.ignoreFriends = (BooleanSetting) addSetting("ignore-friends", new BooleanSetting("Ignore Friends", false));
        this.showInvisible = (BooleanSetting) addSetting("show-invisible", new BooleanSetting("Show Invisible", true));
        this.style = (ModeSetting) addSetting("style", new ModeSetting("Style", "Lines", "Lines", "Offscreen"));
        this.target = (ModeSetting) addSetting("target", new ModeSetting("Target", "Body", "Head", "Body", "Feet"));
        this.stem = (BooleanSetting) addSetting("stem", new BooleanSetting("Stem", true));
        this.maxDistance = (NumberSetting) addSetting("max-distance", new NumberSetting("Max Distance", 256.0f, 0.0f, 256.0f).step(1.0f));
        this.distanceOffscreen = (NumberSetting) addSetting("distance-offscreen", new NumberSetting("Distance Offscreen", 200.0f, 0.0f, 500.0f).step(1.0f));
        this.sizeOffscreen = (NumberSetting) addSetting("size-offscreen", new NumberSetting("Size Offscreen", 10.0f, 2.0f, 50.0f).step(1.0f));
        this.blinkOffscreen = (BooleanSetting) addSetting("blink-offscreen", new BooleanSetting("Blink Offscreen", true));
        this.blinkOffscreenSpeed = (NumberSetting) addSetting("blink-offscreen-speed", new NumberSetting("Blink Offscreen Speed", 4.0f, 1.0f, 15.0f).step(1.0f));
        this.distanceColors = (BooleanSetting) addSetting("distance-colors", new BooleanSetting("Distance Colors", false));
        this.friendOverride = (BooleanSetting) addSetting("show-friend-colors", new BooleanSetting("Show Friend Colors", true));
        this.teamColors = (BooleanSetting) addSetting("team-colors", new BooleanSetting("Team Colors", true));
        this.friendColor = (ColorSetting) addSetting("friend-color", new ColorSetting("Friend Color", -16711756));
        this.playersColor = (ColorSetting) addSetting("players-colors", new ColorSetting("Players Color", 2144193997));
        this.animalsColor = (ColorSetting) addSetting("animals-color", new ColorSetting("Animals Color", 2140274577));
        this.waterAnimalsColor = (ColorSetting) addSetting("water-animals-color", new ColorSetting("Water Animals Color", 2140246527));
        this.monstersColor = (ColorSetting) addSetting("monsters-color", new ColorSetting("Monsters Color", 2147455377));
        this.ambientColor = (ColorSetting) addSetting("ambient-color", new ColorSetting("Ambient Color", 2135640907));
        this.miscColor = (ColorSetting) addSetting("misc-color", new ColorSetting("Misc Color", 2140246417));
        this.target.visibleWhen(() -> {
            return this.style.is("Lines");
        });
        this.stem.visibleWhen(() -> {
            return this.style.is("Lines");
        });
        this.distanceOffscreen.visibleWhen(() -> {
            return this.style.is("Offscreen");
        });
        this.sizeOffscreen.visibleWhen(() -> {
            return this.style.is("Offscreen");
        });
        this.blinkOffscreen.visibleWhen(() -> {
            return this.style.is("Offscreen");
        });
        this.blinkOffscreenSpeed.visibleWhen(() -> {
            return this.style.is("Offscreen") && this.blinkOffscreen.get();
        });
        this.friendOverride.visibleWhen(() -> {
            return this.distanceColors.get() && !this.ignoreFriends.get();
        });
        this.teamColors.visibleWhen(() -> {
            return !this.distanceColors.get() && this.players.get();
        });
        this.friendColor.visibleWhen(() -> {
            return !this.ignoreFriends.get() && this.players.get() && (!this.distanceColors.get() || this.friendOverride.get());
        });
        this.playersColor.visibleWhen(() -> {
            return !this.distanceColors.get() && this.players.get();
        });
        this.animalsColor.visibleWhen(() -> {
            return !this.distanceColors.get() && this.animals.get();
        });
        this.waterAnimalsColor.visibleWhen(() -> {
            return !this.distanceColors.get() && this.waterAnimals.get();
        });
        this.monstersColor.visibleWhen(() -> {
            return !this.distanceColors.get() && this.monsters.get();
        });
        this.ambientColor.visibleWhen(() -> {
            return !this.distanceColors.get() && this.ambient.get();
        });
        this.miscColor.visibleWhen(() -> {
            return !this.distanceColors.get() && this.misc.get();
        });
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        double d;
        class_310 mc = class_310.method_1551();
        if (mc.field_1690.field_1842 || !this.style.is("Lines")) {
            return;
        }
        for (class_1297 entity : targets(mc)) {
            int color = colorOf(mc, entity);
            class_243 feet = entity.method_30950(event.partialTick);
            double height = entity.method_5829().method_17940();
            switch (this.target.get()) {
                case "Head":
                    d = height;
                    break;
                case "Body":
                    d = height / 2.0d;
                    break;
                default:
                    d = 0.0d;
                    break;
            }
            double up = d;
            TracerEngine.draw(feet.method_1031(0.0d, up, 0.0d), color);
            if (this.stem.get()) {
                TracerEngine.stem(feet, height, color);
            }
        }
    }

    @Override // net.anubis.client.module.HudOverlayProvider
    public boolean isHudOverlayEnabled() {
        return isEnabled() && this.style.is("Offscreen") && !class_310.method_1551().field_1690.field_1842;
    }

    @Override // net.anubis.client.module.HudOverlayProvider
    public void renderHudOverlay(ImDrawList dl, float screenW, float screenH) {
        class_310 mc = class_310.method_1551();
        float partialTick = mc.method_61966().method_60637(false);
        float blink = this.blinkOffscreen.get() ? blinkAlpha() : 1.0f;
        float reach = this.distanceOffscreen.get();
        float size = this.sizeOffscreen.get();
        for (class_1297 entity : targets(mc)) {
            Vector4f clip = TracerEngine.clip(entity.method_30950(partialTick));
            if (clip == null) {
                return;
            }
            if (clip.w <= 0.0f || Math.abs(clip.x) >= clip.w || Math.abs(clip.y) >= clip.w) {
                double angle = Math.atan2((-clip.y) * screenH, clip.x * screenW);
                int color = colorOf(mc, entity);
                int alpha = (int) ((color >>> 24) * blink);
                arrow(dl, (screenW * 0.5f) + (reach * ((float) Math.cos(angle))), (screenH * 0.5f) + (reach * ((float) Math.sin(angle))), size, (float) angle, GuiPalette.abgr((alpha << 24) | (color & WHITE)));
            }
        }
    }

    private List<class_1297> targets(class_310 mc) {
        if (mc.field_1687 == null || mc.field_1724 == null) {
            return List.of();
        }
        FreecamModule freecam = (FreecamModule) ModuleRegistry.get(FreecamModule.class);
        boolean skipSelf = this.ignoreSelf.get() || freecam == null || !freecam.isEnabled();
        double maxSq = ((double) this.maxDistance.get()) * ((double) this.maxDistance.get());
        class_243 camera = mc.field_1773.method_19418().method_71156();
        double view = ((double) (((Integer) mc.field_1690.method_42503().method_41753()).intValue() + 1)) * 16.0d;
        List<class_1297> result = new ArrayList<>();
        for (class_746 class_746Var : mc.field_1687.method_18112()) {
            if (class_746Var != mc.field_1724 || !skipSelf) {
                if (groupColor(class_746Var) != null && class_746Var.method_5858(mc.field_1724) <= maxSq && (!this.ignoreFriends.get() || !isFriend(class_746Var))) {
                    if (this.showInvisible.get() || !class_746Var.method_5767()) {
                        if (Math.abs(camera.field_1352 - class_746Var.method_23317()) < view && Math.abs(camera.field_1350 - class_746Var.method_23321()) < view) {
                            result.add(class_746Var);
                        }
                    }
                }
            }
        }
        return result;
    }

    /* JADX INFO: renamed from: net.anubis.client.module.impl.render.TracersModule$1, reason: invalid class name */
    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/TracersModule$1.class */
    @Environment(EnvType.CLIENT)
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$net$minecraft$world$entity$MobCategory = new int[class_1311.values().length];

        static {
            try {
                $SwitchMap$net$minecraft$world$entity$MobCategory[class_1311.field_6294.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$net$minecraft$world$entity$MobCategory[class_1311.field_24460.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$net$minecraft$world$entity$MobCategory[class_1311.field_6300.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$net$minecraft$world$entity$MobCategory[class_1311.field_30092.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$net$minecraft$world$entity$MobCategory[class_1311.field_34447.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$net$minecraft$world$entity$MobCategory[class_1311.field_6302.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$net$minecraft$world$entity$MobCategory[class_1311.field_6303.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    private ColorSetting groupColor(class_1297 entity) {
        if (entity instanceof class_1657) {
            if (this.players.get()) {
                return this.playersColor;
            }
            return null;
        }
        switch (AnonymousClass1.$SwitchMap$net$minecraft$world$entity$MobCategory[entity.method_5864().method_5891().ordinal()]) {
            case 1:
                if (this.animals.get()) {
                    return this.animalsColor;
                }
                return null;
            case 2:
            case 3:
            case 4:
            case 5:
                if (this.waterAnimals.get()) {
                    return this.waterAnimalsColor;
                }
                return null;
            case 6:
                if (this.monsters.get()) {
                    return this.monstersColor;
                }
                return null;
            case 7:
                if (this.ambient.get()) {
                    return this.ambientColor;
                }
                return null;
            default:
                if (this.misc.get()) {
                    return this.miscColor;
                }
                return null;
        }
    }

    private static boolean isFriend(class_1297 entity) {
        if (entity instanceof class_1657) {
            class_1657 player = (class_1657) entity;
            if (FriendStore.getInstance().isFriend(player.method_7334().name())) {
                return true;
            }
        }
        return false;
    }

    private int colorOf(class_310 mc, class_1297 entity) {
        boolean friend = isFriend(entity);
        if (this.distanceColors.get()) {
            return (friend && this.friendOverride.get()) ? this.friendColor.argb() : distanceColor(mc, entity);
        }
        if (entity instanceof class_1657) {
            class_1657 player = (class_1657) entity;
            int base = this.playersColor.argb();
            int alpha = base & (-16777216);
            if (friend) {
                return alpha | this.friendColor.rgb();
            }
            int team = this.teamColors.get() ? teamColor(player) : WHITE;
            return team == WHITE ? base : alpha | team;
        }
        return groupColor(entity).argb();
    }

    private static int distanceColor(class_310 mc, class_1297 entity) {
        double percent = mc.field_1773.method_19418().method_71156().method_1022(entity.method_33571()) / DISTANCE_COLOR_RANGE;
        if (percent > 1.0d) {
            return -16711936;
        }
        int r = percent < 0.5d ? 255 : 255 - ((int) ((255.0d * (percent - 0.5d)) / 0.5d));
        int g = percent < 0.5d ? (int) ((255.0d * percent) / 0.5d) : 255;
        return (-16777216) | (r << 16) | (g << 8);
    }

    private static int teamColor(class_1657 player) {
        Int2IntOpenHashMap counts = new Int2IntOpenHashMap();
        player.method_5476().method_27658((segment, text) -> {
            class_5251 color = segment.method_10973();
            counts.addTo(color == null ? WHITE : color.method_27716(), text.length());
            return Optional.empty();
        }, class_2583.field_24360);
        int best = WHITE;
        int bestCount = 0;
        ObjectIterator it = counts.int2IntEntrySet().iterator();
        while (it.hasNext()) {
            Int2IntMap.Entry entry = (Int2IntMap.Entry) it.next();
            if (entry.getIntValue() > bestCount) {
                best = entry.getIntKey();
                bestCount = entry.getIntValue();
            }
        }
        return best;
    }

    private float blinkAlpha() {
        double time = class_156.method_658() * (((double) this.blinkOffscreenSpeed.get()) / 4.0d);
        return ((float) Math.abs((time % 1000.0d) - 500.0d)) / 500.0f;
    }

    private static void arrow(ImDrawList dl, float x, float y, float size, float angle, int abgr) {
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        float cx = x - ((size * 1.26795f) / 3.0f);
        float[] px = new float[3];
        px[0] = x - size;
        px[1] = x + (size * ARROW_TIP);
        px[2] = x - size;
        float[] py = new float[3];
        py[0] = y - size;
        py[1] = y;
        py[2] = y + size;
        for (int i = 0; i < 3; i++) {
            float dx = px[i] - cx;
            float dy = py[i] - y;
            px[i] = (cx + (dx * cos)) - (dy * sin);
            py[i] = y + (dx * sin) + (dy * cos);
        }
        dl.addTriangleFilled(px[0], py[0], px[1], py[1], px[2], py[2], abgr);
    }
}
