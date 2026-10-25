package net.anubis.client.module.impl.render;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import net.anubis.client.event.EventHandler;
import net.anubis.client.event.impl.Render2DEvent;
import net.anubis.client.gui.WorldLabel;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.util.NameFilter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1542;
import net.minecraft.class_1799;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4184;
import net.minecraft.class_640;
import net.minecraft.class_742;
import net.minecraft.class_746;
import org.joml.Matrix3x2fStack;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/NametagsModule.class */
@Environment(EnvType.CLIENT)
public class NametagsModule extends ClientModule {
    private static final int PANEL_RGB = 1447965;
    private static final int BORDER = 402653183;
    private static final int SHADOW_NEAR = 771751936;
    private static final int SHADOW_FAR = 385875968;
    private static final int DIVIDER = 536870911;
    private static final int EMPTY_SLOT = 452984831;
    private static final int TEXT_NAME = -1447442;
    private static final int TEXT_LABEL = -6052949;
    private static final int TEXT_VALUE = -657929;
    private static final int STATUS_GOOD = -13577896;
    private static final int STATUS_WARN = -10742;
    private static final int STATUS_BAD = -47814;
    private static final int ITEM_COUNT_COLOR = -10742;
    private static final float TEXT_GUARD_BELOW = 0.55f;
    private static final class_1304[] ARMOR_SLOTS = {class_1304.field_6169, class_1304.field_6174, class_1304.field_6172, class_1304.field_6166};
    private static final int PADDING = 4;
    private static final int ROW_GAP = 3;
    private static final int ICON_SIZE = 10;
    private static final int ICON_GAP = 2;
    private static final int TEXT_GAP = 4;
    private static final int SECTION_GAP = 3;
    private static final float ICON_SCALE = 0.625f;
    private static final int TAG_CAP = 64;
    private static final double FULL_SIZE_DISTANCE = 8.0d;
    private final BooleanSetting sPing;
    private final BooleanSetting sHeldItems;
    private final BooleanSetting sArmor;
    private final BooleanSetting sDurability;
    private final BooleanSetting sHealth;
    private final BooleanSetting sHideVanilla;
    private final BooleanSetting sItems;
    private final NumberSetting sRange;
    private final NumberSetting sMaxTags;
    private final NumberSetting sScale;
    private final NumberSetting sMinScale;
    private final NumberSetting sBackgroundAlpha;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/NametagsModule$Nearby.class */
    @Environment(EnvType.CLIENT)
    private static final class Nearby extends Record {
        private final class_1297 entity;
        private final double distanceSq;

        private Nearby(class_1297 entity, double distanceSq) {
            this.entity = entity;
            this.distanceSq = distanceSq;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Nearby.class), Nearby.class, "entity;distanceSq", "FIELD:Lnet/anubis/client/module/impl/render/NametagsModule$Nearby;->entity:Lnet/minecraft/class_1297;", "FIELD:Lnet/anubis/client/module/impl/render/NametagsModule$Nearby;->distanceSq:D").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Nearby.class), Nearby.class, "entity;distanceSq", "FIELD:Lnet/anubis/client/module/impl/render/NametagsModule$Nearby;->entity:Lnet/minecraft/class_1297;", "FIELD:Lnet/anubis/client/module/impl/render/NametagsModule$Nearby;->distanceSq:D").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Nearby.class, Object.class), Nearby.class, "entity;distanceSq", "FIELD:Lnet/anubis/client/module/impl/render/NametagsModule$Nearby;->entity:Lnet/minecraft/class_1297;", "FIELD:Lnet/anubis/client/module/impl/render/NametagsModule$Nearby;->distanceSq:D").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public class_1297 entity() {
            return this.entity;
        }

        public double distanceSq() {
            return this.distanceSq;
        }
    }

    public NametagsModule() {
        super("nametags", "Nametags", "Player nametags with held items, armour, health, ping and distance.", ModuleCategory.RENDER);
        this.sPing = (BooleanSetting) addSetting("ping", new BooleanSetting("Ping", true));
        this.sHeldItems = (BooleanSetting) addSetting("held-items", new BooleanSetting("Held Items", true));
        this.sArmor = (BooleanSetting) addSetting("armor", new BooleanSetting("Armor", true));
        this.sDurability = (BooleanSetting) addSetting("durability", new BooleanSetting("Durability", true));
        this.sHealth = (BooleanSetting) addSetting("health", new BooleanSetting("Health", true));
        this.sHideVanilla = (BooleanSetting) addSetting("hide-vanilla", new BooleanSetting("Hide Vanilla", true));
        this.sItems = (BooleanSetting) addSetting("items", new BooleanSetting("Dropped Items", true));
        this.sRange = (NumberSetting) addSetting("range", new NumberSetting("Range", 128.0f, 8.0f, 512.0f).step(1.0f));
        this.sMaxTags = (NumberSetting) addSetting("max-tags", new NumberSetting("Max Tags", 64.0f, 1.0f, 64.0f).step(1.0f));
        this.sScale = (NumberSetting) addSetting("scale", new NumberSetting("Scale", 1.0f, 0.5f, 2.0f).step(0.1f));
        this.sMinScale = (NumberSetting) addSetting("min-scale", new NumberSetting("Min Scale", TEXT_GUARD_BELOW, 0.3f, 1.0f).step(0.05f));
        this.sBackgroundAlpha = (NumberSetting) addSetting("background-alpha", new NumberSetting("Background Alpha", 0.45f, 0.0f, 1.0f).step(0.05f));
        this.sDurability.visibleWhen(() -> {
            return this.sHeldItems.get() || this.sArmor.get();
        });
    }

    public static boolean hideVanillaNametags() {
        NametagsModule module = (NametagsModule) ModuleRegistry.get(NametagsModule.class);
        return module != null && module.isEnabled() && module.sHideVanilla.get();
    }

    @EventHandler
    public void onRender2D(Render2DEvent event) {
        class_4184 camera;
        class_310 mc = class_310.method_1551();
        if (mc.field_1687 == null || mc.field_1724 == null || mc.field_1690.field_1842 || (camera = mc.field_1773.method_19418()) == null || !camera.method_19332()) {
            return;
        }
        float partialTick = event.partialTick;
        class_243 eye = mc.field_1724.method_5836(partialTick);
        double range = Math.max(1.0f, this.sRange.get());
        double rangeSq = range * range;
        int budget = Math.max(1, Math.min(64, this.sMaxTags.getInt()));
        List<Nearby> players = new ArrayList<>();
        for (class_746 class_746Var : mc.field_1687.method_18456()) {
            if (class_746Var != mc.field_1724 && !class_746Var.method_31481() && !class_746Var.method_29504()) {
                double distanceSq = class_746Var.method_30950(partialTick).method_1025(eye);
                if (distanceSq <= rangeSq) {
                    players.add(new Nearby(class_746Var, distanceSq));
                }
            }
        }
        List<Nearby> players2 = nearest(players, budget);
        List<Nearby> items = new ArrayList();
        int itemBudget = budget - players2.size();
        if (this.sItems.get() && itemBudget > 0) {
            for (class_1542 class_1542Var : mc.field_1687.method_18112()) {
                if (class_1542Var instanceof class_1542) {
                    class_1542 item = class_1542Var;
                    if (!item.method_31481() && !item.method_6983().method_7960()) {
                        double distanceSq2 = item.method_30950(partialTick).method_1025(eye);
                        if (distanceSq2 <= rangeSq) {
                            items.add(new Nearby(item, distanceSq2));
                        }
                    }
                }
            }
            items = nearest(items, itemBudget);
        }
        for (int i = items.size() - 1; i >= 0; i--) {
            renderItemTag(event.graphics, mc, camera, (class_1542) items.get(i).entity(), partialTick);
        }
        for (int i2 = players2.size() - 1; i2 >= 0; i2--) {
            renderTag(event.graphics, mc, camera, (class_742) players2.get(i2).entity(), partialTick);
        }
    }

    private static List<Nearby> nearest(List<Nearby> candidates, int limit) {
        candidates.sort(Comparator.comparingDouble((v0) -> {
            return v0.distanceSq();
        }));
        return candidates.size() <= limit ? candidates : candidates.subList(0, limit);
    }

    private void renderTag(class_332 graphics, class_310 mc, class_4184 camera, class_742 player, float partialTick) {
        int i;
        class_640 info;
        int i2;
        class_243 anchor = player.method_30950(partialTick).method_1031(0.0d, ((double) player.method_17682()) + 0.15d, 0.0d);
        float[] screen = WorldLabel.project(mc, anchor, graphics.method_51421(), graphics.method_51443());
        if (screen == null) {
            return;
        }
        class_327 font = mc.field_1772;
        String name = NameFilter.apply(player.method_7334().name());
        String pingText = null;
        int pingColor = TEXT_LABEL;
        if (this.sPing.get() && mc.method_1562() != null && (info = mc.method_1562().method_2871(player.method_5667())) != null) {
            int latency = info.method_2959();
            pingText = latency + "ms";
            if (latency < 60) {
                i2 = STATUS_GOOD;
            } else {
                i2 = latency < 130 ? -10742 : STATUS_BAD;
            }
            pingColor = i2;
        }
        List<class_1799> hands = this.sHeldItems.get() ? heldItems(player) : List.of();
        List<class_1799> armor = this.sArmor.get() ? wornArmor(player) : List.of();
        String healthText = null;
        int healthColor = STATUS_GOOD;
        float health = player.method_6032() + player.method_6067();
        if (this.sHealth.get() && Float.isFinite(health)) {
            float fraction = Math.min(1.0f, health / Math.max(1.0f, player.method_6063()));
            healthText = String.valueOf((int) Math.ceil(health));
            if (fraction > 0.6f) {
                i = STATUS_GOOD;
            } else {
                i = fraction > 0.3f ? -10742 : STATUS_BAD;
            }
            healthColor = i;
        }
        double dist = anchor.method_1022(camera.method_71156());
        String distText = Math.round((float) dist) + "m";
        int handsW = iconsWidth(hands.size());
        int armorW = iconsWidth(armor.size());
        int healthW = healthText != null ? font.method_1727(healthText) : 0;
        int distW = font.method_1727(distText);
        int row1W = font.method_1727(name) + (pingText != null ? 5 + font.method_1727(pingText) : 0);
        int row2W = extend(0, handsW, 0);
        int contentW = Math.max(row1W, extend(extend(extend(row2W, armorW, 7), healthW, 4), distW, 4));
        int panelW = contentW + 8;
        Objects.requireNonNull(font);
        int panelH = 8 + 9 + 3 + 10;
        float baseScale = Math.max(0.5f, Math.min(2.0f, this.sScale.get()));
        double minScale = Math.max(0.3f, Math.min(1.0f, this.sMinScale.get()));
        float distScale = (float) Math.min(1.0d, Math.max(minScale, FULL_SIZE_DISTANCE / Math.max(1.0d, dist)));
        float scale = baseScale * distScale;
        float surface = Math.max(0.0f, Math.min(1.0f, this.sBackgroundAlpha.get()));
        boolean guard = surface < TEXT_GUARD_BELOW;
        float chrome = Math.min(1.0f, surface * 2.0f);
        Matrix3x2fStack pose = graphics.method_51448();
        pose.pushMatrix();
        pose.translate(screen[0], screen[1]);
        pose.scale(scale, scale);
        int x = (-panelW) / 2;
        int y = (-panelH) - 1;
        drawPanel(graphics, x, y, x + panelW, y + panelH, surface, chrome);
        int left = x + 4;
        int textY = y + 4;
        graphics.method_51433(font, name, left, textY, TEXT_NAME, guard);
        if (pingText != null) {
            graphics.method_51433(font, pingText, left + font.method_1727(name) + 5, textY, pingColor, guard);
        }
        Objects.requireNonNull(font);
        int rowY = y + 4 + 9 + 3;
        Objects.requireNonNull(font);
        int labelY = rowY + ((10 - 9) / 2) + 1;
        int cursor = left;
        boolean placed = false;
        if (!hands.isEmpty()) {
            cursor = drawIcons(graphics, font, player, hands, 0, cursor, rowY, true, chrome);
            placed = true;
        }
        if (!armor.isEmpty()) {
            if (placed) {
                int lineX = cursor + 3;
                graphics.method_25294(lineX, rowY + 1, lineX + 1, (rowY + 10) - 1, scaleAlpha(DIVIDER, chrome));
                cursor += 7;
            }
            cursor = drawIcons(graphics, font, player, armor, hands.size(), cursor, rowY, false, chrome);
            placed = true;
        }
        if (healthText != null) {
            if (placed) {
                cursor += 4;
            }
            graphics.method_51433(font, healthText, cursor, labelY, healthColor, guard);
            cursor += healthW;
            placed = true;
        }
        if (placed) {
            cursor += 4;
        }
        graphics.method_51433(font, distText, cursor, labelY, TEXT_LABEL, guard);
        pose.popMatrix();
    }

    private static List<class_1799> heldItems(class_742 player) {
        class_1799 main = player.method_6047();
        class_1799 off = player.method_6079();
        return (main.method_7960() && off.method_7960()) ? List.of() : List.of(main, off);
    }

    private static List<class_1799> wornArmor(class_742 player) {
        List<class_1799> worn = new ArrayList<>(ARMOR_SLOTS.length);
        for (class_1304 slot : ARMOR_SLOTS) {
            class_1799 stack = player.method_6118(slot);
            if (!stack.method_7960()) {
                worn.add(stack);
            }
        }
        return worn;
    }

    private static int iconsWidth(int count) {
        if (count == 0) {
            return 0;
        }
        return (count * 12) - 2;
    }

    private static int extend(int total, int width, int gap) {
        if (width <= 0) {
            return total;
        }
        return total == 0 ? width : total + gap + width;
    }

    private int drawIcons(class_332 graphics, class_327 font, class_742 holder, List<class_1799> stacks, int seedOffset, int x, int y, boolean counts, float chrome) {
        for (int i = 0; i < stacks.size(); i++) {
            if (i > 0) {
                x += 2;
            }
            drawIcon(graphics, font, holder, stacks.get(i), (holder.method_5628() * 8) + seedOffset + i, x, y, counts, chrome);
            x += 10;
        }
        return x;
    }

    private void drawIcon(class_332 graphics, class_327 font, class_742 holder, class_1799 stack, int seed, int x, int y, boolean count, float chrome) {
        if (stack.method_7960()) {
            graphics.method_25294(x + 2, y + 2, (x + 10) - 2, (y + 10) - 2, scaleAlpha(EMPTY_SLOT, chrome));
            return;
        }
        Matrix3x2fStack pose = graphics.method_51448();
        pose.pushMatrix();
        pose.translate(x, y);
        pose.scale(ICON_SCALE, ICON_SCALE);
        graphics.method_51423(holder, stack, 0, 0, seed);
        if (this.sDurability.get() && stack.method_31578()) {
            graphics.method_25294(2, 13, 15, 15, -16777216);
            graphics.method_25294(2, 13, 2 + stack.method_31579(), 14, (-16777216) | stack.method_31580());
        }
        if (count && stack.method_7947() > 1) {
            String text = String.valueOf(stack.method_7947());
            graphics.method_51433(font, text, 17 - font.method_1727(text), 9, TEXT_VALUE, true);
        }
        pose.popMatrix();
    }

    private void renderItemTag(class_332 graphics, class_310 mc, class_4184 camera, class_1542 item, float partialTick) {
        class_243 anchor = item.method_30950(partialTick).method_1031(0.0d, 0.35d, 0.0d);
        float[] screen = WorldLabel.project(mc, anchor, graphics.method_51421(), graphics.method_51443());
        if (screen == null) {
            return;
        }
        class_1799 stack = item.method_6983();
        String itemName = stack.method_7964().getString();
        int count = stack.method_7947();
        String countText = count > 1 ? "x" + count : null;
        class_327 font = mc.field_1772;
        int nameW = font.method_1727(itemName);
        int countW = countText != null ? font.method_1727(countText) : 0;
        int totalW = nameW + (countText != null ? 3 + countW : 0);
        double dist = anchor.method_1022(camera.method_71156());
        float scale = (float) Math.min(1.0d, Math.max(0.25d, 6.0d / Math.max(1.0d, dist)));
        Matrix3x2fStack pose = graphics.method_51448();
        pose.pushMatrix();
        pose.translate(screen[0], screen[1]);
        pose.scale(scale, scale);
        int tx = (-totalW) / 2;
        Objects.requireNonNull(font);
        int ty = (-9) / 2;
        graphics.method_51433(font, itemName, tx, ty, TEXT_NAME, true);
        if (countText != null) {
            graphics.method_51433(font, countText, tx + nameW + 3, ty, -10742, true);
        }
        pose.popMatrix();
    }

    private static void drawPanel(class_332 g, int x1, int y1, int x2, int y2, float surface, float chrome) {
        drawR4Rect(g, x1 - 1, y1 + 1, x2 + 1, y2 + 2, scaleAlpha(SHADOW_FAR, chrome));
        drawR4Rect(g, x1, y1 + 1, x2, y2 + 1, scaleAlpha(SHADOW_NEAR, chrome));
        drawR4Rect(g, x1, y1, x2, y2, (Math.round(surface * 255.0f) << 24) | PANEL_RGB);
        drawR4Border(g, x1, y1, x2, y2, scaleAlpha(BORDER, chrome));
    }

    private static int scaleAlpha(int argb, float factor) {
        int alpha = Math.round(((argb >>> 24) & 255) * Math.max(0.0f, Math.min(1.0f, factor)));
        return (alpha << 24) | (argb & 16777215);
    }

    private static void drawR4Rect(class_332 g, int x1, int y1, int x2, int y2, int color) {
        if ((color >>> 24) == 0) {
            return;
        }
        g.method_25294(x1 + 4, y1, x2 - 4, y1 + 1, color);
        g.method_25294(x1 + 2, y1 + 1, x2 - 2, y1 + 2, color);
        g.method_25294(x1 + 1, y1 + 2, x2 - 1, y1 + 4, color);
        g.method_25294(x1, y1 + 4, x2, y2 - 4, color);
        g.method_25294(x1 + 1, y2 - 4, x2 - 1, y2 - 2, color);
        g.method_25294(x1 + 2, y2 - 2, x2 - 2, y2 - 1, color);
        g.method_25294(x1 + 4, y2 - 1, x2 - 4, y2, color);
    }

    private static void drawR4Border(class_332 g, int x1, int y1, int x2, int y2, int color) {
        if ((color >>> 24) == 0) {
            return;
        }
        g.method_25294(x1 + 4, y1, x2 - 4, y1 + 1, color);
        g.method_25294(x1 + 4, y2 - 1, x2 - 4, y2, color);
        g.method_25294(x1, y1 + 4, x1 + 1, y2 - 4, color);
        g.method_25294(x2 - 1, y1 + 4, x2, y2 - 4, color);
        g.method_25294(x1 + 2, y1 + 1, x1 + 4, y1 + 2, color);
        g.method_25294(x1 + 1, y1 + 2, x1 + 2, y1 + 4, color);
        g.method_25294(x2 - 4, y1 + 1, x2 - 2, y1 + 2, color);
        g.method_25294(x2 - 2, y1 + 2, x2 - 1, y1 + 4, color);
        g.method_25294(x1 + 1, y2 - 4, x1 + 2, y2 - 2, color);
        g.method_25294(x1 + 2, y2 - 2, x1 + 4, y2 - 1, color);
        g.method_25294(x2 - 2, y2 - 4, x2 - 1, y2 - 2, color);
        g.method_25294(x2 - 4, y2 - 2, x2 - 2, y2 - 1, color);
    }
}
