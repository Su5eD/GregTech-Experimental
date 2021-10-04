package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerAdvancedPump;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiAdvancedPump extends GuiInventory<ContainerAdvancedPump> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("advanced_pump");

    public GuiAdvancedPump(ContainerAdvancedPump container) {
        super(container);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
