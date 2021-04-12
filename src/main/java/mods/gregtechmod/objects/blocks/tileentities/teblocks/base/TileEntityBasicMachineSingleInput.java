package mods.gregtechmod.objects.blocks.tileentities.teblocks.base;

import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class TileEntityBasicMachineSingleInput extends TileEntityBasicMachine<IMachineRecipe<IRecipeIngredient, List<ItemStack>>, IRecipeIngredient, ItemStack, IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>>> {

    public TileEntityBasicMachineSingleInput(String descriptionKey, IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> recipeManager) {
        super(descriptionKey, recipeManager);
    }

    @Override
    public void consumeInput(IMachineRecipe<IRecipeIngredient, List<ItemStack>> recipe, boolean consumeContainers) {
        this.inputSlot.consume(recipe == null ? 1 : recipe.getInput().getCount(), consumeContainers);
    }

    @Override
    protected void relocateStacks() {
        moveStack(this.queueInputSlot, this.inputSlot);
        moveStack(this.queueOutputSlot, this.outputSlot);
    }

    @Override
    protected ItemStack getInput() {
        return this.inputSlot.get();
    }
}
