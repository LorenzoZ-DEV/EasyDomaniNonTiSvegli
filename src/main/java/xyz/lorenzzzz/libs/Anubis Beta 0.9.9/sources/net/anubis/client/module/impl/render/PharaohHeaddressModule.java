package net.anubis.client.module.impl.render;

import java.util.Arrays;
import net.anubis.client.gui.EgyptDraw;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.util.glyph.GlyphTint;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_9848;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/PharaohHeaddressModule.class */
@Environment(EnvType.CLIENT)
public final class PharaohHeaddressModule extends HeadCosmeticModule {
    private static final String STYLE_NEMES = "Nemes";
    private static final String STYLE_KHEPRESH = "Khepresh";
    private static final String STYLE_SUN_DISC = "Sun Disc";
    private final ModeSetting style;
    private final NumberSetting stripes;
    private final BooleanSetting collar;
    private final NumberSetting size;
    private final ModeSetting color;
    private final ColorSetting gold;
    private final ColorSetting inlay;
    private final ColorSetting gem;
    private final BooleanSetting gemGlow;
    private final BooleanSetting glint;
    private final BooleanSetting friends;
    private final PharaohMesh[] meshes;
    private long styleRevision;
    private long stripesRevision;
    private long collarRevision;

    public PharaohHeaddressModule() {
        super("pharaoh-headdress", "Pharaoh Headdress", "Puts a pharaoh headdress on your player model.");
        this.style = (ModeSetting) addSetting("style", new ModeSetting("Style", STYLE_NEMES, STYLE_NEMES, STYLE_KHEPRESH, STYLE_SUN_DISC));
        this.stripes = (NumberSetting) addSetting("stripes", new NumberSetting("Stripes", 5.0f, 3.0f, 9.0f).step(1.0f));
        this.collar = (BooleanSetting) addSetting("collar", new BooleanSetting("Broad Collar", true));
        this.size = (NumberSetting) addSetting("size", new NumberSetting("Size", 1.0f, 0.8f, 1.3f).step(0.05f));
        this.color = (ModeSetting) addSetting("color", new ModeSetting("Color", GlyphTint.THEME, GlyphTint.THEME, GlyphTint.MANUAL));
        this.gold = (ColorSetting) addSetting("manual-gold", new ColorSetting("Gold Color", -7688204));
        this.inlay = (ColorSetting) addSetting("manual-inlay", new ColorSetting("Inlay Color", EgyptDraw.LAPIS));
        this.gem = (ColorSetting) addSetting("manual-gem", new ColorSetting("Gem Color", -10313473));
        this.gemGlow = (BooleanSetting) addSetting("gem-glow", new BooleanSetting("Gem Glow", true));
        this.glint = (BooleanSetting) addSetting("glint", new BooleanSetting("Glint", true));
        this.friends = (BooleanSetting) addSetting("friends", new BooleanSetting("On Friends", false));
        this.meshes = new PharaohMesh[4];
        this.styleRevision = -1L;
        this.stripesRevision = -1L;
        this.collarRevision = -1L;
        this.stripes.visibleWhen(() -> {
            return this.style.is(STYLE_NEMES);
        });
        this.gold.visibleWhen(() -> {
            return this.color.is(GlyphTint.MANUAL);
        });
        this.inlay.visibleWhen(() -> {
            return this.color.is(GlyphTint.MANUAL);
        });
        this.gem.visibleWhen(() -> {
            return this.color.is(GlyphTint.MANUAL);
        });
    }

    @Override // net.anubis.client.module.impl.render.HeadCosmeticModule
    void reset() {
        super.reset();
        Arrays.fill(this.meshes, (Object) null);
        this.styleRevision = -1L;
    }

    @Override // net.anubis.client.module.impl.render.HeadCosmeticModule
    PharaohMesh headMesh(boolean helmet) {
        refreshMeshes();
        int slot = helmet ? 1 : 0;
        PharaohMesh mesh = this.meshes[slot];
        if (mesh == null) {
            mesh = PharaohMesh.head(styleIndex(), this.stripes.getInt(), helmet);
            this.meshes[slot] = mesh;
        }
        return mesh;
    }

    @Override // net.anubis.client.module.impl.render.HeadCosmeticModule
    PharaohMesh bodyMesh(boolean chest) {
        refreshMeshes();
        int slot = chest ? 3 : 2;
        PharaohMesh mesh = this.meshes[slot];
        if (mesh == null) {
            mesh = PharaohMesh.body(styleIndex(), this.stripes.getInt(), chest, this.collar.get());
            this.meshes[slot] = mesh;
        }
        return mesh;
    }

    private void refreshMeshes() {
        long s = this.style.revision();
        long n = this.stripes.revision();
        long c = this.collar.revision();
        if (s == this.styleRevision && n == this.stripesRevision && c == this.collarRevision) {
            return;
        }
        this.styleRevision = s;
        this.stripesRevision = n;
        this.collarRevision = c;
        Arrays.fill(this.meshes, (Object) null);
    }

    private int styleIndex() {
        if (this.style.is(STYLE_KHEPRESH)) {
            return 1;
        }
        return this.style.is(STYLE_SUN_DISC) ? 2 : 0;
    }

    @Override // net.anubis.client.module.impl.render.HeadCosmeticModule
    float scale() {
        return this.size.get();
    }

    @Override // net.anubis.client.module.impl.render.HeadCosmeticModule
    int[] palette() {
        int[] palette = new int[5];
        if (this.color.is(GlyphTint.MANUAL)) {
            int metal = this.gold.argb() | (-16777216);
            palette[0] = metal;
            palette[1] = this.inlay.argb() | (-16777216);
            palette[2] = this.gem.argb() | (-16777216);
            palette[3] = bronze(metal);
        } else {
            int accent = CosmeticAccent.argb();
            int metal2 = metal(accent);
            palette[0] = metal2;
            palette[1] = class_9848.method_61321(accent, 0.78f);
            palette[2] = gem(accent);
            palette[3] = class_9848.method_61321(metal2, 0.62f);
        }
        return palette;
    }

    @Override // net.anubis.client.module.impl.render.HeadCosmeticModule
    boolean glows() {
        return this.gemGlow.get();
    }

    @Override // net.anubis.client.module.impl.render.HeadCosmeticModule
    boolean glints() {
        return this.glint.get();
    }

    @Override // net.anubis.client.module.impl.render.HeadCosmeticModule
    boolean onFriends() {
        return this.friends.get();
    }

    private static int bronze(int argb) {
        int r = (int) (((argb >> 16) & 255) * 0.7f);
        int g = (int) (((argb >> 8) & 255) * 0.55f);
        int b = (int) ((argb & 255) * 0.4f);
        return (-16777216) | (r << 16) | (g << 8) | b;
    }
}
