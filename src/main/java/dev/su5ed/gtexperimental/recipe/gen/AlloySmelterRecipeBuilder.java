package dev.su5ed.gtexperimental.recipe.gen;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.datagen.RecipeGen;
import dev.su5ed.gtexperimental.recipe.gen.compat.TEInductionSmelterRecipeBuilder;
import dev.su5ed.gtexperimental.recipe.type.MISORecipe;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.function.Consumer;

public class AlloySmelterRecipeBuilder extends MISORecipeBuilder<ItemStack, ItemStack> {

    public AlloySmelterRecipeBuilder(MISORecipe<ItemStack, ItemStack> recipe) {
        super(recipe);
    }

    @Override
    public void build(Consumer<FinishedRecipe> finishedRecipeConsumer, RecipeName recipeId, boolean universal) {
        super.build(finishedRecipeConsumer, recipeId, universal);

        if (universal) {
            RecipeIngredient<ItemStack> input = this.recipe.getInputs().get(0);
            Ingredient ingredient = input.asIngredient();
            ItemStack output = this.recipe.getOutput();
            new SmeltingRecipeBuilder(
                ingredient,
                output,
                0,
                200
            )
                .unlockedBy("has_ingredient", RecipeGen.hasIngredient(ingredient))
                .addConditions(this.conditions)
                .build(finishedRecipeConsumer, recipeId.withType("smelting"));

            int energy = output.getCount() * 1000;
            new TEInductionSmelterRecipeBuilder(ingredient, List.of(new TEInductionSmelterRecipeBuilder.Result(output)), energy)
                .addConditions(this.conditions)
                .build(finishedRecipeConsumer, recipeId.toForeign(TEInductionSmelterRecipeBuilder.TYPE));
        }
    }
}
