package mods.gregtechmod.gui;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityAutoRecycler;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import net.minecraft.util.ResourceLocation;

public class GuiAutoRecycler extends GuiBasicMachine<ContainerBasicMachine<TileEntityAutoRecycler>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/auto_recycler.png");

    public GuiAutoRecycler(ContainerBasicMachine<TileEntityAutoRecycler> container) {
        super(TEXTURE, container, GregtechGauge.RECYCLING);
    }
}
