package net.anubis.client.module.impl.render;

import com.example.client.mixin.AccessorAtmosphereClientLevel;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.ColorSetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.util.glyph.GlyphTint;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_12076;
import net.minecraft.class_1937;
import net.minecraft.class_1959;
import net.minecraft.class_2338;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_638;
import net.minecraft.class_7833;
import net.minecraft.class_9848;
import org.joml.Vector3fc;
import org.joml.Vector4f;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/AtmosphereModule.class */
@Environment(EnvType.CLIENT)
public final class AtmosphereModule extends ClientModule {
    private static final String CUSTOM = "Custom";
    private static final String AURORA_ALWAYS = "Always";
    private static final String AURORA_COLD = "Cold Biomes";
    private static final String AURORA_OFF = "Off";
    private static final float MOON_SCALE = 1.4f;
    private static final Theme[] THEMES = {new Theme("Borealis", 198414, 729648, 13073302, 3080090, 9063935, 13163263), new Theme("Nebula", 459791, 2233916, 14053304, 16732120, 5991679, 15124735), new Theme("Anubis", 657157, 2890512, 15239498, 16757306, 16730670, 16769200), new Theme("Frost", 264724, 1454154, 11057384, 8382719, 15135999, 14544639), new Theme("Crimson", 721926, 3018260, 15227490, 16724821, 16751164, 16765640)};
    private final ModeSetting theme;
    private final ColorSetting nightColor;
    private final ColorSetting sunsetColor;
    private final ColorSetting auroraColor;
    private final ColorSetting auroraTopColor;
    private final ModeSetting skyShader;
    private final NumberSetting shaderDaytime;
    private final NumberSetting shaderBrightness;
    private final NumberSetting shaderSpeed;
    private final NumberSetting shaderStars;
    private final NumberSetting shaderEffect;
    private final NumberSetting shaderBand;
    private final BooleanSetting gradientSky;
    private final BooleanSetting keepInRain;
    private final BooleanSetting celestialGlow;
    private final BooleanSetting stars;
    private final NumberSetting starBrightness;
    private final BooleanSetting milkyWay;
    private final NumberSetting milkyWayBrightness;
    private final BooleanSetting shootingStars;
    private final ModeSetting aurora;
    private final NumberSetting auroraBrightness;
    private final NumberSetting auroraSpeed;
    private final BooleanSetting cloudColors;
    private final BooleanSetting endSky;
    private final BooleanSetting netherColors;
    private final AtmosphereSky sky;
    private final AtmosphereStars starField;
    private final AtmosphereAurora auroraMesh;
    private final AtmosphereShaderSky shaderSky;
    private final class_4587 world;
    private float night;
    private float rainBrightness;
    private float auroraHere;
    private long lastFrame;
    private boolean moonScaled;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/AtmosphereModule$Theme.class */
    @Environment(EnvType.CLIENT)
    private static final class Theme extends Record {
        private final String name;
        private final int nightZenith;
        private final int nightHorizon;
        private final int twilight;
        private final int auroraLow;
        private final int auroraHigh;
        private final int milkyWay;

        private Theme(String name, int nightZenith, int nightHorizon, int twilight, int auroraLow, int auroraHigh, int milkyWay) {
            this.name = name;
            this.nightZenith = nightZenith;
            this.nightHorizon = nightHorizon;
            this.twilight = twilight;
            this.auroraLow = auroraLow;
            this.auroraHigh = auroraHigh;
            this.milkyWay = milkyWay;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Theme.class), Theme.class, "name;nightZenith;nightHorizon;twilight;auroraLow;auroraHigh;milkyWay", "FIELD:Lnet/anubis/client/module/impl/render/AtmosphereModule$Theme;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/render/AtmosphereModule$Theme;->nightZenith:I", "FIELD:Lnet/anubis/client/module/impl/render/AtmosphereModule$Theme;->nightHorizon:I", "FIELD:Lnet/anubis/client/module/impl/render/AtmosphereModule$Theme;->twilight:I", "FIELD:Lnet/anubis/client/module/impl/render/AtmosphereModule$Theme;->auroraLow:I", "FIELD:Lnet/anubis/client/module/impl/render/AtmosphereModule$Theme;->auroraHigh:I", "FIELD:Lnet/anubis/client/module/impl/render/AtmosphereModule$Theme;->milkyWay:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Theme.class), Theme.class, "name;nightZenith;nightHorizon;twilight;auroraLow;auroraHigh;milkyWay", "FIELD:Lnet/anubis/client/module/impl/render/AtmosphereModule$Theme;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/render/AtmosphereModule$Theme;->nightZenith:I", "FIELD:Lnet/anubis/client/module/impl/render/AtmosphereModule$Theme;->nightHorizon:I", "FIELD:Lnet/anubis/client/module/impl/render/AtmosphereModule$Theme;->twilight:I", "FIELD:Lnet/anubis/client/module/impl/render/AtmosphereModule$Theme;->auroraLow:I", "FIELD:Lnet/anubis/client/module/impl/render/AtmosphereModule$Theme;->auroraHigh:I", "FIELD:Lnet/anubis/client/module/impl/render/AtmosphereModule$Theme;->milkyWay:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Theme.class, Object.class), Theme.class, "name;nightZenith;nightHorizon;twilight;auroraLow;auroraHigh;milkyWay", "FIELD:Lnet/anubis/client/module/impl/render/AtmosphereModule$Theme;->name:Ljava/lang/String;", "FIELD:Lnet/anubis/client/module/impl/render/AtmosphereModule$Theme;->nightZenith:I", "FIELD:Lnet/anubis/client/module/impl/render/AtmosphereModule$Theme;->nightHorizon:I", "FIELD:Lnet/anubis/client/module/impl/render/AtmosphereModule$Theme;->twilight:I", "FIELD:Lnet/anubis/client/module/impl/render/AtmosphereModule$Theme;->auroraLow:I", "FIELD:Lnet/anubis/client/module/impl/render/AtmosphereModule$Theme;->auroraHigh:I", "FIELD:Lnet/anubis/client/module/impl/render/AtmosphereModule$Theme;->milkyWay:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public String name() {
            return this.name;
        }

        public int nightZenith() {
            return this.nightZenith;
        }

        public int nightHorizon() {
            return this.nightHorizon;
        }

        public int twilight() {
            return this.twilight;
        }

        public int auroraLow() {
            return this.auroraLow;
        }

        public int auroraHigh() {
            return this.auroraHigh;
        }

        public int milkyWay() {
            return this.milkyWay;
        }
    }

    public AtmosphereModule() {
        super("atmosphere", "Atmosphere", "A custom sky with stars and an aurora.", ModuleCategory.VISUALS);
        this.theme = (ModeSetting) addSetting("theme", new ModeSetting(GlyphTint.THEME, "Borealis", "Borealis", "Nebula", "Anubis", "Frost", "Crimson", CUSTOM));
        this.nightColor = (ColorSetting) addSetting("night-color", new ColorSetting("Night Sky Color", -16047568));
        this.sunsetColor = (ColorSetting) addSetting("sunset-color", new ColorSetting("Sunset Color", -3703914));
        this.auroraColor = (ColorSetting) addSetting("aurora-color", new ColorSetting("Aurora Color", -13697126));
        this.auroraTopColor = (ColorSetting) addSetting("aurora-top-color", new ColorSetting("Aurora Top Color", -7713281));
        this.skyShader = (ModeSetting) addSetting("sky-shader", new ModeSetting("Sky Shader", "Off", "Off", "Space", "Northern Lights", "Ringed Planet", "Black Hole"));
        this.shaderDaytime = (NumberSetting) addSetting("sky-shader-daytime", new NumberSetting("Daytime Strength", 1.0f, 0.0f, 1.0f).step(0.05f));
        this.shaderBrightness = (NumberSetting) addSetting("sky-shader-brightness", new NumberSetting("Shader Brightness", 1.0f, 0.2f, 2.0f).step(0.1f));
        this.shaderSpeed = (NumberSetting) addSetting("sky-shader-speed", new NumberSetting("Shader Speed", 1.0f, 0.0f, 3.0f).step(0.1f));
        this.shaderStars = (NumberSetting) addSetting("sky-shader-stars", new NumberSetting("Shader Stars", 1.0f, 0.0f, 2.0f).step(0.1f));
        this.shaderEffect = (NumberSetting) addSetting("sky-shader-effect", new NumberSetting("Nebula / Aurora Strength", 1.0f, 0.0f, 2.0f).step(0.1f));
        this.shaderBand = (NumberSetting) addSetting("sky-shader-galaxy", new NumberSetting("Galaxy Band", 1.0f, 0.0f, 2.0f).step(0.1f));
        this.gradientSky = (BooleanSetting) addSetting("gradient-sky", new BooleanSetting("Gradient Sky", true));
        this.keepInRain = (BooleanSetting) addSetting("keep-in-rain", new BooleanSetting("Keep In Rain", true));
        this.celestialGlow = (BooleanSetting) addSetting("sun-moon-glow", new BooleanSetting("Sun & Moon Glow", true));
        this.stars = (BooleanSetting) addSetting("stars", new BooleanSetting("Stars", true));
        this.starBrightness = (NumberSetting) addSetting("star-brightness", new NumberSetting("Star Brightness", 1.3f, 0.5f, 2.0f).step(0.1f));
        this.milkyWay = (BooleanSetting) addSetting("milky-way", new BooleanSetting("Milky Way", true));
        this.milkyWayBrightness = (NumberSetting) addSetting("milky-way-brightness", new NumberSetting("Milky Way Brightness", 1.0f, 0.2f, 2.0f).step(0.1f));
        this.shootingStars = (BooleanSetting) addSetting("shooting-stars", new BooleanSetting("Shooting Stars", true));
        this.aurora = (ModeSetting) addSetting("aurora", new ModeSetting("Aurora", AURORA_ALWAYS, AURORA_ALWAYS, AURORA_COLD, "Off"));
        this.auroraBrightness = (NumberSetting) addSetting("aurora-brightness", new NumberSetting("Aurora Brightness", 1.0f, 0.2f, 2.0f).step(0.1f));
        this.auroraSpeed = (NumberSetting) addSetting("aurora-speed", new NumberSetting("Aurora Speed", 1.0f, 0.2f, 3.0f).step(0.1f));
        this.cloudColors = (BooleanSetting) addSetting("cloud-colors", new BooleanSetting("Cloud Colors", true));
        this.endSky = (BooleanSetting) addSetting("end-sky", new BooleanSetting("End Sky", true));
        this.netherColors = (BooleanSetting) addSetting("nether-colors", new BooleanSetting("Nether Colors", true));
        this.sky = new AtmosphereSky();
        this.starField = new AtmosphereStars();
        this.auroraMesh = new AtmosphereAurora();
        this.shaderSky = new AtmosphereShaderSky();
        this.world = new class_4587();
        this.rainBrightness = 1.0f;
        this.nightColor.visibleWhen(() -> {
            return this.theme.is(CUSTOM);
        });
        this.sunsetColor.visibleWhen(() -> {
            return this.theme.is(CUSTOM);
        });
        this.auroraColor.visibleWhen(() -> {
            return this.theme.is(CUSTOM);
        });
        this.auroraTopColor.visibleWhen(() -> {
            return this.theme.is(CUSTOM);
        });
        this.shaderDaytime.visibleWhen(this::shaderOn);
        this.shaderBrightness.visibleWhen(this::shaderOn);
        this.shaderSpeed.visibleWhen(this::shaderOn);
        this.shaderStars.visibleWhen(this::shaderOn);
        this.shaderEffect.visibleWhen(this::shaderOn);
        this.shaderBand.visibleWhen(this::shaderOn);
        this.stars.visibleWhen(() -> {
            return !shaderOn();
        });
        this.starBrightness.visibleWhen(() -> {
            return this.stars.get() && !shaderOn();
        });
        this.milkyWay.visibleWhen(() -> {
            return !shaderOn();
        });
        this.milkyWayBrightness.visibleWhen(() -> {
            return this.milkyWay.get() && !shaderOn();
        });
        this.aurora.visibleWhen(() -> {
            return !shaderOn();
        });
        this.auroraBrightness.visibleWhen(() -> {
            return (this.aurora.is("Off") || shaderOn()) ? false : true;
        });
        this.auroraSpeed.visibleWhen(() -> {
            return (this.aurora.is("Off") || shaderOn()) ? false : true;
        });
    }

    public static AtmosphereModule get() {
        AtmosphereModule module = (AtmosphereModule) ModuleRegistry.get(AtmosphereModule.class);
        if (module == null || !module.isEnabled()) {
            return null;
        }
        return module;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        this.sky.close();
        this.starField.close();
        this.auroraMesh.close();
        this.shaderSky.close();
    }

    public void extract(class_638 level, float partialTick, class_12076 state) {
        this.night = class_3532.method_15363(state.field_63094 * 2.0f, 0.0f, 1.0f);
        float rain = 1.0f - state.field_63093;
        float thunder = level.method_8478(partialTick);
        if (this.keepInRain.get()) {
            this.night = Math.max(this.night, clearNight(state.field_63091) * rain);
            state.field_63094 = Math.max(state.field_63094, this.night * 0.5f);
            state.field_63093 = 1.0f;
            rain = 0.0f;
            thunder = 0.0f;
        }
        this.rainBrightness = 1.0f - rain;
        Theme colours = currentTheme();
        int flashTicks = ((AccessorAtmosphereClientLevel) level).anubis$atmosphereSkyFlash();
        this.sky.update(state.field_63091, state.field_64465, rain, thunder, flashTicks > 0 ? Math.min(1.0f, (flashTicks - partialTick) / 2.0f) : 0.0f, colours.nightZenith, colours.nightHorizon, colours.twilight);
    }

    public boolean keepsSkyInRain() {
        return this.keepInRain.get();
    }

    private static float clearNight(float sunAngle) {
        float dark = class_3532.method_15363(0.75f - (2.0f * class_3532.method_15362(sunAngle)), 0.0f, 1.0f);
        return dark * dark;
    }

    public boolean skyDisc() {
        if (!this.gradientSky.get() || !this.sky.ready()) {
            return false;
        }
        this.sky.renderDome(this.world);
        renderShaderSky();
        return true;
    }

    public void afterSkyDisc() {
        renderShaderSky();
    }

    private void renderShaderSky() {
        if (shaderOn()) {
            Theme colours = currentTheme();
            this.shaderSky.render(this.skyShader.get(), shaderVisibility(), this.shaderBrightness.get(), this.shaderSpeed.get(), this.shaderStars.get(), this.shaderEffect.get(), this.shaderBand.get(), colours.auroraLow, colours.auroraHigh, colours.milkyWay, colours.nightZenith, colours.nightHorizon);
        }
    }

    private boolean shaderOn() {
        return !this.skyShader.is("Off");
    }

    private boolean shaderAurora() {
        return this.skyShader.is("Northern Lights");
    }

    private float shaderVisibility() {
        return class_3532.method_16439(this.night, this.shaderDaytime.get(), 1.0f) * (0.4f + (0.6f * this.rainBrightness));
    }

    public boolean replacesSunrise() {
        return this.gradientSky.get() && this.sky.ready();
    }

    public boolean stars(class_4587 pose) {
        if (shaderOn()) {
            return true;
        }
        float clear = this.night * this.rainBrightness;
        if (this.milkyWay.get()) {
            this.starField.renderBand(pose, clear * this.milkyWayBrightness.get(), currentTheme().milkyWay);
        }
        if (!this.stars.get()) {
            return false;
        }
        this.starField.renderStars(pose, clear, this.starBrightness.get());
        return true;
    }

    public void beforeMoon(class_4587 pose) {
        this.moonScaled = this.celestialGlow.get();
        if (this.moonScaled) {
            pose.method_22903();
            pose.method_22905(MOON_SCALE, 1.0f, MOON_SCALE);
        }
    }

    public void afterMoon(class_4587 pose) {
        if (this.moonScaled) {
            pose.method_22909();
        }
        this.moonScaled = false;
    }

    public void afterCelestials() {
        long now = System.nanoTime();
        float dt = this.lastFrame == 0 ? 0.0f : Math.min((now - this.lastFrame) / 1.0E9f, 0.1f);
        this.lastFrame = now;
        if (this.sky.ready()) {
            if (this.celestialGlow.get()) {
                this.sky.renderHalos(this.world, this.rainBrightness, this.night);
            }
            if (this.shootingStars.get()) {
                this.sky.renderMeteors(this.world, this.night, this.rainBrightness);
            }
            this.auroraHere += (auroraTarget() - this.auroraHere) * Math.min(1.0f, dt * 0.5f);
            if (this.auroraHere > 0.01f && this.night > 0.01f && !shaderOn()) {
                Theme colours = currentTheme();
                float time = ((now % 3600000000000L) / 1.0E9f) * this.auroraSpeed.get();
                float intensity = this.auroraHere * this.night * (0.35f + (0.65f * this.rainBrightness)) * this.auroraBrightness.get();
                this.auroraMesh.render(this.world, intensity, time, colours.auroraLow, colours.auroraHigh);
            }
        }
    }

    private float auroraTarget() {
        if (this.aurora.is("Off")) {
            return 0.0f;
        }
        if (this.aurora.is(AURORA_ALWAYS)) {
            return 1.0f;
        }
        class_310 mc = class_310.method_1551();
        if (mc.field_1687 == null) {
            return 0.0f;
        }
        class_2338 at = class_2338.method_49638(mc.field_1773.method_19418().method_71156());
        return ((class_1959) mc.field_1687.method_23753(at).comp_349()).method_33599(at, mc.field_1687.method_8615()) ? 1.0f : 0.0f;
    }

    public void fog(class_4184 camera, class_638 level, Vector4f color) {
        if (level.method_27983() == class_1937.field_25179) {
            if (this.gradientSky.get() && this.sky.ready()) {
                Vector3fc look = camera.method_19335();
                int rgb = this.sky.horizonToward(look.x(), look.z());
                color.x = ((rgb >> 16) & 255) / 255.0f;
                color.y = ((rgb >> 8) & 255) / 255.0f;
                color.z = (rgb & 255) / 255.0f;
            }
            if (shaderOn()) {
                shaderFog(color);
                return;
            }
            return;
        }
        if (level.method_27983() == class_1937.field_25180 && this.netherColors.get()) {
            float luma = (0.2126f * color.x) + (0.7152f * color.y) + (0.0722f * color.z);
            color.x = class_3532.method_15363((luma + ((color.x - luma) * 1.45f)) * 1.12f, 0.0f, 1.0f);
            color.y = class_3532.method_15363((luma + ((color.y - luma) * 1.45f)) * 1.12f, 0.0f, 1.0f);
            color.z = class_3532.method_15363((luma + ((color.z - luma) * 1.45f)) * 1.12f, 0.0f, 1.0f);
        }
    }

    private void shaderFog(Vector4f color) {
        int horizon = currentTheme().nightHorizon;
        float scale = shaderAurora() ? 0.85f : 0.3f;
        float amount = shaderVisibility() * 0.85f;
        color.x = class_3532.method_16439(amount, color.x, (((horizon >> 16) & 255) / 255.0f) * scale);
        color.y = class_3532.method_16439(amount, color.y, (((horizon >> 8) & 255) / 255.0f) * scale);
        color.z = class_3532.method_16439(amount, color.z, ((horizon & 255) / 255.0f) * scale);
    }

    public int clouds(int argb) {
        class_310 mc = class_310.method_1551();
        if (!this.cloudColors.get() || !this.sky.ready() || mc.field_1687 == null || mc.field_1687.method_27983() != class_1937.field_25179) {
            return argb;
        }
        float golden = this.sky.golden();
        float dark = this.sky.nightness();
        int target = class_9848.method_61319(golden, class_9848.method_61319(dark, 16777215, currentTheme().nightHorizon), this.sky.twilightGlow());
        float amount = Math.max(golden * 0.75f, dark * 0.55f);
        return keepBrightness(argb, target, amount);
    }

    public boolean endSky() {
        if (!this.endSky.get()) {
            return false;
        }
        long now = System.nanoTime();
        this.sky.renderEndDome(this.world);
        class_4587 turning = new class_4587();
        turning.method_22907(class_7833.field_40716.rotationDegrees(((now % 1200000000000L) / 1.0E9f) * 0.3f));
        this.starField.renderNebulae(turning, 1.0f);
        this.starField.renderStars(turning, 1.0f, 1.2f);
        return true;
    }

    private Theme currentTheme() {
        for (Theme each : THEMES) {
            if (this.theme.is(each.name)) {
                return each;
            }
        }
        int horizon = this.nightColor.rgb();
        return new Theme(CUSTOM, AtmosphereSky.scale(horizon, 0.3f), horizon, this.sunsetColor.rgb(), this.auroraColor.rgb(), this.auroraTopColor.rgb(), 15263999);
    }

    private static int keepBrightness(int argb, int target, float amount) {
        if (amount <= 0.0f) {
            return argb;
        }
        float r = ((argb >> 16) & 255) / 255.0f;
        float g = ((argb >> 8) & 255) / 255.0f;
        float b = (argb & 255) / 255.0f;
        float tr = ((target >> 16) & 255) / 255.0f;
        float tg = ((target >> 8) & 255) / 255.0f;
        float tb = (target & 255) / 255.0f;
        float scale = luminance(r, g, b) / Math.max(luminance(tr, tg, tb), 0.001f);
        return (argb & (-16777216)) | (channel(class_3532.method_16439(amount, r, Math.min(1.0f, tr * scale))) << 16) | (channel(class_3532.method_16439(amount, g, Math.min(1.0f, tg * scale))) << 8) | channel(class_3532.method_16439(amount, b, Math.min(1.0f, tb * scale)));
    }

    private static float luminance(float r, float g, float b) {
        return (0.2126f * r) + (0.7152f * g) + (0.0722f * b);
    }

    private static int channel(float value) {
        return class_3532.method_15340(Math.round(value * 255.0f), 0, 255);
    }
}
