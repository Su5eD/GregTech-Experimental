package mods.gregtechmod.gui;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.base.TileEntityBasicMachine;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerBasicMachine;
import net.minecraft.util.ResourceLocation;

public class GuiAutoElectricFurnace extends GuiBasicMachine<ContainerBasicMachine<TileEntityBasicMachine<?, ?, ?, ?>>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/auto_electric_furnace.png");

    public GuiAutoElectricFurnace(ContainerBasicMachine<TileEntityBasicMachine<?, ?, ?, ?>> container) {
        super(TEXTURE, container, GregtechGauge.SMELTING);
    }
}
