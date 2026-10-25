package net.anubis.client.gui;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import net.anubis.client.config.HudTheme;
import net.anubis.client.config.ModuleConfigStore;
import net.anubis.client.config.ThemeStore;
import net.anubis.client.imgui.ImGuiManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11908;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/ImGuiScreen.class */
@Environment(EnvType.CLIENT)
public final class ImGuiScreen extends class_437 {
    private final ClickGuiRenderer clickGui;

    public ImGuiScreen() {
        super(class_2561.method_43470("ANUBIS"));
        this.clickGui = new ClickGuiRenderer();
    }

    protected void method_25426() {
        super.method_25426();
        ImGuiManager imGuiManager = ImGuiManager.getInstance();
        ClickGuiRenderer clickGuiRenderer = this.clickGui;
        Objects.requireNonNull(clickGuiRenderer);
        imGuiManager.activate(clickGuiRenderer::render);
    }

    public void method_25432() {
        super.method_25432();
        this.clickGui.handleEscape();
        ImGuiManager.getInstance().deactivate();
        ModuleConfigStore.getInstance().save();
        ThemeStore.get().save();
        HudTheme.get().save();
    }

    public void method_25420(class_332 guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (ThemeStore.get().menuBlur.get()) {
            super.method_25420(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    public void method_29638(List<Path> paths) {
        this.clickGui.onFilesDropped(paths);
    }

    public void method_25394(class_332 graphics, int mouseX, int mouseY, float tickDelta) {
    }

    public boolean method_25404(class_11908 event) {
        if (event.comp_4795() == 256 && this.clickGui.handleEscape()) {
            return true;
        }
        return super.method_25404(event);
    }

    public boolean method_25421() {
        return false;
    }
}
