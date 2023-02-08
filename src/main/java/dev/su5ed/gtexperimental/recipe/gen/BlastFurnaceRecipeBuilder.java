package dev.su5ed.gtexperimental.recipe.gen;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.gen.compat.RCBlastFurnaceRecipeBuilder;
import dev.su5ed.gtexperimental.recipe.type.MIMORecipe;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

public class BlastFurnaceRecipeBuilder extends MIMORecipeBuilder {

    public BlastFurnaceRecipeBuilder(MIMORecipe recipe) {
        super(recipe);
    }

    @Override
    public void build(Consumer<FinishedRecipe> finishedRecipeConsumer, RecipeName recipeId, boolean universal) {
        super.build(finishedRecipeConsumer, recipeId, universal);
        
        if (universal) {
            RecipeIngredient<ItemStack> input = this.recipe.getInputs().get(0);
            Ingredient ingredient = input.asIngredient();
            new RCBlastFurnaceRecipeBuilder(ingredient, this.recipe.getOutput().get(0), this.recipe.getDuration(), 0, 0)
                .addConditions(this.conditions)
                .build(finishedRecipeConsumer, recipeId.toForeign(RCBlastFurnaceRecipeBuilder.TYPE));
        }
    }
}
