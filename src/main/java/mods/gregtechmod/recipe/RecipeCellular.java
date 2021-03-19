package mods.gregtechmod.recipe;

import mods.gregtechmod.api.recipe.CellType;
import mods.gregtechmod.api.recipe.IRecipeCellular;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.Collection;

public abstract class RecipeCellular extends Recipe<IRecipeIngredient, Collection<ItemStack>> implements IRecipeCellular {
    private final int cells;
    private final CellType cellType;

    protected RecipeCellular(IRecipeIngredient input, Collection<ItemStack> output, int cells, int duration, double energyCost, CellType cellType) {
        super(input, output, duration, energyCost);
        this.cells = cells;
        this.cellType = cellType;
    }

    @Override
    public int getCells() {
        return this.cells;
    }

    @Override
    public CellType getCellType() {
        return this.cellType;
    }

    @Override
    public String toString() {
        return getClass().getName()+"{input="+this.input+",output="+this.output+",duration="+this.duration+",energyCost="+this.energyCost+",cells="+this.cells+",cellType="+this.cellType+"}";
    }
}
