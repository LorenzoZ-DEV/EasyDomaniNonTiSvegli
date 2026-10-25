package net.anubis.client.gui.mainmenu;

import java.util.List;
import net.anubis.client.ClientBrand;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/MenuAction.class */
@Environment(EnvType.CLIENT)
public enum MenuAction {
    SINGLEPLAYER("Singleplayer"),
    MULTIPLAYER("Multiplayer"),
    REALMS("Realms"),
    OPTIONS("Options"),
    QUIT("Quit"),
    LANGUAGE("Language"),
    ACCESSIBILITY("Accessibility"),
    ANUBIS(ClientBrand.WINDOW_TITLE),
    MODS("Mods Folder"),
    ACCOUNTS("Accounts"),
    CREDITS("Credits");

    private final String label;
    public static final List<MenuAction> MAIN = List.of(SINGLEPLAYER, MULTIPLAYER, REALMS, OPTIONS, QUIT);

    MenuAction(String label) {
        this.label = label;
    }

    public String label() {
        return this.label;
    }
}
