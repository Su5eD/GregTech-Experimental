package mods.gregtechmod.recipe;

import mods.gregtechmod.api.recipe.IRecipeIngredientFactory;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientFluid;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientItemStack;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientOre;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import java.util.List;

public class RecipeIngredientFactory implements IRecipeIngredientFactory {
    @Override
    public IRecipeIngredient fromStacks(List<ItemStack> stacks, int count) {
        return RecipeIngredientItemStack.create(stacks, count);
    }

    @Override
    public IRecipeIngredient fromOre(String ore, int count) {
        return RecipeIngredientOre.create(ore, count);
    }

    @Override
    public IRecipeIngredientFluid fromFluidName(String fluid, int buckets) {
        return RecipeIngredientFluid.fromName(fluid, buckets);
    }

    @Override
    public IRecipeIngredientFluid fromFluidNames(List<String> fluids, int buckets) {
        return RecipeIngredientFluid.fromNames(fluids, buckets);
    }

    @Override
    public IRecipeIngredientFluid fromFluid(Fluid fluid, int buckets) {
        return RecipeIngredientFluid.fromFluid(fluid, buckets);
    }

    @Override
    public IRecipeIngredientFluid fromFluids(List<Fluid> fluids, int buckets) {
        return RecipeIngredientFluid.fromFluids(fluids, buckets);
    }
}
