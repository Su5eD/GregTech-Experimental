package mods.gregtechmod.api.recipe;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IRecipeFactory {
    default IRecipeCellular makeCentrifugeRecipe(IRecipeIngredient input, List<ItemStack> outputs, int cells, int duration) {
        return makeCentrifugeRecipe(input, outputs, cells, duration, CellType.CELL);
    }

    IRecipeCellular makeCentrifugeRecipe(IRecipeIngredient input, List<ItemStack> outputs, int cells, int duration, CellType cellType);

    IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> makeAssemblerRecipe(IRecipeIngredient primaryInput, IRecipeIngredient secondaryInput, ItemStack output, int duration, double energyCost);

    IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> makeAssemblerRecipe(List<IRecipeIngredient> inputs, ItemStack output, int duration, double energyCost);

    IRecipePulverizer makePulverizerRecipe(IRecipeIngredient input, ItemStack output);

    IRecipePulverizer makePulverizerRecipe(IRecipeIngredient input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance);
    
    IRecipePulverizer makePulverizerRecipe(IRecipeIngredient input, List<ItemStack> output, int chance);
    
    IRecipePulverizer makePulverizerRecipe(IRecipeIngredient input, List<ItemStack> output, int chance, boolean overwrite, boolean universal);

    IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> makeGrinderRecipe(IRecipeIngredient input, IRecipeIngredientFluid fluid, List<ItemStack> output);

    default IRecipeBlastFurnace makeBlastFurnaceRecipe(List<IRecipeIngredient> input, List<ItemStack> output, int duration, int heat) {
        return makeBlastFurnaceRecipe(input, output, duration, 128, heat, false);
    }

    IRecipeBlastFurnace makeBlastFurnaceRecipe(List<IRecipeIngredient> input, List<ItemStack> output, int duration, double energyCost, int heat, boolean universal);

    default IRecipeCellular makeElectrolyzerRecipe(IRecipeIngredient input, List<ItemStack> outputs, int cells, int duration) {
        return makeElectrolyzerRecipe(input, outputs, cells, duration, 128);
    }

    IRecipeCellular makeElectrolyzerRecipe(IRecipeIngredient input, List<ItemStack> outputs, int cells, int duration, double energyCost);

    IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> makeCannerRecipe(List<IRecipeIngredient> input, List<ItemStack> output, int duration, double energyCost);

    IRecipeUniversal<List<IRecipeIngredient>> makeAlloySmelterRecipe(List<IRecipeIngredient> input, ItemStack output, int duration, double energyCost, boolean universal);

    IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> makeImplosionRecipe(IRecipeIngredient input, int tnt, List<ItemStack> output);

    IMachineRecipe<IRecipeIngredient, List<ItemStack>> makeWiremillRecipe(IRecipeIngredient input, ItemStack output, int duration, double energyCost);

    IMachineRecipe<IRecipeIngredient, List<ItemStack>> makeBenderRecipe(IRecipeIngredient input, ItemStack output, int duration, double energyCost);

    IMachineRecipe<IRecipeIngredient, List<ItemStack>> makeLatheRecipe(IRecipeIngredient input, List<ItemStack> output, int duration);

    IMachineRecipe<IRecipeIngredient, List<ItemStack>> makeLatheRecipe(IRecipeIngredient input, List<ItemStack> output, int duration, double energyCost);

    IMachineRecipe<IRecipeIngredient, List<ItemStack>> makeVacuumFreezerRecipe(IRecipeIngredient input, ItemStack output, int duration, double energyCost);

    IMachineRecipe<List<IRecipeIngredient>, List<ItemStack>> makeChemicalRecipe(List<IRecipeIngredient> input, ItemStack output, int duration);

    IRecipeFusion<IRecipeIngredientFluid, FluidStack> makeFluidFusionRecipe(List<IRecipeIngredientFluid> input, FluidStack output, int duration, double energyCost, double startEnergy);

    IRecipeFusion<IRecipeIngredientFluid, ItemStack> makeSolidFusionRecipe(List<IRecipeIngredientFluid> input, ItemStack output, int duration, double energyCost, double startEnergy);

    IRecipeUniversal<List<IRecipeIngredient>> makeSawmillRecipe(IRecipeIngredient input, List<ItemStack> output, int water, boolean universal);
}
