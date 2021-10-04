package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiAssembler extends GuiBasicMachine<ContainerBasicMachine<?>> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("assembler");

    public GuiAssembler(ContainerBasicMachine<?> container) {
        super(container, GregtechGauge.ASSEMBLING);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
