package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeUniversal;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.compat.ModHandler;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

public class RecipeManagerAlloySmelter extends RecipeManagerMultiInput<IRecipeUniversal<List<IRecipeIngredient>>, IRecipeIngredient> {
    @Override
    public boolean addRecipe(IRecipeUniversal<List<IRecipeIngredient>> recipe, boolean overwrite) {
        boolean ret = super.addRecipe(recipe, overwrite);
        if (ret && recipe.isUniversal()) {
            recipe.getOutput()
                    .forEach(output -> {
                        int energy = output.getCount() * 1000;
                        ItemStack sand = new ItemStack(Blocks.SAND);
                        recipe.getInput().get(0).getMatchingInputs().forEach(stack -> {
                            GameRegistry.addSmelting(stack, output, 0);
                            ModHandler.addInductionSmelterRecipe(stack, sand, output, ItemStack.EMPTY, energy, 0);
                        });
                    });
        }
        return ret;
    }
}
