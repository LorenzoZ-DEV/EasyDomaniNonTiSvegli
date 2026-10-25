package net.anubis.client.module.impl.render;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.anubis.client.config.FriendStore;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.util.TracerEngine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_1297;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_1569;
import net.minecraft.class_1657;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_638;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/EspModule.class */
@Environment(EnvType.CLIENT)
public final class EspModule extends ClientModule {
    private static EspModule instance;
    private static final int ENTITY_CAP = 96;
    private static final Comparator<Target> PRIORITY = Comparator.comparing(target -> {
        return Boolean.valueOf(target.group() != Group.PLAYER);
    }).thenComparingDouble((v0) -> {
        return v0.distanceSq();
    });
    private final BooleanSetting sPlayers;
    private final BooleanSetting sMobs;
    private final BooleanSetting sAnimals;
    private final BooleanSetting sIgnoreSelf;
    private final BooleanSetting sBox;
    private final BooleanSetting sTracers;
    private final BooleanSetting sCustomTracerColor;
    private final ColorSetting sTracerColor;
    private final ModeSetting sShape;
    private final NumberSetting sRange;
    private final NumberSetting sMaxEntities;
    private final ColorSetting sPlayerColor;
    private final BooleanSetting sFriends;
    private final ColorSetting sFriendColor;
    private final ColorSetting sMobColor;
    private final ColorSetting sAnimalColor;
    private final NumberSetting sFillAlpha;
    private final NumberSetting sLineWidth;
    private final Map<class_1297, Integer> glowing;
    private long glowFrame;
    private class_638 glowLevel;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/EspModule$Group.class */
    @Environment(EnvType.CLIENT)
    private enum Group {
        PLAYER,
        HOSTILE,
        PASSIVE
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/EspModule$Target.class */
    @Environment(EnvType.CLIENT)
    private static final class Target extends Record {
        private final class_1297 entity;
        private final Group group;
        private final double distanceSq;

        private Target(class_1297 entity, Group group, double distanceSq) {
            this.entity = entity;
            this.group = group;
            this.distanceSq = distanceSq;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Target.class), Target.class, "entity;group;distanceSq", "FIELD:Lnet/anubis/client/module/impl/render/EspModule$Target;->entity:Lnet/minecraft/class_1297;", "FIELD:Lnet/anubis/client/module/impl/render/EspModule$Target;->group:Lnet/anubis/client/module/impl/render/EspModule$Group;", "FIELD:Lnet/anubis/client/module/impl/render/EspModule$Target;->distanceSq:D").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Target.class), Target.class, "entity;group;distanceSq", "FIELD:Lnet/anubis/client/module/impl/render/EspModule$Target;->entity:Lnet/minecraft/class_1297;", "FIELD:Lnet/anubis/client/module/impl/render/EspModule$Target;->group:Lnet/anubis/client/module/impl/render/EspModule$Group;", "FIELD:Lnet/anubis/client/module/impl/render/EspModule$Target;->distanceSq:D").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Target.class, Object.class), Target.class, "entity;group;distanceSq", "FIELD:Lnet/anubis/client/module/impl/render/EspModule$Target;->entity:Lnet/minecraft/class_1297;", "FIELD:Lnet/anubis/client/module/impl/render/EspModule$Target;->group:Lnet/anubis/client/module/impl/render/EspModule$Group;", "FIELD:Lnet/anubis/client/module/impl/render/EspModule$Target;->distanceSq:D").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_1297 entity() {
            return this.entity;
        }

        public Group group() {
            return this.group;
        }

        public double distanceSq() {
            return this.distanceSq;
        }
    }

    public EspModule() {
        super("esp", "ESP", "Highlights players, hostile and passive mobs with outlines, boxes and tracers.", ModuleCategory.RENDER);
        this.sPlayers = (BooleanSetting) addSetting("players", new BooleanSetting("Players", true));
        this.sMobs = (BooleanSetting) addSetting("mobs", new BooleanSetting("Hostile Mobs", true));
        this.sAnimals = (BooleanSetting) addSetting("animals", new BooleanSetting("Passive Mobs", false));
        this.sIgnoreSelf = (BooleanSetting) addSetting("ignore-self", new BooleanSetting("Ignore Self", true));
        this.sBox = (BooleanSetting) addSetting("box", new BooleanSetting("Box", true));
        this.sTracers = (BooleanSetting) addSetting("tracers", new BooleanSetting("Tracers", true));
        this.sCustomTracerColor = (BooleanSetting) addSetting("custom-tracer-color", new BooleanSetting("Custom Tracer Color", false));
        this.sTracerColor = (ColorSetting) addSetting("tracer-color", new ColorSetting("Tracer Color", -1));
        this.sShape = (ModeSetting) addSetting("shape", new ModeSetting("Shape", "Glow", "Glow", "Box", "Fill", "Both"));
        this.sRange = (NumberSetting) addSetting("range", new NumberSetting("Range", 96.0f, 16.0f, 256.0f).step(1.0f));
        this.sMaxEntities = (NumberSetting) addSetting("max-entities", new NumberSetting("Max Entities", 48.0f, 8.0f, 96.0f).step(1.0f));
        this.sPlayerColor = (ColorSetting) addSetting("player-color", new ColorSetting("Player Color", -1));
        this.sFriends = (BooleanSetting) addSetting("friends", new BooleanSetting("Highlight Friends", true));
        this.sFriendColor = (ColorSetting) addSetting("friend-color", new ColorSetting("Friend Color", -10169601));
        this.sMobColor = (ColorSetting) addSetting("mob-color", new ColorSetting("Hostile Color", -47814));
        this.sAnimalColor = (ColorSetting) addSetting("animal-color", new ColorSetting("Passive Color", -13577896));
        this.sFillAlpha = (NumberSetting) addSetting("fill-alpha", new NumberSetting("Fill Alpha", 0.3f, 0.0f, 1.0f).step(0.05f));
        this.sLineWidth = (NumberSetting) addSetting("line-width", new NumberSetting("Line Width", 1.5f, 0.5f, 5.0f).step(0.5f));
        this.glowing = new IdentityHashMap();
        this.glowFrame = -1L;
        instance = this;
        this.sBox.visibleWhen(() -> {
            return !this.sShape.is("Glow");
        });
        this.sFillAlpha.visibleWhen(() -> {
            return this.sShape.is("Fill") || this.sShape.is("Both");
        });
        this.sLineWidth.visibleWhen(() -> {
            return this.sShape.is("Box") || this.sShape.is("Both");
        });
        BooleanSetting booleanSetting = this.sCustomTracerColor;
        BooleanSetting booleanSetting2 = this.sTracers;
        Objects.requireNonNull(booleanSetting2);
        booleanSetting.visibleWhen(booleanSetting2::get);
        this.sTracerColor.visibleWhen(() -> {
            return this.sTracers.get() && this.sCustomTracerColor.get();
        });
        ColorSetting colorSetting = this.sPlayerColor;
        BooleanSetting booleanSetting3 = this.sPlayers;
        Objects.requireNonNull(booleanSetting3);
        colorSetting.visibleWhen(booleanSetting3::get);
        BooleanSetting booleanSetting4 = this.sFriends;
        BooleanSetting booleanSetting5 = this.sPlayers;
        Objects.requireNonNull(booleanSetting5);
        booleanSetting4.visibleWhen(booleanSetting5::get);
        this.sFriendColor.visibleWhen(() -> {
            return this.sPlayers.get() && this.sFriends.get();
        });
        ColorSetting colorSetting2 = this.sMobColor;
        BooleanSetting booleanSetting6 = this.sMobs;
        Objects.requireNonNull(booleanSetting6);
        colorSetting2.visibleWhen(booleanSetting6::get);
        ColorSetting colorSetting3 = this.sAnimalColor;
        BooleanSetting booleanSetting7 = this.sAnimals;
        Objects.requireNonNull(booleanSetting7);
        colorSetting3.visibleWhen(booleanSetting7::get);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        resetGlow();
    }

    public static boolean shouldGlow(class_1297 entity) {
        EspModule module = instance;
        if (module != null && module.isEnabled() && module.sShape.is("Glow")) {
            return module.glowing().containsKey(entity);
        }
        return false;
    }

    public static int glowColor(class_1297 entity) {
        EspModule module = instance;
        Integer color = module == null ? null : module.glowing().get(entity);
        return (color == null ? 16777215 : color.intValue()) & 16777215;
    }

    private Map<class_1297, Integer> glowing() {
        long frame = Render3DEvent.frame();
        if (frame != this.glowFrame) {
            this.glowFrame = frame;
            this.glowing.clear();
            for (Target target : targets(1.0f)) {
                this.glowing.put(target.entity(), Integer.valueOf(colorFor(target)));
            }
        }
        return this.glowing;
    }

    @EventHandler
    public void onTick(TickEvent event) {
        class_638 level = class_310.method_1551().field_1687;
        if (level != this.glowLevel || !this.sShape.is("Glow")) {
            resetGlow();
        }
        this.glowLevel = level;
    }

    private void resetGlow() {
        this.glowing.clear();
        this.glowFrame = -1L;
        this.glowLevel = null;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    @EventHandler
    public void onRender3D(Render3DEvent event) throws MatchException {
        class_12179 class_12179VarMethod_75536;
        boolean box = this.sBox.get() && !this.sShape.is("Glow");
        boolean tracers = this.sTracers.get();
        if (box || tracers) {
            String shape = this.sShape.get();
            float lineWidth = this.sLineWidth.get();
            int fillAlpha = Math.round(this.sFillAlpha.get() * 255.0f);
            for (Target target : targets(event.partialTick)) {
                int color = colorFor(target);
                class_238 bounds = interpolatedBox(target.entity(), event.partialTick);
                if (box) {
                    int fill = (color & 16777215) | (fillAlpha << 24);
                    switch (shape) {
                        case "Fill":
                            class_12179VarMethod_75536 = class_12179.method_75539(fill);
                            break;
                        case "Both":
                            class_12179VarMethod_75536 = class_12179.method_75537(color, lineWidth, fill);
                            break;
                        default:
                            class_12179VarMethod_75536 = class_12179.method_75536(color, lineWidth);
                            break;
                    }
                    class_12179 style = class_12179VarMethod_75536;
                    class_12180.method_75541(bounds, style).method_75533();
                }
                if (tracers) {
                    TracerEngine.draw(bounds.method_1005(), this.sCustomTracerColor.get() ? this.sTracerColor.argb() : color);
                }
            }
        }
    }

    private List<Target> targets(float partialTick) {
        class_310 mc = class_310.method_1551();
        if (mc.field_1687 == null || mc.field_1724 == null) {
            return List.of();
        }
        int max = Math.max(1, Math.min(ENTITY_CAP, this.sMaxEntities.getInt()));
        float range = Math.max(1.0f, this.sRange.get());
        double rangeSq = ((double) range) * ((double) range);
        class_243 eye = mc.field_1724.method_5836(partialTick);
        List<Target> result = new ArrayList<>();
        for (class_1657 class_1657Var : mc.field_1687.method_18112()) {
            Group group = groupOf(class_1657Var);
            if (group != null && shown(group) && (class_1657Var != mc.field_1724 || !this.sIgnoreSelf.get())) {
                if (!isGone(class_1657Var)) {
                    if (class_1657Var instanceof class_1657) {
                        class_1657 player = class_1657Var;
                        if (player.method_7325()) {
                        }
                    }
                    double distanceSq = class_1657Var.method_30950(partialTick).method_1025(eye);
                    if (distanceSq <= rangeSq) {
                        result.add(new Target(class_1657Var, group, distanceSq));
                    }
                }
            }
        }
        if (result.size() <= max) {
            return result;
        }
        result.sort(PRIORITY);
        return result.subList(0, max);
    }

    private static boolean isGone(class_1297 entity) {
        if (!entity.method_31481()) {
            if (entity instanceof class_1309) {
                class_1309 living = (class_1309) entity;
                if (living.method_29504()) {
                }
            }
            return false;
        }
        return true;
    }

    private static Group groupOf(class_1297 entity) {
        if (entity instanceof class_1657) {
            return Group.PLAYER;
        }
        if (entity instanceof class_1569) {
            return Group.HOSTILE;
        }
        if (entity instanceof class_1308) {
            return Group.PASSIVE;
        }
        return null;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private boolean shown(Group group) throws MatchException {
        switch (group) {
            case PLAYER:
                return this.sPlayers.get();
            case HOSTILE:
                return this.sMobs.get();
            case PASSIVE:
                return this.sAnimals.get();
            default:
                throw new MatchException((String) null, (Throwable) null);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private int colorFor(Target target) throws MatchException {
        switch (target.group()) {
            case PLAYER:
                if (this.sFriends.get()) {
                    class_1657 class_1657VarEntity = target.entity();
                    if (class_1657VarEntity instanceof class_1657) {
                        class_1657 player = class_1657VarEntity;
                        if (FriendStore.getInstance().isFriend(player.method_7334().name())) {
                            return this.sFriendColor.argb();
                        }
                    }
                }
                return this.sPlayerColor.argb();
            case HOSTILE:
                return this.sMobColor.argb();
            case PASSIVE:
                return this.sAnimalColor.argb();
            default:
                throw new MatchException((String) null, (Throwable) null);
        }
    }

    private static class_238 interpolatedBox(class_1297 entity, float partialTick) {
        class_243 current = entity.method_73189();
        class_243 interpolated = entity.method_30950(partialTick);
        return entity.method_5829().method_989(interpolated.field_1352 - current.field_1352, interpolated.field_1351 - current.field_1351, interpolated.field_1350 - current.field_1350);
    }
}
