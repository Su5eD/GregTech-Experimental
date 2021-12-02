package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiAutoElectricFurnace extends GuiBasicMachine<ContainerBasicMachine<?>> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("auto_electric_furnace");

    public GuiAutoElectricFurnace(ContainerBasicMachine<?> container) {
        super(container, GregtechGauge.SMELTING);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
