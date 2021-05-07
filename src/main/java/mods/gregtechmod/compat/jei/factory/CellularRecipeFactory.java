package mods.gregtechmod.compat.jei.factory;

import mods.gregtechmod.api.recipe.CellType;
import mods.gregtechmod.api.recipe.IRecipeCellular;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerCellular;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.compat.jei.wrapper.WrapperCellular;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.base.TileEntityIndustrialCentrifugeBase;
import mods.gregtechmod.recipe.RecipeCellular;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.ProfileDelegate;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class CellularRecipeFactory {

    protected CellularRecipeFactory() {}
    public List<? extends WrapperCellular> getCellularRecipes(IGtRecipeManagerCellular recipeManager, boolean showEnergyCost) {
        return recipeManager.getRecipes()
                .stream()
                .filter(recipe -> !recipe.getInput().getMatchingInputs().isEmpty())
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
                                    .allMatch(fluid -> fluid.getTemperature() < 310.15); // melting point of wax in kelvin
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
                .collect(Collectors.toList());
    }

    protected abstract IRecipeCellular createCellRecipe(List<Fluid> input, List<ItemStack> output, int count, int cellCount, int duration, double energyCost);

    protected abstract IRecipeCellular createCanRecipe(List<Fluid> input, List<ItemStack> output, int count, int cellCount, int duration, double energyCost);

    protected abstract IRecipeCellular createCapsuleRecipe(List<Fluid> input, List<ItemStack> output, int count, int cellCount, int duration, double energyCost, ItemStack capsule);

    protected List<ItemStack> getFluidContainers(List<Fluid> input, ItemStack type) {
        return input.stream()
                .map(fluid -> GtUtil.getFluidContainer(type, fluid))
                .collect(Collectors.toList());
    }

    protected int getDuration(int duration, ItemStack input, List<ItemStack> output) {
        TileEntityIndustrialCentrifugeBase.CellAdditionResult result = TileEntityIndustrialCentrifugeBase.addCellsToOutput(input, output);
        if (result == TileEntityIndustrialCentrifugeBase.CellAdditionResult.MELT) return (int) (duration * 1.5);
        else if (result != TileEntityIndustrialCentrifugeBase.CellAdditionResult.FAIL) return duration;

        return -1;
    }

    protected List<ItemStack> getCells(List<Fluid> input) {
        return input.stream()
                .map(fluid -> ProfileDelegate.getCell(fluid.getName()))
                .collect(Collectors.toList());
    }
}
