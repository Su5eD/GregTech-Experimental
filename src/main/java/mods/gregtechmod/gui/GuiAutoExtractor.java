package mods.gregtechmod.gui;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.TileEntityAutoExtractor;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerBasicMachine;
import net.minecraft.util.ResourceLocation;

public class GuiAutoExtractor extends GuiBasicMachine<ContainerBasicMachine<TileEntityAutoExtractor>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/auto_extractor.png");

    public GuiAutoExtractor(ContainerBasicMachine<TileEntityAutoExtractor> container) {
        super(TEXTURE, container, GregtechGauge.EXTRACTING);
    }
}