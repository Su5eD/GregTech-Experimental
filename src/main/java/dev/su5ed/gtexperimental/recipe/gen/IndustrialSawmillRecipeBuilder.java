package dev.su5ed.gtexperimental.recipe.gen;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.IFMORecipe;
import dev.su5ed.gtexperimental.recipe.gen.compat.TEMachineRecipeBuilder;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class IndustrialSawmillRecipeBuilder extends IFMORecipeBuilder {
    
    public IndustrialSawmillRecipeBuilder(IFMORecipe recipe) {
        super(recipe);
    }

    @Override
    public void build(Consumer<FinishedRecipe> finishedRecipeConsumer, RecipeName recipeId, boolean universal) {
        super.build(finishedRecipeConsumer, recipeId, universal);

        if (universal) {
            RecipeIngredient<ItemStack> input = this.recipe.getInput();
            new TEMachineRecipeBuilder(TEMachineRecipeBuilder.SAWMILL, input.asIngredient(), input.getCount(), this.recipe.getOutput(), 100, 1600)
                .addConditions(this.conditions)
                .build(finishedRecipeConsumer, recipeId.toForeign(TEMachineRecipeBuilder.SAWMILL));
        }
    }
}
