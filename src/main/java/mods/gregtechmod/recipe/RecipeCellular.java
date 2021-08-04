package mods.gregtechmod.recipe;

import com.google.common.base.MoreObjects;
import mods.gregtechmod.api.recipe.CellType;
import mods.gregtechmod.api.recipe.IRecipeCellular;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class RecipeCellular extends Recipe<IRecipeIngredient, List<ItemStack>> implements IRecipeCellular {
    private final int cells;
    private final CellType cellType;

    protected RecipeCellular(IRecipeIngredient input, List<ItemStack> output, int cells, int duration, double energyCost, CellType cellType) {
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
        return MoreObjects.toStringHelper(this)
                .add("input", input)
                .add("output", output)
                .add("duration", duration)
                .add("energyCost", energyCost)
                .add("cells", cells)
                .add("cellType", cellType)
                .toString();
    }
}
