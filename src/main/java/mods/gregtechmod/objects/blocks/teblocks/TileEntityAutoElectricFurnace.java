package mods.gregtechmod.objects.blocks.teblocks;

import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityElectricFurnaceBase;
import mods.gregtechmod.recipe.compat.ModRecipes;
import net.minecraft.item.ItemStack;

import java.util.List;

public class TileEntityAutoElectricFurnace extends TileEntityElectricFurnaceBase<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> {

    public TileEntityAutoElectricFurnace() {
        super(ModRecipes.FURNACE);
    }

    @Override
    protected ItemStack getInput() {
        return this.inputSlot.get();
    }

    @Override
    public void consumeInput(IMachineRecipe<IRecipeIngredient, List<ItemStack>> recipe, boolean consumeContainers) {
        this.inputSlot.consume(recipe == null ? 1 : recipe.getInput().getCount(), consumeContainers);
    }
}
