package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.computercube.ComputerCubeGuide;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerComputerCubeGuide;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiComputerCubeGuide extends GuiComputerCubeModule<ComputerCubeGuide, ContainerComputerCubeGuide> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("computer_cube_guide");

    public GuiComputerCubeGuide(ContainerComputerCubeGuide container) {
        super(container, 226, ComputerCubeGuide.class);
    }

    @Override
    protected void drawBackground(ComputerCubeGuide module, float partialTicks, int mouseX, int mouseY) {
        readPage(module.getCurrentPage());
    }

    private void readPage(ComputerCubeGuide.GuidePage page) {
        drawString(7, 7, I18n.format(page.translationKey), 16448255, false);
        for (int i = 1; i < page.length; i++) {
            drawString(7, 7 + 8 * i, I18n.format(page.translationKey + "." + i), 16448255, false);
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
