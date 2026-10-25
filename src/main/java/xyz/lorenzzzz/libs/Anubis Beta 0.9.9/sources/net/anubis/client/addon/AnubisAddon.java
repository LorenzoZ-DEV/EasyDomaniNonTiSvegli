package net.anubis.client.addon;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;

/* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/addon/AnubisAddon.class */
@Environment(EnvType.CLIENT)
public abstract class AnubisAddon {
    private String name = getClass().getSimpleName();
    private String version = "";
    private List<String> authors = List.of();
    String packageName;

    public abstract void onInitialize();

    public abstract String getPackage();

    public void onRegisterCategories() {
    }

    public void onConfigLoaded() {
    }

    public String getWebsite() {
        return null;
    }

    public GithubRepo getRepo() {
        return null;
    }

    public final String name() {
        return this.name;
    }

    public final String version() {
        return this.version;
    }

    public final List<String> authors() {
        return this.authors;
    }

    final void bind(ModMetadata metadata) {
        this.name = metadata.getName();
        this.version = metadata.getVersion().getFriendlyString();
        List<String> names = new ArrayList<>(metadata.getAuthors().size());
        for (Person author : metadata.getAuthors()) {
            names.add(author.getName());
        }
        this.authors = List.copyOf(names);
    }

    /* JADX INFO: loaded from: Anubis Beta 0.9.9.jar:net/anubis/client/addon/AnubisAddon$GithubRepo.class */
    @Environment(EnvType.CLIENT)
    public static final class GithubRepo extends Record {
        private final String owner;
        private final String name;

        public GithubRepo(String owner, String name) {
            this.owner = owner;
            this.name = name;
        }

        @Override // java.lang.Record
        public final String toString() {
            return (String) ObjectMethods.bootstrap(MethodHandles.lookup(), "toString", MethodType.methodType(String.class, GithubRepo.class), GithubRepo.class, "owner;name", "FIELD:Lnet/anubis/client/addon/AnubisAddon$GithubRepo;->owner:Ljava/lang/String;", "FIELD:Lnet/anubis/client/addon/AnubisAddon$GithubRepo;->name:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) ObjectMethods.bootstrap(MethodHandles.lookup(), "hashCode", MethodType.methodType(Integer.TYPE, GithubRepo.class), GithubRepo.class, "owner;name", "FIELD:Lnet/anubis/client/addon/AnubisAddon$GithubRepo;->owner:Ljava/lang/String;", "FIELD:Lnet/anubis/client/addon/AnubisAddon$GithubRepo;->name:Ljava/lang/String;").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final boolean equals(Object o) {
            return (boolean) ObjectMethods.bootstrap(MethodHandles.lookup(), "equals", MethodType.methodType(Boolean.TYPE, GithubRepo.class, Object.class), GithubRepo.class, "owner;name", "FIELD:Lnet/anubis/client/addon/AnubisAddon$GithubRepo;->owner:Ljava/lang/String;", "FIELD:Lnet/anubis/client/addon/AnubisAddon$GithubRepo;->name:Ljava/lang/String;").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public String owner() {
            return this.owner;
        }

        public String name() {
            return this.name;
        }

        public String url() {
            return "https://github.com/" + this.owner + "/" + this.name;
        }
    }
}
