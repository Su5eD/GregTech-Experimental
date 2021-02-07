package mods.gregtechmod.recipe.manager;

import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.api.recipe.Recipes;
import ic2.core.recipe.BasicMachineRecipeManager;
import mods.gregtechmod.api.recipe.IRecipePulverizer;
import mods.gregtechmod.api.util.OreDictUnificator;
import mods.gregtechmod.util.ModHandler;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RecipeManagerPulverizer extends RecipeManagerBasic<IRecipePulverizer> {
    @Override
    public boolean addRecipe(IRecipePulverizer recipe, boolean overwrite) {
        overwrite |= recipe.overwrite();
        boolean ret = super.addRecipe(recipe, overwrite);
        if (ret) {
            for (ItemStack input : recipe.getInput().getMatchingInputs()) {
                addPulverisationRecipe(input, recipe.getPrimaryOutput(), recipe.getSecondaryOutput(), recipe.getChance(), overwrite);
            }
        }
        return ret;
    }

    private static void addPulverisationRecipe(ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance, boolean overwrite) {
        if (!input.getItem().hasContainerItem(input)) {
            removeIC2Recipe(input, (BasicMachineRecipeManager) Recipes.macerator);
            Recipes.macerator.addRecipe(Recipes.inputFactory.forStack(input), null, true, primaryOutput);

            if (!OreDictUnificator.isItemInstanceOf(primaryOutput, "dustWood", false) && !OreDictUnificator.isItemInstanceOf(primaryOutput, "dustSmallWood", false)) {
                if (OreDictUnificator.isItemInstanceOf(input, "ingot", true)) ModHandler.addAEGrinderRecipe(input, primaryOutput, 5);
                if (!input.isItemEqual(new ItemStack(Blocks.OBSIDIAN))) {
                    Map<ItemStack, Float> outputs = new HashMap<>();
                    outputs.put(primaryOutput.copy(), 1.0F / input.getCount());
                    if (!secondaryOutput.isEmpty()) outputs.put(secondaryOutput.copy(), 0.01F * ((chance <= 0) ? 10 : chance) / input.getCount());
                    ModHandler.addRockCrusherRecipe(input.copy().splitStack(1), (input.getItemDamage() != OreDictionary.WILDCARD_VALUE), false, outputs);
                }
                if (secondaryOutput.isEmpty()) ModHandler.addTEPulverizerRecipe(4000, input.copy(), primaryOutput.copy(), overwrite);
                else ModHandler.addTEPulverizerRecipe(4000, input.copy(), primaryOutput.copy(), secondaryOutput.copy(), (chance <= 0) ? 10 : chance, overwrite);
            } else {
                if (secondaryOutput.isEmpty()) ModHandler.addTESawmillRecipe(800, input.copy(), primaryOutput.copy(), overwrite);
                else ModHandler.addTESawmillRecipe(800, input.copy(), primaryOutput.copy(), secondaryOutput.copy(), (chance <= 0) ? 10 : chance, overwrite);
            }
        }
    }

    private static void removeIC2Recipe(ItemStack input, BasicMachineRecipeManager manager) {
        Iterator<? extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> iterator = manager.getRecipes().iterator();
        while (iterator.hasNext()) {
            MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe = iterator.next();
            if (recipe.getInput().matches(input)) {
                iterator.remove();
                return;
            }
        }
    }
}
