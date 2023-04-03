package dev.su5ed.gtexperimental.compat;

import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeProvider;
import dev.su5ed.gtexperimental.recipe.SIMORecipe;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import dev.su5ed.gtexperimental.recipe.type.VanillaRecipeIngredient;
import dev.su5ed.gtexperimental.util.GtUtil;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.common.recipe.MekanismRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public final class MekanismHandler {

    public static RecipeProvider<SIMORecipe<ItemStack, List<ItemStack>>, ItemStack> crusherRecipeTranslator() {
        return new TranslatingRecipeProvider<>("crusher", (level, stack) -> MekanismRecipeType.CRUSHING.getInputCache().containsInput(level, stack),
            (level, stack) -> MekanismRecipeType.CRUSHING.getInputCache().findFirstRecipe(level, stack),
            (input, recipe) -> GtUtil.itemName(input) + "_to_" + GtUtil.itemName(recipe.getOutput(input)),
            (ResourceLocation id, ItemStack input, ItemStackToItemStackRecipe recipe) -> {
                ItemStackIngredient recipeInput = recipe.getInput();
                ItemStack output = recipe.getOutput(input);
                RecipeIngredient<ItemStack> ingredient = new VanillaRecipeIngredient(Ingredient.of(recipeInput.getRepresentations().stream()), (int) recipeInput.getNeededAmount(input));
                return SIMORecipe.pulverizer(id, ingredient, List.of(output), RecipePropertyMap.builder().energyCost(2).chance(0).build());
            }
        );
    }

    private MekanismHandler() {}
}
