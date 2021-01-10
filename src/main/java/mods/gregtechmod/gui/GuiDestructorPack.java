package mods.gregtechmod.gui;

import ic2.core.GuiIC2;
import ic2.core.init.Localization;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.items.containers.ContainerDestructorpack;
import net.minecraft.util.ResourceLocation;

public class GuiDestructorPack extends GuiIC2<ContainerDestructorpack> {

    public GuiDestructorPack(ContainerDestructorpack container) {
        super(container, 166);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.drawString(8, 6, Localization.translate("gregtechmod.item.destructorpack.name"), 4210752, false);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Reference.MODID, "textures/gui/destructorpack.png");
    }
}
