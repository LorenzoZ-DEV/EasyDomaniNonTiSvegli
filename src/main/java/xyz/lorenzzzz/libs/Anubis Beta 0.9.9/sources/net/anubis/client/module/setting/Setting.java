package net.anubis.client.module.setting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import net.anubis.client.module.ModuleRevision;
import net.anubis.client.module.StableId;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/setting/Setting.class */
@Environment(EnvType.CLIENT)
public abstract class Setting {
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    private final String name;
    private String id;
    private boolean idBound;
    private long revision;
    private BooleanSupplier visibility;
    private boolean privateValue;
    private JsonElement defaultValue;
    private final List<Runnable> changeListeners = new ArrayList(1);
    private long defaultCheckedRevision = -1;
    private boolean defaultCheckResult = true;

    public abstract JsonElement serialize();

    protected abstract void deserializeValue(JsonElement jsonElement);

    protected Setting(String name) {
        this.name = name;
        this.id = StableId.legacy(name);
    }

    public final String id() {
        return this.id;
    }

    public final long revision() {
        return this.revision;
    }

    public final void bindId(String id) {
        if (this.idBound) {
            throw new IllegalStateException("Setting id already bound");
        }
        this.id = StableId.validate(id);
        this.idBound = true;
    }

    public final String name() {
        return this.name;
    }

    public final Setting onChange(Runnable listener) {
        this.changeListeners.add(listener);
        return this;
    }

    public final void notifyChanged() {
        this.revision++;
        for (int i = 0; i < this.changeListeners.size(); i++) {
            Runnable listener = this.changeListeners.get(i);
            try {
                if (listener == ModuleRevision.CHANGED) {
                    ModuleRevision.changed();
                } else {
                    listener.run();
                }
            } catch (Throwable t) {
                LOGGER.error("Setting change listener failed for '{}'", this.name, t);
            }
        }
    }

    public final Setting visibleWhen(BooleanSupplier condition) {
        this.visibility = condition;
        return this;
    }

    public final boolean isVisible() {
        return this.visibility == null || this.visibility.getAsBoolean();
    }

    public final Setting privateValue() {
        this.privateValue = true;
        return this;
    }

    public final boolean isPrivate() {
        return this.privateValue;
    }

    public final void captureDefault() {
        if (this.defaultValue == null) {
            this.defaultValue = serialize();
        }
    }

    public final boolean isDefault() {
        if (this.defaultValue == null) {
            return true;
        }
        if (this.defaultCheckedRevision != this.revision) {
            this.defaultCheckResult = this.defaultValue.equals(serialize());
            this.defaultCheckedRevision = this.revision;
        }
        return this.defaultCheckResult;
    }

    public final boolean isDefault(JsonElement value) {
        return this.defaultValue != null && sameValue(this.defaultValue, value);
    }

    public static boolean sameValue(JsonElement a, JsonElement b) {
        if (a == null || b == null) {
            return a == b;
        }
        if (a.isJsonPrimitive() && b.isJsonPrimitive()) {
            JsonPrimitive left = a.getAsJsonPrimitive();
            JsonPrimitive right = b.getAsJsonPrimitive();
            if (!left.isNumber() || !right.isNumber()) {
                return left.equals(right);
            }
            try {
                return left.getAsBigDecimal().compareTo(right.getAsBigDecimal()) == 0;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        if (a.isJsonArray() && b.isJsonArray()) {
            JsonArray left2 = a.getAsJsonArray();
            JsonArray right2 = b.getAsJsonArray();
            if (left2.size() != right2.size()) {
                return false;
            }
            for (int i = 0; i < left2.size(); i++) {
                if (!sameValue(left2.get(i), right2.get(i))) {
                    return false;
                }
            }
            return true;
        }
        if (a.isJsonObject() && b.isJsonObject()) {
            JsonObject left3 = a.getAsJsonObject();
            JsonObject right3 = b.getAsJsonObject();
            if (!left3.keySet().equals(right3.keySet())) {
                return false;
            }
            for (String key : left3.keySet()) {
                if (!sameValue(left3.get(key), right3.get(key))) {
                    return false;
                }
            }
            return true;
        }
        return a.equals(b);
    }

    public final void resetToDefault() {
        if (!isDefault()) {
            deserialize(this.defaultValue);
        }
    }

    public JsonElement compact(JsonElement value) {
        return value;
    }

    protected final JsonElement defaultJson() {
        return this.defaultValue;
    }

    public final void deserialize(JsonElement element) {
        deserializeValue(element);
        notifyChanged();
    }
}
