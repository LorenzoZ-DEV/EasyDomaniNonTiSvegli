package net.anubis.client.module;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/module/ModuleProvider.class */
@Environment(EnvType.CLIENT)
public interface ModuleProvider {
    String id();

    List<ClientModule> createModules();
}
