package mods.gregtechmod.recipe;

import mods.gregtechmod.api.recipe.*;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.util.Either;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;

public class RecipeFactory implements IRecipeFactory {

    @Override
    public IRecipeCellular makeCentrifugeRecipe(IRecipeIngredient input, List<ItemStack> outputs, int cells, int duration, CellType cellType) {
        return RecipeCentrifuge.create(input, outputs, cells, duration, cellType);
    }

    @Override
    public IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> makeAssemblerRecipe(IRecipeIngredient primaryInput, IRecipeIngredient secondaryInput, ItemStack output, int duration, double energyCost) {
        return RecipeDualInput.create(primaryInput, secondaryInput, output, duration, energyCost);
    }

    @Override
    public IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> makeAssemblerRecipe(List<IRecipeIngredient> inputs, ItemStack output, int duration, double energyCost) {
        return RecipeDualInput.create(inputs, output, duration, energyCost);
    }

    @Override
    public IRecipePulverizer makePulverizerRecipe(IRecipeIngredient input, ItemStack output) {
        return RecipePulverizer.create(input, output);
    }

    @Override
    public IRecipePulverizer makePulverizerRecipe(IRecipeIngredient input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance) {
        return RecipePulverizer.create(input, primaryOutput, secondaryOutput, chance);
    }

    @Override
    public IRecipePulverizer makePulverizerRecipe(IRecipeIngredient input, List<ItemStack> output, int chance) {
        return RecipePulverizer.create(input, output, chance);
    }

    @Override
    public IRecipePulverizer makePulverizerRecipe(IRecipeIngredient input, List<ItemStack> output, int chance, boolean overwrite, boolean universal) {
        return RecipePulverizer.create(input, output, chance, overwrite, universal);
    }

    @Override
    public IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> makeGrinderRecipe(IRecipeIngredient input, IRecipeIngredientFluid fluid, List<ItemStack> output) {
        return RecipeGrinder.create(input, fluid, output);
    }

    @Override
    public IRecipeBlastFurnace makeBlastFurnaceRecipe(List<IRecipeIngredient> input, List<ItemStack> output, int duration, double energyCost, int heat, boolean universal) {
        return RecipeBlastFurnace.create(input, output, duration, energyCost, heat, universal);
    }

    @Override
    public IRecipeCellular makeElectrolyzerRecipe(IRecipeIngredient input, List<ItemStack> outputs, int cells, int duration, double energyCost) {
        return RecipeElectrolyzer.create(input, outputs, cells, duration, energyCost);
    }

    @Override
    public IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> makeCannerRecipe(List<IRecipeIngredient> input, List<ItemStack> output, int duration, double energyCost) {
        return RecipeCanner.create(input, output, duration, energyCost);
    }

    @Override
    public IRecipeUniversal<List<IRecipeIngredient>> makeAlloySmelterRecipe(List<IRecipeIngredient> input, ItemStack output, int duration, double energyCost, boolean universal) {
        return RecipeAlloySmelter.create(input, output, duration, energyCost, universal);
    }

    @Override
    public IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> makeImplosionRecipe(IRecipeIngredient input, int tnt, List<ItemStack> output) {
        return RecipeImplosion.create(input, tnt, output);
    }

    @Override
    public IMachineRecipe<IRecipeIngredient, List<ItemStack>> makeWiremillRecipe(IRecipeIngredient input, ItemStack output, int duration, double energyCost) {
        return RecipeSimple.create(input, output, duration, energyCost);
    }

    @Override
    public IMachineRecipe<IRecipeIngredient, List<ItemStack>> makeBenderRecipe(IRecipeIngredient input, ItemStack output, int duration, double energyCost) {
        return RecipeSimple.create(input, output, duration, energyCost);
    }

    @Override
    public IMachineRecipe<IRecipeIngredient, List<ItemStack>> makeLatheRecipe(IRecipeIngredient input, List<ItemStack> output, int duration) {
        return RecipeLathe.create(input, output, duration);
    }

    @Override
    public IMachineRecipe<IRecipeIngredient, List<ItemStack>> makeLatheRecipe(IRecipeIngredient input, List<ItemStack> output, int duration, double energyCost) {
        return RecipeLathe.create(input, output, duration, energyCost);
    }

    @Override
    public IMachineRecipe<IRecipeIngredient, List<ItemStack>> makeVacuumFreezerRecipe(IRecipeIngredient input, ItemStack output, int duration, double energyCost) {
        return RecipeSimple.create(input, output, duration, energyCost);
    }

    @Override
    public IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> makeChemicalRecipe(List<IRecipeIngredient> input, ItemStack output, int duration) {
        return RecipeChemical.create(input, output, duration);
    }

    @Override
    public IRecipeFusion makeFusionRecipe(List<IRecipeIngredientFluid> input, Either<ItemStack, FluidStack> output, int duration, double energyCost, double startEnergy) {
        return RecipeFusion.create(RecipeFusion::new, input, output, duration, energyCost, startEnergy);
    }

    @Override
    public IRecipeUniversal<List<IRecipeIngredient>> makeSawmillRecipe(IRecipeIngredient input, List<ItemStack> output, int water, boolean universal) {
        return RecipeSawmill.create(input, output, water, universal);
    }

    @Override
    public IRecipeCellular makeDistillationRecipe(IRecipeIngredient input, List<ItemStack> output, int cells, int duration) {
        return RecipeDistillation.create(input, output, cells, duration);
    }

    @Override
    public IRecipePrinter makePrinterRecipe(List<IRecipeIngredient> input, @Nullable IRecipeIngredient copy, ItemStack output, int duration, double energyCost) {
        return RecipePrinter.create(input, copy, output, duration, energyCost);
    }
}
