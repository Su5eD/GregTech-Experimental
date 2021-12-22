package mods.gregtechmod.gui;

import mods.gregtechmod.objects.items.containers.ContainerDestructorpack;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiDestructorPack extends GuiSimple<ContainerDestructorpack> {
    private static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("destructorpack");

    public GuiDestructorPack(ContainerDestructorpack container) {
        super(container);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
        this.drawString(8, 6, GtLocale.translateItem("destructorpack.name"), GuiColors.DARK_GRAY, false);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
