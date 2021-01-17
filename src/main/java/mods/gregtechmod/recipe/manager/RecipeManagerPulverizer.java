package mods.gregtechmod.recipe.manager;

import ic2.api.recipe.Recipes;
import ic2.core.recipe.BasicMachineRecipeManager;
import mods.gregtechmod.api.recipe.IRecipePulverizer;
import mods.gregtechmod.api.util.OreDictUnificator;
import mods.gregtechmod.util.ModHandler;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

public class RecipeManagerPulverizer extends RecipeManagerBasic<IRecipePulverizer> {
    @Override
    public void addRecipe(IRecipePulverizer recipe, boolean overwrite) {
        overwrite |= recipe.overwrite();
        super.addRecipe(recipe, overwrite);
        for (ItemStack input : recipe.getInput().getMatchingInputs()) {
            addPulverisationRecipe(input, recipe.getPrimaryOutput(), recipe.getSecondaryOutput(), recipe.getChance(), overwrite);
        }
    }

    private static void addPulverisationRecipe(ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance, boolean overwrite) {
        ModHandler.removeSimpleIC2MachineRecipe(input, (BasicMachineRecipeManager) Recipes.macerator);

        if (!input.getItem().hasContainerItem(input)) {
            Recipes.macerator.addRecipe(Recipes.inputFactory.forStack(input), null, true, primaryOutput);

            if (!OreDictUnificator.isItemInstanceOf(primaryOutput, "dustWood", false) && !OreDictUnificator.isItemInstanceOf(primaryOutput, "dustSmallWood", false)) {
                if (OreDictUnificator.isItemInstanceOf(input, "ingot", true)) ModHandler.addAEGrinderRecipe(input, primaryOutput, 5);
                if (!input.isItemEqual(new ItemStack(Blocks.OBSIDIAN))) {
                    Map<ItemStack, Float> outputs = new HashMap<>();
                    outputs.put(primaryOutput.copy(), 1.0F / input.getCount());
                    if (!secondaryOutput.isEmpty()) outputs.put(secondaryOutput.copy(), 0.01F * ((chance <= 0) ? 10 : chance) / input.getCount());
                    ModHandler.addRockCrusherRecipe(input.copy().splitStack(1), (input.getItemDamage() != OreDictionary.WILDCARD_VALUE), false, outputs);
                }
                if (secondaryOutput.isEmpty()) ModHandler.addTEPulverizerRecipe(400, input.copy(), primaryOutput.copy(), overwrite);
                else ModHandler.addTEPulverizerRecipe(400, input.copy(), primaryOutput.copy(), secondaryOutput.copy(), (chance <= 0) ? 10 : chance, overwrite);
            } else {
                if (secondaryOutput.isEmpty()) ModHandler.addTESawmillRecipe(80, input.copy(), primaryOutput.copy(), overwrite);
                else ModHandler.addTESawmillRecipe(80, input.copy(), primaryOutput.copy(), secondaryOutput.copy(), (chance <= 0) ? 10 : chance, overwrite);
            }
        }
    }
}
