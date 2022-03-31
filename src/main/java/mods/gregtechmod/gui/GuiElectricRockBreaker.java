package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricRockBreaker;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiElectricRockBreaker extends GuiElectricBuffer<ContainerElectricRockBreaker> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("electric_rock_breaker");

    public GuiElectricRockBreaker(ContainerElectricRockBreaker container) {
        super(container);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
