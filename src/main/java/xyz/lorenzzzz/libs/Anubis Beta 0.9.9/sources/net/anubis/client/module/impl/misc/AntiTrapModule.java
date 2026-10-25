package net.anubis.client.module.impl.misc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.anubis.client.chat.ModuleChat;
import net.anubis.client.event.impl.TickEvent;
import net.anubis.client.event.impl.WorldEntityAddEvent;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleCategory;
import net.anubis.client.module.setting.BooleanSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1937;
import net.minecraft.class_310;
import net.minecraft.class_638;
import net.minecraft.class_746;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/impl/misc/AntiTrapModule.class */
@Environment(EnvType.CLIENT)
public final class AntiTrapModule extends ClientModule {
    private static final int SWEEP_INTERVAL_TICKS = 20;
    private final BooleanSetting removeExisting;
    private final BooleanSetting preventSpawn;
    private final BooleanSetting armorStands;
    private final BooleanSetting chestMinecarts;
    private final List<class_1297> pendingSpawns;

    public AntiTrapModule() {
        super("anti-trap", "Anti Trap", "Allows you to escape from armor stands and chest minecarts.", ModuleCategory.DONUT);
        this.removeExisting = (BooleanSetting) addSetting("remove-existing", new BooleanSetting("Remove Existing", true));
        this.preventSpawn = (BooleanSetting) addSetting("prevent-spawn", new BooleanSetting("Prevent Spawn", true));
        this.armorStands = (BooleanSetting) addSetting("armor-stands", new BooleanSetting("Armor Stands", true));
        this.chestMinecarts = (BooleanSetting) addSetting("chest-minecarts", new BooleanSetting("Chest Minecarts", true));
        this.pendingSpawns = new ArrayList();
        usesNotifications();
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onEnable() {
        this.pendingSpawns.clear();
        on(WorldEntityAddEvent.TYPE, this::onEntityAdded);
        on(TickEvent.TYPE, this::onTick);
        if (this.removeExisting.get()) {
            removeTrapEntities();
        }
    }

    @Override // net.anubis.client.module.ClientModule
    protected void onDisable() {
        this.pendingSpawns.clear();
    }

    private void onEntityAdded(WorldEntityAddEvent event) {
        if (this.preventSpawn.get()) {
            class_1297 entity = event.entity;
            if (isTrapEntity(entity)) {
                this.pendingSpawns.add(entity);
            }
        }
    }

    private void onTick(TickEvent event) {
        class_1297 idHolder;
        class_310 mc = class_310.method_1551();
        class_1937 class_1937Var = mc.field_1687;
        class_746 player = mc.field_1724;
        if (class_1937Var == null || player == null) {
            this.pendingSpawns.clear();
            return;
        }
        if (!this.pendingSpawns.isEmpty()) {
            for (class_1297 entity : this.pendingSpawns) {
                if (!entity.method_31481() && entity.method_73183() == class_1937Var && ((idHolder = class_1937Var.method_8469(entity.method_5628())) == null || idHolder == entity)) {
                    if (isTrapEntity(entity) && canRemove(mc, player, entity)) {
                        entity.method_31472();
                    }
                }
            }
            this.pendingSpawns.clear();
        }
        if (player.field_6012 % 20 != 0) {
            return;
        }
        Iterator<class_1297> it = collectTrapEntities(mc, class_1937Var, player).iterator();
        while (it.hasNext()) {
            it.next().method_31472();
        }
    }

    private void removeTrapEntities() {
        class_310 mc = class_310.method_1551();
        class_638 level = mc.field_1687;
        if (level == null) {
            return;
        }
        List<class_1297> traps = collectTrapEntities(mc, level, mc.field_1724);
        for (class_1297 entity : traps) {
            entity.method_31472();
        }
        if (!traps.isEmpty()) {
            ModuleChat.send(this, ModuleChat.body().good("Removed").text(" ").value(Integer.valueOf(traps.size())).text(traps.size() == 1 ? " trap entity" : " trap entities"));
        }
    }

    private List<class_1297> collectTrapEntities(class_310 mc, class_638 level, class_746 player) {
        List<class_1297> traps = new ArrayList<>();
        for (class_1297 entity : level.method_18112()) {
            if (isTrapEntity(entity) && canRemove(mc, player, entity)) {
                traps.add(entity);
            }
        }
        return traps;
    }

    private boolean isTrapEntity(class_1297 entity) {
        if (entity == null) {
            return false;
        }
        class_1299<?> type = entity.method_5864();
        if (this.armorStands.get() && type == class_1299.field_6131) {
            return true;
        }
        return this.chestMinecarts.get() && type == class_1299.field_6126;
    }

    private static boolean canRemove(class_310 mc, class_746 player, class_1297 entity) {
        if (entity == mc.method_1560()) {
            return false;
        }
        if (player == null) {
            return true;
        }
        return (entity == player || entity.method_5821(player)) ? false : true;
    }
}
