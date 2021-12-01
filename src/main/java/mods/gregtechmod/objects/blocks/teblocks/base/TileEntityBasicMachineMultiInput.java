package mods.gregtechmod.objects.blocks.teblocks.base;

import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public abstract class TileEntityBasicMachineMultiInput extends TileEntityBasicMachine<IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>, List<IRecipeIngredient>, List<ItemStack>, IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>>> {

    public TileEntityBasicMachineMultiInput(IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> recipeManager) {
        super(recipeManager);
    }

    @Override
    protected List<ItemStack> getInput() {
        return Arrays.asList(this.queueInputSlot.get(), this.inputSlot.get());
    }

    @Override
    protected void relocateStacks() {}

    @Override
    public void consumeInput(IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> recipe, boolean consumeContainers) {
        GtUtil.consumeMultiInput(recipe.getInput(), this.inputSlot, this.queueInputSlot);
    }

    @Override
    public void addOutput(List<ItemStack> output) {
        this.queueOutputSlot.add(output.get(0));
        if (output.size() > 1) this.outputSlot.add(output);

        dumpOutput();
    }
}
