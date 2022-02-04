package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricBufferSmall;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiCropHarvestor extends GuiElectricBuffer<ContainerElectricBufferSmall<?>> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("crop_harvestor");

    public GuiCropHarvestor(ContainerElectricBufferSmall<?> container) {
        super(container);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
