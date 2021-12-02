package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerBasicMachine;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiAutoRecycler extends GuiBasicMachine<ContainerBasicMachine<?>> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("auto_recycler");

    public GuiAutoRecycler(ContainerBasicMachine<?> container) {
        super(container, GregtechGauge.RECYCLING);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
