package mods.gregtechmod.recipe;

import com.google.common.base.MoreObjects;
import mods.gregtechmod.api.recipe.IRecipeFusion;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.util.Either;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class RecipeFusion extends Recipe<List<IRecipeIngredientFluid>, Either<ItemStack, FluidStack>> implements IRecipeFusion {
    private final double startEnergy;

    protected RecipeFusion(List<IRecipeIngredientFluid> input, Either<ItemStack, FluidStack> output, int duration, double energyCost, double startEnergy) {
        super(input, output, duration, energyCost);
        this.startEnergy = startEnergy;
    }

    public static <T extends RecipeFusion> T create(RecipeFusionFactory<T> factory, List<IRecipeIngredientFluid> input, Either<ItemStack, FluidStack> output, int duration, double energyCost, double startEnergy) {
        List<IRecipeIngredientFluid> adjustedInput = RecipeUtil.adjustInputCount("fusion", input, Collections.singletonList(output), 2, 2);
        T recipe = factory.create(adjustedInput, output, duration, energyCost, startEnergy);

        RecipeUtil.validateRecipeInput("fusion", adjustedInput);
        if (output == null) {
            GregTechMod.LOGGER.warn("Tried to add a fusion recipe with null output! Invalidating...");
            recipe.invalid = true;
        }
        return recipe;
    }

    @Override
    public double getStartEnergy() {
        return this.startEnergy;
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
            .add("startEnergy", startEnergy);
    }
    
    public interface RecipeFusionFactory<T extends RecipeFusion> {
        T create(List<IRecipeIngredientFluid> input, Either<ItemStack, FluidStack> output, int duration, double energyCost, double startEnergy);
    }
}
