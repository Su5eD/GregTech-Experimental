package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerEnergyStorage;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiAESU extends GuiEnergyStorage {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("aesu");

    public GuiAESU(ContainerEnergyStorage container) {
        super(container, 97, 55);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
