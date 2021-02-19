package mods.gregtechmod.recipe.manager;

import ic2.api.recipe.Recipes;
import ic2.core.recipe.BasicMachineRecipeManager;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.recipe.IRecipePulverizer;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.util.OreDictUnificator;
import mods.gregtechmod.util.ModHandler;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class RecipeManagerPulverizer extends RecipeManagerBasic<IRecipePulverizer> {

    @Override
    public boolean addRecipe(IRecipePulverizer recipe, boolean overwrite) {
        overwrite |= recipe.shouldOverwrite();
        boolean ret = super.addRecipe(recipe, overwrite);
        if (ret) {
            IRecipeIngredient input = recipe.getInput();
            int count = input.getCount();
            for (ItemStack stack : input.getMatchingInputs()) {
                addPulverisationRecipe(StackUtil.copyWithSize(stack, count), recipe.getPrimaryOutput(), recipe.getSecondaryOutput(), recipe.getChance(), overwrite);
            }
        }
        return ret;
    }

    private static void addPulverisationRecipe(ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance, boolean overwrite) {
        if (!input.getItem().hasContainerItem(input)) {
            ModHandler.removeIC2Recipe(input, (BasicMachineRecipeManager) Recipes.macerator);
            Recipes.macerator.addRecipe(Recipes.inputFactory.forStack(input), null, true, primaryOutput);

            if (!OreDictUnificator.isItemInstanceOf(primaryOutput, "dustWood", false) && !OreDictUnificator.isItemInstanceOf(primaryOutput, "dustSmallWood", false)) {
                if (OreDictUnificator.isItemInstanceOf(input, "ingot", true)) ModHandler.addAEGrinderRecipe(input, primaryOutput, 5);
                if (!input.isItemEqual(new ItemStack(Blocks.OBSIDIAN))) {
                    Map<ItemStack, Float> outputs = new HashMap<>();
                    outputs.put(primaryOutput.copy(), 1.0F / input.getCount());
                    if (!secondaryOutput.isEmpty()) outputs.put(secondaryOutput.copy(), 0.01F * ((chance <= 0) ? 10 : chance) / input.getCount());
                    ModHandler.addRockCrusherRecipe(input.copy().splitStack(1), outputs);
                }
                if (secondaryOutput.isEmpty()) ModHandler.addTEPulverizerRecipe(4000, input.copy(), primaryOutput.copy(), overwrite);
                else ModHandler.addTEPulverizerRecipe(4000, input.copy(), primaryOutput.copy(), secondaryOutput.copy(), (chance <= 0) ? 10 : chance, overwrite);
            } else {
                if (secondaryOutput.isEmpty()) ModHandler.addTESawmillRecipe(800, input.copy(), primaryOutput.copy(), overwrite);
                else ModHandler.addTESawmillRecipe(800, input.copy(), primaryOutput.copy(), secondaryOutput.copy(), (chance <= 0) ? 10 : chance, overwrite);
            }
        }
    }
}
