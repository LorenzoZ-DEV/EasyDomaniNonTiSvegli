package net.anubis.client.util.staff;

import java.util.ArrayDeque;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1109;
import net.minecraft.class_1113;
import net.minecraft.class_1144;
import net.minecraft.class_310;
import net.minecraft.class_3414;
import net.minecraft.class_3417;
import net.minecraft.class_3419;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/util/staff/StaffAlarm.class */
@Environment(EnvType.CLIENT)
public final class StaffAlarm {
    public static final String SIREN = "Siren";
    public static final String BEEP = "Beep";
    public static final String BELL = "Bell";
    public static final String GUARDIAN = "Guardian";
    public static final String OFF = "Off";
    private static final int SIREN_INTERVAL = 4;
    private static final float SIREN_HIGH = 1.5f;
    private static final float SIREN_LOW = 1.0f;
    private static final int BEEP_INTERVAL = 3;
    private static final float BEEP_PITCH = 1.6f;
    private static final int BELL_INTERVAL = 10;
    private static final int GUARDIAN_INTERVAL = 60;
    private static final int MAX_RINGING = 8;
    private float volume;
    private int ticksLeft;
    private int step;
    private String mode = OFF;
    private final ArrayDeque<class_1113> ringing = new ArrayDeque<>(8);

    public void start(String mode, float seconds, float volume) {
        if (mode == null || OFF.equals(mode) || volume <= 0.0f || seconds <= 0.0f) {
            return;
        }
        int ticks = Math.max(1, Math.round(seconds * 20.0f));
        this.volume = Math.min(1.0f, volume);
        if (isPlaying() && mode.equals(this.mode)) {
            this.ticksLeft = Math.max(this.ticksLeft, ticks);
            return;
        }
        stop();
        this.mode = mode;
        this.ticksLeft = ticks;
        this.step = 0;
    }

    public void tick() {
        if (this.ticksLeft <= 0) {
            return;
        }
        int interval = interval(this.mode);
        if (this.step % interval == 0) {
            play(this.step / interval);
        }
        this.step++;
        int i = this.ticksLeft - 1;
        this.ticksLeft = i;
        if (i == 0) {
            this.step = 0;
        }
    }

    public void stop() {
        this.ticksLeft = 0;
        this.step = 0;
        if (this.ringing.isEmpty()) {
            return;
        }
        class_1144 sounds = class_310.method_1551().method_1483();
        if (sounds != null) {
            ArrayDeque<class_1113> arrayDeque = this.ringing;
            Objects.requireNonNull(sounds);
            arrayDeque.forEach(sounds::method_4870);
        }
        this.ringing.clear();
    }

    public boolean isPlaying() {
        return this.ticksLeft > 0;
    }

    private void play(int note) {
        class_3414 sound;
        float pitch;
        class_1144 sounds = class_310.method_1551().method_1483();
        if (sounds == null) {
            return;
        }
        switch (this.mode) {
            case "Beep":
                sound = (class_3414) class_3417.field_18311.comp_349();
                pitch = 1.6f;
                break;
            case "Bell":
                sound = class_3417.field_17265;
                pitch = 1.0f;
                break;
            case "Guardian":
                sound = class_3417.field_15203;
                pitch = 1.0f;
                break;
            default:
                sound = (class_3414) class_3417.field_14622.comp_349();
                pitch = note % 2 == 0 ? SIREN_HIGH : 1.0f;
                break;
        }
        class_1113 instance = new class_1109(sound.comp_3319(), class_3419.field_15250, this.volume, pitch, class_1113.method_43221(), false, 0, class_1113.class_1114.field_5478, 0.0d, 0.0d, 0.0d, true);
        if (this.ringing.size() >= 8) {
            this.ringing.removeFirst();
        }
        this.ringing.addLast(instance);
        sounds.method_4873(instance);
    }

    private static int interval(String mode) {
        switch (mode) {
            case "Beep":
                return 3;
            case "Bell":
                return 10;
            case "Guardian":
                return GUARDIAN_INTERVAL;
            default:
                return 4;
        }
    }
}
