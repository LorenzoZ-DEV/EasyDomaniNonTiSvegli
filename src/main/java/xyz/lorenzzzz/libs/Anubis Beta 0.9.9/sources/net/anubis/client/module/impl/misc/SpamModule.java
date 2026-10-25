package net.anubis.client.module.impl.misc;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import net.anubis.client.event.Stage;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.anubis.client.module.setting.NumberSetting;
import net.anubis.client.module.setting.StringListSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import net.minecraft.class_634;
import net.minecraft.class_746;
import org.apache.commons.lang3.RandomStringUtils;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/SpamModule.class */
@Environment(EnvType.CLIENT)
public final class SpamModule extends ClientModule {
    public static volatile SpamModule active;
    private static final int MAX_MESSAGE_LENGTH = 256;
    private final StringListSetting messages;
    private final NumberSetting delay;
    private final BooleanSetting disableOnLeave;
    private final BooleanSetting disableOnDisconnect;
    private final BooleanSetting random;
    private final BooleanSetting autoSplitMessages;
    private final NumberSetting splitLength;
    private final NumberSetting autoSplitDelay;
    private final BooleanSetting bypass;
    private final BooleanSetting uppercase;
    private final NumberSetting length;
    private final Random rng;
    private int messageI;
    private int timer;
    private int splitNum;
    private String text;
    private boolean resetPending;

    public SpamModule() {
        super("spam", "Spam", "Spams specified messages in chat.", ModuleCategory.MISC);
        this.messages = (StringListSetting) addSetting("messages", new StringListSetting("Messages", "Anubis was here!"));
        this.delay = (NumberSetting) addSetting("delay", new NumberSetting("Delay", 20.0f, 0.0f, 1200.0f).step(1.0f));
        this.disableOnLeave = (BooleanSetting) addSetting("disable-on-leave", new BooleanSetting("Disable On Leave", true));
        this.disableOnDisconnect = (BooleanSetting) addSetting("disable-on-disconnect", new BooleanSetting("Disable On Disconnect", true));
        this.random = (BooleanSetting) addSetting("randomise", new BooleanSetting("Randomise", false));
        this.autoSplitMessages = (BooleanSetting) addSetting("auto-split-messages", new BooleanSetting("Auto Split Messages", false));
        this.splitLength = (NumberSetting) addSetting("split-length", new NumberSetting("Split Length", 256.0f, 1.0f, 256.0f).step(1.0f));
        this.autoSplitDelay = (NumberSetting) addSetting("split-delay", new NumberSetting("Split Delay", 20.0f, 0.0f, 200.0f).step(1.0f));
        this.bypass = (BooleanSetting) addSetting("bypass", new BooleanSetting("Bypass", false));
        this.uppercase = (BooleanSetting) addSetting("include-uppercase-characters", new BooleanSetting("Include Uppercase Characters", true));
        this.length = (NumberSetting) addSetting("length", new NumberSetting("Length", 16.0f, 1.0f, 256.0f).step(1.0f));
        this.rng = new Random();
        NumberSetting numberSetting = this.splitLength;
        BooleanSetting booleanSetting = this.autoSplitMessages;
        Objects.requireNonNull(booleanSetting);
        numberSetting.visibleWhen(booleanSetting::get);
        NumberSetting numberSetting2 = this.autoSplitDelay;
        BooleanSetting booleanSetting2 = this.autoSplitMessages;
        Objects.requireNonNull(booleanSetting2);
        numberSetting2.visibleWhen(booleanSetting2::get);
        BooleanSetting booleanSetting3 = this.uppercase;
        BooleanSetting booleanSetting4 = this.bypass;
        Objects.requireNonNull(booleanSetting4);
        booleanSetting3.visibleWhen(booleanSetting4::get);
        NumberSetting numberSetting3 = this.length;
        BooleanSetting booleanSetting5 = this.bypass;
        Objects.requireNonNull(booleanSetting5);
        numberSetting3.visibleWhen(booleanSetting5::get);
    }

    @Override // net.anubis.client.module.ClientModule
    public boolean inConfigs() {
        return false;
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        this.resetPending = true;
        active = this;
        on(TickEvent.TYPE, Stage.POST, this::onPostTick);
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        active = null;
    }

    public static void onGameLeft() {
        class_310 mc = class_310.method_1551();
        if (!mc.method_18854()) {
            mc.execute(SpamModule::onGameLeft);
            return;
        }
        SpamModule spam = active;
        if (spam != null) {
            spam.handleGameLeft();
        }
    }

    public static void onDisconnectedScreen() {
        class_310 mc = class_310.method_1551();
        if (!mc.method_18854()) {
            mc.execute(SpamModule::onDisconnectedScreen);
            return;
        }
        SpamModule spam = active;
        if (spam == null || !spam.isEnabled() || !spam.disableOnDisconnect.get()) {
            return;
        }
        spam.requestDisable();
    }

    private void handleGameLeft() {
        if (isEnabled()) {
            if (this.disableOnLeave.get()) {
                requestDisable();
            } else {
                this.resetPending = true;
            }
        }
    }

    private void onPostTick(TickEvent event) {
        int i;
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (player == null || mc.field_1687 == null) {
            return;
        }
        if (this.resetPending) {
            this.resetPending = false;
            this.timer = this.delay.getInt();
            this.messageI = 0;
            this.splitNum = 0;
        }
        List<String> list = this.messages.get();
        if (list.isEmpty()) {
            return;
        }
        if (this.timer > 0) {
            this.timer--;
            return;
        }
        if (this.text == null) {
            if (this.random.get()) {
                i = this.rng.nextInt(list.size());
            } else {
                if (this.messageI >= list.size()) {
                    this.messageI = 0;
                }
                int i2 = this.messageI;
                this.messageI = i2 + 1;
                i = i2;
            }
            this.text = list.get(i);
            if (this.bypass.get()) {
                String suffix = RandomStringUtils.insecure().nextAlphabetic(this.length.getInt());
                if (!this.uppercase.get()) {
                    suffix = suffix.toLowerCase(Locale.ROOT);
                }
                this.text += " " + suffix;
            }
        }
        int chunk = this.splitLength.getInt();
        if (this.autoSplitMessages.get() && this.text.length() > chunk) {
            int splits = ((this.text.length() + chunk) - 1) / chunk;
            if (this.splitNum >= splits) {
                this.splitNum = 0;
            }
            int start = this.splitNum * chunk;
            int end = Math.min(start + chunk, this.text.length());
            sendPlayerMsg(mc, player.field_3944, this.text.substring(start, end));
            this.splitNum = (this.splitNum + 1) % splits;
            this.timer = this.autoSplitDelay.getInt();
            if (this.splitNum == 0) {
                this.timer = this.delay.getInt();
                this.text = null;
                return;
            }
            return;
        }
        if (this.text.length() > 256) {
            this.text = this.text.substring(0, 256);
        }
        sendPlayerMsg(mc, player.field_3944, this.text);
        this.timer = this.delay.getInt();
        this.text = null;
    }

    private static void sendPlayerMsg(class_310 mc, class_634 connection, String message) {
        mc.field_1705.method_1743().method_1803(message);
        if (!message.startsWith("/")) {
            connection.method_45729(message);
        } else {
            connection.method_45730(message.substring(1));
        }
    }
}
