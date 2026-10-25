package net.anubis.client.gui;

import com.mojang.blaze3d.textures.GpuTexture;
import imgui.ImDrawList;
import imgui.ImFont;
import imgui.flag.ImGuiWindowFlags;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import net.anubis.client.config.Surface;
import net.anubis.client.imgui.ImGuiManager;
import net.anubis.client.module.impl.client.HudModule;
import net.anubis.client.module.impl.misc.NameProtectModule;
import net.anubis.client.util.combat.CombatTarget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10868;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1531;
import net.minecraft.class_156;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1826;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_742;
import net.minecraft.class_8685;
import org.lwjgl.opengl.GL11;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/TargetCard.class */
@Environment(EnvType.CLIENT)
final class TargetCard {
    private static final long COMBAT_MEMORY_MS = 4000;
    private static final float LINGER = 0.8f;
    private static final float SHOW_TIME = 0.2f;
    private static final float HIDE_TIME = 0.34f;
    private static final float ENTER_LIFT = 7.0f;
    private static final float HEALTH_RATE = 13.0f;
    private static final float TRAIL_HOLD = 0.4f;
    private static final float TRAIL_RATE = 4.5f;
    private static final float STRIKE_TIME = 0.45f;
    private static final long NAME_REFRESH_MS = 1000;
    private static final float PAD = 8.0f;
    private static final float PORTRAIT = 40.0f;
    private static final float FACE = 32.0f;
    private static final float CONTENT_H = 46.0f;
    private static final float COLUMN_GAP = 10.0f;
    private static final float COLUMN_W = 178.0f;
    private static final float NAME_ROW = 18.0f;
    private static final float BAR_TOP = 22.0f;
    private static final float BAR_H = 6.0f;
    private static final float GEAR = 16.0f;
    private static final float GEAR_GAP = 3.0f;
    private static final float ANKH_H = 11.0f;
    private static final float ANKH_GAP = 4.0f;
    private static final float CLUSTER_GAP = 8.0f;
    private static final int MAT = 1493172224;
    private static final int NOTCH = 1191182336;
    private static final String ELLIPSIS = "...";
    private static final float SKIN_FACE_U = 0.125f;
    private static final float SKIN_HAT_U = 0.625f;
    private static final float SKIN_TOP_V = 0.125f;
    private static final float SKIN_PART = 0.125f;
    private static final class_1304[] GEAR_ORDER = {class_1304.field_6173, class_1304.field_6171, class_1304.field_6169, class_1304.field_6174, class_1304.field_6172, class_1304.field_6166};
    private class_1309 target;
    private float presence;
    private float lost;
    private float health;
    private float trail;
    private float trailHold;
    private float absorb;
    private float lastHealth;
    private int lastHurt;
    private float strike;
    private float clock;
    private class_1309 namedFor;
    private long namedAt;
    private int nameGeneration = ImGuiWindowFlags.NavFlattened;
    private String rawName = "";
    private String fittedName = "";
    private int fittedWidth = -1;
    private int fittedPx = -1;
    private int distanceTenths = ImGuiWindowFlags.NavFlattened;
    private String distanceText = "";
    private int healthTenths = ImGuiWindowFlags.NavFlattened;
    private String healthText = "";
    private int absorbWhole = ImGuiWindowFlags.NavFlattened;
    private String absorbText = "";
    private final Set<GpuTexture> crispSkins = Collections.newSetFromMap(new WeakHashMap());

    TargetCard() {
    }

    void reset() {
        this.target = null;
        this.namedFor = null;
        this.presence = 0.0f;
        this.lost = 0.0f;
    }

    boolean visible() {
        return this.target != null && this.presence > 0.0f;
    }

    float planWidth(float s) {
        if (visible()) {
            return Math.round(244.0f * s);
        }
        return 1.0f;
    }

    float planHeight(float s) {
        return Math.round(62.0f * s);
    }

    void update(class_310 mc, HudModule hud, boolean active, boolean editMode, float dt) {
        this.clock += dt;
        if (this.target != null && (this.target.method_73183() != mc.field_1687 || (!editMode && this.target == mc.field_1724))) {
            reset();
        }
        class_1309 next = active ? pick(mc, hud) : null;
        if (next == null && editMode) {
            next = mc.field_1724;
        }
        if (next != null && next != this.target) {
            adopt(next);
        }
        this.lost = next != null ? 0.0f : this.lost + dt;
        boolean wanted = this.target != null && (next != null || this.lost < LINGER);
        if (editMode && wanted) {
            this.presence = 1.0f;
        } else {
            this.presence = class_3532.method_15363(this.presence + (wanted ? dt / SHOW_TIME : (-dt) / HIDE_TIME), 0.0f, 1.0f);
        }
        if (this.presence <= 0.0f && !wanted) {
            this.target = null;
            this.namedFor = null;
        } else if (this.target != null) {
            follow(dt);
        }
    }

    private static class_1309 pick(class_310 mc, HudModule hud) {
        class_1309 entity = hud.targetOnLook() ? CombatTarget.current(4000L) : CombatTarget.lastHit(4000L);
        if (entity == null || entity == mc.field_1724 || (entity instanceof class_1531)) {
            return null;
        }
        if (!hud.targetPlayersOnly() || (entity instanceof class_1657)) {
            return entity;
        }
        return null;
    }

    private void adopt(class_1309 next) {
        this.target = next;
        float max = Math.max(1.0f, next.method_6063());
        float fMethod_15363 = class_3532.method_15363(next.method_6032(), 0.0f, max);
        this.lastHealth = fMethod_15363;
        this.trail = fMethod_15363;
        this.health = fMethod_15363;
        this.absorb = Math.max(0.0f, next.method_6067());
        this.trailHold = 0.0f;
        this.strike = 0.0f;
        this.lastHurt = next.field_6235;
    }

    private void follow(float dt) {
        float max = Math.max(1.0f, this.target.method_6063());
        float hp = class_3532.method_15363(this.target.method_6032(), 0.0f, max);
        if (hp < this.lastHealth - 0.01f) {
            this.trailHold = TRAIL_HOLD;
            this.strike = 1.0f;
        }
        if (this.target.field_6235 > this.lastHurt) {
            this.strike = 1.0f;
        }
        this.lastHurt = this.target.field_6235;
        this.lastHealth = hp;
        this.health = HudGlyphs.approach(this.health, hp, 13.0f, dt);
        if (this.trailHold > 0.0f) {
            this.trailHold -= dt;
        } else {
            this.trail = HudGlyphs.approach(this.trail, this.health, TRAIL_RATE, dt);
        }
        if (this.trail < this.health) {
            this.trail = this.health;
        }
        this.absorb = HudGlyphs.approach(this.absorb, Math.max(0.0f, this.target.method_6067()), 13.0f, dt);
        this.strike = Math.max(0.0f, this.strike - (dt / STRIKE_TIME));
    }

    void draw(ImDrawList dl, HudLayoutStore.HudElementState state, float x, float y, float alpha, boolean shadow, HudModule hud) {
        if (this.target == null) {
            return;
        }
        float shown = HudGlyphs.easeOutQuint(this.presence);
        float a = alpha * shown;
        if (a <= 0.004f) {
            return;
        }
        class_310 mc = class_310.method_1551();
        float s = HudRenderer.scaleOf(state);
        float y2 = Math.round(y + (ENTER_LIFT * s * (1.0f - shown)));
        float w = state.width;
        float h = state.height;
        Surface surface = HudRenderer.hudSurface(state);
        boolean guard = HudChrome.needsTextGuard(surface);
        HudChrome.card(dl, surface, x, y2, w, h, HudRenderer.hudRounding() * s, a, shadow, s);
        ImGuiManager fonts = ImGuiManager.getInstance();
        ImFont textFont = fonts.hudTextFont();
        ImFont labelFont = fonts.hudLabelFont();
        int textPx = HudChrome.px(17.0f, s);
        int labelPx = HudChrome.px(14.0f, s);
        float left = x + Math.round(8.0f * s);
        float top = y2 + Math.round(8.0f * s);
        float frame = Math.round(40.0f * s);
        drawPortrait(dl, mc, left, top + Math.round(((CONTENT_H * s) - frame) * 0.5f), frame, s, a);
        float colLeft = left + frame + Math.round(COLUMN_GAP * s);
        float colRight = (x + w) - Math.round(8.0f * s);
        float max = Math.max(1.0f, this.target.method_6063());
        float fraction = class_3532.method_15363(this.health / max, 0.0f, 1.0f);
        float textTop = top + ((float) Math.floor(((18.0f * s) - textPx) * 0.5f));
        float labelTop = textTop + HudChrome.baselineOffset(textFont, textPx, labelFont, labelPx);
        refreshNumbers(mc);
        float absorbW = this.absorbText.isEmpty() ? 0.0f : HudChrome.width(labelFont, labelPx, this.absorbText);
        float healthW = HudChrome.tabularWidth(textFont, textPx, this.healthText);
        float ankhH = 11.0f * s;
        float clusterW = (ankhH * 0.62f) + (ANKH_GAP * s) + healthW + (absorbW > 0.0f ? (ANKH_GAP * s) + absorbW : 0.0f);
        float cursor = colRight - clusterW;
        HudGlyphs.ankh(dl, cursor + (ankhH * 0.31f), textTop + (textPx * 0.52f), ankhH, Math.max(1.0f, 1.3f * s), HudGlyphs.ink(EgyptDraw.GOLD, a));
        float cursor2 = cursor + (ankhH * 0.62f) + (ANKH_GAP * s);
        HudChrome.tabular(dl, textFont, textPx, cursor2, textTop, HudGlyphs.ink(HudGlyphs.vitality(fraction), a), this.healthText, guard);
        if (absorbW > 0.0f) {
            HudChrome.text(dl, labelFont, labelPx, cursor2 + healthW + (ANKH_GAP * s), labelTop, HudGlyphs.ink(EgyptDraw.LAPIS_LIGHT, a), this.absorbText, guard);
        }
        String name = fittedName(textFont, textPx, ((colRight - clusterW) - (8.0f * s)) - colLeft);
        HudChrome.text(dl, textFont, textPx, colLeft, textTop, HudChrome.scaleAlpha(HudChrome.TEXT_NAME, a), name, guard);
        float barTop = top + Math.round(22.0f * s);
        drawBar(dl, colLeft, barTop, colRight, Math.max(GEAR_GAP, Math.round(BAR_H * s)), max, s, a);
        int gearPx = (int) ItemIcons.pixels(16.0f * s);
        float gearTop = (top + Math.round(CONTENT_H * s)) - gearPx;
        float distanceW = HudChrome.width(labelFont, labelPx, this.distanceText);
        float distanceX = colRight - distanceW;
        HudChrome.text(dl, labelFont, labelPx, distanceX, gearTop + Math.round((gearPx - labelPx) * 0.5f), HudChrome.scaleAlpha(HudChrome.TEXT_LABEL, a), this.distanceText, guard);
        if (hud.targetGear()) {
            drawGear(dl, colLeft, gearTop, gearPx, distanceX - (BAR_H * s), s, a);
        }
        float rule = Math.max(1.0f, Math.round(s));
        HudGlyphs.register(dl, x + (w * 0.12f), x + (w * 0.88f), ((y2 + h) - rule) - Math.max(1.0f, Math.round(2.0f * s)), rule, HudGlyphs.ink(EgyptDraw.GOLD, a * 0.35f));
    }

    private void drawPortrait(ImDrawList dl, class_310 mc, float fx, float fy, float size, float s, float a) {
        float r = Math.min(HudRenderer.hudRounding(), BAR_H) * s;
        dl.addRectFilled(fx, fy, fx + size, fy + size, HudChrome.scaleAlpha(MAT, a), r);
        float face = Math.max(8.0f, Math.round((32.0f * s) / 8.0f) * 8.0f);
        if (face > size - ANKH_GAP) {
            face = Math.max(8.0f, face - 8.0f);
        }
        float ix = Math.round(fx + ((size - face) * 0.5f));
        float iy = Math.round(fy + ((size - face) * 0.5f));
        boolean drawn = false;
        class_1309 class_1309Var = this.target;
        if (class_1309Var instanceof class_742) {
            class_742 player = (class_742) class_1309Var;
            int texture = skinTexture(mc, player);
            if (texture != 0) {
                float fr = Math.max(1.0f, Math.round(2.0f * s));
                int tint = HudChrome.scaleAlpha(-1, a);
                dl.addImageRounded(texture, ix, iy, ix + face, iy + face, 0.125f, 0.125f, 0.25f, 0.25f, tint, fr, 240);
                dl.addImageRounded(texture, ix, iy, ix + face, iy + face, SKIN_HAT_U, 0.125f, 0.75f, 0.25f, tint, fr, 240);
                drawn = true;
            }
        } else {
            class_1826 egg = class_1826.method_8019(this.target.method_5864());
            if (egg != null) {
                float shown = ItemIcons.pixels(face);
                float ex = Math.round(fx + ((size - shown) * 0.5f));
                float ey = Math.round(fy + ((size - shown) * 0.5f));
                ItemIcons.draw(dl, (class_1792) egg, ex, ey, shown, a);
                drawn = true;
            }
        }
        if (!drawn) {
            HudGlyphs.horusEye(dl, fx + (size * 0.5f), fy + (size * 0.46f), size * 0.3f, Math.max(1.0f, 1.4f * s), HudGlyphs.ink(EgyptDraw.GOLD, a * 0.85f));
        }
        int edge = HudGlyphs.ink(EgyptDraw.mix(EgyptDraw.GOLD, EgyptDraw.CARNELIAN, this.strike), a * (0.7f + (0.3f * this.strike)));
        dl.addRect(fx, fy, fx + size, fy + size, edge, r, 0, Math.max(1.0f, s));
        float tie = Math.max(1.0f, Math.round(1.5f * s));
        float tieY = fy + size + Math.max(1.0f, Math.round(s));
        dl.addRectFilled(fx + (size * 0.22f), tieY, fx + (size * 0.78f), tieY + tie, HudGlyphs.ink(EgyptDraw.GOLD, a * LINGER), tie * 0.5f);
        if (this.strike > 0.0f) {
            float grow = (1.0f - this.strike) * BAR_H * s;
            dl.addRect(fx - grow, fy - grow, fx + size + grow, fy + size + grow, HudGlyphs.ink(EgyptDraw.CARNELIAN, a * this.strike * 0.7f), r + grow, 0, Math.max(1.0f, 1.5f * s));
        }
    }

    private void drawBar(ImDrawList dl, float l, float t, float r, float bh, float max, float s, float a) {
        float f;
        float bw = r - l;
        float round = bh * 0.5f;
        float fraction = class_3532.method_15363(this.health / max, 0.0f, 1.0f);
        float trailFraction = class_3532.method_15363(this.trail / max, 0.0f, 1.0f);
        dl.addRectFilled(l, t, r, t + bh, HudChrome.scaleAlpha(452984831, a), round);
        if (trailFraction > fraction + 0.002f) {
            float tr = l + (bw * trailFraction);
            dl.addRectFilled(l, t, tr, t + bh, HudGlyphs.ink(EgyptDraw.GOLD_LIGHT, a * 0.55f), Math.min(round, (tr - l) * 0.5f));
        }
        if (fraction > 0.0f) {
            float fr = l + (bw * fraction);
            float fill = a;
            if (fraction < 0.25f) {
                fill *= 0.72f + (0.28f * (0.5f + (0.5f * ((float) Math.sin(this.clock * 7.5f)))));
            }
            float corner = Math.min(round, (fr - l) * 0.5f);
            dl.addRectFilled(l, t, fr, t + bh, HudGlyphs.ink(HudGlyphs.vitality(fraction), fill), corner);
            dl.addRectFilled(l, t, fr, t + (bh * STRIKE_TIME), HudGlyphs.ink(-1, fill * 0.16f), corner, 48);
        }
        if (max <= 40.0f) {
            f = 2.0f;
        } else {
            f = max <= 200.0f ? COLUMN_GAP : max / 20.0f;
        }
        float step = f;
        float notch = Math.max(1.0f, Math.round(0.75f * s));
        int notchInk = HudChrome.scaleAlpha(NOTCH, a);
        float f2 = step;
        while (true) {
            float mark = f2;
            if (mark >= max - 0.01f) {
                break;
            }
            float nx = Math.round(l + ((bw * mark) / max));
            dl.addRectFilled(nx, t, nx + notch, t + bh, notchInk);
            f2 = mark + step;
        }
        float absorbFraction = class_3532.method_15363(this.absorb / max, 0.0f, 1.0f);
        if (absorbFraction > 0.002f) {
            float ar = l + (bw * absorbFraction);
            dl.addRectFilled(l, t, ar, t + Math.max(1.5f, bh * STRIKE_TIME), HudGlyphs.ink(EgyptDraw.LAPIS_LIGHT, a * 0.95f), Math.min(round, (ar - l) * 0.5f), 48);
        }
    }

    private void drawGear(ImDrawList dl, float gx, float gy, int gearPx, float limit, float s, float a) {
        float step = gearPx + Math.round(GEAR_GAP * s);
        for (class_1304 slot : GEAR_ORDER) {
            class_1799 stack = this.target.method_6118(slot);
            if (!stack.method_7960()) {
                if (gx + gearPx <= limit) {
                    ItemIcons.draw(dl, stack, gx, gy, gearPx, a);
                    if (stack.method_7958()) {
                        HudGlyphs.sparkle(dl, (gx + gearPx) - (1.5f * s), gy + (1.5f * s), 2.6f * s, HudGlyphs.ink(EgyptDraw.GOLD_LIGHT, a * 0.9f));
                    }
                    if (stack.method_7963() && stack.method_7936() > 0) {
                        float left = 1.0f - (stack.method_7919() / stack.method_7936());
                        if (left < 0.25f) {
                            float line = Math.max(1.0f, Math.round(1.5f * s));
                            dl.addRectFilled(gx + 1.0f, gy + gearPx + (line * 0.5f), (gx + gearPx) - 1.0f, gy + gearPx + (line * 1.5f), HudGlyphs.ink(EgyptDraw.CARNELIAN, a), line * 0.5f);
                        }
                    }
                    gx += step;
                } else {
                    return;
                }
            }
        }
    }

    private void refreshNumbers(class_310 mc) {
        float max = Math.max(1.0f, this.target.method_6063());
        int tenths = Math.round(class_3532.method_15363(this.target.method_6032(), 0.0f, max) * COLUMN_GAP);
        if (tenths != this.healthTenths) {
            this.healthTenths = tenths;
            this.healthText = tenths % 10 == 0 ? Integer.toString(tenths / 10) : (tenths / 10) + "." + (tenths % 10);
        }
        int whole = Math.round(Math.max(0.0f, this.target.method_6067()));
        if (whole != this.absorbWhole) {
            this.absorbWhole = whole;
            this.absorbText = whole > 0 ? "+" + whole : "";
        }
        int distance = mc.field_1724 == null ? 0 : Math.round(mc.field_1724.method_5739(this.target) * COLUMN_GAP);
        if (distance != this.distanceTenths) {
            this.distanceTenths = distance;
            this.distanceText = (distance / 10) + "." + (distance % 10) + "m";
        }
    }

    private String fittedName(ImFont font, int fontPx, float maxW) {
        int generation = NameProtectModule.generation();
        long now = class_156.method_658();
        if (this.target != this.namedFor || generation != this.nameGeneration || now - this.namedAt > NAME_REFRESH_MS) {
            this.namedFor = this.target;
            this.nameGeneration = generation;
            this.namedAt = now;
            String fresh = NameProtectModule.protect(this.target.method_5477()).getString();
            if (!fresh.equals(this.rawName)) {
                this.rawName = fresh;
                this.fittedWidth = -1;
            }
        }
        int budget = Math.max(0, (int) maxW);
        if (budget == this.fittedWidth && fontPx == this.fittedPx) {
            return this.fittedName;
        }
        this.fittedWidth = budget;
        this.fittedPx = fontPx;
        this.fittedName = fit(font, fontPx, this.rawName, budget);
        return this.fittedName;
    }

    private static String fit(ImFont font, int fontPx, String name, float maxW) {
        if (HudChrome.width(font, fontPx, name) <= maxW) {
            return name;
        }
        float dots = HudChrome.width(font, fontPx, ELLIPSIS);
        int lo = 0;
        int hi = name.length();
        while (lo < hi) {
            int mid = ((lo + hi) + 1) >>> 1;
            if (HudChrome.width(font, fontPx, name.substring(0, mid)) + dots <= maxW) {
                lo = mid;
            } else {
                hi = mid - 1;
            }
        }
        return lo == 0 ? "" : name.substring(0, lo).stripTrailing() + "...";
    }

    private int skinTexture(class_310 mc, class_742 player) {
        class_8685 skin = player.method_52814();
        if (NameProtectModule.hidesName(player.method_7334().name())) {
            skin = NameProtectModule.hiddenHead(player.method_5667(), skin);
        }
        class_2960 path = skin.comp_1626().comp_3627();
        try {
            GpuTexture texture = mc.method_1531().method_4619(path).method_68004();
            if (!(texture instanceof class_10868)) {
                return 0;
            }
            class_10868 gl = (class_10868) texture;
            if (gl.isClosed()) {
                return 0;
            }
            if (this.crispSkins.add(texture)) {
                int bound = GL11.glGetInteger(32873);
                GL11.glBindTexture(3553, gl.method_68427());
                GL11.glTexParameteri(3553, 10241, 9728);
                GL11.glTexParameteri(3553, 10240, 9728);
                GL11.glBindTexture(3553, bound);
            }
            return gl.method_68427();
        } catch (IllegalStateException e) {
            return 0;
        }
    }
}
