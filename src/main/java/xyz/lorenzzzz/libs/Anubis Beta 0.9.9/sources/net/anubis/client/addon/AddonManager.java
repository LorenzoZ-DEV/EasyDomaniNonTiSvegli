package net.anubis.client.addon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.anubis.client.config.ModuleConfigStore;
import net.anubis.client.module.ClientModule;
import net.anubis.client.module.ModuleRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/addon/AddonManager.class */
@Environment(EnvType.CLIENT)
public final class AddonManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("Anubis");
    public static final String ENTRYPOINT = "anubis";
    private static final String CLIENT_PACKAGE = "net.anubis.client";
    private static List<AnubisAddon> addons;
    private static boolean configLoaded;

    private AddonManager() {
    }

    public static List<AnubisAddon> addons() {
        List<AnubisAddon> loaded = addons;
        return loaded != null ? loaded : List.of();
    }

    public static void init() {
        if (addons != null) {
            return;
        }
        List<AnubisAddon> loaded = create();
        loaded.removeIf(addon -> {
            return !run(addon, "register its categories", (v0) -> {
                v0.onRegisterCategories();
            });
        });
        loaded.removeIf(addon2 -> {
            if (run(addon2, "initialise", (v0) -> {
                v0.onInitialize();
            })) {
                return false;
            }
            removeModules(addon2);
            return true;
        });
        for (AnubisAddon addon3 : loaded) {
            Logger logger = LOGGER;
            Object[] objArr = new Object[3];
            objArr[0] = addon3.name();
            objArr[1] = addon3.version();
            objArr[2] = addon3.authors().isEmpty() ? "unknown" : String.join(", ", addon3.authors());
            logger.info("[Anubis] Loaded addon {} v{} by {}", objArr);
        }
        addons = List.copyOf(loaded);
    }

    public static void configLoaded() {
        if (addons == null || configLoaded) {
            return;
        }
        configLoaded = true;
        List<AnubisAddon> loaded = new ArrayList<>(addons);
        loaded.removeIf(addon -> {
            if (run(addon, "start", (v0) -> {
                v0.onConfigLoaded();
            })) {
                return false;
            }
            removeModules(addon);
            return true;
        });
        addons = List.copyOf(loaded);
    }

    private static List<AnubisAddon> create() {
        List<AnubisAddon> created = new ArrayList<>();
        try {
            List<EntrypointContainer<AnubisAddon>> containers = FabricLoader.getInstance().getEntrypointContainers(ENTRYPOINT, AnubisAddon.class);
            for (EntrypointContainer<AnubisAddon> container : containers) {
                ModMetadata metadata = container.getProvider().getMetadata();
                try {
                    AnubisAddon addon = (AnubisAddon) container.getEntrypoint();
                    String pkg = addon.getPackage();
                    addon.bind(metadata);
                    String problem = packageProblem(addon, pkg, created);
                    if (problem != null) {
                        LOGGER.error("[Anubis] Addon {} is skipped: {}", addon.name(), problem);
                    } else {
                        addon.packageName = pkg;
                        created.add(addon);
                    }
                } catch (LinkageError | RuntimeException e) {
                    LOGGER.error("[Anubis] Addon {} ({}) could not be created and is skipped", new Object[]{metadata.getName(), metadata.getId(), e});
                }
            }
            return created;
        } catch (LinkageError | RuntimeException e2) {
            LOGGER.error("[Anubis] Could not list addons; starting without them", e2);
            return created;
        }
    }

    private static String packageProblem(AnubisAddon addon, String pkg, List<AnubisAddon> created) {
        if (pkg == null || !addon.getClass().getName().startsWith(pkg + ".")) {
            return "getPackage() is \"" + pkg + "\", which doesn't hold its class " + addon.getClass().getName();
        }
        if (overlap(pkg, CLIENT_PACKAGE)) {
            return "its package " + pkg + " overlaps Anubis' own (net.anubis.client)";
        }
        for (AnubisAddon other : created) {
            if (overlap(pkg, other.packageName)) {
                return "its package " + pkg + " overlaps " + other.name() + "'s (" + other.packageName + "); an addon made from the template needs its package renamed";
            }
        }
        return null;
    }

    private static boolean overlap(String a, String b) {
        return (a + ".").startsWith(b + ".") || (b + ".").startsWith(a + ".");
    }

    private static boolean run(AnubisAddon addon, String step, Consumer<AnubisAddon> action) {
        try {
            action.accept(addon);
            return true;
        } catch (LinkageError | RuntimeException e) {
            LOGGER.error("[Anubis] Addon {} failed to {} and is skipped{}", new Object[]{addon.name(), step, reportHint(addon), e});
            return false;
        }
    }

    private static String reportHint(AnubisAddon addon) {
        try {
            AnubisAddon.GithubRepo repo = addon.getRepo();
            String where = repo != null ? repo.url() + "/issues" : addon.getWebsite();
            return where != null ? " - report it at " + where : "";
        } catch (LinkageError | RuntimeException e) {
            return "";
        }
    }

    private static void removeModules(AnubisAddon addon) {
        String prefix = addon.packageName + ".";
        for (ClientModule module : ModuleRegistry.all()) {
            if (module.getClass().getName().startsWith(prefix)) {
                ModuleConfigStore.getInstance().release(module);
                if (module.isEnabled()) {
                    module.setEnabled(false);
                }
                ModuleRegistry.remove(module);
            }
        }
    }
}
