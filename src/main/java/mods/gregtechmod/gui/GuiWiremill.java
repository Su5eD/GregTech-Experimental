package mods.gregtechmod.gui;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import net.minecraft.util.ResourceLocation;

public class GuiWiremill extends GuiBasicMachine<ContainerBasicMachine<?>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/wiremill.png");

    public GuiWiremill(ContainerBasicMachine<?> container) {
        super(TEXTURE, container, GregtechGauge.EXTRUDING);
    }
}
