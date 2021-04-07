package mods.gregtechmod.compat.jei;

import ic2.core.util.StackUtil;
import mods.gregtechmod.api.recipe.CellType;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IMachineRecipe;
import mods.gregtechmod.api.recipe.IRecipeCellular;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.recipe.manager.IGtRecipeManagerBasic;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.compat.jei.wrapper.WrapperBasicMachine;
import mods.gregtechmod.compat.jei.wrapper.WrapperCentrifuge;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.TileEntityIndustrialCentrifuge;
import mods.gregtechmod.recipe.RecipeCentrifuge;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientItemStack;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.ProfileDelegate;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecipeMaker {
    public static List<WrapperCentrifuge> getCentrifugeRecipes() {
        return GtRecipes.industrialCentrifuge.getRecipes()
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

                        recipes.add(recipe);
                        IRecipeCellular fluidCellRecipe = createCellRecipe(matchingFluids, output, count, cellCount, duration);
                        if (fluidCellRecipe != null) recipes.add(fluidCellRecipe);
                        if (ModHandler.forestry) {
                            boolean coolFluids = matchingFluids.stream()
                                    .allMatch(fluid -> fluid.getTemperature() < 310.15); // melting point of wax in kelvin
                            if (coolFluids) recipes.add(createCapsuleRecipe(matchingFluids, output, count, cellCount, duration, ModHandler.waxCapsule));
                            recipes.add(createCapsuleRecipe(matchingFluids, output, count, cellCount, duration, ModHandler.refractoryCapsule));
                            IRecipeCellular canRecipe = createCanRecipe(matchingFluids, output, count, cellCount, duration);
                            if (canRecipe != null) recipes.add(canRecipe);
                        }
                        return recipes.stream();
                    }
                    return Stream.of(recipe);
                })
                .map(recipe -> new WrapperCentrifuge((RecipeCentrifuge) recipe))
                .collect(Collectors.toList());
    }

    public static List<WrapperBasicMachine> getBasicMachineRecipes(IGtRecipeManagerBasic<IRecipeIngredient, ItemStack, IMachineRecipe<IRecipeIngredient, List<ItemStack>>> manager) {
        return manager.getRecipes()
                .stream()
                .filter(recipe -> !recipe.getInput().getMatchingInputs().isEmpty())
                .map(WrapperBasicMachine::new)
                .collect(Collectors.toList());
    }

    @Nullable
    private static IRecipeCellular createCellRecipe(List<Fluid> input, List<ItemStack> output, int count, int cellCount, int duration) {
        List<ItemStack> fluidCells = input.stream()
                .map(fluid -> ProfileDelegate.getCell(fluid.getName()))
                .collect(Collectors.toList());
        List<ItemStack> recipeOutput = GtUtil.copyList(output);

        TileEntityIndustrialCentrifuge.CellAdditionResult result = TileEntityIndustrialCentrifuge.addCellsToOutput(StackUtil.copyWithSize(fluidCells.get(0), count - cellCount), recipeOutput);
        if (result == TileEntityIndustrialCentrifuge.CellAdditionResult.FAIL) return null;
        else if (result == TileEntityIndustrialCentrifuge.CellAdditionResult.MELT) duration *= 1.5;

        return RecipeCentrifuge.create(RecipeIngredientItemStack.create(fluidCells, count), recipeOutput, Math.max(cellCount - count, 0), duration, CellType.CELL);
    }

    @Nullable
    private static IRecipeCellular createCanRecipe(List<Fluid> input, List<ItemStack> output, int count, int cellCount, int duration) {
        List<ItemStack> fluidCells = input.stream()
                .map(fluid -> GtUtil.getFluidContainer(ModHandler.can, fluid))
                .collect(Collectors.toList());
        List<ItemStack> recipeOutput = GtUtil.copyList(output);

        TileEntityIndustrialCentrifuge.CellAdditionResult result = TileEntityIndustrialCentrifuge.addCellsToOutput(StackUtil.copyWithSize(fluidCells.get(0), count), recipeOutput);
        if (result == TileEntityIndustrialCentrifuge.CellAdditionResult.FAIL) return null;
        else if (result == TileEntityIndustrialCentrifuge.CellAdditionResult.MELT) duration *= 1.5;

        return RecipeCentrifuge.create(RecipeIngredientItemStack.create(fluidCells, count), recipeOutput, cellCount, duration, CellType.CELL);
    }

    private static IRecipeCellular createCapsuleRecipe(List<Fluid> input, List<ItemStack> output, int count, int cellCount, int duration, ItemStack capsule) {
        List<ItemStack> capsules = input.stream()
                .map(fluid -> GtUtil.getFluidContainer(capsule, fluid))
                .collect(Collectors.toList());
        List<ItemStack> recipeOutput = GtUtil.copyList(output);

        TileEntityIndustrialCentrifuge.addCellsToOutput(StackUtil.copyWithSize(capsules.get(0), count), recipeOutput);
        return RecipeCentrifuge.create(RecipeIngredientItemStack.create(capsules, count), recipeOutput, cellCount, duration, CellType.CELL);
    }
}
