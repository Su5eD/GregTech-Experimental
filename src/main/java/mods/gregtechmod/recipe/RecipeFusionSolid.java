package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.util.Either;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class RecipeFusionSolid extends RecipeFusion {

    protected RecipeFusionSolid(List<IRecipeIngredientFluid> input, Either<ItemStack, FluidStack> output, int duration, double energyCost, double startEnergy) {
        super(input, output, duration, energyCost, startEnergy);
    }

    @JsonCreator
    public static RecipeFusionSolid create(@JsonProperty(value = "input", required = true) List<IRecipeIngredientFluid> input,
                                           @JsonProperty(value = "output", required = true) ItemStack output,
                                           @JsonProperty(value = "duration", required = true) int duration,
                                           @JsonProperty(value = "energyCost", required = true) double energyCost,
                                           @JsonProperty(value = "startEnergy", required = true) double startEnergy) {
        return create(RecipeFusionSolid::new, input, Either.left(output), duration, energyCost, startEnergy);
    }
}
