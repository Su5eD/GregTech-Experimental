package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiAutoCanner extends GuiBasicMachine<ContainerBasicMachine<?>> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("auto_canner");

    public GuiAutoCanner(ContainerBasicMachine<?> container) {
        super(container, GregtechGauge.CANNING);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
