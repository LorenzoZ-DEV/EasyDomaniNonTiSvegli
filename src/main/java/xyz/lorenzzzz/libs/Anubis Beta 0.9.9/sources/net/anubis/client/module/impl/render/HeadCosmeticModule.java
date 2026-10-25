package net.anubis.client.module.impl.render;

import it.unimi.dsi.fastutil.ints.Int2BooleanOpenHashMap;
import java.lang.ref.WeakReference;
import net.anubis.client.config.FriendStore;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRegistry;
import net.anubis.client.module.impl.misc.FreecamModule;
import net.anubis.client.module.impl.render.character.CharacterModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10055;
import net.minecraft.class_1657;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_638;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/render/HeadCosmeticModule.class */
@Environment(EnvType.CLIENT)
abstract class HeadCosmeticModule extends ClientModule {
    static volatile HeadCosmeticModule worn;
    static final String COLOR_THEME = "Theme";
    static final String COLOR_MANUAL = "Manual";
    private static final long EPOCH = System.nanoTime();
    private static final long LOOK_REFRESH_NANOS = 4000000;
    private static final long FRIEND_REFRESH_NANOS = 1000000000;
    private static final float GLINT_CYCLE_SECONDS = 3.6f;
    private static final float GLINT_STRENGTH = 0.6f;
    private PharaohMesh.Look look;
    private long lookAt;
    private final Int2BooleanOpenHashMap friendCache;
    private WeakReference<class_638> friendLevel;
    private long friendsCheckedAt;

    abstract PharaohMesh headMesh(boolean z);

    abstract PharaohMesh bodyMesh(boolean z);

    abstract float scale();

    abstract int[] palette();

    abstract boolean glows();

    abstract boolean glints();

    abstract boolean onFriends();

    HeadCosmeticModule(String id, String name, String description) {
        super(id, name, description, ModuleCategory.VISUALS);
        this.friendCache = new Int2BooleanOpenHashMap();
        this.friendLevel = new WeakReference<>(null);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        CharacterModule character = (CharacterModule) ModuleRegistry.get(CharacterModule.class);
        if (character != null) {
            character.setEnabled(false);
        }
        reset();
        worn = this;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        if (worn == this) {
            worn = null;
        }
        reset();
    }

    void reset() {
        this.look = null;
        this.friendCache.clear();
        this.friendLevel = new WeakReference<>(null);
    }

    final boolean wears(class_10055 state) {
        class_310 mc = class_310.method_1551();
        class_746 self = mc.field_1724;
        if (self == null || mc.field_1687 == null) {
            return false;
        }
        if (state.field_53528 == self.method_5628()) {
            return (mc.field_1690.method_31044().method_31034() && mc.field_1755 == null && FreecamModule.active == null) ? false : true;
        }
        return onFriends() && isFriend(mc, state.field_53528);
    }

    /* JADX WARN: Code duplicated, block: B:17:0x0083  */
    private boolean isFriend(class_310 mc, int id) {
        boolean z;
        long now = System.nanoTime();
        if (this.friendLevel.get() != mc.field_1687) {
            this.friendLevel = new WeakReference<>(mc.field_1687);
            this.friendCache.clear();
        }
        if (now - this.friendsCheckedAt > FRIEND_REFRESH_NANOS) {
            this.friendCache.clear();
            this.friendsCheckedAt = now;
        }
        if (this.friendCache.containsKey(id)) {
            return this.friendCache.get(id);
        }
        class_1657 class_1657VarMethod_8469 = mc.field_1687.method_8469(id);
        if (class_1657VarMethod_8469 instanceof class_1657) {
            class_1657 player = class_1657VarMethod_8469;
            if (FriendStore.getInstance().isFriend(player.method_7334().name())) {
                z = true;
            } else {
                z = false;
            }
        } else {
            z = false;
        }
        boolean friend = z;
        this.friendCache.put(id, friend);
        return friend;
    }

    final PharaohMesh.Look look() {
        long now = System.nanoTime();
        PharaohMesh.Look current = this.look;
        if (current != null && now - this.lookAt < LOOK_REFRESH_NANOS) {
            return current;
        }
        float seconds = (now - EPOCH) / 1.0E9f;
        float phase = (seconds % GLINT_CYCLE_SECONDS) / GLINT_CYCLE_SECONDS;
        float sweep = ((phase * 2.0f) - 1.0f) * 3.1415927f * 1.25f;
        float envelope = class_3532.method_15374(phase * 3.1415927f);
        float gain = glints() ? GLINT_STRENGTH * envelope * envelope : 0.0f;
        float pulse = 0.9f + (0.1f * class_3532.method_15374(seconds * 2.2f));
        PharaohMesh.Look current2 = new PharaohMesh.Look(palette(), glows(), class_3532.method_15362(sweep), class_3532.method_15374(sweep), gain, pulse);
        this.look = current2;
        this.lookAt = now;
        return current2;
    }

    static int metal(int accent) {
        return shade(accent, 0.55f, 0.96f);
    }

    static int gem(int accent) {
        return shade(accent, 1.15f, 1.0f);
    }

    static int shade(int argb, float saturation, float value) {
        float hue;
        int r = (argb >> 16) & 255;
        int g = (argb >> 8) & 255;
        int b = argb & 255;
        int max = Math.max(r, Math.max(g, b));
        int chroma = max - Math.min(r, Math.min(g, b));
        float hue2 = 0.0f;
        if (chroma > 0) {
            if (max == r) {
                hue = (g - b) / chroma;
            } else {
                hue = max == g ? 2.0f + ((b - r) / chroma) : 4.0f + ((r - g) / chroma);
            }
            hue2 = hue / 6.0f;
            if (hue2 < 0.0f) {
                hue2 += 1.0f;
            }
        }
        float s = max == 0 ? 0.0f : chroma / max;
        return class_3532.method_60599(hue2, Math.min(1.0f, s * saturation), Math.min(1.0f, value), 255);
    }
}
