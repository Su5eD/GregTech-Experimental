package dev.su5ed.gtexperimental.datagen.recipe;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public interface ModRecipeProvider {
    void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer);
}
