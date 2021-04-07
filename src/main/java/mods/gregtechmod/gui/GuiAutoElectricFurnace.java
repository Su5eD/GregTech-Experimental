package mods.gregtechmod.gui;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.TileEntityAutoElectricFurnace;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerBasicMachine;
import net.minecraft.util.ResourceLocation;

public class GuiAutoElectricFurnace extends GuiBasicMachine<ContainerBasicMachine<TileEntityAutoElectricFurnace>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/auto_electric_furnace.png");

    public GuiAutoElectricFurnace(ContainerBasicMachine<TileEntityAutoElectricFurnace> container) {
        super(TEXTURE, container, GregtechGauge.SMELTING);
    }
}
