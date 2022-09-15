package mods.gregtechmod.api.recipe;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.util.Either;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IRecipeFusion extends IMachineRecipe<List<IRecipeIngredientFluid>, Either<ItemStack, FluidStack>> {
    double getStartEnergy();
}
