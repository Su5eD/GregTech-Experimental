package mods.gregtechmod.gui;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import net.minecraft.util.ResourceLocation;

public class GuiAutoElectricFurnace extends GuiBasicMachine<ContainerBasicMachine<?>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/auto_electric_furnace.png");

    public GuiAutoElectricFurnace(ContainerBasicMachine<?> container) {
        super(container, GregtechGauge.SMELTING);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
