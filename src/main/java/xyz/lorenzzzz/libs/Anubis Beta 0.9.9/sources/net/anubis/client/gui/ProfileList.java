package net.anubis.client.gui;

import imgui.ImDrawList;
import imgui.ImFont;
import imgui.ImGui;
import imgui.type.ImString;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import net.anubis.client.config.ConfigProfiles;
import net.anubis.client.config.ProfileException;
import net.anubis.client.imgui.ImGuiManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/ProfileList.class */
@Environment(EnvType.CLIENT)
final class ProfileList {
    private static final float ROW_H = 52.0f;
    private static final float CHIP_W = 58.0f;
    private static final float CHIP_H = 28.0f;
    private static final float GAP = 6.0f;
    private static final float LABEL_W = 96.0f;
    private static final float FIELD_H = 30.0f;
    private static final long ARM_NANOS = 3000000000L;
    private static final int DISCORD_LIMIT = 2000;
    private static final String PRIVATE_NOTE = "Webhooks, macros, seeds and other private settings are not included.";
    private final BiConsumer<String, Boolean> messages;
    private String expanded;
    private String armed;
    private long armedAt;
    private String scrollTo;
    private boolean focusServer;
    private final ImString renameInput = new ImString(64);
    private final ImString serverInput = new ImString(256);
    private final Map<String, Float> editorHeights = new HashMap();
    private float scrollTarget = Float.NaN;

    ProfileList(BiConsumer<String, Boolean> messages) {
        this.messages = messages;
    }

    void open(String name) {
        this.expanded = null;
        toggle(name);
        this.scrollTo = key(name);
    }

    void disarm() {
        this.armed = null;
    }

    float takeScrollTarget() {
        float target = this.scrollTarget;
        this.scrollTarget = Float.NaN;
        return target;
    }

    void render(ConfigProfiles profiles, List<ConfigProfiles.Profile> list, String active, float pad, float contentW) {
        for (ConfigProfiles.Profile profile : list) {
            row(profiles, profile, profile.name().equalsIgnoreCase(active), pad, contentW);
        }
    }

    private void row(ConfigProfiles profiles, ConfigProfiles.Profile profile, boolean active, float pad, float contentW) {
        String name = profile.name();
        String key = key(name);
        boolean open = key.equals(this.expanded);
        ImGui.pushID("profile_" + key);
        float localY = ImGui.getCursorPosY();
        if (key.equals(this.scrollTo)) {
            this.scrollTarget = localY - AnubisWidgets.s(8.0f);
            this.scrollTo = null;
        }
        float rowH = AnubisWidgets.s(ROW_H);
        float reveal = AnubisWidgets.animate(ImGui.getID("##reveal"), open, 16.0f);
        float editorH = reveal > 0.001f ? this.editorHeights.getOrDefault(key, Float.valueOf(AnubisWidgets.s(240.0f))).floatValue() * reveal : 0.0f;
        ImGui.setCursorPos(pad, localY);
        float x0 = ImGui.getCursorScreenPosX();
        float y0 = ImGui.getCursorScreenPosY();
        ImGui.setNextItemAllowOverlap();
        ImGui.invisibleButton("##row", contentW, rowH);
        if (ImGui.isItemHovered() && ImGui.isMouseDoubleClicked(0)) {
            load(profiles, profile);
        }
        boolean rowHovered = ImGui.isMouseHoveringRect(x0, y0, x0 + contentW, y0 + rowH);
        float hover = AnubisWidgets.animate(ImGui.getItemID(), rowHovered, 18.0f);
        ImDrawList dl = ImGui.getWindowDrawList();
        float left = x0 - AnubisWidgets.s(8.0f);
        float right = x0 + contentW + AnubisWidgets.s(8.0f);
        float r = AnubisWidgets.s(10.0f);
        int corners = reveal > 0.001f ? 48 : 240;
        if (reveal > 0.001f) {
            dl.addRectFilled(left, y0, right, y0 + rowH + editorH, AnubisWidgets.c(AnubisWidgets.withAlpha(184549375, reveal)), r);
        }
        if (active) {
            dl.addRectFilled(left, y0, right, y0 + rowH, AnubisWidgets.c(AnubisWidgets.withAlpha(GuiPalette.accent(), 0.16f)), r, corners);
        }
        if (hover > 0.01f) {
            dl.addRectFilled(left, y0, right, y0 + rowH, AnubisWidgets.c(AnubisWidgets.withAlpha(218103807, hover)), r, corners);
        }
        ImFont font = ImGui.getFont();
        ImFont small = ImGuiManager.getInstance().smallFont();
        float chipW = AnubisWidgets.s(CHIP_W);
        float chipH = AnubisWidgets.s(CHIP_H);
        float editSize = AnubisWidgets.s(CHIP_H);
        float gap = AnubisWidgets.s(GAP);
        float buttonsW = (chipW * 2.0f) + editSize + (gap * 2.0f);
        float textRoom = (contentW - buttonsW) - AnubisWidgets.s(12.0f);
        float badgeW = active ? AnubisWidgets.textWidth(small, "Active") + AnubisWidgets.s(14.0f) : 0.0f;
        String shown = AnubisWidgets.ellipsize(font, name, textRoom - (active ? badgeW + AnubisWidgets.s(8.0f) : 0.0f));
        float lineH = AnubisWidgets.lineHeight(font);
        float smallH = AnubisWidgets.lineHeight(small);
        float ty = y0 + ((((rowH - lineH) - AnubisWidgets.s(3.0f)) - smallH) * 0.5f);
        AnubisWidgets.text(dl, font, x0, ty, AnubisTheme.GUI_TEXT, shown);
        if (active) {
            float bx = x0 + AnubisWidgets.textWidth(font, shown) + AnubisWidgets.s(8.0f);
            float bh = smallH + AnubisWidgets.s(GAP);
            float by = ty + ((lineH - bh) * 0.5f);
            dl.addRectFilled(bx, by, bx + badgeW, by + bh, AnubisWidgets.c(GuiPalette.accent()), bh * 0.5f);
            AnubisWidgets.text(dl, small, bx + AnubisWidgets.s(7.0f), by + AnubisWidgets.s(3.0f), -1, "Active");
        }
        AnubisWidgets.text(dl, small, x0, ty + lineH + AnubisWidgets.s(3.0f), profile.broken() ? ClickGuiRenderer.ERROR_TEXT : AnubisTheme.GUI_TEXT_MUTED, AnubisWidgets.ellipsize(small, summary(profile), textRoom));
        float bx2 = (pad + contentW) - buttonsW;
        float by2 = localY + ((rowH - chipH) * 0.5f);
        ImGui.setCursorPos(bx2, by2);
        if (AnubisWidgets.chipButton("##load", "Load", chipW, chipH, false)) {
            load(profiles, profile);
        }
        if (ImGui.isItemHovered(65536)) {
            ClickGuiRenderer.showTooltip("Apply the saved " + partList(profile.parts()) + " of this profile");
        }
        String saveKey = "save:" + key;
        boolean saveArmed = isArmed(saveKey);
        ImGui.setCursorPos(bx2 + chipW + gap, by2);
        if (AnubisWidgets.chipButton("##save", saveArmed ? "Sure?" : "Save", chipW, chipH, saveArmed) && confirm(saveKey)) {
            save(profiles, profile);
        }
        if (ImGui.isItemHovered(65536)) {
            ClickGuiRenderer.showTooltip("Overwrite this profile with your current " + partList(profile.parts()));
        }
        ImGui.setCursorPos(bx2 + ((chipW + gap) * 2.0f), localY + ((rowH - editSize) * 0.5f));
        float ex = ImGui.getCursorScreenPosX() + (editSize * 0.5f);
        float ey = ImGui.getCursorScreenPosY() + (editSize * 0.5f);
        if (ImGui.invisibleButton("##edit", editSize, editSize)) {
            toggle(name);
        }
        boolean editHovered = ImGui.isItemHovered();
        float editHover = Math.max(AnubisWidgets.animate(ImGui.getItemID(), editHovered, 18.0f), reveal);
        if (editHover > 0.01f) {
            dl.addCircleFilled(ex, ey, editSize * 0.5f, AnubisWidgets.c(AnubisWidgets.withAlpha(AnubisTheme.GUI_SURFACE_HOVER, editHover)), 32);
        }
        AnubisWidgets.chevronRotated(dl, ex, ey, AnubisWidgets.s(4.0f), AnubisTheme.lerpColor(AnubisTheme.GUI_TEXT_MUTED, AnubisTheme.GUI_TEXT, editHover), reveal * 1.5707964f);
        if (editHovered) {
            ClickGuiRenderer.showTooltip(open ? "Close" : "Edit, share or delete");
        }
        float bottom = localY + rowH;
        if (reveal > 0.001f) {
            ImGui.pushClipRect(left, y0 + rowH, right, y0 + rowH + editorH, true);
            ImGui.pushStyleVar(0, ImGui.getStyle().getAlpha() * reveal);
            this.editorHeights.put(key, Float.valueOf(editor(profiles, profile, key, pad, contentW, bottom)));
            ImGui.popStyleVar();
            ImGui.popClipRect();
        }
        ImGui.setCursorPos(0.0f, bottom + editorH + AnubisWidgets.s(4.0f));
        ImGui.popID();
    }

    private float editor(ConfigProfiles profiles, ConfigProfiles.Profile profile, String key, float pad, float contentW, float top) {
        ImDrawList dl = ImGui.getWindowDrawList();
        ImFont font = ImGui.getFont();
        float h = AnubisWidgets.s(FIELD_H);
        float gap = AnubisWidgets.s(GAP);
        float framePadY = (h - ImGui.getTextLineHeight()) * 0.5f;
        float fieldX = pad + AnubisWidgets.s(LABEL_W);
        float fieldW = contentW - AnubisWidgets.s(LABEL_W);
        float y = top + AnubisWidgets.s(GAP);
        label("Name", pad, y, h);
        float renameW = AnubisWidgets.s(78.0f);
        ImGui.setCursorPos(fieldX, y);
        ImGui.pushStyleVar(11, AnubisWidgets.s(10.0f), framePadY);
        ImGui.setNextItemWidth((fieldW - renameW) - gap);
        ImGui.inputTextWithHint("##rename", "Profile name", this.renameInput);
        boolean rename = AnubisWidgets.submittedWithEnter();
        ImGui.popStyleVar();
        ImGui.setCursorPos((pad + contentW) - renameW, y);
        if (AnubisWidgets.chipButton("##rename_go", "Rename", renameW, h, false)) {
            rename = true;
        }
        if (rename) {
            rename(profiles, profile);
        }
        float y2 = y + h + AnubisWidgets.s(10.0f);
        label("Includes", pad, y2, h);
        ConfigProfiles.Part[] parts = ConfigProfiles.Part.values();
        float partW = (fieldW - (gap * (parts.length - 1))) / parts.length;
        for (int i = 0; i < parts.length; i++) {
            ConfigProfiles.Part part = parts[i];
            boolean on = profile.parts().contains(part);
            ImGui.setCursorPos(fieldX + (i * (partW + gap)), y2);
            if (AnubisWidgets.chipButton("##part_" + part.key, part.label, partW, h, on)) {
                setPart(profiles, profile, part, !on);
            }
            if (ImGui.isItemHovered(65536)) {
                ClickGuiRenderer.showTooltip(partTip(part, on));
            }
        }
        float y3 = y2 + h + AnubisWidgets.s(10.0f);
        label("Load on join", pad, y3, h);
        for (String server : profile.servers()) {
            ImGui.pushID("server_" + server);
            ImGui.setCursorPos(fieldX, y3);
            float sx = ImGui.getCursorScreenPosX();
            float sy = ImGui.getCursorScreenPosY();
            dl.addRectFilled(sx, sy, sx + fieldW, sy + h, AnubisWidgets.c(AnubisTheme.GUI_SURFACE), AnubisWidgets.s(7.0f));
            AnubisWidgets.text(dl, font, sx + AnubisWidgets.s(10.0f), sy + ((h - AnubisWidgets.lineHeight(font)) * 0.5f), AnubisTheme.GUI_TEXT, AnubisWidgets.ellipsize(font, server, (fieldW - h) - AnubisWidgets.s(14.0f)));
            ImGui.setCursorPos((fieldX + fieldW) - h, y3);
            if (ImGui.invisibleButton("##remove", h, h)) {
                removeServer(profiles, profile, server);
            }
            boolean removeHovered = ImGui.isItemHovered();
            float removeHover = AnubisWidgets.animate(ImGui.getItemID(), removeHovered, 18.0f);
            float rcx = (sx + fieldW) - (h * 0.5f);
            float rcy = sy + (h * 0.5f);
            if (removeHover > 0.01f) {
                dl.addCircleFilled(rcx, rcy, h * 0.38f, AnubisWidgets.c(AnubisWidgets.withAlpha(ClickGuiRenderer.ERROR_TEXT, 0.18f * removeHover)), 24);
            }
            AnubisWidgets.cross(dl, rcx, rcy, AnubisWidgets.s(3.5f), AnubisTheme.lerpColor(AnubisTheme.GUI_TEXT_MUTED, ClickGuiRenderer.ERROR_TEXT, removeHover));
            if (removeHovered) {
                ClickGuiRenderer.showTooltip("Stop loading on " + server);
            }
            ImGui.popID();
            y3 += h + gap;
        }
        float addW = AnubisWidgets.s(60.0f);
        ImGui.setCursorPos(fieldX, y3);
        ImGui.pushStyleVar(11, AnubisWidgets.s(10.0f), framePadY);
        ImGui.setNextItemWidth((fieldW - addW) - gap);
        if (this.focusServer) {
            ImGui.setKeyboardFocusHere();
            this.focusServer = false;
        }
        ImGui.inputTextWithHint("##server", "Server address", this.serverInput, 16);
        boolean add = AnubisWidgets.submittedWithEnter();
        ImGui.popStyleVar();
        ImGui.setCursorPos((pad + contentW) - addW, y3);
        if (AnubisWidgets.chipButton("##server_add", "Add", addW, h, false)) {
            add = true;
        }
        if (ImGui.isItemHovered(65536)) {
            ClickGuiRenderer.showTooltip("Loads this profile when you join one of these servers.");
        }
        if (add) {
            addServer(profiles, profile);
        }
        float y4 = y3 + h + AnubisWidgets.s(12.0f);
        float actionW = (contentW - (gap * 2.0f)) / 3.0f;
        ImGui.setCursorPos(pad, y4);
        if (AnubisWidgets.chipButton("##export", "Export file", actionW, h, false)) {
            export(profile);
        }
        if (ImGui.isItemHovered(65536)) {
            ClickGuiRenderer.showTooltip("Save this profile as a .json file.\nWebhooks, macros, seeds and other private settings are not included.");
        }
        ImGui.setCursorPos(pad + actionW + gap, y4);
        if (AnubisWidgets.chipButton("##code", "Copy code", actionW, h, false)) {
            copyCode(profiles, profile);
        }
        if (ImGui.isItemHovered(65536)) {
            ClickGuiRenderer.showTooltip("Copy a share code. Others load it with Paste code.\nWebhooks, macros, seeds and other private settings are not included.");
        }
        String deleteKey = "delete:" + key;
        boolean deleteArmed = isArmed(deleteKey);
        ImGui.setCursorPos(pad + ((actionW + gap) * 2.0f), y4);
        if (dangerChip("##delete", deleteArmed ? "Sure?" : "Delete", actionW, h, deleteArmed) && confirm(deleteKey)) {
            delete(profiles, profile);
        }
        if (ImGui.isItemHovered(65536)) {
            ClickGuiRenderer.showTooltip(deleteArmed ? "Click again to delete" : "Delete this profile");
        }
        return (y4 + (h + AnubisWidgets.s(12.0f))) - top;
    }

    private void load(ConfigProfiles profiles, ConfigProfiles.Profile profile) {
        try {
            Set<ConfigProfiles.Part> applied = profiles.load(profile.name());
            this.messages.accept("Loaded " + profile.name() + " (" + partList(applied) + ")", false);
        } catch (ProfileException e) {
            this.messages.accept(e.getMessage(), true);
        }
    }

    private void save(ConfigProfiles profiles, ConfigProfiles.Profile profile) {
        try {
            Set<ConfigProfiles.Part> saved = profiles.save(profile.name());
            this.messages.accept("Saved your current " + partList(saved) + " to " + profile.name(), false);
        } catch (ProfileException e) {
            this.messages.accept(e.getMessage(), true);
        }
    }

    private void rename(ConfigProfiles profiles, ConfigProfiles.Profile profile) {
        try {
            String renamed = profiles.rename(profile.name(), this.renameInput.get());
            if (renamed.equals(profile.name())) {
                return;
            }
            this.expanded = key(renamed);
            this.renameInput.set(renamed);
            this.messages.accept("Renamed " + profile.name() + " to " + renamed, false);
        } catch (ProfileException e) {
            this.messages.accept(e.getMessage(), true);
        }
    }

    private void setPart(ConfigProfiles profiles, ConfigProfiles.Profile profile, ConfigProfiles.Part part, boolean included) {
        try {
            profiles.setPart(profile.name(), part, included);
            this.messages.accept(included ? "Saved your current " + part.label + " into " + profile.name() : part.label + " is no longer part of " + profile.name(), false);
        } catch (ProfileException e) {
            this.messages.accept(e.getMessage(), true);
        }
    }

    private void addServer(ConfigProfiles profiles, ConfigProfiles.Profile profile) {
        try {
            String added = profiles.addServer(profile.name(), this.serverInput.get());
            this.serverInput.set("");
            this.messages.accept(profile.name() + " now loads when you join " + added, false);
        } catch (ProfileException e) {
            this.messages.accept(e.getMessage(), true);
        }
        this.focusServer = true;
    }

    private void removeServer(ConfigProfiles profiles, ConfigProfiles.Profile profile, String server) {
        try {
            profiles.removeServer(profile.name(), server);
            this.messages.accept(profile.name() + " no longer loads on " + server, false);
        } catch (ProfileException e) {
            this.messages.accept(e.getMessage(), true);
        }
    }

    private void export(ConfigProfiles.Profile profile) {
        if (FileDialogs.busy()) {
            this.messages.accept("A file window is already open", true);
        } else {
            String name = profile.name();
            FileDialogs.saveProfile("Export " + name, name + ".json", path -> {
                String fileName = path.getFileName().toString();
                Path target = fileName.toLowerCase(Locale.ROOT).endsWith(".json") ? path : path.resolveSibling(fileName + ".json");
                try {
                    ConfigProfiles.getInstance().exportTo(name, target);
                    this.messages.accept("Exported " + name + " to " + String.valueOf(target.getFileName()), false);
                } catch (ProfileException e) {
                    this.messages.accept(e.getMessage(), true);
                }
            });
        }
    }

    private void copyCode(ConfigProfiles profiles, ConfigProfiles.Profile profile) {
        String str;
        try {
            String code = profiles.shareCode(profile.name());
            class_310.method_1551().field_1774.method_1455(code);
            BiConsumer<String, Boolean> biConsumer = this.messages;
            if (code.length() > DISCORD_LIMIT) {
                str = "Copied, but it is too long for Discord. Send the file instead";
            } else {
                str = "Share code for " + profile.name() + " copied";
            }
            biConsumer.accept(str, false);
        } catch (ProfileException e) {
            this.messages.accept(e.getMessage(), true);
        }
    }

    private void delete(ConfigProfiles profiles, ConfigProfiles.Profile profile) {
        try {
            profiles.delete(profile.name());
            if (key(profile.name()).equals(this.expanded)) {
                this.expanded = null;
            }
            this.messages.accept("Deleted " + profile.name(), false);
        } catch (ProfileException e) {
            this.messages.accept(e.getMessage(), true);
        }
    }

    private void toggle(String name) {
        String key = key(name);
        if (key.equals(this.expanded)) {
            this.expanded = null;
            return;
        }
        this.expanded = key;
        this.renameInput.set(name);
        this.serverInput.set("");
    }

    private boolean isArmed(String key) {
        return key.equals(this.armed) && System.nanoTime() - this.armedAt < ARM_NANOS;
    }

    private boolean confirm(String key) {
        if (isArmed(key)) {
            this.armed = null;
            return true;
        }
        this.armed = key;
        this.armedAt = System.nanoTime();
        return false;
    }

    private static boolean dangerChip(String id, String label, float w, float h, boolean armed) {
        float sx = ImGui.getCursorScreenPosX();
        float sy = ImGui.getCursorScreenPosY();
        boolean clicked = ImGui.invisibleButton(id, w, h);
        float hover = AnubisWidgets.animate(ImGui.getItemID(), ImGui.isItemHovered(), 18.0f);
        int bg = armed ? ClickGuiRenderer.ERROR_TEXT : AnubisTheme.lerpColor(AnubisTheme.GUI_SURFACE, AnubisTheme.GUI_SURFACE_HOVER, hover);
        if (ImGui.isItemActive()) {
            bg = AnubisTheme.lerpColor(bg, -16777216, 0.12f);
        }
        ImDrawList dl = ImGui.getWindowDrawList();
        dl.addRectFilled(sx, sy, sx + w, sy + h, AnubisWidgets.c(bg), AnubisWidgets.s(7.0f));
        ImFont font = ImGui.getFont();
        int text = armed ? -1 : AnubisTheme.lerpColor(AnubisTheme.GUI_TEXT, ClickGuiRenderer.ERROR_TEXT, hover);
        AnubisWidgets.text(dl, font, sx + ((w - AnubisWidgets.textWidth(font, label)) * 0.5f), sy + ((h - AnubisWidgets.lineHeight(font)) * 0.5f), text, label);
        return clicked;
    }

    private static void label(String text, float x, float y, float h) {
        ImFont small = ImGuiManager.getInstance().smallFont();
        float sx = ImGui.getWindowPosX() + x;
        float sy = (ImGui.getWindowPosY() - ImGui.getScrollY()) + y;
        AnubisWidgets.text(ImGui.getWindowDrawList(), small, sx, sy + ((h - AnubisWidgets.lineHeight(small)) * 0.5f), AnubisTheme.GUI_TEXT_MUTED, text);
    }

    private static String summary(ConfigProfiles.Profile profile) {
        if (profile.broken()) {
            return "Can't be read - fix the file or delete it";
        }
        String parts = partList(profile.parts());
        List<String> servers = profile.servers();
        if (servers.size() == 1) {
            return parts + "  ·  loads on " + servers.get(0);
        }
        return servers.size() > 1 ? parts + "  ·  loads on " + servers.size() + " servers" : parts;
    }

    private static String partList(Set<ConfigProfiles.Part> parts) {
        StringBuilder text = new StringBuilder();
        for (ConfigProfiles.Part part : parts) {
            if (!text.isEmpty()) {
                text.append(", ");
            }
            text.append(part.label);
        }
        return text.isEmpty() ? "nothing" : text.toString();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
    private static String partTip(ConfigProfiles.Part part, boolean included) throws MatchException {
        String str;
        switch (part) {
            case MODULES:
                str = "Module settings, keybinds and toggles.";
                break;
            case HUD:
                str = "HUD layout and the HUD theme.";
                break;
            case THEME:
                str = "GUI theme.";
                break;
            case FRIENDS:
                str = "Your friends list.";
                break;
            default:
                throw new MatchException((String) null, (Throwable) null);
        }
        String what = str;
        return what + (included ? "\nClick to remove it from this profile." : "\nClick to add it to this profile.");
    }

    private static String key(String name) {
        return name.toLowerCase(Locale.ROOT);
    }
}
