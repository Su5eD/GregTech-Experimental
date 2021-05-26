package mods.gregtechmod.gui;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityLathe;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import net.minecraft.util.ResourceLocation;

public class GuiLathe extends GuiBasicMachine<ContainerBasicMachine<TileEntityLathe>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/lathe.png");

    public GuiLathe(ContainerBasicMachine<TileEntityLathe> container) {
        super(TEXTURE, container, GregtechGauge.TURNING);
    }
}
