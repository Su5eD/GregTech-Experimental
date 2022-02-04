package mods.gregtechmod.compat.jei.factory;

import mods.gregtechmod.api.recipe.CellType;
import mods.gregtechmod.api.recipe.IRecipeCellular;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerCellular;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.compat.jei.wrapper.WrapperCellular;
import mods.gregtechmod.recipe.RecipeCellular;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class CellularRecipeFactory {
    private static final double WAX_MELTING_POINT = 310.15;

    protected CellularRecipeFactory() {}

    public List<? extends WrapperCellular> getCellularRecipes(IGtRecipeManagerCellular recipeManager, boolean showEnergyCost) {
        return StreamEx.of(recipeManager.getRecipes())
            .remove(recipe -> recipe.getInput().getMatchingInputs().isEmpty())
            .flatMap(recipe -> {
                IRecipeIngredient input = recipe.getInput();
                if (input instanceof IRecipeIngredientFluid && recipe.getCellType() == CellType.CELL) {
                    List<IRecipeCellular> recipes = new ArrayList<>();
                    List<Fluid> matchingFluids = ((IRecipeIngredientFluid) input).getMatchingFluids();
                    List<ItemStack> output = recipe.getOutput();
                    int count = input.getCount();
                    int cellCount = recipe.getCells();
                    int duration = recipe.getDuration();
                    double energyCost = recipe.getEnergyCost();

                    recipes.add(recipe);
                    IRecipeCellular fluidCellRecipe = createCellRecipe(matchingFluids, output, count, cellCount, duration, energyCost);
                    if (fluidCellRecipe != null) recipes.add(fluidCellRecipe);
                    if (ModHandler.forestry) {
                        boolean coolFluids = matchingFluids.stream()
                            .allMatch(fluid -> fluid.getTemperature() < WAX_MELTING_POINT);
                        if (coolFluids) recipes.add(createCapsuleRecipe(matchingFluids, output, count, cellCount, duration, energyCost, ModHandler.waxCapsule));
                        recipes.add(createCapsuleRecipe(matchingFluids, output, count, cellCount, duration, energyCost, ModHandler.refractoryCapsule));
                        IRecipeCellular canRecipe = createCanRecipe(matchingFluids, output, count, cellCount, duration, energyCost);
                        if (canRecipe != null) recipes.add(canRecipe);
                    }
                    return recipes.stream();
                }
                return Stream.of(recipe);
            })
            .map(recipe -> new WrapperCellular((RecipeCellular) recipe, showEnergyCost))
            .toList();
    }

    protected abstract IRecipeCellular createCellRecipe(List<Fluid> input, List<ItemStack> output, int count, int cellCount, int duration, double energyCost);

    protected abstract IRecipeCellular createCanRecipe(List<Fluid> input, List<ItemStack> output, int count, int cellCount, int duration, double energyCost);

    protected abstract IRecipeCellular createCapsuleRecipe(List<Fluid> input, List<ItemStack> output, int count, int cellCount, int duration, double energyCost, ItemStack capsule);
}
