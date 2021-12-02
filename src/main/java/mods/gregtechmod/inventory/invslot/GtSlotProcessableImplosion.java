package mods.gregtechmod.inventory.invslot;

import ic2.core.block.IInventorySlotHolder;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.item.ItemStack;

import java.util.List;

public class GtSlotProcessableImplosion extends GtSlotProcessableItemStack<IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>>, List<ItemStack>>{
    
    public GtSlotProcessableImplosion(IInventorySlotHolder<?> base, String name, int count) {
        super(base, name, Access.I, count, GtUtil.INV_SIDE_NS, GtRecipes.implosion);
    }

    @Override
    public boolean accepts(ItemStack stack) {
        return stack.isItemEqual(ModHandler.itnt);
    }
}
