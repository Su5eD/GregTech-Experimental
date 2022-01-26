package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricCraftingTable;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiElectricCraftingTable extends GuiSimple<ContainerElectricCraftingTable> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("electric_crafting_table");

    public GuiElectricCraftingTable(ContainerElectricCraftingTable container) {
        super(container);
        
        addIconCycle(120, 4, TEXTURE, 0, 184, container.base::getThroughPutMode);
        addIconCycle(120, 40, TEXTURE, 0, 166, container.base::getCraftingMode);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
