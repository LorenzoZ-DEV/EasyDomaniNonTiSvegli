package net.anubis.client.gui.mainmenu;

import imgui.ImDrawList;
import imgui.ImFont;
import imgui.ImGui;
import imgui.ImGuiIO;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import net.anubis.client.account.Account;
import net.anubis.client.account.AccountType;
import net.anubis.client.account.Accounts;
import net.anubis.client.gui.EgyptDraw;
import net.anubis.client.proxy.Proxies;
import net.anubis.client.proxy.Proxy;
import net.anubis.client.proxy.ProxyChecker;
import net.anubis.client.proxy.ProxyType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import org.lwjgl.PointerBuffer;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/ConnectionsPanel.class */
@Environment(EnvType.CLIENT)
public final class ConnectionsPanel {
    private static final String NAME_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_";
    private static final int WINDOW_FLAGS = 447;
    private static final float ACCOUNTS_W = 820.0f;
    private static final float PROXIES_W = 860.0f;
    private static final float PAD = 24.0f;
    private static final float FIELD_H = 50.0f;
    private static final float ACCOUNT_ROW_H = 68.0f;
    private static final float PROXY_ROW_H = 46.0f;
    private static final float TAB_H = 40.0f;
    private static final float BAR_H = 30.0f;
    private static final float FACE = 46.0f;
    private static final float CHECK = 16.0f;
    private static final int ACCOUNT_ROWS = 6;
    private static final int PROXY_ROWS = 8;
    private static final float FADE = 0.15f;
    private static final float CONFIRM_FOR = 3.0f;
    static final int WINDOW = -15461610;
    private static final int ERROR = -1733516;
    private static final int GOOD = -6501498;
    private final Runnable onProxyChange;
    private boolean open;
    private boolean opening;
    private long openedAt;
    private Object armed;
    private float armedUntil;
    private AccountType accountFilter;
    private CompletableFuture<?> microsoft;
    private String accountError;
    private String proxyMessage;
    private boolean proxyError;
    private ProxyType typeFilter;
    private boolean fileDialog;
    private final DuskWidgets ui = new DuskWidgets();
    private final MenuFrame frame = new MenuFrame();
    private Tab tab = Tab.ACCOUNTS;
    private String routeAtOpen = "";
    private AccountType adding = AccountType.CRACKED;
    private final DuskWidgets.TextField crackedName = new DuskWidgets.TextField(16).placeholder("Username").allowedChars(NAME_CHARS);
    private final DuskWidgets.TextField token = new DuskWidgets.TextField(4096).placeholder("Minecraft access token").password(true);
    private final DuskWidgets.TextField proxyInput = new DuskWidgets.TextField(512).placeholder("host:port or socks5://user:pass@host:port");
    private Filter filter = Filter.ALL;
    private Sort sort = Sort.ADDED;
    private final Set<Proxy> selected = new HashSet();

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/ConnectionsPanel$Tab.class */
    @Environment(EnvType.CLIENT)
    public enum Tab {
        ACCOUNTS,
        PROXIES
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/ConnectionsPanel$Filter.class */
    @Environment(EnvType.CLIENT)
    private enum Filter {
        ALL("All"),
        WORKING("Working"),
        DEAD("Dead"),
        UNCHECKED("Unchecked");

        final String label;

        Filter(String label) {
            this.label = label;
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: java.lang.MatchException */
        boolean matches(Proxy proxy) throws MatchException {
            switch (this) {
                case ALL:
                    return true;
                case WORKING:
                    return proxy.ping >= 0;
                case DEAD:
                    return proxy.ping == -1;
                case UNCHECKED:
                    return proxy.ping == -2;
                default:
                    throw new MatchException((String) null, (Throwable) null);
            }
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/ConnectionsPanel$Sort.class */
    @Environment(EnvType.CLIENT)
    private enum Sort {
        ADDED("Added", null),
        PING("Ping", Comparator.comparingInt(ConnectionsPanel::pingOrder)),
        NAME("Name", Comparator.comparing(proxy -> {
            return proxy.name.toLowerCase(Locale.ROOT);
        }));

        final String label;
        final Comparator<Proxy> order;

        Sort(String label, Comparator order) {
            this.label = label;
            this.order = order;
        }
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/gui/mainmenu/ConnectionsPanel$Key.class */
    @Environment(EnvType.CLIENT)
    private static final class Key extends Record {
        private final Object owner;
        private final String what;

        private Key(Object owner, String what) {
            this.owner = owner;
            this.what = what;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, Key.class), Key.class, "owner;what", "FIELD:Lnet/anubis/client/gui/mainmenu/ConnectionsPanel$Key;->owner:Ljava/lang/Object;", "FIELD:Lnet/anubis/client/gui/mainmenu/ConnectionsPanel$Key;->what:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, Key.class), Key.class, "owner;what", "FIELD:Lnet/anubis/client/gui/mainmenu/ConnectionsPanel$Key;->owner:Ljava/lang/Object;", "FIELD:Lnet/anubis/client/gui/mainmenu/ConnectionsPanel$Key;->what:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, Key.class, Object.class), Key.class, "owner;what", "FIELD:Lnet/anubis/client/gui/mainmenu/ConnectionsPanel$Key;->owner:Ljava/lang/Object;", "FIELD:Lnet/anubis/client/gui/mainmenu/ConnectionsPanel$Key;->what:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public Object owner() {
            return this.owner;
        }

        public String what() {
            return this.what;
        }
    }

    public ConnectionsPanel(Runnable onProxyChange) {
        this.onProxyChange = onProxyChange;
    }

    public void open(Tab tab) {
        if (!this.open) {
            this.open = true;
            this.opening = true;
            this.openedAt = System.nanoTime();
            this.routeAtOpen = route();
        }
        this.tab = tab;
        this.armed = null;
        if (tab != Tab.PROXIES) {
            if (this.adding != AccountType.MICROSOFT) {
                field(this.adding).focus();
                return;
            }
            return;
        }
        this.proxyInput.focus();
    }

    public boolean isOpen() {
        return this.open;
    }

    public boolean keyPressed(int key) {
        if (!this.open) {
            return false;
        }
        if (key == 256) {
            close();
            return true;
        }
        return true;
    }

    public void render(float width, float height, float scale, float dt) {
        if (this.open) {
            ImGuiIO io2 = ImGui.getIO();
            boolean click = !this.opening && ImGui.isMouseClicked(0);
            this.opening = false;
            ImGui.setNextWindowPos(0.0f, 0.0f);
            ImGui.setNextWindowSize(width, height);
            ImGui.pushStyleVar(2, 0.0f, 0.0f);
            ImGui.pushStyleVar(4, 0.0f);
            ImGui.begin("##anubis_connections", WINDOW_FLAGS);
            ImGui.popStyleVar(2);
            try {
                this.frame.begin(ImGui.getWindowDrawList(), ImGui.getForegroundDrawList(), width, height, scale, (System.nanoTime() - this.openedAt) / 1.0E9f, dt, io2.getMousePosX(), io2.getMousePosY(), click);
                this.ui.begin(this.frame);
                try {
                    draw();
                    this.ui.end();
                    drawTooltip();
                    ImGui.end();
                } catch (Throwable th) {
                    this.ui.end();
                    throw th;
                }
            } catch (Throwable th2) {
                ImGui.end();
                throw th2;
            }
        }
    }

    private void close() {
        this.open = false;
        this.crackedName.setText("");
        this.token.setText("");
        this.proxyInput.setText("");
        this.selected.clear();
        if (this.microsoft != null) {
            Accounts.get().cancelMicrosoft();
        }
        if (!route().equals(this.routeAtOpen)) {
            this.onProxyChange.run();
        }
    }

    private void draw() {
        float fMax;
        ImDrawList dl = this.frame.bg();
        float s = this.frame.scale();
        float sw = this.frame.width();
        float sh = this.frame.height();
        float fade = Math.min(1.0f, this.frame.time() / FADE);
        float a = 1.0f - ((1.0f - fade) * (1.0f - fade));
        this.ui.alpha(a);
        DuskScene.veil(dl, sw, sh, 0.5f * a);
        boolean accounts = this.tab == Tab.ACCOUNTS;
        List<Proxy> shown = accounts ? List.of() : shownProxies();
        List<Account> listed = accounts ? shownAccounts() : List.of();
        float titleH = this.frame.textHeight(this.frame.itemFont());
        float lineH = this.frame.textHeight(this.frame.bodyFont());
        if (accounts) {
            fMax = Math.max(3, Math.min(listed.size(), 6)) * ACCOUNT_ROW_H * s;
        } else {
            fMax = Math.max(1, Math.min(shown.size(), 8)) * 46.0f * s;
        }
        float listH = fMax;
        float bars = accounts ? 96.0f * s : (60.0f * s) + (22.0f * s);
        float w = Math.min((accounts ? ACCOUNTS_W : PROXIES_W) * s, sw - (32.0f * s));
        float h = Math.min((24.0f * s) + titleH + (16.0f * s) + (FIELD_H * s) + (12.0f * s) + lineH + (18.0f * s) + bars + listH + (16.0f * s), sh - (32.0f * s));
        float x = Math.round((sw - w) * 0.5f);
        float y = Math.round(((sh - h) * 0.5f) + ((1.0f - a) * 6.0f * s));
        for (int i = 1; i <= 3; i++) {
            float g = i * 4.0f * s;
            dl.addRectFilled(x - g, (y - g) + (4.0f * s), x + w + g, y + h + g + (4.0f * s), EgyptDraw.col(-16777216, 0.08f * a));
        }
        dl.addRectFilled(x, y, x + w, y + h, EgyptDraw.col(WINDOW, a));
        dl.addRect(x, y, x + w, y + h, EgyptDraw.col(EgyptDraw.TEXT, 0.1f * a), 0.0f, 0, Math.max(1.0f, s));
        float pad = 24.0f * s;
        this.frame.text(dl, this.frame.itemFont(), x + pad, y + (pad * 0.8f), EgyptDraw.TEXT, a, accounts ? "Accounts" : "Proxies");
        closeCross((x + w) - (44.0f * s), ((y + (pad * 0.8f)) + (titleH * 0.5f)) - (16.0f * s), 32.0f * s, s, a);
        float top = y + pad + titleH + (16.0f * s);
        float listBottom = (y + h) - (16.0f * s);
        if (accounts) {
            accounts(listed, x, top, w, listBottom, s, a);
        } else {
            proxies(shown, x, top, w, listBottom, s, a);
        }
    }

    private void closeCross(float x, float y, float size, float s, float a) {
        ImDrawList dl = this.frame.bg();
        boolean over = this.ui.hovered(x, y, size, size);
        if (this.ui.pressed(x, y, size, size)) {
            close();
        }
        float k = this.ui.hover("close", over);
        float cx = x + (size * 0.5f);
        float cy = y + (size * 0.5f);
        float c = 6.0f * s;
        int color = EgyptDraw.col(EgyptDraw.mix(EgyptDraw.MUTED, EgyptDraw.TEXT, k), a);
        float line = Math.max(1.2f, 1.6f * s);
        dl.addLine(cx - c, cy - c, cx + c, cy + c, color, line);
        dl.addLine(cx - c, cy + c, cx + c, cy - c, color, line);
    }

    private List<Account> shownAccounts() {
        List<Account> all = Accounts.get().list();
        return this.accountFilter == null ? all : all.stream().filter(account -> {
            return account.type() == this.accountFilter;
        }).toList();
    }

    private void accounts(List<Account> listed, float x, float y, float w, float bottom, float s, float a) {
        String str;
        String hint;
        float pad = 24.0f * s;
        float fx = x + pad;
        float fw = w - (2.0f * pad);
        boolean busy = Accounts.get().busy();
        addTabs(fx, y, s, a);
        float y2 = y + (54.0f * s);
        float fh = FIELD_H * s;
        ImFont font = this.frame.bodyFont();
        if (this.adding == AccountType.MICROSOFT) {
            String label = this.microsoft != null ? "Waiting for the browser..." : "Sign in with Microsoft";
            if (box("microsoft", label, fx, y2, fw, fh, this.microsoft == null && !busy, a)) {
                signInMicrosoft();
            }
            hint = "Opens Microsoft's sign-in in your browser";
        } else {
            DuskWidgets.TextField field = field(this.adding);
            if (this.ui.textField(field, fx, y2, fw, fh) && !field.text().isBlank() && !busy) {
                add(field);
            }
            if (this.adding == AccountType.CRACKED) {
                str = "Press Enter to add. Joins servers that don't check accounts.";
            } else {
                str = "Press Enter to add. A token works until it expires, about a day.";
            }
            hint = str;
        }
        float y3 = y2 + fh + (12.0f * s);
        if (this.microsoft != null) {
            float cancelW = this.frame.textWidth(font, "Cancel");
            text(DuskWidgets.fit(font, Accounts.get().status(), (fw - cancelW) - (16.0f * s)), fx, y3, EgyptDraw.TEXT, a);
            if (link("cancel", "Cancel", (fx + fw) - cancelW, y3, true, a)) {
                Accounts.get().cancelMicrosoft();
            }
        } else if (busy) {
            text(DuskWidgets.fit(font, Accounts.get().status(), fw), fx, y3, EgyptDraw.MUTED, a);
        } else if (this.accountError != null) {
            text(DuskWidgets.fit(font, this.accountError, fw), fx, y3, ERROR, a);
        } else {
            text(DuskWidgets.fit(font, hint, fw), fx, y3, EgyptDraw.MUTED, a);
        }
        float y4 = y3 + this.frame.textHeight(font) + (18.0f * s);
        accountFilters(fx, y4, s, a);
        float y5 = y4 + (42.0f * s);
        float listH = bottom - y5;
        if (listed.isEmpty()) {
            centered(this.accountFilter == null ? "No accounts yet" : "No " + this.accountFilter.label() + " accounts", x, y5, w, listH, a);
            return;
        }
        Account active = Accounts.get().active();
        float rowX = x + (8.0f * s);
        float rowW = w - (16.0f * s);
        float rowH = ACCOUNT_ROW_H * s;
        float scroll = this.ui.beginScroll("accounts", rowX, y5, rowW, listH, listed.size() * rowH);
        for (int i = 0; i < listed.size(); i++) {
            Account account = listed.get(i);
            accountRow(account, account.equals(active), rowX, (y5 - scroll) + (i * rowH), rowW, rowH, s, a);
        }
        this.ui.endScroll();
    }

    private void addTabs(float x, float y, float s, float a) {
        ImDrawList dl = this.frame.bg();
        ImFont font = this.frame.menuFont();
        float h = 40.0f * s;
        float ty = y + ((h - this.frame.textHeight(font)) * 0.5f);
        AccountType[] accountTypeArrValues = AccountType.values();
        int length = accountTypeArrValues.length;
        for (int i = 0; i < length; i++) {
            AccountType type = accountTypeArrValues[i];
            String label = type.label();
            float tw = this.frame.textWidth(font, label);
            boolean over = this.ui.hovered(x - (6.0f * s), y, tw + (12.0f * s), h);
            if (this.ui.pressed(x - (6.0f * s), y, tw + (12.0f * s), h) && this.microsoft == null && type != this.adding) {
                this.adding = type;
                this.accountError = null;
                if (type != AccountType.MICROSOFT) {
                    field(type).focus();
                }
            }
            float k = this.ui.hover(new Key("add", label), over);
            this.frame.text(dl, font, x, ty, type == this.adding ? EgyptDraw.TEXT : EgyptDraw.mix(EgyptDraw.MUTED, EgyptDraw.TEXT, k), a, label);
            if (type == this.adding) {
                dl.addRectFilled(x, (y + h) - (2.0f * s), x + tw, y + h, EgyptDraw.col(EgyptDraw.GOLD, a));
            }
            x += tw + (BAR_H * s);
        }
    }

    private void accountFilters(float x, float y, float s, float a) {
        List<Account> all = Accounts.get().list();
        if (chip("all-accounts", "All", all.size(), this.accountFilter == null, x, y, a)) {
            this.accountFilter = null;
        }
        float x2 = x + chipWidth("All", all.size()) + (18.0f * s);
        AccountType[] accountTypeArrValues = AccountType.values();
        int length = accountTypeArrValues.length;
        for (int i = 0; i < length; i++) {
            AccountType type = accountTypeArrValues[i];
            int count = (int) all.stream().filter(account -> {
                return account.type() == type;
            }).count();
            if (chip(type, type.label(), count, this.accountFilter == type, x2, y, a)) {
                this.accountFilter = type;
            }
            x2 += chipWidth(type.label(), count) + (18.0f * s);
        }
    }

    private void accountRow(Account account, boolean inUse, float x, float y, float w, float h, float s, float a) {
        ImDrawList dl = this.frame.bg();
        boolean over = this.ui.hovered(x, y, w, h);
        float k = this.ui.hover(account, over);
        if (k > 0.01f && !inUse) {
            dl.addRectFilled(x, y, x + w, y + h, EgyptDraw.col(EgyptDraw.TEXT, 0.05f * k * a));
        }
        float face = 46.0f * s;
        float fx = x + (12.0f * s);
        PlayerFaces.draw(dl, account, fx, y + ((h - face) * 0.5f), face, s, a);
        String name = account.name().isEmpty() ? account.type().label() + " account" : account.name();
        float right = removeCross(new Key(account, "remove"), (x + w) - (8.0f * s), y, h, k, s, a);
        if (right < 0.0f) {
            Accounts.get().remove(account);
            return;
        }
        if (inUse) {
            right = tag("Active", EgyptDraw.GOLD_TEXT, right, y, h, a);
        }
        entry(this.frame.menuFont(), this.frame.listFont(), fx + face + (16.0f * s), y, h, right - (12.0f * s), name, account.type().label(), a);
        if (!this.ui.pressed(x, y, w, h) || inUse) {
            return;
        }
        login(account);
    }

    private DuskWidgets.TextField field(AccountType type) {
        return type == AccountType.SESSION ? this.token : this.crackedName;
    }

    private void add(DuskWidgets.TextField field) {
        CompletableFuture<Account> completableFutureAddCracked;
        if (field == this.token) {
            completableFutureAddCracked = Accounts.get().addSession(field.text());
        } else {
            completableFutureAddCracked = Accounts.get().addCracked(field.text());
        }
        CompletableFuture<Account> added = completableFutureAddCracked;
        track(added);
        added.thenRun(() -> {
            field.setText("");
        });
    }

    private void login(Account account) {
        if (!Accounts.get().busy()) {
            track(Accounts.get().login(account));
        }
    }

    private void signInMicrosoft() {
        CompletableFuture<Account> signIn = Accounts.get().addMicrosoft();
        this.microsoft = signIn;
        track(signIn);
        signIn.whenComplete((account, failure) -> {
            this.microsoft = null;
        });
    }

    private void track(CompletableFuture<?> action) {
        this.accountError = null;
        action.whenComplete((result, failure) -> {
            this.accountError = failure == null ? null : messageOf(failure);
        });
    }

    private static String messageOf(Throwable failure) {
        Throwable cause = (!(failure instanceof CompletionException) || failure.getCause() == null) ? failure : failure.getCause();
        String message = cause.getMessage();
        return (message == null || message.isBlank()) ? "Login failed" : message;
    }

    private List<Proxy> shownProxies() {
        List<Proxy> shown = new ArrayList<>();
        for (Proxy proxy : Proxies.get().list()) {
            if (this.filter.matches(proxy) && (this.typeFilter == null || proxy.type == this.typeFilter)) {
                shown.add(proxy);
            }
        }
        if (this.sort.order != null) {
            shown.sort(this.sort.order);
        }
        return shown;
    }

    private static int pingOrder(Proxy proxy) {
        if (proxy.ping >= 0) {
            return proxy.ping;
        }
        return proxy.ping == -2 ? 2147483646 : Integer.MAX_VALUE;
    }

    private void proxies(List<Proxy> shown, float x, float y, float w, float bottom, float s, float a) {
        float pad = 24.0f * s;
        float fx = x + pad;
        float fw = w - (2.0f * pad);
        List<Proxy> all = Proxies.get().list();
        this.selected.retainAll(new HashSet(all));
        float fh = FIELD_H * s;
        float fieldW = (fw - fh) - (8.0f * s);
        if (this.ui.textField(this.proxyInput, fx, y, fieldW, fh) && !this.proxyInput.text().isBlank()) {
            int added = Proxies.get().importText(this.proxyInput.text());
            message(added == 0 ? "Not added: already saved, or not host:port" : null, added == 0);
            if (added > 0) {
                this.proxyInput.setText("");
            }
        }
        float px = (fx + fw) - fh;
        if (pasteButton(px, y, fh, a)) {
            String clipboard = class_310.method_1551().field_1774.method_1460();
            imported(Proxies.get().importText(clipboard), clipboard.isBlank() ? "The clipboard is empty" : "Nothing new on the clipboard");
        }
        if (this.ui.hoverOf("paste") > 0.5f) {
            this.frame.tooltip("Paste proxies from the clipboard", px + (fh * 0.5f), y + fh + (6.0f * s));
        }
        float y2 = y + (62.0f * s);
        ImFont font = this.frame.bodyFont();
        if (link("file", "Import file", fx, y2, !this.fileDialog, a)) {
            importFile();
        }
        if (this.ui.hoverOf("file") > 0.5f) {
            this.frame.tooltip("A text file with one proxy per line", fx + (this.frame.textWidth(font, "Import file") * 0.5f), y2 + this.frame.textHeight(font) + (8.0f * s));
        }
        float lx = fx + this.frame.textWidth(font, "Import file") + (24.0f * s);
        int pending = ProxyChecker.get().pending();
        if (pending > 0) {
            statusRight("Checking, " + pending + " left", false, lx, fx + fw, y2, a);
        } else if (this.proxyMessage != null) {
            statusRight(this.proxyMessage, this.proxyError, lx, fx + fw, y2, a);
        }
        float y3 = y2 + this.frame.textHeight(font) + (18.0f * s);
        filters(all, fx, y3, fw, s, a);
        float y4 = y3 + (BAR_H * s) + (10.0f * s);
        actions(shown, all, fx, y4, fw, s, a);
        float y5 = y4 + (BAR_H * s) + (12.0f * s);
        float listH = bottom - y5;
        if (shown.isEmpty()) {
            centered(all.isEmpty() ? "No proxies yet" : "None match the filter", x, y5, w, listH, a);
            return;
        }
        float rowX = x + (8.0f * s);
        float rowW = w - (16.0f * s);
        float rowH = 46.0f * s;
        float scroll = this.ui.beginScroll("proxies", rowX, y5, rowW, listH, shown.size() * rowH);
        int first = Math.max(0, (int) (scroll / rowH));
        int last = Math.min(shown.size(), ((int) ((scroll + listH) / rowH)) + 1);
        for (int i = first; i < last; i++) {
            proxyRow(shown.get(i), rowX, (y5 - scroll) + (i * rowH), rowW, rowH, s, a);
        }
        this.ui.endScroll();
    }

    private void filters(List<Proxy> all, float x, float y, float w, float s, float a) {
        int[] counts = new int[Filter.values().length];
        int[] types = new int[ProxyType.values().length];
        for (Proxy proxy : all) {
            for (Filter each : Filter.values()) {
                if (each.matches(proxy)) {
                    int iOrdinal = each.ordinal();
                    counts[iOrdinal] = counts[iOrdinal] + 1;
                }
            }
            int iOrdinal2 = proxy.type.ordinal();
            types[iOrdinal2] = types[iOrdinal2] + 1;
        }
        float cx = x;
        Filter[] filterArrValues = Filter.values();
        int length = filterArrValues.length;
        for (int i = 0; i < length; i++) {
            Filter each2 = filterArrValues[i];
            if (chip(each2, each2.label, counts[each2.ordinal()], this.filter == each2, cx, y, a)) {
                this.filter = each2;
            }
            cx += chipWidth(each2.label, counts[each2.ordinal()]) + (18.0f * s);
        }
        float cx2 = cx + (10.0f * s);
        this.frame.bg().addLine(cx2, y + (4.0f * s), cx2, (y + (BAR_H * s)) - (4.0f * s), EgyptDraw.col(EgyptDraw.TEXT, 0.12f * a), Math.max(1.0f, s));
        float cx3 = cx2 + (20.0f * s);
        ProxyType[] proxyTypeArrValues = ProxyType.values();
        int length2 = proxyTypeArrValues.length;
        for (int i2 = 0; i2 < length2; i2++) {
            ProxyType type = proxyTypeArrValues[i2];
            boolean on = this.typeFilter == type;
            if (chip(type, type.label(), types[type.ordinal()], on, cx3, y, a)) {
                this.typeFilter = on ? null : type;
            }
            cx3 += chipWidth(type.label(), types[type.ordinal()]) + (18.0f * s);
        }
        ImFont font = this.frame.bodyFont();
        String label = "Sort: " + this.sort.label;
        float lw = this.frame.textWidth(font, label);
        if (link("sort", label, (x + w) - lw, y + (((BAR_H * s) - this.frame.textHeight(font)) * 0.5f), true, a)) {
            this.sort = Sort.values()[(this.sort.ordinal() + 1) % Sort.values().length];
        }
    }

    private void actions(List<Proxy> shown, List<Proxy> all, float x, float y, float w, float s, float a) {
        ImFont font = this.frame.bodyFont();
        float cy = y + (BAR_H * s * 0.5f);
        float ty = cy - (this.frame.textHeight(font) * 0.5f);
        boolean allShown = !shown.isEmpty() && this.selected.containsAll(shown);
        if (checkbox("select-all", allShown, x + (12.0f * s), cy, s, a)) {
            if (allShown) {
                this.selected.removeAll(shown);
            } else {
                this.selected.addAll(shown);
            }
        }
        float lx = x + (12.0f * s) + (16.0f * s) + (18.0f * s);
        if (this.selected.isEmpty()) {
            if (link("check-all", "Check all", lx, ty, !shown.isEmpty(), a)) {
                ProxyChecker.get().check(shown);
            }
            float lx2 = lx + this.frame.textWidth(font, "Check all") + (22.0f * s);
            List<Proxy> dead = all.stream().filter(proxy -> {
                return proxy.ping == -1;
            }).toList();
            if (!dead.isEmpty()) {
                Key removeDead = new Key("bulk", "remove-dead");
                String label = armed(removeDead) ? "Remove " + dead.size() + " dead?" : "Remove dead";
                if (link(removeDead, label, lx2, ty, true, a, armed(removeDead) ? ERROR : EgyptDraw.MUTED) && confirm(removeDead)) {
                    Proxies.get().removeAll(dead);
                    message("Removed " + dead.size() + " dead", false);
                    return;
                }
                return;
            }
            return;
        }
        List<Proxy> picked = List.copyOf(this.selected);
        String count = picked.size() + " selected";
        text(count, lx, ty, EgyptDraw.TEXT, a);
        float lx3 = lx + this.frame.textWidth(font, count) + (22.0f * s);
        if (link("check-selected", "Check", lx3, ty, true, a)) {
            ProxyChecker.get().check(picked);
        }
        float lx4 = lx3 + this.frame.textWidth(font, "Check") + (22.0f * s);
        if (link("copy-selected", "Copy", lx4, ty, true, a)) {
            class_310.method_1551().field_1774.method_1455(String.join("\n", picked.stream().map(ConnectionsPanel::line).toList()));
            message("Copied " + picked.size() + (picked.size() == 1 ? " proxy" : " proxies"), false);
        }
        float lx5 = lx4 + this.frame.textWidth(font, "Copy") + (22.0f * s);
        Key removeSelected = new Key("bulk", "remove-selected");
        String remove = armed(removeSelected) ? "Remove " + picked.size() + "?" : "Remove";
        if (link(removeSelected, remove, lx5, ty, true, a, armed(removeSelected) ? ERROR : EgyptDraw.MUTED) && confirm(removeSelected)) {
            Proxies.get().removeAll(picked);
            this.selected.clear();
            message("Removed " + picked.size(), false);
        }
        if (link("clear-selection", "Clear", lx5 + this.frame.textWidth(font, remove) + (22.0f * s), ty, true, a)) {
            this.selected.clear();
        }
    }

    private void proxyRow(Proxy proxy, float x, float y, float w, float h, float s, float a) {
        ImDrawList dl = this.frame.bg();
        boolean switchable = proxy.enabled || proxy.isValid();
        boolean over = this.ui.hovered(x, y, w, h);
        float k = this.ui.hover(proxy, over);
        boolean picked = this.selected.contains(proxy);
        if (picked) {
            dl.addRectFilled(x, y, x + w, y + h, EgyptDraw.col(EgyptDraw.GOLD, 0.07f * a));
        } else if (k > 0.01f && switchable) {
            dl.addRectFilled(x, y, x + w, y + h, EgyptDraw.col(EgyptDraw.TEXT, 0.05f * k * a));
        }
        float cy = y + (h * 0.5f);
        if (checkbox(new Key(proxy, "select"), picked, x + (12.0f * s), cy, s, a)) {
            if (picked) {
                this.selected.remove(proxy);
            } else {
                this.selected.add(proxy);
            }
        }
        proxyDot(dl, x + (52.0f * s), cy, proxy.enabled, 1.3f * s, a);
        float right = removeCross(new Key(proxy, "remove"), (x + w) - (8.0f * s), y, h, k, s, a);
        if (right < 0.0f) {
            Proxies.get().remove(proxy);
            this.selected.remove(proxy);
            return;
        }
        if (proxy.enabled) {
            right = tag("In use", EgyptDraw.GOLD_TEXT, right, y, h, a);
        }
        float right2 = ping(proxy, right, y, h, a);
        String login = proxy.username.isEmpty() ? "" : " · " + proxy.username;
        String detail = proxy.name.equals(proxy.displayAddress()) ? proxy.type.label() + login : proxy.type.label() + " · " + proxy.displayAddress() + login;
        entry(this.frame.bodyFont(), this.frame.detailFont(), x + (74.0f * s), y, h, right2 - (12.0f * s), proxy.name, detail, a);
        if (this.ui.pressed(x, y, w, h) && switchable) {
            Proxies.get().setEnabled(proxy, !proxy.enabled);
            message(null, false);
        }
    }

    private float ping(Proxy proxy, float right, float y, float h, float a) {
        float column = right - (96.0f * this.frame.scale());
        if (ProxyChecker.get().isChecking(proxy)) {
            int dots = ((int) (this.frame.time() * CONFIRM_FOR)) % 4;
            tag("Checking" + ".".repeat(dots) + " ".repeat(3 - dots), EgyptDraw.MUTED, right, y, h, a);
        } else if (proxy.ping >= 0) {
            tag(proxy.ping + " ms", GOOD, right, y, h, a);
        } else if (proxy.ping == -1) {
            tag("Dead", ERROR, right, y, h, a);
        }
        return column;
    }

    private static String line(Proxy proxy) {
        String str;
        String str2;
        if (proxy.username.isEmpty()) {
            str = "";
        } else {
            str = proxy.username + (proxy.password.isEmpty() ? "" : ":" + proxy.password) + "@";
        }
        String login = str;
        if (proxy.type == ProxyType.SOCKS4) {
            str2 = "socks4://";
        } else {
            str2 = login.isEmpty() ? "" : "socks5://";
        }
        String scheme = str2;
        return scheme + login + proxy.displayAddress();
    }

    private void imported(int added, String none) {
        String str;
        if (added > 0) {
            str = "Added " + added + (added == 1 ? " proxy" : " proxies");
        } else {
            str = none;
        }
        message(str, added == 0);
    }

    private void importFile() {
        this.fileDialog = true;
        Thread thread = new Thread(() -> {
            String chosen;
            String chosen2 = null;
            try {
                chosen = TinyFileDialogs.tinyfd_openFileDialog("Import proxies", (CharSequence) null, (PointerBuffer) null, (CharSequence) null, false);
            } finally {
                chosen = chosen2;
                class_310.method_1551().execute(() -> {
                    this.fileDialog = false;
                    if (chosen != null) {
                        importFrom(Path.of(chosen, new String[0]));
                    }
                });
            }
        }, "Anubis proxy import");
        thread.setDaemon(true);
        thread.start();
    }

    private void importFrom(Path file) {
        try {
            imported(Proxies.get().importText(Files.readString(file)), "No proxies found in " + String.valueOf(file.getFileName()));
        } catch (IOException e) {
            message("Couldn't read " + String.valueOf(file.getFileName()), true);
        }
    }

    private void message(String text, boolean error) {
        this.proxyMessage = text;
        this.proxyError = error;
    }

    private static String route() {
        Proxy proxy = Proxies.get().enabled();
        return proxy == null ? "" : String.valueOf(proxy.type) + " " + proxy.displayAddress() + " " + proxy.username + " " + proxy.password;
    }

    private void entry(ImFont nameFont, ImFont detailFont, float x, float y, float h, float right, String name, String detail, float a) {
        float nh = this.frame.textHeight(nameFont);
        float gap = CONFIRM_FOR * this.frame.scale();
        boolean oneLine = detail.isEmpty();
        float ty = oneLine ? y + ((h - nh) * 0.5f) : y + ((((h - nh) - gap) - this.frame.textHeight(detailFont)) * 0.5f);
        this.frame.text(this.frame.bg(), nameFont, x, ty, EgyptDraw.TEXT, a, DuskWidgets.fit(nameFont, name, right - x));
        if (!oneLine) {
            this.frame.text(this.frame.bg(), detailFont, x, ty + nh + gap, EgyptDraw.MUTED, a, DuskWidgets.fit(detailFont, detail, right - x));
        }
    }

    private float tag(String text, int color, float right, float y, float h, float a) {
        ImFont font = this.frame.bodyFont();
        float x = right - this.frame.textWidth(font, text);
        this.frame.text(this.frame.bg(), font, x, y + ((h - this.frame.textHeight(font)) * 0.5f), color, a, text);
        return x - (14.0f * this.frame.scale());
    }

    private float removeCross(Key key, float right, float y, float h, float k, float s, float a) {
        ImDrawList dl = this.frame.bg();
        if (armed(key)) {
            ImFont font = this.frame.bodyFont();
            float tw = this.frame.textWidth(font, "Remove?");
            float x = right - tw;
            float th = this.frame.textHeight(font);
            boolean over = this.ui.hovered(x - (6.0f * s), y, tw + (12.0f * s), h);
            if (!this.ui.pressed(x - (6.0f * s), y, tw + (12.0f * s), h)) {
                this.frame.text(dl, font, x, y + ((h - th) * 0.5f), ERROR, over ? a : 0.85f * a, "Remove?");
                return x - (14.0f * s);
            }
            this.armed = null;
            return -1.0f;
        }
        float size = 28.0f * s;
        float x2 = right - size;
        float cy = y + (h * 0.5f);
        boolean over2 = k > 0.5f && this.ui.hovered(x2, cy - (size * 0.5f), size, size);
        if (over2 && this.ui.pressed(x2, cy - (size * 0.5f), size, size)) {
            confirm(key);
        }
        float kc = this.ui.hover(key, over2);
        if (k > 0.01f) {
            float cx = x2 + (size * 0.5f);
            float c = 5.0f * s;
            float line = Math.max(1.2f, 1.5f * s);
            int color = EgyptDraw.col(EgyptDraw.mix(EgyptDraw.MUTED, ERROR, kc), k * a);
            dl.addLine(cx - c, cy - c, cx + c, cy + c, color, line);
            dl.addLine(cx - c, cy + c, cx + c, cy - c, color, line);
        }
        return x2 - (6.0f * s);
    }

    private boolean checkbox(Object key, boolean checked, float x, float cy, float s, float a) {
        ImDrawList dl = this.frame.bg();
        float size = 16.0f * s;
        float y = cy - (size * 0.5f);
        float reach = 8.0f * s;
        boolean over = this.ui.hovered(x - reach, y - reach, size + (2.0f * reach), size + (2.0f * reach));
        boolean clicked = over && this.ui.pressed(x - reach, y - reach, size + (2.0f * reach), size + (2.0f * reach));
        float k = this.ui.hover(key, over);
        if (checked) {
            dl.addRectFilled(x, y, x + size, y + size, EgyptDraw.col(EgyptDraw.GOLD, a));
            dl.pathClear();
            dl.pathLineTo(x + (size * 0.22f), y + (size * 0.52f));
            dl.pathLineTo(x + (size * 0.42f), y + (size * 0.72f));
            dl.pathLineTo(x + (size * 0.78f), y + (size * 0.3f));
            dl.pathStroke(EgyptDraw.col(-16777216, a), 0, Math.max(1.4f, 2.0f * s));
        } else {
            dl.addRectFilled(x, y, x + size, y + size, EgyptDraw.col(-16777216, 0.3f * a));
            dl.addRect(x, y, x + size, y + size, EgyptDraw.col(EgyptDraw.mix(EgyptDraw.TEXT, EgyptDraw.GOLD, k), (0.3f + (0.4f * k)) * a), 0.0f, 0, Math.max(1.0f, s));
        }
        return clicked;
    }

    private float chipWidth(String label, int count) {
        return this.frame.textWidth(this.frame.bodyFont(), label) + (7.0f * this.frame.scale()) + this.frame.textWidth(this.frame.bodyFont(), Integer.toString(count));
    }

    private boolean chip(Object key, String label, int count, boolean on, float x, float y, float a) {
        ImDrawList dl = this.frame.bg();
        ImFont font = this.frame.bodyFont();
        float s = this.frame.scale();
        float w = chipWidth(label, count);
        float h = BAR_H * s;
        boolean over = this.ui.hovered(x - (4.0f * s), y, w + (8.0f * s), h);
        boolean clicked = over && this.ui.pressed(x - (4.0f * s), y, w + (8.0f * s), h);
        float k = this.ui.hover(key, over);
        float ty = y + ((h - this.frame.textHeight(font)) * 0.5f);
        this.frame.text(dl, font, x, ty, on ? EgyptDraw.GOLD_TEXT : EgyptDraw.mix(EgyptDraw.MUTED, EgyptDraw.TEXT, k), a, label);
        float lw = this.frame.textWidth(font, label) + (7.0f * s);
        this.frame.text(dl, font, x + lw, ty, EgyptDraw.MUTED, 0.8f * a, Integer.toString(count));
        return clicked;
    }

    private boolean box(Object key, String label, float x, float y, float w, float h, boolean enabled, float a) {
        ImDrawList dl = this.frame.bg();
        float s = this.frame.scale();
        float alpha = enabled ? a : 0.5f * a;
        boolean over = enabled && this.ui.hovered(x, y, w, h);
        boolean clicked = over && this.ui.pressed(x, y, w, h);
        float k = this.ui.hover(key, over);
        dl.addRectFilled(x, y, x + w, y + h, EgyptDraw.col(-16777216, 0.35f * alpha));
        if (k > 0.01f) {
            dl.addRectFilled(x, y, x + w, y + h, EgyptDraw.col(EgyptDraw.GOLD, 0.08f * k * alpha));
        }
        dl.addRect(x, y, x + w, y + h, EgyptDraw.col(EgyptDraw.mix(EgyptDraw.TEXT, EgyptDraw.GOLD, k), (0.2f + (0.5f * k)) * alpha), 0.0f, 0, Math.max(1.0f, s));
        ImFont font = this.frame.bodyFont();
        this.frame.text(dl, font, x + ((w - this.frame.textWidth(font, label)) * 0.5f), y + ((h - this.frame.textHeight(font)) * 0.5f), EgyptDraw.mix(EgyptDraw.TEXT, EgyptDraw.GOLD_LIGHT, k), alpha, label);
        return clicked;
    }

    private boolean pasteButton(float x, float y, float size, float a) {
        ImDrawList dl = this.frame.bg();
        float s = this.frame.scale();
        boolean over = this.ui.hovered(x, y, size, size);
        boolean clicked = over && this.ui.pressed(x, y, size, size);
        float k = this.ui.hover("paste", over);
        dl.addRectFilled(x, y, x + size, y + size, EgyptDraw.col(-16777216, 0.35f * a));
        if (k > 0.01f) {
            dl.addRectFilled(x, y, x + size, y + size, EgyptDraw.col(EgyptDraw.GOLD, 0.08f * k * a));
        }
        dl.addRect(x, y, x + size, y + size, EgyptDraw.col(EgyptDraw.mix(EgyptDraw.TEXT, EgyptDraw.GOLD, k), (0.12f + (0.5f * k)) * a), 0.0f, 0, Math.max(1.0f, s));
        float cx = x + (size * 0.5f);
        float cy = y + (size * 0.5f) + s;
        float line = Math.max(1.2f, 1.5f * s);
        int color = EgyptDraw.col(EgyptDraw.mix(EgyptDraw.MUTED, EgyptDraw.GOLD_LIGHT, k), a);
        dl.addRect(cx - (7.0f * s), cy - (8.0f * s), cx + (7.0f * s), cy + (9.0f * s), color, 2.0f * s, 0, line);
        dl.addRectFilled(cx - (3.5f * s), cy - (10.5f * s), cx + (3.5f * s), cy - (6.0f * s), color, s);
        dl.addLine(cx - (3.5f * s), cy - (1.0f * s), cx + (3.5f * s), cy - (1.0f * s), color, line);
        dl.addLine(cx - (3.5f * s), cy + (3.5f * s), cx + (2.0f * s), cy + (3.5f * s), color, line);
        return clicked;
    }

    private boolean link(Object key, String text, float x, float y, boolean enabled, float a) {
        return link(key, text, x, y, enabled, a, EgyptDraw.MUTED);
    }

    private boolean link(Object key, String text, float x, float y, boolean enabled, float a, int color) {
        ImFont font = this.frame.bodyFont();
        float s = this.frame.scale();
        float w = this.frame.textWidth(font, text);
        float h = this.frame.textHeight(font);
        boolean over = enabled && this.ui.hovered(x, y - (4.0f * s), w, h + (8.0f * s));
        boolean clicked = over && this.ui.pressed(x, y - (4.0f * s), w, h + (8.0f * s));
        float k = this.ui.hover(key, over);
        float alpha = enabled ? a : 0.4f * a;
        this.frame.text(this.frame.bg(), font, x, y, EgyptDraw.mix(color, EgyptDraw.TEXT, k), alpha, text);
        if (k > 0.01f) {
            this.frame.bg().addLine(x, y + h + s, x + (w * k), y + h + s, EgyptDraw.col(EgyptDraw.TEXT, 0.6f * k * alpha), Math.max(1.0f, s));
        }
        return clicked;
    }

    private void statusRight(String status, boolean error, float left, float right, float y, float a) {
        ImFont font = this.frame.bodyFont();
        String shown = DuskWidgets.fit(font, status, right - left);
        text(shown, right - this.frame.textWidth(font, shown), y, error ? ERROR : EgyptDraw.MUTED, a);
    }

    private void text(String text, float x, float y, int color, float a) {
        this.frame.text(this.frame.bg(), this.frame.bodyFont(), x, y, color, a, text);
    }

    private void centered(String text, float x, float y, float w, float h, float a) {
        ImFont font = this.frame.bodyFont();
        this.frame.text(this.frame.bg(), font, x + ((w - this.frame.textWidth(font, text)) * 0.5f), y + ((h - this.frame.textHeight(font)) * 0.5f), EgyptDraw.MUTED, a, text);
    }

    private void drawTooltip() {
        String text = this.frame.pendingTooltip();
        if (text == null) {
            return;
        }
        ImDrawList dl = this.frame.fg();
        ImFont font = this.frame.detailFont();
        float s = this.frame.scale();
        float w = this.frame.textWidth(font, text) + (18.0f * s);
        float h = this.frame.textHeight(font) + (10.0f * s);
        float x = Math.max(6.0f * s, Math.min((this.frame.width() - w) - (6.0f * s), this.frame.tooltipX() - (w * 0.5f)));
        float y = this.frame.tooltipY();
        dl.addRectFilled(x, y, x + w, y + h, EgyptDraw.col(-16053748, 0.96f));
        dl.addRect(x, y, x + w, y + h, EgyptDraw.col(EgyptDraw.TEXT, 0.14f), 0.0f, 0, Math.max(1.0f, s));
        this.frame.text(dl, font, x + (9.0f * s), y + (5.0f * s), EgyptDraw.TEXT, 1.0f, text);
    }

    private boolean armed(Object key) {
        return key.equals(this.armed) && this.frame.time() < this.armedUntil;
    }

    private boolean confirm(Object key) {
        if (armed(key)) {
            this.armed = null;
            return true;
        }
        this.armed = key;
        this.armedUntil = this.frame.time() + CONFIRM_FOR;
        return false;
    }

    static void proxyDot(ImDrawList dl, float cx, float cy, boolean on, float s, float a) {
        if (on) {
            dl.addCircleFilled(cx, cy, 7.0f * s, EgyptDraw.col(EgyptDraw.GOLD, 0.16f * a), 20);
            dl.addCircleFilled(cx, cy, 3.5f * s, EgyptDraw.col(EgyptDraw.GOLD, a), 12);
        } else {
            dl.addCircle(cx, cy, 3.5f * s, EgyptDraw.col(EgyptDraw.MUTED, 0.8f * a), 12, Math.max(1.0f, s));
        }
    }
}
