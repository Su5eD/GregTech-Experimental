package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredientFluid;
import mods.gregtechmod.api.util.Either;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class RecipeFusionFluid extends RecipeFusion {

    protected RecipeFusionFluid(List<IRecipeIngredientFluid> input, Either<ItemStack, FluidStack> output, int duration, double energyCost, double startEnergy) {
        super(input, output, duration, energyCost, startEnergy);
    }

    @JsonCreator
    public static RecipeFusionFluid create(@JsonProperty(value = "input", required = true) List<IRecipeIngredientFluid> input,
                                           @JsonProperty(value = "output", required = true) FluidStack output,
                                           @JsonProperty(value = "duration", required = true) int duration,
                                           @JsonProperty(value = "energyCost", required = true) double energyCost,
                                           @JsonProperty(value = "startEnergy", required = true) double startEnergy) {
        return create(RecipeFusionFluid::new, input, Either.right(output), duration, energyCost, startEnergy);
    }
}
