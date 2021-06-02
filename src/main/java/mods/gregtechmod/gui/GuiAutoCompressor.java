package mods.gregtechmod.gui;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityAutoCompressor;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import net.minecraft.util.ResourceLocation;

public class GuiAutoCompressor extends GuiBasicMachine<ContainerBasicMachine<TileEntityAutoCompressor>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/auto_compressor.png");

    public GuiAutoCompressor(ContainerBasicMachine<TileEntityAutoCompressor> container) {
        super(TEXTURE, container, GregtechGauge.COMPRESSING);
    }
}
