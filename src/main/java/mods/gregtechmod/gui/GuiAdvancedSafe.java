package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerAdvancedSafe;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiAdvancedSafe extends GuiSimple<ContainerAdvancedSafe> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("advanced_safe");

    public GuiAdvancedSafe(ContainerAdvancedSafe container) {
        super(container);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
