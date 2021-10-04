package mods.gregtechmod.gui;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerAdvancedSafe;
import net.minecraft.util.ResourceLocation;

public class GuiAdvancedSafe extends GuiInventory<ContainerAdvancedSafe> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/advanced_safe.png");

    public GuiAdvancedSafe(ContainerAdvancedSafe container) {
        super(container);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
