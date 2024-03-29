package mods.gregtechmod.recipe.compat;

import ic2.api.recipe.IRecipeInput;
import ic2.core.recipe.BasicMachineRecipeManager;
import mods.gregtechmod.compat.ModHandler;
import net.minecraft.item.ItemStack;

public class GtBasicMachineRecipeManager extends BasicMachineRecipeManager {

    public boolean addRecipe(IRecipeInput input, ItemStack output) {
        return ModHandler.addIC2Recipe(this, input, null, true, output);
    }
}
