package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerScrapboxinator;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiScrapboxinator extends GuiElectricBuffer<ContainerScrapboxinator> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("scrapboxinator");
    
    public GuiScrapboxinator(ContainerScrapboxinator container) {
        super(container);
    }
    
    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
