package mods.gregtechmod.compat.crafttweaker;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import mods.gregtechmod.api.recipe.CellType;
import mods.gregtechmod.api.util.Reference;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenProperty;

@ModOnly(Reference.MODID)
@ZenClass("mods.gregtechmod.CellType")
@ZenRegister
public enum CTCellType {
    @ZenProperty CELL(CellType.CELL),
    @ZenProperty FUEL_CAN(CellType.FUEL_CAN);
    
    private final CellType cellType;

    CTCellType(CellType cellType) {
        this.cellType = cellType;
    }

    public CellType getCellType() {
        return cellType;
    }
}
