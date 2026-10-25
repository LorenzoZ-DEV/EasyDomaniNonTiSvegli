package net.anubis.client.module;

import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/StableId.class */
@Environment(EnvType.CLIENT)
public final class StableId {
    private StableId() {
    }

    public static String validate(String id) {
        if (isValid(id)) {
            return id;
        }
        throw new IllegalArgumentException("Invalid stable id: " + id);
    }

    public static String legacy(String label) {
        String lower = label.toLowerCase(Locale.ROOT);
        StringBuilder id = new StringBuilder(lower.length());
        boolean pendingDash = false;
        for (int i = 0; i < lower.length(); i++) {
            char c = lower.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')) {
                if (pendingDash && !id.isEmpty()) {
                    id.append('-');
                }
                pendingDash = false;
                id.append(c);
            } else {
                pendingDash = true;
            }
        }
        return validate(id.toString());
    }

    private static boolean isValid(String id) {
        boolean z;
        if (id == null || id.isEmpty()) {
            return false;
        }
        boolean afterSeparator = true;
        for (int i = 0; i < id.length(); i++) {
            char c = id.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')) {
                z = false;
            } else {
                if ((c != '.' && c != '_' && c != '-') || afterSeparator) {
                    return false;
                }
                z = true;
            }
            afterSeparator = z;
        }
        return !afterSeparator;
    }
}
