package net.anubis.client.module.impl.misc;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import net.anubis.client.chat.ModuleChat;
import net.anubis.client.event.EventPriority;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.KeyInputEvent;
import net.anubis.client.event.impl.MouseInputEvent;
import net.anubis.client.input.ModuleKeybinds;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.KeySetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_265;
import net.minecraft.class_310;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_4184;
import net.minecraft.class_5321;
import net.minecraft.class_638;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/CoordSnapperModule.class */
@Environment(EnvType.CLIENT)
public final class CoordSnapperModule extends ClientModule {
    private static final String TARGET_LOOKED_AT = "Looked At";
    private static final String TARGET_OWN_POSITION = "Own Position";
    private static final String FORMAT_SPACES = "X Y Z";
    private static final String FORMAT_COMMAS = "X, Y, Z";
    private static final String FORMAT_TP = "/tp Command";
    private static final double STEP_PAST_BLOCK = 0.001d;
    private final KeySetting key;
    private final ModeSetting target;
    private final NumberSetting range;
    private final BooleanSetting stopAtFluids;
    private final ModeSetting format;
    private final BooleanSetting includeDimension;
    private final BooleanSetting confirm;

    public CoordSnapperModule() {
        super("coord-snapper", "Coord Snapper", "Copies the coordinates you look at or stand on with one key.", ModuleCategory.MISC);
        this.key = (KeySetting) addSetting("key", new KeySetting("Key", -1));
        this.target = (ModeSetting) addSetting("target", new ModeSetting("Target", TARGET_LOOKED_AT, TARGET_LOOKED_AT, TARGET_OWN_POSITION));
        this.range = (NumberSetting) addSetting("range", new NumberSetting("Range", 256.0f, 16.0f, 512.0f).step(1.0f));
        this.stopAtFluids = (BooleanSetting) addSetting("stop-at-fluids", new BooleanSetting("Stop at Fluids", false));
        this.format = (ModeSetting) addSetting("format", new ModeSetting("Format", FORMAT_SPACES, FORMAT_SPACES, FORMAT_COMMAS, FORMAT_TP));
        this.includeDimension = (BooleanSetting) addSetting("include-dimension", new BooleanSetting("Include Dimension", true));
        this.confirm = (BooleanSetting) addSetting("confirm", new BooleanSetting("Chat Confirmation", true));
        this.range.visibleWhen(() -> {
            return this.target.is(TARGET_LOOKED_AT);
        });
        this.stopAtFluids.visibleWhen(() -> {
            return this.target.is(TARGET_LOOKED_AT);
        });
        BooleanSetting notifications = usesNotifications();
        BooleanSetting booleanSetting = this.confirm;
        Objects.requireNonNull(notifications);
        booleanSetting.visibleWhen(notifications::get);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        on(KeyInputEvent.TYPE, Stage.PRE, EventPriority.HIGH, this::onKey);
        on(MouseInputEvent.TYPE, Stage.PRE, EventPriority.HIGH, this::onMouse);
    }

    private void onKey(KeyInputEvent event) {
        if (event.action == 1) {
            onPress(event.key);
        }
    }

    private void onMouse(MouseInputEvent event) {
        if (event.action == 1) {
            onPress(ModuleKeybinds.MOUSE_OFFSET + event.button);
        }
    }

    private void onPress(int pressed) {
        int bound = this.key.value();
        if (ModuleKeybinds.isBindable(bound) && pressed == bound) {
            class_310 mc = class_310.method_1551();
            if (mc.field_1755 != null || mc.field_1724 == null || mc.field_1687 == null) {
                return;
            }
            snap(mc, mc.field_1724, mc.field_1687);
        }
    }

    private void snap(class_310 mc, class_746 player, class_638 level) {
        class_2338 pos;
        if (this.target.is(TARGET_OWN_POSITION)) {
            pos = player.method_24515();
        } else {
            pos = raycast(mc, player, level);
            if (pos == null) {
                ModuleChat.send(this, ModuleChat.body().danger("Nothing copied").text(": no loaded block within ").value(Integer.valueOf(this.range.getInt())).text(" blocks of your aim"));
                return;
            } else if (this.format.is(FORMAT_TP)) {
                pos = pos.method_10084();
            }
        }
        String text = formatCoords(pos, level.method_27983());
        mc.field_1774.method_1455(text);
        if (this.confirm.get()) {
            ModuleChat.send(this, ModuleChat.body().good("Copied").text(" ").value(text));
        }
    }

    private class_2338 raycast(class_310 mc, class_746 player, class_638 level) {
        class_243 origin;
        class_243 direction;
        FreecamModule freecam = (FreecamModule) ModuleRegistry.get(FreecamModule.class);
        if (freecam != null && freecam.isEnabled()) {
            class_4184 camera = mc.field_1773.method_19418();
            origin = camera.method_71156();
            direction = class_243.method_1030(camera.method_19329(), camera.method_19330());
        } else {
            origin = player.method_5836(1.0f);
            direction = player.method_5828(1.0f);
        }
        class_243 end = origin.method_1019(direction.method_1021(this.range.get()));
        class_3959.class_242 fluids = this.stopAtFluids.get() ? class_3959.class_242.field_1347 : class_3959.class_242.field_1348;
        class_3959 context = new class_3959(origin, end, class_3959.class_3960.field_17559, fluids, player);
        class_2338 viewBlock = class_2338.method_49638(origin);
        if (startsInside(context, level, viewBlock, origin)) {
            Optional<class_243> exit = new class_238(viewBlock).method_992(end, origin);
            if (exit.isEmpty()) {
                return null;
            }
            class_243 start = exit.get().method_1019(direction.method_1021(STEP_PAST_BLOCK));
            context = new class_3959(start, end, class_3959.class_3960.field_17559, fluids, player);
        }
        class_3965 hit = level.method_17742(context);
        if (hit.method_17783() == class_239.class_240.field_1332) {
            return hit.method_17777();
        }
        return null;
    }

    private static boolean startsInside(class_3959 context, class_638 level, class_2338 pos, class_243 point) {
        return contains(context.method_17748(level.method_8320(pos), level, pos), pos, point) || contains(context.method_17749(level.method_8316(pos), level, pos), pos, point);
    }

    private static boolean contains(class_265 shape, class_2338 pos, class_243 point) {
        for (class_238 box : shape.method_1090()) {
            if (box.method_996(pos).method_1006(point)) {
                return true;
            }
        }
        return false;
    }

    private String formatCoords(class_2338 pos, class_5321<class_1937> dimension) {
        int x = pos.method_10263();
        int y = pos.method_10264();
        int z = pos.method_10260();
        boolean withDimension = this.includeDimension.get();
        switch (this.format.get()) {
            case "/tp Command":
                return withDimension ? String.format(Locale.ROOT, "/execute in %s run tp @s %d %d %d", dimension.method_29177(), Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z)) : String.format(Locale.ROOT, "/tp %d %d %d", Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z));
            case "X, Y, Z":
                return String.format(Locale.ROOT, "%d, %d, %d", Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z)) + dimensionSuffix(dimension, withDimension);
            default:
                return String.format(Locale.ROOT, "%d %d %d", Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z)) + dimensionSuffix(dimension, withDimension);
        }
    }

    private static String dimensionSuffix(class_5321<class_1937> dimension, boolean enabled) {
        return enabled ? " (" + dimensionName(dimension) + ")" : "";
    }

    private static String dimensionName(class_5321<class_1937> dimension) {
        if (class_1937.field_25179.equals(dimension)) {
            return "Overworld";
        }
        if (class_1937.field_25180.equals(dimension)) {
            return "Nether";
        }
        return class_1937.field_25181.equals(dimension) ? "End" : dimension.method_29177().toString();
    }
}
