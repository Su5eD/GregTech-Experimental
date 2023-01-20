package dev.su5ed.gtexperimental.recipe.type;

import net.minecraft.resources.ResourceLocation;

public abstract class BaseRecipeTypeImpl<R extends BaseRecipe<?, ?, ? super R>> implements BaseRecipeType<R> {
    private final ResourceLocation name;

    public BaseRecipeTypeImpl(ResourceLocation name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name.toString();
    }
}
