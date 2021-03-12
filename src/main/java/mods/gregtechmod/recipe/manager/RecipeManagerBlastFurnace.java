package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeBlastFurnace;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.compat.ModHandler;
import mods.railcraft.api.crafting.Crafters;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.common.Optional;

public class RecipeManagerBlastFurnace extends RecipeManagerMultiInput<IRecipeBlastFurnace, IRecipeIngredient> {

    @Override
    public boolean addRecipe(IRecipeBlastFurnace recipe, boolean overwrite) {
        boolean ret = super.addRecipe(recipe, overwrite);
        if (ret && recipe.isUniversal() && ModHandler.railcraft) addRCBlastFurnaceRecipe(recipe.getInput().get(0).asIngredient(), recipe.getOutput().get(0), recipe.getDuration());
        return ret;
    }

    @Optional.Method(modid = "railcraft")
    public static void addRCBlastFurnaceRecipe(Ingredient input, ItemStack output, int duration) {
        Crafters.blastFurnace()
                .newRecipe(input)
                .output(output)
                .time(duration)
                .name(output.getItem().getRegistryName())
                .register();
    }
}
