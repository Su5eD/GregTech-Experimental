package dev.su5ed.gtexperimental.api.recipe.manager;

import dev.su5ed.gtexperimental.api.recipe.IMachineRecipe;
import dev.su5ed.gtexperimental.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IGtRecipeManagerSecondaryFluid<R extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> extends IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, R> {
    
    boolean hasRecipeForPrimaryInput(ItemStack input);
    
    @Override
    default R getRecipeFor(List<ItemStack> input) {
        return getRecipeFor(input, null);
    }

    R getRecipeFor(List<ItemStack> input, @Nullable FluidStack fluid);
}
