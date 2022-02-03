package mods.gregtechmod.recipe;

import com.google.common.base.MoreObjects;
import mods.gregtechmod.api.recipe.IRecipeFusion;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;

import java.util.List;

public abstract class RecipeFusion<I extends IRecipeIngredient, T> extends Recipe<List<I>, T> implements IRecipeFusion<I, T> {
    private final double startEnergy;

    protected RecipeFusion(List<I> input, T output, int duration, double energyCost, double startEnergy) {
        super(input, output, duration, energyCost);
        this.startEnergy = Math.max(Math.min(startEnergy, 100000000), 0);
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
}
