package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerSonictron;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiSonictron extends GuiSimple<ContainerSonictron> {
    private static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("sonictron");

    public GuiSonictron(ContainerSonictron container) {
        super(container);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
