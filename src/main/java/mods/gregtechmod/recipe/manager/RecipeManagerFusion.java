package mods.gregtechmod.recipe.manager;

import mods.gregtechmod.api.recipe.IRecipeFusion;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerFusion;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import one.util.streamex.StreamEx;

import java.util.List;

public class RecipeManagerFusion extends RecipeManager<List<IRecipeIngredientFluid>, List<FluidStack>, IRecipeFusion> implements IGtRecipeManagerFusion {
    @Override
    public IRecipeFusion getRecipeFor(List<FluidStack> input) {
        return this.recipes.stream()
            .filter(recipe -> recipe.getInput().stream()
                .allMatch(ingredient -> input.stream()
                    .anyMatch(ingredient::apply)))
            .min(RecipeUtil::compareCountMulti)
            .orElse(null);
    }

    @Override
    public boolean hasRecipeFor(Fluid input) {
        return this.recipes.stream()
            .anyMatch(recipe -> recipe.getInput().stream()
                .anyMatch(ingredient -> ingredient.apply(input)));
    }

    @Override
    public boolean hasRecipeFor(List<FluidStack> input) {
        return this.recipes.stream()
            .anyMatch(recipe -> recipe.getInput().stream()
                .allMatch(ingredient -> input.stream()
                    .anyMatch(ingredient::apply)));
    }

    @Override
    public boolean hasRecipeFor(ItemStack input) {
        return false;
    }

    @Override
    protected IRecipeFusion getRecipeForExact(IRecipeFusion recipe) {
        return StreamEx.of(this.recipes)
            .findFirst(r -> r.getInput().stream()
                .allMatch(ingredient -> recipe.getInput().stream()
                    .anyMatch(ingredient::apply)) && RecipeUtil.compareCountMulti(r, recipe) == 0)
            .orElse(null);
    }
}
