package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiBender extends GuiBasicMachine<ContainerBasicMachine<?>> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("bender");

    public GuiBender(ContainerBasicMachine<?> container) {
        super(container, GregtechGauge.BENDING);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
