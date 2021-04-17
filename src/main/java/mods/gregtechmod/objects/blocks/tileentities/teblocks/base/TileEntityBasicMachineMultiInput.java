package mods.gregtechmod.objects.blocks.tileentities.teblocks.base;

import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public abstract class TileEntityBasicMachineMultiInput extends TileEntityBasicMachine<IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>, List<IRecipeIngredient>, List<ItemStack>, IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>>> {

    public TileEntityBasicMachineMultiInput(String descriptionKey, IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>> recipeManager) {
        super(descriptionKey, recipeManager);
    }

    @Override
    protected List<ItemStack> getInput() {
        return Arrays.asList(this.queueInputSlot.get(), this.inputSlot.get());
    }

    @Override
    protected void relocateStacks() {}

    @Override
    public void consumeInput(IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> recipe, boolean consumeContainers) {
        consumeMultiInput(this, recipe, consumeContainers);
    }

    @Override
    public void addOutput(List<ItemStack> output) {
        this.queueOutputSlot.add(output.get(0));
        if (output.size() > 1) this.outputSlot.add(output);

        dumpOutput();
    }

    public static void consumeMultiInput(TileEntityBasicMachine<? extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>, List<IRecipeIngredient>, List<ItemStack>, ? extends IGtRecipeManagerBasic<List<IRecipeIngredient>, List<ItemStack>, ? extends IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>>>> machine, IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> recipe, boolean consumeContainers) {
        List<IRecipeIngredient> ingredients = recipe.getInput();
        IRecipeIngredient firstIngredient = ingredients.get(0);
        int firstCount = firstIngredient.getCount();
        ItemStack firstInput = machine.queueInputSlot.get();

        IRecipeIngredient secondIngredient = ingredients.get(1);
        int secondCount = secondIngredient.getCount();
        ItemStack secondInput = machine.inputSlot.get();

        if (firstIngredient.apply(firstInput)) machine.queueInputSlot.consume(firstCount, consumeContainers);
        else if (secondIngredient.apply(firstInput)) machine.queueInputSlot.consume(secondCount, consumeContainers);

        if (firstIngredient.apply(secondInput)) machine.inputSlot.consume(firstCount, consumeContainers);
        else if (secondIngredient.apply(secondIngredient)) machine.inputSlot.consume(secondCount, consumeContainers);
    }
}
