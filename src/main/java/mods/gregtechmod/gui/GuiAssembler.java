package mods.gregtechmod.gui;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import net.minecraft.util.ResourceLocation;

public class GuiAssembler extends GuiBasicMachine<ContainerBasicMachine<?>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/assembler.png");

    public GuiAssembler(ContainerBasicMachine<?> container) {
        super(TEXTURE, container, GregtechGauge.ASSEMBLING);
    }
}
