package mods.gregtechmod.inventory.invslot;

import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricCraftingTable;

public class GtSlotCraftingGrid extends GtSlot {

    public GtSlotCraftingGrid(TileEntityElectricCraftingTable base, String name, int count) {
        super(base, name, Access.NONE, count);
    }

    @Override
    public boolean canInput() {
        return isPatternMode();
    }

    @Override
    public boolean canOutput() {
        return isPatternMode();
    }

    private boolean isPatternMode() {
        return ((TileEntityElectricCraftingTable) this.base).getCraftingMode() == TileEntityElectricCraftingTable.CraftingMode.PATTERN;
    }
}
