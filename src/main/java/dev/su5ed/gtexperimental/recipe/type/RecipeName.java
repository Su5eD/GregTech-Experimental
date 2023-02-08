package dev.su5ed.gtexperimental.recipe.type;

import net.minecraft.resources.ResourceLocation;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.Nullable;

public final class RecipeName {
    private final String namespace;

    @Nullable
    private final String modid;
    private final String type;
    @Nullable
    private final String profile;
    private final String name;

    public static RecipeName common(String namespace, String type, String name) {
        return profile(namespace, type, null, name);
    }

    // Common recipes: RECIPE_TYPE/[PROFILE]/RECIPE_ID
    public static RecipeName profile(String namespace, String type, String profile, String name) {
        return new RecipeName(namespace, null, type, profile, name);
    }

    public static RecipeName foreign(String namespace, String modid, String type, String name) {
        return foreign(namespace, modid, type, null, name);
    }

    // Foreign mod recipes: MODID/RECIPE_TYPE/[PROFILE]/RECIPE_ID
    public static RecipeName foreign(String namespace, String modid, String type, String profile, String name) {
        return new RecipeName(namespace, modid, type, profile, name);
    }

    private RecipeName(String namespace, @Nullable String modid, String type, @Nullable String profile, String name) {
        this.namespace = namespace;
        this.modid = modid;
        this.type = type;
        this.profile = profile;
        this.name = name;
    }
    
    public RecipeName withType(String type) {
        return new RecipeName(this.namespace, this.modid, type, this.profile, this.name);
    }

    // Input: pulverizer/classic/ic2/ores_iridium
    // Output: ic2/macerator/classic/ores_iridium
    public RecipeName toForeign(ResourceLocation location) {
        return toForeign(location.getNamespace(), location.getPath());
    }

    public RecipeName toForeign(String modid, String type) {
        String name = this.name;
        if (name.startsWith(modid + "/")) {
            name = name.substring(modid.length() + 1);
        }
        return new RecipeName(this.namespace, modid, type, this.profile, name);
    }

    public String getPath() {
        return StreamEx.of(this.modid, this.type, this.profile, this.name)
            .nonNull()
            .joining("/");
    }

    public ResourceLocation toLocation() {
        return new ResourceLocation(this.namespace, getPath());
    }

    @Override
    public String toString() {
        return toLocation().toString();
    }
}
