package dev.su5ed.gtexperimental.recipe.gen;

import dev.su5ed.gtexperimental.datagen.RecipeGen;
import dev.su5ed.gtexperimental.recipe.type.MISORecipe;
import dev.su5ed.gtexperimental.recipe.type.RecipeIngredient;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.function.Consumer;

public class AlloySmelterRecipeBuilder extends MISORecipeBuilder {

    public AlloySmelterRecipeBuilder(MISORecipe recipe) {
        super(recipe);
    }

    @Override
    public void build(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation recipeId, boolean universal) {
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
                .build(finishedRecipeConsumer, nestedId(recipeId, "smelting"));

            int energy = output.getCount() * 1000;
            new InductionSmelterRecipeBuilder(ingredient, List.of(output), energy)
                .addConditions(this.conditions)
                .build(finishedRecipeConsumer, nestedId(recipeId, "induction_smelter"));
        }
    }

    private ResourceLocation nestedId(ResourceLocation location, String folder) {
        String path = location.getPath();
        int idx = path.lastIndexOf('/') + 1;
        String pathFolder = path.substring(0, idx);
        String name = path.substring(idx);
        return new ResourceLocation(location.getNamespace(), pathFolder + folder + "/" + name);
    }
}
