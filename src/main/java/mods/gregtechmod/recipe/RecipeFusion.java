package mods.gregtechmod.recipe;

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
    public String toString() {
        return "RecipeFusion{input="+this.input+",output="+this.output+",duration="+this.duration+",energyCost="+this.energyCost+",startEnergy="+this.startEnergy+"}";
    }
}
