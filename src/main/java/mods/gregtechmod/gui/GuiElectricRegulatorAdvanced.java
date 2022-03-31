package mods.gregtechmod.gui;

import mods.gregtechmod.objects.blocks.teblocks.container.ContainerElectricRegulatorAdvanced;
import mods.gregtechmod.util.GtUtil;
import net.minecraft.util.ResourceLocation;

public class GuiElectricRegulatorAdvanced extends GuiSimple<ContainerElectricRegulatorAdvanced> {
    public static final ResourceLocation TEXTURE = GtUtil.getGuiTexture("electric_regulator_advanced");

    public GuiElectricRegulatorAdvanced(ContainerElectricRegulatorAdvanced container) {
        super(container);

        addVerticalIconCycle(7, 62, 58, () -> container.base.outputEnergy);
    }

    @Override
    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                int index = x + y * 3;
                int xPos = 120 + x * 17;
                int yPos = 9 + y * 17;

                drawString(xPos, yPos, Integer.toString(this.container.base.slotIndices[index]), GuiColors.WHITE, false);
            }
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
