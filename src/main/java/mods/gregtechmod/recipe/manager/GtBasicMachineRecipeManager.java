package mods.gregtechmod.recipe.manager;

import ic2.api.recipe.IRecipeInput;
import ic2.core.recipe.BasicMachineRecipeManager;
import mods.gregtechmod.util.ModHandler;
import net.minecraft.item.ItemStack;

public class GtBasicMachineRecipeManager extends BasicMachineRecipeManager {
    public boolean addRecipe(IRecipeInput input, ItemStack output) {
        input.getInputs()
                .forEach(recipe -> ModHandler.removeIC2Recipe(recipe, this));
        return super.addRecipe(input, null, true, output);
    }
}
