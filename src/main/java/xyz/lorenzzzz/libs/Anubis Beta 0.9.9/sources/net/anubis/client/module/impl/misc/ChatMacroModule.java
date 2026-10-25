package net.anubis.client.module.impl.misc;

import java.util.Locale;
import net.anubis.client.chat.ModuleChat;
import net.anubis.client.event.EventPriority;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.KeyInputEvent;
import net.anubis.client.event.impl.MouseInputEvent;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.input.ModuleKeybinds;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.KeySetting;
import net.anubis.client.module.setting.ModeSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.module.setting.StringSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import net.minecraft.class_3544;
import net.minecraft.class_408;
import net.minecraft.class_634;
import org.apache.commons.lang3.StringUtils;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/ChatMacroModule.class */
@Environment(EnvType.CLIENT)
public final class ChatMacroModule extends ClientModule {
    public static final int MAX_MACROS = 8;
    private static final String ACTION_SEND = "Send";
    private static final String ACTION_OPEN = "Open in Chat";
    private final ModeSetting slots;
    private final ModeSetting pressAction;
    private final NumberSetting cooldown;
    private final KeySetting[] keys;
    private final StringSetting[] texts;
    private int pendingSlot;
    private long lastSendNanos;
    private boolean sentAny;

    public ChatMacroModule() {
        super("chat-macro", "Chat Macro", "Sends chat messages or commands with a key press.", ModuleCategory.MISC);
        this.slots = (ModeSetting) addSetting("slots", new ModeSetting("Slots", "3", slotChoices()));
        this.pressAction = (ModeSetting) addSetting("on-press", new ModeSetting("On Press", ACTION_SEND, ACTION_SEND, ACTION_OPEN));
        this.cooldown = (NumberSetting) addSetting("cooldown", new NumberSetting("Cooldown (s)", 1.0f, 1.0f, 10.0f).step(0.5f));
        this.keys = new KeySetting[8];
        this.texts = new StringSetting[8];
        this.pendingSlot = -1;
        usesNotifications();
        this.cooldown.visibleWhen(() -> {
            return this.pressAction.is(ACTION_SEND);
        });
        for (int i = 0; i < 8; i++) {
            int slot = i + 1;
            this.keys[i] = (KeySetting) addSetting("macro-" + slot + "-key", new KeySetting("Macro " + slot + " Key", -1));
            this.texts[i] = (StringSetting) addSetting("macro-" + slot + "-text", new StringSetting("Macro " + slot + " Text", ""));
            this.texts[i].privateValue();
            this.keys[i].visibleWhen(() -> {
                return slot <= slotCount();
            });
            this.texts[i].visibleWhen(() -> {
                return slot <= slotCount();
            });
        }
    }

    @Override // net.anubis.client.module.ClientModule
    public boolean inConfigs() {
        return false;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        this.pendingSlot = -1;
        on(KeyInputEvent.TYPE, Stage.PRE, EventPriority.HIGH, this::onKey);
        on(MouseInputEvent.TYPE, Stage.PRE, EventPriority.HIGH, this::onMouse);
        on(TickEvent.TYPE, Stage.PRE, this::onTick);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        this.pendingSlot = -1;
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
        if (ModuleKeybinds.isBindable(pressed) && this.pendingSlot == -1) {
            class_310 mc = class_310.method_1551();
            if (mc.field_1755 != null || mc.field_1724 == null) {
                return;
            }
            int count = slotCount();
            for (int i = 0; i < count; i++) {
                if (this.keys[i].value() == pressed && !this.texts[i].get().isBlank()) {
                    this.pendingSlot = i;
                    return;
                }
            }
        }
    }

    private void onTick(TickEvent event) {
        int slot = this.pendingSlot;
        if (slot < 0) {
            return;
        }
        this.pendingSlot = -1;
        class_310 mc = class_310.method_1551();
        class_634 connection = mc.method_1562();
        if (mc.field_1724 == null || connection == null || mc.field_1755 != null) {
            return;
        }
        String text = normalize(this.texts[slot].get());
        if (text.isEmpty() || text.equals("/")) {
            return;
        }
        if (this.pressAction.is(ACTION_OPEN)) {
            mc.method_1507(new class_408(text, false));
            return;
        }
        long now = System.nanoTime();
        long left = ((long) (((double) this.cooldown.get()) * 1.0E9d)) - (now - this.lastSendNanos);
        if (this.sentAny && left > 0) {
            double seconds = Math.ceil(left / 1.0E8d) / 10.0d;
            ModuleChat.actionBar(this, ModuleChat.body().warn("On cooldown").text(", ").value(String.format(Locale.ROOT, "%.1fs", Double.valueOf(seconds))).text(" left"));
            return;
        }
        this.lastSendNanos = now;
        this.sentAny = true;
        mc.field_1705.method_1743().method_1803(text);
        if (text.startsWith("/")) {
            connection.method_45730(text.substring(1));
        } else {
            connection.method_45729(text);
        }
    }

    static String normalize(String raw) {
        String allowed = class_3544.method_57180(raw);
        return class_3544.method_43681(StringUtils.normalizeSpace(allowed.trim()));
    }

    private int slotCount() {
        return Integer.parseInt(this.slots.get());
    }

    private static String[] slotChoices() {
        String[] choices = new String[8];
        for (int i = 0; i < 8; i++) {
            choices[i] = Integer.toString(i + 1);
        }
        return choices;
    }
}
