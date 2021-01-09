package mods.gregtechmod.api.recipe;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import java.util.Collections;
import java.util.List;

public interface IRecipeIngredientFactory {

    default IRecipeIngredient fromStack(ItemStack stack) {
        return fromStacks(Collections.singletonList(stack), stack.getCount());
    }

    IRecipeIngredient fromStacks(List<ItemStack> stacks, int count);

    default IRecipeIngredient fromOre(String ore) {
        return fromOre(ore, 1);
    }

    IRecipeIngredient fromOre(String ore, int count);

    IRecipeIngredientFluid fromFluidName(String fluid, int buckets);

    IRecipeIngredientFluid fromFluidNames(List<String> fluids, int buckets);

    IRecipeIngredientFluid fromFluid(Fluid fluid, int buckets);

    IRecipeIngredientFluid fromFluids(List<Fluid> fluids, int buckets);
}
