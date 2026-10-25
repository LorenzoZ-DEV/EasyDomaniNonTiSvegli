package net.anubis.client.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import net.anubis.client.event.Event;
import net.anubis.client.event.EventBus;
import net.anubis.client.event.EventPriority;
import net.anubis.client.event.EventType;
import net.anubis.client.event.ListenerHandle;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.ModuleToggleEvent;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.Setting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/ClientModule.class */
@Environment(EnvType.CLIENT)
public class ClientModule {
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    public static final String SHOW_IN_HUD_SETTING = "Show in HUD";
    private final String id;
    private final String name;
    private final String description;
    private final ModuleCategory category;
    private boolean enabled;
    private boolean enabling;
    private boolean disabling;
    private boolean disableRequested;
    private final List<Setting> settings;
    private final List<Setting> settingsView;
    private final BooleanSetting showInHud;
    private final BooleanSetting toggleMessage;
    private BooleanSetting notifications;
    private final List<ListenerHandle<?>> ownedListeners;
    private boolean busRegistered;
    volatile boolean registered;
    private int keybind;
    private boolean holdMode;

    protected ClientModule(String name, String description, ModuleCategory category) {
        this(StableId.legacy(name), name, description, category);
    }

    protected ClientModule(String id, String name, String description, ModuleCategory category) {
        BooleanSetting booleanSetting;
        BooleanSetting booleanSetting2;
        this.settings = new ArrayList();
        this.settingsView = Collections.unmodifiableList(this.settings);
        this.ownedListeners = new ArrayList();
        this.keybind = -1;
        this.id = StableId.validate(id);
        this.name = name;
        this.description = description;
        this.category = (ModuleCategory) Objects.requireNonNull(category, "category");
        if (category != ModuleCategory.CLIENT) {
            booleanSetting = (BooleanSetting) addSetting("show-in-hud", new BooleanSetting(SHOW_IN_HUD_SETTING, true));
        } else {
            booleanSetting = null;
        }
        this.showInHud = booleanSetting;
        if (category != ModuleCategory.CLIENT) {
            booleanSetting2 = (BooleanSetting) addSetting("toggle-message", new BooleanSetting("Toggle Message", true));
        } else {
            booleanSetting2 = null;
        }
        this.toggleMessage = booleanSetting2;
        if (this.toggleMessage != null) {
            this.toggleMessage.visibleWhen(() -> {
                return !isAction();
            });
        }
    }

    protected final <S extends Setting> S addSetting(S s) {
        return (S) addSetting(s.id(), s);
    }

    protected final <S extends Setting> S addSetting(String str, S s) {
        return (S) insertSetting(this.settings.size(), str, s);
    }

    private <S extends Setting> S insertSetting(int index, String id, S setting) {
        for (Setting existing : this.settings) {
            if (existing.id().equals(id)) {
                throw new IllegalArgumentException("Duplicate setting id: " + this.id + "/" + id);
            }
        }
        setting.bindId(id);
        setting.onChange(ModuleRevision.CHANGED);
        this.settings.add(index, setting);
        ModuleRevision.changed();
        return setting;
    }

    protected final BooleanSetting usesNotifications() {
        if (this.notifications == null) {
            int standard = (this.showInHud != null ? 1 : 0) + (this.toggleMessage != null ? 1 : 0);
            this.notifications = (BooleanSetting) insertSetting(standard, "notifications", new BooleanSetting("Notifications", true));
        }
        return this.notifications;
    }

    public final boolean notificationsOn() {
        return this.notifications == null || this.notifications.get();
    }

    public final boolean toggleMessageOn() {
        return (this.toggleMessage == null || !this.toggleMessage.get() || isAction()) ? false : true;
    }

    public final String id() {
        return this.id;
    }

    public final String name() {
        return this.name;
    }

    public final String description() {
        return this.description;
    }

    public final ModuleCategory category() {
        return this.category;
    }

    public final List<Setting> settings() {
        return this.settingsView;
    }

    public final void captureDefaults() {
        for (Setting setting : this.settings) {
            setting.captureDefault();
        }
    }

    public final boolean hasModifiedSettings() {
        for (Setting setting : this.settings) {
            if (!setting.isDefault()) {
                return true;
            }
        }
        return false;
    }

    public final void resetSettings() {
        for (Setting setting : this.settings) {
            setting.resetToDefault();
        }
    }

    public final BooleanSetting showInHud() {
        return this.showInHud;
    }

    public boolean persistEnabled() {
        return true;
    }

    public boolean enabledByDefault() {
        return false;
    }

    public boolean inConfigs() {
        return true;
    }

    public int settingsVersion() {
        return 0;
    }

    public void migrateSettings(int savedVersion) {
    }

    public boolean isAction() {
        return false;
    }

    public void onClick() {
    }

    public final boolean isEnabled() {
        return this.enabled;
    }

    public final void setEnabled(boolean value) {
        boolean was = this.enabled;
        switchTo(value);
        if (this.enabled != was) {
            ModuleToggleEvent.dispatch(this);
        }
    }

    private void switchTo(boolean value) {
        if (this.enabled == value || this.disabling) {
            return;
        }
        this.enabled = value;
        ModuleRevision.changed();
        if (this.registered) {
            ModuleRegistry.switched(this);
        }
        if (value) {
            this.enabling = true;
            this.disableRequested = false;
            try {
                this.busRegistered = EventBus.INSTANCE.register(this) > 0;
                onEnable();
                this.enabling = false;
                if (this.disableRequested) {
                    this.disableRequested = false;
                    switchTo(false);
                }
                return;
            } catch (Throwable failure) {
                this.enabling = false;
                this.disableRequested = false;
                try {
                    switchTo(false);
                } catch (Throwable cleanupFailure) {
                    if (cleanupFailure != failure) {
                        failure.addSuppressed(cleanupFailure);
                    }
                    if (cleanupFailure instanceof Error) {
                        Error error = (Error) cleanupFailure;
                        throw error;
                    }
                }
                LOGGER.error("Failed to enable module '{}'; activation rolled back", this.name, failure);
                if (failure instanceof Error) {
                    Error error2 = (Error) failure;
                    throw error2;
                }
                return;
            }
        }
        this.disabling = true;
        try {
            try {
                onDisable();
                try {
                    this.ownedListeners.forEach((v0) -> {
                        v0.unregister();
                    });
                    this.ownedListeners.clear();
                    if (this.busRegistered) {
                        this.busRegistered = false;
                        EventBus.INSTANCE.unregister(this);
                    }
                } finally {
                    this.disabling = false;
                }
            } catch (RuntimeException failure2) {
                LOGGER.error("Failed to deactivate module '{}'", this.name, failure2);
                try {
                    this.ownedListeners.forEach((v0) -> {
                        v0.unregister();
                    });
                    this.ownedListeners.clear();
                    if (this.busRegistered) {
                        this.busRegistered = false;
                        EventBus.INSTANCE.unregister(this);
                    }
                } finally {
                    this.disabling = false;
                }
            }
        } catch (Throwable th) {
            try {
                this.ownedListeners.forEach((v0) -> {
                    v0.unregister();
                });
                this.ownedListeners.clear();
                if (this.busRegistered) {
                    this.busRegistered = false;
                    EventBus.INSTANCE.unregister(this);
                }
                throw th;
            } finally {
                this.disabling = false;
            }
        }
    }

    public final void toggle() {
        setEnabled(!this.enabled);
    }

    protected final void requestDisable() {
        if (this.enabling) {
            this.disableRequested = true;
        } else {
            setEnabled(false);
        }
    }

    public final int keybind() {
        return this.keybind;
    }

    public final void setKeybind(int glfwKey) {
        if (this.keybind == glfwKey) {
            return;
        }
        this.keybind = glfwKey;
        ModuleRevision.changed();
    }

    public final boolean isHoldMode() {
        return this.holdMode;
    }

    public final void setHoldMode(boolean value) {
        if (this.holdMode == value) {
            return;
        }
        this.holdMode = value;
        ModuleRevision.changed();
    }

    protected void onEnable() {
    }

    protected void onDisable() {
    }

    protected <E extends Event> void on(EventType<E> type, Consumer<E> callback) {
        on(type, Stage.PRE, EventPriority.NORMAL, callback);
    }

    protected <E extends Event> void on(EventType<E> type, Stage stage, Consumer<E> callback) {
        on(type, stage, EventPriority.NORMAL, callback);
    }

    protected <E extends Event> void on(EventType<E> type, Stage stage, EventPriority priority, Consumer<E> callback) {
        if (!this.enabled) {
            throw new IllegalStateException("Cannot register a listener on disabled module: " + this.name);
        }
        this.ownedListeners.add(type.listen(this, stage, priority, callback));
    }
}
