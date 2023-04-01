package dev.su5ed.gtexperimental.recipe.gen;

import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public abstract class ModRecipeBuilder<R extends BaseRecipe<?, ?, ?, ?, ? super R>> extends BaseRecipeBuilder {
    protected final R recipe;

    public ModRecipeBuilder(R recipe) {
        this.recipe = recipe;
    }

    @Override
    public RecipeSerializer<?> getType() {
        return this.recipe.getSerializer();
    }
}
