package dev.su5ed.gtexperimental.api.recipe.manager;

import dev.su5ed.gtexperimental.api.recipe.IRecipeCellular;
import dev.su5ed.gtexperimental.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public interface IGtRecipeManagerCellular extends IGtRecipeManagerFluid<IRecipeIngredient, ItemStack, IRecipeCellular> {
    @Override
    default IRecipeCellular getRecipeFor(@Nullable FluidStack input) {
        return getRecipeFor(input, -1);
    }

    IRecipeCellular getRecipeFor(@Nullable FluidStack input, int cells);

    IRecipeCellular getRecipeFor(ItemStack input, ItemStack cell);
}
