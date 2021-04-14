package mods.gregtechmod.objects.blocks.tileentities.teblocks.base;

import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class TileEntityBasicMachineSingleInput<R extends IMachineRecipe<IRecipeIngredient, List<ItemStack>>> extends TileEntityBasicMachine<R, IRecipeIngredient, ItemStack, IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, R>> {

    public TileEntityBasicMachineSingleInput(String descriptionKey, IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, R> recipeManager) {
        super(descriptionKey, recipeManager);
    }

    @Override
    public void consumeInput(R recipe, boolean consumeContainers) {
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
