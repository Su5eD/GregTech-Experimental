package mods.gregtechmod.gui;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.TileEntityAssembler;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerBasicMachine;
import net.minecraft.util.ResourceLocation;

public class GuiAssembler extends GuiBasicMachine<ContainerBasicMachine<TileEntityAssembler>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/assembler.png");

    public GuiAssembler(ContainerBasicMachine<TileEntityAssembler> container) {
        super(TEXTURE, container, GregtechGauge.ASSEMBLING);
    }
}
