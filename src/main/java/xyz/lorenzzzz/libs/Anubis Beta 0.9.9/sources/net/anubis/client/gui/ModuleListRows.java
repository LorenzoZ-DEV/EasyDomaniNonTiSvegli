package net.anubis.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.ModuleRevision;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/ModuleListRows.class */
@Environment(EnvType.CLIENT)
final class ModuleListRows {
    private static final float SNAP = 0.005f;
    private static final Comparator<Row> WIDEST_FIRST = Comparator.comparingDouble(row -> {
        return -row.nameW;
    }).thenComparing(row2 -> {
        return row2.module.name();
    });
    private static final Comparator<Row> WIDEST_LAST = WIDEST_FIRST.reversed();
    private List<ClientModule> seenEnabled;
    private boolean seenEditMode;
    private boolean unsorted;
    private boolean sortedBottomFirst;
    private final List<Row> rows = new ArrayList();
    private final List<Row> view = Collections.unmodifiableList(this.rows);
    private long seenRevision = Long.MIN_VALUE;

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/ModuleListRows$TextWidth.class */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    interface TextWidth<F> {
        float of(F f, int i, String str);
    }

    ModuleListRows() {
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/ModuleListRows$Row.class */
    @Environment(EnvType.CLIENT)
    static final class Row {
        private final ClientModule module;
        private float anim;
        private float nameW;
        private Object measuredFont;
        private int measuredPx = -1;

        private Row(ClientModule module) {
            this.module = module;
        }

        ClientModule module() {
            return this.module;
        }

        float anim() {
            return this.anim;
        }

        float nameW() {
            return this.nameW;
        }
    }

    void update(List<ClientModule> enabled, boolean editMode, float dt) {
        long revision = ModuleRevision.current();
        if (enabled != this.seenEnabled || revision != this.seenRevision || editMode != this.seenEditMode) {
            this.seenEnabled = enabled;
            this.seenRevision = revision;
            this.seenEditMode = editMode;
            this.rows.removeIf(row -> {
                return !listed(row.module, editMode) || (row.module.isEnabled() && !enabled.contains(row.module));
            });
            for (ClientModule module : enabled) {
                if (listed(module, editMode) && !hasRow(module)) {
                    this.rows.add(new Row(module));
                    this.unsorted = true;
                }
            }
        }
        float k = 1.0f - ((float) Math.exp((-12.0f) * dt));
        for (int i = this.rows.size() - 1; i >= 0; i--) {
            Row row2 = this.rows.get(i);
            float target = row2.module.isEnabled() ? 1.0f : 0.0f;
            if (row2.anim != target) {
                float next = row2.anim + ((target - row2.anim) * k);
                if (Math.abs(next - target) < SNAP) {
                    next = target;
                }
                row2.anim = next;
                if (next == 0.0f) {
                    this.rows.remove(i);
                }
            }
        }
    }

    <F> List<Row> ordered(F font, int px, TextWidth<F> width, boolean bottomFirst) {
        for (int i = 0; i < this.rows.size(); i++) {
            Row row = this.rows.get(i);
            if (row.measuredPx != px || row.measuredFont != font) {
                row.nameW = width.of(font, px, row.module.name());
                row.measuredFont = font;
                row.measuredPx = px;
                this.unsorted = true;
            }
        }
        if (this.unsorted || bottomFirst != this.sortedBottomFirst) {
            this.rows.sort(bottomFirst ? WIDEST_LAST : WIDEST_FIRST);
            this.unsorted = false;
            this.sortedBottomFirst = bottomFirst;
        }
        return this.view;
    }

    private static boolean listed(ClientModule module, boolean editMode) {
        return module.category() != ModuleCategory.CLIENT && (editMode || module.showInHud() == null || module.showInHud().get());
    }

    private boolean hasRow(ClientModule module) {
        for (int i = 0; i < this.rows.size(); i++) {
            if (this.rows.get(i).module == module) {
                return true;
            }
        }
        return false;
    }
}
