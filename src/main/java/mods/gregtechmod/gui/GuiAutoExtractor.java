package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiAutoExtractor extends GuiBasicMachine<ContainerBasicMachine<?>> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("auto_extractor");

    public GuiAutoExtractor(ContainerBasicMachine<?> container) {
        super(container, GregtechGauge.EXTRACTING);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
