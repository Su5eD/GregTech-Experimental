package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricBuffer;
import mods.gregtechmod.objects.blocks.teblocks.inv.TileEntityElectricSorterBase;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiElectricSorter<T extends ContainerElectricBuffer<? extends TileEntityElectricSorterBase>> extends GuiElectricBuffer<T> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("electric_sorter");

    public GuiElectricSorter(T container) {
        super(container);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);

        drawString(138, 7, this.container.base.getOppositeFacing().name(), GuiColors.WHITE, false);
        drawString(100, 65, this.container.base.getTargetFacing().name(), GuiColors.WHITE, false);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
