package mods.gregtechmod.inventory.invslot;

import ic2.core.block.IInventorySlotHolder;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManager;
import net.minecraft.item.ItemStack;

import java.util.List;

public class GtSlotProcessableSecondary<RM extends IGtRecipeManager<List<IRecipeIngredient>, I, ?>, I> extends GtSlotProcessableItemStack<RM, I> {

    public GtSlotProcessableSecondary(IInventorySlotHolder<?> base, String name, int count, InvSide preferredSide, RM recipeManager) {
        super(base, name, Access.I, count, preferredSide, recipeManager);
    }

    @Override
    public boolean accepts(ItemStack stack) {
        return this.recipeManager.getRecipes().stream()
                .map(IMachineRecipe::getInput)
                .anyMatch(list -> list.get(1).apply(stack));
    }
}
