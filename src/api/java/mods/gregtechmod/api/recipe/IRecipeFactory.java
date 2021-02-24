package mods.gregtechmod.api.recipe;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IRecipeFactory {
    IRecipeCellular makeCentrifugeRecipe(IRecipeIngredient input, List<ItemStack> outputs, int cells, int duration);

    default IGtMachineRecipe<List<IRecipeIngredient>, ItemStack> makeAssemblerRecipe(IRecipeIngredient input, ItemStack output, int duration, double energyCost) {
        return makeAssemblerRecipe(input, null, output, duration, energyCost);
    }

    IGtMachineRecipe<List<IRecipeIngredient>, ItemStack> makeAssemblerRecipe(IRecipeIngredient primaryInput, IRecipeIngredient secondaryInput, ItemStack output, int duration, double energyCost);

    default IRecipePulverizer makePulverizerRecipe(IRecipeIngredient input, ItemStack output) {
        return makePulverizerRecipe(input, output, ItemStack.EMPTY, 0);
    }

    IRecipePulverizer makePulverizerRecipe(IRecipeIngredient input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance);

    IRecipeGrinder makeGrinderRecipe(IRecipeIngredient input, IRecipeIngredientFluid fluid, List<ItemStack> output, int duration);

    default IRecipeBlastFurnace makeBlastFurnaceRecipe(List<IRecipeIngredient> input, List<ItemStack> output, int duration, int heat) {
        return makeBlastFurnaceRecipe(input, output, duration, 128, heat, false);
    }

    IRecipeBlastFurnace makeBlastFurnaceRecipe(List<IRecipeIngredient> input, List<ItemStack> output, int duration, double energyCost, int heat, boolean universal);

    default IRecipeCellular makeElectrolyzerRecipe(IRecipeIngredient input, List<ItemStack> outputs, int cells, int duration) {
        return makeElectrolyzerRecipe(input, outputs, cells, duration, 128);
    }

    IRecipeCellular makeElectrolyzerRecipe(IRecipeIngredient input, List<ItemStack> outputs, int cells, int duration, double energyCost);

    IGtMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> makeCannerRecipe(List<IRecipeIngredient> input, List<ItemStack> output, int duration, double energyCost);

    IGtMachineRecipe<List<IRecipeIngredient>, ItemStack> makeAlloySmelterRecipe(List<IRecipeIngredient> input, ItemStack output, int duration, double energyCost);

    IGtMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> makeImplosionRecipe(IRecipeIngredient input, int tnt, List<ItemStack> output);

    IGtMachineRecipe<IRecipeIngredient, ItemStack> makeWiremillRecipe(IRecipeIngredient input, ItemStack output, int duration, double energyCost);

    IGtMachineRecipe<IRecipeIngredient, ItemStack> makeBenderRecipe(IRecipeIngredient input, ItemStack output, int duration, double energyCost);

    IGtMachineRecipe<IRecipeIngredient, List<ItemStack>> makeLatheRecipe(IRecipeIngredient input, List<ItemStack> output, int duration, double energyCost);

    IGtMachineRecipe<IRecipeIngredient, ItemStack> makeVacuumFreezerRecipe(IRecipeIngredient input, ItemStack output, int duration, double energyCost);

    IGtMachineRecipe<List<IRecipeIngredient>, ItemStack> makeChemicalRecipe(List<IRecipeIngredient> input, ItemStack output, int duration);

    IRecipeFusion<IRecipeIngredientFluid, FluidStack> makeFluidFusionRecipe(List<IRecipeIngredientFluid> input, FluidStack output, int duration, double energyCost, double startEnergy);

    IRecipeFusion<IRecipeIngredient, ItemStack> makeSolidFusionRecipe(List<IRecipeIngredient> input, ItemStack output, int duration, double energyCost, double startEnergy);

    IRecipeSawmill makeSawmillRecipe(IRecipeIngredient input, List<ItemStack> output, int water, boolean universal);
}
