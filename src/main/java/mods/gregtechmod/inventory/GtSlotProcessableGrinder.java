package mods.gregtechmod.inventory;

import ic2.core.block.IInventorySlotHolder;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import net.minecraft.item.ItemStack;

import java.util.List;

public class GtSlotProcessableGrinder extends GtSlotProcessableItemStack<IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>>, List<ItemStack>> {
    
    public GtSlotProcessableGrinder(IInventorySlotHolder<?> base, String name, int count) {
        super(base, name, count, GtRecipes.industrialGrinder);
    }

    @Override
    public boolean accepts(ItemStack stack) {
        return this.recipeManager.getRecipes().stream()
                .map(IMachineRecipe::getInput)
                .anyMatch(list -> list.get(1).apply(stack));
    }
}
