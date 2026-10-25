package net.anubis.client.module;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.anubis.client.ClientBrand;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/ModuleCategory.class */
@Environment(EnvType.CLIENT)
public final class ModuleCategory {
    private static final List<ModuleCategory> REGISTERED = new ArrayList();
    private static volatile List<ModuleCategory> values = List.of();
    public static final ModuleCategory COMBAT = register("COMBAT", "Combat", Icon.CROSSHAIR);
    public static final ModuleCategory MISC = register("MISC", "Misc", Icon.GRID);
    public static final ModuleCategory DONUT = register("DONUT", "Donut", Icon.RING);
    public static final ModuleCategory RENDER = register("RENDER", "Render", Icon.EYE);
    public static final ModuleCategory CLIENT = register("CLIENT", ClientBrand.WINDOW_TITLE, Icon.ANKH);
    public static final ModuleCategory MOVEMENT = register("MOVEMENT", "Movement", Icon.CHEVRON);
    public static final ModuleCategory BASE_RAID = register("BASE_RAID", "Base Raid", Icon.HOUSE);
    public static final ModuleCategory VISUALS = register("VISUALS", "Visuals", Icon.MONITOR);
    private final String id;
    private final String displayName;
    private final Icon icon;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/ModuleCategory$Icon.class */
    @Environment(EnvType.CLIENT)
    public enum Icon {
        CROSSHAIR,
        CHEVRON,
        GRID,
        RING,
        EYE,
        MONITOR,
        HOUSE,
        PAPYRUS,
        ANKH
    }

    private ModuleCategory(String id, String displayName, Icon icon) {
        this.id = id;
        this.displayName = displayName;
        this.icon = icon;
    }

    public static synchronized ModuleCategory register(String id, String displayName, Icon icon) {
        if (!isValidId(id)) {
            throw new IllegalArgumentException("Category id must be upper-case letters, digits and '_': " + id);
        }
        Iterator<ModuleCategory> it = REGISTERED.iterator();
        while (it.hasNext()) {
            if (it.next().id.equals(id)) {
                throw new IllegalArgumentException("Duplicate category id: " + id);
            }
        }
        ModuleCategory category = new ModuleCategory(id, (String) Objects.requireNonNull(displayName, "displayName"), (Icon) Objects.requireNonNull(icon, "icon"));
        REGISTERED.add(category);
        values = List.copyOf(REGISTERED);
        return category;
    }

    private static boolean isValidId(String id) {
        if (id == null || id.isEmpty() || id.charAt(0) < 'A' || id.charAt(0) > 'Z') {
            return false;
        }
        for (int i = 1; i < id.length(); i++) {
            char c = id.charAt(i);
            if ((c < 'A' || c > 'Z') && ((c < '0' || c > '9') && c != '_')) {
                return false;
            }
        }
        return true;
    }

    public static ModuleCategory register(String id, String displayName) {
        return register(id, displayName, Icon.ANKH);
    }

    public static List<ModuleCategory> values() {
        return values;
    }

    public String name() {
        return this.id;
    }

    public String displayName() {
        return this.displayName;
    }

    public Icon icon() {
        return this.icon;
    }

    public String toString() {
        return this.id;
    }
}
