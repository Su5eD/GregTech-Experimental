package mods.gregtechmod.api.recipe.manager;

import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;

public interface IGtRecipeManagerSecondaryFluid<R extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> extends IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, R> {
    
    boolean hasRecipeForPrimaryInput(ItemStack input);
    
    @Override
    default R getRecipeFor(List<ItemStack> input) {
        return getRecipeFor(input, null);
    }

    R getRecipeFor(List<ItemStack> input, @Nullable FluidStack fluid);
}
