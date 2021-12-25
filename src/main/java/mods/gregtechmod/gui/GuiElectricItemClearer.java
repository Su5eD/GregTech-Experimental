package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricBufferSmall;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiElectricItemClearer extends GuiElectricBuffer<ContainerElectricBufferSmall<?>> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("electric_item_clearer");
    
    public GuiElectricItemClearer(ContainerElectricBufferSmall<?> container) {
        super(container);
    }
    
    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
