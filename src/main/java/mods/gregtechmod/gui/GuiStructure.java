package mods.gregtechmod.gui;

import ic2.core.ContainerBase;
import mods.gregtechmod.objects.blocks.teblocks.struct.TileEntityStructureBase;
import mods.gregtechmod.util.GtLocale;

public abstract class GuiStructure<T extends ContainerBase<? extends TileEntityStructureBase<?, ?, ?, ?, ?>>> extends GuiInventory<T> {

    public GuiStructure(T container) {
        super(container);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);

        if (container.base.structure.isValid()) doWhenValid();
        else doWhenInvalid();
    }

    protected void doWhenValid() {}

    protected void doWhenInvalid() {
        drawString(8, ySize - 103, GtLocale.translateInfo("structure_invalid"), GuiColors.DARK_GRAY, false);
    }
}
