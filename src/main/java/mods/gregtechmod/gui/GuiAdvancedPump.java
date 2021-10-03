package mods.gregtechmod.gui;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerAdvancedPump;
import net.minecraft.util.ResourceLocation;

public class GuiAdvancedPump extends GuiInventory<ContainerAdvancedPump> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/advanced_pump.png");

    public GuiAdvancedPump(ContainerAdvancedPump container) {
        super(container);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
