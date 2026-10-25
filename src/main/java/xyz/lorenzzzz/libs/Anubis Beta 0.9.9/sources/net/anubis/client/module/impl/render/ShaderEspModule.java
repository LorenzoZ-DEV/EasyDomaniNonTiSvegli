package net.anubis.client.module.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Arrays;
import net.anubis.client.config.FriendStore;
import net.anubis.client.event.impl.Render3DEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.util.ShaderOverlay;
import net.anubis.client.util.combat.CombatTarget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10017;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_1297;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_1511;
import net.minecraft.class_1542;
import net.minecraft.class_1569;
import net.minecraft.class_1657;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_4184;
import net.minecraft.class_4604;
import net.minecraft.class_638;
import net.minecraft.class_9848;
import org.joml.Matrix4f;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/ShaderEspModule.class */
@Environment(EnvType.CLIENT)
public final class ShaderEspModule extends ClientModule {
    public static volatile ShaderEspModule active;
    private static final String COLOR_THEME = "Theme";
    private static final String COLOR_MANUAL = "Manual";
    private static final int OPAQUE = -16777216;
    private static final int WHITE = -1;
    private static final float FRIEND_LIFT = 0.45f;
    private static final float TARGET_LIFT = 0.8f;
    private static final float SHEEN_LIFT = 0.6f;
    private static final int LIMIT_CAP = 96;
    private static final long TARGET_MEMORY_MS = 3500;
    private static final long BREATH_NANOS = 2600000000L;
    private static final long SHEEN_NANOS = 5200000000L;
    private static final double CULL_MARGIN = 0.75d;
    private final BooleanSetting players;
    private final BooleanSetting passives;
    private final BooleanSetting items;
    private final BooleanSetting crystals;
    private final BooleanSetting markTarget;
    private final ModeSetting colorMode;
    private final ColorSetting playerColor;
    private final ColorSetting friendColor;
    private final ColorSetting passiveColor;
    private final ColorSetting itemColor;
    private final ColorSetting crystalColor;
    private final ColorSetting targetColor;
    private final NumberSetting stroke;
    private final NumberSetting aura;
    private final NumberSetting auraReach;
    private final NumberSetting fill;
    private final NumberSetting sheen;
    private final ColorSetting sheenColor;
    private final BooleanSetting breathe;
    private final NumberSetting distance;
    private final NumberSetting limit;
    private final class_1297[] picked;
    private final int[] tints;
    private final double[] spans;
    private int count;
    private long pickedFrame;

    public ShaderEspModule() {
        super("glow-esp", "Glow ESP", "Outlines entities through walls with a glow.", ModuleCategory.RENDER);
        this.players = (BooleanSetting) addSetting("players", new BooleanSetting("Players", true));
        this.passives = (BooleanSetting) addSetting("passives", new BooleanSetting("Passives", false));
        this.items = (BooleanSetting) addSetting("items", new BooleanSetting("Items", false));
        this.crystals = (BooleanSetting) addSetting("crystals", new BooleanSetting("Crystals", true));
        this.markTarget = (BooleanSetting) addSetting("mark-target", new BooleanSetting("Mark Target", true));
        this.colorMode = (ModeSetting) addSetting("color", new ModeSetting("Color", "Theme", "Theme", "Manual"));
        this.playerColor = (ColorSetting) addSetting("player-color", new ColorSetting("Player Color", -13338378));
        this.friendColor = (ColorSetting) addSetting("friend-color", new ColorSetting("Friend Color", -7358982));
        this.passiveColor = (ColorSetting) addSetting("passive-color", new ColorSetting("Passive Color", -13061400));
        this.itemColor = (ColorSetting) addSetting("item-color", new ColorSetting("Item Color", -11744808));
        this.crystalColor = (ColorSetting) addSetting("crystal-color", new ColorSetting("Crystal Color", -6128394));
        this.targetColor = (ColorSetting) addSetting("target-color", new ColorSetting("Target Color", -2693891));
        this.stroke = (NumberSetting) addSetting("stroke", new NumberSetting("Stroke", 2.0f, 0.5f, 6.0f).step(0.5f));
        this.aura = (NumberSetting) addSetting("aura", new NumberSetting("Aura", 55.0f, 0.0f, 100.0f).step(5.0f));
        this.auraReach = (NumberSetting) addSetting("aura-reach", new NumberSetting("Aura Reach", 10.0f, 3.0f, 24.0f).step(1.0f));
        this.fill = (NumberSetting) addSetting("fill", new NumberSetting("Fill", 10.0f, 0.0f, 60.0f).step(5.0f));
        this.sheen = (NumberSetting) addSetting("sheen", new NumberSetting("Sheen", 30.0f, 0.0f, 100.0f).step(5.0f));
        this.sheenColor = (ColorSetting) addSetting("sheen-color", new ColorSetting("Sheen Color", -2299649));
        this.breathe = (BooleanSetting) addSetting("breathe", new BooleanSetting("Breathe", false));
        this.distance = (NumberSetting) addSetting("distance", new NumberSetting("Distance", 64.0f, 8.0f, 192.0f).step(4.0f));
        this.limit = (NumberSetting) addSetting("limit", new NumberSetting("Limit", 48.0f, 4.0f, 96.0f).step(1.0f));
        this.picked = new class_1297[LIMIT_CAP];
        this.tints = new int[LIMIT_CAP];
        this.spans = new double[LIMIT_CAP];
        this.pickedFrame = Long.MIN_VALUE;
        this.playerColor.visibleWhen(() -> {
            return this.players.get() && manual();
        });
        this.friendColor.visibleWhen(() -> {
            return this.players.get() && manual();
        });
        this.passiveColor.visibleWhen(() -> {
            return this.passives.get() && manual();
        });
        this.itemColor.visibleWhen(() -> {
            return this.items.get() && manual();
        });
        this.crystalColor.visibleWhen(() -> {
            return this.crystals.get() && manual();
        });
        this.targetColor.visibleWhen(() -> {
            return this.markTarget.get() && manual();
        });
        this.auraReach.visibleWhen(() -> {
            return this.aura.get() > 0.0f;
        });
        this.sheenColor.visibleWhen(() -> {
            return this.sheen.get() > 0.0f && manual();
        });
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        active = this;
        on(Render3DEvent.TYPE, this::onRender3D);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        active = null;
        forget();
        if (RenderSystem.isOnRenderThread()) {
            ShaderEspPass.release();
        } else {
            class_310.method_1551().execute(ShaderEspModule::releaseIfOff);
        }
    }

    private static void releaseIfOff() {
        if (active == null) {
            ShaderEspPass.release();
        }
    }

    public static void afterLevel(class_4184 camera, Matrix4f viewRotation, Matrix4f projection) {
        ShaderEspModule module = active;
        if (module != null) {
            module.present(camera, viewRotation, projection);
        }
    }

    public static void extracted(class_1297 entity, class_10017 state) {
        if (active != null) {
            ShaderEspPass.keep(entity, state, Render3DEvent.frame());
        }
    }

    private void onRender3D(Render3DEvent event) {
        pick(event.frustum);
        this.pickedFrame = Render3DEvent.frame();
        if (this.count <= 0 || !ShaderOverlay.shaderPackInUse()) {
            return;
        }
        sketch(event.partialTick);
    }

    private void present(class_4184 camera, Matrix4f viewRotation, Matrix4f projection) {
        int drawn = this.count;
        if (drawn != 0) {
            try {
                if (this.pickedFrame + 1 == Render3DEvent.frame() && !ShaderOverlay.shaderPackInUse()) {
                    if (ShaderEspPass.paint(camera, viewRotation, projection, this.picked, this.tints, drawn, this.pickedFrame)) {
                        long now = System.nanoTime();
                        float scale = Math.max(1.0f, class_310.method_1551().method_1522().field_1481 / 1080.0f);
                        float phase = (float) ((now % SHEEN_NANOS) / 5.2E9d);
                        ShaderEspPass.compose(this.stroke.get() * scale, auraStrength(now), this.auraReach.get() * scale, this.fill.get() / 100.0f, phase, sheenTint(), this.sheen.get() / 100.0f);
                    }
                }
            } finally {
                forget();
                ShaderEspPass.dropKept();
            }
        }
    }

    private boolean manual() {
        return this.colorMode.is("Manual");
    }

    private int sheenTint() {
        return manual() ? this.sheenColor.argb() | (-16777216) : class_9848.method_61319(SHEEN_LIFT, CosmeticAccent.argb(), -1);
    }

    private float auraStrength(long now) {
        float strength = this.aura.get() / 100.0f;
        if (!this.breathe.get()) {
            return strength;
        }
        double cycle = (now % BREATH_NANOS) / 2.6E9d;
        float wave = (float) (0.5d - (0.5d * Math.cos((cycle * 3.141592653589793d) * 2.0d)));
        return strength * (FRIEND_LIFT + (0.55f * wave * wave * (3.0f - (2.0f * wave))));
    }

    private void pick(class_4604 frustum) {
        int tint;
        forget();
        class_310 mc = class_310.method_1551();
        class_638 world = mc.field_1687;
        class_1297 class_1297Var = mc.field_1724;
        if (world == null || class_1297Var == null) {
            return;
        }
        class_4184 camera = mc.field_1773.method_19418();
        class_243 eye = camera.method_71156();
        class_1297 viewer = camera.method_19333() ? null : mc.method_1560();
        double reachSq = ((double) this.distance.get()) * ((double) this.distance.get());
        int cap = Math.max(1, Math.min(LIMIT_CAP, this.limit.getInt()));
        class_1309 mark = this.markTarget.get() ? CombatTarget.current(TARGET_MEMORY_MS) : null;
        boolean theme = !manual();
        int accent = theme ? CosmeticAccent.argb() : 0;
        for (class_1297 entity : world.method_18112()) {
            if (entity != class_1297Var && entity != viewer && !entity.method_31481() && (tint = tintOf(entity, mark, theme, accent)) != 0) {
                double spanSq = entity.method_5707(eye);
                if (spanSq <= reachSq && (frustum == null || frustum.method_23093(entity.method_5829().method_1014(CULL_MARGIN)))) {
                    admit(entity, tint, spanSq, cap);
                }
            }
        }
    }

    private int tintOf(class_1297 entity, class_1309 mark, boolean theme, int accent) {
        ColorSetting group;
        if (entity instanceof class_1309) {
            class_1309 living = (class_1309) entity;
            if (living.method_29504()) {
                return 0;
            }
        }
        boolean friend = false;
        if (entity instanceof class_1657) {
            class_1657 player = (class_1657) entity;
            if (!this.players.get() || player.method_7325()) {
                return 0;
            }
            friend = FriendStore.getInstance().isFriend(player.method_7334().name());
            group = this.playerColor;
        } else if (entity instanceof class_1511) {
            if (!this.crystals.get()) {
                return 0;
            }
            group = this.crystalColor;
        } else if (entity instanceof class_1542) {
            if (!this.items.get()) {
                return 0;
            }
            group = this.itemColor;
        } else {
            if ((entity instanceof class_1569) || !(entity instanceof class_1308) || !this.passives.get()) {
                return 0;
            }
            group = this.passiveColor;
        }
        if (mark != null && entity == mark) {
            return theme ? class_9848.method_61319(TARGET_LIFT, accent, -1) : this.targetColor.argb() | (-16777216);
        }
        if (friend) {
            return theme ? class_9848.method_61319(FRIEND_LIFT, accent, -1) : this.friendColor.argb() | (-16777216);
        }
        return theme ? accent : group.argb() | (-16777216);
    }

    private void admit(class_1297 entity, int tint, double spanSq, int cap) {
        int slot = this.count;
        if (this.count < cap) {
            this.count++;
        } else {
            slot = 0;
            for (int i = 1; i < this.count; i++) {
                if (this.spans[i] > this.spans[slot]) {
                    slot = i;
                }
            }
            if (this.spans[slot] <= spanSq) {
                return;
            }
        }
        this.picked[slot] = entity;
        this.tints[slot] = tint;
        this.spans[slot] = spanSq;
    }

    private void sketch(float partialTick) {
        float width = Math.max(1.0f, this.stroke.get());
        for (int i = 0; i < this.count; i++) {
            class_1297 entity = this.picked[i];
            class_243 current = entity.method_73189();
            class_243 shown = entity.method_30950(partialTick);
            class_238 box = entity.method_5829().method_989(shown.field_1352 - current.field_1352, shown.field_1351 - current.field_1351, shown.field_1350 - current.field_1350);
            class_12180.method_75541(box, class_12179.method_75536(this.tints[i], width)).method_75533();
        }
    }

    private void forget() {
        Arrays.fill(this.picked, 0, Math.min(this.count, LIMIT_CAP), (Object) null);
        this.count = 0;
    }
}
