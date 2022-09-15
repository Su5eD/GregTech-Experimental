package mods.gregtechmod.api.recipe;

import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.util.Either;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
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

    IRecipeFusion makeFusionRecipe(List<IRecipeIngredientFluid> input, Either<ItemStack, FluidStack> output, int duration, double energyCost, double startEnergy);

    IRecipeUniversal<List<IRecipeIngredient>> makeSawmillRecipe(IRecipeIngredient input, List<ItemStack> output, int water, boolean universal);
    
    IRecipeCellular makeDistillationRecipe(IRecipeIngredient input, List<ItemStack> output, int cells, int duration);
    
    IRecipePrinter makePrinterRecipe(List<IRecipeIngredient> input, @Nullable IRecipeIngredient copy, ItemStack output, int duration, double energyCost);
}
