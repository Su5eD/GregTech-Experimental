package mods.gregtechmod.gui;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.TileEntityAutoMacerator;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerBasicMachine;
import net.minecraft.util.ResourceLocation;

public class GuiAutoMacerator extends GuiBasicMachine<ContainerBasicMachine<TileEntityAutoMacerator>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/auto_macerator.png");

    public GuiAutoMacerator(ContainerBasicMachine<TileEntityAutoMacerator> container) {
        super(TEXTURE, container, GregtechGauge.MACERATING);
    }
}
