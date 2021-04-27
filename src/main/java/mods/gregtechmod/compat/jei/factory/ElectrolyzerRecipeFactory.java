package mods.gregtechmod.compat.jei.factory;

import mods.gregtechmod.api.recipe.IRecipeCellular;
import mods.gregtechmod.recipe.RecipeElectrolyzer;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientItemStack;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ElectrolyzerRecipeFactory extends CentrifugeRecipeFactory {
    public static final ElectrolyzerRecipeFactory INSTANCE = new ElectrolyzerRecipeFactory();

    private ElectrolyzerRecipeFactory() {}

    @Override
    protected IRecipeCellular constructCellRecipe(List<ItemStack> fluidCells, List<ItemStack> recipeOutput, int count, int cellCount, int duration, double energyCost) {
        return RecipeElectrolyzer.create(RecipeIngredientItemStack.create(fluidCells, count), recipeOutput, cellCount, duration, energyCost);
    }

    @Override
    protected IRecipeCellular constructCanRecipe(List<ItemStack> fluidCells, List<ItemStack> recipeOutput, int count, int cellCount, int duration, double energyCost) {
        return RecipeElectrolyzer.create(RecipeIngredientItemStack.create(fluidCells, count), recipeOutput, cellCount, duration, energyCost);
    }

    @Override
    protected IRecipeCellular constructCapsuleRecipe(List<ItemStack> capsules, List<ItemStack> recipeOutput, int count, int cellCount, int duration, double energyCost) {
        return RecipeElectrolyzer.create(RecipeIngredientItemStack.create(capsules, count), recipeOutput, cellCount, duration, energyCost);
    }
}
