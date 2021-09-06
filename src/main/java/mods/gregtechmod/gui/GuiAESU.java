package mods.gregtechmod.gui;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerEnergyStorage;
import net.minecraft.util.ResourceLocation;

public class GuiAESU extends GuiEnergyStorage {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/aesu.png");

    public GuiAESU(ContainerEnergyStorage container) {
        super(container, 97, 55);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
