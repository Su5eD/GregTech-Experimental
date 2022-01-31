package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricTypeSorter;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiElectricTypeSorter extends GuiElectricSorter<ContainerElectricTypeSorter> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("electric_type_sorter");
    
    public GuiElectricTypeSorter(ContainerElectricTypeSorter container) {
        super(container);
        
        addIconCycle(70, 22, TEXTURE, 0, 166, container.base::getType);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
