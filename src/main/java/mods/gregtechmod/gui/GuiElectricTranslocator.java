package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricTranslocator;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiElectricTranslocator<T extends ContainerElectricTranslocator<?>> extends GuiSimple<T> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("electric_translocator");

    public GuiElectricTranslocator(T container) {
        super(container);

        addVerticalIconCycle(7, 62, 58, () -> container.base.outputEnergy);
        addVerticalIconCycle(25, 62, 166, () -> container.base.invertFilter);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
