package mods.gregtechmod.recipe;

import mods.gregtechmod.api.recipe.IRecipeCellular;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.Collection;

public abstract class RecipeCellular extends Recipe<IRecipeIngredient, Collection<ItemStack>> implements IRecipeCellular {
    private final int cells;

    protected RecipeCellular(IRecipeIngredient input, Collection<ItemStack> output, int cells, int duration, double energyCost) {
        super(input, output, duration, energyCost);
        this.cells = cells;
    }

    @Override
    public int getCells() {
        return this.cells;
    }

    @Override
    public String toString() {
        return getClass().getName()+"{input="+this.input+",output="+this.output+",duration="+this.duration+",energyCost="+this.energyCost+",cells="+this.cells+"}";
    }
}
