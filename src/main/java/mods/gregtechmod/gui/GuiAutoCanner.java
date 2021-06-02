package mods.gregtechmod.gui;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityAutoCanner;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import net.minecraft.util.ResourceLocation;

public class GuiAutoCanner extends GuiBasicMachine<ContainerBasicMachine<TileEntityAutoCanner>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/auto_canner.png");

    public GuiAutoCanner(ContainerBasicMachine<TileEntityAutoCanner> container) {
        super(TEXTURE, container, GregtechGauge.CANNING);
    }
}